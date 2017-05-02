package com.afmobi.palmchat.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.customview.ClipView;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;


public class BitmapUtils {
	
//	public static int WIDTH = 480;
//	public static int HEIGHT = 640;

	
   public static int readBitmapDegree(String path) {  
       int degree  = 0;  
       try {  
               ExifInterface exifInterface = new ExifInterface(path);  
               int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);  
               switch (orientation) {  
               case ExifInterface.ORIENTATION_ROTATE_90:  
                       degree = 90;  
                       break;  
               case ExifInterface.ORIENTATION_ROTATE_180:  
                       degree = 180;  
                       break;  
               case ExifInterface.ORIENTATION_ROTATE_270:  
                       degree = 270;  
                       break;  
               }  
       } catch (IOException e) {  
               e.printStackTrace();  
       }  
       return degree;  
   } 

   
   /**
	 * picture zoom-->300KB
	 * hgm  2013-10-31
	 * @param
	 * @return picture path
	 * @throws IOException
	 */
	public static String imageCompressionAndSavePhotowall(String oldFileName, String newFileName) {
		String notExists = "";
		FileOutputStream fOut = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(oldFileName, options);

			// 480 * 800
			options.inSampleSize = computeSampleSize(options, 300, 600);

			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			Bitmap sourceBitmap = BitmapFactory.decodeFile(oldFileName, options);
			
			if (null != sourceBitmap) {
				/** 旋转图片 */
				int degree = BitmapUtils.readBitmapDegree(oldFileName);
				if (0 != degree) {
					Matrix matrix = new Matrix();
					matrix.setRotate(degree);
					Bitmap newbmp = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
					if (null != newbmp) {
						BufferedOutputStream bos;
						if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
							sourceBitmap.recycle();
							sourceBitmap = null;
						}
						bos = new BufferedOutputStream(new FileOutputStream(oldFileName));
						newbmp.compress(Bitmap.CompressFormat.JPEG, 60, bos);
						bos.flush();
						bos.close();
						sourceBitmap = newbmp;
					}
				}
			}

			File newFile = new File(newFileName);
			if (null != sourceBitmap) {
				fOut = new FileOutputStream(newFile);
				sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 60, fOut);
				fOut.flush();
				sourceBitmap.recycle();
			}else{
				return notExists;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return notExists;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			if (null != fOut) {
				try {
					fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
					return notExists;
				}
			}
		}
		return newFileName;
	}
   
   /**
	 * picture zoom-->300KB
	 * hgm  2013-10-31
	 * @param
	 * @return picture path
	 * @throws IOException
	 */
	public static String imageCompressionAndSave(String oldFileName, String newFileName) {
		String notExists = "";
		FileOutputStream fOut = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(oldFileName, options);

			// 480 * 800
			options.inSampleSize = computeSampleSize(options, 480, 800);

			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			Bitmap sourceBitmap = BitmapFactory.decodeFile(oldFileName, options); 
			if (null != sourceBitmap) {
				/** 旋转图片 */
				int degree = BitmapUtils.readBitmapDegree(oldFileName);
				if (0 != degree) {
					Matrix matrix = new Matrix();
					matrix.setRotate(degree);
					Bitmap newbmp = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
					if (null != newbmp) {
						BufferedOutputStream bos;
						if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
							sourceBitmap.recycle();
							sourceBitmap = null;
						}
						bos = new BufferedOutputStream(new FileOutputStream(oldFileName));
						newbmp.compress(Bitmap.CompressFormat.JPEG, 60, bos);
						bos.flush();
						bos.close();
						sourceBitmap = newbmp;
					}
				}
			}
			File newFile = new File(newFileName);
			if (null != sourceBitmap) {
				fOut = new FileOutputStream(newFile);
				sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 60, fOut);
				fOut.flush();
				sourceBitmap.recycle();
			}else{
				return notExists;
			}
		} catch (Exception e) {
			e.printStackTrace();
			newFileName=null;
			return notExists;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			newFileName=null; 
		} finally {
			if (null != fOut) {
				try {
					fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
					return notExists;
				}
			}
		}
		return newFileName;
	}
	/**
	 *  
	 * @param sourceBitmap
	 * @param newFileName
	 * @return
	 */
	public static String imageSaveToFile(Bitmap sourceBitmap, String newFileName) {
		String notExists = "";
		FileOutputStream fOut = null;
		try {  
			File newFile = new File(newFileName);
			if (null != sourceBitmap) { 
				fOut = new FileOutputStream(newFile);
				sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.flush();
				sourceBitmap.recycle();
			}else{
				return notExists;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return notExists;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			if (null != fOut) {
				try {
					fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
					return notExists;
				}
			}
		}
		return newFileName;
	}
	
	private static int computeSampleSize(BitmapFactory.Options options, int targeW, int targeH) {
		int w = options.outWidth;
		int h = options.outHeight;
		float candidateW = w / targeW;
		float candidateH = h / targeH;

		float candidate = Math.max(candidateW, candidateH);
		if (candidate == 0) {
			return 1;
		}
		if (candidate > 1) {
			if ((w > targeW) && (w / candidate) < targeW) {
				candidate -= 1;
			}
		}

		if (candidate > 1) {
			if ((h > targeH) && (h / candidate) < targeH) {
				candidate -= 1;
			}
		}

		return (int) candidate;
	}
	


	/**
	 * 保存一张图的指定切图到目的文件
	 * @param srcPath
	 * @param destPath
	 * @param width
	 * @param height
	 * @param sx
	 * @param sy
	 * @param clipWidth
	 * @param clipHeight
	 */
	public static String clipImage(String srcPath, String destPath, int width, int height, int sx, int sy, int clipWidth, int clipHeight) {
		boolean isSuccess=false;
		Bitmap sBitmap = //这里不能用UIL否则可能会把滤镜前的缓存图片转为缩图的情况ImageManager.getInstance().loadLocalImageSync(srcPath,false);//
		 ImageUtil.getBitmapFromFile(srcPath);
		if (sBitmap != null) {
		 	if(sx+clipWidth>sBitmap.getWidth()){//滤镜会缩小图片 这里先加个保护 周一跟中间件讨论
				clipWidth=sBitmap.getWidth()-sx;
			}
			if(sy+clipHeight>sBitmap.getHeight()){
				clipHeight=sBitmap.getHeight()-sy;
			}
			Bitmap clipBitmap = Bitmap.createBitmap(sBitmap, sx, sy, clipWidth, clipHeight);
			File f = new File(destPath);
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(f);
				clipBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.flush();
				out.close();
				isSuccess=true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 
			}

		}
		return isSuccess?destPath:null;

	}
	
	
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
}
