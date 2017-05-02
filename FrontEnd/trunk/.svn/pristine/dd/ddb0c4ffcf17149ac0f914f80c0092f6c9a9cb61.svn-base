package com.core;

public class AfGiftInfo {
	public int _id;
	public int identify;   //
	public String afid;
	public int count;
	public int charm_level;
	public long time;
	public String head_image_path;
	public String name;
	public byte sex;
	public int age;
	public String sign;

	public AfGiftInfo(){
	}
	
	public String getSerialFromHead() {
		if (head_image_path != null) {
			String [] ss = head_image_path.split(",");
			if (ss.length >= 2) {
				return ss[1];
			}
		}
		return "";
	}
	
	public String getServerUrl() {
		if (head_image_path != null) {
			String [] ss = head_image_path.split(",");
			if (ss.length >= 3) {
				return ss[2].replaceAll("/d", "");
			}
		}
		return "";
	}
	
}
