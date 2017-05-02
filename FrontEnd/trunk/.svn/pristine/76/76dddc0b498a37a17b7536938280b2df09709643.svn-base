package com.afmobi.palmchat.ui.customview.list;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;


public class LinearLayoutListView extends LinearLayout {
	
	public static final String KEY_TEXT = "text";
	public static final String KEY_VALUE = "value";
	public static final String KEY_ICON = "icon";
	public static final String KEY_ARROW = "arrow";

	private CircleAdapter mAdapter;
	private OnClickListener onClickListener = null;
	private OnItemClickListener mItemClickListener;
	private Rect mTouchFrame;
	private int mItemCount;

	/**
	 *
	 */
	public void bindView() {
		int count = mAdapter.getCount();
		for (int i = 0; i < count; i++) {
			View v = mAdapter.getView(i, null, null);
			addView(v, i);
		}
		Log.v("countTAG", "" + count);
	}

	public LinearLayoutListView(Context context) {
		super(context);
		setOrientation(LinearLayout.VERTICAL) ;
	}

	public LinearLayoutListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL) ;
	}

	/**
	 * 
	 */
	public CircleAdapter getAdpater() {
		return mAdapter;
	}

	/**
	 *
	 */
	public void setAdapter(CircleAdapter adpater) {
		this.removeAllViews();
		this.mAdapter = adpater;
		this.mItemCount = adpater.getCount();
		bindView();
	}

	/**
	 *
	 */
	public OnClickListener getOnclickListner() {
		return onClickListener;
	}

	/**
	 * 
	 */
	public void setOnclickLinstener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}
	
	public void setOnItemClickListener (OnItemClickListener l) {
		this.mItemClickListener = l;
	}
	
	public interface OnItemClickListener {
		void onItemClick(View parent , View v , int position , long id);
	} 
	
/*	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_UP:
			int pos = pointToPosition(x, y);
			PerformClick click = new PerformClick();
			click.mClickMotionPosition = pos;
			click.run();
			break;
		}
	}
	*/
	

	private class PerformClick implements Runnable {
        int mClickMotionPosition;

        public void run() {
            // The data has changed since we posted this action in the event queue,
            // bail out before bad things happen
            final ListAdapter adapter = mAdapter;
            final int motionPosition = mClickMotionPosition;
            if (adapter != null && mItemCount > 0 &&
                    motionPosition != -1 &&
                    motionPosition < adapter.getCount()) {
                final View view = getChildAt(motionPosition);
                // If there is no view, something bad happened (the view scrolled off the
                // screen, etc.) and we should cancel the click
                if (view != null) {
                    performItemClick(view, motionPosition, adapter.getItemId(motionPosition));
                }
            }
        }
    }
	
	public int pointToPosition(int x, int y) {
        Rect frame = mTouchFrame;
        if (frame == null) {
            mTouchFrame = new Rect();
            frame = mTouchFrame;
        }

        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return i;
                }
            }
        }
        return -1;
    }

	public void performItemClick(View view, int motionPosition, long itemId) {
		this.mItemClickListener.onItemClick(this, view, motionPosition, itemId);
	}
}
