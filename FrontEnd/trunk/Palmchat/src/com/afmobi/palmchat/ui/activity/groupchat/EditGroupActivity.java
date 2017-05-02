package com.afmobi.palmchat.ui.activity.groupchat;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.LaunchActivity;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.friends.FriendsActivity;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity.BundleKeys;
import com.afmobi.palmchat.ui.activity.main.helper.HelpManager;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.AppDialog.OnInputDialogListener;
import com.afmobi.palmchat.ui.customview.MyGridView;
import com.afmobi.palmchat.ui.customview.ScrollViewExtend;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.ToastManager;
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
import com.core.listener.AfHttpSysListener;
//import com.umeng.analytics.MobclickAgent;

/**
 * 群组设置界面
 * 
 * @author heguiming 2013-10-26
 * 
 */
@SuppressLint("ShowToast")
public class EditGroupActivity extends BaseActivity implements
		AfHttpResultListener, AfHttpSysListener, OnClickListener,
		OnItemClickListener, OnItemLongClickListener, OnCheckedChangeListener {

	private static final String TAG = EditGroupActivity.class.getCanonicalName();
	
	/*
	 * 创建群组
	 */
	public static final int GROUP_CREATE = 4;
	
	/*
	 * 创建群组失败
	 */
	public static final int GROUP_CREATE_FAIL = 5;
	
	/*
	 * 邀请成员加入群组
	 */
	public static final int GROUP_INVITE = 6;
	
	/*
	 * 邀请成员加入群组失败
	 */
	public static final int GROUP_INVITE_FAIL = 7;

	/*
	 * 邀请成员加入群组成功
	 */
	public static final int GROUP_INVITE_SUCCESS = 8;
	
	/*
	 * 修改群组名称失败
	 */
	public static final int GROUP_SET_NAME_FAIL = 27;

	/*
	 * 设置群组名字成功后显示群组名字
	 */
	public static final int GROUP_NAME_CHANGED = 17;

	
	public static final int REQUEST_CODE_ADD_FRIEND = 8000;
	private static final int CLEAR_HISTORY = 8001;
	private static final int QUIT = 8002;
	private static final int DELETE_MEMBER = 8003;
	private static final int SHOW_PROFILE = 8004;
	private static final int NO_DATA = 8005;
	public static final String PROFILE_INFO = "mProfileInfo";

	private Toast toast;
	/**
	 * 是否修改了群名
	 */
	private boolean isEditGroupName;
	/**
	 * Looper线程
	 */
	private LooperThread looperThread;

	// 控件定义
	private TextView mTitleTxt;
	private MyGridView mMemberGridView;
	private  ImageView[] mGroupHeadAry = new  ImageView[3];
	private ImageView mBackImg;
	private TextView mGroupName;
	private TextView mGroupMemberNum;
	private View mGroupNameLayout, mGroupNofityLayout,mGroupStatusLayout,lin_oth,lin_admin,more_layout,rl_edit_group_info;
	private Button mClearHistoryBtn, mQuitGroupBtn;
	private TextView mEditGroupName, mEditGroupStatus,mEditGroupName_oth,mGroupSig_oth, mGroupSig;
	private ImageView mMemberMore;
	private CheckBox mNotifyBox;
	private LinearLayout mAttrLayout;
	private View mChatBtn;
	private boolean isForwad;
	// 全局对象定义
	public AfPalmchat mAfCorePalmchat;
	private AfGrpProfileInfo mGroupInfo;
	private AfProfileInfo myProfile;

	private EditGroupAdapter editGroupAdapter;
	private LinearLayout roundLayout;
	
//	private ScrollViewExtend mScrollView;
	
	private ScrollView gv_sv;

	// bundle 传过来的值
	// group 的 afid
	private String groupId;
	private boolean selfIsMaster, clearGroupMsgSuccess, isShowMore;
	private String mGameOrSign = "";
	private String deleteMemberAfid;
	private String mGrpName;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CLEAR_HISTORY:
				dismissProgressDialog();
				break;
			case NO_DATA:
				dismissProgressDialog();
				if (null != toast) {
					toast.setText(R.string.groupchat_has_no_history);
					toast.show();
				}
			case Consts.REQ_GRP_GET_PROFILE:
				showMemberData(mGroupInfo);
				break;
			case QUIT:
				Intent data = new Intent();
				Bundle bundle = new Bundle();
				bundle.putBoolean(JsonConstant.KEY_QUIT_GROUP, true);
				data.putExtras(bundle);
				setResult(0, data);
				finish();
				PalmchatApp.getApplication().mMemoryCache.evictAll();
				if(isForwad) {
					Stack<Activity> activityStack = MyActivityManager.getActivityStack();
					int acSize = activityStack.size();
					if (acSize > 0) {
						for (int i = acSize - 1; i >= 0; i--) {
							Activity act = activityStack.get(i);
							if (!(act instanceof MainTab || act instanceof Chatting || act instanceof GroupChatActivity)) {
								act.finish();
							}
						}
					}
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		looperThread = new LooperThread();
		looperThread.setName(TAG);
		looperThread.start();
		super.onCreate(savedInstanceState);
		// heguiming 2013-12-04
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SEE_G_INFO);
//		MobclickAgent.onEvent(context, ReadyConfigXML.SEE_G_INFO);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
//		MobclickAgent.onPageStart("EditGroupActivity");
//	    MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd("EditGroupActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
//	    MobclickAgent.onPause(this);
	}

	@Override
	public void findViews() {

		setContentView(R.layout.activity_edit_group);
		isForwad = getIntent().getBooleanExtra("isForward",false);
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

		mTitleTxt = (TextView) findViewById(R.id.title_text);
		mTitleTxt.setText(R.string.group_details);
		mMemberGridView = (MyGridView) findViewById(R.id.gridview);
		mMemberGridView.setOnItemClickListener(this);
		mMemberGridView.setOnItemLongClickListener(this);

		mGroupHeadAry[0] = ( ImageView) findViewById(R.id.group_head_1);
		mGroupHeadAry[1] = ( ImageView) findViewById(R.id.group_head_2);
		mGroupHeadAry[2] = (ImageView) findViewById(R.id.group_head_3);
//		mGroupHeadAry[3] = (ImageView) findViewById(R.id.group_head_4);

		mGroupName = (TextView) findViewById(R.id.groupName);
		mGroupMemberNum = (TextView) findViewById(R.id.attr_title);

		mBackImg = (ImageView) findViewById(R.id.back_button);
		mBackImg.setOnClickListener(this);
		
		lin_admin =  findViewById(R.id.lin_andmin);
		lin_oth =  findViewById(R.id.lin_oth);
		
		mGroupNameLayout =  findViewById(R.id.editmygroup_groupname_row);
		rl_edit_group_info = findViewById(R.id.rl_edit_group_info);
		mGroupNameLayout.setOnClickListener(this);
		mGroupNofityLayout =  findViewById(R.id.editmygroup_notification_row);
		mGroupNofityLayout.setOnClickListener(this);
		mGroupStatusLayout =  findViewById(R.id.editmygroup_groupstatus_row);
		mGroupStatusLayout.setOnClickListener(this);
		mClearHistoryBtn = (Button) findViewById(R.id.clear_history_btn);
		mClearHistoryBtn.setOnClickListener(this);
		mQuitGroupBtn = (Button) findViewById(R.id.quit_group_btn);
		mQuitGroupBtn.setOnClickListener(this);
//		mScrollView = (ScrollViewExtend) findViewById(R.id.editmygroup_member_scroll);
		
		gv_sv = (ScrollView) findViewById(R.id.gv_sv);
		
		mEditGroupName = (TextView) findViewById(R.id.editmygroup_groupname);
		mEditGroupStatus = (TextView) findViewById(R.id.group_status);
		mEditGroupName_oth = (TextView) findViewById(R.id.editmygroup_groupname_oth);
		mGroupSig = (TextView) findViewById(R.id.group_signature_txt);
		mGroupSig_oth = (TextView) findViewById(R.id.group_signature_txt_oth);
		
		mEditGroupName.setFocusable(true);
		mEditGroupName.setFocusableInTouchMode(true);
		mEditGroupName.requestFocus();

		mEditGroupName_oth.setFocusable(true);
		mEditGroupName_oth.setFocusableInTouchMode(true);
		mEditGroupName_oth.requestFocus();
		
		more_layout =  findViewById(R.id.more_layout);
		mMemberMore = (ImageView) findViewById(R.id.editgroup_member_more);
		mMemberMore.setOnClickListener(this);
		more_layout.setOnClickListener(this);
		
		mNotifyBox = (CheckBox) findViewById(R.id.editmygroup_notification_img);
		mNotifyBox.setOnCheckedChangeListener(this);
		
		mAttrLayout = (LinearLayout) findViewById(R.id.attr_layout);
		roundLayout = (LinearLayout) findViewById(R.id.profile_layout);
//		Drawable drawable = FileUtils.getImageFromAssetsFile(context, SharePreferenceUtils.getInstance(context).getProfileAblum());
//		roundLayout.setBackgroundDrawable(drawable);
		
		mChatBtn =  findViewById(R.id.profile_chat);
		mChatBtn.setOnClickListener(this);
		
	}

	@Override
	public void init() {
		// 注册页面Handler
		Bundle bundle = getIntent().getExtras();

		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		myProfile = CacheManager.getInstance().getMyProfile();
		

		if (bundle != null) {
			groupId = bundle.getString(BundleKeys.ROOM_ID);
			// 搜索内存中是否有群数据
			mGroupInfo = CacheManager.getInstance().searchGrpProfileInfo(groupId);
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					mGroupInfo = loadGroupMembers();    
					updateGroupFromServer(mGroupInfo);
					showMemberData(mGroupInfo);
				}
			}, 300);
			boolean flag = mAfCorePalmchat.AfDbSettingGetTips(groupId);
			PalmchatLogUtils.e(TAG, "----init----flag:" + flag);
			mNotifyBox.setChecked(flag);
			
			if (mGroupInfo != null) {
				String name = mGroupInfo == null ? "" : mGroupInfo.name;
				if (name == null || name.equals("")) {
					name = bundle.getString(BundleKeys.ROOM_NAME);
				}
				mGroupName.setText(name);
				mEditGroupName.setText(name);
				mEditGroupName_oth.setText(name);
				
				String sig = mGroupInfo == null ? "" : mGroupInfo.sig;
				CharSequence text = EmojiParser.getInstance(context).parse(sig, ImageUtil.dip2px(context, 18));
				
				mGroupSig.setText(text);
				mGroupSig_oth.setText(text);
				// 判断自己是否是群组
				String admin = mGroupInfo.admin;
				if (!TextUtils.isEmpty(admin)) {
					if (admin.equals(myProfile.afId)) {
						lin_admin.setVisibility(View.VISIBLE);
						lin_oth.setVisibility(View.GONE);
					}else {
						lin_admin.setVisibility(View.GONE);
						lin_oth.setVisibility(View.VISIBLE);
					}
				}
				mGroupMemberNum.setText(getString(R.string.group_members) + "(" + String.valueOf(mGroupInfo.count) + ")");
			}
		}
	}
	
	private AfGrpProfileInfo loadGroupMembers() {
		// 搜索内存中是否有群数据
		AfGrpProfileInfo mGroupInfo = CacheManager.getInstance().searchGrpProfileInfo(groupId);	
		if (null != mGroupInfo) {
			ArrayList<AfGrpProfileItemInfo> members = mGroupInfo.members;
			for (int i = 0; i < members.size(); i++) {
				AfGrpProfileItemInfo itemInfo = members.get(i);
				AfFriendInfo friend = CacheManager.getInstance().searchAllFriendInfo(itemInfo.afid);
				if (null != friend) {
					String alias = friend.alias;
					if (null != alias && !"".equals(alias)) {
						itemInfo.alias = alias;
					}
					if(TextUtils.isEmpty(itemInfo.head_image_path) && !TextUtils.isEmpty(friend.head_img_path)){//若群成员地址为空则赋值
						itemInfo.head_image_path = friend.head_img_path;
					}
				}
			}
		}
		return mGroupInfo;
	}

	/**
	 * 显示数据到界面
	 */
	private void showMemberData(AfGrpProfileInfo mGroupInfo) {
		// 先显示有信息的群成员信息，后台加载没有信息的成员后更新
		if (null != mGroupInfo) {
			ArrayList<AfGrpProfileItemInfo> members = new ArrayList<AfGrpProfileItemInfo>();
			if (!mGroupInfo.members.isEmpty()) {
				// 复制成员，防止后面接口不回时给这个对象的更改出现数据不一致
				members.addAll(mGroupInfo.members);
				judgeMaster(mGroupInfo, members);
				
				int size = members.size();
				if(size >= 8){
					more_layout.setVisibility(View.VISIBLE);
				}else{
					more_layout.setVisibility(View.GONE);
				}
//				int loadHeadSize = size > mGroupHeadAry.length ? mGroupHeadAry.length : size;
//				// 群头像只有4个
//				for (int i = 0; i < loadHeadSize; i++) {
//					AfGrpProfileItemInfo member = members.get(i);
//					String name = member.alias == null ? member.name : member.alias;
//					// name is empty,so afid
//					if (StringUtil.isNullOrEmpty(name)) {
//						name = member.afid.replace("a", "");
//					}
//					AvatarImageInfo imageInfo = new AvatarImageInfo(member.getAfidFromHead(),
//							member.getSerialFromHead(), name, Consts.AF_HEAD_MIDDLE, member.getServerUrl(), member.sex);
//					ImageLoader.getInstance().displayImage(mGroupHeadAry[i], imageInfo);
//				}
				loadView(mGroupInfo, size);
			}
			setAdapter(members);
		}
	}

	/**
	 * 加载数据到View
	 * @param mGroupInfo
	 */
	private void loadView(AfGrpProfileInfo mGroupInfo, int size) {
		mGroupName.setText(mGroupInfo.name);
		mEditGroupName.setText(mGroupInfo.name);
		mEditGroupName_oth.setText(mGroupInfo.name);
		
		mGroupMemberNum.setText(getString(R.string.group_members) + "(" + String.valueOf(size) + ")");
		if (!StringUtil.isNullOrEmpty(mGroupInfo.sig)) {
			CharSequence text = EmojiParser.getInstance(context).parse(mGroupInfo.sig, ImageUtil.dip2px(context, 18));
			mGroupSig.setText(text);
			mGroupSig_oth.setText(text);
		} else {
			mGroupSig.setText("");
			mGroupSig_oth.setText("");
		}
		
		mEditGroupStatus.setText(mGroupInfo.sig);
		mNotifyBox.setChecked(mGroupInfo.tips);
		
		if (!selfIsMaster) {
			lin_admin.setVisibility(View.GONE);
			lin_oth.setVisibility(View.VISIBLE);
			mGroupStatusLayout.setVisibility(View.GONE);
			mQuitGroupBtn.setText(R.string.member_leave);
			rl_edit_group_info.setOnClickListener(null);
		} else {
			lin_admin.setVisibility(View.VISIBLE);
			lin_oth.setVisibility(View.GONE);
			rl_edit_group_info.setOnClickListener(this);
			mQuitGroupBtn.setText(R.string.editmygroup_destroy);
		}
	}

	/**
	 * 判定群主并进行排序，群主放在第一位
	 * 
	 * @param mGroupInfo
	 */
	private void judgeMaster(AfGrpProfileInfo mGroupInfo,
			ArrayList<AfGrpProfileItemInfo> members) {

		int size = members.size();
		String admin = mGroupInfo.admin;
		if (!StringUtil.isNullOrEmpty(admin)) {
			boolean isMaster = false;
			for (int i = 0; i < size; i++) {
				AfGrpProfileItemInfo member = members.get(i);
				// 加载自己的信息
				if (null != myProfile && myProfile.afId.equals(member.afid)) {
					member.head_image_path = myProfile.head_img_path;
					member.name = myProfile.name;
				}
				if (!isMaster && admin.equals(member.afid)) {
					member.isMaster = AfGrpProfileItemInfo.IS_MASTER;
					members.remove(member);
					members.add(0, member);
					isMaster = true;			
					// 判断自己是否是群组
					if (admin.equals(myProfile.afId)) {
						selfIsMaster = true;
					}
				}
			}
		}
	}

	/**
	 * 设置成员头像的GridView适配器
	 * 
	 * @param dataList
	 */
	private void setAdapter(ArrayList<AfGrpProfileItemInfo> dataList) {
		mMemberMore.setVisibility(View.VISIBLE);
		dataList.add(new AfGrpProfileItemInfo());
		int size = dataList.size();
		editGroupAdapter = new EditGroupAdapter(this, dataList);
		mMemberGridView.setAdapter(editGroupAdapter);
		setGridViewParams(size);
	}

	private void setGridViewParams(int size) {
		View item = editGroupAdapter.getView(0, null, mMemberGridView);
		item.measure(0, 0);
		int height = item.getMeasuredHeight();
		if (size <= 4) {
			gv_sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, height));
		} else if (size <= 8) {
			gv_sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 2 * height));
		} else {
			if (isShowMore) {
				gv_sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			} else {
				gv_sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 2 * height));
			}
		}

		// Log.i("EditGroupActivity", height + "");
		// if (!isShowMore && size > 8) {
		// int h = ImageUtil.dip2px(context, 200);
		// gv_sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, h));
		// } else {
		// gv_sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT));
		// }

		// if (size <= 4) {
		// int h = ImageUtil.dip2px(context, 80);
		// gv_sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, h));
		// } else if (size <= 8) {
		// int h = ImageUtil.dip2px(context, 160);
		// gv_sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, h));
		// } else {
		// if (isShowMore) {
		// // if (size <= 12) {
		// // int h = ImageUtil.dip2px(context, 240);
		// // gv_sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// h));
		// // } else {
		// // int h = ImageUtil.dip2px(context, 320);
		// // gv_sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// h));
		// // }
		// gv_sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT));
		// } else {
		// int h = ImageUtil.dip2px(context, 160);
		// gv_sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, h));
		// }
		// }
	}

	/**
	 * 更新服务器群名片
	 * 
	 * @return
	 */
	private void updateGroupFromServer(AfGrpProfileInfo mGroupInfo) {
		if (null != mGroupInfo) {
			ArrayList<AfGrpProfileItemInfo> members = mGroupInfo.members;
			if (!members.isEmpty()) {
				int size = members.size();
				boolean canLoadFromServer = false;
				boolean hasMaster = false;
				// 主要查询一些没有信息的组成员信息
				for (int i = 0; i < size; i++) {
					AfGrpProfileItemInfo member = members.get(i);
					String afid = member.afid;
					// 不是自己
					if (!StringUtil.isNullOrEmpty(member.afid)
							&& !afid.equals(myProfile.afId)) {
						// 判断名字是否为空
						if (StringUtil.isNullOrEmpty(member.name)) {
							canLoadFromServer = true;
							break;
						}
						if (member.isMaster == AfGrpProfileItemInfo.IS_MASTER) {
							hasMaster = true;
							break;
						}
					}
				}
				if (canLoadFromServer || !hasMaster) {
					PalmchatLogUtils.i(TAG, "EditGroupActivity----:group members is update!!!");
					mAfCorePalmchat.AfHttpGrpOpr(null, mGroupInfo.name, mGroupInfo.afid, Consts.REQ_GRP_GET_PROFILE, null, this);
				}
			}
			// 成员为空时刚表示成员列表没有加载出来需要查询整个组成员的信息
			else {
				PalmchatLogUtils.i(TAG, "EditGroupActivity----:group members is empty!!!");
				mAfCorePalmchat.AfHttpGrpOpr(null, mGroupInfo.name, mGroupInfo.afid,
							Consts.REQ_GRP_GET_PROFILE, null, this);
			}
//			// 现在是服务器不会主动push更新的信息下来，所以需要每次都主动去获取
//			PalmchatLogUtils.i(TAG, "EditGroupActivity----:group members is empty!!!");
//			mAfCorePalmchat.AfHttpGrpOpr(null, mGroupInfo.name, mGroupInfo.afid,
//						Consts.REQ_GRP_GET_PROFILE, null, this);
		}
	}

	@Override
	public boolean AfHttpSysMsgProc(int msg, Object wparam, int lParam) {
		return false;
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result,Object user_data) {
		dismissProgressDialog();
		if (code == Consts.REQ_CODE_SUCCESS) {
			afOnResultSuccess(flag, result,user_data);
		} else {
			PalmchatLogUtils.e(TAG, "----AfOnResult Fail !!!" + code + "--Flag--" + flag);
			if (flag == Consts.REQ_GRP_REMOVE_MEMBER) {
				// user is not in group
				if (code == Consts.REQ_CODE_REMOVE_MEMBERS_NONE
						|| code == Consts.REQ_CODE_GROUP_NOT_EXIST
						|| code == Consts.REQ_CODE_NO_GROUP) {
					if (!StringUtil.isNullOrEmpty(deleteMemberAfid)) {
						ArrayList<AfGrpProfileItemInfo> members = mGroupInfo.members;
						for (int i = 0; i < members.size(); i++) {
							if (members.get(i).afid.equals(deleteMemberAfid)) {
								members.remove(i);
							}
						}
						sendUpdateCatcheMsg(mGroupInfo);
					}
				} else {
					toast.setText(R.string.failure);
					toast.show();
				}
			}else if(flag == Consts.REQ_GRP_QUIT && code == Consts.REQ_CODE_ILLEGAL_USER_G){
				if (null != looperThread.handler) {
					looperThread.handler.sendEmptyMessage(QUIT);
				}
			}else {
				toast.setText(R.string.failure);
				toast.show();
			}
		}
	}
	

	/**
	 * 请求成功情况
	 */
	private void afOnResultSuccess(final int flag, final Object result,final Object user_data) {
		
		PalmchatLogUtils.e(TAG, "----flag:" + flag);
		switch (flag) {
		case Consts.REQ_GRP_MODIFY:
		case Consts.REQ_GRP_REMOVE_MEMBER:
			toast.setText(R.string.success);
			toast.show();
			new Thread(new Runnable() {
				public void run() {
					AfGrpNotifyMsg afGrpNotifyMsg = null;
					if(flag == Consts.REQ_GRP_MODIFY){
						final int modify_type = (Integer) user_data;
						if(AfGrpNotifyMsg.MODIFY_TYPE_NAME == modify_type){
							afGrpNotifyMsg = AfGrpNotifyMsg.getAfGrpNotifyMsg(mGameOrSign, mGroupInfo.afid, 
									CacheManager.getInstance().getMyProfile().afId, getString(R.string.group_you), mGroupInfo.sig, 
									null, null, mGroupInfo.version, AfMessageInfo.MESSAGE_GRP_MODIFY , modify_type);
							
							AfGrpProfileInfo afGrpProfileInfo = CacheManager.getInstance().searchGrpProfileInfo(groupId);
							if (afGrpProfileInfo != null) {
								afGrpProfileInfo.name = mGrpName;
								CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).saveOrUpdate(afGrpProfileInfo, true, true);
							}
							isEditGroupName = true;
						}else{//(AfGrpNotifyMsg.MODIFY_TYPE_SIGN == modify_type)
							afGrpNotifyMsg = AfGrpNotifyMsg.getAfGrpNotifyMsg(mGroupInfo.name, mGroupInfo.afid, 
									CacheManager.getInstance().getMyProfile().afId, getString(R.string.group_you), mGameOrSign, 
									null, null, mGroupInfo.version, AfMessageInfo.MESSAGE_GRP_MODIFY , modify_type);
						}
					}else{
						String[] delMembers = (String[]) user_data;
						afGrpNotifyMsg = AfGrpNotifyMsg.getAfGrpNotifyMsg(mGroupInfo.name, mGroupInfo.afid, 
								CacheManager.getInstance().getMyProfile().afId, getString(R.string.group_you), mGroupInfo.sig, 
								delMembers, null, mGroupInfo.version, AfMessageInfo.MESSAGE_GRP_REMOVE_MEMBER , 0);
					}
					AfMessageInfo afMessageInfo = AfGrpNotifyMsg.toAfMessageInfoForYou(afGrpNotifyMsg,PalmchatApp.getApplication());
					afMessageInfo.client_time = System.currentTimeMillis();
					int msg_id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
					afMessageInfo._id = msg_id;
					if(afGrpNotifyMsg.type != AfMessageInfo.MESSAGE_GRP_DESTROY) {
						CacheManager.getInstance().getAfMessageInfoList().add(afMessageInfo);
					}
					MessagesUtils.insertMsg(afMessageInfo, true, true);
				}
			}).start();
		 case Consts.REQ_GRP_GET_PROFILE:
			if (null != result) {
				AfGrpProfileInfo groupInfo = (AfGrpProfileInfo) result;
				
				PalmchatLogUtils.e(TAG, "----Sign:" + groupInfo.sig);
				// 如果是第一次加载则notify默认为true
				groupInfo.tips = mAfCorePalmchat.AfDbSettingGetTips(groupInfo.afid);
				
				groupId = mGroupInfo.afid;
				// 服务器不更改此值
				groupInfo.tips = mGroupInfo == null ? true : mGroupInfo.tips;
				sendUpdateCatcheMsg(groupInfo);
			}
			break;
		case Consts.REQ_GRP_QUIT:
		case Consts.REQ_GRP_ADMIN_QUIT:
			if(flag == Consts.REQ_GRP_ADMIN_QUIT) {
				toast.setText(R.string.remove_group);
			}
			else{
				toast.setText(R.string.quit_group);
			}
			toast.show();
			
			if (null != looperThread.handler) {
				looperThread.handler.sendEmptyMessage(QUIT);
			}
			
			new Thread(new Runnable() {
				public void run() {
					AfGrpNotifyMsg afGrpNotifyMsg;
					if(flag == Consts.REQ_GRP_QUIT){
						afGrpNotifyMsg = AfGrpNotifyMsg.getAfGrpNotifyMsg(mGroupInfo.name, mGroupInfo.afid, 
								CacheManager.getInstance().getMyProfile().afId, getString(R.string.group_you), mGroupInfo.sig, 
								null, null, mGroupInfo.version, AfMessageInfo.MESSAGE_GRP_DROP , 0);
					}else{
						afGrpNotifyMsg = AfGrpNotifyMsg.getAfGrpNotifyMsg(mGroupInfo.name, mGroupInfo.afid, 
								CacheManager.getInstance().getMyProfile().afId, getString(R.string.group_you), mGroupInfo.sig, 
								null, null, mGroupInfo.version, AfMessageInfo.MESSAGE_GRP_DESTROY , 0);
					}
					AfMessageInfo afMessageInfo = AfGrpNotifyMsg.toAfMessageInfoForYou(afGrpNotifyMsg, PalmchatApp.getApplication());
					afMessageInfo.client_time = System.currentTimeMillis();
					int msg_id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
					afMessageInfo._id = msg_id;
					if(afGrpNotifyMsg.type != AfMessageInfo.MESSAGE_GRP_DESTROY) {
						CacheManager.getInstance().getAfMessageInfoList().add(afMessageInfo);
					}
					MessagesUtils.insertMsg(afMessageInfo, true, true);
				}
			}).start();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 发送消息到looper中更新
	 * @param groupInfo
	 */
	private void sendUpdateCatcheMsg(AfGrpProfileInfo groupInfo) {
		Message msg = new Message();
		msg.what = LooperThread.UPDATE_CATCH;
		msg.obj = groupInfo;
		looperThread.handler.sendMessage(msg);
	}

	/**
	 * 请求失败情况
	 */
	private void afOnResultFail(int flag, Object result) {
		switch (flag) {
		case Consts.REQ_GRP_GET_PROFILE:
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			Bundle bundle = new Bundle();
			bundle.putBoolean("isCreate", false);
			bundle.putString("ROOMNAME", mEditGroupName.getText().toString());
			bundle.putBoolean("clearGroupMsgSuccess", clearGroupMsgSuccess);
			bundle.putBoolean("isEditGroupName", isEditGroupName);
			Intent data = new Intent();
			data.putExtras(bundle);
			setResult(0, data);
			finish();
			break;
		case R.id.editmygroup_groupname_row:
			String name = mGroupInfo == null ? "" : mGroupInfo.name;
			showEditDialog(getString(R.string.editmygroup_groupname), name == null ? "" : name);
			break;
		case R.id.editmygroup_notification_row:
			mNotifyBox.setChecked(!mNotifyBox.isChecked());
			break;
		case R.id.editmygroup_groupstatus_row:
		case R.id.rl_edit_group_info:
			String sig = mGroupInfo == null ? "" : mGroupInfo.sig;

			AppDialog dialog = new AppDialog(this);
			dialog.createEditpEmotionDialog(this, getString(R.string.group_intro), sig, new OnInputDialogListener() {
				@Override
				public void onRightButtonClick(String inputStr) {
					if (null != mGroupInfo) {
						if(inputStr==null||inputStr.length()<1){
							showToast(R.string.failed);
						}else{
							showProgressDialog(R.string.please_wait);
							mGameOrSign = inputStr;
							mAfCorePalmchat.AfHttpGrpUpdateSigAndName(null, inputStr, mGroupInfo.afid, AfGrpNotifyMsg.MODIFY_TYPE_SIGN, EditGroupActivity.this);
						}
					}
				}
				@Override
				public void onLeftButtonClick() {

				}
			});
			dialog.show();
			break;
		case R.id.clear_history_btn:
			showConfirmDialog(getString(R.string.clear_chat_history), null, null,CLEAR_HISTORY);
			break;
		case R.id.quit_group_btn:
			String message = "";
			if (selfIsMaster) {
				message = getString(R.string.editmygroup_destroy_prompt);
			} else {
				message = getString(R.string.member_leave_chat_dialog_message);
			}
			showConfirmDialog(message, null, null,QUIT);
			break;
		case R.id.editgroup_member_more:
		case R.id.more_layout:
			isShowMore = !isShowMore;
//			mScrollView.setShowMore(isShowMore);
			
			if (isShowMore) {
				mMemberMore.setBackgroundResource(R.drawable.icon_circle_more_up);
			} else {
				mMemberMore.setBackgroundResource(R.drawable.icon_circle_more);
			}
			
			showMemberData(mGroupInfo);
			mAttrLayout.invalidate();
			break;
		case R.id.profile_chat:
			toGroupChat();
			break;
		default:
			break;
		}
	}
 
	private void toGroupChat() {
		AfGrpProfileInfo groupListItem = CacheManager.getInstance().searchGrpProfileInfo(groupId);
		if(groupListItem!=null){//防止这个时候群被解散等的情况
			Bundle bundle = new Bundle();
			bundle.putInt(JsonConstant.KEY_STATUS, groupListItem.status);
			bundle.putBoolean(JsonConstant.KEY_FLAG, true);
			if (groupListItem != null && null != groupListItem.afid && !"".equals(groupListItem.afid)) {
				bundle.putString(GroupChatActivity.BundleKeys.ROOM_ID, groupListItem.afid);
			}
			if (groupListItem != null && null != groupListItem.name && !"".equals(groupListItem.name)) {
				bundle.putString(GroupChatActivity.BundleKeys.ROOM_NAME, groupListItem.name);
			}
			HelpManager.getInstance(context).jumpToPage(GroupChatActivity.class, bundle, false, 0, false);
		}else{ 
			ToastManager.getInstance().show(context, R.string.group_exist); 
		}
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int arg2,
			long arg3) {
		String msg = "";
		if (null != editGroupAdapter && selfIsMaster) {
			AfGrpProfileItemInfo memberItem = editGroupAdapter.getItem(arg2);
			if (!"".equals(memberItem.name) && null != memberItem.name) {
				msg = getString(R.string.delete_chatgroup_member, memberItem.name);
			} else {
				msg = getString(R.string.delete_chatgroup_member, memberItem.afid);
			}
			
			if ((null != memberItem.name && !"".equals(memberItem.name))
					&& (null != memberItem.afid && !"".equals(memberItem.afid))
					&& !memberItem.afid.equals(myProfile.afId)) {
				showConfirmDialog(msg, memberItem.afid, memberItem.name, DELETE_MEMBER);
			}
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		if (null != editGroupAdapter) {
			AfGrpProfileItemInfo info = editGroupAdapter.getItem(arg2);
			
			if (StringUtil.isNullOrEmpty(info.afid)) {
				if (null != mGroupInfo) {
						// 是否需要下载组成员的数据---表示成员只有afid信息，无其它基本信息，表示第一次下来的，后续没有进行过成员的信息获取
						boolean canClick = false;
						int length = mGroupInfo.members.size();
						String[] afidAry = new String[length];
						for (int i = 0; i < length; i++) {
							AfGrpProfileItemInfo member = mGroupInfo.members.get(i);
							afidAry[i] = member.afid;
							if (null != member.name) {
								canClick = true;
								break;
							}
						}
						
						if (canClick) {
							ArrayList<String> existList = new ArrayList<String>();
							int size = mGroupInfo.members.size();
							for (int i = 0; i < size; i++) {
								existList.add(mGroupInfo.members.get(i).afid);
							}
							
							Bundle b = new Bundle();
							// 跳转邀请页面
							b.putStringArrayList("afId_list", existList);
							b.putInt("owner_num", size - 1);
							b.putString("come_page", "grouplist_page");
							b.putString("group_id", groupId);
							b.putString("room_name", mGroupInfo.name);
							
							PalmchatLogUtils.e(TAG, "----jumpToPage----FriendsActivity");
							
							Intent intent = new Intent(this, FriendsActivity.class);
							intent.putExtras(b);
							
							startActivityForResult(intent, REQUEST_CODE_ADD_FRIEND);
						}
				}
			} else {
				Bundle bundle = new Bundle();
				if (myProfile.afId.equals(info.afid)) {
					bundle.putString(JsonConstant.KEY_AFID, info.afid);
					
					Intent intent = new Intent(this, MyProfileActivity.class);
					intent.putExtras(bundle);
					
					startActivityForResult(intent, SHOW_PROFILE);

				} else {
					bundle.putString(JsonConstant.KEY_AFID, info.afid);
					bundle.putBoolean(JsonConstant.KEY_FLAG, true);
					bundle.putSerializable(JsonConstant.KEY_PROFILE, AfGrpProfileItemInfo.grpProfileToProfile(info));
					
					// heguiming 2013-12-04
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.GROUP_T_PF);
//					MobclickAgent.onEvent(context, ReadyConfigXML.GROUP_T_PF);
					
					Intent intent = new Intent(this, ProfileActivity.class);
					intent.putExtra(JsonConstant.KEY_FROM_XX_PROFILE, ProfileActivity.GPCHAT_TO_PROFILE);
					intent.putExtras(bundle);
					
					startActivityForResult(intent, SHOW_PROFILE);
				}
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		PalmchatLogUtils.e(TAG, "----onActivityResult----requestCode:" + requestCode);
		if (requestCode == REQUEST_CODE_ADD_FRIEND) {
			mGroupInfo = loadGroupMembers();
			if (null != mGroupInfo) {
				showMemberData(mGroupInfo);
			}
		} else if (requestCode == SHOW_PROFILE) {
			AfGrpProfileInfo info = loadGroupMembers();
			
			if (null != info) {
				int size = info.members.size();
				for (int i = 0; i < size; i++) {
					AfGrpProfileItemInfo member = info.members.get(i);
					String afid = member.afid;
					// myself
					if (!StringUtil.isNullOrEmpty(afid) && afid.equals(myProfile.afId)) {
						myProfile = CacheManager.getInstance().getMyProfile();
						member.name = myProfile.name;
					}
				}
				mGroupInfo = info;
				showMemberData(mGroupInfo);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Bundle bundle = new Bundle();
			bundle.putBoolean("isCreate", false);
			bundle.putString("ROOMNAME", mEditGroupName.getText().toString());
			bundle.putBoolean("clearGroupMsgSuccess", clearGroupMsgSuccess);
			bundle.putBoolean("isEditGroupName", isEditGroupName);
			Intent data = new Intent();
			data.putExtras(bundle);
			setResult(0, data);
			finish();
			return true;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		looperThread.looper.quit();
	}
	
	/**
	 * show confirm dialog
	 */
	private void showConfirmDialog(String msg, final String afid, final String name,final int type) {
		
		AppDialog confirmDialog = new AppDialog(this);
		confirmDialog.createConfirmDialog(context, msg, new OnConfirmButtonDialogListener() {
			@Override
			public void onRightButtonClick() {
				if (type == DELETE_MEMBER) {
					showProgressDialog(R.string.please_wait);
					String[] delMembers = new String[1];
					delMembers[0] = name;
					deleteMemberAfid = afid;
					mAfCorePalmchat.AfHttpGrpOpr(afid, null, groupId,
							Consts.REQ_GRP_REMOVE_MEMBER, delMembers, EditGroupActivity.this);
				} 
				// clear history
				else if (type == CLEAR_HISTORY) {
					showProgressDialog(R.string.please_wait);
					if (null != looperThread.handler) {
						looperThread.handler.sendEmptyMessage(CLEAR_HISTORY);
					}
				} 
				// quit
				else if (type == QUIT) {
					showProgressDialog(R.string.please_wait);
					int reqFlag = Consts.REQ_GRP_ADMIN_QUIT;
					if (!selfIsMaster) {
						reqFlag = Consts.REQ_GRP_QUIT;
					}
					mAfCorePalmchat.AfHttpGrpOpr(getMembersStr(), mGroupInfo.name, groupId, reqFlag,
							null, EditGroupActivity.this);
				}
			}
			
			@Override
			public void onLeftButtonClick() {
				
			}
		});
		confirmDialog.show();
	}

	/**
	 * show edit Dialog
	 * 
	 * @param title
	 */
	private void showEditDialog(final String title, final String message) {
		
		final AppDialog appDialog = new AppDialog(this);
		appDialog.createEditDialog(context, title, message, new OnInputDialogListener() {
			@Override
			public void onRightButtonClick(String inputStr) {
				if(TextUtils.isEmpty(inputStr)) {
					ToastManager.getInstance().show(context, R.string.public_group_name_not_empty);
				}else {
					inputStr = inputStr.trim();
					if (TextUtils.isEmpty(inputStr)) {
						ToastManager.getInstance().show(context, R.string.public_group_name_not_empty);
					}else {
						 if(inputStr.length()<1){
								showToast(R.string.failed);
								 
							}else{
							// 内容改变了
							if (!inputStr.equals(message.trim())) {
								
								// 修改群名
								if (title.equals(getString(R.string.editmygroup_groupname))) {
									String membersStr = getMembersStr();
									if (!StringUtil.isNullOrEmpty(membersStr)) {
										mGameOrSign = inputStr;
										mGrpName = inputStr;
										mAfCorePalmchat.AfHttpGrpOpr(membersStr, inputStr, groupId, Consts.REQ_GRP_MODIFY, AfGrpNotifyMsg.MODIFY_TYPE_NAME, EditGroupActivity.this);
										showProgressDialog(R.string.please_wait);
										PalmchatLogUtils.i(TAG, "----modify group name !!!");
									} else {
										PalmchatLogUtils.e(TAG, "----showEditDialog: membersStr is empty!!!");
									}
								}
								// 修改群签名
								else {
									if (null != mGroupInfo) {
										showProgressDialog(R.string.please_wait);
										mGameOrSign = inputStr;
										mAfCorePalmchat.AfHttpGrpUpdateSigAndName(null, inputStr, mGroupInfo.afid, AfGrpNotifyMsg.MODIFY_TYPE_SIGN, EditGroupActivity.this);
									}
								}
							}
							}
					}
				}
				
			}

			@Override
			public void onLeftButtonClick() {
			}
		});
		appDialog.show();
	}
	
	/**
	 * 组装成服务器需要的数据格式
	 * @return
	 */
	private String getMembersStr() {
		if (null != mGroupInfo) {
			List<AfGrpProfileItemInfo> lists = mGroupInfo.members;
			int size = lists.size();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < size; i++) {
				AfGrpProfileItemInfo info = lists.get(i);
				buffer.append(info.afid).append(",");
			}
			if (buffer.toString().contains(",")) {
				return buffer.substring(0, buffer.lastIndexOf(","));
			} else {
				return null;
			}
		}
		return null;
	}

	/**
	 * 单线程队列操作
	 * 
	 * @author 何桂明 2013-10-23
	 * 
	 */
	class LooperThread extends Thread {
		
		// 更新缓存数据
		private static final int UPDATE_CATCH = 7000;

		/**
		 * 线程内部handler
		 */
		Handler handler;

		/**
		 * 线程内部Looper
		 */
		Looper looper;

		@Override
		public void run() {
			Looper.prepare();
			looper = Looper.myLooper();
			// 保持当前只有一条线程在执行查看数据操作
			handler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
					case UPDATE_CATCH:
						// 更新到缓存
						AfGrpProfileInfo profileInfo = (AfGrpProfileInfo)msg.obj;
						CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).saveOrUpdate(profileInfo, true, true);
						
						for (int i = 0; i < profileInfo.members.size(); i++) {
							AfGrpProfileItemInfo memeberItem = profileInfo.members.get(i);
							AfFriendInfo friendInfo = CacheManager.getInstance().searchAllFriendInfo(memeberItem.afid);
							if (null == friendInfo) {
								AfFriendInfo info = new AfFriendInfo();
								info.afId = memeberItem.afid;
								info.name = memeberItem.name;
								info.head_img_path = memeberItem.head_image_path;
								info.age = memeberItem.age;
								info.sex = memeberItem.sex;
								// STRANGER
								info.type = Consts.AFMOBI_FRIEND_TYPE_STRANGER;
								info.signature = memeberItem.signature;
								CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_STRANGER).saveOrUpdate(info, true, true);
							}
						}
						
						groupId = profileInfo.afid;
						mGroupInfo = loadGroupMembers();
						
//						EditGroupActivity.this.mHandler.sendEmptyMessage(Consts.REQ_GRP_GET_PROFILE);
						EditGroupActivity.this.mHandler.sendEmptyMessageDelayed(Consts.REQ_GRP_GET_PROFILE, 300);
						
						break;
					case QUIT:
						AfGrpProfileInfo info = CacheManager.getInstance().searchGrpProfileInfo(groupId);
						if (null != info) {
							CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).remove(info, true, true);
							List<MainAfFriendInfo> tempList = CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).getList();
							PalmchatLogUtils.e(TAG, "---tempList.size:" + tempList.size());
							for (int i = 0; i < tempList.size(); i++) {
								MainAfFriendInfo temp = tempList.get(i);
								AfMessageInfo afMsgInfo = temp.afMsgInfo;
								if (null != afMsgInfo && MessagesUtils.isGroupChatMessage(afMsgInfo.type)) {
									AfGrpProfileInfo afGrpInfo = temp.afGrpInfo;
									if (null != afGrpInfo && afGrpInfo.afid.equals(info.afid)) {
										CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).remove(temp, true, true);
										break;
									} 
//									else {
//										CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).remove(temp, true, true);
//									}
								}
							}
							PalmchatLogUtils.e(TAG, "---tempList.size:" + tempList.size());
							
							
//							quite group, clear chat history
							
							AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(
									AfMessageInfo.MESSAGE_TYPE_MASK_GRP, mGroupInfo.afid, 0, Integer.MAX_VALUE);
					if (null != recentDataArray && recentDataArray.length > 0) {
						for (AfMessageInfo messageInfo : recentDataArray) {
							mAfCorePalmchat.AfDbGrpMsgRmove(messageInfo);
							MessagesUtils.removeMsg(messageInfo, true, true);
						}
						mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_GRP,
								mGroupInfo.afid);
						
					}
							
						}
						
						mHandler.sendEmptyMessage(QUIT);
						break;
					case CLEAR_HISTORY:
						AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(
										AfMessageInfo.MESSAGE_TYPE_MASK_GRP, mGroupInfo.afid, 0, Integer.MAX_VALUE);
						if (null != recentDataArray && recentDataArray.length > 0) {
							for (AfMessageInfo messageInfo : recentDataArray) {
								mAfCorePalmchat.AfDbGrpMsgRmove(messageInfo);
								MessagesUtils.removeMsg(messageInfo, true, true);
							}
							mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_GRP,
									mGroupInfo.afid);
							clearGroupMsgSuccess = true;
							EditGroupActivity.this.mHandler.sendEmptyMessage(CLEAR_HISTORY);
						} else {
							EditGroupActivity.this.mHandler.sendEmptyMessage(NO_DATA);
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
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		PalmchatLogUtils.e(TAG, "----isChecked:" + isChecked);
		if (null != mGroupInfo) {
			mGroupInfo.tips = isChecked;
			mAfCorePalmchat.AfDbSettingSetTips(mGroupInfo.afid, mGroupInfo.tips);
			sendUpdateCatcheMsg(mGroupInfo);
		}
	}
	@Override
	public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
		return false;
	}
}
