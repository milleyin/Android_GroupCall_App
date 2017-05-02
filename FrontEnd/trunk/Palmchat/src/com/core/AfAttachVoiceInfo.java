package com.core;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class AfAttachVoiceInfo implements Serializable,Parcelable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5281809877713372490L;

	public int _id;
	
	public int voice_len;		//voice playing length
	public int file_size;		//file size
	public String file_name;		//file name
	public AfAttachVoiceInfo(){
		
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
		dest.writeInt(voice_len);
		dest.writeInt(file_size);
		dest.writeString(file_name);
	}
	
	public final Parcelable.Creator<AfAttachVoiceInfo> CREATOR = new Parcelable.Creator<AfAttachVoiceInfo>() {  
		@Override  
		public AfAttachVoiceInfo createFromParcel(Parcel source) {  
			AfAttachVoiceInfo afVoice = new AfAttachVoiceInfo();  
			afVoice._id = source.readInt();  
			afVoice.voice_len = source.readInt();
			afVoice.file_size = source.readInt();
			afVoice.file_name = source.readString();
			return afVoice;  
		}

		@Override
		public AfAttachVoiceInfo[] newArray(int size) {
			return new AfAttachVoiceInfo[size];
		}

		
	};
			
	
}