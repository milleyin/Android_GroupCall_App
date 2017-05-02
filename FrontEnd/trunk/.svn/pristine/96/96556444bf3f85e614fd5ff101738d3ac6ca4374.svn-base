package com.core;

import android.os.Parcel;
import android.os.Parcelable;


public class AfAttachImageInfo implements Parcelable{
	public int _id;
	public String small_file_name; 		//small image file name 
	public int small_file_size;			//small image file size
	
	public String large_file_name;	  	//large image file name
	public int large_file_size;			//large image file size
	
	public String url;				    //large image of url	
	public int progress;		        //progress value
	
	public int upload_id;				//upload image info id
	public AfImageReqInfo upload_info;	//upload image info  
	
	public AfAttachImageInfo(){
		
	}
	public void setAfImageReqInfo(AfImageReqInfo req){
		upload_info = req;
		
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(_id);
		dest.writeString(small_file_name);
		dest.writeInt(small_file_size);
		dest.writeString(large_file_name);
		dest.writeInt(large_file_size);
		dest.writeString(url);
		dest.writeInt(progress);
		dest.writeInt(upload_id);
		dest.writeValue(upload_info);
		
	}
	
	public final Parcelable.Creator<AfAttachImageInfo> CREATOR = new Parcelable.Creator<AfAttachImageInfo>() {  
		@Override  
		public AfAttachImageInfo createFromParcel(Parcel source) {  
			AfAttachImageInfo afImage = new AfAttachImageInfo();  
			afImage._id = source.readInt();  
			afImage.small_file_name = source.readString();
			afImage.small_file_size = source.readInt();
			afImage.large_file_name = source.readString();
			afImage.large_file_size = source.readInt();
			afImage.url = source.readString();
			afImage.progress = source.readInt();
			afImage.upload_id = source.readInt();
			afImage.upload_info = (AfImageReqInfo) source.readValue(AfImageReqInfo.class.getClassLoader());
			return afImage;  
		}

		@Override
		public AfAttachImageInfo[] newArray(int size) {
			return new AfAttachImageInfo[size];
		}

		
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
			
}
