package com.afmobi.palmchat.ui.customview.blurredview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.afmobigroup.gphone.R;

public class BlurImageView extends ImageView {

	private final int BLUR_RADIUS = 5;
	private int mHeightOfWith = 0;

	public BlurImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context,attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 */
	private void initWithContext(Context context, AttributeSet attrs){
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RectImageView);
			mHeightOfWith = typedArray.getInt(R.styleable.RectImageView_heightofwith,0);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		switch (mHeightOfWith){
			case 0:
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
				break;
			case 1:
				super.onMeasure(widthMeasureSpec, widthMeasureSpec);
				break;
			case 2:
				super.onMeasure(heightMeasureSpec, heightMeasureSpec);
				break;
			default:
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
				break;
		}
	}

	@Override
	public void setImageResource(int resId) {
		// TODO Auto-generated method stub

		try {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);

			Bitmap bm = Blur.fastblur(getContext(), bitmap, BLUR_RADIUS);

			super.setImageBitmap(bm);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
		try {
			Bitmap bitmap = Blur.fastblur(getContext(), bm, BLUR_RADIUS);
			super.setImageBitmap(bitmap);
		} catch (Exception e) {
			// TODO: handle exception

			setImageResource(R.color.base_back);

		}
	}

}
