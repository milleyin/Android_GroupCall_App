package com.afmobi.palmchat.util;

import com.afmobi.palmchat.log.PalmchatLogUtils;

import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author HJG
 * @date 2015-12-21 下午6:20:35
 * @modifyAuthor: 
 * @modifyDate: 2015-12-21 下午6:20:35
 * @modifyContent:
 */
public class KeyDownUtil {

	/**
	 * jump to  system MainUI .
	 * @param context
	 */
	public static void actionMain(Context context, Integer...integers){
		
		Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
       //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(integers!=null && integers.length>0){
        	for(Integer flag : integers){
        		intent.setFlags(flag);
        	}
        }
        context.startActivity(intent);
//        SharePreferenceUtils.getInstance(context).setRunningBackground(true);
//        PalmchatLogUtils.e("HJG", "KeyDownUtil actionMain()-> "+SharePreferenceUtils.getInstance(context).isRunningBackground(false));
        
	}
}
