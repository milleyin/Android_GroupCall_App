package com.afmobi.palmchat.eventbusmodel;

/**palmcall 通话时长事件模型
 * Created by zhh on 2016/8/17.
 */
public class UpdatePalmCallDurationEvent {
    /**剩余时间*/
    private long leftTime;

    public UpdatePalmCallDurationEvent(){
        this.leftTime = -1;
    }

    public UpdatePalmCallDurationEvent(long leftTime) {
        this.leftTime = leftTime;
    }

    public long getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(long leftTime) {
        this.leftTime = leftTime;
    }
}
