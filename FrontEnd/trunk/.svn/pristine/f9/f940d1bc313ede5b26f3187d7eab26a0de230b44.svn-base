package com.afmobi.palmchat.ui.activity.chats;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.db.SharePreferenceService;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chattingroom.model.ImageFolderInfo;
import com.afmobi.palmchat.ui.activity.social.ActivitySendBroadcastMessage;
import com.afmobi.palmchat.ui.customview.TouchImageView;
import com.afmobi.palmchat.util.ClippingPicture;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.LocalImageViewPager;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageLoader;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfResponseComm.AfMFileInfo;

public class PreviewImageActivity extends BaseActivity implements OnClickListener, OnItemClickListener,OnItemLongClickListener {
	private Button tvOk;
	private Button imgChecked;
	TouchImageView imgZoom;
	private ImageView imgBack;
	FrameLayout fl_bigimg;
	private GridView mGridView;
	private TextView tv_album_type;
	private ViewGroup rl_album_type;
	private ViewGroup rl_bottom;
	private int max_selected = 6;
	private int change_postion;//用于替换图片时表示索引值
	private boolean isChange;//是否是替换图片
	private List<String> allPhotosLs; // 所有相册的照片集
	private List<String> selectedAlbumPhotosLs; // 当前选中的相册所对应的照片
	private ArrayList<ImageFolderInfo> mainLs; // 按相册存放所有照片
	private LinkedHashMap<String, String> mHashMap; // 当前选中的图片
	private LinkedHashMap<String, String> mHashMap_locked; //当前选中的图片中被锁定的图片，用户锁定该图片不能被取消选择
	private String currentPath;
	private final static int IMAGE_LOADED = 2220; // 获取所有图片
	private GridAdapter mAdapter;
	private boolean isAllPhotos = true; // 当前是否为所有图片
	private boolean isShowZoomImg = false; // 判断当前是否是显示大图片
	private String cameraFilename; // 拍照时，当前照片名
	Dialog mDialog;
	AlbumAdapter mAlbumAdapter;
	private int preAlbumPosition; // 当前选中的相册位置
	private String mButtonText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 

	}

	@Override
	public void findViews() {
		setContentView(R.layout.activity_preview_image2);

		tvOk = (Button) findViewById(R.id.tv_ok);
		mGridView = (GridView) findViewById(R.id.gridview);
		imgZoom = (TouchImageView) findViewById(R.id.img_zoom);
		imgChecked = (Button) findViewById(R.id.img_checked);
		fl_bigimg = (FrameLayout) findViewById(R.id.fl_bigimg);
		imgBack = (ImageView) findViewById(R.id.img_back);
		tv_album_type = (TextView) findViewById(R.id.tv_album_type);
		rl_bottom = (ViewGroup) findViewById(R.id.rl_bottom);
		rl_album_type = (ViewGroup) findViewById(R.id.rl_album_type);
		tvOk.setVisibility(View.GONE);
		tvOk.setOnClickListener(this);
		imgChecked.setOnClickListener(this);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnItemLongClickListener( this);
		imgBack.setOnClickListener(this);
		// tv_album_type.setOnClickListener(this);
		rl_album_type.setOnClickListener(this);
		rl_bottom.setOnClickListener(this);

		allPhotosLs = new ArrayList<String>();
		selectedAlbumPhotosLs = new ArrayList<String>();
		mainLs = new ArrayList<ImageFolderInfo>();
		mHashMap = new LinkedHashMap<String, String>();
		mHashMap_locked= new LinkedHashMap<String, String>();
		initGridView();
	}
	private String picExistPaths;//需要排除的已被发广播选中过的图片路径
	private boolean isBroadcastEditPic;
	private boolean  isTecno=false;
//	private ArrayList<AfMFileInfo> mFileInfolist;
	private String strDCIMPath;
	@Override
	public void init() {
		Intent mIntent = getIntent();
		if (null != mIntent) {
			Bundle mBundle = mIntent.getExtras();
			if (mBundle != null) {
				max_selected = mBundle.getInt("size");
				change_postion=mBundle.getInt("postion");
				isChange=mBundle.getBoolean("isChange");
				picExistPaths=mBundle.getString(JsonConstant.KEY_PICTRUE_EXIST_FOR_BROADCAST_EDIT, null);
				isBroadcastEditPic=	mBundle.getBoolean(JsonConstant.KEY_BROADCAST_EDIT_PIC,false);
				if(isBroadcastEditPic){
//					mFileInfolist = (ArrayList<AfMFileInfo>) mIntent.getSerializableExtra(JsonConstant.KEY_SENDBROADCAST_PICLIST);
					mButtonText = getResources().getString(R.string.next);
				 }
				else{
					mButtonText = getResources().getString(R.string.ok);
				}
			}
		}
		 
	 
		if ( PalmchatApp.getOsInfo().isTecno()) {  
				isTecno=true; 
		} 
		strDCIMPath=Environment .getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DCIM).getAbsolutePath();
		ImageLoader.getInstance().stop();//在载入本地相册前要先关闭其他图片显示任务，不然任务线程占用满后  这里就加载不出来了
		// PalmchatApp.getApplication().mMemoryBitmapCache.evictAll();
		new Thread() {
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = PalmchatApp.getApplication().getContentResolver();
				Cursor mCursor = mContentResolver.query(mImageUri, null, null, null, null);

				HashMap<String, List<String>> gruopMap = new HashMap<String, List<String>>();
				if(mCursor!=null) {
					while (mCursor.moveToNext()) {
						String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
						if (!TextUtils.isEmpty(picExistPaths) && !TextUtils.isEmpty(path)) {

							if (isBroadcastEditPic) {//广播图片才需要判断是否有选过
								int lastIndex = path.lastIndexOf("/");
								boolean isExist = false;
								if (picExistPaths.contains(path)) {//已选路径中已经有这张图了
									isExist = true;
								} else if (isTecno) {//Tecno手机不一样 他会自动生成一张照片到相册
									if (path.contains(strDCIMPath) && lastIndex < path.length()) {//in DCIM
										/*if (mFileInfolist != null && !mFileInfolist.isEmpty()) {
											long timeCam = ClippingPicture.getCameraTime(path.substring(lastIndex + 1));
											for (int i = 0; i < mFileInfolist.size(); i++) {
												if (mFileInfolist.get(i).TecnoCameraPicName != null) {
													if (timeCam > 0
															&& timeCam > mFileInfolist.get(i).TecnoCameraPicName[0]
															&& timeCam < mFileInfolist.get(i).TecnoCameraPicName[1]) {
														isExist = true;
													}
												}
											}
										}*/
										if (lastIndex > 0 && lastIndex < path.length()
												&& picExistPaths.contains(path.substring(lastIndex))) {
											isExist = true;
										}
									}
								} else if (path.contains("afmobi/") && lastIndex > 0 && lastIndex < path.length()//if palmchat copy camera img to DCIM
										&& picExistPaths.contains(path.substring(lastIndex))) {
									isExist = true;//如果是其他品牌的手机 广播模块拍照时会copy图到DCIM/afmobi/目录下的 要判断文件名是否相同
								}
								if (isExist) {
									mHashMap.put(path, path);
									if (isChange) {//如果是替换图片 把已有图片加入锁定
										mHashMap_locked.put(path, path);
									}
								}
							}
						}
						if (!TextUtils.isEmpty(path)) {
							File file = new File(path);
							String parentName = null;
							if (null != file && null != file.getParentFile() && file.exists()) {
							/* 获取所有图片集合 并 排序 */
								allPhotosLs.add(path);
								parentName = file.getParentFile().getName();

								if (!gruopMap.containsKey(parentName)) {

									List<String> chileList = new ArrayList<String>();
									chileList.add(path);
									gruopMap.put(parentName, chileList);

								} else {
									gruopMap.get(parentName).add(path);
								}
							}
						}
					}
				}

				mCursor.close();
				ArrayList<ImageFolderInfo> tmpList = new ArrayList<ImageFolderInfo>();

				Iterator<Map.Entry<String, List<String>>> it = gruopMap.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, List<String>> entry = it.next();
					ImageFolderInfo mImageBean = new ImageFolderInfo();
					String key = entry.getKey();
					List<String> value = entry.getValue();
					mImageBean.dir = key;
					Collections.reverse(value);
					mImageBean.filePathes.addAll(value);
					mImageBean.pisNum = value.size();
					mImageBean.path = value.get(0);

					tmpList.add(mImageBean);
				}

				gruopMap.clear();
				if(isChange){
					if(max_selected-mHashMap_locked.size()>1){//这个处理是针对部分tecno手机 不会自动生成拍照图片而加的额外处理
						max_selected=mHashMap_locked.size()+1;
					}
				}
				mHandler.obtainMessage(IMAGE_LOADED, tmpList).sendToTarget();
			};
		}.start();

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			// root dir grid display
			case IMAGE_LOADED:
				
				allPhotosLs.size();
				if (allPhotosLs.size() > 0) {
					Collections.reverse(allPhotosLs);
					mainLs.addAll((ArrayList<ImageFolderInfo>) msg.obj);
					ImageFolderInfo migInfo = new ImageFolderInfo();
					migInfo.dir = getResources().getString(R.string.all_photos);
					migInfo.pisNum = allPhotosLs.size();
					migInfo.path = allPhotosLs.get(0);
					migInfo.filePathes.addAll(allPhotosLs);
					migInfo.isSelected = true;
					mainLs.add(0, migInfo);
				} else {
					ImageFolderInfo migInfo = new ImageFolderInfo();
					migInfo.dir = getResources().getString(R.string.all_photos);
					migInfo.pisNum = allPhotosLs.size();
					migInfo.path = "";
					migInfo.filePathes.addAll(allPhotosLs);
					migInfo.isSelected = true;
					mainLs.add(0, migInfo);
				}

				allPhotosLs.add(0, "");
				showAllPhotos();

				break;

			default:
				break;
			}

		};
	};

	/**
	 * 显示所有图片
	 */
	private void showAllPhotos() {
		isAllPhotos = true;
		selectedAlbumPhotosLs.clear();
		selectedAlbumPhotosLs.addAll(allPhotosLs);
//		selectedAlbumPhotosLs.add(0, ""); // 第一项为拍照
		initGridView();
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 显示选定相册图片
	 */
	private void showSelectedAlbumPhotos(List<String> ls) {
		selectedAlbumPhotosLs.clear();
		selectedAlbumPhotosLs.addAll(ls);
		initGridView();
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * second dir adapter
	 * 
	 * @author afmobi
	 * 
	 */
	private class GridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return selectedAlbumPhotosLs.size();
		}

		@Override
		public Object getItem(int position) {
			return selectedAlbumPhotosLs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(PreviewImageActivity.this).inflate(R.layout.previewgriditem2, null);
				holder.img = (ImageView) convertView.findViewById(R.id.icon);
				holder.img2 = (ImageView) convertView.findViewById(R.id.icon2);
				holder.isselected = (ImageView) convertView.findViewById(R.id.isselected);
				holder.ll_photo = (ViewGroup) convertView.findViewById(R.id.ll_photo);
				convertView.setTag(holder);
				AbsListView.LayoutParams param = new AbsListView.LayoutParams(
				        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				        ImageUtil.DISPLAYW/3);
				    convertView.setLayoutParams(param);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == 0 && isAllPhotos) {
				holder.ll_photo.setVisibility(View.VISIBLE);
				holder.img.setVisibility(View.GONE);
				holder.isselected.setVisibility(View.GONE);

				holder.ll_photo.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						selectCamera();
					}
				});
			} else {
				holder.img.setVisibility(View.VISIBLE);
				holder.ll_photo.setVisibility(View.GONE);
				holder.isselected.setVisibility(View.VISIBLE);
				final String imgPath = selectedAlbumPhotosLs.get(position);
//				PreviewImageInfo imageInfo = new PreviewImageInfo(imgPath, false);
				
				ImageManager.getInstance().DisplayLocalImage(holder.img,imgPath ,R.color.color_login_bg,R.color.color_login_bg,null);
				if (mHashMap.containsKey(imgPath)) {
					holder.isselected.setImageResource(R.drawable.btn_chattingbackground_sel_big);
					holder.img.setSelected(true);
				} else {
					holder.img.setSelected(false);
					holder.isselected.setImageResource(R.drawable.btn_chattingbackground_nosel_big);
				} 

			}

			return convertView;
		}

		
	}
	class ViewHolder {
		ImageView img, img2, isselected;
		ViewGroup ll_photo;
	}
	/**
	 * check if send btn is clickable
	 */
	private void updateSelected() {
		if(!isShowZoomImg){
			int count = mHashMap.size();
			if (count > 0) {
				tvOk.setClickable(true);
				tvOk.setVisibility(View.VISIBLE);
				tvOk.setEnabled(true);
				if(isChange){//如果是替换一张图片的 只显示ok
					tvOk.setText(mButtonText);
				}else{
					tvOk.setText(mButtonText + "(" + count + "/" + max_selected + ")");
				}
			} else {
				tvOk.setClickable(false);
				tvOk.setVisibility(View.GONE);
				tvOk.setText(mButtonText);
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent mIntent;
		switch (v.getId()) {
		case R.id.img_back:
			goBack();
			break;
		case R.id.tv_ok:
			
			ArrayList<String> ls = new ArrayList<String>();
			Set<String> mapKey = mHashMap.keySet();
			Iterator<String> iterator = mapKey.iterator();
			while (iterator.hasNext()) {
				String imgPath = iterator.next();
				if(!mHashMap_locked.containsKey(imgPath)){ //不锁定的时候 才加进来
					ls.add(imgPath);
				}
			}
			mIntent = new Intent();
			mIntent.putStringArrayListExtra("photoLs", ls);
			mIntent.putExtra("isChange", isChange);
			mIntent.putExtra("postion", change_postion);
			setResult(MessagesUtils.PICTURE, mIntent);
			finish();
			break;
		case R.id.rl_album_type: // 相册类型
			createAlbumDialog();
			break;
		case R.id.img_checked: // 打图预览时 标题栏check图片
//			selectAPicInLargeDialog();
			break;
		case R.id.rl_bottom:
			break;
		default:
			break;
		}

	}
	/**
	 * 从大图预览界面选择一张图
	 * @param filePath
	 */
	public void selectAPicInLargeDialog(String filePath){
		if(TextUtils.isEmpty( filePath)){
			return ;
		}
		currentPath=filePath;
		if(isChange&&mHashMap_locked.containsKey(currentPath)){
			//被锁定 无法选择或 取消选择  用于替换图片的情况
		}else{ 
			if (mHashMap.containsKey(currentPath)) {
				//5.2.1修改  查看大图的时候只能选中  不能取消选中 
				//mHashMap.remove(currentPath);
				//imgChecked.setBackgroundResource(R.drawable.set_select_n);
			} else {
				if (mHashMap.size() < max_selected) {
					mHashMap.put(currentPath, currentPath);
	//				imgChecked.setBackgroundResource(R.drawable.set_select);
				} else {
					if(isChange){//如果是替换一张图片的 
						ToastManager.getInstance().show(PalmchatApp.getApplication(),R.string.change_one_only); 
					}else{
						ToastManager.getInstance().show(PalmchatApp.getApplication(),
								CommonUtils.replace(ReplaceConstant.TARGET_FOR_REPLACE,String.valueOf( max_selected) , getResources().getString(R.string.max_selected_img)));
					}
					return;
				}
			}
		}
		updateSelected();
//		goBack();// 5.2.1改为只要点了这个按钮  就关闭大图预览
		changeUi();
	}
	private boolean goBack() {

		if (isShowZoomImg) {
			isShowZoomImg = false;
			changeUi();
			return false;
		} else {
			setResult(ActivitySendBroadcastMessage.SHOW_VIOICE_FUNCTIONTIPS);
			finish();
			return true;
		}
	}

	private void selectCamera() {
		if (mHashMap.size() >= max_selected) {
			ToastManager.getInstance().show(PalmchatApp.getApplication(),
					CommonUtils.replace(ReplaceConstant.TARGET_FOR_REPLACE,String.valueOf( max_selected) , getResources().getString(R.string.max_selected_img)));
		 	return ;
		}
		if (CommonUtils.getSdcardSize()) {
			ToastManager.getInstance().show(context, R.string.memory_is_full);
			return;
		}
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraFilename = ClippingPicture.getCurrentFilename();
		PalmchatLogUtils.e("WXL","PreViewImage selectCamera() cameraFilename="+cameraFilename+" obj="+this.toString());
		SharePreferenceService.getInstance(this).savaFilename(cameraFilename);
	   /* if(isBroadcastEditPic){//当是编辑广播图片的时候  检查对应文件夹有没生成 没的话就生成
			File f = new File(RequestConstant.CAMERA_TO_DCIM_CACHE);
			if (!f.exists()) {
				f.mkdirs();
			}
		 }*/
		
		File f = new File( RequestConstant.CAMERA_CACHE, cameraFilename);
		Uri u = Uri.fromFile(f);
		intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
		try {
			startActivityForResult(intent, MessagesUtils.CAMERA);
		} catch (Exception e) {
			ToastManager.getInstance().show(context, R.string.no_camera);
		}
	}


	/**
	 * 拍照后保存到图库 用于非Tecno手机
	 * @param path
	 * @return
     */
	private String pathToInfo(String path) {

		File f = null;
		try {
			 
			f = new File(RequestConstant.CAMERA_CACHE, path);
			path = f.getAbsolutePath(); 
			if (f != null && path != null) {  
				if (CommonUtils.isEmpty(path)) {
//					mHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
					return null;
				} 
				/* 将原始图片保存至系统图库 */
				return	saveToDcim(f);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * add by zhh 将图片保存至本地图库
	 */
	private String saveToDcim(File curFile) {
		 
		if ( PalmchatApp.getOsInfo().isTecno()) { // tenno手机调用系统拍照时系统已做了本地保存，所以无需再将照片做本地化保存
			 	return null;
		} 
		StringBuffer out = new StringBuffer(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
		out.append("/afmobi");
		File outFile = new File(out.toString());
		if (!outFile.exists()) {
			outFile.mkdirs();
		}
//		final String saveDir = outFile.getAbsolutePath();
		out.append("/");
		out.append(curFile.getName() );

		FileUtils.copyToImg(curFile.getAbsolutePath(), out.toString());
		// 把图片保存下来后 扫描新增的媒体文件，可以在打开相册的时候出现

		PalmchatApp.getApplication().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + out.toString())));
		return out.toString();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		ViewHolder holder=(ViewHolder) view.getTag();
		final String imgPath = selectedAlbumPhotosLs.get(position);
		if(isChange&&mHashMap_locked.containsKey(imgPath)){
			//被锁定 无法选择或 取消选择  用于替换图片的情况
		}else{
			if (mHashMap.containsKey(imgPath)) {
				mHashMap.remove(imgPath);
				holder.img.setSelected(false);
				holder.isselected.setImageResource(R.drawable.btn_chattingbackground_nosel_big);

			} else {
				if (mHashMap.size() < max_selected) {
					mHashMap.put(imgPath, imgPath);
					holder.img.setSelected(true);
					holder.isselected.setImageResource(R.drawable.btn_chattingbackground_sel_big);
				} else {
					holder.img2.setSelected(false); 
					if(isChange){//如果是替换一张图片的 
						ToastManager.getInstance().show(PalmchatApp.getApplication(),R.string.change_one_only); 
					}else{
						ToastManager.getInstance().show(PalmchatApp.getApplication(),
								CommonUtils.replace(ReplaceConstant.TARGET_FOR_REPLACE,String.valueOf( max_selected) , getResources().getString(R.string.max_selected_img)));
					} 
					return;
				}
			}

			updateSelected();
		}
	}
	 
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		 new LocalImageViewPager(this,selectedAlbumPhotosLs,position)
                 .show(); 
		return true;
	}

 

	private void changeUi() {
		if (isShowZoomImg) {
			fl_bigimg.setVisibility(View.VISIBLE);
			imgChecked.setVisibility(View.VISIBLE);
			mGridView.setVisibility(View.GONE);
			tvOk.setVisibility(View.GONE);
			rl_bottom.setVisibility(View.GONE); 

		} else {
			rl_bottom.setVisibility(View.VISIBLE);
			tvOk.setVisibility(View.VISIBLE);
			fl_bigimg.setVisibility(View.GONE);
			mGridView.setVisibility(View.VISIBLE);
			imgChecked.setVisibility(View.GONE);
			if(imgZoom!=null ){
				imgZoom.recycleBitmap();
			}
			mAdapter.notifyDataSetChanged();
		}

		updateSelected();

	}

	private void createAlbumDialog() {
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

		mDialog = new Dialog(this, R.style.CustomAlbumDialogTheme);
		View mView = LayoutInflater.from(this).inflate(R.layout.dialog_photo_album_type, null);
		mView.setMinimumWidth(10000);
		ListView lv = (ListView) mView.findViewById(R.id.lv); 
		mAlbumAdapter = new AlbumAdapter();
		lv.setAdapter(mAlbumAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position != preAlbumPosition) {
					mainLs.get(preAlbumPosition).isSelected = false;
					mainLs.get(position).isSelected = true;
					preAlbumPosition = position;
					mAlbumAdapter.notifyDataSetChanged();
				}

				tv_album_type.setText(mainLs.get(position).dir);

				if (mDialog != null)
					mDialog.dismiss();

				if (position == 0) {
					isAllPhotos = true;
					showAllPhotos();
				} else {
					isAllPhotos = false;
					List<String> ls = mainLs.get(position).filePathes;
					showSelectedAlbumPhotos(ls);
				}

			}
		});

		 
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(outMetrics.widthPixels, (int) (outMetrics.heightPixels * 0.7));
		Window window = mDialog.getWindow();
		WindowManager.LayoutParams localLayoutParams = window.getAttributes();
		localLayoutParams.gravity = Gravity.BOTTOM;
		localLayoutParams.x = 0;
		localLayoutParams.y = ImageUtil.dip2px(this, 48);
		window.setAttributes(localLayoutParams);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setCancelable(true);
		// mDialog.setContentView(mView);
		mDialog.addContentView(mView, params);
		mDialog.show();

	}

	private class AlbumAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mainLs.size();
		}

		@Override
		public Object getItem(int position) {
			return mainLs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(PreviewImageActivity.this).inflate(R.layout.dialog_photo_album_type_item, null);
				holder.img = (ImageView) convertView.findViewById(R.id.imageview);
				holder.tv_album_name = (TextView) convertView.findViewById(R.id.tv_album_name);
				holder.tv_numbers = (TextView) convertView.findViewById(R.id.tv_numbers);
				holder.img_checked = (ImageView) convertView.findViewById(R.id.img_checked);
				 
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ImageFolderInfo imgInfo = mainLs.get(position);

			if (imgInfo.isSelected)
				holder.img_checked.setVisibility(View.VISIBLE);
			else
				holder.img_checked.setVisibility(View.GONE);

			holder.tv_album_name.setText(imgInfo.dir);
			holder.tv_numbers.setText(String.valueOf(imgInfo.pisNum));
			if (CommonUtils.isEmpty(imgInfo.path) && position == 0) {
				holder.img.setBackgroundResource(R.drawable.app_icon);
			} else { 
				ImageManager.getInstance().DisplayLocalImage(holder.img,imgInfo.path, R.color.color_login_bg,R.color.color_login_bg,null);
			}

			return convertView;
		}

		class ViewHolder {
			ImageView img, img_checked;
			TextView tv_album_name, tv_numbers;
			 
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return goBack();
		}
		return super.onKeyDown(keyCode, event);
	} 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MessagesUtils.CAMERA && resultCode == Activity.RESULT_OK) {
			PalmchatLogUtils.e("WXL", "PreviewImageActivity onActivityResult obj="+this.toString());
			
			if (cameraFilename == null) {//有时会遇到camera拍照后当前类被回收的情况，所以文件名要保存
				PalmchatLogUtils.e("WXL", "PreviewImageActivity onActivityResult  cameraFilename=======null");
				cameraFilename = SharePreferenceService.getInstance(this).getFilename(); 
			} else {
				SharePreferenceService.getInstance(this).clearFilename();
			}

			ArrayList<String> ls = new ArrayList<String>();
			Set<String> mapKey = mHashMap.keySet();
			Iterator<String> iterator = mapKey.iterator();
			while (iterator.hasNext()) {
				String imgPath = iterator.next();
				if(!mHashMap_locked.containsKey(imgPath)){ //不锁定的时候 才加进来
					ls.add(imgPath);
				}
			}
		/*	if(isBroadcastEditPic&&isTecno){//Tecno手机不一样 他会自动生成一张照片到相册,  照片文件名为当时的时间
				String tecnoCameraFilename = ClippingPicture.getCurrentFilename();
				mIntent.putExtra("tecnoCameraFilename", tecnoCameraFilename);
			}*/
			if(isBroadcastEditPic){//如果是编辑广播要保存起来用于编辑
				String realCameraPath=null;
				if(isTecno){//tecno手机自己会生成不用拷贝
					realCameraPath=getTecnoCameraFilePath();//计算出Tecno手机自动生成的这张相片的保存路径
				}else {//如果不是Tecno手机 那就要copy一份到DCIM目录
					 realCameraPath= pathToInfo(cameraFilename);// if broadcast edit camera, copy pic to DCIM 把搬到相册目录的图拿来做滤镜处理才是对的
				}
				if(!TextUtils.isEmpty( realCameraPath) ){
					cameraFilename=realCameraPath;
				}
			}else if(!isBroadcastEditPic){
				PalmchatApp.getApplication().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
						Uri.parse("file://" + RequestConstant.CAMERA_CACHE  + cameraFilename)));
			}

			Intent mIntent = new Intent();
			mIntent.putExtra("cameraFilename", cameraFilename);
			mIntent.putExtra("isChange", isChange);
			mIntent.putStringArrayListExtra("photoLs", ls);
			mIntent.putExtra("postion", change_postion);
			setResult(RESULT_OK, mIntent);
			finish();

		}
	}

	private String getTecnoCameraFilePath() {
		String tecnoCameraFilename = ClippingPicture.getCurrentFilename();
		long timeStart = ClippingPicture.getCameraTime(cameraFilename);//拍照起始时间
		long timeEnd = ClippingPicture.getCameraTime(tecnoCameraFilename);//拍照结束时间
		File file = new File(strDCIMPath  );
		if (file.exists()) {

			File[] subFile = file.listFiles();
			for (int i = 0; i < subFile.length; i++) {
				if (subFile[i].isDirectory()&&subFile[i].getName().toLowerCase().equals("camera")) {
					file = new File(subFile[i].getAbsolutePath()  );
					 subFile = file.listFiles();
					break;
				}
			}
			for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
				// 判断是否为文件夹
				if (!subFile[iFileLength].isDirectory()) {
					String filename = subFile[iFileLength].getName();
					long timeCam = ClippingPicture.getCameraTime(filename);
					if (timeCam > 0 && timeCam > timeStart
							&& timeCam < timeEnd) {
						return subFile[iFileLength].getAbsolutePath();
					}
				}
			}
		}
		return null;
	}

	private void  initGridView () {
		mAdapter = new GridAdapter();
		mGridView.setAdapter(mAdapter);
	}

	
}
