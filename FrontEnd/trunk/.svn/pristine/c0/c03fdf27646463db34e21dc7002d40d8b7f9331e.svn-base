package com.afmobi.palmchat.ui.customview;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.SecurityAnswerEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.core.cache.CacheManager;

import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import de.greenrobot.event.EventBus;

public class LimitTextWatcher implements TextWatcher {
	private final static int WHAT = -1;
	private EditText mEditText;
	private int limit;
	private Handler handler;
	private int what = WHAT;
	private TextView textview;
	private EditListener editListener;
	public LimitTextWatcher(EditText mEditText , int limit) {
		super();
		this.mEditText = mEditText;
		this.limit = limit;
		if(editListener == null){
			editListener = new EditListener();
		}
	}

	public LimitTextWatcher(EditText mEditText , int limit,Handler handler,int what) {
		super();
		this.mEditText = mEditText;
		this.limit = limit;
		this.handler = handler;
		this.what = what;
		if(editListener == null){
			editListener = new EditListener();
		}
	}
	
//	public LimitTextWatcher(EditText mEditText , int limit,TextView textview,int what) {
//		super();
//		this.mEditText = mEditText;
//		this.limit = limit;
//		this.textview = textview;
//		this.what = what;
//	}
	
	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		if(s != null){
			String string = s.toString();
			editListener.edittext_string = string;
			editListener.edittext_length = s.length();
		}else{
			editListener.edittext_length = 0;
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(s.length() > 0){
			EventBus.getDefault().post(new SecurityAnswerEvent(true));
		}else{
			EventBus.getDefault().post(new SecurityAnswerEvent(false));
		}
		if(!CacheManager.getInstance().getEditTextDelete()){
			setEdittextListener(s);
			CacheManager.getInstance().setEditTextDelete(false);
		}else{
			CacheManager.getInstance().setEditTextDelete(false);
		}
		Editable editable = mEditText.getText();  
        int len = editable.length();  
        //大于最大长度  
        if(len > limit){  
            int selEndIndex = Selection.getSelectionEnd(editable);  
            String str = editable.toString();  
            //截取新字符串  
            String newStr = str.substring(0, limit);  
            mEditText.setText(newStr);  
            editable = mEditText.getText();  
            //新字符串长度  
            int newLen = editable.length();  
            //旧光标位置超过字符串长度  
            if(selEndIndex > newLen){  
                selEndIndex = editable.length();  
            }  
            //设置新的光标所在位置  
            Selection.setSelection(editable, selEndIndex);  
        }  
        if(WHAT != what){
        	handler.obtainMessage(what, len).sendToTarget();
        }else if(textview != null){
        	
        }
	}
	
	private void setEdittextListener(CharSequence s) {
		// TODO Auto-generated method stub
		int length = s.length();
		if(editListener.edittext_length > length){
			isDeleteSymbol();
		}else if (editListener.edittext_length < length) {
			
		}
		
		
	}
	
	private boolean isDeleteSymbol(){
		if(editListener.edittext_string != null && editListener.edittext_string.length() >= 2){
			String str = editListener.edittext_string.toString();
			String tempStr = str.substring(str.length()-1);
			if("]".equals(tempStr)){
				int startIndex=str.lastIndexOf("[");
				int endIndex=str.lastIndexOf("]")+1;
				if(startIndex>=0&&endIndex<=str.length()){
					String ttt = str.substring(startIndex, endIndex);
					if(!TextUtils.isEmpty(ttt) && ttt.length() > 2){
						boolean isDelete =  EmojiParser.getInstance(PalmchatApp.getApplication()).isDefaultEmotion(mEditText,ttt);
						return isDelete;
					}
				}
			}
		}
		
		return false;
	}

	static class EditListener{
		public CharSequence edittext_string;
		public int edittext_length;
		public boolean isDelete;
	}
	
	
}

