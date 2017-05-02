package com.afmobi.palmchat.ui.activity.setting;

import java.util.ArrayList;
import java.util.List;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnRadioButtonDialogListener;
import com.afmobi.palmchat.util.StringUtil;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 设置密保问题
 * @author Transsion
 *
 */
public class SetRequestActivity extends BaseActivity implements
        OnClickListener, AfHttpResultListener {

    private final String TAG = SetRequestActivity.class.getCanonicalName();
    /**密保问一*/
    private TextView mTxt_First_SecQuestion;
    /**密保问二*/
    private TextView mTxt_Second_SecQuestion;
    /**密保问三*/
    private TextView mTxt_Third_SecQuestion;
    /**密保答一*/
    private EditText mEdtTxt_First_Answer;
    /**密保答二*/
    private EditText mEdtTxt_Second_Answer;
    /**密保答三*/
    private EditText mEdtTxt_Third_Answer;
    /**下一步*/
    private Button mBtn_Next;
    /**本地接口类*/
    protected AfPalmchat mAfCorePalmchat;
    private List<String> requestList;
    private String afid;
    @Override
    public void findViews() {
        setContentView(R.layout.activity_set_request);
        
        ((TextView)findViewById(R.id.title_text)).setText(R.string.set_request);
        findViewById(R.id.back_button).setOnClickListener(this);

        mTxt_First_SecQuestion = (TextView) findViewById(R.id.one_txt);
        mTxt_First_SecQuestion.setOnClickListener(this);
        mTxt_Second_SecQuestion = (TextView) findViewById(R.id.two_txt);
        mTxt_Second_SecQuestion.setOnClickListener(this);
        mTxt_Third_SecQuestion = (TextView) findViewById(R.id.three_txt);
        mTxt_Third_SecQuestion.setOnClickListener(this);

        mEdtTxt_First_Answer = (EditText) findViewById(R.id.one_edit);
        mEdtTxt_First_Answer.addTextChangedListener(new TextWatcherEdit());
        mEdtTxt_Second_Answer = (EditText) findViewById(R.id.two_edit);
        mEdtTxt_Second_Answer.addTextChangedListener(new TextWatcherEdit());
        mEdtTxt_Third_Answer = (EditText) findViewById(R.id.three_edit);
        mEdtTxt_Third_Answer.addTextChangedListener(new TextWatcherEdit());

        mBtn_Next = (Button) findViewById(R.id.next_btn);
        mBtn_Next.setOnClickListener(this);
        mBtn_Next.setClickable(false);
        mEdtTxt_First_Answer.addTextChangedListener(textWatcher);
        mEdtTxt_Second_Answer.addTextChangedListener(textWatcher);
        mEdtTxt_Third_Answer.addTextChangedListener(textWatcher);
    }

    /**
     * 监听edittext是否输入有值
     */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            String first = mEdtTxt_First_Answer.getText().toString();
            String second = mEdtTxt_Second_Answer.getText().toString();
            String Third = mEdtTxt_Third_Answer.getText().toString();

            if(s.length() > 0 || (first.length()> 0 || second.length() > 0 || Third.length() > 0)){
                isClickBtnContinue(true);
            }else{
                isClickBtnContinue(false);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     *改变button的颜色值
     * @param bool
     */
    private void isClickBtnContinue(boolean bool){
        if (bool) {
            mBtn_Next.setBackgroundResource(R.drawable.login_button_selector);
            mBtn_Next.setClickable(true);//恢复按钮的点击
        } else {
            mBtn_Next.setBackgroundResource(R.drawable.btn_blue_d);
            mBtn_Next.setClickable(false);//设置按钮不能点击
        }
    }

    @Override
    public void init() {
        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        requestList = new ArrayList<String>();
        requestList.add(getString(R.string.the_name_of_your_first_love));
        requestList.add(getString(R.string.your_best_friends_name));
        requestList.add(getString(R.string.the_job_you_are_dreaming_of));
        requestList.add(getString(R.string.where_do_you_often_go));
        requestList.add(getString(R.string.your_father_or_mothers_name));
        requestList.add(getString(R.string.person_influenced_you_most));

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            afid = bundle.getString(SecurityAnswerActivity.KEY_AFID);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.next_btn:
                setQuestion();
                break;
            case R.id.one_txt:
                showSelectQuestionDialog(1, mTxt_First_SecQuestion);
                break;
            case R.id.two_txt:
                showSelectQuestionDialog(2, mTxt_Second_SecQuestion);
                break;
            case R.id.three_txt:
                showSelectQuestionDialog(3, mTxt_Third_SecQuestion);
                break;
            default:
                break;
        }
    }

    /**
     * 密保问题选择对话框
     * @param no 序号
     * @param mTxt
     */
    private void showSelectQuestionDialog(int no, final TextView mTxt) {
        String msg = null;
        String str1 = null;
        String str2 = null;
        if (1 == no) {
            msg = mTxt_First_SecQuestion.getText().toString();
            str1 = mTxt_Second_SecQuestion.getText().toString();
            str2 = mTxt_Third_SecQuestion.getText().toString();
        } else if ( 2 == no) {
            msg = mTxt_Second_SecQuestion.getText().toString();
            str1 = mTxt_First_SecQuestion.getText().toString();
            str2 = mTxt_Third_SecQuestion.getText().toString();
        } else if (3 == no) {
            msg = mTxt_Third_SecQuestion.getText().toString();
            str1 = mTxt_First_SecQuestion.getText().toString();
            str2 = mTxt_Second_SecQuestion.getText().toString();
        }

        if (!StringUtil.isNullOrEmpty(msg)) {
            List<String> listAry = new ArrayList<String>();
            for (int i = 0; i < requestList.size(); i++) {
                String s = requestList.get(i);
                if (!str1.equals(s) && !str2.equals(s)) {
                    listAry.add(s);
                }
            }
            int index = 0;
            final String[] requestAry = new String[listAry.size()];
            for (int i = 0; i < listAry.size(); i++) {
                if (listAry.get(i).equals(msg)) {
                    index = i;
                }
                requestAry[i] = listAry.get(i);
            }

            AppDialog appDialog = new AppDialog(this);
            appDialog.createRadioButtonDialog(this, getString(R.string.select_question), index, requestAry, new OnRadioButtonDialogListener() {
                @Override
                public void onRightButtonClick(int selectIndex) {
                    mTxt.setText(requestAry[selectIndex]);
                }

                @Override
                public void onLeftButtonClick() {

                }
            });
            appDialog.show();
        }
    }

    private void setQuestion() {
        String oneTxt = mTxt_First_SecQuestion.getText().toString();
        String oneEdit = mEdtTxt_First_Answer.getText().toString();
        String twoTxt = mTxt_Second_SecQuestion.getText().toString();
        String twoEdit = mEdtTxt_Second_Answer.getText().toString();
        String threeTxt = mTxt_Third_SecQuestion.getText().toString();
        String threeEdit = mEdtTxt_Third_Answer.getText().toString();

        List<String> questionList = new ArrayList<String>();
        List<String> answerList = new ArrayList<String>();

        if (StringUtil.isNullOrEmpty(oneEdit)
                || StringUtil.isNullOrEmpty(twoEdit)
                || StringUtil.isNullOrEmpty(threeEdit)) {
            showToast(R.string.please_set_answer);
            return;
        } else {
            if (!StringUtil.isNullOrEmpty(oneEdit)) {
                questionList.add(oneTxt);
                answerList.add(oneEdit);
            }
            if (!StringUtil.isNullOrEmpty(twoEdit)) {
                questionList.add(twoTxt);
                answerList.add(twoEdit);
            }
            if (!StringUtil.isNullOrEmpty(threeEdit)) {
                questionList.add(threeTxt);
                answerList.add(threeEdit);
            }
        }

        showProgressDialog(R.string.loading);
        mAfCorePalmchat.AfHttpFindPwdAnswer(Consts.REQ_PSD_SET_ANSWER, afid,
                Consts.AF_LOGIN_AFID, (String[]) questionList.toArray(new String[questionList.size()]),
                (String[]) answerList.toArray(new String[answerList.size()]), null, this);

    }


    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        PalmchatLogUtils.e(TAG, "----Flag:" + flag + "----Code:" + code);
        dismissProgressDialog();
        if (code == Consts.REQ_CODE_SUCCESS) {
            // heguiming 2013-12-04
            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SET_SA_SUCC);
//            MobclickAgent.onEvent(context, ReadyConfigXML.SET_SA_SUCC);

            showToast(R.string.succeeded);
            setResult(MyAccountActivity.SET_SUCCESS);
            finish();
        } else {
            showToast(R.string.failure);
        }
    }

    private class TextWatcherEdit implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            PalmchatLogUtils.e(TAG, s.toString());
            for (int i = s.length(); i > 0; i--) {
                if (s.subSequence(i - 1, i).toString().equals("\n"))
                    s.replace(i - 1, i, "");
            }
            if (StringUtil.getCharacterNum(s.toString()) > 100) {
                s.replace(s.length() - 1, s.length(), "");
            }
        }
    }

}
