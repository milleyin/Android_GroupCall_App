package com.afmobi.palmchat.ui.activity.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.db.SharePreferenceService;
import com.afmobi.palmchat.eventbusmodel.UploadHeadEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.chats.PreviewImageActivity;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.BitmapUtils;
import com.afmobi.palmchat.util.ClippingPicture;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.UploadPictureUtils;
import com.afmobi.palmchat.util.UploadPictureUtils.IUploadHead;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfDatingInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfRespAvatarInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.io.File;
import java.util.ArrayList;

/**
 * 注册完成后 profile完成页面
 * 
 * @author zhh
 * 
 */
public class ComfireProfileActivity extends BaseActivity implements OnClickListener, AfHttpResultListener, AfHttpProgressListener {
	private TextView tv_skip;
	private ViewGroup rl_photo ,rl_inviter;
	private ImageView img_photo;
	private ImageView img_back;
	private EditText et_inviter;
	private Button btn_complete;
	private View ll_main;
	private File fCurrentFile; // 调用系统拍照时照片保存文件
	private String sCameraFilename;// 调用系统拍照时照片文件名
	private String path, filename;
	private AfPalmchat mAfPalmchat;
	public AfProfileInfo afProfileInfo;
	private static String TAG = ComfireProfileActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_CPF);
		fCurrentFile = CacheManager.getInstance().getCurFile();
	}

	@Override
	public void findViews() {
		setContentView(R.layout.comfire_profile);
		ll_main = findViewById(R.id.ll_main);
		tv_skip = (TextView) findViewById(R.id.tv_right);
		tv_skip.setVisibility(View.VISIBLE);
		tv_skip.setText(getString(R.string.skip));
		rl_photo = (ViewGroup) findViewById(R.id.rl_photo);

		img_photo = (ImageView) findViewById(R.id.img_photo);
		img_back = (ImageView) findViewById(R.id.img_left);
		
		rl_inviter= (ViewGroup) findViewById(R.id.rl_inviter);
		et_inviter=(EditText) findViewById(R.id.tv_inviter_right);
		btn_complete = (Button) findViewById(R.id.btn_complete);
		img_back.setVisibility(View.GONE);
		
		tv_skip.setOnClickListener(this);
		rl_photo.setOnClickListener(this);
		btn_complete.setOnClickListener(this);

	}

	@Override
	public void init() {
		mAfPalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		afProfileInfo = AppUtils.createProfileInfoFromCacheValue();
		img_photo.setImageResource(afProfileInfo.sex == Consts.AFMOBI_SEX_FEMALE ? R.drawable.head_female2 : R.drawable.head_male2);
		if(afProfileInfo.is_bind_phone){//只有手机注册的时候才显示推荐人入口
			rl_inviter.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		/* add by zhh 魅族、HTC等手机状态栏适配 */
		CommonUtils.hideStatus(this, ll_main);

	}

	 
	/**
	 * 头像对话框
	 */
	public void alertMenu() {
		createLocalFile();
		Intent mIntent = new Intent(this, PreviewImageActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putInt("size", 1);
		mIntent.putExtras(mBundle);
		startActivityForResult(mIntent, MessagesUtils.PICTURE);
	}

	private void createLocalFile() {
		sCameraFilename = ClippingPicture.getCurrentFilename();
		PalmchatLogUtils.e("camera->", sCameraFilename);
		SharePreferenceService.getInstance(this).savaFilename(sCameraFilename);
		fCurrentFile = new File(RequestConstant.CAMERA_CACHE, sCameraFilename);
		CacheManager.getInstance().setCurFile(fCurrentFile);
	}

	/**
	 * 更换头像
	 * 
	 * @param
	 */
	public void changeHeadPortrait(String path, String filename) {
		this.path = path;
		this.filename = filename;
		if (TextUtils.isEmpty(path)) {
			AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
			profileInfo.head_img_path = path;
			// 调UIL的显示头像方法
			ImageManager.getInstance().DisplayAvatarImage(img_photo, profileInfo.getServerUrl(), profileInfo.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, profileInfo.sex, profileInfo.getSerialFromHead(), null);

		} else {
			/*Bitmap bitmap =ImageManager.getInstance().loadLocalImageSync(path,false);// ImageUtil.getBitmapFromFile(path);
			img_photo.setImageBitmap(ImageUtil.toRoundCorner(bitmap));*/
			ImageManager.getInstance().displayLocalImage_circle(img_photo,path ,0,0,null);
		}
	}

	private void copyHeadImg( final Object result) {
				dismissProgressDialog();
				String path = fCurrentFile.getAbsolutePath();
				AfRespAvatarInfo info = (AfRespAvatarInfo) (result);
				if (info != null) { 
					AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
					if (profileInfo != null) {
						String pathDestination = RequestConstant.IMAGE_UIL_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(CommonUtils.getAvatarUrlKey(profileInfo.getServerUrl(), info.afid, info.serial, Consts.AF_HEAD_MIDDLE));
						File outFile = new File(pathDestination);
						FileUtils.copyToImg(path, outFile.getAbsolutePath()) ;
						profileInfo.head_img_path = info.afid + "," + info.serial + "," + info.host;// 更新头像字段
						mAfPalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, profileInfo);
						
						if (fCurrentFile != null) {
							fCurrentFile.delete();
						} else {
							ToastManager.getInstance().show(ComfireProfileActivity.this, R.string.fail_to_update);
						}
					}
				}
	}
	private boolean isProfileUpdateSuccess;//资料是否已经上传成功了
	private void commitData() {
		showProgressDialog(R.string.please_wait);
		if (isProfileUpdateSuccess) {//Profile上传完毕了  上传头像
			if (!TextUtils.isEmpty(path) && !TextUtils.isEmpty(filename)) {
				mAfPalmchat.AfHttpHeadUpload(path, filename, Consts.AF_AVATAR_FORMAT, null, this, this);
			}
		} else {//如果Profile有变化 那就 上传Profile
			if( et_inviter.getText()!=null&&!TextUtils.isEmpty(et_inviter.getText().toString())){//需要修改资料的
				afProfileInfo.recommenderID = et_inviter.getText().toString();
				mAfPalmchat.AfHttpUpdateInfo(afProfileInfo, null, this); 
			}else if (!TextUtils.isEmpty(path) && !TextUtils.isEmpty(filename)) { //   如果有头像  再上传 头像
				mAfPalmchat.AfHttpHeadUpload(path, filename, Consts.AF_AVATAR_FORMAT, null, this, this);
			}
			else {//如果什么都没有 那直接进主页
				toMainTab();
			}
			 
		}
		
	}

	/**
	 * 5.1.3改为直接进主页面
	 */
	void toMainTab() {
		Intent intent = new Intent(this, MainTab.class);
		intent.putExtra(JsonConstant.KEY_IS_LOGIN, true);
		intent.putExtra(JsonConstant.KEY_FLAG, true);
		intent.putExtra(JsonConstant.KEY_LOGIN_TYPE, Consts.AF_LOGIN_AFID);
		startActivity(intent);
		finish();
	}

	private void onBack() {
		/* 回到系统主页面 */
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // //FLAG_ACTIVITY_NEW_TASK
		startActivity(startMain);
	}


	/**
	 * 创建照片本地保存文件
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right: // 跳过 
			toMainTab();
			break;
		case R.id.img_left: // 回退按钮
			onBack();
			break;
		case R.id.rl_photo: // 头像
			alertMenu();
			break;
		case R.id.btn_complete: // 完成
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SET_MPF);
			commitData(); 
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case MessagesUtils.PICTURE:// 相册
				if (data != null) {// && resultCode == Activity.RESULT_OK) {
					if (null != fCurrentFile) {
						Bitmap bm = null;
						ArrayList<String> arrAddPiclist = data.getStringArrayListExtra("photoLs");
						String cameraFilename = data.getStringExtra("cameraFilename");
						String path = null;
						if (arrAddPiclist != null && arrAddPiclist.size() > 0) {
							path = arrAddPiclist.get(0);
						} else if (cameraFilename != null) {
							path = RequestConstant.CAMERA_CACHE + cameraFilename;
						}
						if (path != null) {
							String largeFilename = Uri.decode(path);
							File f = new File(largeFilename);
							File f2 = FileUtils.copyToImg(largeFilename);// copy到palmchat/camera
							if (f2 != null) {
								f = f2;
							}
							cameraFilename = f2.getAbsolutePath();// f.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
							cameraFilename = RequestConstant.IMAGE_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
							cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
							if (CommonUtils.isEmpty(cameraFilename)) {
								return;
							}
							bm = ImageManager.getInstance().loadLocalImageSync(cameraFilename,false);//ImageUtil.getBitmapFromFile(cameraFilename, true);
							UploadPictureUtils.getInit().showClipPhoto(bm, rl_photo, this, sCameraFilename, new IUploadHead() {

								@Override
								public void onUploadHead(String path, String filename) {
									changeHeadPortrait(path, filename);
								}

								@Override
								public void onCancelUpload() {

								}
							});
						}
					}
				}
				break;
		}

	}

	@Override
	public void AfOnProgress(int httpHandle, int flag, int progress, Object user_data) {

	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		if (code == Consts.REQ_CODE_SUCCESS) {
			if (flag == Consts.REQ_UPDATE_INFO) {// 这里只上传邀请人信息
				// update profile
				isProfileUpdateSuccess=true;
				//上传资料成功后   如果有头像  再上传 头像
				if (!TextUtils.isEmpty(path) && !TextUtils.isEmpty(filename)) {
					mAfPalmchat.AfHttpHeadUpload(path, filename, Consts.AF_AVATAR_FORMAT, null, this, this);
				}else{//否则就进主页 
					toMainTab();
				}

			} else if (flag == Consts.REQ_AVATAR_UPLOAD) { // 上传头像
				// 头像上传成功
				PalmchatLogUtils.i(TAG, "avatar upload success!!!" + user_data + "->result = " + result);
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SET_H_SUCC);
				// 将图片copy到 avatar目录下
				copyHeadImg(result);
				toMainTab();
			}
		} else {
			dismissProgressDialog();
			if (flag == Consts.REQ_AVATAR_UPLOAD) { // 上传头像失败
				ToastManager.getInstance().show(this, R.string.fail_to_update);
			} else if (flag == Consts.REQ_UPDATE_INFO) {// 上传其余资料（除头像、恋爱状态外）失败
				if (code == Consts.REQ_CODE_ILLEGAL_WORD) { // zhh相关语句中含有非法词汇
					// 此处需调用系统的，不能用应用中ToastManager去显示，为了确保该页面关闭时toast还能正常显示，因为toastmanager做了context为空判断
					Toast.makeText(this, getString(R.string.inappropriate_word), Toast.LENGTH_LONG).show();
				}else{
					ToastManager.getInstance().show(this, R.string.fail_to_update);
				}
			}

		}
	}

	@Override
	public void onBackPressed() {
		onBack();
	}

}
