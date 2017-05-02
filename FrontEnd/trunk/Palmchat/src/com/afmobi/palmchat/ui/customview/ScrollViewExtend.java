package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.util.ImageUtil;

/**
 * 
 * @author heguiming
 *
 */
public class ScrollViewExtend extends ScrollView {

	private boolean isShowMore;
	private ScrollViewTouchEventFrozen mScrollViewTouchEventFrozen;
	public ScrollViewExtend(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isShowMore) {
			
			return false;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isShowMore) {
			if(mScrollViewTouchEventFrozen!=null){
				mScrollViewTouchEventFrozen.onScrollChangedFrozen( ev);
			}
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}

	public void setShowMore(boolean isShowMore,int moveDistance,int moveSpeed ,int touchY) {
/*		if (isShowMore) {
			int y = ImageUtil.dip2px(PalmchatApp.getApplication(), 250);
			smoothScrollTo(0, y);
		} else {
			smoothScrollTo(0, 0);
		}*/
		this.isShowMore = isShowMore; 
		if(isShowMore){
		
//			 if(my/2==0){
				 if(touchY- getScrollY()<ImageUtil.DISPLAYH/6){ 
					 moveDistance=-moveSpeed-( ImageUtil.DISPLAYH/6-(touchY- getScrollY()))/4;
				 }else if(touchY- getScrollY()>ImageUtil.DISPLAYH*5/6){ 
					 moveDistance= moveSpeed+( (touchY- getScrollY())-ImageUtil.DISPLAYH*5/6 )/4;
					 }
//			}
		 int sy= getScrollY()+moveDistance/2;
//			if(sy>getScrollBarSize()){
				setScrollY(sy);
//			}
			
		}
	}
	public void setOnFrozenListener( ScrollViewTouchEventFrozen scrollViewTouchEventFrozen) { 
		 	mScrollViewTouchEventFrozen=scrollViewTouchEventFrozen; 
	 }
	public boolean isShowMore() {
		return isShowMore;
	}
	  @Override
	    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
	        super.onScrollChanged(x, y, oldx, oldy);
	        if (mScrollViewTouchEventFrozen != null) {
	        	mScrollViewTouchEventFrozen.onScrollChanged( );
	        }
	    }
}
