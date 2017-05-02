package com.afmobi.palmchat.ui.activity.store;

import java.util.ArrayList;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.store.adapter.StoreMyEmojiListAdapter;
import com.afmobi.palmchat.ui.customview.dragsort.DragSortController;
import com.afmobi.palmchat.ui.customview.dragsort.DragSortListView;
import com.afmobi.palmchat.ui.customview.dragsort.DragSortListView.RemoveListener;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.FileUtils;
//import com.afmobi.palmchat.util.image.ImageOnScrollListener;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.core.AfPalmchat;
import com.core.AfPaystoreCommon;
import com.core.AfPaystoreCommon.AfStoreDlProdInfo;
import com.core.AfPaystoreCommon.AfStoreDlProdInfoList;
import com.core.cache.CacheManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 我的表情界面
 *
 */
public class StoreMyEmojiActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = StoreMyEmojiActivity.class.getCanonicalName();
	
	/**编辑按钮*/
	private ImageView mImageViewRight;
	/**表情显示listview*/
	private ListView mListView;
	/**item可以拖动调整的listview*/
	private DragSortListView mDragSortListView;
	/**表情显示listview适配器*/
	private StoreMyEmojiListAdapter mListAdapter;
	/**item可以拖动调整的listview适配器*/
	private StoreMyEmojiListAdapter mDragSortListAdapter;
	/**表情数据*/
	private ArrayList<AfStoreDlProdInfo> mListData = new ArrayList<AfStoreDlProdInfo>();
	/**是否是编辑模式标记*/
	private boolean mIsEditMode = false;
	/**本地jni接口类*/
	private AfPalmchat mAfCorePalmchat;
	/**表情集id*/
	private String myAfid;
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.store_my_emoji);
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		((TextView)findViewById(R.id.title_text)).setText(R.string.my_emoji);
		
		findViewById(R.id.back_button).setOnClickListener(this);
		
		mImageViewRight = (ImageView) findViewById(R.id.op2);

		mImageViewRight.setBackgroundResource(R.drawable.edit_profile_selector);
		mImageViewRight.setOnClickListener(this);
		
		final View headerView = View.inflate(context, R.layout.scrolltextheader, null);
		
		mListView = (ListView) findViewById(R.id.listview_my_emoji);
		mDragSortListView = (DragSortListView) findViewById(R.id.listview_drag_sort);
		
		mListView.addHeaderView(headerView);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				//由于listview添加了头部 所以实际position减1
				toActivityStoreFaceDetail(position -1);
			}
		});
		mDragSortListView.addHeaderView(headerView);
		
		getData();
		
		if(mListData.size()>0) {//有表情时显示编辑
			mImageViewRight.setVisibility(View.VISIBLE);
		}
		else {
			mImageViewRight.setVisibility(View.GONE);
		}
		mListAdapter = new StoreMyEmojiListAdapter(context, mListData, false);
		mListView.setAdapter(mListAdapter);
		
		mDragSortListAdapter = new StoreMyEmojiListAdapter(context, mListData, true);
		
		CustomBgController controller = new CustomBgController(mDragSortListView, mDragSortListAdapter);
		
		controller.setClickRemoveId(R.id.right_layout);
		controller.setRemoveEnabled(true);
		controller.setRemoveMode(DragSortController.CLICK_REMOVE);
		mDragSortListView.setFloatViewManager(controller);
		mDragSortListView.setOnTouchListener(controller);
		
		mDragSortListView.setAdapter(mDragSortListAdapter);
		
		mDragSortListView.setDropListener(onDrop);
		mDragSortListView.setRemoveListener(onRemove);
		
//		mListView.setOnScrollListener(new ImageOnScrollListener(mListView ));
//		mDragSortListView.setOnScrollListener(new ImageOnScrollListener(mDragSortListView ));
	}
	
	/**
	 * 获取数据
	 */
	private void getData() {
		// TODO Auto-generated method stub
		myAfid = CacheManager.getInstance().getMyProfile().afId;
		AfPaystoreCommon afPaystoreCommon = mAfCorePalmchat.AfDBPaystoreProdinfoList();
		if(afPaystoreCommon != null){
			
			AfStoreDlProdInfoList afStoreDlProdInfoList = (AfStoreDlProdInfoList) afPaystoreCommon.obj;
			if(afStoreDlProdInfoList != null){
				ArrayList<AfStoreDlProdInfo> list = afStoreDlProdInfoList.prod_list;
				mListData.addAll(list);
			}
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		//key back
		case R.id.back_button: {
			back();
		}
		break;
			
		 //edit btn
		case R.id.op2: {
			mIsEditMode = !mIsEditMode;
			showDragSortListViewOrNot();
			CacheManager.getInstance().setIsFaceChange(true);
		}
		break;
		default:
			break;
			
		} 
	}
	
	/**
	 *控制类 
	 *
	 */
	private class CustomBgController extends DragSortController {
		private DragSortListView mDslv;
		private StoreMyEmojiListAdapter mAdapter;
		
		public CustomBgController(DragSortListView dslv, StoreMyEmojiListAdapter adapter) {
			super(dslv);
			// TODO Auto-generated constructor stub
			mDslv = dslv;
			mAdapter = adapter;
		}
		
		@Override
		public int startDragPosition(MotionEvent ev) {
			// TODO Auto-generated method stub
		    int res = super.dragHandleHitPosition(ev);
            int width = mDslv.getWidth();
            if ((int) ev.getX() < width / 6) {
                return res;
            } else {
                return DragSortController.MISS;
            }
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public View onCreateFloatView(int position) {
		
		    View v = mAdapter.getView(position, null, mDslv);
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
				v.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparentlayer));
			}else{
				v.setBackground(getResources().getDrawable(R.drawable.transparentlayer));
			}
		    return v;
		}
 
		@Override
        public void onDestroyFloatView(View floatView) {
		        //do nothing; block super from crashing
		}
	}
	
	/**item移动监听*/
	private DragSortListView.DropListener onDrop =
		        new DragSortListView.DropListener() {
		            @Override
		            public void drop(int from, int to) {
		                if (from != to) {
		                	AfStoreDlProdInfo item = mListData.get(from);
		                    mListData.remove(item);
		                    mListData.add(to, item);
		                    mDragSortListAdapter.notifyDataSetChanged();
		                    mListAdapter.notifyDataSetChanged();
		                    mDragSortListView.moveCheckState(from, to);
		                }
		            }
		        };

    /**移除监听器*/
    private RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(final int which) {
        	AfStoreDlProdInfo item = mListData.get(which);
            mListData.remove(item);
            mAfCorePalmchat.AfDBPaystoreProdinfoRemove(item.item_id);
            
            String filePath = CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(myAfid, item.item_id);
            FileUtils.delete(filePath);
            
            CacheManager.getInstance().getGifImageUtilInstance().removeDownloadFolder(item.item_id);
            mDragSortListAdapter.notifyDataSetChanged();
            mListAdapter.notifyDataSetChanged();
            mDragSortListView.removeCheckState(which);
        }
    };
	
	/**
	 * 设置是否显示编辑界面
	 */
	private void showDragSortListViewOrNot() {
		//in edit mode
		if (mIsEditMode) {
			mListView.setVisibility(View.GONE);
			mDragSortListView.setVisibility(View.VISIBLE);
			mImageViewRight.setBackgroundResource(R.drawable.navigation_yes_btn_selector);
		} else {
			mListView.setVisibility(View.VISIBLE);
			mDragSortListView.setVisibility(View.GONE);
			mImageViewRight.setBackgroundResource(R.drawable.edit_profile_selector);
			save();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			back();
		}
		return false;
	}

	/**
	 * 返回键处理
	 */
	private void back() {
		save();
		finish();
	}

	/**
	 * 保存下载的表情信息到数据库
	 */
	private void save() {
		new Thread(new Runnable() {
			public void run() {
				int size = mListData.size();
				for (int i = size-1 ; i >= 0; i--) {
					AfStoreDlProdInfo item = mListData.get(i);
					mAfCorePalmchat.AfDBPaystoreProdinfoUpdate(item.item_id,item.ver_code,0);
					PalmchatLogUtils.println("item.name:"+item.alas_name);
				}
				CacheManager.getInstance().getGifImageUtilInstance().sortOrder();
			}
		}).start();
	}
	
	/**
	 * 跳转表情详情界面
	 * @param position
	 */
	private void toActivityStoreFaceDetail(int position) {
		AfStoreDlProdInfo afStoreDlProdInfo = mListAdapter.getItem(position);
		if (afStoreDlProdInfo != null ) {
			Bundle bundle = new Bundle();
			bundle.putString(IntentConstant.ITEM_ID, afStoreDlProdInfo.item_id);
			bundle.putString(IntentConstant.SMALL_ICON, afStoreDlProdInfo.save_path);
			bundle.putString(IntentConstant.NAME, afStoreDlProdInfo.alas_name);
			bundle.putInt(IntentConstant.AFCOIN, afStoreDlProdInfo.price);
			Intent intent = new Intent(context, StoreFaceDetailActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
	
}
