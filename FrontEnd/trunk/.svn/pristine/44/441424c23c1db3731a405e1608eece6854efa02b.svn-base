package com.afmobi.palmchat.ui.activity.social;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.HidingScrollListener;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.publicaccounts.InnerNoAbBrowserActivity;
import com.afmobi.palmchat.ui.activity.tagpage.TagPageActivity;
import com.afmobi.palmchat.ui.activity.tagpage.TagsActivity;
import com.afmobi.palmchat.ui.activity.tagpage.adapter.HotTagsAdapter;
import com.afmobi.palmchat.ui.customview.AdvBannerView;
import com.afmobi.palmchat.ui.customview.AdvBannerView.OnJumpEventListener;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfPalmchat;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfBCPriefInfo;
import com.core.AfResponseComm.AfTagGetDefaultTrend;
import com.core.AfResponseComm.AfTagGetTagsResp;
import com.core.AfResponseComm.AfTagGetTrendsMoreResp;
import com.core.AfResponseComm.AfTagGetTrendsResp;
import com.core.AfResponseComm.AfTagInfo;
import com.core.Consts;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * tags fragment 页
 * @author sw
 *
 */
public class TrendingFragment extends BaseFragment implements OnClickListener,IXListViewListener,
							AfHttpResultListener,OnJumpEventListener{
	private final String TAG = TrendingFragment.class.getSimpleName();
	/**banner*/
	private AdvBannerView mAdvBannerView;
	/**tag名字*/
	private TextView mTV_TagName;
	/**HotTags gridView*/
	private GridView mGV_HotTags;
	/**HotTags adapter**/
	private HotTagsAdapter mHotTagsAdapter;	
	/**tags列表*/
	private XListView mXListView;
	/**适配器*/
	private TrendingAdapter mTrendingAdapter;
	/**热门标签*/
	private ArrayList<AfTagInfo> mHotTags;	
	/**中间件接口*/
	private AfPalmchat mAfCorePalmchat;
	/**时间格式*/
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
	/**当前页*/
	private int mCurPage = 0 ;
	/**banner 数据*/
	private ArrayList<AfTagInfo> mBanner_list;
	/**空视图*/
	private View mV_EmptyView;
	/**headview有内容的的视图*/
	private View mV_DataView;
	/**pageid*/
	private int mPageId;
	/**当前操作状态,默认是刷新  true为loadMore;false为reFresh*/
	private boolean mBln_OperationTask = false;	
	/**轮询线程*/
	private LooperThread mLooperThread;
	/**初始化刷新*/
	private boolean mIsInitRefresh;
	/**上次刷新时间*/
	private long mTimeLastRefresh;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what){
				case DefaultValueConstant.RESETTILEPOSITION:{
					Activity activity = getActivity();
					if(activity!=null&&(activity instanceof  MainTab)){
						((MainTab) activity).resetTitlePosition();
					}
					break;
				}
				case DefaultValueConstant.SHOWFRESHSUCCESS:{
					Fragment fragment = getParentFragment();
					if((fragment!=null)&&(fragment instanceof  BroadcastTab)){
						((BroadcastTab)fragment).showRefreshSuccess(1);
					}
					break;
				}
				default:
					break;
			}
		}

	};


	/**
	 * 无参数的构造方法必须复写
	 */
	public TrendingFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		mLooperThread = new LooperThread();
		mLooperThread.setName(TAG);
		mLooperThread.start();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.fragment_trending);
		findViews();
		initData();
		return mMainView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	/**
	 *View初始化
	 */
	private void findViews() {
		View headerView  = View.inflate(context, R.layout.fragment_headview_tags, null);
		//banner
		mAdvBannerView = (AdvBannerView)headerView.findViewById(R.id.broadcast_trending_bannnerview);
		mAdvBannerView.setDefaultDrawable(R.color.base_back);
		mAdvBannerView.setOnJumpEventListener(this);
		RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams( LayoutParams.MATCH_PARENT, (CommonUtils.getRealtimeWindowWidth(getActivity())*7)/15); 		
		mAdvBannerView.setLayoutParams(layoutParam);
		
		mTV_TagName = (TextView)headerView.findViewById(R.id.broadcast_trending_photoname_id);
		mTV_TagName.setOnClickListener(this);
		
		mGV_HotTags = (GridView)headerView.findViewById(R.id.broadcast_trending_tags_gridview_id);
		mGV_HotTags.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mGV_HotTags.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(arg2<mHotTags.size()){
					String tagName = mHotTags.get(arg2).tag;
					doJumpTagsPageAct(tagName);
				}
			}
		});
		mHotTagsAdapter = new HotTagsAdapter(getActivity());
		mGV_HotTags.setAdapter(mHotTagsAdapter);
				
		headerView.findViewById(R.id.broadcast_tags_seemore_id).setOnClickListener(this);
		mXListView = (XListView)findViewById(R.id.broadcast_trending_listview_id);
		mXListView.setXListViewListener(this);
		mXListView.setPullLoadEnable(true);
		mXListView.setPullRefreshEnable(true);
		mXListView.addHeaderView(headerView);
		mXListView.setHidingScrollListener(new HidingScrollListener() {

			@Override
			public void onMoved(float distance) {
				// TODO Auto-generated method stub
				Activity activity = getActivity();
				if(null!=activity){
					((MainTab)activity).moveTitle(distance,true);
				}

			}

			@Override
			public void onShow() {
				// TODO Auto-generated method stub
//				if(mXListView.getFirstVisiblePosition()==0){
//					Activity activity = getActivity();
//					if(activity!=null&&(activity instanceof  MainTab)){
//						((MainTab) activity).resetTitlePosition();
//					}
//				}
				Fragment fragment = getParentFragment();
				if(null!=fragment) {
					((BroadcastTab) fragment).showAnimation();
				}
			}

			@Override
			public void onHide() {
				// TODO Auto-generated method stub
				Activity activity = getActivity();
				if(activity!=null&&(activity instanceof  MainTab)){
					((MainTab) activity).resetTitlePosition();
				}
			}
		});
		mV_DataView = findViewById(R.id.trending_data);
		mV_EmptyView = findViewById(R.id.trending_no_data);
		RelativeLayout.LayoutParams layoutParams  = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParams.setMargins(0,CommonUtils.getRealtimeWindowHeight(getActivity())/5,0,0);
		mV_EmptyView.setLayoutParams(layoutParams);
		mV_EmptyView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}
	
	/**
	 * 数据初始化
	 */
	private void initData() {
		mHotTags = new ArrayList<AfTagInfo>();
		mBanner_list = new ArrayList<AfTagInfo>();
		
		mTrendingAdapter = new TrendingAdapter(getActivity());
		mXListView.setAdapter(mTrendingAdapter);
		AfTagGetTagsResp resp =mAfCorePalmchat.AfDbBCTagInfoGetList(AfResponseComm.AFTAGINFO_USETYPE_BANNER);
		if(null!=resp&&(null!=resp.tags_list)){
			showBannerData(resp.tags_list);
		}
		AfTagGetTagsResp respHottag =mAfCorePalmchat.AfDbBCTagInfoGetList(AfResponseComm.AFTAGINFO_USETYPE_HOT);
		if(null!=respHottag&&(null!=respHottag.tags_list)){
			showHottagData(respHottag.tags_list);
		}
		AfTagGetTrendsResp afTagGetTrendsResp = mAfCorePalmchat.AfDbBCTagGetDefaultTrend();
		if(null!=afTagGetTrendsResp&&(null!=afTagGetTrendsResp.defaluttrend)){
			showDefaultData(afTagGetTrendsResp.defaluttrend);
		}
		setEmptyViewStatus();
	}
	
	/**
	 * 网络拉取数据
	 */
	private void loadData(int pageId) {
		mAfCorePalmchat.AfHttpAfBCGetTrends(pageId, null, this);
	}
	
	/**
	 * loadmore
	 * @param pageId
	 */
	private void loadDataFromServer(int pageId) {
		int start = (mCurPage-1)*DefaultValueConstant.TRENDINGLOADMORNUMBER+DefaultValueConstant.TRENDINGDEFAULTNUMBER;
		mAfCorePalmchat.AfHttpAfBCGetTrendsMore(pageId,start, DefaultValueConstant.TRENDINGLOADMORNUMBER,this);
	}
	
	/**
	 * 显示banner
	 * @param banner_list
	 */
	private void showBannerData(ArrayList<AfTagInfo> banner_list){
		if((null!=banner_list)&&(banner_list.size()>0)) {
			ArrayList<String> imageUrls = new ArrayList<String>();
			for(AfTagInfo afTagInfo:banner_list) {
				if(null!=afTagInfo.pic_url){
					imageUrls.add(afTagInfo.pic_url);
				}	
			}
			if(null!=mBanner_list){
				mBanner_list.clear();
				mBanner_list.addAll(banner_list);
			}
			mAdvBannerView.updateBannerData(imageUrls,true);
		}
	}
	
	/**
	 * 显示热门tag
	 * @param hottags_list
	 */
	private void showHottagData(ArrayList<AfTagInfo> hottags_list ){
		mHotTagsAdapter.updateData(hottags_list);
		mHotTags.clear();
		mHotTags.addAll(hottags_list);
	}
	
	/**
	 * 显示默认tag的广播(九张图)
	 * @param defaluttrend
	 */
	private void showDefaultData(AfTagGetDefaultTrend defaluttrend){
		if(null!=defaluttrend){
			String tagName = defaluttrend.tag_name;
			if(null!=tagName){
				mTV_TagName.setText(tagName);
			}
			ArrayList<AfBCPriefInfo> brief_list = defaluttrend.brief_list;
			mTrendingAdapter.updateListData(brief_list, true);

		}
	}
	
	/**
	 * 设置空视图状态
	 */
	private void setEmptyViewStatus(){
		if((mAdvBannerView.getCount()<=0)&&(mHotTagsAdapter.getCount()<=0)&&mTrendingAdapter.getCount()<=0){
			mV_EmptyView.setVisibility(View.VISIBLE);
			mV_DataView.setVisibility(View.GONE);
			mXListView.setBackgroundColor(getResources().getColor(R.color.predict_d10));
			mXListView.setPullLoadEnable(false);
		}else{
			mV_EmptyView.setVisibility(View.GONE);
			mV_DataView.setVisibility(View.VISIBLE);
			mXListView.setPullLoadEnable(true);
			mXListView.setBackgroundColor(getResources().getColor(R.color.white));
		}
		
		if(mTrendingAdapter.getCount()<=0){
			mTV_TagName.setVisibility(View.GONE);
			mXListView.setPullLoadEnable(false);
		} else {
			mTV_TagName.setVisibility(View.VISIBLE);
			mXListView.setPullLoadEnable(true);
		}
	}
	
	/**
	 * 双击底部 变顶部
	 */
	public void setListScrolltoTop(boolean isFromTab){
		if(mXListView!=null&&mXListView.getCount()>0){
			if(!isFromTab){
				((MainTab)getActivity()).resetTitlePosition();
			}
			mXListView.setListScrolltoTop();
		}
	}
	
	/**
	 * 停止刷新
	 */
	private void stopRefresh() {	 
		//isRefreshing_mListview = false;
		mXListView.stopRefresh(true);
		mXListView.setRefreshTime(mDateFormat.format(new Date(System.currentTimeMillis())));
	}
	
	/**
	 * 停止加载更多
	 */
	private void stopLoadMore(){ 
		//isLoadingMore_mListview = false;
        mXListView.stopLoadMore(); 
	}
	
	/**
	 * 刷新成功的提示
	 */
	private void showRefreshSuccess() {
		if(!mBln_OperationTask){
			mHandler.sendEmptyMessage(DefaultValueConstant.SHOWFRESHSUCCESS);
		}
	}
	
	/**
	 * 程序刷新
	 */
	private void showRefresh(){
		if (NetworkUtils.isNetworkAvailable(PalmchatApp.getApplication())) {
			if (getActivity() != null) {
				int px = AppUtils.dpToPx(getActivity(), 60);
				mIsInitRefresh= true;
				mXListView.performRefresh(px);
				mCurPage=0; 
			}
		}else {
			ToastManager.getInstance().show(context,mFragmentVisible, 
											getResources().getString(R.string.network_unavailable));
			stopRefresh();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			//tag1
			// tag more
			case R.id.broadcast_tags_seemore_id:{
				doJumpAct(-1,TagsActivity.class);
				break;
			}
			
			case R.id.broadcast_trending_photoname_id:{
				String tagName = ((TextView)v).getText().toString();
				doJumpTagsPageAct(tagName);
				break;
			}
		default:
			break;
		}
	}
	
	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.e(TAG, "----flag:" + flag + "----code:" + code + "----result:" + result);
		if(Consts.REQ_CODE_SUCCESS == code) {
			switch (flag) {
			//拉取数据
			case Consts.REQ_BCGET_TRENDS: {
				doWithNetData(result);
				break;
			}
			//加载更多
			case Consts.REQ_BCGET_TRENDS_MORE:{
				doWidthNetDataLoadMore(result);
				break;
			}
			default:
				break;
			}
		} else if (code == Consts.REQ_CODE_104) {//PageID失效 重新获取
			showRefresh();
		} else {
			Consts.getInstance().showToast(context, code, flag, http_code);
			setEmptyViewStatus();
			stopLoadMore();
			stopRefresh();
		}
	}
	
	/**
	 * 处理网络返回数据
	 * @param result
	 */
	private void doWithNetData(Object result){
		AfTagGetTrendsResp afTagGetTrendsResp = null;
		stopRefresh();
		mAdvBannerView.clean();
		mHotTagsAdapter.clearData();
		mTrendingAdapter.cleanData();
		if(null != result) {
			try {
				afTagGetTrendsResp = (AfTagGetTrendsResp)result;
			} catch (Exception e) {
				e.printStackTrace();
			}										
		}
		
		if(null!=afTagGetTrendsResp) {
			//banner
			if(null!=afTagGetTrendsResp.banner_list){
				ArrayList<AfTagInfo> banner_list = afTagGetTrendsResp.banner_list;
				showBannerData(banner_list);				
				if(null!=mLooperThread&&(null!=mLooperThread.looperHandler)){
					Message msg = Message.obtain();
					msg.what = LooperThread.INSERTDATA_BANNER2DB;
					msg.obj = banner_list;
					mLooperThread.looperHandler.sendMessage(msg);
				}						
			}
			
			//hotTags
			if(null!=afTagGetTrendsResp.hottags_list){
				showHottagData(afTagGetTrendsResp.hottags_list);						
				if(null!=mLooperThread&&(null!=mLooperThread.looperHandler)){
					Message msg = Message.obtain();
					msg.what = LooperThread.INSERTDATA_HOTTAG2DB;
					msg.obj = afTagGetTrendsResp.hottags_list;
					mLooperThread.looperHandler.sendMessage(msg);
				}
			}
			
			//列表
			if(null!=afTagGetTrendsResp.defaluttrend){
				showDefaultData(afTagGetTrendsResp.defaluttrend);						
				if(null!=mLooperThread&&(null!=mLooperThread.looperHandler)){
					Message msg = Message.obtain();
					msg.what = LooperThread.INSERTDATA_DEFAULT2DB;
					msg.obj = afTagGetTrendsResp.defaluttrend;
					mLooperThread.looperHandler.sendMessage(msg);
					mXListView.setPullLoadEnable(true);
				}
			}
			setEmptyViewStatus();
			if((mAdvBannerView.getCount()<=0)&&(mHotTagsAdapter.getCount()<=0)&&mTrendingAdapter.getCount()<=0){
				mXListView.setPullLoadEnable(false);
				ToastManager.getInstance().show(getActivity(), R.string.no_data);
			}					
			showRefreshSuccess();
			mTimeLastRefresh =System.currentTimeMillis(); 							
		}
	}
	
	private void doWidthNetDataLoadMore(Object result){
		stopLoadMore();
		AfTagGetTrendsMoreResp afTagGetTrendsMoreResp = null;
		if(null!=result){
			try{
				afTagGetTrendsMoreResp = (AfTagGetTrendsMoreResp)result;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(null!=afTagGetTrendsMoreResp&&(null!=afTagGetTrendsMoreResp.defaluttrend)) {
			
			ArrayList<AfBCPriefInfo> brief_list = afTagGetTrendsMoreResp.defaluttrend.brief_list;
			if(null!=brief_list&&(brief_list.size()>=DefaultValueConstant.TRENDINGLOADMORNUMBER)){
				mTrendingAdapter.updateListData(brief_list, false);
			}else{
				mXListView.setPullLoadEnable(false);
				ToastManager.getInstance().show(getActivity(), R.string.no_data);
			}
		} else {
			mXListView.setPullLoadEnable(false);
			ToastManager.getInstance().show(getActivity(), R.string.no_data);
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		long curTime = System.currentTimeMillis();
		long durationTime=curTime -mTimeLastRefresh;

		MainTab tab = (MainTab)getActivity();
		if(null != tab){
			tab.hideStateBtn();
		}

		String duration = mDateFormat.format(new Date(durationTime));
		PalmchatLogUtils.println("-wx---durationTime==== "+duration);
		if(isVisibleToUser){
			setEmptyViewStatus();
		}
		if (isVisibleToUser &&(//(null==mTrendingAdapter||mTrendingAdapter.isEmpty())||
				(durationTime>DefaultValueConstant.BROADEXPIRE_DATE))) {
			showRefresh();
		}
	}
	
	@Override
	public void onRefresh(View view) {
		// TODO Auto-generated method stub
		if (NetworkUtils.isNetworkAvailable(context)) {
			onPause();
			if(!mIsInitRefresh){
				//((MainTab)getActivity()).resetTitlePosition();
				mHandler.sendEmptyMessage(DefaultValueConstant.RESETTILEPOSITION);
			}
			mIsInitRefresh = false;
			mTimeLastRefresh = System.currentTimeMillis();
			mPageId = (int) System.currentTimeMillis() + new Random(10000).nextInt();
			mCurPage = 0;
			loadData(mPageId);
		} else {
			ToastManager.getInstance().show(context,getUserVisibleHint(), context.getString(R.string.network_unavailable));
			setEmptyViewStatus();
			stopRefresh();
			if (!mIsInitRefresh) {
				//((MainTab) getActivity()).resetTitlePosition();
				mHandler.sendEmptyMessage(DefaultValueConstant.RESETTILEPOSITION);
			}
			mIsInitRefresh = false;
		}
	}

	@Override
	public void onLoadMore(View view) {
		// TODO Auto-generated method stub
		if (NetworkUtils.isNetworkAvailable(context)) {
			mCurPage++; 
			loadDataFromServer(mPageId);
		}else {
			ToastManager.getInstance().show(context,getUserVisibleHint(), context.getString(R.string.network_unavailable));
			stopLoadMore();
		}
	}
	
	//banner跳转
	@Override
	public void goToActivity(int item_id) {
		// TODO Auto-generated method stub
		if(item_id<mBanner_list.size()){
			AfTagInfo afTagInfo = mBanner_list.get(item_id);
			if(null!=afTagInfo){
				switch(afTagInfo.type){
					//tag
					case AfResponseComm.AFMOBI_TREND_CONTENT_TAG_TYPE:{
						doJumpTagsPageAct(afTagInfo.tag);
						break;
					}
					
					//url 调到内置浏览器
					case AfResponseComm.AFMOBI_TREND_CONTENT_URL_TYPE:{
						doJunmpDetailsActivity(afTagInfo.tag);
						break;
					}
					
					//调到hotday
					case AfResponseComm.AFMOBI_TREND_HOT_TODAY_TYPE:{
						doJumpHotToday();
						break;
					}
					default:
						ToastManager.getInstance().show(context, R.string.broadcast_trending_version_notsupport);
						break;
					
				}
			}
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(null!=mAdvBannerView) {
			mAdvBannerView.clean();
		}
	}	
	
	/**
	 * 跳转到url访问界面
	 * @param urlName
	 */
	private void doJunmpDetailsActivity(String urlName){
		if(TextUtils.isEmpty(urlName)){
			ToastManager.getInstance().show(getActivity(), R.string.broadcast_trending_tagnname_limit);
			return ;
		}			
		Intent intent = new Intent();
		intent.putExtra(IntentConstant.RESOURCEURL, urlName);
		intent.setClass(getActivity(), InnerNoAbBrowserActivity.class);
		startActivity(intent);
	}

	/**
	 * 跳转到HotToday
	 */
	private void doJumpHotToday() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), HotTodayActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 跳转到tagsPage
	 * @param tagName
	 */
	private void doJumpTagsPageAct(String tagName) {
		if(TextUtils.isEmpty(tagName)){
			ToastManager.getInstance().show(getActivity(), R.string.broadcast_trending_tagnname_limit);
			return ;
		}		
		Intent intent = new Intent();
		intent.putExtra(IntentConstant.TITLENAME, tagName);
		intent.setClass(getActivity(), TagPageActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 跳转到对应的activity
	 * @param position
	 */
	private void doJumpAct(int position, Class<?> cls) {
		if(-1==position) {
			
		}
		
		Intent intent =new Intent();
		intent.setClass(getActivity(), cls);
		startActivity(intent);
	}
	
	@SuppressLint("NewApi")
	public void setListViewOffSet(float offSet){
    	if(null!=mXListView){    		
    		if(mXListView.getScrollY()<offSet){
    			mXListView.setBlankViewHeight(offSet);        		
    		}
    	}
    }
	
	@SuppressLint("NewApi")
	public void resetListViewPosition(){
		if(null!=mXListView){
			mXListView.setBlankViewHeight(0);
		}
	}
	
	/**
	 *列表停止滑动
	 */
	public void stopListViewScroll(){
		if(null!=mXListView){
			mXListView.stopListViewScroll();
		}
	}
	
	/**
	 * 
	 * @author star
	 *
	 */
	class LooperThread extends Thread {
		private static final int INSERTDATA_BANNER2DB = 1001;
		private static final int INSERTDATA_HOTTAG2DB = 1003;
		private static final int INSERTDATA_DEFAULT2DB = 1005;
		private static final int GETDATA_DEFAULTFROMDB = 1006;
		
		Handler looperHandler;
		Looper looper;
		@SuppressLint("HandlerLeak")
		@Override
		public void run(){
			Looper.prepare();
			looper = Looper.myLooper();
			looperHandler = new Handler(){
				@SuppressWarnings("unchecked")
				public void handleMessage(Message msg){
					switch(msg.what){
					    case INSERTDATA_BANNER2DB:{
					    	if(null!=msg.obj){
					    		ArrayList<AfTagInfo> afTagInfos=null;
					    		try{
					    			afTagInfos = (ArrayList<AfTagInfo>)msg.obj;
					    			if(null!=afTagInfos&&(afTagInfos.size()>0)){
					    				int res = -1;
					    				res = mAfCorePalmchat.AfDbBCTagInfoDeleteByType(AfResponseComm.AFTAGINFO_USETYPE_BANNER);
					    				for(int i=0;i<afTagInfos.size();i++){
					    					AfTagInfo afTagInfo = afTagInfos.get(i);
					    					afTagInfo.use_type = AfResponseComm.AFTAGINFO_USETYPE_BANNER;
					    					res = 	mAfCorePalmchat.AfDbBCTagInfoInsert(afTagInfo);
					    				}
					    			}
					    		} catch (Exception e) {
					    			e.printStackTrace();
					    		}
					    	}
					    	break;
					    }
					    
					    case INSERTDATA_HOTTAG2DB:{
					    	if(null!=msg.obj){
					    		ArrayList<AfTagInfo> afTagInfos=null;
					    		try{
					    			afTagInfos = (ArrayList<AfTagInfo>)msg.obj;
					    			if(null!=afTagInfos&&(afTagInfos.size()>0)){
					    				mAfCorePalmchat.AfDbBCTagInfoDeleteByType(AfResponseComm.AFTAGINFO_USETYPE_HOT);
					    				int res=-1;
					    				for(int i=0;i<afTagInfos.size();i++){
					    					AfTagInfo afTagInfo = afTagInfos.get(i);
					    					afTagInfo.use_type = AfResponseComm.AFTAGINFO_USETYPE_HOT;
					    					res=mAfCorePalmchat.AfDbBCTagInfoInsert(afTagInfo);
					    				}
					    			}
					    		} catch (Exception e) {
					    			e.printStackTrace();
					    		}
					    	}
					    	break;
					    }
						case INSERTDATA_DEFAULT2DB:{
							if(null!=msg.obj){
								try{
									int res = -1;
									AfTagGetDefaultTrend afTagGetDefaultTrend = (AfTagGetDefaultTrend)msg.obj;
									res = mAfCorePalmchat.AfDbBCTagDeleteDefaultTrend();
									res = mAfCorePalmchat.AfDbBCTagDefaultTrendSave2Db(afTagGetDefaultTrend);
								} catch(Exception e){
									e.printStackTrace();
								}								
							}
							
							break;
						}
						case GETDATA_DEFAULTFROMDB:{
							break;
						}
						default:
							break;
					}
				}
			};
			Looper.loop();
			
		}
		
	}
}
