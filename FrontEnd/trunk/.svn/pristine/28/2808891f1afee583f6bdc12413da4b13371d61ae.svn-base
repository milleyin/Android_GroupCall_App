
package com.afmobi.palmchat.ui.activity.chattingroom;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.PalmchatApp.MessageReceiver;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.core.AfChatroomEntryInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.cache.CacheManager;

public class ChattingRoomMainAct extends BaseFragmentActivity implements MessageReceiver{
	public ChattingRoomMainFragment mainFragment;
	private ChattingRoomMemberFragment mainRightFragment;
	private AfPalmchat mAfCorePalmchat;
	private PalmchatApp app;
	private AfChatroomEntryInfo c;
	
	public ChattingRoomMainAct(){}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slidingmenu_main);
        
        c = (AfChatroomEntryInfo) getIntent().getSerializableExtra(JsonConstant.KEY_CHATTING_ROOM_MODEL);
        PalmchatLogUtils.println("ChattingRoomMainAct  afChatroomDetail  "+c);

		mainFragment = new ChattingRoomMainFragment();
		mainFragment.setAfChatroomEntry(c);
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.slidingmenu_layout, mainFragment)
			//.addToBackStack(null)
			.commit();
		
		app = ((PalmchatApp)getApplication());
		mAfCorePalmchat = app.mAfCorePalmchat;
		PalmchatApp.getApplication().addMessageReceiver(this);
    }
    @Override
    protected void onResume() {
		CommonUtils.cancelAtMeNoticefacation(getApplicationContext());
		SharePreferenceUtils.getInstance(getApplicationContext()).setIsAtme(CacheManager.getInstance().getMyProfile().afId,false);
		super.onResume();
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(mainFragment.emojjView.getViewRoot().getVisibility() != View.GONE){
				mainFragment.emojjView.getViewRoot().setVisibility(View.GONE);
				mainFragment.closeEmotions();
				return false;
			}else{
				int num = getSupportFragmentManager().getBackStackEntryCount();
				if(num == 0) {
					mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_CHATROOM, c.cid);
					mAfCorePalmchat.AfChatroomSetServerOpr(Consts.CHATROOM_OPR_TYPE_CLEAR, null, null, null, null);
					finish();
					return true;
				}
			}
			return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		PalmchatLogUtils.println("ChattingRoomMainAct onStart");
	}
	
	@Override
	protected void onResumeFragments() {
		// TODO Auto-generated method stub
		super.onResumeFragments();
		PalmchatLogUtils.println("ChattingRoomMainAct onResumeFragments");
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		PalmchatLogUtils.println("ChattingRoomMainAct onStop");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		app.setMessageReceiver(null);
		PalmchatApp.getApplication().removeMessageReceiver(this);
		PalmchatLogUtils.println("ChattingRoomMainAct onDestroy");
	}

	@Override
	public void handleMessageFromServer(AfMessageInfo afMessageInfo) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("ChattingRoomMainAct handleMessage "+afMessageInfo);
		PalmchatLogUtils.println("ChattingRoomMainAct mainFragment "+mainFragment);
		if(mainFragment != null){
			//通知界面更新
			if(!isFinishing()){
				mainFragment.noticefy(afMessageInfo);
			}
		}
	}

	public void emojj_del() {
		// TODO Auto-generated method stub
		if(null != mainFragment){
			mainFragment.emojj_del();
		}
	}
}
