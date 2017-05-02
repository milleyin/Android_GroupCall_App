package com.afmobi.palmchat.ui.activity.friends.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.listener.FragmentSendFriendCallBack;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.groupchat.EditGroupActivity;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.main.cb.RefreshStateListener;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountDetailsActivity;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.StringUtil;
//import com.afmobi.palmchat.util.image.ImageLoader;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
//import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hj on 2015/12/23.
 */
public class RecentlyAdapter extends BaseAdapter {
    private Context context;
    private List<MainAfFriendInfo> list = new ArrayList<MainAfFriendInfo>();

    private LayoutInflater inflater;
    RefreshStateListener listener;
    FragmentSendFriendCallBack mFragmentSendFriendCallBack;

    public RecentlyAdapter(Context context,FragmentSendFriendCallBack mFragmentSendFriendCallBack) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mFragmentSendFriendCallBack = mFragmentSendFriendCallBack;
    }

    public void setAdapterData(List<MainAfFriendInfo> list) {
        this.list.clear();
        if (null != list) {
            this.list.addAll(list);
        }

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MainAfFriendInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.contacts_group_item,null);
            holder.vImageViewPhoto = (ImageView) convertView.findViewById(R.id.icon);
            holder.vTextViewName = (TextView) convertView.findViewById(R.id.title);
            holder.vSort = (TextView) convertView.findViewById(R.id.friend_sort);
//			group head
            holder.group_heads = (RelativeLayout) convertView.findViewById(R.id.group_heads);
            holder.group_head_1 = ( ImageView) convertView.findViewById(R.id.group_head_1);
            holder.group_head_2 = ( ImageView) convertView.findViewById(R.id.group_head_2);
            holder.group_head_3 = ( ImageView) convertView.findViewById(R.id.group_head_3);
            holder.img_group_of_lord = (ImageView) convertView.findViewById(R.id.img_group_of_lord);
            holder.viewParent = convertView.findViewById(R.id.parent);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.vSort.setVisibility(View.GONE);
//		recent chat list底层已排好序，正常显示即可
        final MainAfFriendInfo aff = (MainAfFriendInfo)list.get(position);
//		私聊
        if(aff.afFriendInfo != null) {

            final AfFriendInfo affFriendInfo = aff.afFriendInfo;

            holder.vImageViewPhoto.setVisibility(View.VISIBLE);
            holder.group_heads.setVisibility(View.GONE);
            holder.img_group_of_lord.setVisibility(View.GONE);

            if (affFriendInfo != null) {
//                if (!CommonUtils.showHeadImage(affFriendInfo.afId, holder.vImageViewPhoto, affFriendInfo.sex)) {
                    //调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
                    ImageManager.getInstance().DisplayAvatarImage(holder.vImageViewPhoto, affFriendInfo.getServerUrl(),
                            affFriendInfo.getAfidFromHead(), Consts.AF_HEAD_MIDDLE,affFriendInfo.sex,affFriendInfo.getSerialFromHead(),null);
//                }
            }

            holder.vTextViewName.setText(CommonUtils.getRealDisplayName(affFriendInfo));

            //click item
            holder.viewParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        // TODO: 2015/12/17 添加发送广播给好友的逻辑
                        if(mFragmentSendFriendCallBack != null){
                            mFragmentSendFriendCallBack.fragmentCallBack(true,aff.afFriendInfo);
                        }
                }
            });

            //click item
            holder.viewParent.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // TODO: 2015/12/17 添加发送广播给好友的逻辑
                    if(mFragmentSendFriendCallBack != null){
                        mFragmentSendFriendCallBack.fragmentCallBack(true,aff.afFriendInfo);
                    }
                    return false;
                }
            });
//		  群聊
        } else if(aff.afGrpInfo != null) {

            final AfGrpProfileInfo afGrpInfo = aff.afGrpInfo;

            if (null != afGrpInfo) {
                holder.vTextViewName.setText(afGrpInfo.name);
                ImageView[] imageviews = {holder.group_head_1,holder.group_head_2,holder.group_head_3};
                setGroupAvatar(imageviews, afGrpInfo, holder);
            }

            holder.vImageViewPhoto.setVisibility(View.GONE);
            holder.group_heads.setVisibility(View.VISIBLE);
            if (aff.afGrpInfo != null) {
                AfProfileInfo myProfile= CacheManager.getInstance().getMyProfile();
                if(myProfile!=null&&myProfile.afId!=null&&aff!=null&&aff.afGrpInfo!=null&&myProfile.afId.equals(aff.afGrpInfo.admin)){
                    holder.img_group_of_lord.setVisibility(View.VISIBLE);
                }else{
                    holder.img_group_of_lord.setVisibility(View.GONE);
                }
            }

            //click item
            holder.viewParent.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO: 2015/12/17  添加发送广播给好友的逻辑
                    if (mFragmentSendFriendCallBack != null) {
                        mFragmentSendFriendCallBack.fragmentCallBack(false, aff.afGrpInfo);
                    }
                }
            });
            //click item
            holder.viewParent.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // TODO: 2015/12/17 添加发送广播给好友的逻辑
                    if (mFragmentSendFriendCallBack != null) {
                        mFragmentSendFriendCallBack.fragmentCallBack(false, aff.afGrpInfo);
                    }
                    return false;
                }
            });
        }
        return convertView;

    }


    private void setGroupAvatar(ImageView[] imageviews, AfGrpProfileInfo infos, ViewHolder holder) {

        holder.group_head_1.setImageResource(R.drawable.head_male2);
        holder.group_head_2.setImageResource(R.drawable.head_male2);
        holder.group_head_3.setImageResource(R.drawable.head_male2);
//		holder.group_head_4.setImageResource(R.drawable.head_male);

        ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
        imageViews.add(holder.group_head_1);
        imageViews.add(holder.group_head_2);
        imageViews.add(holder.group_head_3);
//        ImageLoader.getInstance().displayImage(imageViews, infos );
        ImageManager.getInstance().displayGroupHeadImage(imageViews, infos);
    }

    private class ViewHolder {
        ImageView vImageViewPhoto;
        TextView vTextViewName;
        TextView vSort;
        //		群头像
        public View group_heads;

        /**
         * 小头像01
         */
        public  ImageView group_head_1;

        /**
         * 小头像02
         */
        public  ImageView group_head_2;

        /**
         * 小头像03
         */
        public  ImageView group_head_3;

        /**
         * 群主标识
         */
        public ImageView img_group_of_lord;
        public View viewParent;
    }
}
