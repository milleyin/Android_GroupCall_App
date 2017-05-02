package com.afmobi.palmchat.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chattingroom.model.ImageFolderInfo;
import com.afmobi.palmchat.ui.activity.tagpage.TagPageActivity;
import com.afmobi.palmchat.ui.customview.CutstomPopwindowEditText;
import com.afmobigroup.gphone.R;
import com.core.AfResponseComm.AfBroadCastTagInfo;
import com.core.cache.CacheManager;
import com.core.cache.CacheManager.Region;
import com.google.gson.JsonObject;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class EmojiParser {
	HashMap<String, ArrayList<HashMap<String, Object>>> emojj;
	HashMap<String, Integer> data = new HashMap<String, Integer>();
	HashMap<String, Object> emojjGif = new HashMap<String, Object>();
	HashMap<String, Drawable> emojDrawable = new HashMap<String, Drawable>();

	private static EmojiParser mParser;

	public static final String SUN = "emotion";
	public static final String FACE = "default";
	public static final String FIRE = "fire";
	public static final String BLACK = "black";
	public static final String TYPE = "type";
	public static final String GIF = "gif";
	public static final int MAXTAGSCOUNT=7;
	public static String PREFIX = "emojj_";
	String regex = "\\[(.*?)\\]";
	public static final  String regTags="#([\\u4e00-\\u9fa5\\w\\-]){1,50}";//"#[a-zA-Z0-9]{1,20}";//判断是否是Tags的正则表达式
	static Pattern pattern;
	private static Pattern patternTags;
	Resources resource;
	LayoutInflater mInflater;
	private Context mContext;


	private EmojiParser(Context mContext) {
		pattern = Pattern.compile(regex);
		patternTags= Pattern.compile(regTags);
		resource = mContext.getResources();
		this.mContext = mContext;
		readMap(mContext);
		mInflater = LayoutInflater.from(mContext);
		// String myAfid = CacheManager.getInstance().getMyProfile().afId;
		// if(!TextUtils.isEmpty(myAfid)){
		// readGif(mContext,myAfid,null,null);
		// }

		color_tag_normal=mContext.getResources().getColor(R.color.log_blue);
		color_tag_pa=mContext.getResources().getColor(R.color.tags_pa);// #17a5ef  #f69309
	}

//	public static boolean hasEmojj(String text) {
//		Matcher matcher = pattern.matcher(text);
//		return matcher.find();
//	}

	public static EmojiParser getInstance(Context mContext) {
		if (mParser == null) {
			mParser = new EmojiParser(mContext);
		}
		return mParser;
	}
	/**
	 * 解析表情
	 * @param text
	 * @return
	 */
	public CharSequence parse(String text) {
		if (text == null) {
			text = "";
		}
		Matcher matcher = pattern.matcher(text);
		SpannableStringBuilder sBuilder = new SpannableStringBuilder(text);
		Drawable drawable = null;
		ImageSpan span = null;
		String emo = "";
		while (matcher.find()) {
			emo = matcher.group();
			try {
				// String ttt = emo.substring(emo.indexOf("]") + 1,
				// emo.lastIndexOf("["));
				// Log.e("ttt", emo);
				int id = data.get(emo);
				// Log.e("id", id+"");
				if (id != 0) {
					drawable = resource.getDrawable(id);
					drawable.setBounds(0, 0, CommonUtils.emoji_w_h(PalmchatApp.getApplication()), CommonUtils.emoji_w_h(PalmchatApp.getApplication()));
					span = new ImageSpan(drawable);
					sBuilder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			} catch (Exception e) {
				continue;
			}
		}
		return sBuilder;
	}

	/**
	 * 是否含有表情
	 * @param text
	 * @return
	 */
	public boolean hasEmotions(String text) {
		Matcher matcher = pattern.matcher(text);
		String emo = "";
		while (matcher.find()) {
			emo = matcher.group();
			try {
				int id = data.get(emo);
				if (id != 0) {
					return true;
				}
			} catch (Exception e) {
//				break;
				//无法解析该表情
			}
		}
		return false;
	}

	/**
	 * 解析表情，带有名字
	 * @param text
	 * @param name
	 * @param size
	 * @return
	 */
	public CharSequence parse(String text, String name, int size) {
		if (null == text) {
			text = "";
		}
		if (null == name) {
			name = "";
		}
		// name:ddddd | name那么 Reply @xxxx:
		Matcher matcher = pattern.matcher(text);
		SpannableStringBuilder sBuilder = new SpannableStringBuilder(text);
		sBuilder.setSpan(new ForegroundColorSpan(PalmchatApp.getApplication().getResources().getColor(R.color.color_nice_blue)), 0, name.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		Drawable drawable = null;
		ImageSpan span = null;
		String emo = "";
		while (matcher.find()) {
			emo = matcher.group();
			try {
				// String ttt = emo.substring(emo.indexOf("]") + 1,
				// emo.lastIndexOf("["));
				// Log.e("ttt", emo);
				int id = data.get(emo);
				// Log.e("id", id+"");
				if (id != 0) {
					drawable = resource.getDrawable(id);
					drawable.setBounds(0, 0, size, size);
					span = new ImageSpan(drawable);
					sBuilder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			} catch (Exception e) {
				//无法解析该表情
			}
		}
		return sBuilder;
	}

	/**
	 * zhh 将评论内容中表情转换为图片，并进行事件设置
	 *
	 * @param text
	 * @param name
	 * @param size
	 * @param nameOnClickListener
	 * @param contentOnClickListener
	 * @return
	 */
	public CharSequence parse(String text, String name, int size, final View.OnClickListener nameOnClickListener, final View.OnClickListener contentOnClickListener) {
		if (null == text) {
			text = "";
		}
		if (null == name) {
			name = "";
		}
		ClickableSpan nameClickableSpan = new ClickableSpan() {
			@Override
			public void updateDrawState(TextPaint ds) {
				/* 设置文字颜色 */
				ds.setColor(PalmchatApp.getApplication().getResources().getColor(R.color.color_nice_blue));
				/* 去掉文字下划线 */
				ds.setUnderlineText(false);

			}

			@Override
			public void onClick(View widget) {
				nameOnClickListener.onClick(widget);
			}
		};

		ClickableSpan contentClickableSpan = new ClickableSpan() {
			@Override
			public void updateDrawState(TextPaint ds) {
				/* 设置文字颜色 */
				ds.setColor(PalmchatApp.getApplication().getResources().getColor(R.color.black));
				/* 去掉文字下划线 */
				ds.setUnderlineText(false);
			}

			@Override
			public void onClick(View widget) {

				contentOnClickListener.onClick(widget);
			}
		};

		// name:ddddd | name那么 Reply @xxxx:
		Matcher matcher = pattern.matcher(text);
		SpannableStringBuilder sBuilder = new SpannableStringBuilder(text);
		/* 设置名字点击事件 */
		sBuilder.setSpan(nameClickableSpan, 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		Drawable drawable = null;
		ImageSpan span = null;
		String emo = "";
		while (matcher.find()) {
			emo = matcher.group();
			try {
				int id = data.get(emo);
				if (id != 0) {
					drawable = resource.getDrawable(id);
					drawable.setBounds(0, 0, size, size);
					span = new ImageSpan(drawable);
					sBuilder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			} catch (Exception e) {
				//无法解析该表情
			}
		}
		/* 设置评论内容点击事件 */
		if (text.length() > name.length()) { // 如果评论内容不为空时
			sBuilder.setSpan(contentClickableSpan, name.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return sBuilder;
	}

	public CharSequence parse(String text, int size) {
		if (null == text) {
			text = "";
		}
		Matcher matcher = pattern.matcher(text);
		SpannableStringBuilder sBuilder = new SpannableStringBuilder(text);

		Drawable drawable = null;
		ImageSpan span = null;
		String emo = "";
		while (matcher.find()) {
			emo = matcher.group();
			try {
				// String ttt = emo.substring(emo.indexOf("]") + 1,
				// emo.lastIndexOf("["));
				// Log.e("ttt", emo);
				int id = data.get(emo);
				// Log.e("id", id+"");
				if (id != 0) {
					drawable = resource.getDrawable(id);
					drawable.setBounds(0, 0, size, size);
					span = new ImageSpan(drawable);
					sBuilder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			} catch (Exception e) {
				//   无法解析该表情
			}
		}
		return sBuilder;
	}
	/**
	 * 处理tags 转换
	 * @param context
	 * @param text
	 * @param size
	 * @return
	 */
	/*public CharSequence parseEmojiTags(final Context context,String text, int size) {
		if (null == text) {
			text = "";
		}
		final Matcher matcherTags = patternTags.matcher(text);
		SpannableStringBuilder sBuilder = new SpannableStringBuilder(text);

		 int tagsCount=0;
		while (matcherTags.find()) {

			try {
				final String tag=matcherTags.group();
				sBuilder.setSpan(new ClickableSpan() {

				    @Override
				    public void onClick(View widget) {
				    	 ToastManager.getInstance().show(context, tag);
				    }

				    @Override
				    public void updateDrawState(TextPaint ds) {
				   super.updateDrawState(ds);
				   		ds.setColor(Color.BLUE); // 设置文本颜色
					    // 去掉下划线
//					    ds.setUnderlineText(false);
				    }
				}
				, matcherTags.start(), matcherTags.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			} catch (Exception e) {
				break;
			}
			tagsCount++;
			if(tagsCount>=MAXTAGSCOUNT){
				break;
			}
		}

		Matcher matcher = pattern.matcher(text);
//		SpannableStringBuilder sBuilder = new SpannableStringBuilder(text);

		Drawable drawable = null;
		ImageSpan span = null;
		String emo = "";
		while (matcher.find()) {
			emo = matcher.group();
			try {
				int id = data.get(emo);
				if (id != 0) {
					drawable = resource.getDrawable(id);
					drawable.setBounds(0, 0, size, size);
					span = new ImageSpan(drawable);
					sBuilder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			} catch (Exception e) {
				break;
			}
		}
		return sBuilder;
	}*/

	private int color_tag_normal,color_tag_pa;// #17a5ef  #f69309
	public CharSequence parseEmojiTags(final Context context,String text,final ArrayList<AfBroadCastTagInfo> taglist, int size) {
		if(taglist.isEmpty()){//如果tags为空 那就不用考虑了
			return parse(text, size);
		}
		if (null == text) {
			text = "";
		}
		String reg="";
		//用于按tag字符长度从大到小排序  排序是为了解决#girl  #girlfriend时  girlfriend会被判定为只有girl 的问题
		ArrayList<AfBroadCastTagInfo> taglistResort=new ArrayList<AfBroadCastTagInfo>();
		for(int i=0;i<taglist.size();i++){
			if(i==0){
				taglistResort.add(taglist.get(i));
			}else  {
				boolean minValue=true;
				for(int j=0;j<taglistResort.size();j++){
					if(!TextUtils.isEmpty( taglist.get(i).tag)&&
							!TextUtils.isEmpty( taglistResort.get(j).tag)&&
							taglist.get(i).tag.length()>taglistResort.get(j).tag.length()){
						taglistResort.add(j,taglist.get(i));
						minValue=false;
						break;
					}
				}
				if(minValue){
					taglistResort.add( taglist.get(i));
				}
			}
		}
		final ArrayList<String> arrlistPa=new ArrayList<String>();
		AfBroadCastTagInfo tagInfo=null;
		for(int i=0;i<taglistResort.size();i++){
			tagInfo=taglistResort.get(i);
			if(!TextUtils.isEmpty( tagInfo.tag)){
				reg+=tagInfo.tag;
				if(i<taglistResort.size()-1){
					reg+="|";
				}
			}
			if(!TextUtils.isEmpty( tagInfo.pa)){
				arrlistPa.add(tagInfo.tag);
			}
		}
		Pattern patternTagsList= Pattern.compile(reg);
		final Matcher matcherTags = patternTagsList.matcher(text);
		SpannableStringBuilder sBuilder = new SpannableStringBuilder(text);

		int tagsCount=0;
		while (matcherTags.find()) {

			try {
				final String tag=matcherTags.group();
//				final int index=tagsCount;
				sBuilder.setSpan(new ClickableSpan() {

									 @Override
									 public void onClick(View widget) {
//				    	 ToastManager.getInstance().show(context, tag);
										 Bundle bundle = new Bundle();
										 bundle.putString(IntentConstant.TITLENAME, tag );
										 CommonUtils.to(context, TagPageActivity.class, bundle);

									 }

									 @Override
									 public void updateDrawState(TextPaint ds) {
										 super.updateDrawState(ds);
										 // 去掉下划线
										 ds.setUnderlineText(false);
										 int _inx=arrlistPa.indexOf(tag) ;
										 if( _inx>=0 ){
											 ds.setColor(color_tag_pa ); // 设置文本颜色 #f69309
										 }else{
											 ds.setColor(color_tag_normal); // 设置文本颜色 #17a5ef
										 }
									 }
								 }
						, matcherTags.start(), matcherTags.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
//			tagsCount++;
			/*
			if(tagsCount>=MAXTAGSCOUNT){
				break;
			}*/
		}

		Matcher matcher = pattern.matcher(text);
//		SpannableStringBuilder sBuilder = new SpannableStringBuilder(text);

		Drawable drawable = null;
		ImageSpan span = null;
		String emo = "";
		while (matcher.find()) {
			emo = matcher.group();
			try {
				int id = data.get(emo);
				if (id != 0) {
					drawable = resource.getDrawable(id);
					drawable.setBounds(0, 0, size, size);
					span = new ImageSpan(drawable);
					sBuilder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			} catch (Exception e) {
				//无法解析该表情
			}
		}
		return sBuilder;
	}
	public HashMap<String, ArrayList<HashMap<String, Object>>> getEmojj() {
		return emojj;
	}

	private void readMap(final Context mContext) {
		String packageName = mContext.getPackageName();
		Log.e("packageName", packageName);
		if (emojj == null || emojj.size() == 0) {
			emojj = new HashMap<String, ArrayList<HashMap<String, Object>>>();


			AssetManager assetManager = PalmchatApp.getApplication().getAssets();

			String strResponse = "";
			try {
				InputStream ims = assetManager.open("emotion.json");
				strResponse = FileUtils.getStringFromInputStream(ims);
			} catch (IOException e) {
				e.printStackTrace();
			}

			JSONObject _json;
			try {
				_json = new JSONObject(strResponse);
				ArrayList<HashMap<String, Object>> itemResource = new ArrayList<HashMap<String, Object>>();
				JSONArray emotionArr= _json.getJSONArray(    SUN);
				if(emotionArr!=null){
					JSONObject _jobj=null;
					for(int i=0;i<emotionArr.length();i++){
						HashMap<String, Object>   hashmap_cell=new HashMap<String, Object> ();
						_jobj=(JSONObject) emotionArr.get(i) ;
						String value="["+_jobj.getString("key")+"]";
						hashmap_cell.put("VALUE",value );
						hashmap_cell.put("LOCALNAME","["+_jobj.getString("localName")+"]");
						hashmap_cell.put("TEXT", _jobj.getString("imageName"));
						int id = mContext.getResources().getIdentifier(_jobj.getString("imageName"), "drawable", packageName);
						hashmap_cell.put("IMAGE", id);
						data.put(value, id);
						itemResource.add(hashmap_cell);
					}
					emojj.put(SUN, itemResource);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}




		}

	}

	public boolean isRead = false;

	public void readGif(final Context mContext, final String myAfid, final Handler handler, final Intent intent) {
		// TODO Auto-generated method stub
		if (isRead) {
			return;
		}
		isRead = true;
		new Thread(new Runnable() {
			public void run() {
				CacheManager.getInstance().getGifImageUtilInstance().getImageFolder().clear();
				CacheManager.getInstance().getGifImageUtilInstance().getListFolders().clear();

				long begin = System.currentTimeMillis();
				PalmchatLogUtils.e("EmojiParser", "EmojiParser begin  " + begin);
				CacheManager.getInstance().getGifImageUtilInstance().getFolder(EmojiParser.getInstance(mContext), RequestConstant.STORE_CACHE + myAfid + "/", 0);
				ArrayList<ImageFolderInfo> imageFolders = CacheManager.getInstance().getGifImageUtilInstance().getImageFolder();
				ArrayList<ArrayList<ImageFolderInfo>> listFolders = CacheManager.getInstance().getGifImageUtilInstance().getListFolders();

				ArrayList<ImageFolderInfo> imageFoldersTemp = new ArrayList<ImageFolderInfo>();
				int size = imageFolders.size();
				int a = 0;
				int max = 3;
				for (int i = 0; i < size; i++) {
					if (a % 3 == 0) {
						imageFoldersTemp = new ArrayList<ImageFolderInfo>();
					}
					a++;
					ImageFolderInfo imgInfo = imageFolders.get(i);// Collections.reverse(arr);
					CommonUtils.getSortFaceGifPath(imgInfo);
					PalmchatLogUtils.println("i " + i + "  " + imgInfo.toString());
					imageFoldersTemp.add(imgInfo);
					if (a == max) {
						a = 0;
						listFolders.add(imageFoldersTemp);
						imageFoldersTemp = null;
					}
				}
				CacheManager.getInstance().getGifImageUtilInstance().sortOrder();
				isRead = false;
				if (handler != null && intent != null) {
					handler.obtainMessage(0, intent).sendToTarget();
				}
				// imageFoldersTemp = null;
				long end = System.currentTimeMillis();
				PalmchatLogUtils.e("EmojiParser", "EmojiParser end  " + (end - begin));
				int listSize = listFolders.size();
				PalmchatLogUtils.println("listFolders.size  " + listSize);

			}
		}).start();
	}

	/*private String buildEmojj(String input) {
		if (input == null || input.length() <= 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		if (SUN.equals(PREFIX)) {
			result.append("[" + input + "]");
		} else if (TYPE.equals(PREFIX)) {
			result.append("[" + input + "]");
		} else {
			result.append(input);
		}

		return result.toString();
	}*/

	/*private int[] toCodePointArray(String str) {
		char[] ach = str.toCharArray();
		int len = ach.length;
		int[] acp = new int[Character.codePointCount(ach, 0, len)];
		int j = 0;
		for (int i = 0, cp; i < len; i += Character.charCount(cp)) {
			cp = Character.codePointAt(ach, i);
			acp[j++] = cp;
		}
		return acp;
	}*/

	public HashMap<String, Object> getEmojjGif() {
		return emojjGif;
	}

	public void setEmojjGif(HashMap<String, Object> emojjGif) {
		this.emojjGif = emojjGif;
	}

	public boolean isDefaultEmotion(EditText editText, String text) {
		boolean flag = false;
		if (text == null) {
			text = "";
		}
		Matcher matcher = pattern.matcher(text);
		String emo = "";
		while (matcher.find()) {
			emo = matcher.group();
			try {
				// String ttt = emo.substring(emo.indexOf("]") + 1,
				// emo.lastIndexOf("["));
				// Log.e("ttt", emo);
				int id = data.get(emo);
				// Log.e("id", id+"");
				if (id != 0) {
					flag = true;
					// drawable = resource.getDrawable(id);
					// drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					// drawable.getIntrinsicHeight());
					// span = new ImageSpan(drawable);
					// sBuilder.setSpan(span, matcher.start(),
					// matcher.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					int action = KeyEvent.ACTION_DOWN;
					int code = KeyEvent.KEYCODE_DEL;
					KeyEvent event = new KeyEvent(action, code);
					editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
				}
			} catch (Exception e) {
				flag = false;
				continue;
			}
		}
		return flag;
	}
	/**
	 * 替换掉所有换行符
	 * 之所以改的这么复杂 是因为 有的公司手机C8 J8  只要用了set Text就会清掉之前打的字
	 * @param editText
	 */
	private void replaceAllCrToBlank(CutstomPopwindowEditText editText){
		int crIndex=editText.getText().toString().indexOf( DefaultValueConstant.CR);
		if(crIndex>=0){
			editText.getText().replace(crIndex, crIndex+1, DefaultValueConstant.BLANK);
			replaceAllCrToBlank(editText);
		}
		return;
	}
	/**add by zhh
	 * 解析广播发送页面文本编辑框中tag和表情
	 * 之所以改的这么复杂 是因为 有的公司手机  只要用了settext就会清掉之前打的字
	 * @param context
	 * @param text
	 * @return
	 */
	public void parseEdtEmojAndTag(Context context,String text,CutstomPopwindowEditText editText,int selectindex) {
		if (text.isEmpty())
			return  ;
		replaceAllCrToBlank(editText);
		/*tag集合 最大为不重复的7个tag*/
		HashSet<String> arrTagsRepet = new HashSet<String>();

		/* 解析tag */
		patternTags = Pattern.compile(EmojiParser.regTags); // 判断是否是tag的表达式
		final Matcher matcherTags = patternTags.matcher(text);
//		SpannableStringBuilder sBuilder = new SpannableStringBuilder(text);

		while (matcherTags.find()) {

			String tag = matcherTags.group().toLowerCase();
			if ((arrTagsRepet.size() < EmojiParser.MAXTAGSCOUNT)&&(!arrTagsRepet.contains(tag) )) {
				arrTagsRepet.add(tag);
			}

			if (arrTagsRepet.size() >= EmojiParser.MAXTAGSCOUNT && (!arrTagsRepet.contains(tag)))
				continue;

			//只检测光标附近的50个字符范围内的tag就可以了 不需要全文匹配
//			if(selectindex<0||(selectindex>=matcherTags.start()&&selectindex<=matcherTags.start()+50)){
				//先取出这个Tag范围内 是否已经设置
				ForegroundColorSpan[] arrForegroundColorSpan= editText.getText().getSpans( matcherTags.start(), matcherTags.end(), ForegroundColorSpan.class);
				if(arrForegroundColorSpan==null||arrForegroundColorSpan.length<=0
						||!tag.equals( editText.getText().toString().substring(editText.getText().getSpanStart(  arrForegroundColorSpan[0] )
						,editText.getText().getSpanEnd(  arrForegroundColorSpan[0] )))  ){//未设置过 才去设置
					editText.getText().setSpan(new ForegroundColorSpan(color_tag_normal), matcherTags.start(), matcherTags.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

				}
//			}

		}

		//----------检查是否合法 这块主要用于当从一个TAG中间输入空格截断的时候做处理 先把span取消 再加回合法的部分S
		ForegroundColorSpan[] arrAllForegroundColorSpan =editText.getText().getSpans(0, text.length(), ForegroundColorSpan.class);
		for(int i=0;i<arrAllForegroundColorSpan.length;i++){
			int _inx_start= editText.getText().getSpanStart(  arrAllForegroundColorSpan[i] );
			int _inx_end= editText.getText().getSpanEnd(  arrAllForegroundColorSpan[i] );
			if(_inx_start>=0){
				Matcher matcherSpan=patternTags.matcher( editText.getText().toString().substring(_inx_start,_inx_end));
				if(matcherSpan.find()){
					String _strtag=matcherSpan.group();
					if(_strtag!=null) {
						if (!_strtag.equals(editText.getText().toString().substring(_inx_start, _inx_end).toLowerCase())) {
							editText.getText().removeSpan(arrAllForegroundColorSpan[i]);
							if (arrTagsRepet.contains(editText.getText().toString().substring(_inx_start, _inx_start + _strtag.length()).toLowerCase())) {
								editText.getText().setSpan(new ForegroundColorSpan(color_tag_normal),
										_inx_start, _inx_start + _strtag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
						}else if (!arrTagsRepet.contains(_strtag.toLowerCase())){
							editText.getText().removeSpan(arrAllForegroundColorSpan[i]);
						}
					}
				}else{
					editText.getText().removeSpan(arrAllForegroundColorSpan[i]);
				}
			}
		}
		Matcher matcher = pattern.matcher(text);

		Drawable drawable = null;
		ImageSpan span = null;
		String emo = "";
		while (matcher.find()) {
			emo = matcher.group();
			try {
				int id = data.get(emo);
				if (id != 0) {
//					if(selectindex<0||(selectindex>=matcher.start()&&selectindex<=matcher.start()+20)){先取消这个为了性能 而做的判断 解决 复制黏贴问题
						ImageSpan[] arrEmojiSpan= editText.getText().getSpans( matcher.start(), matcher.end(), ImageSpan.class);
						if(arrEmojiSpan==null||arrEmojiSpan.length<=0  ){//未设置过 才去设置
							drawable=emojDrawable.get(emo);
							if(drawable==null){
								drawable = resource.getDrawable(id);
								drawable.setBounds(0, 0,CommonUtils.emoji_w_h(context), CommonUtils.emoji_w_h(context));
								emojDrawable.put(emo, drawable);
							}
							span = new ImageSpan(drawable);
							editText.getText().setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
//					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				//无法解析该表情
			}
		}
//		return sBuilder;
	}
}
