package com.afmobi.palmchat.ui.activity.social;

  
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.eventbusmodel.ModifyPictureListEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.chats.PreviewImageActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.FlowerAniView;
import com.afmobi.palmchat.ui.customview.ScrollViewExtend;
import com.afmobi.palmchat.ui.customview.ScrollViewTouchEventFrozen;
import com.afmobi.palmchat.ui.customview.ViewEditBroadcastPicture;
import com.afmobi.palmchat.util.BitmapUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfMFileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import de.greenrobot.event.EventBus;

public class EditBroadcastPictureActivity extends BaseActivity implements ScrollViewTouchEventFrozen, OnClickListener,ViewEditBroadcastPicture.OnEditClickListener{
	private ViewEditBroadcastPicture mViewEdit; 
	private  FlowerAniView mViewTips;
	private ScrollViewExtend mScrollView;
	private RelativeLayout mTitleLayout;
	private RelativeLayout mLayoutBottom ;
	private RelativeLayout mButtonAdd;
	private Button sendButton;
	private ImageView backButton;
	private ImageView helpButton;
	private int max_selected = 9;
	private ArrayList<PhotoSelected> photoList =new ArrayList<PhotoSelected>() ;//图片文件名

	private ArrayList<AfMFileInfo> picturePathList = new ArrayList<AfMFileInfo>();
	private String picture_rule_for_recovery;//从发广播返回到编辑广播界面的  排布规则
	private String broadcast_message_recovery="";//从发广播返回到编辑广播界面的 保存的广播文字
	private int mSelect_position = 0;
	private final int ALBUM_REQUEST = 2;
	private String mCurTagname;
	/**从相册分享过来的照片uri*/
	private ArrayList<Uri> shareImageUri;
	private AfPalmchat mAfCorePalmchat;


	private final int VISION_FILTER_ACTIIVITY_REQUEST_CODE = 1000000;



	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_broadcast_edit_picture);
		mViewEdit=(ViewEditBroadcastPicture)findViewById(R.id.view_edit);
		mViewTips=(FlowerAniView)findViewById(R.id.view_tips);
		mScrollView=(ScrollViewExtend)findViewById(R.id.scrollview_editpic);
		mTitleLayout  =(RelativeLayout)findViewById(R.id.title_layout);
		mButtonAdd=(RelativeLayout)findViewById(R.id.button_addpic);
		sendButton=(Button)findViewById(R.id.send_button);
		backButton=(ImageView)findViewById(R.id.back_button);
		mLayoutBottom=(RelativeLayout)findViewById(R.id.layout_bottom);
		helpButton=(ImageView)findViewById(R.id.imageView_help);
		
		helpButton.setOnClickListener(this);
		mButtonAdd.setOnClickListener(this);
		sendButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
		looperThread = new LooperThread(); 
		looperThread.start();
	}
	private LooperThread looperThread;
	public boolean setTitleVisable(boolean isShow){
		if(isShow){
			if(mTitleLayout.getVisibility()==View.GONE){
				mLayoutBottom.setVisibility(View.VISIBLE);
				mTitleLayout.setVisibility(View.VISIBLE);
				return true;
			}
				 
			 
		}else{
			if(mTitleLayout.getVisibility()==View.VISIBLE){
				mLayoutBottom.setVisibility(View.GONE);
				mTitleLayout.setVisibility(View.GONE);
			}
			 
			 
		}
		 return false;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat; 
		mScrollView.setOnFrozenListener(this);
		
		Intent mIntent = getIntent();
		if (null != mIntent) {

			if(parseActionSend(mIntent)){
				if (photoList.size() < 0 || photoList.size() == 0) {
					return;
				}
			} else {
				@SuppressWarnings("unchecked")
				ArrayList<AfMFileInfo> plist= (ArrayList<AfMFileInfo>) mIntent.getSerializableExtra(JsonConstant.KEY_SENDBROADCAST_PICLIST);
				broadcast_message_recovery=mIntent.getStringExtra(JsonConstant.KEY_SENDBROADCAST_MESSAGE );
				mSelect_position = mIntent.getIntExtra(JsonConstant.KEY_SELECT_POSITION,0);
				mCurTagname = mIntent.getStringExtra(JsonConstant.KEY_SENDBROADCAST_TAGNAME);
				if(plist!=null){
					picturePathList=plist;
					if(picturePathList.size() >0){
						picture_rule_for_recovery=mIntent.getStringExtra(JsonConstant.KEY_SENDBROADCAST_PICRULE );
						mHandler.sendEmptyMessage(RECOVERY_FROM_SEND);
					}
				}
			}
		} 
		
		if(picturePathList.isEmpty()&&(photoList.isEmpty())){
			mButtonAdd.performClick();
		}
		
		EventBus.getDefault().register(this);

        mViewEdit.setEditOnClickListener(this);

	}

	@SuppressWarnings("unchecked")
	private boolean parseActionSend(Intent intent){
		boolean isFromThirdShare;
		String action = intent.getAction();
		String type = intent.getType();
		ArrayList<Uri> imageUris = null;
		try {
			imageUris = (ArrayList<Uri>)intent.getSerializableExtra(IntentConstant.SHAREIMAGE_URLS);
		} catch(Exception e) {
			e.printStackTrace();
		}
		if ((Intent.ACTION_SEND_MULTIPLE.equals(action) || Intent.ACTION_SEND.equals(action)) && type != null) {
			imageUris = new ArrayList<Uri>() ;
			if(type.startsWith(DefaultValueConstant.IMAGESHARE_LABEL)){
				
				if (Intent.ACTION_SEND_MULTIPLE.equals(action)) {
					imageUris= intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
				} else {
					Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
					imageUris.clear();
					imageUris.add(imageUri);
				}				
			}
			isFromThirdShare = true;
			doShareImageUris(imageUris);

		} else if(null!=imageUris){ 
			isFromThirdShare = true;
			doShareImageUris(imageUris);
			
		} else {
			isFromThirdShare = false;
		}
		return isFromThirdShare;
	}
	
	/**
	 * 设置分享图片
	 * @param imageUris
	 */
	private void addSharePic(ArrayList<Uri> imageUris) {
		
		if (imageUris != null) { 
			String path = null;
			photoList.clear();
			int index =0;
			for (Uri imageUri:imageUris){
				index++;
				if(index>DefaultValueConstant.MAX_SELECTED_BROADCAST_PIC){
					break;
				}
				File file =null;

				path = CommonUtils.getRealFilePath(this,imageUri);
				if(!TextUtils.isEmpty(path)){
					file= new File(path);
				} else {
					Bitmap imgSource = CommonUtils.getImageUrlWithAuthority(this, imageUri);
					if (imgSource != null) {
						file =new File( BitmapUtils.imageSaveToFile(imgSource, RequestConstant.CAMERA_CACHE + System.currentTimeMillis()));
						if(file.exists()){
							path = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
							path = BitmapUtils.imageCompressionAndSave(file.getAbsolutePath(), path);
						}
					}
				}

				if ((null==file)||(!file.exists())) {
					ToastManager.getInstance().show(context, R.string.fail_to_load_picture);
				}else{
					photoList.add(new PhotoSelected(path));
				}
			}	 
			sendButton.setVisibility(View.GONE);
			mButtonAdd.setEnabled(false);
			showProgressDialog(R.string.loading);
			mHandler.postDelayed(new Runnable() {			
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if((null!=looperThread)&&(null!=looperThread.handler)){
						looperThread.handler.obtainMessage( SEND_IMAGE_LOOPER,0).sendToTarget();
					}
				}
			},DefaultValueConstant.DELAYTIME_LOADTICT);


		}
	}
	
	/**
	 * 关闭当前页面 并且删除编辑过的图片
	 */
	private void doFinish() {
		finish();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() { 
				//当取消发送广播  删除编辑过的图片和缩图。 （如果发送的 就不能删除，发送的时候上传数据要用）
				Iterator<AfMFileInfo> sListIterator = picturePathList.iterator();
				while (sListIterator.hasNext()) {
					AfMFileInfo mediaParams = sListIterator.next();
					String picture_path = mediaParams.local_img_path;
					String thumb_path = mediaParams.local_thumb_path;
					if (!TextUtils.isEmpty(picture_path)) {
						FileUtils.fileDelete(picture_path);
					}
					if (!TextUtils.isEmpty(thumb_path)) {
						FileUtils.fileDelete(thumb_path);
					}
					sListIterator.remove();
				}
			}
		}, 100);
	}
	/**
	 * 返回键 提示是否放弃修改
	 */
	private void onBack(){
		if(mViewTips!=null&&mViewTips.isRunning()){
			mViewTips.stopPlayEditBroadcastPicTips();
		}else{
			if(!picturePathList.isEmpty()){
				AppDialog appDialog = new AppDialog(this);
				appDialog.createConfirmDialog(this, null, getString(R.string.exit_dialog_content), null, getString(R.string.exit), new OnConfirmButtonDialogListener() {
					@Override
					public void onRightButtonClick() {
						doFinish();
					}
		
					@Override
					public void onLeftButtonClick() {
		
					}
				});
				appDialog.show();
			}else{
				doFinish();
			}
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		boolean returnValuew = super.dispatchTouchEvent(ev);
		switch(ev.getAction()){
			case MotionEvent.ACTION_DOWN:{
				break;
			}
			
			case MotionEvent.ACTION_MOVE:{
				break;
			}
			
			case MotionEvent.ACTION_UP:{
				if((null!=mViewEdit)&&(mViewEdit.isLongTouchDown())){
					mViewEdit.onTouchEvent(ev);
				}
				break;
			}
		}
		return returnValuew;
	}
	
	/**
	 * 用已经存在的手机号或邮箱登录
	 */
	private void toLogin(ArrayList<Uri> imageUris) {
		MyActivityManager.getScreenManager().popAllActivityExceptOne(ActivitySendBroadcastMessage.class);
		Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
		if(null!=imageUris){
			intent.putExtra(IntentConstant.SHAREIMAGE_URLS, imageUris);
		}		
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
	
	public void setScrollViewEnable(final boolean isEnable,int moveDistance,int moveSpeed,int touchY){
		if(mScrollView!=null){
			 
			mScrollView.setShowMore(!isEnable ,moveDistance,moveSpeed,touchY);
		}
	}
	private LargeImageDialog largeImageDialog ;
	/**
	 * 进入查看大图模式
	 * @param position
	 */
	public void preViewImage(int position){
		/*  ArrayList<AfMFileInfo> picturePathList = new ArrayList<AfMFileInfo>(); 
		AfMFileInfo params = null;
		for(int i=0;i<photoList.size();i++){
			params = new AfMFileInfo();
			params.local_img_path = photoList.get(i);
//			params.local_thumb_path = thumb_path;
			params.type = Consts.BR_TYPE_IMAGE_TEXT;
			params.url_type = Consts.URL_TYPE_IMG;
			picturePathList.add(params);
		}*/
		largeImageDialog = new LargeImageDialog( this, picturePathList, position, LargeImageDialog.TYPE_SENDBROADCAST );
		largeImageDialog.show();
		
	}
	 
	
	/**
	 * 打开相册
	 */
	private void selectPicture() {
		// TODO Auto-generated method stub
		if (CommonUtils.getSdcardSize()) {
			ToastManager.getInstance().show(context, R.string.memory_is_full);
			return;
		} 
		int size = DefaultValueConstant.MAX_SELECTED_BROADCAST_PIC;// - picturePathList.size(); 
		if (size > 0) {
		
			
			
			Intent mIntent = new Intent(this, PreviewImageActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putInt("size",size);
			if(!picturePathList.isEmpty()){
				String picExistPaths="";
//				ArrayList<AfMFileInfo> tecnoCameraList = new ArrayList<AfMFileInfo>();
				for(int i=0;i<picturePathList.size();i++){
					picExistPaths+=picturePathList.get(i).original_picture_path;
					/*if(picturePathList.get(i).TecnoCameraPicName!=null){
						tecnoCameraList.add(picturePathList.get(i));
					}*/
				}
				mBundle.putString(JsonConstant.KEY_PICTRUE_EXIST_FOR_BROADCAST_EDIT, picExistPaths);
				/*if(!tecnoCameraList.isEmpty()){//tecno camrea pic
					mIntent.putExtra(JsonConstant.KEY_SENDBROADCAST_PICLIST, tecnoCameraList);
				}*/
			} 
			mBundle.putBoolean( JsonConstant.KEY_BROADCAST_EDIT_PIC,true);
			mIntent.putExtras(mBundle);
			mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(mIntent, ALBUM_REQUEST);
		}
	}
	/**
	 * 处理删除一张图片dbnm,.
	 * @param event
	 */
	public void onEventMainThread(ModifyPictureListEvent event) {
		if(event.getType()==ModifyPictureListEvent.DELETE){
			if(event.getIndex()<=picturePathList.size()){
//				photoList.remove(event.getIndex());
				picturePathList.remove(event.getIndex());
				if(mViewEdit!=null){
					mViewEdit.removePic(event.getIndex());
				}
				setAddPictureVisuable(true);
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mViewEdit!=null){
			mViewEdit.setOnPause();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy(); 
		EventBus.getDefault().unregister(this);
		if(looperThread!=null){
			looperThread.looper.quit();
		}
		if(mViewEdit!=null){
			mViewEdit.release(true);
		}
		if (mViewTips != null) {
			mViewTips.stopPlayEditBroadcastPicTips();;
		}
	}
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case RECOVERY_FROM_SEND:
				mViewEdit.recoveryPicLayout(  picturePathList,picture_rule_for_recovery,EditBroadcastPictureActivity.this);
				setAddPictureVisuable(true);
				break;
		 case MessagesUtils.MSG_PICTURE: 
			 	ArrayList<String> strPicList=new ArrayList<String>();
			 	for(int i=0;i<photoList.size();i++){//  为了Tecno手机拍照会2张图的问题 而附加的
			 		strPicList.add(photoList.get(i).path);
			 	}
				if(msg.arg1==1){//替换一张图片
					mViewEdit.changePath(msg.arg2,strPicList);
//					preViewImage(msg.arg2);
					if(largeImageDialog!=null&&largeImageDialog.isShowing()&&photoList!=null){//替换的时候 如果查看大图的Dialog还在  那就去处理喽
						
						if(photoList.size()>0){
							largeImageDialog.changePictureResult(msg.arg2 );
						}
						 
					}
				}else{
					 if(!photoList.isEmpty()){
						 mViewEdit.setImagePathArr( picturePathList,//strPicList,
								 EditBroadcastPictureActivity.this);
					 }else{
						 ToastManager.getInstance().show(context, R.string.fail_to_load_picture);
					 }
					setAddPictureVisuable(true);
					if(mViewTips!=null&&
						!SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getShowEditBroadcastTips()){
						mViewTips.startPlayEditBroadcastPicTips(null);
						SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).setShowEditBroadcastTips(true);
					}
				} 
				sendButton.setVisibility(View.VISIBLE); 
				mButtonAdd.setEnabled(true);
			 dismissProgressDialog();
			 break;
			 
		 	case SEND_IMAGE_THUMB_LOOPER://处理完缩率图以后  进入发送界面
		 		 dismissProgressDialog();
		 		 Intent intent = new Intent();  
                 intent.putExtra(JsonConstant.KEY_SENDBROADCAST_PICLIST, picturePathList); 
                 if(mViewEdit!=null){
                	 intent.putExtra(JsonConstant.KEY_SENDBROADCAST_PICRULE ,mViewEdit.getPictureRule() );   
                 } 
                 if(!TextUtils.isEmpty(broadcast_message_recovery)){
                	 intent.putExtra(JsonConstant.KEY_SENDBROADCAST_MESSAGE ,broadcast_message_recovery );
					 intent.putExtra(JsonConstant.KEY_SELECT_POSITION,mSelect_position);
                 }
                 intent.putExtra(JsonConstant.KEY_SENDBROADCAST_TYPE,  Consts.BR_TYPE_IMAGE_TEXT );
                 intent.putExtra(JsonConstant.KEY_SENDBROADCAST_TAGNAME, mCurTagname);
                 intent.setClass( EditBroadcastPictureActivity.this, ActivitySendBroadcastMessage.class);
                 startActivity(intent);
                 finish();
			 break;
			case MessagesUtils.FAIL_TO_LOAD_PICTURE: {
				 
				ToastManager.getInstance().show(context, R.string.fail_to_load_picture);
				break;
			} 
			case MessagesUtils.CAMERA: // zhh 图库列表页面点击拍照
//				selectCamera();
				break;
			 
			}

		};
	};

	private void setNextButton(){
		int count = picturePathList.size();
		if (count > 0) {
			sendButton.setText(getString(R.string.next) + "(" + count + "/" + max_selected + ")");
		}
		else{
			sendButton.setText(getString(R.string.next));
		}
	}
	/**
	 * 设置添加图片按钮是否显示
	 */
	private void setAddPictureVisuable(boolean isVisuable){
		if(picturePathList.size()>=DefaultValueConstant.MAX_SELECTED_BROADCAST_PIC||!isVisuable){
			mButtonAdd.setVisibility(View.GONE);
		}else{
			mButtonAdd.setVisibility(View.VISIBLE);
		}
		setNextButton();
	}
	

	    
	private   final int SEND_IMAGE_LOOPER = 9999;//在图片编辑前处理图片
	private   final int SEND_IMAGE_THUMB_LOOPER = 9998;//发送广播前 先生成缩率图
	private   final int RECOVERY_FROM_SEND = 9997;//从发广播界面恢复到编辑图片界面

	@Override
	public void onEditClickListner(int photoIndex) {
        Intent intent = new Intent(this, VisionFilterActivity.class);
		intent.putExtra(JsonConstant.KEY_SENDBROADCAST_PICLIST, picturePathList);
        intent.putExtra("photoIndex", photoIndex);
        startActivityForResult(intent, VISION_FILTER_ACTIIVITY_REQUEST_CODE);
	}

	class LooperThread extends Thread {
		
	 
		Handler handler;
		Looper looper;

		public void run() {
			Looper.prepare();
			looper = Looper.myLooper();
			handler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
					case SEND_IMAGE_THUMB_LOOPER://生成缩图 
						if(picturePathList!=null&&!picturePathList.isEmpty()){
							if(mViewEdit!=null){
								 mViewEdit.getPictureSizeCut();
							}
							AfMFileInfo afMinfo=null;
							for(int i=0;i<picturePathList.size();i++){
								afMinfo=picturePathList.get(i);
								if(!TextUtils.isEmpty(afMinfo .local_img_path)&&afMinfo.resize!=null&&afMinfo.cut!=null){
									String filterName="";
									if(!TextUtils.isEmpty( afMinfo.picFilter)) {
										filterName= Constants.PICTURE_FILTER +afMinfo.picFilter ;//滤镜名称加入 用于打点
									}
									afMinfo.local_thumb_path=
											BitmapUtils.clipImage(afMinfo.local_img_path, 
													afMinfo.local_img_path+"_"+afMinfo.getResize()+filterName
													, afMinfo.resize[0], afMinfo.resize[1],
													afMinfo.cut[0], afMinfo.cut[1], afMinfo.cut[2], afMinfo.cut[3]); 
								}
							} 
						}
						mHandler.obtainMessage(SEND_IMAGE_THUMB_LOOPER).sendToTarget();
						break;
					case SEND_IMAGE_LOOPER:
						boolean isChange=msg.arg1==1;//是否是替换
						for(int k=0;k<photoList.size();k++){
							String	largeFilename=Uri.decode( photoList.get(k).path);
							String cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
							cameraFilename = BitmapUtils.imageCompressionAndSave(largeFilename, cameraFilename);
							if (CommonUtils.isEmpty(cameraFilename)) { 
								mHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
								photoList.remove(k);
								k--;
								continue;
							}
							photoList.get(k).path=  cameraFilename;
							AfMFileInfo params = new AfMFileInfo();
							params.local_img_path = cameraFilename;
							params.original_picture_path= largeFilename;
//							params.local_thumb_path = thumb_path;
							params.type = Consts.BR_TYPE_IMAGE_TEXT;
							params.url_type = Consts.URL_TYPE_IMG;
//							params.TecnoCameraPicName=photoList.get(k).tecnoCameraTime;
							 
							if(picturePathList.size() > 0 ) {
								int index = picturePathList.size() - 1;
								AfMFileInfo afMFileInfo = picturePathList.get(index);
								if (afMFileInfo.is_add) {
									picturePathList.remove(index);
								}
							} 
							if(isChange){
								picturePathList.set(msg.arg2, params);
							}else{
								picturePathList.add(params);
							}  
						} 
						mHandler.obtainMessage(MessagesUtils.MSG_PICTURE,msg.arg1,msg.arg2).sendToTarget();  
						break;
					}
				}
			};
			Looper.loop();
		}
	}

	/**
	 * 是否已经包含该图片
	 * @param imgPath
	 * @return
     */
	private boolean isContainedPicPath(String imgPath){
		for(AfMFileInfo minfo:picturePathList ){
			if(imgPath!=null&&imgPath.equals( minfo.original_picture_path)){
				return true;
			}
		}
		return false;
	}
	private boolean processSelectedPic(	ArrayList<String> arrAddPiclist ){
		/*  if(!isChange){//如果不是替换 那就是重新选 要把之前选的全部清掉
						   picturePathList.clear();
						   if(mViewEdit!=null){
								mViewEdit.release(false);//先把之前选的清了 重新选过
							}
					  }*/


		for (String imgPath : arrAddPiclist) {
			if(!isContainedPicPath(imgPath)) {
				File f = new File(imgPath);
				if (!f.exists()) {
					ToastManager.getInstance().show(context, R.string.fail_to_load_picture);
				} else {
					photoList.add(new PhotoSelected(imgPath));
				}
			}
		}
		boolean isDelete=false;//如果有一张或以上的图片是不选了 删除了
			for(int i=0;i< picturePathList.size();i++ ){//如果在所有已经选中的图片中 找不到之前选的 那就把之前选的删除
				if(!arrAddPiclist.contains(picturePathList.get(i).original_picture_path)){
					picturePathList.remove(picturePathList.get(i));
					i--;
					isDelete=true;
				}

		}
		return isDelete;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==VISION_FILTER_ACTIIVITY_REQUEST_CODE){ //滤镜处理的返回
            PalmchatLogUtils.v("AFMOBI_VISION", "onActivityResult" + "requestCode :" + requestCode + " resultCode:" + resultCode);
 		    if (resultCode==0){
				if(data!=null) {
					ArrayList<AfResponseComm.AfMFileInfo> piclist = (ArrayList<AfResponseComm.AfMFileInfo>) data.getSerializableExtra(JsonConstant.KEY_SENDBROADCAST_PICLIST);
					for(int i=0;i<picturePathList.size()&&i<piclist.size();i++){
						picturePathList.get(i).picFilter=piclist.get(i).picFilter;
					}
				}
				mViewEdit.applyVisionFilter();
            }
		}
		else if (resultCode == ActivitySendBroadcastMessage.SHOW_VIOICE_FUNCTIONTIPS) {
			 if(picturePathList.isEmpty()){
				 finish();
			 }
		 }
		 else if (requestCode == ALBUM_REQUEST ) { // 相册选择照片返回
			if (data != null) {//这里要处理三种情况，  第一次添加进来的   后续用add添加进来的 ，  查看大图时change进来的
				 photoList.clear();
				 boolean isDelete=false;//是否存在不选中之前图片的情况
				 boolean isChange=data.getBooleanExtra("isChange", false);//是否是替换
				 int  changIndex=data.getIntExtra("postion", 0);
				 if( resultCode == MessagesUtils.PICTURE){//选相册
					ArrayList<String> arrAddPiclist  = data.getStringArrayListExtra("photoLs");
					 isDelete= processSelectedPic(arrAddPiclist);//处理选中的图片
					
				 }else if( resultCode == Activity.RESULT_OK){//拍照
					 String cameraFilename = data.getStringExtra("cameraFilename");
//					 String tecnoCameraFilename = data.getStringExtra("tecnoCameraFilename");
					 ArrayList<String> arrAddPiclist  = data.getStringArrayListExtra("photoLs");
					 isDelete=processSelectedPic(arrAddPiclist);//处理选中的图片
					  

					 PhotoSelected _PhotoSelected=new PhotoSelected();
					/* if(!TextUtils.isEmpty(tecnoCameraFilename)){//如果是Tecno手机拍的 get time
						 _PhotoSelected.setTecnoCameraName(   cameraFilename,tecnoCameraFilename); 
					 }*/
					 if(cameraFilename!=null){
						 String imgPath=null;
						 if(cameraFilename.contains("/")){//如果拍照回来已经是全路径了
							 imgPath=  cameraFilename;
						 }else{
							 imgPath= RequestConstant.CAMERA_CACHE+cameraFilename;
						 }
								File f = new File(imgPath);
								if (!f.exists()) {
									ToastManager.getInstance().show(context, R.string.fail_to_load_picture);
								}else{
									_PhotoSelected.path=imgPath;
									photoList.add(_PhotoSelected);
								}
					  }
					 
				 }//-------------
			 
				if (photoList.size() < 0 || photoList.size() == 0) {
					if(isDelete){
						if(mViewEdit!=null) {
							 mViewEdit.release(false);//先清之前加载的图片
							mViewEdit.setImagePathArr(picturePathList,  EditBroadcastPictureActivity.this);
						}
					}
					return;
				}
				if(mViewEdit!=null){
					mViewEdit.release(false);//先清之前加载的图片
				}
				sendButton.setVisibility(View.GONE); 
				mButtonAdd.setEnabled(false);
					showProgressDialog(R.string.loading);
					 looperThread.handler.obtainMessage( SEND_IMAGE_LOOPER,isChange?1:0, changIndex).sendToTarget(); 
				 

			}
			setNextButton();
		} 
	}

	 
	@Override
	public void onScrollChangedFrozen(MotionEvent ev) {
		// TODO Auto-generated method stub
//		mViewEdit.touchAction(ev);
	}
	@Override
	public void onScrollChanged() {
		// TODO Auto-generated method stub
		mViewEdit.touchAction( );
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setNextButton();
		/*if(photoList.size()!=picturePathList.size()  ){
			mViewEdit.setImagePathArr(photoList,false);
		}else{
			for(int i=0;i<photoList.size();i++){
				if(!photoList.get(i).equals(picturePathList.get(i).local_img_path)){
					mViewEdit.setImagePathArr(photoList,false);
					break;
				}
			}
		}*/
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	 
			switch (v.getId()) {
			case R.id.back_button:
				onBack();
				break;
			case R.id.send_button: 
				showProgressDialog(R.string.loading);
				 looperThread.handler.obtainMessage( SEND_IMAGE_THUMB_LOOPER  ).sendToTarget(); //先去生成缩率图
				break; 
			case R.id.button_addpic:
				selectPicture(); 
				break;
			case R.id.imageView_help:
				if(mViewTips!=null){
					mViewTips.startPlayEditBroadcastPicTips(null);
				}
				break;
	   }
   }
	@Override
	public void onBackPressed() {
		 onBack();
	}
	
	/**
	 * 去处理分享
	 * @param imageUris
	 */
	private void doShareImageUris(ArrayList<Uri> imageUris) {
		String password = CacheManager.getInstance().getMyProfile().password;
		String afid = CacheManager.getInstance().getMyProfile().afId;
		boolean isExist = CommonUtils.isActivityExist(getPackageName(), MainTab.class.getCanonicalName());
		// 未登录时跳到登录
		if (TextUtils.isEmpty(password) || TextUtils.isEmpty(afid)) { 
			ToastManager.getInstance().show(this, R.string.transit_hint_login);
			toLogin(null);
		} else if(!isExist){
			toLogin(imageUris);
		} else {
			addSharePic(imageUris);
		}
	}
	/**
	 *   为了Tecno手机拍照会2张图的问题 而附加的
	 * @author wxl 
	 *
	 */
	class PhotoSelected{
		String path;
//		long []tecnoCameraTime;
		PhotoSelected(String imgpath){
			path=imgpath;
		}
		PhotoSelected( ){
			 
		}
		/*public void setTecnoCameraName(String time1,String time2){
			tecnoCameraTime=new long[2];
			tecnoCameraTime[0]= ClippingPicture.getCameraTime(time1);
			tecnoCameraTime[1]= ClippingPicture.getCameraTime(time2);
		}*/
	}
}
