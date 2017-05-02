package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.afmobi.palmchat.log.PalmchatLogUtils;

public class KeyboardListenRelativeLayoutEditText extends RelativeLayout {  
    
    private static final String TAG = KeyboardListenRelativeLayoutEditText.class.getSimpleName();  
      
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
      
    public KeyboardListenRelativeLayoutEditText(Context context) {  
        super(context);  
    }  
    public KeyboardListenRelativeLayoutEditText(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
      
    public KeyboardListenRelativeLayoutEditText(Context context, AttributeSet attrs, int defStyle) {  
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
                onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_INIT, mHeight);  
            }  
        } else {  
            mHeight = mHeight < b ? b : mHeight;  
        }  
          
        if(mHasInit && mHeight > b) {  
            mHasKeyboard = true;  
            if(onKeyboardStateChangedListener != null) {  
                onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_SHOW, b);  
            }  
        }  
        if(mHasInit && mHasKeyboard && mHeight == b) {  
            mHasKeyboard = false;  
            if(onKeyboardStateChangedListener != null) {  
                onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_HIDE, b);  
            }  
        }  
    }  
      
    public interface IOnKeyboardStateChangedListener {  
        public void onKeyboardStateChanged(int state,int mHeight);  
    } 
    
}  
