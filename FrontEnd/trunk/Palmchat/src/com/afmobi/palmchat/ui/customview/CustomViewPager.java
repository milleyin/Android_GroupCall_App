package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.afmobi.palmchat.log.PalmchatLogUtils;

public class CustomViewPager extends ViewPager {

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	 	  if (!isCanScroll) {
		        return false;
		  }
		return super.onInterceptTouchEvent(ev);
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent arg0) {
	// FaceFooterView.event = arg0;
	// Log.e("onTouchEvent", FaceFooterView.event.getX() + "|" +
	// FaceFooterView.event.getY());
	// getParent().requestDisallowInterceptTouchEvent(true);
	// return super.onTouchEvent(arg0);
	// }
	
	
	
	
	 private boolean isCanScroll = true;   
    public void setScanScroll(boolean isCanScroll){  
	        this.isCanScroll = isCanScroll;  
	}   
//	    @Override  
//	public void scrollTo(int x, int y){  
//	        if (isCanScroll){  
//	            super.scrollTo(x, y);  
//	        }  
//	}
	
	    
	    @Override
	    public boolean onTouchEvent(MotionEvent ev) {
	      if (!isCanScroll) {
	        return false;
	      }
	      return super.onTouchEvent(ev);
	    }


	    public boolean isScrollble() {
	      return isCanScroll;
	    }

	  

}
