package com.afmobi.palmchat.ui.activity.social;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobigroup.gphone.R;

/**
 * Created by hj on 2016/3/24.
 */
public class HotTodayActivity extends BaseFragmentActivity implements View.OnClickListener {
    private TextView top_title;
    private View mV_TopRefresh;

    private Handler mHandler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_today);
        top_title = (TextView)findViewById(R.id.title_text);
        top_title.setText(R.string.hot_today);
        findViewById(R.id.back_button).setOnClickListener(this);
        HottodayFragment hottodayFragment = new HottodayFragment();
        replaceFragment(R.id.hotdayContain,hottodayFragment);
        mV_TopRefresh = findViewById(R.id.broadcast_top_refresh);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                finish();
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, intent);
        //分享Tag返回
        switch(arg0) {
            case IntentConstant.SHARE_BROADCAST:
            case IntentConstant.REQUEST_CODE_TAGPAGE:{
                if(null!=intent){
                    boolean isSuccess = intent.getBooleanExtra(JsonConstant.KEY_SEND_IS_SEND_SUCCESS, false);
                    String tempTipContent = intent.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL);
                    String tipContent;
                    if (isSuccess) {
                        tipContent = getResources().getString(R.string.share_friend_success);
                    }
                    else {
                        if(!TextUtils.isEmpty(tempTipContent)){
                            tipContent = tempTipContent;
                        }
                        else {
                            tipContent = getResources().getString(R.string.share_friend_failed);
                        }
                    }
                    ToastManager.getInstance().showShareBroadcast(this, DefaultValueConstant.SHARETOASTTIME, isSuccess,tipContent);
                }

                break;
            }
        }
    }

    /**
     * 刷新成功的提示
     */
    public void showRefreshSuccess() {
            mV_TopRefresh.getBackground().setAlpha(100);
            mV_TopRefresh.setVisibility(View.VISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mV_TopRefresh.setVisibility(View.GONE);
                    PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_REFRESH );
                }
            }, 1000);
        }

    @Override
    protected void onPause() {
        super.onPause();
        VoiceManager.getInstance().completion();
    }
}