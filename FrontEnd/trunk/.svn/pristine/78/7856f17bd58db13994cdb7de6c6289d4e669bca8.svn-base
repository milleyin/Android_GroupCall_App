package com.afmobi.palmchat.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import java.util.Locale;

import com.afmobi.palmchat.log.PalmchatLogUtils;

public final class LanguageUtil
{
	public static final String LANGUAGE_CODE = "language_code";
	
	public static final String LANGUAGE_DEFAULT = "default";
	public static final String LANGUAGE_ZH_TW = "zh_TW";
	public static final String LANGUAGE_ZH_HK = "zh_HK";
	public static final String LANGUAGE_ZH_CN = "zh_CN";
	public static final String LANGUAGE_EN = "en";
	
	public static String[] LANGUAGE_TYPE = new String[]
	{
		"default",
		"zh_CN",
		"en",
		"es",
		"pt",
		"ar",
		"ha",
		"fr",
		"sw",
		"am",
		"fa"//fa_IR
		,"hi"//印地
			,"ur"//wu二度
			,"th"//泰语
	};
	
    public static String getCode(String code)
    {
        if(code.length() <= 0 || code.equals(LANGUAGE_DEFAULT))
        {
            code = getDefaultCode();
        }
        
        return code;
    }

    public static Locale getLocale(String code)
    {
		Locale locale = null;
		
		if (code.equals(LANGUAGE_EN))
		{
			locale = Locale.ENGLISH;
		}
		else if (code.equals(LANGUAGE_ZH_TW) || code.equals(LANGUAGE_ZH_HK))
		{
			locale = Locale.TAIWAN;
		}
		else if (code.equals(LANGUAGE_ZH_CN))
		{
			locale = Locale.CHINA;
		}
		
		if (null == locale)
		{
			int nCount = LANGUAGE_TYPE.length;
			int nIndex = 0;
			while (nIndex < nCount)
			{
				if (code.equals(LANGUAGE_TYPE[nIndex]))
				{
					locale = new Locale(code);
					break;
				}
				
				nIndex ++;
			}
		}
		
		if (null == locale)
		{
			locale = Locale.ENGLISH;
		}

		return locale;
	}
    
    private static void update(Context ctt, Locale locale)
    {
        Resources res = ctt.getResources();
        Configuration cfg = res.getConfiguration();
        
        PalmchatLogUtils.println("LanguageUtil src: " + cfg.locale.getLanguage() + "dest: " + locale.getLanguage());
        
        if(!cfg.locale.equals(locale))
        {
            android.util.DisplayMetrics dm = res.getDisplayMetrics();
            cfg.locale = locale;
            res.updateConfiguration(cfg, dm);
            Resources.getSystem().updateConfiguration(cfg, dm);
        }
    }
    
	public static void updateLanguage(Context ctt, String languageCode)
	{
		String lc = LanguageUtil.getCode(languageCode);
		Locale locale = LanguageUtil.getLocale(lc);
		update(ctt, locale);
	}
	
//	public static void restartApp(Context ctt)
//	{
//		// reset time for mainfragment 
//		DateUtil.laguageOrTimeZoneChanged();
//		
//		Intent i = ctt.getPackageManager().getLaunchIntentForPackage(ctt.getPackageName());  
//		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
//		ctt.startActivity(i);
//	}
	
	/**
	 * ���ù㲥�Ƚ������� ���ױ����أ�ֱ�ӵ���
	 * @param ctt
	 */
	public static void restartApp(Context ctt)
	{
		// reset time for mainfragment 
		DateUtil.laguageOrTimeZoneChanged();
		
		Intent i = new Intent(ctt, com.afmobi.palmchat.ui.activity.LaunchActivity.class);  
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		ctt.startActivity(i);
	}

    private static String getDefaultCode()
    {
    	String code = LANGUAGE_EN;
    	
		String sDefaultCode = Locale.getDefault().getLanguage().trim();
		String sCodeCountry = sDefaultCode + "_" + Locale.getDefault().getCountry().trim();
		
		if (sCodeCountry.equals(LANGUAGE_ZH_TW) || sCodeCountry.equals(LANGUAGE_ZH_HK))
		{
			code = LANGUAGE_ZH_TW;
		}
		else if (sCodeCountry.equals(LANGUAGE_ZH_CN))
		{
			code = LANGUAGE_ZH_CN;
		}
		else
		{
			code = sDefaultCode;
		}

		return code;
	}
}
