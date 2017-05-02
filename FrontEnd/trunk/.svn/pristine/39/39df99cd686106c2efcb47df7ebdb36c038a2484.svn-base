package com.afmobi.palmchat.ui.activity.friends.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.util.CommonUtils;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountDetailsActivity;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.Consts;
//import com.umeng.analytics.MobclickAgent;

public class PublicAccountListAdapter extends BaseAdapter {

	private ArrayList<AfFriendInfo> mList = new ArrayList<AfFriendInfo>();
	private Context mContext;
	public PublicAccountListAdapter(Context context, List<AfFriendInfo> list) {
		mContext = context;
		mList.addAll(list);
	}
	
	public void setList(List<AfFriendInfo> list) {
		mList.clear();
		mList.addAll(list);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public AfFriendInfo getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View arg3, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		
		if (arg3 == null) {
			arg3 = LayoutInflater.from(mContext).inflate(
					R.layout.public_account_list_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) arg3.findViewById(R.id.wraphead);
			holder.title = (TextView) arg3.findViewById(R.id.title);
			holder.content = (TextView) arg3.findViewById(R.id.content);
			holder.linefault_view = arg3.findViewById(R.id.linefault_view);
			holder.linefull_view =  arg3.findViewById(R.id.linefull_view);
			arg3.setTag(holder);
		} else {
			holder = (ViewHolder) arg3.getTag();
		}
		bintSetLine(holder, position);
		final AfFriendInfo afFriendInfo = mList.get(position);
		holder.icon.setVisibility(View.VISIBLE);
		holder.title.setText(afFriendInfo.name);
		String sign = afFriendInfo.signature == null ? "" : afFriendInfo.signature;
		CharSequence text = EmojiParser.getInstance(mContext).parse(sign, ImageUtil.dip2px(mContext, 18));
		holder.content.setText(text);
		//调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
		ImageManager.getInstance().DisplayAvatarImage(holder.icon, afFriendInfo.getServerUrl(),afFriendInfo.getAfidFromHead()
				,Consts.AF_HEAD_MIDDLE,afFriendInfo.sex,afFriendInfo.getSerialFromHead(),null);
		holder.icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toDetails(afFriendInfo);
			}
		});
		return arg3;
	}

	private void toDetails(AfFriendInfo info) {
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_PBL_PF);
//		MobclickAgent.onEvent(mContext, ReadyConfigXML.ENTRY_PBL_PF);
		Intent intent = new Intent(mContext, PublicAccountDetailsActivity.class);
		intent.putExtra("Info", info);
		mContext.startActivity(intent);
	}
	private void bintSetLine(ViewHolder viewHolder, int position){
		if (position == (getCount() - 1)) {
			viewHolder.linefault_view.setVisibility(View.GONE);
			viewHolder.linefull_view.setVisibility(View.VISIBLE);
		}else {
			viewHolder.linefault_view.setVisibility(View.VISIBLE);
			viewHolder.linefull_view.setVisibility(View.GONE);
		}
	}
	private class ViewHolder {
		ImageView icon;
		TextView title;
		TextView content;
		View linefault_view;
		View linefull_view;
	}

}
