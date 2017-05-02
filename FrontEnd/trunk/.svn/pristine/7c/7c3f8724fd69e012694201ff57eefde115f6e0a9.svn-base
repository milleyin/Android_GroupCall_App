package com.afmobi.palmchat.ui.activity.social;

import java.util.ArrayList;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.eventbusmodel.BroadcastFollowsNotificationEvent;
import com.afmobi.palmchat.eventbusmodel.SwitchFragmentEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.MenuAdapter;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.customview.UnderlinePageIndicator;
import com.afmobi.palmchat.ui.customview.videoview.VedioManager;
import com.afmobi.palmchat.util.AnimationController;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobigroup.gphone.R;
import com.tencent.bugly.crashreport.CrashReport;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;

import de.greenrobot.event.EventBus;

/**
 * 此Fragment用于标签3个广播也页
 *
 * @author xiaolong
 */
public class BroadcastTab extends BaseFragment {
    private final String TAG = BroadcastTab.class.getSimpleName();
    private View mV_Tabs;
    /**
     * tabs原始坐标
     */
    private float mTabsYCoord;
    private float mTabYDiffTitleY = -1;
    private View mV_TopRefresh;
    /**
     * 底部发送广播按钮
     */
    private ImageButton mBtn_Sendbroad;
    private Handler mHandler = new Handler();
    /**
     * 悬浮按钮原始坐标
     */
    private float mFabOriYCoord = -1;
    /**
     * 发送按钮布局
     */
    private View mV_Sendbroad;
    /**
     * 发送状态
     */
    private boolean mBool_ScrollState;
    /**
     * 屏幕高度
     */
    private float mScreenHeight = -1;


    private Runnable mRunnable = new Runnable() {
        public void run() {
            mHandler.removeCallbacks(mRunnable);
            showShowAnimation();
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        findViews();
        init();

        return mMainView;
    }

    private ViewPager viewpager;
    private ImageView friendsNew_ImageView;//朋友圈红点

    private void findViews() {
        setContentView(R.layout.broadcast_tab);
        mV_Sendbroad = findViewById(R.id.tab_fab_id);
        mBtn_Sendbroad = (ImageButton) findViewById(R.id.broadcast_fab_id);
        mBtn_Sendbroad.setOnClickListener(this);
        layout_newaround = findViewById(R.id.tab_newaround);
        layout_friends = findViewById(R.id.tab_friends);
        layout_trending = findViewById(R.id.tab_trending);
        mV_Tabs = findViewById(R.id.broadcast_tabs);
        mV_Tabs.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTabsYCoord = mV_Tabs.getY();
                mV_Tabs.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        mV_TopRefresh = findViewById(R.id.broadcast_top_refresh);
        layout_newaround.setOnClickListener(new MyOnClickListener(CONTENT_FRAGMENT_NEWAROUND));
        layout_friends.setOnClickListener(new MyOnClickListener(CONTENT_FRAGMENT_FRIENDS));
        layout_trending.setOnClickListener(new MyOnClickListener(CONTENT_FRAGMENT_HOTTODAY));


        viewpager = (ViewPager) findViewById(R.id.viewpager);

        friendsNew_ImageView = (ImageView) findViewById(R.id.friends_imageView);

    }

    public static final int CONTENT_FRAGMENT_NEWAROUND = 0;
    public static final int CONTENT_FRAGMENT_FRIENDS = 2;
    public static final int CONTENT_FRAGMENT_HOTTODAY = 1;
    public ArrayList<Fragment> listFragments;
    private BroadcastFragment newAroundFragment; //
    private FriendCircleFragment friendsFragment;
    //private HottodayFragment hottodayFragment;
    private TrendingFragment trendingFragment;

    private void init() {
        initSrollData();
        listFragments = new ArrayList<Fragment>();
        newAroundFragment = new BroadcastFragment();
        friendsFragment = new FriendCircleFragment();
        trendingFragment = new TrendingFragment();//new HottodayFragment();
        listFragments.add(newAroundFragment);
        listFragments.add(trendingFragment);
        listFragments.add(friendsFragment);


        MenuAdapter adapter = new MenuAdapter(getChildFragmentManager(), listFragments);
        viewpager.setAdapter(adapter);

        viewpager.setOffscreenPageLimit(2);


        UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);
        indicator.setFades(false);
        indicator.setSelectedColor(getResources().getColor(R.color.log_blue));
        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                showSelectedOption(position);
//				currIndex = position;
                //如果有语音正在播放则停止
                if (VoiceManager.getInstance().isPlaying()) {
                    VoiceManager.getInstance().pause();
                }
                //如果有视频正在播放则停止
                if (VedioManager.getInstance().getVideoController() != null)
                    VedioManager.getInstance().getVideoController().stop();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                setOtherListViewOffSetScrol(arg0);
            }
        });
        EventBus.getDefault().register(this);
    }

    /**
     * 初始化滑动的数据
     */
    private void initSrollData(){
        mTabsYCoord = 0;
        mTabYDiffTitleY = 0;
        mTabsYCoord = 0;
        mScreenHeight = ImageUtil.DISPLAYH;
        mBool_ScrollState = false;
    }

    private View layout_newaround;
    private View layout_friends;
    /**
     * 过去为hot today 现在为 Trending
     */
    private View layout_trending;

    /**
     * 切换到哪个tab页
     *
     * @param pos
     */
    private void showSelectedOption(int pos) {
        // TODO Auto-generated method stub
        switch (pos) {
            case CONTENT_FRAGMENT_NEWAROUND:
                layout_newaround.setSelected(true);
                layout_friends.setSelected(false);
                layout_trending.setSelected(false);
                break;
            case CONTENT_FRAGMENT_FRIENDS:
                layout_newaround.setSelected(false);
                layout_friends.setSelected(true);
                layout_trending.setSelected(false);

                int _count = SharePreferenceUtils.getInstance(PalmchatApp.getApplication()).getBradcast_FriendCircleNew();
                if (_count > 0) { //if Friend Circle new msg not read, change new msg to read ,hide the read point.
                    SharePreferenceUtils.getInstance(PalmchatApp.getApplication()).setBradcast_FriendCircleNew(0);
                    friendsNew_ImageView.setVisibility(View.GONE);
                    if (getActivity() != null) {
                        ((MainTab) getActivity()).setBrdFriendCircleUnread(false);
                    }
                    friendsFragment.showRefresh();
                }
                break;
            case CONTENT_FRAGMENT_HOTTODAY:
                layout_newaround.setSelected(false);
                layout_friends.setSelected(false);
                layout_trending.setSelected(true);
                break;
            default:
                break;
        }
        showShowAnimation();
    }

    /**
     * 点击Broadcast按钮 回到顶部
     */
    public void setListScrolltoTop() {
        if (viewpager != null) {
            if (viewpager.getCurrentItem() == CONTENT_FRAGMENT_NEWAROUND) {
                newAroundFragment.setListScrolltoTop(true);
            } else if (viewpager.getCurrentItem() == CONTENT_FRAGMENT_FRIENDS) {
                friendsFragment.setListScrolltoTop(true);
            } else if (viewpager.getCurrentItem() == CONTENT_FRAGMENT_HOTTODAY) {
                trendingFragment.setListScrolltoTop(true);
            }
        } else {
            //Throwable throwable = new Throwable(BroadcastTab.class.getCanonicalName() + "------setListScrolltoTop-------null==viewpager---getActivity=" + getActivity() + " getParentFragment=" + getParentFragment() + " getChildFragmentManager" + getChildFragmentManager());
            Throwable throwable = new Throwable(BroadcastTab.class.getCanonicalName() + "------setListScrolltoTop-------null==viewpager---getActivity=");
            CrashReport.postCatchedException(throwable);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        //super.setUserVisibleHint(isVisibleToUser);
        if (viewpager != null) {
            if (isVisibleToUser) {
                if (viewpager.getCurrentItem() == CONTENT_FRAGMENT_NEWAROUND) {
                    newAroundFragment.setUserVisibleHint(true);
                } else if (viewpager.getCurrentItem() == CONTENT_FRAGMENT_FRIENDS) {
                    friendsFragment.setUserVisibleHint(true);
                } else if (viewpager.getCurrentItem() == CONTENT_FRAGMENT_HOTTODAY) {
                    trendingFragment.setUserVisibleHint(true);
                }
                if (mFabOriYCoord >ImageUtil.DISPLAYH / 2) {//重置发广播按钮的位置
                    Animation ani= mV_Sendbroad.getAnimation();
                    if(ani!=null){
                        ani.setAnimationListener(null);
                        mV_Sendbroad.clearAnimation();
                    }
                    mScreenHeight= ImageUtil.DISPLAYH;
                    mV_Sendbroad.setY(mFabOriYCoord);
                }
            } else {//如果当前tab都不显示了 那么设置他们的子项都不显示
                if (viewpager.getCurrentItem() == CONTENT_FRAGMENT_NEWAROUND) {
                    newAroundFragment.setUserVisibleHint(false);
                } else if (viewpager.getCurrentItem() == CONTENT_FRAGMENT_FRIENDS) {
                    friendsFragment.setUserVisibleHint(false);
                } else if (viewpager.getCurrentItem() == CONTENT_FRAGMENT_HOTTODAY) {
                    trendingFragment.setUserVisibleHint(false);
                }
            }


        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.broadcast_fab_id: {
                Activity activity = getActivity();
                if ((activity != null) && (activity instanceof MainTab)) {
                    ((MainTab) activity).showMaskingView();
                }
                break;
            }
            default:
                break;
        }

    }

    @SuppressLint("NewApi")
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            if (index == viewpager.getCurrentItem()) {
                return;
            }
            stopListViewScroll();
            setOtherListViewOffSet(index);
            viewpager.setCurrentItem(index);
            showSelectedOption(index);

        }
    }

    /**
     * 发广播Comment时的内容回调
     * 这里有个奇怪的问题，FriendCicleFragment发出去的回调在这里会截取到，但是BroadcastFragment发出去的回调这里是收不到的，要在MainTab里回调然后再调到这里，
     * 这个问题后面有时间再查一下 目前先用代码判断区分是从哪里回传的
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (Constants.COMMENT_FLAG == resultCode && viewpager != null) {
            if (viewpager.getCurrentItem() == CONTENT_FRAGMENT_NEWAROUND) {
                newAroundFragment.onActivityResult(requestCode, resultCode, data);
            } else if (viewpager.getCurrentItem() == CONTENT_FRAGMENT_FRIENDS) { //&&requestCode!=Constants.GET_MSG
                friendsFragment.onActivityResult(requestCode, resultCode, data);
            } else if (viewpager.getCurrentItem() == CONTENT_FRAGMENT_HOTTODAY && requestCode != Constants.GET_MSG) {
                trendingFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (VedioManager.getInstance().getVideoController() != null)
            VedioManager.getInstance().getVideoController().stop();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);

    }

    public void onEventMainThread(BroadcastFollowsNotificationEvent event) {
        recvFriendCircleNewMsg();
    }

    /**
     * 首页发送完广播后，要求全部切换到newarround界面
     *
     * @param event
     */
    public void onEventMainThread(SwitchFragmentEvent event) {
        if ((IntentConstant.REQUEST_CODE_MAINTAB != event.getFromType()) && (CONTENT_FRAGMENT_NEWAROUND != viewpager.getCurrentItem())) {
            viewpager.setCurrentItem(CONTENT_FRAGMENT_NEWAROUND);
        }
    }

    /**
     * 处理收到朋友圈有新广播的通知
     */
    private void recvFriendCircleNewMsg() {
        if (friendsFragment != null &&
                CONTENT_FRAGMENT_FRIENDS == viewpager.getCurrentItem()) {//如果当前页就在朋友圈  收到朋友圈更新消息,那就重新获取朋友圈mid列表里的详情

            //把有新朋友圈消息的标志设0
            int _count_new = SharePreferenceUtils.getInstance(PalmchatApp.getApplication()).getBradcast_FriendCircleNew();
            if (_count_new > 0) {
                SharePreferenceUtils.getInstance(PalmchatApp.getApplication()).setBradcast_FriendCircleNew(0);
                friendsFragment.showRefresh();
                friendsNew_ImageView.setVisibility(View.GONE);
                if (getActivity() != null) {
                    ((MainTab) getActivity()).setBrdFriendCircleUnread(false);
                }
            }
        } else {
            int countFriendCircleNew = SharePreferenceUtils.getInstance(PalmchatApp.getApplication()).getBradcast_FriendCircleNew();

            if (friendsNew_ImageView != null) {
                if (countFriendCircleNew > 0) {
                    friendsNew_ImageView.setVisibility(View.VISIBLE);
                    ((MainTab) getActivity()).setBrdFriendCircleUnread(true);
                } else {
                    friendsNew_ImageView.setVisibility(View.GONE);
                    if (getActivity() != null) {
                        ((MainTab) getActivity()).setBrdFriendCircleUnread(false);
                    }
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        /*暂停视频*/
        if (VedioManager.getInstance().getVideoController() != null)
            VedioManager.getInstance().getVideoController().pause();

        VoiceManager.getInstance().completion();
    }


    /**
     * 让列表停止滚动
     */
    public void stopListViewScroll() {
        if (null == viewpager) {
            Throwable throwable = new Throwable(BroadcastTab.class.getCanonicalName() + "------stopListViewScroll-------null==viewpager---getActivity=" + getActivity() + " getParentFragment=" + getParentFragment() + " getChildFragmentManager" + getChildFragmentManager());
            CrashReport.postCatchedException(throwable);
            return;
        }
        int index = viewpager.getCurrentItem();
        switch (index) {
            case CONTENT_FRAGMENT_NEWAROUND: {
                newAroundFragment.stopListViewScroll();
                break;
            }

            case CONTENT_FRAGMENT_FRIENDS: {
                friendsFragment.stopListViewScroll();
                break;
            }

            case CONTENT_FRAGMENT_HOTTODAY: {
                trendingFragment.stopListViewScroll();
                break;
            }
            default:
                break;
        }
    }

    public int getTagHeight() {
        return mV_Tabs.getHeight();
    }

    public int getHeightTags() {
        int height = 0;
        if (null != mV_Tabs) {
            height = mV_Tabs.getHeight();
        }
        return height;
    }

    /**
     * 显示tabs
     *
     * @param titleY
     */
    @SuppressLint("NewApi")
    public void tabsShow(float titleY, float scrollH) {
        float fabYCoord = mV_Sendbroad.getY();
        //悬浮按钮
        float layoutYfab = fabYCoord - scrollH;
        if (null != mV_Tabs) {
            float layoutY = titleY + mTabYDiffTitleY;
            if (null != newAroundFragment) {
                newAroundFragment.showContents(mV_Tabs.getY(), scrollH);
            }

            mV_Tabs.setY(layoutY);
            if (mTabsYCoord - layoutY < 1) {
                resetListViewPosition();
            }
        }
        if (mFabOriYCoord > (CommonUtils.getRealtimeWindowHeight(getActivity()) / 2)) {
            //悬浮按钮
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
     * 隐藏tabs
     *
     * @param scrollH
     */
    @SuppressLint("NewApi")
    public void tabsHide(float titleY, float scrollH) {
        float fabYCoord = mV_Sendbroad.getY();
        //悬浮按钮
        float layoutYfab = fabYCoord - scrollH;

        if (null != newAroundFragment) {
            newAroundFragment.hideContents(scrollH);
        }

        if (null != mV_Tabs) {
            float tabsYCoord = mV_Tabs.getY();
            float layoutY = 0.0f;
            if (tabsYCoord > mTabsYCoord) {
                mTabsYCoord = tabsYCoord;
            }
            if (mTabYDiffTitleY <= 0) {
                mTabYDiffTitleY = tabsYCoord - ((MainTab) getActivity()).getTitleY();
            }
            layoutY = titleY + mTabYDiffTitleY;
            mV_Tabs.setY(layoutY);
        }
        if (mFabOriYCoord > (CommonUtils.getRealtimeWindowHeight(getActivity()) / 2)) {
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

    @SuppressLint("NewApi")
    public void resetTabsPosition() {
        if (null != mV_Tabs) {
            if (mV_Tabs.getY() < mTabsYCoord) {
                mV_Tabs.setY(mTabsYCoord);
            }
        }
        resetListViewPosition();
    }

    private void resetListViewPosition() {

        if (null != newAroundFragment) {
            newAroundFragment.resetListViewPosition();
        }

        if (null != trendingFragment) {
            trendingFragment.resetListViewPosition();
        }

        if (null != friendsFragment) {
            friendsFragment.resetListViewPosition();
        }
    }

    @SuppressLint("NewApi")
    public float getLayoutHeight() {
        if (mV_Tabs == null) {
            Throwable throwable = new Throwable(BroadcastTab.class.getCanonicalName() + "java.lang.NullPointerException  mV_Tabs==null com.afmobi.palmchat.ui.activity.social.BroadcastTab.getLayoutHeight   activity=" + getActivity() + " uservisiblehit" + getUserVisibleHint());
            CrashReport.postCatchedException(throwable);
            PalmchatLogUtils.e("WXL", "getLayoutHeight()mV_Tabs==null activity=" + getActivity() + " uservisiblehit" + getUserVisibleHint());
            return 0;
        }
        float tabsYCoord = mV_Tabs.getY();
        if (mTabYDiffTitleY <=0) {
            mTabYDiffTitleY = tabsYCoord - ((MainTab) getActivity()).getTitleY();
        }
        return mTabYDiffTitleY;
    }

    /**
     * 初始化悬浮按钮坐标
     *
     * @return
     */
    public void initCoord(float bottomTabCoord) {
        mFabOriYCoord = mV_Sendbroad.getY();
        mScreenHeight = bottomTabCoord;
        if (null != newAroundFragment) {
            newAroundFragment.initDiffValue(mV_Tabs.getY());
        }
    }

    public void showViewFab(boolean isShow) {
        if (null != mV_Sendbroad) {
            if (isShow) {
                mV_Sendbroad.setVisibility(View.VISIBLE);
            } else {
                mV_Sendbroad.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 设置列表偏移量
     *
     * @param index
     */
    @SuppressLint("NewApi")
    private void setOtherListViewOffSet(int index) {
        if (mV_Tabs.getY() > mTabsYCoord) {
            return;
        }
        float offSet = Math.abs(mV_Tabs.getY() - mTabsYCoord);
        switch (index) {
            case CONTENT_FRAGMENT_NEWAROUND: {
                newAroundFragment.setListViewOffSet(offSet);
                break;
            }

            case CONTENT_FRAGMENT_FRIENDS: {
                friendsFragment.setListViewOffSet(offSet);
                break;
            }

            case CONTENT_FRAGMENT_HOTTODAY: {
                trendingFragment.setListViewOffSet(offSet);
                break;
            }
        }
    }

    /**
     * 设置列表偏移量
     *
     * @param state
     */
    @SuppressLint("NewApi")
    private void setOtherListViewOffSetScrol(int state) {
        if (mV_Tabs.getY() > mTabsYCoord) {
            return;
        }
        if (1 == state) {
            int index = viewpager.getCurrentItem();
            float offSet = Math.abs(mV_Tabs.getY() - mTabsYCoord);
            switch (index) {
                case CONTENT_FRAGMENT_NEWAROUND: {
                    friendsFragment.setListViewOffSet(offSet);
                    trendingFragment.setListViewOffSet(offSet);
                    break;
                }

                case CONTENT_FRAGMENT_FRIENDS: {
                    newAroundFragment.setListViewOffSet(offSet);
                    trendingFragment.setListViewOffSet(offSet);
                    break;
                }

                case CONTENT_FRAGMENT_HOTTODAY: {
                    newAroundFragment.setListViewOffSet(offSet);
                    friendsFragment.setListViewOffSet(offSet);
                    break;
                }
            }
        }

    }

    /**
     * 刷新成功的提示
     */
    public void showRefreshSuccess(int index) {

        if (viewpager.getCurrentItem() == index) {
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
     * 显示动画
     */
    public void showAnimation() {
        if (mFabOriYCoord < (CommonUtils.getRealtimeWindowHeight(getActivity()) / 2)) {
            mFabOriYCoord = mV_Sendbroad.getY();
            return;
        }
        if (!mBool_ScrollState) {
            if (mV_Sendbroad.getY() - mFabOriYCoord > DefaultValueConstant.DISTANCE_30) {
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
            if (mScreenHeight - mV_Sendbroad.getY() > DefaultValueConstant.DISTANCE_5) {
                showShowAnimation();
            } else {
                mHandler.postDelayed(mRunnable, DefaultValueConstant.DURATION_STATIC_2S);
            }
        }
        if ((newAroundFragment != null) && (viewpager.getCurrentItem() == CONTENT_FRAGMENT_NEWAROUND)) {
            newAroundFragment.showAnimation();
        }
    }

    /**
     * 弹出动画
     */
    private void showShowAnimation() {
        if(getActivity()==null){
            return;
        }
        if (mFabOriYCoord < (CommonUtils.getRealtimeWindowHeight(getActivity()) / 2)) {
            mFabOriYCoord = mV_Sendbroad.getY();
        } else {
            AnimationController.createTranslationYAnimation(DefaultValueConstant.DURATION_ANIMATION_300, mV_Sendbroad, mV_Sendbroad.getY(), mFabOriYCoord).start();
        }
    }

    /**
     * 获取坐标
     *
     * @return
     */
    public float getVTabCoord() {
        return mV_Tabs.getY();
    }


}
