package com.afmobi.palmchat.ui.activity.chattingroom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.ChatRoomListEvent;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chattingroom.adapter.ChattingRoomAdapter;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfChatroomDetail;
import com.core.AfChatroomEntryInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import de.greenrobot.event.EventBus;

public class ChattingRoomListActivity extends BaseActivity  implements OnItemClickListener , IXListViewListener, AfHttpResultListener {

	private List<AfChatroomDetail> list = new ArrayList<AfChatroomDetail>();
	private XListView vXListview;
	private ChattingRoomAdapter chattingRoomAdapter;
	
	private boolean isRefresh = false;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
	private static final String TAG = ChattingRoomListActivity.class.getCanonicalName();
	private ImageView vImageViewBack,vImageViewRight;
	
	private Dialog dialog;
	private LooperThread looperThread;
	private AfPalmchat mAfCorePalmchat;
//	10 minutes
	private static final long mStayTime = 1000 * 60 * 10;
	public ChattingRoomListActivity(){
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		looperThread = new LooperThread();
		looperThread.setName(TAG);
		looperThread.start();
	}
	
	 
	private void initViews() {
		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		
		((TextView)findViewById(R.id.title_text)).setText(R.string.chatting_room);
		vImageViewBack = (ImageView)findViewById(R.id.back_button);

		vImageViewBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		vImageViewRight = (ImageView)findViewById(R.id.op2);
		vImageViewRight.setVisibility(View.GONE);
		/*vImageViewRight.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(menu != null){
					menu.showSecondaryMenu();
				}
			}
		});*/
		vXListview = (XListView)findViewById(R.id.listview_chatting_room);
		vXListview.setOnItemClickListener(this);
		vXListview.setPullLoadEnable(true);
		vXListview.setXListViewListener(this);
		setAdapter();
		getChattingRoomData();

		EventBus.getDefault().register(this);
	
	}

	 
	
//	stay in chatroom list more than 10 minutes, set isEnter flag = false;
	private void scheduleStayTime() {
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("---eee: schedule");
				MessagesUtils.setChatroomExit();
				List<AfChatroomDetail> roomList = CacheManager.getInstance().getAfChatroomDetails();
				if(roomList != null) {
					Handler handler = looperThread.looperHandler;
					if (null != handler) {
						handler.obtainMessage(LooperThread.UPDATE_ROOM_LIST, roomList).sendToTarget();
					}
				}
			
			}
		}, mStayTime);
	
	}
	
	private void cancelStayTime() {
		mHandler.removeCallbacksAndMessages(null);
	}
	
	private void setAdapter() {
		// TODO Auto-generated method stub
		if(chattingRoomAdapter == null){
			chattingRoomAdapter = new ChattingRoomAdapter(context, list);
			vXListview.setAdapter(chattingRoomAdapter);
		}else{
			chattingRoomAdapter.notifyDataSetChanged();
		}
	}


	@Override
	public void onRefresh(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.listview_chatting_room:
			isRefresh = true;
			getChattingRoomData();
			break;
		default:
			break;
		}
	}

	
	
	private void stopRefresh() {
		isRefresh = false;
		
		vXListview.stopRefresh();
		vXListview.stopLoadMore();
		vXListview.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
	};

	
	/**获取房间列表*/
	private void getChattingRoomData() {
		// TODO Auto-generated method stub
		if(!isRefresh){//不是下拉刷新
			showProgressDialog(R.string.please_wait);//	showProgressDialog();
		}
		list = CacheManager.getInstance().getAfChatroomDetails();
		long time = CacheManager.getInstance().getChatroomListTime();
		if(list != null && list.size() > 0 && CommonUtils.isOverTenMini(System.currentTimeMillis(), time) && !isRefresh){
			dismissProgressDialog();
			stopRefresh();
			chattingRoomAdapter.setList(list);
			setAdapter();
		}else{
			mAfCorePalmchat.AfHttpChatroomGetList(null, 0,  this);
		}
	}

	@Override
	public void onLoadMore(View view) {
		// TODO Auto-generated method stub
		
	}

	private int handle = Consts.AF_HTTP_HANDLE_INVALID;
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		// TODO Auto-generated method stub
		int pos = (int) chattingRoomAdapter.getItemId(position);//MyActivityManager.getActivityStack().remove(this);app.getMessageReceiver();
//		showProgressDialog(getString(R.string.please_wait));
		List<AfChatroomDetail> list = chattingRoomAdapter.getList();
	    PalmchatLogUtils.e("entryRoom", " position  "+position);
	    if(list != null && list.size() > 0 && pos != -1){
	    	int size = list.size();
	    	if(size-1 >= pos){
	    		showProgressDialog(R.string.please_wait);
	    		AfChatroomDetail c = list.get(pos);
	    		mAfCorePalmchat.AfChatroomSetServerOpr(Consts.CHATROOM_OPR_TYPE_SERVER, c.cip, c.cport, c.cid,PalmchatApp.getOsInfo().getCountry(context));
	    		if(handle != Consts.AF_HTTP_HANDLE_INVALID){
	    			mAfCorePalmchat.AfRemoveHttpListener(handle);
	    		}
	    		handle = mAfCorePalmchat.AfHttpChatroomEntry(PalmchatApp.getOsInfo().getCountry(context), c.cid, c, this);
	    	}else{
//	    		ToastManager.getInstance().show(context, getFragString(R.string.network_unavailable));
//	    		PalmchatLogUtils.e("entryRoom", "roomName  "+list.get(i).getRoomName());
	    	}
	    }
	}
	
	
	private void toChattingRoomMessage(AfChatroomEntryInfo afChatroomEntryInfo) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context,ChattingRoomMainAct.class);
		intent.putExtra(JsonConstant.KEY_CHATTING_ROOM_MODEL, afChatroomEntryInfo);
		startActivity(intent);
	}

 
	
	private static final int REFRESH_ROOM_LIST = 1000;
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_ROOM_LIST:
				List<AfChatroomDetail> roomList = (List<AfChatroomDetail>) msg.obj;
				CacheManager.getInstance().setAfChatroomDetails(roomList);
				CacheManager.getInstance().setChatroomListTime(System.currentTimeMillis());
				chattingRoomAdapter.setList(roomList);
				setAdapter();
				break;
		
			}
		};
	};
	
	class LooperThread extends Thread {
		
		// 更新缓存数据
				private static final int UPDATE_ROOM_LIST = 1001;

				private static final int ENTRY_ROOM = 1002;
				
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
							case UPDATE_ROOM_LIST:
								
								Object obj = msg.obj;
								if (null != obj) {
									
									List<AfChatroomDetail> listTemp = (List<AfChatroomDetail>)obj;
//									房间列表未读数处理
									for(AfChatroomDetail afchat : listTemp) {
										int chatRoomUnread = mAfCorePalmchat.AfDbMsgGetUnreadSize(AfMessageInfo.MESSAGE_TYPE_MASK_CHATROOM, afchat.cid);
											 afchat.unreadNum = chatRoomUnread;
									}
									mHandler.obtainMessage(REFRESH_ROOM_LIST, listTemp).sendToTarget();
									
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
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		CommonUtils.cancelAtMeNoticefacation(getApplicationContext());
		SharePreferenceUtils.getInstance(getApplicationContext()).setIsAtme( CacheManager.getInstance().getMyProfile().afId, false);
		List<AfChatroomDetail> roomList = CacheManager.getInstance().getAfChatroomDetails();
		if(roomList != null) {//MyActivityManager.getActivityStack().remove(this);
			Handler handler = looperThread.looperHandler;
			if (null != handler) {
				handler.obtainMessage(LooperThread.UPDATE_ROOM_LIST, roomList).sendToTarget();
			}
		}
		
		scheduleStayTime();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		cancelStayTime();
	}
	
	@Override
	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		PalmchatLogUtils.println("ChatitngRoomListFragment  code "+code+"  flag  "+flag+"  result  "+result +"  user_data  "+user_data);
		if(handle  == httpHandle){
			handle = Consts.AF_HTTP_HANDLE_INVALID;
		}
//		if(fragmentActivity != null) {
			dismissProgressDialog();
//		}
		if(isRefresh){
	    	stopRefresh();
	    }
		if(code == Consts.REQ_CODE_SUCCESS){
//			if(isAdded()) {
			if(flag == Consts.REQ_CHATROOM_GET_LIST){
				AfChatroomDetail[] afChatroomDetails = (AfChatroomDetail[]) result;
				if(afChatroomDetails != null && afChatroomDetails.length > 0){
					Handler handler = looperThread.looperHandler;
					if (null != handler) {
						handler.obtainMessage(LooperThread.UPDATE_ROOM_LIST, Arrays.asList(afChatroomDetails)).sendToTarget();
					}
					
				}
				
			}else if(flag == Consts.REQ_CHATROOM_ENTRY){
				final AfChatroomDetail afChatroomDetail = (AfChatroomDetail) user_data;
				AfChatroomEntryInfo afChatroomEntryInfo = (AfChatroomEntryInfo) result;
				mAfCorePalmchat.AfChatroomSetServerOpr(Consts.CHATROOM_OPR_TYPE_ADMIN, afChatroomEntryInfo.bm, null, null, null);
				
				new Thread() {
					public void run() {
						MessagesUtils.setChatroomIsEntry(afChatroomDetail.cid);
						mAfCorePalmchat.AfDbMsgSetStatusEx(AfMessageInfo.MESSAGE_TYPE_MASK_CHATROOM, afChatroomDetail.cid, AfMessageInfo.MESSAGE_UNREAD, AfMessageInfo.MESSAGE_READ);
						
					};
				}.start();
				toChattingRoomMessage(afChatroomEntryInfo);
			}
//			}
		}else{
			if (null != context) {
			  Consts.getInstance().showToast(context, code, flag,http_code);
			}
		}
		
	}
	
	 
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		looperThread.looper.quit();
		EventBus.getDefault().unregister(this);
	}

	/**
	 * 刷新聊天室列表事件 退出聊天室或收到@me会发此事件
	 * @param event
     */
	public void onEventMainThread(ChatRoomListEvent event){
		List<AfChatroomDetail> roomList = CacheManager.getInstance().getAfChatroomDetails();
		if(roomList != null) {
			Handler handler = looperThread.looperHandler;
			if (null != handler) {
				handler.obtainMessage(LooperThread.UPDATE_ROOM_LIST, roomList).sendToTarget();
			}
		}
	}
	/*@Override
	public void onRefreshChatroomList() {
		// TODO Auto-generated method stub

		List<AfChatroomDetail> roomList = CacheManager.getInstance().getAfChatroomDetails();
		if(roomList != null) {
			Handler handler = looperThread.looperHandler;
			if (null != handler) {
				handler.obtainMessage(LooperThread.UPDATE_ROOM_LIST, roomList).sendToTarget();
			}
		}
	
	}*/
	
	/*public void showProgressDialog(){
		if(CommonUtils.isMain(fragmentActivity)){
			dialog = new Dialog(fragmentActivity,R.style.Theme_LargeDialog);
			dialog.setOnKeyListener(fragmentActivity);
			dialog.setCanceledOnTouchOutside(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_loading);
			dialog.show();
		}
	}

	public void dismissProgressDialog() {
		if(CommonUtils.isMain(fragmentActivity)){
			if (null != dialog && dialog.isShowing()){
				try {
					dialog.cancel();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}*/
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_chatting_room);
		initViews();
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
