package com.afmobi.palmchat.ui.activity.payment;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobigroup.gphone.R;
import com.core.AfLottery;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class CoinRechargeDialogActivity extends Activity implements OnClickListener, AfHttpResultListener, OnCheckedChangeListener {
    private TextView tv_balance, tv_msg, tv_title;
    private Button btn_cancel, btn_confim;

    private AfPalmchat mAfCorePalmchat;
    /**
     * 声明文本
     */
    private TextView tv_terms;
    /**
     * 复选框
     */
    private CheckBox check_terms;
    /*url*/
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_coin_recharge);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_balance = (TextView) findViewById(R.id.tv_balance);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_confim = (Button) findViewById(R.id.btn_confim);
        tv_terms = (TextView) findViewById(R.id.tv_terms);
        check_terms = (CheckBox) findViewById(R.id.check_terms);

        /** 添加下划线 */
        tv_terms.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
        tv_terms.getPaint().setAntiAlias(true);// 抗锯齿
        tv_terms.getPaint().setColor(getResources().getColor(R.color.text_level_2));
        tv_terms.setText(getResources().getString(R.string.terms_policies));

        btn_cancel.setOnClickListener(this);
        btn_confim.setOnClickListener(this);
        check_terms.setOnCheckedChangeListener(this);
        tv_terms.setOnClickListener(this);

        tv_title.setText(R.string.recharge_palmcoin);
        tv_msg.setText(R.string.palmchat_isalways_free);
        if (check_terms.isChecked()) {
            btn_confim.setTextColor(getResources().getColor(R.color.log_blue));
            btn_confim.setClickable(true);
            check_terms.setBackgroundResource(R.drawable.checkboxl_sel);
            /*if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                check_terms.setBackgroundDrawable(getResources().getDrawable(R.drawable.checkboxl_sel));
			}else{
				check_terms.setBackground(getResources().getDrawable(R.drawable.checkboxl_sel));
			}*/

        } else {
            btn_confim.setTextColor(getResources().getColor(R.color.text_level_2));
            btn_confim.setClickable(false);
            check_terms.setBackgroundResource(R.drawable.checkboxl_nosel);
            /*if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
				check_terms.setBackgroundDrawable(getResources().getDrawable(R.drawable.checkboxl_nosel));
			}else{
				check_terms.setBackground(getResources().getDrawable(R.drawable.checkboxl_nosel));
			}*/
        }

        mUrl = getIntent().getStringExtra(IntentConstant.URL);
        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        getBalanceFromServer();
    }

    /**
     * 从服务器获取最新coin余额
     */
    private void getBalanceFromServer() {
        AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
        if (null != profileInfo.afId)
            mAfCorePalmchat.AfHttpPredictGetCoins(profileInfo.afId, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confim:
                SharePreferenceUtils.getInstance(CoinRechargeDialogActivity.this).setRechargeCoinClicked(true);

                if (mUrl != null && !mUrl.isEmpty()) {
                    Intent it = new Intent(this, MyWapActivity.class);
                    it.putExtra(IntentConstant.WAP_URL, mUrl);
                    startActivity(it);
                }

                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.tv_terms: // 免责声明
                String url = AppUtils.getWapUrlByModule(MyWapActivity.MODULE_PALMCOIN_TERMS);
                if (url.contains("?")) {
                    url = url + "&module=PalmCoin";
                } else {
                    url = url + "?module=PalmCoin";
                }
                Intent it = new Intent(this, MyWapActivity.class);
                it.putExtra(IntentConstant.WAP_URL, url);
                startActivity(it);
                break;

            default:
                break;
        }
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                case Consts.REQ_PREDICT_BUY_POINTS_GETCOINS: // Consts.REQ_PAYMENT_VCOIN_GET:
                    if (null != result) {
                        AfLottery resp = (AfLottery) result;
                        tv_balance.setText(resp.getconiresp.balance + "");
                    }
                    break;
                default:
                    break;
            }

        } else {

            switch (flag) {
                case Consts.REQ_PREDICT_BUY_POINTS_GETCOINS: // Consts.REQ_PAYMENT_VCOIN_GET:获取availablebalance失败
                    Consts.getInstance().showToast(this, code, flag, http_code);
                    break;

                default:
                    break;
            }

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            btn_confim.setTextColor(getResources().getColor(R.color.log_blue));
            btn_confim.setClickable(true);
            check_terms.setBackgroundResource(R.drawable.checkboxl_sel);
			/*if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
				check_terms.setBackgroundDrawable(getResources().getDrawable(R.drawable.checkboxl_sel));
			}else{
				check_terms.setBackground(getResources().getDrawable(R.drawable.checkboxl_sel));
			}*/

        } else {
            btn_confim.setTextColor(getResources().getColor(R.color.text_level_2));
            btn_confim.setClickable(false);
            check_terms.setBackgroundResource(R.drawable.checkboxl_nosel);
			/*if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
				check_terms.setBackgroundDrawable(getResources().getDrawable(R.drawable.checkboxl_nosel));
			}else{
				check_terms.setBackground(getResources().getDrawable(R.drawable.checkboxl_nosel));
			}*/
        }

    }

}
