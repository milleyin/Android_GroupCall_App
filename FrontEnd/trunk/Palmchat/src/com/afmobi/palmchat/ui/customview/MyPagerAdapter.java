package com.afmobi.palmchat.ui.customview;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MyPagerAdapter extends PagerAdapter {
	ArrayList<View> views;

	public MyPagerAdapter(ArrayList<View> views, String type) {
		super();
		this.views = views;
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		if (position < views.size())
			((ViewPager) container).removeView(views.get(position));
	}

	@Override
	public Object instantiateItem(View container, int position) {
		if (position < views.size()) {
			((ViewPager) container).addView(views.get(position));
			return views.get(position);
		}
		return null;
			
	}

}
