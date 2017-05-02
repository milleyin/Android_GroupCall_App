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
import android.widget.TextView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.listener.OnItemClick;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;

import org.w3c.dom.Text;

public class ForgetPasswordDialog extends Dialog{

	private Context mContext;
	private LinearLayout dialogView;
	private OnItemClick itemClick;
	
	private TextView mTv_phoneNumber,mTv_email, mTv_secureAnswer;
	
	public static final int FORGET_MODE_PHONE_NUMBER = 1;	
	public static final int FORGET_MODE_EMAIL = 2;
	public static final int FORGET_MODE_CANCEL = 3;
	public static final int FORGET_MODE_SECURITY = 8000;
	
	public OnItemClick getItemClick() {
		return itemClick; 
	}

	public void setItemClick(OnItemClick itemClick) {
		this.itemClick = itemClick;
	}

	
	public ForgetPasswordDialog(Context context,int forgetMode) {
		super(context , R.style.LoginDialogTheme);
		this.mContext = context;
		init(forgetMode);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	/***
	 * forgetMode 为当前的登陆模式
	 * @param forgetMode
	 */
	private void init(int forgetMode) {
		dialogView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_forget_password, null);
		mTv_phoneNumber = (TextView) dialogView.findViewById(R.id.tv_phoneNumber);
		mTv_phoneNumber.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnOnClick(FORGET_MODE_PHONE_NUMBER);
			}

		});
		mTv_email = (TextView) dialogView.findViewById(R.id.tv_email);
		mTv_email.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnOnClick(FORGET_MODE_EMAIL);
			}
		});
		
		mTv_secureAnswer = (TextView) dialogView.findViewById(R.id.tv_secure_answer);
		mTv_secureAnswer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnOnClick(FORGET_MODE_SECURITY);
			}
		});
		
		/*btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnOnClick(FORGET_MODE_CANCEL);
			}
		});*/
		
//		resetText(forgetMode);

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
			mTv_phoneNumber.setText(mContext.getString(R.string.phone_number));
			mTv_email.setText(mContext.getString(R.string.email));
			break;
		case LoginActivity.PHONE_NUMBER:
			mTv_phoneNumber.setText(mContext.getString(R.string.af_id));
			mTv_email.setText(mContext.getString(R.string.email));
			break;
		case LoginActivity.EMAIL:
			mTv_phoneNumber.setText(mContext.getString(R.string.af_id));
			mTv_email.setText(mContext.getString(R.string.phone_number));
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
