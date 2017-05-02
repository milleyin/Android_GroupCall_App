package com.afmobi.palmchat.ui.customview.photos;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.afmobigroup.gphone.R;


public class Photo {
	
	public final static float kLabelHeight  =  20.f;
	public final static float kPhotoSize    =  75.f;
	public final static float kMarginTB     =  10.f;
	public final static float kMarginLR     =  4.f;
	
	private Context mContext;

	
	public LinearLayout 	view;
	public ImageView 		viewPhoto;
	
	public ImageView 		deleteMask;
	public TextView 		labelName;
	
	public Point pointOrigin;
	public PPRect frame;

	private String stringImageUrl;
	
	
	boolean editModel;
	boolean isShowLabel;

	byte photType;
	
	PhotoCallback callback;
	
	public PhotoCallback getCallback() {
		return callback;
	}

	public void setCallback(PhotoCallback callback) {
		this.callback = callback;
	}

	public String getStringImageUrl() {
		return stringImageUrl;
	}

	public void setStringImageUrl(String stringImageUrl) {
		this.stringImageUrl = stringImageUrl;
	}
	
	public Photo(Context context , PPRect rect , boolean isShowLabel)
	{
		this.mContext = context;
		this.frame = rect;
		this.isShowLabel = isShowLabel;
		init();
	}
	
	private void init()
	{
		
		PPRect viewFrame = new PPRect(0, 0, frame.width, frame.height);
		if (isShowLabel) {
			viewFrame = new PPRect(0, 0, frame.width, frame.height - kLabelHeight);
			
			labelName = new TextView(mContext);
			labelName.setLayoutParams(new LayoutParams((int)this.frame.width,(int)kLabelHeight));
			labelName.setTextColor(Color.WHITE);
			
		}
		
		view = new LinearLayout(this.mContext);
		view.setLayoutParams(new LayoutParams((int)viewFrame.width ,(int)viewFrame.height));
		
		viewPhoto = new ImageView(mContext);
		viewPhoto.setLayoutParams(new LayoutParams((int)viewFrame.width,(int)viewFrame.height));
		viewPhoto.setImageResource(R.drawable.head_male2);
		
		view.addView(viewPhoto);
		if (labelName != null) {
			view.addView(labelName);
		}
		this.editModel = false;
	}

	public byte getPhotType() {
		return photType;
	}

	public void setPhotType(byte photType) {
		this.photType = photType;
		/*if (photType == PhotoCallback.PhotoTypeAdd) {
	        viewPhoto.setImageResource(R.drawable.add_friend);
	    } else if (photType == PhotoCallback.PhotoTypeDelete)
	    {
	    	viewPhoto.setImageResource(R.drawable.add_friend);
	    }*/
	}
	
	public void setPhotoPath(String photoPath)
	{
		viewPhoto.setImageBitmap(BitmapFactory.decodeFile(photoPath));
	}

	public void setShowName(String name)
	{
		if (isShowLabel && labelName != null) {
			labelName.setText(name);
		}
	   
	}
	void setEditMode(boolean edit)
	{
		if (this.photType == PhotoCallback.PhotoTypePhoto) {
	        if (this.editModel) {
	        	deleteMask.setVisibility(View.VISIBLE);
	        } else {
	        	deleteMask.setVisibility(View.GONE);
	        }
	    }
	}
	
}
