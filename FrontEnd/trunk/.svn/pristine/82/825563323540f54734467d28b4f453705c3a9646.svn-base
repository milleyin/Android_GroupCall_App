package com.afmobi.palmchat.ui.activity.palmcall;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.DateUtil;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfFriendInfo;
import com.core.AfPalmCallResp;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by heguiming on 2016/6/30.
 */
public class PalmCallRecentsAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<AfPalmCallResp.AfPalmCallRecord> mHotList;
    private HashMap<String,AfPalmCallResp.AfPalmCallHotListItem> mHashMapInfo;
    private final int INCOMING = 1;//来电
    private final int OUTCOMING = 2;//去电
    private final int NOANSWER = 3;//未接电话
    /**本机号的语音简介*/
    private String  mCurAudioIntro = "";
    private AnimationDrawable animDrawable;// 播放语音播放效果的动画
    public static final String DOWNLOAD_SUCCESS_SUFFIX = "_COMPLETE"; // Successful download suffix
    /***/
    private String mCurAfId = "";
    public static final int STRATVOIVE = 1;
    public static final int STOPVOIVE = 2;
    public PalmCallRecentsAdapter(Activity mActivity){
        this.mActivity = mActivity;
        this.mHashMapInfo = new HashMap<String,AfPalmCallResp.AfPalmCallHotListItem>();
        this.mHotList = new ArrayList<AfPalmCallResp.AfPalmCallRecord>();
        this.mCurAfId = CacheManager.getInstance().getMyProfile().afId;
        PalmCallVoiceManager.getInstance().setActivity(mActivity);
    }

    @Override
    public int getCount() {
        return mHotList.size();
    }

    @Override
    public AfPalmCallResp.AfPalmCallRecord getItem(int position) {
        return mHotList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.activity_palmcall_recents_item,null);
            holder.mIvCallPhoto = (ImageView)convertView.findViewById(R.id.iv_call_photo);
            holder.mTvCallAge = (TextView)convertView.findViewById(R.id.tv_call_age);
            holder.mTvCallName = (TextView)convertView.findViewById(R.id.tv_call_name);
            holder.mTvCallAnswerinlg = (TextView)convertView.findViewById(R.id.tv_call_recents_answerinlg);
            holder.mIvCallPlay = (ImageView)convertView.findViewById(R.id.iv_call_recents_play);
            holder.mIvCallState = (ImageView)convertView.findViewById(R.id.iv_call_state);
            holder.mTvCallTime = (TextView)convertView.findViewById(R.id.tv_call_time);
            holder.mIvCallRecents = (ImageView)convertView.findViewById(R.id.iv_call_recents);
            holder.mRlCallRecentsPlay = (RelativeLayout)convertView.findViewById(R.id.rl_call_recents_play);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        final AfPalmCallResp.AfPalmCallHotListItem afFriendInfo =((mHashMapInfo==null)||(mHashMapInfo.get(mHotList.get(position).afId)==null))?(new AfPalmCallResp.AfPalmCallHotListItem()):mHashMapInfo.get(mHotList.get(position).afId);
        holder.mTvCallName.setText(afFriendInfo.name);
        holder.mTvCallAge.setText(afFriendInfo.age+"");

//        holder.mTvCallTime.setText(DateUtil.dateChangeStrForChats(new Date(mHotList.get(position).callTime), PalmchatApp.getApplication()));
        holder.mTvCallTime.setText(DateUtil.formatTimeStampForString(mActivity,mHotList.get(position).callTime));
        holder.mTvCallAge.setBackgroundResource(Consts.AFMOBI_SEX_MALE == afFriendInfo.sex ? R.drawable.bg_palmcall_age_male : R.drawable.bg_palmcall_age_female);
        holder.mTvCallAge.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == afFriendInfo.sex ? R.drawable.bg_palmcall_gender_male : R.drawable.bg_palmcall_gender_female, 0, 0, 0);
        int mType = mHotList.get(position).callType;
        switch (mType){
            case INCOMING:
                holder.mIvCallState.setBackgroundResource(R.drawable.ic_palmcall_recents_incomingcall);
                holder.mTvCallAnswerinlg.setText(R.string.call_recents_incoming);
                Resources inresource = (Resources) mActivity.getBaseContext().getResources();
                ColorStateList incs = (ColorStateList) inresource.getColorStateList(R.color.broadcast_trending_868892);
                holder.mTvCallAnswerinlg.setTextColor(incs);
                holder.mTvCallTime.setTextColor(incs);
                break;
            case OUTCOMING:
                holder.mIvCallState.setBackgroundResource(R.drawable.ic_palmcall_recents_outcomingcall);
                holder.mTvCallAnswerinlg.setText(R.string.call_recents_outcoming);
                Resources outresource = (Resources) mActivity.getBaseContext().getResources();
                ColorStateList outcs = (ColorStateList) outresource.getColorStateList(R.color.broadcast_trending_868892);
                holder.mTvCallAnswerinlg.setTextColor(outcs);
                holder.mTvCallTime.setTextColor(outcs);
                break;
            case NOANSWER:
            case AfPalmCallResp.AFMOBI_CALL_TYPE_DECLINE:
                holder.mIvCallState.setBackgroundResource(R.drawable.ic_palmcall_recents_noanswer);
                holder.mTvCallAnswerinlg.setText(R.string.call_recents_noanswer);
                Resources noresource = (Resources) mActivity.getBaseContext().getResources();
                ColorStateList nocs = (ColorStateList) noresource.getColorStateList(R.color.chatting_room_at_me_color);
                holder.mTvCallAnswerinlg.setTextColor(nocs);
                holder.mTvCallTime.setTextColor(nocs);
                break;
        }
        holder.mIvCallRecents.setOnClickListener(new View.OnClickListener() {//点击call按钮请求电话
            @Override
            public void onClick(View v) {
                PalmchatLogUtils.i("PalmCallRecentsActivity", "---mCallSomeOneListener-!=null---");
                // mCallSomeOneListener.callOneInfo(v,afFriendInfo.afid);
                PalmCallRecentsActivity palmCallRecentsActivity = (PalmCallRecentsActivity) mActivity;
                if(palmCallRecentsActivity != null){
                    palmCallRecentsActivity.call(afFriendInfo);
                }
            }
        });
        if(afFriendInfo.mediaDescUrl == null || afFriendInfo.mediaDescUrl.equals("null")){
            holder.mRlCallRecentsPlay.setVisibility(View.GONE);
        }else{
            holder.mRlCallRecentsPlay.setVisibility(View.VISIBLE);
            PalmCallVoiceManager.getInstance().setViewHashMap(holder.mIvCallPlay.hashCode(),System.currentTimeMillis());
            final String urlPath = CommonUtils.splicePalcallAudioUrl(afFriendInfo.mediaDescUrl,afFriendInfo.afid);
            final String path = PalmCallVoiceManager.getInstance().showStopAnim(urlPath,holder.mIvCallPlay,mHotList.get(position)._id);
            holder.mRlCallRecentsPlay.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(afFriendInfo.mediaDescUrl)){
                        PalmCallVoiceManager.getInstance().bindVoiceView(holder.mIvCallPlay,path,urlPath,mHotList.get(position)._id,holder.mIvCallPlay.hashCode());
                    }
                }
            });
        }

        ImageManager.getInstance().DisplayAvatarImageCall(holder.mIvCallPhoto, CommonUtils.splicePalcallCoverUrl(afFriendInfo.coverUrl,afFriendInfo.afid),
                (byte)afFriendInfo.sex,null,true,false);
        return convertView;
    }

    class ViewHolder{
        TextView mTvCallName,mTvCallAge,mTvCallAnswerinlg,mTvCallTime;
        ImageView mIvCallPhoto,mIvCallPlay,mIvCallState,mIvCallRecents;
        RelativeLayout mRlCallRecentsPlay;
    }

    public void updatelist(HashMap<String,AfPalmCallResp.AfPalmCallHotListItem> afidinfo,ArrayList<AfPalmCallResp.AfPalmCallRecord> list,boolean isRefresh){
        mCurAudioIntro = "";
        if(isRefresh){
            mHashMapInfo.clear();
            mHotList.clear();
        }
        if(afidinfo!= null){
            mHashMapInfo.putAll(afidinfo);
        }
        if(list != null){
            mHotList.addAll(list);
        }
        notifyDataSetChanged();
    }

  /*  private void bindVoiceView(ViewHolder holder,String path,int position,String mediaurl,int id){
        boolean exists ;//判断本地是否有地址
        if (!TextUtils.isEmpty(path)) {
            exists = IsVoiceFileExistsSDcard(path);
            if (exists) {
                path = path + DOWNLOAD_SUCCESS_SUFFIX;
                Play(path,holder,mHotList.get(position).afId,id);//本地已有音频地址所以直接播放
            } else {
                if(null != mCallVoiceOnclick){//要下载语音的话显示dialog
                    mCallVoiceOnclick.CallVoice(STRATVOIVE);
                }
                File file = new File(path);
                downloadVoice(holder,file,position,mediaurl,id);
            }
        }
    }

    *//**
     *  下载音频
     * @param holder
     * @param file
     * @param position
     *//*
    private void downloadVoice(final ViewHolder holder,final File file,final int position,final String url,final int id){
        final String voice_path = file.getPath();
        // PalmchatLogUtils.e("downloadVoice", info.url);
        if (!CacheManager.getInstance().getGifDownLoadMap().containsKey(url)) {
            CacheManager.getInstance().getGifDownLoadMap().put(url, voice_path);
            String voiceUrl=url;
            if(!voiceUrl.startsWith(JsonConstant.HTTP_HEAD)){
                voiceUrl=PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerInfo()[Constants.CORE_SERVER_BROADCAST_MEDIA_HOST]+url;
            }
            PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBroadcastDownload(voiceUrl, voice_path, null, new AfHttpResultListener() {
                @Override
                public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
                    if (code == Consts.REQ_CODE_SUCCESS) {
                        if(null != mCallVoiceOnclick){//下载完成后关闭dialog
                            mCallVoiceOnclick.CallVoice(STOPVOIVE);
                        }
                        File newFile = new File(voice_path + DOWNLOAD_SUCCESS_SUFFIX);
                        boolean falg = file.renameTo(newFile);
                        String audioUrl = newFile.getPath();
                        Play(audioUrl,holder,mHotList.get(position).afId,id);
                    }
                    if (CacheManager.getInstance().getGifDownLoadMap().containsKey(url)) {
                        CacheManager.getInstance().getGifDownLoadMap().remove(url);
                    }
                    if(null != mCallVoiceOnclick){//下载完成后关闭dialog
                        mCallVoiceOnclick.CallVoice(STOPVOIVE);
                    }
                }
            });
        }

    }

    *//**
     * 判断本地是否有该音频地址
     * @param voicePath
     * @return
     *//*
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


    private void Play(String audiourl, final ViewHolder holder,String afid,int id) {
        final File file = new File(audiourl);
        if (file.exists() && file.length() > 0) {
            boolean isPlay = VoiceManager.getInstance().isPlaying();
            if (isPlay) {
                VoiceManager.getInstance().pause();

                if (holder.mIvCallPlay.equals(VoiceManager.getInstance().getmPalmCallPlay())) {
                    return;
                }
            }

            if (CommonUtils.isEmpty(VoiceManager.getInstance().getPath())) {
                VoiceManager.getInstance().setPalmCallPlay(holder.mIvCallPlay);
                VoiceManager.getInstance().setmActivity(mActivity);
                VoiceManager.getInstance().setMainMid(afid);
                VoiceManager.getInstance().setListId(id);
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
                        Toast.makeText(mActivity,mActivity.getString(R.string.audio_error), Toast.LENGTH_SHORT).show();
                    }
                });
                startPlayAnim(holder.mIvCallPlay);
            }
        } else {
            Toast.makeText(mActivity,mActivity.getString(R.string.no_audio_file), Toast.LENGTH_SHORT).show();
        }
    }

    private void startPlayAnim(final View view) {
        view.post(new Runnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                animDrawable = ((AnimationDrawable) mActivity.getResources().getDrawable(R.anim.palmcall_voice_animation));
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    view.setBackgroundDrawable(animDrawable);
                }else{
                    view.setBackground(animDrawable);
                }
                if (animDrawable != null && !animDrawable.isRunning()) {
                    animDrawable.setOneShot(false);
                    animDrawable.start();
                }
            }
        });

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

    private String showStopAnim(final String path, ViewHolder viewHolder,int id) {
        final String voiceName = PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(path);
        String voicePaht = RequestConstant.VOICE_CACHE + voiceName;
        String mPath = voicePaht + DOWNLOAD_SUCCESS_SUFFIX;
        if (!mPath.equals(VoiceManager.getInstance().getPath()) && VoiceManager.getInstance().isPlaying() && id != VoiceManager.getInstance().getListId()) {
            stopPlayAnim(viewHolder.mIvCallPlay);
            Log.e("showStopAnim", "stopPlayAnim");

        } else if (mPath.equals(VoiceManager.getInstance().getPath()) && VoiceManager.getInstance().isPlaying() && id == VoiceManager.getInstance().getListId()) {
            startPlayAnim(viewHolder.mIvCallPlay);
            Log.e("showStopAnim", "startPlayAnim");
        } else {
            stopPlayAnim(viewHolder.mIvCallPlay);
            Log.e("showStopAnim", "stopPlayAnim");

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
    }*/
}
