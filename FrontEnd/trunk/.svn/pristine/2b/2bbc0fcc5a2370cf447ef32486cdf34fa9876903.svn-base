package com.afmobi.palmchat.ui.activity.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.db.DBState;
import com.afmobi.palmchat.eventbusmodel.ChangeCountryEvent;
import com.afmobi.palmchat.eventbusmodel.ChangeRegionEvent;
import com.afmobi.palmchat.eventbusmodel.EventLoginBackground;
import com.afmobi.palmchat.eventbusmodel.ExitMobileTopUpEvent;
import com.afmobi.palmchat.eventbusmodel.PalmGuessNotificationEvent;
import com.afmobi.palmchat.eventbusmodel.UpdateCoinsEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.chattingroom.ChattingRoomListActivity;
import com.afmobi.palmchat.ui.activity.invitefriends.PublicAccountsFragmentActivity;
import com.afmobi.palmchat.ui.activity.main.UpdateStateActivity;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.payment.CoinRechargeDialogActivity;
import com.afmobi.palmchat.ui.activity.payment.MobileTopUpDialogActivity;
import com.afmobi.palmchat.ui.activity.payment.MyWapActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.InnerNoAbBrowserActivity;
import com.afmobi.palmchat.ui.activity.social.FunnyClubActivity;
import com.afmobi.palmchat.ui.activity.social.ShakeFragmentActivity;
import com.afmobi.palmchat.ui.activity.store.AccountCenterFragmentAcitivity;
import com.afmobi.palmchat.ui.activity.store.StoreFragmentActivity;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfLoginInfo;
import com.core.AfProfileInfo;
import com.core.AfProfileInfo.PalmCoinMenuItemInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.greenrobot.event.EventBus;


public class ExploreFragment extends BaseFragment implements OnClickListener, AfHttpResultListener {

    RelativeLayout r_predictWin, r_star, r_shake, // r_search,
            r_store, r_game, r_palmplay, r_public_account, r_account_center, r_mobile_tou_up, r_funclub, r_groupchat, r_chatroom, r_betway;
    private TextView unReadText_HasNewStore, unread_msg_predict, unread_msg_chattingroom, tv_coin_change;
    boolean isFirst;
    String myAfid;
    // 30 minutes
    private static final long mStayTime = 1000 * 60 * 30;
    private static final int ENTER_CHATTINGROMM_LIST = 1;
    /*zhh 用于保存当前发送的充值统计文件*/
    HashMap<String, String> fileMap;

    /**
     * 服务器返回的充值列表项数据
     */
    private ArrayList<PalmCoinMenuItemInfo> palmcoin_menu_list;

    public ExploreFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_explore);
        initViews();
        return mMainView;
    }

    private void initViews() {
        r_predictWin = (RelativeLayout) findViewById(R.id.r_predict_win);
        myAfid = CacheManager.getInstance().getMyProfile().afId;
        r_star = (RelativeLayout) findViewById(R.id.r_star);
        r_shake = (RelativeLayout) findViewById(R.id.r_shake);
        // r_search = (RelativeLayout) findViewById(R.id.r_search);
        r_store = (RelativeLayout) findViewById(R.id.r_store);
        r_game = (RelativeLayout) findViewById(R.id.r_game);
        r_palmplay = (RelativeLayout) findViewById(R.id.r_palmplay);
        r_groupchat = (RelativeLayout) findViewById(R.id.r_groupchat);
        r_groupchat.setVisibility(View.GONE);
        r_public_account = (RelativeLayout) findViewById(R.id.r_public_account);
        r_account_center = (RelativeLayout) findViewById(R.id.r_account_center);
        r_mobile_tou_up = (RelativeLayout) findViewById(R.id.r_mobile_tou_up);
        r_funclub = (RelativeLayout) findViewById(R.id.r_funclub);
        r_chatroom = (RelativeLayout) findViewById(R.id.r_chatroom);
        r_betway = (RelativeLayout) findViewById(R.id.r_betway);

        r_predictWin.setOnClickListener(this);
        r_star.setOnClickListener(this);
        r_shake.setOnClickListener(this);
        // r_search.setOnClickListener(this);
        r_store.setOnClickListener(this);
        // r_palmplay.setOnClickListener(this);
        r_palmplay.setVisibility(View.GONE);
        r_game.setOnClickListener(this);
        r_public_account.setOnClickListener(this);
        r_account_center.setOnClickListener(this);
        r_mobile_tou_up.setOnClickListener(this);
        r_betway.setOnClickListener(this);
        r_funclub.setOnClickListener(this);
        r_groupchat.setOnClickListener(this);
        r_chatroom.setOnClickListener(this);
        r_chatroom.setVisibility(View.VISIBLE);
        unReadText_HasNewStore = (TextView) findViewById(R.id.unread_msg_explore);
        tv_coin_change = (TextView) findViewById(R.id.tv_coin_change);
        unread_msg_predict = (TextView) findViewById(R.id.unread_msg_predict);
        unread_msg_chattingroom = (TextView) findViewById(R.id.unread_msg_chattingroom);
        findViewById(R.id.r_far_far_away).setOnClickListener(this);

        r_funclub.setVisibility(View.GONE);
        closeOrOnRechargeUI();

        isFirst = SharePreferenceUtils.getInstance(context).getIsFirstInstall();
        if (isFirst) {
            mHandler.obtainMessage(Constants.PREDICT__NOTIFICATION).sendToTarget();
        }
        EventBus.getDefault().register(this);
    }

    /**
     * 根据国家开放充值入口
     */
    private void closeOrOnRechargeUI() {
        if (countryIsNigeria()) {
            r_mobile_tou_up.setVisibility(View.VISIBLE);
        } else {
            r_mobile_tou_up.setVisibility(View.GONE);
        }
        if (CacheManager.getInstance().getMyProfile().betway == 1) {
            r_betway.setVisibility(View.VISIBLE);
        } else {
            r_betway.setVisibility(View.GONE);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.PREDICT__NOTIFICATION: // 处理PREDICT广播消息
                    if (PalmchatApp.getApplication().isOpenPalmGuess) // 如果开放PalmGuess，一定要把此变量设置为true
                    {
                        if (CacheManager.getInstance().isPalmGuessShow()) { // 判断PalmGuess是否显示
                            if (SharePreferenceUtils.getInstance(context).getUnReadPredictRecord() || SharePreferenceUtils.getInstance(context).getUnReadExchange() || SharePreferenceUtils.getInstance(context).getUnReadPredictPrize() || SharePreferenceUtils.getInstance(context).getUnReadNewPalmGuess() || isFirst) {
                                unread_msg_predict.setVisibility(View.VISIBLE);
                                if (context != null) {
                                    ((MainTab) context).showNew();
                                }
                            } else {
                                unread_msg_predict.setVisibility(View.GONE);
                                ((MainTab) context).hideNew(true, false);
                            }
                        } else {
                            if (isFirst) {
                                isFirst = false;
                                SharePreferenceUtils.getInstance(context).setIsFirstInstall(false);
                            }
                            ((MainTab) context).hideNew(false, false);
                        }
                        break;

                    }
                    break;


            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENTER_CHATTINGROMM_LIST) {
            scheduleStayTime();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * init PalmGuess
     */
    private void lotteryInit() {
        /*if (PalmchatApp.getApplication().mAfCorePalmchat != null) {

			if (!isLotteryIniting) {
				if (System.currentTimeMillis() - timeLotteryInit > 10 * 60000) {// 10分钟内不用重新init
					isLotteryIniting = true;
					showProgressDialog();
					PalmchatApp.getApplication().mAfCorePalmchat.AfHttpLotteryInit(this);
				} else {
					startActivity(new Intent(getActivity(), PalmGuessMainTab.class));
				}
			} else {
				showProgressDialog();
			}
		}*/
    }

    private boolean isLotteryIniting;// 用来防止在一个请求没成功之前 取消进度条 重新请求的情况
    private long timeLotteryInit;// 上次init成功的时间

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.r_predict_win:
                if (isFirst) {
                    isFirst = false;
                    SharePreferenceUtils.getInstance(context).setIsFirstInstall(false);
                }
                SharePreferenceUtils.getInstance(context).setUnReadNewPalmGuess(false);

                lotteryInit();

                break;
        /*
         * case R.id.r_miss_nigeria: startActivity(new Intent(getActivity(),
		 * MissNigeriaFragmentActivity.class)); break;
		 */

            case R.id.r_star:
                PalmchatLogUtils.println("r_star");
                // gtf 2014-12-15
                // new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_NBS);
                // MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_NBS);
                //// startActivity(new Intent(getActivity(),
                // WeeklyStarActivity.class));

                break;
            case R.id.r_shake:
                PalmchatLogUtils.println("r_shake");

                // gtf 2014-12-15
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LEFT_T_SHAKE);
                // MobclickAgent.onEvent(context, ReadyConfigXML.LEFT_T_SHAKE);
                startActivity(new Intent(getActivity(), ShakeFragmentActivity.class));
                break;
        /*
         * case R.id.r_search: PalmchatLogUtils.println("r_search");
		 * startActivity(new Intent(getActivity(),
		 * SearchByInfoFragmentActivity.class));
		 * 
		 * break;
		 */

            case R.id.r_far_far_away:

                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_FAR);
                // MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_FAR);

                // if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context)
                // != ConnectionResult.SUCCESS) {
                //
                // AppDialog appDialog = new AppDialog(context);
                // appDialog.createOKDialog(context,
                // getFragString(R.string.google_play_service_not_installed), null);
                // appDialog.show();
                //
                // return;
                //
                // }

                // startActivity(new Intent(getActivity(),
                // FarFarAwayFragmentActivity.class));
                break;

            case R.id.r_store:
                // gtf 2014-11-16
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_STO);
                // MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_STO);

                PalmchatLogUtils.println("r_store");
                startActivity(new Intent(getActivity(), StoreFragmentActivity.class));

                CacheManager.getInstance().setHasNewStoreProInfo(false);
                hideNew();
                break;
            case R.id.r_palmplay:

                PackageManager pm = context.getPackageManager();
                Intent intent = pm.getLaunchIntentForPackage("com.hzay.market");
                if (intent != null) {
                    startActivity(intent);
                } else {
                    WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    Uri uri = null;
                    if (wifiInfo.getSSID().contains("almplay")) {
                        // uri =
                        // Uri.parse("http://app.palmplay.com/static/Palmplay.apk");//
                        uri = Uri.parse("http://192.168.10.1/static/Palmplay.apk"); // liuzhimodify20160302offlinehostnamechangedtoIP
                    } else {
                        uri = Uri.parse("http://static.palmplaystore.com/static/palmplay.apk");//
                    }
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                }
                break;
            case R.id.r_game:
                PalmchatLogUtils.println("r_game");

                break;
            case R.id.r_public_account:
                // gtf 2014-11-16
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_PBL);
                // MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_PBL);
                PalmchatLogUtils.println("r_public_account");
                startActivity(new Intent(getActivity(), PublicAccountsFragmentActivity.class));

                break;
            case R.id.r_account_center: //palmcoin wallet
                // gtf 2014-11-16
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_ACC);
                // MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_ACC);
                PalmchatLogUtils.println("r_account_center");
                /** 点击时隐藏掉红点显示 */
                ((MainTab) getActivity()).hideNew(true, false);
                startActivity(new Intent(getActivity(), AccountCenterFragmentAcitivity.class));

                break;
            case R.id.r_mobile_tou_up: //mobile top up
                toMobileTopUp();
                break;
            case R.id.r_betway:
                Intent it = new Intent(getActivity(), InnerNoAbBrowserActivity.class);
                it.putExtra(IntentConstant.RESOURCEURL, Constants.KENYA_BETWAY_URL);
                it.putExtra(IntentConstant.FILTER_URL_TYPE, DefaultValueConstant.FILTER_RUL_BETWAY);
                startActivity(it);
                break;
            case R.id.r_funclub:
                // PalmchatLogUtils.println("r_more");
                startActivity(new Intent(getActivity(), FunnyClubActivity.class));
                break;
            case R.id.r_groupchat:
                // CommonUtils.to(context, PublicGroupChatMain.class);

                break;
            case R.id.r_chatroom:
                SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).setIsAtme(myAfid, false);
                hideNewAtMe();
                startActivityForResult(new Intent(getActivity(), ChattingRoomListActivity.class), ENTER_CHATTINGROMM_LIST);
                break;
            default:
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {

            if (r_predictWin != null) {
                if (CacheManager.getInstance().isPalmGuessShow() && PalmchatApp.getApplication().isOpenPalmGuess) {// 如果登录的时候返回的是1才算支持PalmGuess
                    r_predictWin.setVisibility(View.VISIBLE);
                } else {
                    r_predictWin.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 地区改变的通知
     *
     * @param event
     */
    public void onEventMainThread(ChangeRegionEvent event) {
        if (event.isChangePalmguessFlag()) {
            CacheManager.getInstance().setPalmGuessShow(event.getPalmguessFlag() == 1);
            if (CacheManager.getInstance().isPalmGuessShow() && PalmchatApp.getApplication().isOpenPalmGuess) {
                r_predictWin.setVisibility(View.VISIBLE);// 屏蔽入口
            } else {
                r_predictWin.setVisibility(View.GONE);
            }
        }
    }

    public void onEventMainThread(PalmGuessNotificationEvent event) {
        mHandler.sendEmptyMessage(Constants.PREDICT__NOTIFICATION);

    }

    /**
     * 国家改变的通知
     *
     * @param event
     */
    public void onEventMainThread(ChangeCountryEvent event) {
        closeOrOnRechargeUI();
    }


    /**
     * b31消息 红点显示
     *
     * @param event
     */
    public void onEventMainThread(UpdateCoinsEvent event) {
        showCoinRedDot();
    }

    /**
     * 后台登陆完成的话 再初始化充值列表一次
     *
     * @param event
     */
    public void onEventMainThread(EventLoginBackground event) {
        if (event.getIsLoginSuccess()) {
            closeOrOnRechargeUI();
        }
    }


    /**
     * 退出mobile top up wap页面通知
     *
     * @param event
     */
    public void onEventMainThread(ExitMobileTopUpEvent event) {

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                try {
                    JSONArray jsonArr = new JSONArray();
                    fileMap = FileUtils.getFileList(RequestConstant.MOBILE_TOP_UP_CACHE);
                    Iterator iter = fileMap.entrySet().iterator();
                    int i = 0;
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String data = (String) entry.getValue();
                        jsonArr.put(i, data.toString());
                        i++;
                    }
                    mAfCorePalmchat.AfHttpPaymentAnalysis(jsonArr.toString(), null, ExploreFragment.this);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }, 30000);
    }


    @Override
    public void onResume() {
        super.onResume();
        showCoinRedDot();
        showNew();
        showNewAtMe();
        hideNewAtMe();
        mHandler.sendEmptyMessage(Constants.PREDICT__NOTIFICATION);

    }

    public void showNew() {
        if (CacheManager.getInstance().hasNewStoreProInfo()) {
            if (unReadText_HasNewStore != null) {
                unReadText_HasNewStore.setVisibility(View.VISIBLE);
            }
        }
    }

    public void hideNew() {
        if (!CacheManager.getInstance().hasNewStoreProInfo()) {
            unReadText_HasNewStore.setVisibility(View.GONE);
        }

        if (getActivity() != null && getActivity() instanceof MainTab) {
            ((MainTab) getActivity()).hideNew(true, false);
        }
    }

    public void showNewAtMe() {
        if (SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getIsAtme(myAfid)) {
            if (unread_msg_chattingroom != null) {
                unread_msg_chattingroom.setVisibility(View.VISIBLE);
            }
        }
    }

    public void hideNewAtMe() {
        if (!SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getIsAtme(myAfid)) {
            unread_msg_chattingroom.setVisibility(View.GONE);
        }

        if (getActivity() != null && getActivity() instanceof MainTab) {
            ((MainTab) getActivity()).hideNew(true, false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        // unRegisterBroadcast();
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        dismissProgressDialog();
        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                case Consts.REQ_PREDICT_INIT: // 初始化PalmGuess
            /*	isLotteryIniting = false;
                if (result != null) {
					AfLottery mAflottery = (AfLottery) result;
					LotteryInit mLotteryInit = mAflottery.lotteryInit;
					if (mLotteryInit != null) {
						PalmchatApp.getApplication().setMyPoints(mLotteryInit.points);
						timeLotteryInit = System.currentTimeMillis();

						CacheManager.getInstance().setLotteryInit(mAflottery.lotteryInit);
						*//*if (getUserVisibleHint()) {// 如果该界面是显示状态
                            startActivity(new Intent(getActivity(), PalmGuessMainTab.class));
						}*//*
                    }
				}*/
                    break;
                case Consts.REQ_FLAG_PAYMENT_ANALYSIS:  //支付统计上报成功后，删除已上报过的文件
                    FileUtils.deleteFilesBydir(RequestConstant.MOBILE_TOP_UP_CACHE, fileMap);
                    break;
            }
        } else {
            switch (flag) {
                case Consts.REQ_PREDICT_INIT: // 初始化获取转盘抽奖信息
                    isLotteryIniting = false;
                    if (code == Consts.PHONE_UNBIND) { // 手机号未绑定
                        ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.bind_phone_messages);
                    } else if (code == Consts.COUNTRY_NOT_NIGERIA) { // 国家非尼日利亚
                        ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.palmGuess_not_supported);
                    } else if (code == Consts.SERVER_IS_MAINTENANCE) { // 服务器维护
                        ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.palmGuess_sys_offline);
                    } else if (code == Consts.REQ_CODE_UNKNOWN) {
                        ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.palmGuess_is_not_available);
                    } else {
                        Consts.getInstance().showToast(PalmchatApp.getApplication(), code, flag, http_code);
                    }

                    break;

            }
        }

    }

    /**
     * add by zhh 判断当前是否是尼日利亚国家
     *
     * @return
     */
    private boolean countryIsNigeria() {
        AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
        String country = myProfile.country;
        if (country != null && country.equals(DefaultValueConstant.COUNTRY_NIGERIA))
            return true;
        else
            return false;
    }

    /**
     * 从chattingroom列表退出后，中间件与服务器就断开，无法知道是否退出聊天室 所以在此设置30分钟后，把所有的进入聊天室的标识设置退出
     */
    private void scheduleStayTime() {

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                System.out.println("---eee: schedule");
                MessagesUtils.setChatroomExit();
            }
        }, mStayTime);

    }

    /**
     * 当手机号绑定国家和profile国家不一致时，弹出修改国家对话框
     */
    private boolean isCountryNotSame() {
        boolean bool = false;
        String phone_cc = CacheManager.getInstance().getMyProfile().phone_cc;
        String country = CacheManager.getInstance().getMyProfile().country;
        if (phone_cc != null && country != null) {
            String bindCountry = DBState.getInstance(context).getCountryFromCode(phone_cc);
            if (bindCountry != null && !bindCountry.equalsIgnoreCase(country)) // 当profile中国家和手机绑定国家不一�?
                bool = true;

        }
        return bool;

    }

    /**
     * 跳转到修改地区页面
     */
    private void toUpdateStateAcitivty() {
        Intent mIntent = new Intent(context, UpdateStateActivity.class);
        mIntent.putExtra("action", 1); // 1代表profile中国家和手机绑定国家不一�?
        startActivity(mIntent);
    }

    /**
     * 当coin变化时显示红点 B31消息处理
     */
    private void showCoinRedDot() {
        /** 当coin变化时显示红点 B31消息处理 */
        if (SharePreferenceUtils.getInstance(context).getBalancePalmcoinRedDot())
            tv_coin_change.setVisibility(View.VISIBLE);
        else
            tv_coin_change.setVisibility(View.GONE);
    }

    /**
     * 跳转到手机充值页面
     */
    private void toMobileTopUp() {

        try {

            String sessionid = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession();
            String countryCode = AppUtils.getCCFromProfile_forRecharge(CacheManager.getInstance().getMyProfile());
            String language = SharePreferenceUtils.getInstance(context).getLocalLanguage();
            String phone = CacheManager.getInstance().getMyProfile().phone;
            String imei = PalmchatApp.getOsInfo().getImei();
            String afid = CacheManager.getInstance().getMyProfile().afId;
            String password = "";
            AfLoginInfo[] myAccounts = mAfCorePalmchat.AfDbLoginGetAccount();
            if (myAccounts != null && myAccounts.length > 0) {
                AfLoginInfo afLoginInfo = myAccounts[0];
                if (afLoginInfo != null) {
                    password = afLoginInfo.password;
                    password = PalmchatApp.getApplication().mAfCorePalmchat.AfLoginEncode(password, sessionid);
                }
            }

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
            if (imei != null)
                imei = URLEncoder.encode(imei, "UTF-8");
            if (afid != null)
                afid = URLEncoder.encode(afid, "UTF-8");
            if (password != null)
                password = URLEncoder.encode(password, "UTF-8");

            if (PalmchatApp.getApplication().isFormalServer()) { //正式环境
                String mUrl = Constants.mobile_top_up_url;
                if (mUrl.contains("?")) {
                    mUrl = mUrl + "&sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&phone=" + phone + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
                } else {
                    mUrl = mUrl + "?sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&phone=" + phone + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
                }

                if (ReadyConfigXML.R_DSRC == ReadyConfigXML.ANDROID_GP) { //谷歌渠道 每次都需要弹免责对话框
                    Intent mIntent = new Intent(context, MobileTopUpDialogActivity.class);
                    mIntent.putExtra(IntentConstant.URL, mUrl);
                    startActivity(mIntent);
                } else { //非谷歌渠道 第一次点击弹免责对话框
                    if (!SharePreferenceUtils.getInstance(context).getMobileTopUpClicked()) {
                        Intent mIntent = new Intent(context, MobileTopUpDialogActivity.class);
                        mIntent.putExtra(IntentConstant.URL, mUrl);
                        startActivity(mIntent);
                    } else {
                        Intent it = new Intent(getActivity(), MyWapActivity.class);
                        it.putExtra(IntentConstant.WAP_URL, mUrl);
                        it.putExtra(MyWapActivity.FORM, MyWapActivity.MOBILE_TOP_UP);
                        startActivity(it);
                    }
                }
            } else { //测试环境
                String mUrl = Constants.mobile_top_up_test_url;
                if (mUrl.contains("?")) {
                    mUrl = mUrl + "&sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&phone=" + phone + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
                } else {
                    mUrl = mUrl + "?sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&phone=" + phone + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
                }

                if (ReadyConfigXML.R_DSRC == ReadyConfigXML.ANDROID_GP) {
                    Intent mIntent = new Intent(context, MobileTopUpDialogActivity.class);
                    mIntent.putExtra(IntentConstant.URL, mUrl);
                    startActivity(mIntent);
                } else {
                    if (!SharePreferenceUtils.getInstance(context).getMobileTopUpClicked()) {
                        Intent mIntent = new Intent(context, MobileTopUpDialogActivity.class);
                        mIntent.putExtra(IntentConstant.URL, mUrl);
                        startActivity(mIntent);
                    } else {
                        Intent it = new Intent(getActivity(), MyWapActivity.class);
                        it.putExtra(IntentConstant.WAP_URL, mUrl);
                        it.putExtra(MyWapActivity.FORM, MyWapActivity.MOBILE_TOP_UP);
                        startActivity(it);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
