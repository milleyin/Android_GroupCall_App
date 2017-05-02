package com.afmobi.palmchat.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.palmcall.PalmCallRecentsActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by hj on 2016/9/2.
 */

public class HomeBroadcast {
    private HomeReceiver homeReceiver;
    private Context mContext;
    private HomeStateListener mHomeStateListener;
    public HomeBroadcast(Context context, HomeStateListener homeStateListener){
        mContext = context;
        mHomeStateListener = homeStateListener;
        registerBroadcastReceiver();
    }
    private void registerBroadcastReceiver() {
        homeReceiver = new HomeReceiver();
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mContext.registerReceiver(homeReceiver, filter2);
    }

    public void unRegisterBroadcastReceiver() {
        if(homeReceiver != null) {
            try {
                mContext.unregisterReceiver(homeReceiver);
            }
            catch(Exception e) {
            }
        }
    }

    class HomeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){//HJ 监听Home键盘，为了显示PalmCall拨打电话界面
                String reason = intent.getStringExtra(Constants.SYSTEM_DIALOG_REASON_KEY);
                if (Constants.SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                    if(mHomeStateListener != null){
                        mHomeStateListener.OnPressHome();
                    }
                }
            }
        }
    }

    /**
     * 供外部调用接口
     */
    public interface HomeStateListener {
        void OnPressHome();
    }
}
