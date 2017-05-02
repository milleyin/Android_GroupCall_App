package com.afmobi.palmchat.ui.activity.profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.setting.adapter.GiftDetailAdapter;
import com.afmobi.palmchat.util.CommonUtils;
import com.core.AfGiftInfo;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

public class GiftDetailActivity extends BaseActivity implements AfHttpResultListener, IXListViewListener{

	private AfPalmchat mAfCorePalmchat;
//	private GridView gridview;
	private XListView vListView;
	private int mOffset = 0;
	private int mCount = 10;
	private boolean isRefreshing = false;
	private boolean isLoadingMore = false;
	
	private GiftDetailAdapter adapterListView;
	private ArrayList<AfGiftInfo> listChats = new ArrayList<AfGiftInfo>();
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
	
	private final static int UP = 0;
	private final static int DOWN = 1;
	private final static int FIRST = 2;
	
	private boolean isFirst = true;
	
	@Override
	protected void onResume() {
		super.onResume();
//		MobclickAgent.onPageStart("GiftDetailActivity");
//	    MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd("GiftDetailActivity");
//	    MobclickAgent.onPause(this);
	}
	
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_gift_detail);
		mAfCorePalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
		((TextView) findViewById(R.id.title_text)).setText(R.string.hot);
		View backView = findViewById(R.id.back_button);
		if (backView != null) {
			backView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		}
		
//		gridview = (GridView) findViewById(R.id.gridview);
		vListView = (XListView) findViewById(R.id.listview);
		vListView.setPullLoadEnable(true);
		vListView.setXListViewListener(this);
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		List<AfGiftInfo> list = getRecentData();
		if(list != null && list.size() > 0){
			listChats.addAll(list);
		}else{
			getGiftReceivedDetails(FIRST);
		}
		isFirst = false;
		setAdapter(DOWN);
	}
	
	private void getGiftReceivedDetails(int userdata) {
		// TODO Auto-generated method stub
		if(isFirst){
			showProgressDialog(R.string.please_wait);
		}
		if(UP == userdata || FIRST == userdata){
			mAfCorePalmchat.AfHttpGetGifts(mOffset, mCount, 0, true, true,userdata, this);
		}else{
			mAfCorePalmchat.AfHttpGetGifts(mOffset, mCount, 0, false, true,userdata, this);
		}
	}

	private List<AfGiftInfo> getRecentData() {
		 PalmchatLogUtils.println("mOffset  "+mOffset +"  mCount  "+mCount);
		 AfGiftInfo[] recentDataArray = mAfCorePalmchat.AfDbGiftInfoGetRecord(mOffset, mCount);
		 if(recentDataArray == null || recentDataArray.length == 0){
			return new  ArrayList<AfGiftInfo>();
		 }
		 List<AfGiftInfo> recentDataList = Arrays.asList(recentDataArray);
		 return recentDataList;
	}

	private void setAdapter(int userdata) {
		// TODO Auto-generated method stub
		if(adapterListView == null){
			adapterListView = new GiftDetailAdapter(context,listChats);
//			vListView.setOnScrollListener(new ImageOnScrollListener(vListView, listViewAddOn));
			vListView.setAdapter(adapterListView);  
		}else{
//			adapterListView.setList(listChats);
//			if(afMessageInfo != null && MessagesUtils.ACTION_UPDATE == afMessageInfo.action){
//				PalmchatLogUtils.println("setAdapter  afMessageInfo.action ACTION_UPDATE");
//				vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
//			}else if(afMessageInfo != null){
//				PalmchatLogUtils.println("setAdapter  afMessageInfo.action "+afMessageInfo.action);
//				vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//			}
			adapterListView.notifyDataSetChanged();
			vListView.setSelection(adapterListView.getCount());
			if(UP == userdata && 0 < adapterListView.getCount()){
				vListView.setSelection(0);
			}
//			if ((mIsNoticefy && 0 < adapterListView.getCount()) || isBottom) {
//				PalmchatLogUtils.println("Chatting  setAdapter");
//				vListView.setSelection(adapterListView.getCount());
//				mIsNoticefy = false;
//			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return false;
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, List<AfGiftInfo>> {

    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			listHeader.setVisibility(View.VISIBLE);
//			AnimUtils.getInstance(Chatting.this).start(listHeaderProgress);
		}
    	
    	
        @Override
        protected List<AfGiftInfo> doInBackground(Void... params) {
            // Simulates a background job.
        	List<AfGiftInfo> recentData = getRecentData();
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            return recentData;
        }

        @Override
        protected void onPostExecute(List<AfGiftInfo> result) {
//            mListItems.addFirst("Added after refresh...");
        	if(result != null && result.size() > 0){
        		bindData(result,DOWN);
        		stopLoadMore();
        	}else{
        		getGiftReceivedDetails(DOWN);
        	}
        	
            super.onPostExecute(result);
        }
    }
	
	
	 private void stopRefresh() {
	    	// TODO Auto-generated method stub
	    	isRefreshing = false;
	    	
	    	vListView.stopRefresh();
			vListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
	 }
	 
	 
	 private void stopLoadMore(){
		 isLoadingMore = false;
		 vListView.stopLoadMore();
	 }

	
	 private void bindData(List<AfGiftInfo> result,int userdata) {
		 if(UP == userdata){
			 if(listChats.size() == 0){
				 listChats.addAll(result); 
			 }else{
				 for (int i = 0; i < result.size(); i++) {
					 CommonUtils.getRealGiftList(listChats, result.get(i));
				 }
			 }
		 }else{
//			 for (int i = 0; i < result.size(); i++) {
//				 CommonUtils.getRealGiftList(listChats, result.get(i));
//			 }
			 listChats.addAll(result);
		 }
		 setAdapter(userdata);
	 }
	
	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result,Object user_data) {
		// TODO Auto-generated method stub
		dismissProgressDialog();
		int userdata = -1;
		if(user_data != null){
			userdata = (Integer) user_data;
		}
		if(Consts.REQ_CODE_SUCCESS == code){
			AfGiftInfo[] afGiftInfos = (AfGiftInfo[]) result;
			if(afGiftInfos != null && afGiftInfos.length > 0){
				List<AfGiftInfo> list = Arrays.asList(afGiftInfos);
				bindData(list,userdata);
			}
		}else{
			Consts.getInstance().showToast(context, code, flag,http_code);
		}
		if(UP == userdata){
			stopRefresh();
		}else{
			stopLoadMore();
		}
	}

	@Override
	public void onRefresh(View view) {
		// TODO Auto-generated method stub
		if(!isRefreshing){
			isRefreshing = true;
			AfGiftInfo afGiftInfoFirst = adapterListView.getFirstMsg();
			if(afGiftInfoFirst != null){
				mOffset = afGiftInfoFirst.identify;
			}
			getGiftReceivedDetails(UP);
		}
	}

	@Override
	public void onLoadMore(View view) {
		// TODO Auto-generated method stub
		if(!isLoadingMore){
			isLoadingMore = true;
			AfGiftInfo afGiftInfoLast = adapterListView.getLastMsg();
			if(afGiftInfoLast != null){
				mOffset = afGiftInfoLast.identify;
				new GetDataTask().execute();
			}
		}
	}
	
}
