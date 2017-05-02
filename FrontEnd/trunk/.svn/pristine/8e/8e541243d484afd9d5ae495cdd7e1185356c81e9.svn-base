package com.afmobi.palmchat.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.broadcasts.BroadcastNotificationReceiver;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.FacebookConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.constant.RequestType;
import com.afmobi.palmchat.db.DBState;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.MsgAlarmManager;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.LaunchActivity;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.chattingroom.ChattingRoomListActivity;
import com.afmobi.palmchat.ui.activity.chattingroom.ChattingRoomMainAct;
import com.afmobi.palmchat.ui.activity.chattingroom.model.ImageFolderInfo;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.helper.RefreshNotificationManager;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.AccountsChattingActivity;
import com.afmobi.palmchat.ui.activity.social.BroadcastNotificationData;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager.SystemBarConfig;
import com.afmobigroup.gphone.R;
import com.core.AfFriendInfo;
import com.core.AfGiftInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfGrpProfileInfo.AfGrpProfileItemInfo;
import com.core.AfMessageInfo;
import com.core.AfNearByGpsInfo;
import com.core.AfPalmCallResp;
import com.core.AfPaystoreCommon.AfStoreDlProdInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.facebook.AccessToken;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 *
 */
public class CommonUtils {
	private final String TAG = CommonUtils.class.getSimpleName();
	private static String ip;
	public static final int TYPE_REG = 0;
	public static final int TYPE_LOGIN = 1;

	public static final String AVATAR_POST_DOWNLOAD="[POST]";//标志用post方式下载图片
	/**
	 * 上一次的点击事件
	 */
	private static long mLastClickTime;
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	static Notification notification;
	public static final int NOTIFY_NET_UNAVAILABLE = 1;
	public static final String CONTENT_URI_SMS = "content://sms";
	public static String[] SMS_COLUMNS = new String[] { "_id", // 0
			"thread_id", // 1
			"address", // 2
			"person", // 3
			"date", // 4
			"body", // 5
			"read", // 6; 0:not read 1:read; default is 0
			"type", // 7; 0:all 1:inBox 2:sent 3:draft 4:outBox 5:failed
			// 6:queued
			"service_center" // 8
	};

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * get right afid
	 */
	public static String getCorrectAfid(String lastLoginAfid) {
		// TODO Auto-generated method stub
		if (!"".equals(lastLoginAfid) && null != lastLoginAfid && (lastLoginAfid.startsWith("a") || lastLoginAfid.startsWith("r"))) {
			lastLoginAfid = lastLoginAfid.substring(1, lastLoginAfid.length());
			return lastLoginAfid;
		} else {
			return lastLoginAfid;
		}
	}

	public static void addShortcut(Context context) {
		Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
		// 是否可以有多个快捷方式的副本，参数如果是true就可以生成多个快捷方式，如果是false就不会重复添加
		intent.putExtra("duplicate", false);

		Intent intent2 = new Intent(Intent.ACTION_MAIN);
		intent2.addCategory(Intent.CATEGORY_LAUNCHER);
		// 删除的应用程序的ComponentName，即应用程序包名+activity的名字
		intent2.setComponent(new ComponentName(context.getPackageName(),"com.afmobi.palmchat.ui.activity.LaunchActivity"));

		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent2);
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.drawable.app_icon));
		context.sendBroadcast(intent);
//		SharePreferenceUtils.getInstance(context).setIsAddShortCut(true);
	}

	public static boolean isEmail(String email) {
		if (!TextUtils.isEmpty(email)) {
			String strPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
			Pattern pattern = Pattern.compile(strPattern);
			Matcher m = pattern.matcher(email);
			return m.matches();
		}
		return false;
	}

	/**
	 * get the right pattern imei
	 */
	public static String getRightIMEI(String imei) {
		if (!"".equals(imei) && null != imei && !"null".equals(imei)) {
			int length = imei.length();
			for (int i = 0; i < length; i++) {
				if (Character.isLetter(imei.charAt(i))) {
					imei = imei.replace(imei.charAt(i), '0');
				}
			}
			int imeiPatterLength = 15;// imei standard pattern length
			if (length < imeiPatterLength) {
				for (int i = 0; i < imeiPatterLength; i++) {
					imei += "9";
					if (imei.length() == imeiPatterLength) {
						break;
					}
				}
			} else if (length > imeiPatterLength) {
				imei = imei.substring(0, imeiPatterLength);
			}
		}
		return imei;
	}

	@SuppressWarnings("finally")
	public static boolean isCanUseSim(Context context) {
		boolean isCanUseSim = true;
		try {
			TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			isCanUseSim = TelephonyManager.SIM_STATE_READY == mgr.getSimState();
			if (TelephonyManager.SIM_STATE_UNKNOWN == mgr.getSimState() && !TextUtils.isEmpty(PalmchatApp.getOsInfo().getImsi())) {
				isCanUseSim = true;
			}
			return isCanUseSim;
		} catch (Exception e) {
			e.printStackTrace();
			isCanUseSim = false;
		} finally {
			return isCanUseSim;
		}
	}

	public static void getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						String ip = inetAddress.getHostAddress().toString();
						setIp(ip);
						return;
					}
				}
			}
		} catch (SocketException ex) {
			PalmchatLogUtils.e("WifiPreference IpAddress", ex.toString());
		}

		setIp(null);
	}

	public static void setIp(String ip) {
		if (verifi(ip)) {
			CommonUtils.ip = ip;
		} else {
			CommonUtils.ip = "0.0.0.0";
		}
	}

	private static boolean verifi(String ip) {
		if (ip == null) {
			return false;
		}
		String patter = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
		Pattern p = Pattern.compile(patter);
		Matcher m = p.matcher(ip);
		return m.find() ? true : false;
	}

	public static boolean isEmpty(String str) {
		if (null == str || "".equals(str) || "".equals(str.trim()) || "null".equals(str)) {
			return true;
		}
		return false;
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null)
			return false;
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo == null)
			return false;
		if (networkInfo.isConnected())
			return true;
		return false;
	}

	/**
	 * 是否去下载显示头像
	 * 
	 * @param opposite
	 *            afid
	 * @param imgPhoto
	 *            图片
	 * @param sex
	 *            性别
	 * @return true直接显示默认的公共帐号头像, flase去下载头像
	 */
	/*public static boolean showHeadImage(String opposite, ImageView imgPhoto, int sex) {
		if (opposite != null && opposite.startsWith(RequestConstant.SERVICE_FRIENDS)) {
			// PlamchatTeam
			if (RequestConstant.PALMCHAT_ID.equals(opposite)) { // 系统帐号
				imgPhoto.setImageResource(R.drawable.palmchat_team);// 显示系统帐号头像
			} else if (RequestConstant.TECNO_ID.equals(opposite)) {
				imgPhoto.setImageResource(R.drawable.tecno_mobile);
			} else if (RequestConstant.INFINIX_ID.equals(opposite)) {
				imgPhoto.setImageResource(R.drawable.infinix_mobile);
			} else if (RequestConstant.CARLCARE_ID.equals(opposite)) {
				imgPhoto.setImageResource(R.drawable.carlcare_service);
			} else if (RequestConstant.ITEL_ID.equals(opposite)) {
				imgPhoto.setImageResource(R.drawable.itel_mobile);
			} else {
				// imgPhoto.setBackgroundResource(R.drawable.palmchat_team);
				return false;
			}
			return true;
		}
		return false;
	}*/

	public static void getMyCountryInfo(final Context context, final Handler handler, final int msgWhat) {

		new Thread(new Runnable() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(RequestType.MY_COUNTRY_INFO_URL());
				HttpResponse response;
				try {
					response = client.execute(get);
					String s = EntityUtils.toString(response.getEntity(), "UTF-8").trim();
					JSONObject json = new JSONObject(s);
					String country = (json.isNull(JsonConstant.KEY_COUNTRY) || "".equals(json.isNull(JsonConstant.KEY_COUNTRY))) == true ? DefaultValueConstant.OVERSEA : json.getString(JsonConstant.KEY_COUNTRY);
					String state = (json.isNull(JsonConstant.KEY_REGION) || "".equals(json.isNull(JsonConstant.KEY_REGION))) == true ?  DefaultValueConstant.OTHERS : json.getString(JsonConstant.KEY_REGION);
					String city = (json.isNull(JsonConstant.KEY_CITY) || "".equals(json.isNull(JsonConstant.KEY_CITY))) == true ? DefaultValueConstant.OTHER : json.getString(JsonConstant.KEY_CITY);
					double lat = (json.isNull(JsonConstant.KEY_LATITUDE) == true || "".equals(json.isNull(JsonConstant.KEY_LATITUDE))) ? -1 : json.getDouble(JsonConstant.KEY_LATITUDE);
					double lng = (json.isNull(JsonConstant.KEY_LONGITUDE) == true || "".equals(json.isNull(JsonConstant.KEY_LONGITUDE))) ? -1 : json.getDouble(JsonConstant.KEY_LONGITUDE);
					boolean isEqual = compareMyCountry(context, country);
					if (isEqual) {
						PalmchatApp.getOsInfo().setState(state);
						PalmchatApp.getOsInfo().setCity(city);
					}
					PalmchatApp.getOsInfo().setLatitude(lat);
					PalmchatApp.getOsInfo().setLongitude(lng);
					PalmchatLogUtils.println("mParam.region  getMyCountryInfo  " + state + "  mParam.country  " + country + "  mParam.city  " + city);

					// CommonUtils.writeToSdcard(s);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					handler.obtainMessage(msgWhat).sendToTarget();
				}

			}
		}).start();

	}

	@SuppressWarnings("finally")
	public static String getCurrentVersion(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				versionName = "";
			}
		} catch (Exception e) {
			PalmchatLogUtils.e("VersionInfo", "Exception " + e.getMessage());
		} finally {
			return versionName;
		}
	}

	public static String saveCrashInfo2File(Throwable ex, Context context) {
//		if (PalmchatLogUtils.DEBUG) {
			if (isHasSDCard()) {
				StringBuffer sb = new StringBuffer();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
				Writer writer = new StringWriter();
				PrintWriter pw = new PrintWriter(writer);
				ex.printStackTrace(pw);
				Throwable cause = ex.getCause();
				while (cause != null) {
					cause.printStackTrace(pw);
					cause = cause.getCause();
				}
				pw.close();
				String result = writer.toString();
				String time = format.format(new Date());
				sb.append(time + "==  ==  ==" + result);

				long timetamp = System.currentTimeMillis();

				String fileName = getCurrentVersion(context) + "-crash-" + time + "-" + timetamp + ".log";

				try {
					File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "crash");
					Log.i("CrashHandler", dir.toString());
					if (!dir.exists())
						dir.mkdir();
					FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
					fos.write(sb.toString().getBytes());
					fos.close();
					return fileName;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
//		}
		return null;
	}

	public static boolean isHasSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	public static boolean compareDate(CharSequence yearText, CharSequence monthText, CharSequence dayText, View view) {

		try {
			Date selected = df.parse(yearText + "-" + monthText + "-" + dayText);
			Date today = new Date();
			if (selected.getTime() - today.getTime() < 0) {
				view.setEnabled(true);
				view.setClickable(true);
				return true;
			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		view.setEnabled(false);
		view.setClickable(false);
		return false;
	}

	public static String replace(String targetConstant, String targetValue, String totalString) {
		if (TextUtils.isEmpty(totalString) || TextUtils.isEmpty(targetValue)) {
			PalmchatLogUtils.println("CommonUtils  str  " + totalString + "  targetName  " + targetValue);
			return totalString;
		}
		totalString = totalString.replace(targetConstant, targetValue);
		return totalString;
	}

	public static void closeSoftKeyBoard(View editText) {
		InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

	}
	
	/**
	 * zhh 
	 * 当前窗口有软键盘则隐藏
	 * @param mContext
	 */
	public static void closeSoftKeyBoard(Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	
	public static void showSoftKeyBoard(View editText) {
		InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(editText, 0);

	}
	
	 
	//强制显示或者关闭系统键盘
    public static void forceSetKeyBoard(final EditText txtSearchKey,final String status)
    {
         
    	Timer timer = new Timer();
    	timer.schedule(new TimerTask(){
    		@Override
    		public void run()
    		{
    			InputMethodManager m = (InputMethodManager)
    					txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    			if(status.equals("open"))
    			{
    				m.showSoftInput(txtSearchKey,InputMethodManager.SHOW_FORCED); 
    			}
    			else
    			{
              	m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0); 
    			}
    		}  
       }, 300);
    }


	public static long getThreadsNum(int thread_id, Context context) {
		Cursor cursor = null;
		ContentResolver contentResolver = context.getContentResolver();
		cursor = contentResolver.query(Uri.parse(CONTENT_URI_SMS), SMS_COLUMNS, "thread_id = " + thread_id, null, null);
		if (cursor == null || cursor.getCount() == 0)
			return -1;
		cursor.moveToFirst();
		long date = cursor.getLong(4);
		cursor.close();
		return date;
	}

	public static boolean diffBetweenTwoTime(long time1, long time2) {
		long l;
		if (time1 > time2) {
			l = time1 - time2;
		} else {
			l = time2 - time1;
		}
		long second = l / 1000;
		return second < 30;
	}

	public static int getOrientation(Context context) {
		return context.getResources().getConfiguration().orientation;
	}

	public static void getInitData(Context context) {
		PalmchatLogUtils.println("CoomonUtils  getInitData 1");
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		PalmchatApp.getOsInfo().setImsi(tm.getSubscriberId());
		String[] countryCodeArray = DBState.getInstance(context).getCountry_cc_FromMcc(PalmchatApp.getOsInfo().getMcc());
		String countryCode = countryCodeArray[0];
		PalmchatApp.getOsInfo().setCountryCode(countryCode);
		PalmchatLogUtils.println("CoomonUtils  getInitData countryCode  " + countryCode);
		if (TextUtils.isEmpty(countryCode)) {
			PalmchatApp.getOsInfo().setMcc(null);
			PalmchatApp.getApplication().mAfCorePalmchat.AfSetMcc(null);
		}
		String country = countryCodeArray[1];
		if (!TextUtils.isEmpty(country)) {
			PalmchatApp.getOsInfo().setCountry(country);
		}
	}

	 
	/**
	 *该方法能从手机相册读取图片 也能从Google相册获取的URI的情况  （处理Google photos的分享 ）
	 * @param context
	 * @param uri
	 * @return
	 */
	public static Bitmap getImageUrlWithAuthority(Context context, Uri uri) {
//		Uri newUri=null; 
	    InputStream is = null;
	    Bitmap bmp=null; 
	    if (uri.getAuthority() != null) {
	        try {
	            is = context.getContentResolver().openInputStream(uri);
	            if(is!=null){
		              bmp = BitmapFactory.decodeStream(is);
//		            newUri= writeToTempImageAndGetPathUri(context, bmp) ;
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }catch(SecurityException e){
	        	e.printStackTrace();
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	        finally {
	            try {
	            	if(is!=null){
	            		is.close();
	            	}
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    return bmp;
	}

	 
	
	
	 
	/**
	 * 获取当前在前台运行的Activity
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrentActivity(Context context) {
		// TODO Auto-generated method stub
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos=  am.getRunningTasks(1);
		if((null!=runningTaskInfos)&&(runningTaskInfos.size()>0)){
			ComponentName cn  = runningTaskInfos.get(0).topActivity;
			PalmchatLogUtils.println("cn.getClassName()  " + cn.getClassName());
			return cn.getClassName();
		}
		PalmchatLogUtils.println("cn.getClassName()  " + "");
		return "";
	}

	private static NotificationManager mNotifMan;
	private static Notification mNotification;
	private static Notification mAtMeNotification;

	public static long lastSoundTime = 0;

	public static final int TYPE_DEFAULT = 0;
	public static final int TYPE_AT_ME = 1;
	public static final int TYPE_BROADCAST = 2;
	public static final int TYPE_PALMCALL = 3;

	/**
	 * notify chatroom at me msg
	 * 
	 * @param context
	 * @param afMsg
	 */
	public static void showAtMeNotification(Context context, AfMessageInfo afMsg) {

		if (ChattingRoomMainAct.class.getName().equals(CommonUtils.getCurrentActivity(context))) {
			return;
		}

		if (mNotifMan == null) {

			mNotifMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		}
		if (mAtMeNotification == null) {
			mAtMeNotification = new Notification();

		}
		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			mAtMeNotification.icon = R.drawable.icon_notify;
		} else {
			mAtMeNotification.icon = R.drawable.app_icon_notify;
		}
		StringBuffer tickerStr = new StringBuffer();
		tickerStr.append(afMsg.name).append(afMsg.msg);
		mAtMeNotification.tickerText = tickerStr.toString();
		mAtMeNotification.when = System.currentTimeMillis();

		if (((PalmchatApp) context).getSettingMode().isVibratio()) {
			// time inteval 4s
			if ((System.currentTimeMillis() - lastSoundTime) > 4000) {
				TipHelper.vibrate(context, 200L);
			}
		}

		if (!((PalmchatApp) context).getSettingMode().isMute()) {
			// time inteval 4s
			if ((System.currentTimeMillis() - lastSoundTime) > 4000) {
				playSound(context);

			}
		}

		Intent i = new Intent(context, ChattingRoomListActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);
		mAtMeNotification.setLatestEventInfo(context, "", "", pendingIntent);

		mNotifMan.notify(TYPE_AT_ME, mAtMeNotification);
		mNotifMan.cancel(TYPE_AT_ME);
		lastSoundTime = System.currentTimeMillis();

		NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();

		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.ledARGB = 0xff0000ff;// Color.GREEN;//0xff00ff00
		notification.ledOnMS = 1;
		notification.ledOffMS = 0;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			notification.icon = R.drawable.icon_notify;
		} else {
			notification.icon = R.drawable.app_icon_notify;
		}
		notification.tickerText = tickerStr.toString();
		notification.when = System.currentTimeMillis();

		if (PalmchatApp.getApplication().getSettingMode().isVibratio()) {
			TipHelper.vibrate(context, 200L);
		}

		if (!PalmchatApp.getApplication().getSettingMode().isMute()) {
			playSound(context);
		}

		// Intent i = new Intent(context, ActivityBroadcastNotification.class);
		// PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
		// i, Intent.FLAG_ACTIVITY_NEW_TASK);
		/*
		 * Intent intent = new Intent(); intent.setClass(context,
		 * ChattingRoomListActivity.class); PendingIntent pendingIntentTnd =
		 * PendingIntent.getBroadcast(context, 0, intent,
		 * PendingIntent.FLAG_UPDATE_CURRENT);
		 */

		notification.setLatestEventInfo(context, context.getString(R.string.app_name), tickerStr.toString(), pendingIntent);

		// notifyManager.cancel(TYPE_BROADCAST);
		notifyManager.notify(TYPE_AT_ME, notification);
	}

	public static void cancelAtMeNoticefacation(Context context) {
		if (mNotifMan == null) {
			mNotifMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}

		mNotifMan.cancel(TYPE_AT_ME);
		lastSoundTime = 0;
	}

	public static void cancelAllNotification(Context context){
		if (mNotifMan == null) {
			mNotifMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		mNotifMan.cancel(TYPE_AT_ME);
		mNotifMan.cancel(TYPE_BROADCAST);
		mNotifMan.cancel(TYPE_DEFAULT);
	}

	public static void showBroadCastNotification(Context context, ArrayList<HashMap<String, List<BroadcastNotificationData>>> list) {

		boolean closePalmchat = SharePreferenceUtils.getInstance(context).getIsClosePalmchat();

		// 1 like, 2 commont
		if (list.size() != 2 || !PalmchatApp.getApplication().getSettingMode().isNewMsgNotice() || do_not_disturbOption() || closePalmchat) {
			return;
		}

		// totoal like count
		int likedSize = 0;

		// total comment count
		int commentSize = 0;
		BroadcastNotificationData itemInfo = null;

		// compute total brd notify count

		// data[0] like, data[1] comment
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, List<BroadcastNotificationData>> map = list.get(i);
			Iterator<Entry<String, List<BroadcastNotificationData>>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, List<BroadcastNotificationData>> entry = iterator.next();
				List<BroadcastNotificationData> midBrd = entry.getValue();
				int size = midBrd.size();
				if (i == 0) {
					likedSize += size;
				} else if (i == 1) {
					commentSize += size;
				}

				if (size > 0) {
					itemInfo = midBrd.get(0);
				}

			}

		}

		if (likedSize == 0 && commentSize == 0) {
			return;
		}

		String displayText;

		if (likedSize == 1 && commentSize == 0) {
			String name = "";
			if (itemInfo != null) {
				name = TextUtils.isEmpty(itemInfo.name) ? itemInfo.afid.replace("a", "") : itemInfo.name;
			}

			displayText = context.getString(R.string.notify_like_single).replace("XXXX", name);

		}
		/*else if (likedSize == 0 && commentSize == 1) {
			String name = "";
			if (itemInfo != null) {
				name = TextUtils.isEmpty(itemInfo.name) ? itemInfo.afid.replace("a", "") : itemInfo.name;
			}
			String str = new StringBuffer(name).append(" : ").append(itemInfo.msg).toString();
			CharSequence text = EmojiParser.getInstance(context).parse(str);
			displayText = text.toString();
		} */
		else {
			displayText = context.getString(R.string.got_new_notification).replace("XXXX", String.valueOf(likedSize + commentSize));
		}

		NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();

		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.ledARGB = 0xff0000ff;// Color.GREEN;//0xff00ff00
		notification.ledOnMS = 1;
		notification.ledOffMS = 0;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			notification.icon = R.drawable.icon_notify;
		} else {
			notification.icon = R.drawable.app_icon_notify;
		}
		notification.tickerText = displayText;
		notification.when = System.currentTimeMillis();

		if (PalmchatApp.getApplication().getSettingMode().isVibratio()) {
			TipHelper.vibrate(context, 200L);
		}

		if (!PalmchatApp.getApplication().getSettingMode().isMute()) {
			playSound(context);
		}

		// Intent i = new Intent(context, ActivityBroadcastNotification.class);
		// PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
		// i, Intent.FLAG_ACTIVITY_NEW_TASK);
		Intent intent = new Intent();
		intent.setClass(context, BroadcastNotificationReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(context, context.getString(R.string.app_name), displayText, pendingIntent);

		// notifyManager.cancel(TYPE_BROADCAST);
		notifyManager.notify(TYPE_BROADCAST, notification);
	}

	public static void showNoticefacation(Context context, int index, int messageCount, AfMessageInfo afMsg) {

		if (!((PalmchatApp) context).getSettingMode().isNewMsgNotice()) {
			RefreshNotificationManager.getInstence().setRefreshing(false);
			return;
		}
		if (do_not_disturbOption()) {
			RefreshNotificationManager.getInstence().setRefreshing(false);
			return;
		}

		boolean closePalmchat = SharePreferenceUtils.getInstance(context).getIsClosePalmchat();
		if (closePalmchat) {
			RefreshNotificationManager.getInstence().setRefreshing(false);
			return;
		}

		boolean isForeGround = MsgAlarmManager.getInstence().isForeGround();
		if (isForeGround && (Chatting.class.getName().equals(CommonUtils.getCurrentActivity(context)) || GroupChatActivity.class.getName().equals(CommonUtils.getCurrentActivity(context)))) {

			RefreshNotificationManager.getInstence().nextRefresh(context, afMsg);
			return;
		}

		if (messageCount == 0) {
			RefreshNotificationManager.getInstence().nextRefresh(context, afMsg);
			return;
		}

		if (mNotifMan == null) {
			mNotifMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		if (mNotification == null) {
			mNotification = new Notification();
		}

		PalmchatLogUtils.println("led light  isForeGround  " + isForeGround);
		mNotification.defaults |= Notification.DEFAULT_LIGHTS;
		mNotification.ledARGB = 0xff0000ff;// Color.GREEN;//0xff00ff00
		mNotification.ledOnMS = 1;
		mNotification.ledOffMS = 0;
		mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
		PalmchatLogUtils.println("led light enter");

		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			mNotification.icon = R.drawable.icon_notify;
		} else {
			mNotification.icon = R.drawable.app_icon_notify;
		}

		String str = context.getString(R.string.receive_messages);
		str = CommonUtils.replace(ReplaceConstant.TARGET_NUMBER, index + "", str);
		str = CommonUtils.replace(ReplaceConstant.TARGET_MESSAGE_COUNT, messageCount + "", str);
		String message = str;// index + "  friend(s) sent you " + messageCount +
		// " messenge(s)";

		StringBuffer tickerStr = new StringBuffer();

		if (MessagesUtils.isPrivateMessage(afMsg.type) || MessagesUtils.isSystemMessage(afMsg.type)) {
			AfFriendInfo afFrd = CacheManager.getInstance().searchAllFriendInfo(afMsg.getKey());
			if (afFrd != null) {
				tickerStr.append(afFrd.name);
			}

			tickerStr.append(" ").append(context.getString(R.string.notify_sent_msg));

		} else if (MessagesUtils.isGroupChatMessage(afMsg.type)) {

			AfGrpProfileInfo afGrp = CacheManager.getInstance().searchGrpProfileInfo(afMsg.getKey());

			if (afGrp != null) {
				tickerStr.append("[").append(afGrp.name).append("]");
			}

			AfFriendInfo afFrd = CacheManager.getInstance().searchAllFriendInfo(afMsg.fromAfId);
			if (afFrd != null) {
				tickerStr.append(afFrd.name);
			}

			tickerStr.append(" ").append(context.getString(R.string.notify_sent_msg));
		}

		mNotification.tickerText = tickerStr.toString();
		mNotification.when = System.currentTimeMillis();

		if (((PalmchatApp) context).getSettingMode().isVibratio()) {
			// time inteval 4s
			if ((System.currentTimeMillis() - lastSoundTime) > 4000) {
				TipHelper.vibrate(context, 200L);
			}
		}

		PalmchatLogUtils.println("--- ccc notification show notification:" + !((PalmchatApp) context).getSettingMode().isMute());
		if (!((PalmchatApp) context).getSettingMode().isMute()) {
			PalmchatLogUtils.println("--- ccc notification isSound");
			// time inteval 4s
			if ((System.currentTimeMillis() - lastSoundTime) > 4000) {
				PalmchatLogUtils.println("--- ccc notification playSound");
				playSound(context);

			}

		}

		Intent i = new Intent(context, MainTab.class);
		i.putExtra(JsonConstant.KEY_TO_MAIN, true);
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);
		mNotification.setLatestEventInfo(context, context.getString(R.string.app_name), message, pendingIntent);

		mNotifMan.cancel(TYPE_DEFAULT);
		mNotifMan.notify(TYPE_DEFAULT, mNotification);

		lastSoundTime = System.currentTimeMillis();

		RefreshNotificationManager.getInstence().nextRefresh(context, afMsg);

	}

	public static void friendReqNotify() {
		if (!PalmchatApp.getApplication().getSettingMode().isNewMsgNotice()) {
			return;
		}
		if (do_not_disturbOption()) {
			return;
		}

		boolean closePalmchat = SharePreferenceUtils.getInstance(PalmchatApp.getApplication()).getIsClosePalmchat();
		if (closePalmchat) {
			return;
		}

		boolean isForeGround = MsgAlarmManager.getInstence().isForeGround();
		if (isForeGround && (Chatting.class.getName().equals(CommonUtils.getCurrentActivity(PalmchatApp.getApplication())) || GroupChatActivity.class.getName().equals(CommonUtils.getCurrentActivity(PalmchatApp.getApplication())))) {
			return;
		}

		if (PalmchatApp.getApplication().getSettingMode().isVibratio()) {
			TipHelper.vibrate(PalmchatApp.getApplication(), 200L);
		}

		if (!PalmchatApp.getApplication().getSettingMode().isMute()) {
			playSound(PalmchatApp.getApplication());
		}

	}

	private static boolean do_not_disturbOption() {
		// TODO Auto-generated method stub
		boolean flag = false;
		Date curDate = new Date(System.currentTimeMillis());
		if (curDate != null) {
			int hours = curDate.getHours();
			int msg_disturb = PalmchatApp.getApplication().getSettingMode().getMsgDisturb();
			switch (msg_disturb) {
			case Consts.AVOID_DISTURB_ALL_TIME:
				flag = true;
				break;
			case Consts.AVOID_DISTURB_IN_NIGHT:
				if (hours < 8 || (hours >= 22 && hours <= 23)) {
					flag = true;
				}
				break;
			case Consts.AVOID_DISTURB_OFF:
				flag = false;
				break;
			}
		}

		return flag;
	}

	public static void cancelNoticefacation(Context context) {
		if (mNotifMan == null) {
			mNotifMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}

		mNotifMan.cancel(TYPE_DEFAULT);
		mNotifMan.cancel(TYPE_BROADCAST);

		lastSoundTime = 0;

	}

	public static void cancelBrdNotification() {
		if (mNotifMan == null) {
			mNotifMan = (NotificationManager) PalmchatApp.getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
		}

		mNotifMan.cancel(TYPE_BROADCAST);
	}

	private static void playSound(Context context) {
		// TODO Auto-generated method stub
		AudioManager mAudioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
		switch (mAudioManager.getRingerMode()) {
		// No volume,no vvibrate.
		case AudioManager.RINGER_MODE_SILENT:
			break;
		// Volume mode
		case AudioManager.RINGER_MODE_NORMAL:
			// PalmchatApp.getApplication().getSoundManager().playSound(1);
			// change by chengyu
			PalmchatApp.getApplication().getSoundManager().playNotification();
			break;
		// Vibrate mode
		case AudioManager.RINGER_MODE_VIBRATE:
			// TipHelper.Vibrate(context, 300L);
			break;
		default:
			break;
		}
	}

	public static void copy(TextView tagView, Context context) {
		int selStart = tagView.getSelectionStart();
		int selEnd = tagView.getSelectionEnd();

		if (!tagView.isFocused()) {
			selStart = 0;
			selEnd = tagView.getText().toString().length();
		}

		int min = Math.min(selStart, selEnd);
		int max = Math.max(selStart, selEnd);

		if (min < 0) {
			min = 0;
		}
		if (max < 0) {
			max = 0;
		}

		ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		clip.setText(tagView.getText());
	}

	public static void showToastImage(final View view, ImageView vImageSex, AfFriendInfo afFriendInfo) {
		// TODO Auto-generated method stub
		if (Consts.AFMOBI_SEX_MALE == afFriendInfo.sex) {
			vImageSex.setBackgroundResource(R.drawable.icon_sexage_boy);
		} else {
			vImageSex.setBackgroundResource(R.drawable.icon_sexage_girl);
		}
		view.setVisibility(View.VISIBLE);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				view.setVisibility(View.GONE);
			}
		}, 2000);
	}

	public static boolean IsCanUseSdCard() {
		try {
			return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<AfGrpProfileItemInfo> getRealList(ArrayList<AfGrpProfileItemInfo> list, List<String> afidList, List<String> userNameList, int action) {
		if (null != afidList && null != userNameList && 0 < afidList.size() && 0 < userNameList.size())
			for (int i = 0; i < afidList.size(); i++) {
				if (null != list && 0 < list.size()) {
					if (Consts.ACTION_ADD == action) {
						add(list, afidList.get(i), userNameList.get(i));
						// print();
					} else if (Consts.ACTION_REMOVE == action) {
						remove(list, afidList.get(i));
					}
				} else {
					break;
				}
			}

		return list;
	}

	/**
	 * 获取群组列表(服务端发送一个退群成员，直接删除之后返回更新过的群成员)
	 * 
	 * @param list
	 * @param afid
	 * @param action
	 * @return
	 */
	public static ArrayList<AfGrpProfileItemInfo> getRealList(ArrayList<AfGrpProfileItemInfo> list, String afid, int action) {
		if (null != list && 0 < list.size()) {
			if (Consts.ACTION_REMOVE == action) {
				remove(list, afid);
			}
		}
		return list;
	}

	static void add(ArrayList<AfGrpProfileItemInfo> list, String afid, String name) {
		int size = list.size();
		for (int j = 0; j < size; j++) {
			if (afid.equals(list.get(j).afid)) {
				list.get(j).name = name;
				return;
			}
		}
		AfGrpProfileItemInfo itemInfo = new AfGrpProfileItemInfo();
		itemInfo.afid = afid;
		itemInfo.name = name;
		list.add(itemInfo);
	}

	static void remove(ArrayList<AfGrpProfileItemInfo> list, String afid) {
		int size = list.size();
		for (int j = 0; j < size; j++) {
			if (afid.equals(list.get(j).afid)) {
				list.remove(j);
				return;
			}
		}
	}

	static public char getSortKey(String str) {
		if (!TextUtils.isEmpty(str)) {
			char ch = str.charAt(0);
			if (ch >= 'A' && ch <= 'Z') {
				return ch;
			} else if (ch >= 'a' && ch <= 'z') {
				return (char) (ch - 'a' + 'A');
			}
		}
		return 'a';
	}

	public static boolean isChatting(Context context) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("isChatting()");
		if (Chatting.class.getName().equals(CommonUtils.getCurrentActivity(context))) {
			PalmchatLogUtils.println("isChatting()  true");
			return true;
		}
		PalmchatLogUtils.println("isChatting()  false");
		return false;
	}

	public static boolean isGroupChatActivity(Context context) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("isGroupChatActivity()");
		if (GroupChatActivity.class.getName().equals(CommonUtils.getCurrentActivity(context))) {
			PalmchatLogUtils.println("isGroupChatActivity()  true");
			return true;
		}
		PalmchatLogUtils.println("isGroupChatActivity()  false");
		return false;
	}

	public static boolean isChattingRoom(Context context) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("isChattingRooom()");
		if (ChattingRoomMainAct.class.getName().equals(CommonUtils.getCurrentActivity(context))) {
			PalmchatLogUtils.println("isChattingRooom()  true");
			return true;
		}
		PalmchatLogUtils.println("isChattingRooom()  false");
		return false;
	}

	public static String getRealDisplayName(AfFriendInfo afFriendInfo) {

		if (afFriendInfo == null) {
			PalmchatLogUtils.println("CommonUtils getRealDisplayName afFriendInfo == null");
			return "";
		}

		if (afFriendInfo.alias != null && !"".equals(afFriendInfo.alias)) {
			return afFriendInfo.alias;
		} else {
			if (afFriendInfo.name != null) {

				return afFriendInfo.name;
			} else {
				return "";
			}
		}

	}

	public static String showImageSize(int fileSize) {
		int size = fileSize / 1024;
		if (fileSize % 1024 > 0) {
			size = size + 1;
		}
		return (size) + "KB";
	}

	public static void getRealList(ArrayList<AfMessageInfo> list, AfMessageInfo msg) {
		int start = 0, mid;
		int end = list.size() - 1;
		AfMessageInfo cur;

		while (start <= end) {
			mid = (start + end) >> 1;
			cur = list.get(mid);
			if (msg._id == cur._id) {
				list.set(mid, msg);
				return;
			} else if (msg._id < cur._id) {
				end = mid - 1;
			} else {
				start = mid + 1;
			}
		}
		if (start < 0 || start >= list.size()) {
			list.add(msg);
		} else {
			list.add(start, msg);
		}
	}

	public static void getRealGiftList(ArrayList<AfGiftInfo> list, AfGiftInfo msg) {
		int start = 0, mid;
		int end = list.size() - 1;
		AfGiftInfo cur;

		while (start <= end) {
			mid = (start + end) >> 1;
			cur = list.get(mid);
			if (msg.identify == cur.identify) {
				list.set(mid, msg);
				return;
			} else if (msg.identify > cur.identify) {
				end = mid - 1;
			} else {
				start = mid + 1;
			}
		}
		if (start < 0 || start >= list.size()) {
			list.add(msg);
		} else {
			list.add(start, msg);
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static String getRealChatDate(Context context, long now, long client_time) {
		long minusTime = now - client_time;

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);

		// today 00:00 in millisSecond
		long zeroTime = cal.getTimeInMillis();

		if (minusTime > (7 * 24 * 3600 * 1000)) {
			return new SimpleDateFormat("dd MMM HH:mm").format(client_time);
		} else if (minusTime > (1 * 24 * 3600 * 1000) && minusTime < (7 * 24 * 3600 * 1000)) {
			return new SimpleDateFormat("E HH:mm").format(client_time);
		} else if (client_time - zeroTime < 0) {

			return context.getString(R.string.yesterday) + " " + new SimpleDateFormat("HH:mm").format(client_time);

		} else {
			return new SimpleDateFormat("HH:mm").format(client_time);
		}

	}

	public static String diffTime(long cur, long last) {
		int diff = (int) ((cur - last) / 100);
		int ms = (diff / 10);
		int dms = diff % 10;
		return ms + "." + dms + "s";
	}

	public static String getRealCountryCode(String countryCode) {
		PalmchatLogUtils.println("getRealCountryCode  " + countryCode);
		if (!isEmpty(countryCode) && countryCode.startsWith("+")) {
			return countryCode.substring(1);
		}
		return countryCode;
	}

	public static String getRealPhoneNO(String phoneNO) {
		PalmchatLogUtils.println("getRealPhoneNO  " + phoneNO);
		if (!isEmpty(phoneNO) && phoneNO.startsWith("0")) {
			return phoneNO.substring(1);
		}
		return phoneNO;
	}

	public static boolean compareMyCountry(Context context, String countryFromServer) {
		String countryCode = PalmchatApp.getOsInfo().getCountryCode();
		String localCountry = DBState.getInstance(context).getCountryFromCode(countryCode);
		// String defaultCountry = context.getString(R.string.oversea);
		if (!TextUtils.isEmpty(localCountry) && localCountry.equals(countryFromServer)) {
			return true;
		}
		return false;
	}

	public static void showOffLineToast(Context context) {

		if (CacheManager.getInstance().getMyProfile() == null) {
			PalmchatLogUtils.println("CacheManager.getInstance().getMyProfile()  null");
			cancelNetUnavailableNoticefacation();
			return;
		}
		PalmchatLogUtils.println("showOffLineToast  context  " + context + "  PalmchatApp.getApplication()  " + PalmchatApp.getApplication());
		if (mNotifMan == null) {
			mNotifMan = (NotificationManager) PalmchatApp.getApplication().getSystemService(context.NOTIFICATION_SERVICE);
		}
		if (notification == null) {
			notification = new Notification();
		}

		int icon = R.drawable.icon_notify;
		long when = System.currentTimeMillis();

		Intent i = new Intent(PalmchatApp.getApplication(), MainTab.class);
		i.putExtra(JsonConstant.KEY_NET_UNAVAILABLE, true);

		notification.icon = icon;
		notification.when = when;

		PendingIntent contentIntent = PendingIntent.getActivity(PalmchatApp.getApplication(), 0, i, 0);
		notification.setLatestEventInfo(PalmchatApp.getApplication(), PalmchatApp.getApplication().getString(R.string.network_unavailable), PalmchatApp.getApplication().getString(R.string.network_unavailable), contentIntent);
		mNotifMan.notify(NOTIFY_NET_UNAVAILABLE, notification);//

		new Handler().post(new Runnable() {
			public void run() {
				cancelNetUnavailableNoticefacation();
			}
		});
	}

	public static void cancelNetUnavailableNoticefacation() {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("mNotifMan " + mNotifMan + "  notification  " + notification);
		if (mNotifMan != null) {
			mNotifMan.cancel(NOTIFY_NET_UNAVAILABLE);
		}
	}

	public static boolean isHD(int w, int h) {
		// TODO Auto-generated method stub
		if ((w >= DefaultValueConstant.WIDTH && h >= DefaultValueConstant.HEIGHT) || (w >= DefaultValueConstant.HEIGHT && h >= DefaultValueConstant.WIDTH)) { // above
																																								// px
																																								// 480x800
																																								// or
																																								// 800x480
			return true;
		}
		return false;
	}

	public static double getRealDisdance(AfNearByGpsInfo s1) {
		// TODO Auto-generated method stub
		if (s1 != null) {
			String distance1 = s1.range;
			if (distance1 != null) {
				distance1 = distance1.replace("km", "").trim();
				if (!"".equals(distance1) && distance1 != null) {
					return Double.valueOf(distance1);
				}
			}
		}
		return 10000000d;
	}

	public static boolean isOverTenMini(long currentTime, long lastShowTime) {
		long diff = currentTime - lastShowTime; // compare last time
		long mini = diff / (1000 * 60 * 10);//
		if (mini < 10) {
			return true;
		}
		return false;
	}

	/**
	 * true can't use app false can use app
	 * 
	 * @return true
	 */
	public static boolean getSdcardSize() {
		String sdcard = Environment.getExternalStorageDirectory().getPath();
		File file = new File(sdcard);
		StatFs statFs = new StatFs(file.getPath());

		long blockSize = statFs.getBlockSize();
		long availableBlocks = statFs.getAvailableBlocks();

		long availableSpare = blockSize * availableBlocks;// (long)
		// (statFs.getBlockSize()*((long)statFs.getAvailableBlocks()));
		// //left byte
		availableSpare = availableSpare / 1024; // left kb
		PalmchatLogUtils.println("sdcardSize  left  kb: " + availableSpare + "  blockSize  " + blockSize + "  availableBlocks  " + availableBlocks);
		if (availableSpare < 10*1024) {//10M以内就报空间不足
			return true;
		}
		return false;
	}

	static String regex = "\\[(.*?)\\]";
	static Pattern pattern;

	private static Pattern getPatter() {
		if (pattern == null) {
			pattern = Pattern.compile(regex);
		}
		return pattern;
	}

	public static CharSequence replceGiftIcon(Context context, String totalString) {
		getPatter();
		Matcher matcher = pattern.matcher(totalString);
		SpannableStringBuilder sBuilder = new SpannableStringBuilder(totalString);
		Drawable drawable = null;
		ImageSpan span = null;
		String emo = "";
		int count = 1;
		try {
			while (matcher.find()) {
				emo = matcher.group();
				// if (count == 1) {
				drawable = context.getResources().getDrawable(R.drawable.rose_little);
				// } else if (count == 2) {
				// drawable =
				// context.getResources().getDrawable(R.drawable.icon_profile_fire);
				// }
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				span = new ImageSpan(drawable);
				sBuilder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				count++;
				PalmchatLogUtils.println("emo  " + emo);
			}
		} catch (Exception e) {
			PalmchatLogUtils.println("replceGiftIcon  " + e.getMessage());
		}
		return sBuilder;
	}

	public static boolean isProfileActivity(Context context) {
		PalmchatLogUtils.println("isProfileActivity()");
		if (ProfileActivity.class.getName().equals(CommonUtils.getCurrentActivity(context))) {
			PalmchatLogUtils.println("isProfileActivity()  true");
			return true;
		}
		PalmchatLogUtils.println("isProfileActivity()  false");
		return false;
	}

	/**
	 * judge this message is top message or not
	 */
	public static boolean isTopMessage(String content, String bm) {
		boolean flag = false;
		String myAfid = CacheManager.getInstance().getMyProfile().afId + ":";
		String bmStr = bm + ":";
		if (!TextUtils.isEmpty(content) && content.startsWith(DefaultValueConstant.LABEL) && bmStr.contains(myAfid)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * judge is it system account,for example: palmchat team
	 * 
	 * @param afid
	 * @return true system account
	 */
	public static boolean isSystemAccount(String afid) {
		if (RequestConstant.PALMCHAT_ID.equals(afid) || RequestConstant.TECNO_ID.equals(afid) || RequestConstant.INFINIX_ID.equals(afid) || RequestConstant.CARLCARE_ID.equals(afid) || RequestConstant.ITEL_ID.equals(afid)) {
			return true;
		}
		return false;
	}

	// Local Language
	public static final String LANGUAGE_LOCAL_DEFUALT = "default";
	public static final String LANGUAGE_LOCAL_CN = "zh_CN";
	public static final String LANGUAGE_LOCAL_ZH = "zh";
	public static final String LANGUAGE_LOCAL_EN = "en";
	public static final String LANGUAGE_LOCAL_ES = "es";
	public static final String LANGUAGE_LOCAL_PT = "pt";
	public static final String LANGUAGE_LOCAL_AR = "ar";
	public static final String LANGUAGE_LOCAL_HA = "ha";
	public static final String LANGUAGE_LOCAL_FR = "fr";
	public static final String LANGUAGE_LOCAL_SW = "sw";
	public static final String LANGUAGE_LOCAL_AM = "am";
	public static final String LANGUAGE_LOCAL_FAIR = "fa";//fa_IR
	public static final String LANGUAGE_LOCAL_HI= "in";//fa_IR
	public static final String LANGUAGE_LOCAL_UR= "ur";//fa_IR
	public static final String LANGUAGE_LOCAL_TH= "th";
	// Server Receive Language
	public static final String LANGUAGE_SERVER_CN = "zh-CN";
	public static final String LANGUAGE_SERVER_EN = "en-US";
	public static final String LANGUAGE_SERVER_ES = "es-ES";
	public static final String LANGUAGE_SERVER_PT = "pt-PT";
	public static final String LANGUAGE_SERVER_AR = "ar-SA";
	public static final String LANGUAGE_SERVER_HA = "ha";
	public static final String LANGUAGE_SERVER_FR = "fr-FR";
	public static final String LANGUAGE_SERVER_SW = "sw-KE";
	public static final String LANGUAGE_SERVER_AM = "amh"; 
	public static final String LANGUAGE_SERVER_FAIR = "fa";//fa-IR波斯
	public static final String LANGUAGE_SERVER_HI = "hi";//
	public static final String LANGUAGE_SERVER_UR = "ur";//f
	public static final String LANGUAGE_SERVER_TH = "th";//f
	public static final int TEXT_MAX = 140;

	public static String getRightLanguage(Context context, int type) {
		// TODO Auto-generated method stub
		if (type == TYPE_REG) {
			return getLocalLanguage();
		} else if (type == TYPE_LOGIN) {
			String local_language = SharePreferenceUtils.getInstance(context).getLocalLanguage();
			if (LANGUAGE_LOCAL_AM.equals(local_language)) {
				return LANGUAGE_SERVER_AM;
			} else if (LANGUAGE_LOCAL_AR.equals(local_language)) {
				return LANGUAGE_SERVER_AR;
			} else if (LANGUAGE_LOCAL_CN.equals(local_language) || LANGUAGE_LOCAL_ZH.equals(local_language)) {
				return LANGUAGE_SERVER_CN;
			} else if (LANGUAGE_LOCAL_DEFUALT.equals(local_language)) {
				return getLocalLanguage();
			} else if (LANGUAGE_LOCAL_EN.equals(local_language)) {
				return LANGUAGE_SERVER_EN;
			} else if (LANGUAGE_LOCAL_ES.equals(local_language)) {
				return LANGUAGE_SERVER_ES;
			} else if (LANGUAGE_LOCAL_FR.equals(local_language)) {
				return LANGUAGE_SERVER_FR;
			} else if (LANGUAGE_LOCAL_HA.equals(local_language)) {
				return LANGUAGE_SERVER_HA;
			} else if (LANGUAGE_LOCAL_PT.equals(local_language)) {
				return LANGUAGE_SERVER_PT;
			} else if (LANGUAGE_LOCAL_SW.equals(local_language)) {
				return LANGUAGE_SERVER_SW;
			}else if (LANGUAGE_LOCAL_FAIR.equals(local_language)) {
				return LANGUAGE_SERVER_FAIR;
			}else if (LANGUAGE_LOCAL_HI.equals(local_language)) {
				return LANGUAGE_SERVER_HI;
			}else if (LANGUAGE_LOCAL_UR.equals(local_language)) {
				return LANGUAGE_SERVER_UR;
			}else if(LANGUAGE_LOCAL_TH.equals(local_language)){
				return LANGUAGE_SERVER_TH;
			}
		}
		return LANGUAGE_SERVER_EN;
	}

	public static String getLocalLanguage() {
		String local_language = Locale.getDefault().getLanguage();
		if (LANGUAGE_LOCAL_AM.equals(local_language)) {
			return LANGUAGE_SERVER_AM;
		} else if (LANGUAGE_LOCAL_AR.equals(local_language)) {
			return LANGUAGE_SERVER_AR;
		} else if (LANGUAGE_LOCAL_CN.equals(local_language) || LANGUAGE_LOCAL_ZH.equals(local_language)) {
			return LANGUAGE_SERVER_CN;
		} else if (LANGUAGE_LOCAL_EN.equals(local_language)) {
			return LANGUAGE_SERVER_EN;
		} else if (LANGUAGE_LOCAL_ES.equals(local_language)) {
			return LANGUAGE_SERVER_ES;
		} else if (LANGUAGE_LOCAL_FR.equals(local_language)) {
			return LANGUAGE_SERVER_FR;
		} else if (LANGUAGE_LOCAL_HA.equals(local_language)) {
			return LANGUAGE_SERVER_HA;
		} else if (LANGUAGE_LOCAL_PT.equals(local_language)) {
			return LANGUAGE_SERVER_PT;
		} else if (LANGUAGE_LOCAL_SW.equals(local_language)) {
			return LANGUAGE_SERVER_SW;
		}else if (LANGUAGE_LOCAL_FAIR.equals(local_language)) {
			return LANGUAGE_SERVER_FAIR;
		}else if (LANGUAGE_LOCAL_HI.equals(local_language)) {
			return LANGUAGE_SERVER_HI;
		}else if (LANGUAGE_LOCAL_UR.equals(local_language)) {
			return LANGUAGE_SERVER_UR;
		}else if(LANGUAGE_LOCAL_TH.equals(local_language)){
			return LANGUAGE_SERVER_TH;
		}
		return LANGUAGE_SERVER_EN;
	}

	public static final void to(Context context, Class<?> cls) {
		to(context, cls, null);
	}

	public static final void to(Context context, Class<?> cls, Bundle data) {
		Intent intent = new Intent(context, cls);
		if (data != null) {
			intent.putExtras(data);
		}
		context.startActivity(intent);
	}

	public static final String getDownLoadGIF_URL(String productID, String index) {
		String DownLoadGIF_URL = PalmchatApp.getApplication().getGifUrl()+"static/img/picture/" + productID + "/" + CacheManager.getInstance().getScreenString() + "/source/" + index + ".gif";
		return DownLoadGIF_URL;
	}

	public static final String getStoreFaceDetail_Split_img_savepaht(String productID) {
		String split_img_savepaht = RequestConstant.PRODUCT_EMOJI_CACHE + productID + "/";
		File file = new File(split_img_savepaht);
		if (!file.exists()) {
			file.mkdir();
		}
		return split_img_savepaht;
	}

	public static final String getDownLoadGIF_URL(String str) {
		String[] arrays = getProductIdAndIndex(str);
		String productID = arrays[0];
		String index = arrays[1];
		String url = getDownLoadGIF_URL(productID, index);
		return url;

	}

	public static final String getStoreFaceDetail_DownLoadGIF_savepaht(String productID) {
		String DownLoadGIF_savepaht = getStoreFaceDetail_Split_img_savepaht(productID) + "gif/";
		File file = new File(DownLoadGIF_savepaht);
		if (!file.exists()) {
			file.mkdir();
		}
		return DownLoadGIF_savepaht;
	}

	public static String[] getProductIdAndIndex(String str) {
		// TODO Auto-generated method stub
		String[] arrays = new String[2];
		if (!TextUtils.isEmpty(str)) {
			str = str.replace("[", "");
			str = str.replace("]", "");
			arrays = str.split(",");
		}
		return arrays;
	}

	public static final String getStoreFaceDetail_UserFolder(String afid) {
		String UserFolder = RequestConstant.STORE_CACHE + afid + "/";
		File file = new File(UserFolder);
		if (!file.exists()) {
			file.mkdir();
		}
		return UserFolder;
	}

	public static final String getStoreFaceFolderDownLoadSdcardSavePath(String afid, String productID) {

		String download_zipfile_save_path = getStoreFaceDetail_UserFolder(afid) + productID + "/";
		File file = new File(download_zipfile_save_path);
		if (!file.exists()) {
			file.mkdir();
		}
		return download_zipfile_save_path;
	}

	public static boolean checkStoreFace(String productID) {
		String savePath = new StringBuffer(CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(CacheManager.getInstance().getMyProfile().afId, productID)).append(CacheManager.getInstance().getScreenString()).toString();
		File f = new File(CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(CacheManager.getInstance().getMyProfile().afId, productID));
		File[] files = f.listFiles();
		String oldpath = "";
		if (null == files) {
			return false;
		}
		for (int i = 0; i < files.length; i++) {
			oldpath = files[i].getAbsolutePath();
			PalmchatLogUtils.e("comm", "productID=" + productID + "name=" + files[i].getName() + "oldpaht = " + oldpath);
			if (!oldpath.equals(savePath)) {
				if (!oldpath.endsWith(".tmp")) {
					FileUtils.delete(oldpath);
				} else {
					return false;
				}
			} else {
				return true;
			}
		}
		File file = new File(savePath);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNumeric(String str) {
		if(TextUtils.isEmpty(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static void getSortFaceGifPath(ImageFolderInfo imgInfo) {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, Object>> arrayString = imgInfo.filePathesMap;
		int size = arrayString.size();
		HashMap<Integer, String> tempArrayString = new HashMap<Integer, String>();
		for (int i = 0; i < size; i++) {
			HashMap<String, Object> item_id_path_hashmap = arrayString.get(i);
			String item_id_path = (String) item_id_path_hashmap.get("IMAGE");
			String string = item_id_path.substring(item_id_path.lastIndexOf("/") + 1, item_id_path.lastIndexOf("."));
			if (isNumeric(string)) {
				int index = Integer.parseInt(string);
				tempArrayString.put(index, item_id_path);
			} else {
				System.out.println("string:" + string);
				break;
			}
		}
		int hashSize = tempArrayString.size();
		if (hashSize == 0) {
			return;
		}
		boolean isEmpty = false;
		ArrayList<HashMap<String, Object>> arrayStringTemp = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < hashSize; i++) {
			String item_id_path = tempArrayString.get(i);
			if (TextUtils.isEmpty(item_id_path)) {
				PalmchatLogUtils.println("getSortFaceGifPath i:" + i + "  item_id_path:" + item_id_path);
				isEmpty = true;
				break;
			}
			HashMap<String, Object> hashmap = new HashMap<String, Object>();
			hashmap.put("IMAGE", item_id_path);
			String qualityString = "/" + CacheManager.getInstance().getScreenString();
			String item_id = item_id_path.substring(item_id_path.lastIndexOf("store/") + 6, item_id_path.lastIndexOf(qualityString));
			String value = buildEmojj(item_id, i + "");
			String repString = CacheManager.getInstance().getMyProfile().afId + "/";
			value = value.replace(repString, "");
			hashmap.put("VALUE", value);
			arrayStringTemp.add(hashmap);
		}
		if (!isEmpty) {
			imgInfo.filePathesMap = arrayStringTemp;
		}

	}

	public static String buildEmojj(String item_id, String index) {
		if (item_id == null || item_id.length() <= 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		result.append("[" + item_id + "," + index + "]");

		return result.toString();
	}

	public static void backTofragment(Activity activity, int fragmentTag) {

		activity.finish();
		Stack<Activity> activityStack = MyActivityManager.getActivityStack();
		int acSize = activityStack.size();
		if (acSize > 0) {
			for (int i = acSize - 1; i >= 0; i--) {
				Activity act = activityStack.get(i);
				if (!(act instanceof MainTab || act instanceof LaunchActivity)) {
					act.finish();
				}
			}
		}

	}

	/**
	 * 判断点击的是否为表情框的删除按钮
	 * 
	 * @param id
	 * @param editText
	 * @return
	 */
	public static boolean isDeleteIcon(int id, EditText editText) {
		boolean flag = false;
		if (id == R.drawable.emojj_del) {// click delete icon
			CacheManager.getInstance().setEditTextDelete(true);

			flag = true;
			int action = KeyEvent.ACTION_DOWN;
			int code = KeyEvent.KEYCODE_DEL;
			KeyEvent event = new KeyEvent(action, code);
			editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);

			CacheManager.getInstance().setEditTextDelete(false);
		}
		return flag;
	}

	public static int split_source_w(String source_pic_url) {
		int source_w = 0;
		String w = "0";
		if (source_pic_url.contains("-")) {
			try {
				String temp = source_pic_url.substring(0, source_pic_url.lastIndexOf("-"));
				if (!TextUtils.isEmpty(temp)) {
					w = temp.substring(temp.lastIndexOf("-") + 1, temp.length());
				}
				if (!TextUtils.isEmpty(w)) {
					source_w = Integer.parseInt(w);
					return source_w;
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		if (source_pic_url.contains("_")) {
			try {
				String temp = source_pic_url.substring(0, source_pic_url.lastIndexOf("_"));
				if (!TextUtils.isEmpty(temp)) {
					w = temp.substring(temp.lastIndexOf("_") + 1, temp.length());
				}
				if (!TextUtils.isEmpty(w)) {
					source_w = Integer.parseInt(w);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return source_w;
	}

	public static int split_source_h(String source_pic_url) {
		int source_h = 0;
		if (source_pic_url.contains("-")) {
			try {
				String h = source_pic_url.substring(source_pic_url.lastIndexOf("-") + 1, source_pic_url.length());
				if (!TextUtils.isEmpty(h)) {
					if(h.contains("_")){
						h = h.substring(0, h.indexOf( "_"));
					}
					if (!TextUtils.isEmpty(h)) {
						source_h = Integer.parseInt(h);
						return source_h;
					}
				}
			} catch (Exception e) {

			}
		}
		if (source_pic_url.contains("_")) {
			try {
				String h = source_pic_url.substring(source_pic_url.lastIndexOf("_") + 1, source_pic_url.length());
				if (!TextUtils.isEmpty(h)) {
					if(h.contains(".")){
						h = h.substring(0, h.lastIndexOf("."));
					}
					if (!TextUtils.isEmpty(h)) {
						source_h = Integer.parseInt(h);
					}
				}
			} catch (Exception e) {

			}
		}
		return source_h;
	}

	/**
	 * 根据比例限制 返回高度值
	 * @param w
	 * @param h
     * @return
     */
	public static int checkDisplayRatio_h(int w,int h){
		if(w*9/h>16){
			return w*9/16;
		}
		if(w*5/h<4){
			return 5*w/4;
		}
		return h;
	}
	public static List<AfNearByGpsInfo> getRealList(int ReceiveLatestMsgid, List<AfNearByGpsInfo> ServerList) {
		if (ReceiveLatestMsgid > 0) {
			int size = ServerList.size();
			List<AfNearByGpsInfo> tempList = new ArrayList<AfNearByGpsInfo>();
			for (int i = size - 1; i >= 0; i--) {
				AfNearByGpsInfo afNearByGpsInfo = ServerList.get(i);
				int server_msg_id = afNearByGpsInfo.msg_id;
				if (ReceiveLatestMsgid < server_msg_id) {
					tempList.add(afNearByGpsInfo);
				}
			}
			return tempList;
		} else {
			return ServerList;
		}
	}

	public static int getRandomId(long sid) {
		int id = -(new Random().nextInt(2000000000));
		return id;
	}

	public static BigDecimal getBigDecimalCount(int time) {
		BigDecimal mScore = new BigDecimal(time);
		BigDecimal percent = new BigDecimal(1000);
		BigDecimal result = mScore.divide(percent, 1, BigDecimal.ROUND_HALF_DOWN);

		if (result.intValue() > 100) {
			BigDecimal tempbigDecimal = new BigDecimal(0);
			return tempbigDecimal;
		} else {
			return result.intValue() >= RequestConstant.RECORD_VOICE_TIME_BROADCAST ? new BigDecimal(RequestConstant.RECORD_VOICE_TIME_BROADCAST) : result;
		}
	}

	private static int login_type = 2;

	public static int getLoginType(byte b_login_type) {
		if (b_login_type == Consts.AF_LOGIN_AFID) {
			login_type = 2;
		} else if (b_login_type == Consts.AF_LOGIN_PHONE) {
			login_type = 1;
		} else if (b_login_type == Consts.AF_LOGIN_IMEI) {
			login_type = 3;
		} else if (b_login_type == Consts.AF_LOGIN_EMAIL) {
			login_type = 4;
		} else if (b_login_type == Consts.AF_LOGIN_FACEBOOK) {
			login_type = 5;
		} else if (b_login_type == Consts.AF_LOGIN_GOOGLE) {
			login_type = 6;
		} else if (b_login_type == Consts.AF_LOGIN_AUTO_CHECK) {
			login_type = 7;
		}
		return login_type;
	}

	public static boolean isInternetPicture(Uri uri) {
		if (null != uri && (DefaultValueConstant.URI_SCHEME_WWW.equals(uri.getScheme()) || DefaultValueConstant.URI_SCHEME_HTTP.equals(uri.getScheme()) || DefaultValueConstant.URI_SCHEME_HTTPS.equals(uri.getScheme()))) {
			return true;
		}
		return false;
	}

	public static int getMaritalStart(int marital_status) {
		int maritalStatus = R.string.unknown;
		switch (marital_status) {
		case 0:
			maritalStatus = R.string.single;
			break;
		case 1:
			maritalStatus = R.string.married;
			break;
		case 2:
			maritalStatus = R.string.single;
			break;
		case 3:
			maritalStatus = R.string.in_relationship;
			break;
		case 4:
			maritalStatus = R.string.widowed;
			break;
		case 5:
			maritalStatus = R.string.divorced;
			break;
		case 6:
			maritalStatus = R.string.not_public;
			break;
		case 7:
			maritalStatus = R.string.Trouble;
			break;
		case 8:
			maritalStatus = R.string.gay;
			break;
		case 9:
			maritalStatus = R.string.engaged;
			break;

		}
		return maritalStatus;
	}

	public static boolean isOverOneDay(Context context, String my_afid, String stranger_afid) {
		long lastShowTime = SharePreferenceUtils.getInstance(context).getFriendReqTime(my_afid, stranger_afid);
		long diff = System.currentTimeMillis() - lastShowTime; // compare last
		// time
		long days = diff / (1000 * 60 * 60 * 24);//
		if (days >= 1) {
			return true;
		}
		return false;
	}

	/*public static int getCorner(Bitmap bm) {
		int corner = 15;
		return corner;
	}*/

	public static String stringToJson(AfStoreDlProdInfo afStoreDlProdInfo) {
		String ver_json = "";
		JSONObject json = new JSONObject();
		try {
			PackageManager pm = PalmchatApp.getApplication().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(PalmchatApp.getApplication().getPackageName(), 0);
			json.put(pi.packageName, pi.versionCode);
			json.put(afStoreDlProdInfo.packagename, afStoreDlProdInfo.ver_code);// afStoreDlProdInfo.ver_code
			ver_json = json.toString();
			PalmchatLogUtils.println("ver_json:" + ver_json);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ver_json;
	}

	/**
	 * 计算地球上任意两点(经纬度)距离
	 * 
	 * @param long1
	 *            第一点经度
	 * @param lat1
	 *            第一点纬度
	 * @param long2
	 *            第二点经度
	 * @param lat2
	 *            第二点纬度
	 * @return 返回距离 单位：米
	 */
	public static String getDistance(double long1, double lat1,String slong2,String slat2 ) {
		try{
		double long2=Double.parseDouble(slong2); 
		double lat2=Double.parseDouble(slat2); 
		double a, b, R;
		R = 6378137; // 地球半径
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		a = lat1 - lat2;
		b = (long1 - long2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
		BigDecimal distance = new BigDecimal(d);
		BigDecimal percent = new BigDecimal(1000);
		BigDecimal result = distance.divide(percent, 2, BigDecimal.ROUND_HALF_DOWN);
		d = result.doubleValue();
		if (d < 0.01) {
			d = 0.01;
		}
		String distanceString = d + "KM";
		return distanceString;
		}catch(Exception ex){
			return "0KM";
		}
	}

	public static int getBcakgroundResIdByName(String background_name) {
		// TODO Auto-generated method stub
		if ("background0".equals(background_name)) {
			return R.color.bg_chatting;
		} else if ("background1".equals(background_name)) {
			return R.drawable.img_chatbg_00_720;
		} else if ("background2".equals(background_name)) {
			return R.drawable.img_chatbg_01_720;
		} else if ("background3".equals(background_name)) {
			return R.drawable.img_chatbg_02_720;
		} else if ("background4".equals(background_name)) {
			return R.drawable.img_chatbg_03_720;
		} else if ("background5".equals(background_name)) {
			return R.drawable.img_chatbg_04_720;
		} else if ("background6".equals(background_name)) {
			return R.drawable.img_chatbg_05_720;
		} else if ("background7".equals(background_name)) {
			return R.drawable.img_chatbg_06_720;
		} else if ("background8".equals(background_name)) {
			return R.drawable.img_chatbg_07_720;
		}

		return R.color.bg_chatting;
	}

	/**
	 * 设置相关监听器
	 */
	public static void setListener(EditText editText) {
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
			}
		});
	}

	public static boolean is_req(String name, Context context) {
		long time = SharePreferenceUtils.getInstance(context).getLastTime(name);
		PalmchatLogUtils.e("is_req", "time=" + time);
		Long hours = DateUtil.getHours(time);
		PalmchatLogUtils.e("is_req", "hours=" + hours);
		if (time <= 0) {
			SharePreferenceUtils.getInstance(context).setLastTime(name, System.currentTimeMillis());
			return false;
		} else {
			if (hours > 24) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static int emoji_w_h(Context context) {
		return dip2px(context, Constants.emoji_w_h);
	}

	public static String ToDBC(String input) {
		if (!TextUtils.isEmpty(input)) {
			char[] c = input.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (c[i] == 12288) {
					c[i] = (char) 32;
					continue;
				}
				if (c[i] > 65280 && c[i] < 65375)
					c[i] = (char) (c[i] - 65248);
			}
			return new String(c);
		}
		return input;
	}

	/**
	 * 将超过十万的数字以K表示，小数点后数直接去掉
	 * 
	 * @param number
	 * @return
	 */
	public static String getNumberByK(long number) {
		String numStr = "";
		if (number >= 100000 || number <= -100000) {
			long kNum = number / 1000;
			numStr = kNum + "K";
		} else {
			numStr = number + "";
		}
		return numStr;
	}
	public static double stringToDouble(String value) {
		double v=0;
		if(!TextUtils.isEmpty(value)){
			 try{
				 v=Double.parseDouble(value);
			 }catch(Exception e){ 
			 }
		}
		 return v;
	}
	/**
	 * 以千分位显示数据
	 * 
	 * @param numberData
	 * @return
	 */
	public static String getMicrometerData(long numberData) {
		String data = String.valueOf(numberData);
		DecimalFormat df = null;
		if (data.indexOf(".") > 0) {
			if (data.length() - data.indexOf(".") - 1 == 0) {
				df = new DecimalFormat("###,##0.");
			} else if (data.length() - data.indexOf(".") - 1 == 1) {
				df = new DecimalFormat("###,##0.0");
			} else {
				df = new DecimalFormat("###,##0.00");
			}
		} else {
			df = new DecimalFormat("###,##0");
		}
		double number = 0.0;
		try {
			number = Double.parseDouble(data);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}

	/* add by zhh 魅族、HTC等隐藏手机状态栏适配 */
	@SuppressLint("NewApi")
	public static void hideStatus(Activity activity, View view) {
		SystemBarTintManager tintManager;
		SystemBarConfig systemBarConfig;
		tintManager = new SystemBarTintManager(activity);
		systemBarConfig = tintManager.getConfig();
		if (VERSION.SDK_INT == VERSION_CODES.KITKAT) {
			if (systemBarConfig.hasNavigtionBar()) {
				if (PalmchatApp.getOsInfo().getUa().contains("HTC") || PalmchatApp.getOsInfo().getUa().contains("MI") || PalmchatApp.getOsInfo().getBrand().contains("Meizu")) {// 过滤htc,小米
					// meizu
					view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
					tintManager.setNavigationBarTintEnabled(false);
				} else {
					tintManager.setNavigationBarTintEnabled(true);
					tintManager.setNavigationBarTintColor(Color.BLACK);
					if (view != null) {
						int NavigationBarHeight = systemBarConfig.getNavigationBarHeight();
						view.setPadding(0, 0, 0, NavigationBarHeight);
					}
				}
			}
		}

	}

	/* add by zhh 魅族、HTC等显示手机状态栏适配 */
	@SuppressLint("NewApi")
	public static void showStatus(Activity activity, View view) {

		if (VERSION.SDK_INT == VERSION_CODES.KITKAT) {
			view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		}
	}

	/**
	 * 判断是否为快速点击,默认设置为500ms
	 * 
	 * @return
	 */
	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - mLastClickTime < 600) {
			return true;
		}
		mLastClickTime = time;
		return false;
	}

	/**
	 * zhh 获取国家和国码（如果有sim卡且sim卡能识别，则获取sim卡所对应的国家和国码，否则默认为尼日利亚）
	 * 
	 * @param context
	 * @return
	 */
	public static String[] getCountryAndCode(Context context) {
		String str[] = new String[2];
		String country = PalmchatApp.getOsInfo().getCountry(context);
		// 如果sim卡可用，且国家不为oversea
		// !CommonUtils.isEmpty(PalmchatApp.getOsInfo().getImsi()
		if (CommonUtils.isCanUseSim(context) && !DefaultValueConstant.OVERSEA.equals(country)) {
			str[0] = PalmchatApp.getOsInfo().getCountry(context);
			str[1] = "+" + PalmchatApp.getOsInfo().getCountryCode();

		} else { // sim卡不可用或获取国家为oversea时，国家和国码默认为尼日利亚
			str[0] = DefaultValueConstant.COUNTRY_NIGERIA;
			str[1] = "+" + DefaultValueConstant._234;
		}
		return str;
	}

	/**
	 * 判断屏幕是否锁定
	 * 
	 * @param c
	 * @return
	 */
	public final static boolean isScreenLocked(Context c) {
		android.app.KeyguardManager mKeyguardManager = (KeyguardManager) c.getSystemService(c.KEYGUARD_SERVICE);
		return mKeyguardManager.inKeyguardRestrictedInputMode();
	}

	/**
	 * zhh 获取本机号码s
	 * 
	 * @param context
	 * @return
	 */
	public final static String getMyPhoneNumber(Context context) {
		/* 获取本机号码 */
		TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String tel = telManager.getLine1Number();

		if (!CommonUtils.isEmpty(tel)) {
			String tmpTel = tel.replace("0", "");
			tmpTel = tmpTel.replace("+", "");
			if (CommonUtils.isEmpty(tmpTel)) { // 如果获取得到的手机号全部为0时
				return "";
			}

			/* 如果号码中有+号，先去掉 */
			if (tel.contains("+")) {
				String simCountryCode = "+" + PalmchatApp.getOsInfo().getCountryCode();
				/* 如果手机号中有国码，则去掉国码，只获取手机号 */
				if (tel.contains(simCountryCode)) {
					tel = tel.substring(simCountryCode.length(), tel.length());
				} else {
					tel = tel.replace("+", "");
				}
			}
			if(tel.length()>=15){
				tel=tel.substring(0,14);//某些变态的Tecno手机会取到17位数的手机号，，太变态了
			}
			return tel;
		} else {
			return "";
		}

	}
	
	/**
	 * zhh  是否为谷歌渠道
	 * @return
	 */
	public static boolean isAndroidGp() {
		return ReadyConfigXML.R_DSRC.equals(ReadyConfigXML.ANDROID_GP);
	}
	public static boolean isAndroid_yz(){
		return ReadyConfigXML.R_DSRC.equals(ReadyConfigXML.ANDROID_YZ);
	}
	/**
	 * 是否开放充值
	 * @return
	 */
	public static boolean isOpenRecharge() {
		return false;
	}
	/**
	 * 判断是否有facebook分享权限
	 * @return
	 */
	public  static boolean hasPublishPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && 
        		accessToken.getPermissions().contains(FacebookConstant.PUBLISH_ACTIONS);
    }
	
	/***/
	public static boolean isAppExist(String packageName) {
		if(TextUtils.isEmpty(packageName)){
			return false;
		}
		try {
			ApplicationInfo info = PalmchatApp.getApplication().getPackageManager().getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (NameNotFoundException e) {
		       return false;
		}
	}
	
	public static boolean isActivityExist(String packageName,String activityName) {
		if(TextUtils.isEmpty(packageName)||TextUtils.isEmpty(activityName)){
			return false ;
		}
		try {
			
			ActivityManager mActivityManager = (ActivityManager) PalmchatApp.getApplication().getSystemService(Context.ACTIVITY_SERVICE) ; 
			//获得当前正在运行的activity  
			List<ActivityManager.RunningTaskInfo> appList = mActivityManager  
					.getRunningTasks(1000);  
				for (ActivityManager.RunningTaskInfo running : appList) {  
					if(activityName.equals(running.baseActivity.getClassName())){
						return true;
					}
				}  

		}catch(Exception e) {
			PalmchatLogUtils.e("isActivityExist", e.toString());
			e.printStackTrace();
		}
		return false ;		
	}
	
	
	public static int getStatusBarHeight(Activity activity){
		DisplayMetrics dm = new DisplayMetrics();  
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);  
		Rect frame = new Rect();    
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);    
		int statusBarHeight = frame.top;  //状态栏高
		int contentTop =activity. getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
		int titleBarHeight = contentTop - statusBarHeight; //标题栏高
		return Math.abs(titleBarHeight);
	}
	
	/**
	 * 由于横竖屏问题，写个实时方法
	 * @return
	 */
	public static final int getRealtimeWindowWidth(Activity activity){
		/*@SuppressWarnings("deprecation")
		int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
		return screenWidth;*/
		return ImageUtil.DISPLAYW;
	}
	
	/**
	 * 由于横竖屏问题，写个实时方法
	 * @return
	 */
	public static final int getRealtimeWindowHeight(Activity activity){
		/*@SuppressWarnings("deprecation")
		int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
		return screenHeight;*/
		return ImageUtil.DISPLAYH;
	}

	public static void setUnKnownSpanLink(final Activity context,TextView tv,final Class goalClass){
		SpannableString spannableString = new SpannableString(context.getString(R.string.the_broadcast_unknow));
		spannableString.setSpan(new UnderlineSpan(), 59, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.log_blue)), 59, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new AbsoluteSizeSpan(16,true), 59,65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(spannableString);
		tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String url = context.getString(R.string.the_broadcast_unknow_url);
				Intent intent = new Intent();
				intent.setClass(context,goalClass);
				intent.putExtra(IntentConstant.RESOURCEURL, url);
				((Activity) context).startActivity(intent);
			}
		});
	}

	
	public static void setUrlSpanLink(Context context,TextView tv) {
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		CharSequence text = tv.getText();
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable sp = (Spannable) tv.getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			if (urls.length == 0) {
				return;
			}

			SpannableStringBuilder spannable = new SpannableStringBuilder(text);
			// 只拦截 http:// URI
			LinkedList<String> myurls = new LinkedList<String>();
			for (URLSpan uri : urls) {
				String uriString = uri.getURL();
				if (uriString.indexOf(JsonConstant.HTTP_HEAD) == 0 || (uriString.indexOf(JsonConstant.HTTPS_HEAD) == 0)) {
					myurls.add(uriString);
				}
			}
			// 循环把链接发过去
			for (URLSpan uri : urls) {
				String uriString = uri.getURL();
				if (uriString.indexOf(JsonConstant.HTTP_HEAD) == 0 || (uriString.indexOf(JsonConstant.HTTPS_HEAD) == 0)) {
					InnerUrlSpan myURLSpan = new InnerUrlSpan(context,uriString, myurls);
					spannable.setSpan(myURLSpan, sp.getSpanStart(uri), sp.getSpanEnd(uri),
							Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				}
			}
			tv.setText(spannable);
		}
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @author paulburke
	 */
	public static String getRealFilePath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {

	        // Return the remote address
	        if (isGooglePhotosUri(uri))
	            return uri.getLastPathSegment();

	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	/**
	 * 对比版本号
	 * @param version1
	 * @param version2
     * @return
     */
	public static int versionComparison(String versionLocal,String versionServer) {
		String version1 = versionServer;
		String version2 = versionLocal;
		if (version1 == null || version1.length() == 0 || version2 == null || version2.length() == 0){
			throw new IllegalArgumentException("Invalid parameter!");
		}
		int index1 = 0;
		int index2 = 0;
		while (index1 < version1.length() && index2 < version2.length()) {
			int[] number1 = getValue(version1, index1);
			int[] number2 = getValue(version2, index2);
			if (number1[0] < number2[0]){
				return -1;
			} else if (number1[0] > number2[0]){
				return 1;
			} else {
				index1 = number1[1] + 1;
				index2 = number2[1] + 1;
			}
		}
		if (index1 == version1.length() && index2 == version2.length())
			return 0;
		if (index1 < version1.length())  {
			return 1;
		} else {
			return -1;
		}

	}

	public static int[] getValue(String version, int index) {
		int[] value_index = new int[2];
		StringBuilder sb = new StringBuilder();
		while (index < version.length() && version.charAt(index) != '.') {
			sb.append(version.charAt(index));
			index++;
		}
		value_index[0] = Integer.parseInt(sb.toString());
		value_index[1] = index;
		return value_index;
	}

	/** 好友请求转换成本地语言
	 * @param context
	 * @param afMessageInfo
	 * @return
	 */
	public static String getFriendRequestContent(Context context,final AfMessageInfo afMessageInfo){
		final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
		final AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afMessageInfo.getKey());
		String showStr = "";
		if(msgType == AfMessageInfo.MESSAGE_FRIEND_REQ || msgType == AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD){
			if(afFriendInfo != null){
				showStr = CommonUtils.replace(DefaultValueConstant.TARGET_NAME,
						afFriendInfo.name, context.getString(R.string.want_to_be_friend_ignored));
			}else{
				showStr = CommonUtils.replace(DefaultValueConstant.TARGET_NAME,
						afMessageInfo.name, context.getString(R.string.want_to_be_friend_ignored));
			}
		}else if(msgType == AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS){
			if(afFriendInfo != null){
				showStr = CommonUtils.replace(DefaultValueConstant.TARGET_NAME,
						afFriendInfo.name, context.getString(R.string.frame_toast_friend_req_success));
			}else{
				showStr = CommonUtils.replace(DefaultValueConstant.TARGET_NAME,
						afMessageInfo.name, context.getString(R.string.frame_toast_friend_req_success));
			}

		}
		return showStr;
	}
	
	/**
	 * zhh 
	 * 获取恋爱状态
	 * @param marital_text
	 * @return
	 */
	public static byte getShortRelations(String marital_text) {
		byte maritalStatus = 0;
		if (PalmchatApp.getApplication().getString(R.string.single).equals(marital_text)) {
			maritalStatus = Consts.AFMOBI_MARITAL_STATUS_UNMARRIED;
		}else if (PalmchatApp.getApplication().getString(R.string.in_relationship).equals(marital_text)) {
			maritalStatus = Consts.AFMOBI_MARITAL_STATUS_LOVING;
		}  else if (PalmchatApp.getApplication().getString(R.string.married).equals(marital_text)) {
			maritalStatus = Consts.AFMOBI_MARITAL_STATUS_MARRIED;
		} else if (PalmchatApp.getApplication().getString(R.string.engaged).equals(marital_text)) {
			maritalStatus = Consts.AFMOBI_MARITAL_STATUS_ENGAGED;
		}else if (PalmchatApp.getApplication().getString(R.string.gay).equals(marital_text)) {
			maritalStatus = Consts.AFMOBI_MARITAL_STATUS_GAY;
		}else if (PalmchatApp.getApplication().getString(R.string.widowed).equals(marital_text)) {
			maritalStatus = Consts.AFMOBI_MARITAL_STATUS_WIDOWED;
		} else if (PalmchatApp.getApplication().getString(R.string.divorced).equals(marital_text)) {
			maritalStatus = Consts.AFMOBI_MARITAL_STATUS_DIVORCED;
		} else if(PalmchatApp.getApplication().getString(R.string.not_public).equals(marital_text)) {
			maritalStatus = Consts.AFMOBI_MARITAL_STATUS_NOT_PUBLIC;
		}else if(PalmchatApp.getApplication().getString(R.string.Trouble).equals(marital_text)) {
			maritalStatus = Consts.AFMOBI_MARITAL_STATUS_TROUBLED;
		}
		return maritalStatus;
	}
	/** 判断是否已follow
	 * @param afid
	 * @return
	 */
	public static boolean isFollow(String afid) {
		if (!CacheManager.getInstance().getClickableMap().containsKey(afid + CacheManager.follow_suffix)) {
			CacheManager.getInstance().getClickableMap().put(afid + CacheManager.follow_suffix, false);
		}
		return CacheManager.getInstance().isFollow(afid);
	}


	/**
	 * 拼接 头像 相片墙 的参数作为查询图片的Key(是没有的MD5化之前的key)
	 * @param serverUrl
	 * @param afid
	 * @param sn
	 * @param sizeMode
	 * @return
	 */
	public static String getAvatarUrlKey(String serverUrl,String afid,String sn,String sizeMode){
		String surl = "http://%s/d/afid=%s&pixel=%s";
		surl = String.format(surl,serverUrl ,  afid,sizeMode) ;
		if(!TextUtils.isEmpty(sn)){// 必须带sn
			surl+="&sn="+sn;

		}
		return surl+AVATAR_POST_DOWNLOAD;
	}

	/**
	 * 格式化视频时间
	 * @param timeMs 单位为毫秒
	 * @return
	 */
	public static String FormatVideoTime(int timeMs) {
		StringBuilder mFormatBuilder = new StringBuilder();
		Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
		int totalSeconds = timeMs / 1000;

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		mFormatBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

	/**
	 *
	 * @param palmcallList
     */
	public synchronized static void splicePalmcallUrlList(ArrayList<AfPalmCallResp.AfPalmCallHotListItem> palmcallList){
		if(palmcallList!=null){
			for(int i=0;i<palmcallList.size();i++){
				if(!TextUtils.isEmpty(palmcallList.get(i).mediaDescUrl)){
					palmcallList.get(i).mediaDescUrl = PalmchatApp.getApplication().getPalmcallHost()+"/"+"audio"+"/"+palmcallList.get(i).afid+".amr?ts="+palmcallList.get(i).mediaDescUrl;
				}

				if(!TextUtils.isEmpty(palmcallList.get(i).coverUrl)){
					palmcallList.get(i).coverUrl = PalmchatApp.getApplication().getPalmcallHost()+"/"+"cover"+"/"+palmcallList.get(i).afid+".jpg?ts="+palmcallList.get(i).coverUrl;
				}

			}
		}
	}

	/**
	 *
	 * @param palmCallHotListItem
     */
	public synchronized static void splicePalmcallHotlistItemUrl(AfPalmCallResp.AfPalmCallHotListItem palmCallHotListItem){
		if(palmCallHotListItem!=null){
			if(!TextUtils.isEmpty(palmCallHotListItem.mediaDescUrl)){
				palmCallHotListItem.mediaDescUrl = PalmchatApp.getApplication().getPalmcallHost()+"/"+"audio"+"/"+palmCallHotListItem.afid+".amr?ts="+palmCallHotListItem.mediaDescUrl;
			}

			if(!TextUtils.isEmpty(palmCallHotListItem.coverUrl)){
				palmCallHotListItem.coverUrl = PalmchatApp.getApplication().getPalmcallHost()+"/"+"cover"+"/"+palmCallHotListItem.afid+".jpg?ts="+palmCallHotListItem.coverUrl;
			}

		}

	}

	/**
	 * 转换palmcall 封面 url
	 * @param url
	 * @param afid
     * @return
     */
	public synchronized static String splicePalcallCoverUrl(String ts,String afid){
		if(!TextUtils.isEmpty(ts)){
			ts= PalmchatApp.getApplication().getPalmcallHost()+"/"+"cover"+"/"+afid+".jpg?ts="+ts;
		}
		return  ts;
	}

	/**
	 * 转换palmcall audio url
	 * @param url
	 * @param afid
     * @return
     */
	public synchronized static String splicePalcallAudioUrl(String ts,String afid){
		if(!TextUtils.isEmpty(ts)){
			ts = PalmchatApp.getApplication().getPalmcallHost()+"/"+"audio"+"/"+afid+".amr?ts="+ts;
		}
		return  ts;
	}

}
