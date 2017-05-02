package com.afmobi.palmchat.ui.customview;

import android.view.View;
import android.widget.PopupWindow;

public class DictTagPopupWindow extends PopupWindow {
	/***/
	private boolean mPopWapcontent;
	
    public DictTagPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height, false);
    }
	
	
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		mPopWapcontent = false;
	}
	
	/**
	 * 设置状态
	 * @param isPop
	 */
	public void setPopWapcontent(boolean isPop){
		mPopWapcontent = isPop;		
	}
	
	public boolean isPopWapcontent(){
		return mPopWapcontent;
	}

}
