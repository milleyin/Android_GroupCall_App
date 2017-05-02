package com.afmobi.palmchat.ui.activity.setting;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.ui.customview.switchbutton.SwitchButton;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.core.cache.CacheManager;
/**
 * 系统消息
 *
 */
public class SoundNotificationActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener{
	/**接收新消息通知*/
	private RelativeLayout mBtn_NewMsgNotify;
	/**声音*/
	private RelativeLayout mBtn_Sound;
	/**新消息提示，需要开声音才能看到*/
	private RelativeLayout mBtn_InNewMsgNotify;
	/**震动*/
	private RelativeLayout mBtn_Vibrate;
	/**弹窗回复*/
	private RelativeLayout mBtn_Popup;
	/**功能消息免打扰*/
	private RelativeLayout mBtn_NoDisturb;
	/**In-app Sounds*/
	private RelativeLayout mBtn_InSound;
	/**接收新消息通知CheckBox*/
	private SwitchButton mChx_new_msg_notify;
	/**声音checkbox*/
	private SwitchButton mChx_Sound;
	/**震动checkbox*/
	private SwitchButton mChk_Vibrate;
	/**弹窗震动checkbox*/
	private SwitchButton mChk_Popup;
	/**In-app Sounds checkbox*/
	private SwitchButton mChk_InappSound;
	/**震动、弹窗回复布局*/
    private LinearLayout mLlyt_Message;
	/**返回键*/
	private View back_button;
	/**标题*/
	private TextView mTxt_Title;
	/**In-appSounds下面的分割线20dp*/
	private TextView mLine_Sound;
	/**免打扰上面的分割线*/
	private TextView mLine_NoDisturbTop;
    /***/
	private final static int SOUND_SELECT = 10;
	
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_sound_notifaction);
		
		mTxt_Title = (TextView)findViewById(R.id.title_text);
		mTxt_Title.setText(getString(R.string.sound_receive_new_msg_prompt));
		
        back_button = findViewById(R.id.back_button);
		back_button.setOnClickListener(this);
		//item项
		mBtn_NewMsgNotify = (RelativeLayout)findViewById(R.id.btn_new_msg_notify);
		mBtn_Sound = (RelativeLayout)findViewById(R.id.r_sound);
        mBtn_InSound = (RelativeLayout)findViewById(R.id.r_in_app);
		mBtn_InNewMsgNotify = (RelativeLayout)findViewById(R.id.btn_sound);
		mBtn_Vibrate = (RelativeLayout)findViewById(R.id.btn_vibrate);
		mBtn_Popup = (RelativeLayout)findViewById(R.id.btn_popup);
		mBtn_NoDisturb = (RelativeLayout)findViewById(R.id.btn_no_disturb);
        mLlyt_Message = (LinearLayout)findViewById(R.id.r_message);
        mLine_Sound = (TextView)findViewById(R.id.sound_line);
        mLine_NoDisturbTop = (TextView)findViewById(R.id.sound_line_top_disturb);
		
		mBtn_NewMsgNotify.setOnClickListener(this);
		mBtn_Sound.setOnClickListener(this);
		mBtn_InNewMsgNotify.setOnClickListener(this);
		mBtn_Vibrate.setOnClickListener(this);
		mBtn_Popup.setOnClickListener(this);
		mBtn_NoDisturb.setOnClickListener(this);
		
		//checkbox选择项
		mChx_new_msg_notify = (SwitchButton)findViewById(R.id.checkbox_new_msg_notify);
		mChx_Sound = (SwitchButton)findViewById(R.id.checkbox_sound);
		mChk_Vibrate = (SwitchButton)findViewById(R.id.checkbox_vibrate);
		mChk_Popup = (SwitchButton)findViewById(R.id.checkbox_popup);
		mChk_InappSound= (SwitchButton)findViewById(R.id.checkbox_inapp_sound);
		
		mChx_new_msg_notify.setOnCheckedChangeListener(this);
		mChx_Sound.setOnCheckedChangeListener(this);
		mChk_Vibrate.setOnCheckedChangeListener(this);
		mChk_Popup.setOnCheckedChangeListener(this);
		mChk_InappSound.setOnCheckedChangeListener(this);
		
		boolean is_new_msg_notice = app.getSettingMode().isNewMsgNotice();
		mChx_new_msg_notify.setChecked(is_new_msg_notice);	
		mChx_Sound.setChecked(!app.getSettingMode().isMute());	
		mChk_Vibrate.setChecked(app.getSettingMode().isVibratio());	
		mChk_Popup.setChecked(app.getSettingMode().isPopMsg());
		mChk_InappSound .setChecked(app.getSettingMode().isInAppSound());

		if(!mChx_Sound.isChecked()) {
			mBtn_InNewMsgNotify.setVisibility(View.GONE);
          }else{
          	mBtn_InNewMsgNotify.setVisibility(View.VISIBLE);
          }
		
		showClickable(is_new_msg_notice);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_button:
			this.finish();
			break;
		//接收新消息通知
		case R.id.btn_new_msg_notify:
			if(mChx_new_msg_notify.isChecked()){
				mChx_new_msg_notify.setChecked(false);
			}else{
				mChx_new_msg_notify.setChecked(true);
			}
			
			break;
		//声音
		case R.id.r_sound:
			if(mChx_Sound.isChecked()){
				mChx_Sound.setChecked(false);
			}else{
				mChx_Sound.setChecked(true);
			}
			break;
		//in_app sounds
		case R.id.checkbox_inapp_sound: 
			if(mChk_InappSound.isChecked()){
				mChk_InappSound.setChecked(false);
			}else{
				mChk_InappSound.setChecked(true);
			}
			break;
		//新消息提示音
		case R.id.btn_sound:
			startActivityForResult(new Intent(this , SoundSettingActivty.class),SOUND_SELECT);
			break;
		//震动
		case R.id.btn_vibrate:
			if(mChk_Vibrate.isChecked()){
				mChk_Vibrate.setChecked(false);
			}else{
				mChk_Vibrate.setChecked(true);
			}
			break;
		//弹窗回复
		case R.id.btn_popup:
			if(mChk_Popup.isChecked()){
				mChk_Popup.setChecked(false);
			}else{
				mChk_Popup.setChecked(true);
			}
			break;
		//功能消息免打扰
		case R.id.btn_no_disturb:
			startActivity(new Intent(this , SoundNotDisturbNotificationActivity.class));
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		String str_afid = CacheManager.getInstance().getMyProfile().afId;
		switch (buttonView.getId()) {
		//接收新消息通知
		case R.id.checkbox_new_msg_notify:
			PalmchatApp.getApplication().mAfCorePalmchat.AfDbOnoffNofitymsg(str_afid, isChecked, true);
			app.getSettingMode().setNewMsgNotice(isChecked);
			showClickable(isChecked);
			break;
		//声音
		case R.id.checkbox_sound:
			PalmchatApp.getApplication().mAfCorePalmchat.AfDbSettingSetSoundOrVibrate(str_afid, isChecked, true);// 存储的值要与实际mute值取反
            app.getSettingMode().setMute(!isChecked);
            if(!isChecked){
            	mBtn_InNewMsgNotify.setVisibility(View.GONE);
            }else{
            	mBtn_InNewMsgNotify.setVisibility(View.VISIBLE);
            }
			break;
		//震动
		case R.id.checkbox_vibrate:
			PalmchatApp.getApplication().mAfCorePalmchat.AfDbSettingSetSoundOrVibrate(str_afid, isChecked, false);
			app.getSettingMode().setVibratio(isChecked);
			break;
		//checkbox_inapp_sound
		case R.id.checkbox_inapp_sound:
//			PalmchatApp.getApplication().mAfCorePalmchat.AfDbSettingSetSoundOrVibrate(str_afid, isChecked, false);
			SharePreferenceUtils.getInstance(PalmchatApp.getApplication() ).setInAppSound(str_afid, isChecked);
			app.getSettingMode().setInAppSound(isChecked);
			break;
		//弹窗回复
		case R.id.checkbox_popup:
			PalmchatApp.getApplication().mAfCorePalmchat.AfDbPopUpOpr(str_afid, isChecked, true);
			app.getSettingMode().setPopMsg(isChecked);
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @param isChecked
	 */
	private void showClickable(boolean isChecked) {
        if(!isChecked){
            CommonUtils.cancelNoticefacation(context);
        }
        setVisibleCheck(isChecked);
	}

	/**
	 * 根据保存值设置显示
	 * @param isVisible
	 */
    private void setVisibleCheck(boolean isVisible){
        if(isVisible){
            mBtn_Sound.setVisibility(View.VISIBLE);
            mBtn_InSound.setVisibility(View.VISIBLE);
            mLine_Sound.setVisibility(View.VISIBLE);
            mLlyt_Message.setVisibility(View.VISIBLE);
            if(!mChx_Sound.isChecked()){
                mBtn_InNewMsgNotify.setVisibility(View.GONE);
            }else{
                mBtn_InNewMsgNotify.setVisibility(View.VISIBLE);
            }
        }
        else {
            mBtn_Sound.setVisibility(View.GONE);
            mBtn_InNewMsgNotify.setVisibility(View.GONE);
            mBtn_InSound.setVisibility(View.GONE);
            mLine_Sound.setVisibility(View.GONE);
            mLlyt_Message.setVisibility(View.GONE);
        }
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == SOUND_SELECT){
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
