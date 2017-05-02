package com.afmobi.palmchat.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.listener.OnItemClick;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;

public class LoginDialog extends Dialog  {
	
	private Context mContext;
	private LinearLayout dialogView;
	private OnItemClick itemClick;
	
	private Button btn_phoneNumber,btn_email,btn_cancel;
	
	public static final int BTN_PHONE_NUMBER = 1;
	public static final int BTN_EMAIL  = 2;	
	public static final int BTN_CANCEL = 3;	
	
	public OnItemClick getItemClick() {
		return itemClick; 
	}

	public void setItemClick(OnItemClick itemClick) {
		this.itemClick = itemClick;
	}

	
	public LoginDialog(Context context,int loginMode) {
		super(context , R.style.LoginDialogTheme);
		this.mContext = context;
		init(loginMode);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	/***
	 * loginMode 为当前的登陆模式
	 * @param loginMode
	 */
	private void init(int loginMode) {
		dialogView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_login, null);
		btn_phoneNumber = (Button) dialogView.findViewById(R.id.btn_phoneNumber);
		btn_phoneNumber.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnOnClick(BTN_PHONE_NUMBER);
			}

		});
		btn_email = (Button) dialogView.findViewById(R.id.btn_email);
		btn_email.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnOnClick(BTN_EMAIL);
			}
		});
		btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnOnClick(BTN_CANCEL);
			}
		});
		
		resetText(loginMode);

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

	
	private void resetText(int loginMode) {
		// TODO Auto-generated method stub
		switch (loginMode) {
		case LoginActivity.AFID:
			btn_phoneNumber.setText(mContext.getString(R.string.phone_number));
			btn_email.setText(mContext.getString(R.string.email));
			break;
		case LoginActivity.PHONE_NUMBER:
			btn_phoneNumber.setText(mContext.getString(R.string.af_id));
			btn_email.setText(mContext.getString(R.string.email));
			break;
		case LoginActivity.EMAIL:
			btn_phoneNumber.setText(mContext.getString(R.string.af_id));
			btn_email.setText(mContext.getString(R.string.phone_number));
			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(dialogView);
	}
	
	
	private void btnOnClick(int item) {
		if(itemClick != null){
			itemClick.onItemClick(item);
		}
		cancel();
	}
	
	
}
