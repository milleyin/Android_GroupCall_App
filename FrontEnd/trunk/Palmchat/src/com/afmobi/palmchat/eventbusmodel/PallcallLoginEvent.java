package com.afmobi.palmchat.eventbusmodel;

/**
 * Created by Transsion on 2016/7/7.
 */
public class PallcallLoginEvent {
    private boolean isLoginState;

    public boolean getLoginState(){
        return  this.isLoginState;
    }

    public void setLoginState(boolean loginState) {
        this.isLoginState = loginState;
    }

}
