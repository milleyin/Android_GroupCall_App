package com.afmobi.palmchat.ui.activity.palmcall;

import android.view.View;

/**
 * Created by Transsion on 2016/7/1.
 */
public interface CallSomeOneStateListener {
        void progressing();
        void progressend(boolean isFinish);
        void noLeftTime();
}
