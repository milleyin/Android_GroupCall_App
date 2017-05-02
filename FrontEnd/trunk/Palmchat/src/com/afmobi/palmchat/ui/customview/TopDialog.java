package com.afmobi.palmchat.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.listener.OnItemClick;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.util.ImageUtil;
import com.core.Consts;

/**
 * 顶部浮层Dialog
 * 
 * @author heguiming
 * 
 */
public class TopDialog extends Dialog {

	private Context mContext;
	private View dialogView;

	public TopDialog(Context context) {
		super(context, R.style.TopCustomDialogTheme);
		this.mContext = context;
	}

	private OnItemClick onItemClick;

	public OnItemClick getOnItemClick() {
		return onItemClick;
	}

	public void setOnItemClick(OnItemClick onItemClick) {
		this.onItemClick = onItemClick;
	}

	 

	public static final String IS_SELECTED_HEAD = "isSelectedHead";
	public static final String M_SEX = "mSex";
	public static final String M_AGE = "mAge";

	public static final String IS_ONLINE = "isOnline";
	public static final String IS_NEARBY = "isNearby";

 
	// home dialog

	public void createHomeFilterDialog(byte mSex, boolean isOnline, boolean isNearby, String myCity, final OnOkButtonDialogListener onOkButtonDialogListener) {
		dialogView = LayoutInflater.from(mContext).inflate(R.layout.top_dialog_home_filter_layout, null);

		initHomeFilter(dialogView, mSex, isOnline, isNearby, myCity, onOkButtonDialogListener);

		WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
		localLayoutParams.x = 0;
		int y = ImageUtil.dip2px(mContext, 40);
		localLayoutParams.y = y;
		localLayoutParams.gravity = Gravity.TOP;
		dialogView.setMinimumWidth(10000);

		onWindowAttributesChanged(localLayoutParams);
		setCanceledOnTouchOutside(true);
		setCancelable(true);

	}

	private void initHomeFilter(View view, byte mSex, boolean isOnline, boolean isNearby, String myCity, final OnOkButtonDialogListener onOkButtonDialogListener) {
		final LookAroundFilterDialogViewHolder holder = new LookAroundFilterDialogViewHolder();
		holder.mGenderRadio = (RadioGroup) view.findViewById(R.id.gender_radio_g);
		if (mSex == Consts.AFMOBI_SEX_FEMALE_AND_MALE) {
			holder.mGenderRadio.check(R.id.gender_all);
		} else if (mSex == Consts.AFMOBI_SEX_MALE) {
			holder.mGenderRadio.check(R.id.gender_male);
		} else {
			holder.mGenderRadio.check(R.id.gender_female);
		}
		holder.mAreaRadio = (RadioGroup) view.findViewById(R.id.gender_radio_Area);
		if (isNearby) {
			holder.mAreaRadio.check(R.id.radio_Nearby);
		} else {
			holder.mAreaRadio.check(R.id.radio_city);
		}
		((TextView) view.findViewById(R.id.radio_city)).setText(myCity);
		holder.mPhotoRadio = (RadioGroup) view.findViewById(R.id.photo_radio_g);
		holder.mWithPhoto = (Button) view.findViewById(R.id.btn_vibrate);
		holder.mWithPhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (holder.mCheckBox.isChecked()) {
					holder.mCheckBox.setChecked(false);
				} else {
					holder.mCheckBox.setChecked(true);
				}
			}
		});
		holder.mCheckBox = (CheckBox) view.findViewById(R.id.checkbox_vibrate);
		holder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			}
		});
		holder.mCheckBox.setChecked(isOnline);
		if (isOnline) {
			holder.mPhotoRadio.check(R.id.photo_with);
		} else {
			holder.mPhotoRadio.check(R.id.photo_all);
		}

		holder.mOkBtn = (Button) view.findViewById(R.id.ok_btn);
		holder.mOkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int genderCheckId = holder.mGenderRadio.getCheckedRadioButtonId();
				byte mSex = getSelectSex(genderCheckId);
				 
				boolean isOnline = holder.mCheckBox.isChecked();// getSelectPhoto(photoCheckId);
				boolean isNearby = holder.mAreaRadio.getCheckedRadioButtonId() == R.id.radio_Nearby ? true : false;
				PalmchatLogUtils.println("initHomeFilter onClick  isOnline " + isOnline);
				 
				Bundle bundle = new Bundle();
				bundle.putBoolean(IS_ONLINE, isOnline);
				bundle.putBoolean(IS_NEARBY, isNearby);
				bundle.putByte(M_SEX, mSex);
				 
				if (onOkButtonDialogListener != null) {
					onOkButtonDialogListener.onOkButtonClick(bundle);
				}
				dismiss();
			}
		});
	}

	private byte getSelectSex(int id) {
		byte mSex = Consts.AFMOBI_SEX_FEMALE_AND_MALE;
		switch (id) {
		case R.id.gender_all:
			mSex = Consts.AFMOBI_SEX_FEMALE_AND_MALE;
			break;
		case R.id.gender_male:
			mSex = Consts.AFMOBI_SEX_MALE;
			break;
		case R.id.gender_female:
			mSex = Consts.AFMOBI_SEX_FEMALE;
			break;
		default:
			break;
		}
		return mSex;
	}
 

	@Override
	public void show() {
		super.show();
		if (mContext != null) {
			if (mContext instanceof MainTab) {
				((MainTab) mContext).setIco_start(0);
			}
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		if (mContext != null) {
			if (mContext instanceof MainTab) {
				((MainTab) mContext).setIco_start(1);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(dialogView);
	}

	class LookAroundFilterDialogViewHolder {
		RadioGroup mGenderRadio;
		RadioGroup mPhotoRadio;
		RadioGroup mAreaRadio;
		Button mOkBtn, mWithPhoto;
		CheckBox mCheckBox;
	}

	public interface OnOkButtonDialogListener {
		void onOkButtonClick(Bundle bundle);
	}
}
