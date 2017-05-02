package com.afmobi.palmchat.ui.activity.store.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.eventbusmodel.EmoticonDownloadEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ZipFileUtils;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfPalmchat;
import com.core.AfStoreProdList.AfProdProfile;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 表情列表适配器
 * @author starw
 *
 */
public class StoreListAdapter extends BaseAdapter implements AfHttpResultListener, AfHttpProgressListener {
	/**类型1-表情*/
	public static final int SORT_EMOJI = 1;
	/**类型2-礼物*/
	public static final int SORT_GIFT = 2;
	/**当前类型*/
	private int mAdapterSort;
	/**上下文*/
	private Activity mContext;
	/**--暂时没发现用处*/
	private int type;
	/**LayotuoutInflater*/
	private LayoutInflater mInflater;
	/**本地接口类*/
	private AfPalmchat mAfCorePalmchat;
	/**下载地址*/
	private String mStaticAddr;
	/**--暂时没发现用处*/
	public static final int LAST_ENTRY = 1;
	/**--暂时没发现用处*/
	public static final int COMMON_ENTRY = 2;
	/**数据列表*/
	private ArrayList<AfProdProfile> mList = new ArrayList<AfProdProfile>();
	/**id 与 数据对应存储*/
	private HashMap<String, AfProdProfile> mHashMap = new HashMap<String, AfProdProfile>();
	/**需要适配的View*/
	private ListView mListView;
	/**用户中心监听*/
	private toAccountCenterListener mListener;
	
	/**
	 * 构造方法 数据初始化
	 * @param context
	 * @param sort
	 * @param listView
	 */
	public StoreListAdapter(Activity context, int sort, ListView listView) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		mListView = listView;
		mAdapterSort = sort;
		mAfCorePalmchat = ((PalmchatApp) PalmchatApp.getApplication()).mAfCorePalmchat;
	}
	
	/**
	 * 设置下载地址
	 * @param addr
	 */
	public void setAddr(String addr) {
		mStaticAddr = addr;
	}
	
	/**
	 * 添加数据
	 * @param list
	 */
	public void addAll(ArrayList<AfProdProfile> list) {
		if((null==list)||(list.size()<=0)||null==mList)
		{
			return;
		}
		mList.clear();
		mList.addAll(list);
		for (AfProdProfile profile : mList) {
			mHashMap.put(profile.item_id, profile);
		} 
	}
	
	/**
	 * 清空list、map数据
	 */
	public void clear() {
		
		if(null!=mList) {
			mList.clear();
			mList =null;
		}
		
		if(null!=mHashMap) {
			mHashMap.clear();
			mHashMap=null;
		}
	}
	
	/**
	 * 更新进度条
	 * @param key
	 * @param progress
	 */
	public void updateProgress(String key, int progress) {
		if (mHashMap.containsKey(key)) {
			mHashMap.get(key).progress = progress;
		}
		int index = getPositionForItemId(key);
		if (index != -1) {
			updateProgressForItem(index);
		}
	}
	
	/**
	 * 更新下载状态
	 * @param key
	 * @param isDownloaded
	 */
	public void updateDownloadState(String key, boolean isDownloaded) {
		if (mHashMap.containsKey(key)) {
			mHashMap.get(key).isDownloaded = isDownloaded;
		}
	}
	
	/**
	 * 根据key值获取当前项的顺序
	 * @param key
	 * @return
	 */
	private int getPositionForItemId(String key) {
		if (key != null) {
			int size = mList.size();
			for (int i = 0; i < size; i++) {
				AfProdProfile afProdProfile = mList.get(i);
				if (key.equals(afProdProfile.item_id)) {
					return i;
				}
			}
		}
		return -1;
	} 
	
	/**
	 * 局部刷新进度条
	 * @param index
	 */
	private void updateProgressForItem(int index) {
		
		int FirstVisiblePosition = mListView.getFirstVisiblePosition();
		
		int childIndex = index - FirstVisiblePosition;
		
		if (childIndex >= 0) {
			//exclude headerView
			View view = mListView.getChildAt(childIndex + 1);
			if (view != null) {
				AfProdProfile afProdProfile = mList.get(index);
				ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
				progressBar.setProgress(afProdProfile.progress);
				PalmchatLogUtils.println("updateProgressForItem index " + index);
			}
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null==mList?0:mList.size();
	}

	@Override
	public AfProdProfile getItem(int position) {
		// TODO Auto-generated method stub 
		return ((null==mList)||(position>=getCount()||position<0))?null:mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(LAST_ENTRY == type){
			return position;
		}
		return --position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.store_list_item, null);
			holder = new ViewHolder(); 
			    holder.newFlag = (TextView)convertView.findViewById(R.id.text_new);
			    holder.head = (ImageView)convertView.findViewById(R.id.img_photo);
	            holder.name = (TextView)convertView.findViewById(R.id.text_name);
	            holder.sign = (TextView)convertView.findViewById(R.id.text_sign);
	            holder.distance = (TextView)convertView.findViewById(R.id.text_distance);
	            holder.time = (TextView)convertView.findViewById(R.id.text_time);
	            holder.score = (TextView)convertView.findViewById(R.id.text_score);
	            holder.free = (TextView)convertView.findViewById(R.id.text_score_free);
	            holder.tick = (TextView)convertView.findViewById(R.id.text_tick);
	            
	            holder.rightBtn = convertView.findViewById(R.id.img_right);
	            holder.progress = (ProgressBar)convertView.findViewById(R.id.progress);
	            
	            holder.linefault_view = convertView.findViewById(R.id.linefault_view);
	            holder.linefull_view =  convertView.findViewById(R.id.linefull_view);
	    		
	            convertView.setTag(holder); 
		} else {
			  holder = (ViewHolder) convertView.getTag();  
		}
		
		bintSetLine(holder, position);
		final AfProdProfile profile = mList.get(position);
		
		//is downloading
		if (CacheManager.getInstance().getStoreDownloadingMap().containsKey(profile.item_id)
				|| (profile.progress > 0 && profile.progress < 100)) {
			holder.progress.setVisibility(View.VISIBLE);
			holder.rightBtn.setVisibility(View.GONE);
			holder.tick.setVisibility(View.GONE);
			holder.progress.setProgress(profile.progress);
			PalmchatLogUtils.println("--fff storeListAdapter progress " + profile.progress);
		} else {
			//download success
			if (profile.isDownloaded) {
				boolean to_update = CacheManager.getInstance().getItemid_update().containsKey(profile.item_id);
				if(to_update){
					holder.rightBtn.setVisibility(View.VISIBLE);
					holder.tick.setVisibility(View.GONE);
					holder.free.setVisibility(View.VISIBLE);
					holder.free.setText(R.string.update);
					holder.score.setVisibility(View.GONE);
				}else{
					holder.tick.setVisibility(View.VISIBLE);
					holder.rightBtn.setVisibility(View.GONE);
					holder.free.setText(R.string.free);
				}
			} else {
				//download failure, or have not download
				holder.rightBtn.setVisibility(View.VISIBLE);
				holder.tick.setVisibility(View.GONE);
				if (profile.afcoin > 0) {
					holder.free.setVisibility(View.GONE);
					holder.score.setVisibility(View.VISIBLE);
					holder.score.setText(String.valueOf(profile.afcoin));
				} else {
					holder.free.setVisibility(View.VISIBLE);
					boolean to_update = CacheManager.getInstance().getItemid_update().containsKey(profile.item_id);
					if(to_update){
						holder.free.setText(R.string.update);
					}else{
						holder.free.setText(R.string.free);
					}
					holder.score.setVisibility(View.GONE);
				}
			}
			holder.progress.setVisibility(View.GONE);
		}
		
		if (profile.is_new) {
			holder.newFlag.setVisibility(View.VISIBLE);
		} else {
			holder.newFlag.setVisibility(View.GONE);
			
		}
		
		holder.name.setText(profile.name);
		
		holder.rightBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (profile.afcoin > 0) {
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BUY_EM);
				} else {
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DL_FREE_EM);
				}
				
				holder.rightBtn.setVisibility(View.GONE);
				holder.progress.setProgress(0);
				holder.progress.setVisibility(View.VISIBLE);
				download(profile);
			}
		});
		
		if (mStaticAddr != null) {
			ImageManager.getInstance().DisplayImage(holder.head, 
					new StringBuilder(mStaticAddr).append(profile.small_icon).toString(), 
					R.drawable.store_emoji_default,false,null);
		}
		return convertView;
	}
	
	/**
	 * 下载表情
	 * @param profile
	 */
	private void download(AfProdProfile profile) {
		if(!CacheManager.getInstance().getStoreDownloadingMap().containsKey(profile.item_id)){
			PalmchatLogUtils.println("--ppp StoreListAdapter  AfHttpStoreDownload");
			String savePath = new StringBuffer(CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(CacheManager.getInstance().getMyProfile().afId, profile.item_id)).append(profile.item_id).append(Constants.STORE_DOWNLOAD_TMP).toString();
		    CacheManager.getInstance().getStoreDownloadingMap().put(profile.item_id, 0);
		    boolean to_update = CacheManager.getInstance().getItemid_update().containsKey(profile.item_id);
		    if(to_update){
		    	profile.is_face_change = false;
		    }else{
		    	profile.is_face_change = true;
		    }
			mAfCorePalmchat.AfHttpStoreDownload(null, profile.item_id,  CacheManager.getInstance().getScreenType(), savePath, false, profile, this, this);
		}
	}
	
	/**
	 * 控制linefault与linefull哪个显示
	 * @param viewHolder
	 * @param position
	 */
	public void bintSetLine(ViewHolder viewHolder, int position){
		if (position == (getCount() - 1)) {
			viewHolder.linefault_view.setVisibility(View.GONE);
			viewHolder.linefull_view.setVisibility(View.VISIBLE);
		}else {
			viewHolder.linefault_view.setVisibility(View.VISIBLE);
			viewHolder.linefull_view.setVisibility(View.GONE);
		}
	}
	
  
	@Override
	public void AfOnProgress(int httpHandle, int flag, int progress,
			Object user_data) {
		// TODO Auto-generated method stub
		AfProdProfile profile = (AfProdProfile)user_data;
		PalmchatLogUtils.println("--ppp StoreListAdapter itemId " + profile.item_id + "  progress=" + progress);
//		the same item, same progress, no need to refresh
		if (CacheManager.getInstance().getStoreDownloadingMap().get(profile.item_id) == progress) {
			return;
		}
		
		CacheManager.getInstance().getStoreDownloadingMap().put(profile.item_id, progress);
		//通过EventBus更新进度条
		EventBus.getDefault().post(new EmoticonDownloadEvent(profile.item_id, progress, false,profile.is_face_change,JsonConstant.BROADCAST_STORE_FRAGMENT_MARK,mAdapterSort));
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			//total complete
			case 0:
				Intent intent = (Intent)msg.obj;
				String itemId = intent.getStringExtra(IntentConstant.ITEMID);
				boolean is_face_change = intent.getBooleanExtra(IntentConstant.IS_FACE_CHANGE,true);
				//通过EventBus更新进度条
				EventBus.getDefault().post(new EmoticonDownloadEvent(itemId, 100, true,is_face_change,JsonConstant.BROADCAST_STORE_FRAGMENT_MARK,mAdapterSort));
				
				break;
				
			//unzip complete
			case 1:
				Params params = (Params) msg.obj;
				String myAfid = CacheManager.getInstance().getMyProfile().afId;
				if(!TextUtils.isEmpty(myAfid)){
					Intent intent2 = new Intent();
					intent2.putExtra(IntentConstant.ITEMID, params.itemId);
					intent2.putExtra(IntentConstant.IS_FACE_CHANGE, params.is_face_change);
					if(!TextUtils.isEmpty(params.gifFolderPath)){
						CacheManager.getInstance().getGifImageUtilInstance().putDownLoadFolder(mContext,
								params.gifFolderPath, mHandler, intent2);
					}
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code,
			Object result, Object user_data) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("--kkk : store download AfOnResult code = " + code + " flag=" + flag + " result = " + result);
		final AfProdProfile profile = (AfProdProfile)user_data;
		final String itemId = profile.item_id;
		CacheManager.getInstance().getStoreDownloadingMap().remove(itemId);
		
		if (code == Consts.REQ_CODE_SUCCESS) {
			
			if (flag == Consts.REQ_STORE_DOWNLOAD) {

				if (profile.afcoin > 0) {
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BUY_EM_SUCC);
//					MobclickAgent.onEvent(mContext, ReadyConfigXML.BUY_EM_SUCC);
					
				} else {
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DL_EM_SUCC);
//					MobclickAgent.onEvent(mContext, ReadyConfigXML.DL_EM_SUCC);
				}
				
				new Thread() {
					public void run() {
						
						String savePath = new StringBuffer(CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(CacheManager.getInstance().getMyProfile().afId, itemId)).append(itemId).append(Constants.STORE_DOWNLOAD_TMP).toString();
						File tmpfile = new File(savePath);
						String zipPath = "";
						String gifFolderPath = null;
						if (tmpfile.exists()) {
							//download success rename .tmp to .zip	
							zipPath = new StringBuffer(CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(CacheManager.getInstance().getMyProfile().afId, itemId)).append(itemId).append(Constants.STORE_DOWNLOAD_COMPLETE).toString();
							File zipFile = new File(zipPath);
							tmpfile.renameTo(zipFile);
							gifFolderPath = CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(CacheManager.getInstance().getMyProfile().afId, itemId);
							try {
								if (zipFile.exists()) {
									ZipFileUtils.getInstance().upZipFile(zipFile , gifFolderPath);
									FileUtils.renameTo(gifFolderPath + "/" + CacheManager.getInstance().getScreenString()+ "/");
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								zipFile.delete();
							}
						}
						mAfCorePalmchat.AfDBPaystoreProdinfoRemove(itemId);
						mAfCorePalmchat.AfDBPaystoreProdinfoInsert(itemId, new StringBuffer(mStaticAddr).append(profile.small_icon).toString(), profile.name, "", 100, profile.afcoin, System.currentTimeMillis(),profile.ver_code,profile.packet_name);
						if(itemId != null && CacheManager.getInstance().getItemid_update().containsKey(itemId)){
							AfProdProfile afProdProfile = CacheManager.getInstance().getItemid_update().get(itemId);
							int ver_code = afProdProfile.ver_code;
							mAfCorePalmchat.AfDBPaystoreProdinfoUpdate(itemId,ver_code,System.currentTimeMillis());
						}
						Params params = new Params();
						params.itemId = itemId;
						params.is_face_change = profile.is_face_change;
						params.gifFolderPath = gifFolderPath;
						mHandler.obtainMessage(1, params).sendToTarget();
					};
				}.start();
			}
			
		} else if(code == Consts.REQ_CODE_PAYSTORE_MEMONY_ERR){
			if (flag == Consts.REQ_STORE_DOWNLOAD_PRE) {
				
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BUY_EM_RCG);
//				MobclickAgent.onEvent(mContext, ReadyConfigXML.BUY_EM_RCG);
				//通过EventBus更新进度条
				EventBus.getDefault().post(new EmoticonDownloadEvent(profile.item_id, 0, false,profile.is_face_change,JsonConstant.BROADCAST_STORE_FRAGMENT_MARK,mAdapterSort));
				PalmchatLogUtils.println("afOnResult REQ_CODE_PAYSTORE_MEMONY_ERR store_back:"+CacheManager.getInstance().isStoreBack());
				if(CacheManager.getInstance().isStoreBack()){
					CacheManager.getInstance().setStoreBack(false);
				}
				
//				if (MyActivityManager.getScreenManager().getCurrentFragment() instanceof StoreFragment 
//						&& !CacheManager.getInstance().isStoreBack() 
//						&& !MyActivityManager.getScreenManager().isExistsActivity(RechargingActivity.class)) {
//						Intent intent = new Intent(mContext, RechargingActivity.class);
//						intent.putExtra(RechargingActivity.RECHARGE_FROM_TAG, RechargingActivity.TAG_FROM_STORE_LIST);
//						mContext.startActivity(intent);
//				}
			}
		} else {
			
			if (profile.afcoin > 0) {
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BUY_EM_FAIL);
//				MobclickAgent.onEvent(mContext, ReadyConfigXML.BUY_EM_FAIL);
				
			} else {
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DL_EM_FAIL);
//				MobclickAgent.onEvent(mContext, ReadyConfigXML.DL_EM_FAIL);
			}
			
			if (flag == Consts.REQ_STORE_DOWNLOAD || flag == Consts.REQ_STORE_DOWNLOAD_PRE) {
				//通过EventBus更新进度条
				EventBus.getDefault().post(new EmoticonDownloadEvent(profile.item_id, 0, false,profile.is_face_change,JsonConstant.BROADCAST_STORE_FRAGMENT_MARK,mAdapterSort)); 
			}
			
			if (null != mContext &&  !mContext.isFinishing()) {
				Consts.getInstance().showToast(mContext, code, flag, http_code);
			}
		}
	}
	
	/**
	 * 监听接口
	 * @author Transsion
	 *
	 */
	public interface toAccountCenterListener {
		void onAccountCenterListener();
	}

	/**
	 * 设置监听
	 * @param listener
	 */
	public void setToAccountCenterListener(toAccountCenterListener listener) {
		mListener = listener;
	}
	
	/**
	 * 更新进度
	 * @param holder --viewHolder
	 * @param profile --数据
	 */
	public void updateProgress(ViewHolder holder,AfProdProfile profile){
		//is downloading
		if (CacheManager.getInstance().getStoreDownloadingMap().containsKey(profile.item_id) 
				|| (profile.progress > 0 && profile.progress < 100)) {
			holder.progress.setVisibility(View.VISIBLE);
			holder.rightBtn.setVisibility(View.GONE);
			holder.tick.setVisibility(View.GONE);
			holder.progress.setProgress(profile.progress);
			PalmchatLogUtils.println("--fff storeListAdapter progress " + profile.progress);
		} else {
			//download success
			if (profile.isDownloaded) {
				holder.tick.setVisibility(View.VISIBLE);
				holder.rightBtn.setVisibility(View.GONE);
			} else {
				//download failure, or have not download
				holder.rightBtn.setVisibility(View.VISIBLE);
				holder.tick.setVisibility(View.GONE);
				if (profile.afcoin > 0) {
					holder.free.setVisibility(View.GONE);
					holder.score.setVisibility(View.VISIBLE);
					holder.score.setText(String.valueOf(profile.afcoin));
				} else {
					holder.free.setVisibility(View.VISIBLE);
					holder.score.setVisibility(View.GONE);
				}
			}
			holder.progress.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 参数类
	 * @author Transsion
	 *
	 */
	private class Params {
		/**项item的id*/
		String itemId;
		/**gif文件路径*/
		String gifFolderPath;
		/***/
		boolean is_face_change;
	}
	
	/**
	 * viewHolder
	 * @author Transsion
	 * 
	 */
	private class ViewHolder {		
		/**right view*/
//		View rightView;
		/*** new flag*/
		TextView newFlag;
		/*** small_icon*/
		ImageView head;
		/***name*/ 
		TextView name;
		/***afcoin*/
		TextView score;
		/***free view*/
		TextView free;
		/***progress*/
		ProgressBar progress;
		/***has download tick*/
		TextView tick;
		/***/
		View rightBtn;
		/***/
		TextView sign;
		/***/
		TextView distance;
		/***/
		TextView time;
		/***/		
		View linefault_view;
		/***/
		View linefull_view;		
	}  
}
