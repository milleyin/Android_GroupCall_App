package com.afmobi.palmchat.ui.activity.tagpage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.eventbusmodel.EventFollowNotice;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.HidingScrollListener;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.tagpage.adapter.TopPostsAdapter;
import com.afmobi.palmchat.ui.customview.AdvBannerView.OnJumpEventListener;
import com.afmobi.palmchat.ui.customview.videoview.VedioManager;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfPalmchat;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfPeoplesChaptersList;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import de.greenrobot.event.EventBus;

public class TopPostsFragment extends BaseFragment implements OnClickListener, IXListViewListener,
        AfHttpResultListener, OnJumpEventListener {
    private final String TAG = TopPostsFragment.class.getSimpleName();
    /**
     * 列表
     */
    private XListView mXListView;
    /**
     * 中间件接口
     */
    private AfPalmchat mAfCorePalmchat;
    /**
     * Adapter
     */
    private TopPostsAdapter mTopPostsAdapter;
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
     * 当前tag名
     */
    private String mCurTagName = "";
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
     * 第一行提示语
     */
    private TextView mTv_HintFirst;
    /**
     * 第二行提示语
     */
    private TextView mTv_HintSecond;
    /**
     * 轮询线程
     */
    private LooperThread mLooperThread;
    /**
     * 初始化刷新
     */
    private boolean mIsInitRefresh;

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
                                mTopPostsAdapter.updateData(afChapterInfos, mBln_OperationTask);
                            }
                            showEmptyView(false);
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

    /**
     * 无参构造方法
     */
    public TopPostsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mLooperThread = new LooperThread();
        mLooperThread.setName(TAG);
        mLooperThread.start();
        mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
        mAfChapterList = new ArrayList<AfChapterInfo>();
        mCurTagName = getArguments().getString(IntentConstant.TITLENAME);
        if (null == mCurTagName) {
            mCurTagName = "";
        }
        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_tagposts);
        initViews();
        initData();
        return mMainView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);

    }

    /**
     * View初始化
     */
    private void initViews() {
        View headerView = View.inflate(context, R.layout.tagpagenodata_layout, null);
        mXListView = (XListView) findViewById(R.id.broadcast_tagposts_listview_id);
        mXListView.setXListViewListener(this);
        mXListView.setPullLoadEnable(true);
        mXListView.setPullRefreshEnable(true);
        mXListView.addHeaderView(headerView);
        mTopPostsAdapter = new TopPostsAdapter(getActivity());
        mXListView.setAdapter(mTopPostsAdapter);
        mXListView.setHidingScrollListener(new HidingScrollListener() {

            @Override
            public void onMoved(float distance) {
                // TODO Auto-generated method stub
                Activity activity = getActivity();
                if (null != activity) {
                    ((TagPageActivity) activity).moveTitle(distance, true);
                }

            }

            @Override
            public void onShow() {
                // TODO Auto-generated method stub
                Activity activity = getActivity();
                if (null != activity) {
                    ((TagPageActivity) activity).showAnimation();
                }
            }

            @Override
            public void onHide() {
                // TODO Auto-generated method stub
                Activity activity = getActivity();
                if((null!=activity)&&(activity instanceof TagPageActivity)){
                    ((TagPageActivity)activity).resetTitlePosition();
                }
            }
        });

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

        mV_EmptyView = headerView.findViewById(R.id.broadcast_tagposts_no_data);
        RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParam.setMargins(0, PalmchatApp.getApplication().getWindowHeight() / 5, 0, 0);
        mV_EmptyView.setLayoutParams(layoutParam);
        mV_EmptyView.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });
        mTv_HintFirst = (TextView) headerView.findViewById(R.id.broadcast_tagposts_nodata0_id);
        mTv_HintSecond = (TextView) headerView.findViewById(R.id.broadcast_tagposts_nodata1_id);
    }

    /**
     * 数据初始化
     */
    private void initData() {
        mTv_HintSecond.setVisibility(View.GONE);
        reRefresh();
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

        if (TextUtils.isEmpty(mCurTagName)) {
            ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.broadcast_trending_tagnname_limit);
            return;
        }
        mAfCorePalmchat.AfHttpAfBcgetChaptersRecentHotByNewTag(mPageId, mCurPageIndex * LIMIT, LIMIT, mCurTagName, this);
    }

    /**
     * 处理TopPosts请求
     *
     * @param result
     */
    private void doReqTopPosts(Object result) {
        AfResponseComm afResonseComm = null;
        AfPeoplesChaptersList afPeoplesChaptersList = null;
        if (null != result) {
            try {
                afResonseComm = (AfResponseComm) result;
                afPeoplesChaptersList = (AfPeoplesChaptersList) afResonseComm.obj;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (null != afPeoplesChaptersList && (null != afPeoplesChaptersList.list_chapters)) {
            Activity activity = getActivity();
            if (null != activity && (activity instanceof TagPageActivity)) {
                ((TagPageActivity) activity).setPublicView(afPeoplesChaptersList.pa);
            }

            ArrayList<AfChapterInfo> list_chapters = afPeoplesChaptersList.list_chapters;
            if ((!mBln_OperationTask) && (!mAfChapterList.isEmpty())) {
                mAfChapterList.clear();
            }
            if ((null != list_chapters) && (list_chapters.size() > 0)) {
                mXListView.setPullLoadEnable(true);
                mCurPageIndex++;
            } else if (mBln_OperationTask) {
                mXListView.setPullLoadEnable(false);
                ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.no_data);
            }

            if ((null != mLooperThread) && (mLooperThread.looperHandler != null)) {
                Message msg = Message.obtain();
                msg.what = LooperThread.DOWITH_REQUESTDATA;
                msg.obj = list_chapters;
                mLooperThread.looperHandler.sendMessage(msg);
            }
        }
        showEmptyView(false);
        stopOperation();
        showRefreshSuccess();
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
            if (!mBln_OperationTask) {
                Activity activity = getActivity();
                if (null != activity) {
                    ((TagPageActivity) activity).showRefreshSuccess(0);
                }
            }
        }
    }

    /**
     * 设置空视图显示与否
     */
    private void showEmptyView(boolean isIllegal) {

        if (!isAdded()) {
            return;
        }

        Activity activity = getActivity();
        if (null == activity) {
            return;
        }
        if (mTopPostsAdapter.getCount() <= 0) {

            if (isIllegal) {
                mTv_HintFirst.setText(activity.getResources().getString(R.string.broadcast_tagpage_illegel_nocontent));
                mTv_HintSecond.setText(activity.getResources().getString(R.string.broadcast_tagpage_illegel_nocontent1));
                mTv_HintSecond.setVisibility(View.VISIBLE);
                ((TagPageActivity) activity).showHidePublicView(false);
            } else {
                mTv_HintFirst.setText(activity.getResources().getString(R.string.broadcast_tagpage_nocontent));
                mTv_HintSecond.setText(activity.getResources().getString(R.string.broadcast_tagpage_nocontent1));
                mTv_HintSecond.setVisibility(View.GONE);
                ((TagPageActivity) activity).showHidePublicView(true);
            }
            mV_EmptyView.setVisibility(View.VISIBLE);
        } else {
            mV_EmptyView.setVisibility(View.GONE);
            ((TagPageActivity) activity).showHidePublicView(true);
        }
    }

    /**
     * 获得一张有图片的URL
     *
     * @return
     */
    public String getImageUrl() {
        String imageUrl = "";
        if (null != mTopPostsAdapter) {
            imageUrl = mTopPostsAdapter.getImageUrl();
        }
        return imageUrl;
    }

    public void reRefresh() {
        mIsInitRefresh = true;
        int px = AppUtils.dpToPx(getActivity(), 60);
        mXListView.performRefresh(px);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPause() {

        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

        Point p = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth = p.x;
        int screenHeight = p.y;
        rect = new Rect(0, 0, screenWidth, screenHeight);
    }

    @Override
    public void goToActivity(int item_id) {
        // TODO Auto-generated method stub

    }

    /**
     * 在profile界面follow或者followed的时候刷新界面
     * @param eventFollowNotice
     */
    public void onEventMainThread(EventFollowNotice eventFollowNotice){
        if(mTopPostsAdapter!=null) {
            mTopPostsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        // TODO Auto-generated method stub
        PalmchatLogUtils.e(TAG, "----flag:" + flag + "----code:" + code + "----result:" + result);
        if (Consts.REQ_CODE_SUCCESS == code) {
            switch (flag) {
                case Consts.REQ_BCGET_RECENT_HOTS_BY_TAGNAME: {
                    doReqTopPosts(result);

                    break;
                }
            }



        } else if (code == Consts.REQ_CODE_104) {//PageID失效 重新获取
            onRefresh(null);
        } else if (Consts.REQ_CODE_TAG_ILLEGAL == code) {
            stopOperation();
            showEmptyView(true);
        } else {
            stopOperation();
            showEmptyView(false);
            Consts.getInstance().showToast(context, code, flag, http_code);
        }
    }

    /**
     * 发送了广播
     *
     * @param afChapterInfo
     */
    public void onEventMainThread(AfChapterInfo afChapterInfo) {
        //消息通知
        if (Constants.BROADCAST_NOTIFICATION_ACTION == afChapterInfo.eventBus_action) {

        } else if (Constants.UPDATE_DELECT_BROADCAST == afChapterInfo.eventBus_action) {
            mTopPostsAdapter.notifyDataSetChanged_removeBymid(afChapterInfo.mid);//modify by wxl
            CacheManager.getInstance().remove_BC_RecordSendSuccessDataBy_mid(afChapterInfo.mid);
            showEmptyView(false);
        } else if (Constants.UPDATE_LIKE == afChapterInfo.eventBus_action) {
            mTopPostsAdapter.updateLikeOrComment(afChapterInfo);

        }
    }

    @Override
    public void onRefresh(View view) {
        // TODO Auto-generated method stub
        if (NetworkUtils.isNetworkAvailable(PalmchatApp.getApplication())) {
            mBln_OperationTask = false;
            if (!mIsInitRefresh) {
                ((TagPageActivity) getActivity()).resetTitlePosition();
            }
            mIsInitRefresh = false;
            loadData(false);
        } else {
            ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.network_unavailable);
            showEmptyView(false);
            stopRefresh();
            if (!mIsInitRefresh) {
                ((TagPageActivity) getActivity()).resetTitlePosition();
            }
            mIsInitRefresh = false;
        }
    }

    @Override
    public void onLoadMore(View view) {
        // TODO Auto-generated method stub
        if (NetworkUtils.isNetworkAvailable(PalmchatApp.getApplication())) {
            mBln_OperationTask = true;
            loadData(true);
        } else {
            ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.network_unavailable);
            stopLoadMore();
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();



        EventBus.getDefault().unregister(this);
        if (mAfChapterList != null) {
            mAfChapterList.clear();
        }
        if (mTopPostsAdapter != null) {
            mTopPostsAdapter.cleanData();
        }
    }

    @SuppressLint("NewApi")
    public void setListViewOffSet(float offSet) {
        if (null != mXListView) {
            if (mXListView.getScrollY() < offSet) {
                mXListView.setBlankViewHeight(offSet);
            }
        }
    }

    @SuppressLint("NewApi")
    public void resetListViewPosition() {
        if (null != mXListView) {
            mXListView.setBlankViewHeight(0);
        }
    }

    /**
     * 列表停止滑动
     */
    public void stopListViewScroll() {
        if (null != mXListView) {
            mXListView.stopListViewScroll();
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
