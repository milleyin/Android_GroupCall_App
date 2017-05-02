package com.core;

import java.io.Serializable;
import java.util.ArrayList;

public class AfAttachPalmCoinSysInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5281809877713372490L;

	public int refresh_balance; // 值为1时表示用户余额有变动，需要更新用户余额;值为2刷新PallCall帐户余额
	public int status;		    // 用于确定消息体中的支付状态，值为0表示处理中，1表示成功，2 表示失败
	public String header;		// title信息
	public String footer;		// footer信息url
	public ArrayList<AfAttachPalmCoinSysInfoItem> body_list; // body信息, a list of AfAttachPalmCoinSysInfoItem
	public AfAttachPalmCoinSysInfo(){
		
	}
	
	public void AddItemInfo(String key, String value)
	{
		AfAttachPalmCoinSysInfoItem item = new AfAttachPalmCoinSysInfoItem();
		item.key = key;
		item.value = value;
		if(body_list == null)
		{
			body_list = new ArrayList<AfAttachPalmCoinSysInfo.AfAttachPalmCoinSysInfoItem>();
		}
		
		body_list.add(item);
	}
	
	public class AfAttachPalmCoinSysInfoItem implements Serializable
	{
		private static final long serialVersionUID = -636843364684906119L;
		public String key;		// key
		public String value;	// value
		
		public AfAttachPalmCoinSysInfoItem(){
			
		}
	}
}	