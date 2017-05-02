package com.core.cache;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import android.location.LocationManager;
import android.text.TextUtils;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.afmobi.palmchat.ui.activity.setting.Contacts;
import com.afmobi.palmchat.util.DateUtil;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.GifImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.PopMessageManager;
import com.afmobi.palmchat.util.SearchFactory;
import com.afmobi.palmchat.util.SearchFilter;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.ThreadPool;
import com.core.AfChatroomDetail;
import com.core.AfFriendInfo;
import com.core.AfGrpListInfo.AfGrpItemInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfGrpProfileInfo.AfGrpProfileItemInfo;
import com.core.AfLoginInfo;
import com.core.AfLottery;
import com.core.AfMessageInfo;
import com.core.AfMutualFreindInfo;
import com.core.AfNearByGpsInfo;
import com.core.AfPalmCallResp;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfMFileInfo;
import com.core.AfStoreProdList;
import com.core.AfStoreProdList.AfProdProfile;
import com.core.Consts;
import com.core.param.AfCacheParam;

import static com.afmobi.palmchat.PalmchatApp.getApplication;

public class CacheManager {
    public static final String follow_suffix = "_FOLLOW";
    public static final String add_friend_suffix = "_ADD_FRIEND";
    public static final String accept_friend_suffix = "_ACCEPT_FRIEND";
    public static final String FLOWER_MSG_SUFFIX = "FLOWER_MSG_SUFFIX";
    private ArrayList<String> mStates = new ArrayList<String>();
    private ArrayList<AfNearByGpsInfo> broadcastArrayList = new ArrayList<AfNearByGpsInfo>();
    private AfProfileInfo profileInfo;
    private AfLoginInfo loginInfo;
    private LocationManager gpsManager;
    private String curState;
    @SuppressWarnings("rawtypes")
    final private AbsCacheSortList[] mCacheSortList;
    private List<AfGrpProfileInfo> mGroupInfo_list;
    private List<AfGrpItemInfo> mGrpGroupInfo_list = new ArrayList<AfGrpItemInfo>();
    private List<AfFriendInfo> mFriendInfo_list = new ArrayList<AfFriendInfo>();
    private File mCurFile;
    private int mCurAction;
    private String recharge_intro_url = "";//zhh 充值方式说明的 url

    private AfPalmCallResp.AfPalmCallHotListItem MyPalmCallHotListItem;


    //  my profile upload photo file
    public void setCurFile(File curFile) {
        mCurFile = curFile;
    }

    public File getCurFile() {
        return mCurFile;
    }

// my profile upload photo action

    public void setCurAction(int curAction) {
        mCurAction = curAction;
    }

    public int getCurAction() {
        return mCurAction;
    }

    /**
     * heguiming 2013-11-15  PeopleYouMayKnow Cache
     */
    private List<AfMutualFreindInfo> peopleYouMayKnowMutualList;
    private List<AfFriendInfo> peopleYouMayKnowFriendList;
    private List<AfNearByGpsInfo> lookAroundFriendList;
    private List<AfFriendInfo> searchByInfoList;

    public double lat;
    public double lon;

    private AfLottery.LotteryInit mLotteryInit = null;

    public AfLottery.LotteryInit getLotteryInit() {
        return mLotteryInit;
    }


    private boolean isShowPalmCall = false;//是否显示PalmCall入口

    public boolean isPalmGuessShow() {
        return isShowPalmCall;
    }

    public void setPalmGuessShow(boolean isShow) {
        isShowPalmCall = isShow;
    }

//	public boolean isOpenAirtime() {
//		return isOpenAirtime;
//	} 
//	public void setOpenAirtime(boolean isShow) {
//		isOpenAirtime = isShow;
//	}


    public String getRecharge_intro_url() {
        return recharge_intro_url;
    }

    public void setRecharge_intro_url(String recharge_intro_url) {
        this.recharge_intro_url = recharge_intro_url;
    }

    private boolean isLoginSuccess;

    public void setLoginSuccess(boolean isLogined) {
        isLoginSuccess = isLogined;
    }

    public boolean isLoginSuccess() {
        return isLoginSuccess;
    }

    private void clearCacheData() {
        peopleYouMayKnowMutualList = null;
        peopleYouMayKnowFriendList = null;
        lookAroundFriendList = null;
        afChatroomDetails = null;
        mStoreEmojiProdList = null;

        gifDownLoadMap.clear();
        mStoreDownloadingMap.clear();
        broadcastMap.clear();
        clickable.clear();
        hashMap_bc_resend.clear();
        BC_RecordSendSuccessData.clear();
        /*if (PalmchatApp.getApplication().mMemoryBitmapCache != null) {
            PalmchatApp.getApplication().mMemoryBitmapCache.evictAll();
		}*/

        getClickableMap().clear();

        mFlowerSendingMap.clear();
        mChattingMap.clear();

        mLotteryInit = null;
        isLoginSuccess = false;
    }


    private AfStoreProdList mStoreEmojiProdList;


    public AfStoreProdList getmStoreEmojiProdList() {
        if (mStoreEmojiProdList == null) {
            mStoreEmojiProdList = FileUtils.LoadAfStoreProdListFromFile(RequestConstant.STORE_LIST_CACHE);
        }
        return mStoreEmojiProdList;
    }

    public void setmStoreEmojiProdList(AfStoreProdList mStoreEmojiProdList) {
        this.mStoreEmojiProdList = mStoreEmojiProdList;
        if (mStoreEmojiProdList != null) {
            FileUtils.SaveAfStoreProdListToFile(mStoreEmojiProdList, RequestConstant.STORE_LIST_CACHE);
        }
    }

    public List<AfMutualFreindInfo> getPeopleYouMayKnowMutualList() {
        return peopleYouMayKnowMutualList;
    }

    public void setPeopleYouMayKnowMutualList(
            List<AfMutualFreindInfo> peopleYouMayKnowMutualList) {
        this.peopleYouMayKnowMutualList = peopleYouMayKnowMutualList;
    }

    public List<AfFriendInfo> getPeopleYouMayKnowFriendList() {
        return peopleYouMayKnowFriendList;
    }

    public void setPeopleYouMayKnowFriendList(
            List<AfFriendInfo> peopleYouMayKnowFriendList) {
        this.peopleYouMayKnowFriendList = peopleYouMayKnowFriendList;
    }

    public void setGroup(AfGrpProfileInfo mGroupInfo) {
        mGroupInfo_list.set(0, mGroupInfo);
    }

    public AfFriendInfo findAfFriendInfoByAfId(String afId) {
        PalmchatLogUtils.println("findAfFriendInfoByAfId  afId  " + afId);
        AfFriendInfo mFriendInfo = null;
        if (StringUtil.isEmpty(afId, true)) {
            PalmchatLogUtils.println("findAfFriendInfoByAfId  afId2  " + afId);
            return mFriendInfo;
        }
        mFriendInfo_list = CacheManager.getInstance().getFriends_list(Consts.AFMOBI_FRIEND_TYPE_FF);
        if (mFriendInfo_list != null) {
            PalmchatLogUtils.println("findAfFriendInfoByAfId  mFriendInfo_list.size  " + mFriendInfo_list.size());
        }
        synchronized (mFriendInfo_list) {
            Iterator<AfFriendInfo> items = mFriendInfo_list.iterator();
            while (items.hasNext()) {
                AfFriendInfo item = items.next();
                if (afId.equals(item.afId) && Consts.AFMOBI_FRIEND_TYPE_FF == item.type) {
                    mFriendInfo = item;
                    PalmchatLogUtils.println("findAfFriendInfoByAfId  mFriendInfo  " + mFriendInfo);
                    break;
                }
            }
        }
        PalmchatLogUtils.println("findAfFriendInfoByAfId  mFriendInfo  return  " + mFriendInfo);
        return mFriendInfo;
    }

    /** 保存好友Profile，非自己的profile
     * @param afProfileInfo
     */
    public synchronized void saveOrUpdateFriendProfile(AfProfileInfo afProfileInfo){
        if (afProfileInfo != null) {
            AfFriendInfo aff = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).search(afProfileInfo, false, true);
            int followType = ((PalmchatApp) getApplication()).mAfCorePalmchat.AfDbProfileGetFollowtype(afProfileInfo.afId);
            afProfileInfo.follow_type = followType;
            if (aff != null) {
                afProfileInfo.alias = aff.alias;
                afProfileInfo.type = Consts.AFMOBI_FRIEND_TYPE_FF;
                CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).saveOrUpdate(afProfileInfo, false, true);
            } else if (null != CacheManager.getInstance().getFriendsCacheSortListEx(
                    Consts.AFMOBI_FRIEND_TYPE_BF).search(afProfileInfo, false, true)) {
                    afProfileInfo.type = Consts.AFMOBI_FRIEND_TYPE_BF;
                    CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_BF).saveOrUpdate(afProfileInfo, false, true);
            } else {
                afProfileInfo.type = Consts.AFMOBI_FRIEND_TYPE_STRANGER;
                CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_STRANGER).saveOrUpdate(afProfileInfo, false, true);
            }
            ((PalmchatApp) getApplication()).mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, afProfileInfo);
        }
    }

    /***********************************************************************************************************************/
    //cache friend manager

    /***********************************************************************************************************************/
    @SuppressWarnings("unchecked")
    public List<AfFriendInfo> getFriends_list(byte type) {
        return (List<AfFriendInfo>) mCacheSortList[type].getList();
    }

    @SuppressWarnings("unchecked")
    public void loadCache(AfCacheParam cache) {
        PalmchatLogUtils.i("WXL", //加log 用于排查 好友列表服务器已经发  中间件已经通知但好友列表为空的情况 ，这里打印出中间件通知好友列表等列表的长度，
                "cacheMsg=" + cache.msg
                        + "frd_list=" + (cache.frd_cache == null ? "cache.frd_cache==null" : String.valueOf(cache.frd_cache.size()))
        );
        int i;
        if (Consts.AF_SYS_MSG_INIT == cache.msg || Consts.AF_SYS_MSG_UPDATE_FRIEND == cache.msg) {
            for (i = Consts.AFMOBI_FRIEND_TYPE_FF; i <= Consts.AFMOBI_FRIEND_TYPE_STRANGER; i++) {
                mCacheSortList[i].insert(cache.frd_cache.get(i));
                PalmchatLogUtils.i("WXL", //加log 用于排查 好友列表服务器已经发  中间件已经通知但好友列表为空的情况 ，这里打印出中间件通知好友列表等列表的长度，
                        "cacheMsg i=" + i + " frd_list=" + (cache.frd_cache.get(i) == null ? "null" : cache.frd_cache.get(i).size()));
            }
            //cache public account
            mCacheSortList[Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT].insert(cache.public_account);
        }

//		following, follower
        if (Consts.AF_SYS_MSG_INIT == cache.msg || Consts.AFMOBI_SYS_MSG_UPDATE_FOLLOW == cache.msg) {
            mCacheSortList[Consts.AFMOBI_FOLLOW_TYPE_MASTER].insert(cache.follow_master);
            mCacheSortList[Consts.AFMOBI_FOLLOW_TYPE_PASSIVE].insert(cache.follow_passive);
        }

        getGroupMembers(cache);
        if (Consts.AFMOBI_SYS_MSG_UPDATE_GRP == cache.msg) {

            //search
            mCacheSortList[Consts.AFMOBI_FRIEND_TYPE_GROUP].insert(cache.grp);

            mCacheSortList[Consts.AFMOBI_FRIEND_TYPE_PUBLIC_GROUP].insert(cache.public_grp);
        } else if (Consts.AF_SYS_MSG_INIT == cache.msg) {
            mCacheSortList[Consts.AFMOBI_FRIEND_TYPE_GROUP].insert(cache.grp);

            mCacheSortList[Consts.AFMOBI_FRIEND_TYPE_PUBLIC_GROUP].insert(cache.public_grp);

            //recent cache record
            List<MainAfFriendInfo> frd_req = new ArrayList<MainAfFriendInfo>();
            List<MainAfFriendInfo> frd_recent = new ArrayList<MainAfFriendInfo>();

//			friend req cache	
            for (int i1 = cache.frd_req_msg.size() - 1; i1 >= 0; i1--) {
                AfMessageInfo msg = cache.frd_req_msg.get(i1);
                AfFriendInfo afFriendInfo1 = searchAllFriendInfo(msg.getKey());
                if (afFriendInfo1 != null) {
                    MainAfFriendInfo friend = new MainAfFriendInfo();
                    friend.afFriendInfo = afFriendInfo1;
                    friend.afMsgInfo = msg;
                    frd_req.add(friend);
                }
            }

//			recent msg cache
            for (int i1 = cache.recent_msg.size() - 1; i1 >= 0; i1--) {
                AfMessageInfo msg = cache.recent_msg.get(i1);

                if (MessagesUtils.isPrivateMessage(msg.type) || MessagesUtils.isSystemMessage(msg.type)) {
                    AfFriendInfo afFriendInfo1 = searchAllFriendInfo(msg.getKey());
                    if (afFriendInfo1 != null) {
                        MainAfFriendInfo friend = new MainAfFriendInfo();
                        friend.afFriendInfo = afFriendInfo1;
                        friend.afMsgInfo = msg;
                        frd_recent.add(friend);
                    }

                } else if (MessagesUtils.isGroupChatMessage(msg.type)) {

                    AfGrpProfileInfo afGrpInfo = new AfGrpProfileInfo();
                    afGrpInfo.afid = msg.getKey();
                    AfGrpProfileInfo result = getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).search(afGrpInfo, false, true);

                    if (result != null) {

                        MainAfFriendInfo grpInfo = new MainAfFriendInfo();
                        grpInfo.afGrpInfo = result;
                        grpInfo.afMsgInfo = msg;
                        frd_recent.add(grpInfo);
                    }

                }

            }

            mCacheSortList[Consts.AFMOBI_FRIEND_TYPE_FRD_REQ].insert(frd_req);
            mCacheSortList[Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD].insert(frd_recent);

//			mCacheSortList[Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD].insert(cache.recent_msg);
//			mCacheSortList[Consts.AFMOBI_FRIEND_TYPE_FRD_REQ].insert(cache.frd_req_msg);

            PalmchatLogUtils.println("---load cache frd_cache size:" + cache.frd_cache.size() +
                    "--grp size:" + cache.grp.size() + "-- frd_req_msg size:" + cache.frd_req_msg.size() + "--recent_msg size:" + cache.recent_msg.size());

        }
        cache = null;
    }


    private void getGroupMembers(AfCacheParam cache) {
        if (null != cache.grp && 0 < cache.grp.size()) {
            for (int j = 0; j < cache.grp.size(); j++) {
                AfGrpProfileInfo afGrpProfileInfo = cache.grp.get(j);
                int size = afGrpProfileInfo.members.size();
                for (int k = 0; k < size; k++) {
                    AfGrpProfileItemInfo afGrpProfileItemInfo = afGrpProfileInfo.members.get(k);
                    AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afGrpProfileItemInfo.afid);
                    if (null != afFriendInfo) {
                        afGrpProfileItemInfo.name = afFriendInfo.name;
                        afGrpProfileItemInfo.head_image_path = afFriendInfo.head_img_path;
                        afGrpProfileItemInfo.sex = afFriendInfo.sex;
                    }
                }
            }
        }
    }

    /**
     * clear all cache
     */
    public void clearCache() {
        if (mCacheSortList != null) {
            for (int i = Consts.AFMOBI_FRIEND_CACHE_MIN; i < Consts.AFMOBI_RRIEND_TYPE_CACHE_MAX; i++) {
                if (mCacheSortList[i] != null) {
                    mCacheSortList[i].clear(false, true);
                }
            }
        }
        clearCacheData();
    }


    @SuppressWarnings("unchecked")
    public final AbsCacheSortList<MainAfFriendInfo> getCacheSortListEx(byte type) {
        return mCacheSortList[type];
    }


    @SuppressWarnings("unchecked")
    public final AbsCacheSortList<AfFriendInfo> getFriendsCacheSortListEx(byte type) {
        return mCacheSortList[type];
    }

    @SuppressWarnings("unchecked")
    public final AbsCacheSortList<AfMessageInfo> getRecentMsgCacheSortList(byte type) {
        return mCacheSortList[type];
    }

    public final AbsCacheSortList<AfGrpProfileInfo> getGrpCacheSortList(byte type) {
        return mCacheSortList[type];
    }

    public final AfFriendInfo searchAllFriendInfo(String afid) {

        AfFriendInfo element = new AfFriendInfo();
        element.afId = afid;

        for (byte type = Consts.AFMOBI_FRIEND_TYPE_FF; type <= Consts.AFMOBI_FRIEND_TYPE_STRANGER; type++) {
            AfFriendInfo info = (AfFriendInfo) mCacheSortList[type].search(element, true, true);
            if (info != null) {
                return info;
            }

        }
        if (!TextUtils.isEmpty(afid) && afid.equals(CacheManager.getInstance().getMyProfile().afId)) {
            AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
            AfFriendInfo info = AfProfileInfo.profileToFriend(myAfProfileInfo);
            return info;
        }

        if (afid != null && profileInfo != null && afid.equals(profileInfo.afId)) {
            return profileInfo;
        }

//		search public account
        AfFriendInfo search = new AfFriendInfo();
        search.afId = afid;
        AfFriendInfo info = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).search(search, false, true);

        if (info != null) {
            return info;
        }

        AfFriendInfo info2 = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).search(search, false, true);

        if (info2 != null) {
            return info2;
        }


        AfFriendInfo info3 = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE).search(search, false, true);

        if (info3 != null) {
            return info3;
        }


        return null;
    }

    /**
     * following或follower模块，查找本地是否有缓存
     *
     * @param afid
     * @return
     */
    public final AfFriendInfo searchFriendInfoForFollow(String afid) {

        AfFriendInfo element = new AfFriendInfo();
        element.afId = afid;

        AfFriendInfo info2 = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).search(element, false, true);

        if (info2 != null && !TextUtils.isEmpty(info2.name)) {
            return info2;
        }


        AfFriendInfo info3 = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE).search(element, false, true);

        if (info3 != null && !TextUtils.isEmpty(info3.name)) {
            return info3;
        }

        for (byte type = Consts.AFMOBI_FRIEND_TYPE_FF; type <= Consts.AFMOBI_FRIEND_TYPE_STRANGER; type++) {
            AfFriendInfo info = (AfFriendInfo) mCacheSortList[type].search(element, true, true);
            if (info != null && !TextUtils.isEmpty(info.name)) {
                return info;
            }

        }
        if (!TextUtils.isEmpty(afid) && afid.equals(CacheManager.getInstance().getMyProfile().afId)) {
            AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
            AfFriendInfo info = AfProfileInfo.profileToFriend(myAfProfileInfo);
            return info;
        }

        if (afid != null && profileInfo != null && afid.equals(profileInfo.afId)) {
            return profileInfo;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public final AfGrpProfileInfo searchGrpProfileInfo(
            String groupId) {
        AfGrpProfileInfo element = new AfGrpProfileInfo();
        element.afid = groupId;

        AfGrpProfileInfo info = (AfGrpProfileInfo) mCacheSortList[Consts.AFMOBI_FRIEND_TYPE_GROUP]
                .search(element, true, true);
        if (info != null) {
            return info;
        }

        return null;
    }


    private ThreadPool threadPool;
    private PopMessageManager mPopMessageManager;

    public CacheManager() {
        AfPalmchat core = AfPalmchat.getAfCorePalmchat();
        byte type;

        //friend cache
        mCacheSortList = new AbsCacheSortList[Consts.AFMOBI_RRIEND_TYPE_CACHE_MAX];
        for (type = Consts.AFMOBI_FRIEND_TYPE_FF; type <= Consts.AFMOBI_FRIEND_TYPE_STRANGER; type++) {
            mCacheSortList[type] = new FriendCacheSortList(core, type, CacheSortList.ASCENDING_ORDER);
        }
        //groupChat cache
        mCacheSortList[Consts.AFMOBI_FRIEND_TYPE_GROUP] = new GrpCacheSortList(core, CacheSortList.ASCENDING_ORDER);
        //pgroup
        mCacheSortList[Consts.AFMOBI_FRIEND_TYPE_PUBLIC_GROUP] = new GrpCacheSortList(core, CacheSortList.ASCENDING_ORDER);

        for (type = Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD; type <= Consts.AFMOBI_FRIEND_TYPE_FRD_REQ; type++) {
            mCacheSortList[type] = new MsgCacheSortList(core, type, CacheSortList.DESCEDNING_ORDER);
        }
        //public account cache
        mCacheSortList[Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT] = new FriendCacheSortList(core, Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT, CacheSortList.ASCENDING_ORDER);
        mCacheSortList[Consts.AFMOBI_FOLLOW_TYPE_MASTER] = new FriendCacheSortList(core, Consts.AFMOBI_FOLLOW_TYPE_MASTER, CacheSortList.ASCENDING_ORDER);
        mCacheSortList[Consts.AFMOBI_FOLLOW_TYPE_PASSIVE] = new FriendCacheSortList(core, Consts.AFMOBI_FOLLOW_TYPE_PASSIVE, CacheSortList.ASCENDING_ORDER);

//		mNotification = new ArrayList<MainAfFriendInfo>();		
//		mRecentChat = new ArrayList<MainAfFriendInfo>();		
        mGroupInfo_list = new ArrayList<AfGrpProfileInfo>();
        mFriendInfo_list = new ArrayList<AfFriendInfo>();

        threadPool = new ThreadPool();

        mGifImageUtil = new GifImageUtil();
        mPopMessageManager = new PopMessageManager();
    }


    public ThreadPool getThreadPoolInstance() {

        if (threadPool == null) {
            threadPool = new ThreadPool();
        }

        return threadPool;
    }

    public PopMessageManager getPopMessageManager() {
        if (mPopMessageManager == null) {
            mPopMessageManager = new PopMessageManager();
        }
        return mPopMessageManager;
    }

    private GifImageUtil mGifImageUtil;

    public GifImageUtil getGifImageUtilInstance() {
        if (mGifImageUtil == null) {
            mGifImageUtil = new GifImageUtil();
        }
        return mGifImageUtil;
    }

    public boolean isFriend(String afid) {
        AfFriendInfo friend = new AfFriendInfo();
        friend.afId = afid;
        AfFriendInfo info = (AfFriendInfo) mCacheSortList[Consts.AFMOBI_FRIEND_TYPE_FF].search(friend, false, true);
        return info == null ? false : true;
    }

    public boolean isFollow(String afid) {
        AfFriendInfo info = (AfFriendInfo) mCacheSortList[Consts.AFMOBI_FOLLOW_TYPE_MASTER].searchNoCheckName(afid, false, true);
        return info == null ? false : true;
    }

    private List<Contacts> contacts = new ArrayList<Contacts>();


    public static CacheManager getInstance() {
        return AfPalmchat.getCacheManager();
    }


    public AfProfileInfo getMyProfile() {
        if (profileInfo == null) {
            return new AfProfileInfo();
        }
        return profileInfo;
    }

    public void setMyProfile(AfProfileInfo myProfile) {
        profileInfo = myProfile;
//		profileInfo.type = Consts.AFMOBI_FRIEND_TYPE_STRANGER; 
    }



    /**
     * 用于保存登陆信息
     *
     * @return
     */
    public AfLoginInfo getLoginInfo() {
        if (loginInfo == null) {
            PalmchatLogUtils.e("WXL", "new AfLoginInfo()");
            return new AfLoginInfo();
        }
        return loginInfo;
    }

    public void setLoginInfo(AfLoginInfo afLoginInfo) {
        loginInfo = afLoginInfo;
    }

    public List<Contacts> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contacts> contacts) {
        Collections.sort(contacts);
        this.contacts = contacts;
    }

    public static class Country implements Comparable<Country> {

        SearchFilter filter = SearchFactory.getSearchFilter(SearchFactory.DEFAULT_CODE);
        private String country;
        private String code;
        private String sortKey;
        private String MCC;

        public String getMCC() {
            return MCC;
        }

        public void setMCC(String mCC) {
            MCC = mCC;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            sortKey = filter.getFullSpell(country);
            this.country = country;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSortKey() {
            return sortKey;
        }

        @Override
        public String toString() {
            return "Country [country=" + country + ", code=" + code + ", sortKey="
                    + sortKey + "]";
        }

        @Override
        public int compareTo(Country another) {
            return this.sortKey.compareTo(another.sortKey);
        }

    }

    public static class Region implements Serializable {


        private static final long serialVersionUID = 2315752446846279835L;
        private String name;
        private String countryCode;
        private String[] regions;
        private List<String[]> cities;

        public String getCountryCode() {
            if (null == countryCode) {
                return "";
            }
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String[] getRegions() {
            return regions;
        }

        public void setRegions(String[] regions) {
            this.regions = regions;
        }

        public List<String[]> getCities() {
            return cities;
        }

        public void setCities(List<String[]> cities) {
            this.cities = cities;
        }

        @Override
        public String toString() {
            return "State [name=" + name + ", regions=" + regions + ", cities="
                    + cities + "]";
        }

    }

    private ArrayList<AfMessageInfo> list = null;

    public ArrayList<AfMessageInfo> getAfMessageInfoList() {
        if (list == null) {
            list = new ArrayList<AfMessageInfo>();
        }
        return list;
    }

    private AfMessageInfo afMessageInfo;

    public void setAfMessageInfo(AfMessageInfo afMessageInfo) {
        // TODO Auto-generated method stub
        this.afMessageInfo = afMessageInfo;
    }

    public AfMessageInfo getAfMessageInfo() {
        return afMessageInfo;
    }

    //	forward msg
    private AfMessageInfo mForwardMsg;

    public void setForwardMsg(AfMessageInfo afMessageInfo) {
        // TODO Auto-generated method stub
        this.mForwardMsg = afMessageInfo;
    }

    public AfMessageInfo getForwardMsg() {
        return mForwardMsg;
    }


    //  need to send picture list
    private ArrayList<AfMessageInfo> mImgSendList = new ArrayList<AfMessageInfo>();

    public ArrayList<AfMessageInfo> getmImgSendList() {
        return mImgSendList;
    }

    public void addmImgSendList(AfMessageInfo msg) {

        mImgSendList.add(msg);
    }

    public void clearmImgSendList() {
       PalmchatLogUtils.i("GoupChatSendImage","clearmImgSendList");
        mImgSendList.clear();
    }

    private List<AfChatroomDetail> afChatroomDetails;

    public void setAfChatroomDetails(List<AfChatroomDetail> listTemp) {
        // TODO Auto-generated method stub
        this.afChatroomDetails = listTemp;
    }

    public List<AfChatroomDetail> getAfChatroomDetails() {
        return afChatroomDetails;
    }

    private long chatroomlistTime = 0;

    public long getChatroomListTime() {
        // TODO Auto-generated method stub
        return chatroomlistTime;
    }

    public void setChatroomListTime(long timeTemp) {
        this.chatroomlistTime = timeTemp;
    }

    private List<AfFriendInfo> mPublicAccountsList;
    //	private List<AfFriendInfo> mPublicAccountsList;
    private long mPublicAccountsTime = 0;


    public List<AfFriendInfo> getmPublicAccountsList() {
        return mPublicAccountsList;
    }

    public void setmPublicAccountsList(List<AfFriendInfo> mPublicAccountsList) {
        this.mPublicAccountsList = mPublicAccountsList;
    }

    public long getmPublicAccountsTime() {
        return mPublicAccountsTime;
    }

    public void setmPublicAccountsTime(long mPublicAccountsTime) {
        this.mPublicAccountsTime = mPublicAccountsTime;
    }

    /**
     * key : item id
     * value : downloading progress
     */
    private ConcurrentHashMap<String, Integer> mStoreDownloadingMap = new ConcurrentHashMap<String, Integer>();

    public ConcurrentHashMap<String, Integer> getStoreDownloadingMap() {

        return mStoreDownloadingMap;
    }

    private String screenString = "md";

    public void setScreenString(String commonString) {
        // TODO Auto-generated method stub
        this.screenString = commonString;
    }

    public String getScreenString() {
        return screenString;
    }

    public int getScreenType() {
        if ("xd".equals(screenString)) {
            return Consts.EXPRESS_TYPE_XD;
        } else if ("hd".equals(screenString)) {
            return Consts.EXPRESS_TYPE_HD;
        } else if ("ld".equals(screenString)) {
            return Consts.EXPRESS_TYPE_LD;
        } else {
            return Consts.EXPRESS_TYPE_ND;
        }
    }

    private HashMap<String, String> gifDownLoadMap = new HashMap<String, String>();
    private LinkedHashMap<Integer, AfNearByGpsInfo> broadcastMap = new LinkedHashMap<Integer, AfNearByGpsInfo>();

    public LinkedHashMap<Integer, AfNearByGpsInfo> getBroadcastaMap() {
        return broadcastMap;
    }

    public ArrayList<AfNearByGpsInfo> getArrayList() {
        return broadcastArrayList;
    }

    public HashMap<String, String> getGifDownLoadMap() {
        return gifDownLoadMap;
    }


    private int selected_position = 0;

    public int getSelectedPosition() {
        return selected_position;
    }

    public void setSelectedPosition(int selected_position) {
        this.selected_position = selected_position;
    }

    private boolean hasNewStoreProInfo = false;

    public boolean hasNewStoreProInfo() {
        // TODO Auto-generated method stub
        return this.hasNewStoreProInfo;
    }

    public void setHasNewStoreProInfo(boolean hasNewStoreProInfo) {
        this.hasNewStoreProInfo = hasNewStoreProInfo;
    }

    private boolean isChange;

    public boolean isFaceChange() {
        // TODO Auto-generated method stub
        return isChange;
    }

    public void setIsFaceChange(boolean isChange) {
        this.isChange = isChange;
    }

    /**
     * 发送广播时 计算出缓存url
     *
     * @param imgUrl
     * @param isSingle
     * @return
     */
    public String getThumb_url_sending(AfMFileInfo fileInfo, String imgUrl, boolean isSingle) {
        if (TextUtils.isEmpty(imgUrl)) {
            return null;
        }
        String thumb_url = null;//
        int index = imgUrl.indexOf("_"); ///images/2015/11/5/f8cd21233d69d7c3ba5e74dda7ff3475_600_500.jpg
        if (index >= 0) {//获取原图尺寸大小
            thumb_url = imgUrl.substring(0, index);//images/2015/11/5/f8cd21233d69d7c3ba5e74dda7ff3475
            index = imgUrl.indexOf("-"); //
            if (index >= 0) {//获取原图尺寸大小
                thumb_url = imgUrl.substring(0, index);//images/2015/11/5/f8cd21233d69d7c3ba5e74dda7ff3475
            }
            index = thumb_url.lastIndexOf("/");
            if (index >= 0) {
                thumb_url = thumb_url.substring(0, index + 1) + "64_64" + thumb_url.substring(index);

            }
            index = imgUrl.indexOf(".");
            if (index >= 0) {
                thumb_url += imgUrl.substring(index);//images/2015/11/5/f8cd21233d69d7c3ba5e74dda7ff3475.jpg
            }
            if (fileInfo != null) {
                fileInfo.thumb_url = thumb_url;//当发送出去但是还没从服务器重新获取该广播详情的时候  需要计算出它的thumb_url地址，转发的时候显示要用的
            }
        }


        String newPaht = "";
        if (getApplication().getWindowWidth() >= 720) {
            newPaht = thumb_url.replace("64_64", "layout");
        } else {
            newPaht = thumb_url.replace("64_64/", "layout/s");
        }


        return newPaht;
    }

    /**
     * 根据屏幕分辨率获取广播请求url
     *
     * @param thumb_url
     * @param isSingle
     * @param picRule   图片的排布规则  如果无排布规则 则是老版本广播
     * @return
     */
    public String getThumb_url(String thumb_url, boolean isSingle, String picRule) {
        if (TextUtils.isEmpty(thumb_url)) {
            return null;
        }
        String newSize = "W160_H160";
        String newPaht = "";
        if (TextUtils.isEmpty(picRule)) {//如果排布规则没有 那就取在正方的图
            if (isSingle) {
                if (getApplication().getWindowWidth() >= 1080) {
                    newSize = "W768_H768";
                } else if (getApplication().getWindowWidth() >= 720) {
                    newSize = "W512_H512";
                } else if (getApplication().getWindowWidth() >= 480) {
                    newSize = "W320_H320";
                } else if (getApplication().getWindowWidth() >= 320) {
                    newSize = "W160_H160";
                }
            } else {
                if (getApplication().getWindowWidth() >= 1080) {
                    newSize = "270_270";
                } else if (getApplication().getWindowWidth() >= 720) {
                    newSize = "180_180";
                } else if (getApplication().getWindowWidth() >= 480) {
                    newSize = "120_120";
                } else if (getApplication().getWindowWidth() >= 320) {
                    newSize = "64_64";
                }

            }
            newPaht = thumb_url.replace("64_64", newSize);
        } else {
            if (getApplication().getWindowWidth() >= 720) {
                newPaht = thumb_url.replace("64_64", "layout");
            } else {
                newPaht = thumb_url.replace("64_64/", "layout/s");
            }
        }
        return newPaht;
    }

    /**
     * 根据屏幕分辨率获取广播请求url
     *
     * @param thumb_url
     * @param isSingle
     * @return
     */
    public String getTrThumb_url(String thumb_url, boolean isSingle, boolean isMax) {
        if (TextUtils.isEmpty(thumb_url)) {
            return null;
        }
        String newSize = "W160_H160";
        String newPaht = "";
        if (isMax) {
            newPaht = thumb_url.replace("64_64", "540_540");
            return newPaht;
        }
        if (isSingle) {
            if (getApplication().getWindowWidth() >= 1080) {
                newSize = "W768_H768";
            } else if (getApplication().getWindowWidth() >= 720) {
                newSize = "W512_H512";
            } else if (getApplication().getWindowWidth() >= 480) {
                newSize = "W320_H320";
            } else if (getApplication().getWindowWidth() >= 320) {
                newSize = "W160_H160";
            }
        } else {
            if (getApplication().getWindowWidth() >= 1080) {
                newSize = "270_270";
            } else if (getApplication().getWindowWidth() >= 720) {
                newSize = "180_180";
            } else if (getApplication().getWindowWidth() >= 480) {
                newSize = "120_120";
            } else if (getApplication().getWindowWidth() >= 320) {
                newSize = "64_64";
            }

        }
        newPaht = thumb_url.replace("64_64", newSize);
        return newPaht;
    }

    /**
     * 根据屏幕分辨率获取广播请求url
     *
     * @param thumb_url
     * @param isSingle
     * @return
     */
    public String getThumb_Chatting_url(String thumb_url, boolean isSingle) {
        String newSize = "W160_H160";
        String newPaht = "";
        if (getApplication().getWindowWidth() >= 1080) {
            newSize = "270_270";
        } else if (getApplication().getWindowWidth() >= 320) {
            newSize = "180_180";
        }
        newPaht = thumb_url.replace("64_64", newSize);
        return newPaht;
    }

    private boolean is_store_back = false;

    public void setStoreBack(boolean isBack) {
        // TODO Auto-generated method stub
        is_store_back = isBack;
    }

    public boolean isStoreBack() {
        return is_store_back;
    }

    /**
     * record sending flower request
     * AfMessageInfo entity;
     * key: entity.afid + entity._id + CacheManager.FLOWER_MSG_SUFFIX
     * value: equal to key
     */
    private HashMap<String, String> mFlowerSendingMap = new HashMap<String, String>();

    public HashMap<String, String> getFlowerSendingMap() {
        return mFlowerSendingMap;
    }


    private HashMap<String, Boolean> clickable = new HashMap<String, Boolean>();

    public HashMap<String, Boolean> getClickableMap() {
        return clickable;
    }

    private boolean isEditTextDelete;

    public void setEditTextDelete(boolean b) {
        // TODO Auto-generated method stub
        isEditTextDelete = b;
    }

    public boolean getEditTextDelete() {
        return isEditTextDelete;
    }

    private String store_item_id;

    public void setSelectedEmotionItemId(String item_id) {
        // TODO Auto-generated method stub
        this.store_item_id = item_id;
    }

    private HashMap<String, AfProdProfile> itemid_update = new HashMap<String, AfProdProfile>();

    public HashMap<String, AfProdProfile> getItemid_update() {
        return itemid_update;
    }

    public String getsendTime(long currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = String.valueOf(currentTime);
        if (!timeStr.isEmpty() && timeStr.length() > 14) {
            String time = timeStr.substring(0, 13);
            currentTime = Long.valueOf(time);
        }
        Date date = new Date(currentTime);
        return formatter.format(date);
    }

    private HashMap<Integer, AfChapterInfo> hashMap_bc_resend = new HashMap<Integer, AfChapterInfo>();

    public HashMap<Integer, AfChapterInfo> getBC_resend_HashMap() {
        return hashMap_bc_resend;
    }

    public ArrayList<AfChapterInfo> getBC_sendFailed() {
        ArrayList<AfChapterInfo> aList = new ArrayList<AfChapterInfo>();
        if (hashMap_bc_resend.size() > 0) {
            Object[] key_arr = hashMap_bc_resend.keySet().toArray();
            Arrays.sort(key_arr); //顺序排序
            for (Object key : key_arr) {
                AfChapterInfo value = hashMap_bc_resend.get(key);
                if (value.status == AfMessageInfo.MESSAGE_UNSENT) {
                    PalmchatLogUtils.println("getBC_resend, _id=" + value._id);
                    aList.add(0, hashMap_bc_resend.get(key));
                }
            }
        }
        return aList;
    }

    public int getFailure_BC_ListAfMFile_count(List<AfMFileInfo> picturePathList) {
        int count = 0;
        if (picturePathList.size() > 0) {
            for (int i = 0; i < picturePathList.size(); i++) {
                AfMFileInfo afMFileInfo = picturePathList.get(i);
                if (afMFileInfo.status != AfMessageInfo.MESSAGE_SENT) {
                    count++;
                }
            }
        }
        return count;
    }

    private ArrayList<AfChapterInfo> BC_RecordSendSuccessData = new ArrayList<AfChapterInfo>();

    public void setBC_RecordSendSuccessData(AfChapterInfo afChapterInfo) {
        BC_RecordSendSuccessData.add(0, afChapterInfo);
    }

    public ArrayList<AfChapterInfo> getBC_RecordSendSuccessData() {
        return BC_RecordSendSuccessData;
    }

    /**
     * 清除所有自己已发送成功的广播列表
     */
    public void remove_BC_RecordSendSuccessData_All() {
        BC_RecordSendSuccessData.clear();
    }

    public void remove_BC_RecordSendSuccessDataBy_mid(String mid) {
        int size = BC_RecordSendSuccessData.size();
        ArrayList<AfChapterInfo> remove_data = new ArrayList<AfChapterInfo>();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                AfChapterInfo afInfo = BC_RecordSendSuccessData.get(i);
                if (afInfo.mid.equals(mid)) {
//					BC_RecordSendSuccessData.remove(i);
                    remove_data.add(afInfo);

                }
            }
            BC_RecordSendSuccessData.removeAll(remove_data);
        }
    }

    public void remove_BC_RecordSendSuccessDataBy_lat_lon(final double lat, final double lon) {
        final String Systemdatatime = CacheManager.getInstance().getsendTime(System.currentTimeMillis());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int size = BC_RecordSendSuccessData.size();
                ArrayList<AfChapterInfo> remove_data = new ArrayList<AfChapterInfo>();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        AfChapterInfo afInfo = BC_RecordSendSuccessData.get(i);
                        if (!(roundDouble(lat, 2).equals(roundDouble(afInfo.lat, 2)) || roundDouble(lon, 2).equals(roundDouble(afInfo.lng, 2)))) {
                            remove_data.add(afInfo);//经纬度变动掉本地缓存
                            PalmchatLogUtils.e("remove_BC_RecordSendSuccessData", "afInfo.lat=" + afInfo.lat + ",afInfo.lng=" + afInfo.lng);
                        } else {
                            String datatime = afInfo.time;
                            if (!TextUtils.isEmpty(datatime)) {
                                if (DateUtil.compare_date(datatime, Systemdatatime)) {
                                    remove_data.add(afInfo);//一分钟去掉本地缓存
                                    PalmchatLogUtils.e("remove_BC_RecordSendSuccessData", afInfo.time);
                                }
                            }
                        }
                    }
                    BC_RecordSendSuccessData.removeAll(remove_data);
                }
            }
        }).start();
    }

    public static Double roundDouble(double val, int precision) {
        Double ret = null;
        try {
            double factor = Math.pow(10, precision);
            ret = Math.floor(val * factor + 0.5) / factor;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static Double roundDouble(String val, int precision) {
        Double ret = null;
        if (!TextUtils.isEmpty(val)) {
            double val2 = Double.valueOf(val);
            try {
                double factor = Math.pow(10, precision);
                ret = Math.floor(val2 * factor + 0.5) / factor;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    /**
     * record if show chatting(groupchat) ui voice layout when enter chatting(groupchat).
     */
    private HashMap<String, Boolean> mChattingMap = new HashMap<String, Boolean>();

    /**
     * @param key           myAfid + afid or gid.
     * @param showVoiceView if show voice view.
     */
    public void putShowChattingVoice(String key, boolean showVoiceView) {
        if (key != null) {
            mChattingMap.put(key, showVoiceView);
        }
    }

    /**
     * get the show voice view state of chatting, default is true.
     *
     * @param key
     * @return
     */
    public boolean getShowChattingVoice(String key) {
        if (mChattingMap.containsKey(key)) {
            return mChattingMap.get(key);
        } else {
            return true;
        }
    }
}
