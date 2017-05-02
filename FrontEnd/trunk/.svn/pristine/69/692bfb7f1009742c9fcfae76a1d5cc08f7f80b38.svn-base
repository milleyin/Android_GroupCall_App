package com.afmobi.palmchat.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.graphics.Bitmap;
import android.util.Log;

import com.afmobi.palmchat.log.PalmchatLogUtils;


public class ImageSplitUtils {

	private final String TAG = ImageSplitUtils.class.getCanonicalName();
	public static ImageSplitUtils imageSplitUtils = null;
	private ArrayList<String> split_file = new ArrayList<String>();
	private int index = 0;

	public ImageSplitUtils() {
	}

	public static ImageSplitUtils getInstance() {
		if (imageSplitUtils == null) {
			imageSplitUtils = new ImageSplitUtils();
		}
		return imageSplitUtils;

	}

	/**
	 * @param bitmap
	 * @param xPiece
	 * @param yPiece
	 * @param split_img_paht
	 * @param picName
	 * @throws IOException
	 */
	public void img_Split(Bitmap bitmap, int xPiece, int yPiece, String split_img_savepaht, String picName) throws IOException {
		try {
			if (bitmap != null && (0 != xPiece) && (0 != yPiece)) {
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				int pieceWidth = width / xPiece;
				int pieceHeight = height / yPiece;
				for (int i = 0; i < yPiece; i++) {
					for (int j = 0; j < xPiece; j++) {
						int xValue = j * pieceWidth;
						int yValue = i * pieceHeight;
						Bitmap b = Bitmap.createBitmap(bitmap, xValue, yValue, pieceWidth, pieceHeight);

						saveBitmap(b, split_img_savepaht, picName);
						if (b != null) {
							b.recycle();
							System.gc();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * save img
	 * 
	 * @throws IOException
	 */
	public void saveBitmap(Bitmap bm, String save_Img_path, String picname) throws IOException {
		File file = new File(save_Img_path);
		if (!file.exists()) {
			file.mkdir();
		}
		File f = new File(save_Img_path, index + "");
		FileOutputStream out = new FileOutputStream(f);
		bm.compress(Bitmap.CompressFormat.PNG, 100, out);
		out.flush();
		out.close();
		split_file.add(f.getPath());
		index++;
		Log.e(TAG, "saved img");

	}

	public ArrayList<String> getImg_SplitFile(Bitmap bitmap, int xPiece, int express_count, String split_img_savepaht, String picName) {
		File file = new File(split_img_savepaht);
		if (file.exists()) {
			File[] fileArray = file.listFiles();
			split_file.clear();
			index = 0;
			if (fileArray.length > 1) {
				for (int i = 0; i < express_count; i++) {
					split_file.add( split_img_savepaht + i);
				}
			} else {
				try {
				double tempYPiece =(double) (Double.valueOf(express_count) / Double.valueOf(xPiece));
				PalmchatLogUtils.e("getImg_SplitFile", Math.ceil(tempYPiece)+"");
				int yPiece=	(int) Math.ceil(tempYPiece);
					img_Split(bitmap, xPiece, yPiece, split_img_savepaht, picName);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		return split_file;
	}
//	public ArrayList<String> getImg_SplitFile(Bitmap bitmap, int xPiece, int yPiece, String split_img_savepaht, String picName) {
//		File file = new File(split_img_savepaht);
//		if (file.exists()) {
//			File[] fileArray = file.listFiles();
//			split_file.clear();
//			index = 0;
//			if (fileArray.length > 0) {
////				for (File f : fileArray) {
////					
////					if (f.isFile()) {
////						split_file.add(f.getPath());
////						orderByName(split_file, fileArray);
////						FileSortByNameUtils.getInstance().orderByName(split_file,fileArray);
////					}
////				}
//				int filecount = xPiece * yPiece;  
//				for (int i = 0; i < filecount; i++) {
//					split_file.add( split_img_savepaht + i);
//				}
//			} else {
//				try {
//					img_Split(bitmap, xPiece, yPiece, split_img_savepaht, picName);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//
//		}
//		
//		return split_file;
//	}
	
	//Sort by file name
	public ArrayList<String> orderByName(ArrayList<String> arrayList,String fliePath) {
	  List<File> files = Arrays.asList(new File(fliePath).listFiles());
	  Collections.sort(files, new Comparator<File>() {
		    @Override
		    public int compare(File o1, File o2) {
		        if (o1.isDirectory() && o2.isFile())
		            return -1;
		        if (o1.isFile() && o2.isDirectory())
		            return 1;
		        return o2.getName().compareTo(o1.getName());
		    }
	  });
	
	  for (File f : files) {
	     Log.e("f.getName", f.getName());
	     if (f.isFile()) {
	    	 arrayList.add(f.getPath());
		}
	  }
	return arrayList;
	}
}