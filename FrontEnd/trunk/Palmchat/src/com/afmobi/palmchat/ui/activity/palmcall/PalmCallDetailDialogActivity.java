package com.afmobi.palmchat.ui.activity.palmcall;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.RoundImageView;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfPalmCallResp;
import com.core.Consts;
import com.core.listener.AfHttpResultListener;

import java.util.ArrayList;

/**
 * Created by heguiming on 2016/8/3.
 * palmcall点击详细dialog界面
 */
public class PalmCallDetailDialogActivity extends BaseActivity implements AfHttpResultListener,CallSomeOneStateListener {
    private final String TAG = PalmCallDetailDialogActivity.class.getSimpleName();
    private AfPalmCallResp.AfPalmCallHotListItem mListItem;
    //头像
    private RoundImageView mIvDetailCallPhoto;
    //剩余时间
    private int mTime;
    private ImageView mIvDetailCall;//按钮
    //名称 性别 拨打次数 剩余时间
    private TextView mTvDetailCallName, mTvDetailCallAge, mTvDetailCallFrequency, mTvDetailSurplusMin;
    //按钮的点击范围很小，所以引用mLlDetailCall做为点击事件
    private LinearLayout mLlDetailCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(true);//设置点击空百处消失
        findViews();
        init();
    }

    public void findViews() {
        setContentView(R.layout.dialog_activity_palmcall_detail);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) (d.getWidth() / 1.2); // 宽度
        p.height = (int) (d.getHeight() / 1.4); // 高度
//        p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.8
//        p.width = (int) (d.getWidth() * 0.9);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.date_picker_dialog_round));
        getWindow().setAttributes(p);
        mIvDetailCallPhoto = (RoundImageView) findViewById(R.id.iv_detail_call_photo);
        mIvDetailCall = (ImageView) findViewById(R.id.iv_detail_call);
        mTvDetailCallName = (TextView) findViewById(R.id.tv_detail_call_name);
        mTvDetailCallAge = (TextView) findViewById(R.id.tv_detail_call_age);
        mTvDetailCallFrequency = (TextView) findViewById(R.id.tv_detail_call_frequency);
        mTvDetailSurplusMin = (TextView) findViewById(R.id.tv_detail_surplus_min);
        mLlDetailCall = (LinearLayout)findViewById(R.id.ll_detail_call);
    }


    public void init() {
        mListItem = (AfPalmCallResp.AfPalmCallHotListItem) getIntent().getSerializableExtra(JsonConstant.CALLLISTITEM);
        mTime = getIntent().getIntExtra(JsonConstant.CALLLISTTIME,0);
        PalmcallManager.getInstance().setCallSomeOneStateListener(this);
        if (null != mListItem) {
            String DescUrl = CommonUtils.splicePalcallCoverUrl(mListItem.coverUrl,mListItem.afid);
            //设置头像
            ImageManager.getInstance().DisplayAvatarImageCall(mIvDetailCallPhoto,DescUrl,
                     (byte) mListItem.sex, null, false,true);
            //设置名称
            mTvDetailCallName.setText(mListItem.name);
            //设置性别和年龄
            mTvDetailCallAge.setBackgroundResource(Consts.AFMOBI_SEX_MALE == mListItem.sex ? R.drawable.bg_palmcall_popup_age_male : R.drawable.bg_palmcall_popup_age_female);
            mTvDetailCallAge.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == mListItem.sex ? R.drawable.bg_palmcall_popup_gender_male : R.drawable.bg_palmcall_popup_gender_female, 0, 0, 0);
            mTvDetailCallAge.setText(mListItem.age+"");
            //设置拨打次数
            mTvDetailCallFrequency.setText(this.getString(R.string.call_ai_times).replace(ReplaceConstant.TARGET_FOR_REPLACE, String.valueOf( mListItem.answeringTimes)));
            String text = String.format(getResources().getString(R.string.call_residual_time),mTime);
            SpannableStringBuilder styke = new SpannableStringBuilder(text);
            String time = mTime + "";
            int index = text.indexOf(time);
            styke.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.log_blue)),index,index+ time.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            mTvDetailSurplusMin.setText(styke);
            mLlDetailCall.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    callOneInfo(mListItem);
                }
            });
        }
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        if(code == Consts.REQ_CODE_SUCCESS){
            AfPalmCallResp  afPalmCallResp = null;
            switch (flag){
                case Consts.REQ_PALM_CALL_GET_INFO:
                    try {
                        afPalmCallResp = (AfPalmCallResp)result;
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    if(null!=afPalmCallResp){
                        PalmcallManager.getInstance().savaAfPalmCallRespToDb(afPalmCallResp);
                        doCallWidthThirdSdk(afPalmCallResp,mListItem);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void callOneInfo(AfPalmCallResp.AfPalmCallHotListItem listItem) {
        if(TextUtils.isEmpty(listItem.afid)) {
            PalmchatLogUtils.i(TAG,getString(R.string.afid_not_found)+"callOneInfo");
            ToastManager.getInstance().show(this,R.string.error_occurred);
            return;
        }
        PalmchatLogUtils.i(TAG, "---to---do-AfHttpPalmCallMakeCall----" );
        PalmcallManager.getInstance().palmcallCall(listItem);

    }


    /**
     * 通过菊风服务器拨打电话
     * @param afPalmCallRespSelf
     * @param listItem
     */

    private void doCallWidthThirdSdk(AfPalmCallResp afPalmCallRespSelf,AfPalmCallResp.AfPalmCallHotListItem listItem) {
        if (null!=listItem&&(afPalmCallRespSelf!=null)) {
            AfPalmCallResp.AfPalmCallHotListItem afPalmCallHotListItem = null;
            try{
                afPalmCallHotListItem = ((ArrayList<AfPalmCallResp.AfPalmCallHotListItem>)afPalmCallRespSelf.respobj).get(0);
            } catch (Exception e){
                PalmchatLogUtils.i(TAG,"---------afPalmCallHotListItem1======NULLL--------"+e.toString());
                e.printStackTrace();
            }
            if(null!=afPalmCallHotListItem){

                finish();
            } else {
                PalmchatLogUtils.i(TAG,"---------------------null==afPalmCallHotListItem--------");
                ToastManager.getInstance().show(getApplicationContext(),"-----null==afPalmCallHotListItem-----");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PalmcallManager.getInstance().setCallSomeOneStateListener(null);
    }

    @Override
    public void progressing() {
        showProgressDialog(R.string.loading);
        if(null!=dialog){
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    PalmcallManager.getInstance().cancelTocall();
                }
            });
        }
    }

    @Override
    public void progressend(boolean isFinish) {
        dismissAllDialog();
        if(isFinish){
            finish();
        }
    }

    @Override
    public void noLeftTime() {
        AppDialog appDialog = new AppDialog(this);
        String msg =PalmchatApp.getApplication().getApplicationContext().getString(R.string.palmcall_recharger_tip);
        appDialog.createConfirmDialog(this, msg, new AppDialog.OnConfirmButtonDialogListener() {
            @Override
            public void onRightButtonClick() {
                PalmcallManager.getInstance().jumpToChargePage();
            }

            @Override
            public void onLeftButtonClick() {

            }
        });
        appDialog.show();
    }
}
