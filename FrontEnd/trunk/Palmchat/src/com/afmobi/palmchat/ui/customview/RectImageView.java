package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 正方形 Imageview(让高等于宽)
 * @author Transsion
 *
 */
public class RectImageView extends ImageView {

	public RectImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public RectImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public RectImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int height = widthMeasureSpec;
		super.onMeasure(widthMeasureSpec, height);
	}
}
