package com.afmobi.palmchat.ui.activity.publicaccounts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.FacebookConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.eventbusmodel.EventFollowNotice;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.main.model.DialogItem;
import com.afmobi.palmchat.ui.customview.ListDialog;
import com.afmobi.palmchat.ui.customview.ListDialog.OnItemClick;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfFriendInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * Public Account Detail
 *
 * 
 */
public class PublicAccountDetailsActivity extends BaseActivity implements
		OnClickListener, AfHttpResultListener,ListDialog.OnItemClick  {
	
	private static final String TAG = PublicAccountDetailsActivity.class.getSimpleName();
	/**公共账号信息*/
	private AfFriendInfo mCurAfFriendInfo;
	/**头像*/
	private ImageView mHeadImg;
	/**公共账号名*/
	private TextView mNameTxt;
	/**公共账号ID*/
	private TextView mIdTxt;
	/**关注人数*/
	private TextView mNumTxt;
	/**关注按钮*/
	private Button mBottomFollowBtn;
	/**聊天历史*/
	private View mV_HistoryLayout;
	/**广播历史*/
	private View mV_BroadHistory;
	/**中间件接口*/
	private AfPalmchat mAfPalmchat;
	/**详情*/
	private TextView mTv_Intro;
	/**follow状态*/
	private boolean mBl_IsFollowed;
	 /**回调facebooksdk*/
    private CallbackManager mCallbackManager;
    /**fackbook回调*/
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
        	mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_SUCCESS);
        }

        @Override
        public void onCancel() {
        	PalmchatLogUtils.i(TAG, "--FacebookCallback---onCancel----");
			mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_CANCEL);
        }

        @Override
        public void onError(FacebookException exception) {
        	mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_FAILURE);
        }
    };
    /**分享回调*/
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
        	dismissAllDialog(); 
            Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
        	dismissAllDialog(); 
        	PalmchatLogUtils.e(TAG,error.toString());
			ToastManager.getInstance().show(PublicAccountDetailsActivity.this, R.string.sharing_failure);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
        	dismissAllDialog(); 
        	ToastManager.getInstance().show(PublicAccountDetailsActivity.this, R.string.sharing_success);
        }
    };
	/**handler*/
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case DefaultValueConstant.LOGINFACEBOOK_SUCCESS:{
					//ToastManager.getInstance().show(ActivityPublicAccountDetails.this, R.string.success);
					PalmchatLogUtils.i(TAG,getString(R.string.success));
					postLink();
					break;
				}
				case DefaultValueConstant.LOGINFACEBOOK_FAILURE: {
					dismissAllDialog();
					PalmchatLogUtils.i(TAG,getString(R.string.sharing_failure));
					ToastManager.getInstance().show(PublicAccountDetailsActivity.this, R.string.sharing_failure);
					break;
				}
				
				case DefaultValueConstant.LOGINFACEBOOK_CANCEL: {
					ToastManager.getInstance().show(PublicAccountDetailsActivity.this, R.string.facebook_login_cancel_tip);
					break;
				}
				default:
					break;
			}
		}
	};

    
    private String getShareMessage() {
    	String msg = getString(R.string.pb_share_fb);
		return msg.replaceAll("XXXX", mCurAfFriendInfo.name);
    }

	@Override
	public void findViews() {
		setContentView(R.layout.activity_public_account_details);

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mCallbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void init() {
		mAfPalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
		
		mCurAfFriendInfo = (AfFriendInfo) getIntent().getExtras().get("Info");
		
		TextView mTitleTxt = (TextView) findViewById(R.id.title_text);
		mTitleTxt.setText(R.string.details);		
		mV_BroadHistory = findViewById(R.id.accountdetails_broadhistory_layout);
		mV_BroadHistory.setOnClickListener(this);
		
		findViewById(R.id.back_button).setOnClickListener(this);
		mTv_Intro = (TextView)findViewById(R.id.accountdetails_intro);
		mHeadImg = (ImageView) findViewById(R.id.accountdetails_head_img);
		mNameTxt = (TextView) findViewById(R.id.accountdetails_name);
		mIdTxt = (TextView) findViewById(R.id.accountdetails_id);
		mNumTxt = (TextView) findViewById(R.id.accountdetails_like_count);
		mBottomFollowBtn = (Button) findViewById(R.id.accountdetails_follow);
		mBottomFollowBtn.setOnClickListener(this);
		
		mV_HistoryLayout = findViewById(R.id.accountdetails_viewmessage_layout);
		mV_HistoryLayout.setOnClickListener(this);
		loadInfoData();
		
		ImageView vImageViewRight = (ImageView) findViewById(R.id.op2);
		boolean mBl_IsFbExist = CommonUtils.isAppExist(FacebookConstant.FACEBOOKPACKAGE);
		if (mBl_IsFbExist) {
			vImageViewRight.setVisibility(View.VISIBLE);
		}
		vImageViewRight.setBackgroundResource(R.drawable.navigation);
		
		vImageViewRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showItemDialog(context);
			}
		});
		
		checkFollow(false);
		
	}

	/**
	 * 对话框
	 * @param c
     */
	private void showItemDialog(final Context c) {
		List<DialogItem> items = new ArrayList<DialogItem>();
		items.add(new DialogItem(R.string.share_to_facebook, R.layout.custom_dialog_image, 0, 0, R.drawable.icon_sheet_share_to_facebook, null));
		ListDialog dialog = new ListDialog(c, items);
		dialog.setItemClick(this);
		dialog.show();
	}

	@Override
	public void onItemClick(DialogItem item) {
		switch (item.getTextId()) {
			case R.string.share_to_facebook:

				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SHA_PBL_FB);
				PalmchatLogUtils.i("tagpublic","test");
				if (CommonUtils.hasPublishPermission()) {
					postLink();
				} else {
					try {
						LoginManager.getInstance().logOut();
					} catch (Exception e) {
						// TODO: handle exception
					}

					new Thread(new Runnable() {
						@Override
						public void run() {
							LoginManager.getInstance().logInWithPublishPermissions(
									PublicAccountDetailsActivity.this,
									Arrays.asList(FacebookConstant.PUBLISH_ACTIONS));
						}
					}).start();
				}

				break;

			default:
				break;
		}
	}
	
	private void showItemDialog() {
		List<DialogItem> items = new ArrayList<DialogItem>();
		items.add(new DialogItem(R.string.share_to_facebook, R.layout.custom_dialog_normal));
		items.add(new DialogItem(R.string.cancel, R.layout.custom_dialog_cancel));
		ListDialog dialog = new ListDialog(this, items);
		dialog.setItemClick(new OnItemClick() {
			
			@Override
			public void onItemClick(DialogItem item) {
				// TODO Auto-generated method stub
				
				switch (item.getTextId()) {
				case R.string.share_to_facebook:
					
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SHA_PBL_FB);
					if (CommonUtils.hasPublishPermission()) {
						postLink();
					} else {
						try {
							LoginManager.getInstance().logOut();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

						new Thread(new Runnable() {  
							@Override  
							public void run() {  
					            LoginManager.getInstance().logInWithPublishPermissions(
					            		PublicAccountDetailsActivity.this,
					                    Arrays.asList(FacebookConstant.PUBLISH_ACTIONS));
							}  
						}).start();
					}
					break;
				default:
					break;
				}
			}
		});
		dialog.show();
	}

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,mCallback);
	};
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AppEventsLogger.deactivateApp(this);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * 发送链接
	 */
	private void postLink() {
		String message = getShareMessage();
		postMsg(message);
	}

	/**
	 * check if current account is followed
	 * @return
	 */
	private boolean checkFollow(boolean isReSet) {
		AfFriendInfo resInfo = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).search(mCurAfFriendInfo, false, true);
	
		if(isReSet){
			boolean isFollowed=false;
			if(resInfo != null){
				isFollowed = true;
			}
			EventBus.getDefault().post(new EventFollowNotice(mCurAfFriendInfo.afId));
		}
		
		if (resInfo != null) {
			mBottomFollowBtn.setText(R.string.unfollow);
			mBl_IsFollowed = true;
			mBottomFollowBtn.setTextColor(getResources().getColor(R.color.broadcast_trending_17a5ef));
			mBottomFollowBtn.setBackgroundResource(R.drawable.btn_predict_win_bg_selector);
			return true;
		
		} else {
			mBottomFollowBtn.setText(R.string.follow);
			mBl_IsFollowed = false;
			mBottomFollowBtn.setTextColor(getResources().getColor(R.color.color_white));
			mBottomFollowBtn.setBackgroundResource(R.drawable.login_button_selector);
			return false;			
		}
	}

	/**
	 * 初始化信息
	 */
	private void loadInfoData() {
		if (null != mCurAfFriendInfo) {
			mNameTxt.setText(mCurAfFriendInfo.name);
			String afid = mCurAfFriendInfo.afId;
			mIdTxt.setText(afid.replace("r", ""));
			mAfPalmchat.AfHttpGetInfo(new String[] { afid }, Consts.REQ_GET_INFO, null, null, PublicAccountDetailsActivity.this);
			//wxl 20151012调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
			ImageManager.getInstance().DisplayAvatarImage(mHeadImg, mCurAfFriendInfo.getServerUrl(),
					mCurAfFriendInfo.getAfidFromHead(),Consts.AF_HEAD_MIDDLE,mCurAfFriendInfo.sex, mCurAfFriendInfo.getSerialFromHead(),null);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			closeActivity();
			break;
			
		//to chat
		case R.id.accountdetails_viewmessage_layout:
			if (null != mCurAfFriendInfo) {
				toChatting(mCurAfFriendInfo, mCurAfFriendInfo.afId, mCurAfFriendInfo.name);
			}
			break;
		case R.id.accountdetails_broadhistory_layout:{
			if (null != mCurAfFriendInfo) {
				toBroadcastHistory(mCurAfFriendInfo, mCurAfFriendInfo.afId, mCurAfFriendInfo.name);
			}
			break;
		}

		//follow/unfollow 按钮
		case R.id.accountdetails_follow:
			if (checkFollow(false)) {
				unFollow();
			} else {
				follow();
			}
			break;
		default:
			break; 
		}
	}

	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		closeActivity();
	}

	/**
	 * 跳转到公共账号聊天界面
	 * @param infos
	 * @param afid
	 * @param name
     */
	private void toChatting(AfFriendInfo infos, String afid, String name) {
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_P_PBL);
		Intent intent = new Intent(context, AccountsChattingActivity.class);
		intent.putExtra(JsonConstant.VIEW_HISTORY, true);
		intent.putExtra(JsonConstant.KEY_FROM_UUID, afid);
		intent.putExtra(JsonConstant.KEY_FROM_NAME, name);
		intent.putExtra(JsonConstant.KEY_FROM_ALIAS, infos.alias);
		intent.putExtra(JsonConstant.KEY_FRIEND, infos);
		context.startActivity(intent);
	}
	
	/**
	 * 跳转到公共账号广播历史
	 * @param info
	 * @param afid
	 * @param name
	 */
	private void toBroadcastHistory(AfFriendInfo info, String afid, String name) {
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_P_PBL);
		if(TextUtils.isEmpty(afid)){
			ToastManager.getInstance().show(PalmchatApp.getApplication(), getString(R.string.broadcast_publicaccount_accountid_limit));
			return;
		}
		Intent intent = new Intent(context, PublicAccountsHistoryActivity.class);
		intent.putExtra(IntentConstant.PROFILE, info);
		context.startActivity(intent);
	}

	/**
	 * 清理聊天消息
	 * @param mAfCorePalmchat
	 * @param afId
     */
	private void clearPrivateChatHistory(final AfPalmchat mAfCorePalmchat, final String afId) {
		new Thread(new Runnable() {
			public void run() {
				final AfMessageInfo[] recentDataArray = mAfCorePalmchat
						.AfDbRecentMsgGetRecord(
								AfMessageInfo.MESSAGE_TYPE_MASK_PRIV,
								afId, 0, Integer.MAX_VALUE);
				if (null != recentDataArray && recentDataArray.length > 0) {
					for (AfMessageInfo messageInfo : recentDataArray) {
						mAfCorePalmchat.AfDbMsgRmove(messageInfo);
						MessagesUtils.removeMsg(messageInfo, true, true);
					}
					mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, afId);
				}
			}
		}).start();
	}

	/**
	 * 关闭窗口
	 */
	private void closeActivity(){
		Intent intent =new Intent();
		intent.putExtra(DefaultValueConstant.ISFOLLOWED,mBl_IsFollowed);
		setResult(RESULT_OK,intent);
		finish();
	}

	/**
	 * follow操作
	 */
	private void follow() {
		if (null != mCurAfFriendInfo) {
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.FOL_PBL);
			showProgressDialog(R.string.following);
			mAfPalmchat.AfHttpFriendOpr(null, mCurAfFriendInfo.afId, Consts.HTTP_ACTION_A, Consts.FRIENDS_PUBLIC_ACCOUNT,
					(byte) Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT, null, Consts.HTTP_ACTION_A, PublicAccountDetailsActivity.this);
		}
	}

	/**
	 * 取消关注
	 */
	private void unFollow() {
		if (null != mCurAfFriendInfo) {
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.UNFOL_PBL);
			showProgressDialog(R.string.unfollowing);
			mAfPalmchat.AfHttpFriendOpr(null, mCurAfFriendInfo.afId, Consts.HTTP_ACTION_D, Consts.FRIENDS_PUBLIC_ACCOUNT,
					(byte) Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT, null, Consts.HTTP_ACTION_D, PublicAccountDetailsActivity.this);
		}
	}

	@Override
	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){		
		PalmchatLogUtils.e("TAG", "----AfOnResult--Flag:" + flag + "--Code:" + code);
		dismissAllDialog();
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			case Consts.REQ_FRIEND_LIST:
				
				byte action = (Byte) user_data;
				
				if (action == Consts.HTTP_ACTION_A) {
				
				mCurAfFriendInfo.type = Consts.AFMOBI_FRIEND_TYPE_PFF;
				CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).saveOrUpdate(mCurAfFriendInfo, true, true);
					ToastManager.getInstance().show(this,getString(R.string.public_follow_success));
				
				} else if (action == Consts.HTTP_ACTION_D) {
					ToastManager.getInstance().show(this,getString(R.string.public_unfollow_success));
					CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).remove(mCurAfFriendInfo, true, true);
					clearPrivateChatHistory(mAfPalmchat, mCurAfFriendInfo.afId);
				}
				checkFollow(true);
				dismissAllDialog();
				break;
				
			case Consts.REQ_GET_INFO:

				if (result != null && result instanceof AfProfileInfo) {
					AfProfileInfo afInfo = (AfProfileInfo) result;
					updateProfile(afInfo);
				}
				break;
			}
		} else {
			
			switch (flag) {
			case Consts.REQ_FRIEND_LIST:
				dismissAllDialog();
				ToastManager.getInstance().show(this, getString(R.string.req_failed));
				break;
				
			case Consts.REQ_GET_INFO:
				
				break;
				
			}
			
		}
	}

	/**
	 * 更新profile
	 * @param afInfo
     */
	private void updateProfile(AfProfileInfo afInfo) {
		mCurAfFriendInfo = afInfo;
		mNameTxt.setText(afInfo.name);
		String afid = afInfo.afId;
		mIdTxt.setText(afid.replace("r", ""));
		mNumTxt.setText(afInfo.imsi);
		mTv_Intro.setText(afInfo.hobby);
	}

    /**
	 * 分享到facebook
	 * @param msg
	 */
    private void postMsg(String msg) {
        
        ShareOpenGraphObject graphObject  = new ShareOpenGraphObject.Builder()
        		.putString("og:type", FacebookConstant.OG_OBJECT_TYPE)
        		.putString("og:title", "Palmchat - Free chat, Meet new friends and Fun!")
                .build();
        
        ShareOpenGraphAction playAction = new ShareOpenGraphAction.Builder()
        		.putString("og:type", FacebookConstant.OG_ACTION_TYPE)
        		.putBoolean("fb:explicitly_shared", true)
        		.putObject(FacebookConstant.OG_OBJECTCONTENT, graphObject)
                .build();
        ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
		         .setAction(playAction)
		         .setPreviewPropertyName(FacebookConstant.OG_OBJECTCONTENT)
		         .build();
        ShareApi.share(content,shareCallback,msg);
    }
}
