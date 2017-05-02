package com.afmobi.palmchat.ui.activity.friends;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.ui.activity.invitefriends.PhoneBookActivity;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.friends.adapter.AddFriendsSearchResultAdapter;
import com.afmobi.palmchat.ui.activity.people.PeopleYouMayKnowActivity;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfFriendInfo;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;
public class ContactsAddActivity extends BaseActivity implements OnClickListener, AfHttpResultListener {

	private static final String TAG = ContactsAddActivity.class.getCanonicalName();
	
	private EditText mEditText;
	private AfPalmchat mAfCorePalmchat;
	private View mAddFriendsLayout;
	private ListView mNetSearchResultListView;
	private AddFriendsSearchResultAdapter mNetSearchResultAdapter;
	
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_contacts_add);
		mEditText = (EditText)  findViewById(R.id.search_et);
		findViewById(R.id.additem1).setOnClickListener(this);
		findViewById(R.id.additem2).setOnClickListener(this);
		findViewById(R.id.enter_btn).setOnClickListener(this);
		findViewById(R.id.back_button).setOnClickListener(this);
		mAfCorePalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
		
		mAddFriendsLayout = findViewById(R.id.add_friend_layout);
		mNetSearchResultListView = (ListView) findViewById(R.id.net_search_result_list);
		mNetSearchResultAdapter = new AddFriendsSearchResultAdapter(this);
		mNetSearchResultListView.setAdapter(mNetSearchResultAdapter);
		
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		
		return false;
		
	}
	
	private void back() {
		
		if (mNetSearchResultListView.getVisibility() == View.VISIBLE) {
			mNetSearchResultListView.setVisibility(View.GONE);
			mAddFriendsLayout.setVisibility(View.VISIBLE);
		} else {
			finish();
		}
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onPageStart(TAG);
//		MobclickAgent.onResume(this);
	} 
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		 MobclickAgent.onPageEnd(TAG);  
//		 MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		
		//invite friends
		case R.id.additem1:
			startActivity(new Intent(this, PhoneBookActivity.class));
			//startActivity(new Intent(this, InviteFriendsActivity.class));
			break;
			
			
//        people you may know
		case R.id.additem2:
			startActivity(new Intent(this, PeopleYouMayKnowActivity.class));
			break;
			
//			search btn
		case R.id.enter_btn:
			
			// gtf 2014-11-16
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ID_T_PF);
//			MobclickAgent.onEvent(context, ReadyConfigXML.ID_T_PF);
			
			String str = mEditText.getText().toString();
			
			if (TextUtils.isEmpty(str) || str.length() < 6) {
				ToastManager.getInstance().show(context, R.string.invalid_number);
				
			} else {
				
			showProgressDialog();
			String afid[] = new String[1];
			afid[0] = str;

			mAfCorePalmchat.AfHttpGetInfo(afid, Consts.REQ_GET_INFO_EX, null,0, ContactsAddActivity.this);
			PalmchatLogUtils.println("AfHttpGetInfo:" + afid[0]);
			
			}
			
			break;
			
		case R.id.back_button:
			back();
			break;

		default:
			break;
		}
		
	}
	
	private Dialog dialog;
	private void showProgressDialog() {
		if (dialog == null) {
			dialog = new Dialog(this,R.style.Theme_LargeDialog);
			dialog.setOnKeyListener(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_loading);
			((TextView) dialog.findViewById(R.id.textview_tips)).setText(R.string.searching);
		
		}
			dialog.show();
	}

	private void dismissDialog() {
			if (null != dialog && dialog.isShowing()){
				try {
					dialog.cancel();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
			}
		}
			
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("ContactsAddActivity AfOnResult ----code:" + code + "----flag:" + flag + " result:" + result);
		
		dismissDialog();
		
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			// search afid on net
			case Consts.REQ_GET_INFO_EX: 
				
				if (result != null) {
					final AfFriendInfo afF = (AfFriendInfo) result;
					ArrayList<AfFriendInfo> results = new ArrayList<AfFriendInfo>();
					results.add(afF);
					mNetSearchResultAdapter.setList(results);
					mNetSearchResultAdapter.notifyDataSetChanged();
					
					mNetSearchResultListView.setVisibility(View.VISIBLE);
					mAddFriendsLayout.setVisibility(View.GONE);
					
					new Thread() {
						
						public void run() {
				
							AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afF.afId);
				
							if (afFriendInfo == null) {
							
								MessagesUtils.insertStranger(afF);							
				
							}
					
						};
				
					}.start();
					
					
				} else {
					ToastManager.getInstance().show(context, R.string.afid_not_found);
				}
				
				break;
				
			}
			
		} else {
			
			switch (flag) {
			// search afid on net
			case Consts.REQ_GET_INFO_EX: 
				
				if (code == Consts.REQ_CODE_UNNETWORK) {
					ToastManager.getInstance().show(context, R.string.network_unavailable);	
				} else {
					ToastManager.getInstance().show(context, R.string.afid_not_found);				
				}
				
				break;
				
			}
			
		
			
		}
		
		
	}
}
