package com.afmobi.palmchat.ui.customview;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.afmobi.palmchat.log.PalmchatLogUtils;

public class AdvViewPager extends ViewPager {
	
    
	private ScrollerCustomDuration mScroller = null;
	
	public AdvViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		  postInitViewPager();

	}
	
/*	 private int mVelocity = 1;
	 
	   @Override
	    void smoothScrollTo(int x, int y, int velocity) {
	        //ignore passed velocity, use one defined here
	        super.smoothScrollTo(x, y, mVelocity);
	    }*/
	
   /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private void postInitViewPager() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = viewpager.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new ScrollerCustomDuration(getContext(),
                    (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception e) {
        }
    }
    
    
    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		PalmchatLogUtils.println("---ccc AdvViewPager: onTouchEvent");
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(arg0);
	}
	 
	
	public class ScrollerCustomDuration extends Scroller {

	    private double mScrollFactor = 7;

	    public ScrollerCustomDuration(Context context) {
	        super(context);
	    }

	    public ScrollerCustomDuration(Context context, Interpolator interpolator) {
	        super(context, interpolator);
	    }

//	    public ScrollerCustomDuration(Context context, Interpolator interpolator, boolean flywheel) {
//	        super(context, interpolator, flywheel);
//	    }

	    /**
	     * Set the factor by which the duration will change
	     */
	    public void setScrollDurationFactor(double scrollFactor) {
	        mScrollFactor = scrollFactor;
	    }

	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
	        super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor));
	    }

	}
	   
}
