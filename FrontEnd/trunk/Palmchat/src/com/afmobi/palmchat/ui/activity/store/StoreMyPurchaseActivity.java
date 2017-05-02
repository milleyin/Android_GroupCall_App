package com.afmobi.palmchat.ui.activity.store;

import com.afmobi.palmchat.BaseActivity;
import com.afmobigroup.gphone.R;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 我的购买界面
 * @author starw
 *
 */
public class StoreMyPurchaseActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = StoreMyPurchaseActivity.class.getSimpleName();
	
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.store_my_purchase);
		((TextView)findViewById(R.id.title_text)).setText(R.string.my_purchase);
		
		findViewById(R.id.back_button).setOnClickListener(this);
		
		findViewById(R.id.my_purchase1).setOnClickListener(this);
		findViewById(R.id.my_purchase2).setOnClickListener(this);
		findViewById(R.id.my_purchase3).setOnClickListener(this);
		findViewById(R.id.my_purchase4).setOnClickListener(this);
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		//key back
		case R.id.back_button:
			finish();
			break;
			
		//my emoji
		case R.id.my_purchase1:
			startActivity(new Intent(context, StoreMyEmojiActivity.class));
			break;
			
		//my gift
		case R.id.my_purchase2:
			
			break;
			
		//my props
		case R.id.my_purchase3:
	
			break;
	
		//purchase history 跳转购买记录
		case R.id.my_purchase4:
			startActivity(new Intent(context, StorePurchaseHistoryActivity.class));
			break;
			
		}
	}

}
