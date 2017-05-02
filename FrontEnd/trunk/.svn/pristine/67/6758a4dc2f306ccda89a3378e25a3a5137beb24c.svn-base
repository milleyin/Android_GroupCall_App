package com.afmobi.palmchat.ui.activity.palmcall;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.text.TextUtils;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.eventbusmodel.PallcallLoginEvent;
import com.afmobi.palmchat.eventbusmodel.UpdatePalmCallDurationEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.palmcall.model.JustalkDataInfo;
import com.afmobi.palmchat.ui.activity.payment.MyWapActivity;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfLoginInfo;
import com.core.AfPalmCallResp;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.justalk.cloud.lemon.MtcCallConstants;
import com.justalk.cloud.lemon.MtcCli;
import com.justalk.cloud.lemon.MtcCliConstants;
import com.justalk.cloud.lemon.MtcUtil;
import com.justalk.cloud.lemon.MtcVer;
import com.justalk.cloud.zmf.Zmf;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
/**
 * 调用拨打语音电话接口是，
 * 1.检测自己的账户在菊风服务器是否是登陆状态，不是的话先登录
 * 2.检查自己的palmcall个人信息本地是否有，没有的话网络请求更新
 * 3.检查对方是否在线，同时下发自己的可通话时长
 * 4.确定以上都ok后调用菊风的通话接口
 */

/**
 * 封装下palmcall 登陆
 * Created by Transsion on 2016/7/8.
 */
public class PalmcallManager implements PalmcallLoginDelegate.InitStat,PalmcallLoginDelegate.Callback, AfHttpResultListener {
    /**tag*/
    private final String TAG = PalmcallManager.class.getSimpleName();
    /**上下文环境*/
    private Context mContext;
    /**线程池*/
    private ScheduledThreadPoolExecutor mBackgroundExecutor;
    /**登陆判断*/
    private boolean  mIsDoLogin = false;
    /**单例*/
    private volatile static PalmcallManager mPalmcallLoginManager;
    /**自己的语音简介地址*/
    private String mSelfMediaUrl = "";
    /**对方详细信息*/
    private volatile AfPalmCallResp.AfPalmCallHotListItem mCurAfPalmCallHotListItem;
    /**因为incoming需要跟新状态，所以需要积累个缓存保存_id*/
    private AfPalmCallResp.AfPalmCallRecord mCurIncomingRecord;
    /**可拨打时间*/
    private long mAvaiableCallTime;
    /**被叫方的信息*/
    private AfPalmCallResp.AfPalmCallHotListItem mPeerListItem;
    /**主叫方的信息*/
    private AfPalmCallResp.AfPalmCallHotListItem mSelfListItem;
    /**去拨打电话状态*/
    private boolean mBL_toCall;
    /**状态回调*/
    private CallSomeOneStateListener mCallSomeOneStateListener;
    /**是否取消通话标记*/
    private boolean isCancelCall;
    /**请求剩余时间的http id*/
    private int mCurHttpId = -1;
    /**充值url*/
    private String mRechargeUrl;
    private HashMap<Integer,Long> mHashMapTalkedTime;
    /***/
    private boolean mIsSupportPalmcall = false;
    /***/
    private boolean mIsPalmcalling = false;
    /**
     * 构造方法
     */
    private PalmcallManager(){
        mContext = PalmchatApp.getApplication().getApplicationContext();
        mBackgroundExecutor = new ScheduledThreadPoolExecutor(1);
    }

    public static PalmcallManager getInstance() {
        if(null==mPalmcallLoginManager){
            synchronized(PalmcallManager.class){ //双重检验锁
                if(null==mPalmcallLoginManager){
                    mPalmcallLoginManager = new PalmcallManager();
                }
            }
        }
        return mPalmcallLoginManager;
    }

    /**
     * justcall初始化
     */
    public void justCallInit() {
        if(!PalmchatLogUtils.USE_PALMCALL) {
            return;
        }
        PalmcallLoginDelegate.setCallback(this);
        if (PalmcallLoginDelegate.init(mContext, mContext.getString(R.string.JusTalkCloud_AppKey)) != MTC_INIT_SUCCESS) {
            ToastManager.getInstance().show(mContext,R.string.justtalk_initerror);
            PalmchatLogUtils.i(TAG,"--PalmcallLoginDelegate.init--faile--");
            return;
        }
        //通话模块
        MtcPalmcallDelegate.init(mContext);
        MtcPalmcallDelegate.setCallActivityClass(PalmcallActivity.class);

        //下面的还没搞懂作用
        String avatarVer = MtcVer.Mtc_GetAvatarVersion();
        String lemonVer = MtcVer.Mtc_GetLemonVersion();
        String melonVer = MtcVer.Mtc_GetMelonVersion();
        String mtcVer = MtcVer.Mtc_GetVersion();
        String zmfVer = Zmf.getVersion();
        PalmchatLogUtils.i(TAG,"-----avatarVer-------"+avatarVer);
        PalmchatLogUtils.i(TAG,"-----lemonVer-------"+lemonVer);
        PalmchatLogUtils.i(TAG,"-----melonVer-------"+melonVer);
        PalmchatLogUtils.i(TAG,"-----mtcVer-------"+mtcVer);
        PalmchatLogUtils.i(TAG,"-----zmfVer-------"+zmfVer);
    }

    /**
     * 登陆palmcall
     */
    public void loginPalmchatCall(long delayTime){
        if(!PalmchatLogUtils.USE_PALMCALL) {
            PalmchatLogUtils.i(TAG,"--------PalmchatLogUtils.USE_PALMCALL-----FALSE-");
            return;
        }
        PalmchatLogUtils.i(TAG,"----loginPalmchatCall-----------cur thread id ---=========="+Thread.currentThread().getId());
        doLoginPalmcall();
       // mBackgroundExecutor.schedule(mPalmcallLogin,delayTime, TimeUnit.SECONDS);
    }

    /**
     * 执行登陆
     */
    private void doLoginPalmcall(){
        mIsDoLogin = false;
        PalmchatLogUtils.i(TAG,"-----palmcallId------=="+ CacheManager.getInstance().getMyProfile().justalkId);
        if(MtcCli.Mtc_CliGetState() == MtcCliConstants.EN_MTC_CLI_STATE_LOGINED){
            PalmchatLogUtils.i(TAG,"--MtcCliConstants.EN_MTC_CLI_STATE_LOGINED=");
            String palmcallId = CacheManager.getInstance().getMyProfile().justalkId;
            if((!TextUtils.isEmpty(palmcallId))&&(palmcallId.equals(SharePreferenceUtils.getInstance(mContext).getCurPalmcallId()))){
                PalmchatLogUtils.i(TAG,"-------palmcallId has logined ,no need to login-------");
                return;
            }
            mIsDoLogin = true;
            PalmcallLoginDelegate.logout();
        } else {
            PalmchatLogUtils.i(TAG,"-!-MtcCliConstants.EN_MTC_CLI_STATE_LOGINED=");
            loginPalmcall();
        }
    }

    /**
     * palmcall 登出
     */
    public void logoutPalmcall(){
        if(!PalmchatLogUtils.USE_PALMCALL) {
            PalmchatLogUtils.i(TAG,"--------PalmchatLogUtils.USE_PALMCALL-----FALSE-");
            return;
        }
        PalmcallLoginDelegate.logout();
    }

    public void destroy(){
        if(null!=mBackgroundExecutor){
            mBackgroundExecutor.shutdownNow();
        }
    }

    /**
     * 外部调用接口
     * @param peerListItem
     */
    public void palmcallCall (AfPalmCallResp.AfPalmCallHotListItem peerListItem){
        isCancelCall = false;
        mPeerListItem = peerListItem;
        if(!NetworkUtils.isNetworkAvailable(mContext)){
            ToastManager.getInstance().show(mContext, mContext.getString(R.string.network_unavailable));
            return;
        }
        if(mCallSomeOneStateListener!=null){
            mCallSomeOneStateListener.progressing();
        }
        if(MtcCli.Mtc_CliGetState() != MtcCliConstants.EN_MTC_CLI_STATE_LOGINED){
            mBL_toCall = true;
            loginPalmchatCall(DefaultValueConstant.PALMCLAL_LOGIN_DURATION);
        } else {
            mBL_toCall = false;
            String afId = CacheManager.getInstance().getMyProfile().afId;
            AfPalmCallResp afPalmCallRespSelf = PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallInfoGet(afId);
            if(afPalmCallRespSelf==null){
                PalmchatApp.getApplication().mAfCorePalmchat.AfHttpPalmCallGetInfo(afId,null,this);
            } else {
                doCallWidthThirdSdk(afPalmCallRespSelf,peerListItem);
            }
        }

    }


    private void justalkCall(AfPalmCallResp.AfPalmCallHotListItem afPalmCallHotListItemSelf,AfPalmCallResp.AfPalmCallHotListItem listItem,long leftTime){
        mCurAfPalmCallHotListItem = listItem;
        Map<String,String> extraMapInfo =  new HashMap<String,String>();
        Map<String,Object> justalkDataMap = new HashMap<>();
        justalkDataMap.put(JustalkDataInfo.AFAFIDKEY,CacheManager.getInstance().getMyProfile().afId);
        justalkDataMap.put(JustalkDataInfo.AFNAMEKEY,afPalmCallHotListItemSelf.name);
        justalkDataMap.put(JustalkDataInfo.AFAGEKEY,afPalmCallHotListItemSelf.age);
        justalkDataMap.put(JustalkDataInfo.AFSEXKEY,afPalmCallHotListItemSelf.sex);
        justalkDataMap.put(JustalkDataInfo.AFCOVERURLKEY,afPalmCallHotListItemSelf.coverUrl);
        justalkDataMap.put(JustalkDataInfo.AFMEDIAURLKEY,afPalmCallHotListItemSelf.mediaDescUrl);
        JSONObject jsonObject =  new JSONObject(justalkDataMap);
        extraMapInfo.put(MtcCallConstants.MtcCallInfoUserDataKey,jsonObject.toString());
        byte peerSex = Consts.AFMOBI_SEX_FEMALE;
        try {
            peerSex = (byte)listItem.sex;
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!isCancelCall){
            JustalkDataInfo justalkDataInfo = new JustalkDataInfo();
            justalkDataInfo.setAFAfIdKey(listItem.afid);
            justalkDataInfo.setAFSexKey(peerSex);
            justalkDataInfo.setAFNameKey(listItem.name);
            justalkDataInfo.setAFCoverUrlKey(listItem.coverUrl);
           // MtcPalmcallDelegate.call(listItem.afid,listItem.name,listItem.coverUrl,peerSex,false,extraMapInfo,leftTime);
            PalmcallService.outgoingStart(justalkDataInfo,extraMapInfo,leftTime);
        } else {
            isCancelCall = false;
        }
        if(mCallSomeOneStateListener!=null){
            mCallSomeOneStateListener.progressend(true);
        }

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
                mSelfListItem  =afPalmCallHotListItem;
                mCurHttpId =  PalmchatApp.getApplication().mAfCorePalmchat.AfHttpPalmCallMakeCall(listItem.afid,null,this);
                //调用界面完成方法
                //finish();
            } else {
                PalmchatLogUtils.i(TAG,"---------------------null==afPalmCallHotListItem--------");
                ToastManager.getInstance().show(mContext,mContext.getString(R.string.userself_infoupdate_fail));
            }
        }
    }

    /**
     * 登陆palmcall
     */
    private void loginPalmcall(){
        String palmcallId = CacheManager.getInstance().getMyProfile().afId;
        String passWord = Constants.FACEBOOK_PASSWORD;
        String justCallServer = mContext.getString(R.string.justcall_updateserver);
        String justCallNetWork = mContext.getString(R.string.justcall_updatenetwork);
        if(TextUtils.isEmpty(palmcallId)){
            PalmchatLogUtils.i(TAG,"---palmcallId======null");
            return;
        }
        if (!PalmcallLoginDelegate.login(palmcallId, passWord, justCallServer,justCallNetWork)) {
            //ToastManager.getInstance().show(this, R.string.justtalk_loginfailed);
            PalmchatLogUtils.i(TAG,mContext.getString(R.string.justtalk_loginfailed));
        } else {
            //ToastManager.getInstance().show(this, R.string.justtalk_loginok);
            PalmchatLogUtils.i(TAG,mContext.getString(R.string.justtalk_loginok));
        }
    }

    /**
     * 登陆账户的mediaurl
     * @param selfMediaUrl
     */
    public void setPalmcallSelfMediaUrl(String selfMediaUrl) {
        this.mSelfMediaUrl = selfMediaUrl;
    }

    @Override
    public void mtcLoginOk() {
        SharePreferenceUtils.getInstance(mContext).setCurPalmcallId();
        EventBus.getDefault().post(new PallcallLoginEvent());
        PalmchatLogUtils.i(TAG,CacheManager.getInstance().getMyProfile().afId+"---"+CacheManager.getInstance().getMyProfile().justalkId+"------------mtcLoginOk---------------");
        if (mBL_toCall){
            palmcallCall(mPeerListItem);
        }
    }

    @Override
    public void mtcLoginDidFail() {
        //	ToastManager.getInstance().show(this, R.string.justtalk_loginfailed);
        loginPalmchatCall(DefaultValueConstant.PALMCLAL_RELOGIN_DURATION_FAIL);
        PalmchatLogUtils.i(TAG,"----mtcLoginDidFail-----------cur thread id ---=========="+Thread.currentThread().getId());
        PalmchatLogUtils.i(TAG,CacheManager.getInstance().getMyProfile().afId+"---"+CacheManager.getInstance().getMyProfile().justalkId+"------------mtcLoginDidFail---------------");
    }

    @Override
    public void mtcLogoutOk() {
        PalmchatLogUtils.i(TAG,CacheManager.getInstance().getMyProfile().afId+"---"+SharePreferenceUtils.getInstance(mContext).getCurPalmcallId()+"------------mtcLogoutOk---------------");
        //closs palmchat下不用重登
        if( SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getIsClosePalmchat()){
            return;
        }
        if((!TextUtils.isEmpty(PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession()))&&isSupportPalmcall()){
            loginPalmchatCall(DefaultValueConstant.PALMCLAL_LOGIN_DURATION);
        }
    }

    @Override
    public void mtcLogouted() {
        PalmchatLogUtils.i(TAG,CacheManager.getInstance().getMyProfile().afId+"---"+CacheManager.getInstance().getMyProfile().justalkId+"------------mtcLogouted---------------");
        PalmchatLogUtils.i(TAG,"------------------------AfHttpGetServerSession------------------------"+PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession());
        PalmchatLogUtils.i(TAG,"------------------------isSupportPalmcall------------------------"+isSupportPalmcall());
        //closs palmchat下不用重登
        if( SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getIsClosePalmchat()){
            return;
        }
        if((!TextUtils.isEmpty(PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession()))&&isSupportPalmcall()){
            PalmchatLogUtils.i(TAG,"------------------------APPROVED--------RELOGIN----------------");
            loginPalmchatCall(DefaultValueConstant.PALMCLAL_LOGIN_DURATION);
        }
    }

    @Override
    public void mtcAuthRequire(String s, String s1) {

    }

    /**
     *
     * @param call_id
     * @param justalkDataInfo
     * @param callType
     */
    private synchronized void palmcallSaveIncomingToDb(int call_id, JustalkDataInfo justalkDataInfo, final int callType) {

        try{

            AfPalmCallResp.AfPalmCallRecord record = new AfPalmCallResp.AfPalmCallRecord();
            record.afId =justalkDataInfo.getAFAfIdKey();
            record.callId = call_id;
            record.callType = callType;
            record.mediaUrl = justalkDataInfo.getAFMediaUrlKey();
            record.callTime = System.currentTimeMillis();
            PalmchatLogUtils.i(TAG,"-----------------record.callTime--------Incoming-----------"+record.callTime);
            record._id = PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallRecordInsert(record);
            mCurIncomingRecord = record;
            AfPalmCallResp.AfPalmCallHotListItem  hotListItem= new AfPalmCallResp.AfPalmCallHotListItem();
            hotListItem.afid = justalkDataInfo.getAFAfIdKey();
            hotListItem.name = justalkDataInfo.getAFNameKey();
            hotListItem.age = justalkDataInfo.getAFAgeKey();
            hotListItem.sex = justalkDataInfo.getAFSexKey();
            hotListItem.coverUrl = justalkDataInfo.getAFCoverUrlKey();
            hotListItem.mediaDescUrl = justalkDataInfo.getAFMediaUrlKey();

            savaHotListItemToDb(hotListItem);

        } catch (Exception e){
            PalmchatLogUtils.e(TAG,"-----Exception------"+e.toString());
            e.printStackTrace();
        }
    }

    public void savaAfPalmCallRespToDb(final AfPalmCallResp afPalmCallHotListItem){
        if(null!=afPalmCallHotListItem){
            if(Thread.currentThread() == Looper.getMainLooper().getThread()){
                mBackgroundExecutor.schedule(new Runnable() {
                    @Override
                    public void run() {
                        savaHotListItemToDb(afPalmCallHotListItem);
                    }
                }, 0, TimeUnit.SECONDS);
            } else {
                savaHotListItemToDb(afPalmCallHotListItem);
            }

        }
    }

    private synchronized void savaHotListItemToDb(final AfPalmCallResp afPalmCallHotListItem){
        AfPalmCallResp.AfPalmCallHotListItem afPalmCallHotListItemE = null;
        try{
            afPalmCallHotListItemE = ((ArrayList<AfPalmCallResp.AfPalmCallHotListItem>)afPalmCallHotListItem.respobj).get(0);
            CommonUtils.splicePalmcallHotlistItemUrl(afPalmCallHotListItemE);
        } catch (Exception e){
            e.printStackTrace();
        }

        if(afPalmCallHotListItem!=null){
            if(PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallInfoGet(afPalmCallHotListItemE.afid)==null){
                PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallInfoInsert(afPalmCallHotListItemE);
            }else{
                PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallInfoUpdate(afPalmCallHotListItemE);
            }
        }
    }

    private void savaHotListItemToDb(final AfPalmCallResp.AfPalmCallHotListItem afPalmCallHotListItem){

        if(afPalmCallHotListItem!=null){
            if(PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallInfoGet(afPalmCallHotListItem.afid)==null){
                PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallInfoInsert(afPalmCallHotListItem);
            }else{
                PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallInfoUpdate(afPalmCallHotListItem);
            }
        }
    }

    /**
     * 更新来电状态
     * @param callType
     */
    private synchronized void palmcallUpdateIncomingToDb(int callType){
        if(mCurIncomingRecord!=null){
            mCurIncomingRecord.callType = callType;
            PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallRecordUpdate(mCurIncomingRecord);
            //mCurIncomingRecord = null;
        } else {
            //mCurIncomingRecord = null;
            PalmchatLogUtils.e(TAG,"-------getCurCallRecord-----NULL------call--id-----");
        }
    }

    /**
     * 保存去掉信息到数据库
     * @param call_id
     * @param callType
     */
    private synchronized void palmcallSaveOutgoingToDb(int call_id,int callType) {
        mCurIncomingRecord = null;
        if(mCurAfPalmCallHotListItem==null){
            PalmchatLogUtils.i(TAG,"-------palmcallSaveOutgoingToDb------mCurAfPalmCallHotListItem==null----return-");
            return;
        }

        try{
            AfPalmCallResp.AfPalmCallRecord record = new AfPalmCallResp.AfPalmCallRecord();
            record.afId =mCurAfPalmCallHotListItem.afid;
            record.callId = call_id;
            record.callType = callType;
            record.mediaUrl = mCurAfPalmCallHotListItem.mediaDescUrl;
            record.callTime = System.currentTimeMillis();
            PalmchatLogUtils.i(TAG,"-----------------record.callTime--------Outgoing-----------"+record.callTime);
            record._id = PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallRecordInsert(record);

            AfPalmCallResp.AfPalmCallHotListItem  hotListItem= new AfPalmCallResp.AfPalmCallHotListItem();
            hotListItem.afid = mCurAfPalmCallHotListItem.afid;
            hotListItem.name = mCurAfPalmCallHotListItem.name;
            hotListItem.age = mCurAfPalmCallHotListItem.age;
            hotListItem.sex = mCurAfPalmCallHotListItem.sex;
            hotListItem.coverUrl = mCurAfPalmCallHotListItem.coverUrl;
            hotListItem.mediaDescUrl = mCurAfPalmCallHotListItem.mediaDescUrl;


            if(PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallInfoGet(hotListItem.afid)==null){
                hotListItem._id =  PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallInfoInsert(hotListItem);
            }else{
                //hotListItem._id =  PalmchatApp.getApplication().mAfCorePalmchat.AfDbPalmCallInfoUpdate(hotListItem);
            }

        } catch (Exception e){
            PalmchatLogUtils.e(TAG,"-----Exception------"+e.toString());
            e.printStackTrace();
        }

    }

    /**
     * 供外部调用的保存通话信息到数据库
     * @param call_id
     * @param justalkDataInfo
     * @param callType
     */
    public void saveCallInfoToDb(final int call_id,final JustalkDataInfo justalkDataInfo, final int callType){
        mBackgroundExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                switch (callType) {
                    case AfPalmCallResp.AFMOBI_CALL_TYPE_MISSED:
                        palmcallSaveIncomingToDb(call_id,justalkDataInfo,callType);
                        break;
                    case AfPalmCallResp.AFMOBI_CALL_TYPE_DECLINE:
                    case AfPalmCallResp.AFMOBI_CALL_TYPE_IN:
                        palmcallUpdateIncomingToDb(callType);
                        break;
                    case AfPalmCallResp.AFMOBI_CALL_TYPE_OUT:
                        palmcallSaveOutgoingToDb(call_id, callType);
                        break;
                    default:
                        break;
                }
            }
        }, 0, TimeUnit.SECONDS);
    }

    /**
     * 获取当前缓存的AfPalmCallResp.AfPalmCallRecord
     */
    public AfPalmCallResp.AfPalmCallRecord getCurRecord(){
        return mCurIncomingRecord;
    }

    /**
     * 设置需要弹出加载框的监听
     * @param callSomeOneStateListener
     */
    public void  setCallSomeOneStateListener(CallSomeOneStateListener callSomeOneStateListener) {
        mCallSomeOneStateListener = callSomeOneStateListener;
    }

    public boolean isPalmcallLogined () {
        boolean isLogined = false;
        if(MtcCli.Mtc_CliGetState() != MtcCliConstants.EN_MTC_CLI_STATE_LOGINED){
            isLogined = false;
        } else {
            isLogined = true;
        }
        return isLogined;
    }

    /**
     *
     * @param httpHandle
     * @param flag
     * @param code
     * @param http_code
     * @param result
     * @param user_data
     */

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        PalmchatLogUtils.i(TAG, "----------------flag---------" + flag + "-------------code-------------------" + code );
        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                case Consts.REQ_PALM_CALL_GET_INFO:
                    doResultGetSelfInfo(result);
                    break;

                case Consts.REQ_PALM_CALL_MAKECALL:
                    doResultGetLeftTime(result);
                    break;
                default:
                    break;
            }
        } else {
            if(flag == Consts.REQ_PALM_CALL_MAKECALL){
                //回调对方不在线状态
                if(mCallSomeOneStateListener!=null){
                    mCallSomeOneStateListener.progressend(false);
                }
                if(Consts.REQ_CODE_PALMCALL_NO_ENOUGH_MINITES_801==code){
                    PalmchatLogUtils.i(TAG,"--------------Consts.REQ_CODE_PALMCALL_NO_ENOUGH_MINITES_801==code----");
                    mCallSomeOneStateListener.noLeftTime();
                    return;
                }
            }
            Consts.getInstance().showToast(PalmchatApp.getApplication().getApplicationContext(), code, flag, http_code);
        }
    }

    private void doResultGetSelfInfo(Object result){
        AfPalmCallResp afPalmCallResp = null;
        try {
            afPalmCallResp = (AfPalmCallResp) result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != afPalmCallResp) {
            savaAfPalmCallRespToDb(afPalmCallResp);
            doCallWidthThirdSdk(afPalmCallResp,mPeerListItem);
        }
    }
    /**
     * 处理请求剩余时间的返回数据
     * @param result
     */
    private void doResultGetLeftTime(Object result){
        AfPalmCallResp afPalmCallResp = null;
        try {
            afPalmCallResp = (AfPalmCallResp) result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(afPalmCallResp!=null) {
            AfPalmCallResp.AfPalmCallMakeCallResp afPalmCallMakeCallResp = null;
            try {
                afPalmCallMakeCallResp = (AfPalmCallResp.AfPalmCallMakeCallResp)afPalmCallResp.respobj;
            } catch (Exception e){
                e.printStackTrace();
                PalmchatLogUtils.e(TAG,"--------------afPalmCallMakeCallResp======---------------"+e.toString());
            }

            if(null!=afPalmCallMakeCallResp){
                mAvaiableCallTime = afPalmCallMakeCallResp.leftTime;
                EventBus.getDefault().post(new UpdatePalmCallDurationEvent(mAvaiableCallTime));
                if(mAvaiableCallTime>0){
                    justalkCall(mSelfListItem,mPeerListItem,mAvaiableCallTime);
                } else {
                    if(mCallSomeOneStateListener!=null){
                        mCallSomeOneStateListener.progressend(false);
                        mCallSomeOneStateListener.noLeftTime();
                    }
                }
            }
        } else {
            PalmchatLogUtils.i(TAG, "--------------data-parse-error-------------------------");
        }
    }

    /**
     * 跳转到充值界面
     */
    public void jumpToChargePage(){
        try {
            //需要跳转到充值接口
            if (!TextUtils.isEmpty(mRechargeUrl)) {
                String sessionid = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession();
                String countryCode = AppUtils.getCCFromProfile_forRecharge(CacheManager.getInstance().getMyProfile());
                String language = SharePreferenceUtils.getInstance(mContext).getLocalLanguage();
                String phone = CacheManager.getInstance().getMyProfile().phone;
                String password = "";
                AfLoginInfo[] myAccounts =  PalmchatApp.getApplication().mAfCorePalmchat.AfDbLoginGetAccount();
                if (myAccounts != null && myAccounts.length > 0) {
                    AfLoginInfo afLoginInfo = myAccounts[0];
                    if (afLoginInfo != null) {
                        password = afLoginInfo.password;
                        /** DESC + Base64 */
                       password = PalmchatApp.getApplication().mAfCorePalmchat.AfLoginEncode(password, sessionid);
                    }
                }
                String imei = PalmchatApp.getOsInfo().getImei();
                String afid = CacheManager.getInstance().getMyProfile().afId;

                if (sessionid != null)
                    sessionid = URLEncoder.encode(sessionid, "UTF-8");
                if (countryCode != null)
                    countryCode = URLEncoder.encode(countryCode, "UTF-8");
                if (language != null)
                    language = URLEncoder.encode(language, "UTF-8");
                if (phone != null)
                    phone = URLEncoder.encode(phone, "UTF-8");
                if (password != null)
                    password = URLEncoder.encode(password, "UTF-8");
                if (imei != null)
                    imei = URLEncoder.encode(imei, "UTF-8");
                if (afid != null)
                    afid = URLEncoder.encode(afid, "UTF-8");

                String mUrl = mRechargeUrl;
                if (mUrl.contains("?")) {
                    mUrl = mUrl + "&sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&phone=" + phone + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
                } else {
                    mUrl = mUrl + "?sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&phone=" + phone + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
                }
                PalmchatLogUtils.i(TAG,"-----------------mRechargeUrl-------------------------------"+mRechargeUrl);
                Intent intent = new Intent(mContext, MyWapActivity.class);
                intent.putExtra(IntentConstant.WAP_URL, mUrl);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } else {
                PalmchatLogUtils.i(TAG,"-----------------mRechargeUrl------NULL--------------------------");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 设置充值url
     * @param reChargeUrl
     */
    public void setRechargeUrl(String reChargeUrl) {
        mRechargeUrl = reChargeUrl;
    }


    /**
     * 去掉通话
     */
    public void cancelTocall(){
        isCancelCall = true;
        PalmchatApp.getApplication().mAfCorePalmchat.AfHttpCancel(mCurHttpId);
    }

    /**
     * 设置已经通话时间
     * @param talkedTime
     */
    public void setTalkedTime(int dwSessId,long talkedTime){
        if(null==mHashMapTalkedTime){
            mHashMapTalkedTime  = new HashMap<Integer,Long>();
        }
        mHashMapTalkedTime.put(dwSessId,talkedTime);
    }

    /**
     * 设置是否支持palmcall
     * @param isSupportPalmcall
     */
    public void setSupportPalmcall(boolean isSupportPalmcall) {
        this.mIsSupportPalmcall = isSupportPalmcall;
    }

    /**
     * 是否支持palmcall
     * @return
     */
    public boolean isSupportPalmcall(){
        return  this.mIsSupportPalmcall;
    }

    /**
     * 获取是否通话
     * @return
     */
    public boolean isPalmcalling() {
        return  PalmcallService.isCalling();
    }

    /**
     * 获取当前的通话时长
     * @param dwSessID
     * @return
     */
    public long getTalkedTime(int dwSessID) {
        long talkedTime = 0;
        if(mHashMapTalkedTime!=null){
            try{
                talkedTime = mHashMapTalkedTime.get(dwSessID);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return  talkedTime;
    }
}
