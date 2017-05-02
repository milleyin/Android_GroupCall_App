package com.core;

public class AfVersionInfo {
	
	public String error;
	public String version;
	public String desc;
	public String url;	
	public String incr_url;
	public String md5;
	public long size;
	
	public AfVersionInfo(){
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "error  "+error+"  version  "+version+"  desc  "+desc+"  url  "+url + " incr_url "+incr_url+" md5 "+md5+" size "+size;
	}
}
