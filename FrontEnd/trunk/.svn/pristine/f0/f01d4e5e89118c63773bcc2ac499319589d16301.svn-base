package com.afmobi.palmchat.ui.activity.social;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.model.DialogItem;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog.ILargeImageDialog;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.social.AdapterBroadcastUitls.IAdapterBroadcastUitls;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.util.BroadcastUtil;
import com.afmobi.palmchat.util.ByteUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.DateUtil;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfLikeInfo;
import com.core.AfResponseComm.AfPeopleInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class HomeListAdapter extends BaseAdapter implements IAdapterBroadcastUitls{
	
	private Activity mContext;
	private List<HomeAdapterData> nearPeopleList = new ArrayList<HomeAdapterData>();
	private AfProfileInfo myInfo;
	private AfPeopleInfo myNearByInfo;
//	private ListViewAddOn listViewAddOn = new ListViewAddOn();
	private AdapterBroadcastUitls adapterBroadcastUitls;
	private CacheManager cacheManager ;
	private AfProfileInfo myprofileInfo ;
	private boolean mIsFarFarAway;
	private double mLat;
	private double mLng;
	
	
	private AfPalmchat mAfCorePalmchat;
	private HomeFragment homeFragment;
	private LargeImageDialog largeImageDialog;
	public HomeListAdapter(Activity mContext, List<HomeAdapterData> nearPeopleList,HomeFragment homeFragment) {
		this.mContext = mContext;
		resetMyProfile();
		cacheManager = CacheManager.getInstance();
		myprofileInfo = cacheManager.getMyProfile();
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		this.homeFragment = homeFragment;
		adapterBroadcastUitls = new AdapterBroadcastUitls(mContext,BroadcastDetailActivity.FROM_HOMEBROADCAST);
		adapterBroadcastUitls.setIAdapterBroadcastUitls(this);
	}
	
	public HomeListAdapter(Activity mContext, List<HomeAdapterData> nearPeopleList, boolean addMe, boolean farFarAway) {
		this.mContext = mContext;
		resetMyProfile();
		setNearPeopleList(nearPeopleList, false, addMe);
		cacheManager = CacheManager.getInstance();
		myprofileInfo = cacheManager.getMyProfile();
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;

		mIsFarFarAway = farFarAway;
		if (farFarAway) {
			adapterBroadcastUitls = new AdapterBroadcastUitls(mContext, BroadcastDetailActivity.FROM_FAR_FAR_AWAY);
		} else {
			adapterBroadcastUitls = new AdapterBroadcastUitls(mContext, BroadcastDetailActivity.FROM_HOMEBROADCAST);
		}
		
		adapterBroadcastUitls.setIAdapterBroadcastUitls(this);
	}  
	

	private void resetMyProfile() {
		myInfo = CacheManager.getInstance().getMyProfile();
		myNearByInfo = new AfPeopleInfo();
		myNearByInfo.afid = myInfo.afId;
		myNearByInfo.name = myInfo.name;
		myNearByInfo.sex = myInfo.sex;
		myNearByInfo.age = myInfo.age;
		myNearByInfo.sign = myInfo.signature;
		myNearByInfo.head_img_path = myInfo.head_img_path;
	}
	
//	public void refreshMyProfile() {
//		resetMyProfile();
//		if (nearPeopleList.size() > 0) {
//			HomeAdapterData data = nearPeopleList.get(0);
//			if (data != null && data.mAfPeopleInfo != null) {
//				data.mAfPeopleInfo = myNearByInfo;
//				notifyDataSetChanged();
//			}
//		}
//		
//	
//		
//	}
	

	@Override
	public int getCount() {
		return nearPeopleList.size();
	}

	@Override
	public HomeAdapterData getItem(int position) {
//		if (position >= getCount()) {
//			return nearPeopleList.get(0);
//		}
		if (position >0 && position <= getCount()) {
			return nearPeopleList.get(position);
		}else {
			return nearPeopleList.get(0);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int setNearPeopleList(List<HomeAdapterData> list, boolean isLoadMore) {
		if (!isLoadMore) {
			this.nearPeopleList.clear();
			
			HomeAdapterData data = new HomeAdapterData();
			data.mAfPeopleInfo = myNearByInfo;
			this.nearPeopleList.add(data);
		}
		
		if (null != list && !list.isEmpty()) {

			HomeAdapterData data =null;
			for (int i = 0; i < list.size(); i++) {
				  data = list.get(i);
				if (!nearPeopleList.contains(data)) {
				/*	if(data.mAfChapterInfo!=null){
						if(ByteUtils.indexOfHomeBroadCastListBymid(nearPeopleList, data.mAfChapterInfo.mid)<0){
						}
							nearPeopleList.add(data);
					}else if(data.mAfPeopleInfo!=null){
						if(ByteUtils.indexOfHomePeopleInfoListByid( nearPeopleList, data.mAfPeopleInfo.afid)<0){
						}
						} */
							
					nearPeopleList.add(data);
					
				}
			}
	
			 
			//this.nearPeopleList.addAll(list);
		}
		
		if (list != null) {
			return list.size();
		}
		return 0;
	}
	
//	add list exclude me on the top
	public int setNearPeopleList(List<HomeAdapterData> list, boolean isLoadMore, boolean addMe) {
		
		if (!isLoadMore) {
			this.nearPeopleList.clear();
			
			if (addMe) {
			HomeAdapterData data = new HomeAdapterData();
			data.mAfPeopleInfo = myNearByInfo;
			this.nearPeopleList.add(data);
			
			}
		}
		
		
		if (null != list && !list.isEmpty()) {
			
//			this.nearPeopleList.addAll(list);
			
			for (int i = 0; i < list.size(); i++) {
				HomeAdapterData data = list.get(i);
				if (!nearPeopleList.contains(data)) {
					/*if(data.mAfChapterInfo!=null){
						if(ByteUtils.indexOfHomeBroadCastListBymid(nearPeopleList, data.mAfChapterInfo.mid)<0){
							nearPeopleList.add(data);
						}  
					}else if(data.mAfPeopleInfo!=null){
						if(ByteUtils.indexOfHomePeopleInfoListByid( nearPeopleList, data.mAfPeopleInfo.afid)<0){
						}  
					}*/
					
					nearPeopleList.add(data);
				}
			} 
			
		}
		
		if (list != null) {
			return list.size();
		}
		return 0;
	}
	
	
	public void notifyDataSetChanged(List<HomeAdapterData> nearPeopleList, boolean isLoadMore) {
		setNearPeopleList(nearPeopleList, isLoadMore);
		notifyDataSetChanged();
	}
	
	
	public void notifyDataSetChanged(List<HomeAdapterData> nearPeopleList, boolean isLoadMore, boolean addMe) {
		setNearPeopleList(nearPeopleList, isLoadMore, addMe);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.look_around_item, null);
			viewHolder.mNearbyView = convertView.findViewById(R.id.nearby_layout);
			viewHolder.mBroadcastView =  convertView.findViewById(R.id.broadcast_layout);
			
			viewHolder.mHeadImg = (ImageView) convertView.findViewById(R.id.look_around_friend_head);
			viewHolder.mSexAgeTxt = (TextView) convertView.findViewById(R.id.text_age);
			viewHolder.mNameTxt = (TextView) convertView.findViewById(R.id.friend_name);
			viewHolder.mSignTxt = (TextView) convertView.findViewById(R.id.friend_sign);
			viewHolder.mRegionLayout = (LinearLayout) convertView.findViewById(R.id.range_layout);
			viewHolder.mFriendRegionTxt = (TextView) convertView.findViewById(R.id.range_txt);
			viewHolder.mTimeLayout = convertView.findViewById(R.id.time_layout);
			viewHolder.mFriendTimeTxt = (TextView) convertView.findViewById(R.id.time_txt);
			viewHolder.linefault_view = convertView.findViewById(R.id.linefault_view);
			viewHolder.linefull_view =  convertView.findViewById(R.id.linefull_view);
			viewHolder.brdDividerLine =  convertView.findViewById(R.id.brd_divider_line);
			viewHolder.brdDividerBottom = (ImageView) convertView.findViewById(R.id.brd_divider_bottom);
			adapterBroadcastUitls.initTextView(viewHolder, convertView); // BC
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
//		bintSetLine(viewHolder, position);
		
		HomeAdapterData itemHome = nearPeopleList.get(position);

		final AfPeopleInfo item = itemHome.mAfPeopleInfo;
		AfChapterInfo itemBroadcast  = itemHome.mAfChapterInfo;
		
		
//		nearby item
		if (item != null) {
			
	    viewHolder.mNearbyView.setVisibility(View.VISIBLE);
	    viewHolder.mBroadcastView.setVisibility(View.GONE);
	  
		
		String name = item.name;
		
		// name is empty,so afid
		if (StringUtil.isNullOrEmpty(name)) {
			name = item.afid.replace("a", "");
		}
		
		viewHolder.linefault_view.setVisibility(View.VISIBLE);
		
//		remove divider line if nearby item's next item is broadcast item
		if (position + 1 < nearPeopleList.size()) {
			HomeAdapterData nextItemHome =nearPeopleList.get(position + 1);
//			broadcast item
			if (nextItemHome != null && nextItemHome.mAfChapterInfo != null) {
				viewHolder.linefault_view.setVisibility(View.GONE);
			}
		}
		
		//WXl 20151013 调UIL的显示头像方法, 替换原来的AvatarImageInfo.统一图片管理
		 ImageManager.getInstance().DisplayAvatarImage(viewHolder.mHeadImg,  item.getServerUrl() 
					  ,item.afid ,Consts.AF_HEAD_MIDDLE,   item.sex,item.getSerialFromHead(),null);
		/*AvatarImageInfo imageInfo = new AvatarImageInfo(item.afid, item.getSerialFromHead(), name,
				Consts.AF_HEAD_MIDDLE, item.getServerUrl(), item.sex);
		ImageLoader.getInstance().displayImage(viewHolder.mHeadImg, imageInfo, listViewAddOn.isSlipping());*/
		
		int age = item.age;
		byte sex = item.sex;
		viewHolder.mSexAgeTxt.setText(age + "");
		viewHolder.mSexAgeTxt.setBackgroundResource(Consts.AFMOBI_SEX_MALE == sex ? R.drawable.bg_sexage_male: R.drawable.bg_sexage_female);
		viewHolder.mSexAgeTxt.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == sex ? R.drawable.icon_sexage_boy : R.drawable.icon_sexage_girl,0,0,0);
		viewHolder.mNameTxt.setText(item.name);
		String signStr = item.sign == null ? mContext.getString(R.string.default_status) : item.sign;
		signStr = signStr == null ? "" : signStr;
		CharSequence text = EmojiParser.getInstance(mContext).parse(signStr, ImageUtil.dip2px(mContext, 13));
		viewHolder.mSignTxt.setText(text);
		
//		logout_time mins
		viewHolder.mTimeLayout.setVisibility(View.VISIBLE);
		
		viewHolder.mFriendTimeTxt.setText(DateUtil.getHomeDisplayTime(mContext, item.logout_time));
		String distanceStr = TextUtils.isEmpty(item.distance) ? myInfo.city : item.distance;
		
		
		if (TextUtils.isEmpty(distanceStr)) {
			viewHolder.mRegionLayout.setVisibility(View.GONE);
		} else {
			viewHolder.mRegionLayout.setVisibility(View.VISIBLE);
			viewHolder.mFriendRegionTxt.setText(distanceStr);
		}

		  
	    if (myInfo.afId.equals(item.afid)) {
	    	viewHolder.mTimeLayout.setVisibility(View.VISIBLE);
	    	viewHolder.mRegionLayout.setVisibility(View.GONE);
			viewHolder.mFriendTimeTxt.setText(R.string.online);
	    }
	    
	    viewHolder.mNearbyView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
//			    onclick
			    if (myInfo.afId.equals(item.afid)) {
					
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_MMPF);
//					MobclickAgent.onEvent(mContext, ReadyConfigXML.ENTRY_MMPF);
					
					Bundle bundle = new Bundle();
					bundle.putString(JsonConstant.KEY_AFID, item.afid);
					Intent intent = new Intent(mContext, MyProfileActivity.class);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
					
				} else { 
					
					if (mIsFarFarAway) {
						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.FAR_T_PF);
//						MobclickAgent.onEvent(mContext, ReadyConfigXML.FAR_T_PF);
					} else {
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BCM_T_PF);
//					MobclickAgent.onEvent(mContext, ReadyConfigXML.BCM_T_PF);

					}
					 
					Intent intent = new Intent(mContext, ProfileActivity.class);
					AfProfileInfo profile = AfFriendInfo.PeopleInfoToProfile(item);
					intent.putExtra(JsonConstant.KEY_PROFILE, (Serializable) profile);
					intent.putExtra(JsonConstant.KEY_FLAG, true);
					intent.putExtra(JsonConstant.KEY_AFID, item.afid);
					mContext.startActivity(intent);
				}
			}
		});
	 
	    
	    
		
//		broadcast item 
		} else if (itemBroadcast != null) {
			viewHolder.mNearbyView.setVisibility(View.GONE);
			viewHolder.mBroadcastView.setVisibility(View.VISIBLE);
			
			
			viewHolder.brdDividerLine.setVisibility(View.GONE);
			viewHolder.brdDividerBottom.setVisibility(View.VISIBLE);
			AfChapterInfo info = getItem(position).mAfChapterInfo;
			
			if (mIsFarFarAway) {
				adapterBroadcastUitls.bindViewFarFarAway(viewHolder, info , position, mLat, mLng); 
			} else {
				adapterBroadcastUitls.bindView(viewHolder, info , position,mContext.getClass().getName() );//BC
			}
			
			
//			bindComment(viewHolder, info, position);  
//			remove divider view if broadcast item's next item is broadcast item
			if (position + 1 < nearPeopleList.size()) {
				HomeAdapterData nextItemHome =nearPeopleList.get(position + 1);
//				broadcast item
				if (nextItemHome != null && nextItemHome.mAfChapterInfo != null) {
					viewHolder.brdDividerBottom.setVisibility(View.GONE);
				}
			}
//			last item is broadcast item
			if (position == nearPeopleList.size() - 1) {
				viewHolder.brdDividerBottom.setVisibility(View.GONE);
			}
			
		 
		} else {
			viewHolder.mNearbyView.setVisibility(View.GONE);
			viewHolder.mBroadcastView.setVisibility(View.GONE);
		}
	
		
		return convertView;
	}

	



/*	private void bintSetLine(ViewHolder viewHolder, int position){
		if (position == (nearPeopleList.size() - 1)) {
			viewHolder.linefault_view.setVisibility(View.GONE);
			viewHolder.linefull_view.setVisibility(View.VISIBLE);
		}else {
			viewHolder.linefault_view.setVisibility(View.VISIBLE);
			viewHolder.linefull_view.setVisibility(View.GONE);
		}
	}*/
	
	


	public class ViewHolder extends BaroadCast_ViewHolder{
		View mNearbyView;
		View mBroadcastView;
		ImageView mHeadImg;
		TextView mSexAgeTxt;
		TextView mNameTxt;
		TextView mSignTxt;
		LinearLayout mRegionLayout;
		TextView mFriendRegionTxt;
		View mTimeLayout;
		TextView mFriendTimeTxt;
		View linefault_view;
		View linefull_view;
		
		/**
		 * broadcast
		 */
		
		View brdDividerLine;
		ImageView brdDividerBottom;
	}
 
	public void showAppDialog(String msg ,final int code, final AfChapterInfo afChapterInfo, final int position){
		if (afChapterInfo !=null) {
			AppDialog appDialog = new AppDialog(mContext);
			appDialog.createConfirmDialog(mContext, msg, new OnConfirmButtonDialogListener() {
				@Override
				public void onRightButtonClick() {
	//				if (code == Consts.REQ_BCMSG_DELETE) {
						if (afChapterInfo != null) {
	//						showProgressDialog(mContext.getString(R.string.Sending));
							if (homeFragment !=null && homeFragment.isAdded()) {
								homeFragment.showProgressDialog();
							}

							PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMsgOperate(code, myprofileInfo.afId, "", afChapterInfo.mid, null, null, afChapterInfo,  afHttpResultListener);
						}
	//				}
				}
				@Override
				public void onLeftButtonClick() {
					
				}
			});
			appDialog.show();
		}
	}
	 
AfHttpResultListener afHttpResultListener = new AfHttpResultListener() {
		
		@Override
		public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
			PalmchatLogUtils.e(AdapterBroadcastMessages.class.getCanonicalName(), "----flag:" + flag + "----code:" + code + "----result:" + result);
//			String time = cacheManager.getsendTime(System.currentTimeMillis()).toString();
			if (code == Consts.REQ_CODE_SUCCESS) {
				int status =  AfMessageInfo.MESSAGE_SENT;
				switch (flag) {
				case Consts.REQ_BCMSG_AGREE:
					if (user_data != null && user_data instanceof BaroadCast_ViewHolder) {
						BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
						AfChapterInfo afChapterInfo = (AfChapterInfo) viewHolder.lin_comment.getTag();
						int like_db_id =   (Integer) viewHolder.txt_like.getTag();
						mAfCorePalmchat.AfDBBCLikeUpdateStatusByID(status, like_db_id);
						mAfCorePalmchat.AfBcLikeFlagSave(afChapterInfo.mid);
						viewHolder.txt_like.setTag(0);
//						sendUpdateBroadcastForResult(afChapterInfo);
						if (mIsFarFarAway) {
							new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LIKE_BCM_FAR);
//							MobclickAgent.onEvent(mContext, ReadyConfigXML.LIKE_BCM_FAR);
						} else {
						
							
						}
						
					}
					break;
				case Consts.REQ_BCMSG_COMMENT:
					if (user_data != null && user_data instanceof BaroadCast_ViewHolder) {
						BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
						int c_db_id =   (Integer) viewHolder.txt_comment.getTag();
						mAfCorePalmchat.AfDBBCCommentUpdateStatusByID(status, c_db_id);
						if (result != null) {
							mAfCorePalmchat.AfDBBCCommentUpdateCidByID(String.valueOf(result), c_db_id);
						
							if (mIsFarFarAway) {
								new ReadyConfigXML().saveReadyInt(ReadyConfigXML.COM_BCM_FAR);
//								MobclickAgent.onEvent(mContext, ReadyConfigXML.COM_BCM_FAR);
							} else {
								new ReadyConfigXML().saveReadyInt(ReadyConfigXML.COM_BCM);
//								MobclickAgent.onEvent(mContext, ReadyConfigXML.COM_BCM);
							}
						
						
						}
						viewHolder.txt_comment.setTag(0);
					}
					break;
				case Consts.REQ_BCMSG_ACCUSATION:
					if (homeFragment !=null && homeFragment.isAdded()) {
						homeFragment.dismissProgressDialog();
					}
					
					if (mIsFarFarAway) {
						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.REPORT_BCM_FAR);
//						MobclickAgent.onEvent(mContext, ReadyConfigXML.REPORT_BCM_FAR);
					} else {
						
						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.REPORT_BCM);
//						MobclickAgent.onEvent(mContext, ReadyConfigXML.REPORT_BCM);
					}
					
					
					ToastManager.getInstance().show(mContext, R.string.report_success);
					break;
				case Consts.REQ_BCMSG_DELETE:
					if (homeFragment !=null && homeFragment.isAdded()) {
						homeFragment.dismissProgressDialog();
					}
					
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_BCM_SUCC);
//					MobclickAgent.onEvent(mContext, ReadyConfigXML.DEL_BCM_SUCC);
					
					ToastManager.getInstance().show(mContext, R.string.bc_del_success);
					if (user_data != null) {
//						int position = (Integer) user_data;
						AfChapterInfo afInfo = (AfChapterInfo) user_data;
//						nearPeopleList.remove(position);
						mAfCorePalmchat.AfDBBCChapterDeleteByID(afInfo._id);
						notifyDataSetChanged();
						sendUpdateDeleteBroadcastList(afInfo);
						if (largeImageDialog != null) {
							if (largeImageDialog.isShowing()) {
								largeImageDialog.dismiss();
							}
						}
					}
					break;
//				case Consts.REQ_BCCOMMENT_DELETE:
//					if (homeFragment !=null && homeFragment.isAdded()) {
//						homeFragment.dismissProgressDialog();
//					}
//					ToastManager.getInstance().show(mContext, R.string.success);
//					if (user_data != null) {
//						BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
//						AfChapterInfo afChapterInfo =(AfChapterInfo) viewHolder.lin_comment.getTag();
//						if (c_index != -1) {
//							afChapterInfo.list_comments.remove(c_index);
//							afChapterInfo.total_comment --;
//							int position = (Integer) viewHolder.bc_item.getTag();
//							viewHolder.txt_comment.setText(afChapterInfo.total_comment+"");
//							int c_db_id =   (Integer) viewHolder.txt_comment.getTag();
//							mAfCorePalmchat.AfDBBCCommentDeleteByID(c_db_id);
//							bindComment(viewHolder, afChapterInfo, position);
//						}
//						c_index =-1;
//					}
//					break;
				default:
					break;
				}
			}else {
				
				switch (flag) {
				case Consts.REQ_BCMSG_AGREE:
					if (user_data != null && user_data instanceof BaroadCast_ViewHolder) {
						BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
						AfChapterInfo afChapterInfo = (AfChapterInfo) viewHolder.lin_comment.getTag();
						if (code == Consts.REQ_CODE_UNNETWORK) {
							afChapterInfo.isLike = false;
							viewHolder.txt_like.setSelected(false);
							viewHolder.txt_like.setClickable(true);
						}else if (code == Consts.REQ_CODE_169)
						{
							mAfCorePalmchat.AfBcLikeFlagSave(afChapterInfo.mid);
						}
						afChapterInfo.total_like --;
						int like_db_id =   (Integer) viewHolder.txt_like.getTag();
						mAfCorePalmchat.AfDBBCLikeDeleteByID(like_db_id);
						viewHolder.txt_like.setText(afChapterInfo.total_like+"");
						if(!afChapterInfo.list_likes.isEmpty()){
							afChapterInfo.list_likes.remove(0);
						}
//						sendUpdateBroadcastForResult(afChapterInfo);
					}
					break;
//				case Consts.REQ_BCMSG_COMMENT:
//					if (user_data != null && user_data instanceof BaroadCast_ViewHolder) {
//						BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
//						AfChapterInfo afChapterInfo =(AfChapterInfo) viewHolder.lin_comment.getTag();
//						afChapterInfo.list_comments.remove(0);
//						afChapterInfo.total_comment --;
//						int position = (Integer) viewHolder.bc_item.getTag();
//						viewHolder.txt_comment.setText(afChapterInfo.total_comment+"");
//						bindComment(viewHolder, afChapterInfo, position);
//						int c_db_id =   (Integer) viewHolder.txt_comment.getTag();
//						mAfCorePalmchat.AfDBBCCommentDeleteByID(c_db_id);
//					}
//					break;
				case Consts.REQ_BCMSG_ACCUSATION:
				case Consts.REQ_BCMSG_DELETE:
				case Consts.REQ_BCCOMMENT_DELETE:
					if (homeFragment !=null && homeFragment.isAdded()) {
						((BaseFragment) homeFragment).dismissProgressDialog();
						
					}
					break;
				default:
					break;
				}
				Consts.getInstance().showToast(mContext, code, flag, http_code);
			}
			
		}
	};

	public void notifyDataSetChanged_removeBymid(String mid) {
		int index = ByteUtils.indexOfHomeBroadCastListBymid(nearPeopleList, mid);
		if (index > -1) {
			nearPeopleList.remove(index);    
			notifyDataSetChanged();
		}
	}

	@Override
	public void onClikeLike(BaroadCast_ViewHolder viewHolder, AfChapterInfo afChapterInfo, int position) {
		if(!viewHolder.txt_like.isSelected()){
			
		String time = System.currentTimeMillis()+"";
		int like_db_id = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCLikeInsert(Consts.DATA_BROADCAST_PAGE, afChapterInfo._id, "", time , myprofileInfo.afId,  AfMessageInfo.MESSAGE_SENTING, AfProfileInfo.profileToFriend(myprofileInfo));
		PalmchatLogUtils.e(AdapterBroadcastMessages.class.getCanonicalName()+"_like", "mid="+afChapterInfo.mid);
		AfLikeInfo afLikeInfo = BroadcastUtil.toAfLikeinfo(like_db_id, afChapterInfo._id, "", time, myprofileInfo.afId, AfMessageInfo.MESSAGE_SENTING, AfProfileInfo.profileToFriend(myprofileInfo));
		afChapterInfo.list_likes.add(afLikeInfo);
		viewHolder.txt_like.setTag(like_db_id);
		afChapterInfo.total_like ++;
		afChapterInfo.isLike = true;
		viewHolder.txt_like.setText(afChapterInfo.total_like+"");
		viewHolder.lin_comment.setTag(afChapterInfo);
		viewHolder.txt_like.setSelected(true);
		viewHolder.txt_like.setClickable(false);
//		viewHolder.lin_comment.setTag(afChapterInfo);
//		sendUpdateBroadcastForResult(afChapterInfo);
		PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_LIKE);
		PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMsgOperate(Consts.REQ_BCMSG_AGREE, myprofileInfo.afId, "", afChapterInfo.mid, null, null, viewHolder, afHttpResultListener);
		}
	}

	@Override
	public void onClikeMore(DialogItem dialogItem) {
		if (VoiceManager.getInstance().isPlaying()) {
			VoiceManager.getInstance().pause();
		}
	}

	/**
	 *  点击广播图片 查看大图    处理举报和删除图片
	 */
	@Override
	public void onBindLookePicture(BaroadCast_ViewHolder viewHolder , final AfChapterInfo afChapterInfo,final int position,final ImageView imageView,final int img_index) {
 
				largeImageDialog = new LargeImageDialog(mContext ,  afChapterInfo.list_mfile , img_index, LargeImageDialog.TYPE_BROADCASTLIST, afChapterInfo);
			/*	largeImageDialog.setOnCancelListener(new OnCancelListener() { 
					@Override
					public void onCancel(DialogInterface dialog) {
						//									notifyDataSetChanged();
						if (broadCastImageInfo != null) {
							ImageLoader.getInstance().displayImage(imageView, broadCastImageInfo);
						}
					}
				});*/
				largeImageDialog.show();
				largeImageDialog.setILargeImageDialog(new ILargeImageDialog() {
					
					@Override
					public void onItemClickeReportAbuse() {
						String msg = mContext.getString(R.string.sure_report_abuse);
						showAppDialog(msg,  Consts.REQ_BCMSG_ACCUSATION, afChapterInfo, position);
					}
					
					@Override
					public void onItemClickeDelete() {
						// TODO Auto-generated method stub
						String msg = mContext.getString(R.string.bc_del);
						showAppDialog(msg,  Consts.REQ_BCMSG_DELETE, afChapterInfo, position);
						
						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_BCM);
//						MobclickAgent.onEvent(mContext, ReadyConfigXML.DEL_BCM);
						
					}
				});
				 
	}

	@Override
	public void notifyData_VoiceManagerCompletion() {
		// TODO Auto-generated method stub
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged_updateLikeBymid(AfChapterInfo afChapterInfo) {
		int index = ByteUtils.indexOfHomeBroadCastListBymid(nearPeopleList, afChapterInfo.mid);
		if (index > -1) {
			PalmchatLogUtils.e("like__", afChapterInfo.total_like+"");
			nearPeopleList.get(index).mAfChapterInfo = afChapterInfo;
			notifyDataSetChanged();
//			broadcastMessageList.set(0, afChapterInfo);
		}
	}
	
	/*private static void sendUpdateBroadcastForResult(AfChapterInfo afChapterInfo) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Constants.UPDATE_LIKE);
		intent.putExtra(Constants.BROADCAST_MSG_OBJECT, afChapterInfo);
		PalmchatApp.getApplication().sendBroadcast(intent);
	}*/
	
	private   void sendUpdateDeleteBroadcastList(AfChapterInfo afChapterInfo) {
		// TODO Auto-generated method stub
		/*Intent intent = new Intent(Constants.UPDATE_DELECT_BROADCAST);
		intent.putExtra(Constants.BROADCAST_MSG_OBJECT, afChapterInfo);
		PalmchatApp.getApplication().sendBroadcast(intent);*/
		 afChapterInfo.eventBus_action= Constants.UPDATE_DELECT_BROADCAST;
		 EventBus.getDefault().post(afChapterInfo);
	}
	

	 
	
}
