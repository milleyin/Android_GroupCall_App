package com.afmobi.palmchat.ui.activity.chattingroom.widget;

/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * 		Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.ui.activity.social.AdapterBroadcastMessages;
import com.afmobi.palmchat.ui.activity.social.HomeListAdapter;
import com.afmobi.palmchat.util.AppUtils;


public class LabelXListView extends ListView implements OnScrollListener {
	
	private View mLabelView;
	private int mMeasuredWidth;
	private int mMeasuredHeight;
	private boolean mDrawFlag = true;
	private PinnedLabelAdapter mPinnedHeaderAdapter;

	private float mLastY = -1; // save event y
	private Scroller mScroller; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener

	// the interface to trigger refresh and load more.
	private IXListViewListener mListViewListener;

	// -- header view
	private XListViewHeader mHeaderView;
	
	//broadcast notification view
	private View mBrdNotifiView;
	//public groupchat popular top
	private View mGCPopularTopView;
	
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;
	private TextView mHeaderTimeView;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	private XListViewFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;
	
	//获取不到数据的时候
	//private XListViewMessageShow mShow;
	
	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;

	private final static int SCROLL_DURATION = 400; // scroll back duration
	private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
														// at bottom, trigger
														// load more.
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
													// feature.
	private boolean mShowAnim;
	
	private boolean mIsShowFootView;
	private boolean mIsShowNotifiHeaderView;
	private boolean mIsShowpopular_top;
	
	
//	private TrackElement[] trackElements = {
//			   new TrackElement(0), // top view, bottom Y
//			   new TrackElement(1), // mid view, bottom Y
//			   new TrackElement(2), // mid view, top Y
//			   new TrackElement(3)
//	};// bottom view, top Y
	
	
	
	/**
	 * @param context
	 */
	public LabelXListView(Context context) {
		super(context);
		initWithContext(context,null);
	}

	public LabelXListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context,attrs);
	}

	public LabelXListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context,attrs);
	}
	

	/**
	 * 设置置顶的Label View
	 * 
	 * @param label
	 */
	public void setLabelView(View label) {
		mLabelView = label;
		
		requestLayout();
	}

	private void initWithContext(Context context,AttributeSet attrs) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

//		if (attrs != null) {
//			TypedArray a = context.obtainStyledAttributes(attrs,
//					R.styleable.ShowAnimation);
//			mShowAnim = a.getBoolean(
//					R.styleable.ShowAnimation_showAnim, mShowAnim);
//			a.recycle();
//		}
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ShowFootView);
			mIsShowFootView = a.getBoolean(R.styleable.ShowFootView_showfootview, false);
			mIsShowNotifiHeaderView = a.getBoolean(R.styleable.ShowFootView_shownotiheaderview, false);
			mIsShowpopular_top = a.getBoolean(R.styleable.ShowFootView_showpopular_top, false);
			a.recycle();
		}
		
		
		// init header view
		mHeaderView = new XListViewHeader(context,mShowAnim);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);
		mHeaderTimeView = (TextView) mHeaderView
				.findViewById(R.id.xlistview_header_time);
		
		
//		broadcastFragment, add notification header view
		if (mIsShowNotifiHeaderView) {
			mBrdNotifiView = View.inflate(context, R.layout.header_broadcast_notification, null);
			LinearLayout headerParent = new LinearLayout(context);
			headerParent.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			headerParent.setOrientation(LinearLayout.VERTICAL);
			headerParent.addView(mHeaderView);
			headerParent.addView(mBrdNotifiView);
			addHeaderView(headerParent);
		}else if(mIsShowpopular_top){
			mGCPopularTopView = View.inflate(context, R.layout.popular_top, null);
			LinearLayout headerParent = new LinearLayout(context);
			headerParent.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			headerParent.setOrientation(LinearLayout.VERTICAL);
			headerParent.addView(mHeaderView);
			headerParent.addView(mGCPopularTopView);
			addHeaderView(headerParent);
		}else {
			addHeaderView(mHeaderView);
		}
		

		// init footer view
		mFooterView = new XListViewFooter(context,mIsShowFootView);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						System.out.println("---eee onGlobalLayout mHeaderViewHeight"+mHeaderViewHeight);
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				}); 
		
		//mShow = new XListViewMessageShow(context);
	}
	
	
	public View getBrdNotifyView() {
		return mBrdNotifiView;
	}

	public View getGCPopularTopView() {
		return mGCPopularTopView;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// make sure XListViewFooter is the last footer view, and only add once.
//		if (mIsFooterReady == false) {
//			mIsFooterReady = true;
//			addFooterView(mFooterView);
//		}
		super.setAdapter(adapter);
		mPinnedHeaderAdapter = (PinnedLabelAdapter) adapter;
	}

	/**
	 * enable or disable pull down refresh feature.
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * enable or disable pull up load more feature.
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}
	
     /**
      * stop refresh even if the listview is gone
      */
	public void stopRefresh2() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight2();
		}
	}
	

	public void stopRefresh(boolean isTrue) {
			if (isTrue) {
				mPullRefreshing = false;
				resetHeaderHeight();
			}
	}
	
	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	/**
	 * set last refresh time
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTimeView.setText(time);
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		
	    if (mIsShowNotifiHeaderView) {
	    	 
	    	if (mHeaderView.getVisiableHeight() > mHeaderViewHeight / 2) {
	    		setSelection(0); 
	    	}
	    	
	    } else {
	    	setSelection(0); // scroll to top each time
	    }
	}

	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
//			mScroller.startScroll(0, 0, 0, 0);
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
		
		// trigger computeScroll
		invalidate();
	}
	
	
	/**
	 * reset header view's height. even if the listview is gone
	 */
	private void resetHeaderHeight2() {
//		int height = mHeaderView.getVisiableHeight();
		int height = AppUtils.dpToPx(PalmchatApp.getApplication(), 60);
		if (height == 0) // not visible.
//			mScroller.startScroll(0, 0, 0, 0);
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
		
		// trigger computeScroll
		invalidate();
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
													// more.
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

//		setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore(this);
		}
	}
	
	private int mDeltaY;
	
	public int getDeltaY() {
		return mDeltaY;
	}
	private boolean isTouchMoved_error;
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY(); 
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mDeltaY = (int)deltaY;
			mLastY = ev.getRawY();
			if (getFirstVisiblePosition() == 0 &&!isTouchMoved_error&&  mHeaderView.isNeedtoAmend()&& deltaY > 0){ 
					isTouchMoved_error=true; 
//					 CommonUtils.showSoftKeyBoard(this);
//					 invalidate();
//					 postInvalidate();
					 ListAdapter listAdapter= getAdapter();
					 if (listAdapter != null) {
						 if (listAdapter instanceof HomeListAdapter) {
							 HomeListAdapter adapter = (HomeListAdapter) listAdapter;
							 adapter.notifyDataSetChanged();
						 }else if(listAdapter instanceof AdapterBroadcastMessages){
							 AdapterBroadcastMessages adapter = (AdapterBroadcastMessages) listAdapter;
							 adapter.notifyDataSetChanged();
						 }
					 }
//					 PalmchatLogUtils.i("WXL", "showSoftKeyBoard--------------------------------");
//						
//					 new Handler().postDelayed(new Runnable() {
//							@Override
//							public void run() {
//								 CommonUtils.closeSoftKeyBoard(XListView.this);
//							}   
//					 },100); 
			}else if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
				// the first item is showing, header has shown or pull down.
				 
				updateHeaderHeight(deltaY / OFFSET_RADIO);     
				invokeOnScrolling();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1 && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
				// last item, already pulled up or want to pull up.
				updateFooterHeight(-deltaY / OFFSET_RADIO); 
			}
			 
			final int childCount = getChildCount()-1;
			if(childCount <= 0) return super.onTouchEvent(ev);
			final int lastBottom = getChildAt(childCount).getBottom() - mHeaderView.getVisiableHeight();
			final int end = getHeight() - getPaddingBottom();
			
			if (getFirstVisiblePosition() + childCount >= mTotalItemCount - 1 && lastBottom >= end && deltaY < 0) {
	        	if (mIsFooterReady == false) {
					mIsFooterReady = true;
					addFooterView(mFooterView);
				}
	        }
			break;
		default:
			isTouchMoved_error=false;
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh
 				if (mEnablePullRefresh
						&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					mPullRefreshing = true;
					mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) {
						mListViewListener.onRefresh(this);
					}
				}
				resetHeaderHeight();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// invoke load more.
				if (mEnablePullLoad
						&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
					startLoadMore();
				}
				resetFooterHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	public void performRefresh(int px) {
			// invoke refresh
			mPullRefreshing = true;
			mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
			if (mListViewListener != null) {
				mListViewListener.onRefresh(this);
			}
			mScroller.startScroll(0, px, 0,  0);
			mHeaderView.setVisiableHeight(px);
			invalidate(); 
	
	}
 
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
//		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL ||scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//			for (TrackElement t : trackElements)
//				t.syncState(view);
//		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
		
	}

	public void setH(int px){
		mHeaderView.setVisiableHeight(px);
	}
	
	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		void onXScrolling(View view);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXListViewListener {
		void onRefresh(View view);

		void onLoadMore(View view);
	}
	
	public void show(){
//		addHeaderView(mHeaderView)
	//	addHeaderView(mShow);
		//addHeaderView(mShow, null, true);
		//addFooterView(mFooterView);
	}
	
	public void romveView(){
		//removeHeaderView(mShow);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if (null != mLabelView) {
			measureChild(mLabelView, widthMeasureSpec, heightMeasureSpec);
			mMeasuredWidth = mLabelView.getMeasuredWidth();
			mMeasuredHeight = mLabelView.getMeasuredHeight();
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		
//		if (null != mLabelView) {
//			mLabelView.layout(0, 0, mMeasuredWidth, mMeasuredHeight);
//			controlPinnedLabel(getFirstVisiblePosition());
//		}
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		
		if (null != mLabelView && mDrawFlag) {
			drawChild(canvas, mLabelView, getDrawingTime());
		}
	}
	
	
	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * HeaderView三种状态的具体处理
	 * 
	 * @param position
	 */
	public void controlPinnedLabel(int position) {
		if (null == mLabelView) {
			return;
		}
		
		if (null == mPinnedHeaderAdapter) {
			return;
		}
		
		int pinnedHeaderState = mPinnedHeaderAdapter.getPinnedLabelState(position);
		if (getFirstVisiblePosition() <= 0) {
			pinnedHeaderState = PinnedLabelAdapter.PINNED_HEADER_GONE;
		}
		switch (pinnedHeaderState) {
		case PinnedLabelAdapter.PINNED_HEADER_GONE:
			mDrawFlag = false;
			break;

		case PinnedLabelAdapter.PINNED_HEADER_VISIBLE:
			mPinnedHeaderAdapter.configurePinnedLabel(mLabelView, position, 0);
			mDrawFlag = true;
			mLabelView.layout(0, 0, mMeasuredWidth, mMeasuredHeight);
			break;
			
		case PinnedLabelAdapter.PINNED_HEADER_PUSHED_UP:
			mPinnedHeaderAdapter.configurePinnedLabel(mLabelView, position, 0);
			mDrawFlag = true;
			
			// 移动位置
			View topItem = getChildAt(0);
			
			if (null != topItem) {
				int bottom = topItem.getBottom();
				int height = mLabelView.getHeight();
				
				int y;
				if (bottom < height) {
					y = bottom - height;
				}else {
					y = 0;
				} 

				if (mLabelView.getTop() != y) {
					mLabelView.layout(0, y, mMeasuredWidth, mMeasuredHeight + y);
				}
	
			}			
			break;
		}
		
	}
	
	
	public interface PinnedLabelAdapter {
		
		int PINNED_HEADER_GONE = 0;
		
		int PINNED_HEADER_VISIBLE = 1;
		
		int PINNED_HEADER_PUSHED_UP = 2;
		
		int getPinnedLabelState(int position);
		
		void configurePinnedLabel(View headerView, int position, int alpaha);
	}

}
