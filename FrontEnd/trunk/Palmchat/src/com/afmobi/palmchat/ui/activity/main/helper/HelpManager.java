package com.afmobi.palmchat.ui.activity.main.helper;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.ui.activity.friends.FriendsActivity;

public class HelpManager {

	private Context context;
	
	private static HelpManager helpManager=null;
	public HelpManager(Context context){
		this.context=context;
	}
	
	
	public static HelpManager getInstance(Context context){
		if(null==helpManager){
			helpManager= new HelpManager(context);
		}
		
		return  helpManager;
	}
	
    public void createGroup()
    {
       
            Bundle b = new Bundle();
            ArrayList<String> existList = new ArrayList<String>();
            existList.add(DefaultValueConstant.PALMCHAT_ID);
            b.putStringArrayList("palmchat_id", existList);
            b.putInt("owner_num", existList.size());
            b.putString("come_page", "first_compepage");
            jumpToPage(FriendsActivity.class, b, true, 0, false);           
    }
    
    public void jumpToPage(Class<?> cls, Bundle bundle, boolean isReturn, int requestCode, boolean isFinish)
    {
        if (cls == null)
        {
            return;
        }

        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(context, cls);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }

        if (isReturn)
        {
            ((Activity) context).startActivityForResult(intent, requestCode);
        }
        else
        {
    
            try
            {
                context.startActivity(intent);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        
        }

        if (isFinish)
        {
        	((Activity) context).finish();
        }
    }
}
