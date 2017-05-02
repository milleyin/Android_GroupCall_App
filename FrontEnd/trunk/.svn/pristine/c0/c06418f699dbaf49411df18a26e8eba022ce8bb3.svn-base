package com.afmobi.palmchat.ui.activity.setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.eventbusmodel.ShowUpdateVersionEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.setting.UpdateVersion.NotificationBroadcastReceiver;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListenerForDownLoadAPK;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfVersionInfo;
import com.core.Consts;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * 版本增量更新类
 * 
 * @author xiaolong
 *
 */
public class UpdateVersion implements AfHttpResultListener, AfHttpProgressListener, OnConfirmButtonDialogListenerForDownLoadAPK {

	/**
	 * 调用.so库中的方法,合并apk
	 * 
	 * @param old
	 *            旧Apk地址
	 * @param newapk
	 *            新apk地址(名字)
	 * @param patch
	 *            增量包地址
	 */
	public static native void patcher(String old, String newapk, String patch);

	private Context context;
	private View view;
	private final String _apk = ".apk";
	private static UpdateVersion mUpdateVersion;
	public AppDialog downLoadApkDialog;
	int handleForDownloadApk;
	private String completeApkFileName;// 合成后的完整apk名称；
	private String baseApkfileName;// 增量下载时 基础包的文件名
	private String addedApkfileName;// 下载的 增量包的文件名
	private String newVerMd5;
	private boolean isIncreaseMode;// 增量更新模式
	private final int DOWNLOADING_NOTIFY = 1, SUCCESS_NOTIFY = 2, FAILED_NOTIRY = 3,PAUSE_NOTIFY=4,PREPARE_DOWNLOADING_NOTIFY=5; // 下载状态
	private int status = DOWNLOADING_NOTIFY; // 下载时的状态，默认为正在下载
	private String reUrl, reInUrl; // 用于保存当前下载时的url或增量URL，用于重新下载用
	private long  downloadApkSize;//需要下载多大的APK包
	private String completeApkFileNameTemp;// 下载整个包时，临时的名称；
	private String strTemp = "_tmp"; // 临时文件名后缀
	private String addedApkfileNameTemp;// 下载增量包时，临时的文件名，用于断点下载
	/** Notification管理 */
	public NotificationManager mNotificationManager;// 下载的三种状态的广播
	/* zhh 下载通知字段 */
	private NotificationCompat.Builder mBuilder;
	NotificationBroadcastReceiver notifyBroadcastReceiver;
	private final String DOWNLOADING_NOTIFY_ACTION = "downloading_notify", SUCCESS_NOTIFY_ACTION = "success_notify", FAILED_NOTIFY_ACTION = "failed_notify",PAUSE_NOTIFY_ACTION="paused_nofity",PREPARE_DOWNLOADING_NOTIFY_ACTION="prepare_downloading_nofity"; // 下载时不同状态点击事件
	public static final int notifyId = 102;

	public static UpdateVersion getUpdateVersion() {
		if (mUpdateVersion == null) {
			mUpdateVersion = new UpdateVersion();
		}
		return mUpdateVersion;
	}

	/**
	 * 检查版本更新
	 * 
	 * @param productid
	 * @param afid
	 * @param packagename
	 *            包名
	 * @param channel
	 *            渠道号，诸如androidxxxxx
	 * @param user_data
	 * @param context
	 * @return
	 */
	public int AfHttpVersionCheck(String productid, String afid, String packagename, String channel, Object user_data, Context context) {
		this.context = context;
		return PalmchatApp.getApplication().mAfCorePalmchat.AfHttpVersionCheck(productid, afid, packagename, channel, user_data, this);
	}

	/**
	 * 不升级
	 */
	@Override
	public void onLeftButtonClick() {
		handleForDownloadApk= Consts.AF_HTTP_HANDLE_INVALID;
		PalmchatApp.getApplication().mAfCorePalmchat.AfHttpCancel(handleForDownloadApk);
	}

	/**
	 * 升级
	 */
	@Override
	public void onRightButtonClick(final String version, final String url, final String incUrl, final boolean isIncrease) {

		if (handleForDownloadApk != Consts.AF_HTTP_HANDLE_INVALID) { // 如果当前正在下载中
			return;
		}

		/* add by zhh 初始化通知栏 */
		initNotify();

		/* modify by zhh 将包先下载在临时文件下 */
		/*String path = RequestConstant.UPDATE_APK + completeApkFileNameTemp;// version+_apk;
		if (isIncrease) {
			path = RequestConstant.UPDATE_APK + addedApkfileNameTemp;
		}
		handleForDownloadApk = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpInstallPackageDownload(path, isIncrease ? incUrl : url, -1, path, this, this);
		*/
		reqDownload();

		PalmchatLogUtils.println("PalmchatApp  onRightButtonClick  handle  " + handleForDownloadApk);

	}

	/**
	 * 更新完成后 安装apk
	 * 
	 * @param path
	 */
	private void installApk(String path) {
		String cmd = GetChmodcmd("777", path);
		try {
			Runtime.getRuntime().exec(cmd);
			 
		} catch (Exception e) {
			// TODO: handle exception
		}
		 File file = new File(path);
		PalmchatLogUtils.println("PalmchatApp  installAPK");
		if (file != null) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			/**zhh 解决4.0以后系统覆盖安装时，不显示安装成功的界面*/
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			context.startActivity(intent);
		} 
		
		
		

	}

	private static String GetChmodcmd(String permission, String path)
	  {
		  return "chmod " + permission + " " + path;
	  }
 


	/**
	 * 弹框提示是否升级 先根据本地有无老的apk来决定下载哪个
	 * 
	 * @param afVersionInfo
	 */
	private void toDownLoadApk(AfVersionInfo afVersionInfo) {
		String version = afVersionInfo.version;
		reUrl = afVersionInfo.url;
		reInUrl = afVersionInfo.incr_url;
		String url = afVersionInfo.url;
		String incUrl = afVersionInfo.incr_url;
		downloadApkSize = afVersionInfo.size;
		newVerMd5 = afVersionInfo.md5;
		PalmchatLogUtils.println("toDownLoadApk  url  " + url + "  context  " + context + "  ");
		if (!TextUtils.isEmpty(url)) {
			PalmchatApp.getApplication().setHasNewVersion(true);
			if (context instanceof AboutActivity) { // 关于页面更新
				((AboutActivity) context).toNotify();
			}

			isIncreaseMode = false;
			if (!TextUtils.isEmpty(url)) {// 先检查完整包本地是否存在http://54.246.154.33:8081/apk/Palmchat_5_1_22_yz.apk
				int _inx = url.lastIndexOf("/");
				if (_inx >= 0) {
					completeApkFileName = url.substring(_inx + 1); // Palmchat_5_1_22_yz.apk
					completeApkFileNameTemp = completeApkFileName + strTemp; // Palmchat_5_1_22_yz.apk__tmp
					File file = new File(RequestConstant.UPDATE_APK + completeApkFileName); // .afmobi/apk/Palmchat_5_1_22_yz.apk
					if (file.exists()) {// 完整包存在
						if (verifyInstallPackage(RequestConstant.UPDATE_APK + completeApkFileName, afVersionInfo.md5)) {
							installApk(RequestConstant.UPDATE_APK + completeApkFileName);// 直接安装APK就可以了
							return;
						} else {// 检验码不对 删除错误的apk包
							File fileApk = new File(RequestConstant.UPDATE_APK + completeApkFileName);
							if (fileApk.exists())
								fileApk.delete();//
						}
					}
				}
			}
			if (!TextUtils.isEmpty(incUrl)) {// 增量包下载地址存在http://54.246.154.33:8081/?filename=Palmchat_5_1_21_yz_to_Palmchat_5_1_22_yz.apk&dir=5.1.22
				int _inx = incUrl.indexOf("filename=");
				if (_inx >= 0) {
					String _incUrl = incUrl.substring(_inx + 9);// Palmchat_5_1_21_yz_to_Palmchat_5_1_22_yz.apk&dir=5.1.22
					_inx = _incUrl.indexOf(_apk);
					String _incFileName = null;
					if (_inx >= 0) {
						// 获取增量包文件名 用于检查是否本地存在增量包
						addedApkfileName = _incFileName = _incUrl.substring(0, _inx + _apk.length());// Palmchat_5_1_21_yz_to_Palmchat_5_1_22_yz.apk
						addedApkfileNameTemp = _incUrl.substring(0, _inx + _apk.length()) + strTemp; // Palmchat_5_1_21_yz_to_Palmchat_5_1_22_yz.apk_tmp
					}

					_inx = _incUrl.indexOf("_to_");
					if (_inx >= 0) {
						baseApkfileName = _incUrl = _incUrl.substring(0, _inx) + _apk;// Palmchat_5_1_21_yz.apk
						File file = new File(RequestConstant.UPDATE_APK + _incUrl);// 检查基础包是否存在
																					// .afmobi/apk/Palmchat_5_1_21_yz.apk
						if (file.exists()) {
							isIncreaseMode = true;
							if (!TextUtils.isEmpty(_incFileName)) {
								File fileInc = new File(RequestConstant.UPDATE_APK + _incFileName);// 检查增量包是否存在.afmobi/apk/Palmchat_5_1_21_yz_to_Palmchat_5_1_22_yz.apk
								if (fileInc.exists()) {// 存在则跳过下载步骤 直接合体
									exAsyncTask();
									return;
								}
							}
						}

					}
				}

			}

			if (context != null && (AboutActivity.class.getName().equals(CommonUtils.getCurrentActivity(context)) || MainTab.class.getName().equals(CommonUtils.getCurrentActivity(context)))) {
				AppDialog appDialog = new AppDialog(context);
				appDialog.createDownloadApkDialog(context, context.getResources().getString(R.string.update_to) + " " + version + "?" + (isIncreaseMode ? R.string.incremental_updating_model : ""), url, incUrl, isIncreaseMode, version, R.layout.dialog_download_apk, this);

				try {
					appDialog.show();
				} catch (Exception e) {
					// TODO: handle exception
					PalmchatLogUtils.println("toDownLoadApk  " + e);
				} finally {
					PalmchatLogUtils.println("toDownLoadApk  finally");
				}
			}
		}

	}

	@Override
	public void AfOnProgress(int httpHandle, int flag, int progress, Object user_data) {
		/* add by zhh 更新通知栏下载状态 */
		status = DOWNLOADING_NOTIFY;
		showProgressNotify(progress);
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		if (flag == Consts.REQ_FLAG_VERSION_DOWNLOAD) { // 如果是版本下载，如论成功或失败都先释放hanlerid
			if (handleForDownloadApk == httpHandle) {
				handleForDownloadApk = Consts.AF_HTTP_HANDLE_INVALID;
			}
		}

		if (code == Consts.REQ_CODE_SUCCESS) {

			switch (flag) {

			case Consts.REQ_FLAG_VERSION_CHECK: { // to update.
				if (context instanceof AboutActivity) {
					((AboutActivity) context).dismissProgressDialog();
				}
				
				SharePreferenceUtils.getInstance(context).setCurrentVerCheckTime();
				AfVersionInfo afVersionInfo = (AfVersionInfo) result;
				PalmchatLogUtils.println("Success  Consts.REQ_FLAG_VERSION_CHECK  afVersionInfo  " + afVersionInfo);
				if (afVersionInfo != null && !TextUtils.isEmpty(afVersionInfo.url)) { // 有新版本并下载
					/* add by zhh */
					if (CommonUtils.isAndroidGp()) { // 谷歌渠道（已下架）
						SharePreferenceUtils.getInstance(context).setShelves(true);// 关于页面放开更新入口
						EventBus.getDefault().post(new ShowUpdateVersionEvent());
					}
					SharePreferenceUtils.getInstance(context).setUpdateNewVersion(afVersionInfo.version);
					PalmchatLogUtils.println("Success  afVersionInfo  " + afVersionInfo.toString());
					toDownLoadApk(afVersionInfo);
				} else { // 当前为最新版本无需下载

					if (context instanceof AboutActivity) {
						ToastManager.getInstance().show(context, R.string.no_update);
						((AboutActivity) context).toNotify();
					}
				}

				break;
			}
			case Consts.REQ_FLAG_VERSION_DOWNLOAD: { // 下载成功
				/* add by zhh 下载成功后更新通知栏下载状态 */
				status = SUCCESS_NOTIFY;
				showProgressNotify(0);

				String path = (String) user_data; // 临时文件路径
													// Palmchat_5_1_22_yz.apk_tmp
				int _inx = path.lastIndexOf(strTemp);
				if (_inx >= 0) { // 下载完成后去掉临时文件后缀，改为最终完整apk文件名
									// Palmchat_5_1_22_yz.apk
					File srcDir = new File(path);// Palmchat_5_1_22_yz.apk_tmp
					if (srcDir.exists()) {
						path = path.substring(0, _inx);
						srcDir.renameTo(new File(path));
					}

				}
				if (isIncreaseMode) {
					exAsyncTask();
				} else {
					if (verifyInstallPackage(path, newVerMd5)) {// 下载完成后校验md5
						installApk(path);
					} else {// MD5校验失败 删除新包
						File file = new File(path);
						if (file.exists())
							file.delete();//
					}

				}

				/* add by zhh 取消通知栏进度 */
				clearNotify(notifyId);
				break;
			}

			}
		} else {
			PalmchatLogUtils.println("---Frd req Suc req failed flag=" + flag + "-code = " + code);
			switch (flag) {
			case Consts.REQ_FLAG_VERSION_CHECK: { // 检查更新失败
				AfVersionInfo afVersionInfo = (AfVersionInfo) result;
				PalmchatLogUtils.println("Failure  Consts.REQ_FLAG_VERSION_CHECK  afVersionInfo  " + afVersionInfo);
				if (afVersionInfo != null) {
					PalmchatLogUtils.println("Failure  afVersionInfo  " + afVersionInfo.toString());
				}

				if (context instanceof AboutActivity) {
					((AboutActivity) context).dismissProgressDialog();
				}

				ToastManager.getInstance().show(context, R.string.network_unavailable);
				break;
			}
			case Consts.REQ_FLAG_VERSION_DOWNLOAD: { // 下载失败
				/* add zhh 更新通知栏状态 */
				status = FAILED_NOTIRY;
				showProgressNotify(0);
				ToastManager.getInstance().show(context, R.string.download_fail);
				break;
			}
			default:
				break;
			}

		}
	}

	/**
	 * 在子线程中完成apk合并,并在主线程安装apk
	 */
	private void exAsyncTask() {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = ProgressDialog.show(context, context.getResources().getString(R.string.generateNewVersion), context.getResources().getString(R.string.please_wait), true, false);
				progressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... arg0) {
				String newApk = RequestConstant.UPDATE_APK + completeApkFileName; // 下载后完整新包路径
				File file = new File(newApk);
				if (file.exists())
					file.delete();// 如果newApk文件已经存在,先删除

				// 调用.so库中的方法,把增量包和老的apk包合并成新的apk baseApkfileName:
				// Palmchat_5_1_21_yz.apk;
				// addedApkfileName:Palmchat_5_1_21_yz_to_Palmchat_5_1_22_yz.apk
				patcher(RequestConstant.UPDATE_APK + baseApkfileName// mTxtOld.getText().toString()
				, newApk, RequestConstant.UPDATE_APK + addedApkfileName);// mTxtPatcher.getText().toString());
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				progressDialog.dismiss();
				if (verifyInstallPackage(RequestConstant.UPDATE_APK + completeApkFileName, newVerMd5)) {
					installApk(RequestConstant.UPDATE_APK + completeApkFileName);
				} else {// MD5校验失败 删除新和成的包 并删除增量包
					String newApk = RequestConstant.UPDATE_APK + completeApkFileName;
					File file = new File(newApk);
					if (file.exists())
						file.delete();//

					File file2 = new File(RequestConstant.UPDATE_APK + addedApkfileName);
					if (file2.exists())
						file2.delete();
				}
			}
		};
		task.execute();
	}

	/**
	 * 校验MD5是否正确
	 * 
	 * @param packagePath
	 * @param crc
	 * @return
	 */
	public static boolean verifyInstallPackage(String packagePath, String crc) {
		try {
			MessageDigest sig = MessageDigest.getInstance("MD5");
			try {
				File packageFile = new File(packagePath);
				InputStream signedData = new FileInputStream(packageFile);
				byte[] buffer = new byte[4096];// 每次检验的文件区大小
				long toRead = packageFile.length();
				long soFar = 0;
				boolean interrupted = false;
				while (soFar < toRead) {
					interrupted = Thread.interrupted();
					if (interrupted)
						break;
					int read = signedData.read(buffer);
					soFar += read;
					sig.update(buffer, 0, read);
				}
				signedData.close();
			} catch (IOException ie) {
				ie.printStackTrace();
			}

			byte[] digest = sig.digest();
			String digestStr = bytesToHexString(digest);// 将得到的MD5值进行移位转换
			digestStr = digestStr.toLowerCase();
			crc = crc.toLowerCase();
			if (digestStr.equals(crc)) {// 比较两个文件的MD5值，如果一样则返回true
				return true;
			}

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return false;
	}

	// 2 bytesToHexString MD5值移位转换
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		int i = 0;
		while (i < src.length) {
			int v;
			String hv;
			v = (src[i] >> 4) & 0x0F;
			hv = Integer.toHexString(v);
			stringBuilder.append(hv);
			v = src[i] & 0x0F;
			hv = Integer.toHexString(v);
			stringBuilder.append(hv);
			i++;
		}
		return stringBuilder.toString();
	}

	/**
	 * zhh 初始化通知栏
	 */
	private void initNotify() {
		mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
				.setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
//				.setOngoing(true)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setAutoCancel(false).setDefaults(Notification.FLAG_ONLY_ALERT_ONCE)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
				.setSmallIcon(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP?R.drawable.icon_notify:R.drawable.app_icon_notify);
		registerBroadcastReceiver();
	}

	/** 显示带进度条通知栏 */
	private void showProgressNotify(int progress) {
		PendingIntent pendButtonIntent = null;
		String title = "", content = "";
		/** 设置通知栏点击事件 */
		switch (status) {
			case PREPARE_DOWNLOADING_NOTIFY://准备下载
				title = context.getResources().getString(R.string.app_name);
				content = context.getResources().getString(R.string.ready_to_download);
				pendButtonIntent = PendingIntent.getBroadcast(context, 0, new Intent(PREPARE_DOWNLOADING_NOTIFY_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
				mBuilder.setWhen(System.currentTimeMillis()).setContentTitle(title).setContentText(content).setContentIntent(pendButtonIntent);
				mBuilder.setProgress(0, 0, false);
				break;
		case DOWNLOADING_NOTIFY: // 正在下载
			title = context.getResources().getString(R.string.downloading) + ": " + context.getResources().getString(R.string.app_name);
			content = progress + "%";
			pendButtonIntent = PendingIntent.getBroadcast(context, 0, new Intent(DOWNLOADING_NOTIFY_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setWhen(System.currentTimeMillis()).setContentTitle(title).setContentText(content).setContentIntent(pendButtonIntent);
			mBuilder.setProgress(100, progress, false);
			break;
	 	case PAUSE_NOTIFY://暂停状态
			title = context.getResources().getString(R.string.app_name);
			content =  context.getResources().getString(R.string.paused);
			pendButtonIntent = PendingIntent.getBroadcast(context, 0, new Intent(PAUSE_NOTIFY_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setWhen(System.currentTimeMillis()).setContentTitle(title).setContentText(content).setContentIntent(pendButtonIntent);
			mBuilder.setProgress(0, 0, false);
			 break;
		case SUCCESS_NOTIFY: // 下载成功
			title = context.getResources().getString(R.string.app_name);
			content = context.getResources().getString(R.string.downloaded_finished);
			pendButtonIntent = PendingIntent.getBroadcast(context, 0, new Intent(SUCCESS_NOTIFY_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setWhen(System.currentTimeMillis()).setContentTitle(title).setContentText(content).setContentIntent(pendButtonIntent);
			mBuilder.setProgress(0, 0, false);
			break;
		case FAILED_NOTIRY: // 下载失败
			title = context.getResources().getString(R.string.palmguess_awarded_failed) + ": " + context.getResources().getString(R.string.app_name);
			content = context.getResources().getString(R.string.downloaded_failed);
			pendButtonIntent = PendingIntent.getBroadcast(context, 0, new Intent(FAILED_NOTIFY_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setWhen(System.currentTimeMillis()).setContentTitle(title).setContentText(content).setContentIntent(pendButtonIntent);
			mBuilder.setProgress(0, 0, false);
			break;

		default:
			break;
		}

		mNotificationManager.notify(notifyId, mBuilder.build());
	}

	/**
	 * 清除当前创建的通知栏
	 */
	public void clearNotify(int notifyId) {
		handleForDownloadApk = Consts.AF_HTTP_HANDLE_INVALID;
		if (null != mNotificationManager)
			mNotificationManager.cancel(notifyId);// 删除一个特定的通知ID对应的通知
		unRegisterBroadcastReceiver();
	}

	private void registerBroadcastReceiver() {
		notifyBroadcastReceiver = new NotificationBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(PREPARE_DOWNLOADING_NOTIFY_ACTION);
		filter.addAction(DOWNLOADING_NOTIFY_ACTION);
		filter.addAction(PAUSE_NOTIFY_ACTION);
		filter.addAction(SUCCESS_NOTIFY_ACTION);
		filter.addAction(FAILED_NOTIFY_ACTION);
		PalmchatApp.getApplication().registerReceiver(notifyBroadcastReceiver, filter);
	}

	private void unRegisterBroadcastReceiver() {
		if (null != notifyBroadcastReceiver)
			PalmchatApp.getApplication().unregisterReceiver(notifyBroadcastReceiver);
	}
	private  void reqDownload(){
			/* modify by zhh 将包先下载在临时文件下 */
		String path = RequestConstant.UPDATE_APK + completeApkFileNameTemp;// version+_apk;
		if (isIncreaseMode) {
			path = RequestConstant.UPDATE_APK + addedApkfileNameTemp;
		}
		handleForDownloadApk = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpInstallPackageDownload(path, isIncreaseMode ? reInUrl : reUrl, -1, path, UpdateVersion.this, UpdateVersion.this);
		PalmchatLogUtils.println("PalmchatApp  onRightButtonClick  handle  " + handleForDownloadApk);
		status = PREPARE_DOWNLOADING_NOTIFY;
		showProgressNotify(0);
	}
	class NotificationBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 在这里处理点击事件
			if(intent.getAction().equals(PREPARE_DOWNLOADING_NOTIFY_ACTION)||//准备开始下载
				 intent.getAction().equals(DOWNLOADING_NOTIFY_ACTION)) { // 正在下载时
				if (handleForDownloadApk != Consts.AF_HTTP_HANDLE_INVALID) { // 如果当前正在下载中  暂停
					PalmchatApp.getApplication().mAfCorePalmchat.AfHttpCancel(handleForDownloadApk);
					handleForDownloadApk= Consts.AF_HTTP_HANDLE_INVALID;
					status = PAUSE_NOTIFY;
					showProgressNotify(0);
				}
			} else if (intent.getAction().equals(PAUSE_NOTIFY_ACTION)) {//暂停的时候
				if (handleForDownloadApk== Consts.AF_HTTP_HANDLE_INVALID) {
					reqDownload();
				}
			}
			else if (intent.getAction().equals(SUCCESS_NOTIFY_ACTION)) {// 下载完成时候
				// 因为下载完成时会自动弹框，所以不需要处理点击去弹框事件
			} else if (intent.getAction().equals(FAILED_NOTIFY_ACTION)) {// 下载失败时

				if (handleForDownloadApk != Consts.AF_HTTP_HANDLE_INVALID) { // 如果当前正在下载中
					return;
				}
				reqDownload();

			}
		}
	}

}
