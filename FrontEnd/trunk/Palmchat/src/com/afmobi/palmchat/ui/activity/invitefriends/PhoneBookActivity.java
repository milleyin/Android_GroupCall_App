package com.afmobi.palmchat.ui.activity.invitefriends;

import android.os.Bundle;

import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobigroup.gphone.R;

public class PhoneBookActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.slidingmenu_content);
		InviteFriendsFragment fragment =  new InviteFriendsFragment();
		replaceFragment(R.id.slidingmenu_content, fragment);
		
	}
}
