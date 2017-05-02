package com.afmobi.palmchat.ui.customview;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.afmobi.palmchat.log.PalmchatLogUtils;

/**
 * zoom image listener
 * 
 * @author heguiming
 * 
 */
public class SimpleZoomListener implements View.OnTouchListener,View.OnLongClickListener {
	
	private static final float MOVE = 1.0f;
	public static final float ZOOM_DEFAULT = 4.0f;
	public static float ZOOM = ZOOM_DEFAULT;

	public enum ControlType {
		PAN,
		ZOOM
	} 

	private ZoomState mState;

	private float mX;
	private float mY;
	private float mGap;
	
	private float downX;
	private float downY;
	
	private float upX;
	private float upY;
	
	private float moveX;
	private float moveY;
	
	private boolean canLongClick;
	
	private ZoomViewPager mViewPage;
	
	private ImgOnClickListener mOnClick;
	

	public SimpleZoomListener(ZoomViewPager mViewPage, ImgOnClickListener mOnClick) {
		this.mViewPage = mViewPage;
		this.mOnClick = mOnClick;
	}
	

	public SimpleZoomListener() {
	}


	public void setZoomState(ZoomState state) {
		mState = state;
	}
	
	/**
	 * 
	 * @param v
	 *            View
	 * @param event
	 *            MotionEvent
	 * @return boolean
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 *      android.view.MotionEvent)
	 */
	public boolean onTouch(View v, MotionEvent event) {
		
		final int action = event.getAction();
		
//		PalmchatLogUtils.e("TAG", "----onTouch--action:" + action);
		
		int pointCount = event.getPointerCount();
		
		if (pointCount == 1) {
			
			canLongClick = true;
			
			final float x = event.getX();
			final float y = event.getY();
			
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				downX = x;
				downY = y;
				mX = x;
				mY = y;
				break;
			case MotionEvent.ACTION_MOVE: 
				
				if (moveX != x && moveY != y) {
					canLongClick = false;
				}
				
				moveX = x;
				moveY = y;
				
				int vWidth = v.getWidth();
				int vHeight = v.getHeight();
				
				final float dx = (x - mX) / vWidth;
				final float dy = (y - mY) / vHeight;
				
				float moveX = (mState.getPanX() - dx);
				float moveY = (mState.getPanY() - dy);
				float zoom = mState.getZoom();
				
				if (zoom == 1.0f) {
					moveX = (moveX > 0.5f) ? 0.5f : (moveX < 0.5f) ? 0.5f : moveX;
					moveY = (moveY > 0.5f) ? 0.5f : (moveY < 0.5f) ? 0.5f : moveY;
					
					//sliding to edge and notify viewPager to switch image
					if (moveX == 0.5f || moveY == 0.5f) {
						if (null != mViewPage) {
							mViewPage.setCanSwitching(true);
						}
					} else {
						if (null != mViewPage) {
							mViewPage.setCanSwitching(false);
						}
					}
					
				} else {
					moveX = (moveX > MOVE) ? MOVE : (moveX < 0.0f) ? 0.0f : moveX;
					moveY = (moveY > MOVE) ? MOVE : (moveY < 0.0f) ? 0.0f : moveY;
					
					//sliding to edge and notify viewPager to switch image
					if (moveX == MOVE || moveX == 0.0f || moveY == MOVE || moveY == 0.0f) {
						if (null != mViewPage) {
							mViewPage.setCanSwitching(true);
						}
					} else {
						if (null != mViewPage) {
							mViewPage.setCanSwitching(false);
						}
					}
					
				}
				
				mState.setPanX(moveX);
				mState.setPanY(moveY);
				mState.notifyObservers();
				mX = x;
				mY = y;
				break;
			case MotionEvent.ACTION_UP:
				upX = x;
				upY = y;
				if (null != mOnClick) {
					if (Math.abs(upX - downX) < 5 && Math.abs(upY - downY) < 5){
						mOnClick.onClick();
					}
				}
				mState.notifyObservers();
				break;
			}
		}
		
		if (pointCount == 2) {
			
			canLongClick = false;
			
			if (null != mOnClick) {
				mOnClick.onMoveOrScale();
			}
		    float x0 = 0;
			float y0 =0;

			try {
				x0 = event.getX(event.getPointerId(0));
				  y0 = event.getY(event.getPointerId(0));
			} catch (Exception e) {
				e.printStackTrace();
				return true;
			}
			float x1 = 0;
			float y1 = 0;
			
			try {
				x1 = event.getX(event.getPointerId(1));
				y1 = event.getY(event.getPointerId(1));
			} catch (Exception e) {
				e.printStackTrace();
				return true;
			}

			final float gap = getGap(x0, x1, y0, y1);
			switch (action) {
			case MotionEvent.ACTION_POINTER_2_DOWN:
			case MotionEvent.ACTION_POINTER_1_DOWN:
				mGap = gap;
				break;
			case MotionEvent.ACTION_POINTER_1_UP:
				mX = x1;
				mY = y1;
				mState.notifyObservers();
				break;
			case MotionEvent.ACTION_POINTER_2_UP:
				mX = x0;
				mY = y0;
				mState.notifyObservers();
				break;
			case MotionEvent.ACTION_MOVE:
				final float dgap = (gap - mGap) / mGap;
				float zoom = mState.getZoom() * (float) Math.pow(5, dgap);
				zoom = zoom > 1.0f ? zoom : 1.0f;
				zoom = zoom > ZOOM ? ZOOM : zoom;
				
				if (zoom == 1.0f) {
					if (null != mViewPage) {
						mViewPage.setCanSwitching(true);
					}
					mState.setPanX(0.5f);
					mState.setPanY(0.5f);
				}
				mState.setZoom(zoom);
				mState.notifyObservers();
				mGap = gap;
				break;
			}
		}
		if (canLongClick) {
			return false;
		}
		return true;
	}

	/**
	 * @param x0
	 *            x0
	 * @param x1
	 *            x1
	 * @param y0
	 *            yo
	 * @param y1
	 *            y1
	 * @return float
	 */
	private float getGap(float x0, float x1, float y0, float y1) {
		return (float) Math.pow(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2),
				0.5);
	}
	
	public interface ImgOnClickListener {
		/**
		 *  click control whether hide title
		 */
		public void onClick();

		/**
		 *  move or zoom, hide title
		 */
		public void onMoveOrScale();
		
		public void onLongClick();
	}

	@Override
	public boolean onLongClick(View v) {
		if (canLongClick && null != mOnClick) {
			mOnClick.onLongClick();
		}
		return false;
	}

}
