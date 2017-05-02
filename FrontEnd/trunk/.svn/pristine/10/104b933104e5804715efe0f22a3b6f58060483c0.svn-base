package com.afmobi.palmchat.ui.activity.chattingroom.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfChatroomDetail;

public class ChattingRoomAdapter extends BaseAdapter{
	private Context mContext;
	private List<AfChatroomDetail> list = new ArrayList<AfChatroomDetail>();
	private int type;
	
	
	public static final int LAST_ENTRY = 1;
	public static final int COMMON_ENTRY = 2;
	public void setType(int type) {
		this.type = type;
	}

	public List<AfChatroomDetail> getList() {
		return list;
	}

	public void setList(List<AfChatroomDetail> list) {
		this.list = list;
	}

	private LayoutInflater inflater;  
	public ChattingRoomAdapter(Context context,List<AfChatroomDetail> list){
		this.mContext = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public AfChatroomDetail getItem(int position) {
		// TODO Auto-generated method stub
		if(LAST_ENTRY == type){
			return list.get(position);
		}
		int pos = --position;
		if(pos < 0) return null; 
		return list.get(pos);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		if(LAST_ENTRY == type){
			return position;
		}
		return --position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_chatting_room_list, null);
            holder = new ViewHolder();  
            holder.imageView = (ImageView)convertView.findViewById(R.id.icon);
            holder.roomName = (TextView)convertView.findViewById(R.id.title);
            holder.roomlistMe = (TextView)convertView.findViewById(R.id.roomlist_me);
            holder.roomPersonOnlineCount = (TextView)convertView.findViewById(R.id.textview_content);
            holder.vImageViewPoint = (ImageView)convertView.findViewById(R.id.entryflag);
            convertView.setTag(holder);  
        } else {  
            holder = (ViewHolder) convertView.getTag();  
        }
		if(position < 0) return convertView;
		
		AfChatroomDetail c = list.get(position);

		holder.roomName.setText(c.name);
		
		if(c.unreadNum > 0) {
			holder.roomlistMe.setVisibility(View.VISIBLE);
		} else {
			holder.roomlistMe.setVisibility(View.GONE);
		}
		holder.roomPersonOnlineCount.setText(c.size+"");
		
		/*if(50 < c.size){
			holder.roomPersonOnlineCount.setTextColor(Color.RED);
		}else{
			holder.roomPersonOnlineCount.setTextColor(mContext.getResources().getColor(R.color.chatting_room_less_fifty));
		}*/
        if(c.isEntry) {
        	holder.vImageViewPoint.setVisibility(View.VISIBLE);
        } else {
        	holder.vImageViewPoint.setVisibility(View.GONE);
        }
		
      //WXL 201510 19  
      	ImageManager.getInstance().DisplayImageRoom(holder.imageView, c.url,null);
//		ImageManager.getInstance().DisplayAvatarImage(holder.imageView, c.url,
//				afFriendInfo .getAfidFromHead(), Consts.AF_HEAD_MIDDLE,afFriendInfo.sex,afFriendInfo.getSerialFromHead(),null);
		
		return convertView;
	}

	private class ViewHolder {
		ImageView imageView;
		TextView roomName;  
		TextView roomlistMe;
		TextView roomPersonOnlineCount;
		ImageView vImageViewPoint;
		
	}
}
