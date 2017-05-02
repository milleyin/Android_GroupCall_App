package com.afmobi.palmchat.ui.activity.register;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.db.DBState;
import com.afmobi.palmchat.ui.customview.list.CircleAdapter;
import com.afmobi.palmchat.ui.customview.list.LinearLayoutListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RegionOneActivity extends BaseActivity {
    private LinearLayoutListView listView1;
    private List<Map<String, Object>> data1;
    private CircleAdapter mCircleAdapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void getData() {
        // TODO Auto-generated method stub
        initData();
    }


    private void getCountryList() {
        getData();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == DefaultValueConstant.RESULT_10) {
                Intent intent = new Intent();
                intent.putExtra(JsonConstant.KEY_CITY, data.getStringExtra(JsonConstant.KEY_CITY));
                intent.putExtra(JsonConstant.KEY_STATE, data.getStringExtra(JsonConstant.KEY_STATE));
                intent.putExtra(JsonConstant.KEY_COUNTRY, data.getStringExtra(JsonConstant.KEY_COUNTRY));
                intent.putExtra(JsonConstant.KEY_REGION_LIST, data.getSerializableExtra(JsonConstant.KEY_REGION_LIST));
                setResult(DefaultValueConstant.RESULT_10, intent);
                MyActivityManager.getScreenManager().popActivity();
            }
        }
    }


    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    boolean flag;

    @Override
    public void findViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_region);
        findViewById(R.id.back_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.title_text)).setText(R.string.sign1);
        listView1 = (LinearLayoutListView) findViewById(R.id.list1);
        String from = getIntent().getStringExtra(JsonConstant.KEY_FROM);
        flag = getIntent().getBooleanExtra(JsonConstant.KEY_FLAG, false);
        getCountryList();


        ImageView back_button = (ImageView) findViewById(R.id.back_button);
        if (back_button != null) {
            back_button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    finish();
                }
            });
        }
    }


    @Override
    public void init() {

    }


    public void initData() {
         try {
            JSONArray strCountryList = DBState.readAssertCountryList();
            int _count = strCountryList.length();
            data1 = new ArrayList<Map<String, Object>>();
            JSONObject _js = null;
            for (int i = 0; i < _count; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                _js = (JSONObject) strCountryList.get(i);
                map.put("text", _js.get("country"));
                map.put("arrow", R.drawable.arrow);
                data1.add(map);
            }

            mCircleAdapter1 = new CircleAdapter(RegionOneActivity.this, data1, R.layout.setting_default_items, new String[]{"text", "arrow"}, new int[]{R.id.setting_title, R.id.setting_arrow});

            if (flag) {
                mCircleAdapter1.setPosition(0);
            }

            mCircleAdapter1.setFristBackground(R.drawable.uilist);
            mCircleAdapter1.setLastBackgroud(R.drawable.uilist);
            mCircleAdapter1.setCenterBackgroud(R.drawable.uilist);
            mCircleAdapter1.setBackgroud(R.drawable.uilist);

//							handler.sendMessageDelayed(handler.obtainMessage(), 1500);


            listView1.setAdapter(mCircleAdapter1);
            mCircleAdapter1.setItemClickListener(new CircleAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(Map<String, Object> item, View view, int position) {
                    Intent intent = new Intent(RegionOneActivity.this, RegionTwoActivity.class);
                    intent.putExtra(JsonConstant.KEY_COUNTRY, item.get("text").toString());
                    intent.putExtra(JsonConstant.KEY_FLAG, flag);
                    startActivityForResult(intent, 10);
                }
            });
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


}
