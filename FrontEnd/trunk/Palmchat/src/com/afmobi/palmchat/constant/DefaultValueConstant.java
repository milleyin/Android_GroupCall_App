package com.afmobi.palmchat.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Pair;
import android.view.KeyEvent;

public class DefaultValueConstant {

	public static String MCC = null;
	public static String MNC = "01";
	public static String IMSI = "";
	public static String UA = "Tecno T1";
	public static String MVER = "4.2.0";
	public static String OS_VERSION = RequestConstant.ANDROID;
	public static String CID = "2";
	public static String SMSC = "0";
	public static int WIDTH = 480;
	public static int HEIGHT = 800;
	public static String COUNTRY_CODE = "86";  //"86" //modify by zhh 2015/08/18
	public static String SDK = "sdk";
	public static String SEX_M = "M";
	public static String SEX_F = "F";
	public static String S = "{s}";
	public static String AF = "af";
	public static final int LENGTH_0 = 0;
	public static final int LENGTH_6 = 6;
	public static final int LENGTH_10 = 10;
	public static final int LENGTH_16 = 16;
	public static final int LENGTH_USERNAME = 40;
	public static final int LENGTH_100 = 100;
	public static final String BIRTHDAY = "1990-1-5";
	public static final String CHINA = "China";
	public static final int _86 = 86;
	public static final String ACTIVITY = "CompleteActivity";
	public static final int RESULT_10 = 10;
	public static final String dispatchURL = "http://218.17.157.95:8088";
	public static final String REQUEST_GET_MY_COUNTRY_INFO = dispatchURL+"/mycountry/?gps=1";
	public static final int LENGTH_2 = 2;
	public static final String COUNTRY = "China";
	
	/*Receiver Action*/
	public static final String RECEIVE_ACTION_ANDROID_PROVIDER_TELEPHONY_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	public static final String RECEIVE_ACTION_ANDROID_INTENT_ACTION_DATA_SMS_RECEIVED = "android.intent.action.DATA_SMS_RECEIVED";
	
	/*Receiver DataScheme*/
	public static final String RECEIVE_DATA_SCHEME_SMS = "sms";
	
	/*Receiver DataAuthority*/
	public static final String RECEIVE_DATA_AUTHORITY_KEY = "localhost";
	public static final String RECEIVE_DATA_AUTHORITY_VALUE = "51234";
	public static final int MAX_SIZE = 6100;
	public static final int MAX_SIZE_SORT_BY_INFO = 50;
	public static final String WORK_DIR = Environment.getExternalStorageDirectory().getPath();
	public static final String RES_DIR = Environment.getExternalStorageDirectory().getPath()+"/.afmobi/palmchat";
	public static final String JPG = ".jpg";
	public static final String EMPTY = "";
	public static final String TARGET_NAME = "{$targetName}";
	public static final String TARGET_MEMBERS = "{$targetMembers}";
	public static final String TARGET_GNAME = "{$targetGName}";
	public static final String TARGET_STATUS = "{$targetStatus}";
	public static final String TARGET_SMS_RANDOM_CODE = "{$targetSMSRandomCode}";
	public static final String VERIFY_MESSAGE = "Success ! You can use the email to login Palmchat or retrieve password. Enjoy Palmchat !";
	public static final String IS_TRUE = "true";
	
	public static final String TARGET_GET_GIFT_NUM = "{$targetGetGiftNum}";
//	public static final String TARGET_GET_GIFT = "{$targetGetGift}";
//	public static final String TARGET_CHARM_LEVLE = "{$targetCharmLevel}";
	public static final String TARGET_CHARM_LEVLE_NUM = "{$targetCharmLevelNum}";
	public static final int MAX_SELECTED_BROADCAST_PIC = 9;

	public static String PALMCHAT_ID = null;
	

    public static LinkedHashMap<String, Pair<Bitmap, Integer>> avatarCache = new LinkedHashMap<String, Pair<Bitmap, Integer>>(50 , 0.5f , true);
//    public static String AMR = ".amr";
    

    public static HashMap<String, Integer> groupIdHasMemberHash = new HashMap<String, Integer>();
    
    public final static int DIA_INITGROUPMEMBERS_WAIT = 5;
    

	public static float currentDensity;
	

	public static final float HIGH_DENSITY = 1.5f;
    

	public static Timer timer = new Timer();
	
	
	public final static int DELETE_CHATLOG = 1012345;
	

	public final static int NONETWORK = -1;
	

	public static int SUCCESS = 0;
	

	public static boolean isNetworkConntected = false;
	

	public final static int TOAST_FIVE_SECONDS=5000;
	
	

	public final static int Max_Member_Num = 50;
	
	
	public static ArrayList<String> GroupChatNotificationList = new ArrayList<String>();
	
	public static final String _R = "r";
	public static final String _R_PALMTEMA = "r99999999";
	public static final String _R_PALMCALL = "r99920061";
 
	public static final CharSequence FILEMANAGER = "file";
	
	public static String comePage="comePage";
	

	

	public static OnKeyListener onKeyListener = new OnKeyListener()
	{
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
		{
			if (keyCode == KeyEvent.KEYCODE_BACK
					|| keyCode == KeyEvent.KEYCODE_SEARCH)
			{
				return true;
			}
			return false;
		}
	};
	
	/**baidu map key*/
	public static final String BAIDU_AK = "GRDeaI3oI2mMoynEH93R6ZpF";
	public static final String OTHERS = "Others";
	public static final String OTHER = "Other";
	public static final String OVERSEA = "Oversea";
/** forward invalid fid**/
	public static final String MSG_INVALID_FID = null;
	
	/**look around*/
	public static final int COMPLETE_SET_PHONE = 99;
	public static final int LOOKAROUND_COMPLETE_PROFILE = 100;
	public static final int MISS_NIGERIA_COMPLETE_PROFILE = 101;
	public static final CharSequence ASTERISK = "*";
	
	public static final String LABEL = "#";
	public static final String BLANK = " ";
	public static final String CR = "\n";
	
	public static final String COUNTRY_NIGERIA = "Nigeria";
	public static final String COUNTRY_KENYA = "Kenya";
	public static final int _234 = 234;  //add by zhh  Nigeria默认国码
	public static final String SHOW_GIF = "show_gif";
	public static final int MAX_SELECTED = 6;
	public static final String URI_SCHEME_WWW = "www";
	public static final String URI_SCHEME_HTTP = "http";
	public static final String URI_SCHEME_HTTPS = "https";
	
	public static final int LOGINFACEBOOK_SUCCESS  = 91;
	public static final int LOGINFACEBOOK_FAILURE = LOGINFACEBOOK_SUCCESS +1;
	public static final int FETCHPROFILE_SUCCESS = LOGINFACEBOOK_FAILURE+1;
	public static final int FETCHPROFILE_FAILURE = FETCHPROFILE_SUCCESS+1;
	public static final int SHAREFACEBOOK_SUCCESS = FETCHPROFILE_FAILURE+1;
	public static final int SHAREFACEBOOK_FAILURE = SHAREFACEBOOK_SUCCESS+1;
	public static final int LOGINFACEBOOK_CANCEL = SHAREFACEBOOK_FAILURE +1;
	public static final int RESETTILEPOSITION = LOGINFACEBOOK_CANCEL+1;
	public static final int SHOWFRESHSUCCESS = RESETTILEPOSITION+1;
	public static final int ONDDAYTIME= 24*60*60*1000;
	public static final int DIALOGACTIONONE = 0;
	public static final int DIALOGACTIONTWO = 1;
	public static final int BROADEXPIRE_DATE = 600 *1000;
//	public static final int BROADEXPIRE_DATE = 840*1000;
	public static final int TRENDINGDEFAULTNUMBER = 9;
	public static final int TRENDINGLOADMORNUMBER = 18;
	
	public static final int BROADCAST_NOTSHARE=0;//广播非分享的
	public static final int BROADCAST_SHARE_COMMENT=1;//广播分享  带评论
	public static final int BROADCAST_SHARE_NOCOMMENT=2;//广播分享的 无评论
	public static final int BROADCAST_SHARE_DELETE_FLAG=1;//广播原文被删除标志
	
	public static final int BROADCAST_PROFILE_NORMAL=1;//1 : normal use 
	public static final int BROADCAST_PROFILE_PA=2;//2: public account
	/**从词典里面热度前二十个里面取三个随机的*/
	public static final int SENDBROADCASTTAGFROMTAGS = 3;
	/**从词典里面热度前多少个数*/
	public static final int SENDBROADCASTTAGSFROMDICT = 20;
	/**从词典里面检索tag*/
	public static final int SEARCHTAGSFROMDICT = 6;
	/**tag length*/
	public static final int TAGLENGTH = 50;
	/**从词典里面查询判断是否是tag*/
	public static final String SEARCHTAGCONDITION="#([\\u4e00-\\u9fa5\\w\\-]){1,50}";
	/**从词典加载数据延时500ms,因为立即加载的话thread.handler 可能没有创建*/
	public static final int DELAYTIME_LOADTICT = 500;
	/**分享toast显示时间*/
	public static final int SHARETOASTTIME = 2500;
	/**是否follow*/
	public static final String ISFOLLOWED = "is_follow";
	public static final String IMAGESHARE_LABEL = "image/";
	/**动画时间*/
	public static final int DURATION_ANIMATION_100 =100;
	public static final int DURATION_ANIMATION_200=200;
	public static final int DURATION_ANIMATION_300=300;
	public static final int DURATION_ANIMATION_500=500;
	public static final int DURATION_ANIMATION_700=700;
	public static final long DURATION_STATIC_2S = 2*1000;
	/**palmcall id 请求超时时间*/
	public static final int CANCEL_LOADING_TIME = 30*1000;
	/***/
	/**显示动画距离判断*/
	public static final int DISTANCE_5 = 5;
	/**隐藏动画距离判断*/
	public static final int DISTANCE_30 = 30;
	/**跳转 url 测试服务器*/
	public static final String URL_WEBSKIPTEST="http://hastest.palm-chat.com/filterUrl";
	/**跳转 url 正式服务器*/
	public static final String URL_WEBSKIP="http://has.palm-chat.com/filterUrl";
	public static final int  FILTER_RUL_NORMAL=0;//一般过滤跳转
	public static final int  FILTER_RUL_BETWAY=1;//BetWay过滤调换
	/**betway跳转 url 正式服务器*/
	public static final String URL_BETWAY_FILTER="http://betway.palm-chat.com/filterUrl";
	/**betway跳转 url 测试服务器*/
	public static final String URL_BETWAY_FILTER_TEST="http://test-betway.palm-chat.com/filterUrl";

	/**测试服务器域名 免责声明*/
	public static final String DOMAIN_TERMS_TEST="http://terms-test";
	/**正式服务器域名 免责声明*/
	public static final String DOMAIN_TERMS_FORMAL="http://terms";
	/**测试服务器域名 feedback*/
	public static final String DOMAIN_FEEDBACK_TEST="https://helper-test.palm-chat.com";
	/**正式服务器域名 feedback*/
	public static final String DOMAIN_FEEDBACK_FORMAL="https://helper.palm-chat.com";
	/**modeule名 用来拼接域名 免责申明*/
	public static final String MODEULE_PALMCOIN="PalmCoin";
	/**modeule名 用来拼接域名 免责声明*/
	public static final String MODEULE_GENERIC="Generic";
	/**gif表情测试服务器*/
	public static final String GIF_FACE_TEST = "http://52.76.181.50:8010/";
	/**gif表情正式服务器*/
	public static final String GIF_FACE_FORMAL = "http://storeadmin.palm-chat.com/";
	/**palmcall 重登陆机制时间*/
	public static final long PALMCLAL_RELOGIN_DURATION_FAIL = 8;
	/**palmcall 立即登录*/
	public static final long PALMCLAL_LOGIN_DURATION = 0;
	/**trending 图片uri判断*/
	public static final String TRENGING_VIDEO_FLAG_URL = "video";

	public static final int PALMCALL_LIMITTIME = 1;
	
}
