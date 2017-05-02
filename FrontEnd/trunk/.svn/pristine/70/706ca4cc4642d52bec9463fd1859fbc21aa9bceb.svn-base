package com.afmobi.palmchat.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chattingroom.model.ImageFolderInfo;
import com.core.AfPaystoreCommon;
import com.core.AfPaystoreCommon.AfStoreDlProdInfo;
import com.core.AfPaystoreCommon.AfStoreDlProdInfoList;
import com.core.cache.CacheManager;

public class GifImageUtil {

	private final static String FILTER_MEDIA = ".nomedia";
	private LinkedList<String> extens=null;
	
	private boolean isScaned; //is scan
	
	/**
	 * all pictures folder
	 */
	private ArrayList<ImageFolderInfo> imageFolders = new ArrayList<ImageFolderInfo>();
	private ArrayList<ArrayList<ImageFolderInfo>> listFolders = new ArrayList<ArrayList<ImageFolderInfo>>();
	private ArrayList<ImageFolderInfo> downlaod_imageFolders = new ArrayList<ImageFolderInfo>();
	
	private int downloadType = 1;
	public LinkedList<String> getExtens(){
		if(extens == null){
			extens = new LinkedList<String>();
			extens.add("JPEG");
			extens.add("JPG");
			extens.add("PNG");
			extens.add("GIF");
			extens.add("BMP");
			extens.add("PAL");
		}
		return extens;
	}
	
	
	
	/**
	 * need to filter picture folder
	 * @param dirName
	 * @return
	 */
	public boolean isFilterDir(String dirName) {
		if(dirName.startsWith(".")) {
			return true;
		}
		return false;
	}
	
	public void getImageFilePath(String path) {

		File f = new File(path);
		File[] files = f.listFiles();
		ImageFolderInfo ifi = new ImageFolderInfo();
		ifi.dir = path;
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				final File ff = files[i];
				
				if (ff.isDirectory()) { 
					String dirName = ff.getName();
					if(isFilterDir(dirName)) {
						continue;
						
					}
					
					getImageFilePath(ff.getPath());
					
				} else {
					String fName = ff.getName();
//					if a dir contains .nomedia file, ignore it
					if(fName.contains(FILTER_MEDIA)) {
						break;
					}
					if (fName.indexOf(".") > -1) {
						String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toUpperCase();
						if (getExtens().contains(end)) {
							ifi.filePathes.add(ff.getPath());
						}
					} 
				}
			}
		}
		if (!ifi.filePathes.isEmpty()) {
			ifi.pisNum = ifi.filePathes.size();
			Collections.reverse(ifi.filePathes);
			String imagePath = ifi.filePathes.get(0);
			ifi.path = imagePath;
			imageFolders.add(ifi);
		}
//		PalmchatLogUtils.println("imageFolders.size "+imageFolders.size());
	}
	
	public ArrayList<ImageFolderInfo> getImageFolder() {
		return imageFolders;
	}
	
	public void clearImageFolder() {
		imageFolders.clear();
	}
	
	public ArrayList<ArrayList<ImageFolderInfo>> getListFolders() {
		return listFolders;
	}
	public void setListFolders(ArrayList<ArrayList<ImageFolderInfo>> listFolders) {
		this.listFolders = listFolders;
	}
	
	/**
	 * get sdcard REQUESTConstant.STORE_CACHE folder
	 */
	public void getFolder(EmojiParser emojiParser,String path,int type){
		if(emojiParser == null){
			return; 
		}
		HashMap<String, Object> hashmapGif = emojiParser.getEmojjGif();
		File f = new File(path);
		File[] files = f.listFiles();
		ImageFolderInfo ifi = new ImageFolderInfo();
		ifi.dir = path; 
		PalmchatLogUtils.e("gif", "ifi.dir="+ifi.dir);
		if (files != null) {
//			String myGifFolder = CacheManager.getInstance().getMyProfile().afId+"_";
			for (int i = 0; i < files.length; i++) {
				final File ff = files[i];
//				String folderName = ff.getName();
//				if(!folderName.startsWith(myGifFolder)){
//					continue;
//				}
				if (ff.isDirectory()) { 
//					String dirName = ff.getName();
					
//					if(isFilterDir(dirName)) {
//						continue;
//					}
					PalmchatLogUtils.e("gif", "ff.getPath()="+ff.getPath());
					getFolder(emojiParser,ff.getPath(),type);
					
				} else {
					String fName = ff.getName();
					PalmchatLogUtils.e("gif", "fName="+fName);
//					if (ff.getName().equals(CacheManager.getInstance().getScreenString())) {
	//					if a dir contains .nomedia file, ignore it
						if(fName.contains(FILTER_MEDIA)) {
							break;
						}
						if (fName.indexOf(".") > -1) {
							String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toUpperCase();
							PalmchatLogUtils.e("gif", "end="+end);
							if (getExtens().contains(end)) {
								String item_id_path = ff.getPath();
								ifi.filePathes.add(item_id_path);
								String path_index = item_id_path.substring(item_id_path.lastIndexOf("/")+1, item_id_path.lastIndexOf("."));
								String qualityString = "/"+CacheManager.getInstance().getScreenString();
								int end_index = item_id_path.lastIndexOf(qualityString);
								PalmchatLogUtils.println("GifImageUtil end_index :"+end_index);
								if(end_index != -1){
									String item_id = item_id_path.substring(item_id_path.lastIndexOf("store/")+6, end_index);
									String value = buildEmojj(item_id,path_index);
									HashMap<String, Object> fileMap = new HashMap<String, Object>();
									fileMap.put("IMAGE", item_id_path);
									String repString = CacheManager.getInstance().getMyProfile().afId+"/";
									value = value.replace(repString, "");
									fileMap.put("VALUE", value);
									if(item_id_path.contains("source")){
										hashmapGif.put(value, item_id_path);
									}
									ifi.filePathesMap.add(fileMap);
								}
	//							PalmchatLogUtils.println("ff.getPath()  "+item_id_path);
							}
						}
//					}
				}
			}
		}
		if (!ifi.filePathes.isEmpty()) {
			ifi.pisNum = ifi.filePathes.size();
			Collections.reverse(ifi.filePathes);
			String imagePath = ifi.filePathes.get(0);
			ifi.path = imagePath;
			PalmchatLogUtils.println("ff.getPath()2  "+imagePath);
			if (imagePath.contains(CacheManager.getInstance().getScreenString())) {
				imageFolders.add(ifi);
			}
			if(type == downloadType){
				downlaod_imageFolders.add(ifi);
			}
		}
//		PalmchatLogUtils.println("imageFolders.size "+imageFolders.size());
	}
	
	public synchronized void putDownLoadFolder(Context context,final String path,final Handler handler,final Intent intent){
//		PalmchatLogUtils.println("putDownLoadFolder  path:"+path);// /mnt/sdcard/afmobi/palmchat/store/a2055980/1ea8a80b6-f082-11e3-9569-02a5e9fe5ba0/
		final boolean is_face_change = intent.getBooleanExtra("is_face_change", true);
		final EmojiParser emojiParser = EmojiParser.getInstance(context);
		CacheManager.getInstance().getThreadPoolInstance().execute(new Thread(new Runnable() {
			public void run() {
				int size = imageFolders.size();
				if(size == 0){
					getFolder(emojiParser, path,downloadType);
				}else{
					for (int i = 0; i < size; i++) {
						ImageFolderInfo imageFolderInfo = imageFolders.get(i);
						if(!imageFolderInfo.dir.contains(path)){
							getFolder(emojiParser, path,downloadType);
							break;
						}
					}
				}
				int last_size = imageFolders.size();
				if(last_size > size){
					ArrayList<ImageFolderInfo> imageFoldersTemp = new ArrayList<ImageFolderInfo>();
					int size_download = downlaod_imageFolders.size();
					int a = 0;
					int max = 3;
					for (int i = 0; i < size_download; i++) {
						if(a%3==0){
							imageFoldersTemp = new ArrayList<ImageFolderInfo>();
						}
						a++;
						ImageFolderInfo imgInfo = downlaod_imageFolders.get(i);// Collections.reverse(arr);
						CommonUtils.getSortFaceGifPath(imgInfo);
//						PalmchatLogUtils.println("i "+i+"  "+imgInfo.toString());
						imageFoldersTemp.add(imgInfo);
						if(a == max){
							a = 0;
							listFolders.add(0,imageFoldersTemp);
//							PalmchatLogUtils.println("putDownLoadFolder  listFolders size:"+listFolders.size());
							imageFoldersTemp = null;
							downlaod_imageFolders.clear();
							if(is_face_change){
								CacheManager.getInstance().setIsFaceChange(true);
							}
							break;
						}
					}
				}
//				CacheManager.getInstance().resetSelectedPositionAfterAdded();
				if(handler != null && intent != null){
					handler.obtainMessage(0,intent).sendToTarget();
				}
			}
		}));
		
	}
	
	
	public synchronized void removeDownloadFolder(String path){
//		int size = imageFolders.size();
//		for (int i = 0; i < size; i++) {
//			ImageFolderInfo imageFolderInfo = imageFolders.get(i);
//			if(imageFolderInfo.dir.contains(path)){
//				
//			}
//		}
//		int count = 0;
		Iterator<ImageFolderInfo> sImageFolderListIterator = imageFolders.iterator();  
		while(sImageFolderListIterator.hasNext()){  
		    ImageFolderInfo imageFolderInfo = sImageFolderListIterator.next();  
		    if(imageFolderInfo.dir.contains(path)){  
		    	sImageFolderListIterator.remove();
//		    	count ++;
//		    	PalmchatLogUtils.println("removeDownloadFolder count:"+count);
		    }  
		}
		
//		int size = 0;
		Iterator<ArrayList<ImageFolderInfo>> sListIterator = listFolders.iterator();  
		while(sListIterator.hasNext()){  
			ArrayList<ImageFolderInfo> imageFolderInfoList = sListIterator.next();  
			imageFolders.remove(imageFolderInfoList);
			ImageFolderInfo imageFolderInfo = imageFolderInfoList.get(0);
			if(imageFolderInfo.dir.contains(path)){  
				sListIterator.remove();
				CacheManager.getInstance().setIsFaceChange(true);
				break;
//				size ++;
//				PalmchatLogUtils.println("removeDownloadFolder size:"+size);
			}  
		}
//		CacheManager.getInstance().resetSelectedPositionAfterRemoved();
		
	}
	
	public String buildEmojj(String item_id,String index) {
		if (item_id == null || item_id.length() <= 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
			result.append("[" + item_id + "," + index + "]");
		return result.toString();
	}
	
	public void sortOrder(){
		AfPaystoreCommon afPaystoreCommon = PalmchatApp.getApplication().mAfCorePalmchat.AfDBPaystoreProdinfoList();
		if(afPaystoreCommon != null){
			
			AfStoreDlProdInfoList afStoreDlProdInfoList = (AfStoreDlProdInfoList) afPaystoreCommon.obj;
			if(afStoreDlProdInfoList != null){
				ArrayList<ArrayList<ImageFolderInfo>> tempListFolders = new ArrayList<ArrayList<ImageFolderInfo>>();
				ArrayList<AfStoreDlProdInfo> list = afStoreDlProdInfoList.prod_list;
				int size = list.size();
				for (int i = 0; i < size; i++) {
					AfStoreDlProdInfo afStoreDlProdInfo = list.get(i);
					String item_id = afStoreDlProdInfo.item_id;
					Iterator<ArrayList<ImageFolderInfo>> sListIterator = listFolders.iterator();  
					while(sListIterator.hasNext()){  
						ArrayList<ImageFolderInfo> imageFolderInfoList = sListIterator.next();  
						ImageFolderInfo imageFolderInfo = imageFolderInfoList.get(0);
						if(imageFolderInfo.dir.contains(item_id)){  
							tempListFolders.add(imageFolderInfoList);
							break;
						}  
					}
				}
				if(tempListFolders.size() > 0){
					listFolders = tempListFolders;
				}
			}
		}
	}
}
