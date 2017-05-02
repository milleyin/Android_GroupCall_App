package com.afmobi.palmchat.ui.activity.friends;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.eventbusmodel.RefreshFollowerFolloweringOrGroupListEvent;
import com.afmobi.palmchat.eventbusmodel.ShareBroadcastRefreshChatting;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.friends.adapter.GroupListAdapter;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.helper.HelpManager;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfAttachPAMsgInfo;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfResponseComm;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.listener.AfHttpSysListener;
//import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 群组列表界面
 */
public class GroupListActivity extends BaseActivity implements AfHttpSysListener, AfHttpResultListener, OnClickListener {
	private static final String TAG = GroupListActivity.class.getCanonicalName();
	private LooperThread looperThread;
	private ListView mListView;
	private AfPalmchat mAfCorePalmchat;
	private GroupListAdapter mAdapter;
	private View filterImage;
	private View mViewNoGroup;
	private boolean mIsShareBrd;
	private TextView mAddGroup;
	/**
	 * 当前界面离开了，用于取消与中间件的连接
	 */
//	private int mhttpHandle;

	private boolean isShareTag = false;
	private String mShareId;
	private String mTagName;
	private String mTagUrl;
	private String mContent;
	private String senderName;
	private String senderHeaderUrl;
	private int mShareTagPostNum;
	/**
	 * 发送广播的用户ID
	 */
	private String mBroadcastAfid;
	public AfResponseComm.AfChapterInfo afChapterInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAfCorePalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
		mAfCorePalmchat.AfAddHttpSysListener(this);
		looperThread = new LooperThread();
		looperThread.setName(TAG);
		looperThread.start();
//		registerBroadcastReceiver();
		 EventBus.getDefault().register(this);
		// gtf 2014-11-16
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_GLIST);
//		MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_GLIST);
	}
	
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_group_list);
		mIsShareBrd = getIntent().getBooleanExtra("isShowPhoneContacts",false);
		Intent intent = getIntent();
		isShareTag = intent.getBooleanExtra(JsonConstant.KEY_IS_SHARE_TAG, false);
		mShareId = intent.getStringExtra(JsonConstant.KEY_SHARE_ID);
		mTagName = intent.getStringExtra(JsonConstant.KEY_TAG_NAME);
		mTagUrl = intent.getStringExtra(JsonConstant.KEY_TAG_URL);
		mContent = intent.getStringExtra(JsonConstant.KEY_BROADCAST_CONTENT);
		mShareTagPostNum = intent.getIntExtra(JsonConstant.KEY_SHARE_TAG_POST_NUM, 0);
		mBroadcastAfid = intent.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_AFID);
		senderName = intent.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_NAME);
		senderHeaderUrl = intent.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_HEADER_URL);
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			afChapterInfo = (AfResponseComm.AfChapterInfo) bundle.getSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO);
		}
		((TextView)findViewById(R.id.title_text)).setText(R.string.groupchat);
		mListView = (ListView) findViewById(R.id.group_listview);
		mViewNoGroup = findViewById(R.id.no_group_layout);
		findViewById(R.id.back_button).setOnClickListener(this);
		mAddGroup = (TextView) findViewById(R.id.add_group_chat);

		filterImage = findViewById(R.id.op3);
		filterImage.setBackgroundResource(R.drawable.btn_chat_more);
		filterImage.setOnClickListener(this);
		if(mIsShareBrd){ //分享广播时，跳转到此页面时，不显示新增广播
			mAddGroup.setVisibility(View.GONE);
			filterImage.setVisibility(View.GONE);
		}
		else{
			filterImage.setVisibility(View.VISIBLE);
		}
		mAddGroup.setOnClickListener(this);
		ArrayList<AfGrpProfileInfo> grp_list = new ArrayList<AfGrpProfileInfo>();

		List<AfGrpProfileInfo> grp_cacheList = CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).getList();
		for(AfGrpProfileInfo grpProfile : grp_cacheList) {
			if(grpProfile.status == Consts.AFMOBI_GRP_STATUS_ACTIVE) {
				grp_list.add(grpProfile);
			}
		}
		setViewNoGroupLayout(grp_list);
		mAdapter = new GroupListAdapter(context, grp_list,mIsShareBrd);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
//				 add group btn
			/*	if (position == 0) {
					*//**
				 * 创建群组的操作
				 *//*
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.GL_ADD_G);
					MobclickAgent.onEvent(context, ReadyConfigXML.GL_ADD_G);
					Bundle b = new Bundle();
			            ArrayList<String> existList = new ArrayList<String>();
			            existList.add(DefaultValueConstant.PALMCHAT_ID);
			            b.putStringArrayList("palmchat_id", existList);
			            b.putInt("owner_num", existList.size());
			            b.putString("come_page", "first_compepage");
			            Intent intent = new Intent();
			            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			            intent.setClass(context, FriendsActivity.class);
			            intent.putExtras(b);
			            context.startActivity(intent);
				} else {
					//gtf 2014-11-16*/
				final AfGrpProfileInfo infos = mAdapter.getItem(position);
				AfGrpProfileInfo groupListItem = CacheManager.getInstance().searchGrpProfileInfo(infos.afid);
				if (!mIsShareBrd) {
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CONTACT_T_P);
//					MobclickAgent.onEvent(context, ReadyConfigXML.CONTACT_T_P);

					CacheManager.getInstance().getThreadPoolInstance().execute(
							new Thread() {
								public void run() {
									mAfCorePalmchat.AfRecentMsgSetUnread(infos.afid, 0);
									MessagesUtils.setUnreadMsg(infos.afid, 0);
								}
							});

					Bundle bundle = new Bundle();
					if (groupListItem != null) {
						bundle.putInt(JsonConstant.KEY_STATUS, groupListItem.status);
					}
					if (groupListItem != null && null != groupListItem.afid && !"".equals(groupListItem.afid)) {
						bundle.putString(GroupChatActivity.BundleKeys.ROOM_ID, groupListItem.afid);
					}
					if (groupListItem != null && null != groupListItem.name && !"".equals(groupListItem.name)) {
						bundle.putString(GroupChatActivity.BundleKeys.ROOM_NAME, groupListItem.name);
					}
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.setClass(context, GroupChatActivity.class);
					intent.putExtra(JsonConstant.KEY_BACK_TO_DEFAULT, true);
					intent.putExtras(bundle);
					startActivity(intent);
				} else {
					if (groupListItem != null && !TextUtils.isEmpty(groupListItem.afid)) {
						sendBroadcastToGroup(groupListItem);
					}
				}
			}
			//}
		});
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
//				 add group btn
			/*	if (position == 0) {

				} else {*/
				final AfGrpProfileInfo removeItem = mAdapter.getItem(position);
				AfGrpProfileInfo groupListItem = CacheManager.getInstance().searchGrpProfileInfo(removeItem.afid);
				if(!mIsShareBrd) {

					if (!GroupListActivity.this.isFinishing()) {

						/**
						 * 判断是否是自己创建的群组
						 */
						if (CacheManager.getInstance().getMyProfile().afId.equals(removeItem.admin)) {
							/**
							 * 是自己创建的群组的长按操作
							 */
							AppDialog appDialog = new AppDialog(context);
							String content = getString(R.string.editmygroup_destroy_prompt);
							appDialog.createConfirmDialog(context, content, new OnConfirmButtonDialogListener() {
								@Override
								public void onRightButtonClick() {
									if (!CommonUtils.isNetworkAvailable(context)) {
										ToastManager.getInstance().show(context, R.string.network_unavailable);
										return;
									}
									showProgressDialog(R.string.delete);
									int req_flag = Consts.REQ_GRP_ADMIN_QUIT;
									CallGrpOpr(removeItem, req_flag, false);
								}

								@Override
								public void onLeftButtonClick() {

								}
							});
							appDialog.show();
						} else {
							/**
							 * 不是自己创建的群组的相关操作
							 */
							AppDialog appDialog = new AppDialog(context);
							String content = getString(R.string.member_leave_chat_dialog_message);
							appDialog.createConfirmDialog(context, content, new OnConfirmButtonDialogListener() {
								@Override
								public void onRightButtonClick() {
									showProgressDialog(R.string.delete);
									int req_flag = Consts.REQ_GRP_QUIT;
									CallGrpOpr(removeItem, req_flag, false);
								}

								@Override
								public void onLeftButtonClick() {

								}
							});
							appDialog.show();

						}


					}
				}
				else {
					if (groupListItem != null && !TextUtils.isEmpty(groupListItem.afid)) {
						sendBroadcastToGroup(groupListItem);
					}
				}
				//}

				return true;
			}
		});
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	
	String title = "";
	String content = "";
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		title = getString(R.string.groupchat);
		content = getString(R.string.addgroup);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onPageStart(TAG);
//		MobclickAgent.onResume(this);
		initGroupList();
	} 
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		 MobclickAgent.onPageEnd(TAG);  
//		 MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mAfCorePalmchat.AfRemoveHttpSysListener(this);
//		unRegisterBroadcastReceiver();
		looperThread.looper.quit();
		 EventBus.getDefault().unregister(this);
	}

	/**
	 * 此界面不可见的时候取消与中间件的连接
	 */
	/*public void onBack(){
		if(mAfCorePalmchat!=null&&mhttpHandle!=Consts.AF_HTTP_HANDLE_INVALID){
			mAfCorePalmchat.AfHttpCancel(mhttpHandle);
		}
	}*/


	/**
	 * 调用此接口获取到该组的id
	 * @param grpInfo
	 * @param req_flag
	 */
	private void CallGrpOpr(AfGrpProfileInfo grpInfo,int req_flag,boolean isBackgroud) {
		  mAfCorePalmchat.AfHttpGrpOpr(null, null, grpInfo.afid, req_flag,isBackgroud, grpInfo, this);
		
	}
	
	private final static int DELETE_GROUP_SUCCESS = 15;
	private final static int UPDATE_GROUP_PROFILE = 16;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case DELETE_GROUP_SUCCESS://删除成功后刷新列表
				ToastManager.getInstance().show(context, R.string.success);	
				initGroupList();
				break;
			case UPDATE_GROUP_PROFILE:
				initGroupList();
				break;
			case LooperThread.UPDATE_GROUP_LIST:
				List<AfGrpProfileInfo> grp_list = (List<AfGrpProfileInfo>)msg.obj;
				setViewNoGroupLayout(grp_list);
				if(mAdapter != null) {
					mAdapter.setList(grp_list);
					mAdapter.notifyDataSetChanged();
				}
				break;
				case MessagesUtils.MSG_SET_STATUS:
					PalmchatLogUtils.println("---www : MSG_SET_STATUS");
//					sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
					EventBus.getDefault().post(new RefreshChatsListEvent());
					break;
			}
			
		}
		
	};

	/**
	 * 刷新列表
	 */
	private void initGroupList() {
		if (null != looperThread) {
			Handler handler = looperThread.handler;
			if (null != handler) {
				handler.obtainMessage(LooperThread.UPDATE_GROUP_LIST).sendToTarget();
			}
		}

	}
	
//	RefreshGroupReceiver mRefreshGroupReceiver;

	 
/*	private void registerBroadcastReceiver() {
		
		mRefreshGroupReceiver = new RefreshGroupReceiver();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(Constants.REFRESH_GROUP_LIST_ACTION);
		registerReceiver(mRefreshGroupReceiver, filter2);
	}*/

	/**
	 * 销毁广播接收器
	 *//*
	private void unRegisterBroadcastReceiver() {
		unregisterReceiver(mRefreshGroupReceiver);
	}*/

	 
	/*class RefreshGroupReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
		 if(Constants.REFRESH_GROUP_LIST_ACTION.equals(intent.getAction())) {
				initGroupList();
			}
		}
	}*/
    public void onEventMainThread(RefreshFollowerFolloweringOrGroupListEvent event) {
		// TODO Auto-generated method stub
    	if(event.getType() == Constants.REFRESH_GROUPLIST){
    		initGroupList();
   	     } 
	} 
	class LooperThread extends Thread {
		
		private static final int UPDATE_GROUP_LIST = 7002;
		Handler handler;
		Looper looper;
		@Override
		public void run() {
		Looper.prepare();
		looper = Looper.myLooper();
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
//					update group list
				case UPDATE_GROUP_LIST:
					ArrayList<AfGrpProfileInfo> grp_list = new ArrayList<AfGrpProfileInfo>();
					
					List<AfGrpProfileInfo> grp_cacheList = CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).getList();
					for(AfGrpProfileInfo grpProfile : grp_cacheList) {
						if(grpProfile.status == Consts.AFMOBI_GRP_STATUS_ACTIVE) {
							grp_list.add(grpProfile);
						}
						CallGrpOpr(grpProfile,Consts.REQ_GRP_GET_PROFILE,true);
					}
					mHandler.obtainMessage(UPDATE_GROUP_LIST, grp_list).sendToTarget();
					break;
				default:
					break;
				}
			}
			
		};
		
		Looper.loop();
		
		}
		
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		// TODO Auto-generated method stub
		if (!isFinishing()) {
			dismissProgressDialog();
		}
//		mhttpHandle=Consts.AF_HTTP_HANDLE_INVALID;
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			
			case Consts.REQ_GRP_QUIT:
			case Consts.REQ_GRP_ADMIN_QUIT:
				final AfGrpProfileInfo removeItem = (AfGrpProfileInfo) user_data;
				PalmchatLogUtils.d("==","removeItem:"+ removeItem);
				deleteGrp(removeItem);
				break;
				case Consts.REQ_GRP_GET_PROFILE:
					if(mAdapter != null) {
						mAdapter.notifyDataSetChanged();
					}
					break;
				case Consts.REQ_BC_SHARE_BROADCAST:
				case Consts.REQ_BC_SHARE_TAG:
					AfMessageInfo messageInfo = null;
					if(user_data != null && user_data instanceof AfMessageInfo){
						messageInfo = (AfMessageInfo)user_data;
					}
					//向缓存中插入一条数据
					MessagesUtils.insertMsg(messageInfo, true, true);
					Intent intent = new Intent();
					dismissAllDialog();
					AfResponseComm.AfTagShareTagOrBCResp afResponseComm = (AfResponseComm.AfTagShareTagOrBCResp) result;
					if(messageInfo != null) {
						if(afResponseComm != null){
							AfAttachPAMsgInfo afAttachPAMsgInfo = (AfAttachPAMsgInfo) messageInfo.attach;
							if(afAttachPAMsgInfo != null){
								afAttachPAMsgInfo.postnum = afResponseComm.post_number;
								MessagesUtils.updateShareTagMsgPostNumber(messageInfo,afAttachPAMsgInfo._id);
							}
						}
						new StatusThead(messageInfo._id, AfMessageInfo.MESSAGE_SENT, messageInfo.fid,messageInfo.type).start();
					}
					intent.putExtra("isSucess", true);
					setResult(RESULT_OK, intent);
					finish();
					break;
			}
		} else {
			
			switch (flag) {
			case Consts.REQ_GRP_QUIT:
			case Consts.REQ_GRP_ADMIN_QUIT:
				
				final AfGrpProfileInfo removeItem = (AfGrpProfileInfo) user_data;
				// not exist this group 
				if (code == Consts.REQ_CODE_GROUP_NOT_EXIST
						|| code == Consts.REQ_CODE_NO_GROUP
						|| code == Consts.REQ_CODE_REMOVE_MEMBERS_NONE) {
					
					PalmchatLogUtils.d(TAG, "----Server do not exists,delete group.----");
					deleteGrp(removeItem);
					
				} else if (code == Consts.REQ_CODE_UNNETWORK) {
					ToastManager.getInstance().show(context, R.string.network_unavailable);	
					
				} else {
					Consts.getInstance().showToast(context, code, flag,http_code);
				}

				break;
				case Consts.REQ_BC_SHARE_BROADCAST:
				case Consts.REQ_BC_SHARE_TAG:
					AfMessageInfo messageInfo = null;
					if(user_data != null && user_data instanceof AfMessageInfo){
						messageInfo = (AfMessageInfo)user_data;
					}
					Intent intent = new Intent();
					dismissAllDialog();
					intent.putExtra(JsonConstant.KEY_SHARE_OR_SEND_FRIENDS_ERROR_CODE,code);
					if(code == Consts.REQ_CODE_202) {
						MessagesUtils.deleteMessageFromDb(this,messageInfo);
						intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL, getResources().getString(R.string.the_broadcast_doesnt_exist));
						if(afChapterInfo != null){
							sendUpdate_delect_BroadcastList(afChapterInfo);
						}
					}
					else if (code == Consts.REQ_CODE_UNNETWORK || code == Consts.REQ_CODE_CANCEL) {
						//向缓存中插入一条数据
						MessagesUtils.insertMsg(messageInfo, true, true);
						intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL, getResources().getString(R.string.network_unavailable));
					}
					else if(code == Consts.REQ_CODE_TAG_ILLEGAL){
						//intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL, false);
					}
					if(messageInfo != null) {
						new StatusThead(messageInfo._id, AfMessageInfo.MESSAGE_UNSENT, messageInfo.fid,messageInfo.type).start();
					}
					intent.putExtra("isSucess", false);
					setResult(RESULT_OK, intent);
					finish();
			}
			
		}
	}

	/**
	 * 删除群组
	 * @param removeItem
	 */
	private void deleteGrp(final AfGrpProfileInfo removeItem) {
		final AfPalmchat mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		new Thread(new Runnable() {
			public void run() {
				CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).remove(removeItem, true, true);
				AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, removeItem.afid, 0, Integer.MAX_VALUE);
				if (null != recentDataArray && recentDataArray.length > 0) {
					for (AfMessageInfo messageInfo : recentDataArray) {
						mAfCorePalmchat.AfDbGrpMsgRmove(messageInfo);
						MessagesUtils.removeMsg(messageInfo, true, true);
					}
					mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, removeItem.afid);
					/*if (context != null) {
						context.sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
					}*/
					EventBus.getDefault().post(new RefreshChatsListEvent());
				}
				
				mHandler.sendEmptyMessage(DELETE_GROUP_SUCCESS);
			
			}
		}).start();
	}

	@Override
	public boolean AfHttpSysMsgProc(int msg, Object wparam, int lParam) {
		// TODO Auto-generated method stub
		switch (msg) {

		case Consts.AF_SYS_MSG_INIT: 
		case Consts.AFMOBI_SYS_MSG_UPDATE_GRP:
			
			initGroupList();
			break;
		}
		
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		  
	    case R.id.back_button:
				finish();
//				onBack();
				break;
			case R.id.op3:
			case R.id.add_group_chat:
				HelpManager helpManager = new HelpManager(this);
				helpManager.createGroup();
				break;
		default:
			break;
		}
	}

	/** 当没有group时，设置显示提示
	 * @param list
	 */
  private void setViewNoGroupLayout(List<AfGrpProfileInfo> list){
	  if(list != null && list.size() > 0){
		  mViewNoGroup.setVisibility(View.GONE);
		  mListView.setVisibility(View.VISIBLE);
	  }
	  else{
		  mViewNoGroup.setVisibility(View.VISIBLE);
		  mListView.setVisibility(View.GONE);
	  }
  }

	public void sendBroadcastToGroup(AfGrpProfileInfo grpProfileInfo) {
		AppDialog appDialog = new AppDialog(this);
		String showStr = null;
		showStr = CommonUtils.replace("XX", grpProfileInfo.name + "", getResources().getString(R.string.send_to_friend_toast));
		final AfGrpProfileInfo grpProfileInfoTemp = grpProfileInfo;
		appDialog.createConfirmDialog(this, showStr, new AppDialog.OnConfirmButtonDialogListener() {
			@Override
			public void onRightButtonClick() {
				AfAttachPAMsgInfo afAttachPAMsgInfo = new AfAttachPAMsgInfo();
				AfMessageInfo messageInfo = null;
				String strId = null;
				afAttachPAMsgInfo.mid = mShareId;
				afAttachPAMsgInfo.afid = mBroadcastAfid;
					strId = grpProfileInfoTemp.afid;
					// insertBroadcastTextToDb(isPrivate,grpProfileInfoTemp);
				//mTagUrl = "http://www.qq.com";
				if(!TextUtils.isEmpty(mTagUrl)){
					if(!mTagUrl.startsWith(JsonConstant.HTTP_HEAD)){//拼接url的
						mTagUrl = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerInfo()[4] + mTagUrl;
					}
					afAttachPAMsgInfo.imgurl = mTagUrl;
				}
				else{
					afAttachPAMsgInfo.imgurl = "";
				}
				showProgressDialog(R.string.please_wait);
				if(isShareTag){
					afAttachPAMsgInfo.postnum = mShareTagPostNum;
					afAttachPAMsgInfo.content = mTagName;
					afAttachPAMsgInfo.msgtype =AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG;
					messageInfo = getMessageInfo(afAttachPAMsgInfo,strId);
					mAfCorePalmchat.AfHttpAfBCShareTags(strId,System.currentTimeMillis(),mTagName,mTagUrl,messageInfo,GroupListActivity.this);
				}else{
					afAttachPAMsgInfo.content = mContent;
					afAttachPAMsgInfo.msgtype = AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG;
					afAttachPAMsgInfo.local_img_path = senderHeaderUrl;
					afAttachPAMsgInfo.name = senderName;
					messageInfo = getMessageInfo(afAttachPAMsgInfo,strId);
					mAfCorePalmchat.AfHttpAfBCShareBroadCast(strId,System.currentTimeMillis(),mShareId,messageInfo,GroupListActivity.this);
				}

			}

			@Override
			public void onLeftButtonClick() {
				Intent intent = new Intent();
				intent.putExtra("isCancel", true);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		appDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				Intent intent = new Intent();
				intent.putExtra("isCancel", true);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		appDialog.show();
	}

	private AfMessageInfo getMessageInfo(AfAttachPAMsgInfo afAttachPAMsgInfo,String mFriendAfid) {
		AfMessageInfo messageInfo = new AfMessageInfo();//新增
		afAttachPAMsgInfo._id = mAfCorePalmchat.AfDbAttachPAMsgInsert(afAttachPAMsgInfo);

		messageInfo.client_time = System.currentTimeMillis();
//						messageInfo.fromAfId = CacheManager.getInstance().getMyProfile().afId;
		messageInfo.status = AfMessageInfo.MESSAGE_SENTING;
		messageInfo.attach = afAttachPAMsgInfo;
		messageInfo.attach_id = afAttachPAMsgInfo._id;
		if (afAttachPAMsgInfo.msgtype == AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG) {
			messageInfo.msg = afAttachPAMsgInfo.content;
		}
		messageInfo.fromAfId = mFriendAfid;
		messageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_GRP | afAttachPAMsgInfo.msgtype;
		messageInfo._id = mAfCorePalmchat.AfDbGrpMsgInsert(messageInfo);
		messageInfo.action = MessagesUtils.ACTION_INSERT;
		//handler.obtainMessage(handler_MsgType, messageInfo).sendToTarget();
		new StatusThead(messageInfo._id, AfMessageInfo.MESSAGE_SENTING, messageInfo.fid, messageInfo.type).start();
		return messageInfo;
	}

	/**
	 * 更新消息发送状态 线程类
	 */
	public class StatusThead extends Thread {
		private int msgId;
		private int status;
		private String fid;
		private int msgType;

		public StatusThead(int msgId, int status, String fid, int type) {
			this.msgId = msgId;
			this.status = status;
			this.fid = fid;
			msgType = type;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int _id = DefaultValueConstant.LENGTH_0;
			AfMessageInfo msg = null;
//			update recent chats msg status
			if (fid != DefaultValueConstant.MSG_INVALID_FID) {
				_id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS_AND_FID);

			} else {
				_id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS);
			}
			msg = mAfCorePalmchat.AfDbGrpMsgGet(msgId);
			if (msg == null) {
				PalmchatLogUtils.println("Chatting AfDbMsgGet msg:" + msg);
				return;
			}
			PalmchatLogUtils.println("---www : StatusThead isLast msg " + msgId + " msg status: " + status);
			MessagesUtils.setRecentMsgStatus(msg, status);
			mHandler.obtainMessage(MessagesUtils.MSG_SET_STATUS).sendToTarget();
			PalmchatLogUtils.println("update status msg_id " + _id);
			EventBus.getDefault().post(new ShareBroadcastRefreshChatting());
		}
	}

	/**
	 * 发广播通知 各个页面这个广播被删除了
	 *
	 * @param afChapterInfo
	 */
	private void sendUpdate_delect_BroadcastList(AfResponseComm.AfChapterInfo afChapterInfo) {
		afChapterInfo.eventBus_action = Constants.UPDATE_DELECT_BROADCAST;
		EventBus.getDefault().post(afChapterInfo);
	}
}
