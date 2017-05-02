package com.afmobi.palmchat.ui.customview;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.ImageUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View {
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	private final String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };
	
	private static final int BG_NORMAL = PalmchatApp.getApplication().getResources().getColor(R.color.transparent);
	private static final int BG_SELECTED = PalmchatApp.getApplication().getResources().getColor(R.color.slid_bar_bg_selected);
	private static final int LETTER_NORMAL = PalmchatApp.getApplication().getResources().getColor(R.color.text_level_2);
	private static final int LETTER_SELECTED = PalmchatApp.getApplication().getResources().getColor(R.color.white);
	private int choose = -1; 
	private Paint paint = new Paint();

	private TextView mTextDialog;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}


	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SideBar(Context context) {
		super(context);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight(); 
		int width = getWidth();  
		int singleHeight = height / b.length; 

		for (int i = 0; i < b.length; i++) {
			paint.setColor(LETTER_NORMAL);
			// paint.setColor(Color.WHITE);
			paint.setTypeface(Typeface.DEFAULT);
			paint.setAntiAlias(true);
			paint.setTextSize(ImageUtil.dip2px(PalmchatApp.getApplication(), 12));
			if (i == choose) {
				paint.setColor(LETTER_SELECTED);
				paint.setFakeBoldText(true);
			}
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();
		}

	}

	public void reset() {
		if (choose != -1) {
		setBackgroundColor(BG_NORMAL);
		choose = -1;//
		invalidate();
		if (mTextDialog != null) {
			mTextDialog.setVisibility(View.INVISIBLE);
		}
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length);
		switch (action) {
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				setBackgroundColor(BG_NORMAL);
				choose = -1;//
				invalidate();
				if (mTextDialog != null) {
					mTextDialog.setVisibility(View.INVISIBLE);
				}
				return true;
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_DOWN:
				if (event.getX() < 0) {
					setBackgroundColor(BG_NORMAL);
					choose = -1;//
					invalidate();
					if (mTextDialog != null) {
						mTextDialog.setVisibility(View.INVISIBLE);
					}
					return true;
				} else {
					setBackgroundColor(BG_SELECTED);
					if (oldChoose != c) {
						if (c >= 0 && c < b.length) {
							if (listener != null) {
								listener.onTouchingLetterChanged(b[c]);
							}
							if (mTextDialog != null) {
								mTextDialog.setText(b[c]);
								mTextDialog.setVisibility(View.VISIBLE);
							}

							choose = c;
							invalidate();
						}
					}
					return true;
				}
		}
		return super.onTouchEvent(event);
	}

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}
}