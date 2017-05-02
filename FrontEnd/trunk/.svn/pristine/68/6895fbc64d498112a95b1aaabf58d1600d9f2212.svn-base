package com.afmobi.palmchat.ui.activity.palmcall;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.broadcasts.HomeBroadcast;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.eventbusmodel.EventPalmcallNotication;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobigroup.gphone.R;
import com.core.AfPalmCallResp;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.listener.AfHttpResultListener;
import com.justalk.cloud.lemon.MtcCallConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by heguiming on 2016/6/23.
 */
public class PalmCallRecentsActivity extends BaseActivity implements AfHttpResultListener,View.OnClickListener,CallSomeOneStateListener{

    private static final String TAG = PalmCallRecentsActivity.class.getSimpleName();
    private Context mContext;
    private View mVNo_Data;
    private ImageView mIvCallRecentsBack;//后退按钮
    //标题
    private TextView mTitleText;

    private ListView mListView;

    private PalmCallRecentsAdapter mPalmCallRecentsAdapter;

    private List<String> mList;

    private AfPalmchat mAfPalmchat;

    ArrayList<AfPalmCallResp.AfPalmCallRecord> mCallRespList;//数据库取出的数据

    private HashMap<String,AfPalmCallResp.AfPalmCallHotListItem> mPalmCallHosts;
    private static final int DB_NODATA = 9001;
    private static final int REFRESH = 9002;
    /**拨打电话状态*/
    private boolean mBL_toCall;
    private LooperHandler looperThread;
    private ArrayList<String> mAfids;
    /**
     * 自己剩余时间
     */
    public int mLeftTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        looperThread = new LooperHandler();
        looperThread.setName(TAG);
        looperThread.start();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void findViews() {
        PalmCallVoiceManager.getInstance().isSameFragment =true;
        mContext = this;
        setContentView(R.layout.activity_palmcall_recents);
        Intent intent = getIntent();
        mLeftTime = intent.getIntExtra(JsonConstant.CALLLISTTIME,0);
        mAfPalmchat = ((PalmchatApp) context.getApplicationContext()).mAfCorePalmchat;
        mPalmCallHosts = new HashMap<>();
        mList = new ArrayList<String>();
        mListView = (ListView) findViewById(R.id.ls_palmcallrecents);
        mTitleText = (TextView)findViewById(R.id.title_text);
        mTitleText.setText(R.string.call_title_recents);
        mVNo_Data = findViewById(R.id.friendcircle_lin_no_data);
        showNoDataDes(false);
        mIvCallRecentsBack = (ImageView)findViewById(R.id.back_button);
        mIvCallRecentsBack.setOnClickListener(this);
        showProgressDialog(R.string.please_wait);
        mAfids = new ArrayList<>();
        looperThread.handler.sendEmptyMessage(LooperHandler.INIT_RECORD_LIST);
        mPalmCallRecentsAdapter = new PalmCallRecentsAdapter(this);
        mListView.setAdapter(mPalmCallRecentsAdapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(mPalmCallRecentsAdapter.getCount() <= position)
                    return true;
                AppDialog appDialog = new AppDialog(context);
                String content = context.getResources().getString(R.string.delete_friend_dialog_content);
                final int positionTemp = position;
                appDialog.createConfirmDialog(context, content, new AppDialog.OnConfirmButtonDialogListener() {
                    @Override
                    public void onRightButtonClick() {
                        Message message = new Message();
                        message.what = LooperHandler.DELETE_RECORD;
                        message.obj = positionTemp;
                        looperThread.handler.sendMessage(message);
                    }
                    @Override
                    public void onLeftButtonClick() {

                    }
                });
                appDialog.show();
                return true;
            }
        });
        PalmcallManager.getInstance().setCallSomeOneStateListener(this);
    }

    /** 是否无数据描述
     * @param isShowNoData
     */
    private void showNoDataDes(boolean isShowNoData){
        if(isShowNoData){
            mVNo_Data.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }else {
            mVNo_Data.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }

    /** 获取PalmcallProfile
     * @param strId
     * @return
     */
    private void getPalmcallProfile(String strId) {
        if (!mPalmCallHosts.containsKey(strId)) {
            AfPalmCallResp afPalmCallHotListItem = mAfPalmchat.AfDbPalmCallInfoGet(strId);
            if (afPalmCallHotListItem != null && afPalmCallHotListItem.respobj != null) {
                mPalmCallHosts.put(strId,((ArrayList<AfPalmCallResp.AfPalmCallHotListItem>) afPalmCallHotListItem.respobj).get(0));
            }
        }
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DB_NODATA:
                    showNoDataDes(true);
                    //ToastManager.getInstance().show(mContext, mContext.getResources().getString(R.string.no_data));
                    dismissProgressDialog();
                    break;
                case REFRESH:
                    mPalmCallRecentsAdapter.updatelist(mPalmCallHosts,mCallRespList,true);
                    dismissProgressDialog();
                    break;
            }
        }
    };

    public void call(AfPalmCallResp.AfPalmCallHotListItem listItem){
        if(TextUtils.isEmpty(listItem.afid)) {
            PalmchatLogUtils.i(TAG,getString(R.string.afid_not_found)+"callOneInfo");
            ToastManager.getInstance().show(this,R.string.error_occurred);
            return;
        }
        PalmcallManager.getInstance().palmcallCall(listItem);
    }

    @Override
    public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)&&(event.getAction()==KeyEvent.ACTION_UP)) {
            PalmcallManager.getInstance().cancelTocall();
        }
        return false;
    }

    @Override
    public void progressing() {
        showProgressDialog(R.string.loading);
    }


    @Override
    public void progressend(boolean isFinish) {
        dismissAllDialog();
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

    class LooperHandler extends Thread{
        private static final int INIT_RECORD_LIST = 1000;
        private static final int DELETE_RECORD = 1001;
        Handler handler;
        Looper looper;
        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case INIT_RECORD_LIST: //数据库获取呼叫记录
                            AfPalmCallResp callResp = mAfPalmchat.AfDbPalmCallRecordGetList();
                            if(callResp == null) {//没有数据的情况下提示语句
                                if (msg.arg1 == -1)//表示删除
                                {
                                    if (mCallRespList != null) {
                                        mCallRespList.clear();
                                    }
                                }
                                /*else{*/
                                Message msgs = new Message();
                                msgs.what = DB_NODATA;
                                mHandler.sendMessage(msgs);
                                return;
                                //  }
                            }
                            if(null != callResp){
                                mCallRespList = (ArrayList<AfPalmCallResp.AfPalmCallRecord>)callResp.respobj;
                                if(mCallRespList.size()>0) {
                                    mAfids.clear();
                                    Collections.reverse(mCallRespList);
                                    for (int i = 0; i < mCallRespList.size(); i++) {
                                        String afid = mCallRespList.get(i).afId;
                                        if (afid == null) {
                                            mAfPalmchat.AfDbPalmCallRecordDelete(mCallRespList.get(i)._id);
                                        }
                                        getPalmcallProfile(afid);
                                        mAfids.add(afid);
                                    }
                                    updateProfileFromServer((String[])mAfids.toArray(new String[mAfids.size()]));
                                }
                            }
                            mHandler.sendEmptyMessage(REFRESH);
                            break;
                        case DELETE_RECORD:
                             int position = (int) msg.obj;
                            AfPalmCallResp.AfPalmCallRecord afPalmCallRecord = mPalmCallRecentsAdapter.getItem(position);
                            if(afPalmCallRecord != null) {
                                mAfPalmchat.AfDbPalmCallRecordDelete(afPalmCallRecord._id);
                                Message message = new Message();
                                message.arg1 = -1;//表示删除
                                message.what = LooperHandler.INIT_RECORD_LIST;
                                looperThread.handler.sendMessage(message);
                            }
                            break;
                    }
                }
            };
            looper.loop();
        }
    }

    @Override
    public void init() {

    }

    private void updateProfileFromServer(String[] afids){
        if(isOverTwoDay()) {
            if (afids != null && afids.length > 0) {
                mAfPalmchat.AfHttpPalmCallGetInfoBatch(afids, null, this);
            }
        }
    }

    private boolean isOverTwoDay() {
        long lastShowTime = SharePreferenceUtils.getInstance(context).getSetUpdatePalmCallProfileTime();
        long diff = System.currentTimeMillis() - lastShowTime; // compare last
        // time
        float days = diff / (1000 * 60 * 60 * 24);//
        if (days > 2) {
            return true;
        }
        return false;
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        PalmchatLogUtils.i(TAG, "----flag:" + flag + "----code:" + code + "----result:" + result);
        if(code == Consts.REQ_CODE_SUCCESS){//请求成功
            switch (flag){
                case Consts.REQ_PALM_CALL_GET_INFO_BATCH:
                    if(result != null){
                        AfPalmCallResp afPalmCallResps = (AfPalmCallResp) result;
                        if(afPalmCallResps != null && afPalmCallResps.respobj != null){
                            ArrayList<AfPalmCallResp.AfPalmCallHotListItem> listItems = (ArrayList<AfPalmCallResp.AfPalmCallHotListItem>)afPalmCallResps.respobj;
                            if(listItems != null) {
                                SharePreferenceUtils.getInstance(context).setUpdatePalmCallProfileTime();
                                for (AfPalmCallResp.AfPalmCallHotListItem item : listItems) {
                                    if (item != null) {
                                        if (item != null && mPalmCallHosts.containsKey(item.afid)) {
                                            mPalmCallHosts.put(item.afid, item);
                                        }
                                    }
                                }
                            }
                            mPalmCallRecentsAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                default:
            }
        }else{//请求失败
            Consts.getInstance().showToast(mContext, code, flag, http_code);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button://返回按钮
                finish();
                break;
            default:
                break;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(mPalmCallHosts != null){
            mPalmCallHosts.clear();
        }
        if(mCallRespList != null){
            mCallRespList.clear();
        }
        if(looperThread != null && looperThread.looper != null){
            looperThread.looper.quit();
        }
        PalmcallManager.getInstance().setCallSomeOneStateListener(null);
        if (VoiceManager.getInstance().isPlaying()) {
            VoiceManager.getInstance().pause();
        }
    }

    /**
     *目前在此处处理打出电话，因为打出电话的广播里面没有exintro 信息
     * @param notication
     */
    public void onEventMainThread(EventPalmcallNotication notication) {
        String callState = notication.getCallState();
        if(!TextUtils.isEmpty(callState)){
            if(callState.equals(MtcCallConstants.MtcCallDidTermNotification) || callState.equals(MtcCallConstants.MtcCallTermedNotification)){
                looperThread.handler.sendEmptyMessage(LooperHandler.INIT_RECORD_LIST);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PalmCallVoiceManager.getInstance().setViewisHome(false);
        VoiceManager.getInstance().completion();//是否要停止播放
    }

    @Override
    protected void onResume() {
        super.onResume();
        PalmCallVoiceManager.getInstance().setViewisHome(true);
    }
}