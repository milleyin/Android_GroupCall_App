package com.afmobi.palmchat.ui.activity.profile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.db.SharePreferenceService;
import com.afmobi.palmchat.eventbusmodel.RefreshFollowerFolloweringOrGroupListEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.chats.ActivityBackgroundChange;
import com.afmobi.palmchat.ui.activity.chats.PreviewImageActivity;
import com.afmobi.palmchat.ui.activity.friends.ContactsActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.profile.AdapterBroadcastProfile.IAdapterBroadcastProfile;
import com.afmobi.palmchat.ui.activity.social.BroadcastFragment;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.InnerListView;
import com.afmobi.palmchat.ui.customview.MyScrollView;
import com.afmobi.palmchat.ui.customview.PhotoWallView;
import com.afmobi.palmchat.ui.customview.ScrollViewListener;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager.SystemBarConfig;
import com.afmobi.palmchat.ui.customview.photos.WallEntity;
import com.afmobi.palmchat.ui.customview.videoview.CustomVideoController;
import com.afmobi.palmchat.ui.customview.videoview.VedioManager;
import com.afmobi.palmchat.util.BitmapUtils;
import com.afmobi.palmchat.util.BroadcastUtil;
import com.afmobi.palmchat.util.ClippingPicture;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.FolllowerUtil;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.UploadPictureUtils;
import com.afmobi.palmchat.util.UploadPictureUtils.IUploadHead;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfRespAvatarInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfPeoplesChaptersList;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;
import com.core.listener.AfHttpSysListener;
//import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

public class MyProfileActivity extends BaseActivity implements OnClickListener, AfHttpResultListener, AfHttpProgressListener, ScrollViewListener {

    private static final String TAG = MyProfileActivity.class.getSimpleName();
    private static final int MYPROFILEACTIVITY = 10010;
    private File fCurrentFile;
    private String sCameraFilename;
    private static final int AF_INTENT_ALBUM = 5;
    private static final int AF_INTENT_DETAIL = 6;
    public static final int AF_ACTION_PHOTO_WALL = 10;
    private static final int AF_ACTION_PHOTO_WALL_UPDATE = 30;
    private static final int AF_ACTION_HEAD = 20;
    private static final int NODTIFYDATASETCHANGED = 111;
    public int currentAction;
    private String update_sn = "";
    private boolean is_update = false;
    private int update_index = -1;
    private final int StatusBarAlpha = 125;

    private final int BROADCAST_DB = 0,//从数据库获取
            BROADCAST_SERVER = 1;//数据是从服务器获取


    private static int followers_count;//Following维持原来的从列表计算大小 不变
    /**
     * 整个布局最外层控件
     */
    private View r_paofile;
    /**
     * 详情布局最外层scrollview
     */
    private MyScrollView mScrollView;
    /**
     * 广播列表最外层布局
     */
    private View rl_broadcast;
    private AfPalmchat mAfPalmchat;
    private AfProfileInfo afProfileInfo;
    private List<WallEntity> entities;
    private List<String> serials;
    private TextView viewWhatsupValue;
    private TextView textNameValue, textNameValue2;
    private TextView textAfidValue, textAfidValue2;
    private ImageView imageHeadValue, imageHeadValue2, attr_start, mIVFollowed, mIVFollowed2;
    private View attrLayout;
    /**
     * 更换头部背景
     */
    private View rl_add_bg, rl_add_bg2;
    private TextView textAgeValue, textAgeValue2;
    private String afid;
    private ProgressBar progressBar;
    private TextView albumTitle;
    private PhotoWallView photoWallView;
    private String oldSign;
    private LargeImageDialog largeImageDialog;
    private TextView textRegionValue;
    private TextView textFollowing, textFollowing2;
    private TextView textFollowers, textFollowers2;
    private SystemBarTintManager tintManager;
    private View relativelayout_title, lin_title, edit_profile;
    private View lin_Following, lin_Following2, lin_Followers, lin_Followers2;
    private InnerListView mListview;
    private View lin_profile, lin_profile2, lin_broadcast, lin_broadcast2;
    private View lin_profile_suspension, lin_broadcast_suspension;
    private int ScrollY = 0;
    private AdapterBroadcastProfile adapter;
    private int pageid = 0;
    private int START_INDEX = 0;
    private boolean isLoadMore, LoadingData = false;
    private static final int LIMIT = 10;
    /**
     * 无数据时
     */
    private View rl_no_data;
    private LooperThread looperThread;
    private int alpha = 0;
    private int StatusBarHeight = 0;
    private ImageView mMyQrcode, mMyQrcode2;// 显示二维码控件
    private int lastItemIndex;// 当前ListView中最后一个Item的索引
    private int firstItem;
    /**
     * 详情页面头部控件
     */
    private View head_detail_view;
    /**
     * 广播页面头部控件
     */
    private View head_broadcast_view;
    private View lin_tab_suspension_linearLayout;
    /**
     * 头部添加背景控件的高度
     */
    private int addBg_H = 0;
    /**
     * 头部View的高度
     */
    private int head_h = 0;

    /*当前window大小*/
    private Rect rect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    public void findViews() {
        setContentView(R.layout.activity_my_profile);
        /** 初始化标题栏 */
        r_paofile = findViewById(R.id.r_paofile);
        ((TextView) findViewById(R.id.title_text)).setText(R.string.my_profile);
        progressBar = (ProgressBar) findViewById(R.id.img_loading);
        edit_profile = (ImageView) findViewById(R.id.op2);
        edit_profile.setBackgroundResource(R.drawable.edit_profile_selector);
        lin_title = findViewById(R.id.lin_title);
        relativelayout_title = findViewById(R.id.relativelayout_title);
        View backView = findViewById(R.id.back_button);
        backView.setBackgroundResource(R.drawable.profile_back_button);

        /** 初始化详情头部 */
        head_detail_view = findViewById(R.id.layout_profile_top);
        rl_add_bg = head_detail_view.findViewById(R.id.rl_add_bg);
        imageHeadValue = (ImageView) head_detail_view.findViewById(R.id.img_photo);
        textNameValue = (TextView) head_detail_view.findViewById(R.id.profile_text_name);
        textAfidValue = (TextView) head_detail_view.findViewById(R.id.profile_text_afid);
        textAgeValue = (TextView) head_detail_view.findViewById(R.id.text_age);
        lin_Followers = head_detail_view.findViewById(R.id.lin_followers);
        textFollowing = (TextView) head_detail_view.findViewById(R.id.following);
        textFollowers = (TextView) head_detail_view.findViewById(R.id.followers);
        lin_Following = head_detail_view.findViewById(R.id.lin_following);
        //lin_Followers = head_detail_view.findViewById(R.id.lin_followers);
        lin_profile = head_detail_view.findViewById(R.id.lin_profile);
        lin_broadcast = head_detail_view.findViewById(R.id.lin_broadcast);
        ((TextView) head_detail_view.findViewById(R.id.txt_broadcast)).setText(getString(R.string.my_broadcast));
        mMyQrcode = (ImageView) head_detail_view.findViewById(R.id.my_qrcode);

        /** 初始化详情页 */
        viewWhatsupValue = (TextView) findViewById(R.id.profile_whatsup_value);
        textRegionValue = (TextView) findViewById(R.id.profile_region_value);
        mScrollView = (MyScrollView) findViewById(R.id.sl_detail);

        /** 初始化相册 */
        ViewStub view_stub = (ViewStub) findViewById(R.id.viewstub);
        view_stub.inflate();
        attrLayout = findViewById(R.id.attr_layout);
        attr_start = (ImageView) findViewById(R.id.attr_start);
        attr_start.setTag(1);
        attr_start.setVisibility(View.GONE);
        albumTitle = (TextView) findViewById(R.id.attr_title);
        photoWallView = (PhotoWallView) findViewById(R.id.photo_wall_view);

        /** 初始化广播页面 */
        rl_broadcast = findViewById(R.id.rl_broadcast);
        mListview = (InnerListView) findViewById(R.id.listview);
        rl_no_data = findViewById(R.id.rl_no_data);
        lin_tab_suspension_linearLayout = findViewById(R.id.lin_tab_suspension);
        /** 初始化广播头部 */
        head_broadcast_view = mListview.getHeadView();
        rl_add_bg2 = head_broadcast_view.findViewById(R.id.rl_add_bg);
        imageHeadValue2 = (ImageView) head_broadcast_view.findViewById(R.id.img_photo);
        textNameValue2 = (TextView) head_broadcast_view.findViewById(R.id.profile_text_name);
        textAfidValue2 = (TextView) head_broadcast_view.findViewById(R.id.profile_text_afid);
        textAgeValue2 = (TextView) head_broadcast_view.findViewById(R.id.text_age);
        textFollowing2 = (TextView) head_broadcast_view.findViewById(R.id.following);
        textFollowers2 = (TextView) head_broadcast_view.findViewById(R.id.followers);
        lin_Following2 = head_broadcast_view.findViewById(R.id.lin_following);
        lin_Followers2 = head_broadcast_view.findViewById(R.id.lin_followers);
        lin_profile2 = head_broadcast_view.findViewById(R.id.lin_profile);
        lin_broadcast2 = head_broadcast_view.findViewById(R.id.lin_broadcast);
        lin_profile_suspension = findViewById(R.id.lin_profile_suspension);
        lin_broadcast_suspension = findViewById(R.id.lin_broadcast_suspension);
        lin_profile_suspension.setOnClickListener(this);
        ((TextView) head_broadcast_view.findViewById(R.id.txt_broadcast)).setText(getString(R.string.my_broadcast));
        mMyQrcode2 = (ImageView) head_broadcast_view.findViewById(R.id.my_qrcode);

        mIVFollowed = (ImageView) head_detail_view.findViewById(R.id.tv_followed);
        mIVFollowed2 = (ImageView) head_broadcast_view.findViewById(R.id.tv_followed);

        edit_profile.setVisibility(View.VISIBLE);
        edit_profile.setOnClickListener(this);
        if (backView != null) {
            backView.setOnClickListener(this);
        }

//        rl_add_bg.setOnClickListener(this);
        imageHeadValue.setOnClickListener(this);
        lin_Following.setOnClickListener(this);
        lin_Followers.setOnClickListener(this);
        lin_profile.setOnClickListener(this);
        lin_broadcast.setOnClickListener(this);
        mMyQrcode.setOnClickListener(this);

//        rl_add_bg2.setOnClickListener(this);
        imageHeadValue2.setOnClickListener(this);
        lin_Following2.setOnClickListener(this);
        lin_Followers2.setOnClickListener(this);
        lin_profile2.setOnClickListener(this);
//		lin_broadcast2.setOnClickListener(this);
        mMyQrcode2.setOnClickListener(this);
        mScrollView.setScrollViewListener(this);
    }

    @Override
    public void init() {
        mAfPalmchat = ((PalmchatApp) context.getApplicationContext()).mAfCorePalmchat;

        looperThread = new LooperThread();
        looperThread.setName(TAG);
        looperThread.start();
        PalmchatLogUtils.e("MyProfile_onCreate", "MyProfile_onCreate");
        fCurrentFile = CacheManager.getInstance().getCurFile();
        currentAction = CacheManager.getInstance().getCurAction();
        // heguiming 2013-12-04
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_ME);
        EventBus.getDefault().register(this);

        new Timer().schedule(new TimerTask() {
            public void run() {
                handler.sendEmptyMessage(1);
            }

        }, 1000);

        afProfileInfo = CacheManager.getInstance().getMyProfile();

        if (afProfileInfo != null &&
                (JsonConstant.getMyProfileFromServer_forFollowCount_byPalmID == null ||
                        !afProfileInfo.afId.equals(JsonConstant.getMyProfileFromServer_forFollowCount_byPalmID))) {//如果切换过账号id不一致 也需要重新取
            //myProfile的info要至少取一次 发送 get profile请求 为了获取Follow的相关数量
            followers_count = 0;
            select_Followers_or_FollowingCount(true);
            if (mAfPalmchat != null && afProfileInfo.afId != null) {
                progressBar.setVisibility(View.VISIBLE);
                edit_profile.setVisibility(View.GONE);
                mAfPalmchat.AfHttpGetInfo(new String[]{afProfileInfo.afId}, Consts.REQ_GET_INFO, null, null, this);
            }
        }
        if (afProfileInfo != null) {//如果缓存中的MyProfile不为空 就设置
            afid = afProfileInfo.afId;
            setContent(afProfileInfo);

        }
        setAdapter(new ArrayList<AfChapterInfo>(), BROADCAST_DB);
        mListview.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == adapter.getCount() - 1 && firstItem != 0) {
                    // 加载数据代码，此处省略了
                    if (!LoadingData) {
                        loadmore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /*当视频在当前页面不可见时则停止*/
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
                PalmchatLogUtils.e(TAG, "lastItemIndex=" + lastItemIndex + ",firstItem=" + firstItem);

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
                if(!isPaused ) {//OnPause后 这里竟然会被调用 奇怪
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
                // showTitle(alpha);
                // int tempAlpha = StatusBarAlpha;
                // if (alpha >= max) {
                // tempAlpha = 255;
                // } else {
                // tempAlpha = StatusBarAlpha + alpha;
                // if (tempAlpha > 255) {
                // tempAlpha = 255;
                // }
                // }
                // tintManager.setStatusBarAlpha(tempAlpha);
                // }
                //
                // }
            }

        });
        showIsFollowed();
    }

    /**
     * 是否被followed
     */
    private void showIsFollowed() {
        if (SharePreferenceUtils.getInstance(getApplicationContext()).getIsFollowed(CacheManager.getInstance().getMyProfile().afId)
                && FolllowerUtil.getInstance().getSizeForDefault() > 0) {
            mIVFollowed.setVisibility(View.VISIBLE);
            mIVFollowed2.setVisibility(View.VISIBLE);
        } else {
            mIVFollowed.setVisibility(View.INVISIBLE);
            mIVFollowed2.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 当页面加载完成后获取控件高度
     */
    private boolean measure;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !measure) {
            measure = true;
            addBg_H = rl_add_bg.getHeight();
            head_h = head_detail_view.getHeight();

            /** 初始化头部背景图 */
            Drawable drawable = FileUtils.getImageFromAssetsFile2(context, SharePreferenceUtils.getInstance(context).getProfileAblum(), head_h);
            if (Build.VERSION.SDK_INT <= VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                head_detail_view.setBackgroundDrawable(drawable);
                head_broadcast_view.setBackgroundDrawable(drawable);
            } else {
                head_detail_view.setBackground(drawable);
                head_broadcast_view.setBackground(drawable);
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth = p.x;
        int screenHeight = p.y;
        rect = new Rect(0, 0, screenWidth, screenHeight);

        showNoData();
        setNavigationBar();
        PalmchatLogUtils.e("myProfile_onResume", alpha + "");
        isPaused=false;
        showTitle(alpha, true);
//        select_Followers_or_FollowingCount();
    }
    private boolean isPaused=false;
    @Override
    protected void onPause() {
        super.onPause();
        showTitle(255, false);

		   /*停止当前正在播放的视频*/
        if (VedioManager.getInstance().getVideoController() != null) {
            VedioManager.getInstance().getVideoController().pause();
        }


        LoadingData = false;
        VoiceManager.getInstance().completion();
        isPaused=true;
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

    /**
     * 设置标题的透明度
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

        // if (relativelayout_title != null) {
        // if (alpha2 != -1) {
        // if (alpha2 > 255) {
        // alpha2 = 255;
        // }
        // relativelayout_title.getBackground().setAlpha(alpha2); // 显示标题是否透明
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
        return false;
    }

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

    /**
     * 请求自己的广播列表数据
     *
     * @param page_id
     */
    private void loadDataFromServer(final int page_id) {
        mAfPalmchat.AfHttpBcgetProfileByAfid(page_id, START_INDEX * LIMIT, LIMIT, afid, null, MyProfileActivity.this);
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

    /**
     * @param broadcastMessageList
     */
    private void setAdapter(final List<AfChapterInfo> broadcastMessageList, int sourceType) {
        if (null == adapter) {
            adapter = new AdapterBroadcastProfile(MyProfileActivity.this, broadcastMessageList, MYPROFILEACTIVITY, this, afProfileInfo);
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
                    msg.arg1 = sourceType;
                    looperHandler.sendMessage(msg);
                }
            } else {
                ToastManager.getInstance().show(context, R.string.no_data);
            }
        }
    }

    /**
     * 判断点击的是“简介”还是“我的广播”
     *
     * @param view
     */
    private void showSelectedOption(View view) {
        switch (view.getId()) {
            case R.id.lin_profile:
                lin_profile.setSelected(true);
                lin_broadcast2.setSelected(false);
                if (mListview != null) {
                    mListview.hide_loadmore();
                }

                	   /*停止当前正在播放的视频*/
                if (VedioManager.getInstance().getVideoController() != null) {
                    VedioManager.getInstance().getVideoController().stop();
                }
                break;
            case R.id.lin_broadcast:


                lin_broadcast2.setSelected(true);
                lin_profile2.setSelected(false);
                if (LoadingData) {
                    if (mListview != null) {
                        mListview.show_loadmore();
                    }
                } else {
                    if (mListview != null) {
                        mListview.hide_loadmore();
                    }
                }

              	   /*停止当前正在播放的视频*/
                if (VedioManager.getInstance().getVideoController() != null) {
                    VedioManager.getInstance().getVideoController().stop();
                }
                break;

        }
    }

    private void stopLoad() {
        LoadingData = false;
        mListview.hide_loadmore();
    }

    /**
     * 初始化照片墙
     */
    private void initSerials() {
        if (entities != null) {
            entities.clear();
        } else {
            entities = new ArrayList<WallEntity>();
        }
        serials = (afProfileInfo != null ? afProfileInfo.getSerials() : new ArrayList<String>());
        for (String sn : serials) {
            entities.add(new WallEntity(afid, sn, afProfileInfo.getServerUrl()));
        }
        if (entities.size() < Consts.AF_AVATAR_MAX_INDEX) {
            entities.add(new WallEntity(null, null, afProfileInfo.getServerUrl(), WallEntity.PhotoTypeAdd));
        }
        albumTitle.setText(getString(R.string.album) + "(" + serials.size() + ")");
        photoWallView.setData(this, entities, afProfileInfo);
        photoWallView.setMyProfileActivity(this);
    }

    /**
     * 初始化地区
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

    /**
     * 初始化页面数据
     *
     * @param profileInfo
     */
    private void setContent(AfProfileInfo profileInfo) {
        if (profileInfo != null) {
            this.afProfileInfo = profileInfo;
            String sign = CommonUtils.isEmpty(profileInfo.signature) ? getString(R.string.default_status) : profileInfo.signature;
            CharSequence text = EmojiParser.getInstance(context).parse(sign, ImageUtil.dip2px(context, 18));
            /** 初始化详情 */
            viewWhatsupValue.setText(text);
            int marital_status = profileInfo.dating.marital_status;
            displayRegion(textRegionValue, profileInfo.country, profileInfo.region, profileInfo.city);
            String showProfession = profileInfo.getShowProfession(this);


            /** 初始化头部 */
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
            imageHeadValue.setBackgroundResource(R.drawable.head_male2);
            imageHeadValue2.setBackgroundResource(R.drawable.head_male2);
            select_Followers_or_FollowingCount(false);
            // WXL 20151013 调UIL的显示头像方法,统一图片管理
            ImageManager.getInstance().DisplayAvatarImage(imageHeadValue, profileInfo.getServerUrl(), profileInfo.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, profileInfo.sex, profileInfo.getSerialFromHead(), null);
            // WXL 20151013 调UIL的显示头像方法,统一图片管理
            ImageManager.getInstance().DisplayAvatarImage(imageHeadValue2, profileInfo.getServerUrl(), profileInfo.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, profileInfo.sex, profileInfo.getSerialFromHead(), null);
        }
    }

    private String getAfid(String afid) {
        return !TextUtils.isEmpty(afid) ? "Palm ID:" + afid : "";
    }

    /**
     * 上传头像
     */
    private void uploadAvatar(String path, String filename, int index) {
        if (is_update) {
            if (!TextUtils.isEmpty(update_sn)) {
                showProgressDialog(R.string.uploading);
                mAfPalmchat.AfHttpAvatarwallModify(path, filename, Consts.AF_AVATAR_FORMAT, update_sn, AF_ACTION_PHOTO_WALL, MyProfileActivity.this, MyProfileActivity.this);
            }
        } else {
            showProgressDialog(R.string.uploading);
            mAfPalmchat.AfHttpAvatarUpload(path, filename, Consts.AF_AVATAR_FORMAT, index, index, MyProfileActivity.this, MyProfileActivity.this);
        }
    }

    private void uploadHead(String path, String filename) {
        showProgressDialog(R.string.uploading);
        mAfPalmchat.AfHttpHeadUpload(path, filename, Consts.AF_AVATAR_FORMAT, null, MyProfileActivity.this, MyProfileActivity.this);
    }

    public void setPhotoWall_update_or_add_State() {
        update_sn = "";
        is_update = false;
        update_index = -1;
    }

    /**
     * 跳转转到相册界面
     */
    public void alertMenu() {

        createLocalFile();
        Intent mIntent = new Intent(this, PreviewImageActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putInt("size", 1);
        mIntent.putExtras(mBundle);
        startActivityForResult(mIntent, MessagesUtils.PICTURE);
    }

    /**
     * 何桂明 2013-10-29 添加界面信息更改时页面刷新问题
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String name = null;
        switch (requestCode) {
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
            case AF_INTENT_DETAIL:
                AfProfileInfo profile = CacheManager.getInstance().getMyProfile();
                setContent(profile);
                if (photoWallView != null) {
                    photoWallView.resetProfile(profile);
                }
                // WXL 20151013 调UIL的显示头像方法,统一图片管理
                ImageManager.getInstance().DisplayAvatarImage(imageHeadValue, profile.getServerUrl(), profile.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, profile.sex, profile.getSerialFromHead(), null);
                ImageManager.getInstance().DisplayAvatarImage(imageHeadValue2, profile.getServerUrl(), profile.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, profile.sex, profile.getSerialFromHead(), null);

                Drawable drawable = FileUtils.getImageFromAssetsFile2(context, SharePreferenceUtils.getInstance(context).getProfileAblum(), head_h);
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    head_detail_view.setBackgroundDrawable(drawable);
                    head_broadcast_view.setBackgroundDrawable(drawable);
                } else {
                    head_detail_view.setBackground(drawable);
                    head_broadcast_view.setBackground(drawable);
                }
                break;
//            case AF_INTENT_ALBUM: //选择背景图返回
//                if (resultCode != 0) {
//                    // heguiming 2013-12-04
//                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SET_B_SUCC);
//                    // MobclickAgent.onEvent(context, ReadyConfigXML.SET_B_SUCC);
//                    name = getNameFromRes(resultCode);
//                    Drawable drawableBackbround = FileUtils.getImageFromAssetsFile2(context, name, head_h);
//
//                    head_detail_view.setBackgroundDrawable(drawableBackbround);
//                    head_broadcast_view.setBackgroundDrawable(drawableBackbround);
//                    SharePreferenceUtils.getInstance(context).setProfileAblum(name);
//                }
//                break;

            case MessagesUtils.PICTURE:// 相册
                if (data != null) {// && resultCode == Activity.RESULT_OK) {
                    if (null != fCurrentFile) {
                        if (currentAction == AF_ACTION_PHOTO_WALL) {

                            ArrayList<String> arrAddPiclist = data.getStringArrayListExtra("photoLs");
                            String cameraFilename = data.getStringExtra("cameraFilename");
                            String path = null;
                            if (arrAddPiclist != null && arrAddPiclist.size() > 0) {
                                path = arrAddPiclist.get(0);
                            } else if (cameraFilename != null) {
                                path = RequestConstant.CAMERA_CACHE + cameraFilename;
                            }
                            if (path != null) {
                                fCurrentFile = new File(path);
                                String newFilepath = RequestConstant.IMAGE_CACHE + fCurrentFile.getName();
                                BitmapUtils.imageCompressionAndSavePhotowall(fCurrentFile.getAbsolutePath(), newFilepath);
                                fCurrentFile = new File(newFilepath);
                                uploadToService(newFilepath);
                            }
                        } else if (currentAction == AF_ACTION_HEAD) {
                            if (null != fCurrentFile) {
                                Bitmap bm = null;

                                ArrayList<String> arrAddPiclist = data.getStringArrayListExtra("photoLs");
                                String cameraFilename = data.getStringExtra("cameraFilename");
                                String path = null;
                                if (arrAddPiclist != null && arrAddPiclist.size() > 0) {// 选相册
                                    path = arrAddPiclist.get(0);
                                } else if (cameraFilename != null) {// 拍照而来
                                    path = RequestConstant.CAMERA_CACHE + cameraFilename;
                                }
                                if (path != null) {
                                    String largeFilename = Uri.decode(path);
                                    File f = new File(largeFilename);
                                    File f2 = FileUtils.copyToImg(largeFilename);// copy到palmchat/camera
                                    if (f2 != null) {
                                        f = f2;
                                    }
                                    cameraFilename = f2.getAbsolutePath();// f.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
                                    cameraFilename = RequestConstant.IMAGE_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
                                    cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
                                    if (CommonUtils.isEmpty(cameraFilename)) {
                                        return;
                                    }
                                    bm = ImageManager.getInstance().loadLocalImageSync(cameraFilename, false);//ImageUtil.getBitmapFromFile(cameraFilename, true);
                                    UploadPictureUtils.getInit().showClipPhoto(bm, head_detail_view, MyProfileActivity.this, sCameraFilename, new IUploadHead() {

                                        @Override
                                        public void onUploadHead(String path, String filename) {
                                            uploadHead(path, filename);
                                        }

                                        @Override
                                        public void onCancelUpload() {

                                        }
                                    });
                                }
                            }
                        }
                    }
                }
                break;
            case IntentConstant.REQUEST_CODE_FOLLOW:
                select_Followers_or_FollowingCount(false);
                break;
        }
    }

    /**
     * 上传服务
     *
     * @param path
     */
    private void uploadToService(String path) {
        if (currentAction == AF_ACTION_PHOTO_WALL) {
            uploadAvatar(path, sCameraFilename, serials.size() + 1);
            PalmchatLogUtils.e(TAG, "----uploadAvatar----");
        } else if (currentAction == AF_ACTION_HEAD) {
            uploadHead(path, sCameraFilename);
            PalmchatLogUtils.e(TAG, "----uploadHead----");
        }
    }

    private boolean isShowTab_suspension;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_profile_suspension:
            case R.id.lin_profile:
                showSelectedOption(v);
                mScrollView.setVisibility(View.VISIBLE);
                rl_broadcast.setVisibility(View.GONE);
                attrLayout.setVisibility(View.VISIBLE);
                if (lin_tab_suspension_linearLayout.getVisibility() == View.VISIBLE) {
                    isShowTab_suspension = true;
                    lin_tab_suspension_linearLayout.setVisibility(View.GONE);
                }
                lin_broadcast2.setSelected(false);
                break;
            case R.id.lin_broadcast:
                if (!lin_broadcast2.isSelected()) {
                    lin_broadcast2.requestFocus();
                    lin_broadcast2.requestFocusFromTouch();
                    lin_broadcast2.setFocusable(false);
                    lin_broadcast2.requestFocus();
                    showSelectedOption(v);
                    if (adapter.getCount() < 1 && !LoadingData) {
//                        loadData();
                        getListFromDBAndRefresh();
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    mScrollView.setVisibility(View.GONE);
                    rl_broadcast.setVisibility(View.VISIBLE);
                    rl_no_data.setVisibility(View.GONE);
                    if (isShowTab_suspension) {
                        lin_tab_suspension_linearLayout.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case R.id.back_button: {
                finish();
                break;
            }
//            case R.id.rl_add_bg: {
//                Intent intent = new Intent(this, ActivityBackgroundChange.class);
//                intent.putExtra(JsonConstant.KEY_FROM, "profile");
//                startActivityForResult(intent, AF_INTENT_ALBUM);
//                break;
//            }
            case R.id.img_photo: {
                if (null != afProfileInfo && !StringUtil.isNullOrEmpty(afProfileInfo.getSerialFromHead())) {
                    String[] serials = {afProfileInfo != null ? afProfileInfo.getSerialFromHead() : null};
                    showTitle(255, false);
                    largeImageDialog = new LargeImageDialog(MyProfileActivity.this, Arrays.asList(serials), afProfileInfo.getServerUrl(), afid, 0, LargeImageDialog.TYPE_AVATAR);
                    largeImageDialog.show();
                    largeImageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            showTitle(alpha, true);
                            setNavigationBar();
                        }
                    });

                }
                break;
            }
            case R.id.op2: {
                // heguiming 2013-12-04
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_MPF);
                // MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_MPF);
                Intent intent = new Intent(context, MyProfileDetailActivity.class);
                startActivityForResult(intent, AF_INTENT_DETAIL);
                break;
            }

            case R.id.lin_following:
                toContactsActivity();
                break;
            case R.id.lin_followers:
                toFollowinfFragment();
                break;
            case R.id.my_qrcode:// 跳转到扫描界面
                Intent intent2 = new Intent(MyProfileActivity.this, MyQrCodeActivity.class);
                context.startActivity(intent2);
                break;
        }
    }

    /**
     * 从数据库获得缓存数据
     */
    private void getListFromDBAndRefresh() {
        AfResponseComm afResponseComm = mAfPalmchat.AfDBBCChapterGetListEx(START_INDEX * LIMIT, LIMIT, Consts.DATA_BROADCAST_MYPROFILE);
        if (afResponseComm != null && afResponseComm.obj != null) {
            if (afResponseComm != null) {
                AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
                if (afPeoplesChaptersList != null) {
                    ArrayList<AfChapterInfo> list_AfChapterInfo = afPeoplesChaptersList.list_chapters;
                    if (list_AfChapterInfo.size() > 0) {
                        setAdapter(list_AfChapterInfo, BROADCAST_DB);

                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                loadData(); //初始化后 都要去取一次新数据
                            }
                        }, 600);
                    } else {
                        loadData();
                    }
                }
            }
        } else {
            loadData();
        }
    }

    /**
     * 点击MyProfile正在关注跳转到联系人界面
     *
     * @param
     */
    private void toContactsActivity() {
        Intent intent = new Intent();
        intent.setClass(MyProfileActivity.this, ContactsActivity.class);
        intent.putExtra("id", "0");// "0"不代表任何意思，只是为了区分
        startActivityForResult(intent, IntentConstant.REQUEST_CODE_FOLLOW);
    }

    private void toFollowinfFragment() {
        SharePreferenceUtils.getInstance(getApplicationContext()).setIsFolloed(afProfileInfo.afId, false);
        mIVFollowed.setVisibility(View.INVISIBLE);
        mIVFollowed2.setVisibility(View.INVISIBLE);
        Intent intent = new Intent();
        intent.setClass(MyProfileActivity.this, ContactsActivity.class);
        intent.putExtra("id", "1");// "1"不代表任何意思，只是为了区分
        startActivityForResult(intent, IntentConstant.REQUEST_CODE_FOLLOW);
    }

//    /**
//     * 可选择的背景图片
//     *
//     * @param resultCode
//     * @return
//     */
//    private String getNameFromRes(int resultCode) {
//        switch (resultCode) {
//            case R.drawable.pic_store01:
//                return "01";
//            case R.drawable.pic_store02:
//                return "02";
//            case R.drawable.pic_store03:
//                return "03";
//            case R.drawable.pic_store04:
//                return "04";
//            case R.drawable.pic_store05:
//                return "05";
//            case R.drawable.pic_store06:
//                return "06";
//            case R.drawable.pic_store07:
//                return "07";
//            case R.drawable.pic_store08:
//                return "08";
//            default:
//                return SharePreferenceUtils.getInstance(context).getProfileAblum();
//        }
//    }

    public void onCallback() {
        initSerials();
    }

    public void onCallback_alertMenu(String sn, int index) {
        is_update = true;
        update_sn = sn;
        update_index = index;
        currentAction = MyProfileActivity.AF_ACTION_PHOTO_WALL;
        alertMenu();
    }

    private void createLocalFile() {
        sCameraFilename = ClippingPicture.getCurrentFilename();
        PalmchatLogUtils.e("camera->", sCameraFilename);
        SharePreferenceService.getInstance(MyProfileActivity.this).savaFilename(sCameraFilename);
        fCurrentFile = new File(RequestConstant.CAMERA_CACHE, sCameraFilename);
        CacheManager.getInstance().setCurFile(fCurrentFile);
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        PalmchatLogUtils.e(TAG, "----AfOnResult----Flag:" + flag + "----code:" + code);
        dismissProgressDialog();
        if (code == Consts.REQ_CODE_SUCCESS) {// 请求数据成功
            if (flag == Consts.REQ_GET_INFO) {// 请求信息
                // get profile
                if (result != null && result instanceof AfProfileInfo) {
                    AfProfileInfo afinfo = (AfProfileInfo) result;
                    /*if(afProfileInfo==null){//被回收过的情况才需要重新赋值，否则只是获取Follow数量的情况
                		//只所以要加这个区别是因为login返回的MyProfile会有一些额外信息保存 所以这里不能直接覆盖
	                    setContent(afinfo);
	                    CacheManager.getInstance().setMyProfile(afinfo);
                	}*/
                    //设置Follow数量
                    JsonConstant.getMyProfileFromServer_forFollowCount_byPalmID = afinfo.afId;
//                    following_count=afinfo.follow_count;
                    followers_count = afinfo.followers_count;
                    FolllowerUtil.getInstance().setFollowerCount(followers_count);
                    select_Followers_or_FollowingCount(true);
                    progressBar.setVisibility(View.GONE);
                    edit_profile.setVisibility(View.VISIBLE);
                    mAfPalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, afinfo);
                    showIsFollowed();
                }
            } else if (flag == Consts.REQ_AVATAR_UPLOAD) {// 请求上传头像
                // 上传成功
                PalmchatLogUtils.i(TAG, "avatar upload success!!!" + user_data + "->result = " + result);
                // 将图片copy到 avatar目录下
                int index = (Integer) user_data;
                if (index > 0) {
                    if (((AfRespAvatarInfo) (result)).serial != null) {
                        // heguiming 2013-12-04
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ADD_PWALL);
                        // MobclickAgent.onEvent(context,
                        // ReadyConfigXML.ADD_PWALL);
                        copy(AF_ACTION_PHOTO_WALL, result);
                    }
                } else {
                    // heguiming 2013-12-04
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SET_H_SUCC);
                    // MobclickAgent.onEvent(context,
                    // ReadyConfigXML.SET_H_SUCC);
                    PalmchatLogUtils.i(TAG, "AfRespAvatarInfo = " + ((AfRespAvatarInfo) (result)).afid);
                    copy(AF_ACTION_HEAD, result);
                }
            } else if (flag == Consts.REQ_UPDATE_INFO) {// 更新个人信息
                if (result != null && result instanceof AfProfileInfo) {
                    String sign = ((AfProfileInfo) result).signature;
                    CharSequence text = EmojiParser.getInstance(context).parse(sign, ImageUtil.dip2px(context, 18));
                    viewWhatsupValue.setText(text);
                    CacheManager.getInstance().getMyProfile().signature = sign;
                }
            } else if (flag == Consts.REQ_AVATAR_WALL_MODIFY) { //返回广播数据列表
                if (((AfRespAvatarInfo) (result)).serial != null) {
                    copy(AF_ACTION_PHOTO_WALL_UPDATE, result);
                }
            } else if (flag == Consts.REQ_BCGET_PROFILE_BY_AFID) {
                if (null != result) {
                    AfResponseComm afResponseComm = (AfResponseComm) result;
                    AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
                    if (afPeoplesChaptersList != null) {
                        ArrayList<AfChapterInfo> list_AfChapterInfo = afPeoplesChaptersList.list_chapters;
                        if (list_AfChapterInfo.size() > 0) {
                            setAdapter(list_AfChapterInfo, BROADCAST_SERVER);
                        } else {
                            if ( !isLoadMore) {// 如果刷新不出数据 就清历史记录
                                if ( adapter != null) {
                                    adapter.clear();
                                    adapter.notifyDataSetChanged();
                                    showNoData();
                                }
                                mAfPalmchat.AfDBBCChapterDeleteByType(Consts.DATA_BROADCAST_MYPROFILE);
                            }
                            ToastManager.getInstance().show(context, R.string.no_data);


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


            }


        } else {
            progressBar.setVisibility(View.GONE);
            edit_profile.setVisibility(View.VISIBLE);
            if (code == Consts.REQ_CODE_UNNETWORK) {
                if (flag == Consts.REQ_UPDATE_INFO) {
                    if (!StringUtil.isNullOrEmpty(oldSign)) {
                        CharSequence text = EmojiParser.getInstance(context).parse(oldSign, ImageUtil.dip2px(context, 18));
                        viewWhatsupValue.setText(text);
                        CacheManager.getInstance().getMyProfile().signature = oldSign;
                        afProfileInfo.signature = oldSign;
                    }
                }
                Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
            } else if (flag == Consts.REQ_BCGET_PROFILE_BY_AFID) {
                if (code == Consts.REQ_CODE_UNNETWORK) {
                    if (START_INDEX > 0) {
                        START_INDEX--;
                    }
                }
                stopLoad();
                if (code == Consts.REQ_CODE_104) {// 特殊处理-104
                    PalmchatLogUtils.e(TAG, "----code:" + code);
                    loadData();
                } else {
                    showNoData();
                }
            } else {
                if (flag == Consts.REQ_UPDATE_INFO) {
                    if (!StringUtil.isNullOrEmpty(oldSign)) {
                        CharSequence text = EmojiParser.getInstance(context).parse(oldSign, ImageUtil.dip2px(context, 18));
                        viewWhatsupValue.setText(text);
                        CacheManager.getInstance().getMyProfile().signature = oldSign;
                        afProfileInfo.signature = oldSign;
                    }
                }
                Toast.makeText(this, R.string.failure, Toast.LENGTH_SHORT).show();
            }
            Consts.getInstance().showToast(context, code, flag, http_code);
        }
    }

    /**
     * 将图片copy到avatar文件夹下
     *
     * @param type
     * @param result
     */
    private void copy(final int type, final Object result) {
        // copy
        new Thread(new Runnable() {

            @Override
            public void run() {
                String path = fCurrentFile.getAbsolutePath();
                AfRespAvatarInfo info = (AfRespAvatarInfo) (result);

                AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
                if (profileInfo != null) {
                    String pathDestination = RequestConstant.IMAGE_UIL_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(CommonUtils.getAvatarUrlKey(profileInfo.getServerUrl(), afid, info.serial, Consts.AF_HEAD_MIDDLE));
                    File outFile = new File(pathDestination);
                    // String fileName = afid + Consts.AF_HEAD_MIDDLE +
                    // info.serial;
                    // File outFile = new File(RequestConstant.AVATAR_CACHE
                    // + Consts.AF_HEAD_MIDDLE, fileName);
                    FileUtils.copyToImg(path, outFile.getAbsolutePath());

                    handler.obtainMessage(type, result).sendToTarget();
                    if (fCurrentFile != null) {
                        fCurrentFile.delete();
                    }
                }
            }
        }).start();
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

    /**
     * 新的用户还没有上传头像提示先完成上传头像
     */
    public void showSetPhotoWallDialog() {
        AppDialog dialog = new AppDialog(context);
        dialog.createConfirmDialog(context, getString(R.string.pealse_upload_your_picture_first), new OnConfirmButtonDialogListener() {
            @Override
            public void onRightButtonClick() {
                currentAction = AF_ACTION_HEAD;
                CacheManager.getInstance().setCurAction(currentAction);
                alertMenu();
            }

            @Override
            public void onLeftButtonClick() {

            }
        });
        dialog.show();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Object result = msg.obj;
            switch (msg.what) {
                case 1:
                    initSerials();
                    lin_profile.performClick();
                    break;
                case AF_ACTION_PHOTO_WALL_UPDATE:
                    if (photoWallView != null) {
                        PalmchatLogUtils.i(TAG, "serials->size = " + serials.size() + " entities->size = " + entities.size());
                        AfRespAvatarInfo afRespAvatarInfo = (AfRespAvatarInfo) result;
                        if (afRespAvatarInfo != null && update_index >= 0) {
                            String newSn = afRespAvatarInfo.serial;
                            PalmchatLogUtils.e("--------old_serials----------", serials.toString());
                            serials.set(update_index, newSn);
                            PalmchatLogUtils.e("--------new_Serials----------", serials.toString());
                            entities.get(update_index).sn = newSn;
                            afProfileInfo.serials.set(update_index, newSn);
                            PalmchatLogUtils.e("--------new_afProfileInfo.serials----------", afProfileInfo.serials.toString());
                            CacheManager.getInstance().setMyProfile(afProfileInfo);
                            photoWallView.setData(MyProfileActivity.this, entities, afProfileInfo);
                            PalmchatLogUtils.e("--------CacheManager.getInstance().getMyProfile().serials----------", CacheManager.getInstance().getMyProfile().serials.toString());
                            mAfPalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, CacheManager.getInstance().getMyProfile());
                            setPhotoWall_update_or_add_State();
                            if (photoWallView != null && photoWallView.largeImageDialog != null) {
                                if (photoWallView.largeImageDialog.isShowing()) {
                                    photoWallView.largeImageDialog.dismiss();
                                }
                            }
                        }
                    }
                    break;
                case AF_ACTION_PHOTO_WALL:
                    if (photoWallView != null) {
                        PalmchatLogUtils.i(TAG, "serials->size = " + serials.size() + " entities->size = " + entities.size());
                        List<WallEntity> entities = photoWallView.getList();
                        initSerials();
                        serials.add(((AfRespAvatarInfo) (result)).serial);
                        entities.add(serials.size() - 1, new WallEntity(afid, serials.get(serials.size() - 1), afProfileInfo.getServerUrl()));
                        if (entities.size() > Consts.AF_AVATAR_MAX_INDEX) {
                            photoWallView.removeAddPhoto();
                        }
                        photoWallView.setData(MyProfileActivity.this, entities, afProfileInfo);
                        photoWallView.setMyProfileActivity(MyProfileActivity.this);
                        CacheManager.getInstance().getMyProfile().serialToAttr1();
                        mAfPalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, CacheManager.getInstance().getMyProfile());
                        albumTitle.setText(getString(R.string.album) + "(" + serials.size() + ")");
                        PalmchatLogUtils.i(TAG, "serials->size = " + serials.size() + " entities->size = " + entities.size());
                        setPhotoWall_update_or_add_State();
                    }
                    break;
                case AF_ACTION_HEAD:
                    StringBuffer head_image_path = new StringBuffer();
                    if (((AfRespAvatarInfo) (result)).afid != null) {
                        head_image_path.append(((AfRespAvatarInfo) (result)).afid);
                    }
                    if (((AfRespAvatarInfo) (result)).serial != null) {
                        head_image_path.append(",");
                        head_image_path.append(((AfRespAvatarInfo) (result)).serial);
                    }
                    if (((AfRespAvatarInfo) (result)).host != null) {
                        head_image_path.append(",");
                        head_image_path.append(((AfRespAvatarInfo) (result)).host);
                    }
                    AfProfileInfo profile = CacheManager.getInstance().getMyProfile();
                    if (profile != null) {
                        profile.head_img_path = head_image_path.toString();
                    }

                    // 何桂明 2013-10-23
                    byte sex = 0;
                    // 调UIL的显示头像方法
                    ImageManager.getInstance().DisplayAvatarImage(imageHeadValue, profile.getServerUrl(), afid, Consts.AF_HEAD_MIDDLE, sex, profile.getSerialFromHead(), null);
                    ImageManager.getInstance().DisplayAvatarImage(imageHeadValue2, profile.getServerUrl(), afid, Consts.AF_HEAD_MIDDLE, sex, profile.getSerialFromHead(), null);
                    dismissProgressDialog();
                    mAfPalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, profile);
                    break;
                case NODTIFYDATASETCHANGED:
                    if (LoadingData) {
                        LoadingData = false;
                    }
                    List<AfChapterInfo> broadcastMessageList = (List<AfChapterInfo>) msg.obj;
                    adapter.notifyDataSetChanged(broadcastMessageList, isLoadMore);
                    break;
                default:
                    break;
            }

        }

    };

    /**
     * 处理Follower follwering刷新相关
     *
     * @param event
     */
    public void onEventMainThread(RefreshFollowerFolloweringOrGroupListEvent event) {
        if (event.getType() == Constants.REFRESH_FOLLOWING || event.getType() == Constants.REFRESH_FOLLOWER) {
            select_Followers_or_FollowingCount(false);
            showIsFollowed();
        }
    }

    /**
     * 处理事件
     *
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

    /**
     * 刷新followers和following的数据
     */
    private void select_Followers_or_FollowingCount(boolean isFirst) {
        int following_count = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).size(false, true);
        if (!isFirst) {
            followers_count = FolllowerUtil.getInstance().getSizeForDefault();
        }
        textFollowing.setText(String.valueOf(following_count));
        textFollowers.setText(String.valueOf(followers_count));
        textFollowing2.setText(String.valueOf(following_count));
        textFollowers2.setText(String.valueOf(followers_count));
    }


    class LooperThread extends Thread {
        private static final int SETPROFILE_AND_CHECK_ISLIKE = 7004;
        private static final int DELETE_DB_IS_SERVER_DATA = 7005;

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

                            //写入数据库 
                            if (!isLoadMore && msg.arg1 == BROADCAST_SERVER) {//不是loadmore且是从服务器取的数据的情况下写入缓存
                                mAfPalmchat.AfDBBCChapterDeleteByType(Consts.DATA_BROADCAST_MYPROFILE);

                                for (int i = size - 1; i >= 0; i--) {
                                    BroadcastUtil.ServerData_AfDBBCChapterInsert(Consts.DATA_BROADCAST_MYPROFILE, broadcastMessageList.get(i)); // insert
                                    // DATA
                                    // to
                                    // DB
                                }
                            }
                            break;
                        case DELETE_DB_IS_SERVER_DATA:
                            break;
                        default:
                            break;
                    }
                }
            };

            Looper.loop();
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
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (ScrollY > 0) {
                        // int scroly= ScrollY-50;
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
    public void AfOnProgress(int httpHandle, int flag, int progress, Object user_data) {

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
        if(!isPaused ) {//OnPause后 这里竟然会被调用 奇怪
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
        // showTitle(alpha);
        // int tempAlpha = StatusBarAlpha;
        // if (alpha >= max) {
        // tempAlpha = 255;
        // } else {
        // tempAlpha = StatusBarAlpha + alpha;
        // if (tempAlpha > 255) {
        // tempAlpha = 255;
        // }
        // }
        // tintManager.setStatusBarAlpha(tempAlpha);
        //
        // }
    }


}
