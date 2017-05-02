package com.afmobi.palmchat.ui.activity.payment;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobigroup.gphone.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 充值成功页面
 * 
 * @author zhh 2015/12/5
 * 
 */
public class PaymentSuccessActivity extends BaseActivity implements OnClickListener {
	private Button btn_sure;

	@Override
	public void findViews() {
		setContentView(R.layout.activity_payment_success);
	}

	@Override
	public void init() {
		TextView tv_title = (TextView) findViewById(R.id.title_text);
		tv_title.setText(R.string.mobile_top_up);
		ImageView img_back = (ImageView) findViewById(R.id.back_button);
		img_back.setVisibility(View.GONE);
		btn_sure = (Button) findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);
	}

	/**
	 * 退出当前页面
	 */
	private void back() {
		MyActivityManager.getScreenManager().popActivity(MobileTopUpActivity.class);
		MyActivityManager.getScreenManager().popActivity(MobileTopUpSureActivity.class);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sure:
			back();
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		back();
	}

}
