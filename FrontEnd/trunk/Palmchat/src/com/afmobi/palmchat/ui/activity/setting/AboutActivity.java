package com.afmobi.palmchat.ui.activity.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.eventbusmodel.ChangeCountryEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.eventbusmodel.ShowUpdateVersionEvent;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.payment.MyWapActivity;
import com.afmobi.palmchat.ui.customview.list.CircleAdapter;
import com.afmobi.palmchat.ui.customview.list.CircleAdapter.OnClickListener;
import com.afmobi.palmchat.ui.customview.list.LinearLayoutListView;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import de.greenrobot.event.EventBus;

/**
 * 版本信息
 */
public class AboutActivity extends BaseActivity implements android.view.View.OnClickListener {

    private List<Map<String, Object>> mData;
    private LinearLayoutListView mLlistView;
    private CircleAdapter mCircleAdapter;
    private TextView mTxt_VersionName;
    private View mBackButton;
    private boolean mExistNewVer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {

        String curVer = AppUtils.getAppVersionName(this);
        String updateVer = SharePreferenceUtils.getInstance(this).getUpdateNewVersion();
//		boolean isShelves = SharePreferenceUtils.getInstance(this).isShelves(); // 是否下架
        int value = CommonUtils.versionComparison(curVer, updateVer);
        PalmchatLogUtils.i(TAG, "---curVer---==" + curVer + "==" + updateVer + "--diff--===" + String.valueOf(value));
        if (value > 0) {
            mExistNewVer = true;
            PalmchatApp.getApplication().setHasNewVersion(true);
        } else {
            mExistNewVer = false;
            PalmchatApp.getApplication().setHasNewVersion(false);
        }

        closeOrOnRechargeUI();
//		String[] array = null;
//
//		if (CommonUtils.isAndroidGp()) {
//			if (isShelves) {
//				array = getResources().getStringArray(R.array.setting_about_items);
//			} else {
//				array = getResources().getStringArray(R.array.setting_about_items2);
//			}
//
//		} else {
//			array = getResources().getStringArray(R.array.setting_about_items);
//		}
//
//		mData = new ArrayList<Map<String, Object>>();
//		for (int i = 0; i < array.length; i++) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("text", array[i]);
//			map.put("test", array[i]);
//			mData.add(map);
//		}
//
//		mCircleAdapter = new CircleAdapter(this, mData, R.layout.setting_default_two_items, new String[] { "text", "test" }, new int[] { R.id.setting_title, R.id.setting_arrow }, app.isHasNewVersion(), SharePreferenceUtils.getInstance(context).getTouchGetScore());
//		mCircleAdapter.setExitNewVer(mExistNewVer);
//		mCircleAdapter.setFristBackground(R.drawable.uilist);
//		mCircleAdapter.setLastBackgroud(R.drawable.uilist);
//		mCircleAdapter.setCenterBackgroud(R.drawable.uilist);
//		mCircleAdapter.setBackgroud(R.drawable.uilist);
//
//		mCircleAdapter.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v, int position) {
//				doClickCircleAdapter(v, position);
//
//			}
//		});
//
//		mLlistView.setAdapter(mCircleAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        try {
            app.setContext(null);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_layout || v.getId() == R.id.back_button) {
            if (PalmchatLogUtils.DEBUG) {
                String dbpath = getApplicationContext().getFilesDir().getParent() + "/" + CacheManager.getInstance().getMyProfile().afId;
                FileUtils.copyfile(dbpath, Environment.getExternalStorageDirectory().getPath() + "/" + CacheManager.getInstance().getMyProfile().afId);
            }
            finish();
        }
    }

    @Override
    public void findViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_about);
        ((TextView) findViewById(R.id.title_text)).setText(R.string.about);

        mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(this);
        mTxt_VersionName = (TextView) findViewById(R.id.textview_version_name);
        mTxt_VersionName.setText(AppUtils.getAppVersionName(AboutActivity.this));
        findViewById(R.id.back_button).setOnClickListener(this);
        mLlistView = (LinearLayoutListView) findViewById(R.id.list1);

        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    /**
     * 处理item点击
     *
     * @param v
     * @param position
     */
    private void doClickCircleAdapter(View v, int position) {
        String tag = (String) v.getTag();
        if (TextUtils.isEmpty(tag)) {
            return;
        } else {

            if (getString(R.string.feedback).equals(tag)) {
                // 反馈
                startActivity(new Intent(AboutActivity.this, FeedbackActivity.class));
            } else if (getString(R.string.updates).equals(tag)) {
                // 检查更新
                showProgressDialog(R.string.please_wait);
                UpdateVersion.getUpdateVersion().AfHttpVersionCheck(getString(R.string.product_id), CacheManager.getInstance().getMyProfile().afId, getPackageName(), ReadyConfigXML.R_DSRC, null, context);
            } else if (getString(R.string.help).equals(tag)) {
                // 帮助
                Uri uri = Uri.parse(Consts.ABOUT_HELP);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            } else if (getString(R.string.introduce).equals(tag)) {
                // 介绍
                Uri uri = Uri.parse(Consts.FAQ);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            } else if (getString(R.string.rate_app).equals(tag)) {
                // 评价
                SharePreferenceUtils.getInstance(context).setTouchGetScore(false);

                Uri uri = Uri.parse(Consts.GET_SCORE);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);

                toNotify();
            } else if (getString(R.string.terms_policies).equals(tag)) { //免责申明
                toGenericTerms();
            }
        }
    }

    /**
     * 刷新数据
     */
    public void toNotify() {
        mCircleAdapter.setData(null);
        init();
    }

    /**
     * zhh 显示版本更新event
     *
     * @param event
     */
    public void onEventMainThread(ShowUpdateVersionEvent event) {
        if (null != event) {
            String[] array1 = getResources().getStringArray(R.array.setting_about_items);
            mData = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < array1.length; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("text", array1[i]);
                map.put("test", array1[i]);
                mData.add(map);
            }

            mCircleAdapter = new CircleAdapter(this, mData, R.layout.setting_default_two_items, new String[]{"text", "test"}, new int[]{R.id.setting_title, R.id.setting_arrow}, app.isHasNewVersion(), SharePreferenceUtils.getInstance(context).getTouchGetScore());
            mCircleAdapter.setExitNewVer(mExistNewVer);
            mCircleAdapter.setFristBackground(R.drawable.uilist);
            mCircleAdapter.setLastBackgroud(R.drawable.uilist);
            mCircleAdapter.setCenterBackgroud(R.drawable.uilist);
            mCircleAdapter.setBackgroud(R.drawable.uilist);

            mCircleAdapter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v, int position) {
                    doClickCircleAdapter(v, position);

                }
            });

            mLlistView.setAdapter(mCircleAdapter);
        }
    }

    /**
     * 国家改变的通知
     *
     * @param event
     */
    public void onEventMainThread(ChangeCountryEvent event) {
        closeOrOnRechargeUI();
    }

    /**
     * 关闭或打开充值协议入口
     */
    private void closeOrOnRechargeUI() {
        boolean isShelves = SharePreferenceUtils.getInstance(this).isShelves(); // 是否下架
        String[] array = null;

        if (CommonUtils.isAndroidGp()) {
            if (isShelves) {
                array = getResources().getStringArray(R.array.setting_about_items);
            } else {
                array = getResources().getStringArray(R.array.setting_about_items2);
            }

        } else {
            array = getResources().getStringArray(R.array.setting_about_items);
        }

        mData = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < array.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", array[i]);
            map.put("test", array[i]);
            mData.add(map);
        }

        if (countryIsNigeria()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", getResources().getText(R.string.terms_policies));
            map.put("test", getResources().getText(R.string.terms_policies));
            mData.add(map);
        }

        mCircleAdapter = new CircleAdapter(this, mData, R.layout.setting_default_two_items, new String[]{"text", "test"}, new int[]{R.id.setting_title, R.id.setting_arrow}, app.isHasNewVersion(), SharePreferenceUtils.getInstance(context).getTouchGetScore());
        mCircleAdapter.setExitNewVer(mExistNewVer);
        mCircleAdapter.setFristBackground(R.drawable.uilist);
        mCircleAdapter.setLastBackgroud(R.drawable.uilist);
        mCircleAdapter.setCenterBackgroud(R.drawable.uilist);
        mCircleAdapter.setBackgroud(R.drawable.uilist);

        mCircleAdapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v, int position) {
                doClickCircleAdapter(v, position);

            }
        });


        mLlistView.setAdapter(mCircleAdapter);
    }

    /**
     * add by zhh 判断当前是否是尼日利亚国家
     *
     * @return
     */
    private boolean countryIsNigeria() {
        AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
        String country = myProfile.country;
        if (country != null && country.equals(DefaultValueConstant.COUNTRY_NIGERIA))
            return true;
        else
            return false;
    }

    /**
     * 免责申明
     */
    private void toGenericTerms() {
        try {
            String mUrl = AppUtils.getWapUrlByModule(MyWapActivity.MODULE_GENERIC_TERMS);
            if (mUrl.contains("?")) {
                mUrl = mUrl + "&module=Generic";
            } else {
                mUrl = mUrl + "?module=Generic";
            }
            Intent it = new Intent(context, MyWapActivity.class);

            it.putExtra(IntentConstant.WAP_URL, mUrl);
            startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
