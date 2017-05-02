package com.afmobi.palmchat.gif;

import java.io.IOException;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.FileUtils;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

/**
 * An {@link ImageView} which tries treating background and src as {@link GifDrawable}
 * @author koral--
 */
public class GifImageView extends ImageView
{
	static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";

	/**
	 * A corresponding superclass constructor wrapper.
	 * @see ImageView#ImageView(Context)
	 * @param context
	 */
	public GifImageView ( Context context )
	{
		super( context );
	}

	/**
	 * Like eqivalent from superclass but also try to interpret src and background
	 * attributes as {@link GifDrawable}.
	 * @see ImageView#ImageView(Context, AttributeSet)
	 * @param context
	 * @param attrs
	 */
	public GifImageView ( Context context, AttributeSet attrs )
	{
		super( context, attrs );
		trySetGifDrawable( attrs, getResources() );
	}

	/**
	 * Like eqivalent from superclass but also try to interpret src and background
	 * attributes as GIFs.
	 * @see ImageView#ImageView(Context, AttributeSet, int)
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public GifImageView ( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );
		trySetGifDrawable( attrs, getResources() );
	}

	@Override
	public void setImageResource ( int resId )
	{
		setResource( true, resId, getResources() );
	}

	@Override
	public void setBackgroundResource ( int resId )
	{
		setResource( false, resId, getResources() );
	}

	void trySetGifDrawable ( AttributeSet attrs, Resources res )
	{
		if ( attrs != null && res != null && !isInEditMode() )
		{
			int resId = attrs.getAttributeResourceValue( ANDROID_NS, "src", -1 );
			if ( resId > 0 && "drawable".equals( res.getResourceTypeName( resId ) ) )
				setResource( true, resId, res );

			resId = attrs.getAttributeResourceValue( ANDROID_NS, "background", -1 );
			if ( resId > 0 && "drawable".equals( res.getResourceTypeName( resId ) ) )
				setResource( false, resId, res );
		}
	}

	@TargetApi ( Build.VERSION_CODES.JELLY_BEAN )
	@SuppressWarnings ( "deprecation" )
	//new method not avalilable on older API levels
	void setResource ( boolean isSrc, int resId, Resources res )
	{
		try
		{
			GifDrawable d = new GifDrawable( res, resId );
			if ( isSrc )
				setImageDrawable( d );
			else if ( Build.VERSION.SDK_INT >= 16 )
				setBackground( d );
			else
				setBackgroundDrawable( d );
			return;
		}
		catch ( IOException e )
		{
			//ignored
		}
		catch ( NotFoundException e )
		{
			//ignored
		}
		if ( isSrc )
			super.setImageResource( resId );
		else
			super.setBackgroundResource( resId );
	}
	
	private GifDrawable gifDrawable = null;;
	
	public void displayGif(final Context context, final GifImageView gifImageView, final String value,final GifImageCallBack gifImageCallBack) {
		String sdpath = (String) EmojiParser.getInstance(context).getEmojjGif().get(value);
		if(!TextUtils.isEmpty(sdpath)){
			boolean flag = FileUtils.fileIsExistsAndCanUse(sdpath);
			if(flag){
				showGifImageView(gifImageView, sdpath,gifImageCallBack);
				return;
			}
		}
		String[] strings = CommonUtils.getProductIdAndIndex(value);
		if (strings != null&&strings.length>1) {//防止产品上传动态表情的时候出错引起的Crash
			String download_gif_url = CommonUtils.getDownLoadGIF_URL(strings[0], strings[1]);
			final String download_gif_savepath = CommonUtils.getStoreFaceDetail_DownLoadGIF_savepaht(strings[0]) + strings[1];

			if (FileUtils.fileIsExistsAndCanUse(download_gif_savepath)) {
				EmojiParser.getInstance(context).getEmojjGif().put(value, download_gif_savepath);
				showGifImageView(gifImageView, download_gif_savepath,gifImageCallBack);
			} else {
				if (gifImageCallBack == null) {
					showGifImageView(gifImageView, R.drawable.image_loading,gifImageCallBack);
				}else {
					showGifImageView(gifImageView, R.drawable.store_image_loading_default,gifImageCallBack);
				}
				AfHttpResultListener resultListener = new AfHttpResultListener() {
					@Override
					public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
						PalmchatLogUtils.e("AfOnResult", "StoreFragment: flag = " + flag + " code=" + code + " result= " + result);
						if(CacheManager.getInstance().getGifDownLoadMap().containsKey(download_gif_savepath)){
							CacheManager.getInstance().getGifDownLoadMap().remove(download_gif_savepath);
						}
						if (code == Consts.REQ_CODE_SUCCESS) {
							switch (flag) {
							case Consts.REQ_STORE_DOWNLOAD:
								showGifImageView(gifImageView, download_gif_savepath,gifImageCallBack);
								break;

							default:
								break;
							}

						}else {
							if(gifImageCallBack != null){
								gifImageCallBack.gifOnResult(httpHandle, flag, code, http_code, result, user_data);
							}
						}
					}
				};

				if(!CacheManager.getInstance().getGifDownLoadMap().containsKey(download_gif_savepath)){
					CacheManager.getInstance().getGifDownLoadMap().put(download_gif_savepath, value);
					PalmchatApp.getApplication().mAfCorePalmchat.AfHttpStoreDownload(download_gif_url, null, CacheManager.getInstance().getScreenType(), download_gif_savepath, false, null, resultListener, null);
				}
				
			}

		}
	}

	private LruCache<String, GifDrawable> mMemoryCache;
	private void showGifImageView(final GifImageView gifImageView,final String path,final GifImageCallBack gifImageCallBack) {
		try {
			gifDrawable = new GifDrawable(path);
//			if(gifImageCallBack == null){
//				CacheManager.getInstance().putGifDrawable(gifDrawable);
//			}
			mMemoryCache = PalmchatApp.getApplication().mMemoryCache;
			synchronized (mMemoryCache) {
				if (mMemoryCache.get(path) == null) {
					mMemoryCache.put(path, gifDrawable);
//					System.out.println("--sss mMemoryCache.put");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			if ( e.getMessage() != null) {
				PalmchatLogUtils.e("showGifImageView", e.getMessage());
			}
			if (gifImageCallBack == null) {
				showGifImageView(gifImageView, R.drawable.image_loading,gifImageCallBack);
			}else {
				showGifImageView(gifImageView, R.drawable.store_image_loading_default,gifImageCallBack);
			}
			if(!CacheManager.getInstance().getGifDownLoadMap().containsKey(path)){
				FileUtils.fileDelete(path);
			}
			return;
		}catch(OutOfMemoryError e){
			e.printStackTrace();
			if ( e.getMessage() != null) {
				PalmchatLogUtils.e("showGifImageView", e.getMessage());
			} 
			if(!CacheManager.getInstance().getGifDownLoadMap().containsKey(path)){
				FileUtils.fileDelete(path);
			}
			return;
			
		}
		gifImageView.setImageDrawable(gifDrawable);
	}

	private void showGifImageView(final GifImageView gifImageView,final int resId,final GifImageCallBack gifImageCallBack) {
		try {
			gifDrawable = new GifDrawable(getResources(),resId);
//			if(gifImageCallBack == null ){
//				CacheManager.getInstance().putGifDrawable(gifDrawable);
//			}
			mMemoryCache = PalmchatApp.getApplication().mMemoryCache;
			synchronized (mMemoryCache) {
				if (mMemoryCache.get(resId+"") == null) {
					mMemoryCache.put(resId+"", gifDrawable);
//					System.out.println("--sss mMemoryCache.put");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		gifImageView.setImageDrawable(gifDrawable);
	}

	public interface GifImageCallBack{
		void gifOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data);
	}
}
