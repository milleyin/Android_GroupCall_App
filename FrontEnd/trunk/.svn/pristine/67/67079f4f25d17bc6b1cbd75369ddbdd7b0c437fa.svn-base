package com.afmobi.palmchat.ui.customview;

import com.afmobi.palmchat.util.EmojiParser;
import com.core.cache.CacheManager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class CutstomEditText extends EditText {  
    
   Context mContext;
    int mSelectLength;
    int mIndex;
    int mBeforePasteLength;
    int mPasteLength;
    String mBeforeTextContent;
    private int mMaxLength;
    public CutstomEditText(Context context,AttributeSet paramAttributeSet) {
        super(context,paramAttributeSet);
        mContext = context;
        // TODO Auto-generated constructor stub  
    }
    public void setMaxLength(int maxLength)
    {
        mMaxLength = maxLength;
    }
      @Override
    public void setImeOptions(int imeOptions) {
    	// TODO Auto-generated method stub
    	super.setImeOptions(imeOptions);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	if(keyCode == KeyEvent.KEYCODE_DEL){
    		CacheManager.getInstance().setEditTextDelete(true);
    	}
//    	if (keyCode==KeyEvent.KEYCODE_ENTER) 
//    	{
//    		// Just ignore the [Enter] key
//    		return true;
//      }
         // Handle all other keys in the default way
    	return super.onKeyDown(keyCode, event);
    }
  
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	if(keyCode == KeyEvent.KEYCODE_DEL){
    		CacheManager.getInstance().setEditTextDelete(false);
    	}
    	return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        mIndex = getSelectionStart();
        mBeforeTextContent = getText().toString();
        mBeforePasteLength = mBeforeTextContent.length();
        boolean ispa = super.onTextContextMenuItem(id);
        if(id == android.R.id.paste){//粘贴
            ClipboardManager clipboard =
                    (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = clipboard.getPrimaryClip();
            if(clip != null && clip.getItemCount() > 0){
                mPasteLength = mBeforePasteLength + clip.getItemAt(0).getText().length();
                mSelectLength = mIndex + clip.getItemAt(0).getText().length();

                if(mPasteLength > mMaxLength){
                    if(mBeforeTextContent.length() > 0 && EmojiParser.getInstance(mContext).hasEmotions(getText().toString())) {//为了解决表情显示不全的问题
                        getText().clear();
                        getText().append(mBeforeTextContent);
                        setText(EmojiParser.getInstance(mContext).parse(getText().toString()));
                        setSelection(mIndex);
                    }
                }
                else if(getText().toString().length() >= mSelectLength) {
                    setText(EmojiParser.getInstance(mContext).parse(getText().toString()));
                    setSelection(mSelectLength);
                }
                else{
                    setText(EmojiParser.getInstance(mContext).parse(getText().toString()));
                    setSelection(getText().toString().length());
                }
            }
        }
        return ispa;
    }
}  