package com.afmobi.palmchat.ui.activity.invitefriends;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.invitefriends.Query.OnQueryComplete;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
//import com.afmobi.palmchat.util.image.ImageOnScrollListener;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobigroup.gphone.R;
import com.core.AfFriendInfo;
import com.core.cache.CacheManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class InviteFriendsFragment extends BaseFragment implements OnQueryComplete {
	
	private static final String TAG = InviteFriendsFragment.class.getCanonicalName();
	
	Intent intent;
	
	private Query queryHandler;
	   
	private EditText mLikeSearch;
	
	private ImageView vImageViewBack,mCancel_btn;
	
	
	InviteFriendAdapter mCursorAdapter;
	ListView exList;
	
	private SearchTextWatcher searchTextWatcher = new SearchTextWatcher();
//	private ListViewAddOn listViewAddOn = new ListViewAddOn();
	
	private LooperThread mLooperThread;

	List<AfFriendInfo> mContactList;

	private boolean mWaitPhoneQuery, mWaitSimQuery;
	
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LooperThread.DISPLAY_PHONE_LIST:
				
				List<AfFriendInfo> tmpList = (List<AfFriendInfo>) msg.obj;
				mContactList = tmpList;
				mCursorAdapter.notifyDataSetChanged(tmpList);
				break;
				
				
			case LooperThread.SEARCH_FRIENDS:
				SearchParams params = (SearchParams)msg.obj;
				mCursorAdapter.notifyDataSetChanged(params.mList);
				break;
				
				
			}
			
		}
		
	};
	
	
	class LooperThread extends Thread {

		private static final int DISPLAY_PHONE_LIST = 101;
		private static final int SEARCH_FRIENDS = 102;
		
		Handler handler;
		Looper looper;
		
		@Override
		public void run() {
			Looper.prepare();
			looper = Looper.myLooper();
			handler = new Handler() {
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case DISPLAY_PHONE_LIST:
						HashMap<String, AfFriendInfo> phoneMap = (HashMap<String, AfFriendInfo>)msg.obj;

						ArrayList<AfFriendInfo> realList = new ArrayList<AfFriendInfo>();
						String myMsisdn = CacheManager.getInstance().getMyProfile().phone;

						
						if(phoneMap.containsKey(myMsisdn)) {
							phoneMap.remove(myMsisdn);
							PalmchatLogUtils.println("invite friend remove msisdn:" + myMsisdn);
						}
							
							Set<Map.Entry<String,AfFriendInfo>> tmpSet = phoneMap.entrySet();
							Iterator<Map.Entry<String,AfFriendInfo>> iterator = tmpSet.iterator();
							
							while(iterator.hasNext()) {
								Map.Entry<String,AfFriendInfo> entry = iterator.next();
								if(isExitsPhone(realList,entry.getValue()))
									continue;
								realList.add(entry.getValue());
							}
							
							Collections.sort(realList, new Comparator<AfFriendInfo>() {
								@Override
								public int compare(AfFriendInfo lhs,
										AfFriendInfo rhs) {
									// TODO Auto-generated method stub
									return lhs.name.compareTo(rhs.name);
								}
							});
							
							mHandler.obtainMessage(DISPLAY_PHONE_LIST, realList).sendToTarget();
						
						break;
						
						
					case SEARCH_FRIENDS:
						SearchParams params = (SearchParams)msg.obj;
						String search = params.mSearch;
						List<AfFriendInfo> list = params.mList;
						ArrayList<AfFriendInfo> results = new ArrayList<AfFriendInfo>();
						
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
						
						AfFriendInfo findInfo = null;
						for (int i = 0; i < list.size(); i++) {
							
							if(list.get(i) == null) {
								break;
							}
							

							String str = list.get(i).name;
							if(str != null) {
								
								Matcher matcher2 = pattern.matcher(str.toUpperCase());
							if (matcher2.find()) {

									results.add(list.get(i));
									
									continue;
							}
							
							}
							
							if(list.get(i).user_msisdn != null) {
								Matcher matcher3 = pattern.matcher((list.get(i).user_msisdn));
								if (matcher3.find()) {
									results.add(list.get(i));
								}
							}
							
						}
						
						params.mList = results;
						mHandler.obtainMessage(SEARCH_FRIENDS, params).sendToTarget();
						
						break;
						
					}
				}
				
			};
			
			Looper.loop();
			
		}
		
	}
	
	public InviteFriendsFragment(){
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mLooperThread = new LooperThread();
		mLooperThread.setName(TAG);
		mLooperThread.start();
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.invitefriends);
		initViews();
		return mMainView;
		
	}	
	
	private void initViews() {
		((TextView)  findViewById(R.id.title_text)).setText(R.string.phonebook);
		
		mLikeSearch = (EditText) findViewById(R.id.search_et);
		vImageViewBack = (ImageView) findViewById(R.id.back_button);
		vImageViewBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (getActivity() != null) {
					getActivity().finish();
				}
			}
		});
		 
		mCancel_btn = (ImageView) findViewById(R.id.cancel_btn);

		exList = (ListView) findViewById(R.id.expandable_listview);
		View view = LayoutInflater.from(context).inflate(R.layout.home_left_group_header, null);
		TextView tvTitle = (TextView)view.findViewById(R.id.g_title);
		TextView tvMsgCnt = (TextView)view.findViewById(R.id.msgcnt);
		RelativeLayout rl1 = (RelativeLayout)view.findViewById(R.id.rl1);
		RelativeLayout rl2 = (RelativeLayout)view.findViewById(R.id.rl2);
		tvMsgCnt.setVisibility(View.GONE);
		tvTitle.setTextColor(Color.WHITE);
		tvTitle.setText(R.string.invite_to_palmchat);
		rl1.setVisibility(View.GONE);
		rl2.setVisibility(View.VISIBLE);

		rl2.setBackgroundResource(R.drawable.list_label);

		exList.addHeaderView(view,null,false);
		mContactList = new ArrayList<AfFriendInfo>();
		mCursorAdapter = new InviteFriendAdapter(getActivity(),mContactList);
//		exList.setOnScrollListener(new ImageOnScrollListener(exList ));
		exList.setAdapter(mCursorAdapter);
		
		mLikeSearch.addTextChangedListener(searchTextWatcher);
		mCancel_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLikeSearch.getText().clear();
			}
		});
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	
		 
		
		try {
			
			getContacts();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		 
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		mLooperThread.looper.quit();
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
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

	private boolean isExitsPhone(List<AfFriendInfo> afFriendInfosList,AfFriendInfo afFriendInfo){
		for (AfFriendInfo friendInfo : afFriendInfosList){
			if((!TextUtils.isEmpty(friendInfo.user_msisdn) && friendInfo.user_msisdn.equals(afFriendInfo.user_msisdn))
					&& (!TextUtils.isEmpty(friendInfo.name) && friendInfo.name.equals(afFriendInfo.name))){
				return true;
			}
		}
		return false;
	}
	
	private void filterContactRequest() {
		
		if(!mWaitPhoneQuery && !mWaitSimQuery) {
			PalmchatLogUtils.println("filterContactRequest");
		HashMap<String, AfFriendInfo> contactMap = queryHandler.getContacts();
		if (null != mLooperThread) {
			Handler handler = mLooperThread.handler;
			if (null != handler) {
				handler.obtainMessage(LooperThread.DISPLAY_PHONE_LIST, contactMap).sendToTarget();	
			}
			}
		}
	}
	
	private class SearchParams {
		String mSearch;
		List<AfFriendInfo> mList;
	}

    private class SearchTextWatcher implements TextWatcher {
    	
    	//private List<AfFriendInfo> mmList = null;

		@SuppressWarnings("unchecked")
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			/*if (mCursorAdapter != null && s.length() == 0) {
				mmList = (List<AfFriendInfo>) mCursorAdapter.getContactList();
			}*/
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}

		@Override
		public void afterTextChanged(Editable s) {

			String text = s.toString();
			if (text == null || text.length() == 0 && mCursorAdapter != null) {
				
				mCursorAdapter.notifyDataSetChanged(mContactList);
				mCancel_btn.setVisibility(View.GONE);
				
				return;
			}
			
			try {
				search(text, mContactList);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mCancel_btn.setVisibility(View.VISIBLE);
			
		}
    	
    }
    
    @SuppressLint("DefaultLocale")
	private void search(String search, List<AfFriendInfo> list) {

		if (list != null) {
	
			if (null != mLooperThread) {
				Handler handler = mLooperThread.handler;
				if (null != handler) {
					
					SearchParams ms = new SearchParams();
					ms.mSearch = search;
					ms.mList = list;
					
					handler.obtainMessage(LooperThread.SEARCH_FRIENDS, ms).sendToTarget();
				}
			}
			
			
//			mSearchResultList.clear();
//			mSearchResultList.addAll(results);
		}

	}

	@Override
	public void onClick(View v) {
		
	}



}
