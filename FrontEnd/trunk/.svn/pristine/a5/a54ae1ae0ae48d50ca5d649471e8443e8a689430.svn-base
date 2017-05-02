package com.afmobi.palmchat.ui.activity.social;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountDetailsActivity;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfFriendInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfLikeInfo;
import com.core.AfResponseComm.AfPeoplesChaptersList;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

public class BroadcastLikeActivity extends BaseActivity implements IXListViewListener, AfHttpResultListener {
	private XListView mListView;
	AfPalmchat mAfCorePalmchat;
	AfProfileInfo myprofile;
	private LikeAdapter adapter;
	private AfChapterInfo afChapterInfo;

	private boolean isLoadMore;
	private int pageid = (int) System.currentTimeMillis() + new Random(10000).nextInt();
	private int START_INDEX = 0;
	private static final int LIMIT = 20;
	private static final int SET_AFCHAPTERINFO = 100;
	private static final int REFRESH_LIKEADAAPTER = 101;
	protected static final int SET_COMMENTADAAPTER = 0;

	private ProgressBar progressBar;

	private int res_total;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SET_AFCHAPTERINFO:
				adapter = new LikeAdapter(BroadcastLikeActivity.this, afChapterInfo.list_likes);
				mListView.setAdapter(adapter);
				break;
			case REFRESH_LIKEADAAPTER:
				ArrayList<AfLikeInfo> afLikeInfos = (ArrayList<AfLikeInfo>) msg.obj;
				if (adapter == null) {
					adapter = new LikeAdapter(BroadcastLikeActivity.this);
				}
				adapter.notifyDataSetChanged(afLikeInfos, isLoadMore);
				if (LIMIT <= res_total) {
					mListView.setPullLoadEnable(true);
				}
				else {
					mListView.setPullLoadEnable(false);
				}
				if(afLikeInfos.size() == 0){
					ToastManager.getInstance().show(BroadcastLikeActivity.this,true, R.string.no_data);
					mListView.setPullLoadEnable(false);
				}
				if (isLoadMore) {
					stopLoadMore();
				} else {
					stopRefresh();
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.broadcast_like);
		mListView = (XListView) findViewById(R.id.retry_list);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		myprofile = CacheManager.getInstance().getMyProfile();

		findViewById(R.id.back_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		((TextView) findViewById(R.id.title_text)).setText(R.string.like);
		progressBar = (ProgressBar) findViewById(R.id.img_loading);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AfLikeInfo afLikeInfo = adapter.getItem((position - 1));
				if (afLikeInfo != null) {
					AfProfileInfo profile = AfFriendInfo.friendToProfile(afLikeInfo.profile_Info);
					profile.afId = afLikeInfo.afid;
					if (DefaultValueConstant.BROADCAST_PROFILE_PA == profile.user_class) {
						Intent intent = new Intent(BroadcastLikeActivity.this, PublicAccountDetailsActivity.class);
						intent.putExtra("Info", profile);
						BroadcastLikeActivity.this.startActivity(intent);
					} else if (profile.afId.equals(myprofile.afId)) {

						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_MMPF);
						// MobclickAgent.onEvent(BroadcastLikeActivity.this,
						// ReadyConfigXML.ENTRY_MMPF);

						Bundle bundle = new Bundle();
						bundle.putString(JsonConstant.KEY_AFID, profile.afId);
						Intent intent = new Intent(BroadcastLikeActivity.this, MyProfileActivity.class);
						intent.putExtras(bundle);
						BroadcastLikeActivity.this.startActivity(intent);

					} else {

						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BCM_T_PF);
						// MobclickAgent.onEvent(BroadcastLikeActivity.this,
						// ReadyConfigXML.BCM_T_PF);

						Intent intent = new Intent(BroadcastLikeActivity.this, ProfileActivity.class);
						intent.putExtra(JsonConstant.KEY_PROFILE, (Serializable) profile);
						intent.putExtra(JsonConstant.KEY_FLAG, true);
						intent.putExtra(JsonConstant.KEY_AFID, profile.afId);
						BroadcastLikeActivity.this.startActivity(intent);
					}
				}
			}
		});
		// adapter = new LikeAdapter(this, null);
		// mListView.setAdapter(adapter);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			afChapterInfo = (AfChapterInfo) bundle.getSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO);
			handler.sendEmptyMessage(SET_AFCHAPTERINFO);
		}
		loadData();
		progressBar.setVisibility(View.VISIBLE);

	}

	private void loadData() {
		isLoadMore = false;
		START_INDEX = 0;
		pageid++;
		loadDataFromServer(pageid);
	}

	private void loadDataFromServer(int pageId) {
		int page_start = (START_INDEX * LIMIT);
		mAfCorePalmchat.AfHttpBcgetChaptersLikeByMid(pageId, page_start, LIMIT, afChapterInfo.mid, null, this);

	}

	private void loadmore() {
		isLoadMore = true;
		START_INDEX++;
		loadDataFromServer(pageid);
	}

	@Override
	public void onRefresh(View view) {
		mListView.setPullLoadEnable(true);
		loadData();
	}

	@Override
	public void onLoadMore(View view) {
		loadmore();

	}

	private void stopRefresh() {
		// TODO Auto-generated method stub
		mListView.stopRefresh(true);
		mListView.setRefreshTime(mDateFormat.format(new Date(System.currentTimeMillis())));

	}

	private void stopLoadMore() {
		mListView.stopLoadMore();
	}

	public class LikeAdapter extends BaseAdapter {
		LayoutInflater mInflater;
		Context context;
		ArrayList<AfLikeInfo> alist;

		public LikeAdapter(Context context) {
			this.context = context;
			this.mInflater = LayoutInflater.from(context);
			alist = new ArrayList<AfResponseComm.AfLikeInfo>();
		}

		public void notifyDataSetChanged(ArrayList<AfLikeInfo> afLikeInfos, boolean isLoadMore) {
			if (!isLoadMore) {
				alist.clear();
			}
			alist.addAll(afLikeInfos);
			super.notifyDataSetChanged();
		}

		public LikeAdapter(Context context, ArrayList<AfLikeInfo> alist) {
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
		public AfLikeInfo getItem(int position) {
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
			final AfLikeInfo info = alist.get(position);
			if (null == convertView) {
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.broadcast_like_item, null);
				initTextView(viewHolder, convertView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			bindView(viewHolder, info, position);
			return convertView;
		}

		public void notifyDataSetChanged(ArrayList<AfLikeInfo> afChapterInfos) {
			this.alist.clear();
			this.alist.addAll(afChapterInfos);
			super.notifyDataSetChanged();
		}
	}

	private void initTextView(ViewHolder viewHolder, View convertView) {
		viewHolder.time = (TextView) convertView.findViewById(R.id.text_date);
		viewHolder.text_content = (TextView) convertView.findViewById(R.id.text_content);
		viewHolder.img_photo = (ImageView) convertView.findViewById(R.id.img_photo);
	}

	private void bindView(ViewHolder viewHolder, final AfLikeInfo info, final int position) {
		if (info != null && viewHolder != null) {
			viewHolder.time.setText(info.time);
			// WXl 20151013 调UIL的显示头像方法, 替换原来的AvatarImageInfo.统一图片管理
			ImageManager.getInstance().DisplayAvatarImage(viewHolder.img_photo, info.profile_Info.getServerUrl(), info.afid, Consts.AF_HEAD_MIDDLE, info.profile_Info.sex, info.profile_Info.getSerialFromHead(), null);

			int string_id = R.string.notify_like_single;
			if (myprofile != null &&!TextUtils.isEmpty( afChapterInfo.afid)&& !afChapterInfo.afid.equals(myprofile.afId)) {
				string_id = R.string.like_this_bc;
			}
			String name = info.profile_Info.name;
			if (TextUtils.isEmpty(name)) { // 名字为空的时候 用afid
				if (!TextUtils.isEmpty(info.afid)) {//
					name = info.afid.replace("a", "");
				}
			}
			if (!TextUtils.isEmpty(name)) {
				String text = getString(string_id).replace("XXXX", name);
				viewHolder.text_content.setText(text);
			}
			if (DefaultValueConstant.BROADCAST_PROFILE_PA == info.profile_Info.user_class) {
				viewHolder.text_content.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_broadcast_list_comment_public_account, 0, 0, 0);
				viewHolder.text_content.setCompoundDrawablePadding(10);
			} else {
				viewHolder.text_content.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}
		}
	}

	class ViewHolder {
		TextView text_content, time;
		ImageView img_photo;
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		PalmchatLogUtils.e(BroadcastLikeActivity.class.getCanonicalName(), "----flag:" + flag + "----code:" + code + "----result:" + result);
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			case Consts.REQ_BCGET_COMMENTS_LIKE_BY_MID:
				progressBar.setVisibility(View.GONE);
				AfResponseComm afResponseComm = (AfResponseComm) result;
				AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
				res_total = afPeoplesChaptersList.res_total;
				AfChapterInfo temp_afChapterInfo = afPeoplesChaptersList.list_chapters.get(0);
				// refresh_CommentAdapter(temp_afChapterInfo.list_comments);
				Message msg = new Message();
				msg.what = REFRESH_LIKEADAAPTER;
				msg.obj = temp_afChapterInfo.list_likes;
				handler.sendMessage(msg);
				break;

			default:
				break;
			}
		} else {
			stopLoadMore();
			if (code == Consts.REQ_CODE_104) {
				PalmchatLogUtils.e(BroadcastLikeActivity.class.getCanonicalName(), "----code:" + code);
				loadData();
			} else {
				Consts.getInstance().showToast(context, code, flag, http_code);

				switch (flag) {
				case Consts.REQ_BCGET_COMMENTS_LIKE_BY_MID: // 获取点赞列表失败
					if (code == Consts.REQ_CODE_142 || code == Consts.REQ_CODE_201 || code == Consts.REQ_CODE_202) {// 文字不存在或已删除
						sendUpdate_delect_BroadcastList(afChapterInfo);
						setResult(RESULT_OK,new Intent());
						finish();
						
					}

					break;

				default:
					break;
				}

			}

		}

	}
	
	/**
	 * 发广播通知 各个页面这个广播被删除了
	 * 
	 * @param afChapterInfo
	 */
	private void sendUpdate_delect_BroadcastList(AfChapterInfo afChapterInfo) {
		afChapterInfo.eventBus_action = Constants.UPDATE_DELECT_BROADCAST;
		EventBus.getDefault().post(afChapterInfo);
	}


}
