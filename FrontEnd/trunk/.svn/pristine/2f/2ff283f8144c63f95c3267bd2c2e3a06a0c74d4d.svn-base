package com.afmobi.palmchat.ui.customview.photos;

import java.util.ArrayList;
import java.util.List;

import com.afmobi.palmchat.util.AppUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class PhotoWall extends ViewGroup implements PhotoCallback {
	
	
	
	PhotoWallCallback wallCallback;
	boolean isEditModel;
	boolean isShowLabel;
	List<Photo> arrayPhotos;
	boolean isShake;
	int numColumns;
	byte showType;
	PPRect frame;
	
	float kPhotoWallMarginTop = 12.f;
	float kPhotoWallMarginLeft = 8.f;
	
	int getRow(int index ,int numColumns)
	{
		return index/numColumns;
	}
	
	int getColumn(int index ,int numColumns)
	{
		return index % numColumns;
	}
	
	float GET_WIDTH_FOR_COLUMN(int column ,float w)
	{
		return (column + 1) * kPhotoWallMarginLeft + (column * w);
		
	}
	
	float GET_HEIGHT_FOR_ROW(int row ,float w )
	{
		return (row + 1) * kPhotoWallMarginTop + (row * w);
	}
	
	float GET_HEIGHT_FOR_WIDTH(boolean show , float w)
	{
	    if (show) {
	        return w + Photo.kLabelHeight;
	    } else
	    {
	        return w;
	    }
	}
	
	void addPhotoInView(boolean editEnable ,Object photo ,String showName ,int index , float w)
	{
		 int row = getRow(index, this.numColumns);
		 int column =  getColumn(index, this.numColumns);
		 float originx = GET_WIDTH_FOR_COLUMN(column , w);
		   
		 float originy = GET_HEIGHT_FOR_ROW(row, GET_HEIGHT_FOR_WIDTH(this.isShowLabel, w));
		    
		 Photo photoTemp = new Photo(getContext(), new PPRect(originx, originy, w, GET_HEIGHT_FOR_WIDTH(this.isShowLabel, w)), this.isShowLabel);
//		 photoTemp.setEditMode(editEnable); 
		 photoTemp.setCallback(this);
		 if (photo != null && photo instanceof String) {
			 if ("+".equalsIgnoreCase((String)photo)) {
				 photoTemp.setPhotType(PhotoCallback.PhotoTypeAdd);
			} else if ("-".equalsIgnoreCase((String)photo)) {
				photoTemp.setPhotType(PhotoCallback.PhotoTypeDelete);
			} else {
				if (((String) photo).length() <= 0) {
					photoTemp.setPhotType(PhotoTypeAdd);
//					photoTemp.setPhotoPath(photoPath);
				} else {
//					photoTemp.setPhotoPath(photoPath)
					photoTemp.setPhotType(PhotoTypeAdd);
				}
				if (showName != null) {
		            photoTemp.setShowName(showName);
		        }
			}
		}
		 
		arrayPhotos.add(photoTemp);
		this.addView(photoTemp.view);
	}

	public void setPhotos(List<WallEntity> walls)
	{
		this.arrayPhotos.clear();
		
		this.removeAllViews();
		
		int count = walls.size();
		float w = (this.frame.width - (this.numColumns + 1) * kPhotoWallMarginLeft) / (this.numColumns);
	    for (int i=0; i<count; i++) {
	    	WallEntity entity = walls.get(i);
	        addPhotoInView(true, entity.sn, entity.afid, i, w);
	    }
		addPhotoInView(false, "+", null, getChildCount(), w);
		addPhotoInView(false, "-", null, getChildCount(), w);
		
		int showCount = 0;
		
		count = getChildCount();
		for (int i=0; i<count; i++) {
			View view = getChildAt(i);
			if (view.getVisibility() == View.VISIBLE) {
				showCount++;
			}
		}
		
	    float frameHeight = -1;
	    //showcount－1代表的是index
	    int row = getRow((showCount - 1), this.numColumns) + 1;
	    frameHeight = GET_HEIGHT_FOR_ROW(row, GET_HEIGHT_FOR_WIDTH(this.isShowLabel, w));
	    this.frame = new PPRect(frame.x ,frame.y ,frame.width ,frameHeight);
	    
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int count = getChildCount();
		int height = 0;
		int totalHeight = 0;
		for (int i = 0; i < count; i++) {
			boolean firstRow = false;
			boolean firstColumn = false; 
			View child = getChildAt(i);
			int paddingLeft = (int) kPhotoWallMarginLeft;
			int paddingTop = (int) kPhotoWallMarginTop;
			if (i / numColumns <= 0) {
				firstRow = true;
			}
			if (i % numColumns == 0) {
				firstColumn = true;
			}
			
			
			int width = AppUtils.getWidth(getContext());
			LayoutParams lp = getLayoutParams();
			
			if (lp != null) {
				width = lp.width;
			}
			int w = width / numColumns;
			int h = w;
			if (firstRow && firstColumn) {
//				child.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
			} else if (firstRow) {
				child.setPadding(0, paddingTop, paddingLeft, paddingTop);
			} else if (firstColumn) {
				child.setPadding(paddingLeft, 0, paddingLeft, paddingTop);
			} else {
				child.setPadding(0, 0, paddingLeft, paddingTop);
			}
			child.measure(getChildMeasureSpec(widthMeasureSpec, 0, w),
					getChildMeasureSpec(heightMeasureSpec, 0, w));
			
			
			height = child.getMeasuredHeight();
			if (i % numColumns == 0) {
				totalHeight += resolveSize(height, heightMeasureSpec);
			}
		}
		setMeasuredDimension(resolveSize(getMeasuredWidth(), widthMeasureSpec),totalHeight);
	}
	/*
	private int getChildMeasureSpec(int childStart, int childEnd,
            int childSize, int startMargin, int endMargin, int startPadding,
            int endPadding, int mySize) {
        int childSpecMode = 0;
        int childSpecSize = 0;

        // Figure out start and end bounds.
        int tempStart = childStart;
        int tempEnd = childEnd;

        // If the view did not express a layout constraint for an edge, use
        // view's margins and our padding
        if (tempStart < 0) {
            tempStart = startPadding + startMargin;
        }
        if (tempEnd < 0) {
            tempEnd = mySize - endPadding - endMargin;
        }

        // Figure out maximum size available to this view
        int maxAvailable = tempEnd - tempStart;

        if (childStart >= 0 && childEnd >= 0) {
            // Constraints fixed both edges, so child must be an exact size
            childSpecMode = MeasureSpec.EXACTLY;
            childSpecSize = maxAvailable;
        } else {
            if (childSize >= 0) {
                // Child wanted an exact size. Give as much as possible
                childSpecMode = MeasureSpec.EXACTLY;

                if (maxAvailable >= 0) {
                    // We have a maxmum size in this dimension.
                    childSpecSize = Math.min(maxAvailable, childSize);
                } else {
                    // We can grow in this dimension.
                    childSpecSize = childSize;
                }
            } else if (childSize == LayoutParams.MATCH_PARENT) {
                // Child wanted to be as big as possible. Give all available
                // space
                childSpecMode = MeasureSpec.EXACTLY;
                childSpecSize = maxAvailable;
            } else if (childSize == LayoutParams.WRAP_CONTENT) {
                // Child wants to wrap content. Use AT_MOST
                // to communicate available space if we know
                // our max size
                if (maxAvailable >= 0) {
                    // We have a maximum size in this dimension.
                    childSpecMode = MeasureSpec.AT_MOST;
                    childSpecSize = maxAvailable;
                } else {
                    // We can grow in this dimension. Child can be as big as it
                    // wants
                    childSpecMode = MeasureSpec.UNSPECIFIED;
                    childSpecSize = 0;
                }
            }
        }

        return MeasureSpec.makeMeasureSpec(childSpecSize, childSpecMode);
    }
	*/
	/**
     * Measure a child. The child should have left, top, right and bottom information
     * stored in its LayoutParams. If any of these values is -1 it means that the view
     * can extend up to the corresponding edge.
     *
     * @param child Child to measure
     * @param params LayoutParams associated with child
     * @param myWidth Width of the the RelativeLayout
     * @param myHeight Height of the RelativeLayout
     */
	/*
    private void measureChild(View child, LayoutParams params, int myWidth, int myHeight) {
        int childWidthMeasureSpec = getChildMeasureSpec(params.mLeft,
                params.mRight, params.width,
                params.leftMargin, params.rightMargin,
                mPaddingLeft, mPaddingRight,
                myWidth);
        int childHeightMeasureSpec = getChildMeasureSpec(params.mTop,
                params.mBottom, params.height,
                params.topMargin, params.bottomMargin,
                mPaddingTop, mPaddingBottom,
                myHeight);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
*/
	public PhotoWall(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PhotoWall(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PhotoWall(Context context ,PPRect frame) {
		super(context);
		this.frame = frame;
		this.init();
	}
	
	private void init()
	{
		this.arrayPhotos = new ArrayList<Photo>();
		numColumns = 4;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		
		Log.i(VIEW_LOG_TAG, ""+getChildCount() + "-"+changed);
		if (changed) {
			int childLeft = 0;
			int childTop = 0;
			final int childCount = getChildCount();
			
			for (int i = 0; i < childCount; i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					if (i % numColumns == 0 && i != 0) {
						final int childHeight = childView.getMeasuredHeight();
						childTop += childHeight;
						childLeft = 0;
						childView.layout(childLeft, childTop,
								childView.getMeasuredWidth(), childTop
										+ childHeight);
						childLeft += childView.getMeasuredWidth();
					} else {
						final int childWidth = childView.getMeasuredWidth();
						childView.layout(childLeft, childTop, childLeft
								+ childWidth,
								childTop + childView.getMeasuredHeight());
						childLeft += childWidth;
					}

				}
			}
		}
		/*
		final int count = getChildCount();
		Log.i(VIEW_LOG_TAG, "onLayout="+count);
		int lengthX=l;    // right position of child relative to parent
		int lengthY=t;    // bottom position of child relative to parent
		int row = 0;
		for (int i = 0; i < count; i++) {
			final View child = this.getChildAt(i);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			lengthX+=width+kPhotoWallMarginLeft;
			lengthY=(int) (row*(height+kPhotoWallMarginTop)+kPhotoWallMarginTop+height+t);
			if (i % numColumns == 0) {
				lengthX=(int) (width+kPhotoWallMarginLeft+l);
				row++;
				lengthY=(int) (row*(height+kPhotoWallMarginTop)+kPhotoWallMarginTop+height+t);
			}
			child.layout(lengthX-width, lengthY-height, lengthX, lengthY);
		}
		*/
	}
	
	void setEditMode(boolean canEdit , boolean isShake)
	{
		
	}
	
	void addPhoto(String str)
	{
		
	}
	
	void deletePhotoByIndex(int index)
	{
		
	}

	@Override
	public void photoTaped(Photo photo) {
		
	}

	@Override
	public void deleteTaped(Photo photo) {
		
	}

	@Override
	public void photoMoveFinished(Photo photo) {
		
	}

}
