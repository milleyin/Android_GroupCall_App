package com.afmobi.palmchat.ui.activity.friends;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.util.NetworkUtils;
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
import com.afmobi.palmchat.ui.customview.SideBar;
import com.afmobi.palmchat.ui.customview.SideBar.OnTouchingLetterChangedListener;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.listener.AfHttpSysListener;
//import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 正在关注列表界面
 */
public class FollowingFragment extends BaseFragment implements AfHttpSysListener, AfHttpResultListener,XListView.IXListViewListener {

    private static final String TAG = FollowingFragment.class.getCanonicalName();
    private XListView sortListView;
    private FollowAdapter adapter;
    private boolean isLoadingMore_mListview;
    private int mPageIndex = 1;
    private int mPageSize = 20;
    List<AfFriendInfo> mList;
    /*add by zhh 控制当长按删除好友时，该条目不能进行别的点击事件*/
    private List<AfFriendInfo> deleteItemLs = new ArrayList<AfFriendInfo>();
    //没有数据情况下提示布局
    private LinearLayout mLinFollowingNoDataArea;
    private LooperThread looperThread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        looperThread = new LooperThread();
        looperThread.setName(TAG);
        looperThread.start();
        EventBus.getDefault().register(this);
        mAfCorePalmchat.AfAddHttpSysListener(this);
        // gtf 2014-11-16
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_FPLL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_following);
        initViews();
        return mMainView;
    }

    private void initViews() {
        sortListView = (XListView) findViewById(R.id.country_lvcountry);
        sortListView.setPullRefreshEnable(false);
        sortListView.setPullLoadEnable(true);
        sortListView.setXListViewListener(this);
        mLinFollowingNoDataArea = (LinearLayout)findViewById(R.id.lin_following_no_data_area);
        List<AfFriendInfo> list = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).getList();
        adapter = new FollowAdapter(context, list);
        sortListView.setAdapter(adapter);
        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AfFriendInfo afFriendInfo = adapter.getItem(position - 1);

				/* add by zhh */
                if (deleteItemLs.contains(afFriendInfo)) { // 如果当前正在删除该好友，则不能再点击
                } else {
                    if (afFriendInfo != null && afFriendInfo.afId != null && afFriendInfo.afId.startsWith("r")) {
                        return;
                    }
                    // gtf 2014-11-16
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CONTACT_T_PF);

/*                    if (CacheManager.getInstance().getMyProfile().afId.equals(afFriendInfo.afId)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(JsonConstant.KEY_AFID, afFriendInfo.afId);
                        Intent intent = new Intent(context, MyProfileActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else {*/
                    if(afFriendInfo != null && !TextUtils.isEmpty(afFriendInfo.afId)) {
                        toProfile(afFriendInfo);
                    }
                    //}
                }
            }
        });

        sortListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final AfFriendInfo afFriendInfo = adapter.getItem(position - 1);
                if (deleteItemLs.contains(afFriendInfo)) { //如果当前正在删除该好友，则不能再点击
                    return true;
                } else {
                    if (afFriendInfo != null && afFriendInfo.afId != null && afFriendInfo.afId.startsWith("r")) {
                        return true;
                    }
                    AppDialog appDialog = new AppDialog(context);
                    String msg = context.getResources().getString(R.string.unfollow_dialog_msg);
                    appDialog.createConfirmDialog(context, msg, new OnConfirmButtonDialogListener() {
                        @Override
                        public void onRightButtonClick() {
                            deleteItemLs.add(afFriendInfo);
                              mAfCorePalmchat.AfHttpFollowCmd(Consts.AFMOBI_FOLLOW_MASTRE, afFriendInfo.afId, Consts.HTTP_ACTION_D, afFriendInfo, FollowingFragment.this);
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
    	if(event.getType() == Constants.REFRESH_FOLLOWING){
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
            List<AfFriendInfo> mListTemp =CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).getList(1,mPageSize * mPageIndex);
            mList.clear();
            sortListView.setPullLoadEnable(true);
            if(mListTemp != null) {
                getFollowing(mListTemp);
            }
            else {
                ToastManager.getInstance().show(getActivity(), getUserVisibleHint(),R.string.no_data);
                sortListView.setPullLoadEnable(false);
            }
        }else {
            List<AfFriendInfo> mListTemp =CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).getList(mPageIndex,mPageSize);
            if(mListTemp != null) {
                getFollowing(mListTemp);
            }
            else{
                ToastManager.getInstance().show(getActivity(), getUserVisibleHint(),R.string.no_data);
                sortListView.setPullLoadEnable(false);
            }
        }
        if(isDelete){
            adapter.setListForDelete(mList);
        }
        else{
            adapter.setList(mList);
        }
        adapter.notifyDataSetChanged();
        if(adapter.getCount() <=0 ){
            showNoData(true);
        }else{
            showNoData(false);
        }
        PalmchatLogUtils.println("-- xxxx following size " + mList.size());
        if (getActivity() != null && getActivity() instanceof ContactsActivity) {
            ((ContactsActivity) getActivity()).updateListCounts();
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        refreshList(true,false);
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
        mAfCorePalmchat.AfRemoveHttpSysListener(this);
        EventBus.getDefault().unregister(this);
    }

    /**
     * 判断当前的Fragment是否可见区域
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        if (adapter != null //&&adapter.getCount()<1
                && System.currentTimeMillis() - timeRefreshFollowFollowing > 300000) {
            //如果没Following数据 那就重新获取一下
            mAfCorePalmchat.AfCoreRefreshFollow((int)Consts.AFMOBI_FOLLOW_MASTRE);
            timeRefreshFollowFollowing = System.currentTimeMillis();
        }
        if(mLinFollowingNoDataArea != null && mLinFollowingNoDataArea.getVisibility() == View.VISIBLE){
            int followingSize = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).size(false, true);
            if(followingSize > 0){
                refreshList(true,false);
            }
        }
    }

    public static long timeRefreshFollowFollowing;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    }

    /**
     * 查看好友信息
     * @param afFriendInfo
     */
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
    public boolean AfHttpSysMsgProc(int msg, Object wparam, int lParam) {
        // TODO Auto-generated method stub
        switch (msg) {
            case Consts.AF_SYS_MSG_INIT:
            case Consts.AFMOBI_SYS_MSG_UPDATE_FOLLOW:
                refreshList(true,false);
                break;

        }

        return false;
    }


    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        // TODO Auto-generated method stub
        ((BaseFragmentActivity) context).dismissProgressDialog();
         
        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                case Consts.REQ_FLAG_FOLLOW_LIST:
                /*add by zhh*/
                    AfFriendInfo friendInfo = (AfFriendInfo) user_data;
                    if (deleteItemLs.contains(friendInfo))
                        deleteItemLs.remove(friendInfo);
                    MessagesUtils.onDelFollowSuc(Consts.AFMOBI_FOLLOW_TYPE_MASTER, friendInfo);
                    if (isAdded()) {
                        refreshList(true,true);
                        ToastManager.getInstance().show(context, context.getResources().getString(R.string.success));
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
                            msg.what = LooperThread.UPDATE_FOLLOWING;
                            msg.obj = afFriendInfo;
                            looperThread.handler.sendMessage(msg);
                        }
                    }
                    adapter.setList(mList);
                    adapter.notifyDataSetChanged();

                    break;
            }
        } else {
			/*add by zhh*/
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
    /**
     * 判断是否显示提示控件
     * @param bool
     */
    private void showNoData(boolean bool){
        if(bool){//判断是否有数据显示提示语
            mLinFollowingNoDataArea.setVisibility(View.VISIBLE);
        }else{
            mLinFollowingNoDataArea.setVisibility(View.GONE);
        }
    }

    private void getFollowing(List<AfFriendInfo> lst){
        List<AfFriendInfo> mListTemp = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> arrayAfids = new ArrayList<>();
        for (AfFriendInfo afFriendInfo : lst){
            AfFriendInfo localInfo = CacheManager.getInstance().searchFriendInfoForFollow(afFriendInfo.afId);
            if(localInfo == null || (localInfo != null && TextUtils.isEmpty(localInfo.name))) {
                arrayList.add(afFriendInfo.afId);
            }
            else {
                mListTemp.add(localInfo);
            }
            arrayAfids.add(afFriendInfo.afId);
        }
        if(arrayList.size() == 0){
            mList.addAll(mListTemp);
            adapter.setList(mList);
            adapter.notifyDataSetChanged();
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

    class LooperThread extends Thread {
        private static final int UPDATE_FOLLOWING = 9002;
        Handler handler;
        Looper looper;
        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            handler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case UPDATE_FOLLOWING:
                            AfFriendInfo afFriendInfo = (AfFriendInfo) msg.obj;
                            if(afFriendInfo != null) {
                                MessagesUtils.onAddFollower(Consts.AFMOBI_FOLLOW_TYPE_MASTER, afFriendInfo, true, true);
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
