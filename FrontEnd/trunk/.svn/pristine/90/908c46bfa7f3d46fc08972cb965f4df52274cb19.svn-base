package com.afmobi.palmchat.util;

import java.util.ArrayList;
import java.util.List;

import com.afmobi.palmchat.ui.activity.chats.PreviewImageActivity;
import com.afmobi.palmchat.ui.customview.ImageZoomView;
import com.afmobi.palmchat.ui.customview.ListDialog;
import com.afmobi.palmchat.ui.customview.SimpleZoomListener;
import com.afmobi.palmchat.ui.customview.ZoomState;
import com.afmobi.palmchat.ui.customview.ZoomViewPager;
import com.afmobi.palmchat.util.universalimageloader.core.DisplayImageOptions;
import com.afmobi.palmchat.util.universalimageloader.core.ImageLoader;
import com.afmobi.palmchat.util.universalimageloader.core.assist.FailReason;
import com.afmobi.palmchat.util.universalimageloader.core.assist.ImageScaleType;
import com.afmobi.palmchat.util.universalimageloader.core.assist.ImageSize;
import com.afmobi.palmchat.util.universalimageloader.core.display.SquareCutBitmapDisplayer;
import com.afmobi.palmchat.util.universalimageloader.core.listener.ImageLoadingListener;
import com.afmobi.palmchat.ui.customview.SimpleZoomListener.ImgOnClickListener;
import com.afmobigroup.gphone.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 本地相册预览大图 
 * @author Thinkpad
 *
 */
public class LocalImageViewPager extends Dialog implements android.view.View.OnClickListener {
	private ZoomViewPager mViewPager;
	private ArrayList<ImageZoomView> list_view;
	private Activity mContext;
	private List<String> selectedAlbumPhotosLs; // 当前选中的相册所对应的照片
	private int imgIndex;
	private MyPagerAdapter mAdapter;
	private DisplayImageOptions	opts_localsImage;
	private ImageSize targetImageSize;
	private Button mButtonOK;
	private ImageView mImageViewBack;
	public LocalImageViewPager(Activity context,List<String> photos,int index) {
		super(context ,R.style.Theme_LargeDialog);
		mContext=context;
		// TODO Auto-generated constructor stub
		selectedAlbumPhotosLs=photos;
		imgIndex=index-1;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_localimage_viewpager); 
		 
		
		mButtonOK=  (Button) findViewById(R.id.tv_ok);
		mImageViewBack=  (ImageView) findViewById(R.id.back_button);
		mViewPager=  (ZoomViewPager) findViewById(R.id.viewpager);  
		
		
		list_view=new ArrayList<ImageZoomView>();
		for(int i=0;i<6;i++){//创建6个用于重复循环利用
			list_view.add(new ImageZoomView(mContext ));
		}
		  mAdapter=new MyPagerAdapter();
		mViewPager.setAdapter(mAdapter);
		
	 
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() { 
			@Override
			public void onPageSelected(int arg0) { 
				imgIndex = arg0;
//				showZoomImage( selectedAlbumPhotosLs.get(arg0+1),list_view.get(arg0% list_view.size()) );
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
//		mViewPager.setOffscreenPageLimit(1);
	    // 让第一个当前页是 0
	    //currentItem = currentItem - ((Integer.MAX_VALUE / 2) % 4);
	    mViewPager.setCurrentItem(imgIndex);

	    

		  targetImageSize=new ImageSize(ImageUtil.DISPLAYW,ImageUtil.DISPLAYH);
		 	opts_localsImage = new DisplayImageOptions.Builder()
  			.showImageOnLoading(R.drawable.picture_normel)//R.drawable.loading) 
  			.showImageOnFail(R.drawable.picture_normel)
  			.cacheInMemory(false)
  			.cacheOnDisc(false)//本地图片不缓存到文件夹 
  			.considerExifParams(true) 
  			.bitmapConfig(Bitmap.Config.RGB_565)
  			.imageScaleType(ImageScaleType.IN_SAMPLE_INT) 
  			.build(); 
//		setContentView(mViewPager);
		 	
		 	mButtonOK.setOnClickListener(this);
		 	mImageViewBack.setOnClickListener(this);
		 	getWindow().setWindowAnimations(R.style.Theme_LargeDialog_Animation);
			getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			
	}
	
	// 有大图的图片才能放大缩小
		private void showZoomImage(String filePath , final ImageZoomView mZoomView ) {
			 
//			boolean isSuccess=true; 
			if(!filePath.startsWith("file://")){
				filePath="file://"+filePath;
	    	}
			//采用异步加载方式 加载进本地图片
			ImageLoader.getInstance().loadImage(filePath, targetImageSize, opts_localsImage, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					Bitmap bitmap=loadedImage;//ImageLoader.getInstance().loadImageSync(filePath,targetImageSize ,opts_localsImage);
				 	 
					
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
					
					SimpleZoomListener mZoomListener = new SimpleZoomListener(mViewPager, new ImgOnClickListener() {
						@Override
						public void onMoveOrScale() {
							/*if (mTitleLayout.getVisibility() == View.VISIBLE) {
								mTitleLayout.setVisibility(View.INVISIBLE);
								if(sendBroadcast_BottomLayout!=null){
									sendBroadcast_BottomLayout.setVisibility(View.GONE);
								}
							}*/
						}
						
						@Override
						public void onClick() {
							/*if (!ListDialog.canLongClick) {
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
							}*/
						}

						@Override
						public void onLongClick() {
							//showItemDialog();
						}
					});
					
					mZoomListener.setZoomState(mZoomState);
					mZoomView.setOnTouchListener(mZoomListener);
					mZoomView.setOnLongClickListener(mZoomListener);
//					mZoomView.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
			
			 
		}
	class MyPagerAdapter extends PagerAdapter{

		 public void destroyItem(View container, int position, Object object) {
			 ImageZoomView view = list_view.get(position % list_view.size()); 
			 view.recycleBitmap();
			 
			 ((ViewPager) container).removeView(view);  
		  } 
		  @Override 
		  public Object instantiateItem(View container, int position) { 
			  ImageZoomView view =list_view.get(position% list_view.size());
			  ((ViewPager) container).addView(view); 
			   
			  showZoomImage(selectedAlbumPhotosLs.get(position+1),view);
			  return view;
		   }


		@Override
		public int getCount() {
			 
			// TODO Auto-generated method stub
			return selectedAlbumPhotosLs.size()-1;
		}
		/*@Override
		  public boolean isViewFromObject(View view, Object object) {
		    return view==object;
		  }*/
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
	private void goBack(){
		ImageZoomView img=null;
		for(int i=0;i<list_view.size();i++){
		  img=	list_view.get(i);
		  if(img!=null){
			  img.recycleBitmap();
		  }
		}
		dismiss();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.tv_ok://选择了当前这张图片
				if(mContext!=null&&mContext instanceof PreviewImageActivity){
					if(imgIndex+1<selectedAlbumPhotosLs.size()){
						String path=selectedAlbumPhotosLs.get(imgIndex+1);
						((PreviewImageActivity)mContext).selectAPicInLargeDialog(path);
					}
				}
				goBack();
			break;
			
			case R.id.back_button:
				goBack();
				break; 
		} 
	 
	}
 
}
