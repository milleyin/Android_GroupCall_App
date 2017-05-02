
package com.afmobi.palmchat.ui.activity.store.fragment;

import java.util.ArrayList;

import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.eventbusmodel.EmoticonDownloadEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.store.StoreFaceDetailActivity;
import com.afmobi.palmchat.ui.activity.store.StoreMyPurchaseActivity;
import com.afmobi.palmchat.ui.activity.store.adapter.StoreListAdapter;
import com.afmobi.palmchat.ui.customview.AdvBannerView;
import com.afmobi.palmchat.ui.customview.AdvBannerView.OnJumpEventListener;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.RefreshableView;
import com.afmobi.palmchat.ui.customview.RefreshableView.RefreshListener;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
//import com.afmobi.palmchat.util.image.ImageOnScrollListener;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobigroup.gphone.R;
import com.core.AfStoreProdList;
import com.core.AfStoreProdList.AfBannerInfo;
import com.core.AfStoreProdList.AfProdProfile;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * 表情商店Fragment
 * @author starw
 *
 */
@SuppressLint("HandlerLeak")
public class StoreFragment extends BaseFragment implements
			AfHttpResultListener, RefreshListener, OnItemClickListener, OnJumpEventListener {
	
	/**该类的tag*/
	private static final String TAG = StoreFragment.class.getSimpleName();
	/**页面刷新线程*/
	private LooperThread mLooperThread;
	/**用于记录页面开始高度*/
	private int mNearbyStarPagerH;
	/**下拉刷新效果显示视图*/
	private RefreshableView mRefrshView;
	/**表情列表*/
	private ListView mEmotionListView;
	/**banner*/
	private AdvBannerView mAdvBannerView;
	/**表情列表适配器*/
	private StoreListAdapter mStoreEmotionListAdapter;
	/**Banner数据*/
	private ArrayList<AfBannerInfo> mStoreGridData = new ArrayList<AfBannerInfo>();
	
	/**fragment的空参数构造是必须写的，否则会在一些情况下挂掉的*/
	public StoreFragment() {
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//EventBus注册
		EventBus.getDefault().register(this);
		mLooperThread = new LooperThread();
		mLooperThread.setName(TAG);
		mLooperThread.start();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.activity_store);
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.STO_ME);
		initViews();
		return mMainView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		 
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {	
			//返回按钮
			case R.id.back_button:{
				do_BackButton();
			}
			break;
			
			//title right btn 我的购买
			case R.id.op2: {
				startActivity(new Intent(context, StoreMyPurchaseActivity.class));
			}
			break;
			
			case R.id.op1: {
				do_op1();
			} 
			break;
			default:
				break;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();		
		Handler handler = mLooperThread.looperHandler;
		if (handler != null) {
			AfStoreProdList data1 = CacheManager.getInstance().getmStoreEmojiProdList();
			if (data1 != null) {
				handler.obtainMessage(LooperThread.REFRESH_LIST, StoreListAdapter.SORT_EMOJI, 0, data1).sendToTarget();
			}
		}
		
	}
	
	@Override
	public void onPause() {
		super.onPause();		
	} 
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if(null!=mLooperThread) {
			mLooperThread.looper.quit();
			mLooperThread.looperHandler.removeCallbacksAndMessages(this);
			mLooperThread=null;
		}
		 
		if(null!=mStoreGridData) {
			mStoreGridData.clear();
		}
		if(null!=mStoreEmotionListAdapter) {
			mStoreEmotionListAdapter.clear();
		}
		if(null!=mAdvBannerView) {
			mAdvBannerView.clean();
		}
		//EventBus反注册
		EventBus.getDefault().unregister(this);
	}
	
	/**
	 * 初始化视图
	 */
	private void initViews() {
		
		((TextView) findViewById(R.id.title_text)).setText(R.string.sticker_store);
		findViewById(R.id.back_button).setOnClickListener(this);
		
		View view1 = findViewById(R.id.op1);
		view1.setOnClickListener(this);
		view1.setVisibility(View.GONE);
		
		View view = findViewById(R.id.op2);
		view.setOnClickListener(this);
		view.setVisibility(View.VISIBLE);
		view.setBackgroundResource(R.drawable.store_right_btn);
				
		mNearbyStarPagerH = AppUtils.dpToPx(getActivity(), 32) + AppUtils.getWidth(getActivity()) / 3;
		
		initListViews();
		getCacheData();
		refreshEmojiList();
	}
	
	/**
	 *读取缓存数据
	 */
	private void getCacheData() {
		AfStoreProdList emotionData = CacheManager.getInstance().getmStoreEmojiProdList();
		
		if ( null != emotionData) {
			
			mStoreEmotionListAdapter.setAddr(emotionData.url);
	
			updateBannerData(emotionData.banners,emotionData.url);
			
			mStoreEmotionListAdapter.addAll(emotionData.page_info.prof_list); 
			mStoreEmotionListAdapter.notifyDataSetChanged();
		} 		
	}
	
	/**
	 * 初始化列表数据
	 */
	private void initListViews() {
		
		mRefrshView = (RefreshableView) findViewById(R.id.refresh_list1);
		mRefrshView.setRefreshListener(this);
		
		mEmotionListView = (ListView) findViewById(R.id.listview_store1);
		mEmotionListView.setOnItemClickListener(this);
		
		//banner区域
		final View headerView = View.inflate(context, R.layout.storegridpagerheader, null);
		
		mAdvBannerView = (AdvBannerView)headerView.findViewById(R.id.bannnerview);
		LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, (CommonUtils.getRealtimeWindowWidth(getActivity())*5)/12);
		mAdvBannerView.setLayoutParams(layoutParam);
		mAdvBannerView.setOnJumpEventListener(this);
		mEmotionListView.addHeaderView(headerView);
		mStoreEmotionListAdapter = new StoreListAdapter(context, StoreListAdapter.SORT_EMOJI, mEmotionListView);
//		mEmotionListView.setOnScrollListener(new ImageOnScrollListener(mEmotionListView ));
		mEmotionListView.setAdapter(mStoreEmotionListAdapter);
	}
	
	/**
	 * 更新banner列表数据
	 * @param banners banner的数据
	 */
	private void updateBannerData(ArrayList<AfBannerInfo> banners,String url) {
		ArrayList<String> imageUrls = new ArrayList<String>();
		for(AfBannerInfo afBannerInfo:banners) {
			imageUrls.add(new StringBuffer(url).append(afBannerInfo.image_path).toString());
		}
		
		mAdvBannerView.updateBannerData(imageUrls, true);
		if(null!=mStoreGridData ) {
			mStoreGridData.clear();
			mStoreGridData.addAll(banners); 
		}
	}
	
	/**
	 * 处理返回键
	 * 
	 */
	private void do_BackButton( ) { 
		CacheManager.getInstance().setStoreBack(true);
		getActivity().finish();
		MyActivityManager.getScreenManager().popFragment(this);
		 
	}
	
	/**
	 * 处理op1事件 进入palmplay或者去下载palmplay
	 */
    private void do_op1( ) {
		PackageManager pm =  context.getPackageManager() ;
		Intent intent = pm .getLaunchIntentForPackage(IntentConstant.HAZYMARKET);
		
		if (intent != null){
			startActivity(intent); 
	    }else{
	    	Uri uri = Uri.parse(IntentConstant.PALMPLAYURI);//id为包名 
            Intent it = new Intent(Intent.ACTION_VIEW, uri); 
            startActivity(it); 
		}
    }
    
	/**
	 * 刷新表情列表
	 */
	private void refreshEmojiList(){
		mRefrshView.showRefresh();
		mAfCorePalmchat.AfHttpStoreGetProdList(0, 1000, Consts.PROD_TYPE_EXPRESS_WALLPAPER, 
				Consts.PROD_CATEGORY_EXPRESS, Consts.PROD_CATEGORY_EXPRESS, this);
	}
	
	/**
	 * 显示表情视图
	 */
	private void showEmojiView() {
		mRefrshView.setVisibility(View.VISIBLE);
	}

	/**
	 * EventBus回调 表情下载进度更新
	 * @param event
	 */
	public void onEventMainThread(EmoticonDownloadEvent event) {

		int sortType = event.getSort();
			
		String itemId = event.getItem_id();
		int progress = event.getProgress();
	
		boolean isDownloaded = event.isDownloaded();
		
		PalmchatLogUtils.println("--kkk : store onreceive itemId=" + itemId + " progress=" + progress + "  isDownloaded=" + isDownloaded);
		
		if (isDownloaded) {
			if (sortType == StoreListAdapter.SORT_EMOJI) {
				if(CacheManager.getInstance().getItemid_update().containsKey(itemId)){
					CacheManager.getInstance().getItemid_update().remove(itemId);
				}
				mStoreEmotionListAdapter.updateProgress(itemId, 100);
				mStoreEmotionListAdapter.updateDownloadState(itemId, true);
				mStoreEmotionListAdapter.notifyDataSetChanged();
			}
			
		} else {
			if (sortType == StoreListAdapter.SORT_EMOJI) {
				
				//download failed
				if (progress == 0) {
					mStoreEmotionListAdapter.updateProgress(itemId, 0);
					mStoreEmotionListAdapter.updateDownloadState(itemId, false);								
					mStoreEmotionListAdapter.notifyDataSetChanged();
				} else {								
					mStoreEmotionListAdapter.updateProgress(itemId, progress);
				}						
			} 
		}		
	
	}
	
	/**刷新*/
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			if (msg.what == LooperThread.REFRESH_LIST) {
				AfStoreProdList data = (AfStoreProdList)msg.obj;
				if (msg.arg1 == StoreListAdapter.SORT_EMOJI) {
					mStoreEmotionListAdapter.addAll(data.page_info.prof_list); 
					mStoreEmotionListAdapter.notifyDataSetChanged();
					CacheManager.getInstance().setmStoreEmojiProdList(data);
				}
			}
		};
	};
	
	/**
	 * 刷新线程
	 *
	 */
	class LooperThread extends Thread {
		/**刷新列表*/
		private static final int REFRESH_LIST = 30001;
		/**更新进度*/
		private static final int UPDATE_LIST_PROGRESS = 30002;
		/**Handler*/
		Handler looperHandler;
		/**Looper*/
		Looper looper;

		@Override
		public void run() {
			Looper.prepare();
			looper = Looper.myLooper();

			looperHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					
					if (msg.what == REFRESH_LIST) {
						
						AfStoreProdList data = (AfStoreProdList)msg.obj;
						int size = data.page_info.prof_list.size();
						for (int i = 0; i < size; i++) {

							AfProdProfile afProd = data.page_info.prof_list.get(i);
							if (CommonUtils.checkStoreFace(afProd.item_id)) {
								afProd.isDownloaded = true;
							} else {
								afProd.isDownloaded = false;
							}
							
							if (CacheManager.getInstance().getStoreDownloadingMap().containsKey(afProd.item_id)) {
								afProd.progress = CacheManager.getInstance().getStoreDownloadingMap().get(afProd.item_id);
							} else {
								afProd.progress = 0;
							}
						}
						
						if (msg.arg1 == StoreListAdapter.SORT_EMOJI) {
							mHandler.obtainMessage(LooperThread.REFRESH_LIST, StoreListAdapter.SORT_EMOJI, 0, data).sendToTarget();		
						} 				
					} else if (msg.what == UPDATE_LIST_PROGRESS) {
						//update download progress
						AfStoreProdList data = (AfStoreProdList)msg.obj;
						int size = data.page_info.prof_list.size();
						for (int i = 0; i < size; i++) {
							AfProdProfile afProd = data.page_info.prof_list.get(i);
							if (CacheManager.getInstance().getStoreDownloadingMap().containsKey(afProd.item_id)) {
								afProd.progress = CacheManager.getInstance().getStoreDownloadingMap().get(afProd.item_id);
							}
						} 

						if (msg.arg1 == StoreListAdapter.SORT_EMOJI) {
							mHandler.obtainMessage(LooperThread.REFRESH_LIST, StoreListAdapter.SORT_EMOJI, 0, data).sendToTarget();		
						} 
					}
				}				
			};
			Looper.loop();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int realPos = --position;
		
		switch (parent.getId()) {		
			//emojji
			case R.id.listview_store1: {
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.EM_DETAIL);
				mStoreEmotionListAdapter.getItem(realPos);
				Bundle bundle = new Bundle();
				bundle.putString(IntentConstant.ITEM_ID, mStoreEmotionListAdapter.getItem(position).item_id);
				bundle.putString(IntentConstant.SMALL_ICON, mStoreEmotionListAdapter.getItem(position).small_icon);
				bundle.putString(IntentConstant.NAME, mStoreEmotionListAdapter.getItem(position).name);
				bundle.putInt(IntentConstant.AFCOIN, mStoreEmotionListAdapter.getItem(position).afcoin);
				Intent intent = new Intent(context, StoreFaceDetailActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			break;
			//gift
			case R.id.listview_store2:{
			}
			break;
			default:
				break;
		}
	}

	@Override
	public void onRefresh(RefreshableView view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
			//emojji
			case R.id.refresh_list1:{
				refreshEmojiList();
			}
			break;	
			default:
				break;
		}
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code,
			Object result, Object user_data) {
		PalmchatLogUtils.println("StoreFragment: flag = " + flag + " code="+ code + " result= "+ result);
		if (code == Consts.REQ_CODE_SUCCESS) {	
			switch (flag) {
				case Consts.REQ_STORE_PROD_LIST_GET: {
					byte category = (Byte)user_data;
					//request emoji 
					if (category == Consts.PROD_CATEGORY_EXPRESS) {
						mRefrshView.finishRefresh();
						AfStoreProdList data = (AfStoreProdList)result;
						if (data != null) {
							int topStarSize = data.banners.size();
							int prodSize = data.page_info.prof_list.size();
							PalmchatLogUtils.println("--storeInfo emoji banner size:" + topStarSize+ " Prod size:"+ prodSize);
							mStoreEmotionListAdapter.setAddr(data.url);
							updateBannerData(data.banners,data.url);

							if((null!=mLooperThread)&&(null!=mLooperThread.looperHandler)){
								mLooperThread.looperHandler.obtainMessage(LooperThread.REFRESH_LIST, StoreListAdapter.SORT_EMOJI, 0, data).sendToTarget();
							}
						}		
					}
				}
				break;
			default:
				break;
			}
		} else {	
			switch (flag) {
				case Consts.REQ_STORE_PROD_LIST_GET:{
					byte category = (Byte)user_data; 
					//request emoji
					if (category == Consts.PROD_CATEGORY_EXPRESS) {
						mRefrshView.finishRefresh();						
					} 
					
					if(code == Consts.REQ_CODE_UNNETWORK){						
						if(mStoreEmotionListAdapter.isEmpty()){
							showFailedDialog(getActivity());
						}		
					}
				}
				break;

			default:
				break;
			}
			
			if (isAdded()) {
				if (null != context && this.isVisible()) {
					Consts.getInstance().showToast(context, code, flag, http_code);
				}
			}
		}
	}
	
	/**
	 * 显示失败对话框
	 * @param context 上下文
	 */
	private void showFailedDialog(final Context context ) {
		if(context != null ) {
			AppDialog appDialog = new AppDialog(context);
			String msg = context.getResources().getString(R.string.updata_failed_try_again);
			appDialog.createConfirmDialog(context, msg, new OnConfirmButtonDialogListener() {
				
				@Override
				public void onRightButtonClick() {
					refreshEmojiList();
				}
				
				@Override
				public void onLeftButtonClick() {

				}
			});
			appDialog.show();
		}	
	}

	@Override
	public void goToActivity(int index) {
		// TODO Auto-generated method stub
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.EM_DETAIL);
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstant.ITEM_ID, mStoreGridData.get(index).image_id);
		Intent intent = new Intent(getActivity(), StoreFaceDetailActivity.class);
		intent.putExtras(bundle);
		getActivity().startActivity(intent);
	}
}
