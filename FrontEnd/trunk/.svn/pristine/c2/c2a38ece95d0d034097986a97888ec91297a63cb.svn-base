
package com.afmobi.palmchat.ui.activity.setting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;

import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;

public class Query extends AsyncQueryHandler {
	String[] projection = { ContactsContract.Contacts._ID,
			Phone.DISPLAY_NAME,
			Phone.NUMBER, "sort_key" };
	protected final String ORDER_BY = "sort_key COLLATE LOCALIZED asc";
	public static final int QUERY_TOKEN = 0;
	public static final int INSERT_TOKEN = 1;
	public static final int UPDATE_TOKEN = 2;
	public static final int DELETE_TOKEN = 3;
	private OnQueryComplete queryComplete;
	protected Context mContext;

	public OnQueryComplete getQueryComplete() {
		return queryComplete;
	}

	public void setQueryComplete(OnQueryComplete queryComplete) {
		this.queryComplete = queryComplete;
	}

	public Query(ContentResolver cr, Context mContext) {
		super(cr);
		this.mContext = mContext;
	}

	public void query() {
		startQuery(QUERY_TOKEN, null,
				Phone.CONTENT_URI, projection,
				null, null, ORDER_BY);
	}
	
	
	public List<Contacts> getContacts(Context context  ,Cursor c) {
		List<Contacts> list = new ArrayList<Contacts>();
		if(c != null){
			while (c.moveToNext()) {
				if( TextUtils.isEmpty(c.getString(2)) ){
					continue;
				}
				Contacts ctt = new Contacts();
				ctt.setId(Integer.parseInt(c.getString(0)));
				ctt.setName(c.getString(1));
		       if(c.getString(2)!=null||"".equals(c.getString(2))){
				ctt.setNumber(c.getString(2).replaceAll("-", "").replace(" ", ""));
				}
				list.add(ctt);
			}
			c.close();
		}
		return list;
	}
	
	public void saveOrUpdate(final List<Contacts> newList, final List<Contacts> oldList, final Handler handler) {
		new Thread() { 
		    public void run() { 
		    	Collection<Contacts> cccc = addContacts(newList, oldList);
				int k = 0;
				Iterator<Contacts> contactsIterator = cccc.iterator();
				while(contactsIterator.hasNext()){
					Contacts c =  contactsIterator.next();
					if (insert(c)) {
						k++;
					}
				}
				handler.obtainMessage(2).sendToTarget();
		   };
		}.start();
		
//		return k;
	}
	
	private Collection<Contacts> addContacts(List<Contacts> list, List<Contacts> list2) {
		HashMap<String,Contacts> contactses = new HashMap<>();
		if (list2 == null || list2.size() == 0) {
			for (Contacts contact : list) {
				addContact(contactses,contact);
			}
		}
		for (Contacts contacts : list) {
			Contacts c = filter(list2, contacts);
			if (c != null) {
				addContact(contactses,c);
			}
			
		}
		return contactses.values();
	}

	private void addContact(HashMap<String,Contacts> contactsHashMap,Contacts contact){
		Contacts contactsTemp;
		if (contactsHashMap.containsKey(contact.name)) {
			contactsTemp = contactsHashMap.get(contact.name);
			contactsTemp.setNumbers(contact.number);
		} else {
			contact.setNumbers(contact.getNumber());
			contactsHashMap.put(contact.name,contact);
		}
	}
	
	public Contacts filter(List<Contacts> list, Contacts c) {
		boolean flag = false;
		for (int i = 0; i < list.size() ; i++) {
			Contacts cc = list.get(i);
			if (cc.getNumber().equals(c.getNumber())
					&& cc.getName().equals(c.getName())) {
				flag = true;
				continue;
			}
			
			if (i == list.size() - 1) {
				if (flag) {
					return null;
				} else {
					return c;
				}
			}
			
		}
		return null;
	}
	
	private boolean update(Contacts c ,int id) {
		String selection = Data._ID + "=?";
		Cursor cursor = mContext.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data.RAW_CONTACT_ID}, 
				selection, new String[]{String.valueOf(id)}, null);
		int raw_contacts_id = 0;
		if (cursor != null && cursor.moveToNext()) {
			raw_contacts_id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
		}
		if (raw_contacts_id <= 0) {
			return false;
		}
		ContentValues values = new ContentValues();
		values.put(StructuredName.DISPLAY_NAME, c.getName());
		this.mContext.getContentResolver().update(android.provider.ContactsContract.Data.CONTENT_URI, values, " raw_contact_id=? and mimetype=?", new String[]{String.valueOf(raw_contacts_id),StructuredName.CONTENT_ITEM_TYPE});
		values.clear();

		values.put(Phone.NUMBER, c.getNumber());
		this.mContext.getContentResolver().update(android.provider.ContactsContract.Data.CONTENT_URI, values, " raw_contact_id=? and mimetype=? and data2=?", new String[]{String.valueOf(raw_contacts_id),Phone.CONTENT_ITEM_TYPE,"2"});
		values.clear();
		return true;
	}

	private boolean insert(Contacts c) {
		ContentValues values = new ContentValues();
		// ������RawContacts.CONTENT_URIִ��һ����ֵ���룬Ŀ���ǻ�ȡϵͳ���ص�rawContactId
		Uri rawContactUri = mContext.getContentResolver().insert(
				RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);

		// ��data�����������
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		values.put(StructuredName.DISPLAY_NAME, c.getName());
		mContext.getContentResolver().insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);
		// ��data����绰���
		Uri uri = null;
		if(c.getNumbers() != null && c.getNumbers().size() >0) {
			for (String str : c.getNumbers()) {
				values.clear();
				values.put(
						android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
						rawContactId);
				values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
				values.put(Phone.NUMBER, getRealNumber(str));
				values.put(Phone.TYPE, Phone.TYPE_MOBILE);
				uri = mContext.getContentResolver().insert(
						android.provider.ContactsContract.Data.CONTENT_URI, values);
			}
		}
		if (uri != null) {
			return true;
		}
		return false;
	}
	public static String getRealNumber(String number) {
		if (number != null) {
			if (number.startsWith(RequestConstant.dcc)) {
				return number.substring(2);
			} else if (number.startsWith("+" + RequestConstant.dcc)) {
				return number.substring(3);
			}
		}
		return number;
	}
	
	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
		if (token == QUERY_TOKEN) {
			PalmchatLogUtils.e("Wxl","complete : " + queryComplete);
			if (queryComplete != null) {
				queryComplete.onComplete(cursor);
			}
		}
	}
	

	public interface OnQueryComplete {
		void onComplete(Cursor c);
	}

}
