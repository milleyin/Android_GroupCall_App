package com.afmobi.palmchat.ui.activity.chats;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chats.adapter.ForwardSelectAdapter;
import com.afmobi.palmchat.ui.activity.main.model.HomeGroupInfo; 
import com.afmobi.palmchat.ui.customview.RightHomeListView;
import com.afmobi.palmchat.util.BitmapUtils;
import com.afmobi.palmchat.util.ByteUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ToastManager;
//import com.afmobi.palmchat.util.image.ImageOnScrollListener;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfLoginInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpSysListener;
import com.core.param.AfCacheParam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
/**
 * forward select friend or group 
 * @author afmobi
 *
 */
public class ForwardSelectActivity extends BaseActivity implements OnClickListener , AfHttpSysListener {
	private static final String TAG = ForwardSelectActivity.class.getCanonicalName();
	
	public static final int SHARE_IMAGE = 8001;
	
	private EditText likeSearch;
	private RightHomeListView exList;
	private ForwardSelectAdapter adapter;
	private SearchTextWatcher mSearchTextWatcher;

	private LooperThread looperThread;
//	private ListViewAddOn listViewAddOn = new ListViewAddOn();
	
	private ArrayList<String> shareImageUri;//保存的是从相册或Google相册分享图片的路径,这里之所以用String替代Uri是因为ForwardelectActivity这个类分享图片要跳到chatting或GroupChatting里 如果跳过去再解析uri地址会遇到权限问题
	private boolean isShareImage;
	private String forward_imagePath = "";
	String afid;
	String password;
	/**中间件*/
	private AfPalmchat mAfCorePalmchat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat; 
		parseActionSend(getIntent());
		super.onCreate(savedInstanceState);
	}
	/**
	 * 从图库分享的处理
	 * @param intent
	 */
	private void parseActionSend(Intent intent) {
		String action = intent.getAction();
		String type = intent.getType();
		if ((Intent.ACTION_SEND_MULTIPLE.equals(action) || Intent.ACTION_SEND.equals(action)) && type != null) {
			if (type.startsWith(DefaultValueConstant.IMAGESHARE_LABEL)) {
				shareImageUri = new ArrayList<String>();
				isShareImage = true;
				if (Intent.ACTION_SEND_MULTIPLE.equals(action)) {
					if(intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM) != null){
						//for ()
						//shareImageUri.addAll((ArrayList<Uri>) intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM));
						ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
						int index=0;
						String path=null;
						for (Uri imageUri : imageUris) {
//							shareImageUri.add(imageUri);
							index++;
							if(index>DefaultValueConstant.MAX_SELECTED_BROADCAST_PIC){
								break;
							}
							File file =null; 
							 path = CommonUtils.getRealFilePath(this,imageUri);
							if(!TextUtils.isEmpty(path)){
							 	file= new File(path);
							} else {
								Bitmap imgSource = CommonUtils.getImageUrlWithAuthority(this, imageUri);
								if (imgSource != null) {
									file =new File( BitmapUtils.imageSaveToFile(imgSource, RequestConstant.CAMERA_CACHE + System.currentTimeMillis()));
									if(file.exists()){
										path = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
										path = BitmapUtils.imageCompressionAndSave(file.getAbsolutePath(), path);
									}
								}
							}
							 
							if ((null==file)||(!file.exists())) {
								ToastManager.getInstance().show(context, R.string.fail_to_load_picture);
							}else{
								shareImageUri.add( path );
							}
						}
					}
				} else {
					Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
					if (imageUri != null) {
//						shareImageUri.add(imageUri);
						File file =null; 
						String path= CommonUtils.getRealFilePath(this,imageUri);
						if(!TextUtils.isEmpty(path)){
							file= new File(path);
						} 
						if ((null!=file)&&(!file.exists())) {
							ToastManager.getInstance().show(context, R.string.fail_to_load_picture);
						}else{
							shareImageUri.add( path );
						}
					}
				}
			}
		}
		if (type != null && type.startsWith(JsonConstant.KEY_BC_FORWARD_IMAGE_TO_CHATTING)) {
			forward_imagePath = intent.getStringExtra(JsonConstant.KEY_BC_FORWARD_IMAGEPAHT);
		}
		 password = CacheManager.getInstance().getMyProfile().password;
		 afid = CacheManager.getInstance().getMyProfile().afId;
		
		if(TextUtils.isEmpty(password) || TextUtils.isEmpty(afid)){
			Toast.makeText(this, R.string.transit_hint_login, Toast.LENGTH_SHORT).show();
			/*add by zhh*/
			toLogin();
		}
		
    }
	
	
	/** zhh
	 * 用已经存在的手机号或邮箱登录
	 */
	private void toLogin() {
		MyActivityManager.getScreenManager().popAllActivityExceptOne(ForwardSelectActivity.class);
		Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		PalmchatLogUtils.i("forwadLogin","log");
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SHARE_IMAGE) {
			finish();
		}
	}
	
	private void initViews() {
		setContentView(R.layout.activity_forward_select);
//		TextView tvTitle = (TextView) findViewById(R.id.title_text);
//		tvTitle.setText(R.string.select);
		
		ImageView mBackImg = (ImageView) findViewById(R.id.back_button);
		mBackImg.setOnClickListener(this);
		
		likeSearch = (EditText)  findViewById(R.id.search_et);
		List<AfFriendInfo> frdList = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).getList();
		if(frdList == null || frdList.size() == 0){
			AfLoginInfo afLoginInfo = null;// getPalmchatAccounts(); //获取本地曾经登录过的账号  这里注释掉 是不用再共享了
			if(afLoginInfo == null){//如果客户端本地保存为空  那就从中间件获取登录过的账号
				AfLoginInfo[] myAccounts = PalmchatApp.getApplication().mAfCorePalmchat.AfDbLoginGetAccount();
				if(myAccounts != null && myAccounts.length > 0)
				{
					afLoginInfo = myAccounts[0];
				}
				PalmchatLogUtils.i("forwad","afLoginInfo");
			}
			PalmchatApp.getApplication().mAfCorePalmchat.setHttpSysListener(ForwardSelectActivity.this);
			PalmchatApp.getApplication().mAfCorePalmchat.AfLoadAccount(afLoginInfo);
		}

		exList = (RightHomeListView) findViewById(R.id.home_expandable_listview);

		exList.setHeaderView(getLayoutInflater().inflate(
				R.layout.home_right_group_header, exList, false));
		exList.setFooterView(getLayoutInflater().inflate(
				R.layout.home_right_group_header, exList, false));

		if (null != looperThread) {
			Handler handler = looperThread.handler;
			if (null != handler) {
				handler.obtainMessage(LooperThread.UPDATE_LIST).sendToTarget();
			}
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		updateGroupList();
	}
	
	private void updateGroupList() {
		if (null != looperThread) {
			Handler handler = looperThread.handler;
			if (null != handler) {
				handler.obtainMessage(LooperThread.UPDATE_GROUP_LIST).sendToTarget();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		looperThread.looper.quit();
		
	};
	
	
	ForwardSelectAdapter resultAdapter = null;

	@Override
	public boolean AfHttpSysMsgProc(int msg, Object wparam, int lParam) {
		switch (msg) {

			case Consts.AF_SYS_MSG_INIT:
				PalmchatLogUtils.i("forwad","AfHttpSysMsgProc");
				AfCacheParam param = (AfCacheParam)wparam;
				if( null != param){
					param.msg = msg;
					AfPalmchat.getCacheManager().loadCache(param);
				}
				if (null != looperThread) {
					Handler handler = looperThread.handler;
					if (null != handler) {
						handler.obtainMessage(LooperThread.UPDATE_LIST).sendToTarget();
					}
				}
				break;
		}
		return false;
	}

	private class SearchTextWatcher implements TextWatcher {

		List<AfFriendInfo> result = null;


		public SearchTextWatcher(ForwardSelectAdapter adapter) {
			resultAdapter = adapter;
		}
		
		public void resetResultList(List<AfFriendInfo> rlist) {
			result = rlist;
		}

		
		@SuppressWarnings("unchecked")
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			if (resultAdapter != null && s.length() == 0) {
				// friends list
				result = (List<AfFriendInfo>) resultAdapter.getFriendsList();
			}
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			String text = s.toString();
			if (text == null || text.length() == 0 && resultAdapter != null) {
				resultAdapter.setFriendsList(result);
				resultAdapter.notifyDataSetChanged();
				if(!exList.isGroupExpanded(0)) {
					
					exList.expandGroup(0);
				}
				
				if(!exList.isGroupExpanded(1)) {
					
					exList.expandGroup(1);
				}
//				likeSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.searchicon, 0, 0, 0);
				return;
			}
			
//				likeSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				
				try {
					
					search(text, result);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

		/**
		 * fuzzy search friends list
		 * 
		 * @param search
		 *            key
		 * @param list
		 */
		@SuppressLint("DefaultLocale")
		public void search(String search, List<AfFriendInfo> list) {

			if (list != null) {
		
				if (null != looperThread) {
					Handler handler = looperThread.handler;
					if (null != handler) {
						
						SearchParams ms = new SearchParams();
						ms.mSearch = search;
						ms.mList = list;
						
						handler.obtainMessage(LooperThread.SEARCH_FRIENDS, ms).sendToTarget();
					}
				}

			}

		}

	}
	
	private class SearchParams {
		String mSearch;
		List<AfFriendInfo> mList;
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case LooperThread.SEARCH_FRIENDS:

					try {

						SearchParams params = (SearchParams) msg.obj;
						resultAdapter.setFriendsList(params.mList);
						resultAdapter.notifyDataSetChanged();


						exList.setVisibility(View.VISIBLE);

//						mNetSearchResultLayout.setVisibility(View.GONE);
//						enter_btn.setVisibility(View.GONE);

						if (exList.isGroupExpanded(0)) {

							exList.collapseGroup(0);
						}

						if (!exList.isGroupExpanded(1)) {

							exList.expandGroup(1);
						}


					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				case LooperThread.UPDATE_GROUP_LIST:

					List<AfGrpProfileInfo> grp_list = (List<AfGrpProfileInfo>) msg.obj;
					if (adapter != null) {
						adapter.setGroupList(grp_list);
						adapter.setGroupsCount(grp_list.size() + "");
						adapter.notifyDataSetChanged();
					}
					break;
				case LooperThread.UPDATE_LIST:
					List<List<?>> childs = (ArrayList<List<?>>) msg.obj;
					List<AfFriendInfo> frdList = (List<AfFriendInfo>) childs.get(1);
					List<AfGrpProfileInfo> groupList = (List<AfGrpProfileInfo>) childs.get(0);
					String[] gTitles = getResources().getStringArray(R.array.forward_list);
					List<Map<String, HomeGroupInfo>> groups = new ArrayList<Map<String, HomeGroupInfo>>();
					for (int i = 0; i < 2; i++) {
						Map<String, HomeGroupInfo> group = new HashMap<String, HomeGroupInfo>();
						HomeGroupInfo gInfo = new HomeGroupInfo();
						gInfo.title = gTitles[i];

						// group
						if (i == 0) {
							gInfo.msgCount = groupList.size() + "";

							// friends
						} else if (i == 1) {
							gInfo.msgCount = frdList.size() + "";
						}
						group.put("g", gInfo);
						groups.add(group);
					}
					adapter = new ForwardSelectAdapter(ForwardSelectActivity.this, exList, groups, childs,   isShareImage, shareImageUri, forward_imagePath);
					exList.setOnScrollListener(new ImageOnScrollListener(exList ));
					exList.setAdapter(adapter);
					exList.expandGroup(0);
					exList.expandGroup(1);
					adapter.notifyDataSetChanged();
					mSearchTextWatcher = new SearchTextWatcher(adapter);
					likeSearch.addTextChangedListener(mSearchTextWatcher);
					break;
			}
		}
		
	};
	
	class LooperThread extends Thread {
		
		private static final int SEARCH_FRIENDS = 801;
		private static final int UPDATE_GROUP_LIST = 802;
		private static final int UPDATE_LIST = 803;
		Handler handler;

		/**
		 * 线程内部Looper
		 */
		Looper looper;

		@SuppressLint("HandlerLeak")
		@Override
		public void run() {
			Looper.prepare();
			looper = Looper.myLooper();
			// 保持当前只有�?条线程在执行查看数据操作
			handler = new Handler() {
				@SuppressLint("DefaultLocale")
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
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
							
							if(list.get(i).user_msisdn != null) {
								Matcher matcher3 = pattern.matcher((list.get(i).user_msisdn));
								if (matcher3.find()) {
									if (findInfo != null && !findInfo.afId.equals(list.get(i).afId)
											|| findInfo == null) {
									results.add(list.get(i));
									}
								}
							}
							
						}
						
						Message mainMsg = new Message();
						mainMsg.what = SEARCH_FRIENDS;
						params.mList = results;
						mainMsg.obj = params;
						mHandler.sendMessage(mainMsg);
						break; 
						
						
//						update group list
					case UPDATE_GROUP_LIST:
						PalmchatLogUtils.println("LooperThread UPDATE_GROUP_LIST");
						ArrayList<AfGrpProfileInfo> grp_list = new ArrayList<AfGrpProfileInfo>();
						
						List<AfGrpProfileInfo> grp_cacheList = CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).getList();
						
//						status == active, add to group list
						for(AfGrpProfileInfo grpProfile : grp_cacheList) {
							if(grpProfile.status == Consts.AFMOBI_GRP_STATUS_ACTIVE) {
								grp_list.add(grpProfile);
							}
						}
						
						mHandler.obtainMessage(UPDATE_GROUP_LIST, grp_list).sendToTarget();
						break;
						case UPDATE_LIST:

							List<Map<String, HomeGroupInfo>> groups = new ArrayList<Map<String, HomeGroupInfo>>();
							List<List<?>> childs = new ArrayList<List<?>>();
							List<AfGrpProfileInfo> groupList = CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).getList();
							List<AfFriendInfo> frdList = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).getList();
							List<AfFriendInfo> filterFriends = ByteUtils.searchFilterFriend(frdList, RequestConstant.SERVICE_FRIENDS);
							if (filterFriends != null) {
								for (AfFriendInfo f : filterFriends) {
									frdList.remove(f);
								}
							}
							PalmchatLogUtils.i("forwad",Integer.toString(frdList.size()));
							childs.add(groupList);
							childs.add(frdList);
							Message message = new Message();
							message.what = UPDATE_LIST;
							message.obj = childs;
							PalmchatLogUtils.i("forwad","getfdsa");
							mHandler.sendMessage(message);
							break;
					}
					
				}
				
			};
			
			Looper.loop();
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_button:
			CommonUtils.closeSoftKeyBoard(likeSearch);
			finish();
			break;
			
		}
	}

	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		looperThread = new LooperThread();
		looperThread.setName(TAG);
		looperThread.start();
		initViews();
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}



	class ImageOnScrollListener implements AbsListView.OnScrollListener {
		private ListView listView;

		public ImageOnScrollListener(ListView listView) {
			super();
			this.listView = listView;

		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			if (view instanceof RightHomeListView) {

				final long flatPos = ((RightHomeListView) view).getExpandableListPosition(firstVisibleItem);
				int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);
				int childPosition = ExpandableListView.getPackedPositionChild(flatPos);

				final long flatPos2 = ((RightHomeListView) view).getExpandableListPosition(firstVisibleItem + visibleItemCount - 1);
				int groupPosition2 = ExpandableListView.getPackedPositionGroup(flatPos2);
				int childPosition2 = ExpandableListView.getPackedPositionChild(flatPos2);

				((RightHomeListView) view).onHomeListViewScroll(groupPosition, childPosition, groupPosition2, childPosition2);
			}
		}
	}
}
