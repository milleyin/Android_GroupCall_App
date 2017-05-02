package com.afmobi.palmchat.ui.customview;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.ImageUtil;

/**
 * 图片缩放控件
 * @author 何桂明
 *
 */
public class ImageZoomView extends View implements Observer {

	/**
	 * 画笔
	 */
	private final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
	private final Rect mRectSrc = new Rect();
	private final Rect mRectDst = new Rect();
	private float mAspectQuotient;

	private Bitmap mBitmap;

	private ZoomState mState;

	/**
	 * 
	 * [构造图片的缩放管理工作类]
	 * 
	 * @param context
	 *            当前操作的上下文对象
	 * @param attrs
	 *            AttributeSet
	 */
	public ImageZoomView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ImageZoomView(Context context ) {
		super(context );
	}
	/**
	 * 
	 * [设置图片的ZoomState]<BR>
	 * [功能详细描述]
	 * 
	 * @param state
	 *            ZoomState
	 */
	public void setZoomState(ZoomState state) {
		if (mState != null) {
			mState.deleteObserver(this);
		}
		mState = state;
		if (null != mBitmap) {
			int bitmapWidth = mBitmap.getWidth();
			int bitmapHeight = mBitmap.getHeight();
			mState.setBitmapWidth(bitmapWidth);
			mState.setBitmapHeight(bitmapHeight);
		}
		mState.addObserver(this);
		invalidate();
	}

	/**
	 * 
	 * [画制新的图片]<BR>
	 * [功能详细描述]
	 * 
	 * @param canvas
	 *            画布
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	protected void onDraw(Canvas canvas) {
		if (mBitmap != null && mState != null) {
			final int viewWidth = getWidth();
			final int viewHeight = getHeight();
			final int bitmapWidth = mBitmap.getWidth();
			final int bitmapHeight = mBitmap.getHeight();

			final float panX = mState.getPanX();
			final float panY = mState.getPanY();
			final float zoomX = mState.getZoomX(mAspectQuotient) * viewWidth / bitmapWidth;
			final float zoomY = mState.getZoomY(mAspectQuotient) * viewHeight / bitmapHeight;

			// Setup source and destination rectangles
			mRectSrc.left = (int) (panX * bitmapWidth - viewWidth / (zoomX * 2));
			mRectSrc.top = (int) (panY * bitmapHeight - viewHeight / (zoomY * 2));
			mRectSrc.right = (int) (mRectSrc.left + viewWidth / zoomX);
			mRectSrc.bottom = (int) (mRectSrc.top + viewHeight / zoomY);
			mRectDst.left =0;//(viewWidth-bitmapWidth)/2;// getLeft();
			mRectDst.top =0;//(viewHeight-bitmapHeight)/2;// getTop();
			mRectDst.right = viewWidth;//(viewWidth-bitmapWidth)/2+bitmapWidth;//getRight();
			mRectDst.bottom =viewHeight;// (viewHeight-bitmapHeight)/2+bitmapHeight;//getBottom();

//			if (mRectSrc.left < 0) {
//				mRectDst.left += -mRectSrc.left * zoomX;
//				mRectSrc.left = 0;
//			}
//			if (mRectSrc.right > bitmapWidth) {
//				mRectDst.right -= (mRectSrc.right - bitmapWidth) * zoomX;
//				mRectSrc.right = bitmapWidth;
//			}
//			if (mRectSrc.top < 0) {
//				mRectDst.top += -mRectSrc.top * zoomY;
//				mRectSrc.top = 0;
//			}
//			if (mRectSrc.bottom > bitmapHeight) {
//				mRectDst.bottom -= (mRectSrc.bottom - bitmapHeight) * zoomY;
//				mRectSrc.bottom = bitmapHeight;
//			}
			
			// Adjust source rectangle so that it fits within the source image.
			if (mRectSrc.left < 0) {
				mRectDst.left += -mRectSrc.left * zoomX;
				mRectSrc.left = 0;
			}
			if (mRectSrc.right > bitmapWidth) {
				mRectDst.right -= (mRectSrc.right - bitmapWidth) * zoomX;
				mRectSrc.right = bitmapWidth;
			}
			if (mRectSrc.top < 0) {
				mRectDst.top += -mRectSrc.top * zoomY;
				mRectSrc.top = 0;
			}
			if (mRectSrc.bottom > bitmapHeight) {
				mRectDst.bottom -= (mRectSrc.bottom - bitmapHeight) * zoomY;
				mRectSrc.bottom = bitmapHeight;
			}
			if (!mBitmap.isRecycled()) {
			try {
				canvas.drawBitmap(mBitmap, mRectSrc, mRectDst, mPaint);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} 
			}
		}
	}

	/**
	 * 
	 * [更新]<BR>
	 * [功能详细描述]
	 * 
	 * @param observable
	 *            Observable
	 * @param data
	 *            Object
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object data) {
		invalidate();
	}

	private void calculateAspectQuotient() {
		if (mBitmap != null) {
			mAspectQuotient = (((float) mBitmap.getWidth()) / mBitmap.getHeight()) / (((float) getWidth()) / getHeight());
		}
	}

	/**
	 * 
	 * [用于设置图片源]<BR>
	 * [功能详细描述]
	 * 
	 * @param bitmap
	 *            图片源
	 */
	public void setImage(Bitmap bitmap) {
		int h = bitmap.getHeight();
		int w = bitmap.getWidth();
		
		PalmchatLogUtils.e("TAG", "----h:" + h);
		PalmchatLogUtils.e("TAG", "----w:" + w);
		
		int max = h;
		boolean isH = true;
		if (h < w) {
			max = w;
			isH = false;
		}
		
		Bitmap newBit = bitmap;
		int maxS = 2000;
		float maxSf = 2000.0f;
		if (max > maxS) {
			if (isH) {
				float width = (maxSf / h) * w;
				newBit = ImageUtil.picZoom(bitmap, (int) width, maxS);
				bitmap.recycle();
			} else {
				float height = (maxSf / w) * h;
				newBit = ImageUtil.picZoom(bitmap, maxS, (int) height);
				bitmap.recycle();
			}
		}
		
		mBitmap = newBit;
		calculateAspectQuotient();
		invalidate();
	}

	/**
	 * 
	 * [一句话功能简述]<BR>
	 * [功能详细描述]
	 * 
	 * @param changed
	 *            是否改变
	 * @param left
	 *            左侧距离
	 * @param top
	 *            顶部距离
	 * @param right
	 *            右侧距离
	 * @param bottom
	 *            底部距离
	 * @see android.view.View#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		calculateAspectQuotient();
	}
	
	public Bitmap getmBitmap() {
		return mBitmap;
	}
	
	public void recycleBitmap() {
		if (mBitmap != null && !mBitmap.isRecycled()) {
			mBitmap.recycle();
			mBitmap = null;
		}
	}
}
