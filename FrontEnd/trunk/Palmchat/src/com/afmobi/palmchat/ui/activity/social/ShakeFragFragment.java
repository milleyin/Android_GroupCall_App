package com.afmobi.palmchat.ui.activity.social;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.social.ShakeListener.OnShakeListener;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.ToastManager;
//import com.afmobi.palmchat.util.image.ImageOnScrollListener;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

/**
 * Shake
 * 
 * @author Heguiming
 * 
 */
@SuppressWarnings("deprecation")
public class ShakeFragFragment extends BaseFragment implements OnClickListener,
		AfHttpResultListener, OnItemClickListener {

	private static final String TAG = ShakeFragFragment.class.getCanonicalName();
	
	private static final int UPDATE_AF_FRIEND = 9000; 
	private static final int RESULT_ANIM_END = 9001;
	private static final int RESET_SHAKE_STATE = 9002;
	private static final int RESET_SURFACE_VIEW = 9003;
	
	private ImageView mShakeAnimImg;
	
	private ImageView mHeadImg;
	private TextView mAgeTxt, mNameTxt, mSignTxt, mRegionTxt;
	private SlidingDrawer mSlidingDrawer;
//	private ListViewAddOn mListViewAddOn = new ListViewAddOn();
	private ListView mListView;
	private View mTitleView;
	private ImageView mArrowImg;
	private TextView mShakeTxt1, mShakeTxt2;
	private ImageView mAddImg;
	
	private SoundManager mSoundManager;
	private boolean isShaking, isFragStart;
	
	private View shakeFriendProfileLayout;
	private List<AfFriendInfo> friendInfoList;
	
	/**
	 * Looper线程
	 */
	private LooperThread looperThread;
	
	private ShakeAdapter mAdapter;
	
	private TranslateAnimation mShowAction;
	private TranslateAnimation mHiddenAction;
	
	private ShakeListener mShakeListener;
	
	@SuppressWarnings("unchecked")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE_AF_FRIEND:
				if(isAdded()) {
					friendInfoList = (List<AfFriendInfo>)msg.obj;
					if (null != friendInfoList && !friendInfoList.isEmpty()) {
						startAnim(R.anim.shake_show_result);
					}
				}
				break;
			case RESULT_ANIM_END:
				showShakingResult();
				break;
			case RESET_SHAKE_STATE:
				isShaking = false;
				break;
			case RESET_SURFACE_VIEW:
//				startAnim(R.anim.shake_shake_in);
				break;
			}
		}
	};
	
	public ShakeFragFragment() {
		
	}
//	public ShakeFrag(ShakeListener mShakeListener) {
//		this.mShakeListener = mShakeListener;
//	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		setContentView(R.layout.activity_shake_frag);
		initViews();
		initData();
		
		return mMainView;
	}
	
	private void initData() {
		looperThread = new LooperThread();
		looperThread.setName(TAG);
		looperThread.start();
	}

	private void startAnim(final int resId) {
		
		stopAnim();
		if(mShakeAnimImg != null){
			mShakeAnimImg.setBackgroundResource(resId);
			mShakeAnimImg.setTag(R.id.shake_anim_img, resId);
			
			final AnimationDrawable animationDrawable = (AnimationDrawable) mShakeAnimImg.getBackground();
			
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					animationDrawable.start();
					if (resId == R.anim.shake_show_result) {
						mHandler.sendEmptyMessageDelayed(RESULT_ANIM_END, 800);
					}
				}
			});
		}
		
	}
	
	private void stopAnim() {
		Drawable oldDraw = mShakeAnimImg.getBackground();
		if (null != oldDraw && oldDraw instanceof AnimationDrawable) {
			AnimationDrawable oldAnimDraw = (AnimationDrawable)oldDraw;
			oldAnimDraw.stop();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
//		MobclickAgent.onPageStart("ShakeFrag");
		
		isFragStart = true;
		if (null != mSlidingDrawer && !mSlidingDrawer.isOpened()) {
			if (null != mShakeAnimImg) {
				Object obj = mShakeAnimImg.getTag(R.id.shake_anim_img);
				if (null != obj && obj instanceof Integer) {
					int resId = (Integer) mShakeAnimImg.getTag(R.id.shake_anim_img);
					if (resId != R.drawable.shakeshake01) {
						mHandler.sendEmptyMessageDelayed(RESET_SURFACE_VIEW, 200);
					} else if (resId == R.anim.shaking) {
						startAnim(R.anim.shaking);
						
					}
				} else {
					mHandler.sendEmptyMessageDelayed(RESET_SURFACE_VIEW, 200);
				}
			}
		}
		if (null != friendInfoList && !friendInfoList.isEmpty()) {
			AfFriendInfo friend = friendInfoList.get(0);
			//WXL 20151015 调UIL的显示头像方法,统一图片管理
			ImageManager.getInstance().DisplayAvatarImage( mHeadImg, friend.getServerUrl(),
					friend.afId,Consts.AF_HEAD_MIDDLE,  friend.sex,friend.getSerialFromHead(),null);  
			 
		}
		if (null != mAdapter) {
			List<AfFriendInfo> friendInfoList = mAdapter.getFriendInfoList();
			if (null != friendInfoList && !friendInfoList.isEmpty()) {
				List<AfFriendInfo> list = new ArrayList<AfFriendInfo>();
				list.addAll(friendInfoList);
				setAdapter(list);
			}
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
//		MobclickAgent.onPageStart("ShakeFrag");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		isFragStart = false;
		stopAnim();
	}
	
	private void initViews() {
		mSoundManager = PalmchatApp.getApplication().getSoundManager();
		
		((TextView) findViewById(R.id.title_text)).setText(getFragString(R.string.shake_shake));
		
		shakeFriendProfileLayout = findViewById(R.id.shake_friend_profile_layout);
		shakeFriendProfileLayout.setOnClickListener(this);
		
		mHeadImg = (ImageView) findViewById(R.id.friend_head);
		mAgeTxt = (TextView) findViewById(R.id.friend_sex_age);
		mNameTxt = (TextView) findViewById(R.id.friend_name);
		mSignTxt = (TextView) findViewById(R.id.friend_sign);
		mRegionTxt = (TextView) findViewById(R.id.friend_region);
		
		mSlidingDrawer = (SlidingDrawer) findViewById(R.id.sliding_drawer);
		mListView = (ListView) findViewById(R.id.shake_list);
		mListView.setOnItemClickListener(this);
		
		mAddImg = (ImageView) findViewById(R.id.friend_add_img);
		mAddImg.setOnClickListener(this);
		
		mTitleView = findViewById(R.id.title_layout);
		mArrowImg = (ImageView) findViewById(R.id.arrow_img);
		mShakeTxt1 = (TextView) findViewById(R.id.shake_text1);
		mShakeTxt2 = (TextView) findViewById(R.id.shake_text2);
		
		View buttonBack =  findViewById(R.id.back_button);
//		buttonBack.setBackgroundResource(R.drawable.t_home);
		buttonBack.setOnClickListener(this);
		
	 
		
		mShakeAnimImg = (ImageView) findViewById(R.id.shake_anim_img);
		
		mSlidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				mArrowImg.setImageResource(R.drawable.up);
				mTitleView.setVisibility(View.VISIBLE);
				mTitleView.startAnimation(mShowAction);
			}
		});
		mSlidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				mArrowImg.setImageResource(R.drawable.down);
				mTitleView.startAnimation(mHiddenAction);
				mTitleView.setVisibility(View.GONE);
			}
		});
		
		initAnimation();
	 
		isNeedPlaySound = PalmchatApp.getApplication().getSettingMode().isInAppSound();
		mShakeListener = new ShakeListener(getActivity());
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			@Override
			public void onShake() {
				if (isFragStart && !isShaking && !mSlidingDrawer.isOpened()) {
					isShaking = true;
					mHandler.sendEmptyMessageDelayed(RESET_SHAKE_STATE, 3 * 1000);
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SHAKE_NUM);
//					MobclickAgent.onEvent(context, ReadyConfigXML.SHAKE_NUM);
					
					mAfCorePalmchat.AfHttpShakeAndShake("S", 10, "", null, ShakeFragFragment.this);
					
					shakeFriendProfileLayout.setVisibility(View.GONE);
					mShakeTxt2.setVisibility(View.GONE);
					mShakeTxt1.setVisibility(View.VISIBLE);
					mShakeTxt1.setText(R.string.searching);
					mSlidingDrawer.setVisibility(View.GONE);
					
					startAnim(R.anim.shaking);
					
				
					if (!isNeedPlaySound) {
						return;
					} else
						mSoundManager.playSound(R.raw.shake_shake);
				}
			}
		});
		
	}
	private boolean isNeedPlaySound;
	private void initAnimation() {
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,   
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,   
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);   
        mShowAction.setDuration(500);   
        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,   
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,   
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,   
                -1.0f);   
        mHiddenAction.setDuration(300);  
	}
	
	private void showShakingResult() {
		FragmentActivity activity = getActivity();
		if (null != friendInfoList && !friendInfoList.isEmpty() && null != activity) {
			mSlidingDrawer.setVisibility(View.VISIBLE);
			setAdapter(friendInfoList);
			
			shakeFriendProfileLayout.setVisibility(View.VISIBLE);
			viewFriendProfile(friendInfoList.get(0));
			
			stopAnim();
//			mShakeAnimImg.setBackgroundResource(R.drawable.shakeshake05);
//			mShakeAnimImg.setTag(R.id.shake_anim_img, R.drawable.shakeshake05);
			
			shakeFriendProfileLayout.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.shake_out));
			
			mShakeTxt2.setVisibility(View.GONE);
			mShakeTxt1.setVisibility(View.GONE);
		}
	}
	
	private void viewFriendProfile(AfFriendInfo friend) {
		mAddImg.setVisibility(View.GONE);
		shakeFriendProfileLayout.setVisibility(View.VISIBLE);
		if(isNeedPlaySound){
			if (friend.sex == Consts.AFMOBI_SEX_MALE) {
				mSoundManager.playSound(R.raw.shake_match_male);
			} else {
				mSoundManager.playSound(R.raw.shake_match_female);
			}
		}
		
		String name = friend.alias == null ? friend.name : friend.alias;
		if (StringUtil.isNullOrEmpty(name)) {
			name = friend.afId.replace("a", "");
		}
		mNameTxt.setText(name);
		mAgeTxt.setText(friend.age + "");
		if (Consts.AFMOBI_SEX_MALE == friend.sex) {
			mAgeTxt.setBackgroundResource(R.drawable.bg_sexage_male);
			mAgeTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_sexage_boy,0,0,0);
		} else {
			mAgeTxt.setBackgroundResource(R.drawable.bg_sexage_female);
			mAgeTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_sexage_girl,0,0,0);
		}
		
		String sign = getFragString(R.string.default_status);
		mSignTxt.setText(friend.signature == null ? sign : friend.signature);
		
		mRegionTxt.setText(friend.region);
		
		//WXL 20151015 调UIL的显示头像方法,统一图片管理
		ImageManager.getInstance().DisplayAvatarImage( mHeadImg, friend.getServerUrl(),
				friend.afId,Consts.AF_HEAD_MIDDLE,  friend.sex,friend.getSerialFromHead(),null);  
	}
	
	private void setAdapter(List<AfFriendInfo> friendInfoList) {
		if (null == mAdapter) {
			mAdapter = new ShakeAdapter(getActivity(), friendInfoList );
//			mListView.setOnScrollListener(new ImageOnScrollListener(mListView ));
			mListView.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged(friendInfoList);
		}
	}
	
	@Override
	public void onDestroy() {
		stopShakeListener();
		super.onDestroy();
	}
	
	public void stopShakeListener(){
		isFragStart = false;
		if(null != looperThread && null != looperThread.looper){
			looperThread.looper.quit();
		}
		if (null != mShakeListener) {
			mShakeListener.stop();
		}
	}
	
	/**
	 * 单线程队列操作
	 * 
	 * @author 何桂明 2013-10-23
	 * 
	 */
	class LooperThread extends Thread {
		
		// 更新缓存数据
		private static final int SELECT_FRIEND = 7000;

		/**
		 * 线程内部handler
		 */
		Handler handler;

		/**
		 * 线程内部Looper
		 */
		Looper looper;

		@Override
		public void run() {
			Looper.prepare();
			looper = Looper.myLooper();
			// 保持当前只有一条线程在执行查看数据操作
			handler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
					case SELECT_FRIEND:
						Object obj = msg.obj;
						if (null != obj) {
							if (obj instanceof AfFriendInfo[]) {
								AfFriendInfo[] afFriendAry = (AfFriendInfo[]) obj;
								List<AfFriendInfo> list = (List<AfFriendInfo>) Arrays.asList(afFriendAry);
								
								int size = list.size();
								for (int i = 0; i < size; i++) {
									AfFriendInfo friend = list.get(i);
									// 搜索所有好友信息
									AfFriendInfo mFriendInfo = CacheManager.getInstance().findAfFriendInfoByAfId(friend.afId);
									if (null != mFriendInfo) {
										friend.isAddFriend = true;
									}
								}
								Message mainMsg = new Message();
								mainMsg.what = UPDATE_AF_FRIEND;
								mainMsg.obj = list;
								mHandler.sendMessage(mainMsg);
							}
						}
						break;
					default:
						break;
					}
				}
			};
			Looper.loop();
		}
	}
	
	private void showNobody(int code) {
		PalmchatLogUtils.e("---showNobody---", "showNobody");
		mShakeAnimImg.setBackgroundResource(R.drawable.shakeshake01);
		mShakeAnimImg.setTag(R.id.shake_anim_img, R.drawable.shakeshake01);
		
		mShakeTxt1.setVisibility(View.VISIBLE);
		mShakeTxt1.setText(R.string.please_try_again);
		mShakeTxt2.setVisibility(View.VISIBLE);
		if(code==4096){
			mShakeTxt2.setText(R.string.req_failed);
		}else{
			mShakeTxt2.setText(R.string.shake_shake_no_data);
		}
		mSlidingDrawer.setVisibility(View.GONE);
		if (isAdded()) {
			if(isNeedPlaySound){
				mSoundManager.playSound(R.raw.shake_nomatch);
			}
		}
		
	}

	@Override
	public void  AfOnResult(int httpHandle, final int flag, int code, int http_code, Object result, Object user_data){
		if (Consts.REQ_CODE_SUCCESS == code) {
			switch (flag) {
			case Consts.REQ_SHAKE_AND_SHAKE:
				final AfFriendInfo[] friendsInfo = (AfFriendInfo[]) result;
				if (null != friendsInfo && friendsInfo.length != 0) {
					List<AfFriendInfo> list = (List<AfFriendInfo>) Arrays.asList(friendsInfo);
					if(list != null && list.size() > 0){
						Handler handler = looperThread.handler;
						if (null != handler) {
							Message msg = new Message();
							msg.what = LooperThread.SELECT_FRIEND;
							msg.obj = friendsInfo;
							handler.sendMessage(msg);
						}
					} else {
						showNobody(code);
					}
				} else {
					showNobody(code);
				}
				break;
			case Consts.REQ_MSG_SEND:
				if (null != friendInfoList && !friendInfoList.isEmpty()) {
					AfFriendInfo friend = friendInfoList.get(0);
					mAfCorePalmchat.AfHttpFriendOpr("all", friend.afId,
							Consts.HTTP_ACTION_A, Consts.FRIENDS_MAKE,
							(byte) Consts.AFMOBI_FRIEND_TYPE_FF, null, friend,
							ShakeFragFragment.this);
				
					
					
				}
				break;
			case Consts.REQ_FRIEND_LIST:
				
				MessagesUtils.addMsg2Chats(mAfCorePalmchat, ((AfFriendInfo)user_data).afId, MessagesUtils.ADD_CHATS_FRD_REQ_SENT);
				
				if (null != context && !context.isFinishing() && isAdded()) {
				fragmentActivity.dismissProgressDialog();
				ToastManager.getInstance().show(context, getFragString(R.string.add_friend_req));
				mAddImg.setVisibility(View.GONE);
				}
				break;
			default:
				break;
			}
		} else {
			PalmchatLogUtils.e("--AfOnResult--", "code="+ code+"");
			fragmentActivity.dismissProgressDialog();
			switch (flag) {
			case Consts.REQ_SHAKE_AND_SHAKE:
				showNobody(code);
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
//			if(menu != null){
//				menu.showMenu();
//			}
			if(null != getActivity()){
				getActivity().finish();
				stopShakeListener();
			}
			break;
	 
		case R.id.shake_friend_profile_layout:
			if (null != friendInfoList && !friendInfoList.isEmpty()) {
				AfFriendInfo friend = friendInfoList.get(0);
				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SHAKE_T_PF);
//				MobclickAgent.onEvent(context, ReadyConfigXML.SHAKE_T_PF);
				Intent action = new Intent(context, ProfileActivity.class);
				action.putExtra(JsonConstant.KEY_PROFILE, (Serializable) AfFriendInfo.friendToProfile(friend));
				//请求新的profile资料
				action.putExtra(JsonConstant.KEY_FLAG, true);
				action.putExtra(JsonConstant.KEY_AFID, friend.afId);
				action.putExtra(JsonConstant.KEY_USER_MSISDN, friend.user_msisdn);
				startActivity(action);
			}
			break;
		case R.id.friend_add_img:
			if (null != friendInfoList && !friendInfoList.isEmpty()) {
				
				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SHAKE_ADD_SUCC);
//				MobclickAgent.onEvent(context, ReadyConfigXML.SHAKE_ADD_SUCC);
				
				AfFriendInfo friend = friendInfoList.get(0);
				fragmentActivity.showProgressDialog(R.string.Sending);
				MessagesUtils.addStranger2Db(friend);
				mAfCorePalmchat.AfHttpSendMsg(friend.afId, System.currentTimeMillis(),
						getFragString(R.string.want_to_be_friend).replace("{$targetName}",
										CacheManager.getInstance().getMyProfile().name),
								Consts.MSG_CMMD_FRD_REQ, friend, ShakeFragFragment.this);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (null != mAdapter) {
			AfFriendInfo dataItem = mAdapter.getItem(position);
			// heguiming 2013-12-04
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SHAKE_T_PF);
//			MobclickAgent.onEvent(context, ReadyConfigXML.SHAKE_T_PF);
			
			Intent intent = new Intent(context, ProfileActivity.class);
			AfProfileInfo info =AfFriendInfo.friendToProfile(dataItem);
			Log.i(TAG, "AfProfileFriend->" + info);
			intent.putExtra(JsonConstant.KEY_PROFILE, (Serializable) AfFriendInfo.friendToProfile(dataItem));
			//请求新的profile资料
			intent.putExtra(JsonConstant.KEY_FLAG, true);
			intent.putExtra(JsonConstant.KEY_AFID, dataItem.afId);
			intent.putExtra(JsonConstant.KEY_USER_MSISDN, dataItem.user_msisdn);
			startActivity(intent);
		}
	}
	
	
	
}
