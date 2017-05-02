package com.afmobi.palmchat.ui.activity.setting;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.db.DBState;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.register.CountryActivity;
import com.afmobi.palmchat.ui.activity.register.RegistrationActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

/**
 * 手机号码选项界面
 */
public class MyAccountInfoActivity extends BaseActivity implements OnClickListener, AfHttpResultListener {
    /**
     * 没有绑定手机号码 总布局
     */
    private ViewGroup mLl_Phone_Unbind;
    /**
     * 手机相关布局包括 国家 城市码 手机号
     */
    private ViewGroup mLl_Phone;
    /***/
    private ViewGroup mLl_Verificode;
    /**
     * 国家显示view
     */
    private TextView mTv_Country;
    /**
     * 城市码比如 +86
     */
    private TextView mTv_CityCode;
    /**
     * 手机号码标记框
     */
    private EditText mEdt_Phone;
    /**
     * 请输入您的手机号码
     */
    private TextView mTv_PhoneNotice;

    /***/
    private TextView mTv_Veri_CityCode;
    /***/
    private TextView mTv_Veri_Phone;
    /**
     * textview倒计时提示 your sms .....
     */
    private TextView mTv_Time;
    /**
     * 重新发送验证码或编辑号码
     */
    private ImageView mImg_Resend_Verifi;

    /**
     * 验证码
     */
    private EditText mEdt_VeriCode;
    /**
     * 继续按钮
     */
    private Button mBtn_Continue;
    /**
     * 标题布局
     */
    private ViewGroup mLl_Title;
    /**
     * 返回按钮
     */
    private ImageView mIv_BackButton;

    // 更改已绑定手机 start
    /***/
    private ViewGroup mLl_Update_Phone_Bind;
    private TextView mTv_Update_Phone_Bind;
    private Button mBtn_Update_Phone_Bind;
    /* 更改已绑定手机 end */

    /* 邮箱绑定 start */
    private ViewGroup mLl_Email_Unlinked;
    private EditText mEdt_Email_Unlinked;
    private TextView mTv_Email_Unlinked;
    private TextView mTv_Email_Unlinked_Notice;
    private Button mBtn_Email_Continue, btn_edit_email;
    private String mStr_PreEmail;
    /* 邮箱绑定 end */
    private TextView mTv_Title;
    /* 上次输入的号码 当60s内 相同手机号不重复请求验证码 */
    private String mStr_PrePhoneNumber = "";
    private String mPreCityCode = "";
    //    private boolean mIsChangePhoneNumBer = true; // 电话号码是否改变
    private boolean mIsResendSmsCode = false; // 是否是重发验证码
    private Dialog mDlg_Progress; // 进度条对话框

    // AppDialog appDialog;
    private AfPalmchat mAfCorePalmchat;
    private int mVerifiTime = 60; // 验证码倒计时时间
    private boolean mBln_KeyBack = false;
    /**
     * 限制字符的数量
     */
    private int mLimitNumber = 100;
    private final Handler mHandler = new Handler();
    private byte mBindState;
    private int mStep = 1; // 步骤一

    //	private final static int REQUEST_CODE_SELECT_COUNTRY = 87;
    public final static String STATE = "state";
    public final static String PARAM_INFO = "info";
    public final static String PARAM_COUNTRY_CODE = "code";
    public final static String PARAM_COUNTRY_NAME = "name";
    public final static byte PHONE_UNBIND = 0X1;
    public final static byte PHONE_UPDATE_BIND = 0X2;
    public final static byte EMAIL_UNBIND = 0X3;
    public final static byte EMAIL_BIND_UNVERIFI = 0X4;
    public final static byte EMAIL_BIND_VERIFICODE = 0X5;
    public static final int SET_SUCCESS_PHONE = 8001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void findViews() {
        setContentView(R.layout.activity_my_account_info);

		/* 手机号未绑定 start */
        // 输入手机号码界面
        mLl_Phone_Unbind = (ViewGroup) findViewById(R.id.ll_phone_unbind);
        mLl_Phone = (ViewGroup) findViewById(R.id.ll_phone1);

        mTv_Country = (TextView) findViewById(R.id.tv_country1);
        mTv_CityCode = (TextView) findViewById(R.id.tv_city_code1);
        mEdt_Phone = (EditText) findViewById(R.id.edt_phone1);
        mTv_PhoneNotice = (TextView) findViewById(R.id.tv_phone_notice1);

        // 输入验证码界面
        mLl_Verificode = (ViewGroup) findViewById(R.id.ll_verificode1);
        mTv_Veri_CityCode = (TextView) findViewById(R.id.tv_veri_city_code1);
        mTv_Veri_Phone = (TextView) findViewById(R.id.tv_veri_phone1);
        mEdt_VeriCode = (EditText) findViewById(R.id.edt_veri_code1);
        mTv_Time = (TextView) findViewById(R.id.tv_time1);
        mTv_Time.setOnClickListener(this);

        mImg_Resend_Verifi = (ImageView) findViewById(R.id.img_resend_verifi);
        mImg_Resend_Verifi.setOnClickListener(this);

//		mTv_Country.setOnClickListener(this);
        mEdt_Phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                if (str.length() > 0) {
                    isClickBtnContinue(true);
                } else {
                    isClickBtnContinue(false);
                }
            }
        });

        mEdt_VeriCode.addTextChangedListener(new TextWatcher() {

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
        /* 手机号未绑定 end */

		/* 更改已绑定手机号 start */
        mLl_Update_Phone_Bind = (ViewGroup) findViewById(R.id.ll_update_phone_bind);
        mTv_Update_Phone_Bind = (TextView) findViewById(R.id.tv_update_phone_bind_1);
        mBtn_Update_Phone_Bind = (Button) findViewById(R.id.btn_update_phone_bind);
        mBtn_Update_Phone_Bind.setOnClickListener(this);
        /* 更改已绑定手机号 start end */

		/* 邮箱绑定 start */
        mLl_Email_Unlinked = (ViewGroup) findViewById(R.id.ll_email_unlinked);
        mEdt_Email_Unlinked = (EditText) findViewById(R.id.edt_email_unlinked);
        /**设置限定输入的长度最大为100个字符*/
        mEdt_Email_Unlinked.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mLimitNumber)});
        mTv_Email_Unlinked = (TextView) findViewById(R.id.tv_email_unlinked);
        mTv_Email_Unlinked_Notice = (TextView) findViewById(R.id.tv_email_unlinked_notice);
        mBtn_Email_Continue = (Button) findViewById(R.id.btn_email_continue);
        btn_edit_email = (Button) findViewById(R.id.btn_edit_email);
        btn_edit_email.setOnClickListener(this);
        mBtn_Email_Continue.setOnClickListener(this);
        /* 邮箱绑定 end */

        mTv_Title = (TextView) findViewById(R.id.title_text);

        mIv_BackButton = (ImageView) findViewById(R.id.back_button);
        mIv_BackButton.setOnClickListener(this);
        mLl_Title = (ViewGroup) findViewById(R.id.title_layout);

        mBtn_Continue = (Button) findViewById(R.id.btn_continue);
        mBtn_Continue.setOnClickListener(this);

    }

    @Override
    public void init() {
        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        Intent it = getIntent();
        mBindState = it.getByteExtra(STATE, PHONE_UNBIND);

        if (mBindState == PHONE_UNBIND) {
            AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
            if (myProfile.country != null) {
                mTv_Country.setText(myProfile.country);
                String cc = DBState.getInstance(this).getCcFromCountry(myProfile.country);
                if (cc != null) {
                    if (cc.contains("+"))
                        mTv_CityCode.setText(cc);
                    else
                        mTv_CityCode.setText("+" + cc);

                }

            }

            // String[] str = CommonUtils.getCountryAndCode(this);
            // mTv_Country.setText(str[0]);
            // mTv_CityCode.setText(str[1]);

        } else if (mBindState == PHONE_UPDATE_BIND) {
            String phone = it.getStringExtra(PARAM_INFO);
            String linked_phone = this.getString(R.string.linked_phone).replace("XXXX", phone);
            mTv_Update_Phone_Bind.setText(linked_phone);
        } else if (mBindState == EMAIL_BIND_VERIFICODE || mBindState == EMAIL_BIND_UNVERIFI || mBindState == EMAIL_UNBIND) {
            String email = it.getStringExtra(PARAM_INFO);
            if (!CommonUtils.isEmpty(email)) {
                mTv_Email_Unlinked.setText(email);
            }
        }
        mStep = 1;
        changeUiByStep();
    }

    private void changeUiByStep() {
        switch (mStep) {
            case 1:
                if (mBindState == PHONE_UNBIND) { // 手机号未绑定
                    mTv_Title.setText(getResources().getString(R.string.phone));
                    mLl_Title.setVisibility(View.VISIBLE);
                    mLl_Phone_Unbind.setVisibility(View.VISIBLE);
                    mLl_Phone.setVisibility(View.VISIBLE);
                    mLl_Verificode.setVisibility(View.GONE);
                    mTv_PhoneNotice.setText(R.string.prompt_input_phone);
                    mTv_PhoneNotice.setVisibility(View.VISIBLE);
                    String phoneNUm = getPhoneNumber1();
                    if (CommonUtils.isEmpty(phoneNUm)) {
                        isClickBtnContinue(false);
                    } else {
                        isClickBtnContinue(true);
                    }
                } else if (mBindState == PHONE_UPDATE_BIND) { // 更改手机绑定
                    mLl_Update_Phone_Bind.setVisibility(View.VISIBLE);
                    mLl_Title.setVisibility(View.VISIBLE);
                } else if (mBindState == EMAIL_UNBIND) { // 邮箱未绑定
                    mTv_Title.setText(getResources().getString(R.string.email));
                    mLl_Title.setVisibility(View.VISIBLE);
                    mLl_Email_Unlinked.setVisibility(View.VISIBLE);
                    mEdt_Email_Unlinked.setVisibility(View.VISIBLE);
                    mTv_Email_Unlinked_Notice.setVisibility(View.VISIBLE);
                    mTv_Email_Unlinked_Notice.setText(getResources().getString(R.string.explanation_email_unlinked));
                    mTv_Email_Unlinked_Notice.setTextColor(getResources().getColor(R.color.my_account_gray));
                    mBtn_Email_Continue.setVisibility(View.VISIBLE);
                    mBtn_Email_Continue.setText(getResources().getString(R.string.str_continue));
                    mTv_Email_Unlinked.setVisibility(View.GONE);
                    btn_edit_email.setVisibility(View.GONE);
                } else if (mBindState == EMAIL_BIND_UNVERIFI) { // 邮箱号绑定未验证
                    mTv_Title.setText(getResources().getString(R.string.email));
                    mLl_Title.setVisibility(View.VISIBLE);
                    mLl_Email_Unlinked.setVisibility(View.VISIBLE);
                    mEdt_Email_Unlinked.setVisibility(View.GONE);
                    mTv_Email_Unlinked.setVisibility(View.VISIBLE);
                    mTv_Email_Unlinked_Notice.setVisibility(View.VISIBLE);
                    mTv_Email_Unlinked_Notice.setText(getResources().getString(R.string.explanation_email_unverified));
                    mTv_Email_Unlinked_Notice.setTextColor(getResources().getColor(R.color.my_account_red));
                    mBtn_Email_Continue.setVisibility(View.VISIBLE);
                    mBtn_Email_Continue.setText(getResources().getString(R.string.resend_email));
                    btn_edit_email.setVisibility(View.VISIBLE);
                    btn_edit_email.setText(getResources().getString(R.string.eidt_email));
                } else if (mBindState == EMAIL_BIND_VERIFICODE) { // 邮箱号已绑定和验证
                    mTv_Title.setText(getResources().getString(R.string.email));
                    mLl_Title.setVisibility(View.VISIBLE);
                    mLl_Email_Unlinked.setVisibility(View.VISIBLE);
                    mEdt_Email_Unlinked.setVisibility(View.GONE);
                    mTv_Email_Unlinked.setVisibility(View.VISIBLE);
                    mTv_Email_Unlinked_Notice.setVisibility(View.VISIBLE);
                    mTv_Email_Unlinked_Notice.setText(getResources().getString(R.string.explanation_email_verified));
                    mTv_Email_Unlinked_Notice.setTextColor(getResources().getColor(R.color.my_account_gray));
                    mBtn_Email_Continue.setVisibility(View.GONE);
                    btn_edit_email.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                if (mBindState == PHONE_UNBIND) { // 手机号未绑定
                    if (NetworkUtils.isNetworkAvailable(context)) {
                        mTv_Title.setText(getResources().getString(R.string.phone));
                        mLl_Title.setVisibility(View.VISIBLE);
                        mLl_Phone_Unbind.setVisibility(View.VISIBLE);
                        mLl_Phone.setVisibility(View.GONE);
                        mLl_Verificode.setVisibility(View.VISIBLE);
                        mTv_PhoneNotice.setVisibility(View.GONE);
                        mTv_Veri_CityCode.setText(getCityCode1());
                        mTv_Veri_Phone.setText(getPhoneNumber1());

                        if (mVerifiTime == 0 || mVerifiTime == 60) {
                            mHandler.removeCallbacks(mRnnable);
                            mVerifiTime = 60;
                            mTv_Time.setVisibility(View.GONE);
                            showProgDialog(R.string.please_wait);
                            getSmsCode();
                        } else {
                            String timeStr = getString(R.string.register_verifi_notice);
                            timeStr = timeStr.replace("XXXX", mVerifiTime + "");
                            hideOrShowTime(true, timeStr);
                            mTv_Time.setClickable(false);

                        }


                        String verifiCode = getVerifiCode1();
                        if (TextUtils.isEmpty(verifiCode)) {
                            isClickBtnContinue(false);
                        } else {
                            isClickBtnContinue(true);
                        }
                    } else {
                        ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
                        mStep = 1;
                        changeUiByStep();
                    }

                } else if (mBindState == PHONE_UPDATE_BIND) { // 更改手机绑定

                } else if (mBindState == EMAIL_UNBIND) { // 邮箱未绑定
                    mTv_Title.setText(getResources().getString(R.string.email));
                    mLl_Title.setVisibility(View.VISIBLE);
                    mLl_Email_Unlinked.setVisibility(View.VISIBLE);
                    mEdt_Email_Unlinked.setVisibility(View.GONE);
                    mTv_Email_Unlinked.setVisibility(View.VISIBLE);
                    mTv_Email_Unlinked.setText(mEdt_Email_Unlinked.getText().toString().trim());
                    mTv_Email_Unlinked_Notice.setVisibility(View.VISIBLE);
                    mTv_Email_Unlinked_Notice.setText(getResources().getString(R.string.explanation_email_unverified));
                    mTv_Email_Unlinked_Notice.setTextColor(getResources().getColor(R.color.my_account_red));
                    mBtn_Email_Continue.setVisibility(View.VISIBLE);
                    mBtn_Email_Continue.setText(getResources().getString(R.string.resend_email));
                    btn_edit_email.setVisibility(View.VISIBLE);
                    btn_edit_email.setText(getResources().getString(R.string.eidt_email));
                } else if (mBindState == EMAIL_BIND_UNVERIFI) { // 邮箱号绑定未验证
                    mTv_Title.setText(getResources().getString(R.string.email));
                    mLl_Title.setVisibility(View.VISIBLE);
                    mLl_Email_Unlinked.setVisibility(View.VISIBLE);
                    mEdt_Email_Unlinked.setVisibility(View.VISIBLE);
                    mEdt_Email_Unlinked.setText(mStr_PreEmail);
                    mTv_Email_Unlinked_Notice.setVisibility(View.VISIBLE);
                    mTv_Email_Unlinked_Notice.setText(getResources().getString(R.string.explanation_email_modify));
                    mTv_Email_Unlinked_Notice.setTextColor(getResources().getColor(R.color.my_account_red));
                    mBtn_Email_Continue.setVisibility(View.VISIBLE);
                    mBtn_Email_Continue.setText(getResources().getString(R.string.str_continue));
                    mTv_Email_Unlinked.setVisibility(View.GONE);
                    btn_edit_email.setVisibility(View.GONE);
                } else if (mBindState == EMAIL_BIND_VERIFICODE) { // 邮箱号已绑定和验证
                    mTv_Title.setText(getResources().getString(R.string.email));
                    mLl_Title.setVisibility(View.VISIBLE);
                    mLl_Email_Unlinked.setVisibility(View.VISIBLE);
                    mEdt_Email_Unlinked.setVisibility(View.VISIBLE);
                    mTv_Email_Unlinked_Notice.setVisibility(View.VISIBLE);
                    mEdt_Email_Unlinked.setText(mStr_PreEmail);
                    mTv_Email_Unlinked_Notice.setText(getResources().getString(R.string.explanation_email_modify));
                    mTv_Email_Unlinked_Notice.setTextColor(getResources().getColor(R.color.my_account_red));
                    mBtn_Email_Continue.setVisibility(View.VISIBLE);
                    mBtn_Email_Continue.setText(getResources().getString(R.string.str_continue));
                    mTv_Email_Unlinked.setVisibility(View.GONE);
                    btn_edit_email.setVisibility(View.GONE);
                }
                break;
            case 3:
                if (mBindState == EMAIL_UNBIND) {
                    mTv_Title.setText(getResources().getString(R.string.email));
                    mLl_Title.setVisibility(View.VISIBLE);
                    mLl_Email_Unlinked.setVisibility(View.VISIBLE);
                    mEdt_Email_Unlinked.setVisibility(View.VISIBLE);
                    mTv_Email_Unlinked.setVisibility(View.GONE);
                    mTv_Email_Unlinked_Notice.setVisibility(View.VISIBLE);
                    mTv_Email_Unlinked_Notice.setText(getResources().getString(R.string.explanation_email_modify));
                    mTv_Email_Unlinked_Notice.setTextColor(getResources().getColor(R.color.my_account_red));
                    mBtn_Email_Continue.setVisibility(View.VISIBLE);
                    mBtn_Email_Continue.setText(getResources().getString(R.string.str_continue));
                    mTv_Email_Unlinked.setVisibility(View.GONE);
                    btn_edit_email.setVisibility(View.GONE);
                }
                break;

            default:
                break;
        }

    }

    private void nextStep() {
        switch (mStep) {
            case 1:
                if (mBindState == PHONE_UNBIND) { // 手机号未绑定
                    String phone = getPhoneNumber1();
                    String cityCode = getCityCode1();
                    if (phone.length() >= 8 && phone.length() <= 15) {
                        if (!mStr_PrePhoneNumber.equals(phone) || !mPreCityCode.equals(cityCode)) { // 当手机号改变时
                            mEdt_VeriCode.setText("");
                            mHandler.removeCallbacks(mRnnable);
                            mVerifiTime = 60;

                            mStr_PrePhoneNumber = phone;
                            mPreCityCode = cityCode;
                        }
                        showPhoneDialog();
                    } else { // 手机号无效
                        ToastManager.getInstance().show(this, R.string.invalid_number);
                    }
                } else if (mBindState == PHONE_UPDATE_BIND) { // 更改手机绑定

                } else if (mBindState == EMAIL_UNBIND) { // 邮箱未绑定
                    if (mStep == 1) {
                        String email = mEdt_Email_Unlinked.getText().toString().trim();
                        if (null != email) {
                            if (!CommonUtils.isEmail(email)) {
                                ToastManager.getInstance().show(this, R.string.invalid_email_address);
                            } else {
                                sendEmailReq();
                            }
                        }
                    }

                } else if (mBindState == EMAIL_BIND_UNVERIFI) { // 邮箱号绑定未验证
                    sendEmailReq();

                } else if (mBindState == EMAIL_BIND_VERIFICODE) { // 邮箱号已绑定和验证

                }
                break;
            case 2:
                if (mBindState == PHONE_UNBIND) { // 手机号未绑定
                    submitData();
                } else if (mBindState == PHONE_UPDATE_BIND) { // 更改手机绑定

                } else if (mBindState == EMAIL_UNBIND) { // 修改邮箱
                    sendEmailReq();

                } else if (mBindState == EMAIL_BIND_UNVERIFI) { // 邮箱号绑定未验证
                    if (mStr_PreEmail != null) {
                        if (mStr_PreEmail.equals(getEmail())) {
                            ToastManager.getInstance().show(this, R.string.same_email_address);
                        } else if (!CommonUtils.isEmail(getEmail())) {
                            ToastManager.getInstance().show(this, R.string.prompt_email_address_not_legal);
                        } else {
                            showDialogEx(R.string.tips_changed_email);
                        }
                    }
                } else if (mBindState == EMAIL_BIND_VERIFICODE) { // 邮箱号已绑定和验证
                    if (mStr_PreEmail != null) {
                        if (mStr_PreEmail.equals(getEmail())) {
                            ToastManager.getInstance().show(this, R.string.same_email_address);
                        } else if (!CommonUtils.isEmail(getEmail())) {
                            ToastManager.getInstance().show(this, R.string.prompt_email_address_not_legal);
                        } else {
                            showDialogEx(R.string.tips_changed_email);
                        }
                    }
                }
                break;
            case 3:
                if (mBindState == EMAIL_UNBIND) {
                    if (mStr_PreEmail != null) {
                        if (mStr_PreEmail.equals(getEmail())) {
                            ToastManager.getInstance().show(this, R.string.same_email_address);
                        } else {
                            showDialogEx(R.string.tips_changed_email);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取手机号
     *
     * @return
     */
    private String getPhoneNumber1() {
        return mEdt_Phone.getText().toString().trim();
    }

    /**
     * 获取验证码
     *
     * @return
     */
    private String getVerifiCode1() {
        return mEdt_VeriCode.getText().toString().trim();
    }

    private String getCityCode1() {
        return mTv_CityCode.getText().toString().trim();
    }

    private String getEmail() {
        if (mBindState == EMAIL_UNBIND) {
            if (mStep == 1) {
                return mEdt_Email_Unlinked.getText().toString().trim();
            } else if (mStep == 2) {
                return mTv_Email_Unlinked.getText().toString().trim();
            } else if (mStep == 3) {
                return mEdt_Email_Unlinked.getText().toString().trim();
            }
        } else if (mBindState == EMAIL_BIND_UNVERIFI) {
            if (mStep == 1) {
                return mTv_Email_Unlinked.getText().toString().trim();
            } else if (mStep == 2) {
                return mEdt_Email_Unlinked.getText().toString().trim();
            }
        } else if (mBindState == EMAIL_BIND_VERIFICODE) {
            if (mStep == 1) {
                return mTv_Email_Unlinked.getText().toString().trim();
            } else if (mStep == 2) {
                return mEdt_Email_Unlinked.getText().toString().trim();
            }
        }
        return null;
    }

    /*
     * 是否可点击继续按钮
     *
     * @param bool
     */
    private void isClickBtnContinue(boolean bool) {
        if (bool) {
            mBtn_Continue.setBackgroundResource(R.drawable.login_button_selector);
            mBtn_Continue.setTextColor(Color.WHITE);
            mBtn_Continue.setClickable(true);
        } else {
            mBtn_Continue.setBackgroundResource(R.drawable.btn_blue_d);
            mBtn_Continue.setTextColor(getResources().getColor(R.color.guide_text_color));
            mBtn_Continue.setClickable(false);
        }
    }

    /**
     * 显示或隐藏“提示文字信息”
     */
    private void hideOrShowTime(boolean isShow, String text) {
        if (isShow) {
            mTv_Time.setVisibility(View.VISIBLE);
            mTv_Time.setText(text);
        } else
            mTv_Time.setVisibility(View.GONE);
    }

    /**
     * 手机号码确认框
     */
    private void showPhoneDialog() {
        String countryCode = getCityCode1();
        String phoneNumber = getPhoneNumber1();
        AppDialog appDialog = new AppDialog(this);
        appDialog.createChangePhoneDialog(MyAccountInfoActivity.this, getString(R.string.sms_code_will_sent), countryCode, phoneNumber, getString(R.string.edit_phone_num), getString(R.string.ok), new OnConfirmButtonDialogListener() {

            @Override
            public void onLeftButtonClick() {
                if (mIsResendSmsCode)
                    mIsResendSmsCode = false;
                mStep = 1;
                changeUiByStep();
            }

            @Override
            public void onRightButtonClick() {
                mStep = 2;
                changeUiByStep();
            }

        });
        appDialog.show();
    }

    /**
     * 手机号码确认框
     */
    private void showEidtPhoneDialog() {
        String countryCode = getCityCode1();
        String phoneNumber = getPhoneNumber1();
        final AppDialog appDialog = new AppDialog(this);
        appDialog.createChangePhoneDialog(MyAccountInfoActivity.this, getString(R.string.edit_your_phone_number), countryCode, phoneNumber, getString(R.string.cancel), getString(R.string.ok), new OnConfirmButtonDialogListener() {

            @Override
            public void onLeftButtonClick() {
                if (mIsResendSmsCode)
                    mIsResendSmsCode = false;
                appDialog.dismiss();
            }

            @Override
            public void onRightButtonClick() {
                mIsResendSmsCode = false;
                mStep = 1;
                changeUiByStep();
            }

        });
        appDialog.show();
    }

    /**
     * sms code等待认证goback提示提示框
     */
    private void showGoBackDialog() {
        AppDialog appDialog = new AppDialog(this);
        appDialog.createConfirmDialog(context, "", getString(R.string.sms_code_back_wait), getString(R.string.back), getString(R.string.wait), new OnConfirmButtonDialogListener() {

            @Override
            public void onLeftButtonClick() {
                if (mBindState == PHONE_UNBIND && mStep == 2) {
                    mStep = 1;
                    changeUiByStep();
                }

            }

            @Override
            public void onRightButtonClick() {

            }

        });
        appDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_country1:
                Intent intent = new Intent(this, CountryActivity.class);
                intent.putExtra(JsonConstant.KEY_COUNTRY, true);
                startActivityForResult(intent, DefaultValueConstant._86);
                break;
            case R.id.btn_continue:
                nextStep();
                break;
            case R.id.tv_time1:
                if (mStep == 2) {
                    mIsResendSmsCode = true;
                    showPhoneDialog();
                }
                break;
            case R.id.img_resend_verifi: // 绑定手机号验证码页面，重新发送验证码图片
                if (mStep == 2) {
                    mIsResendSmsCode = true;
                    showEidtPhoneDialog();
                }
                break;
            case R.id.back_button:
                onBack();
                break;
            case R.id.btn_update_phone_bind: // 更改手机号绑定
                Intent it = new Intent(this, ChangePhoneActivity.class);
                startActivityForResult(it, SET_SUCCESS_PHONE);
                break;
            case R.id.btn_email_continue:
                nextStep();
                break;
            case R.id.btn_edit_email:
                if (mBindState == EMAIL_UNBIND && mStep == 2) {
                    mStr_PreEmail = getEmail();
                    mStep = 3;
                    changeUiByStep();
                }

                if (mBindState == EMAIL_BIND_UNVERIFI) {
                    if (mStep == 1) {
                        mStr_PreEmail = getEmail();
                        mStep = 2;
                        changeUiByStep();
                    }
                }

                if (mBindState == EMAIL_BIND_VERIFICODE) {
                    if (mStep == 1) {
                        mStr_PreEmail = getEmail();
                        mStep = 2;
                        changeUiByStep();
                    }
                }

                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DefaultValueConstant._86) {
            if (data != null) {
                String temp1 = null, temp2 = null;
                temp1 = data.getStringExtra(JsonConstant.KEY_COUNTRY);
                temp2 = data.getStringExtra(JsonConstant.KEY_COUNTRY_CODE);
                if (!TextUtils.isEmpty(temp1) && !TextUtils.isEmpty(temp2)) {
                    mTv_CityCode.setText(temp2);
                    mTv_Country.setText(temp1);
                }

            }
        } else if (requestCode == SET_SUCCESS_PHONE) {
            if (data != null) {
                Intent intent = new Intent(this, MyAccountActivity.class);
                setResult(8001, intent);
                MyActivityManager.getScreenManager().popActivity();
            }
        }
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        if (code == Consts.REQ_CODE_SUCCESS) {
            dismissProgressDialog();
            dismissDialog();
            switch (flag) {
                case Consts.REQ_GET_SMS_CODE_AFTER_LOGIN: // 获取验证码成功

                    if (mIsResendSmsCode) {
                        ToastManager.getInstance().show(context, R.string.text_random_promt);
                        mIsResendSmsCode = false;
                    }
                    mHandler.postDelayed(mRnnable, 1000);
                    PalmchatLogUtils.println("REQ_GET_SMS_CODE result  " + result);

                    break;
                case Consts.REQ_CHECK_ACCOUNT_BY_PHONE_EX: { // 检验手机号是否绑定
                    boolean is_exist = null != result;
                    System.out.println("ywp: AfOnResult: check account result  = " + is_exist);
                    if (is_exist) {// 已绑定
                        dismissProgressDialog();
                        showOKDialog(R.string.phone_has_been_binded);
                    } else { // 未绑定
                        toBindPhone();
                    }
                    break;
                }
                case Consts.REQ_BIND_BY_PHONE_EX: {

                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SET_BP_SUCC);
//				MobclickAgent.onEvent(context, ReadyConfigXML.SET_BP_SUCC);
                    dismissDialog();
                    AfProfileInfo profile = CacheManager.getInstance().getMyProfile();
                    profile.is_bind_phone = true;
                    profile.phone = getPhoneNumber1();
                    profile.phone_cc = bindPhoneCC;


//				CacheManager.getInstance().setMyProfile(profile);
                    updateProfileToDB();

                    AppDialog appDialog = new AppDialog(context);
                    appDialog.createSuccessDialog(context, "", getString(R.string.verify_success), new OnConfirmButtonDialogListener() {

                        @Override
                        public void onRightButtonClick() {
                            finish();
                        }

                        @Override
                        public void onLeftButtonClick() {

                        }
                    });
                    appDialog.show();
                    break;
                }
                case Consts.REQ_BIND_BY_EMAIL: {

                    // heguiming 2013-12-04
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SET_AE_SUCC);
//				MobclickAgent.onEvent(context, ReadyConfigXML.SET_AE_SUCC);

                    dismissProgressDialog();
                    String des = (String) result;
                    System.out.println("ywp: AfOnResult: REQ_BIND_BY_EMAIL descrip =  " + des);
                    AfProfileInfo profile = CacheManager.getInstance().getMyProfile();
                    profile.is_bind_email = false;
                    profile.email = getEmail();

                    CacheManager.getInstance().setMyProfile(profile);
                    showOKDialog(R.string.tips_send_email);
                    updateProfileToDB();
                    break;
                }
                default:
                    break;
            }
        } else {
            dismissProgressDialog();
            dismissDialog();
            if (code == Consts.REQ_CODE_SMS_CODE_ALREADY_BINDED) {
                showOKDialog(R.string.number_bind);
            } else if (code == Consts.REQ_CODE_SMS_CODE_NOT_REGISTER_OR_BIND) {
                showOKDialog(R.string.sms_code_not_register_or_bind);
            } else if (code == Consts.REQ_CODE_SMS_CODE_LIMIT) {
                showOKDialog(R.string.verification_code_count_tips);
            } else if (code == Consts.REQ_CODE_PHONE_OR_EMAIL_ALREADY_BINDED) {
                showOKDialog(R.string.email_binded);
            } else {
                Consts.getInstance().showToast(context, code, flag, http_code);
            }
        }
    }

    Runnable mRnnable = new Runnable() {
        public void run() {
            if (mBln_KeyBack) {
                mHandler.removeCallbacks(this);
                return;
            }
            String timeStr = getString(R.string.register_verifi_notice);
            timeStr = timeStr.replace("XXXX", mVerifiTime + "");
            if (mStep == 2)
                hideOrShowTime(true, timeStr);
            mVerifiTime--;

            if (mVerifiTime > 0) {
                mHandler.postDelayed(this, 1000);
                mTv_Time.setClickable(false);
            } else {
                mVerifiTime = 60;
                String verifiCode = getVerifiCode1();
//                if (CommonUtils.isEmpty(verifiCode)) { // 如果60s结束后未得到验证码，提示重新去获取
                mTv_Time.setText(getString(R.string.no_verification_received));
                mTv_Time.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                mTv_Time.setVisibility(View.VISIBLE);
                mTv_Time.setClickable(true);
//                } else {
//                    mTv_Time.setVisibility(View.GONE);
//                }

            }
        }
    };

    /**
     * 获取短信验证码
     */
    private void getSmsCode() {
        mAfCorePalmchat.AfHttpGetSMSCode(Consts.REQ_GET_SMS_CODE_AFTER_LOGIN, CommonUtils.getRealCountryCode(getCityCode1()), getPhoneNumber1(), Consts.SMS_CODE_TYPE_BIND, 0, null, this);
    }

    /**
     * 绑定手机号
     */
    private void toBindPhone() {
        bindPhoneCC = CommonUtils.getRealCountryCode(getCityCode1());
        mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_BIND_BY_PHONE_EX, getPhoneNumber1(), bindPhoneCC, CacheManager.getInstance().getMyProfile().afId, null, getVerifiCode1(), 0, this);//
    }

    private String bindPhoneCC;

    /**
     * 进度条对话框
     *
     * @param resId
     */
    private void showProgDialog(int resId) {
        if (mDlg_Progress == null) {
            mDlg_Progress = new Dialog(this, R.style.Theme_LargeDialog);
            mDlg_Progress.setOnKeyListener(this);
            mDlg_Progress.setCanceledOnTouchOutside(false);
            mDlg_Progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDlg_Progress.setContentView(R.layout.dialog_loading);
            ((TextView) mDlg_Progress.findViewById(R.id.textview_tips)).setText(resId);
        }
        mDlg_Progress.show();
    }

    private void dismissDialog() {
        if (null != mDlg_Progress && mDlg_Progress.isShowing()) {
            try {
                mDlg_Progress.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 按返回按钮时 返回到上一页面
     */
    private void onBack() {
        if (mBindState == PHONE_UNBIND) {
            if (mStep == 1) {
                exit();
            } else if (mStep == 2) {
                showGoBackDialog();
            }

        } else if (mBindState == EMAIL_UNBIND) {
            if (mStep == 2) {
                mStep = 1;
                changeUiByStep();
            } else if (mStep == 3) {
                mStep = 2;
                changeUiByStep();
            } else {
                exit();
            }
        } else if (mBindState == EMAIL_BIND_UNVERIFI) {
            if (mStep == 2) {
                mStep = 1;
                changeUiByStep();
            } else if (mStep == 1) {
                exit();
            }
        } else if (mBindState == EMAIL_BIND_VERIFICODE) {
            if (mStep == 1) {
                exit();
            } else if (mStep == 2) {
                mStep = 1;
                changeUiByStep();
            }
        } else {
            exit();
        }

    }

    /**
     * 提交数据到服务器
     */
    private void submitData() {
        showProgressDialog(R.string.verifyloading);
        mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_CHECK_ACCOUNT_BY_PHONE_EX, getPhoneNumber1(), CommonUtils.getRealCountryCode(getCityCode1()), null, null, null, RegistrationActivity.BIND, this);
    }

    /**
     * 退出当前activity
     */
    private void exit() {
        mBln_KeyBack = true;
        mAfCorePalmchat.AfHttpRemoveAllListener();
        finish();

    }

    private void showOKDialog(final int code) {
        String str = getString(code);

        if (code == R.string.tips_send_email && str != null) {
            str = str.replace(ReplaceConstant.TARGET_EMAIL, getEmail());
//			StringBuilder sb = new StringBuilder(str);
//			int pos = str.indexOf(ReplaceConstant.TARGET_EMAIL);
//			sb.deleteCharAt(pos);
//			sb.insert(pos, getEmail());
//			str = sb.toString();
//			sb = null;
        }
        AppDialog appDialog = new AppDialog(this);
        appDialog.createOKDialog(context, str, new OnConfirmButtonDialogListener() {

            @Override
            public void onRightButtonClick() {
                if (code == R.string.phone_has_been_binded) {
                    if (mBindState == PHONE_UNBIND && mStep == 2) {
                        mStep = 1;
                        changeUiByStep();
                    }

                } else if (code == R.string.verification_code_count_tips) {
                    if (mBindState == PHONE_UNBIND && mStep == 2) {
                        mTv_Time.setVisibility(View.GONE);
                    }
                } else if (code == R.string.tips_send_email) {
                    if (mBindState == EMAIL_UNBIND && mStep == 1) {

                        mStep = 2;
                        changeUiByStep();

                    } else if (mBindState == EMAIL_UNBIND && mStep == 2) {

                    } else if (mBindState == EMAIL_BIND_UNVERIFI && mStep == 1) {

                    } else {
                        exit();
                    }

                } else if (code == R.string.email_binded) { // 邮箱已经被绑定
                    if (mBindState == EMAIL_BIND_UNVERIFI && mStep == 2) {
                        mStep = 1;
                        changeUiByStep();
                    } else if (mBindState == EMAIL_UNBIND && mStep == 3) {
                        mStep = 2;
                        changeUiByStep();
                    } else if (mBindState == EMAIL_BIND_VERIFICODE && mStep == 2) {
                        mStep = 1;
                        changeUiByStep();
                    }
                } else if (code == R.string.number_bind) { // 手机号已绑定到其他ID
                    mStep = 1;
                    changeUiByStep();
                } else {
                    exit();
                }

            }

            @Override
            public void onLeftButtonClick() {

            }
        });

        appDialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                if (code == R.string.number_bind) { // 手机号已绑定到其他ID
                    mStep = 1;
                    changeUiByStep();
                }
                // if (code == Consts.REQ_CODE_SMS_CODE_LIMIT) {
                // finish();
                // } else {
                // mStep = 1;
                // changeUiByStep();
                // }

            }
        });
        appDialog.show();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    /**
     * update profile to db
     */
    private void updateProfileToDB() {
        AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
        if (!CommonUtils.isEmpty(myAfProfileInfo.afId)) {
            new Thread(new Runnable() {
                public void run() {
                    int msg_id = mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, CacheManager.getInstance().getMyProfile());
                    PalmchatLogUtils.println("PalmchatApp  msg_id  " + msg_id);
                }
            }).start();
        }
    }

    private void sendEmailReq() {
        CommonUtils.closeSoftKeyBoard(mEdt_Email_Unlinked);
        showProgressDialog(R.string.please_wait);
        mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_BIND_BY_EMAIL, getEmail(), PalmchatApp.getOsInfo().getCountryCode(), Consts.HTTP_PARAMS_TYPE_TRUE, null, null, 0, this);
    }

    private void showDialogEx(final int resId) {

        AppDialog appDialog = new AppDialog(this);
        String conetentStr = null;
        if (resId == R.string.tips_changed_email) { // 修改邮箱
            conetentStr = this.getString(R.string.tips_changed_email);
        }

        CommonUtils.closeSoftKeyBoard(mEdt_Email_Unlinked);
        appDialog.createConfirmDialog(this, conetentStr, new OnConfirmButtonDialogListener() {
            @Override
            public void onRightButtonClick() {
                if (mBindState == EMAIL_UNBIND) {
                    if (mStep == 3) {
                        sendEmailReq();
                    }
                }
                if (mBindState == EMAIL_BIND_UNVERIFI) {
                    if (mStep == 2) {
                        sendEmailReq();
                    }
                }

                if (mBindState == EMAIL_BIND_VERIFICODE) {
                    if (mStep == 2) {
                        sendEmailReq();
                    }
                }
            }

            @Override
            public void onLeftButtonClick() {
                CommonUtils.closeSoftKeyBoard(mEdt_Email_Unlinked);
            }
        });
        try {
            appDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch blockf
            e.printStackTrace();
        }
    }

}
