package com.core;

import java.io.Serializable;

public class AfChatroomEntryInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1948856855722686122L;
	public String bm;
	public String cid;
	public String cname;
	public String ctoken;
	public String cptoken;
	
	//top message
	public String name;	
	public byte age;
	public byte sex;	
	public String msg;  
	public String fromAfId; 						

	AfChatroomEntryInfo(){
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "bm  "+bm + " cid "+cid +" cname  "+cname+ "  ctoken  "+ctoken +"  cptoken  "+cptoken;
	}
}
