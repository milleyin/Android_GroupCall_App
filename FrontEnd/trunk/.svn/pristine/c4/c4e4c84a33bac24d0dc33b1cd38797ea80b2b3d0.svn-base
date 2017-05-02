package com.afmobi.palmchat.ui.activity.social;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfProfileInfo;
import com.core.AfResponseComm.AfPeopleInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
//import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Look Around Adapter
 * @author heguiming 2013-11-16
 *
 */
public class HomeGridAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<HomeGridAdapterData> nearPeopleList = new ArrayList<HomeGridAdapterData>();
	
	
	private List<AfPeopleInfo> mListData = new ArrayList<AfPeopleInfo>();
	
	private AfProfileInfo myInfo;
	private AfPeopleInfo myNearByInfo;
	
	private LinearLayout.LayoutParams mColLayoutParams, mColLayoutParamsRight;

	//总条数除以3的余数
	private int mInt;

	public HomeGridAdapter(Context mContext) {
		this.mContext = mContext;
		
		myInfo = CacheManager.getInstance().getMyProfile();
		myNearByInfo = new AfPeopleInfo();
		myNearByInfo.afid = myInfo.afId;
		myNearByInfo.name = myInfo.name;
		myNearByInfo.sex = myInfo.sex;
		myNearByInfo.age = myInfo.age;
		myNearByInfo.sign = myInfo.signature;
		myNearByInfo.head_img_path = myInfo.head_img_path;

		int displayWidth = ImageUtil.DISPLAYW / 3;
		
//		int widthMargin = ImageUtil.dip2px(mContext, 3);
		mColLayoutParams = new LinearLayout.LayoutParams(displayWidth, displayWidth);
		mColLayoutParams.weight = 1.0f;
//		mColLayoutParams.setMargins(widthMargin, 0, 0, 0); 
		
		
		mColLayoutParamsRight = new LinearLayout.LayoutParams(displayWidth, displayWidth);
		mColLayoutParamsRight.weight = 1.0f;
//		mColLayoutParamsRight.setMargins(widthMargin, 0, widthMargin, 0); 
		
	}
	
	public void resetMyProfile() {
		myInfo = CacheManager.getInstance().getMyProfile();
		myNearByInfo = new AfPeopleInfo();
		myNearByInfo.afid = myInfo.afId;
		myNearByInfo.name = myInfo.name;
		myNearByInfo.sex = myInfo.sex;
		myNearByInfo.age = myInfo.age;
		myNearByInfo.sign = myInfo.signature;
		myNearByInfo.head_img_path = myInfo.head_img_path;
	}
	
	public void refreshMyProfile() {
		resetMyProfile();
		if (mListData.size() > 0) {
			AfPeopleInfo data = mListData.get(0);
			if (data != null) {
//				data = myNearByInfo;
			 
				mListData.set(0, myNearByInfo);
				
				setDisplayData();
				notifyDataSetChanged();
			}
		}
		
	}
	
	private void setDisplayData() {
		nearPeopleList.clear();
		
		int size = mListData.size();
		int sizemo = size % 3;
		
		HomeGridAdapterData tmp = null;
		
		for(int i = 0; i < size; i++) {
			AfPeopleInfo afPeople = mListData.get(i);
			
			int posmo = i % 3;
			
		switch (posmo) {
		case 0:
				
			HomeGridAdapterData homeGridAdapterData = new HomeGridAdapterData();
			homeGridAdapterData.mAfPeopleInfo1 = afPeople;
			tmp = homeGridAdapterData;
			break;

		case 1:
			tmp.mAfPeopleInfo2 = afPeople;
			break;

		case 2:
	
			tmp.mAfPeopleInfo3 = afPeople;
			break;

//		case 3:
//			
//			tmp.mAfPeopleInfo4 = afPeople;
//			break;

		default:
			break;
		
		}
		
		switch (sizemo) {
		case 0:
		
			if (tmp.mAfPeopleInfo1 != null && tmp.mAfPeopleInfo2 != null &&
				tmp.mAfPeopleInfo3 != null) {
				
				nearPeopleList.add(tmp);
				
			}
			
			break;

		case 1:
			
			if (i == size - 1) {
				nearPeopleList.add(tmp);
			} else {
				if (tmp.mAfPeopleInfo1 != null && tmp.mAfPeopleInfo2 != null &&
						tmp.mAfPeopleInfo3 != null) {
						
						nearPeopleList.add(tmp);
				}
			}
			
			break;

		case 2:
			if (i == size - 2) {
				nearPeopleList.add(tmp);
			} else {
				if (tmp.mAfPeopleInfo1 != null && tmp.mAfPeopleInfo2 != null &&
						tmp.mAfPeopleInfo3 != null) {
						
						nearPeopleList.add(tmp);
				}
			}
			
			break;

//		case 3:
//			if (i == size - 3) {
//				nearPeopleList.add(tmp);
//			} else {
//				if (tmp.mAfPeopleInfo1 != null && tmp.mAfPeopleInfo2 != null &&
//						tmp.mAfPeopleInfo3 != null  && tmp.mAfPeopleInfo4 != null) {
//						
//						nearPeopleList.add(tmp);
//				}
//			}
//			
//			break;

		default:
			break;
		}
		
		}
	}

	@Override
	public int getCount() {
		return nearPeopleList.size();

	}

	@Override
	public HomeGridAdapterData getItem(int position) {
//		if (position >= getCount()) {
//			return nearPeopleList.get(0);
//		}
		if (position > 0 && position <= getCount()) {
			return nearPeopleList.get(position);
		}else {
			return nearPeopleList.get(0);
		}

	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void notifyDataSetChanged(List<HomeAdapterData> nearPeopleList, boolean isLoadMore, boolean addMe) {
		setNearPeopleList(nearPeopleList, isLoadMore, addMe);
		setDisplayData();
		notifyDataSetChanged();
	}
	
//	add list exclude me on the top
	public void setNearPeopleList(List<HomeAdapterData> list, boolean isLoadMore, boolean addMe) {
		
		if (!isLoadMore) {
			mListData.clear();
			
			if (addMe) {
			mListData.add(myNearByInfo);
			
			}
		}
		
		
		if (null != list && !list.isEmpty()) {
			
			for (int i = 0; i < list.size(); i++) {
				HomeAdapterData data = list.get(i);
				
				boolean contain = false;
				
				for (int j = 0; j < mListData.size(); j++) {
					AfPeopleInfo tmp = mListData.get(j);
					if (tmp != null && tmp.afid != null && data != null && data.mAfPeopleInfo != null) {
						if (tmp.afid.equals(data.mAfPeopleInfo.afid)) {
							contain = true;
							break;
					}
					}
				}
					
				   if (!contain) {
					   if (data.mAfPeopleInfo != null) {
						   mListData.add(data.mAfPeopleInfo);
					   }
				   }
					
			} 
			
		}

		if(mListData != null && mListData.size() > 12) {
			mInt = mListData.size() % 3;//取余
			if(mInt > 0) {
				for (int i = 0; i < mInt; i++) {//循环删除多于的数据，保证每行都显示三条数据
					mListData.remove(mListData.size() - 1);
				}
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RowViewHolder rowViewHolder;
		ViewHolder viewHolder1;
		ViewHolder viewHolder2;
		ViewHolder viewHolder3;
//		ViewHolder viewHolder4;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.homegriditem, null);
			
			rowViewHolder = new RowViewHolder();
			
			viewHolder1 = new ViewHolder();
			viewHolder2 = new ViewHolder();
			viewHolder3 = new ViewHolder();
//			viewHolder4 = new ViewHolder();
			
			View col1 = convertView.findViewById(R.id.col1);
			View col2 = convertView.findViewById(R.id.col2);
			View col3 = convertView.findViewById(R.id.col3);
//			View col4 = convertView.findViewById(R.id.col4);
			
			viewHolder1.mHeadImg = (ImageView) col1.findViewById(R.id.itemimage);
			viewHolder2.mHeadImg = (ImageView) col2.findViewById(R.id.itemimage2);
			viewHolder3.mHeadImg = (ImageView) col3.findViewById(R.id.itemimage3);
//			viewHolder4.mHeadImg = (ImageView) col4.findViewById(R.id.itemimage);
			
			viewHolder1.mAge = (TextView) col1.findViewById(R.id.grid_age);
			viewHolder2.mAge = (TextView) col2.findViewById(R.id.grid_age2);
			viewHolder3.mAge = (TextView) col3.findViewById(R.id.grid_age3);
			
			viewHolder1.mDistance = (TextView) col1.findViewById(R.id.grid_distance);
			viewHolder2.mDistance = (TextView) col2.findViewById(R.id.grid_distance2);
			viewHolder3.mDistance = (TextView) col3.findViewById(R.id.grid_distance3);
			
			viewHolder1.mOnlineDot = (ImageView) col1.findViewById(R.id.grid_online);
			viewHolder2.mOnlineDot = (ImageView) col2.findViewById(R.id.grid_online2);
			viewHolder3.mOnlineDot = (ImageView) col3.findViewById(R.id.grid_online3);
			
			viewHolder1.mCol = col1;
			viewHolder2.mCol = col2;
			viewHolder3.mCol = col3;
//			viewHolder4.mCol = col4;
			
			rowViewHolder.viewHolder1 = viewHolder1;
			rowViewHolder.viewHolder2 = viewHolder2;
			rowViewHolder.viewHolder3 = viewHolder3;
//			rowViewHolder.viewHolder4 = viewHolder4;
			
			convertView.setTag(rowViewHolder);
		} else {
			rowViewHolder = (RowViewHolder) convertView.getTag();
		}
		bindView(rowViewHolder, getItem(position));
		
		return convertView;
	}
	
	
	
	
	private void bindView(RowViewHolder rowViewHolder, final HomeGridAdapterData item) {
		
		rowViewHolder.viewHolder1.mCol.setLayoutParams(mColLayoutParams);
		rowViewHolder.viewHolder2.mCol.setLayoutParams(mColLayoutParams);
		rowViewHolder.viewHolder3.mCol.setLayoutParams(mColLayoutParamsRight);

		if (item.mAfPeopleInfo1 != null) {
			rowViewHolder.viewHolder1.mCol.setVisibility(View.VISIBLE);
			getColView(rowViewHolder.viewHolder1, item.mAfPeopleInfo1);
		} else {
			rowViewHolder.viewHolder1.mCol.setVisibility(View.INVISIBLE);
		}
		
		
		if (item.mAfPeopleInfo2 != null) {
			rowViewHolder.viewHolder2.mCol.setVisibility(View.VISIBLE);
			getColView(rowViewHolder.viewHolder2, item.mAfPeopleInfo2);
		} else {
			rowViewHolder.viewHolder2.mCol.setVisibility(View.INVISIBLE);
		}
		
		
		if (item.mAfPeopleInfo3 != null) {
			rowViewHolder.viewHolder3.mCol.setVisibility(View.VISIBLE);
			getColView(rowViewHolder.viewHolder3, item.mAfPeopleInfo3);
		} else {
			rowViewHolder.viewHolder3.mCol.setVisibility(View.INVISIBLE);
		}
		
		
		/*if (item.mAfPeopleInfo4 != null) {
			rowViewHolder.viewHolder4.mCol.setVisibility(View.VISIBLE);
			getColView(rowViewHolder.viewHolder4, item.mAfPeopleInfo4);
		} else {
			rowViewHolder.viewHolder4.mCol.setVisibility(View.INVISIBLE);
		}*/
		
	}
	
	private void getColView(ViewHolder viewHolder, final AfPeopleInfo afPeopleInfo) {
//		AvatarImageInfo imageInfo1 = new AvatarImageInfo(afPeopleInfo.getAfidFromHead(), afPeopleInfo.getSerialFromHead(), afPeopleInfo.name,
//				Consts.AF_HEAD_MIDDLE, afPeopleInfo.getServerUrl(), afPeopleInfo.sex, false , false, true);
//		ImageLoader.getInstance().displayImage(viewHolder.mHeadImg, imageInfo1);
		
		
		ImageManager.getInstance().DisplayAvatarImage_PhotoWall(viewHolder.mHeadImg, afPeopleInfo.getServerUrl(),afPeopleInfo.afid,Consts.AF_HEAD_MIDDLE
				,afPeopleInfo.sex,afPeopleInfo.getSerialFromHead(),true,true,null);
		viewHolder.mAge.setText(String.valueOf(afPeopleInfo.age));
		viewHolder.mAge.setCompoundDrawablesWithIntrinsicBounds
		(afPeopleInfo.sex == Consts.AFMOBI_SEX_MALE ? R.drawable.ic_fpgrid_male : R.drawable.ic_fpgrid_female,
		0, 0, 0);
		
		String distanceStr = TextUtils.isEmpty(afPeopleInfo.distance) ? myInfo.region : afPeopleInfo.distance;
		if (TextUtils.isEmpty(distanceStr)) {
			viewHolder.mDistance.setText(R.string.empty);
		} else {
			viewHolder.mDistance.setText(distanceStr);
		}  
		
		viewHolder.mOnlineDot.setImageResource(afPeopleInfo.online ? R.drawable.ic_fpgrid_online : R.drawable.ic_fpgrid_offline);
		
		 if (afPeopleInfo.afid.equals(myInfo.afId)) {
			 viewHolder.mOnlineDot.setImageResource(R.drawable.ic_fpgrid_online);
			 viewHolder.mDistance.setText(R.string.empty);
		 }
		viewHolder.mHeadImg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
//				    onclick
				    if (afPeopleInfo.afid.equals(myInfo.afId)) {
						
						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_MMPF);
//						MobclickAgent.onEvent(mContext, ReadyConfigXML.ENTRY_MMPF);
						
						Bundle bundle = new Bundle();
						bundle.putString(JsonConstant.KEY_AFID, afPeopleInfo.afid);
						Intent intent = new Intent(mContext, MyProfileActivity.class);
						intent.putExtras(bundle);
						mContext.startActivity(intent);
						
					} else { 
						 
						Intent intent = new Intent(mContext, ProfileActivity.class);
						AfProfileInfo profile = AfFriendInfo.PeopleInfoToProfile(afPeopleInfo);
						PalmchatLogUtils.e("==", "profile:"+ profile);
						intent.putExtra(JsonConstant.KEY_PROFILE, (Serializable) profile);
						intent.putExtra(JsonConstant.KEY_FLAG, true);
						intent.putExtra(JsonConstant.KEY_AFID, afPeopleInfo.afid);
						intent.putExtra(JsonConstant.KEY_FROM_XX_PROFILE, ProfileActivity.HOME_TO_PROFILE);
						mContext.startActivity(intent);
					}
				}
			});
	}
	
	class RowViewHolder {
		ViewHolder viewHolder1;
		ViewHolder viewHolder2;
		ViewHolder viewHolder3;
//		ViewHolder viewHolder4;
	}

	class ViewHolder {
		View mCol;
		ImageView mHeadImg;
		ImageView mOnlineDot;
		TextView mAge;
		TextView mDistance;
		

	}

}
