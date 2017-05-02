package com.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.core.AfNewPayment.AFAirtimeTopupReq;
import com.core.AfNewPayment.AFRechargeOrderReq;
import com.core.AfResponseComm.AfBroadCastTagInfo;
import com.core.AfResponseComm.AfTagGetDefaultTrend;
import com.core.AfResponseComm.AfTagGetLangPackageResp;
import com.core.AfResponseComm.AfTagGetTagsResp;
import com.core.AfResponseComm.AfTagGetTrendsResp;
import com.core.AfResponseComm.AfTagInfo;
import com.core.cache.CacheManager;
import com.core.contact.ContactAPI;
import com.core.contact.ContactInfo;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;
import com.core.listener.AfHttpSysListener;
import com.core.param.AfChatroomSendMsgParam;
import com.core.param.AfImageReqParam;
import com.core.param.AfNearByGpsParam;
import com.core.param.AfPhoneInfoParam;
import com.core.param.AfRegInfoParam;
import com.core.param.AfSearchUserParam;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
@SuppressLint({ "HandlerLeak", "SdCardPath" })
public class AfPalmchat {

	/***********************************************************************************************************************/
	// native interface and http req interface
	/***********************************************************************************************************************/
	private native void HttpCancel(int httpHandle);
	
	public native void VersionDownloadHttpCancel(int http_handle);
	
	private native int HttpLogin(String user_name, String password, String country_code, byte type, int user_data);
	private native int HttpRegLogin(AfRegInfoParam param, String user_id, byte type, int user_data);

	private native int HttpRegister(AfRegInfoParam param, int req_flag, int user_data);
	private native int AfGetSMSCode(int req_flag, String cc, String phone_number, byte sms_code_type,int voice);
	
	private native int HttpChangPwd(String old_pwd, String new_pwd, String user_ip, int user_data);

	private native int HttpAccountOpr(int req_flag, String phone_or_email, String mcc, String afid_or_sendvfy, String user_ip, int user_data, String smscode);

	private native int HttpLogout(int user_data);

	private native int HttpGetInfo(String[] afid, int req_flag, boolean is_background, String user_ip, int user_data);

	private native int HttpFriendOpr(String group, String uuid, byte action, byte src, byte type, String user_ip, int user_data);
	
	private native int HttpBlockOpr(String uuid, byte action, byte type, String user_ip, int user_data);

	private native int HttpGpsClean(int user_data);

	private native int HttpNearByGps(AfNearByGpsParam param, int user_data);

	private native int HttpShakeAndShake(String type, int limit, String user_ip, int user_data);

	private native int HttpLookAround(String country, String city, String region, int start, int pageid, int limit, String user_ip, int user_data);

	private native int HttpSearchUser(int req_flag, AfSearchUserParam param);

	private native int HttpUpdateInfo(AfProfileInfo param, int flag);

	private native int HttpSendMsg(String toAfid, long sid, String msg, byte command, int user_data);

	private native int HttpVoiceSend(String toAfid, long sid, byte[] voice_data, int voice_len, int record_time, int user_data);

	private native int HttpGetOnlineStatusNew(String afid);
	
	private native int HttpPhonebookBackUP(String pb_name[], String pb_phone[], byte action, int start, int limit, int user_data);

	private native int HttpPbImportFriend(int user_data);

	private native int HttpSendImage(AfImageReqInfo param);

	private native int HttpGrpOpr(String members, String name, String sig, String notice, String afid, int req_flag, boolean is_background);

	private native int HttpGrpGetList();

	private native int HttpFindPwdGetQuestion(String afid, byte type);

	private native int HttpFindPwdAnswer(int req_flag, String afid, byte type, String[] question, String[] answer);

	private native int HttpMutualFriends();

	private native int HttpFeedback(String user, String msg);

	private native int HttpGetRegRandom(String phone, String cc, int src);

	private native int HttpVersionCheck(String productid, String afid,String packagename,String channel);
	
	
	private native int HttpInstallPackageDownload(String path, String url, long cur_pos);

	private native int HttpStatistic(boolean is_logined, boolean is_new_version, String data);

	private native int HttpDownloadFile(String url, String path, boolean need_progress);

	// chatroom
	private native int HttpChatroomGetList(int isPalmguess);

	private native int HttpChatroomGetMemberList(String afid);

	private native int HttpChatroomEntry(String country, String cid);

	private native int HttpChatroomExit();

	private native int HttpChatroomSendMsg(AfChatroomSendMsgParam param);

	private native int HttpChatroomBmCmd(String afid, boolean forbid);

	public native void AfChatroomSetServerOpr(byte type, String ip_ctoken, String port_cptoke, String cid, String country);

	// avatar
	private native int HttpAvatarUpload(String filePath, String fileName, String format, int index);
	private native int HttpAvatarwallModify(String filePath, String fileName, String format, String sn);

	private native int HttpAvatarDownload(String afid, String savePath, String sn, String px, int index, String server_url, boolean foreground);

	private native int HttpAvatarDownloadDirect(String afid, String savePath, String sn, String px, int index, String server_url, boolean foreground);

	private native int HttpAvatarDelete(String afid, String sn, int index);

	// download iamge
	private native int HttpMediaDownload(AfImageReqParam param);

	// set polling status to server
	private native int HttpLoginStatus(byte status);

	private native void HttpRemoveAllListener();

	// import
	private native int HttpImportFriends(int flag, String myid, String friends);

	// promotion
	private native int HttpPromotion(String afid);

	// accuation
	private native int HttpAccusation(String afid);

	// dating or broadcast
	private native int HttpSendBroadcast(int flag, double lat, double lng, String region, String city, long sid, String msg,int btype);

    private native int HttpBroadcastUpload(String url, String resume_url, String token, String media_path, int media_type);
	
	private native int HttpBroadcastHistory(int flag, String region, String city, double lat, double lng, int start, int pageid, int limit);

	private native int HttpBroadcastDownload(String dl_url, String save_path);

	private native int HttpSendGift(String recv_afid, int number, long sid);

	private native int HttpGetStar();

	private native int HttpUpdateDatingInfo(int flag, AfDatingInfo dating);

	private native int HttpGetDatingPhone(String afid);

	private native int HttpGetGifts(int offset, int count, int max, boolean newly, boolean db_opr);

	private native int HttpGiveGifts(byte type);

	// miss Negeria
	private native int HttpMissNegeriaOpr(int flag, int pageid, boolean Is20Items);

	// public account
	private native int HttpPublicAccountGetList(boolean is_tag);
	private native int HttpPublicAccountGetHistory(String paid, int pageid, int start, int limit);
	private native int HttpPublicAccountGetDetail(String url);

												   /***********************************************************************************************************************/
	// res manager native interface
	/***********************************************************************************************************************/
	public native String AfResGenerateFileName(int type);

	public native String AfResSaveData(int type, byte[] data);

	public native String AfResGetDir(int type);

	/***********************************************************************************************************************/
	// polling native interface
	/***********************************************************************************************************************/
	public native void AfPollSetStatus(int status);

	public native void AfChatroomPollSetStatus(int status);

	public native void AfSetLoginInfo(AfLoginInfo info);

	/***********************************************************************************************************************/
	// database native interface
	/***********************************************************************************************************************/
	// message database operation
	public native int AfDbMsgRmove(int id);

	public native AfMessageInfo AfDbMsgGet(int id);

	public native int AfDbMsgUpdate(AfMessageInfo msg);

	public native int AfDbMsgInsert(AfMessageInfo msg);

	public native int AfDbMsgSetStatus(int id, int status);

	public native int AfDbMsgGetUnreadSize(int msg_type, String afid);

	public native int AfDbMsgSetStatusEx(int msg_type, String afid, int old_status, int new_status);

	public native int AfDbMsgSetStatusOrFid(int msg_type, int msg_id, int status, String fid, byte param_select);

	// chatroom
	public native int AfDbCrMsgInsert(AfMessageInfo msg);

	public native int AfDbCrMsgSetStatus(int id, int status);

	public native int AfDbCrMsgUpdate(AfMessageInfo msg);

	// group message database operation
	public native AfMessageInfo AfDbGrpMsgGet(int id);

	public native int AfDbGrpMsgUpdate(AfMessageInfo msg);

	public native int AfDbGrpMsgInsert(AfMessageInfo msg);

	public native int AfDbGrpMsgSetStatus(int id, int status);

	public native int AfDbGrpMsgRemove(int id);

	// attach voice info
	public native int AfDbAttachVoiceRmove(int id);

	public native AfAttachVoiceInfo AfDbAttachVoiceGet(int id);

	public native int AfDbAttachVoiceInsert(AfAttachVoiceInfo msg);
	// attach public account msg info
	public native int AfDbAttachPAMsgRmove(int id);

	public native AfAttachPAMsgInfo AfDbAttachPAMsgGet(int id);

	public native int AfDbAttachPAMsgInsert(AfAttachPAMsgInfo msg);
	public native int AfDbAttachPAMsgUpdate(AfAttachPAMsgInfo msg);
	

	// attach image info
	public native int AfDbAttachImageRmove(int id);

	public native AfAttachImageInfo AfDbAttachImageGet(int id);

	public native int AfDbAttachImageInsert(AfAttachImageInfo msg);

	public native int AfDbAttachImageUpdateProgress(int id, int progress);

	public native int AfDbAttachImageUpdateId(int id, int upload_id);

	public native int AfDbAttachImageSetLargePath(int id, String path);

	// attach image req info
	public native int AfDbImageReqRmove(int id);

	public native AfImageReqInfo AfDbImageReqGet(int id);

	public native int AfDbImageReqInsert(AfImageReqInfo msg);

	public native int AfDbImageReqUPdate(AfImageReqInfo msg);

	/***********************************************************************************************************************/
	// database friend or profile
	/***********************************************************************************************************************/
	public native int AfDbProfileInsert(int db_type, AfFriendInfo elem);

	public native int AfDbProfileUpdate(int db_type, AfFriendInfo elem);

	public native int AfDbProfileRemove(int db_type, String afid);

	public native int AfDbProfileSaveOrUpdate(int db_type, AfFriendInfo elem);

	public native boolean AfDbProfileSearch(String afid);
	//加陌生人AFMOBI_FRIEND_TYPE_BF   删陌生人 AFMOBI_FRIEND_TYPE_STRANGER
	public native boolean AfDbProfileUpdateType(int type, String afid);
	public native boolean AfDbProfileUpdateFollowtype(int follow_type, String afid);

	public native boolean AfDbProfileUpdateSid(int sid, String afid);

	public native boolean AfDbProfileUpdateAlias(String afid, String alias);

	public native AfFriendInfo AfDbProfileGet(int db_type, String afid);

	public native void AfDbSetHeadImagePath(String afid, String head_image_path);

	public native void AfDbProfileUpdateNewContact(String afid, boolean is_new_contact);

	// public native AfFriendInfo[] AfDbProfileGetList(int db_type, int type);
	public native AfFriendInfo AfDbProfileGetPhone(int db_type, String phone);

	/***********************************************************************************************************************/
	// recent record database
	/***********************************************************************************************************************/
	public native int AfDbRecentMsgInsert(int db_type, AfMessageInfo elem);

	public native int AfDbRecentMsgRemove(int db_type, AfMessageInfo elem);

	public native AfMessageInfo[] AfDbRencentMsgGetList(int db_type);

	public native AfMessageInfo[] AfDbRecentMsgGetRecord(int type, String afid, int offset, int count);

	public native void AfDbMsgClear(int type, String afid);

	public native int AfRecentMsgSetUnread(String afid, int unread);

	public native int AfRecentMsgGetUnread(String afid);

	public native int AfDbSetMsgExtra(String afid, String msg);

	public native String AfDbGetMsgExtra(String afid);

	/***********************************************************************************************************************/
	// account relatvie interface
	/***********************************************************************************************************************/
	public native int AfDbLoginSetStatus(String afid, int status);

	public native AfLoginInfo[] AfDbLoginGetAccount();

	public native int AfDbLoginRemove(String afid);

	public native AfLoginInfo AfDbLoginGet(int type, String afid);

	public native int AfDbLoginUpdatePassword(String afid, String password);

	public native void AfCoreOpenDatabase(String afid);

	public native void AfCoreSwitchAccount();

	public native boolean AfDbLoginSaveOrUpdate(AfLoginInfo info);

	/***********************************************************************************************************************/
	// gift info database interface
	/***********************************************************************************************************************/
	public native int AfDbGiftInfoInsert(AfGiftInfo info);
	public native AfGiftInfo[] AfDbGiftInfoGetRecord(int offset, int count);

	/***********************************************************************************************************************/
	// setting data
	/***********************************************************************************************************************/
	public native boolean AfDbSettingGetTips(String afid);
	public native int AfDbSettingSetTips(String afid, boolean flag);

	// is_sound = true, set sound
	// is_sound = false set vibrate
	public native int AfDbSettingSetSoundOrVibrate(String afid, boolean flag, boolean is_sound);
	public native boolean AfDbSettingGetSoundOrVibrate(String afid, boolean is_sound);

	public native int AfDbSettingSetEmailOrCount(String afid, long time_count, boolean is_time);
	public native long AfDbSettingGetEmailOrCount(String afid, boolean is_time);

	public native int AfDbSettingSetAppSound(String afid, boolean flag);
	public native boolean AfDbSettingGetAppSound(String afid);
	
	// set or update pb time
	public native long AfDbSettingPbTimeOpr(String afid, long time, boolean is_setting);

	// set or update look around first time flag
	public native boolean AfDbLookAroundOpr(String afid, boolean first, boolean is_setting);

	// set miss nigeria first time flag
	public native boolean AfDbMissNigeriaOpr(String afid, boolean first, boolean is_setting);

	// voice type
	public native String AfDbSettingVoiceTypeOpr(String afid, String voice, boolean is_setting);

	// pop up opr
	public native boolean AfDbPopUpOpr(String afid, boolean first, boolean is_setting);

	//onoff notify msg
	public native boolean AfDbOnoffNofitymsg(String afid, boolean inout, boolean is_setting);

	//onoff detail msg
	public native boolean AfDbOnoffDetailmsg(String afid, boolean inout, boolean is_setting);

	//onoff detail msg
	public native int AfDbAvoidDisturb(String afid, int inout, boolean is_setting);

	public native int AfRTCDoCallback(int handle);

	// get md5
	public native String AfGetmd5(byte[] data);
	
	//  encode as login
	public native String AfLoginEncode(String src, String key);
	

	/***********************************************************************************************************************/
	// test server info
	/***********************************************************************************************************************/
	public native String AfHttpGetServerCmd(int flag);
	public native String AfHttpGetServerSession();

	/**
	 * This function get server info CHAR *[5] [0]: server url [1]: server token
	 * [2]: server session [3]: photo host [4]: bc host 
	 * [5] : online server,"1",online;"0",test
     * [6] : palmcall download server
	 */
	public native String[] AfHttpGetServerInfo();

	/**
	 * This function set language \sa
	 */
	public native void AfCoreSetLanguage(String language);

	/**
	 * This function set time zone \sa
	 */
	public native void AfCoreSetTimezone(String timezone);

	/*
	 * get store product list
	 */
	public native int AfStoreGetProdList(int page_index, int page_size, int prod_type, int prod_categoty);

	/*
	 * get store product detail
	 */
	public native int AfStoreGetProdDetail(String item_id);

	/*
	 * download store file
	 */
	public native int AfStoreDownload(String d_url, String item_id, int img_pixel, String save_path, boolean zip_flag);

	/*
	 * get remain coin
	 */
	public native int AfStoreGetRemainCoin();

	/*
	 * product version check
	 */
	public native int AfStoreProdVersionCheck(int prod_type, String ver_json);

	/*
	 * product version check
	 */
	public native int AfStoreGetConsumeRecord(int prod_type, int page_index, int page_size);

	/*
	 * product version check
	 */
	public native int AfStoreHaveNewProd();

	/*
	*get pcoin
	*/                
	public native int AfPaymentGetVCoin();

	/*
	*GT card recharge
	*/                
	public native int AfPaymentGTRecharge(String card_num,String card_pwd, int channel);

	/*
	*query transaction record
	*/                
	public native int AfPaymentQueryTransRecord(int page_index, int page_size);

	/*
	*query transaction detail by trans_id
	*/                
	public native int AfPaymentQueryTransDetail(String trans_id);

	/*
	*payment cousume 
	*/                
	public native int AfPaymentConsume(int consume_coin, int channel);

    /*
	*payment get sn by sms 
	*/                
	public native int AfPaymentSmsGetsn();

	
    /*
	*payment get sn by sms 
	*/                
	public native int AfPaymentSmsRecharge(String sn);

    /*
	*insert download product info to database
	*/   
	public native int AfDBPaystoreProdinfoInsert(String item_id, String save_path, String alas_name, String life_time, int status,int price, long datetime,int ver_code,String packagename);

	/*
	*remove download product info to database
	*/   
	public native int AfDBPaystoreProdinfoRemove(String item_id);

    /*
	*update create time because of order
	*/   
	public native int AfDBPaystoreProdinfoUpdate(String item_id, int ver_code, long datatime);

	/*
	*remove download product info to database
	*/   
	public native AfPaystoreCommon AfDBPaystoreProdinfoList();

    /*
	*insert record to database
	*/   
	public native int AfDBBrinfoInsert(AfNearByGpsInfo param);


	/*
	*get latest record by user refer to 
	*/   
    public native AfResponseComm AfDBBrinfoGetLatestRecord(int count,int offset, byte brmsg_type);

    /*
	*update brmsg status to database
	*/   
    public native int AfDBBrinfoUpdateStatusByAfid(int status,String afid);

    /*
	*update brmsg status to database
	*/   
    public native int AfDBBrinfoUpdateStatusByPID(int status, int _id);

    /*
	*update brmsg status to database
	*/   
	public native int AfDBBrinfoGetReceiveLatestMsgid();

   /*
	*update brmsg status to database
	*/   
	public native void AfCoreSetIMSI(String imsi);
    /*
	*follow opr cmd
	*/   
	public native int AfFollowCmd(int follow_type, String uuid_list/*id1:id2:id3...*/, int action);

    /*
    *refresh follow and notify cache..
	*/   
	public native void AfCoreRefreshFollow(int follow_type);


	/*
   * get follow/followers with callback..
   */
	public native int AfFollowGetList(int follow_type);


    /*
    *get follow type by afid..
	*/   
	public native int AfDbProfileGetFollowtype(String afid);

    /*
	*refresh kc state..
	*/   
	public native void AfCoreKCStatusReset();

	/*
	  *get the same city peoples..
	  */ 
	public native int AfBcNearbyCityPeoples(byte sex, int page_id, int page_start, int page_limit, boolean online);
	/*
	  *get the same state peoples..
	  */ 
	public native int AfBcNearbyStatePeoples(byte sex, int page_id, int page_start, int page_limit, boolean online);

    /*
	  *get the same gps peoples..
	  */ 
	public native int AfBcNearbyGPSPeoples(byte sex, int page_id, int page_start, int page_limit, boolean online,double lng, double lat, boolean isFarfaraway);

	public native int AfBcNearbyGPSPeoplesPAccounts(byte sex, int page_id, int page_start, int page_limit, boolean online,double lng, double lat, boolean isFarfaraway);
	/*
       *get the same city chapter..
	 */ 
	public native int AfBcgetChaptersByCity(int page_id, int page_start, int page_limit);
	public native int AfBcgetChaptersByState(int page_id, int page_start, int page_limit);

	/*
       *get the same city chapter..
	 */ 
	public native int AfBcgetChaptersByGPS(int page_id, int page_start, int page_limit, double lng, double lat);
	public native int AfBcgetChaptersByMid(int page_id, int page_start, int page_limit, String mid, boolean is_only_com);
	public native int AfBcgetChaptersLikeByMid(int page_id, int page_start, int page_limit, String mid);
	public native int AfBcgetProfileByAfid(int page_id, int page_start, int page_limit, String afid);
	public native int AfBcgetChaptersLikeStar(int flag, int page_id, int page_start, int page_limit);
	
	/*
      *get the same tag chapter..
	*/ 
	public native int AfBcgetChaptersByTag(int page_id, int page_start, int page_limit, String tag);
	public native int AfBcgetChaptersByTagCity(int page_id, int page_start, int page_limit, String tag);
	public native int AfBcgetChaptersByTagGps(int page_id, int page_start, int page_limit, String tag);

    /*
	 *get the same city peoples..
	 */ 
	public native int AfBcgetPeoplesChaptersByCity(byte sex, int page_id, int page_start, int page_limit, boolean online);

	
	/*
	*get the same gps peoples..
	*/ 
	 public native int AfBcgetPeoplesChaptersByGPS(byte sex, int page_id, int page_start, int page_limit, boolean online,double lng, double lat, boolean isFarfaraway);

     /*
	*Broadcast50 publish title.
	*/ 
	 public native int AfBCMsgPublish(String title, String content, byte bctype, byte purview, String afid,int attach_cnt,double lng, double lat, String city, String state, String country, String tag, String cc,String language, String forward_mid, String layout);

	 /*
	 *Broadcast50 check new mid.
	 */ 
     public native int AfBCMsgCheckMid( String state, String country);

	/*
	*Broadcast50 upload media file.
	*/
	 public native int AfBCMediaUpload(String media_path, int media_seq,byte media_type, String mstoken,int duration, String resize, String cut,String filter);


	/*
	*Broadcast50 message operate.
	*/
	public native int AfBCMsgOperate(int flag, String afid, String to_afid, String mid,String content, String cid);

    /*
	*Broadcast50 insert media file to db.
	*/
    public native int AfDBBCMFileInsert(int ds_type, int aid, String local_img_path, String local_thumb_path,String url,String thumb_url, int url_type, int status);

    /*
	*Broadcast50 insert like info to db.
	*/
	public native int AfDBBCLikeInsert(int ds_type, int aid, String like_id, String time, String afid, int status, AfFriendInfo profile);
	 
    /*
	*Broadcast50 insert comment info to db.
	*/
	public native int AfDBBCCommentInsert(int ds_type, int aid, String cid, String time, String afid, String to_afid, String comment,int status, AfFriendInfo profile);

	/*
	*Broadcast50 insert chapter info to db.
	*ds_type: Consts.DATA_FROM_LOCAL  Consts.DATA_FROM_SERVER
	*bc_type: Consts.BR_TYPE_TEXT     Consts.BR_TYPE_IMAGE_TEXT   Consts.BR_TYPE_VOICE_TEXT   .....
	*/
	public native int AfDBBCChapterInsert(int ds_type, byte bc_type, String mid, String title, String time, String content, String country, byte purview, String afid, 
			                              String tag, int total_like, int total_comment, int status, String lat, String lng, AfFriendInfo profile, String des,
			                              String share_mid, String tagvip_pa, String pic_rule, int share_flag, int aid, int share_del,byte content_flag);
	
	public native int AfDBBCChaptersInsert(int ds_type, byte bc_type, String mid, String title, String time, String content, String country, byte purview, String afid, 
			                              String tag, int total_like, int total_comment, int status, String lat, String lng, int pos, AfFriendInfo profile, String desc,
			                              String share_mid, String tagvip_pa, String pic_rule, int share_flag, int aid, int share_del, byte content_flag);
	public native int AfDBPepolesInsert(int ds_type, String distance, int logout_time, int pos, AfFriendInfo profile);
	public native AfResponseComm AfDBPeoplesChaptersList(int offset, int count, int type);
	public native int AfDBPeoplesChaptersDeleteByType(int type);
	
	/**offset�?1表示查询最新的*/
	public native AfData AfDBBCNotifyList(int offset, int count, int _status, int type, String toAfid);
	public native AfData AfDBBCNotifyListByStatus(String toAfid, int _status, int type);
	public native int AfDBBCNotifyInsert(String toAfid, String afId, String mid, int type, int status, long server_time, String msg, String name, int age, byte sex,String sign,String region,String head_img_path,String safid,String sname,String content,byte bctype);
	public native int AfDBBCNotifyDeleteByID(int _id);
	public native int AfDBBCNotifyDeleteByStatus(String toAfid, int _status);
	public native int AfDBBCNotifyUpdataStatusByID(int _id, int _status);
	public native int AfDBBCNotifyUpdataAllStatus(String toAfid, int oldstatus, int newstatus);
	
 /*
	*Broadcast50 delete media file from db.
	*/
    public native int AfDBBCMFileDeleteByID(int aid);

    /*
	*Broadcast50 delete like info from db.
	*/
	public native int AfDBBCLikeDeleteByID(int aid);
	 
    /*
	*Broadcast50 delete comment info from db.
	*/
	public native int AfDBBCCommentDeleteByID(int aid);

	/*
	*Broadcast50 delete chapter info from db.
	*/
	public native int AfDBBCChapterDeleteByID(int _id);
	public native int AfDBBCChapterDeleteByType(int type);  //type: Consts.DATA_FROM_LOCAL  Consts.DATA_FROM_SERVER

   /*
	*getrecord by database id 
	*/   
    public native AfResponseComm AfDBBCChapterFindByID(int _id);

   /*
	*find chapter by afid or status. afid is null the condition will uneffect, status < 0 the status is uneffect.
	*/   
    public native AfResponseComm AfDBBCChapterFindByAFIDStatus(String afid, int status);

   
    /*
	*get all record list from database
	*/  
    public native AfResponseComm AfDBBCChapterGetList();
    public native AfResponseComm AfDBBCChapterGetListEx(int offset, int count, int type);  //type: Consts.DATA_FROM_LOCAL  Consts.DATA_FROM_SERVER

    /*
	*update mid by _id
	*/ 
	public native int AfDBBCChapterUpdateMidByID(String mid, int _id);

    /*
	*update mstoken by _id
	*/ 
	public native int AfDBBCChapterUpdateMstokenByID(String mstoken, int _id);
	public native int AfBcLikeFlagSave(String mid);
	public native boolean AfBcLikeFlagCheck(String mid);
    /*
	*update chapter status by _id
	*/ 
    public native int  AfDBBCChapterUpdateStatusByID(int status, int _id);

    /*
	*update comment status by _id
	*/
    public native int  AfDBBCCommentUpdateStatusByID(int status, int _id);
    public native int  AfDBBCCommentUpdateCidByID(String cid, int _id);
 
     /*
	*update like status by _id
	*/
    public native int  AfDBBCLikeUpdateStatusByID(int status, int _id);

	 /*
	*update mfile status by _id
	*/
    public native int  AfDBBCMfileUpdateStatusByID(int status, int _id);
    
  //**********************************5.2 bc tag start doc 18.27~28.34*********************************/
    // http
    public native int AfBCGetTrends(int pageid);
    public native int AfBCGetTrendsMore(int pageid, int start, int limit);
    public native int AfBCGetTags(int pageid, int start, int limit, String tagname, int flag);
    public native int AfBCShareOpt(String toafid, long sid, String mid, String tagname, String picurl, int flag);
    public native int AfBCGetDefaultTags();
    public native int AfBcgetChaptersByNewTag(int flag, int page_id, int page_start, int page_limit, String tag);
    public native int AfBCLangPackage(String langver);
	public native int AfBcgetRegionBroadcast();
	public native int AfBcgetByStateOrCity(int pageid, int start, int limit, String country, String state, String city);
	
	public native int HttpCheckIlleagalWord(String[] wordarray);
    
    // tagAttr db 广播�?	
	public native int AfDbBCTagAttrInsert(AfBroadCastTagInfo info);
	public native int AfDbBCTagAttrDeleteByAid(int aid);
	public native int AfDbBCTagAttrDeleteCmd(int aid, int status, int type);
	public native AfResponseComm AfDbBCTagAttrFindByAid(int aid);  // AfResponseComm.obj.obj.list_tags
	public native int AfDbBCTagAttrUpdateStatusByAid(int status, int aid);
	
	// taginfo db
	public native int AfDbBCTagInfoInsert(AfTagInfo info);
	public native int AfDbBCTagInfoDeleteByType(int type);
	public native int AfDbBCTagInfoDeleteAll();
	public native AfTagGetTagsResp AfDbBCTagInfoGetList(int type);  // AfTagGetTagsResp.tags_list
	public native AfTagGetTagsResp AfDbBCTagInfoGetLimitList(int offset, int limit, int type);  // AfTagGetTagsResp.tags_list
	
	// defaulttrends db
	public native int AfDbBCTagDefaultTrendSave2Db(AfTagGetDefaultTrend info);
	public native AfTagGetTrendsResp AfDbBCTagGetDefaultTrend();  // AfTagGetTagsResp.defaluttrend
	public native int AfDbBCTagDeleteDefaultTrend();
	
	
	// lang db
	public native int AfDbBCLangPackageListDeleteAll();
	public native int AfDbBCLangPackageListInsert(String[] items, byte type); 
	public native AfTagGetLangPackageResp AfDbBCLangPackageListSearch(String tag, int offset, int limit);
	public native AfTagGetLangPackageResp AfDbBCLangPackagGetLimitListDb(int type,int offset, int limit);
  
    //**********************************5.2 bc tag  end doc 18.27~28.34 *********************************/
	
	//**********************************5.3 palmcall start doc 24.1~24.4  *********************************/
	
	public native int HttpPalmCallGetHotList(boolean gift);
	public native int HttpPalmCallMakeCall(String afid);
	public native int HttpPalmCallUploadIntroMedia(String content, String filepath, int format, int duration);
    public native int HttpPalmCallGetLeftTime();
    public native int HttpPalmCallSetAlonePeriod(int open, int startTime, int endTime);
    public native int HttpPalmCallGetAlonePeriod();
    public native int HttpPalmCallGetInfo(String afid);
    public native int HttpPalmCallGetInfoBatch(String[] afids);


	public native int AfDbPalmCallRecordInsert(AfPalmCallResp.AfPalmCallRecord record);
	public native int AfDbPalmCallRecordUpdate(AfPalmCallResp.AfPalmCallRecord record); // now only update type
	public native int AfDbPalmCallRecordDelete(int record);
	public native AfPalmCallResp AfDbPalmCallRecordGetList();
	public native AfPalmCallResp AfDbPalmCallRecordGetLimitList(int offset, int limit,int type);


    public native int AfDbPalmCallInfoInsert(AfPalmCallResp.AfPalmCallHotListItem info);
    public native int AfDbPalmCallInfoDelete(String afid);
    public native AfPalmCallResp AfDbPalmCallInfoGet(String afid);// (ArrayList<AfPalmCallHotListItem>) AfPalmCallResp.respobj,only one
    public native int AfDbPalmCallInfoUpdate(AfPalmCallResp.AfPalmCallHotListItem info);
	
	//**********************************5.3 palmcall end doc 24.1~24.4  *********************************/

   
	/***********************************************************************************************************************/
 	//gpr profile database
 	/***********************************************************************************************************************/
	
	/**
	 * grp profile operation
	 * 
	 * @param opr_type
	 *            0: insert 1: update 2: remove
	 * @return none
	 * @note
	 */
	public native int AfDbGrpProfileOpr(AfGrpProfileInfo elem, byte opr_type);
	public native int AfDbGrpProfileUpdateSid(String afid, int sid);
	public native int AfDbGrpProfileUpdateName(String afid, String name);
	public native int AfDbGrpProfileSetStatus(String afid, int status);
	public native int AfDbGrpProfileGetStatus(String afid);
	public native int AfGetGrpMember(String gid);
	public native int AfGetPublicGrpList(int flag, int page_id,int page_start,int page_limit, double lng, double lat, String category, String tag);
	public native int AfGetPublicGrpHotList(int page_limit);
    public native int AfGetPublicGrpTagsList();
    public native int AfGetPublicGrpByKeyword(String keyWord);
    public native int AfGetPublicGrpByGid(String gis);
    
    public native int AfLotteryInit();
    public native int AfTurnLottery(String afid);
    public native int AfGetPredictActionList(int flag, int type, String afid, int start, int limit);
    public native int AfPredictPrepareBet(String afid, String actionId, String itemId, long points);
    public native int AfPredictSureBet(String afid, String actionId, String itemId);
    public native int AfPredictGetPoints(String afid);
    public native int AfPredictGetCoins(String afid);
    public native int AfPredictBetHistory(String afid,int start,int limit);
    public native int AfPredictRanking(String afid,int start,int limit, String type);
    public native int AfPredictFeedback(String afid,String contents);
    public native int AfGetPointsDetail(String afid,int start,int limit);
    public native int AfPredictAnnouncement(String afid,int start,int limit);
    public native int AfPredictGiftAds(String afid,int start,int limit);
    public native int AfPredictGiftList(String afid,int start,int limit,int type);
    public native int AfPredictGetAddressRecord(String afid,int start,int limit);
    public native int AfPredictSetAddress(String afid,int giftId,String country,String province,String city,String consignee,String phone,String firstName,String lastName,String address,String landmark);
	public native int AfPredictGiftsExchangeList(String afid,int start,int limit,int type);
	public native int AfPredictGetFriendlist(String afid, int flag, int start, int limit, long inviteFriendsVersion);
	
	public native int AfGetFrdConnMidList(int pageId,int start,int limit);
	public native int AfGetFrdConnList(String mid_js);
	public native int AfGetFrdConnListPage(int pageId,int start,int limit);
	public native int AfPaymentRecharge(int flag,String afid,String card_No,String card_pwd,int coins,String sn,String front_url,String channel);
	public native int AfPaymentGetSMS(String afid,String front_url1);
	public native int AfPaymentGetPagaOrderId(String afid,String front_url,String orderMoney,String giftMoney,String currency,String moneyType,String remark,String payGateway,String returnUrl);
	
	/*5.1.3 New Payment*/
	public native int AfNewPaymentCoins2GoodsList(int coin2goods_type);
	public native int AfNewPaymentBillHistoryList(int transtype,int pageid, int start,int limit);
	public native int AfNewPaymentMoney2CoinsList();
	public native int AfNewPaymentAirtimeTopup(AFAirtimeTopupReq req);
	public native int AfNewPaymentGetRCOrderId(AFRechargeOrderReq req);
	public native int AfNewPaymentGetRechargeOrderLogs(int index, int size);

    public native int HttpPaymentAnalysis(String data);

	//Notification
//	public native AfPrizeInfo AfDBPrizeNotifyList(int offset, int count, int _status, String toAfid);
//	public native AfPrizeInfo AfDBPrizeNotifyListByStatus(String toAfid, int _status);
//	public native int AfDBPrizeNotifyInsert(String toAfId,String actiontitle,String actionId,String desc,int points,int status);
//	public native int AfDBPrizeNotifyDeleteByID(int _id);
//	public native int AfDBPrizeNotifyDeleteByStatus(String toAfid, int _status);
//	public native int AfDBPrizeNotifyUpdataStatusByID(int _id, int _status);
//	public native int AfDBPrizeNotifyUpdataAllStatus(String toAfid, int oldstatus, int newstatus);
	
	public native AfPrizeInfo AfDBRechargeNotifyList(int offset, int count, int _status, String toAfid);
	public native AfPrizeInfo AfDBRechargeNotifyListByStatus(String toAfid, int _status);
	public native int AfDBRechargeNotifyInsert(String toAfId,String pay_money,String gift_money,String currency,String pay_gateway,String pay_points,String gift_points,int status);
	public native int AfDBRechargeNotifyDeleteByID(int _id);
	public native int AfDBRechargeNotifyDeleteByStatus(String toAfid, int _status);
	public native int AfDBRechargeNotifyUpdataStatusByID(int _id, int _status);
	public native int AfDBRechargeNotifyUpdataAllStatus(String toAfid, int oldstatus, int newstatus);
    
	public native String AfGetDispatchUrl();
	public native String AfGetLoginService();
	public native int AfGetGIS(String name);

	// public static int test_handle = 0;
	private void remove(int type, AfMessageInfo afMessageInfo) {
		if (type == AfMessageInfo.MESSAGE_TYPE_MASK_PRIV) {
			AfDbMsgRmove(afMessageInfo._id);
		} else if (type == AfMessageInfo.MESSAGE_TYPE_MASK_GRP) {
			AfDbGrpMsgRemove(afMessageInfo._id);
		}
		if (afMessageInfo.attach_id > 0) {
			if (AfMessageInfo.MESSAGE_IMAGE == type) {
				AfDbAttachImageRmove(afMessageInfo.attach_id);
				if (null != afMessageInfo.attach) {
					AfAttachImageInfo imageInfo = (AfAttachImageInfo) afMessageInfo.attach;
					if (imageInfo.upload_id > 0) {
						AfDbImageReqRmove(imageInfo.upload_id);
					}
				}
			} else if (AfMessageInfo.MESSAGE_VOICE == type) {
				AfDbAttachVoiceRmove(afMessageInfo.attach_id);
			}
			 else if (AfMessageInfo.MESSAGE_PUBLIC_ACCOUNT == type || AfMessageInfo.MESSAGE_PA_URL == type) {
				 AfDbAttachPAMsgRmove(afMessageInfo.attach_id);
				}
		}
	}

	public void AfDbMsgRmove(AfMessageInfo afMessageInfo) {
		remove(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, afMessageInfo);
	}

	public void AfDbGrpMsgRmove(AfMessageInfo afMessageInfo) {
		remove(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, afMessageInfo);
	}

	/***********************************************************************************************************************/
	// cache process
	/***********************************************************************************************************************/
	private AfHttpSysListener mAfHttpSysListener;

	public void setHttpSysListener(AfHttpSysListener listener) {
		mAfHttpSysListener = listener;
	}

	/*static {
		System.loadLibrary("afmobi");
		System.loadLibrary("afmobigif");
		System.loadLibrary("Patcher");//增量更新�?	}
	}*/

	/***********************************************************************************************************************/
	// network proxy
	/***********************************************************************************************************************/

	private static final byte NET_STATE_OFF = 0;

	private static final byte NET_STATE_MOBILE = 1;

	private static final byte NET_STATE_ROAM = 2;

	private static final byte NET_STATE_WIFI = 3;

	private final static String[] getNetworkState() {
		String[] ret = new String[3];
		byte state = NET_STATE_OFF;
		final Context app = getContext();

		if (null != app) {
			try {
				ConnectivityManager conman = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo ni = conman.getActiveNetworkInfo();
				if (null != ni && ni.isAvailable()) {
					if (ni.isRoaming()) {
						state = NET_STATE_ROAM;
					} else if (ni.isConnectedOrConnecting()) {
						state = (ni.getType() == ConnectivityManager.TYPE_WIFI) ? NET_STATE_WIFI : NET_STATE_MOBILE;
						state = NET_STATE_WIFI;
						if (state == NET_STATE_MOBILE) {
							// read proxy
							@SuppressWarnings("deprecation")
							String proxyHost = android.net.Proxy.getDefaultHost();
							@SuppressWarnings("deprecation")
							int proxyPort = android.net.Proxy.getDefaultPort();

							if (null != proxyHost && proxyHost.length() > 0) {
								state = NET_STATE_MOBILE;
								ret[1] = String.valueOf(proxyPort);
								ret[2] = new String(proxyHost);

							} else {
								state = NET_STATE_WIFI;
							}
						}
					}
					ni = null;
				}
			} catch (Exception e) {

			}
		}
		ret[0] = String.valueOf((int) state);
		return ret;
	}

	/***********************************************************************************************************************/
	// to Main thread process
	/***********************************************************************************************************************/
	private static class HttpListenerInner {
		private AfHttpProgressListener mProgressListener;
		private AfHttpResultListener mResultListener;
		private Object mUserData;

		HttpListenerInner(AfHttpResultListener result, AfHttpProgressListener progress, Object obj) {
			mResultListener = result;
			mProgressListener = progress;
			mUserData = obj;
		}
	}

	private static class HttpResponseInner {
		int httpHandle;
		int flag;
		int code;
		int http_code = Consts.HTTP_CODE_UNKNOWN;
		Object result;
		int progress;

		// int user_data;

		HttpResponseInner(int httpHandle, int flag, int code, int http_code, Object result, int progress, int user_data) {
			this.httpHandle = httpHandle;
			this.code = code;
			this.result = result;
			this.progress = progress;
			this.http_code = http_code;
			// this.user_data = user_data;
			this.flag = flag;
		}
	}

	private HashSet<AfHttpSysListener> mSysListener = new HashSet<AfHttpSysListener>();
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, HttpListenerInner> mHttpListener = new HashMap<Integer, HttpListenerInner>();

	static private Handler getMainHandle() {
		if (null != mAfPalmchat) {
			return mAfPalmchat.mMainHandler;
		}
		return null;
	}

	static public Context getContext() {
		if (null != mAfPalmchat) {
			return mAfPalmchat.mContext;
		}
		return null;
	}

	static public AfPalmchat getAfCorePalmchat() {
		return mAfPalmchat;
	}

	private static final int AF_MSG_RESULT = 0;
	private static final int AF_MSG_PROGRESS = 1;
	private static final int AF_MSG_SYS = 2;

	// if result function, progress must be -1
	// if progress funcation, progresss must be large and equal 0
	private static Object mObject = new Object();
	private final static void AfOnResultInner(int httpHandle, int flag, int code, int http_code, Object result, int progress, int user_data) {
		synchronized(mObject){
			Handler mainHandler = getMainHandle();
			if (null != mainHandler) {
				 /*if(flag == Consts.REQ_PAYMENT_SMS_RECHARGE)
				 { 
					 int i; 
					 i =0;
					 //AfStoreProdList prod = (AfStoreProdList)result;
					 //AfStoreProdDetail prod = (AfStoreProdDetail)result;
					 //AfStoreConsumeRecord prod = (AfStoreConsumeRecord)result; 
					// AfPaymentCommon pay = (AfPaymentCommon)result;
					 i = i+1; 
				 }*/
				
				    HttpResponseInner response = new HttpResponseInner(httpHandle, flag, code, http_code, result, progress, user_data);
				    Message message = Message.obtain(mainHandler, (progress < 0 ? AF_MSG_RESULT : AF_MSG_PROGRESS), 0, 0, response);
				    mainHandler.sendMessage(message);
			}
		}
	}

	private final static void AfSysMsgProcInner(int msg, Object wparam, int lparam) {
		
		Handler mainHandler = getMainHandle();

		if (null != mainHandler) {
			Message message = Message.obtain(mainHandler, AF_MSG_SYS, msg, lparam, wparam);
			mainHandler.sendMessage(message);
		}

	}
	/**
	 * 生成缓存图片md5的时候要先去掉服务器地址部分，因为地址是会变的 而后面部分是不会变的 当IP地址变的时候也能找到对应的缓存�?而不用从新去下载
	 * @param url
	 * @return
	 */
	public String getMD5_removeIpAddress(String url){
		if(!TextUtils.isEmpty(url)){
			if(url.startsWith("http://")){
				int _inx=url.substring(7).indexOf("/");
				if(_inx>=0){
					url=url.substring(_inx+7);
				} 
			}
			return AfGetmd5(url.getBytes());
		}
		return "";
	}
	private Handler mMainHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AF_MSG_RESULT:
			case AF_MSG_PROGRESS: {
				HttpResponseInner response = (HttpResponseInner) msg.obj;
				HttpListenerInner listener = (HttpListenerInner) mHttpListener.get(response.httpHandle);
				if (null != listener) {
					if (msg.what == AF_MSG_RESULT) {
						if (null != listener.mResultListener) {
							AfRemoveHttpListener(response.httpHandle);
							try{
								listener.mResultListener.AfOnResult(response.httpHandle, response.flag, response.code, response.http_code, response.result, listener.mUserData);
							}catch(Exception e){
								PalmchatLogUtils.e("AfOnResult", e.toString());
							}
						}
					} else {
						if (null != listener.mProgressListener) {
							listener.mProgressListener.AfOnProgress(response.httpHandle, response.flag, response.progress, listener.mUserData);
						}
					}
				}

			}
				break;

			case AF_MSG_SYS: {
				Iterator<AfHttpSysListener> it = mSysListener.iterator();
				AfHttpSysListener listener;

				if (null != mAfHttpSysListener && mAfHttpSysListener.AfHttpSysMsgProc(msg.arg1, msg.obj, msg.arg2)) {
					break;
				}
				while (it.hasNext()) {
					listener = it.next();
					if (null != listener && listener.AfHttpSysMsgProc(msg.arg1, msg.obj, msg.arg2)) {
						break;
					}
				}
			}
				break;
			}
		}

	};

	public void AfAddHttpSysListener(AfHttpSysListener listener) {
		mSysListener.add(listener);
	}

	public void AfRemoveHttpSysListener(AfHttpSysListener listener) {
		if (null != listener) {
			mSysListener.remove(listener);
		}
	}

	private void AfAddHttpListener(int httpHandler, AfHttpResultListener result, AfHttpProgressListener progress, Object obj) {
		if (Consts.AF_HTTP_HANDLE_INVALID != httpHandler && (null != result || null != progress)) {
			mHttpListener.put(httpHandler, new HttpListenerInner(result, progress, obj));
		}
	}

	public void AfRemoveHttpListener(int httpHandler) {
		if (Consts.AF_HTTP_HANDLE_INVALID != httpHandler) {
			mHttpListener.remove(httpHandler);
		}
	}

	public void AfHttpRemoveAllListener() {
		Handler mainHandler = getMainHandle();
		if (null != mainHandler) {
			mainHandler.removeCallbacks(null);
		}
		HttpRemoveAllListener();
		mHttpListener.clear();
	}

	/***********************************************************************************************************************/
	// to Main thread process
	/***********************************************************************************************************************/
	public AfPalmchat() {

	}

	private static AfPalmchat mAfPalmchat = null;
	private CacheManager mCacheManagerInstance = null;

	private Context mContext;

	public static synchronized AfPalmchat create(Context app, AfPhoneInfoParam phoneInfo, AfHttpSysListener listener) {
		if (null == mAfPalmchat) {
			mAfPalmchat = new AfPalmchat();
			getCacheManager();
			mAfPalmchat.mContext = app;
			mAfPalmchat.AfInit(phoneInfo);
			mAfPalmchat.setHttpSysListener(listener);
		}
		return mAfPalmchat;
	}

	static public final CacheManager getCacheManager() {
		if (null != mAfPalmchat) {
			if (null == mAfPalmchat.mCacheManagerInstance) {
				mAfPalmchat.mCacheManagerInstance = new CacheManager();
			}
			return mAfPalmchat.mCacheManagerInstance;
		}
		return null;
	}

	/**
	 * init afmobi palmchat core
	 * 
	 * @param param
	 *            the current phone info, eg: country code, resource work
	 * @return true: success false:failure
	 * @note
	 */
	public native boolean AfInit(AfPhoneInfoParam param);

	/**
	 * AfSetScreen
	 * 
	 * @param param
	 * 
	 * @return
	 * @note
	 */
	public native void AfSetScreen(int widht, int height);

	/**
	 * This function set mcc \return no
	 * 
	 * \sa
	 */
	public native void AfSetMcc(String mcc);

	/**
	 * exit asynchronouslly the whole application
	 * 
	 * @param none
	 * 
	 * @return none
	 * @note
	 */
	public native void AfAsynchronousDestroy();

	/**
	 * exit Synchronous the whole application
	 * 
	 * @param none
	 * 
	 * @return none
	 * @note
	 */
	public native void AfSynchronousDestroy();

	/**
	 * select the current logined palmchat user and start to load core sqilt and
	 * requir managerment.
	 * 
	 * @param afid
	 *            palmchat afid
	 * 
	 * @param password
	 *            password
	 * 
	 * @param country_code
	 *            the current palmchat country cod
	 * 
	 * @return true: success false: failur
	 * @note
	 */
	public native boolean AfLoadAccount(AfLoginInfo login);

	/**
	 * cancel the specified http req
	 * 
	 * @param httpHandle
	 *            http handle
	 * @return none
	 * @note
	 */
	public void AfHttpCancel(int httpHandle) {
		if (Consts.AF_HTTP_HANDLE_INVALID != httpHandle) {
			AfRemoveHttpListener(httpHandle);
			HttpCancel(httpHandle);
		}
	}

    /**
    * AfCoreVersionDownloadHttpCancel
    * @param http_handle:    
    * @note:  code	flag:
    */
   public void AfCoreVersionDownloadHttpCancel(int httpHandle){
	   
		if (Consts.AF_HTTP_HANDLE_INVALID != httpHandle) {
			VersionDownloadHttpCancel(httpHandle);
			HttpCancel(httpHandle);
		}  
   }
	/**
	 * login a palmchat user
	 * 
	 * @param user_name
	 *            : afid/imei/email/phone number
	 * @param password
	 *            : pass word
	 * @param country_code
	 *            : country code, eg: if this is China, country_code == 86
	 * @param type
	 *            : login type, specify user_name type
	 * 
	 * @param user_data
	 *            : user data, equal with the user_data element of
	 *            AfHttpResultListener
	 * @param result
	 *            : login result interface result of AfProfileInfo
	 * @return http handle
	 * @note
	 */
	public int AfHttpLogin(String user_name, String password, String country_code, byte type, Object user_data, AfHttpResultListener result) {
		//
		int handle = HttpLogin(user_name, password, country_code, type, 0);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}
	public int AfHttpRegLogin(AfRegInfoParam param, String user_id, byte type, Object user_data, AfHttpResultListener result) {
		int handle = HttpRegLogin(param, user_id, type, 0);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * registe a palmchat user by phone or email
	 * 
	 * @param param
	 *            : register info
	 * @param req_flag
	 *            : req flag, REQ_REG_BY_PHONE or REQ_REG_BY_EMAIL
	 * @param user_data
	 *            : equal with the user_data element of AfHttpResultListener
	 * 
	 * @param result
	 *            : login result interface
	 * 
	 * @return http handle
	 * @note: result of String, a new afid
	 */
	public int AfHttpRegister(AfRegInfoParam param, int req_flag, Object user_data, AfHttpResultListener result) {
		int handle = HttpRegister(param, req_flag, 0);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 *when phone register, notify sp  service send verify code to client..
	 * 
	 * @param param
	 *            : register info
	 * @param req_flag
	 *            : req flag, REQ_GET_SMS_CODE
     * @param voice
     *            : if voice == 1, get code by voice
	 * @param user_data
	 *            : equal with the user_data element of AfGetSMSCode
	 * 
	 * @param result
	 *            : login result interface
	 * 
	 * @return http handle
	 * @note: result of String, a new afid
	 */
	public int AfHttpGetSMSCode(int req_flag, String cc, String phone_number, byte sms_code_type, int voice, Object user_data, AfHttpResultListener result) {
		int handle = AfGetSMSCode(req_flag, cc, phone_number, sms_code_type,voice);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}
	
	/**
	 * change current user password
	 * 
	 * @param old_pwd
	 *            : old password
	 * @param new_pwd
	 *            : new password
	 * @param user_ip
	 *            : user ip , option item
	 * @param user_data
	 *            : equal with the user_data element of AfHttpResultListener
	 * @param result
	 *            : change password result interface
	 * @return http handle
	 * @note: code == Consts.REQ_CODE_SUCCESS: modify password success!
	 */
	public int AfHttpChangPwd(String old_pwd, String new_pwd, String user_ip, Object user_data, AfHttpResultListener result) {
		int handle = HttpChangPwd(old_pwd, new_pwd, user_ip, 0);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * change current user password
	 * 
	 * @param req_flag
	 *            : REQ_RESET_PWD_BY_PHONE: get password by phone
	 *            REQ_RESET_PWD_BY_EMAIL: get password by email
	 *            REQ_CHECK_ACCOUNT_BY_PHONE_EX: check account of phone after
	 *            login REQ_CHECK_ACCOUNT_BY_PHONE: check account of phone
	 *            before login REQ_CHECK_ACCOUNT_BY_EMAIL: check account of
	 *            email REQ_BIND_BY_PHONE: bind by phone REQ_BIND_BY_EMAIL: bind
	 *            by email
	 * @param phone_or_email
	 *            : phone or email
	 * @param country_code
	 *            : country code
	 * @param afid_or_sendvfy
	 *            : afid or send verify info
	 * @param user_ip
	 *            : user ip , option item
	 * @param user_data
	 *            : equal with the user_data element of AfHttpResultListener
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note:
	 */

	public int AfHttpAccountOpr(int req_flag, String phone_or_email, String country_code, String afid_or_sendvfy, String user_ip, String smscode, Object user_data, AfHttpResultListener result) {
		int handle = HttpAccountOpr(req_flag, phone_or_email, country_code, afid_or_sendvfy, user_ip, 0,smscode);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * logout a palmchat user
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpLogout
	 * 
	 * @param result
	 *            : logout result interface
	 * 
	 * @return http handle
	 * @note:
	 */
	public int AfHttpLogout(Object user_data, AfHttpResultListener result) {
		int handle = HttpLogout(0);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get afid profile or batch friend info
	 * 
	 * @param afid
	 *            : afid array, when req_flag == Consts.REQ_GET_INFO, afid only
	 *            one; when req_flag == Consts.REQ_GET_BATCH_INFO, mult-afid
	 * @param user_ip
	 *            : user ip , option item
	 * @param user_data
	 *            : equal with the user_data element of AfHttpLogout
	 * 
	 * @param result
	 *            : get info result interface
	 * 
	 * @return http handle
	 * @note: when req_flag == Consts.REQ_GET_INFO: AfProfileInfo when req_flag
	 *        == Consts.REQ_GET_BATCH_INFO, AfFriendInfo array
	 */
	public int AfHttpGetInfo(String[] afid, int req_flag, String user_ip, Object user_data, AfHttpResultListener result) {
		return AfHttpGetInfo(afid, req_flag, false, user_ip, user_data, result);
	}

	public int AfHttpGetInfo(String[] afid, int req_flag, boolean is_background, String user_ip, Object user_data, AfHttpResultListener result) {
		int handle = HttpGetInfo(afid, req_flag, is_background, user_ip, 0);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * add or delete a palmchat user
	 * 
	 * @param group
	 *            : default: all in order to extend
	 * @param uuid
	 *            : afid, if it is mult-afid, the sperator char is ':'
	 * @param action
	 *            : friend operation type, eg: HTTP_ACTION_A or HTTP_ACTION_D
	 * @param src
	 *            : friend source type
	 * @param type
	 *            : AFMOBI_FRIEND_TYPE_FF
	 * @param user_ip
	 *            : user ip , option item
	 * @param user_data
	 *            : equal with the user_data element of AfHttpFriendOpr
	 * @param result
	 *            : result interface
	 * 
	 * @return http handle
	 * @note:
	 */
	public int AfHttpFriendOpr(String group, String uuid, byte action, byte src, byte type, String user_ip, Object user_data, AfHttpResultListener result) {
		int handle = HttpFriendOpr(group, uuid, action, src, type, user_ip, 0);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}
	
	/**
	 * add or delete a block user
	 * 
	 * @param uuid
	 *            : afid, if it is mult-afid, the sperator char is ':'
	 * @param action
	 *            : friend operation type, now only HTTP_ACTION_A or HTTP_ACTION_D
	 * @param user_ip
	 *            : user ip , option item
	 * @param user_data
	 *            : equal with the user_data element of AfHttpBlockOpr
	 * @param result
	 *            : result interface
	 * 
	 * @return http handle
	 * @note: code	flag: REQ_BLOCK_LIST if action = HTTP_ACTION_A or HTTP_ACTION_D
	 */
	public int AfHttpBlockOpr(String uuid, byte action, String user_ip, Object user_data, AfHttpResultListener result) {
		int handle = HttpBlockOpr( uuid, action,  Consts.AFMOBI_FRIEND_TYPE_BF, user_ip, 0);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * clean gps info
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpGpsClean
	 * 
	 * @param result
	 *            : logout result interface
	 * 
	 * @return http handle
	 * @note:
	 */
	public int AfHttpGpsClean(Object user_data, AfHttpResultListener result) {
		int handle = HttpGpsClean(0);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * update profile info
	 * 
	 * @param param
	 *            : new profile info
	 * @param user_data
	 *            : equal with the user_data element of AfHttpUpdateInfo
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note:
	 */
	public int AfHttpUpdateInfo(AfProfileInfo param, Object user_data, AfHttpResultListener result) {
		int handle = HttpUpdateInfo(param, Consts.REQ_UPDATE_INFO);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * update profile info
	 * 
	 * @param param
	 *            : new profile info
	 * @param flag
	 *            : req flag: Consts.REQ_UPDATE_INFO or
	 *            Consts.REQ_UPDATE_INFO_BY_IMEI
	 * @param user_data
	 *            : equal with the user_data element of AfHttpUpdateInfo
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: AfProfileInfo
	 */
	public int AfHttpUpdateInfo(AfProfileInfo param, int flag, Object user_data, AfHttpResultListener result) {
		int handle = HttpUpdateInfo(param, flag);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * phone book backup or download
	 * 
	 * @param pb_uuid
	 *            : uuid list
	 * @param pb_name
	 *            : name list
	 * @param pb_phone
	 *            : phone number list
	 * @param action
	 *            : HTTP_ACTION_A
	 * @param start
	 *            : download start position
	 * @param limit
	 *            : download limited size
	 * @param user_data
	 *            : equal with the user_data element of AfHttpUpdateInfo
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note:
	 */
	public int AfHttpPhonebookBackup(String pb_name[], String pb_phone[], byte action, int start, int limit, Object user_data, AfHttpResultListener result) {
		int handle = HttpPhonebookBackUP(pb_name, pb_phone, action, start, limit, 0);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}
	
	/**
	 * get onlinestatus(new api)
	 * 
	 * @param afid
	 *            : afid
	 * @param user_data
	 *            : equal with the user_data element of AfHttpGetOnlineStatusNew
	 * @param result
	 *            : AfOnlineStatusInfo
	 * @return http handle
	 * @note: code	flag: REQ_GET_ONLINE_STATUS_NEW 
	 */
	public int AfHttpGetOnlineStatusNew(String afid, Object user_data, AfHttpResultListener result) {
		int handle = HttpGetOnlineStatusNew(afid);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * shake and shake
	 * 
	 * @param type
	 *            : extends, the default value: "S"
	 * @param limit
	 *            : search limited size
	 * @param user_ip
	 *            : user ip , option item
	 * @param user_data
	 *            : equal with the user_data element of AfHttpUpdateInfo
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note:
	 */
	public int AfHttpShakeAndShake(String type, int limit, String user_ip, Object user_data, AfHttpResultListener result) {
		int handle = HttpShakeAndShake(type, limit, user_ip, 0);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * near palmchat user by gps
	 * 
	 * @param param
	 *            : the class of AfNearByGpsParam
	 * @param user_data
	 *            : equal with the user_data element of AfHttpUpdateInfo
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: array of AfFriendInfo
	 */
	public int AfHttpNearByGps(AfNearByGpsParam param, Object user_data, AfHttpResultListener result) {
		int handle = HttpNearByGps(param, 0);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * look around
	 * 
	 * @param param
	 *            : param of lookaround
	 * @param user_data
	 *            : equal with the user_data element of AfHttpLookAround
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: array of AfFriendInfo
	 */
	public int AfHttpLookAround(AfSearchUserParam param, Object user_data, AfHttpResultListener result) {
		int handle = HttpSearchUser(Consts.REQ_LOOK_AROUND, param);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * pb import friend
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpPbImportFriend
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpPbImportFriend(Object user_data, AfHttpResultListener result) {
		int handle = HttpPbImportFriend(0);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * near palmchat user by gps
	 * 
	 * @param param
	 *            : the class of AfSearchUserParam
	 * @param user_data
	 *            : equal with the user_data element of AfHttpSearchUser
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: array of AfFriendInfo
	 */
	public int AfHttpSearchUser(AfSearchUserParam param, Object user_data, AfHttpResultListener result) {
		int handle = HttpSearchUser(Consts.REQ_SEARCH_USER, param);
		AfAddHttpListener(handle, result, null, user_data);

		return handle;
	}

	/**
	 * send message to another palmchat user
	 * 
	 * @param toAfid
	 *            : afid of reciever
	 * @param sid
	 *            : unique identify
	 * @param msg
	 *            : content of message or forwording fid
	 * @param command
	 *            : type of command
	 * @param user_data
	 *            : equal with the user_data element of AfHttpSearchUser
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpSendMsg(String toAfid, long sid, String msg, byte command, Object user_data, AfHttpResultListener result) {
		int handle = HttpSendMsg(toAfid, sid, msg, command, 0);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * send voice to another palmchat user
	 * 
	 * @param toAfid
	 *            : afid of reciever
	 * @param sid
	 *            : unique identify
	 * @param voice_data
	 *            : data of voice
	 * @param voice_len
	 *            : data length
	 * @param record_time
	 *            : voice playing length
	 * @param user_data
	 *            : equal with the user_data element of AfHttpSearchUser
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpSendVoice(String toAfid, long sid, byte[] voice_data, int voice_len, int record_time, Object user_data, AfHttpResultListener result, AfHttpProgressListener progress) {
		int handle = HttpVoiceSend(toAfid, sid, voice_data, voice_len, record_time, 0);
		AfAddHttpListener(handle, result, progress, user_data);
		return handle;
	}

	/**
	 * send image to another palmchat user
	 * 
	 * @param param
	 *            : send image relative
	 * @param user_data
	 *            : equal with the user_data element of AfHttpSearchUser
	 * @param result
	 *            : result interface
	 * @param progress
	 *            : progress interface
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpSendImage(AfImageReqInfo param, Object user_data, AfHttpResultListener result, AfHttpProgressListener progress) {
		int handle = HttpSendImage(param);
		AfAddHttpListener(handle, result, progress, user_data);
		return handle;
	}

	/***********************************************************************************************************************/
	// group chat relative
	/***********************************************************************************************************************/
	/**
	 * group relative interface
	 * 
	 * @param members
	 *            : member afid, mult-members with ',' splite
	 * @param name
	 *            : name: group name
	 * @param afid
	 *            : group afid
	 * @param req_flag
	 * 
	 *      private grp:
	 *            REQ_GRP_GET_PROFILE: AfGrpProfileInfo
	 *            REQ_GRP_CREATE AfGrpProfileInfo REQ_GRP_MODIFY
	 *            AfGrpProfileInfo REQ_GRP_REMOVE_MEMBER AfGrpProfileInfo
	 *            REQ_GRP_ADD_MEMBER AfGrpProfileInfo REQ_GRP_ADMIN_QUIT code
	 *            REQ_GRP_QUIT code
	 *      public grp:
	 *      
	 *            REQ_P_GRP_GET_PROFILE
	 *            REQ_P_GRP_MODIFY
	 *            REQ_P_GRP_BROADCAST
	 *            REQ_P_GRP_REMOVE_MEMBER
	 *            REQ_P_GRP_ADMIN_QUIT
	 *            REQ_P_GRP_ADD_MEMBER
	 *            REQ_P_GRP_QUIT
	 * @param user_data
	 *            : equal with the user_data element of AfHttpSearchUser
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpGrpOpr(String members, String name, String afid, int req_flag, Object user_data, AfHttpResultListener result) {
		return AfHttpGrpOpr(members, name, afid, req_flag, false, user_data, result);
	}
	public int AfHttpGrpOpr(String members, String name, String afid, int req_flag, boolean is_background, Object user_data, AfHttpResultListener result) {
		int handle = HttpGrpOpr(members, name, null, null, afid, req_flag, is_background);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}
	public int AfHttpGrpUpdateSigAndName(String name, String sig, String afid, Object user_data, AfHttpResultListener result) {
		int handle = HttpGrpOpr(null, name, sig, null, afid, Consts.REQ_GRP_MODIFY, false);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}
	public int AfHttpGrpSetBroadcast(String name, String notice, String afid, Object user_data, AfHttpResultListener result) {
		int handle = HttpGrpOpr(null, name, null, notice, afid, Consts.REQ_P_GRP_BROADCAST, false);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get current all the group info
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpSearchUser
	 * @param result
	 *            : result interface AfGrpListInfo
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpGrpGetList(Object user_data, AfHttpResultListener result) {
		int handle = HttpGrpGetList();
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	
	// avatar
	public int AfHttpHeadUpload(String filePath, String fileName, String format, Object user_data, AfHttpResultListener result, AfHttpProgressListener progress) {
		return AfHttpAvatarUpload(filePath, fileName, format, Consts.AF_HEAD_UPLOAD_INDEX, Consts.AF_HEAD_UPLOAD_INDEX, result, progress);
	}

	// avatar
	public int AfHttpAvatarUpload(String filePath, String fileName, String format, int index, Object user_data, AfHttpResultListener result, AfHttpProgressListener progress) {
		int handle = HttpAvatarUpload(filePath, fileName, format, index > Consts.AF_AVATAR_MAX_INDEX ? Consts.AF_AVATAR_MAX_INDEX : index);
		AfAddHttpListener(handle, result, progress, user_data);
		return handle;
	}
    	public int AfHttpAvatarwallModify(String filePath, String fileName, String format, String sn, Object user_data, AfHttpResultListener result, AfHttpProgressListener progress) {
		int handle = HttpAvatarwallModify(filePath, fileName, format, sn);
		AfAddHttpListener(handle, result, progress, user_data);
		return handle;
	}

	public int AfHttpAvatarDownload(String afid, String savePath, String sn, String px, int index, String server_url, boolean foreground, Object user_data, AfHttpResultListener result, AfHttpProgressListener progress) {
		int handle = HttpAvatarDownload(afid, savePath, sn, px, index, server_url, foreground);
		AfAddHttpListener(handle, result, progress, user_data);
		return handle;
	}

	public int AfHttpAvatarDownloadDirect(String afid, String savePath, String sn, String px, int index, String server_url, boolean foreground, Object user_data, AfHttpResultListener result, AfHttpProgressListener progress) {
		int handle = HttpAvatarDownloadDirect(afid, savePath, sn, px, index, server_url, foreground);
		AfAddHttpListener(handle, result, progress, user_data);
		return handle;
	}

	public int AfHttpAvatarDownload(String afid, String savePath, String sn, String px, int index, boolean foreground, Object user_data, AfHttpResultListener result, AfHttpProgressListener progress) {
		return AfHttpAvatarDownload(afid, savePath, sn, px, index, null, foreground, user_data, result, progress);
	}

	public int AfHttpAvatarDelete(String afid, String sn, int index, Object user_data, AfHttpResultListener result) {
		int handle = HttpAvatarDelete(afid, sn, index);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * download image
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpMediaDownload
	 * @param result
	 *            : result interface String file image path
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpMediaDownload(AfImageReqParam param, Object user_data, AfHttpResultListener result, AfHttpProgressListener progress) {
		int handle = HttpMediaDownload(param);
		AfAddHttpListener(handle, result, progress, user_data);
		return handle;
	}

	/**
	 * set login status
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpLoginStatus
	 * @param result
	 *            : result interface code
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpLoginStatus(byte status, Object user_data, AfHttpResultListener result) {
		int handle = HttpLoginStatus(status);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get password question
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpFindPwdGetQuestion
	 * @param result
	 *            : result interface string array, size is 3, question[0] ~
	 *            question[2]
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpFindPwdGetQuestion(String afid, byte type, Object user_data, AfHttpResultListener result) {
		int handle = HttpFindPwdGetQuestion(afid, type);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get set answer or verify question
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpFindPwdAnswer
	 * @param req_flag
	 *            : req flag REQ_PSD_SET_ANSWER: set answer after logining
	 *            account REQ_PSD_ANSWER verify question and answer
	 * @param afid_phone
	 *            : afid or phone number
	 * @param type
	 *            : select afid or phone type, Consts.AF_LOGIN_PHONE or
	 *            Consts.AF_LOGIN_AFID
	 * @param question
	 *            : question array
	 * @param answer
	 *            : answer array
	 * @param result
	 *            : result interface code
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpFindPwdAnswer(int req_flag, String afid_phone, byte type, String[] question, String[] answer, Object user_data, AfHttpResultListener result) {
		int handle = HttpFindPwdAnswer(req_flag, afid_phone, type, question, answer);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * search mutual friends
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpMutualFriends
	 * @param result
	 *            : result
	 * @return http handle
	 * @note: AfMutualFreindInfo[]
	 */
	public int AfHttpMutualFriends(Object user_data, AfHttpResultListener result) {
		int handle = HttpMutualFriends();
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * feed back to server
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpFeedback
	 * @param user
	 *            : afid or phone or imei
	 * @param msg
	 *            : feedback message
	 * @return http handle
	 * @note: true or false
	 */
	public int AfHttpFeedback(String user, String msg, Object user_data, AfHttpResultListener result) {
		int handle = HttpFeedback(user, msg);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/***********************************************************************************************************************/
	// chatroom relative interface
	/***********************************************************************************************************************/
	/**
	 * get chat room list
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpChatroomGetList
	 * @param isPalmguess
	 *            : set 0 when get normal chatroom list; setup 1 when get palmguess chatroom list;
	 * @return http handle
	 * @note: AfChatroomDetail array
	 */
	public int AfHttpChatroomGetList(Object user_data, int isPalmguess,  AfHttpResultListener result) {
		int handle = HttpChatroomGetList(isPalmguess);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get chat room member list
	 * 
	 * @param user_data
	 *            : equal with the user_data element of
	 *            AfHttpChatroomGetMemberList
	 * @return http handle
	 * @note: AfChatroomMemberInfo array
	 */
	public int AfHttpChatroomGetMemberList(String afid, Object user_data, AfHttpResultListener result) {
		int handle = HttpChatroomGetMemberList(afid);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * entry the specified chat room
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpChatroomEntry
	 * @return http handle
	 * @note: AfChatroomEntryInfo
	 */
	public int AfHttpChatroomEntry(String country, String cid, Object user_data, AfHttpResultListener result) {
		int handle = HttpChatroomEntry(country, cid);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * exit current chat room
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpChatroomExit
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpChatroomExit(Object user_data, AfHttpResultListener result) {
		int handle = HttpChatroomExit();
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * send msg to current chat room
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpChatroomSendMsg
	 * @param param
	 *            : send msg param
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpChatroomSendMsg(AfChatroomSendMsgParam param, Object user_data, AfHttpResultListener result) {
		int handle = HttpChatroomSendMsg(param);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * send msg to current chat room
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpChatroomSendMsg
	 * @param param
	 *            : send msg param
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpChatroomBmCmd(String afid, boolean forbid, Object user_data, AfHttpResultListener result) {
		int handle = HttpChatroomBmCmd(afid, forbid);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get register random from server
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpGetRegRandom
	 * @param phone
	 *            : phone number
	 * @param cc
	 *            : country code
	 * @return http handle
	 * @note: code int type reg random
	 */
	public int AfHttpGetRegRandom(String phone, String cc, int src, Object user_data, AfHttpResultListener result) {
		int handle = HttpGetRegRandom(phone, cc, src);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get contact info
	 */
	final public static ContactInfo[] AfGetContactInfo() {
		HashMap<String, ContactInfo> contact = ContactAPI.getAllContact();
		ContactInfo[] ret = null;

		if (null == contact) {
			return null;
		}
		int size = contact.size();
		ret = new ContactInfo[size];

		Set<Entry<String, ContactInfo>> set = contact.entrySet();
		Iterator<Entry<String, ContactInfo>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Entry<String, ContactInfo> entry = iterator.next();
			ContactInfo value = entry.getValue();
			if (!TextUtils.isEmpty(value.name) && !TextUtils.isEmpty(value.phone)) {
				size--;
				if (size >= 0) {
					ret[size] = value;
				} else {
					break;
				}
			}
		}

		contact = null;
		return ret;
	}

	/**
	 * get check version
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpVersionCheck
	 * @param productid
	 *            : ????
	 * @param afid
	 *            : current afid
	 * @param package:  包名
	 * @param channel:  渠道号，诸如androidxxxxx
	 * @return http handle
	 * @note: code result: AfVersionInfo
	 */
	public int AfHttpVersionCheck(String productid, String afid, String packagename, String channel, Object user_data, AfHttpResultListener result) {
		int handle = HttpVersionCheck(productid, afid,packagename,channel);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}
	
	/**
	 * download install package
	 * 
	 * @param user_data
	 *            : equal with the user_data element of
	 *            AfHttpInstallPackageDownload
	 * @param path
	 *            : file full path of saving install package
	 * @param url
	 *            : downloading url
	 * @return http handle
	 * @note: code the special cur_pos = 0, if file is exist, it is deleted.
	 */
	public int AfHttpInstallPackageDownload(String path, String url, long cur_pos, Object user_data, AfHttpResultListener result, AfHttpProgressListener progress) {
		int handle = HttpInstallPackageDownload(path, url, cur_pos);
		AfAddHttpListener(handle, result, progress, user_data);
		return handle;
	}

	/**
	 * This function This is the founction statistic to server is_logined :
	 * FALSE: before logined TRUE: after logined is_new_version: FALSE: before
	 * 4.0 version TRUE: after 4.0 version data: statistic data, json format
	 * \return The return value of http hanlde. \sa result:
	 */
	public int AfHttpStatistic(boolean is_logined, boolean is_new_version, String data, Object user_data, AfHttpResultListener result) {
		int handle = HttpStatistic(is_logined, is_new_version, data);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * This function This is the founction download file url : download file url
	 * path: save to the specified path AF_NULL: "res_dir/cr/" + md5 of the url
	 * \return The return value of http hanlde. \sa result:
	 */
	public int AfHttpDownloadFile(String url, String path, Object user_data, AfHttpResultListener result, AfHttpProgressListener progress) {
		int handle = HttpDownloadFile(url, path, null != progress);
		AfAddHttpListener(handle, result, progress, user_data);
		return handle;
	}

	/**
	 * This function This is the founction import bbm or facebook friends flag
	 * :REQ_FB_IMPROT_FD: facebook import REQ_BBM_IMPROT_FD: bbm import myid :
	 * my bbm identify or facebook identify friends : friend json data \return
	 * The return value of http hanlde. \sa result: code
	 */
	public int AfHttpImportFriends(int flag, String myid, String friends, Object user_data, AfHttpResultListener result) {
		int handle = HttpImportFriends(flag, myid, friends);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * send message to another palmchat user
	 * 
	 * @param lat
	 *            : lat of location
	 * @param lng
	 *            : lng of location
	 * @param msg
	 *            : content of broadcating message
	 * @param user_data
	 *            : equal with the user_data element of AfHttpSearchUser
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpSendBroadcast(double lat, double lng, long sid, String msg, int btype,  Object user_data, AfHttpResultListener result) {
		int handle = HttpSendBroadcast(Consts.REQ_BROADCAST_MSG_SEND, lat, lng, null, null, sid, msg, btype);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	public int AfHttpSendBroadcast(String region, String city, long sid, String msg,int btype, Object user_data, AfHttpResultListener result) {
		int handle = HttpSendBroadcast(Consts.REQ_BROADCAST_MSG_SEND_BY_CITY, 0, 0, region, city, sid, msg,btype);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

    /**
	 * upload media file to broadcast
	 * 
	 * @param media_path
	 *            : media file path
	 * @param media_type
	 *            : detail define in const.java
	 * @param user_data
	 *            : equal with the user_data element of AfHttpSearchUser
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code
	 */
	public int AfHttpBroadcastUpload(String url, String resume_url, String token, String media_path, int media_type, Object user_data, AfHttpResultListener result) {
		int handle = HttpBroadcastUpload(url, resume_url, token, media_path, media_type);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}
    
	/**
	 * send message to another palmchat user
	 * 
	 * @param lat
	 *            : lat of location
	 * @param lng
	 *            : lng of location
	 * @param start
	 *            : offset of broadcating message
	 * @param pageid
	 *            : base id of broadcating message
	 * @param limit
	 *            : max broadcasting message
	 * @param user_data
	 *            : equal with the user_data element of AfHttpBroadcastHistory
	 * @param result
	 *            : result interface
	 * @return http handle AfNearByGpsInfo array
	 * @note: code
	 */
	public int AfHttpBroadcastHistory(double lat, double lng, int start, int pageid, int limit, Object user_data, AfHttpResultListener result) {
		int handle = HttpBroadcastHistory(Consts.REQ_BROADCAST_GET_HISTORY, null, null, lat, lng, start, pageid, limit);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	public int AfHttpBroadcastHistory(String region, String city, int start, int pageid, int limit, Object user_data, AfHttpResultListener result) {
		int handle = HttpBroadcastHistory(Consts.REQ_BROADCAST_GET_HISTORY_BY_CITY, region, city, 0, 0, start, pageid, limit);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	public int AfHttpBroadcastDownload(String dl_url, String save_path, Object user_data, AfHttpResultListener result) {
		int handle = HttpBroadcastDownload(dl_url, save_path);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}
	/**
	 * send message to another palmchat user
	 * 
	 * @param afid
	 *            : reviced gift afid
	 * @param number
	 *            : number of sending flowers
	 * @param sid
	 *            : sequence id
	 * @param user_data
	 *            : equal with the user_data element of AfHttpBroadcastHistory
	 * @param result
	 *            : result interface
	 * @return http handle array index 0 : AfDatingInfo of sender 1:
	 *         AfDatingInfo of receiver
	 * @note: code
	 */
	public int AfHttpSendGift(String afid, int number, long sid, Object user_data, AfHttpResultListener result) {
		int handle = HttpSendGift(afid, number, sid);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get star info
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpGetStar
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code AfStarInfo
	 */
	public int AfHttpGetStar(Object user_data, AfHttpResultListener result) {
		int handle = HttpGetStar();
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * update dating info
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpGetStar
	 * @param result
	 *            : result interface
	 * @param dating
	 *            : modify dating info
	 * @param flag
	 *            : REQ_UPDATE_MARRIAGE: update marriage status
	 *            REQ_UPDATE_SHOWSTAR: update show start REQ_UPDATE_SHOWMOBILE:
	 *            update show phone and flowers
	 * @return http handle
	 * @note: code AfDatingInfo
	 */
	public int AfHttpUpdateDatingInfo(int flag, AfDatingInfo dating, Object user_data, AfHttpResultListener result) {
		int handle = HttpUpdateDatingInfo(flag, dating);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * update dating info
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpGetStar
	 * @param result
	 *            : result interface
	 * @param afid
	 *            : target afid
	 * @return http handle
	 * @note: code AfDatingInfo
	 */
	public int AfHttpGetDatingPhone(String afid, Object user_data, AfHttpResultListener result) {
		int handle = HttpGetDatingPhone(afid);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get gift history
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpGetGifts
	 * @param result
	 *            : result interface
	 * @param offset
	 *            : offset of history info
	 * @param count
	 *            : size of history items
	 * @param max
	 *            : when newly is ture, max of history items, but newly is
	 *            false, min of history items
	 * @return http handle
	 * @note: code array AfGiftInfo[] flag: REQ_GIFT_HISTORY
	 */
	public int AfHttpGetGifts(int offset, int count, int max, boolean newly, boolean db_opr, Object user_data, AfHttpResultListener result) {
		int handle = HttpGetGifts(offset, count, max, newly, db_opr);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * give gifts by sign
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpGiveGiftsBySign
	 * @param result
	 *            : result interface
	 * @return http handle type: GIVE_GITFS_TYPE_PHONE or GIVE_GITFS_TYPE_PHOTO
	 *         flag: REQ_GIFT_GIVE type: GIVE_GITFS_TYPE_SIGN flag:
	 *         REQ_GIFT_GIVE_BY_SIGN
	 * @note: code AfDatingInfo
	 */

	public int AfHttpGiveGifts(byte type, Object user_data, AfHttpResultListener result) {
		int handle = HttpGiveGifts((byte) type);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * promotion a certain afid to server
	 * 
	 * @param afid
	 *            : afid of promation
	 * @param user_data
	 *            : equal with the user_data element of AfHttpPromotion
	 * @param result
	 *            : result interface
	 * @return http handle flag: REQ_FLAG_PROMOTION
	 * @note: code
	 */
	public int AfHttpPromotion(String afid, Object user_data, AfHttpResultListener result) {
		int handle = HttpPromotion(afid);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * accuse a certain afid
	 * 
	 * @param afid
	 *            : afid of Accusation
	 * @param user_data
	 *            : equal with the user_data element of AfHttpAccusation
	 * @param result
	 *            : result interface
	 * @return http handle flag: REQ_FLAG_ACCUSATION
	 * @note: code
	 */
	public int AfHttpAccusation(String afid, Object user_data, AfHttpResultListener result) {
		int handle = HttpAccusation(afid);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * miss negeria join
	 * 
	 * @param flag
	 *            : REQ_MISS_NEGERIA_JOIN or REQ_MISS_NEGERIA_FIRST_JOIN
	 * @param user_data
	 *            : equal with the user_data element of AfHttpMissNegeriaJoin
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code when flag == REQ_MISS_NIGERIA_FIRST_JOIN, result: String
	 *        str[2] str[0]: Activity title str[1]: Activity content
	 */
	public int AfHttpMissNigeriaJoin(int flag, Object user_data, AfHttpResultListener result) {
		int handle = HttpMissNegeriaOpr(flag, 0, false);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get miss negeria home page
	 * 
	 * @param user_data
	 *            : equal with the user_data element of
	 *            AfHttpMissNegeriaHomePage
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code flag: REQ_MISS_NIGERIA_HOME result:AfMissNigeriaHomeInfo
	 */
	public int AfHttpMissNigeriaHomePage(Object user_data, AfHttpResultListener result) {
		int handle = HttpMissNegeriaOpr(Consts.REQ_MISS_NIGERIA_HOME, 0, false);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get miss negeria rank histroy
	 * 
	 * @param Is20Items
	 *            : false: 5 items true: 20 items
	 * @param pageid
	 *            : when Is20Items == true, 1: 1- 20 2: 21 - 40 .... when
	 *            Is20Items == false, 1: 1- 5 2: 6 - 10 ....
	 * @param user_data
	 *            : equal with the user_data element of AfHttpMissNegeriaRank
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code flag: REQ_MISS_NIGERIA_RANK result: AfProfileInfo[]
	 */
	public int AfHttpMissNigeriaRank(int pageid, boolean Is20Items, Object user_data, AfHttpResultListener result) {
		int handle = HttpMissNegeriaOpr(Consts.REQ_MISS_NIGERIA_RANK, pageid, Is20Items);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get public account list
	 * 
	 * @param user_data
	 *            : equal with the user_data element of
	 *            AfHttpPublicAccountGetList
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code flag: REQ_PUBLIC_ACCOUNT_LIST result: AfProfileInfo[]
	 */
	public int AfHttpPublicAccountGetList(boolean is_tag, Object user_data, AfHttpResultListener result) {
		int handle = HttpPublicAccountGetList(is_tag);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}
	
	/**
	 * get public account histrory list
	 * 
	 * @param user_data
	 *            : equal with the user_data element of
	 *            AfHttpPublicAccountGetHistory
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code flag: REQ_PUBLIC_ACCOUNT_HISTORY result: AfPublicAccountResp
	 */
	public int AfHttpPublicAccountGetHistory(String paid, int pageid, int start, int limit, Object user_data, AfHttpResultListener result) {
		int handle = HttpPublicAccountGetHistory(paid, pageid,start,limit);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get public account pa_url msg detail
	 *
	 * @param user_data
	 *            : equal with the user_data element of
	 *            AfHttpHttpPublicAccountGetDetail
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code flag: REQ_FLAG_POLLING_PA_GETDETAIL result: AfAttachPAMsgInfo
	 */
	public int AfHttpHttpPublicAccountGetDetail(String url, Object user_data, AfHttpResultListener result) {

		int handle = HttpPublicAccountGetDetail(url);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get store product list
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpStoreGetProdList
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code flag: REQ_STORE_PROD_LIST_GET result: AfStoreProdList[]
	 */
	public int AfHttpStoreGetProdList(int page_index, int page_size, int prod_type, int prod_categoty, Object user_data, AfHttpResultListener result) {
		int handle = AfStoreGetProdList(page_index, page_size, prod_type, prod_categoty);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * get store product detail infomation
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpStoreGetProdDetail
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code flag: REQ_STORE_PROD_DETAIL result: AfStoreProdDetail[]
	 */
	public int AfHttpStoreGetProdDetail(String item_id, Object user_data, AfHttpResultListener result) {
		int handle = AfStoreGetProdDetail(item_id);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * download store product file
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpStoreDownload
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code flag: REQ_STORE_DOWNLOAD result: none
	 */
	public int AfHttpStoreDownload(String url, String item_id, int img_pixel, String save_path, boolean zip_flag, Object user_data, AfHttpResultListener result, AfHttpProgressListener progress) {
		int handle = AfStoreDownload(url, item_id, img_pixel, save_path, zip_flag);
		// test_handle = handle;
		Log.e("AfHttpStoreDownload", handle + "");
		AfAddHttpListener(handle, result, progress, user_data);
		return handle;
	}

	/**
	 * get remain/rest afcoin
	 * 
	 * @param user_data
	 *            : equal with the user_data element of AfHttpStoreGetRemainCoin
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code flag: REQ_STORE_GET_REMAIN_COIN result: interger
	 */
	public int AfHttpStoreGetRemainCoin(Object user_data, AfHttpResultListener result) {
		int handle = AfStoreGetRemainCoin();
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * check version
	 * 
	 * @param user_data
	 *            : equal with the user_data element of
	 *            AfHttpStoreProdVersionCheck
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code flag: REQ_STORE_VERSION_CHECK result: AfStoreProdDetail[]
	 */
	public int AfHttpStoreProdVersionCheck(int prod_type, String ver_json, Object user_data, AfHttpResultListener result) {
		int handle = AfStoreProdVersionCheck(prod_type, ver_json);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * user consume record list
	 * 
	 * @param user_data
	 *            : equal with the user_data element of
	 *            AfHttpStoreGetConsumeRecord
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code flag: REQ_STORE_CONSUME_RECORD result: AfStoreConsumeRecord[]
	 */
	public int AfHttpStoreGetConsumeRecord(int prod_type,int page_index, int page_size, Object user_data, AfHttpResultListener result) {
		int handle = AfStoreGetConsumeRecord(prod_type, page_index, page_size);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 * user consume record list
	 * 
	 * @param user_data
	 *            : equal with the user_data element of
	 *            AfHttpStoreGetConsumeRecord
	 * @param result
	 *            : result interface
	 * @return http handle
	 * @note: code flag: REQ_STORE_CONSUME_RECORD result: AfStoreConsumeRecord[]
	 */
	public int AfHttpStoreHaveNewProd( Object user_data, AfHttpResultListener result) {
		int handle = AfStoreHaveNewProd();
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

	/**
	 *get current pcoin count
 
	 * @param user_data: 
	 *         equal with the user_data element of AfHttpPaymentGetVCoin                   
	 * @param result: 
	 *            result interface                
	 * @return http handle    
	 * @note:  code  flag: REQ_PAYMENT_VCOIN_GET result: AfHttpPaymentGetVCoin[]
	 */	
	public int AfHttpPaymentGetVCoin(Object user_data,AfHttpResultListener result){
		int handle =AfPaymentGetVCoin();
		AfAddHttpListener(handle, result, null, user_data);
		return handle;	
	}

/**
	 *GT card recharge
 
	 * @param user_data: 
	 *         equal with the user_data element of AfHttpPaymentGTRecharge                   
	 * @param result: 
	 *            result interface                
	 * @return http handle    
	 * @note:  code  flag: REQ_PAYMENT_GTCARD_RECHARGE result: AfHttpPaymentGTRecharge[]
	 */	
	public int AfHttpPaymentGTRecharge(String card_num, String card_pwd, int channel, Object user_data,AfHttpResultListener result){
		int handle =AfPaymentGTRecharge(card_num, card_pwd, channel);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;	
	}
	/**
	 *query transaction record
 
	 * @param user_data: 
	 *         equal with the user_data element of AfHttpPaymentQueryTransRecord                   
	 * @param result: 
	 *            result interface                
	 * @return http handle    
	 * @note:  code  flag: REQ_PAYMENT_TRANS_RECORD result: AfHttpPaymentQueryTransRecord[]
	 */	
	public int AfHttpPaymentQueryTransRecord(int page_index, int page_size, Object user_data,AfHttpResultListener result){
		int handle =AfPaymentQueryTransRecord(page_index, page_size);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;	
	}

	/**
	 *query transaction detail
 
	 * @param user_data: 
	 *         equal with the user_data element of AfHttpPaymentQueryTransDetail                   
	 * @param result: 
	 *            result interface                
	 * @return http handle    
	 * @note:  code  flag: REQ_PAYMENT_TRANS_DETAIL result: AfHttpPaymentQueryTransDetail[]
	 */	
	public int AfHttpPaymentQueryTransDetail(String trans_id, Object user_data,AfHttpResultListener result){
		int handle =AfPaymentQueryTransDetail(trans_id);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;	
	}
   /**
	 *payoff current object
 
	 * @param user_data: 
	 *         equal with the user_data element of AfHttpPaymentConsume                   
	 * @param result: 
	 *            result interface                
	 * @return http handle    
	 * @note:  code  flag: REQ_PAYMENT_CONSUME result: AfHttpPaymentConsume[]
	 */	
	public int AfHttpPaymentConsume(int consume_coin,int channel, Object user_data,AfHttpResultListener result){
		int handle =AfPaymentConsume(consume_coin, channel);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;	
	}

	 /**
	 *get recharge sn by smsm
 
	 * @param user_data: 
	 *         equal with the user_data element of AfHttpPaymentSmsGetsn                   
	 * @param result: 
	 *            result interface                
	 * @return http handle    
	 * @note:  code  flag: REQ_PAYMENT_SMS_GETSN result: AfHttpPaymentSmsGetsn[]
	 */	
	public int AfHttpPaymentSmsGetsn(Object user_data,AfHttpResultListener result){
		int handle =AfPaymentSmsGetsn();
		AfAddHttpListener(handle, result, null, user_data);
		return handle;	
	}
    /**
	 *sms recharge 
 
	 * @param user_data: 
	 *         equal with the user_data element of AfHttpPaymentSmsRecharge                   
	 * @param result: 
	 *            result interface                
	 * @return http handle    
	 * @note:  code  flag: REQ_PAYMENT_SMS_RECHARGE result: AfHttpPaymentSmsRecharge[]
	 */	
	public int AfHttpPaymentSmsRecharge(String sn, Object user_data,AfHttpResultListener result){
		int handle =AfPaymentSmsRecharge(sn);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;	
	}

	 /**
	 *follow operate command.
	 * @param user_data: 
	 *         equal with the user_data element of AfHttpFollowCmd                   
	 * @param result: 
	 *            result interface                
	 * @return http handle    
	 * @note:  code  flag: REQ_PAYMENT_SMS_RECHARGE result: AfHttpFollowCmd[]
	 */	
	public int AfHttpFollowCmd(int follow_type, String uuid_list/*id1:id2:id3...*/, int action, Object user_data,AfHttpResultListener result){
		int handle =AfFollowCmd(follow_type, uuid_list, action);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;	
	}

	/**
	 *get follow/followers with callback.
	 * @param follow_type: only Consts.AFMOBI_FOLLOW_MASTRE or Consts.AFMOBI_FOLLOW_PASSIVE
	 * @param user_data:
	 *         equal with the user_data element of AfFollowGetList
	 * @param result:
	 *            result interface
	 * @return http handle
	 * @note:  code  flag: REQ_FLAG_FOLLOW_LIST if  follow_type isConsts.AFMOBI_FOLLOW_MASTRE
	 *                      REQ_FLAG_FOLLOWERS_LIST if  follow_type isConsts.AFMOBI_FOLLOW_PASSIVE
	 * @note: result: String[]
	 */
	public int AfFollowGetList(int follow_type, Object user_data,AfHttpResultListener result){
		int handle = AfFollowGetList(follow_type);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}

     /**
	 *broadcast get people by the same city.
	 * @param user_data: 
	 *         equal with the user_data element of AfHttpBcNearbyCityPeoples                   
	 * @param result: 
	 *            result interface                
	 * @return http handle    
	 * @note:  code  flag: REQ_BCGET_PEOPLES_BY_CITY result: AfHttpBcNearbyCityPeoples[]
	 */	
	public int AfHttpBcNearbyCityPeoples(byte sex, int page_id, int page_start, int page_limit, boolean online, Object user_data,AfHttpResultListener result){
		int handle =AfBcNearbyCityPeoples(sex, page_id, page_start, page_limit,online);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;	
	}
    /**
	 *broadcast get people by the same state.
	 * @param user_data: 
	 *         equal with the user_data element of AfHttpBcNearbyStatePeoples                   
	 * @param result: 
	 *            result interface                
	 * @return http handle    
	 * @note:  code  flag: REQ_BCGET_PEOPLES_BY_STATE result: AfHttpBcNearbyStatePeoples[]
	 */	
	public int AfHttpBcNearbyStatePeoples(byte sex, int page_id, int page_start, int page_limit, boolean online, Object user_data,AfHttpResultListener result){
		int handle =AfBcNearbyStatePeoples(sex, page_id, page_start, page_limit,online);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;	
	}
   /**
	 *broadcast get peoples by GPS .
	 * @param user_data: 
	 *         equal with the user_data element of AfHttpBcNearbyGPSPeoples                   
	 * @param result: 
	 *            result interface                
	 * @return http handle    
	 * @note:  code  flag: REQ_BCGET_PEOPLES_BY_GPS: AfHttpBcNearbyGPSPeoples[]
	 */	
	public int AfHttpBcNearbyGPSPeoples(byte sex, int page_id, int page_start, int page_limit,double lng, double lat, boolean online, boolean isFarfaraway, Object user_data,AfHttpResultListener result){
		int handle =AfBcNearbyGPSPeoples(sex, page_id, page_start, page_limit,online,lng,lat,isFarfaraway);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;	
	}
   /**
	 *broadcast get peoples and public accounts by GPS .
	 * @param user_data: 
	 *         equal with the user_data element of AfHttpBcNearbyGPSPeoples                   
	 * @param result: 
	 *            result interface                
	 * @return http handle    
	 * @note:  code  flag: REQ_BCGET_GPS_PEOPLES: AfHttpBcNearbyGPSPeoples[]
	 */	
	public int AfHttpBcNearbyGPSPeoplesPAccounts(byte sex, int page_id, int page_start, int page_limit,double lng, double lat, boolean online, boolean isFarfaraway, Object user_data,AfHttpResultListener result){
		int handle =AfBcNearbyGPSPeoplesPAccounts(sex, page_id, page_start, page_limit,online,lng,lat,isFarfaraway);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;
	}
   /**
	*broadcast get chapters by city .
	* @param user_data: 
	*		  equal with the user_data element of AfHttpBcgetChaptersByCity					 
	* @param result: 
	*			 result interface				 
	* @return http handle	 
	* @note:  code	flag: REQ_BCGET_COMMENTS_BY_CITY: AfHttpBcgetChaptersByCity[]
	*/ 
   public int AfHttpBcgetChaptersByCity(int page_id, int page_start, int page_limit,double lng, double lat, Object user_data,AfHttpResultListener result){
	   int handle =AfBcgetChaptersByCity( page_id, page_start, page_limit);
	   AfAddHttpListener(handle, result, null, user_data);
	   return handle;  
   }
   /**
	*broadcast get chapters by state .
	* @param user_data: 
	*		  equal with the user_data element of AfHttpBcgetChaptersByState				 
	* @param result: 
	*			 result interface				 
	* @return http handle	 
	* @note:  code	flag: REQ_BCGET_COMMENTS_BY_STATE: AfHttpBcgetChaptersByCity[]
	*/ 
   public int AfHttpBcgetChaptersByState(int page_id, int page_start, int page_limit, Object user_data,AfHttpResultListener result){
	   int handle =AfBcgetChaptersByState( page_id, page_start, page_limit);
	   AfAddHttpListener(handle, result, null, user_data);
	   return handle;  
   }
   
  /**
   *broadcast get chaptser by GPS .
   * @param user_data: 
   *		 equal with the user_data element of AfHttpBcgetChaptersByGPS					
   * @param result: 
   *			result interface				
   * @return http handle	
   * @note:  code  flag: REQ_BCGET_COMMENT_BY_GPS: AfHttpBcgetChaptersByGPS[]
   */ 
  public int AfHttpBcgetChaptersByGPS(int page_id, int page_start, int page_limit,double lng, double lat, Object user_data,AfHttpResultListener result){
	  int handle =AfBcgetChaptersByGPS(page_id, page_start, page_limit,lng, lat);
	  AfAddHttpListener(handle, result, null, user_data);
	  return handle;  
  }

 /**
   *broadcast get chaptser by tag.
   * @param user_data: 
   *		 equal with the user_data element of AfHttpBcgetChaptersByTag					
   * @param result: 
   *			result interface				
   * @return http handle	
   * @note:  code  flag: REQ_BCGET_COMMENT_BY_TAG: AfHttpBcgetChaptersByTag[]
   */ 
  public int AfHttpBcgetChaptersByTag(int page_id, int page_start, int page_limit,String tag, Object user_data,AfHttpResultListener result){
	  int handle =AfBcgetChaptersByTag( page_id, page_start, page_limit,tag);
	  AfAddHttpListener(handle, result, null, user_data);
	  return handle;
  }
  /**
   *broadcast get chaptser city data by tag .
   * @param user_data: 
   *		 equal with the user_data element of AfHttpBcgetChaptersByTagCity					
   * @param result: 
   *			result interface				
   * @return http handle	
   * @note:  code  flag: REQ_BCGET_COMMENTS_BY_TAG_CITY: AfHttpBcgetChaptersByTag[]
   */ 
  public int AfHttpBcgetChaptersByTagCity(int page_id, int page_start, int page_limit,String tag, Object user_data,AfHttpResultListener result){
	  int handle =AfBcgetChaptersByTagCity( page_id, page_start, page_limit,tag);
	  AfAddHttpListener(handle, result, null, user_data);
	  return handle;
  }
  /**
   *broadcast get chaptser Gps data by tag.
   * @param user_data: 
   *		 equal with the user_data element of AfHttpBcgetChaptersByTagGps					
   * @param result: 
   *			result interface				
   * @return http handle	
   * @note:  code  flag: REQ_BCGET_COMMENTS_BY_TAG_GPS: AfHttpBcgetChaptersByTag[]
   */ 
  public int AfHttpBcgetChaptersByTagGps(int page_id, int page_start, int page_limit,String tag, Object user_data,AfHttpResultListener result){
	  int handle =AfBcgetChaptersByTagGps( page_id, page_start, page_limit,tag);
	  AfAddHttpListener(handle, result, null, user_data);
	  return handle;
  }
  
  /**
   *broadcast get chaptser by Mid.
   * @param user_data: 
   *		 equal with the user_data element of AfHttpBcgetChaptersByMid					
   * @param result: 
   *			result interface				
   * @return http handle	
   * @note:  code  flag: REQ_BCGET_COMMENTS_BY_MID: AfHttpBcgetChaptersByTag[]
   */ 
  public int AfHttpBcgetChaptersByMid(int page_id, int page_start, int page_limit, String mid, boolean is_only_com, Object user_data,AfHttpResultListener result){
	  int handle =AfBcgetChaptersByMid(page_id, page_start, page_limit, mid, is_only_com);
	  AfAddHttpListener(handle, result, null, user_data);
	  return handle;
  }
  /**
   *broadcast get chaptser Like by Mid.
   * @param user_data: 
   *		 equal with the user_data element of AfHttpBcgetChaptersLikeByMid					
   * @param result: 
   *			result interface				
   * @return http handle	
   * @note:  code  flag: REQ_BCGET_COMMENTS_LIKE_BY_MID: AfHttpBcgetChaptersByTag[]
   */ 
  public int AfHttpBcgetChaptersLikeByMid(int page_id, int page_start, int page_limit, String mid, Object user_data,AfHttpResultListener result){
	  int handle =AfBcgetChaptersLikeByMid(page_id, page_start, page_limit, mid);
	  AfAddHttpListener(handle, result, null, user_data);
	  return handle;
  }
  /**
   *broadcast get chaptser profile by Mid.
   * @param user_data: 
   *		 equal with the user_data element of AfHttpBcgetProfileByAfid					
   * @param result: 
   *			result interface				
   * @return http handle	
   * @note:  code  flag: REQ_BCGET_PROFILE_BY_AFID: AfHttpBcgetChaptersByTag[]
   */ 
  public int AfHttpBcgetProfileByAfid(int page_id, int page_start, int page_limit, String afid, Object user_data,AfHttpResultListener result){
	  int handle =AfBcgetProfileByAfid(page_id, page_start, page_limit, afid);
	  AfAddHttpListener(handle, result, null, user_data);
	  return handle;
  }
  /**
   *broadcast get chaptser like star.
   * @param user_data: 
   *		 equal with the user_data element of AfHttpBcgetProfileByAfid					
   * @param result: 
   *			result interface				
   * @return http handle	
   * @note:  code  flag: REQ_BCGET_LIKE_STAR_BY_HOUR or REQ_BCGET_LIKE_STAR_BY_DAY
   */ 
  public int AfHttpBcgetChaptersLikeStar(int flag, int page_id, int page_start, int page_limit, Object user_data,AfHttpResultListener result){
	  int handle =AfBcgetChaptersLikeStar(flag, page_id, page_start, page_limit);
	  AfAddHttpListener(handle, result, null, user_data);
	  return handle;
  }
/**
  *broadcast get people by the same city.
  * @param user_data: 
  * 		equal with the user_data element of AfHttpBcgetPeoplesChaptersByCity					
  * @param result: 
  * 		   result interface 			   
  * @return http handle    
  * @note:	code  flag: REQ_BCGET_PEOPLES_COMMENTS_BY_CITY result: AfHttpBcgetPeoplesChaptersByCity[]
  */ 
 public int AfHttpBcgetPeoplesChaptersByCity(byte sex, int page_id, int page_start, int page_limit, boolean online, Object user_data,AfHttpResultListener result){
	 int handle =AfBcgetPeoplesChaptersByCity(sex, page_id, page_start, page_limit,online);
	 AfAddHttpListener(handle, result, null, user_data);
	 return handle;  
 }
/**
  *broadcast get peoples by GPS .
  * @param user_data: 
  * 		equal with the user_data element of AfHttpBcgetPeoplesChaptersByGPS				   
  * @param result: 
  * 		   result interface 			   
  * @return http handle    
  * @note:	code  flag: REQ_BCGET_PEOPLES_COMMENTS_BY_GPS: AfHttpBcgetPeoplesChaptersByGPS[]
  */ 
 public int AfHttpBcgetPeoplesChaptersByGPS(byte sex, int page_id, int page_start, int page_limit,double lng, double lat, boolean online, boolean isFarfaraway, Object user_data,AfHttpResultListener result){
	 int handle =AfBcgetPeoplesChaptersByGPS(sex,page_id,page_start,page_limit,online,lng,lat,isFarfaraway);
	 AfAddHttpListener(handle, result, null, user_data);
	 return handle;  
 }

   /**
	 *broadcast title.
	 * @param user_data: 
	 *		   equal with the user_data element of AfBCMsgPublish 				  
	 * @param result: 
	 *			  result interface				  
	 * @return http handle	  
	 * @note:  code  flag: REQ_BCMSG_PUBLISH: AfHttpBCMsgPublish[]
	 */ 
	public int AfHttpBCMsgPublish(String title, String content, byte bctype, byte purview, String afid,int attach_cnt,double lng, double lat, String city, String state, String country, String tag, String cc, Object user_data,AfHttpResultListener result){
		int handle =AfBCMsgPublish(title, content, bctype,purview,afid,attach_cnt,lng,lat, city,state,country, tag,  cc, null, null, null);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;	
	}
	
	/**
	 *AfHttpBCMsgPublishEX, 发布广播接口，主要增加参数，BC52 Add
	 * @param language: 可选参数，客户端的语言，将会影响到tag系统的分类。可用值为：ar-SA, en-US, fr-FR, pt-PT,es-ES, sw-KE, amh, ha。如果没在这些值里面的话，自动设置为en-Us
	 * @param forward_mid:可选参数，当是转发的时候，请把转发的广播的mid带上
	 * @param layout:可选参数，图片字定义布局，base64编码
	 * @param user_data: 
	 *		   equal with the user_data element of AfBCMsgPublish 				  
	 * @param result: 
	 *			  result interface				  
	 * @return http handle	  
	 * @note:  code  flag: REQ_BCMSG_PUBLISH: AfHttpBCMsgPublish[]
	 */ 
	public int AfHttpBCMsgPublishEX(String title, String content, byte bctype, byte purview, String afid,int attach_cnt,double lng, double lat, String city, String state, String country, String tag, String cc,
			                        String language, String forward_mid, String layout,Object user_data,AfHttpResultListener result){
		int handle =AfBCMsgPublish(title, content, bctype,purview,afid,attach_cnt,lng,lat, city,state,country, tag,  cc, language, forward_mid, layout);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;	
	}
	
	 /**
	 *check mid.
	 * @param user_data: 
	 *		   equal with the user_data element of AfBCMsgCheckMid 				  
	 * @param result: 
	 *			  result interface				  
	 * @return http handle	  
	 * @note:  code  flag: REQ_BCMSG_CHECK_MID: AfPulishInfo
	 */ 
	public int AfHttpBCMsgCheckMId( String state, String country, Object user_data,AfHttpResultListener result){
		int handle =AfBCMsgCheckMid(state,country);
		AfAddHttpListener(handle, result, null, user_data);
		return handle;	
	}
   
   /**
	*Broadcast50 upload media file 
	* @param user_data: 
	*		  equal with the user_data element of AfBCMediaUpload				 
	* @param result: class String
	*         the sourceurl				 
	* @return http handle	 
	* @note:  code	flag: REQ_BCMEDIA_UPLOAD: AfHttpBCMediaUpload[]
	*/ 
   public int AfHttpBCMediaUpload(String media_path, int media_seq,byte media_type, String mstoken, int duration, Object user_data,AfHttpResultListener result){
	   int handle =AfBCMediaUpload(media_path, media_seq, media_type,  mstoken, duration, null, null,null);
	   AfAddHttpListener(handle, result, null, user_data);
	   return handle;  
   }
   
   /**
	*Broadcast52 upload media file, 上传图片新接口，增加参数，BC52 add 
	* @param resize: 可选参数，图片调整,w_h，例�?00_300
	* @param cut:可选参数，图片裁剪,x0_y1_x0_y1。裁剪的两个点的坐标,例如0_0_100_100
    * @param filter:可选参数，图片使用过滤镜效果, 带上这个参数, 服务器用作打点使用
	* @param user_data: 
	*		  equal with the user_data element of AfBCMediaUpload				 
	* @param result: class String
	*         the sourceurl				 
	* @return http handle	 
	* @note:  code	flag: REQ_BCMEDIA_UPLOAD: AfHttpBCMediaUpload[]
	*/ 
  public int AfHttpBCMediaUploadEx(String media_path, int media_seq,byte media_type, String mstoken, int duration, String resize, String cut, String filter,Object user_data,AfHttpResultListener result){
	   int handle =AfBCMediaUpload(media_path, media_seq, media_type,  mstoken, duration, resize, cut,filter);
	   AfAddHttpListener(handle, result, null, user_data);
	   return handle;  
  }
 
    /**
	*broadcast title.
	* @param user_data: 
	*		  equal with the user_data element of AfBCMsgOperate				 
	* @param result: 
	*			 result interface				 
	* @return http handle	 
	* @note:  code	flag: REQ_BCMSG_PUBLISH: AfHttpBCMsgOperate[]
	*/ 
   public int AfHttpBCMsgOperate(int flag, String afid, String to_afid, String mid,String content, String cid, Object user_data,AfHttpResultListener result){
	   int handle =AfBCMsgOperate(flag, afid, to_afid, mid, content, cid);
	   AfAddHttpListener(handle, result, null, user_data);
	   return handle;  
   }
   
   public int AfHttpGetGIS(String name,AfHttpResultListener result){
	   int handle =AfGetGIS(name);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;  
   }
   
   
   //***********************************************************************************/
   
  
   
   //**********************************5.2 bc tag start doc 18.27~28.34*********************************/
   /**
    * AfHttpAfBCGetTrends Trends获取首页内容,18.27
    * @param pageid: 
	*		  pageid	
	* @param user_data: 
	*		  equal with the user_data element of AfBCMsgOperate	
    * @return http handle: 
    * @note:  code	flag:REQ_BCGET_TRENDS
    * @note:  result result: AfResponseComm.AfTagGetTrendsResp
    */
   public int AfHttpAfBCGetTrends(int pageid, Object user_data, AfHttpResultListener result){
	   int handle = AfBCGetTrends(pageid);
	   AfAddHttpListener(handle, result, null, user_data); 
	   return handle;
   }
   
   /**
   * AfHttpAfBCGetTrendsMore,获得更多推荐Broadcast的相关数�?18.28
   * @param pageid:    
   * @param start:    
   * @param limit:    
   * @return http handle: 
   * @note:  code	flag:REQ_BCGET_TRENDS_MORE
   * @note:  result result: AfResponseComm.AfTagGetTrendsMoreResp
   */
  public int AfHttpAfBCGetTrendsMore(int pageid, int start, int limit,  AfHttpResultListener result){
	   int handle = AfBCGetTrendsMore(pageid, start, limit);
	   AfAddHttpListener(handle, result, null, 0); 
	   return handle;
  }
  
  /**
   * AfHttpAfBCGetHotTags, hottags,18.29
   * @param pageid:    
   * @param start:    
   * @param limit:    
   * @param tagname:    
   * @param flag:    
   * @return http handle: 
   * @note:  code	flag:REQ_BCGET_HOT_TAGS
   * @note:  result result: AfResponseComm.AfTagGetTagsResp
   */
  public int AfHttpAfBCGetHotTags(int pageid, int start, int limit,  AfHttpResultListener result){
	   int handle = AfBCGetTags(pageid, start, limit, null, Consts.REQ_BCGET_HOT_TAGS);
	   AfAddHttpListener(handle, result, null, 0); 
	   return handle;
  }
  
  /**
   * AfHttpAfBCSearchTags,搜索tag,18.30
   * @param pageid:    
   * @param start:    
   * @param limit:    
   * @param tagname:    
   * @param flag:    
   * @return http handle: 
   * @note:  code	flag:REQ_BCGET_SEARCH_TAGS
   * @note:  result result: AfResponseComm.AfTagGetTagsResp
   */
  public int AfHttpAfBCSearchTags(int pageid, int start, int limit, String tagname,  AfHttpResultListener result){
	   int handle = AfBCGetTags(pageid, start, limit, tagname, Consts.REQ_BCGET_SEARCH_TAGS);
	   AfAddHttpListener(handle, result, null, 0); 
	   return handle;
  }
  
  
  /**
   * AfHttpAfBCShareBroadCast,分享广播,18.31
   * @param toafid:  afid或GroupID  
   * @param sid:    
   * @param mid:    
   * @param tagname:    
   * @param picurl:    
   * @return http handle: 
   * @note:  code	flag:REQ_BC_SHARE_BROADCAST
   * @note:  result result: AfResponseComm.AfTagShareTagOrBCResp
   */
  public int AfHttpAfBCShareBroadCast(String toafid, long sid, String mid, Object obj, AfHttpResultListener result){
	   int handle = AfBCShareOpt(toafid, sid, mid, null, null, Consts.REQ_BC_SHARE_BROADCAST);
	   AfAddHttpListener(handle, result, null, obj);
	   return handle;
  }
  
  /**
   * AfHttpAfBCShareTags, 分享tag,18.32
   * @param toafid:  afid或GroupID  
   * @param sid:    
   * @param mid:    
   * @param tagname:    
   * @param picurl:    
   * @return http handle: 
   * @note:  code	flag:REQ_BC_SHARE_TAG
   * @note:  result result: AfResponseComm.AfTagShareTagOrBCResp
   */
  public int AfHttpAfBCShareTags(String toafid, long sid, String tagname, String picurl,Object obj, AfHttpResultListener result){
	   int handle = AfBCShareOpt(toafid, sid, null, tagname, picurl, Consts.REQ_BC_SHARE_TAG);
	   AfAddHttpListener(handle, result, null, obj);
	   return handle;
  }
  
  /**
   * AfHttpAfBcgetChaptersByNewTag，取得以Tag为主的Broadcast列表�?8.33    
   * @param page_id:    
   * @param page_start:    
   * @param page_limit:    
   * @param tag:    
   * @return http handle: 
   * @note:  code	flag:REQ_BCGET_COMMENTS_BY_TAGNAME
   * @note:  result result: AfResponseComm.AfPeoplesChaptersList
   */
  public int AfHttpAfBcgetChaptersByNewTag(int page_id, int page_start, int page_limit, String tag,  AfHttpResultListener result){
	   int handle = AfBcgetChaptersByNewTag(Consts.REQ_BCGET_COMMENTS_BY_TAGNAME, page_id, page_start, page_limit, tag);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
  }
  
  /**
   * AfHttpAfBcgetChaptersRecentHotByNewTag，取得以Tag获得24小时内点赞次数的广播�?8.34    
   * @param page_id:    
   * @param page_start:    
   * @param page_limit:    
   * @param tag:    
   * @return http handle: 
   * @note:  code	REQ_BCGET_RECENT_HOTS_BY_TAGNAME
   * @note:  result result: AfResponseComm.AfPeoplesChaptersList
   */
  public int AfHttpAfBcgetChaptersRecentHotByNewTag(int page_id, int page_start, int page_limit, String tag,  AfHttpResultListener result){
	   int handle = AfBcgetChaptersByNewTag(Consts.REQ_BCGET_RECENT_HOTS_BY_TAGNAME, page_id, page_start, page_limit, tag);
	   AfAddHttpListener(handle, result, null, 0); 
	   return handle;
  }
  
	  /**
	  * AfHttpAfBCGetDefaultTags,默认tag列表数据,18.35
	  * @return http handle: 
	  * @note:  code	flag: REQ_BCGET_DEFAULT_TAGS
	  * @note:  result result: AfResponseComm.AfTagGetDefaultTagsResp
	  */
	 public int AfHttpAfBCGetDefaultTags( AfHttpResultListener result){
		   int handle = AfBCGetDefaultTags();
		   AfAddHttpListener(handle, result, null, 0); 
		   return handle;
	 }
	 
	 /**
	 * AfHttpAfBCLangPackage, 下载TAG语言包，18.36
	 * @param langver:   语言包版本号 
	 * @return http handle: 
	 * @note:  code	flag:REQ_BCGET_LANGUAGE_PACKAGE
	 * @note:  result result: AfResponseComm.AfTagGetLangPackageResp
	 */
	public int AfHttpAfBCLangPackage(String langver,  AfHttpResultListener result){
		   int handle = AfBCLangPackage(langver);
		   AfAddHttpListener(handle, result, null, null); 
		   return handle;
	}
	
	/**
    * AfHttpAfBcgetRegionBroadcast,获得指定区域Broadcast数目,18.37
    * @return http handle: 
    * @note:  code	flag:REQ_BCGET_REGION_BROADCAST
    * @note:  result result: AfResponseComm.AfBCGetRegionBroadcastResp
    */
   public int AfHttpAfBcgetRegionBroadcast( AfHttpResultListener result){
	   int handle = AfBcgetRegionBroadcast();
	   AfAddHttpListener(handle, result, null, null); 
	   return handle;
   }
    /**
    * AfHttpAfBcgetByStateOrCity,取得相应地区的Broadcast列表,18.38
    * @param pageid:    
    * @param start:    
    * @param limit:    
    * @param country:    
    * @param state:    
    * @param city:    
    * @return http handle: 
    * @note:  code	flag:REQ_BCGET_COMMENTS_BY_STATE_OR_CITY
    * @note:  result result: AfResponseComm.AfPeoplesChaptersList
    */
   public int AfHttpAfBcgetByStateOrCity(int pageid, int start, int limit, String country, String state, String city, Object user_data, AfHttpResultListener result){
	   int handle = AfBcgetByStateOrCity(pageid, start, limit, country, state, city);
	   AfAddHttpListener(handle, result, null, user_data); 
	   return handle;
   }
   
   /**
   * AfHttpCheckIlleagalWord,注册检测字符是否非�?   * 
   * @param wordarray:    
   * @return http handle: 
   * @note:  code	flag:REQ_CHECK_ILLEAGAL_WORDS
   */
  public int AfHttpCheckIlleagalWord(String[] wordarray,  AfHttpResultListener result){
	   int handle = HttpCheckIlleagalWord(wordarray);
	   AfAddHttpListener(handle, result, null, null); 
	   return handle;
  }
   
 
   
   //**********************************5.2 bc tag  end doc 18.27~28.34 *********************************/
  
  //**********************************5.3 palmcall start doc 24.1~24.4  *********************************/
  	/**
	  * AfHttpPalmCallGetHotList, 获取可拨打palmcall列表
     *  @param gift: 值为true时，表示用户第一次使用PalmCall功能，可获赠通话分钟数
	  * @return http handle: 
	  * @note:  code flag:REQ_PALM_CALL_HOT_LIST
	  * @note:  result result:  (ArrayList<AfPalmCallHotListItem>) AfPalmCallResp.respobj & leftTime
	  */
	 public int AfHttpPalmCallGetHotList(boolean gift, Object user_data, AfHttpResultListener result){
		   int handle = HttpPalmCallGetHotList(gift);
		   AfAddHttpListener(handle, result, null, user_data); 
		   return handle;
	 }
  
	 /**
	 * AfHttpPalmCallMakeCall, 检查好友是否可拨打并返回justalkId
	 * @return http handle: 
	 * @note:  code flag:REQ_PALM_CALL_MAKECALL
	 * @note:  result result:  (AfPalmCallMakeCallResp) AfPalmCallResp.respobj
	 */
	public int AfHttpPalmCallMakeCall(String afid,Object user_data, AfHttpResultListener result){
		   int handle = HttpPalmCallMakeCall(afid);
		   AfAddHttpListener(handle, result, null, user_data); 
		   return handle;
	}
	
	/**
	 * AfHttpPalmCallUploadIntroMedia, 上传PalmCall简介
	 * @param content: 文字描述
	 * @param filepath: 语音文件路径
	 * @param format: 文件格式，目前只支持MEDIA_TYPE_AMR（语音），MEDIA_TYPE_JPG（头像）
	 * @param duration:语音时长，以s为单位
	 * @return http handle: 
	 * @note:  code flag:REQ_PALM_CALL_UPLOAD_INTRO_MEDIA
	 * @note:  result result:  String(现修改为返回ts，由上层拼url，By Yunxiang 20160817)
	 */
	public int AfHttpPalmCallUploadIntroMedia(String content, String filepath, int format, int duration,Object user_data, AfHttpResultListener result){
		   int handle = HttpPalmCallUploadIntroMedia(content, filepath,format, duration);
		   AfAddHttpListener(handle, result, null, user_data); 
		   return handle;
	}

    /**
     * AfHttPalmCallGetLeftTime, 获取可拨打时间
     * @return http handle:
     * @note:  code flag:REQ_PALM_CALL_GET_LEFTTIME
     * @note:  result result:   AfPalmCallResp.leftTime
     */
    public int AfHttPalmCallGetLeftTime(Object user_data, AfHttpResultListener result){
        int handle = HttpPalmCallGetLeftTime();
        AfAddHttpListener(handle, result, null, user_data);
        return handle;
    }

    /**
     * AfHttpPalmCallSetAlonePeriod，设置免打扰时间
     * @param open: 1,open;0,closed;
     * @param startTime:免打扰开始时间（小时，当startTime  > endTime时，表示跨天）
     * @param endTime:免打扰结束时间（小时）
     * @return http handle:
     * @note:  code	flag: REQ_PALM_CALL_SET_ALONE_PERIOD
     * @note:  result result:   null
     */
    public int AfHttpPalmCallSetAlonePeriod(int open, int startTime, int endTime, Object user_data, AfHttpResultListener result){
        int handle = HttpPalmCallSetAlonePeriod(open, startTime, endTime);
        AfAddHttpListener(handle, result, null, user_data);
        return handle;
    }
    /**
     * AfHttpPalmCallGetAlonePeriod，获取设置的免打扰时间
     * @return http handle:
     * @note:  code	flag:REQ_PALM_CALL_GET_ALONE_PERIOD
     * @note:  result result:   (AfPalmCallResp.AfPalmCallGetAlonePeriodResp) AfPalmCallResp.respobj
     */
    public int AfHttpPalmCallGetAlonePeriod(Object user_data, AfHttpResultListener result){
        int handle = HttpPalmCallGetAlonePeriod();
        AfAddHttpListener(handle, result, null, user_data);
        return handle;
    }
    /**
     * AfHttpPalmCallGetInfo,获取palmcall用户信息
     * @param afid:afid
     * @return http handle:
     * @note:  code	flag: REQ_PALM_CALL_GET_INFO
     * @note:  result result:  (ArrayList<AfPalmCallHotListItem>) AfPalmCallResp.respobj
     */
    public int AfHttpPalmCallGetInfo(String afid,Object user_data, AfHttpResultListener result){
        int handle = HttpPalmCallGetInfo(afid);
        AfAddHttpListener(handle, result, null, user_data);
        return handle;
    }

    /**
     * AfHttpPalmCallGetInfoBatcn,批量获取palmcall用户信息
     * @param afids:afids
     * @return http handle:
     * @note:  code	flag: REQ_PALM_CALL_GET_INFO_BATCH
     * @note:  result result:  (ArrayList<AfPalmCallHotListItem>) AfPalmCallResp.respobj
     */
    public int AfHttpPalmCallGetInfoBatch(String[] afids,Object user_data, AfHttpResultListener result){
        int handle = HttpPalmCallGetInfoBatch(afids);
        AfAddHttpListener(handle, result, null, user_data);
        return handle;
    }
	
  //********************************** 5.3 palmcall end doc 24.1~24.4 *********************************/
   
   
   //**********************************5.0 public group*********************************/
   
   /*** doc:  19.1 ~ 19.8
        reference to:     AfHttpGrpOpr()
     
    ************************************
   /**
    * 19.9 Show the tags List
	* @param 			 
	* @param result: 
	*			 result interface				 
	* @return http handle	 
	* @note:  code	flag: REQ_P_GRP_TAG_LIST    AfPublicGroup:  AfPublicGroup.Category[]
	*/ 
   public int AfHttpPublicGrpTagsList(AfHttpResultListener result){
	   int handle = AfGetPublicGrpTagsList();
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * 19.10 Get Public Group Member  list
	* @param gid: group id
	* @param result: 
	*			 result interface				 
	* @return http handle	 
	* @note:  code	flag: REQ_P_GRP_MEMBER_LIST    AfGrpProfileInfo:  members
	*/ 
   public int AfHttpGetPublicGroupMember(String gid, AfHttpResultListener result){
	   int handle = AfGetGrpMember(gid);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * 19.11 Get Public Group List by gps
	* @param page_id: 
	* @param result: 
	*			 result interface				 
	* @return http handle	 
	* @note:  code	flag: REQ_P_GRP_LIST_BY_GPS    AfPublicGroup:  total  grpProfileList
	*/ 
   public int AfHttpGetPublicGrpList_ByGps(int page_id,int page_start,int page_limit, double lng, double lat, AfHttpResultListener result){
	   int handle = AfGetPublicGrpList(Consts.REQ_P_GRP_LIST_BY_GPS, page_id, page_start, page_limit, lng, lat, "", "");
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * 19.12 Get Public Group List by tag
	* @param page_id:    category:      tag:
	* @param result: 
	*			 result interface				 
	* @return http handle	 
	* @note:  code	flag: REQ_P_GRP_LIST_BY_TAG    AfPublicGroup:  total  grpProfileList
	*/ 
   public int AfHttpGetPublicGrpList_ByTag(int page_id,int page_start,int page_limit, String category, String tag, AfHttpResultListener result){
	   int handle = AfGetPublicGrpList(Consts.REQ_P_GRP_LIST_BY_TAG, page_id, page_start, page_limit, 0, 0, category, tag);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * 19.13 Get Public Group Hot List
	* @param page_limit: 
	* @param result: 
	*			 result interface				 
	* @return http handle	 
	* @note:  code	flag: REQ_P_GRP_LIST_BY_HOT    AfPublicGroup:  countryList  cityList  countryTotal  cityTotal  grpProfileList
	*/ 
   public int AfHttpGetPublicGrpList_Hot(int page_limit, AfHttpResultListener result){
	   int handle = AfGetPublicGrpHotList(page_limit);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * 19.14 Get Public Group List by country
	* @param page_id: 
	* @param result: 
	*			 result interface				 
	* @return http handle	 
	* @note:  code	flag: REQ_P_GRP_LIST_BY_COUNTRY    AfPublicGroup:  total  grpProfileList
	*/ 
   public int AfHttpGetPublicGrpList_ByCountry(int page_id,int page_start,int page_limit, AfHttpResultListener result){
	   int handle = AfGetPublicGrpList(Consts.REQ_P_GRP_LIST_BY_COUNTRY, page_id, page_start, page_limit, 0, 0, "", "");
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * 19.15 Get Public Group List by city
	* @param page_id: 
	* @param result: 
	*			 result interface				 
	* @return http handle	 
	* @note:  code	flag: REQ_P_GRP_LIST_BY_CITY    AfPublicGroup:  total  grpProfileList
	*/ 
   public int AfHttpGetPublicGrpList_ByCity(int page_id,int page_start,int page_limit, AfHttpResultListener result){
	   int handle = AfGetPublicGrpList(Consts.REQ_P_GRP_LIST_BY_CITY, page_id, page_start, page_limit, 0, 0, "", "");
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * 19.16 SearchPublic Group by Keyword & Country
	* @param page_id: 
	* @param result: 
	*			 result interface				 
	* @return http handle	 
	* @note:  code	flag: REQ_P_GRP_LIST_BY_KEYWORD    AfPublicGroup:  grpProfileList
	*/ 
   public int AfHttpGetPublicGrpByKeyword(String keyWord, AfHttpResultListener result){
	   int handle = AfGetPublicGrpByKeyword(keyWord);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * 19.17 SearchPublic Group by public group id
	* @param page_id: 
	* @param result: 
	*			 result interface				 
	* @return http handle	 
	* @note:  code	flag: REQ_P_GRP_LIST_BY_GID    AfPublicGroup:  grpProfileList
	*/ 
   public int AfHttpGetPublicGrpByGid(String gid, AfHttpResultListener result){
	   int handle = AfGetPublicGrpByGid(gid);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   
   
   
   /*Lottery Predict*/
   /**
    * init data
	* @param page_id: 
	* @param result:  class	  AfLottery:  [lottery_list  flag  win]
	* @return http handle	 
	* @note:  code	flag: REQ_PREDICT_INIT
	*/ 
   public int AfHttpLotteryInit(AfHttpResultListener result){
	   int handle = AfLotteryInit();
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
	* @param afid: 
	* @param result:  code value
	* @return http handle	 
	* @note:  code	flag: REQ_PREDICT_TURNLOTTERY
	*/ 
   public int AfHttpTurnLottery(String afid, AfHttpResultListener result){
	   int handle = AfTurnLottery(afid);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get predict action list 
    * @param flag: REQ_PREDICT_GETACTIONLIST
	* @param type: 0.all data  1.just odds data
	* @param afid: user account
	* @param start: page begin index
	* @param limit: counts
	* @param result:     class	 AfLottery:  [action_list  odds_list]
	* @return http handle	 
	* @note:  code	flag: REQ_PREDICT_GETACTIONLIST     REQ_PREDICT_GETENDACTIONLIST
	*/ 
   public int AfHttpGetPredictActionList(int flag, int type, String afid, int start, int limit, AfHttpResultListener result){
	   int handle = AfGetPredictActionList(flag, type, afid, start, limit);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Prepare bet
	* @param afid: user account
	* @param actionId: actionId
	* @param itemId: itemId
	* @param points: points
	* @param result:     class	 AfLottery:  [ betInfo ]
	* @return http handle	 
	* @note:  code	flag: RRE_PREDICT_PREPBET  
	*/ 
   public int AfHttpPredictPrepareBet(String afid, String actionId, String itemId, long points, AfHttpResultListener result){
	   int handle = AfPredictPrepareBet(afid, actionId, itemId, points);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * confirm your bet
	* @param afid: user account
	* @param actionId: actionId
	* @param result:     class	 AfLottery:  [ betInfo ]
	* @return http handle	 
	* @note:  code	flag: PRE_PREDICT_SUREBET  
	*/ 
   public int AfHttpPredictSureBet(String afid, String actionId, String itemId, AfHttpResultListener result){
	   int handle = AfPredictSureBet(afid, actionId, itemId);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get your current points data
	* @param afid: user account
	* @param result:     class	 AfLottery:  [ betInfo ]
	* @return http handle	 
	* @note:  code	flag: PRE_PREDICT_GETPOINTS  
	*/ 
   public int AfHttpPredictGetPoints(String afid, AfHttpResultListener result){
	   int handle = AfPredictGetPoints(afid);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get your current coins data
	* @param afid: user account
	* @param result:     class	 Integer:   AfLottery [getconiresp]
	* @return http handle	 
	* @note:  code	flag: REQ_PREDICT_BUY_POINTS_GETCOINS  
	*/ 
   public int AfHttpPredictGetCoins(String afid, AfHttpResultListener result){
	   int handle = AfPredictGetCoins(afid);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get your bet history data
	* @param afid: user account
	* @param start: page begin index
	* @param limit: counts
	* @param result:     class	 AfLottery:  [ betHistory]
	* @return http handle	 
	* @note:  code	flag: PRE_PREDICT_BETHISTORY  
	*/ 
   public int AfHttpPredictBetHistory(String afid, int start, int limit, AfHttpResultListener result){
	   int handle = AfPredictBetHistory(afid, start, limit);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get the ranking data
	* @param afid: user account
	* @param start: page begin index
	* @param limit: counts
	* @param type: week month year
	* @param result:     class	 AfLottery:  [ ranking ]
	* @return http handle	 
	* @note:  code	flag: PRE_PREDICT_GETRANKING  
	*/ 
   public int AfHttpPredictRanking(String afid, int start, int limit, String type, AfHttpResultListener result){
	   int handle = AfPredictRanking(afid, start, limit, type);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * send the Feedback
	* @param afid: user account
	* @param contents: contents
	* @param result:     
	* @return http handle	 
	* @note:  code	flag: PRE_PREDICT_FEEDBACK  
	*/ 
   public int AfHttpPredictFeedback(String afid, String contents, AfHttpResultListener result){
	   int handle = AfPredictFeedback(afid, contents);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get the points detail
	* @param afid: user account
	* @param start: page begin index
	* @param limit: counts
	* @param result:     class	 AfLottery:  [ pointsDetailList ]
	* @return http handle	 
	* @note:  code	flag: PRE_PREDICT_GET_POINTSDETAILLIST  
	*/ 
   public int AfHttpGetPointsDetail(String afid, int start, int limit, AfHttpResultListener result){
	   int handle = AfGetPointsDetail(afid, start, limit);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get the announcement
	* @param afid: user account
	* @param start: page begin index
	* @param limit: counts
	* @param result:     class	 AfLottery:  [ announcementList ]
	* @return http handle	 
	* @note:  code	flag: PRE_PREDICT_GET_ANNOUNCEMENT
	*/ 
   public int AfHttpGetAnnouncement(String afid, int start, int limit, AfHttpResultListener result){
	   int handle = AfPredictAnnouncement(afid, start, limit);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get the predict gif and ads
	* @param afid: user account
	* @param start: page begin index
	* @param limit: counts
	* @param result:     class	 AfPrizeInfo:  [ giftAdsList ]
	* @return http handle	 
	* @note:  code	flag: PRE_PREDICT_GET_GIFTSADSLIST
	*/ 
   public int AfHttpPredictGiftAds(String afid, int start, int limit, AfHttpResultListener result){
	   int handle = AfPredictGiftAds(afid, start, limit);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get the predict gif list
	* @param afid: user account
	* @param start: page begin index
	* @param limit: counts
	* @param type:  礼物类型:1,周榜礼物; 2,�?3,�?
	* @param result:     class	 AfPrizeInfo:  [ giftList ]
	* @return http handle	 
	* @note:  code	flag: REQ_PREDICT_GET_GIFTSLIST
	*/ 
   public int AfHttpPredictGiftList(String afid, int start, int limit, int type, AfHttpResultListener result){
	   int handle = AfPredictGiftList(afid, start, limit, type);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get the address record
	* @param afid: user account
	* @param start: page begin index
	* @param limit: counts
	* @param result:     class	 AfPrizeInfo:  [ addrRecord ]
	* @return http handle	 
	* @note:  code	flag: REQ_PREDICT_GET_ADDRESSLIST
	*/ 
   public int AfHttpPredictGetAddressRecord(String afid, int start, int limit, AfHttpResultListener result){
	   int handle = AfPredictGetAddressRecord(afid, start, limit);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Set the address of user
	* @param afid: user account
	* @param giftId: gift id
	* @param address info: country,province,city,consignee,phone,firstName,lastName,address,landmark
	* @param result:     code value
	* @return http handle	 
	* @note:  code	flag: REQ_PREDICT_ADD_ADDRESS
	*/ 
   public int AfHttpPredictSetAddress(String afid,int giftId,String country,String province,String city,String consignee,String phone,String firstName,String lastName,String address,String landmark, AfHttpResultListener result){
	   int handle = AfPredictSetAddress(afid,giftId,country,province,city,consignee,phone,firstName,lastName,address,landmark);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get gifts exchange list
	* @param afid: user account
	* @param start: page begin index
	* @param limit: counts
	* @param type: 类型�?.为获取全部，1.虚拟礼品官网兑换�?.实物礼品
	* @param result:     class	 AfPrizeInfo:  [ giftsExchangeList ]
	* @return http handle	 
	* @note:  code	flag: REQ_PREDICT_GET_GIFTSEXCHANGELIST
	*/ 
   public int AfHttpPredictGiftsExchangeList(String afid, int start, int limit, int type, AfHttpResultListener result){
	   int handle = AfPredictGiftsExchangeList(afid, start, limit, type);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   
   /**
    * AfHttpPredictGetInviteFriendlist,博彩获取邀请好友列�?    * @param afid: user account
	* @param start: page begin index
	* @param limit: counts
    * @param inviteFriendsVersion:    
    * @param result:    class	 AfLottery:  [ rankingList, inviteFriendsVersion,total ]
    * @return http handle: 
    * @note:  code	flag: Consts.REQ_PREDICT_INVITEFRIENDS
    */
   public int AfHttpPredictGetInviteFriendlist(String afid, int start, int limit, long inviteFriendsVersion,  AfHttpResultListener result){
	   int handle = AfPredictGetFriendlist(afid, Consts.REQ_PREDICT_INVITEFRIENDS, start, limit, inviteFriendsVersion);
	   AfAddHttpListener(handle, result, null, 0); 
	   return handle;
   }
   
   /**
    * AfHttpPredictGetRecommendFriendlist，博彩获取推荐好友列�?    * @param afid: user account        
    * @param result:     class	 AfLottery:  [ rankingList ]
    * @return http handle: 
    * @note:  code	flag: Consts.REQ_PREDICT_RECOMMENDFRIENDS
    */
   public int AfHttpPredictGetRecommendFriendlist(String afid,  AfHttpResultListener result){
	   int handle = AfPredictGetFriendlist(afid, Consts.REQ_PREDICT_RECOMMENDFRIENDS, 0, 0, 0);
	   AfAddHttpListener(handle, result, null, 0); 
	   return handle;
   }
   
   /**
    * Get friend mid list
	* @param pageId: page Id
	* @param start: page begin index
	* @param limit: counts
	* @param result:     class	 AfResponseComm:  [ frdConnList ]
	* @return http handle	 
	* @note:  code	flag: REQ_CONNECTIONS_GET_INDEX_LIST
	*/ 
   public int AfHttpGetFrdConnMidList(int pageId, int start, int limit, AfHttpResultListener result){
	   int handle = AfGetFrdConnMidList(pageId, start, limit);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get friend connection list
	* @param mid_js:  ["mid1","mid2"]
	* @param result:     class	 AfResponseComm:  [ list_chapters ]
	* @return http handle	 
	* @note:  code	flag: REQ_CONNECTIONS_GET_LIST_BY_MIDS
	*/ 
   public int AfHttpGetFrdConnList(String mid_js, AfHttpResultListener result){
	   int handle = AfGetFrdConnList(mid_js);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get friend connection page list
	* @param pageId: page Id
	* @param start: page begin index
	* @param limit: counts
	* @param result:     class	 AfResponseComm:  [ list_chapters ]
	* @return http handle	 
	* @note:  code	flag: REQ_CONNECTIONS_GET_LIST_BY_PAGE
	*/ 
   public int AfHttpGetFrdConnListPage(int pageId, int start, int limit, Object userData,AfHttpResultListener result){
	   int handle = AfGetFrdConnListPage(pageId, start, limit);
	   AfAddHttpListener(handle, result, null, userData);
	   return handle;
   }
   /**
    * Set payment recharge
	* @param flag:  REQ_PREDICT_BUY_POINTS_BYPREPAID, REQ_PREDICT_BUY_POINTS_BYCOINS, REQ_PREDICT_BUY_POINTS_BYSMS
	* @param afid:  user account 
	* @param card_No: 卡号,only REQ_PREDICT_BUY_POINTS_BYPREPAID
	* @param card_pwd: 密码,only REQ_PREDICT_BUY_POINTS_BYPREPAID
	* @param coins:    所要换购points的coin值，only REQ_PREDICT_BUY_POINTS_BYCOINS
	* @param sn:       短信流水号，only REQ_PREDICT_BUY_POINTS_BYSMS
	* @param front_url: paylist返回的front_url
	* @param channel:   接入应用的渠道， a博彩模块固定�?PREDICT_WIN"
	* @param result:     class	 AfPrizeInfo:  [ paymentInfo ]
	* @return http handle	 
	* @note:  code	flag: REQ_PREDICT_BUY_POINTS_BYPREPAID, REQ_PREDICT_BUY_POINTS_BYCOINS, REQ_PREDICT_BUY_POINTS_BYSMS
	*/ 
   public int AfHttpPaymentRecharge(int flag,String afid,String card_No,String card_pwd,int coins,String sn,String front_url,String channel, AfHttpResultListener result){
	   int handle = AfPaymentRecharge(flag,afid,card_No,card_pwd,coins,sn,front_url,channel);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get payment sms sn
	* @param afid:        user account 
	* @param front_url:  sms充值接口用的API,login后返回paylist所�?	* @param result:     class	 AfPrizeInfo:  [ smsInfo ]
	* @return http handle	 
	* @note:  code	flag: REQ_PREDICT_BUY_POINTS_BYSMS_GETSN
	*/ 
   public int AfHttpPaymentGetSMS(String afid,String front_url, AfHttpResultListener result){
	   int handle = AfPaymentGetSMS(afid,front_url);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   /**
    * Get paga order id
	* @param afid:        // 必须  user account
	* @param front_url:   // 必须     Paga充值获取订单用的API,返回的front_url
	* @param orderMoney:  // 必须    下单金额
	* @param giftMoney:   // 可�?待用户充值成功后，额外给用户的赠送金额，单位为对应币种的�? 默认值为0
	* @param currency:    // 可�?操作的币�? 充值包括国际货币的币种，如奈拉为NGN、CNY等。全球货币符号，参看：http://huobi.00cha.net/hbfh.asp
	* @param moneyType:   // 必须    充值到账的方式, COIN：用户充的钱存在钱包系统中，供用户以后消�? POINT：用户充的钱会立即使用，此时一笔充值会在订单系统以及钱包系统中同时会产生两条记录，包括充值及消息记录
	* @param remark:      // 必须	说明该订单购买的什么东西等
	* @param payGateway:  // 必须	用于支付该笔订单的支付网关PAGA：指PAGA......其值和epay_gateway_t表中的CODE字段相对�?	* @param returnUrl:   // 可�?在第三方充值平台充值成功后，第三方充值平台回调的URL。如果没有传，则使用EPay中默认充值成功的URL；如果用户有传该值，则使用用户给的RETURN_URL
	* @param result:     class	 String:   order_id 
	* @return http handle	 
	* @note:  code	flag: REQ_PREDICT_BUY_POINTS_BYPAGA_GETORDERID
	*/ 
   public int AfHttpPaymentGetPagaOrderId(String afid,String front_url,String orderMoney,String giftMoney,String currency,String moneyType,String remark,String payGateway,String returnUrl, AfHttpResultListener result){
	   int handle = AfPaymentGetPagaOrderId(afid,front_url,orderMoney,giftMoney,currency,moneyType,remark,payGateway,returnUrl);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   
   /*5.1.3 New Payment*/
   /**
    * Get coins2goods list 获取cointogoods消费coin方式列表
	* @param coin2goods_type:   such as AfNewPayment.NewPayment_Coin2Goods_Type_AirTime
	* @param result:     class	 AfNewPayment.AFCoin2GoodsResp
	* @return http handle	 
	* @note:  code	flag: REQ_PALM_COIN_GET_GOODS_LIST
	*/ 
   public int AfHttpNewPaymentCoins2GoodsList(int coin2goods_type, AfHttpResultListener result){
	   int handle = AfNewPaymentCoins2GoodsList(coin2goods_type);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   
   /**
    * Get BillHistory list 获取billhistory账单列表(消费或充值，目前仅支持消�?
	* @param transtype:   now only AfNewPayment.NewPayment_Bill_History_Type_Consume
	* @param pageid: pageid
	* @param start: page begin index
	* @param limit: counts
	* @param result:     class	 AfNewPayment.AFBillHistoryResp
	* @return http handle	 
	* @note:  code	flag: REQ_PALM_COIN_GET_BILL_HISTORY
	*/ 
   public int AfHttpNewPaymentBillHistoryList(int transtype,int pageid, int start,int limit, AfHttpResultListener result){
	   int handle = AfNewPaymentBillHistoryList(transtype,pageid,start,limit);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   
   /**
    * Get Money2Coins list 获取Money2Coin充值方式列�?	* @param result:     class	 AfNewPayment.AFMoney2CoinsResp
	* @return http handle	 
	* @note:  code	flag: REQ_PALM_COIN_GET_MONEY2COINS_LIST
	*/ 
   public int AfHttpNewPaymentMoney2CoinsList(AfHttpResultListener result){
	   int handle = AfNewPaymentMoney2CoinsList();
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   
   /**
    * AirtimeTopup airtime充话费，消费coin
	* @param result:     class Integer,user balance
	* @return http handle	 
	* @note:  code	flag: REQ_PALM_COIN_GET_AIRTIME_TOPUP
	*/ 
   public int AfHttpNewPaymentAirtimeTopup(AFAirtimeTopupReq req,AfHttpResultListener result){
	   int handle = AfNewPaymentAirtimeTopup(req);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   
   /**
    * AfHttpNewPaymentGetRCOrderId 充值获取订单号
	* @param result:     class String, orderid
	* @return http handle	 
	* @note:  code	flag: REQ_PALM_COIN_CREATE_RECHARGE
	*/ 
   public int AfHttpNewPaymentGetRCOrderId(AFRechargeOrderReq req,AfHttpResultListener result){
	   int handle = AfNewPaymentGetRCOrderId(req);
	   AfAddHttpListener(handle, result, null, 0);
	   return handle;
   }
   
   /**
    * AfCoreAfNewPaymentGetRechargeOrderLogs 获取订单记录
    * @param result:     class AFGetCoinOrderHistoryResp
    * @param index:  page index  
    * @param size:   page size
    * @return http handle: 
    * @note:  code	flag:REQ_PAYMENT_GET_RECHARGE_ORDERS_LOGS
    */
   public int AfCoreAfNewPaymentGetRechargeOrderLogs(int index, int size,  AfHttpResultListener result){
	   int handle = AfNewPaymentGetRechargeOrderLogs(index, size);
	   AfAddHttpListener(handle, result, null, 0); 
	   return handle;
   }
   /*5.1.3 New Payment*/

    /**
     * AfHttpPaymentAnalysis 支付统计上报
     * @param data: 按协议4.46格式拼装的json数据
     * @note:  code	flag:REQ_FLAG_PAYMENT_ANALYSIS
     * @note:  result result: null
     */
    public int AfHttpPaymentAnalysis(String data,Object user_data, AfHttpResultListener result){
        int handle = HttpPaymentAnalysis(data);
        AfAddHttpListener(handle, result, null, user_data);
        return handle;
    }
   

}// AfPalmchat
