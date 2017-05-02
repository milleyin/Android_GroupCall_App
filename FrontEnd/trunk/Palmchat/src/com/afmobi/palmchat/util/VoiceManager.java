package com.afmobi.palmchat.util;

import java.io.IOException;
import java.util.ArrayList;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.ui.customview.videoview.VedioManager;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.chats.adapter.ChattingAdapter;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.groupchat.adapter.GroupChattingAdapter;
import com.afmobi.palmchat.util.SensorManagerUtil.SensorChangedCallback;
import com.core.AfMessageInfo;
//import com.umeng.fb.util.Log;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

public class VoiceManager {
	private static final String TAG = VoiceManager.class.getSimpleName();

	private static VoiceManager instance;
	private MediaPlayer mediaPlayer = null;
	private MediaPlayer mediaPlayer2 = null;

	private String mPath;
	private boolean mPlaying;
	private View mBGView, mPlayIcon,mPalmCallPlay;
	private Activity mActivity;
	private OnCompletionListener mCompletionListener;
	private AfMessageInfo mAfMessageInfo;
	private Context mContext;
	/** 当前列表id */
	private int mPosition;
	/** mid */
	private String mMid = "";
	//记录数据库ID
	private int mId;
	private VoiceManager() {
		mediaPlayer = new MediaPlayer();
	}

	public static VoiceManager getInstance() {
		if (instance == null) {
			instance = new VoiceManager();
		}
		return instance;
	}

	public Activity getmActivity() {
		return mActivity;
	}

	public void setmActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}

	public View getPlayIcon() {
		return mPlayIcon;
	}

	public void setPlayIcon(View playIcon) {
		this.mPlayIcon = playIcon;
	}

	public void setMainMid(String mid) {
		this.mMid = mid;
	}

	public String getMainMid() {
		return this.mMid;
	}

	public void setListId(int id){
		this.mId = id;
	}

	public int getListId(){
		return mId;
	}

	/**
	 * 设置当前item位置
	 * 
	 * @param position
	 */
	public void setPosition(int position) {
		this.mPosition = position;
	}

	/**
	 * 当前播放的item的position
	 * 
	 * @return
	 */
	public int getPosition() {
		return mPosition;
	}

	public View getView() {
		return mBGView;
	}

	public void setView(View view) {
		mBGView = view;
	}

	public View getmPalmCallPlay(){
		return mPalmCallPlay;
	}
	public void setPalmCallPlay(View callplay){
		mPalmCallPlay = callplay;
	}

	public String getPath() {
		return mPath;
	}

	// MediaPlayer的isPlaying有时不准
	public boolean isPlaying() {
		return mPlaying;
	}

	public MediaPlayer getPlayer() {
		return mediaPlayer;
	}


	public int getVoiceTime(final String path) {
		if (null == mediaPlayer2) {
			mediaPlayer2 = new MediaPlayer();
		}
		int time = 0;
		try {
			mediaPlayer2.reset();
			mediaPlayer2.setDataSource(path);
			mediaPlayer2.prepare();

			PalmchatLogUtils.e("getVoiceTime", "duration=" + mediaPlayer.getDuration() + " | mPath =" + path);
			time = mediaPlayer2.getDuration();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;

	}

	/**
	 * 获取正在播放的位置 the duration in milliseconds
	 */
	public long getVoicePlayingPosition (){
		if(null!=mediaPlayer){
			return  mediaPlayer.getCurrentPosition();
		} else {
			return  0;
		}
	}

	/**
	 * 获取正在播放的总时长
	 */
	public long getVoicePlayingDuration(){
		if(null!=mediaPlayer){
			return  mediaPlayer.getDuration();
		} else {
			return  0;
		}
	}

	public void play(String path, final OnCompletionListener listener) {
		mCompletionListener = listener;
		mAfMessageInfo = null;
	  	SensorManagerUtil.getInstance().register_MyHeadsetPlugBroadcastReceiver(PalmchatApp.getApplication().getApplicationContext(), mSensorChangedCallback);
		SensorManagerUtil.getInstance().setmActivity(mActivity);
		this.mPath = path;
		try {
			if(VedioManager.getInstance().getVideoController() != null)
				VedioManager.getInstance().getVideoController().stop();

			if (mediaPlayer != null) {
				mediaPlayer.release();
				mediaPlayer = null;
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(path);
				mediaPlayer.prepare();
				mediaPlayer.start();
				mPlaying = true;

				AudioManager am = (AudioManager) PalmchatApp.getApplication().getSystemService(Context.AUDIO_SERVICE);
				am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

				mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						// 去掉播放完成音效
						// PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_AFTERVOICE);

						// SensorManagerUtil.getInstance().unregister_MyHeadsetPlugBroadcastReceiver();

						pause();
						listener.onCompletion();

					}
				});
				mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
					@Override
					public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
						try {
							pause();
							release();
							mediaPlayer = new MediaPlayer();
							listener.onError();

						} catch (Exception e) {
							listener.onCompletion();
						}
						return false;
					}
				});
			}
		} catch (Exception e) {
			pause();
			listener.onCompletion();

		}
	}

	public void play(final Context context, final AfMessageInfo afMessageInfo, String path, final OnCompletionListener listener) {
		mCompletionListener = listener;
		mAfMessageInfo = afMessageInfo;
		mContext = context;

		SensorManagerUtil.getInstance().register_MyHeadsetPlugBroadcastReceiver(PalmchatApp.getApplication().getApplicationContext(), mSensorChangedCallback);
		SensorManagerUtil.getInstance().setmActivity(mActivity);

		this.mPath = path;
		try {
			if(VedioManager.getInstance().getVideoController() != null)
				VedioManager.getInstance().getVideoController().stop();
			if (mediaPlayer != null) {
				mediaPlayer.release();
				mediaPlayer = null;
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(path);
				mediaPlayer.prepare();
				mediaPlayer.start();
				mPlaying = true;

				AudioManager am = (AudioManager) PalmchatApp.getApplication().getSystemService(Context.AUDIO_SERVICE);
				am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
				mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						// 去掉播放完成音效
						// PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_AFTERVOICE);

						// SensorManagerUtil.getInstance().unregister_MyHeadsetPlugBroadcastReceiver();

						pause();
						listener.onCompletion();

						palyNext(context, afMessageInfo, listener);

					}

				});
				mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
					@Override
					public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
						try {
							pause();
							release();
							mediaPlayer = new MediaPlayer();
							listener.onError();

						} catch (Exception e) {
							listener.onCompletion();

						}
						return false;
					}
				});
			}
		} catch (Exception e) {
			pause();
			listener.onCompletion();

		}
	}

	// 如果聊天界面有多个未读语音，则连着播放
	private void palyNext(final Context mContext, AfMessageInfo afMessageInfo, final OnCompletionListener listener) {

		mCompletionListener = listener;
		PalmchatLogUtils.println("playNext  afMessageInfo  " + afMessageInfo.toString());

		ArrayList<AfMessageInfo> listVoice = null;
		if (mContext instanceof Chatting) {
			listVoice = ((Chatting) mContext).getListVoiceChats();
		} else if (mContext instanceof GroupChatActivity) {
			listVoice = ((GroupChatActivity) mContext).getListVoiceChats();
		}

		if (listVoice != null && listVoice.size() > 0) {
			int position = ByteUtils.indexOf(listVoice, afMessageInfo._id);
			PalmchatLogUtils.println("palyNext  position  " + position);
			position = position + 1;
			if (listVoice.size() > position) {
				AfMessageInfo afVoice = listVoice.get(position);
				int status = afVoice.status;
				PalmchatLogUtils.println("playNext  afVoice  status  " + status);
				if (status == AfMessageInfo.MESSAGE_UNREAD) {
					SensorManagerUtil.getInstance().register_MyHeadsetPlugBroadcastReceiver(PalmchatApp.getApplication().getApplicationContext(), mSensorChangedCallback);
					SensorManagerUtil.getInstance().setmActivity(mActivity);
					if (listener.onGetContext() instanceof ChattingAdapter) {
						ChattingAdapter adapterListView = (ChattingAdapter) listener.onGetContext();
						int index = ByteUtils.indexOf(adapterListView.getLists(), afVoice._id);
						PalmchatLogUtils.println("index  " + index + "  msgId  " + afVoice._id);
						if (index != -1 && index < adapterListView.getCount()) {
							AfMessageInfo afVoiceMessageInfo = adapterListView.getItem(index);
							// 如果聊天界面有多个语音的话，则下面的连续播放
							afVoiceMessageInfo.autoPlay = true;
							afVoiceMessageInfo.status = AfMessageInfo.MESSAGE_READ;
							adapterListView.notifyDataSetChanged();
							PalmchatLogUtils.println("playNext  afVoice  " + afVoice.toString());
						}
					} else if (listener.onGetContext() instanceof GroupChattingAdapter) {
						GroupChattingAdapter adapterListView = (GroupChattingAdapter) listener.onGetContext();
						int index = ByteUtils.indexOf(adapterListView.getLists(), afVoice._id);
						PalmchatLogUtils.println("index  " + index + "  msgId  " + afVoice._id);
						if (index != -1 && index < adapterListView.getCount()) {
							AfMessageInfo afVoiceMessageInfo = adapterListView.getItem(index);
							afVoiceMessageInfo.autoPlay = true;
							afVoiceMessageInfo.status = AfMessageInfo.MESSAGE_READ;
							adapterListView.notifyDataSetChanged();
							PalmchatLogUtils.println("playNext  afVoice  " + afVoice.toString());
						}
					}
				}
			}
		}
	}

	public interface OnCompletionListener {
		void onCompletion();

		void onError();

		Object onGetContext();
	}

	public void mediaPlayerStop() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
	}


	/**
	 * 重新播放
	 */
	public void mediaPlayerrestart() {

		try {
			if (mediaPlayer != null) {
				mediaPlayer.seekTo(0);
				// mediaPlayer.start();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 暂停
	 */

	public void mediaPlayerpause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	/**
	 * 续播
	 */
	public void mediaPlayerstart() {
		if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
			mediaPlayer.start();
		}
	}

	public void release() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
	}

	// 人为停止播放，如onclick中
	public void pause() {
		SensorManagerUtil.getInstance().unregister_MyHeadsetPlugBroadcastReceiver();
		// 主线程调用
		refreshView();
	}

	// 自动停止播放，如activity pause\播放完成等
	public void completion() {
		// 决断传感设备有没有获取到屏幕关闭的锁，如果是则不能停止播放
		if (!SensorManagerUtil.getInstance().isAcquireWakeLock()) {
			pause();
		}
	}

	private void refreshView() {
		mPath = null;
		if (mBGView != null && mBGView.getBackground() instanceof AnimationDrawable) {
			AnimationDrawable playAnim = (AnimationDrawable) mBGView.getBackground();
			playAnim.stop();
		}

		if (mBGView != null && mPlayIcon != null) {
			mBGView.setBackgroundResource(R.drawable.voice_anim01);
			mPlayIcon.setBackgroundResource(R.drawable.chatting_voice_player_icon);
		}

		if(mPalmCallPlay != null){
			mPalmCallPlay.setBackgroundResource(R.drawable.btn_palmcall_voice_n);
		}

		try {
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mPlaying = false;
			AudioManager am = (AudioManager) PalmchatApp.getApplication().getSystemService(Context.AUDIO_SERVICE);
			am.abandonAudioFocus(null);
		} catch (Exception e) {
			PalmchatLogUtils.e(TAG, e.getMessage());
		}
	}

	private SensorChangedCallback mSensorChangedCallback = new SensorChangedCallback() {

		@Override
		public void onChanged(boolean isEarphone) {
			// TODO Auto-generated method stub
			if (mediaPlayer != null) {
				if (!mediaPlayer.isPlaying())
				{
					return;
				}
				int curPosition = 0;
				if (mediaPlayer.isPlaying()) {
					curPosition = mediaPlayer.getCurrentPosition();
					mediaPlayer.stop();

					Log.e("VoiceManager_zhh", "onChanged 原来 mediaPlayer.stop();");
				}
				mediaPlayer.release();
				mediaPlayer = null;
				mediaPlayer = new MediaPlayer();
				if (isEarphone) {
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
				}

				try {
					if (!TextUtils.isEmpty(mPath)) {
						mediaPlayer.setDataSource(mPath);
					} else {
						PalmchatLogUtils.println("---mPath---isEmpty-----");
					}
					mediaPlayer.prepare();
					mediaPlayer.seekTo(curPosition);
					mediaPlayer.start();
					Log.e("VoiceManager_zhh", "onChanged 新   mediaPlayer.start();");
					mPlaying = true;

					mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							// 去掉播放完成音效
							// PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_AFTERVOICE);

							// SensorManagerUtil.getInstance().unregister_MyHeadsetPlugBroadcastReceiver();

							pause();
							mCompletionListener.onCompletion();
							if (mAfMessageInfo != null && mContext != null)
								palyNext(mContext, mAfMessageInfo, mCompletionListener);

							Log.e("VoiceManager_zhh", "onChanged 新   mediaPlayer completion");
						}
					});
					mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
						@Override
						public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
							try {
								pause();
								release();
								mediaPlayer = new MediaPlayer();
								mCompletionListener.onError();

								Log.e("VoiceManager_zhh", "onChanged 新   mediaPlayer onError");
							} catch (Exception e) {
								mCompletionListener.onCompletion();
								Log.e("VoiceManager_zhh", "onChanged 新   mediaPlayer Exception_1");
							}
							return false;
						}
					});
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					Log.e("VoiceManager_zhh", "onChanged 新   mediaPlayer Exception_2");
				} catch (SecurityException e) {
					e.printStackTrace();
					Log.e("VoiceManager_zhh", "onChanged 新   mediaPlayer Exception_3");
				} catch (IllegalStateException e) {
					e.printStackTrace();
					Log.e("VoiceManager_zhh", "onChanged 新   mediaPlayer Exception_4");
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("VoiceManager_zhh", "onChanged 新   mediaPlayer Exception_2");
				}

			}
		}
	};
}
