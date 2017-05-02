package com.afmobi.palmchat.ui.activity.social;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.EventFollowNotice;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.ToastManager;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.core.AfResponseComm;
import com.core.Consts;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfPeoplesChaptersList;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import de.greenrobot.event.EventBus;
/**
 * Broadcast下的Hottoday页 
 */
public class HottodayFragment extends BaseFragment implements
OnClickListener, IXListViewListener, AfHttpResultListener ,AdapterBroadcastMessages.IFragmentBroadcastListener{

	 
//	public static final String TAG = FriendCircleFragment.class.getCanonicalName(); 
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");//用于显示刷新时间
 
 	public XListView mListView;   
	private AdapterBroadcastMessages adapter;  
	//public View  top_refresh;//显示刷新成功 显示update_successful 
	private LooperThread looperThread;
	public View chatting_options_layout,input_box_area;

	/**listview分割线长度*/
	private int mDivideHeight;
//	private ListViewAddOn listViewAddOn = new ListViewAddOn();
	public int softkeyboard_H = -1;
	/**空视图*/
	private View mV_EmptyView;
	AfChapterInfo mCurAfChapterInfo;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) { 
			case LooperThread.INSERT_SERVER_DATA_TO_DB:
				List<AfChapterInfo> list_aAfChapterInfos = (List<AfChapterInfo>) msg.obj; 
				int loadTypeWhenReq=msg.arg1;
				adapter.notifyDataSetChanged(list_aAfChapterInfos , loadTypeWhenReq==BroadcastFragment.LOADTYPE_WHEN_REQ_LOADMORE);
				if (loadTypeWhenReq==BroadcastFragment.LOADTYPE_WHEN_REQ_NEW){//!isLoadMore) {
					onStop();
				}
				settingStop();
				break;  
			}
		}
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
	} 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		looperThread = new LooperThread();
//		looperThread.getName(TAG);
		looperThread.start();

		findViews();
		init();
		return mMainView;
			
	}
	
	@Override
	public void onResume() {
		super.onResume();  
//		MobclickAgent.onPageStart(TAG);
	}

	   
	
	@Override
	public void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd(TAG);
	}
	
	 int _start_index;
	 int _end_index; 
	public void findViews() { 
		setContentView(R.layout.fragment_hot_today);

		View headerView  = View.inflate(context, R.layout.hot_today_nodata_layout, null);
		mListView = (XListView) findViewById(R.id.hottoadlistview);
		mDivideHeight = mListView.getDividerHeight();
		mListView.setVisibility(View.VISIBLE);
		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.addHeaderView(headerView);
		mListView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					break;
				default:
					break;
				}
				return false;
			}
		});
		mV_EmptyView = headerView.findViewById(R.id.broadcast_hottoday_no_data);
		mV_EmptyView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}
	public void setListScrolltoTop(){
		if(mListView!=null&&mListView.getCount()>0){
			mListView.setSelection(0);
		}
	}
	int clike_count = 0;
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser); 
		if (mFragmentVisible&& (adapter  ==null||adapter .isEmpty())) {
			showRefresh();
		}
	};
	 
	public void init() {
		if(mAfCorePalmchat==null){
			mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		}
//		mAfCorePalmchat.AfDBBrinfoGetReceiveLatestMsgid();
		CacheManager.getInstance().getMyProfile(); 
	/*	TextView mTitleTxt = (TextView) findViewById(R.id.title_text);
		mTitleTxt.setText(R.string.broadcast_messages);  */
		//top_refresh = findViewById(R.id.top_refresh); 
		new ReadyConfigXML(); 
		mListView.setOnScrollListener(new OnScrollListener() { 
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						// TODO Auto-generated method stub
						if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
//							listViewAddOn.setSlipState(false);
							adapter.pageImgLoad(_start_index, _end_index); 
						}else { 
//							listViewAddOn.setSlipState(true);
						}
					} 
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
						// TODO Auto-generated method stub
						//设置当前屏幕显示的起始index和结束index
					    _start_index = firstVisibleItem;
					    _end_index = firstVisibleItem + visibleItemCount - 2;
					    if (_end_index >= totalItemCount) {
					     _end_index = totalItemCount - 1;
					    }
					}
		});
		adapter = new AdapterBroadcastMessages(getActivity(), new ArrayList<AfResponseComm.AfChapterInfo>(), Consts.AFMOBI_BRMSG_INPUT, this,  BroadcastDetailActivity.FROM_HOTTODAY);
		mListView.setAdapter(adapter);  
		EventBus.getDefault().register(this);
		showRefresh();
	}
	 
	
	/**
	 * 
	 * @param broadcastMessageList
	 * @param dataType
	 * @param loadTypeWhenReq  请求的时候是否是loadmore
	 */
	private void setAdapter(final List<AfChapterInfo> broadcastMessageList, byte dataType ,int loadTypeWhenReq) {
    	switch (dataType) {
		case Consts.AFMOBI_BRMSG_INPUT: 
				if (null == adapter  ) { 
						adapter = new AdapterBroadcastMessages(getActivity(), broadcastMessageList, Consts.AFMOBI_BRMSG_INPUT, this,  BroadcastDetailActivity.FROM_HOTTODAY);
						mListView.setAdapter(adapter);
					hideBlankView();
				} else {
					final int size = broadcastMessageList.size();
					if (size > 0) {
						showRefreshSuccess();
						hideBlankView();
						showRefreshSuccess();
						Handler handler = looperThread.looperHandler;
						if (null != handler) {
							Message msgMessage = new Message();
							msgMessage.obj = broadcastMessageList; 
							msgMessage.arg1=loadTypeWhenReq ;
						   msgMessage.what = LooperThread.INSERT_SERVER_DATA_TO_DB;
							 
							handler.sendMessage(msgMessage);
						}
					}else {
						if (isAdded() ) {
							ToastManager.getInstance().show(getActivity(),mFragmentVisible, R.string.no_data);
							mListView.setPullLoadEnable(false);
						}
					}
				}  
			break; 
		default:
			break;
		}
	}

	private int pageid = 0;
	private int START_INDEX = 0 ;
	private boolean isLoadMore;
	private static final int LIMIT = 10; 
	
	private void loadData() {
		//mListView.setHeaderDividersEnabled(false);
		isLoadMore = false; 
			START_INDEX = 0; 
		pageid = (int) System.currentTimeMillis() + new Random(10000).nextInt();
		loadDataFromServer(pageid); 
			if (adapter.getCount() < 0) {
				hideBlankView();
			} 
	}
	  
	private void loadDataFromServer(int pageId) {  
		int loadType=isLoadMore?BroadcastFragment.LOADTYPE_WHEN_REQ_LOADMORE:BroadcastFragment.LOADTYPE_WHEN_REQ_NEW;
		//这里之所以要用loadType 而不用isLoadMore 是因为isLoadMore是在上拉的时候就会触发，这样就会有如果先刷新 再更多  刷新返回时 isLoadMore这个状态是错的，导致会有重复广播 
		mAfCorePalmchat.AfHttpBcgetChaptersLikeStar(Consts.REQ_BCGET_LIKE_STAR_BY_DAY, pageId, START_INDEX  * LIMIT, LIMIT, 
				loadType, this);
		 
	}

	private void hideBlankView(){
		if (mV_EmptyView != null) {
			mV_EmptyView.setVisibility(View.GONE);
			mListView.setDividerHeight(mDivideHeight);
			//mListView.setHeaderDividersEnabled(true);
			mListView.setPullLoadEnable(true);
		}
	}

	private void showBlankView(){
		if (mV_EmptyView != null) {
			mV_EmptyView.setVisibility(View.VISIBLE);
			mListView.setDividerHeight(0);
			mListView.setHeaderDividersEnabled(false);
			mListView.setPullLoadEnable(false);
		}
	}
	/**
	 * 发评论
	 * @param text
	 */
	public void send_comment(final String text){
		close_inputbox(true);
		mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					if (adapter != null ) {
					 
						String msg = text;
						if (!TextUtils.isEmpty(msg)) {
							String hint_text = "";
							String to_afid = adapter.getCommentModel().getTo_afid();
							if (!hint_text.equals(getString(R.string.hint_commet))&& !TextUtils.isEmpty(to_afid)) {
								msg = hint_text + msg;
							}
							adapter.getCommentModel().setMsg(msg);
							adapter.sendComment();
						}else {
							ToastManager.getInstance().show(getActivity(), R.string.hint_commet);
						}
					}
				}
			}, 100);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent data) { 
			switch (resultCode) { 
			case Constants.COMMENT_FLAG:
				if (data != null) {
					String msg = data.getExtras().getString(JsonConstant.KEY_COMMENT_COUNTENT);
					String isSend = data.getExtras().getString("isSend");
					if(isSend.equals("1")){
						send_comment(msg);
					}
				}
				break;
			 
		}
	   super.onActivityResult(requestCode, resultCode, data);
	}
	class LooperThread extends Thread { 
		
		private static final int INSERT_SERVER_DATA_TO_DB = 7004; 
		
		Handler looperHandler;

		Looper looper;
		
		private boolean isInit;

		@Override 
		public void run() {
			Looper.prepare();
			looper = Looper.myLooper();

			looperHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					
					if (!isInit) {
						isInit = true;
					}
					
					
					switch (msg.what) { 
				 	 case INSERT_SERVER_DATA_TO_DB:
						List<AfChapterInfo> tempAfChapterInfos = new ArrayList<AfChapterInfo>();
						ArrayList<AfChapterInfo> broadcastMessageList = (ArrayList<AfChapterInfo>) msg.obj;
						int loadTypeWhenReq=msg.arg1;
						int size =  broadcastMessageList.size();
						for (int i = size  - 1; i >= 0 ; i--) {
								AfChapterInfo afChapterInfo = broadcastMessageList.get(i);
								String mid = afChapterInfo.mid; 
								afChapterInfo.isLike = mAfCorePalmchat.AfBcLikeFlagCheck(mid); // check like
								tempAfChapterInfos.add(0, afChapterInfo); 
						} 
						 
						Message msg1 = new Message();
						msg1.obj = tempAfChapterInfos;
						msg1.arg1=loadTypeWhenReq ;
						msg1.what = INSERT_SERVER_DATA_TO_DB;
						mHandler.sendMessage(msg1); 
						break;  
				 
					}
				}
			};

			Looper.loop();
		}
	}
	
	
 
 
	  
	@Override
	public void onClick(View v) {
		switch (v.getId()) {   
		case R.id.send_bc_failed_notification:
			CommonUtils.to(getActivity(), BroadcastRetryPageActivity.class); 
			break;
		case R.id.chatting_operate_one:
			close_inputbox(true);

			break;
		}

	}

	@Override
	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) { 
			case Consts.REQ_BCGET_LIKE_STAR_BY_DAY: 
				settingStop();
				if (null != result) {
					int loadTypeWhenReq=BroadcastFragment.LOADTYPE_WHEN_REQ_NEW;
					if(user_data!=null){
						loadTypeWhenReq=(Integer)user_data;
					}
					AfResponseComm afResponseComm = (AfResponseComm) result;
					AfPeoplesChaptersList afPeoplesChaptersList= (AfPeoplesChaptersList) afResponseComm.obj;
					if (afPeoplesChaptersList != null) {
						ArrayList<AfChapterInfo> list_AfChapterInfo = afPeoplesChaptersList.list_chapters;
						if (list_AfChapterInfo.size() > 0) {
							setAdapter(list_AfChapterInfo, Consts.AFMOBI_BRMSG_INPUT,loadTypeWhenReq );
						}else {
							ToastManager.getInstance().show(getActivity(),mFragmentVisible, R.string.no_data);
							mListView.setPullLoadEnable(false);
							if (adapter .getCount() < 1) {
								showBlankView();
							}
						}
					}else {
						if (isAdded()  ) {
							if (adapter .getCount() < 1) {
								showBlankView();
							}
						}
					}
				} else {
					if (isAdded()  ) {
						if (adapter .getCount() < 1) {
							showBlankView();
						}
					}
				}  
				break;   
			}
		} else {
			settingStop();
			if (code == Consts.REQ_CODE_104) { 
				adapter.clear(); 
				showRefresh();
			}else {
				if (flag == Consts.REQ_BCGET_LIKE_STAR_BY_DAY
						) {
					if (START_INDEX > 0) {
						START_INDEX--;
					}
				}
				showBlankView();
				Consts.getInstance().showToast(context, code, flag, http_code);
			}
		}
	}


	/**
	 * 刷新成功的提示
	 */
	public void showRefreshSuccess(){
		if (!isLoadMore) {
			if(((HotTodayActivity)getActivity()) != null) {
				((HotTodayActivity) getActivity()).showRefreshSuccess();
			}
		}
	}

	private Toast toast;
	public void showToast(int resId) {
		if (null != toast) {
			toast.setText(resId);
			toast.show();
		}
	} 
	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy(); 
		if(null != looperThread &&  null != looperThread.looper){
			looperThread.looper.quit();
		}
		EventBus.getDefault().unregister(this);
	}

	  
	@Override
	public void onRefresh(View view) {
		if (NetworkUtils.isNetworkAvailable(context)) {
			onPause();
			if (!isRefreshing_mListview) {
				isLoadMore = false;
				isRefreshing_mListview = true; 
				START_INDEX=0;  
				loadData();
				if (mV_EmptyView != null) {
					mV_EmptyView.setVisibility(View.GONE);
					mListView.setPullLoadEnable(true);
				}
				mListView.setDividerHeight(0);
				two_minutes_Cancel_Refresh_Animation();
			}
		}else {
			ToastManager.getInstance().show(context,mFragmentVisible, context.getString(R.string.network_unavailable));
			stopRefresh();
		}
		
	}
	@Override
	public void onLoadMore(View view) {
		if (NetworkUtils.isNetworkAvailable(context)) {
			if (!isLoadingMore_mListview) {
				isLoadMore = true;
				isLoadingMore_mListview = true; 
				START_INDEX++; 
				
//				if (isSelectDB_Data  ) { 
//					loadData();
//				}else {
					loadDataFromServer(pageid);
//				}
			}
		}else {
			ToastManager.getInstance().show(context,mFragmentVisible, context.getString(R.string.network_unavailable));
			stopLoadMore();
		}
	}
	
	public void showRefresh(){
		if (NetworkUtils.isNetworkAvailable(context)) {
			if (getActivity() != null) {
				int px = AppUtils.dpToPx(getActivity(), 60);
				mListView.performRefresh(px);
				isLoadMore = false;
				isRefreshing_mListview = true; 
				START_INDEX=0;
				hideBlankView();
			}
		}else {
			ToastManager.getInstance().show(context,mFragmentVisible, context.getString(R.string.network_unavailable));
			stopRefresh();
		}
	}

	/**
	 * 处理广播更新事件
	 * @param afChapterInfo
	 */
	public void onEventMainThread(AfChapterInfo afChapterInfo) {
		 if(Constants.UPDATE_DELECT_BROADCAST==afChapterInfo.eventBus_action){
			adapter.notifyDataSetChanged_removeBymid(afChapterInfo.mid);
			CacheManager.getInstance().remove_BC_RecordSendSuccessDataBy_mid(afChapterInfo.mid);
		}else if (Constants.UPDATE_LIKE==afChapterInfo.eventBus_action) {
			adapter.notifyDataSetChanged_updateLikeBymid(afChapterInfo);
		}
	}

	/**
	 * 在profile界面follow或者followed的时候刷新界面
	 * @param eventFollowNotice
	 */
	public void onEventMainThread(EventFollowNotice eventFollowNotice){
		adapter.notifyDataSetChanged();
	}

	private boolean isRefreshing_mListview = false;
	private boolean isLoadingMore_mListview = false;
 
	
	 private void stopRefresh() {	
	    	// TODO Auto-generated method stub 
				isRefreshing_mListview = false;
				mListView.stopRefresh(true);
				mListView.setRefreshTime(mDateFormat.format(new Date(System.currentTimeMillis())));
		 
	 }
	 
	 public void settingStop(){
		if (isLoadingMore_mListview) {
			stopLoadMore();
		}	
		if (isRefreshing_mListview) {
			stopRefresh();
		}
	 }
	 
	 private void stopLoadMore(){ 
			 isLoadingMore_mListview = false;
			 mListView.stopLoadMore();
		 
	 }
	 
	 public void close_inputbox(final boolean emotion_isClose){
		 if (isAdded()) {
			 mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					input_box_area.setVisibility(View.GONE); 
				}
			}, 200);
		}
	 }

		public void two_minutes_Cancel_Refresh_Animation(){
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (isRefreshing_mListview) {
						settingStop();
						ToastManager.getInstance().show(context,mFragmentVisible, R.string.network_unavailable);
					}
					 
					
				}
			}, Constants.TWO_MINUTER);
		}

		@Override
		public void interface_open_inputbox() {
			// TODO Auto-generated method stub
		}



		@Override
		public void interface_dismissProgressDialog() {
			// TODO Auto-generated method stub
			dismissProgressDialog();
		} 
		@Override
		public boolean interface_isAdded() {
			// TODO Auto-generated method stub
			return isAdded();
		}  
		@Override
		public  Activity interface_getActivity() {
			// TODO Auto-generated method stub
			return getActivity();
		} 
		@Override
		public void interface_showProgressDialog() {
			// TODO Auto-generated method stub
			showProgressDialog();
		} 
		@Override
		public XListView interface_getmListView() {
			// TODO Auto-generated method stub
			  	return mListView; 
		}
}