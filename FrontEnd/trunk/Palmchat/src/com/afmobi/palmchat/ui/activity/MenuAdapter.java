package com.afmobi.palmchat.ui.activity;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.palmcall.PalmCallFragment;
import com.afmobi.palmchat.ui.customview.CustomFragmentStatePagerAdapter;

public class MenuAdapter extends CustomFragmentStatePagerAdapter {
	ArrayList<Fragment> list = new ArrayList<Fragment>();
	private FragmentManager mFragmentManager;
	private FragmentTransaction mCurTransaction = null;
	private Fragment mCurrentFragment;

	public MenuAdapter(FragmentManager fm) {
		super(fm);
	}

	public MenuAdapter(FragmentManager fm,ArrayList<Fragment> list) {
		super(fm);
		this.list=list;
	}


	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return null==list?0:list.size();
	}
	@Override
	public int getItemPosition(Object object) {
		//if(object instanceof PalmCallFragment){
		PalmchatLogUtils.i("MenuAdapter","getItemPosition");
		return PagerAdapter.POSITION_NONE;
		//return super.getItemPosition(object);
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		if (mCurrentFragment != object) {
			mCurrentFragment = ((Fragment) object);
		}

		super.setPrimaryItem(container, position, object);
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	public Fragment getCurrentFragment() {
		return mCurrentFragment;
	}

	public void updateData(ArrayList<Fragment> list){
		if(null!=list){
			this.list.clear();
			this.list.addAll(list);
			notifyDataSetChanged();
		}

	}

	/**
	 * 清理数据
	 */
	public void cleanData(){
		if(null!=this.list){
			this.list.clear();
			this.list=null;
		}
	}

//	@Override
//	public Object instantiateItem(ViewGroup container, int position) {
//			return super.instantiateItem(container, position);
//			/*Fragment fragment;
//			if(this.list.size() > position) {
//				fragment = (Fragment)this.list.get(position);
//				if(fragment != null) {
//					return fragment;
//				}
//			}
//
//			if(this.mCurTransaction == null) {
//				this.mCurTransaction = this.mFragmentManager.beginTransaction();
//			}
//
//			fragment = this.getItem(position);
//			if(this.mSavedState.size() > position) {
//				Fragment.SavedState fss = (Fragment.SavedState)this.mSavedState.get(position);
//				if(fss != null) {
//					fragment.setInitialSavedState(fss);
//				}
//			}
//
//			while(this.list.size() <= position) {
//				this.list.add(null);
//			}
//
//			fragment.setMenuVisibility(false);
//			fragment.setUserVisibleHint(false);
//			this.list.set(position, fragment);
//			this.mCurTransaction.add(container.getId(), fragment);
//			return fragment;*/
//	}
}
