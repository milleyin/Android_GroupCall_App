package com.core;

import java.util.ArrayList;

import com.core.AfPaymentTransRecord.AfPaymentTransItem;
import com.core.AfPaystoreCommon.AfStoreDlProdInfo;
import com.core.AfStoreProdList.AfPageInfo;



public class AfNewPayment {
	/*Coin to goods type*/
	public final static int  NewPayment_Coin2Goods_Type_AirTime = 0;				/*airtime*/
	
	// 每个请求返回的数据对象
	public Object  obj = null;
	
	
	/* 获取cointogoods 列表*/
	public static class AFCoin2GoodsItem
	{
		public int item_id = 0;        // 消费选项 ID
		public int afcoin = 0;         // 消费的 Palmcoin的数量 
		public int aim_value = 0;      // 购买数量
		public int poundage = 0;       // 手续费	
		
		public AFCoin2GoodsItem(){}
	}
	
	public static class AFCoin2GoodsMenu
	{
		public int coin2goods_type = 0;
		public boolean use_flag = false; // 用于表示此充值运营商当前是否可用,false:不可用；true,可用
		public ArrayList<AFCoin2GoodsItem> coin2goods_list =  null; // 支持的消费选项列表
		public ArrayList<String> network_list =  null; // 支持网络运营商列表,coin2goods_type=NewPayment_Coin2Goods_Type_AirTime必定有
		
		public void AddNetWork(String network)
		{
			if(network != null && network.length() > 0 )
			{
				if(null == network_list)
				{
					network_list =  new  ArrayList<String>();
				}
				network_list.add(network);
			}
		}
		
		public void AddC2GItem(int id, int afcoin,int aim_value, int poundage)
		{
			AFCoin2GoodsItem item = new AFCoin2GoodsItem();
			item.item_id = id;
			item.afcoin = afcoin;
			item.aim_value = aim_value;
			item.poundage = poundage;
			
			if(null == coin2goods_list)
			{
				coin2goods_list =  new  ArrayList<AFCoin2GoodsItem>();
			}
			coin2goods_list.add(item);
		}
		
		public AFCoin2GoodsMenu(){}
	}
	
	public static class AFCoin2GoodsResp
	{
		public ArrayList<AFCoin2GoodsMenu> menu_list =  new  ArrayList<AFCoin2GoodsMenu>(); // 
		public void AddC2GMenu(AFCoin2GoodsMenu menu)
		{
			menu_list.add(menu);
		}
		
		public AFCoin2GoodsResp(){}
	}
	/* 获取cointogoods消费coin方式列表 */
	
	/* 获取billhistory账单列表 */
	public final static int  NewPayment_Bill_History_Type_ReChange = 0;				/*ReChange*/
	public final static int  NewPayment_Bill_History_Type_Consume = 1;				/*Consume*/
	public static class AFBillHistoryResp
	{
		public AfPageInfo<AfPaymentTransItem> page_info = new AfPageInfo<AfPaymentTransItem>(); // 记录列表
		public int total; // 总笔数
		public void set_pageinfo_trans_item(String afid, String name, int coin, String trans_date, String trans_id, String trans_desc,String arg1, String arg2, int trans_type)
		{	     
			   page_info.set_list(new AfPaymentTransItem(afid, name, coin, trans_date, trans_id, trans_desc, arg1, arg2, trans_type));
		}
	}
	/* 获取billhistory账单列表 */
	
	/* 获取Money2Coin充值方式列表 */
	public static class AFMoney2CoinsItem
	{
		public int item_id = 0;        // 充值选项 ID
		public int afcoin = 0;         // 所得的 PalmCoin 数量 
		public int money = 0;          // 充值所需花费的money数
		public int gift = 0;           // 赠送的 PalmCoin的数量(单位：%)
		public String currency = "";   // 币种
		
		public AFMoney2CoinsItem(){}
	}
	
	public static class AFMoney2CoinsMenu
	{
		public String gateway = "";  // 充值运营商，如 PAGA
		public String display = "";  // 充值运营商显示名称
		public String channel = "";  // 充值运营商下支援的充值渠道，如Agent
		public String logo = "";     // 充值运营商的logo url
		public String api_url = "";  // 使用该运营商时所需调用的url
		public boolean use_flag = false; // 用于表示此充值运营商当前是否可用
		
		public ArrayList<AFMoney2CoinsItem> money2coins_list =  new  ArrayList<AFMoney2CoinsItem>(); // 支持的充值选项列表
		public AFMoney2CoinsMenu(){}
		public void SetBasicInfo(String gateway, String display, String channel, String logo, String api_url,boolean use_flag)
		{
			this.gateway = gateway;
			this.display = display;
			this.channel = channel;
			this.logo = logo;
			this.api_url = api_url;
			this.use_flag = use_flag;
		}
		
		public void AddM2CItem(int id, int afcoin,int money, int gift, String currency)
		{
			AFMoney2CoinsItem item = new AFMoney2CoinsItem();
			item.item_id = id;
			item.afcoin = afcoin;
			item.money = money;
			item.gift = gift;
			item.currency = currency;
			
			money2coins_list.add(item);
		}
	}

	public static class AFMoney2CoinsResp
	{
		public ArrayList<AFMoney2CoinsMenu> menu_list =  new  ArrayList<AFMoney2CoinsMenu>(); 
		public AFMoney2CoinsResp(){}
		public void AddM2CMenu(AFMoney2CoinsMenu menu)
		{
			menu_list.add(menu);
		}
	}
	/* 获取Money2Coin充值方式列表 */
	
	/*airtime_topup airtime充话费，消费coin */
	
	public static class AFAirtimeTopupReq
	{
		public String from_afid = "";     // 支付 coin 的afid
		public String to_afid = "";  // 接收话费的afid
		public String phone_number = "";     // 接受话费的手机号
		public String phone_country_code = "";  // 接受话费的 afid 所在的国家的代码，如234
		public int bill_item_id = 0;          // Coin to Goods 列表中消费的 id
		public int afcoin = 0;           // 花费的 coin 的数量
		public int amount = 0;           // 要充值话费的值
		public String network = "";     // 手机号所属的运营商(运营商列表的数据请参照 coin2goods 接口返回的 network_list)
		public String remark = "";  // 发送给接收话费的 afid 的赠言（BASE64 Encode）
	}
	/*airtime_topup airtime充话费，消费coin */
	
	/*recharge order, 充值获取订单号*/			
	public final static String  NewPayment_RechargeOrder_AppChannel_Type_WALLET = "WALLET";       // Explore钱包
	public final static String  NewPayment_RechargeOrder_AppChannel_Type_PalmGuess = "PREDICT_WIN"; // PalmGuess
	public static class AFRechargeOrderReq
	{
		
		public int item_id = 0;          // PalmCoin 充值选项ID
		public int afcoin = 0;           // 所得的 PalmCoin 数量
		public int money = 0;            // 充值所需花费的money数量
		public String gateway = "";      // 充值运营商，如 PAGA(请参考money2coin 接口返回结果的gateway字段)
		public String channel = "";      // 充值运营商下支援的充值渠道，如Agent(请参考money2coin 接口返回结果的channel字段)
		public String currency = "";     // 币种
		public String app_channel = "";  // 接入应用的充值入口
	}
	/*recharge order 充值获取订单号*/
	
	/* 获取coin账单列表 */
	public final static int  GetCoinOrderHistory_Order_Type_ReChange = 1;				/*充值（用户通过第三方支付平台进行充值，如PAGA）*/
	public final static int  GetCoinOrderHistory_Order_Type_INCREASE = 2;				/*加钱（指内部的其它系统给该用户加钱，如赌博赢了给账户加钱）*/
	public final static int  GetCoinOrderHistory_Order_Type_CONSUME = 3;				/*消费（如购买东西或参与赌博）*/
	public final static int  GetCoinOrderHistory_Order_Type_WITHDRAW_CASH = 4;			/*提现（预留）*/
	public final static int  GetCoinOrderHistory_Order_Type_RETURN_MONEY = 5;			/*退款*/
	
	public final static int  GetCoinOrderHistory_Order_Status_Create_Order = 1;				/*订单创建*/
	public final static int  GetCoinOrderHistory_Order_Status_BALANCE_NO_ENOUGH = 2;		/*余额不足（消费时钱包返回）*/
	public final static int  GetCoinOrderHistory_Order_Status_ACCOUNT_ABNORMAL = 3;			/*账户信息不正常（加钱或消费时钱包返回）*/
	public final static int  GetCoinOrderHistory_Order_Status_OPERATE_SUCCESS = 4;			/*操作成功（包括充值成功、加钱成功、消费成功等）*/
	public final static int  GetCoinOrderHistory_Order_Status_OPERATE_FAIL_OTHERS = 5;		/*其它原因失败*/
	
	public final static String  GetCoinOrderHistory_Order_Money_Type_Coin = "COIN";		/*用户充的钱存在钱包系统中，供用户以后消费*/
	public final static String  GetCoinOrderHistory_Order_Money_Type_Point = "POINT";		/*用户充的钱会立即使用，此时一笔充值会在订单系统以及钱包系统中同时会产生两条记录，包括充值及消费记录*/
	
	public static class AFGetCoinOrderHistoryItem
	{
		public String order_id = "";        // 充值选项 ID
		public int afcoin = 0;         // 所得的 PalmCoin 数量 
		public String date_time = "";   // 订单日期
		public String order_desc = "";   // 客户端显示的描述内容
		public String money_type = "";   // 充值到账的方式:COIN/POINT
		public String product_code = "";   // 订单产品编号，目前只有3个产品：PalmCoin/Point/AirTime
		public int order_type = 0;        // 订单类型，GetCoinOrderHistory_Order_Type_xx
		public int order_status = 0;         // 订单状态 ，GetCoinOrderHistory_Order_Status_xx
			
		
		public AFGetCoinOrderHistoryItem(){}
	}
	
	public static class AFGetCoinOrderHistoryRecord
	{
		public String order_id = "";        // 订单ID
		public ArrayList<AFGetCoinOrderHistoryItem> pCOHItemList= new ArrayList<AFGetCoinOrderHistoryItem>(); // 记录列表
		public void set_pageinfo_trans_item(String order_id, int afcoin, String date_time, String order_desc, String money_type, String product_code, int order_type, int order_status)
		{	   
			AFGetCoinOrderHistoryItem item = new AFGetCoinOrderHistoryItem();
			item.order_id = order_id;
			item.afcoin = afcoin;
			item.date_time = date_time;
			item.order_desc = order_desc;
			item.money_type = money_type;
			item.product_code = product_code;
			item.order_type = order_type;
			item.order_status = order_status;
			
			pCOHItemList.add(item);
		}
		
		public AFGetCoinOrderHistoryRecord(){}
	}
	
	
	
	public static class AFGetCoinOrderHistoryResp
	{
		
		public int count; // 总笔数
		public int page_index; // 页的索引
		public int page_size; // 一页大小
		public int page_count; // 总页数
		
		public ArrayList<AFGetCoinOrderHistoryRecord> pCOHRecordList = new ArrayList<AFGetCoinOrderHistoryRecord>(); // 记录列表
		public void AddGetCoinOrderHistoryRecord(AFGetCoinOrderHistoryRecord node)
		{
			pCOHRecordList.add(node);
		}
		
	}
	/* 获取coin账单列表 */
	
}
