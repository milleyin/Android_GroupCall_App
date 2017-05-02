package com.afmobi.palmchat.ui.activity.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.list.CircleAdapter;
import com.afmobi.palmchat.ui.customview.list.CircleAdapter.OnItemBindListener;
import com.afmobi.palmchat.ui.customview.list.LinearLayoutListView;
import com.afmobi.palmchat.util.StringUtil;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 我的账户
 *
 */
public class MyAccountActivity extends BaseActivity implements OnClickListener,
		CircleAdapter.OnClickListener, OnItemBindListener, AfHttpResultListener {
	
	public static final String KEY_AFID = "Afid";

	private final static int INDEX_AFID = 0;
	private final static int INDEX_PHONE = 1;
	private final static int INDEX_EMAIL = 2;
	private final static int INDEX_SECURITY = 3;
	private final static int INDEX_MAX = 4;
	/***/
	public final static byte BIND_STATE_UNLINKED = -0X1;
	/**绑定状态未验证过*/
	public final static byte BIND_STATE_UNVERIFIED = 0X1;
	/**绑定状态以及验证过*/
	public final static byte BIND_STATE_VERIFIED = 0X2;
	
	public static final int SET_SUCCESS = 8000;
	public static final int SET_SUCCESS_PHONE = 8001;
	/**返回按钮*/
	private ImageView mBtnBack;
	/**标题*/
	private TextView mTextTitle;
	
	private String mDispText[] = new String[INDEX_MAX];
	private byte mState[] = new byte[INDEX_MAX];
	
	private CircleAdapter mCircleAdapter;
	private LinearLayoutListView mLinearLayoutListView;
	private boolean mIsFirst = true;
	/**本地库类*/
	private AfPalmchat mAfCorePalmchat;
	private View mView_Parent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void findViews() {
		setContentView(R.layout.activity_my_account);
		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		mAfCorePalmchat.AfHttpFindPwdGetQuestion(null, Consts.AF_LOGIN_AFID, null, this);
	}

	@Override
	public void init() {
		
	}
	
	protected void onResume(){
		super.onResume();
		update();
	}
	
	protected void onDestroy(){
		super.onDestroy();
		mDispText = null;
		mState = null;
		mCircleAdapter = null;
	}
	
	private void update(){
		if(mIsFirst){
			init(mIsFirst);	
			mIsFirst = false;
		}else{
			init(false);	
		}
	}
	
	/**
	 * 初始化
	 * @param first
	 */
	private void init(boolean first){
		AfProfileInfo profile = CacheManager.getInstance().getMyProfile();
		
		mDispText[INDEX_PHONE] = profile.phone;
		mDispText[INDEX_EMAIL] = profile.email;
		
		if( profile.is_bind_email && !TextUtils.isEmpty(mDispText[INDEX_EMAIL]) ){
			mState[INDEX_EMAIL] = BIND_STATE_VERIFIED;
		}else{
			if( TextUtils.isEmpty(mDispText[INDEX_EMAIL])){
				mState[INDEX_EMAIL] = BIND_STATE_UNLINKED;
			}else{
				mState[INDEX_EMAIL] = BIND_STATE_UNVERIFIED;
			}
		}
		if( profile.is_bind_phone && !TextUtils.isEmpty(mDispText[INDEX_PHONE]) ){
			mState[INDEX_PHONE] = BIND_STATE_VERIFIED;
		}else{
			mState[INDEX_PHONE] = BIND_STATE_UNLINKED;
			mDispText[INDEX_PHONE] = null;
		}
		if(mLinearLayoutListView != null){
			mLinearLayoutListView.removeAllViews();
		}
		if( first ){
			mDispText[INDEX_AFID] = profile.showAfid();
			mState[INDEX_AFID] = BIND_STATE_VERIFIED;
			
			mBtnBack = (ImageView) this.findViewById(R.id.back_button);
			mBtnBack.setOnClickListener(this);

			mTextTitle = (TextView)this.findViewById(R.id.title_text);
			mTextTitle.setText(R.string.my_account);
			
			mLinearLayoutListView = (LinearLayoutListView) this.findViewById(R.id.setting_account);
			String [] array = getResources().getStringArray(R.array.setting_text_myaccount);
			ArrayList<Map<String, Object>> data = new ArrayList<Map<String,Object>>(); 
			
			for (int i = 0; i < array.length ; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("text", array[i]);
				data.add(map);
			}
			mCircleAdapter = new CircleAdapter(this, data, R.layout.setting_default_items, new String[]{"text"}, new int[]{R.id.setting_title});
			mCircleAdapter.setOnClickListener(this);
			mCircleAdapter.setFristBackground(R.drawable.uilist);
			mCircleAdapter.setLastBackgroud(R.drawable.uilist);
			mCircleAdapter.setCenterBackgroud(R.drawable.uilist);
			mCircleAdapter.setBackgroud(R.drawable.uilist);
			mCircleAdapter.setOOnItemBindListener(this);
			mLinearLayoutListView.setAdapter(mCircleAdapter);
		}else{
			mLinearLayoutListView.setAdapter(mCircleAdapter);
		}

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_email:{
			
			break;
		}
		
		case R.id.back_button:{
			finish();
			break;
		}
		
		default:
			break;
		}
	}

	/**
	 * 给每一个item赋值和设置颜色
	 * @param position
	 * @param b_parent
	 */
	@Override
	public void OnBindView(int position, View b_parent) {
		mView_Parent = b_parent;
		TextView v = (TextView) b_parent.findViewById(R.id.setting_text);
		v.setVisibility(View.VISIBLE);
		int color = Color.BLACK;
		if( BIND_STATE_VERIFIED == mState[position]){
			color = Color.BLACK;
			v.setText(mDispText[position]);
		}else if(BIND_STATE_UNVERIFIED == mState[position]){
			color = Color.RED;
			v.setText( R.string.unverified);
		}else if(BIND_STATE_UNLINKED ==  mState[position]){
			color = Color.RED;
			v.setText( R.string.unlinked);
		} else {
			color = Color.BLACK;
			AfProfileInfo info = CacheManager.getInstance().getMyProfile();
			boolean isSet = new ReadyConfigXML().getBoolean(ReadyConfigXML.KEY_ALREAD_SET + info.afId);
			if (isSet) {
				color = Color.BLACK;
				v.setText(R.string.setted);
			} else {
				color = Color.RED;
				v.setText(R.string.not_set);
			}
		}
		v.setTextColor(color);
		if(INDEX_AFID ==  position){

		}
	}

	
	@Override
	public void onClick(View v, int position) {

		switch (position) {
			//手机
			case INDEX_PHONE:
				entryPhone();
				break;
			//邮箱
			case INDEX_EMAIL:
				entryEmail();
				break;
			//密保问题
			case INDEX_SECURITY:
				mView_Parent = v;
				String afid = CacheManager.getInstance().getMyProfile().afId;
				if (!StringUtil.isNullOrEmpty(afid)) {
					showProgressDialog(R.string.loading);
					mAfCorePalmchat.AfHttpFindPwdGetQuestion(null, Consts.AF_LOGIN_AFID, afid, this);
				}
				break;
		}
	}
	
	/**
	 * 进入手机选项
	 */
	private void entryPhone(){
		Intent it = new Intent();
		it.setClass(this, MyAccountInfoActivity.class);
		//手机已认证
		if( mState[INDEX_PHONE] == BIND_STATE_VERIFIED){
			it.putExtra(MyAccountInfoActivity.STATE, MyAccountInfoActivity.PHONE_UPDATE_BIND);
			it.putExtra(MyAccountInfoActivity.PARAM_INFO, mDispText[INDEX_PHONE]);
		}else{
			//手机未认证
			it.putExtra(MyAccountInfoActivity.STATE, MyAccountInfoActivity.PHONE_UNBIND);
			String countryCode = PalmchatApp.getOsInfo().getCountryCode();
			countryCode = "+" + countryCode;
			String country = PalmchatApp.getOsInfo().getCountry(context);			
			it.putExtra(MyAccountInfoActivity.PARAM_COUNTRY_CODE, countryCode);
			it.putExtra(MyAccountInfoActivity.PARAM_COUNTRY_NAME, country);
		}
		this.startActivityForResult(it,SET_SUCCESS_PHONE);
	}
	
	/**
	 * 进入邮箱项
	 */
	private void entryEmail(){
		Intent it = new Intent();
		it.setClass(this, MyAccountInfoActivity.class);
		if( mState[INDEX_EMAIL] == BIND_STATE_VERIFIED){
			it.putExtra(MyAccountInfoActivity.STATE, MyAccountInfoActivity.EMAIL_BIND_VERIFICODE);
		 }else if(  mState[INDEX_EMAIL] == BIND_STATE_UNVERIFIED ){
			it.putExtra(MyAccountInfoActivity.STATE, MyAccountInfoActivity.EMAIL_BIND_UNVERIFI);
		}else{
			it.putExtra(MyAccountInfoActivity.STATE, MyAccountInfoActivity.EMAIL_UNBIND);
		}
		it.putExtra(MyAccountInfoActivity.PARAM_INFO, mDispText[INDEX_EMAIL]);
		this.startActivity(it);	
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return false;
	}
	
	@Override
	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		dismissProgressDialog();
		if (code == Consts.REQ_CODE_SUCCESS) {
			if (flag == Consts.REQ_PSD_GET_QUESTION_EX) {
				AfProfileInfo info = CacheManager.getInstance().getMyProfile();
				new ReadyConfigXML().saveBoolean(ReadyConfigXML.KEY_ALREAD_SET + info.afId, true);
				if(user_data != null){
					showToast(R.string.you_already_set_your_security_questions_yet);
				}
			}
			Log.e("TAG", "----onActivityResult:" + mView_Parent);
			if (null != mView_Parent) {
				TextView v = (TextView) mView_Parent.findViewById(R.id.setting_text);
				v.setTextColor(Color.BLACK);
				v.setText(R.string.setted);
			}
		} else {
			if(user_data == null){
				if(code != -56){
					mAfCorePalmchat.AfHttpFindPwdGetQuestion(null, Consts.AF_LOGIN_AFID, null, this);
				}
				return;
			}
			if (code == -56) {
					showCustomDialog(R.string.you_havent_set_your_security_questions_yet);
			} else {
				showToast(R.string.failure);
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == SET_SUCCESS) {
			Log.e("TAG", "----onActivityResult:" + mView_Parent);
			AfProfileInfo info = CacheManager.getInstance().getMyProfile();
			new ReadyConfigXML().saveBoolean(ReadyConfigXML.KEY_ALREAD_SET + info.afId, true);
			if (null != mView_Parent) {
				TextView v = (TextView) mView_Parent.findViewById(R.id.setting_text);
				v.setTextColor(Color.BLACK);
				v.setText(R.string.setted);
			}
		}else if(requestCode == SET_SUCCESS_PHONE){
			update();
		}
	}
	
	/**
	 * 
	 * @param resId
	 */
	private void showCustomDialog(final int resId) {
		AppDialog appDialog = new AppDialog(this);
		appDialog.createOKDialog(this, getString(resId), new OnConfirmButtonDialogListener() {
			@Override
			public void onRightButtonClick() {
				//没有设置密保问题
				if (resId == R.string.you_havent_set_your_security_questions_yet) {
					String afid = CacheManager.getInstance().getMyProfile().afId;
					if (!StringUtil.isNullOrEmpty(afid)) {
						Intent intent = new Intent(MyAccountActivity.this, SetRequestActivity.class);
						intent.putExtra(KEY_AFID, afid);
						startActivityForResult(intent, SET_SUCCESS);
					}
				}
			}
			@Override
			public void onLeftButtonClick() {
				
			}
		});
		try {
			appDialog.show();
		} catch (Exception e) {
			// TODO: handle exception
			PalmchatLogUtils.println("showCustomDialog error.");
		}
		
	}
	
}
