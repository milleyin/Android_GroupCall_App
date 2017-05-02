package com.core;
import java.io.Serializable;
import java.util.ArrayList;
import android.text.TextUtils;
public class AfStoreProdList implements Serializable {
   
    /**
	 * 
	 */
	private static final long serialVersionUID = -4364251474600405383L;
	public static class AfBannerInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2081980995152371746L;
		public AfBannerInfo(String image_id, String image_path){
			this.image_id = image_id;
			this.image_path = image_path;
		}
		public String image_id;
		public String image_path;
    }
		
    public static class AfProdProfile implements Serializable {
    	/**
		 * 
		 */
		private static final long serialVersionUID = -3564530672787940766L;
		public AfProdProfile(){
    	}
		public AfProdProfile(String name, String pkt_name, int ver_code, String s_icon, String item_id, String ver_name, int afcoin, int pkt_size,boolean is_new){
			this.set(name, pkt_name, ver_code, s_icon, item_id, ver_name, afcoin, pkt_size,is_new);
		}
		public void set(String name, String pkt_name, int ver_code, String s_icon, String item_id, String ver_name, int afcoin, int pkt_size,boolean is_new)
		{
			this.name = name;
			this.packet_name = pkt_name;
			this.ver_code = ver_code;
			this.small_icon = s_icon;
			this.item_id = item_id;
			this.ver_name = ver_name;
			this.afcoin = afcoin;
			this.packet_size = pkt_size;
			this.is_new = is_new;
		}
		public  String       name;
	    public  String       packet_name;
	    public  int          ver_code;
	    public  String       small_icon;
	    public  String       item_id;
	    public  String       ver_name;
	    public  int          afcoin;
	    public  int          packet_size;
		public  boolean      is_new;
//	    add by wk
	    public int progress;
	    public boolean isDownloaded;
	    public boolean is_face_change;
    }

	 public static class AfPageInfo<T> implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1296423388463067591L;

		public AfPageInfo(){
		}
		
		public void set_page(int total, int page_count, int page_size, int page_index)
		{
			this.total =total;
			this.page_count = page_count;
			this.page_size = page_size;
			this.page_index = page_index;
		}
		public void set_list(T element)
		{
			this.prof_list.add(element);
		}
		public int      total;         //product total
  		public int      page_count;   //page total
  		public int      page_size;    //every page size
  		public int      page_index;   //currnt page index

		public ArrayList<T> prof_list =  new  ArrayList<T>();	//members 
    }

	public void add_banner(String image_id, String image_path){
		if( !TextUtils.isEmpty(image_path) ){
			banners.add(new AfBannerInfo(image_id, image_path));
		}
	}
	public void set_pageinfo(int total, int page_count, int page_size, int page_index)
	{
	     page_info.set_page(total,page_count,page_size, page_index);
	}
	public void set_pageinfo_prod_profile(String name, String pkt_name, int ver_code, String s_icon, String item_id, String ver_name, int afcoin, int pkt_size,boolean is_new)
	{	     
		   page_info.set_list(new AfProdProfile(name, pkt_name, ver_code, s_icon, item_id, ver_name, afcoin, pkt_size,is_new));
	}
	
	public String url;
	public ArrayList<AfBannerInfo> banners =  new  ArrayList<AfBannerInfo>();	//members 
	public AfPageInfo<AfProdProfile> page_info = new AfPageInfo<AfProdProfile>();
}
