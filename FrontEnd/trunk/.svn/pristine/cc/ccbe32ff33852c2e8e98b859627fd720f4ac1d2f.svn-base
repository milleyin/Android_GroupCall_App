package com.afmobi.palmchat.ui.activity.register;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseForgroundLoginActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.broadcasts.SMSBroadcastReceiver;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.guide.NewGuideActivity;
import com.afmobi.palmchat.ui.activity.login.FacebookCompleteProfileActivity;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.profile.ComfireProfileActivity;
import com.afmobi.palmchat.ui.activity.setting.ChangePasswordActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.BaseDialog;
import com.afmobi.palmchat.ui.customview.datepicker.STDatePicker;
import com.afmobi.palmchat.ui.customview.datepicker.STDatePickerDialog;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.param.AfRegInfoParam;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;

import java.util.Arrays;
import java.util.Calendar;

/**
 * 手机号注册
 * Created by zhh on 2016/8/22.
 */
public class RegisteredByPhoneActivity extends BaseForgroundLoginActivity implements View.OnClickListener, AfHttpResultListener { //implements View.OnClickListener, AfHttpResultListener
    /*标题栏返回按钮*/
    private ImageView img_left_back;
    /*标题栏标题*/
    private TextView tv_title;
    /*标题*/
    private TextView tv_getstart;
    /*底部提示语*/
    private TextView tv_notice;
    /*提交按钮*/
    private Button btn_continue;
    /*facebook登录链接*/
    private TextView tv_facebook;
    /*邮箱注册链接*/
    private TextView tv_reg_by_email;

    /*使用已有账号登录链接*/
    private TextView tv_alredy_account;
    /*help链接*/
    private ImageView img_help;

    /*手机号填写*/
    private ViewGroup ll_phone;
    private TextView tv_country;
    private TextView tv_country_code;
    private EditText edt_phone;
    /*密码填写*/
    private EditText edt_pwd;
    /*名字填写*/
    private EditText edt_name;
    /*生日填写*/
    private ViewGroup ll_birthday;
    private TextView tv_birthday;
    /*地区填写*/
    private ViewGroup ll_state;
    private TextView tv_state_country;
    private TextView tv_state_city;
    /*性别填写*/
    private ViewGroup ll_gender;
    private TextView tv_female;
    private TextView tv_male;
    /*验证码填写*/
    private ViewGroup ll_verification;
    /*短信验证*/
    private ViewGroup ll_sms_verification;
    private TextView tv_veri_city_code;
    private TextView tv_veri_phone;
    private ImageView img_edit_phone;
    private EditText edt_veri_code;
    /*语音验证*/
    private ViewGroup ll_call_vefification;
    private EditText edt_call_veri_code;
    /*验证码倒计时提示语*/
    private TextView tv_verifi_time;
    /*未收到验证码时布局*/
    private ViewGroup rl_no_verification;
    private TextView tv_request_call;
    private TextView tv_resend_sms;

    /*当前步骤常量*/
    private final int PHONE = 1;
    private final int PASSWORD = 2;
    private final int NAME = 3;
    private final int BIRTHDAY = 4;
    private final int STATE = 5;
    private final int GENDER = 6;
    private final int VERIFICODE_MSG = 7;
    private final int VERIFICODE_CALL = 8;
    /*当前是哪个步骤*/
    private int step = PHONE;
    /*中间件接口*/
    private AfPalmchat mAfCorePalmchat;
    /* 记录第一步选择的国家 */
    private String prePhoneCountry = "";
    /* 第一步输入的手机号 当60s内 相同手机号不重复请求验证码 */
    private String prePhoneNumber = "";
    /*记录日期对话框中的日期 */
    private int year, monthOfYear, dayOfMonth;
    /*名字中是否含有非法词汇*/
    private boolean nameIsInappropriateWord = false;
    /*选择地区后返回*/
    private static final int ACTION_REGION = 3;
    /*保存选择的地区 和城市*/
    private String mState, mCity;
    /* 保存选择的性别 0 : male 1: female */
    private byte gender = -1;
    /*用来60s倒计时*/
    final Handler handler = new Handler();
    /* 验证码倒计时时间 */
    int verifi_time = 60;
    /* 是否是重发验证码 */
    private boolean isResendSmsCode = false;
    /*监听验证码短信，自动获取并填入验证码*/
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    /* 拦截短息广播 */
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    /*facebook*/
    private BaseDialog mWheelWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销短信监听广播
        this.unregisterReceiver(mSMSBroadcastReceiver);
    }

    @Override
    public void findViews() {
        setContentView(R.layout.activity_registered_by_phone);

        img_left_back = (ImageView) findViewById(R.id.img_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_getstart = (TextView) findViewById(R.id.tv_getstart);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        tv_facebook = (TextView) findViewById(R.id.tv_facebook);
        tv_reg_by_email = (TextView) findViewById(R.id.tv_reg_by_email);

        tv_alredy_account = (TextView) findViewById(R.id.tv_alredy_account);
        img_help = (ImageView) findViewById(R.id.img_help);

        ll_phone = (ViewGroup) findViewById(R.id.ll_phone);
        tv_country = (TextView) findViewById(R.id.tv_country);
        tv_country_code = (TextView) findViewById(R.id.tv_country_code);
        edt_phone = (EditText) findViewById(R.id.edt_phone);

        edt_pwd = (EditText) findViewById(R.id.edt_pwd);

        edt_name = (EditText) findViewById(R.id.edt_name);

        ll_birthday = (ViewGroup) findViewById(R.id.ll_birthday);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);

        ll_state = (ViewGroup) findViewById(R.id.ll_state);
        tv_state_country = (TextView) findViewById(R.id.tv_state_country);
        tv_state_city = (TextView) findViewById(R.id.tv_state_city);

        ll_gender = (ViewGroup) findViewById(R.id.ll_gender);
        tv_female = (TextView) findViewById(R.id.tv_female);
        tv_male = (TextView) findViewById(R.id.tv_male);

        ll_verification = (ViewGroup) findViewById(R.id.ll_verification);
        ll_sms_verification = (ViewGroup) findViewById(R.id.ll_sms_verification);
        tv_veri_city_code = (TextView) findViewById(R.id.tv_veri_city_code);
        tv_veri_phone = (TextView) findViewById(R.id.tv_veri_phone);
        img_edit_phone = (ImageView) findViewById(R.id.img_edit_phone);
        edt_veri_code = (EditText) findViewById(R.id.edt_veri_code);

        ll_call_vefification = (ViewGroup) findViewById(R.id.ll_call_vefification);
        edt_call_veri_code = (EditText) findViewById(R.id.edt_call_veri_code);

        tv_verifi_time = (TextView) findViewById(R.id.tv_verifi_time);

        rl_no_verification = (ViewGroup) findViewById(R.id.rl_no_verification);
        tv_request_call = (TextView) findViewById(R.id.tv_request_call);
        tv_resend_sms = (TextView) findViewById(R.id.tv_resend_sms);


        mWheelWindow = new BaseDialog(this);
        View viewHelp = LayoutInflater.from(this).inflate(R.layout.login_help_layout, null);
        mWheelWindow.setContentView(viewHelp);

        img_left_back.setOnClickListener(this);
        tv_country.setOnClickListener(this);
        tv_birthday.setOnClickListener(this);
        tv_state_city.setOnClickListener(this);
        tv_female.setOnClickListener(this);
        tv_male.setOnClickListener(this);
        tv_facebook.setOnClickListener(this);
        tv_reg_by_email.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        tv_request_call.setOnClickListener(this);
        tv_resend_sms.setOnClickListener(this);

        edt_phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                if (!TextUtils.isEmpty(str))
                    edtTextChange(true, true, true, R.string.register_phone_notice);
                else
                    edtTextChange(true, false, false, 0);
            }
        });

        edt_pwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                if (!TextUtils.isEmpty(str))
                    edtTextChange(true, true, true, R.string.register_pwd_notice);
                else
                    edtTextChange(true, false, false, 0);
            }
        });


        edt_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                if (!TextUtils.isEmpty(str))
                    edtTextChange(true, true, true, R.string.register_name_notice);
                else
                    edtTextChange(true, false, false, 0);
            }
        });

        edt_veri_code.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                if (!TextUtils.isEmpty(str))
                    edtTextChange(true, true, false, 0);
                else
                    edtTextChange(true, false, false, 0);
            }
        });

        edt_call_veri_code.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                if (str.length() > 0)
                    edtTextChange(true, true, false, 0);
                else
                    edtTextChange(true, false, false, 0);
            }
        });

    }

    @Override
    public void init() {

        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;

        /*初始化国家和国码*/
        String[] str = CommonUtils.getCountryAndCode(this);
        tv_country.setText(str[0]);
        tv_country_code.setText(str[1]);
        /*初始化手机号为本机号码*/
        initNativePhone();

        changeUiByStep();

        // 生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
        // 实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        // 注册广播
        registerReceiver(mSMSBroadcastReceiver, intentFilter);

        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String message) {
                edt_veri_code.setText(message);
                edt_call_veri_code.setText(message);
            }
        });

    }

    /**
     * 初始化手机号为本机号码
     */
    private void initNativePhone() {
        String tel = CommonUtils.getMyPhoneNumber(this);
        if (!CommonUtils.isEmpty(tel)) {
            edt_phone.setText(tel);
            edt_phone.setSelection(tel.length());
        }
    }

    /**
     * 设置控件获得焦点
     *
     * @param view
     */
    private void getFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.requestFocusFromTouch();
    }

    /**
     * 当输入内容不为空时，控制UI显示
     *
     * @param
     */
    private void edtTextChange(boolean isShowBtn, boolean btnClickAble, boolean isShowNotice, int resId) {
        isClickBtnContinue(isShowBtn, btnClickAble);
        hideOrShowNotice(isShowNotice, resId);
    }

    /**
     * 控制继续按钮
     *
     * @param isShowBtn
     * @param btnClickAble
     */
    private void isClickBtnContinue(boolean isShowBtn, boolean btnClickAble) {
        if (isShowBtn) {
            btn_continue.setVisibility(View.VISIBLE);
            if (btnClickAble) {
                btn_continue.setBackgroundResource(R.drawable.login_button_selector);
                btn_continue.setTextColor(Color.WHITE);
                btn_continue.setClickable(true);
            } else {
                btn_continue.setBackgroundResource(R.drawable.btn_blue_d);
                btn_continue.setTextColor(getResources().getColor(R.color.guide_text_color));
                btn_continue.setClickable(false);
            }
        } else {
            btn_continue.setVisibility(View.GONE);
        }

    }

    /**
     * 显示或隐藏“提示文字信息”
     */
    private void hideOrShowNotice(boolean isShow, int resId) {
        if (isShow) {
            if (resId != 0) {
                String text = getString(resId);
                tv_notice.setText(text);
                tv_notice.setVisibility(View.VISIBLE);

            }

        } else
            tv_notice.setVisibility(View.GONE);
    }

    /**
     * 显示或隐藏 60s倒计时后未收到验证码时的布局
     */
    private void hideOrShowNoVerificationUI(boolean isShow) {

        if (isShow)
            rl_no_verification.setVisibility(View.VISIBLE);
        else
            rl_no_verification.setVisibility(View.GONE);


    }

    /**
     * 初始化日期控件为当前时间向后推六年
     */
    private void initDate() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR) - 6;
        monthOfYear = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
    }


    /* 初始化日期控件 */
    private void initTimePicker() {

        final AfProfileInfo afProfileInfo = CacheManager.getInstance().getMyProfile();

        Calendar c = Calendar.getInstance();
        STDatePickerDialog pickerDialog = new STDatePickerDialog(context, new STDatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(STDatePicker view, int year, int monthOfYear, int dayOfMonth, String formatedDate) {
                Calendar c = Calendar.getInstance();
                if ((c.get(Calendar.YEAR) - year) < 6) { // 如果选择的日期与当前日期小于6年
                    ToastManager.getInstance().show(context, R.string.choose_right_birthday);
                    return;
                }
                // 修改日历控件的年，月，日
                // 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                if (CommonUtils.compareDate(String.valueOf(year),
                        String.valueOf(monthOfYear + 1), String.valueOf(dayOfMonth), view)) {
                    RegisteredByPhoneActivity.this.year = year;
                    RegisteredByPhoneActivity.this.monthOfYear = monthOfYear;
                    RegisteredByPhoneActivity.this.dayOfMonth = dayOfMonth;

                    afProfileInfo.birth = formatedDate;
                    tv_birthday.setText(formatedDate);
                    edtTextChange(true, true, false, 0);
                } else {
                    ToastManager.getInstance().show(context, R.string.valide_birthday);
                }

            }
        }, year, monthOfYear, dayOfMonth, c.getTimeInMillis());


        pickerDialog.show();

    }

    /**
     * 手机号码确认框
     */
    private void showPhoneDialog(final boolean isRetry) {
        if (isRetry) {
            // gtf 205-5-22
            new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.GET_CODE);

        } else {
            // gtf 205-5-22
            new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.PNUM_CON);

        }
        String countryCode = getCountryCode();
        String phoneNumber = getPhoneNumber();
        AppDialog appDialog = new AppDialog(this);
        appDialog.createChangePhoneDialog(RegisteredByPhoneActivity.this, getString(R.string.sms_code_will_sent), countryCode, phoneNumber, getString(R.string.edit_phone_num), getString(R.string.ok), new AppDialog.OnConfirmButtonDialogListener() {

            @Override
            public void onLeftButtonClick() {
                step = 1;
                changeUiByStep();
            }

            @Override
            public void onRightButtonClick() {
                /*设置重新发送验证码标识，用于获取验证码后提示*/
                if (isRetry)
                    isResendSmsCode = true;
                step = VERIFICODE_MSG;
                changeUiByStep();
            }

        });
        appDialog.show();
    }

    /**
     * 进度条对话框
     *
     * @param resId
     */
    private void showProgDialog(int resId) {
        if (dialog == null) {
            dialog = new Dialog(this, R.style.Theme_LargeDialog);
            dialog.setOnKeyListener(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_loading);
            ((TextView) dialog.findViewById(R.id.textview_tips)).setText(resId);

        }
        dialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dismissProgDialog() {
        if (null != dialog && dialog.isShowing()) {
            try {
                dialog.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 一天获取验证码超过三次
     */
    private void showVerifiDialog() {
        AppDialog appDialog = new AppDialog(this);
        appDialog.createOKDialog(context, getString(R.string.sms_code_limit), new AppDialog.OnConfirmButtonDialogListener() {

            @Override
            public void onRightButtonClick() {
            }

            @Override
            public void onLeftButtonClick() {

            }
        });
        appDialog.show();
    }

    /**
     * sms code等待认证goback提示提示框
     */
    private void showGoBackDialog() {
        AppDialog appDialog = new AppDialog(this);
        appDialog.createConfirmDialog(context, "", getString(R.string.sms_code_back_wait), getString(R.string.back), getString(R.string.wait), new AppDialog.OnConfirmButtonDialogListener() {

            @Override
            public void onLeftButtonClick() {
                if (step == 8) {
                    isResendSmsCode = false;
                    removeRunnable();
                }
                step = PHONE;
                changeUiByStep();
            }

            @Override
            public void onRightButtonClick() {

            }

        });
        appDialog.show();
    }


    /**
     * 开启60s倒计时线程
     */
    Runnable mRnnable = new Runnable() {
        public void run() {
            String timeStr = getString(R.string.register_verifi_notice);
            timeStr = timeStr.replace("XXXX", verifi_time + "");
            if (step == VERIFICODE_MSG || step == VERIFICODE_CALL) {
                tv_verifi_time.setText(timeStr);
                tv_verifi_time.setVisibility(View.VISIBLE);
            }

            verifi_time--;

            if (verifi_time > 0) {
                handler.postDelayed(this, 1000);
            } else {
                // gtf 205-5-22
                new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.CODE_INVA);

                if (step == VERIFICODE_MSG || step == VERIFICODE_CALL) {
                    tv_verifi_time.setVisibility(View.GONE);
                    rl_no_verification.setVisibility(View.VISIBLE);
                }

                removeRunnable();


            }
        }
    };

    /**
     * 移除60s倒计时线程
     */
    private void removeRunnable() {
        handler.removeCallbacks(mRnnable);
        verifi_time = 60;
    }

    /**
     * 获取短信验证码
     */
    private void getSmsCode() {
        if (step == VERIFICODE_CALL) {
            mAfCorePalmchat.AfHttpGetSMSCode(Consts.REQ_GET_SMS_CODE_BEFORE_LOGIN, CommonUtils.getRealCountryCode(getCountryCode()), getPhoneNumber(), Consts.SMS_CODE_TYPE_REG, 1, null, this);
        } else if (step == VERIFICODE_MSG) {
            mAfCorePalmchat.AfHttpGetSMSCode(Consts.REQ_GET_SMS_CODE_BEFORE_LOGIN, CommonUtils.getRealCountryCode(getCountryCode()), getPhoneNumber(), Consts.SMS_CODE_TYPE_REG, 0, null, this);
        }


    }


    /**
     * 用已经存在的手机号或邮箱登录
     */
    private void toLogin() {
        dismissProgressDialog();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(JsonConstant.KEY_MODE, LoginActivity.PHONE_NUMBER);
        intent.putExtra(JsonConstant.KEY_PHONE, getPhoneNumber());
        intent.putExtra(JsonConstant.KEY_IS_SHOW_PHONE_NUMBER, true);
        startActivity(intent);
        finish();
    }


    /**
     * 手机号注册
     *
     * @return
     */
    private void registeByPhone() {
        // heguiming 2013-12-04
        new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_P_F);
        new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.W_PNUM_N);

        showProgDialog(R.string.verifyloading);

        AfRegInfoParam mParam = new AfRegInfoParam();
        mParam.cc = CommonUtils.getRealCountryCode(getCountryCode());// PalmchatApp.getOsInfo().getCountryCode();
        mParam.imei = PalmchatApp.getOsInfo().getImei();
        mParam.imsi = PalmchatApp.getOsInfo().getImsi();
        mParam.sex = gender;
        mParam.birth = AfProfileInfo.getUploadBirth(this, getBirthDay());
        mParam.name = getName();
        mParam.password = getPassword();
        mParam.user_ip = null;
        mParam.voip = null;
        mParam.city = mCity;
        mParam.region = mState;
        mParam.country = getCountry();
        mParam.smscode = getVerifiCode();
        mParam.phone_or_email = getPhoneNumber();

        PalmchatLogUtils.println("mParam.region  " + mParam.region + "  mParam.country  " + mParam.country + "  mParam.city  " + mParam.city);
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SMSVER_SUCC);


//        if (!mFlag) { // 不需要验证手机号是否注册
//            mParam.password = "111111";
//        }


        mAfCorePalmchat.AfHttpRegister(mParam, Consts.REQ_REG_BY_PHONE, 0, this);
        PalmchatLogUtils.println("registerByPhone  mHandler  " + mHandler);

    }

    /**
     * 用palmID登录
     *
     * @param afid
     * @param pass
     * @param userdata
     */
    private void login(String afid, String pass, String userdata) {
        mAfCorePalmchat.AfHttpLogin(afid, pass, CommonUtils.getRealCountryCode(getCountryCode()), Consts.AF_LOGIN_AFID, userdata, this);
    }

    /**
     * 获取手机号填写时国家
     *
     * @return
     */
    private String getCountry() {
        return tv_country.getText().toString().trim();
    }

    /**
     * 获取国码
     *
     * @return
     */
    private String getCountryCode() {
        if (tv_country_code != null) {
            return tv_country_code.getText().toString().trim();
        }
        return "+" + PalmchatApp.getOsInfo().getCountryCode();
    }


    /**
     * 获取手机号
     *
     * @return
     */
    private String getPhoneNumber() {
        return edt_phone.getText().toString().trim();
    }

    /**
     * 获取生日
     *
     * @return
     */
    private String getBirthDay() {
        return tv_birthday.getText().toString().trim();
    }


    /**
     * 获取名字
     *
     * @return
     */
    private String getName() {
        return edt_name.getText().toString().trim();
    }

    /**
     * 获取密码
     *
     * @return
     */
    private String getPassword() {
        return edt_pwd.getText().toString().trim();
    }

    /**
     * 获取state时的国家
     *
     * @return
     */
    private String getStateCountry() {
        return tv_state_country.getText().toString().trim();
    }

    /**
     * 获取城市地区
     *
     * @return
     */
    private String getStateCity() {
        return tv_state_city.getText().toString().trim();
    }

    /**
     * 获取验证码
     *
     * @return
     */
    private String getVerifiCode() {
        if (step == VERIFICODE_MSG)
            return edt_veri_code.getText().toString().trim();
        else if (step == VERIFICODE_CALL)
            return edt_call_veri_code.getText().toString().trim();
        return "";
    }

    /**
     * 根据当前步骤刷新页面
     */
    private void changeUiByStep() {
        switch (step) {
            case PHONE: //手机号
                tv_getstart.setText(getResources().getString(R.string.getstarted));
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setVisibility(View.VISIBLE);
                ll_phone.setVisibility(View.VISIBLE);
                getFocus(edt_phone);
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                ll_verification.setVisibility(View.GONE);
                tv_facebook.setVisibility(View.VISIBLE);
                tv_reg_by_email.setVisibility(View.VISIBLE);


                String phoneNumber = getPhoneNumber();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    edtTextChange(true, true, true, R.string.register_phone_notice);
                } else
                    edtTextChange(true, false, false, 0);

                break;
            case PASSWORD:
                // gtf 205-5-22
                new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.REG_CRE_PW);

                tv_getstart.setText(getResources().getString(R.string.create_password));
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setVisibility(View.VISIBLE);
                edt_pwd.setVisibility(View.VISIBLE);
                getFocus(edt_pwd);
                ll_phone.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                ll_verification.setVisibility(View.GONE);
                tv_facebook.setVisibility(View.GONE);
                tv_reg_by_email.setVisibility(View.GONE);


                String pwd = getPassword();
                if (!TextUtils.isEmpty(pwd)) {
                    edtTextChange(true, true, true, R.string.register_pwd_notice);
                } else
                    edtTextChange(true, false, false, 0);
                break;
            case NAME:
                // gtf 205-5-22
                new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.REG_IPT_NAME);

                tv_getstart.setText(getResources().getString(R.string.what_your_name));
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setVisibility(View.VISIBLE);

                edt_name.setVisibility(View.VISIBLE);
                getFocus(edt_name);
                ll_phone.setVisibility(View.GONE);
                edt_pwd.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                ll_verification.setVisibility(View.GONE);
                tv_facebook.setVisibility(View.GONE);
                tv_reg_by_email.setVisibility(View.GONE);


                String name = getName();
                if (!TextUtils.isEmpty(name)) {
                    edtTextChange(true, true, true, R.string.register_name_notice);
                    if (nameIsInappropriateWord) { // 非法词汇
                        nameIsInappropriateWord = false;
                        tv_notice.setText(getString(R.string.inappropriate_word));
                        tv_notice.setTextColor(getResources().getColor(R.color.public_group_disband_color)); // 设置字体为红色
                    }
                } else
                    edtTextChange(true, false, false, 0);
                break;
            case BIRTHDAY:
                tv_getstart.setText(getResources().getString(R.string.when_your_birthday));
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setVisibility(View.VISIBLE);
                ll_birthday.setVisibility(View.VISIBLE);
                ll_phone.setVisibility(View.GONE);
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                ll_verification.setVisibility(View.GONE);
                tv_facebook.setVisibility(View.GONE);
                tv_reg_by_email.setVisibility(View.GONE);


                String birthDay = getBirthDay();
                if (!TextUtils.isEmpty(birthDay)) {
                    edtTextChange(true, true, false, 0);

                } else
                    edtTextChange(true, false, false, 0);

                break;
            case STATE:
                tv_getstart.setText(getResources().getString(R.string.which_your_state));
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setVisibility(View.VISIBLE);
                ll_state.setVisibility(View.VISIBLE);
                ll_phone.setVisibility(View.GONE);
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                ll_verification.setVisibility(View.GONE);
                tv_facebook.setVisibility(View.GONE);
                tv_reg_by_email.setVisibility(View.GONE);


                tv_state_country.setText(getCountry());
                edtTextChange(true, true, false, 0);
                break;
            case GENDER:
                // gtf 205-5-22
                new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.REG_SEL_GDER);

                tv_getstart.setText(getResources().getString(R.string.what_your_gender));
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setVisibility(View.VISIBLE);
                ll_gender.setVisibility(View.VISIBLE);
                CommonUtils.closeSoftKeyBoard(ll_gender);
                ll_phone.setVisibility(View.GONE);
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_verification.setVisibility(View.GONE);
                tv_facebook.setVisibility(View.GONE);
                tv_reg_by_email.setVisibility(View.GONE);

                btn_continue.setVisibility(View.GONE);
                edtTextChange(false, false, false, 0);
                break;
            case VERIFICODE_MSG:
                // gtf 205-5-22
                new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.SMS_CODE);

                tv_getstart.setVisibility(View.GONE);
                ll_verification.setVisibility(View.VISIBLE);
                ll_sms_verification.setVisibility(View.VISIBLE);
                getFocus(edt_veri_code);
                tv_verifi_time.setVisibility(View.GONE);
                ll_call_vefification.setVisibility(View.GONE);
                rl_no_verification.setVisibility(View.GONE);
                ll_phone.setVisibility(View.GONE);
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                tv_facebook.setVisibility(View.GONE);
                tv_reg_by_email.setVisibility(View.GONE);


                tv_veri_city_code.setText(getCountryCode());
                tv_veri_phone.setText(getPhoneNumber());

                String vCode = getVerifiCode();
                if (TextUtils.isEmpty(vCode)) {
                    edtTextChange(true, false, false, 0);
                } else {
                    edtTextChange(true, true, false, 0);
                }

                if (verifi_time == 0 || verifi_time == 60) {
                    removeRunnable();
                    showProgDialog(R.string.please_wait);
                    getSmsCode();
                } else {
                    String timeStr = getString(R.string.register_verifi_notice);
                    timeStr = timeStr.replace("XXXX", verifi_time + "");
                    tv_verifi_time.setText(timeStr);
                    tv_verifi_time.setVisibility(View.VISIBLE);
                }
                break;
            case VERIFICODE_CALL:
                tv_getstart.setVisibility(View.GONE);
                ll_verification.setVisibility(View.VISIBLE);
                ll_call_vefification.setVisibility(View.VISIBLE);
                getFocus(edt_call_veri_code);
                ll_sms_verification.setVisibility(View.GONE);
                tv_verifi_time.setVisibility(View.GONE);
                rl_no_verification.setVisibility(View.GONE);
                ll_phone.setVisibility(View.GONE);
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                tv_facebook.setVisibility(View.GONE);
                tv_reg_by_email.setVisibility(View.GONE);



                String vCallCode = getVerifiCode();
                if (TextUtils.isEmpty(vCallCode)) {
                    edtTextChange(true, false, false, 0);
                } else {
                    edtTextChange(true, true, false, 0);
                }

                if (verifi_time == 0 || verifi_time == 60) {
                    removeRunnable();
                    showProgDialog(R.string.please_wait);
                    getSmsCode();
                } else {
                    String timeStr = getString(R.string.register_verifi_notice);
                    timeStr = timeStr.replace("XXXX", verifi_time + "");
                    tv_verifi_time.setText(timeStr);
                    tv_verifi_time.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    /**
     * 下一步操作事件
     */
    private void nextStep() {
        switch (step) {
            case PHONE:

                if (getPhoneNumber().length() >= 8 && getPhoneNumber().length() <= 16) {
                        /* 友盟埋点统计 */
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_P_F);
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.W_PNUM_N);
                    //如果手机号改变，则清空之前填写的内容，并移除倒计时
                    if (!prePhoneNumber.equals(getPhoneNumber())) {
                        prePhoneNumber = getPhoneNumber();

                        removeRunnable();
                        edt_pwd.setText("");
                        edt_name.setText("");
                        tv_birthday.setText("");
                        tv_state_country.setText(getCountry());
                        tv_state_city.setText("");
                        edt_veri_code.setText("");
                        /*初始化日期控件为当前日期往后推六年*/
                        initDate();
                    }

                    //当手机号对应国家改变时，清空填写地区，并移除倒计时
                    if (!prePhoneCountry.equals(getCountry())) {
                        prePhoneCountry = getCountry();

                        removeRunnable();
                        tv_state_country.setText(getCountry());
                        tv_state_city.setText("");
                    }
                    //切换到密码输入页面
                    step = PASSWORD;
                    changeUiByStep();
                } else { // 手机号无效
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.PNUM_INVA);
                    ToastManager.getInstance().show(this, R.string.invalid_number);
                }


                break;
            case PASSWORD:
                if (getPassword().length() < 6 || getPassword().length() > 16) {
                    ToastManager.getInstance().show(this, R.string.prompt_input_password_little6);
                    return;
                } else {
                    // 切换到名字输入页面
                    step = NAME;
                    changeUiByStep();
                }
                break;
            case NAME:
                if (getName().length() > 0) {
                    step = BIRTHDAY;
                    changeUiByStep();
                }
                break;
            case BIRTHDAY:

                if (getBirthDay().length() > 0) {
                    step = STATE;
                    changeUiByStep();
                }
                break;
            case STATE:
                String city = getStateCity();
                if (CommonUtils.isEmpty(city)) {
                    ToastManager.getInstance().show(context, R.string.prompt_input_region);
                } else {
                    step = GENDER;
                    changeUiByStep();
                }
                break;
            case GENDER:
                showPhoneDialog(false);
                break;
            case VERIFICODE_MSG: // 短信验证码
                registeByPhone();
                break;
            case VERIFICODE_CALL://语音验证
                registeByPhone();
                break;

        }
    }

    /**
     * 回退事件处理
     */
    private void onBack() {
        switch (step) {
            case PHONE:
                mAfCorePalmchat.AfHttpRemoveAllListener();
                finish();
                break;
            case PASSWORD:
                step = PHONE;
                changeUiByStep();
                break;
            case NAME:
                step = PASSWORD;
                changeUiByStep();
                break;
            case BIRTHDAY:
                step = NAME;
                changeUiByStep();
                break;
            case STATE:
                step = BIRTHDAY;
                changeUiByStep();
                break;
            case GENDER:
                step = STATE;
                changeUiByStep();
                break;
            case VERIFICODE_MSG:
                showGoBackDialog();
                break;
            case VERIFICODE_CALL:
                showGoBackDialog();
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue: //继续按钮
                nextStep();
                break;
            case R.id.img_left: //返回按钮
                onBack();
                break;
            case R.id.tv_country: //选择国家
                Intent intent = new Intent(RegisteredByPhoneActivity.this, CountryActivity.class);
                intent.putExtra(JsonConstant.KEY_SELECT_COUNTRY_ONLY, true);
                startActivityForResult(intent, DefaultValueConstant._86);
                break;
            case R.id.tv_birthday: //生日日期选择对话框
                initTimePicker();
                break;
            case R.id.tv_state_city: //地区选择
                Intent intent1 = new Intent(this, RegionTwoActivity.class);
                intent1.putExtra(JsonConstant.KEY_COUNTRY, getStateCountry());
                intent1.putExtra(JsonConstant.KEY_FLAG, false);
                startActivityForResult(intent1, ACTION_REGION);
                break;
            case R.id.tv_female: //选择女性
                // gtf 205-5-22
                new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.SEL_FEMALE);
                gender = 1;
                nextStep();
                break;
            case R.id.tv_male: //选择男性
                // gtf 205-5-22
                new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.SEL_MALE);
                gender = 0;
                nextStep();
                break;
            case R.id.tv_facebook: //facebook登陆
                try {
                    LoginManager.getInstance().logOut();
                } catch (Exception e) {
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LoginManager.getInstance().logInWithReadPermissions(RegisteredByPhoneActivity.this, Arrays.asList("user_location", "user_birthday", "email", "user_friends"));
                    }
                }).start();
                break;
            case R.id.tv_reg_by_email: //邮箱登陆
                startActivity(new Intent(RegisteredByPhoneActivity.this,RegisteredByEmailActivity.class));
                break;
            case R.id.tv_request_call: //call
                if (step == VERIFICODE_MSG) {
                    isResendSmsCode = false;
                    removeRunnable();

                    step = VERIFICODE_CALL;
                    changeUiByStep();
                }
                break;
            case R.id.tv_resend_sms: //sms
                if (step == VERIFICODE_MSG) {
                    showPhoneDialog(true);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DefaultValueConstant._86) {//选择国家后返回
            if (data != null) {
                if (step == PHONE) {
                    tv_country.setText(data.getStringExtra(JsonConstant.KEY_COUNTRY));
                    tv_country_code.setText(data.getStringExtra(JsonConstant.KEY_COUNTRY_CODE));

                }

            }
        } else if (requestCode == ACTION_REGION) {  //选择地区后返回
            if (data == null) {
                return;
            }

            mState = data.getStringExtra(JsonConstant.KEY_STATE);
            mCity = data.getStringExtra(JsonConstant.KEY_CITY);

            if (TextUtils.isEmpty(mCity)) {
                mCity = DefaultValueConstant.OTHER;
            }
            if (TextUtils.isEmpty(mState)) {
                mState = DefaultValueConstant.OTHERS;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(mCity);
            if (sb.length() == 0) {
                sb.append(mState);
            } else {
                sb.append(",").append(mState);
            }

            tv_state_city.setText(sb.toString());
        }
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {

        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                // 获取验证码成功
                case Consts.REQ_GET_SMS_CODE_BEFORE_LOGIN: {
                    dismissProgDialog();
                    if (isResendSmsCode) {
                        ToastManager.getInstance().show(context, R.string.text_random_promt);
                        isResendSmsCode = false;
                    }
                    handler.postDelayed(mRnnable, 1000);
                    PalmchatLogUtils.println("REQ_GET_SMS_CODE result  " + result);
                    break;
                }
                case Consts.REQ_REG_BY_PHONE: //手机号注册成功
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.REG_P_SUCC);
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LG_P_SUCC);

                    String mRegisterAfid = (String) result;

                    String pass = getPassword();
//                    if (!mFlag && flag == Consts.REQ_REG_BY_PHONE) {
//                        pass = "111111";
//                    }

                    if (mRegisterAfid != null) {
                        /*保存新的afid到profileinfo*/
                        AfProfileInfo info = CacheManager.getInstance().getMyProfile();
                        info.afId = mRegisterAfid;
                        /*用注册后生成的afid进行登陆*/
                        login(mRegisterAfid, pass, null);
                    }
                    break;
                case Consts.REQ_FLAG_LOGIN: { // 用新Afid登录成功后
                    dismissProgDialog();
                    AfProfileInfo myProfile = (AfProfileInfo) result;
                    myProfile.password = getPassword();
                    CacheManager.getInstance().setMyProfile(myProfile);
                    double lat = 0d;
                    double lon = 0d;
                    PalmchatLogUtils.println("myProfile.lat:" + myProfile.lat + "  myProfile.lng:" + myProfile.lng);
                    if (!TextUtils.isEmpty(myProfile.lat) && myProfile.lat.trim().length() > 0) {
                        lat = CommonUtils.stringToDouble(myProfile.lat);
                    }
                    if (!TextUtils.isEmpty(myProfile.lng) && myProfile.lat.trim().length() > 0) {
                        lon = CommonUtils.stringToDouble(myProfile.lng);
                    }

                    SharePreferenceUtils.getInstance(context).setLatitudeAndLongitude(lat, lon);
                    PalmchatLogUtils.println("Consts.REQ_LONGIN_1 user_data " + user_data);
                    /*palmguess入口是否开放*/
                    CacheManager.getInstance().setPalmGuessShow(myProfile.palmguess_flag == 1);
                     /*Airtime*入口是否开放*/
                    // CacheManager.getInstance().setOpenAirtime(myProfile.airtime
                    // == 1);
                   /*zhh设置充值方式说明的 url*/
                    CacheManager.getInstance().setRecharge_intro_url(myProfile.recharge_intro_url);
                    /*统计用户登陆前的操作行为*/
                    PalmchatApp.getApplication().mAfCorePalmchat.AfHttpStatistic(false, true, new ReadyConfigXML().getNoLoginHttpJsonStr(), null, null);

                    /*通知中间件保存数据*/
                    setLoadAccount(myProfile, Consts.AF_LOGIN_AFID, mAfCorePalmchat);
                    // 跳转到补充资料页面
                    Intent intent = new Intent(context, ComfireProfileActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                }
                default:
                    dismissProgressDialog();
                    break;
            }
        } else {
            dismissProgDialog();
            if (flag == Consts.REQ_GET_SMS_CODE_BEFORE_LOGIN) { // 获取验证码失败
                if (isResendSmsCode)
                    isResendSmsCode = false;

                // 手机号已被注册
                if (code == Consts.REQ_CODE_SMS_CODE_ALREADY_REGISTERED) {
                    ToastManager.getInstance().show(context, R.string.number_has_registered);
                    toLogin();
                } else if (code == Consts.REQ_CODE_SMS_CODE_LIMIT) { // 找回密码、手机号注册（一天之内获取验证码超过三次）
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.CODE_OVER);

                    tv_verifi_time.setVisibility(View.GONE);
                    /*显示语音入口*/
                    hideOrShowNoVerificationUI(true);
                    showVerifiDialog();
                } else {
                    Consts.getInstance().showToast(this, code, flag, http_code);
                }


            } else if (flag == Consts.REQ_REG_BY_PHONE) { // 手机号注册失败
                if (code == Consts.REQ_CODE_SMS_CHECK_ERROR) { // 验证码不正确
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.CODE_ERROR);
                    ToastManager.getInstance().show(this, R.string.verification_code_not_correct);
                } else if (code == Consts.REQ_CODE_ILLEGAL_WORD) { // zhh相关语句中含有非法词汇
                    Consts.getInstance().showToast(this, code, flag, http_code);
                    nameIsInappropriateWord = true;
                    step = NAME;
                    changeUiByStep();
                } else {
                    Consts.getInstance().showToast(this, code, flag, http_code);
                }

            } else if (flag == Consts.REQ_FLAG_LOGIN) { // 用新Afid登录失败
                Consts.getInstance().showToast(this, code, flag, http_code);
            }
        }
    }
}
