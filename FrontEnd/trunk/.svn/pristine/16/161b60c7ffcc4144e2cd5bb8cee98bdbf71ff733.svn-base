package com.afmobi.palmchat.ui.activity.palmcall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.db.SharePreferenceService;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.social.AdapterBroadcastUitls;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.RippleBackground;
import com.afmobi.palmchat.ui.customview.RippleView;
import com.afmobi.palmchat.ui.customview.SimpleWaveView;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobigroup.gphone.R;
import com.core.AfMessageInfo;
import com.core.AfPalmCallResp;
import com.core.AfPalmchat;
import com.core.AfResponseComm;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mqy on 2016/7/5.
 */
public class PalmCallSendVoiceActivity extends BaseActivity implements View.OnClickListener,AfHttpResultListener{
    private final String TAG = PalmCallSendVoiceActivity.class.getSimpleName();
    private AfPalmchat mAfPalmchat;
    //后退按钮
    private ImageView mIvCallRecentsBack;
    //标题
    private TextView mTitleText;
    //发送语音整个view
    private View mSendVoiceView;
    //显示语音时间
    private TextView mVoiceTime;
    //录音按钮
    private Button mTalkBtn;
    //重新录按钮
    private Button mReTalkBtn;
    //试听按钮
    private Button mTestBtn;

    private AfPalmchat mAfCorePalmchat;

    private String  mMediaDescUrl;
    //初始文件路径 用来判断和新语音的路径 是否一致 不一致就上传提交
    private String voicePaht = "";

    private boolean download_success;

    /** send voice */
    private   ImageView  mImgDecibel;
    private TextView mTextCancel;
    private MediaRecorder mRecorder;
    private long recordStart;
    private long recordEnd;
    private int recordTime;
    private boolean sent;
    private String mVoiceName;
    private final static int AT_LEAST_LENGTH = 60;
    private final int BASE = 600;
    private static final int RECORDING_TIMER = 9001;
    private static final int PLAY_RECORD_TIMER= 9002;
    private View mRetalk_test_layout;
    private TextView mTextTest;
    private AfPalmCallResp.AfPalmCallHotListItem afPalmCallHotListItem;
    /** 录音界面 */
    private RippleView mRippleView;

    /**水波纹*/
    private RippleBackground mRippleViewEffect;

    private ProgressBar mVoiceProgressBar, mVoiceProgressBar2;
    private ArrayList<AfResponseComm.AfMFileInfo> picturePathList = new ArrayList<AfResponseComm.AfMFileInfo>();
    /**保存按钮*/
    private Button mBtn_Save;

    private boolean isPause;

    //录音波纹图
    Paint barPencilFirst = new Paint();
    Paint barPencilSecond = new Paint();
    Paint peakPencilFirst = new Paint();
    Paint peakPencilSecond = new Paint();

    Paint xAxisPencil = new Paint();

    private int advance_demo_loop = 1;


    final LinkedList<Integer> ampList = new LinkedList<>();


    @Override
    public void findViews() {
        setContentView(R.layout.activity_palmcall_sendvoice);
    }

    @Override
    public void init() {
        mAfPalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        AfPalmCallResp afPalmCallRespSelf = mAfPalmchat.AfDbPalmCallInfoGet(CacheManager.getInstance().getMyProfile().afId);
        if (afPalmCallRespSelf!=null)     //判断数据库存有自己的信息 有就直接加载
        {
            try{
                if (afPalmCallRespSelf.respobj!=null) {
                    afPalmCallHotListItem=((ArrayList<AfPalmCallResp.AfPalmCallHotListItem>) afPalmCallRespSelf.respobj).get(0);
                    recordTime=afPalmCallHotListItem.duration;
                }
            } catch (Exception e){
                PalmchatLogUtils.i(TAG,"---------afPalmCallHotListItem1======NULLL--------"+e.toString());
                e.printStackTrace();
            }
        }
        mIvCallRecentsBack = (ImageView) findViewById(R.id.back_button);
        mIvCallRecentsBack.setOnClickListener(this);
        mSendVoiceView = findViewById(R.id.sendvoice_layout);
        mVoiceTime = (TextView) findViewById(R.id.voice_time);
        mVoiceTime.setText("0.0s");
        mTalkBtn = (Button) findViewById(R.id.talk_button1);
        mTalkBtn.setOnTouchListener(new HoldTalkListener());
        mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
        mRippleView = (RippleView) findViewById(R.id.ripple);
        mRippleViewEffect=(RippleBackground)findViewById(R.id.palmcall_recordcall_effect);

        mVoiceProgressBar = (ProgressBar) findViewById(R.id.voice_progress);
        mVoiceProgressBar.setMax(30);
        mVoiceProgressBar2 = (ProgressBar) findViewById(R.id.voice_progress2);
        mImgDecibel = (ImageView) findViewById(R.id.img_decibel);
        mTextCancel = (TextView) findViewById(R.id.tv_cancel);
        mTextCancel.setVisibility(View.GONE);
        mBtn_Save = (Button) findViewById(R.id.btn_ok);
        mReTalkBtn = (Button) findViewById(R.id.retalk_btn);
        mTestBtn = (Button) findViewById(R.id.talktest_btn);
        mTestBtn.setOnClickListener(this);
        mReTalkBtn.setOnClickListener(this);
        mBtn_Save.setOnClickListener(this);
        mRetalk_test_layout = (View) findViewById(R.id.retalk_test_layout);
        mTextTest = (TextView) findViewById(R.id.tv_retest);
        if (afPalmCallHotListItem!=null && !TextUtils.isEmpty(afPalmCallHotListItem.mediaDescUrl)) {
            //判断语音链接有没有再本地，如果有可以直接播放，如果没有，点击播放、重播无效 需要下载，下载完才可以点击两按钮
            final String voiceName = PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(afPalmCallHotListItem.mediaDescUrl);
            voicePaht = RequestConstant.VOICE_CACHE + voiceName;
            boolean exists = IsVoiceFileExistsSDcard(voicePaht);
            if (exists) {
                download_success = true;
                voicePaht = voicePaht + AdapterBroadcastUitls.DOWNLOAD_SUCCESS_SUFFIX;
            } else {
                download_success = false;
            }
            mRetalk_test_layout.setVisibility(View.VISIBLE);
            mTalkBtn.setVisibility(View.GONE);
            mBtn_Save.setVisibility(View.VISIBLE);
            if (download_success) {
                readyforPlay(voicePaht);
            } else {
                File file = new File(voicePaht);
                downloadVoice(file, CommonUtils.splicePalcallAudioUrl(afPalmCallHotListItem.mediaDescUrl,afPalmCallHotListItem.afid));
                mReTalkBtn.setClickable(false);
                mTestBtn.setClickable(false);
            }
            mVoiceTime.setText(recordTime+"s");
        } else
        {
            mTextCancel.setText(R.string.call_Voice_Tips);
            mTextCancel.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                IschangeSave();
                break;
            case R.id.talktest_btn:
                if (CommonUtils.isFastClick()) {
                    return;
                }
                playRecording();
                break;
            case R.id.retalk_btn:
                reRecording();
                break;
            case R.id.btn_ok:
                //判断是否需要更新语音
                if (picturePathList.size()>0 && voicePaht!=null) {
                    if (!TextUtils.isEmpty(voicePaht)  && (voicePaht.equals(picturePathList.get(0).local_img_path) || picturePathList.get(0).local_img_path.startsWith(voicePaht))) {
                        this.finish();
                    } else {
                        mAfPalmchat.AfHttpPalmCallUploadIntroMedia("", picturePathList.get(0).local_img_path, Consts.MEDIA_TYPE_AMR, recordTime, null, this);
                        showProgressDialog(R.string.please_wait);
                    }
                }
                break;
            default:
                break;
        }
    }



    private Handler mTimer = new Handler();
    private long mTimeStart;

    /**
     * send voice
     *
     */
    Handler mHandler2 = new Handler();

    private void reRecording()
    {
        stopPlayRecording();
        stopRecording();
        recordEnd = 0;
        mTextCancel.setText(R.string.call_Voice_Tips);
        mTextCancel.setVisibility(View.VISIBLE);
        mRetalk_test_layout.setVisibility(View.GONE);
        mTalkBtn.setVisibility(View.VISIBLE);
        mBtn_Save.setVisibility(View.GONE);
        picturePathList.clear();
    }


    /**
     * 准备可以播放、保存的本地路径
     * @param voicePaht
     */
    private void readyforPlay(String voicePaht)
    {
        picturePathList.clear();
        AfResponseComm.AfMFileInfo params = new AfResponseComm.AfMFileInfo();
        params.local_img_path = voicePaht;
        params.type = Consts.BR_TYPE_VOICE_TEXT;
        params.url_type = Consts.URL_TYPE_VOICE;
        params.recordTime = recordTime;
        picturePathList.add(params);
    }

    /**
     * 播放已经录好的语音
     */
    private void playRecording() {
        if (picturePathList.size() > 0) {
            final AfResponseComm.AfMFileInfo info = picturePathList.get(0);
            if (!CommonUtils.isHasSDCard()) {
                ToastManager.getInstance().show(PalmCallSendVoiceActivity.this, R.string.without_sdcard_cannot_play_voice_and_so_on);
                return;
            }

            boolean isPlaying = VoiceManager.getInstance().isPlaying();
            if (isPlaying && !isPause ) {
                info.voicePlaying = false;
                stopRecordTimeTask();
                VoiceManager.getInstance().mediaPlayerpause();
                mTextTest.setText(R.string.call_Voice_test);
                mTestBtn.setBackgroundResource(R.drawable.btn_talktest_selector);
                isPause = true;
                return;
            } else if ( isPlaying && isPause)
            {
                playRecordTimeTask();
                VoiceManager.getInstance().mediaPlayerstart();
                mTestBtn.setBackgroundResource(R.drawable.btn_talkpause_selector);
                mTextTest.setText(R.string.call_Voice_pause);
                isPause=false;
                return;
            }
            info.voicePlaying = true;
            VoiceManager.getInstance().setmActivity(PalmCallSendVoiceActivity.this);
            VoiceManager.getInstance().play(info.local_img_path, new VoiceManager.OnCompletionListener() {
                @Override
                public Object onGetContext() {
                    return PalmCallSendVoiceActivity.this;
                }

                @Override
                public void onCompletion() {
                    info.voicePlaying = false;
                    mTextTest.setText(R.string.call_Voice_test);
                    mTestBtn.setBackgroundResource(R.drawable.btn_talktest_selector);
                    stopRecordTimeTask();
                }

                @Override
                public void onError() {

                }
            });
            if (info.voicePlaying) {
                mVoiceTime.setVisibility(View.VISIBLE);
                mVoiceTime.setText("0.0s");
                mTextTest.setText(R.string.call_Voice_pause);
                mTestBtn.setBackgroundResource(R.drawable.btn_talkpause_selector);
//                recordStart = System.currentTimeMillis();
                playRecordTimeTask();
            }

        }

    }

    /**
     * 下载语音
     * @param file 传入保存本地文件
     * @param _voiceUrl     下载语音的URL
     */

    public void downloadVoice( final File file ,final String _voiceUrl ) {
        showProgressDialog(R.string.please_wait);
        final String voice_path = file.getPath();
        PalmchatLogUtils.e("downloadVoice", _voiceUrl);
        if (!CacheManager.getInstance().getGifDownLoadMap().containsKey(_voiceUrl)) {
            CacheManager.getInstance().getGifDownLoadMap().put(_voiceUrl, voice_path);
            String voiceUrl=_voiceUrl;
            if(!voiceUrl.startsWith(JsonConstant.HTTP_HEAD)){
                voiceUrl= PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerInfo()[Constants.CORE_SERVER_BROADCAST_MEDIA_HOST]+_voiceUrl;
            }
            PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBroadcastDownload(voiceUrl, voice_path, null, new AfHttpResultListener() {
                @Override
                public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
                    PalmchatLogUtils.d("AfOnResult========", "flag," + flag + "code=" + code + ",http_code=" + http_code);
                    dismissProgressDialog();
                    if (code == Consts.REQ_CODE_SUCCESS) {
                        File newFile = new File(voice_path + AdapterBroadcastUitls.DOWNLOAD_SUCCESS_SUFFIX);
                        boolean falg = file.renameTo(newFile);
                        if (falg) {
                            readyforPlay(file.getPath()+ AdapterBroadcastUitls.DOWNLOAD_SUCCESS_SUFFIX);
                            download_success = true;
                            mReTalkBtn.setClickable(true);
                            mTestBtn.setClickable(true);
                        }
                    } else {
                        download_success = false;
                        ToastManager.getInstance().show(context, context.getString(R.string.updata_failed_try_again));
                        PalmCallSendVoiceActivity.this.finish();
                    }
                }
            });
        }
    }


    public boolean IsVoiceFileExistsSDcard(String voicePath) {
        boolean falg = false;
        if (!TextUtils.isEmpty(voicePath)) {
            String path = voicePath + AdapterBroadcastUitls.DOWNLOAD_SUCCESS_SUFFIX;
            File file = new File(path);
            if (file.exists()) {
                falg = true;
            } else {
                falg = false;
            }
        }
        return falg;
    }


    /**
     *  窗口停止时停放音乐
     */
    protected void onPause() {
        super.onPause();
        if (recordStart > 0) {
            if (!sent) {
                stopRecordingTimeTask();
                stopRecording();
                PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_UP----MessagesUtils.MSG_VOICE");
                if (CommonUtils.getSdcardSize()) {
                    ToastManager.getInstance().show(context, R.string.memory_is_full);
                } else {

                    PalmchatLogUtils.println("---- send voice----");
                    if (mVoiceName!=null) {
                        mHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
                    }
                }

                setMode(SharePreferenceService.getInstance(PalmCallSendVoiceActivity.this).getListenMode());

                mVoiceTime.setVisibility(View.INVISIBLE);

                mTimer.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mTextCancel.setVisibility(View.GONE);
                    }
                }, 1000);

                mSendVoiceView.setBackgroundColor(PalmchatApp.getApplication().getResources().getColor(R.color.brdcast_voice_blue));

//                onBack();

            }

        }
        stopPlayRecording();

    }


    protected void onResume() {
        super.onResume();
        if (recordTime>1 && recordTime<31) {
            mVoiceTime.setText(recordTime + "s");
        }  else
        {
            mVoiceTime.setText("0.0s");
        }
    }

    @Override
    public void onBackPressed() {
        //判断是否需要更新语音
        IschangeSave();
    }

    //判断是否需要更新语音
    private  void  IschangeSave()
    {
//        stopRecording();
//        stopRecordTimeTask();
//        stopPlayRecording();
//        stopRecordingTimeTask();
        if (picturePathList.size()>0 && voicePaht!=null) {
            if (!TextUtils.isEmpty(voicePaht)  && (voicePaht.equals(picturePathList.get(0).local_img_path)|| picturePathList.get(0).local_img_path.startsWith(voicePaht))) {
                this.finish();
            } else {
                showConfirmDialog();
            }
        } else
        {
            this.finish();
        }
    }


    /**
     * 显示确认对话框
     */
    private void showConfirmDialog() {
        final AppDialog confirmDialog = new AppDialog(this);
        confirmDialog.createConfirmDialog(this, getString(R.string.exit_dialog_content), new AppDialog.OnConfirmButtonDialogListener() {
            @Override
            public void onRightButtonClick() {
//                showProgressDialog(R.string.please_wait);
//                mAfPalmchat.AfHttpPalmCallUploadIntroMedia("", picturePathList.get(0).local_img_path, Consts.MEDIA_TYPE_AMR, recordTime, null, PalmCallSendVoiceActivity.this);
                PalmCallSendVoiceActivity.this.finish();
            }

            @Override
            public void onLeftButtonClick() {
//                PalmCallSendVoiceActivity.this.finish();
                confirmDialog.dismiss();
            }
        });
        confirmDialog.show();
    }

    /**
     * 停止播放
     */
    private void stopPlayRecording()
    {
        recordStart = 0;
        stopRecordTimeTask();
        VoiceManager.getInstance().pause();
        mTextTest.setText(R.string.call_Voice_test);
        mTestBtn.setBackgroundResource(R.drawable.btn_talktest_selector);
        mRecorder = null;
        recordEnd = System.currentTimeMillis();
        mVoiceTime.setText("0.0s");
        mRippleView.setVisibility(View.GONE);
        mRippleView.stopRipple();
        mRippleViewEffect.stopRippleAnimation();
        mVoiceProgressBar.setVisibility(View.VISIBLE);
        mVoiceProgressBar2.setVisibility(View.GONE);
        mVoiceProgressBar.setProgress(0);
        mVoiceProgressBar2.setProgress(0);
        isPause = false;
    }


    /**
     * 开始录音
     * @return 返回播放的路径文件
     */
    private String startRecording() {
        String voiceName = null;
        mVoiceProgressBar.setMax(300);
        mVoiceProgressBar2.setMax(300);
        if (RequestConstant.checkSDCard()) {
            voiceName = mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_VOICE);
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mRecorder.setOutputFile(RequestConstant.VOICE_CACHE + voiceName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {

                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    PalmchatLogUtils.println("mRecorder onInfo what " + what + " extra " + extra);
                    mHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
                    stopRecordingTimeTask();
                    setMode(SharePreferenceService.getInstance(PalmCallSendVoiceActivity.this).getListenMode());
                    mTalkBtn.setSelected(false);
                    stopRecording();
                    sent = true;
                }
            });

            try {
                mRecorder.prepare();
                mRecorder.start();
                recordStart = System.currentTimeMillis();
                mTimeStart = System.currentTimeMillis();
            } catch (Exception e) {
                e.printStackTrace();
                PalmchatLogUtils.e("startRecording", "prepare() failed" + e.getMessage());

                return null;
            }

            final String recordName = voiceName;

            mHandler2.postDelayed(new Runnable() {

                @Override
                public void run() {
                    long time1 = System.currentTimeMillis();
                    byte[] recordData = FileUtils.readBytes(RequestConstant.VOICE_CACHE + recordName);
                    long time2 = System.currentTimeMillis();

                    if (recordData != null) {
                        PalmchatLogUtils.println("--ddd ActivitySendBroadcastMessage recordData length " + recordData.length);
                    } else {
                        PalmchatLogUtils.println("--ddd ActivitySendBroadcastMessage recordData null ");
                    }
                    PalmchatLogUtils.println("--ddd ActivitySendBroadcastMessage readBytes time  " + (time2 - time1));
                    mRippleView.setVisibility(View.VISIBLE);
                    mRippleView.setOriginRadius(mTalkBtn.getWidth() / 2);
                    mRippleView.startRipple();
                    mRippleViewEffect.startRippleAnimation();
                    mTextCancel.setVisibility(View.VISIBLE);
                    mTextCancel.setText(R.string.push_up_to_cancel);
                    mVoiceTime.setVisibility(View.VISIBLE);
                    mVoiceTime.setText("0.0s");
                    startRecordingTimeTask();
//                    startVoiceAmplitude();
                    AudioManager am = (AudioManager) PalmchatApp.getApplication().getSystemService(Context.AUDIO_SERVICE);
                    am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                }
            }, 50);

        }

        return voiceName;
    }
    /*
     *    停止录音
    */
    private void stopRecording() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
                PalmchatLogUtils.e("stopRecording", "stopRecording() failed" + e.getMessage());
            } finally {
                mRecorder = null;
                recordEnd = System.currentTimeMillis();
                recordTime = Math.min((int) ((recordEnd - recordStart) / 1000), RequestConstant.RECORD_VOICE_TIME);
                recordStart = 0;
                recordEnd = 0;
//                mVoiceTime.setVisibility(View.GONE);
                mVoiceTime.setText("0.0s");
                mRippleView.setVisibility(View.GONE);
                mRippleView.stopRipple();
                mRippleViewEffect.stopRippleAnimation();
                mVoiceProgressBar.setVisibility(View.VISIBLE);
                mVoiceProgressBar2.setVisibility(View.GONE);
                mVoiceProgressBar.setProgress(0);
                mVoiceProgressBar2.setProgress(0);

                AudioManager am = (AudioManager) PalmchatApp.getApplication().getSystemService(Context.AUDIO_SERVICE);
                am.abandonAudioFocus(null);

            }
        }
    }

    private Timer recordingTimer = new Timer();
    private TimerTask recordingTimerTask;

    private void startRecordingTimeTask() {
        if (VoiceManager.getInstance().isPlaying()) {
            VoiceManager.getInstance().pause();
        }

        recordingTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(RECORDING_TIMER);
            }
        };
        recordingTimer.schedule(recordingTimerTask, 0, 100);
    }




    private void stopRecordingTimeTask() {
        if (null != recordingTimerTask) {
            recordingTimerTask.cancel();
        }
        mImgDecibel.setBackgroundResource(Constants.voice_anims[0]);
    }


    private Timer PlayRecordTimer = new Timer();
    private TimerTask PlayrecordTimerTask;
    private void playRecordTimeTask()
    {
        PlayrecordTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(PLAY_RECORD_TIMER);
            }
        };
        PlayRecordTimer.schedule(PlayrecordTimerTask, 0, 100);
    }

    private void stopRecordTimeTask() {
        if (null != PlayrecordTimerTask) {
            PlayrecordTimerTask.cancel();
        }
        mImgDecibel.setBackgroundResource(Constants.voice_anims[0]);
    }

    private float mDownX;
    private float mDownY;

    private long mCurrentClickTime;
    private static final long LONG_PRESS_TIME = 500;

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        dismissProgressDialog();
        if (code == Consts.REQ_CODE_SUCCESS) {
            dismissProgressDialog();
            PalmchatLogUtils.i(TAG, "flag = " + (flag == Consts.REQ_PALM_CALL_UPLOAD_INTRO_MEDIA));
            if (flag == Consts.REQ_PALM_CALL_UPLOAD_INTRO_MEDIA) {// 更新语音
                AfResponseComm.AfMFileInfo _fileInfo = (AfResponseComm.AfMFileInfo) user_data;
                String _url=(String)result;
                if (afPalmCallHotListItem!=null) {
                    afPalmCallHotListItem.mediaDescUrl = _url;  //更改成功后 更新到服务器 只更新ts
                    afPalmCallHotListItem.duration=recordTime;
                    mAfPalmchat.AfDbPalmCallInfoUpdate(afPalmCallHotListItem);
                }
                String pathOriginalDestination = RequestConstant.VOICE_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(_url)+AdapterBroadcastUitls.DOWNLOAD_SUCCESS_SUFFIX;;
                File outFile_original = new File(pathOriginalDestination);
                if (!TextUtils.isEmpty(picturePathList.get(0).local_img_path)) {
                    FileUtils.copyToImg(picturePathList.get(0).local_img_path, outFile_original.getAbsolutePath());
                }
                picturePathList.clear();
                this.finish();
            }
        }else {
            Consts.getInstance().showToast(context, code, flag,http_code);
        }
    }

    class HoldTalkListener implements View.OnTouchListener {
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == R.id.talk_button1) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (CommonUtils.isFastClick()) {
                            break;
                        }
                        mDownX = event.getX();
                        mDownY = event.getY();

                        PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_DOWN----");

                        if (!CommonUtils.isHasSDCard()) {
                            ToastManager.getInstance().show(PalmCallSendVoiceActivity.this, R.string.without_sdcard_cannot_play_voice_and_so_on);
                            return false;
                        }
                        sent = false;
                        mCurrentClickTime = Calendar.getInstance().getTimeInMillis();

                        if (recordStart > 0) {
                            mHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
                            stopRecordingTimeTask();
                            setMode(SharePreferenceService.getInstance(PalmCallSendVoiceActivity.this).getListenMode());
                            mTalkBtn.setSelected(false);
                            stopRecording();
                            sent = true;
                            return false;
                        }

                        mVoiceName = startRecording();

                        if (mVoiceName == null) {
                            return false;
                        }
                        setMode(AudioManager.MODE_NORMAL);
                        if (VoiceManager.getInstance().isPlaying()) {
                            VoiceManager.getInstance().pause();
                        }
                        mTimer.removeCallbacksAndMessages(null);

                        PalmchatLogUtils.println("mVoiceName " + mVoiceName);
                        break;
                    case MotionEvent.ACTION_MOVE:

                        if (recordStart <= 0) {
                            break;
                        }

                        float offsetX1 = Math.abs(event.getX() - mDownX);
                        float offsetY1 = Math.abs(event.getY() - mDownY);
                        if (offsetX1 < ImageUtil.dip2px(context, 85) && offsetY1 < ImageUtil.dip2px(context, 85)) {
                            if (mTextCancel.getText().equals(getString(R.string.cancel_voice))) {
                                mTextCancel.setText(R.string.empty);
                            } else
                            {
                                mTextCancel.setText(R.string.push_up_to_cancel);
                            }
//                            mTextCancel.setVisibility(View.GONE);
                            mVoiceProgressBar.setVisibility(View.VISIBLE);
                            mVoiceProgressBar2.setVisibility(View.GONE);
                            mTalkBtn.setSelected(false);
                            mVoiceTime.setTextColor(getResources().getColor(R.color.log_blue));
                            mSendVoiceView.setBackgroundColor(PalmchatApp.getApplication().getResources().getColor(R.color.brdcast_voice_blue));
                        } else {
                            mTextCancel.setText(R.string.cancel_voice);
                            mTextCancel.setVisibility(View.VISIBLE);
                            mTalkBtn.setSelected(true);
                            mVoiceProgressBar.setVisibility(View.GONE);
                            mVoiceProgressBar2.setVisibility(View.VISIBLE);
                            mVoiceTime.setTextColor(getResources().getColor(R.color.color_voice_red));
                            mSendVoiceView.setBackgroundColor(PalmchatApp.getApplication().getResources().getColor(R.color.brdcast_voice_red));
                        }
                        break;

                    case MotionEvent.ACTION_UP:

                        PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_UP----");

                        if (mVoiceTime != null && mVoiceTime.isShown() && !sent || (Calendar.getInstance().getTimeInMillis() - mCurrentClickTime <= LONG_PRESS_TIME)) {

                            PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_UP----MessagesUtils.MSG_VOICE");
                            if (CommonUtils.getSdcardSize()) {
                                ToastManager.getInstance().show(context, R.string.memory_is_full);
                            } else {

                                float offsetX = Math.abs(event.getX() - mDownX);
                                float offsetY = Math.abs(event.getY() - mDownY);

                                if (offsetX < mTalkBtn.getWidth() / 2 && offsetY < mTalkBtn.getHeight() / 2) {
                                    if (recordStart > 0) {
                                        mHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
                                    }
                                } else {
                                    mTextCancel.setText(R.string.empty);
                                    ToastManager.getInstance().show(context, R.string.cancelled);
                                    mHandler2.removeCallbacksAndMessages(null);
                                }
                            }
                            stopRecording();
                            stopRecordingTimeTask();
                            setMode(SharePreferenceService.getInstance(PalmCallSendVoiceActivity.this).getListenMode());

                            mVoiceTime.setVisibility(View.INVISIBLE);

                            mTimer.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    mVoiceTime.setVisibility(View.VISIBLE);
                                    if (mTextCancel.getText().equals( getString( R.string.empty))) {
                                        mVoiceTime.setText("0.0s");
                                    }
                                    if (!mTextCancel.getText().equals(getString( R.string.call_Voice_Tips))) {
//                                        mTextCancel.setVisibility(View.GONE);
                                        mTextCancel.setText(R.string.call_Voice_Tips);
                                    }
                                }
                            }, 1000);

                            mSendVoiceView.setBackgroundColor(PalmchatApp.getApplication().getResources().getColor(R.color.brdcast_voice_blue));
                            mTalkBtn.setSelected(false);
                        }
                        break;
                }
            }
            return false;
        }
    }

    public void setMode(int mode) {
        if (mode == AudioManager.MODE_IN_CALL) {
            AudioManager audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            audio.setSpeakerphoneOn(false);
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            audio.setMode(AudioManager.MODE_IN_CALL);
        } else {
            AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.MODE_NORMAL);
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case MessagesUtils.MSG_VOICE: {
                    recordVoiceFinish(mVoiceName);
                    break;
                }
                case RECORDING_TIMER:
                    long recordCurTime = System.currentTimeMillis();
                    int voiceProgress = (int) ((recordCurTime - recordStart) / 100);
                    if (voiceProgress <= RequestConstant.RECORD_VOICE_TIME*10) {
                        mVoiceProgressBar.setProgress(voiceProgress);
                        mVoiceProgressBar2.setProgress(voiceProgress);
                    }
                    if (recordStart>0) {
                        mVoiceTime.setText(CommonUtils.diffTime(recordCurTime, recordStart));
                    } else
                    {
                        mVoiceTime.setText("0.0s");
                    }
//                    mVoiceTime.setText(CommonUtils.diffTime(recordCurTime, recordStart));
                    refreshVoiceAmplitude();
                    if (recordStart > 0 && recordCurTime - recordStart >= RequestConstant.RECORD_VOICE_TIME * 1000 && !sent) {
                        stopRecordingTimeTask();
                        stopRecording();
                        if (CommonUtils.getSdcardSize()) {
                            ToastManager.getInstance().show(context, R.string.memory_is_full);
                            break;
                        }
                        PalmchatLogUtils.e(TAG, "----handleMessage----MessagesUtils.MSG_VOICE");

                        sent = true;

                        mHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);

                        setMode(SharePreferenceService.getInstance(PalmCallSendVoiceActivity.this).getListenMode());

                        mVoiceTime.setVisibility(View.INVISIBLE);

                        mTimer.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                mTextCancel.setVisibility(View.GONE);
                            }
                        }, 1000);

                        mSendVoiceView.setBackgroundColor(PalmchatApp.getApplication().getResources().getColor(R.color.brdcast_voice_blue));
                    }
                    break;
                case PLAY_RECORD_TIMER:
                    long PlayCurTime=VoiceManager.getInstance().getVoicePlayingPosition();
                    long PlayTotalTime=VoiceManager.getInstance().getVoicePlayingDuration();
                    int PlayvoiceProgress = (int) (PlayCurTime);
                    mVoiceProgressBar.setMax((int)PlayTotalTime);
                    mVoiceProgressBar2.setMax((int)PlayTotalTime);
                    if (PlayvoiceProgress <= PlayTotalTime) {
                        mVoiceProgressBar.setProgress(PlayvoiceProgress);
                        mVoiceProgressBar2.setProgress(PlayvoiceProgress);
                    }
                    mVoiceTime.setText(CommonUtils.diffTime(PlayCurTime, 0));
                    if (PlayvoiceProgress+15>=PlayTotalTime || PlayvoiceProgress+30>=PlayTotalTime || PlayvoiceProgress+50>=PlayTotalTime) {
                        mTextTest.setText(R.string.call_Voice_test);
                        mTestBtn.setBackgroundResource(R.drawable.btn_talktest_selector);
                        stopRecordTimeTask();
                        if (CommonUtils.getSdcardSize()) {
                            ToastManager.getInstance().show(context, R.string.memory_is_full);
                            break;
                        }
                        mSendVoiceView.setBackgroundColor(PalmchatApp.getApplication().getResources().getColor(R.color.brdcast_voice_blue));
                    }
                    break;
                case MessagesUtils.MSG_TOO_SHORT:
                    ToastManager.getInstance().show(context, R.string.message_too_short);
                    mVoiceTime.setVisibility(View.VISIBLE);
                    stopRecordingTimeTask();
                    reRecording();


                    break;
                case MessagesUtils.SEND_VOICE:
                    mRetalk_test_layout.setVisibility(View.VISIBLE);
                    mVoiceTime.setVisibility(View.VISIBLE);
                    mVoiceTime.setText(recordTime+"s");
                    mTalkBtn.setVisibility(View.GONE);
                    mBtn_Save.setVisibility(View.VISIBLE);
                    mTextCancel.setVisibility(View.INVISIBLE);
                    readyforPlay( (String) msg.obj);
                    break;
                case MessagesUtils.MSG_ERROR_OCCURRED:
                    ToastManager.getInstance().show(context, R.string.error_occurred);
                    break;
                default:
                    break;
            }

        };
    };

    private void recordVoiceFinish(final String voiceName) {
        mHandler2.removeCallbacksAndMessages(null);
        if (!CommonUtils.isEmpty(voiceName)) {
            long end = System.currentTimeMillis();
            final long time = Math.min((int) ((end - mTimeStart) / 1000), RequestConstant.RECORD_VOICE_TIME);
            PalmchatLogUtils.e(TAG, "----sendVoice---- time " + time);
            PalmchatLogUtils.e(TAG, "----sendVoice---- time11111111 " + recordTime);
            new Thread(new Runnable() {
                public void run() {
                    byte[] data = FileUtils.readBytes(RequestConstant.VOICE_CACHE + voiceName);
                    if (time < 2) {
                        recordTime=0;
                        mHandler.sendEmptyMessage(MessagesUtils.MSG_TOO_SHORT);
                    } else {
                        if (data == null || data.length <= AT_LEAST_LENGTH) {
                            mHandler.sendEmptyMessage(MessagesUtils.MSG_ERROR_OCCURRED);
                            return;
                        }
                        if (recordTime==1)
                        {
                            recordTime=2;
                        }
                        mHandler.obtainMessage(MessagesUtils.SEND_VOICE, RequestConstant.VOICE_CACHE + voiceName).sendToTarget();
                    }
                }
            }).start();
        } else {
            ToastManager.getInstance().show(context, getString(R.string.sdcard_unmounted));
        }

    }

    private void refreshVoiceAmplitude() {
        if (mRecorder != null) {
            int maxAmplitude = mRecorder.getMaxAmplitude();
            PalmchatLogUtils.println("--wwww refreshVoiceAmplitude maxAmplitude " + maxAmplitude);
            int ratio = maxAmplitude / BASE;
            int decibel = 0;
            if (ratio > 1) {
                decibel = (int) (20 * Math.log(ratio));
            }
            decibel = decibel / 4;
            PalmchatLogUtils.println("--wwww refreshVoiceAmplitude decibel " + decibel);

            if (decibel<1)
            {
                mImgDecibel.setBackgroundResource(Constants.voice_anims[0]);
            } else if (decibel>0 && decibel < 4) {
                mImgDecibel.setBackgroundResource(Constants.voice_anims[1]);
            } else if (decibel > 5 && decibel < 7) {
                mImgDecibel.setBackgroundResource(Constants.voice_anims[2]);
            } else if (decibel > 7 && decibel < 10) {
                mImgDecibel.setBackgroundResource(Constants.voice_anims[3]);
            } else if (decibel > 10 && decibel < 13) {
                mImgDecibel.setBackgroundResource(Constants.voice_anims[4]);
            } else if (decibel > 13 && decibel < 15) {
                mImgDecibel.setBackgroundResource(Constants.voice_anims[5]);
            } else if (decibel > 15) {
                mImgDecibel.setBackgroundResource(Constants.voice_anims[6]);
            }

        }
    }


}
