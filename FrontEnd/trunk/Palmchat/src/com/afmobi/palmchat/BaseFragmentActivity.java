package com.afmobi.palmchat;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager.SystemBarConfig;
import com.afmobigroup.gphone.R;
import com.core.Consts;
//import com.umeng.analytics.MobclickAgent;

/**
 * BaseFragmentActivity
 * 
 * @author Transsion
 * @modifyAuthor HJG 2015-12-21 content: extends FragmentActivity -> extends
 *               BaseRouteChangeFragmentActivity
 */

public class BaseFragmentActivity extends  FragmentActivity implements DialogInterface.OnKeyListener {

	public static final String DIALOG_RES = "DIALOG_RES";
	public static final int DIALOG_PROGRESS = 0x8;
	private Dialog dialog;
	/** 屏幕锁 */
	WakeLock mWakeLock;
	/** 样式 */
	private SystemBarTintManager tintManager;
	public SystemBarConfig systemBarConfig;

	@Override
	protected void onCreate(Bundle arg0) {
		
		initTheme();// modify by HJG 2015-11-17
		super.onCreate(arg0);
		if (!this.isTaskRoot()) { // 判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
			// 如果你就放在launcher Activity中话，这里可以直接return了
			Intent mainIntent = getIntent();
			if (mainIntent != null) {
				String action = mainIntent.getAction();
				if (!TextUtils.isEmpty(action) && mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
					finish();
					return;// finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
				}
			}
		}

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "XYTEST");
		if (Consts.mAcquireWakeLock) {
			mWakeLock.acquire();
		}

		MyActivityManager.getScreenManager().pushBaseFragmentActivity(this);
		PalmchatApp.setCurActivity(this);

		tintManager = new SystemBarTintManager(this);
		systemBarConfig = tintManager.getConfig();
	}

	// modify by HJG 2015-11-17
	public void initTheme() {
		if (VERSION.SDK_INT == VERSION_CODES.KITKAT) {
			this.setTheme(R.style.ThemeActivity_19);
		}
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (Consts.mAcquireWakeLock) {
			mWakeLock.release();
		}

		if (this != null && !MyActivityManager.getScreenManager().isAllClear) {
			MyActivityManager.getScreenManager().popBaseFragmentActivity(this);
		}
		PalmchatApp.setCurActivity(null);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		if (this != null && !MyActivityManager.getScreenManager().isAllClear) {
			MyActivityManager.getActivityStack().remove(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
//		MobclickAgent.onPageStart(getClass().getSimpleName());
//		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd(getClass().getSimpleName());
//		MobclickAgent.onPause(this);
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		dismissAllDialog();
		if (null == dialog) {
			dialog = new Dialog(this, R.style.Theme_LargeDialog);
			dialog.setOnKeyListener(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_loading);
		}
		Object obj = args.get(DIALOG_RES);
		if (obj instanceof Integer) {
			((TextView) dialog.findViewById(R.id.textview_tips)).setText((Integer) obj);
		} else {
			((TextView) dialog.findViewById(R.id.textview_tips)).setText(obj.toString());
		}
		return dialog;
	}

	/**
	 * 显示fragment
	 * 
	 * @param resId
	 * @param frag
	 */
	public void replaceFragment(int resId, Fragment frag) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		String tag = frag.getClass().getName();
		ft.replace(resId, frag, tag);
		ft.setTransition(FragmentTransaction.TRANSIT_NONE);
		ft.commit();
	}

	/**
	 * 显示进度对话框
	 * 
	 * @param msg
	 *            需要显示的消息内容
	 */
	public final void showProgressDialog(String msg) {
		if (!this.isFinishing()) {
			Bundle data = new Bundle();
			data.putString(DIALOG_RES, msg);
			try {
				showDialog(DIALOG_PROGRESS, data);
			} catch (Exception e) {
				// TODO: handle exception
				if (e != null) {
					PalmchatLogUtils.e("BaseFragmentActivity", "showProgressDialog(String msg)  " + e.getMessage());
				}
			}
		}
	}

	/**
	 * 显示进度对话框
	 * 
	 * @param resId
	 *            需要显示的资源id
	 */
	public final void showProgressDialog(int resId) {
		if (!isFinishing()) {
			Bundle data = new Bundle();
			data.putInt(DIALOG_RES, resId);
			try {
				showDialog(DIALOG_PROGRESS, data);
			} catch (Exception e) {
				// TODO: handle exception
				if (e != null) {
					PalmchatLogUtils.e("BaseFragmentActivity", "showProgressDialog(int resId)  " + e.getMessage());
				}
			}
		}

	}

	/**
	 * 不显示消进度对话框
	 */
	public void dismissAllDialog() {
		if (null != dialog && dialog.isShowing()) {
			dialog.cancel();
			removeDialog(DIALOG_PROGRESS);
		}
	}

	/**
	 * 不显示消进度对话框
	 */
	public final void dismissProgressDialog() {
		dismissAllDialog();
	}

	/**
	 * 取消进度对话框
	 */
	public final void cancelProgressDialog() {
		if (null != dialog && dialog.isShowing()) {
			dismissDialog(DIALOG_PROGRESS);
		}
	}
}
