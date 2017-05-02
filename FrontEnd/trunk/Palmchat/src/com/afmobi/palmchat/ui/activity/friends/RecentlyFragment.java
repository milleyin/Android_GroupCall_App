package com.afmobi.palmchat.ui.activity.friends;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.listener.FragmentSendFriendCallBack;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.friends.adapter.RecentlyAdapter;
import com.afmobi.palmchat.ui.activity.main.ChatsContactsActivity;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.cb.RefreshStateListener;
import com.afmobi.palmchat.ui.activity.main.helper.RefreshNotificationManager;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
//import com.afmobi.palmchat.util.image.ImageLoader;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
//import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hj on 2015/12/23.
 */
public class RecentlyFragment extends BaseFragment implements RefreshStateListener, RefreshNotificationManager.RefreshListener {
    FragmentSendFriendCallBack mFragmentSendFriendCallBack;
    private   final String TAG = "RecentlyFragment";
    private ListView mListView;
    private RecentlyAdapter adapterListView;
    private LooperThread looperThread;
    //没有数据的情况下显示提示语句
    private LinearLayout mLinRecentlyNoDataArea;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        PalmchatLogUtils.i("Recently", "onAttach");
        initData();
        if(activity instanceof ChatsContactsActivity) {
            mFragmentSendFriendCallBack = (ChatsContactsActivity) activity;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PalmchatLogUtils.i("Recently", "onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_recently);
        initViews();
        return mMainView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(TAG);
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
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshData();
                }
            },20);

        }
    }

    /**
     * 刷新数据
     */
    public void refreshData(){
        PalmchatLogUtils.i("Recently", "refreshData");
        if (null != looperThread) {
            Handler handler = looperThread.handler;
            if (null != handler) {
                handler.sendEmptyMessage(LooperThread.UPDATE_DATA);
            }
        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//        MobclickAgent.onPageEnd(TAG);
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
        looperThread.looper.quit();
//        ImageLoader.getInstance().cleanOtherImageCache();
//        PalmchatApp.getApplication().mMemoryBitmapCache.evictAll();
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.listview);
        mLinRecentlyNoDataArea = (LinearLayout)findViewById(R.id.lin_recently_no_data_area);
        adapterListView = new RecentlyAdapter(getActivity(),mFragmentSendFriendCallBack);
        mListView.setAdapter(adapterListView);
    }

    private void initData() {
        looperThread = new LooperThread();
        looperThread.setName(TAG);
        looperThread.start();

    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LooperThread.UPDATE_DATA:
                    List<MainAfFriendInfo> refData = ( List<MainAfFriendInfo>) msg.obj;
                    if(refData.size()<=0){
                        showNoData(true);
                    }else{
                        showNoData(false);
                    }
                    if (refData != null && adapterListView != null) {
                        adapterListView.setAdapterData(refData);
                        adapterListView.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }

    };


    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentSendFriendCallBack = null;
    }

    class LooperThread extends Thread {
        private static final int UPDATE_DATA = 7000;
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
            PalmchatLogUtils.i("Recently", "LooperThread");
            // 保持当前只有一条线程在执行查看数据操作
            handler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case UPDATE_DATA:
                            List<MainAfFriendInfo> tempList = CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).getList();
                            List<MainAfFriendInfo> mainAfFriendInfos = new ArrayList<MainAfFriendInfo>();
                            if (null != tempList) {
                                int size = tempList.size();
                                for (int i = 0; i < size; i++) {
                                    MainAfFriendInfo info = tempList.get(i);
                                    AfFriendInfo afFriendInfo = info.afFriendInfo;
                                    if (null != info && null != afFriendInfo) {
                                        if (afFriendInfo.afId.toLowerCase().startsWith("g") || afFriendInfo.afId.toLowerCase().startsWith("r")) {
                                            continue;
                                        }
                                    }
                                    mainAfFriendInfos.add(info);
                                }
                            }
                            Message mainMsg = new Message();
                            mainMsg.what = UPDATE_DATA;
                            mainMsg.obj = mainAfFriendInfos;
                            mHandler.sendMessage(mainMsg);
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
    public void startRefresh() {
        // TODO Auto-generated method stub

        if (null != looperThread) {
            Handler handler = looperThread.handler;
            if (null != handler) {
                handler.sendEmptyMessage(LooperThread.UPDATE_DATA);
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
     /*   if (v.getId() == R.id.rl_Contacts) {
            startActivity(new Intent(getActivity(), ContactsActivity.class));
        }*/
    }

    @Override
    public void onRefreshState() {
        // TODO Auto-generated method stub
        if (null != looperThread) {
            Handler handler = looperThread.handler;
            if (null != handler) {
                handler.sendEmptyMessage(LooperThread.UPDATE_DATA);
            }
        }
    }

    /**
     * 判断是否显示提示语句
     * @param bool
     */
    private void showNoData(boolean bool){
        if(bool){
            mLinRecentlyNoDataArea.setVisibility(View.VISIBLE);
        }else{
            mLinRecentlyNoDataArea.setVisibility(View.GONE);
        }
    }

}
