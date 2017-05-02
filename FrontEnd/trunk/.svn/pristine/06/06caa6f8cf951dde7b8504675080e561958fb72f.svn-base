package com.afmobi.palmchat.ui.activity.friends;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.ui.activity.invitefriends.PhoneBookActivity;
import com.afmobi.palmchat.ui.activity.setting.MyAccountInfoActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.core.cache.CacheManager;
//import com.umeng.analytics.MobclickAgent;

public class InviteFriendsActivity extends BaseActivity implements OnClickListener { 

	@Override
	public void findViews() {
		// TODO Auto-generated method stub

		setContentView(R.layout.activity_invite_friends);
		
		findViewById(R.id.subitem1).setOnClickListener(this); 
		findViewById(R.id.back_button).setOnClickListener(this);
		
		((TextView)findViewById(R.id.title_text)).setText(R.string.invite_friends);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onPageStart(TAG);
//		MobclickAgent.onResume(this);
	} 
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		 MobclickAgent.onPageEnd(TAG);  
//		 MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		
		switch (v.getId()) {
		
		//phonebook
		case R.id.subitem1:
			
		/*	if (!CacheManager.getInstance().getMyProfile().is_bind_phone) {
				AppDialog appDialog = new AppDialog(context);
				String content = context.getString(R.string.bind_phone_messages);
				appDialog.createConfirmDialog(context, content,
						new OnConfirmButtonDialogListener() {
							@Override
							public void onRightButtonClick() {
								Intent it = new Intent();
								it.setClass(context, MyAccountInfoActivity.class);
								it.putExtra(MyAccountInfoActivity.STATE, MyAccountInfoActivity.PHONE_UNBIND);
								String countryCode = PalmchatApp.getOsInfo().getCountryCode();
								countryCode = "+" + countryCode;
								String country = PalmchatApp.getOsInfo().getCountry(context);
								it.putExtra(MyAccountInfoActivity.PARAM_COUNTRY_CODE, countryCode);
								it.putExtra(MyAccountInfoActivity.PARAM_COUNTRY_NAME, country);
								context.startActivity(it);
							}
							@Override
							public void onLeftButtonClick() {

							}
						});
				appDialog.show();

			} else {*/
				startActivity(new Intent(this, PhoneBookActivity.class));
			//}
			
			break;
			
		 
			
		case R.id.back_button:
			finish();
			break;

		default:
			break;
		}
	
	}
	
}
