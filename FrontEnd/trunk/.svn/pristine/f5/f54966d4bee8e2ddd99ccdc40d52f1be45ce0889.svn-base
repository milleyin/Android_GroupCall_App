package com.afmobi.palmchat.ui.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager.SystemBarConfig;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.SharePreferenceUtils;

import java.util.ArrayList;

public class AppFunctionTips_pop_Utils {
	private PopupWindow pop;
	
private static AppFunctionTips_pop_Utils appFunctionTips_pop_Utils;
	
	public static AppFunctionTips_pop_Utils getInit(){
		if (appFunctionTips_pop_Utils == null) {
			appFunctionTips_pop_Utils = new AppFunctionTips_pop_Utils();
		}
		return appFunctionTips_pop_Utils;
	}
	private Context mContext;
	public void showFunctionTips_pop(final Context c , final View view,final int msg_id, final int img_resid) {
		boolean isFirst = SharePreferenceUtils.getInstance(c).getFunctionTips_Key(msg_id+"");
		mContext=c;
		if (isFirst) {
			LayoutInflater inflater = LayoutInflater.from(c);
			// 引入窗口配置文件
			View inflater_view = inflater.inflate(R.layout.functiontips_pop, null);
			inflater_view.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					dismiss();
					return false;
				}
			});
			LinearLayout linearLayout = (LinearLayout) inflater_view.findViewById(R.id.lin_bg_functiontips);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			TextView txt_msg = (TextView) inflater_view.findViewById(R.id.txt_msg);
			int Height = 0;
			int[] location = new int[2];  
			view.getLocationOnScreen(location);  
			int x = location[0];
			int y = location[1];
			
			PalmchatLogUtils.println("showFunctionTips_pop x = " + x + " y = " + y); 
			
			if (x < 0) {
				return;
			}
			
		
			txt_msg.setText(c.getString(msg_id));
			linearLayout.setBackgroundResource(img_resid);
			inflater_view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
 			
			
//			
//			Log.i("showFunctionTips_pop", "view x = " + x + "view  y = " + y + "pop x= "  + pop.getWidth() + "pop y=" + pop.getHeight() + "linearLayout x= " + linearLayout.getWidth() + "linearLayout y =" + linearLayout.getHeight() + "txt_msg x= " + txt_msg.getWidth()
//					+ "txt_msg y= " + txt_msg.getHeight() + "inflater_view x" + inflater_view.getMeasuredWidth() + "inflater_view y = " + inflater_view.getMeasuredHeight());
			ColorDrawable dw;
			switch (msg_id) {
			case R.string.bc_edit_functiontips:
				SharePreferenceUtils.getInstance(c).setFunctionTips_Key(R.string.bc_edit_functiontips+"", false);
				pop = new PopupWindow(inflater_view,  inflater_view.getMeasuredWidth(), inflater_view.getMeasuredHeight() , false);
				//pop.setAnimationStyle(R.style.popwin_anim_style);
				// 实例化一个ColorDrawable颜色为半透明
				dw = new ColorDrawable(Color.TRANSPARENT);
				// 需要设置一下此参数，点击外边可消失
				pop.setBackgroundDrawable(dw);
				// 设置非PopupWindow区域可触摸
				pop.setOutsideTouchable(true);
				// 设置此 参数获得焦点，否则无法点击
				pop.setFocusable(true);
				pop.showAtLocation(view, Gravity.NO_GRAVITY, x-30, y-pop.getHeight()); 
//				y = y + view.getHeight();
//				y = y + 200;
//				Height = view.getHeight() + CommonUtils.dip2px(c, 8);
//				Height = setNavigtionBar_H(((BaseFragmentActivity)c).systemBarConfig, Height);
//				params.setMargins(0, 0,  CommonUtils.dip2px(c, 4), Height);
//				linearLayout.setLayoutParams(params);
				
				break;
			case R.string.profile_bc_functiontips:
				Height =  view.getHeight() -  view.getHeight()/5;
				params.setMargins(0, Height,  CommonUtils.dip2px(c, 25), 0);
				linearLayout.setLayoutParams(params);
				SharePreferenceUtils.getInstance(c).setFunctionTips_Key(R.string.profile_bc_functiontips+"", false);
				
				pop = new PopupWindow(inflater_view, LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT, false);
				//pop.setAnimationStyle(R.style.popwin_anim_style);
				// 实例化一个ColorDrawable颜色为半透明
				dw = new ColorDrawable(Color.TRANSPARENT);
				// 需要设置一下此参数，点击外边可消失
				pop.setBackgroundDrawable(dw);
				// 设置非PopupWindow区域可触摸
				pop.setOutsideTouchable(true);
				// 设置此 参数获得焦点，否则无法点击
				pop.setFocusable(true);
				pop.showAtLocation(view, Gravity.NO_GRAVITY, x, y);  
				break;
			case R.string.chatting_functiontips:
				x -= CommonUtils.dip2px(c, 15);
				y -= CommonUtils.dip2px(c, 85);
				Height = (int) (view.getHeight()) ;
				Height = setNavigtionBar_H(((BaseActivity)c).systemBarConfig, Height);
				params.setMargins(CommonUtils.dip2px(c, 6), 0,  0, Height);
				linearLayout.setLayoutParams(params);
				SharePreferenceUtils.getInstance(c).setFunctionTips_Key(R.string.chatting_functiontips+"", false);
				
				pop = new PopupWindow(inflater_view, LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT, false);
				//pop.setAnimationStyle(R.style.popwin_anim_style);
				// 实例化一个ColorDrawable颜色为半透明
				dw = new ColorDrawable(Color.TRANSPARENT);
				// 需要设置一下此参数，点击外边可消失
				pop.setBackgroundDrawable(dw);
				// 设置非PopupWindow区域可触摸
				pop.setOutsideTouchable(true);
				// 设置此 参数获得焦点，否则无法点击
				pop.setFocusable(true);
				pop.showAtLocation(view, Gravity.NO_GRAVITY, x, y);  
				break;
			case R.string.send_bc_voice_functiontips:
				Height = (int) (view.getHeight());
				Height = setNavigtionBar_H(((BaseActivity)c).systemBarConfig, Height);
				int w = (int) (PalmchatApp.getApplication().getWindowWidth()* 0.8);
				params.width =  w;
				int left = ((ImageUtil.DISPLAYW - w) / 2 + view.getWidth() / 2) - (ImageUtil.DISPLAYW/2 - x);
				params.setMargins(left, 0, 0, Height+CommonUtils.dip2px(c, 10));	
				x = 0;
				linearLayout.setLayoutParams(params);
				SharePreferenceUtils.getInstance(c).setFunctionTips_Key(R.string.send_bc_voice_functiontips+"", false);
				
				pop = new PopupWindow(inflater_view, LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT, false);
				//pop.setAnimationStyle(R.style.popwin_anim_style);
				// 实例化一个ColorDrawable颜色为半透明
				dw = new ColorDrawable(Color.TRANSPARENT);
				// 需要设置一下此参数，点击外边可消失
				pop.setBackgroundDrawable(dw);
				// 设置非PopupWindow区域可触摸
				pop.setOutsideTouchable(true);
				// 设置此 参数获得焦点，否则无法点击
				pop.setFocusable(true);
				pop.showAtLocation(view, Gravity.NO_GRAVITY, x, y);  
				break;
				
			default:
				break;
			}
			
//			txt_msg.setText(c.getString(msg_id));
//			linearLayout.setBackgroundResource(img_resid);
			
		
//			pop = new PopupWindow(inflater_view, LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT, false);
//			//pop.setAnimationStyle(R.style.popwin_anim_style);
//			// 实例化一个ColorDrawable颜色为半透明
//			ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
//			// 需要设置一下此参数，点击外边可消失
//			pop.setBackgroundDrawable(dw);
//			// 设置非PopupWindow区域可触摸
//			pop.setOutsideTouchable(true);
//			// 设置此 参数获得焦点，否则无法点击
//			pop.setFocusable(true);
			
			
			// 设置显示的方位
//			if (isUpper) {
//				pop.showAtLocation(view, Gravity.NO_GRAVITY, x, y);  
//			}else {
//				pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]+ Height); 
//			}
  		}

	}


	/*ColorDrawable dw;
	public void showMatchingTag_pop(Context context,View view,String[] string){

		LayoutInflater inflater = LayoutInflater.from(context);
		// 引入窗口配置文件
		View inflater_view = inflater.inflate(R.layout.activity_broadcast_area_listview, null);
		ListView listview = (ListView)inflater_view.findViewById(R.id.area_listview);
		MacChingTagAdapter macChingTagAdapter = new MacChingTagAdapter(context,string);
		listview.setAdapter(macChingTagAdapter);


		pop = new PopupWindow(inflater_view, WindowManager.LayoutParams.WRAP_CONTENT,  WindowManager.LayoutParams.WRAP_CONTENT);
		//pop.setAnimationStyle(R.style.popwin_anim_style);
		// 实例化一个ColorDrawable颜色为半透明
		dw = new ColorDrawable(Color.TRANSPARENT);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(dw);
		// 设置非PopupWindow区域可触摸
		pop.setOutsideTouchable(true);
		// 设置此 参数获得焦点，否则无法点击
		pop.setFocusable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.showAsDropDown(view, 0, 0);

	}*/

	private int setNavigtionBar_H(SystemBarConfig systemBarConfig, int Height) {
		if (systemBarConfig != null) {
			if (systemBarConfig.hasNavigtionBar()) {
				if (!(PalmchatApp.getOsInfo().getUa().contains("HTC") || PalmchatApp.getOsInfo().getUa().contains("MI")
						|| PalmchatApp.getOsInfo().getBrand().contains("Meizu"))) {//过滤htc,小米
					Height = Height  + systemBarConfig.getNavigationBarHeight();
				}
			}
		}
		return Height;
	}

	public void showFunctionTips_pop(final Context c , final View view,final int layout_id, final boolean isUpper) {
		LayoutInflater inflater = LayoutInflater.from(c);
		// 引入窗口配置文件
		View inflater_view = inflater.inflate(layout_id, null);
//		LinearLayout linearLayout = (LinearLayout) inflater_view.findViewById(R.id.lin_bg_functiontips);
		pop = new PopupWindow(inflater_view,  CommonUtils.dip2px(c, 200),  LayoutParams.WRAP_CONTENT , false);
//		pop.setAnimationStyle(R.style.popwin_anim_style);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(dw);
		// 设置非PopupWindow区域可触摸
		pop.setOutsideTouchable(true);
		// 设置此 参数获得焦点，否则无法点击
		pop.setFocusable(true);
		pop.update();
		// 设置显示的方位
		if (isUpper) {
			int[] location = new int[2];  
			view.getLocationOnScreen(location);  
			pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]- (view.getHeight()));  
		}else {
			pop.showAsDropDown(view);
		}
		
	}
	
	public void dismiss(){
		if(mContext!=null &&mContext instanceof Activity){
			if(!((Activity)mContext).isFinishing()){
				if (pop != null && pop.isShowing()) {
					pop.dismiss();
				}
			}
		} 
	}

	/*class MacChingTagAdapter extends BaseAdapter{

		private Context context;
		private String[] string;


		public MacChingTagAdapter(Context context,String[] string){
			this.context = context;
			this.string = string;
		}

		@Override
		public int getCount() {
			return string.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}



		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			LayoutInflater inflater = LayoutInflater.from(context);
			if(convertView == null){
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.invitefrineds_son,null);
				viewHolder.mTextInvitefriends = (TextView)convertView.findViewById(R.id.text_invitefriends);
				viewHolder.mImgPlayerPhotoInvitefriends = (ImageView)convertView.findViewById(R.id.img_player_photo_invitefriends);
				viewHolder.mTextFollowInvitefriends = (TextView)convertView.findViewById(R.id.text_follow_invitefriends);
				viewHolder.mTextFollowInvitefriends.setVisibility(View.GONE);
				viewHolder.mImgPlayerPhotoInvitefriends.setVisibility(View.GONE);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)convertView.getTag();
			}
				viewHolder.mTextInvitefriends.setText(string[position]);

			return convertView;
		}

		public class ViewHolder{
			TextView mTextInvitefriends;
			ImageView mImgPlayerPhotoInvitefriends;
			TextView mTextFollowInvitefriends;
		}
	}*/

}
