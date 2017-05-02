package com.afmobi.palmchat.ui.activity.register;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseForgroundLoginActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.login.FacebookCompleteProfileActivity;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.profile.ComfireProfileActivity;
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

import java.util.Calendar;

/**
 * Created by tony on 2016/8/31.
 */
public class RegisteredByEmailActivity extends BaseForgroundLoginActivity implements View.OnClickListener, AfHttpResultListener {
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

    /*手机注册链接*/
    private TextView tv_reg_by_phone;
    /*使用已有账号登录链接*/
    private TextView tv_alredy_account;
    /*help链接*/
    private ImageView img_help;

    /*邮箱填写*/
    private EditText edt_email;
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

    /*当前步骤常量*/
    private final int EMAIL = 1;
    private final int PASSWORD = 2;
    private final int NAME = 3;
    private final int BIRTHDAY = 4;
    private final int STATE = 5;
    private final int GENDER = 6;

    /*当前是哪个步骤*/
    private int step = EMAIL;
    /*中间件接口*/
    private AfPalmchat mAfCorePalmchat;
    /*记录输入的邮箱*/
    private String preEmail = "";
    /*记录日期对话框中的日期 */
    private int year, monthOfYear, dayOfMonth;
    /* 名字是否是非法词汇*/
    private boolean INAPPROPRIATE_WORD = false;
    /*记录之前选中的国家*/
    private String preCountry = "";
    /*当前选择的地区和城市*/
    private String mState, mCity;
    /*国码*/
    private String country_code = "";
    /* 性别 0 : male 1: female */
    private byte gender = -1;
    private static final int ACTION_REGION = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void findViews() {
        setContentView(R.layout.activity_registered_by_email);

        img_left_back = (ImageView) findViewById(R.id.img_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_getstart = (TextView) findViewById(R.id.tv_getstart);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        tv_facebook = (TextView) findViewById(R.id.tv_facebook);

        tv_reg_by_phone = (TextView) findViewById(R.id.tv_reg_by_phone);
        tv_alredy_account = (TextView) findViewById(R.id.tv_alredy_account);
        img_help = (ImageView) findViewById(R.id.img_help);

        edt_email = (EditText) findViewById(R.id.edt_email);
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

        img_left_back.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        tv_state_city.setOnClickListener(this);
        tv_state_country.setOnClickListener(this);
        tv_female.setOnClickListener(this);
        tv_male.setOnClickListener(this);
        tv_reg_by_phone.setOnClickListener(this);

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
                isClickBtnContinue(str.length());
                hideOrShowNotice(str.length(), getResources().getString(R.string.register_email_notice));
            }
        });
    }

    @Override
    public void init() {
        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        initCountry();
        step = EMAIL;
        changeUiByStep();

    }

    /**
     * 初始化国家
     */
    private void initCountry() {
        String[] str = CommonUtils.getCountryAndCode(this);
        tv_state_country.setText(str[0]);
    }

    /**
     * 是否可点击继续按钮
     *
     * @param length
     */
    private void isClickBtnContinue(int length) {
        if (length > 0) {
            btn_continue.setBackgroundResource(R.drawable.login_button_selector);
            btn_continue.setTextColor(Color.WHITE);
            btn_continue.setClickable(true);
        } else {
            btn_continue.setBackgroundResource(R.drawable.btn_blue_d);
            btn_continue.setTextColor(getResources().getColor(R.color.guide_text_color));
            btn_continue.setClickable(false);
        }
    }

    /**
     * 显示或隐藏“提示文字信息”
     */
    private void hideOrShowNotice(int length, String text) {
        if (length > 0) {
            tv_notice.setVisibility(View.VISIBLE);
            tv_notice.setText(text);
        } else
            tv_notice.setVisibility(View.GONE);
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
     * 初始化日期控件为当前时间向后推六年
     */
    private void initDate() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR) - 6;
        monthOfYear = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
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
     * 获取密码
     *
     * @return
     */
    private String getPassword() {
        return edt_pwd.getText().toString().trim();
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
     * 获取生日
     *
     * @return
     */
    private String getBirthday() {
        return tv_birthday.getText().toString().trim();
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


    /* 初始化日期控件 */
    private void initTimePicker(boolean isShow) {

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
                    RegisteredByEmailActivity.this.year = year;
                    RegisteredByEmailActivity.this.monthOfYear = monthOfYear;
                    RegisteredByEmailActivity.this.dayOfMonth = dayOfMonth;

                    afProfileInfo.birth = formatedDate;
                    tv_birthday.setText(formatedDate);
                    isClickBtnContinue(getBirthday().length());
                } else {
                    ToastManager.getInstance().show(context, R.string.valide_birthday);
                }

            }
        }, year, monthOfYear, dayOfMonth, c.getTimeInMillis());

        if (isShow)
            pickerDialog.show();

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
     * 邮箱注册
     */
    private void registeByEmail() {

        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.W_EMAIL_N);
        showProgDialog(R.string.verifyloading);

        AfRegInfoParam mParam = new AfRegInfoParam();
        mParam.cc = CommonUtils.getRealCountryCode(country_code);
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
        mParam.country = getStateCountry();
        mParam.phone_or_email = getEmail();
        mAfCorePalmchat.AfHttpRegister(mParam, Consts.REQ_REG_BY_EMAIL, 0, this);
    }

    /**
     * 用palmID登录
     *
     * @param afid
     * @param pass
     * @param userdata
     */
    private void login(String afid, String pass, String userdata) {
        mAfCorePalmchat.AfHttpLogin(afid, pass, CommonUtils.getRealCountryCode(country_code), Consts.AF_LOGIN_AFID, userdata, this);
    }

    /**
     * 用已经存在的手机号或邮箱登录
     */
    private void toLogin() {
        dismissProgressDialog();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(JsonConstant.KEY_MODE, LoginActivity.EMAIL);
        intent.putExtra(JsonConstant.KEY_EMAIL, getEmail());
        intent.putExtra(JsonConstant.KEY_IS_REG_BY_EMAIL, true);
        intent.putExtra(JsonConstant.KEY_IS_SHOW_PHONE_NUMBER, false);
        startActivity(intent);
        finish();
    }

    /**
     * 根据当前步骤刷新页面
     */
    private void changeUiByStep() {
        switch (step) {
            case EMAIL: //邮箱
                tv_getstart.setText(getResources().getString(R.string.getstarted));
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setVisibility(View.VISIBLE);
                edt_email.setVisibility(View.VISIBLE);
                getFocus(edt_email);
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                tv_facebook.setVisibility(View.VISIBLE);
                tv_reg_by_phone.setVisibility(View.VISIBLE);


                String email = getEmail();
                if (!TextUtils.isEmpty(email)) {
                    isClickBtnContinue(email.length());
                    hideOrShowNotice(email.length(), getResources().getString(R.string.register_email_notice));
                } else {
                    isClickBtnContinue(0);
                    hideOrShowNotice(0, null);
                }


                break;
            case PASSWORD:
                tv_getstart.setText(getResources().getString(R.string.create_password));
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setVisibility(View.VISIBLE);
                edt_pwd.setVisibility(View.VISIBLE);
                getFocus(edt_pwd);
                edt_email.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                tv_facebook.setVisibility(View.GONE);

                tv_reg_by_phone.setVisibility(View.GONE);

                String pwd = getPassword();
                if (pwd != null && pwd.length() > 0) {
                    isClickBtnContinue(pwd.length());
                    hideOrShowNotice(pwd.length(), getResources().getString(R.string.register_pwd_notice));
                } else {
                    isClickBtnContinue(0);
                    hideOrShowNotice(0, null);
                }

                break;
            case NAME:
                tv_getstart.setText(getResources().getString(R.string.what_your_name));
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setVisibility(View.VISIBLE);
                edt_name.setVisibility(View.VISIBLE);
                getFocus(edt_name);
                edt_email.setVisibility(View.GONE);
                edt_pwd.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                tv_facebook.setVisibility(View.GONE);

                tv_reg_by_phone.setVisibility(View.GONE);

                String name = getName();
                if (name != null && name.length() > 0) {
                    isClickBtnContinue(name.length());
                    hideOrShowNotice(name.length(), getResources().getString(R.string.register_name_notice));
                    if (INAPPROPRIATE_WORD) { // 非法词汇
                        INAPPROPRIATE_WORD = false;
                        tv_notice.setText(getString(R.string.inappropriate_word));
                        tv_notice.setTextColor(getResources().getColor(R.color.public_group_disband_color)); // 设置字体为红色
                    }
                } else {
                    isClickBtnContinue(0);
                    hideOrShowNotice(0, null);
                }

                break;
            case BIRTHDAY:
                tv_getstart.setText(getResources().getString(R.string.when_your_birthday));
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setVisibility(View.VISIBLE);
                ll_birthday.setVisibility(View.VISIBLE);
                edt_email.setVisibility(View.GONE);
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                tv_facebook.setVisibility(View.GONE);

                tv_reg_by_phone.setVisibility(View.GONE);
                hideOrShowNotice(0, null);


                String birthday = getBirthday();
                if (CommonUtils.isEmpty(birthday)) { // 默认为当前日期往后推六年
                    isClickBtnContinue(0);
                    initTimePicker(false);
                } else {
                    isClickBtnContinue(birthday.length());
                }

                break;
            case STATE:
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setText(getResources().getString(R.string.which_your_state));
                tv_getstart.setVisibility(View.VISIBLE);
                ll_state.setVisibility(View.VISIBLE);
                edt_email.setVisibility(View.GONE);
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_gender.setVisibility(View.GONE);
                tv_facebook.setVisibility(View.GONE);

                tv_reg_by_phone.setVisibility(View.GONE);

                isClickBtnContinue(1);
                hideOrShowNotice(0, null);
                break;
            case GENDER:
                tv_getstart.setTextColor(getResources().getColor(R.color.invite_friend_phonenum));
                tv_getstart.setText(getResources().getString(R.string.what_your_gender));
                tv_getstart.setVisibility(View.VISIBLE);
                ll_gender.setVisibility(View.VISIBLE);
                CommonUtils.closeSoftKeyBoard(ll_gender);
                edt_email.setVisibility(View.GONE);
                edt_pwd.setVisibility(View.GONE);
                edt_name.setVisibility(View.GONE);
                ll_birthday.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
                tv_facebook.setVisibility(View.GONE);

                tv_reg_by_phone.setVisibility(View.GONE);

                btn_continue.setVisibility(View.GONE);
                hideOrShowNotice(0, null);

                break;

        }
    }


    /**
     * 下一步操作事件
     */
    private void nextStep() {
        switch (step) {
            case EMAIL:
                String mAccount = getEmail();
                if (!CommonUtils.isEmail(mAccount)) {
                    ToastManager.getInstance().show(this, R.string.prompt_email_address_not_legal);
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.EMAIL_ERROR);

                } else if (mAccount.length() >= 7 && mAccount.length() < 100) {
                    if (!preEmail.equals(getEmail())) { // 当邮箱号改变时
                        edt_pwd.setText("");
                        edt_name.setText("");
                        tv_birthday.setText("");
                        initCountry();
                        tv_state_city.setText("");
                        preEmail = getEmail();
                        initDate();
                    }
                    step = PASSWORD;
                    changeUiByStep(); // 切换到密码输入页面
                } else {
                    ToastManager.getInstance().show(this, R.string.prompt_email_address_not_legal);
                }


                break;
            case PASSWORD:
                String pwd = getPassword();
                if (pwd.length() < 6 || pwd.length() > 16) {
                    ToastManager.getInstance().show(this, R.string.prompt_input_password_little6);
                    return;
                } else {
                    step = NAME;
                    changeUiByStep(); // 切换到密码输入页面
                }
                break;
            case NAME:
                String name = getName();
                if (name.length() > 0) {
                    step = BIRTHDAY;
                    changeUiByStep();
                }
                break;
            case BIRTHDAY:
                String birthday = getBirthday();
                if (birthday.length() > 0) {
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


        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }


    /**
     * 回退事件处理
     */
    private void onBack() {
        switch (step) {
            case EMAIL:
                mAfCorePalmchat.AfHttpRemoveAllListener();
                finish();
                break;
            case PASSWORD:
                step = EMAIL;
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


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue: //继续按钮
                nextStep();
                break;
            case R.id.tv_state_city:// 地区选择
                Intent intent1 = new Intent(this, RegionTwoActivity.class);
                intent1.putExtra(JsonConstant.KEY_COUNTRY, getStateCountry());
                intent1.putExtra(JsonConstant.KEY_FLAG, false);
                startActivityForResult(intent1, ACTION_REGION);
                break;
            case R.id.tv_state_country: // 国家
                preCountry = getStateCountry();
                startActivityForResult(new Intent(RegisteredByEmailActivity.this, CountryActivity.class), DefaultValueConstant.RESULT_10);

                break;
            case R.id.tv_female: // 女性
                // gtf 205-5-22
                new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.EMAIL_SEL_FEMALE);
                gender = 1;
                registeByEmail();


                break;
            case R.id.tv_male: // 男性
                gender = 0;
                // gtf 205-5-22
                new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.EMAIL_SEL_MALE);
                registeByEmail();

                break;
            case R.id.tv_reg_by_phone: // 使用手机号注册
                startActivity(new Intent(RegisteredByEmailActivity.this, RegisteredByPhoneActivity.class));
                finish();
                break;
            case R.id.img_left: //返回按钮
                onBack();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_REGION || requestCode == DefaultValueConstant.RESULT_10) {//选了国家 省市
            if (data == null) {
                if (preCountry != null && !preCountry.equals(getStateCountry())) {
                    tv_state_city.setText("");
                }
                return;
            }
            String strCountry = data.getStringExtra(JsonConstant.KEY_COUNTRY);
            country_code = data.getStringExtra(JsonConstant.KEY_COUNTRY_CODE);
            if (!TextUtils.isEmpty(strCountry)) {
                tv_state_country.setText(strCountry);
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
                case Consts.REQ_REG_BY_EMAIL: { //邮箱注册成功
                    new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.REG_E_SUCC);
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LG_P_SUCC);

                    AfProfileInfo info = CacheManager.getInstance().getMyProfile();
                    String mRegisterAfid = (String) result;
                    info.afId = mRegisterAfid;
                    String pass = getPassword();
                    if (mRegisterAfid != null) {
                        login(mRegisterAfid, pass, null);
                    }

                    System.out.println("ywp: AfOnResult: REQ_REG_BY_PHONE afid  = " + mRegisterAfid);
                    break;
                }
                case Consts.REQ_FLAG_LOGIN: { // 用palmID登录成功后
                    dismissProgressDialog();
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

                    CacheManager.getInstance().setPalmGuessShow(myProfile.palmguess_flag == 1);
                    CacheManager.getInstance().setRecharge_intro_url(myProfile.recharge_intro_url); // zhh设置充值方式说明的url

                    PalmchatApp.getApplication().mAfCorePalmchat.AfHttpStatistic(false, true, new ReadyConfigXML().getNoLoginHttpJsonStr(), null, null);

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
        } else { //请求失败
            if (flag == Consts.REQ_REG_BY_EMAIL) { // 邮箱注册失败
                if (code == Consts.REQ_CODE_USER_ALREADY_REGISTER) { // 邮箱已经注册
                    ToastManager.getInstance().show(this, R.string.email_has_registered);
                    toLogin();
                } else if (code == Consts.REQ_CODE_ILLEGAL_WORD) { // zhh相关语句中含有非法词汇
                    Consts.getInstance().showToast(this, code, flag, http_code);
                    INAPPROPRIATE_WORD = true;
                    step = NAME;
                    changeUiByStep();
                } else {
                    Consts.getInstance().showToast(this, code, flag, http_code);
                }
            } else {
                Consts.getInstance().showToast(this, code, flag, http_code);
            }

        }
    }

}
