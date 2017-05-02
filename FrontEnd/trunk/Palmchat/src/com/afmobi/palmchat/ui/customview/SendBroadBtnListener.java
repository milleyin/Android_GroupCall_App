package com.afmobi.palmchat.ui.customview;

/**
 * Created by Transsion on 2016/5/16.
 */
public interface SendBroadBtnListener  {
    /**
     * 发送语音
     */
    void sendVoice();

    /**
     * 发送图片
     */
    void sendPhoto();

    /**
     * view 隐藏
     */
    void dismiss();
}
