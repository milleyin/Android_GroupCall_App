package com.afmobi.palmchat.ui.activity.store;

import android.os.Bundle;
import android.view.KeyEvent;

import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.ui.activity.store.fragment.StoreFragment;
import com.core.cache.CacheManager;
/**
 *  * 表情商店activity
 * @author Transsion
 *
 */
public class StoreFragmentActivity extends BaseFragmentActivity {

	/**表情商店fragment*/
	private StoreFragment mStoreFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidingmenu_content);
		mStoreFragment =  new StoreFragment();
		replaceFragment(R.id.slidingmenu_content, mStoreFragment);	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			CacheManager.getInstance().setStoreBack(true);
			if(mStoreFragment != null){
				MyActivityManager.getScreenManager().popFragment(mStoreFragment);
			}
		}
		return false;
	}
}
 