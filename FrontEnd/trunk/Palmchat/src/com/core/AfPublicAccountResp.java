package com.core;

import java.util.ArrayList;
import java.util.Arrays;



public class AfPublicAccountResp {
	
	public boolean next = false;
	public AfAttachPAMsgInfo[] historylist = null;
	
	
	
	public  void AddHistoryList(AfAttachPAMsgInfo[] list)
	{
		historylist = list;
	}
	
	
}
