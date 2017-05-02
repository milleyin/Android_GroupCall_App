package com.afmobi.palmchat.ui.activity.login;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.db.DBState;
import com.afmobi.palmchat.eventbusmodel.UploadHeadEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.chats.PreviewImageActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.model.DialogItem;
import com.afmobi.palmchat.ui.activity.register.CountryActivity;
import com.afmobi.palmchat.ui.activity.register.RegionTwoActivity;
import com.afmobi.palmchat.ui.customview.ListDialog;
import com.afmobi.palmchat.ui.customview.ListDialog.OnItemClick;
import com.afmobi.palmchat.ui.customview.datepicker.STDatePicker;
import com.afmobi.palmchat.ui.customview.datepicker.STDatePickerDialog;
import com.afmobi.palmchat.ui.customview.datepicker.STDatePickerDialog.OnDateSetListener;
import com.afmobi.palmchat.util.BitmapUtils;
import com.afmobi.palmchat.util.ClippingPicture;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.UploadPictureUtils;
import com.afmobi.palmchat.util.UploadPictureUtils.IUploadHead;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfRespAvatarInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;
import com.core.param.AfRegInfoParam;
import com.facebook.Profile;
//import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * 
 * @author heguiming
 *	Facebook(also google+ twitter) Complete Profile
 */
public class FacebookCompleteProfileActivity extends BaseActivity implements
		OnClickListener, AfHttpResultListener, AfHttpProgressListener {
	
	private static final String TAG = FacebookCompleteProfileActivity.class.getCanonicalName();
	private TextView mCountryTxt;
	private EditText mNameEdit; 
    private View mViewCompleteNote;
	
	private String facebookId;
	private String password = Constants.FACEBOOK_PASSWORD;
	
	private byte mThirdLoginType = Consts.AF_LOGIN_FACEBOOK;
	private final SimpleDateFormat mSimpleFormat = new SimpleDateFormat("dd-MM-yyyy");
	private AfPalmchat mAfCorePalmchat;
	private String mState;
	private String mCity;
	private String mCountry;
	private TextView mBirthdayTxt;
	private ImageView mPhotoImageView;
	
	private TextView mGenderTxt;
	private RadioGroup mGenderGroup;
	
	private ImageView mRegionCompleteNote;
	private ImageView mBirthdayCompleteNote;
	
	private View mPhotoParent;
	
	private Profile mFbProfile;
	
	private String mName, mBirthday;
	private byte mGender;
	
	private AfProfileInfo mProfile  = new AfProfileInfo();
	private String mAfid;
	private boolean isUpdatePhoto;
	public AfProfileInfo afProfileInfo;
	private boolean isProfileUpdateSucessed;
	private static final int DownLoadAvatarCallback = 1;
	@Override
	public void findViews() {
		setContentView(R.layout.activity_third_part_complete_profile);

		ImageView imgBack = (ImageView) findViewById(R.id.img_left);
		imgBack.setVisibility(View.VISIBLE);
		imgBack.setOnClickListener(this);
		TextView mTitleTxt = (TextView) findViewById(R.id.title_text);
		mTitleTxt.setVisibility(View.VISIBLE);
		mTitleTxt.setText(R.string.complete_profile);
		
		mViewCompleteNote =  findViewById(R.id.view_complete_profile);
		
		mPhotoParent = findViewById(R.id.setphoto);
		
		mPhotoImageView = (ImageView) findViewById(R.id.profile_photo);
		mPhotoImageView.setOnClickListener(this);
		mNameEdit = (EditText) findViewById(R.id.edit_name);
		findViewById(R.id.select_country).setOnClickListener(this);
		mCountryTxt = (TextView) findViewById(R.id.text_region);
		findViewById(R.id.rl_birthday).setOnClickListener(this);
		mBirthdayTxt = (TextView) findViewById(R.id.text_birthday);
		
		mGenderTxt = (TextView) findViewById(R.id.gender_text);
		
		mGenderGroup = (RadioGroup) findViewById(R.id.gender_radio_g);
		
		findViewById(R.id.next_button).setOnClickListener(this);
		
		mRegionCompleteNote = (ImageView) findViewById(R.id.note_region);
		mBirthdayCompleteNote = (ImageView) findViewById(R.id.note_birthday);
		
		mNameEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				checkInput();
			}
		});
		
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
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
			sCameraFilename = savedInstanceState.getString("sCameraFilename");
			fCurrentFile = (File) savedInstanceState.getSerializable("fCurrentFile");
			PalmchatLogUtils.println("--cfd onCreate savedInstanceState sCameraFilename =" + sCameraFilename);
			if (fCurrentFile != null) {
				setShowHean(fCurrentFile.getAbsolutePath());
			}
		}
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		if (outState != null) {
			outState.putString("sCameraFilename", sCameraFilename);
			outState.putSerializable("fCurrentFile", fCurrentFile);
			PalmchatLogUtils.println("--cfd onSaveInstanceState sCameraFilename =" + sCameraFilename);
		}
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void init() {
		
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
		
		facebookId = getIntent().getExtras().getString("FacebookId");
		mAfid = getIntent().getExtras().getString("afid");
		if(TextUtils.isEmpty(mAfid)){
			mAfid=CacheManager.getInstance().getMyProfile().afId;
		}
		mThirdLoginType = getIntent().getExtras().getByte("ThirdLoginType");
		
		mFbProfile = getIntent().getParcelableExtra("SocialPerson");
		
		mAfCorePalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
		
		setThirdParyInfo();
		checkInput();
		displayRegion(mCountryTxt, mState, mCity, mCountry);
	}
	
	private void setThirdParyInfo() {
        
        String[] locarray = DBState.getInstance(this)
        .getCountryAndDefaultRegionFromMcc(PalmchatApp.getApplication().osInfo.getRealMcc());
          
          if (!TextUtils.isEmpty(locarray[0])) {
               mCountry = locarray[0];
          }
          
            if (!TextUtils.isEmpty(locarray[1])) {
               mState =  locarray[1];
          }
          
            if (!TextUtils.isEmpty(locarray[2])) {
               mCity =  locarray[2];
          }
          
	
        if (mFbProfile != null) {
        	
        	String gender = "";
        	String name = mFbProfile.getFirstName();
        	String imageUrl = mFbProfile.getProfilePictureUri(480, 480).toString();
        	String birthday = "";
        	
        	createLocalFile();
        	getThirdparyAvatar(imageUrl, fCurrentFile.getAbsolutePath());

	        if (!TextUtils.isEmpty(name)) {
	         	mName = name;
	         	mNameEdit.setText(name);
	        }
	        if (!TextUtils.isEmpty(gender)) {   
	        	if ("male".equals(gender)) {
	        		mGender = Consts.AFMOBI_SEX_MALE;
	        		mGenderGroup.check(R.id.radio_gender_male);
	        	} else if ("female".equals(gender)) {
	         		mGender = Consts.AFMOBI_SEX_FEMALE;
	         		mGenderGroup.check(R.id.radio_gender_female);
	        	}
	        }

	        if (!TextUtils.isEmpty(birthday)) {
	        	String[] array = birthday.split("/");
	        	if (array != null && array.length == 3) {
	         		mProfile.birth = array[1] + "-" + array[0] + "-" + array[2];
                	mBirthday = mProfile.getUploadBirth(this);
					Calendar dateAndTime = Calendar.getInstance(Locale.getDefault());
					dateAndTime.set(mProfile.getYear(), mProfile.getMonth() - 1, mProfile.getDay());
					String formatedDate = mSimpleFormat.format(dateAndTime.getTime());
					mBirthdayTxt.setText(formatedDate);
	        	}    
	        }
	    }         
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
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			onBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case DefaultValueConstant.RESULT_10:
				if(data == null) {
					break;
              	}
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
				displayRegion(mCountryTxt, mState, mCity, mCountry);
				checkInput();
				break;
			case  MessagesUtils.PICTURE://相册
				if (data != null ) {
					if (null != fCurrentFile) {
						Bitmap bm = null ;
						ArrayList<String> arrAddPiclist  = data.getStringArrayListExtra("photoLs");
						String cameraFilename = data.getStringExtra("cameraFilename");
						String path=null;
						if(arrAddPiclist!=null&&arrAddPiclist.size()>0){
							path=arrAddPiclist.get(0);
						}else if(cameraFilename!=null){
							path=RequestConstant.CAMERA_CACHE+cameraFilename;
						}
						if(path!=null ){
							String	largeFilename=Uri.decode( 	path);
							File f = new File(largeFilename);
							File f2 = FileUtils.copyToImg(largeFilename);//copy到palmchat/camera
							if (f2 != null) {
								f = f2;
							}
							  cameraFilename = f2.getAbsolutePath();// f.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
							cameraFilename = RequestConstant.IMAGE_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
							cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
							if (CommonUtils.isEmpty(cameraFilename)) { 
								return;
							}   
							setShowHean(cameraFilename);
						 	bm=ImageManager.getInstance().loadLocalImageSync(cameraFilename,false);//ImageUtil.getBitmapFromFile(cameraFilename,true);
							UploadPictureUtils.getInit().showClipPhoto(bm, mPhotoImageView, this, sCameraFilename, new IUploadHead() {

								@Override
								public void onUploadHead(String path, String filename) {
									isUpdatePhoto = true;
									setShowHean(path );
								}
	
								@Override
								public void onCancelUpload() {
	
								}
							});
						}
					}
				}
		 		break;
			case AF_INTENT_ACTION_CUT:
			{
				if (data != null) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						final Bitmap photo = bundle.getParcelable("data");
						if (null != photo && fCurrentFile != null) {
							byte [] bytes = BitmapUtils.Bitmap2Bytes(photo);
							String path = FileUtils.writeBytes(fCurrentFile.getAbsolutePath(), bytes);
							setShowHean(path);
						} else {
							ToastManager.getInstance().show(this, R.string.fail_to_update);
						}
					}
				}
				break;
			}

		default:
			break;
		}
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_country:
			new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.SELECT_COUNTR);
			if(CacheManager.getInstance().getMyProfile().is_bind_phone && !DefaultValueConstant.OVERSEA.equals(CacheManager.getInstance().getMyProfile().country)){
				Intent intent = new Intent(this,RegionTwoActivity.class);
				intent.putExtra(JsonConstant.KEY_COUNTRY, CacheManager.getInstance().getMyProfile().country);
				intent.putExtra(JsonConstant.KEY_FLAG, false);
				startActivityForResult(intent, DefaultValueConstant.RESULT_10);
			}else{
				Intent intent = new Intent(this, CountryActivity.class);
				startActivityForResult(intent, DefaultValueConstant.RESULT_10);
			}
			break;
		case R.id.next_button:
			
			new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.W_PW_N);
			register();
			
			break;
		case R.id.img_left:
			onBack();
			break;
			
			
			case R.id.rl_birthday:
			initTimePicker();
			break;
			
			case R.id.profile_photo:
			alertMenu();
			break;
			
		default:
			break;
		}
	}
	
	private void alertMenu() {
		createLocalFile();

		Intent mIntent = new Intent(this, PreviewImageActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putInt("size",1);
		mIntent.putExtras(mBundle);
		startActivityForResult(mIntent, MessagesUtils.PICTURE);
	
	}
	
	private void onBack() {
		// TODO Auto-generated method stub
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // //FLAG_ACTIVITY_NEW_TASK
		startActivity(startMain);
	}

	private void register() {
		
		if (mViewCompleteNote.getVisibility() == View.VISIBLE) {
			ToastManager.getInstance().show(this, R.string.complete_note);
			return;
		}
		
		showProgressDialog(R.string.loading);
		if(isProfileUpdateSucessed){//如果资料上传好了  那就是头像要上传了
			if (fCurrentFile != null && !TextUtils.isEmpty(sCameraFilename)) {
				uploadHead(fCurrentFile.getAbsolutePath(), sCameraFilename);
			}
			return ;
		}
	    mName = mNameEdit.getText().toString();
		mProfile.name = mName;
		mProfile.afId = mAfid;
		mProfile.birth = mBirthday;
		mProfile.login_type = mThirdLoginType;
		mProfile.third_account = facebookId;

		String countryCode = PalmchatApp.getApplication().osInfo.getCountryCode();
		AfRegInfoParam param = new AfRegInfoParam();
		param.cc = countryCode;
		param.imei = PalmchatApp.getOsInfo().getImei();
		param.imsi = PalmchatApp.getOsInfo().getImsi();
		param.sex = mGender;
		
		param.birth = mBirthday;
		param.name = mName;
		param.phone_or_email = facebookId;
		param.password = password;
		
		if (TextUtils.isEmpty(mState)) {
			param.region = PalmchatApp.getOsInfo().getState(context);
		} else {
			param.region = mState;
		}
		
		if (TextUtils.isEmpty(mCity)) {
			param.city = PalmchatApp.getOsInfo().getCity(context);
		} else {
			param.city = mCity;
		}
		
		if (TextUtils.isEmpty(mCountry)) {
			param.country = PalmchatApp.getOsInfo().getCountry(context);
		} else {
			param.country = mCountry;
		}
		mProfile.region = mState;
		mProfile.country = mCountry;
		mProfile.city = mCity;
		mProfile.sex = mGender;

		switch (mThirdLoginType) {
		case Consts.AF_LOGIN_FACEBOOK:
			CacheManager.getInstance().setMyProfile(mProfile);

			mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, mProfile);
			mProfile.birth = mProfile.getUploadBirth(this);
			mAfCorePalmchat.AfHttpUpdateInfo(mProfile, null, this);
			break;
		default:
			break;
		}
		

	}



	
	private void toMainTab() {
		dismissProgressDialog();
		Intent intent = new Intent(this, MainTab.class);
		intent.putExtra(JsonConstant.KEY_IS_LOGIN, true);
		intent.putExtra(JsonConstant.KeyPopMsg_NotAFriend,true);
		intent.putExtra(JsonConstant.KEY_LOGIN_TYPE, Consts.AF_LOGIN_FACEBOOK);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		PalmchatLogUtils.e("TAG", "facebook completeProfile----AfOnResult:Flag:" + flag + "-- Code---" + code);
		if (code == Consts.REQ_CODE_SUCCESS) {
			 if (flag == Consts.REQ_UPDATE_INFO) {
				//update profile

				if (result != null && result instanceof AfProfileInfo) {

					AfProfileInfo profileInfo = (AfProfileInfo)result;
					mProfile.age = profileInfo.age;
					mProfile.afId = mAfid;
					CacheManager.getInstance().setMyProfile(mProfile);
					mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, mProfile);
				}
				isProfileUpdateSucessed=true; 
				SharePreferenceUtils.getInstance(this).setIsFacebookLogin(true);
				//瞎上传头像
				if (fCurrentFile != null && !TextUtils.isEmpty(sCameraFilename)) {
					uploadHead(fCurrentFile.getAbsolutePath(), sCameraFilename);
				}else{
					  toMainTab();//没有头像的  说明用户不愿意上传头像  直接进进主页
				}
			}else if (flag == Consts.REQ_AVATAR_UPLOAD) {
					//upload head success 
					PalmchatLogUtils.println("-fcfdds avatar upload success!!!" + user_data + "->result = " + result );
					copy(AF_ACTION_HEAD ,result);
				    toMainTab();//头像上传成功  才进主页
						
			}
			
		} else {
			
			dismissProgressDialog();
			if (code == Consts.REQ_CODE_ILLEGAL_WORD) { // zhh相关语句中含有非法词汇
				Consts.getInstance().showToast(this, code, flag, http_code); 
			}else { 
				if (flag == Consts.REQ_UPDATE_INFO) { //更新资料失败
					ToastManager.getInstance().show(context, R.string.fail_to_update);
					 
				 }else if (flag == Consts.REQ_AVATAR_UPLOAD) { //头像上传失败
						ToastManager.getInstance().show(context, R.string.fail_to_update); 
				  }
			}
		}
	}

//	select country, state, city
	private void displayRegion(TextView view,String region,String city,String country) {
		StringBuilder sb = new  StringBuilder();
		if (!TextUtils.isEmpty(city)) {
			if(!DefaultValueConstant.OTHER.equals(city)){
				sb.append(city);
			}
		}
		if (!TextUtils.isEmpty(region)) {
			if (sb.length() == 0 && !DefaultValueConstant.OTHERS.equals(region)) {
				sb.append(region);
			} else if(!DefaultValueConstant.OTHERS.equals(region)){
				sb.append(",").append(region);
			}
		}
		if (!TextUtils.isEmpty(country)) {
			if (sb.length() == 0 && !DefaultValueConstant.OVERSEA.equals(country)) {
				sb.append(country);
			} else if(!DefaultValueConstant.OVERSEA.equals(country)){
				sb.append(",").append(country);
			}
		}
		
		view.setText(sb.toString());
		
		if(DefaultValueConstant.OVERSEA.equals(country) || TextUtils.isEmpty(country)){
			view.setText("");
		}
	}
	
	private void initTimePicker() {

		final AfProfileInfo afProfileInfo = mProfile;
				            
		final Calendar dateAndTime = Calendar.getInstance(Locale.getDefault());
		dateAndTime.set(afProfileInfo.getYear(), afProfileInfo.getMonth() - 1, afProfileInfo.getDay());
		
		String formatedDate = mSimpleFormat.format(dateAndTime.getTime());
		mBirthdayTxt.setText(formatedDate);
		
		STDatePickerDialog pickerDialog = new STDatePickerDialog(context, 
				new OnDateSetListener() {
					
					@Override
					public void onDateSet(STDatePicker view, int year, int monthOfYear,int dayOfMonth,String formatedDate) {
						Calendar c = Calendar.getInstance();
						if ((c.get(Calendar.YEAR) - year) < 6) { // 如果选择的日期与当前日期小于6年
							ToastManager.getInstance().show(context, R.string.choose_right_birthday);
							return;
						}
						//修改日历控件的年，月，日
					 
			            //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
						if(CommonUtils.compareDate(String.valueOf( year), (monthOfYear+1)+"", dayOfMonth+"", view)){
							 
							dateAndTime.set(Calendar.YEAR, year);
				            dateAndTime.set(Calendar.MONTH, monthOfYear);
				            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);  
				            afProfileInfo.birth = formatedDate;
				            mBirthday = afProfileInfo.getUploadBirth(FacebookCompleteProfileActivity.this);
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

	private static final int AF_INTENT_ACTION_CUT = 3;
	
	private static final int AF_ACTION_HEAD 	= 20;
	private File fCurrentFile;
	private String sCameraFilename;
	
	private void createLocalFile() {
		sCameraFilename = ClippingPicture.getCurrentFilename();
		PalmchatLogUtils.e("camera->", sCameraFilename);
		fCurrentFile = new File(RequestConstant.CAMERA_CACHE, sCameraFilename);
	}
	
	
	private void uploadHead(String path ,String filename) {
		mAfCorePalmchat.AfHttpHeadUpload(path, filename,Consts.AF_AVATAR_FORMAT, null, this, this);
	}
	
		private void setShowHean(String filepath){
		if (TextUtils.isEmpty(filepath)) {
			AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
			//调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
			ImageManager.getInstance().DisplayAvatarImage(mPhotoImageView, profileInfo.getServerUrl(),
					profileInfo .getAfidFromHead(),Consts.AF_HEAD_MIDDLE,profileInfo.sex,profileInfo.getSerialFromHead(),null);
		}else {
			/*Bitmap bitmap = ImageManager.getInstance().loadLocalImageSync(filepath,false);//ImageUtil.getBitmapFromFile(filepath);
			mPhotoImageView.setImageBitmap(ImageUtil.toRoundCorner(bitmap));*/
			ImageManager.getInstance().displayLocalImage_circle(mPhotoImageView,filepath ,0,0,null);
		}
	}
	private void copy(final int type ,final Object result) {
		afProfileInfo = CacheManager.getInstance().getMyProfile();
		if(fCurrentFile != null && afProfileInfo != null && afProfileInfo.afId != null) {
			
		String path = fCurrentFile.getAbsolutePath();
		AfRespAvatarInfo info = (AfRespAvatarInfo) (result);
		
		String pathDestination = RequestConstant.IMAGE_UIL_CACHE +  PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(
				CommonUtils.getAvatarUrlKey(afProfileInfo.getServerUrl(), mAfid, info.serial, Consts.AF_HEAD_MIDDLE ));
		File outFile = new File(pathDestination);
		FileUtils.copyToImg(path, outFile.getAbsolutePath());

		dismissProgressDialog();
		StringBuffer head_image_path = new StringBuffer();
		if (((AfRespAvatarInfo)(result)).afid != null) {
			head_image_path.append(((AfRespAvatarInfo)(result)).afid);
		}
		if (((AfRespAvatarInfo)(result)).serial != null) {
			head_image_path.append(",");
			head_image_path.append(((AfRespAvatarInfo)(result)).serial);
		}
		if (((AfRespAvatarInfo)(result)).host != null) {
			head_image_path.append(",");
			head_image_path.append(((AfRespAvatarInfo)(result)).host);
		}
		AfProfileInfo profile = CacheManager.getInstance().getMyProfile();
		if (profile != null) {
			profile.head_img_path = head_image_path.toString();
			
			byte sex = 0;
			//调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
			ImageManager.getInstance().DisplayAvatarImage(mPhotoImageView, profile.getServerUrl(),
					profile.afId,Consts.AF_HEAD_MIDDLE, sex,profile.getSerialFromHead(),null); 
			 
		}
		
		PalmchatLogUtils.i(TAG, "head_image_path = " + head_image_path.toString());
		mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, profile);
		EventBus.getDefault().post(new UploadHeadEvent());//因为 上传头像是异步的过程，可能用户进了MainTab页以后 这个头像才传完毕，那么要通知maintab更新头像，同时通知homefragemnt隐藏上传头像框
		
		
		
		if (fCurrentFile != null) {
			fCurrentFile.delete();
		}
		} else {
//			ToastManager.getInstance().show(this, R.string.fail_to_update); 
		}
	}

	@Override
	public void AfOnProgress(int httpHandle, int flag, int progress,
			Object user_data) {
		// TODO Auto-generated method stub
		
	}
		
	Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == DownLoadAvatarCallback) {
				String filepath = (String) msg.obj;
				setShowHean(filepath);
			}

		};
	};
		
	private void getThirdparyAvatar(final String avatarUrl, final String savePath) {
		new Thread() {
			public void run() {
				   FileOutputStream outStream = null;
				   ByteArrayOutputStream out = null;
				   BufferedInputStream inputStreamReader = null;
				   try {
					   DefaultHttpClient httpClient = new DefaultHttpClient();
					   HttpGet httpget = new HttpGet(avatarUrl);
					   HttpResponse response = httpClient.execute(httpget);
					   out = new ByteArrayOutputStream();
					   inputStreamReader = new BufferedInputStream(response.getEntity().getContent());
					   byte[] temp = new byte[1024];
					   int size = 0;
					   while ((size = inputStreamReader.read(temp)) != -1) {
							   out.write(temp, 0, size);
						   }

					File saveFile = new File(savePath);
					if (saveFile.exists()) {
						saveFile.delete();
					}

					saveFile.createNewFile();
					outStream = new FileOutputStream(saveFile);
					outStream.write(out.toByteArray());

					mHandler.obtainMessage(DownLoadAvatarCallback, savePath).sendToTarget();


				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					if (inputStreamReader != null) {
						try {
							inputStreamReader.close();
						} catch (Exception e2) {
							// TODO: handle exception
						}
					}

					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (outStream != null) {
						try {
							outStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}
}
