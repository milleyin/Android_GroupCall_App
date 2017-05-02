package com.afmobi.palmchat.ui.activity.social;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML; 
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.ToastManager;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

/**
 * ShakeAdapter
 * 
 * @author Heguiming
 * 
 */
public class ShakeAdapter extends BaseAdapter implements AfHttpResultListener {
	
	private Context mContext;
//	private ListViewAddOn listViewAddOn;
	private AfPalmchat mAfCorePalmchat;
	private List<AfFriendInfo> friendInfoList = new ArrayList<AfFriendInfo>();
	
	public ShakeAdapter(Context mContext, List<AfFriendInfo> friendInfoList) {
		this.mContext = mContext;
//		this.listViewAddOn = listViewAddOn;
		mAfCorePalmchat = ((PalmchatApp) PalmchatApp.getApplication()).mAfCorePalmchat;
		setData(friendInfoList);
	}
	
	public void notifyDataSetChanged(List<AfFriendInfo> friendInfoList) {
		setData(friendInfoList);
		notifyDataSetChanged();
	}
	
	private void setData(List<AfFriendInfo> friendInfoList) {
		this.friendInfoList.clear();
		if (null != friendInfoList) {
			this.friendInfoList.addAll(friendInfoList);
		}
	}
	
	public List<AfFriendInfo> getFriendInfoList() {
		return friendInfoList;
	}

	@Override
	public int getCount() {
		return friendInfoList.size();
	}

	@Override
	public AfFriendInfo getItem(int position) {
		return friendInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.shake_item, null);
			viewHolder = new ViewHolder();
			initView(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		bintSetLine(viewHolder, position);
		bindView(viewHolder, getItem(position));
		
		return convertView;
	}
	
	private void initView(ViewHolder viewHolder, View convertView) {
		
		viewHolder.mHeadImg = (ImageView) convertView.findViewById(R.id.look_around_friend_head);
		viewHolder.mSexAgeTxt = (TextView) convertView.findViewById(R.id.text_age);
		
		viewHolder.mNameTxt = (TextView) convertView.findViewById(R.id.friend_name);
		viewHolder.mSignTxt = (TextView) convertView.findViewById(R.id.friend_sign);
		
		viewHolder.mRegionLayout = (LinearLayout) convertView.findViewById(R.id.range_layout);
		viewHolder.mFriendRegionTxt = (TextView) convertView.findViewById(R.id.range_txt);
		
		viewHolder.mTimeLayout = (LinearLayout) convertView.findViewById(R.id.time_layout);
		viewHolder.mTimeLayout.setVisibility(View.GONE);
		
		viewHolder.mAddFriendImg = (ImageView) convertView.findViewById(R.id.look_around_add_friend_img);
		

		viewHolder.linefault_view = convertView.findViewById(R.id.linefault_view);
		viewHolder.linefull_view =  convertView.findViewById(R.id.linefull_view);
	}
	
	public void bintSetLine(ViewHolder viewHolder, int position){
		if (position == (getCount() - 1)) {
			viewHolder.linefault_view.setVisibility(View.GONE);
			viewHolder.linefull_view.setVisibility(View.VISIBLE);
		}else {
			viewHolder.linefault_view.setVisibility(View.VISIBLE);
			viewHolder.linefull_view.setVisibility(View.GONE);
		}
	}
	private void bindView(ViewHolder viewHolder, final AfFriendInfo item) {
		//WXL 20151015 调UIL的显示头像方法,统一图片管理
		ImageManager.getInstance().DisplayAvatarImage(viewHolder.mHeadImg, item.getServerUrl(),
				item.afId,Consts.AF_HEAD_MIDDLE,  item.sex,item.getSerialFromHead(),null);  
		
		int age = item.age;
		byte sex = item.sex;
		viewHolder.mSexAgeTxt.setText(age + "");
		viewHolder.mSexAgeTxt.setBackgroundResource(Consts.AFMOBI_SEX_MALE == sex ? R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
		viewHolder.mSexAgeTxt.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == sex ? R.drawable.icon_sexage_boy
				: R.drawable.icon_sexage_girl,0,0,0);
		viewHolder.mNameTxt.setText(item.name);
		String signStr = item.signature == null ? mContext.getString(R.string.default_status) : item.signature;
		signStr = signStr == null ? "" : signStr;
		CharSequence text = EmojiParser.getInstance(mContext).parse(signStr, ImageUtil.dip2px(mContext, 13));
		viewHolder.mSignTxt.setText(text);
		viewHolder.mFriendRegionTxt.setText(item.region);
		if (item.isAddFriend) {
			viewHolder.mAddFriendImg.setVisibility(View.GONE);
		} else {
			viewHolder.mAddFriendImg.setVisibility(View.GONE);
		}
		
		viewHolder.mAddFriendImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(mContext);
				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SHAKE_ADD_SUCC);
//				MobclickAgent.onEvent(mContext, ReadyConfigXML.SHAKE_ADD_SUCC);
				MessagesUtils.addStranger2Db(item);
				mAfCorePalmchat.AfHttpSendMsg(item.afId,
						System.currentTimeMillis(), mContext.getString(R.string.want_to_be_friend).replace("{$targetName}", CacheManager.getInstance().getMyProfile().name),
						Consts.MSG_CMMD_FRD_REQ, item.afId, ShakeAdapter.this);
				
			}
		});
		
	}

	private void showDialog(Context context) {
		if (context instanceof BaseFragmentActivity) {
			BaseFragmentActivity activity = (BaseFragmentActivity)context;
			activity.showProgressDialog(R.string.Sending);
		} else if (context instanceof BaseActivity) {
			BaseActivity activity = (BaseActivity)context;
			activity.showProgressDialog(R.string.Sending);
		}
	}
	
	private void dismissDialog(Context context) {
		if (context instanceof BaseFragmentActivity) {
			BaseFragmentActivity activity = (BaseFragmentActivity)context;
			activity.dismissProgressDialog();
		} else if (context instanceof BaseActivity) {
			BaseActivity activity = (BaseActivity)context;
			activity.dismissProgressDialog();
		}
	}
	
	class ViewHolder {
		ImageView mHeadImg;
		TextView mSexAgeTxt;
		TextView mNameTxt;
		TextView mSignTxt;
		
		LinearLayout mRegionLayout;
		TextView mFriendRegionTxt;
		
		LinearLayout mTimeLayout;
		TextView mFriendTimeTxt;
		
		ImageView mAddFriendImg;
		
		View linefault_view;
		View linefull_view;
		
	}

	@Override
	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		PalmchatLogUtils.e("TAG", "----AfOnResult:" + flag);
		switch (flag) {
		// 添加好友成功
		case Consts.REQ_MSG_SEND:
			if (code == Consts.REQ_CODE_SUCCESS) {
				
				final String afid = (String) user_data;
				if (!StringUtil.isNullOrEmpty(afid)) {
					MessagesUtils.addMsg2Chats(mAfCorePalmchat, afid, MessagesUtils.ADD_CHATS_FRD_REQ_SENT);
					if (null != friendInfoList) {
						int size = friendInfoList.size();
						for (int i = 0; i < size; i++) {
							AfFriendInfo info = friendInfoList.get(i);
							if (afid.equals(info.afId)) {
								info.isAddFriend = true;
								break;
							}
						}
					}
				}
				
				ToastManager.getInstance().show(mContext,
						mContext.getString(R.string.add_friend_req));
				notifyDataSetChanged();
				dismissDialog(mContext);
			} else {
				dismissDialog(mContext);
				ToastManager.getInstance().show(mContext,
						mContext.getString(R.string.req_failed));
			}
			break;
		default:
			break;
		}
	}

}
