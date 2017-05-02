package com.afmobi.palmchat.ui.activity.palmcall;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.eventbusmodel.EventPalmcallHeadsetStateNotication;
import com.afmobi.palmchat.eventbusmodel.EventPalmcallNotication;
import com.afmobi.palmchat.eventbusmodel.EventPalmcallServiceFinishNotication;
import com.afmobi.palmchat.eventbusmodel.EventPalmcallServiceNotication;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.palmcall.model.JustalkDataInfo;
import com.afmobi.palmchat.util.Bluetooth.BluetoothHelper;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.TipHelper;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobigroup.gphone.R;
import com.core.AfPalmCallResp;
import com.justalk.cloud.juscall.MtcHeadsetPlugReceiver;
import com.justalk.cloud.juscall.MtcResource;
import com.justalk.cloud.juscall.MtcRing;
import com.justalk.cloud.juscall.MtcTermRing;
import com.justalk.cloud.lemon.MtcCall;
import com.justalk.cloud.lemon.MtcCallConstants;
import com.justalk.cloud.lemon.MtcMdm;
import com.justalk.cloud.lemon.MtcUser;
import com.justalk.cloud.zmf.ZmfAudio;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Transsion on 2016/9/6.
 */
public class PalmcallService extends Service  implements  MtcPalmcallDelegate.Callback,com.justalk.cloud.juscall.MtcHeadsetPlugReceiver.Callback,BluetoothHelper.Callback,PalmcallViewReponseListener{
    private static final String TAG = PalmcallService.class.getSimpleName();
    /**打电话*/
    private static final int TOCALL = 0;
    /**结束界面*/
    private static final int TOFINISH = 1;
    /**去设置speaker*/
    private static final int TOSETSPEAKER = 4;
    /**通话断开(非主动)*/
    private static final int TODISCONNECT = 7;
    /**计时用*/
    private static final int TICK_WHAT = 11;
    /**connecting之后，timeout后挂断*/
    private static final int TIMEOUTDURATION = 30*1000;
    private static JustalkDataInfo mJustalkDataInfo;
    private static Map<String, String> mUserData;
    private static long mLeftTime;
    /**来电标记*/
    private static boolean isComingCall;

    private static Toast mToast;
    private static int mSessState = 0;
    private static int mSessId = -1;
    private boolean mIsRtpConnected = false;
    /**按键发音功能*/
    private ToneGenerator mToneGenerator;
    /**闹铃管理*/
    private MtcRing mRing;
    /**显示Notification提示判断*/
    private boolean mShowNotification = false;
    /**通知的基准时间*/
    private long mNotificationBase;
    /**基准时间*/
    private static long mBaseTime;
    /***/
    private long mRealBaseTime;
    /**打电话模式标记*/
    private boolean mCallMode;
    /**音频管理器*/
    private AudioManager mAudioManager;
    /**蓝牙工具，没什么用*/
    private BluetoothHelper mBluetoothHelper;
    private MtcTermRing mTermRing;
    private int mAudio;
    private boolean mReconnecting;
    /**剩余时间不足提示的判断*/
    private boolean mLeftTimeRingTip;
    /***/
    private MtcHeadsetPlugReceiver mHeadsetPlugReceiver;
    /***/
    private boolean mBluethConnected;
    /**耳机当前状态*/
    private boolean mHeadsetState = false;


    /**电话管理器*/
    private TelephonyManager mTelephoneManager;
    /***/
    private Runnable mCheckConnectingRunable = new Runnable() {
        @Override
        public void run() {
            if(mSessState==6){
                onHangup();
            }
        }
    };
    /**电话状态监听*/
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            PalmchatLogUtils.i(TAG,"-----------------------state--===="+state+"----incomingNumber------------"+incomingNumber);
            switch (state) {
                //来电响铃
                case TelephonyManager.CALL_STATE_RINGING:
                    TipHelper.vibrate(getApplicationContext(), 500L);
                    palmcallOver();
                    break;
                //去电或者来电接通
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    TipHelper.vibrate(getApplicationContext(), 500L);
                    palmcallOver();
                    break;
                //
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                default:
                    break;
            }
        }
    };


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //主叫出去
                case TOCALL:
                    toCall();
                    break;
                //关闭
                case TOFINISH:
                    palmcallOver();
                    break;
                //
                case TOSETSPEAKER:
                    break;
                case TODISCONNECT:
                    callDisconnected();
                    break;
                case TICK_WHAT:
                    doLeftTimeCheck(SystemClock.elapsedRealtime());
                    sendMessageDelayed(Message.obtain(this, TICK_WHAT), 1000);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        PalmchatLogUtils.i(TAG,"---------onCreate---------------");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        mHeadsetState =false;
        mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.BOTTOM,0,getResources().getDimensionPixelSize(R.dimen.palmcall_toast_bottom_margin));
        mTelephoneManager= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephoneManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        MtcPalmcallDelegate.setCallback(this);
        MtcPalmcallDelegate.setViewReponseCallBack(this);
        mHeadsetPlugReceiver = new MtcHeadsetPlugReceiver();
        mHeadsetPlugReceiver.setCallback(this);
        mBluetoothHelper = new BluetoothHelper(this.getApplicationContext());
        mBluetoothHelper.setCallback(this);
        mBluetoothHelper.start();
        mAudioManager = (AudioManager)this.getApplication().getSystemService(Context.AUDIO_SERVICE);
        clearCallMode();
        PalmchatLogUtils.i(TAG,"---------onStart---------------");
        if(!isComingCall){
            mtcCallDelegateCall("","",false);
        } else {
            mtcCallDelegateIncoming(mSessId);
        }
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return super.bindService(service, conn, flags);
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PalmchatLogUtils.i(TAG,"---------onDestroy---------------");
        ringStop();
        if(this.mShowNotification) {
            this.removeNotification();
        }
        if(null!=mHandler){
            mHandler.removeMessages(TICK_WHAT);
            mHandler.removeMessages(TOFINISH);
            mHandler.removeMessages(TOSETSPEAKER);
            mHandler.removeMessages(TODISCONNECT);
            mHandler.removeCallbacks(mCheckConnectingRunable);
        }
        mToast.cancel();
        mToast = null;
        mJustalkDataInfo = null;
        if(null!=mUserData){
            mUserData.clear();
            mUserData = null;
        }
        mLeftTime =0;

        if(this.mTermRing != null) {
            this.mTermRing.release();
            this.mTermRing = null;
        }
        if(this.mBluetoothHelper != null) {
            mBluetoothHelper.stop();
            this.mBluetoothHelper.setCallback(null);
        }

        if(this.mToneGenerator != null) {
            this.mToneGenerator.stopTone();
            this.mToneGenerator.release();
            this.mToneGenerator = null;
        }

        try{
            clearCallMode();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(mHeadsetPlugReceiver != null) {
            mHeadsetPlugReceiver.stop(getApplicationContext());
            mHeadsetPlugReceiver.setCallback(null);
        }

        if(MtcPalmcallDelegate.getCallback() == this) {
            MtcPalmcallDelegate.setCallback(null);
        }

        if(MtcPalmcallDelegate.getViewReponseCallBack()==this){
            MtcPalmcallDelegate.setViewReponseCallBack(null);
        }

        if(mTelephoneManager!=null){
            mTelephoneManager.listen(mPhoneStateListener,PhoneStateListener.LISTEN_NONE);
            mPhoneStateListener= null;
        }
    }

    @Override
    public void mtcCallDelegateIncoming(int dwSessId) {
        PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegateIncoming----------------------");
        //if(this.mSessId != dwSessId)
        {
            if(mHandler.hasMessages(TOFINISH)) {
                mHandler.removeMessages(TOFINISH);
            }
            if(mHandler.hasMessages(TOCALL)) {
                mHandler.removeMessages(TOCALL);
            }
            mSessState = 1;
            mIsRtpConnected = false;
            ringTermStop();
            //if(!"samsung".equals(this.getMeta(this, "UMENG_CHANNEL")) || !MtcPalmcallDelegate.isInPhoneCall()) {
                ring(false);
            //}
            MtcCall.Mtc_CallAlert(dwSessId, 0L, MtcCallConstants.EN_MTC_CALL_ALERT_RING, false);
            PalmcallManager.getInstance().saveCallInfoToDb(dwSessId,mJustalkDataInfo, AfPalmCallResp.AFMOBI_CALL_TYPE_MISSED);
            if(mShowNotification) {
                postNotification();
            }
        }
    }

    //不用管
    @Override
    public void mtcCallDelegateReferIn(int var1, boolean var2) {

    }

    @Override
    public void mtcCallDelegateCall(String number, String peerDisplayName,boolean isVideo) {
        if((null!=mJustalkDataInfo)&&!TextUtils.isEmpty(mJustalkDataInfo.getAFAfIdKey())) {
            ringTermStop();
            if(mHandler.hasMessages(TOFINISH)) {
                mHandler.removeMessages(TOFINISH);
            }

            this.mSessState = 3;
            //this.setStateText(getString(R.string.Calling), true, false);
//            this.setViewEnabled(mIBtn_callDecline, true);
//            this.setViewEnabled(mIBtn_callMute, true);
//            this.setViewEnabled(mIBtn_callAuto, true);
            if(mShowNotification) {
                postNotification();
            }
            mReconnecting = false;
//            this.mPaused = false;
//            this.mPausedByCS = false;
            setCallMode(false);
            setAudio(this.getDefaultAudio());
            Message msg = mHandler.obtainMessage(TOCALL);
            mHandler.sendMessageDelayed(msg, 200L);
        }
    }

    @Override
    public void mtcCallDelegateOutgoing(int dwSessId) {
        if(dwSessId == this.mSessId) {
            ringBack();
            mSessState = 4;
            PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegateOutgoing----------------------");
            PalmcallManager.getInstance().saveCallInfoToDb(dwSessId,null, AfPalmCallResp.AFMOBI_CALL_TYPE_OUT);
            EventBus.getDefault().post(new EventPalmcallServiceNotication(MtcCallConstants.MtcCallOutgoingNotification));
        }
    }

    @Override
    public void mtcCallDelegateAlerted(int dwSessId, int dwAlertType) {
        if(dwSessId == this.mSessId) {
            if((dwAlertType == 2001 || dwAlertType == 2002) && this.mSessState == 4) {
                this.mSessState = 5;
                PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegateAlerted----------------------");
                EventBus.getDefault().post(new EventPalmcallServiceNotication(MtcCallConstants.MtcCallAlertedNotification));
                if(mShowNotification) {
                    postNotification();
                }
            }
        }
    }

    @Override
    public void mtcCallDelegateConnecting(int dwSessId) {
        if(dwSessId == this.mSessId) {
            EventBus.getDefault().post(new EventPalmcallServiceNotication(MtcCallConstants.MtcCallConnectingNotification));
            ringBackStop();
            PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegateConnecting----------------------");
            if(this.mSessState > 2) {
                TipHelper.vibrate(getApplicationContext(), 123L);
            }
            mSessState = 6;
            mHandler.postDelayed(mCheckConnectingRunable,TIMEOUTDURATION);
            //setStateText(getString(R.string.Connecting), true, false);
            mRealBaseTime = SystemClock.elapsedRealtime();
            if(mShowNotification) {
                postNotification();
            }
        }
    }

    @Override
    public void mtcCallDelegateTalking(int dwSessId) {
        if(dwSessId == this.mSessId) {
            if(mHandler.hasMessages(TODISCONNECT)) {
                mHandler.removeMessages(TODISCONNECT);
            }
            PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegateTalking----------------------");
            this.mIsRtpConnected = true;
            //??
            if(this.mSessState < 6) {
                this.mtcCallDelegateConnecting(dwSessId);
            }
            mReconnecting = false;
            if(mSessState == 6) {
                mSessState = 7;
                mBaseTime = SystemClock.elapsedRealtime();
                EventBus.getDefault().post(new EventPalmcallServiceNotication(MtcCallConstants.MtcCallTalkingNotification));
                mNotificationBase = System.currentTimeMillis();
                mLeftTimeRingTip = false;
                if(!isComingCall){
                    mHandler.sendMessageDelayed(Message.obtain(mHandler,TICK_WHAT), 1000);
                }
                if(mShowNotification) {
                    postNotification();
                }
            }
        }
    }

    @Override
    public void mtcCallDelegateTermed(int dwSessId, int dwStatCode, String pcReason) {
        {
            if (this.mSessId == dwSessId) {
                PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegateTermed----------------------");
                long talkedTime=0;
                PalmcallManager.getInstance().setTalkedTime(mSessId,talkedTime);
                int state = this.mSessState;
                term();
                if (!NetworkUtils.isNetworkAvailable(PalmchatApp.getApplication().getApplicationContext())) {
                    mToast.setText(getString(R.string.network_unavailable));
                    mToast.show();
                    TipHelper.vibrate(getApplicationContext(),new long[]{0,200,500,200,500,200},false);
                    mHandler.sendEmptyMessageDelayed(TOFINISH, 5000L);//finish当前界面
                } else if (state == 1) {
                    palmcallOver();
                } else if (state == 10) {
//                  mToast.setText(getString(R.string.Call_disconnected));
                    ringTerm();
                    mHandler.sendEmptyMessageDelayed(TOFINISH, 3000L);//finish当前界面
                } else {
                    short delay = 0;
                    String stateText = null;
                    if (dwStatCode < 0) {
                        dwStatCode = 1000;
                    }
                    switch (dwStatCode) {
                        case MtcCallConstants.EN_MTC_CALL_TERM_STATUS_NORMAL:
                        case MtcCallConstants.EN_MTC_CALL_TERM_STATUS_REPLACED:
                            if (state == 12) {
                                delay = 1000;
                                //stateText = this.getString(MtcResource.getIdByName("string", "Call_ending"));
                            } else if (state != 1) {
                                delay = 3000;
                                mToast.setText(R.string.call_ended);
                                mToast.show();
                            }
                            break;
                        case MtcCallConstants.EN_MTC_CALL_TERM_STATUS_DECLINE:
                            if (state != 13) {
                                delay = 3000;
                                TipHelper.vibrate(getApplicationContext(), 200L);
                                ringTerm();
                                mToast.setText(R.string.peer_refuse_to_answer);
                                mToast.show();
                            }
                            break;
                        case MtcCallConstants.EN_MTC_CALL_TERM_STATUS_BUSY:
                            delay = 5000;
                            if (TextUtils.isEmpty(pcReason)) {
                                stateText = this.getString(R.string.palmcall_busy);
                            } else {
                                stateText = pcReason;
                            }
                            mToast.setText(stateText);
                            mToast.show();

                            break;
                        case MtcCallConstants.EN_MTC_CALL_TERM_STATUS_TIMEOUT:
                            if (state >= 3 && state < 6) {
                                delay = 3000;
//                            stateText = this.getString(MtcResource.getIdByName("string", "No_answer"));
//                            ringTerm(MtcResource.getIdByName("raw", "not_answered"), 2, false);
                                mToast.setText(getString(R.string.peer_no_answer));
                                mToast.show();
                                TipHelper.vibrate(getApplicationContext(),new long[]{0,200,500,200},false);
                                ringTerm();
                            } else {
                                delay = 3000;
                                stateText = getString(R.string.Temporarily_unavailable);
                                mToast.setText(stateText);
                                mToast.show();
                                stateText = "";
                                this.ringTerm();
                            }
                            break;
                        case MtcCallConstants.EN_MTC_CALL_TERM_STATUS_USER_OFFLINE:
                            delay = 3000;
                            stateText = this.getString(R.string.Offline);
                            mToast.setText(stateText);
                            mToast.show();
                            stateText ="";
                            //ringTerm(MtcResource.getIdByName("raw", "offline"), 2, false);
                            break;
                        case MtcCallConstants.EN_MTC_CALL_TERM_STATUS_NOT_FOUND:
                            delay = 3000;
                            stateText = this.getString(MtcResource.getIdByName("string", "app_label_hasnot_been_installed"), new Object[]{this.getString(MtcResource.getIdByName("string", "app_name"))});
                            mToast.setText(stateText);
                            mToast.show();
                            stateText ="";
                            ringTerm();
                            break;
                        case MtcCallConstants.EN_MTC_CALL_TERM_STATUS_ERROR_TRANSACTION_FAIL:
                            delay = 5000;
                            if (state >= 3 && state < 6) {
                                stateText = this.getString(MtcResource.getIdByName("string", "No_internet_connection"));
                            } else {
                                stateText = this.getString(MtcResource.getIdByName("string", "Temporarily_unavailable"));
                            }

                            mToast.setText(stateText);
                            mToast.show();
                            stateText ="";
                            ringTerm();
                            break;
                        default:
                            if (state >= 3 && state < 6) {
                                delay = 5000;
                                // ringTerm(MtcResource.getIdByName("raw", "can_not_be_connected"), 2, true);
                            } else {
                                delay = 5000;
                                ringTerm();
                            }

                            stateText = this.getString(MtcResource.getIdByName("string", "Temporarily_unavailable"));
                            mToast.setText(stateText);
                            mToast.show();
                            stateText ="";
                    }

                    if (delay == 0) {
                        palmcallOver();
                    } else {
 //                       setStateText(stateText, false, false);
                        if(delay > 0) {
//                            setViewEnabled(mIBtn_callMute, false);
//                            setViewEnabled(mIBtn_callAuto, false);
//                            setViewEnabled(mIBtn_callDecline, false);
                            mHandler.sendEmptyMessageDelayed(TOFINISH, (long)delay);//finish
                        }
                    }

                }
            }
        }
    }

    @Override
    public void mtcCallDelegateTermAll() {
        PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegateTermAll----------------------");
        if(this.mSessState == 1) {
            this.declineWithText("");
        } else if(this.mSessState == 0) {
             palmcallOver();
        } else {
            this.mSessState = 12;
            this.ringBackStop();
            MtcCall.Mtc_CallTerm(this.mSessId, 1000, "");
            this.term();
            palmcallOver();
        }
    }

    @Override
    public void mtcCallDelegateLogouted() {
        PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegateLogouted----------------------");
        if(mSessState >= 1) {
            term();
          //  this.finish();
        }
    }

    @Override
    public void mtcCallDelegateStartPreview() {
        PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegateStartPreview----------------------");
    }

    @Override
    public void mtcCallDelegateStartVideo(int var1) {
        PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegateStartVideo----------------------");
    }

    @Override
    public void mtcCallDelegateStopVideo(int var1) {
        PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegateStopVideo----------------------");
    }

    @Override
    public void mtcCallDelegateNetStaChanged(int dwSessId, boolean bVideo, boolean bSend, int iStatus) {
        if(!bSend) {
            if(this.mSessId == dwSessId) {
                if(!bVideo) {
                   // this.setErrorText();
                    if(iStatus > -3) {
                        mHandler.removeMessages(TODISCONNECT);
                    }
                }
            }
        }
    }

    @Override
    public void mtcCallDelegateInfo(int dwSessId, String info) {
        PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegateInfo----------------------");
        if(this.mSessId == dwSessId) {

        }
    }

    @Override
    public void mtcCallDelegateVideoReceiveStaChanged(int var1, int var2) {
        PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegateVideoReceiveStaChanged----------------------");
    }

    @Override
    public void mtcCallDelegatePhoneCallBegan() {
        PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegatePhoneCallBegan----------------------");
        if(mSessState < 7) {
            onHangup();
        } else {
            clearCallMode();
            MtcCall.Mtc_CallInfo(this.mSessId, "Call Interrupt");
            if(this.mSessState == 7) {
              //  this.setErrorText();
            }
        }
    }

    @Override
    public void mtcCallDelegatePhoneCallEnded() {
        PalmchatLogUtils.i(TAG,"-------------------mtcCallDelegatePhoneCallEnded----------------------");
        mHandler.postDelayed(new Runnable() {
            public void run() {
                setCallMode(false);
                setAudio(mAudio);
            }
        }, 1000L);
        if(this.mSessState > 6) {
            MtcCall.Mtc_CallInfo(this.mSessId, "Call Resume");
        }

        if(this.mIsRtpConnected) {
            mSessState = 7;
            mReconnecting = false;
            setErrorText();
        } else {
           setErrorText();
        }
    }

    @Override
    public boolean mtcCallDelegateIsCalling() {
        return isCalling();
    }

    @Override
    public boolean mtcCallDelegateIsExisting(String number) {
        return TextUtils.equals(mJustalkDataInfo.getAFAfIdKey(), number);
    }

    @Override
    public int mtcCallDelegateGetCallId() {
        return mSessId;
    }

    @Override
    public void mtcHeadsetStateChanged(boolean plugged) {
        PalmchatLogUtils.i(TAG,"----------------plugged-------------------"+plugged);
        if(mHeadsetState!=plugged){
            PalmchatLogUtils.i(TAG,"----------------mHeadsetState!=plugged-------------"+plugged);
            int audio = 1;
            mHeadsetState = plugged;
            EventBus.getDefault().post(new EventPalmcallHeadsetStateNotication(plugged));
            if(!plugged) {
                if(this.mAudio != 1) {
                    return;
                }

                audio = getDefaultAudio();
            }
            setAudio(audio);
        }

    }


    @Override
    public void mtcBluetoothChanged(boolean connected) {
        if(connected){
            int audio = 3;
            if(this.mBluetoothHelper.getCount() == 0) {
                if(this.mAudio == 3) {
                    this.mAudio = this.getDefaultAudio();
                } else {
                    audio = this.mAudio;
                }
            }
            mBluethConnected = true;
            PalmchatLogUtils.i(TAG,"-----------------audio---------------------"+audio);
            EventBus.getDefault().post(new EventPalmcallHeadsetStateNotication(true));
            this.setAudio(audio);
        } else {
            mBluethConnected = false;
            EventBus.getDefault().post(new EventPalmcallHeadsetStateNotication(false));
        }

    }

    @Override
    public void onHangup() {
        if(mSessState == 0) {
            palmcallOver();
        } else {
            mSessState = 12;
            MtcCall.Mtc_CallTerm(mSessId, MtcCallConstants.EN_MTC_CALL_TERM_STATUS_NORMAL, "");
            PalmchatLogUtils.i(TAG,"-------------------onHangup----------------------");
            mtcCallDelegateTermed(mSessId, MtcCallConstants.EN_MTC_CALL_TERM_STATUS_NORMAL, "");
        }
    }

    @Override
    public void onDecline() {
        if(1==mSessState){
            mSessState = 13;
            PalmcallManager.getInstance().saveCallInfoToDb(mSessId,null,AfPalmCallResp.AFMOBI_CALL_TYPE_DECLINE);
            MtcCall.Mtc_CallTerm(mSessId, MtcCallConstants.EN_MTC_CALL_TERM_STATUS_DECLINE, "");
            PalmchatLogUtils.i(TAG,"-------------------onHangup----------------------");
            mtcCallDelegateTermed(mSessId, MtcCallConstants.EN_MTC_CALL_TERM_STATUS_DECLINE, "");
            PalmchatLogUtils.i(TAG,"----------Mtc_CallTerm--MtcCallConstants.EN_MTC_CALL_TERM_STATUS_DECLINE---"+MtcCallConstants.EN_MTC_CALL_TERM_STATUS_DECLINE);
        }
    }

    @Override
    public void onAnswer() {
        ringStop();
        if(mHandler.hasMessages(TOFINISH)){
            mHandler.removeMessages(TOFINISH);
        }
        mSessState = 2;
        if(mShowNotification) {
            postNotification();
        }
        PalmcallManager.getInstance().saveCallInfoToDb(mSessId,null,AfPalmCallResp.AFMOBI_CALL_TYPE_IN);

        setCallMode(true);
        setAudio(this.getDefaultAudio());
        if(0 != MtcCall.Mtc_CallAnswer(mSessId, 0L, true, false)) {
            MtcCall.Mtc_CallTerm(mSessId, -2, "");
            PalmchatLogUtils.i(TAG,"-------------------onAnswer----------------------");
            mtcCallDelegateTermed(mSessId, -2, "");
        }
    }

    @Override
    public void onAudio(boolean isSelected) {
//        if(this.mBluetoothHelper.getCount() > 0) {
//        } else
        PalmchatLogUtils.i(TAG,"----------------mBluetoothHelper.getCount() > 0------------------"+mBluetoothHelper.getCount());
        {
            speaker(isSelected);
        }
        adjustMusicVolumeToMax();
    }

    @Override
    public void onMute(boolean isSelected) {
        if(this.mSessId != -1) {
            MtcCall.Mtc_CallSetMicMute(this.mSessId, isSelected);
        }
    }
    @Override
    public void onPostNotify() {
        if(isCalling()){
            postNotification();
        }
    }

    @Override
    public boolean isWiredHeadsetOn() {
        boolean isWiredHeadsetOn = false;
        if(mAudioManager!=null){
            isWiredHeadsetOn = mAudioManager.isWiredHeadsetOn();
        }
        if(mHeadsetPlugReceiver!=null){
            mHeadsetPlugReceiver.start(PalmchatApp.getApplication().getApplicationContext());
        }
        if(mBluethConnected){
            isWiredHeadsetOn = true;
        }
        return isWiredHeadsetOn;
    }

    /**
     * 使用mtc拨打
     */
    private void toCall() {
        if(this.mSessId == -1 && this.mSessState == 3) {
            String uri = MtcUser.Mtc_UserFormUri(3, mJustalkDataInfo.getAFAfIdKey());
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(MtcCallConstants.MtcCallInfoUserDataKey, mUserData.get(MtcCallConstants.MtcCallInfoUserDataKey));
            } catch (JSONException var8) {
                var8.printStackTrace();
            }
            mSessId = MtcCall.Mtc_CallJ(uri, 0L, jsonObject.toString());
            if(mSessId == -1) {
                PalmchatLogUtils.i(TAG,"------------------- if(mSessId == -1) {----------------------");
               mtcCallDelegateTermed(this.mSessId, -1, "");
            } else {
                mIsRtpConnected = false;
            }
        }
    }

    /**
     *调用断掉通话
     */
    private void callDisconnected() {
        MtcCall.Mtc_CallTerm(mSessId, -3, "");
        mSessState = 10;
        PalmchatLogUtils.i(TAG,"-------------------callDisconnected---------------------");
        mtcCallDelegateTermed(mSessId, -3, "");
    }

    private void declineWithText(String text) {
        mSessState = 13;
        MtcCall.Mtc_CallTerm(this.mSessId, 1002, text);
        PalmchatLogUtils.i(TAG,"-------------------declineWithText---------------------");
        mtcCallDelegateTermed(this.mSessId, 1002, "");
    }

    private void setErrorText() {
        setErrorText(true);
    }
    private void setErrorText(boolean alertIfNeeded) {
        if(this.mSessState < 7) {
            //setErrorText((String)null);
        } else if(this.getNet() == -2) {
            if(!mHandler.hasMessages(TODISCONNECT)) {
                mHandler.sendEmptyMessageDelayed(TODISCONNECT, 30000L);
            }

        } else if(this.mReconnecting) {

        } else {
            int audioNetSta = MtcCall.Mtc_CallGetAudioNetSta(this.mSessId);
            if(audioNetSta <= -3) {
                if(!mHandler.hasMessages(TODISCONNECT)) {
                    mHandler.sendEmptyMessageDelayed(TODISCONNECT, 30000L);
                }
            }
        }
    }

    private int getNet() {
        int net = -2;
        ConnectivityManager cm = (ConnectivityManager)this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            net = ni.getType() << 8;
        }
        return net;
    }

    /**
     * 终止
     */
    private void term() {
        ringStop();
        ringBackStop();
        removeNotification();
        mSessState = 0;
        mSessId = -1;
    }

    /**
     * 闹铃响起
     * @param vibrateOnly
     */
    private void ring(boolean vibrateOnly) {
        if (this.mRing == null) {
            this.mRing = new MtcRing();
        }
        if (vibrateOnly) {
            this.mRing.vibrate(this.getApplicationContext());
        } else {
            this.mRing.ring(this.getApplicationContext());
        }
    }

    /**
     * 闹铃响起
     * @param vibrateOnly
     */
    private void ring(Context c, String ringtone, boolean looping, long timeout, int streamType) {
        if (mRing == null) {
            mRing = new MtcRing();
        }
        mRing.play(c,ringtone,looping,timeout,streamType);
    }

    /**
     * ringBack
     */
    public void ringBack() {
        if(this.mToneGenerator == null) {
            this.mToneGenerator = new ToneGenerator(this.getStreamType(), 80);
        }
        this.mToneGenerator.startTone(ToneGenerator.TONE_SUP_RINGTONE);
    }

    /**
     * 响铃停止
     */
    private void ringStop() {
        if (this.mRing != null) {
            this.mRing.stop();
        }
    }

    public void ringBackStop() {
        if(this.mToneGenerator != null) {
            this.mToneGenerator.stopTone();
        }
    }

    public void ringTerm() {
        if(this.mToneGenerator == null) {
            this.mToneGenerator = new ToneGenerator(getStreamType(), 80);
        }
        this.mToneGenerator.startTone(20);
    }

    private int getStreamType() {
        return ZmfAudio.outputGetStreamType(MtcMdm.Mtc_MdmGetAndroidAudioOutputDevice());
    }

    /**
     * 设置为打电话模式
     *
     * @param answering
     */
    private void setCallMode(boolean answering) {
        if (!mCallMode) {
            mCallMode = true;
            int mode = MtcMdm.Mtc_MdmGetAndroidAudioMode();
            if (mode != this.mAudioManager.getMode()) {
                this.mAudioManager.setMode(mode);
            }

            //mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            mAudioManager.requestAudioFocus((AudioManager.OnAudioFocusChangeListener) null,AudioManager.STREAM_VOICE_CALL, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            audioStart(answering);
            adjustMusicVolumeToMax();
//            mHeadsetPlugReceiver.start(getApplicationContext());
            mBluetoothHelper.start();
        }
    }

    private void setAudio(int audio) {
        if (this.mBluetoothHelper.getCount() > 0) {
            if (inCallMode()) {
                switch (audio) {
                    case 0:
                    case 1:
                        this.mBluetoothHelper.unlink(false);
                        break;
                    case 2:
                        this.mBluetoothHelper.unlink(true);
                        break;
                    case 3:
                        this.mBluetoothHelper.link((String) this.mBluetoothHelper.mAddressList.get(0));
                }
            }
            //this.mBtnAudio.setSelected(false);
            mAudio = audio;
        } else {
//            this.mBtnAudio.setImageResource(MtcResource.getIdByName("drawable", "call_speaker_state"));
//            this.mBtnAudio.setSelected(audio == 2);
            //这个有点问题
            speaker(false);
        }
    }

    /**
     * 判断是否为打电话模式
     * @return
     */
    private boolean inCallMode() {
        return this.mAudioManager == null ? false : this.mCallMode;
    }

    /**
     * 清掉电话模式
     */
    private void clearCallMode() {
        mCallMode = false;
        if (mAudioManager != null) {
            mHeadsetPlugReceiver.stop(getApplicationContext());
            mBluetoothHelper.stop();
            synchronized (this) {
                ZmfAudio.inputStopAll();
                ZmfAudio.outputStopAll();
            }

            if (0 != mAudioManager.getMode()) {
                mAudioManager.setMode(0);
            }
            mAudioManager.abandonAudioFocus(null);
        }
    }

    private void audioStart(boolean answering) {
        if (isCalling()) {
            try{
                syncAudioStart(answering);
            } catch (Exception e) {
                ToastManager.getInstance().show(getApplicationContext(),"Please set the permissions!");
                palmcallOver();
                e.printStackTrace();
            }
        }
    }

    private void adjustMusicVolumeToMax() {
        int streamType = getStreamType();
        if (streamType == 3) {
            this.mAudioManager.setStreamVolume(streamType, this.mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        }
    }

    private void syncAudioStart(boolean answering) {
        int ret = ZmfAudio.outputStart(MtcMdm.Mtc_MdmGetAndroidAudioOutputDevice(), 0, 0);
        if (ret == 0) {
            ret = ZmfAudio.inputStart(MtcMdm.Mtc_MdmGetAndroidAudioInputDevice(), 0, 0, MtcMdm.Mtc_MdmGetOsAec() ? 1 : 0, MtcMdm.Mtc_MdmGetOsAgc() ? 1 : 0);
        }

        if (ret != 0) {
            ZmfAudio.inputStopAll();
            ZmfAudio.outputStopAll();
            if (mAudioManager != null && 0 != mAudioManager.getMode()) {
                mAudioManager.setMode(AudioManager.MODE_NORMAL);
            }
            ret = ZmfAudio.outputStart(MtcMdm.Mtc_MdmGetAndroidAudioOutputDevice(), 0, 0);
            if (ret == 0) {
                ret = ZmfAudio.inputStart(MtcMdm.Mtc_MdmGetAndroidAudioInputDevice(), 0, 0, MtcMdm.Mtc_MdmGetOsAec() ? 1 : 0, MtcMdm.Mtc_MdmGetOsAgc() ? 1 : 0);
            }
        }
    }

    private void doLeftTimeCheck(long time){
        long realTime = time - mRealBaseTime;
        realTime /=1000;
        PalmchatLogUtils.i(TAG,"-----------------doLeftTimeCheck----------------------");
        if((!mLeftTimeRingTip)&&((mLeftTime-realTime)<=60)){
            mLeftTimeRingTip = true;
            ring(getApplicationContext(),"joanna.mp3",false,0,AudioManager.STREAM_RING);
        }

        if(mLeftTime-realTime<=0){
            PalmchatLogUtils.i(TAG,"-----CALLING TIME-----OVER--");
            onHangup();
        }
    }


    private int getDefaultAudio() {
//        return this.mBluetoothHelper.getCount() > 0?3:(this.mBtnAudio.isSelected()?2:(this.mHeadsetPlugReceiver.mPlugged?1:(this.isVideo()?2:0)));
        return this.mBluetoothHelper.getCount() > 0 ? 3 : (false ? 2 : (this.mHeadsetPlugReceiver.mPlugged ? 1 : 0));
    }

    private void ringTermStop() {
        if(this.mTermRing != null) {
            this.mTermRing.stop();
        }
    }

    private void palmcallOver(){
        EventBus.getDefault().post(new EventPalmcallServiceFinishNotication());
        stopSelf();
    }
    private  boolean isTalking(){
        return this.mSessState >= 6 && this.mSessState <= 9;
    }
    /**
     * post通知栏
     */
    private void postNotification() {
        int id= R.drawable.app_icon_notify;
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            id = R.drawable.icon_notify;
        }
        Context context = this.getApplicationContext();
        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context);
        String title = mJustalkDataInfo.getAFAfIdKey();
        if(!TextUtils.isEmpty(mJustalkDataInfo.getAFNameKey())) {
            title = mJustalkDataInfo.getAFNameKey();
        }

        builder.setContentTitle(title);
        builder.setSmallIcon(id);
        builder.setOngoing(true);
        builder.setPriority(2);
        String state = MtcPalmcallDelegate.getStateString(this, this.mSessState, false, false);
        builder.setTicker(state);
        builder.setContentText(state);
        if(isTalking()) {
            builder.setWhen(mNotificationBase);
            builder.setUsesChronometer(true);
        }
        Intent intent = new Intent(context, PalmcallActivity.class);
        intent.putExtra(IntentConstant.PALMCALLNOTIRYCALL, true);
        PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 134217728);
        builder.setContentIntent(pending);
        NotificationManager notificationManager = (NotificationManager)this.getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(CommonUtils.TYPE_PALMCALL,builder.build());
        mShowNotification = true;
    }

    /**
     * 删掉notify通知
     */
    private void removeNotification() {
        NotificationManager notificationManager = (NotificationManager)this.getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(CommonUtils.TYPE_PALMCALL);
        mShowNotification = false;
    }
    private void speaker(boolean isSelected) {
        //先屏蔽掉这个判断，了解清楚该功能后再填上
        if (isSelected) {
            if (this.inCallMode() && !this.mAudioManager.isSpeakerphoneOn()) {
                this.mAudioManager.setSpeakerphoneOn(true);
            }
            this.mAudio = 2;
        } else {
            if (this.inCallMode() && this.mAudioManager.isSpeakerphoneOn()) {
                this.mAudioManager.setSpeakerphoneOn(false);
            }
            this.mAudio = this.mHeadsetPlugReceiver.mPlugged ? 1 : 0;
        }
    }

    public synchronized static void outgoingStart(JustalkDataInfo justalkDataInfo, Map<String, String> userData, long leftTime) {
        if(mSessState!=0){
            ToastManager.getInstance().show(PalmchatApp.getApplication().getApplicationContext(),PalmchatApp.getApplication().getApplicationContext().getString(R.string.Talking));
            if(justalkDataInfo!=null) {
                PalmchatLogUtils.i(TAG,"---outgoingStart--istalking----"+justalkDataInfo.getAFAfIdKey());
            }
            return;
        } else {
            mJustalkDataInfo = justalkDataInfo;
            isComingCall = false;
            mSessId = -1;
            mSessState =0;
            mUserData = userData;
            mLeftTime = leftTime*60;
            if(VoiceManager.getInstance().isPlaying()){
                VoiceManager.getInstance().pause();
            }
            Intent intent = new Intent(PalmchatApp.getApplication().getApplicationContext(),PalmcallService.class);
            PalmchatApp.getApplication().getApplicationContext().startService(intent);

            Intent intent1 = new Intent(PalmchatApp.getApplication().getApplicationContext(),PalmcallActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PalmchatApp.getApplication().getApplicationContext().startActivity(intent1);
        }
    }

    public synchronized static void incomingStart(int dwCallId,JustalkDataInfo justalkDataInfo){

        //先判断palmchat是否在线
        if(TextUtils.isEmpty(PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession())|| SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getIsClosePalmchat()){
            MtcCall.Mtc_CallTerm(dwCallId, MtcCallConstants.EN_MTC_CALL_TERM_STATUS_USER_OFFLINE, "");
            PalmchatLogUtils.i(TAG,"---AfHttpGetServerSession--==null------getIsClosePalmchat---true---");
            PalmcallManager.getInstance().logoutPalmcall();
            return;
        }

        if(mSessState!=0||(mJustalkDataInfo!=null)){
            if(mToast!=null){
//                mToast.setText(PalmchatApp.getApplication().getApplicationContext().getString(R.string.onoallcoming));
//                mToast.show();
            } else {
          //      ToastManager.getInstance().show(PalmchatApp.getApplication().getApplicationContext(),PalmchatApp.getApplication().getApplicationContext().getString(R.string.onoallcoming));
            }
            if(justalkDataInfo==null){
                PalmchatLogUtils.i(TAG,"---incomingStart--istalking---justalkDataInfo==null-");
                return;
            } else {
                PalmcallManager.getInstance().saveCallInfoToDb(dwCallId,justalkDataInfo, AfPalmCallResp.AFMOBI_CALL_TYPE_MISSED);
                PalmchatLogUtils.i(TAG,"---incomingStart--istalking----"+justalkDataInfo.getAFAfIdKey());
                EventBus.getDefault().post(new EventPalmcallNotication(MtcCallConstants.MtcCallTermedNotification,AfPalmCallResp.AFMOBI_CALL_TYPE_MISSED,dwCallId,0));
            }
            TipHelper.vibrate(PalmchatApp.getApplication().getApplicationContext(), 123L);
            MtcCall.Mtc_CallTerm(dwCallId, MtcCallConstants.EN_MTC_CALL_TERM_STATUS_BUSY, "");
            return;
        }
        mJustalkDataInfo = justalkDataInfo;
        mSessId = dwCallId;
        isComingCall = true;
        mSessState =0;
        if(VoiceManager.getInstance().isPlaying()){
            VoiceManager.getInstance().pause();
        }
        //需要判断当前是否在通话
        Intent intent = new Intent(PalmchatApp.getApplication().getApplicationContext(),PalmcallService.class);
        PalmchatApp.getApplication().getApplicationContext().startService(intent);
        Intent intent1 = new Intent(PalmchatApp.getApplication().getApplicationContext(),PalmcallActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PalmchatApp.getApplication().getApplicationContext().startActivity(intent1);
    }

    /**
     * 判断是否在通话
     * @return
     */
    public static boolean isCalling() {
        return mSessState > 0 && mSessState <= 9;
    }

    public static int getPalmcallSessId(){
        return mSessId;
    }

    public static JustalkDataInfo getmJustalkDataInfo(){
        return mJustalkDataInfo;
    }

    public static int getPalmcallSessState(){
        return mSessState;
    }

    public static long getBaseTime(){
        return  mBaseTime;
    }
}
