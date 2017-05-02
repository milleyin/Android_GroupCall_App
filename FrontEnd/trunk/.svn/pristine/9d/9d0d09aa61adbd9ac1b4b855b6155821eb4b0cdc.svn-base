package com.afmobi.palmchat.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.afmobigroup.gphone.R;

public class BaseDialog extends Dialog {
	private Window mWindow;
	private int height;
	private int gravity;
	
	public int getGravity() {
		return gravity;
	}

	public void setGravity(int gravity) {
		this.gravity = gravity;
	}

	public BaseDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		mWindow = getWindow();
		setCanceledOnTouchOutside(true);
		setCancelable(true);
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		mWindow = getWindow();
		setCanceledOnTouchOutside(true);
		setCancelable(true);
	}

	public BaseDialog(Context context) {
		super(context , R.style.BaseCustomDialog);
		mWindow = getWindow();
		setCanceledOnTouchOutside(true);
		setCancelable(true);
	}

	@Override
	public void show() {
		WindowManager.LayoutParams lp = mWindow.getAttributes();
		if (gravity == 0) {
			lp.gravity = Gravity.BOTTOM;
		} else {
			lp.gravity = gravity;
		}
		lp.width = LayoutParams.FILL_PARENT;
		if (height != 0) {
			lp.height = height;
		}
		lp.y = 100;
		onWindowAttributesChanged(lp);
		super.show();
	}
	
	public void setHeight(int h) {
		this.height = h;
	}

}
