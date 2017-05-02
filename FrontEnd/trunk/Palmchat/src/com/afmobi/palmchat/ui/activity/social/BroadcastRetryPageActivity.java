package com.afmobi.palmchat.ui.activity.social;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.util.BroadcastUtil;
import com.afmobi.palmchat.util.CommonUtils;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfPeoplesChaptersList;
import com.core.Consts;
import com.core.cache.CacheManager;

public class BroadcastRetryPageActivity extends BaseActivity implements IXListViewListener{

	private XListView mListView;
	private RetryAdapter adapter;
	String test_str ="";
	AfPalmchat mAfCorePalmchat;
	AfProfileInfo profile;
	@Override
	public void findViews() {
		setContentView(R.layout.bc_retrypage);
		mListView = (XListView) findViewById(R.id.retry_list);
		((TextView)findViewById(R.id.title_text)).setText(R.string.draft_box);
		findViewById(R.id.back_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mListView.setPullRefreshEnable(false);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		profile = CacheManager.getInstance().getMyProfile();
		get_resend_DB();
		ArrayList<AfChapterInfo> alist = new ArrayList<AfChapterInfo>();
		
//		ArrayList<AfChapterInfo> alist = new ArrayList<AfChapterInfo>();
//		List<AfMFileInfo> picturePathList = new ArrayList<AfMFileInfo>();
//		List<AfCommentInfo> afInfos = new ArrayList<AfCommentInfo>();
//		for (int i = 0; i < 10; i++) {
//			picturePathList.clear();
//			test_str += "Today is a nice day! " ;
//			AfChapterInfo afChapterInfo = BroadcastUtil.toAfChapterInfoByAfMFile(null,System.currentTimeMillis(), test_str+"_"+i, picturePathList, Consts.BC_PURVIEW_ALL,  Consts.BR_TYPE_IMAGE_TEXT, "#TEST", afInfos);
//			afChapterInfo.total_comment =10;
//			afChapterInfo.profile_Info = AfProfileInfo.profileToFriend(CacheManager.getInstance().getMyProfile());
//			alist.add(afChapterInfo);
//			
//		}
		adapter = new RetryAdapter(this, alist);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, adapter.getItem(position-1));
				bundle.putInt(JsonConstant.KEY_BC_SKIP_TYPE, BroadcastDetailActivity.TYPE_RETRY_BROADCASTLIST);
				Intent intent = new Intent(context, BroadcastDetailActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
//		registerBroadcast();
		EventBus.getDefault().register(this);
	}

	@Override
	public void init() {

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (CacheManager.getInstance().getBC_sendFailed().size() > 0) {
			adapter.notifyDataSetChanged(CacheManager.getInstance().getBC_sendFailed());
		}else {
			this.finish();
		}
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		unRegisterBroadcast();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onRefresh(View view) {
		
	}

	@Override
	public void onLoadMore(View view) {
		
	}
	
	public class RetryAdapter extends BaseAdapter{
	   	LayoutInflater mInflater;
	   	Context context;
	   	ArrayList<AfChapterInfo> alist;
	   	
	   	public RetryAdapter(){
	   		
	   	}
	   	
	   	public RetryAdapter(Context context, ArrayList<AfChapterInfo> alist){
	   		this.context = context;
	   		this.mInflater = LayoutInflater.from(context);
	   		this.alist = alist;
	   	}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return alist.size();
		}

		@Override
		public AfChapterInfo getItem(int position) {
			// TODO Auto-generated method stub
			return alist.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			final AfChapterInfo info = alist.get(position);
			if (null == convertView) {
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.bc_retrypage_item, null);
				initTextView(viewHolder, convertView);
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			bindView(viewHolder, info, position);
			return convertView;
		}
		
		public void notifyDataSetChanged(ArrayList<AfChapterInfo> afChapterInfos) {
			this.alist.clear();
			this.alist.addAll(afChapterInfos);
			super.notifyDataSetChanged();
		}
	}
	
	
	  private void initTextView(ViewHolder viewHolder, View convertView) {
		  viewHolder.resend_title = (TextView) convertView.findViewById(R.id.resend_title);
		  viewHolder.resend_time = (TextView) convertView.findViewById(R.id.resend_time);
		  viewHolder.resend_content = (TextView) convertView.findViewById(R.id.resend_content);
		  viewHolder.txt_type = (TextView) convertView.findViewById(R.id.txt_type);
		  viewHolder.bnt_resend =  convertView.findViewById(R.id.bnt_resend);
		  viewHolder.bnt_del =  convertView.findViewById(R.id.bnt_del);
		  viewHolder.img_type = (ImageView) convertView.findViewById(R.id.img_type);
		  viewHolder.lin_type_panel = convertView.findViewById(R.id.lin_type_panel);
	   }
   
	  private void bindView(ViewHolder viewHolder, final AfChapterInfo info,final int position) {
		if (info != null && viewHolder != null) {
			 viewHolder.resend_content.setText(EmojiParser.getInstance(getApplicationContext()).parse(info.content));
			 viewHolder.resend_time.setText(info.time);
			if (info.getType() == Consts.BR_TYPE_IMAGE_TEXT || info.getType() == Consts.BR_TYPE_IMAGE) {
				viewHolder.lin_type_panel.setVisibility(View.VISIBLE);
				viewHolder.img_type.setBackgroundResource(R.drawable.ic_draftbox_photo);
				viewHolder.txt_type.setText(R.string.photo);
			}else if (info.getType() == Consts.BR_TYPE_VOICE_TEXT || info.getType() == Consts.BR_TYPE_VOICE) {
				viewHolder.lin_type_panel.setVisibility(View.VISIBLE);
				viewHolder.img_type.setBackgroundResource(R.drawable.ic_draftbox_voice);
				viewHolder.txt_type.setText(R.string.voice);
			}else if ( BroadcastUtil.isShareBroadcast(info) ) {
				viewHolder.lin_type_panel.setVisibility(View.VISIBLE);
				viewHolder.img_type.setBackgroundResource(R.drawable.btn_broadcast_share_n);
				viewHolder.txt_type.setText(R.string.msg_share_broadcast);
			}
			else {
				viewHolder.lin_type_panel.setVisibility(View.GONE);
			}
			viewHolder.bnt_resend.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					BroadcastUtil.getInstance().resendBroadcast(BroadcastRetryPageActivity.this, info);
					if (adapter != null&&adapter.alist!=null&&adapter.alist.size()>position) {
						adapter.alist.remove(position);
						adapter.notifyDataSetChanged();
						if (adapter.alist.size() < 1) {
							finish();
						}
					}
				}
			});
			viewHolder.bnt_del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mAfCorePalmchat.AfDBBCChapterDeleteByID(info._id);
					CacheManager.getInstance().getBC_resend_HashMap().remove(info._id);
						
					if (adapter != null) {
						adapter.alist.remove(position);
						adapter.notifyDataSetChanged();
						if (adapter.alist.size() < 1) {
							finish();
						}
					}
				}
			});
		}
	  }
   
	
	class ViewHolder{
		   TextView resend_title,resend_time,resend_content,txt_type;
		   ImageView img_type;
		   View lin_type_panel,bnt_resend,bnt_del;
	}
	
	 public void onEventMainThread(AfChapterInfo afChapterInfo) {  
		 if(afChapterInfo.eventBus_action==Constants.UPDATE_BROADCAST_MSG){
			 if (afChapterInfo.status == AfMessageInfo.MESSAGE_UNSENT) { 
					adapter.alist.add(0, afChapterInfo);
					adapter.notifyDataSetChanged();
				}
		 }
	 }
	
	/*UpdateMsgReceiver mReveicer;
	private void registerBroadcast() {
		mReveicer = new UpdateMsgReceiver();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(Constants.BROADCAST_MSG_OBJECT);
		this.registerReceiver(mReveicer, filter2);
	}
	
	private void unRegisterBroadcast() {
		this.unregisterReceiver(mReveicer);
	}*/
	
	/*class UpdateMsgReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			AfChapterInfo afChapterInfo = (AfChapterInfo)intent.getSerializableExtra(Constants.BROADCAST_MSG_OBJECT);
			if (Constants.BROADCAST_MSG_OBJECT.equals(action)) {
//				mBroadcastListData = (ArrayList<BroadcastNotificationData>) intent.getSerializableExtra("data");
				
				PalmchatLogUtils.println("BroadcastFragment onReceive BROADCAST_NOTIFICATION_ACTION,afChapterInfo.status="+afChapterInfo.status);
			
//				Handler handler = looperThread.looperHandler;
//				if (null != handler) {
//					handler.sendEmptyMessage(LooperThread.GET_BRD_NOTIFICATION_DATA);
//				}
				if (afChapterInfo.status == AfMessageInfo.MESSAGE_SENT) {
//					adapter.alist.clear();
				}else {
					adapter.alist.add(0, afChapterInfo);
					adapter.notifyDataSetChanged();
				}
			}
		}
	}*/
	/**
	 * resend db data
	 */
	public void get_resend_DB(){
		String afid = CacheManager.getInstance().getMyProfile().afId;
		AfResponseComm afResponseComm = mAfCorePalmchat.AfDBBCChapterFindByAFIDStatus(afid , AfMessageInfo.MESSAGE_UNSENT);
		if (afResponseComm != null) {
			AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
			if (afPeoplesChaptersList != null) {
				ArrayList<AfChapterInfo> list_AfChapterInfo = afPeoplesChaptersList.list_chapters;
				for (AfChapterInfo afChapterInfo : list_AfChapterInfo) {
					PalmchatLogUtils.println("getResend_DB,afChapterInfo._id ="+afChapterInfo._id);
					BroadcastUtil.saveCache(afChapterInfo);
				}
			}
		}
	}
}
