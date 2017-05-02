package com.afmobi.palmchat.ui.customview;

import java.util.ArrayList;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.ui.activity.social.PopwindowAdapter;
import com.afmobigroup.gphone.R;
import com.core.cache.CacheManager;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class CutstomPopwindowEditText extends EditText {  
    
	/** 标记框头边距 */
	private int mEditPaddingTop;
	/** 编辑框底部边距 */
	private int mEditPaddingBottom;
	/** 编辑框高度 */
	private int mEditHeight;
	/** 文字的行高度 */
	private int mEditLineHeight;
	/****/
	private int mPopwindowMartop;
	private int mPopwindowOffset;
	private int mPopDefaultHeight;
	/**popwindow宽度*/
	private int mPopDefaultMarginLR;
	private DictTagPopupWindow mPw_TagsAssociation;
	private ListView mLv_TagsAssociation;
	private PopwindowAdapter mPopwindowAdapter;
	private Context mContext;
	private boolean mIsCalculated;
	private View mDropView;
	private OnPopItemClickListener mOnPopItemClickListener;
	
	public interface  OnPopItemClickListener {
		void onItemClick(AdapterView<?> parent, View view, int position, long id);
	}
	
	
	
    public CutstomPopwindowEditText(Context context,AttributeSet paramAttributeSet) {  
        super(context,paramAttributeSet);
        mContext = context;
		this.mDropView = this;
        init();
    }  
    
    @SuppressWarnings("deprecation")
	private void init(){
    	mPopDefaultHeight = getResources().getDimensionPixelSize(R.dimen.popwindow_tagsdict_height);
		mPopwindowMartop = getResources().getDimensionPixelSize(R.dimen.popwindow_tagsdict_margintop);
		mPopDefaultMarginLR = getResources().getDimensionPixelSize(R.dimen.popwindow_tagsdict_marginlr);
		
		mEditPaddingTop = getPaddingTop();
		//mEditPaddingBottom = getPaddingBottom();
		mEditLineHeight = getLineHeight();
		
		View contentView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_list_layout, null);
		mPw_TagsAssociation = new DictTagPopupWindow(findViewById(R.id.message_edit), PalmchatApp.getApplication().getRealtimeWindowWidth()-2*mPopDefaultMarginLR, mPopDefaultHeight);
		mPw_TagsAssociation.setContentView(contentView);
		mPw_TagsAssociation.setBackgroundDrawable(new BitmapDrawable());
		mPw_TagsAssociation.setOutsideTouchable(true);
		mLv_TagsAssociation = (ListView) contentView.findViewById(R.id.popwindowlist_id);

		mPopwindowAdapter = new PopwindowAdapter(PalmchatApp.getApplication());
		mLv_TagsAssociation.setAdapter(mPopwindowAdapter);
//		mLv_TagsAssociation.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				
//				if(mOnPopItemClickListener!=null){
//					mOnPopItemClickListener.onItemClick(parent, view, position, id);
//				}
//			}
//			
//		});
		mPopwindowAdapter.setOnPopItemClickListener(new OnPopItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(mOnPopItemClickListener!=null){
					mOnPopItemClickListener.onItemClick(parent, view, position, id);
				}
			}
			
		});
		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				//由于加入scrollview，这个获取就有问题了，直接用scrollview的高度
				//mEditHeight = getHeight();
				mEditHeight = getResources().getDimensionPixelSize(R.dimen.sendbroadcast_edittext_height);
			}
		});
    }
	
	/**
	 * 显示提示信息
	 * @param xoff
	 * @param yoff
	 * @param gravity
	 */
	public void showpInfoTip(int xoff, int yoff){
		if(mPw_TagsAssociation!=null){
			mPw_TagsAssociation.showAsDropDown(mDropView, xoff, yoff);
		}
	}
	
	/**
	 * 显示提示信息
	 * @param xoff
	 * @param gravity
	 */
	public void showInfoTip(int xoff){
		if(mPw_TagsAssociation!=null){
			int offset = 0;
			if(mIsCalculated){
				offset = mPopwindowOffset;
			} else {
				offset = getPopYoffset();
			}
			mPw_TagsAssociation.showAsDropDown(mDropView, xoff, offset);
			mIsCalculated = false;
		}
	}
	
	public void showInfoTip(){
		if(mPw_TagsAssociation!=null){
			int offset = 0;
			if(mIsCalculated){
				offset = mPopwindowOffset;
			} else {
				offset = getPopYoffset();
			}
			mPw_TagsAssociation.showAsDropDown(mDropView, mPopDefaultMarginLR, offset);
			mIsCalculated = false;
		}
	}
	
	/**
	 * 隐藏信息提示
	 */
	public void hideInfoTip(){
		if((mPw_TagsAssociation!=null)&&(mPw_TagsAssociation.isShowing())){
			mPw_TagsAssociation.dismiss();
		}
	}
	
	/**
	 * 设置pop高度
	 * @param height
	 */
	public void setPopHeight(int height){
		if(mPw_TagsAssociation!=null){
			mPw_TagsAssociation.setHeight(height);
		}
	}
	
	public void setPopDefaultHeight(){
		if(mPw_TagsAssociation!=null){
			mPw_TagsAssociation.setHeight(mPopDefaultHeight);
		}
	}
	
	public int getPopdefaultHeight(){
		return mPopDefaultHeight;
	}
	
	/**
	 * 获取便宜量量
	 * @return
	 */
	public int getPopYoffset(){
		String msg = getText().toString().replace("\n", "");
		int index = getSelectionStart();// 获取光标的位置
		int lineCount = getLineCount();

		Log.d("==", "index:" + index);

		mPopwindowOffset = -mEditHeight + mEditPaddingTop + mEditLineHeight * lineCount + mPopwindowMartop;
		int limitOffset = -mEditPaddingBottom;

		if (mPopwindowOffset >= limitOffset) {
			mPopwindowOffset = limitOffset;
		}
		mIsCalculated = true;
		return mPopwindowOffset;
	}
	
	/**
	 * 更新提示 信息
	 * @param tipInfos
	 */
	public void updateData(ArrayList<String> tipInfos){
		if(null!=mPopwindowAdapter){
			mPopwindowAdapter.updateData(tipInfos);
			mLv_TagsAssociation.setSelection(0);
		}
	}
	
	public boolean isPopShow(){
		boolean isShow = false;
		if(mPw_TagsAssociation.isShowing()){
			isShow = true;
		} else {
			isShow = false;
		}		
		return isShow;
	}
    
      @Override
    public void setImeOptions(int imeOptions) {
    	super.setImeOptions(imeOptions);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_DEL){
    		CacheManager.getInstance().setEditTextDelete(true);
    	}
    	if (keyCode==KeyEvent.KEYCODE_ENTER) 
    	{
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
  
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_DEL){
    		CacheManager.getInstance().setEditTextDelete(false);
    	}
    	return super.onKeyUp(keyCode, event);
    }
  
    public void setOnPopItemClickListener(OnPopItemClickListener listener) {
    	mOnPopItemClickListener = listener;
    }

	public void setAsDropDownView(View view){
		mDropView = view;
		mDropView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mEditHeight = mDropView.getHeight();
			}
		});
	}
  
}  