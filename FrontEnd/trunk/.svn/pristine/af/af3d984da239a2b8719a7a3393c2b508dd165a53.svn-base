//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.afmobi.palmchat.ui.activity.palmcall;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.EventPalmcallNotication;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.palmcall.model.JustalkDataInfo;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfPalmCallResp;
import com.justalk.cloud.juscall.FloatWindowService;
import com.justalk.cloud.juscall.MtcResource;
import com.justalk.cloud.lemon.MtcApi;
import com.justalk.cloud.lemon.MtcCall;
import com.justalk.cloud.lemon.MtcCallConstants;
import com.justalk.cloud.lemon.MtcMdm;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class MtcPalmcallDelegate {
    private static final String TAG = MtcPalmcallDelegate.class.getSimpleName();
    public static Context sContext;
    private static WeakReference<MtcPalmcallDelegate.Callback> sCallback;
    private static WeakReference<PalmcallViewReponseListener> sServiceback;
    private static Class<?> sCallActivityClass;
    private static boolean sIsInPhoneCall = false;
    private static MtcPalmcallDelegate.CallListener sCallListener;
    private static TelephonyManager sTelephonyManager;
    private static ArrayList<Integer> sPendingCall;
    public static final String CALL_ID = "call_id";
    public static final String VIDEO = "video";
    public static final String NUMBER = "number";
    public static final String DISPLAY_NAME = "name";
    public static final String PEER_HEADURL = "peer_headUrl";
    public static final String PEER_SEX = "peer_sex";
    public static final String LEFT_TIME = "left_time";
    public static final String PEER_DISPLAY_NAME = "peer_name";
    public static final String TERMED = "termed";
    public static final String STAT_CODE = "stat_code";
    public static final String TERMED_REASON = "term_reason";
    public static final String IS_CALL_REFERIN = "is_call_referin";
    public static final String PEER_INFO = "peer_info";
    public static final int  INCOMINGSTATE = 1;
    public static final int OUTGOINGSTATE = 3;
    public static final int DELEGATEOUTGOINT = 4;
    public static final int DELEGATEALERTED  = 5;
    public static final int DELEGATECONNECTING=6;
    public static final int NORMALEND = 12;
    private static BroadcastReceiver sMtcCallIncomingReceiver;
    private static BroadcastReceiver sMtcCallReferInReceiver;
    private static BroadcastReceiver sMtcCallOutgoingReceiver;
    private static BroadcastReceiver sMtcCallAlertedReceiver;
    private static BroadcastReceiver sMtcCallConnectingReceiver;
    private static BroadcastReceiver sMtcCallTalkingReceiver;
    private static BroadcastReceiver sMtcCallTermedReceiver;
    private static BroadcastReceiver mMtcCallDidTermReceiver;
    private static BroadcastReceiver sMtcCallNetworkStatusChangedReceiver;
    private static BroadcastReceiver sMtcCallInfoReceivedReceiver;
    private static BroadcastReceiver sMtcCallVideoReceiveStatusChangedReceiver;
    private static BroadcastReceiver sMtcLogoutedReceiver;
    private static BroadcastReceiver sMtcDidLogoutReceiver;

    public MtcPalmcallDelegate() {
    }

    public static void init(Context context) {
        MtcResource.init(context);
        sContext = context;
        sCallListener = new MtcPalmcallDelegate.CallListener();
        sTelephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        sTelephonyManager.listen(sCallListener, 32);
        MtcMdm.Mtc_MdmAnSetBitrateMode(2);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
        if(sMtcCallIncomingReceiver == null) {
            sMtcCallIncomingReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    PalmchatLogUtils.i(TAG,"-------------------MtcCallIncomingNotification-------------");
                    boolean dwCallId = true;
                    int dwCallId1;
                    String extra_info;
                    try {
                        extra_info = intent.getStringExtra("extra_info");
                        JSONObject json = (JSONObject)(new JSONTokener(extra_info)).nextValue();
                        dwCallId1 = json.getInt("MtcCallIdKey");
                    } catch (Exception var6) {
                        var6.printStackTrace();
                        return;
                    }

                    JSONObject json = null;
                    try{
                        json  = (JSONObject) new JSONTokener(extra_info).nextValue();
                    } catch (Exception e){
                        PalmchatLogUtils.e(TAG,"-----JSONObject---Exception--------"+e.toString());
                        e.printStackTrace();
                    }

                    if(json==null){
                        ToastManager.getInstance().show(PalmchatApp.getApplication().getApplicationContext(),"====json==null");
                        PalmchatLogUtils.e(TAG,"-----json==null---");
                        return;
                    }
                    JustalkDataInfo justalkDataInof = new JustalkDataInfo();
                    try {
                        JSONObject obj = new JSONObject(json.getString(MtcCallConstants.MtcCallUserDataKey));
                        justalkDataInof.setAFAfIdKey(obj.optString(JustalkDataInfo.AFAFIDKEY));
                        justalkDataInof.setAFNameKey(obj.optString(JustalkDataInfo.AFNAMEKEY));
                        justalkDataInof.setAFAgeKey(obj.optInt(JustalkDataInfo.AFAGEKEY));
                        justalkDataInof.setAFSexKey(obj.optInt(JustalkDataInfo.AFSEXKEY));
                        justalkDataInof.setAFCoverUrlKey(obj.optString(JustalkDataInfo.AFCOVERURLKEY));
                        justalkDataInof.setAFMediaUrlKey(obj.optString(JustalkDataInfo.AFMEDIAURLKEY));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    PalmchatLogUtils.i(TAG,"----------MtcCallIncomingNotification----INFO--------------------"+justalkDataInof.toString());
                    PalmcallService.incomingStart(dwCallId1,justalkDataInof);

                }
            };
            broadcastManager.registerReceiver(sMtcCallIncomingReceiver, new IntentFilter(MtcCallConstants.MtcCallIncomingNotification));
        }

        if(sMtcCallReferInReceiver == null) {
            sMtcCallReferInReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    PalmchatLogUtils.i(TAG,"-------------------MtcCallReferInNotification-------------");
                    boolean callId = true;
                    boolean isVideo = false;

                    int callId1;
                    try {
                        String callback = intent.getStringExtra("extra_info");
                        JSONObject json = (JSONObject)(new JSONTokener(callback)).nextValue();
                        callId1 = json.getInt("MtcCallIdKey");
                        isVideo = json.getBoolean("MtcCallIsVideoKey");
                    } catch (Exception var7) {
                        var7.printStackTrace();
                        return;
                    }

                    MtcPalmcallDelegate.Callback callback1 = MtcPalmcallDelegate.getCallback();
//                    if(callback1 != null) {
//                        callback1.mtcCallDelegateReferIn(callId1, isVideo);
//                    } else {
//                        MtcPalmcallDelegate.callReferIn(callId1, isVideo);
//                    }
                }
            };
            broadcastManager.registerReceiver(sMtcCallReferInReceiver, new IntentFilter(MtcCallConstants.MtcCallReferInNotification));
        }

        if(sMtcCallOutgoingReceiver == null) {
            sMtcCallOutgoingReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    PalmchatLogUtils.i(TAG,"-------------------MtcCallOutgoingNotification-------------");
                    boolean dwCallId = true;
                    int dwCallId1;
                    try {
                        String callback = intent.getStringExtra("extra_info");
                        JSONObject json = (JSONObject)(new JSONTokener(callback)).nextValue();
                        dwCallId1 = json.getInt("MtcCallIdKey");
                    } catch (Exception var6) {
                        var6.printStackTrace();
                        return;
                    }

                    MtcPalmcallDelegate.Callback callback1 = MtcPalmcallDelegate.getCallback();
                    if(callback1 != null) {
                        callback1.mtcCallDelegateOutgoing(dwCallId1);
                    }

                }
            };
            broadcastManager.registerReceiver(sMtcCallOutgoingReceiver, new IntentFilter(MtcCallConstants.MtcCallOutgoingNotification));
        }

        if(sMtcCallAlertedReceiver == null) {
            sMtcCallAlertedReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    boolean dwCallId = true;
                    boolean dwAlertType = true;
                    PalmchatLogUtils.i(TAG,"-------------------MtcCallAlertedNotification-------------");
                    int dwCallId1;
                    int dwAlertType1;
                    try {
                        String callback = intent.getStringExtra("extra_info");
                        JSONObject json = (JSONObject)(new JSONTokener(callback)).nextValue();
                        dwCallId1 = json.getInt("MtcCallIdKey");
                        dwAlertType1 = json.getInt("MtcCallAlertTypeKey");
                    } catch (Exception var7) {
                        var7.printStackTrace();
                        return;
                    }

                    MtcPalmcallDelegate.Callback callback1 = MtcPalmcallDelegate.getCallback();
                    if(callback1 != null) {
                        callback1.mtcCallDelegateAlerted(dwCallId1, dwAlertType1);
                    }

                }
            };
            broadcastManager.registerReceiver(sMtcCallAlertedReceiver, new IntentFilter(MtcCallConstants.MtcCallAlertedNotification));
        }

        if(sMtcCallConnectingReceiver == null) {
            sMtcCallConnectingReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    boolean dwCallId = true;
                    PalmchatLogUtils.i(TAG,"-------------------MtcCallConnectingNotification-------------");
                    int dwCallId1;
                    try {
                        String callback = intent.getStringExtra("extra_info");
                        JSONObject json = (JSONObject)(new JSONTokener(callback)).nextValue();
                        dwCallId1 = json.getInt("MtcCallIdKey");
                    } catch (Exception var6) {
                        var6.printStackTrace();
                        return;
                    }

                    MtcPalmcallDelegate.Callback callback1 = MtcPalmcallDelegate.getCallback();
                    if(callback1 != null) {
                        callback1.mtcCallDelegateConnecting(dwCallId1);
                    }

                }
            };
            broadcastManager.registerReceiver(sMtcCallConnectingReceiver, new IntentFilter(MtcCallConstants.MtcCallConnectingNotification));
        }

        if(sMtcCallTalkingReceiver == null) {
            sMtcCallTalkingReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    boolean dwCallId = true;

                    int dwCallId1;
                    try {
                        String callback = intent.getStringExtra("extra_info");
                        JSONObject json = (JSONObject)(new JSONTokener(callback)).nextValue();
                        dwCallId1 = json.getInt("MtcCallIdKey");
                    } catch (Exception var6) {
                        var6.printStackTrace();
                        return;
                    }

                    PalmchatLogUtils.i(TAG,"-------------------MtcCallTalkingNotification-------------");
                    MtcPalmcallDelegate.Callback callback1 = MtcPalmcallDelegate.getCallback();
                    if(callback1 != null) {
                        callback1.mtcCallDelegateTalking(dwCallId1);
                    }
                }
            };
            broadcastManager.registerReceiver(sMtcCallTalkingReceiver, new IntentFilter(MtcCallConstants.MtcCallTalkingNotification));
        }

        if(sMtcCallTermedReceiver == null) {
            sMtcCallTermedReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    boolean dwCallId = true;
                    boolean dwStatusCode = true;
                    String pcReason = null;

                    int dwCallId1;
                    int dwStatusCode1;
                    try {
                        String callback = intent.getStringExtra("extra_info");
                        JSONObject intentCallActivity = (JSONObject)(new JSONTokener(callback)).nextValue();
                        dwCallId1 = intentCallActivity.getInt("MtcCallIdKey");
                        dwStatusCode1 = intentCallActivity.getInt("MtcCallStatusCodeKey");
                        pcReason = intentCallActivity.optString("MtcCallDescriptionKey", (String)null);
                    } catch (Exception var8) {
                        var8.printStackTrace();
                        return;
                    }

                    if(MtcPalmcallDelegate.sPendingCall == null || !MtcPalmcallDelegate.sPendingCall.remove(Integer.valueOf(dwCallId1))) {
                        MtcPalmcallDelegate.Callback callback1 = MtcPalmcallDelegate.getCallback();
                        if(callback1 != null) {
                            callback1.mtcCallDelegateTermed(dwCallId1, dwStatusCode1, pcReason);
                        } else {
                            Intent intentCallActivity1 = new Intent(MtcPalmcallDelegate.sContext, MtcPalmcallDelegate.sCallActivityClass);
                            intentCallActivity1.addFlags(872415232);
                            intentCallActivity1.putExtra(TERMED, true);
                            intentCallActivity1.putExtra(CALL_ID, dwCallId1);
                            intentCallActivity1.putExtra(STAT_CODE, dwStatusCode1);
                            intentCallActivity1.putExtra(TERMED_REASON, pcReason);
                            MtcPalmcallDelegate.sContext.startActivity(intentCallActivity1);
                        }

                        int callType = AfPalmCallResp.AFMOBI_CALL_TYPE_UNKOWN;
                        AfPalmCallResp.AfPalmCallRecord curIncomingRecord = PalmcallManager.getInstance().getCurRecord();
                        long talkedTime = PalmcallManager.getInstance().getTalkedTime(dwCallId1);
                        if(curIncomingRecord!=null){
                            callType = curIncomingRecord.callType;
                            EventBus.getDefault().post(new EventPalmcallNotication(MtcCallConstants.MtcCallTermedNotification,callType,dwCallId1,talkedTime));
                        } else {
                            EventBus.getDefault().post(new EventPalmcallNotication(MtcCallConstants.MtcCallTermedNotification,callType,dwCallId1,talkedTime));
                        }
                        if(callType==AfPalmCallResp.AFMOBI_CALL_TYPE_MISSED){
                            SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).setMissedCalls(true);
                        }

                        PalmchatLogUtils.i(TAG,"------MtcCallConstants.MtcCallTermedNotification-----对方挂断----");


                    }
                }
            };
            broadcastManager.registerReceiver(sMtcCallTermedReceiver, new IntentFilter(MtcCallConstants.MtcCallTermedNotification));
        }

        if(sMtcCallNetworkStatusChangedReceiver == null) {
            sMtcCallNetworkStatusChangedReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    boolean dwCallId = true;
                    boolean bVideo = false;
                    boolean bSend = false;
                    boolean iStatus = false;

                    int dwCallId1;
                    int iStatus1;
                    try {
                        String callback = intent.getStringExtra("extra_info");
                        JSONObject json = (JSONObject)(new JSONTokener(callback)).nextValue();
                        dwCallId1 = json.getInt("MtcCallIdKey");
                        bVideo = json.getBoolean("MtcCallIsVideoKey");
                        bSend = json.getBoolean("MtcCallIsSendKey");
                        iStatus1 = json.getInt("MtcCallNetworkStatusKey");
                    } catch (Exception var9) {
                        var9.printStackTrace();
                        return;
                    }

                    MtcPalmcallDelegate.Callback callback1 = MtcPalmcallDelegate.getCallback();
                    if(callback1 != null) {
                        callback1.mtcCallDelegateNetStaChanged(dwCallId1, bVideo, bSend, iStatus1);
                    }
                    PalmchatLogUtils.i(TAG,"------MtcCallConstants.MtcCallDidTermNotification-----MtcCallNetworkStatusChangedNotification----");

                }
            };
            broadcastManager.registerReceiver(sMtcCallNetworkStatusChangedReceiver, new IntentFilter("MtcCallNetworkStatusChangedNotification"));
        }

        if(sMtcCallInfoReceivedReceiver == null) {
            sMtcCallInfoReceivedReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    boolean dwCallId = true;
                    String pcInfo = null;

                    int dwCallId1;
                    try {
                        String callback = intent.getStringExtra("extra_info");
                        JSONObject json = (JSONObject)(new JSONTokener(callback)).nextValue();
                        dwCallId1 = json.getInt("MtcCallIdKey");
                        pcInfo = json.getString("MtcCallBodyKey");
                    } catch (Exception var7) {
                        var7.printStackTrace();
                        return;
                    }

                    MtcPalmcallDelegate.Callback callback1 = MtcPalmcallDelegate.getCallback();
                    if(callback1 != null) {
                        callback1.mtcCallDelegateInfo(dwCallId1, pcInfo);
                    }

                    PalmchatLogUtils.i(TAG,"------MtcCallConstants.MtcCallDidTermNotification-----MtcCallInfoReceivedNotification----");

                }
            };
            broadcastManager.registerReceiver(sMtcCallInfoReceivedReceiver, new IntentFilter("MtcCallInfoReceivedNotification"));
        }

        if(sMtcCallVideoReceiveStatusChangedReceiver == null) {
            sMtcCallVideoReceiveStatusChangedReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    boolean dwCallId = true;
                    boolean dwStatus = true;

                    int dwCallId1;
                    int dwStatus1;
                    try {
                        String callback = intent.getStringExtra("extra_info");
                        JSONObject json = (JSONObject)(new JSONTokener(callback)).nextValue();
                        dwCallId1 = json.getInt("MtcCallIdKey");
                        dwStatus1 = json.getInt("MtcCallVideoStatusKey");
                    } catch (Exception var7) {
                        var7.printStackTrace();
                        return;
                    }

                    MtcPalmcallDelegate.Callback callback1 = MtcPalmcallDelegate.getCallback();
                    if(callback1 != null) {
                        callback1.mtcCallDelegateVideoReceiveStaChanged(dwCallId1, dwStatus1);
                    }

                }
            };
            broadcastManager.registerReceiver(sMtcCallVideoReceiveStatusChangedReceiver, new IntentFilter("MtcCallVideoReceiveStatusChangedNotification"));
        }

        if(sMtcLogoutedReceiver == null) {
            sMtcLogoutedReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    MtcPalmcallDelegate.Callback callback = MtcPalmcallDelegate.getCallback();
                    if(callback != null) {
                        callback.mtcCallDelegateLogouted();
                    }
                    PalmchatLogUtils.i(TAG,"------MtcCallConstants.MtcCallDidTermNotification-----MtcLogoutedNotification----");
                }
            };
            broadcastManager.registerReceiver(sMtcLogoutedReceiver, new IntentFilter("MtcLogoutedNotification"));
        }

        if(sMtcDidLogoutReceiver == null) {
            sMtcDidLogoutReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    PalmchatLogUtils.i(TAG,"------MtcCallConstants.MtcCallDidTermNotification-----MtcDidLogoutNotification----");
                }
            };
            broadcastManager.registerReceiver(sMtcDidLogoutReceiver, new IntentFilter("MtcDidLogoutNotification"));
        }

        if(mMtcCallDidTermReceiver==null){
            mMtcCallDidTermReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int dwCallId1;
                    try {
                        String callback = intent.getStringExtra("extra_info");
                        JSONObject intentCallActivity = (JSONObject)(new JSONTokener(callback)).nextValue();
                        dwCallId1 = intentCallActivity.getInt("MtcCallIdKey");
                    } catch (Exception var8) {
                        var8.printStackTrace();
                        return;
                    }
                    int callType = AfPalmCallResp.AFMOBI_CALL_TYPE_UNKOWN;
                    AfPalmCallResp.AfPalmCallRecord curIncomingRecord = PalmcallManager.getInstance().getCurRecord();
                    long talkedTime = PalmcallManager.getInstance().getTalkedTime(dwCallId1);
                    if(curIncomingRecord!=null){
                        callType = curIncomingRecord.callType;
                        EventBus.getDefault().post(new EventPalmcallNotication(MtcCallConstants.MtcCallDidTermNotification,callType,dwCallId1,talkedTime));
                    } else {
                        EventBus.getDefault().post(new EventPalmcallNotication(MtcCallConstants.MtcCallDidTermNotification,callType,dwCallId1,talkedTime));
                    }
                    if(callType==AfPalmCallResp.AFMOBI_CALL_TYPE_MISSED){
                        SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).setMissedCalls(true);
                    }
                    PalmchatLogUtils.i(TAG,"------MtcCallConstants.MtcCallDidTermNotification-----自己挂断----");
                }
            };
            broadcastManager.registerReceiver(mMtcCallDidTermReceiver, new IntentFilter(MtcCallConstants.MtcCallDidTermNotification));
        }

    }

    public static void destroy() {
        if(sTelephonyManager != null) {
            sTelephonyManager.listen((PhoneStateListener)null, 32);
            sCallListener = null;
            sTelephonyManager = null;
        }

    }

    private static void callIncoming(int dwCallId) {
        Intent intentCallActivity = new Intent(sContext, sCallActivityClass);
        intentCallActivity.addFlags(872415232);
        intentCallActivity.putExtra(VIDEO, MtcCall.Mtc_CallPeerOfferVideo(dwCallId));
        intentCallActivity.putExtra(CALL_ID, dwCallId);
        sContext.startActivity(intentCallActivity);
    }

    private static void callIncoming(int dwCallId,String extra_info) {
        Intent intentCallActivity = new Intent(sContext, sCallActivityClass);
        intentCallActivity.addFlags(872415232);
        intentCallActivity.putExtra(VIDEO, MtcCall.Mtc_CallPeerOfferVideo(dwCallId));
        intentCallActivity.putExtra(CALL_ID, dwCallId);
        intentCallActivity.putExtra(MtcApi.EXTRA_INFO,extra_info);
        sContext.startActivity(intentCallActivity);
    }

    private static void callReferIn(int callId, boolean isVideo) {
        Intent intent = new Intent(sContext, sCallActivityClass);
        intent.addFlags(872415232);
        intent.putExtra(VIDEO, isVideo);
        intent.putExtra(CALL_ID, callId);
        intent.putExtra(IS_CALL_REFERIN, true);
        sContext.startActivity(intent);
    }

    public static void setCallback(MtcPalmcallDelegate.Callback callback) {
        sCallback = callback == null?null:new WeakReference(callback);
        if(callback == null && sPendingCall != null) {
            Iterator i$ = sPendingCall.iterator();

            while(i$.hasNext()) {
                int dwCallId = ((Integer)i$.next()).intValue();
                callIncoming(dwCallId);
            }

            sPendingCall = null;
        }
    }

    public static MtcPalmcallDelegate.Callback getCallback() {
        return sCallback == null?null:(MtcPalmcallDelegate.Callback)sCallback.get();
    }


    public static void setViewReponseCallBack(PalmcallViewReponseListener callBack) {
        sServiceback = new WeakReference<PalmcallViewReponseListener>(callBack);
    }

    public static PalmcallViewReponseListener getViewReponseCallBack(){
        return sServiceback == null?null:(PalmcallViewReponseListener)sServiceback.get();
    }

    public static MtcPalmcallDelegate.Callback getActiveCallback() {
        MtcPalmcallDelegate.Callback callback = getCallback();
        if(callback != null) {
            Activity activity = (Activity)callback;
            if(activity.isFinishing()) {
                callback = null;
            }
        }

        return callback;
    }

    public static void setCallActivityClass(Class<?> cls) {
        sCallActivityClass = cls;
    }

    public static void call(String number, String peerDisplayName,String peerHeadUrl,byte peer_sex, boolean isVideo, Map<String, String> userData,long leftTime) {
        if(FloatWindowService.sIsShow) {
            Toast.makeText(sContext, "Now in the calling...", 0).show();
        } else {
            Intent intent = new Intent(sContext, sCallActivityClass);
            intent.addFlags(872415232);
            intent.putExtra(VIDEO, isVideo);
            intent.putExtra(NUMBER, number);
            intent.putExtra(PEER_DISPLAY_NAME, peerDisplayName);
            intent.putExtra(PEER_HEADURL,peerHeadUrl);
            intent.putExtra(PEER_SEX,peer_sex);
            intent.putExtra(LEFT_TIME,leftTime);
            if(userData != null && userData.containsKey(MtcCallConstants.MtcCallInfoUserDataKey)) {
                intent.putExtra(MtcCallConstants.MtcCallInfoUserDataKey, (String)userData.get(MtcCallConstants.MtcCallInfoUserDataKey));
            }
            sContext.startActivity(intent);
        }
    }

    public static void termAll() {
        MtcPalmcallDelegate.Callback callback = sCallback == null?null:(MtcPalmcallDelegate.Callback)sCallback.get();
        if(callback != null) {
            callback.mtcCallDelegateTermAll();
        }

    }

    public static boolean isCalling() {
        MtcPalmcallDelegate.Callback callback = getCallback();
        return callback != null?callback.mtcCallDelegateIsCalling():false;
    }

    public static boolean isExisting(String number) {
        MtcPalmcallDelegate.Callback callback = getCallback();
        return callback != null?callback.mtcCallDelegateIsExisting(number):false;
    }

    public static int getCallId() {
        MtcPalmcallDelegate.Callback callback = getCallback();
        return callback != null?callback.mtcCallDelegateGetCallId():-1;
    }

    public static boolean isInPhoneCall() {
        return sIsInPhoneCall;
    }

    public static String getStateString(Context context, int state, boolean isVideo, boolean forShort) {
        switch(state) {
            case 1:
                if(forShort) {
                    return context.getString(MtcResource.getIdByName("string", "Incoming"));
                } else {
                    if(isVideo) {
                        return context.getString(MtcResource.getIdByName("string", "Video_incoming"));
                    }

                    return context.getString(MtcResource.getIdByName("string", "Voice_incoming"));
                }
            case 2:
                //return context.getString(MtcResource.getIdByName("string", "Answering"));
                return context.getString(MtcResource.getIdByName("string", "Connecting"));
            case 3:
                return context.getString(MtcResource.getIdByName("string", "Calling"));
            case 4:
            case 8:
            case 10:
            default:
                return "";
            case 5:
                return context.getString(MtcResource.getIdByName("string", "Calling"));
                //return context.getString(MtcResource.getIdByName("string", "Ringing"));
            case 6:
                return context.getString(MtcResource.getIdByName("string", "Connecting"));
            case 7:
                return context.getString(MtcResource.getIdByName("string", "Talking"));
            case 9:
                return context.getString(MtcResource.getIdByName("string", "Paused"));
            case 11:
            case 12:
                return context.getString(MtcResource.getIdByName("string", "Call_ended"));
        }
    }

    private static class CallListener extends PhoneStateListener {
        private CallListener() {
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            if(2 != state && 1 != state) {
                if(0 != state) {
                    return;
                }

                if(!MtcPalmcallDelegate.sIsInPhoneCall) {
                    return;
                }

                MtcPalmcallDelegate.sIsInPhoneCall = false;
            } else {
                MtcPalmcallDelegate.sIsInPhoneCall = true;
            }

            MtcPalmcallDelegate.Callback callback = MtcPalmcallDelegate.getCallback();
            if(callback != null) {
                if(MtcPalmcallDelegate.sIsInPhoneCall) {
                    callback.mtcCallDelegatePhoneCallBegan();
                } else {
                    callback.mtcCallDelegatePhoneCallEnded();
                }

            }
        }
    }

    public interface Callback extends MtcPalmcallDelegate.State, MtcPalmcallDelegate.Info {
        void mtcCallDelegateIncoming(int var1);

        void mtcCallDelegateReferIn(int var1, boolean var2);

        void mtcCallDelegateCall(String var1,String var3, boolean var4);

        void mtcCallDelegateOutgoing(int var1);

        void mtcCallDelegateAlerted(int var1, int var2);

        void mtcCallDelegateConnecting(int var1);

        void mtcCallDelegateTalking(int var1);

        void mtcCallDelegateTermed(int var1, int var2, String var3);

        void mtcCallDelegateTermAll();

        void mtcCallDelegateLogouted();

        void mtcCallDelegateStartPreview();

        void mtcCallDelegateStartVideo(int var1);

        void mtcCallDelegateStopVideo(int var1);

        void mtcCallDelegateNetStaChanged(int var1, boolean var2, boolean var3, int var4);

        void mtcCallDelegateInfo(int var1, String var2);

        void mtcCallDelegateVideoReceiveStaChanged(int var1, int var2);

        void mtcCallDelegatePhoneCallBegan();

        void mtcCallDelegatePhoneCallEnded();

        boolean mtcCallDelegateIsCalling();

        boolean mtcCallDelegateIsExisting(String var1);

        int mtcCallDelegateGetCallId();

    }

    public interface Info {
        String CALL_INFO_CALL_PAUSE = "Call Pause";
        String CALL_INFO_CALL_INTERRUPT = "Call Interrupt";
        String CALL_INFO_CALL_RESUME = "Call Resume";
        String CALL_INFO_VIDEO_PAUSE = "Video Pause";
        String CALL_INFO_VIDEO_RESUME = "Video Resume";
        String CALL_INFO_VIDEO_OFF = "Video Off";
        String CALL_INFO_VIDEO_ON = "Video On";
    }

    public interface State {
        int CALL_STATE_NONE = 0;
        int CALL_STATE_INCOMING = 1;
        int CALL_STATE_ANSWERING = 2;
        int CALL_STATE_CALLING = 3;
        int CALL_STATE_OUTGOING = 4;
        int CALL_STATE_ALERTED_RINGING = 5;
        int CALL_STATE_CONNECTING = 6;
        int CALL_STATE_TALKING = 7;
        int CALL_STATE_TIMING = 8;
        int CALL_STATE_PAUSED = 9;
        int CALL_STATE_DISCONNECTED = 10;
        int CALL_STATE_TERM_RINGING = 11;
        int CALL_STATE_ENDING = 12;
        int CALL_STATE_DECLINING = 13;
        int CALL_FAIL_CALL = -1;
        int CALL_FAIL_ANSWER = -2;
        int CALL_FAIL_CALL_DISCONNECTED = -3;
        int CALL_FAIL_CALL_PICKUPX = -4;
        int CALL_FAIL_AUDIO_DEVICE = -5;
        int CALL_FAIL_CALL_AUDIO_INIT = -6;
        int CALL_FAIL_ANSWER_AUDIO_INIT = -7;
    }
}
