package com.afmobi.palmchat.ui.activity.people;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.util.DialogUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.ToastManager;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfMutualFreindInfo;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

/**
 * PeopleYouMayKnow适配器
 * @author heguiming 2013-11-1
 *
 */
public class PeopleYouMayKnowTwoAdapter extends BaseAdapter implements AfHttpResultListener {

	private Context mContext;
	private List<AfFriendInfo> afFriendDataList = new ArrayList<AfFriendInfo>();
	
	/**
	 * 头像加载：是否只从缓存中加载头像
	 * 何桂明 2013-10-22
	 */
//	private ListViewAddOn listViewAddOn;
	private AfPalmchat mAfCorePalmchat;
	private DialogUtils mDialog;
	
	public PeopleYouMayKnowTwoAdapter(Context context, List<AfFriendInfo> dataList) {
		this.mContext = context;
		setFriendList(dataList);
//		this.listViewAddOn = listViewAddOn;
		mAfCorePalmchat = ((PalmchatApp) PalmchatApp.getApplication()).mAfCorePalmchat;
	}

	@Override
	public int getCount() {
		return afFriendDataList.size();
	}

	@Override
	public AfFriendInfo getItem(int position) {
		return afFriendDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.people_you_may_know_item, null);
			viewHolder = new ViewHolder();
			initView(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Object item = getItem(position);
		
		bindView(viewHolder, item);
		addListener(viewHolder, item);
		
		return convertView;
	}
	
	private void addListener(ViewHolder viewHolder, final Object item) {
		viewHolder.mHeadImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AfFriendInfo friend = (AfFriendInfo) item;
				Intent action = new Intent(mContext, ProfileActivity.class);
				action.putExtra(JsonConstant.KEY_PROFILE,
						(Serializable) AfFriendInfo.friendToProfile(friend));
				// 请求新的profile资料
				action.putExtra(JsonConstant.KEY_FLAG, true);
				action.putExtra(JsonConstant.KEY_AFID, friend.afId);
				mContext.startActivity(action);
			}
		});	
	}

	private void bindView(ViewHolder viewHolder, Object item) {
		String signStr = "";
		String mutualNoStr = "";
		String nameStr = "";
		int age = 0;
		byte sex = 0;
		String afid = "";
		String sn = "";
		String regionStr = "";
		boolean isAdd = false;
		
		final AfFriendInfo afFriend = (AfFriendInfo) item;
		signStr = afFriend.signature == null ? mContext.getString(R.string.default_status) : afFriend.signature;
		nameStr = afFriend.name;
		age = afFriend.age;
		sex = afFriend.sex;
		afid = afFriend.afId;
		sn = afFriend.getSerialFromHead();
		regionStr = afFriend.region;
		
		viewHolder.mRegionLayout.setVisibility(View.VISIBLE);
		viewHolder.mMutualNoTxt.setVisibility(View.GONE);
		viewHolder.mFriendRegionTxt.setText(regionStr == null ? "" : regionStr);
		isAdd = afFriend.isAddFriend;
		
		PalmchatLogUtils.e("TAG", "----afFriend.type:" + afFriend.type);
		
		if (afFriend.type == AfMutualFreindInfo.AFMOBI_THRID_APP_FACEBOOK) {
			viewHolder.mFbImg.setVisibility(View.VISIBLE);
		} else {
			viewHolder.mFbImg.setVisibility(View.GONE);
		}
		CharSequence text = EmojiParser.getInstance(mContext).parse(signStr, ImageUtil.dip2px(mContext, 13));
		viewHolder.mSignTxt.setText(text);
		
		viewHolder.mMutualNoTxt.setText(mutualNoStr);
		viewHolder.mNameTxt.setText(nameStr == null ? "" : nameStr);
		viewHolder.mSexAgeTxt.setText(age + "");
		viewHolder.mSexAgeTxt.setBackgroundResource(Consts.AFMOBI_SEX_MALE == sex ?  R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
		viewHolder.mSexAgeTxt.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == sex ? R.drawable.icon_sexage_boy
				: R.drawable.icon_sexage_girl,0,0,0);
		viewHolder.mAddFriendImg.setVisibility(isAdd ? View.GONE : View.GONE);
		
		final String tmpName = nameStr;
		final String tmpAfid = afid;
		viewHolder.mAddFriendImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(mContext);
				MessagesUtils.addStranger2Db(afFriend);
				mAfCorePalmchat.AfHttpSendMsg(tmpAfid,
						System.currentTimeMillis(), 
						mContext.getString(R.string.want_to_be_friend).replace("{$targetName}", CacheManager.getInstance().getMyProfile().name),
						Consts.MSG_CMMD_FRD_REQ, tmpAfid, PeopleYouMayKnowTwoAdapter.this);
			}
		});
		
		//调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
	    ImageManager.getInstance().DisplayAvatarImage(viewHolder.mHeadImg, afFriend.getServerUrl(),
						afid,Consts.AF_HEAD_MIDDLE, sex,sn,null);  
		/*String name = afFriend.alias == null ? afFriend.name : afFriend.alias;
		// name is empty,so afid
		if (StringUtil.isNullOrEmpty(name)) {
			name = afFriend.afId.replace("a", "");
		}
		AvatarImageInfo imageInfo = new AvatarImageInfo(afid, sn, name,
				Consts.AF_HEAD_MIDDLE, afFriend.getServerUrl(), sex);
		com.afmobi.palmchat.util.image.ImageLoader.getInstance().displayImage(viewHolder.mHeadImg, imageInfo, listViewAddOn.isSlipping());*/
		
	}
	
	private void showDialog(Context context) {
		if (context instanceof BaseFragmentActivity) {
			BaseFragmentActivity activity = (BaseFragmentActivity)context;
			activity.showProgressDialog(R.string.Sending);
		} else if (context instanceof BaseActivity) {
			BaseActivity activity = (BaseActivity)context;
			activity.showProgressDialog(R.string.Sending);
		} else {
			mDialog.showCustomProgressDialog(context, context.getString(R.string.Sending));
		}
	}
	
	private void dismissDialog(Context context) {
		if (context instanceof BaseFragmentActivity) {
			BaseFragmentActivity activity = (BaseFragmentActivity)context;
			activity.dismissProgressDialog();
		} else if (context instanceof BaseActivity) {
			BaseActivity activity = (BaseActivity)context;
			activity.dismissProgressDialog();
		} else {
			mDialog.dismissCustomProgressDialog();
		}
	}

	private void initView(ViewHolder viewHolder, View convertView) {
		viewHolder.mAddFriendImg = (ImageView) convertView.findViewById(R.id.friend_add_lookaround);
		viewHolder.mHeadImg = (ImageView) convertView.findViewById(R.id.friend_photo);
		viewHolder.mSexAgeTxt = (TextView) convertView.findViewById(R.id.text_age);
		viewHolder.mNameTxt = (TextView) convertView.findViewById(R.id.friend_name);
		viewHolder.mSignTxt = (TextView) convertView.findViewById(R.id.friend_sign);
		viewHolder.mMutualNoTxt = (TextView) convertView.findViewById(R.id.friend_mutual_no);
		viewHolder.mRegionLayout = (LinearLayout) convertView.findViewById(R.id.region_layout);
		viewHolder.mFriendRegionTxt = (TextView) convertView.findViewById(R.id.friend_region);
		viewHolder.mFbImg = (ImageView) convertView.findViewById(R.id.fb_img);
		viewHolder.mMutualLayout = (LinearLayout) convertView.findViewById(R.id.friend_mutual_layout);
		viewHolder.mFbmImg = (ImageView) convertView.findViewById(R.id.fb_m_img);
	}
	
	public void setFriendList(List<AfFriendInfo> dataList) {
		this.afFriendDataList.clear();
		if (null != dataList) {
			this.afFriendDataList.addAll(dataList);
		}
	}
	
	// 刷新
	public void notifyFriendSetChanged(List<AfFriendInfo> dataList) {
		setFriendList(dataList);
		notifyDataSetChanged();
	}

	class ViewHolder {
		ImageView mAddFriendImg;
		ImageView mHeadImg;
		TextView mSexAgeTxt;
		TextView mNameTxt;
		TextView mSignTxt;
		TextView mMutualNoTxt;
		LinearLayout mRegionLayout, mMutualLayout;
		TextView mFriendRegionTxt;
		ImageView mFbImg, mFbmImg;
		
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.DB_DELETE:
				ToastManager.getInstance().show(mContext,
						mContext.getString(R.string.add_friend_req));
				notifyDataSetChanged();
				dismissDialog(mContext);
				break;
			}
		}
	};

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result,Object user_data) {
		switch (flag) {
		// 添加好友成功
		case Consts.REQ_FRIEND_LIST:

			if (code == Consts.REQ_CODE_SUCCESS) {
				if (user_data != null && user_data instanceof String) {
					final String afid = (String) user_data;
					MessagesUtils.addMsg2Chats(mAfCorePalmchat, afid, MessagesUtils.ADD_CHATS_FRD_REQ_SENT);
					mHandler.sendMessage(mHandler.obtainMessage(
							Constants.DB_DELETE, afid));
				}
				
			} else {
				dismissDialog(mContext);
				ToastManager.getInstance().show(mContext,
						mContext.getString(R.string.req_failed));
			}

			break;
		case Consts.REQ_MSG_SEND:
			if (code == Consts.REQ_CODE_SUCCESS) {
				if (user_data != null && user_data instanceof String) {
					final String afid = (String) user_data;
					mAfCorePalmchat.AfHttpFriendOpr("all", afid,
							Consts.HTTP_ACTION_A, Consts.FRIENDS_MAKE,
							(byte) Consts.AFMOBI_FRIEND_TYPE_FF, null, afid,
							PeopleYouMayKnowTwoAdapter.this);
				}

			} else {
				dismissDialog(mContext);
				ToastManager.getInstance().show(mContext,
						mContext.getString(R.string.req_failed));
			}
			break;
		}
	}
	
}
