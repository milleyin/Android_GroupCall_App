package com.afmobi.palmchat.ui.activity.friends;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.ui.activity.MenuAdapter;
import com.afmobi.palmchat.ui.customview.UnderlinePageIndicator;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.afmobi.palmchat.util.FolllowerUtil;
/**
 * 联系人的主activity界面
 */
public class ContactsActivity extends BaseFragmentActivity implements OnClickListener {
    public static final String TAG = ContactsActivity.class.getCanonicalName();
    public static final String INTENT_KEY = "CurrentItem";
    public static final int FRIENDS = 0;
    public static final int FOLLOWING = 1;
    public static final int FOLLOWRES = 2;
    public static final int TAG_CONTACT_ACTIVITY = 1;

    private FollowingFragment followingFragment;
    private FollowerFragment followerFragment;
    private ViewPager mViewPager;
    private TextView  mTv2, mTv3;
    private int currentItem;
    private View mView_Followed_Red;
    /**
     * 获取MyProfile跳转过来的值
     */


    private TextView mTvCount2, mTvCount3;
    private TextView mTvTitle;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        currentItem = getIntent().getIntExtra(INTENT_KEY, FRIENDS);
        findViews();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(TAG);
//        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(TAG);
//        MobclickAgent.onPause(this);
    }


    public void findViews() {
        // TODO Auto-generated method stub

        setContentView(R.layout.activity_contacts);
        mViewPager = (ViewPager) findViewById(R.id.vpager);

        //mTv1 = (TextView) findViewById(R.id.text1);
        mTv2 = (TextView) findViewById(R.id.text2);
        mTv3 = (TextView) findViewById(R.id.text3);


        //mTvCount1 = (TextView) findViewById(R.id.text_count1);
        mTvCount2 = (TextView) findViewById(R.id.text_count2);
        mTvCount3 = (TextView) findViewById(R.id.text_count3);
        mView_Followed_Red = findViewById(R.id.tv_followed);
        followingFragment = new FollowingFragment();
        followerFragment = new FollowerFragment();
        ArrayList<Fragment> listFragments = new ArrayList<Fragment>();
        //listFragments.add(new ContactsFriendsFragment());
        listFragments.add(followingFragment);
        listFragments.add(followerFragment);

        //mTv1.setOnClickListener(this);
        mTv2.setOnClickListener(this);
        mTv3.setOnClickListener(this);


        //mTvCount1.setOnClickListener(this);
        mTvCount2.setOnClickListener(this);
        mTvCount3.setOnClickListener(this);

        findViewById(R.id.back_button).setOnClickListener(this);


        mTvTitle = (TextView) findViewById(R.id.title_text);
        mTvTitle.setVisibility(View.VISIBLE);
        mTvTitle.setText(R.string.followingandfollower);

        final MenuAdapter adapter = new MenuAdapter(getSupportFragmentManager(), listFragments);

        mViewPager.setAdapter(adapter);

        mViewPager.setOffscreenPageLimit(1);//3);

        UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        indicator.setFades(false);
        indicator.setSelectedColor(getResources().getColor(R.color.log_blue));
        Bundle mBundle = getIntent().getExtras();
        if(null != mBundle) {
            String id = mBundle.getString("id");
            if(null != id){
                if(id.equals("0")){
                    if(FolllowerUtil.getInstance().getSizeForDefault() > 0 && SharePreferenceUtils.getInstance(getApplicationContext()).getIsFollowed(CacheManager.getInstance().getMyProfile().afId)){
                        mView_Followed_Red.setVisibility(View.VISIBLE);
                    }
                    mViewPager.setCurrentItem(0);
                    changeTab(0);
                }else if(id.equals("1")){
                    mViewPager.setCurrentItem(1);
                    changeTab(1);
                }
            }
        }

        /**
         * ViewPager滑动事件
         */
        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            /**
             * 表示那个界面被选中了
             * @param position 当点滑动到的界面
             */
            @Override
            public void onPageSelected(int position) {
                changeTab(position);

            }

            /**
             * 此方法会在屏幕滚动过程中不断被调用
             * @param arg0
             * @param arg1
             * @param arg2
             */
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

                boolean hideSlideBar;
                if (arg1 == 0f && arg2 == 0) {
                    hideSlideBar = false;
                } else {
                    hideSlideBar = true;
                }

               // Fragment fragment = adapter.getCurrentFragment();
              /*  if (fragment != null && fragment instanceof ContactsFriendsFragment) {
                    ((ContactsFriendsFragment) fragment).resetSlidBar(hideSlideBar);*/
            /*    if (fragment != null && fragment instanceof FollowingFragment) {
                    ((FollowingFragment) fragment).resetSlidBar(hideSlideBar);
                } else if (fragment != null && fragment instanceof FollowerFragment) {
                    ((FollowerFragment) fragment).resetSlidBar(hideSlideBar);
                }*/
            }

            /**
             * 手机在操作屏幕的时候发生变化
             * @param arg0
             */
            @Override
            public void onPageScrollStateChanged(int arg0) {
                //Fragment fragment = adapter.getCurrentFragment();
               /* if (fragment != null && fragment instanceof ContactsFriendsFragment) {
                    ((ContactsFriendsFragment) fragment).onPageScrollStateChanged(arg0);
                } else*/
          /*      if (fragment != null && fragment instanceof FollowingFragment) {
                    ((FollowingFragment) fragment).onPageScrollStateChanged(arg0);
                } else if (fragment != null && fragment instanceof FollowerFragment) {
                    ((FollowerFragment) fragment).onPageScrollStateChanged(arg0);
                }*/
            }
        });

//		mViewPager.setCurrentItem(currentItem);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.text2:
            case R.id.text_count2:
                mViewPager.setCurrentItem(0);
                break;

            case R.id.text3:
            case R.id.text_count3:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.back_button:
                finish();
                break;
            default:
                break;

        }

    }

    public void updateListCounts() {
        int followingSize = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).size(false, true);
        int followerSize = FolllowerUtil.getInstance().getSizeForDefault();
        if(followerSize == 0){
            mView_Followed_Red.setVisibility(View.GONE);
        }
        if (mTvCount2 != null) {
            mTvCount2.setText(String.valueOf(followingSize));
        }

        if (mTvCount3 != null) {
            mTvCount3.setText(String.valueOf(followerSize));
        }
    }

    /**
     * 滑动改变颜色值
     * @param index
     */
    private void changeTab(int index) {

        switch (index) {
            case 0:
                //mTv1.setTextColor(getResources().getColor(R.color.log_blue));
                mTv2.setTextColor(getResources().getColor(R.color.log_blue));
                mTv3.setTextColor(getResources().getColor(R.color.text_tab));

                //mTvCount1.setTextColor(getResources().getColor(R.color.text_tab));
                mTvCount2.setTextColor(getResources().getColor(R.color.log_blue));
                mTvCount3.setTextColor(getResources().getColor(R.color.text_tab));

                break;

            case 1:

                // mTv1.setTextColor(getResources().getColor(R.color.text_tab));
                mTv2.setTextColor(getResources().getColor(R.color.text_tab));
                mTv3.setTextColor(getResources().getColor(R.color.log_blue));

                //mTvCount1.setTextColor(getResources().getColor(R.color.text_tab));
                mTvCount2.setTextColor(getResources().getColor(R.color.text_tab));
                mTvCount3.setTextColor(getResources().getColor(R.color.log_blue));
                SharePreferenceUtils.getInstance(getApplicationContext()).setIsFolloed(CacheManager.getInstance().getMyProfile().afId, false);
                mView_Followed_Red.setVisibility(View.INVISIBLE);
                break;
        }

    }

    /*public void onEventMainThread(BlockFriendEvent mBlockFriendEvent){
        followerFragment.refreshfollowerfragment(mBlockFriendEvent);
        followingFragment.refreshfollowingfragment(mBlockFriendEvent);
    }*/

}
