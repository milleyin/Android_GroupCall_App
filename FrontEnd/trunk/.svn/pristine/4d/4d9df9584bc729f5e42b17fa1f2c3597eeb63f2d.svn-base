package com.core;

import java.util.ArrayList;


public class AfPrizeInfo {

	//gift and ads list
	public ArrayList<GiftAds> giftAdsList = new ArrayList<GiftAds>();
	
	//gift list
	public ArrayList<Gift> giftList = new ArrayList<Gift>();
	public String pic;                 // 大图片
	public String thumb;               // 小图,缩略图
	
	//Address record
	public ArrayList<AddressRecord> addrRecord = new ArrayList<AddressRecord>();
	
	//Gifts exchange list
	public ArrayList<GiftsExchange> giftsExchangeList = new ArrayList<GiftsExchange>();
	
	//Payment recharges
	public PaymentInfo paymentInfo=null;
	
	//get sms information
	public ArrayList<SmsInfo> smsInfo = new ArrayList<SmsInfo>();
	
	//get the prize notify information
	//public ArrayList<PrizeNotify> prizeNotify = new ArrayList<PrizeNotify>();
	
	//get the recharges notify information
	public ArrayList<RechargeNotify> rechargeNotify = new ArrayList<RechargeNotify>();
	
	
	
	
	
	
	//======================custom class=========================
	public AfPrizeInfo(){}
	public class Gift{
//		public String name;                                  // 礼物名称
//		public String picurl;                                // 礼物图片url
//		public long exchangeDeadline;                       // 兑换截止日期
//		public String rankingRange;                          // 兑换此礼物需要的排行范围
//		public int num;                                    // 数量
//		public int orderNumber;                            // 排序
		
		public String name;                                  // 礼物名称
		public int rankingRangeStart;                        // 兑换此礼品需要的排行范围开始值
		public int rankingRangeEnd;                        // 兑换此礼品需要的排行范围结束值
		public Gift(){}
	}
	@SuppressWarnings("unused")
	private void addGift(String name,int start,int end)
	{
		Gift gift = new Gift();
		gift.name=name;
		gift.rankingRangeStart=start;
		gift.rankingRangeEnd=end;
		giftList.add(gift);
	}
	//==================================================================
	
	public class GiftAds{
		public String name;                               // 礼物广告名称
		public String picurl;                           // 礼物广告图片url
		public String url;                              // 广告链接url
		public int orderNumber;                       // 排序
		public GiftAds(){}
	}
	@SuppressWarnings("unused")
	private void setGifTads(String name,String picurl,String url,int orderNumber)
	{
		GiftAds gif = new GiftAds();
		gif.name=name;
		gif.picurl=picurl;
		gif.url=url;
		gif.orderNumber=orderNumber;
		giftAdsList.add(gif);
	}
	//====================================================================
	
	public class AddressRecord{
		public String country;                             // 国家
		public String province;                            // 省份
		public String city;                                // 城市
		public String consignee;                           // 收货人
		public String phone;                               // 联系电话
		public String firstName;                           // 第一联系人,O
		public String lastName;                           // 最后联系人,O
		public String address;                           // 详细地址,O
		public String landmark;                           // 备注,O
		public AddressRecord(){}
	}
	@SuppressWarnings("unused")
	private void setAddress(String country,String province,String city,String consignee,String phone,String firstName,String lastName,String address,String landmark)
	{
		AddressRecord addr = new AddressRecord();
		addr.country=country;
		addr.province=province;
		addr.city=city;
		addr.consignee=consignee;
		addr.phone=phone;
		addr.firstName=firstName;
		addr.lastName=lastName;
		addr.address=address;
		addr.landmark=landmark;
		addrRecord.add(addr);
	}
	//====================================================================
	
	public class GiftsExchange{
		public int id;                                    // id
		public String giftName;                             // 礼物名字
		public String level;                                // 等级
		public long createTime;                            // 创建时间
		public int status;                                // 发货状态（实物奖品:1:未填写地址，2：已填写地址，未发货 3：已发货） 虚拟奖品：1 未申请充值 2 已申请充值 3 充值成功 4 充值失败
		public int type;                                 // 礼物类型:1虚拟礼品官网兑换，2实物礼品
		public String url;                                  // 虚拟礼品 兑换url地址, 实体礼物为空，(可选	)
		public int listType;                              // 榜单类型（1：周榜 2：月榜 3：年榜）
		public String listTime;                             // 榜单时间(例：周榜：07/01/2015-07/07/2015,月榜:07/2015)
		public long ranking;                               // 排行榜排名
		public String statusInfo;                          // 发货状态的文案
		public GiftsExchange(){}
	}
	@SuppressWarnings("unused")
	private void setExchange(int id,String giftName,String level,long createTime,int status,int type,String url,int listType,String listTime,long ranking,String statusInfo)
	{
		GiftsExchange gift = new GiftsExchange();
		gift.id = id;
		gift.giftName = giftName;
		gift.level = level;
		gift.createTime = createTime;
		gift.status = status;
		gift.type = type;
		gift.url = url;
		gift.listType = listType;
		gift.listTime = listTime;
		gift.ranking = ranking;
		gift.statusInfo = statusInfo;
		giftsExchangeList.add(gift);
	}
	//==================================================================
	
	public class PaymentInfo{
		public long balance;           // 用户剩余的coins
		public long recharge_pcoin;   // 充值coin,only REQ_PREDICT_BUY_POINTS_BYSMS
		public PaymentInfo(){}
	}
	@SuppressWarnings("unused")
	private void setPayment(long balance,long recharge_pcoin)
	{
		paymentInfo = new PaymentInfo();
		paymentInfo.balance=balance;
		paymentInfo.recharge_pcoin=recharge_pcoin;
	}
	//====================================================================
	
	public class SmsInfo{
		public String sn;                                  // 短信流水号
		public String port;                                // 短信发送端口
		public String sms_content;                         // 發送短信內容
		public String content;                             // 发送提示内容
		public String price;                               // 短信当地价格
		public String currency;                            // 当地货币单位
		public String pcoin;                               // 短信充值的虚拟币值
		public SmsInfo(){}
	}
	@SuppressWarnings("unused")
	private void setSmsInfo(String sn,String port,String sms_content,String content,String price,String currency,String pcoin)
	{
		SmsInfo sms = new SmsInfo();
		sms.sn=sn;
		sms.port=port;
		sms.sms_content=sms_content;
		sms.content=content;
		sms.price=price;
		sms.currency=currency;
		sms.pcoin=pcoin;
		smsInfo.add(sms);
	}
	//================================================================
	
//	public class PrizeNotify{
//		public int _id;								//key id
//		public String toAfId;							//recieved afid
//		public String actiontitle;						//title
//		public String actionId ;						//action id 
//		public String desc;								//description
//		public int	points;								//point
//		public int status;								//msg status
//		public PrizeNotify(){}
//	}
//	@SuppressWarnings("unused")
//	private void setPrizeNotify(int _id,String toAfId,String actiontitle,String actionId,String desc,int points,int status)
//	{
//		PrizeNotify notify = new PrizeNotify();
//		notify._id=_id;
//		notify.toAfId=toAfId;
//		notify.actiontitle=actiontitle;
//		notify.actionId=actionId;
//		notify.desc=desc;
//		notify.points=points;
//		notify.status=status;
//		prizeNotify.add(notify);
//	}
	//===============================================================
	
	public class RechargeNotify{
		public int _id;								//key id
		public String toAfId;							//recieved afid
		public String pay_money;						//pay money
		public String gift_money ;						//gift money 
		public String currency;							//currency
		public String pay_gateway;						//pay gateway
		public String pay_points;						//pay points
		public String gift_points;						//gift points
		public int status;								//msg status
		public RechargeNotify(){}
	}
	@SuppressWarnings("unused")
	private void setRechargeNotify(int _id,String toAfId,String pay_money,String gift_money,String currency,String pay_gateway,String pay_points,String gift_points,int status)
	{
		RechargeNotify notify = new RechargeNotify();
		notify._id=_id;
		notify.toAfId=toAfId;
		notify.pay_money=pay_money;
		notify.gift_money=gift_money;
		notify.currency=currency;
		notify.pay_gateway=pay_gateway;
		notify.pay_points=pay_points;
		notify.gift_points=gift_points;
		notify.status=status;
		rechargeNotify.add(notify);
	}
	
}
