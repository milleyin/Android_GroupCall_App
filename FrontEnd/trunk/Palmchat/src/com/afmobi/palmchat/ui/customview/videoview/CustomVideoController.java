package com.afmobi.palmchat.ui.customview.videoview;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountsHistoryActivity;
import com.afmobi.palmchat.ui.activity.social.BroadcastDetailActivity;
import com.afmobi.palmchat.ui.activity.social.BroadcastFragment;
import com.afmobi.palmchat.ui.activity.social.FriendCircleFragment;

import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.ToastManager;


import com.afmobi.palmchat.util.ImageUtil;

import com.afmobi.palmchat.util.VoiceManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobi.palmchat.util.universalimageloader.core.assist.FailReason;
import com.afmobi.palmchat.util.universalimageloader.core.listener.ImageLoadingListener;
import com.afmobigroup.gphone.R;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by zhh on 2016/7/14.
 */
public class CustomVideoController extends FrameLayout {
    private Context mContext;

    //    private int mPostion = -1;
    private String mPath;
    /*布局控件*/
    private View mRoot;
    private ViewGroup rl_videoView;
    private ProgressBar progress_loading;
    private View mCenterPlayButton;
    private TextView tv_remaining_time;
    private ImageView img_first_frame;
    private VideoView mVideoView;
    private ViewGroup error_layout;
    /*格式化时间*/
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    /*用来标记播放开始时，倒计时三秒*/
    private static final int SHOW_TIME = 2;
    /*服务器传过来的视频总时长*/
    private int duration_server = 0;

    /*标识唯一的一条视频广播*/
    private String flag;
    /*当前是否是暂停*/
    private boolean isPaused = false;

    /**
     * 耳机检测广播
     */
    private MyHeadsetPlugBroadcastReceiver headsetPlugReceiver;
    /*保存之前耳机插入状态*/
    private int preEarState = -1;


    public CustomVideoController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        makeControllerView();

        PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  构造函数 ");

    }


    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.custom_videoview, this);
        rl_videoView = (ViewGroup) mRoot.findViewById(R.id.rl_videoView);
        progress_loading = (ProgressBar) mRoot.findViewById(R.id.progress_loading);
        mCenterPlayButton = mRoot.findViewById(R.id.center_play_btn);
        tv_remaining_time = (TextView) mRoot.findViewById(R.id.tv_remaining_time);
        img_first_frame = (ImageView) mRoot.findViewById(R.id.img_first_frame);
        error_layout = (ViewGroup) findViewById(R.id.error_layout);

        mCenterPlayButton.setOnClickListener(mCenterPlayListener);
        rl_videoView.setOnClickListener(mVideoViewLayoutListener);

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        return mRoot;
    }


    /**
     * 初始化视频时长
     *
     * @param time 单位为毫秒
     */
    public void setTime(int time) {
        if (mVideoView == null) {
            tv_remaining_time.setVisibility(View.VISIBLE);
            tv_remaining_time.setText(stringForTime(time));
            duration_server = time;

            PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  setTime ");
        }

    }

    public void setPath(String path) {
        mPath = path;
        PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  setPath ");
    }

    public void setFlag(String tmpFlag) {
        flag = tmpFlag;

        PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  setPath ");
    }

    public void setImgFirstFrame(String thumb_url) {
        ImageManager.getInstance().DisplayImage(img_first_frame, thumb_url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
//						// 下载开始的时候 要设置这个图片的宽高和排布 显示默认的灰色框框
//						setDefaultImageLayoutParams(viewHolder.img_first_frame, isSingle, img_url,mfileInfo,info==null?null:info.pic_rule);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }

        });

        PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  setImgFirstFrame ");

    }


    private OnClickListener mCenterPlayListener = new OnClickListener() {
        public void onClick(View v) {
            PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  mCenterPlayListener ");
            if (NetworkUtils.isNetworkAvailable(mContext)) { //判断网络是否可用
                  /*如果点击同一条广播*/
                if (!TextUtils.isEmpty(flag) && flag.equals(VedioManager.getInstance().getFlag())) {
                    resume();
                } else { //如果点击新的广播
                    start();
                    mHandler.removeCallbacks(runnable);
                    mHandler.postDelayed(runnable, Constants.video_timeout);
                }
            } else {
                ToastManager.getInstance().show(mContext, mContext.getString(R.string.network_unavailable));
            }

        }
    };

    private OnClickListener mVideoViewLayoutListener = new OnClickListener() {
        public void onClick(View v) {
            PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  mVideoViewLayoutListener ");
            if (VedioManager.getInstance().getFlag() != null && flag != null && VedioManager.getInstance().getFlag().equals(flag) && !isPaused)
                pause();
        }


    };


    private void start() {
        /*判断当前耳机插入状态*/
        AudioManager localAudioManager = (AudioManager) PalmchatApp.getApplication().getSystemService(Context.AUDIO_SERVICE);
        if (localAudioManager.isWiredHeadsetOn()) { //true插入了耳机
            preEarState = 1;
        } else {//false未插入耳机
            preEarState = 0;
        }


        register_MyHeadsetPlugBroadcastReceiver();

        PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  start() ");

        /*释放掉之前播放的广播*/
        if (VoiceManager.getInstance().isPlaying()) {
            VoiceManager.getInstance().pause();
        }

        if (VedioManager.getInstance().getVideoController() != null) {
            VedioManager.getInstance().getVideoController().stop();
            VedioManager.getInstance().setVideoController(null);
            VedioManager.getInstance().setFlag(null);
        }

        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }

        mCenterPlayButton.setVisibility(View.INVISIBLE);
        progress_loading.setVisibility(View.VISIBLE);
        img_first_frame.setVisibility(View.VISIBLE);


        mVideoView = (VideoView) mRoot.findViewById(R.id.videoview);
        mVideoView.requestFocus();


        /*根据url 里的图片尺寸对布局的高度进行设置*/
        try {
            int video_w = CommonUtils.split_source_w(mPath);
            int video_h = CommonUtils.split_source_h(mPath);
            int real_h = 0;
            int real_w = 0;
            if (video_w > video_h) {
                real_h = rl_videoView.getWidth() * video_h / video_w;
                real_w = rl_videoView.getWidth();
            } else {
                real_w = rl_videoView.getHeight() * video_w / video_h;
                real_h = rl_videoView.getHeight();
            }
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp2.setMargins(rl_videoView.getWidth() - real_w >> 1, rl_videoView.getHeight() - real_h >> 1, 0, 0);
            mVideoView.setLayoutParams(lp2);
        } catch (Exception e) {//防止URL中没带宽高参数时做的保护
            e.printStackTrace();
        }

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  setOnCompletionListener ");
                stop();

            }
        });
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mVideoView != null && !isPaused) {
                    PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  setOnPreparedListener ");
                    progress_loading.setVisibility(View.INVISIBLE);
                    img_first_frame.setVisibility(View.INVISIBLE);
                    mCenterPlayButton.setVisibility(View.INVISIBLE);

                    mVideoView.start();
                    mHandler.removeMessages(SHOW_TIME);
                    mHandler.sendEmptyMessage(SHOW_TIME);
                }


            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                if (mVideoView != null && !mVideoView.isPlaying() && !isPaused) {
                    PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  setOnErrorListener  getCurrentPosition: " + mVideoView.getCurrentPosition());
                    if (mVideoView.getCurrentPosition() > 0) { //播放中超时，暂停
                        pause();
                    } else { //刚开始播放时，超时，停止
                        stop();
                    }
                    ToastManager.getInstance().show(mContext, R.string.network_inst_stable);
                }
                return true;
            }
        });

            /*因为该接口只能在API 17及以上才能使用，否则编译报错，所以加了版本控制*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:

                            if (mVideoView != null && !isPaused) {
                                PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  setOnInfoListener MEDIA_INFO_BUFFERING_START");
                                progress_loading.setVisibility(View.VISIBLE);
                                mHandler.removeCallbacks(runnable);
                                mHandler.postDelayed(runnable, Constants.video_timeout);
                            }

                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:

                            PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  setOnInfoListener MEDIA_INFO_BUFFERING_END");

                            progress_loading.setVisibility(View.INVISIBLE);


                            break;
                    }
                    return true;
                }
            });

        }

        mVideoView.setVisibility(View.VISIBLE);
        mVideoView.setVideoPath(mPath);

        isPaused = false;


        VedioManager.getInstance().setVideoController(this);
        VedioManager.getInstance().setFlag(flag);


    }

    private void resume() {
        if (mVideoView != null) {

            PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  resume()");

            mVideoView.start();

            mCenterPlayButton.setVisibility(View.INVISIBLE);
            tv_remaining_time.setVisibility(View.INVISIBLE);
            img_first_frame.setVisibility(View.INVISIBLE);

            isPaused = false;

        }
    }


    public void pause() {
        if (mVideoView != null && !isPaused) {
            PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController   pause()");
            mVideoView.pause();
            setRetainTime();

            progress_loading.setVisibility(View.INVISIBLE);
            mCenterPlayButton.setVisibility(View.VISIBLE);
            tv_remaining_time.setVisibility(View.VISIBLE);

            mHandler.removeMessages(SHOW_TIME);

            isPaused = true;
        }

    }
//
//
//    public void stopOrPause() {
//        if (mVideoView != null) {
//            if (PalmchatApp.getApplication().isBackground()) {
//                pause();
//            } else {
//                stop();
//            }
//
//
//        }
//    }

    public void stop() {
        if (mVideoView != null) {

            PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController   stop()");

            VedioManager.getInstance().setVideoController(null);
            VedioManager.getInstance().setFlag(null);

            img_first_frame.setVisibility(View.VISIBLE);
            mCenterPlayButton.setVisibility(View.VISIBLE);
            tv_remaining_time.setVisibility(View.VISIBLE);
            int duration = mVideoView.getDuration();
            if (duration > 0) {
                tv_remaining_time.setText(stringForTime(duration));
            } else if (duration_server > 0) {
                tv_remaining_time.setText(stringForTime(duration_server));
            }

            progress_loading.setVisibility(View.INVISIBLE);

            mVideoView.setVisibility(View.GONE);
            mVideoView.stopPlayback();
            mVideoView = null;

            isPaused = false;
            mHandler.removeMessages(SHOW_TIME);
            mHandler.removeCallbacks(runnable);
            unregister_MyHeadsetPlugBroadcastReceiver();
        }

//        if (mVideoView != null) {
//            VedioManager.getInstance().setVideoController(null);
//            VedioManager.getInstance().setFlag(null);
//
//            img_first_frame.setVisibility(View.VISIBLE);
//            mCenterPlayButton.setVisibility(View.VISIBLE);
//            tv_remaining_time.setVisibility(View.VISIBLE);
//            tv_remaining_time.setText(stringForTime(duration_server));
//            progress_loading.setVisibility(View.INVISIBLE);
//
//            mVideoView.setVisibility(View.GONE);
//            mVideoView.stopPlayback();
//            mVideoView = null;
//            isPaused = false;
//
//            mHandler.removeMessages(SHOW_TIME);
//
//
//        }
    }


    public boolean isPlaying() {
        if (mVideoView != null)
            return mVideoView.isPlaying();
        else
            return false;
    }


    /**
     * 设置当前剩余播放时间
     *
     * @return
     */
    private int setRetainTime() {
        PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  setRetainTime" + "  mVideoView.getCurrentPosition(): " + mVideoView.getCurrentPosition() + "  mVideoView.getDuration(): " + mVideoView.getDuration()  + " duration_server: " + duration_server);

        if (mVideoView == null) {
            return 0;
        }
        int positon = mVideoView.getCurrentPosition();
        int duration = mVideoView.getDuration();
        if (duration > 0) {
            int retainTime = duration - positon;
            tv_remaining_time.setText(stringForTime(retainTime));
        } else if (duration_server > 0) {
            int retainTime = duration_server - positon;
            tv_remaining_time.setText(stringForTime(retainTime));
        }

        return positon;

    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public boolean getVisibleRect(Rect rect) {
        boolean visible = true;
        if (rl_videoView != null && rect != null) {

            if (VedioManager.getInstance().getFlag() != null && flag != null && VedioManager.getInstance().getFlag().equals(flag)) {

                visible = rl_videoView.getLocalVisibleRect(rect);

                PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController getVisibleRect " + " left: " + rect.left + " top: " + rect.top + " right: " + rect.right + " bottom: " + rect.bottom + " visible: " + visible);
            }

        }


        return visible;

    }


    private Runnable runnable = new Runnable() {

        public void run() { //如果超过十秒还未播放的话，就做超时处理
            if (mVideoView != null && !mVideoView.isPlaying() && !isPaused) {
                PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController runnable getCurrentPosition(: " + mVideoView.getCurrentPosition());
                if (mVideoView.getCurrentPosition() > 0) { //播放中超时，暂停
                    pause();
                } else { //刚开始播放时，超时，停止
                    stop();
                }

            }

            mHandler.removeCallbacks(runnable);

        }

    };


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                case SHOW_TIME: // 2 每隔一秒倒计时,倒计时三秒后停止

                    PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  mHandler SHOW_TIME");
                    pos = setRetainTime();
                    if (isPlaying() && pos <= 4000) {
                        msg = obtainMessage(SHOW_TIME);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    } else {
                        removeMessages(SHOW_TIME);
                        tv_remaining_time.setVisibility(View.INVISIBLE);
                    }
                    break;

            }
        }
    };


    /**
     * 取消注册
     */
    public void unregister_MyHeadsetPlugBroadcastReceiver() {
        if (headsetPlugReceiver != null && mContext != null) {
            PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  unregister_MyHeadsetPlugBroadcastReceiver");
            PalmchatApp.getApplication().unregisterReceiver(headsetPlugReceiver);
            headsetPlugReceiver = null;
        }

    }

    /**
     * 只要播放的时候，就注册一下耳机和距离感应
     *
     * @param
     * @param
     */
    public void register_MyHeadsetPlugBroadcastReceiver() {

        if (headsetPlugReceiver == null) {
            PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  register_MyHeadsetPlugBroadcastReceiver preEarState: " + preEarState);
            headsetPlugReceiver = new MyHeadsetPlugBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.HEADSET_PLUG");
            PalmchatApp.getApplication().registerReceiver(headsetPlugReceiver, intentFilter);
        }

    }

    /**
     * 耳机监听
     *
     * @author Transsion
     */
    public class MyHeadsetPlugBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")) {
                int state = intent.getIntExtra("state", 0);
                if (state != preEarState) {
                    preEarState = state;
                    if (state == 0) { //拔出耳机
                        PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  拔出耳机");
                        pause();
                    } else if (state == 1) { //插入耳机
                        PalmchatLogUtils.i("zhh_2016_07_27", "CustomVideoController  插入耳机");
                        pause();
                    }
                }

            }
        }
    }

    //    private void startOpenVideo() {
//        if (mVideoView != null) {
//            mVideoView.setVisibility(GONE);
//            mVideoView.stopPlayback();
//            mVideoView = null;
//        }
//
//        mCenterPlayButton.setVisibility(View.INVISIBLE);
//        progress_loading.setVisibility(View.VISIBLE);
//        img_first_frame.setVisibility(View.VISIBLE);
//
//
//        mVideoView = (VideoView) mRoot.findViewById(R.id.videoview);
//        mVideoView.requestFocus();
//
//        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                Log.i("zhh_20160715", "CustomVideoController setOnCompletionListener  ");
//                if (mVideoView != null)
//                    mVideoView.seekTo(0);
//
//                img_first_frame.setVisibility(View.VISIBLE);
//
//                pause();
//            }
//        });
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                Log.i("zhh_20160715", "CustomVideoController setOnPreparedListener  " + " isPaused: " + isPaused);
//                //设置MediaPlayer的OnSeekComplete监听
////                mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
////                    @Override
////                    public void onSeekComplete(MediaPlayer mp) {
//
//                if (mVideoView != null && !isPaused) {
//                    progress_loading.setVisibility(View.INVISIBLE);
//                    img_first_frame.setVisibility(View.INVISIBLE);
//                    mVideoView.start();
//                    mHandler.sendEmptyMessage(SHOW_TIME);
//                }
////
////                    }
////                });
//
//            }
//        });
//
//        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                Log.i("zhh_20160715", "CustomVideoController setOnErrorListener  ");
//                stop();
//                return true;
//            }
//        });
//
//            /*因为该接口只能在API 17及以上才能使用，否则编译报错，所以加了版本控制*/
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//                public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                    switch (what) {
//                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                            Log.i("zhh_20160715", "CustomVideoController MEDIA_INFO_BUFFERING_START  ");
//                            progress_loading.setVisibility(View.VISIBLE);
//                            break;
//                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                            Log.i("zhh_20160715", "CustomVideoController MEDIA_INFO_BUFFERING_END  ");
//                            progress_loading.setVisibility(View.INVISIBLE);
//                            break;
//                    }
//                    return true;
//                }
//            });
//
//        }
//
//        mVideoView.setVisibility(View.VISIBLE);
//        mVideoView.setVideoPath(mPath);
//
//        isPaused = false;
//
//
//    }
}
