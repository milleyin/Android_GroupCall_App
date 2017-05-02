package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.afmobi.palmchat.BaseActivity;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.CommonUtils;

public class MySideBar extends View {

	private char[] l = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',  'H',
			 'I',  'J',
			 'K', 'L', 'M', 'N',  'O', 
			 'P',  'Q',
'R', 'S', 'T', 'U', 'V',
			 'W', 'X', 'Y',
			  'Z','#'};
	public void setCharArr(List<String >tempSections){
		l=new char[tempSections.size()];
		String _str=null;
		for(int i=0;i<tempSections.size();i++){
			  _str=tempSections.get(i);
			  if(!TextUtils.isEmpty(_str)){
				 l[i]= _str.charAt(0);
			  } 
		}
		l[tempSections.size()-1]='#';
	}
	private ListView list;
	private TextView mDialogText;
	Bitmap mbitmap;
	private int orientation;
	private Context context;
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public MySideBar(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public MySideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		mbitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.scroll_bar_search_icon);
		Configuration cf= this.getResources().getConfiguration(); //获取设置的配置信息
		orientation = cf.orientation;
		textSize=getContext().getResources().getDimension(R.dimen.sideBarTextSize);
	}

	public MySideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}


	public void setListView(ListView _list) {
		list = _list;
	}

	private SectionIndexer sectionIndexter;
	
	public SectionIndexer getSectionIndexter() {
		return sectionIndexter;
	}

	public void setSectionIndexter(SectionIndexer sectionIndexter) {
		this.sectionIndexter = sectionIndexter;
	}

	public void setTextView(TextView mDialogText) {
		this.mDialogText = mDialogText;
	}

	public boolean onTouchEvent(MotionEvent event) {

		super.onTouchEvent(event);
		int i = (int) event.getY();
		int idx = i / (getMeasuredHeight() / l.length);
		if (idx >= l.length) {
			idx = l.length - 1;
		} else if (idx < 0) {
			idx = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
//			setBackgroundResource(R.drawable.scrollbar_bg);
			mDialogText.setVisibility(View.VISIBLE);
//			if (idx == 0) {
//				mDialogText.setText("Search");
//				mDialogText.setTextSize(22);
//			} else {
				mDialogText.setText(String.valueOf(l[idx]));
				mDialogText.setTextSize(34);
//			}
			if (sectionIndexter == null) {
				sectionIndexter = (SectionIndexer) list.getAdapter();
			}
			if(selectIndex!=idx){
				selectIndex=idx;
				invalidate();
			}
			
			int position = sectionIndexter.getPositionForSection(l[idx]);
			if (position == -1) {
				return true;
			}
			list.setSelection(position);
		} else {
			mDialogText.setVisibility(View.INVISIBLE);

		}
		/*if (event.getAction() == MotionEvent.ACTION_UP) {
			setBackgroundDrawable(new ColorDrawable(0x00000000));
		}*/
		return true;
	}
	Paint paint = new Paint();
	DisplayMetrics dm = new DisplayMetrics();
	public void setNowIndex(int inx){
		selectIndex=inx;
		invalidate();
	}
	private int selectIndex;
	float textSize;
	protected void onDraw(Canvas canvas) {  
		int h;
		((BaseActivity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;//屏幕分辨率的高
		int width = dm.widthPixels;//屏幕分辨率的宽
		
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			int min = Math.min(width, height);
			int max = Math.max(width, height);
			float s = (float) min / max;
			paint.setTextSize( textSize* s - 2);
		} else {
			paint.setTextSize(textSize);
		}
		h = CommonUtils.dip2px(getContext(), 46);
		float widthCenter = getMeasuredWidth() / 2; 
		
		if (l.length > 0) {
			float p_height = (float)(getMeasuredHeight() - h) / l.length;
			paint.setColor(0xff555555 ); 
			paint.setStyle(Style.FILL); 
			canvas.drawCircle( widthCenter,
					selectIndex * p_height + h, widthCenter, paint); 
	
			paint.setTextAlign(Paint.Align.CENTER); 
			FontMetrics fontMetrics=paint.getFontMetrics();
			float fontHeight=fontMetrics.bottom-fontMetrics.top;
			for (int i = 0; i < l.length; i++) { 
				if(i==selectIndex){
					paint.setColor(0xffffffff );
				}else{
					paint.setColor(0xff838383 );
				}
				canvas.drawText(String.valueOf(l[i]), widthCenter,
						(i) * p_height + h+
						  fontHeight/2  - fontMetrics.bottom, paint);
			}
		} 
		 
			super.onDraw(canvas);
	}
	 
}
