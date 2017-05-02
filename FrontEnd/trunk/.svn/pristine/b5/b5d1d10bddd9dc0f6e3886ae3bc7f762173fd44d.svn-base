package com.afmobi.palmchat.ui.activity.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.customview.list.CircleAdapter;
import com.afmobi.palmchat.ui.customview.list.LinearLayoutListView;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.LanguageUtil;
import com.afmobi.palmchat.util.SharePreferenceUtils;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
/**
 * 语言设置
 *
 */
public class LanguageActivity extends BaseActivity implements OnClickListener {

	private LinearLayoutListView mListView;
	private List<Map<String, Object>> mDataList;
	private CircleAdapter mCircleAdapter;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}
	
	@Override
	public void findViews() {
		setContentView(R.layout.activity_language);

		findViewById(R.id.back_button).setOnClickListener(this);

		findViewById(R.id.btn_ok).setOnClickListener(this);
	}
	
	/**
	 *  Change the Language
	 */
	private void submitChangeLanguage() {
		int position = mCircleAdapter.getSelectead_language_position();
		Map<String, Object> item  = mCircleAdapter.getItem(position);
		LanguageUtil.updateLanguage(LanguageActivity.this, item.get("code").toString());
		SharePreferenceUtils.getInstance(LanguageActivity.this).setLocalLanguage(item.get("code").toString());
		SharePreferenceUtils.getInstance(LanguageActivity.this).setSelectedLanguagePosition(position);
	
		String language = CommonUtils.getRightLanguage(getApplication(), CommonUtils.TYPE_LOGIN);
		PalmchatLogUtils.println("setItemClickListener language "+language);
		PalmchatApp.getApplication().mAfCorePalmchat.AfCoreSetLanguage(language);
		showProgressDialog(R.string.please_wait);
		LanguageUtil.restartApp(LanguageActivity.this);
	}

	@Override
	public void init() {
		
		((TextView) findViewById(R.id.title_text)).setText(R.string.language);
		mListView = (LinearLayoutListView) findViewById(R.id.language_setting);

		String[] languageArr = getResources().getStringArray(R.array.language_setting);
		mDataList = new ArrayList<Map<String, Object>>();
		
		for (int i = 0; i < languageArr.length ; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", languageArr[i]);
			map.put("code", LanguageUtil.LANGUAGE_TYPE[i]);
			mDataList.add(map);
		}
		
		mCircleAdapter = new CircleAdapter(this, mDataList, R.layout.setting_default_two_items, new String[]{"text","code"}, 
				new int[]{R.id.setting_title,R.id.setting_arrow},true,SharePreferenceUtils.getInstance(context).getSelectedLanguagePosition());
		
		mCircleAdapter.setItemClickListener(new CircleAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(Map<String, Object> item, View view, int position) {
				
				mCircleAdapter.setIsSettingLanguage(true);
				mCircleAdapter.SetSelectead_language_position(position);
				mListView.setAdapter(mCircleAdapter);
				mCircleAdapter.notifyDataSetChanged();
			}
		});
		
		mCircleAdapter.setFristBackground(R.drawable.uilist);
		mCircleAdapter.setLastBackgroud(R.drawable.uilist);
		mCircleAdapter.setCenterBackgroud(R.drawable.uilist);
		mCircleAdapter.setBackgroud(R.drawable.uilist);
		mListView.setAdapter(mCircleAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;
			

		case R.id.btn_ok:
			submitChangeLanguage();
			break;

		default:
			break;
		}
		
	}
}
