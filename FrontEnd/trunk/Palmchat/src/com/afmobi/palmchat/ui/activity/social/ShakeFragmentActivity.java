package com.afmobi.palmchat.ui.activity.social;

import android.os.Bundle;
import android.view.KeyEvent;

import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobigroup.gphone.R;
import com.core.cache.CacheManager;

public class ShakeFragmentActivity extends BaseFragmentActivity {

	private ShakeFragFragment shakeFrag;
//	private ShakeListener mShakeListener;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.slidingmenu_content);
//		mShakeListener = new ShakeListener(ShakeFragmentActivity.this);
		shakeFrag =  new ShakeFragFragment();
		replaceFragment(R.id.slidingmenu_content, shakeFrag);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			if(shakeFrag != null){
				shakeFrag.stopShakeListener();
				MyActivityManager.getScreenManager().popFragment(shakeFrag);
			}
		}
		return false;
	}

}
