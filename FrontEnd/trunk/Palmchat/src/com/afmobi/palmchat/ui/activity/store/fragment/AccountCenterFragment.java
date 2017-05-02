package com.afmobi.palmchat.ui.activity.store.fragment;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.eventbusmodel.ChangeCountryEvent;
import com.afmobi.palmchat.eventbusmodel.EventLoginBackground;
import com.afmobi.palmchat.eventbusmodel.UpdateCoinsEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.payment.CoinRechargeDialogActivity;
import com.afmobi.palmchat.ui.activity.payment.MyWapActivity;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfLoginInfo;
import com.core.AfLottery;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.net.URLEncoder;

import de.greenrobot.event.EventBus;

/**
 * 账户中心的Fragment
 */
public class AccountCenterFragment extends BaseFragment implements AfHttpResultListener {

    private static final String TAG = AccountCenterFragment.class.getSimpleName();
    private long mClickRechargeTime;
    private Dialog dialog;
    /**
     * 余额value控件
     */
    private TextView mTvBalance;
    /**
     * 头像控件
     */
    private ImageView mMyPhoto;
    /**
     * 名字
     */
    private TextView mMyName;
    /**
     * plamId控件
     */
    private TextView mMyId;
    /* 货币币种 */
    private TextView tv_currency;
    /**
     * 充值按钮
     */
    private Button rc_btn;
    /**
     * coin改变时红点
     */
    private ImageView img_coin_change;
    /**
     * 运营活动总布局
     */
    private View ll_action;
    /**
     * 活动标题
     */
    private TextView tv_action_title;
    /**
     * 活动url
     */
    private String action_url;
    /**
     * feedback
     */
    private TextView tv_feedback;
    /**
     * 标题栏右上角更多按钮
     */
    private ImageView img_op1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.activity_account_center);
        findViews();
        init();
        return mMainView;
    }

    private void findViews() {
        ((TextView) findViewById(R.id.title_text)).setText(R.string.palmcoin_wallet);
        View backView = findViewById(R.id.back_button);
        backView.setOnClickListener(this);

        mTvBalance = (TextView) findViewById(R.id.profile_relationship_value);
        mMyPhoto = (ImageView) findViewById(R.id.img_photo);
        mMyName = (TextView) findViewById(R.id.text_name);
        mMyId = (TextView) findViewById(R.id.text_sign);
        tv_currency = (TextView) findViewById(R.id.tv_currency);
        img_coin_change = (ImageView) findViewById(R.id.img_coin_change);
        rc_btn = (Button) findViewById(R.id.rc_btn);
        ll_action = findViewById(R.id.ll_action);
        tv_action_title = (TextView) findViewById(R.id.tv_action_title);
        tv_feedback = (TextView) findViewById(R.id.tv_feedback);
        img_op1 = (ImageView) findViewById(R.id.op1);
        img_op1.setBackgroundResource(R.drawable.navigation);


        img_coin_change.setVisibility(View.GONE);

        rc_btn.setOnClickListener(this);
        ll_action.setOnClickListener(this);
        EventBus.getDefault().register(this);
        tv_feedback.setOnClickListener(this);
        img_op1.setOnClickListener(this);

        closeOrOnRechargeUI();

    }

    @Override
    public void onResume() {
        super.onResume();
        showCoinRedDot();
    }

    private void init() {
        AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
        // WXL 20151015 调UIL的显示头像方法,统一图片管理
        ImageManager.getInstance().DisplayAvatarImage(mMyPhoto, profileInfo.getServerUrl(), profileInfo.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, profileInfo.sex, profileInfo.getSerialFromHead(), null);
        if (null != profileInfo.afId)
            mMyId.setText("Palm ID:" + profileInfo.afId.replace("a", ""));
        if (null != profileInfo.name)
            mMyName.setText(profileInfo.name);
    }


    /**
     * 根据国家开放充值入口
     */
    private void closeOrOnRechargeUI() {
        if (countryIsNigeria()) {
            rc_btn.setVisibility(View.VISIBLE);
//            tv_currency.setVisibility(View.VISIBLE);
            tv_feedback.setVisibility(View.VISIBLE);
            img_op1.setVisibility(View.VISIBLE);
        } else {
            rc_btn.setVisibility(View.GONE);
//            tv_currency.setVisibility(View.GONE);
            tv_feedback.setVisibility(View.GONE);
            img_op1.setVisibility(View.GONE);
        }
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
        }
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button: // 返回按钮
                getActivity().finish();
                break;
            case R.id.rc_btn: // recharging btn
                toCoinRecharge();
                break;
            case R.id.ll_action: // 运营活动
                if (action_url != null && !action_url.isEmpty()) {
                    Intent it = new Intent(context, MyWapActivity.class);
                    it.putExtra(IntentConstant.WAP_URL, action_url);
                    startActivity(it);
                }

                break;
            case R.id.tv_feedback:
                toFeedBack();
                break;
            case R.id.op1: // 更多
                MyPopupWindow addPopWindow = new MyPopupWindow(context);
                addPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
                addPopWindow.showPopupWindow(img_op1);

                backgroundAlpha(0.4f);
                break;

        }
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        PalmchatLogUtils.println("AccountCenterFragment: flag = " + flag + " code=" + code + " result= " + result);

        if (code == Consts.REQ_CODE_SUCCESS) {
            if (isAdded()) { // 该Fragment对象被添加到了它的Activity中
                if (null != context && !context.isFinishing()) {
                    switch (flag) {
                        case Consts.REQ_PREDICT_BUY_POINTS_GETCOINS: // Consts.REQ_PAYMENT_VCOIN_GET:
                            if (null != result) {
                                AfLottery resp = (AfLottery) result;
                                mTvBalance.setText(resp.getconiresp.balance + "");
                                tv_currency.setText("(" + resp.getconiresp.code + ")");
                                String action_title = resp.getconiresp.title;
                                String url = resp.getconiresp.url;
                                if (action_title == null || url == null) {
                                    ll_action.setVisibility(View.GONE);
                                } else {
                                    ll_action.setVisibility(View.VISIBLE);
                                    tv_action_title.setText(action_title);
                                    action_url = url;
                                }
                                // String text =
                                // context.getString(R.string.Balnce_Palmcoin);
                                // text = text.replace("$xxx",
                                // resp.getconiresp.code);

                            }

                            break;
                        default:
                            break;
                    }
                }
            }

        } else {
            PalmchatLogUtils.println("AccountCenterFragment error: flag = " + flag + " code=" + (code - Consts.REQ_CODE_PAYSTORE_BASE));
            if (isAdded()) { // 该Fragment对象被添加到了它的Activity中
                if (null != context && !context.isFinishing()) {
                    switch (flag) {
                        case Consts.REQ_PREDICT_BUY_POINTS_GETCOINS: // Consts.REQ_PAYMENT_VCOIN_GET:获取availablebalance失败
                            Consts.getInstance().showToast(context, code, flag, http_code);
                            break;

                        default:
                            break;
                    }
                }
            }
        }
    }

    /**
     * 弹出的PopupWindow视图
     */
    private class MyPopupWindow extends PopupWindow {
        private View conentView;

        public MyPopupWindow(final Activity context) {
            super(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            conentView = inflater.inflate(R.layout.activity_qrcode_popupwindow, null);
            // 设置SelectPicPopupWindow的View
            this.setContentView(conentView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体可点�?
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            // 刷新状�?
            this.update();
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0000000000);
            // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
            this.setBackgroundDrawable(dw);
            // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            // 设置SelectPicPopupWindow弹出窗体动画效果
            // this.setAnimationStyle(R.style.AnimationPreview);

            LinearLayout add_contacts_text = (LinearLayout) conentView.findViewById(R.id.add_contacts_text);
            LinearLayout group_chat_text = (LinearLayout) conentView.findViewById(R.id.group_chat_text);
            LinearLayout shakeshake_text = (LinearLayout) conentView.findViewById(R.id.shakeshake_text);
            LinearLayout qr_code_linear = (LinearLayout) conentView.findViewById(R.id.qr_code_linear);
            shakeshake_text.setVisibility(View.GONE);
            qr_code_linear.setVisibility(View.GONE);
            // View line_3 = conentView.findViewById(R.id.line_3);
            // line_3.setVisibility(View.GONE);
            ImageView img_1 = (ImageView) conentView.findViewById(R.id.img_1);
            img_1.setBackgroundResource(R.drawable.ic_menu_transaction);
            ImageView img_2 = (ImageView) conentView.findViewById(R.id.img_2);
            img_2.setBackgroundResource(R.drawable.ic_menu_hepl);

            TextView tv_1 = (TextView) conentView.findViewById(R.id.tv_1);
            tv_1.setText(R.string.transaction);
            TextView tv_2 = (TextView) conentView.findViewById(R.id.tv_2);
            tv_2.setText(R.string.help);
            View pop_view_2 = (View) conentView.findViewById(R.id.pop_view_2);
            View pop_view_3 = (View) conentView.findViewById(R.id.pop_view_3);
            pop_view_2.setVisibility(View.GONE);//隐藏textview与textview分割线
            pop_view_3.setVisibility(View.GONE);
            final AfProfileInfo afProfileInfo = CacheManager.getInstance().getMyProfile();

            /**
             * 交易记录
             */
            add_contacts_text.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    toTransaction();
                    MyPopupWindow.this.dismiss();
                }
            });

            /**
             * 帮助
             */
            group_chat_text.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    toFeedBack();
                    MyPopupWindow.this.dismiss();
                }
            });


        }

        /**
         * 显示popupWindow
         *
         * @param parent
         */
        public void showPopupWindow(View parent) {
            if (!this.isShowing()) {
                // 以下拉方式显示popupwindow
                this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 5);// 显示popupwindow的位�?
            } else {
                this.dismiss();
            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 收到B31消息，重新获取余额，并显示红点
     */
    private void showCoinRedDot() {
        /** 当coin变化时显示红点 B31消息处理 */
        if (SharePreferenceUtils.getInstance(context).getBalancePalmcoinRedDot()) {
            img_coin_change.setVisibility(View.VISIBLE);
            SharePreferenceUtils.getInstance(context).setBalancePalmcoinRedDot(false);
        } else
            img_coin_change.setVisibility(View.GONE);

		/* get my balance */
        // zhh
        if (null != CacheManager.getInstance().getMyProfile().afId)
            mAfCorePalmchat.AfHttpPredictGetCoins(CacheManager.getInstance().getMyProfile().afId, this);
    }

    /**
     * coin充值
     */
    private void toCoinRecharge() {
        try {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mClickRechargeTime < 1000) { // 两次点击充值按钮时间小于1秒时不做处理
                mClickRechargeTime = nowTime;
                PalmchatLogUtils.println("rc_btn");
                return;
            }

            mClickRechargeTime = nowTime;
            boolean isFormalServer = PalmchatApp.getApplication().isFormalServer(); //是否正式环境
            String mUrl = isFormalServer ? Constants.coin_recharge_url : Constants.coin_recharge_test_url;

            String sessionid = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession();
            String countryCode = AppUtils.getCCFromProfile_forRecharge(CacheManager.getInstance().getMyProfile());
            String language = SharePreferenceUtils.getInstance(context).getLocalLanguage();
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

            if (imei != null)
                imei = URLEncoder.encode(imei, "UTF-8");
            if (afid != null)
                afid = URLEncoder.encode(afid, "UTF-8");

            if (password != null)
                password = URLEncoder.encode(password, "UTF-8");

            if (mUrl.contains("?")) {
                mUrl = mUrl + "&sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
            } else {
                mUrl = mUrl + "?sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
            }


            if (ReadyConfigXML.R_DSRC == ReadyConfigXML.ANDROID_GP) {
                Intent mIntent = new Intent(context, CoinRechargeDialogActivity.class);
                mIntent.putExtra(IntentConstant.URL, mUrl);
                startActivity(mIntent);
            } else {
                if (!SharePreferenceUtils.getInstance(context).getRechargeCoinClicked()) {
                    Intent mIntent = new Intent(context, CoinRechargeDialogActivity.class);
                    mIntent.putExtra(IntentConstant.URL, mUrl);
                    startActivity(mIntent);
                } else {
                    Intent it = new Intent(context, MyWapActivity.class);
                    it.putExtra(IntentConstant.WAP_URL, mUrl);
                    startActivity(it);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 反馈
     */
    private void toFeedBack() {
        try {

            String mUrl = AppUtils.getWapUrlByModule(MyWapActivity.MODULE_FEEDBACK);

            String palmid = CacheManager.getInstance().getMyProfile().afId;
            String country = CacheManager.getInstance().getMyProfile().country;
            String countryCode = AppUtils.getCCFromProfile_forRecharge(CacheManager.getInstance().getMyProfile());
            String phone = CacheManager.getInstance().getMyProfile().phone;
            String type = "Payment";

            if (palmid != null)
                palmid = URLEncoder.encode(palmid, "UTF-8");
            if (country != null)
                country = URLEncoder.encode(country, "UTF-8");
            if (countryCode != null)
                countryCode = URLEncoder.encode(countryCode, "UTF-8");
            if (phone != null)
                phone = URLEncoder.encode(phone, "UTF-8");
            else
                phone = "";
            type = URLEncoder.encode(type, "UTF-8");

            if (mUrl.contains("?")) {
                mUrl = mUrl + "&palmid=" + palmid + "&country=" + country + "&countryCode=" + countryCode + "&phone=" + phone + "&type=" + type;
            } else {
                mUrl = mUrl + "?palmid=" + palmid + "&country=" + country + "&countryCode=" + countryCode + "&phone=" + phone + "&type=" + type;
            }
            Intent it = new Intent(context, MyWapActivity.class);
            it.putExtra(IntentConstant.WAP_URL, mUrl);
            startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 交易记录
     */
    private void toTransaction() {
        try {
            String sessionid = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession();
            String countryCode = AppUtils.getCCFromProfile_forRecharge(CacheManager.getInstance().getMyProfile());
            String language = SharePreferenceUtils.getInstance(context).getLocalLanguage();
            String password = "";
            AfLoginInfo[] myAccounts = mAfCorePalmchat.AfDbLoginGetAccount();
            if (myAccounts != null && myAccounts.length > 0) {
                AfLoginInfo afLoginInfo = myAccounts[0];
                if (afLoginInfo != null) {
                    password = afLoginInfo.password;
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
            if (password != null)
                password = URLEncoder.encode(password, "UTF-8");
            if (imei != null)
                imei = URLEncoder.encode(imei, "UTF-8");
            if (afid != null)
                afid = URLEncoder.encode(afid, "UTF-8");

            if (PalmchatApp.getApplication().isFormalServer()) { //正式环境
                String mUrl = Constants.transaction_url;
                if (mUrl.contains("?")) {
                    mUrl = mUrl + "&sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
                } else {
                    mUrl = mUrl + "?sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
                }
                Intent it = new Intent(context, MyWapActivity.class);

                it.putExtra(IntentConstant.WAP_URL, mUrl);
                startActivity(it);
            } else { //测试环境
                String mUrl = Constants.transaction_test_url;
                if (mUrl.contains("?")) {
                    mUrl = mUrl + "&sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
                } else {
                    mUrl = mUrl + "?sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
                }

                Intent it = new Intent(getActivity(), MyWapActivity.class);
                it.putExtra(IntentConstant.WAP_URL, mUrl);
                startActivity(it);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
