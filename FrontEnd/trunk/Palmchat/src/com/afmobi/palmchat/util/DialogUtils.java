package com.afmobi.palmchat.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.view.Window;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.ui.activity.setting.MyAccountInfoActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobigroup.gphone.R;
import com.core.listener.AfHttpResultListener;

public class DialogUtils {
	
	Dialog customDialog;
	
	public final void showCustomProgressDialog(Context context, String msg) {
		if (customDialog == null) {
			customDialog = new Dialog(context,
					android.R.style.Theme_Translucent_NoTitleBar);
			customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			customDialog.setContentView(R.layout.dialog_progress);
			customDialog.setCanceledOnTouchOutside(false);
		}
		
		((TextView) customDialog.findViewById(R.id.dialog_progress_tvMessage))
				.setText(msg);
		customDialog.show();
	}
	
	public final void dismissCustomProgressDialog() {
		if (customDialog != null && customDialog.isShowing()) {
			customDialog.dismiss();
		}
	}
	
	

	public static void confirmDialog(Context context,String text,OnClickListener confim) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(text);
		builder.setPositiveButton(R.string.confirm, confim);

		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	public static void confirmDialog(Context context,String title , String text) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(text);
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		builder.create().show();
	}
	
	
	public static void confirmDialog(Context context,String confirmText,String title , String text,DialogInterface.OnClickListener confirm) {
		try {
			AlertDialog.Builder builder = new Builder(context);
			builder.setTitle(title);
			builder.setMessage(text);
			builder.setNeutralButton(confirmText, confirm);
			builder.setPositiveButton (R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void confirmDialog(Context context,String title , String text,OnClickListener confirm) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(text);
		builder.setPositiveButton(R.string.settings, confirm);
		builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	/**
	 * zhh
	 * 绑定手机号对话框
	 * @param context
	 * @param msg
	 */
	public static void toBindPhoneDialog(final Context context) {
		  AppDialog appDialog = new AppDialog(context);
          String msg = context.getString(R.string.link_your_phone);
          appDialog.createConfirmDialog(context, msg,
                  new AppDialog.OnConfirmButtonDialogListener() {
                      @Override
                      public void onRightButtonClick() {
                          Intent it = new Intent();
                          it.setClass(context, MyAccountInfoActivity.class);
                          it.putExtra(MyAccountInfoActivity.STATE, MyAccountInfoActivity.PHONE_UNBIND);
                          String countryCode = PalmchatApp.getOsInfo().getCountryCode();
                          countryCode = "+" + countryCode;
                          String country = PalmchatApp.getOsInfo().getCountry(context);
                          it.putExtra(MyAccountInfoActivity.PARAM_COUNTRY_CODE, countryCode);
                          it.putExtra(MyAccountInfoActivity.PARAM_COUNTRY_NAME, country);
                          context.startActivity(it);
                      }

                      @Override
                      public void onLeftButtonClick() {

                      }
                  });
          appDialog.show();
	}
//	
//	/**
//	 * zhh 重新登录对话框
//	 * 
//	 * @param context
//	 */
//	public static void showReLoginDialog(final Context context,final AfHttpResultListener afHttpResultListener) {
//		AppDialog appDialog = new AppDialog(context);
//		appDialog.setCancelable(false);
//		String msg = context.getString(R.string.to_relogin);
//		appDialog.createOKDialog(context, msg, new OnConfirmButtonDialogListener() {
//
//			@Override
//			public void onLeftButtonClick() {
//				
//			}
//
//			@Override
//			public void onRightButtonClick() {
//				
//			}
//
//		});
//		
//		appDialog.setOnDismissListener(new OnDismissListener() {
//			
//			@Override
//			public void onDismiss(DialogInterface dialog) {
//				AppUtils.doLogout(context,afHttpResultListener);
//			}
//		});
//		  appDialog.show();
//	}
	


}
