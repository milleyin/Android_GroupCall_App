package com.afmobi.palmchat.ui.activity.invitefriends;
 
import android.os.Bundle;
import android.view.KeyEvent;

import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobigroup.gphone.R;

public class PublicAccountsFragmentActivity extends BaseFragmentActivity {

	private PublicAccountsFragment publicAccountsFragment;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.slidingmenu_content);
		publicAccountsFragment =  new PublicAccountsFragment();
		replaceFragment(R.id.slidingmenu_content, publicAccountsFragment);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			if(publicAccountsFragment != null){
				MyActivityManager.getScreenManager().popFragment(publicAccountsFragment);
			}
		}
		return false;
	}
}
