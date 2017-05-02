package com.afmobi.palmchat.util;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.core.Consts;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ToastManager {

private static ToastManager instance;
	
	private Toast mToast;
	
	private Context mContext;
	private boolean showing;
	private static final int DEFAULT = 3000;
	public boolean isShowing() {
		return showing;
	}
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			 if (msg.what > 0) {
//				 LogUtils.d("handleMessage", msg.what + "");
				 cancelToast();
			}
		}
	};
	private ToastManager(Context context) {
		this.mContext = context;
	}
	private ToastManager() {
	}
	
	protected synchronized void cancelToast() {
		if (mToast != null) {  
            mToast.cancel();  
            showing = false;
        }  
	}

	public static ToastManager getInstance() {
		if (instance == null) {
			instance = new ToastManager();
		}
		return instance;
	}
	
	public synchronized void show(Context context,String msg,int time) {
		if (mToast != null && showing) {
			Log.e("show", "cancel");
			//cancelToast();
		}
		if(context != null){
			mToast = Toast.makeText(context, msg, time);
			mToast.show();
			showing = true;
			Message delayMsg = handler.obtainMessage(time);  
	        handler.sendMessageDelayed(delayMsg, time);
		}
	}

	public synchronized void mShow(Context context,String msg,int time){
		if(mToast == null){
			mToast = Toast.makeText(context,msg,time);
		}else{
			mToast.setText(msg);
		}
		mToast.show();
	}

	
	public synchronized void show(Context context,int id) {
		if(context != null){
			show(context, context.getString(id), DEFAULT);
		}
	}
	public synchronized void show(Context context,String msg) {
		if(context != null){
			show(context, msg, DEFAULT);
		}
	}

	public synchronized void AddFriendsShow(Context context,String msg){
		if(context != null){
			mShow(context, msg, DEFAULT);
		}
	}
	
	public synchronized void show(Context context,int id,int time) {
		if(context != null){
			show(context, context.getString(id), time);
		}
	}
	
	public synchronized void showS(Context context,int id) {
		if(context != null){
			show(context, context.getString(id), Toast.LENGTH_SHORT);
		}
	}
	
	public synchronized void showS(Context context,String msg) {
		if(context != null){
			show(context, msg, Toast.LENGTH_SHORT);
		}
	}
	
	public synchronized void showL(Context context,int id) {
		if(context != null){
			show(context, context.getString(id), Toast.LENGTH_LONG);
		}
	}
	
	public synchronized void showL(Context context,String msg) {
		if(context != null){
			show(context, msg, Toast.LENGTH_LONG);
		}
	}

	public synchronized void showShareBroadcast(Context context,int time,boolean isShareStatus) {
		if(context != null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.activity_share_broadcast_send_tip, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.share_image_status);
			TextView tip = (TextView) view.findViewById(R.id.share_broadcast_status_text);
			if(isShareStatus){
				imageView.setBackgroundResource(R.drawable.icon_sheet_tagpage_share_succ);
				tip.setText(R.string.share_friend_success);
			}
			else{
				imageView.setBackgroundResource(R.drawable.icon_sheet_tagpage_share_failed);
				tip.setText(R.string.share_friend_failed);
			}

			if (mToast != null && showing) {
				Log.e("show", "cancel");
			}
			if(context != null){
				mToast =  new Toast(context);
				mToast.setView(view);
				mToast.setGravity(Gravity.CENTER_VERTICAL,0,0);
				mToast.show();
				showing = true;
				Message delayMsg = handler.obtainMessage(time);
				handler.sendMessageDelayed(delayMsg, time);
			}
		}
	}

	public synchronized void showShareBroadcast(Context context,int time,boolean isShareStatus,String tipContent) {
		if(context != null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.activity_share_broadcast_send_tip, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.share_image_status);
			TextView tip = (TextView) view.findViewById(R.id.share_broadcast_status_text);
			if(isShareStatus){
				imageView.setBackgroundResource(R.drawable.icon_sheet_tagpage_share_succ);
			}
			else{
				imageView.setBackgroundResource(R.drawable.icon_sheet_tagpage_share_failed);
			}
			tip.setText(tipContent);
			if (mToast != null && showing) {
				Log.e("show", "cancel");
			}
			if(context != null){
				mToast =  new Toast(context);
				mToast.setView(view);
				mToast.setGravity(Gravity.CENTER_VERTICAL,0,0);
				mToast.show();
				showing = true;
				Message delayMsg = handler.obtainMessage(time);
				handler.sendMessageDelayed(delayMsg, time);
			}
		}
	}
	
	public synchronized void showByCode(Context context,int code) {
		switch (code) {
		case Consts.REQ_CODE_UNNETWORK:
		{
			if(context != null){
				show(context, context.getString(com.afmobigroup.gphone.R.string.network_unavailable));
			}
		}
			break;
			
		default:
			if(context != null){
				show(context, context.getString(com.afmobigroup.gphone.R.string.unknown));
			}
			break;
		}
		
	}

	/**
	 * 弹出窗
	 * @param context getActivity
	 * @param isVisible 当前activity或fragment是否在前端
	 * @param id 资源ID
	 */
	public synchronized void show(Context context,boolean isVisible,int id){
		if (null != context) {
			if (isVisible) {//判断是否显示
				show(context, id);
			}
		}
	}

	/**
	 * 弹出窗
	 * @param context getActivity
	 * @param isVisible 当前activity或fragment是否在前端
	 * @param id 资源ID
	 */
	public synchronized void show(Context context,boolean isVisible,int id,String tag){
		if (null != context) {
			if (isVisible) {//判断是否显示
				PalmchatLogUtils.e(tag,"弹出窗出错");
				show(context, id);
			}
		}
	}

	/**
	 * 弹出窗
	 * @param context getActivity
	 * @param isVisible 当前activity或fragment是否在前端
     * @param msg 描述
	 */
	public synchronized void show(Context context,boolean isVisible,String msg){
		if (null != context) {
			if (isVisible) {//判断是否显示
				show(context, msg);
			}
		}
	}
	
	/**
	 * 弹出窗
	 * @param context getActivity
	 * @param isVisible 当前activity或fragment是否在前端
	 * @param msg 描述
	 */
	public synchronized void show(Context context,boolean isVisible,String msg,String tag){
		if (null != context) {
			if (isVisible) {//判断是否显示
				PalmchatLogUtils.e(tag,"弹出窗出错");
				show(context, msg);
			}
		}
	}
	
	public synchronized void showByCode(Context context,int code ,String msg) {
		
	}
}
