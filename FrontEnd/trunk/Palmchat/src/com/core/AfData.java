package com.core;

import java.util.ArrayList;

//import java.util.ArrayList;

public class AfData {
	
	//for user:
	public ArrayList<BcNotify> mBcNotifyList = new ArrayList<BcNotify>();
	public GisData mGisData;
	
	
	
	
	
	
	
	
	
	
	public AfData(){}
	//======================custom class=========================
	public class BcNotify{
		public BcNotify(){}
		public int _id,type,status,age;public long server_time;public byte sex;
		public String afId,mid,msg,name,sign,region,head_img_path,safid,sname,content;
		public byte content_type;
	}
	public class GisData{
		public GisData(){}
		public double lat,lng;
	}
	//////////////////////////////////////////////////////////////
	
	
	
	//=========================custom fuctions=====================
	public void clear_BcNotifyData(){mBcNotifyList.clear();}
	public void add_BcNotifyData(int _id,String afId,String mid,int type,int status,long server_time,String msg,String name,int age,byte sex,String sign,String region,String head_img_path,String safid,String sname,String content,byte content_type)
	{
		BcNotify sd = new BcNotify();
		sd._id=_id;
		sd.afId=afId;
		sd.mid=mid;
		sd.type=type;
		sd.status=status;
		sd.server_time=server_time;
		sd.msg=msg;
		sd.name=name;
		sd.age=age;
		sd.sex=sex;
		sd.sign=sign;
		sd.region=region;
		sd.head_img_path=head_img_path;
		sd.safid=safid;
		sd.sname=sname;
		sd.content=content;
		sd.content_type=content_type;
		mBcNotifyList.add(sd);
	}
	
	//public void clear_GisData(){List1.clear();}
	public void add_GisData(String lat,String lng)
	{
		GisData sd = new GisData();
		try{
		sd.lat=Double.parseDouble(lat);
		sd.lng=Double.parseDouble(lng);
		}catch(Exception e){
			sd.lat=0;sd.lng=0;
		}
		this.mGisData = sd;
		//List1.add(sd);
	}
}
