package com.afmobi.palmchat.ui.activity.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.login.SignUpOrLoginActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfLoginInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

public class ResetActivity extends BaseActivity implements OnClickListener, AfHttpResultListener, OnConfirmButtonDialogListener {

    private TextView vTextViewDetail;
    private Button vBtnComfirm;
    private Button vBtnSkip;
    private EditText vEditTextNewPassword;
    private String mPassword;
    private String mNewPassword;
    private AfPalmchat mAfCorePalmchat;

    private RelativeLayout vRelativelayoutPrompt;
    private LinearLayout vLinearlayoutVerifyCode, vLinearLayoutResendCode, vLinearLayoutOK;
    private String countryCode, phoneNum;

    private boolean isSuccess = false;

    private EditText vEditPhone, vEditTextPassword, vEditTextCode;
    private TextView vTextViewCountryCode;
    private Button vButtonNext, vButtonResend;

    private View view;
    private TextView vTitleText;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart("ResetActivity");
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd("ResetActivity");
//        MobclickAgent.onPause(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confim_button: {
                mNewPassword = vEditTextNewPassword.getText().toString();
                if (mNewPassword != null && mNewPassword.length() == DefaultValueConstant.LENGTH_0) {
                    ToastManager.getInstance().show(this, getString(R.string.enter_new_password));
                } else if (mNewPassword != null && mNewPassword.length() < DefaultValueConstant.LENGTH_6) {
                    ToastManager.getInstance().show(this, getString(R.string.prompt_input_password_little6));
                } else {
                    if (mNewPassword != null && mNewPassword.length() >= DefaultValueConstant.LENGTH_6) {
                        showProgressDialog(R.string.please_wait);
                        resetPass();
//					toChangePwd();
                    }
                }
                break;
            }

            case R.id.skip_button: {
                if (!CommonUtils.isEmpty(mPassword)) {
//				jumpToMainTab(pass);
                    AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
                    myAfProfileInfo.password = mPassword;
                    toMainTab();
                } else {
                    ToastManager.getInstance().show(this, getString(R.string.enter_new_password));
                }
                break;
            }
            case R.id.next_button: {
                next();
                break;
            }
            case R.id.resend_button: {

                resend();
                break;
            }
            default:
                break;
        }
    }

    /**
     * button next
     */
    private void next() {
        // TODO Auto-generated method stub
        String mPassword = getEditTextPassowrd();
        if (TextUtils.isEmpty(countryCode) || TextUtils.isEmpty(phoneNum)) {
            ToastManager.getInstance().show(context, R.string.invalid_number);
            return;
        } else if (mPassword.length() <= 0) {
            ToastManager.getInstance().show(this, R.string.enter_password);
            return;
        } else if (mPassword.length() < 6 || mPassword.length() > 16) {
            ToastManager.getInstance().show(this, R.string.prompt_input_password_little6);
            return;
        } else if (!isSuccess) {
            if (TextUtils.isEmpty(getEditTextRandomCode())) {
                ToastManager.getInstance().show(this, R.string.verification_code_not_empty);
                return;
            }
            String sendContent = SharePreferenceUtils.getInstance(context).getSendContent();
            String inputContent = CommonUtils.replace(DefaultValueConstant.TARGET_SMS_RANDOM_CODE, getEditTextRandomCode(), getString(R.string.recovery_verify));
            PalmchatLogUtils.println("next  sendContent  " + sendContent + "  inputContent  " + inputContent);

            if (!TextUtils.isEmpty(sendContent) && !sendContent.contains(inputContent)) {//may be have problem
                ToastManager.getInstance().show(this, R.string.verification_code_not_correct);
                return;
            }
        }

        showProgressDialog(R.string.please_wait);
        //
        resetPass();
    }

    private void resend() {
        // TODO Auto-generated method stub
        AppDialog appDialog = new AppDialog(context);
        appDialog.createResendRandomCodeDialog(context, getString(R.string.text_random_promt), this, getTextViewCountryCode(), getEditTextPhone());
        appDialog.show();
    }


    public int toChangePwd(String resetPassword) {
        String newPassword;
        if (isSuccess) {
            newPassword = getNewPassword();
        } else {
            newPassword = getEditTextPassowrd();
        }
        int handle = mAfCorePalmchat.AfHttpChangPwd(resetPassword, newPassword, null, newPassword, this);
        PalmchatLogUtils.println("toChangePwd  resetPassword  " + resetPassword + "  handle  " + handle + "  newPassword  " + newPassword);
        return handle;
    }


    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void findViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_reset_password);
        countryCode = getIntent().getStringExtra(JsonConstant.KEY_COUNTRY_CODE);
        phoneNum = getIntent().getStringExtra(JsonConstant.KEY_PHONE);
        isSuccess = getIntent().getBooleanExtra(JsonConstant.KEY_FLAG, false);
        action = getIntent().getStringExtra(JsonConstant.KEY_ACTION);
        mResetAfid = getIntent().getStringExtra(JsonConstant.KEY_UUID);

        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;

//        view = findViewById(R.id.frameLayout_left);
//        view.setVisibility(View.GONE);
        vTextViewCountryCode = (TextView) findViewById(R.id.cty_code);
        vEditPhone = (EditText) findViewById(R.id.edit_phone);
        vEditTextPassword = (EditText) findViewById(R.id.edit_password);
        vEditTextCode = (EditText) findViewById(R.id.edit_code);
        vButtonNext = (Button) findViewById(R.id.next_button);
        vButtonResend = (Button) findViewById(R.id.resend_button);
        vButtonNext.setOnClickListener(this);
        vButtonResend.setOnClickListener(this);
        vRelativelayoutPrompt = (RelativeLayout) findViewById(R.id.relativelayout_prompt);
        vLinearlayoutVerifyCode = (LinearLayout) findViewById(R.id.linearlayout_verify_code);
        vLinearLayoutResendCode = (LinearLayout) findViewById(R.id.linearlayout_resend);
        vLinearLayoutOK = (LinearLayout) findViewById(R.id.linearlayout_set_new_password);
        vTextViewCountryCode.setText(countryCode);
        vEditPhone.setText(phoneNum);
        if (!isSuccess) {
            vRelativelayoutPrompt.setVisibility(View.VISIBLE);
            vLinearlayoutVerifyCode.setVisibility(View.VISIBLE);
            vLinearLayoutResendCode.setVisibility(View.VISIBLE);
            vLinearLayoutOK.setVisibility(View.GONE);
        }

        ((TextView) (findViewById(R.id.title_text))).setText(R.string.password_reset);

        PalmchatLogUtils.println("oldPass ->" + mPassword);
        vTextViewDetail = (TextView) findViewById(R.id.reset_pass_detail);
        vBtnComfirm = (Button) findViewById(R.id.confim_button);
        vBtnComfirm.setOnClickListener(this);
        vBtnComfirm.setClickable(false);
        vEditTextNewPassword = (EditText) findViewById(R.id.new_password);

        ImageView back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setVisibility(View.GONE);
        vEditTextNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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

    String getTextViewCountryCode() {
        return vTextViewCountryCode.getText().toString().trim();
    }

    String getEditTextPhone() {
        return vEditPhone.getText().toString().trim();
    }

    String getEditTextPassowrd() {
        return vEditTextPassword.getText().toString().trim();
    }

    String getEditTextRandomCode() {
        return vEditTextCode.getText().toString().trim();
    }

    private int resetPass() {
        // TODO Auto-generated method stub
        if (!StringUtil.isNullOrEmpty(mResetAfid)) {
            String pwd = CacheManager.getInstance().getMyProfile().password;
            String newPassword = vEditTextNewPassword.getText().toString();
            return mAfCorePalmchat.AfHttpChangPwd(pwd, newPassword, null, newPassword, this);
        } else {
            return mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_RESET_PWD_BY_PHONE,
                    phoneNum, CommonUtils.getRealCountryCode(countryCode),
                    null, null, null, 0, this);
        }
    }

    private String getNewPassword() {
        return vEditTextNewPassword.getText().toString().trim();
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }


    /**
     * 跳转到主界面
     */
    private void toMain() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, MainTab.class);
        startActivity(intent);

    }

    private void toMainTab() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, MainTab.class);
        startActivity(intent);
    }


    String mResetAfid;
    String mResetPassword;

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        // TODO Auto-generated method stub
        if (code == Consts.REQ_CODE_SUCCESS) {
            if (flag == Consts.REQ_CHANGE_PASSWORD) {

                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.FIND_P_SUCC);
//                MobclickAgent.onEvent(context, ReadyConfigXML.FIND_P_SUCC);

                dismissProgressDialog();
                System.out.println("ywp: AfOnResult: REQ_CHANGE_PASSWORD or change success");
                AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
                myAfProfileInfo.password = (String) user_data;
                myAfProfileInfo.afId = mResetAfid;

//                CacheManager.getInstance().setMyProfile(myAfProfileInfo);
                setLoginInfo(myAfProfileInfo);
                toMainTab();
            } else if (flag == Consts.REQ_RESET_PWD_BY_PHONE) {
                String[] ret = (String[]) result;
                if (ret != null && ret.length >= DefaultValueConstant.LENGTH_2) {
                    mResetAfid = ret[1];
                    mResetPassword = ret[0];
                    System.out.println("ywp: AfOnResult: REQ_RESET_PWD_BY_PHONE or REQ_RESET_PWD_BY_EMAIL password  = " + mResetPassword);
                    System.out.println("ywp: AfOnResult: REQ_RESET_PWD_BY_PHONE or REQ_RESET_PWD_BY_EMAIL afid  = " + mResetAfid);
                    login(mResetAfid, mResetPassword);
                } else {
                    dismissProgressDialog();
                    ToastManager.getInstance().show(context, R.string.failed);
                }
            } else if (flag == Consts.REQ_FLAG_LOGIN) {
                PalmchatLogUtils.println("Consts.REQ_LONGIN_1 reset");
                AfProfileInfo myProfile = (AfProfileInfo) result;
                myProfile.password = mResetPassword;
                CacheManager.getInstance().setMyProfile(myProfile);
                setLoginInfo(myProfile);
                toChangePwd(mResetPassword);
            }
        } else {
            dismissProgressDialog();
            Consts.getInstance().showToast(context, code, flag, http_code);
        }
    }

    private void setLoginInfo(AfProfileInfo info) {
        // TODO Auto-generated method stub
        AfLoginInfo login =  CacheManager.getInstance().getLoginInfo();
        login.afid = info.afId;
        login.password = info.password;
        login.cc = PalmchatApp.getOsInfo().getCountryCode();
        login.email = info.email;
        login.fdsn = info.fdsn;
    	login.blsn = info.blsn;
        login.gpsn = info.gpsn;
        login.phone = info.phone;
        login.pbsn = info.pbsn;
        PalmchatLogUtils.println("ResetActivity  setLoginInfo");
        mAfCorePalmchat.AfSetLoginInfo(login);
        mAfCorePalmchat.AfDbLoginUpdatePassword(info.afId, info.password);
//        PalmchatApp.getApplication().savePalmAccount(login, true);
    }


    private void login(String afid, String pass) {
        mAfCorePalmchat.AfHttpLogin(afid, pass, CommonUtils.getRealCountryCode(countryCode),
                Consts.AF_LOGIN_AFID, 0, this);
    }


    @Override
    public void onLeftButtonClick() {
        Intent intent = new Intent(context, RegistrationActivity.class);
        intent.putExtra(JsonConstant.KEY_ACTION, RegistrationActivity.RESET);
        intent.putExtra(JsonConstant.KEY_FROM, SignUpOrLoginActivity.class.getName());
        startActivity(intent);
        finish();
    }

    @Override
    public void onRightButtonClick() {
        // TODO Auto-generated method stub
        getRamdomSmsCode();
    }

    /**
     * get radom sms code from server
     */
    private void getRamdomSmsCode() {
        // TODO Auto-generated method stub

        showProgressDialog(R.string.please_wait);
        mAfCorePalmchat.AfHttpGetRegRandom(getEditTextPhone(), CommonUtils.getRealCountryCode(getTextViewCountryCode()), Consts.HTTP_RANDOM_FIND_PASSWORD, null, this);

    }


    @Override
    public void onBackPressed() {

    }

    /**
     * 是否可点击继续按钮
     *
     * @param bool
     */
    private void isClickBtnContinue(boolean bool) {
        if (bool) {
            vBtnComfirm.setBackgroundResource(R.drawable.login_button_selector);
            vBtnComfirm.setTextColor(Color.WHITE);
            vBtnComfirm.setClickable(true);
        } else {
            vBtnComfirm.setBackgroundResource(R.drawable.btn_blue_d);
            vBtnComfirm.setTextColor(getResources().getColor(R.color.guide_text_color));
            vBtnComfirm.setClickable(false);
        }
    }

}
