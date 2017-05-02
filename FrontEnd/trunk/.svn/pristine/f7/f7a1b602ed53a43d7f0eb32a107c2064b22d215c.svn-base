package com.afmobi.palmchat.ui.customview.videoview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobigroup.gphone.R;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by Administrator on 2016/7/21.
 */
public class VedioManager {
    private static final String TAG = VedioManager.class.getSimpleName();
    private static VedioManager instance;
    /*当前播放的视频对象*/
    private CustomVideoController videoController;
    /*标识唯一的一条视频广播*/
    private String flag;
    private Context mContext;


    private VedioManager() {

    }

    public static VedioManager getInstance() {
        if (instance == null) {
            instance = new VedioManager();
        }
        return instance;
    }

    public void setVideoController(CustomVideoController tmpVideoController) {
        videoController = tmpVideoController;
    }

    public CustomVideoController getVideoController() {
        return videoController;
    }

    public void setFlag(String tmpFlag) {
        flag = tmpFlag;
    }

    public String getFlag() {
        return flag;
    }


}
