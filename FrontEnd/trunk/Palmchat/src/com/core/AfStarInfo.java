package com.core;

import java.util.Arrays;
import java.util.List;


public class AfStarInfo {
	public int year;
	public int week;
	public int charm_level;   						//self charm level
	public int wealth_flower;						//self wealth flower
	public List<AfProfileInfo> top_star;
	public List<AfProfileInfo> normal_star;
	
	public AfStarInfo(){
		
	}
	
	//native add
	private void set(int year, int week, int charm_level, int wealth_flower, Object []top_star, Object []normal_star){
		AfProfileInfo []result;
		
		if( null != top_star){
			result = (AfProfileInfo [])top_star;
			this.top_star = Arrays.asList(result);
		}
		if( null != normal_star){
			result = (AfProfileInfo [])normal_star;
			this.normal_star = Arrays.asList(result);
		}
		this.year = year;
		this.week = week;
		this.charm_level = charm_level;
		this.wealth_flower = wealth_flower;
	}
}
