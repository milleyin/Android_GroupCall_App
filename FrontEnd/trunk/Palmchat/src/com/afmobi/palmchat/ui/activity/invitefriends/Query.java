package com.afmobi.palmchat.ui.activity.invitefriends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.core.AfFriendInfo;

public class Query extends AsyncQueryHandler {
	
	String[] mProjectionPhone = {
			Phone.DISPLAY_NAME,
			Phone.NUMBER,
	        Phone.TYPE};

	protected final String ORDER_BY = "sort_key COLLATE LOCALIZED asc";
	
	public static final int QUERY_PHONE_TOKEN = 0;
//	public static final int SEARCH_RESULT_TOKEN = 1;
	
	public static final int QUERY_SIM_TOKEN = 2;
	
	private static final Uri SIM_URI = Uri.parse("content://icc/adn");
	
	
	protected Context mContext;
	private OnQueryComplete queryComplete;
	private String phoneNum;
	private String name;
	private String type;
	HashMap<String, AfFriendInfo> mContactMap = new HashMap<String, AfFriendInfo>();

	public Query(ContentResolver cr, Context mContext) {
		super(cr);
		this.mContext = mContext;
	}
	
	public void setQueryComplete(OnQueryComplete queryComplete) {
		this.queryComplete = queryComplete;
	}
	
	/**
	 * query all phone contacts
	 * 
	 */
	public void query() {
		
		startQuery(QUERY_PHONE_TOKEN, null,
				Phone.CONTENT_URI, mProjectionPhone,
				null, null, ORDER_BY);
		
		
		startQuery(QUERY_SIM_TOKEN, null,
				SIM_URI, null,
				null, null, ORDER_BY);
	}

	
	public void setContacts(Context context, Cursor c,int token) {
		if(c != null){
			while (c.moveToNext()) {

				if (token == QUERY_PHONE_TOKEN) {
					phoneNum = c.getString(1);
					type = c.getString(2);
					PalmchatLogUtils.i("hj","phone:"+phoneNum);
				} else {
					phoneNum = c.getString(c.getColumnIndex("number"));
				}
				if(!TextUtils.isEmpty(type) && !type.equals("2"))
					continue;
				if (!TextUtils.isEmpty(phoneNum)) {
					AfFriendInfo contact = new AfFriendInfo();
					contact.isAddFriend = true;
					if (token == QUERY_PHONE_TOKEN) {
						name = c.getString(0);
						PalmchatLogUtils.i("hj","name:"+name);
					} else {
						name = c.getString(c.getColumnIndex("name"));
					}
					if (name == null) {
						name = "";
					}

					contact.name = name;
					contact.user_msisdn = phoneNum.replaceAll("-", "").replace(" ", "");
					mContactMap.put(phoneNum, contact);
				}
			}
			c.close();
		}
	}
	
	public HashMap<String, AfFriendInfo> getContacts() {
		return mContactMap;
	}
	
	
	/**
	 * get fuzzy search result
	 * @param c
	 * @return
	 */
	  public List<AfFriendInfo> getSearchUser(Cursor c) {
		  
		  List<AfFriendInfo> list = new ArrayList<AfFriendInfo>();
		  
		  if(c != null){
				
			  while (c.moveToNext()) {
			
				String number = c.getString(c.getColumnIndex(Phone.NUMBER));
				String name = c.getString(c.getColumnIndex(Phone.DISPLAY_NAME));
//				String photo_id = cur.getString(cur.getColumnIndex(Phone.PHOTO_ID));
//				String sort_key = cur.getString(cur.getColumnIndex("sort_key"));
				
				AfFriendInfo cont = new AfFriendInfo();
				cont.name = name;
				cont.user_msisdn = number;

				    list.add(cont);
				    
			}
			
			 c.close();
			 
			}
		  
		  return list;
		}
	
	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
		
			if (queryComplete != null) {
				queryComplete.onComplete(token, cursor);
		}
	}
	
	public interface OnQueryComplete {
		void onComplete(int token, Cursor c);
	}

}
