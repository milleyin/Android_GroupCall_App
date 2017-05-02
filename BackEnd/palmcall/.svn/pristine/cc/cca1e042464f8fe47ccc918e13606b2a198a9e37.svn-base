package com.afmobi.palmcall.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BirthdayToAgeUtil {

	/**
	 * 根据用户生日计算出年龄
	 */
	public static int getAgeByBirthday(String birthday) {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) {
			return 24;
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date birthdayDate = sdf.parse(birthday);

			cal.setTime(birthdayDate);
			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

			int age = yearNow - yearBirth;

			if (monthNow <= monthBirth) {
				if (monthNow == monthBirth) {
					if (dayOfMonthNow < dayOfMonthBirth) {
						age--;
					}
				} else {
					age--;
				}
			}

			return age;
		} catch (ParseException e) {
			return 24;
		}

	}

}
