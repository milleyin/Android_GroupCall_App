package com.afmobi.palmchat.ui.customview;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.listener.OnItemClick;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.friends.adapter.AddFriendsSearchResultAdapter;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.util.MessagesUtils;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
//import com.umeng.analytics.MobclickAgent;

/**
 * ��ע���ٱ�Υ��Dialog
 */
public class AbuseDialog extends Dialog{

	private Context mContext;
	private LinearLayout dialogView;
	private OnItemClick itemClick;
	private TextView mTv_ReportAbuse;
	private TextView mBtn_Block;
	private TextView mTv_Follow;
	private LinearLayout ll_follow;
	private Button mBtn_Cancel;
	/**are you sure to block/unblock this user*/
	private TextView mTv_ReminderUsers;
	public static final int ACTION_ABUSE = 1;	
	public static final int ACTION_SEND = 2;
	public static final int ACTION_CANCEL = 3;
	public static final int ACTION_FOLLOW = 4;
	public static final int ACTION_BLOCK = 5;
	public static final int ACTION_YES = 6;
	private String afid ;
	private boolean isFollow;
	private int isFw;
	/**是否在黑名单*/
	private boolean mIsBlocked;

	public void setItemClick(OnItemClick itemClick) {
		this.itemClick = itemClick;
	}

	
	public AbuseDialog(Context context,String afid,int actionMode) {
		super(context , R.style.CustomDialogTheme);
		this.mContext = context;
		this.afid = afid;
		init(actionMode);
		isFw = actionMode;
	}
	
	public AbuseDialog(Context context,String afid,int actionMode,boolean isBlocked) {
		super(context , R.style.CustomDialogTheme);
		this.mContext = context;
		isFw = actionMode;
		this.afid = afid;
		mIsBlocked = isBlocked;
		init(actionMode);
	}

	/**
	 * ���캯��
	 * @param context
	 * @param actionMode
	 * @param afid
	 */
	public AbuseDialog(Context context,int actionMode,String afid) {
		super(context , R.style.CustomDialogTheme);
		this.mContext = context;
		this.afid = afid;
		init(actionMode);
	}
	
	/**
	 * 
	 * @param context
	 * @param actionMode
	 * @param afid 
	 * @param isBlocked 是否在黑名单
	 */
	public AbuseDialog(Context context,int actionMode,String afid,boolean isBlocked) {
		// TODO Auto-generated constructor stub
		super(context , R.style.CustomDialogTheme);
		this.mContext = context;
		this.afid = afid;
		this.mIsBlocked = isBlocked;
		afid = this.afid;
		init(actionMode);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	/**
	 * ����dialog�еİ�ť�¼�
	 * @param actionMode
	 */
	private void init(int actionMode) {
		if(ACTION_ABUSE == actionMode){
			dialogView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_abuse, null);
		}
		mTv_ReportAbuse = (TextView) dialogView.findViewById(R.id.tv_report_abuse);
		if(mTv_ReportAbuse != null){
			if(afid.startsWith("r")){
				mTv_ReportAbuse.setVisibility(View.GONE);
			}else{
				mTv_ReportAbuse.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						btnOnClick(ACTION_ABUSE);
					}

				});
			}

		}
		mBtn_Cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
		if(mBtn_Cancel != null){
			mBtn_Cancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					btnOnClick(ACTION_CANCEL);
				}
			});
		}
		ll_follow = (LinearLayout)dialogView.findViewById(R.id.ll_follow);
		mTv_Follow = (TextView) dialogView.findViewById(R.id.tv_follow);
		if(mTv_Follow != null){
			set_IsFollow();
			if(mIsBlocked) {
				mTv_Follow.setVisibility(View.GONE);
				ll_follow.setVisibility(View.GONE);
			}
			mTv_Follow.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					btnOnClick(ACTION_FOLLOW);
				}
			});
		}
		
		mBtn_Block = (TextView)dialogView.findViewById(R.id.tv_block);
		//更改为unblock
		if(afid.startsWith("r")){
			mBtn_Block.setVisibility(View.GONE);
		}else{
			if(mIsBlocked&&(null!=mBtn_Block)) {
				mBtn_Block.setText(R.string.un_block);
			}

			if(mBtn_Block != null){
				mBtn_Block.setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View v){
						btnOnClick(ACTION_BLOCK);
					}
				});
			}
		}
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
		if(isFw != ACTION_SEND && isFw != ACTION_YES){
//			set_IsFollow();
		}
	}
	
	private void btnOnClick(int item) {
		if(itemClick != null){
			itemClick.onItemClick(item);
		}
		cancel();
	}

	/**
	 * �ж��Ƿ��ע������
	 */
	public void set_IsFollow(){
		if (!CacheManager.getInstance().getClickableMap().containsKey(afid + CacheManager.follow_suffix)) {
			CacheManager.getInstance().getClickableMap().put(afid + CacheManager.follow_suffix ,false);
		}
		isFollow = CacheManager.getInstance().isFollow(afid);
		if (!isFollow && !CacheManager.getInstance().getClickableMap().get(
				afid + CacheManager.follow_suffix)) {
			mTv_Follow.setText(R.string.follow);
		}else {
			mTv_Follow.setText(R.string.unfollow);

		}
		if (!CacheManager.getInstance().getClickableMap().get(afid + CacheManager.follow_suffix)) {
			PalmchatLogUtils.e("lin_tab_follow.setClickable", "lin_tab_follow.setClickable = true"+" | isFollow= "+isFollow);
		}else {
			PalmchatLogUtils.e("lin_tab_follow.setClickable", "lin_tab_follow.setClickable = false"+" | isFollow= "+isFollow);
		}
	}
	
}
