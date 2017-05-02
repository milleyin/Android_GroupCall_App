package com.core;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.core.AfNewPayment.AFAirtimeTopupReq;
import com.core.AfNewPayment.AFBillHistoryResp;
import com.core.AfNewPayment.AFCoin2GoodsResp;
import com.core.AfNewPayment.AFGetCoinOrderHistoryResp;
import com.core.AfNewPayment.AFMoney2CoinsResp;
import com.core.AfNewPayment.AFRechargeOrderReq;
import com.core.AfResponseComm.AfBCGetRegionBroadcastResp;
import com.core.AfResponseComm.AfBCPriefInfo;
import com.core.AfResponseComm.AfBroadCastTagInfo;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfPeoplesChaptersList;
import com.core.AfResponseComm.AfPulishInfo;
import com.core.AfResponseComm.AfTagGetDefaultTagsResp;
import com.core.AfResponseComm.AfTagGetDefaultTrend;
import com.core.AfResponseComm.AfTagGetLangPackageResp;
import com.core.AfResponseComm.AfTagGetTagsResp;
import com.core.AfResponseComm.AfTagGetTrendsMoreResp;
import com.core.AfResponseComm.AfTagGetTrendsResp;
import com.core.AfResponseComm.AfTagInfo;
import com.core.AfResponseComm.AfTagShareTagOrBCResp;
import com.core.cache.CacheManager;
import com.core.contact.ContactAPI;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;
import com.core.param.AfNearByGpsParam;
import com.core.param.AfRegInfoParam;
import com.core.param.AfSearchUserParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

public class test implements AfHttpResultListener, AfHttpProgressListener {
	private AfPalmchat mAfCorePalmchat;
	
	public test(AfPalmchat core){
		mAfCorePalmchat = core;

		this.readDbDataTest();

	}
	public int AfStoreGetProdList(){
		return mAfCorePalmchat.AfStoreGetProdList(0, 20, Consts.PROD_TYPE_EXPRESS_WALLPAPER, Consts.PROD_CATEGORY_EXPRESS);
	}
	public void AfGetmd5Test(){
		String url = "http://54.229.2.164:8088/android/C8001_a0.png";
			
		String md5 = mAfCorePalmchat.AfGetmd5(url.getBytes());
		String l = md5;
	}
	
	public void AfLoginEncodeTest(){
		AfLoginInfo[] myAccounts = mAfCorePalmchat.AfDbLoginGetAccount();
		if(myAccounts != null && myAccounts.length > 0)
		{
			AfLoginInfo afLoginInfo = myAccounts[0];
			String[] serverinfo = mAfCorePalmchat.AfHttpGetServerInfo();
			
			if(serverinfo.length > 3)
			{
				String encodestr = mAfCorePalmchat.AfLoginEncode(afLoginInfo.password, serverinfo[2]);
				
				System.out.println("ywp: AfLoginEncodeTest success " + encodestr);
				
			}
			
		}
			
		
	}
	
	
	public int AfHttpGetRegRandomTest(){
		return mAfCorePalmchat.AfHttpGetRegRandom("15221278385", "86", Consts.HTTP_RANDOM_REGISTER,null,this);
	}
	public int AfHttpStatisticTest(){
		return mAfCorePalmchat.AfHttpStatistic(true, true, "{\"ProductName\":\"Customer's}", null, this);
	}
	
	public int AfSendImageTest(){
		
		AfImageReqInfo param = new AfImageReqInfo();		
		
		param.file_name = "s03.jpg";
		param.path = Environment.getExternalStorageDirectory().getPath() + "/03.jpg";
		param.send_msg = "a1902418";
		param.recv_afid="a1902418";	
	
		int handle =  mAfCorePalmchat.AfHttpSendImage(param, 0, this, this);
		System.out.println("ywp: AfSendImageTest: handle = " + handle);
		return handle;
	}
	public int AfDbMessage(){
		AfMessageInfo msg = new AfMessageInfo();

		msg.attach_id = 1;
		msg.client_time = 12233;
		msg.server_time = 123456;
		msg.fromAfId = "a485566";
		msg.msg = "content";
		msg.toAfId = "1afasdf";
		msg.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_TEXT;

		msg._id = mAfCorePalmchat.AfDbMsgInsert(msg);
		System.out.println("ywp: AfDbMessage:AfDbMsgInsert = " + msg._id);
		if( msg._id >= 0){
			AfMessageInfo tmp = mAfCorePalmchat.AfDbMsgGet(msg._id);
			
			System.out.println("ywp: AfDbMsgGet success " + msg._id);
			tmp.attach_id = 2;
			tmp.status = 122;
			tmp.client_time = 96652;
			tmp.server_time = 98455;
			
			if( mAfCorePalmchat.AfDbMsgUpdate(tmp) >= Consts.AFMOBI_DATABASE_RESULT_OK){
				System.out.println("ywp: AfDbMsgUpdate: success msg id = " +tmp._id);
			}
		}
		
		mAfCorePalmchat.AfDbMsgSetStatus(msg._id, 100);
		if( mAfCorePalmchat.AfDbMsgRmove(msg._id) == Consts.AFMOBI_DATABASE_RESULT_OK)
		{
			System.out.println("ywp: AfDbMsgRmove: success ");
		}

		return 1;
	}
	
	
	public int AfDbAttachVoice(){
		AfAttachVoiceInfo cur = new AfAttachVoiceInfo();

		cur.file_name = "fadfsaf";
		cur.file_size = 123;
		cur.voice_len = 5;
		cur._id = mAfCorePalmchat.AfDbAttachVoiceInsert(cur);
		System.out.println("ywp: AfDbAttachVoiceInsert: msg id = " + cur._id);
		
		if( mAfCorePalmchat.AfDbAttachVoiceRmove(cur._id) >= Consts.AFMOBI_DATABASE_RESULT_OK)
		{
			System.out.println("ywp: AfDbAttachVoiceRmove: success ");
		}

		return 1;
	}
	
	
	public int AfDbAttachImage(){
		AfAttachVoiceInfo cur = new AfAttachVoiceInfo();

		cur.file_name = "fadfsaf";
		cur.file_size = 123;
		cur.voice_len = 5;
		cur._id = mAfCorePalmchat.AfDbAttachVoiceInsert(cur);
		System.out.println("ywp: AfDbAttachVoiceInsert: msg id = " + cur._id);
		
		if( mAfCorePalmchat.AfDbAttachVoiceRmove(cur._id) == Consts.AFMOBI_DATABASE_RESULT_OK)
		{
			System.out.println("ywp: AfDbAttachVoiceRmove: success ");
		}

		return 1;
	}
	
	public int AfHttpRegisterTest() {
		
		AfRegInfoParam param = new AfRegInfoParam();
		param.cc="86";
		param.imei="";
		param.imsi=null;
		param.sex=Consts.AFMOBI_SEX_FEMALE;
	
		param.region="Others";
		param.birth="2000-12-12";
		param.phone_or_email="ywp1@qq.com";
	//	param.phone_or_email="13164734370";
		param.name="jinde123456";
		param.country="China";
		param.city="Shenzhen";
		param.password="123456";
		param.user_ip=null;
		param.voip=null;
		
		int handle = mAfCorePalmchat.AfHttpRegister(param, Consts.REQ_REG_BY_EMAIL, 0, this);;
		return handle;
	}
	
	public int AfHttpChangPwdTest(){
		return mAfCorePalmchat.AfHttpChangPwd("123456" , "111111", null, 0, this);
	}	

	public int AfHttpBindPhoneTest(){
		return mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_BIND_BY_PHONE, "15221278389", "86", "a8328654", null, null, 0, this);
	}
	public int AfHttpBindEmailTest(){
		return mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_BIND_BY_EMAIL, "ywp2@qq.com", null, "T", null, null, 0, this);
	}
	public int AfHttpResetPwdByPhoneTest(){
		return mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_RESET_PWD_BY_PHONE, "15221278389", "460", null, null, null, 0, this);
	}
	public int AfHttpResetPwdByEmailTest(){
		return mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_RESET_PWD_BY_EMAIL, "ywp2@qq.com", null, null, null, null, 0, this);
	}
	public int AfHttpCheckAccoutByEmailTest(){
		return mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_CHECK_ACCOUNT_BY_EMAIL, "ywp2@qq.com", null, null, null, null, 0, this);
	}
	
	public int AfHttpCheckAccoutByPhoneTest(){
		return mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_CHECK_ACCOUNT_BY_PHONE, "13533352498", "86", null, null,null, 0, this);
	}
	public int AfHttpLogoutTesst(){
		return mAfCorePalmchat.AfHttpLogout(0, this);
	}
	public int AfHttpGetProfileTest(){
		String afid[] = new String[1];
		
		afid[0] = new String("a2504056");
		
		return mAfCorePalmchat.AfHttpGetInfo(afid, Consts.REQ_GET_INFO, null, 0, this);
	}
	public int AfHttpGetBatchProfileTest(){
		String afid[] = new String[2];
		
		afid[0] = new String("a2504056");
		afid[1] = new String("a8328654");
		
		return mAfCorePalmchat.AfHttpGetInfo(afid, Consts.REQ_GET_BATCH_INFO, null, 0, this);
	}
	public int AfHttpShakeAndShakeTest(){
		return mAfCorePalmchat.AfHttpShakeAndShake("S", 50, null, 0, this);
	}
	
	public int AfHttpFriendOpr(){
		return mAfCorePalmchat.AfHttpFriendOpr("all", "a8328654", Consts.HTTP_ACTION_A,
				Consts.FRIENDS_MAKE, (byte) Consts.AFMOBI_FRIEND_TYPE_FF, null, 0, this);	
	}

	public int AfHttpUpdateProfileTest(){
		AfProfileInfo profile = new AfProfileInfo();
		
		profile.sex = Consts.AFMOBI_SEX_FEMALE;
		profile.email = "yangwenping2@126.com";
		profile.name = "modify";
		profile.birth = "1985-9-7";
		profile.country = "FUZHOU1";
		profile.region = "jiangxi";
		profile.school = "linchuan";
		
		return mAfCorePalmchat.AfHttpUpdateInfo(profile, 0, this);
	}
	public int AfHttpPhonebookBackUPTest(){
		String pb_uuid[] = new String[2];
		String pb_name[] = new String[2];
		String pb_phone[] = new String[2];
		
		pb_uuid[0] = "a1902418";
		pb_uuid[1] = "a8328654";
		
		pb_name[0] = "huaqiang";
		pb_name[1] = "yangwenping";
		
		pb_phone[0] = "13533352498";
		pb_phone[1] = "15221278389";
		
		return mAfCorePalmchat.AfHttpPhonebookBackup(pb_name, pb_phone, Consts.HTTP_ACTION_B, 0, 50, 0, this);
	}

	public int AfDbProfileTest(){
		AfFriendInfo info = mAfCorePalmchat.AfDbProfileGet(Consts.AFMOBI_DB_TYPE_FRIEND, "a3097766");

		if( null != info)
		{
			info.age = 100;
			info.name = "name";
			info.region ="region";
			info.signature = "signature";
			info.status = "status";
			info.user_msisdn = "user_msisdn";
			
			mAfCorePalmchat.AfDbProfileUpdate(Consts.AFMOBI_DB_TYPE_FRIEND, info);			

			mAfCorePalmchat.AfDbProfileUpdateType(Consts.AFMOBI_DB_TYPE_FRIEND, info.afId);
			mAfCorePalmchat.AfDbProfileUpdateSid(1000, info.afId);

			info.age = 50;
			info.name = "name1";
			info.region ="region1";
			info.signature = "signature1";
			info.status = "status1";
			info.user_msisdn = "user_msisdn1";
			mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_FRIEND, info);
			
			if( mAfCorePalmchat.AfDbProfileSearch(info.afId)){
				mAfCorePalmchat.AfDbProfileRemove(Consts.AFMOBI_DB_TYPE_FRIEND, info.afId);
			}
		}
		return 1;
	}
	
	public int AfHttpNearByGpsTest(){
		AfNearByGpsParam param = new AfNearByGpsParam();
		
		param.req_type = Consts.REQ_NEAR_BY_GPS;
		param.limit = 50;
		param.mcc = -1;
		param.self_sex = Consts.AFMOBI_SEX_MALE;
		param.search_sex = Consts.AFMOBI_SEX_FEMALE;
		param.mnc = -1;
		param.lng = 113.9490;
		param.cid = -1;
		param.lac = -1;
		param.lat = 22.5429;
		
		return mAfCorePalmchat.AfHttpNearByGps(param, 0, this);
		
	}
	public int AfHttpLookAroundTest()
	{
		return 0;//mAfCorePalmchat.AfHttpLookAround("China", "Shenzhen", "Shenzhen", 0, 0, 50, null, 0, this);
	}
	public int AfHttpSearchUserTest()
	{
		AfSearchUserParam param = new AfSearchUserParam();
		
		param.limit= 50;
		param.country = "China";		
		return mAfCorePalmchat.AfHttpSearchUser(param,  0, this);
		
	}
	
	public int AfHttpSendMsgTest()
	{
		return mAfCorePalmchat.AfHttpSendMsg("a1902418", System.currentTimeMillis(), "123456", Consts.MSG_CMMD_NORMAL, 0, this);
	}
	public int AfHttpGrpOprTest(){
		
		String members = "a1902418,a1568854,a1675034,a2452792";
		String name = "ywp";
		int  req_flag= Consts.REQ_GRP_CREATE;
		
		return  mAfCorePalmchat.AfHttpGrpOpr(members, name, null, req_flag, null, this);
		
	}	

	public int AfHttpSendVoiceTest()
	{
		byte data[] = new String("fafdsasdfasdf").getBytes();
		
		return mAfCorePalmchat.AfHttpSendVoice("a1902418", System.currentTimeMillis(), data, data.length, 5, 0, this, this);
	}
	
	public int AfHttpAvatarUploadTest()
	{
		PalmchatLogUtils.i("test", "AfHttpAvatarUploadTest");
		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Test.JPG";
		String fileName = "Test.JPG";
		return mAfCorePalmchat.AfHttpAvatarUpload(filePath, fileName ,"JPEG",-1, null, this, this);
	}
	public int AfHttpAvatarDownloadTest()
	{
		PalmchatLogUtils.i("test", "AfHttpAvatarDownloadTest");
		return mAfCorePalmchat.AfHttpAvatarDownload("a1675034" ,"/sdcard/test.jpg", null, "64x64", 1,true, null, this, this);
	}
	
	public int AfHttpAvatarDownloadTest(AfHttpResultListener result ,AfHttpProgressListener progress)
	{
		PalmchatLogUtils.i("test", "AfHttpAvatarDownloadTest");
		return mAfCorePalmchat.AfHttpAvatarDownload("a1675034" ,"/sdcard/test.jpg", null, "64x64", 1,true, null, result, progress);
	}
	
	public int AfHttpAvatarDeleteTest()
	{
		PalmchatLogUtils.i("test", "AfHttpAvatarDeleteTest");
		return mAfCorePalmchat.AfHttpAvatarDelete("a1675034", null, 1, null, this);
	}
	public int AfHttpLoginStatus()
	{
		PalmchatLogUtils.i("test", "AfHttpLoginStatus");
		return mAfCorePalmchat.AfHttpLoginStatus(Consts.LOGIN_STATUS_FRONT, null, this);
	}
	
	public int AfDbMsgClearTest(){
		mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, "a2504056");
		mAfCorePalmchat.AfSetScreen(120, 160);
		return 1;
	}
	public int AfDbLoginInfoTest(){
		 AfLoginInfo[] tmp = mAfCorePalmchat.AfDbLoginGetAccount();
		 
		 if( null != tmp){
			 AfLoginInfo test = mAfCorePalmchat.AfDbLoginGet(Consts.AF_LOGIN_AFID, tmp[0].afid);
			 
			 if( null != test){
				 mAfCorePalmchat.AfDbLoginSetStatus(tmp[0].afid, 100);
				 mAfCorePalmchat.AfDbLoginRemove(tmp[0].afid);
				 mAfCorePalmchat.AfCoreOpenDatabase(tmp[0].afid);		 
			 }		 
		 }
		 return 1;		
	}
	public int AfHttpFindPwdAnswerTest(){
		
		String []question = new String[3];
		String []answer = new String[3];
		
		question[0] = "11";
		question[1] = "22";
		question[2] = "33";

		answer[0] = "111";
		answer[1] = "222";
		answer[2] = "333";
		
	//	return mAfCorePalmchat.AfHttpFindPwdAnswer(Consts.REQ_PSD_ANSWER, "a2504056", Consts.AF_LOGIN_AFID, question,  answer, null, this);
		return mAfCorePalmchat.AfHttpFindPwdGetQuestion( "a2504056", Consts.AF_LOGIN_AFID, null, this);	
	}
	
	public int AfHttpMutualFriendsTest(){
		return mAfCorePalmchat.AfHttpMutualFriends(null, this);
	}
	
	public int AfHttpFeedback(){
		return mAfCorePalmchat.AfHttpFeedback("a2504056", "yangwenping", null, this);
	}
	
	public int AfDbAttachImageInsertTest(){
		int id;
		
		AfAttachImageInfo attach = new AfAttachImageInfo();

		attach.small_file_name = "820899156";
		attach.small_file_size = 2212;
		attach.large_file_size = 15204;
		attach.url = "palmchat://54.246.154.122:34599/20131105/19344a8572/23a3c3d7b82a475faadfa799dc02dd7dc0b94c0f/E@9AEZFE35PKCVWYRWI`QFA.jpg";
		
		id = mAfCorePalmchat.AfDbAttachImageInsert(attach);
	
		PalmchatLogUtils.e("test", "AfDbAttachImageInsertTest: id = " + id);
		return id;
	}
		
	public int ContactAPITest(){
		ContactAPI.getAllContact();
		return 1;
	}
	
	 public static long getmem_UNUSED(Context mContext) {
	        long MEM_UNUSED;
		// 得到ActivityManager
	        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

		// 创建ActivityManager.MemoryInfo对象  

	        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
	        am.getMemoryInfo(mi);

		// 取得剩余的内存空间 

	        MEM_UNUSED = mi.availMem / 1024;
	        return MEM_UNUSED;
	    }
	
	@Override
	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		
		System.out.println("ywp: AfOnResult httpHandle =" + httpHandle +  " flag=" + flag + " code=" + code);
		
		if( code != Consts.REQ_CODE_SUCCESS){
			return;
		}
		
		// TODO Auto-generated method stubl
		switch(flag){
		case Consts.REQ_FLAG_VERSION_CHECK:
		{
			AfVersionInfo orderid = (AfVersionInfo)result;
			System.out.println("ywp: AfOnResult: REQ_FLAG_VERSION_CHECK_GOOGLE  success: "+flag);
		}
		break;
		case Consts.REQ_BCGET_REGION_BROADCAST:
		{
			AfBCGetRegionBroadcastResp resp = (AfBCGetRegionBroadcastResp) result;
			
			System.out.println("yx: AfOnResult: REQ_BCGET_REGION_BROADCAST  success: "+flag);
		}
		break;
		case Consts.REQ_BCGET_LANGUAGE_PACKAGE:
		{
			AfTagGetLangPackageResp afResponseComm = (AfTagGetLangPackageResp) result;
			int ret = 0;
			long time =  System.currentTimeMillis();
			long time1 =  System.currentTimeMillis();
			long memleave = getmem_UNUSED(PalmchatApp.getApplication());
			if(afResponseComm.default_list != null)
			{   // 
				String[] array = (String[])afResponseComm.default_list.toArray(new String[afResponseComm.default_list.size()]);
				time1 =  System.currentTimeMillis();
				ret = mAfCorePalmchat.AfDbBCLangPackageListInsert(array, (byte)0);
				time1 = System.currentTimeMillis()-time1;
			}
			
			time = System.currentTimeMillis()-time;
			if(afResponseComm.local_list != null)
			{   // 
				String[] array = (String[])afResponseComm.local_list.toArray(new String[afResponseComm.local_list.size()]);
				ret = mAfCorePalmchat.AfDbBCLangPackageListInsert(array, (byte)1);
			}
			
			memleave = getmem_UNUSED(PalmchatApp.getApplication())-memleave;
			
			time1 = System.currentTimeMillis();
			AfTagGetLangPackageResp dbResp = mAfCorePalmchat.AfDbBCLangPackagGetLimitListDb(1,0,100);
			AfTagGetLangPackageResp dbResp1 = mAfCorePalmchat.AfDbBCLangPackagGetLimitListDb(0,0,100);
			AfTagGetLangPackageResp dbResp2 = mAfCorePalmchat.AfDbBCLangPackageListSearch("#a",0,100);
			time1 = System.currentTimeMillis()-time1;
			//mAfCorePalmchat.AfDbBCLangPackageListDeleteAll();
			
			dbResp = mAfCorePalmchat.AfDbBCLangPackageListSearch("#abc",0, 100);
		
			
			System.out.println("yx: AfOnResult: REQ_BCGET_LANGUAGE_PACKAGE  success: "+flag);
		}
		break;
		case Consts.REQ_BCMSG_PUBLISH:
		{
			AfResponseComm afResponseComm = (AfResponseComm) result;
			AfPulishInfo afPulishInfo = (AfPulishInfo) afResponseComm.obj;
			
			System.out.println("yx: AfOnResult: REQ_BCMSG_PUBLISH  success: "+flag);
		}
		break;
		case Consts.REQ_BCGET_COMMENTS_BY_TAGNAME:
		case Consts.REQ_BCGET_RECENT_HOTS_BY_TAGNAME:
		case Consts.REQ_BCGET_COMMENTS_BY_STATE_OR_CITY:
		{
			AfResponseComm afResponseComm = (AfResponseComm) result;
			AfPeoplesChaptersList afPeoplesChaptersList= (AfPeoplesChaptersList) afResponseComm.obj;
			int res_total = afPeoplesChaptersList.res_total;
			
			System.out.println("yx: AfOnResult: REQ_BCGET_COMMENTS_BY_TAGNAME  success: "+flag);
		}
		break;
		
		case Consts.REQ_BCGET_DEFAULT_TAGS:
		{
			AfTagGetDefaultTagsResp orderid = (AfTagGetDefaultTagsResp)result;
			System.out.println("ywp: AfOnResult: REQ_PAYMENT_GET_RECHARGE_ORDERS_LOGS  success: "+flag);
		}
		break;
		case Consts.REQ_BC_SHARE_BROADCAST:
		case Consts.REQ_BC_SHARE_TAG:
		{
			AfTagShareTagOrBCResp orderid = (AfTagShareTagOrBCResp)result;
			System.out.println("ywp: AfOnResult: REQ_PAYMENT_GET_RECHARGE_ORDERS_LOGS  success: "+flag);
		}
		break;
		
		case Consts.REQ_BCGET_HOT_TAGS:
		case Consts.REQ_BCGET_SEARCH_TAGS:
		{
			AfTagGetTagsResp orderid = (AfTagGetTagsResp)result;
			System.out.println("ywp: AfOnResult: REQ_PAYMENT_GET_RECHARGE_ORDERS_LOGS  success: "+flag);
		}
		break;
		case Consts.REQ_BCGET_TRENDS:
		{
			AfTagGetTrendsResp orderid = (AfTagGetTrendsResp)result;
			System.out.println("ywp: AfOnResult: REQ_PAYMENT_GET_RECHARGE_ORDERS_LOGS  success: "+flag);
		}
		break;
		case Consts.REQ_BCGET_TRENDS_MORE:
		{
			AfTagGetTrendsMoreResp orderid = (AfTagGetTrendsMoreResp)result;
			System.out.println("ywp: AfOnResult: REQ_PAYMENT_GET_RECHARGE_ORDERS_LOGS  success: "+flag);
		}
		break;
		case Consts.REQ_PREDICT_RECOMMENDFRIENDS:
		{
			AfLottery orderid = (AfLottery)result;
			System.out.println("ywp: AfOnResult: REQ_PAYMENT_GET_RECHARGE_ORDERS_LOGS  success: "+flag);
		}
		break;
		case Consts.REQ_PREDICT_INVITEFRIENDS:
		{
			AfLottery orderid = (AfLottery)result;
			System.out.println("ywp: AfOnResult: REQ_PAYMENT_GET_RECHARGE_ORDERS_LOGS  success: "+flag);
		}
		break;
		case Consts.REQ_PAYMENT_GET_RECHARGE_ORDERS_LOGS:
		{
			AFGetCoinOrderHistoryResp orderid = (AFGetCoinOrderHistoryResp)result;
			System.out.println("ywp: AfOnResult: REQ_PAYMENT_GET_RECHARGE_ORDERS_LOGS  success: "+flag);
		}
		break;
		case Consts.REQ_PALM_COIN_GET_BILL_HISTORY:
		{
			AFBillHistoryResp orderid = (AFBillHistoryResp)result;
			System.out.println("ywp: AfOnResult: REQ_PALM_COIN_GET_GOODS_LIST  success: "+flag);
		}
		break;
		
		case Consts.REQ_PALM_COIN_GET_MONEY2COINS_LIST:
		{
			AFMoney2CoinsResp orderid = (AFMoney2CoinsResp)result;
			System.out.println("ywp: AfOnResult: REQ_PALM_COIN_GET_GOODS_LIST  success: "+flag);
		}
		break;
		case Consts.REQ_PALM_COIN_GET_GOODS_LIST:
		{
			AFCoin2GoodsResp orderid = (AFCoin2GoodsResp)result;
			System.out.println("ywp: AfOnResult: REQ_PALM_COIN_GET_GOODS_LIST  success: "+flag);
		}
		break;
		case Consts.REQ_PALM_COIN_CREATE_RECHARGE:
		{
			String orderid = (String)result;
			System.out.println("ywp: AfOnResult: REQ_PALM_COIN_CREATE_RECHARGE  success: "+flag+orderid);	
		}
		break;
		case Consts.REQ_PALM_COIN_GET_AIRTIME_TOPUP:
		{
			Integer mycoin = (Integer)result;
			System.out.println("ywp: AfOnResult: REQ_PALM_COIN_GET_AIRTIME_TOPUP  success: "+flag+mycoin.intValue());	
		}
		break;
		case Consts.REQ_BLOCK_LIST:
		{
			System.out.println("ywp: AfOnResult: REQ_BLOCK_LIST  success: "+flag);	
		}
		break;
		case Consts.REQ_BCMSG_CHECK_MID:
		{
			AfResponseComm afResponseComm = (AfResponseComm) result;
			AfPulishInfo afPulishInfo = (AfPulishInfo) afResponseComm.obj;
			AfChapterInfo afChapterInfo = (AfChapterInfo) user_data;
		}
		break;
		case Consts.REQ_GET_ONLINE_STATUS_NEW:
		{
			AfOnlineStatusInfo profile = (AfOnlineStatusInfo)result;
			System.out.println("ywp: AfOnResult: REQ_GET_ONLINE_STATUS_NEW  success: "+profile.status);	
		}
		break;
			case Consts.REQ_PHONEBOOK_BACKUP:
			{
				System.out.println("ywp: AfOnResult: REQ_PHONEBOOK_BACKUP  success ");		
			}
			break;
			case Consts.REQ_UPDATE_INFO:
			{
				AfProfileInfo profile = (AfProfileInfo)result;
				System.out.println("ywp: AfOnResult: REQ_UPDATE_INFO  success, afid = " + profile.afId);		
			}
			break;
			case Consts.REQ_REG_BY_PHONE:
			case Consts.REQ_REG_BY_EMAIL:
			{
				String afid = (String)result;
				
				System.out.println("ywp: AfOnResult: REQ_REG_BY_PHONE or REQ_REG_BY_PHONE afid  = "  +  afid);				
			}
			break;

			case Consts.REQ_CHANGE_PASSWORD:
			{
				System.out.println("ywp: AfOnResult: REQ_CHANGE_PASSWORD or change success");	
			}
			break;
			case Consts.REQ_RESET_PWD_BY_PHONE:
			{
				String []ret = (String[])result;
				
				
			}
			break;
			case Consts.REQ_RESET_PWD_BY_EMAIL:
			{
				String password = (String)result;
				System.out.println("ywp: AfOnResult: REQ_RESET_PWD_BY_PHONE or REQ_RESET_PWD_BY_EMAIL password  = "  +  password);	
			}
			break;
			
			case Consts.REQ_CHECK_ACCOUNT_BY_EMAIL:
			case Consts.REQ_CHECK_ACCOUNT_BY_PHONE:
			case Consts.REQ_CHECK_ACCOUNT_BY_PHONE_EX:
			{
				boolean is_exist = (boolean)(null != result);
				
				System.out.println("ywp: AfOnResult: check account result  = "  +  is_exist);	
				
			}
			break;
			case Consts.REQ_BIND_BY_PHONE:
			{
				System.out.println("ywp: AfOnResult: REQ_BIND_BY_PHONE success ");	
			}
			break;
			case Consts.REQ_BIND_BY_EMAIL:
			{
				String des = (String)result;
				
				System.out.println("ywp: AfOnResult: REQ_BIND_BY_EMAIL descrip =  " + des);	
			}
			break;
			case Consts.REQ_LOGOUT:
			{
				System.out.println("ywp: AfOnResult: REQ_LOGOUT  success");	
			}
			break;
			case Consts.REQ_GET_BATCH_INFO:
			case Consts.REQ_SHAKE_AND_SHAKE:
			case Consts.REQ_LOOK_AROUND:
			case Consts.REQ_SEARCH_USER:
			{
				AfFriendInfo info[] = (AfFriendInfo[])result;
				
				if( null == info)
				{
					System.out.println("ywp: AfOnResult: REQ_GET_BATCH_INFO  or REQ_SHAKE_AND_SHAKE OR REQ_SEARCH_USER null");	
					break;
				}
				
				for(AfFriendInfo tmp : info)
				{
					System.out.println("ywp: AfOnResult: REQ_GET_BATCH_INFO  or REQ_SHAKE_AND_SHAKE success, afid = " + tmp.afId);	
				}
			}
			break;
			case Consts.REQ_GET_INFO:
			{
				AfProfileInfo profile = (AfProfileInfo)result;
				System.out.println("ywp: AfOnResult: REQ_GET_INFO  success, afid = " + profile.afId);	
			}
			break;
			case Consts.REQ_NEAR_BY_GPS:
			case Consts.REQ_NEAR_BY_GPS_EX:
			case Consts.REQ_NEAR_FRIEND:				
			case Consts.REQ_NEAR_FRIEND_EX:		
			
			{
				AfNearByGpsInfo gps[] = (AfNearByGpsInfo[])result;
				
				for(AfNearByGpsInfo tmp : gps)
				{
					System.out.println("ywp: AfOnResult: REQ_GET_BATCH_INFO  or REQ_SHAKE_AND_SHAKE success, afid = " + tmp.afid);	
				}
			}
			break;
			case Consts.REQ_GRP_CREATE:
			{
				mAfCorePalmchat.AfHttpGrpGetList(null, this);
			}
			break;
			case Consts.REQ_GRP_LIST:
			{
				
			}
			break;
		}
		
	}

	@Override
	public void AfOnProgress(int httpHandle, int flag, int progress, Object user_data) {
		// TODO Auto-generated method stub
		System.out.println("ywp: AfOnProgress httpHandle =" + httpHandle +  " flag=" + flag + " progress=" + progress + "user_data = " + user_data);
	}
	
	public static int readDbDataTest(){
		String src = "/data/data/com.afmobi.palmchat/a37541109";
		String dst = Environment.getExternalStorageDirectory().getPath()+"/a37541109";
		

//		String src = "/data/data/com.afmobi.palmchat/a2504056";
//		String dst = Environment.getExternalStorageDirectory().getPath()+"/a2504056";
		
		byte[] data = load(src);
		if( null != data){
			save(dst, data);
		}
		
		return 1;
	}
	public static int readDbDataTest(String afid){
		String src = "/data/data/com.afmobi.palmchat/"+afid;
		String dst = Environment.getExternalStorageDirectory().getPath()+"/"+afid;
		
		
//		String src = "/data/data/com.afmobi.palmchat/a2504056";
//		String dst = Environment.getExternalStorageDirectory().getPath()+"/a2504056";
		
		byte[] data = load(src);
		if( null != data){
			save(dst, data);
		}
		
		return 1;
	}
	
	/**
	 * 保存指定的数据流到指定位�?
	 * 
	 * @param path
	 * @param data
	 */
	public final static boolean save(String path, byte[] data)
	{
		try
		{
			File f = new File(path);
			if (f.exists())
			{
				f.delete();
			}

			FileOutputStream fos = new FileOutputStream(f);
			fos.write(data);
			fos.close();
			fos = null;
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 从指定位置读取数据流
	 * 
	 * @param path
	 * @return
	 */
	public final static byte[] load(String path)
	{
		try
		{
			
			File f = new File(path);
			if( !f.exists())
			{
				return null;
			}
			FileInputStream is = new FileInputStream(f);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			is = null;
			return buffer;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	public int AfHttpGetOnlineStatusNew(){
		return mAfCorePalmchat.AfHttpGetOnlineStatusNew("a74309148", null, this);
	}
	
	public int AfHttpBCMsgCheckMId(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		
		return mAfCorePalmchat.AfHttpBCMsgCheckMId(info.region, info.country, null, this);
	}
	
	public int AfHttpBlockOpr(){
		
		return mAfCorePalmchat.AfHttpBlockOpr("a190020", Consts.HTTP_ACTION_A, "0", null, this);
	}
	
	public int AfHttpNewPaymentCoins2GoodsList(){
			
			return mAfCorePalmchat.AfHttpNewPaymentCoins2GoodsList(AfNewPayment.NewPayment_Coin2Goods_Type_AirTime, this);
	}
	
	public int AfHttpNewPaymentBillHistoryList(){
		
		return mAfCorePalmchat.AfHttpNewPaymentBillHistoryList(AfNewPayment.NewPayment_Bill_History_Type_Consume, (int) System.currentTimeMillis() + new Random(10000).nextInt(), 1, 10, this);
	}
	
	public int AfHttpNewPaymentMoney2CoinsList(){
		
		return mAfCorePalmchat.AfHttpNewPaymentMoney2CoinsList( this);
	}
	
	public int AfHttpNewPaymentAirtimeTopup(){
		AFAirtimeTopupReq req = new AFAirtimeTopupReq();
		req.to_afid = "190189";
		req.from_afid = "190189";
		req.bill_item_id = 5;
		req.amount = 1;
		req.network = "Airtel";
		req.afcoin = 100;
		req.phone_country_code = "234";
		req.phone_number = "18033441033";
		req.remark = "";
		
		
		return mAfCorePalmchat.AfHttpNewPaymentAirtimeTopup(req, this);
	}
	
	public int AfHttpNewPaymentGetRCOrderId(){
		AFRechargeOrderReq req = new AFRechargeOrderReq();
		req.afcoin = 100;
		req.money = 100;
		req.item_id = 10;
		req.gateway = "PAGA";
		req.channel = "PAGA_EPAY";
		req.currency = "NGN";
		req.app_channel = AfNewPayment.NewPayment_RechargeOrder_AppChannel_Type_WALLET;
		return mAfCorePalmchat.AfHttpNewPaymentGetRCOrderId(req, this);
	}
	
	public int AfCoreAfNewPaymentGetRechargeOrderLogs(){
		return mAfCorePalmchat.AfCoreAfNewPaymentGetRechargeOrderLogs(0,10, this);
	}
	
	public int AfHttpPredictGetRecommendFriendlist(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		
		return mAfCorePalmchat.AfHttpPredictGetRecommendFriendlist(info.afId, this);
	}
	
	public int AfHttpPredictGetInviteFriendlist(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		return mAfCorePalmchat.AfHttpPredictGetInviteFriendlist(info.afId, 0, 10, 0, this);
	}
	
	
	public int AfHttpAfBCGetTrends(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		return mAfCorePalmchat.AfHttpAfBCGetTrends(10000, null, this);
	}
	
	public int AfHttpAfBCGetTrendsMore(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		return mAfCorePalmchat.AfHttpAfBCGetTrendsMore( 10000, 0, 10, this);
	}
	
	public int AfHttpAfBCGetHotTags(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		return mAfCorePalmchat.AfHttpAfBCGetHotTags( 10000, 0, 10, this);
	}
	
	public int AfHttpAfBCSearchTags(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		return mAfCorePalmchat.AfHttpAfBCSearchTags( 10000, 0, 10,"#abc", this);
	}
	
	public int AfHttpAfBCShareBroadCast(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		return mAfCorePalmchat.AfHttpAfBCShareBroadCast( "a1000019", System.currentTimeMillis(), "86-0-Guangdong-1aDUp2-4D3oy-0-0-004CfY-0",null, this);
	}
	
	public int AfHttpAfBCShareTags(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		return mAfCorePalmchat.AfHttpAfBCShareTags( "a1000019", System.currentTimeMillis(),"#abc", "http://i3.sinaimg.cn/blog/2014/1029/S129809T1414550868715.jpg",null, this);
	}
	
	public int AfHttpAfBCGetDefaultTags(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		return mAfCorePalmchat.AfHttpAfBCGetDefaultTags(this);
	}
	
	public int AfHttpAfBcgetChaptersByNewTag(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		return mAfCorePalmchat.AfHttpAfBcgetChaptersByNewTag(10000,0,10, "#abc", this);
	}
	
	public int AfHttpAfBcgetChaptersRecentHotByNewTag(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		return mAfCorePalmchat.AfHttpAfBcgetChaptersRecentHotByNewTag(10000,0,10, "#abc", this);
	}
	
	public int AfHttpBCMsgPublishEX(Context context){

		double lat = Double.valueOf(SharePreferenceUtils.getInstance(context).getLat());
		double lon = Double.valueOf(SharePreferenceUtils.getInstance(context).getLng());
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		long sid = System.currentTimeMillis(); 
		if (lat == 4.9E-324) {
			lat = 0.0d;
		}
		if ( lon == 4.9E-324) {
			lon = 0.0d;
		}
		String msg = "tagtest";
		String tag = "#abc";
		

		
		return mAfCorePalmchat.AfHttpBCMsgPublishEX("BR_TYPE_TEXT", msg,  Consts.BR_TYPE_TEXT, (byte)1, info.afId, 0, lon, lat, info.city, info.region,info.country, tag,PalmchatApp.getOsInfo().getCountryCode() , null, null, null, null ,this);
	}
	
	public void AfDbBCTagDefaultTrendOpr(){
		AfTagGetDefaultTrend saveinfo = new AfTagGetDefaultTrend();
		
		AfBCPriefInfo bpinfo = new AfBCPriefInfo();
		bpinfo.mid = "1234556";
		bpinfo.pic_url = "http://www.baidu.com/";
		
		saveinfo.tag_name = "#abc";
		saveinfo.brief_list.add(bpinfo);
		
		// write
		int ret = mAfCorePalmchat.AfDbBCTagDefaultTrendSave2Db(saveinfo);
		
		// read
		AfTagGetTrendsResp obj = mAfCorePalmchat.AfDbBCTagGetDefaultTrend();
		// del
		ret = mAfCorePalmchat.AfDbBCTagDeleteDefaultTrend();
		
		
		obj = mAfCorePalmchat.AfDbBCTagGetDefaultTrend();
		
		bpinfo.mid = "11";
	}
	
	public void  AfDbBCTagAttrOpr(){
		
		AfBroadCastTagInfo info = new AfBroadCastTagInfo(0, 11, "#123", "官方", AfResponseComm.AFTAGINFO_TAGTYPE_PUBLIC_ACCOUNT, 0, 0);

		
		// insert
		info._id = mAfCorePalmchat.AfDbBCTagAttrInsert(info);
		
		// get
		AfResponseComm resp = mAfCorePalmchat.AfDbBCTagAttrFindByAid(11);
		
		// update status
		int ret = mAfCorePalmchat.AfDbBCTagAttrUpdateStatusByAid(1, 11);
		resp = mAfCorePalmchat.AfDbBCTagAttrFindByAid(11);
		
		// delete by aid
		ret = mAfCorePalmchat.AfDbBCTagAttrDeleteByAid(11);
		resp = mAfCorePalmchat.AfDbBCTagAttrFindByAid(11);
		
		// delete cmd
		info._id = mAfCorePalmchat.AfDbBCTagAttrInsert(info);
		resp = mAfCorePalmchat.AfDbBCTagAttrFindByAid(11);
		ret = mAfCorePalmchat.AfDbBCTagAttrDeleteCmd(11,0,AfResponseComm.AFTAGINFO_TAGTYPE_PUBLIC_ACCOUNT);
		resp = mAfCorePalmchat.AfDbBCTagAttrFindByAid(11);
		
		ret++;

	}
	
	public void  AfDbBCTagInfoOpr(){
		AfTagInfo info = new AfTagInfo();
		info.pic_url = "http://www.baidu.com/";
		info.post_number = 100;
		info.tag = "#abcd";
		info.use_type = AfResponseComm.AFTAGINFO_USETYPE_BANNER;
		
		// insert
		info._id = mAfCorePalmchat.AfDbBCTagInfoInsert(info);
		
		// get
		AfTagGetTagsResp resp = mAfCorePalmchat.AfDbBCTagInfoGetList(AfResponseComm.AFTAGINFO_USETYPE_BANNER);
		
		// delete by type
		int ret =  mAfCorePalmchat.AfDbBCTagInfoDeleteByType(AfResponseComm.AFTAGINFO_USETYPE_BANNER);
		
		resp = mAfCorePalmchat.AfDbBCTagInfoGetList(AfResponseComm.AFTAGINFO_USETYPE_BANNER);
		
		
		
		// delete all
		info._id = mAfCorePalmchat.AfDbBCTagInfoInsert(info);
		
		resp = mAfCorePalmchat.AfDbBCTagInfoGetList(AfResponseComm.AFTAGINFO_USETYPE_BANNER);
		
		ret =  mAfCorePalmchat.AfDbBCTagInfoDeleteAll();
		
		resp = mAfCorePalmchat.AfDbBCTagInfoGetList(AfResponseComm.AFTAGINFO_USETYPE_BANNER);
		
		ret++;

	}
	
	
	public int AfHttpAfBCLangPackage(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		return mAfCorePalmchat.AfHttpAfBCLangPackage("", this);
	}
	
	public int AfHttpAfBcgetRegionBroadcast(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		return mAfCorePalmchat.AfHttpAfBcgetRegionBroadcast(this);
	}
	
	public int AfHttpAfBcgetByStateOrCity(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		return mAfCorePalmchat.AfHttpAfBcgetByStateOrCity(10000, 0, 20, "China", "Guangdong", "shenzhen", null,this);
	}
	
	public int AfHttpVersionCheck(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		return mAfCorePalmchat.AfHttpVersionCheck(info.afId, "palmchat_5.1.3_0_2016_01_13", "com.afmobigroup.android","android_gp", null, this);
	}
	
	public int AfHttpCheckIlleagalWord(){
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		String[] strarray = {"abc", "suck", "fuck"};
		
		return mAfCorePalmchat.AfHttpCheckIlleagalWord(strarray, this);
	}
	
	public int AfHttpPalmCallGetHotList(){
		return mAfCorePalmchat.AfHttpPalmCallGetHotList(false,null, this);
	}
	
	public int AfHttpPalmCallMakeCall(){
		return mAfCorePalmchat.AfHttpPalmCallMakeCall("a1010612",null, this);
	}
	
	public int AfHttpPalmCallUploadIntroMedia(){
		return mAfCorePalmchat.AfHttpPalmCallUploadIntroMedia("12231","/sdcard/Recordings/20160623_001.amr",Consts.MEDIA_TYPE_AMR, 13,null, this);
	}

    public int AfHttPalmCallGetLeftTime(){
        return mAfCorePalmchat.AfHttPalmCallGetLeftTime(null, this);
    }

    public int AfHttpPalmCallSetAlonePeriod(){
        return mAfCorePalmchat.AfHttpPalmCallSetAlonePeriod(1, 22, 8, null, this);
    }

    public int AfHttpPalmCallGetAlonePeriod(){
        return mAfCorePalmchat.AfHttpPalmCallGetAlonePeriod(null, this);
    }

    public int AfHttpPalmCallGetInfo(){
        CacheManager cm = CacheManager.getInstance();
        AfProfileInfo info = cm.getMyProfile();
        return mAfCorePalmchat.AfHttpPalmCallGetInfo("a1010999", null, this);
    }

    public int AfHttpPalmCallGetInfoBatch(){
        CacheManager cm = CacheManager.getInstance();
       String[] afids = { "a1010999"};
        return mAfCorePalmchat.AfHttpPalmCallGetInfoBatch(afids, null, this);
    }

	
	public int AfHttpPublicAccountGetHistory(){
		return mAfCorePalmchat.AfHttpPublicAccountGetHistory("r99923456", (int)10000, 0, 10, null, this);
	}

	public int AfDbPalmCallRecordTest(){
		AfPalmCallResp.AfPalmCallRecord record = new AfPalmCallResp.AfPalmCallRecord();
		record.afId = "a1000018";
		record.mediaUrl = "urlrlrl";
		record.callId = 1234;
		record.callTime = 1111;
		record.callType = AfPalmCallResp.AFMOBI_CALL_TYPE_MISSED;
		record._id = mAfCorePalmchat.AfDbPalmCallRecordInsert(record);

		AfPalmCallResp resp = mAfCorePalmchat.AfDbPalmCallRecordGetList();

		resp =  mAfCorePalmchat.AfDbPalmCallRecordGetLimitList(0, 10, (int)AfPalmCallResp.AFMOBI_CALL_TYPE_MISSED);
		record.callType = AfPalmCallResp.AFMOBI_CALL_TYPE_IN;
		mAfCorePalmchat.AfDbPalmCallRecordUpdate(record);

		resp = mAfCorePalmchat.AfDbPalmCallRecordGetList();

		mAfCorePalmchat.AfDbPalmCallRecordDelete(record._id);

		resp = mAfCorePalmchat.AfDbPalmCallRecordGetList();

		resp = mAfCorePalmchat.AfDbPalmCallRecordGetList();

		return  1;
	}

	public int AfFollowGetList(){
		int type = Consts.AFMOBI_FOLLOW_PASSIVE;
		if(((int)Math.random())%2 == 0)
		{
			type = Consts.AFMOBI_FOLLOW_MASTRE;
		}
		return mAfCorePalmchat.AfFollowGetList(type, null, this);
	}

	public int AfHttpHttpPublicAccountGetDetail(){
		//return mAfCorePalmchat.AfHttpHttpPublicAccountGetDetail("http://ms.palm-chat.com/00/html/201511/AhPywcXPoE.html", null, this);
        return mAfCorePalmchat.AfHttpHttpPublicAccountGetDetail("https://www.baidu.com/", null, this);
	}

    public int AfHttpPaymentAnalysis(){
        return mAfCorePalmchat.AfHttpPaymentAnalysis("[{\"ts\":\"1234561190\",\"url\":\"http://www.mypaga.com/aaw7\",\"RSSI\":\"436\",\"CINR\":\"719\",\"ping\":{\"total packets\":\"8\",\"received\":\"4\",\"packet loss\":\"50%\",\"time\":\"8031\",\"rt\":\"7.310/8.784/11.607/1.540\"}}]", null, this);
    }


    public void   AfDbPalmCallInfoTest()
    {
        AfPalmCallResp.AfPalmCallHotListItem record = new AfPalmCallResp.AfPalmCallHotListItem();
        record.afid = "a1010618";
        record.mediaDescUrl = "urlrlrl";
        record.coverUrl = "urlrlrl";
        record.name = "yunxiang";
        record.age = 1111;
        record.sex = 1;
        record.answeringTimes = 100;
        record.availableType = 1;
        record.duration = 9;
        record._id = mAfCorePalmchat.AfDbPalmCallInfoInsert(record);

        AfPalmCallResp tmp = mAfCorePalmchat.AfDbPalmCallInfoGet("a1010618");


        record.mediaDescUrl = "11";
        record.coverUrl = "111";
        record.name = "222";
        record.age = 22;
        record.sex = 33;
        record.answeringTimes = 10;
        record.availableType = 2;
        record.duration = 5;

        int ret = mAfCorePalmchat.AfDbPalmCallInfoUpdate(record);

        tmp = mAfCorePalmchat.AfDbPalmCallInfoGet("a1010618");

        ret =  mAfCorePalmchat.AfDbPalmCallInfoDelete("a1010618");

        tmp = mAfCorePalmchat.AfDbPalmCallInfoGet("a1010618");

        ret++;
    }

	
	
	
	
}
