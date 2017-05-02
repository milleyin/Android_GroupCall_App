package com.afmobi.palmchat.ui.activity.profile;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.eventbusmodel.ChatImageDownloadedEvent;
import com.afmobi.palmchat.eventbusmodel.ModifyPictureListEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.ForwardSelectActivity;
import com.afmobi.palmchat.ui.activity.chats.PreviewImageActivity;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.model.DialogItem;
import com.afmobi.palmchat.ui.activity.social.BroadcastDetailActivity;
import com.afmobi.palmchat.ui.activity.social.EditBroadcastPictureActivity;
import com.afmobi.palmchat.ui.customview.FlowLayout;
import com.afmobi.palmchat.ui.customview.ImageZoomView;
import com.afmobi.palmchat.ui.customview.ListDialog;
import com.afmobi.palmchat.ui.customview.ListDialog.OnItemClick;
import com.afmobi.palmchat.ui.customview.MyImageView;
import com.afmobi.palmchat.ui.customview.SimpleZoomListener;
import com.afmobi.palmchat.ui.customview.SimpleZoomListener.ImgOnClickListener;
import com.afmobi.palmchat.ui.customview.ZoomState;
import com.afmobi.palmchat.ui.customview.ZoomViewPager;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobi.palmchat.util.universalimageloader.core.assist.FailReason;
import com.afmobi.palmchat.util.universalimageloader.core.listener.ImageLoadingListener;
import com.core.AfAttachImageInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfMFileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;
import com.core.param.AfImageReqParam;
//import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
 

/**
 * 
 * @author heguiming
 * 2013-10-30 何桂明--代码整理
 * 2015-10   WXL修改
 */
public class LargeImageDialog extends Dialog implements OnClickListener,
		OnItemClick, AfHttpResultListener, AfHttpProgressListener {

	private static final String TAG = LargeImageDialog.class.getCanonicalName();
	
	public static final int TYPE_PHOTO_WALL = 8000;
	public static final int TYPE_AVATAR = 8001;
	public static final int TYPE_CHAT = 8002;
	public static final int TYPE_SENDBROADCAST = 8003;//发广播的时候用
	public static final int TYPE_BROADCASTLIST = 8004;//广播列表 广播详情里用
	public static final int TYPE_PALMCALL_AVATAR = 8005;//palmcall 头像
	
	private ZoomViewPager mViewPage;//用来左右滑动
	//把需要滑动的页卡添加到这个list中  
	private List<View> viewList = new ArrayList<View>();
	private ArrayList<AfMessageInfo> imgMsgList = new ArrayList<AfMessageInfo>();
	private List<String> serials;
	private List<AfMFileInfo> piturePathList;
	private List<AfMFileInfo> broadcast_picturePathList;
	private String serverUrl;//下载图片的服务器地址  //palmcall头像地址
	private String afid;
	
	private int type;
	private int index;
	private boolean mIsForward = false;//是否转发
	
	private Activity mContext;
	private Toast toast;
	
	private ImagePagerAdapter mPagerAdapter;
	private BaseFragment fragment;
	private MyProfileActivity myProfileActivity;
	
	private RelativeLayout mTitleLayout;//标题布局
	
	private String showImagePath;//大图的本地存放路径
	
	private ImageView vImageViewRight;
	
	private ImageView[] imageViews;
	private ViewGroup viewPoints;
	private ImageView imageView;
	
	private AfChapterInfo afChapterInfo;

	private int act_type ;
	
	public void setFragment(BaseFragment fragment) {
		this.fragment = fragment;
	}
	
	public void setMyProfileActivity(MyProfileActivity myProfileActivity) {
		this.myProfileActivity = myProfileActivity;
	}
	
	private void onCallback() {
		if (null != myProfileActivity) {
			myProfileActivity.onCallback();
		}
	}
	 
	/**
	 * 私聊群聊里的聊天图片查看大图
	 * @param context
	 * @param imgMsgList
	 * @param index
	 * @param from
	 * @param type
	 * @param isForward
	 */
	public LargeImageDialog(Activity context, ArrayList<AfMessageInfo> imgMsgList, int index, int from, int type, boolean isForward) {
		super(context ,R.style.Theme_LargeDialog);
		
		this.mContext = context;
		this.index = index;
		this.from = from;
		this.type = type;
		this.mIsForward = isForward;
		this.imgMsgList.clear();
		if (null != imgMsgList) {
			this.imgMsgList.addAll(imgMsgList);
		}
		app = PalmchatApp.getApplication();
	}
	
	public LargeImageDialog(Activity context, List<String> serials,String serverUrl, String afid, int index, int type) {
		super(context ,R.style.Theme_LargeDialog);
		
		this.type = type;
		this.afid = afid;
		this.index = index;
		this.serials = serials;
		this.mContext = context;
		this.serverUrl = serverUrl;
	}


	/**
	 *
	 * @param context
	 * @param picUrl   头像的URL
	 * @param afid     头像AFID
     * @param type     类型
     */
	public LargeImageDialog(Activity context,String picUrl, String afid, int type) {
		super(context ,R.style.Theme_LargeDialog);
		this.type = type;
		this.afid = afid;
		this.mContext = context;
		this.serverUrl = picUrl;
	}


	/*public LargeImageDialog(Context context, ArrayList<AfMFileInfo> picturePathList,int img_index, int type , BroadcastDetailActivity broadcastDetailActivity) {
		super(context ,R.style.Theme_LargeDialog);
		this.type = type;
		this.index = img_index;
		this.broadcast_picturePathList = picturePathList;
		this.mContext = (Activity) context;
	}*/
	
	public LargeImageDialog(Context context , ArrayList<AfMFileInfo> picturePathList,int img_index, int type,AfChapterInfo afChapterInfo) {
		super(context ,R.style.Theme_LargeDialog);
		this.type = type;
		this.index = img_index;
//		this.position = position;
		this.afChapterInfo = afChapterInfo;
		this.broadcast_picturePathList = picturePathList;
		this.mContext = (Activity) context;
		this.act_type =  BroadcastDetailActivity.TYPE_BROADCASTLIST;
	}
	public LargeImageDialog(Context context , ArrayList<AfMFileInfo> picturePathList,int img_index, int type,AfChapterInfo afChapterInfo,int act_type) {
		super(context ,R.style.Theme_LargeDialog);
		this.type = type;
		this.index = img_index;
//		this.position = position;
		this.afChapterInfo = afChapterInfo;
		this.broadcast_picturePathList = picturePathList;
		this.mContext = (Activity) context;
		this.act_type = act_type;
	}
	/**
	 * 发广播界面 查看大图调用
	 * @param context
	 * @param picturePathList
	 * @param position
	 * @param type
	 */
	public LargeImageDialog(Activity context, ArrayList<AfMFileInfo> picturePathList,int position, int type) {
		super(context ,R.style.Theme_LargeDialog);
		this.type = type;
		this.index = position;
		this.piturePathList = picturePathList;
		this.mContext = context;
		
	}
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_large_image);
		mTitleLayout = (RelativeLayout) findViewById(R.id.title_layout);
		findViewById(R.id.back_button).setOnClickListener(this);
		vImageViewRight = (ImageView) findViewById(R.id.right_button);
		mViewPage = (ZoomViewPager) findViewById(R.id.viewpager);
		viewPoints = (ViewGroup) findViewById(R.id.viewPoints);
		
		mViewPage.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				selectedPageChange(arg0);
//				if (arg0 < viewList.size()) {
//// 					if(index!=arg0){
//						index = arg0;
//						showImage(viewList.get(arg0), arg0);
//// 					}
//				}
//				if (type == TYPE_BROADCASTLIST) {
//					viewPoints.setVisibility(View.VISIBLE);
//					for (int i = 0; i < imageViews.length; i++) {
//						imageViews[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.page_sel));
//						if (arg0 != i)
//							imageViews[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.page_nosle));
//					}
//				}else {
//					viewPoints.setVisibility(View.GONE);
//				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		mViewPage.setOffscreenPageLimit(1);
		
		
		if (type == TYPE_AVATAR || type == TYPE_PHOTO_WALL) {
			loadPhotoWallOrAvatarImageData();
		} else if (type == TYPE_CHAT) {
			loadChatImageData();
		} else if(type == TYPE_SENDBROADCAST){//发广播
			sendBroadcast_BottomLayout=(RelativeLayout)  findViewById(R.id.bottom_layout);
			sendBroadcast_BottomLayout.setVisibility(View.VISIBLE);
			sendBroadcast_photoIndex_textView=(TextView)  findViewById(R.id.photoindex_textview); 
			sendBroadcast_change_Button=(Button) findViewById(R.id.change_button); 
			sendBroadcast_delete_Button=(Button) findViewById(R.id.delete_button);
			sendBroadcast_change_Button.setOnClickListener(this);
			sendBroadcast_delete_Button.setOnClickListener(this);
			vImageViewRight.setVisibility(View.GONE);
//		sendBroadcast_photoIndex_textView.setText(position+"/"+piturePathList.size());
			loadBroadcastImageData();
		}else if (type == TYPE_BROADCASTLIST) {//广播列表
			loadBroadCastListImageData();
			imageViews = new ImageView[broadcast_picturePathList.size()];
			for (int i = 0; i < broadcast_picturePathList.size(); i++) {
				imageView = new ImageView(mContext);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
				lp.setMargins(0, 0, 0, 0);
				imageView.setLayoutParams(lp);
				imageView.setPadding(5, 0, 5, 0);
				imageViews[i] = imageView;
				if (i == 0)
					imageViews[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.page_sel));
				else
					imageViews[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.page_nosle));
				viewPoints.addView(imageViews[i]);
			}
		} else  if (type == TYPE_PALMCALL_AVATAR) {//palmcall头像大图
//			vImageViewRight.setVisibility(View.GONE);
			loadPalmcallAvatarImageData();
		}
		
		vImageViewRight.setOnClickListener(this);
		
		mPagerAdapter = new ImagePagerAdapter();
		mViewPage.setAdapter(mPagerAdapter);
	
		mViewPage.setCurrentItem(index);
		
		getWindow().setWindowAnimations(R.style.Theme_LargeDialog_Animation);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
//		PalmchatController.getInstance().getHandlerMap().put(LargeImageDialog.class.getCanonicalName(), mHandler);
	 
		if(type == TYPE_SENDBROADCAST){
			vImageViewRight.setBackgroundResource(R.drawable.delete_btn);
			 
		}
		
	} 
	/**
	 * 发广播界面里用
	 */
	private void loadBroadcastImageData() {
		// TODO Auto-generated method stub
		if (null != piturePathList) {
			viewList.clear();
			LayoutInflater lf = LayoutInflater.from(mContext);
			int size = piturePathList.size();
			for (int i = 0; i < size; i++) {
				AfMFileInfo afMFileInfo = piturePathList.get(i);
				if (!afMFileInfo.is_add) {
					View item = lf.inflate(R.layout.larger_image_item, null);
					viewList.add(item);
					if (i == index) {
						showImage(viewList.get(i), i);
					}
				}
			}
		}
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
		if(isDeleteSuccess){
			onCallback();
			isDeleteSuccess = false;
		}
//		unregisterListener();
	}
	 
	/**
	 * 显示不同类型的大图
	 * @param view
	 * @param position
	 */
	private void showImage(View view, final int position) {
		if (type == TYPE_AVATAR || type == TYPE_PHOTO_WALL) {
			showPhotoWallOrAvatarImage(view, position);
		} else if (type == TYPE_CHAT) {
			showChatImage(view, position);
		} else if(type == TYPE_SENDBROADCAST){
			showBroadcastImage(view, position);
		}else if (type == TYPE_BROADCASTLIST) {
			showBroadCastListImage(view, position);
		} else if(type == TYPE_PALMCALL_AVATAR) {
			showPalmcallAvatarImage(view, position);
		}
	}
	
	private MyImageView chatImageView;
	private TextView chatProgressText;
	private ImageZoomView mZoomView;//可以缩放的，大图加载出来后 就用他来放大缩小的查看图片
	private PalmchatApp app;
	private int from;
	private AfMessageInfo msgInfo;
	
	private ImageView	bigPicImgview ;//用来在广播列表的大图显示中    预览的小图加载后  用UIL去下载显示大图时做为临时过度的 
	/**
	 * 广播列表 广播详情界面点开的大图 显示。
	 * @param view
	 * @param position
	 */
	private void showBroadCastListImage(View view, int position) {
		if (null != vImageViewRight) {
			vImageViewRight.setClickable(false);
			vImageViewRight.setEnabled(false);
		} 
		Log.e("showBroadCastListImage", position+"");
		final MyImageView imageView = (MyImageView) view.findViewById(R.id.icon);
		final ProgressBar progress = (ProgressBar) view.findViewById(R.id.progressBar);
		 mZoomView = (ImageZoomView) view.findViewById(R.id.zoom_view);
		 chatProgressText = (TextView) view.findViewById(R.id.progress_text);
		mZoomView.setVisibility(View.GONE);
		int size = broadcast_picturePathList.size();
		if (size > 0) {
			boolean isSingle = false;
			if (size > 1) {
				isSingle = false;
			}else {
				isSingle = true;
			}
		
			  String thumb_url = broadcast_picturePathList.get(position).thumb_url;
			final String img_url =  broadcast_picturePathList.get(position).url;
			if (!TextUtils.isEmpty(thumb_url) && !TextUtils.isEmpty(img_url)) {
				final String thumb_url_resize = CacheManager.getInstance().getThumb_url(thumb_url,isSingle,afChapterInfo==null?null:afChapterInfo.pic_rule); 
				progress.setVisibility(View.VISIBLE); 
//				imageInfo= new BroadCastImageInfo(thumb_url, true, img_url ); 
				
				ImageManager.getInstance().DisplayImage(imageView, thumb_url_resize,new ImageLoadingListener(){ 
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						 
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						// TODO Auto-generated method stub
						progress.setVisibility(View.GONE);
						if(LargeImageDialog.this.isShowing()){
						 	 ToastManager.getInstance().show(LargeImageDialog.this.getContext(), R.string.fail_to_load_picture);
						  }
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) { 
						thumbZoom(loadedImage,imageView);
						//当小预览图加载完成后 去显示大图
						 if(bigPicImgview==null){//因为UIL加载图片必须定义一个ImageView 但是这里的图又要用来显示缩率图  所以只好另外new一个
							 bigPicImgview=new ImageView(mContext);
						 }
						displayBroadcastBigPic(imageView,bigPicImgview, thumb_url_resize,img_url,progress,broadcast_picturePathList.size()==1 );
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub
						progress.setVisibility(View.GONE);
//						ToastManager.getInstance().show(mContext, R.string.download_fail);
					}
					
				}); 
			}else {
				if (null != vImageViewRight) {
					vImageViewRight.setClickable(true);
					vImageViewRight.setEnabled(true);
				}
				String showImagePath = broadcast_picturePathList.get(position).local_img_path;
				showZoomImage(showImagePath, null,imageView, mZoomView, chatProgressText);
			}
		}
	}
	
	/**
	 * 判断广播里的缩图和原图 哪个更大 ，如果缩图本身大小就已经跟原图一样了 那就直接用缩图表示原图就好了
	 * @param thumb_url  缩图 
	 * @param url        原图
	 * @return
	 */
	private String getBigImg_url(String thumb_url,String url ){ 
		String sSize="";
		int _w=0,_h=0;
		String newPaht=url;
		try{
			int index=url.indexOf("_"); ///images/2015/11/5/f8cd21233d69d7c3ba5e74dda7ff3475_600_500.jpg
			if(index>=0){//获取原图尺寸大小
				sSize=url.substring(index+1);
				index=sSize.indexOf("_"); //600_500.jpg
				if(index>=0){
					  _w=Integer.parseInt(sSize.substring(0, index));
					sSize=sSize.substring(index+1);//500.jpg
					index=sSize.indexOf(".");
					if(index>=0){
						_h=Integer.parseInt(sSize.substring(0, index));//500 
					}
				}
			}  
			int _thumbSize=0; 
			//获取缩率图尺寸大小
				if (PalmchatApp.getApplication().getWindowWidth() >= 1080) {
					_thumbSize=768;
				}else if (PalmchatApp.getApplication().getWindowWidth() >= 720) {
					_thumbSize=512;
				}else if (PalmchatApp.getApplication().getWindowWidth() >= 480) {
					_thumbSize=320;
				}else if (PalmchatApp.getApplication().getWindowWidth() >= 320) {
					_thumbSize=160;
				}  
			if(thumb_url.contains("64_64"))	{//老广播
				if(_thumbSize+10>=_w&&_thumbSize+10>=_h){//如果缩图尺寸大于等于原图尺寸 那就用缩图  只所以加10是因为有的图刚好比缩图大几个像素，为了节省流量 牺牲这几个像素是看不出来的。
					newPaht = thumb_url.replace("64_64", "W"+_thumbSize+"_"+"H"+_thumbSize);
				} 
			}else{//新广播
				newPaht = newPaht;
			}
		}catch(Exception e){
			PalmchatLogUtils.e(TAG, e.toString());
		}
		return newPaht;
	}
	
	/**
	 * 显示avatar大图   头像和 个人相册（相片墙）
	 * @param imageView_icon
	 * @param imageView_big_temp
	 * @param serverUrl
	 * @param progress
	 */
	private void displayAvatarBigPic(final ImageView imageView_icon,final ImageView imageView_big_temp,final String serverUrl,final String sn,final String afid,byte sex,final ProgressBar progress ){
		ImageManager.getInstance().DisplayAvatarImage_PhotoWall(imageView_big_temp,  serverUrl, afid,Consts.AF_HEAD_MAX_LARGE ,
				sex,sn,false,false,new ImageLoadingListener(){ 
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				progress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				// TODO Auto-generated method stub
				
				progress.setVisibility(View.GONE);  
				if(LargeImageDialog.this.isShowing()){
				 	 ToastManager.getInstance().show(LargeImageDialog.this.getContext(), R.string.fail_to_load_picture);
				  }
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				String path= RequestConstant.IMAGE_UIL_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(
						CommonUtils.getAvatarUrlKey(serverUrl,afid,sn,Consts.AF_HEAD_MAX_LARGE) );
				showZoomImage(path, loadedImage,imageView_icon, mZoomView, chatProgressText);
				if (null != vImageViewRight) { 
					vImageViewRight.setClickable(true);
					vImageViewRight.setEnabled(true);
				}
 				progress.setVisibility(View.GONE); 
 			    
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				progress.setVisibility(View.GONE);  
// 				imgView.setVisibility(View.GONE); 
			}
			
		}); 
	}
	/**
	 * 显示广播的大图 （当取到小图后）
	 * @param imageView_icon
	 * @param
	 * @param progress
	 */
	private void displayBroadcastBigPic(final ImageView imageView_icon,final ImageView imageView_big_temp,
			final String thumb_url, String _img_url,final ProgressBar progress ,boolean isSingle){
		  String big_img_url=_img_url;
		 if(isSingle){//单图要判断缩图和原图哪个更大 就用哪个
			 big_img_url= getBigImg_url(thumb_url, _img_url);
		 } 
		 final String  img_url=big_img_url;
		ImageManager.getInstance().DisplayImage(imageView_big_temp, img_url,new ImageLoadingListener(){

			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				progress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				// TODO Auto-generated method stub
				
				progress.setVisibility(View.GONE);  
				if(LargeImageDialog.this.isShowing()){
				 	 ToastManager.getInstance().show(LargeImageDialog.this.getContext(), R.string.fail_to_load_picture);
				  }
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				String path= RequestConstant.IMAGE_UIL_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(img_url );
				showZoomImage(path, loadedImage,imageView_icon, mZoomView, chatProgressText);
				if (null != vImageViewRight) { 
					vImageViewRight.setClickable(true);
					vImageViewRight.setEnabled(true);
				}
 				progress.setVisibility(View.GONE); 
 			    
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				progress.setVisibility(View.GONE);  
// 				imgView.setVisibility(View.GONE); 
			}
			
		}); 
	}
	private void showChatImage(View view, int position) {
		if (null != imgMsgList && !imgMsgList.isEmpty()) {
			msgInfo = imgMsgList.get(position);
			chatImageView = (MyImageView) view.findViewById(R.id.icon);
			chatImageView.setVisibility(View.VISIBLE);
			chatProgressText = (TextView) view.findViewById(R.id.progress_text);
			mZoomView = (ImageZoomView) view.findViewById(R.id.zoom_view);
			mZoomView.setVisibility(View.GONE);
			
			AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) msgInfo.attach;
			if (null != afAttachImageInfo) {
				String largeFilename = afAttachImageInfo.large_file_name;
				 if (!CommonUtils.isEmpty(largeFilename)) {
					showZoomImage(largeFilename,null, chatImageView, mZoomView, chatProgressText);
					
				} else { 
					chatProgressText.setVisibility(View.VISIBLE);
					String filepath = RequestConstant.IMAGE_CACHE + afAttachImageInfo.small_file_name;
					 Bitmap bitmap=ImageManager.getInstance().loadLocalImageSync(filepath,true);//ImageUtil.readBitMap(filepath);
					chatImageView.setImageBitmap(bitmap);
					thumbZoom(bitmap,chatImageView);
					 
					AfImageReqParam param = new AfImageReqParam();
					param.afid = CacheManager.getInstance().getMyProfile().afId;
					param.file_link = afAttachImageInfo.url;
					param.file_length = afAttachImageInfo.large_file_size;
					param.low_res = 0;
					app.mAfCorePalmchat.AfHttpMediaDownload(param, msgInfo._id, this, this);
					
					chatProgressText.setText("0KB/" + (afAttachImageInfo.large_file_size / 1024) + "KB");
					
 				}
			}
		}
	}
	
	String current_picture;
	private RelativeLayout sendBroadcast_BottomLayout;
	private TextView sendBroadcast_photoIndex_textView;
	private Button sendBroadcast_change_Button,sendBroadcast_delete_Button;
	/**
	 * 显示发送广播时候的 大图
	 * @param view
	 * @param position
	 */
	private void showBroadcastImage(View view, int position) {
		if (null != piturePathList && !piturePathList.isEmpty()) {
			current_picture = piturePathList.get(position).local_img_path;
			 
				chatImageView = (MyImageView) view.findViewById(R.id.icon);
				chatImageView.setVisibility(View.VISIBLE); 
				chatProgressText = (TextView) view.findViewById(R.id.progress_text);
				mZoomView = (ImageZoomView) view.findViewById(R.id.zoom_view); 
			sendBroadcast_photoIndex_textView.setText((position+1)+"/"+piturePathList.size());
			mZoomView.setVisibility(View.GONE);
			
			if (!CommonUtils.isEmpty(current_picture)) {
				showZoomImage(current_picture,null, chatImageView, mZoomView, chatProgressText);
			} 
		}
	}
	
	// 有大图的图片才能放大缩小
	private boolean showZoomImage(String filePath, Bitmap img,ImageView chatImageView, ImageZoomView mZoomView, TextView chatProgressText) {
		chatProgressText.setVisibility(View.GONE);
		chatImageView.setVisibility(View.GONE);
		mZoomView.setVisibility(View.VISIBLE);
		boolean isSuccess=true;
		Bitmap bitmap = img;
		if(bitmap==null){
			bitmap=ImageManager.getInstance().loadLocalImageSync( filePath ,false);//ImageUtil.readBitMap(filePath);
			if (null == bitmap) {
				isSuccess=false;
				bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.picture_normel);
			}

		}
		
		/**
		 * 删除上一个显示图片
		 */
		mZoomView.setImage(bitmap);
		
		ZoomState mZoomState = new ZoomState();
		mZoomState.setPanX(0.5f);
		mZoomState.setPanY(0.5f);
		mZoomState.setZoom(1.0f);
		mZoomState.notifyObservers();
		
		mZoomView.setZoomState(mZoomState);
		
		SimpleZoomListener mZoomListener = new SimpleZoomListener(mViewPage, new ImgOnClickListener() {
			@Override
			public void onMoveOrScale() {
				if (mTitleLayout.getVisibility() == View.VISIBLE) {
					mTitleLayout.setVisibility(View.INVISIBLE);
					if(sendBroadcast_BottomLayout!=null){
						sendBroadcast_BottomLayout.setVisibility(View.GONE);
					}
				}
			}
			
			@Override
			public void onClick() {
				if (!ListDialog.canLongClick) {
					if (mTitleLayout.getVisibility() == View.VISIBLE) {
						mTitleLayout.setVisibility(View.INVISIBLE);
						if(sendBroadcast_BottomLayout!=null){
							sendBroadcast_BottomLayout.setVisibility(View.GONE);
						}
					} else {
						mTitleLayout.setVisibility(View.VISIBLE);
						if(sendBroadcast_BottomLayout!=null){
							sendBroadcast_BottomLayout.setVisibility(View.VISIBLE);
						}
					}
				}
			}

			@Override
			public void onLongClick() {
				showItemDialog();
			}
		});
		
		mZoomListener.setZoomState(mZoomState);
		mZoomView.setOnTouchListener(mZoomListener);
		mZoomView.setOnLongClickListener(mZoomListener);
		mZoomView.setVisibility(View.VISIBLE);
		return isSuccess;
	}

	private void showPalmcallAvatarImage(View view, final int position){
		final MyImageView imageView = (MyImageView) view.findViewById(R.id.icon);
		if(bigPicImgview==null){//因为UIL加载图片必须定义一个ImageView 但是这里的图又要用来显示缩率图  所以只好另外new一个
			bigPicImgview=new ImageView(mContext);
		}

		final ProgressBar progress = (ProgressBar) view.findViewById(R.id.progressBar);
		mZoomView = (ImageZoomView) view.findViewById(R.id.zoom_view);
		chatProgressText = (TextView) view.findViewById(R.id.progress_text);
		if (null != vImageViewRight) {
			vImageViewRight.setClickable(false);
			vImageViewRight.setEnabled(false);
		}
		mZoomView.setVisibility(View.GONE);
		ImageManager.getInstance().DisplayImage(imageView, serverUrl, 0, false, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						progress.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						progress.setVisibility(View.GONE);
						if(LargeImageDialog.this.isShowing()){
							ToastManager.getInstance().show(LargeImageDialog.this.getContext(), R.string.download_fail);
						}
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						String pathOriginalDestination = RequestConstant.IMAGE_UIL_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(CommonUtils.splicePalcallCoverUrl(serverUrl,CacheManager.getInstance().getMyProfile().afId));
						if(bigPicImgview==null){//因为UIL加载图片必须定义一个ImageView 但是这里的图又要用来显示缩率图  所以只好另外new一个
							bigPicImgview=new ImageView(mContext);
						}
						imageView.setVisibility(View.GONE);
						showZoomImage(pathOriginalDestination, loadedImage,bigPicImgview, mZoomView, chatProgressText);
						progress.setVisibility(View.GONE);
						if (null != vImageViewRight) {
							vImageViewRight.setClickable(true);
							vImageViewRight.setEnabled(true);
						}
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						progress.setVisibility(View.GONE);
					}
				}
		);




	}



	private void showPhotoWallOrAvatarImage(View view, final int position) {
		final MyImageView imageView = (MyImageView) view.findViewById(R.id.icon);
		final ProgressBar progress = (ProgressBar) view.findViewById(R.id.progressBar);
		   mZoomView = (ImageZoomView) view.findViewById(R.id.zoom_view);
		  chatProgressText = (TextView) view.findViewById(R.id.progress_text);
		  if (null != vImageViewRight) {
				vImageViewRight.setClickable(false);
				vImageViewRight.setEnabled(false);
			} 
		mZoomView.setVisibility(View.GONE);
		progress.setVisibility(View.VISIBLE);
		showImagePath =  RequestConstant.IMAGE_UIL_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(
				CommonUtils.getAvatarUrlKey(serverUrl,afid,serials.get(position),Consts.AF_HEAD_MAX_LARGE) );
		ImageManager.getInstance().DisplayAvatarImage_PhotoWall(imageView,  serverUrl, afid,Consts.AF_HEAD_MIDDLE,
				Consts.AFMOBI_SEX_MALE, serials.get(position),false,false,
				new ImageLoadingListener(){ 
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub 
				
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				// TODO Auto-generated method stub
				progress.setVisibility(View.GONE);
				if(LargeImageDialog.this.isShowing()){
				 	 ToastManager.getInstance().show(LargeImageDialog.this.getContext(), R.string.fail_to_load_picture);
				  }
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub 
				thumbZoom(loadedImage,imageView);
//				//当小预览图加载完成后 去显示大图
				 if(bigPicImgview==null){//因为UIL加载图片必须定义一个ImageView 但是这里的图又要用来显示缩率图  所以只好另外new一个
					 bigPicImgview=new ImageView(mContext);
				 }
				displayAvatarBigPic(imageView,bigPicImgview, serverUrl,  serials.get(position),afid,Consts.AFMOBI_SEX_MALE,progress) ;
			} 
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
//				progress.setVisibility(View.GONE);
//				ToastManager.getInstance().show(mContext, R.string.download_fail);
			}
			
		});
		
		
		/*	largeImageInfo = new CommonImageInfo(afid, serials.get(position),Consts.AF_HEAD_MAX_LARGE, serverUrl, progress,new ZoomViewCallBack() {
					@Override
					public void downloadSuccess(Bitmap bitmap) {
						showZoomImage(showImagePath, imageView, mZoomView, chatProgressText);
					}
				});
		Bitmap bitmap = largeImageInfo.getDefaultBitmap();
		
		if (null != bitmap) {
			com.afmobi.palmchat.util.image.ImageLoader.getInstance().displayImage(imageView, largeImageInfo);
		} else {
			imageView.setBackgroundResource(R.drawable.bg_gallery_downloadfailed);
			
			LargeImageInfo largerInfo = new LargeImageInfo(afid, serials.get(position), Consts.AF_HEAD_MIDDLE,serverUrl, 
					new LargeImageCallback() {
					@Override
					public void callback(Bitmap bitmap) {
						PalmchatLogUtils.e(TAG, "----largerInfo:" + bitmap);
						if (null != bitmap) {
							BitmapDrawable background = new BitmapDrawable(bitmap);
				            imageView.setBackgroundDrawable(background);
				            
							com.afmobi.palmchat.util.image.ImageLoader.getInstance().displayImage(imageView, largeImageInfo);
						} else {
							showToast(R.string.download_fail);
							progress.setVisibility(View.GONE);
						}
					}
				});
			com.afmobi.palmchat.util.image.ImageLoader.getInstance().displayImage(imageView, largerInfo);
		}*/
	}
	/**
	 * 缩图出来后 也要撑满
	 * @param img
	 */
	private void thumbZoom(Bitmap img,ImageView imgView){
		if(img!=null){
			int source_w = img.getWidth();
			int source_h = img.getHeight();
			if (source_w > 0 && source_h > 0) { 
				int	zoom_px = source_h * ImageUtil.DISPLAYW  / source_w; 
				if (type == TYPE_AVATAR || type == TYPE_PHOTO_WALL||type==TYPE_CHAT) {
					FrameLayout.LayoutParams layout=	new FrameLayout.LayoutParams(ImageUtil.DISPLAYW , zoom_px);
					layout.setMargins(0, (ImageUtil.DISPLAYH-zoom_px)/2, 0, (ImageUtil.DISPLAYH-zoom_px)/2);
					imgView.setLayoutParams(layout); 
				}else{
					LinearLayout.LayoutParams layout=	new LinearLayout.LayoutParams(ImageUtil.DISPLAYW , zoom_px);
					layout.setMargins(0, (ImageUtil.DISPLAYH-zoom_px)/2, 0, (ImageUtil.DISPLAYH-zoom_px)/2);
					imgView.setLayoutParams(layout); 
				}
				
			}  
		} 
	}
	private void loadPhotoWallOrAvatarImageData() {
		if (null != serials) {
			viewList.clear();
			LayoutInflater lf = LayoutInflater.from(mContext);
			for (int i = 0; i < serials.size(); i++) {
				View item = lf.inflate(R.layout.larger_image_item, null);
				viewList.add(item);
				if (i == index) {
					showImage(viewList.get(i), i);
				}
			}
		}
	}

	private void loadPalmcallAvatarImageData(){
		if (null != serverUrl) {
			viewList.clear();
			LayoutInflater lf = LayoutInflater.from(mContext);
			View item = lf.inflate(R.layout.larger_image_item, null);
			viewList.add(item);
			showImage(viewList.get(0), 0);
		}
	}

	
	private void loadChatImageData() {
		if (null != imgMsgList) {
			viewList.clear();
			LayoutInflater lf = LayoutInflater.from(mContext);
			int size = imgMsgList.size();
			for (int i = 0; i < size; i++) {
				View item = lf.inflate(R.layout.larger_image_item, null);
				viewList.add(item);
				if (i == index) {
					showImage(viewList.get(i), i);
				}
			}
		}
	}
	/**
	 * 各种广播列表 广播详情里用  （除了发广播）
	 */
	private void loadBroadCastListImageData() {
		if (null != imgMsgList) {
			viewList.clear();
			LayoutInflater lf = LayoutInflater.from(mContext);
			int size = broadcast_picturePathList.size();
			for (int i = 0; i < size; i++) {
				View item = lf.inflate(R.layout.larger_image_broadcast_item, null);
				viewList.add(item);
				if (i == index) {//加载完后 默认显示用户点击的第index张图片
					showImage(viewList.get(i), i);
				}
			}
		}
	}
	
	@Override
	public void show() {
		super.show();
	}
	
	private void showItemDialog() {
		
		if (type == TYPE_AVATAR || type == TYPE_PHOTO_WALL) {
			if (StringUtil.isNullOrEmpty(showImagePath) || null == ImageManager.getInstance().loadLocalImageSync(showImagePath,false) // ImageUtil.getBitmapFromFile(showImagePath)
					) {
				return;
			}
		} else if (type == TYPE_CHAT) {
			if (null != msgInfo && null != msgInfo.attach) {
				AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) msgInfo.attach;
				if (null != afAttachImageInfo) {
					if (CommonUtils.isEmpty(afAttachImageInfo.large_file_name)) {
						return;
					}
				} else {
					return;
				}
			} else {
				return;
			}
		} else if(type == TYPE_SENDBROADCAST){ //自己发广播 不需要弹了
				return;
		}else if (type == TYPE_BROADCASTLIST) {
			if(broadcast_picturePathList == null || broadcast_picturePathList.size() <= 0){
				return;
			}
		}
		
		List<DialogItem> items = new ArrayList<DialogItem>();
		
		if (type == TYPE_CHAT) {
			items.add(new DialogItem(R.string.forward, R.layout.custom_dialog_normal, R.drawable.icon_shet_forward));
			items.add(new DialogItem(R.string.save_to_phone, R.layout.custom_dialog_normal, R.drawable.icon_shet_save_to_phone));
			/*items.add(new DialogItem(R.string.cancel, R.layout.custom_dialog_cancel, R.drawable.icon_shet_save_to_phone));*/
			
		} else if(type == TYPE_AVATAR) {
			items.add(new DialogItem(R.string.save_to_phone, R.layout.custom_dialog_normal, R.drawable.icon_shet_save_to_phone));
			/*items.add(new DialogItem(R.string.cancel, R.layout.custom_dialog_cancel));*/
			
		} else if (type == TYPE_PHOTO_WALL) {
			AfProfileInfo profile = CacheManager.getInstance().getMyProfile();
			if ((profile != null && profile.afId.equals(afid))) {
				items.add(new DialogItem(R.string.update, R.layout.custom_dialog_normal,R.drawable.ic_update));
				items.add(new DialogItem(R.string.delete, R.layout.custom_dialog_normal,R.drawable.icon_list_delete));
			}
			items.add(new DialogItem(R.string.save_to_phone, R.layout.custom_dialog_normal, R.drawable.icon_shet_save_to_phone));

		}  else if(type == TYPE_BROADCASTLIST) {
			String afid = CacheManager.getInstance().getMyProfile().afId;
			if (!afid.equals(afChapterInfo.afid)) {
				if (act_type != BroadcastDetailActivity.TYPE_RETRY_BROADCASTLIST) {
					items.add(new DialogItem(R.string.report_abuse, R.layout.custom_dialog_normal,R.drawable.broadcast_dialog_abuse));
				}
			}else {
				/*if (!afChapterInfo.mid.contains(afid+"_") && afChapterInfo.status != 512) {
					*//*items.add(new DialogItem(R.string.delete, R.layout.custom_dialog_normal));*//*
				}*//**和产品确认不需要删除按钮*/
			}
			items.add(new DialogItem(R.string.forward, R.layout.custom_dialog_normal, R.drawable.icon_shet_forward));
			items.add(new DialogItem(R.string.save_to_phone, R.layout.custom_dialog_normal, R.drawable.icon_shet_save_to_phone));
	
		} else if (type == TYPE_PALMCALL_AVATAR)
		{
			items.add(new DialogItem(R.string.save_to_phone, R.layout.custom_dialog_normal, R.drawable.icon_shet_save_to_phone));
		}
		ListDialog dialog = new ListDialog(getContext(), items);
		dialog.setItemClick(this);
		dialog.show();
	}
	/**
	 * 保存图片到相册
	 */
	private void saveToPhone(){
		if (!CommonUtils.isHasSDCard()) {
			ToastManager.getInstance().show(mContext, R.string.without_sdcard_cannot_play_voice_and_so_on);
			return;
		}
		if (type == TYPE_CHAT) {//聊天大图
			if (null != msgInfo && null != msgInfo.attach) {
				AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) msgInfo.attach;
				if (!StringUtil.isNullOrEmpty(afAttachImageInfo.large_file_name)) {
					File curFile = new File(afAttachImageInfo.large_file_name);
					if (curFile != null) {
						StringBuffer out = new StringBuffer(Environment
								.getExternalStoragePublicDirectory(
										Environment.DIRECTORY_DCIM).getAbsolutePath());
						out.append("/afmobi");
						File outFile = new File(out.toString());
						if (!outFile.exists()) {
							outFile.mkdirs();
						}
						final String saveDir = outFile.getAbsolutePath();
						out.append("/");
						out.append(curFile.getName()+DefaultValueConstant.JPG);
						
						FileUtils.copyToImg(new Handler() {
							@Override
							public void handleMessage(Message msg) {
								ToastManager.getInstance().show(mContext, mContext.getString(R.string.save_to_phone) + "" + saveDir);
							}
							
						}, curFile.getAbsolutePath(), out.toString());
						//把图片保存下来后  扫描新增的媒体文件，可以在打开相册的时候出现
						PalmchatApp.getApplication().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
								Uri.parse("file://" + out.toString())));
						
					}
				}
			}
		}else if (type == TYPE_BROADCASTLIST) {//广播列表或详情里
			if (null != broadcast_picturePathList ) {
				String path = broadcast_picturePathList.get(index).url;
				String thumb_url = broadcast_picturePathList.get(index).thumb_url;
				if (!StringUtil.isNullOrEmpty(path)) {
					if(broadcast_picturePathList.size()==1){//单图要判断缩图和原图哪个更大 就用哪个
						String thumb_url_resize = CacheManager.getInstance().getThumb_url(thumb_url,broadcast_picturePathList.size()==1,afChapterInfo==null?null:afChapterInfo.pic_rule);
						path= getBigImg_url(thumb_url_resize, path);
					}

//					if (afNearByGpsInfo.msg_id > 0) {
					String md5 = PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(path);
					path = RequestConstant.IMAGE_UIL_CACHE + md5;
//					}
					File curFile = new File(path);
					if (curFile != null) {
						StringBuffer out = new StringBuffer(Environment
								.getExternalStoragePublicDirectory(
										Environment.DIRECTORY_DCIM).getAbsolutePath());
						out.append("/afmobi");
						File outFile = new File(out.toString());
						if (!outFile.exists()) {
							outFile.mkdirs();
						}
						final String saveDir = outFile.getAbsolutePath();
						out.append("/");
						out.append(curFile.getName()+DefaultValueConstant.JPG);

						FileUtils.copyToImg(new Handler() {
							@Override
							public void handleMessage(Message msg) {
								if(msg.what==Constants.SAVE_IMG_TOPHONE_SUCCESS){
									ToastManager.getInstance().show(mContext, mContext.getString(R.string.save_to_phone) + "" + saveDir);
								}else{
									ToastManager.getInstance().show(mContext,mContext.getString(R.string.save_to_phone_failed)  );
								}
							}

						}, curFile.getAbsolutePath(), out.toString());
						//把图片保存下来后  扫描新增的媒体文件，可以在打开相册的时候出现
						PalmchatApp.getApplication().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
								Uri.parse("file://" + out.toString())));

					}
				}else {//如果是自己发的广播图片  就不需要再保存了
					if(!TextUtils.isEmpty( broadcast_picturePathList.get(index).local_img_path )){
						ToastManager.getInstance().show(mContext, mContext.getString(R.string.save_to_phone_exist));
					}
				}
			}
		}else if (type == TYPE_PALMCALL_AVATAR) {//广播列表或详情里
			String path = RequestConstant.IMAGE_UIL_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(serverUrl);
			File curFile = new File(path);
			if (curFile != null) {
				StringBuffer out = new StringBuffer(Environment
						.getExternalStoragePublicDirectory(
								Environment.DIRECTORY_DCIM).getAbsolutePath());
				out.append("/afmobi");
				File outFile = new File(out.toString());
				if (!outFile.exists()) {
					outFile.mkdirs();
				}
				final String saveDir = outFile.getAbsolutePath();
				out.append("/");
				out.append(curFile.getName() + Consts.AF_IMAGE_SUFFIX);

				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SAVE_PWALL);
//				MobclickAgent.onEvent(mContext, ReadyConfigXML.SAVE_PWALL);

				FileUtils.copyToImg(new Handler() {
					@Override
					public void handleMessage(Message msg) {
						ToastManager.getInstance().show(mContext, mContext.getString(R.string.save_to_phone) + "" + saveDir);

					}
				}, curFile.getAbsolutePath(), out.toString());

				//把图片保存下来后  扫描新增的媒体文件，可以在打开相册的时候出现
				PalmchatApp.getApplication().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
						Uri.parse("file://" + out.toString())));
			}
		}
		else {
			String path = RequestConstant.IMAGE_UIL_CACHE +  PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(CommonUtils.
					getAvatarUrlKey(serverUrl, afid,  serials.get(index), Consts.AF_HEAD_MAX_LARGE ));;
			String pathOriginalDestination = RequestConstant.IMAGE_UIL_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(serverUrl);
			File curFile = new File(path);
			if (curFile != null) {
				StringBuffer out = new StringBuffer(Environment
						.getExternalStoragePublicDirectory(
								Environment.DIRECTORY_DCIM).getAbsolutePath());
				out.append("/afmobi");
				File outFile = new File(out.toString());
				if (!outFile.exists()) {
					outFile.mkdirs();
				}
				final String saveDir = outFile.getAbsolutePath();
				out.append("/");
				out.append(curFile.getName() + Consts.AF_IMAGE_SUFFIX);
				
				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SAVE_PWALL);
//				MobclickAgent.onEvent(mContext, ReadyConfigXML.SAVE_PWALL);
				
				FileUtils.copyToImg(new Handler() {
					@Override
					public void handleMessage(Message msg) { 
						ToastManager.getInstance().show(mContext, mContext.getString(R.string.save_to_phone) + "" + saveDir);
					  
					}
				}, curFile.getAbsolutePath(), out.toString());
				
				//把图片保存下来后  扫描新增的媒体文件，可以在打开相册的时候出现
				PalmchatApp.getApplication().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
						Uri.parse("file://" + out.toString())));
			}
		}
	}
	/**
	 * 长按弹出的菜单项处理
	 */
	@Override
	public void onItemClick(DialogItem item) {
		// 保存
		if (item.getTextId() == R.string.save_to_phone) {//保存图片
			saveToPhone(); 
		} else if (item.getTextId() == R.string.delete) {//删除
			if(item.getmType() == TYPE_SENDBROADCAST){// 自己发的广播
				delete_sendBroadCast_image();
			}else if (type == TYPE_BROADCASTLIST) { //广播列表
				if (ilLargeImageDialog != null) {
					ilLargeImageDialog.onItemClickeDelete();
				} 
			}else{//相片墙 
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_PWALL);
//				MobclickAgent.onEvent(mContext, ReadyConfigXML.DEL_PWALL); 
				if (null != myProfileActivity) {
					myProfileActivity.showProgressDialog(mContext.getString(R.string.deleting));
				}
				AfPalmchat core = PalmchatApp.getApplication().mAfCorePalmchat;
				core.AfHttpAvatarDelete(afid, serials.get(this.index), -1, serials.get(index), this); 
			}
		} else if(item.getTextId() == R.string.forward) {//转发
			if (type == TYPE_BROADCASTLIST) {//广播里点大图转发
				Intent intent = new Intent(mContext, ForwardSelectActivity.class);
				intent.setType(JsonConstant.KEY_BC_FORWARD_IMAGE_TO_CHATTING);
				String local_img_path =  broadcast_picturePathList.get(index).local_img_path;
				String url = broadcast_picturePathList.get(index).url;
				String thumb_url= broadcast_picturePathList.get(index).thumb_url;
				String path = url;
				if (!TextUtils.isEmpty(local_img_path)) {//如果是自己刚发的广播，那就走这里 
					path = local_img_path;
				}else {//如果是从服务器获取的广播
					if (broadcast_picturePathList.size() == 1) {// 单图要判断缩图和原图哪个更大
																// 如果缩图>=原图
																// 那就用缩图进行转发
						String thumb_url_resize = CacheManager.getInstance().getThumb_url(thumb_url, broadcast_picturePathList.size() == 1,afChapterInfo==null?null:afChapterInfo.pic_rule);
						path = getBigImg_url(thumb_url_resize, path);
						String md5 = PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(path);
						path = RequestConstant.IMAGE_UIL_CACHE + md5;
					} else {
						path = RequestConstant.IMAGE_UIL_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(url);
					}
				}
				intent.putExtra(JsonConstant.KEY_BC_FORWARD_IMAGEPAHT, path);
				mContext.startActivity(intent);
				dismiss();
			}else {
				if (msgInfo != null) { 
					CacheManager.getInstance().setForwardMsg(msgInfo);
					mContext.startActivity(new Intent(mContext, ForwardSelectActivity.class));
				}
			}
		}else if (item.getTextId() == R.string.update) {//上传
			if (null != myProfileActivity) {
				myProfileActivity.onCallback_alertMenu(serials.get(index),index); 
			}
		}else if (item.getTextId() == R.string.report_abuse) {//举报
			 if (type == TYPE_BROADCASTLIST) { 
					 if (ilLargeImageDialog != null) {
							ilLargeImageDialog.onItemClickeReportAbuse();
					 } 
			 }
		}
	}
	
	/**
	 * 删除要发的图片
	 */
	public void delete_sendBroadCast_image(){
		index = mViewPage.getCurrentItem();
		/*if(index == 0 && type == TYPE_SENDBROADCAST && piturePathList != null){
			if(piturePathList.size() > 1){
				mViewPage.setCurrentItem(1);
				mViewPage.setCurrentItem(0);
			}
		}*/
		if (index < piturePathList.size() && index >=0) {
			String picturePath = piturePathList.get(index).local_img_path;
			Iterator<AfMFileInfo> sListIterator = piturePathList.iterator();  
			while(sListIterator.hasNext()){
				AfMFileInfo mediaParams = sListIterator.next(); 
				String picture_path = mediaParams.local_img_path;
				if(!TextUtils.isEmpty(picturePath) && picturePath.equals(picture_path)){  
					FileUtils.fileDelete(picturePath);
					sListIterator.remove();
					EventBus.getDefault().post(new ModifyPictureListEvent(ModifyPictureListEvent.DELETE,index));//通知编辑图片界面去删除这张图片
					break;
				}  
			}
			viewList.remove(index);
			int current = index ;
			if(current>=viewList.size()){
				current=index-1;
			}
			if(viewList.size() > 0){
				mPagerAdapter.notifyDataSetChanged();
				if(current < 0) {
					mViewPage.setCurrentItem(0);
					/*避免当删除第一张图片被删除时，不会回调onPageSelected()*/
					selectedPageChange(0);
				}else {
					if( current < viewList.size()) {
						mViewPage.setCurrentItem(current);
					}
					selectedPageChange(current);
				}
//				if(current >= 0 && viewList.size() > current-1){
//					mViewPage.setCurrentItem(current);
//				}else{
//					mViewPage.setCurrentItem(0);
//				}
				
			}else{
				dismiss();
			}
		}else {
			dismiss();
		}
	}
	
	private boolean isDeleteSuccess;
	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result,Object user_data) {
		if (null != myProfileActivity) {
			myProfileActivity.dismissProgressDialog();
		}
		PalmchatLogUtils.println("LargeImageDialog  httpHandle  "+httpHandle+"  flag  "+flag+"  code  "+code+"  result  "+result);
		if (code == Consts.REQ_CODE_SUCCESS) {
			if (flag == Consts.REQ_AVATAR_DELETE) {
				//删掉当前删除的sn
				if (user_data != null && user_data instanceof String) {
					String sn = (String) user_data;
					AfProfileInfo info = CacheManager.getInstance().getMyProfile();
					List<String> serials = info.getSerials();
					serials.remove(sn);
					//attr1复位
					info.serialToAttr1();
					PalmchatApp.getApplication().mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, info);
					onCallback();
					isDeleteSuccess = true;
					dismiss();
				}
			} else {
				// download success
				if (null != result && result instanceof String) {
					String large_file_name = (String) result;
					if (null != chatProgressText) {
						chatProgressText.setVisibility(View.GONE);
					}
					
					if (null != user_data && user_data instanceof Integer) {
						int _id = (Integer) user_data;
						if (null != msgInfo && null != msgInfo.attach && msgInfo._id == _id) {
							AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) msgInfo.attach;
							//如果下载成功 那就copy原图到缩图里
							if(showZoomImage(large_file_name,null, chatImageView, mZoomView, chatProgressText)){
							    String path = RequestConstant.IMAGE_CACHE + afAttachImageInfo.small_file_name;
							    FileUtils.copyToImg(large_file_name, path);
							    EventBus.getDefault().post(new ChatImageDownloadedEvent());
							} 
							
							afAttachImageInfo.large_file_name = large_file_name;
							app.mAfCorePalmchat.AfDbAttachImageSetLargePath(afAttachImageInfo._id, large_file_name);
							if (from == Consts.FROM_CHATTING) {
								app.mAfCorePalmchat.AfDbMsgSetStatus(msgInfo._id, AfMessageInfo.MESSAGE_READ);
							} else if (from == Consts.FROM_GROUP_CHAT) {
								app.mAfCorePalmchat.AfDbGrpMsgSetStatus(msgInfo._id, AfMessageInfo.MESSAGE_READ);
							}
						}
					}
					
					if(isShowing()) {
						if(mIsForward) {
							Intent intent = new Intent(mContext, ForwardSelectActivity.class);
							mContext.startActivity(intent);
						}
					}
					
					
				}
			}
		} else {
			ToastManager.getInstance().show(mContext, isShowing(), R.string.failure); 
		}
	}
	
	class ImagePagerAdapter extends PagerAdapter {

		private ImagePagerAdapter() {
			
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
//			if(isDelete){
//				isDelete = false;
//				mHandler.obtainMessage(TYPE_SENDBROADCAST).sendToTarget();
//			}
		}

		@Override
		public void finishUpdate(View container) {
			
		}

		@Override
		public int getCount() {
			return viewList.size();
		}
		
		public View getItem(int position) {
			return viewList.get(position);
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			
			View item = getItem(position);
			view.addView(item, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//			view.addView(item);
			return item;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
		
		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return PagerAdapter.POSITION_NONE;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			dismiss();
			break;
		case R.id.right_button:
			if (type == TYPE_SENDBROADCAST) {
				delete_sendBroadCast_image();
			}else {
				showItemDialog();
			}
			
			break;
		case R.id.delete_button:
			if (type == TYPE_SENDBROADCAST) {
				delete_sendBroadCast_image();
			}
			break;
		case R.id.change_button:
			if (type == TYPE_SENDBROADCAST) {
				changePicture(index);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void AfOnProgress(int httpHandle, int flag, int progress,
			Object user_data) {
		if (null != chatProgressText && type == TYPE_CHAT && null != msgInfo && null != msgInfo.attach && null != user_data) {
			
			int id = (Integer) user_data;
			if (msgInfo._id == id) {
				AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) msgInfo.attach;
				int large_file_size = afAttachImageInfo.large_file_size;
				if (large_file_size != -1) {
					int size = (int) (large_file_size * (progress / 100.0d));
					chatProgressText.setText((size / 1024) + "KB/" + (large_file_size / 1024) + "KB");
				}
			}
		}
	}
	
	/**
	 * 打开相册 替换一张图片 给发广播的时候用
	 */
	private void changePicture(int postion) {
		// TODO Auto-generated method stub 
	 
		 if(mContext!=null){
			Intent mIntent = new Intent(mContext, PreviewImageActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putInt("size",piturePathList.size()+1);//替换图片的时候只允许选一张即使已经满9张了 也可以
			mBundle.putInt("postion",postion);
			mBundle.putBoolean("isChange",true);
			
			if(!piturePathList.isEmpty()){
				String picExistPaths=""; 
				ArrayList<AfMFileInfo> tecnoCameraList = new ArrayList<AfMFileInfo>();
				for(int i=0;i<piturePathList.size();i++){
					picExistPaths+=piturePathList.get(i).original_picture_path;
					/*if(piturePathList.get(i).TecnoCameraPicName!=null){
						tecnoCameraList.add(piturePathList.get(i));// 替换图标的时候 也要兼顾tecno手机的已拍照过选择的图片
					}*/
				}
				mBundle.putString(JsonConstant.KEY_PICTRUE_EXIST_FOR_BROADCAST_EDIT, picExistPaths);
				if(!tecnoCameraList.isEmpty()){//tecno camrea pic
					mIntent.putExtra(JsonConstant.KEY_SENDBROADCAST_PICLIST, tecnoCameraList); 
				}
			}
			mBundle.putBoolean( JsonConstant.KEY_BROADCAST_EDIT_PIC,true);
			mIntent.putExtras(mBundle);
			mContext.startActivityForResult(mIntent ,MessagesUtils.PICTURE);
			 
		 }
		 
	}
	/**
	 * 发广播时  查看大图 替换完一张图片后
	 * @param postion
	 */
	public void changePictureResult(int postion ){
		current_picture=piturePathList.get(postion).local_img_path;
		showZoomImage(current_picture,null, chatImageView, mZoomView, chatProgressText);
	}
	 
	private ILargeImageDialog ilLargeImageDialog;
	public void setILargeImageDialog(ILargeImageDialog l) {
		ilLargeImageDialog = l;
	}
	public interface ILargeImageDialog{
		void onItemClickeDelete();
		void onItemClickeReportAbuse();
	}
	
	/**
	 * add by zhh
	 * 当选中项发生改变时，之所以不用onpageSlected()，是因为如果删除第一张图后，默认选中index为0,此时onpageSelected()方法不会被调用
	 */
	private void selectedPageChange(int selectedPosition) {
		if (selectedPosition < viewList.size()) {
//				if(index!=arg0){
				index = selectedPosition;
				showImage(viewList.get(selectedPosition), selectedPosition);
//				}
		}
		if (type == TYPE_BROADCASTLIST) {
			viewPoints.setVisibility(View.VISIBLE);
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.page_sel));
				if (selectedPosition != i)
					imageViews[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.page_nosle));
			}
		}else {
			viewPoints.setVisibility(View.GONE);
		}
		
	}
}