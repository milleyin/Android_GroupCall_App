package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Zoom View Pager
 * 
 * @author heguiming
 * 
 */
public class ZoomViewPager extends ViewPager {

	// can switch image
	private boolean canSwitching = true;

	public ZoomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent mv) {
		try {
			super.onTouchEvent(mv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent mv) {
		try {
			if (canSwitching) {
				 return super.onInterceptTouchEvent(mv);
			} else {
				return false;
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	@Override
	protected void onDraw(Canvas arg0) {
		// TODO Auto-generated method stub
		try{
			super.onDraw(arg0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void setCanSwitching(boolean canSwitching) {
		this.canSwitching = canSwitching;
	}

}
