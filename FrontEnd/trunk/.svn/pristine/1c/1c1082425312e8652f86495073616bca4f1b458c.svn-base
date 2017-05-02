package com.afmobi.palmchat.ui.activity.friends;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.FolllowerUtil;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.eventbusmodel.RefreshFollowerFolloweringOrGroupListEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.friends.adapter.FollowAdapter;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfFriendInfo;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.listener.AfHttpSysListener;
//import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 粉丝列表界面
 */
public class FollowerFragment extends BaseFragment implements AfHttpResultListener,XListView.IXListViewListener {

    private static final String TAG = FollowerFragment.class.getCanonicalName();
    private XListView sortListView;
    private FollowAdapter adapter;
    private int mPageIndex = 1;
    private int mPageSize = 20;
    private boolean isLoadingMore_mListview;
    private List<AfFriendInfo> mList;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");//用于显示刷新时间
    /**
     * 用于取消与中间件的关联
     */
//    private int mhttpHandle;
    /*add by zhh 控制当长按删除好友时，该条目不能进行别的点击事件*/
    private List<AfFriendInfo> deleteItemLs = new ArrayList<AfFriendInfo>();
    //没有数据情况下提示布局
    private LinearLayout mLinFollowingNoDataArea;
    private LooperThread looperThread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        registerBroadcastReceiver();
        looperThread = new LooperThread();
        looperThread.setName(TAG);
        looperThread.start();
        EventBus.getDefault().register(this);
        // gtf 2014-11-16
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_FOLLER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setContentView(R.layout.fragment_following);
        initViews();
        return mMainView;


    }

    @Override
    public void dismissProgressDialog() {
        super.dismissProgressDialog();
    }


    private void initViews() {
        mLinFollowingNoDataArea = (LinearLayout)findViewById(R.id.lin_following_no_data_area);
        //sideBar.setTextView(dialog);
        sortListView = (XListView) findViewById(R.id.country_lvcountry);
        sortListView.setPullRefreshEnable(true);
        sortListView.setPullLoadEnable(true);
        sortListView.setXListViewListener(this);
        List<AfFriendInfo> list = new ArrayList<>();
        adapter = new FollowAdapter(context, list);
        sortListView.setAdapter(adapter);


        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* add by zhh */
                AfFriendInfo afFriendInfo = adapter.getItem(position - 1);

                if (deleteItemLs.contains(afFriendInfo)) {
                    return;
                }
                if (afFriendInfo != null && afFriendInfo.afId != null && afFriendInfo.afId.startsWith("r")) {
                    return;
                }
                // gtf 2014-11-16
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CONTACT_T_PF);
//                MobclickAgent.onEvent(context, ReadyConfigXML.CONTACT_T_PF);
    /*            if (CacheManager.getInstance().getMyProfile().afId.equals(afFriendInfo.afId)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(JsonConstant.KEY_AFID, afFriendInfo.afId);
                    Intent intent = new Intent(context, MyProfileActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else {*/
                if(afFriendInfo != null && !TextUtils.isEmpty(afFriendInfo.afId)) {
                    toProfile(afFriendInfo);
                }
               // }
            }
        });

        sortListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final AfFriendInfo afFriendInfo = adapter.getItem(position - 1);
                if (deleteItemLs.contains(afFriendInfo)) {
                    return true;
                } else {

                    if (afFriendInfo != null && afFriendInfo.afId != null && afFriendInfo.afId.startsWith("r")) {
                        return true;
                    }

                    AppDialog appDialog = new AppDialog(context);
                    String msg = context.getResources().getString(R.string.delete_follower);
                    appDialog.createConfirmDialog(context, msg, new OnConfirmButtonDialogListener() {
                        @Override
                        public void onRightButtonClick() {
                            deleteItemLs.add(afFriendInfo);
                              mAfCorePalmchat.AfHttpFollowCmd(Consts.AFMOBI_FOLLOW_PASSIVE, afFriendInfo.afId, Consts.HTTP_ACTION_D, afFriendInfo, FollowerFragment.this);
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
    }

     public void onEventMainThread(RefreshFollowerFolloweringOrGroupListEvent event) {
		// TODO Auto-generated method stub
    	 if(event.getType() == Constants.REFRESH_FOLLOWER){
             if(context != null) {
                 SharePreferenceUtils.getInstance(context.getApplicationContext()).setIsFolloed(CacheManager.getInstance().getMyProfile().afId, false);
             }
    		 refreshList(true,false);
    	 }
	}
    /**
     * 刷新列表
     */
    private void refreshList(boolean isAdd,boolean isDelete) {
        isLoadingMore_mListview = false;
        sortListView.stopLoadMore();
        if(mList == null){
            mList = new ArrayList<>();
        }
        if(isAdd){
            String[] mListTemp = FolllowerUtil.getInstance().getAfids(1,mPageSize * mPageIndex);
            mList.clear();
            sortListView.setPullLoadEnable(true);
            if(mListTemp != null) {
                getFollower(mListTemp);
            }
            else {
                if(isDelete){
                    adapter.setListForDelete(mList);
                    adapter.notifyDataSetChanged();
                }
                if(adapter.getCount() <=0 ){
                    showNoData(true);
                }else{
                    showNoData(false);
                }
                ToastManager.getInstance().show(getActivity(), getUserVisibleHint(),R.string.no_data);
                sortListView.setPullLoadEnable(false);
            }
        }else {
            String[] mListTemp = FolllowerUtil.getInstance().getAfids(mPageIndex,mPageSize);
            if(mListTemp != null) {
                getFollower(mListTemp);
            }
            else{
                ToastManager.getInstance().show(getActivity(), getUserVisibleHint(),R.string.no_data);
                sortListView.setPullLoadEnable(false);
            }
        }
        PalmchatLogUtils.println("-- xxxx follower size " + mList.size());
        if (getActivity() != null && getActivity() instanceof ContactsActivity) {
            ((ContactsActivity) getActivity()).updateListCounts();
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(!FolllowerUtil.getInstance().getIsLoadFollower()) {
            mAfCorePalmchat.AfFollowGetList((int) Consts.AFMOBI_FOLLOW_PASSIVE, null, this);
            showRefresh();
        }else {
            refreshList(true,false);
        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(looperThread != null) {
            looperThread.looper.quit();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    }

    private void toProfile(final AfFriendInfo afFriendInfo) {
        Intent intent = new Intent(context, ProfileActivity.class);
        AfProfileInfo info = AfFriendInfo.friendToProfile(afFriendInfo);
        intent.putExtra(JsonConstant.KEY_PROFILE, info);
        intent.putExtra(JsonConstant.KEY_FLAG, true);
        intent.putExtra(JsonConstant.KEY_AFID, info.afId);
        intent.putExtra(JsonConstant.KEY_USER_MSISDN, info.user_msisdn);
        context.startActivity(intent);
    }


    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        ((BaseFragmentActivity) context).dismissProgressDialog(); 
        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                case Consts.REQ_FLAG_FOLLOWERS_LIST:
				/* add by zhh */
                    if (user_data != null) {
                        AfFriendInfo friendInfo = (AfFriendInfo) user_data;
                        if (deleteItemLs.contains(friendInfo))
                            deleteItemLs.remove(friendInfo);
                        MessagesUtils.onDelFollowSuc(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE, friendInfo);
                        FolllowerUtil.getInstance().remove(friendInfo.afId);
                        if (isAdded()) {
                            refreshList(true,true);
                            ToastManager.getInstance().show(context, context.getResources().getString(R.string.success));
                        }
                    } else {
                        if (result != null) {
                            String[] arr = (String[]) result;
                            FolllowerUtil.getInstance().setIsLoadFollower(true);
                            if (arr.length == 0) {
                                stopRefresh();
                                if (getActivity() != null && getActivity() instanceof ContactsActivity) {
                                    ((ContactsActivity) getActivity()).updateListCounts();
                                }
                            }
                            else {
                                showNoData(false);
                                FolllowerUtil.getInstance().addAll(arr);
                                refreshList(false,false);
                            }
                        } else {
                            stopRefresh();
                            showNoData(true);
                        }
                    }
                    break;
                case Consts.REQ_GET_BATCH_INFO:
                    AfFriendInfo[] afFriendInfos = (AfFriendInfo[]) result;

                    Follower follower = (Follower) user_data;
                    for (String str : follower.afids) {
                        for (AfFriendInfo info : follower.mList) {
                            if (info.afId.equals(str)) {
                                mList.add(info);
                                break;
                            }
                        }
                        if (afFriendInfos != null) {
                            for (AfFriendInfo info1 : afFriendInfos) {
                                if (info1.afId.equals(str)) {
                                    mList.add(info1);
                                    break;
                                }
                            }
                        }
                    }
                    if (afFriendInfos != null) {
                        for (AfFriendInfo afFriendInfo : afFriendInfos) {
                            Message msg = new Message();
                            msg.what = LooperThread.UPDATE_FOLLOWER;
                            msg.obj = afFriendInfo;
                            looperThread.handler.sendMessage(msg);
                        }
                    }
                    adapter.setList(mList);
                    adapter.notifyDataSetChanged();
                    stopRefresh();
                    if(adapter.getCount() <=0 ){
                        showNoData(true);
                    }else{
                        showNoData(false);
                    }
                    break;
            }

        } else {
			/* add by zhh */
            if (null != user_data) {
                AfFriendInfo friendInfo = (AfFriendInfo) user_data;
                if (deleteItemLs.contains(friendInfo))
                    deleteItemLs.remove(friendInfo);
            }


            if (isAdded()) {
                Consts.getInstance().showToast(context, code, flag, http_code);
            }
        }
    }

    /**
     * 判断是否显示提示控件
     * @param bool
     */
    private void showNoData(boolean bool){
        if(bool){//判断是否有数据显示提示语
            mLinFollowingNoDataArea.setVisibility(View.VISIBLE);
           // sideBar.setVisibility(View.GONE);
        }else{
            mLinFollowingNoDataArea.setVisibility(View.GONE);
           // sideBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh(View view) {

    }

    @Override
    public void onLoadMore(View view) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            if (!isLoadingMore_mListview) {
                isLoadingMore_mListview = true;
                mPageIndex++;
                refreshList(false,false);
            }
        }else {
            ToastManager.getInstance().show(context,getUserVisibleHint(), R.string.network_unavailable);
            isLoadingMore_mListview = false;
            sortListView.stopLoadMore();
        }
    }

    private void getFollower(String[] afids){
        if(adapter.getCount() == 0){
            showRefresh();
        }
       List<AfFriendInfo> mListTemp = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> arrayAfids = new ArrayList<>();
        for (String afid : afids){
            AfFriendInfo localInfo = CacheManager.getInstance().searchFriendInfoForFollow(afid);
            if(localInfo == null || (localInfo != null && TextUtils.isEmpty(localInfo.name))) {
                arrayList.add(afid);
            }
            else {
                mListTemp.add(localInfo);
            }
            arrayAfids.add(afid);
        }
        if(arrayList.size() == 0) {
            mList.addAll(mListTemp);
            adapter.setList(mList);
            adapter.notifyDataSetChanged();
            stopRefresh();
            if(adapter.getCount() <=0 ){
                showNoData(true);
            }else{
                showNoData(false);
            }
        }
        else{
            Follower follower = new Follower();
            follower.afids = arrayAfids.toArray(new String[]{});
            follower.mList = mListTemp;
            mAfCorePalmchat.AfHttpGetInfo(arrayList.toArray(new String[]{}),Consts.REQ_GET_BATCH_INFO,null,follower,this);
        }
    }

    class Follower
    {
        public String[] afids;
        public List<AfFriendInfo> mList;
    }

    public void showRefresh(){
        if (NetworkUtils.isNetworkAvailable(context)) {
            if (getActivity() != null) {
                int px = AppUtils.dpToPx(getActivity(), 60);
                sortListView.performRefresh(px);
            }
        }else {
            ToastManager.getInstance().show(context,mFragmentVisible, context.getString(R.string.network_unavailable));
            stopRefresh();
        }
    }


    private void stopRefresh() {
        // TODO Auto-generated method stub
        sortListView.stopRefresh(true);
        sortListView.setPullRefreshEnable(false);
        sortListView.setRefreshTime(mDateFormat.format(new Date(System.currentTimeMillis())));

    }

    class LooperThread extends Thread {
        private static final int UPDATE_FOLLOWER = 9002;
        Handler handler;
        Looper looper;
        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            handler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case UPDATE_FOLLOWER:
                            AfFriendInfo afFriendInfo = (AfFriendInfo) msg.obj;
                            if(afFriendInfo != null) {
                                MessagesUtils.onAddFollower(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE,afFriendInfo,true,true);
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
}
