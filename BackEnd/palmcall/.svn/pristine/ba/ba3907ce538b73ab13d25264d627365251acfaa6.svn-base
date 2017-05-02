package com.afmobi.palmcall.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	//将本地时间 转为0时区时间返回
	public static Calendar localTimeToZeroTimeZone() {
		//1 取得本地时间:
		Calendar calendar = Calendar.getInstance();
		//2 取得时间偏移量：
		int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
		//3 取得夏令时差：
		int dstOffset = calendar.get(Calendar.DST_OFFSET);
		//4 从本地时间里扣除这些差量，即可以取得UTC时间：
		calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

		return  calendar;
	}

	//将时间 转为0时区时间返回
	public static Calendar changeToZeroTimeZone(Calendar calendar) {
		int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
		int dstOffset = calendar.get(Calendar.DST_OFFSET);
		calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

		return  calendar;
	}

	public static void main(String[] args) {
		Calendar calendar = localTimeToZeroTimeZone();
		//之后调用calendar.get(int x)或
		//calendar.getTimeInMillis()方法所取得的时间即是UTC标准时间。
		System.out.println("UTC:" + new Date(calendar.getTimeInMillis()));
		System.out.println("hour:" + calendar.get(Calendar.HOUR_OF_DAY));

		Calendar calendar2 = changeToZeroTimeZone(Calendar.getInstance());
		System.out.println("hour:" + calendar2.get(Calendar.HOUR));
	}
	
}
