package com.core;

import android.content.Context;

import com.afmobi.palmchat.BaseActivity;
import com.afmobigroup.gphone.R;
//import com.afmobi.palmchat.listener.ReqCodeListener;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.ToastManager;

public class Consts {

	public static Consts errorCodeInstance;
	private Consts(){
		
	}
	
//	wake lock
	public static final boolean mAcquireWakeLock = false;
	
	public static Consts getInstance(){
		if(errorCodeInstance == null){
			errorCodeInstance = new Consts();
		}
		return errorCodeInstance;
	}
 	/***********************************************************************************************************************/
 	// marital status value
 	/***********************************************************************************************************************/
	public static final byte AFMOBI_MARITAL_STATUS_UNKNOWN = 0;  		/*unknown status*/
	public static final byte AFMOBI_MARITAL_STATUS_MARRIED =1;			/*married status*/
	public static final byte AFMOBI_MARITAL_STATUS_UNMARRIED = 2;		/*unmarried status*/
	public static final byte AFMOBI_MARITAL_STATUS_LOVING = 3;			/*in love status*/
	public static final byte AFMOBI_MARITAL_STATUS_WIDOWED =4;			/*widowed status*/
	public static final byte AFMOBI_MARITAL_STATUS_DIVORCED = 5;		/*divorced status*/
	public static final byte AFMOBI_MARITAL_STATUS_NOT_PUBLIC = 6;		/*no public */
	public static final byte AFMOBI_MARITAL_STATUS_TROUBLED = 7;		/*troubled status*/
	public static final byte AFMOBI_MARITAL_STATUS_GAY = 8;				/*girl or boy gay*/
	public static final byte AFMOBI_MARITAL_STATUS_ENGAGED = 9;			/*engaged status*/
	
	public static final byte GIVE_GITFS_TYPE_PHONE = 0;  		// the first modify phone
	public static final byte GIVE_GITFS_TYPE_PHOTO =1;			//the frist modify head image
	public static final byte GIVE_GITFS_TYPE_SIGN = 2;			//sign  per day
	 
	//server support head
	public static final int AF_AVATAR_MAX_INDEX = 8;
	public static final int AF_HEAD_UPLOAD_INDEX = 0;
	public static final String AF_AVATAR_SUFFIX = ".JPG";
	public static final String AF_IMAGE_SUFFIX = ".JPG";
	public static final String AF_AVATAR_FORMAT_JPG = ".JPG";
	public static final String AF_AVATAR_FORMAT_PNG = ".PNG";
	public static final String AF_AVATAR_FORMAT = "JPEG";
	public static final String AF_HEAD_SMALL = "40x40";
	public static final String AF_HEAD_MIDDLE = "160x160";
	public static final String AF_HEAD_LARGE = "320x320";
	public static final String AF_HEAD_MAX_LARGE = "MAX";
	
	/*the rquest bind email type*/
	public final static String HTTP_PARAMS_TYPE_TRUE = "T";		
	public final static String HTTP_PARAMS_TYPE_FALSE = "F";		
	
 	/***********************************************************************************************************************/
 	// get random type 
 	/***********************************************************************************************************************/
	public final static int  HTTP_RANDOM_REGISTER = 0X1;
	public final static int  HTTP_RANDOM_FIND_PASSWORD = 0X1;
	public final static int  HTTP_RANDOM_BIND_PHONE = 0X1;

	/***********************************************************************************************************************/
 	// polling status  
 	/***********************************************************************************************************************/
	public final static int  POLLING_STATUS_PAUSE = 0X1; 		
	public final static int  POLLING_STATUS_RUNNING = 0X2; 

 	/***********************************************************************************************************************/
 	// chatroom type 
 	/***********************************************************************************************************************/
	public final static byte CHATROOM_OPR_TYPE_SERVER = 0;  			//set ip and port
	public final static byte CHATROOM_OPR_TYPE_SESSION = 1; 			//set ctoken and cptoken
	public final static byte CHATROOM_OPR_TYPE_CLEAR_SERVER = 2; 		//clear ip and port 
	public final static byte CHATROOM_OPR_TYPE_CLEAR_SSESSION = 3; 		//clear ctoken and cptoken
	public final static byte CHATROOM_OPR_TYPE_CLEAR = 4; 				//clear ip port ctoken cptoken
	public final static byte CHATROOM_OPR_TYPE_ADMIN = 5; 				//set current chatroom admin

	
	/**************************************************************************************************/
	//http handle
	/**************************************************************************************************/		
	public final static int AF_HTTP_HANDLE_INVALID = 0;
	
	
	/**************************************************************************************************/
	//database operation error
	/**************************************************************************************************/
	public final static int  AFMOBI_DATABASE_RESULT_ERROR = -1;
	public final static int  AFMOBI_DATABASE_RESULT_OK = 0;

	/**************************************************************************************************/
	//defined public account type
	/**************************************************************************************************/	
	public final static byte PA_TYPE_PARTNERS = 0;    			/*Partners */	
	public final static byte PA_TYPE_SHOPPING = 1;    			/*Shopping*/
	public final static byte PA_TYPE_TRAVEL = 2;    			/*Travel*/
	public final static byte PA_TYPE_ENTERTAINMENT = 3;    		/*Entertainment*/	
	public final static byte PA_TYPE_PUBLIC = 4;    			/*Public*/
	public final static byte PA_TYPE_BANKING = 5;    			/*Banking*
	public final static byte PA_TYPE_FOODS = 6;    				/*Foods*/
	public final static byte PA_TYPE_PUBLIC_PERSON = 7;    		/*Public Person*/	
	public final static byte PA_TYPE_GOVERNMENT = 8;    		/*Government*/		
	public final static byte PA_TYPE_SPORTS = 9;    			/*Sports*/
	public final static byte PA_TYPE_LIVE = 10;    				/*Live*/
	public final static byte PA_TYPE_PUBLIC_NEWS = 11;    		/*News*/	
	public final static byte PA_TYPE_OTHERS = 12;  				/*Others*/

	
	/**************************************************************************************************/
	//afid type
	/**************************************************************************************************/	
	public final static byte AFMOBI_FRIEND_CACHE_MIN = 0;    			
	public final static byte AFMOBI_FRIEND_TYPE_FF = 0; //好友  							/*friends*/
	public final static byte AFMOBI_FRIEND_TYPE_BF = 1; //黑名�?		   					/*block list*/
	public final static byte AFMOBI_FRIEND_TYPE_STRANGER = 2;//陌生�?		   				/*stranger friends*/
	public final static byte AFMOBI_FRIEND_TYPE_GROUP = 3;			   				/*group list*/	
	public final static byte AFMOBI_FRIEND_TYPE_RECENT_RECORD = 4;
	public final static byte AFMOBI_FRIEND_TYPE_FRD_REQ = 5;
	public final static byte AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT = 6;
	public final static byte AFMOBI_FOLLOW_TYPE_MASTER = 7;
	public final static byte AFMOBI_FOLLOW_TYPE_PASSIVE = 8;
	public final static byte AFMOBI_FRIEND_TYPE_PUBLIC_GROUP = 9;			   		/*public group list*/	
	public final static byte AFMOBI_RRIEND_TYPE_CACHE_MAX = 10;
	public final static byte AFMOBI_FRIEND_TYPE_SBF = 11;								/*search nearby friends*/
	public final static byte AFMOBI_FRIEND_UNKNOWN = 12;
	
	//public account type
	public final static byte AFMOBI_FRIEND_TYPE_PFF = 13;     						/*public friends*/
	public final static byte AFMOBI_FRIEND_TYPE_PBF = 14;     						/*public strange friends*/
	
									
    /**************************************************************************************************/
	//follow type
	/**************************************************************************************************/	
    public final static byte AFMOBI_FOLLOW_UNKNOWN = 0;		
	public final static byte AFMOBI_FOLLOW_MASTRE  = 1;	
    public final static byte AFMOBI_FOLLOW_PASSIVE = 2;	
	public final static byte AFMOBI_FOLLOW_ALL = AFMOBI_FOLLOW_MASTRE|AFMOBI_FOLLOW_PASSIVE;	
	/**************************************************************************************************/
	//database type
	/**************************************************************************************************/	
	public final static int AFMOBI_DB_TYPE_FRIEND = 0;
	public final static int AFMOBI_DB_TYPE_PROFILE = 2;	
	public final static int AFMOBI_DB_TYPE_RECENT_MESSAGE = 10;		 /*recent message*/
	public final static int AFMOBI_DB_TYPE_FRIEND_REQ = 11;			/*friend req */
	 
	
	/**************************************************************************************************/
	//key type
	/**************************************************************************************************/		
	public final static byte AF_KEYTYPE_9KEYS = 0;				/*9 keys*/
	public final static byte AF_KEYTYPE_QWETRY = 1;				/* QWETRY*/
	public final static byte AF_KEYTYPE_TOUCH = 2;      		/*PURE TOUCH*/	
	public final static byte AF_KEYTYPE_9KEYS_TOUCH = 3;		/*PURE 9KEYS and TOUCH*/
	public final static byte AF_KEYTYPE_QWETRY_TOUCH = 4;		/* QWETRY TOUCH*/

	 
	
	/**************************************************************************************************/
	//login type
	/**************************************************************************************************/		
	public final static byte AF_LOGIN_PHONE = 1;				/*phone login*/
	public final static byte AF_LOGIN_AFID = 2;					/*afid login*/
	public final static byte AF_LOGIN_IMEI = 3;				    /*imei login*/
	public final static byte AF_LOGIN_EMAIL = 4;				/*email login*/
	public final static byte AF_LOGIN_FACEBOOK = 5;		   		/*facebook login*/
	public final static byte AF_LOGIN_GOOGLE = 6;				/*google login*/
	public final static byte AF_LOGIN_TWITTER = 7;				/*Twitter login*/
	public final static byte AF_LOGIN_AUTO_CHECK = 8;			
    
	/**************************************************************************************************/
	//avoid disturb
	/**************************************************************************************************/	
	public final static int AVOID_DISTURB_ALL_TIME = 0;				/*all time*/
	public final static int AVOID_DISTURB_IN_NIGHT = 1;				/*at night*/
	public final static int AVOID_DISTURB_OFF = 2;				        /*off*/
	
	/**************************************************************************************************/
	//login type
	/**************************************************************************************************/	
	public final static byte AFMOBI_GRP_STATUS_ACTIVE = 0;                			//group active
	public final static byte AFMOBI_GRP_STATUS_EXIT = 1;                			//group exit
	
	
	/**************************************************************************************************/
	//action type cmd/friendlist 
	/**************************************************************************************************/	
	//operation type
	public final static byte HTTP_ACTION_G = 0;                			//download friends
	public final static byte HTTP_ACTION_A = (HTTP_ACTION_G + 1);		//add friends
	public final static byte HTTP_ACTION_B = (HTTP_ACTION_A + 1);		//
	public final static byte HTTP_ACTION_C = (HTTP_ACTION_B + 1);
	public final static byte HTTP_ACTION_D = (HTTP_ACTION_C + 1);		//delete friends
	public final static byte HTTP_ACTION_MAX = (HTTP_ACTION_D + 1);
   
	//friends source type
	public final static byte FRIENDS_MAKE = 1;									//private friends
	public final static byte FRIENDS_GPS_SEARCH = (FRIENDS_MAKE + 1);			//gps serach friends
	public final static byte FRIENDS_SHAKES_SHAKE = (FRIENDS_GPS_SEARCH + 1);	//shakes and shakes friends
	public final static byte FRIENDS_PB_IMPORT = (FRIENDS_SHAKES_SHAKE + 1);    //import by phonebook
	public final static byte FRIENDS_PUBLIC_ACCOUNT = (FRIENDS_PB_IMPORT + 1);   //public account
	

	/**************************************************************************************************/
	//gender type
	/**************************************************************************************************/
	public final static byte AFMOBI_SEX_MALE = 0;    			/*male*/
	public final static byte AFMOBI_SEX_FEMALE = 1;  		   /*female*/
	public final static byte AFMOBI_SEX_FEMALE_AND_MALE = 2;   /*male or female*/ 
	
	/**************************************************************************************************/
	//message command
	/**************************************************************************************************/
	public final static byte MSG_CMMD_NORMAL = 0X1;    			//A01 normal message
	public final static byte MSG_CMMD_SHAKE = 0X2;    			//A02 vibrate msg
	public final static byte MSG_CMMD_FRD_REQ = 0X3;    		//A03 send friend request
	public final static byte MSG_CMMD_AGREE_FRD_REQ = 0X4;    	//A04 agree friend request
	public final static byte MSG_CMMD_RECOMMEND_CARD = 0X5;    	//A05 recommend friend card
	public final static byte MSG_CMMD_EMOTION = 0X6;    		//A06 special emojj
	public final static byte MSG_CMMD_READ = 0X8;    			//A08 send alread read msg
	public final static byte MSG_CMMD_STORE_EMOTION = 0X9;

	public final static byte MSG_CMMD_CHATROOM_NORMAL = 1;    	//A01 normal message
	public final static byte MSG_CMMD_CHATROOM_TOP = 5;    		//A05 copy and top
	public final static byte MSG_CMMD_CHATROOM_ADD = 10;       //A10  @ msg

	public final static byte MSG_CMMD_FORWARD = 101;       //forward command
	
	/**************************************************************************************************/
	//login status
	/**************************************************************************************************/
	public final static byte LOGIN_STATUS_UNLOGIN = 0;		//no login
	public final static byte LOGIN_STATUS_FRONT = 1;		//front login
	public final static byte LOGIN_STATUS_BACK = 2;			//background login
	public final static byte LOGIN_STATUS_CHATROOM = 3;		// chat room
	public final static byte LOGIN_STATUS_IOS = 4;		 	//IOS
	public final static byte LOGIN_STATUS_OFFLINE = 5;		 //off line

	/**************************************************************************************************/
	//set status param selecting
	/**************************************************************************************************/
	public final static byte AFMOBI_PARAM_STATUS = 0;			//set status
	public final static byte AFMOBI_PARAM_FID = 1;				//set fid
	public final static byte AFMOBI_PARAM_STATUS_AND_FID = 2;   //set  status and fid

	/**************************************************************************************************/
	//set store enum param
	/**************************************************************************************************/
	public final static byte PROD_BASE_TYPE = 0;			//set status
    public final static byte PROD_TYPE_EXPRESS_WALLPAPER = 1;			//set status
	public final static byte PROD_TYPE_SOCIAL_GAMES_TYPE = 2;			//set status
	public final static byte PROD_TYPE_SOFTWARE = 3;			//set status

	public final static byte PROD_CATEGORY_BASE = 0;			//set status
    public final static byte PROD_CATEGORY_EXPRESS = 10;			//set status
	public final static byte PROD_CATEGORY_WALLPAPER = 11;			//set status
	public final static byte PROD_CATEGORY_DOTA = 12;			//set status
	public final static byte PROD_CATEGORY_GIFT = 13;			//set status
	public final static byte PROD_CATEGORY_SOCIAL_GAMES = 20;			//set status
	public final static byte PROD_CATEGORY_APP = 30;			//set status
	public final static byte PROD_CATEGORY_GAMES = 31;			//set status

    public final static byte EXPRESS_TYPE_HD = 0;			//set status
    public final static byte EXPRESS_TYPE_ND = 1;			//set status
	public final static byte EXPRESS_TYPE_LD = 2;			//set status
	public final static byte EXPRESS_TYPE_XD = 3;			//set status
	/**************************************************************************************************/
	//set payment enum param
	/**************************************************************************************************/
	public final static byte PAYMENT_AFMARKET = 0;			//set status
	public final static byte PAYMENT_PUSHMAIL = 1;			//set status
	public final static byte PAYMENT_PALMCHAT = 2;			//set status
	public final static byte PAYMENT_FUNCARRIER = 3;			//set status
	public final static byte PAYMENT_HELLOAF = 4;			//set status
	public final static byte PAYMENT_AFMUSIC = 5;			//set status
	public final static byte PAYMENT_TZONE = 6;			//set status
	public final static byte PAYMENT_AFPAYMENT = 7;			//set status

	/**************************************************************************************************/
	//set payment transaction type
	/**************************************************************************************************/
	public final static byte PAYMENT_TRANS_RECHARGE = 0;			//set status
	public final static byte PAYMENT_TRANS_CONSUME = 1;			//set status

    /**************************************************************************************************/
	//set broadcast type
	/**************************************************************************************************/
	public final static byte  BR_TYPE_UNKNOW = -1;        /*unknow type,if have text content,display it*/
    public final static byte  BR_TYPE_TEXT = 0;           /*only text*/
    public final static byte  BR_TYPE_IMAGE_TEXT = 1;     /*image and text*/
    public final static byte  BR_TYPE_VOICE_TEXT = 2;     /*voice and text*/
    public final static byte  BR_TYPE_IMAGE = 3;          /*only image*/
    public final static byte  BR_TYPE_VOICE = 4;          /*only voice*/
    public final static byte  BR_TYPE_SHARE = 5;          /*only share，纯转发*/
    public final static byte  BR_TYPE_SHARE_TEXT = 6;     /*share with text�?带正文的转发*/
	public final static byte  BR_TYPE_VIDEO		= 7;        /* only video */
	public final static byte  BR_TYPE_VIDEO_TEXT	= 8;        /* video with text */
	
    /**
     * 以下BroadCast 来源页TYPE 的最大值不能超0X00FF跟中间件约定
     */
    public final static byte  DATA_FROM_UNKNOWN = 0;     /* unknown */
    public final static byte  DATA_FROM_LOCAL = 1;       /* local   */
    public final static byte  DATA_FROM_SERVER = 2;      /* server  */
    public final static byte  DATA_HOME_PAGE = 3;      /* home  */
    public final static byte  DATA_BROADCAST_PAGE = 4;      /* broadcast  */
    public final static byte  DATA_BROADCAST_FOLLOWING = 5;      /* broadcast  */
    public final static byte  DATA_BROADCAST_MYPROFILE = 6;      /* broadcast  */
    
	public static final int  AFMOBI_DATA_SOURCE_TYPE_USER_MAX		= 0X00FF;	/* max ( user define ) */
	public static final int AFMOBI_DATA_SOURCE_TYPE_SHARE_BASE		= 0X0100;	/* base ( broadcast share define ) */
	/**************************************************************************************************/
	//set broadcast media type
	/**************************************************************************************************/
	public final static byte  MEDIA_TYPE_AMR = 0;         
    public final static byte  MEDIA_TYPE_WAV = 1;    
    public final static byte  MEDIA_TYPE_MP3 = 2;     
    public final static byte  MEDIA_TYPE_JPG = 3;          
    public final static byte  MEDIA_TYPE_GIF = 4;          
    public final static byte  MEDIA_TYPE_PNG  =5;         
	/**************************************************************************************************/
	//set broadcast message type
	/**************************************************************************************************/
	public final static byte  AFMOBI_BRMSG_ALL = 0;     
	public final static byte  AFMOBI_BRMSG_INPUT = 1;     
	public final static byte  AFMOBI_BRMSG_OUTPUT = 2;     
	/**************************************************************************************************/

	//set broadcast message type
	/**************************************************************************************************/
	public final static byte  BC_PURVIEW_ALL = 0;     
	public final static byte  BC_PURVIEW_FOLLOW = 1;     
	public final static byte  BC_PURVIEW_FRIEND = 2;     
	/**************************************************************************************************/
    
	//set broadcast url type
	/**************************************************************************************************/
	public final static byte  URL_TYPE_MIN = 0;     
	public final static byte  URL_TYPE_IMG = 1;     
	public final static byte  URL_TYPE_VOICE = 2;    
	public final static byte  URL_TYPE_VEDIO = 3;   // 5.2.2 not supported, make a notice   
	public final static byte  URL_TYPE_GIF = 4;     // 5.2.2 not supported, make a notice   
	/**************************************************************************************************/  
 
	//set SMS code type
	/**************************************************************************************************/
	public final static byte  SMS_CODE_TYPE_REG = 1;     
	public final static byte  SMS_CODE_TYPE_BIND = 2;     
	public final static byte  	SMS_CODE_TYPE_RSTPWD = 3;    

	/**************************************************************************************************/

	//sys message to application
	/**************************************************************************************************/	
	public final static int AF_SYS_MSG_BASE = 0X500;
	/**
	 * \def AF_SYS_MSG_INIT
	 * \brief notify the application after the core is the end of initing.
	 */	
	public final static int AF_SYS_MSG_INIT = (AF_SYS_MSG_BASE + 1);	
	
	/**
	 * \def AF_SYS_MSG_RECV
	 * \param:  wparam AFMOBI_MESSAGE_INFO_PTR
	 * \brief notify the application after the core recive new message.
	 */	
	public final static int AF_SYS_MSG_RECV = (AF_SYS_MSG_INIT + 1);		

	/**
	 * \def AF_SYS_MSG_UPDATE_FRIEND
	 * \brief notify the application after the core get the newest friend and block from server.
	 */	
	public final static int AF_SYS_MSG_UPDATE_FRIEND = (AF_SYS_MSG_RECV + 1);
	
	/**
	 * \def AF_SYS_MSG_EXIT_CORE
	 * \brief notify the application after the core destroy all thread, and exit.
	 */	
	public final static int AF_SYS_MSG_EXIT_CORE = (AF_SYS_MSG_UPDATE_FRIEND + 1);	
	
	/**
 * \def AFMOBI_SYS_MSG_RE
 * \brief notify the application after the current palmchat user is logined in other client.
 */	
	public final static int AFMOBI_SYS_MSG_RELOGIN = (AF_SYS_MSG_EXIT_CORE + 1);
	
	/**
 * \def AFMOBI_SYS_MSG_SWITCH_ACCOUNT
 *  调AfCoreSwitchAccount会通知
 * \brief notify the application after the current account is switch
 */	
	public final static int AFMOBI_SYS_MSG_SWITCH_ACCOUNT = (AFMOBI_SYS_MSG_RELOGIN + 1);	

	   /**
	* \def AFMOBI_SYS_MSG_BACKGROUND_RELOGIN
	* \brief notify the application after core relogin because of -66 or -65
	*/
	public final static int AFMOBI_SYS_MSG_BACKGROUND_RELOGIN = (AFMOBI_SYS_MSG_SWITCH_ACCOUNT + 1);		
	   
	/**
	 * \def AFMOBI_SYS_MSG_GRP_NOTIFY
	 * \brief notify the application after group create, add, modify, remove and exit operation
	 */
	public final static int AFMOBI_SYS_MSG_GRP_NOTIFY = (AFMOBI_SYS_MSG_BACKGROUND_RELOGIN + 1);		

	/**
	 * \def AFMOBI_SYS_MSG_UPDATE_GRP
	 * \brief notify the application after getting grp profile from server
	 */
	public final static int AFMOBI_SYS_MSG_UPDATE_GRP = (AFMOBI_SYS_MSG_GRP_NOTIFY + 1);		

    /**
	 * \def AFMOBI_SYS_MSG_UPDATE_FOLLOW
	 * \brief notify the application after getting grp profile from server
	 */
	public final static int AFMOBI_SYS_MSG_UPDATE_FOLLOW = (AFMOBI_SYS_MSG_UPDATE_GRP + 1);		
	
	/**
	 * \def AFMOBI_SYS_MSG_OFFLINE
	 * \brief notify the application after req error or no connect to server
	 */
	public final static int AFMOBI_SYS_MSG_OFFLINE = (AFMOBI_SYS_MSG_UPDATE_FOLLOW + 1);			

		
	/**
	 * \def AFMOBI_SYS_MSG_CHATROOM_EXIT
	 * \brief notify the application after chatromm exit
	 */
	public final static int AFMOBI_SYS_MSG_CHATROOM_EXIT = (AFMOBI_SYS_MSG_OFFLINE + 1);	
	
	/**
	 * \def AFMOBI_SYS_MSG_READ_NOTIFY
	 * \param:	wparam AFMOBI_MESSAGE_INFO_PTR
	 * \brief notify the application after the core recive read status.
	 */ 
	public final static int AFMOBI_SYS_MSG_READ_NOTIFY = (AFMOBI_SYS_MSG_CHATROOM_EXIT + 1);	
	
	
	public final static int AFMOBI_SYS_AUTO_LOGIN_STATUS = (AFMOBI_SYS_MSG_READ_NOTIFY + 1);	

	public final static int AFMOBI_SYS_CANCEL_HTTP_REQ = (AFMOBI_SYS_AUTO_LOGIN_STATUS + 1);

    /**
     * \def AFMOBI_SYS_MSG_DP_PROXYLIST
     * \param:	AfDPProxyItem[]
     * \brief notify the application recv the proxylist.
     */
    public final static int AFMOBI_SYS_MSG_DP_PROXYLIST = (AFMOBI_SYS_CANCEL_HTTP_REQ + 1);
	/**************************************************************************************************/
	//http flag 
	/**************************************************************************************************/		
	public final static int REQ_DISPATCH =  0;	
	public final static int REQ_LONGIN_1 = (REQ_DISPATCH + 1);	
	public final static int REQ_LONGIN_2 = (REQ_LONGIN_1 + 1);	
	public final static int REQ_LONGIN_5 = (REQ_LONGIN_2 + 1);	
	public final static int REQ_LOGIN_6 = (REQ_LONGIN_5 + 1);
	public final static int REQ_CHECK_ACCOUNT_BY_PHONE =  (REQ_LOGIN_6 + 1);
	public final static int REQ_BIND_BY_PHONE =  (REQ_CHECK_ACCOUNT_BY_PHONE + 1);	
	public final static int REQ_REG_BY_PHONE = (REQ_BIND_BY_PHONE + 1);	
	public final static int REQ_REG_BY_EMAIL =  (REQ_REG_BY_PHONE + 1);		
	public final static int REQ_REG_BY_FACEBOOK =  (REQ_REG_BY_EMAIL + 1);		
	public final static int REQ_REG_BY_GOOGLE =  (REQ_REG_BY_FACEBOOK + 1);
	public final static int REQ_REG_BY_TWITTER = (REQ_REG_BY_GOOGLE + 1);
	public final static int REQ_CHECK_ILLEAGAL_WORDS = (REQ_REG_BY_TWITTER + 1);	
	public final static int REQ_RESET_PWD_BY_PHONE =  (REQ_CHECK_ILLEAGAL_WORDS + 1);	
	public final static int REQ_RESET_PWD_BY_EMAIL =  (REQ_RESET_PWD_BY_PHONE + 1);	
	public final static int REQ_CHECK_ACCOUNT_BY_EMAIL = (REQ_RESET_PWD_BY_EMAIL + 1);	
	public final static int REQ_PSD_GET_QUESTION =  (REQ_CHECK_ACCOUNT_BY_EMAIL + 1);	
	public final static int REQ_PSD_ANSWER =  (REQ_PSD_GET_QUESTION + 1);	
	public final static int REQ_GET_SMS_CODE_BEFORE_LOGIN =  (REQ_PSD_ANSWER + 1);	
	public final static int REQ_GET_SMS_CODE_AFTER_LOGIN =  (REQ_GET_SMS_CODE_BEFORE_LOGIN + 1);	
	public final static int REQ_GET_REG_RANDOM =  (REQ_GET_SMS_CODE_AFTER_LOGIN + 1);	
	
	/*the following following logined interface*/
	public final static int REQ_SPLITE_LOGINED =  (REQ_GET_REG_RANDOM + 1);		
	public final static int REQ_LOGOUT = (REQ_SPLITE_LOGINED + 1);	
	public final static int REQ_CHANGE_PASSWORD = (REQ_LOGOUT + 1);	
	public final static int REQ_CHECK_ACCOUNT_BY_PHONE_EX = (REQ_CHANGE_PASSWORD + 1);	
	public final static int REQ_BIND_BY_EMAIL =  (REQ_CHECK_ACCOUNT_BY_PHONE_EX + 1);	
	public final static int REQ_BIND_BY_PHONE_EX =  (REQ_BIND_BY_EMAIL + 1);		
	public final static int REQ_PHONEBOOK_IMPORT =  (REQ_BIND_BY_PHONE_EX + 1);
	public final static int REQ_PHONEBOOK_BACKUP = (REQ_PHONEBOOK_IMPORT + 1);	
	public final static int REQ_FRIEND_LIST = (REQ_PHONEBOOK_BACKUP + 1);	
	public final static int REQ_BLOCK_LIST =  (REQ_FRIEND_LIST + 1);	
	public final static int REQ_GET_USER_BATCH =  (REQ_BLOCK_LIST + 1);	
	public final static int REQ_POLLING =  (REQ_GET_USER_BATCH + 1);	
		
	public final static int REQ_MSG_SEND = (REQ_POLLING + 1);	
	public final static int REQ_MSG_FORWARD = (REQ_MSG_SEND + 1);
	public final static int REQ_SHAKE_AND_SHAKE = (REQ_MSG_FORWARD + 1);	
	public final static int REQ_SEARCH_USER =  (REQ_SHAKE_AND_SHAKE + 1);	
	public final static int REQ_LOOK_AROUND =  (REQ_SEARCH_USER + 1);	
	public final static int REQ_VOICE_SEND =  (REQ_LOOK_AROUND + 1);	
	
	public final static int REQ_GPS_CLEAN = (REQ_VOICE_SEND + 1);	
	public final static int REQ_NEAR_BY_GPS = (REQ_GPS_CLEAN + 1);	
	public final static int REQ_NEAR_BY_GPS_EX =  (REQ_NEAR_BY_GPS + 1);	
	public final static int REQ_NEAR_FRIEND =  (REQ_NEAR_BY_GPS_EX + 1);	
	public final static int REQ_NEAR_FRIEND_EX =  (REQ_NEAR_FRIEND + 1);
	public final static int REQ_GET_GPS_LOC = (REQ_NEAR_FRIEND_EX + 1);
	
	public final static int REQ_GRP_GET_PROFILE =  (REQ_GET_GPS_LOC + 1);
	public final static int REQ_GRP_CREATE =  (REQ_GRP_GET_PROFILE + 1);
	public final static int REQ_GRP_MODIFY = (REQ_GRP_CREATE + 1);
	public final static int REQ_GRP_REMOVE_MEMBER = (REQ_GRP_MODIFY + 1);
	public final static int REQ_GRP_ADD_MEMBER =  (REQ_GRP_REMOVE_MEMBER + 1);
	public final static int REQ_GRP_QUIT =  (REQ_GRP_ADD_MEMBER + 1);
	public final static int REQ_GRP_ADMIN_QUIT =  (REQ_GRP_QUIT + 1);
	public final static int REQ_GRP_LIST = (REQ_GRP_ADMIN_QUIT + 1);
	public final static int REQ_P_GRP_GET_PROFILE = (REQ_GRP_LIST + 1);
	public final static int REQ_P_GRP_MODIFY = (REQ_P_GRP_GET_PROFILE + 1);
	public final static int REQ_P_GRP_BROADCAST = (REQ_P_GRP_MODIFY + 1);
	public final static int REQ_P_GRP_REMOVE_MEMBER = (REQ_P_GRP_BROADCAST + 1);
	public final static int REQ_P_GRP_ADMIN_QUIT = (REQ_P_GRP_REMOVE_MEMBER + 1);
	public final static int REQ_P_GRP_ADD_MEMBER = (REQ_P_GRP_ADMIN_QUIT + 1);
	public final static int REQ_P_GRP_QUIT = (REQ_P_GRP_ADD_MEMBER + 1);
	public final static int REQ_P_GRP_LIST = (REQ_P_GRP_QUIT + 1);
	public final static int REQ_P_GRP_TAG_LIST = (REQ_P_GRP_LIST + 1);	
	public final static int REQ_P_GRP_MEMBER_LIST = (REQ_P_GRP_TAG_LIST + 1);	
	public final static int REQ_P_GRP_LIST_BY_GPS = (REQ_P_GRP_MEMBER_LIST + 1);	
	public final static int REQ_P_GRP_LIST_BY_TAG = (REQ_P_GRP_LIST_BY_GPS + 1);	
	public final static int REQ_P_GRP_LIST_BY_HOT = (REQ_P_GRP_LIST_BY_TAG + 1);	
	public final static int REQ_P_GRP_LIST_BY_COUNTRY = (REQ_P_GRP_LIST_BY_HOT + 1);	
	public final static int REQ_P_GRP_LIST_BY_CITY = (REQ_P_GRP_LIST_BY_COUNTRY + 1);	
	public final static int REQ_P_GRP_LIST_BY_KEYWORD = (REQ_P_GRP_LIST_BY_CITY + 1);	
	public final static int REQ_P_GRP_LIST_BY_GID = (REQ_P_GRP_LIST_BY_KEYWORD + 1);	
	
	public final static int REQ_GET_INFO = (REQ_P_GRP_LIST_BY_GID + 1);
	public final static int REQ_GET_INFO_EX = (REQ_GET_INFO + 1);
	public final static int REQ_GET_BATCH_INFO =  (REQ_GET_INFO_EX + 1);	
	public final static int REQ_LOGIN_STATUS =  (REQ_GET_BATCH_INFO + 1);	
	public final static int REQ_SET_ONLINE =  (REQ_LOGIN_STATUS + 1);
	public final static int REQ_GET_ONLINE_STATUS_NEW =  (REQ_SET_ONLINE + 1);
	
	public final static int REQ_UPDATE_INFO =  (REQ_GET_ONLINE_STATUS_NEW + 1);
	public final static int REQ_UPDATE_INFO_BY_IMEI =  (REQ_UPDATE_INFO + 1);	
	public final static int REQ_PB_IMPROT_FD =  (REQ_UPDATE_INFO_BY_IMEI + 1);	
	public final static int REQ_FB_IMPROT_FD =  (REQ_PB_IMPROT_FD + 1);	
	public final static int REQ_BBM_IMPROT_FD =  (REQ_FB_IMPROT_FD + 1);		
	/*password relatvie*/
	public final static int REQ_PSD_SET_ANSWER =  (REQ_BBM_IMPROT_FD + 1);
	public final static int REQ_MUTUAL_FRIEND =  (REQ_PSD_SET_ANSWER + 1);		
	public final static int REQ_PSD_GET_QUESTION_EX =  (REQ_MUTUAL_FRIEND + 1);

	/*dating and broadcast*/
	public final static int REQ_BROADCAST_MSG_SEND =  (REQ_PSD_GET_QUESTION_EX + 1);
	public final static int REQ_BROADCAST_MEDIA_UPLOAD_DP =  (REQ_BROADCAST_MSG_SEND + 1);
	public final static int REQ_BROADCAST_MEDIA_UPLOAD =  (REQ_BROADCAST_MEDIA_UPLOAD_DP + 1);
	public final static int REQ_BROADCAST_MSG_SEND_BY_CITY =  (REQ_BROADCAST_MEDIA_UPLOAD + 1);	
	public final static int REQ_BROADCAST_GET_HISTORY =  (REQ_BROADCAST_MSG_SEND_BY_CITY + 1);	
	public final static int REQ_BROADCAST_GET_HISTORY_BY_CITY =  (REQ_BROADCAST_GET_HISTORY + 1);	
	public final static int REQ_BROADCAST_MEDIA_DOWNLOAD =  (REQ_BROADCAST_GET_HISTORY_BY_CITY + 1);		
	public final static int REQ_SEND_GIFT =  (REQ_BROADCAST_MEDIA_DOWNLOAD + 1);
	public final static int REQ_GET_STAR =  (REQ_SEND_GIFT + 1); 
	public final static int REQ_UPDATE_MARRIAGE =  (REQ_GET_STAR + 1);
	public final static int REQ_UPDATE_SHOWSTAR =  (REQ_UPDATE_MARRIAGE + 1);
	public final static int REQ_UPDATE_SHOWMOBILE =  (REQ_UPDATE_SHOWSTAR + 1);
	public final static int REQ_GET_DATING_PHONE =  (REQ_UPDATE_SHOWMOBILE + 1);
	public final static int REQ_SAYHI =  (REQ_GET_DATING_PHONE + 1);
	public final static int REQ_GIFT_HISTORY =  (REQ_SAYHI + 1);	
	public final static int REQ_GIFT_GIVE_BY_SIGN =  (REQ_GIFT_HISTORY + 1);
	public final static int REQ_GIFT_GIVE =  (REQ_GIFT_GIVE_BY_SIGN + 1);		
	//promotion
	public final static int REQ_FLAG_PROMOTION =  (REQ_GIFT_GIVE + 1);	
	public final static int REQ_FLAG_ACCUSATION =  (REQ_FLAG_PROMOTION + 1);
	//BC5.0 FOR palmchat
	public final static int REQ_BCGET_PEOPLES_BY_CITY =  (REQ_FLAG_ACCUSATION + 1);	 
	public final static int REQ_BCGET_PEOPLES_BY_GPS =	(REQ_BCGET_PEOPLES_BY_CITY + 1);  
	public final static int REQ_BCGET_PEOPLES_BY_STATE = (REQ_BCGET_PEOPLES_BY_GPS + 1);
	public final static int REQ_BCGET_PEOPLES_ACCOUNTS_BY_GPS = (REQ_BCGET_PEOPLES_BY_STATE + 1);
	public final static int REQ_BCGET_COMMENTS_BY_CITY =  (REQ_BCGET_PEOPLES_ACCOUNTS_BY_GPS + 1);	 
	public final static int REQ_BCGET_COMMENTS_BY_GPS =  (REQ_BCGET_COMMENTS_BY_CITY + 1);
	public final static int REQ_BCGET_COMMENTS_BY_STATE = (REQ_BCGET_COMMENTS_BY_GPS + 1);
	public final static int REQ_BCGET_COMMENTS_BY_TAG_CITY =	(REQ_BCGET_COMMENTS_BY_STATE + 1);
	public final static int REQ_BCGET_COMMENTS_BY_TAG_GPS =	(REQ_BCGET_COMMENTS_BY_TAG_CITY + 1);
	public final static int REQ_BCGET_COMMENTS_BY_TAG =	(REQ_BCGET_COMMENTS_BY_TAG_GPS + 1);
    public final static int REQ_BCGET_PEOPLES_COMMENTS_BY_CITY =  (REQ_BCGET_COMMENTS_BY_TAG + 1);	 
	public final static int REQ_BCGET_PEOPLES_COMMENTS_BY_GPS =  (REQ_BCGET_PEOPLES_COMMENTS_BY_CITY + 1);
	
	public final static int REQ_BCGET_COMMENTS_BY_MID =  (REQ_BCGET_PEOPLES_COMMENTS_BY_GPS + 1);
	public final static int REQ_BCGET_COMMENTS_LIKE_BY_MID =  (REQ_BCGET_COMMENTS_BY_MID + 1);
	public final static int REQ_BCGET_LIKE_STAR_BY_HOUR =  (REQ_BCGET_COMMENTS_LIKE_BY_MID + 1);
	public final static int REQ_BCGET_LIKE_STAR_BY_DAY =  (REQ_BCGET_LIKE_STAR_BY_HOUR + 1);
	public final static int REQ_BCGET_PROFILE_BY_AFID = (REQ_BCGET_LIKE_STAR_BY_DAY + 1);
	public final static int REQ_CONNECTIONS_GET_INDEX_LIST = (REQ_BCGET_PROFILE_BY_AFID + 1);
	public final static int REQ_CONNECTIONS_GET_LIST_BY_MIDS = (REQ_CONNECTIONS_GET_INDEX_LIST + 1);
	public final static int REQ_CONNECTIONS_GET_LIST_BY_PAGE = (REQ_CONNECTIONS_GET_LIST_BY_MIDS + 1);
	
	public final static int REQ_BCGET_TRENDS  = (REQ_CONNECTIONS_GET_LIST_BY_PAGE + 1);
	public final static int REQ_BCGET_TRENDS_MORE  = (REQ_BCGET_TRENDS + 1);
	public final static int REQ_BCGET_HOT_TAGS = (REQ_BCGET_TRENDS_MORE + 1);
	public final static int REQ_BCGET_SEARCH_TAGS = (REQ_BCGET_HOT_TAGS + 1);
	public final static int REQ_BC_SHARE_BROADCAST = (REQ_BCGET_SEARCH_TAGS + 1);
	public final static int REQ_BC_SHARE_TAG = (REQ_BC_SHARE_BROADCAST + 1);
	public final static int REQ_BCGET_COMMENTS_BY_TAGNAME = (REQ_BC_SHARE_TAG + 1);
	public final static int REQ_BCGET_RECENT_HOTS_BY_TAGNAME = (REQ_BCGET_COMMENTS_BY_TAGNAME + 1);
	public final static int REQ_BCGET_DEFAULT_TAGS = (REQ_BCGET_RECENT_HOTS_BY_TAGNAME + 1);
	public final static int REQ_BCGET_LANGUAGE_PACKAGE = (REQ_BCGET_DEFAULT_TAGS + 1);
	public final static int REQ_BCGET_REGION_BROADCAST = (REQ_BCGET_LANGUAGE_PACKAGE + 1);
	public final static int REQ_BCGET_COMMENTS_BY_STATE_OR_CITY = (REQ_BCGET_REGION_BROADCAST + 1);
	
	/*miss nigeria*/
	public final static int REQ_MISS_NIGERIA_JOIN =  (REQ_BCGET_COMMENTS_BY_STATE_OR_CITY + 1);
	public final static int REQ_MISS_NIGERIA_FIRST_JOIN =  (REQ_MISS_NIGERIA_JOIN + 1);
	public final static int REQ_MISS_NIGERIA_HOME =  (REQ_MISS_NIGERIA_FIRST_JOIN + 1);
	public final static int REQ_MISS_NIGERIA_RANK =  (REQ_MISS_NIGERIA_HOME + 1);
	
	/*public account*/
	public final static int REQ_PUBLIC_ACCOUNT_LIST =  (REQ_MISS_NIGERIA_RANK + 1);
	public final static int REQ_PUBLIC_ACCOUNT_BATCH_INFO =  (REQ_PUBLIC_ACCOUNT_LIST + 1);
	public final static int REQ_PUBLIC_ACCOUNT_HISTORY =  (REQ_PUBLIC_ACCOUNT_BATCH_INFO + 1);

	public final static int REQ_CHATROOM_GET_LIST =  (REQ_PUBLIC_ACCOUNT_HISTORY + 1);	
	/*the following using chatroom special host ip and port */	
	public final static int REQ_CHATROOM_ENTRY =  (REQ_CHATROOM_GET_LIST + 1);		
	public final static int REQ_CHATROOM_POLLING =  (REQ_CHATROOM_ENTRY + 1);		
	public final static int REQ_CHATROOM_GET_MEMBER_LIST =  (REQ_CHATROOM_POLLING + 1);		
	public final static int REQ_CHATROOM_BM_CMD =  (REQ_CHATROOM_GET_MEMBER_LIST + 1);		
	public final static int REQ_CHATROOM_EXIT =  (REQ_CHATROOM_BM_CMD + 1);		
	public final static int REQ_CHATROOM_MSG_SEND =  (REQ_CHATROOM_EXIT + 1);		

	/*the following keep_connect*/
	public final static int REQ_LPOLL_URL =  (REQ_CHATROOM_MSG_SEND + 1);	
	public final static int REQ_FLAG_FOLLOW_LIST =  (REQ_LPOLL_URL + 1);	
	public final static int REQ_FLAG_FOLLOWERS_LIST =  (REQ_FLAG_FOLLOW_LIST + 1);	

	//5.0BROADCAST
	public final static int REQ_BC50_BEGIN =  (REQ_FLAG_FOLLOWERS_LIST + 1);	  
	public final static int REQ_BCMSG_PUBLISH =	(REQ_BC50_BEGIN + 1);  
	public final static int REQ_BCMEDIA_UPLOAD_DP =  (REQ_BCMSG_PUBLISH + 1);	
	public final static int REQ_BCMEDIA_UPLOAD =  (REQ_BCMEDIA_UPLOAD_DP + 1);
	public final static int REQ_BCMSG_CHECK_MID =  (REQ_BCMEDIA_UPLOAD + 1);	
	public final static int REQ_BCMSG_DELETE =	(REQ_BCMSG_CHECK_MID + 1);	
	public final static int REQ_BCMSG_AGREE =  (REQ_BCMSG_DELETE + 1);	
	public final static int REQ_BCMSG_COMMENT =  (REQ_BCMSG_AGREE + 1);	 
	public final static int REQ_BCCOMMENT_DELETE =	(REQ_BCMSG_COMMENT + 1);  
	public final static int REQ_BCMSG_ACCUSATION =  (REQ_BCCOMMENT_DELETE + 1);	
	public final static int REQ_BC50_END =	(REQ_BCMSG_ACCUSATION + 1);	
	
	/*the following medial url*/
	public final static int REQ_SPLITE_MEDIA = (REQ_BC50_END + 1);	
	public final static int REQ_MEDIA_INIT = (REQ_SPLITE_MEDIA + 1);	
	public final static int REQ_MEDIA_UPLOAD =  (REQ_MEDIA_INIT + 1);		
	public final static int REQ_AVATAR_INIT =  (REQ_MEDIA_UPLOAD + 1);	
	public final static int REQ_MEDIA_DOWNLOAD =  (REQ_AVATAR_INIT + 1);	
	
	
	/*the following avatar url*/
	public final static int REQ_SPLITE_AVATAR =  (REQ_MEDIA_DOWNLOAD + 1);		
	public final static int REQ_AVATAR_UPLOAD =  (REQ_SPLITE_AVATAR + 1);	
	public final static int REQ_AVATAR_DOWNLOAD =  (REQ_AVATAR_UPLOAD + 1);	
	public final static int REQ_AVATAR_DELETE =  (REQ_AVATAR_DOWNLOAD + 1);	
	public final static int REQ_AVATAR_WALL_MODIFY =  (REQ_AVATAR_DELETE + 1);	
	public final static int REQ_AVATAR_DOWNLOAD_DIRECT =  (REQ_AVATAR_WALL_MODIFY + 1);
	

    /*payment*/
	public final static int REQ_PAYMENT_BASE =  (REQ_AVATAR_DOWNLOAD_DIRECT + 1);
    public final static int REQ_PAYMENT_VCOIN_GET =(REQ_PAYMENT_BASE + 1);
	public final static int REQ_PAYMENT_GTCARD_RECHARGE =  (REQ_PAYMENT_VCOIN_GET + 1);
    public final static int REQ_PAYMENT_TRANS_RECORD =  (REQ_PAYMENT_GTCARD_RECHARGE + 1);
    public final static int REQ_PAYMENT_TRANS_DETAIL =  (REQ_PAYMENT_TRANS_RECORD + 1);
	public final static int REQ_PAYMENT_CONSUME =  (REQ_PAYMENT_TRANS_DETAIL + 1);
    public final static int REQ_PAYMENT_SMS_GETSN =  (REQ_PAYMENT_CONSUME + 1);
    public final static int REQ_PAYMENT_SMS_RECHARGE =  (REQ_PAYMENT_SMS_GETSN + 1);
	public final static int REQ_PAYMENT_WPIOS_RECHARGE =  (REQ_PAYMENT_SMS_RECHARGE + 1);
    public final static int REQ_PAYMENT_MAX =  (REQ_PAYMENT_WPIOS_RECHARGE + 1);

    /*store*/
	public final static int REQ_STORE_BASE =  (REQ_PAYMENT_MAX + 1);
    public final static int REQ_STORE_PROD_LIST_GET =  (REQ_STORE_BASE + 1);
    public final static int REQ_STORE_PROD_DETAIL =  (REQ_STORE_PROD_LIST_GET + 1);
    public final static int REQ_STORE_DOWNLOAD_PRE =  (REQ_STORE_PROD_DETAIL + 1);
    public final static int REQ_STORE_DOWNLOAD =  (REQ_STORE_DOWNLOAD_PRE + 1);
    public final static int REQ_STORE_VERSION_CHECK =  (REQ_STORE_DOWNLOAD + 1);
    public final static int REQ_STORE_CONSUME_RECORD =  (REQ_STORE_VERSION_CHECK + 1);
    public final static int REQ_STORE_GET_REMAIN_COIN =  (REQ_STORE_CONSUME_RECORD + 1);
    public final static int REQ_STORE_HAVE_NEW_PROD =  (REQ_STORE_GET_REMAIN_COIN + 1);
    public final static int REQ_STORE_PROD_RANK_TOP = (REQ_STORE_HAVE_NEW_PROD + 1);
    public final static int REQ_STORE_GET_MY_EXPRESSION = (REQ_STORE_PROD_RANK_TOP + 1);
    public final static int REQ_STORE_MAX =  (REQ_STORE_GET_MY_EXPRESSION + 1);
    
    /*Lottery Predict*/
    public final static int REQ_PREDICT_BASE = (REQ_STORE_MAX + 1);
    public final static int REQ_PREDICT_INIT = (REQ_PREDICT_BASE + 1);
    public final static int REQ_PREDICT_TURNLOTTERY = (REQ_PREDICT_INIT + 1);
    public final static int REQ_PREDICT_GETACTIONLIST = (REQ_PREDICT_TURNLOTTERY + 1);
    public final static int REQ_PREDICT_GETENDACTIONLIST = (REQ_PREDICT_GETACTIONLIST + 1);
    public final static int RRE_PREDICT_PREPBET = (REQ_PREDICT_GETENDACTIONLIST + 1);
    public final static int PRE_PREDICT_SUREBET = (RRE_PREDICT_PREPBET + 1);
    public final static int PRE_PREDICT_GETPOINTS = (PRE_PREDICT_SUREBET + 1);
    public final static int PRE_PREDICT_BETHISTORY = (PRE_PREDICT_GETPOINTS + 1);
    public final static int PRE_PREDICT_GETRANKING = (PRE_PREDICT_BETHISTORY + 1);
    public final static int PRE_PREDICT_GET_ANNOUNCEMENT = (PRE_PREDICT_GETRANKING + 1);
    public final static int PRE_PREDICT_FEEDBACK = (PRE_PREDICT_GET_ANNOUNCEMENT + 1);
    public final static int PRE_PREDICT_GET_POINTSDETAILLIST = (PRE_PREDICT_FEEDBACK + 1);
    public final static int PRE_PREDICT_GET_GIFTSADSLIST = (PRE_PREDICT_GET_POINTSDETAILLIST + 1);
    public final static int REQ_PREDICT_GET_GIFTSLIST = (PRE_PREDICT_GET_GIFTSADSLIST + 1);
    public final static int REQ_PREDICT_GET_GIFTSLIST2 = (REQ_PREDICT_GET_GIFTSLIST + 1);
    public final static int REQ_PREDICT_GET_ADDRESSLIST = (REQ_PREDICT_GET_GIFTSLIST2 + 1);
    public final static int REQ_PREDICT_GET_GIFTSEXCHANGELIST = (REQ_PREDICT_GET_ADDRESSLIST + 1);
    public final static int REQ_PREDICT_ADD_ADDRESS = (REQ_PREDICT_GET_GIFTSEXCHANGELIST + 1);
    public final static int REQ_PREDICT_INVITEFRIENDS = (REQ_PREDICT_ADD_ADDRESS + 1);
    public final static int REQ_PREDICT_RECOMMENDFRIENDS = (REQ_PREDICT_INVITEFRIENDS + 1);	
    public final static int REQ_PREDICT_BUY_POINTS_BYPREPAID = (REQ_PREDICT_RECOMMENDFRIENDS + 1);
    public final static int REQ_PREDICT_BUY_POINTS_BYCOINS = (REQ_PREDICT_BUY_POINTS_BYPREPAID + 1);
    public final static int REQ_PREDICT_BUY_POINTS_BYSMS = (REQ_PREDICT_BUY_POINTS_BYCOINS + 1);
    public final static int REQ_PREDICT_BUY_POINTS_BYSMS_GETSN = (REQ_PREDICT_BUY_POINTS_BYSMS + 1);
    public final static int REQ_PREDICT_BUY_POINTS_BYPAGA_GETORDERID = (REQ_PREDICT_BUY_POINTS_BYSMS_GETSN + 1);
    public final static int REQ_PREDICT_BUY_POINTS_GETCOINS = (REQ_PREDICT_BUY_POINTS_BYPAGA_GETORDERID + 1);
    public final static int REQ_PREDICT_MAX = (REQ_PREDICT_BUY_POINTS_GETCOINS + 1);
    
    /*5.1.3 New Payment*/
	public final static int REQ_PALM_COIN_BASE = (REQ_PREDICT_MAX + 1);
	public final static int REQ_PALM_COIN_GET_GOODS_LIST = (REQ_PALM_COIN_BASE + 1);
	public final static int REQ_PALM_COIN_GET_BILL_HISTORY = (REQ_PALM_COIN_GET_GOODS_LIST + 1);
	public final static int REQ_PALM_COIN_GET_MONEY2COINS_LIST = (REQ_PALM_COIN_GET_BILL_HISTORY + 1);
	public final static int REQ_PALM_COIN_GET_AIRTIME_TOPUP = (REQ_PALM_COIN_GET_MONEY2COINS_LIST + 1);
	public final static int REQ_PALM_COIN_CREATE_RECHARGE = (REQ_PALM_COIN_GET_AIRTIME_TOPUP + 1);
	public final static int REQ_PAYMENT_GET_RECHARGE_ORDERS_LOGS = (REQ_PALM_COIN_CREATE_RECHARGE + 1);
	public final static int REQ_PALM_COIN_MAX = (REQ_PAYMENT_GET_RECHARGE_ORDERS_LOGS + 1);
	
	
	// 5.3.0 PalmCall
	public final static int REQ_PALM_CALL_BASE = (REQ_PALM_COIN_MAX + 1);
	public final static int REQ_PALM_CALL_HOT_LIST = (REQ_PALM_CALL_BASE + 1);
	public final static int REQ_PALM_CALL_MAKECALL = (REQ_PALM_CALL_HOT_LIST + 1);
	public final static int REQ_PALM_CALL_UPLOAD_INTRO_MEDIA = (REQ_PALM_CALL_MAKECALL + 1);
    public final static int REQ_PALM_CALL_GET_LEFTTIME = (REQ_PALM_CALL_UPLOAD_INTRO_MEDIA + 1);
    public final static int REQ_PALM_CALL_SET_ALONE_PERIOD = (REQ_PALM_CALL_GET_LEFTTIME + 1);
    public final static int REQ_PALM_CALL_GET_ALONE_PERIOD = (REQ_PALM_CALL_SET_ALONE_PERIOD + 1);
    public final static int REQ_PALM_CALL_GET_INFO = (REQ_PALM_CALL_GET_ALONE_PERIOD + 1);
    public final static int REQ_PALM_CALL_GET_INFO_BATCH = (REQ_PALM_CALL_GET_INFO + 1);
	public final static int REQ_PALM_CALL_MAX = (REQ_PALM_CALL_GET_INFO_BATCH + 1);

    public final static int REQ_FLAG_PAYMENT_ANALYSIS = (REQ_PALM_CALL_MAX + 1);
	
	//palmplay
	/*public final static int REQ_FLAG_PALMPLAY_RANK = (REQ_PALM_CALL_MAX + 1);
	public final static int REQ_FLAG_PALMPLAY_DETAIL = (REQ_FLAG_PALMPLAY_RANK + 1);	
	public final static int REQ_FLAG_PALMPLAY_ISSUE_COMMENT = (REQ_FLAG_PALMPLAY_DETAIL + 1);	
	public final static int REQ_FLAG_PALMPLAY_COMMENT_LIST = (REQ_FLAG_PALMPLAY_ISSUE_COMMENT + 1);	
	public final static int REQ_FLAG_PALMPLAY_CHECK_VERSION = (REQ_FLAG_PALMPLAY_COMMENT_LIST + 1);	
	
	public final static int REQ_FLAG_PALMPLAY_PRE_DOWNLOAD = (REQ_FLAG_PALMPLAY_CHECK_VERSION + 1);	
	public final static int REQ_FLAG_PALMPLAY_FINISH_DOWNLOAD = (REQ_FLAG_PALMPLAY_PRE_DOWNLOAD + 1);	
	public final static int REQ_FLAG_PALMPLAY_FINISH_DOWNLOAD_BATCH = (REQ_FLAG_PALMPLAY_FINISH_DOWNLOAD + 1);	
	public final static int REQ_FLAG_PALMPLAY_SETUP = (REQ_FLAG_PALMPLAY_FINISH_DOWNLOAD_BATCH + 1);	
	public final static int REQ_FLAG_PALMPLAY_SETUP_BATCH = (REQ_FLAG_PALMPLAY_SETUP + 1);	
	
	public final static int REQ_FLAG_PALMPLAY_FINISH_SETUP_BATCH = (REQ_FLAG_PALMPLAY_SETUP_BATCH + 1);	
	public final static int REQ_FLAG_PALMPLAY_FILE_DOWNLOAD = (REQ_FLAG_PALMPLAY_FINISH_SETUP_BATCH + 1);
	
	public final static int REQ_FLAG_PALMPLAY_SEARCH = (REQ_FLAG_PALMPLAY_FILE_DOWNLOAD + 1);
	public final static int REQ_FLAG_PALMPLAY_SEARCH_TAG = (REQ_FLAG_PALMPLAY_SEARCH + 1);
	public final static int REQ_FLAG_PALMPLAY_SEARCH_CATEGORY = (REQ_FLAG_PALMPLAY_SEARCH_TAG + 1);
	public final static int REQ_FLAG_PALMPLAY_GET_SHOP_INFO = (REQ_FLAG_PALMPLAY_SEARCH_CATEGORY + 1);

	public final static int REQ_FLAG_PALMPLAY_MAX = (REQ_FLAG_PALMPLAY_GET_SHOP_INFO + 1);	*/
	/*public account url msg*/
	public final static int REQ_FLAG_POLLING_PA_GETDETAIL = (REQ_FLAG_PAYMENT_ANALYSIS + 1);
	
	
	/*custom flag req*/
	public final static int REQ_FLAG_MAX =  (REQ_FLAG_POLLING_PA_GETDETAIL + 1);		
	public final static int REQ_FLAG_CUSTOM =  (REQ_FLAG_MAX + 1);	
	public final static int REQ_FLAG_NO_GPS =  (REQ_FLAG_CUSTOM + 1);	
	public final static int REQ_FLAG_LOGIN =  (REQ_FLAG_NO_GPS + 1);
	public final static int REQ_FLAG_FEEDBACK =  (REQ_FLAG_LOGIN + 1);
	
	/*country or city*/
	public final static int REQ_FLAG_GET_CC =  (REQ_FLAG_FEEDBACK + 1);	
	public final static int REQ_FLAG_GET_CN =  (REQ_FLAG_GET_CC + 1);
	public final static int REQ_FLAG_GET_CITY_LIST =  (REQ_FLAG_GET_CN + 1);
	public final static int REQ_FLAG_GET_COUNTRY_LIST = (REQ_FLAG_GET_CITY_LIST + 1);
	
	/*version check or download*/
	public final static int REQ_FLAG_VERSION_CHECK =  (REQ_FLAG_GET_COUNTRY_LIST + 1);	
	public final static int REQ_FLAG_VERSION_DOWNLOAD = (REQ_FLAG_VERSION_CHECK + 1);	
	public final static int REQ_FLAG_STATISTIC = (REQ_FLAG_VERSION_DOWNLOAD + 1);
	public final static int REQ_FLAG_GET_GIS_LOCATION = (REQ_FLAG_STATISTIC + 1);
	public final static int REQ_FLAG_GET_IP_INFO = (REQ_FLAG_GET_GIS_LOCATION + 1);		
	//download file 
	public final static int REQ_FLAG_DOWNLOAD_FILE = (REQ_FLAG_GET_IP_INFO + 1);	
	
	//baidu gps loacation
	public final static int REQ_FLAG_BAIDU_LOCATION = (REQ_FLAG_GET_IP_INFO + 1);	
	
	public static final int GUIDE_SLIDE = 0;
	public static final int GUIDE_YOUR_FRIEND= GUIDE_SLIDE+1;
	public static final int GUIDE_SHAKE= GUIDE_YOUR_FRIEND+1;
	public static final int GUIDE_GROUP_CHAT= GUIDE_SHAKE+1;
	public static final int GUIDE_LOOK_AROUND= GUIDE_GROUP_CHAT+1;
	public static final int GUIDE_FINISH = GUIDE_LOOK_AROUND+1;
	public static final int PALMPLAY_DB_TYPE_DOWNLOAED = 0;
	public static final int PALMPLAY_DB_TYPE_DOWNLOADING_NORMAL = 1;
	public static final int PALMPLAY_DB_TYPE_DOWNLOADING_HOT_SPOT = 2;
	public static final int PALMPLAY_DB_TYPE_DOWNLOADING_OFFLINE = 3;
	/**************************************************************************************************/
	//http req code 
	/**************************************************************************************************/	
	public final static int HTTP_CODE_UNKNOWN = -1;
	//server req code
	public final static int REQ_CODE_SUCCESS = 0;
	public final static int REQ_CODE_PARAM_ERROR = -3;   		//param error or account is illgal users
	public final static int REQ_CODE_ACCOUNT_NOEXIST = -4;		 //account is not exist
	public final static int REQ_CODE_REMOVE_MEMBERS_NONE = -21;
	public final static int REQ_CODE_NO_BIND_EMAIL = -30; 		//no bind email
	public final static int REQ_CODE_YOU_HAVE_BEEN_REMOVED = -31; 		//you_have_been_removed
	public final static int REQ_CODE_FID_EXPIRED = -33;					//message forword expired
	public final static int REQ_CODE_FID_NOT_AVALIBALE = -34;			//message forword not avilable
	public final static int REQ_CODE_NOT_ENTRY_CHATROOM    = -41;	//not entry the specified chatroom	
	public final static int REQ_CODE_NOT_IN_CHATROOM    = -44;	//not in this chatroom
	public final static int REQ_CODE_USER_ALREADY_REGISTER = -50; //user already register
	public final static int REQ_CODE_CREATE_GROUP_LIMIT = -51;   //此人之所建群组已超出限制
	public final static int REQ_CODE_UNBIND_PHONE = -52;			//account isn't binded by phone
	public final static int REQ_CODE_CREATE_MEMBER_LIMIT = -53;   	//总成员超过该员所能创单群的人数上	
	public final static int REQ_CODE_MEMBER_EXIST =-54;   			//总成员不足三
	public final static int REQ_CODE_GROUP_NOT_EXIST = -62;			// 服务器群已经不存
	public final static int REQ_CODE_NO_CHATROOM = -62;				//no the specified chatroom	
	public final static int REQ_CODE_NO_GROUP = -63;				//无此群或者没有权限删除此
	public final static int REQ_CODE_65 = -65;				//无此群或者没有权限删除此
	public final static int REQ_CODE_REDISPATCH = -66;				//re-dispatch
	public final static int REQ_CODE_SMS_CODE_ERROR = -67;				//sms code error
	public final static int REQ_CODE_ILLEGAL_USER = -68;			//Illegal users
	public final static int REQ_CODE_PHONE_OR_EMAIL_ALREADY_BINDED = -69;		//phone or email already binded
	public final static int REQ_CODE_71 = -71;					// 留言者今天已经发送了留言
	public final static int REQ_CODE_BLOCK_PEOPLE = -73;	//You've blocked the people.
	public final static int REQ_CODE_GROUNP_NOT_EXISTS = -74;	//group not exists 
	public final static int REQ_CODE_SERVER_MAINTENANCE = -75;	//server maintenance 
	public final static int REQ_CODE_RELOGIN = -76;				//current palmchat user is logined in other client.
	public final static int REQ_CODE_MEDIA_SESSTION_TIMEOUT = -80;//media session timeout
	public final static int REQ_CODE_FILE_NOT_EXIST = -83;		// File does not exist
	public final static int REQ_CODE_MSTOKE_FAILURE = -82;		// MSTOKEN失效
	public final static int REQ_CODE_AFID_NOT_FOUND = -84;
	public final static int REQ_CODE_ILLEGAL_USER_G = -89;//无效用户
	public final static int REQ_CODE_UNKNOWN = -99;				// unknown error
	public final static int REQ_CODE_98 = -98;				// server error
	public final static int REQ_CODE_100 = -100;				// 留言者未绑定手机号或未上传头像无法留言
	public final static int REQ_CODE_101 = -101;				// 留言�?4小时内已经发送了留言
	public final static int REQ_CODE_102 = -102;				// not enough gift flower
	public final static int REQ_CODE_103 = -103;				// not enough gift flower
	public final static int REQ_CODE_104 = -104;				// not enough gift flower
	public final static int REQ_CODE_105 = -105;				// can not watch
	public final static int REQ_CODE_106 = -106;				// already get give by sign
	public final static int REQ_CODE_107 = -107;				// already upload photo or phone
	public final static int REQ_CODE_108 = -108;				// not enough gift flower
	public final static int REQ_CODE_110 = -110;				// already report

	public final static int REQ_CODE_130 = -130;                // already been recommend
	public final static int REQ_CODE_131 = -131;                // not in nigeria

	public final static int REQ_CODE_141 = -141;                // No authorization to delete
	public final static int REQ_CODE_142 = -142;                // The broadcast message does not exist or has been deleted
	public final static int REQ_CODE_143 = -143;                // This user has been report
	public final static int REQ_CODE_144 = -144;                // Cannot report broadcast news of his hair
	public final static int REQ_CODE_169 = -169;                // repeated like
	public final static int REQ_CODE_200 = -200;                // profile country不存�?
	public final static int REQ_CODE_201 = -201;                //The broadcast message does not exist or has been deleted
	public final static int REQ_CODE_202 = -202;                //profile broadcast not exist or has ben deleted
	public final static int REQ_CODE_189 = -189;                //广播附件上传超时
	public final static int REQ_CODE_203 = -203;     			//广播超过30�?无法评论
	public final static int REQ_CODE_TAG_ILLEGAL = -205;        //Tag不合法或违规
	public final static int REQ_CODE_ILLEGAL_WORD = -207;     	//相关语句中含有非法词�?

	public final static int REQ_CODE_212 = -212;                // 24小时内已有SMS购买计录, 无法再次购买
	public final static int REQ_CODE_213 = -213;                // 不支持SMS充�?
	
	public final static int REQ_CODE_220 = -220;                // 余额不足
	public final static int REQ_CODE_221 = -221;                // 点卡帐号密码错误
	public final static int REQ_CODE_222 = -222;                // 点卡帐号已被锁定
	public final static int REQ_CODE_223 = -223;                // 点卡帐号已被使用
	public final static int REQ_CODE_224 = -224;     			// 点卡帐号已被禁用
	
	public final static int REQ_CODE_NO_DATA				= -300;								 // 无数�?
	public final static int REQ_CODE_NO_RECHARGE_ITEM		= -310;								 // 此充值选项不存�?
	public final static int REQ_CODE_NO_RECHARGE_RULE		= -313;								 // 此充值选项ID存在，但与制定的规则不符
	public final static int REQ_CODE_CREATE_RECHARGE_FAIL	= -315;								 // 订单创建失败
	public final static int REQ_CODE_NO_ENOUGH_COIN		    = -321;								 // 该帐�?Coin 余额不足
	public final static int REQ_CODE_NOT_SUPPORT_COUNTRY	= -322;								 // 国家码出错或该国家暂不支持此服务
	public final static int REQ_CODE_RECHARGE_FAIL		    = -326;								 // 话费充值失�?
	public final static int REQ_CODE_PAYMENT_MAINTAIN		= -351;								 // 支付模块正在维护�?
	
	// PalmCall 
	public final static int REQ_CODE_PALMCALL_NO_ENOUGH_MINITES_801    = -801;		   // 拨打分钟数不足, 无法拨打
	public final static int REQ_CODE_PALMCALL_PALMCHAT_UNLOGINED_802   = -802;	       // 对方未登入Palmchat无法拨打
	public final static int REQ_CODE_PALMCALL_NOT_SUPPORT_803          = -803;	       // 对方不支援Palmcall功能
	public final static int REQ_CODE_PALMCALL_ID_ERR_804               = -804;	       //对方JustalkID码错误
	public final static int REQ_CODE_PALMCALL_COUNTRY_NOT_SUPPORT_805  = -805;	       //拨打对方用户的所在国家不支援Palmcall功能
	public final static int REQ_CODE_PALMCALL_ON_CALLING_806           = -806;	       //拨打对方用户正在通话中
	public final static int REQ_CODE_PALMCALL_THIRD_UNLOGINED_807      = -807;	       //拨打对方用户未登入语音系统
	public final static int REQ_CODE_PALMCALL_SERVER_DEDUCTION_808     = -808;         //服务器正在扣费，系统忙
    public final static int REQ_CODE_PALMCALL_ALONE_PERIOD_809         = -809;           // 对方正处于免打扰时间中
	
	// 黑名�?
	public final static int REQ_CODE_FRIEND_BLOCKOPR_MAX_LIMIT = -260;   //  黑名单达到上�?
	public final static int REQ_CODE_BROADCAST_SHARE_ERROR = -62003;  // 上传的转发的mid有问题，该mid对应的广播不能被转发 

	/*predict win zhh 2015-7-25 start*/
	public final static int REQ_CODE_8010 = -8010;              //下注超时
	public final static int REQ_CODE_8011 = -8011;              //缺少points
	public final static int REQ_CODE_8012 = -8012;              //无此活动信息 �?活动已截�?
	public final static int REQ_CODE_8016 = -8016;              //同一活动�?0秒内不能重复投注(如果返回此code,会有expire字段,此活动可下投注时间还有多少秒)
	public final static int REQ_CODE_8065 = -8065;              //找无此pwsession资料
	public final static int REQ_CODE_8066 = -8066;              //pwtoken不符
	public final static int REQ_CODE_8076 = -8076;              //该账号被重新登入，该pwsession无效
	public final static int PHONE_UNBIND = -210;          		//未绑定手�?
	public final static int COUNTRY_NOT_NIGERIA = -211;          		//非尼日利亚国�?
	public final static int SERVER_IS_MAINTENANCE = -75;		//服务器正在维�?
	/*predict win zhh 2015-7-25 end*/
	//palmplay commone error begin
	public final static int REQ_CODE_500 = -500;              
	public final static int REQ_CODE_501 = -501;              
	public final static int REQ_CODE_502 = -502;             
	public final static int REQ_CODE_503 = -503;              
	public final static int REQ_CODE_504 = -504;              
	public final static int REQ_CODE_505 = -505;  
	public final static int REQ_CODE_506 = -506;
	public final static int REQ_CODE_507 = -507;
	public final static int REQ_CODE_508 = -508;
	public final static int REQ_CODE_509 = -509;
	public final static int REQ_CODE_510 = -510;
	public final static int REQ_CODE_511 = -511;
	public final static int REQ_CODE_515 = -515;
	
	public final static int REQ_CODE_MISS_NIGERIA_NO_PROFILE = -170; //Not satisfied profile in miss nigeria activity
	public final static int REQ_CODE_MISS_NIGERIA_PAGEID_MAX = -172;   //Pageid is larger than the maximum of 200
	public final static int REQ_CODE_MISS_NIGERIA_ACTIVITY_END = -174;   //cancel miss Nigeria
	
	public final static int REQ_CODE_SMS_CHECK_ERROR= -58;   //sms code invalid
	public final static int REQ_CODE_SMS_CODE_LIMIT = -190;   //sms code invalid
	public final static int REQ_CODE_SMS_CODE_ALREADY_REGISTERED = -191;   //phone number already registered
	public final static int REQ_CODE_SMS_CODE_ALREADY_BINDED = -192;   //phone number already binded
	public final static int REQ_CODE_SMS_CODE_NOT_REGISTER_OR_BIND = -193;   //phone number does not register or bind
	public final static int REQ_CODE_READ = -123123;  			//message already read
	public final static int REQ_CODE_PAYSTORE_BASE = 0x700;
	public final static int REQ_CODE_PAYSTORE_SERVICE_ERR = REQ_CODE_PAYSTORE_BASE+1;//paystore service error
	public final static int REQ_CODE_PAYSTORE_REQUEST_ERR = REQ_CODE_PAYSTORE_BASE+2;//paystore request error
	public final static int REQ_CODE_PAYSTORE_ACCOUNT_ERR = REQ_CODE_PAYSTORE_BASE+10;//paystore account error
	public final static int REQ_CODE_PAYSTORE_MEMONY_ERR = REQ_CODE_PAYSTORE_BASE+20;//paystore recharge error
	public final static int REQ_CODE_PAYSTORE_SESSION_ERR = REQ_CODE_PAYSTORE_BASE+61;//paystore c/s session error
	public final static int REQ_CODE_PAYSTORE_CONVERSATION_ERR = REQ_CODE_PAYSTORE_BASE+2000;//paystore c/s conversation error.
	public final static int REQ_CODE_4096 = 4096;
	
	public final static int REQ_CODE_PAYSTORE_IMSI_MCC_ERR = 50;
		
	public final static String GET_SCORE = "https://play.google.com/store/apps/details?id=com.palmchatnow";
	
	
	//custom req code
	public final static int REQ_CODE_CUSTOM = 0XFFF;
	public final static int REQ_CODE_UNNETWORK = REQ_CODE_CUSTOM + 1; //not network
	public final static int REQ_CODE_CANCEL = REQ_CODE_UNNETWORK+1;   // cancel network
	/**
	 * 发送文本消�?
	 */
	public static final int SEND_MSG = 0;
	/**
	 * 发送语音消�?
	 */
	public static final int SEND_VOICE = 1;
	/**
	 * 发送图片消�?
	 */
	public static final int SEND_IMAGE = 2;

	//Grp profile type
    
	public final static byte REQ_TYPE_INSERT = 0;	
	public final static byte REQ_TYPE_UPDATE = 1;
	public final static byte REQ_TYPE_REMOVE = 2;
	public final static byte REQ_TYPE_SEARCH = 3;
	public static final int ACTION_ADD = 0;
	public static final int ACTION_REMOVE = 1;
	public static final int FROM_CHATTING = 2;
	public static final int FROM_GROUP_CHAT = 3;
	public static final String FAQ = "http://www.palmchatnow.com/faq.html";
	public static final String MISS_NIGERIA_HELP = "http://m.palmchatnow.com/missnigeria/joinmissnigeria";
	public static final String ABOUT_HELP = "http://www.palmchatnow.com";
	public static final int CHATTING = 0;
	public static final int GROUPCHATTING = 1;
	public static final int NORMAL = -1;
	public static final int SHARE_CHATTING = 2;
	
	public static final int AUTO_LOGIN_STATUS_TYPE_START = 0;    /*send to sys before auto-login*/
	public static final int AUTO_LOGIN_STATUS_TYPE_FAILED = 1;   /*send to sys after auto-login*/
	public static final int AUTO_LOGIN_STATUS_TYPE_PARAM_FAILED = 2;   /*send to sys after auto-login,param err*/

	
	/*private ReqCodeListener reqCodeListener;
	
	public ReqCodeListener getReqCodeListener() {
		return reqCodeListener; 
	}

	public void setReqCodeListener(ReqCodeListener ReqCodeListener) {
		this.reqCodeListener = ReqCodeListener;
	}*/
	public void showToast(Context context,int code,int flag,int http_code,boolean isVisible) {
		if(isVisible){
			showToast(context,  code,  flag,  http_code);
		}
	}
	
		// TODO Auto-generated method stub
	public void showToast(Context context,int code,int flag,int http_code) {
//		writeResponseCode(response);
		PalmchatLogUtils.println("code begin "+code);
		/*if(context instanceof ReqCodeListener){
			setReqCodeListener((ReqCodeListener)context);
		}*/
		PalmchatLogUtils.println("showToast  context  "+context);
		if(context == null){
			return;
		}
		
		switch (code) {
		case REQ_CODE_PARAM_ERROR:
			ToastManager.getInstance().show(context, context.getString(R.string.request_params_error));
			break;
		case REQ_CODE_ACCOUNT_NOEXIST:
			PalmchatLogUtils.println("code2  "+code);
			ToastManager.getInstance().show(context, context.getString(R.string.invalid_password));
			break;
		case REQ_CODE_CREATE_GROUP_LIMIT:
			ToastManager.getInstance().show(context, context.getString(R.string.groupchat_num_limit));
			break;
		case REQ_CODE_UNBIND_PHONE:
			ToastManager.getInstance().show(context, context.getString(R.string.bind_phone_messages));
			break;
		case REQ_CODE_REDISPATCH:
			ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
//			sendUmeng(context,code,flag,http_code);
			break;
		case REQ_CODE_ILLEGAL_USER:
			ToastManager.getInstance().show(context, context.getString(R.string.illegal_user));
			break;
		case Consts.REQ_CODE_BLOCK_PEOPLE:
			ToastManager.getInstance().show(context, context.getString( R.string.beblocked));
				break;
		case REQ_CODE_SERVER_MAINTENANCE:
			ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
//			sendUmeng(context,code,flag,http_code);
			break;
		case REQ_CODE_UNKNOWN:
			ToastManager.getInstance().show(context, context.getString(R.string.unkonw_error));
			break;
		case REQ_CODE_UNNETWORK:
			ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
//			sendUmeng(context,code,flag,http_code);
			break;
		case REQ_CODE_USER_ALREADY_REGISTER:
			/*if(reqCodeListener != null){
				reqCodeListener.reqCode(code,flag);
			}*/
			ToastManager.getInstance().show(context, R.string.number_has_registered);
			break;
		case REQ_CODE_PHONE_OR_EMAIL_ALREADY_BINDED:
			/*if(reqCodeListener != null){
				reqCodeListener.reqCode(code,flag);
			}*/
			ToastManager.getInstance().show(context,R.string.email_binded);
			break;
		case REQ_CODE_NO_BIND_EMAIL:
			ToastManager.getInstance().show(context,com.afmobigroup.gphone.R.string.no_bind_email);
			break;
		case REQ_CODE_NO_GROUP ://无此??
			ToastManager.getInstance().show(context, context.getString(R.string.no_group_or_illegal));
			break;		
		case REQ_CODE_ILLEGAL_USER_G:
			ToastManager.getInstance().show(context, context.getString(R.string.illegal_user));
			break;
		case Consts.REQ_CODE_CREATE_MEMBER_LIMIT:
			ToastManager.getInstance().show(context, context.getString(R.string.max_member_num_text));
		/*case Consts.REQ_CODE_MEMBER_EXIST:
			if(reqCodeListener != null){
				reqCodeListener.reqCode(code,flag);
			}
			break;*/
//		case Consts.REQ_GRP_GET_PROFILE:
//			ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
//			break;
//		case Consts.REQ_GRP_MODIFY:
//			ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
//			break;
//		case Consts.REQ_GRP_REMOVE_MEMBER:
//			ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
//			break;
//		case Consts.REQ_GRP_QUIT:
//			ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
//			break;
		case Consts.REQ_CODE_GROUP_NOT_EXIST:
			ToastManager.getInstance().show(context, context.getString(R.string.group_exist));
			break;
		case Consts.REQ_CODE_YOU_HAVE_BEEN_REMOVED:
			ToastManager.getInstance().show(context, context.getString(R.string.you_have_been_removed));
			break;
		case Consts.REQ_CODE_SMS_CODE_ERROR:
			ToastManager.getInstance().show(context, context.getString(R.string.verification_code_not_correct));
			break;
		case Consts.REQ_CODE_106:
			ToastManager.getInstance().show(context, context.getString(R.string.come_tomorrow));
			break;
		case Consts.REQ_CODE_108:
			ToastManager.getInstance().show(context, R.string.not_enough_flower_to_send);
			break;
		case Consts.REQ_CODE_MISS_NIGERIA_PAGEID_MAX:
			PalmchatLogUtils.println("no more data.");
			break;
		case Consts.REQ_CODE_SMS_CODE_LIMIT:
			ToastManager.getInstance().show(context, R.string.sms_code_limit);
			break;
		case Consts.REQ_CODE_SMS_CODE_ALREADY_REGISTERED:
			ToastManager.getInstance().show(context, R.string.number_has_registered);
			break;
		case Consts.REQ_CODE_SMS_CODE_ALREADY_BINDED:
//			ToastManager.getInstance().show(context, R.string.number_bind);
			break;
		case Consts.REQ_CODE_SMS_CODE_NOT_REGISTER_OR_BIND:
			ToastManager.getInstance().show(context, R.string.sms_code_not_register_or_bind);
			break;
		case  Consts.REQ_CODE_141:
			ToastManager.getInstance().show(context, R.string.no_authorization_del);
			break;
		case  Consts.REQ_CODE_142:
		case  Consts.REQ_CODE_202:
		case  Consts.REQ_CODE_201:
		case  Consts.REQ_CODE_BROADCAST_SHARE_ERROR:
			ToastManager.getInstance().show(context, R.string.bc_not_exist);
			break;
		case  Consts.REQ_CODE_143:
			ToastManager.getInstance().show(context, R.string.has_report);
			break;
		case  Consts.REQ_CODE_144:
			ToastManager.getInstance().show(context, R.string.own_broadcast_not_report);
			break;
		case  Consts.REQ_CODE_169:
//			ToastManager.getInstance().show(context, R.string.repeated_praise);
			break;
		case REQ_CODE_203:
			ToastManager.getInstance().show(context, context.getString(R.string.post_failed_broadcast_expired));
			break;
			
		case REQ_CODE_TAG_ILLEGAL:{
			ToastManager.getInstance().show(context, context.getString(R.string.tagillegal));
			break;
		}
			
		case REQ_CODE_212:
			ToastManager.getInstance().show(context, context.getString(R.string.cannot_recharge_24hours));
			break;
		case REQ_CODE_213:
			ToastManager.getInstance().show(context, context.getString(R.string.sms_not_support_country));
			break;
			
		case REQ_CODE_221:
			ToastManager.getInstance().show(context, context.getString(R.string.recharge_card_pwd_error));
			break;
		case REQ_CODE_222:
			ToastManager.getInstance().show(context, context.getString(R.string.recharge_card_is_locked));
			break;
		case REQ_CODE_223:
			ToastManager.getInstance().show(context, context.getString(R.string.recharge_card_is_used));
			break;
		case REQ_CODE_224:
			ToastManager.getInstance().show(context, context.getString(R.string.recharge_card_is_disable));
			break;
		case REQ_CODE_ILLEGAL_WORD:  //zhh相关语句中含有非法词�?
			ToastManager.getInstance().show(context, context.getString(R.string.inappropriate_word));
			break;
		case REQ_CODE_PALMCALL_NO_ENOUGH_MINITES_801:
			ToastManager.getInstance().show(context, context.getString(R.string.palmcall_self_lockoftime_801));
			break;
		case REQ_CODE_PALMCALL_PALMCHAT_UNLOGINED_802:
			ToastManager.getInstance().show(context, context.getString(R.string.palmcall_opposite_notlogined_802));
			break;
		case REQ_CODE_PALMCALL_NOT_SUPPORT_803:
			ToastManager.getInstance().show(context, context.getString(R.string.palmcall_opposite_nonsupport_803));
			break;
		case REQ_CODE_PALMCALL_ID_ERR_804:
			ToastManager.getInstance().show(context, context.getString(R.string.palmcall_opposite_palmcallid_error_804));
			break;
		case REQ_CODE_PALMCALL_COUNTRY_NOT_SUPPORT_805:
			ToastManager.getInstance().show(context, context.getString(R.string.palmcall_opposite_nation_nonsupport_805));
			break;
		case REQ_CODE_PALMCALL_ON_CALLING_806:
			ToastManager.getInstance().show(context, context.getString(R.string.palmcall_opposite_calling_806));
			break;
		case REQ_CODE_PALMCALL_THIRD_UNLOGINED_807:
			ToastManager.getInstance().show(context, context.getString(R.string.palmcall_opposite_notlogged_807));
			break;
		case REQ_CODE_PALMCALL_SERVER_DEDUCTION_808:
			ToastManager.getInstance().show(context,context.getString(R.string.palmcall_palmcall_server_is_busying_808));
			break;
		case REQ_CODE_PALMCALL_ALONE_PERIOD_809:
			ToastManager.getInstance().show(context,context.getString(R.string.palmcall_palmcall_notdisturb__809));
			break;
		default:
			PalmchatLogUtils.println("showToast default");
			ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
//			sendUmeng(context,code,flag,http_code);
			break;
		}
		PalmchatLogUtils.println("code  end  "+code);
	}

//	void sendUmeng(Context context,int code,int flag,int http_code){
//		if(context instanceof BaseActivity){
//			((BaseActivity)context).sendUmengLog(http_code, flag, code);
//		}
//	}

}
