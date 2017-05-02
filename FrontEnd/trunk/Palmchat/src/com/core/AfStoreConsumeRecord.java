package com.core;

import java.util.ArrayList;

public class AfStoreConsumeRecord {

	public static class AfConsumeItem {
		 public AfConsumeItem(String item_id, int afcoin, String name, String date,String item_icon){
		     this.item_id = item_id;
		     this.afcoin = afcoin;
		     this.name = name;
		     this.date = date;
			 this.item_icon = item_icon;
		 }
		 public  String    item_id;
		 public  int       afcoin;
		 public  String    name;
		 public  String    date;
		 public  String    item_icon;
	}
	public static class AfPageInfo {
		public AfPageInfo(){
		}
		public void set_page(int total, int page_count, int page_size, int page_index)
		{
			this.total = total;
			this.page_count = page_count;
			this.page_size = page_size;
			this.page_index = page_index;
		}
		public void set_list(String item_id, int afcoin, String name, String date, String item_icon)
		{
			this.prof_list.add(new AfConsumeItem(item_id, afcoin, name, date, item_icon));
		}
		
		public int      total;         //product total
  		public int      page_count;   //page total
  		public int      page_size;    //every page size
  		public int      page_index;   //currnt page index
		public ArrayList<AfConsumeItem> prof_list =  new  ArrayList<AfConsumeItem>();	//members 
    }
	public void set_pageinfo(int total, int page_count, int page_size, int page_index)
	{
		page_info.set_page(total,page_count,page_size,page_index);
	}
	public void set_page_cosumeitem(String item_id, int afcoin, String name, String date, String item_icon)
	{
		page_info.set_list(item_id, afcoin,name, date,item_icon);
	}
	public String     url;
	public AfPageInfo page_info = new AfPageInfo();
}
