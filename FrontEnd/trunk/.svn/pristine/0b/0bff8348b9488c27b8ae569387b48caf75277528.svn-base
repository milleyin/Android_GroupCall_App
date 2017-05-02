package com.afmobi.palmchat.ui.activity.register;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.ui.customview.list.CircleAdapter;
import com.afmobi.palmchat.ui.customview.list.CircleAdapter.OnItemClickListener;
import com.afmobi.palmchat.ui.customview.list.LinearLayoutListView;
import com.afmobigroup.gphone.R;
import com.core.cache.CacheManager.Region;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RegionThreeActivity extends BaseActivity {
	
	private LinearLayoutListView listView1;
	private List<Map<String, Object>> data1;
	private CircleAdapter mCircleAdapter1;
	
	private TextView titleText;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if(mCircleAdapter1 != null){
					listView1.setAdapter(mCircleAdapter1);
				}
				break;
			case 1:
				dismissProgressDialog();
				if(mCircleAdapter1 != null){
					listView1.setAdapter(mCircleAdapter1);
				}
				break;

			default:
				break;
			}
			
			
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	private String countyCode;
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_region);
		listView1 = (LinearLayoutListView) findViewById(R.id.list1);
		titleText = (TextView)findViewById(R.id.title_text);
		final Region region =  (Region) getIntent().getSerializableExtra(JsonConstant.KEY_REGION);
		final String state = getIntent().getStringExtra(JsonConstant.KEY_STATE);
		final String country = getIntent().getStringExtra(JsonConstant.KEY_COUNTRY);
		final boolean flag = getIntent().getBooleanExtra(JsonConstant.KEY_FLAG,false);
		final int position = getIntent().getIntExtra(JsonConstant.KEY_POSITION,0);
		countyCode=getIntent().getStringExtra(JsonConstant.KEY_COUNTRY_CODE);
		titleText.setText(R.string.select_your_city);
		if(region != null ){
			showData(region, state, flag, position);
		}
		/*else{
			showProgressDialog(R.string.please_wait);
			new Thread(new Runnable() {
				public void run() {
					try {
						DBState.copyDataBase(RegionThreeActivity.this);
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						if(e != null){
							e.printStackTrace();
						}
						PalmchatLogUtils.println("findViews error");
					} finally{
						CommonUtils.getInitData(RegionThreeActivity.this);
						Region region_new = CacheManager.getInstance().getState(country);
						PalmchatLogUtils.println("RegionTwoActivity region2:"+region);
						if(region_new != null ){
							String [] states = region_new.getRegions();
							if(states != null && states.length > 0){
								showData(region_new, state, flag, position);
							}else{
								//getRegions(country);
							}
						}
						handler.sendEmptyMessage(1);
					}
				}
			}).start();
		}*/
		
		
		
		ImageView back_button = (ImageView)findViewById(R.id.back_button);
		if(back_button != null){
			back_button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		}
		
	}
	
	String title;
	private void showData(final Region region, final String state, final boolean flag, final int position) {
		title = region.getName() + "," + state;
		int position_new = getPosition(region,state);
		String [] cities = region.getCities().get(position_new);
		if(DefaultValueConstant.OTHERS.equals(state)){
			cities = new String[]{DefaultValueConstant.OTHER};
		}
		String [] temp = null;
		if (flag) {
			temp = new String[cities.length + 1];
			temp[0] = title;
			for (int i = 1; i < temp.length; i++) {
				temp[i] = cities[i - 1];
			}
		} else {
			temp = new String[cities.length];
			for (int i = 0; i < cities.length; i++) {
				temp[i] = cities[i];
			}
		}
		
		data1 = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < temp.length ; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", temp[i]);
			map.put("arrow", R.drawable.arrow);
			data1.add(map);
		}
		
		
		mCircleAdapter1 = new CircleAdapter(this, data1, R.layout.setting_default_items, new String[]{"text","arrow"}, new int[]{R.id.setting_title,R.id.setting_arrow});
		mCircleAdapter1.setItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(Map<String, Object> item, View view, int position) {
				String city = item.get("text").toString();
				if (flag && 0 == position) {
					city = "";
				}
				Intent intent = new Intent(RegionThreeActivity.this, RegionTwoActivity.class);
				intent.putExtra(JsonConstant.KEY_CITY, city);
				intent.putExtra(JsonConstant.KEY_STATE, state);
				intent.putExtra(JsonConstant.KEY_COUNTRY, region.getName());
				intent.putExtra(JsonConstant.KEY_REGION_LIST, region);
				intent.putExtra(JsonConstant.KEY_COUNTRY_CODE,countyCode);
				setResult(DefaultValueConstant.RESULT_10, intent);
				MyActivityManager.getScreenManager().popActivity();
			}
		});
		if (flag) {
			mCircleAdapter1.setPosition(0);
		}
		
		
		mCircleAdapter1.setFristBackground(R.drawable.uilist);
		mCircleAdapter1.setLastBackgroud(R.drawable.uilist);
		mCircleAdapter1.setCenterBackgroud(R.drawable.uilist);
		mCircleAdapter1.setBackgroud(R.drawable.uilist);
		
		
		handler.sendEmptyMessage(0);
	}
	
	private int getPosition(Region region,String state) {
		// TODO Auto-generated method stub
		String[] regions = region.getRegions();
		int size = regions.length;
		for (int i = 0; i < size; i++) {
			if(state.equals(regions[i])){
				return i;
			}
		}
		return 0;
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return false;
	}
	
	
}
