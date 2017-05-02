package com.core;

public class AfPublicGroup {

	//tags
	public Category[] categoryTags;
	
	//gps tag...  group
	public int total;
	public AfGrpProfileInfo[] grpProfileList;
	
	//hot group 
	public String[] countryList;
	public String[] cityList;
	public int countryTotal;
	public int cityTotal;
	
	
	
	
	
	
	
	
	
	//======================custom class=========================
	public AfPublicGroup(){}
	public class Category{
		public String category;
		public String avatar;
		public String[] tags;
		public Category(){}
	}
	//===========================================================
}
