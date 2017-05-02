package com.core;


import android.os.Parcel;
import android.os.Parcelable;

public class AfImageReqInfo implements Parcelable{
	
	public int _id;
	//public int chat_msg_id; 		// chat msg id
	public String recv_afid; 		// recevie afid
	public String send_msg; 		// msg content
	public String file_name; 		// download disp-file name
	public String path; 			// file full path
	public String msession; 		// server session, NULL: need to init
	public String server_dir; 		// server dir
	public String server_name;		// server file name
	public int cur_part;			// cur finished part
	public int len_part; 			// part len
	public int total_part; 			// total part
	public int file_size; 		    // file size
	public String file_md5; 		// file md5
	
	public AfImageReqInfo(){
		
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(_id);
		dest.writeString(recv_afid);
		dest.writeString(send_msg);
		dest.writeString(file_name);
		dest.writeString(path);
		dest.writeString(msession);
		dest.writeString(server_dir);
		dest.writeString(server_name);
		dest.writeInt(cur_part);
		dest.writeInt(len_part);
		dest.writeInt(total_part);
		dest.writeInt(file_size);
		dest.writeString(file_md5);
		
	}
	
	public final Parcelable.Creator<AfImageReqInfo> CREATOR = new Parcelable.Creator<AfImageReqInfo>() {  
		@Override  
		public AfImageReqInfo createFromParcel(Parcel source) {  
			AfImageReqInfo afReqInfo = new AfImageReqInfo();  
			afReqInfo._id = source.readInt();  
			afReqInfo.recv_afid = source.readString();
			afReqInfo.send_msg = source.readString();
			afReqInfo.file_name = source.readString();
			afReqInfo.path = source.readString();
			afReqInfo.msession = source.readString();
			afReqInfo.server_dir = source.readString();
			afReqInfo.server_name = source.readString();
			afReqInfo.cur_part = source.readInt();
			afReqInfo.len_part = source.readInt();
			afReqInfo.total_part = source.readInt();
			afReqInfo.file_size = source.readInt();
			afReqInfo.file_md5 = source.readString();
			return afReqInfo;  
		}

		@Override
		public AfImageReqInfo[] newArray(int size) {
			return new AfImageReqInfo[size];
		}

		
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}


}
