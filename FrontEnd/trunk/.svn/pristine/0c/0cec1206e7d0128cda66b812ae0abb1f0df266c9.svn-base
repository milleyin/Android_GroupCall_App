package com.core;

import java.util.ArrayList;

public class AfLottery {

	//Lottery Initialization informations:
	public LotteryInit lotteryInit=null;
	
	//action data
	public ArrayList<PredictActions> action_list = new ArrayList<PredictActions>();  //action list
	public ArrayList<PredictOdds> odds_list = new ArrayList<PredictOdds>();    //odd list
	public int newActionId = 0;   //  new actionId
	
	//prepBet sureBet getPoints
	public BetInfo betInfo;
	
	//Bet Record
	public ArrayList<BetHistory> betHistory = new ArrayList<BetHistory>();
	
	//Ranking
	public ArrayList<Ranking> rankingList = new ArrayList<Ranking>();
	public long currentRanking;
	public long winPoints;
	public long rankingsVersion;
	
	//Points detail 
	public ArrayList<PointsDetail> pointsDetailList = new ArrayList<PointsDetail>();
	
	//Announcement公告
	public ArrayList<Announcement> announcementList = new ArrayList<Announcement>();
	
	
	// friendlist REQ_PREDICT_INVITEFRIENDS or REQ_PREDICT_RECOMMENDFRIENDS
	// 包含rankinglist
	public long         inviteFriendsVersion; // inviteFriendVer,only REQ_PREDICT_INVITEFRIENDS
	public int          total;                // totalcount, only REQ_PREDICT_INVITEFRIENDS
	
	// get coin
	public GetCoinsResp getconiresp ;
	
	
	
	
	
	//======================custom class=========================
	public AfLottery(){	}
	public class LotteryInit{
		public ArrayList<LotteryInfo> lottery_list = new ArrayList<LotteryInfo>();
		public int flag;			// flag
		public long win;			// win
		public long points;       // user points
		public long maxPoints;   // max points pertime
		public int giftExchangeFlag; // 1：有可兑换的礼物；0：无可兑换的礼物；
		public ArrayList<PayInfo> payInfo_list = new ArrayList<PayInfo>(); // a list of payinfo
		public LotteryInit(){}
	}
	@SuppressWarnings("unused")
	private void setInit(int flag,long win,long points,long maxPoints,int giftExchangeFlag){
		if(lotteryInit==null)
			lotteryInit = new LotteryInit();
		lotteryInit.flag=flag;
		lotteryInit.win=win;
		lotteryInit.points=points;
		lotteryInit.maxPoints=maxPoints;
		lotteryInit.giftExchangeFlag=giftExchangeFlag;
	}
	//========
	public class PointsInfo{
		public int coins;           // 购买所需充值的Coins
		public int extra;           // 赠送额外point点数, 单位%, 如客户购买100点, 赠送额外点数为10%, 客户最后获得点数为110
		public long points;          // 购买所得到的Points
		public PointsInfo(){}
	}
	public class PayInfo{
		public String code;
		public String paytypename;
		public String sname;
		//public int extra;
		public String front_url;
		public String front_url1;
		public ArrayList<PointsInfo> pointsInfo_list = new ArrayList<PointsInfo>(); // a list of pointsinfo
		public PayInfo(){}
	}
	private PayInfo pInfo=null;
	@SuppressWarnings("unused")
	private void setPayInfo(String code,String paytypename,String sname,String f_url,String f_url1)
	{
		pInfo = new PayInfo();
		pInfo.code=code;
		pInfo.paytypename=paytypename;
		pInfo.sname=sname;
		pInfo.front_url=f_url;
		pInfo.front_url1=f_url1;
		//info.pointsInfo_list.add(object)
		//lotteryInit.payInfo_list.add(pInfo);
	}
	/**
	 * JustFor test
	 * @return
	 */
	public PayInfo addPayInfo_test(){
		PayInfo _pi= new PayInfo();
		_pi.code="P0003";
		_pi.paytypename="COIN";
		_pi.sname="Recharge with Coins";
		_pi.front_url="http://ael-cs.palm-chat.cn:8088/payment/palmguess/coin/";
		PointsInfo _p=new PointsInfo();
		_p.coins= 300 ;
		_p.points= 3000 ;
		_p.extra= 10 ;
		_pi.pointsInfo_list.add(_p);
		
		_p=new PointsInfo();
		_p.coins= 300 ;
		_p.points= 3000 ;
		_p.extra= 10 ;
		_pi.pointsInfo_list.add(_p);
		
		_p=new PointsInfo();
		_p.coins= 500 ;
		_p.points= 5000 ;
		_p.extra= 15 ;
		_pi.pointsInfo_list.add(_p);
		
		_p=new PointsInfo();
		_p.coins= 1000 ;
		_p.points= 10000 ;
		_p.extra= 20 ;
		_pi.pointsInfo_list.add(_p);
		return _pi;
	}
	@SuppressWarnings("unused")
	private void setPointsInfo(int coins,int extra,long points)
	{
		PointsInfo info = new PointsInfo();
		info.coins=coins;
		info.extra=extra;
		info.points=points;
		pInfo.pointsInfo_list.add(info);
	}
	@SuppressWarnings("unused")
	private void addPayInfo()
	{
		//if (pInfo != null) {
			lotteryInit.payInfo_list.add(pInfo);
			pInfo = null;
		//}
	}
	
	//=====
	public class LotteryInfo{
		public int id;                                 // 抽奖ID
		public long points;                             // 签到中奖points
		public int probability;                        // 中奖几率
		public LotteryInfo(){}
	}
	@SuppressWarnings("unused")
	private void addInfo(int id,long points,int probability)
	{
		LotteryInfo info = new LotteryInfo();
		info.id=id;
		info.points=points;
		info.probability=probability;
		//lottery_list.add(info);
		lotteryInit.lottery_list.add(info);
	}
	//===========================================================
	
	public class PredictActions{
		public int         actionId;                           // 比赛或竞猜活动ID
		public String      title;                             // 比赛标题或竞猜问题
		public int         type;                               // 活动类别(1: 比赛, 2: 竞猜)
		public int      phoneCountryCode;                  // 可参与活动的电话号码国码(如86)
		public String      picUrl;                            // 比赛或竞猜图片URL
		public int         status;                             // 活动状态(1:已录入但未审核, 2: 已录入但审何不通过, 3: 已录入审核通过(进行中),
		//          4:异常终止但未审核,5: 异常终止但审何不通过, 6:异常终止审核通过,
		//          7:结束活动派彩但未审核, 8:结束活动派彩但审核未通过, 9:结束活动派彩审核已通过)
		public long       startTime;                          // 比赛或竞猜活动开始时间,时间戳类型
		public long       StopTime;                           // 停止下注时间, 时间戳类型
		public String    teamName1;                          // 比赛队伍1名称,当活动类别为1时必须
		public String    teamPicUrl1;                        // 比赛队伍1图像地址,当活动类别为1时必须
		public String    teamName2;                          // 比赛队伍2名称,当活动类别为1时必须
		public String    teamPicUrl2;                        // 比赛队伍2图像地址,当活动类别为1时必须
		public ArrayList<PredictOddsItem> oddlist;           // 赔率列表
		public PredictActions(){}
	}
	public class PredictOddsItem{
		public int       oddId;                              // 赔率ID
		public String    oddItem;                           // 赔率标题
		public String    odds;                                // 赔率值
		public int number;                                 // 已下注人数
		public String rate;                                 // 已下注人数
		public PredictOddsItem(){}
	}
	public class PredictOdds{
		public int       actionId;                           // 比赛或竞猜活动ID
		public ArrayList<PredictOddsItem> oddlist;           // 赔率列表
		public PredictOdds(){}
	}
	
	@SuppressWarnings("unused")
	private void addActions(int actionId,String title,int type,int phoneCountryCode,String picUrl,int status,long startTime,long StopTime,String teamName1,String teamPicUrl1,String teamName2,String teamPicUrl2)
	{
		PredictActions action = new PredictActions();
		action.actionId = actionId;
		action.title = title;
		action.type=type;
		action.phoneCountryCode = phoneCountryCode;
		action.picUrl = picUrl;
		action.status = status;
		action.startTime=startTime;
		action.StopTime=StopTime;
		action.teamName1=teamName1;
		action.teamPicUrl1=teamPicUrl1;
		action.teamName2=teamName2;
		action.teamPicUrl2=teamPicUrl2;
		
		tempItem = new ArrayList<PredictOddsItem>();
		action.oddlist = tempItem;
		action_list.add(action);
	}
	private ArrayList<PredictOddsItem> tempItem = new ArrayList<PredictOddsItem>();    //odd item
	@SuppressWarnings("unused")
	private void addOddsItem(int oddId,String oddItem,String odds,int number,String rate)
	{
		PredictOddsItem odds1 = new PredictOddsItem();
		odds1.oddId = oddId;
		odds1.oddItem = oddItem;
		odds1.odds = odds;
		odds1.number = number;
		odds1.rate = rate;
		tempItem.add(odds1);
	}
	
	@SuppressWarnings("unused")
	private void addOdds(int actionId)
	{
		PredictOdds odds1 = new PredictOdds();
		odds1.actionId = actionId;
		tempItem = new ArrayList<PredictOddsItem>();
		odds1.oddlist = tempItem;
		odds_list.add(odds1);
	}

	//===========================================================
	
	public class BetInfo {
		public String afid; // afid (only get point)
		public String actionId; // id of action (only prep bet)
		public String itemId; // id of item (only prep bet)
		public String odds; // odds (only prep bet)
		public int expire;  // 同一活动下次下注剩余时间，单位秒(only prep bet)
		public long points; // points min : 1 max :1000000 (only prep bet | get  points)
		public BetInfo() {}
	}
	@SuppressWarnings("unused")
	private void addBetInfo(String afid,String actionId,String itemId,String odds,int expire,long points)
	{
		betInfo = new BetInfo();
		betInfo.afid=afid;
		betInfo.actionId=actionId;
		betInfo.itemId=itemId;
		betInfo.odds=odds;
		betInfo.expire=expire;
		betInfo.points=points;
	}
	//===========================================================
	
	public class BetHistory{
		public int actionId;                           // 比赛或竞猜活动ID 　
		public int actionType;                         // 比赛类型:1比赛，2竟猜
		public String actiontitle;                       // 比赛标题或竞猜问题
		public long gain;                               // 结果是赢多少points或输多少-points
		public long inputPoints;                        // 下注的points
		public String item;                              // 选择下注时的结果
		public String odds;                              // 下注时的赔率
		public String resultsId;                         // 比赛结果ID
		public String results;                           // 比赛结果
		public int status;                             // 状态：1比赛还没开始，2比赛正在进行等待结果，3已经结束
		public BetHistory(){}
	}
	@SuppressWarnings("unused")
	private void addBetRecord(int actionId,int actionType,String actiontitle,long gain,long inputPoints,String item,String odds,String resultsId,String results,int status)
	{
		BetHistory record = new BetHistory();
		record.actionId=actionId;
		record.actionType=actionType;
		record.actiontitle=actiontitle;
		record.gain=gain;
		record.inputPoints=inputPoints;
		record.item=item;
		record.odds=odds;
		record.resultsId=resultsId;
		record.results=results;
		record.status=status;
		betHistory.add(record);
	}
	//===========================================================
	
	public class Ranking{
		public String afid;                               // afid
		public String name;                          // 昵称
		public String local_img_path;                        // 头像地址
		public int winrate;                                // 胜场
		public long points;                             // points
		public int  sex;                                // 性别 AFMOBI_SEX_MALE|AFMOBI_SEX_FEMALE|AFMOBI_SEX_FEMALE_AND_MALE
		public Ranking(){}
	}
	@SuppressWarnings("unused")
	private void addRanking(String afid,String name,String local_img_path,int winrate,long points,int sex)
	{
		Ranking ranking = new Ranking();
		ranking.afid=afid;
		ranking.name=name;
		ranking.local_img_path=local_img_path;
		ranking.winrate=winrate;
		ranking.points=points;
		ranking.sex = sex;
		rankingList.add(ranking);
	}
	//==========================================================
	
	public class PointsDetail{
		public String ptype;                              //  points操作类型1,2,3,4,5,6,7,8,9,10,11,12
		public String ptypeDesc;                          // points操作类型说明1 : 登录赠送, 2 : 获奖, 3 : 下注, 4 : 取消比赛, 5 : 消费, 
		                                                  //                   6 : 冲值赠送, 7 : page冲值, 8 : 短信冲值, 9 : coins冲值, 10 : 点卡冲值
		                                                  //                   11:Referral bonus,12:Invited bonus   
		public String actionTitle;                       // 说明, 下注的比赛标题
		public String item;                              // 下注的选项
		public long points;                             // 操作的points数量
		public long inTime;                             // 操作时的时间戳
		public PointsDetail(){}
	}
	@SuppressWarnings("unused")
	private void setPoints(String ptype, String ptypeDesc,String actionTitle,String item,long points,long inTime)
	{
		PointsDetail point = new PointsDetail();
		point.ptype=ptype;
		point.ptypeDesc=ptypeDesc;
		point.actionTitle=actionTitle;
		point.item=item;
		point.points=points;
		point.inTime=inTime;
		pointsDetailList.add(point);
	}
	//====================================================================
	
	public class Announcement{
		public int id;                                 // Announcement id
		public String content;                           // content
		public String createAccount;                     // 通知创建人帐号
		public long createTime;                         // 创建时间
		public long expired;                            // 到期时间戳
		public Announcement(){}
	}
	@SuppressWarnings("unused")
	private void setAnnouncement(int id, String content,String createAccount,long createTime,long expired)
	{
		Announcement ann = new Announcement();
		ann.id=id;
		ann.content=content;
		ann.createAccount=createAccount;
		ann.createTime=createTime;
		ann.expired=expired;
		announcementList.add(ann);
	}
	//==================================================================
	
	public class GetCoinsResp{
		public long balance;                    // 用户账户剩余的COINS
		public String symbol;                   // 货币符号
		public String code;                     // 货币标准符号，如 NGN
		public String full;                     // 货币名称，如奈拉、Nigerian Naira(与用户设置的语言种类相关)
		public String desc;                     // PalmCoin用途说明文字
		public String title;                    // 活动简介
		public String url;                      // 活动展示页面的 UR
		public GetCoinsResp(){}
	}
	
	@SuppressWarnings("unused")
	private void setCoinsResp(long balance, String symbol,String code,String full,String desc,String title,String url)
	{
		if(getconiresp == null)
		{
			getconiresp = new GetCoinsResp();
		}
		getconiresp.balance = balance;
		getconiresp.symbol = symbol;
		getconiresp.code = code;
		getconiresp.full = full;
		getconiresp.desc = desc;
		getconiresp.title = title;
		getconiresp.url = url;
	}
	
}
