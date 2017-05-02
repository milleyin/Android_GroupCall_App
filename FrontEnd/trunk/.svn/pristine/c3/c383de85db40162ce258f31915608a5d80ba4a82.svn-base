package com.afmobi.palmchat.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;

public class ClippingPicture {
	
	public static final String TALK_FILES = RequestConstant.IMAGE_CACHE;
	
	public static String talkFullPicName = "";
	public static String talkPicName;
	
	
	public static String saveTalkBitmap(Bitmap bitmap ,AfPalmchat afPalmchat) {
		BufferedOutputStream bos;
		String smallFileName="";
		try {
			smallFileName = afPalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
			bos = new BufferedOutputStream(new FileOutputStream(TALK_FILES+smallFileName));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			talkPicName = smallFileName;
		}
		return talkPicName;
	}
	
	public static void saveTalkBitmap(Bitmap bitmap ,String filename) {
		File myCaptureFile = new File(TALK_FILES);
		if (!myCaptureFile.exists()) {
			myCaptureFile.mkdirs();
		}
		File f = new File(myCaptureFile + "/" + getCurrentTimeMillis() + "_" + filename);
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(f));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getCurrentTimeMillis() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'img'_yyyyMMdd_HHmmss");
		return dateFormat.format(date);
	}
	
	public static String getCurrentFilename() {
		return getCurrentTimeMillis() + ".jpg";
	}
	/**
	 * 
	 * @param time  img_yyyyMMdd_HHmmss.jpg 
	 * @return
	 */
	public static long getCameraTime(String time){
		long ltime=0;
		if(!TextUtils.isEmpty( time)&&time.endsWith(".jpg")&& (time.startsWith( "img_")||time.startsWith( "IMG_"))&&
				
				time.length()>=19){
			String _str=time.substring(8,time.indexOf(".jpg"));
			_str=_str.replace("_", "");
			
			try{
				 ltime= Long.parseLong(_str);
			}catch(Exception e ){
				e.printStackTrace();
			}
		}
		return ltime;
	}
	
	public static String saveTalkFileNames() {
		String filename = getCurrentFilename();
		talkPicName = filename;
		talkFullPicName = TALK_FILES + filename;
		return talkFullPicName;
	}
	
	
	

	public static Bitmap Resize(Bitmap bmp) {
		float scaleWidth = 1;
		float scaleHeight = 1;
		double scale = 0;

		int width = bmp.getWidth();
		int height = bmp.getHeight();
		if (width <= 600 && height <= 600) {
			return bmp;
		}else if(width <= 800 && height <= 800){
			scale = 0.8;
		}else if(width <= 960 && height <= 800){
			scale = 0.7;
		}else if(width <= 1200 && height <= 1600){
			scale = 0.6;
		}else if(width > 1200 || height > 1200){
			scale = 0.5;
		}
		scaleWidth = (float) (scaleWidth * scale);
		scaleHeight = (float) (scaleHeight * scale);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbm = bmp;
		try {
			newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix,
					true);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		
		return newbm;
	}
	
	
	
	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	
	
	public static Drawable bitmapToDrawable(Bitmap bmp){
		
		return new BitmapDrawable(bmp);
	}
	
	
	
    public static void deleteSDFile(String fileName) { 
        File file = new File(TALK_FILES + "//" + fileName); 
        if (file == null || !file.exists() || file.isDirectory()) {
        	
        }else{
        	file.delete();
        }
    } 
}
