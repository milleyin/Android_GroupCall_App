package com.afmobi.palmchat.ui.activity.store;

import android.os.Bundle;

import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.ui.activity.store.fragment.AccountCenterFragment;

public class AccountCenterFragmentAcitivity extends BaseFragmentActivity {

	private AccountCenterFragment accountCenterFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.slidingmenu_content);
		accountCenterFragment = new AccountCenterFragment();
		replaceFragment(R.id.slidingmenu_content, accountCenterFragment);

	}

}
