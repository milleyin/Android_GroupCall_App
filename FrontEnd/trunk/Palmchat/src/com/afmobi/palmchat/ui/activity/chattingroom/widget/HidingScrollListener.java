package com.afmobi.palmchat.ui.activity.chattingroom.widget;

/*
* This class is a ScrollListener for RecyclerView that allows to show/hide
* views when list is scrolled.
* */
public abstract class HidingScrollListener  {
    public abstract void onMoved(float distance);
    public abstract void onShow();
    public abstract void onHide();
}
