package com.afmobi.palmchat.ui.customview.datepicker;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.DateUtil;

public class STDatePickerDialog extends Dialog implements OnClickListener,
		STDatePicker.OnDateChangedListener {
	
	public static final byte SET = 0x01;
	public static final byte DONE = 0x02;
	

	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY = "day";

	private Button leftButton;
	private Button rightButton;
	
	private TextView textTitle;
	
	private final STDatePicker mDatePicker;
	private final OnDateSetListener mCallBack;
	private final Calendar mCalendar;

	private boolean mTitleNeedsUpdate = true;

	/**
	 * The callback used to indicate the user is done filling in the date.
	 */
	public interface OnDateSetListener {

		/**
		 * @param view
		 *            The view associated with this listener.
		 * @param year
		 *            The year that was set.
		 * @param monthOfYear
		 *            The month that was set (0-11) for compatibility with
		 *            {@link java.util.Calendar}.
		 * @param dayOfMonth
		 *            The day of the month that was set.
		 */
		void onDateSet(STDatePicker view, int year, int monthOfYear,
				int dayOfMonth,String formatedDate);
	}

	/**
	 * @param context
	 *            The context the dialog is to run in.
	 * @param callBack
	 *            How the parent is notified that the date is set.
	 * @param year
	 *            The initial year of the dialog.
	 * @param monthOfYear
	 *            The initial month of the dialog.
	 * @param dayOfMonth
	 *            The initial day of the dialog.
	 */
	public STDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
		this(context,R.style.Theme_Hold_Dialog_Base, callBack, year, monthOfYear, dayOfMonth);
	}

	/**
	 * @param context
	 *            The context the dialog is to run in.
	 * @param theme
	 *            the theme to apply to this dialog
	 * @param callBack
	 *            How the parent is notified that the date is set.
	 * @param year
	 *            The initial year of the dialog.
	 * @param monthOfYear
	 *            The initial month of the dialog.
	 * @param dayOfMonth
	 *            The initial day of the dialog.
	 */
	public STDatePickerDialog(Context context, int theme,OnDateSetListener callBack, int year, int monthOfYear,int dayOfMonth) {
		super(context, theme);
		setCanceledOnTouchOutside(false);
		mCallBack = callBack;

		mCalendar = Calendar.getInstance();

		Context themeContext = getContext();
//		setButton(BUTTON_POSITIVE,
//				themeContext.getText(R.string.date_time_done), this);
//		setIcon(0);

		LayoutInflater inflater = (LayoutInflater) themeContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.date_picker_dialog, null);
		setContentView(view);
//		setView(view);
		
		textTitle = (TextView) findViewById(R.id.dialog_title_text);
		
		mDatePicker = (STDatePicker) view.findViewById(R.id.datePicker);
		mDatePicker.init(year, monthOfYear, dayOfMonth, this);
		updateTitle(year, monthOfYear, dayOfMonth);
		
		
		leftButton = (Button) view.findViewById(R.id.cancel_button);
		rightButton = (Button) view.findViewById(R.id.confim_button);
		leftButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		
		leftButton.setTag(DONE);
		rightButton.setTag(SET);
	}
	
	
	
	/**zhh  主要用于设置最大日期   如果传进来的参数maxYear为0 ，则默认为2100
	 * @param context
	 *            The context the dialog is to run in.
	 * @param callBack
	 *            How the parent is notified that the date is set.
	 * @param year
	 *            The initial year of the dialog.
	 * @param monthOfYear
	 *            The initial month of the dialog.
	 * @param dayOfMonth
	 *            The initial day of the dialog.
	 */
	public STDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth,long maxDate) {
		this(context,R.style.Theme_Hold_Dialog_Base, callBack, year, monthOfYear, dayOfMonth,maxDate);
	}
	/**
	 * zhh  主要用于设置最大日期   如果传进来的参数maxYear为0 ，则默认为2100
	 * @param context
	 * @param theme
	 * @param callBack
	 * @param year
	 * @param monthOfYear
	 * @param dayOfMonth
	 */
	public STDatePickerDialog(Context context, int theme,OnDateSetListener callBack, int year, int monthOfYear,int dayOfMonth,long maxDate) {
		super(context, theme);
		setCanceledOnTouchOutside(false);
		mCallBack = callBack;

		mCalendar = Calendar.getInstance();

		Context themeContext = getContext();
//		setButton(BUTTON_POSITIVE,
//				themeContext.getText(R.string.date_time_done), this);
//		setIcon(0);

		LayoutInflater inflater = (LayoutInflater) themeContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.date_picker_dialog, null);
		setContentView(view);
//		setView(view);
		
		textTitle = (TextView) findViewById(R.id.dialog_title_text);
		
		mDatePicker = (STDatePicker) view.findViewById(R.id.datePicker);
		mDatePicker.init(year, monthOfYear, dayOfMonth, this);
		if(maxDate > 0)  {
			mDatePicker.setMaxDate(maxDate);
		}
			
		updateTitle(year, monthOfYear, dayOfMonth);
		
		
		leftButton = (Button) view.findViewById(R.id.cancel_button);
		rightButton = (Button) view.findViewById(R.id.confim_button);
		leftButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		
		leftButton.setTag(DONE);
		rightButton.setTag(SET);
	}
	
	
	@Override
	public void onClick(View arg0) {
		this.dismiss();
		Object tag = arg0.getTag();
		if (tag != null) {
			if ((Byte)tag == SET) {
				tryNotifyDateSet();
			}
		}        
		
	}

	public void onClick(DialogInterface dialog, int which) {
		
	}

	public void onDateChanged(STDatePicker view, int year, int month, int day) {
		mDatePicker.init(year, month, day, this);
		updateTitle(year, month, day);
	}

	/**
	 * Gets the {@link DatePicker} contained in this dialog.
	 * 
	 * @return The calendar view.
	 */
	public STDatePicker getDatePicker() {
		return mDatePicker;
	}

	/**
	 * Sets the current date.
	 * 
	 * @param year
	 *            The date year.
	 * @param monthOfYear
	 *            The date month.
	 * @param dayOfMonth
	 *            The date day of month.
	 */
	public void updateDate(int year, int monthOfYear, int dayOfMonth) {
		mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
	}

	private void tryNotifyDateSet() {
		if (mCallBack != null) {
			mDatePicker.clearFocus();
			int iMonth=mDatePicker.getMonth()+1;
			String month=String.valueOf( iMonth);
			String day= String.valueOf(mDatePicker.getDayOfMonth());
			if(iMonth<10){
				month="0"+month;
			}
			if(mDatePicker.getDayOfMonth()<10){
				day="0"+day;
			}
			//这里只所有不用SimpleFormat.format是因为多国语言的情况下 如阿拉伯语会变成非数字的字符 导致服务器不识别 所以自己处理
			String formatedDate=day+"-"+month+"-"+ mDatePicker.getYear();
			mCallBack.onDateSet(mDatePicker, mDatePicker.getYear(),
					mDatePicker.getMonth(), mDatePicker.getDayOfMonth(),formatedDate);
		}
	}

	private void updateTitle(int year, int month, int day) {
		if (!mDatePicker.getCalendarViewShown()) {
			mCalendar.set(Calendar.YEAR, year);
			mCalendar.set(Calendar.MONTH, month);
			mCalendar.set(Calendar.DAY_OF_MONTH, day);
//			String title = DateUtils.formatDateTime(getContext(),mCalendar.getTimeInMillis(),
//					DateUtils.FORMAT_SHOW_DATE
//					| DateUtils.FORMAT_SHOW_WEEKDAY
//					| DateUtils.FORMAT_SHOW_YEAR
//					| DateUtils.FORMAT_ABBREV_MONTH
//					| DateUtils.FORMAT_ABBREV_WEEKDAY);
			String title = DateFormat.format("yyyy-MM-dd", mCalendar).toString();
//			String title = DateUtil.getDateFormate_month("yyyy-MMM-dd").format(mCalendar.getTime());
			setTitle(title);
			mTitleNeedsUpdate = true;
		} else {
			if (mTitleNeedsUpdate) {
				mTitleNeedsUpdate = false;
				setTitle(R.string.date_picker_dialog_title);
			}
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		if (textTitle == null) {
			super.setTitle(title);
		} else {
			textTitle.setText(title);
		}
	}

	@Override
	public void setTitle(int titleId) {
		if (textTitle == null) {
			super.setTitle(titleId);
		} else {
			textTitle.setText(titleId);
		}
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(YEAR, mDatePicker.getYear());
		state.putInt(MONTH, mDatePicker.getMonth());
		state.putInt(DAY, mDatePicker.getDayOfMonth());
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int year = savedInstanceState.getInt(YEAR);
		int month = savedInstanceState.getInt(MONTH);
		int day = savedInstanceState.getInt(DAY);
		mDatePicker.init(year, month, day, this);
	}

	
}