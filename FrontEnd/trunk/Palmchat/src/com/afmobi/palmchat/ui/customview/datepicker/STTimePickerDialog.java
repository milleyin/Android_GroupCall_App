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

public class STTimePickerDialog extends Dialog implements OnClickListener,
        STTimePicker.OnTimeChangedListener {

    public static final byte SET = 0x01;
    public static final byte DONE = 0x02;

    private static final String HOUR = "hour";
    private static final String AM_PM = "am_pm";

    private Button leftButton;
    private Button rightButton;

    private TextView textTitle;
    private final STTimePicker mDatePicker;
    private final OnTimeSetListener mCallBack;
    private final Calendar mCalendar;
    private boolean mTitleNeedsUpdate = true;



    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnTimeSetListener {

        /**
         * @param view
         *            The view associated with this listener.
         * @param hour
         *            The year that was set.
         * @param minute
         *            The month that was set (0-11) for compatibility with
         *            {@link java.util.Calendar}.
         * @param am_pm
         *            The day of the month that was set.
         */
        void onDateSet(STTimePicker view, int hour, int minute,
                       int am_pm,String formatedDate);
    }

    /**
     *   是否年日 true 就是 闹钟类型
     * @param context
     *            The context the dialog is to run in.
     * @param callBack
     *            How the parent is notified that the date is set.
     * @param hour
     *            The initial year of the dialog.
     * @param minute
     *            The initial month of the dialog.
     * @param am_pm
     *            The initial day of the dialog.
     */
    public STTimePickerDialog(Context context, OnTimeSetListener callBack, int hour, int minute, int am_pm,String title) {
        this(context,R.style.Theme_Hold_Dialog_Base, callBack, hour, minute, am_pm,title);
    }



    /**
     * @param context
     *            The context the dialog is to run in.
     * @param theme
     *            the theme to apply to this dialog
     * @param callBack
     *            How the parent is notified that the date is set.
     * @param hour
     *            The initial hour of the dialog.
     * @param minute
     *            The initial minute of the dialog.
     * @param am_pm
     *            The initial am_pm of the dialog.
     * @param title
     *           The initial title of the dialog.
     */
    public STTimePickerDialog(Context context, int theme,OnTimeSetListener callBack, int hour, int minute,int am_pm,String title) {
        super(context, theme);
        setCanceledOnTouchOutside(false);
        mCallBack = callBack;

        mCalendar = Calendar.getInstance();

        Context themeContext = getContext();

        LayoutInflater inflater = (LayoutInflater) themeContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.time_picker_dialog, null);
        setContentView(view);
        textTitle = (TextView) findViewById(R.id.dialog_title_text);
        textTitle.setText(title);
        mDatePicker = (STTimePicker) view.findViewById(R.id.timePicker);
        mDatePicker.initclock(hour, minute, am_pm, this);
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

    public void onDateChanged(STTimePicker view, int hour, int minute, int am_pm) {
        mDatePicker.initclock(hour, minute, am_pm, this);
//        updateTitle(hour, minute, am_pm);
    }

    /**
     * Gets the {@link DatePicker} contained in this dialog.
     *
     * @return The calendar view.
     */
    public STTimePicker getTimePicker() {
        return mDatePicker;
    }



    private void tryNotifyDateSet() {
        if (mCallBack != null) {
            mDatePicker.clearFocus();
            int iHour=mDatePicker.getHour();
            String hour=String.valueOf( iHour);
            if (iHour==0)
            {
                hour=String.valueOf( 12);
            }
            String am_pm="";
            if (mDatePicker.getam_pm()==0)
            {
                am_pm="AM";
            } else
            {
                am_pm="PM";
            }
            String formatedDate=hour+" "+ am_pm;
            mCallBack.onDateSet(mDatePicker, mDatePicker.getHour(),
                    mDatePicker.getMinute(), mDatePicker.getam_pm(),formatedDate);
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
        state.putInt(HOUR, mDatePicker.getHour());
        state.putInt(AM_PM, mDatePicker.getam_pm());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int hour = savedInstanceState.getInt(HOUR);
        int am_pm = savedInstanceState.getInt(AM_PM);
        mDatePicker.initclock(hour, 0, am_pm, this);
    }


}