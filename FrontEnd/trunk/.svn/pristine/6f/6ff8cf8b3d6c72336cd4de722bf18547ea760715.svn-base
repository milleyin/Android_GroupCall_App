package com.afmobi.palmchat.ui.activity.people;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.customview.RefreshableView;
import com.afmobi.palmchat.ui.customview.RefreshableView.RefreshListener;
import com.afmobi.palmchat.util.ToastManager;
//import com.afmobi.palmchat.util.image.ImageOnScrollListener;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.core.AfFriendInfo;
import com.core.AfMutualFreindInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.param.AfSearchUserParam;
//import com.umeng.analytics.MobclickAgent;

/**
 * poeple you may know list page
 * @author heguiming  2013-11-14
 *
 */
@SuppressLint("ValidFragment")
public class PeopleYouMayKnowOneFragment extends BaseFragment implements
		RefreshListener, OnClickListener, AfHttpResultListener, OnItemClickListener {
	
	private static final String TAG = PeopleYouMayKnowOneFragment.class.getCanonicalName();
	
	// update Mutual data
	private static final int UPDATE_AF_MUTUAL = 8000;
	// update friend data
	private static final int UPDATE_AF_FRIEND = 8001;
	
	/**
	 * Looper线程
	 */
	private LooperThread looperThread;

	private RefreshableView mRefrshView;
	private ListView mListView;
	private LinearLayout mNoPeopleLayout;
	private PeopleYouMayKnowOneAdapter adapter;
//	private ListViewAddOn mListViewAddOn = new ListViewAddOn();
	
	/**
	 * mHandler UI
	 */
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_AF_MUTUAL:
				List<AfMutualFreindInfo> mutualList = (List<AfMutualFreindInfo>) msg.obj;
				if (null != mutualList && !mutualList.isEmpty()) {
					notificationMutualListView(mutualList);
					mRefrshView.finishRefresh();
				} else {
					showNoPeopleLayout();
				}
				break;
			case UPDATE_AF_FRIEND:
				List<AfFriendInfo> friendList = (List<AfFriendInfo>) msg.obj;
				if (null != friendList && !friendList.isEmpty()) {
					notificationFriendslListView(friendList);
					mRefrshView.finishRefresh();
				} else {
					showNoPeopleLayout();
				}
				break;
			}
		};
	};
	
	
	public PeopleYouMayKnowOneFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		// heguiming 2013-12-04
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_MAY_NUM);
//		MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_MAY_NUM);
		
		setContentView(R.layout.activity_people_you_may_know_one);
		initViews();
		initData();
		
		return mMainView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		 MobclickAgent.onPageStart("PeopleYouMayKnowOneFragment");
	}

	@Override
	public void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd("PeopleYouMayKnowOneFragment"); 
	}
	
	private void initData() {
		mRefrshView.showRefresh();
		loadMutualFriends();
		loadCacheData();
		
		looperThread = new LooperThread();
		looperThread.setName(TAG);
		looperThread.start();
	}
	
	/**
	 * Load Cache Data
	 */
	private void loadCacheData() {
		List<AfMutualFreindInfo> peopleYouMayKnowMutualList = CacheManager.getInstance().getPeopleYouMayKnowMutualList();
		if (null != peopleYouMayKnowMutualList) {
			notificationMutualListView(peopleYouMayKnowMutualList);
		} else {
			List<AfFriendInfo> peopleYouMayKnowFriendList = CacheManager.getInstance().getPeopleYouMayKnowFriendList();
			notificationFriendslListView(peopleYouMayKnowFriendList);
		}
	}

	/**
	 * load Mutual Friends
	 */
	private void loadMutualFriends() {
		mAfCorePalmchat.AfHttpMutualFriends(null, this);
	}
	
	private int pageid = -1;
	
	/**
	 * load Region Friends
	 */
	private void loadRegionFriends() {
		String city = CacheManager.getInstance().getMyProfile().city;
		if (null != city) {
			pageid++;
			AfSearchUserParam param = new AfSearchUserParam();
			param.city = city;
			param.pageid = pageid;
			param.sex = Consts.AFMOBI_SEX_FEMALE_AND_MALE;
			mAfCorePalmchat.AfHttpSearchUser(param, null, this);
		} else {
			mRefrshView.finishRefresh();
			showNoPeopleLayout();
		}
	}

	private void initViews() {
		mRefrshView = (RefreshableView) findViewById(R.id.refresh_list);
		mRefrshView.setRefreshListener(this);
		TextView mTitleTxt = (TextView) findViewById(R.id.title_text);
		mTitleTxt.setText(getFragString(R.string.people_you_may_know));
		View backView = findViewById(R.id.back_button);
//		backView.setBackgroundResource(R.drawable.t_home);
		backView.setOnClickListener(this);
		
		 
		
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		mNoPeopleLayout = (LinearLayout) findViewById(R.id.no_people_layout);
		findViewById(R.id.try_again_btn).setOnClickListener(this);
		
	}
	
	/**
	 * Mutual adapter
	 * @param list
	 */
	private void notificationMutualListView(List<AfMutualFreindInfo> list) {
		mListView.setVisibility(View.VISIBLE);
		mNoPeopleLayout.setVisibility(View.GONE);
		if (null != list) {
			CacheManager.getInstance().setPeopleYouMayKnowMutualList(list);
			adapter = new PeopleYouMayKnowOneAdapter(list, fragmentActivity );
//			mListView.setOnScrollListener(new ImageOnScrollListener(mListView ));
			mListView.setAdapter(adapter);
		}
	}
	
	/**
	 * Friends adapter
	 * @param list
	 */
	private void notificationFriendslListView(List<AfFriendInfo> list) {
		mListView.setVisibility(View.VISIBLE);
		mNoPeopleLayout.setVisibility(View.GONE);
		if (null != list) {
			CacheManager.getInstance().setPeopleYouMayKnowFriendList(list);
			adapter = new PeopleYouMayKnowOneAdapter(fragmentActivity, list );
//			mListView.setOnScrollListener(new ImageOnScrollListener(mListView ));
			mListView.setAdapter(adapter);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(looperThread != null && looperThread.looper != null){
			looperThread.looper.quit();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
//			if(menu != null){
//				menu.showMenu();
//			}
			if(null != getActivity()){
				getActivity().finish();
			}
			break;
		case R.id.try_again_btn:
			mNoPeopleLayout.setVisibility(View.GONE);
			mRefrshView.showRefresh();
			loadMutualFriends();
			break;
		 
		default:
			break;
		}
	}
	
	/**
	 * 单线程队列操作
	 * 
	 * @author 何桂明 2013-10-23
	 * 
	 */
	class LooperThread extends Thread {
		
		// 更新缓存数据
		private static final int SELECT_FRIEND = 7000;

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
					case SELECT_FRIEND:
						Object obj = msg.obj;
						if (null != obj) {
							if (obj instanceof AfMutualFreindInfo[]) {
								AfMutualFreindInfo[] afMutualAry = (AfMutualFreindInfo[]) obj;
								List<AfMutualFreindInfo> list = new ArrayList<AfMutualFreindInfo>();
								
								int size = afMutualAry.length;
								for (int i = 0; i < size; i++) {
									AfMutualFreindInfo mutual = afMutualAry[i];
									// 搜索所有好友信息
									AfFriendInfo mFriendInfo = CacheManager.getInstance().findAfFriendInfoByAfId(mutual.afid);
									if (null != mFriendInfo) {
										mutual.isAddFriend = true;
									} else {
										list.add(mutual);
									}
								}
								
								CacheManager.getInstance().setPeopleYouMayKnowMutualList(list);
								
								// sort
								Collections.sort(list, new Comparator<AfMutualFreindInfo>() {
									public int compare(AfMutualFreindInfo lhs, AfMutualFreindInfo rhs) {
										int s1 = lhs.comm_frds == null ? 0 : lhs.comm_frds.size();
										int s2 = rhs.comm_frds == null ? 0 : rhs.comm_frds.size();
										return s2 - s1;
									}
								});
								Message friendMsg = new Message();
								friendMsg.what = UPDATE_AF_MUTUAL;
								friendMsg.obj = list;
								
								mHandler.sendMessage(friendMsg);
								
							} else if (obj instanceof AfFriendInfo[]) {
								AfFriendInfo[] afFriendAry = (AfFriendInfo[]) obj;
								
								List<AfFriendInfo> list = new ArrayList<AfFriendInfo>();
								
								int size = afFriendAry.length;
								for (int i = 0; i < size; i++) {
									AfFriendInfo friend = afFriendAry[i];
									// 搜索所有好友信息
									AfFriendInfo mFriendInfo = CacheManager.getInstance().findAfFriendInfoByAfId(friend.afId);
									if (null != mFriendInfo) {
										friend.isAddFriend = true;
									} else {
										list.add(friend);
									}
								}
								CacheManager.getInstance().setPeopleYouMayKnowFriendList(list);
								
								Message friendMsg = new Message();
								friendMsg.what = UPDATE_AF_FRIEND;
								friendMsg.obj = list;
								
								mHandler.sendMessage(friendMsg);
							}
							mRefrshView.finishRefresh();
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
	public void onRefresh(RefreshableView view) {
		PalmchatLogUtils.e(TAG, "----onRefresh----");
		loadMutualFriends();
	}

	/**
	 * 没有可能认识的人
	 */
	private void showNoPeopleLayout() {
		CacheManager.getInstance().setPeopleYouMayKnowFriendList(null);
		CacheManager.getInstance().setPeopleYouMayKnowMutualList(null);
		
		mListView.setVisibility(View.GONE);
		mNoPeopleLayout.setVisibility(View.VISIBLE);
		mRefrshView.finishRefresh();
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result,Object user_data) {
		PalmchatLogUtils.e("AfOnResult", "----Flag:" + flag + "----Code:" + code + result);
		if (Consts.REQ_CODE_SUCCESS == code) {
			PalmchatLogUtils.d(TAG, "OnResult:"+flag);
			switch (flag) {
			case Consts.REQ_MUTUAL_FRIEND:
				PalmchatLogUtils.i("AfOnResult", "----MUTUAL_FRIENDS----");
				if (null != result) {
					AfMutualFreindInfo info[] = (AfMutualFreindInfo[]) result;
					if (info != null && info.length > 0) {
						Handler handler = looperThread.looperHandler;
						if (null != handler) {
							Message msg = new Message();
							msg.what = LooperThread.SELECT_FRIEND;
							msg.obj = info;
							handler.sendMessage(msg);
						}
					} else {
						loadRegionFriends();
					}
				} else {
					loadRegionFriends();
				}
				
				break;
			case Consts.REQ_SEARCH_USER:
				if (null != result) {
					AfFriendInfo info[] = (AfFriendInfo[]) result;
					if (info != null && info.length > 0) {
						Handler handler = looperThread.looperHandler;
						if (null != handler) {
							Message msg = new Message();
							msg.what = LooperThread.SELECT_FRIEND;
							msg.obj = info;
							handler.sendMessage(msg);
						}
					} else {
						showNoPeopleLayout();
					}
				} else {
					showNoPeopleLayout();
				}
				break;
			default:
				break;
			}
		} else {
			if (flag == Consts.REQ_MUTUAL_FRIEND && code != Consts.REQ_CODE_UNNETWORK) {
				loadRegionFriends();
			} else {
				if (null != context && !context.isFinishing() && isAdded()) {
				mRefrshView.finishRefresh();
				mNoPeopleLayout.setVisibility(View.GONE);
				ToastManager.getInstance().showByCode(context, code);
				}
			}
		}
		
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PeopleYouMayKnowOneAdapter adapter = (PeopleYouMayKnowOneAdapter) parent.getAdapter();
		Object obj = adapter.getItem(position);
		if (obj instanceof AfMutualFreindInfo) {
			AfMutualFreindInfo friend = (AfMutualFreindInfo) obj;
//			int mutualSize = friend.comm_frds == null ? 0 : friend.comm_frds.size();
//			if (mutualSize != 0) {
//				Intent intent = new Intent(context, PeopleYouMayKnowTwoActivity.class);
//				intent.putExtra(JsonConstant.KEY_LIST_STR,
//						friend.comm_frds == null ? new String[] {} : friend.comm_frds.toArray());
//				intent.putExtra(JsonConstant.KEY_NO, friend.comm_frds == null ? 0 : friend.comm_frds.size());
//				startActivity(intent);
//			} else {
//				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.MAY_T_PF);
//				MobclickAgent.onEvent(context, ReadyConfigXML.MAY_T_PF);
				Intent action = new Intent(context, ProfileActivity.class);
				action.putExtra(JsonConstant.KEY_PROFILE, (Serializable) AfMutualFreindInfo.mutualFriendToProfile(friend));
				// 请求新的profile资料
				action.putExtra(JsonConstant.KEY_FLAG, true);
				action.putExtra(JsonConstant.KEY_AFID, friend.afid);
				startActivity(action);
//			}
		} else if (obj instanceof AfFriendInfo) {
//			
			// heguiming 2013-12-04
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.MAY_T_PF);
//			MobclickAgent.onEvent(context, ReadyConfigXML.MAY_T_PF);
			
			AfFriendInfo friend = (AfFriendInfo) obj;
			Intent action = new Intent(context, ProfileActivity.class);
			action.putExtra(JsonConstant.KEY_PROFILE, (Serializable) AfFriendInfo.friendToProfile(friend));
			// 请求新的profile资料
			action.putExtra(JsonConstant.KEY_FLAG, true);
			action.putExtra(JsonConstant.KEY_AFID, friend.afId);
			startActivity(action);
		}
	}

}
