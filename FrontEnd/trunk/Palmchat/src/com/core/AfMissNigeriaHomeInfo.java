package com.core;

import java.util.Arrays;
import java.util.List;

public class AfMissNigeriaHomeInfo {	
	public String tips;
	public String title;
	
	public int rank;    			//myself rank
	public int charm_level;			//myself charm level
	
	public boolean hasEntry;		//true means has joined,false means not join.
	public List<AfProfileInfo> recommend; 
	
	AfMissNigeriaHomeInfo(){
		
	}
	
	//native  used
	private void set(String tips, String title, int rank, int charm_level, boolean has_entry, Object []rec){
		this.tips = tips;
		this.title = title;
		this.rank = rank;
		this.charm_level = charm_level;
		this.hasEntry = has_entry;
		
		if( null != rec){
			AfProfileInfo []infos = (AfProfileInfo[])rec;
			recommend = (List<AfProfileInfo>) Arrays.asList(infos);
		}

	}
	
	
}
