package com.core;
import com.core.AfStoreProdList.AfPageInfo;
public class AfPaymentTransRecord{
	public static class AfPaymentTransItem{
		public AfPaymentTransItem(){
		}
		public AfPaymentTransItem(String afid, String name, int coin, String trans_date, String trans_id, String trans_desc, String arg1,String arg2, int trans_type)
		{
		    this.set(afid, name, coin, trans_date, trans_id, trans_desc, arg1, arg2, trans_type);
		}
		public void set(String afid, String name, int coin, String trans_date, String trans_id, String trans_desc, String arg1,String arg2, int trans_type)
		{
			this.afid = afid;
		    this.name = name;
		    this.afcoin = coin;
		    this.trans_date = trans_date;
		    this.trans_id = trans_id;
		    this.trans_desc = trans_desc;
		    this.arg1 = arg1;
		    this.arg2 = arg2;
		    this.trans_type = trans_type;
		}
		public String    afid;
		public String    name;
		public int       afcoin;
		public String    trans_date;
		public String    trans_id;
		public String    trans_desc;
		public String    arg1;
		public String    arg2;
		public int       trans_type;
	}
	public void set_pageinfo(int total, int page_count, int page_size, int page_index)
	{
	     page_info.set_page(total,page_count,page_size, page_index);
	}
	public void set_pageinfo_trans_item(String afid, String name, int coin, String trans_date, String trans_id, String trans_desc,String arg1, String arg2, int trans_type)
	{	     
		   page_info.set_list(new AfPaymentTransItem(afid, name, coin, trans_date, trans_id, trans_desc, arg1, arg2, trans_type));
	}
    public AfPageInfo<AfPaymentTransItem> page_info = new AfPageInfo<AfPaymentTransItem>();
}
