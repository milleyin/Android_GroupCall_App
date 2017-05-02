package com.afmobi.palmchat.ui.customview;

import java.util.ArrayList;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
/**
 * Store banner view
 * @author starw
 *
 */
public class AdvBannerView extends RelativeLayout implements OnPageChangeListener,
				OnClickListener{

	/**类TAG*/
	private  final String  TAG = AdvBannerView.class.getSimpleName();
	
	/**默认时间间隔大小*/
	private final int TIMEINTERVAL = 4*1000;
	/**是否自动轮播*/
	private boolean mIsAutoPlaying;
	/**当前显示的是第几个*/
	private int mCurrentItem;
	/**轮播时间间隔*/
	private int mPlayTimeInterval;
	
	/**需要播放的图片*/
	private ArrayList<ImageView> mImageViews = new ArrayList<ImageView>();
	/**显示当前第几个的点*/
	private ArrayList<View> mDotsViews =new ArrayList<View>();;
	/**装饰小圆点的LinearLayout*/
	private LinearLayout mDotsLayout; 

	/**实现轮播图的组件*/
	private BannerViewPager mBannerViewPager;
	/**适配器*/
	private BannerPagerAdapter mPagerAdapter;
	/**Handler*/
	private Handler mHandler;
	/**触摸状态*/
	private boolean mIsTouched;
	/***/
	private Runnable mRunnable;
	/***/
	private int mDefaultResourceId;
	
	/**
	 * 构造方法
	 * @param context
	 */
	public AdvBannerView(Context context) {
		super(context,null);
		initBannerView();
	}
	
	/**
	 * 构造方法
	 * @param context
	 * @param attrs
	 */
	public AdvBannerView(Context context, AttributeSet attrs) {
		super(context, attrs,0);
		// TODO Auto-generated constructor stub
		initBannerView();
	}

	/**
	 *构造方法
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public AdvBannerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initBannerView();
	}
	
	/**
	 * bannerview初始化
	 */
	@SuppressLint("HandlerLeak")
	private void initBannerView() {
		mPlayTimeInterval = TIMEINTERVAL;
		
		mRunnable = new Runnable() {
			public void run() {		
				
				if(!mIsAutoPlaying){
					return;
				}
				if (mIsTouched) {
					mHandler.removeCallbacks(mRunnable);
					mHandler.postDelayed(mRunnable, mPlayTimeInterval);
					return;
				}
				
				int index = mBannerViewPager.getCurrentItem();
				int count = mDotsViews.size();
				index++;
				
				if (count > 1) {
					mBannerViewPager.setCurrentItem(index);
					mHandler.removeCallbacks(mRunnable);
					mHandler.postDelayed(mRunnable, mPlayTimeInterval);
				}
			}
		};
				
		mHandler = new Handler();
	}
	
	/**
	 * 更新banner数据
	 * @param urls  banner数据
	 * @param isAutoPlaying 是否自动轮播
	 */
	public void updateBannerData(ArrayList<String> urls,boolean isAutoPlaying)
	{
		if(null==urls||urls.size()<=0) {
			//setVisibility(View.GONE);
			return;
		}
		
		if(null!=mDotsViews) {
			mDotsViews.clear();
		}
		
		if(null!=mImageViews) {
			mImageViews.clear();
		}
		if(null!=mDotsLayout) {
			mDotsLayout.removeAllViews();
		}

		mIsAutoPlaying = isAutoPlaying;
		
		LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT); 
		layoutParams.setMargins(0, 0, 10, 10);
		for(int i = 0,length = (urls.size());i<length;i++) {
			
			if(i<length) {
				ImageView viewDot = new ImageView(getContext());
				if(0==i){
					viewDot.setBackgroundResource(R.drawable.ic_adv_page_dot_p);
				} else {
					viewDot.setBackgroundResource(R.drawable.ic_adv_page_dot_n);
				}
				mDotsViews.add(viewDot);
				mDotsLayout.addView(viewDot,layoutParams);
				
				ImageView view = new ImageView(getContext());
				view.setScaleType(ImageView.ScaleType.FIT_XY);
				
				mImageViews.add(view);
			}
		}
		
		mPagerAdapter.setDataList(urls);
		mPagerAdapter.setPagerViews(mImageViews);
		
		if(isAutoPlaying&&(mImageViews.size()>1)){
			startPlay();
		} else {
			stopPlay();
		}
	}
	
	/**
	 * 设置当前item
	 * @param index
	 */
	public void setCurrentItem(int index) {
		if(index>=mImageViews.size()||index<0){
			return;
		}
		mBannerViewPager.setCurrentItem(index);
	}
	
	/**
	 * 设置监听
	 * @param listener
	 */
	public void setOnJumpEventListener(OnJumpEventListener listener) {
		if(mPagerAdapter!=null) {
			mPagerAdapter.setOnJumpEventListener(listener);
		}
	}

	public void setDefaultDrawable(int resourceId){
		this.mDefaultResourceId = resourceId;
	}
	/**
	 * 返回ViewPager对象
	 * @return
	 */
	public ViewPager getViewPager() {
		return this.mBannerViewPager;
	}	

	/**开轮播*/
	public void startPlay() {
		mIsAutoPlaying = true;
		mHandler.removeCallbacks(mRunnable);
		mHandler.postDelayed(mRunnable, mPlayTimeInterval);
	}
	
	/**
	 * 停止轮播
	 */
	public void stopPlay(){
		mIsAutoPlaying = false;
		mHandler.removeCallbacks(mRunnable);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		if(null!=mDotsViews&&(mDotsViews.size()>0)&&(position>0)){
			position = position%mDotsViews.size();
			mCurrentItem = position;
			setDotsShowView(mCurrentItem);
		}

	}
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mImageViews =  new ArrayList<ImageView>();
		mDotsViews = new ArrayList<View>();
		
		mBannerViewPager = (BannerViewPager)findViewById(R.id.bannnerviewPager);
		
		mBannerViewPager.setPageMargin(0);
		mBannerViewPager.setOnPageChangeListener(this);
		mBannerViewPager.setOffscreenPageLimit(3);
		mPagerAdapter = new BannerPagerAdapter(getContext());
		mBannerViewPager.setAdapter(mPagerAdapter);
		
		mDotsLayout = (LinearLayout)findViewById(R.id.dots_layout);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mIsTouched = true;
				break;
			case MotionEvent.ACTION_UP:
				mIsTouched = false;
				break;
			case MotionEvent.ACTION_CANCEL:
				mIsTouched =false;
				break;
			case MotionEvent.ACTION_MOVE:
				mIsTouched = true;
				break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	/**
	 * 显示点的视图
	 * @param curIndex 显示序号
	 */
	private void setDotsShowView(int curIndex) {
		for(int i=0;i<mDotsViews.size();i++) {
			if(curIndex == i) {
				mDotsViews.get(i).setBackgroundResource(R.drawable.ic_adv_page_dot_p);
			} else {
				mDotsViews.get(i).setBackgroundResource(R.drawable.ic_adv_page_dot_n);
			}
		}
	}	
	
	public int getCount(){
		int count = 0;
		if(null!=mPagerAdapter){
			count=mPagerAdapter.getCount();
		}
		return count;
	}
	
	/**清理数据*/
	public void clean() {
		stopPlay();
		if(null!=mDotsViews) {
			mDotsViews.clear();
		}
		
		if(null!=mDotsLayout) {
			mDotsLayout.removeAllViews();
		}
		
		if(null!=mImageViews) {
			mImageViews.clear();
		}
		
		if(null!=mPagerAdapter) {
			mPagerAdapter.clean();
		}
		
	}
	
	public class BannerPagerAdapter extends PagerAdapter {
		/**上下文*/
		private Context mContext;
		/**pagerview列表*/
		private ArrayList<ImageView> mPagerViews = new ArrayList<ImageView>();;
		/**BannerInfo*/
		private ArrayList<String> mDataList = new ArrayList<String>();
		/***/	
		private OnJumpEventListener mOnJumpEventListener;
		
		/**
		 * 构造方法
		 * @param context  上下文
		 */
		public BannerPagerAdapter(Context context) {
			this.mContext = context;
		}
		
		/**
		 * 设置Views
		 * @param pagerViews
		 */
		public void setPagerViews(ArrayList<ImageView> pagerViews) {
			if((pagerViews!=null)&&(pagerViews.size()>0)&&(null!=mPagerViews)) {
				this.mPagerViews.clear();
				this.mPagerViews.addAll(pagerViews);  
				notifyDataSetChanged();
			}
			
		}
		
		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}
		/**
		 * 设置banner数据
		 * @param dataList
		 */
		public void setDataList(ArrayList<String> dataUrl) {
			if(dataUrl!=null&&(dataUrl.size()>0)&&(null!=mDataList)) {
				this.mDataList.clear();
				this.mDataList.addAll(dataUrl);
				notifyDataSetChanged();
			}
		}
		
		/**
		 * 设置监听
		 * @param addr
		 */
		public void setOnJumpEventListener(OnJumpEventListener listener) {
			this.mOnJumpEventListener = listener;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if ((null!=mDataList)&&(mDataList.size() > 1))
				return Integer.MAX_VALUE;
			return null==mDataList?0:mDataList.size();
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
			
		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			
			if(0 == mDataList.size()||position<0){
				return null;
			}
			ImageView view = new ImageView(mContext);
			view.setScaleType(ScaleType.FIT_XY);
			String url = mDataList.get(position%mDataList.size()).toString();
			if(0!=mDefaultResourceId){
				ImageManager.getInstance().DisplayImage(view, url, mDefaultResourceId,false,null);
			} else {
				ImageManager.getInstance().DisplayImage(view, url, R.drawable.banner_default,false,null);
			}


			container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			onClickHeadListener(view, position);		
			return view;
		}
			
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) container).removeView((View) object);			
		}
		
		/**
		 * 设置监听	
		 * @param imageView
		 * @param profile
		 */
		private void onClickHeadListener(ImageView imageView, final int position) {
			
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mOnJumpEventListener!=null) {
						mOnJumpEventListener.goToActivity(position%mDataList.size());
					}
				}
			});
		}
		
		public void clean() {
			if(null!=mPagerViews) {
				mPagerViews.clear();
			}
			if(null!=mDataList) {
				mDataList.clear();
			}
			notifyDataSetChanged();
		}
	}
	
	/**
	 *跳转回调 
	 * @author starw
	 *
	 */
	public interface OnJumpEventListener{
		void goToActivity(int item_id);
	}
}
