package com.afmobi.palmchat.ui.activity.people;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.ui.activity.friends.FriendsActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
//import com.afmobi.palmchat.util.image.ImageOnScrollListener;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.core.AfFriendInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

/**
 * People You May Know two page
 * 
 * @author heguiming 2013-11-14
 * 
 */
public class PeopleYouMayKnowTwoActivity extends BaseActivity implements
		AfHttpResultListener, OnItemClickListener, OnClickListener {
	
	private static final String TAG = PeopleYouMayKnowTwoActivity.class.getCanonicalName();
	
	private ListView mListView;
//	private ListViewAddOn listViewAddOn = new ListViewAddOn();
	private PeopleYouMayKnowTwoAdapter adapter;
	
	private String[] listArray;
	
	/**
	 * Looper线程
	 */
	private LooperThread looperThread;
	
	public static final int FIND_OK = 0;
	public static final int INIT_OK = 9000;
	
	@SuppressWarnings("unchecked")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FIND_OK:
				List<AfFriendInfo> friendList = (List<AfFriendInfo>) msg.obj;
				setAdapter(friendList);
				break;
			case INIT_OK:
				if (null != looperThread.looperHandler) {
					looperThread.looperHandler.sendEmptyMessage(LooperThread.SELECT_FRIEND);
				}
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		looperThread = new LooperThread();
		looperThread.setName(TAG);
		looperThread.start();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		looperThread.looper.quit();
	}

	@Override
	public void findViews() {
		setContentView(R.layout.activity_people_you_may_know);
		
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		int count = getIntent().getIntExtra(JsonConstant.KEY_NO,0);
		TextView mTitleTxt = (TextView)findViewById(R.id.title_text);
		mTitleTxt.setText(count + " " + getString(R.string.mutual_friends));
		findViewById(R.id.back_button).setOnClickListener(this);
		
	}

	@Override
	public void init() {
		listArray = getIntent().getStringArrayExtra(JsonConstant.KEY_LIST_STR);
	}
	
	public void setAdapter(List<AfFriendInfo> friendList) {
		if (null == adapter) {
			adapter = new PeopleYouMayKnowTwoAdapter(this, friendList );
//			mListView.setOnScrollListener(new ImageOnScrollListener(mListView ));
			mListView.setAdapter(adapter);
		} else {
			adapter.notifyFriendSetChanged(friendList);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (null != adapter) {
			AfFriendInfo friend = adapter.getItem(position);
			Intent action = new Intent(this, ProfileActivity.class);
			action.putExtra(JsonConstant.KEY_PROFILE,
					(Serializable) AfFriendInfo.friendToProfile(friend));
			// 请求新的profile资料
			action.putExtra(JsonConstant.KEY_FLAG, true);
			action.putExtra(JsonConstant.KEY_AFID, friend.afId);
			startActivity(action);
		}
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result,Object user_data) {
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;

		default:
			break;
		}
	}
	
	/**
	 * looper thread
	 * 
	 * @author heguiming 2013-10-23
	 * 
	 */
	class LooperThread extends Thread {
		
		private static final int SELECT_FRIEND = 7000;

		/**
		 * looper handler
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
			looperHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
					case SELECT_FRIEND:
						if (null != listArray && listArray.length != 0) {
							List<String> list = Arrays.asList(listArray);
							List<AfFriendInfo> tmp = new ArrayList<AfFriendInfo>();
							List<AfFriendInfo> friends = CacheManager.getInstance().getFriends_list(Consts.AFMOBI_FRIEND_TYPE_FF);
							for (int i = 0; i < friends.size(); i++) {
								AfFriendInfo afFriendInfo = friends.get(i);
								if (list.contains(afFriendInfo.afId)) {
									afFriendInfo.isAddFriend = true;
									tmp.add(afFriendInfo);
								}
							}
							mHandler.obtainMessage(FIND_OK, tmp).sendToTarget();
						}
						break;
					default:
						break;
					}
				}
			};
			mHandler.sendEmptyMessage(INIT_OK);
			Looper.loop();
		}
	}

}
