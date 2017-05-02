package com.afmobi.palmchat;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.LaunchActivity;
import com.afmobi.palmchat.ui.activity.SoftkeyboardActivity;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.chats.PopMessageActivity;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.groupchat.PopGroupMessageActivity;
import com.afmobi.palmchat.ui.activity.login.FacebookCompleteProfileActivity;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;
import com.afmobi.palmchat.ui.activity.palmcall.PalmCallRecentsActivity;
import com.afmobi.palmchat.ui.activity.palmcall.PalmCallSendVoiceActivity;
import com.afmobi.palmchat.ui.activity.profile.MyProfileDetailActivity;
import com.afmobi.palmchat.ui.activity.profile.MyQrCodeActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.AccountsChattingActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountsHistoryActivity;
import com.afmobi.palmchat.ui.activity.qrcode.activity.CaptureActivity;
import com.afmobi.palmchat.ui.activity.qrcode.activity.QrcodeScanningTextActivity;
import com.afmobi.palmchat.ui.activity.register.CompleteProfileActivity;
import com.afmobi.palmchat.ui.activity.social.BroadcastDetailActivity;
import com.afmobi.palmchat.ui.activity.social.EditBroadcastPictureActivity;
import com.afmobi.palmchat.ui.activity.social.VisionFilterActivity;
import com.afmobi.palmchat.ui.activity.tagpage.TagsActivity;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager.SystemBarConfig;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobigroup.gphone.R;
import com.core.crash.CoreCrashHandler;
//import com.umeng.analytics.MobclickAgent;
//import com.umeng.fb.FeedbackAgent;
//import com.umeng.fb.model.Conversation;
//import com.umeng.fb.model.DevReply;
//import com.umeng.fb.model.Reply;
//import com.umeng.fb.model.UserInfo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

/**
 * BaseActivity
 * 
 * @author heguiming
 * @modifyAuthor HJG 2015-12-21 content: extends Activity -> extends BaseRouteChangeActivity
 * 
 */
public abstract class BaseActivity extends Activity implements DialogInterface.OnKeyListener{

	protected static final String TAG = BaseActivity.class.getCanonicalName();
	public static final String DIALOG_RES = "DIALOG_RES";
	public static final int DIALOG_PROGRESS = 0x8;
	protected Context context;
	/**application*/
	protected PalmchatApp app;
	protected int mHandler;
    protected Dialog dialog;
	private Toast toast;
	
	public SystemBarTintManager tintManager;
	public SystemBarConfig systemBarConfig ;
	/**友盟的-记录user信息*/
//	protected FeedbackAgent mAgentFeedbackAgent;
	/**友盟的发起会话功能，就是发送用户的意见反馈*/
//	protected Conversation mDefaultConversation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	 	if(VERSION.SDK_INT == VERSION_CODES.KITKAT) {
			
            //transparent theme activity don't set this theme
			if (! ( this instanceof PopMessageActivity 
					|| this instanceof PopGroupMessageActivity || 
					this instanceof Chatting || this instanceof GroupChatActivity || this instanceof LaunchActivity
					|| this instanceof SoftkeyboardActivity || this instanceof CompleteProfileActivity || this instanceof FacebookCompleteProfileActivity
				    || this instanceof AccountsChattingActivity
					||this instanceof BroadcastDetailActivity||this instanceof VisionFilterActivity
                    ||this instanceof EditBroadcastPictureActivity||this instanceof TagsActivity||
                    this instanceof LoginActivity ||this instanceof PublicAccountsHistoryActivity
					|| this instanceof PalmCallRecentsActivity || this instanceof MyQrCodeActivity
					|| this instanceof CaptureActivity || this instanceof QrcodeScanningTextActivity
			        || this instanceof MyProfileDetailActivity||this instanceof PalmCallSendVoiceActivity)) {
				this.setTheme(R.style.ThemeActivity_19);
			}
		} 
		
		super.onCreate(savedInstanceState);
		if(!this.isTaskRoot()) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来  
			  //如果你就放在launcher Activity中话，这里可以直接return了  
			                    Intent mainIntent=getIntent();   
			if (mainIntent != null) {
			  String action=mainIntent.getAction();  
				if (!TextUtils.isEmpty(action) && mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
			      finish();   
			      return;//finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception  
			  }  
			}
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		app = (PalmchatApp) getApplication();
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		MyActivityManager.getScreenManager().pushActivity(this);
		PalmchatApp.setCurActivity(this);
		CoreCrashHandler.setContext(this);
		findViews();
		init();
		Display defaultDisplay = getWindow().getWindowManager().getDefaultDisplay();
		int w = getIntent().getIntExtra("display_width", defaultDisplay.getWidth());
		int h = getIntent().getIntExtra("display_height", defaultDisplay.getHeight());
		PalmchatLogUtils.println("wwwwwwwww:" + w + "  hhhhhhhhh:" + h);
		if (CommonUtils.isHD(w, h)) {
			super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			if (w > h) {
				super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}
		
		//umeng设置
//		mAgentFeedbackAgent = new FeedbackAgent(this);
//		mDefaultConversation = new Conversation(this);
		//样式
		tintManager = new SystemBarTintManager(this);
		systemBarConfig = tintManager.getConfig();		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		CommonUtils.closeSoftKeyBoard(getWindow().getDecorView());
//		PalmchatLogUtils.println("BaseActivity onTouchEvent onDown");
		return super.onTouchEvent(event);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.getParent();
	}

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(getClass().getSimpleName());
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(getClass().getSimpleName());
//        MobclickAgent.onPause(this);
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
	
	@Override
	public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
		return false;
	}
	
	@Override
	public void finish() {
		super.finish();
		if(!MyActivityManager.getScreenManager().isAllClear) {
			MyActivityManager.getActivityStack().remove(this);
		}
	}
	
	@Override
	protected void onDestroy() {
		// MyActivityManager.getActivityStack().remove(this);
		CoreCrashHandler.setContext(null);
		PalmchatApp.setCurActivity(null);
		if(!MyActivityManager.getScreenManager().isAllClear) {
			MyActivityManager.getActivityStack().remove(this);
		}
		super.onDestroy();
	}
	
	/**
	 * 界面跳转
	 * @param cls  目标类
	 * @param bundle 传递的bundle
	 * @param isReturn 是否需要返回
	 * @param requestCode 请求码
	 * @param isFinish 是否结束当前activity
	 */
	public void jumpToPage(Class<?> cls, Bundle bundle, boolean isReturn, int requestCode, boolean isFinish) {
		if (cls == null) {
			return;
		}

		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}

		if (isReturn) {
			startActivityForResult(intent, requestCode);
		} else {

			try {
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (isFinish) {
			finish();
		}
	}

/*	void sync() {
		Conversation.SyncListener listener = new Conversation.SyncListener() {

			@Override
			public void onSendUserReply(List<Reply> replyList) {
				// adapter.notifyDataSetChanged();
			}

			@Override
			public void onReceiveDevReply(List<DevReply> replyList) {
			}
		};
		mDefaultConversation.sync(listener);
	}*/

	/**
	 * 友盟log发送
	 * @param httpResponseCode
	 * @param flag
	 * @param code
	 */
	/*public void sendUmengLog(int httpResponseCode, int flag, int code) {
		// TODO Auto-generated method stub
		String dpUrl = PalmchatApp.getApplication().mAfCorePalmchat.AfGetDispatchUrl();
		String ip = PalmchatApp.getApplication().mAfCorePalmchat.AfGetLoginService();
		String flagStr = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerCmd(flag);
		String[] arrayStr = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerInfo();
		String serverUrl = null;
		String serverToken = null;
		String serverSession = null;
		
		if (arrayStr != null && arrayStr.length > 0) {
			serverUrl = arrayStr[0];
			serverToken = arrayStr[1];
			serverSession = arrayStr[2];
		}
		String editStr = "dpUrl:" + dpUrl + "  ip:  "+ip+" flag:" + flag + " requstAPI:" + flagStr + " httpResponseCode:" + httpResponseCode + " code:" + code + " serverUrl:" + serverUrl + " serverToken:" + serverToken + " serverSession:" + serverSession + " versionName:" + PalmchatApp.getOsInfo().getOsVersion();

		AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
		PalmchatLogUtils.println("umeng:"+myProfile.afId + ";" + myProfile.email + ";" + myProfile.phone + " editStr " + editStr);
		mDefaultConversation.addUserReply(editStr);
		//用户信息
		UserInfo info = mAgentFeedbackAgent.getUserInfo();
		if (info == null) {
			info = new UserInfo();	
		}
		Map<String, String> contact = info.getContact();
		if (contact == null) {
			contact = new HashMap<String, String>();
		}
		contact.put(JsonConstant.KEY_CONTACT_INFO, myProfile.afId + ";" + myProfile.email + ";" + myProfile.phone);
		info.setContact(contact);

//		mAgentFeedbackAgent.setUserInfo(info);
//		mAgentFeedbackAgent.sync();

	}*/

	/**
	 * 显示进度对话框
	 * @param msg 需要显示的消息内容
	 */
	public void showProgressDialog(String msg) {
		if (!this.isFinishing()) {
			Bundle data = new Bundle();
			data.putString(DIALOG_RES, msg);
			showDialog(DIALOG_PROGRESS, data);
		}
	}

	/**
	 * 显示进度对话框
	 * @param resId 需要显示的资源id
	 */
	public final void showProgressDialog(int resId) {
		if (!this.isFinishing()) {
			Bundle data = new Bundle();
			data.putInt(DIALOG_RES, resId);
			showDialog(DIALOG_PROGRESS, data);
		}
	}

	/**
	 * toast显示      备注:不应该显示资源id
	 * @param resId
	 */
	public void showToast(int resId) {
		if (null != toast) {
			toast.setText(resId);
			toast.show();
		}
	}
	
	/**
	 * 不显示所以对话框
	 */
	public void dismissAllDialog() {
		if (null != dialog && dialog.isShowing()) {
			dialog.cancel();
			removeDialog(DIALOG_PROGRESS);
		}
		dialog = null;
	}

	/**
	 *不显示消进度对话框
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
		dialog = null;
	}
	
	/**
	 * view初始化
	 */
	public abstract void findViews();

	/**
	 * 数据初始化
	 */
	public abstract void init();

}
