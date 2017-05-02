package com.afmobi.palmchat.ui.activity.register;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.BaseForgroundLoginActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.OSInfo;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.broadcasts.SMSBroadcastReceiver;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.guide.NewGuideActivity;
import com.afmobi.palmchat.ui.activity.login.FacebookCompleteProfileActivity;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.profile.ComfireProfileActivity;
import com.afmobi.palmchat.ui.activity.setting.ChangePasswordActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.BaseDialog;
import com.afmobi.palmchat.ui.customview.datepicker.STDatePicker;
import com.afmobi.palmchat.ui.customview.datepicker.STDatePickerDialog;
import com.afmobi.palmchat.ui.customview.datepicker.STDatePickerDialog.OnDateSetListener;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.param.AfRegInfoParam;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.Profile.FetchProfileCallback;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 手机注册页面
 *
 * @author zhh
 */
public class RegistrationActivity extends BaseForgroundLoginActivity implements OnClickListener, AfHttpResultListener, OnConfirmButtonDialogListener {
    private TextView tv_country, tv_country_code, tv_notice, tv_reg_by_email, tv_reg_by_phone, tv_getstart;
    private EditText edt_phone;
    private Button btn_continue;
    private ImageView img_help;
    private EditText edt_pwd, edt_name, edt_veri_code, edt_call_veri_code;
    private TextView tv_birthday;
    private ViewGroup ll_birthday;
    private TextView tv_veri_city_code, tv_veri_phone;
    private TextView tv_female, tv_male;
    private ViewGroup ll_state;
    /*手机号/性别/验证码/密码/未收到验证码时 布局控件*/
    private ViewGroup ll_phone, ll_gender, ll_verification, ll_sms_verification, ll_call_vefification, rl_no_verification;
    private TextView tv_call, tv_sms;
    private ImageView img_left_back;
    private EditText edt_email;
    private TextView tv_title;
    /* 进度条对话框 */
    private Dialog dialog;
    private EditText edt_new_pwd;
    /* 重新发送验证码或编辑号码 */
    private ImageView img_resend_verifi;
    /**
     * 用于区分当前是 填写手机号页面 还是 填写密码页面 1：手机号 2：密码 3:名字 4:生日 5:地区 6:性别 7:短信验证码 8：语音验证码
     */
    private int type = 1;
    private static final String TAG = RegistrationActivity.class.getCanonicalName();
    /* 0 : male 1: female */
    private byte gender = -1;
    //	SimpleDateFormat mSimpleFormat = new SimpleDateFormat("dd-MM-yyyy");
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    /* 拦截短息广播 */
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    /* 国家信息对象封装 */
    private AfRegInfoParam mParam = new AfRegInfoParam();
    /* 第三方登录账号 */
    private String mThirdPartUserId;
    /* 默认注册方式为手机注册 */
    int mRegisterType = R_PHONE_NUMBER;
    private String mAccount;
    private String sms_code = "";
    /* 判断行为 （手机号注册/找回密码） */
    private String mAction;
    /* 行为具体类型 注册 */
    public static final String REG = "1";
    /* 行为具体类型 找回密码 */
    public static final String RESET = "2";
    public static final String BIND = "3";
    private String mPassword;
    /* 从哪个页面跳转过来的 */
    private String mFrom;
    public static final String REGISTER_TYPE = "mRegisterType";
    private AfPalmchat mAfCorePalmchat;
    /* 手机号注册 */
    public static final int R_PHONE_NUMBER = 0;
    private final int R_EMAIL = 1;
    public static final int PHONE_NUMBER = 0;
    public static final int EMAIL = 1;
    private String mRegisterAfid;
    /* 是否需要验证手机号是否已经注册 */
    private boolean mFlag = true;
    /* 是否是第三方登录 */
    private boolean isThirdPartLogin;
    AppDialog appDialog;
    private String mResetPassword;
    private String mResetAfid;
    //    private boolean key_back = false;
    /* 验证码倒计时时间 */
    int verifi_time = 60;
    public static final int EMAIL_REGISTER = 1;
    public static final int PHONE_REGISTER = 2;
    private boolean mCancel = false;
    protected int result;
    private BaseDialog mWheelWindow;
    private View viewHelp;
    /* 上次输入的号码 当60s内 相同手机号不重复请求验证码 */
    private String prePhoneNumber = "";
    /* 第一步输入手机号时所对应的国家 */
    private String prePhoneCountry = "";
    private String preEmail = "";

    /* 是否是重发验证码 */
    private boolean isResendSmsCode = false;
    final Handler handler = new Handler();
    /* 发送验证码提示DIalog */
    AppDialog sendVerifiNoticeDialog;
    /* 手机号尚未注册Dialog */
    AppDialog phoneNotRegisterDialog;
    private TextView tv_state_country, tv_state_city;
    private static final int ACTION_REGION = 3; // 地区
    private String mState, mCity;
    private static final String REGISTERED = "1";
    private int LOGINMODE = 0;
    //名字禁止输入表情符
    //输入表情前EditText中的文本
    private String tmpName;
    //是否重置了EditText的内容
    private boolean resetText;
    /**
     * fb个人信息
     */
    private Profile mFbProfile;
    private TextView mBtnFacebook;
    /**
     * 回调facebooksdk
     */
    private CallbackManager mCallbackManager;
    /* zhh 记录之前选中的日期 */
    private int year, monthOfYear, dayOfMonth;
    private boolean INAPPROPRIATE_WORD = false; // 名字是否是非法词汇
    /*是否从性别页面获取的验证码 成功*/
    private boolean gender_vefification_success = false;
    /*从性别页面获取的验证码 失败*/
    private boolean gender_vefification_failure = false;

    /**
     * 获取FB个人资料回调
     */
    private FetchProfileCallback mFetchProfileCallback = new FetchProfileCallback() {

        @Override
        public void onSuccess() {
            PalmchatLogUtils.i(TAG, "--FetchProfileCallback---onSuccess----");
            mHandler.sendEmptyMessage(DefaultValueConstant.FETCHPROFILE_SUCCESS);
        }

        @Override
        public void onError(FacebookException error) {
            PalmchatLogUtils.i(TAG, "--FetchProfileCallback---onError----" + error.toString());
            mHandler.sendEmptyMessage(DefaultValueConstant.FETCHPROFILE_FAILURE);
        }

    };
    /**
     * fackbook回调
     */
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_SUCCESS);
        }

        @Override
        public void onCancel() {
            PalmchatLogUtils.i(TAG, "--FacebookCallback---onCancel----");
            mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_CANCEL);
        }

        @Override
        public void onError(FacebookException exception) {
            mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_FAILURE);
        }
    };

    @Override
    public void findViews() {
        setContentView(R.layout.activity_registration);
        tv_country = (TextView) findViewById(R.id.tv_country);
        tv_country_code = (TextView) findViewById(R.id.tv_country_code);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        img_help = (ImageView) findViewById(R.id.img_help);
        mBtnFacebook = (TextView) findViewById(R.id.tv_facebook);
        tv_reg_by_email = (TextView) findViewById(R.id.tv_reg_by_email);
        tv_reg_by_phone = (TextView) findViewById(R.id.tv_reg_by_phone);
        edt_pwd = (EditText) findViewById(R.id.edt_pwd);
        ll_phone = (ViewGroup) findViewById(R.id.ll_phone);
        tv_getstart = (TextView) findViewById(R.id.tv_getstart);
        img_left_back = (ImageView) findViewById(R.id.img_left);
        edt_name = (EditText) findViewById(R.id.edt_name);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        ll_birthday = (ViewGroup) findViewById(R.id.ll_birthday);
        ll_gender = (ViewGroup) findViewById(R.id.ll_gender);
        tv_female = (TextView) findViewById(R.id.tv_female);
        tv_male = (TextView) findViewById(R.id.tv_male);
        ll_verification = (ViewGroup) findViewById(R.id.ll_verification);
        ll_sms_verification = (ViewGroup) findViewById(R.id.ll_sms_verification);
        ll_call_vefification = (ViewGroup) findViewById(R.id.ll_call_vefification);
        rl_no_verification = (ViewGroup) findViewById(R.id.rl_no_verification);
        tv_call = (TextView) findViewById(R.id.tv_call);
        tv_sms = (TextView) findViewById(R.id.tv_sms);
        edt_veri_code = (EditText) findViewById(R.id.edt_veri_code);
        edt_call_veri_code = (EditText) findViewById(R.id.edt_call_veri_code);
        tv_veri_city_code = (TextView) findViewById(R.id.tv_veri_city_code);
        tv_veri_phone = (TextView) findViewById(R.id.tv_veri_phone);
        edt_email = (EditText) findViewById(R.id.edt_email);
        tv_title = (TextView) findViewById(R.id.tv_title);
        edt_new_pwd = (EditText) findViewById(R.id.edt_new_pwd);
        tv_state_country = (TextView) findViewById(R.id.tv_state_country);
        tv_state_city = (TextView) findViewById(R.id.tv_state_city);
        ll_state = (ViewGroup) findViewById(R.id.ll_state);
        img_resend_verifi = (ImageView) findViewById(R.id.img_resend_verifi);

        mWheelWindow = new BaseDialog(this);
        viewHelp = LayoutInflater.from(this).inflate(R.layout.login_help_layout, null);
        mWheelWindow.setContentView(viewHelp);

        btn_continue.setOnClickListener(this);
        img_help.setOnClickListener(this);
        tv_country.setOnClickListener(this);
        img_left_back.setOnClickListener(this);
        tv_birthday.setOnClickListener(this);
        tv_female.setOnClickListener(this);
        tv_male.setOnClickListener(this);
        tv_notice.setOnClickListener(this);
        tv_reg_by_email.setOnClickListener(this);
        tv_reg_by_phone.setOnClickListener(this);
        mBtnFacebook.setOnClickListener(this);
        // tv_alredy_account.setOnClickListener(this);
        tv_state_city.setOnClickListener(this);
        tv_state_country.setOnClickListener(this);
        img_resend_verifi.setOnClickListener(this);
        tv_call.setOnClickListener(this);
        tv_sms.setOnClickListener(this);
//        /*表情过滤器*/
//        InputFilter[] emojiFilters = {emojiFilter};


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
                if (REG.equals(mAction)) {
                    edtTextChange(str.length(), getResources().getString(R.string.register_phone_notice));
                } else if (RESET.equals(mAction)) {
                    edtTextChange(str.length(), getResources().getString(R.string.enter_phone));
                }

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
                mPassword = str;
                edtTextChange(str.length(), getResources().getString(R.string.register_pwd_notice));
            }
        });

//        edt_name.setFilters(emojiFilters);
        edt_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!resetText && s.length() > 0 && start < s.length()) {
                    char codePoint = s.charAt(start);
                    if (isEmojiCharacter(codePoint)) {
                        resetText = true;
                        //是表情符号就将文本还原为输入表情符号之前的内容
                        edt_name.setText(tmpName);
                        edt_name.setSelection(tmpName.length());//设置EditText光标
                    }
                } else {
                    resetText = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (!resetText) {
                    tmpName = s.toString();//存储这次输入后的文本   好在现场输入表情后还原文本
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                edtTextChange(str.length(), getResources().getString(R.string.register_name_notice));
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
                if (str.length() > 0)
                    isClickBtnContinue(true);
                else
                    isClickBtnContinue(false);
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
                    isClickBtnContinue(true);
                else
                    isClickBtnContinue(false);
            }
        });


        edt_email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                edtTextChange(str.length(), getResources().getString(R.string.register_email_notice));
            }
        });

        edt_new_pwd.addTextChangedListener(new TextWatcher() {

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
                    isClickBtnContinue(true);
                else
                    isClickBtnContinue(false);
            }
        });

    }

    @Override
    public void init() {
        Intent intent = getIntent();
        mAction = intent.getStringExtra(JsonConstant.KEY_ACTION);
        mAccount = intent.getStringExtra(JsonConstant.KEY_AFID);
        mPassword = intent.getStringExtra(JsonConstant.KEY_PASS);
        mRegisterType = intent.getIntExtra(REGISTER_TYPE, R_PHONE_NUMBER);
        mFrom = intent.getStringExtra(JsonConstant.KEY_FROM);
        LOGINMODE = intent.getIntExtra(JsonConstant.KEY_MODE, LoginActivity.AFID);
        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        String[] str = CommonUtils.getCountryAndCode(this);
        tv_country.setText(str[0]);
        tv_state_country.setText(str[0]);
        tv_country_code.setText(str[1]);

        if (REG.equals(mAction)) {
            type = 1;
            changeUiByType();
            /* 获取本机号 */
            initMyPhone();

        } else if (BIND.equals(mAction)) {
            // bindPhone();
        } else if (RESET.equals(mAction)) {
            type = 1;
            changeUiByType();
        }

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

                edt_veri_code.setCursorVisible(false);
                edt_call_veri_code.setCursorVisible(false);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, mCallback);

    }

    @Override
    protected void onResume() {
        super.onResume();

        switch (type) {
            case 1: // 手机号
                if (mRegisterType == R_EMAIL) {
                    if (getEmail().length() > 0)
                        isClickBtnContinue(true);
                } else {
                    if (getPhoneNumber().length() > 0)
                        isClickBtnContinue(true);
                }

                break;
            case 2: // 密码
                if (getPassword().length() > 0)
                    isClickBtnContinue(true);

                break;
            case 3: // 名字
                if (getName().length() > 0)
                    isClickBtnContinue(true);
                break;
            case 4:

                break;
            case 5: // 地区
                isClickBtnContinue(true);
                break;

            case 7: // 验证码
                if (getVerifiCode().length() > 0)
                    isClickBtnContinue(true);

                break;
            case 8: // 语音验证码
                if (getVerifiCode().length() > 0)
                    isClickBtnContinue(true);

                break;

            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // mSocialNetworkManager.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // mSocialNetworkManager.onStop();
    }

    /**
     * 清除控件内容
     */
    private void clearContent() {
        edt_phone.setText("");
        edt_pwd.setText("");
        edt_name.setText("");
        tv_birthday.setText("");
        edt_email.setText("");
        edt_veri_code.setText("");
        edt_call_veri_code.setText("");

        handler.removeCallbacks(mRnnable);
//        key_back = false;
        initDate();

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
     * 获取手机号填写时国家
     *
     * @return
     */
    private String getCountry() {
        return tv_country.getText().toString().trim();
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
     * 获取密码
     *
     * @return
     */
    private String getPassword() {
        return edt_pwd.getText().toString().trim();
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
     * 获取邮箱
     *
     * @return
     */
    private String getEmail() {
        return edt_email.getText().toString().trim();
    }

    /**
     * 获取邮箱
     *
     * @return
     */
    private String getBirthday() {
        return tv_birthday.getText().toString().trim();
    }

    /**
     * 获取验证码
     *
     * @return
     */
    private String getVerifiCode() {
        if (type == 7)
            return edt_veri_code.getText().toString().trim();
        else if (type == 8)
            return edt_call_veri_code.getText().toString().trim();
        return "";
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
     * 获取新密码
     *
     * @return
     */
    private String getNewPwd() {
        return edt_new_pwd.getText().toString().trim();
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
     * 用palmID登录
     *
     * @param afid
     * @param pass
     * @param userdata
     */
    private void login(String afid, String pass, String userdata) {
        isThirdPartLogin = false;
        mAfCorePalmchat.AfHttpLogin(afid, pass, CommonUtils.getRealCountryCode(getCountryCode()), Consts.AF_LOGIN_AFID, userdata, this);
    }

    /**
     * 重置密码
     *
     * @param sms_code
     * @return
     */
    private int resetPwd(String sms_code) {
        return mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_RESET_PWD_BY_PHONE, getPhoneNumber(), CommonUtils.getRealCountryCode(getCountryCode()), null, null, sms_code, 0, this);
    }

    /**
     * 跳转到主页面
     *
     * @param login_type
     */
    private void toMainTab(byte login_type) {
        dismissProgressDialog();
        Intent intent = new Intent(this, MainTab.class);
        intent.putExtra(JsonConstant.KEY_IS_LOGIN, true);
        intent.putExtra(JsonConstant.KeyPopMsg_NotAFriend, true);
        intent.putExtra(JsonConstant.KEY_LOGIN_TYPE, login_type);
        startActivity(intent);
        finish();
    }

    /**
     * 用已经存在的手机号或邮箱登录
     */
    private void toLogin() {
        dismissProgressDialog();
        Intent intent = new Intent(this, LoginActivity.class);
        if (mRegisterType == R_EMAIL) {
            intent.putExtra(JsonConstant.KEY_MODE, LoginActivity.EMAIL);
            intent.putExtra(JsonConstant.KEY_EMAIL, getEmail());
            intent.putExtra(JsonConstant.KEY_IS_REG_BY_EMAIL, true);
            intent.putExtra(JsonConstant.KEY_IS_SHOW_PHONE_NUMBER, false);
        } else {
            intent.putExtra(JsonConstant.KEY_MODE, LoginActivity.PHONE_NUMBER);
            intent.putExtra(JsonConstant.KEY_PHONE, getPhoneNumber());
            intent.putExtra(JsonConstant.KEY_IS_SHOW_PHONE_NUMBER, true);
        }
        startActivity(intent);
        finish();
    }

    /**
     * 跳转到重置密码页面
     *
     * @param afid
     * @param pass
     */
    private void toReset(String afid, String pass) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, ResetActivity.class);
        intent.putExtra(JsonConstant.KEY_UUID, afid);
        intent.putExtra(JsonConstant.KEY_PASSWD, pass);
        intent.putExtra(JsonConstant.KEY_FLAG, true);
        intent.putExtra(JsonConstant.KEY_ACTION, mAction);
        startActivity(intent);
        finish();// ??

    }


    private void onSuccess(String action, boolean flag) {
        mFlag = flag;
        if (!mCancel) {
            // 注册
            if (REG.equals(action)) {
                // 注册
                result = register();
            } else if (BIND.equals(action)) {
                // verifyResetPasswordOrBind(BIND, flag);
            } else if (RESET.equals(action)) {
                String smsCode = edt_veri_code.getText().toString().trim();
                resetPwd(smsCode);

            }

        }

    }

    /**
     * 手机号注册
     */
    private void registerByPhone() {
        if (!mFlag) { // 不需要验证手机号是否注册
            mParam.password = "111111";
        }

        if (CommonUtils.isEmpty(getCountry())) {
            mParam.country = getCountry();
        }

        result = mAfCorePalmchat.AfHttpRegister(mParam, Consts.REQ_REG_BY_PHONE, 0, this);
        PalmchatLogUtils.println("registerByPhone  mHandler  " + mHandler);
    }

    /**
     * 找回密码
     *
     * @param reset
     * @param flag
     */
    private void resetPasswordBeforeCheckAccount(String reset, boolean flag) {
        mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_CHECK_ACCOUNT_BY_PHONE, getPhoneNumber(), CommonUtils.getRealCountryCode(getCountryCode()), null, null, null, reset, this);
    }

    /**
     * 提交数据到服务器
     */
    private void submitData() {
        mPassword = getPassword();
        if (mRegisterType == R_EMAIL) {// email方式注册
            mAccount = getEmail();
            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.W_EMAIL_N);
            // MobclickAgent.onEvent(context, ReadyConfigXML.W_EMAIL_N);
            if (!TextUtils.isEmpty(mAction)) {
                showProgDialog(R.string.verifyloading);
                // 邮箱注册
                onSuccess(mAction, false);// email

                // CommonUtils.getMyCountryInfo(context, mHandler,
                // EMAIL_REGISTER);
            }

        } else {// 手机号码

            mAccount = getPhoneNumber();

            // heguiming 2013-12-04
            new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_P_F);
            new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.W_PNUM_N);
            // MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_P_F);
            // MobclickAgent.onEvent(context, ReadyConfigXML.W_PNUM_N);

            sms_code = getVerifiCode();
            if (!TextUtils.isEmpty(mAction)) {
                if (RESET.equals(mAction)) {
                    if (TextUtils.isEmpty(sms_code)) {
                        ToastManager.getInstance().show(this, R.string.verification_code_not_empty);
                        return;
                    }
                    showProgDialog(R.string.verifyloading);
                    onSuccess(mAction, mFlag);
                    return;
                }
                showProgDialog(R.string.verifyloading);
                onSuccess(mAction, mFlag);// phone
                // /* 获取经纬度和国家信息 */
                // CommonUtils.getMyCountryInfo(context, mHandler,
                // PHONE_REGISTER);
            }
        }

    }

    /**
     * 继续下一步操作
     */
    private void nextStep() {

        switch (type) {
            case 1: // 手机号
                if (REG.equals(mAction)) {
                    if (mRegisterType == R_PHONE_NUMBER) { // 手机号注册
                        mAccount = getPhoneNumber();
                        if (mAccount.length() >= 8 && mAccount.length() <= 16) {
                        /* 友盟埋点统计 */
                            new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_P_F);
                            new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.W_PNUM_N);

                            // MobclickAgent.onEvent(context,
                            // ReadyConfigXML.ENTRY_P_F);
                            // MobclickAgent.onEvent(context,
                            // ReadyConfigXML.W_PNUM_N);
                            if (!prePhoneNumber.equals(getPhoneNumber())) { // 当手机号改变时
                                edt_pwd.setText("");
                                edt_name.setText("");
                                tv_birthday.setText("");
                                edt_veri_code.setText("");
                                prePhoneNumber = getPhoneNumber();
                                initDate();
                                removeRunnable();
                            }
                            if (!prePhoneCountry.equals(getCountry())) { // 当手机号对应国家改变时
                                prePhoneCountry = getCountry();
                                tv_state_city.setText("");
                                removeRunnable();
                            }

                            type = 2;
                            changeUiByType(); // 切换到密码输入页面
                        } else { // 手机号无效
                            // gtf 205-5-22
                            new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.PNUM_INVA);
                            // MobclickAgent.onEvent(RegistrationActivity.this,
                            // ReadyConfigXML.PNUM_INVA);

                            ToastManager.getInstance().show(this, R.string.invalid_number);
                        }
                    } else if (mRegisterType == R_EMAIL) { // 邮箱注册
                        mAccount = getEmail();
                        if (!CommonUtils.isEmail(mAccount)) {
                            ToastManager.getInstance().show(this, R.string.prompt_email_address_not_legal);
                            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.EMAIL_ERROR);
                            // MobclickAgent.onEvent(context,
                            // ReadyConfigXML.EMAIL_ERROR);
                        } else if (mAccount.length() >= 7 && mAccount.length() < 100) {
                            if (!preEmail.equals(getEmail())) { // 当邮箱号改变时
                                edt_pwd.setText("");
                                edt_name.setText("");
                                tv_birthday.setText("");
                                preEmail = getEmail();
                                initDate();
                            }
                            type = 2;
                            changeUiByType(); // 切换到密码输入页面
                        } else {
                            ToastManager.getInstance().show(this, R.string.prompt_email_address_not_legal);
                        }
                    }

                } else if (BIND.equals(mAction)) {
                    // bindPhone();
                } else if (RESET.equals(mAction)) {
                    mAccount = getPhoneNumber();
                    if (mAccount.length() >= 8 && mAccount.length() <= 16) {
                        if (!prePhoneNumber.equals(getPhoneNumber())) { // 当手机号改变时
                            edt_pwd.setText("");
                            edt_veri_code.setText("");
                            prePhoneNumber = getPhoneNumber();
                            removeRunnable();
                        }

                        if (!prePhoneCountry.equals(getCountry())) { // 当手机号对应国家改变时
                            prePhoneCountry = getCountry();
                            tv_state_city.setText("");
                            removeRunnable();
                        }

                        showPhoneDialog(false);
                    } else { // 手机号无效
                        // gtf 205-5-22
                        new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.PNUM_INVA);
                        // MobclickAgent.onEvent(RegistrationActivity.this,
                        // ReadyConfigXML.PNUM_INVA);

                        ToastManager.getInstance().show(this, R.string.invalid_number);
                    }

                }

                break;
            case 2:// 密码
                String pwd = edt_pwd.getText().toString().trim();
                if (pwd.length() < 6 || pwd.length() > 16) {
                    ToastManager.getInstance().show(this, R.string.prompt_input_password_little6);
                    return;
                } else {
                    type = 3;
                    changeUiByType(); // 切换到密码输入页面
                }
                break;
            case 3: // 名字
                String name = edt_name.getText().toString().trim();
                if (name.length() > 0) {
                    type = 4;
                    changeUiByType();


                }
                break;
            case 4: // 生日
                String birthday = tv_birthday.getText().toString().trim();
                if (birthday.length() > 0) {
                    type = 5;
                    changeUiByType();
                }
                break;
            case 5: // 地区
                if (TextUtils.isEmpty(mState)) {
                    mState = DefaultValueConstant.OTHERS;
                }
                String city = getStateCity();
                if (CommonUtils.isEmpty(city)) {
                    ToastManager.getInstance().show(context, R.string.prompt_input_region);
                } else {
                    type = 6;
                    changeUiByType();
                }
                break;

            case 6: // 性别
                if (mRegisterType == R_PHONE_NUMBER) { // 手机号注册
                    showPhoneDialog(false);
                }

                // else if (mRegisterType == R_EMAIL) {// 邮箱注册
                // new ReadyConfigXML().saveReadyInt(ReadyConfigXML.W_EMAIL_N);
                // // MobclickAgent.onEvent(context, ReadyConfigXML.W_EMAIL_N);
                // if (!TextUtils.isEmpty(mAction)) {
                // showProgDialog(R.string.verifyloading);
                // onSuccess(mAction, false);// email
                //// CommonUtils.getMyCountryInfo(context, mHandler,
                // EMAIL_REGISTER);
                // }
                //
                // }

                break;
            case 7: // 验证码
                submitData();
                break;
            case 8://语音验证码
                submitData();
                break;

            default:
                break;
        }

    }

    /**
     * 显示或隐藏“提示文字信息”
     */
    private void hideOrShowNotice(boolean isShow, String text) {
        if (isShow) {
            if (!text.isEmpty()) {
                tv_notice.setVisibility(View.VISIBLE);
                tv_notice.setText(text);

                if (text.equals(getString(R.string.no_verification_received))) { // 验证码倒计时提示语
                    tv_notice.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                    tv_notice.setClickable(true);
                } else {
                    tv_notice.setTextColor(getResources().getColor(R.color.log_gray));
                    tv_notice.setClickable(false);
                }
            }

        } else
            tv_notice.setVisibility(View.GONE);
    }

    /**
     * 显示或隐藏 60s倒计时后未收到验证码时的布局
     */
    private void hideOrShowNoVerificationUI(boolean isShow) {
        if (REG.equals(mAction) && mRegisterType == R_PHONE_NUMBER) {
            if (isShow)
                rl_no_verification.setVisibility(View.VISIBLE);
            else
                rl_no_verification.setVisibility(View.GONE);
        }


    }

    /**
     * 当输入内容不为空时，控制UI显示
     *
     * @param length
     */
    private void edtTextChange(int length, String notice) {
        if (length > 0) {
            isClickBtnContinue(true);
            hideOrShowNotice(true, notice);
        } else {
            isClickBtnContinue(false);
            hideOrShowNotice(false, null);
        }
    }

    private void removeRunnable() {
        handler.removeCallbacks(mRnnable);
        verifi_time = 60;
    }

    /**
     * 是否可点击继续按钮
     *
     * @param bool
     */
    private void isClickBtnContinue(boolean bool) {
        if (bool) {
            btn_continue.setBackgroundResource(R.drawable.login_button_selector);
            btn_continue.setTextColor(Color.WHITE);
            btn_continue.setClickable(true);
        } else {
            btn_continue.setBackgroundResource(R.drawable.btn_blue_d);
            btn_continue.setTextColor(getResources().getColor(R.color.guide_text_color));
            btn_continue.setClickable(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue: // 继续
                nextStep();
                break;
            case R.id.img_help: // 帮助
                mWheelWindow.show();
                break;
            case R.id.tv_country: // 国家
                Intent intent = new Intent(RegistrationActivity.this, CountryActivity.class);
                intent.putExtra(JsonConstant.KEY_SELECT_COUNTRY_ONLY, true);
                startActivityForResult(intent, DefaultValueConstant._86);
                break;
            case R.id.img_left: // 返回按钮
                onBack();
                break;
            case R.id.tv_birthday: // 点击生日时 弹出时间选择对话框
                initTimePicker(true);
                break;
            case R.id.tv_female: // 女性
                gender = 1;
                if (mRegisterType == R_EMAIL) {
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.EMAIL_SEL_FEMALE);
                    // MobclickAgent.onEvent(RegistrationActivity.this,
                    // ReadyConfigXML.EMAIL_SEL_FEMALE);
                    submitData();

                } else {
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.SEL_FEMALE);
                    // MobclickAgent.onEvent(RegistrationActivity.this,
                    // ReadyConfigXML.SEL_FEMALE);
                    nextStep();
                }
                break;
            case R.id.tv_male: // 男性
                gender = 0;
                if (mRegisterType == R_EMAIL) {

                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.EMAIL_SEL_MALE);
                    // MobclickAgent.onEvent(RegistrationActivity.this,
                    // ReadyConfigXML.EMAIL_SEL_MALE);
                    submitData();
                } else {
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.SEL_MALE);
                    // MobclickAgent.onEvent(RegistrationActivity.this,
                    // ReadyConfigXML.SEL_MALE);
                    nextStep();
                }
                break;
//            case R.id.ll_verification: // 验证码
//                nextStep();
//                break;
            case R.id.tv_notice: // 没有得到验证码 重新获取
                // gtf 205-5-22
                new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.GET_CODE);
                // MobclickAgent.onEvent(RegistrationActivity.this,
                // ReadyConfigXML.GET_CODE);
                if (type == 7) {
                    isResendSmsCode = true;
                    showPhoneDialog(true);
                }
                break;
            case R.id.img_resend_verifi: // 验证码页面，重新发送验证码图片
                if (type == 7) {

                    // isResendSmsCode = true;
                    // showPhoneDialog(true);
                    reEditPhoneDialog();
                }
                break;
            case R.id.tv_reg_by_email: // 使用邮箱注册
                mRegisterType = R_EMAIL;
                clearContent();
                type = 1;
                changeUiByType();
                break;
            case R.id.tv_reg_by_phone: // 使用手机号注册
                type = 1;
                mRegisterType = R_PHONE_NUMBER;
                clearContent();
            /* 获取本机号 */
                initMyPhone();
                changeUiByType();
                break;
            case R.id.tv_facebook:
                try {
                    LoginManager.getInstance().logOut();
                } catch (Exception e) {
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LoginManager.getInstance().logInWithReadPermissions(RegistrationActivity.this, Arrays.asList("user_location", "user_birthday", "email", "user_friends"));
                    }
                }).start();

                break;
            case R.id.tv_alredy_account:

                if (REG.equals(mAction)) {
                    if (!MyActivityManager.getScreenManager().isExistsActivity(LoginActivity.class)) {
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                    finish();
                } else if (RESET.equals(mAction)) {
                    if (!MyActivityManager.getScreenManager().isExistsActivity(LoginActivity.class)) {
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                    finish();
                }
                break;
            case R.id.tv_state_city:// 地区选择
                Intent intent1 = new Intent(this, RegionTwoActivity.class);
                intent1.putExtra(JsonConstant.KEY_COUNTRY, getStateCountry());
                intent1.putExtra(JsonConstant.KEY_FLAG, false);
                startActivityForResult(intent1, ACTION_REGION);
                break;
            case R.id.tv_state_country: // 国家
                if (mRegisterType == R_EMAIL) {
                    preCountry = getStateCountry();
                    startActivityForResult(new Intent(RegistrationActivity.this, CountryActivity.class), DefaultValueConstant.RESULT_10);
                }
                break;
            case R.id.tv_call: //语音验证码
                if (type == 7) {
                    isResendSmsCode = false;
                    removeRunnable();

                    type = 8;
                    changeUiByType();
                } else if (type == 8) {
                    isResendSmsCode = true;
                    showPhoneDialog(true);
                }

                break;
            case R.id.tv_sms: //短信验证
                if (type == 8) {
                    isResendSmsCode = false;
                    removeRunnable();

                    type = 7;
                    changeUiByType();
                } else if (type == 7) {
                    isResendSmsCode = true;
                    showPhoneDialog(true);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 根据type改变当前页面 1：手机填写页面 2：密码填写页面
     */
    private void changeUiByType() {
        switch (type) {
            case 1: // 手机填写
                if (REG.equals(mAction)) {
                    mBtnFacebook.setVisibility(View.VISIBLE);
                    if (mRegisterType == R_EMAIL) { // 邮箱注册
                        ll_phone.setVisibility(View.GONE);
                        edt_email.setVisibility(View.VISIBLE);
                        getFocus(edt_email);
                        tv_reg_by_email.setVisibility(View.GONE);
                        tv_reg_by_phone.setVisibility(View.VISIBLE);
                    } else if (mRegisterType == R_PHONE_NUMBER) {
                        ll_phone.setVisibility(View.VISIBLE);
                        getFocus(edt_phone);
                        edt_email.setVisibility(View.GONE);
                        tv_reg_by_email.setVisibility(View.VISIBLE);
                        tv_reg_by_phone.setVisibility(View.GONE);
                    }
                    img_left_back.setVisibility(View.VISIBLE);// 5.1 wxl modifys
                } else if (RESET.equals(mAction)) {
                    ll_phone.setVisibility(View.VISIBLE);
                    getFocus(edt_phone);
                    img_left_back.setVisibility(View.VISIBLE);
                    tv_title.setVisibility(View.VISIBLE);
                    tv_title.setText(getString(R.string.phone_number));
                    tv_reg_by_email.setVisibility(View.GONE);
                    tv_reg_by_phone.setVisibility(View.GONE);
                    mBtnFacebook.setVisibility(View.GONE);
                    edt_email.setVisibility(View.GONE);
                    img_help.setVisibility(View.GONE);
                    // tv_alredy_account.setVisibility(View.GONE);
                    tv_getstart.setVisibility(View.GONE);
                }
                tv_getstart.setText(getResources().getString(R.string.getstarted));
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                btn_continue.setVisibility(View.VISIBLE);
                ll_verification.setVisibility(View.GONE);
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                // tv_getstart.setVisibility(View.VISIBLE);


                if (REG.equals(mAction)) {
                    if (mRegisterType == R_EMAIL) { // 邮箱注册
                        String email = getEmail();
                        if (email != null && email.length() > 0) {
                            edtTextChange(email.length(), getResources().getString(R.string.register_email_notice));
                        } else
                            edtTextChange(0, null);
                    } else if (mRegisterType == R_PHONE_NUMBER) {
                        String phoneNumber = getPhoneNumber();
                        if (phoneNumber != null && phoneNumber.length() > 0) {
                            edtTextChange(phoneNumber.length(), getResources().getString(R.string.register_phone_notice));
                        } else
                            edtTextChange(0, null);
                    }
                } else if (RESET.equals(mAction)) {
                    String phoneNumber = getPhoneNumber();
                    if (phoneNumber != null && phoneNumber.length() > 0) {
                        edtTextChange(phoneNumber.length(), getResources().getString(R.string.enter_phone));
                    } else
                        edtTextChange(0, null);
                }
                break;
            case 2: // 密码填写
                if (mRegisterType == R_EMAIL) {
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.EMAIL_CRE_PW);
                    // MobclickAgent.onEvent(RegistrationActivity.this,
                    // ReadyConfigXML.EMAIL_CRE_PW);
                } else {
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.REG_CRE_PW);
                    // MobclickAgent.onEvent(RegistrationActivity.this,
                    // ReadyConfigXML.REG_CRE_PW);
                }

                ll_phone.setVisibility(View.GONE);
                edt_email.setVisibility(View.GONE);
                tv_getstart.setText(getResources().getString(R.string.create_password));
                edt_pwd.setVisibility(View.VISIBLE);
                getFocus(edt_pwd);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                mBtnFacebook.setVisibility(View.GONE);
                tv_reg_by_email.setVisibility(View.GONE);
                tv_reg_by_phone.setVisibility(View.GONE);
                ll_verification.setVisibility(View.GONE);
                btn_continue.setVisibility(View.VISIBLE);
                img_left_back.setVisibility(View.VISIBLE);
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setVisibility(View.VISIBLE);

                String pwd = getPassword();
                if (pwd != null && pwd.length() > 0)
                    edtTextChange(pwd.length(), getResources().getString(R.string.register_pwd_notice));
                else
                    edtTextChange(0, null);
                break;
            case 3: // 名字
                if (mRegisterType == R_EMAIL) {
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.EMAIL_IPT_NAME);
                    // MobclickAgent.onEvent(RegistrationActivity.this,
                    // ReadyConfigXML.EMAIL_IPT_NAME);
                } else {
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.REG_IPT_NAME);
                    // MobclickAgent.onEvent(RegistrationActivity.this,
                    // ReadyConfigXML.REG_IPT_NAME);
                }
                ll_phone.setVisibility(View.GONE);
                edt_email.setVisibility(View.GONE);
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.VISIBLE);
                getFocus(edt_name);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                mBtnFacebook.setVisibility(View.GONE);
                tv_reg_by_email.setVisibility(View.GONE);
                ll_verification.setVisibility(View.GONE);
                btn_continue.setVisibility(View.VISIBLE);
                img_left_back.setVisibility(View.VISIBLE);
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setText(getResources().getString(R.string.what_your_name));
                tv_getstart.setVisibility(View.VISIBLE);

                String name = getName();
                if (name != null && name.length() > 0) {
                    edtTextChange(name.length(), getResources().getString(R.string.register_name_notice));
                    if (INAPPROPRIATE_WORD) { // 非法词汇
                        INAPPROPRIATE_WORD = false;
                        tv_notice.setText(getString(R.string.inappropriate_word));
                        tv_notice.setTextColor(getResources().getColor(R.color.public_group_disband_color)); // 设置字体为红色
                    }
                } else
                    edtTextChange(0, null);
                break;
            case 4: // 生日
                if (mRegisterType == R_EMAIL) {
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.EMAIL_IPT_BDATE);
                    // MobclickAgent.onEvent(RegistrationActivity.this,
                    // ReadyConfigXML.EMAIL_IPT_BDATE);
                } else {
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.REG_IPT_BDATE);
                    // MobclickAgent.onEvent(RegistrationActivity.this,
                    // ReadyConfigXML.REG_IPT_BDATE);
                }
                ll_phone.setVisibility(View.GONE);
                edt_email.setVisibility(View.GONE);
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.VISIBLE);
                ll_state.setVisibility(View.GONE);
                CommonUtils.closeSoftKeyBoard(tv_birthday);
                ll_gender.setVisibility(View.GONE);
                mBtnFacebook.setVisibility(View.GONE);
                tv_reg_by_email.setVisibility(View.GONE);
                ll_verification.setVisibility(View.GONE);
                btn_continue.setVisibility(View.VISIBLE);
                img_left_back.setVisibility(View.VISIBLE);
                tv_getstart.setText(getResources().getString(R.string.when_your_birthday));
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setVisibility(View.VISIBLE);

                String birthday = tv_birthday.getText().toString().trim();
                if (CommonUtils.isEmpty(birthday)) { // 默认为当前日期往后推六年
                    isClickBtnContinue(false);
                    initTimePicker(false);
                } else {
                    isClickBtnContinue(true);
                }
                hideOrShowNotice(false, null);
                break;
            case 5:// 地区
                ll_phone.setVisibility(View.GONE);
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.VISIBLE);
                ll_gender.setVisibility(View.GONE);
                CommonUtils.closeSoftKeyBoard(ll_state);
                hideOrShowNotice(false, null);
                mBtnFacebook.setVisibility(View.GONE);
                tv_reg_by_email.setVisibility(View.GONE);
                btn_continue.setVisibility(View.VISIBLE);
                ll_verification.setVisibility(View.GONE);
                img_left_back.setVisibility(View.VISIBLE);
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setText(getResources().getString(R.string.which_your_state));
                tv_getstart.setVisibility(View.VISIBLE);

                if (mRegisterType == R_PHONE_NUMBER) { // 手机号注册时，国家和第一步手机号填写时国家保持一致
                    tv_state_country.setText(getCountry());
                }
                isClickBtnContinue(true);
                hideOrShowNotice(false, null);
                break;
            case 6: // 性别
                if (mRegisterType == R_EMAIL) {
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.EMAIL_SEL_GDER);
                    // MobclickAgent.onEvent(RegistrationActivity.this,
                    // ReadyConfigXML.EMAIL_SEL_GDER);
                } else {
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.REG_SEL_GDER);
                    // MobclickAgent.onEvent(RegistrationActivity.this,
                    // ReadyConfigXML.REG_SEL_GDER);
                }
                ll_phone.setVisibility(View.GONE);
                edt_email.setVisibility(View.GONE);
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.VISIBLE);
                CommonUtils.closeSoftKeyBoard(ll_gender);
                hideOrShowNotice(true, getResources().getString(R.string.change_this_in_your_profile));
                // tv_notice.setText(getResources().getString(R.string.change_this_in_your_profile));
                mBtnFacebook.setVisibility(View.GONE);
                tv_reg_by_email.setVisibility(View.GONE);
                btn_continue.setVisibility(View.GONE);
                ll_verification.setVisibility(View.GONE);
                img_left_back.setVisibility(View.VISIBLE);
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setText(getResources().getString(R.string.what_your_gender));
                tv_getstart.setVisibility(View.VISIBLE);

                edtTextChange(0, null);
                break;

            case 7: // 验证码
                // gtf 205-5-22
                new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.SMS_CODE);
                // MobclickAgent.onEvent(RegistrationActivity.this,
                // ReadyConfigXML.SMS_CODE);

                // }
                ll_phone.setVisibility(View.GONE);
                edt_email.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                mBtnFacebook.setVisibility(View.GONE);
                tv_reg_by_email.setVisibility(View.GONE);
                tv_reg_by_phone.setVisibility(View.GONE);
                btn_continue.setVisibility(View.VISIBLE);
                ll_verification.setVisibility(View.VISIBLE);
                ll_sms_verification.setVisibility(View.VISIBLE);
                ll_call_vefification.setVisibility(View.GONE);
                hideOrShowNoVerificationUI(false);
                getFocus(edt_veri_code);
                edt_veri_code.setCursorVisible(true);
                tv_veri_phone.setText(getPhoneNumber());
                img_left_back.setVisibility(View.VISIBLE);
                tv_getstart.setText(getResources().getString(R.string.register_verification_title));
                tv_veri_city_code.setText(getCountryCode());

                tv_getstart.setVisibility(View.GONE);

                String vCode = getVerifiCode();
                if (TextUtils.isEmpty(vCode)) {
                    isClickBtnContinue(false);
                } else {
                    isClickBtnContinue(true);

                }

                if (gender_vefification_success) {
                    gender_vefification_success = false;
                    handler.postDelayed(mRnnable, 1000);
                } else if (gender_vefification_failure) {
                    gender_vefification_failure = false;
                    hideOrShowNoVerificationUI(true);
                } else {
                    if (verifi_time == 0 || verifi_time == 60) {
                        removeRunnable();
                        hideOrShowNotice(false, null);
                        showProgDialog(R.string.please_wait);
                        getSmsCode();
                    } else {
                        String timeStr = getString(R.string.register_verifi_notice);
                        timeStr = timeStr.replace("XXXX", verifi_time + "");
                        hideOrShowNotice(true, timeStr);
                    }
                }


                break;
            case 8: //语音验证码
                ll_phone.setVisibility(View.GONE);
                edt_email.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                mBtnFacebook.setVisibility(View.GONE);
                tv_reg_by_email.setVisibility(View.GONE);
                tv_reg_by_phone.setVisibility(View.GONE);
                btn_continue.setVisibility(View.VISIBLE);
                ll_verification.setVisibility(View.VISIBLE);
                ll_sms_verification.setVisibility(View.GONE);
                ll_call_vefification.setVisibility(View.VISIBLE);
                hideOrShowNoVerificationUI(false);
                getFocus(edt_call_veri_code);
                edt_call_veri_code.setCursorVisible(true);
                img_left_back.setVisibility(View.VISIBLE);
                tv_getstart.setVisibility(View.GONE);

                String vCallCode = getVerifiCode();
                if (TextUtils.isEmpty(vCallCode)) {
                    isClickBtnContinue(false);
                } else {
                    isClickBtnContinue(true);
                }

                if (verifi_time == 0 || verifi_time == 60) {
                    handler.removeCallbacks(mRnnable);
                    hideOrShowNotice(false, null);
                    verifi_time = 60;
                    showProgDialog(R.string.please_wait);
                    getSmsCode();
                } else {
                    String timeStr2 = getString(R.string.register_verifi_notice);
                    timeStr2 = timeStr2.replace("XXXX", verifi_time + "");
                    hideOrShowNotice(true, timeStr2);
                }


                break;

            default:
                break;
        }
    }

    /**
     * 按返回按钮时 返回到上一页面
     */
    private void onBack() {
        switch (type) {
            case 1:
                exit();
                break;
            case 2: // 密码页面
                type = 1;
                changeUiByType();
                break;
            case 3:
                type = 2;
                changeUiByType();
                break;
            case 4:
                type = 3;
                changeUiByType();
                break;
            case 5:
                type = 4;
                changeUiByType();
                break;
            case 6:
                type = 5;
                changeUiByType();
                break;
            case 7:

                showGoBackDialog();

                break;
            case 8:
                showGoBackDialog();
                break;

            default:
                break;
        }
    }

    /**
     * 退出当前activity
     */
    private void exit() {
        if (CommonUtils.isEmpty(mFrom)) {
            if (R_PHONE_NUMBER == mRegisterType) {
                Intent mIntent = new Intent(this, LoginActivity.class);
                if (RESET.equals(mAction)) {
                    if (LOGINMODE == LoginActivity.AFID) {
                        mIntent.putExtra(JsonConstant.KEY_MODE, LoginActivity.AFID);
                    } else if (LOGINMODE == LoginActivity.PHONE_NUMBER) {
                        mIntent.putExtra(JsonConstant.KEY_MODE, LoginActivity.PHONE_NUMBER);
                    } else if (LOGINMODE == LoginActivity.EMAIL) {
                        mIntent.putExtra(JsonConstant.KEY_MODE, LoginActivity.EMAIL);
                    }
                }

                startActivity(mIntent);
                finish();
            } else {
                mRegisterType = R_PHONE_NUMBER;
                type = 1;
                clearContent();
                changeUiByType();
            }

        } else {
            if (R_EMAIL == mRegisterType) {
                mRegisterType = R_PHONE_NUMBER;
                type = 1;
                clearContent();
                changeUiByType();

            } else {
                if (mFrom.equals(ChangePasswordActivity.class.getCanonicalName())) {
                    finish();
                } else if (NewGuideActivity.class.getName().equals(mFrom)) {

                    finish();
                } else {
                    finish();
                }
            }

        }

//        key_back = true;
        mAfCorePalmchat.AfHttpRemoveAllListener();
    }

    /* 初始化日期控件 */
    private void initTimePicker(boolean isShow) {

        final AfProfileInfo afProfileInfo = CacheManager.getInstance().getMyProfile();

        Calendar c = Calendar.getInstance();
        //
        // final Calendar dateAndTime =
        // Calendar.getInstance(Locale.getDefault());
        STDatePickerDialog pickerDialog = new STDatePickerDialog(context, new OnDateSetListener() {

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
                    RegistrationActivity.this.year = year;
                    RegistrationActivity.this.monthOfYear = monthOfYear;
                    RegistrationActivity.this.dayOfMonth = dayOfMonth;

                    afProfileInfo.birth = formatedDate;
                    tv_birthday.setText(formatedDate);
                    isClickBtnContinue(true);
                } else {
                    ToastManager.getInstance().show(context, R.string.valide_birthday);
                }

            }
        }, year, monthOfYear, dayOfMonth, c.getTimeInMillis());

        if (isShow)
            pickerDialog.show();

    }

    private void reEditPhoneDialog() {
        AppDialog appDialog = new AppDialog(this);
        appDialog.createConfirmDialog(RegistrationActivity.this, getString(R.string.edit_your_phone_number), new OnConfirmButtonDialogListener() {

            @Override
            public void onLeftButtonClick() {

            }

            @Override
            public void onRightButtonClick() {
//                isChangePhoneNumBer = true;
                type = 1;
                changeUiByType();
            }

        });
        appDialog.show();
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
        appDialog.createChangePhoneDialog(RegistrationActivity.this, getString(R.string.sms_code_will_sent), countryCode, phoneNumber, getString(R.string.edit_phone_num), getString(R.string.ok), new AppDialog.OnConfirmButtonDialogListener() {

            @Override
            public void onLeftButtonClick() {
                if (isResendSmsCode)
                    isResendSmsCode = false;
                type = 1;
                changeUiByType();
            }

            @Override
            public void onRightButtonClick() {
                if (!NetworkUtils.isNetworkAvailable(PalmchatApp.getApplication())) {//网络不可用
                    ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.network_unavailable);
                    return;
                } else {
                    if (type == 6) {
                        if (verifi_time == 0 || verifi_time == 60) {
                            removeRunnable();
                            showProgDialog(R.string.please_wait);
                            getSmsCode();
                        } else {
                            type = 7;
                            changeUiByType();
                        }
                    } else {
                        if (type == 8) {
                            changeUiByType();
                        } else {
                            type = 7;
                            changeUiByType();
                        }
                    }
                }
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
     * sms code等待认证goback提示提示框
     */
    private void showGoBackDialog() {
        AppDialog appDialog = new AppDialog(this);
        appDialog.createConfirmDialog(context, "", getString(R.string.sms_code_back_wait), getString(R.string.back), getString(R.string.wait), new OnConfirmButtonDialogListener() {

            @Override
            public void onLeftButtonClick() {
                if (type == 8) {
                    isResendSmsCode = false;
                    removeRunnable();

                    handler.removeCallbacks(mRnnable);
                }
                type = 1;
                changeUiByType();
            }

            @Override
            public void onRightButtonClick() {

            }

        });
        appDialog.show();
    }

    /**
     * 一天获取验证码超过三次
     */
    private void showVerifiDialog(int resId) {
        AppDialog appDialog = new AppDialog(this);
        appDialog.createOKDialog(context, getString(resId), new OnConfirmButtonDialogListener() {

            @Override
            public void onRightButtonClick() {
                if (type == 6) {
                    type = 7;
                    changeUiByType();
                }
            }

            @Override
            public void onLeftButtonClick() {

            }
        });
        appDialog.show();
    }

    /**
     * 获取短信验证码
     */
    private void getSmsCode() {
        if (RESET.equals(mAction)) {
            mAfCorePalmchat.AfHttpGetSMSCode(Consts.REQ_GET_SMS_CODE_BEFORE_LOGIN, CommonUtils.getRealCountryCode(getCountryCode()), getPhoneNumber(), Consts.SMS_CODE_TYPE_RSTPWD, 0, null, this);
        } else {
            if (type == 8) {
                mAfCorePalmchat.AfHttpGetSMSCode(Consts.REQ_GET_SMS_CODE_BEFORE_LOGIN, CommonUtils.getRealCountryCode(getCountryCode()), getPhoneNumber(), Consts.SMS_CODE_TYPE_REG, 1, null, this);
            } else {
                mAfCorePalmchat.AfHttpGetSMSCode(Consts.REQ_GET_SMS_CODE_BEFORE_LOGIN, CommonUtils.getRealCountryCode(getCountryCode()), getPhoneNumber(), Consts.SMS_CODE_TYPE_REG, 0, null, this);
            }

        }
    }

    private void dismissDialog() {
        if (null != dialog && dialog.isShowing()) {
            try {
                dialog.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private int register() {
        mParam.cc = CommonUtils.getRealCountryCode(getCountryCode());// PalmchatApp.getOsInfo().getCountryCode();
        mParam.imei = PalmchatApp.getOsInfo().getImei();
        mParam.imsi = PalmchatApp.getOsInfo().getImsi();
        mParam.sex = gender;
        mParam.birth = AfProfileInfo.getUploadBirth(this, getBirthday());
        mParam.name = getName();
        mParam.password = getPassword();
        mParam.user_ip = null;
        mParam.voip = null;
        mParam.city = mCity;
        mParam.region = mState;

        PalmchatLogUtils.println("mParam.region  " + mParam.region + "  mParam.country  " + mParam.country + "  mParam.city  " + mParam.city);

        if (mRegisterType == R_EMAIL) {
            mParam.country = getStateCountry();
            mParam.phone_or_email = getEmail();
            registerByEmail();
        } else {
            mParam.country = getCountry();
            mParam.smscode = getVerifiCode();
            mParam.phone_or_email = getPhoneNumber();
            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SMSVER_SUCC);
            // MobclickAgent.onEvent(context, ReadyConfigXML.SMSVER_SUCC);
            registerByPhone();

        }

        return result;
    }

    /**
     * 用邮箱号注册
     */
    void registerByEmail() {
        result = mAfCorePalmchat.AfHttpRegister(mParam, Consts.REQ_REG_BY_EMAIL, 0, this);
        PalmchatLogUtils.println("registerByPhone  registerByEmail  " + result);
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


    private String preCountry;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DefaultValueConstant._86) {//只选国家
            if (data != null) {
                if (type == 1) {
                    tv_country.setText(data.getStringExtra(JsonConstant.KEY_COUNTRY));
                    tv_country_code.setText(data.getStringExtra(JsonConstant.KEY_COUNTRY_CODE));

                }
                /*else if (type == 5) {// 注册到选择省市那步，如果改了国家 就弹出选省市
                    tv_state_country.setText(data.getStringExtra(JsonConstant.KEY_COUNTRY_TEXT));
					Intent intent = new Intent(this, RegionTwoActivity.class);
					intent.putExtra("country", getStateCountry());
					intent.putExtra(JsonConstant.KEY_FLAG, false);
					startActivityForResult(intent, ACTION_REGION);
				}*/

            }
        } else if (requestCode == ACTION_REGION || requestCode == DefaultValueConstant.RESULT_10) {//选了国家 省市
            if (data == null) {
                if (preCountry != null && !preCountry.equals(getStateCountry())) {
                    tv_state_city.setText("");
                }
                return;
            }
            String strCountry = data.getStringExtra(JsonConstant.KEY_COUNTRY);
            String strCountryCode = data.getStringExtra(JsonConstant.KEY_COUNTRY_CODE);
            if (!TextUtils.isEmpty(strCountry)) {
                tv_state_country.setText(strCountry);
            }
            if (!TextUtils.isEmpty(strCountryCode)) {
                tv_country_code.setText(strCountryCode);
            }
            mState = data.getStringExtra(JsonConstant.KEY_STATE);
            mCity = data.getStringExtra(JsonConstant.KEY_CITY);
            // String mCountry = data.getStringExtra(JsonConstant.KEY_COUNTRY);
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
    public void onBackPressed() {
        onBack();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // mSocialNetworkManager.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // mSocialNetworkManager.onDestroy();
        // 注销短信监听广播
        this.unregisterReceiver(mSMSBroadcastReceiver);
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        PalmchatLogUtils.println("registration flag = " + flag + " code = " + code);
        dismissAllDialog();
        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                case Consts.REQ_REG_BY_PHONE:
                case Consts.REQ_REG_BY_EMAIL: { // 手机号或者邮箱注册成功
                    AfProfileInfo info = CacheManager.getInstance().getMyProfile();
                    mRegisterAfid = (String) result;
                    info.afId = mRegisterAfid;
                    if (flag == Consts.REQ_REG_BY_PHONE) {
                        new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.REG_P_SUCC);
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LG_P_SUCC);

                        // MobclickAgent.onEvent(context,
                        // ReadyConfigXML.REG_P_SUCC);
                        // MobclickAgent.onEvent(context, ReadyConfigXML.LG_P_SUCC);

                    } else if (flag == Consts.REQ_REG_BY_EMAIL) {
                        new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.REG_E_SUCC);
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LG_P_SUCC);

                        // MobclickAgent.onEvent(context,
                        // ReadyConfigXML.REG_E_SUCC);
                        // MobclickAgent.onEvent(context, ReadyConfigXML.LG_P_SUCC);
                    }

                    System.out.println("ywp: AfOnResult: REQ_REG_BY_PHONE afid  = " + mRegisterAfid);

                    String pass = getPassword();
                    if (!mFlag && flag == Consts.REQ_REG_BY_PHONE) {
                        pass = "111111";
                    }
                    if (mRegisterAfid != null) {
                        login(mRegisterAfid, pass, null);
                    } else {
                        if (mRegisterType == R_EMAIL) {
                            ToastManager.getInstance().show(this, R.string.email_has_registered);
                        } else {
                            new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.PNUM_REGED);
                            // MobclickAgent.onEvent(context,
                            // ReadyConfigXML.PNUM_REGED);
                            ToastManager.getInstance().show(this, R.string.number_has_registered);
                        }
                        toLogin();
                    }
                    break;
                }
                case Consts.REQ_CHECK_ACCOUNT_BY_PHONE: { // 检查手机号是否已经注册过
                    String userdata = (String) user_data;
                    boolean is_exist = (boolean) (null != result);
                    System.out.println("ywp: AfOnResult: check account result  = " + is_exist);

                    if (RESET.equals(userdata)) {
                        if (is_exist) {
                            String smsCode = edt_veri_code.getText().toString().trim();
                            resetPwd(smsCode);

                        } else {
                            dismissProgressDialog();
                            appDialog = new AppDialog(context);
                            appDialog.createOKDialog(context, getString(R.string.phone_no_binded), RegistrationActivity.this);
                            appDialog.show();
                        }
                    } else {
                        if (is_exist) {
                            if (mRegisterType == R_EMAIL) {
                                ToastManager.getInstance().show(this, R.string.email_has_registered);
                            } else {
                                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.W_PNUM_R);
                                // MobclickAgent.onEvent(this,
                                // ReadyConfigXML.W_PNUM_R);
                                ToastManager.getInstance().show(this, R.string.number_has_registered);
                            }
                            toLogin();
                        } else {
                            registerByPhone();
                        }
                    }

                    break;
                }
                case Consts.REQ_FLAG_LOGIN: { // 用palmID登录成功后
                    dismissProgressDialog();
                    dismissDialog();
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
                    // PalmchatLogUtils.e(TAG, "Registration....login....lat = " +
                    // lat + ",lon = " + lon);
                    SharePreferenceUtils.getInstance(context).setLatitudeAndLongitude(lat, lon);
                    if (isThirdPartLogin) {
                        myProfile.third_account = mThirdPartUserId;
                        myProfile.password = Constants.FACEBOOK_PASSWORD;
                        SharePreferenceUtils.getInstance(this).setIsFacebookLogin(true);
                        CacheManager.getInstance().setMyProfile(myProfile);

                        mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, myProfile);
                        setLoadAccount(myProfile, Consts.AF_LOGIN_FACEBOOK, mAfCorePalmchat);
                        // "1,"was setted byserver to distinguish whether third
                        // login user registered or not.
                        if (!"1,".equals(myProfile.attr2) || (myProfile.city.equals("other") || myProfile.city.equals(DefaultValueConstant.OTHERS) || myProfile.country.equals(DefaultValueConstant.OTHERS) || myProfile.country.equals(DefaultValueConstant.OTHERS) || myProfile.region.equals(DefaultValueConstant.OTHERS) || myProfile.region.equals("other"))) {
                            Intent intent = new Intent(this, FacebookCompleteProfileActivity.class);
                            intent.putExtra("FacebookId", mThirdPartUserId);
                            intent.putExtra("afid", myProfile.afId);
                            intent.putExtra("SocialPerson", mFbProfile);

                            if (user_data instanceof Byte) {
                                byte thirdLoginType = (Byte) user_data;
                                intent.putExtra("ThirdLoginType", thirdLoginType);
                            }
                            startActivity(intent);
                        } else {
                            toMainTab(Consts.AF_LOGIN_FACEBOOK);
                        }
                    } else {
                        PalmchatLogUtils.println("Consts.REQ_LONGIN_1 user_data " + user_data);

                        if (RESET.equals(user_data)) {
                            PalmchatLogUtils.println("Consts.REQ_LONGIN_1 reset");
                            myProfile.password = mResetPassword;
                            setLoadAccount(myProfile, Consts.AF_LOGIN_AFID, mAfCorePalmchat);// 调中间件保存当前的登陆方式和信息
                            toReset(mResetAfid, mResetPassword);
                        } else {
                            setLoadAccount(myProfile, Consts.AF_LOGIN_AFID, mAfCorePalmchat);
                            // 跳转到补充资料页面
                            Intent intent = new Intent(context, ComfireProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    CacheManager.getInstance().setPalmGuessShow(myProfile.palmguess_flag == 1);
                    // CacheManager.getInstance().setOpenAirtime(myProfile.airtime
                    // == 1);
                    CacheManager.getInstance().setRecharge_intro_url(myProfile.recharge_intro_url); // zhh设置充值方式说明的
                    // url

				/*boolean isShowPalmGuessNew = false;
                int ver = SharePreferenceUtils.getInstance(this).getPalmguessVersion(myProfile.afId);
				if (ver < myProfile.palmguess_version) {
					SharePreferenceUtils.getInstance(this).setPalmguessVersion(myProfile.afId, myProfile.palmguess_version);
					SharePreferenceUtils.getInstance(this).setUnReadNewPalmGuess(true);
					isShowPalmGuessNew = true;
				}*/
                    // EventBus.getDefault().post(new
                    // EventLoginBackground(isShowPalmGuessNew));
                    PalmchatApp.getApplication().mAfCorePalmchat.AfHttpStatistic(false, true, new ReadyConfigXML().getNoLoginHttpJsonStr(), null, null);
                    break;
                }
                case Consts.REQ_BIND_BY_PHONE: {
                    // 直接登录
                    login(mAccount, mPassword, null);
                    break;
                }
                case Consts.REQ_RESET_PWD_BY_EMAIL:
                case Consts.REQ_RESET_PWD_BY_PHONE: {
                    String[] ret = (String[]) result;
                    if (ret != null && ret.length >= DefaultValueConstant.LENGTH_2) {
                        mResetAfid = ret[1];
                        mResetPassword = ret[0];

                        login(mResetAfid, mResetPassword, RESET);
                    } else {
                        dismissProgressDialog();
                        ToastManager.getInstance().show(context, R.string.failed);
                    }
                    break;
                }

                case Consts.REQ_GET_SMS_CODE_BEFORE_LOGIN: { // 获取验证码成功
                    dismissDialog();
                    if (isResendSmsCode) {
                        ToastManager.getInstance().show(context, R.string.text_random_promt);
                        isResendSmsCode = false;
                    }
                    if (type == 6) {
                        gender_vefification_success = true;
                        type = 7;
                        changeUiByType();

                    } else {
                        handler.postDelayed(mRnnable, 1000);
                    }

                    PalmchatLogUtils.println("REQ_GET_SMS_CODE result  " + result);
                    break;
                }
                default:
                    dismissProgressDialog();
                    break;
            }

        } else { // 请求失败

            dismissDialog();
            if (flag == Consts.REQ_FLAG_LOGIN) { // 用palmId登录失败
                Consts.getInstance().showToast(this, code, flag, http_code);
            } else if (flag == Consts.REQ_REG_BY_PHONE) { // 手机号注册失败
                if (code == Consts.REQ_CODE_SMS_CHECK_ERROR) { // 验证码不正确
                    // gtf 205-5-22
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.CODE_ERROR);
                    // MobclickAgent.onEvent(RegistrationActivity.this,
                    // ReadyConfigXML.CODE_ERROR);

                    ToastManager.getInstance().show(this, R.string.verification_code_not_correct);
                } else if (code == Consts.REQ_CODE_ILLEGAL_WORD) { // zhh相关语句中含有非法词汇
                    Consts.getInstance().showToast(this, code, flag, http_code);
                    INAPPROPRIATE_WORD = true;
                    type = 3;
                    changeUiByType();
                } else {
                    Consts.getInstance().showToast(this, code, flag, http_code);
                }

            } else if (flag == Consts.REQ_GET_SMS_CODE_BEFORE_LOGIN) { // 获取验证码失败
                if (REG.equals(mAction)) {
                    if (code == Consts.REQ_CODE_SMS_CODE_ALREADY_REGISTERED) { // 手机号已被注册
                        ToastManager.getInstance().show(context, R.string.number_has_registered);
                        if (isResendSmsCode)
                            isResendSmsCode = false;
                        toLogin();
                    } else if (code == Consts.REQ_CODE_SMS_CODE_LIMIT) { // 找回密码、手机号注册（一天之内获取验证码超过三次）
                        // gtf 205-5-22
                        new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.CODE_OVER);
                        // MobclickAgent.onEvent(RegistrationActivity.this,
                        // ReadyConfigXML.CODE_OVER);
                        if (isResendSmsCode)
                            isResendSmsCode = false;
                        hideOrShowNotice(false, null);
                        /*显示语音入口*/
                        hideOrShowNoVerificationUI(true);
                        if (type == 6) {
                            gender_vefification_failure = true;
                            showVerifiDialog(R.string.sms_code_limit);
                        } else if (type == 7) {
                            showVerifiDialog(R.string.sms_code_limit);
                        } else if (type == 8) {
                            showVerifiDialog(R.string.call_code_limit);
                        }

                    } else {
//                        handler.postDelayed(mRnnable, 1000);//因为网络超时 或其他原因导致的获取不到验证码 也开始倒计时
                        Consts.getInstance().showToast(this, code, flag, http_code);
                        if (type == 7 || type == 8)
                            hideOrShowNoVerificationUI(true);
                    }

                } else if (RESET.equals(mAction)) {
                    if (code == Consts.REQ_CODE_SMS_CODE_NOT_REGISTER_OR_BIND) { // 手机号尚未注册或者绑定
                        if (isResendSmsCode)
                            isResendSmsCode = false;
                        disMissSendVerifiNoticeDialog();
                        phoneNotRegisterDialog = new AppDialog(RegistrationActivity.this);
                        phoneNotRegisterDialog.createOKDialog(RegistrationActivity.this, getResources().getString(R.string.sms_code_not_register_or_bind), new OnConfirmButtonDialogListener() {

                            @Override
                            public void onLeftButtonClick() {

                            }

                            @Override
                            public void onRightButtonClick() {
                            }

                        });

                        phoneNotRegisterDialog.setOnDismissListener(new OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                type = 1;
                                changeUiByType();
                            }
                        });
                        phoneNotRegisterDialog.show();
                    } else if (code == Consts.REQ_CODE_SMS_CODE_LIMIT) { // 找回密码、手机号注册（一天之内获取验证码超过三次）
                        // gtf 205-5-22
                        new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.CODE_OVER);
                        // MobclickAgent.onEvent(RegistrationActivity.this,
                        // ReadyConfigXML.CODE_OVER);
                        if (isResendSmsCode)
                            isResendSmsCode = false;
                        hideOrShowNotice(false, null);
                        showVerifiDialog(R.string.sms_code_limit);
                    } else {
                        Consts.getInstance().showToast(this, code, flag, http_code);
                    }
                }

            } else if (flag == Consts.REQ_REG_BY_EMAIL) { // 邮箱注册失败
                if (code == Consts.REQ_CODE_USER_ALREADY_REGISTER) { // 邮箱已经注册
                    ToastManager.getInstance().show(this, R.string.email_has_registered);
                    toLogin();
                } else if (code == Consts.REQ_CODE_ILLEGAL_WORD) { // zhh相关语句中含有非法词汇
                    Consts.getInstance().showToast(this, code, flag, http_code);
                    INAPPROPRIATE_WORD = true;
                    type = 3;
                    changeUiByType();
                } else {
                    Consts.getInstance().showToast(this, code, flag, http_code);
                }
            } else {

                Consts.getInstance().showToast(this, code, flag, http_code);

                if (code == Consts.REQ_CODE_UNNETWORK) {
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.NETWORK_UN);
                    // MobclickAgent.onEvent(this, ReadyConfigXML.NETWORK_UN);
                } else if (code == Consts.REQ_REG_BY_PHONE || code == Consts.REQ_REG_BY_EMAIL) {
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.E_NETWORK_UN);
                    // MobclickAgent.onEvent(RegistrationActivity.this,
                    // ReadyConfigXML.E_NETWORK_UN);
                }

            }

        }

    }

    @Override
    public void onLeftButtonClick() {

    }

    @Override
    public void onRightButtonClick() {
        // TODO Auto-generated method stub
        if (appDialog != null) {
            appDialog.cancel();
            appDialog = null;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                // case EMAIL_REGISTER: // 邮箱注册
                // onSuccess(mAction, false);// email
                // break;
                // case PHONE_REGISTER: // 手机号注册 获取国家信息成功后
                // onSuccess(mAction, mFlag);// phone
                // break;
                // facebook成功处理ui
                case DefaultValueConstant.LOGINFACEBOOK_SUCCESS: {
                    ToastManager.getInstance().show(RegistrationActivity.this, R.string.success);
                    int socialNetworkID = msg.arg1;
                    setProfile();
                    break;
                }
                // facebook失败处理UI
                case DefaultValueConstant.LOGINFACEBOOK_FAILURE: {
                    dismissAllDialog();
                    ToastManager.getInstance().show(RegistrationActivity.this, R.string.please_try_again);
                    break;
                }

                case DefaultValueConstant.LOGINFACEBOOK_CANCEL: {
                    ToastManager.getInstance().show(RegistrationActivity.this, R.string.facebook_login_cancel_tip);
                    break;
                }
                // 获取个人资料成功
                case DefaultValueConstant.FETCHPROFILE_SUCCESS: {
                    setProfile();
                    break;
                }
                // 获取个人资料失败
                case DefaultValueConstant.FETCHPROFILE_FAILURE: {
                    dismissAllDialog();
                    ToastManager.getInstance().show(RegistrationActivity.this, R.string.fetchprofile_failure);
                    break;
                }
                default:
                    break;
            }

        }

        ;
    };

    private void setProfile() {
        showProgressDialog(R.string.loading);
        OSInfo os = PalmchatApp.getApplication().osInfo;
        Profile profile = Profile.getCurrentProfile();
        mFbProfile = profile;
        if (null == profile) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PalmchatLogUtils.i(TAG, "--FacebookCallback---profile==NULL----");
                    Profile.setFetchProfileCallback(mFetchProfileCallback);
                    Profile.fetchProfileForCurrentAccessToken();
                }
            }).start();
        }

        if ((null != os) && (null != profile)) {
            String countryCode = PalmchatApp.getApplication().osInfo.getCountryCode();
            isThirdPartLogin = true;
            mThirdPartUserId = profile.getId();
            AfRegInfoParam param = new AfRegInfoParam();
            param.cc = countryCode;
            param.imei = PalmchatApp.getOsInfo().getImei();
            param.imsi = PalmchatApp.getOsInfo().getImsi();
            param.sex = Consts.AFMOBI_SEX_MALE;

            param.birth = DefaultValueConstant.BIRTHDAY;
            param.name = null;
            param.phone_or_email = profile.getId();
            param.password = Constants.FACEBOOK_PASSWORD;

            param.region = PalmchatApp.getOsInfo().getState(context);
            param.city = PalmchatApp.getOsInfo().getCity(context);
            param.country = PalmchatApp.getOsInfo().getCountry(context);
            param.fb_token = AccessToken.getCurrentAccessToken().getToken();
            mAfCorePalmchat.AfHttpRegLogin(param, mThirdPartUserId, Consts.AF_LOGIN_FACEBOOK, Consts.AF_LOGIN_FACEBOOK, RegistrationActivity.this);
        }
    }

    Runnable mRnnable = new Runnable() {
        public void run() {
//            if (key_back) {
//                handler.removeCallbacks(this);
//                return;
//            }
            String timeStr = getString(R.string.register_verifi_notice);
            timeStr = timeStr.replace("XXXX", verifi_time + "");
            if (type == 7 || type == 8)
                hideOrShowNotice(true, timeStr);
            verifi_time--;

            if (verifi_time > 0) {
                handler.postDelayed(this, 1000);
                tv_notice.setClickable(false);
            } else {
                removeRunnable();
                // gtf 205-5-22
                new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.CODE_INVA);
                // MobclickAgent.onEvent(RegistrationActivity.this,
                // ReadyConfigXML.CODE_INVA);

                if (REG.equals(mAction) && mRegisterType == R_PHONE_NUMBER) {
                    if (type == 7 || type == 8) {
                        hideOrShowNotice(false, null);
                        hideOrShowNoVerificationUI(true);
                    }
                } else {
                    if (type == 7) {
                        hideOrShowNotice(true, getString(R.string.no_verification_received));
                    }

                }


            }
        }
    };

    private void disMissSendVerifiNoticeDialog() {
        if (null != sendVerifiNoticeDialog)
            sendVerifiNoticeDialog.dismiss();
    }

    private void disMissPhoneNotRegisterDialog() {
        if (null != phoneNotRegisterDialog)
            phoneNotRegisterDialog.dismiss();
    }

    /**
     * 初始化手机号为本机号码 zhh
     */
    private void initMyPhone() {
        String tel = CommonUtils.getMyPhoneNumber(this);
        if (!CommonUtils.isEmpty(tel)) {
            edt_phone.setText(tel);
            edt_phone.setSelection(tel.length());
        }

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

//    /**
//     * 过滤掉表情
//     */
//    InputFilter emojiFilter = new InputFilter() {
//        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
//
//
//        @Override
//        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//            Matcher emojiMatcher = emoji.matcher(source);
//            if (emojiMatcher.find()) {
//                return "";
//            }
//            return null;
//        }
//    };


    /**
     * 是否包含表情
     *
     * @param codePoint
     * @return 如果不包含 返回false,包含 则返回true
     */

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }


}