package com.afmobi.palmchat.ui.activity.store;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.eventbusmodel.EmoticonDownloadEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ZipFileUtils;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfStoreProdList.AfProdProfile;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
//import com.afmobi.palmchat.ui.activity.main.MainLeftFragment;

public class StoreFaceDownLoadActivity extends BaseActivity implements OnClickListener, AfHttpResultListener, AfHttpProgressListener {

	private final String TAG = StoreFaceDownLoadActivity.class.getCanonicalName();
	/***/
	private TextView mFaceDownloadProgressValue;
	/**进度条有进度时*/
	private TextView mFaceDownloadProgress; 
	/***/
	private TextView mFaceDownloadValue;
	/**表情集名显示view*/
	private TextView mFaceName;
	/**title显示view*/
	private TextView mTitleTextView;
	/**返回按钮*/
	private View back_button;
	/**表情图片显示view*/
	private ImageView mFaceImgView;
	/**进度条区域*/
	private RelativeLayout mLayoutFace;
	/**JNI接口类*/
	private AfPalmchat mAfCorePalmchat;
	/**下载文件保存路径*/
	private String mDownloadFileSavePath ="";
	/**小图全URL*/
	private String small_url ="";
	/**itemId*/
	private String mItemId;
	/**钱数*/
	private int afcoin;
	/**dialog*/
	private AppDialog mAppDialog;
	/**表情集合名称*/
	private String mProfileName;
	/**版本号*/
	private int mVerCode;
	/**表情变化标记*/
	private boolean mFaceChageFlag;
	/**表情内部包名*/
	private String mFaceInnerName;
	

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				//全部完成 
				case 0: {
					Intent intent = (Intent)msg.obj;
					String itemId = intent.getStringExtra("itemId");
					boolean mFaceChageFlag = intent.getBooleanExtra("is_face_change",true);
					//通过EventBus发送进度消息
					EventBus.getDefault().post(new EmoticonDownloadEvent(itemId, 100, true, mFaceChageFlag, JsonConstant.BROADCAST_STORE_FRAGMENT_MARK, 0));
				}
				break;
				
				//zip文件解压完成
				case 1:
					Params params = (Params) msg.obj;
					String myAfid = CacheManager.getInstance().getMyProfile().afId;
					if(!TextUtils.isEmpty(myAfid)) {
						
						Intent intent2 = new Intent();
						intent2.putExtra("itemId", params.itemId);
						intent2.putExtra("is_face_change", params.is_face_change);
						
						if(!TextUtils.isEmpty(params.gifFolderPath)){
							CacheManager.getInstance().getGifImageUtilInstance().putDownLoadFolder(context, params.gifFolderPath, mHandler, intent2);
						}
						if (mAppDialog != null && !mAppDialog.isShowing()) {
							showDialog(getResources().getString(R.string.complete_title), mProfileName + "\r\n" + getResources().getString(R.string.lifespan), Consts.REQ_CODE_SUCCESS);
						}
					}
					break;
						default:
							break;
			}
		};
	};

	@Override
	public void findViews() {
		setContentView(R.layout.activity_store_face_download);
		mFaceDownloadProgressValue = (TextView) findViewById(R.id.tv_face_download_progress_value);
		mFaceDownloadProgress = (TextView) findViewById(R.id.tv_face_download_progress);
		mFaceDownloadValue = (TextView) findViewById(R.id.tv_face_download_value);
		mTitleTextView = (TextView) findViewById(R.id.title_text);
		mFaceName = (TextView) findViewById(R.id.tv_face_name);
		mLayoutFace = (RelativeLayout) findViewById(R.id.r_face);
		mFaceImgView = (ImageView) findViewById(R.id.img_face);
		back_button =  findViewById(R.id.back_button);
		back_button.setOnClickListener(this);

	}

	@Override
	public void init() {
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		Bundle bundle = getIntent().getExtras();
		mItemId = bundle.getString(IntentConstant.ITEM_ID);
		afcoin = bundle.getInt(IntentConstant.AFCOIN);
		String downloadurl = bundle.getString(IntentConstant.DOWNLOADURL);
		small_url = bundle.getString(IntentConstant.SMALLURL);
		mProfileName = bundle.getString(IntentConstant.PROFILE_NAME);
		mVerCode = bundle.getInt(IntentConstant.VER_CODE);
		mFaceChageFlag = bundle.getBoolean(IntentConstant.IS_FACE_CHANGE,true);
		mFaceInnerName = bundle.getString(IntentConstant.FACEINNERNAME);
		
		mTitleTextView.setText(getString(R.string.download));
		PalmchatLogUtils.e("aa", "item_id=" + mItemId + "|downloadurl=" + downloadurl + "|profile_name=" + mProfileName);
		AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
		String afid = myAfProfileInfo.afId;
		
		//获取表情包url
		mDownloadFileSavePath = CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(afid, mItemId);
		mDownloadFileSavePath += mItemId + Constants.STORE_DOWNLOAD_TMP;
		mAppDialog = new AppDialog(context);
		
		downloadFace();
		
		if (downloadurl != null && mProfileName!=null) {
			mFaceName.setText(mProfileName);
			if (downloadurl != null) {
				/*Store ImageInfo imageInfo = new Store ImageInfo(downloadurl);
				ImageLoader.getInstance().displayImage(this.mFaceImgView, imageInfo);*/
				ImageManager.getInstance().DisplayImage(this.mFaceImgView,downloadurl,R.drawable.store_emoji_default,false,null);
			}
		}
		//EventBus注册
		EventBus.getDefault().register(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			StoreFaceDownLoadActivity.this.finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void AfOnProgress(int httpHandle, int flag, int progress, Object user_data) {
		PalmchatLogUtils.println("httpHandle=" + httpHandle + " flag=" + flag + " progress=" + progress + " user_data = " + user_data);
		switch (flag) {
		case Consts.REQ_STORE_DOWNLOAD:
			String itemId = (String)user_data;
			//the same item, same progress, no need to refresh  
			if (CacheManager.getInstance().getStoreDownloadingMap().get(itemId) == progress) {
				return;
			}
			CacheManager.getInstance().getStoreDownloadingMap().put(itemId, progress);
			//通过EventBus发送进度消息
			EventBus.getDefault().post(new EmoticonDownloadEvent(itemId, progress, false, mFaceChageFlag, JsonConstant.BROADCAST_STORE_FRAGMENT_MARK, 0));
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		finish();
	}
  
	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		PalmchatLogUtils.println("------ StoreFaceDownLoadActivity: download AfOnResult code =" + code + "  flag=" + flag + " reuslt=" + result);
		final String item_id = (String)user_data;
		if (code == Consts.REQ_CODE_SUCCESS) {
	
			if (flag == Consts.REQ_STORE_DOWNLOAD) {
				
				if (afcoin > 0) {
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BUY_EM_SUCC);
//					MobclickAgent.onEvent(this, ReadyConfigXML.BUY_EM_SUCC);
					
				} else {
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DL_EM_SUCC);
//					MobclickAgent.onEvent(this, ReadyConfigXML.DL_EM_SUCC);
				}
				
				new Thread() {
					public void run() {
						
						String savePath = new StringBuffer(CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(CacheManager.getInstance().getMyProfile().afId, item_id)).append(item_id).append(Constants.STORE_DOWNLOAD_TMP).toString();
						File tmpfile = new File(savePath);
						
						String gifFolderPath = null;
						if (tmpfile.exists()) {
							String zipPath = new StringBuffer(CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(CacheManager.getInstance().getMyProfile().afId, item_id)).append(item_id).append(Constants.STORE_DOWNLOAD_COMPLETE).toString();
							File zipFile = new File(zipPath);
							tmpfile.renameTo(zipFile);
							
							 gifFolderPath = CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(CacheManager.getInstance().getMyProfile().afId, item_id);
							try {
								if (zipFile.exists()) {
									ZipFileUtils.getInstance().upZipFile(zipFile , gifFolderPath);
									FileUtils.renameTo(gifFolderPath + "/" + CacheManager.getInstance().getScreenString()+ "/");
								}
								
							} catch (ZipException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}finally{
								zipFile.delete();
							}
						}
						
						mAfCorePalmchat.AfDBPaystoreProdinfoRemove(item_id);
						mAfCorePalmchat.AfDBPaystoreProdinfoInsert(item_id, small_url, mFaceName.getText().toString().trim(), "", 100, afcoin, System.currentTimeMillis(),mVerCode,mFaceInnerName);
						
						if(item_id != null && CacheManager.getInstance().getItemid_update().containsKey(item_id)){
							AfProdProfile afProdProfile = CacheManager.getInstance().getItemid_update().get(item_id);
							int mVerCode = afProdProfile.ver_code;
							mAfCorePalmchat.AfDBPaystoreProdinfoUpdate(item_id,mVerCode,System.currentTimeMillis());
						}
						
						Params params = new Params();
						params.itemId = item_id;
						params.gifFolderPath = gifFolderPath;
						params.is_face_change = mFaceChageFlag;
						mHandler.obtainMessage(1, params).sendToTarget();
						
					};
				}.start();
			}
		}else if(code == Consts.REQ_CODE_PAYSTORE_MEMONY_ERR){
			//balance not enough to download
			if (flag == Consts.REQ_STORE_DOWNLOAD_PRE) {
				if (!isFinishing()) {
					
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BUY_EM_RCG);
//					MobclickAgent.onEvent(this, ReadyConfigXML.BUY_EM_RCG);
					
//					Intent intent = new Intent(this, RechargingActivity.class);
//					intent.putExtra(RechargingActivity.RECHARGE_FROM_TAG, RechargingActivity.TAG_FROM_STORE_DETAIL);
//					startActivityForResult(intent, 0);
				}
			}
		} else {
			
			if (afcoin > 0) {
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BUY_EM_FAIL);
//				MobclickAgent.onEvent(this, ReadyConfigXML.BUY_EM_FAIL);
				
			} else {
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DL_EM_FAIL);
//				MobclickAgent.onEvent(this, ReadyConfigXML.DL_EM_FAIL);
			}
			
			if (!isFinishing()) {
				if (mAppDialog != null && !mAppDialog.isShowing()) {
					showDialog(getResources().getString(R.string.download_fail),getResources().getString(R.string.failed_msg),code);
				}
			}
		}
		
		if (CacheManager.getInstance().getStoreDownloadingMap().containsKey(item_id)) {
			CacheManager.getInstance().getStoreDownloadingMap().remove(item_id);
		}

	}
	
	/**
	 * EventBus回调
	 * @param event
	 */
	public void onEventMainThread(EmoticonDownloadEvent event) {

		String itemId = event.getItem_id();
		if (itemId.equals(mItemId)) {
			int progress = event.getProgress();
			mFaceDownloadProgress.setLayoutParams(new RelativeLayout.LayoutParams((int) (progress * ((float) mLayoutFace.getWidth() / 100)), mFaceDownloadProgress.getHeight()));
			mFaceDownloadValue.setText(progress + "%");
			mFaceDownloadProgressValue.setText(progress + "%");
			if (progress == 100) {
				if(CacheManager.getInstance().getItemid_update().containsKey(itemId)){
					CacheManager.getInstance().getItemid_update().remove(itemId);
				}
				if (mAppDialog != null && !mAppDialog.isShowing()) {
					showDialog(getResources().getString(R.string.complete_title),mProfileName+"\r\n"+getResources().getString(R.string.lifespan),Consts.REQ_CODE_SUCCESS);
				}
			}
		}
	
	}
	
	private void showDialog(String title ,String msg ,final int code){
		mAppDialog.createOKDialog(context, title, new OnConfirmButtonDialogListener() {
			
			@Override
			public void onRightButtonClick() {
				switch (code) {
				case Consts.REQ_CODE_PAYSTORE_MEMONY_ERR:
					StoreFaceDownLoadActivity.this.finish();
					break;
				case Consts.REQ_CODE_SUCCESS:
					StoreFaceDownLoadActivity.this.finish();
					break;
				case Consts.REQ_CODE_UNNETWORK:{
					StoreFaceDownLoadActivity.this.finish();
					break;
				}
				default:
					downloadFace();
					break;
				}
			}
			
			@Override
			public void onLeftButtonClick() {
				if (mAppDialog != null && mAppDialog.isShowing()) {
					mAppDialog.cancel();
				}
				
			}
		});
		try {
				mAppDialog.show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 调用jni接口下载表情
	 */
	private void downloadFace() {
		if(!CacheManager.getInstance().getStoreDownloadingMap().containsKey(mItemId)){
			CacheManager.getInstance().getStoreDownloadingMap().put(mItemId, 0);
			mAfCorePalmchat.AfHttpStoreDownload(null, mItemId, CacheManager.getInstance().getScreenType(), mDownloadFileSavePath, false, mItemId, this, this);
		}
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//EventBus反注册
		EventBus.getDefault().unregister(this);
	}
	
	/**
	 * 消息封装类
	 *
	 */
	private class Params {
		String itemId;
		String gifFolderPath;
		boolean is_face_change;
	}
}
