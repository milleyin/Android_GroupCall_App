package com.afmobi.palmchat.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.facebook.internal.Utility;




public class ImageUtil {
	
	private static final String TAG = ImageUtil.class.getCanonicalName();
	
	public   static int DISPLAYH = PalmchatApp.getApplication().getResources()
			.getDisplayMetrics().heightPixels;

	public   static int DISPLAYW = PalmchatApp.getApplication().getResources()
			.getDisplayMetrics().widthPixels;

	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		return zoomBitmapEx(bitmap, w, h, 0);
	}
	
	public static Bitmap zoomBitmapEx(Bitmap bitmap, int w, int h, int rotation) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		if( 0 != rotation){
			matrix.setRotate(rotation);
		}
		
		if( 90 == rotation || 270 == rotation){
			scaleHeight   = ((float) w / width);
			scaleWidht = ((float) h / height);
		}else{
			scaleWidht = ((float) w / width);
			scaleHeight = ((float) h / height);
		}
		matrix.postScale(Math.min(1, scaleWidht), Math.min(1, scaleHeight));
		Bitmap newbmp = bitmap;
		try {
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		Log.e("zoomBitmap", newbmp.getWidth() + "->" + newbmp.getHeight());
		return newbmp;
	}
	
	public static Bitmap zoomBitmapEx2(Bitmap bitmap, int rotation) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int w = 0;
		int h = 0;
		
		int limit = 160;
		int zoom_px = 0;
		
		if (PalmchatApp.getApplication().getWindowWidth() >= 1080) {
			 limit = 512;
		}else if (PalmchatApp.getApplication().getWindowWidth() >= 720) {
			 limit = 320;
		}else if (PalmchatApp.getApplication().getWindowWidth() >= 480) {
			 limit = 240;
		}else if (PalmchatApp.getApplication().getWindowWidth() >= 320) {
			 limit = 160;
		}
		
		if (bitmap.getWidth() >= limit || bitmap.getHeight() >= limit) {
	        if ( bitmap.getWidth() >= bitmap.getHeight()){
		         zoom_px = (int)Math.round(( bitmap.getHeight() * limit * 1.0 /  bitmap.getWidth()));  
//		         imageView.setLayoutParams(new FrameLayout.LayoutParams( limit , zoom_px));
		         w = limit;
		         h = zoom_px;
//		         Log.e("zoom_px", "imageView.getWidth()= "+imageView.getWidth()+",imageView.getHeight()= "+imageView.getHeight());
	        }  
	        else   
	        {  
		         zoom_px = (int)Math.round(( bitmap.getWidth() * limit * 1.0 /  bitmap.getHeight()));  
//		         imageView.setLayoutParams(new FrameLayout.LayoutParams(zoom_px, limit));
		         w = zoom_px;
		         h = limit;
	        }
		}else {
//			 imageView.setLayoutParams(new FrameLayout.LayoutParams( bitmap.getWidth() , bitmap.getHeight()));
			 w = bitmap.getWidth();
	         h = bitmap.getHeight();
		}
		
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		if( 0 != rotation){
			matrix.setRotate(rotation);
		}
		
		if( 90 == rotation || 270 == rotation){
			scaleHeight   = ((float) w / width);
			scaleWidht = ((float) h / height);
		}else{
			scaleWidht = ((float) w / width);
			scaleHeight = ((float) h / height);
		}
		matrix.postScale(Math.min(1, scaleWidht), Math.min(1, scaleHeight));
		Bitmap newbmp = bitmap;
		try {
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		Log.e("zoomBitmap", newbmp.getWidth() + "->" + newbmp.getHeight());
		return newbmp;
	}




	public static Bitmap getImageThumbnail(Bitmap bitmap, int width, int height) {  
		
        try {
	        int h = bitmap.getHeight();  
	        int w = bitmap.getWidth();  
	        int beWidth = w / width;  
	        int beHeight = h / height;  
	        int be = 1;  
	        if (beWidth < beHeight) {  
	            be = beWidth;  
	        } else {  
	            be = beHeight;  
	        }  
	        if (be <= 0) {  
	            be = 1;  
	        }
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
        return bitmap;  
    }
	



	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	
	public static Bitmap picZoom(Bitmap bmp, int width, int height) {
		if (bmp == null) {
			return null;
		}
		int bmpWidth = bmp.getWidth();
		int bmpHeght = bmp.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale((float) width / bmpWidth, (float) height / bmpHeght);
		Bitmap bitmap = bmp;
		try {
			bitmap = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, true);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bitmap;
	}




	
	/**
	 * 获取图片文件的最小尺寸值
	 * @param photoFile
	 * @return
	 */
    public static Pair<File, Integer> getImageFileAndMinDimension(File photoFile) {
        if (photoFile != null) {
            InputStream is = null;
            try {
                is = new FileInputStream(photoFile);

                // We only want to get the bounds of the image, rather than load the whole thing.
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, null, options);

                return new Pair<File, Integer>(photoFile, Math.min(options.outWidth, options.outHeight));
            } catch (Exception e) {
                return null;
            } finally {
                Utility.closeQuietly(is);
            }
        }
        return null;
    }


	public static Bitmap getBitmapFromFile(String filepath) {
		Bitmap bitmap = null;
		try {
			if (!StringUtil.isNullOrEmpty(filepath)) {
				File headFile = new File(filepath);
				if (headFile.exists()) {
					if (bitmap == null) {
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 1;
						options.inJustDecodeBounds = true;
						BitmapFactory.decodeFile(filepath, options);

						// 480 * 800
//						options.inSampleSize = computeSampleSize(options, 480, 800);
						options.inJustDecodeBounds = false;
						options.inDither = false;
						options.inPurgeable = true;
						options.inInputShareable = true;
						options.inPreferredConfig = Bitmap.Config.RGB_565;
						bitmap = BitmapFactory.decodeFile(filepath, options);

						if (bitmap == null) {
							headFile.delete();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}