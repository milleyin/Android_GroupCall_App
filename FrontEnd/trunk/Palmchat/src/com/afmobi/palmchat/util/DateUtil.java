package com.afmobi.palmchat.util;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;

/**
 * date util
 * 
 * @author heguiming
 * 
 */
public class DateUtil {

	// 一天
	private final static long ONE_DAYS = 86400 * 1000;

	private static DateFormat dateFormate_list = null;

	private static DateFormat dateFormate_week = null;

	private static DateFormat dateFormate_month = null;

	private static final String dateFormate_list_PATTEN = "HH:mm";
	private static final String dateFormate_week_PATTEN = "EEE HH:mm";
	private static final String dateFormate_month_PATTEN_EN = "MMM dd HH:mm";
	private static final String dateFormate_week_format = "EEE";
	private static final String dateFormate_month_day= "MMM dd";
	// 5.0
	private static final long ONE_MINUTE = 60000L;
	private static final long ONE_HOUR = 3600000L;
	private static final long ONE_DAY = 86400000L;
	private static final long ONE_WEEK = 604800000L;

//	private static final int ONE_SECOND_AGO = R.string.second_ago;
	private static final int ONE_MINUTE_AGO = R.string.minute_ago;
	private static final int ONE_HOUR_AGO = R.string.hour_ago;
	/*private static final int ONE_DAY_AGO = R.string.day_ago;
	private static final int ONE_MONTH_AGO = R.string.month_ago;
	private static final int ONE_YEAR_AGO = R.string.year_ago;

	private static final int ONE_SECOND_AGO_NOT_S = R.string.second_ago_not_s;
	private static final int ONE_MINUTE_AGO_NOT_S = R.string.minute_ago_not_s;
	private static final int ONE_HOUR_AGO_NOT_S = R.string.hour_ago_not_s;
	private static final int ONE_DAY_AGO_NOT_S = R.string.day_ago_not_s;
	private static final int ONE_MONTH_AGO_NOT_S = R.string.month_ago_not_s;
	private static final int ONE_YEAR_AGO_NOT_S = R.string.year_ago_not_s;*/
	private static final int JUST_NOW = R.string.just_now;
	public static boolean compare_date(String datatime, String Systemdatatime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(datatime);
			Date dt2 = df.parse(Systemdatatime);
			long time = dt2.getTime() - dt1.getTime();
			if (time >= ONE_MINUTE) {
				return true;
			} else {
				return false;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}



	/** 格式化时间
	 * @param date 时间
	 * @param context
	 * @return 转化为Today,yesterday
	 */
	public static String dateChangeStr(Date date, Context context) {
		if (null == date) {
			return "";
		}
		try {
			Calendar cal = Calendar.getInstance();
			int week1 = cal.get(Calendar.DAY_OF_WEEK);
			long timeInMillis1 = cal.getTimeInMillis();

			cal.setTime(date);
			long timeInMillis2 = cal.getTimeInMillis();
			int week2 = cal.get(Calendar.DAY_OF_WEEK);

			if (timeInMillis1 < timeInMillis2) {
				if (timeInMillis2 - timeInMillis1 < ONE_DAYS && week1 == week2) {
					return context.getString(R.string.today) + " " + getDateFormate_list().format(date);
				} else {
					return getDateFormate_month().format(date);
				}
			}
			int day = (int) Math.abs((timeInMillis1 - timeInMillis2) / ONE_DAYS);
			if (day < 1 && week1 == week2) {
				return context.getString(R.string.today) + " " + getDateFormate_list().format(date);
			} else if (day < 2 && (Math.abs(week1 - week2) == 1 || Math.abs(week1 - week2) == 6)) {
				return context.getString(R.string.yesterday) + " " + getDateFormate_list().format(date);
			} else if (day < 7 && week1 != week2) {
				return getDateFormate_week().format(date);
			} else {
				return getDateFormate_month().format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/** 格式化时间
	 * @param date 时间
	 * @param context
	 * @return 转化为Today,yesterday
	 */
	public static String dateChangeStrForChats(Date date, Context context) {
		if (null == date) {
			return "";
		}
		try {
			Calendar cal = Calendar.getInstance();
			int week1 = cal.get(Calendar.DAY_OF_WEEK);
			long timeInMillis1 = cal.getTimeInMillis();

			cal.setTime(date);
			long timeInMillis2 = cal.getTimeInMillis();
			int week2 = cal.get(Calendar.DAY_OF_WEEK);

			if (timeInMillis1 < timeInMillis2) {
				if (timeInMillis2 - timeInMillis1 < ONE_DAYS && week1 == week2) {
					return getDateFormate_list().format(date);
				} else {
					return getDateFormate_month_day().format(date);
				}
			}
			int day = (int) Math.abs((timeInMillis1 - timeInMillis2) / ONE_DAYS);
			if (day < 1 && week1 == week2) {
				return getDateFormate_list().format(date);
			} else if (day < 2 && (Math.abs(week1 - week2) == 1 || Math.abs(week1 - week2) == 6)) {
				return context.getString(R.string.yesterday);
			} else if (day < 7 && week1 != week2) {
				return getDateFormate_only_week_short().format(date);
			} else {
				return getDateFormate_month_day().format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private synchronized static DateFormat getDateFormate_list() {
		if (dateFormate_list == null) {
			dateFormate_list = new SimpleDateFormat(dateFormate_list_PATTEN);
		}
		return dateFormate_list;
	}

	private synchronized static DateFormat getDateFormate_week() {
		if (dateFormate_week == null) {
			DateFormatSymbols symbols = new DateFormatSymbols();
			Context context = PalmchatApp.getApplication();
			String[] oddWeekAbbreviations = new String[] { "", context.getString(R.string.sunday), context.getString(R.string.monday), context.getString(R.string.tuesday), context.getString(R.string.wednesday), context.getString(R.string.thursday), context.getString(R.string.friday), context.getString(R.string.saturday) };
			symbols.setShortWeekdays(oddWeekAbbreviations);
			dateFormate_week = new SimpleDateFormat(dateFormate_week_PATTEN, symbols);
		}
		return dateFormate_week;
	}

	private synchronized static DateFormat getDateFormate_only_week_short() {
		if (dateFormate_week == null) {
			DateFormatSymbols symbols = new DateFormatSymbols();
			Context context = PalmchatApp.getApplication();
			String[] oddWeekAbbreviations = new String[] { "", context.getString(R.string.sunday_short), context.getString(R.string.monday_short), context.getString(R.string.tuesday_short), context.getString(R.string.wednesday_short), context.getString(R.string.thursday_short), context.getString(R.string.friday_short), context.getString(R.string.saturday_short) };
			symbols.setShortWeekdays(oddWeekAbbreviations);
			dateFormate_week = new SimpleDateFormat(dateFormate_week_format, symbols);
		}
		return dateFormate_week;
	}

	private synchronized static DateFormat getDateFormate_week(String template) {
		if (dateFormate_week == null) {
			DateFormatSymbols symbols = new DateFormatSymbols();
			Context context = PalmchatApp.getApplication();
			String[] oddWeekAbbreviations = new String[] { "", context.getString(R.string.sunday), context.getString(R.string.monday), context.getString(R.string.tuesday), context.getString(R.string.wednesday), context.getString(R.string.thursday), context.getString(R.string.friday), context.getString(R.string.saturday) };
			symbols.setShortWeekdays(oddWeekAbbreviations);
			dateFormate_week = new SimpleDateFormat(template, symbols);
		}
		return dateFormate_week;
	}

	private synchronized static DateFormat getDateFormate_month() {
		if (dateFormate_month == null) {
			DateFormatSymbols symbols = new DateFormatSymbols();
			Context context = PalmchatApp.getApplication();
			String[] oddMonthAbbreviations = new String[] { context.getString(R.string.month_jan), context.getString(R.string.month_feb), context.getString(R.string.month_mar), context.getString(R.string.month_apr), context.getString(R.string.month_may), context.getString(R.string.month_june), context.getString(R.string.month_july), context.getString(R.string.month_aug), context.getString(R.string.month_sept), context.getString(R.string.month_oct), context.getString(R.string.month_nov), context.getString(R.string.month_dec) };
			symbols.setShortMonths(oddMonthAbbreviations);
			dateFormate_month = new SimpleDateFormat(dateFormate_month_PATTEN_EN, symbols);
		}
		return dateFormate_month;
	}

	private synchronized static DateFormat getDateFormate_month_day() {
		if (dateFormate_month == null) {
			DateFormatSymbols symbols = new DateFormatSymbols();
			Context context = PalmchatApp.getApplication();
			String[] oddMonthAbbreviations = new String[] { context.getString(R.string.month_jan), context.getString(R.string.month_feb), context.getString(R.string.month_mar), context.getString(R.string.month_apr), context.getString(R.string.month_may), context.getString(R.string.month_june), context.getString(R.string.month_july), context.getString(R.string.month_aug), context.getString(R.string.month_sept), context.getString(R.string.month_oct), context.getString(R.string.month_nov), context.getString(R.string.month_dec) };
			symbols.setShortMonths(oddMonthAbbreviations);
			dateFormate_month = new SimpleDateFormat(dateFormate_month_day, symbols);
		}
		return dateFormate_month;
	}

	public synchronized static DateFormat getDateFormate_month(String template) {
		if (dateFormate_month == null) {
			DateFormatSymbols symbols = new DateFormatSymbols();
			Context context = PalmchatApp.getApplication();
			String[] oddMonthAbbreviations = new String[] { context.getString(R.string.month_jan), context.getString(R.string.month_feb), context.getString(R.string.month_mar), context.getString(R.string.month_apr), context.getString(R.string.month_may), context.getString(R.string.month_june), context.getString(R.string.month_july), context.getString(R.string.month_aug), context.getString(R.string.month_sept), context.getString(R.string.month_oct), context.getString(R.string.month_nov), context.getString(R.string.month_dec) };
			symbols.setShortMonths(oddMonthAbbreviations);
			dateFormate_month = new SimpleDateFormat(template, symbols);
		}
		return dateFormate_month;
	}

	/**
	 * language change(area change area)
	 */
	public static void laguageOrTimeZoneChanged() {
		dateFormate_week = null;
		dateFormate_month = null;
		dateFormate_list = null;
	}

	private static final int HOUR_BY_MIN = 60;
	private static final int DAY_BY_MIN = HOUR_BY_MIN * 24;

	public static String getHomeDisplayTime(Context context, int minute) {

		String strAppend = "";
		int integer;

		// min
		if (minute >= 0 && minute < HOUR_BY_MIN) {
			integer = minute;
			if (integer < 1) {
				return context.getString(R.string.online);
			} else {
				strAppend = context.getString(R.string.minute_ago);
				if (strAppend.contains("(s)")) {
					if (integer == 1) {
						strAppend = strAppend.replace("(s)", "");
					} else if (integer > 1) {
						strAppend = strAppend.replace(" (", "");
						strAppend = strAppend.replace(")", "");
					}
				}

			}
			// hour
		} else if (minute >= HOUR_BY_MIN && minute < DAY_BY_MIN) {
			integer = (int) (minute / HOUR_BY_MIN);
			if (strAppend.contains("(s)")) {
				if (integer == 1) {
					strAppend = context.getString(R.string.hour_ago);
					strAppend = strAppend.replace("(s)", "");
				} else if (integer > 1) {
					strAppend = context.getString(R.string.hour_ago);
					strAppend = strAppend.replace(" (", "");
					strAppend = strAppend.replace(")", "");
				}
			}

			// day
		} else {
			integer = (int) (minute / DAY_BY_MIN);

			if (integer < 1) {
				return context.getString(R.string.online);
			} else {
				strAppend = context.getString(R.string.day_ago);
				if (strAppend.contains("(s)")) {
					if (integer == 1) {
						strAppend = strAppend.replace("(s)", "");
					} else if (integer > 1) {
						strAppend = strAppend.replace(" (", "");
						strAppend = strAppend.replace(")", "");
					}
				}
			}

		}
		strAppend = strAppend.replace(ReplaceConstant.TARGET_FOR_REPLACE, integer + "").toString();
		// return new
		// StringBuffer(String.valueOf(integer)).append(" ").append(strAppend).toString();
		return strAppend;

	}

	/** 格式化时间
	 * @param milliseconds
	 * @param pattern
	 * @return
	 */
	public static String getFormatDateTime(long milliseconds,String pattern) {
		Date date = new Date(milliseconds);
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		String str = sf.format(date);
		return str;
	}

	public static Long getHours(long time) {
		long diff = System.currentTimeMillis() - time;// 这样得到的差值是微秒级别
		long days = diff / (1000 * 60 * 60 * 24);
		long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		return hours;
	}

	public static String getTimeAgo(Context c, String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		try{
			Date date = getDateTime(time);
//			PalmchatLogUtils.e("time", time);
			long delta = System.currentTimeMillis() - date.getTime();
//			PalmchatLogUtils.e("getTimeAgo", System.currentTimeMillis() + "|" + time);
	
			if (delta < 1L * ONE_MINUTE) {
				return c.getString(JUST_NOW);
				/*long seconds = toSeconds(delta);
				if (seconds <= 1) {
					return c.getString(ONE_SECOND_AGO_NOT_S).replace("XXXX", 1 + "");
				}
				return c.getString(ONE_SECOND_AGO).replace("XXXX", (seconds <= 0 ? 1 : seconds) + "");*/
			}
			if (delta < 60L * ONE_MINUTE) {
				long minutes = toMinutes(delta);
				/*if (minutes <= 1) {
					return c.getString(ONE_MINUTE_AGO_NOT_S).replace("XXXX", 1 + "");
				}*/
				return c.getString(ONE_MINUTE_AGO).replace(ReplaceConstant.TARGET_FOR_REPLACE, (minutes <= 0 ? 1 : minutes) + "");
			}
			if (delta < 24L * ONE_HOUR) {
				long hours = toHours(delta);
				/*if (hours <= 1) {
					return c.getString(ONE_HOUR_AGO_NOT_S).replace("XXXX", 1 + "");
				}*/
				return c.getString(ONE_HOUR_AGO).replace(ReplaceConstant.TARGET_FOR_REPLACE, (hours <= 0 ? 1 : hours) + "");
	
			}
			 
			/*String strGMT=date.toString();
			int _indexGMT=strGMT.indexOf("+"); 
			if(_indexGMT<0){
				_indexGMT=strGMT.indexOf("GMT");
			}
			if(_indexGMT>0){
				strGMT=strGMT.substring( 0,_indexGMT);
				_indexGMT=strGMT.lastIndexOf( ":");
				if(_indexGMT>0){//去掉秒
					strGMT=strGMT.substring( 0,_indexGMT);
				}
			}
			   return strGMT ;*/
			int _index =time.lastIndexOf(":");
			if(_index>0){//去掉秒
				time=time.substring( 0,_index);
			}
			 return time ; 
			/*if (delta < 48L * ONE_HOUR) {
				return c.getString(R.string.yesterday);
			}
			if (delta < 30L * ONE_DAY) {
				long days = toDays(delta);
				if (days <= 1) {
					return c.getString(ONE_DAY_AGO_NOT_S).replace("XXXX", 1 + "");
				}
				return c.getString(ONE_DAY_AGO).replace("XXXX", (days <= 0 ? 1 : days) + "");
			}
			if (delta < 12L * 4L * ONE_WEEK) {
				long months = toMonths(delta);
				if (months <= 1) {
					return c.getString(ONE_MONTH_AGO_NOT_S).replace("XXXX", 1 + "");
				}
				return c.getString(ONE_MONTH_AGO).replace("XXXX", (months <= 0 ? 1 : months) + "");
			} else {
				long years = toYears(delta);
				if (years <= 1) {
					return c.getString(ONE_YEAR_AGO_NOT_S).replace("XXXX", 1 + "");
				}
				return c.getString(ONE_YEAR_AGO).replace("XXXX", (years <= 0 ? 1 : years) + "");
			}*/
		}catch(Exception e){
			return time;
		}
	}

	/**
	 * add by zhh 获取当前日期往后推6年
	 * 
	 * @return
	 */
	public static String getDateSixYearAgo() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		year = year - 6;
		c.set(Calendar.YEAR, year);
		SimpleDateFormat mSimpleFormat = new SimpleDateFormat("dd-MM-yyyy");
		String str = mSimpleFormat.format(c.getTime());
		return str;

	}

	private static long toSeconds(long date) {
		return date / 1000L;
	}

	private static long toMinutes(long date) {
		return toSeconds(date) / 60L;
	}

	private static long toHours(long date) {
		return toMinutes(date) / 60L;
	}

	private static long toDays(long date) {
		return toHours(date) / 24L;
	}

	private static long toMonths(long date) {
		return toDays(date) / 30L;
	}

	private static long toYears(long date) {
		return toMonths(date) / 365L;
	}

	public static Date getDateTime(String currentTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = formatter.parse(currentTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getTimeMonth(Context mContext, String time) {
		String str = time;
		String sr = str.substring(3, 5);
		String aop = "";
		String times = "";
		if ("01".equals(sr)) {
			aop = "-" + mContext.getString(R.string.month_jan);
			times = time.replace("-01", aop);
			return times;
		} else if ("02".equals(sr)) {
			aop = "-" + mContext.getString(R.string.month_feb);
			times = time.replace("-02", aop);
			return times;
		} else if ("03".equals(sr)) {
			aop = "-" + mContext.getString(R.string.month_mar);
			times = time.replace("-03", aop);
			return times;
		} else if ("04".equals(sr)) {
			aop = "-" + mContext.getString(R.string.month_apr);
			times = time.replace("-04", aop);
			return times;
		} else if ("05".equals(sr)) {
			aop = "-" + mContext.getString(R.string.month_may);
			times = time.replace("-05", aop);
			return times;
		} else if ("06".equals(sr)) {
			aop = "-" + mContext.getString(R.string.month_june);
			times = time.replace("-06", aop);
			return times;
		} else if ("07".equals(sr)) {
			aop = "-" + mContext.getString(R.string.month_july);
			times = time.replace("-07", aop);
			return times;
		} else if ("08".equals(sr)) {
			aop = "-" + mContext.getString(R.string.month_aug);
			times = time.replace("-08", aop);
			return times;
		} else if ("09".equals(sr)) {
			aop = "-" + mContext.getString(R.string.month_sept);
			times = time.replace("-09", aop);
			return times;
		} else if ("10".equals(sr)) {
			aop = "-" + mContext.getString(R.string.month_oct);
			times = time.replace("-10", aop);
			return times;
		} else if ("11".equals(sr)) {
			aop = "-" + mContext.getString(R.string.month_nov);
			times = time.replace("-11", aop);
			return times;
		} else if ("12".equals(sr)) {
			aop = "-" + mContext.getString(R.string.month_dec);
			times = time.replace("-12", aop);
			return times;
		}
		return null;
	}

	/**
	 * 获取比赛或竞猜结束时间 //Sat 8 Aug 08:56改为 8 Aug 08:56 / 18/8/2015 7:50-
	 * 
	 * @param mContext
	 * @param milliseconds
	 *            毫秒
	 * @return
	 */
	public static String getPredictEndTime(Context mContext, long milliseconds) {
		Date date = new Date(milliseconds);
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String str = sf.format(date);
		return str;
//		Date date = new Date(milliseconds);
//		String str = getDateFormate_month("dd MMM HH:mm").format(date);
//		String[] weekDays = new String[] { mContext.getString(R.string.sunday), mContext.getString(R.string.monday), mContext.getString(R.string.tuesday), mContext.getString(R.string.wednesday), mContext.getString(R.string.thursday), mContext.getString(R.string.friday), mContext.getString(R.string.saturday) };
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
//		if (w < 0)
//			w = 0;
//		String str2 = weekDays[w];
//		String result = str2 + " " + str;
//		return result;

	}
	
	/**
	 * zhh
	 * 
	 * @param currentTime
	 * @return
	 */
	public static String getYyyyMmDdHhMmSs(String currentTime) {
		long time = Long.parseLong(currentTime);
		Date date = new Date(time);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = "";
		dateStr = formatter.format(date);

		return dateStr;
	}

	/**
	 * 显示时间(xxHxxm)和日期（xxMxxD）
	 * @param context
	 * @param when
     * @return
     */
	public static String formatTimeStampString(Context context, long when) {
		Time then = new Time();
		then.set(when);
		Time now = new Time();
		now.setToNow();
		String date = "";
		// Basic settings for formatDateTime() we want for all cases.
		int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT |
				DateUtils.FORMAT_ABBREV_ALL |
				DateUtils.FORMAT_CAP_AMPM;

		// If the message is from a different year, show the date and year.
		if (then.year != now.year) {
			format_flags |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;
			date = getDateToStringYear(when);
		} else if (then.yearDay != now.yearDay) {
			// If it is from a different day than today, show only the date.
			format_flags |= DateUtils.FORMAT_SHOW_DATE;
			date = getDateToStringDay(when);
		} else {
			// Otherwise, if the message is from today, show the time.
			format_flags |= DateUtils.FORMAT_SHOW_TIME;
			date = DateUtils.formatDateTime(context, when, format_flags);
		}
		return date;
	}

	/**
	 * 显示时间(xxHxxm)和日期（xxMxxD）
	 * @param context
	 * @param when
	 * @return
	 */
	public static String formatTimeStampForString(Context context, long when) {
		Date then = new Date(when);
		Calendar cal = Calendar.getInstance();
		int yearNow = cal.get(Calendar.YEAR);
		long dayNow = cal.get(Calendar.DAY_OF_YEAR);

		cal.setTime(then);
		int yearThen = cal.get(Calendar.YEAR);
		long dayThen = cal.get(Calendar.DAY_OF_YEAR);


		String date = "";
		// Basic settings for formatDateTime() we want for all cases.
		int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT |
				DateUtils.FORMAT_ABBREV_ALL |
				DateUtils.FORMAT_CAP_AMPM;

		// If the message is from a different year, show the date and year.
		if (yearThen != yearNow) {
			date = getDateToStringYear(when);
		} else if (dayNow != dayThen) {
			// If it is from a different day than today, show only the date.
			date = getDateToStringDay(when);
		} else {
			// Otherwise, if the message is from today, show the time.
			Date dateThen = new Date(when);
			date =  getDateFormate_list().format(dateThen);
		}
		return date;
	}

	public static String getDateToStringDay(long time) {
		Date d = new Date(time);
		SimpleDateFormat sf = new SimpleDateFormat("MM/dd");
		return sf.format(d);
	}

	public static String getDateToStringYear(long time) {
		Date d = new Date(time);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
		return sf.format(d);
	}





}
