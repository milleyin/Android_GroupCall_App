package com.afmobi.palmchat.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.StringUtil;
import com.core.AfProfileInfo;
import com.core.cache.CacheManager;
import com.google.gson.Gson;

/**
 * data Statistics--Ready
 * 
 * @author heguiming
 * 
 */
public class ReadyConfigXML {
	
	private static final boolean SAVE_FILE = false;
	
	public static final String BROADCAST_MESSAGE = "BroadcastMessage";
	
	private static final String TAG = ReadyConfigXML.class.getCanonicalName();
	// password security problem
	public static final String KEY_ALREAD_SET = "alread_set";
	
	public static final String LAST_UPDATE_TIME = "last_update_time";
	public static final long ONE_DAY = 24 * 60 * 60 * 1000;
	
	public static final String NOW_TIME = "now_time";
	
	/************************** 1.Required parameters start **************************/
	// Terminal System
	public static final String OSVER = "osver";
	// Terminal brand
	public static final String MVER = "mver";
	// IMEI
	public static final String IMEI = "imei";
	// MCC
	public static final String MCC = "mcc";
	// IMSI
	public static final String IMSI = "imsi";
	// Download channels
	public static final String DSRC = "dsrc";
	// palmchat version number
	public static final String VER = "ver";
	// afid
	public static final String AFID = "afid";
	// time
	public static final String TIME = "time";
	/************************** 1.Required parameters end **************************/
	
	
	/************************** 2.base data start **************************/
	// Use palmchat cumulative length of use
	public static final String T_DURATION = "t_duration";
	/************************** 2.base data end **************************/
	
	
	/************************** 3.chat data start **************************/
	// Picture number of messages
	public static final String S_M_PIC = "s_m_pic";
	// Text number of messages
	public static final String S_M_TEXT = "s_m_text";
	// Emotion number of messages
	public static final String S_M_EMOTION = "s_m_emotion";
	// Voice number of messages
	public static final String S_M_VOICE = "s_m_voice";
	// Card number of messages
	public static final String S_M_CARD = "s_m_card";
	// Whisper number of messages
	public static final String P_NUM = "p_num";
	// Group chat number of messages
	public static final String G_NUM = "g_num";
	// Chat Room number of messages
	public static final String C_NUM = "c_num";
	// Message forwarding number
	public static final String F_NUM = "f_num";
	// The number of initiated chat icon in the profile page click chat
	public static final String PF_T_CHAT = "pf_t_chat";
	// From the friends list or recent contact list into private chat interface number
	public static final String LIST_T_P = "list_t_p";
	/************************** 3.chat data end **************************/
	
	
	/************************** 4.add friend data start **************************/
	// From the list of features into look around times
	public static final String LEFT_T_LOOK = "left_t_look";
	// Look around from home to enter the number of hub
	public static final String HUB_T_LOOK = "hub_t_look";
	// By look around Add Friend Views
	public static final String LOOK_ADD_SUCC = "look_add_succ";
	// From the list of features into the shake frequency
	public static final String LEFT_T_SHAKE = "left_t_shake";
	// Views from the home hub into shake
	public static final String HUB_T_SHAKE = "hub_t_shake";
	// Shake number
	public static final String SHAKE_NUM = "shake_num";
	// By shake Add Friend
	public static final String SHAKE_ADD_SUCC = "shake_add_succ";
	// From the list of features into the chat room number
	public static final String LEFT_T_CR = "left_t_cr";
	// Enter the chat room from the home hub Views
	public static final String HUB_T_CR = "hub_t_cr";
	// Enter chat room user list page number
	public static final String ENTRY_CRLIST_NUM = "entry_crlist_num";
	// Add friends through chat room number
	public static final String CR_ADD_SUCC = "cr_add_succ";
	// Enter the number of group chat interface
	public static final String ENTRY_G = "entry_g";
	// Add Friend times through group chat
	public static final String GROUP_ADD_SUCC = "group_add_succ";
	// Enter the page number search by info
	public static final String ENTRY_SBI_NUM = "entry_sbi_num";
	// By search by info Add Friend Views
	public static final String SBI_ADD_SUCC = "sbi_add_succ";
	// Enter the number of people you may know of
	public static final String ENTRY_MAY_NUM = "entry_may_num";
	// Add people you may know by good Friends of the times
	public static final String MAY_ADD_SUCC = "may_add_succ";
	/************************** 4.add friend data end **************************/
	
	
	/************************** 5.Page views data start **************************/
	
	// a.base page
	// Enter the number of home pages
	public static final String ENTRY_HOME = "entry_home";
	// Chat list to enter the number of the page
	public static final String ENTRY_CL = "entry_cl";
	// Enter the page number feature list
	public static final String ENTRY_FUCL = "entry_fucl";
	// Enter the number of times friends list page
	public static final String ENTRY_FL = "entry_fl";
	// Friends of friends into the search times
	public static final String FL_T_SF = "fl_t_sf";
	// In the system message page click on the "+" button on the number of requests by friends
	public static final String NOTI_PLUS = "noti_plus";
	
	// b.View User Profile
	// By entering the user profile page look around times
	public static final String LOOK_T_PF = "look_t_pf";
	// By shake enter the user profile page number
	public static final String SHAKE_T_PF = "shake_t_pf";
	// By entering the chat room user profile page number
	public static final String CR_T_PF = "cr_t_pf";
	// Through group chat page number into the user profile
	public static final String GROUP_T_PF = "group_t_pf";
	// By search by info page number into the user profile
	public static final String SBI_T_PF = "sbi_t_pf";
	// By entering the number of people you may know the user's profile page
	public static final String MAY_T_PF = "may_t_pf";
	
	// c.Register
	// For the first time into the guide page number
	public static final String ENTRY_GUIDE = "entry_guide";
	// Enter the phone number registration number
	public static final String ENTRY_REG_P = "entry_reg_p";
	// Phone number using the number of successful registration
	public static final String REG_P_SUCC = "reg_p_succ";
	// Enter email sign-ups
	public static final String ENTRY_REG_E = "entry_reg_e";
	// Use the number of successful email registration
	public static final String REG_E_SUCC = "reg_e_succ";
	// Enter the complete profile page number
	public static final String ENTRY_CPF = "entry_cpf";
	// Click skip skip to complement the data page number
//	public static final String CPF_SKIP = "cpf_skip";
	
	// d.login
	// Landing page number into afid
	public static final String ENTRY_LG_A = "entry_lg_a";
	// Into the phone NO. Landing page number
	public static final String ENTRY_LG_P = "entry_lg_p";
	// Enter the number using the email landing page
	public static final String ENTRY_LG_E = "entry_lg_e";
	// Use afid successful landing times
	public static final String LG_A_SUCC = "lg_a_succ";
	// Use phone NO. Successful landing times
	public static final String LG_P_SUCC = "lg_p_succ";
	// The number of successful login using email
	public static final String LG_E_SUCC = "lg_e_succ";
	
	// e.Forgot Password
	// Retrieve Password page to enter the number of security issues
	public static final String ENTRY_S_F = "entry_s_f";
	// Number of successful retrieve password
	public static final String FIND_P_SUCC = "find_p_succ";
	// Forgot your password number to enter the phone number
	public static final String ENTRY_P_F = "entry_p_f";
	// Set the number of successful new password
	public static final String RESET_P_SUCC = "reset_p_succ";
	// Forgot your password into the Email address number
	public static final String ENTRY_E_F = "entry_e_f";
	// "Sent Mail" message box pops up times
	public static final String SM_NUM = "sm_num";
	
	// f.Group chat
	// Click on the number of group chat list new group chat new group
	public static final String GL_ADD_G = "gl_add_g";
	// Click on the top right of the interface to invite private chat group chat icon in the number of new
	public static final String INVITE_ADD_G = "invite_add_g";
	// Group successfully created a number of
	public static final String ADD_G_SUCC = "add_g_succ";
	// Enter the number of the page to see the group information
	public static final String SEE_G_INFO = "see_g_info";
	
	// g.ME-profile
	// Enter the page number ME
	public static final String ENTRY_ME = "entry_me";
	// Set the number of successes Avatar
	public static final String SET_H_SUCC = "set_h_succ";
	// The number of successful modifications background
	public static final String SET_B_SUCC = "set_b_succ";
	// Click on the photo wall view photos of the times
	public static final String ENTRY_PWALL = "entry_pwall";
	// Add photo wall photo number
	public static final String ADD_PWALL = "add_pwall";
	// Delete photo wall photo number
	public static final String DEL_PWALL = "del_pwall";
	// Save photos to the phone number of a wall photo
	public static final String SAVE_PWALL = "save_pwall";
	// Click on my profile page, enter the number of personal data
	public static final String ENTRY_MPF = "entry_mpf";
	// Modify personal data confirm the number of
	public static final String SET_MPF = "set_mpf";
	
	// h.ME-Setting
	// Click Settings to enter the number of pages setting
	public static final String ENTRY_SETTING = "entry_setting";
	// Upload address book number
	public static final String U_PB_SUCC = "u_pb_succ";
	// Number of downloads address book
	public static final String D_PB_SUCC = "d_pb_succ";
	// Enter the number of times my account page.
	public static final String ENTRY_MA = "entry_ma";
	// Set the number of successful security answer
	public static final String SET_SA_SUCC = "set_sa_succ";
	// The number of successful bind phone number
	public static final String SET_BP_SUCC = "set_bp_succ";
	// The number of successful authentication email
	public static final String SET_AE_SUCC = "set_ae_succ";
	// The number of times a user clicks on phone book friends list "invite" button
	public static final String PB_T_INVITE = "pb_t_invite";
	// profile add friend
	public static final String PF_ADD_SUCC = "pf_add_succ";
	/************************** 5.Page views data end **************************/
	
	
	// regist 
	public static final String W_PNUM_N = "w_pnum_n";
	public static final String W_PNUM_R = "w_pnum_r";
	public static final String ENTRY_SMS = "entry_sms";
	public static final String ENTRY_SMS_W = "entry_sms_w";
	public static final String SMSVER_SUCC = "smsver_succ";
	
	public static final String W_CODE_N = "w_code_n";
	public static final String CODE_ERROR = "code_error";
	public static final String W_EMAIL_N = "w_email_n";
	public static final String EMAIL_ERROR = "email_error";
	public static final String W_CODE_SUCC = "w_code_succ";
	public static final String REG_ERROR = "reg_error";
	public static final String NETWORK_UN = "network_un";
	
	// regist invite friends 
	public static final String ENTRY_REG_RE = "entry_reg_re";
	public static final String W_ID_CON = "w_id_con";
	public static final String W_ID_SUCC = "w_id_succ";
	public static final String W_ID_ERR = "w_id_err";
	public static final String RE_NO_ID = "re_no_id";
	public static final String RE_ID_UNNET = "re_id_unnet";
	
	public static final String ENTRY_NBS = "entry_nbs";
	public static final String ENTRY_FILT = "entry_filt";
	public static final String FILT_FM = "filt_fm";
	public static final String FILT_M = "filt_m";
	public static final String FILT_PT_ON = "filt_pt_on";
	public static final String ENTRY_BCM = "entry_bcm";
	public static final String ENTRY_SD_BCM = "entry_sd_bcm";
	public static final String SD_BCM_SUCC = "sd_bcm_succ";
	public static final String BCM_T_PF = "bcm_t_pf";
	public static final String ENTRY_LOOK_CPF = "entry_look_cpf";
	public static final String SET_LH_SUCC = "set_lh_succ";
	public static final String SET_LP_SUCC = "set_lp_succ";
	public static final String L_SET_LH = "l_set_lh";
	public static final String LSET_LH_SUCC = "lset_lh_succ";
	public static final String L_SET_LP = "l_set_lp";
	public static final String LSET_LP_SUCC = "lset_lp_succ";
	public static final String ENTRY_PO = "entry_po";
	public static final String SET_PN_OFF = "set_pn_off";
	public static final String SET_NBS_OFF = "set_nbs_off";
	public static final String ENTRY_MMPF = "entry_mmpf";
	public static final String MPF_SET_P = "mpf_set_p";
	public static final String MPF_T_GR = "mpf_t_gr";
	public static final String GR_T_PF = "gr_t_pf";
	public static final String MPF_SET_WUP = "mpf_set_wup";
	public static final String PF_T_HI = "pf_t_hi";
	public static final String PFSD_FL_SUCC = "pfsd_fl_succ";
	
	
	public static final String SEE_PN = "see_pn";
	public static final String ND_FL = "nd_fl";
	public static final String PNSD_FL = "pnsd_fl";
	public static final String PNSD_FL_SUCC = "pnsd_fl_succ";
	
//	public static final String FB_T_INVITE = "fb_t_invite";
	public static final String ENTRY_SOUND = "entry_sound";
	public static final String SOUND_SYSTEM = "sound_system";
	public static final String SOUND_DRUM = "sound_drum";
	public static final String SOUND_BLOOM = "sound_bloom";
	public static final String SOUND_BUBBLE = "sound_bubble";
	public static final String SOUND_GLASS = "sound_glass";
	
	public static final String SOUND_SHORTMSG = "sound_shortmsg";
	public static final String SOUND_SHORTMSG2 = "sound_shortmsg2";
	public static final String SOUND_TWEET = "sound_tweet";
	public static final String SOUND_BUBBLE2 = "sound_bubble2";
	public static final String SOUND_DROP = "sound_drop";
	public static final String SOUND_FRESH = "sound_fresh";
	public static final String STAR_T_PF = "star_t_pf";
	public static final String GET_GIFT = "get_gift";
	public static final String GET_GIFT_ERR = "get_gift_err";
	
	public static final String CODE_FAIL = "code_fail";
	public static final String SMS_FAIL = "sms_fail";
	public static final String E_NETWORK_UN = "e_network_un";
	
	
//	add 2014.5.23  
	public static final String W_EMAIL_R = "w_email_r";
	public static final String ENTRY_REG_I = "entry_reg_i";
	public static final String CHG_SEX = "chg_sex";
	public static final String CHG_DATE  = "chg_date";
	public static final String CHG_STATE = "chg_state";
	public static final String CPF_OK = "cpf_ok";
	
	public static final String SET_PT = "set_pt";
	public static final String CPF_SKIP = "cpf_skip";
	
	public static final String ENTRY_LG_FB = "entry_lg_fb";
	public static final String ENTRY_LG_WFB = "entry_lg_wfb";
	public static final String FB_OK = "fb_ok";
	public static final String WFB_OK = "wfb_ok";
	public static final String LG_FB_SUCC = "lg_fb_succ";
	public static final String LG_WFB_SUCC = "lg_wfb_succ";
	public static final String FB_NETWORK_UN = "fb_network_un";
	public static final String ENTRY_HOME_FB = "entry_home_fb";
	public static final String ENTRY_CPF_FB = "entry_cpf_fb";
	public static final String SELECT_COUNTR = "select_countr";
	public static final String W_PW_N = "w_pw_n";
	public static final String PW_NETWORK_UN = "pw_network_un";
	public static final String ENTRY_HOME_FBN = "entry_home_fbn";
	
	public static final String ENTRY_PBL = "entry_pbl";
	public static final String ENTRY_P_PBL = "entry_p_pbl";
	public static final String ENTRY_PBL_PF = "entry_pbl_pf";
	public static final String FOL_PBL = "fol_pbl";
	public static final String UNFOL_PBL = "unfol_pbl";
	public static final String SHA_PBL_SMS = "sha_pbl_sms";
	public static final String SHA_PBL_FB = "sha_pbl_fb";
	
	
	public static final String ENTRY_MISS = "entry_miss";
	public static final String ENTRY_MISS_CPF = "entry_miss_cpf";
	public static final String SET_MH_SUCC = "set_mh_succ";
	public static final String SET_MP_SUCC = "set_mp_succ";
	public static final String SET_MPN_OFF = "set_mpn_off";
	public static final String JOINED_MISS = "joined_miss";
	public static final String SHARE_MISS_FB = "share_miss_fb";
	public static final String GET_MGIFT = "get_mgift";
	public static final String GET_MGIFT_ERR = "get_mgift_err";
	public static final String MISS_T_PF = "miss_t_pf";
	public static final String MISSTOP_T_PF = "misstop_t_pf";
	public static final String MISSNEW_T_PF = "missnew_t_pf";
	public static final String ENTRY_MINI = "entry_mini";
	public static final String MISS_MORE = "miss_more";
	
	public static final String LOOK_MORE = "look_more";
	
	public static final String LGO_EXIT = "lgo_exit";
	public static final String LGO_LG = "lgo_lg";
	public static final String FB_T_INVITE = "fb_t_invite";
	
	
	
//	add 2014.7.17 , Store, Account Center
	public static final String ENTRY_STO = "entry_sto";
	public static final String EM_T_STO = "em_t_sto";
	public static final String STO_ME = "sto_em";
	public static final String EM_DETAIL = "em_detail";
	public static final String BUY_EM = "buy_em";
	public static final String DL_FREE_EM = "dl_free_em";
	public static final String BUY_EM_SUCC = "buy_em_succ";
	
	public static final String BUY_EM_RCG = "buy_em_rcg";
	public static final String EM_RCG_SMS = "em_rcg_sms";
	public static final String EM_RCG_CARD = "em_rcg_card";
	public static final String BUY_EM_SMS = "buy_em_sms";
	public static final String BUY_EM_CARD = "buy_em_card";
	public static final String BUY_EM_FAIL = "buy_em_fail";
	
	public static final String DL_EM_SUCC = "dl_em_succ";
	public static final String DL_EM_FAIL = "dl_em_fail";
	public static final String ENTRY_ACC = "entry_acc";
	public static final String ENTRY_RR = "entry_rr";
	public static final String ENTRY_RCG = "entry_rcg";
	public static final String RCG_SMS = "rcg_sms";
	
	public static final String RCG_CARD = "rcg_card";
	public static final String RCG_SMS_SUCC = "rcg_sms_succ";
	public static final String RCG_SMS_FAIL = "rcg_sms_fail";
	public static final String RCG_CARD_SUCC = "rcg_card_succ";
	public static final String RCG_CARD_CANCEL = "rcg_card_cancel";
	public static final String RCG_CARD_FAIL = "rcg_card_fail";
	
//	add 2014.8.15 broadcast
	public static final String ENTRY_MYBCM = "entry_mybcm";
	public static final String SD_BCM_PIC = "sd_bcm_pic";
	public static final String SD_BCM_VOICE = "sd_bcm_voice";
	public static final String SD_BCM_TEXT = "sd_bcm_text";
	
//	add 2014.11.15 store --GTFT
	public static final String STO_GIFT = "sto_gift";
	public static final String GIFT_DEETAIL = "gift_detail";
	public static final String BUY_GIFT = "buy_gift";
	public static final String DL_GIFT = "dl_gift";
	public static final String DL_FREE_GIFT = "dl_free_gift";
	public static final String BUY_GIFT_SUCC = "buy_gift_succ";
	public static final String BUY_GIFT_RCG = "buy_gift_rcg";
	public static final String GIFT_RCG_SMS = "gift_rcg_sms";
	public static final String GIFT_RCG_CARD = "gift_rcg_card";
	public static final String BUY_GIFT_SMS = "buy_gift_sms";
	public static final String BUY_GIFT_CARD = "buy_gift_card";
	public static final String BUY_GIFT_FAIL = "buy_gift_fail";
	public static final String DL_GIFT_SUCC = "dl_gift_succ";
	public static final String DL_GIFT_FAIL = "dl_gift_fail";
	public static final String DL_EM_CANCEL = "dl_em_cancel";
	public static final String PF_T_SDG = "pf_t_sdg";
	public static final String SDG_SUCC = "sdg_succ";
	public static final String SDG_SUCC_COIN = "sdg_succ_coin";
	public static final String SDG_T_RCG = "sdg_t_rcg";
	public static final String SDG_SUCC_SMS = "sdg_succ_sms";
	public static final String SDG_SUCC_CARD = "sdg_succ_card";
	public static final String SDG_CANCEL = "sdg_cancel";
	
	
//	add 2014.11.16 chats
	public static final String ENTRY_GLIST = "entry_glist";
	public static final String ENTRY_FPLL = "entry_fpll";
	public static final String ENTRY_FOLLER = "entry_foller";
	public static final String CONTACT_T_P = "contact_t_p";
	public static final String CT_ADD_SUCC = "ct_add_succ";
	
//	add 2014.11.16 MainTab
	public static final String ENTRY_XEP = "entry_xep";
	public static final String CP_NUM = "cp_num";

	//	add 2014.11.16 profile
	public static final String PF_T_REPORT = "pf_t_report";
	public static final String PF_T_FOLLOW = "pf_t_follow";
	public static final String PF_T_BCM = "pf_t_bcm";
	
	//	add 2014.11.16 LookUserInfo
	public static final String CT_T_PF = "ct_t_pf";
	public static final String ID_T_PF = "id_t_pf";
	public static final String CONTACT_T_PF = "contact_t_pf";
	
	
//	add 2015.3.15 (5.0)
	public static final String VSTAR_T_PF = "vstar_t_pf";
	
	public static final String ENTRY_LG_GG = "entry_lg_gg";
	public static final String ENTRY_LG_WGG = "entry_lg_wgg";
	public static final String GG_OK = "gg_ok";
	public static final String WGG_OK = "wgg_ok";
	public static final String LG_GG_SUCC = "lg_gg_succ";
	public static final String LG_WGG_SUCC = "lg_wgg_succ";
	public static final String GG_NETWORK_UN = "gg_network_un";
	public static final String ENTRY_HOME_GG = "entry_home_gg";
	public static final String ENTRY_CPF_GG = "entry_cpf_gg";
	public static final String ENTRY_HOME_GGN = "entry_home_ggn";
	
	public static final String ENTRY_LG_TW = "entry_lg_tw";
	public static final String ENTRY_LG_WTW = "entry_lg_wtw";
	public static final String TW_OK = "tw_ok";
	public static final String WTW_OK = "wtw_ok";
	public static final String LG_TW_SUCC = "lg_tw_succ";
	public static final String LG_WTW_SUCC = "lg_wtw_succ";
	public static final String TW_NETWORK_UN = "tw_network_un";
	public static final String ENTRY_HOME_TW = "entry_home_tw";
	public static final String ENTRY_CPF_TW = "entry_cpf_tw";
	public static final String ENTRY_HOME_TWN = "entry_home_twn";
	
	public static final String LIKE_BCM = "like_bcm";
	public static final String COM_BCM = "com_bcm";
	public static final String REPORT_BCM = "report_bcm";
	public static final String DEL_BCM = "del_bcm";
	public static final String DEL_BCM_SUCC = "del_bcm_succ";
	public static final String DEL_BCMCOM = "del_bcmcom";
	
	
	public static final String CODE_NO_PN = "code_no_pn";
	public static final String CODE_NO_PW = "code_no_pw";
	public static final String PNUM_REGED = "pnum_reged";
	public static final String REG_PNUM = "reg_pnum";
	public static final String REG_NO_PN = "reg_no_pn";
	public static final String REG_NO_CODE = "reg_no_code";
	public static final String REG_NO_PW = "reg_no_pw";
	
	public static final String ENTRY_FAR = "entry_far";
	public static final String FAR_T_PF = "far_t_pf";
	public static final String LIKE_BCM_PF = "like_bcm_pf";
	public static final String COM_BCM_PF = "com_bcm_pf";
	public static final String REPORT_BCM_PF = "report_bcm_pf";
	public static final String FAR_T_SEARCH = "far_t_search";
	public static final String FAR_T_LIST = "far_t_list";
	public static final String FAR_T_BCM = "far_t_bcm";
	public static final String LIKE_BCM_FAR = "like_bcm_far";
	public static final String COM_BCM_FAR = "com_bcm_far";
	public static final String REPORT_BCM_FAR = "report_bcm_far";
	
	
	//gtf 2015-5-21
	//Reg
	public static final String PNUM_INVA = "pnum_inva";
	public static final String REG_CRE_PW = "reg_cre_pw";
	public static final String REG_IPT_NAME = "reg_ipt_name";
	public static final String REG_IPT_BDATE = "reg_ipt_bdate";
	public static final String REG_SEL_GDER = "reg_sel_gder";
	public static final String SEL_FEMALE = "sel_female";
	public static final String SEL_MALE = "sel_male";
	public static final String PNUM_CON = "pnum_con";
	public static final String SMS_CODE = "sms_code";
	public static final String PNUM_OVER = "pnum_over";
	public static final String CODE_INVA = "code_inva";
	public static final String CODE_OVER = "code_over";
	public static final String GET_CODE = "get_code";
	public static final String NEW_CODE = "new_code";
	public static final String EMAIL_NO = "email_no";
	public static final String EMAIL_CRE_PW = "email_cre_pw";
	public static final String EMAIL_IPT_NAME = "email_ipt_name";
	public static final String EMAIL_IPT_BDATE = "email_ipt_bdate";
	public static final String EMAIL_SEL_GDER = "email_sel_gder";
	public static final String EMAIL_SEL_FEMALE = "email_sel_female";
	public static final String EMAIL_SEL_MALE = "email_sel_male";
	
	//Chats
	public static final String CLI_GBD = "cli_bgd";
	public static final String SEL_NO_DEFAU = "sel_no_defau";
	public static final String SEL_DEFAU = "sel_defau";
	
//	follow
	public static final String HM_T_FOLLOW = "hm_t_follow";
	public static final String BC_T_FOLLOW = "bc_t_follow";
	public static final String CH_T_FOLLOW = "ch_t_follow";
	public static final String CONT_T_FOLLOW = "cont_t_follow";
	public static final String GPCHAT_T_FOLLOW = "gpchat_t_follow";
	//discover
	public static final String EN_BC_DISC = "en_bc_disc";
	public static final String EN_BC_HOT = "en_bc_hot";
	public static final String EN_BC_TRD = "en_bc_trd";
	public static final String EN_BC_CITY = "en_bc_city";
	public static final String CITY_BC_PF = "city_bc_pf";
	public static final String CITY_PF_REGION = "city_pf_region";

	
	
	/**
	 * SharedPreference
	 */
	private SharedPreferences share;
	private SharedPreferences noLoginShare;
	private Editor editor;
	private Editor noLoginEditor;
	
	/**
	 * SharedPreferences
	 */
	private static final String CONFIG_URL = "ready";
	
	private static final String NO_LOGIN_CONFIG_URL = "nologin";
	
	/**
	 * Read and write permissions
	 */
	private final static int MODE = Context.MODE_PRIVATE;
    
    public static final String YYYY_MM_DD = "yyyyMMdd";
	
	public static final String KEY_RECEPTION = "Key_Reception";
	 
//	public static final String ANDROID_SAM="android_sam";//因三星市场政策问题 必须集成任何一个三星API 所以不上了
//	public static final String ANDROID_ALCATEL="android_alcatel";//接到通知 不上线了

	public static final String ANDROID_YZ="android_yz";
	public static final String ANDROID_GP="android_gp";
	public static final String ANDROID_PALMPLAY="android_palmplay";
	public static final String ANDROID_OP="android_op";
	public static final String ANDROID_CARLCARE="android_carlcare";
	public static final String ANDROID_PALMWEBSITE="android_palmwebsite";//官网的渠道名为：android_palmwebsite   (小写)
	public static final String ANDROID_UC="android_uc";
	public static final String R_DSRC = ANDROID_YZ;//渠道号
	
	public ReadyConfigXML() {
		AfProfileInfo info = CacheManager.getInstance().getMyProfile();
		share = PalmchatApp.getApplication().getSharedPreferences(CONFIG_URL + info.afId, MODE);
		noLoginShare = PalmchatApp.getApplication().getSharedPreferences(NO_LOGIN_CONFIG_URL, MODE);
		
		editor = share.edit();
		noLoginEditor = noLoginShare.edit();
	}
	
	public void saveBoolean(String key, Boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public boolean getBoolean(String key) {
		return share.getBoolean(key, false);
	}
	
	public void saveReadyInt(final String key) {
		
		CacheManager.getInstance().getThreadPoolInstance().executeReady(new Thread() {
			public void run() {
				int value = share.getInt(key, 0);
				PalmchatLogUtils.e("Ready key", "saveReadyInt thread id " + Thread.currentThread().getId() +  " key = "+ key + " value = " + value);
				editor.putInt(key, value + 1);
				editor.commit();
				saveFile(getLoginSuccessHttpJsonStr(), true);
				saveFile(getNoLoginHttpJsonStr(), false);
				 
			}
		});
				
	}
	
	public void saveNoLoginReadyInt(final String key) {
		CacheManager.getInstance().getThreadPoolInstance().executeReady(new Thread() {
			public void run() {
		int value = noLoginShare.getInt(key, 0);
		PalmchatLogUtils.e("Ready key", "saveNoLoginReadyInt thread id " + Thread.currentThread().getId() +  " key = "+ key + " value = " + value);
		noLoginEditor.putInt(key, value + 1);
		noLoginEditor.commit();
		saveFile(getLoginSuccessHttpJsonStr(), true);
		saveFile(getNoLoginHttpJsonStr(), false);
		
			}});
	}
	
	public void saveReadyInt(String key, int i) {
		editor.putInt(key, i);
//		editor.commit();
		
//		saveFile(getLoginSuccessHttpJsonStr(), true);
//		saveFile(getNoLoginHttpJsonStr(), false);
	}
	
	public void saveLong(String key, long l) {
		editor.putLong(key, l);
		editor.commit();
//		saveFile(getLoginSuccessHttpJsonStr(), true);
//		saveFile(getNoLoginHttpJsonStr(), false);
	}
	
	public void saveReadyUseTime() {
		long backstageTime = System.currentTimeMillis();
		long receptionTime = share.getLong(KEY_RECEPTION, 0);
		long useTime = share.getLong(T_DURATION, 0);
		
		PalmchatLogUtils.e(TAG, "----saveReadyUseTime:receptionTime--" + (backstageTime - receptionTime));
		PalmchatLogUtils.e(TAG, "----saveReadyUseTime:useTime--" + useTime);
		
		if (receptionTime != 0L) {
			long nowUseTime = backstageTime - receptionTime;
			saveReadyTime();
			
			editor.putLong(T_DURATION, useTime + nowUseTime);
			editor.commit();
			
			saveFile(getLoginSuccessHttpJsonStr(), true);
			saveFile(getNoLoginHttpJsonStr(), false);
		}
	}
	
	public void saveReadyTime() {
		editor.putLong(KEY_RECEPTION, System.currentTimeMillis());
		editor.commit();
		saveFile(getLoginSuccessHttpJsonStr(), true);
		saveFile(getNoLoginHttpJsonStr(), false);
	}
	
	private String getTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
		String value = format.format(date);
		return value;
	}
	
	public long getLong(String key) {
		return share.getLong(key, 0);
	}
	
	public int getInt(String key) {
		return share.getInt(key, 0);
	}
	
	public int getNoLoginInt(String key) {
		return noLoginShare.getInt(key, 0);
	}
	
	public String getLoginSuccessHttpJsonStr() {
		LoginSuccessStatisticBean statistic = new LoginSuccessStatisticBean();
		Gson gson = new Gson();
		String gsonStr = gson.toJson(statistic);
		
		return gsonStr;
	}
	
	public String getNoLoginHttpJsonStr() {
		NoLoginStatisticBean statistic = new NoLoginStatisticBean();
		Gson gson = new Gson();
		String gsonStr = gson.toJson(statistic);
		return gsonStr;
	}
	
	/**
	　　* 保存文件
	　　* @param toSaveString
	　　* @param filePath
	　　*/
	private void saveFile(String toSaveString, boolean isLogin) {
		if (SAVE_FILE) {
			try {
				String saveName = null;
				if (isLogin) {
					String afid = CacheManager.getInstance().getMyProfile().afId;
					if (!StringUtil.isNullOrEmpty(afid)) {
						saveName = Environment.getExternalStorageDirectory()
								.getPath() + "/PalmchatReadyLogin" + afid + ".txt";
					}
				} else {
					saveName = Environment.getExternalStorageDirectory().getPath()
							+ "/PalmchatReadyNoLogin.txt";
				}
				File saveFile = new File(saveName);
				if (saveFile.exists()) {
					saveFile.delete();
				}
				saveFile.createNewFile();
				FileOutputStream outStream = new FileOutputStream(saveFile);
				outStream.write(toSaveString.getBytes());
				outStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void saveUpdateTime() {
		editor.putLong(LAST_UPDATE_TIME, System.currentTimeMillis());
		editor.commit();
		saveFile(getLoginSuccessHttpJsonStr(), true);
		saveFile(getNoLoginHttpJsonStr(), false);
	}
	
	public boolean canUpdate() {
		long updateTime = getLong(LAST_UPDATE_TIME);
		long nowTime = System.currentTimeMillis();
		
		if ((nowTime - updateTime) > ONE_DAY) {
			return true;
		}
		return false;
	}
	
	public void clearLoginSuccessData() {
		
		CacheManager.getInstance().getThreadPoolInstance().executeReady(new Thread() {
			public void run() {
		
		saveLong(KEY_RECEPTION, 0);
		
		saveLong(T_DURATION, 0);
		long useTime = share.getLong(T_DURATION, 0);
		PalmchatLogUtils.e(TAG, "--clearLoginSuccessData--" + useTime);
		
		
		saveReadyInt(S_M_PIC, 0);
		saveReadyInt(S_M_TEXT, 0);
		saveReadyInt(S_M_EMOTION, 0);
		
		saveReadyInt(S_M_VOICE, 0);
		saveReadyInt(S_M_CARD, 0);
		saveReadyInt(P_NUM, 0);
		saveReadyInt(G_NUM, 0);
		saveReadyInt(C_NUM, 0);
		saveReadyInt(F_NUM, 0);
		saveReadyInt(PF_T_CHAT, 0);
		saveReadyInt(LIST_T_P, 0);
		saveReadyInt(LEFT_T_LOOK, 0);
		saveReadyInt(HUB_T_LOOK, 0);
		saveReadyInt(LOOK_ADD_SUCC, 0);
		saveReadyInt(LEFT_T_SHAKE, 0);
		saveReadyInt(HUB_T_SHAKE, 0);
		saveReadyInt(SHAKE_NUM, 0);
		saveReadyInt(SHAKE_ADD_SUCC, 0);
		saveReadyInt(LEFT_T_CR, 0);
		saveReadyInt(HUB_T_CR, 0);
		saveReadyInt(ENTRY_CRLIST_NUM, 0);
		saveReadyInt(CR_ADD_SUCC, 0);
		saveReadyInt(ENTRY_G, 0);
		saveReadyInt(GROUP_ADD_SUCC, 0);
		saveReadyInt(ENTRY_SBI_NUM, 0);
		saveReadyInt(SBI_ADD_SUCC, 0);
		saveReadyInt(ENTRY_MAY_NUM, 0);
		saveReadyInt(MAY_ADD_SUCC, 0);
		saveReadyInt(ENTRY_HOME, 0);
		saveReadyInt(ENTRY_CL, 0);
		saveReadyInt(ENTRY_FUCL, 0);
		saveReadyInt(ENTRY_FL, 0);
		saveReadyInt(FL_T_SF, 0);
		saveReadyInt(NOTI_PLUS, 0);
		saveReadyInt(LOOK_T_PF, 0);
		saveReadyInt(SHAKE_T_PF, 0);
		saveReadyInt(CR_T_PF, 0);
		saveReadyInt(GROUP_T_PF, 0);
		saveReadyInt(SBI_T_PF, 0);
		saveReadyInt(MAY_T_PF, 0);
		saveReadyInt(REG_P_SUCC, 0);
		saveReadyInt(ENTRY_CPF, 0);
		saveReadyInt(CPF_SKIP, 0);
		saveReadyInt(LG_A_SUCC, 0);
		saveReadyInt(LG_P_SUCC, 0);
		saveReadyInt(LG_E_SUCC, 0);
		saveReadyInt(ENTRY_S_F, 0);
		saveReadyInt(FIND_P_SUCC, 0);
		saveReadyInt(GL_ADD_G, 0);
		saveReadyInt(INVITE_ADD_G, 0);
		saveReadyInt(ADD_G_SUCC, 0);
		saveReadyInt(SEE_G_INFO, 0);
		saveReadyInt(ENTRY_ME, 0);
		saveReadyInt(SET_H_SUCC, 0);
		saveReadyInt(SET_B_SUCC, 0);
		saveReadyInt(ENTRY_PWALL, 0);
		saveReadyInt(ADD_PWALL, 0);
		saveReadyInt(DEL_PWALL, 0);
		saveReadyInt(SAVE_PWALL, 0);
		saveReadyInt(ENTRY_MPF, 0);
		saveReadyInt(SET_MPF, 0);
		saveReadyInt(ENTRY_SETTING, 0);
		saveReadyInt(U_PB_SUCC, 0);
		saveReadyInt(D_PB_SUCC, 0);
		saveReadyInt(ENTRY_MA, 0);
		saveReadyInt(SET_SA_SUCC, 0);
		saveReadyInt(SET_BP_SUCC, 0);
		saveReadyInt(SET_AE_SUCC, 0);
		saveReadyInt(PB_T_INVITE, 0);
		saveReadyInt(PF_ADD_SUCC, 0);
		
		// new add
		saveReadyInt(ENTRY_REG_RE, 0);
		saveReadyInt(W_ID_CON, 0);
		saveReadyInt(W_ID_SUCC, 0);
		saveReadyInt(W_ID_ERR, 0);
		saveReadyInt(RE_NO_ID, 0);
		saveReadyInt(RE_ID_UNNET, 0);
		saveReadyInt(ENTRY_NBS, 0);
		saveReadyInt(ENTRY_FILT, 0);
		saveReadyInt(FILT_FM, 0);
		saveReadyInt(FILT_M, 0);
		saveReadyInt(FILT_PT_ON, 0);
		saveReadyInt(ENTRY_BCM, 0);
		saveReadyInt(ENTRY_SD_BCM, 0);
		saveReadyInt(SD_BCM_SUCC, 0);
		saveReadyInt(BCM_T_PF, 0);
		saveReadyInt(ENTRY_LOOK_CPF, 0);
		saveReadyInt(SET_LH_SUCC, 0);
		saveReadyInt(SET_LP_SUCC, 0);
		saveReadyInt(L_SET_LH, 0);
		saveReadyInt(LSET_LH_SUCC, 0);
		saveReadyInt(L_SET_LP, 0);
		saveReadyInt(LSET_LP_SUCC, 0);
		saveReadyInt(ENTRY_PO, 0);
		saveReadyInt(SET_PN_OFF, 0);
		saveReadyInt(SET_NBS_OFF, 0);
		saveReadyInt(ENTRY_MMPF, 0);
		saveReadyInt(MPF_SET_P, 0);
		saveReadyInt(MPF_T_GR, 0);
		saveReadyInt(GR_T_PF, 0);
		saveReadyInt(MPF_SET_WUP, 0);
		saveReadyInt(PF_T_HI, 0);
		saveReadyInt(PF_T_SDG, 0);
		saveReadyInt(PFSD_FL_SUCC, 0);
		saveReadyInt(SEE_PN, 0);
		saveReadyInt(ND_FL, 0);
		saveReadyInt(PNSD_FL, 0);
		saveReadyInt(PNSD_FL_SUCC, 0);
//		saveReadyInt(FB_T_INVITE, 0);
		saveReadyInt(ENTRY_SOUND, 0);
		saveReadyInt(SOUND_SYSTEM, 0);
		saveReadyInt(SOUND_DRUM, 0);
		saveReadyInt(SOUND_BLOOM, 0);
		saveReadyInt(SOUND_BUBBLE, 0);
		saveReadyInt(SOUND_GLASS, 0);
		saveReadyInt(SOUND_SHORTMSG, 0);
		saveReadyInt(SOUND_SHORTMSG2, 0);
		saveReadyInt(SOUND_TWEET, 0);
		saveReadyInt(SOUND_BUBBLE2, 0);
		saveReadyInt(SOUND_DROP, 0);
		saveReadyInt(SOUND_FRESH, 0);
		saveReadyInt(STAR_T_PF, 0);
		
		
		
//		add 2014.5.23
		saveReadyInt(CHG_SEX, 0);
		saveReadyInt(CHG_DATE, 0);
		saveReadyInt(CHG_STATE, 0);
		saveReadyInt(CPF_OK, 0);
		saveReadyInt(SET_PT, 0);
		saveReadyInt(CPF_SKIP, 0);
		saveReadyInt(ENTRY_HOME_FB, 0);
		saveReadyInt(ENTRY_HOME_FBN, 0);
		saveReadyInt(ENTRY_PBL, 0);
		saveReadyInt(ENTRY_P_PBL, 0);
		saveReadyInt(ENTRY_PBL_PF, 0);
		saveReadyInt(FOL_PBL, 0);
		saveReadyInt(UNFOL_PBL, 0);
		saveReadyInt(SHA_PBL_SMS, 0);
		saveReadyInt(SHA_PBL_FB, 0);
		saveReadyInt(ENTRY_MISS, 0);
		saveReadyInt(ENTRY_MISS_CPF, 0);
		saveReadyInt(SET_MH_SUCC, 0);
		saveReadyInt(SET_MP_SUCC, 0);
		saveReadyInt(SET_MPN_OFF, 0);
		saveReadyInt(JOINED_MISS, 0);
		saveReadyInt(SHARE_MISS_FB, 0);
		saveReadyInt(GET_MGIFT, 0);
		saveReadyInt(GET_MGIFT_ERR, 0);
		saveReadyInt(MISS_T_PF, 0);
		saveReadyInt(MISSTOP_T_PF, 0);
		saveReadyInt(MISSNEW_T_PF, 0);
		saveReadyInt(ENTRY_MINI, 0);
		saveReadyInt(MISS_MORE, 0);
		saveReadyInt(LOOK_MORE, 0);
		saveReadyInt(LGO_EXIT, 0);
		saveReadyInt(FB_T_INVITE, 0);
		
//		add 2014.7.17 store, account center
		saveReadyInt(ENTRY_STO, 0);
		saveReadyInt(EM_T_STO, 0);
		saveReadyInt(STO_ME, 0);
		saveReadyInt(EM_DETAIL, 0);
		saveReadyInt(BUY_EM, 0);
		saveReadyInt(DL_FREE_EM, 0);
		saveReadyInt(BUY_EM_SUCC, 0);
		
		saveReadyInt(BUY_EM_RCG, 0);
		saveReadyInt(EM_RCG_SMS, 0);
		saveReadyInt(EM_RCG_CARD, 0);
		saveReadyInt(BUY_EM_SMS, 0);
		saveReadyInt(BUY_EM_CARD, 0);
		saveReadyInt(BUY_EM_FAIL, 0);
		
		saveReadyInt(DL_EM_SUCC, 0);
		saveReadyInt(DL_EM_FAIL, 0);
		saveReadyInt(ENTRY_ACC, 0);
		saveReadyInt(ENTRY_RR, 0);
		saveReadyInt(ENTRY_RCG, 0);
		saveReadyInt(RCG_SMS, 0);
		
		saveReadyInt(RCG_CARD, 0);
		saveReadyInt(RCG_SMS_SUCC, 0);
		saveReadyInt(RCG_SMS_FAIL, 0);
		saveReadyInt(RCG_CARD_SUCC, 0);
		saveReadyInt(RCG_CARD_CANCEL, 0);
		saveReadyInt(RCG_CARD_FAIL, 0);
		
		saveReadyInt(ENTRY_MYBCM, 0);
		
		saveReadyInt(SD_BCM_PIC, 0);
		saveReadyInt(SD_BCM_VOICE, 0);
		saveReadyInt(SD_BCM_TEXT, 0);
		//ADD 2014-11-16 
		saveReadyInt(STO_GIFT, 0);
		saveReadyInt(GIFT_DEETAIL, 0);
		saveReadyInt(BUY_GIFT, 0);
		saveReadyInt(DL_GIFT, 0);
		saveReadyInt(DL_FREE_GIFT, 0);
		saveReadyInt(BUY_GIFT_SUCC, 0);
		saveReadyInt(BUY_GIFT_RCG, 0);
		saveReadyInt(GIFT_RCG_SMS, 0);
		saveReadyInt(GIFT_RCG_CARD, 0);
		saveReadyInt(BUY_GIFT_SMS, 0);
		saveReadyInt(BUY_GIFT_CARD, 0);
		saveReadyInt(BUY_GIFT_FAIL, 0);
		saveReadyInt(DL_GIFT_SUCC, 0);
		saveReadyInt(DL_GIFT_FAIL, 0);
		saveReadyInt(DL_EM_CANCEL, 0);
		saveReadyInt(SDG_SUCC, 0);
		saveReadyInt(SDG_SUCC_COIN, 0);
		saveReadyInt(SDG_T_RCG, 0);
		saveReadyInt(SDG_SUCC_SMS, 0);
		saveReadyInt(SDG_SUCC_CARD, 0);
		saveReadyInt(SDG_CANCEL, 0);
		
		saveReadyInt(ENTRY_GLIST, 0);
		saveReadyInt(ENTRY_FPLL, 0);
		saveReadyInt(ENTRY_FOLLER, 0);
		saveReadyInt(CONTACT_T_P, 0);
		saveReadyInt(CT_ADD_SUCC, 0);
		
		saveReadyInt(ENTRY_XEP, 0);
		saveReadyInt(CP_NUM, 0);
		
		saveReadyInt(PF_T_REPORT, 0);
		saveReadyInt(PF_T_FOLLOW, 0);
		saveReadyInt(PF_T_BCM, 0);
		
		saveReadyInt(CT_T_PF, 0);
		saveReadyInt(ID_T_PF, 0);
		saveReadyInt(CONTACT_T_PF, 0);
		
//		add 2014.3.15 (5.0)
		saveReadyInt(VSTAR_T_PF, 0);
		saveReadyInt(ENTRY_LG_GG, 0);
		saveReadyInt(ENTRY_LG_WGG, 0);
		saveReadyInt(GG_OK, 0);
		saveReadyInt(WGG_OK, 0);
		saveReadyInt(LG_GG_SUCC, 0);
		saveReadyInt(LG_WGG_SUCC, 0);
		saveReadyInt(GG_NETWORK_UN, 0);
		saveReadyInt(ENTRY_HOME_GG, 0);
		saveReadyInt(ENTRY_CPF_GG, 0);
		saveReadyInt(ENTRY_HOME_GGN, 0);
		saveReadyInt(ENTRY_LG_TW, 0);
		saveReadyInt(ENTRY_LG_WTW, 0);
		saveReadyInt(TW_OK, 0);
		saveReadyInt(WTW_OK, 0);
		saveReadyInt(LG_TW_SUCC, 0);
		saveReadyInt(LG_WTW_SUCC, 0);
		saveReadyInt(TW_NETWORK_UN, 0);
		saveReadyInt(ENTRY_HOME_TW, 0);
		saveReadyInt(ENTRY_CPF_TW, 0);
		saveReadyInt(ENTRY_HOME_TWN, 0);
		
		saveReadyInt(LIKE_BCM, 0);
		saveReadyInt(COM_BCM, 0);
		saveReadyInt(REPORT_BCM, 0);
		saveReadyInt(DEL_BCM, 0);
		saveReadyInt(DEL_BCM_SUCC, 0);
		
		saveReadyInt(DEL_BCMCOM, 0);
		
		saveReadyInt(CODE_NO_PN, 0);
		saveReadyInt(CODE_NO_PW, 0);
		saveReadyInt(PNUM_REGED, 0);
		saveReadyInt(REG_PNUM, 0);
		saveReadyInt(REG_NO_PN, 0);
		saveReadyInt(REG_NO_CODE, 0);
		saveReadyInt(REG_NO_PW, 0);
		saveReadyInt(ENTRY_FAR, 0);
		saveReadyInt(FAR_T_PF, 0);
		saveReadyInt(LIKE_BCM_PF, 0);
		saveReadyInt(COM_BCM_PF, 0);
		saveReadyInt(REPORT_BCM_PF, 0);
		saveReadyInt(FAR_T_SEARCH, 0);
		saveReadyInt(FAR_T_LIST, 0);
		saveReadyInt(FAR_T_BCM, 0);
		saveReadyInt(LIKE_BCM_FAR, 0);
		saveReadyInt(COM_BCM_FAR, 0);
		saveReadyInt(REPORT_BCM_FAR, 0);
		
		// add 2015-5-21
		//reg
		saveReadyInt(PNUM_INVA, 0);
		saveReadyInt(REG_CRE_PW, 0);
		saveReadyInt(REG_IPT_NAME, 0);
		saveReadyInt(REG_IPT_BDATE, 0);
		saveReadyInt(REG_SEL_GDER, 0);
		saveReadyInt(SEL_FEMALE, 0);
		saveReadyInt(SEL_MALE, 0);
		saveReadyInt(PNUM_CON, 0);
		saveReadyInt(SMS_CODE, 0);
		saveReadyInt(PNUM_OVER, 0);
		saveReadyInt(CODE_INVA, 0);
		saveReadyInt(CODE_OVER, 0);
		saveReadyInt(GET_CODE, 0);
		saveReadyInt(NEW_CODE, 0);
		saveReadyInt(EMAIL_NO, 0);
		saveReadyInt(EMAIL_CRE_PW, 0);
		saveReadyInt(EMAIL_IPT_NAME, 0);
		saveReadyInt(EMAIL_IPT_BDATE, 0);
		saveReadyInt(EMAIL_SEL_GDER, 0);
		saveReadyInt(EMAIL_SEL_FEMALE, 0);
		saveReadyInt(EMAIL_SEL_MALE, 0);
		//chats
		saveReadyInt(CLI_GBD, 0);
		saveReadyInt(SEL_NO_DEFAU, 0);
		saveReadyInt(SEL_DEFAU, 0);
		//follw
		saveReadyInt(HM_T_FOLLOW, 0);
		saveReadyInt(BC_T_FOLLOW, 0);
		saveReadyInt(CH_T_FOLLOW, 0);
		saveReadyInt(CONT_T_FOLLOW, 0);
		saveReadyInt(GPCHAT_T_FOLLOW, 0);
		//discover
		saveReadyInt(EN_BC_DISC, 0);
		saveReadyInt(EN_BC_HOT, 0);
		saveReadyInt(EN_BC_TRD, 0);
		saveReadyInt(EN_BC_CITY, 0);
		saveReadyInt(CITY_BC_PF, 0);
		saveReadyInt(CITY_PF_REGION, 0);
		
		
		
		
		editor.commit();
		saveFile(getLoginSuccessHttpJsonStr(), true);
		saveFile(getNoLoginHttpJsonStr(), false);
		
			}});
		
	}
	
	public void clearNoLoginData() {
		
	}
	
	class LoginSuccessStatisticBean {
		
		String osver;
		String mver;
		String imei;
		String mcc;
		String imsi;
		String dsrc;
		String ver;
		String afid;
		String time;
		
		Data data;
		
		LoginSuccessStatisticBean() {
			osver = "Android";
			mver = PalmchatApp.getOsInfo().getUa();
			imei = PalmchatApp.getOsInfo().getImei();
			mcc = PalmchatApp.getOsInfo().getMcc();
			imsi = PalmchatApp.getOsInfo().getImsi();
			
//			dsrc = "android_sam";
//			dsrc = "android_yz";
//			dsrc = "android_gp";
//			dsrc = "android_op";
			dsrc = R_DSRC;
			ver = AppUtils.getAppVersionName(PalmchatApp.getApplication());
			afid = CacheManager.getInstance().getMyProfile().afId;
			time = getTime();
			
			data = new Data();
		}
		
		class Data {
			String version;
			String time;
			int t_duration;
			int s_m_pic;
			int s_m_text;
			int s_m_emotion;
			int s_m_voice;
			int s_m_card;
			int p_num;
			int g_num;
			int c_num;
			int f_num;
			int pf_t_chat;
			int list_t_p;
			int left_t_look;
			int hub_t_look;
			int look_add_succ;
			int left_t_shake;
			int hub_t_shake;
			int shake_num;
			int shake_add_succ;
			int left_t_cr;
			int hub_t_cr;
			int entry_crlist_num;
			int cr_add_succ;
			int entry_g;
			int group_add_succ;
			int entry_sbi_num;
			int sbi_add_succ;
			int entry_may_num;
			int may_add_succ;
			int entry_home;
			int entry_cl;
			int entry_fucl;
			int entry_fl;
			int fl_t_sf;
			int noti_plus;
			int look_t_pf;
			int shake_t_pf;
			int cr_t_pf;
			int group_t_pf;
			int sbi_t_pf;
			int may_t_pf;
			int entry_cpf;
//			int cpf_skip;
			int lg_a_succ;
			int lg_p_succ;
			int lg_e_succ;
			int gl_add_g;
			int invite_add_g;
			int add_g_succ;
			int see_g_info;
			int entry_me;
			int set_h_succ;
			int set_b_succ;
			int entry_pwall;
			int add_pwall;
			int del_pwall;
			int save_pwall;
			int entry_mpf;
			int set_mpf;
			int entry_setting;
			int u_pb_succ;
			int d_pb_succ;
			int entry_ma;
			int set_sa_succ;
			int set_bp_succ;
			int set_ae_succ;
			int pb_t_invite;
			int pf_add_succ;
			
			// new add
			int entry_reg_re;
			int w_id_con;
			int w_id_succ;
			int w_id_err;
			int re_no_id;
			int re_id_unnet;
			int entry_nbs;
			int entry_filt;
			int filt_fm;
			int filt_m;
			int filt_pt_on;
			int entry_bcm;
			int entry_sd_bcm;
			int sd_bcm_succ;
			int bcm_t_pf;
			int entry_look_cpf;
			int set_lh_succ;
			int set_lp_succ;
			int l_set_lh;
			int lset_lh_succ;
			int l_set_lp;
			int lset_lp_succ;
			int entry_po;
			int set_pn_off;
			int set_nbs_off;
			int entry_mmpf;
			int mpf_set_p;
			int mpf_t_gr;
			int gr_t_pf;
			int mpf_set_wup;
			int pf_t_hi;
			int pf_t_sdg;
			int pfsd_fl_succ;
			int see_pn;
			int nd_fl;
			int pnsd_fl;
			int pnsd_fl_succ;
//			int fb_t_invite;
			int entry_sound;
			int sound_system;
			int sound_drum;
			int sound_bloom;
			int sound_bubble;
			int sound_glass;
			int sound_shortmsg;
			int sound_shortmsg2;
			int sound_tweet;
			int sound_bubble2;
			int sound_drop;
			int sound_fresh;
			int star_t_pf;
			
			
//			add 2014.5.23
			int chg_sex;
			int chg_date;
			int chg_state;
			int cpf_ok;
			int set_pt;
			int cpf_skip;
			int entry_home_fbn;
			int entry_pbl;
			int entry_p_pbl;
			int entry_pbl_pf;
			int fol_pbl;
			int unfol_pbl;
			int sha_pbl_sms;
			int sha_pbl_fb;
			int entry_miss;
			int entry_miss_cpf;
			int set_mh_succ;
			int set_mp_succ;
			int set_mpn_off;
			int joined_miss;
			int share_miss_fb;
			int get_mgift;
			int get_mgift_err;
			int miss_t_pf;
			int misstop_t_pf;
			int missnew_t_pf;
			int entry_mini;
			int miss_more;
			int look_more;
			int lgo_exit;
			int lgo_lg;
			int fb_t_invite;
			int entry_home_fb;
			
			
//			add 2014.7.17 store, account center
			int entry_sto;
			int em_t_sto;
			int sto_em;
			int em_detail;
			int buy_em;
			int dl_free_em;
			int buy_em_succ;
			int buy_em_rcg;
			int em_rcg_sms;
			int em_rcg_card;
			int buy_em_sms;
			int buy_em_card;
			int buy_em_fail;
			int dl_em_succ;
			int dl_em_fail;
			int entry_acc;
			int entry_rr;
			int entry_rcg;
			int rcg_sms;
			int rcg_card;
			int rcg_sms_succ;
			int rcg_sms_fail;
			int rcg_card_succ;
			int rcg_card_cancel;
			int rcg_card_fail;
			int entry_mybcm;
			
			int sd_bcm_pic;
			int sd_bcm_voice;
			int sd_bcm_text;
			
			//2014-11-16 5.0 store
			int sto_gift;
			int gift_detail;
			int buy_gift;
			int dl_gift;
			int dl_free_gift;
			int buy_gift_succ;
			int buy_gift_rcg;
			int gift_rcg_sms;
			int gift_rcg_card;
			int buy_gift_sms;
			int buy_gift_card;
			int buy_gift_fail;
			int dl_gift_succ;
			int dl_gift_fail;
			int dl_em_cancel;
			int sdg_succ;
			int sdg_succ_coin;
			int sdg_t_rcg;
			int sdg_succ_sms;
			int sdg_succ_card;
			int sdg_cancel;
			
			//2014-11-16 5.0 chats
			int entry_glist;
			int entry_fpll;
			int entry_foller;
			int contact_t_p;
			int ct_add_succ;
			
			int entry_xep;
			int cp_num;
			
			int pf_t_report;
			int pf_t_follow;
			int pf_t_bcm;

			int ct_t_pf;
			int id_t_pf;
			int contact_t_pf;
			
//			add 2015.3.15(5.0
			int vstar_t_pf;
			
			int like_bcm;
			int com_bcm;
			int report_bcm;
			int del_bcm;
			int del_bcm_succ;
			int del_bcmcom;
			
			int entry_far;
			int far_t_pf;
			int like_bcm_pf;
			int com_bcm_pf;
			int report_bcm_pf;
			int far_t_search;
			int far_t_list;
			int far_t_bcm;
			int like_bcm_far;
			int com_bcm_far;
			int report_bcm_far;
			
			//add 2015-5-21
			//chats
			int cli_bgd;
			int sel_no_defau;
			int sel_defau;
			
			//profile
			int hm_t_follow;
			int bc_t_follow;
			int ch_t_follow;
			int cont_t_follow;
			int gpchat_t_follow;
			//discover
			int en_bc_disc;
			int en_bc_hot;
			int en_bc_trd;
			int en_bc_city;
			int city_bc_pf;
			int city_pf_region;
			
			
			
			
			Data() {
				version = AppUtils.getAppVersionName(PalmchatApp.getApplication());;
				time = getTime();
				t_duration = (int)(getLong(T_DURATION) / 1000);
				s_m_pic = getInt(S_M_PIC);
				s_m_text = getInt(S_M_TEXT);
				s_m_emotion = getInt(S_M_EMOTION);
				s_m_voice = getInt(S_M_VOICE);
				s_m_card = getInt(S_M_CARD);
				p_num = getInt(P_NUM);
				g_num = getInt(G_NUM);
				c_num = getInt(C_NUM);
				f_num = getInt(F_NUM);
				pf_t_chat = getInt(PF_T_CHAT);
				list_t_p = getInt(LIST_T_P);
				left_t_look = getInt(LEFT_T_LOOK);
				hub_t_look = getInt(HUB_T_LOOK);
				look_add_succ = getInt(LOOK_ADD_SUCC);
				left_t_shake = getInt(LEFT_T_SHAKE);
				hub_t_shake = getInt(HUB_T_SHAKE);
				shake_num = getInt(SHAKE_NUM);
				shake_add_succ = getInt(SHAKE_ADD_SUCC);
				left_t_cr = getInt(LEFT_T_CR);
				hub_t_cr = getInt(HUB_T_CR);
				entry_crlist_num = getInt(ENTRY_CRLIST_NUM);
				cr_add_succ = getInt(CR_ADD_SUCC);
				entry_g = getInt(ENTRY_G);
				group_add_succ = getInt(GROUP_ADD_SUCC);
				entry_sbi_num = getInt(ENTRY_SBI_NUM);
				sbi_add_succ = getInt(SBI_ADD_SUCC);
				entry_may_num = getInt(ENTRY_MAY_NUM);
				may_add_succ = getInt(MAY_ADD_SUCC);
				entry_home = getInt(ENTRY_HOME);
				entry_cl = getInt(ENTRY_CL);
				entry_fucl = getInt(ENTRY_FUCL);
				entry_fl = getInt(ENTRY_FL);
				fl_t_sf = getInt(FL_T_SF);
				noti_plus = getInt(NOTI_PLUS);
				look_t_pf = getInt(LOOK_T_PF);
				shake_t_pf = getInt(SHAKE_T_PF);
				cr_t_pf = getInt(CR_T_PF);
				group_t_pf = getInt(GROUP_T_PF);
				sbi_t_pf = getInt(SBI_T_PF);
				may_t_pf = getInt(MAY_T_PF);
				entry_cpf = getInt(ENTRY_CPF);
				cpf_skip = getInt(CPF_SKIP);
				lg_a_succ = getInt(LG_A_SUCC);
				lg_p_succ = getInt(LG_P_SUCC);
				lg_e_succ = getInt(LG_E_SUCC);
				gl_add_g = getInt(GL_ADD_G);
				invite_add_g = getInt(INVITE_ADD_G);
				add_g_succ = getInt(ADD_G_SUCC);
				see_g_info = getInt(SEE_G_INFO);
				entry_me = getInt(ENTRY_ME);
				set_h_succ = getInt(SET_H_SUCC);
				set_b_succ = getInt(SET_B_SUCC);
				entry_pwall = getInt(ENTRY_PWALL);
				add_pwall = getInt(ADD_PWALL);
				del_pwall = getInt(DEL_PWALL);
				save_pwall = getInt(SAVE_PWALL);
				entry_mpf = getInt(ENTRY_MPF);
				set_mpf = getInt(SET_MPF);
				entry_setting = getInt(ENTRY_SETTING);
				u_pb_succ = getInt(U_PB_SUCC);
				d_pb_succ = getInt(D_PB_SUCC);
				entry_ma = getInt(ENTRY_MA);
				set_sa_succ = getInt(SET_SA_SUCC);
				set_bp_succ = getInt(SET_BP_SUCC);
				set_ae_succ = getInt(SET_AE_SUCC);
				pb_t_invite = getInt(PB_T_INVITE);
				pf_add_succ = getInt(PF_ADD_SUCC);
				
				// new add
				entry_reg_re = getInt(ENTRY_REG_RE);
				w_id_con = getInt(W_ID_CON);
				w_id_succ = getInt(W_ID_SUCC);
				w_id_err = getInt(W_ID_ERR);
				re_no_id = getInt(RE_NO_ID);
				re_id_unnet = getInt(RE_ID_UNNET);
				entry_nbs = getInt(ENTRY_NBS);
				entry_filt = getInt(ENTRY_FILT);
				filt_fm = getInt(FILT_FM);
				filt_m = getInt(FILT_M);
				filt_pt_on = getInt(FILT_PT_ON);
				entry_bcm = getInt(ENTRY_BCM);
				entry_sd_bcm = getInt(ENTRY_SD_BCM);
				sd_bcm_succ = getInt(SD_BCM_SUCC);
				bcm_t_pf = getInt(BCM_T_PF);
				entry_look_cpf = getInt(ENTRY_LOOK_CPF);
				set_lh_succ = getInt(SET_LH_SUCC);
				set_lp_succ = getInt(SET_LP_SUCC);
				l_set_lh = getInt(L_SET_LH);
				lset_lh_succ = getInt(LSET_LH_SUCC);
				l_set_lp = getInt(L_SET_LP);
				lset_lp_succ = getInt(LSET_LP_SUCC);
				entry_po = getInt(ENTRY_PO);
				set_pn_off = getInt(SET_PN_OFF);
				set_nbs_off = getInt(SET_NBS_OFF);
				entry_mmpf = getInt(ENTRY_MMPF);
				mpf_set_p = getInt(MPF_SET_P);
				mpf_t_gr = getInt(MPF_T_GR);
				gr_t_pf = getInt(GR_T_PF);
				mpf_set_wup = getInt(MPF_SET_WUP);
				pf_t_hi = getInt(PF_T_HI);
				pf_t_sdg = getInt(PF_T_SDG);
				pfsd_fl_succ = getInt(PFSD_FL_SUCC);
				see_pn = getInt(SEE_PN);
				nd_fl = getInt(ND_FL);
				pnsd_fl = getInt(PNSD_FL);
				pnsd_fl_succ = getInt(PNSD_FL_SUCC);
//				fb_t_invite = getInt(FB_T_INVITE);
				entry_sound = getInt(ENTRY_SOUND);
				sound_system = getInt(SOUND_SYSTEM);
				sound_drum = getInt(SOUND_DRUM);
				sound_bloom = getInt(SOUND_BLOOM);
				sound_bubble = getInt(SOUND_BUBBLE);
				sound_glass = getInt(SOUND_GLASS);
				sound_shortmsg = getInt(SOUND_SHORTMSG);
				sound_shortmsg2 = getInt(SOUND_SHORTMSG2);
				sound_tweet = getInt(SOUND_TWEET);
				sound_bubble2 = getInt(SOUND_BUBBLE2);
				sound_drop = getInt(SOUND_DROP);
				sound_fresh = getInt(SOUND_FRESH);
				star_t_pf = getInt(STAR_T_PF);
				
				
//				add 2014.5.23
				chg_sex = getInt(CHG_SEX);
				chg_date = getInt(CHG_DATE);
				chg_state = getInt(CHG_STATE);
				cpf_ok = getInt(CPF_OK);
				set_pt = getInt(SET_PT);
				cpf_skip = getInt(CPF_SKIP);
				entry_home_fbn = getInt(ENTRY_HOME_FBN);
				entry_pbl = getInt(ENTRY_PBL);
				entry_p_pbl = getInt(ENTRY_P_PBL);
				entry_pbl_pf = getInt(ENTRY_PBL_PF);
				fol_pbl = getInt(FOL_PBL);
				unfol_pbl = getInt(UNFOL_PBL);
				sha_pbl_sms = getInt(SHA_PBL_SMS);
				sha_pbl_fb = getInt(SHA_PBL_FB);
				entry_miss = getInt(ENTRY_MISS);
				entry_miss_cpf = getInt(ENTRY_MISS_CPF);
				set_mh_succ = getInt(SET_MH_SUCC);
				set_mp_succ = getInt(SET_MP_SUCC);
				set_mpn_off = getInt(SET_MPN_OFF);
				joined_miss = getInt(JOINED_MISS);
				share_miss_fb = getInt(SHARE_MISS_FB);
				get_mgift = getInt(GET_MGIFT);
				get_mgift_err = getInt(GET_MGIFT_ERR);
				miss_t_pf = getInt(MISS_T_PF);
				misstop_t_pf = getInt(MISSTOP_T_PF);
				missnew_t_pf = getInt(MISSNEW_T_PF);
				miss_more = getInt(MISS_MORE);
				look_more = getInt(LOOK_MORE);
				lgo_exit = getInt(LGO_EXIT);
				lgo_lg = getInt(LGO_LG);
				fb_t_invite = getInt(FB_T_INVITE);
				entry_home_fb = getInt(ENTRY_HOME_FB);
				
//				add 2014.7.17 store, account center
				entry_sto = getInt(ENTRY_STO);
				em_t_sto = getInt(EM_T_STO);
				sto_em = getInt(STO_ME);
				em_detail = getInt(EM_DETAIL);
				buy_em = getInt(BUY_EM);
				dl_free_em = getInt(DL_FREE_EM);
				buy_em_succ = getInt(BUY_EM_SUCC);
				buy_em_rcg = getInt(BUY_EM_RCG);
				em_rcg_sms = getInt(EM_RCG_SMS);
				em_rcg_card = getInt(EM_RCG_CARD);
				buy_em_sms = getInt(BUY_EM_SMS);
				buy_em_card = getInt(BUY_EM_CARD);
				buy_em_fail = getInt(BUY_EM_FAIL);
				dl_em_succ = getInt(DL_EM_SUCC);
				dl_em_fail = getInt(DL_EM_FAIL);
				entry_acc = getInt(ENTRY_ACC);
				entry_rr = getInt(ENTRY_RR);
				entry_rcg = getInt(ENTRY_RCG);
				rcg_sms = getInt(RCG_SMS);
				rcg_card = getInt(RCG_CARD);
				rcg_sms_succ = getInt(RCG_SMS_SUCC);
				rcg_sms_fail = getInt(RCG_SMS_FAIL);
				rcg_card_succ = getInt(RCG_CARD_SUCC);
				rcg_card_cancel = getInt(RCG_CARD_CANCEL);
				rcg_card_fail = getInt(RCG_CARD_FAIL);
				
				entry_mybcm = getInt(ENTRY_MYBCM);
				
				sd_bcm_pic = getInt(SD_BCM_PIC);
				sd_bcm_voice = getInt(SD_BCM_VOICE);
				sd_bcm_text = getInt(SD_BCM_TEXT);

				//2014-11-16 5.0
				sto_gift = getInt(STO_GIFT);
				gift_detail = getInt(GIFT_DEETAIL);
				buy_gift = getInt(BUY_GIFT);
				dl_gift = getInt(DL_GIFT);
				dl_free_gift = getInt(DL_FREE_GIFT);
				buy_gift_succ = getInt(BUY_GIFT_SUCC);
				buy_gift_rcg = getInt(BUY_GIFT_RCG);
				gift_rcg_sms = getInt(GIFT_RCG_SMS);
				gift_rcg_card = getInt(GIFT_RCG_CARD);
				buy_gift_sms = getInt(BUY_GIFT_SMS);
				buy_gift_card = getInt(BUY_GIFT_CARD);
				buy_gift_fail = getInt(BUY_GIFT_FAIL);
				dl_gift_succ = getInt(DL_GIFT_SUCC);
				dl_gift_fail = getInt(DL_GIFT_FAIL);
				dl_em_cancel = getInt(DL_EM_CANCEL);
				sdg_succ = getInt(SDG_SUCC);
				sdg_succ_coin = getInt(SDG_SUCC_COIN);
				sdg_t_rcg = getInt(SDG_T_RCG);
				sdg_succ_sms = getInt(SDG_SUCC_SMS);
				sdg_succ_card = getInt(SDG_SUCC_CARD);
				sdg_cancel = getInt(SDG_CANCEL);
				
				//2014-11-16 5.0
				entry_glist = getInt(ENTRY_GLIST);
				entry_fpll = getInt(ENTRY_FPLL);
				entry_foller = getInt(ENTRY_FOLLER);
				contact_t_p = getInt(CONTACT_T_P);
				ct_add_succ = getInt(CT_ADD_SUCC);
				
				entry_xep = getInt(ENTRY_XEP);
				cp_num = getInt(CP_NUM);
				
				pf_t_report = getInt(PF_T_REPORT);
				pf_t_follow = getInt(PF_T_FOLLOW);
				pf_t_bcm = getInt(PF_T_BCM);
				
				ct_t_pf = getInt(CT_T_PF);
				id_t_pf = getInt(ID_T_PF);
				contact_t_pf = getInt(CONTACT_T_PF);
				
//				add 2015.3.15
				vstar_t_pf = getInt(VSTAR_T_PF);
			
				like_bcm = getInt(LIKE_BCM);
				com_bcm = getInt(COM_BCM);
				report_bcm = getInt(REPORT_BCM);
				del_bcm = getInt(DEL_BCM);
				del_bcm_succ = getInt(DEL_BCM_SUCC);
				del_bcmcom = getInt(DEL_BCMCOM);
				
				entry_far = getInt(ENTRY_FAR);
				far_t_pf = getInt(FAR_T_PF);
				like_bcm_pf = getInt(LIKE_BCM_PF);
				com_bcm_pf = getInt(COM_BCM_PF);
				report_bcm_pf = getInt(REPORT_BCM_PF);
				far_t_search = getInt(FAR_T_SEARCH);
				far_t_list = getInt(FAR_T_LIST);
				far_t_bcm = getInt(FAR_T_BCM);
				like_bcm_far = getInt(LIKE_BCM_FAR);
				com_bcm_far = getInt(COM_BCM_FAR);
				report_bcm_far = getInt(REPORT_BCM_FAR);
				
				//add 2015-5-21
				//chats
				cli_bgd = getInt(CLI_GBD);
				sel_no_defau = getInt(SEL_NO_DEFAU);
				sel_defau = getInt(SEL_DEFAU);
				
				//follow
				hm_t_follow = getInt(HM_T_FOLLOW);
				bc_t_follow = getInt(BC_T_FOLLOW);
				ch_t_follow = getInt(CH_T_FOLLOW);
				cont_t_follow = getInt(CONT_T_FOLLOW);
				gpchat_t_follow = getInt(GPCHAT_T_FOLLOW);
				
				//discover
				en_bc_disc = getInt(EN_BC_DISC);
				en_bc_hot = getInt(EN_BC_DISC);
				en_bc_trd = getInt(EN_BC_TRD);
				en_bc_city = getInt(EN_BC_TRD);
				city_bc_pf = getInt(CITY_BC_PF);
				city_pf_region = getInt(CITY_PF_REGION);
				
				
				
				
			
			}
			
		}
		
	}
	
	
	class NoLoginStatisticBean {
		
		String osver;
		String mver;
		String imei;
		String mcc;
		String imsi;
		String dsrc;
		String ver;
		String time;
		
		Data data;
		
		NoLoginStatisticBean() {
			osver = "Android";
			mver = PalmchatApp.getOsInfo().getUa();
			imei = PalmchatApp.getOsInfo().getImei();
			mcc = PalmchatApp.getOsInfo().getMcc();
			imsi = PalmchatApp.getOsInfo().getImsi();
			
			// TODO
//			dsrc = "android_sam";
//			dsrc = "android_yz";
//			dsrc = "android_gp";
//			dsrc = "android_op";
			dsrc = R_DSRC;
			ver = AppUtils.getAppVersionName(PalmchatApp.getApplication());
			time = getTime();
			
			data = new Data();
		}
		
		class Data {
			String version;
			String time;
			int entry_guide;
			int entry_reg_p;
			int reg_p_succ;
			int entry_reg_e;
			int reg_e_succ;
			int entry_lg_a;
			int entry_lg_p;
			int entry_lg_e;
			int entry_e_f;
			int reset_p_succ;
			int entry_p_f;
			int sm_num;
			
			// new add 
			int w_pnum_n;
			int w_pnum_r;
			int entry_sms;
			int entry_sms_w;
			int smsver_succ;
			int w_code_n;
			int code_error;
			int w_email_n;
			int email_error;
			int w_code_succ;
			int reg_error;
			int network_un;
			int find_p_succ;
			
			// new add 2014.4.23
			int code_fail;
			int sms_fail;
			int e_network_un;
			
			int entry_s_f;
			
//			add 2014.5.23
			int w_email_r;
			int entry_reg_i;
			int entry_lg_fb;
			int entry_lg_wfb;
			int fb_ok;
			int wfb_ok;
			int lg_fb_succ;
			int lg_wfb_succ;
			int fb_network_un;
			int entry_cpf_fb;
			int select_countr;
			int w_pw_n;
			int pw_network_un;
			
//			add 2015.3.15 (5.0)
			int entry_lg_gg;
			int entry_lg_wgg;
			int gg_ok;
			int wgg_ok;
			int lg_gg_succ;
			int lg_wgg_succ;
			int gg_network_un;
			int entry_home_gg;
			int entry_cpf_gg;
			int entry_home_ggn;
			int entry_lg_tw;
			int entry_lg_wtw;
			int tw_ok;
			int wtw_ok;
			int lg_tw_succ;
			int lg_wtw_succ;
			int tw_network_un;
			int entry_home_tw;
			int entry_cpf_tw;
			
			int entry_home_twn;
			
			int code_no_pn;
			int code_no_pw;
			int pnum_reged;
			int reg_pnum;
			int reg_no_pn;
			int reg_no_code;
			int reg_no_pw;
			
			//add 2015-5-21
			int pnum_inva;
			int reg_cre_pw;
			int reg_ipt_name;
			int reg_ipt_bdate;
			int reg_sel_gder;
			int sel_female;
			int sel_male;
			int pnum_con;
			int sms_code;
			int pnum_over;
			int code_inva;
			int code_over;
			int get_code;
			int new_code;
			int email_no;
			int email_cre_pw;
			int email_ipt_name;
			int email_ipt_bdate;
			int email_sel_gder;
			int email_sel_female;
			int email_sel_male;
			
			
			
			
			Data() {
				version = AppUtils.getAppVersionName(PalmchatApp.getApplication());;
				time = getTime();
				
				entry_guide = getNoLoginInt(ENTRY_GUIDE);
				entry_reg_p = getNoLoginInt(ENTRY_REG_P);
				reg_p_succ = getNoLoginInt(REG_P_SUCC);
				entry_reg_e = getNoLoginInt(ENTRY_REG_E);
				reg_e_succ = getNoLoginInt(REG_E_SUCC);
				find_p_succ = getInt(FIND_P_SUCC);
				
				entry_lg_a = getNoLoginInt(ENTRY_LG_A);
				entry_lg_p = getNoLoginInt(ENTRY_LG_P);
				entry_lg_e = getNoLoginInt(ENTRY_LG_E);
				
				entry_e_f = getNoLoginInt(ENTRY_E_F);
				reset_p_succ = getNoLoginInt(RESET_P_SUCC);
				entry_p_f = getNoLoginInt(ENTRY_P_F);
				sm_num = getNoLoginInt(SM_NUM);
				
				w_pnum_n = getNoLoginInt(W_PNUM_N);
				w_pnum_r = getNoLoginInt(W_PNUM_R);
				entry_sms = getNoLoginInt(ENTRY_SMS);
				entry_sms_w = getNoLoginInt(ENTRY_SMS_W);
				smsver_succ = getNoLoginInt(SMSVER_SUCC);
				w_code_n = getNoLoginInt(W_CODE_N);
				code_error = getNoLoginInt(CODE_ERROR);
				w_email_n = getNoLoginInt(W_EMAIL_N);
				email_error = getNoLoginInt(EMAIL_ERROR);
				w_code_succ = getNoLoginInt(W_CODE_SUCC);
				reg_error = getNoLoginInt(REG_ERROR);
				network_un = getNoLoginInt(NETWORK_UN);
				
				// new add 2014.4.23
				code_fail = getNoLoginInt(CODE_FAIL);
				sms_fail = getNoLoginInt(SMS_FAIL);
				e_network_un = getNoLoginInt(E_NETWORK_UN);
				
				entry_s_f = getInt(ENTRY_S_F);
				
//				add 2014.5.23
				w_email_r = getNoLoginInt(W_EMAIL_R);
				entry_reg_i = getNoLoginInt(ENTRY_REG_I);
				entry_lg_fb = getNoLoginInt(ENTRY_LG_FB);
				entry_lg_wfb = getNoLoginInt(ENTRY_LG_WFB);
				fb_ok = getNoLoginInt(FB_OK);
				wfb_ok = getNoLoginInt(WFB_OK);
				lg_fb_succ = getNoLoginInt(LG_FB_SUCC);
				lg_wfb_succ = getNoLoginInt(LG_WFB_SUCC);
				fb_network_un = getNoLoginInt(FB_NETWORK_UN);
				entry_cpf_fb = getNoLoginInt(ENTRY_CPF_FB);
				select_countr = getNoLoginInt(SELECT_COUNTR);
				w_pw_n = getNoLoginInt(W_PW_N);
				pw_network_un = getNoLoginInt(PW_NETWORK_UN);
				
				
//				add 2015.3.15(5.0)
				entry_lg_wgg = getNoLoginInt(ENTRY_LG_WGG);
				gg_ok = getNoLoginInt(GG_OK);
				wgg_ok = getNoLoginInt(WGG_OK);
				lg_gg_succ = getNoLoginInt(LG_GG_SUCC);
				lg_wgg_succ = getNoLoginInt(LG_WGG_SUCC);
				gg_network_un = getNoLoginInt(GG_NETWORK_UN);
				entry_home_gg = getNoLoginInt(ENTRY_HOME_GG);
				entry_cpf_gg = getNoLoginInt(ENTRY_CPF_GG);
				entry_home_ggn = getNoLoginInt(ENTRY_HOME_GGN);
				entry_lg_tw = getNoLoginInt(ENTRY_LG_TW);
				entry_lg_wtw = getNoLoginInt(ENTRY_LG_WTW);
				tw_ok = getNoLoginInt(TW_OK);
				wtw_ok = getNoLoginInt(WTW_OK);
				lg_tw_succ = getNoLoginInt(LG_TW_SUCC);
				lg_wtw_succ = getNoLoginInt(LG_WTW_SUCC);
				tw_network_un = getNoLoginInt(TW_NETWORK_UN);
				entry_home_tw = getNoLoginInt(ENTRY_HOME_TW);
				entry_cpf_tw = getNoLoginInt(ENTRY_CPF_TW);
				entry_home_twn = getNoLoginInt(ENTRY_HOME_TWN);
				
				code_no_pn = getNoLoginInt(CODE_NO_PN);
				code_no_pw = getNoLoginInt(CODE_NO_PW);
				pnum_reged = getNoLoginInt(PNUM_REGED);
				reg_pnum = getNoLoginInt(REG_PNUM);
				reg_no_pn = getNoLoginInt(REG_NO_PN);
				reg_no_code = getNoLoginInt(REG_NO_CODE);
				reg_no_pw = getNoLoginInt(REG_NO_PW);
				
//				add 2015-5-21
				pnum_inva = getNoLoginInt(PNUM_INVA);
				reg_cre_pw = getNoLoginInt(REG_CRE_PW);
				reg_ipt_name = getNoLoginInt(REG_IPT_NAME);
				reg_ipt_bdate = getNoLoginInt(REG_IPT_BDATE);
				reg_sel_gder = getNoLoginInt(REG_SEL_GDER);
				sel_female = getNoLoginInt(SEL_FEMALE);
				sel_male = getNoLoginInt(SEL_MALE);
				pnum_con = getNoLoginInt(PNUM_CON);
				sms_code = getNoLoginInt(SMS_CODE);
				pnum_over = getNoLoginInt(PNUM_OVER);
				code_inva = getNoLoginInt(CODE_INVA);
				code_over = getNoLoginInt(CODE_OVER);
				get_code = getNoLoginInt(GET_CODE);
				new_code = getNoLoginInt(NEW_CODE);
				email_no = getNoLoginInt(EMAIL_NO);
				email_cre_pw = getNoLoginInt(EMAIL_CRE_PW);
				email_ipt_name = getNoLoginInt(EMAIL_IPT_NAME);
				email_ipt_bdate = getNoLoginInt(EMAIL_IPT_BDATE);
				email_sel_gder = getNoLoginInt(EMAIL_SEL_GDER);
				email_sel_female = getNoLoginInt(EMAIL_SEL_FEMALE);
				email_sel_male = getNoLoginInt(EMAIL_SEL_MALE);
				
				
			}
			
		}
		
	}

}
