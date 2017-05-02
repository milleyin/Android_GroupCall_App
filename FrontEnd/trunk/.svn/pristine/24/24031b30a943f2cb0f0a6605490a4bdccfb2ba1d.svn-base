package com.afmobi.palmchat.ui.activity.setting;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.core.Consts;
import com.core.cache.CacheManager;
/**
 * 免打扰界面
 *
 */
public class SoundNotDisturbNotificationActivity extends BaseActivity implements OnClickListener{

	/**全天开启*/
	private RelativeLayout mRelLay_AllDay;
	/**尽在夜间开启*/
	private RelativeLayout mRelLay_OnlyNight;
	/**关闭*/
	private RelativeLayout mRelLay_Close;
	/**全天开启*/
	private ImageView mImg_AllDay;
	/**仅在夜间开启*/
	private ImageView mImg_OnlyNight;
	/**关闭*/
	private ImageView mImg_Closet;
	/**返回按钮*/
	private ImageView back_button;
	/**标题*/
	private TextView titleText;
	
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_sound_no_disturb_notifaction);
		
		back_button = (ImageView)findViewById(R.id.back_button);
		back_button.setOnClickListener(this);
		
		titleText = (TextView)findViewById(R.id.title_text);
		titleText.setText(getString(R.string.sound_not_disturb));
		
		mRelLay_AllDay = (RelativeLayout) findViewById(R.id.r_all_day);
		mRelLay_OnlyNight = (RelativeLayout) findViewById(R.id.r_only_night);
		mRelLay_Close = (RelativeLayout) findViewById(R.id.r_close);
		
		mImg_AllDay = (ImageView) findViewById(R.id.img_all_day);
		mImg_OnlyNight = (ImageView) findViewById(R.id.img_only_night);
		mImg_Closet = (ImageView) findViewById(R.id.img_close);
		
		mRelLay_AllDay.setOnClickListener(this);
		mRelLay_OnlyNight.setOnClickListener(this);
		mRelLay_Close.setOnClickListener(this);
		
		showDefault();
	}

	/**
	 * 显示默认
	 */
	private void showDefault() {
		// TODO Auto-generated method stub
		int sel = app.getSettingMode().getMsgDisturb();
		switch (sel) {
		case Consts.AVOID_DISTURB_ALL_TIME:
			setSelectedOption(R.drawable.radiobutton_press, R.drawable.radiobutton_normal, R.drawable.radiobutton_normal);
			break;
		case Consts.AVOID_DISTURB_IN_NIGHT:
			setSelectedOption(R.drawable.radiobutton_normal, R.drawable.radiobutton_press, R.drawable.radiobutton_normal);
			break;
		case Consts.AVOID_DISTURB_OFF:
			setSelectedOption(R.drawable.radiobutton_normal, R.drawable.radiobutton_normal, R.drawable.radiobutton_press);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String str_afid = CacheManager.getInstance().getMyProfile().afId;
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;
		case R.id.r_all_day:
			setSelectedOption(R.drawable.radiobutton_press, R.drawable.radiobutton_normal, R.drawable.radiobutton_normal);
			app.getSettingMode().setMsgDisturb(Consts.AVOID_DISTURB_ALL_TIME);
			PalmchatApp.getApplication().mAfCorePalmchat.AfDbAvoidDisturb(str_afid ,Consts.AVOID_DISTURB_ALL_TIME,true);
			break;
		case R.id.r_only_night:
			setSelectedOption(R.drawable.radiobutton_normal, R.drawable.radiobutton_press, R.drawable.radiobutton_normal);
			app.getSettingMode().setMsgDisturb(Consts.AVOID_DISTURB_IN_NIGHT);
			PalmchatApp.getApplication().mAfCorePalmchat.AfDbAvoidDisturb(str_afid ,Consts.AVOID_DISTURB_IN_NIGHT,true);
			break;
		case R.id.r_close:
			setSelectedOption(R.drawable.radiobutton_normal, R.drawable.radiobutton_normal, R.drawable.radiobutton_press);
			app.getSettingMode().setMsgDisturb(Consts.AVOID_DISTURB_OFF);
			PalmchatApp.getApplication().mAfCorePalmchat.AfDbAvoidDisturb(str_afid ,Consts.AVOID_DISTURB_OFF,true);
			break;

		}
	}

	/**
	 * 选中
	 * @param res_all_day
	 * @param res_only_night
	 * @param res_close
	 */
	private void setSelectedOption(int res_all_day,int res_only_night,int res_close) {
		mImg_AllDay.setImageResource(res_all_day);
		mImg_OnlyNight.setImageResource(res_only_night);
		mImg_Closet.setImageResource(res_close);
	}


	
}
