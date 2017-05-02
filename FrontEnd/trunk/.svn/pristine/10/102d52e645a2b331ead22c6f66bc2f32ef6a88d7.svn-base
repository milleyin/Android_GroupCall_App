package com.afmobi.palmchat.ui.customview;
import com.afmobi.palmchat.log.PalmchatLogUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 重写ImageView，避免引用已回收的bitmap异常
 * 
 * @author gtf
 * 
 */
public class MyImageView extends ImageView {
	
	public MyImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	
	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	private IRecycleCallBack irRecycleCallBack ;
	
	//先去掉该方法，因为api最低版本要求16
//	public void setBackground(Drawable background, IRecycleCallBack irRecycleCallBack) {
//		super.setBackground(background);
//		if (irRecycleCallBack != null) {
//			irRecycleCallBack.onResetShowPicture();
//		}
//	}
	public void setBackgroundResource(int resid, IRecycleCallBack irRecycleCallBack) {
		super.setBackgroundResource(resid);
		if (irRecycleCallBack != null) {
			irRecycleCallBack.onResetShowPicture();
		}
	}
	//api16以上才有
//	public void setBackgroundResource(Drawable background, IRecycleCallBack irRecycleCallBack) {
//		super.setBackground(background);
//		this.irRecycleCallBack = irRecycleCallBack;
//	}
	
	public void setBackgroundDrawable(Drawable background, IRecycleCallBack irRecycleCallBack) {
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
			super.setBackgroundDrawable(background);
		}else {
			super.setBackground(background);
		}
		this.irRecycleCallBack = irRecycleCallBack;
	}
	
	public void setImageBitmap(Bitmap bm, IRecycleCallBack irRecycleCallBack) {
		super.setImageBitmap(bm);
		this.irRecycleCallBack = irRecycleCallBack;
	}
	
	public void setImageDrawable(Drawable drawable, IRecycleCallBack irRecycleCallBack) {
		super.setImageDrawable(drawable);
		this.irRecycleCallBack = irRecycleCallBack;
	}
	
	public void setImageResource(int resId, IRecycleCallBack irRecycleCallBack) {
		super.setImageResource(resId);
		this.irRecycleCallBack = irRecycleCallBack;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		try {
			super.onDraw(canvas);
		} catch (Exception e) {
			if (irRecycleCallBack != null) {
				PalmchatLogUtils.e("MyImageView", "MyImageView  -> onDraw() Canvas: trying to use a recycled bitmap --onResetShowPicture()");
				irRecycleCallBack.onResetShowPicture();
			}
		}
	}
	
	public interface IRecycleCallBack{
		void onResetShowPicture();
	}

}
