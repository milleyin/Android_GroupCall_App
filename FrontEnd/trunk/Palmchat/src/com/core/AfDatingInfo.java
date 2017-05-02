package com.core;

import java.io.Serializable;

public class AfDatingInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public byte marital_status = Consts.AFMOBI_MARITAL_STATUS_UNMARRIED; /*marital status*/
	public String dating_phone;							/*dating phone number*/
	public boolean is_show_dating_phone;				/*wheather show  phone number of dating to other*/
	public int charm_level;								/*charm level*/
	public boolean is_show_star;						/*is show star*/
	public int wealth_flower;							/*the wealth flower used by myself*/
	public int gift_flower;								/*get gift flower frow others*/
	public int dating_phone_flower = 5;						/*right to view dating phone when the wealth flower arrives the speicified amount*/
	
	public AfDatingInfo(){
		
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "marital_status  "+marital_status +" dating_phone  "+dating_phone +" is_show_dating_phone  "+is_show_dating_phone
				+" charm_level  "+charm_level+" is_show_star  "+is_show_star+" wealth_flower  "+wealth_flower
				+" gift_flower  "+gift_flower+" dating_phone_flower  "+dating_phone_flower;
	}
}
