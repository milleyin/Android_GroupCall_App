package com.afmobi.palmchat.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.customview.ClipImageView;
import com.afmobi.palmchat.ui.customview.ClipView;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager.SystemBarConfig;

public class UploadPictureUtils {
	
	private PopupWindow pop;
	private static UploadPictureUtils uploadPictureUtils;
	
	public static UploadPictureUtils getInit(){
		if (uploadPictureUtils == null) {
			uploadPictureUtils = new UploadPictureUtils();
		}
		return uploadPictureUtils;
	}
	public void showClipPhoto_squre(final Bitmap bm, final View view, final Activity c ,final String sCameraFilename, final IUploadHead iuploadHead) {
		LayoutInflater inflater = LayoutInflater.from(c);
		// 引入窗口配置文件
		View v = inflater.inflate(R.layout.pop_clip_photo, null);
		final ClipImageView clipImageView =(ClipImageView) v.findViewById(R.id.src_pic);
		final ClipView clipView = (ClipView) v.findViewById(R.id.clipview);
		clipView.TYPE_START=ClipView.RECTANGULAR;
		clipImageView.setImageBitmap(bm);
		Button btn_save = (Button) v.findViewById(R.id.btn_save);
		Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
		btn_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pop != null && pop.isShowing()) {
					// 此处获取剪裁后的bitmap
					try{
						Bitmap clipBitmap = clipImageView.clip();
						String save_Img_path = RequestConstant.CAMERA_CACHE ;
						File file = new File(save_Img_path );
						if (!file.exists()) {
							file.mkdir();
						}
						File f = new File(save_Img_path,sCameraFilename);
						FileOutputStream out = new FileOutputStream(f);
						clipBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
						out.flush();
						out.close();
						if (iuploadHead != null) {
							iuploadHead.onUploadHead(f.getAbsolutePath(), sCameraFilename);
						}
					}catch (Exception e) {

					}
					if (pop != null && pop.isShowing()) {
						pop.dismiss();
					}
				}
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (iuploadHead != null) {
					iuploadHead.onCancelUpload();
				}
				if (pop != null && pop.isShowing()) {
					pop.dismiss();
				}
			}
		});
		try {
			pop = new PopupWindow(v, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
//		pop.setAnimationStyle(R.style.popwin_anim_style);
			// 实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
			// 需要设置一下此参数，点击外边可消失
//		pop.setBackgroundDrawable(dw);
			// 设置非PopupWindow区域可触摸
			pop.setOutsideTouchable(true);
			// 设置此 参数获得焦点，否则无法点击
			pop.setFocusable(true);
			pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			pop.update();
			//某些机型是虚拟按键的，所以要加上以下设置防止档住按键
			pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			// 设置显示的方位
			pop.showAtLocation(view, Gravity.FILL, 0, 0);
		}catch (Exception e){
			e.printStackTrace();//捕捉被回收的情况  这里被回收还要另外想办法处理 明天处理
			PalmchatLogUtils.e("WXL",e.getMessage());
		}


	}


	public void showClipPhoto(final Bitmap bm, final View view, final Activity c ,final String sCameraFilename, final IUploadHead iuploadHead) {
		LayoutInflater inflater = LayoutInflater.from(c);
		// 引入窗口配置文件

		View v = inflater.inflate(R.layout.pop_clip_photo, null);
		final ClipImageView clipImageView =(ClipImageView) v.findViewById(R.id.src_pic);
		clipImageView.setImageBitmap(bm);
		Button btn_save = (Button) v.findViewById(R.id.btn_save);
		Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pop != null && pop.isShowing()) {
					// 此处获取剪裁后的bitmap
					try{
						Bitmap clipBitmap = clipImageView.clip();
						String save_Img_path = RequestConstant.CAMERA_CACHE ;
						File file = new File(save_Img_path );
						if (!file.exists()) {
							file.mkdir();
						}
						File f = new File(save_Img_path,sCameraFilename);
						FileOutputStream out = new FileOutputStream(f);
						clipBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
						out.flush();
						out.close();
						if (iuploadHead != null) {
							iuploadHead.onUploadHead(f.getAbsolutePath(), sCameraFilename);
						}
//						uploadHead(f.getAbsolutePath(), sCameraFilename);
//						profile_photo_value.setImageBitmap(clipBitmap);
					}catch (Exception e) {

					}
					if (pop != null && pop.isShowing()) {
						pop.dismiss();
					}
				}
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (iuploadHead != null) {
					iuploadHead.onCancelUpload();
				}
				if (pop != null && pop.isShowing()) {
					pop.dismiss();
				}
			}
		});
		try {
			pop = new PopupWindow(v, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
//		pop.setAnimationStyle(R.style.popwin_anim_style);
			// 实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
			// 需要设置一下此参数，点击外边可消失
//		pop.setBackgroundDrawable(dw);
			// 设置非PopupWindow区域可触摸
			pop.setOutsideTouchable(true);
			// 设置此 参数获得焦点，否则无法点击
			pop.setFocusable(true);
			pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			pop.update();
			//某些机型是虚拟按键的，所以要加上以下设置防止档住按键
			pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			// 设置显示的方位
			pop.showAtLocation(view, Gravity.FILL, 0, 0);
		}catch (Exception e){
			e.printStackTrace();//捕捉被回收的情况  这里被回收还要另外想办法处理 明天处理
			PalmchatLogUtils.e("WXL",e.getMessage());
		}

	}

	
	public Bitmap smallImage(File f) {
		FileInputStream is = null;
		Bitmap bitmap = null;

		try {
			int len = 0;
			BitmapFactory.Options opts = new BitmapFactory.Options();// 获取缩略图显示到屏幕
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inPurgeable = true;
			opts.inInputShareable = true;
			is = new FileInputStream(f.getAbsolutePath());
			len = is.available();
			if (len > 100 * 100 * 100) {
				opts.inSampleSize = 4;
			} else if (len > 10 * 100 * 100) {
				opts.inSampleSize = 2;
			}
			bitmap = BitmapFactory.decodeStream(is, null, opts);
			is.close();
			opts = null;
			if (null != bitmap) {
				/** 旋转图片 */
				int degree = BitmapUtils.readBitmapDegree(f.getAbsolutePath());
				if (0 != degree) {
					Matrix matrix = new Matrix();
					matrix.setRotate(degree);
					Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
					if (null != newbmp) {
						BufferedOutputStream bos;
						if (bitmap != null && !bitmap.isRecycled()) {
							bitmap.recycle();
							bitmap = null;
						}
						bos = new BufferedOutputStream(new FileOutputStream(f.getAbsolutePath()));
						newbmp.compress(Bitmap.CompressFormat.JPEG, 60, bos);
						bos.flush();
						bos.close();
						bitmap = newbmp;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		if( null == bitmap){
			return null;
		}
		return bitmap;
	}
	
	
	 public File getFileFromUri(Uri uri, Context activity)
	 {
		 File file = null;
//		 Cursor actualimagecursor = null ;
//		 try {
//			 String[] proj = { MediaStore.Images.Media.DATA };
//			 
//			 actualimagecursor = ((Activity) activity).managedQuery(uri,proj,null,null,null);
//			 
//			 int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//			 
//			 actualimagecursor.moveToFirst();
//			 
//			 String img_path = actualimagecursor.getString(actual_image_column_index);
//			 file = new File(img_path);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}finally{
//			if (actualimagecursor != null) {
//				actualimagecursor.close();
//			}
//		}
		
		
		Cursor cursor = null;
		String path;
		if(uri == null){
			return null;
		}
		if(DefaultValueConstant.FILEMANAGER.equals(uri.getScheme())){
			path = uri.getEncodedPath();
		}else{
			cursor = activity.getContentResolver().query(uri, null,null, null, null);
			if(cursor == null || !cursor.moveToFirst()){
//			ToastManager.getInstance().show(context, );
				PalmchatLogUtils.println("onActivityResult  cursor  "+cursor+"  cursor.moveToFirst()  false");
				return null;
			}
			String authority = uri.getAuthority();
			
			if(!CommonUtils.isEmpty(authority) && authority.contains(DefaultValueConstant.FILEMANAGER)){
				path = cursor.getString(0);
			}else{
				path = cursor.getString(1);
			}
		}
			PalmchatLogUtils.e("path=", path); // 这个就是我们想要的原图的路径
			if(cursor != null){
				cursor.close();
			}
		file = new File(path);
		return file;
	 }
	 
	 public interface IUploadHead{
		 void onUploadHead(String path, String filename);
		 /*add by zhh 取消上传  */
		 void onCancelUpload();
	 }
}
