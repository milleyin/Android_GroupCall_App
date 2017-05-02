package com.afmobi.palmchat.ui.activity.setting;

import java.util.ArrayList;
import java.util.List;

import com.afmobi.palmchat.BaseActivity;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.setting.adapter.BlockedListAdapter;
import com.afmobi.palmchat.util.CommonUtils;
import com.core.AfFriendInfo;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 黑名单列表界面
 * @author "starw"
 *
 */
public class BlockedListActivity extends BaseActivity implements OnClickListener {
	
	private final String TAG = BlockedListActivity.class.getCanonicalName();
	/**适配器*/
	private BlockedListAdapter mAdapter;
	/**列表*/
	private ListView mListView;
	/**数据*/
	private List<AfFriendInfo> mFrdList;
	
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_setting_blockedlist);
		((TextView)findViewById(R.id.title_text)).setText(R.string.setting_blockedlist);
		findViewById(R.id.back_button).setOnClickListener(this);
	
		mListView = (ListView)findViewById(R.id.blocklist_listview_id);
		mListView.setEmptyView(findViewById(R.id.blocklist_lin_no_data));
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		mFrdList = new ArrayList<AfFriendInfo>();
		mAdapter = new BlockedListAdapter(this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				if(CommonUtils.isFastClick()||(position>=mFrdList.size())) {
					return ;
				}
		        Intent intent = new Intent(BlockedListActivity.this, ProfileActivity.class);
		        AfProfileInfo info = AfFriendInfo.friendToProfile(mFrdList.get(position));
		        intent.putExtra(JsonConstant.KEY_PROFILE, info);
		        intent.putExtra(JsonConstant.KEY_FLAG, true);
		        intent.putExtra(JsonConstant.KEY_AFID, info.afId);
		        intent.putExtra(JsonConstant.KEY_USER_MSISDN, info.user_msisdn);
		        PalmchatLogUtils.i(TAG,"current click id ===="+info.afId);
		        startActivity(intent);
			}
			
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mFrdList = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_BF).getList();
		mAdapter.updata(mFrdList);
	}
	
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
    }
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		    case R.id.back_button: {
		    	finish();
		    	break;
		    }
		    default:
		    	break;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(null!=mFrdList) {
			mFrdList.clear();
			mFrdList=null;
		}
		if(null!=mAdapter) {
			mAdapter.clean();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyUp(keyCode, event);
	}

}
