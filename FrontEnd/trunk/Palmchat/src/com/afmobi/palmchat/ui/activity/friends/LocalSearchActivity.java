package com.afmobi.palmchat.ui.activity.friends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.eventbusmodel.ShareBroadcastRefreshChatting;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.friends.adapter.LocalSearchAdapter;
import com.afmobi.palmchat.ui.activity.friends.model.LocalSearchAdapterItem;
import com.afmobi.palmchat.ui.activity.invitefriends.Query;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.afmobi.palmchat.ui.activity.people.PeopleYouMayKnowActivity;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
//import com.afmobi.palmchat.ui.activity.social.SearchByInfoFragmentActivity;
import com.afmobi.palmchat.ui.activity.social.ShakeFragmentActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfAttachPAMsgInfo;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class LocalSearchActivity extends BaseActivity implements Query.OnQueryComplete, OnClickListener, AfHttpResultListener {

	private static final String TAG = LocalSearchActivity.class.getCanonicalName();
	private LooperThread looperThread;
	private EditText mEditText;
	private ListView mListView;
	private LocalSearchAdapter mAdapter;
	private SearchTextWatcher mSearchTextWatcher;
	private View mNotFoundLayout;
	private View mSearchOnlineLayout;
	
	/**
	 * online search id
	 */
	private TextView mTextView;
	private Dialog dialog;
	private AfPalmchat mAfCorePalmchat;
	
	private int mFromTag;
	private boolean isShareBrdToFriend;
	private boolean isShareTag = false;
	private String mShareId;
	private String mTagName;
	private String mTagUrl;
	private String mContent;
	private String senderName;
	private String senderHeaderUrl;
	private int mShareTagPostNum;
	/**
	 * 发送广播的用户ID
	 */
	public String mBroadcastAfid;
	/**
	 * 手机通讯录内的联系人
	 */
	private List<AfFriendInfo> mPhoneFriend = new ArrayList<AfFriendInfo>();
	private boolean mWaitPhoneQuery, mWaitSimQuery;
	private Query queryHandler;
	public AfResponseComm.AfChapterInfo afChapterInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		looperThread = new LooperThread();
		looperThread.setName(TAG);
		looperThread.start();
	}
	
	 
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		isShareBrdToFriend = intent.getBooleanExtra("isSendFriend",false);
		isShareTag = intent.getBooleanExtra(JsonConstant.KEY_IS_SHARE_TAG, false);
		mShareId = intent.getStringExtra(JsonConstant.KEY_SHARE_ID);
		mTagName = intent.getStringExtra(JsonConstant.KEY_TAG_NAME);
		mTagUrl = intent.getStringExtra(JsonConstant.KEY_TAG_URL);
		mContent = intent.getStringExtra(JsonConstant.KEY_BROADCAST_CONTENT);
		mShareTagPostNum = intent.getIntExtra(JsonConstant.KEY_SHARE_TAG_POST_NUM, 0);
		mBroadcastAfid = intent.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_AFID);
		senderName = intent.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_NAME);
		senderHeaderUrl = intent.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_HEADER_URL);
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			afChapterInfo = (AfResponseComm.AfChapterInfo) bundle.getSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO);
		}
		setContentView(R.layout.activity_local_search);
		mAfCorePalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
		mListView = (ListView)findViewById(R.id.listview);
		mEditText = (EditText)findViewById(R.id.search_et);
		mTextView = (TextView)findViewById(R.id.text_online_search_id);
		mNotFoundLayout = findViewById(R.id.not_fround_layout);
		mAdapter = new LocalSearchAdapter(this,isShareBrdToFriend);
		mListView.setAdapter(mAdapter);
		mSearchTextWatcher = new SearchTextWatcher();
		mEditText.addTextChangedListener(mSearchTextWatcher);
		
//		findViewById(R.id.r_search).setOnClickListener(this);
		findViewById(R.id.r_shake).setOnClickListener(this);
		findViewById(R.id.r_people_you_may_know).setOnClickListener(this);
		findViewById(R.id.back_button).setOnClickListener(this);
		
		mSearchOnlineLayout = findViewById(R.id.online_search_layout);
		mSearchOnlineLayout.setOnClickListener(this);
		
		mFromTag = getIntent().getIntExtra("tag", 0);
		if(!isShareBrdToFriend){
			getContacts();
		}
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				CommonUtils.closeSoftKeyBoard(mEditText);
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	
	private class SearchTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			//huangjie 转义正则表达式关键字
			String text = s.toString().replace("*", "\\*").replace("?", "\\?").replace("(","\\(").replace(")", "\\)")
					.replace("[", "\\[").replace("]", "\\]").replace("^", "\\^").replace("\\","\\")
					.replace("$ ", "\\$").replace("+", "\\+").replace(".", "\\.^").replace("{","\\{").replace("}","\\}");
			if (text == null || text.length() == 0) {
				mAdapter.clearList();
				mAdapter.notifyDataSetChanged();
				
			} else {
				
				mAdapter.setSearchText(text);
			
				if (null != looperThread) {
				Handler handler = looperThread.handler;
				if (null != handler) {
					
					handler.obtainMessage(LooperThread.SEARCH_FRIENDS, text).sendToTarget();
				}
			}
				
//				if can search online
				if (isNumeric(text)) {
					if(!isShareBrdToFriend) {
						mSearchOnlineLayout.setVisibility(View.VISIBLE);
						mTextView.setText(text);
					}
				} else {
					mSearchOnlineLayout.setVisibility(View.GONE);
				}
				
		}
			
		}
		
	}
	
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LooperThread.SEARCH_FRIENDS:
				
				ArrayList<LocalSearchAdapterItem> list = (ArrayList<LocalSearchAdapterItem>)msg.obj;
				if (list.size() > 0) {
					mListView.setVisibility(View.VISIBLE);
					mNotFoundLayout.setVisibility(View.GONE);
					mAdapter.setList((ArrayList<LocalSearchAdapterItem>)msg.obj);
					mAdapter.notifyDataSetChanged();
				} else {
					mListView.setVisibility(View.GONE);
					if(!isShareBrdToFriend) {
						mNotFoundLayout.setVisibility(View.VISIBLE);
					}
				}
				break;
				case MessagesUtils.MSG_SET_STATUS:
					PalmchatLogUtils.println("---www : MSG_SET_STATUS");
//					sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
					EventBus.getDefault().post(new RefreshChatsListEvent());
					break;
			}
			
		}
		
	};
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onPageStart(TAG);
//		MobclickAgent.onResume(this);
	} 
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		 MobclickAgent.onPageEnd(TAG);  
//		 MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		looperThread.looper.quit();
	}

	private void getContacts(){
		queryHandler = new Query(context.getContentResolver(), context);
		queryHandler.setQueryComplete(this);
		queryHandler.query();
		mWaitPhoneQuery = true;
		mWaitSimQuery = true;
	}

	@Override
	public void onComplete(int token, Cursor c) {
		// TODO Auto-generated method stub
		switch (token) {
			case Query.QUERY_PHONE_TOKEN:
				queryHandler.setContacts(context, c,token);
				mWaitPhoneQuery = false;
				filterContactRequest();
				break;
			case Query.QUERY_SIM_TOKEN:
				mWaitSimQuery = false;
				queryHandler.setContacts(context, c,token);
				filterContactRequest();
				break;

		}

	}

	private void filterContactRequest() {
		if(!mWaitPhoneQuery && !mWaitSimQuery) {
			PalmchatLogUtils.println("filterContactRequest");
			HashMap<String, AfFriendInfo> contactMap = queryHandler.getContacts();
			if (null != looperThread) {
				Handler handler = looperThread.handler;
				if (null != handler) {
					handler.obtainMessage(LooperThread.DISPLAY_PHONE_LIST, contactMap).sendToTarget();
				}
			}
		}
	}
	
	
	class LooperThread extends Thread {

		private static final int SEARCH_FRIENDS = 1001;
		private static final int DISPLAY_PHONE_LIST = 101;
		Handler handler;
		Looper looper;
		
		@Override
		public void run() {
			Looper.prepare();
			looper = Looper.myLooper();
			handler = new Handler() {
				@SuppressLint("DefaultLocale")
				public void handleMessage(Message msg) {
					switch (msg.what) {
					
					case SEARCH_FRIENDS:
						
						String search = (String)msg.obj;
						
						Pattern pattern = null;
						try {
							
							 pattern = Pattern.compile(search.toUpperCase());
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							
						}
						
						if(pattern == null) {
							break;
						}
						
						HashMap<String, String> mapKeys = new HashMap<String, String>();
						
						ArrayList<LocalSearchAdapterItem> listSearchResult = new ArrayList<LocalSearchAdapterItem>();
						
//						from ContactsActivity. not search recent chats record.
						if (mFromTag != ContactsActivity.TAG_CONTACT_ACTIVITY) {
						
						List<MainAfFriendInfo> listRecent = CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).getList();
						
						int listRecentSize = listRecent.size();
						for (int i = 0; i < listRecentSize; i++) {
							
							MainAfFriendInfo info = listRecent.get(i);
							AfMessageInfo message = info.afMsgInfo;
							AfFriendInfo afFriendInfo = info.afFriendInfo;
							AfGrpProfileInfo afGrpInfo = info.afGrpInfo;
							
							if(MessagesUtils.isPrivateMessage(message.type) || MessagesUtils.isSystemMessage(message.type)) {	
								if (null != info && null != afFriendInfo) {
								
//									Matcher matcher = pattern.matcher(afFriendInfo.afId);
//									
//									if (matcher.find()) {
//										if (!mapKeys.containsKey(afFriendInfo.afId)) {
//											mapKeys.put(afFriendInfo.afId, afFriendInfo.afId);
//											AdapterItems item = new AdapterItems();
//									        item.mItemType = LocalSearchAdapter.ITEM_TYPE_RECENT_RECORD;
//									        item.mItemInfoRecents = info;
//									        listSearchResult.add(item);
//										}
//									}
									if(isShareBrdToFriend) { //分享广播给好友时，则不显示公众帐号
										if (!TextUtils.isEmpty(afFriendInfo.afId) && (afFriendInfo.afId.toLowerCase().startsWith("r") || afFriendInfo.afId.toLowerCase().startsWith("g"))) {
											continue;
										}
									}
									String realName = TextUtils.isEmpty(afFriendInfo.alias) ? afFriendInfo.name : afFriendInfo.alias;
									if(!TextUtils.isEmpty(realName)) {
										Matcher matcher2 = pattern.matcher(realName.toUpperCase());


										if (matcher2.find()) {
											if (!mapKeys.containsKey(afFriendInfo.afId)) {
												mapKeys.put(afFriendInfo.afId, afFriendInfo.afId);

												LocalSearchAdapterItem item = new LocalSearchAdapterItem();
												item.mItemType = LocalSearchAdapter.ITEM_TYPE_RECENT_RECORD;
												item.mItemInfoRecents = info;

												listSearchResult.add(item);
											}
										}

//									if (afFriendInfo.user_msisdn != null) {
//										Matcher matcher3 = pattern.matcher(afFriendInfo.user_msisdn);
//										
//										if (matcher3.find()) {
//											if (!mapKeys.containsKey(afFriendInfo.afId)) {
//												mapKeys.put(afFriendInfo.afId, afFriendInfo.afId);
//												AdapterItems item = new AdapterItems();
//							                    item.mItemType = LocalSearchAdapter.ITEM_TYPE_RECENT_RECORD;
//							                    item.mItemInfoRecents = info;
//							                    listSearchResult.add(item);
//											}
//										}
//										
//									}


									}
								}
								
							} else if(MessagesUtils.isGroupChatMessage(message.type)) {
								
								if (null != info && null != afGrpInfo && afGrpInfo.name != null) {
									Matcher matcher = pattern.matcher(afGrpInfo.name.toUpperCase());
									if (matcher.find()) {
										if (!mapKeys.containsKey(afGrpInfo.afid)) {
											mapKeys.put(afGrpInfo.afid, afGrpInfo.afid);
											LocalSearchAdapterItem item = new LocalSearchAdapterItem();
									        item.mItemType = LocalSearchAdapter.ITEM_TYPE_RECENT_RECORD;
									        item.mItemInfoRecents = info;
									        listSearchResult.add(item);
										}
									}
									
									
							
								}
								
							}
							
						}
						
						}
						
						int recentResultSize = listSearchResult.size();
						if (recentResultSize > 0) {
							listSearchResult.get(0).isFirstItem = true;
						}
						
						List<AfFriendInfo> listFriends = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).getList();
						
//						following, follower
						List<AfFriendInfo> list = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).getList();
						List<AfFriendInfo> list2 = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE).getList();
						
						listFriends.addAll(list);
						listFriends.addAll(list2);
						
						int listFriendsSize = listFriends.size();
						for (int i = 0; i < listFriendsSize; i++) {
							
							AfFriendInfo afFriendInfo = listFriends.get(i);

							if (null != afFriendInfo) {
								if(TextUtils.isEmpty(afFriendInfo.name))
									continue;
								if (afFriendInfo.afId != null) {
									if(isShareBrdToFriend) { //分享广播给好友时，则不显示公众帐号
										if (afFriendInfo.afId.toLowerCase().startsWith("r") || afFriendInfo.afId.toLowerCase().startsWith("g")) {
											continue;
										}
									}
								Matcher matcher = pattern.matcher(afFriendInfo.afId);
								
								if (matcher.find()) {
									if (!mapKeys.containsKey(afFriendInfo.afId)) {
										mapKeys.put(afFriendInfo.afId, afFriendInfo.afId);
										LocalSearchAdapterItem item = new LocalSearchAdapterItem();
								        item.mItemType = LocalSearchAdapter.ITEM_TYPE_FRIENDS;
								        item.mItemInfoFriends = afFriendInfo;
								        listSearchResult.add(item);
									}
								}
								}
								
								String realName = afFriendInfo.alias != null ? afFriendInfo.alias : afFriendInfo.name;
								
								if (realName != null) {
									
								Matcher matcher2 = pattern.matcher(realName.toUpperCase());
								
								
								if (matcher2.find()) {
									if (!mapKeys.containsKey(afFriendInfo.afId)) {
										mapKeys.put(afFriendInfo.afId, afFriendInfo.afId);
										LocalSearchAdapterItem item = new LocalSearchAdapterItem();
								        item.mItemType = LocalSearchAdapter.ITEM_TYPE_FRIENDS;
								        item.mItemInfoFriends = afFriendInfo;
								        listSearchResult.add(item);
									}
								}
								}
								
								
								if (afFriendInfo.user_msisdn != null) {
									Matcher matcher3 = pattern.matcher(afFriendInfo.user_msisdn);
									
									if (matcher3.find()) {
										if (!mapKeys.containsKey(afFriendInfo.afId)) {
											mapKeys.put(afFriendInfo.afId, afFriendInfo.afId);
											LocalSearchAdapterItem item = new LocalSearchAdapterItem();
									        item.mItemType = LocalSearchAdapter.ITEM_TYPE_FRIENDS;
									        item.mItemInfoFriends = afFriendInfo;
									        listSearchResult.add(item);
										}
									}
									
								}
								if (afFriendInfo.phone != null) {
									Matcher matcher3 = pattern.matcher(afFriendInfo.phone);

									if (matcher3.find()) {
										if (!mapKeys.containsKey(afFriendInfo.afId)) {
											mapKeys.put(afFriendInfo.afId, afFriendInfo.afId);
											LocalSearchAdapterItem item = new LocalSearchAdapterItem();
											item.mItemType = LocalSearchAdapter.ITEM_TYPE_FRIENDS;
											item.mItemInfoFriends = afFriendInfo;
											listSearchResult.add(item);
										}
									}

								}
								
							}
							
							
						}
						//手机通讯录未加好友
						if(mPhoneFriend != null && mPhoneFriend.size() > 0){
							int listPhoneFriendsSize = mPhoneFriend.size();
							for (int i = 0; i < listPhoneFriendsSize; i++) {

								AfFriendInfo afFriendInfo = mPhoneFriend.get(i);
                                String friendKey = afFriendInfo.name + afFriendInfo.user_msisdn;
								if (null != afFriendInfo) {
									String realName = afFriendInfo.name;

									if (realName != null) {

										Matcher matcher2 = pattern.matcher(realName.toUpperCase());
										if (matcher2.find()) {
											if (!mapKeys.containsKey(friendKey)) {
												mapKeys.put(friendKey, afFriendInfo.user_msisdn);
												LocalSearchAdapterItem item = new LocalSearchAdapterItem();
												item.mItemType = LocalSearchAdapter.ITEM_TYPE_FRIENDS;
												item.mItemInfoFriends = afFriendInfo;
												listSearchResult.add(item);
											}
										}
									}


									if (afFriendInfo.user_msisdn != null) {
										Matcher matcher3 = pattern.matcher(afFriendInfo.user_msisdn);

										if (matcher3.find()) {
											if (!mapKeys.containsKey(friendKey)) {
												mapKeys.put(friendKey, afFriendInfo.user_msisdn);
												LocalSearchAdapterItem item = new LocalSearchAdapterItem();
												item.mItemType = LocalSearchAdapter.ITEM_TYPE_FRIENDS;
												item.mItemInfoFriends = afFriendInfo;
												listSearchResult.add(item);
											}
										}

									}


								}
							}
						}
						
						
						int totalSize = listSearchResult.size();
						int friendsSize = totalSize - recentResultSize;
						
						if (friendsSize > 0) {
							listSearchResult.get(recentResultSize).isFirstItem = true;
						}
						
						mHandler.obtainMessage(SEARCH_FRIENDS, listSearchResult).sendToTarget();
						
						
						mapKeys.clear();
						
						break;
					
						case DISPLAY_PHONE_LIST:
							HashMap<String, AfFriendInfo> phoneMap = (HashMap<String, AfFriendInfo>) msg.obj;
							String myMsisdn = CacheManager.getInstance().getMyProfile().phone;
							if (phoneMap.containsKey(myMsisdn)) {
								phoneMap.remove(myMsisdn);
								PalmchatLogUtils.println("invite friend remove msisdn:" + myMsisdn);
							}
							Set<Map.Entry<String, AfFriendInfo>> tmpSet = phoneMap.entrySet();
							Iterator<Map.Entry<String, AfFriendInfo>> iterator = tmpSet.iterator();
							AfFriendInfo afFriendInfo = null;
							while (iterator.hasNext()) {
								Map.Entry<String, AfFriendInfo> entry = iterator.next();
								afFriendInfo = entry.getValue();
								if(isExitsPhone(afFriendInfo))
									continue;
								mPhoneFriend.add(afFriendInfo);
							}
							break;
					}
				}
				
			};
			
			Looper.loop();
			
		}
	}

	/** 防止重复加载手机通讯录
	 * @param afFriendInfo
	 * @return
	 */
	private boolean isExitsPhone(AfFriendInfo afFriendInfo){
		for (AfFriendInfo friendInfo : mPhoneFriend){
			if((!TextUtils.isEmpty(friendInfo.user_msisdn) && friendInfo.user_msisdn.equals(afFriendInfo.user_msisdn))
					&& (!TextUtils.isEmpty(friendInfo.name) && friendInfo.name.equals(afFriendInfo.name))){
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
	/*	case R.id.r_search:
			startActivity(new Intent(this, SearchByInfoFragmentActivity.class));
			break;*/

		case R.id.r_shake:
			
			startActivity(new Intent(this, ShakeFragmentActivity.class));
			break;

		case R.id.r_people_you_may_know:
			startActivity(new Intent(this, PeopleYouMayKnowActivity.class));
			break;
			
		case R.id.back_button:
			/*modify by zhh 2015-04-09*/
			InputMethodManager inputManager = (InputMethodManager) getSystemService(LocalSearchActivity.this.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
			finish();
			break;
			
//			search by id online
		case R.id.online_search_layout:
			
			// gtf 2014-11-16
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ID_T_PF);
//			MobclickAgent.onEvent(context, ReadyConfigXML.ID_T_PF);
			
			String str = mEditText.getText().toString();
			showProgressDialog();
			String afid[] = new String[1];
			afid[0] = str;

			mAfCorePalmchat.AfHttpGetInfo(afid, Consts.REQ_GET_INFO_EX, null,
					0, LocalSearchActivity.this);
			
			break;

		default:
			break;
		}
		
	}
	
	
	
	private void showProgressDialog() {
		if (dialog == null) {
			dialog = new Dialog(this,R.style.Theme_LargeDialog);
			dialog.setOnKeyListener(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_loading);
			((TextView) dialog.findViewById(R.id.textview_tips)).setText(R.string.searching);
		
		}
			dialog.show();
	}

	private void dismissDialog() {
			if (null != dialog && dialog.isShowing()){
				try {
					dialog.cancel();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
			}
		}
			
	}
	
	/**
	 * if search afid on net
	 * 
	 * @param str
	 * @return
	 */
	private boolean isNumeric(String str) {
		
		if (TextUtils.isEmpty(str)) {
			return false;
		}

		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}

		if (str.length() >= 6) {
			return true;
		} else {
			return false;
		}

	}
	
	
	
	private void toProfile(AfFriendInfo afFriendInfo) {
		Intent intent = new Intent(this, ProfileActivity.class);
		AfProfileInfo info = AfFriendInfo.friendToProfile(afFriendInfo);
		intent.putExtra(JsonConstant.KEY_PROFILE, info);
		intent.putExtra(JsonConstant.KEY_FLAG, true);
		intent.putExtra(JsonConstant.KEY_AFID, info.afId);
		intent.putExtra(JsonConstant.KEY_USER_MSISDN, info.user_msisdn);
		startActivity(intent);
	}


	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		// TODO Auto-generated method stub
		dismissDialog();
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			// search afid on net
			case Consts.REQ_GET_INFO_EX: 
				if (result != null) {
					
					AfFriendInfo afFriendInfo = (AfFriendInfo) result;
					if (CacheManager.getInstance().getMyProfile().afId.equals(afFriendInfo.afId)) {
						Bundle bundle = new Bundle();
						bundle.putString(JsonConstant.KEY_AFID, afFriendInfo.afId);
						Intent intent = new Intent(this, MyProfileActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					} else {
						toProfile(afFriendInfo);
					}
					
				} else {
					ToastManager.getInstance().show(context, R.string.afid_not_found);
				}
				
				break;
				case Consts.REQ_BC_SHARE_BROADCAST:
				case Consts.REQ_BC_SHARE_TAG:
					AfMessageInfo messageInfo = null;
					if(user_data != null && user_data instanceof AfMessageInfo){
						messageInfo = (AfMessageInfo)user_data;
					}
					//向缓存中插入一条数据
					MessagesUtils.insertMsg(messageInfo, true, true);
					Intent intent = new Intent();
					dismissAllDialog();
					AfResponseComm.AfTagShareTagOrBCResp afResponseComm = (AfResponseComm.AfTagShareTagOrBCResp) result;
					if(messageInfo != null) {
						if(afResponseComm != null){
							AfAttachPAMsgInfo afAttachPAMsgInfo = (AfAttachPAMsgInfo) messageInfo.attach;
							if(afAttachPAMsgInfo != null){
								afAttachPAMsgInfo.postnum = afResponseComm.post_number;
								MessagesUtils.updateShareTagMsgPostNumber(messageInfo,afAttachPAMsgInfo._id);
							}
						}
						new StatusThead(messageInfo._id, AfMessageInfo.MESSAGE_SENT, messageInfo.fid,messageInfo.type).start();
					}
					intent.putExtra("isSucess", true);
					setResult(RESULT_OK, intent);
					finish();
					break;
				
			}
	} else {
		
		switch (flag) {
		// search afid on net
		case Consts.REQ_GET_INFO_EX: 
			
			if (code == Consts.REQ_CODE_UNNETWORK) {
				ToastManager.getInstance().show(context, R.string.network_unavailable);	
			} else {
				ToastManager.getInstance().show(context, R.string.afid_not_found);				
			}
			
			break;
			case Consts.REQ_BC_SHARE_BROADCAST:
			case Consts.REQ_BC_SHARE_TAG:
				AfMessageInfo messageInfo = null;
				if(user_data != null && user_data instanceof AfMessageInfo){
					messageInfo = (AfMessageInfo)user_data;
				}
				Intent intent = new Intent();
				dismissAllDialog();
				intent.putExtra(JsonConstant.KEY_SHARE_OR_SEND_FRIENDS_ERROR_CODE, code);
				if(code == Consts.REQ_CODE_202) {
					MessagesUtils.deleteMessageFromDb(this, messageInfo);
					intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL, getResources().getString(R.string.the_broadcast_doesnt_exist));
					if(afChapterInfo != null){
						sendUpdate_delect_BroadcastList(afChapterInfo);
					}
				}
				else if (code == Consts.REQ_CODE_UNNETWORK || code == Consts.REQ_CODE_CANCEL) {
					//向缓存中插入一条数据
					MessagesUtils.insertMsg(messageInfo, true, true);
					intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL, getResources().getString(R.string.network_unavailable));
				}
				else if(code == Consts.REQ_CODE_TAG_ILLEGAL){
					//intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL, false);
				}
				if(messageInfo != null) {
					new StatusThead(messageInfo._id, AfMessageInfo.MESSAGE_UNSENT, messageInfo.fid,messageInfo.type).start();
				}
				intent.putExtra("isSucess", false);
				setResult(RESULT_OK, intent);
				finish();
				break;
		}
		
	}
	}

	public void sendBroadcastToFriend(final boolean isPrivate, Object object) {
		AfFriendInfo friendInfo = null;
		AfGrpProfileInfo grpProfileInfo = null;
		AppDialog appDialog = new AppDialog(this);
		String showStr = null;
		if (isPrivate) {
			friendInfo = (AfFriendInfo) object;
			showStr = CommonUtils.replace("XX", friendInfo.name + "", getResources().getString(R.string.send_to_friend_toast));
		} else {
			grpProfileInfo = (AfGrpProfileInfo) object;
			showStr = CommonUtils.replace("XX", grpProfileInfo.name + "", getResources().getString(R.string.send_to_friend_toast));
		}
		final AfFriendInfo friendInfoTemp = friendInfo;
		final AfGrpProfileInfo grpProfileInfoTemp = grpProfileInfo;
		appDialog.createConfirmDialog(this, showStr, new AppDialog.OnConfirmButtonDialogListener() {
			@Override
			public void onRightButtonClick() {
				AfAttachPAMsgInfo afAttachPAMsgInfo = new AfAttachPAMsgInfo();
				AfMessageInfo messageInfo = null;
				String strId = null;
				afAttachPAMsgInfo.mid = mShareId;
				afAttachPAMsgInfo.afid = mBroadcastAfid;
				if (isPrivate) {
					strId = friendInfoTemp.afId;
					// insertBroadcastTextToDb(isPrivate,friendInfoTemp);
				} else {
					strId = grpProfileInfoTemp.afid;
					// insertBroadcastTextToDb(isPrivate,grpProfileInfoTemp);
				}
				//mTagUrl = "http://www.qq.com";
				if (!TextUtils.isEmpty(mTagUrl)) {
					if (!mTagUrl.startsWith(JsonConstant.HTTP_HEAD)) {//拼接url的
						mTagUrl = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerInfo()[4] + mTagUrl;
					}
					afAttachPAMsgInfo.imgurl = mTagUrl;
				} else {
					afAttachPAMsgInfo.imgurl = "";
				}
				showProgressDialog(R.string.please_wait);
				if (isShareTag) {
					afAttachPAMsgInfo.postnum = mShareTagPostNum;
					afAttachPAMsgInfo.content = mTagName;
					afAttachPAMsgInfo.msgtype = AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG;
					messageInfo = getMessageInfo(afAttachPAMsgInfo, strId, isPrivate);
					mAfCorePalmchat.AfHttpAfBCShareTags(strId, System.currentTimeMillis(), mTagName, mTagUrl, messageInfo, LocalSearchActivity.this);
				} else {
					afAttachPAMsgInfo.content = mContent;
					afAttachPAMsgInfo.msgtype = AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG;
					afAttachPAMsgInfo.local_img_path = senderHeaderUrl;
					afAttachPAMsgInfo.name = senderName;
					messageInfo = getMessageInfo(afAttachPAMsgInfo, strId, isPrivate);
					mAfCorePalmchat.AfHttpAfBCShareBroadCast(strId, System.currentTimeMillis(), mShareId, messageInfo, LocalSearchActivity.this);
				}

			}

			@Override
			public void onLeftButtonClick() {
				Intent intent = new Intent();
				intent.putExtra("isCancel", true);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		appDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				Intent intent = new Intent();
				intent.putExtra("isCancel", true);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		appDialog.show();
	}

	public AfMessageInfo getMessageInfo(AfAttachPAMsgInfo afAttachPAMsgInfo,String mFriendAfid,boolean isPrivate){
		AfMessageInfo messageInfo = new AfMessageInfo();//新增
		afAttachPAMsgInfo._id = mAfCorePalmchat.AfDbAttachPAMsgInsert(afAttachPAMsgInfo);

		messageInfo.client_time = System.currentTimeMillis();
//						messageInfo.fromAfId = CacheManager.getInstance().getMyProfile().afId;
		messageInfo.status = AfMessageInfo.MESSAGE_SENTING;
		messageInfo.attach = afAttachPAMsgInfo;
		messageInfo.attach_id = afAttachPAMsgInfo._id;
		if(afAttachPAMsgInfo.msgtype == AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG) {
			messageInfo.msg = afAttachPAMsgInfo.content;
		}
		if(isPrivate) {
			messageInfo.toAfId = mFriendAfid;
			messageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | afAttachPAMsgInfo.msgtype;
			messageInfo._id = mAfCorePalmchat.AfDbMsgInsert(messageInfo);
		}
		else{
			messageInfo.fromAfId = mFriendAfid;
			messageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_GRP | afAttachPAMsgInfo.msgtype;
			messageInfo._id = mAfCorePalmchat.AfDbGrpMsgInsert(messageInfo);
		}
		messageInfo.action = MessagesUtils.ACTION_INSERT;
		//handler.obtainMessage(handler_MsgType, messageInfo).sendToTarget();
		new StatusThead(messageInfo._id, AfMessageInfo.MESSAGE_SENTING, messageInfo.fid,messageInfo.type).start();
		return messageInfo;
	}

	/**
	 * 更新消息发送状态 线程类
	 */
	public class StatusThead extends Thread {
		private int msgId;
		private int status;
		private String fid;
		private int msgType;
		public StatusThead(int msgId, int status, String fid,int type) {
			this.msgId = msgId;
			this.status = status;
			this.fid = fid;
			msgType = type;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int _id = DefaultValueConstant.LENGTH_0;
			AfMessageInfo msg = null;
//			update recent chats msg status
			if(MessagesUtils.isPrivateMessage(msgType)) {
				if (fid != DefaultValueConstant.MSG_INVALID_FID) {
					_id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS_AND_FID);

				} else {
					_id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS);
				}
				msg = mAfCorePalmchat.AfDbMsgGet(msgId);
			}
			else if(MessagesUtils.isGroupChatMessage(msgType)){
				if (fid != DefaultValueConstant.MSG_INVALID_FID) {
					_id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS_AND_FID);

				} else {
					_id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS);
				}
				msg = mAfCorePalmchat.AfDbGrpMsgGet(msgId);
			}
			if (msg == null) {
				PalmchatLogUtils.println("Chatting AfDbMsgGet msg:" + msg);
				return;
			}
			PalmchatLogUtils.println("---www : StatusThead isLast msg " + msgId + " msg status: " + status);
			MessagesUtils.setRecentMsgStatus(msg, status);
			mHandler.obtainMessage(MessagesUtils.MSG_SET_STATUS).sendToTarget();
			PalmchatLogUtils.println("update status msg_id " + _id);
			EventBus.getDefault().post(new ShareBroadcastRefreshChatting());
		}
	}
	/**
	 * 发广播通知 各个页面这个广播被删除了
	 *
	 * @param afChapterInfo
	 */
	private void sendUpdate_delect_BroadcastList(AfResponseComm.AfChapterInfo afChapterInfo) {
		afChapterInfo.eventBus_action = Constants.UPDATE_DELECT_BROADCAST;
		EventBus.getDefault().post(afChapterInfo);
	}
}
