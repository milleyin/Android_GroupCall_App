package com.afmobi.palmchat.ui.activity.palmcall;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobigroup.gphone.R;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hj on 2016/8/18.
 */
public class PalmCallVoiceManager {
    private static PalmCallVoiceManager palmCallVoiceManager = new PalmCallVoiceManager();
    public static final String DOWNLOAD_SUCCESS_SUFFIX = "_COMPLETE"; // Successful download suffix
    public static PalmCallVoiceManager getInstance(){
        return palmCallVoiceManager;
    }
    private Activity mActivity;
    private AnimationDrawable animDrawable;// 播放语音播放效果的动画
    private Animation animation;
    private HashMap<Integer,Long> viewHaspMap = new HashMap<>();
    private ArrayList<View> DownloadPlay = new ArrayList<>();
    public boolean isSameFragment = true;
    private String mMediaurl;//保存点击的语音地址
    private boolean isDownloadAnim;
    //记录是否按下home键
    private boolean isHome = true;
    public void setActivity(Activity activity) {
        this.mActivity = activity;
        animation = AnimationUtils.loadAnimation(this.mActivity,R.anim.call_voice_download_animation);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
    }
    private PalmCallVoiceManager(){

    }

    //按下home键后会调用此方法
    public void setViewisHome(boolean home){
        isHome = home;
    }

    public void setViewHashMap(int key,Long value){
        viewHaspMap.put(key,value);
    }
    public void bindVoiceView(View play,String path,String mediaurl,int id,int viewHashcode){
        boolean exists ;//判断本地是否有地址
        mMediaurl = mediaurl;
        VoiceModel voiceModel = new VoiceModel();
        if(viewHaspMap.containsKey(viewHashcode)){
            voiceModel.key = viewHashcode;
            voiceModel.value = viewHaspMap.get(viewHashcode);
        }
        voiceModel.className = mActivity.getLocalClassName();
        if (!TextUtils.isEmpty(path)) {
            exists = IsVoiceFileExistsSDcard(path);
            if (exists) {
                path = path + DOWNLOAD_SUCCESS_SUFFIX;
                Play(path,play,id,mActivity.getLocalClassName());//本地已有音频地址所以直接播放
            } else {
                if(NetworkUtils.isNetworkAvailable(mActivity)){//判断是否有没有网络
                    DownloadPlay.add(play);
                    File file = new File(path);
                    downloadVoice(play,file,mediaurl,id,voiceModel);
                    startDownloadAnim(play);
                }else{
                    ToastManager.getInstance().show(mActivity, mActivity.getString(R.string.network_unavailable));
                }
            }
        }
    }

    /**
     *  下载音频
     * @param holder
     * @param file
     */
    private void downloadVoice(final View holder,final File file,final String url,final int id,VoiceModel voiceModel){
        final String voice_path = file.getPath();
        if (!CacheManager.getInstance().getGifDownLoadMap().containsKey(url)) {
            CacheManager.getInstance().getGifDownLoadMap().put(url, voice_path);
            String voiceUrl=url;
            if(!voiceUrl.startsWith(JsonConstant.HTTP_HEAD)){
                voiceUrl= PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerInfo()[Constants.CORE_SERVER_BROADCAST_MEDIA_HOST]+url;
            }
            PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBroadcastDownload(voiceUrl, voice_path, voiceModel, new AfHttpResultListener() {
                @Override
                public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
                    if (code == Consts.REQ_CODE_SUCCESS) {
                        stopDownloadAnim();
                        File newFile = new File(voice_path + DOWNLOAD_SUCCESS_SUFFIX);
                        file.renameTo(newFile);
                        String audioUrl = newFile.getPath();
                        VoiceModel voiceModelTemp = (VoiceModel) user_data;
                        if(voiceModelTemp != null && isHome) {
                            if(viewHaspMap.containsKey(voiceModelTemp.key) && voiceModelTemp.value == viewHaspMap.get(voiceModelTemp.key)) {
                                PalmchatLogUtils.i("PalmCallAdapter","Ada viewHashcode:"+voiceModelTemp.key+",Ada Times:"+voiceModelTemp.value);
                                Play(audioUrl, holder, id, voiceModelTemp.className);
                            }
                        }
                    }else{
                        stopDownloadAnim();
                    }
                    isHome = true;
                    if (CacheManager.getInstance().getGifDownLoadMap().containsKey(url)) {
                        CacheManager.getInstance().getGifDownLoadMap().remove(url);
                    }
                }
            });
        }
    }

    /**
     * 判断本地是否有该音频地址
     * @param voicePath
     * @return
     */
    private boolean IsVoiceFileExistsSDcard(String voicePath) {
        boolean falg = false;
        if (!TextUtils.isEmpty(voicePath)) {
            String path = voicePath + DOWNLOAD_SUCCESS_SUFFIX;
            File file = new File(path);
            if (file.exists()) {
                falg = true;
            } else {
                falg = false;
            }
        }
        return falg;
    }


    private void Play(String audiourl, final View view,int id,String className) {
        if((mActivity.getLocalClassName().equals(className)) && isSameFragment) {
            final File file = new File(audiourl);
            if (file.exists() && file.length() > 0) {
                boolean isPlay = VoiceManager.getInstance().isPlaying();
                if (isPlay) {
                    VoiceManager.getInstance().pause();

                    if (view.equals(VoiceManager.getInstance().getmPalmCallPlay())) {
                        return;
                    }
                }

                if (CommonUtils.isEmpty(VoiceManager.getInstance().getPath())) {
                    VoiceManager.getInstance().setPalmCallPlay(view);
                    VoiceManager.getInstance().setmActivity(mActivity);
                    VoiceManager.getInstance().setListId(id);
                    startPlayAnim(view);
                    VoiceManager.getInstance().play(file.getPath(), new VoiceManager.OnCompletionListener() {

                        @Override
                        public Object onGetContext() {
                            return mActivity;
                        }

                        @Override
                        public void onCompletion() {
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(mActivity, mActivity.getString(R.string.audio_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(mActivity, mActivity.getString(R.string.no_audio_file), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startPlayAnim(final View view) {
        animDrawable = ((AnimationDrawable) mActivity.getResources().getDrawable(R.anim.palmcall_voice_animation));
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            view.setBackgroundDrawable(animDrawable);
        } else {
            view.setBackground(animDrawable);
        }
        if (animDrawable != null && !animDrawable.isRunning()) {
            animDrawable.setOneShot(false);
            animDrawable.start();
        }

        // 解决广播列表播放播放语音下拉列表，语音显示停止实际继续播放
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (VoiceManager.getInstance().isPlaying()) {
                    if (animDrawable != null && !animDrawable.isRunning()) {
                        animDrawable.setOneShot(false);
                        animDrawable.start();
                    }
                }
                return true;
            }
        });
    }

    public String showStopAnim(final String path, View view,int id) {
        final String voiceName = PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(path);
        String voicePaht = RequestConstant.VOICE_CACHE + voiceName;
        String mPath = voicePaht + DOWNLOAD_SUCCESS_SUFFIX;
        stopDownloadAnim();
        //PalmchatLogUtils.i("PalmCallVoiceManager",""+id + ",Path:"+ VoiceManager.getInstance().getPath() + ",isPlaying:"+VoiceManager.getInstance().isPlaying());
        if (!mPath.equals(VoiceManager.getInstance().getPath()) && VoiceManager.getInstance().isPlaying() && (id != VoiceManager.getInstance().getListId())) {
            stopPlayAnim(view);
            PalmchatLogUtils.i("PalmCallVoiceManager","----showStopAnim----:stopPlayAnim");
        } else if (mPath.equals(VoiceManager.getInstance().getPath()) && VoiceManager.getInstance().isPlaying() && (id == VoiceManager.getInstance().getListId())) {
            startPlayAnim(view);
            PalmchatLogUtils.i("PalmCallVoiceManager","----showStopAnim----:startPlayAnim");
        } else {
            stopPlayAnim(view);
            PalmchatLogUtils.i("PalmCallVoiceManager","====showStopAnim====:stopPlayAnim");
        }
        return voicePaht;
    }

    private void stopPlayAnim(final View view) {
        Drawable de = view.getBackground();
        // 刚进去聊天界面是没有赋动画的
        if (de instanceof AnimationDrawable) {
            AnimationDrawable playAnim = (AnimationDrawable) de;
            playAnim.stop();
        }
        view.setBackgroundResource(R.drawable.btn_palmcall_voice_n);
    }

    /**
     * 启动动画
     * @param view
     */
    private void startDownloadAnim(View view){
        view.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.btn_voice_loading));
        view.startAnimation(animation);
        isDownloadAnim = true;
    }

    /**
     * 取消动画
     */
    public void stopDownloadAnim(){
        if(DownloadPlay.size() > 0){
            for(int i = 0;i<DownloadPlay.size();i++){
                DownloadPlay.get(i).clearAnimation();
                DownloadPlay.get(i).setBackgroundResource(R.drawable.btn_palmcall_voice_n);
                PalmchatLogUtils.i("PalmCallVoiceManager","stopDownloadAnim==========");
            }
            DownloadPlay.clear();
            isDownloadAnim = false;
        }
    }

    public void showDownloadAnim(String url,View view){
        if(isDownloadAnim){
            PalmchatLogUtils.i("PalmCallVoiceManager","uel:+"+url + "====mMediaurl:"+mMediaurl);
            if(url.equals(mMediaurl)){
                startDownloadAnim(view);
            }else{
                stopDownloadAnim();
            }
        }
    }

    class VoiceModel{
        int key;
        long value;
        String className;
    }
}
