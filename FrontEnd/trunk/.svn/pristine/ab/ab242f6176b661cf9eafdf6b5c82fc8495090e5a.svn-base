package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;

import com.afmobi.palmchat.log.PalmchatLogUtils;

public class MyTextView extends TextView{

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyTextView(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if(e.getAction() == MotionEvent.ACTION_DOWN){
			setMovementMethod(LinkMovementClickMethod.getInstance());
		}
		return super.onTouchEvent(e);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
		if (!TextUtils.isEmpty(text)) {
			if (text.length() < 2) {
				setGravity(Gravity.CENTER);
			}else {
				setGravity(Gravity.LEFT|Gravity.CENTER|Gravity.CENTER_VERTICAL);
			}
		}
			
	}
}

