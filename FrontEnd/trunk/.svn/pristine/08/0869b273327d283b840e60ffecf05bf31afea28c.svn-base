package com.afmobi.palmchat.ui.customview;

import android.text.TextUtils;
import android.widget.EditText;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.core.cache.CacheManager;

public class EditListener{
	
	public CharSequence edittext_string;
	public int edittext_length;
	public boolean isDelete;
	
	private static EditListener editListener;
	private static EditText mMessageEdit;
	
	public static EditListener getInstance(EditText editText){
		if (editListener == null) {
			editListener = new EditListener();
		}
		mMessageEdit = editText;
		return editListener;
	}
	
	
	public void bindTextChanged(CharSequence s){
		PalmchatLogUtils.e("onTextChanged", s.toString());
//		if(!CacheManager.getInstance().getEditTextDelete()){
			setEdittextListener(s);
			CacheManager.getInstance().setEditTextDelete(false);
//		}else{
//			CacheManager.getInstance().setEditTextDelete(false);
//		}
	}
	
	public void bind_beforeTextChanged(CharSequence s) {
		if(s != null){
			PalmchatLogUtils.e("beforeTextChanged", s.toString());
			String string = s.toString();
			edittext_string = string;
			edittext_length = s.length();
		}else{
			edittext_length = 0;
		}
	}
	
	private void setEdittextListener(CharSequence s) {
		// TODO Auto-generated method stub
		int length = s.length();
		if(edittext_length > length){
			if (mMessageEdit != null && !TextUtils.isEmpty(edittext_string)&& !TextUtils.isEmpty(s)) {
				isDeleteSymbol();
			}
		}else if (edittext_length < length) {
			
		}
		
		
	}
	
	private boolean isDeleteSymbol(){
		if(editListener.edittext_string != null && editListener.edittext_string.length() >= 2){
			String str = edittext_string.toString();
			String tempStr = str.substring(str.length()-1);
			String hintText = mMessageEdit.getHint().toString();
			if("]".equals(tempStr)){
				String ttt = str.substring(str.lastIndexOf("["), str.lastIndexOf("]")+1);
				if(!TextUtils.isEmpty(ttt) && ttt.length() > 2){
					boolean isDelete =  EmojiParser.getInstance(PalmchatApp.getApplication()).isDefaultEmotion(mMessageEdit,ttt);
					return isDelete;
				}
			}else if (":".equals(tempStr)) {
				String ttt = str.substring(str.lastIndexOf("@"), str.lastIndexOf(":")+1);
				if(!TextUtils.isEmpty(ttt) && ttt.length() > 2 && ttt.equals(hintText)){ //del @的字符串
					mMessageEdit.setText("");
    				mMessageEdit.setHint(R.string.hint_commet);
//					boolean isDelete =  EmojiParser.getInstance(PalmchatApp.getApplication()).isDefaultEmotion(mMessageEdit,ttt);
//					return isDelete;
    				return true;
				}
			}
		}
		return false;
	}
	
}