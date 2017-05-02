package com.afmobi.palmchat.ui.customview;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager.SystemBarConfig;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class KeyboardListenRelativeLayout extends RelativeLayout {  
    
    private static final String TAG = KeyboardListenRelativeLayout.class.getSimpleName();  
      
    public static final byte KEYBOARD_STATE_SHOW = -3;  
    public static final byte KEYBOARD_STATE_HIDE = -2;  
    public static final byte KEYBOARD_STATE_INIT = -1;  
      
    private boolean mHasInit = false;  
    private boolean mHasKeyboard = false;  
    public boolean hasKeyboard() {
		return mHasKeyboard;
	}

	private int mHeight;  
      
    private IOnKeyboardStateChangedListener onKeyboardStateChangedListener;  
      
    public KeyboardListenRelativeLayout(Context context) {  
        super(context);  
    }  
    public KeyboardListenRelativeLayout(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
      
    public KeyboardListenRelativeLayout(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
      
    public void setOnKeyboardStateChangedListener(IOnKeyboardStateChangedListener onKeyboardStateChangedListener) {  
        this.onKeyboardStateChangedListener = onKeyboardStateChangedListener;  
    }  
     
    @Override  
    protected void onLayout(boolean changed, int l, int t, int r, int b) {  
        super.onLayout(changed, l, t, r, b);  
        if(!mHasInit) {  
            mHasInit = true;  
            mHeight = b;  
            if(onKeyboardStateChangedListener != null) {  
                onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_INIT);  
            }  
        } else {  
            mHeight = (mHeight < b ? b : mHeight);  
        }  
       
        boolean isShowSoftKey=(mHeight -b)>mHeight/5;//modify by wxl 20151116 之前用高度相等的判断比较坑爹，有的机型拍照后回来高度获取会差75像素 也就是导航条的高度 或则会取得1920的高度 但b又是1701去掉了状态条和底部虚拟键条的高度
        if(mHasInit && isShowSoftKey){//mHeight > b) {  
            mHasKeyboard = true;  
            if(onKeyboardStateChangedListener != null) {  
                onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_SHOW);  
            }
            PalmchatLogUtils.e(TAG, "KEYBOARD_STATE_SHOW | b = "+b);
        }  
        if(mHasInit && mHasKeyboard &&!isShowSoftKey){// mHeight == b) {  
            mHasKeyboard = false;  
            if(onKeyboardStateChangedListener != null) {  
                onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_HIDE);  
            }  
            PalmchatLogUtils.e(TAG, "KEYBOARD_STATE_HIDE | b = "+b);
        }  
    }  
      
    public interface IOnKeyboardStateChangedListener {  
        public void onKeyboardStateChanged(int state);  
    }  
}  
