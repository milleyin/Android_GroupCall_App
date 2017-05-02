package com.afmobi.palmchat.ui.activity.profile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.db.SharePreferenceService;
import com.afmobi.palmchat.eventbusmodel.ChangeCountryEvent;
import com.afmobi.palmchat.eventbusmodel.ChangeRegionEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.MsgAlarmManager;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.ActivityBackgroundChange;
import com.afmobi.palmchat.ui.activity.chats.PreviewImageActivity;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;
import com.afmobi.palmchat.ui.activity.main.UpdateStateActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.register.CountryActivity;
import com.afmobi.palmchat.ui.activity.register.RegionTwoActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.AppDialog.OnInputDialogListener;
import com.afmobi.palmchat.ui.customview.AppDialog.OnRadioButtonDialogListener;
import com.afmobi.palmchat.ui.customview.datepicker.STDatePicker;
import com.afmobi.palmchat.ui.customview.datepicker.STDatePickerDialog;
import com.afmobi.palmchat.ui.customview.datepicker.STDatePickerDialog.OnDateSetListener;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.BitmapUtils;
import com.afmobi.palmchat.util.ClippingPicture;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.UploadPictureUtils;
import com.afmobi.palmchat.util.UploadPictureUtils.IUploadHead;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfDatingInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfRespAvatarInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;
import com.core.listener.AfHttpSysListener;
import com.facebook.login.LoginManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

/**
 *
 */
public class MyProfileDetailActivity extends BaseActivity implements OnClickListener, AfHttpResultListener, AfHttpProgressListener  {

	public AfProfileInfo afProfileInfo;
	private AfPalmchat mAfPalmchat;

	private final SimpleDateFormat mSimpleFormat = new SimpleDateFormat("dd-MM-yyyy");
	private static String TAG = MyProfileDetailActivity.class.getSimpleName();
	public static final String STRANGER = "stranger";
	public static final String FRIENDS = "friends";
	public static final String OWNER = "owner";
	public static final String BLOCK = "block";
	public static final int FROM_CITY_BC = -100;
	// private static final int AF_INTENT_ACTION_CAMERA = 1;
	// private static final int AF_INTENT_ACTION_PICTURE = 2;
	private static final int AF_INTENT_ACTION_CUT = 3;
	private static final int AF_INTENT_ALBUM = 30;
	private static final int AF_INTENT_DETAIL = 6;
	private File fCurrentFile;
	private String sCameraFilename;

	/**
	 * 城市是否改变
	 */
	private boolean isChangeOfCity;
	/**
	 * 地区是否改变
	 */
	private boolean isChangeOfState;
	/**
	 * 国家是否改变
	 */
	private boolean isChangeOfCountry;

	private ImageView imgProfile;
	private View viewStatus;
	private View viewGender;
	private View viewRegion;
	private View viewName;
	private View viewPhoto;
	private View viewWhatsUp;
	private ImageView profile_photo_value;
	private String oldSign;
	private TextView textNameValue;
	private TextView textWhatsupValue;
	private TextView textGenderValue;
  	private TextView textRegionValue;
	private TextView textBrithValue;
	private View viewBirth;
  	private TextView textStatusValue;
  	private Button mSaveButton;
	/* add by zhh 魅族状态栏适配 */
	View title_layout, r_paofile;
	private String new_head_image_path;
	private static final int AF_ACTION_HEAD = 20;
	/** 本地接口 */
	private AfPalmchat mAfCorePalmchat;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Object result = msg.obj;
			switch (msg.what) {
			case AF_ACTION_HEAD:
				StringBuffer head_image_path = new StringBuffer();
				if (((AfRespAvatarInfo) (result)).afid != null) {
					head_image_path.append(((AfRespAvatarInfo) (result)).afid);
				}
				if (((AfRespAvatarInfo) (result)).serial != null) {
					head_image_path.append(",");
					head_image_path.append(((AfRespAvatarInfo) (result)).serial);
				}
				if (((AfRespAvatarInfo) (result)).host != null) {
					head_image_path.append(",");
					head_image_path.append(((AfRespAvatarInfo) (result)).host);
				}
				AfProfileInfo profile = CacheManager.getInstance().getMyProfile();
				if (profile != null) {
					profile.head_img_path = head_image_path.toString();
				}
				new_head_image_path = head_image_path.toString();

				byte sex = 0;
				// WXL 20151013 调UIL的显示头像方法,统一图片管理
				ImageManager.getInstance().DisplayAvatarImage(profile_photo_value, profile.getServerUrl(), profile.afId, Consts.AF_HEAD_MIDDLE, sex, profile.getSerialFromHead(), null);
				dismissProgressDialog();
				CacheManager.getInstance().setMyProfile(profile);
				PalmchatLogUtils.i(TAG, "head_image_path = " + head_image_path.toString());
				mAfPalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, profile);
				break;
			default:
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAfPalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		afProfileInfo = AppUtils.createProfileInfoFromCacheValue();
		fCurrentFile = CacheManager.getInstance().getCurFile();
		setContent(afProfileInfo);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (FROM_CITY_BC == bundle.getInt(JsonConstant.KEY_FORM_CITY_BC, 0)) {
				viewRegion.performClick();
			}
		}
	}

	/**
	 * 所在的城市拼接起来
	 * 
	 * @param view
	 *            控件
	 * @param country
	 *            国家
	 * @param state
	 *            省市
	 * @param city
	 *            地级市
	 */
	public void displayRegion(TextView view, String country, String state, String city) {
		StringBuilder sb = new StringBuilder();
		if (!TextUtils.isEmpty(city)) {
			if (!DefaultValueConstant.OTHER.equals(city)) {
				sb.append(city);
			}
		}
		if (!TextUtils.isEmpty(state)) {
			if (sb.length() == 0 && !DefaultValueConstant.OTHERS.equals(state)) {
				sb.append(state);
			} else if (!DefaultValueConstant.OTHERS.equals(state)) {
				sb.append(",").append(state);
			}
		}
		if (!TextUtils.isEmpty(country)) {
			if (sb.length() == 0 && !DefaultValueConstant.OVERSEA.equals(country)) {
				sb.append(country);
			} else if (!DefaultValueConstant.OVERSEA.equals(country)) {
				sb.append(",").append(country);
			}
		}
		view.setText(sb.toString());
	}

	/**
	 * 给自己的信息赋值
	 * 
	 * @param profileInfo
	 */
	private void setContent(AfProfileInfo profileInfo) {
		if (profileInfo != null) {
			this.afProfileInfo = profileInfo;
			displayRegion(textRegionValue, profileInfo.country, profileInfo.region, profileInfo.city);
		  	textNameValue.setText(profileInfo.name);
			textStatusValue.setText(CommonUtils.isEmpty(profileInfo.signature) ? getString(R.string.default_status) : profileInfo.signature);
		   	String birth = profileInfo.getBirthNew(this);
			textBrithValue.setText(birth);
			textGenderValue.setText((profileInfo.sex != Consts.AFMOBI_SEX_MALE) ? R.string.female : R.string.male);

			int marital_status = profileInfo.dating.marital_status;
		  	// WXL 20151013 调UIL的显示头像方法,统一图片管理
			ImageManager.getInstance().DisplayAvatarImage(profile_photo_value, profileInfo.getServerUrl(), profileInfo.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, profileInfo.sex, profileInfo.getSerialFromHead(), null);
			String sign = CommonUtils.isEmpty(profileInfo.signature) ? getString(R.string.default_status) : profileInfo.signature;
			CharSequence text = EmojiParser.getInstance(context).parse(sign, ImageUtil.dip2px(context, 18));
			textWhatsupValue.setText(text);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// MobclickAgent.onPageStart("MyProfileDetailActivity");
		// MobclickAgent.onResume(this);
		viewRegion.setClickable(true);
		/* add by zhh 魅族、HTC等手机状态栏适配 */
		CommonUtils.hideStatus(this, r_paofile);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// MobclickAgent.onPageEnd("MyProfileDetailActivity"); // 保证 onPageEnd
		// 在onPause 之前调用,因为 onPause 中会保存信息
		// MobclickAgent.onPause(this);

	}

	@Override
	public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// isCanBack 当国家改变时，正在注销时，按back键不做处理
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (null != afProfileInfo && (afProfileInfo.isChange || afProfileInfo.isPhoneChange)) {
				showConfirmDialog();
			} else {
				setResult(Constants.COMPLET_PROFILE);
				finish();
			}
		}
		return false;
	}

	private boolean isDialogUpdate;

	/**
	 * 显示确认对话框
	 */
	private void showConfirmDialog() {
		AppDialog confirmDialog = new AppDialog(this);
		confirmDialog.createConfirmDialog(this, getString(R.string.information_changes_changes), new OnConfirmButtonDialogListener() {
			@Override
			public void onRightButtonClick() {
				showProgressDialog(R.string.please_wait);
				isDialogUpdate = true;
				updateProfile();
			}

			@Override
			public void onLeftButtonClick() {
				setResult(Constants.COMPLET_PROFILE);
				MyProfileDetailActivity.this.finish();
			}
		});
		confirmDialog.show();
	}

	@Override
	public void findViews() {
		setContentView(R.layout.activity_profile_detail);

		// mhttpHandle = new SparseArray<Integer>();
		((TextView) findViewById(R.id.title_text)).setText(R.string.edit_profile);
		mSaveButton = (Button) findViewById(R.id.btn_ok);
		mSaveButton.setText(R.string.save);
		mSaveButton.setVisibility(View.VISIBLE);
		mSaveButton.setOnClickListener(this);
		viewBirth = findViewById(R.id.profile_brith_layout);
		imgProfile = (ImageView) findViewById(R.id.profile_head_img);
		profile_photo_value = (ImageView) findViewById(R.id.profile_photo_value);
		viewStatus = findViewById(R.id.profile_status_sign_layout);
		viewStatus.setOnClickListener(this);
		viewName = findViewById(R.id.profile_name_layout);
		viewGender = findViewById(R.id.profile_sex_layout);
		viewRegion = findViewById(R.id.profile_region_layout);
		viewPhoto = findViewById(R.id.profile_photo_layout);
		viewWhatsUp = findViewById(R.id.profile_whatsup_layout);
	  	r_paofile = findViewById(R.id.r_paofile);
		viewWhatsUp.setOnClickListener(this);
		viewPhoto.setOnClickListener(this);
		viewName.setOnClickListener(this);
		viewGender.setOnClickListener(this);
		viewRegion.setOnClickListener(this);
		viewBirth.setOnClickListener(this);
		textNameValue = (TextView) findViewById(R.id.profile_name_value);
  		textRegionValue = (TextView) findViewById(R.id.profile_region_value);
		textBrithValue = (TextView) findViewById(R.id.profile_brith_value);
		textWhatsupValue = (TextView) findViewById(R.id.profile_whatsup_value);
		textGenderValue = (TextView) findViewById(R.id.profile_sex_value);
	  	textStatusValue = (TextView) findViewById(R.id.profile_status_value);
	    title_layout = findViewById(R.id.title_layout);
		textWhatsupValue.setOnClickListener(this);
		findViewById(R.id.select_button_img).setVisibility(View.GONE);
		// findViewById(R.id.lin_tab_save).setOnClickListener(this);
		View backView = findViewById(R.id.back_button);
		if (backView != null) {
			backView.setOnClickListener(this);
		}
	}

	@Override
	public void init() {
		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.profile_relationship_layout:
//		case R.id.profile_relationship_value:
//			createDialog(textRelationship, R.array.relations, R.array.relations, R.string.relationship, getRelations(this, afProfileInfo.dating.marital_status));
//			break;
		case R.id.profile_whatsup_layout:
		case R.id.profile_whatsup_value:
			if (null != afProfileInfo) {
				AppDialog dialog = new AppDialog(this);
				String inputMsg = afProfileInfo.signature == null ? "" : afProfileInfo.signature;
				dialog.createEditpEmotionDialog(this, getString(R.string.whatsup), inputMsg, new OnInputDialogListener() {
					@Override
					public void onRightButtonClick(String inputStr) {
						if (mAfPalmchat != null && afProfileInfo != null) {
							// GTF 2014-11-16
							new ReadyConfigXML().saveReadyInt(ReadyConfigXML.MPF_SET_WUP);
							// MobclickAgent.onEvent(context,
							// ReadyConfigXML.MPF_SET_WUP);

							oldSign = afProfileInfo.signature;
							afProfileInfo.signature = inputStr;
							afProfileInfo.isChange = true;
							String sign = CommonUtils.isEmpty(afProfileInfo.signature) ? getString(R.string.default_status) : afProfileInfo.signature;
							CharSequence text = EmojiParser.getInstance(context).parse(sign, ImageUtil.dip2px(context, 18));
							textWhatsupValue.setText(text);
						}
					}

					@Override
					public void onLeftButtonClick() {

					}

				});
				dialog.show();
			}
			break;

		case R.id.btn_ok:// yes comfirm ok
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SET_MPF);
			// MobclickAgent.onEvent(context, ReadyConfigXML.SET_MPF);
			if (null != afProfileInfo && (afProfileInfo.isChange || afProfileInfo.isPhoneChange)) {
				showProgressDialog(R.string.please_wait);
				isDialogUpdate = true;
				updateProfile();
			} else {
				setResult(Constants.COMPLET_PROFILE);
				finish();
			}
			break;
		case R.id.back_button:
			if (null != afProfileInfo && afProfileInfo.isChange) {
				showConfirmDialog();
			} else {
				setResult(Constants.COMPLET_PROFILE);
				// onBack();
				finish();
			}
			break;
		case R.id.profile_name_layout: {
			final AppDialog appDialog = new AppDialog(this);
			String title = getString(R.string.set_name);
			String inputMsg = afProfileInfo == null ? "" : afProfileInfo.name;
			appDialog.createEditDialog(context, title, inputMsg, DefaultValueConstant.LENGTH_USERNAME, new OnInputDialogListener() {
				@Override
				public void onRightButtonClick(String inputStr) {
					if (TextUtils.isEmpty(inputStr)) {
						ToastManager.getInstance().show(context, R.string.prompt_input_username);
					} else if (afProfileInfo != null) {
						String temp_inputStr = inputStr.trim();
						if (TextUtils.isEmpty(temp_inputStr)) {
							ToastManager.getInstance().show(context, R.string.prompt_input_username);
						} else {
							String name = afProfileInfo.name;
							if (notSame(name, inputStr)) {
								afProfileInfo.isChange = true;
							}
							afProfileInfo.name = inputStr;
							textNameValue.setText(inputStr);
						}
					}
				}

				@Override
				public void onLeftButtonClick() {

				}
			});
			appDialog.show();
			break;
		}

		case R.id.profile_photo_layout: {
			alertMenu();
			break;
		}
		case R.id.profile_region_layout: {
			viewRegion.setClickable(false);
			if (CacheManager.getInstance().getMyProfile().is_bind_phone) {
				Intent intent = new Intent(MyProfileDetailActivity.this, RegionTwoActivity.class);
				intent.putExtra(JsonConstant.KEY_COUNTRY, afProfileInfo.country);
				intent.putExtra(JsonConstant.KEY_FLAG, false);
				startActivityForResult(intent, DefaultValueConstant.RESULT_10);
			} else {
				Intent mIntent = new Intent(this, CountryActivity.class);
				mIntent.putExtra(JsonConstant.KEY_FROM,TAG);
				startActivityForResult(mIntent, DefaultValueConstant.RESULT_10);
			}
			break;
		}

		case R.id.profile_sex_layout:
			createDialog(textGenderValue, R.array.gender, R.array.gender, R.string.gender, getLongSex(this, afProfileInfo.sex));
			break;
		case R.id.profile_brith_layout:
			if (afProfileInfo != null) {
				final Calendar dateAndTime = Calendar.getInstance(Locale.getDefault());
				dateAndTime.set(afProfileInfo.getYear(), afProfileInfo.getMonth() - 1, afProfileInfo.getDay());
				STDatePickerDialog pickerDialog = new STDatePickerDialog(context, new OnDateSetListener() {

					@Override
					public void onDateSet(STDatePicker view, int year, int monthOfYear, int dayOfMonth,String formatedDate) {
						Calendar c = Calendar.getInstance();
						if ((c.get(Calendar.YEAR) - year) < 6) { // 如果选择的日期与当前日期小于6年
							ToastManager.getInstance().show(context, R.string.choose_right_birthday);
							return;
						}

						// 修改日历控件的年，月，日
						 
						// 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
						if (CommonUtils.compareDate(String.valueOf( year), String.valueOf(monthOfYear+1)  , String.valueOf(dayOfMonth), view)) {
							 
							dateAndTime.set(Calendar.YEAR, year);
							dateAndTime.set(Calendar.MONTH, monthOfYear);
							dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
							// 将页面TextView的显示更新为最新时间
						 	if (notSame(afProfileInfo.birth, formatedDate)) {
								afProfileInfo.isChange = true;
							}
							afProfileInfo.birth = formatedDate;
							textBrithValue.setText(formatedDate);
						} else {
							//
							ToastManager.getInstance().show(context, R.string.choose_right_birthday);
						}
					}
				}, dateAndTime.get(Calendar.YEAR), dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
				pickerDialog.show();
			}

			break;
//		case R.id.profile_hobby_layout:
//			createDialog(textHobbyValue, R.array.hobby, R.array.hobby_en, R.string.hobbys, afProfileInfo.hobby);
//			break;
//		case R.id.profile_profession_layout:
//			createDialog(textProfessionValue, R.array.profession, R.array.profession_en, R.string.profession, afProfileInfo.profession);
//			break;
//		case R.id.profile_religion_layout:
//			createDialog(textReligionValue, R.array.religion, R.array.religion_en, R.string.religion, afProfileInfo.religion);
//			break;
//		case R.id.profile_school_layout:
//			final AppDialog appDialog = new AppDialog(this);
//			String title = getString(R.string.set_education);
//			String inputMsg = afProfileInfo == null ? "" : afProfileInfo.school;
//			appDialog.createEditDialog(this, title, inputMsg, new OnInputDialogListener() {
//				@Override
//				public void onRightButtonClick(String inputStr) {
//					if (afProfileInfo != null) {
//						if (notSame(afProfileInfo.school, inputStr)) {
//							afProfileInfo.isChange = true;
//						}
//						afProfileInfo.school = inputStr;
//						textEducationValue.setText(inputStr);
//					}
//				}
//
//				@Override
//				public void onLeftButtonClick() {
//
//				}
//			});
//			appDialog.show();
//			break;
		case R.id.profile_status_sign_layout:
			Intent intent = new Intent(this, UpdateStatusActivity.class);
			intent.putExtra(JsonConstant.KEY_PROFILE, afProfileInfo);
			startActivityForResult(intent, 20);

			break;
		}
	}

	private boolean notSame(String t, String s) {
		if (null == t) {
			return true;
		}
		return t.equals(s) ? false : true;
	}

	static byte getShortSex(String sex) {
		return PalmchatApp.getApplication().getString(R.string.female).equals(sex) ? Consts.AFMOBI_SEX_FEMALE : Consts.AFMOBI_SEX_MALE;
	}
 
	private String getLongSex(Context context, int sex) {
		return (Consts.AFMOBI_SEX_MALE != sex) ? context.getString(R.string.female) : context.getString(R.string.male);
	}

	private String getRelations(Context context, int marital_status) {
		return context.getString(CommonUtils.getMaritalStart(marital_status));
	}

	private int indexOf(final String[] ch, String target) {
		if (target != null) {
			for (int i = 0; i < ch.length; i++) {
				if (target.equals(ch[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 创建Dialog
	 * 
	 * @param view
	 * @param arrayId
	 * @param arrayId_en
	 * @param titleId
	 * @param defaultText
	 */
	private void createDialog(final TextView view, final int arrayId, final int arrayId_en, final int titleId, String defaultText) {
		final String[] ch = getResources().getStringArray(arrayId);
		final String[] ch_en = getResources().getStringArray(arrayId_en);
		int index = indexOf(ch, defaultText);
		if (index == -1) {
			index = indexOf(ch_en, defaultText);
		}
		index = index == -1 ? 0 : index;
		AppDialog appDialog = new AppDialog(this);
		appDialog.createRadioButtonDialog(this, getString(titleId), index, ch, new OnRadioButtonDialogListener() {
			@Override
			public void onRightButtonClick(int selectIndex) {
				String text = ch_en[selectIndex];
				String text_show = ch[selectIndex];
				view.setText(text_show);
				switch (arrayId) {
				case R.array.hobby:
					if (notSame(afProfileInfo.hobby, text)) {
						afProfileInfo.isChange = true;
					}
					afProfileInfo.hobby = text;
					break;
				case R.array.gender:
					// flag = !same(getShortSex(text), afProfileInfo.sex);
					if (afProfileInfo.sex != getShortSex(text)) {
						afProfileInfo.isChange = true;
					}
					afProfileInfo.sex = getShortSex(text);
					break;
				case R.array.profession:
					if (notSame(afProfileInfo.profession, text)) {
						afProfileInfo.isChange = true;
					}
					afProfileInfo.profession = text;
					break;
				case R.array.religion:
					if (notSame(afProfileInfo.religion, text)) {
						afProfileInfo.isChange = true;
					}
					afProfileInfo.religion = text;
					break;
				case R.array.school:
					if (notSame(afProfileInfo.school, text)) {
						afProfileInfo.isChange = true;
					}
					afProfileInfo.school = text;
					break;
				case R.array.relations:
					// flag = !same(getShortSex(text), afProfileInfo.sex);
					if (afProfileInfo.dating.marital_status != CommonUtils.getShortRelations(text)) {
						afProfileInfo.isChange = true;
					}
					afProfileInfo.dating.marital_status = CommonUtils.getShortRelations(text);
					update_MaritalStatus(afProfileInfo.dating.marital_status);
					break;
				}
			}

			@Override
			public void onLeftButtonClick() {

			}
		});
		appDialog.show();
	}

	/**
	 * 更新修改的数据
	 */
	private void updateProfile() {
		if (afProfileInfo != null) {
			// heguiming 2013-12-04
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SET_MPF);
			// MobclickAgent.onEvent(context, ReadyConfigXML.SET_MPF);

			if (afProfileInfo.isChange) {
				if (TextUtils.isEmpty(afProfileInfo.city)) {
					afProfileInfo.city = DefaultValueConstant.OTHER;
				}
				if (TextUtils.isEmpty(afProfileInfo.region)) {
					afProfileInfo.region = DefaultValueConstant.OTHERS;
				}
				afProfileInfo.birth = afProfileInfo.getUploadBirth(this);
				if (!TextUtils.isEmpty(new_head_image_path)) {
					afProfileInfo.head_img_path = new_head_image_path;
				}
				int handler = mAfPalmchat.AfHttpUpdateInfo(afProfileInfo, null, this);
				// mhttpHandle.put(handler,handler);
			} else if (afProfileInfo.isPhoneChange) {
				int handler = mAfPalmchat.AfHttpUpdateDatingInfo(Consts.REQ_UPDATE_SHOWMOBILE, getUploadDatingInfo(), null, this);
				// mhttpHandle.put(handler,handler);
			}
		}
	}

	private AfDatingInfo getUploadDatingInfo() {
		AfDatingInfo datingInfo = new AfDatingInfo();
		datingInfo.dating_phone = "";
		datingInfo.is_show_dating_phone = CacheManager.getInstance().getMyProfile().dating.is_show_dating_phone;
		datingInfo.dating_phone_flower = CacheManager.getInstance().getMyProfile().dating.dating_phone_flower;
		return datingInfo;

	}

	/**
	 * 更新婚姻状态
	 * 
	 * @param marital_status
	 */
	private void update_MaritalStatus(byte marital_status) {
		showProgressDialog(R.string.please_wait);
		mAfPalmchat.AfHttpUpdateDatingInfo(Consts.REQ_UPDATE_MARRIAGE, getUploadDatingInfo_MaritalStatus(marital_status), null, this);
	}

	private AfDatingInfo getUploadDatingInfo_MaritalStatus(byte marital_status) {
		AfDatingInfo datingInfo = new AfDatingInfo();
		datingInfo.marital_status = marital_status;
		return datingInfo;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (null != data) {
			if (requestCode == DefaultValueConstant.RESULT_10) { // state选择后返回
					AfProfileInfo cacheProfile = CacheManager.getInstance().getMyProfile();
					String country = data.getStringExtra(JsonConstant.KEY_COUNTRY);
					String region = data.getStringExtra(JsonConstant.KEY_STATE);
					String city = data.getStringExtra(JsonConstant.KEY_CITY);
					if (notSame(cacheProfile.city, city)) {
						afProfileInfo.isChange = true;
						isChangeOfCity = true;
					} else {
						isChangeOfCity = false;
					}

					if (notSame(cacheProfile.region, region)) {
						afProfileInfo.isChange = true;
						isChangeOfState = true;
					} else {
						isChangeOfState = false;
					}

					if (notSame(cacheProfile.country, country)) {
						afProfileInfo.isChange = true;
						isChangeOfCountry = true;
					} else {
						isChangeOfCountry = false;
					}

					if (TextUtils.isEmpty(city)) {
						city = DefaultValueConstant.OTHER;
					}
					if (TextUtils.isEmpty(region)) {
						region = DefaultValueConstant.OTHERS;
					}
					if (TextUtils.isEmpty(country)) {
						country = DefaultValueConstant.OVERSEA;
					}
					afProfileInfo.country = country;
					afProfileInfo.city = city;
					afProfileInfo.region = region;
					displayRegion(textRegionValue, afProfileInfo.country, afProfileInfo.region, afProfileInfo.city);

				} else if (requestCode == 20) {
				String sign = data.getStringExtra(JsonConstant.KEY_STATUS);
				if (sign != null) {
					afProfileInfo.signature = sign;
					textStatusValue.setText(sign);
					CacheManager.getInstance().setMyProfile(afProfileInfo);
					mAfPalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, afProfileInfo);
				}
			}
			/*else if (requestCode == DefaultValueConstant._86) {
				Intent intent = new Intent(this, RegionTwoActivity.class);
				intent.putExtra("country", data.getStringExtra(JsonConstant.KEY_COUNTRY_TEXT));
				intent.putExtra(JsonConstant.KEY_FLAG, false);
				startActivityForResult(intent, DefaultValueConstant.RESULT_10);
			}*/

		}
		String name = null;
		switch (requestCode) {
		case AF_INTENT_DETAIL:
			break;

		/*
		 * case MessagesUtils.CAMERA: // 相机 if ( null!=data && resultCode ==
		 * Activity.RESULT_OK) { String cameraFilename =
		 * data.getStringExtra("cameraFilename"); File f = new
		 * File(cameraFilename); Bitmap bm =
		 * UploadPictureUtils.getInit().smallImage(f);
		 * UploadPictureUtils.getInit().showClipPhoto(bm, viewPhoto,
		 * MyProfileDetailActivity.this, sCameraFilename, new IUploadHead() {
		 * 
		 * @Override public void onUploadHead(String path, String filename) {
		 * uploadHead(path, filename); }
		 * 
		 * @Override public void onCancelUpload() {
		 * 
		 * } }); }
		 * 
		 * break;
		 */
		case MessagesUtils.PICTURE:// 相册
			PalmchatLogUtils.e("WXL", "Profile Detail RESULT_OK fCurrentFile="+fCurrentFile);
			
			if (data != null) {
				if (null != fCurrentFile) {
					Bitmap bm = null;

					ArrayList<String> arrAddPiclist = data.getStringArrayListExtra("photoLs");
					String cameraFilename = data.getStringExtra("cameraFilename");
					PalmchatLogUtils.e("WXL", "Profile Detail RESULT_OK cameraFilename="+cameraFilename);
					String path = null;
					if (arrAddPiclist != null && arrAddPiclist.size() > 0) {
						path = arrAddPiclist.get(0);
					} else if (cameraFilename != null) {
						path = RequestConstant.CAMERA_CACHE + cameraFilename;
					}
					if (path != null) {

						PalmchatLogUtils.e("WXL", "Profile Detail path "+path);
						String largeFilename = Uri.decode(path);
						File f = new File(largeFilename);
						File f2 = FileUtils.copyToImg(largeFilename);// copy到palmchat/camera
						if (f2 != null) {
							f = f2;
						}
						cameraFilename = f2.getAbsolutePath();// f.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
						cameraFilename = RequestConstant.IMAGE_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
						cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
						if (CommonUtils.isEmpty(cameraFilename)) {
							return;
						}

						/*
						 * if (data.getData() != null) { File file =
						 * UploadPictureUtils.getInit().getFileFromUri(data.
						 * getData(),MyProfileDetailActivity.this); bm =
						 * UploadPictureUtils.getInit().smallImage(file); }
						 */
						bm = ImageManager.getInstance().loadLocalImageSync(cameraFilename,false);//ImageUtil.getBitmapFromFile(cameraFilename, true);
						UploadPictureUtils.getInit().showClipPhoto(bm, viewPhoto, MyProfileDetailActivity.this, sCameraFilename, new IUploadHead() {
							@Override
							public void onUploadHead(String path, String filename) {
								uploadHead(path, filename);
							}

							@Override
							public void onCancelUpload() {

							}
						});
					}
				}
			}
			break;
		}

	}


	/**
	 * 点击头像后弹出的dialog
	 */
	public void alertMenu() {
		/*
		 * DialogItem[] items = new DialogItem[] { new
		 * DialogItem(R.string.camera, R.layout.custom_dialog_normal), new
		 * DialogItem(R.string.gallary, R.layout.custom_dialog_normal), new
		 * DialogItem(R.string.cancel, R.layout.custom_dialog_cancel) };
		 * ListDialog dialog = new ListDialog(context, Arrays.asList(items));
		 * dialog.setItemClick(this); dialog.show();
		 */
		createLocalFile();// 先创建裁切图片存放路径

		Intent mIntent = new Intent(this, PreviewImageActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putInt("size", 1);
		mIntent.putExtras(mBundle);
		startActivityForResult(mIntent, MessagesUtils.PICTURE);
	}

	/**
	 * 调用相机
	 */
	/*
	 * private void startCamearPicCut() {
	 * 
	 * // 调用系统的拍照功能 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	 * // 调用前置摄像头 // intent.putExtra("camerasensortype", 2); // // 自动对焦 //
	 * intent.putExtra("autofocus", true); // // 全屏 //
	 * intent.putExtra("fullScreen", false); //
	 * intent.putExtra("showActionIcons", false); // 指定调用相机拍照后照片的储存路径
	 * createLocalFile(); intent.putExtra(MediaStore.EXTRA_OUTPUT,
	 * Uri.fromFile(fCurrentFile)); try { startActivityForResult(intent,
	 * AF_INTENT_ACTION_CAMERA); } catch (Exception e) { // TODO Auto-generated
	 * catch block // e.printStackTrace();
	 * ToastManager.getInstance().show(context,R.string.no_camera); } }
	 */
	/**
	 * 调用相册
	 */
	/*
	 * private void startImageCaptrue() { createLocalFile(); Intent intent = new
	 * Intent(Intent.ACTION_PICK, null);
	 * intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	 * "image/*"); startActivityForResult(intent, AF_INTENT_ACTION_PICTURE); }
	 */

	/**
	 * 创建本地本件
	 */
	private void createLocalFile() {
		sCameraFilename = ClippingPicture.getCurrentFilename();
		PalmchatLogUtils.e("camera->", sCameraFilename);
		SharePreferenceService.getInstance(MyProfileDetailActivity.this).savaFilename(sCameraFilename);
		fCurrentFile = new File(RequestConstant.CAMERA_CACHE, sCameraFilename);
		CacheManager.getInstance().setCurFile(fCurrentFile);
	}

	/**
	 * 上传头像
	 * 
	 * @param path
	 * @param filename
	 */
	private void uploadHead(String path, String filename) {
		setShowHean(path);
		showProgressDialog(R.string.uploading);
		int handler = mAfPalmchat.AfHttpHeadUpload(path, filename, Consts.AF_AVATAR_FORMAT, null, MyProfileDetailActivity.this, MyProfileDetailActivity.this);
		// mhttpHandle.put(handler,handler);
	}

	/**
	 * 显示头像
	 * 
	 * @param filepath
	 */
	public void setShowHean(String filepath) {
		if (TextUtils.isEmpty(filepath)) {
			AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
			// WXL 20151013 调UIL的显示头像方法,统一图片管理
			ImageManager.getInstance().DisplayAvatarImage(profile_photo_value, profileInfo.getServerUrl(), profileInfo.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, profileInfo.sex, profileInfo.getSerialFromHead(), null);
			PalmchatLogUtils.d("==", "imageview==++:" + profileInfo.getServerUrl().toString());
		} else {
			/*Bitmap bitmap = ImageManager.getInstance().loadLocalImageSync(filepath,false);//ImageUtil.getBitmapFromFile(filepath);
			profile_photo_value.setImageBitmap(ImageUtil.toRoundCorner(bitmap));*/
			ImageManager.getInstance().displayLocalImage_circle(profile_photo_value,filepath ,0,0,null);

		}
	}

	/**
	 * 相机，相册的点击事件
	 * 
	 * @param
	 */
	/*
	 * @Override public void onItemClick(DialogItem item) { switch
	 * (item.getTextId()) { case R.string.camera: { startCamearPicCut(); break;
	 * } case R.string.gallary: { startImageCaptrue(); break; } default: break;
	 * } }
	 */

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		// mhttpHandle.put(httpHandle,Consts.AF_HTTP_HANDLE_INVALID);
		if (code == Consts.REQ_CODE_SUCCESS) {
			PalmchatLogUtils.i(TAG, "flag = " + (flag == Consts.REQ_UPDATE_INFO));
			if (flag == Consts.REQ_UPDATE_INFO) {// 更新信息
				// update profile
				afProfileInfo.isChange = false;

				if (result != null && result instanceof AfProfileInfo) {
					AfProfileInfo profileInfo = (AfProfileInfo) result;
					afProfileInfo.age = profileInfo.age;
					afProfileInfo.palmguess_flag = profileInfo.palmguess_flag;
					afProfileInfo.palmguess_version = profileInfo.palmguess_version;
					afProfileInfo.airtime = profileInfo.airtime;
					afProfileInfo.betway = profileInfo.betway;
					CacheManager.getInstance().setMyProfile(afProfileInfo);
					setContent(afProfileInfo);
					mAfPalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, afProfileInfo);
					if (afProfileInfo.isPhoneChange) {
						mAfPalmchat.AfHttpUpdateDatingInfo(Consts.REQ_UPDATE_SHOWMOBILE, getUploadDatingInfo(), null, this);
					}

					dismissProgressDialog();
					if (isChangeOfCountry) { // 国家改变时，首页则不显示
						EventBus.getDefault().post(new ChangeCountryEvent());
						EventBus.getDefault().post(new ChangeRegionEvent(true, profileInfo.palmguess_flag));
						finish();
//						SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).setChangeOfCountry(true);
//						SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).setUnReadNewPalmGuess(false);
//						DialogUtils.showReLoginDialog(this, this);
					} else {
						if (isChangeOfState || isChangeOfCity) {
							EventBus.getDefault().post(new ChangeRegionEvent(true, profileInfo.palmguess_flag));
							// CacheManager.getInstance().setOpenAirtime(profileInfo.airtime
							// == 1);
							CacheManager.getInstance().setRecharge_intro_url(profileInfo.recharge_intro_url); // zhh设置充值方式说明的

						}

						if (isDialogUpdate && !afProfileInfo.isPhoneChange) {
							Intent data = new Intent();
							data.putExtra(JsonConstant.KEY_IS_CHANGE, true);
							setResult(Constants.COMPLET_PROFILE, data);
							finish();
						}

 					}

				}

			} else if (flag == Consts.REQ_GET_INFO) {// 获取信息
				dismissProgressDialog();
				// get profile
				if (result != null && result instanceof AfProfileInfo) {

					AfProfileInfo profileInfo = (AfProfileInfo) result;
					afProfileInfo.age = profileInfo.age;

					setContent(afProfileInfo);
					CacheManager.getInstance().setMyProfile(afProfileInfo);
					mAfPalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, afProfileInfo);
				}
			} else if (flag == Consts.REQ_UPDATE_SHOWMOBILE) {
				dismissProgressDialog();
				PalmchatLogUtils.println("Consts.REQ_UPDATE_SHOWMOBILE Success");
				afProfileInfo.isPhoneChange = false;
				afProfileInfo.dating = (AfDatingInfo) result;
				AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
				myAfProfileInfo.dating = afProfileInfo.dating;
				setContent(afProfileInfo);
				mAfPalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, afProfileInfo);
				setResult(Constants.COMPLET_PROFILE);
				finish();
			} else if (flag == Consts.REQ_AVATAR_UPLOAD) {
				// 头像上传成功
				PalmchatLogUtils.i(TAG, "avatar upload success!!!" + user_data + "->result = " + result);
				// 将图片copy到 avatar目录下
				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SET_H_SUCC);
				// MobclickAgent.onEvent(context, ReadyConfigXML.SET_H_SUCC);
				ToastManager.getInstance().show(context, R.string.success);
				// PalmchatLogUtils.i(TAG, "AfRespAvatarInfo = " +
				// ((AfRespAvatarInfo) (result)).afid);
				copy(AF_ACTION_HEAD, result);

			} else if (flag == Consts.REQ_UPDATE_MARRIAGE) {
				dismissProgressDialog();
				afProfileInfo.dating = (AfDatingInfo) result;
				mAfPalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, afProfileInfo);
			}
		} else {
			if (flag == Consts.REQ_AVATAR_UPLOAD) {
				setShowHean(null);
			} else if (flag == Consts.REQ_UPDATE_INFO) {// zhh上传其余资料（除头像、恋爱状态外）
				if (!StringUtil.isNullOrEmpty(oldSign)) {
					CharSequence text = EmojiParser.getInstance(context).parse(oldSign, ImageUtil.dip2px(context, 18));
					textWhatsupValue.setText(text);
					CacheManager.getInstance().getMyProfile().signature = oldSign;
					afProfileInfo.signature = oldSign;
				}
				if (code == Consts.REQ_CODE_UNNETWORK) {
					Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
				} else if (code == Consts.REQ_CODE_ILLEGAL_WORD) { // zhh相关语句中含有非法词汇
					Consts.getInstance().showToast(context, code, flag, http_code);
				} else {
					Toast.makeText(this, R.string.failure, Toast.LENGTH_SHORT).show();
				}

			} else {
				Consts.getInstance().showToast(context, code, flag, http_code);
			}

			dismissProgressDialog();
		}
	}

	private void copy(final int type, final Object result) {
		// copy
		new Thread(new Runnable() {
			@Override
			public void run() {
				String path = fCurrentFile.getAbsolutePath();
				AfRespAvatarInfo info = (AfRespAvatarInfo) (result);
				if (info != null) {
					AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
					String pathD = RequestConstant.IMAGE_UIL_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(CommonUtils.
							getAvatarUrlKey(profileInfo.getServerUrl(), profileInfo.afId, info.serial, Consts.AF_HEAD_MIDDLE));
					// String fileName = afid + Consts.AF_HEAD_MIDDLE +
					// info.serial;
					File outFile = new File(pathD);
					FileUtils.copyToImg(path, outFile.getAbsolutePath());
					handler.obtainMessage(type, result).sendToTarget();
					if (fCurrentFile != null) {
						fCurrentFile.delete();
					}
				}
			}
		}).start();
	}

	@Override
	public void AfOnProgress(int httpHandle, int flag, int progress, Object user_data) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		 
	}

	  

}
