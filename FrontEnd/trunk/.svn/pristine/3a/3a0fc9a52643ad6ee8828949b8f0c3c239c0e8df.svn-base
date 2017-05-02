package com.afmobi.palmchat.ui.activity.tagpage;

import java.util.ArrayList;
import java.util.List;

import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.MenuAdapter;
import com.afmobi.palmchat.ui.activity.main.ChatsContactsActivity;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountDetailsActivity;
import com.afmobi.palmchat.ui.activity.social.ActivitySendBroadcastMessage;
import com.afmobi.palmchat.ui.activity.social.EditBroadcastPictureActivity;
import com.afmobi.palmchat.ui.customview.SendBroadBtnListener;
import com.afmobi.palmchat.ui.customview.SendBroadcastMaskingView;
import com.afmobi.palmchat.ui.customview.UnderlinePageIndicator;
import com.afmobi.palmchat.ui.customview.videoview.VedioManager;
import com.afmobi.palmchat.util.AnimationController;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobigroup.gphone.R;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TagPageActivity extends BaseFragmentActivity implements OnClickListener {

    private final String TAG = TagPageActivity.this.getClass().getSimpleName();
    /**
     * 左tag标记
     */
    private final int LEFT_TAG = 0;
    /**
     * 右tag标记
     */
    private final int RIGHT_TAG = 1;
    /**
     * 标题
     */
    private TextView mTV_Title;
    /**
     * fragment列表
     */
    private ArrayList<Fragment> mListFragments = new ArrayList<Fragment>();
    /**
     * viewpager
     */
    private ViewPager mViewPager;
    /**
     * 适配器
     */
    private MenuAdapter mMennuAdapter;
    /***/
    private TopPostsFragment mTopPostsFragment;
    /***/
    private RecentPostsFragment mRecentPostsFragment;
    /**
     * 左tag
     */
    private TextView mTV_LeftTag;
    /**
     * 右tag
     */
    private TextView mTV_RightTag;
    /**
     * 当前tag
     */
    private String mCurTagName;
    /**
     * 发送照片
     */
    private LinearLayout mLy_Photo;
    /**
     * 发送声音
     */
    private LinearLayout mLy_Voice;
    /**
     * 分享按钮
     */
    private ImageView mIv_Share;
    /**
     * 公共账号关联标记
     */
    private ImageView mIv_Association;
    private String mCurPublicAccount;
    /**
     * 由于有机器差异，不确定第一次一定是运行隐藏，所有给个标记初始化
     */
    private boolean mIsFirstRun = true;
    /**
     * 标题
     */
    private View mV_Title;
    /**
     * tabs
     */
    private View mV_Tabs;
    /**
     * title原始坐标
     */
    private float mTitleOriYCoord;
    /**
     * tabs原始坐标
     */
    private float mTabsOriYCoord;
    /**
     * 悬浮按钮原始坐标
     */
    private float mFabOriYCoord = -1;
    /**
     * 屏幕高度
     */
    private int mScreenHeight = -1;
    /**
     * title的实际高度
     */
    private float mTabYDiffTitleY = -1;
    /**
     * 标题栏高度
     */
    private int mStatusBarHeight = -1;
    /**
     * 刷新成功的视图
     */
    private View mV_TopRefresh;
    /**
     * 发送布局
     */
    private View mV_Publish;
    /**
     * 初始化标记
     */
    private boolean mUnInit = true;
    /**
     * 底部发送广播按钮
     */
    private ImageButton mBtn_Sendbroad;
    /**
     * 发送按钮布局
     */
    private View mV_Sendbroad;
    /**
     * 发图片按钮
     */
    private ImageButton mIBtn_SendPhoto;
    /**
     * 发图片文字
     */
    private TextView mTv_SendPhoto;
    /**
     * 发声音按钮
     */
    private ImageButton mIBtn_SendVoice;
    /**
     * 发声音文字
     */
    private TextView mTv_SendVoice;
    /**
     * 发送状态
     */
    private boolean mBool_ScrollState;
    /**
     * 发送布局
     */
    private View mV_Maintab_Send_layout;
    private SendBroadcastMaskingView mSendBroadcastMaskingView;
    /**
     * handler
     */
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        public void run() {
            mHandler.removeCallbacks(mRunnable);
            showShowAnimation();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        findViews();
        init();
    }

    public void findViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_tagpage);

        mV_Title = findViewById(R.id.tagpager_title_layout);
        mV_Title.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });
        mV_Tabs = findViewById(R.id.tagpage_tab_layout_id);
        mV_TopRefresh = findViewById(R.id.tagpage_top_refresh);
        mTV_Title = (TextView) findViewById(R.id.title_text);
        mTV_LeftTag = (TextView) findViewById(R.id.tagpage_tab_left_id);
        mTV_LeftTag.setOnClickListener(new TagOnClickListener(LEFT_TAG));
        mTV_RightTag = (TextView) findViewById(R.id.tagpage_tab_right_id);
        mTV_RightTag.setOnClickListener(new TagOnClickListener(RIGHT_TAG));

        mIv_Share = (ImageView) findViewById(R.id.op3);
        mIv_Share.setOnClickListener(this);
        mIv_Share.setVisibility(View.VISIBLE);

        mIv_Association = (ImageView) findViewById(R.id.op2);
        mIv_Association.setOnClickListener(this);
        mIv_Association.setBackgroundResource(R.drawable.btn_tagpage_publicaccount_selector);

        mViewPager = (ViewPager) findViewById(R.id.tagpage_viewpager_id);
        mMennuAdapter = new MenuAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMennuAdapter);
        mViewPager.setOffscreenPageLimit(1);

        UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.tagpage_indicator_id);
        indicator.setViewPager(mViewPager);
        indicator.setFades(false);
        indicator.setSelectedColor(getResources().getColor(R.color.log_blue));
        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub
                setOtherListViewOffsetScrol(state);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                showSelectedOption(position);
                if (VedioManager.getInstance().getVideoController() != null)
                    VedioManager.getInstance().getVideoController().stop();

                //如果有语音正在播放则停止
                if (VoiceManager.getInstance().isPlaying()) {
                    VoiceManager.getInstance().pause();
                }
            }

        });

        mSendBroadcastMaskingView = (SendBroadcastMaskingView) findViewById(R.id.tagepage_fab_id);
        mSendBroadcastMaskingView.setSendBroadBtnListener(new SendBroadBtnListener() {
            @Override
            public void sendVoice() {
                Intent intent = new Intent(TagPageActivity.this, ActivitySendBroadcastMessage.class);
                intent.putExtra(JsonConstant.KEY_SENDBROADCAST_TYPE, Consts.BR_TYPE_VOICE_TEXT);
                intent.putExtra(JsonConstant.KEY_SENDBROADCAST_TAGNAME, mCurTagName);
                startActivityForResult(intent, IntentConstant.REQUEST_CODE_SENDBROADCAST);
            }

            @Override
            public void sendPhoto() {
                Intent intent = new Intent(TagPageActivity.this, EditBroadcastPictureActivity.class);
                intent.putExtra(JsonConstant.KEY_SENDBROADCAST_TYPE, JsonConstant.KEY_SENDBROADCAST_TYPE_CAMERA);
                intent.putExtra(JsonConstant.KEY_SENDBROADCAST_TAGNAME, mCurTagName);
                startActivityForResult(intent, IntentConstant.REQUEST_CODE_SENDBROADCAST);
            }

            @Override
            public void dismiss() {
                mV_Sendbroad.setVisibility(View.VISIBLE);
            }
        });

        //发送广播部分
        mV_Sendbroad = findViewById(R.id.tagepage_tab_fab_id);
        mBtn_Sendbroad = (ImageButton) findViewById(R.id.tagepage_broadcast_fab_id);
        mBtn_Sendbroad.setOnClickListener(this);
        mV_Maintab_Send_layout = findViewById(R.id.maintab_send_layout);

    }

    @SuppressLint("DefaultLocale")
    public void init() {
        // TODO Auto-generated method stub
        Intent intent = getIntent();
        mCurTagName = intent.getStringExtra(IntentConstant.TITLENAME);
        mTV_Title.setText(mCurTagName.toUpperCase());
        Bundle bundle = new Bundle();
        bundle.putString(IntentConstant.TITLENAME, mCurTagName);
        findViewById(R.id.back_button).setOnClickListener(this);
        mTopPostsFragment = new TopPostsFragment();
        mTopPostsFragment.setArguments(bundle);
        mRecentPostsFragment = new RecentPostsFragment();
        mRecentPostsFragment.setArguments(bundle);
        mListFragments.add(mTopPostsFragment);
        mListFragments.add(mRecentPostsFragment);
        mMennuAdapter.updateData(mListFragments);
        showSelectedOption(0);

        mBtn_Sendbroad.setVisibility(View.INVISIBLE);
        mIv_Share.setBackgroundResource(R.drawable.btn_nav_loading);
        mIv_Share.setClickable(false);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_offline_main_profile);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        animation.setInterpolator(linearInterpolator);
        animation.setDuration(1000);
        mIv_Share.startAnimation(animation);
    }

    /**
     * 分享tag需要图片,在广播里面随便找个
     *
     * @return url
     */
    private String getImageUrl() {
        String imageUrl = "";
        if (null != mTopPostsFragment) {
            imageUrl = mTopPostsFragment.getImageUrl();
        }
        if (TextUtils.isEmpty(imageUrl)) {
            imageUrl = mRecentPostsFragment.getImageUrl();
        }
        return imageUrl;
    }

    /**
     * 跳转到公共帐号详情页面
     */
    private void toPublicAccountDetail() {
        AfFriendInfo afFriendInfo = new AfFriendInfo();
        afFriendInfo.name = "";
        afFriendInfo.afId = mCurPublicAccount;

        Intent intent = new Intent(this, PublicAccountDetailsActivity.class);
        intent.putExtra("Info", afFriendInfo);
        startActivity(intent);
    }

    /**
     * 调节选中
     *
     * @param position
     */
    private void showSelectedOption(int position) {
        switch (position) {
            case LEFT_TAG: {
                mTV_LeftTag.setSelected(true);
                mTV_RightTag.setSelected(false);
                break;
            }
            case RIGHT_TAG: {
                mTV_LeftTag.setSelected(false);
                mTV_RightTag.setSelected(true);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 设置显示关联图标
     *
     * @param publicAccount
     */
    public void setPublicView(String publicAccount) {
        if (!TextUtils.isEmpty(publicAccount)) {
            mCurPublicAccount = publicAccount;
            mIv_Association.setVisibility(View.VISIBLE);
        } else {
            mCurPublicAccount = "";
            mIv_Association.setVisibility(View.GONE);
        }
    }

    /**
     * 获取当前tag号
     *
     * @return
     */
    public String getCurTagName() {
        return mCurTagName;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.back_button: {
                finish();
                break;
            }
            //公共账号关联,跳转到公共账号详情
            case R.id.op2: {
                toPublicAccountDetail();
                break;
            }

            case R.id.op3: {
                List<MainAfFriendInfo> tempList = CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).getList();//获取最近联系人的数据
                List<AfFriendInfo> mAfFriendInfosList = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).getList();//获取联系人(好友)的数据
                List<AfGrpProfileInfo> grp_cacheList = CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).getList();//获取群组列表数据
                if (tempList.size() <= 0 && mAfFriendInfosList.size() <= 0) {
                    ToastManager.getInstance().show(getApplicationContext(), R.string.no_friends_or_group_chat);
                } else {
                    int tplist = 0;//非官方账号的时候就++
                    for (int i = 0; i < tempList.size(); i++) {
                        MainAfFriendInfo info = tempList.get(i);
                        AfFriendInfo afFriendInfo = info.afFriendInfo;
                        if (null != info && null != afFriendInfo) {
                            if (afFriendInfo.afId.toLowerCase().startsWith("g") || afFriendInfo.afId.toLowerCase().startsWith("r")) {
                                continue;
                            }
                        }
                        tplist++;
                    }
                    int infolist = 0;
                    for (int s = 0; s < mAfFriendInfosList.size(); s++) {
                        AfFriendInfo afFriendInfo = mAfFriendInfosList.get(s);
                        if (null != afFriendInfo) {
                            if (afFriendInfo.afId.toLowerCase().startsWith("g") || afFriendInfo.afId.toLowerCase().startsWith("r")) {
                                continue;
                            }
                        }
                        infolist++;
                    }
                    if (tplist > 0 || infolist > 0 || grp_cacheList.size() > 0) {
                        Intent intent = new Intent();
                        intent.setClass(this, ChatsContactsActivity.class);
                        intent.putExtra(JsonConstant.KEY_TAG_NAME, mCurTagName);
                        intent.putExtra(JsonConstant.KEY_IS_SHARE_TAG, true);
                        intent.putExtra(JsonConstant.KEY_TAG_URL, getImageUrl());
                        startActivityForResult(intent, IntentConstant.REQUEST_CODE_TAGPAGE);
                    } else {
                        ToastManager.getInstance().show(getApplicationContext(), R.string.no_friends_or_group_chat);
                    }
                }
                break;
            }
            case R.id.tagepage_broadcast_fab_id: {
                doWithFabClick();
                break;
            }

            default:
                break;
        }
    }

    /**
     * 处理发广播点击
     */
    private void doWithFabClick() {
        mV_Sendbroad.setVisibility(View.INVISIBLE);
        mSendBroadcastMaskingView.show(mHandler);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        //super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, intent);
        //分享Tag返回
        switch (arg0) {
            case IntentConstant.SHARE_BROADCAST:
            case IntentConstant.REQUEST_CODE_TAGPAGE: {
                if (null != intent) {
                    boolean isSuccess = intent.getBooleanExtra(JsonConstant.KEY_SEND_IS_SEND_SUCCESS, false);
                    String tempTipContent = intent.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL);
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

            case IntentConstant.REQUEST_CODE_SENDBROADCAST: {
                //mViewPager.setCurrentItem(1);
                if (null != mRecentPostsFragment) {
                    //	mRecentPostsFragment.onRefresh(null);
                }
                if (mSendBroadcastMaskingView.getVisibility() == View.VISIBLE) {
                    mSendBroadcastMaskingView.setVisibility(View.GONE);
                    mV_Sendbroad.setVisibility(View.VISIBLE);

                }

                break;
            }

            case IntentConstant.REQUEST_CODE_SHARETOFACEBOOK: {
                if (null != intent) {
                    boolean isSuccess = intent.getBooleanExtra(JsonConstant.KEY_SEND_IS_SEND_SUCCESS, false);
                    ToastManager.getInstance().showShareBroadcast(this, DefaultValueConstant.SHARETOASTTIME, isSuccess);
                }


                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (VedioManager.getInstance().getVideoController() != null)
            VedioManager.getInstance().getVideoController().pause();
        VoiceManager.getInstance().completion();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (VedioManager.getInstance().getVideoController() != null)
            VedioManager.getInstance().getVideoController().stop();

    }


    @Override
    public void onBackPressed() {
        if (mSendBroadcastMaskingView.getVisibility() == View.VISIBLE) {
            mSendBroadcastMaskingView.setVisibility(View.GONE);
            mV_Sendbroad.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (null != mListFragments) {
            mListFragments.clear();
            mListFragments = null;
        }
        if (mMennuAdapter != null) {
            mMennuAdapter.cleanData();
        }
        mHandler.removeCallbacks(mRunnable);

        boolean isPlay = VoiceManager.getInstance().isPlaying();
        if (isPlay) {
            VoiceManager.getInstance().completion();
        }
    }

    /***
     * @author tag监听类
     */
    public class TagOnClickListener implements View.OnClickListener {
        private int index = 0;

        public TagOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            if (index == mViewPager.getCurrentItem()) {
                return;
            }
            stopListViewScroll();
            setOtherListViewOffSet(index);
            mViewPager.setCurrentItem(index);
        }
    }

    public void setCurrentItem(int index) {
        if (index != mViewPager.getCurrentItem()) {
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 第一次舒适化坐标值
     */
    private void initCoord(float titleYCoord, float tabsYCoord, float fabYCoord) {
        mTitleOriYCoord = titleYCoord;
        mTabsOriYCoord = tabsYCoord;
        mFabOriYCoord = fabYCoord;
        mStatusBarHeight = CommonUtils.getStatusBarHeight(this);
        mScreenHeight = CommonUtils.getRealtimeWindowHeight(this);
        mTabYDiffTitleY = mTabsOriYCoord - mTitleOriYCoord;
        mIsFirstRun = false;
    }

    /**
     * 下拉显示title
     *
     * @param scrollH
     */
    @SuppressLint("NewApi")
    public void showTitle(float scrollH, boolean isExtraTabs) {
        float titleYCoord = mV_Title.getY();
        float tabsYCoord = mV_Tabs.getY();
        float fabYCoord = mV_Sendbroad.getY();
        float layoutY = titleYCoord + scrollH;
        //悬浮按钮
        float layoutYfab = fabYCoord - scrollH;

        if (mIsFirstRun) {
            initCoord(titleYCoord, tabsYCoord, fabYCoord);
        }

        if (layoutY < mTitleOriYCoord) {
            mV_Title.setY(layoutY);
        } else {
            layoutY = mTitleOriYCoord;
            mV_Title.setY(mTitleOriYCoord);
        }

        layoutY = layoutY + mTabYDiffTitleY;
        mV_Tabs.setY(layoutY);
        if (mTabsOriYCoord - layoutY < 1) {
            resetListViewPosition();
        }

        //悬浮按钮
        if (mFabOriYCoord > (CommonUtils.getRealtimeWindowHeight(this) / 2)) {
            if (layoutYfab > mFabOriYCoord) {
                mV_Sendbroad.setY(layoutYfab);
                mBool_ScrollState = true;
            } else {
                mV_Sendbroad.setY(mFabOriYCoord);
            }
        } else {
            mFabOriYCoord = mV_Sendbroad.getY();
        }
        mHandler.removeCallbacks(mRunnable);
    }

    /**
     * 移动titile
     *
     * @param scrollH
     * @param isExtraTabs
     */
    public void moveTitle(float scrollH, boolean isExtraTabs) {
        if (scrollH < 0.0) {
            hideTitle(scrollH, isExtraTabs);
        } else if (scrollH > 0.0) {
            showTitle(scrollH, isExtraTabs);
        }
    }

    /**
     * 隐藏tabs
     *
     * @param scrollH
     */
    @SuppressLint("NewApi")
    public void hideTitle(float scrollH, boolean isExtraTabs) {
        float titleYCoord = mV_Title.getY();
        float tabsYCoord = mV_Tabs.getY();
        float fabYCoord = mV_Sendbroad.getY();
        //悬浮按钮
        float layoutYfab = fabYCoord - scrollH;
        if (mIsFirstRun) {
            initCoord(titleYCoord, tabsYCoord, fabYCoord);
        }

        float layoutY = titleYCoord + scrollH;
        if (layoutY + mTabYDiffTitleY >= mStatusBarHeight) {
            mV_Title.setY(layoutY);
        } else {
            layoutY = -mTabYDiffTitleY + mStatusBarHeight;
            mV_Title.setY(layoutY);
        }

        layoutY = layoutY + mTabYDiffTitleY;
        mV_Tabs.setY(layoutY);

        if (mFabOriYCoord > (CommonUtils.getRealtimeWindowHeight(this) / 2)) {
            if (layoutYfab < mScreenHeight) {
                mBool_ScrollState = false;
                mV_Sendbroad.setY(layoutYfab);
            } else {
                mV_Sendbroad.setY(mScreenHeight);
            }
        } else {
            mFabOriYCoord = mV_Sendbroad.getY();
        }

        mHandler.removeCallbacks(mRunnable);

    }

    /**
     * 重置title位置
     */
    @SuppressLint("NewApi")
    public void resetTitlePosition() {
        if (mV_Title.getY() < mTitleOriYCoord) {
            mV_Title.setY(mTitleOriYCoord);
        }

        if (mV_Tabs.getY() < mTabsOriYCoord) {
            mV_Tabs.setY(mTabsOriYCoord);
        }
        if (mFabOriYCoord > (CommonUtils.getRealtimeWindowHeight(this) / 2)) {
            mV_Sendbroad.setY(mFabOriYCoord);
        }

        resetListViewPosition();
    }

    /**
     * 设置列表偏移量
     *
     * @param index
     */
    @SuppressLint("NewApi")
    private void setOtherListViewOffSet(int index) {
        if (mV_Tabs.getY() > mTabsOriYCoord) {
            return;
        }
        float offSet = Math.abs(mV_Tabs.getY() - mTabsOriYCoord);
        switch (index) {
            case 0: {
                mTopPostsFragment.setListViewOffSet(offSet);
                break;
            }

            case 1: {
                mRecentPostsFragment.setListViewOffSet(offSet);
                break;
            }
            default:
                break;
        }
        showShowAnimation();
    }

    /**
     * 让列表停止滚动
     */
    private void stopListViewScroll() {
        int index = mViewPager.getCurrentItem();
        switch (index) {
            case 0: {
                mTopPostsFragment.stopListViewScroll();
                break;
            }

            case 1: {
                mRecentPostsFragment.stopListViewScroll();
                break;
            }
            default:
                break;
        }
    }

    /**
     * 滑动时处理其他fragment中listview的偏移量
     *
     * @param state
     */
    @SuppressLint("NewApi")
    private void setOtherListViewOffsetScrol(int state) {
        if (mV_Tabs.getY() > mTabsOriYCoord) {
            return;
        }

        if (1 == state) {
            int index = mViewPager.getCurrentItem();
            float offSet = Math.abs(mV_Tabs.getY() - mTabsOriYCoord);
            switch (index) {
                case 0: {
                    mRecentPostsFragment.setListViewOffSet(offSet);
                    break;
                }

                case 1: {
                    mTopPostsFragment.setListViewOffSet(offSet);
                    break;
                }
                default:
                    break;
            }
            showShowAnimation();
        }

    }

    /**
     * 重置列表位置
     */
    private void resetListViewPosition() {

        if (null != mTopPostsFragment) {
            mTopPostsFragment.resetListViewPosition();
        }

        if (null != mRecentPostsFragment) {
            mRecentPostsFragment.resetListViewPosition();
        }
    }

    /**
     * 刷新成功的提示
     */
    public void showRefreshSuccess(int index) {

        if (mViewPager.getCurrentItem() == index) {
            mV_TopRefresh.getBackground().setAlpha(100);
            mV_TopRefresh.setVisibility(View.VISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mV_TopRefresh.setVisibility(View.GONE);
                    PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_REFRESH);
                }
            }, 1000);
        }
    }

    /**
     * 控制发送布局是显示
     *
     * @param isShow
     */
    public void showHidePublicView(boolean isShow) {

        if (!mUnInit) {
            return;
        }
        if (isShow) {
            mBtn_Sendbroad.setVisibility(View.VISIBLE);
            mIv_Share.clearAnimation();
            mIv_Share.setBackgroundResource(R.drawable.group_share_btn);
            mIv_Share.setClickable(true);
            mIv_Share.setVisibility(View.VISIBLE);
        } else {
            //mV_Publish.setVisibility(View.GONE);
            mBtn_Sendbroad.setVisibility(View.GONE);
            mIv_Share.setVisibility(View.GONE);
        }
        mUnInit = false;
    }

    /**
     * 显示动画
     */
    public void showAnimation() {
        if (mFabOriYCoord < (CommonUtils.getRealtimeWindowHeight(this) / 2)) {
            mFabOriYCoord = mV_Sendbroad.getY();
            return;
        }
        if (!mBool_ScrollState) {
            if (mV_Sendbroad.getY() - mFabOriYCoord > 30) {
                ObjectAnimator objectAnimator = AnimationController.createTranslationYAnimation(DefaultValueConstant.DURATION_ANIMATION_300, mV_Sendbroad, mV_Sendbroad.getY(), mScreenHeight);
                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        PalmchatLogUtils.i(TAG, "---objectAnimator---onAnimationEnd----");
                        mHandler.postDelayed(mRunnable, DefaultValueConstant.DURATION_STATIC_2S);
                    }
                });
                objectAnimator.start();
            } else {
                showShowAnimation();
            }
        } else {
            if (mScreenHeight - mV_Sendbroad.getY() > 5) {
                showShowAnimation();
            } else {
                mHandler.postDelayed(mRunnable, DefaultValueConstant.DURATION_STATIC_2S);
            }
        }
    }

    /**
     * 弹出动画
     */
    private void showShowAnimation() {
        if (mFabOriYCoord < (CommonUtils.getRealtimeWindowHeight(this) / 2)) {
            mFabOriYCoord = mV_Sendbroad.getY();
        } else {
            AnimationController.createTranslationYAnimation(DefaultValueConstant.DURATION_ANIMATION_300, mV_Sendbroad, mV_Sendbroad.getY(), mFabOriYCoord).start();
        }
    }
}

