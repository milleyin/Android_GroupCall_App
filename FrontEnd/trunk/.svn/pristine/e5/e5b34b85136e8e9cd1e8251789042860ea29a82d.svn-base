package com.afmobi.palmchat.ui.customview;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;

/**
 * Pull to refresh
 * @author heguiming
 */
public class RefreshableView extends LinearLayout {
	
	/**
	 * Hanldr Looper
	 */
	public static Handler handler = new Handler(Looper.getMainLooper());;
	
	/** Last refresh time */
	public String mTime;

	protected Scroller scroller;
	private int headHeight;

	protected View headRefreshView;
	private ImageView refreshImg;
	private ProgressBar refreshBar;
	private TextView downTextView;
	private TextView timeTextView;

	private RefreshListener refreshListener;

	protected String downTextString;
	protected String releaseTextString;
	protected String refreshText;

	private int lastY;

	/** Initialization state */
	public static final int INIT_REFRESH = 1;
	/** Drop does not exceed a predetermined height TIP: You can refresh drop */
	public static final int PULL_TO_REFRESH = 2;
	/** Drop exceeds a predetermined height TIP: You can refresh release */
	public static final int RELEASE_TO_REFRESH = 3;
	/** Refreshing */
	public static final int REFRESHING = 4;

	protected int refreshState;

//	protected Context mContext;

	/** Arrow flip animation */
	protected RotateAnimation upAnimation;

	protected RotateAnimation downAnimation;
	/** Whether showing head down View */
	private boolean isShowHead = true;

	protected Handler mHandler;
	
	/** Refresh over delay time, in order to avoid rollback failure */
	public final static int DELAY_TIME = 500;

	public RefreshableView(Context context) {
		super(context);
	}

	public RefreshableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
 
			mHandler = new Handler();

			upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			upAnimation.setInterpolator(new LinearInterpolator());
			upAnimation.setDuration(250);
			upAnimation.setFillAfter(true);

			downAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			downAnimation.setInterpolator(new LinearInterpolator());
			downAnimation.setDuration(250);
			downAnimation.setFillAfter(true);
			// Slide Objects
			scroller = new Scroller(getContext());

			// Refresh the view at the top of the view
			headRefreshView = LayoutInflater.from(PalmchatApp.getApplication()).inflate(R.layout.refresh_pull_view, this, false);
			// Indicators view
			refreshImg = (ImageView) headRefreshView.findViewById(R.id.pull_to_refresh_img);
			// Refresh bar
			refreshBar = (ProgressBar) headRefreshView.findViewById(R.id.refresh_progress);
			// Drop down text
			downTextView = (TextView) headRefreshView.findViewById(R.id.refresh_text);
			// Drop down time
			timeTextView = (TextView) headRefreshView.findViewById(R.id.update_time);

			measureView(headRefreshView);
			// Fu initial distance from the top
			headHeight = -headRefreshView.getMeasuredHeight();
			LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, -headHeight);
			lp.topMargin = headHeight;

			addView(headRefreshView, lp);
			setRefreshState(INIT_REFRESH);

			downTextString = PalmchatApp.getApplication().getString(R.string.xlistview_header_hint_ready);
			releaseTextString = PalmchatApp.getApplication().getString(R.string.xlistview_header_hint_normal);
			refreshText = PalmchatApp.getApplication().getString(R.string.loading);
	 
	
	}

	/**
	 * Hide head down View and Refresh functions
	 */
	public void goneHeadView() {
		handler.post(new Runnable() {

			public void run() {
				isShowHead = false;
				headRefreshView.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * Show head down and refresh functions
	 */
	public void visibleHeadView() {
		handler.post(new Runnable() {

			public void run() {
				isShowHead = true;
				headRefreshView.setVisibility(View.VISIBLE);
			}
		});
	}

	/**
	 * Set the current status refresh
	 */
	protected void setRefreshState(int state) {
		refreshState = state;
	}

	/**
	 * Gets the current state value refresh View
	 * 
	 * @return The current state of the control period value
	 */
	public int getRefreshState() {
		return refreshState;
	}

	protected String getState(int state) {
		String tips = "";
		switch (state) {
		case INIT_REFRESH:
			tips = "init";
			break;
		case PULL_TO_REFRESH:
			tips = "pull to refresh";
			break;
		case RELEASE_TO_REFRESH:
			tips = "release to refresh";
			break;
		case REFRESHING:
			tips = "refreshing";
			break;
		default:
			break;
		}
		return tips;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int y = (int) event.getRawY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// Record the y coordinate
			// lastY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// y moving coordinate
			int m = y - lastY;
			// move tension size determined by the distance to determine the distance down
			doMoveMent(m, false);
			// Record the y coordinate of the moment
			lastY = y;
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (refreshState != REFRESHING) {
				fling();
			}
			break;
		}
		return true;
	}

	/**
	 * Changing the head from the top of the padding, in order to move up and down to do
	 * 
	 * @param dy
	 *            Mobile Interval
	 * @param isUpAction
	 *            If it is refreshed when the upward movement, you need to move up along the head view
	 */
	protected void doMoveMent(int dy, boolean isUpAction) {
		LayoutParams lp = getHeaderParams();
		float f1 = lp.topMargin;
		float f2 = 0;
		// If you refresh the status, needs and linkage with listview scrollview or slide up
		if (isUpAction) {
			f2 = dy;
		}
		else {
			f2 = dy * 0.3f;
		}

		int topPadding = (int) (f1 + f2);
		// If you refresh the state, can not exceed the maximum range of the head rolling
		if (refreshState == REFRESHING) {
			if (topPadding > 0) {
				topPadding = 0;
			}
			else if (topPadding < headHeight) {
				topPadding = headHeight;
			}
		}
		lp.topMargin = topPadding;
		// Modified Refresh
		headRefreshView.setLayoutParams(lp);
		invalidate();
		updataArrowState(lp);
	}

	protected void updataArrowState(LayoutParams lp) {
		if (refreshState != REFRESHING) {
			refreshImg.setVisibility(View.VISIBLE);
			// You can let go
			if (lp.topMargin > 0 && refreshState != RELEASE_TO_REFRESH) {
				downTextView.setText(releaseTextString);
				refreshImg.clearAnimation();
				refreshImg.startAnimation(upAnimation);
				setRefreshState(RELEASE_TO_REFRESH);
			}
			// Continue down
			else if (lp.topMargin <= 0 && refreshState != PULL_TO_REFRESH) {
				downTextView.setText(downTextString);
				// Picture itself down, stand in the initial state does not require additional flip animation
				if (refreshState != INIT_REFRESH) {
					refreshImg.clearAnimation();
					refreshImg.startAnimation(downAnimation);
				}
				setRefreshState(PULL_TO_REFRESH);
			}
		}
	}

	/**
	 * up event handling
	 */
	protected void fling() {
		LayoutParams lp = getHeaderParams();
		// Pulled the trigger event to refresh
		if (lp.topMargin > 0 && refreshState == RELEASE_TO_REFRESH && refreshListener != null && isShowHead) {
			refresh(true);
			refreshListener.onRefresh(this);
		}
		else {
			returnInitState();
		}
	}

	/**
	 * Back to the initial state of the head view
	 */
	protected void returnInitState() {
		if (refreshState != INIT_REFRESH) {
			LayoutParams lp = getHeaderParams();
			int i = lp.topMargin;
			scroller.startScroll(0, i, 0, headHeight);
			invalidate();

			setRefreshState(INIT_REFRESH);
			// Restore arrow Pictures
			refreshImg.clearAnimation();
			refreshImg.setImageResource(R.drawable.refresh_arrow_up);
			refreshImg.setVisibility(View.GONE);
			refreshBar.setVisibility(View.GONE);
			// Restore prompt text
			downTextView.setText("");
		}
	}

	/**
	 * Converted Refresh Status
	 */
	protected void refresh(boolean isNeedMove) {
		if (isNeedMove) {
			LayoutParams lp = getHeaderParams();
			int i = lp.topMargin;
			scroller.startScroll(0, i, 0, 0 - i);
			invalidate();
		}

		// Cancel Arrow
		downTextView.setText(refreshText);
		refreshImg.setImageDrawable(null);
		refreshImg.setVisibility(View.GONE);
		refreshBar.setVisibility(View.VISIBLE);

		setRefreshState(REFRESHING);
	}

	/**
	 * Set the current update
	 * 
	 * @param lastUpdated
	 */
	protected void setLastUpdated(String lastUpdated) {
		if (lastUpdated != null) {
			String temp = PalmchatApp.getApplication().getString(R.string.last_updated);
			temp = temp + " " + lastUpdated;
			timeTextView.setVisibility(View.VISIBLE);
			timeTextView.setText(temp);
			mTime = lastUpdated;
		}
		else {
			timeTextView.setVisibility(View.GONE);
			mTime = null;
		}
	}

	/**
	 * View the configuration parameters for the head
	 * 
	 * @return
	 */
	protected LinearLayout.LayoutParams getHeaderParams() {
		LayoutParams lp = (LinearLayout.LayoutParams) this.headRefreshView.getLayoutParams();
		return lp;
	}

	/**
	 * Into the refresh status, guided by an external
	 * 
	 * @param isNeedMove
	 *            Whether you need to move to the head show is being refreshed
	 */
	public void showRefresh(boolean isNeedMove) {
		if (refreshState != REFRESHING) {
			refresh(isNeedMove);
		}
	}

	public void showRefresh() {
		showRefresh(true);
	}

	/**
	 * Complete refresh, back to the initial state
	 */
	public void finishRefresh() {
		// Head to prevent falling when called repeatedly, delay 500ms
		mHandler.postDelayed(new Runnable() {

			public void run() {
				// Set the current update
				SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String lastUpdated = sDate.format(new Date(System.currentTimeMillis()));
				setLastUpdated(lastUpdated);

				returnInitState();
			}
		}, DELAY_TIME);
	}

	public void setRefreshListener(RefreshListener listener) {
		this.refreshListener = listener;
	}

	@Override
	public void computeScroll() {
		if(!isInEditMode()){
			// Slide animation executed, the actual distance change View
			if (scroller.computeScrollOffset()) {
				int i = this.scroller.getCurrY();
				LayoutParams lp = getHeaderParams();
				int k = Math.max(i, headHeight);
				lp.topMargin = k;
				this.headRefreshView.setLayoutParams(lp);
				this.headRefreshView.invalidate();
				invalidate();
			}
			}

		
		
	}

	/**
	 * Distinguish the current slide to the head View events are received
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int action = e.getAction();
		int y = (int) e.getRawY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			int m = y - lastY;
			boolean isFirstLine = canScroll();
			// To filter out some of the single-point event, and the decline in ability to control itself
			if (m > MAX_MOVE_LENGTH && isFirstLine && isShowHead) {
				return true;
			}
			// When in refresh mode, push up the listview or scrollView, need to hide the head
			else if (m < 0 && refreshState == REFRESHING) {
				// With the sub-view of the sliding float
				if (isFirstLine) {
					doMoveMent(m, true);
				}
				else {
					LayoutParams lp = getHeaderParams();
					// Has been out of the head region, but the head if either contingent display, you need to be completely hidden
					int problemZoon = lp.topMargin - headHeight;
					if (problemZoon > 0) {
						doMoveMent(-problemZoon, true);
					}
				}
			}
			lastY = y;
			break;
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return false;
	}

	/** Down gesture distance equivalent to the intensity */
	protected final int MAX_MOVE_LENGTH = 6;

	/**
	 * If listview or ScrollView, because they themselves have the roll axis, you need to distinguish between the sliding events, when they are in the first row, the sliding events need to be passed to the current head of View
	 * 
	 * @return
	 */
	private boolean canScroll() {
		View childView;
		if (getChildCount() > 1) {
			childView = this.getChildAt(1);
			if (childView != null) {
				if (childView instanceof ListView || childView instanceof GridView) {
					// If the first line, you need to determine whether to pass the current head gestures View
					if (((AbsListView) childView).getFirstVisiblePosition() == 0) {
						// Here is the callback adapter's getview (0), need to dynamically get the current List 0th row height
						View v = ((AbsListView) childView).getChildAt(0);
						if (v != null) {
							int top = v.getTop();
							int pad = ((AbsListView) childView).getListPaddingTop();
							// If the top margin of the first line only when the current head gesture passed
							if ((Math.abs(top - pad)) < 3) {
								return true;
							}
							return false;
						}
						// When the ListView has no content, they need to pass events to head View
						return true;
					}
					return false;
				}
				else if (childView instanceof ScrollView) {
					if (((ScrollView) childView).getScrollY() == 0) {
						return true;
					}
					else {
						return false;
					}
				}
			}
		}
		return false;
	}

	/**
	 * The width and height calculation sub-view
	 * 
	 * @param child
	 *            View
	 */
	protected void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		}
		else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * Gets the current height of the head View
	 * 
	 * @return
	 */
	public int getViewHeight() {
		return -headHeight;
	}

	public interface RefreshListener {
		public void onRefresh(RefreshableView view);
	}
}