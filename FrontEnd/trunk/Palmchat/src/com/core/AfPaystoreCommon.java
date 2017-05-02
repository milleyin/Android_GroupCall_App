package com.core;
import com.core.AfPaymentTransRecord.AfPaymentTransItem;
import java.util.ArrayList;
public class AfPaystoreCommon {
	public static class AfPaymentConsume {
		public AfPaymentConsume(int balance, String trans_id, String afid, String trans_date){
			this.set(balance, trans_id, afid, trans_date);
		}
		public void set(int balance, String trans_id, String afid, String trans_date)
		{
			this.balance = balance;
			this.trans_id = trans_id;
			this.afid = afid;
			this.trans_date = trans_date;
		}
		public int       balance;
		public String    trans_id;
		public String    afid;
		public String    trans_date;
	}
	
	public static class AfPaymentSmsGetsn{
		 public AfPaymentSmsGetsn(String sn, String sp, String sms_content, String content, int price, String currency, int recharge_pcoin){
		     this.set(sn, sp, sms_content, content, price ,currency, recharge_pcoin);
		 }
		 public void set(String sn, String sp, String sms_content, String content,int price, String currency, int recharge_pcoin)
		 {
			 this.sn = sn;
			 this.sp = sp;
			 this.sms_content = sms_content;
			 this.content = content;
			 this.price = price;
			 this.currency = currency;
			 this.recharge_pcoin = recharge_pcoin;
		 }
	     public String    sn;
	     public String    sp;
	     public String    sms_content;
	     public String    content;
		 public int       price;
		 public String    currency;
		 public int       recharge_pcoin;
	}
	
	public static class AfPaymentGTRecharge {
		public AfPaymentGTRecharge(int balance, String trans_id,String afid, String trans_date, int recharge_coin){
		   this.set(balance, trans_id, afid,trans_date, recharge_coin);
		}
		public  void set(int balance, String trans_id,String afid, String trans_date, int recharge_coin)
		{
			this.balance = balance;
			this.trans_id = trans_id;
			this.afid = afid;
			this.trans_date = trans_date;
			this.recharge_coin = recharge_coin;
		}
		public  int       balance;
		public  String    trans_id;
		public  String    afid;
		public  String    trans_date;
		public  int       recharge_coin;

	}

	public static class AfStoreDlProdInfo {
		public AfStoreDlProdInfo(String item_id, String save_path,String alas_name, String life_time, int status, int price, long datetime, int ver_code,String packagename){
		   this.set( item_id, save_path,alas_name, life_time, status, price, datetime,ver_code,packagename);
		}
		public  void set(String item_id, String save_path,String alas_name, String life_time, int status, int price,long datetime, int ver_code,String packagename)
		{
			this.item_id = item_id;
			this.save_path= save_path;
			this.alas_name = alas_name;
			this.life_time= life_time;
			this.status = status;
			this.price = price;
			this.datetime = datetime;
			this.ver_code = ver_code;
			this.packagename = packagename;
		}
	
		public  String    item_id;
		public  String    save_path;
		public  String    alas_name;
		public  String    life_time;
		public  int       status;
		public  int       price;
		public  long      datetime;
		public  int       ver_code;

		public String     packagename;
	}

	public static class AfStoreDlProdInfoList {

		public void set_list(String item_id, String save_path, String alas_name, String life_time, int status, int price, long datetime, int ver_code,String packagename)
		{
			this.prod_list.add(new AfStoreDlProdInfo(item_id, save_path, alas_name, life_time, status, price, datetime, ver_code, packagename));
		}
		public ArrayList<AfStoreDlProdInfo> prod_list =  new  ArrayList<AfStoreDlProdInfo>();	//members 
		
	}
	public void set_gt_recharge(int balance, String trans_id,String afid, String trans_date, int recharge_coin)
	{
	    obj = new AfPaymentGTRecharge(balance, trans_id, afid, trans_date, recharge_coin);
	}
	public void set_trans_detail(String afid, String name, int coin, String trans_date, String trans_id, String trans_desc, String arg1,String arg2, int trans_type)
	{
		obj = new AfPaymentTransItem(afid, name, coin, trans_date, trans_id, trans_desc, arg1, arg2, trans_type);
	}
	public void set_consume(int balance, String trans_id, String afid, String trans_date)
	{
		obj = new AfPaymentConsume(balance,trans_id, afid, trans_date);
	}
	public void set_sms_getsn(String sn, String sp, String sms_content, String content, int price, String currency, int recharge_pcoin)
	{
		obj = new AfPaymentSmsGetsn(sn, sp, sms_content, content, price,currency, recharge_pcoin);
	}
	public void set_db_prodinfo_list(String item_id, String save_path, String alas_name, String life_time, int status, int price, long datetime,int ver_code,String packagename)
	{
		AfStoreDlProdInfoList tmp = null;
	    if(null == obj){
		    obj = new AfStoreDlProdInfoList();
	    }
	    tmp = (AfStoreDlProdInfoList)obj;
		tmp.set_list(item_id, save_path, alas_name, life_time, status, price, datetime, ver_code,packagename);
	}
	public Object  obj= null;
}
