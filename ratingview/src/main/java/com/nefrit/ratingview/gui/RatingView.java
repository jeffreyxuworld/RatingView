package com.nefrit.ratingview.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.nefrit.ratingview.R;
import com.nefrit.ratingview.model.Scale;

import java.util.Arrays;
import java.util.List;

public class RatingView extends LinearLayout {

	private RatingItemView[] ratingItemViews;

	private int scalesCount;
	private boolean needStars;

	private int maxValue;

	public RatingView(Context context) {
		super(context);

		scalesCount = 0;

		init(context);
	}

	public RatingView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);

		if (attrs != null) {
			TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.RatingView);
			needStars = attributes.getBoolean(R.styleable.RatingView_show_image, false);

			attributes.recycle();
		}

		init(context);
	}

	private void init(Context context) {

		setOrientation(LinearLayout.VERTICAL);

	}

	public void setScales(List<Scale> scales) {
		removeAllViews();
		scalesCount = scales.size();

		if (scales.isEmpty()) {
			return;
		}

		ratingItemViews = new RatingItemView[scalesCount];

		maxValue = scales.get(0).getCount();

		for (Scale scale : scales) {
			if (scale.getCount() > maxValue) {
				maxValue = scale.getCount();
			}
		}

		for (int i = 0; i < scalesCount ; i++) {
			RatingItemView ratingItemView = new RatingItemView(getContext());
			ratingItemViews[i] = ratingItemView;
			addView(ratingItemView);
			ratingItemView.setScale(scales.get(i));
			if (needStars) {
				ratingItemView.showStar();
			} else {
				ratingItemView.hideStar();
			}
		}

		post(new Runnable() {
			@Override
			public void run() {
				updateWidth();
			}
		});
	}

	public void setScales(Scale[] scales) {
		setScales(Arrays.asList(scales));
	}

	int getMaxValue() {
		return maxValue;
	}

	public RatingItemView getRatingItem(int position) {
		return ratingItemViews[position];
	}

	public int getScalesCount() {
		return scalesCount;
	}

	private void updateWidth() {
		int maxWidth = ratingItemViews[0].getValueTextView().getWidth();
		int maxCountWidth = ratingItemViews[0].getValueTextView().getWidth();

		for (RatingItemView ratingItemView : ratingItemViews) {
			if (ratingItemView.getValueTextView().getWidth() > maxWidth) {
				maxWidth = ratingItemView.getValueTextView().getWidth();
			}
			if (ratingItemView.getCountTextView().getWidth() > maxCountWidth) {
				maxCountWidth = ratingItemView.getCountTextView().getWidth();
			}
		}

		for (RatingItemView ratingItemView : ratingItemViews) {
			ratingItemView.getValueTextView().setWidth(maxWidth);
			ratingItemView.getCountTextView().setWidth(maxCountWidth);
		}
	}

	public List<RatingItemView> getItems() {
		return Arrays.asList(ratingItemViews);
	}
}