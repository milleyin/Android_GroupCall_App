package com.core;

public class AfChatroomMemberInfo {
	public String afid;
	public String name;
	public byte sex;
	public int age;
	public String sign;
	
	// is add to my friend ? 
	public boolean isAddFriend;
	
//	img path from AfFriendInfo
	public String head_img_path;
	
	
	public static AfFriendInfo ChatroomMemberInfoToFriend(AfChatroomMemberInfo f) {
		AfFriendInfo dto = new AfFriendInfo();
		dto.afId=f.afid;
		dto.name=f.name;
		dto.age=f.age;
		dto.sex=f.sex;
		dto.signature=f.sign;
		dto.head_img_path=f.head_img_path;
		dto.alias=f.name;
		return dto;
	}

	public String getAfidFromHead() {
		if (head_img_path != null) {
			String [] ss = head_img_path.split(",");
			if (ss.length >= 1) {
				return ss[0];
			}
		}
		return afid;
	}
	
	public String getSerialFromHead() {
		if (head_img_path != null) {
			String [] ss = head_img_path.split(",");
			if (ss.length >= 2) {
				return ss[1];
			}
		}
		return "";
	}
	
	public String getServerUrl() {
		if (head_img_path != null) {
			String [] ss = head_img_path.split(",");
			if (ss.length >= 3) {
				return ss[2].replaceAll("/d", "");
			}
		}
		return "";
	}
	
	AfChatroomMemberInfo(){
		
	}
}
