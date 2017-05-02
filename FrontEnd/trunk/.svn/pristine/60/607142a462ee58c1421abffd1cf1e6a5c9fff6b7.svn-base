package com.afmobi.palmchat.ui.activity.tagpage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.tagpage.adapter.TagsAdapter;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfPalmchat;
import com.core.AfResponseComm.AfTagGetTagsResp;
import com.core.AfResponseComm.AfTagInfo;
import com.core.Consts;
import com.core.listener.AfHttpResultListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author Transsion
 *
 */
public class TagsActivity extends BaseActivity implements OnClickListener,AfHttpResultListener,IXListViewListener{

	private static final String TAG = TagsActivity.class.getSimpleName();
	/**列表*/
	private XListView mXListView;
	/**Title*/
	private TextView mTV_Title;
	/**适配器*/
	private TagsAdapter mTagsAdapter;
	/**轮训线程*/
	private LooperThread mLooperThread;
	/**编辑框*/
	private EditText mEditText;
	/**检索按钮*/
	private Button mBtn_Search;
	/**返回键*/
	private Button mBtn_Back;
	/**中间件接口*/
	private AfPalmchat mAfCorePalmchat;
	/**当前页序号*/
	private int mCurPageIndex;
	/**PageId*/
	private int mPageId;
	/**检索结果当前页*/
	private int mCurSearchPageIndex;
	/**一页的数量*/
	private final int LIMIT = 30;
	/**当前操作状态,默认是刷新  true为loadMore;false为reFresh*/
	private boolean mBln_OperationTask = false;	
	/**数据列表*/
	private ArrayList<AfTagInfo> mAfTagInfoList;
	/**检索数据列表*/
	private ArrayList<AfTagInfo> mSearchInfoList;
	/**检索按钮起作用标记*/
	private boolean mBln_SearchPermit;
	/**关键字*/
	private String mCurKeyWord;
	/**刷新成功视图*/
	private View mV_TopRefresh;
	/**空视图*/
	private View mV_EmptyView;
	/**时间格式*/
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
	/**空视图*/
	/**Handler*/
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch(msg.what){
				case LooperThread.SEARCH_CONTENT:{
					if(null!=msg.obj) {
						try {
							mTagsAdapter.updateData((ArrayList<AfTagInfo>)msg.obj, true);
						} catch(Exception e){
							e.printStackTrace();
						}
					}
					break;
				}
				default:
					break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAfTagInfoList = new ArrayList<AfTagInfo>();
		mSearchInfoList = new ArrayList<AfTagInfo>();
		mLooperThread = new LooperThread();
		mLooperThread.setName(TAG);
		mLooperThread.start();
	}
	
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_tags);
		mTV_Title = (TextView)findViewById(R.id.tags_title_text);
		mBtn_Back = (Button)findViewById(R.id.tags_back_button);
		mBtn_Back.setOnClickListener(this);
		mBtn_Search = (Button)findViewById(R.id.tags_search_id);
		mBtn_Search.setOnClickListener(this);
		
		mEditText=(EditText)findViewById(R.id.tags_search_et);
		mEditText.addTextChangedListener(new SearchTextWatcher());
		mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				/*判断是否是“search”键*/
				if(EditorInfo.IME_ACTION_SEARCH== actionId ){
					/*隐藏软键盘*/
					doClickSearchId();
					CommonUtils.closeSoftKeyBoard(v);					
					return true;
				}
				return false;
			}
		});
		
		mXListView = (XListView)findViewById(R.id.tags_listview_id);
		mXListView.setXListViewListener(this);
		mXListView.setPullLoadEnable(true);
		mXListView.setPullRefreshEnable(true);
		mXListView.setOnScrollListener(new ImageOnScrollListener());
		mXListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				String  tagString = mTagsAdapter.getCurTag(arg2);
				Intent intent = new Intent();
				intent.setClass(TagsActivity.this, TagPageActivity.class);
				intent.putExtra(IntentConstant.TITLENAME,tagString);
				startActivity(intent);
			}
		
		});
		
		mTagsAdapter = new TagsAdapter(this);
		mXListView.setAdapter(mTagsAdapter);
		mV_TopRefresh = findViewById(R.id.tags_top_refresh);
		
		mV_EmptyView = findViewById(R.id.tags_no_data);
		mV_EmptyView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		String title = getIntent().getStringExtra(IntentConstant.TITLENAME);
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		
		if(!TextUtils.isEmpty(title)){
			mTV_Title.setText(title);
		} else {
			mTV_Title.setText(getResources().getString(R.string.broadcast_trending_tag)+"Tags");
		}
		reRefresh();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.tags_search_id: {
				doClickSearchId();
				break;
			}
			case R.id.tags_back_button:{
				finish();
				break;
			}
		}
	}
	
	/**
	 * 处理检索按钮点击事件
	 */
	private void doClickSearchId(){
		String keyWord =filterKeyWord(mEditText.getText().toString()).trim();
		if(View.VISIBLE!=mEditText.getVisibility()){
			mBtn_Search.setBackgroundResource(R.drawable.ic_search_not_click);
			mBln_OperationTask =false;
			mBln_SearchPermit = false;
			mTV_Title.setVisibility(View.GONE);
			mEditText.setVisibility(View.VISIBLE);
			mEditText.setFocusable(true);   
			mEditText.setFocusableInTouchMode(true);   
			mEditText.requestFocus();
			CommonUtils.showSoftKeyBoard(mEditText);
		} else if(!TextUtils.isEmpty(keyWord)) {
			mCurKeyWord = keyWord;
			mBln_OperationTask =false;
			searchData(keyWord,false);
		} else {
			mTagsAdapter.updateData(mAfTagInfoList, true);
		}
	}
	
	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.e(TAG, "----flag:" + flag + "----code:" + code + "----result:" + result);
		if(Consts.REQ_CODE_SUCCESS == code) {
			switch (flag) {
				//tags请求响应
				case Consts.REQ_BCGET_HOT_TAGS:{
					doReqHotTAGS(result);
					break;
				}
				//search请求响应
				case Consts.REQ_BCGET_SEARCH_TAGS:{
					doResSearch(result);
					break;
				}
				default:
					break;
			}
		}else if (code == Consts.REQ_CODE_104) {//PageID失效 重新获取
			onRefresh(null);
		} else {
			Consts.getInstance().showToast(context, code, flag, http_code);
		}
	}
	
	/**
	 * 处理HotTags相应数据
	 * @param result
	 */
	private void doReqHotTAGS(Object result){
		AfTagGetTagsResp afTagGetTagsResp = null;
		if(null!=result) {
			
			try {
				afTagGetTagsResp = (AfTagGetTagsResp)result;
			} catch(Exception e) {
				e.printStackTrace();
			}	
		}
		
		if((null!=afTagGetTagsResp)&&(null!=afTagGetTagsResp.tags_list)) {
			if(!mBln_OperationTask&&(!mAfTagInfoList.isEmpty())){
				mAfTagInfoList.clear();
			}
			
			if(afTagGetTagsResp.tags_list.size()>0){
				mCurPageIndex++;
			} else if(mBln_OperationTask){
				mXListView.setPullLoadEnable(false);
				ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.no_data);
			}
			
			mAfTagInfoList.addAll(afTagGetTagsResp.tags_list);
			mTagsAdapter.updateData(afTagGetTagsResp.tags_list,!mBln_OperationTask);
		}
		
		showEmptyView();
		stopOperation();
		showRefreshSuccess();
	}
		
	/**
	 * 处理服务器检索返回数据
	 * @param result
	 */
	private void doResSearch(Object result){
		AfTagGetTagsResp afTagGetTagsResp = null;
		if(null!=result) {
			
			try {
				afTagGetTagsResp = (AfTagGetTagsResp)result;
			} catch(Exception e) {
				e.printStackTrace();
			}	
		}
		if((null!=afTagGetTagsResp)&&(null!=afTagGetTagsResp.tags_list)) {
			if((!mBln_OperationTask)&&(!mSearchInfoList.isEmpty())){
				mSearchInfoList.clear();
			}
			if(afTagGetTagsResp.tags_list.size()>0){
				mCurSearchPageIndex++;
			}
			if(afTagGetTagsResp.tags_list.size()<=0){
				mXListView.setPullLoadEnable(false);
				ToastManager.getInstance().show(this, R.string.no_data);
			}
			
			mSearchInfoList.addAll(afTagGetTagsResp.tags_list);
			mTagsAdapter.updateData(afTagGetTagsResp.tags_list,!mBln_OperationTask);
		} else {
			mXListView.setPullLoadEnable(false);
			ToastManager.getInstance().show(this, R.string.no_data);
		}
		if(mSearchInfoList.size()<=0){
			mBln_SearchPermit = false;
			mBtn_Search.setBackgroundResource(R.drawable.ic_search_not_click);
		}
		mXListView.setListScrolltoTop();
		showEmptyView();	
		CommonUtils.closeSoftKeyBoard(mEditText);
		stopOperation();
		showRefreshSuccess();
	}
	
	@Override
	public void onRefresh(View view) {
		// TODO Auto-generated method stub
		if (NetworkUtils.isNetworkAvailable(context)) {
			mBln_OperationTask = false;
			mXListView.setPullLoadEnable(true);
			if(TextUtils.isEmpty(mEditText.getText().toString())){
				loadData(false);
			} else {
				searchData(mCurKeyWord,false);
			}
		} else {
			ToastManager.getInstance().show(this, R.string.network_unavailable);
			stopRefresh();
			showEmptyView();
		}
	}

	@Override
	public void onLoadMore(View view) {
		// TODO Auto-generated method stub
		if(NetworkUtils.isNetworkAvailable(this)) {
			mBln_OperationTask = true;
			if(TextUtils.isEmpty(mEditText.getText().toString())){
				loadData(true);
			} else {
				searchData(mCurKeyWord,true);
			}
			
		} else {
			ToastManager.getInstance().show(context, R.string.network_unavailable);
			stopLoadMore();
		}
	}
	
	/**
	 * 重新拉取数据
	 */
	private void loadData(boolean isLoadMore) {
		//重新加载数据，页数置为0
		if(!isLoadMore){
			mCurPageIndex=0;
			mPageId = (int) System.currentTimeMillis() + new Random(10000).nextInt();
		}
		mAfCorePalmchat.AfHttpAfBCGetHotTags( mPageId, mCurPageIndex*LIMIT, LIMIT, this);
	}
	
	/**
	 * 检索数据
	 * @param keyWord
	 */
	@SuppressLint("DefaultLocale")
	private void searchData(String keyWord,boolean isLoadMore) {
		if(TextUtils.isEmpty(keyWord)||(false==mBln_SearchPermit)) {
			stopOperation();
			return;
		}
		mTagsAdapter.setKeyword(mCurKeyWord.toLowerCase());
		if(!isLoadMore) {
			mCurSearchPageIndex=0;
			mSearchInfoList.clear();
			mPageId = (int) System.currentTimeMillis() + new Random(10000).nextInt();
		}
		
		mAfCorePalmchat.AfHttpAfBCSearchTags( mPageId, mCurSearchPageIndex*LIMIT, LIMIT,keyWord, this);
	}
	
	/**
	 * 模拟下拉
	 */
	private void reRefresh(){
		int px = AppUtils.dpToPx(this, 60);
		mXListView.performRefresh(px);
	}
	
	/**
	 * 刷新成功的提示
	 */
	private void showRefreshSuccess() {
		if(!mBln_OperationTask){
			mV_TopRefresh.getBackground().setAlpha(100);
			mV_TopRefresh.setVisibility(View.VISIBLE);
			mHandler.postDelayed(new Runnable() {				
				@Override
				public void run() {
					mV_TopRefresh.setVisibility(View.GONE);
					PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_REFRESH );						
				}
			}, 1000);
		}
	}
	
	/**
	 * 停止操作
	 */
	private void stopOperation(){
		if(mBln_OperationTask){
			stopLoadMore();
		} else {
			stopRefresh();
		}
	}
	/**
	 * 停止刷新
	 */
	private void stopRefresh() {	 
		mXListView.stopRefresh(true);
		mXListView.setRefreshTime(mDateFormat.format(new Date(System.currentTimeMillis())));
	}
	
	/**
	 * 停止加载更多
	 */
	private void stopLoadMore(){ 
        mXListView.stopLoadMore(); 
	 }
	
	/**
	 * g过滤检索关键字
	 * @param keyWord
	 * @return
	 */
	private String  filterKeyWord(String keyWord) {
		if(null==keyWord){
			return "";
		}
		return keyWord.toString().replace("*", "\\*").replace("?", "\\?").replace("(","\\(").replace(")", "\\)")
			.replace("[", "\\[").replace("]", "\\]").replace("^", "\\^").replace("\\","\\")
			.replace("$ ", "\\$").replace("+", "\\+").replace(".", "\\.^").replace("{","\\{").replace("}","\\}");
	}
	
	/**
	 * 设置空视图
	 */
	private void showEmptyView(){
		if(mTagsAdapter.getCount()>0){
			mV_EmptyView.setVisibility(View.GONE);
		} else {
			mV_EmptyView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 轮询线程
	 *
	 */
	class LooperThread extends Thread {
	
		public static final  int SEARCH_CONTENT =  2001;
		Handler nHandler;
		Looper nLooper;

		@SuppressLint("HandlerLeak")
		public void run(){
			Looper.prepare();
			nLooper = Looper.myLooper();
			nHandler = new Handler() {
				
				public void handleMessage(Message msg){
					switch (msg.what) {
					   case SEARCH_CONTENT: {						   
						   if(null!=msg.obj) {
							   String keyWord = (String)msg.obj;
							   Pattern pattern = null;
							   try{
								   pattern = Pattern.compile(keyWord.toUpperCase());
							   } catch (Exception e) {
								   e.printStackTrace();
							   }
							   
							   if(null==pattern) {
									break;
							   }
							   ArrayList<AfTagInfo> afTagInfoList = new ArrayList<AfTagInfo>(); 
							   for(AfTagInfo afTagInfo:mAfTagInfoList) {
								   Matcher matcher = pattern.matcher(afTagInfo.tag); 
								   if(matcher.find()){
									   afTagInfoList.add(afTagInfo);
								   }
							   }
							   mHandler.obtainMessage(LooperThread.SEARCH_CONTENT, afTagInfoList).sendToTarget();
							   
						   }
						   
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

	 private class ImageOnScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
            CommonUtils.closeSoftKeyBoard(mEditText);
		}
		 
	 }
	 
	/**
	 * 监听EditText
	 * @author Transsion
	 *
	 */
	private class SearchTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String keyWord = filterKeyWord(s.toString()).trim();
			if(TextUtils.isEmpty(keyWord)){
				//mTagsAdapter.clearData();
				mBtn_Search.setBackgroundResource(R.drawable.ic_search_not_click);
				mBln_SearchPermit = false;
				mXListView.setPullRefreshEnable(true);
				mTagsAdapter.setKeyword("");
				mTagsAdapter.updateData(mAfTagInfoList, true);
			} else {
				mBtn_Search.setBackgroundResource(R.drawable.btn_tags_search_select);
				mBln_SearchPermit = true;
			}			
		}		
	}
}
