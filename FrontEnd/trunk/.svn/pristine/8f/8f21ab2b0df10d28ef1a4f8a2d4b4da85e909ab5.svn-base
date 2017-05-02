package com.afmobi.palmchat.ui.activity.store;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.util.universalimageloader.core.assist.FailReason;
import com.afmobi.palmchat.util.universalimageloader.core.listener.ImageLoadingListener;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.eventbusmodel.EmoticonDownloadEvent;
import com.afmobi.palmchat.gif.GifImageView;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.store.adapter.StoreFaceDetailAdapter;
import com.afmobi.palmchat.ui.customview.MyGridView;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ImageSplitUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfPalmchat;
import com.core.AfStoreProdDetail;
import com.core.AfStoreProdList.AfProdProfile;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 表情详情界面
 *
 */
public class StoreFaceDetailActivity extends BaseActivity implements OnClickListener, AfHttpResultListener, OnItemLongClickListener {
	/**下载按钮*/
	private View mLayoutBtn;
	/**scrollview*/
	private ScrollView mScrollView;
	/**itemid*/
	private String mItemId = null;
	/**数据传递Bunder*/
	private Bundle mBundle = null;
	/**默认表情大图*/
	private ImageView mHeadImg;
	/**返回按钮*/
	private ImageView mBackBtn;
	/**表情集名称*/
	private TextView mNameTextView;
	/**按钮价格文字*/
	private TextView mPriceTextView;
	/**表情集描述*/
	private TextView mDescriptionTexView;
	/**按钮状态文字*/
	private TextView mScoreTextView;
	/**标题栏文字*/
	private TextView mTitleTextView;
	/**本地库接口类*/
	private AfPalmchat mAfCorePalmchat;
	/**Url的前半部分*/
	private String mStaticAddr = null;
	/**图片保存路径*/
	private String mSplitImgSavePath = null;
	/**完整的下载路径*/
	private String mDownloadUrlStr = null;
	/**小图片后半段url*/
	private String mSmallIconStr = "";
	/**中间件传的表情数据*/
	public AfProdProfile mAfProdProfile = null;
	/**表情数据*/
	public AfStoreProdDetail mAfStoreProdDetail = null;
	/**表情显示gridview*/
	public MyGridView mMyGridView = null;
	/**表情数量*/
	public int mExpressCount = 0;
	/**详情列表适配器*/
	public StoreFaceDetailAdapter mFaceDetailAdapter;
	/**适配器默认设置*/
	private final int ADAPTER_DEFAULT_SET = 1;
	private final int DOWNLOAD_FINISHED=999;
	/**钱数*/
	private int mAfcoin;
	/**更新标记*/
	private boolean mToUpdateFlag;
	/**长按显示大图的popupWindow*/
	private PopupWindow mPopupWindow;

	private Handler handler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			//适配器默认设置
			case ADAPTER_DEFAULT_SET: {
				if (msg.obj != null) {
					if(mFaceDetailAdapter==null) {
						ArrayList<String> tempArrayList = (ArrayList<String>) msg.obj;
						mFaceDetailAdapter = new StoreFaceDetailAdapter(context, tempArrayList);
						mMyGridView.setAdapter(mFaceDetailAdapter);
					}
				}
			}
			break;
			//下载完成
			case  DOWNLOAD_FINISHED: {
				ArrayList<String> arrayList = null;
				if (msg.obj != null) {
					@SuppressWarnings("unchecked")
					Pair<String, Bitmap> pair = (Pair<String, Bitmap>) msg.obj;
					if (pair != null) {
						Bitmap b = pair.second;
						if (b != null) {
							if (arrayList != null) {
								arrayList.clear();
							}
							arrayList = ImageSplitUtils.getInstance().getImg_SplitFile(b, mAfStoreProdDetail.detail.express_col, mExpressCount, mSplitImgSavePath, mItemId);
							if(mFaceDetailAdapter==null) {
								mFaceDetailAdapter = new StoreFaceDetailAdapter(context, arrayList);
								mMyGridView.setAdapter(mFaceDetailAdapter);
							}else {
								mFaceDetailAdapter.getStringList().clear();
								mFaceDetailAdapter.getStringList().addAll(arrayList);
								mFaceDetailAdapter.notifyDataSetChanged();
							}

						}
					}
				}
			}
			break;
			
			default:
				break;
			}
		};
	};

	@Override
	public void findViews() {
		setContentView(R.layout.activity_face_details);
		mLayoutBtn = findViewById(R.id.linear_btn);
		mScrollView = (ScrollView) findViewById(R.id.mysv);
		mHeadImg = (ImageView) findViewById(R.id.head_img);
		
		mBackBtn = (ImageView) findViewById(R.id.back_button);
		mNameTextView = (TextView) findViewById(R.id.name_txt);
		mPriceTextView = (TextView) findViewById(R.id.price_txt);
		mDescriptionTexView = (TextView) findViewById(R.id.description_txt);
		mScoreTextView = (TextView) findViewById(R.id.text_score);
		mTitleTextView = (TextView) findViewById(R.id.title_text);
		mTitleTextView.setText(getString(R.string.store_detail_title));
		mMyGridView = (MyGridView) findViewById(R.id.grid);

		mLayoutBtn.setOnClickListener(this);
		mBackBtn.setOnClickListener(this);

		mMyGridView.setOnItemLongClickListener(this);
		mMyGridView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				switch (e.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						if (mPopupWindow != null && mPopupWindow.isShowing()) {
							mScrollView.requestDisallowInterceptTouchEvent(true);
						}
					}
					break;
				
					case MotionEvent.ACTION_MOVE: {
						if (mPopupWindow != null && mPopupWindow.isShowing()) {
							mScrollView.requestDisallowInterceptTouchEvent(true);
						}
					}
					break;
				
					case MotionEvent.ACTION_UP: {
						if (mPopupWindow != null) {
							mScrollView.requestDisallowInterceptTouchEvent(false);
							mPopupWindow.dismiss();
						}
					}
					break;
				}
				return false;
			}
		});
	}

	@Override
	public void init() {
		//EventBus注册
		EventBus.getDefault().register(this);
		mBundle = getIntent().getExtras();
		mItemId = (String) mBundle.getString(IntentConstant.ITEM_ID);
		mSmallIconStr = (String) mBundle.getString(IntentConstant.SMALL_ICON);
		String name = (String) mBundle.getString(IntentConstant.NAME);
		mAfcoin = (int) mBundle.getInt(IntentConstant.AFCOIN);
		this.mNameTextView.setText(name);
		if (mAfcoin > 0) {
			this.mPriceTextView.setText(" "+mAfcoin+"");
			this.mPriceTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.storeicon_goldcoin, 0, 0, 0);
			this.mScoreTextView.setText(getResources().getString(R.string.store_purchase));
		}else {
			this.mPriceTextView.setText(R.string.free);
		}
		
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		mAfCorePalmchat.AfHttpStoreGetProdDetail(mItemId, Consts.PROD_CATEGORY_EXPRESS, this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//下载按钮
		case R.id.linear_btn: {
			do_linearBtn();
		}
		break;
		//返回
		case R.id.back_button: {
			this.finish();
		}
		break;

		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (CommonUtils.checkStoreFace(mItemId) ) {
			mLayoutBtn.setEnabled(false);
			mScoreTextView.setVisibility(View.VISIBLE);
			mPriceTextView.setVisibility(View.GONE);
			mScoreTextView.setText(R.string.downloaded);
		} else {
			mLayoutBtn.setEnabled(true);
			mScoreTextView.setVisibility(View.GONE);
			mPriceTextView.setVisibility(View.VISIBLE);
		}
		showUpdate();
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("StoreFragment: flag = " + flag + " code=" + code + " result= " + result);

		if (code == Consts.REQ_CODE_SUCCESS) {

			switch (flag) {
				case Consts.REQ_STORE_PROD_DETAIL: {
						do_REQ_STORE_PROD_DETAIL(user_data,result);
					}
					break;
				case Consts.REQ_STORE_DOWNLOAD:
					break;
				default:
					break;
			}

		} else {
			Consts.getInstance().showToast(context, code, flag, http_code);
			this.finish();
		}
	}
	
	private void do_linearBtn() {
		
		if (!NetworkUtils.isNetworkAvailable(PalmchatApp.getApplication())) {
			ToastManager.getInstance().show(this, 
					getResources().getString(R.string.network_unavailable));
			return;
		}

		if (mAfcoin > 0) {
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BUY_EM);
		} else {
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DL_FREE_EM);
		}
		
		if (mAfProdProfile != null) {
			mLayoutBtn.setEnabled(true);
			mBundle.putString(IntentConstant.DOWNLOADURL, mDownloadUrlStr);
			String str = mStaticAddr + mSmallIconStr;
			mBundle.putString(IntentConstant.SMALLURL, str);
			mBundle.putString(IntentConstant.PROFILE_NAME, mAfProdProfile.name);
			mBundle.putInt(IntentConstant.AFCOIN, mAfProdProfile.afcoin);
			mBundle.putInt(IntentConstant.VER_CODE, mAfProdProfile.ver_code);
			mBundle.putBoolean(IntentConstant.IS_FACE_CHANGE, !mToUpdateFlag);
			mBundle.putString(IntentConstant.FACEINNERNAME, mAfProdProfile.packet_name);
			CommonUtils.to(context, StoreFaceDownLoadActivity.class, mBundle);
		}else {
			mLayoutBtn.setEnabled(false);
		}
	
	}
	
 	/**
	 * 
	 * @param user_data
	 * @param result
	 */
	private void do_REQ_STORE_PROD_DETAIL(Object user_data,Object result) {
		byte category = (Byte) user_data;
		// request emoji
		if (category == Consts.PROD_CATEGORY_EXPRESS) {
			mAfStoreProdDetail = (AfStoreProdDetail) result;
			if (mAfStoreProdDetail != null) {
				mStaticAddr = ((AfStoreProdDetail) result).url;
				if (mStaticAddr != null) {
					mDownloadUrlStr = mStaticAddr + mAfStoreProdDetail.detail.big_icon;
					ImageManager.getInstance().DisplayImage(mHeadImg, mDownloadUrlStr, R.drawable.store_emoji_default_big, false, null);
					setGridView(mAfStoreProdDetail, mStaticAddr);
				}
				this.mDescriptionTexView.setText(mAfStoreProdDetail.detail.desc);
				mAfProdProfile = mAfStoreProdDetail.detail.profile;
				if (mAfProdProfile != null) {
					mAfcoin = mAfProdProfile.afcoin;
					mNameTextView.setText(mAfProdProfile.name);
					if (mAfStoreProdDetail.detail.is_payed) {
						this.mScoreTextView.setText(R.string.download);
					}else {
						if (mAfcoin > 0) {
							this.mPriceTextView.setText(" "+mAfcoin+"");
							this.mPriceTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.storeicon_goldcoin, 0, 0, 0);
							this.mScoreTextView.setText(getResources().getString(R.string.store_purchase));
						}else {
							this.mPriceTextView.setText(R.string.free);
						}
					}
					
					if (CommonUtils.checkStoreFace(mItemId)) {
						mLayoutBtn.setEnabled(false);
						mScoreTextView.setText(R.string.downloaded);
						mScoreTextView.setVisibility(View.VISIBLE);
						mPriceTextView.setVisibility(View.GONE);
					} else {
						mLayoutBtn.setEnabled(true);
						mScoreTextView.setVisibility(View.GONE);
						mPriceTextView.setVisibility(View.VISIBLE);
					}
					showUpdate();
				}
			}
			// request gift
		} else if (category == Consts.PROD_CATEGORY_GIFT) {
			AfStoreProdDetail data = (AfStoreProdDetail) result;
			if (data != null) {

			}
		}
	}
	
	/**
	 * 显示更新
	 */
	private void showUpdate() {
		mToUpdateFlag = CacheManager.getInstance().getItemid_update().containsKey(mItemId);
		if(mToUpdateFlag){
			mScoreTextView.setVisibility(View.GONE);
			mPriceTextView.setVisibility(View.VISIBLE);
			this.mPriceTextView.setText(R.string.update);
			mLayoutBtn.setEnabled(true);
		}
	}

	/**
	 * 跟进屏幕设置表情显示gridview
	 * @param data
	 * @param mAddr
	 */
	public void setGridView(AfStoreProdDetail data, String mAddr) {
		int screentype =CacheManager.getInstance().getScreenType();
		String path = mAddr;
		boolean isNull = false ;
		switch (screentype) {
		case Consts.EXPRESS_TYPE_XD:
			if (TextUtils.isEmpty(data.detail.xd_path)) {
				ToastManager.getInstance().show(context, R.string.download_fail);
				isNull = true ;
			}else {
				path += data.detail.xd_path;
			}
			break;
		case Consts.EXPRESS_TYPE_HD:
			if (TextUtils.isEmpty(data.detail.hd_path)) {
				ToastManager.getInstance().show(context, R.string.download_fail);
				isNull = true ;
			}else {
				path += data.detail.hd_path;
			}
			break;
		case Consts.EXPRESS_TYPE_ND:
			if (TextUtils.isEmpty(data.detail.md_path)) {
				ToastManager.getInstance().show(context, R.string.download_fail);
				isNull = true ;
			}else {
				path += data.detail.md_path;
			}
			break;
		case Consts.EXPRESS_TYPE_LD:
			if (TextUtils.isEmpty(data.detail.ld_path)) {
				ToastManager.getInstance().show(context, R.string.download_fail);
				isNull = true ;
			}else {
				path += data.detail.ld_path;
			}
			break;
		default:
			break;
		}
		
		mExpressCount = data.detail.express_count;
		mSplitImgSavePath = RequestConstant.PRODUCT_EMOJI_CACHE + mItemId + "/";
		File file = new File(mSplitImgSavePath);
		if (!file.exists()) {
			file.mkdir();
		}
		
//		Store ImageInfo imageInfo = new Store ImageInfo(path);
//		Bitmap b = ImageUtil.getBitmapFromFile(imageInfo.getFileName());
		Bitmap b = ImageManager.getInstance().loadLocalImageSync(path,false);//
		ArrayList<String> arrayList = null;
		if (b == null) {
			arrayList = new ArrayList<String>();
			for (int i = 0; i < data.detail.express_count; i++) {
				arrayList.add("");
			}
			if (!isNull) {
				/*StoreImage DownloadWorker worker = new StoreImage DownloadWorker(imageInfo, handler);
				Resource Downloader.singleton.download(worker);*/
				ImageManager.getInstance().loadImage(path, 0, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}
					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

					}
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						Message msg = new Message();
						msg.what =  DOWNLOAD_FINISHED;
						msg.obj = new Pair<String, Bitmap>(imageUri, loadedImage);
						handler.sendMessage(msg);
					}
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
					}
				});
			}
		} else {
			arrayList = ImageSplitUtils.getInstance().getImg_SplitFile(b, data.detail.express_col, mExpressCount, mSplitImgSavePath, mItemId);
		}
		Message msg = new Message();
		msg.what = ADAPTER_DEFAULT_SET;
		msg.obj = arrayList;
		handler.sendMessage(msg);
	}

	/**
	 * 显示popwindow
	 * @param c 上下文
	 * @param view 所按的图片
	 * @param x  
	 * @param y
	 * @param str key
	 */
	@SuppressWarnings("deprecation")
	private void showPopupWindow(final Context c, final View view, float x, float y, String str) {
		LayoutInflater inflater = LayoutInflater.from(c);
		View v = inflater.inflate(R.layout.item_face_popup, null);
		final GifImageView tv_img = (GifImageView) v.findViewById(R.id.tv_img);
		tv_img.displayGif(c, tv_img, str,new GifImageView.GifImageCallBack() {
			
			@Override
			public void gifOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
				// TODO Auto-generated method stub
				Consts.getInstance().showToast(context, code, flag, http_code);
				if(mPopupWindow != null){
					mPopupWindow.dismiss();
				}
			}
		});
		
		mPopupWindow = new PopupWindow(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.update();
		mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				PalmchatLogUtils.println("mPopupWindow gif dismiss.");
				
				if(tv_img !=null){
					tv_img.destroyDrawingCache();
				}
			}
		});
		
		if (context instanceof Activity) {
			Activity activity = (Activity) context;
			Display defaultDisplay = activity.getWindow().getWindowManager().getDefaultDisplay();
			int w = activity.getIntent().getIntExtra("display_width", defaultDisplay.getWidth());
			int h = activity.getIntent().getIntExtra("display_height", defaultDisplay.getHeight());
			if(w >= 1080){
				mPopupWindow.showAtLocation(v, Gravity.TOP, (int) (x - ((view.getMeasuredWidth() + CommonUtils.dip2px(c, 20)) * 1.5)), (int)(y - 1.5 * (view.getMeasuredHeight() + CommonUtils.dip2px(c, 20))));
			}else if(w >= 720){
				mPopupWindow.showAtLocation(v, Gravity.TOP, (int) (x - ((view.getMeasuredWidth() + CommonUtils.dip2px(c, 20)) * 1.5)), (int)(y - 1.5 *  (view.getMeasuredHeight() + CommonUtils.dip2px(c, 20))));
			}else if(w >= 320){
				mPopupWindow.showAtLocation(v, Gravity.TOP, (int) (x - ((view.getMeasuredWidth() + CommonUtils.dip2px(c, 20)) * 1.5)), (int)(y - 1.5 *  (view.getMeasuredHeight() + CommonUtils.dip2px(c, 20))));
			}else{
				// no handle
				mPopupWindow.showAtLocation(v, Gravity.TOP, (int) (x - ((view.getMeasuredWidth() + CommonUtils.dip2px(c, 20)) * 1.5)), (int)(y -  (view.getMeasuredHeight() + CommonUtils.dip2px(c, 20))));
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		view.setBackgroundColor(Color.TRANSPARENT);
		String str = "[" + mItemId + "," + arg2 + "]";
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		showPopupWindow(context, view, location[0], location[1], str);
		return true;
	}

	/**
	 * EventBus 回调
	 * @param event 表情下载相关进度消息
	 */
	public void onEventMainThread(EmoticonDownloadEvent event) {

		int progress = event.getProgress();
		String itemId = event.getItem_id();
		if (progress == 100) {
			if (itemId.equals(mItemId)) {
				mLayoutBtn.setEnabled(false);
				mScoreTextView.setText(R.string.downloaded);
				mScoreTextView.setVisibility(View.VISIBLE);
				mPriceTextView.setVisibility(View.GONE);
			}
		}
	
	}
	
	/**
	 * 下载gif
	 * @param express_count
	 */
	public void downloadGif(final int express_count){
		new Thread(new Runnable() {
			@Override
			public void run() {
				AfHttpResultListener resultListener = new AfHttpResultListener() {
					@Override
					public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
						String download_gif_savepath = String.valueOf(user_data);
						PalmchatLogUtils.e("-----downloadGif-----", download_gif_savepath);
						if(CacheManager.getInstance().getGifDownLoadMap().containsKey(download_gif_savepath)){
							CacheManager.getInstance().getGifDownLoadMap().remove(download_gif_savepath);
						}
					}
				};
				for (int i = 0; i < express_count; i++) {
					String download_gif_url = CommonUtils.getDownLoadGIF_URL(mItemId,i+"");
					String download_gif_savepath = CommonUtils.getStoreFaceDetail_DownLoadGIF_savepaht(mItemId) + i;
					if (!FileUtils.fileIsExistsAndCanUse(download_gif_savepath)) {
						if(!CacheManager.getInstance().getGifDownLoadMap().containsKey(download_gif_savepath)){
							CacheManager.getInstance().getGifDownLoadMap().put(download_gif_savepath, "");
							PalmchatApp.getApplication().mAfCorePalmchat.AfHttpStoreDownload(download_gif_url, null, CacheManager.getInstance().getScreenType(), download_gif_savepath, false, download_gif_savepath, resultListener , null);
						}
					}
				}				
			}
		}).start();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		/**add by zhh 界面关闭时销毁popupwindow 避免not attached to window manager异常*/
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
		//EventBus反注册
		EventBus.getDefault().unregister(this);
	}
}