package com.afmobi.palmchat.ui.activity.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.db.DBState;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.customview.list.CircleAdapter;
import com.afmobi.palmchat.ui.customview.list.CircleAdapter.OnItemClickListener;
import com.afmobi.palmchat.ui.customview.list.LinearLayoutListView;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobigroup.gphone.R;
import com.core.cache.CacheManager;
import com.core.cache.CacheManager.Region;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RegionTwoActivity extends BaseActivity {

	private LinearLayoutListView listView1;
	private List<Map<String, Object>> data1;
	private CircleAdapter mCircleAdapter1;
	/* add by zhh 显示state列表 */
	private final int SHOW_STATE = 1;
	private boolean flag = false;
	String country;
	String country_code;
	Region region;
	String[] states;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void findViews() {
		setContentView(R.layout.activity_region);
		listView1 = (LinearLayoutListView) findViewById(R.id.list1);
		country = getIntent().getStringExtra(JsonConstant.KEY_COUNTRY);
		country_code= getIntent().getStringExtra(JsonConstant.KEY_COUNTRY_CODE);

		((TextView) findViewById(R.id.title_text)).setText(R.string.select_your_state);
		region = DBState.readAssertStatesList(country);
		// region = CacheManager.getInstance().getState(country);
		PalmchatLogUtils.println("RegionTwoActivity region:" + region);
		// final String title = country;
		flag = getIntent().getBooleanExtra(JsonConstant.KEY_FLAG, false);
		if (region != null) {
			states = region.getRegions();
			if (states != null && states.length > 0) {
				showStates(region, country, flag, states);
			}
		} else {
			backToRegion0ne();
		}

		ImageView back_button = (ImageView) findViewById(R.id.back_button);
		if (back_button != null) {
			back_button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					finish();

				}
			});
		}
	}

	@Override
	public void init() {

	}

	private void backToRegion0ne() {
		Intent intent = new Intent( );
		intent.putExtra(JsonConstant.KEY_CITY, "");
		intent.putExtra(JsonConstant.KEY_STATE, "");
		intent.putExtra(JsonConstant.KEY_COUNTRY, country);
		setResult(DefaultValueConstant.RESULT_10, intent);
		MyActivityManager.getScreenManager().popActivity();
	}

	/**
	 * add by zhh 显示state列表
	 */
	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_STATE: // 邮箱注册
				showStates(region, country, flag, states);
				break;
			default:
				break;
			}

		};
	};

	private void getData(Object object) {
		Region region = (Region) object;
		showStates(region, region.getName(), flag, region.getRegions());
	}

	private void showStates(final Region region, final String title, final boolean flag, String[] states) {
		String[] temp = null;
		if (flag) {
			temp = new String[states.length + 1];
			temp[0] = title;
			for (int i = 1; i < temp.length; i++) {
				temp[i] = states[i - 1];
			}
		} else {
			temp = new String[states.length];
			for (int i = 0; i < states.length; i++) {
				String state = states[i];
				if (!CommonUtils.isEmpty(state)) {
					temp[i] = state;
				}
			}
		}

		data1 = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < temp.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", temp[i]);
			map.put("arrow", R.drawable.arrow);
			data1.add(map);
		}

		if (data1 != null && data1.size() > 0) {
			mCircleAdapter1 = new CircleAdapter(this, data1, R.layout.setting_default_items, new String[] { "text", "arrow" }, new int[] { R.id.setting_title, R.id.setting_arrow });
			mCircleAdapter1.setItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(Map<String, Object> item, View view, int position) {
					if (flag && 0 == position) {
						Intent intent = new Intent( );
						intent.putExtra(JsonConstant.KEY_CITY, "");
						intent.putExtra(JsonConstant.KEY_STATE, "");
						intent.putExtra(JsonConstant.KEY_COUNTRY, region.getName());
						intent.putExtra(JsonConstant.KEY_COUNTRY_CODE,country_code);
						setResult(DefaultValueConstant.RESULT_10, intent);
						MyActivityManager.getScreenManager().popActivity();
					} else {
						Intent intent = new Intent(RegionTwoActivity.this, RegionThreeActivity.class);
						intent.putExtra(JsonConstant.KEY_REGION, region);
						intent.putExtra(JsonConstant.KEY_STATE, item.get("text").toString());
						intent.putExtra(JsonConstant.KEY_POSITION, flag == true ? position - 1 : position);
						intent.putExtra(JsonConstant.KEY_FLAG, flag);
						intent.putExtra(JsonConstant.KEY_REGION_LIST, region);
						intent.putExtra(JsonConstant.KEY_COUNTRY_CODE,country_code);
						startActivityForResult(intent, DefaultValueConstant.RESULT_10);
					}
				}
			});
			if (flag) {
				mCircleAdapter1.setPosition(0);
			}
			mCircleAdapter1.setFristBackground(R.drawable.uilist);
			mCircleAdapter1.setLastBackgroud(R.drawable.uilist);
			mCircleAdapter1.setCenterBackgroud(R.drawable.uilist);
			mCircleAdapter1.setBackgroud(R.drawable.uilist);
			listView1.setAdapter(mCircleAdapter1);
		} else {
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == DefaultValueConstant.RESULT_10) {
				Intent intent = new Intent( );
				intent.putExtra(JsonConstant.KEY_CITY, data.getStringExtra(JsonConstant.KEY_CITY));
				intent.putExtra(JsonConstant.KEY_STATE, data.getStringExtra(JsonConstant.KEY_STATE));
				intent.putExtra(JsonConstant.KEY_COUNTRY, data.getStringExtra(JsonConstant.KEY_COUNTRY));
				intent.putExtra(JsonConstant.KEY_COUNTRY_CODE, data.getStringExtra(JsonConstant.KEY_COUNTRY_CODE));
				intent.putExtra(JsonConstant.KEY_REGION_LIST, data.getSerializableExtra(JsonConstant.KEY_REGION_LIST));
				setResult(DefaultValueConstant.RESULT_10, intent);
				MyActivityManager.getScreenManager().popActivity();
			}
		}

	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
