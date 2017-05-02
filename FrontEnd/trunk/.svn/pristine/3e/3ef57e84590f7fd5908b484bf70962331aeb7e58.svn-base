package com.afmobi.palmchat.ui.customview;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.customview.photos.WallEntity;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobi.palmchat.util.universalimageloader.core.assist.FailReason;
import com.afmobi.palmchat.util.universalimageloader.core.listener.ImageLoadingListener;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
//import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author heguiming 2013-11-7
 *
 */
public class PhotoWallView extends LinearLayout {
	
	private static final String TAG = PhotoWallView.class.getCanonicalName();

	private View parentView;
	private LinearLayout mLayout2;
	private Activity mContext;
	private AfProfileInfo afProfileInfo;
	private List<WallEntity> list;
	private MyImageView[] imageViewAry = new MyImageView[8];
	private ProgressBar[] pbAry = new ProgressBar[8];
	private MyProfileActivity myProfileActivity;
	private LayoutInflater layoutInflater;
	
	public LargeImageDialog largeImageDialog;

	private Context context;
	
	public PhotoWallView(Context context) {
		super(context);
		this.context = context;
	}

	public PhotoWallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		parentView = layoutInflater.inflate(R.layout.view_photo_wall_parent2, null);
		
		imageViewAry[0] = (MyImageView) parentView.findViewById(R.id.photo_wall_1);
		imageViewAry[1] = (MyImageView) parentView.findViewById(R.id.photo_wall_2);
		imageViewAry[2] = (MyImageView) parentView.findViewById(R.id.photo_wall_3);
		imageViewAry[3] = (MyImageView) parentView.findViewById(R.id.photo_wall_4);
		imageViewAry[4] = (MyImageView) parentView.findViewById(R.id.photo_wall_5);
		imageViewAry[5] = (MyImageView) parentView.findViewById(R.id.photo_wall_6);
		imageViewAry[6] = (MyImageView) parentView.findViewById(R.id.photo_wall_7);
		imageViewAry[7] = (MyImageView) parentView.findViewById(R.id.photo_wall_8);
		
		
		
		pbAry[0] = (ProgressBar) parentView.findViewById(R.id.pb_1);
		pbAry[1] = (ProgressBar) parentView.findViewById(R.id.pb_2);
		pbAry[2] = (ProgressBar) parentView.findViewById(R.id.pb_3);
		pbAry[3] = (ProgressBar) parentView.findViewById(R.id.pb_4);
		pbAry[4] = (ProgressBar) parentView.findViewById(R.id.pb_5);
		pbAry[5] = (ProgressBar) parentView.findViewById(R.id.pb_6);
		pbAry[6] = (ProgressBar) parentView.findViewById(R.id.pb_7);
		pbAry[7] = (ProgressBar) parentView.findViewById(R.id.pb_8);
		
		mLayout2 = (LinearLayout) parentView.findViewById(R.id.layout_2);
		
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(parentView, params);
		
	}

	public void setMyProfileActivity(MyProfileActivity myProfileActivity) {
		this.myProfileActivity = myProfileActivity;
	}
	/**
	 * 重新设置一下My Profile 因为修改资料后再去点相册 这个时候的Profile不是同一个对象了
	 * @param afProfileInfo
	 */
	public void resetProfile(AfProfileInfo afProfileInfo){
		this.afProfileInfo = afProfileInfo;
	}
	public void setData(Activity mContext, List<WallEntity> list, AfProfileInfo afProfileInfo) {
		this.mContext = mContext;
		this.list = list;
		this.afProfileInfo = afProfileInfo;
		if (null != list && !list.isEmpty()) {
			parentView.setVisibility(View.VISIBLE);
			for (int i = 0; i < imageViewAry.length; i++) {
//				if(PalmchatApp.getOsInfo().getUa().equals("Lenovo S930") || PalmchatApp.getOsInfo().getUa().contains("MediaPad") || PalmchatApp.getOsInfo().getUa().contains("P6") ){//适配Lenovo s930
					int WindowWidth = ImageUtil.DISPLAYW;
					int Spacing=  CommonUtils.dip2px(mContext, 6);
					int Width = WindowWidth - Spacing;
					PalmchatLogUtils.e(TAG, "Width="+Width+"");
					int w = (Width / 4);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, w);
					imageViewAry[i].setLayoutParams(params);
//				}
				imageViewAry[i].setVisibility(View.INVISIBLE);
			}
			initView();
		} else {
			parentView.setVisibility(View.GONE);
		}
	}

	public List<WallEntity> getList() {
		return list;
	}

	public void removeAddPhoto() {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				WallEntity entity = list.get(i);
				if (entity.photoType == WallEntity.PhotoTypeAdd) {
					list.remove(entity);
				}
			}
		}
	}
	
	int position = 0 ;
	/**
	 * 设置个人相册里的图片
	 */
	private void initView() {
		int size = list.size() > 8 ? 8 : list.size();
		if (size <= 4) {
			mLayout2.setVisibility(View.GONE);
		} else {
			mLayout2.setVisibility(View.VISIBLE);
		}
		for (int i = 0; i < size; i++) {
			position = i;
			final WallEntity entity = list.get(i);
			imageViewAry[i].setVisibility(View.VISIBLE);
			pbAry[i].setVisibility(View.VISIBLE);
			if (null != entity.sn) {
				final int inx=i;
				ImageManager.getInstance().DisplayAvatarImage_PhotoWall(imageViewAry[i], entity.serverUrl,entity.afid,Consts.AF_HEAD_MIDDLE
						,Consts.AFMOBI_SEX_MALE,entity.sn,true,false,new ImageLoadingListener(){ 
							@Override
							public void onLoadingStarted(String imageUri, View view) { 
							} 
							@Override
							public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
								// TODO Auto-generated method stub
								 pbAry[inx].setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								// TODO Auto-generated method stub
								 pbAry[inx].setVisibility(View.GONE);
							}

							@Override
							public void onLoadingCancelled(String imageUri, View view) {
								// TODO Auto-generated method stub
								 pbAry[inx].setVisibility(View.GONE);
							}
					
				});
				
				
				/*final PhotoWallImageInfo imageInfo = new PhotoWallImageInfo(entity.afid, entity.sn, Consts.AF_HEAD_MIDDLE, entity.serverUrl, pbAry[i], null);
				final String savePath = imageInfo.getFileName();
				if (!StringUtil.isNullOrEmpty(savePath)) {
//					Bitmap bitmap = ImageUtil.getBitmapFromFile(savePath);
					Bitmap bitmap = ImageUtil.readBitMap(savePath);
					if (null != bitmap) {
						final BitmapDrawable background = new BitmapDrawable(bitmap);
						imageViewAry[i].setImageDrawable(background);
					} else {
						ImageLoader.getInstance().displayImage(imageViewAry[i], imageInfo);
					}
				} else {
					ImageLoader.getInstance().displayImage(imageViewAry[i], imageInfo);
				}*/
			} else {
				pbAry[i].setVisibility(View.GONE);
				imageViewAry[i].setImageResource(R.drawable.album_add_btn_selector);
			}
			addListener(i);
		}
	}
	/**
	 * 图片点击事件响应
	 * @param index
	 */
	private void addListener(final int index) {
		final ImageView imageView = imageViewAry[index];
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != afProfileInfo) {
					WallEntity wallEntity = list.get(index);
					if (wallEntity != null) {
						if (wallEntity.photoType == WallEntity.PhotoTypeAdd) {//添加一张图片
							if (null != myProfileActivity) {
//								myProfileActivity.isSetAvatar = false;
								if (null != afProfileInfo && StringUtil.isNullOrEmpty(afProfileInfo.getSerialFromHead())) {
									myProfileActivity.showSetPhotoWallDialog();
								} else {
									myProfileActivity.currentAction = MyProfileActivity.AF_ACTION_PHOTO_WALL;
									CacheManager.getInstance().setCurAction(MyProfileActivity.AF_ACTION_PHOTO_WALL);
									myProfileActivity.alertMenu();
									myProfileActivity.setPhotoWall_update_or_add_State();
								}
								
							}
						} else {//查看大图模式
							
							// heguiming 2013-12-04
							new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_PWALL);
//							MobclickAgent.onEvent(mContext, ReadyConfigXML.ENTRY_PWALL);
							
							List<String> serials = afProfileInfo.getSerials();
							largeImageDialog = new LargeImageDialog(mContext, serials, afProfileInfo.getServerUrl(),afProfileInfo.afId, index, LargeImageDialog.TYPE_PHOTO_WALL);
							if (null != myProfileActivity) {
								largeImageDialog.setMyProfileActivity(myProfileActivity);
							}
							largeImageDialog.show();
							largeImageDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
								
								@Override
								public void onCancel(DialogInterface dialog) {
									/*按下Back键时*/
									if(context instanceof ProfileActivity){
 										((ProfileActivity) context).showTitle(-1,false);
										((ProfileActivity) context).setNavigationBar(); //add by zhh 手机隐藏底部导航条
									}else if(context instanceof MyProfileActivity){
										((MyProfileActivity) context).showTitle(-1,false);
										((MyProfileActivity) context).setNavigationBar(); //add by zhh 手机隐藏底部导航条
									}
								}
							});
							largeImageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { 
								@Override
								public void onDismiss(DialogInterface dialog) {
									/*按下标题栏回退按钮*/
									if(context instanceof ProfileActivity){
 										((ProfileActivity) context).showTitle(-1,false);
									}else if(context instanceof MyProfileActivity){
										((MyProfileActivity) context).showTitle(-1,false);
									}
								} 
							});
							if(context instanceof ProfileActivity){
								((ProfileActivity) context).showTitle(255,false);
							}else if(context instanceof MyProfileActivity){
								((MyProfileActivity) context).showTitle(255,false);
							}
						}
					}
				}
			}
		});
	}
}
