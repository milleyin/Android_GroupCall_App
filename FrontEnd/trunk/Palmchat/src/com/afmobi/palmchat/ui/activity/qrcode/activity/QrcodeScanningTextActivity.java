package com.afmobi.palmchat.ui.activity.qrcode.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobigroup.gphone.R;

/**
 * Created by heguiming on 2015/10/22.
 */
  public class QrcodeScanningTextActivity extends BaseActivity {

    private Context mContent;

    private TextView mTextViewShowBox;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContent = this;
    }

    @Override
    public void findViews() {
        setContentView(R.layout.qrcode_scanning_text);
        mTextViewShowBox = (TextView)findViewById(R.id.textviewshowbox);
        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        mTextViewShowBox.setText(text);
    }

    @Override
    public void init() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
