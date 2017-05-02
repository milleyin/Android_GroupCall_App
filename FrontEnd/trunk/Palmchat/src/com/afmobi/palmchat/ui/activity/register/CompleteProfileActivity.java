package com.afmobi.palmchat.ui.activity.register;

import java.util.Calendar;
import java.util.Locale;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.customview.LimitTextWatcher;
import com.afmobi.palmchat.ui.customview.datepicker.STDatePicker;
import com.afmobi.palmchat.ui.customview.datepicker.STDatePickerDialog;
import com.afmobi.palmchat.ui.customview.datepicker.STDatePickerDialog.OnDateSetListener;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 该类只有当登陆的用户 name sex birthday regin有一个为空时才会进来  补全资料  不可跳过。 区别于ComfirmProfile（可跳过 只上传头像）
 */
public class CompleteProfileActivity extends BaseActivity implements OnClickListener ,
				AfHttpResultListener  {
	private TextView mBirthdayTxt,mCountryTxt;
	private EditText mNameEdit;
	private String mState;
	private String mCity;
	private String mCountry;
	private String mUsername;
	private byte mGender;
	private String mBirthday;
	private String mRegion;
	private AfPalmchat mAfCorePalmchat;
	private View rl_ragion, rl_dob;
	
	private RadioGroup mGenderGroup;
	private TextView mGenderTxt;
    private View mViewCompleteNote;
    private ImageView mRegionCompleteNote;
	private ImageView mBirthdayCompleteNote;
	private Button vButtonRegister;

	AfProfileInfo info;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_CPF);

	}

	@Override
	protected void onResume() {
		super.onResume();
	    mCountryTxt.setClickable(true);
	}

	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_complete_profile);
		mBirthdayTxt = (TextView) findViewById(R.id.edit_dob);
		mBirthdayTxt.setOnClickListener(this);
		mCountryTxt = (TextView) findViewById(R.id.edit_ragion);
		mCountryTxt.setOnClickListener(this);
		mNameEdit = (EditText) findViewById(R.id.edit_user);
		mNameEdit.addTextChangedListener(new LimitTextWatcher(mNameEdit, DefaultValueConstant.LENGTH_USERNAME) {
			  @Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
					checkInput();
			}
		});

		vButtonRegister = (Button) findViewById(R.id.register_button);
		vButtonRegister.setOnClickListener(this);
		ImageView imgBack = (ImageView) findViewById(R.id.img_left);
		imgBack.setVisibility(View.GONE);
		TextView mTitleTxt = (TextView) findViewById(R.id.title_text);
		mTitleTxt.setVisibility(View.VISIBLE);
		mTitleTxt.setText(R.string.complete_profile);
		
		mGenderGroup = (RadioGroup) findViewById(R.id.gender_radio_g);

		mGenderGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				if (checkedId == R.id.radio_gender_male) {
					mGender = Consts.AFMOBI_SEX_MALE;
				} else if (checkedId == R.id.radio_gender_female) {
					mGender = Consts.AFMOBI_SEX_FEMALE;
				}
	        
				checkInput();
			}
		});

		mAfCorePalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;

		rl_ragion = findViewById(R.id.rl_ragion);
		rl_dob = findViewById(R.id.rl_dob);
		rl_ragion.setOnClickListener(this);
		rl_dob.setOnClickListener(this);
		
		mGenderTxt = (TextView) findViewById(R.id.gender_text);
		mRegionCompleteNote = (ImageView) findViewById(R.id.note_region);
		mBirthdayCompleteNote = (ImageView) findViewById(R.id.note_birthday);
		mViewCompleteNote =  findViewById(R.id.view_complete_profile);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

		info = CacheManager.getInstance().getMyProfile().deepClone();
		if (info.afId == null) {
			info = (AfProfileInfo) getIntent().getSerializableExtra(JsonConstant.KEY_PROFILE);
			CacheManager.getInstance().setMyProfile(info);
		}
		vButtonRegister.setText(R.string.confirm);

		mState = PalmchatApp.getOsInfo().getState(context);
		mCity = PalmchatApp.getOsInfo().getCity(context);
		mCountry = PalmchatApp.getOsInfo().getCountry(context);
		if(CommonUtils.isEmpty(mState)){
			mState =  DefaultValueConstant.OTHERS;
		}
		if(CommonUtils.isEmpty(mCity)){
			mCity = DefaultValueConstant.OTHER;
		};
		if(CommonUtils.isEmpty(mCountry)){
			mCountry = DefaultValueConstant.OVERSEA;
		};

		checkInput();
	
	}


	private void onBack() {
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // //FLAG_ACTIVITY_NEW_TASK
			startActivity(startMain);
	}
	
	private void initTimePicker() {
		final AfProfileInfo afProfileInfo = CacheManager.getInstance().getMyProfile();
		
		final Calendar dateAndTime = Calendar.getInstance(Locale.getDefault());
		dateAndTime.set(afProfileInfo.getYear(), afProfileInfo.getMonth() - 1, afProfileInfo.getDay());
		STDatePickerDialog pickerDialog = new STDatePickerDialog(context, 
				new OnDateSetListener() {
					
					@Override
					public void onDateSet(STDatePicker view, int year, int monthOfYear,
							int dayOfMonth,String formatedDate) {
						//修改日历控件的年，月，日
						 
			            //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
						if(CommonUtils.compareDate(String.valueOf(year), String.valueOf( monthOfYear+1) , String.valueOf(dayOfMonth), view)){
							 
							dateAndTime.set(Calendar.YEAR, year);
				            dateAndTime.set(Calendar.MONTH, monthOfYear);
				            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);  
				            afProfileInfo.birth = formatedDate;
				            mBirthdayTxt.setText(formatedDate);
						}else{
							//
							ToastManager.getInstance().show(context, R.string.choose_right_birthday);
						}
						
						checkInput();
			            
					}
				}, 
				dateAndTime.get(Calendar.YEAR), 
				dateAndTime.get(Calendar.MONTH), 
				dateAndTime.get(Calendar.DAY_OF_MONTH));
		pickerDialog.show();
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.edit_dob:
			case R.id.rl_dob:
				initTimePicker();
				break;
			case R.id.edit_ragion:
			case R.id.rl_ragion:
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CHG_STATE);
				if (CacheManager.getInstance().getMyProfile().is_bind_phone && !DefaultValueConstant.OVERSEA.equals(CacheManager.getInstance().getMyProfile().country)) {
					Intent intent = new Intent(this, RegionTwoActivity.class);
					intent.putExtra("country", CacheManager.getInstance().getMyProfile().country);
					intent.putExtra(JsonConstant.KEY_FLAG, false);
					startActivityForResult(intent, DefaultValueConstant.RESULT_10);
				} else {
					mCountryTxt.setClickable(false);
					Intent intent = new Intent(this, CountryActivity.class);
					startActivityForResult(intent, DefaultValueConstant.RESULT_10);
				}
				break;

			case R.id.register_button:
				comfirm();
				break;
		}
	}

	private String getUserName(){
		return mNameEdit.getText().toString().trim();
	}
	
	private String getRegion(){
		return mCountryTxt.getText().toString().trim();
	}
	
	private void comfirm() {
		
		if (mViewCompleteNote.getVisibility() == View.VISIBLE) {
			ToastManager.getInstance().show(this, R.string.complete_note);
			return;
		}
		mUsername = getUserName();

		mBirthday =   mBirthdayTxt.getText().toString();
		mRegion = getRegion();
		if (CommonUtils.isEmpty(mCity)) {
			mCity = DefaultValueConstant.OTHER;
		} 
		if (CommonUtils.isEmpty(mState)) {
			mState = DefaultValueConstant.OTHERS;
		} 
		if (CommonUtils.isEmpty(mCountry)) {
			mCountry = DefaultValueConstant.OVERSEA;
		}
		if (mUsername.length() <= 0) {
			ToastManager.getInstance().show(context, R.string.prompt_input_username);
		}   else  if (mBirthday.length() <= DefaultValueConstant.LENGTH_0) {

			ToastManager.getInstance().show(context, R.string.prompt_input_dob);
		}
		  else if (mRegion.length() <= DefaultValueConstant.LENGTH_0) {
			//
			ToastManager.getInstance().show(context, R.string.prompt_input_region);
		} else {
					showProgressDialog(R.string.please_wait);
					updateProfile();//updateProfile(username, gender, dob, state,city,country, null);
		}
	}

//	private boolean mWaitUpdateShowProfile, mWaitUpdateShowStar;
	private void updateProfile() {
		// TODO Auto-generated method stub
		info.sex = mGender;
		info.name = getUserName();
		info.birth = CacheManager.getInstance().getMyProfile().birth;
		if(CommonUtils.isEmpty(info.country)){
			info.country =DefaultValueConstant.OVERSEA;
		}
		if(CommonUtils.isEmpty(info.region)){
			info.region = DefaultValueConstant.OTHERS;
		}
		if(CommonUtils.isEmpty(info.city)){
			info.city = DefaultValueConstant.OTHER;
		}
		info.birth = info.getUploadBirth(this);
		
		if (info != null) {
			String msg ="afid ="+info.afId+",name="+info.name+",birth="+info.birth+",country="+info.country+",gender="+info.sex;
			PalmchatLogUtils.e("---CompleteProfileActivity_updateProfile---", msg );
		}
		mAfCorePalmchat.AfHttpUpdateInfo(info, 0, this);
		
	}






	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result,Object user_data) {

//		mDialog.dismissCustomProgressDialog();
		
		if(code == Consts.REQ_CODE_SUCCESS){
			switch (flag) {
				case Consts.REQ_UPDATE_INFO:
				{
					AfProfileInfo profile = (AfProfileInfo)result;
					AfProfileInfo oldProfile = CacheManager.getInstance().getMyProfile();
					profile.head_img_path = oldProfile.head_img_path;
					oldProfile.sex = profile.sex;
					oldProfile.name = profile.name;
					oldProfile.birth = profile.birth;
					oldProfile.country = profile.country;
					oldProfile.region = profile.region;
					oldProfile.city = profile.city;
					oldProfile.age = profile.age;
					mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, oldProfile);
					toMainTab();
					break;
				}
			}
			
		} else {
			if(!isFinishing()) {
					Consts.getInstance().showToast(context, code, flag,http_code);
					dismissProgressDialog();
			}
		}
			
	}

	private void toMainTab() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,MainTab.class);
		intent.putExtra(JsonConstant.KEY_IS_LOGIN, true);
		intent.putExtra(JsonConstant.KEY_FLAG, true);
		startActivity(intent);
		finish();
	}
	
	public void displayRegion(TextView view,String region,String city,String country) {
		StringBuilder sb = new  StringBuilder();
		if (!TextUtils.isEmpty(city)) {
			if(!DefaultValueConstant.OTHER.equals(city)){
				sb.append(city);
			}
			info.city = city;
		}
		if (!TextUtils.isEmpty(region)) {
			if (sb.length() == 0 && !DefaultValueConstant.OTHERS.equals(region)) {
				sb.append(region);
			} else if(!DefaultValueConstant.OTHERS.equals(region)){
				sb.append(",").append(region);
			}
			info.region = region;
		}
		if (!TextUtils.isEmpty(country)) {
			if (sb.length() == 0 && !DefaultValueConstant.OVERSEA.equals(country)) {
				sb.append(country);
			} else if(!DefaultValueConstant.OVERSEA.equals(country)){
				sb.append(",").append(country);
			}
			info.country = country;
		}
		
		view.setText(sb.toString());
		
		if(DefaultValueConstant.OVERSEA.equals(country) || TextUtils.isEmpty(country)){
			view.setText("");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		PalmchatLogUtils.println("onActivityResult");
		switch (requestCode) {
		case DefaultValueConstant.RESULT_10:
			if(data == null)break;
			
			mState = data.getStringExtra(JsonConstant.KEY_STATE);
			mCity = data.getStringExtra(JsonConstant.KEY_CITY);
			mCountry = data.getStringExtra(JsonConstant.KEY_COUNTRY);
			if (TextUtils.isEmpty(mCity)) {
				mCity = DefaultValueConstant.OTHER;
			} 
			if (TextUtils.isEmpty(mState)) {
				mState = DefaultValueConstant.OTHERS;
			} 
			if ( TextUtils.isEmpty(mCountry)) {
				mCountry = DefaultValueConstant.OVERSEA;
			} 
			displayRegion(mCountryTxt, mState, mCity,mCountry);
			checkInput();
			break;
		}
	}


	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			onBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void checkInput() {

		String name = mNameEdit.getText().toString();
		String region = mCountryTxt.getText().toString().trim();
		String birthday = mBirthdayTxt.getText().toString();
		int checkId = mGenderGroup.getCheckedRadioButtonId();

		boolean notComplete = false;

		if (TextUtils.isEmpty(name)) {
			mNameEdit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_exclamationmark, 0, 0, 0);
			notComplete = true;
		} else {
			mNameEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		}

		if (TextUtils.isEmpty(region)) {
			mRegionCompleteNote.setVisibility(View.VISIBLE);
			notComplete = true;
		} else {
			mRegionCompleteNote.setVisibility(View.GONE);
		}

		if (TextUtils.isEmpty(birthday)) {
			mBirthdayCompleteNote.setVisibility(View.VISIBLE);
			notComplete = true;
		} else {
			mBirthdayCompleteNote.setVisibility(View.GONE);
		}

		if (checkId == -1) {
			mGenderTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_exclamationmark, 0, 0, 0);
			notComplete = true;
		} else {
			mGenderTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		}


		if (notComplete) {
			mViewCompleteNote.setVisibility(View.VISIBLE);
		} else {
			mViewCompleteNote.setVisibility(View.INVISIBLE);
		}

	}
}
