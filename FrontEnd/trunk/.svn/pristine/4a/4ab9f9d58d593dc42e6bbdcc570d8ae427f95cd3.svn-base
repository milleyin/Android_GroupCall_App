package com.afmobi.palmchat.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.eventbusmodel.BlockFriendEvent;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.MenuAdapter;
import com.afmobi.palmchat.ui.activity.friends.ContactsFriendsFragment;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.customview.UnderlinePageIndicator;

import java.util.ArrayList;

/**
 * Created by hj on 2015/12/11.
 */
public class ChatsContactsTab extends BaseFragment {
    private ViewPager viewpager;
    public ArrayList<Fragment> listFragments;
    private ChatFragment mChatFragment ; //
    private ContactsFriendsFragment mContactsFriendsFragment;
    private View layout_message;
    private View layout_friends;
    private TextView mtxtMessages,mtxtContacts;
    public static final int CONTENT_FRAGMENT_MESSAGE = 0;
    public static final int CONTENT_FRAGMENT_FRIENDS = 1;

    @Override
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
    public void refreshContactsList(BlockFriendEvent mBlockFriendEvent){
        mContactsFriendsFragment.rerefreshContactsLists(mBlockFriendEvent);
    }
    private void findViews() {
        setContentView(R.layout.fragment_message_contacts);
        layout_message = findViewById(R.id.tab_message);
        layout_friends = findViewById(R.id.tab_contact);

        mtxtMessages = (TextView)findViewById(R.id.txt_messages);
        mtxtContacts = (TextView)findViewById(R.id.txt_contacts);
        layout_message.setOnClickListener(new MyOnClickListener(CONTENT_FRAGMENT_MESSAGE ));
        layout_friends.setOnClickListener(new MyOnClickListener(CONTENT_FRAGMENT_FRIENDS));
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mContactsFriendsFragment.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        listFragments = new ArrayList<Fragment>();
        mChatFragment= new ChatFragment();
        mContactsFriendsFragment = new ContactsFriendsFragment();
        listFragments.add(mChatFragment);
        listFragments.add(mContactsFriendsFragment);


        MenuAdapter adapter  = new MenuAdapter(getChildFragmentManager() , listFragments);
        viewpager.setAdapter(adapter);

        viewpager.setOffscreenPageLimit(3);


        UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);
        indicator.setFades(false);
        indicator.setSelectedColor(getResources().getColor(R.color.log_blue));
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                showSelectedOption(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            viewpager.setCurrentItem(index);
            showSelectedOption(index);

        }
    }

    /**
     * 切换到哪个tab页
     * @param pos
     */
    private void showSelectedOption(int pos) {
        // TODO Auto-generated method stub
        switch (pos) {
            case CONTENT_FRAGMENT_MESSAGE:
                layout_message.setSelected(true);
                layout_friends.setSelected(false);
                mtxtMessages.setTextColor(getResources().getColor(R.color.chats_17a5ef));
                mtxtContacts.setTextColor(getResources().getColor(R.color.text_level_2));
                break;
            case CONTENT_FRAGMENT_FRIENDS :
                layout_message.setSelected(false);
                layout_friends.setSelected(true);
                mtxtContacts.setTextColor(getResources().getColor(R.color.chats_17a5ef));
                mtxtMessages.setTextColor(getResources().getColor(R.color.text_level_2));
                break;
        }
    }
    @Override
    public void onClick(View v) {

    }
    public void setListScrolltoTop() {
        if(mChatFragment != null) {
            mChatFragment.setListScrolltoTop();
        }
    }
}