package com.afmobi.palmchat.ui.customview.datepicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
//import android.view.accessibility.AccessibilityNodeInfo;

public class STTimePicker extends FrameLayout {

    private static final String LOG_TAG = STDatePicker.class.getSimpleName();

    private static final String DATE_FORMAT = "MM/dd/yyyy";

    private static final int DEFAULT_START_YEAR = 1900;

    private static final int DEFAULT_END_YEAR = 2100;

    private static final boolean DEFAULT_CALENDAR_VIEW_SHOWN = true;

    private static final boolean DEFAULT_SPINNERS_SHOWN = true;

    private static final boolean DEFAULT_ENABLED_STATE = true;

    private final LinearLayout mSpinners;

    private final STNumberPicker mDaySpinner;

//    private final STNumberPicker mMonthSpinner;

    private final STNumberPicker mYearSpinner;

    private final EditText mDaySpinnerInput;

//    private final EditText mMonthSpinnerInput;

    private final EditText mYearSpinnerInput;

    private final STCalendarView mCalendarView;

    private Locale mCurrentLocale;

    private OnTimeChangedListener mOnTimeChangedListener;

//    private String[] mShortMonths;

    private final java.text.DateFormat mDateFormat = new SimpleDateFormat(DATE_FORMAT);

//    private int mNumberOfMonths;

    private Calendar mTempDate;

    private Calendar mMinDate;

    private Calendar mMaxDate;

    private Calendar mCurrentDate;


    private boolean mIsEnabled = DEFAULT_ENABLED_STATE;


    /**
     * The callback used to indicate the user changes\d the date.
     */
    public interface OnTimeChangedListener {

        /**
         * Called upon a date change.
         *
         * @param view The view associated with this listener.
         * @param hour The hour that was set.
         * @param minute The minute that was set (0-59) for compatibility
         *            with {@link java.util.Calendar}.
         * @param am_pm The day of the am_pm that was set.
         */
        void onDateChanged(STTimePicker view, int hour, int minute, int am_pm);
    }

    public STTimePicker(Context context) {
        this(context, null);
    }

    public STTimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.datePickerStyle);
    }

    public STTimePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // initialization based on locale
        setCurrentLocale(Locale.getDefault());

        TypedArray attributesArray = context.obtainStyledAttributes(attrs, R.styleable.STDatePicker,
                defStyle, 0);
        boolean spinnersShown = attributesArray.getBoolean(R.styleable.STDatePicker_dp_spinnersShown,
                DEFAULT_SPINNERS_SHOWN);
        boolean calendarViewShown = attributesArray.getBoolean(
                R.styleable.STDatePicker_dp_calendarViewShown, DEFAULT_CALENDAR_VIEW_SHOWN);
        int startYear = attributesArray.getInt(R.styleable.STDatePicker_dp_startYear,
                DEFAULT_START_YEAR);
        int endYear = attributesArray.getInt(R.styleable.STDatePicker_dp_endYear, DEFAULT_END_YEAR);
        String minDate = attributesArray.getString(R.styleable.STDatePicker_dp_minDate);
        String maxDate = attributesArray.getString(R.styleable.STDatePicker_dp_maxDate);
        int layoutResourceId = attributesArray.getResourceId(R.styleable.STDatePicker_dp_internalLayout,
                R.layout.date_picker_holo);
        attributesArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layoutResourceId, this, true);

        STNumberPicker.OnValueChangeListener onChangeListener = new STNumberPicker.OnValueChangeListener() {
            public void onValueChange(STNumberPicker picker, int oldVal, int newVal) {
                updateInputState();
                // take care of wrapping of days and months to update greater fields
//                mTempDate.setTimeInMillis(mCurrentDate.getTimeInMillis());
                if (picker == mDaySpinner) {
//                  int maxHourOfDay = mTempDate.getActualMaximum(Calendar.HOUR);
//                    if (oldVal == maxHourOfDay && newVal == 1) {
//                        mTempDate.add(Calendar.HOUR, 1);
//                    } else if (oldVal == 1 && newVal == maxHourOfDay) {
//                        mTempDate.add(Calendar.HOUR, -1);
//                    } else if (oldVal == 1 && newVal == 12) {
//                        mTempDate.set(Calendar.HOUR,12);
//                    } else if (oldVal == 11 && newVal == 12) {
//                        mTempDate.set(Calendar.HOUR,12);
//                    }
//                    else {
//                        mTempDate.add(Calendar.HOUR, newVal - oldVal);
//                    }
                    mTempDate.set(Calendar.HOUR, newVal);
                } else if (picker == mYearSpinner) {
                    mTempDate.set(Calendar.AM_PM, newVal);
                } else {
                    throw new IllegalArgumentException();
                }
//                setClock(mTempDate.get(Calendar.HOUR),mTempDate.get(Calendar.MINUTE),mTempDate.get(Calendar.AM_PM));
//                updateClockSpinners(mTempDate.get(Calendar.HOUR),mTempDate.get(Calendar.MINUTE),mTempDate.get(Calendar.AM_PM));
//                updateCalendarView();
                notifyDateChanged();

            }
        };

        mSpinners = (LinearLayout) findViewById(R.id.pickers);

        // calendar view day-picker
        mCalendarView = (STCalendarView) findViewById(R.id.calendar_view);
        mCalendarView.setOnTimeChangeListener(new STCalendarView.OnTimeChangeListener() {
            public void onSelectedTimeChange(STCalendarView view, int hour, int minute, int am_pm) {
                setClock(hour, minute, am_pm);
                updateClockSpinners(hour, minute, am_pm);
                notifyDateChanged();
            }
        });

        // day
        mDaySpinner = (STNumberPicker) findViewById(R.id.day);
        mDaySpinner.setFormatter(STNumberPicker.getTwoDigitFormatter());
        mDaySpinner.setOnLongPressUpdateInterval(100);
        mDaySpinner.setOnValueChangedListener(onChangeListener);
        mDaySpinnerInput = (EditText) mDaySpinner.findViewById(R.id.np__numberpicker_input);


        // year
        mYearSpinner = (STNumberPicker) findViewById(R.id.year);
        mYearSpinner.setOnLongPressUpdateInterval(100);
        mYearSpinner.setOnValueChangedListener(onChangeListener);
        mYearSpinnerInput = (EditText) mYearSpinner.findViewById(R.id.np__numberpicker_input);

        // show only what the user required but make sure we
        // show something and the spinners have higher priority
        if (!spinnersShown && !calendarViewShown) {
            setSpinnersShown(true);
        } else {
            setSpinnersShown(spinnersShown);
            setCalendarViewShown(calendarViewShown);
        }

        // set the min date giving priority of the minDate over startYear
        mTempDate.clear();
        if (!TextUtils.isEmpty(minDate)) {
            if (!parseDate(minDate, mTempDate)) {
                mTempDate.set(startYear, 0, 1);
            }
        } else {
            mTempDate.set(startYear, 0, 1);
        }
        setMinDate(mTempDate.getTimeInMillis());

        // set the max date giving priority of the maxDate over endYearo
        mTempDate.clear();
        if (!TextUtils.isEmpty(maxDate)) {
            if (!parseDate(maxDate, mTempDate)) {
                mTempDate.set(endYear, 11, 31);
            }
        } else {
            mTempDate.set(endYear, 11, 31);
        }
        setMaxDate(mTempDate.getTimeInMillis());

        // initialize to current date
        mCurrentDate.setTimeInMillis(System.currentTimeMillis());
        initclock(mCurrentDate.get(Calendar.YEAR), mCurrentDate.get(Calendar.MONTH), mCurrentDate
                .get(Calendar.DAY_OF_MONTH), null);

        // re-order the number spinners to match the current date format
        reorderSpinners();

        // accessibility
        setContentDescriptions();
    }

    /**
     * Gets the minimal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     * <p>
     * Note: The default minimal date is 01/01/1900.
     * <p>
     *
     * @return The minimal supported date.
     */
    public long getMinDate() {
        return mCalendarView.getMinDate();
    }

    /**
     * Sets the minimal date supported by this {@link NumberPicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @param minDate The minimal supported date.
     */
    public void setMinDate(long minDate) {
        mTempDate.setTimeInMillis(minDate);
        if (mTempDate.get(Calendar.YEAR) == mMinDate.get(Calendar.YEAR)
                && mTempDate.get(Calendar.DAY_OF_YEAR) != mMinDate.get(Calendar.DAY_OF_YEAR)) {
            return;
        }
        mMinDate.setTimeInMillis(minDate);
        mCalendarView.setMinDate(minDate);
        if (mCurrentDate.before(mMinDate)) {
            mCurrentDate.setTimeInMillis(mMinDate.getTimeInMillis());
            updateCalendarView();
        }
//        updateSpinners();
    }

    /**
     * Gets the maximal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     * <p>
     * Note: The default maximal date is 12/31/2100.
     * <p>
     *
     * @return The maximal supported date.
     */
    public long getMaxDate() {
        return mCalendarView.getMaxDate();
    }

    /**
     * Sets the maximal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @param maxDate The maximal supported date.
     */
    public void setMaxDate(long maxDate) {
        mTempDate.setTimeInMillis(maxDate);
        if (mTempDate.get(Calendar.YEAR) == mMaxDate.get(Calendar.YEAR)
                && mTempDate.get(Calendar.DAY_OF_YEAR) != mMaxDate.get(Calendar.DAY_OF_YEAR)) {
            return;
        }
        mMaxDate.setTimeInMillis(maxDate);
        mCalendarView.setMaxDate(maxDate);
        if (mCurrentDate.after(mMaxDate)) {
            mCurrentDate.setTimeInMillis(mMaxDate.getTimeInMillis());
            updateCalendarView();
        }
//        updateSpinners();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (mIsEnabled == enabled) {
            return;
        }
        super.setEnabled(enabled);
        mDaySpinner.setEnabled(enabled);
        mYearSpinner.setEnabled(enabled);
        mCalendarView.setEnabled(enabled);
        mIsEnabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return mIsEnabled;
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
//        onPopulateAccessibilityEvent(event);
        return true;
    }


    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setCurrentLocale(newConfig.locale);
    }

    /**
     * Gets whether the {@link CalendarView} is shown.
     *
     * @return True if the calendar view is shown.
     * @see #getCalendarView()
     */
    public boolean getCalendarViewShown() {
        return mCalendarView.isShown();
    }

    /**
     * Gets the {@link CalendarView}.
     *
     * @return The calendar view.
     * @see #getCalendarViewShown()
     */
    public STCalendarView getCalendarView () {
        return mCalendarView;
    }

    /**
     * Sets whether the {@link CalendarView} is shown.
     *
     * @param shown True if the calendar view is to be shown.
     */
    public void setCalendarViewShown(boolean shown) {
        mCalendarView.setVisibility(shown ? VISIBLE : GONE);
    }

    /**
     * Gets whether the spinners are shown.
     *
     * @return True if the spinners are shown.
     */
    public boolean getSpinnersShown() {
        return mSpinners.isShown();
    }

    /**
     * Sets whether the spinners are shown.
     *
     * @param shown True if the spinners are to be shown.
     */
    public void setSpinnersShown(boolean shown) {
        mSpinners.setVisibility(shown ? VISIBLE : GONE);
    }

    /**
     * Sets the current locale.
     *
     * @param locale The current locale.
     */
    private void setCurrentLocale(Locale locale) {
        if (locale.equals(mCurrentLocale)) {
            return;
        }

        mCurrentLocale = locale;

        mTempDate = getCalendarForLocale(mTempDate, locale);
        mMinDate = getCalendarForLocale(mMinDate, locale);
        mMaxDate = getCalendarForLocale(mMaxDate, locale);
        mCurrentDate = getCalendarForLocale(mCurrentDate, locale);
    }

    /**
     * Gets a calendar for locale bootstrapped with the value of a given calendar.
     *
     * @param oldCalendar The old calendar.
     * @param locale The locale.
     */
    private Calendar getCalendarForLocale(Calendar oldCalendar, Locale locale) {
        if (oldCalendar == null) {
            return Calendar.getInstance(locale);
        } else {
            final long currentTimeMillis = oldCalendar.getTimeInMillis();
            Calendar newCalendar = Calendar.getInstance(locale);
            newCalendar.setTimeInMillis(currentTimeMillis);
            return newCalendar;
        }
    }

    /**
     * Reorders the spinners according to the date format that is
     * explicitly set by the user and if no such is set fall back
     * to the current locale's default format.
     */
    private void reorderSpinners() {
        mSpinners.removeAllViews();
        mSpinners.addView(mDaySpinner);
        setImeOptions(mDaySpinner, 2, 0);
        mSpinners.addView(mYearSpinner);
        setImeOptions(mYearSpinner, 2, 1);
    }

    /**
     * Updates the current date.
     *
     * @param year The year.
     * @param month The month which is <strong>starting from zero</strong>.
     * @param dayOfMonth The day of the month.
     */
    public void updateDate(int year, int month, int dayOfMonth) {
        if (!isNewDate(year, month, dayOfMonth)) {
            return;
        }
        setDate(year, month, dayOfMonth);
//        updateSpinners();
        updateCalendarView();
        notifyDateChanged();
    }

    // Override so we are in complete control of save / restore for this widget.
    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, getHour(), getMinute(), getam_pm());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setDate(ss.mHour, ss.mMinute, ss.mAm_Pm);
        updateClockSpinners(ss.mHour, ss.mMinute, ss.mAm_Pm);
        updateCalendarView();
    }



    /**
     * Initialize the state. If the provided values designate an inconsistent
     * date the values are normalized before updating the spinners.
     *
     * @param hour The initial year.
     * @param minute The initial month <strong>starting from zero</strong>.
     * @param am_pm The initial day of the month.
     * @param onTimeChangedListener How user is notified date is changed by
     *            user, can be null.
     */
    public void initclock(int hour, int minute, int am_pm,
                          OnTimeChangedListener onTimeChangedListener) {
        setClock(hour, minute, am_pm);
        updateClockSpinners(hour, minute, am_pm);
        updateCalendarView();
        mOnTimeChangedListener = onTimeChangedListener;
    }


    /**
     * Parses the given <code>date</code> and in case of success sets the result
     * to the <code>outDate</code>.
     *
     * @return True if the date was parsed.
     */
    private boolean parseDate(String date, Calendar outDate) {
        try {
            outDate.setTime(mDateFormat.parse(date));
            return true;
        } catch (ParseException e) {
            Log.w(LOG_TAG, "Date: " + date + " not in format: " + DATE_FORMAT);
            return false;
        }
    }

    private boolean isNewDate(int year, int month, int dayOfMonth) {
        return (mCurrentDate.get(Calendar.YEAR) != year
                || mCurrentDate.get(Calendar.MONTH) != dayOfMonth
                || mCurrentDate.get(Calendar.DAY_OF_MONTH) != month);
    }

    private void setDate(int year, int month, int dayOfMonth) {
        mCurrentDate.set(year, month, dayOfMonth);
        if (mCurrentDate.before(mMinDate)) {
            mCurrentDate.setTimeInMillis(mMinDate.getTimeInMillis());
        } else if (mCurrentDate.after(mMaxDate)) {
            mCurrentDate.setTimeInMillis(mMaxDate.getTimeInMillis());
        }
    }

    private void setClock(int hour, int minute, int am_pm) {
        mCurrentDate.set(Calendar.HOUR,hour);
        mCurrentDate.set(Calendar.MINUTE,minute);
        mCurrentDate.set(Calendar.AM_PM,am_pm);
    }


    private void updateClockSpinners(int hour, int minute, int am_pm) {

        mDaySpinner.setMinValue(1);
        mDaySpinner.setMaxValue(12);
        mDaySpinner.setWrapSelectorWheel(true);
        mYearSpinner.setMinValue(Calendar.AM);
        mYearSpinner.setMaxValue(Calendar.PM);
        mYearSpinner.setWrapSelectorWheel(false);
        String[] displayedValues = new String[] {"AM","PM"};
        mYearSpinner.setDisplayedValues(displayedValues);
        mYearSpinner.setValue(am_pm);
        mDaySpinner.setValue(hour);


    }

    /**
     * Updates the calendar view with the current date.
     */
    private void updateCalendarView() {
        mCalendarView.setDate(mCurrentDate.getTimeInMillis(), false, false);
    }

    /**
     * @return The selected year.
     */
    public int getHour() {
        return mDaySpinner.getValue();
    }

    /**
     * @return The selected month.
     */
    public int getMinute() {
        return mCurrentDate.get(Calendar.MINUTE);
    }

    /**
     * @return The selected day of month.
     */
    public int getam_pm() {
        return mYearSpinner.getValue();
    }

    /**
     * Notifies the listener, if such, for a change in the selected date.
     */
    private void notifyDateChanged() {
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
        if (mOnTimeChangedListener != null) {
            mOnTimeChangedListener.onDateChanged(this,getHour(), getMinute(), getam_pm());
        }
    }

    /**
     * Sets the IME options for a spinner based on its ordering.
     *
     * @param spinner The spinner.
     * @param spinnerCount The total spinner count.
     * @param spinnerIndex The index of the given spinner.
     */
    private void setImeOptions(STNumberPicker spinner, int spinnerCount, int spinnerIndex) {
        final int imeOptions;
        if (spinnerIndex < spinnerCount - 1) {
            imeOptions = EditorInfo.IME_ACTION_NEXT;
        } else {
            imeOptions = EditorInfo.IME_ACTION_DONE;
        }
        TextView input = (TextView) spinner.findViewById(R.id.np__numberpicker_input);
        input.setImeOptions(imeOptions);
    }

    private void setContentDescriptions() {
        if (true) return; // increment/decrement buttons don't exist in backport
        // Day
        trySetContentDescription(mDaySpinner, R.id.st__increment,
                R.string.date_picker_increment_day_button);
        trySetContentDescription(mDaySpinner, R.id.st__decrement,
                R.string.date_picker_decrement_day_button);
        // Month
//        trySetContentDescription(mMonthSpinner, R.id.st__increment,
//                R.string.date_picker_increment_month_button);
//        trySetContentDescription(mMonthSpinner, R.id.st__decrement,
//                R.string.date_picker_decrement_month_button);
        // Year
        trySetContentDescription(mYearSpinner, R.id.st__increment,
                R.string.date_picker_increment_year_button);
        trySetContentDescription(mYearSpinner, R.id.st__decrement,
                R.string.date_picker_decrement_year_button);
    }

    private void trySetContentDescription(View root, int viewId, int contDescResId) {
        View target = root.findViewById(viewId);
        if (target != null) {
            target.setContentDescription(getContext().getString(contDescResId));
        }
    }

    private void updateInputState() {
        // Make sure that if the user changes the value and the IME is active
        // for one of the inputs if this widget, the IME is closed. If the user
        // changed the value via the IME and there is a next input the IME will
        // be shown, otherwise the user chose another means of changing the
        // value and having the IME up makes no sense.
        //InputMethodManager inputMethodManager = InputMethodManager.peekInstance();
        InputMethodManager inputMethodManager =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive(mYearSpinnerInput)) {
                mYearSpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
//            } else if (inputMethodManager.isActive(mMonthSpinnerInput)) {
//                mMonthSpinnerInput.clearFocus();
//                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            } else if (inputMethodManager.isActive(mDaySpinnerInput)) {
                mDaySpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }

    /**
     * Class for managing state storing/restoring.
     */
    private static class SavedState extends BaseSavedState {

        private final int mHour;

        private final int mMinute;

        private final int mAm_Pm;

        /**
         * Constructor called from {@link DatePicker#onSaveInstanceState()}
         */
        private SavedState(Parcelable superState, int hour, int minute, int am_pm) {
            super(superState);
            mHour = hour;
            mMinute = minute;
            mAm_Pm = am_pm;
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            mHour = in.readInt();
            mMinute = in.readInt();
            mAm_Pm = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mHour);
            dest.writeInt(mMinute);
            dest.writeInt(mAm_Pm);
        }

        @SuppressWarnings("all")
        // suppress unused and hiding
        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
