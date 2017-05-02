package com.afmobi.palmchat.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.afmobi.palmchat.util.CommonUtils;

import android.os.Environment;
import android.util.Log;

/**
 * @author Tony
 * @date 2013/08/06
 * @version
 */
public class PalmchatLogUtils {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	//connect to shenzen server
	public static final boolean SHENZHEN_VERSION =false;
	public static   boolean DEBUG = true;
	public static  boolean USE_PALMCALL=true;
	private static final String TAG = PalmchatLogUtils.class.getSimpleName();

//	private static Calendar calendar = Calendar.getInstance();
	
	
	private static boolean existSdCard = true;
	
	private static final int LOG_FILE_SIZE = 5242880;// 5 * 1024 * 1024 = 5MB
	
	public static void checkSdCard(){
		synchronized(PalmchatLogUtils.class){	
			existSdCard = CommonUtils.IsCanUseSdCard();
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(">>" + tag + "<<", msg == null ? "" : msg);
		}
		storeLogInfo(tag, msg, "E");
	}

	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(">>" + tag + "<<", msg == null ? "" : msg);
		}
		storeLogInfo(tag, msg, "D");
	}

	public static void v(String tag, String msg) {
		if (DEBUG) {
			Log.v(">>" + tag + "<<", msg == null ? "" : msg);
		}
		storeLogInfo(tag, msg, "V");
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(">>" + tag + "<<", msg == null ? "" : msg);
		}
		storeLogInfo(tag, msg, "I");
	}

	public static void w(String tag, String msg) {
		if (DEBUG) {
			Log.w(">>" + tag + "<<", msg == null ? "" : msg);
		}
		storeLogInfo(tag, msg, "W");
	}

	private static void storeLogInfo(String tag, String msg, String priority) {
		if (!DEBUG){
//			Log.e("PalmchatLogUtil", "PalmchatLogUtil  storeLogInfo");
			return;
		}
		synchronized(PalmchatLogUtils.class){
			
//			if(!CommonUtils.IsCanUseSdCard()){
//				return;
//			}
			if( !existSdCard ){
				return;
			}
		    String path = Environment.getExternalStorageDirectory().getPath()+"/Palmchat_Log_File.txt";
			File logFile = new File(path);
			if(logFile.exists()){
				long size = logFile.length();
				if(size >= LOG_FILE_SIZE){
					boolean isDelete = logFile.delete();
					System.out.println("storeLogInfo isDelete:"+isDelete);
				}
			}
			FileWriter outPutStream = null;
			PrintWriter out = null;
			try {
				outPutStream = new FileWriter(logFile, true);
				out = new PrintWriter(outPutStream);
				out.println(dateFormat.format(new Date(System.currentTimeMillis())) + "	" + priority
						+ "	" + ">>" + tag + "<<    	" + msg + '\r');
				outPutStream.flush();
//				out.close();
//				outPutStream.close();
			} catch (FileNotFoundException e) {
				//e(TAG, e.toString());
				e.printStackTrace();
			} catch (IOException e) {
				//e(TAG, e.toString());
				e.printStackTrace();
			} finally {
				try {
					if (null != outPutStream) {
						outPutStream.close();
					}
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					// e(tag, e.toString());
					e.printStackTrace();
				}
			}	
		}
	}

	public static void println(String msg) {
		if (DEBUG) {
			System.out.println(msg);
			storeLogInfo(TAG, msg, "V");
		}
	}
}
