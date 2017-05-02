package com.afmobi.palmchat.ui.activity.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.eventbusmodel.EventFollowNotice;
import com.afmobi.palmchat.eventbusmodel.EventRefreshFriendList;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobi.palmchat.listener.OnItemClick;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.afmobi.palmchat.ui.activity.profile.AdapterBroadcastProfile.IAdapterBroadcastProfile;
import com.afmobi.palmchat.ui.customview.AbuseDialog;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.AppDialog.OnInputDialogListener;
import com.afmobi.palmchat.ui.customview.AppFunctionTips_pop_Utils;
import com.afmobi.palmchat.ui.customview.InnerListView;
import com.afmobi.palmchat.ui.customview.MyScrollView;
import com.afmobi.palmchat.ui.customview.PhotoWallView;
import com.afmobi.palmchat.ui.customview.ScrollViewListener;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager.SystemBarConfig;
import com.afmobi.palmchat.ui.customview.photos.WallEntity;
import com.afmobi.palmchat.ui.customview.videoview.CustomVideoController;
import com.afmobi.palmchat.ui.customview.videoview.VedioManager;
import com.afmobi.palmchat.util.ByteUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfDatingInfo;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfGrpProfileInfo.AfGrpProfileItemInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfPeoplesChaptersList;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import de.greenrobot.event.EventBus;

/**
 * 个人资料
 */
public class ProfileActivity extends BaseActivity implements OnClickListener, AfHttpResultListener, OnItemClick, ScrollViewListener {

    private static String TAG = ProfileActivity.class.getSimpleName();
    public static final String STRANGER = "stranger";
    public static final String FRIENDS = "friends";
    public static final String OWNER = "owner";
    public static final String BLOCK = "block";
    public static final int DEF_TO_PROFILE = 0x01;
    public static final int HOME_TO_PROFILE = 0x02;
    public static final int BC_TO_PROFILE = 0x03;
    public static final int CHAT_TO_PROFILE = 0x04;
    public static final int FRIENDS_TO_PROFILE = 0x05;
    public static final int GPCHAT_TO_PROFILE = 0x06;
    private static final int NODTIFYDATASETCHANGED = 111;
    private AfPalmchat mAfPalmchat;
    public AfProfileInfo afProfileInfo;
    private ImageView img_follow, sendgift_gif;
    /**
     * 详情布局最外层scrollview
     */
    private MyScrollView mScrollView;
    /**
     * 广播列表最外层布局
     */
    private View rl_broadcast;
    private int lastItemIndex;// 当前ListView中最后一个Item的索引
    private int firstItem;
    // private MyImageView profile_background_image;
    private TextView viewRegionValue;
    private TextView viewAliasValue;
    private PhotoWallView photoWallView;
    private List<String> serials;
    private TextView textNameValue, textNameValue2;
    private TextView textAfidValue, textAfidValue2;
    private ImageView imageHeadValue, imageHeadValue2, attr_start;
    private View attrLayout;
    ;
    private TextView textAgeValue, textAgeValue2;
    /**
     * 解除黑名单 button
     */
    private ViewGroup rl_unblock;

    /**
     * 底部聊天与加好友布局
     */
    private View lin_tab_bottom;
    private TextView textchat;
    private TextView textFollowing, textFollowing2;
    private TextView textFollowers, textFollowers2;
    private TextView textAdd;
    private Intent intent;
    private String mFriendAfid, mFriendName, mAlias;
    private AfPalmchat mAfCorePalmchat;
    private ArrayList<AfMessageInfo> listChats = new ArrayList<AfMessageInfo>();
    private String afid;
    private int from = 1;
    private LargeImageDialog largeImageDialog;
    private TextView albumTitle;
    private View viewAlias;
    /**
     * 学校
     */
    boolean isFriend;
    boolean isFollow;
    private String lastAlias;
    private boolean isAgree;
    private SystemBarTintManager tintManager;
    private int StatusBarAlpha = 125;
    /**
     * 标题栏
     */
    private View lin_title;
    /**
     * 整个布局最外层控件
     */
    private View r_paofile;
    /**
     * 标题栏最外层布局
     */
    private View relativelayout_title;
    private LinearLayout lin_tab_chat, lin_tab_follow;
    private ImageView vImageViewRight;
    /**
     * 头像被Blocked
     */
    private ImageView mIv_BlockedFlag, mIv_BlockedFlag2;
    private ProgressBar progressBar;
    int alpha;
    int ScrollY = 0;
    public static final int PROFILEACTIVITY = 10011;
    private AdapterBroadcastProfile adapter;
    private InnerListView mListview;
    private View lin_profile, lin_profile2, lin_broadcast, lin_broadcast2;
    private int pageid = 0;
    private int START_INDEX = 0;
    /**
     * 是否正在从服务器获取数据
     */
    private boolean LoadingData = false;
    private boolean isLoadMore;
    private static final int LIMIT = 10;
    /**
     * 无数据时
     */
    public View rl_no_data;
    private LooperThread looperThread;
    int StatusBarHeight = 0;
    private boolean mIsBloaked;
    private TextView viewWhatsupValue;
    /**
     * 详情页面头部控件
     */
    private View head_detail_view;
    /**
     * 广播页面头部控件
     */
    private View head_broadcast_view;
    /**
     * 是否从chattingRoom进入
     */
    private boolean isFromChattingRoom;
    /**
     * 更换背景控件
     */
    private View rl_add_bg;
    /**
     * 头部添加背景控件的高度
     */
    private int addBg_H = 0;
    /**
     * 头部View的高度
     */
    private int head_h = 0;

    private View lin_profile_suspension;
    private View lin_tab_suspension_linearLayout;

    /*当前window大小*/
    private Rect rect;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth = p.x;
        int screenHeight = p.y;
        rect = new Rect(0, 0, screenWidth, screenHeight);

        setNavigationBar();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!isFinishing()) {
                    try {
                        AppFunctionTips_pop_Utils.getInit().showFunctionTips_pop(ProfileActivity.this, lin_broadcast, R.string.profile_bc_functiontips, R.drawable.bg_functiontips_checkbroadcast);
                    } catch (Exception e) {
                    }
                }
            }
        }, 300);
        showTitle(alpha, true);
        isPaused = false;
    }

    private boolean isPaused;

    @Override
    protected void onPause() {
        super.onPause();
        showTitle(0xFF, false);
        isPaused = true;
           /*停止当前正在播放的视频*/
        if (VedioManager.getInstance().getVideoController() != null) {
            VedioManager.getInstance().getVideoController().pause();
        }

        LoadingData = false;
        VoiceManager.getInstance().completion();
    }


    @Override
    protected void onStop() {
        super.onStop();
                /*停止当前正在播放的视频*/
        if (VedioManager.getInstance().getVideoController() != null) {
            VedioManager.getInstance().getVideoController().stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showTitle(255, false);

        EventBus.getDefault().unregister(this);
        looperThread.looper.quit();
    }

    @Override
    public void findViews() {
        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        setContentView(R.layout.activity_profile);
        /** 初始详情化头部控件 */
        head_detail_view = findViewById(R.id.layout_profile_top);
        textNameValue = (TextView) head_detail_view.findViewById(R.id.profile_text_name);
        textAfidValue = (TextView) head_detail_view.findViewById(R.id.profile_text_afid);
        textAgeValue = (TextView) head_detail_view.findViewById(R.id.text_age);
        imageHeadValue = (ImageView) head_detail_view.findViewById(R.id.img_photo);
        mIv_BlockedFlag = (ImageView) head_detail_view.findViewById(R.id.blocked_flag);
        textFollowing = (TextView) head_detail_view.findViewById(R.id.following);
        textFollowers = (TextView) head_detail_view.findViewById(R.id.followers);
        lin_profile = head_detail_view.findViewById(R.id.lin_profile);
        lin_broadcast = head_detail_view.findViewById(R.id.lin_broadcast);
        ((TextView) head_detail_view.findViewById(R.id.txt_broadcast)).setText(getString(R.string.option_broadcast));
        rl_add_bg = head_detail_view.findViewById(R.id.rl_add_bg);
        head_detail_view.findViewById(R.id.my_qrcode).setVisibility(View.GONE);

        /** 初始化广播页面 */
        rl_broadcast = findViewById(R.id.rl_broadcast);
        mListview = (InnerListView) findViewById(R.id.listview);
        rl_no_data = findViewById(R.id.rl_no_data);

        /** 初始广播头部控件 */
        head_broadcast_view = mListview.getHeadView();
        textNameValue2 = (TextView) head_broadcast_view.findViewById(R.id.profile_text_name);
        textAfidValue2 = (TextView) head_broadcast_view.findViewById(R.id.profile_text_afid);
        textAgeValue2 = (TextView) head_broadcast_view.findViewById(R.id.text_age);
        imageHeadValue2 = (ImageView) head_broadcast_view.findViewById(R.id.img_photo);
        mIv_BlockedFlag2 = (ImageView) head_broadcast_view.findViewById(R.id.blocked_flag);
        textFollowing2 = (TextView) head_broadcast_view.findViewById(R.id.following);
        textFollowers2 = (TextView) head_broadcast_view.findViewById(R.id.followers);
        lin_profile2 = head_broadcast_view.findViewById(R.id.lin_profile);
        lin_broadcast2 = head_broadcast_view.findViewById(R.id.lin_broadcast);
        ((TextView) head_broadcast_view.findViewById(R.id.txt_broadcast)).setText(getString(R.string.option_broadcast));
        head_broadcast_view.findViewById(R.id.my_qrcode).setVisibility(View.GONE);
        /** 初始化标题 */
        ((TextView) findViewById(R.id.title_text)).setText(R.string.profile);
        // profile_background_image = (MyImageView)
        // findViewById(R.id.personal_background_image);
        lin_title = findViewById(R.id.lin_title);
        r_paofile = findViewById(R.id.r_paofile);
        progressBar = (ProgressBar) findViewById(R.id.img_loading);
        vImageViewRight = (ImageView) findViewById(R.id.op2);
        vImageViewRight.setBackgroundResource(R.drawable.navigation);
        /** 初始化详情 */
        viewWhatsupValue = (TextView) findViewById(R.id.profile_whatsup_value);
        viewAlias = findViewById(R.id.profile_alias_layout);
        viewRegionValue = (TextView) findViewById(R.id.profile_region_value);
        viewAliasValue = (TextView) findViewById(R.id.profile_alias_value);
        mScrollView = (MyScrollView) findViewById(R.id.sl_detail);
        /** 初始化相册 */
        ViewStub view_stub = (ViewStub) findViewById(R.id.viewstub);
        view_stub.inflate();
        attrLayout = findViewById(R.id.attr_layout);
        albumTitle = (TextView) findViewById(R.id.attr_title);
        attr_start = (ImageView) findViewById(R.id.attr_start);
        attr_start.setVisibility(View.GONE);
        attr_start.setTag(0);
        photoWallView = (PhotoWallView) findViewById(R.id.photo_wall_view);
        rl_unblock = (ViewGroup) findViewById(R.id.rl_unblock);

        /** 初始化底部 */
        textAdd = (TextView) findViewById(R.id.txt_Add);
        textchat = (TextView) findViewById(R.id.txt_chat);
        View backView = findViewById(R.id.back_button);
        backView.setBackgroundResource(R.drawable.profile_back_button);
        relativelayout_title = findViewById(R.id.relativelayout_title);
        img_follow = (ImageView) findViewById(R.id.img_follow);
        img_follow.setBackgroundResource(R.drawable.btn_follow_style);
        lin_tab_chat = (LinearLayout) findViewById(R.id.lin_tab_chat);
        lin_tab_follow = (LinearLayout) findViewById(R.id.lin_tab_follow);
        lin_tab_bottom = findViewById(R.id.lin_tab_bottom);

        lin_tab_suspension_linearLayout = findViewById(R.id.lin_tab_suspension);
        lin_profile_suspension = findViewById(R.id.lin_profile_suspension);
        lin_profile_suspension.setOnClickListener(this);

        rl_unblock.setOnClickListener(this);
        vImageViewRight.setOnClickListener(this);
        lin_tab_chat.setOnClickListener(this);
        lin_tab_follow.setOnClickListener(this);
        lin_profile.setOnClickListener(this);
        lin_profile2.setOnClickListener(this);
        lin_broadcast.setOnClickListener(this);
//		lin_broadcast2.setOnClickListener(this);
        textchat.setOnClickListener(this);
        viewAlias.setOnClickListener(this);
        textAdd.setOnClickListener(this);
        imageHeadValue.setOnClickListener(this);
        imageHeadValue2.setOnClickListener(this);
        backView.setOnClickListener(this);
        mScrollView.setScrollViewListener(this);

    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        lin_profile.performClick();
        setAdapter(new ArrayList<AfChapterInfo>());
        mListview.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                PalmchatLogUtils.i(TAG, "--fffc onScrollStateChanged");
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == adapter.getCount() - 1 && firstItem != 0) {
                    PalmchatLogUtils.i(TAG, "--fffc onScrollStateChanged scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == adapter.getCount() - 1 LoadingData " + LoadingData);
                    // 加载数据代码，此处省略了
                    if (!LoadingData) {
                        loadmore();
                        PalmchatLogUtils.i(TAG, "--fffc loadmore");
                    }
                }

                Log.i("ProfileActivity_zhh", "onScrollStateChanged: " + view.getScrollY());
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (VedioManager.getInstance().getVideoController() != null) {
                    if (!VedioManager.getInstance().getVideoController().getVisibleRect(rect)) {
                        VedioManager.getInstance().getVideoController().stop();
                    }
                }

                if (firstVisibleItem >= 1 && rl_broadcast.getVisibility() == View.VISIBLE) {
                    lin_tab_suspension_linearLayout.setVisibility(View.VISIBLE);
                    isShowTab_suspension = true;
                } else {
                    lin_tab_suspension_linearLayout.setVisibility(View.GONE);
                    isShowTab_suspension = false;
                }
                // ListView 的FooterView和headview也会算到visibleItemCount中去，所以要再减去一
                lastItemIndex = firstVisibleItem + visibleItemCount - 1 - 1 - 1;
                firstItem = firstVisibleItem;
                Log.i(TAG, "--fffc onScroll lastItemIndex=" + lastItemIndex + ",firstItem=" + firstItem);

                int y = mListview.defGetScrollY();
                alpha = y = (int) Math.abs(y);
                int subAddBg_H = (int) Math.abs(addBg_H);
                if (y <= subAddBg_H) {
                    if (alpha > 255) {
                        alpha = 255;
                    }

                } else {
                    alpha = 255;

                }
                if (!isPaused) {//OnPause后 这里竟然会被调用 奇怪
                    showTitle(alpha, true);
                }
                /*relativelayout_title.getBackground().setAlpha(alpha);
                relativelayout_title.postInvalidate();*/

                if (tintManager != null) {
                    int tempAlpha = StatusBarAlpha;
                    if (tempAlpha >= 255) {
                        tempAlpha = 255;
                    } else {
                        tempAlpha = StatusBarAlpha + alpha;
                        if (tempAlpha > 255) {
                            tempAlpha = 255;
                        }
                    }
                    PalmchatLogUtils.e(">>>>>>tempAlpha<=125<<<<<<<<", "tempAlpha=" + tempAlpha);
                    tintManager.setStatusBarAlpha(tempAlpha);
                }

                //
                // int y = mListview.defGetScrollY();
                // alpha = (int) Math.abs(y);
                // int max = (int) Math.max(y, addBg_H);
                // if (alpha <= 255) {
                // if (alpha > max) {
                // alpha = 255;
                // }
                // relativelayout_title.getBackground().setAlpha(alpha);
                // relativelayout_title.postInvalidate();
                // if (tintManager != null) {
                // int tempAlpha = StatusBarAlpha;
                // if (alpha >= max) {
                // tempAlpha = 255;
                // } else {
                // tempAlpha = StatusBarAlpha + alpha;
                // if (tempAlpha > 255) {
                // tempAlpha = 255;
                // }
                // }
                // PalmchatLogUtils.e(">>>>>>tempAlpha<=125<<<<<<<<",
                // "tempAlpha=" + tempAlpha);
                // tintManager.setStatusBarAlpha(tempAlpha);
                // }
                //
                // }

            }
        });

        looperThread = new LooperThread();
        looperThread.setName(TAG);
        looperThread.start();
        mAfPalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        afProfileInfo = (AfProfileInfo) getIntent().getSerializableExtra(JsonConstant.KEY_PROFILE);
        afid = getIntent().getStringExtra(JsonConstant.KEY_AFID);
        from = getIntent().getIntExtra(JsonConstant.KEY_FROM_XX_PROFILE, 1);
        isAgree = getIntent().getBooleanExtra("isAgree", false);
        intent = getIntent();
        isFromChattingRoom = intent.getBooleanExtra(JsonConstant.KEY_FROM_CHATTINGROOM_TO_PROFILE, false);
        isFriend = CacheManager.getInstance().isFriend(afid);
        /** 获取profile信息 */
        afGetDBProfile();
        isFollow = CommonUtils.isFollow(afid);
        isFriends();

        final AfFriendInfo afF = CacheManager.getInstance().findAfFriendInfoByAfId(afid);
        if (afF != null) {
            lin_tab_follow.setVisibility(View.GONE);
        }

        if (afF == null) {// not my friend
            String my_afid = CacheManager.getInstance().getMyProfile().afId;
            if (CommonUtils.isOverOneDay(context, my_afid, mFriendAfid)) {
                lin_tab_follow.setVisibility(View.VISIBLE);
                set_addFriend_clickable(afid, Consts.HTTP_CODE_UNKNOWN);
            }
        }
        if (afProfileInfo != null) {
            setContent(afProfileInfo);
            Handler looperHandler = looperThread.looperHandler;
            if (null != looperThread.looperHandler) {
                looperThread.looperHandler.obtainMessage(LooperThread.UPDATE_FRIEND_LIST, afProfileInfo).sendToTarget();
            }
        }
        // 判断是否已经加入黑名单
        if (afProfileInfo != null && null != CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_BF).search(afProfileInfo, false, true)) {
            mIsBloaked = true;
            mIv_BlockedFlag.setVisibility(View.VISIBLE);
            mIv_BlockedFlag2.setVisibility(View.VISIBLE);
            lin_tab_bottom.setVisibility(View.GONE);
            rl_unblock.setVisibility(View.VISIBLE);
            setGrayImage();
        } else {
            mIsBloaked = false;
            lin_tab_bottom.setVisibility(View.VISIBLE);
            rl_unblock.setVisibility(View.GONE);
            imageHeadValue.setColorFilter(null);
            imageHeadValue2.setColorFilter(null);
        }
    }

    /**
     * 当页面加载完成后获取控件高度
     */
    boolean measure;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !measure) {
            measure = true;
            addBg_H = rl_add_bg.getHeight();
            head_h = head_detail_view.getHeight();

            Drawable drawable = FileUtils.getImageFromAssetsFile2(context, "01", head_h);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                head_detail_view.setBackgroundDrawable(drawable);
                head_broadcast_view.setBackgroundDrawable(drawable);
            } else {
                head_detail_view.setBackground(drawable);
                head_broadcast_view.setBackground(drawable);
            }
        }

    }

    /**
     * 到数据库取数据
     */
    private void afGetDBProfile() {
        final Handler handler = new Handler(new Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.obj != null) {
                    AfProfileInfo profile = (AfProfileInfo) msg.obj;
                    PalmchatLogUtils.i(TAG, "search profile alias = " + profile.alias);
                    if(profile != null &&!TextUtils.isEmpty(  profile.name)  ){
                        afProfileInfo = profile;
                        int ty = afProfileInfo.type;
                        PalmchatLogUtils.d("==", "getDBprofile:" + ty);
                        setContent(profile);
                    }
                }
                if (mAfPalmchat != null && afid != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    mAfPalmchat.AfHttpGetInfo(new String[]{afid}, Consts.REQ_GET_INFO, null, null, ProfileActivity.this);
                }
                return false;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                AfProfileInfo profile = (AfProfileInfo) mAfPalmchat.AfDbProfileGet(Consts.AFMOBI_DB_TYPE_PROFILE, afid);
                handler.obtainMessage(1, profile).sendToTarget();
            }
        }).start();
    }

    /**
     * 给控件赋值参数
     *
     * @param profileInfo 个人信息
     */
    public void setContent(AfProfileInfo profileInfo) {
        if (profileInfo != null) {
            this.afProfileInfo = profileInfo;
            textAfidValue.setText(getAfid(profileInfo.showAfid()));
            textAfidValue2.setText(getAfid(profileInfo.showAfid()));
            textNameValue.setText(profileInfo.name);
            textNameValue2.setText(profileInfo.name);
            textAgeValue.setText(profileInfo.age + "");
            textAgeValue.setBackgroundResource(Consts.AFMOBI_SEX_MALE == afProfileInfo.sex ? R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
            textAgeValue.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == afProfileInfo.sex ? R.drawable.icon_sexage_boy : R.drawable.icon_sexage_girl, 0, 0, 0);
            textAgeValue2.setText(profileInfo.age + "");
            textAgeValue2.setBackgroundResource(Consts.AFMOBI_SEX_MALE == afProfileInfo.sex ? R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
            textAgeValue2.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == afProfileInfo.sex ? R.drawable.icon_sexage_boy : R.drawable.icon_sexage_girl, 0, 0, 0);
            // WXL 20151013 调UIL的显示头像方法,统一图片管理
            ImageManager.getInstance().DisplayAvatarImage(imageHeadValue, afProfileInfo.getServerUrl(), afProfileInfo.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, afProfileInfo.sex, afProfileInfo.getSerialFromHead(), null);
            // WXL 20151013 调UIL的显示头像方法,统一图片管理
            ImageManager.getInstance().DisplayAvatarImage(imageHeadValue2, afProfileInfo.getServerUrl(), afProfileInfo.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, afProfileInfo.sex, afProfileInfo.getSerialFromHead(), null);
            textFollowing.setText(profileInfo.follow_count + "");
            textFollowing2.setText(profileInfo.follow_count + "");

            int marital_status = profileInfo.dating.marital_status;
            textFollowers.setText(profileInfo.followers_count + "");
            textFollowers2.setText(profileInfo.followers_count + "");
            String showHobby = profileInfo.getShowHobby(this);
            String showProfession = profileInfo.getShowProfession(this);
            displayRegion(viewRegionValue, profileInfo.country, profileInfo.region, profileInfo.city);
            String showReligion = profileInfo.getShowReligion(this);
            String sign = CommonUtils.isEmpty(profileInfo.signature) ? getString(R.string.default_status) : profileInfo.signature;
            CharSequence text = EmojiParser.getInstance(context).parse(sign, ImageUtil.dip2px(context, 18));
            lastAlias = profileInfo.alias;
            viewWhatsupValue.setText(text);
//			if (isFriend) {
//				viewAlias.setVisibility(View.VISIBLE);
//				viewAliasValue.setText(lastAlias);
//			} else {
//				viewAlias.setVisibility(View.GONE);
//			}
            /** 初始化相册 */
            initSerials();
        } else {
            textAfidValue.setText(afid);
            textAfidValue2.setText(afid);
            textNameValue.setText(afid);
            textNameValue2.setText(afid);
            textAgeValue.setText("0");
            textAgeValue2.setText("0");
        }

    }

    List<WallEntity> entities = new ArrayList<WallEntity>();

    /**
     * 图片墙赋值显示
     */
    private void initSerials() {
        entities.clear();
        serials = (afProfileInfo != null ? afProfileInfo.getSerials() : new ArrayList<String>());
        for (String sn : serials) {
            entities.add(new WallEntity(afProfileInfo.afId, sn, afProfileInfo.getServerUrl()));
        }

        if (serials.size() > 0) {// 判断图片墙是否有数据
            albumTitle.setText(getString(R.string.album) + "(" + serials.size() + ")");
            attrLayout.setVisibility(View.VISIBLE);

        } else {
            attrLayout.setVisibility(View.GONE);
        }
        photoWallView.setData(this, entities, afProfileInfo);
        //
        // if (serials.size() > 0) {// 判断图片墙是否有数据
        // if (lin_broadcast2.isSelected()) {
        // view_stub.setVisibility(View.GONE);
        // } else {
        // view_stub.setVisibility(View.VISIBLE);
        // photoWallView.setData(this, entities, afProfileInfo);
        // }
        // } else {
        // view_stub.setVisibility(View.GONE);
        // }

    }

    /**
     * 设置国家地区
     *
     * @param view
     * @param country
     * @param state
     * @param city
     */
    private void displayRegion(TextView view, String country, String state, String city) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(city)) {
            if (!DefaultValueConstant.OTHER.equals(city)) {
                sb.append(city);
            }
        }
        if (!TextUtils.isEmpty(state)) {
            if (sb.length() == 0 && !DefaultValueConstant.OTHERS.equals(state)) {
                sb.append(state);
            } else if (!DefaultValueConstant.OTHERS.equals(state)) {
                sb.append(",").append(state);
            }
        }
        if (!TextUtils.isEmpty(country)) {
            if (sb.length() == 0 && !DefaultValueConstant.OVERSEA.equals(country)) {
                sb.append(country);
            } else if (!DefaultValueConstant.OVERSEA.equals(country)) {
                sb.append(",").append(country);
            }
        }
        view.setText(sb.toString());
    }

    private String getAfid(String afid) {
        return !TextUtils.isEmpty(afid) ? "Palm ID:" + afid : "";
    }

    /**
     * 取消与中间件的关联
     */
    // private SparseArray<Integer> mhttpHandle;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NODTIFYDATASETCHANGED:
                    if (LoadingData) {
                        LoadingData = false;
                    }
                    List<AfChapterInfo> broadcastMessageList = (List<AfChapterInfo>) msg.obj;
                    adapter.notifyDataSetChanged(broadcastMessageList, isLoadMore);
                    break;
                case Constants.DB_DELETE: {
                    ToastManager.getInstance().show(context, getString(R.string.add_friend_req));
                    dismissProgressDialog();

                    String my_afid = CacheManager.getInstance().getMyProfile().afId;
                    SharePreferenceUtils.getInstance(context).setFriendReqTime(my_afid, mFriendAfid);
                    lin_tab_follow.setVisibility(View.GONE);
                }
                break;
                case Constants.TO_REFRESH_FRIEND_LIST: {

			/*	Intent intent = new Intent();
                intent.setAction(Constants.REFRESH_FRIEND_LIST_ACTION);
				context.sendBroadcast(intent);*/
                    EventBus.getDefault().post(new EventRefreshFriendList());
                /*Intent intent2 = new Intent();
                intent2.setAction(Constants.REFRESH_CHATS_ACTION);
				context.sendBroadcast(intent2);*/
                    EventBus.getDefault().post(new RefreshChatsListEvent());

                    dismissProgressDialog();

                    AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
                    CommonUtils.getRealList(listChats, afMessageInfo);
                    final AfFriendInfo afF = CacheManager.getInstance().findAfFriendInfoByAfId(afid);
                    if (afF != null) {
                        vImageViewRight.setBackgroundResource(R.drawable.t_profile);
                        String aa = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, afF.name, context.getString(R.string.frame_toast_friend_req_success));
                        ToastManager.getInstance().show(context, aa);
                    }
                    lin_tab_follow.setVisibility(View.GONE);
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 从服务器获取广播数据
     */
    private void loadData() {
        isLoadMore = false;
        START_INDEX = 0;
        pageid = (int) System.currentTimeMillis() + new Random(10000).nextInt();
        loadDataFromServer(pageid);
        mListview.show_loadmore();
        if (adapter.getCount() < 1) {
            if (rl_no_data != null) {
                rl_no_data.setVisibility(View.GONE);
            }
        }

    }

    private void loadDataFromServer(int page_id) {
        mAfPalmchat.AfHttpBcgetProfileByAfid(page_id, START_INDEX * LIMIT, LIMIT, afid, null, this);
        LoadingData = true;
    }

    private void loadmore() {
        mListview.show_loadmore();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                isLoadMore = true;
                START_INDEX++;
                loadDataFromServer(pageid);
            }
        }, 200);
    }

    private void setAdapter(final List<AfChapterInfo> broadcastMessageList) {
        if (null == adapter) {
            if (afProfileInfo == null) {
                afProfileInfo = (AfProfileInfo) getIntent().getSerializableExtra(JsonConstant.KEY_PROFILE);
            }
            adapter = new AdapterBroadcastProfile(ProfileActivity.this, broadcastMessageList, PROFILEACTIVITY, this, afProfileInfo);
            adapter.setIAdapterBroadcastMessages_profile(new IAdapterBroadcastProfile() {

                @Override
                public void on_showNoData() {
                    showNoData();
                }
            });
            mListview.setAdapter(adapter);
        } else {
            final int size = broadcastMessageList.size();
            if (size > 0) {
                Handler looperHandler = looperThread.looperHandler;
                if (null != looperHandler) {
                    Message msg = new Message();
                    msg.obj = broadcastMessageList;
                    msg.what = LooperThread.SETPROFILE_AND_CHECK_ISLIKE;
                    looperHandler.sendMessage(msg);
                }
            } else {
                ToastManager.getInstance().show(context, R.string.no_data);
            }
        }

    }

    private void stopLoad() {
        LoadingData = false;
        mListview.hide_loadmore();
    }

    /**
     * 设置标题栏的透明度
     *
     * @param
     */
    public void showTitle(int alphaValue, boolean isSaveAlpha) {

        if (relativelayout_title != null) {

            if (alphaValue != -1) {
                if (alphaValue > 255)
                    alphaValue = 255;

                relativelayout_title.getBackground().setAlpha(alphaValue);
            } else {
                if (alpha > 255)
                    alpha = 255;
                relativelayout_title.getBackground().setAlpha(alpha);

            }

            if (isSaveAlpha)
                alpha = alphaValue;

            relativelayout_title.postInvalidate();
        }
        //
        // if (relativelayout_title != null) {
        // if (alpha2 != -1) {
        // if (alpha2 > 255) {
        // alpha2 = 255;
        // }
        // relativelayout_title.getBackground().setAlpha(alpha2);
        // alpha = alpha2;
        // } else {
        // if (alpha > 255) {
        // alpha = 255;
        // }
        // relativelayout_title.getBackground().setAlpha(alpha);
        // }
        // relativelayout_title.postInvalidate();
        // }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
        arg0.dismiss();
        return false;
    }

    /**
     * 更改了或设置了备注名称
     *
     * @param afid  afid账号
     * @param alias 备注名
     */
    void updateAlias(String afid, String alias) {
        boolean flag = mAfPalmchat.AfDbProfileUpdateAlias(afid, alias);
        PalmchatLogUtils.println("updateAlias  flag  " + flag);
        if (!flag) {
            afProfileInfo.alias = lastAlias;
            viewAliasValue.setText(lastAlias);
            ToastManager.getInstance().show(context, R.string.failed);
            return;
        }
        if (isFriend) {
            afProfileInfo.alias = alias;
            CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).saveOrUpdate(afProfileInfo, false, true);
            // 更改别名后,好友列表重新排序

            CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).remove(afProfileInfo, false, true);
            CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).insert(afProfileInfo, false, true);

            MainAfFriendInfo chat = MainAfFriendInfo.getMainAfFriendInfoFromAfID(afProfileInfo.afId);
            if (chat != null) {
                chat.afFriendInfo.alias = alias;
                CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).saveOrUpdate(chat, false, true);
            }
            EventBus.getDefault().post(new EventRefreshFriendList());
        }
    }

    private boolean isShowTab_suspension;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.op2:
                if (VoiceManager.getInstance().isPlaying()) {
                    VoiceManager.getInstance().pause();
                }


                AbuseDialog abuseDialog = new AbuseDialog(context, AbuseDialog.ACTION_ABUSE, afid, mIsBloaked);
                abuseDialog.setItemClick(ProfileActivity.this);
                abuseDialog.show();
                break;
            case R.id.lin_profile:
            case R.id.lin_profile_suspension:
                if (lin_tab_suspension_linearLayout.getVisibility() == View.VISIBLE) {
                    isShowTab_suspension = true;
                    lin_tab_suspension_linearLayout.setVisibility(View.GONE);
                }
                lin_broadcast2.setSelected(false);
                showSelectedOption(v);
                rl_broadcast.setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);
                if (serials != null && serials.size() > 0) {
                    attrLayout.setVisibility(View.VISIBLE);
                } else {
                    attrLayout.setVisibility(View.GONE);
                }

                  	   /*停止当前正在播放的视频*/
                if (VedioManager.getInstance().getVideoController() != null) {
                    VedioManager.getInstance().getVideoController().stop();
                }
                break;

            case R.id.lin_broadcast:
                lin_broadcast2.requestFocus();
                lin_broadcast2.requestFocusFromTouch();
                lin_broadcast2.setFocusable(false);
                lin_broadcast2.requestFocus();
                showSelectedOption(v);
                if (adapter.getCount() < 1 && !LoadingData) {
                    loadData();
                }
                mScrollView.setVisibility(View.GONE);
                rl_broadcast.setVisibility(View.VISIBLE);
                rl_no_data.setVisibility(View.GONE);
                if (isShowTab_suspension) {
                    lin_tab_suspension_linearLayout.setVisibility(View.VISIBLE);
                }

                  	   /*停止当前正在播放的视频*/
                if (VedioManager.getInstance().getVideoController() != null) {
                    VedioManager.getInstance().getVideoController().stop();
                }
                break;
            case R.id.back_button: {
                finish();
                break;
            }
            case R.id.profile_alias_layout:// 设置或更改好友备注
            {
                final AppDialog appDialog = new AppDialog(this);
                String title = getString(R.string.set_alias);
                String inputMsg = afProfileInfo == null ? "" : afProfileInfo.alias;
                appDialog.createEditDialog(this, title, inputMsg, new OnInputDialogListener() {
                    @Override
                    public void onRightButtonClick(String inputStr) {
                        if (afProfileInfo != null) {
                            afProfileInfo.alias = inputStr;
                            viewAliasValue.setText(inputStr);
                            updateAlias(afProfileInfo.afId, inputStr);
                        }
                    }

                    @Override
                    public void onLeftButtonClick() {

                    }
                });
                appDialog.show();
                break;
            }
            case R.id.img_photo: {
                if (null != afProfileInfo) {
                    String sn = afProfileInfo.getSerialFromHead();
                    if (!StringUtil.isNullOrEmpty(sn)) {
                        String[] serials = {afProfileInfo != null ? sn : null};
                        showTitle(255, false);
                        largeImageDialog = new LargeImageDialog(this, Arrays.asList(serials), afProfileInfo.getServerUrl(), afid, 0, LargeImageDialog.TYPE_AVATAR);
                        largeImageDialog.show();
                        largeImageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                showTitle(alpha, true);
                                setNavigationBar();
                            }
                        });

                    }
                }
                break;
            }
            case R.id.options_two:
            case R.id.lin_tab_chat:
            case R.id.txt_chat:
                // heguiming 2013-12-04
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PF_T_CHAT);
                // MobclickAgent.onEvent(context, ReadyConfigXML.PF_T_CHAT);

                Intent intent = new Intent(context, Chatting.class);
                intent.putExtra(JsonConstant.KEY_FROM_UUID, afid);
                intent.putExtra(JsonConstant.KEY_FLAG, true);
                intent.putExtra(JsonConstant.KEY_FROM_NAME, afProfileInfo != null ? afProfileInfo.name : "");
                intent.putExtra(JsonConstant.KEY_FROM_PROFILE, true);
                if (afProfileInfo != null) {
                    AfFriendInfo friend = AfProfileInfo.profileToFriend(afProfileInfo);
                    intent.putExtra(JsonConstant.KEY_FROM_ALIAS, afProfileInfo.alias);
                    intent.putExtra(JsonConstant.KEY_FRIEND, friend);
                }

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                if (isFromChattingRoom) {// 若从chattingRoom进入profile，则进入chatting时，直接关闭
                    finish();
                }
                break;

            case R.id.rl_unblock: {//解除block人员
                blockDialog();
            }
            break;
            case R.id.lin_tab_follow: // follw
            case R.id.txt_Add: // follw
                if (CommonUtils.isFastClick()) {
                    return;
                }
                PalmchatLogUtils.i("profileadd", "add send");
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CT_ADD_SUCC);
                // MobclickAgent.onEvent(context, ReadyConfigXML.CT_ADD_SUCC);

                String name = CacheManager.getInstance().getMyProfile().name;
                if (!TextUtils.isEmpty(name)) {
                    boolean addFriend_clickable = update_addFriend_clickable(afid, Consts.HTTP_CODE_UNKNOWN);
                    if (addFriend_clickable) {
                        int sendmsg = mAfCorePalmchat.AfHttpSendMsg(afid, System.currentTimeMillis(), getString(R.string.want_to_be_friend).replace(DefaultValueConstant.TARGET_NAME, name), Consts.MSG_CMMD_FRD_REQ, String.valueOf(Consts.MSG_CMMD_FRD_REQ), ProfileActivity.this);
                        // mhttpHandle.put(sendmsg,sendmsg);
                    }
                }
                switch (from) {
                    case HOME_TO_PROFILE:
                        // gtf 2015-5-21
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.HM_T_FOLLOW);
                        // MobclickAgent.onEvent(context, ReadyConfigXML.HM_T_FOLLOW);
                        break;
                    case BC_TO_PROFILE:
                        // gtf 2015-5-21
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BC_T_FOLLOW);
                        // MobclickAgent.onEvent(context, ReadyConfigXML.BC_T_FOLLOW);

                        break;
                    case CHAT_TO_PROFILE:
                        // gtf 2015-5-21
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CH_T_FOLLOW);
                        // MobclickAgent.onEvent(context, ReadyConfigXML.CH_T_FOLLOW);

                        break;
                    case FRIENDS_TO_PROFILE:
                        // gtf 2015-5-21
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CONT_T_FOLLOW);
                        // MobclickAgent.onEvent(context, ReadyConfigXML.CONT_T_FOLLOW);
                        break;
                    case GPCHAT_TO_PROFILE:
                        // gtf 2015-5-21
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.GPCHAT_T_FOLLOW);
                        // MobclickAgent.onEvent(context,
                        // ReadyConfigXML.GPCHAT_T_FOLLOW);
                        break;
                    default:
                        if (isFollow) { // ture is follow /false is unfollow
                            // gtf 2014-11-16
                            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PF_T_FOLLOW);
                            // MobclickAgent.onEvent(context,
                            // ReadyConfigXML.PF_T_FOLLOW);
                        }
                        break;
                }
                if (!TextUtils.isEmpty(afid) && !CacheManager.getInstance().getClickableMap().get(afid + CacheManager.follow_suffix)) {
                    // 判断是否已经关注过了，关注后不执行AddFollw();
                    if (!isFollow && !CacheManager.getInstance().getClickableMap().get(afid + CacheManager.follow_suffix) || isFollow && CacheManager.getInstance().getClickableMap().get(afid + CacheManager.follow_suffix)) {
                        AddFollw(afid);
                    }
                }
                break;

        }
    }

    /**
     * 增加好友
     *
     * @param afid
     * @param code
     * @return
     */
    public boolean update_addFriend_clickable(String afid, int code) {
        if (CacheManager.getInstance().getClickableMap().containsKey(afid + CacheManager.add_friend_suffix)) {
            if (CacheManager.getInstance().getClickableMap().get(afid + CacheManager.add_friend_suffix)) {
                CacheManager.getInstance().getClickableMap().put(afid + CacheManager.add_friend_suffix, false);
            } else {
                CacheManager.getInstance().getClickableMap().put(afid + CacheManager.add_friend_suffix, true);
            }
            set_addFriend_clickable(afid, code);
            return CacheManager.getInstance().getClickableMap().get(afid + CacheManager.add_friend_suffix);
        }
        return true;
    }

    private void set_addFriend_clickable(String afid, int code) {
        PalmchatLogUtils.e("set_addFriend_clickable", code + "");
        if (!CacheManager.getInstance().getClickableMap().containsKey(afid + CacheManager.add_friend_suffix)) {
            CacheManager.getInstance().getClickableMap().put(afid + CacheManager.add_friend_suffix, false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        /*
         * case UPDATE_STATE_CITY: //这个已经不用了 已用EventBus机制 break;
		 */
            case IntentConstant.SHARE_BROADCAST:
                if (data != null) {
                    boolean isSuccess = data.getBooleanExtra(JsonConstant.KEY_SEND_IS_SEND_SUCCESS, false);
                    String tempTipContent = data.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL);
                    String tipContent;
                    if (isSuccess) {
                        tipContent = getResources().getString(R.string.share_friend_success);
                    } else {
                        if (!TextUtils.isEmpty(tempTipContent)) {
                            tipContent = tempTipContent;
                        } else {
                            tipContent = getResources().getString(R.string.share_friend_failed);
                        }
                    }
                    ToastManager.getInstance().showShareBroadcast(this, DefaultValueConstant.SHARETOASTTIME, isSuccess, tipContent);
                }
                break;
        }
    }

    boolean sendSuccess;

    /**
     * 请求接口返回来的参数数据
     *
     * @param httpHandle
     * @param flag       你调用那个接口返回对应的参数
     * @param code       请求是否成功
     * @param http_code
     * @param result     返回来的数据
     * @param user_data  自己发什么数据就返回什么数据
     */
    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, final Object user_data) {
        PalmchatLogUtils.println("profile AfOnResult, flag = " + flag + ",code=" + code);
        if (code == Consts.REQ_CODE_SUCCESS) {// 请求数据成功
            PalmchatLogUtils.d(TAG, "=====:" + flag);
            if (flag == Consts.REQ_MSG_SEND) {// 发送请求好友
                // 请求增加为好友
                if (user_data instanceof String) {
                    final String cmd = (String) user_data;
                    int req = Integer.parseInt(cmd);
                    AfPalmchat core = PalmchatApp.getApplication().mAfCorePalmchat;
                    if (req == Consts.MSG_CMMD_FRD_REQ) {
                        update_addFriend_clickable(afid, code);
                        if (mIsBloaked) {
                            MessagesUtils.onUnBlockSuc(afProfileInfo);// 进行数据库和缓存列表操作
                        }
                        handler.obtainMessage(Constants.DB_DELETE).sendToTarget();
                        AfMessageInfo afMessageInfo = MessagesUtils.addMsg2Chats(core, afid, MessagesUtils.ADD_CHATS_FRD_REQ_SENT);
                        sendFriendReqOnResultBroadcast(code, afid, req);
                    } else {
                        int opr = core.AfHttpFriendOpr("all", afid, Consts.HTTP_ACTION_A, Consts.FRIENDS_MAKE, Consts.AFMOBI_FRIEND_TYPE_FF, null, req, this);
                    }
                }
            } else if (flag == Consts.REQ_FRIEND_LIST) {
                int req = (Integer) user_data;
                AfPalmchat core = PalmchatApp.getApplication().mAfCorePalmchat;
                if (req == Consts.MSG_CMMD_AGREE_FRD_REQ) {
                    MainAfFriendInfo aff = MainAfFriendInfo.getFreqInfoFromAfID(afid);
                    new_friend_insert(core, aff);
                } else {
                    update_addFriend_clickable(afid, code);
                    handler.sendMessage(handler.obtainMessage(Constants.DB_DELETE));
                    AfPalmchat afPalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
                    AfMessageInfo afMessageInfo = MessagesUtils.addMsg2Chats(afPalmchat, afid, MessagesUtils.ADD_CHATS_FRD_REQ_SENT);
                    CommonUtils.getRealList(listChats, afMessageInfo);
                }
                sendFriendReqOnResultBroadcast(code, afid, req);
            } else if (flag == Consts.REQ_GET_INFO) {// 获取个人信息
                if (result != null && result instanceof AfProfileInfo) {
                    AfProfileInfo afInfo = (AfProfileInfo) result;
                    updateProfile(afInfo);
                    progressBar.setVisibility(View.GONE);
                    vImageViewRight.setVisibility(View.VISIBLE);
                }
            } else if (flag == Consts.REQ_FRIEND_LIST) {
                if (user_data instanceof AfFriendInfo) {
                    new Thread() {
                        public void run() {
                            AfFriendInfo friendInfo = (AfFriendInfo) user_data;
                            MessagesUtils.onDelFriendSuc(friendInfo);

                            final AfMessageInfo[] recentDataArray = mAfPalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, friendInfo.afId, 0, Integer.MAX_VALUE);
                            if (null != recentDataArray && recentDataArray.length > 0) {
                                for (AfMessageInfo messageInfo : recentDataArray) {
                                    mAfPalmchat.AfDbMsgRmove(messageInfo);
                                    MessagesUtils.removeMsg(messageInfo, true, true);
                                }
                                mAfPalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, friendInfo.afId);

                            }

                            mHandler.obtainMessage(Constants.DB_DELETE_FRIEND, friendInfo).sendToTarget();

                        }

                    }.start();
                } else {
                    final String afid = (String) user_data;
                    final AfPalmchat core = PalmchatApp.getApplication().mAfCorePalmchat;
                    final MainAfFriendInfo aff = MainAfFriendInfo.getFreqInfoFromAfID(afid);
                    if (aff != null) {
                        String aa = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, aff.afFriendInfo.name, context.getString(R.string.frame_toast_friend_req_success));
                        aff.afMsgInfo.msg = aa;
                        new Thread() {
                            public void run() {
                                // heguiming 2013-12-04
                                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PF_ADD_SUCC);
                                // MobclickAgent.onEvent(context,
                                // ReadyConfigXML.PF_ADD_SUCC);

                                MessagesUtils.onAddFriendSuc(aff.afFriendInfo);
                                // 添加好友成功。删除好友请求,刷新好友列表
                                // MessagesUtils.removeFreqMsg(aff.afMsgInfo,
                                // true, true);
                                aff.afMsgInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS;
                                int _id = core.AfDbMsgInsert(aff.afMsgInfo);
                                aff.afMsgInfo._id = _id;
                                MessagesUtils.insertMsg(aff.afMsgInfo, true, true);
                                mHandler.sendEmptyMessage(Constants.REFRESH_FRIEND_LIST);
                            }

                        }.start();
                    } else {
                        mHandler.sendMessage(mHandler.obtainMessage(Constants.DB_DELETE));
                        AfPalmchat afPalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
                        MessagesUtils.addMsg2Chats(afPalmchat, afid, MessagesUtils.ADD_CHATS_FRD_REQ_SENT);
                    }
                }
            } else if (flag == Consts.REQ_MSG_SEND) {
                if (user_data instanceof String) {
                    final String afid = (String) user_data;
                    AfPalmchat core = PalmchatApp.getApplication().mAfCorePalmchat;
                    core.AfHttpFriendOpr("all", afid, Consts.HTTP_ACTION_A, Consts.FRIENDS_MAKE, (byte) Consts.AFMOBI_FRIEND_TYPE_FF, null, afid, this);
                } else if (user_data instanceof Integer) {
                    dismissProgressDialog();
                    int msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
                    PalmchatLogUtils.println("flag  msg_id  " + msg_id);
                    if (msg_id > DefaultValueConstant.LENGTH_0) {
                        String fid = (result != null && result instanceof String) ? result.toString() : DefaultValueConstant.MSG_INVALID_FID;
                        if (CommonUtils.isProfileActivity(context)) {
                            ToastManager.getInstance().show(context, R.string.success);
                        }
                        sendBroadcastForTextOrVoice(msg_id, AfMessageInfo.MESSAGE_SENT, fid);
                    } else if (msg_id == Consts.REQ_CODE_READ) {
                        PalmchatLogUtils.println("flag  Consts.REQ_CODE_READ code " + code + "  flag  " + flag);
                    } else {
                        ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
                        PalmchatLogUtils.println("case Consts.REQ_MSG_SEND code " + code + "  flag  " + flag);
                    }
                }

            } else if (flag == Consts.REQ_SEND_GIFT) {
                int userData = (Integer) user_data;
                if (userData == 0) {
                    AfDatingInfo[] afDatingInfos = (AfDatingInfo[]) result;
                    PalmchatLogUtils.println("afDatingInfos  " + afDatingInfos);
                    if (afDatingInfos != null && afDatingInfos.length > 1) {
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PFSD_FL_SUCC);
                        // MobclickAgent.onEvent(context,
                        // ReadyConfigXML.PFSD_FL_SUCC);
                        AfDatingInfo myDatingInfo = afDatingInfos[0];
                        AfDatingInfo otherDatingInfo = afDatingInfos[1];
                        PalmchatLogUtils.println("myDatingInfo  " + myDatingInfo);
                        PalmchatLogUtils.println("otherDatingInfo  " + otherDatingInfo);
                        ToastManager.getInstance().show(context, R.string.success);
                        AfProfileInfo myProfileInfo = CacheManager.getInstance().getMyProfile();
                        myProfileInfo.dating = myDatingInfo;
                        afProfileInfo.dating.charm_level = otherDatingInfo.charm_level;
                        afProfileInfo.dating.wealth_flower = otherDatingInfo.wealth_flower;
                        updateProfile(afProfileInfo);
                    } else {
                        ToastManager.getInstance().show(context, R.string.failure);
                    }
                } else {
                    if (Consts.REQ_GET_DATING_PHONE == userData) {
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PNSD_FL_SUCC);
                        // MobclickAgent.onEvent(context,
                        // ReadyConfigXML.PNSD_FL_SUCC);
                        int phone = mAfPalmchat.AfHttpGetDatingPhone(afid, null, this);
                        // mhttpHandle.put(phone,phone);
                    }
                }
            } else if (flag == Consts.REQ_GET_DATING_PHONE) {
                dismissProgressDialog();
                AfDatingInfo afDatingInfo = (AfDatingInfo) result;
                if (afDatingInfo != null) {// &&
                    // afDatingInfo.is_show_dating_phone
                }
            } else if (flag == Consts.REQ_FLAG_ACCUSATION) {
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PF_T_REPORT);
                // MobclickAgent.onEvent(context, ReadyConfigXML.PF_T_REPORT);
                dismissProgressDialog();
                ToastManager.getInstance().show(context, R.string.report_success);
            } else if (flag == Consts.REQ_FLAG_FOLLOW_LIST) {// 关注 或取消关注
                dismissProgressDialog();
                boolean isFollow = (Boolean) user_data;
                if (isFollow) {// 取消关注一个人
                    MessagesUtils.onDelFollowSuc(Consts.AFMOBI_FOLLOW_TYPE_MASTER, afProfileInfo);
                    ToastManager.getInstance().show(context, getString(R.string.public_unfollow_success));
                } else {// 关注一个人
                    MessagesUtils.onAddFollowSuc(Consts.AFMOBI_FOLLOW_TYPE_MASTER, afProfileInfo);
                    //MessagesUtils.addMsg2Chats(mAfPalmchat, afid, MessagesUtils.ADD_CHATS_FOLLOW);
                    ToastManager.getInstance().show(context, R.string.followed);
                }
                EventBus.getDefault().post(new EventFollowNotice(afProfileInfo.afId));
                Update_IsFollow();
            } else if (flag == Consts.REQ_BCGET_PROFILE_BY_AFID) { // 从服务器获取广播列表数据


                if (null != result) {
                    AfResponseComm afResponseComm = (AfResponseComm) result;
                    AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
                    if (afPeoplesChaptersList != null) {
                        ArrayList<AfChapterInfo> list_AfChapterInfo = afPeoplesChaptersList.list_chapters;
                        if (list_AfChapterInfo.size() > 0) {
                            setAdapter(list_AfChapterInfo);
                        } else {
                            if (lin_broadcast2.isSelected()) {
                                showNoData();
                                ToastManager.getInstance().show(context, R.string.no_data);
                            }
                        }
                    } else {
                        ToastManager.getInstance().show(context, R.string.no_data);
                        showNoData();
                    }
                } else {
                    ToastManager.getInstance().show(context, R.string.no_data);
                    showNoData();
                }
                stopLoad();

            } else if (flag == Consts.REQ_BLOCK_LIST) { // block 返回成功的
                dismissProgressDialog();
                if (mIsBloaked) {
                    MessagesUtils.onUnBlockSuc(afProfileInfo);// 进行数据库和缓存列表操作
                    mIsBloaked = false;
                    mIv_BlockedFlag.setVisibility(View.GONE);
                    mIv_BlockedFlag2.setVisibility(View.GONE);
                    rl_unblock.setVisibility(View.GONE);
                    lin_tab_bottom.setVisibility(View.VISIBLE);
                    imageHeadValue.setColorFilter(null);
                    imageHeadValue2.setColorFilter(null);
                    isFriends();
                    ToastManager.getInstance().show(context, R.string.unblocksucceeded);
                } else {
                    MessagesUtils.onAddBlockSuc(afProfileInfo);// 进行数据库和缓存列表操作
                    mIsBloaked = true;
                    mIv_BlockedFlag.setVisibility(View.VISIBLE);
                    mIv_BlockedFlag2.setVisibility(View.VISIBLE);
                    rl_unblock.setVisibility(View.VISIBLE);
                    lin_tab_bottom.setVisibility(View.GONE);
                    setGrayImage();
                    ToastManager.getInstance().show(context, R.string.blocksucceeded);
                    /*finish();*/
                }

            }
        } else {//
            if (flag == Consts.REQ_GET_INFO) {
                if (code == Consts.REQ_CODE_ACCOUNT_NOEXIST) {
                    ToastManager.getInstance().show(context, R.string.afid_not_found);
                }
            } else if (flag == Consts.REQ_BLOCK_LIST) {
                if (code == Consts.REQ_CODE_FRIEND_BLOCKOPR_MAX_LIMIT) {
                    ToastManager.getInstance().show(context, R.string.blockedlimitedtip);
                }
            } else if (flag == Consts.REQ_MSG_SEND || flag == Consts.REQ_FRIEND_LIST) {
                dismissProgressDialog();
                Consts.getInstance().showToast(context, code, flag, http_code);
                sendSuccess = false;
                if (user_data != null && user_data instanceof Integer) {
                    sendMsgFailure(flag, code, user_data);
                }
            } else if (flag == Consts.REQ_GET_DATING_PHONE) {
                if (code == Consts.REQ_CODE_102 || code == Consts.REQ_CODE_103) {
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ND_FL);
                    // MobclickAgent.onEvent(context, ReadyConfigXML.ND_FL);
                } else if (code == Consts.REQ_CODE_105) {
                    ToastManager.getInstance().show(context, R.string.phone_invisible);
                } else {
                    Consts.getInstance().showToast(context, code, flag, http_code);
                }
            } else if (flag == Consts.REQ_SEND_GIFT) {
                Consts.getInstance().showToast(context, code, flag, http_code);

            } else if (flag == Consts.REQ_FLAG_ACCUSATION) {
                if (code == Consts.REQ_CODE_110) {
                    ToastManager.getInstance().show(context, R.string.has_report);
                } else {
                    Consts.getInstance().showToast(context, code, flag, http_code);
                }
            } else if (flag == Consts.REQ_FLAG_FOLLOW_LIST) {
                if(code == Consts.REQ_CODE_4096){
                    Consts.getInstance().showToast(context, code, flag, http_code);
                }else {
                    dismissProgressDialog();
                    boolean isFollow = (Boolean) user_data;
                    String msg = "";
                    if (isFollow) {
                        msg = getString(R.string.unfollow_failure).replace("XXXX", afProfileInfo.name);
                    } else {
                        msg = getString(R.string.follow_failure).replace("XXXX", afProfileInfo.name);
                    }
                    ToastManager.getInstance().show(context, msg);
                }
                Update_IsFollow();
            } else if (flag == Consts.REQ_BCGET_PROFILE_BY_AFID) {
                stopLoad();
                if (code == Consts.REQ_CODE_UNNETWORK) {
                    if (START_INDEX > 0) {
                        START_INDEX--;
                    }
                }
                if (code == Consts.REQ_CODE_104) {
                    PalmchatLogUtils.e(TAG, "----code:" + code);
                    loadData();
                } else {
                    showNoData();
                }
                Consts.getInstance().showToast(context, code, flag, http_code);
            } else {
                Consts.getInstance().showToast(context, code, flag, http_code);
            }
            progressBar.setVisibility(View.GONE);
            vImageViewRight.setVisibility(View.VISIBLE);
            dismissProgressDialog();

        }
    }

    /**
     * 设置图片变灰色
     *
     * @param
     */
    private void setGrayImage() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageHeadValue2.setColorFilter(filter);
        imageHeadValue.setColorFilter(filter);
    }

    /**
     * 添加好友成功处理
     *
     * @param core
     * @param aff
     */
    private void new_friend_insert(final AfPalmchat core, final MainAfFriendInfo aff) {
        String aa = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, aff.afFriendInfo.name, context.getString(R.string.frame_toast_friend_req_success));
        aff.afMsgInfo.msg = aa;
        new Thread() {
            public void run() {
                // heguiming 2013-12-04
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PF_ADD_SUCC);
                // MobclickAgent.onEvent(context, ReadyConfigXML.PF_ADD_SUCC);

                MessagesUtils.onAddFriendSuc(aff.afFriendInfo);
                // 添加好友成功。删除好友请求,刷新好友列表
                MessagesUtils.removeFreqMsg(aff.afMsgInfo, true, true);
                aff.afMsgInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS;
                // 生成一条好友请求成功消息 插入数据库中。
                int _id = core.AfDbMsgInsert(aff.afMsgInfo);
                aff.afMsgInfo._id = _id;
                // 向内存内插入新消息，后面两个参数表示是否向数据库或内存内插入消息
                MessagesUtils.insertMsg(aff.afMsgInfo, true, true);
                handler.obtainMessage(Constants.TO_REFRESH_FRIEND_LIST, aff.afMsgInfo).sendToTarget();
            }

        }.start();
    }

    private void sendFriendReqOnResultBroadcast(int code, String afid, int flagReq) {
        Intent intent = new Intent(MessagesUtils.FRIEND_REQ_CALLBACK);
        intent.putExtra("fromAfId", afid);
        intent.putExtra("code", code);
        intent.putExtra("flagReq", flagReq);
        sendBroadcast(intent);
    }

    /**
     * 发送信息错误
     */
    private boolean sendMsgFailure(int flag, int code, Object user_data) {
        int msg_id;
        msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
        if (msg_id > DefaultValueConstant.LENGTH_0) {
            sendBroadcastForTextOrVoice(msg_id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID);
            return false;
        } else if (msg_id == Consts.REQ_CODE_READ) {
            PalmchatLogUtils.println("case Consts.REQ_CODE_READ sendMsgFailure  code " + code + "  flag  " + flag);
            return true;
        } else {
            // updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
            ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
            PalmchatLogUtils.println("else Consts.REQ_MSG_SEND else code " + code + "  flag  " + flag);
            return false;
        }
    }

    /**
     * 更新信息
     *
     * @param afInfo 个人信息
     */
    private void updateProfile(AfProfileInfo afInfo) {
        AfProfileInfo result = afInfo;
        CacheManager.getInstance().saveOrUpdateFriendProfile(afInfo);
        // refresh friend request's friend info add by wk
        MainAfFriendInfo tmpReq = MainAfFriendInfo.getFreqInfoFromAfID(afInfo.afId);
        if (tmpReq != null) {
            tmpReq.afFriendInfo = afInfo;
        }

        setContent(afInfo);

        // 何桂明 2013-10-28 更新此人所有所在组的信息
        List<AfGrpProfileInfo> groupList = CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).getList();
        AfProfileInfo profile = result;
        if (null != groupList) {
            for (int i = 0; i < groupList.size(); i++) {
                AfGrpProfileInfo group = groupList.get(i);
                ArrayList<AfGrpProfileItemInfo> members = group.members;
                for (int j = 0; j < members.size(); j++) {
                    AfGrpProfileItemInfo member = members.get(j);
                    if (profile.afId.equals(member.afid)) {
                        member.head_image_path = profile.head_img_path;
                        member.name = profile.name;
                        member.sex = profile.sex;
                        member.signature = profile.signature;
                    }
                }
            }
        }

        // update recent chat name
        MainAfFriendInfo chat = MainAfFriendInfo.getMainAfFriendInfoFromAfID(afInfo.afId);
        if (chat != null) {
            chat.afFriendInfo = afInfo;
            CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).saveOrUpdate(chat, false, true);
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.DB_DELETE: {
                    ToastManager.getInstance().show(ProfileActivity.this, getString(R.string.add_friend_req));
                    dismissProgressDialog();
                }
                break;
                case Constants.REFRESH_FRIEND_LIST: {

				/*Intent intent = new Intent();
                intent.setAction(Constants.REFRESH_FRIEND_LIST_ACTION);
				context.sendBroadcast(intent);*/
                    EventBus.getDefault().post(new EventRefreshFriendList());
				/*Intent intent2 = new Intent();
				intent2.setAction(Constants.REFRESH_CHATS_ACTION);
				context.sendBroadcast(intent2);*/
                    EventBus.getDefault().post(new RefreshChatsListEvent());

                    ToastManager.getInstance().show(ProfileActivity.this, getString(R.string.add_friend_req));
                    dismissProgressDialog();
                    break;
                }
                case Constants.DB_DELETE_FRIEND:
                    dismissProgressDialog();
                    ToastManager.getInstance().show(context, context.getResources().getString(R.string.delete));

//				sendBroadcast(new Intent(Constants.REFRESH_FRIEND_LIST_ACTION));
                    EventBus.getDefault().post(new EventRefreshFriendList());
//				sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
                    EventBus.getDefault().post(new RefreshChatsListEvent());
                    finish();
                    break;
            }
        }
    };

    private ArrayList<AfMessageInfo> listHiChats = new ArrayList<AfMessageInfo>();

    public void updateStatus(final int msgId, final int status, final String fid) {
        AfPalmchat mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        int index = ByteUtils.indexOf(listHiChats, msgId);
        for (AfMessageInfo af : listHiChats) {
            PalmchatLogUtils.println("af.id  " + af._id);
        }
        PalmchatLogUtils.println("index  " + index + "  msgId  " + msgId + "  status  " + status);
        int count = listHiChats.size();
        if (index != -1 && index < count) {
            AfMessageInfo afMessageInfo = listHiChats.get(index);
            afMessageInfo.status = status;
            afMessageInfo.fid = fid;
            PalmchatLogUtils.println("afMessageInof.fid:" + fid + "   afMessageInfo.msgId  " + afMessageInfo._id + "  afMessageInfo.status  " + afMessageInfo.status);
        }
        int _id = DefaultValueConstant.LENGTH_0;
        if (fid != DefaultValueConstant.MSG_INVALID_FID) {
            _id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS_AND_FID);

        } else {
            _id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS);
        }
        PalmchatLogUtils.println("p update status msg_id " + _id);
    }

    void sendBroadcastForTextOrVoice(final int msg_id, final int status, final String fid) {
        Intent intent = new Intent(MessagesUtils.TEXT_MESSAGE);
        intent.putExtra(JsonConstant.KEY_MESSAGE_ID, msg_id);
        intent.putExtra(JsonConstant.KEY_STATUS, status);
        intent.putExtra(JsonConstant.KEY_MSG_FID, fid);
        sendBroadcast(intent);
        new Thread(new Runnable() {
            public void run() {
                updateStatus(msg_id, status, fid);
            }
        }).start();
    }

    /**
     * dailog点击事件处理
     *
     * @param item 判断选则的是那个按钮
     */
    @Override
    public void onItemClick(int item) {
        // TODO Auto-generated method stub
        switch (item) {
            case AbuseDialog.ACTION_ABUSE://举报点击事件
                /*AbuseDialog abuseDialog = new AbuseDialog(context, afid, AbuseDialog.ACTION_SEND);
                abuseDialog.setItemClick(ProfileActivity.this);
                abuseDialog.show();*/

                AppDialog reportSendAppDialog = new AppDialog(context);
                String msg = context.getResources().getString(R.string.sure_report_abuse);
                reportSendAppDialog.createConfirmDialog(context, msg, new OnConfirmButtonDialogListener() {
                    @Override
                    public void onLeftButtonClick() {//点击取消
                    }

                    @Override
                    public void onRightButtonClick() {//点击确认发送
                        PalmchatLogUtils.println("AbuseDialog.ACTION_SEND");
                        showProgressDialog(R.string.please_wait);
                        mAfPalmchat.AfHttpAccusation(afid, afid, ProfileActivity.this);
                    }
                });
                reportSendAppDialog.show();
                break;
            case AbuseDialog.ACTION_FOLLOW:// 关注按钮
                switch (from) {
                    case HOME_TO_PROFILE:
                        // gtf 2015-5-21
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.HM_T_FOLLOW);
                        // MobclickAgent.onEvent(context, ReadyConfigXML.HM_T_FOLLOW);
                        break;
                    case BC_TO_PROFILE:
                        // gtf 2015-5-21
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BC_T_FOLLOW);
                        // MobclickAgent.onEvent(context, ReadyConfigXML.BC_T_FOLLOW);
                        break;
                    case CHAT_TO_PROFILE:
                        // gtf 2015-5-21
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CH_T_FOLLOW);
                        // MobclickAgent.onEvent(context, ReadyConfigXML.CH_T_FOLLOW);
                        break;
                    case FRIENDS_TO_PROFILE:
                        // gtf 2015-5-21
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CONT_T_FOLLOW);
                        // MobclickAgent.onEvent(context, ReadyConfigXML.CONT_T_FOLLOW);
                        break;
                    case GPCHAT_TO_PROFILE:
                        // gtf 2015-5-21
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.GPCHAT_T_FOLLOW);
                        // MobclickAgent.onEvent(context,
                        // ReadyConfigXML.GPCHAT_T_FOLLOW);
                        break;
                    default:
                        if (isFollow) { // ture is follow /false is unfollow
                            // gtf 2014-11-16
                            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PF_T_FOLLOW);
                            // MobclickAgent.onEvent(context,
                            // ReadyConfigXML.PF_T_FOLLOW);
                        }
                        break;
                }
                if (!TextUtils.isEmpty(afid) && !CacheManager.getInstance().getClickableMap().get(afid + CacheManager.follow_suffix)) {
                    AddFollw(afid);
                }
                break;
            case AbuseDialog.ACTION_BLOCK: // block按钮
                blockDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 是否加入或者取消黑名单确认dialog
     */
    private void blockDialog() {
        AppDialog bloakAppDialog = new AppDialog(context);
        String hintMsg;
        if (mIsBloaked) {//判断是否在黑名单列表
            hintMsg = context.getResources().getString(R.string.unblock_users);//在黑名单列表提示的语句
        } else {
            hintMsg = context.getResources().getString(R.string.Shielding_users);//不在黑名单列表提示的语句
        }
        bloakAppDialog.createConfirmDialog(context, hintMsg, new OnConfirmButtonDialogListener() {
            @Override
            public void onLeftButtonClick() {//点击取消
            }

            @Override
            public void onRightButtonClick() {//点击确认发送
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    ToastManager.getInstance().show(context, getString(R.string.network_unavailable));
                    return;
                }
                showProgressDialog(R.string.please_wait);
                if (mIsBloaked) {// 确认加入黑名单(Consts.HTTP_ACTION_A)或者取消黑名单(Consts.HTTP_ACTION_D )
                    mAfPalmchat.AfHttpBlockOpr(afid, Consts.HTTP_ACTION_D, null, null, ProfileActivity.this);
                } else {
                    mAfPalmchat.AfHttpBlockOpr(afid, Consts.HTTP_ACTION_A, null, null, ProfileActivity.this);
                }
            }
        });
        bloakAppDialog.show();
    }

    /**
     * 判断是选中的是“简介”还是“我的广播”
     *
     * @param view
     */
    private void showSelectedOption(View view) {
        switch (view.getId()) {
            case R.id.lin_profile:// 简介
                lin_profile.setSelected(true);
                lin_broadcast.setSelected(false);
                break;
            case R.id.lin_broadcast:// 我的广播
                lin_profile2.setSelected(false);
                lin_broadcast2.setSelected(true);
                if (LoadingData) {
                    if (mListview != null) {
                        mListview.show_loadmore();
                    }
                } else {
                    if (mListview != null) {
                        mListview.hide_loadmore();
                    }
                }
                break;
        }
    }

    // 判断是否点击关注按钮
    public void AddFollw(final String afid) {
        if (!isFollow) {
            Update_IsFollow();
            showProgressDialog(R.string.following);
            mAfPalmchat.AfHttpFollowCmd(Consts.AFMOBI_FOLLOW_MASTRE, afid, Consts.HTTP_ACTION_A, isFollow, ProfileActivity.this);
            MessagesUtils.addStranger2Db(afProfileInfo);
        } else if (isFollow) {
            if(afProfileInfo.name != null){
                AppDialog appDialog = new AppDialog(this);
                String title = getString(R.string.unfollow_dialog_msg).replace("XXXX", afProfileInfo.name);
                appDialog.createConfirmDialog(this, title, new OnConfirmButtonDialogListener() {
                    @Override
                    public void onRightButtonClick() {
                        Update_IsFollow();
                        showProgressDialog(R.string.unfollowing);
                        mAfPalmchat.AfHttpFollowCmd(Consts.AFMOBI_FOLLOW_MASTRE, afid, Consts.HTTP_ACTION_D, isFollow, ProfileActivity.this);

                    }

                    @Override
                    public void onLeftButtonClick() {
                    }
                });
                if (!appDialog.isShowing()) {
                    appDialog.show();
                }
            }
        }
    }

    public void Update_IsFollow() {
        if (CacheManager.getInstance().getClickableMap().containsKey(afid + CacheManager.follow_suffix)) {
            if (CacheManager.getInstance().getClickableMap().get(afid + CacheManager.follow_suffix)) {
                CacheManager.getInstance().getClickableMap().put(afid + CacheManager.follow_suffix, false);
            } else {
                CacheManager.getInstance().getClickableMap().put(afid + CacheManager.follow_suffix, true);
            }
            isFollow = CommonUtils.isFollow(afid);
        }
    }

    /**
     * @param afChapterInfo
     */
    public void onEventMainThread(AfChapterInfo afChapterInfo) {
        if (Constants.UPDATE_DELECT_BROADCAST == afChapterInfo.eventBus_action) {// 删除广播
            adapter.notifyDataSetChanged_removeBymid(afChapterInfo.mid);
            CacheManager.getInstance().remove_BC_RecordSendSuccessDataBy_mid(afChapterInfo.mid);
        } else if (Constants.UPDATE_LIKE == afChapterInfo.eventBus_action) {
            adapter.notifyDataSetChanged_updateLikeBymid(afChapterInfo);
        }
    }

    class LooperThread extends Thread {
        private static final int SETPROFILE_AND_CHECK_ISLIKE = 7004;
        private static final int DELETE_DB_IS_SERVER_DATA = 7005;
        private static final int UPDATE_FRIEND_LIST = 7006;
        Handler looperHandler;
        Looper looper;

        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            looperHandler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case SETPROFILE_AND_CHECK_ISLIKE:
                            ArrayList<AfChapterInfo> broadcastMessageList = (ArrayList<AfChapterInfo>) msg.obj;
                            int size = broadcastMessageList.size();
                            for (int i = 0; i < size; i++) {
                                broadcastMessageList.get(i).profile_Info = afProfileInfo;
                                broadcastMessageList.get(i).isLike = PalmchatApp.getApplication().mAfCorePalmchat.AfBcLikeFlagCheck(broadcastMessageList.get(i).mid); // check
                                // like
                            }
                            Message message = new Message();
                            message.obj = broadcastMessageList;
                            message.what = NODTIFYDATASETCHANGED;
                            handler.sendMessage(message);
                            break;
                        case DELETE_DB_IS_SERVER_DATA:
                            break;
                        case UPDATE_FRIEND_LIST:
                            AfProfileInfo afInfo = (AfProfileInfo) msg.obj;
                            AfFriendInfo searchResult = CacheManager.getInstance().searchAllFriendInfo(afInfo.afId);
                            if ((searchResult == null) && ((null == CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_BF).search(afProfileInfo, false, true)))) {
                                PalmchatLogUtils.println("--xxxvvvv searchResult searchResult ProfileActivity update friend cache");
                                int followType = mAfPalmchat.AfDbProfileGetFollowtype(afInfo.afId);
                                afInfo.follow_type = followType;
                                afInfo.type = Consts.AFMOBI_FRIEND_TYPE_STRANGER;
                                CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_STRANGER).saveOrUpdate(afInfo, false, true);
                                mAfPalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, afInfo);
                            }
                            break;
                        default:
                            break;
                    }
                }
            };
            Looper.loop();
        }
    }

    /**
     * 判断是否有数据在“我的广播”列表没有数据的时候显示的图片
     */
    private void showNoData() {
        if (lin_broadcast2.isSelected()) {
            if (adapter.getCount() < 1) {
                if (rl_no_data != null) {
                    rl_no_data.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /* 设置导航条 */
    @SuppressLint("NewApi")
    public void setNavigationBar() {
        tintManager = new SystemBarTintManager(this);
        SystemBarConfig systemBarConfig = tintManager.getConfig();
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(Color.BLACK);
        tintManager.setStatusBarAlpha(StatusBarAlpha);
        if (VERSION.SDK_INT == VERSION_CODES.KITKAT) {
            StatusBarHeight = systemBarConfig.getStatusBarHeight();
            if (lin_title != null) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(0, StatusBarHeight, 0, 0);
                lin_title.setLayoutParams(params);
            }
            if (systemBarConfig.hasNavigtionBar()) {
                if (PalmchatApp.getOsInfo().getUa().contains("HTC") || PalmchatApp.getOsInfo().getUa().contains("MI") || PalmchatApp.getOsInfo().getBrand().contains("Meizu")) {// 过滤htc,小米
                    // meizu
                    r_paofile.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    tintManager.setNavigationBarTintEnabled(false);
                } else {
                    tintManager.setNavigationBarTintEnabled(true);
                    tintManager.setNavigationBarTintColor(Color.BLACK);
                    if (r_paofile != null) {
                        int NavigationBarHeight = systemBarConfig.getNavigationBarHeight();
                        r_paofile.setPadding(0, 0, 0, NavigationBarHeight);
                    }
                }
            }
        }
        Bundle bundle = getIntent().getExtras();
        PalmchatLogUtils.e("MyProfile_bundle", bundle + "");
        if (bundle != null) {
            ScrollY = bundle.getInt("ScrollY");
            final int statusAlpha = bundle.getInt("alpha");
            PalmchatLogUtils.e("MyProfile_ScrollY", ScrollY + "|" + statusAlpha);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (ScrollY > 0) {
                        if (statusAlpha < StatusBarAlpha) {
                            tintManager.setStatusBarAlpha(StatusBarAlpha);
                        } else {
                            tintManager.setStatusBarAlpha(statusAlpha);
                        }
                    }
                }
            }, 300);
        }

    }

    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        alpha = y = (int) Math.abs(y);
        int subAddBg_H = (int) Math.abs(addBg_H);
        if (y <= subAddBg_H) {
            if (alpha > 255) {
                alpha = 255;
            }

        } else {
            alpha = 255;

        }
        if (!isPaused) {//OnPause后 这里竟然会被调用 奇怪
            showTitle(alpha, true);
        }
        /*relativelayout_title.getBackground().setAlpha(alpha);
        relativelayout_title.postInvalidate();*/

        if (tintManager != null) {
            int tempAlpha = StatusBarAlpha;
            if (tempAlpha >= 255) {
                tempAlpha = 255;
            } else {
                tempAlpha = StatusBarAlpha + alpha;
                if (tempAlpha > 255) {
                    tempAlpha = 255;
                }
            }
            PalmchatLogUtils.e(">>>>>>tempAlpha<=125<<<<<<<<", "tempAlpha=" + tempAlpha);
            tintManager.setStatusBarAlpha(tempAlpha);
        }

        // alpha = (int) Math.abs(y);
        // int max = (int) Math.max(y, addBg_H);
        // if (alpha <= 255) {
        // if (alpha > max) {
        // alpha = 255;
        // }
        // relativelayout_title.getBackground().setAlpha(alpha);
        // relativelayout_title.postInvalidate();
        //
        // int tempAlpha = StatusBarAlpha;
        // if (alpha >= max) {
        // tempAlpha = 255;
        // } else {
        // tempAlpha = StatusBarAlpha + alpha;
        // if (tempAlpha > 255) {
        // tempAlpha = 255;
        // }
        // }
        // PalmchatLogUtils.e(">>>>>>tempAlpha<=125<<<<<<<<", "tempAlpha=" +
        // tempAlpha);
        // tintManager.setStatusBarAlpha(tempAlpha);
        // }
    }

    /**
     * 判断是否为好友，是好友就只显示聊天按钮，添加按钮隐藏
     */
    private void isFriends(){
        if(afid != null){
           /* final MainAfFriendInfo aff = MainAfFriendInfo.getFreqInfoFromAfID(afid);
            if (aff != null) {
                boolean flag = MessagesUtils.isFriendReqMessage(aff.afMsgInfo.type);
                if (flag) {
                    lin_tab_follow.setVisibility(View.GONE);
                }else{
                    lin_tab_follow.setVisibility(View.VISIBLE);
                }
            }*/
            isFriend =  CacheManager.getInstance().isFriend(afid);
            if (isFriend) {
                lin_tab_follow.setVisibility(View.GONE);
            }else{
                lin_tab_follow.setVisibility(View.VISIBLE);
            }
        }

    }

}