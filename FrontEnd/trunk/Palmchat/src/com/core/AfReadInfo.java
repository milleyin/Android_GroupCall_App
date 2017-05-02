package com.core;

import java.io.Serializable;

public class AfReadInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int type; //message type
	public int status;
	public String fromAfId;
	public String toAfId;
	
	
}
