package com.core.contact;


import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afmobi.palmchat.PalmchatApp;
//import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.core.AfPalmchat;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

public class ContactAPI
{
	public static HashMap<String, ContactInfo> getAllContact() {
		Context app = AfPalmchat.getContext();		
		if( null == app){
			return null;
		}
		ContentResolver resolver = app.getContentResolver();
		Cursor cur = null;
		String name, id, phone;
		ContactInfo info;
		
		HashMap<String, ContactInfo> map = null;
		try{
			int size = 0;
			
			cur = resolver.query(ContactsContract.Contacts.CONTENT_URI, new String[] { "_id",  ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);
			if( cur == null || (size = cur.getCount()) <= 0){
				return null;
			}
			map = new HashMap<String, ContactInfo>((size*3)>>1);
			if (cur != null && cur.moveToFirst()){
				do{
					
					name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					id = cur.getString(cur.getColumnIndex("_id"));
					
					if( !TextUtils.isEmpty(name) && !TextUtils.isEmpty(id) ){
						info = new ContactInfo();
						info.set(name, false);
						map.put(id, info);
					}
				}while (cur.moveToNext());
			}
		} finally {
			if(cur != null) {
				cur.close();
				cur = null;
			}
			if( null == map){
				return null;
			}
		}	
		
		try{
			cur = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {ContactsContract.Data.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DATA1}, null , null, null);
			if (cur != null && cur.moveToFirst()){
				String regEx="[^0-9]";   				
				Pattern p = Pattern.compile(regEx);   
			
				do{
					id = cur.getString(cur.getColumnIndex(ContactsContract.Data.CONTACT_ID));	
					phone = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1));						
					info = map.get(id);
					if(info != null && !TextUtils.isEmpty(phone)) {	
						Matcher m = p.matcher(phone);  	
						phone = m.replaceAll("").trim();
						
						if( !TextUtils.isEmpty(phone) ){
							info.set(phone, true);
						//	PalmchatLogUtils.e("Contact", "phone = " + info.mPhone + "name = " + info.mName);							
						}
					}					
				}
				while (cur.moveToNext());
			}
		}finally{
			if (cur != null)
			{
				cur.close();
				cur = null;
			}
		}		
		return map;
	}
}