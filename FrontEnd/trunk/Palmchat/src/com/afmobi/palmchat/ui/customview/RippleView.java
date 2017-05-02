package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.ImageUtil;

public class RippleView extends View {
	
	private static final int MAX_RADIUS = ImageUtil.DISPLAYW / 2;
	private int mRadiuLevel;
	private static final int LEVEL_COUNT = 5;
	private static final int MAX_ALPHA = 255;
	private static final int ORIGIN_ALPHA = (int) (MAX_ALPHA * 0.6);
	private static final int ALPHA_4 = (int) (MAX_ALPHA * 0.5);
	private static final int ALPHA_3 = (int) (MAX_ALPHA * 0.4);
	private static final int ALPHA_2 = (int) (MAX_ALPHA * 0.3);
	private static final int ALPHA_1 = (int) (MAX_ALPHA * 0.2);
	
	
	private static final int DELAYTIME = 50;
	private static final int INC = MAX_RADIUS / 50;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int mOriginRadius;
	private int mRadius;
	private Handler mHandler = new Handler();
	
	public RippleView(Context context) {
		super(context);
	}
	
	public RippleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub 
		mPaint.setStyle(Paint.Style.STROKE);
		setColor(context.getResources().getColor(R.color.color_voice_ripple));
		mPaint.setStrokeWidth(ImageUtil.dip2px(context, 1)); 
		
	}
	
	public void setColor(int color) {
		mPaint.setColor(color);
	}
	
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}
	
	public void setOriginRadius(int radius) {
		
		mOriginRadius = radius;
		mRadius = mOriginRadius;
		mRadiuLevel = (MAX_RADIUS - mOriginRadius) / LEVEL_COUNT;
	}
	
	public void startRipple() {
		
	     
		setAlpha(ORIGIN_ALPHA);
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mRadius += INC;
				
				if (mRadius < mOriginRadius + mRadiuLevel * 2 && mRadius > mOriginRadius + mRadiuLevel * 1) {
					setAlpha(ALPHA_4);
				} else if (mRadius < mOriginRadius + mRadiuLevel * 3 && mRadius > mOriginRadius + mRadiuLevel * 2) {
					setAlpha(ALPHA_3);
				} else if (mRadius < mOriginRadius + mRadiuLevel * 4 && mRadius > mOriginRadius + mRadiuLevel * 3) {
					setAlpha(ALPHA_2);
				} else if (mRadius < mOriginRadius + mRadiuLevel * 5 && mRadius > mOriginRadius + mRadiuLevel * 4) {
					setAlpha(ALPHA_1);
				}
				
				if (mRadius >= MAX_RADIUS) {
					mRadius = mOriginRadius;
				}
				
				invalidate();
				mHandler.postDelayed(this, DELAYTIME);
			}
		}, DELAYTIME);
	}
	
	
	public void stopRipple() {
		mHandler.removeCallbacksAndMessages(null);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mPaint);
		
	}

}
