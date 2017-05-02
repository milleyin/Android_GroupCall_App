package com.afmobi.palmchat.ui.activity.palmcall;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.eventbusmodel.EventPalmcallHeadsetStateNotication;
import com.afmobi.palmchat.eventbusmodel.EventPalmcallServiceFinishNotication;
import com.afmobi.palmchat.eventbusmodel.EventPalmcallServiceNotication;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.palmcall.model.JustalkDataInfo;
import com.afmobi.palmchat.ui.customview.RippleBackground;
import com.afmobi.palmchat.ui.customview.blurredview.BlurImageView;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SensorManagerUtil;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.Consts;
import com.justalk.cloud.juscall.FontFitTextView;
import com.justalk.cloud.juscall.MtcResource;
import com.justalk.cloud.lemon.MtcCallConstants;

import de.greenrobot.event.EventBus;

/**
 * 通话界面
 */
public class PalmcallActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = PalmcallActivity.class.getSimpleName();
    /**倒计时器*/
    private Chronometer mChrState;
    /**通话背景*/
    private BlurImageView mIV_CallBg;
    /**用户名*/
    private FontFitTextView mFFTV_UserName;
    /**对方头像*/
    private ImageView mIV_UserOppositeAvatar;
    /**水波纹*/
    private RippleBackground mRB_InComingEffect;
    /**静音按钮*/
    private ImageButton mIBtn_callMute;
    /**外因切换按钮*/
    private ImageButton mIBtn_callAuto;
    /**拒接按钮*/
    private ImageButton mIBtn_callDecline;

    /**底部三个按钮布局*/
    private View mV_ThreeBtnGroup;
    /**底部两个按钮布局*/
    private View mV_TwoBtnGroup;
    /**两个按钮布局的左边按钮*/
    private ImageButton mIBtn_TwoLayoutLeft;
    /**两个按钮布局的左边按钮文字*/
    private TextView mIBtn_TwoLayoutLeftTxt;
    /**两个按钮布局的右边按钮*/
    private ImageButton mIBtn_TwoLayoutRight;
    /**两个按钮布局的右边按钮文字*/
    private TextView mIBtn_TwoLayoutRightTxt;
    /**传过来的Json数据对象*/
    private JustalkDataInfo mJustalkDataInfo;
    /**某人头像id*/
    private int mBgDefaultDrawId;
    /**来电标记*/
    private boolean isComingCall;
    /**电源管理*/
    private PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void findViews() {
        setContentView(R.layout.palmcall_activity);
        mV_ThreeBtnGroup = findViewById(R.id.call_sub_operation_threebtn);
        mV_TwoBtnGroup = findViewById(R.id.call_sub_operation_twobtn);
        mIV_CallBg = (BlurImageView)findViewById(R.id.palmcall_bg);
        mIV_CallBg.setImageDrawable(new ColorDrawable(this.getResources().getColor(R.color.base)));

        mRB_InComingEffect = (RippleBackground)findViewById(R.id.palmcall_makecall_effect);

        mFFTV_UserName = (FontFitTextView)findViewById(R.id.palmcall_name);

        mIV_UserOppositeAvatar = (ImageView) findViewById(R.id.palmcall_opposite_avatar);
        mRB_InComingEffect.startRippleAnimation();

        mChrState = (Chronometer)findViewById(R.id.call_state);

        mIBtn_callMute = (ImageButton)findViewById(R.id.call_mute_id);
        mIBtn_callMute.setOnClickListener(this);
        mIBtn_callAuto = (ImageButton)findViewById(R.id.call_auto_id);
        mIBtn_callAuto.setOnClickListener(this);
        mIBtn_callDecline = (ImageButton)findViewById(R.id.call_hangup_id);
        mIBtn_callDecline.setOnClickListener(this);
        mIBtn_TwoLayoutLeft = (ImageButton)findViewById(R.id.call_twobtn_left_id);
        mIBtn_TwoLayoutLeft.setOnClickListener(this);
        mIBtn_TwoLayoutRight = (ImageButton)findViewById(R.id.call_twobtn_right_id);
        mIBtn_TwoLayoutRight.setOnClickListener(this);
        mIBtn_TwoLayoutLeftTxt = (TextView)findViewById(R.id.call_twobtn_leftxt_id);
        mIBtn_TwoLayoutRightTxt = (TextView)findViewById(R.id.call_twobtn_rightxt_id);
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        mBgDefaultDrawId = R.drawable.head_palmcall_female;
        mJustalkDataInfo = new JustalkDataInfo();
        Intent intent = getIntent();
        if(intent==null){
            PalmchatLogUtils.i(TAG,"-------INTENT====NULL-----");
            finish();
            return;
        }
        handleIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(null!=mWakeLock){
            try{
                mWakeLock.release();
            } catch (Exception e){
                PalmchatLogUtils.e(TAG,"--------------mWakeLock.release()----onStop-----ERROR-----------");
                e.printStackTrace();
            }
        }
        SensorManagerUtil.getInstance().register_MyHeadsetPlugBroadcastReceiver(this,null,true);
        SensorManagerUtil.getInstance().setmActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PalmcallViewReponseListener listener = MtcPalmcallDelegate.getViewReponseCallBack();
        if(listener!=null){
            listener.onPostNotify();
        }
        SensorManagerUtil.getInstance().unregister_MyHeadsetPlugBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onEventMainThread(EventPalmcallServiceNotication palmcallServiceNotication){
        switch (palmcallServiceNotication.getStateString()){
            case MtcCallConstants.MtcCallIncomingNotification:
                break;
            case MtcCallConstants.MtcCallOutgoingNotification:
                mChrState.stop();
                mChrState.setText(getString(R.string.Calling)+"...");
                break;
            case MtcCallConstants.MtcCallTalkingNotification:
                resetStateText();
                mChrState.setBase(PalmcallService.getBaseTime());
                mChrState.start();
                mRB_InComingEffect.stopRippleAnimation();
                break;
            case MtcCallConstants.MtcCallTermedNotification:

                break;
            case MtcCallConstants.MtcCallConnectingNotification:
                mChrState.stop();
                mChrState.setText(getString(R.string.Connecting)+"...");
                break;
            case MtcCallConstants.MtcCallAlertedNotification:
                //Ringing
                mChrState.stop();
                mChrState.setText(getString(R.string.Calling)+"...");
                break;
            default:
                break;
        }
    }


    public void onEventMainThread(EventPalmcallHeadsetStateNotication eventPalmcallHeadsetStateNotication){
        if(mV_ThreeBtnGroup.getVisibility()==View.VISIBLE){

            PalmcallViewReponseListener listener = MtcPalmcallDelegate.getViewReponseCallBack();
            if(listener!=null){
                if(listener.isWiredHeadsetOn()){
                    mIBtn_callAuto.setEnabled(false);
                    mIBtn_callAuto.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_audio_d));
                } else {
                    mIBtn_callAuto.setEnabled(true);
                    mIBtn_callAuto.setBackgroundDrawable(getResources().getDrawable(R.drawable.call_auto_selector));
                }
            }
//            if(eventPalmcallHeadsetStateNotication.isHeadsetState()){
//                mIBtn_callAuto.setEnabled(false);
//                mIBtn_callAuto.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_audio_d));
//            } else {
//                mIBtn_callAuto.setEnabled(true);
//                mIBtn_callAuto.setBackgroundDrawable(getResources().getDrawable(R.drawable.call_auto_selector));
//                mIBtn_callAuto.setSelected(false);
//            }
        }

    }

    public void onEventMainThread(EventPalmcallServiceFinishNotication eventPalmcallServiceFinishNotication){
        finish();
    }

    private void handleIntent(Intent intent){
        mJustalkDataInfo = PalmcallService.getmJustalkDataInfo();
        if(mJustalkDataInfo==null){
            PalmchatLogUtils.i(TAG,"---------------mJustalkDataInfo==null-------------------");
            finish();
            return;
        }
        int sessId = PalmcallService.getPalmcallSessId();
        if(!intent.getBooleanExtra(IntentConstant.PALMCALLNOTIRYCALL, false)) {
            if(sessId == -1){
                isComingCall =false;
                setIncomingView(false);
            } else {
                isComingCall=true;
                setIncomingView(true);
            }
            mChrState.setText("");
            mRB_InComingEffect.startRippleAnimation();
            PowerManager powerManager=(PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
            if(mWakeLock!=null){
                try{
                    mWakeLock.acquire();
                } catch ( Exception e){
                    PalmchatLogUtils.e(TAG,"------------mWakeLock.acquire()------onResume------ERROR--");
                    e.printStackTrace();
                }
            }
        } else {
            int sessState = PalmcallService.getPalmcallSessState();
            switch (sessState){
                case MtcPalmcallDelegate.OUTGOINGSTATE:
                    setIncomingView(false);
                    mChrState.stop();
                    mChrState.setText(getString(R.string.Calling)+"...");
                    mRB_InComingEffect.startRippleAnimation();
                    break;
                case MtcPalmcallDelegate.DELEGATEOUTGOINT:
                    setIncomingView(false);
                    mChrState.stop();
                    mChrState.setText(getString(R.string.Calling)+"...");
                    mRB_InComingEffect.startRippleAnimation();
                    break;
                case MtcPalmcallDelegate.INCOMINGSTATE:
                    setIncomingView(true);
                    mChrState.stop();
                    mChrState.setText("");
                    mRB_InComingEffect.startRippleAnimation();
                    break;
                case MtcPalmcallDelegate.DELEGATECONNECTING:
                    setIncomingView(false);
                    mChrState.stop();
                    mChrState.setText(getString(R.string.Connecting)+"...");
                    mRB_InComingEffect.stopRippleAnimation();
                    break;
                case MtcPalmcallDelegate.DELEGATEALERTED:
                    setIncomingView(false);
                    mChrState.stop();
                    mChrState.setText(getString(R.string.Calling)+"...");
                    mRB_InComingEffect.startRippleAnimation();
                    break;
                default:
                    setIncomingView(false);
                    mChrState.setBase(PalmcallService.getBaseTime());
                    mChrState.start();
                    mRB_InComingEffect.stopRippleAnimation();
                    break;
            }

        }

        byte peerSex = Consts.AFMOBI_SEX_FEMALE;
        String peerHeadUrl = mJustalkDataInfo.getAFCoverUrlKey();

        try {
            peerSex =(byte)mJustalkDataInfo.getAFSexKey();
        } catch (Exception e){
            e.printStackTrace();
        }

        if ( peerSex == Consts.AFMOBI_SEX_MALE) {
            mBgDefaultDrawId  = R.drawable.head_palmcall_male;
        }

        if(!URLUtil.isHttpUrl(peerHeadUrl)) {
            peerHeadUrl = CommonUtils.splicePalcallCoverUrl(peerHeadUrl,mJustalkDataInfo.getAFAfIdKey());
        }

        ImageManager.getInstance().DisplayAvatarImage(mIV_UserOppositeAvatar, peerHeadUrl,getResources().getDimensionPixelSize(R.dimen.palmcall_avator_circle_margin),
                peerSex,null);
        ImageManager.getInstance().DisplayImage(mIV_CallBg,peerHeadUrl ,mBgDefaultDrawId,false,null);

        if(TextUtils.isEmpty(mJustalkDataInfo.getAFNameKey())){
            mFFTV_UserName.setText(mJustalkDataInfo.getAFAfIdKey());
        } else {
            mFFTV_UserName.setText(mJustalkDataInfo.getAFNameKey());
        }
    }


    /***/
    private boolean setErrorText(String text) {
        if(TextUtils.equals(text, mChrState.getText())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 设置来电时间
     * @param incoming
     */
    private void setIncomingView(boolean incoming) {
        if(incoming) {
            mV_ThreeBtnGroup.setVisibility(View.GONE);
            mV_TwoBtnGroup.setVisibility(View.VISIBLE);
        } else {
            mV_ThreeBtnGroup.setVisibility(View.VISIBLE);
            PalmcallViewReponseListener listener = MtcPalmcallDelegate.getViewReponseCallBack();
            if(listener!=null){
                if(listener.isWiredHeadsetOn()){
                    mIBtn_callAuto.setEnabled(false);
                    mIBtn_callAuto.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_audio_d));
                } else {
                    mIBtn_callAuto.setEnabled(true);
                    mIBtn_callAuto.setBackgroundDrawable(getResources().getDrawable(R.drawable.call_auto_selector));
                }
            } else {

            }
            mV_TwoBtnGroup.setVisibility(View.GONE);
        }
    }

    /**
     * 计时view重置
     */
    private void resetStateText() {
        this.mChrState.stop();
        this.mChrState.setBase(0L);
        this.mChrState.setText("");
        this.mChrState.setTextColor(-1);
    }

    /**
     * 设置状态提示
     * @param text
     * @param animated
     * @param warn
     */
    private void setStateText(CharSequence text, boolean animated, boolean warn) {
        int textColor = -1;
        if(TextUtils.isEmpty((CharSequence)text)) {
            this.mChrState.stop();
            this.mChrState.setText("");
        } else {
            this.mChrState.stop();

            if(animated) {
                text = ((CharSequence)text).toString().concat("...");
            }

            if(warn) {
                textColor = this.getResources().getColor(MtcResource.getIdByName("color", "call_poor_network_bg"));
            }

            this.mChrState.setText((CharSequence)text);
        }

        this.mChrState.setTextColor(textColor);
    }

    /**
     * 处理mute切换
     */
    private void onMute() {
        mIBtn_callMute.setSelected(!mIBtn_callMute.isSelected());
        PalmcallViewReponseListener listener = MtcPalmcallDelegate.getViewReponseCallBack();
        if(listener!=null){
            listener.onMute(mIBtn_callMute.isSelected());
        }
    }

    /**
     * 外音切换
     * @param
     */
    private void onAudio( ) {
        mIBtn_callAuto.setSelected(!mIBtn_callAuto.isSelected());
        PalmcallViewReponseListener listener = MtcPalmcallDelegate.getViewReponseCallBack();
        if(listener!=null){
            listener.onAudio(mIBtn_callAuto.isSelected());

        }
    }

    private void onDecline(){
        //拒接状态
        PalmcallViewReponseListener listener = MtcPalmcallDelegate.getViewReponseCallBack();
        if(listener!=null){
            listener.onDecline();
        }
    }

    private void onAnswer(){
        mChrState.setText(getString(R.string.Connecting));
        setIncomingView(false);
        PalmcallViewReponseListener listener = MtcPalmcallDelegate.getViewReponseCallBack();
        if(listener!=null){
            listener.onAnswer();
        }
    }

    private void onHangup( ) {
        PalmcallViewReponseListener listener = MtcPalmcallDelegate.getViewReponseCallBack();
        if(listener!=null){
            listener.onHangup();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId( )) {
            //麦克风开关
            case R.id.call_mute_id:
                onMute( );
                break;
            //外音开关
            case R.id.call_auto_id:
                onAudio( );
                break;
            //挂断按钮
            case R.id.call_hangup_id:
                onHangup();
                break;
            case R.id.call_twobtn_left_id:
                onDecline( );
                break;
            case R.id.call_twobtn_right_id:
                onAnswer( );
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChrState.stop();
        EventBus.getDefault().unregister(this);
        SensorManagerUtil.getInstance().setmActivity(null);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}