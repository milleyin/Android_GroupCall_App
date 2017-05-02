package com.afmobi.palmchat.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

public class BrandXMLUtils {
	private static final String BRAND = "brand";
	private static final String ITEM = "item";
	private static final String NAME = "name";

	public static Map<String, List<String>> parse(Context context) {
		Map<String, List<String>> brandMap = new HashMap<String, List<String>>();
		InputStream inputStream = null;
		XmlPullParser xmlParser = Xml.newPullParser();
		try {
			inputStream = context.getResources().getAssets().open("brand.xml");
			xmlParser.setInput(inputStream, "utf-8");
			int evtType = xmlParser.getEventType();
			List<String> brands = null;
			String brandName = null;
			while (evtType != XmlPullParser.END_DOCUMENT) {
				switch (evtType) {
				case XmlPullParser.START_TAG:
					String tag = xmlParser.getName();
					if (tag.equalsIgnoreCase(BRAND)) {
						brands = new ArrayList<String>();
						brandName = xmlParser.getAttributeValue(0);
					} 
					if (tag.equalsIgnoreCase(ITEM)) {
						brands.add(xmlParser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					if (xmlParser.getName().equalsIgnoreCase(BRAND)) {
						brandMap.put(brandName, brands);
					}
					break;
				default:
					break;
				}
				evtType = xmlParser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return brandMap;
	}

}
