package com.afmobi.palmchat.util.universalimageloader.core;
import java.io.File;
import java.util.ArrayList;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.afmobi.palmchat.util.universalimageloader.core.assist.ImageScaleType;
import com.afmobi.palmchat.util.universalimageloader.core.assist.ImageSize;
import com.afmobi.palmchat.util.universalimageloader.core.assist.QueueProcessingType;
import com.afmobi.palmchat.util.universalimageloader.core.display.CircleBitmapDisplayer;
import com.afmobi.palmchat.util.universalimageloader.core.display.DownRoundBitmapDisplayer;
import com.afmobi.palmchat.util.universalimageloader.core.display.FitXDisplayer;
import com.afmobi.palmchat.util.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.afmobi.palmchat.util.universalimageloader.core.display.SquareCutBitmapDisplayer;
import com.afmobi.palmchat.util.universalimageloader.core.imageaware.ImageAware;
import com.afmobi.palmchat.util.universalimageloader.core.imageaware.ImageViewAware;
import com.afmobi.palmchat.util.universalimageloader.core.listener.ImageLoadingListener;
import com.afmobigroup.gphone.R;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
/**
 * 
 * 
 * 
 .showImageOnLoading(R.drawable.ic_launcher) //设置图片在下载期间显示的图片  
 .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片  
.showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中  
.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//图片的缩放方式
.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//  
.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置  
//.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//设置图片加入缓存前，对bitmap进行设置  
//.preProcessor(BitmapProcessor preProcessor)  
.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少  
.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间  
.build();//构建完成  
 *
 *
 *
 *
 *
 *
 * EXACTLY :图像将完全按比例缩小的目标大小

              EXACTLY_STRETCHED:图片会缩放到目标大小完全

              IN_SAMPLE_INT:图像将被二次采样的整数倍

              IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小

              NONE:图片不会调整
 */
//图片管理类
public class ImageManager{
	public static final String AVATAR_POST_DOWNLOAD="[POST]";//标志用post方式下载图片
	public static final String TAG = ImageManager.class.getSimpleName(); 
//	private ConcurrentHashMap<String,String> mmap = new ConcurrentHashMap<String,String>(); //afid、文件名映射列表
	private static ImageManager instance;
	
	public static ImageManager getInstance() {
		synchronized (ImageManager.class) {
			if (null == instance) {
				instance = new ImageManager();
			}
		}
		return instance;
	}
	
//	ConcurrentHashMap<String,String>  getMap(){return mmap;}
	
	/*初始化图片类,配置信息包括下载线程优先级、文件名存储、日志、缓存等
	 * @param context 方便调用系统内部函数使用
	*/
	public  void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiSt
		int defaultMemory=(int) (Runtime.getRuntime().maxMemory() / 1024/1024)/8;
		File savePath = new File(RequestConstant.IMAGE_UIL_CACHE) ;
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory() 
				//.discCacheFileNameGenerator(new CustomFileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiscCache(savePath,new CustomFileNameGenerator()))// 自定义缓存路径 
//				.memoryCache(new WeakMemoryCache())
				.memoryCacheExtraOptions(480, 800)
				 .memoryCacheSize((defaultMemory>50?50:defaultMemory ) * 1024 * 1024)    
				  .discCacheSize(100 * 1024 * 1024) 
//				.writeDebugLogs() // Remove for release app
				
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

     /** 改进方案：md5值可只生产一次就加入hashmap中 下次就不用再生成md5？
      * 下载头像 或相片墙    显示的是圆形图片
      * @param view
      * @param serverUrl
      * @param afid
      * @param sizeMode
      * @param sex
      * @param sn   个人相册 sn
      */
    public void DisplayAvatarImage(final ImageView view,final String serverUrl,String afid,String sizeMode,byte sex,String sn,ImageLoadingListener listener)
    {
    	
    	int id = R.drawable.head_female2;
		if ( sex == Consts.AFMOBI_SEX_MALE) {
			id = R.drawable.head_male2; 
		}
		if (afid != null && afid.startsWith(RequestConstant.SERVICE_FRIENDS)) {
			// 显示默认的公共帐号头像
			if (RequestConstant.PALMCHAT_ID.equals(afid)) { // 系统帐号
				id=R.drawable.palmchat_team ;// 显示系统帐号头像
			} else if (RequestConstant.TECNO_ID.equals(afid)) {
				id=R.drawable.tecno_mobile;
			} else if (RequestConstant.INFINIX_ID.equals(afid)) {
				id=R.drawable.infinix_mobile;
			} else if (RequestConstant.CARLCARE_ID.equals(afid)) {
				id=R.drawable.carlcare_service;
			} else if (RequestConstant.ITEL_ID.equals(afid)) {
				id=R.drawable.itel_mobile;
			}
		}

		String surl = null;
		if(!TextUtils.isEmpty(serverUrl)) {
			surl = CommonUtils.getAvatarUrlKey(serverUrl, afid, sn, sizeMode);
		}
		DisplayImageOptions opts = new DisplayImageOptions.Builder()
		.showImageOnLoading(id)
		.showImageForEmptyUri(id)
		.showImageOnFail(id)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)  
		.displayer(new CircleBitmapDisplayer(false))//设置圆形头像
		.build();
		ImageLoader.getInstance().displayImage(surl, view,opts,listener);
    }

	/** 改进方案：md5值可只生产一次就加入hashmap中 下次就不用再生成md5？
	 * 下载头像 或相片墙    显示的是圆形图片
	 * @param view
	 * @param serverUrl
	 * @param afid
	 * @param sizeMode
	 * @param sex
	 * @param sn   个人相册 sn
	 */
	public void DisplayAvatarImage(final ImageView view,final String imageUrl,int margin,byte sex,ImageLoadingListener listener)
	{

		int id = R.drawable.head_female2;
		if ( sex == Consts.AFMOBI_SEX_MALE) {
			id = R.drawable.head_male2;
		}

		DisplayImageOptions opts = new DisplayImageOptions.Builder()
				.showImageOnLoading(id)
				.showImageForEmptyUri(id)
				.showImageOnFail(id)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new CircleBitmapDisplayer(margin,false))//设置圆形头像
				.build();
		ImageLoader.getInstance().displayImage(imageUrl, view,opts,listener);
	}

	/**
	 * 显示上边是直角 下端是圆角的 图片 用于分享广播 分享TAG等的图片
	 * @param view
	 */
	public void DisplayDownRoundImage(final ImageView view,final String url,int resIdLoading,ImageLoadingListener listener)
	{
		/*if(TextUtils.isEmpty(url)){不再拦截 交给UIL自行处理
			view.setImageResource(resIdLoading);
			return ;
		}*/
		DisplayImageOptions opts = new DisplayImageOptions.Builder()
				.showImageOnLoading(resIdLoading)
				.showImageForEmptyUri(resIdLoading)
				.showImageOnFail(resIdLoading)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new DownRoundBitmapDisplayer(false))//设置底部圆角
				.build();
		ImageLoader.getInstance().displayImage(url, view, opts, listener);
	}
    /**
     * 用于相片墙和个人相册， 与DisplayAvatarImage 非圆形
     * @param view
     * @param serverUrl
     * @param afid
     * @param sizeMode
     * @param sex
     * @param sn
     * @param isSquareCute   是否是正方形的显示 并且是裁切放大 成为正方形的 不变形图片
     * @param listener
     */
    public void DisplayAvatarImage_PhotoWall(final ImageView view,final String serverUrl,String afid,String sizeMode,byte sex,String sn,boolean isSquareCute,boolean isAvatar,ImageLoadingListener listener)
    {
    	
    	int id = R.drawable.head_female;
		if ( sex == Consts.AFMOBI_SEX_MALE) 
			id = R.drawable.head_male;
		if(!isAvatar){
			id = R.color.color_login_bg;
		}
		String surl = null;
		if(!TextUtils.isEmpty(serverUrl)){
			surl=CommonUtils.getAvatarUrlKey(serverUrl,afid,sn,sizeMode);
		}
		if(isSquareCute){ //裁切成正方形
			DisplayImageOptions opts = new DisplayImageOptions.Builder()
			.showImageOnLoading(id)
			.showImageForEmptyUri(id)
			.showImageOnFail(id)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)     
			.displayer(new SquareCutBitmapDisplayer())
			.build();
			ImageLoader.getInstance().displayImage(surl, view,opts,listener);
		}else{
			DisplayImageOptions opts = new DisplayImageOptions.Builder()
			.showImageOnLoading(id)
			.showImageForEmptyUri(id)
			.showImageOnFail(id)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)     
			.build();  
			ImageLoader.getInstance().displayImage(surl, view,opts,listener);
		}
    }
    /**
     * 用于广播图片等直接get下载显示的
     * @param view
     * @param url
     * @param listener
     */
    public void DisplayImage(final ImageView view,final String url,ImageLoadingListener listener)
    {
    	String surl = url; 
    	if(!url.startsWith(JsonConstant.HTTP_HEAD)){//如果是广播的 是需要自己拼接url的
    		String strBroadcastHost=PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerInfo()[Constants.CORE_SERVER_BROADCAST_MEDIA_HOST];
    		if(TextUtils.isEmpty( strBroadcastHost)){
    			 /*如果host为空 表示还没登陆完成, 但New Around等缓存里的广播图片还是要能
    			  看，取图片本地缓存的机制是去掉url的host地址的 ，所以这里随便定义一个即可*/
    			surl="http://localhost:8080"+url;
    		}else{
    			surl=strBroadcastHost+url;
    		}
    	}
    	
		DisplayImageOptions opts = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.color.color_login_bg)//R.drawable.loading) 
		.showImageOnFail(R.color.color_login_bg)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();  
		ImageLoader.getInstance().displayImage(surl, view,opts ,listener); 
    }
    
    private DisplayImageOptions opts_localsImage;
    /**
     * 缓存本地图片  不采用保存到文件缓存的方式 只有内存缓存
     * @param view
     * @param uri
     * @param listener
     */
    public void DisplayLocalImage(final ImageView view,String uri,int loadingResId,int failedResId,ImageLoadingListener listener){
		if(TextUtils.isEmpty(uri)){
			return;
		}
		if(!uri.startsWith("file://")){
			uri="file://"+uri;
		}
		if(opts_localsImage==null){
			opts_localsImage = new DisplayImageOptions.Builder()
					.showImageOnLoading(loadingResId)//R.drawable.loading)
					.showImageOnFail(failedResId)
					.cacheInMemory(true)
					.cacheOnDisc(false)//本地图片不缓存到文件夹

					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
					.displayer(new SquareCutBitmapDisplayer())

//	    			.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
					.build();
		}
		ImageLoader.getInstance().displayImage(uri, view,opts_localsImage ,listener);
	}

	/**
	 * 从手机文件里载入一张图片 并显示成圆形， 并在内存缓存中
	 * @param view
	 * @param uri
	 * @param listener
	 * @param loadingResId
     * @param failedResId
     */
	public void displayLocalImage_circle(final ImageView view,String uri,int loadingResId,int failedResId,ImageLoadingListener listener){
		if(TextUtils.isEmpty(uri)){
			return;
		}
		if(!uri.startsWith("file://")){
			uri="file://"+uri;
		}
		DisplayImageOptions	opts = new DisplayImageOptions.Builder()
					.showImageOnLoading(loadingResId)
					.showImageOnFail(failedResId)
					.cacheInMemory(true)
					.cacheOnDisc(false)//本地图片不缓存到文件夹
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
					.displayer(new CircleBitmapDisplayer(false))
					.build();
		ImageLoader.getInstance().displayImage(uri, view,opts ,listener);
	}
	private DisplayImageOptions opts_ChatsImage;

	/**
	 * 处理聊天页面里的图片消息  取代原来的 ChatImageInfo，不需要再载入bitmap后还要重新设置大小 再去变为圆角，节省内存开销和加快显示图片的速度
	 * @param view
	 * @param uri
	 * @param listener
	 * @param loadingResId
	 * @param failedResId
     * @param size
     */
	public void DisplayChatImage(final ImageView view, String uri, ImageLoadingListener listener, int loadingResId, int failedResId, int size) {
		if (TextUtils.isEmpty(uri)) {
			return;
		}
		if (!uri.startsWith("file://")) {
			uri = "file://" + uri;
		}
		if (opts_ChatsImage == null) {
			opts_ChatsImage = new DisplayImageOptions.Builder()
					.cacheInMemory(true)
					.cacheOnDisc(false)//本地图片不缓存到文件夹
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
					.build();
		}
		Bitmap bitmap = ImageLoader.getInstance().loadImageSync(uri, new ImageSize(Constants.CHATIMAGE_SIZE, Constants.CHATIMAGE_SIZE), opts_ChatsImage);
	   	/*if (bitmap == null || bitmap.isRecycled()) {
			bitmap = ImageUtil.getBitmapFromFile(uri, true);
		}*/
		if (bitmap == null || bitmap.isRecycled()) {
			bitmap = BitmapFactory.decodeResource(PalmchatApp.getApplication().getResources(), failedResId);
		}
		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();

		int realWidth;
		int realHeight;

		if (bmpWidth > bmpHeight) {
			realWidth = size;
			realHeight = bmpHeight * size / bmpWidth;

		} else {
			realHeight = size;
			realWidth = bmpWidth * size / bmpHeight;
		}
		if (realWidth < realHeight / 2) {
			realWidth = realHeight / 2;
		}
		if (realHeight < realWidth / 2) {
			realHeight = realWidth / 2;
		}
		view.setImageBitmap(bitmap);
		LayoutParams layout = view.getLayoutParams();
		layout.width = realWidth;
		layout.height = realHeight;
		view.setLayoutParams(layout);
	}
	private DisplayImageOptions opts_LoadSysImage;

	/**
	 * 同步获取本地的图片
	 * @param uri
	 * @param isCacheInMemory
     * @return
     */
	public Bitmap loadLocalImageSync(String uri ,boolean isCacheInMemory){
		if(!TextUtils.isEmpty(uri)){
			if(!uri.startsWith("file://")){
				uri="file://"+uri;
			}
		}

		if(opts_LoadSysImage==null){
			opts_LoadSysImage = new DisplayImageOptions.Builder()
//					.showImageOnLoading(R.color.color_login_bg)//R.drawable.loading)
//					.showImageOnFail(R.color.color_login_bg)同步载入时 是无效的
					.cacheInMemory(isCacheInMemory)
					.cacheOnDisc(false)//本地图片不缓存到文件夹
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
					.build();
		}
		return ImageLoader.getInstance().loadImageSync(uri,null,opts_LoadSysImage );
	}

	public void loadImage(String uri ,int loadingOrFailedID ,ImageLoadingListener listener ){
		if(TextUtils.isEmpty(uri)){
			return  ;
		}
		DisplayImageOptions opts = new DisplayImageOptions.Builder()
					.showImageOnLoading(loadingOrFailedID)
					.showImageOnFail(loadingOrFailedID)
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
					.build();
		  ImageLoader.getInstance().loadImage(uri, opts,listener );
	}
	/**
	 * 用于广播图片等直接get下载显示的
	 * @param view
	 * @param url
	 * @param listener
	 */
	public void DisplayImageRoom(final ImageView view,final String url,ImageLoadingListener listener)
	{
		String surl = url;
		DisplayImageOptions opts = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.color.color_login_bg)//R.drawable.loading)
				.showImageOnFail(R.color.color_login_bg)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new CircleBitmapDisplayer(false))//设置圆形头像
				.build();
		ImageLoader.getInstance().displayImage(surl, view,opts ,listener);
	}


    /**
     * 用直接get下载显示的图片， 多了一个等比缩放
     * @param view
     * @param url
     * @param
     * @param isStretch  是否要等比缩放
     * @param listener
     */
    public void DisplayImage(final ImageView view,final String url,int loadingOrFailedResID ,boolean isStretch,ImageLoadingListener listener)
    {
    	String surl = url; 
    	if(isStretch){
    		int maxHeight=0;
    		if(Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN ){
    			maxHeight=view.getMaxHeight();
    		}
    		DisplayImageOptions opts = new DisplayImageOptions.Builder()
    		.showImageOnLoading(loadingOrFailedResID) 
    		.showImageOnFail(loadingOrFailedResID)
    		.cacheInMemory(true)
    		.cacheOnDisc(true)
    		.considerExifParams(true)
    		.displayer(new FitXDisplayer(maxHeight))
    		.bitmapConfig(Bitmap.Config.RGB_565)
    		.build();  
    		ImageLoader.getInstance().displayImage(surl, view,opts ,listener);
    	}else{
    		DisplayImageOptions opts = new DisplayImageOptions.Builder()
    		.showImageOnLoading(loadingOrFailedResID) 
    		.showImageOnFail(loadingOrFailedResID)
    		.cacheInMemory(true)
    		.cacheOnDisc(true)
    		.considerExifParams(true)
    		.bitmapConfig(Bitmap.Config.RGB_565)
    		.build();  
    		ImageLoader.getInstance().displayImage(surl, view,opts ,listener);
    	} 
    }

	/**
	 * 用直接get下载显示的图片， 多了一个等比缩放
	 * @param view
	 * @param url
	 * @param
	 * @param isStretch  是否要等比缩放
	 * @param listener
	 */
	public void DisplayImage(final ImageView view,final String url ,byte sex,boolean isStretch,ImageLoadingListener listener)
	{
		String surl = url;
		int loadingOrFailedResID = R.drawable.head_female2;
		if ( sex == Consts.AFMOBI_SEX_MALE) {
			loadingOrFailedResID = R.drawable.head_male2;
		}
		if(isStretch){
			int maxHeight=0;
			if(Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN ){
				maxHeight=view.getMaxHeight();
			}
			DisplayImageOptions opts = new DisplayImageOptions.Builder()
					.showImageOnLoading(loadingOrFailedResID)
					.showImageOnFail(loadingOrFailedResID)
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.considerExifParams(true)
					.displayer(new FitXDisplayer(maxHeight))
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();
			ImageLoader.getInstance().displayImage(surl, view,opts ,listener);
		}else{
			DisplayImageOptions opts = new DisplayImageOptions.Builder()
					.showImageOnLoading(loadingOrFailedResID)
					.showImageOnFail(loadingOrFailedResID)
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();
			ImageLoader.getInstance().displayImage(surl, view,opts ,listener);
		}
	}
	public  void clearCache(){
		ImageLoader.getInstance().clearMemoryCache();
	}
	public  void clear(){
		groupIdList.clear();
		ImageLoader.getInstance().clearMemoryCache();
	}
	private ArrayList<String> groupIdList = new ArrayList<String>();//用于过滤已经在请求群Profile中，防止多次请求

	/**
	 * 用于下载 载入 显示群组头像
	 * @param imageViews
	 * @param groupInfo
     */
	public void displayGroupHeadImage(ArrayList<ImageView> imageViews, AfGrpProfileInfo groupInfo ) {

			if (null != groupInfo && null != imageViews) {
				boolean canDownload = true;
				int length = groupInfo.members.size();
				String[] afidAry = new String[length];
				for (int i = 0; i < length; i++) {
					AfGrpProfileInfo.AfGrpProfileItemInfo member = groupInfo.members.get(i);
					afidAry[i] = member.afid;
					if (null != member.name) {
						canDownload = false;
						break;
					}
				}
				if (canDownload) {
					String afid = groupInfo.afid;
					if (!groupIdList.contains(afid)) {
						groupIdList.add(afid);
						GroupImageInfo groupImageInfo = new GroupImageInfo(imageViews, groupInfo);
						AfPalmchat afPalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
						afPalmchat.AfHttpGrpOpr(null, groupInfo.name, afid, Consts.REQ_GRP_GET_PROFILE, true, groupImageInfo, new AfHttpResultListener() {
							@Override
							public void AfOnResult(int httpHandle, int flag, int code, int http_code,final  Object result, final Object user_data) {
								if (null != user_data && user_data instanceof GroupImageInfo) {
									GroupImageInfo groupImageInfo = (GroupImageInfo) user_data;
									String afid = groupImageInfo.groupInfo.afid;
									groupIdList.remove(afid);
								}
									new Thread(new Runnable() {
										@Override
										public void run() {
											final GroupImageInfo groupImageInfo = (GroupImageInfo) user_data;

											if (null != result && null != user_data) {
												final AfGrpProfileInfo groupInfo = (AfGrpProfileInfo) result;

												AfPalmchat mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
												groupInfo.tips = mAfCorePalmchat.AfDbSettingGetTips(groupInfo.afid);

												CacheManager.getInstance()
														.getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP)
														.saveOrUpdate(groupInfo, true, true);

												for (int i = 0; i < groupInfo.members.size(); i++) {
													AfGrpProfileInfo.AfGrpProfileItemInfo memeberItem = groupInfo.members.get(i);
													AfFriendInfo friendInfo = CacheManager.getInstance().searchAllFriendInfo(memeberItem.afid);
													if (null == friendInfo) {
														AfFriendInfo info = new AfFriendInfo();
														info.afId = memeberItem.afid;
														info.name = memeberItem.name;
														info.head_img_path = memeberItem.head_image_path;
														// STRANGER
														info.type = Consts.AFMOBI_FRIEND_TYPE_STRANGER;
														info.age = memeberItem.age;
														info.signature = memeberItem.signature;
														info.sex = memeberItem.sex;
														CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_STRANGER).saveOrUpdate(info, true, true);
													}
												}
												if (null != groupImageInfo.imageViews && !groupImageInfo.imageViews.isEmpty()) {
													Activity activity = (Activity) groupImageInfo.imageViews.get(0).getContext();
													activity.runOnUiThread(new Runnable() {
														@Override
														public void run() {
															groupImageInfo.groupInfo.members = groupInfo.members;
															int size = groupInfo.members.size();
															int loadHeadSize = size > 3 ? 3 : size;
															for (int i = 0; i < loadHeadSize; i++) {
																AfGrpProfileInfo.AfGrpProfileItemInfo member = groupInfo.members.get(i);
																//WXL 20151015 调UIL的显示头像方法,统一图片管理
																ImageManager.getInstance().DisplayAvatarImage( groupImageInfo.imageViews.get(i), member.getServerUrl(),
																		member.getAfidFromHead(),Consts.AF_HEAD_MIDDLE,  member.sex,member.getSerialFromHead(),null);
															}
														}


													});
												}
											}
										}
									}).start();

							}
						});
					}
				} else {
					if (null != groupInfo) {
						ArrayList<AfGrpProfileInfo.AfGrpProfileItemInfo> members = new ArrayList<AfGrpProfileInfo.AfGrpProfileItemInfo>();
						if (!groupInfo.members.isEmpty()) {
							members.addAll(groupInfo.members);
							judgeMaster(groupInfo, members);
							int size = members.size();
							int loadHeadSize = size > 3 ? 3 : size;
							for (int i = 0; i < loadHeadSize; i++) {
								AfGrpProfileInfo.AfGrpProfileItemInfo member = members.get(i);

								//WXL 20151015 调UIL的显示头像方法,统一图片管理
								ImageManager.getInstance().DisplayAvatarImage( imageViews.get(i), member.getServerUrl(),
										member.getAfidFromHead(),Consts.AF_HEAD_MIDDLE,  member.sex,member.getSerialFromHead(),null);

							}
						}
					}
				}
			}
	}
	private void judgeMaster(AfGrpProfileInfo mGroupInfo, ArrayList<AfGrpProfileInfo.AfGrpProfileItemInfo> members) {
		int size = members.size();
		String admin = mGroupInfo.admin;
		if (!StringUtil.isNullOrEmpty(admin)) {
			AfFriendInfo myProfile = CacheManager.getInstance().getMyProfile();
			boolean isMaster = false;
			for (int i = 0; i < size; i++) {
				AfGrpProfileInfo.AfGrpProfileItemInfo member = members.get(i);
				if(member!=null&& null != myProfile && myProfile.afId!=null){
					if (null != myProfile && myProfile.afId.equals(member.afid)) {
						member.head_image_path = myProfile.head_img_path;
						member.name = myProfile.name;
					}
					if (!isMaster && admin.equals(member.afid)) {
						member.isMaster = AfGrpProfileInfo.AfGrpProfileItemInfo.IS_MASTER;
						members.remove(member);
						members.add(0, member);
						isMaster = true;
					}
				}
			}
		}
	}
	private  class GroupImageInfo {
		ArrayList<ImageView> imageViews;
		AfGrpProfileInfo groupInfo;

		GroupImageInfo(ArrayList<ImageView> imageViews,
					   AfGrpProfileInfo groupInfo) {
			this.imageViews = imageViews;
			this.groupInfo = groupInfo;
		}
	}

	/**
	 * Palmcall设置头像
	 * @param view
	 * @param serverUrl
	 * @param sex
	 * @param listener
     * @param isCircle 表示是否要设置圆形
     */
	public void DisplayAvatarImageCall(final ImageView view,final String serverUrl,byte sex,ImageLoadingListener listener,boolean isCircle,boolean isTopArc)
	{
		int id;
		String surl =  serverUrl;
		if(isCircle){
			id = R.drawable.head_female2;
			if (sex == Consts.AFMOBI_SEX_MALE)
				id = R.drawable.head_male2;
			DisplayImageOptions opts = new DisplayImageOptions.Builder()
					.showImageOnLoading(id)//R.drawable.loading)
					.showImageOnFail(id)
					.showImageForEmptyUri(id)
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new CircleBitmapDisplayer(false))//设置圆形头像
					.build();
			ImageLoader.getInstance().displayImage(surl, view,opts ,listener);
		}else{
			id = R.drawable.head_female;
			if ( sex == Consts.AFMOBI_SEX_MALE)
				id = R.drawable.head_palmcall_male;

			DisplayImageOptions opts = new DisplayImageOptions.Builder()
					.showImageOnLoading(id)
					.showImageForEmptyUri(id)
					.showImageOnFail(id)
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(isTopArc?new SimpleBitmapDisplayer():new SquareCutBitmapDisplayer())
					.build();
			ImageLoader.getInstance().displayImage(surl, view,opts,listener);
		}
	}
}


