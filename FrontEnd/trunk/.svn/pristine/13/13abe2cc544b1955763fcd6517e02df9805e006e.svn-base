package com.afmobi.palmchat.ui.activity.chats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.ui.customview.SideBar;
import com.afmobi.palmchat.ui.customview.SideBar.OnTouchingLetterChangedListener;
import com.afmobi.palmchat.util.ByteUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.SearchFactory;
import com.afmobi.palmchat.util.SearchFilter;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.Consts;
import com.core.cache.CacheManager;

public class SelectListActivity extends BaseActivity implements OnClickListener ,OnItemClickListener {

	private static final String TAG = SelectListActivity.class.getCanonicalName();
	private SearchFilter searchFilter = SearchFactory.getSearchFilter(SearchFactory.DEFAULT_CODE);
	private SearchTextWatcher searchTextWatcher = new SearchTextWatcher();
	private List<AfFriendInfo> friendList = new ArrayList<AfFriendInfo>();
	
	private ListView listView;
	private TextView mDialogText;
	private SideBar indexBar;
	private FriendAdapter adapter;
	private String action;
	private String afid = "";

    private boolean recommend;
	private EditText likeSearch;

    private void search(String text) {
    	
    	List<AfFriendInfo> results = new ArrayList<AfFriendInfo>();
		List<AfFriendInfo> list = searchTextWatcher.mmList;
		Pattern pattern = null;
		try {
			
			 pattern = Pattern.compile(text.toUpperCase());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		
		if(pattern == null) {
			return;
		}
		
		AfFriendInfo findInfo = null;
		for (int i = 0; i < list.size(); i++) {
			
			if(list.get(i) == null) {
				break;
			}
			
			Matcher matcher = pattern.matcher((list.get(i).afId));

			if (matcher.find()) {
				findInfo = list.get(i);
				results.add(findInfo);
			}

			String str = list.get(i).alias != null ? list.get(i).alias : list.get(i).name;
			if(str != null) {
				
				Matcher matcher2 = pattern.matcher(str.toUpperCase());
			if (matcher2.find()) {

				if (findInfo != null && !findInfo.afId.equals(list.get(i).afId)
						|| findInfo == null) {

					results.add(list.get(i));
				}

			}
			
			}
    
    }
		
		if (results.size() > 0 && results.size() == CacheManager.getInstance().getFriends_list(Consts.AFMOBI_FRIEND_TYPE_FF).size()) {
			indexBar.setVisibility(View.VISIBLE);
		} else {
			indexBar.setVisibility(View.GONE);
		}
		
		adapter.reflashData(results);
		adapter.notifyDataSetChanged();
    }
    
    private class SearchTextWatcher implements TextWatcher {
    	
    	private List<AfFriendInfo> mmList = null;

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			if (adapter != null && s.length() == 0) {
				mmList = adapter.list;
			}
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			String text = s.toString();
			if (text == null || text.length() == 0 && adapter != null) {
				adapter.reflashData(mmList);
				adapter.notifyDataSetChanged();
				if (mmList.size() > 0) {
					indexBar.setVisibility(View.VISIBLE);
				}
				return;
			}
			search(text);
		}
    	
    }

	@Override
    protected void onResume() {
        super.onResume();
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
    protected void onPause() {
        super.onPause();
    }
    
    private class FriendAdapter extends BaseAdapter implements SectionIndexer {
    	
    	private Context mContext;
		private List<AfFriendInfo> list;
		private Integer [] mPositions;
		public FriendAdapter(Context context , List<AfFriendInfo> list) {
			this.mContext = context;
			this.list = list;
		}
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		public void reflashData(List<AfFriendInfo> data) {
			if (data != null) {
				list = data;
			}
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			/*******************解决华为等部分手机闪退的问题********************/
			Activity activity = (Activity) mContext;
			View currentFocus = activity.getCurrentFocus();
			if (currentFocus != null) {
				currentFocus.clearFocus();
			}
			/*******************解决华为等部分手机闪退的问题********************/
			ViewHolder holder;
			
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.friend_list_items, null);
				holder = new ViewHolder();
				holder.textSort = (TextView)convertView.findViewById(R.id.friend_sort);
				holder.imgIcon = (ImageView)convertView.findViewById(R.id.friend_photo);
				holder.textName = (TextView)convertView.findViewById(R.id.friend_name);
				holder.textSign = (TextView)convertView.findViewById(R.id.friend_sign);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		
			final AfFriendInfo afFriendInfo = list.get(position);
			
			char c = searchFilter.getAlpha(CommonUtils.getRealDisplayName(afFriendInfo).toUpperCase());
			if (position == 0) {
				holder.textSort.setVisibility(View.VISIBLE);
				holder.textSort.setText(c + "");
			} else {
				AfFriendInfo afPre = (AfFriendInfo) list.get(position - 1);
			
				char lastCatalog = searchFilter.getAlpha(CommonUtils.getRealDisplayName(afPre).toUpperCase());
				if (c == lastCatalog) {
					holder.textSort.setVisibility(View.INVISIBLE);
				} else {
					holder.textSort.setVisibility(View.VISIBLE);
					holder.textSort.setText(c + "");
				}
			}
			
			String signStr = afFriendInfo.signature == null ? mContext.getString(R.string.default_status) : afFriendInfo.signature;
			CharSequence text = EmojiParser.getInstance(mContext).parse(signStr, ImageUtil.dip2px(mContext, 13));
			holder.textSign.setText(text);
			
			String name =  TextUtils.isEmpty(afFriendInfo.alias) ? afFriendInfo.name : afFriendInfo.alias;
			// name is empty,so afid
			if (StringUtil.isNullOrEmpty(name)) {
				name = afFriendInfo.afId.replace("a", "");
			}
			
			holder.textName.setText(name);
			//调UIL的显示头像方法, 替换原来的AvatarImageInfo.
			ImageManager.getInstance().DisplayAvatarImage(holder.imgIcon, afFriendInfo.getServerUrl(),afFriendInfo .getAfidFromHead()
					,Consts.AF_HEAD_MIDDLE,afFriendInfo.sex,afFriendInfo.getSerialFromHead(),null);
			/*AvatarImageInfo imageInfo = new AvatarImageInfo(afFriendInfo.getAfidFromHead(),
					afFriendInfo.getSerialFromHead(), name, Consts.AF_HEAD_MIDDLE, afFriendInfo.getServerUrl(), afFriendInfo.sex);
			com.afmobi.palmchat.util.image.ImageLoader.getInstance().displayImage(holder.imgIcon, imageInfo);*/
			
			return convertView;
		}

		private class ViewHolder {
			TextView textSort; 
			ImageView imgIcon; 
			TextView textName; 
			TextView textSign; 
			
		}

		@Override
		public Object[] getSections() {
			return null;
		}
		
		@Override
		public int getPositionForSection(int section) {
			if (section == '!') {
				return 0;
			} else {
				AfFriendInfo friend = null;
				for (int i = 0; i < getCount(); i++) {
					friend = list.get(i);
					char firstChar = searchFilter.getAlpha(CommonUtils.getRealDisplayName(friend).toUpperCase());
					if (firstChar == section) {
						return i + 1;
					}

				}
			}
			return -1;
		}

		@Override
		public int getSectionForPosition(int position) {
			int index = Arrays.binarySearch(mPositions, position);
			return index >= 0 ? index : 0; 
		}
		
		public String converterToFirstSpell(String chines){    
			return null;
		}      
    }
    
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AfFriendInfo f = (AfFriendInfo) parent.getAdapter().getItem(position);
		//recommend friend
		if (recommend) {
//			Friend recommend = getIntent().getParcelableExtra("friend");
//			Intent intent = new Intent(SelectListActivity.this, Chatting.class);
//			intent.putExtra(Chatting.TT, Chatting.RECOMMEND);
//			intent.putExtra("friend", (Parcelable)f);
//			intent.putExtra("recommend", (Parcelable)recommend);
//			startActivity(intent);
//			finish();
		} else {
			if ("chats".equals(action)) {
				Intent intent = new Intent(SelectListActivity.this, Chatting.class);
				intent.putExtra(Chatting.TT, Chatting.FRIEND);
				intent.putExtra("friend", (Parcelable)f);
				intent.putExtra(JsonConstant.KEY_FROM_ALIAS, f.alias);
				startActivity(intent);
				finish();
			} else {
				Intent intent = new Intent(SelectListActivity.this, Chatting.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(JsonConstant.KEY_FRIEND, f);
				bundle.putString(Chatting.TT, Chatting.RECOMMEND);
				intent.putExtras(bundle);
				setResult(10,intent);
				finish();
				
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			back();
			break;
		}
	}
	
	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
//		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_select_list);
		
        recommend = getIntent().getBooleanExtra(JsonConstant.KEY_TYPE, false);
        action = getIntent().getStringExtra(JsonConstant.KEY_ACTION);
        afid = getIntent().getStringExtra(JsonConstant.KEY_AFID);
        
        if (afid == null) {
			afid = "";
		}
        
        ((TextView)findViewById(R.id.title_text)).setText(R.string.select_a_friend);
		View searchLayout = View.inflate(context, R.layout.include_search_layout, null);
		listView = (ListView) findViewById(android.R.id.list);
		likeSearch = (EditText) searchLayout.findViewById(R.id.search_input);
		mDialogText = (TextView) findViewById(R.id.textview_show); 
		indexBar = (SideBar) findViewById(R.id.sidrbar);
		indexBar.setTextView(mDialogText);
		findViewById(R.id.back_button).setOnClickListener(this);
		
		listView.addHeaderView(searchLayout);
        listView.setOnItemClickListener(this);
        likeSearch.addTextChangedListener(searchTextWatcher);
        friendList.addAll(CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).getList());
        List<AfFriendInfo> filterFriends = ByteUtils.searchFilterFriend(friendList, RequestConstant.SERVICE_FRIENDS);
        if (filterFriends != null) {
        	for (AfFriendInfo f : filterFriends) {
        		friendList.remove(f);
			}
		} 
        
        AfFriendInfo f = ByteUtils.searchFriend(friendList, afid);
        if (f != null) {
        	friendList.remove(f);
        } 
        
        adapter = new FriendAdapter(this , friendList);
    	listView.setAdapter(adapter);
    	
    	indexBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				CommonUtils.closeSoftKeyBoard(likeSearch);
				likeSearch.clearFocus();
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					listView.setSelection(position);
				}
				
			}
		});
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				CommonUtils.closeSoftKeyBoard(likeSearch);
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
	
	private void back() {
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			back();
		}
		return false;
	}
	

}

