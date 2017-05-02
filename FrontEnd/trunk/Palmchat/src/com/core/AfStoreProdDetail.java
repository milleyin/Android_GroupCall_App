package com.core;

import com.core.AfStoreProdList.AfProdProfile;


public class AfStoreProdDetail {
	public static class AfProdDetail{
		public AfProdDetail(){
		}
		public void set_profile(String name, String pkt_name, int ver_code, String s_icon, String item_id, String ver_name, int afcoin, int pkt_size)
		{
			this.profile.set(name, pkt_name, ver_code, s_icon,item_id,ver_name, afcoin,pkt_size,false);
		}
		public void set(String big_icon, int e_count, int e_col, String desc, String xd_path, String hd_path, String ld_path, String md_path,boolean is_payed)
		{
			this.big_icon = big_icon;
			this.express_count = e_count;
			this.express_col = e_col;
			this.desc = desc;
			this.xd_path = xd_path;
			this.hd_path = hd_path;
			this.ld_path = ld_path;
			this.md_path = md_path;
			this.is_payed = is_payed;
		}
	    public  String       big_icon;
		public  int          express_count;
		public  int          express_col;
		public  String       desc;
		public  String       xd_path;
		public  String       hd_path;
        public  String       ld_path;
		public  String       md_path;
		public  boolean      is_payed;
        public  AfProdProfile profile = new AfProdProfile();
    }
	void set_prod_detail(String name, String pkt_name, int ver_code,String s_icon, String item_id, String ver_name, int afcoin,
		int pkt_size, String b_icon, int e_count,int e_col, String desc, String xd_path, String hd_path, String ld_path, String md_path, boolean is_payed)
	{
		detail.set_profile(name, pkt_name, ver_code, s_icon,item_id,ver_name, afcoin,pkt_size);
		detail.set(b_icon, e_count, e_col, desc, xd_path, hd_path, ld_path, md_path, is_payed);
	}
	public String url;
	public AfProdDetail detail = new AfProdDetail();
}
