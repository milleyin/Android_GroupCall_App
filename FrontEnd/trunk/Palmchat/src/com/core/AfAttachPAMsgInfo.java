package com.core;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class AfAttachPAMsgInfo implements Serializable,Parcelable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3434705223361372447L;

	public int _id;

	public int msgtype;             // MESSAGE_PUBLIC_ACCOUNT,MESSAGE_PA_URL,MESSAGE_BROADCAST_SHARE_TAG,MESSAGE_BROADCAST_SHARE_BRMSG       
	public String content;			// content or description or tagname
	public String url;              // nly MESSAGE_PA_URL,MESSAGE_PUBLIC_ACCOUNT:detail url

	
	public String title;            // only MESSAGE_PA_URL    
	public String imgurl;           // only MESSAGE_PA_URL,MESSAGE_BROADCAST_SHARE_TAG,MESSAGE_BROADCAST_SHARE_BRMSG
	public String author;           // only MESSAGE_PA_URL 
	public String type;             // only MESSAGE_PA_URL
	
	public String mid;              // only MESSAGE_BROADCAST_SHARE_BRMSG
	public long postnum;             // only MESSAGE_BROADCAST_SHARE_TAG
	
	public String afid;            // broadcast sender afid,only MESSAGE_BROADCAST_SHARE_BRMSG
	public String local_img_path;  // broadcast sender headimg,only MESSAGE_BROADCAST_SHARE_BRMSG
	public String name;            // broadcast sender nickname,only MESSAGE_BROADCAST_SHARE_BRMSG
	
	public String time;            // only MESSAGE_PA_URL,MESSAGE_PA,for public account history
	public long time_int64;            // only MESSAGE_PA_URL,MESSAGE_PA,for public account history
	
	public AfAttachPAMsgInfo(){
		
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(_id);
		dest.writeInt(msgtype);
		dest.writeString(content);
		dest.writeString(url);
		
		dest.writeString(title);
		dest.writeString(imgurl);
		dest.writeString(author);
		dest.writeString(type);
		
		dest.writeString(mid);
		dest.writeLong(postnum);
		
		dest.writeString(afid);
		dest.writeString(local_img_path);
		dest.writeString(name);
		dest.writeString(time);
		dest.writeLong(time_int64);
	}
	
	public final Parcelable.Creator<AfAttachPAMsgInfo> CREATOR = new Parcelable.Creator<AfAttachPAMsgInfo>() {  
		@Override  
		public AfAttachPAMsgInfo createFromParcel(Parcel source) {  
			AfAttachPAMsgInfo afPAMsg = new AfAttachPAMsgInfo();  
			afPAMsg._id = source.readInt();  
			afPAMsg.msgtype = source.readInt();
			afPAMsg.content = source.readString();
			afPAMsg.content = source.readString();
			
			afPAMsg.title = source.readString();
			afPAMsg.imgurl = source.readString();
			afPAMsg.author = source.readString();
			afPAMsg.type = source.readString();
			afPAMsg.mid = source.readString();
			afPAMsg.postnum = source.readLong();
			
			afPAMsg.afid = source.readString();
			afPAMsg.local_img_path = source.readString();
			afPAMsg.name = source.readString();
			afPAMsg.time = source.readString();
			afPAMsg.time_int64 = source.readLong();
			
			return afPAMsg;  
		}

		@Override
		public AfAttachPAMsgInfo[] newArray(int size) {
			return new AfAttachPAMsgInfo[size];
		}

		
	};
			
	
}