package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

public class RightHomeListView extends ExpandableListView implements OnScrollListener, OnGroupClickListener {
	
	public RightHomeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		registerListener();
	}

	public RightHomeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		registerListener();
	}

	public RightHomeListView(Context context) {
		super(context);
		registerListener();
	}

	public interface HeaderAdapter {
		public static final int PINNED_HEADER_GONE = 0;
		public static final int PINNED_HEADER_VISIBLE = 1;
		public static final int PINNED_HEADER_PUSHED_UP = 2;
		
		/**
		 * get header state
		 * @param groupPosition
		 * @param childPosition
		 * @return PINNED_HEADER_GONE,PINNED_HEADER_VISIBLE,PINNED_HEADER_PUSHED_UP 其中之一
		 */
		int getHeaderState(int groupPosition, int childPosition);

		/**
		 * configure headerView
		 * @param header
		 * @param groupPosition
		 * @param childPosition
		 * @param alpha
		 */
		void configureHeader(View header, int groupPosition,int childPosition, int alpha);

		/**
		 * set headerView click status
		 * @param groupPosition
		 * @param status
		 */
		void setHeaderGroupClickStatus(int groupPosition, int status);

		/**
		 * get headerView click status
		 * @param groupPosition
		 * @return
		 */
		int getHeaderGroupClickStatus(int groupPosition);
		
	

	}
	
	
//	add by chris
	
	public interface FooterAdapter {
		public static final int PINNED_FOOTER_GONE = 0;
		public static final int PINNED_FOOTER_VISIBLE = 1;
		public static final int PINNED_FOOTER_PUSHED_DOWN = 2;
		
		/**
		 * get FooterState
		 * @param groupPosition
		 * @param childPosition
		 * @return PINNED_HEADER_GONE,PINNED_HEADER_VISIBLE,PINNED_HEADER_PUSHED_UP 其中之一
		 */
		int getFooterState(int groupPosition, int childPosition);

		/**
		 * configure FooterView
		 * @param header
		 * @param groupPosition
		 * @param childPosition
		 * @param alpha
		 */
		void configureFooter(View header, int groupPosition,int childPosition, int alpha);

		/**
		 * set footerView click status
		 * @param groupPosition
		 * @param status
		 */
		void setFooterGroupClickStatus(int groupPosition, int status);

		/**
		 * get footerView click status
		 * @param groupPosition
		 * @return
		 */
		int getFooterGroupClickStatus(int groupPosition);

		
		void setShowNewFriendFlag(boolean isShow);
		
		boolean getShowNewFriendFlag();
	}
	

	private static final int MAX_ALPHA = 255;

	private HeaderAdapter mAdapter;
	
	
	private FooterAdapter mFAdapter;

	
	private View mHeaderView;
	

	/**
	 * HeaderView visibility
	 */
	private boolean mHeaderViewVisible;

	private int mHeaderViewWidth;
	private int mHeaderViewHeight;
	
	
//	add by chris
	
	private View mFooterView;
	private boolean mFooterViewVisible;
	private int mFooterViewWidth;
	private int mFooterViewHeight;
	/**
	 * expandableListView width height
	 */
	private int mWidth;
	private int mHeight;
	
	
	
//	add by chris
	private View mEditView;
	private int mEditViewWidth;
	private int mEditViewHeight;
	

	public void setHeaderView(View view) {
		mHeaderView = view;
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);

		if (mHeaderView != null) {
			setFadingEdgeLength(0);
		}
		
		

		requestLayout();
	}
	
	
	public void setFooterView(View view) {
		mFooterView = view;
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);

		if (mFooterView != null) {
			setFadingEdgeLength(0);
		}
		
		

		requestLayout();
	}
	
	Context context;

	public void setHeaderView(View view, Context con, View vi) {
		context = con;
		mHeaderView = view;
		this.mEditView = vi;
		
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);

		if (mHeaderView != null) {
			setFadingEdgeLength(0);
		}
		
/*		mEditView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editViewClick();
			}
		});*/

		requestLayout();
	}

	private void registerListener() {
		setOnScrollListener(this);
		setOnGroupClickListener(this);
	}

	/**
	 * HeaderView click event
	 */
	private void headerViewClick() {
		long packedPosition = getExpandableListPosition(this.getFirstVisiblePosition());
		
		int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
		
		if (mAdapter.getHeaderGroupClickStatus(groupPosition) == 1) {
			this.collapseGroup(groupPosition);
			mAdapter.setHeaderGroupClickStatus(groupPosition, 0);
		}
		else{
			this.expandGroup(groupPosition);
			mAdapter.setHeaderGroupClickStatus(groupPosition, 1);
		}
		
		this.setSelectedGroup(groupPosition);
		
/*		switch (groupPosition) {
		case 0:
			Toast.makeText(context, "click group 1", 0).show();
			break;

		case 1:
			Toast.makeText(context, "click group 2", 0).show();
			break;
		}*/
		
		
	}
	
	
	
	private void footerViewClick() {
		long packedPosition = getExpandableListPosition(this.getLastVisiblePosition());
		int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
//		PalmchatLogUtils.println("right groupPosition:" + groupPosition);
//		int groupPosition = 1;
		
		if (mFAdapter.getFooterGroupClickStatus(groupPosition) == 1) {
			this.collapseGroup(groupPosition);
			mFAdapter.setFooterGroupClickStatus(groupPosition, 0);
		}
		else{
			this.expandGroup(groupPosition);
			mFAdapter.setFooterGroupClickStatus(groupPosition, 1);
		}
		
		this.setSelectedGroup(groupPosition);
		
/*		switch (groupPosition) {
		case 0:
			Toast.makeText(context, "click group 1", 0).show();
			break;

		case 1:
			Toast.makeText(context, "click group 2", 0).show();
			break;
		}*/
		
		
		
	}
	
/*	
	private void editViewClick() {

		long packedPosition = getExpandableListPosition(this.getFirstVisiblePosition());
		
		int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
		switch (groupPosition) {
		case 0:
			Toast.makeText(context, "click group 1", 0).show();
			break;

		case 1:
			Toast.makeText(context, "click group 2", 0).show();
			break;
			
		}
	}*/

	private float mDownX;
	private float mDownY;

	/**
	 * override onTouchEvent
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownX = ev.getX();
				mDownY = ev.getY();
				if (mDownX <= mHeaderViewWidth && mDownY <= mHeaderViewHeight) {
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				float x = ev.getX();
				float y = ev.getY();
				float offsetX = Math.abs(x - mDownX);
				float offsetY = Math.abs(y - mDownY);
				
				
		/*		if (x >= mHeaderViewWidth -  mEditViewWidth && y <= mEditViewHeight
						&& offsetX <= mEditViewWidth && offsetY <= mEditViewHeight) {
							if (mEditView != null) {
								editViewClick();
							}
							return true;
						}*/
				if (mHeaderViewVisible) {
				// touch headerView
				if (x <= mHeaderViewWidth && y <= mHeaderViewHeight
				&& offsetX <= mHeaderViewWidth && offsetY <= mHeaderViewHeight) {
					if (mHeaderView != null) {
						
						try {
							
							headerViewClick();
						} catch (Exception e) {
							// TODO: handle exception
						}
						
					}
					return true;
				}
				
				}
				
				
				 if(mFooterViewVisible) {
				
				if (x <= mFooterViewWidth && y >= mFooterView.getTop()
						&& offsetX <= mFooterViewWidth && offsetY <= mFooterViewHeight) {
							if (mFooterView != null) {
								try {
									
									footerViewClick();
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
							return true;
						}
				
				 }
				
				 if(mFAdapter!=null&&mFAdapter.getShowNewFriendFlag()  ) {
					 
					 mFAdapter.setShowNewFriendFlag(false);
				 }
				 
				break;
			default:
				break;
				
			}
			
			boolean value = false;
			
			try {
				
				value = super.onTouchEvent(ev);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		return value;

	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		
//		return super.onInterceptTouchEvent(ev);
		
		float x = ev.getX();
		float y = ev.getY();
		
			if (x <= mHeaderViewWidth && y <= mHeaderViewHeight) {
				if (mHeaderViewVisible) {
					return true;
				
				}
			}
				
			
					if (x <= mFooterViewWidth && y >= mFooterView.getTop()) {
						
						if(mFooterViewVisible) {
							return true;
						}
					}
		
			return super.onInterceptTouchEvent(ev);
	}
	

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		super.setAdapter(adapter);
		mAdapter = (HeaderAdapter) adapter;
		
		mFAdapter = (FooterAdapter) adapter;
	}

	/**
	 * 
	 * group click status
	 */
	@Override
	public boolean onGroupClick(ExpandableListView parent,View v,int groupPosition,long id) {
		
		if (mAdapter.getHeaderGroupClickStatus(groupPosition) == 0) {
			mAdapter.setHeaderGroupClickStatus(groupPosition, 1);
			parent.expandGroup(groupPosition);
			parent.setSelectedGroup(groupPosition);
			
		} else if (mAdapter.getHeaderGroupClickStatus(groupPosition) == 1) {
			mAdapter.setHeaderGroupClickStatus(groupPosition, 0);
			parent.collapseGroup(groupPosition);
		}
		
		
/*		if (mFAdapter.getFooterGroupClickStatus(groupPosition) == 0) {
			mFAdapter.setFooterGroupClickStatus(groupPosition, 1);
			parent.expandGroup(groupPosition);
			parent.setSelectedGroup(groupPosition);
			
		} else if (mFAdapter.getFooterGroupClickStatus(groupPosition) == 1) {
			mFAdapter.setFooterGroupClickStatus(groupPosition, 0);
			parent.collapseGroup(groupPosition);
		}*/

		return true;

	}
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
			
		}
		
		
		
		if (mFooterView != null) {
			measureChild(mFooterView, widthMeasureSpec, heightMeasureSpec);
			mFooterViewWidth = mFooterView.getMeasuredWidth();
			mFooterViewHeight = mFooterView.getMeasuredHeight();
		}
		
		
	
		if(mEditView != null) {
			measureChild(mEditView, widthMeasureSpec, heightMeasureSpec);
			mEditViewWidth = mEditView.getMeasuredWidth();
			mEditViewHeight = mEditView.getMeasuredHeight();
		}
		
//homeListview
		mWidth = this.getMeasuredWidth();
		mHeight = this.getMeasuredHeight();
		
	}

//	header and footer state
	private int mOldState = -1;
	
	private int mOldState2 = -1;
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		final long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
		final int groupPos = ExpandableListView.getPackedPositionGroup(flatPostion);
		final int childPos = ExpandableListView.getPackedPositionChild(flatPostion);
		
		if(mAdapter != null) {
		int state = mAdapter.getHeaderState(groupPos, childPos);
		if (mHeaderView != null && mAdapter != null && state != mOldState) {
			mOldState = state;
			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
		}


		configureHeaderView(groupPos, childPos);
		
//		add by chris
		final long flatPostion2 = getExpandableListPosition(getLastVisiblePosition());
		final int groupPos2 = ExpandableListView.getPackedPositionGroup(flatPostion2);
		final int childPos2 = ExpandableListView.getPackedPositionChild(flatPostion2);
		int state2 = mAdapter.getHeaderState(groupPos2, childPos2);
		if (mFooterView != null && mFAdapter != null && state2 != mOldState2) {
			mOldState = state2;
			mFooterView.layout(0, mHeight - mFooterViewHeight, mFooterViewWidth, mHeight);
		}


		configureFooterView(groupPos2, childPos2);
		
		}
	}

	public void configureHeaderView(int groupPosition, int childPosition) {
		if (mHeaderView == null || mAdapter == null
		|| ((ExpandableListAdapter) mAdapter).getGroupCount() == 0) {
			return;
		}

		int state = mAdapter.getHeaderState(groupPosition, childPosition);
		
//		System.out.println("mHeaderView top:"+mHeaderView.getTop()+"--mHeaderView bottom:"+mHeaderView.getBottom());

		switch (state) {
			case HeaderAdapter.PINNED_HEADER_GONE: {
				mHeaderViewVisible = false;
				
//				System.out.println("right PINNED_HEADER_GONE");
				
				break;
			}
			
	
			case HeaderAdapter.PINNED_HEADER_VISIBLE: {
				
				
				mAdapter.configureHeader(mHeaderView, groupPosition,childPosition, MAX_ALPHA);
				
				
	
				if (mHeaderView.getTop() != 0){
					mHeaderView.layout(0, 0, mFooterViewWidth, mFooterViewHeight);
				}
	
					mHeaderViewVisible = true;
	
//				System.out.println("right PINNED_HEADER_VISIBLE");
				break;
			}
			
	
			case HeaderAdapter.PINNED_HEADER_PUSHED_UP: {
				View firstView = getChildAt(0);
				int bottom = firstView.getBottom();
	
				// intitemHeight = firstView.getHeight();
				int headerHeight = mFooterView.getHeight();
	
				int y;
	
				int alpha;
	
				if (bottom < headerHeight) {
					y = (bottom - headerHeight);
					alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
				} else {
					y = 0;
					alpha = MAX_ALPHA;
				}
			
				mAdapter.configureHeader(mHeaderView, groupPosition, childPosition, alpha);
	
				if (mHeaderView.getTop() != 0) {
				}
					mHeaderView.layout(0, y, mFooterViewWidth, mFooterViewHeight + y);
				
	
				mHeaderViewVisible = true;
				
				
//				System.out.println("right header PINNED_HEADER_PUSHED_UP");
				break;
			}
		}
		
	}
	
	
	
//	footerView 
	public void configureFooterView(int groupPosition, int childPosition) {
		if (mFooterView == null || mFAdapter == null
		|| ((ExpandableListAdapter) mFAdapter).getGroupCount() == 0) {
			return;
		}

		int state = mFAdapter.getFooterState(groupPosition, childPosition);

		switch (state) {
			case FooterAdapter.PINNED_FOOTER_GONE: {
				mFooterViewVisible = false;
				
				System.out.println("right footer PINNED_FOOTER_GONE");
				
				break;
			}
	
			case FooterAdapter.PINNED_FOOTER_VISIBLE: {
				mFAdapter.configureFooter(mFooterView, groupPosition,childPosition, MAX_ALPHA);
	
				if (mFooterView.getTop() != mHeight - mFooterViewHeight) {
					mFooterView.layout(0, mHeight - mFooterViewHeight, mFooterViewWidth, mHeight);
				}
				
	
				mFooterViewVisible = true;
	
//				System.out.println("mFooterView top:"+mFooterView.getTop()+"--mFooterView bottom:"+mFooterView.getBottom());
				System.out.println("right footer PINNED_FOOTER_VISIBLE");
				break;
			}
			
	
			case FooterAdapter.PINNED_FOOTER_PUSHED_DOWN: {
			 	
//		 		View firstView = getChildAt(0);
//				int bottom = firstView.getBottom();
//	
//				// intitemHeight = firstView.getHeight();
//				int headerHeight = mFooterView.getHeight();
//	
//				int y;
//	
//				int alpha;
//	
//				if (bottom < headerHeight) {
//					y = (bottom - headerHeight);
//					alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
//				} else {
//					y = 0;
//					alpha = MAX_ALPHA;
//				}
//			
//				mFAdapter.configureFooter(mFooterView, groupPosition, childPosition, alpha);
//	
//				if (mFooterView.getTop() != y) {
//				}
//					mFooterView.layout(0, getBottom() - mFooterViewHeight + y, mFooterViewWidth, getBottom() + y);
//	
//				mFooterViewVisible = true;
				mFooterViewVisible = false;
//				System.out.println("right footer PINNED_FOOTER_PUSHED_DOWN");
				break;
				
				
			}
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mHeaderViewVisible) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
		
		if (mFooterViewVisible) {
			drawChild(canvas, mFooterView, getDrawingTime());
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		final long flatPos = getExpandableListPosition(firstVisibleItem);
		int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);
		int childPosition = ExpandableListView.getPackedPositionChild(flatPos);
		
		final long flatPos2 = getExpandableListPosition(firstVisibleItem + visibleItemCount - 1);
		int groupPosition2 = ExpandableListView.getPackedPositionGroup(flatPos2);
		int childPosition2 = ExpandableListView.getPackedPositionChild(flatPos2);
		
		try {
			
			configureHeaderView(groupPosition, childPosition);
			
			configureFooterView(groupPosition2, childPosition2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
	
	
	
	public void onHomeListViewScroll(int groupPosition, int childPosition, int groupPosition2, int childPosition2) {
	
		try {
			
			configureHeaderView(groupPosition, childPosition);
			configureFooterView(groupPosition2, childPosition2);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
}
