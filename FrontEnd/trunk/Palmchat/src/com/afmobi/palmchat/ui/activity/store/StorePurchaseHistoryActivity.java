package com.afmobi.palmchat.ui.activity.store;

import java.util.ArrayList;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.store.adapter.StorePurchaseHistoryAdapter;
import com.afmobi.palmchat.ui.customview.RefreshableView;
import com.afmobi.palmchat.ui.customview.RefreshableView.RefreshListener;
//import com.afmobi.palmchat.util.image.ImageOnScrollListener;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.core.AfPalmchat;
import com.core.AfStoreConsumeRecord;
import com.core.AfStoreConsumeRecord.AfConsumeItem;
import com.core.Consts;
import com.core.listener.AfHttpResultListener;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 购买记录界面
 *
 */
public class StorePurchaseHistoryActivity extends BaseActivity implements OnClickListener, AfHttpResultListener, RefreshListener {

	/**tag*/
	private static final String TAG = StorePurchaseHistoryActivity.class.getCanonicalName();
	/**jni本地类库*/
	private AfPalmchat mAfCorePalmchat;
	/**下拉刷新view*/
	private RefreshableView mRefrshView;
	/**显示列表*/
	private ListView mListView;
	/**适配器*/
	private StorePurchaseHistoryAdapter mAdapter;
	/**购买记录显示数据*/
	private ArrayList<AfConsumeItem> mListData = new ArrayList<AfConsumeItem>();
	
	 
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		mAfCorePalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
		setContentView(R.layout.store_purchase_history);
		((TextView)findViewById(R.id.title_text)).setText(R.string.purchase_history);
		
		findViewById(R.id.back_button).setOnClickListener(this);
		
		
		mRefrshView = (RefreshableView) findViewById(R.id.refresh_list1);
		mRefrshView.setRefreshListener(this);
		
		mAdapter = new StorePurchaseHistoryAdapter(this, mListData);
		mListView = (ListView) findViewById(R.id.listview1);
//		mListView.setOnScrollListener(new ImageOnScrollListener(mListView ));
		mListView.setAdapter(mAdapter);		
		refresRecordList();		
	}
	
	/**
	 * 刷新记录列表
	 */
	private void refresRecordList(){
		mRefrshView.showRefresh();
		mAfCorePalmchat.AfHttpStoreGetConsumeRecord(Consts.PROD_TYPE_EXPRESS_WALLPAPER, 0, 10, null, this);
 	
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
//		key back
		case R.id.back_button:
			finish();
			break;	
		}
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code,
			Object result, Object user_data) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("StorePurchaseHistoryActivity: flag = " + flag + " code="+ code + " result= "+ result);
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			case Consts.REQ_STORE_CONSUME_RECORD:
				mRefrshView.finishRefresh();
				
				AfStoreConsumeRecord data = (AfStoreConsumeRecord)result;
				if (data != null) {
				int size = data.page_info.prof_list.size();	
				PalmchatLogUtils.println("--store consume list size:" + size);
				
				mAdapter.setAddr(data.url);
				
				mListData.clear();
				mListData.addAll(data.page_info.prof_list);
				mAdapter.notifyDataSetChanged();
				
				}
				
				break;
			}
			
		} else {
			switch (flag) {
			case Consts.REQ_STORE_CONSUME_RECORD:
				mRefrshView.finishRefresh();
				break;
			
			}
			
			if (!isFinishing()) {
				Consts.getInstance().showToast(context, code, flag, http_code);
			}
		}
	}

	@Override
	public void onRefresh(RefreshableView view) {
		// TODO Auto-generated method stub
		if (view.getId() == R.id.refresh_list1) {
			refresRecordList();
		}
	}

}
