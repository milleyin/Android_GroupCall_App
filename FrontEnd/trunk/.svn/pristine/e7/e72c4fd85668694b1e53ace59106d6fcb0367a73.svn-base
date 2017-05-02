package com.afmobi.palmchat.ui.activity.main.building;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.ui.activity.chattingroom.ChattingRoomMainAct;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.core.AfPalmchat;
//import com.umeng.analytics.MobclickAgent;

/**
 * BaseFragment
 * 
 * @author heguiming
 * 
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {

	public Activity context;
	private Display defaultDisplay;
	private int w;
	protected int h;

	protected View mMainView;
	protected LayoutInflater mInflate;
	protected ViewGroup mContainer;
 
	public BaseFragmentActivity fragmentActivity;
	protected AfPalmchat mAfCorePalmchat;
	
	protected PalmchatApp app;
	protected boolean mFragmentVisible;//是否在显示状态  用来判断是否需要弹出错误提示
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAfCorePalmchat = ((PalmchatApp) getActivity().getApplication()).mAfCorePalmchat;
		context = getActivity();
		fragmentActivity = (BaseFragmentActivity)getActivity();
		app = (PalmchatApp) getActivity().getApplication();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MyActivityManager.getScreenManager().pushFragment(this);
		 
		defaultDisplay = getActivity().getWindow().getWindowManager().getDefaultDisplay();
		w = getActivity().getIntent().getIntExtra("display_width", defaultDisplay.getWidth());
		h = getActivity().getIntent().getIntExtra("display_height", defaultDisplay.getHeight());
		AppUtils.WIDTH=w;
		AppUtils.HEIGHT=h;


		requestedOrientation();
	}
	
	/**
	 * Request Screen Orientation
	 */
	public void requestedOrientation(){
		if(CommonUtils.isHD(w,h)){
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else{
			if (w > h) {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		MyActivityManager.getFragmentStack().remove(this);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		mFragmentVisible = isVisibleToUser;
	}

	/**
	 * SetContentView
	 * @param id
	 */
	public void setContentView(int id) {
		this.mMainView = (View) this.mInflate.inflate(id, mContainer, false);
	}

	/**
	 * findViewById
	 * @param id
	 * @return
	 */
	public View findViewById(int id) {
		return mMainView.findViewById(id);
	}

	/**
	 * getString
	 * @param id
	 * @return
	 */
	public String getFragString(int id) {
		return PalmchatApp.getApplication().getResources().getString(id);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mInflate = inflater;
		this.mContainer = container;
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		MyActivityManager.getScreenManager().popFragment(this);
		mMainView = null;
	}

 

	public void replaceFragment(int resId, Fragment frag) {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		String tag = frag.getClass().getName();
		ft.replace(resId, frag, tag);
		ft.setTransition(FragmentTransaction.TRANSIT_NONE);
		ft.commitAllowingStateLoss();
	}
	private Dialog dialog;
	
	public void showProgressDialog(){
	dialog = new Dialog(fragmentActivity,R.style.Theme_LargeDialog);
	dialog.setOnKeyListener(fragmentActivity);
	dialog.setCanceledOnTouchOutside(false);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialog.setContentView(R.layout.dialog_loading);
	dialog.show();
	}

	public void dismissProgressDialog() {
	if (fragmentActivity != null) {
		if (null != dialog && dialog.isShowing()){
			try {
				dialog.cancel();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		}
	}

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(getClass().getSimpleName());
//        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(getClass().getSimpleName());
//        MobclickAgent.onPause(getActivity());
    }
}
