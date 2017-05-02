package com.afmobi.palmchat.network.dataparse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.social.BroadcastNotificationData;
import com.core.AfData;
import com.core.AfData.BcNotify;
import com.core.AfFriendInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.cache.CacheManager.Country;
import com.core.cache.CacheManager.Region;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

public class CommonJson {
	
	public static CommonJson instance;
	private CommonJson(){
		
	}
	
	public static CommonJson getInstance(){
		if(instance ==  null){
			return new CommonJson();
		}
		return instance;
	}


	
	/**
	 * parse broadcast FriendCircle notification json data.
	 * 5.1 add Friends Circle notification 版本新增朋友圈 通知功能
	 */
	public ArrayList< String > parseBrdFriendCircleNotiJson(String json ) {
	
		ArrayList< String > dataMidList=new ArrayList< String >();
		try {
			JSONObject jsonObject = new JSONObject(json); 
			 
			if (!jsonObject.isNull("new")) {//5.1 add Friends Circle notification 版本新增朋友圈 通知功能
				JSONArray comArray = jsonObject.getJSONArray("new");
				for (int i = 0; i < comArray.length(); i++) {
					JSONObject item = (JSONObject) comArray.get(i);
//					BroadcastNotificationData data = new BroadcastNotificationData();
//					data.type = BroadcastNotificationData.ITEM_TYPE_FRIENS_NEW;
//					data.afid = item.getString("afid");
//					data.mid = item.getString("mid");
//					data.ts = item.getLong("ts");  
			     	String mid=item.getString("mid");
			     	dataMidList.add(mid);
//			     	if(CacheManager.getInstance().getBroadcastFriendCircleList()!=null){//如果为空，说明还没有朋友圈mid列表，那就在进朋友圈的时候自动去刷新就行了，这里就不用加了
//			     		CacheManager.getInstance().addBroadcastFriendCircleList(mid);//getBroadcastFriendCircleList().midList.add(mid);
//			     	}2015/08/04后该不用了，因为已经改为Follow的人可以在朋友圈里看到历史的广播，这个列表就无法用了
				}
			}  
			return dataMidList; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			PalmchatLogUtils.println("CommonJson parseBrdNotiJson " + json);
			return null;
		}
		
	}
	
	/**
	 * parse broadcast notification json data.
	 * 
	 * return datas [0] like,  [1] com, null if Exception
	 * 
	 */
	public ArrayList<HashMap<String, List<BroadcastNotificationData>>> parseBrdNotiJson(String json, boolean save2Db) {
	
		ArrayList<HashMap<String, List<BroadcastNotificationData>>> datas = new ArrayList<HashMap<String,List<BroadcastNotificationData>>>();
		HashMap<String, List<BroadcastNotificationData>> mapLike = new HashMap<String, List<BroadcastNotificationData>>();
		HashMap<String, List<BroadcastNotificationData>> mapCom = new HashMap<String, List<BroadcastNotificationData>>();
//		HashMap<String, List<BroadcastNotificationData>> mapFriendsNew = new HashMap<String, List<BroadcastNotificationData>>();
		
		try {
			JSONObject jsonObject = new JSONObject(json);
			
			if (!jsonObject.isNull("like")) {
				JSONArray likeArray = jsonObject.getJSONArray("like");
				for (int i = 0; i < likeArray.length(); i++) {
					JSONObject item = (JSONObject) likeArray.get(i);
					BroadcastNotificationData data = new BroadcastNotificationData();
					data.type = BroadcastNotificationData.ITEM_TYPE_LIKE;
					data.afid = item.getString("afid");
					data.mid = item.getString("mid");
					data.ts = item.getLong("ts");
					data.user_class = item.getInt("class");

					String msg = null;
					if(item.has("msg")) {
						msg = item.getString("msg");
						if (!TextUtils.isEmpty(msg)) {
							data.msg = new String(Base64.decode(msg, Base64.DEFAULT));
						} else {
							data.msg = "";
						}
					}

					String strSex = item.getString("sex");
					byte sex = 0;
					if (AfFriendInfo.MALE.equals(strSex)) {
						sex = Consts.AFMOBI_SEX_MALE;
					} else if (AfFriendInfo.FEMALE.equals(strSex)) {
						sex = Consts.AFMOBI_SEX_FEMALE;
					}
					data.sex = sex;
					
					data.age = item.getInt("age");
					data.name = item.getString("name");
					data.sign = item.getString("sign");
					data.region = item.getString("region");
					data.head_img_path = item.getString("local_img_path");
					try{//added Ver5.1  5.1以后版本才会有以下4个字段，
						data.original_type=(byte) item.getInt("type");

						String _content =	 item.getString("content");
						if (!TextUtils.isEmpty(_content)) {
							data.original_content = new String(Base64.decode(_content, Base64.DEFAULT));
						}else {
							data.original_content = "";
						}

						String safid = null;
						if(item.has("safid")) {
							safid = item.getString("safid");
							if (!TextUtils.isEmpty(safid)) {
								data.at_safid = safid;
							} else {
								data.at_safid = "";
							}
						}
						String sname = null;
						if(item.has("sname")) {
							sname = item.getString("sname");
							if (!TextUtils.isEmpty(sname)) {
								data.at_sname = sname;
							} else {
								data.at_sname = "";
							}
						}

					}catch(Exception e){
					}

					if (save2Db) {
						PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCNotifyInsert(CacheManager.getInstance().getMyProfile().afId,
								data.afid, data.mid,
								BroadcastNotificationData.ITEM_TYPE_LIKE,
								BroadcastNotificationData.STATUS_UNREAD,
								data.ts, data.msg, data.name, data.age,
								data.sex, data.sign, data.region, data.head_img_path,"","",data.original_content,data.original_type);
					}
					
					
					if (mapLike.containsKey(data.mid)) {
						mapLike.get(data.mid).add(data);
					} else {
						ArrayList<BroadcastNotificationData> list = new ArrayList<BroadcastNotificationData>();
						list.add(data);
						mapLike.put(data.mid, list);
					}
							
				}
				
			}
			
			if (!jsonObject.isNull("com")) {
				JSONArray comArray = jsonObject.getJSONArray("com");
				for (int i = 0; i < comArray.length(); i++) {
					JSONObject item = (JSONObject) comArray.get(i);
					BroadcastNotificationData data = new BroadcastNotificationData();
					data.type = BroadcastNotificationData.ITEM_TYPE_COMMENT;
					data.afid = item.getString("afid");
					data.mid = item.getString("mid");
					data.ts = item.getLong("ts");
					data.user_class = item.getInt("class");
					String msg = item.getString("msg");
					if (!TextUtils.isEmpty(msg)) {
						data.msg = new String(Base64.decode(msg, Base64.DEFAULT));
					}else {
						data.msg = "";
					}
					data.name = item.getString("name");
					data.head_img_path = item.getString("local_img_path");


					try{
						String strSex = item.getString("sex");
						byte sex = Consts.AFMOBI_SEX_MALE;
						if (AfFriendInfo.MALE.equals(strSex)) {
							sex = Consts.AFMOBI_SEX_MALE;
						} else if (AfFriendInfo.FEMALE.equals(strSex)) {
							sex = Consts.AFMOBI_SEX_FEMALE;
						}
						data.sex = sex;

						data.age = item.getInt("age");

						data.sign = item.getString("sign");
						data.region = item.getString("region");
					}catch(Exception e){
						//公众账号可能没性别 年龄 签名 地区
					}
					try{//added Ver5.1  5.1以后版本才会有以下4个字段， 
						data.original_type=(byte) item.getInt("type");
						
						String _content =	 item.getString("content"); 
						if (!TextUtils.isEmpty(_content)) {
							data.original_content = new String(Base64.decode(_content, Base64.DEFAULT));
						}else {
							data.original_content = "";
						}  
						
						String safid =	 item.getString("safid"); 
						if (!TextUtils.isEmpty(safid)) {
							data.at_safid = safid;
						}else {
							data.at_safid = "";
						} 
						String sname =	 item.getString("sname"); 
						if (!TextUtils.isEmpty(sname)) {
							data.at_sname = sname;
						}else {
							data.at_sname = "";
						}  
						
					}catch(Exception e){ 
					}
				 
					if (save2Db) {//
						PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCNotifyInsert(CacheManager.getInstance().getMyProfile().afId, data.afid, data.mid, BroadcastNotificationData.ITEM_TYPE_COMMENT, 
								BroadcastNotificationData.STATUS_UNREAD, data.ts, data.msg, data.name, data.age, data.sex, data.sign, 
								data.region, data.head_img_path ,data.at_safid,data.at_sname,data.original_content,data.original_type);
					}
					
					if (mapCom.containsKey(data.mid)) {
						mapCom.get(data.mid).add(data);
					} else {
						ArrayList<BroadcastNotificationData> list = new ArrayList<BroadcastNotificationData>();
						list.add(data);
						mapCom.put(data.mid, list);
					}
							
				}
				
				
			}
			 
			if(mapLike!=null&&mapLike.size()>0){
				datas.add(mapLike);
			}
			if(mapCom!=null&&mapCom.size()>0){
				datas.add(mapCom);
			} 
			if(datas.isEmpty()){
				return null;
			}
			return datas; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			PalmchatLogUtils.println("CommonJson parseBrdNotiJson " + json+e.toString());
			return null;
		}
		
	}
	
	/**
	 * 获得最近的多少条数据
	 * @param readStatus BroadcastNotificationData.STATUS_UNREAD or BroadcastNotificationData.STATUS_READ
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static ArrayList<BroadcastNotificationData> getBrdNotifyDataFromDb(int count,int readStatus) {
		
		ArrayList<BroadcastNotificationData> datas = new ArrayList<BroadcastNotificationData>();
		AfData dataLikeOrCom = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCNotifyList(-1,count,
				readStatus,-1,CacheManager.getInstance().getMyProfile().afId);
	    
		//ceshiyong
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		for (int i = 0; i < dataLikeOrCom.mBcNotifyList.size(); i++) {
			String time = (dateFormat.format(new Date(dataLikeOrCom.mBcNotifyList.get(i).server_time*1000)));
			Log.e("--wx--", time);
			BcNotify bcNotify = dataLikeOrCom.mBcNotifyList.get(i);
			BroadcastNotificationData data = BroadcastNotificationData.AfData2BrdNotifyData(bcNotify);
			datas.add(data);
		}	
		return datas;
	}
	
	/**
	 * 
	 * @param readStatus BroadcastNotificationData.STATUS_UNREAD or BroadcastNotificationData.STATUS_READ
	 * @return
	 */
	public static ArrayList<HashMap<String, List<BroadcastNotificationData>>> getBrdNotifyDataFromDb(int readStatus) {
		
		ArrayList<HashMap<String, List<BroadcastNotificationData>>> datas = new ArrayList<HashMap<String,List<BroadcastNotificationData>>>();
		HashMap<String, List<BroadcastNotificationData>> mapLike = new HashMap<String, List<BroadcastNotificationData>>();
		HashMap<String, List<BroadcastNotificationData>> mapCom = new HashMap<String, List<BroadcastNotificationData>>();
		
		AfData dataLike = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCNotifyListByStatus(CacheManager.getInstance().getMyProfile().afId, readStatus
				, BroadcastNotificationData.ITEM_TYPE_LIKE);
		
		AfData dataComment = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCNotifyListByStatus(CacheManager.getInstance().getMyProfile().afId, readStatus
				, BroadcastNotificationData.ITEM_TYPE_COMMENT);
		
		
		for (int i = 0; i < dataLike.mBcNotifyList.size(); i++) {
			BcNotify bcNotify = dataLike.mBcNotifyList.get(i);
			BroadcastNotificationData data = BroadcastNotificationData.AfData2BrdNotifyData(bcNotify);
			if (mapLike.containsKey(data.mid)) {
				mapLike.get(data.mid).add(data);
			} else {
				ArrayList<BroadcastNotificationData> list = new ArrayList<BroadcastNotificationData>();
				list.add(data);
				mapLike.put(data.mid, list);
			}
			
		}
		

		for (int i = 0; i < dataComment.mBcNotifyList.size(); i++) {
			BcNotify bcNotify = dataComment.mBcNotifyList.get(i);
			BroadcastNotificationData data = BroadcastNotificationData.AfData2BrdNotifyData(bcNotify);
			if (mapCom.containsKey(data.mid)) {
				mapCom.get(data.mid).add(data);
			} else {
				ArrayList<BroadcastNotificationData> list = new ArrayList<BroadcastNotificationData>();
				list.add(data);
				mapCom.put(data.mid, list);
			}
		}
		
		
		datas.add(mapLike);
		datas.add(mapCom);
		
		return datas;
	}
}
