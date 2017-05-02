package com.afmobi.palmchat.ui.activity.setting;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.fb.FeedbackAgent;
//import com.umeng.fb.model.Conversation;
//import com.umeng.fb.model.DevReply;
//import com.umeng.fb.model.Reply;
//import com.umeng.fb.model.UserInfo;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 反馈界面
 *
 */
public class FeedbackActivity extends BaseActivity implements OnClickListener, AfHttpResultListener{
	/**反馈编辑框*/
	private EditText editFeedback;
	/**本地接口类*/
	private AfPalmchat mAfCorePalmchat;
	/**id号*/
	private String mAfid;
	/**中间层返回码*/
	private int mHttpHanlde;
	/**反馈按钮*/
	private RelativeLayout mRl_Send ;
	private View mBtn_Back;
	private Button mSaveButton;
	/**友盟反馈统计*/
//	private FeedbackAgent mFeedbackAgent;
//	/**友盟反馈统计结构*/
//	private Conversation mDefaultConversation;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			if( mHttpHanlde != Consts.AF_HTTP_HANDLE_INVALID){
				break;
			}
			final String msg = editFeedback.getText().toString();
			if (CommonUtils.isEmpty(msg)) {
				ToastManager.getInstance().show(FeedbackActivity.this, R.string.fd_tip_enter);
			} else {
				showProgressDialog(R.string.Sending);
				mHttpHanlde = mAfCorePalmchat.AfHttpFeedback(mAfid, msg, null, this);
				
//				sendUmengFeedback();
			}
			break;
		case R.id.back_button:
		case R.id.back_layout:
			finish();
			break;
		}
	}
	
	/**
	 * 发送反馈
	 */
	/*private void sendUmengFeedback() {
		// TODO Auto-generated method stub
		
		String editStr = editFeedback.getText().toString().trim();
		AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
		
		mDefaultConversation.addUserReply(editStr);
		mDefaultConversation.getReplyList();
		UserInfo info = mFeedbackAgent.getUserInfo();
		if (info == null)
            info = new UserInfo();
		Map<String, String> contact = info.getContact();
		if (contact == null)
             contact = new HashMap<String, String>();
		
		contact.put(JsonConstant.KEY_CONTACT_INFO, myProfile.afId+";"+myProfile.email+";"+myProfile.phone);
		info.setContact(contact);
		
		mFeedbackAgent.setUserInfo(info);
		mFeedbackAgent.sync();
	}
	
	void sync() {
		Conversation.SyncListener listener = new Conversation.SyncListener() {

			@Override
			public void onSendUserReply(List<Reply> replyList) {
				//adapter.notifyDataSetChanged();
			}

			@Override
			public void onReceiveDevReply(List<DevReply> replyList) {
			}
		};
		mDefaultConversation.sync(listener);
	}*/

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result,Object user_data) {
		if( flag == Consts.REQ_FLAG_FEEDBACK) {
			mHttpHanlde = Consts.AF_HTTP_HANDLE_INVALID;
			dismissAllDialog();
			if(Consts.REQ_CODE_SUCCESS  == code ){
				ToastManager.getInstance().show(FeedbackActivity.this, R.string.thanks_for_feedback);
				finish();
			}else{
				ToastManager.getInstance().show(FeedbackActivity.this, R.string.network_unavailable);
			}
		}
	}

	@Override
	public void init() {
		setContentView(R.layout.feedback);
		AfProfileInfo profile  = CacheManager.getInstance().getMyProfile();
		if( null == profile || TextUtils.isEmpty(profile.afId)){
			this.finish();
		}
		mHttpHanlde = Consts.AF_HTTP_HANDLE_INVALID;
		mAfid = profile.afId;
		mAfCorePalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
		
		
		((TextView) findViewById(R.id.title_text)).setText(R.string.feedback);
		editFeedback = (EditText) findViewById(R.id.feedback_edit);
	/*	mRl_Send = (RelativeLayout) findViewById(R.id.ok_button);*/
		mBtn_Back =  findViewById(R.id.back_button);
		mBtn_Back.setOnClickListener(this);
		mSaveButton = (Button) findViewById(R.id.btn_ok);
		mSaveButton.setText(R.string.sent);
		mSaveButton.setVisibility(View.VISIBLE);
		mSaveButton.setOnClickListener(this);
/*		mRl_Send.setOnClickListener(this);*/
		
//		mFeedbackAgent = new FeedbackAgent(this); 
//		mDefaultConversation =  new Conversation(FeedbackActivity.this);
		
	}
	
	@Override
	public void findViews() {
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return false;
	}
	
	@Override
	public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
		return true;
	}

}
