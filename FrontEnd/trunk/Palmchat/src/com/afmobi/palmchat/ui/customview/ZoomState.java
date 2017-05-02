package com.afmobi.palmchat.ui.customview;

import java.util.Observable;

/**
 * 用于设置图片的缩放的大小
 * @author heguiming
 *
 */
public class ZoomState extends Observable {

	private float mZoom;
	private float mPanX;
	private float mPanY;
	private int bitmapWidth;
	private int bitmapHeight;

	public float getPanX() {
		return mPanX;
	}

	public float getPanY() {
		return mPanY;
	}

	public float getZoom() {
		return mZoom;
	}

	/**
	 * 
	 * [用于设置画笔横坐标]<BR>
	 * [功能详细描述]
	 * 
	 * @param panX
	 *            画笔横坐标
	 */
	public void setPanX(float panX) {
		if (panX != mPanX) {
			mPanX = panX;
			setChanged();
		}
	}

	/**
	 * 
	 * [用于设置画笔纵坐标]<BR>
	 * [功能详细描述]
	 * 
	 * @param panY
	 *            画笔纵坐标
	 */
	public void setPanY(float panY) {
		if (panY != mPanY) {
			mPanY = panY;
			setChanged();
		}
	}

	/**
	 * 
	 * [用于设置缩放的比例]<BR>
	 * [功能详细描述]
	 * 
	 * @param zoom
	 *            缩放比例
	 */
	public void setZoom(float zoom) {
		if (zoom != mZoom) {
			mZoom = zoom;
			setChanged();
		}
	}

	/**
	 * 
	 * [获取缩放比例]<BR>
	 * [功能详细描述]
	 * 
	 * @param aspectQuotient
	 *            缩放比例横坐标
	 * @return 缩放比例
	 */
	public float getZoomX(float aspectQuotient) {
		return Math.min(mZoom, mZoom * aspectQuotient);
	}

	/**
	 * 
	 * [用于获取缩放的纵坐标]<BR>
	 * [功能详细描述]
	 * 
	 * @param aspectQuotient
	 *            纵坐标
	 * @return 纵坐标
	 */
	public float getZoomY(float aspectQuotient) {
		return Math.min(mZoom, mZoom / aspectQuotient);
	}

	public int getBitmapWidth() {
		return bitmapWidth;
	}

	public void setBitmapWidth(int bitmapWidth) {
		this.bitmapWidth = bitmapWidth;
	}

	public int getBitmapHeight() {
		return bitmapHeight;
	}

	public void setBitmapHeight(int bitmapHeight) {
		this.bitmapHeight = bitmapHeight;
	}
	
}
