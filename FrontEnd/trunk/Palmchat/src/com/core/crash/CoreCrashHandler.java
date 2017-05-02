package com.core.crash;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.afmobi.palmchat.log.PalmchatLogUtils;

import android.content.Context;

public class CoreCrashHandler {
	private StringBuilder mLog;
	public static final String CRASH_LOG = "log";
	private static Context g_Context = null;
	
	final public static void setContext(Context con){
		g_Context = con;
	}
	CoreCrashHandler()
	{
		
	}
	private static final boolean logcatEnable(){
	  File f = new File("/system/bin/logcat");
	  return f.exists();
	}
	
	public CoreCrashHandler(int signum)
	{
		mLog = new StringBuilder();		
		mLog.append("Palmchat core is terminated by signal (");
		mLog.append(signum);
		PalmchatLogUtils.e("Core Crash", "ywp AFMOBI_JNI: core crash:" + mLog.toString());
		getCrashLogInfo();
	}
	
	private final void getCrashLogInfo()
	{
		if( !logcatEnable()){
			return;
		}
		Runnable r = new Runnable(){
			public void run(){
				BufferedReader reader = null;
				Process mLogcat = null;
				
				try	{
					// 查找Tag为DEBUG的log信息
					mLogcat = Runtime.getRuntime().exec(new String[] { "logcat", "-s", "DEBUG", "-b", "main" });
					reader = new BufferedReader(new InputStreamReader(mLogcat.getInputStream()));
					String line;
		
					PalmchatLogUtils.e("Core Crash", "ywp AFMOBI_JNI: read core crash");
					while ((line = reader.readLine()) != null)	{
						mLog.append(line);
						mLog.append("|");
					}
				//	PalmchatLogUtils.v("AFMOBI_JNI", mLog.toString());
				}
				catch (Exception e){
					e.printStackTrace();
				}finally{
					if (reader != null){
						try{
							reader.close();
						}catch (IOException e){
							e.printStackTrace();
						}
						reader = null;
					}
					if (mLogcat != null){
						mLogcat.destroy();
						mLogcat = null;
					}
					//write sdcard
					if( null != mLog){
						PalmchatLogUtils.e("Core Crash", mLog.toString());						
						mLog = null;		
					}

				}
			}
		};
		new Thread(r).start();
	}
	
	/** plamchat内核回调函数 */
	private static final void handleCoreCrash(int signum)
	{
//		try
//		{
//			// 清空logcat中的信息
//			if( logcatEnable()){
//				Runtime.getRuntime().exec(new String[] { "logcat", "-c" });
//			}
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}		
		
//		if( null != g_Context){
//			Intent intent = new Intent(g_Context, CrashActivity.class);
//			intent.putExtra(CRASH_LOG, signum);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			g_Context.startActivity(intent);		
//		}
	}
}
