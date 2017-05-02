package com.afmobi.palmchat.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.listener.OnItemClick;


public class ChattingMenuDialog extends Dialog  {
	
	public static final int ACTION_SPEAKER_MODE = 0;
	public static final int ACTION_HANDSET_MODE = 1;
	public static final int ACTION_CLEAR_DATA = 2;
	public static final int ACTION_CANCEL = 3;
	
	private Context mContext;
	private LinearLayout dialogView;
	private OnItemClick itemClick;
	
	
	private int mode;
	public OnItemClick getItemClick() {
		return itemClick;
	}

	public void setItemClick(OnItemClick itemClick) {
		this.itemClick = itemClick;
	}

	
	public ChattingMenuDialog(Context context,int mode) {
		super(context , R.style.CustomDialogTheme);
		this.mContext = context;
		this.mode = mode;
		
		init();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	private void init() {
		dialogView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.dialog_chatting_menu, null);
		ImageView imageSpeaker = (ImageView) dialogView.findViewById(R.id.image_speaker_mode);
		ImageView imageHandset = (ImageView) dialogView.findViewById(R.id.image_handset_mode);
		if(mode == AudioManager.MODE_IN_CALL){
//			res = new int[] {R.drawable.menu_speaker_icon ,R.drawable.menu_clear_icon };
//			text = new String[]{getString(R.string.switch_speaker_mode),getString(R.string.clear_chat_records)};
			imageSpeaker.setBackgroundResource(R.drawable.radiobutton_normal);
			imageHandset.setBackgroundResource(R.drawable.radiobutton_press);
		}else{
			imageSpeaker.setBackgroundResource(R.drawable.radiobutton_press);
			imageHandset.setBackgroundResource(R.drawable.radiobutton_normal);
//			res = new int[] {R.drawable.menu_handset_icon ,R.drawable.menu_clear_icon };
//			text = new String[]{getString(R.string.switch_handset_mode),getString(R.string.clear_chat_records)};
		}
		Button btn_speak_mode = (Button)dialogView.findViewById(R.id.btn_speaker_mode);
		btn_speak_mode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(itemClick != null){
					itemClick.onItemClick(ACTION_SPEAKER_MODE);
				}
			}
		});
		Button btn_handset_mode = (Button)dialogView.findViewById(R.id.btn_handset_mode);
		btn_handset_mode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(itemClick != null){
					itemClick.onItemClick(ACTION_HANDSET_MODE);
				}
			}
		});
		Button btn_clear_data = (Button)dialogView.findViewById(R.id.btn_clear_data);
		btn_clear_data.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(itemClick != null){
					itemClick.onItemClick(ACTION_CLEAR_DATA);
				}
			}
		});
		
		Button btn_cancel = (Button)dialogView.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(itemClick != null){
					itemClick.onItemClick(ACTION_CANCEL);
				}
			}
		});
		
		WindowManager.LayoutParams localLayoutParams = getWindow()
				.getAttributes();
		localLayoutParams.x = 0;
		localLayoutParams.y = -1000;
		localLayoutParams.gravity = Gravity.BOTTOM;
		dialogView.setMinimumWidth(10000);

		onWindowAttributesChanged(localLayoutParams);
		setCanceledOnTouchOutside(true);
		setCancelable(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(dialogView);
	}
	
}
