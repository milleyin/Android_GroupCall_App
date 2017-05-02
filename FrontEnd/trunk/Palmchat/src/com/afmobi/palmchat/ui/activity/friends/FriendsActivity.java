package com.afmobi.palmchat.ui.activity.friends;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobi.palmchat.eventbusmodel.CloseChatingEvent;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.groupchat.EditGroupActivity;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SearchFactory;
import com.afmobi.palmchat.util.SearchFilter;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfGrpNotifyMsg;
import com.core.AfGrpProfileInfo;
import com.core.AfGrpProfileInfo.AfGrpProfileItemInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import de.greenrobot.event.EventBus;
//import com.umeng.analytics.MobclickAgent;

/**
 * 增加好友到群组
 *
 * @author jindegege
 */

public class FriendsActivity extends BaseActivity implements
		AfHttpResultListener {

	private static final String TAG = FriendsActivity.class.getCanonicalName();
	ListView mListView;
	AfPalmchat mAfCorePalmchat;
	private FriendsListAdapter mAdapter;
	EditText mSearchEditText;
	TextView title_text,groupCount_text;
	ImageView delSearch;
	View select_button;

	/**
	 * 群组名
	 */
	private String groupName;
	/**
	 * 用于取消与中间件的连接
	 */
//	private SparseArray<Integer> mhttpHandle;
	/**
	 * 选定好友列表
	 */
	public volatile ArrayList<AfGrpProfileItemInfo> recipientList;

	/**
	 * 当前正在聊天好友的列表
	 */
	private ArrayList<String> chatList = new ArrayList<String>();

	/**
	 * 圈子成员最大人数
	 */
	public final static int Max_Member_Num = 50;
	/**
	 * 圈子能容纳的成员数
	 */
	private int groupListItemSize = Max_Member_Num;

	/**
	 * 进入联系人选择器时，可以选择的联系人个数（针对联系个数有限制的）
	 */
	private int selectMaxNum = Integer.MAX_VALUE;

	/**
	 * 群聊原有人数
	 */
	private int ownerNum;

	/**
	 * 已选择的好友列表
	 */
	public HashMap<String, AfGrpProfileItemInfo> selectedFriendMap = new HashMap<String, AfGrpProfileItemInfo>();
	public HashMap<String, AfGrpProfileItemInfo> friendMap = new HashMap<String, AfGrpProfileItemInfo>();

	/**
	 * 存放进入选择好友界面时，要排除的好友
	 */
	HashMap<String, String> selectedMap = new HashMap<String, String>();

	/**
	 * 已选择的联系人数量
	 */
	private int selectedCount = 0;
	/**
	 /**
	 * 从私聊页面建群时，好友afid
	 */
	private String singleAfid;
	/* 从管理群聊过来的群组id
	*/
	private String groupId, gname = null;
	private static final int MAX_ADD_MEMBER = 50;
	/**
	 * which page come from
	 */
	private String comePage = null;

	private AfMessageInfo mAfMessageInfoTempCreate;
	private SearchFilter searchFilter = SearchFactory
			.getSearchFilter(SearchFactory.DEFAULT_CODE);
	/**
	 * 头像加载：是否只从缓存中加载头像
	 * 何桂明 2013-10-22
	 */
//	private ListViewAddOn listViewAddOn = new ListViewAddOn();

	/**
	 * 好友集合
	 */
	List<AfGrpProfileItemInfo> profileItemInfo_list;
	private String mMultichat_page;
	private String mGroupChooseMember;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAfCorePalmchat = app.mAfCorePalmchat;
	}

	@Override
	protected void onResume() {
		super.onResume();
//		MobclickAgent.onPageStart("FriendsActivity");
//		MobclickAgent.onResume(this);
		// heguiming 2013-12-04
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_FL);
//		MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_FL);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd("FriendsActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
//		MobclickAgent.onPause(this);
	}

	/**
	 * handle for disposal
	 */
	private Handler groupHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			PalmchatLogUtils.e(TAG, "----msg.what:" + msg.what);

			switch (msg.what) {
				case EditGroupActivity.GROUP_CREATE:
					createGroup();
					showProgressDialog(R.string.please_wait);
					break;
				case EditGroupActivity.GROUP_CREATE_FAIL:
					dismissProgressDialog();
					Toast.makeText(FriendsActivity.this, R.string.error_create,
							Toast.LENGTH_SHORT).show();
					break;
				case EditGroupActivity.GROUP_INVITE:
					if (null == msg.obj) {
						sendEmptyMessage(EditGroupActivity.GROUP_INVITE_FAIL);
						break;
					}
					if (!StringUtil.isEmpty(groupId, true)) {
						showProgressDialog(R.string.please_wait);
					}

					String temp = (String) msg.obj;
					if (!StringUtil.isEmpty(groupId, true) && temp.equals(groupId)) {
						if (null == recipientList || 0 >= recipientList.size()) {
							groupHandler.sendEmptyMessage(EditGroupActivity.GROUP_INVITE_FAIL);
						} else {
							inviteMember(groupId);
						}
					} else if (!StringUtil.isEmpty(groupId, true)
							&& temp.equals(groupId)) {
						if (null == recipientList || 0 >= recipientList.size()) {
							groupHandler.sendEmptyMessage(EditGroupActivity.GROUP_INVITE_FAIL);
						} else {
							inviteMember(groupId);
						}
					}
					break;
				case EditGroupActivity.GROUP_INVITE_FAIL:
					dismissProgressDialog();
					Toast.makeText(FriendsActivity.this,
							getString(R.string.error_invite),
							DefaultValueConstant.TOAST_FIVE_SECONDS).show();
					break;
				case EditGroupActivity.GROUP_INVITE_SUCCESS:
					AfGrpProfileInfo mProfileInfo = null;
					List<AfGrpProfileItemInfo> members;
					String member = "";
					if (msg.obj != null) {
						mProfileInfo = (AfGrpProfileInfo) msg.obj;
						members = mProfileInfo.members;
						members = sortMemberList1(members);
						if (null != members) {
							if(TextUtils.isEmpty(groupName)){
								member = sortGroupName(members, member);
							}
							else {
								member = groupName;
							}
							if (isCreate) {
								mProfileInfo.name = member;
							}
						}
						mProfileInfo.members = (ArrayList<AfGrpProfileItemInfo>) members;
						member = mProfileInfo.name;

						mProfileInfo.tips = mAfCorePalmchat.AfDbSettingGetTips(mProfileInfo.afid);

						if (isCreate) {
							mProfileInfo.tips = true;
							CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).insert(mProfileInfo, true, true);
							if (mAfMessageInfoTempCreate != null) {
								new Thread(new Runnable() {
									public void run() {
										MessagesUtils.insertMsg(mAfMessageInfoTempCreate, true, true);
										mAfMessageInfoTempCreate = null;
									}
								}).start();
							}
						} else {
							CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).saveOrUpdate(mProfileInfo, true, true);
						}

					}
					dismissProgressDialog();
					if (mProfileInfo != null && !StringUtil.isEmpty(groupId, true)) {
						Bundle bundle = new Bundle();

						bundle.putString(GroupChatActivity.BundleKeys.ROOM_ID, groupId);
						bundle.putString(GroupChatActivity.BundleKeys.ROOM_NAME, member);
						bundle.putString("come_page", "FriendsActivity");
						if (comePage.contains("first_compepage") || comePage.contains("singlechat_page")) {
							bundle.putBoolean("is_create", true);
							bundle.putBoolean("singlechat", true);
							jumpToPage(GroupChatActivity.class, bundle, true, 0, true);
						}
						if (comePage.contains("grouplist_page")) {
							bundle.putBoolean("is_invite", true);
							setResult(EditGroupActivity.REQUEST_CODE_ADD_FRIEND);
						}

						finish();
					}
					break;
				default:
					break;
			}
		}
	};


	/**
	 *
	 * @param members
	 * @return
	 */
	private List<AfGrpProfileItemInfo> sortMemberList1(
			List<AfGrpProfileItemInfo> members) {
		if (null == members || 2 >= members.size()) {
			return members;
		}
		List<AfGrpProfileItemInfo> memberList = new ArrayList<AfGrpProfileItemInfo>();
		List<AfGrpProfileItemInfo> NameMemberList = new ArrayList<AfGrpProfileItemInfo>();

		AfGrpProfileItemInfo admin = null;
		for (AfGrpProfileItemInfo mitem : members) {

			if (0 == mitem.invitePhoto) {
				memberList.add(mitem);
			} else if (1 == mitem.isMaster) {
				admin = mitem;
			} else if (!StringUtil.isEmpty(mitem.afid, true)) {
				memberList.add(mitem);
			} else if (!StringUtil.isEmpty(mitem.name, true)) {
				NameMemberList.add(mitem);
			}
		}
		if (null != admin) {
			memberList.add(0, admin);
		}
		if (0 < NameMemberList.size()) {
			memberList.addAll(NameMemberList);
		}
		return memberList;
	}

	/**
	 * from page
	 */
	private void fromPage() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			if (bundle.containsKey("come_page")) {
				if (bundle.getString("come_page") != null) {
					if (bundle.getString("come_page").equals("multichat_page")
							|| bundle.getString("come_page").equals(
							"grouplist_page")
							|| bundle.getString("come_page").equals(
							"first_compepage")
							|| bundle.getString("come_page").equals(
							"singlechat_page")) {//singlechat_page从私聊页面添加群

						// 多聊页面跳转
						comePage = bundle.getString("come_page");
						getChatPerson(intent);
					}
					if (bundle.getString("come_page").equals("first_compepage")) {
						selectedFriendMap.clear();
						selectedCount = 0;
						refreshTitleBar();
					}

				}
			}
		}

	}


	/**
	 * 方法表述:将selectedFriendMap对象转化成Recipient对象,并添加到recipientList中
	 *
	 */
	public void AfFriendInfoAddToRecipientList() {
		if (recipientList == null) {
			recipientList = new ArrayList<AfGrpProfileItemInfo>();
		} else {
			recipientList.clear();
		}
	/*	if("singlechat_page".equals(comePage)) {//singlechat_page从私聊页面添加群
			for (AfGrpProfileItemInfo info:profileItemInfo_list)
			if (null != selectedFriendMap && selectedFriendMap.containsKey(info.afid)) {
				if (!TextUtils.isEmpty(singleAfid) && singleAfid.equals(info.afid)) {
					selectedFriendMap.put(singleAfid, info);
				}
			}
		}*/
		Collection<AfGrpProfileItemInfo> c = selectedFriendMap.values();
		Iterator<AfGrpProfileItemInfo> it = c.iterator();
		while (it.hasNext()) {
			AfGrpProfileItemInfo temLoc = (AfGrpProfileItemInfo) it.next();
			AfGrpProfileItemInfo recipient = new AfGrpProfileItemInfo();
			recipient.afid = temLoc.afid;
			recipient.name = temLoc.name;
			recipient.sex = temLoc.sex;
			recipient.age = temLoc.age;
			recipientList.add(recipient);
		}
		mMultichat_page = "multichat_page";
		if (!"grouplist_page".equals(comePage)
				&& !"first_compepage".equals(comePage)
				&& !mMultichat_page.equals(comePage)
				&& !"singlechat_page".equals(comePage)) {//singlechat_page从私聊页面添加群
			selectedFriendMap.clear();
			selectedCount = 0;
		}
	}

	/**
	 * 取得当前聊天的对象
	 *
	 * @param intent
	 */
	private void getChatPerson(Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle.getString("come_page").equals("singlechat_page")) {//singlechat_page从私聊页面添加群
			chatList = new ArrayList<String>();
			chatList = bundle.getStringArrayList("afId_list");
			selectedFriendMap.clear();
			selectedCount = 0;
			if (null != chatList) {
				selectedCount = chatList.size();
				Log.e(TAG, "----selectedCount----" + selectedCount);
				for (int i = 0; i <= chatList.size() - 1; i++) {
					String afid = chatList.get(i);
					singleAfid = afid;
					if (!selectedMap.containsKey(afid)) {
						AfFriendInfo afF = CacheManager.getInstance().findAfFriendInfoByAfId(afid);
						AfGrpProfileItemInfo mProfileItemInfo = AfFriendInfo.ProfileItemInfoToFriend(afF);
						selectedMap.put(afid, null);
						selectedFriendMap.put(afid, mProfileItemInfo);
						//selectedCount++;
					}
					refreshTitleBar();
				}
				return;
			}

		}
		if (bundle != null) {
			if (bundle.containsKey("afId_list")) {
				chatList = new ArrayList<String>();
				chatList = bundle.getStringArrayList("afId_list");
				groupId = bundle.getString("group_id");
				if (null != chatList) {
					selectedFriendMap.clear();
					selectedCount = 0;
					List<AfGrpProfileItemInfo> infos = CacheManager.getInstance().searchGrpProfileInfo(groupId).members;
					int size = infos.size();
					for (int i = 0; i <= chatList.size() - 1; i++) {
						String afid = chatList.get(i);
						if (!selectedMap.containsKey(afid)) {
							selectedMap.put(afid, null);
						}
						if (infos != null) {
							if (i < size) {
								selectedFriendMap.put(afid, infos.get(i));
							}
							refreshTitleBar();
						}

					}

				}

			}

		}

		if (bundle.containsKey("group_id")) {
			groupId = bundle.getString("group_id");
		}
		if (bundle.containsKey("owner_num")) {
			ownerNum = bundle.getInt("owner_num");
			if (!StringUtil.isEmpty(groupId, true)) {
				AfGrpProfileInfo groupListItem = CacheManager.getInstance().searchGrpProfileInfo(groupId);
				if (null != groupListItem && 0 < groupListItem.maxSize) {
					groupListItemSize = groupListItem.maxSize;
				} else {
					Log.i(TAG, "error grouplistitem is null");
				}
			}
			selectMaxNum = groupListItemSize - ownerNum;
		}
		mAdapter.notifyDataSetChanged();
	}

	private class FriendsListAdapter extends BaseAdapter {
		List<AfGrpProfileItemInfo> listFriends;

		public FriendsListAdapter(List<AfGrpProfileItemInfo> listFriends) {
			this.listFriends = listFriends;

		}

		public void setData(List<AfGrpProfileItemInfo> listFriends) {
			this.listFriends = listFriends;
		}

		public List<AfGrpProfileItemInfo> getData() {
			return listFriends;
		}

		@Override
		public int getCount() {
			return listFriends.size();
		}

		@Override
		public Object getItem(int position) {
			return listFriends.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
							ViewGroup parent) {
			final ViewHolder holder;

			if (convertView == null) {
				convertView = LayoutInflater.from(FriendsActivity.this).inflate(R.layout.select_button_add_item, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.content = (TextView) convertView.findViewById(R.id.content);
				holder.add_btn = (ImageView) convertView.findViewById(R.id.select_btn);
				holder.textSort = (TextView) convertView.findViewById(R.id.friend_sort);
				// 陌生人头像
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.viewParent = convertView.findViewById(R.id.parent);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final AfGrpProfileItemInfo info = listFriends.get(position);
			AfFriendInfo mFriendInfo = CacheManager.getInstance().searchAllFriendInfo(info.afid);
			holder.title.setText(CommonUtils.getRealDisplayName(mFriendInfo));
			String sign = mFriendInfo.signature == null ? "" : mFriendInfo.signature;
			CharSequence text = EmojiParser.getInstance(context).parse(sign, ImageUtil.dip2px(context, 18));
			holder.content.setText(text);
			char c = searchFilter.getAlpha(CommonUtils.getRealDisplayName(mFriendInfo).toUpperCase());
			if (position == 0) {
				holder.textSort.setVisibility(View.VISIBLE);
				holder.textSort.setText(c + "");
			} else {
				AfFriendInfo preFriendInfo = CacheManager.getInstance().searchAllFriendInfo(listFriends.get(position - 1).afid);
				char lastCatalog = searchFilter.getAlpha(CommonUtils.getRealDisplayName(preFriendInfo).toUpperCase());
				if (c == lastCatalog) {
					holder.textSort.setVisibility(View.INVISIBLE);
				} else {
					holder.textSort.setVisibility(View.VISIBLE);
					holder.textSort.setText(c + "");
				}
			}
			if (mFriendInfo.signature == null) {
				holder.content.setText(context.getResources().getString(
						R.string.default_signature));
			}
			//调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
			ImageManager.getInstance().DisplayAvatarImage(holder.icon, mFriendInfo.getServerUrl(), mFriendInfo.getAfidFromHead()
					, Consts.AF_HEAD_MIDDLE, mFriendInfo.sex, mFriendInfo.getSerialFromHead(), null);
			final String afId = info.afid;
			if (null != selectedFriendMap && selectedFriendMap.containsKey(afId)) {
				holder.add_btn.setBackgroundResource(R.drawable.icon_group_chat_list_p);
			} else {
				holder.add_btn.setBackgroundResource(R.drawable.icon_group_chat_list_n);
			}

			holder.viewParent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (null != comePage) {
						if (null != chatList) { // 群组界面，已在群组中的则直接返回 return; }
							mSearchEditText.setText("");
							if (!selectedMap.containsKey(afId)) {
								if (selectedFriendMap.containsKey(afId)) {
									selectedFriendMap.remove(afId);
									selectedCount--;
									holder.add_btn.setBackgroundResource(R.drawable.set_select_bg);
								} else {
									int max;
									AfGrpProfileInfo mGrpProfileInfo = CacheManager.getInstance().searchGrpProfileInfo(groupId);
									if (!TextUtils.isEmpty(groupId)) {
										if (mGrpProfileInfo != null && CacheManager.getInstance().getMyProfile().afId.equals(mGrpProfileInfo.admin)) {
											max = 49;

										} else {
											max = 50;
										}
									} else {
										max = 49;
									}
									if (mGrpProfileInfo != null && CacheManager.getInstance().getMyProfile().afId.equals(mGrpProfileInfo.admin)) {
										if (selectedFriendMap.size() > max) {
											ToastManager.getInstance().show(context, context.getString(R.string.max_member_num_text));
											return;
										}
									} else {
										if (selectedFriendMap.size() >= max) {
											ToastManager.getInstance().show(context, context.getString(R.string.max_member_num_text));
											return;
										}
									}
									if ("grouplist_page".equals(comePage)
											|| "multichat_page".equals(comePage)
											|| "first_compepage".equals(comePage)
											|| "singlechat_page".equals(comePage)) {
										selectedFriendMap.put(afId, info);
										selectedCount++;
										holder.add_btn.setBackgroundResource(R.drawable.set_select);
									}
								}
								groupCount_text.setVisibility(View.VISIBLE);
								refreshTitleBar();
							}
						}
					} else {
						mSearchEditText.setText("");
						if (!selectedMap.containsKey(afId)) {
							if (selectedFriendMap.containsKey(afId)) {
								selectedFriendMap.remove(afId);
								selectedCount--;
								holder.add_btn.setBackgroundResource(R.drawable.set_select_bg);
							} else {
								if (selectedFriendMap.size() >= 49) {
									ToastManager.getInstance().show(context, context.getString(R.string.max_member_num_text));
									return;
								}
								selectedFriendMap.put(afId, info);
								selectedCount++;
								holder.add_btn.setBackgroundResource(R.drawable.set_select);
							}
							refreshTitleBar();
						}
					}
				}
			});
			return convertView;
		}
	}

	class ViewHolder {
		ImageView icon;
		TextView title;
		TextView content;
		ImageView add_btn;
		TextView textSort;
		View viewParent;
	}

	ImageView back_button;

	@Override
	public void findViews() {
		setContentView(R.layout.friend);
//		mhttpHandle = new SparseArray<Integer>();
		mListView = (ListView) findViewById(R.id.s_list);
		back_button = (ImageView) findViewById(R.id.back_button);
		back_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectedFriendMap.clear();
				selectedCount = 0;
				onBack();
			}
		});

		title_text = (TextView) findViewById(R.id.title_text);
		groupCount_text = (TextView) findViewById(R.id.title_offOnline);
		groupCount_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		groupCount_text.setVisibility(View.VISIBLE);

		mSearchEditText = (EditText) findViewById(R.id.search_et);
		mSearchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// heguiming 2013-12-04
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.FL_T_SF);
//					MobclickAgent.onEvent(context, ReadyConfigXML.FL_T_SF);
				}
			}
		});
		select_button = findViewById(R.id.op2);
		select_button.setBackgroundResource(R.drawable.navigation_yes_btn_selector);
		select_button.setVisibility(View.GONE);
		delSearch = (ImageView) findViewById(R.id.friend_delete_search_button);
		initSearchET(mSearchEditText);
		delSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSearchEditText.setText("");
				delSearch.setVisibility(View.GONE);
			}
		});
		delSearch.setVisibility(View.GONE);
		mGroupChooseMember = getString(R.string.choose_group_member);
		refreshTitleBar();
		select_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (selectedCount == 1 && !"grouplist_page".equals(comePage)) {
					List<AfGrpProfileItemInfo> lists = selectHashMapToList();
					if (null != lists && lists.size() > 0) {
						/*Handler handler = PalmchatApp.getApplication().getHandlerMap().get(Chatting.class.getCanonicalName());
						if (null != handler) {
							handler.sendEmptyMessage(Chatting.FINISH);
						}*/
						EventBus.getDefault().post(new CloseChatingEvent());
						AfGrpProfileItemInfo profileItem = lists.get(0);
						AfFriendInfo friendInfo = AfFriendInfo.FriendInfoToGrpProfileItemInfo(profileItem);
						Intent intent = new Intent(context, Chatting.class);
						intent.putExtra(JsonConstant.KEY_FROM_UUID, friendInfo.afId);
						intent.putExtra(JsonConstant.KEY_FROM_NAME, friendInfo.name);
						intent.putExtra(JsonConstant.KEY_FRIEND, friendInfo);
						startActivity(intent);
					}
					return;
				}
				AfFriendInfoAddToRecipientList();
				if ("first_compepage".equals(comePage) || "singlechat_page".equals(comePage)) {
					Message msg = new Message();
					msg.what = EditGroupActivity.GROUP_CREATE;
					groupHandler.sendMessage(msg);
				} else if ("grouplist_page".equals(comePage)) {
					Message msg = new Message();
					msg.what = EditGroupActivity.GROUP_INVITE;
					msg.obj = groupId;
					groupHandler.sendMessage(msg);
				}
			}
		});
	}

	/**
	 * 更改标题栏名称
	 */
	private void refreshTitleBar() {
		AfProfileInfo afInfo = CacheManager.getInstance().getMyProfile();
		afInfo.gmax = afInfo.gmax == 0 ? 50 : afInfo.gmax;
		int maxSize= 50 - 1;
		int nowSize = 0;
		if (!StringUtil.isNullOrEmpty(groupId)) {
			AfGrpProfileInfo mGroupInfo = CacheManager.getInstance().searchGrpProfileInfo(groupId);
			maxSize = mGroupInfo.max_count - 1 <= 0 ? afInfo.gmax - 1 : mGroupInfo.max_count - 1;
			nowSize = mGroupInfo.members.size() - 1;
		} else {
			maxSize = afInfo.gmax == 0 ? 50 - 1 : afInfo.gmax - 1;
		}
		if ("singlechat_page".equals(comePage)) {//singlechat_page从私聊页面添加群
			if(selectedCount > 0) {
				nowSize = nowSize + selectedCount;
			}
			if (selectedCount > 1) {
				//groupCount_text.setVisibility(View.VISIBLE);

				//groupCount_text.setText("(" + nowSize + "/" + maxSize + ")");
				select_button.setVisibility(View.VISIBLE);
			} else {

				select_button.setVisibility(View.GONE);
				//groupCount_text.setVisibility(View.GONE);
			}
			groupCount_text.setText(mGroupChooseMember.replace(ReplaceConstant.TARGET_FOR_REPLACE,"(" + nowSize + "/" + maxSize + ")"));
		} else {
			if (selectedCount > 0) {
				nowSize = nowSize + selectedCount;
				select_button.setVisibility(View.VISIBLE);
			} else {
				select_button.setVisibility(View.GONE);
				//groupCount_text.setVisibility(View.GONE);
			}
			groupCount_text.setText(mGroupChooseMember.replace(ReplaceConstant.TARGET_FOR_REPLACE,"(" + nowSize + "/" + maxSize + ")"));
		}
	}

	/**
	 * 现有的人员数量
	 * @return
	 */
	private int getNowCount() {
		int nowSize = 0;
		if (!StringUtil.isNullOrEmpty(groupId)) {
			AfGrpProfileInfo mGroupInfo = CacheManager.getInstance().searchGrpProfileInfo(groupId);
			nowSize = mGroupInfo.members.size() - 1;
		}
		return nowSize + selectedCount;
	}

	/**
	 * 获取好友数量增加到选则朋友列表里面去
	 */
	@Override
	public void init() {
		AfGrpProfileItemInfo mProfileItemInfo;
		profileItemInfo_list = new ArrayList<AfGrpProfileItemInfo>();
		List<AfFriendInfo> friends_list = CacheManager.getInstance().getFriends_list(Consts.AFMOBI_FRIEND_TYPE_FF);//获取好友列表
		for (AfFriendInfo friendInfo : friends_list) {
			if (!friendInfo.afId.startsWith("r")) {
				mProfileItemInfo = AfFriendInfo.ProfileItemInfoToFriend(friendInfo);
				profileItemInfo_list.add(mProfileItemInfo);
			}
		}
		mAdapter = new FriendsListAdapter(profileItemInfo_list);
		/**
		 * 滑动监听
		 * 何桂明 2013-10-22
		 */
//		mListView.setOnScrollListener(new ImageOnScrollListener(mListView ));
		mListView.setAdapter(mAdapter);
		fromPage();
		if ("singlechat_page".equals(comePage)){
			title_text.setText(R.string.groupchat);
		}
		else {
			title_text.setText(R.string.select_friend);
		}
	}


	/**
	 * 根据搜索框是否可见，初始化搜索框数据 void
	 */
	private void initSearchET(EditText searchET) {
		if (searchET != null) {
			searchET.setText("");
			searchET.setVisibility(View.VISIBLE);
			TextChangeListener textChangeListener = new TextChangeListener();
			searchET.addTextChangedListener(textChangeListener);
		}
	}

	/**
	 * 观察输入框输入的内容
	 */
	public class TextChangeListener implements TextWatcher {
		private int index = 0;

		public void setIndex(int index) {
			this.index = index;
		}

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			if (null == mSearchEditText || null == delSearch) {
				return;
			}
			List<AfGrpProfileItemInfo> searchList = new ArrayList<AfGrpProfileItemInfo>();
			for (AfFriendInfo friendInfo : CacheManager.getInstance().getFriends_list(Consts.AFMOBI_FRIEND_TYPE_FF)) {
//				except palmchat team
				if (friendInfo != null && friendInfo.afId != null && !friendInfo.afId.startsWith("r")) {

					searchList.add(AfFriendInfo.ProfileItemInfoToFriend(friendInfo));
				}
			}

			// 获取输入关键字
			String key = s.toString().trim();
			if (key != null && key.length() > 0) {
				// 在搜索框中输入搜索条件时，文字省略号在前面，防止输入号码过长导致后面的看不到
				mSearchEditText.setEllipsize(TruncateAt.START);
				delSearch.setVisibility(View.VISIBLE);
			} else {
				// 搜索框中显示“共有多少位好友”时，文字省略号在后面，防止看不到全部的人数
				mSearchEditText.setEllipsize(TruncateAt.END);
				delSearch.setVisibility(View.GONE);
			}
			// 关键字非空进行处理
			if (key != null && key.length() > 0 && searchList.size() != 0) {
				key = key.toLowerCase();
				if (s.charAt(0) > 128) {
					ArrayList<AfGrpProfileItemInfo> searchResult = new ArrayList<AfGrpProfileItemInfo>();
					final int length = searchList.size();
					for (int i = 0; i < length; i++) {
						String name =
								searchList.get(i).alias != null && !"".equals(searchList.get(i).alias)
										? searchList.get(i).alias : searchList.get(i).name;
						if (null != name && name.indexOf(key) >= 0) {
							searchResult.add(searchList.get(i));
						}
					}
					mAdapter.setData(searchResult);
					mAdapter.notifyDataSetChanged();
				} else {
					ArrayList<AfGrpProfileItemInfo> searchResult = new ArrayList<AfGrpProfileItemInfo>();
					final int length = searchList.size();
					for (int i = 0; i < length; i++) {
						String name =
								searchList.get(i).alias != null && !"".equals(searchList.get(i).alias)
										? searchList.get(i).alias : searchList.get(i).name;
						if (null != name && name.substring(0).toLowerCase().contains(key)
								|| searchList.get(i).afid.contains(key)) {
							searchResult.add(searchList.get(i));
						}
					}
					mAdapter.setData(searchResult);
					mAdapter.notifyDataSetChanged();
				}
			} else {
				if (searchList.size() != 0) {
					mAdapter.setData(searchList);
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	private void setSelectedItem(AfGrpProfileItemInfo selectedItem) {
		selectedFriendMap.put(selectedItem.afid, selectedItem);
		selectedCount++;
		refreshTitleBar();
	}

	private boolean isCreate = false;
	String names = "";
	int gversion;

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		dismissProgressDialog();
		int key = 0;
		//取消与中间的联系
		/*for(int i = 0; i < mhttpHandle.size(); i++) {
			key = mhttpHandle.keyAt(i);
			if(key != httpHandle){
				mhttpHandle.put(key,Consts.AF_HTTP_HANDLE_INVALID);
			}
		}*/
		if (code == Consts.REQ_CODE_SUCCESS) {
			AfGrpProfileInfo mProfileInfo = null;
			if (null == result) {
				ToastManager.getInstance().show(FriendsActivity.this, R.string.network_unavailable);
				return;
			}
			switch (flag) {
				case Consts.REQ_GRP_CREATE://创建群组
					PalmchatLogUtils.e(TAG, "----Consts.REQ_GRP_CREATE----");
					// heguiming 2013-12-04
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ADD_G_SUCC);
//					MobclickAgent.onEvent(context, ReadyConfigXML.ADD_G_SUCC);

					isCreate = true;
					mProfileInfo = (AfGrpProfileInfo) result;
					List<AfGrpProfileItemInfo> members = null;
					final String[] addMembersName = new String[MAX_ADD_MEMBER];
					if (null != mProfileInfo) {
						members = mProfileInfo.members;
						//addMembersName = new String[members.size()];
						for (int i = 0; i < members.size(); i++) {
							AfGrpProfileItemInfo info = members.get(i);
							members.remove(info);
							String afid = info.afid;
							if (mProfileInfo.admin.equals(afid)) {
								info.isMaster = 1;
							}
							members.add(info);
						}

						mProfileInfo.members = null;
						mProfileInfo.members = (ArrayList<AfGrpProfileItemInfo>) members;
						groupId = mProfileInfo.afid;
						gname = mProfileInfo.name;
						mProfileInfo.tips = true;
						gversion = mProfileInfo.version;
						ArrayList<AfGrpProfileItemInfo> newMembers = new ArrayList<AfGrpProfileItemInfo>();
						for (int i = 0; i < members.size(); i++) {
							AfGrpProfileItemInfo info = members.get(i);
							String myAfid = CacheManager.getInstance().getMyProfile().afId;
							if (!myAfid.equals(info.afid)) {
								newMembers.add(info);
							}
						}
						for (int i = 0; i < newMembers.size(); i++) {
							AfGrpProfileItemInfo info = newMembers.get(i);
							addMembersName[i] = info.name;
						}
					}
					final AfGrpProfileInfo profileInfo = mProfileInfo;
					new Thread(new Runnable() {
						public void run() {
							AfGrpNotifyMsg afGrpNotifyMsg;
							afGrpNotifyMsg = AfGrpNotifyMsg.getAfGrpNotifyMsg(gname, groupId,
									CacheManager.getInstance().getMyProfile().afId, getString(R.string.group_you), null,
									addMembersName, null, gversion, AfMessageInfo.MESSAGE_GRP_CREATE, 0);

							AfMessageInfo afMessageInfo = AfGrpNotifyMsg.toAfMessageInfoForYou(afGrpNotifyMsg, PalmchatApp.getApplication());
							afMessageInfo.client_time = System.currentTimeMillis();
							int msg_id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
							afMessageInfo._id = msg_id;
							mAfMessageInfoTempCreate = afMessageInfo;
							groupHandler.sendMessage(groupHandler.obtainMessage(EditGroupActivity.GROUP_INVITE_SUCCESS, profileInfo));
						}
					}).start();
					break;
				case Consts.REQ_GRP_ADD_MEMBER://添加成员
					dismissProgressDialog();
					mProfileInfo = (AfGrpProfileInfo) result;
					if (null != mProfileInfo) {
						groupId = mProfileInfo.afid;
					}

					if ((!StringUtil.isEmpty(groupId, true))) {
						isCreate = false;
						groupHandler.sendMessage(groupHandler.obtainMessage(EditGroupActivity.GROUP_INVITE_SUCCESS, mProfileInfo));
					}


					final AfGrpProfileInfo profileInfoTemp = mProfileInfo;
					new Thread(new Runnable() {
						public void run() {
							AfGrpNotifyMsg afGrpNotifyMsg;
							afGrpNotifyMsg = AfGrpNotifyMsg.getAfGrpNotifyMsg(profileInfoTemp.name, profileInfoTemp.afid,
									CacheManager.getInstance().getMyProfile().afId, getString(R.string.group_you), null,
									addMembers, null, gversion, AfMessageInfo.MESSAGE_GRP_ADD_MEMBER, 0);

							AfMessageInfo afMessageInfo = AfGrpNotifyMsg.toAfMessageInfoForYou(afGrpNotifyMsg, PalmchatApp.getApplication());
							afMessageInfo.client_time = System.currentTimeMillis();
							int msg_id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
							afMessageInfo._id = msg_id;
							CacheManager.getInstance().getAfMessageInfoList().add(afMessageInfo);
							MessagesUtils.insertMsg(afMessageInfo, true, true);
						}
					}).start();
					break;
			}

		} else {
			switch (flag) {
				case Consts.REQ_GRP_CREATE:
					// heguiming 2013-12-04
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ADD_G_SUCC);
//					MobclickAgent.onEvent(context, ReadyConfigXML.ADD_G_SUCC);
				case Consts.REQ_GRP_ADD_MEMBER:
					if (code == Consts.REQ_CODE_CREATE_MEMBER_LIMIT || code == Consts.REQ_CODE_MEMBER_EXIST) {
						String afids[] = (String[]) result;
						String string = getString(R.string.unable_to_add);
						String XXXX = "";
						if (null != afids && afids.length > 0) {
							int size = afids.length;
							if (afids.length < 3) {
								for (int i = 0; i < size; i++) {
									AfFriendInfo info = CacheManager.getInstance().searchAllFriendInfo(afids[i]);
									if (null != info) {
										XXXX += info.name + ",";
									}
								}
								string = string.replace("XXXX", XXXX);
								if (string.contains(",")) {
									names = string.substring(0, string.lastIndexOf(","));
								}
							} else {
								for (int i = 0; i < 2; i++) {
									AfFriendInfo info = CacheManager.getInstance().searchAllFriendInfo(afids[i]);
									if (null != info) {
										XXXX += info.name + ",";
									}

								}
								if (string.contains(",")) {
									names = string.substring(0, string.lastIndexOf(","));
								}
							}
						}
					}
					break;
				case Consts.REQ_GRP_LIST:
				case Consts.REQ_GRP_GET_PROFILE:
					break;
				// 群成员达到上限
				case Consts.REQ_CODE_CREATE_MEMBER_LIMIT:
					break;
				default:
					break;
			}
			dismissProgressDialog();

			if (code == Consts.REQ_CODE_CREATE_MEMBER_LIMIT) {
				if (getNowCount() > getMaxCount()) {
					Consts.getInstance().showToast(FriendsActivity.this, code, flag, http_code);
				} else {
					ToastManager.getInstance().show(FriendsActivity.this, names);
				}
			}else if (code == Consts.REQ_CODE_MEMBER_EXIST) {
				ToastManager.getInstance().show(FriendsActivity.this, names);
			}
			else {
				Consts.getInstance().showToast(FriendsActivity.this, code, flag, http_code);
			}
			selectedFriendMap.putAll(friendMap);
		}

	}

	/**
	 * 最大的人员数量
	 * @return
	 */
	private int getMaxCount() {
		int maxSize;
		AfProfileInfo afInfo = CacheManager.getInstance().getMyProfile();
		afInfo.gmax = afInfo.gmax == 0 ? 50 : afInfo.gmax;
		if (!StringUtil.isNullOrEmpty(groupId)) {
			AfGrpProfileInfo mGroupInfo = CacheManager.getInstance().searchGrpProfileInfo(groupId);
			maxSize = mGroupInfo.max_count - 1;
		} else {
			maxSize = afInfo.gmax == 0 ? 50 - 1 : afInfo.gmax - 1;
		}
		return maxSize;
	}

	/**
	 * 获取到邀请人员的name
	 * @param members 人员的数量
	 * @param member 所以人员的name 拼接起来作为返回参数
	 * @return
	 */
	private String sortGroupName(List<AfGrpProfileItemInfo> members,
								 String member) {
		if (members.size() >= 3) {
			StringBuffer buffer = new StringBuffer();
			if (null != members.get(0).name && !"".equals(members.get(0).name)) {
				buffer = buffer.append(members.get(0).name).append(",");
			} else {
				buffer = buffer.append(members.get(0).afid).append(",");
			}
			if (null != members.get(members.size() - 2).name && !"".equals(members.get(members.size() - 2).name)) {
				buffer = buffer.append(members.get(members.size() - 2).name).append(",");
			} else {
				buffer = buffer.append(members.get(members.size() - 2).afid).append(",");
			}
			if (null != members.get(members.size() - 1).name && !"".equals(members.get(members.size() - 1).name)) {
				buffer = buffer.append(members.get(members.size() - 1).name).append(",");
			} else {
				buffer = buffer.append(members.get(members.size() - 1).afid).append(",");
			}
			member = buffer.substring(0, buffer.lastIndexOf(","));
		}
		return member;
	}


	String[] addMembers;

	/**
	 * 邀请成员
	 * @param groupId
	 * @return
	 */
	public int inviteMember(String groupId) {
		List<AfGrpProfileItemInfo> lists = selectNewMapToList();
		String afid[] = new String[lists.size()];
		StringBuffer buffer = new StringBuffer();

		addMembers = new String[lists.size()];
		for (int i = 0; i < lists.size(); i++) {
			afid[i] = lists.get(i).afid;
			buffer = buffer.append(afid[i]).append(",");
			addMembers[i] = lists.get(i).name;
		}
		String members = buffer.substring(0, buffer.lastIndexOf(","));
		int req_flag = Consts.REQ_GRP_ADD_MEMBER;
		int handle = mAfCorePalmchat.AfHttpGrpOpr(members, null, groupId, req_flag, null, this);
//		mhttpHandle.put(handle,handle);
		return handle;
	}

	/**
	 * 创建群组
	 */
	public void createGroup() {
		//是否包含自己
		boolean isContainMe = false;
		AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
		List<AfGrpProfileItemInfo> lists = selectHashMapToList();
		String afid[] = new String[lists.size()];
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < lists.size(); i++) {
			afid[i] = lists.get(i).afid;
			if(afid[i].equals(profileInfo.afId)){
				isContainMe = true;
			}
			buffer = buffer.append(afid[i]).append(",");
		}
		groupName = "";
		if(!isContainMe){
			AfGrpProfileItemInfo recipient = new AfGrpProfileItemInfo();
			recipient.afid = profileInfo.afId;
			recipient.name = profileInfo.name;
			recipient.sex = profileInfo.sex;
			recipient.age = profileInfo.age;
			lists.add(0,recipient);
		}
		groupName = sortGroupName(lists,groupName);
		String members = buffer.substring(0, buffer.lastIndexOf(","));
		int req_flag = Consts.REQ_GRP_CREATE;
		int handle = mAfCorePalmchat.AfHttpGrpOpr(members, groupName, null, req_flag, null, this);
//		mhttpHandle.put(handle,handle);
	}

	/**
	 * 把要添加的联系人hashMap转换为ArrayList 方法表述
	 *
	 * @return ArrayList<Recipient>
	 */
	public ArrayList<AfGrpProfileItemInfo> selectHashMapToList() {
		ArrayList<AfGrpProfileItemInfo> locList = new ArrayList<AfGrpProfileItemInfo>();
		Collection<AfGrpProfileItemInfo> c = selectedFriendMap.values();
		Iterator<AfGrpProfileItemInfo> it = c.iterator();
		while (it.hasNext()) {
			AfGrpProfileItemInfo temLoc = (AfGrpProfileItemInfo) it.next();
			locList.add(temLoc);
		}
		return locList;
	}

	/**
	 * 把要添加的联系人hashMap转换为ArrayList 方法表述
	 *
	 * @return ArrayList<Recipient>
	 */
	public ArrayList<AfGrpProfileItemInfo> selectNewMapToList() {
		friendMap.clear();
		friendMap.putAll(selectedFriendMap);
		for (int i = 0; i < chatList.size(); i++) {
			selectedFriendMap.remove(chatList.get(i));
		}

		ArrayList<AfGrpProfileItemInfo> locList = new ArrayList<AfGrpProfileItemInfo>();
		Collection<AfGrpProfileItemInfo> c = selectedFriendMap.values();
		Iterator<AfGrpProfileItemInfo> it = c.iterator();
		while (it.hasNext()) {
			AfGrpProfileItemInfo temLoc = (AfGrpProfileItemInfo) it.next();
			locList.add(temLoc);
		}
		return locList;
	}


	/**
	 * 退出此界面
	 */
	private void onBack() {
		CommonUtils.closeSoftKeyBoard(mSearchEditText);
		finish();
	/*	int at = 0;
		//取消与中间的联系
		for(int i = 0; i < mhttpHandle.size(); i++) {
			at = mhttpHandle.keyAt(i);
			if(mAfCorePalmchat != null && at != Consts.AF_HTTP_HANDLE_INVALID){
				mAfCorePalmchat.AfHttpCancel(at);
			}
		}*/
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBack();
			return true;
		}
		return false;
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return false;
	}


	/*@Override
	public void reqCode(int code, int flag) {
		switch (code) {
			case Consts.REQ_CODE_MEMBER_EXIST:
				ToastManager.getInstance().show(FriendsActivity.this, names);
				break;
			default:
				break;
		}

	}*/

}