/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.afmobi.palmchat.util.universalimageloader.core.download;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.util.universalimageloader.core.DisplayImageOptions;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobi.palmchat.util.universalimageloader.core.assist.ContentLengthInputStream;
import com.afmobi.palmchat.util.universalimageloader.utils.IoUtils;
import com.afmobi.palmchat.util.universalimageloader.utils.L;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

/**
 * Provides retrieving of {@link InputStream} of image by URI from network or file system or app resources.<br />
 * {@link URLConnection} is used to retrieve image stream from network.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see HttpClientImageDownloader
 * @since 1.8.0
 */
public class BaseImageDownloader implements ImageDownloader {
	/** {@value} */
	public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 8 * 1000; // milliseconds
	/** {@value} */
	public static final int DEFAULT_HTTP_READ_TIMEOUT = 25 * 1000; // milliseconds

	/** {@value} */
	protected static final int BUFFER_SIZE = 32 * 1024; // 32 Kb
	/** {@value} */
	protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

	protected static final int MAX_REDIRECT_COUNT = 5;

	protected  static final String CONTENT_CONTACTS_URI_PREFIX = "content://com.android.contacts/";

	private static final String ERROR_UNSUPPORTED_SCHEME = "UIL doesn't support scheme(protocol) by default [%s]. "
			+ "You should implement this support yourself (BaseImageDownloader.getStreamFromOtherSource(...))";

	protected final Context context;
	protected final int connectTimeout;
	protected final int readTimeout;

	public BaseImageDownloader(Context context) {
		this.context = context.getApplicationContext();
		this.connectTimeout = Constants.USE_PROXY?DEFAULT_HTTP_CONNECT_TIMEOUT*2:DEFAULT_HTTP_CONNECT_TIMEOUT;
		this.readTimeout =  Constants.USE_PROXY?DEFAULT_HTTP_READ_TIMEOUT*2:DEFAULT_HTTP_READ_TIMEOUT;
	}

	public BaseImageDownloader(Context context, int connectTimeout, int readTimeout) {
		this.context = context.getApplicationContext();
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	@Override
	public InputStream getStream(String imageUri, Object extra) throws IOException {
		switch (Scheme.ofUri(imageUri)) {
			case HTTP:
			case HTTPS:
				return getStreamFromNetwork(imageUri, extra);
			case FILE:
				return getStreamFromFile(imageUri, extra);
			case CONTENT:
				return getStreamFromContent(imageUri, extra);
			case ASSETS:
				return getStreamFromAssets(imageUri, extra);
			case DRAWABLE:
				return getStreamFromDrawable(imageUri, extra);
			case UNKNOWN:
			default:
				return getStreamFromOtherSource(imageUri, extra);
		}
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located in the network).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException if some I/O error occurs during network request or if no InputStream could be created for
	 *                     URL.
	 */
	protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
		String strSession=PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession();

		String stv =null;
		HttpURLConnection conn =null; 
		if(imageUri.endsWith(ImageManager.AVATAR_POST_DOWNLOAD)){//头像下载的话  用POST方式 
			imageUri=imageUri.replace(ImageManager.AVATAR_POST_DOWNLOAD, "");
			int pos = imageUri.indexOf("/d/"); 
		   imageUri += "&session="+strSession; 
		   stv = imageUri.substring(0, pos+3);
		   conn = createConnection(stv, extra); 
			 
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setDoOutput(true);  
			conn.setDoInput(true);  
			OutputStream out = conn.getOutputStream();  
	       String content = imageUri.substring(pos+3 ); 
	       out.write(content.getBytes());   
	       out.flush();  
			 
		}else{//不需要session的 广播等可以直接用get方式下载的图片 
			  conn = createConnection(imageUri, extra);
			conn.setRequestMethod("GET");
		} 
	    

		int redirectCount = 0;
		while (conn.getResponseCode() / 100 == 3 && redirectCount < MAX_REDIRECT_COUNT) {
			conn = createConnection(conn.getHeaderField("Location"), extra);
			redirectCount++;
		}

		InputStream imageStream;
		try {
			imageStream = conn.getInputStream();
		} catch (IOException e) {
			// Read all data to allow reuse connection (http://bit.ly/1ad35PY)
			imageStream=null;
			IoUtils.readAndCloseStream(conn.getErrorStream());
			L.e(e);
			throw e;
		}
//		int len = conn.getContentLength();
//		if(imageStream != null && len < 0)
//		{
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			try
//			{
//				 byte[] sCaceh = new byte[1024];
//	             pos = 0;
//	              while ((pos = imageStream.read(sCaceh, 0, sCaceh.length)) != -1) { 
//		                 String str = new String(sCaceh);
//		                 bos.write(sCaceh, 0, pos); 
//
//	             }
//			}
//			catch(Exception e)
//			{
//				e.printStackTrace();
//			}
//			finally
//			{
//				len = bos.size();
//				bos.close();
//				bos = null;
//			}
//		}
		return new ContentLengthInputStream(new BufferedInputStream(imageStream, BUFFER_SIZE), conn.getContentLength());
	}

	/**
	 * Create {@linkplain HttpURLConnection HTTP connection} for incoming URL
	 *
	 * @param url   URL to connect to
	 * @param extra Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *              DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@linkplain HttpURLConnection Connection} for incoming URL. Connection isn't established so it still configurable.
	 * @throws IOException if some I/O error occurs during network request or if no InputStream could be created for
	 *                     URL.
	 */
	protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
		String encodedUrl = Uri.encode(url, ALLOWED_URI_CHARS);
		HttpURLConnection conn=null;
		if(Constants.USE_PROXY) {
			PalmchatLogUtils.e("WXL","img:"+url);
			/*Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Constants.PROXY_URL,Constants.PROXY_PORT));
			conn = (HttpURLConnection) new URL(encodedUrl).openConnection(proxy);
			conn.setRequestProperty("connection", "close");*/


			Properties systemProperties = System.getProperties();
			systemProperties.setProperty("http.proxyHost",Constants.PROXY_URL);
			systemProperties.setProperty("http.proxyPort", String.valueOf(Constants.PROXY_PORT));
			systemProperties.setProperty("http.keepAlive", "false");
			conn = (HttpURLConnection) new URL(encodedUrl).openConnection( );
		}else{
			conn = (HttpURLConnection) new URL(encodedUrl).openConnection( );
		}
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(readTimeout);

		return conn;
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located on the local file system or SD card).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException if some I/O error occurs reading from file system
	 */
	protected InputStream getStreamFromFile(String imageUri, Object extra) throws IOException {
		String filePath = Scheme.FILE.crop(imageUri);
		return new ContentLengthInputStream(new BufferedInputStream(new FileInputStream(filePath), BUFFER_SIZE),
				(int) new File(filePath).length());
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is accessed using {@link ContentResolver}).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws FileNotFoundException if the provided URI could not be opened
	 */
	protected InputStream getStreamFromContent(String imageUri, Object extra) throws FileNotFoundException {
		ContentResolver res = context.getContentResolver();
		Uri uri = Uri.parse(imageUri);
		if (imageUri.startsWith(CONTENT_CONTACTS_URI_PREFIX)) {
			return ContactsContract.Contacts.openContactPhotoInputStream(res, uri);
		} else {
			return res.openInputStream(uri);
		}
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located in assets of application).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException if some I/O error occurs file reading
	 */
	protected InputStream getStreamFromAssets(String imageUri, Object extra) throws IOException {
		String filePath = Scheme.ASSETS.crop(imageUri);
		return context.getAssets().open(filePath);
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located in drawable resources of application).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 */
	protected InputStream getStreamFromDrawable(String imageUri, Object extra) {
		String drawableIdString = Scheme.DRAWABLE.crop(imageUri);
		int drawableId = Integer.parseInt(drawableIdString);
		BitmapDrawable drawable = (BitmapDrawable) context.getResources().getDrawable(drawableId);
		Bitmap bitmap = drawable.getBitmap();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, os);
		return new ByteArrayInputStream(os.toByteArray());
	}

	/**
	 * Retrieves {@link InputStream} of image by URI from other source with unsupported scheme. Should be overriden by
	 * successors to implement image downloading from special sources.<br />
	 * This method is called only if image URI has unsupported scheme. Throws {@link UnsupportedOperationException} by
	 * default.
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException                   if some I/O error occurs
	 * @throws UnsupportedOperationException if image URI has unsupported scheme(protocol)
	 */
	protected InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {
		throw new UnsupportedOperationException(String.format(ERROR_UNSUPPORTED_SCHEME, imageUri));
	}
}