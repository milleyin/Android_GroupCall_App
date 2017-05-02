package com.afmobi.palmchat.ui.activity.payment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobigroup.gphone.R;
import com.core.AfLoginInfo;
import com.core.AfPalmchat;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/9/1.
 */
public class MobileTopUpDialogActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView tv_msg, tv_title;
    private Button btn_cancel, btn_confim;
    /*声明文本 */
    private TextView tv_terms;
    /*复选框 */
    private CheckBox check_terms;
    /*url*/
    private String mUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_mobile_top_up);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_confim = (Button) findViewById(R.id.btn_confim);
        tv_terms = (TextView) findViewById(R.id.tv_terms);
        check_terms = (CheckBox) findViewById(R.id.check_terms);

        /** 添加下划线 */
        tv_terms.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
        tv_terms.getPaint().setAntiAlias(true);// 抗锯齿
        tv_terms.getPaint().setColor(getResources().getColor(R.color.text_level_2));


        btn_cancel.setOnClickListener(this);
        btn_confim.setOnClickListener(this);
        check_terms.setOnCheckedChangeListener(this);
        tv_terms.setOnClickListener(this);

        tv_terms.setText(getResources().getString(R.string.terms_policies));
        tv_title.setText(R.string.mobile_top_up);
        tv_msg.setText(R.string.airtime_top_up_terms);

        if (check_terms.isChecked()) {
            btn_confim.setTextColor(getResources().getColor(R.color.log_blue));
            btn_confim.setClickable(true);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                check_terms.setBackgroundDrawable(getResources().getDrawable(R.drawable.checkboxl_sel));
            } else {
                check_terms.setBackground(getResources().getDrawable(R.drawable.checkboxl_sel));
            }

        } else {
            btn_confim.setTextColor(getResources().getColor(R.color.text_level_2));
            btn_confim.setClickable(false);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                check_terms.setBackgroundDrawable(getResources().getDrawable(R.drawable.checkboxl_nosel));
            } else {
                check_terms.setBackground(getResources().getDrawable(R.drawable.checkboxl_nosel));
            }
        }

        mUrl = getIntent().getStringExtra(IntentConstant.URL);

    }


    /**
     * 进入协议页面
     */
    private void toTerms() {
        String mUrl = AppUtils.getWapUrlByModule(MyWapActivity.MODULE_AIRTIME_TERMS);
        if (mUrl.contains("?")) {
            mUrl = mUrl + "&module=Airtime";
        } else {
            mUrl = mUrl + "?module=Airtime";
        }

        Intent it = new Intent(this, MyWapActivity.class);
        it.putExtra(IntentConstant.WAP_URL, mUrl);
        startActivity(it);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confim: //mobile top up
                SharePreferenceUtils.getInstance(MobileTopUpDialogActivity.this).setMobileTopUpClicked(true);

                Intent it = new Intent(this, MyWapActivity.class);
                it.putExtra(IntentConstant.WAP_URL, mUrl);
                it.putExtra(MyWapActivity.FORM, MyWapActivity.MOBILE_TOP_UP);
                startActivity(it);
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.tv_terms: // 免责声明
                toTerms();
                break;

            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            btn_confim.setTextColor(getResources().getColor(R.color.log_blue));
            btn_confim.setClickable(true);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                check_terms.setBackgroundDrawable(getResources().getDrawable(R.drawable.checkboxl_sel));
            } else {
                check_terms.setBackground(getResources().getDrawable(R.drawable.checkboxl_sel));
            }

        } else {
            btn_confim.setTextColor(getResources().getColor(R.color.text_level_2));
            btn_confim.setClickable(false);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                check_terms.setBackgroundDrawable(getResources().getDrawable(R.drawable.checkboxl_nosel));
            } else {
                check_terms.setBackground(getResources().getDrawable(R.drawable.checkboxl_nosel));
            }
        }

    }
}
