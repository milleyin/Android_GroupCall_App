package com.afmobi.palmchat.ui.activity.social;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.FacebookConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.Consts;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;

import java.util.Arrays;

/**
 * 各个地方的广播都要分享facebook，搞个界面集中处理
 * @author Transsion
 *
 */
public class BroadcastShareToFbActivity extends BaseActivity {

	private final String TAG = BroadcastShareToFbActivity.class.getSimpleName();
	/**回调facebooksdk*/
    private CallbackManager mCallbackManager;
    /**dialog*/
    private ShareDialog mShareDialog;
    /**action组*/
	private enum PendingAction { NONE, POST_PHOTO, POST_MSG,POST_VIDEO}
	/**当前行为*/
	private PendingAction mPendingAction = PendingAction.NONE;
	/**数据*/
	private AfChapterInfo mAfChapterInfo;
	/**web显示*/
	private boolean mIsWebModeShow;
	/**默认show类型*/
	private ShareDialog.Mode mShowMode = ShareDialog.Mode.AUTOMATIC;

	/**刷新token回调*/
	private AccessToken.AccessTokenRefreshCallback mAccessTokenRefreshCallback = new AccessToken.AccessTokenRefreshCallback() {

		@Override
		public void OnTokenRefreshed(AccessToken accessToken) {
			dismissAllDialog();
			//mHandler.removeCallbacks(mTimeOutRunnable);
			initData();
		}

		@Override
		public void OnTokenRefreshFailed(FacebookException exception) {
			dismissAllDialog();
			if(!isFinishing()){
				if(exception.toString().equals("No current access token to refresh")){
					initData();
				} else {
					ToastManager.getInstance().show(BroadcastShareToFbActivity.this, R.string.token_refresh_failed);
					doWithResult(false);
				}
			}
		}

	};
	 /**分享回调*/
    private FacebookCallback<Sharer.Result> mShareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
            doWithResult(false);
        }

        @Override
        public void onError(FacebookException error) {
        	PalmchatLogUtils.i(TAG,"-------wx-----mShareCallback---error-"+error.toString());
        	Message msg = new Message();
        	msg.what = DefaultValueConstant.SHAREFACEBOOK_FAILURE;
        	msg.obj = error;
        	mHandler.sendMessage(msg);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
        	PalmchatLogUtils.i(TAG,"-------wx-----mShareCallback---result-"+result.toString());
			if(TextUtils.isEmpty(result.getPostId())){
				mHandler.sendEmptyMessage(DefaultValueConstant.SHAREFACEBOOK_FAILURE);
			} else {
				mHandler.sendEmptyMessage(DefaultValueConstant.SHAREFACEBOOK_SUCCESS);
			}

        }
    };

    /**fackbook回调*/
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
        	PalmchatLogUtils.i(TAG, "--FacebookCallback---loginResult----"+loginResult.toString());
        	mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_SUCCESS);

        }

        @Override
        public void onCancel() {
        	PalmchatLogUtils.i(TAG, "--FacebookCallback---onCancel----");
			mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_CANCEL);
        }

        @Override
        public void onError(FacebookException exception) {
        	PalmchatLogUtils.i(TAG, "--FacebookCallback---exception----"+exception.toString());
        	mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_FAILURE);
        }
    };

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case DefaultValueConstant.SHAREFACEBOOK_SUCCESS:{
					ToastManager.getInstance().show(BroadcastShareToFbActivity.this, R.string.synchronous_fb_tips_success);
		        	doWithResult(true);
					break;
				}
				case DefaultValueConstant.SHAREFACEBOOK_FAILURE:{
					ToastManager.getInstance().show(BroadcastShareToFbActivity.this, R.string.synchronous_fb_tips);
					doWithResult(false);
					break;
				}
				case DefaultValueConstant.LOGINFACEBOOK_SUCCESS: {
					ToastManager.getInstance().show(BroadcastShareToFbActivity.this, R.string.facebook_login_success_tip);
					handlePendingAction();
					break;
				}
				case DefaultValueConstant.LOGINFACEBOOK_FAILURE: {
					ToastManager.getInstance().show(BroadcastShareToFbActivity.this, R.string.facebook_login_failed_tip);
					doWithResult(false);
					break;
				}
				case DefaultValueConstant.LOGINFACEBOOK_CANCEL: {
					ToastManager.getInstance().show(BroadcastShareToFbActivity.this, R.string.facebook_login_cancel_tip);
		        	doWithResult(false);
					break;
				}
				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mCallbackManager = CallbackManager.Factory.create();
		LoginManager.getInstance().registerCallback(mCallbackManager,mCallback);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		View view = new View(this);
		setContentView(view);
		mShareDialog = new ShareDialog(this);
		mShareDialog.registerCallback(mCallbackManager,mShareCallback);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		if(!NetworkUtils.isNetworkAvailable(PalmchatApp.getApplication())){
			ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.network_unavailable);
			finish();
		}
		showProgressDialog(R.string.please_wait);
		if(null!=dialog){
			dialog.setOnKeyListener(new DialogInterface.OnKeyListener(){

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
						dialog.dismiss();
						BroadcastShareToFbActivity.this.finish();
						return true;
					} else {
						return  false;
					}

				}
			});
		}

		AccessToken.refreshCurrentAccessTokenAsync(mAccessTokenRefreshCallback);

	}

	/**
	 * 数据初始化
	 */
	private void initData(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mAfChapterInfo = (AfChapterInfo)bundle.getSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO);
		mIsWebModeShow = bundle.getBoolean(IntentConstant.ISWEBMODESHOW);
		if(mIsWebModeShow){
			mShowMode = ShareDialog.Mode.WEB;
		}
		if(!NetworkUtils.isNetworkAvailable(PalmchatApp.getApplication())){
			ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.network_unavailable);
			finish();
		}
		if (Consts.BR_TYPE_IMAGE_TEXT == mAfChapterInfo.type || Consts.BR_TYPE_IMAGE == mAfChapterInfo.type) {
			postPhotoMsg();
		} else if (Consts.BR_TYPE_TEXT == mAfChapterInfo.type||Consts.BR_TYPE_VOICE_TEXT == mAfChapterInfo.type ) {
			postMsg();
		}  else if(Consts.BR_TYPE_VIDEO == mAfChapterInfo.type||Consts.BR_TYPE_VIDEO_TEXT == mAfChapterInfo.type) {
			postVideoText();
		} else {
			ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.sharetofb_voice);
			finish();
		}
	}


	/***
	 * 处理返回结果
	 * @param isSuccess
	 */
	private void doWithResult(boolean isSuccess){
		Intent intent = new Intent();
		intent.putExtra(JsonConstant.KEY_SEND_IS_SEND_SUCCESS, isSuccess);
		setResult(RESULT_OK, intent);
		finish();
	}

	/**
	 * 分享图或者图文
	 */
    private void postPhotoMsg() {

    	String msg = "";
    	String path = "";

    	if((null!=mAfChapterInfo)&&(null!=mAfChapterInfo.list_mfile)&&(null!=mAfChapterInfo.list_mfile.get(0))&&(
				null!=mAfChapterInfo.list_mfile.get(0).url)){
    		msg = mAfChapterInfo.content;
    		path = mAfChapterInfo.list_mfile.get(0).url.toString();
		} else {
			ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.data_is_null);
			doWithResult(false);
		}


        mPendingAction = PendingAction.POST_PHOTO;
        if (!CommonUtils.hasPublishPermission()) {
        	LoginManager.getInstance().logInWithPublishPermissions(
                    this,Arrays.asList(FacebookConstant.PUBLISH_ACTIONS));
        } else {
        	mPendingAction = PendingAction.NONE;
            PalmchatLogUtils.i(TAG,"----postPhotoAndMsg----isFacebookShareClose is open");
            if (!CommonUtils.isEmpty(path)) {
            	if(!(path.startsWith(JsonConstant.HTTP_HEAD)||path.startsWith(JsonConstant.HTTPS_HEAD))){
            		String [] serverInfo=((PalmchatApp)  getApplication()).mAfCorePalmchat.AfHttpGetServerInfo();
            		if((null!=serverInfo)&&(serverInfo.length>=5)){
            			path =serverInfo[4] +path;
            		}
            	}
            } else {
            	ToastManager.getInstance().show(BroadcastShareToFbActivity.this, R.string.synchronous_fb_url_empty);
            }
            postMsgAndPhoto(path,msg);
        }
    }

	/**
     * 发送纯文字消息
     */
    private void postMsg( ) {
    	String msg = "";
    	if((null!=mAfChapterInfo)&&(null!=mAfChapterInfo.content)){
    		msg = mAfChapterInfo.content;
		} else {
			ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.data_is_null);
			doWithResult(false);
		}

        mPendingAction = PendingAction.POST_MSG;
        if (!CommonUtils.hasPublishPermission()) {
        	// We need to get new permissions, then complete the action when we get called back.
        	LoginManager.getInstance().logInWithPublishPermissions(
                    this,Arrays.asList(FacebookConstant.PUBLISH_ACTIONS));
        } else {
        	mPendingAction = PendingAction.NONE;
        	ShareOpenGraphObject graphObject  = new ShareOpenGraphObject.Builder()
            		.putString("og:type", FacebookConstant.OG_OBJECT_TYPE)
            		.putString("og:title", FacebookConstant.SHARETITLE)
            		.putString("og:description", msg)
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
            mShareDialog.show(content,mShowMode);
        }
    }

	/**
	 * 发送图文消息
	 * @param path
	 * @param msg
     */
    private void postMsgAndPhoto(String path, String msg){
    	if(null==msg){
    		msg = "";
    	}
    	SharePhoto.Builder sharePhotoBuilder = new SharePhoto.Builder();
    	Uri uri = Uri.parse(path);
    	sharePhotoBuilder.setImageUrl(uri);
    	SharePhoto gesturePhoto = sharePhotoBuilder.build();
    	ShareOpenGraphObject graphObject = createGraphObject(gesturePhoto,msg);
    	ShareOpenGraphAction playAction = createPlayActionWithGame(graphObject);
		ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
		         .setAction(playAction)
		         .setPreviewPropertyName(FacebookConstant.OG_OBJECTCONTENT)
		         .build();
		mShareDialog.show(content,mShowMode);
    }

	private void postVideoText( ){
		//预览图
		String previewPhoto = "";
		//视频地址
		String videoUrl = "";
		String msg = "";
		if((null!=mAfChapterInfo)&&(null!=mAfChapterInfo.list_mfile)&&(null!=mAfChapterInfo.list_mfile.get(0))&&(null!=mAfChapterInfo.list_mfile.get(0).url)
				&&(null!=mAfChapterInfo.list_mfile.get(0).thumb_url)){
			previewPhoto = mAfChapterInfo.list_mfile.get(0).thumb_url;
			videoUrl = mAfChapterInfo.list_mfile.get(0).url;
			if(mAfChapterInfo.content!=null){
				msg = mAfChapterInfo.content;
			}
		} else {
			ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.data_is_null);
			doWithResult(false);
		}
		mPendingAction = PendingAction.POST_VIDEO;
		if (!CommonUtils.hasPublishPermission()) {
			LoginManager.getInstance().logInWithPublishPermissions(
					this,Arrays.asList(FacebookConstant.PUBLISH_ACTIONS));
		} else {
			mPendingAction = PendingAction.NONE;
			Uri videouri = Uri.parse(videoUrl);
			SharePhoto.Builder sharePhotoBuilder = new SharePhoto.Builder();
			Uri imgUri = Uri.parse(previewPhoto);
			sharePhotoBuilder.setImageUrl(imgUri);
			SharePhoto sharePhoto = sharePhotoBuilder.build();

			ShareOpenGraphObject graphObject  = new ShareOpenGraphObject.Builder()
					.putString("og:type", FacebookConstant.OG_OBJECT_TYPE)
					.putString("og:title", FacebookConstant.SHARETITLE)
					.putString("og:description", msg)
					.putPhoto("og:image",sharePhoto)
					.build();

			ShareOpenGraphAction playAction = new ShareOpenGraphAction.Builder()
					.putString("og:type", FacebookConstant.OG_ACTION_TYPE)
					.putBoolean("fb:explicitly_shared", true)
					.putObject(FacebookConstant.OG_OBJECTCONTENT, graphObject)
					.build();
			ShareLinkContent shareLinkContent = new ShareLinkContent.Builder().setContentUrl(videouri).setContentDescription(msg).setImageUrl(imgUri).setContentTitle(FacebookConstant.SHARETITLE).build();
//			ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
//					.setAction(playAction)
//					.setPreviewPropertyName(FacebookConstant.OG_OBJECTCONTENT)
//					.setContentUrl(videouri)
//					.build();
			mShareDialog.show(shareLinkContent,mShowMode);
		}

	}

    /**
     * 图文中创建graph对象
	 * @param gesturePhoto
	 * @param msg
	 * @return
	 */
    private ShareOpenGraphObject createGraphObject(SharePhoto gesturePhoto,String msg) {
        return new ShareOpenGraphObject.Builder()
        		.putString("og:type", FacebookConstant.OG_OBJECT_TYPE)
        		.putString("og:title", FacebookConstant.SHARETITLE)
        		.putString("og:description", msg)
        		.putPhoto("og:image", gesturePhoto)
                .build();
    }

    /**
     * 创建action对象
     * @param game
     * @return
     */
    private ShareOpenGraphAction createPlayActionWithGame(ShareOpenGraphObject game) {
        return new ShareOpenGraphAction.Builder()
        		.putString("og:type", FacebookConstant.OG_ACTION_TYPE)
        		.putBoolean("fb:explicitly_shared", true)
                .putObject(FacebookConstant.OG_OBJECTCONTENT, game)
                .build();
    }

    /**
     * 有权限后继续分享
     */
    private void handlePendingAction(){
    	switch (mPendingAction) {
			case NONE:{
				break;
			}
			case POST_PHOTO:{
				postPhotoMsg( );
				break;
			}
			case POST_MSG:{
				postMsg( );
				break;
			}
			case POST_VIDEO:{
				postVideoText();
				break;
			}
		default:
			break;
		}
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!NetworkUtils.isNetworkAvailable(PalmchatApp.getApplication())){
			ToastManager.getInstance().show(PalmchatApp.getApplication(), R.string.network_unavailable);
			finish();
		}
	}

			
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		mCallbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override


	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
