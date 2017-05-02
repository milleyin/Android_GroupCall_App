package com.afmobi.palmchat.util.universalimageloader.core;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.afmobi.palmchat.util.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.afmobi.palmchat.util.universalimageloader.cache.disc.naming.Md5FileNameGenerator;

public class CustomFileNameGenerator extends Md5FileNameGenerator{

	//获取下载地址 保存对应文件命
	//@param imageUri 请求url 不带session
	@Override
	public String generate(String imageUri) {
		
		/*ConcurrentHashMap<String,String> map = ImageManager.getInstance().getMap();
		Iterator<String> it = map.keySet().iterator(); 
		String name = "";
        while(it.hasNext())
        {
        	String key = (String)it.next();
        	if(imageUri.indexOf(key) >= 0)
        	{
	        	name = map.get(key);
	        	//map.remove(key);
	        	if(name.isEmpty())
	        		break;
	        	else
	        		return name;
        	}
        }*/
        return super.generate(imageUri);        				
	}
}
