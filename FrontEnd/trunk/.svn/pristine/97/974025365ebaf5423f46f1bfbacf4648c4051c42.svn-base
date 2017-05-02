package com.afmobi.palmchat.ui.activity.palmcall;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.eventbusmodel.UpdatePalmCallDurationEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.HidingScrollListener;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.payment.MyWapActivity;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobigroup.gphone.R;
import com.core.AfLoginInfo;
import com.core.AfPalmCallResp;
import com.core.AfPalmCallResp.AfPalmCallHotListItem;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import java.io.Serializable;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import de.greenrobot.event.EventBus;

/**
 * Created by heguiming on 2016/6/21.
 */
public class PalmCallFragment extends BaseFragment implements AfHttpResultListener, IXListViewListener {

    private final String TAG = PalmCallFragment.class.getSimpleName();
    private Context mContext;
    private XListView mListView;
    private PalmCallAdapter mPalmCallAdapter;
    private ArrayList<AfPalmCallHotListItem> mPalmcallList = new ArrayList<>();

    //保存请求hotlist返回的数据
    private AfPalmCallResp resp;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
    //增加listview头部
    private View mVCallHeaderview;
    private TextView mTvCallRechargeOften, mTvCallRecharge;
    private boolean isRefreshing = false;
    private boolean isLoadMore = false;
    private String mCurrentCallId = "";
    private AfPalmchat mAfPalmchat;
    /**
     * 拨打电话状态
     */
    private boolean mBL_toCall;
    /**
     * 自己剩余时间
     */
    public int mLeftTime;
    //奖励通话时长
    private int mRewardTime;
    // rechargeUrl，GetHotList返回
    private String rechargeUrl;
    private Handler mHandler = new Handler();
    private LooperHandler looperThread;
    /**
     * 自己的afid
     */
    private String mCurAfId;
    //保存请求了自己个人的信息
    private AfPalmCallResp mMyResp;
    //保存最新刷新时间
    private long timeLastRefresh;
    private static final int mTiming = 10 * 60 * 1000;
    /**
     * 通话时长请求id
     */
    private int mCurRefreshAvaiableTimeHttpId;
    //奖励dialog
    private Dialog mDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContext = getActivity();
        looperThread = new LooperHandler();
        looperThread.setName(TAG);
        looperThread.start();
        findViews();
        init();
        /*注册刷新当前通话时长事件 当系统推送B31消息时*/
        EventBus.getDefault().register(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            boolean isBackgroupLogin = bundle.getBoolean(JsonConstant.KEY_IS_BACKGROUD_LOGIN);
            if (isBackgroupLogin) {
                setListviewHeader(false);
            }
        }
        return mMainView;

    }

    private void findViews() {
        mAfPalmchat = ((PalmchatApp) context.getApplicationContext()).mAfCorePalmchat;
        setContentView(R.layout.fragment_palmcall);
        mCurAfId = CacheManager.getInstance().getMyProfile().afId;
        mVCallHeaderview = LayoutInflater.from(mContext).inflate(R.layout.palmcallfragment_headerview, null);
        mTvCallRechargeOften = (TextView) mVCallHeaderview.findViewById(R.id.tv_call_recharge_often);
        setRemainingTime(0);
        mTvCallRecharge = (TextView) mVCallHeaderview.findViewById(R.id.tv_call_recharge);//recharge按钮
        mTvCallRecharge.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //跳转到wap版网页充值页面
                if (rechargeUrl != null && !rechargeUrl.isEmpty()) {
                   toPalmCallRecharge();
                }
            }
        });
        mListView = (XListView) findViewById(R.id.ls_palmcall);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);
        mListView.addHeaderView(mVCallHeaderview);
        mPalmCallAdapter = new PalmCallAdapter(getActivity());
        mListView.setAdapter(mPalmCallAdapter);
        mListView.setHidingScrollListener(new HidingScrollListener() {

            @Override
            public void onMoved(float distance) {
                Activity activity = getActivity();
                if (null != activity) {
                    ((MainTab) activity).moveTitle(distance, true);
                }
            }

            @Override
            public void onShow() {

            }

            @Override
            public void onHide() {
                Activity activity = getActivity();
                if (activity != null && (activity instanceof MainTab)) {
                    ((MainTab) activity).resetTitlePosition();
                }
            }
        });
        mListView.setPullLoadEnable(false);//禁止上拉加载
    }

    /**
     * 数据初始化
     */
    private void init() {

    }

    /**
     * 是否显示ListviewHeader
     *
     * @param isShow
     */
    public void setListviewHeader(boolean isShow) {
        if (mListView != null) {
            if (isShow) {
                mListView.setBlankViewHeight(0);
                if (mVCallHeaderview != null) {
                    mVCallHeaderview.setVisibility(View.VISIBLE);
                }
            } else {
                mListView.setBlankViewHeight(getResources().getDimensionPixelSize(R.dimen.home_listview_margin_height));
                if (mVCallHeaderview != null) {
                    mVCallHeaderview.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    class LooperHandler extends Thread {
        private static final int UPDATE_DB = 1000;
        private static final int INSERT_DB = 2000;
        Handler handler;
        Looper looper;

        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case UPDATE_DB:
                            ArrayList<AfPalmCallHotListItem> items = (ArrayList<AfPalmCallHotListItem>) msg.obj;
                            for (AfPalmCallHotListItem item : items) {
                                AfPalmCallResp resp = mAfPalmchat.AfDbPalmCallInfoGet(item.afid);
                                if (resp != null && resp.respobj != null) {
                                    ArrayList<AfPalmCallHotListItem> itemsTemp = (ArrayList<AfPalmCallHotListItem>) resp.respobj;
                                    if (itemsTemp != null && itemsTemp.size() > 0) {
                                        AfPalmCallHotListItem itemTemp = itemsTemp.get(0);
                                        if ((TextUtils.isEmpty(itemTemp.mediaDescUrl) && !TextUtils.isEmpty(item.mediaDescUrl))
                                                || (!TextUtils.isEmpty(itemTemp.mediaDescUrl) && !TextUtils.isEmpty(item.mediaDescUrl) && !itemTemp.mediaDescUrl.equals(item.mediaDescUrl))
                                                || (!TextUtils.isEmpty(itemTemp.name) && !TextUtils.isEmpty(item.name) && !itemTemp.name.equals(item.name))
                                                || itemTemp.age != item.age
                                                || (TextUtils.isEmpty(itemTemp.coverUrl) && !TextUtils.isEmpty(item.coverUrl))
                                                || (!TextUtils.isEmpty(itemTemp.coverUrl) && !TextUtils.isEmpty(item.coverUrl) && !itemTemp.coverUrl.equals(item.coverUrl))
                                                || itemTemp.sex != item.sex) {
                                            mAfPalmchat.AfDbPalmCallInfoUpdate(item);
                                        }
                                    }
                                }
                            }
                            break;
                        case INSERT_DB:
                            AfPalmCallHotListItem listItem = (AfPalmCallHotListItem) msg.obj;
                            PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallInfoInsert(listItem);//插入数据库
                            break;
                    }
                }
            };
            looper.loop();
        }
    }

    /**
     * 判断当前界面是否正在显示
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mPalmCallAdapter == null) {
            return;
        }
        if (isVisibleToUser) {
            judgeToPalmcallActivity();
            if (mPalmCallAdapter.getCount() <= 0 || (System.currentTimeMillis() - timeLastRefresh) > mTiming) {
                showRefresh();
            } else {
                int rewardtime = SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getPalmcallTime();
                if (rewardtime > 0 && !SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getPalmcallRewardTime()) {
                    rewardTime(rewardtime, (mPalmcallList != null && !mPalmcallList.isEmpty()), true);
                } else if (SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getPalmcallRewardTime()
                        || !SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getPalmCallIsGuide()) {
                    if (mPalmcallList != null && !mPalmcallList.isEmpty()) {
                        showPalmcallGuide(true);
                    }
                }
            }
            PalmCallVoiceManager.getInstance().isSameFragment = true;
        } else {
            PalmCallVoiceManager.getInstance().stopDownloadAnim();
            PalmCallVoiceManager.getInstance().isSameFragment = false;
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }
    }

    /**
     * 请求call列表数据
     */
    private void getHotList(boolean bool) {
        PalmchatLogUtils.i("WXL", "getHotList()");
        PalmchatApp.getApplication().mAfCorePalmchat.AfHttpPalmCallGetHotList(bool, null, this);
    }

    /**
     * 获取自己的信息
     * 跳转到拨电话窗口
     */
    private void getMyInfo() {
        if (null != mCurAfId) {
            //判断数据库里面是否有自己的info资料
            if (PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallInfoGet(mCurAfId) == null) {
                PalmchatApp.getApplication().mAfCorePalmchat.AfHttpPalmCallGetInfo(mCurAfId, null, this);
            }
        }
    }

    public void toPalmCallDetail() {
        if (getActivity() != null && mPalmcallList != null && mPalmcallList.size() > 0) {
            AfPalmCallHotListItem afPalmCallHotListItem = mPalmcallList.get(0);
            Intent intent = new Intent(getActivity(), PalmCallDetailDialogActivity.class);
            intent.putExtra(JsonConstant.CALLLISTITEM, (Serializable) afPalmCallHotListItem);
            intent.putExtra(JsonConstant.CALLLISTTIME, mLeftTime);
            intent.putExtra("listitem", afPalmCallHotListItem);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PalmCallVoiceManager.getInstance().setViewisHome(true);
        PalmCallVoiceManager.getInstance().isSameFragment = true;
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        PalmchatLogUtils.i(TAG, "----AfOnResult--Flag:" + flag + "---Code:" + code + "  result:" + result);
        if (code == Consts.REQ_CODE_SUCCESS) {//请求成功
            switch (flag) {
                case Consts.REQ_PALM_CALL_HOT_LIST://获取call列表
                    getMyInfo();
                    stopVoice();
                    PalmCallVoiceManager.getInstance().stopDownloadAnim();//取消下载动画
                    resp = (AfPalmCallResp) result;
                    mPalmcallList = (ArrayList<AfPalmCallHotListItem>) resp.respobj;
                    mLeftTime = resp.leftTime;//自己的还剩多长通话时间
                    setRemainingTime(mLeftTime);
                    mRewardTime = resp.rewardTime;//一个账户第一次使用的时候会有个奖励通话时长
                    rechargeUrl = resp.rechargeUrl;
                    PalmcallManager.getInstance().setRechargeUrl(rechargeUrl);
                    if (mRewardTime > 0) {//大于0的话说明有赠送时长
                        SharePreferenceUtils.getInstance(mContext).setPalmCallTime(mRewardTime);
                        if (getUserVisibleHint()) {
                            rewardTime(mRewardTime, (mPalmcallList != null && !mPalmcallList.isEmpty()), false);
                        }
                    } else {
                        if (mPalmcallList != null && !mPalmcallList.isEmpty()) {
                            if (SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getPalmcallRewardTime()
                                    || !SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getPalmCallIsGuide()) {
                                /**********新手导航***********/
                                showPalmcallGuide(false);
                                /**********新手导航***********/
                            }
                        }
                    }
                    if (mPalmcallList != null && !mPalmcallList.isEmpty()) {//判断是否有数据
                        int islist = mPalmcallList.size();
                        for (int i = 0; i < islist; i++) {//删除掉自己afid账号数据
                            if (mPalmcallList.get(i).afid.equals(CacheManager.getInstance().getMyProfile().afId)) {
                                mPalmcallList.remove(i);
                            }
                        }
                        mPalmCallAdapter.updatelist(mPalmcallList, mLeftTime, isLoadMore, System.currentTimeMillis());
                        /**********更新profile***********/
                        Message message = new Message();
                        message.obj = mPalmcallList;
                        message.what = LooperHandler.UPDATE_DB;
                        looperThread.handler.sendMessage(message);
                        /**********更新profile***********/
                    } else {
                        mPalmCallAdapter.updatelist(mPalmcallList, mLeftTime, isLoadMore, System.currentTimeMillis());
                    }
                    timeLastRefresh = System.currentTimeMillis();//获取最新的刷新时间
                    settingStop();//停止刷新
                    break;
                case Consts.REQ_PALM_CALL_GET_INFO:
                    mMyResp = (AfPalmCallResp) result;
//                    AfPalmCallResp.AfPalmCallHotListItem mMyItem = (AfPalmCallResp.AfPalmCallHotListItem)mMyResp.respobj;
                    ArrayList<AfPalmCallHotListItem> mMyItem = (ArrayList<AfPalmCallHotListItem>) mMyResp.respobj;
                    if (null != mMyItem) {
                        AfPalmCallResp.AfPalmCallHotListItem listItem = new AfPalmCallResp.AfPalmCallHotListItem();
                        for (int i = 0; i < mMyItem.size(); i++) {
                            listItem = mMyItem.get(i);
                        }
                        Message message = new Message();
                        message.obj = listItem;
                        message.what = LooperHandler.INSERT_DB;
                        looperThread.handler.sendMessage(message);
                    }
                    break;
                case Consts.REQ_PALM_CALL_GET_LEFTTIME:
                    doResultGetLeftTime(result);
                    break;
                default:
                    break;
            }
        } else {//请求失败
            if (flag == Consts.REQ_PALM_CALL_MAKECALL) {
                dismissDialog();
            }
            Consts.getInstance().showToast(mContext, code, flag, http_code);
            settingStop();//返回数据后停止刷新
        }
    }


    /**
     * 处理自己的剩余时间
     *
     * @param result
     */
    private void doResultGetLeftTime(Object result) {
        AfPalmCallResp afPalmCallResp = null;
        try {
            afPalmCallResp = (AfPalmCallResp) result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (afPalmCallResp != null) {
            mLeftTime = afPalmCallResp.leftTime;
            setRemainingTime(mLeftTime);
        }
    }

    /**
     * 弹出奖励通话时长dialog
     */
    public void rewardTime(final int time, final boolean isPalmcallList, final boolean ischeck) {
        mDialog = new Dialog(mContext, R.style.Theme_Hold_Dialog_Base);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = LayoutInflater.from(context);
        View viewdialog = inflater.inflate(R.layout.dialog_palmcall_rewardtime, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDialog.setContentView(viewdialog, layoutParams);
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display mDisplay = m.getDefaultDisplay();

        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);//居中显示
        lp.width = (int) (mDisplay.getWidth() / 1.0); // 宽度
        lp.height = (int) (mDisplay.getHeight() / 1.3); // 高度
        dialogWindow.setAttributes(lp);
        TextView okTextView = (TextView) viewdialog.findViewById(R.id.tv_rewardtime_ok);
        TextView timeTextView = (TextView) viewdialog.findViewById(R.id.tv_reward_time);
        String text = String.format(getResources().getString(R.string.call_reward_time), time);
        SpannableStringBuilder styke = new SpannableStringBuilder(text);
        String mTime = time + "";
        int index = text.indexOf(mTime);
        styke.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.log_blue)), index, index + mTime.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        timeTextView.setText(styke);
        okTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isPalmcallList) {
                    /**********新手导航***********/
                    showPalmcallGuide(ischeck);
                    /**********新手导航***********/
                }
                mDialog.dismiss();
            }
        });
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (getUserVisibleHint()) {
                    SharePreferenceUtils.getInstance(mContext).setPalmCallRewardTime(true);//表示奖励dialog已弹出过
                    SharePreferenceUtils.getInstance(mContext).setPalmCallTime(time);//设置奖励的为0
                }
            }
        });
        mDialog.show();
    }

    /**
     * 显示新手导航
     */
    private void showPalmcallGuide(boolean isCheck) {
        MainTab mainTab = (MainTab) getActivity();
        if (mainTab != null) {
            mainTab.setPalmcallTransparent(isCheck);
            mainTab.showPalmcallGuide(isCheck);
        }
    }

    /**
     * 上拉刷新数据
     *
     * @param view
     */
    @Override
    public void onRefresh(View view) {
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            PalmchatLogUtils.i("WXL", "onRefresh isRefreshing=" + isRefreshing);
            if (!isRefreshing) {
                isRefreshing = true;
                isLoadMore = false;
                //判断是否卸载了应用
                if (!SharePreferenceUtils.getInstance(mContext).getPalmcallRewardTime()) {
                    getHotList(true);
                } else {
                    getHotList(false);
                }
                two_minutes_Cancel_Refresh_Animation();
            }
        } else {
            ToastManager.getInstance().show(mContext, getUserVisibleHint(), context.getString(R.string.network_unavailable));
            stopRefresh();
        }
    }

    /**
     * 下拉加载数据
     *
     * @param view
     */
    @Override
    public void onLoadMore(View view) {
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            if (!isLoadMore) {
                isLoadMore = true;
                isRefreshing = false;
            }
        } else {
            ToastManager.getInstance().show(mContext, getUserVisibleHint(), context.getString(R.string.network_unavailable));
            stopRefresh();
        }
    }

    /**
     * 显示刷新
     */
    private void showRefresh() {
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            PalmchatLogUtils.i("WXL", "showRefresh isRefreshing=" + isRefreshing);
            if (getActivity() != null && mListView != null) {
                int px = AppUtils.dpToPx(getActivity(), 56);
                mListView.performRefresh(px);
                isRefreshing = true;
                isLoadMore = false;
            }
        } else {
            ToastManager.getInstance().show(mContext, mFragmentVisible, context.getString(R.string.network_unavailable));
            stopRefresh();
        }
    }

    /**
     * 停止刷新
     */
    private void stopRefresh() {
        isRefreshing = false;
        mListView.stopRefresh(true);
        mListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
    }

    private void stopLoadMore() {
        isLoadMore = false;
        mListView.stopLoadMore();
    }

    public void settingStop() {
        if (isRefreshing) {
            stopRefresh();
        }
        if (isLoadMore) {
            stopLoadMore();
        }
    }

    /**
     * 去掉等待条
     */
    private void dismissDialog() {
        mBL_toCall = false;
        dismissProgressDialog();
    }

    public void onDestroy() {
        super.onDestroy();
        PalmcallManager.getInstance().setCallSomeOneStateListener(null);
        PalmchatApp.getApplication().mAfCorePalmchat.AfHttpCancel(mCurRefreshAvaiableTimeHttpId);
        if (null != mPalmcallList) {
            mPalmcallList.clear();
        }
        mBL_toCall = false;
        /*if(null != timer){
            timer.cancel();
        }*/
        /*注销掉刷新当前时长事件*/
        EventBus.getDefault().unregister(this);
        stopVoice();
    }


    /**
     * 为了防止中间件会把请求丢了 不返回的情况 2分钟后重置状态
     */
    public void two_minutes_Cancel_Refresh_Animation() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (isRefreshing) {
                    settingStop();
                }
                PalmchatLogUtils.e(TAG, "---two_minutes_Cancel_Refresh_Animation");

            }
        }, Constants.TWO_MINUTER);
    }


    /* 接收到palmcall类型 B31消息时，刷新当前通话时长*/
    public void onEventMainThread(UpdatePalmCallDurationEvent event) {
        if (event.getLeftTime() < 0) {
            mCurRefreshAvaiableTimeHttpId = PalmchatApp.getApplication().mAfCorePalmchat.AfHttPalmCallGetLeftTime(null, this);
        } else {
            mLeftTime = (int) event.getLeftTime();
            setRemainingTime(mLeftTime);
        }
    }

    /**
     * 暂停语音播放
     */
    private void stopVoice() {
        if (VoiceManager.getInstance().isPlaying()) {
            VoiceManager.getInstance().pause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        PalmCallVoiceManager.getInstance().setViewisHome(false);
        VoiceManager.getInstance().completion();//是否要停止播放
        PalmCallVoiceManager.getInstance().stopDownloadAnim();
    }

    private void setRemainingTime(int time) {
        String text = String.format(getResources().getString(R.string.palmcall_surplus_minutes), time);
        SpannableStringBuilder styke = new SpannableStringBuilder(text);
        String mTime = time + "";
        int index = text.indexOf(mTime);
        styke.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.log_blue)), index, index + mTime.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mTvCallRechargeOften.setText(styke);
    }

    /**
     * 当两次点击Local时 回到顶部
     */
    public void setListScrolltoTop() {
        if (mListView != null && mListView.getCount() > 0) {
            mListView.smoothScrollToPosition(0);
        }
        mListView.setListScrolltoTop();
    }

    /**
     * 判断是否打开Palmcall
     */
    private void judgeToPalmcallActivity(){
        if((PalmcallManager.getInstance().isPalmcalling())){
            PalmchatLogUtils.i(TAG,"-----------------judgeToPalmcallActivity-------------------");
            Intent intent = new Intent(getActivity(), PalmcallActivity.class);
            intent.putExtra(IntentConstant.PALMCALLNOTIRYCALL, true);
            startActivity(intent);
        }
    }

    /**
     * palmcall充值
     */
    private void toPalmCallRecharge() {
        try {
            String sessionid = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession();
            String countryCode = AppUtils.getCCFromProfile_forRecharge(CacheManager.getInstance().getMyProfile());
            String language = SharePreferenceUtils.getInstance(context).getLocalLanguage();
            String phone = CacheManager.getInstance().getMyProfile().phone;
            String password = "";
            AfLoginInfo[] myAccounts = mAfCorePalmchat.AfDbLoginGetAccount();
            if (myAccounts != null && myAccounts.length > 0) {
                AfLoginInfo afLoginInfo = myAccounts[0];
                if (afLoginInfo != null) {
                    password = afLoginInfo.password;
                    /** DESC + Base64 */
                    password = PalmchatApp.getApplication().mAfCorePalmchat.AfLoginEncode(password, sessionid);
                }
            }
            String imei = PalmchatApp.getOsInfo().getImei();
            String afid = CacheManager.getInstance().getMyProfile().afId;

            if (sessionid != null)
                sessionid = URLEncoder.encode(sessionid, "UTF-8");
            if (countryCode != null)
                countryCode = URLEncoder.encode(countryCode, "UTF-8");
            if (language != null)
                language = URLEncoder.encode(language, "UTF-8");
            if (phone != null)
                phone = URLEncoder.encode(phone, "UTF-8");
            else
                phone = "";
            if (password != null)
                password = URLEncoder.encode(password, "UTF-8");
            if (imei != null)
                imei = URLEncoder.encode(imei, "UTF-8");
            if (afid != null)
                afid = URLEncoder.encode(afid, "UTF-8");

            String mUrl = rechargeUrl;
            if (mUrl.contains("?")) {
                mUrl = mUrl + "&sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&phone=" + phone + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
            } else {
                mUrl = mUrl + "?sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&phone=" + phone + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
            }

            Intent it = new Intent(getActivity(), MyWapActivity.class);
            it.putExtra(IntentConstant.WAP_URL, mUrl);
            startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
