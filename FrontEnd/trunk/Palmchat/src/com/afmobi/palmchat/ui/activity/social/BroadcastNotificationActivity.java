package com.afmobi.palmchat.ui.activity.social;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.network.dataparse.CommonJson;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
//import com.afmobi.palmchat.util.image.ImageOnScrollListener;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.core.AfFriendInfo;
import com.core.AfPalmchat;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

/**
 * 通知界面
 * @author Transsion
 *
 */
public class BroadcastNotificationActivity extends BaseActivity implements OnClickListener, AfHttpResultListener {

	private static final String TAG = BroadcastNotificationActivity.class.getSimpleName();
	/**中间件接口*/
	private AfPalmchat mAfCorePalmchat;
	/**轮训线程*/
	private LooperThread mLooperThread;
	/**列表*/
	private ListView mListView;
	/**adapter*/
	private BrdNotificationfAdapter mAdapter;
	/**same broadcast mid have comment or like more than this size, aggregate to 1 item*/
	public static final int AGGREGATE_SIZE = 10;
	/**不全是未读消息的时候最多显示100个*/
	private final int LIMIT_MAX = 100;
	/**未读消息数*/
	private int mUnReadCount = 0;


	@Override
	public void findViews() {
		// TODO Auto-generated method stub

		setContentView(R.layout.activity_broadcast_notification);
		((TextView)findViewById(R.id.title_text)).setText(R.string.notifications);
		findViewById(R.id.back_button).setOnClickListener(this);
		
		mAfCorePalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
		mListView = (ListView) findViewById(R.id.listview);
//		mListView.setOnScrollListener(new ImageOnScrollListener(mListView ));
		
		mAdapter = new BrdNotificationfAdapter(this);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(position>=mAdapter.getCount()) {
					return;
				}
				mAdapter.updateItemView(position,mListView);
				BroadcastNotificationData item = mAdapter.getItem(position);
				
				AfChapterInfo afChapterInfo = new AfChapterInfo();
				afChapterInfo.mid = item.mid;
				
				Bundle bundle = new Bundle();
				bundle.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo);
				bundle.putInt(JsonConstant.KEY_BC_SKIP_TYPE, BroadcastDetailActivity.TYPE_BROADCASTLIST);
				Intent intent = new Intent(BroadcastNotificationActivity.this, BroadcastDetailActivity.class);
				intent.putExtras(bundle);
				BroadcastNotificationActivity.this.startActivity(intent);
			}
		}); 
		
		mLooperThread = new LooperThread();
		mLooperThread.setName(TAG);
		mLooperThread.start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				mAfCorePalmchat.AfDBBCNotifyUpdataAllStatus(CacheManager.getInstance().getMyProfile().afId, BroadcastNotificationData.STATUS_UNREAD, BroadcastNotificationData.STATUS_READ);
			}
		}).start();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	
	class LooperThread extends Thread {

		private static final int GET_BRD_NOTIFICATION_DATA = 1001;
		private static final int INIT_FINISH = 1002;
		/**after getBatchInfo, refresh list data*/
		private static final int REFRESH_NOTIFICATION_LIST = 1003;
		
		private static final int ON_BACK = 1004;

		private boolean isInit;

		Handler looperHandler;

		Looper looper;

		@Override
		public void run() {
			Looper.prepare();
			looper = Looper.myLooper();
			looperHandler = new Handler() {
				
				public void handleMessage(Message msg) {
					
					if (!isInit) {
						isInit = true;
					}
					
					switch (msg.what) {
						case GET_BRD_NOTIFICATION_DATA: {
							
							SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
							
							PalmchatLogUtils.println("ActivityBroadcastNotification LooperThread handleMessage REFRESH_LIST");
							
							ArrayList<BroadcastNotificationData> realDisplayList = new ArrayList<BroadcastNotificationData>();
							//获取未读相关 通知(点赞或者评论)
							ArrayList<BroadcastNotificationData> unReadDataList =  
									CommonJson.getBrdNotifyDataFromDb(-1,BroadcastNotificationData.STATUS_UNREAD);	
							//data[0] like, data[1] comment
							for (int i = 0; i < unReadDataList.size(); i++) {
								
								//check if local cache have this people profile, if not, add to getBatchInfo
								BroadcastNotificationData unReadData = unReadDataList.get(i);
								String time = (dateFormat.format(new Date(unReadData.ts*1000)));
								AfFriendInfo info = CacheManager.getInstance().searchAllFriendInfo(unReadData.afid);
								if (info != null) {
									unReadData.name = TextUtils.isEmpty(info.alias) ? info.name : info.alias;
									unReadData.head_img_path = info.head_img_path;
									unReadData.sex = info.sex;
								}
								realDisplayList.add(unReadData);
							}
							
							mUnReadCount = realDisplayList.size();
							//小于100怎用已读消息补
							if(mUnReadCount<LIMIT_MAX) {
								
								ArrayList<BroadcastNotificationData> readDataList =  
										CommonJson.getBrdNotifyDataFromDb(LIMIT_MAX-mUnReadCount, BroadcastNotificationData.STATUS_READ);
			
								for (int i = 0; i < readDataList.size(); i++) {
									BroadcastNotificationData readData = readDataList.get(i);
									//check if local cache have this people profile, if not, add to getBatchInfo
									AfFriendInfo info = CacheManager.getInstance().searchAllFriendInfo(readData.afid);
									String time = (dateFormat.format(new Date(readData.ts*1000)));
									Log.e("--wx2--", time);
									if (info != null) {
										readData.name = TextUtils.isEmpty(info.alias) ? info.name : info.alias;
										readData.head_img_path = info.head_img_path;
										readData.sex = info.sex;
									}
									realDisplayList.add(readData);
								}
							}
							
							DataParams param = new DataParams();
							param.realDisplayList = realDisplayList;						
							mHandler.obtainMessage(LooperThread.GET_BRD_NOTIFICATION_DATA, param).sendToTarget();
							break;
						}
								
					/*	case REFRESH_NOTIFICATION_LIST:
							
							AfFriendInfo infos[] = (AfFriendInfo[]) msg.obj;
							
							for (AfFriendInfo info : infos) {
								mAdapter.updateItem(info);
							}
							
							mHandler.sendEmptyMessage(REFRESH_NOTIFICATION_LIST);
							break;*/
	
						default:
							break;
					}	
				}
			};
			
			if (!isInit) {
				mHandler.sendEmptyMessage(INIT_FINISH);
			}
			
			Looper.loop();
			
		}
		}
	
	private class DataParams {
		public ArrayList<BroadcastNotificationData> realDisplayList;
	}
	
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LooperThread.INIT_FINISH:
				Handler handler = mLooperThread.looperHandler;
				if (null != handler) {
					handler.sendEmptyMessage(LooperThread.GET_BRD_NOTIFICATION_DATA);
				}
				break;
				
			case LooperThread.GET_BRD_NOTIFICATION_DATA:
				
				DataParams param = (DataParams) msg.obj;
				mAdapter.setAdapterList(param.realDisplayList,mUnReadCount);
				mAdapter.notifyDataSetChanged();
				break;
				
				
			case LooperThread.REFRESH_NOTIFICATION_LIST:
				
				mAdapter.notifyDataSetChanged();
				break;
				
				
			case LooperThread.ON_BACK:
				finish();
				break;

			default:
				break;
				
			}
			
		};
	};
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mLooperThread.looper.quit();
		super.onDestroy();
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		// TODO Auto-generated method stub
		
		PalmchatLogUtils.println("ActivityBroadcastNotification: flag = " + flag + " code="+ code + " result= "+ result);
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
				case Consts.REQ_GET_BATCH_INFO:
				
					if (result != null) {
					
						AfFriendInfo infos[] = (AfFriendInfo[])result;
					
						PalmchatLogUtils.println("ActivityBroadcastNotification REQ_GET_BATCH_INFO AfOnResult infos length " + infos.length);
					
						Handler handler = mLooperThread.looperHandler;
						if (null != handler) {
							handler.obtainMessage(LooperThread.REFRESH_NOTIFICATION_LIST, infos).sendToTarget();
						}
					
					} else {
						ToastManager.getInstance().show(this, R.string.network_unavailable);
					}
					
				break;
			}
			
		} else {

			switch (flag) {
				case Consts.REQ_GET_BATCH_INFO:
					break;
				}
			
			if (!isFinishing()) {
				Consts.getInstance().showToast(context, code, flag, http_code);
			}
		
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		//key back
		case R.id.back_button:
//			onBack();
			finish();
			break;
			
		}
	}
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBack();
		}
		return false;
	}*/

	/**
	 * 返回
	 */
/*	private void onBack() {
		new Thread() {
			public void run() {
				mAfCorePalmchat.AfDBBCNotifyUpdataAllStatus(CacheManager.getInstance().getMyProfile().afId, BroadcastNotificationData.STATUS_UNREAD, BroadcastNotificationData.STATUS_READ);
				mHandler.obtainMessage(LooperThread.ON_BACK).sendToTarget();
		}}.start();
	}*/

}
