package com.afmobi.palmchat.ui.activity.chattingroom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chattingroom.adapter.ChattingRoomPersonAdapter;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.util.AppUtils;
import com.core.AfChatroomEntryInfo;
import com.core.AfChatroomMemberInfo;
import com.core.AfFriendInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.listener.AfHttpSysListener;
//import com.umeng.analytics.MobclickAgent;

public class ChattingRoomMemberFragment extends BaseFragment implements OnItemClickListener, IXListViewListener, AfHttpResultListener, AfHttpSysListener {

	private static final String TAG = ChattingRoomMemberFragment.class.getCanonicalName();
	private ChattingRoomPersonAdapter mChattingRoomPersonAdapter;

	private ArrayList<AfChatroomMemberInfo> mMembersListData = new ArrayList<AfChatroomMemberInfo>();
	private XListView mListview;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
	private boolean isRefresh = false;
	private static final int REFRESH_ROOM_MEMBER = 8000;
	private LooperThread looperThread;
	private AfPalmchat mAfPalmchat;
	private View mNoMemberView;
	private Button mNomemberBtn;
	public AfProfileInfo afProfileInfo;
	private String afid;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		looperThread = new LooperThread();
		looperThread.setName(TAG);
		looperThread.start();
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_CRLIST_NUM);
//		MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_CRLIST_NUM);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		findViews();
		return mMainView;
	}

	@Override
	public void onResume() {
		super.onResume();
//		MobclickAgent.onPageStart("ChattingRoomMainMemberFragment");
	}
	
	@Override
	public void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd("ChattingRoomMainMemberFragment");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		looperThread.looper.quit();
	}

	/**
	 * 初始化view
	 */
	public void findViews() {
		// TODO Auto-generated method stub
		mAfPalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		afProfileInfo = CacheManager.getInstance().getMyProfile();
		afid = afProfileInfo.afId;
		setContentView(R.layout.chatting_room_right_person_list);
		mListview = (XListView)findViewById(R.id.listview_chatting_room_person_list);
		mListview.setPullLoadEnable(true);
		mListview.setXListViewListener(this);
		mListview.setOnItemClickListener(this);
		mNoMemberView = findViewById(R.id.no_member_layout);
		mNomemberBtn = (Button)findViewById(R.id.member_try_again);
		mNomemberBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mNoMemberView.setVisibility(View.GONE);
				mListview.setVisibility(View.VISIBLE);
				refreshMemberList();
			}
		});
		
		((TextView)findViewById(R.id.title_text)).setText(R.string.members);
		setAdapter();
		
		ImageView imageViewBack = (ImageView)findViewById(R.id.back_button);
		imageViewBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getFragmentManager().popBackStack();
			}
		});
		refreshMemberList();
	}

	/**
	 * 刷新数据
	 */
	private void refreshMemberList() {
			if(!isRefresh){//不是下拉刷新
				if(context != null) {
//					 display loading 
						int px = AppUtils.dpToPx(context, 60);
						mListview.performRefresh(px);
						
					}
			} else {
				mAfPalmchat.AfHttpChatroomGetMemberList(afid, null, this);
				
			}
	}

	/**
	 * 设置数据源
	 */
	private void setAdapter() {
		// TODO Auto-generated method stub
		if(mChattingRoomPersonAdapter == null){
			mChattingRoomPersonAdapter = new ChattingRoomPersonAdapter(context, mMembersListData);
			mListview.setAdapter(mChattingRoomPersonAdapter);
		}else{
			mChattingRoomPersonAdapter.notifyDataSetChanged();
		}
	}

	public void onClick(View v) {
		
	}

	@Override
	public void onRefresh(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.listview_chatting_room_person_list:
			isRefresh = true;
			refreshMemberList();
			break;

		default:
			break;
		}
		
	}


	@Override
	public void onLoadMore(View view) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 停止刷新动画
	 */
	private void stopRefresh() {
		isRefresh = false;
		
		mListview.stopRefresh();
		mListview.stopLoadMore();
		mListview.setRefreshTime(mDateFormat.format(new Date(System.currentTimeMillis())));
	};
	
	@Override
	public boolean AfHttpSysMsgProc(int msg, Object wparam, int lParam) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		PalmchatLogUtils.println("ChattingRoomMainRightActivity  code "+code+"  flag  "+flag+"  result  "+result +"  user_data  "+user_data);
		if(context != null) {
			dismissProgressDialog();
		}
		
		if(isRefresh){
			stopRefresh();
		}

		if(code == Consts.REQ_CODE_SUCCESS) {
			switch(flag) {
				case Consts.REQ_CHATROOM_GET_MEMBER_LIST:
					if(result != null) {
						AfChatroomMemberInfo info[] = (AfChatroomMemberInfo[]) result;
						if(info.length > 0) {
							
							Handler handler = looperThread.looperHandler;
							if (null != handler) {
								Message msg = new Message();
								msg.what = LooperThread.UPDATE_ROOM_MEMBERS;
								msg.obj = Arrays.asList(info);
								handler.sendMessage(msg);
							}
						} else {
							showNoPeopleLayout();
						}
						
						
					} else {
						showNoPeopleLayout();
					}
					
				break;
			}
			
		} else {
			
			if (null != context && !this.context.isFinishing()) {
				if(flag == Consts.REQ_CHATROOM_GET_MEMBER_LIST) {
					if(mChattingRoomPersonAdapter.getCount() == 0) {
						showNoPeopleLayout();
					}
				}
				Consts.getInstance().showToast(context, code, flag,http_code);
			
			}
		}
	}
	
	
	class LooperThread extends Thread {
		
		// 更新缓存数据
				private static final int UPDATE_ROOM_MEMBERS = 6001;

				/**
				 * 线程内部handler
				 */
				Handler looperHandler;

				/**
				 * 线程内部Looper
				 */
				Looper looper;

		
				@Override
				public void run() {
					Looper.prepare();
					looper = Looper.myLooper();
					// 保持当前只有一条线程在执行查看数据操作
					looperHandler = new Handler() {
						public void handleMessage(android.os.Message msg) {
							switch (msg.what) {
							case UPDATE_ROOM_MEMBERS:
								
								Object obj = msg.obj;
								if (null != obj) {
									List<AfChatroomMemberInfo> memberList = (List<AfChatroomMemberInfo>)obj;
									String myAfId = CacheManager.getInstance().getMyProfile().afId;
									int size = memberList.size();
									for (int i = 0; i < size; i++) {
										AfChatroomMemberInfo mutual = memberList.get(i);
										// 搜索所有好友信息
										AfFriendInfo mFriendInfo = CacheManager.getInstance().findAfFriendInfoByAfId(mutual.afid);
										if (null != mFriendInfo) {
											mutual.isAddFriend = true;
										}
										if(myAfId != null && myAfId.equals(mutual.afid)) {
											mutual.isAddFriend = true;
										}
										
//										获取cache中头像信息
										AfFriendInfo mFriendInfo2 = CacheManager.getInstance().searchAllFriendInfo(mutual.afid);
										if(mFriendInfo2 != null) {
											mutual.head_img_path = mFriendInfo2.head_img_path;
										}
										
									}
									
									mHandler.obtainMessage(REFRESH_ROOM_MEMBER, memberList).sendToTarget();
									
								} 
								
								break;
							default:
								break;
							}
						}
					};
					
					Looper.loop();
				}
		
	}
	
	/**
	 * mHandler UI
	 */
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_ROOM_MEMBER:
				List<AfChatroomMemberInfo> membersList = (List<AfChatroomMemberInfo>) msg.obj;
				if (null != membersList && !membersList.isEmpty()) {
					mMembersListData.clear();
					mMembersListData.addAll(membersList);
					mChattingRoomPersonAdapter.notifyDataSetChanged();
				} else {
					showNoPeopleLayout();
				}
				break;

			}
		};
	};
	
	 
	private void showNoPeopleLayout() {
		mListview.setVisibility(View.GONE);
		mNoMemberView.setVisibility(View.VISIBLE);
	}

	/**
	 * 点击Item 跳转 查看信息
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		int pos = (int) mChattingRoomPersonAdapter.getItemId(position);
		if(pos != -1) {
		AfChatroomMemberInfo memberInfo = mMembersListData.get(pos);
		AfFriendInfo afFriendInfo = AfFriendInfo.FriendInfoToAfChatroomMemberInfo(memberInfo);
		
		String myAfId = CacheManager.getInstance().getMyProfile().afId;
		if(myAfId != null && myAfId.equals(afFriendInfo.afId)) {
			toMyProfile(afFriendInfo);
		} else {
			toProfile(afFriendInfo);
		}
		}
	}

	/** 跳转到用户自己的profile
	 * @param afFriendInfo
	 */
	private void toMyProfile(AfFriendInfo afFriendInfo) {
		Bundle bundle = new Bundle();
		bundle.putString(JsonConstant.KEY_AFID, afFriendInfo.afId);
		Intent intent = new Intent(context, MyProfileActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/** 跳转到好友profile
	 * @param afFriendInfo
	 */
	private void toProfile(AfFriendInfo afFriendInfo) {
		
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CR_T_PF);
		
//		MobclickAgent.onEvent(context, ReadyConfigXML.CR_T_PF);
		
		Intent intent = new Intent(context, ProfileActivity.class);
		AfProfileInfo info =AfFriendInfo.friendToProfile(afFriendInfo);
		intent.putExtra(JsonConstant.KEY_PROFILE, info);
		intent.putExtra(JsonConstant.KEY_FLAG, true);
		intent.putExtra(JsonConstant.KEY_AFID, info.afId);
		intent.putExtra(JsonConstant.KEY_USER_MSISDN, info.user_msisdn);
		startActivity(intent);
	}
	
}
