package com.afmobi.palmchat.ui.activity.chattingroom.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 图片文件夹实体
 * @author wk
 *
 */
public class ImageFolderInfo {
		public String path;
		public int pisNum = 0;
		public ArrayList<String> filePathes = new ArrayList<String>();
		public ArrayList<HashMap<String,Object>> filePathesMap = new ArrayList<HashMap<String,Object>>();
		public String dir;
		public String upperPath;
		public boolean isSelected = false;  //当前相册是否为选中状态
		@Override
		public String toString() {
		// TODO Auto-generated method stub
			String path = "";
			for (int i = 0; i < filePathes.size(); i++) {
				path = path +"\n"+ filePathes.get(i)+"\n";
			}
			path = "dir  "+dir + " "+path+"\n";
			return path;
		}
}
