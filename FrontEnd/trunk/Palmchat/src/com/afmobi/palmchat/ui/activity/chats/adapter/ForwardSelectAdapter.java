package com.afmobi.palmchat.ui.activity.chats.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.chats.ForwardSelectActivity;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.main.cb.RefreshFriendListListener;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.model.HomeGroupInfo;
import com.afmobi.palmchat.ui.customview.RightHomeListView;
import com.afmobi.palmchat.ui.customview.RightHomeListView.FooterAdapter;
import com.afmobi.palmchat.ui.customview.RightHomeListView.HeaderAdapter;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SearchFactory;
import com.afmobi.palmchat.util.SearchFilter;
import com.afmobi.palmchat.util.StringUtil;
//import com.afmobi.palmchat.util.image.ImageLoader;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.cache.CacheManager;

import de.greenrobot.event.EventBus;

public class ForwardSelectAdapter extends BaseExpandableListAdapter implements
		HeaderAdapter, FooterAdapter {
	
	private static final String TAG = ForwardSelectAdapter.class.getCanonicalName();
	
	private RightHomeListView listView;
	private Activity context;
	private ArrayList<Map<String, HomeGroupInfo>> groups = new ArrayList<Map<String,HomeGroupInfo>>();
	List<List<?>> childs;

	/**
	 * 可添加的最大群组数
	 */
	private static final int GROUP_MAX = 6;
	
	private boolean isShareImage;
	private ArrayList<String> shareImageUri;
	
	private String forward_imagePath = "";

	private SearchFilter searchFilter = SearchFactory
			.getSearchFilter(SearchFactory.DEFAULT_CODE);

//	DialogUtils mDialog;
	
	/**
	 * 头像加载：是否只从缓存中加载头像
	 * 何桂明 2013-10-22
	 */
//	private ListViewAddOn listViewAddOn = new ListViewAddOn();
	
	private RefreshFriendListListener mRefreshFriendListener;

	public ForwardSelectAdapter(Activity context, RightHomeListView listView,
			List<? extends Map<String, ?>> groupData, List<List<?>> childData,
					boolean isShareImage, ArrayList<String> shareImageUri,String imagePath) {

		// TODO Auto-generated constructor stub
		this.context = context;
		this.listView = listView;
		this.isShareImage = isShareImage;
		this.shareImageUri = shareImageUri;
		this.forward_imagePath = imagePath;

		groups.addAll((List<Map<String, HomeGroupInfo>>) groupData);
		this.childs = childData;
		
//		this.listViewAddOn = listViewAddOn;

//		mDialog = new DialogUtils();
	}
	
	public void setOnFreshFriendListListener(RefreshFriendListListener refreshFriendListener) {
		 mRefreshFriendListener = refreshFriendListener; 
	}

	/**
	 * get friends list info
	 * 
	 * @return
	 */
	public List<?> getFriendsList() {
		return childs.get(1);
	}

	/**
	 * set friends list info
	 * 
	 * @param data
	 */
	public void setFriendsList(List<?> data) {

		childs.set(1, data);
	}

	/**
	 * @param data
	 */
	public void setGroupList(List<?> data) {

		childs.set(0, data);

	}

	public void setGroupsCount(String count) {

		groups.get(0).get("g").msgCount = count;
	}

	public void setFriendsCount(String count) {

		groups.get(1).get("g").msgCount = count;
	}

	@Override
	public int getHeaderState(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		final int childCount = getChildrenCount(groupPosition);
		if (childPosition == childCount - 1) {
			return PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1
				&& !listView.isGroupExpanded(groupPosition)) {
			return PINNED_HEADER_GONE;
		} else {
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureHeader(View header, int groupPosition,
			int childPosition, int alpha) {
		// TODO Auto-generated method stub
		Map<String, HomeGroupInfo> groupData = (Map<String, HomeGroupInfo>) this
				.getGroup(groupPosition);

		HomeGroupInfo gInfo = groupData.get("g");

		((TextView) header.findViewById(R.id.msgcnt)).setText(gInfo.msgCount);
		((TextView) header.findViewById(R.id.g_title)).setText(gInfo.title);

		
		((TextView) header.findViewById(R.id.new_flag)).setVisibility(View.GONE);
		/*
		 * ImageView edit =(ImageView)header.findViewById(R.id.group_edit);
		 * 
		 * switch (groupPosition) { case 0:
		 * edit.setImageResource(R.drawable.child_image); break;
		 * 
		 * case 1: edit.setImageResource(R.drawable.installn); break; }
		 */

	}

	private HashMap<Integer, Integer> groupStatusMap = new HashMap<Integer, Integer>();

	/*public toMeListener toMeListener;
	
	public void setOntoMeListener(toMeListener toMeListener) {
		this.toMeListener = toMeListener;
	}*/
	
/*    *//**
     * 点击自己头像时，进入Me页面
     * @author afmobi
     *
     *//*
	public interface toMeListener {
		
		void OntoMeListener(); 
		
	}*/

	@Override
	public void setHeaderGroupClickStatus(int groupPosition, int status) {
		// TODO Auto-generated method stub
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getHeaderGroupClickStatus(int groupPosition) {
		// TODO Auto-generated method stub
		if (groupStatusMap.containsKey(groupPosition)) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 1;
		}
	}
	
	private boolean mNew = false;
	
	public void setNewFlag(boolean flag) {
		this.mNew = flag;
		
	}
	
	public boolean getNewFlag() {
		return mNew;
		
	}
	
	class GroupViewHolder {
		TextView mTvMsgCnt;
		TextView mTvTitle;
		View mNewFriend;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.home_right_group_header, null);
			viewHolder = new GroupViewHolder();
			viewHolder.mTvMsgCnt = (TextView) convertView.findViewById(R.id.msgcnt);
			viewHolder.mTvTitle = (TextView) convertView.findViewById(R.id.g_title);
			viewHolder.mNewFriend = convertView.findViewById(R.id.new_flag);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (GroupViewHolder) convertView.getTag();
		}
		viewHolder.mNewFriend.setVisibility(View.GONE);
		if (groupPosition == 1) {
			if (mNew) {
				viewHolder.mNewFriend.setVisibility(View.VISIBLE);
			} else {
				if (viewHolder.mNewFriend.getVisibility() == View.VISIBLE) {
					viewHolder.mNewFriend.setVisibility(View.GONE);
				}
			}
		}

		HomeGroupInfo gInfo = groups.get(groupPosition).get("g");

		viewHolder.mTvTitle.setText(gInfo.title);
		viewHolder.mTvMsgCnt.setText(gInfo.msgCount);

		return convertView;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		return childs.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	@Override
	public View getChildView(int arg0, final int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		ViewHolder holder;
		
		if (arg3 == null) {
			arg3 = LayoutInflater.from(context).inflate(
					R.layout.home_group_item2, null);
			holder = new ViewHolder();
			holder.viewParent = arg3.findViewById(R.id.parent);
			holder.icon = (ImageView) arg3.findViewById(R.id.icon);
			holder.group_heads = (RelativeLayout) arg3.findViewById(R.id.group_heads);
			
			holder.group_head_1 = (ImageView) arg3.findViewById(R.id.group_head_1);
			holder.group_head_2 = (ImageView) arg3.findViewById(R.id.group_head_2);
			holder.group_head_3 = (ImageView) arg3.findViewById(R.id.group_head_3);
			holder.gender = (ImageView) arg3.findViewById(R.id.gender);
			holder.title = (TextView) arg3.findViewById(R.id.title);
			holder.content = (TextView) arg3.findViewById(R.id.content);
			holder.group_num = (TextView) arg3.findViewById(R.id.group_num);
			holder.textSort = (TextView) arg3.findViewById(R.id.friend_sort);
			holder.newFlag = (TextView)arg3.findViewById(R.id.tv_new);
			holder.img_group_of_lord = (ImageView) arg3.findViewById(R.id.img_group_of_lord);
			arg3.setTag(holder);
		} else {
			holder = (ViewHolder) arg3.getTag();
		}
		
		
		// Group
		if (arg0 == 0) {
			final AfGrpProfileInfo infos = (AfGrpProfileInfo) childs.get(arg0)
					.get(arg1);
			holder.gender.setVisibility(View.GONE);
			holder.textSort.setVisibility(View.GONE);
			holder.newFlag.setVisibility(View.GONE);
			holder.textSort.setVisibility(View.GONE);
			holder.group_num.setVisibility(View.GONE);
				holder.title.setText(infos.name);

				holder.content.setText(infos.sig);
				
				// 点击groupitem跳转到群聊界面
				holder.viewParent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						CacheManager.getInstance().getThreadPoolInstance().execute(
								new Thread() {
									public void run() {
										System.out.println("--- thread id friendlist OnClickListener " + Thread.currentThread().getId());
										AfPalmchat mAfCorePalmchat = ((PalmchatApp) context.getApplicationContext()).mAfCorePalmchat;
										mAfCorePalmchat.AfRecentMsgSetUnread(infos.afid, 0);
										MessagesUtils.setUnreadMsg(infos.afid, 0);
									}
								});
						
						
						AfGrpProfileInfo groupListItem = null;
							groupListItem = CacheManager.getInstance().searchGrpProfileInfo(infos.afid);
						Bundle bundle = new Bundle();
						if (TextUtils.isEmpty(forward_imagePath) || !isShareImage) {
							bundle.putBoolean(JsonConstant.KEY_FORWARD, true);
						}else {
							bundle.putBoolean(JsonConstant.KEY_FORWARD, false);
						}
						bundle.putInt(JsonConstant.KEY_STATUS, groupListItem.status);
						if (groupListItem != null && null != groupListItem.afid && !"".equals(groupListItem.afid))
								 {
							bundle.putString(
									GroupChatActivity.BundleKeys.ROOM_ID,
									groupListItem.afid);
						}
						if (groupListItem != null && null != groupListItem.name && !"".equals(groupListItem.name)) {
							bundle.putString(
									GroupChatActivity.BundleKeys.ROOM_NAME,
									groupListItem.name);
						}

					/*	HelpManager.getInstance(context).jumpToPage(
								GroupChatActivity.class, bundle, false, 0,
								false);*/
						bundle.putBoolean(JsonConstant.KEY_SHARE_IMG, isShareImage);
						bundle.putSerializable(JsonConstant.KEY_SHARE_IMG_URI, shareImageUri);
						bundle.putString(JsonConstant.KEY_BC_FORWARD_IMAGEPAHT, forward_imagePath);
						
						Intent intent = new Intent(context, GroupChatActivity.class);
						intent.putExtras(bundle);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivityForResult(intent, ForwardSelectActivity.SHARE_IMAGE);
						PalmchatLogUtils.e(TAG, "----" + isShareImage + "--shareImageUri--" + shareImageUri);
					}
				});

				holder.group_heads.setVisibility(View.VISIBLE);// 显示群头像
				if(infos != null && CacheManager.getInstance().getMyProfile().afId.equals(infos.admin)){
					holder.img_group_of_lord.setVisibility(View.VISIBLE);
					//					holder.group_heads.setBackgroundResource(R.drawable.groupbg_add);
				}else{
					holder.img_group_of_lord.setVisibility(View.GONE);
//					holder.group_heads.setBackgroundResource(R.drawable.groupbg_addother);
				}
				holder.icon.setVisibility(View.GONE);// 隐藏好友头像
				
				setGroupAvatar(infos, holder);

			// Friends
		} else if (arg0 == 1) {

			final AfFriendInfo afFriendInfo = (AfFriendInfo) childs.get(arg0)
					.get(arg1);

//			holder.gender.setVisibility(View.VISIBLE);
			holder.gender.setVisibility(View.GONE);
			
			holder.group_num.setVisibility(View.GONE);
			holder.group_heads.setVisibility(View.GONE);
			holder.icon.setVisibility(View.VISIBLE);
			holder.img_group_of_lord.setVisibility(View.GONE);
			

			char c = searchFilter.getAlpha(CommonUtils.getRealDisplayName(afFriendInfo).toUpperCase());
			if (arg1 == 0) {
				holder.textSort.setVisibility(View.VISIBLE);
				holder.textSort.setText(c + "");
			} else {
				AfFriendInfo afPre = (AfFriendInfo) childs.get(arg0).get(arg1 - 1);
			
				char lastCatalog = searchFilter.getAlpha(CommonUtils.getRealDisplayName(afPre).toUpperCase());
				if (c == lastCatalog) {
					holder.textSort.setVisibility(View.GONE);
				} else {
					holder.textSort.setVisibility(View.VISIBLE);
					holder.textSort.setText(c + "");
				}
			}

			if(afFriendInfo.is_new_contact) {
				holder.newFlag.setVisibility(View.VISIBLE);
			} else {
				holder.newFlag.setVisibility(View.GONE);
			}
			
			String displayName = CommonUtils.getRealDisplayName(afFriendInfo);
			if(TextUtils.isEmpty(displayName)) {
				if(!TextUtils.isEmpty(afFriendInfo.afId)) {
					displayName = afFriendInfo.afId.substring(1);
					
				}
			}
			holder.title.setText(displayName);
			
			String signStr = afFriendInfo.signature == null ? context.getString(R.string.default_status) : afFriendInfo.signature;
			signStr = signStr == null ? "" : signStr;
			CharSequence text = EmojiParser.getInstance(context).parse(signStr, ImageUtil.dip2px(context, 13));
			holder.content.setText(text);


			if (afFriendInfo != null) {
//				if (!CommonUtils.showHeadImage(afFriendInfo.afId, holder.icon, afFriendInfo.sex)) {
					//调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
        			ImageManager.getInstance().DisplayAvatarImage(holder.icon, afFriendInfo.getServerUrl(),afFriendInfo .getAfidFromHead()
        					,Consts.AF_HEAD_MIDDLE,afFriendInfo.sex,afFriendInfo.getSerialFromHead(),null);
//				}
			}

			holder.viewParent.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					CacheManager.getInstance().getThreadPoolInstance().execute(
					new Thread() {
						public void run() {
							AfPalmchat mAfCorePalmchat = ((PalmchatApp) context
									.getApplicationContext()).mAfCorePalmchat;
							mAfCorePalmchat.AfRecentMsgSetUnread(afFriendInfo.afId, 0);
							MessagesUtils.setUnreadMsg(afFriendInfo.afId, 0);
							
							if(afFriendInfo.is_new_contact) {
								afFriendInfo.is_new_contact = false;
								mAfCorePalmchat.AfDbProfileUpdateNewContact(afFriendInfo.afId, false);
							}
							
						}
				
					});
					
					toChatting(afFriendInfo, afFriendInfo.afId, afFriendInfo.name);
					
				}

			});
		
		}

		return arg3;

	}


	public void clearPrivateChatHistory(final AfPalmchat mAfCorePalmchat,final String afId) {
		new Thread(new Runnable() {
			public void run() {
				final AfMessageInfo[] recentDataArray = mAfCorePalmchat
						.AfDbRecentMsgGetRecord(
								AfMessageInfo.MESSAGE_TYPE_MASK_PRIV,
								afId, 0, Integer.MAX_VALUE);
				if (null != recentDataArray && recentDataArray.length > 0) {
					for (AfMessageInfo messageInfo : recentDataArray) {
						mAfCorePalmchat.AfDbMsgRmove(messageInfo);
						MessagesUtils.removeMsg(messageInfo, true, true);
					}
					mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV,afId);
//					context.sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
					EventBus.getDefault().post(new RefreshChatsListEvent());
				}
			}
			
		}).start();
		
	}


	/**
	 * 设置群组头像
	 * @param infos 
	 * @param holder
	 */
	private void setGroupAvatar(AfGrpProfileInfo infos, ViewHolder holder) {
		if (holder != null) {
			holder.group_head_1.setImageResource(R.drawable.head_male2);
			holder.group_head_2.setImageResource(R.drawable.head_male2);
			holder.group_head_3.setImageResource(R.drawable.head_male2);
//		holder.group_head_4.setImageResource(R.drawable.head_male);
			
			ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
			imageViews.add(holder.group_head_1);
			imageViews.add(holder.group_head_2);
			imageViews.add(holder.group_head_3);
//		imageViews.add(holder.group_head_4);
			
//			ImageLoader.getInstance().displayImage(imageViews, infos );
			ImageManager.getInstance().displayGroupHeadImage(imageViews, infos);
		}

	}

	private void toChatting(AfFriendInfo infos, String afid, String name) {
		
		PalmchatLogUtils.println("toChatting");
		Intent intent = new Intent(context, Chatting.class);
		intent.putExtra(JsonConstant.KEY_FROM_UUID, afid);
		intent.putExtra(JsonConstant.KEY_FROM_NAME, name);
		intent.putExtra(JsonConstant.KEY_FROM_ALIAS, infos.alias);
		intent.putExtra(JsonConstant.KEY_FRIEND, infos);
		if (TextUtils.isEmpty(forward_imagePath) || !isShareImage) {
			intent.putExtra(JsonConstant.KEY_FORWARD, true);
		}else {
			intent.putExtra(JsonConstant.KEY_FORWARD, false);
		}
		Bundle bundle = new Bundle();
		bundle.putBoolean(JsonConstant.KEY_SHARE_IMG, isShareImage);
		bundle.putSerializable(JsonConstant.KEY_SHARE_IMG_URI, shareImageUri);
		bundle.putString(JsonConstant.KEY_BC_FORWARD_IMAGEPAHT, forward_imagePath);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtras(bundle);
/*		intent.putExtra(JsonConstant.KEY_SHARE_IMG, isShareImage);
		intent.putExtra(JsonConstant.KEY_SHARE_IMG_URI, shareImageUri);
		intent.putExtra(JsonConstant.KEY_BC_FORWARD_IMAGEPAHT, forward_imagePath);*/
		context.startActivityForResult(intent, ForwardSelectActivity.SHARE_IMAGE);
		
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return childs.get(arg0).size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return groups.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getFooterState(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub

		// System.out.println("right childPosition:"+childPosition+"--groupPosition:"+groupPosition);

		if (childPosition == 0) {

			return PINNED_FOOTER_PUSHED_DOWN;
		} else if (!listView.isGroupExpanded(groupPosition)
				|| (groupPosition == 1 && childPosition != -1)
				|| getChildrenCount(1) == 0) {

			return PINNED_FOOTER_GONE;
		} else {
			return PINNED_FOOTER_VISIBLE;
		}
	}
	
	@Override
	public void setShowNewFriendFlag(boolean isShow) {
		// TODO Auto-generated method stub
		mNew = isShow;
		notifyDataSetChanged();
		
	}
	

	@Override
	public boolean getShowNewFriendFlag() {
		// TODO Auto-generated method stub
		return mNew;
	}

	@Override
	public void configureFooter(View header, int groupPosition,
			int childPosition, int alpha) {
		// TODO Auto-generated method stub
		Map<String, HomeGroupInfo> groupData = (Map<String, HomeGroupInfo>) this
				.getGroup(1);

		HomeGroupInfo gInfo = groupData.get("g");

		((TextView) header.findViewById(R.id.msgcnt)).setText(gInfo.msgCount);
		((TextView) header.findViewById(R.id.g_title)).setText(gInfo.title);
		
//		TextView mNewFriend = (TextView) header.findViewById(R.id.new_flag);
		
		if(mNew) {
			((TextView) header.findViewById(R.id.new_flag)).setVisibility(View.VISIBLE);
			
		} else {
			((TextView) header.findViewById(R.id.new_flag)).setVisibility(View.GONE);
			
		}
		
		// ImageView edit =(ImageView)header.findViewById(R.id.group_edit);
		// RelativeLayout rl1 = (RelativeLayout)header.findViewById(R.id.rl1);
		// RelativeLayout rl2 = (RelativeLayout)header.findViewById(R.id.rl2);

		// rl1.setVisibility(View.GONE);
		// rl2.setVisibility(View.VISIBLE);

	}

	@Override
	public void setFooterGroupClickStatus(int groupPosition, int status) {
		// TODO Auto-generated method stub
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getFooterGroupClickStatus(int groupPosition) {
		// TODO Auto-generated method stub
		if (groupStatusMap.containsKey(groupPosition)) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}

	/**
	 * holder
	 * 
	 * @author afmobi
	 * 
	 */
	private class ViewHolder {

		TextView group_num;
		ImageView icon;
		ImageView gender;
		TextView title;
		TextView content;
		TextView textSort;
		View viewParent;
		TextView newFlag;
		public ImageView img_group_of_lord;

		public RelativeLayout group_heads;

		/**
		 * 小头像01
		 */
		public ImageView group_head_1;

		/**
		 * 小头像02
		 */
		public ImageView group_head_2;

		/**
		 * 小头像03
		 */
		public ImageView group_head_3;

		/**
		 * 小头像04
		 */
		public ImageView group_head_4;

	}
	
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
		
		listView.invalidate();
		
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {
			
			
			
			}

		}

	};




}