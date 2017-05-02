package com.afmobi.palmchat.eventbusmodel;

/**
 *  * 通知切换fragment，非newarround切换界面时
 * Created by Transsion on 2016/5/3.
 */
public class SwitchFragmentEvent {
    /** 0：表示从maintab进入发送广播
     *  2：表示从tagpage进入发送广播  根据 IntentConstant 中的定义
     *  ...待定
     *
     */
    private int fromType;
    public void setFromType(int fromType) {
        this.fromType = fromType;
    }

    public int getFromType() {
        return this.fromType;
    }

    @Override
    public String toString() {
        return "SwitchFragmentEvent{" +
                "fromType=" + fromType +
                '}';
    }
}
