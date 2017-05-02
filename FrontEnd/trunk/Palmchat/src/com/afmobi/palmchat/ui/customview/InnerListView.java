
package com.afmobi.palmchat.ui.customview;

import com.afmobigroup.gphone.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class InnerListView extends ListView {

	private View footerview, headView, lin_loadmore, ll_head;

	public InnerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		footerview = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.listview_footer, null);
		headView = LayoutInflater.from(getContext()).inflate(R.layout.include_profile_top, null);
		ll_head = headView.findViewById(R.id.ll_head);
		lin_loadmore = footerview.findViewById(R.id.lin_loadmore22);
		this.addHeaderView(headView, null, false);
		this.addFooterView(footerview, null, false);
	}

	/**
	 * hide head
	 */
	public View getHeadView() {
		return ll_head;
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide_loadmore() {
		lin_loadmore.setVisibility(View.GONE);
	}

	/**
	 * show footer
	 */
	public void show_loadmore() {
		lin_loadmore.setVisibility(View.VISIBLE);
		// postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// smoothScrollBy(getScrollY() + lin_loadmore.getHeight(), 100);
		// }
		// }, 50);
	}

	/**
	 * 获取listview滑动距离
	 * 
	 * @return
	 */
	public int defGetScrollY() {
		View c = getChildAt(0);
		if (c == null) {
			return 0;
		}
		int firstVisiblePosition = getFirstVisiblePosition();
		int top = c.getTop();
		return -top + firstVisiblePosition * c.getHeight();
	}

}
