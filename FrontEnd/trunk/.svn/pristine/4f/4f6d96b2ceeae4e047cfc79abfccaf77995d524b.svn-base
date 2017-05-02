package com.afmobi.palmchat.ui.activity.friends.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.friends.GroupListActivity;
import com.afmobi.palmchat.ui.activity.groupchat.EditGroupActivity;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.main.helper.HelpManager;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
//import com.afmobi.palmchat.util.image.ImageLoader;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.core.AfGrpProfileInfo;
import com.core.AfPalmchat;
import com.core.cache.CacheManager;
//import com.umeng.analytics.MobclickAgent;

public class GroupListAdapter extends BaseAdapter {
    private ArrayList<AfGrpProfileInfo> mList = new ArrayList<AfGrpProfileInfo>();
    private Context mContext;
    private boolean mIsShareBrd;
//    private ListViewAddOn listViewAddOn = new ListViewAddOn();
    public GroupListAdapter(Context context, List<AfGrpProfileInfo> list,boolean isShareBrd) {
        mContext = context;
        mList.addAll(list);
        mIsShareBrd = isShareBrd;
    }


    public void setList(List<AfGrpProfileInfo> list) {
        mList.clear();
        mList.addAll(list);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public AfGrpProfileInfo getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View arg3, ViewGroup parent) {
        ViewHolder holder;
        if (arg3 == null) {
            arg3 = LayoutInflater.from(mContext).inflate(R.layout.grouplist_item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) arg3.findViewById(R.id.icon);
            holder.group_heads = (RelativeLayout) arg3.findViewById(R.id.group_heads);
            holder.group_head_1 = (ImageView) arg3.findViewById(R.id.group_head_1);
            holder.group_head_2 = (ImageView) arg3.findViewById(R.id.group_head_2);
            holder.group_head_3 = (ImageView) arg3.findViewById(R.id.group_head_3);
            holder.img_group_of_lord = (ImageView) arg3.findViewById(R.id.img_group_of_lord);
            holder.title = (TextView) arg3.findViewById(R.id.title);
            holder.textSort = (TextView) arg3.findViewById(R.id.friend_sort);
            holder.txt_group_member_count = (TextView) arg3.findViewById(R.id.group_member_count);
            holder.groupNameRow = (LinearLayout) arg3.findViewById(R.id.lin_name);
            arg3.setTag(holder);
        } else {
            holder = (ViewHolder) arg3.getTag();
        }
        final AfGrpProfileInfo infos = mList.get(position);
        holder.textSort.setVisibility(View.GONE);
        holder.txt_group_member_count.setVisibility(View.VISIBLE);
        //first item, add group btn
        holder.textSort.setVisibility(View.GONE);
        holder.title.setText(infos.name);
        String sign = infos.sig == null ? "" : infos.sig;
        holder.group_heads.setTag(infos);
        holder.group_heads.setVisibility(View.VISIBLE);// show group head
        if (infos.members != null) {
            holder.txt_group_member_count.setText(" (" + Integer.toString(infos.members.size()) + ")");
        }
        else {
            holder.txt_group_member_count.setVisibility(View.GONE);
        }
        if (infos != null && null != CacheManager.getInstance().getMyProfile().afId  && null != infos.admin && CacheManager.getInstance().getMyProfile().afId.equals(infos.admin)) {
            holder.img_group_of_lord.setVisibility(View.VISIBLE);
        } else {
            holder.img_group_of_lord.setVisibility(View.GONE);
        }
        holder.group_heads.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsShareBrd) {
                    Bundle b = new Bundle();
                    b.putString("come_page", "multichat_page");
                    b.putString("room_id", infos.afid);
                    b.putString("room_name", infos.name);
                    b.putBoolean("isForward",false);
                    if(((GroupListActivity)mContext) != null) {
                        ((GroupListActivity) mContext).jumpToPage(EditGroupActivity.class, b, false, 0, false);
                    }
                }
            }
        });
        holder.icon.setVisibility(View.GONE);// hide friend head
        setGroupAvatar(infos, holder);
        resetGroupNameWidth(holder,infos);
        return arg3;
    }

    private void resetGroupNameWidth(ViewHolder holder,AfGrpProfileInfo infos){
        int groupCountWidth;
        int w,h,lordWidh,parentWidth,textWidth;
        parentWidth =  holder.groupNameRow.getWidth();
        w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        if(parentWidth == 0) {
            holder.groupNameRow.measure(w, h);
            parentWidth = holder.groupNameRow.getMeasuredWidth();
        }
        holder.txt_group_member_count.measure(w, h);
        groupCountWidth = holder.txt_group_member_count.getMeasuredWidth();

        holder.title.measure(w, h);
        textWidth = holder.title.getMeasuredWidth();
        if (infos != null && null != CacheManager.getInstance().getMyProfile().afId  && null != infos.admin && CacheManager.getInstance().getMyProfile().afId.equals(infos.admin)) {
            holder.img_group_of_lord.measure(w, h);
            lordWidh = holder.img_group_of_lord.getMeasuredWidth();
            if((parentWidth - groupCountWidth- lordWidh - 30) > 60) {
                holder.title.setMaxWidth(parentWidth - groupCountWidth - lordWidh - 30);
            }
        }
        else {
            if((parentWidth - groupCountWidth - 15) > textWidth) {
                holder.title.setMaxWidth(parentWidth - groupCountWidth - 15);
            }
        }
    }


    private class ViewHolder {
        ImageView icon;
        TextView title;
        TextView textSort;

        public RelativeLayout group_heads;

        /**
         * small head image 01
         */
        public ImageView group_head_1;

        /**
         * small head image 02
         */
        public ImageView group_head_2;

        /**
         * small head image 03
         */
        public ImageView group_head_3;

        public ImageView img_group_of_lord;

        public TextView txt_group_member_count;

        public LinearLayout groupNameRow;
    }


    /**
     * ��Ⱥ����Ա��ͷ��ֵ
     * @param infos
     * @param holder
     */
    private void setGroupAvatar(AfGrpProfileInfo infos, ViewHolder holder) {
        holder.group_head_1.setImageResource(R.drawable.head_male2);
        holder.group_head_2.setImageResource(R.drawable.head_male2);
        holder.group_head_3.setImageResource(R.drawable.head_male2);
        ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
        imageViews.add(holder.group_head_1);
        imageViews.add(holder.group_head_2);
        imageViews.add(holder.group_head_3);
//        ImageLoader.getInstance().displayImage(imageViews, infos );
        ImageManager.getInstance().displayGroupHeadImage(imageViews, infos);
    }

}
