package com.afmobi.palmchat.ui.activity.publicaccounts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.publicaccounts.adapter.AccoutntsHistoryAdapter;
import com.afmobi.palmchat.ui.customview.videoview.CustomVideoController;
import com.afmobi.palmchat.ui.customview.videoview.VedioManager;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobigroup.gphone.R;
import com.core.AfFriendInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfPeoplesChaptersList;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import de.greenrobot.event.EventBus;

public class PublicAccountsHistoryActivity extends BaseActivity implements OnClickListener, IXListViewListener, AfHttpResultListener {
    private final String TAG = PublicAccountsHistoryActivity.this.getClass().getSimpleName();
    /**
     * 当前id
     */
    private String mCurAccountsId;
    /**
     * 当前页序号
     */
    private int mCurPageIndex;
    /**
     * PageId
     */
    private int mPageId;
    /**
     * 一页的数量
     */
    private final int LIMIT = 30;
    /**
     * 当前操作状态,默认是刷新  true为loadMore;false为reFresh
     */
    private boolean mBln_OperationTask = false;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    /**
     * 广播列表
     */
    private ArrayList<AfChapterInfo> mAfChapterList;
    /**
     * 空视图
     */
    private View mV_EmptyView;
    /**
     * 轮询线程
     */
    private LooperThread mLooperThread;
    /**
     * 列表
     */
    private XListView mXListView;
    /**
     * Title
     */
    private TextView mTV_Title;
    /**
     * 返回按钮
     */
    private ImageView mIv_back;
    /**
     * 刷新成功的视图
     */
    private View mV_TopRefresh;
    /**
     * 中间件接口
     */
    private AfPalmchat mAfCorePalmchat;
    /**
     * 公共账号名
     */
    private AfFriendInfo mAfFriendInfo;
    /**
     * Adapter
     */
    private AccoutntsHistoryAdapter mAccoutntsHistoryAdapter;
    /**
     * 视频相关
     */
    private Rect rect;

    /**
     * handler
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case LooperThread.DOWITH_REQUESTDATA: {
                    if (null != msg.obj) {
                        ArrayList<AfChapterInfo> afChapterInfos = null;
                        try {
                            afChapterInfos = (ArrayList<AfChapterInfo>) msg.obj;
                            if (null != afChapterInfos) {
                                mAfChapterList.addAll(afChapterInfos);
                                mAccoutntsHistoryAdapter.updateData(afChapterInfos, mBln_OperationTask);
                            }
                            showEmptyView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth = p.x;
        int screenHeight = p.y;
        rect = new Rect(0, 0, screenWidth, screenHeight);

    }

    @Override
    public void findViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_publicaccounts_history);
        mV_EmptyView = findViewById(R.id.publichistory_nodata_layout);
        mXListView = (XListView) findViewById(R.id.publichistory_listview_id);
        mXListView.setXListViewListener(this);
        mXListView.setPullLoadEnable(true);
        mXListView.setPullRefreshEnable(true);

        mXListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (VedioManager.getInstance().getVideoController() != null) {
                    if (!VedioManager.getInstance().getVideoController().getVisibleRect(rect)) {
                        VedioManager.getInstance().getVideoController().stop();
                    }
                }

            }
        });


        mV_TopRefresh = findViewById(R.id.publichistory_top_refresh);
        mTV_Title = (TextView) findViewById(R.id.title_text);
        mTV_Title.setText(getString(R.string.public_accountdetails_broadcasthistory));
        mIv_back = (ImageView) findViewById(R.id.back_button);
        mIv_back.setOnClickListener(this);
        mAccoutntsHistoryAdapter = new AccoutntsHistoryAdapter(this);
        mXListView.setAdapter(mAccoutntsHistoryAdapter);


    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        mAfChapterList = new ArrayList<AfChapterInfo>();

        Intent intent = getIntent();
        //IntentConstant.PROFILE
        try {
            mAfFriendInfo = (AfFriendInfo) intent.getSerializableExtra(IntentConstant.PROFILE);
        } catch (Exception e) {
            mAfFriendInfo = new AfFriendInfo();
            e.printStackTrace();
        }
        //2代表公共账号
        mAfFriendInfo.user_class = DefaultValueConstant.BROADCAST_PROFILE_PA;//
        mCurAccountsId = mAfFriendInfo.afId;
        mAccoutntsHistoryAdapter.setCurPAccountsId(mCurAccountsId);
        mLooperThread = new LooperThread();
        mLooperThread.setName(TAG);
        mLooperThread.start();
        mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
        EventBus.getDefault().register(this);
        reRefresh();
    }


    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        // TODO Auto-generated method stub
        PalmchatLogUtils.e(TAG, "----flag:" + flag + "----code:" + code + "----result:" + result);
        if (Consts.REQ_CODE_SUCCESS == code) {
            switch (flag) {
                case Consts.REQ_BCGET_PROFILE_BY_AFID: {
                    doReqRecentPosts(result);


                    break;
                }
            }

        } else if (code == Consts.REQ_CODE_104) {//PageID失效 重新获取
            onRefresh(null);
        } else {
            stopOperation();
            showEmptyView();
            Consts.getInstance().showToast(context, code, flag, http_code);
        }
    }

    @Override
    public void onRefresh(View view) {
        // TODO Auto-generated method stub
        if (NetworkUtils.isNetworkAvailable(PalmchatApp.getApplication())) {
            mBln_OperationTask = false;
            mXListView.setPullLoadEnable(true);
            loadData(false);
        } else {
            ToastManager.getInstance().show(PalmchatApp.getApplication(), getString(R.string.network_unavailable));
            stopRefresh();
        }
    }

    @Override
    public void onLoadMore(View view) {
        // TODO Auto-generated method stub
        if (NetworkUtils.isNetworkAvailable(PalmchatApp.getApplication())) {
            mBln_OperationTask = true;
            loadData(true);
        } else {
            ToastManager.getInstance().show(PalmchatApp.getApplication(), getString(R.string.network_unavailable));
            stopLoadMore();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.back_button: {
                finish();
                break;
            }

            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        boolean isPlay = VoiceManager.getInstance().isPlaying();
        if (isPlay) {
            VoiceManager.getInstance().completion();
        }

        if (VedioManager.getInstance().getVideoController() != null)
            VedioManager.getInstance().getVideoController().pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (VedioManager.getInstance().getVideoController() != null)
            VedioManager.getInstance().getVideoController().stop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        EventBus.getDefault().unregister(this);
        boolean isPlay = VoiceManager.getInstance().isPlaying();
        if (isPlay) {
            VoiceManager.getInstance().completion();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case IntentConstant.SHARE_BROADCAST: {
                if (null != intent) {
                    boolean isSuccess = intent.getBooleanExtra(JsonConstant.KEY_SEND_IS_SEND_SUCCESS, false);
                    String tempTipContent = intent.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL);
                    String tipContent;
                    if (isSuccess) {
                        tipContent = getResources().getString(R.string.share_friend_success);
                    } else {
                        if (!TextUtils.isEmpty(tempTipContent)) {
                            tipContent = tempTipContent;
                        } else {
                            tipContent = getResources().getString(R.string.share_friend_failed);
                        }
                    }
                    ToastManager.getInstance().showShareBroadcast(this, DefaultValueConstant.SHARETOASTTIME, isSuccess, tipContent);
                }
                break;
            }
        }
    }

    /**
     * 发送了广播
     *
     * @param afChapterInfo
     */
    public void onEventMainThread(AfChapterInfo afChapterInfo) {

        if (afChapterInfo.eventBus_action == Constants.UPDATE_BROADCAST_MSG) {//发广播 更新状态
            AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
            if ((profileInfo.afId.equals(mCurAccountsId))) {
                CacheManager.getInstance().setBC_RecordSendSuccessData(afChapterInfo);
                mAccoutntsHistoryAdapter.updateDataStatus(afChapterInfo);
                setListScrolltoTop(false);
            }
        } else if (Constants.BROADCAST_NOTIFICATION_ACTION == afChapterInfo.eventBus_action) { //消息通知

        } else if (Constants.UPDATE_DELECT_BROADCAST == afChapterInfo.eventBus_action) {
            mAccoutntsHistoryAdapter.updateDataStatus(afChapterInfo._id);
            CacheManager.getInstance().remove_BC_RecordSendSuccessDataBy_mid(afChapterInfo.mid);
        } else if (Constants.UPDATE_LIKE == afChapterInfo.eventBus_action) {
            mAccoutntsHistoryAdapter.updateLikeOrComment(afChapterInfo);
        }
    }

    /**
     * 重新拉取数据
     */
    private void loadData(boolean isLoadMore) {
        //重新加载数据，页数置为0
        if (!isLoadMore) {
            mCurPageIndex = 0;
            mPageId = (int) System.currentTimeMillis() + new Random(10000).nextInt();
        }

        if (TextUtils.isEmpty(mCurAccountsId)) {
            ToastManager.getInstance().show(this, R.string.broadcast_publicaccount_accountid_limit);
            return;
        }
        mAfCorePalmchat.AfHttpBcgetProfileByAfid(mPageId, mCurPageIndex * LIMIT, LIMIT, mCurAccountsId, null, this);
    }

    /**
     * 处理返回数据请求
     *
     * @param result
     */
    private void doReqRecentPosts(Object result) {
        AfResponseComm afResonseComm = null;
        AfPeoplesChaptersList afPeoplesChaptersList = null;
        stopOperation();
        if (null != result) {
            try {
                afResonseComm = (AfResponseComm) result;
                afPeoplesChaptersList = (AfPeoplesChaptersList) afResonseComm.obj;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (null != afPeoplesChaptersList && (null != afPeoplesChaptersList.list_chapters)) {
            ArrayList<AfChapterInfo> list_chapters = afPeoplesChaptersList.list_chapters;

            if ((!mBln_OperationTask) && (!mAfChapterList.isEmpty())) {
                mAfChapterList.clear();
            }

            if (list_chapters.size() > 0) {
                mCurPageIndex++;
                if(!mBln_OperationTask){//不是loadmore的 有数据才显示
                    showRefreshSuccess();
                }
            } else if (mBln_OperationTask) {
                mXListView.setPullLoadEnable(false);
                ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.no_data);
            }else{//广播数为空 且 不是loadmore
                showEmptyView();
            }

            if ((null != mLooperThread) && (mLooperThread.looperHandler != null)) {
                Message msg = Message.obtain();
                msg.what = LooperThread.DOWITH_REQUESTDATA;
                msg.obj = list_chapters;
                mLooperThread.looperHandler.sendMessage(msg);
            }

        }

    }

    public void reRefresh() {
        if (mXListView != null && mXListView.getCount() > 0) {
            mXListView.setSelection(0);
        }
        int px = AppUtils.dpToPx(this, 60);
        mXListView.performRefresh(px);
    }

    /**
     * 滚到最顶部
     */
    public void setListScrolltoTop(boolean isFromTab) {
        if (mXListView != null && mXListView.getCount() > 0) {
            mXListView.stopListViewScroll();
            mXListView.setSelection(0);
        }
    }

    /**
     * 停止操作
     */
    private void stopOperation() {
        if (mBln_OperationTask) {
            stopLoadMore();
        } else {
            stopRefresh();
        }
    }

    /**
     * 停止刷新
     */
    private void stopRefresh() {
        mXListView.stopRefresh(true);
        mXListView.setRefreshTime(mDateFormat.format(new Date(System.currentTimeMillis())));
    }

    /**
     * 停止加载更多
     */
    private void stopLoadMore() {
        //isLoadingMore_mListview = false;
        mXListView.stopLoadMore();
    }


    /**
     * 刷新成功的提示
     */
    private void showRefreshSuccess() {
        if (!mBln_OperationTask) {
            mV_TopRefresh.getBackground().setAlpha(100);
            mV_TopRefresh.setVisibility(View.VISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mV_TopRefresh.setVisibility(View.GONE);
                    PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_REFRESH);
                }
            }, 1000);
        }
    }

    /**
     * 设置空视图显示与否
     */
    private void showEmptyView() {
        if (mAccoutntsHistoryAdapter.getCount() <= 0) {
            mV_EmptyView.setVisibility(View.VISIBLE);
        } else {
            mV_EmptyView.setVisibility(View.GONE);
        }
    }


    /**
     * @author star
     */
    class LooperThread extends Thread {
        private static final int DOWITH_REQUESTDATA = 1001;
        Handler looperHandler;
        Looper looper;

        @SuppressLint("HandlerLeak")
        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            looperHandler = new Handler() {
                @SuppressWarnings("unchecked")
                public void handleMessage(Message msg) {
                    switch (msg.what) {

                        case DOWITH_REQUESTDATA: {
                            if (null != msg.obj) {
                                ArrayList<AfChapterInfo> afChapterInfos = null;
                                try {
                                    afChapterInfos = (ArrayList<AfChapterInfo>) msg.obj;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (null != afChapterInfos) {
                                    for (int i = 0; i < afChapterInfos.size(); i++) {
                                        AfChapterInfo afChapterInfo = afChapterInfos.get(i);
                                        String mid = afChapterInfo.mid;
                                        if (null != afChapterInfo) {
                                            afChapterInfos.get(i).isLike = mAfCorePalmchat.AfBcLikeFlagCheck(mid);
                                            afChapterInfo.profile_Info = mAfFriendInfo;
                                        }
                                    }
                                }
                                Message msg1 = Message.obtain();
                                msg1.what = DOWITH_REQUESTDATA;
                                msg1.obj = afChapterInfos;
                                mHandler.sendMessage(msg1);
                            }
                            break;
                        }
                        default:
                            break;
                    }
                }
            };
            Looper.loop();
        }
    }

}
