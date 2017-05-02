package com.afmobi.palmchat.ui.activity.friends;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.eventbusmodel.EventRefreshFriendList;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.publicaccounts.AccountsChattingActivity;
import com.afmobi.palmchat.ui.activity.setting.Contacts;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.eventbusmodel.BlockFriendEvent;
import com.afmobi.palmchat.eventbusmodel.DestroyGroupEvent;
import com.afmobi.palmchat.eventbusmodel.RefreshFollowerFolloweringOrGroupListEvent;
import com.afmobi.palmchat.listener.FragmentSendFriendCallBack;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.friends.adapter.FollowAdapter;
import com.afmobi.palmchat.ui.activity.invitefriends.Query;
import com.afmobi.palmchat.ui.activity.main.ChatsContactsActivity;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.SideBar;
import com.afmobi.palmchat.ui.customview.SideBar.OnTouchingLetterChangedListener;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.listener.AfHttpSysListener;
//import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 好友列表界面
 */
public class ContactsFriendsFragment extends BaseFragment  implements Query.OnQueryComplete, AfHttpSysListener, AfHttpResultListener {
    private static final String TAG = ContactsFriendsFragment.class.getCanonicalName();
    private ListView mListView;
    private FollowAdapter mAdapter;
    private SideBar sideBar;
    private TextView dialog,mtxtGroupCount,mtxtPublicCount;
    /*add by zhh 控制当长按删除好友时，该条目不能进行别的点击事件*/
    private List<AfFriendInfo> deleteItemLs = new ArrayList<AfFriendInfo>();
    /**
     * 手机通讯录内的联系人
     */
    private HashMap<String,AfFriendInfo> mPhoneFriend;
    private boolean mWaitPhoneQuery, mWaitSimQuery;
    private Query queryHandler;
    private LooperThread mLooperThread;
   /* List<AfFriendInfo> mAfFriendInfosList;*/
    /**
     * 是否已获取手机通讯录
     */
    boolean mIsGetPhoneContacts;
    /**
     * 应用初始化
     */
    boolean mIsInit;
    /**
     * 是否显示手机通讯录
     */
    private boolean isShowPhoneContacts = true;

    private final int GROUP_RESULT_CODE = 1001;
    //没有数据情况下显示提示语句
    private LinearLayout mLinContactsNoDataArea;

    TextView tvFriendCount;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof ChatsContactsActivity){
            mFragmentSendFriendCallBack = (ChatsContactsActivity) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentSendFriendCallBack = null;
    }
    //public static void clearPhoneFriendList(){
    	//mPhoneFriend.clear();
   // }
    FragmentSendFriendCallBack mFragmentSendFriendCallBack;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mAfCorePalmchat.AfAddHttpSysListener(this);

        // gtf 2014-11-16
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_FL);
//        MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_FL);

        mLooperThread = new LooperThread();
        mLooperThread.setName(TAG);
        mLooperThread.start();
        mIsInit = false;
        EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_contacts_friends);
        Bundle data = getArguments();
        if(data != null){
            isShowPhoneContacts = data.getBoolean("isShowPhoneContacts");
        }
        mPhoneFriend = new HashMap<String,AfFriendInfo>();
        initViews();
        return mMainView;

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        MobclickAgent.onPageStart(TAG);
        updateListCounts();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//        MobclickAgent.onPageEnd(TAG);
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mAfCorePalmchat.AfRemoveHttpSysListener(this);

        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(DestroyGroupEvent event) {
        // TODO Auto-generated method stub
        updateListCounts();
    }

    public void onEventMainThread(RefreshFollowerFolloweringOrGroupListEvent event) {
        // TODO Auto-generated method stub
        if(event.getType() == Constants.REFRESH_GROUPLIST){
            updateListCounts();
        }
    }

    public void onEventMainThread(EventRefreshFriendList event) {
        // TODO Auto-generated method stub
        getFriendList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == GROUP_RESULT_CODE) {
                boolean isSucess = data.getBooleanExtra("isSucess", false);
                boolean isCancel = data.getBooleanExtra("isCancel", false);
                String boradTips = data.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL);
                if (!isShowPhoneContacts) {
                    ChatsContactsActivity main = (ChatsContactsActivity) getActivity();
                    if (main != null) {
                        main.closeActivity(isSucess, isCancel, boradTips);
                    }
                }
            }
        }
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.friends_contacts_listview);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        mLinContactsNoDataArea = (LinearLayout)findViewById(R.id.lin_contacts_no_data_area);
        sideBar.setTextView(dialog);
        View headerView = View.inflate(context, R.layout.header_contacts_friends, null);
        headerView.findViewById(R.id.h1).setOnClickListener(this);
        headerView.findViewById(R.id.h2).setOnClickListener(this);
        if(!isShowPhoneContacts){
            headerView.findViewById(R.id.h2).setVisibility(View.GONE);
        }
        mtxtGroupCount = (TextView) headerView.findViewById(R.id.group_count);
        mtxtPublicCount = (TextView) headerView.findViewById(R.id.publicAccount_count);
        mListView.addHeaderView(headerView);

        View footView = View.inflate(context, R.layout.contacts_count_des, null);
        tvFriendCount = (TextView) footView.findViewById(R.id.tv_friend_count);
        if(isShowPhoneContacts) {
            mListView.addFooterView(footView,null,false);
        }
        // List<AfFriendInfo> list = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).getList();
        mAdapter = new FollowAdapter(context, new ArrayList<AfFriendInfo>(),!isShowPhoneContacts,true);
        mListView.setAdapter(mAdapter);
        /**
         * 点击跳转profile界面
         */
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				position 0 is headerview
                position--;
                if(mAdapter.getCount() <= position)
                    return;
                final AfFriendInfo afFriendInfo = mAdapter.getItem(position);
                if (afFriendInfo != null && afFriendInfo.afId != null && afFriendInfo.afId.startsWith("r")) {
                    toChatting(afFriendInfo,afFriendInfo.afId,afFriendInfo.name);
                    return;
                }
                if (isShowPhoneContacts) {
                /*add by zhh*/
                    if (deleteItemLs.contains(afFriendInfo) || afFriendInfo.isAddFriend) { //如果当前正在删除该好友，则不能再点击
                        return;
                    } else {
                        CacheManager.getInstance().getThreadPoolInstance().execute(
                                new Thread() {
                                    public void run() {
                                        mAfCorePalmchat.AfRecentMsgSetUnread(afFriendInfo.afId, 0);
                                        MessagesUtils.setUnreadMsg(afFriendInfo.afId, 0);
                                        if (afFriendInfo.is_new_contact) {
                                            afFriendInfo.is_new_contact = false;
                                            mAfCorePalmchat.AfDbProfileUpdateNewContact(afFriendInfo.afId, false);
                                        }

                                    }

                                });
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LIST_T_P);
//                        MobclickAgent.onEvent(context, ReadyConfigXML.LIST_T_P);
                        //gtf 2014-11-16
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CONTACT_T_PF);
//                        MobclickAgent.onEvent(context, ReadyConfigXML.CONTACT_T_PF);
                        if (CacheManager.getInstance().getMyProfile().afId.equals(afFriendInfo.afId)) {
                            Bundle bundle = new Bundle();
                            bundle.putString(JsonConstant.KEY_AFID, afFriendInfo.afId);
                            Intent intent = new Intent(context, MyProfileActivity.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        } else {
                            toProfile(afFriendInfo);
                        }

                    }

                } else {
                    if (mFragmentSendFriendCallBack != null) {
                        mFragmentSendFriendCallBack.fragmentCallBack(true, afFriendInfo);
                    }
                }
            }
        });
        /**
         * 长按点击是否要删除此好友
         */
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                position--;
                if(mAdapter.getCount() <= position)
                    return true;
                final AfFriendInfo afFriendInfo = mAdapter.getItem(position);
                if (!isShowPhoneContacts) {
                    if (mFragmentSendFriendCallBack != null) {
                        mFragmentSendFriendCallBack.fragmentCallBack(true, afFriendInfo);
                    }
                    return true;
                }
                if(afFriendInfo.isAddFriend)
                    return true;
				/* add by zhh */
                if (deleteItemLs.contains(afFriendInfo)) { // 如果当前正在删除该好友，则不能再点击
                    return true;
                } else {
                    // palmchat team can't be deleted
                    if (afFriendInfo != null && afFriendInfo.afId != null && afFriendInfo.afId.startsWith("r")) {
                        return true;
                    }
                    AppDialog appDialog = new AppDialog(context);
                    String msg = context.getResources().getString(R.string.delete_friend_dialog_content2);
                    appDialog.createConfirmDialog(context, msg, new OnConfirmButtonDialogListener() {
                        @Override
                        public void onRightButtonClick() {
                            deleteItemLs.add(afFriendInfo);
                            mAfCorePalmchat.AfHttpFriendOpr("all", afFriendInfo.afId, Consts.HTTP_ACTION_D, Consts.FRIENDS_MAKE, Consts.AFMOBI_FRIEND_TYPE_FF, null, afFriendInfo, ContactsFriendsFragment.this);
                        }

                        @Override
                        public void onLeftButtonClick() {
                        }
                    });
                    appDialog.show();

                }
                return true;
            }
        });

        /**
         * 右边字母的触摸事件
         */
        sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);

                }
            }

        });
    }

    private void toChatting(AfFriendInfo affFriendInfo, String afid, String name) {
//		enter public account chat
        if(afid.startsWith(DefaultValueConstant._R) && !CommonUtils.isSystemAccount(affFriendInfo.afId)){
            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_P_PBL);
//			MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_P_PBL);
        } else {
            // heguiming 2013-12-04
            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LIST_T_P);
//			MobclickAgent.onEvent(context, ReadyConfigXML.LIST_T_P);

        }


        Intent intent = new Intent();
        if (afid != null && afid.startsWith(DefaultValueConstant._R)) {
            intent.setClass(context, AccountsChattingActivity.class);
        } else {
            intent.setClass(context,Chatting.class);
        }

        intent.putExtra(JsonConstant.KEY_FROM_UUID, afid);
        intent.putExtra(JsonConstant.KEY_FROM_NAME, name);
        intent.putExtra(JsonConstant.KEY_FROM_ALIAS, affFriendInfo.alias);
        intent.putExtra(JsonConstant.KEY_FRIEND, affFriendInfo);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    public void resetSlidBar(boolean hideSlideBar) {
        if (sideBar != null) {
            sideBar.reset();
        }
    }

    /**
     * 子母列表滚动改变数据
     *
     * @param arg0
     */
    public void onPageScrollStateChanged(int arg0) {
        switch (arg0) {
            case ViewPager.SCROLL_STATE_IDLE:
                if (sideBar != null) {
                    if(mLinContactsNoDataArea.getVisibility() == View.VISIBLE){
                        sideBar.setVisibility(View.GONE);
                    }else{
                        sideBar.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    /**
     * 判断当前的Fragment界面是否可见
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        if (mFragmentVisible) {
            refreshFriendList();
            updateListCounts();
           /* if (sideBar != null) {
                sideBar.setVisibility(View.VISIBLE);
*/
        }
        /*} else {
            if (sideBar != null) {
                sideBar.setVisibility(View.GONE);
            }
        }*/
    }

    /**
     * 刷新listview列表
     */
    private void refreshFriendList() {
        if(mIsGetPhoneContacts || mIsInit || !isShowPhoneContacts) {
            getFriendList();
        }
        else {
            getContacts();
        }
    }

    private void getFriendList(){
        List<AfFriendInfo> mAfFriendInfosList = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).getList();
        if (null != mLooperThread) {
            Handler handler = mLooperThread.handler;
            if (null != handler) {
                handler.obtainMessage(LooperThread.REFRESH_FRIEND_LIST, mAfFriendInfosList).sendToTarget();
            }
        }
    }

    public void rerefreshContactsLists(BlockFriendEvent mBlockFriendEvent){

        getFriendList();

//        mAfCorePalmchat.AfHttpFriendOpr("all", mBlockFriendEvent.getMsg(), Consts.HTTP_ACTION_D, Consts.FRIENDS_MAKE, Consts.AFMOBI_FRIEND_TYPE_FF, null, null, ContactsFriendsFragment.this);
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
           if(!mIsGetPhoneContacts) {
               mIsGetPhoneContacts = true;
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
    }

    class LooperThread extends Thread {
        private static final int DISPLAY_PHONE_LIST = 101;
        private static final int REFRESH_FRIEND_LIST = 102;
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
                            List<AfFriendInfo> mAfFriendInfosList = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).getList();
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
                                if(isPhoneBinded(mAfFriendInfosList,afFriendInfo.user_msisdn))
                                    continue;
                                if(isExitsPhone(mAfFriendInfosList,afFriendInfo))
                                    continue;
                                mPhoneFriend.put(afFriendInfo.user_msisdn,afFriendInfo);
                                friendIndertSort(mAfFriendInfosList,afFriendInfo);
                            }
                            mHandler.obtainMessage(DISPLAY_PHONE_LIST, mAfFriendInfosList).sendToTarget();
                            break;
                        case REFRESH_FRIEND_LIST:
                            List<AfFriendInfo> mAfFriendInfosListRefresh = (List<AfFriendInfo>) msg.obj;
                            if(!isShowPhoneContacts) {//在分享广播给好友时，不显示系统PALMCHAT
                                List<AfFriendInfo> mAfFriendInfosListRefreshTemp = new ArrayList<AfFriendInfo>();
                                for (AfFriendInfo friendInfo : mAfFriendInfosListRefresh) {
                                     if(!friendInfo.afId.toLowerCase().startsWith("r")){
                                         mAfFriendInfosListRefreshTemp.add(friendInfo);
                                     }
                                }
                                mHandler.obtainMessage(DISPLAY_PHONE_LIST, mAfFriendInfosListRefreshTemp).sendToTarget();
                            }
                            else {
                                if(mPhoneFriend != null && mPhoneFriend.size() > 0) {
                                    Iterator  infoIterator = mPhoneFriend.entrySet().iterator();
                                    AfFriendInfo afFriendInfo1;
                                    while (infoIterator.hasNext()){
                                        Map.Entry entry = (Map.Entry) infoIterator.next();
                                        afFriendInfo1 = (AfFriendInfo) entry.getValue();
                                        if(isPhoneBinded(mAfFriendInfosListRefresh,afFriendInfo1.user_msisdn))
                                            continue;
                                        friendIndertSort(mAfFriendInfosListRefresh, afFriendInfo1);
                                    }
                                }
                                mHandler.obtainMessage(DISPLAY_PHONE_LIST, mAfFriendInfosListRefresh).sendToTarget();
                            }

                            break;
                    }
                }
            };

            Looper.loop();

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

    private boolean isPhoneBinded(List<AfFriendInfo> afFriendInfosList,String phoneNum){
        for (AfFriendInfo afFriendInfo : afFriendInfosList){
            if(!TextUtils.isEmpty(afFriendInfo.phone) && phoneNum.equals(afFriendInfo.phone)){
                return true;
            }
        }
        return false;
    }




    /**
     * 查看好友的信息
     *
     * @param afFriendInfo
     */
    private void toProfile(final AfFriendInfo afFriendInfo) {
        Intent intent = new Intent(context, ProfileActivity.class);
        AfProfileInfo info = AfFriendInfo.friendToProfile(afFriendInfo);
        intent.putExtra(JsonConstant.KEY_PROFILE, info);
        intent.putExtra(JsonConstant.KEY_FLAG, true);
        intent.putExtra(JsonConstant.KEY_AFID, info.afId);
        intent.putExtra(JsonConstant.KEY_USER_MSISDN, info.user_msisdn);
        intent.putExtra(JsonConstant.KEY_FROM_XX_PROFILE, ProfileActivity.FRIENDS_TO_PROFILE);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.h1:
                if(isShowPhoneContacts) {
                    //跳转到群组列表界面
                    startActivity(new Intent(context, GroupListActivity.class));
                }
                else{
                    //跳转到群组列表界面
                    ChatsContactsActivity main = (ChatsContactsActivity)getActivity();
                    Intent intent = new Intent(getActivity(), GroupListActivity.class);
                    if(main != null) {
                        intent.putExtra(JsonConstant.KEY_SHARE_ID, main.mShareId);
                        intent.putExtra(JsonConstant.KEY_IS_SHARE_TAG, main.isShareTag);
                        intent.putExtra(JsonConstant.KEY_BROADCAST_CONTENT, main.mContent);
                        intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_AFID, main.mBroadcastAfid);
                        intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_NAME, main.senderName);
                        intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_HEADER_URL, main.senderHeaderUrl);
                        intent.putExtra(JsonConstant.KEY_TAG_URL, main.mTagUrl);
                        intent.putExtra(JsonConstant.KEY_TAG_NAME, main.mTagName);
                        intent.putExtra(JsonConstant.KEY_SHARE_TAG_POST_NUM, main.mShareTagPostNum);
                        if(main.afChapterInfo != null) {
                            intent.putExtra(JsonConstant.KEY_BC_AFCHAPTERINFO,main.afChapterInfo);
                        }
                    }
                    intent.putExtra("isShowPhoneContacts", true);
                    startActivityForResult(intent,GROUP_RESULT_CODE);
                }
                break;
            case R.id.h2:
                //跳转到公共账号界面
                startActivity(new Intent(context, PublicAccountListActivity.class));
                break;
        }

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {

                case Constants.DB_DELETE:
                    refreshFriendList();//删除好友成功后刷新列表
                    AfFriendInfo afFriendInfo = (AfFriendInfo) msg.obj;
                    clearPrivateChatHistory(mAfCorePalmchat, afFriendInfo.afId);
                    ((BaseFragmentActivity) context).dismissProgressDialog();

                    if (isAdded()) {
                        ToastManager.getInstance().show(context, context.getResources().getString(R.string.success));
                    }

                    break;
                case LooperThread.DISPLAY_PHONE_LIST:
                    List<AfFriendInfo> tmpList = (List<AfFriendInfo>) msg.obj;
                    if(tmpList.size()<=0){
                        mLinContactsNoDataArea.setVisibility(View.VISIBLE);
                        sideBar.setVisibility(View.GONE);
                    }else{
                        mLinContactsNoDataArea.setVisibility(View.GONE);
                        sideBar.setVisibility(View.VISIBLE);
                    }
                    mAdapter.setList(tmpList);
                    mAdapter.notifyDataSetChanged();
                    if(isAdded()) {// attached to Activity才执行
                        String msgCount = getString(R.string.friend_contacts_count_text);
                        int friendCount = tmpList.size() - mPhoneFriend.size();
                        if (friendCount < 0)
                            friendCount = 0;
                        String tip = msgCount.replace("XXXX", friendCount + "").replace("YYYY", mPhoneFriend.size() + "");
                        tvFriendCount.setText(tip);
                    }
                   /* if (getActivity() != null && getActivity() instanceof ContactsActivity) {
                        ((ContactsActivity) getActivity()).updateListCounts();
                    }*/
                    break;
            }

        }

    };

    /**
     * 删除好友后并且会把删除的这个好友相关的信息全部删除掉
     *
     * @param mAfCorePalmchat
     * @param afId
     */
    public void clearPrivateChatHistory(final AfPalmchat mAfCorePalmchat, final String afId) {
        new Thread(new Runnable() {
            public void run() {
                final AfMessageInfo[] recentDataArray = mAfCorePalmchat
                        .AfDbRecentMsgGetRecord(
                                AfMessageInfo.MESSAGE_TYPE_MASK_PRIV,
                                afId, 0, Integer.MAX_VALUE);
                if (null != recentDataArray && recentDataArray.length > 0) {
                    for (AfMessageInfo messageInfo : recentDataArray) {
                        mAfCorePalmchat.AfDbMsgRmove(messageInfo);
                        MessagesUtils.removeMsg(messageInfo, true, true);
                    }
                    mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, afId);
//                    context.sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
                    EventBus.getDefault().post(new RefreshChatsListEvent());
                }
            }

        }).start();

    }

    @Override
    public boolean AfHttpSysMsgProc(int msg, Object wparam, int lParam) {
        // TODO Auto-generated method stub

        switch (msg) {

            case Consts.AF_SYS_MSG_INIT:
            case Consts.AF_SYS_MSG_UPDATE_FRIEND:
                refreshFriendList();
                mIsInit = false;
                break;
            case Consts.AFMOBI_SYS_MSG_UPDATE_GRP:
                updateListCounts();
                break;
        }
        return false;
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                // 删除好友
                case Consts.REQ_FRIEND_LIST:
				/* add by zhh */
                    final AfFriendInfo friendInfo = (AfFriendInfo) user_data;
                    if (deleteItemLs.contains(friendInfo))
                        deleteItemLs.remove(friendInfo);

                    new Thread() {
                        public void run() {
                            //删除选定的好友请求
                            MessagesUtils.onDelFriendSuc(friendInfo);
                            mHandler.obtainMessage(Constants.DB_DELETE, friendInfo).sendToTarget();
                        }

                    }.start();

                    break;
            }

            // 失败的
        } else {
			/* add by zhh */
            if (null != user_data) {
                AfFriendInfo friendInfo = (AfFriendInfo) user_data;
                if (deleteItemLs.contains(friendInfo))
                    deleteItemLs.remove(friendInfo);
            }
            ((BaseFragmentActivity) context).dismissProgressDialog();
            if (isAdded()) {
                Consts.getInstance().showToast(context, code, flag, http_code);
            }

        }
    }

    /** 播入数据
     * @param friendInfo
     */
    private void friendIndertSort(List<AfFriendInfo>mAfFriendInfosList, AfFriendInfo friendInfo) {
        int start = 0, mid;
        int end = mAfFriendInfosList.size() - 1;
        AfFriendInfo cur;
        while (start <= end) {
            mid = (start + end) >> 1;
            cur = mAfFriendInfosList.get(mid);
            if (compareObjectKey(friendInfo, cur) < 0) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        if (start < 0 || start >= mAfFriendInfosList.size()) {
            mAfFriendInfosList.add(friendInfo);
        } else {
            mAfFriendInfosList.add(start, friendInfo);
        }
    }

    /** 比较
     * @param one
     * @param another
     * @return
     */
    protected int compareObjectKey(AfFriendInfo one, AfFriendInfo another) {
        // TODO Auto-generated method stub
        char key1, key2;
        String str1, str2;
        str1 = TextUtils.isEmpty(one.alias) ?one.name: one.alias;
        str2 = TextUtils.isEmpty(another.alias) ?another.name: another.alias;

        key1 = CommonUtils.getSortKey(str1);
        key2 = CommonUtils.getSortKey(str2);

        if( key1 == key2)
        {
            if( null == str1)
            {
                return -1;
            }
            if( null == str2)
            {
                return 1;
            }
            return str1.compareToIgnoreCase(str2);
        }
        return (key1 - key2);
    }

    private void updateListCounts() {
        int publicAccountSize = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).size(false, true);
      /*  if (mTvCount1 != null) {
            mTvCount1.setText(String.valueOf(frdSize));
        }*/
        ArrayList<AfGrpProfileInfo> grp_list = new ArrayList<AfGrpProfileInfo>();
        List<AfGrpProfileInfo> grp_cacheList = CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).getList();
        for(AfGrpProfileInfo grpProfile : grp_cacheList) {
            if(grpProfile.status == Consts.AFMOBI_GRP_STATUS_ACTIVE) {
                grp_list.add(grpProfile);
            }
        }
        if (mtxtGroupCount != null) {
            mtxtGroupCount.setText(String.valueOf(grp_list.size()));
        }

        if (mtxtPublicCount != null) {
            mtxtPublicCount.setText(String.valueOf(publicAccountSize));
        }
    }
}
