package com.afmobi.palmchat.util;

import java.io.IOException;
import java.util.HashMap;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;


/**
 * 
 * @ClassName: SoundManagerUtil 
 * @author heguiming
 * @date 
 * @version V1.0
 */
public class SoundManager {
	private Context context;
	private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundPoolMap;
	private AudioManager mAudioManager;
	private final int REFRESH_VIEW = 1; //add by zhh 刷新界面

	public SoundManager(Context context){
		this.context = context;
		initSounds();
	}
	
	/**
	 * @Title: initSounds 
	 */
	private void initSounds() {
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		addShakeSound();
		addInAppSound();
	}
	
	public void addShakeSound() {
		addSound(R.raw.shake_shake, R.raw.shake_shake);
		addSound(R.raw.shake_match_male, R.raw.shake_match_male);
		addSound(R.raw.shake_match_female, R.raw.shake_match_female);
		addSound(R.raw.shake_nomatch, R.raw.shake_nomatch);
	}
	public void addInAppSound() {
		addSound(R.raw.send_emoji, R.raw.send_emoji);
		addSound(R.raw.send_pic, R.raw.send_pic);
		addSound(R.raw.send_msg, R.raw.send_msg);
		addSound(R.raw.voice_end, R.raw.voice_end);
		addSound(R.raw.broadcast_like, R.raw.broadcast_like);
		addSound(R.raw.broadcast_comment, R.raw.broadcast_comment);
		addSound(R.raw.broadcast_refresh, R.raw.broadcast_refresh);
		addSound(R.raw.joanna,R.raw.joanna);
		 
		
	}
	public void clearShakeSound() {
		mSoundPoolMap.remove(R.raw.shake_shake);
		mSoundPoolMap.remove(R.raw.shake_match_male);
		mSoundPoolMap.remove(R.raw.shake_match_female);
		mSoundPoolMap.remove(R.raw.shake_nomatch);
	}

	/**
	 * @Title: addSound 
	 * @param SoundID res ID
	 */
	public void addSound(int index, int SoundID) {
		mSoundPoolMap.put(index, mSoundPool.load(context, SoundID, 1));
	}

	/**
	 * @Title: playSound 
	 * @param index need to play key
	 */
	public void playSound(int index) {
		
		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int RingerMode = audio.getRingerMode();
		if (RingerMode != AudioManager.RINGER_MODE_NORMAL) {
			return;
		}
		
		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
	}
	public static final  int IN_APP_SOUND_SENDMSG=0,IN_APP_SOUND_SENDIMG=1,
			IN_APP_SOUND_SENDEMOTION=2,IN_APP_SOUND_AFTERVOICE=3,IN_APP_SOUND_LIKE=4,IN_APP_SOUND_COMMENT=5,IN_APP_SOUND_REFRESH=6;
	public void playInAppSound(int type) {
		
		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int RingerMode = audio.getRingerMode();
		if (RingerMode != AudioManager.RINGER_MODE_NORMAL) {
			return;
		}
		
		 PalmchatApp tmp_app = (PalmchatApp)context.getApplicationContext();
		   boolean is_inAppSound = tmp_app.getSettingMode().isInAppSound();
	        if(!is_inAppSound)
	        { 
	            return;
	        }
	    int index=R.raw.send_msg;
	    switch(type){
	    	case IN_APP_SOUND_SENDIMG:
	    		index=R.raw.send_pic; 
	    	break;
	    	case IN_APP_SOUND_SENDEMOTION:
	    		index=R.raw.send_emoji; 
	    	break;
	    	case IN_APP_SOUND_AFTERVOICE:
	    		index=R.raw.voice_end;
	    	break;
	    	case IN_APP_SOUND_LIKE:
//				if (VoiceManager.getInstance().isPlaying()) {
//					VoiceManager.getInstance().pause();
//				}

	    		index=R.raw.broadcast_like; 
	    	break;
	    	case IN_APP_SOUND_COMMENT:
	    		index=R.raw.broadcast_comment; 
	    	break;
	    	case IN_APP_SOUND_REFRESH:
	    		index=R.raw.broadcast_refresh; 
	    		break;
	    } 
		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
	}
    //added by chengyu 2014-3-21 14:05:12
    public void playNotification()
    {
        PalmchatApp tmp_app = (PalmchatApp)context.getApplicationContext();
        SettingMode sm = tmp_app.getSettingMode();
        if(sm==null)
        {

            return;
        }
        boolean is_mute = tmp_app.getSettingMode().isMute();
        if(is_mute)
        {

            return;
        }
        int id=0;
        String str_id = tmp_app.getSettingMode().getToneId();
        if(str_id!=null)
            id = Integer.valueOf(str_id);


        MediaPlayer player= new MediaPlayer();
//        String header = context.getResources().getString(R.string.tone_header);
        String header = "tone";
        if(id==0)
        {
            String system_tone_name = Settings.System.getString(context.getContentResolver(),Settings.System.NOTIFICATION_SOUND);
            if(TextUtils.isEmpty(system_tone_name)){
            	return;
            }
            if(player.isPlaying()){
                player.stop();
            }
            player.reset();
            try {
                player.setDataSource(context,Uri.parse(system_tone_name));
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
				PalmchatLogUtils.i("SoundManager",e.getMessage());
            }
            player.start();
        }
        else
        {
            int tmp_id = context.getResources().getIdentifier(header+id, "raw", PalmchatApp.getApplication().getPackageName());
            if(player.isPlaying()){
                player.stop();
            }
            player.reset();
            if(tmp_id <= 0){//
            	PalmchatLogUtils.println("Resources NotFound tmp_id "+tmp_id);
            	return;
            }
            player=MediaPlayer.create(context,tmp_id);
            player.start();
        }

    }

	/**
	 * @Title: playLoopedSound 
	 * @param index which res key
	 */
	public void playLoopedSound(int index) {
		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, -1, 1f);
	}
	
	/**
	 * @Title: removeAll 
	 */
	public void removeAll(){
		if (mSoundPoolMap != null) {
			mSoundPoolMap.clear();
			mSoundPoolMap = null;
		}
		
		if (mSoundPool != null) {
			mSoundPool.release();
			mSoundPool = null;
		}
		
		if (mAudioManager != null) {
			mAudioManager = null;
		}
	}
	
	
	/**
	 * add by zhh解决在非主线程更新UI的bug
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_VIEW:
				VoiceManager.getInstance().getView().setBackgroundResource(R.drawable.voice_anim01);
				VoiceManager.getInstance().getPlayIcon().setBackgroundResource(R.drawable.chatting_voice_player_icon);
				break;

			default:
				break;
			}
		}
	};
}
