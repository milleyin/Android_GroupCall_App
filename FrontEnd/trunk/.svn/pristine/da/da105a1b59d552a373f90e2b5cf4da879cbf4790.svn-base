package com.afmobi.palmchat.ui.activity.groupchat.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.gif.GifImageView;
import com.afmobi.palmchat.listener.OnItemLongClick;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.chats.ForwardSelectActivity;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.social.BroadcastDetailActivity;
import com.afmobi.palmchat.ui.activity.tagpage.TagPageActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnSelectButtonDialogListener;
import com.afmobi.palmchat.ui.customview.MyTextView;
import com.afmobi.palmchat.ui.customview.RoundImageView;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.DateUtil;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfAttachImageInfo;
import com.core.AfAttachPAMsgInfo;
import com.core.AfAttachVoiceInfo;
import com.core.AfFriendInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.param.AfImageReqParam;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GroupChattingAdapter extends BaseAdapter implements AfHttpResultListener {

    /**
     * 消息集合
     */
    private ArrayList<AfMessageInfo> listMsgs;

    private Activity mContext;

    private LayoutInflater mInflater;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    /**
     * 中间件类
     */
    private AfPalmchat mAfCorePalmchat;

    //记录handleid,用于取消与中间件的关联
    private int handleId;

    private AfProfileInfo myProfileInfo;
    private boolean mIsRecording;
    public static final long THREE_MIN = 3 * 60 * 1000;

    /**
     * 是否已经显示Menu
     */
    boolean isShowMenu;
    public GroupChattingAdapter(Activity context, ArrayList<AfMessageInfo> list, ListView listview ) {
        mContext = context;
        this.listMsgs = list;
        mInflater = LayoutInflater.from(context);
        myProfileInfo = CacheManager.getInstance().getMyProfile();
        mAfCorePalmchat = ((PalmchatApp) mContext.getApplicationContext()).mAfCorePalmchat;
    }

    @Override
    public void notifyDataSetChanged() {
        myProfileInfo = CacheManager.getInstance().getMyProfile();
        super.notifyDataSetChanged();
    }

    /** 设置正在录音
     * @param isRecording
     */
    public void setIsRecording(boolean isRecording) {
        mIsRecording = isRecording;
    }

    /** 设置消息集合
     * @param list
     */
    public void setList(ArrayList<AfMessageInfo> list) {
        this.listMsgs = list;
    }

    /** 取得消息集合
     * @return
     */
    public ArrayList<AfMessageInfo> getLists() {
        return listMsgs;
    }


    public int getCount() {
        return listMsgs.size();
    }

    public AfMessageInfo getItem(int position) {
//    	int pos = --position;
//		if(pos < 0) return null; 
//		return listMsgs.get(pos);
        return listMsgs.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    OnItemLongClick onItemLongClick;

    /** 设置点击项目事件
     * @param onItemLongClick
     */
    public void setOnItemClick(OnItemLongClick onItemLongClick) {
        this.onItemLongClick = onItemLongClick;
    }

    /** 取得点击事件
     * @return
     */
    public OnItemLongClick getOnItemClick() {
        return onItemLongClick;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        final AfMessageInfo entity = listMsgs.get(position);
        //信息类型  //信息类型  --文本消息、图片、表情、自定义表情
        final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & entity.type;
//    	final int msgTypeNew = entity.type;
        final int status = entity.status;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            viewHolder.position = position;

            convertView = mInflater.inflate(R.layout.item_chatting_to_text, null);
            viewHolder.l_chatting_date = (LinearLayout) convertView.findViewById(R.id.l_chatting_date);
            viewHolder.vTextViewSendTime = (TextView) convertView.findViewById(R.id.chatting_date);
            viewHolder.l_group_system_msg = (RelativeLayout) convertView.findViewById(R.id.l_group_system_msg);
            viewHolder.vTextViewGroupSystemText = (TextView) convertView.findViewById(R.id.textview_group_system_text);
            viewHolder.linearLayoutFromRecommend = (LinearLayout) convertView.findViewById(R.id.chatting_linearlayout_layout_from_recommend); //chatting_linearlayout_layout_from_recommend
            /*收到好友请求消息 begin*/
            viewHolder.imageViewFromRecommendIcon = (ImageView) convertView.findViewById(R.id.icon_from_recommend); //icon_from_recommend
            viewHolder.textViewFriendSignFromRecommend = (TextView) convertView.findViewById(R.id.friend_sign_from_recommend);//friend_sign_from_recommend
            viewHolder.textViewFriendSignFromAccept = (TextView) convertView.findViewById(R.id.accept);//accept
            viewHolder.textViewFriendSignFromIgnore = (TextView) convertView.findViewById(R.id.ignore);//ignore
            viewHolder.imageViewFromRecommendPhoto = (ImageView) convertView.findViewById(R.id.chatting_photo_from_recommend); //chatting_photo_from_recommend
            viewHolder.textViewFriendSignFromRecommendMsg = (TextView) convertView.findViewById(R.id.chatting_msg_content_from_recommend); //chatting_msg_content_from_recommend
            viewHolder.rightViewStub = (ViewStub) convertView.findViewById(R.id.view_stub_right);
            viewHolder.leftViewStub = (ViewStub) convertView.findViewById(R.id.view_stub_left);
            /*收到好友消息 end*/
            //由于view太多，导致性能很差，现在改为延迟加载布局
            if(MessagesUtils.isReceivedMessage(status)) {
                //初始化收到消息布局
                initLeftView(viewHolder,convertView);
            }
            else {
                //初始化发送消息布局
                initRightView(viewHolder,convertView);
            }
//	    	  convertView = buildView(viewHolder,entity,position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (MessagesUtils.isReceivedMessage(status)) {//收到消息
                if(viewHolder.initLeftViewStub == null) {
                    //初始化收到消息布局
                    initLeftView(viewHolder, convertView);
                }
            }
            else {
                if (viewHolder.initRightViewStub == null) {
                    //初始化发送消息布局
                    initRightView(viewHolder, convertView);
                }
            }
        }

//        AvatarImageInfo imageInfo = null;
        if (position < 0) return convertView;  

        viewHolder.l_group_system_msg.setVisibility(View.GONE);
        viewHolder.vTextViewGroupSystemText.setVisibility(View.GONE);
        if (MessagesUtils.isReceivedMessage(status)) {//收到消息
            PalmchatLogUtils.println("GroupChattingAdapter  position  " + position + " getView  " + entity.client_time);
            //点击用户头像事件
            viewHolder.vImageViewChattingPhoto.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    AfFriendInfo afFriendInfo2 = CacheManager.getInstance().searchAllFriendInfo(entity.fromAfId);
                    if (afFriendInfo2 != null && !afFriendInfo2.afId.startsWith(DefaultValueConstant._R)) {
                        toProfile(afFriendInfo2);
                    } else {
                        toProfileForAfid(entity.fromAfId);
                        PalmchatLogUtils.println("vImageViewChattingPhoto  get card failure.");
                    }
                }
            });

            viewHolder.vImageViewChattingPhoto.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub
                 /*   Object obj = entity.attach;
                    if (obj != null && obj instanceof AfFriendInfo) {
                        final AfFriendInfo afFriendInfo = (AfFriendInfo) entity.attach;
                        if (onItemLongClick != null) {
                            onItemLongClick.onItemClick(Chatting.ACTION_LONG_CLICK, afFriendInfo.name, afFriendInfo.afId, afFriendInfo.sex);
                        }
                        //toProfile(afFriendInfo);
                    } else {*/
                        AfFriendInfo afFriendInfo2 = CacheManager.getInstance().searchAllFriendInfo(entity.fromAfId);
                        if (afFriendInfo2 != null && !afFriendInfo2.afId.startsWith(DefaultValueConstant._R)) {
                            //toProfile(afFriendInfo2);
                            if (onItemLongClick != null) {
                                onItemLongClick.onItemClick(Chatting.ACTION_LONG_CLICK, afFriendInfo2.name, afFriendInfo2.afId, afFriendInfo2.sex);
                            }
                        } else {
                            PalmchatLogUtils.println("GroupChattingAdapter  vImageViewChattingPhoto  get card failure no name.");
                        }
                    //}
                    return true;
                }
            });

            viewHolder.vTextViewSendTime.setText(dateFormat.format(new Date(entity.client_time)));//(CommonUtils.getRealChatDate(mContext, System.currentTimeMillis(), entity.client_time));
            final AfFriendInfo afFriend = CacheManager.getInstance().searchAllFriendInfo(entity.fromAfId);
            if (afFriend != null) { //如果此用户不存在当前friend，则直接friend名直接显示afid
                viewHolder.vTextViewFriendName.setVisibility(View.VISIBLE);
                viewHolder.vTextViewFriendName.setText(afFriend.name);
                if (!TextUtils.isEmpty(afFriend.alias)) {
                    viewHolder.vTextViewFriendName.setText(afFriend.alias);
                }
            } else {
                viewHolder.vTextViewFriendName.setVisibility(View.VISIBLE);
                //getCorrectAfid方法去除a,只显示数字
                viewHolder.vTextViewFriendName.setText(CommonUtils.getCorrectAfid(entity.fromAfId));
            }

            final ImageView imgIconHead = viewHolder.vImageViewChattingPhoto;
            if (afFriend != null) {
                entity.mSex = afFriend.sex;
                entity.mName = afFriend.name;
                entity.mAlias = afFriend.alias;
                entity.headImagePath = afFriend.head_img_path;

//                if (!CommonUtils.showHeadImage(afFriend.afId, imgIconHead, afFriend.sex)) {//判断是否显示默认指定的头像(如系统帐号头像)
                	//wxl 20151012调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
        			ImageManager.getInstance().DisplayAvatarImage(imgIconHead, afFriend.getServerUrl(),
        					entity.fromAfId,Consts.AF_HEAD_MIDDLE,afFriend.sex,afFriend.getSerialFromHead(),null);
//                }
            } else {
                byte sex = entity.mSex;
              //wxl 20151012调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
    			ImageManager.getInstance().DisplayAvatarImage(imgIconHead, entity.getServerUrl(),
    					entity.fromAfId,Consts.AF_HEAD_MIDDLE, sex,entity.getSerialFromHead(),null);
            }
            switch (msgType) {//根据消息类型显示
                case AfMessageInfo.MESSAGE_TEXT: { //文本消息
                    viewHolder.myTextViewContentFromText.setText(entity.msg);//chatting_msg_content_from_text
//	    	    viewHolder.imageViewFromRecommendStatusText = (ImageView)convertView.findViewById(R.id.sending_status_from_text);//sending_status_from_text
                    CharSequence text = EmojiParser.getInstance(mContext).parse(entity.msg);
                    viewHolder.myTextViewContentFromText.setText(text);
                    CommonUtils.setUrlSpanLink(mContext,viewHolder.myTextViewContentFromText);
                    showOrDisappear(viewHolder, status, msgType);
                    viewHolder.myTextViewContentFromText.setOnLongClickListener(new OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            // TODO Auto-generated method stub
                            showLongClickItem(status, msgType, entity, v, position);
                            return true;
                        }
                    });
                    break;
                }
                case AfMessageInfo.MESSAGE_STORE_EMOTIONS: { //store表情
                    viewHolder.myTextViewContentFromTextGif.displayGif(mContext, viewHolder.myTextViewContentFromTextGif, entity.msg, null);
                    viewHolder.myTextViewContentFromTextGif.setOnLongClickListener(new OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            showLongClickItem(status, msgType, entity, v, position);
                            return true;
                        }
                    });
                    showOrDisappear(viewHolder, status, msgType);
                    break;
                }
                case AfMessageInfo.MESSAGE_VOICE: { //声音消息
                    final ImageView voiceIconFrom = viewHolder.imageViewChattingPlayIconFromVoice;
                    final ImageView voiceIconFromAnim = viewHolder.imageViewChattingPlayIconFromVoiceAnim;
                    final ImageView voiceStatusFrom = viewHolder.imageViewSendingStatusFromVoice;

                    final AfAttachVoiceInfo afAttachVoiceInfo = (AfAttachVoiceInfo) entity.attach;
                    if (afAttachVoiceInfo != null) {
                        viewHolder.textViewChattingPlayTimeFromVoice.setText(afAttachVoiceInfo.voice_len + "s");
                        final String path = showStopAnim(entity, voiceIconFrom, voiceIconFromAnim, afAttachVoiceInfo);

                        viewHolder.linearLayoutContentfromVoice.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                processVoice(entity, voiceIconFrom, voiceIconFromAnim, voiceStatusFrom, path);
                            }
                        });

                        if (entity.autoPlay) {
                            entity.autoPlay = false;
                            viewHolder.linearLayoutContentfromVoice.performClick();
                        }
                    } else {
                        viewHolder.textViewChattingPlayTimeFromVoice.setText("0s");
                    }

                    if (status == AfMessageInfo.MESSAGE_READ) {
                        voiceStatusFrom.setVisibility(View.GONE);
                    } else {
                        voiceStatusFrom.setVisibility(View.VISIBLE);
                    }

                    viewHolder.linearLayoutContentfromVoice.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showLongClickItem(status, msgType, entity, voiceIconFrom, position);
                            return true;
                        }
                    });

                    showOrDisappear(viewHolder, status, msgType);
                    break;
                }
                case AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG:
                case AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG: {
                    final AfAttachPAMsgInfo afAttachPAMsgInfo = (AfAttachPAMsgInfo) entity.attach;
                    if (afAttachPAMsgInfo != null) {
                        AfFriendInfo afFChatTemp = CacheManager.getInstance().searchAllFriendInfo(afAttachPAMsgInfo.afid);
                        if(afFChatTemp == null){
                            afFChatTemp = new AfFriendInfo();
                            afFChatTemp.afId = afAttachPAMsgInfo.afid;
                            afFChatTemp.head_img_path = afAttachPAMsgInfo.local_img_path;
                            afFChatTemp.name = afAttachPAMsgInfo.name;
                        }
                        if(TextUtils.isEmpty(afFChatTemp.name)) {
                            afFChatTemp.name = afFChatTemp.afId;
                        }
                        final AfFriendInfo afFChat = afFChatTemp;
                        if (afAttachPAMsgInfo.msgtype == AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG) {
                            viewHolder.from_share_title.setText(afAttachPAMsgInfo.content);
                            viewHolder.from_share_bellow_title.setText(mContext.getString(R.string.chatting_tags_post).replace("{$xxx}", CommonUtils.getMicrometerData(afAttachPAMsgInfo.postnum)));
                            viewHolder.from_share_header.setVisibility(View.GONE);
                        } else {
                            if (afFChat != null) {
                                viewHolder.from_share_header.setVisibility(View.VISIBLE);
                                final ImageView imgIconHeadFrom = viewHolder.from_share_header;
                                final String imgUrl = afFChat.head_img_path;
                                imgIconHeadFrom.setTag(imgUrl);
                                //调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
                                ImageManager.getInstance().DisplayAvatarImage(imgIconHeadFrom, afFChat.getServerUrl(), afFChat.getAfidFromHead()
                                        , Consts.AF_HEAD_MIDDLE, afFChat.sex, afFChat.getSerialFromHead(), null);
                            }
                            viewHolder.from_share_title.setText(afAttachPAMsgInfo.name);
                            if(TextUtils.isEmpty(afAttachPAMsgInfo.content)) {
                                viewHolder.from_share_bellow_title.setVisibility(View.GONE);
                            }
                            else{
                                CharSequence text = EmojiParser.getInstance(mContext).parse(afAttachPAMsgInfo.content,CommonUtils.dip2px(mContext, 13));
                                viewHolder.from_share_bellow_title.setText(text);
                                viewHolder.from_share_bellow_title.setVisibility(View.VISIBLE);
                            }
                        }

                        viewHolder.chatting_msg_content_from_share.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toBroadcastDetailOrTagPage(afAttachPAMsgInfo);
                            }
                        });
                        viewHolder.chatting_from_layout_from_share.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toBroadcastDetailOrTagPage(afAttachPAMsgInfo);
                            }
                        });
                       /* viewHolder.from_share_header.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(afFChat != null){
                                    toProfile(afFChat);
                                }
                                else{
                                    toProfileForAfid(afAttachPAMsgInfo.afid);
                                }
                            }
                        });
                        viewHolder.from_share_title.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(afAttachPAMsgInfo.msgtype == AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG){
                                    toBroadcastDetailOrTagPage(afAttachPAMsgInfo);
                                }
                                else {
                                    if (afFChat != null) {
                                        toProfile(afFChat);
                                    } else {
                                        toProfileForAfid(afAttachPAMsgInfo.afid);
                                    }
                                }
                            }
                        });*/
                        final ImageView imageContent = viewHolder.chatting_msg_content_from_share;
                        viewHolder.chatting_msg_content_from_share.setBackgroundResource(R.color.transparent);
                        String thumb_url = "";
                        if (!TextUtils.isEmpty(afAttachPAMsgInfo.imgurl)) {
                            thumb_url = CacheManager.getInstance().getThumb_Chatting_url(afAttachPAMsgInfo.imgurl, false);
                        }
                        if(!thumb_url.startsWith("http://")){//拼接url的
                            thumb_url = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerInfo()[4] + thumb_url;
                        }
                        // imageView异步显示图片，如果图片本地不存在就下载
                        ImageManager.getInstance().DisplayDownRoundImage(imageContent, thumb_url,R.drawable.img_default_share_broadcast, null);
                        showOrDisappear(viewHolder, status, msgType);
                        viewHolder.chatting_msg_content_from_share.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                showLongClickItem(status, msgType, entity, v, position);
                                return true;
                            }
                        });
                        viewHolder.chatting_from_layout_from_share.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                showLongClickItem(status, msgType, entity, v, position);
                                return true;
                            }
                        });
                    }
                    break;
                }

//                case AfMessageInfo.MESSAGE_EMOTIONS:趣味表情
                case AfMessageInfo.MESSAGE_IMAGE: {
                    final ImageView imageContent = viewHolder.imageViewChattingMsgContentFromImage;
                    final TextView imageContentEmotion = viewHolder.imageViewChattingMsgContentFromEmotion;
                    //趣味表情
                  /*  if (msgType == AfMessageInfo.MESSAGE_EMOTIONS) {
                        viewHolder.textViewChattingFileSizeFromImage.setVisibility(View.GONE);
                        imageContent.setVisibility(View.GONE);
                        imageContentEmotion.setVisibility(View.VISIBLE);
                        String text = entity.msg;
                        String[] s = text.split(":");
                        String emotionPath = "default/" + s[1] + "L.png";
                        imageContentEmotion.setText(text);
                        if (!CommonUtils.isEmpty(emotionPath)) {
                            imageContentEmotion.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    PalmchatLogUtils.println("imageContent  onclick group chat");
                                }
                            });
                            imageContentEmotion.setOnLongClickListener(new OnLongClickListener() {

                                @Override
                                public boolean onLongClick(View v) {
                                    showLongClickItem(status, msgType, entity, v, position);
                                    return true;
                                }
                            });
                        }
                    } else*/
                    {//图片
                        imageContent.setVisibility(View.VISIBLE);
                        imageContentEmotion.setVisibility(View.GONE);
                        final AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) entity.attach;
                        if (afAttachImageInfo != null) {

                            String path = RequestConstant.IMAGE_CACHE + afAttachImageInfo.small_file_name;
                          /*  ChatImageInfo chatImageInfo = new ChatImageInfo(path, false);
                            chatImageInfo.setWhich_screen(Consts.CHATTING);
                            if(isExistsImage(imageContent,path,afAttachImageInfo,chatImageInfo)) {
                                ImageLoader.getInstance().displayImage(imageContent, chatImageInfo);
                            }*/
                            ImageManager.getInstance().DisplayChatImage(imageContent,path,null,R.color.white,R.drawable.ic_loadfailed, Constants.CHATIMAGE_SIZE);
                            viewHolder.textViewChattingProgressFromImageFromImage.setVisibility(View.GONE);
                            imageContent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toImageActivity(entity._id, afAttachImageInfo, false, false);
                                }

                            });
                            viewHolder.textViewChattingFileSizeFromImage.setText(CommonUtils.showImageSize(afAttachImageInfo.large_file_size));
                        }

                        viewHolder.textViewChattingFileSizeFromImage.setVisibility(View.GONE);

                        viewHolder.imageViewChattingMsgContentFromImage.setOnLongClickListener(new OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                showLongClickItem(status, msgType, entity, v, position);
                                return true;
                            }
                        });
                    }

                    showOrDisappear(viewHolder, status, msgType);
                    break;
                }
                case AfMessageInfo.MESSAGE_CARD: { //朋友名片
                    final AfFriendInfo afFriendInfo = (AfFriendInfo) entity.attach;
                    if (afFriendInfo != null) {
                        viewHolder.textViewFriendNameFromCard.setText(afFriendInfo.name);
                        if (Consts.AFMOBI_SEX_FEMALE == afFriendInfo.sex) {
                            viewHolder.imageViewFriendSexFromCard.setBackgroundResource(R.drawable.icon_sexage_girl);
                            viewHolder.imageViewIconFromCard.setBackgroundResource(R.drawable.head_male2);
                        } else {
                            viewHolder.imageViewFriendSexFromCard.setBackgroundResource(R.drawable.icon_sexage_boy);
                            viewHolder.imageViewIconFromCard.setBackgroundResource(R.drawable.head_male2);
                        }
                        viewHolder.textViewFriendAgeFromCard.setBackgroundResource(Consts.AFMOBI_SEX_MALE == afFriendInfo.sex ? R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
                        viewHolder.textViewFriendAgeFromCard.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == afFriendInfo.sex ? R.drawable.icon_sexage_boy : R.drawable.icon_sexage_girl, 0, 0, 0);
                        viewHolder.textViewFriendAgeFromCard.setText(afFriendInfo.age + "");
                        viewHolder.textViewFriendAgeFromCard.setPadding(0, 0, ImageUtil.dip2px(mContext, 5), 0);
                        viewHolder.textViewchattingMsgContentFromCard.setText(mContext.getString(R.string.af_id) + ":" + CommonUtils.getCorrectAfid(afFriendInfo.afId));
                        final ImageView imgIcon = viewHolder.imageViewIconFromCard;

                        //wxl 20151012调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
            			ImageManager.getInstance().DisplayAvatarImage(imgIcon, afFriendInfo.getServerUrl(),
            					afFriendInfo.getAfidFromHead(),Consts.AF_HEAD_MIDDLE,afFriendInfo.sex,afFriendInfo.getSerialFromHead(),null);
                    } else {
                        final AfFriendInfo afFriendInfo2 = CacheManager.getInstance().searchAllFriendInfo(entity.msg);
                        if (afFriendInfo2 != null) {
                            viewHolder.textViewFriendNameFromCard.setText(afFriendInfo2.name);
                            if (Consts.AFMOBI_SEX_FEMALE == afFriendInfo2.sex) {
                                viewHolder.imageViewFriendSexFromCard.setBackgroundResource(R.drawable.icon_sexage_girl);
                                viewHolder.imageViewIconFromCard.setBackgroundResource(R.drawable.head_male2);
                            } else {
                                viewHolder.imageViewFriendSexFromCard.setBackgroundResource(R.drawable.icon_sexage_boy);
                                viewHolder.imageViewIconFromCard.setBackgroundResource(R.drawable.head_male2);
                            }
                            viewHolder.textViewFriendAgeFromCard.setBackgroundResource(Consts.AFMOBI_SEX_MALE == afFriendInfo2.sex ? R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
                            viewHolder.textViewFriendAgeFromCard.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == afFriendInfo2.sex ? R.drawable.icon_sexage_boy : R.drawable.icon_sexage_girl, 0, 0, 0);
                            viewHolder.textViewFriendAgeFromCard.setText(afFriendInfo2.age + "");
                            viewHolder.textViewFriendAgeFromCard.setPadding(0, 0, ImageUtil.dip2px(mContext, 5), 0);
                            viewHolder.textViewchattingMsgContentFromCard.setText(mContext.getString(R.string.af_id) + ":" + CommonUtils.getCorrectAfid(afFriendInfo2.afId));
//						sending(entity.status ,viewHolder.imageViewSendingStautsCard);
                            final ImageView imgIcon = viewHolder.imageViewIconFromCard;
                          //wxl 20151012调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
                			ImageManager.getInstance().DisplayAvatarImage(imgIcon, afFriendInfo2.getServerUrl(),
                					afFriendInfo2.getAfidFromHead(),Consts.AF_HEAD_MIDDLE,afFriendInfo2.sex,afFriendInfo2.getSerialFromHead(),null);
                        } else {
                            PalmchatLogUtils.println("from get card failure.");
                            viewHolder.textViewchattingMsgContentFromCard.setText(CommonUtils.getCorrectAfid(entity.msg));
                        }
                    }
                    showOrDisappear(viewHolder, status, msgType);
                    viewHolder.relativeLayoutFromCard.setOnLongClickListener(new View.OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            // TODO Auto-generated method stub
                            showLongClickItem(status, msgType, entity, v, position);
                            return true;
                        }
                    });
                    viewHolder.relativeLayoutFromCard.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (afFriendInfo != null) {
                                toProfile(afFriendInfo);
                            } else {
                                AfFriendInfo afFriendInfo2 = CacheManager.getInstance().searchAllFriendInfo(entity.msg);
                                if (afFriendInfo2 != null) {
                                    toProfile(afFriendInfo2);
                                } else {
                                    PalmchatLogUtils.println("relativeLayoutFromCard  get card failure.");
                                    toProfileForAfid(entity.msg);
                                }
                            }
                        }
                    });
                    break;
                }
                case AfMessageInfo.MESSAGE_FRIEND_REQ: {
                    showOrDisappear(viewHolder, status, msgType);
                    break;
                }
                case AfMessageInfo.MESSAGE_GRP_CREATE://群创建
                {
                    showSystemText(entity, viewHolder);
                    break;
                }
                case AfMessageInfo.MESSAGE_GRP_ADD_MEMBER://群添加成员
                {
                    showSystemText(entity, viewHolder);
                    break;
                }
                case AfMessageInfo.MESSAGE_GRP_FRIEND_REQ://群好友请求
                {

                    break;
                }
                case AfMessageInfo.MESSAGE_GRP_MODIFY://群更改
                {
                    showSystemText(entity, viewHolder);
                    break;
                }
                case AfMessageInfo.MESSAGE_GRP_REMOVE_MEMBER://群移除成员
                {
                    showSystemText(entity, viewHolder);
                    break;
                }
                case AfMessageInfo.MESSAGE_GRP_DROP://群某成员退群
                {
                    showSystemText(entity, viewHolder);
                    break;
                }
                case AfMessageInfo.MESSAGE_GRP_DESTROY://群销毁解散
                {
                    showSystemText(entity, viewHolder);
                    break;
                }
                case AfMessageInfo.MESSAGE_GRP_HAVE_BEEN_REMOVED://you have been removed
                {
                    showSystemText(entity, viewHolder);
                    break;
                }
                default:
                    break;
            }
        } else {//发送消息
            if(viewHolder.vTextViewFriendName != null) {
                viewHolder.vTextViewFriendName.setVisibility(View.INVISIBLE);
            }
            viewHolder.vTextViewSendTime.setText(dateFormat.format(new Date(entity.client_time)));//(CommonUtils.getRealChatDate(mContext, System.currentTimeMillis(), entity.client_time));
            if (null != myProfileInfo) {
                //调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
                ImageManager.getInstance().DisplayAvatarImage(viewHolder.mMyHeadImg, myProfileInfo.getServerUrl(),
                        myProfileInfo.getAfidFromHead(),Consts.AF_HEAD_MIDDLE,myProfileInfo.sex,myProfileInfo.getSerialFromHead(),null);
            }
            viewHolder.mMyHeadImg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mContext != null) {
                        if(mContext != null) {
                            mContext.startActivityForResult(new Intent(mContext, MyProfileActivity.class),IntentConstant.REQUEST_CODE_CHATTING);
                        }
                    }
                }
            });
            switch (msgType) {
                case AfMessageInfo.MESSAGE_TEXT: {

                    PalmchatLogUtils.println("entity " + entity + "  viewHolder.vTextViewContent  " + viewHolder.myTextViewContentText);
                    CharSequence text = EmojiParser.getInstance(mContext).parse(entity.msg);
                    viewHolder.myTextViewContentText.setText(text);
                    CommonUtils.setUrlSpanLink(mContext,viewHolder.myTextViewContentText);
                    showProgressBarOrNot(position, entity, msgType, status, viewHolder.imageViewSendingStautsTextImg, viewHolder.imageViewSendingStautsText);
                    showOrDisappear(viewHolder, status, msgType);
                    viewHolder.myTextViewContentText.setOnLongClickListener(new OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            // TODO Auto-generated method stub
                            showLongClickItem(status, msgType, entity, v, position);
                            return true;
                        }
                    });
                }
                break;
                case AfMessageInfo.MESSAGE_STORE_EMOTIONS: {
                    viewHolder.myTextViewContentTextGif.displayGif(mContext, viewHolder.myTextViewContentTextGif, entity.msg, null);
                    showProgressBarOrNot(position, entity, msgType, status, viewHolder.imageViewSendingStautsTextImgGif, viewHolder.imageViewSendingStautsTextGif);
                    showOrDisappear(viewHolder, status, msgType);

                    viewHolder.myTextViewContentTextGif.setOnLongClickListener(new OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            showLongClickItem(status, msgType, entity, v, position);
                            return true;
                        }
                    });
                    break;
                }
                case AfMessageInfo.MESSAGE_VOICE: {
                    final ImageView voiceIcon = viewHolder.imageViewPlayIconVoice;
                    final ImageView voiceIconAnim = viewHolder.imageViewPlayIconVoiceAnim;

                    final AfAttachVoiceInfo afAttachVoiceInfo = (AfAttachVoiceInfo) entity.attach;
                    if (afAttachVoiceInfo != null) {
                        viewHolder.textViewPlayTimeToVoice.setText(afAttachVoiceInfo.voice_len + "s");
                        final String path = showStopAnim(entity, voiceIcon, voiceIconAnim, afAttachVoiceInfo);

                        viewHolder.linearLayoutContentVoice.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                processVoice(entity, voiceIcon, voiceIconAnim, null, path);
                            }
                        });
                    } else {
                        viewHolder.textViewPlayTimeToVoice.setText("0s");
                    }

                    showProgressBarOrNot(position, entity, msgType, status, viewHolder.imageViewSendingStautsVoiceImg, viewHolder.imageViewSendingStautsVoice);

                    viewHolder.linearLayoutContentVoice.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showLongClickItem(status, msgType, entity, voiceIcon, position);
                            return true;
                        }
                    });

                    showOrDisappear(viewHolder, status, msgType);
                    break;
                }
                case AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG:
                case AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG: {
                    final AfAttachPAMsgInfo afAttachPAMsgInfo = (AfAttachPAMsgInfo) entity.attach;
                    if (afAttachPAMsgInfo != null) {
                        AfFriendInfo afFTemp = CacheManager.getInstance().searchAllFriendInfo(afAttachPAMsgInfo.afid);
                        if(afFTemp == null) {
                            afFTemp = new AfFriendInfo();
                            afFTemp.afId = afAttachPAMsgInfo.afid;
                            afFTemp.head_img_path = afAttachPAMsgInfo.local_img_path;
                            afFTemp.name = afAttachPAMsgInfo.name;
                        }
                        if(TextUtils.isEmpty(afFTemp.name)) {
                            afFTemp.name = afFTemp.afId;
                        }
                        final AfFriendInfo afF = afFTemp;
                        if (afAttachPAMsgInfo.msgtype == AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG) {
                            viewHolder.send_share_title.setText(afAttachPAMsgInfo.content);
                            viewHolder.send_share_bellow_title.setText(mContext.getString(R.string.chatting_tags_post).replace("{$xxx}", CommonUtils.getMicrometerData(afAttachPAMsgInfo.postnum)));
                            viewHolder.send_share_header.setVisibility(View.GONE);
                        } else {
                            if (afF != null) {
                                viewHolder.send_share_header.setVisibility(View.VISIBLE);
                                final ImageView imgIconHead = viewHolder.send_share_header;
                                final String imgUrl = afF.head_img_path;
                                imgIconHead.setTag(imgUrl);
                                //调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
                                ImageManager.getInstance().DisplayAvatarImage(imgIconHead, afF.getServerUrl(), afF.getAfidFromHead()
                                        , Consts.AF_HEAD_MIDDLE, afF.sex, afF.getSerialFromHead(), null);
                                viewHolder.send_share_title.setText(afF.name);
                            }
                            if(TextUtils.isEmpty(afAttachPAMsgInfo.content)) {
                                viewHolder.send_share_bellow_title.setVisibility(View.GONE);
                            }
                            else{
                                CharSequence text = EmojiParser.getInstance(mContext).parse(afAttachPAMsgInfo.content, CommonUtils.dip2px(mContext, 13));
                                viewHolder.send_share_bellow_title.setText(text);
                                viewHolder.send_share_bellow_title.setVisibility(View.VISIBLE);
                            }
                        }
                        viewHolder.chatting_send_layout_from_share.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toBroadcastDetailOrTagPage(afAttachPAMsgInfo);
                            }
                        });
                        viewHolder.chatting_msg_content_send_share.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toBroadcastDetailOrTagPage(afAttachPAMsgInfo);
                            }
                        });
                      /*  viewHolder.send_share_header.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(afF != null){
                                    toProfile(afF);
                                }
                                else{
                                    toProfileForAfid(afAttachPAMsgInfo.afid);
                                }
                            }
                        });
                        viewHolder.send_share_title.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(afAttachPAMsgInfo.msgtype == AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG){
                                    toBroadcastDetailOrTagPage(afAttachPAMsgInfo);
                                }
                                else {
                                    if (afF != null) {
                                        toProfile(afF);
                                    } else {
                                        toProfileForAfid(afAttachPAMsgInfo.afid);
                                    }
                                }
                            }
                        });*/
                        final ImageView imageContent = viewHolder.chatting_msg_content_send_share;
                        viewHolder.chatting_msg_content_send_share.setBackgroundResource(R.color.transparent);
                        String thumb_url = "";
                        if (!TextUtils.isEmpty(afAttachPAMsgInfo.imgurl)) {
                            thumb_url = CacheManager.getInstance().getThumb_Chatting_url(afAttachPAMsgInfo.imgurl, false);
                        }
                        // imageView异步显示图片，如果图片本地不存在就下载
                        ImageManager.getInstance().DisplayDownRoundImage(imageContent, thumb_url,R.drawable.img_default_share_broadcast, null);
                        showOrDisappear(viewHolder, status, msgType);
                        showProgressBarOrNot(position, entity, msgType, status, viewHolder.sending_status_text_img_share, viewHolder.sending_status_text_share);
                        viewHolder.chatting_msg_content_send_share.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                showLongClickItem(status, msgType, entity, v, position);
                                return true;
                            }
                        });
                        viewHolder.chatting_send_layout_from_share.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                showLongClickItem(status, msgType, entity, v, position);
                                return true;
                            }
                        });
                    }
                    break;
                }
//                case AfMessageInfo.MESSAGE_EMOTIONS:趣味表情没了
                case AfMessageInfo.MESSAGE_IMAGE: {
                    final ImageView imageContent = viewHolder.imageViewContentImage;
                    final ImageView imageContentEmotion = viewHolder.imageViewContentEmotion;
                    //趣味表情
                    /*if (msgType == AfMessageInfo.MESSAGE_EMOTIONS) {
                        imageContent.setVisibility(View.GONE);
                        imageContentEmotion.setVisibility(View.VISIBLE);
                        String text = entity.msg;
                        String[] s = text.split(":");
                        String emotionPath = "default/" + s[1] + "L.png";
                        if (!CommonUtils.isEmpty(emotionPath)) {
                            ChatImageInfo chatImageInfo = new ChatImageInfo(emotionPath, true);
                            ImageLoader.getInstance().displayImage(imageContentEmotion, chatImageInfo);
                        }
                        imageContentEmotion.setOnLongClickListener(new OnLongClickListener() {

                            @Override
                            public boolean onLongClick(View v) {
                                // TODO Auto-generated method stub
                                showLongClickItem(status, msgType, entity, v, position);
                                return true;
                            }
                        });
                        viewHolder.textViewSendingProgressImage.setVisibility(View.GONE);
                    } else */{//图片
                        imageContent.setVisibility(View.VISIBLE);
                        imageContentEmotion.setVisibility(View.GONE);
                        final AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) entity.attach;
                        if (afAttachImageInfo != null) {

                            String path = RequestConstant.IMAGE_CACHE + afAttachImageInfo.small_file_name;
                          /*  ChatImageInfo chatImageInfo = new ChatImageInfo(path, false);
                            chatImageInfo.setWhich_screen(Consts.CHATTING);
                            if(isExistsImage(imageContent,path,afAttachImageInfo,chatImageInfo)) {
                                ImageLoader.getInstance().displayImage(imageContent, chatImageInfo);
                            }*/
                            ImageManager.getInstance().DisplayChatImage(imageContent,path,null,R.color.white,R.drawable.ic_loadfailed, Constants.CHATIMAGE_SIZE);

                            viewHolder.textViewSendingProgressImage.setText(afAttachImageInfo.progress + "%");
                            if (AfMessageInfo.MESSAGE_SENT == status) {
                                viewHolder.textViewSendingProgressImage.setVisibility(View.GONE);
                            } else {
                                viewHolder.textViewSendingProgressImage.setVisibility(View.VISIBLE);
                            }
                            imageContent.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    toImageActivity(entity._id, afAttachImageInfo, true, false);
                                }

                            });
                        }

                    }
                    if (msgType == AfMessageInfo.MESSAGE_EMOTIONS) {
                        if (AfMessageInfo.MESSAGE_SENTING == status) {
                            showProgressBar(viewHolder.imageViewSendingStautsImage, viewHolder.progressbarStatusImage);
                        } else if (AfMessageInfo.MESSAGE_UNSENT == status) {
                            disappearProgressBar(viewHolder.imageViewSendingStautsImage, viewHolder.progressbarStatusImage);

                            showSendFail(viewHolder.imageViewSendingStautsImage, status, msgType, entity, position);

                        } else {
                            disappearProgressBar(viewHolder.imageViewSendingStautsImage, viewHolder.progressbarStatusImage);
                            showSendSuccessed(viewHolder.imageViewSendingStautsImage);
                        }
                        //sending(entity.status ,viewHolder.imageViewSendingStautsImage);
                    } else {
                        AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) entity.attach;
                        if (afAttachImageInfo != null) {
                            showProgressBarOrNot(position, entity, msgType, status, viewHolder.imageViewSendingStautsImage, viewHolder.progressbarStatusImage);
                        }
                    }
                    showOrDisappear(viewHolder, status, msgType);

                    viewHolder.imageViewContentImage.setOnLongClickListener(new OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            // TODO Auto-generated method stub
                            showLongClickItem(status, msgType, entity, v, position);
                            return true;
                        }
                    });
                    break;
                }
                case AfMessageInfo.MESSAGE_CARD: {
                    final AfFriendInfo afFriendInfo = (AfFriendInfo) entity.attach;
                    PalmchatLogUtils.println("AfFriendInfo  " + afFriendInfo + "  friendId  " + entity.msg);
                    if (afFriendInfo != null) {
                        viewHolder.textViewNameCard.setText(afFriendInfo.name);
                        if (Consts.AFMOBI_SEX_FEMALE == afFriendInfo.sex) {
                            viewHolder.imageViewSexCard.setBackgroundResource(R.drawable.icon_sexage_girl);
                            viewHolder.imageViewIconCard.setBackgroundResource(R.drawable.head_male2);
                        } else {
                            viewHolder.imageViewSexCard.setBackgroundResource(R.drawable.icon_sexage_boy);
                            viewHolder.imageViewIconCard.setBackgroundResource(R.drawable.head_male2);
                        }
                        viewHolder.textViewAgeCard.setBackgroundResource(Consts.AFMOBI_SEX_MALE == afFriendInfo.sex ? R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
                        viewHolder.textViewAgeCard.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == afFriendInfo.sex ? R.drawable.icon_sexage_boy : R.drawable.icon_sexage_girl, 0, 0, 0);
                        viewHolder.textViewAgeCard.setText(afFriendInfo.age + "");
                        viewHolder.textViewAgeCard.setPadding(0, 0, ImageUtil.dip2px(mContext, 5), 0);
                        String msg = mContext.getString(R.string.af_id) + ":" + CommonUtils.getCorrectAfid(afFriendInfo.afId);
                        viewHolder.textViewContentCard.setText(msg);

//						sending(entity.status ,viewHolder.imageViewSendingStautsCard, msgType, entity, position);
                        showProgressBarOrNot(position, entity, msgType, status, viewHolder.imageViewSendingStautsCardImg, viewHolder.imageViewSendingStautsCard);
                        final ImageView imgIcon = viewHolder.imageViewIconCard;

                      //wxl 20151012调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
            			ImageManager.getInstance().DisplayAvatarImage(imgIcon, afFriendInfo.getServerUrl(),
            					afFriendInfo.getAfidFromHead(),Consts.AF_HEAD_MIDDLE,afFriendInfo.sex,afFriendInfo.getSerialFromHead(),null);

                    } else {
                        final AfFriendInfo afFriendInfo2 = CacheManager.getInstance().searchAllFriendInfo(entity.msg);
                        final ImageView imgIcon = viewHolder.imageViewIconCard;
                        if (afFriendInfo2 != null) {
                            viewHolder.textViewNameCard.setText(afFriendInfo2.name);
                            if (Consts.AFMOBI_SEX_FEMALE == afFriendInfo2.sex) {
                                viewHolder.imageViewSexCard.setBackgroundResource(R.drawable.icon_sexage_girl);
                                viewHolder.imageViewIconCard.setBackgroundResource(R.drawable.head_male2);
                            } else {
                                viewHolder.imageViewSexCard.setBackgroundResource(R.drawable.icon_sexage_boy);
                                viewHolder.imageViewIconCard.setBackgroundResource(R.drawable.head_male2);
                            }
                            viewHolder.textViewAgeCard.setBackgroundResource(Consts.AFMOBI_SEX_MALE == afFriendInfo2.sex ? R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
                            viewHolder.textViewAgeCard.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == afFriendInfo2.sex ? R.drawable.icon_sexage_boy : R.drawable.icon_sexage_girl, 0, 0, 0);
                            viewHolder.textViewAgeCard.setText(afFriendInfo2.age + "");
                            viewHolder.textViewAgeCard.setPadding(0, 0, ImageUtil.dip2px(mContext, 5), 0);
                            if (afFriendInfo2 != null) {
                                String msg = mContext.getString(R.string.af_id) + ":" + CommonUtils.getCorrectAfid(afFriendInfo2.afId);
                                viewHolder.textViewContentCard.setText(mContext.getString(R.string.af_id) + ":" + CommonUtils.getCorrectAfid(afFriendInfo2.afId));
                            }
                            showProgressBarOrNot(position, entity, msgType, status, viewHolder.imageViewSendingStautsCardImg, viewHolder.imageViewSendingStautsCard);
                          
                            //wxl 20151012调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
                			ImageManager.getInstance().DisplayAvatarImage(imgIcon, afFriendInfo2.getServerUrl(),
                					afFriendInfo2.getAfidFromHead(),Consts.AF_HEAD_MIDDLE,afFriendInfo2.sex,afFriendInfo2.getSerialFromHead(),null);
                        } else {
                            PalmchatLogUtils.println("get card failure.");
                            viewHolder.textViewNameCard.setText(CommonUtils.getCorrectAfid(entity.msg));
                        }

                    }
                    showOrDisappear(viewHolder, status, msgType);

                    viewHolder.relativeLayoutToCard.setOnLongClickListener(new View.OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            // TODO Auto-generated method stub
                            showLongClickItem(status, msgType, entity, v, position);
                            return true;
                        }
                    });
                    viewHolder.relativeLayoutToCard.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub

                            if (afFriendInfo != null) {
                                toProfile(afFriendInfo);
                            } else {
                                AfFriendInfo afFriendInfo2 = CacheManager.getInstance().searchAllFriendInfo(entity.msg);
                                if (afFriendInfo2 != null) {
                                    toProfile(afFriendInfo2);
                                } else {
                                    PalmchatLogUtils.println("relativeLayoutToCard  get card failure.");
                                    toProfileForAfid(entity.msg);
                                }
                            }
                        }
                    });
                    break;
                }
                case AfMessageInfo.MESSAGE_FRIEND_REQ: {

                    showOrDisappear(viewHolder, status, msgType);
                    break;
                }

                default:
                    break;
            }

        }
        if (MessagesUtils.isReceivedMessage(status) && msgType == AfMessageInfo.MESSAGE_FRIEND_REQ) {//receive friend request
            viewHolder.l_chatting_date.setVisibility(View.GONE);
            viewHolder.vTextViewSendTime.setVisibility(View.GONE);
            viewHolder.l_group_system_msg.setVisibility(View.GONE);
            viewHolder.vTextViewGroupSystemText.setVisibility(View.GONE);
            return convertView;
        }
        showTimeLine(viewHolder.l_chatting_date, viewHolder.vTextViewSendTime, position);

//    	showTimeLine(viewHolder.vTextViewSendTime, position);
        return convertView;
    }



    private void toBroadcastDetailOrTagPage(final AfAttachPAMsgInfo afAttachPAMsgInfo){
        Intent intent = new Intent();
        if (afAttachPAMsgInfo.msgtype == AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG) {
            Bundle bundle = new Bundle();
            AfResponseComm.AfChapterInfo afChapterInfo = new AfResponseComm.AfChapterInfo();
            afChapterInfo.mid = afAttachPAMsgInfo.mid;
            bundle.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo);
            bundle.putInt(JsonConstant.KEY_BC_SKIP_TYPE, BroadcastDetailActivity.TYPE_BROADCASTLIST);
            intent.setClass((Activity) mContext, BroadcastDetailActivity.class);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        } else {
            intent.setClass((Activity) mContext, TagPageActivity.class);
            intent.putExtra(IntentConstant.TITLENAME, afAttachPAMsgInfo.content);
            mContext.startActivity(intent);
        }
    }

    /** 初始化发送消息布局
     * @param viewHolder
     * @param convertView
     */
    private void initLeftView(ViewHolder viewHolder,View convertView){
        viewHolder.initLeftViewStub = viewHolder.leftViewStub.inflate();
        viewHolder.leftViewStub.setVisibility(View.VISIBLE);
        viewHolder.vTextViewFriendName = (TextView) convertView.findViewById(R.id.tv_name);
        viewHolder.vTextViewFriendName.setVisibility(View.VISIBLE);
        //photo(head) image
        viewHolder.vImageViewChattingPhoto = (ImageView) convertView.findViewById(R.id.chatting_photo);
        viewHolder.relativeLayoutFrom = (RelativeLayout) convertView.findViewById(R.id.chatting_from_layout_from);
        //receive text
        viewHolder.relativeLayoutFromText = (RelativeLayout) convertView.findViewById(R.id.chatting_from_layout_from_text);//chatting_from_layout_from_text
        viewHolder.myTextViewContentFromText = (MyTextView) convertView.findViewById(R.id.chatting_msg_content_from_text);//chatting_msg_content_from_text
//    	    viewHolder.imageViewFromRecommendStatusText = (ImageView)convertView.findViewById(R.id.sending_status_from_text);//sending_status_from_text
        //receive gif
        viewHolder.relativeLayoutFromTextGif = (RelativeLayout) convertView.findViewById(R.id.chatting_from_layout_text_gif);//chatting_from_layout_text_gif
        viewHolder.myTextViewContentFromTextGif = (GifImageView) convertView.findViewById(R.id.chatting_msg_content_from_text_gift);//chatting_msg_content_from_text_gift
        //receive voice
        viewHolder.relativeLayoutFromVoice = (RelativeLayout) convertView.findViewById(R.id.chatting_from_layout_from_voice);//chatting_from_layout_from_voice
        viewHolder.linearLayoutContentfromVoice = (LinearLayout) convertView.findViewById(R.id.chatting_msg_content_from_voice);//chatting_from_layout_from_voice
        viewHolder.textViewChattingPlayTimeFromVoice = (TextView) convertView.findViewById(R.id.chatting_play_time_from_voice);//chatting_play_time_from_voice
        viewHolder.imageViewChattingPlayIconFromVoice = (ImageView) convertView.findViewById(R.id.chatting_play_icon_from_voice);//chatting_play_icon_from_voice
        viewHolder.imageViewChattingPlayIconFromVoiceAnim = (ImageView) convertView.findViewById(R.id.chatting_play_icon_from_voice_anim);//chatting_play_icon_from_voice_anim
        viewHolder.imageViewSendingStatusFromVoice = (ImageView) convertView.findViewById(R.id.sending_status_from_voice);//sending_status_from_voice
        //receive image
        viewHolder.relativeLayoutFromImage = (RelativeLayout) convertView.findViewById(R.id.chatting_from_layout_from_image);//chatting_from_layout_from_image
        viewHolder.imageViewChattingMsgContentFromImage = (RoundImageView) convertView.findViewById(R.id.chatting_msg_content_from_image);//chatting_msg_content_from_image
        viewHolder.imageViewChattingMsgContentFromEmotion = (TextView) convertView.findViewById(R.id.chatting_msg_content_from_emotion);//chatting_msg_content_from_image
        viewHolder.textViewChattingFileSizeFromImage = (TextView) convertView.findViewById(R.id.chatting_file_size_from_image);//chatting_file_size_from_image
        viewHolder.textViewChattingProgressFromImageFromImage = (TextView) convertView.findViewById(R.id.chatting_progress_from_image);//chatting_progress_from_image
        //receive card
        viewHolder.relativeLayoutFromCard = (RelativeLayout) convertView.findViewById(R.id.chatting_from_layout_from_card);//chatting_from_layout_from_card
        viewHolder.imageViewIconFromCard = (ImageView) convertView.findViewById(R.id.icon_from_card);//icon_from_card
        viewHolder.textViewFriendNameFromCard = (TextView) convertView.findViewById(R.id.friend_name_from_card);//friend_name_from_card
        viewHolder.imageViewFriendSexFromCard = (ImageView) convertView.findViewById(R.id.friend_sex_from_card);//friend_sex_from_card
        viewHolder.textViewFriendAgeFromCard = (TextView) convertView.findViewById(R.id.friend_age_from_card);//friend_age_from_card
        viewHolder.textViewchattingMsgContentFromCard = (TextView) convertView.findViewById(R.id.chatting_msg_content_from_card);//chatting_msg_content_from_card
//    	    viewHolder.imageViewSendingStatusFromCard = (ImageView)convertView.findViewById(R.id.sending_status_from_card);//sending_status_from_card

        viewHolder.chatting_from_layout_from_share = convertView.findViewById(R.id.chatting_from_layout_from_share);
        viewHolder.from_share_header = (ImageView) convertView.findViewById(R.id.from_share_header);
        viewHolder.from_share_title = (TextView) convertView.findViewById(R.id.from_share_title);
        viewHolder.from_share_bellow_title = (TextView) convertView.findViewById(R.id.from_share_bellow_title);
        viewHolder.chatting_msg_content_from_share = (ImageView) convertView.findViewById(R.id.chatting_msg_content_from_share);
    		/*收到消息end*/
    }

    /** 初始化发送消息布局
     * @param viewHolder
     * @param convertView
     */
    private void initRightView(ViewHolder viewHolder,View convertView){
  /*自己发送消息给别人 */
        viewHolder.initRightViewStub=viewHolder.rightViewStub.inflate();
        viewHolder.rightViewStub.setVisibility(View.VISIBLE);
        viewHolder.mMyHeadImg = (ImageView) convertView.findViewById(R.id.chatting_right_photo);
        viewHolder.relativeLayoutTo = (RelativeLayout) convertView.findViewById(R.id.chatting_to_layout_to);
                /*发送文本*/
        viewHolder.relativeLayoutToText = (RelativeLayout) convertView.findViewById(R.id.chatting_to_layout_text);
        viewHolder.imageViewSendingStautsTextImg = (ImageView) convertView.findViewById(R.id.sending_status_text_img);
        viewHolder.imageViewSendingStautsText = (ProgressBar) convertView.findViewById(R.id.sending_status_text);
        viewHolder.myTextViewContentText = (MyTextView) convertView.findViewById(R.id.chatting_msg_content_text);
            /*send gif image*/
        viewHolder.relativeLayoutToTextGif = (RelativeLayout) convertView.findViewById(R.id.chatting_to_layout_text_gif);
        viewHolder.imageViewSendingStautsTextImgGif = (ImageView) convertView.findViewById(R.id.sending_status_text_img_gif);
        viewHolder.imageViewSendingStautsTextGif = (ProgressBar) convertView.findViewById(R.id.sending_status_text_gif);
        viewHolder.myTextViewContentTextGif = (GifImageView) convertView.findViewById(R.id.chatting_msg_content_text_gift);
                /*发送语音*/
        viewHolder.relativeLayoutToVoice = (RelativeLayout) convertView.findViewById(R.id.chatting_to_layout_voice);
        viewHolder.imageViewSendingStautsVoiceImg = (ImageView) convertView.findViewById(R.id.sending_status_voice_img);
        viewHolder.imageViewSendingStautsVoice = (ProgressBar) convertView.findViewById(R.id.sending_status_voice);
        viewHolder.linearLayoutContentVoice = (LinearLayout) convertView.findViewById(R.id.chatting_msg_content_voice);
        viewHolder.imageViewPlayIconVoice = (ImageView) convertView.findViewById(R.id.chatting_play_icon_to_voice);
        viewHolder.imageViewPlayIconVoiceAnim = (ImageView) convertView.findViewById(R.id.chatting_play_icon_to_voice_anim);
        viewHolder.textViewPlayTimeToVoice = (TextView) convertView.findViewById(R.id.chatting_play_time_to_voice);

				/*发送图片*/
        viewHolder.relativeLayoutToImage = (RelativeLayout) convertView.findViewById(R.id.chatting_to_layout_image);
        viewHolder.progressbarStatusImage = (ProgressBar) convertView.findViewById(R.id.progressbar_status);
        viewHolder.imageViewSendingStautsImage = (ImageView) convertView.findViewById(R.id.sending_status_image);
        viewHolder.textViewSendingProgressImage = (TextView) convertView.findViewById(R.id.chatting_progress_image);
        viewHolder.imageViewContentImage = (RoundImageView) convertView.findViewById(R.id.chatting_msg_content_image);
        viewHolder.imageViewContentEmotion = (ImageView) convertView.findViewById(R.id.chatting_msg_content_emotion);

				/*发送名片*/
        viewHolder.relativeLayoutToCard = (RelativeLayout) convertView.findViewById(R.id.chatting_to_layout_card);
        viewHolder.imageViewSendingStautsCardImg = (ImageView) convertView.findViewById(R.id.sending_status_card_img);
        viewHolder.imageViewSendingStautsCard = (ProgressBar) convertView.findViewById(R.id.sending_status_card);
        viewHolder.imageViewIconCard = (ImageView) convertView.findViewById(R.id.icon);
        viewHolder.textViewNameCard = (TextView) convertView.findViewById(R.id.friend_name);
        viewHolder.imageViewSexCard = (ImageView) convertView.findViewById(R.id.friend_sex);
        viewHolder.textViewAgeCard = (TextView) convertView.findViewById(R.id.friend_age);
        viewHolder.textViewContentCard = (TextView) convertView.findViewById(R.id.chatting_msg_content_card);
        viewHolder.chatting_send_layout_from_share = convertView.findViewById(R.id.chatting_send_layout_from_share);
        viewHolder.send_share_header = (ImageView) convertView.findViewById(R.id.send_share_header);
        viewHolder.send_share_title = (TextView) convertView.findViewById(R.id.send_share_title);
        viewHolder.send_share_bellow_title = (TextView) convertView.findViewById(R.id.send_share_bellow_title);
        viewHolder.chatting_msg_content_send_share = (ImageView) convertView.findViewById(R.id.chatting_msg_content_send_share);
        viewHolder.sending_status_layout_send_share= convertView.findViewById(R.id.sending_status_layout_send_share);
        viewHolder.sending_status_text_img_share= (ImageView) convertView.findViewById(R.id.sending_status_text_img_share);
        viewHolder.sending_status_text_share = (ProgressBar) convertView.findViewById(R.id.sending_status_text_share);
    }

    /** 显示系统信息
     * @param entity
     * @param viewHolder
     */
    private void showSystemText(final AfMessageInfo entity,
                                ViewHolder viewHolder) {
        viewHolder.l_group_system_msg.setVisibility(View.VISIBLE);
        viewHolder.vTextViewGroupSystemText.setVisibility(View.VISIBLE);
        viewHolder.vTextViewGroupSystemText.setText(EmojiParser.getInstance(mContext).parse(entity.msg));
        disableFromAndTo(viewHolder);
    }


    /** 隐藏接收和发送消息布局
     * @param viewHolder
     */
    private void disableFromAndTo(ViewHolder viewHolder) {
        if(viewHolder.relativeLayoutFrom != null) {
            viewHolder.relativeLayoutFrom.setVisibility(View.GONE);
        }
        if(viewHolder.relativeLayoutTo != null){
            viewHolder.relativeLayoutTo.setVisibility(View.GONE);
        }
    }


    /** 显示图片发送进度
     * @param imageViewSendingStautsImage
     * @param progressbarStatusImage
     */
    private void showProgressBar(ImageView imageViewSendingStautsImage,
                                 ProgressBar progressbarStatusImage) {
        // TODO Auto-generated method stub
        imageViewSendingStautsImage.setVisibility(View.GONE);
        progressbarStatusImage.setVisibility(View.VISIBLE);
    }

    private void disappearProgressBar(ImageView imageViewSendingStautsImage,
                                      ProgressBar progressbarStatusImage) {
        imageViewSendingStautsImage.setVisibility(View.VISIBLE);
        progressbarStatusImage.setVisibility(View.GONE);
    }

    /** 根据消息的类型不同，显示或隐藏view控件
     * @param viewHolder
     * @param status 消息状态
     * @param msgType 消息类型
     */
    private void showOrDisappear(ViewHolder viewHolder, int status, int msgType) {
        if (MessagesUtils.isReceivedMessage(status)) {//收到
            if(viewHolder.relativeLayoutFrom != null) {
                viewHolder.relativeLayoutFrom.setVisibility(View.VISIBLE);
            }
            if(viewHolder.relativeLayoutTo != null) {
                viewHolder.relativeLayoutTo.setVisibility(View.GONE);
            }
            viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
            switch (msgType) {
                case AfMessageInfo.MESSAGE_TEXT: {  //收到的文本消息，则只显示文本布局
                    viewHolder.relativeLayoutFromText.setVisibility(View.VISIBLE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.GONE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);
                    break;
                }
                case AfMessageInfo.MESSAGE_STORE_EMOTIONS: { //收到的store表情消息，则只显示store表情布局
                    viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.VISIBLE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.GONE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);
                    break;
                }
                case AfMessageInfo.MESSAGE_VOICE: {  //收到的语音消息，则只显示语音布局
                    viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.VISIBLE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.GONE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);
                    break;
                }
                case AfMessageInfo.MESSAGE_EMOTIONS:
                case AfMessageInfo.MESSAGE_IMAGE: { //收到表情或图片，则只显示表情或图片
                    viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.VISIBLE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.GONE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);
                    break;
                }
                case AfMessageInfo.MESSAGE_CARD: {  //收到名片消息，则只显示名片布局
                    viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.VISIBLE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);
                    break;
                }
                case AfMessageInfo.MESSAGE_FRIEND_REQ: { //朋友请求消息
                    viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.GONE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.VISIBLE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);
                    disableFromAndTo(viewHolder);
                    break;
                }
                case AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG:
                case AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG:
                    viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.GONE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        } else { //发送消息
            if(viewHolder.relativeLayoutTo != null) {
                viewHolder.relativeLayoutTo.setVisibility(View.VISIBLE);
            }
            if(viewHolder.relativeLayoutFrom != null) {
                viewHolder.relativeLayoutFrom.setVisibility(View.GONE);
            }
            viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
            switch (msgType) {
                case AfMessageInfo.MESSAGE_TEXT:
                    viewHolder.relativeLayoutToText.setVisibility(View.VISIBLE);
                    viewHolder.relativeLayoutToTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToCard.setVisibility(View.GONE);
                    viewHolder.chatting_send_layout_from_share.setVisibility(View.GONE);
                    break;
                case AfMessageInfo.MESSAGE_STORE_EMOTIONS:
                    viewHolder.relativeLayoutToText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToTextGif.setVisibility(View.VISIBLE);
                    viewHolder.relativeLayoutToVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToCard.setVisibility(View.GONE);
                    viewHolder.chatting_send_layout_from_share.setVisibility(View.GONE);
                    break;
                case AfMessageInfo.MESSAGE_VOICE:
                    viewHolder.relativeLayoutToText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToVoice.setVisibility(View.VISIBLE);
                    viewHolder.relativeLayoutToImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToCard.setVisibility(View.GONE);
                    viewHolder.chatting_send_layout_from_share.setVisibility(View.GONE);
                    break;
                case AfMessageInfo.MESSAGE_EMOTIONS:
                case AfMessageInfo.MESSAGE_IMAGE:
                    viewHolder.relativeLayoutToText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToImage.setVisibility(View.VISIBLE);
                    viewHolder.relativeLayoutToCard.setVisibility(View.GONE);
                    viewHolder.chatting_send_layout_from_share.setVisibility(View.GONE);
                    break;
                case AfMessageInfo.MESSAGE_CARD:
                    viewHolder.relativeLayoutToText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToCard.setVisibility(View.VISIBLE);
                    viewHolder.chatting_send_layout_from_share.setVisibility(View.GONE);
                    break;
                case AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG:
                case AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG:
                    viewHolder.relativeLayoutToText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutToCard.setVisibility(View.GONE);
                    viewHolder.chatting_send_layout_from_share.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

    }


    /** 根据条件显示或隐藏进度条
     * @param position 当前集合位置
     * @param entity 消息对象
     * @param msgType 消息类型
     * @param status 消息状态
     * @param img ImageView对象
     * @param progressBar 进度条控件
     */
    private void showProgressBarOrNot(final int position, final AfMessageInfo entity, final int msgType, final int status, ImageView img, ProgressBar progressBar) {
        if (AfMessageInfo.MESSAGE_SENTING == status) {//正在发送图片
            PalmchatLogUtils.println("position  " + position + "TIME  sending  " + dateFormat.format(new Date()) + "  ");
            showProgressBar(img, progressBar);//显示进度条
        } else if (AfMessageInfo.MESSAGE_UNSENT == status) { //发送失败
            PalmchatLogUtils.println("position  " + position + "TIME  fail  " + dateFormat.format(new Date()) + "  ");
            disappearProgressBar(img, progressBar);//不显示进度条
            showSendFail(img, status, msgType, entity, position);//显示发送失败
        } else {
            PalmchatLogUtils.println("position  " + position + "TIME  success  " + dateFormat.format(new Date()) + "  ");
            disappearProgressBar(img, progressBar);//不显示进度条
            showSendSuccessed(img);//显示发送成功
        }
    }

    private AnimationDrawable animDrawable;

    /** 显示消息发送成功的状态
     * @param view
     */
    private void showSendSuccessed(final View view) {
        view.setVisibility(View.GONE);
        view.setBackgroundResource(R.drawable.chatings_sent_icon);
    }

    /** 显示消息发送失败的状态
     * @param view
     * @param status
     * @param msgType
     * @param entity
     * @param position
     */
    private void showSendFail(final View view, final int status, final int msgType, final AfMessageInfo entity, final int position) {
        view.setVisibility(View.VISIBLE);
        view.setBackgroundResource(R.drawable.msg_fail_icon);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showLongClickItemForResend(status, msgType, entity, view, position);
            }
        });
    }


    /** 停止播放语音动画并返回语音文件路径
     * @param entity 消息对象
     * @param voiceIconFrom ImageView控件对象
     * @param voiceIconFromAnim ImageView控件对象
     * @param afAttachVoiceInfo 附件信息
     * @return 返回语音文件存储路径
     */
    private String showStopAnim(final AfMessageInfo entity, final ImageView voiceIconFrom, final ImageView voiceIconFromAnim, final AfAttachVoiceInfo afAttachVoiceInfo) {
        final String path = RequestConstant.VOICE_CACHE + afAttachVoiceInfo.file_name;
        if (!path.equals(VoiceManager.getInstance().getPath()) && VoiceManager.getInstance().getPlayer() != null && VoiceManager.getInstance().getPlayer().isPlaying()) {
            stopPlayAnim(voiceIconFromAnim, voiceIconFrom);
        } else if (path.equals(VoiceManager.getInstance().getPath()) && VoiceManager.getInstance().getPlayer() != null && VoiceManager.getInstance().getPlayer().isPlaying()) {
            //需要重新赋值，否则在播放一个语音的同时又接收到一个语音，此时再点击播放新语音时，会出现第一个的播放动画未停止
            VoiceManager.getInstance().setView(voiceIconFromAnim);
            VoiceManager.getInstance().setPlayIcon(voiceIconFrom);
            startPlayAnim(voiceIconFromAnim, voiceIconFrom);
        } else {
            stopPlayAnim(voiceIconFromAnim, voiceIconFrom);
        }
        return path;
    }

    static class ViewHolder {

        public ViewStub leftViewStub;
        public ViewStub rightViewStub;
        public View initLeftViewStub;
        public View initRightViewStub;
        public RelativeLayout l_group_system_msg;
        //消息时间
        public LinearLayout l_chatting_date;
        public TextView vTextViewSendTime;
        public TextView vTextViewGroupSystemText;//textview_group_system_text
        public TextView vTextViewFriendName;//tv_name
        //公用头像(除收到的好友请求消息imageViewFromRecommendIcon/imageViewFromRecommendPhoto外)
        public ImageView vImageViewChattingPhoto;//chatting_photo
        public int position;

        /*收到消息begin*/
        public RelativeLayout relativeLayoutFrom;//chatting_from_layout_from
        //receive text
        public RelativeLayout relativeLayoutFromText;//chatting_from_layout_from_text
        public MyTextView myTextViewContentFromText;//chatting_msg_content_from_text
        public ImageView imageViewFromRecommendStatusText;//sending_status_from_text
        //receive gif
        public RelativeLayout relativeLayoutFromTextGif;//chatting_from_layout_from_text
        public GifImageView myTextViewContentFromTextGif;//chatting_msg_content_from_text
        //receive voice
        public RelativeLayout relativeLayoutFromVoice;//chatting_from_layout_from_voice
        public LinearLayout linearLayoutContentfromVoice;
        public TextView textViewChattingPlayTimeFromVoice;//chatting_play_time_from_voice
        public ImageView imageViewChattingPlayIconFromVoice;//chatting_play_icon_from_voice
        public ImageView imageViewChattingPlayIconFromVoiceAnim;//chatting_play_icon_from_voice_anim
        public ImageView imageViewSendingStatusFromVoice;//sending_status_from_voice
        //receive imaeg
        public RelativeLayout relativeLayoutFromImage;//chatting_from_layout_from_image
        public RoundImageView imageViewChattingMsgContentFromImage;//chatting_msg_content_from_image
        public TextView imageViewChattingMsgContentFromEmotion;//chatting_msg_content_from_emotion
        public TextView textViewChattingFileSizeFromImage;//chatting_file_size_from_image
        public TextView textViewChattingProgressFromImageFromImage;//chatting_progress_from_image
        //receive card
        public RelativeLayout relativeLayoutFromCard;//chatting_from_layout_from_card
        public ImageView imageViewIconFromCard;//icon_from_card
        public TextView textViewFriendNameFromCard;//friend_name_from_card
        public ImageView imageViewFriendSexFromCard;//friend_sex_from_card
        public TextView textViewFriendAgeFromCard;//friend_age_from_card
        public TextView textViewchattingMsgContentFromCard;//chatting_msg_content_from_card
        public ImageView imageViewSendingStatusFromCard;//sending_status_from_card

        /*收到好友请求消息 begin*/
        public LinearLayout linearLayoutFromRecommend;//chatting_linearlayout_layout_from_recommend
        public ImageView imageViewFromRecommendIcon; //icon_from_recommend
        public TextView textViewFriendSignFromRecommend;//friend_sign_from_recommend
        public TextView textViewFriendSignFromAccept;//accept
        public TextView textViewFriendSignFromIgnore;//ignore
        public ImageView imageViewFromRecommendPhoto; //chatting_photo_from_recommend
        public TextView textViewFriendSignFromRecommendMsg; //chatting_msg_content_from_recommend
		/*收到好友消息 end*/

        public View chatting_from_layout_from_share;
        public ImageView from_share_header;
        public TextView from_share_title;
        public TextView from_share_bellow_title;
        public ImageView chatting_msg_content_from_share;
		/*收到消息end*/


        /*自己发送消息 begin*/
        public RelativeLayout relativeLayoutTo;
        //send text
        public RelativeLayout relativeLayoutToText;
        public ImageView imageViewSendingStautsTextImg;
        public ProgressBar imageViewSendingStautsText;
        public MyTextView myTextViewContentText;
        //send gif
        public RelativeLayout relativeLayoutToTextGif;
        public ImageView imageViewSendingStautsTextImgGif;
        public ProgressBar imageViewSendingStautsTextGif;
        public GifImageView myTextViewContentTextGif;
        //send voice
        public RelativeLayout relativeLayoutToVoice;
        public ImageView imageViewSendingStautsVoiceImg;
        public ProgressBar imageViewSendingStautsVoice;
        public LinearLayout linearLayoutContentVoice;
        public ImageView imageViewPlayIconVoice;
        public ImageView imageViewPlayIconVoiceAnim;
        public TextView textViewPlayTimeToVoice;
        //send image
        public RelativeLayout relativeLayoutToImage;
        public ProgressBar progressbarStatusImage;//progressbar_status
        public RoundImageView imageViewContentImage;
        public ImageView imageViewContentEmotion;
        public TextView textViewSendingProgressImage;
        public ImageView imageViewSendingStautsImage;
        //send card
        public RelativeLayout relativeLayoutToCard;
        public TextView textViewContentCard;
        public TextView textViewAgeCard;
        public ImageView imageViewSexCard;
        public TextView textViewNameCard;
        public ImageView imageViewIconCard;
        public ImageView imageViewSendingStautsCardImg;
        public ProgressBar imageViewSendingStautsCard;
        public ImageView mMyHeadImg;

        public View chatting_send_layout_from_share;
        public ImageView send_share_header;
        public TextView send_share_title;
        public TextView send_share_bellow_title;
        public ImageView chatting_msg_content_send_share;
        public RelativeLayout chatting_send_share_title_row;

        public View sending_status_layout_send_share;
        public ImageView sending_status_text_img_share;
        public ProgressBar sending_status_text_share;
		/*自己发送消息 end*/

    }

    private Handler handler = new Handler();

    /** 获取最后一条数据
     * @return 返回对象
     */
    public void stopPlayAnim(final View view, final View playIcon) {
        Drawable de = view.getBackground();
        // 刚进去聊天界面是没有赋动画的
        if (de instanceof AnimationDrawable) {
            AnimationDrawable playAnim = (AnimationDrawable) de;
            playAnim.stop();
        }

        view.setBackgroundResource(R.drawable.voice_anim01);
        playIcon.setBackgroundResource(R.drawable.chatting_voice_player_icon);
    }

    /** 播放语音时开始动画
     * @param view
     * @param playIcon
     */
    void startPlayAnim(final View view, View playIcon) {
        if (!VoiceManager.getInstance().getView().equals(view)) {
            return;
        }
        playIcon.setBackgroundResource(R.drawable.voice_pause_icon_normal);
/*        handler.post(new Runnable() {
            @Override
            public void run() {*/
                animDrawable = ((AnimationDrawable) mContext.getResources().getDrawable(R.anim.playing_from_voice_frame));
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    view.setBackgroundDrawable(animDrawable);
                }else{
                    view.setBackground(animDrawable);
                }
                if (animDrawable != null && !animDrawable.isRunning()) {
                    animDrawable.setOneShot(false);
                    animDrawable.start();
                }
           // }
     //   });
    }


    /** 长按操作
     * @param context
     * @param arrayId
     * @param afMessageInfo
     * @param v
     * @param position
     */
    private void longClick(final Context context, final int arrayId, final AfMessageInfo afMessageInfo, final View v, final int position) {
        if(isShowMenu)
            return;
        isShowMenu = true;
        final String[] contentAry = context.getResources().getStringArray(arrayId);
        String title = context.getResources().getString(R.string.operate);

        AppDialog appDialog = new AppDialog(context);
        appDialog.createSelectBtnDialog(context, title, contentAry, new OnSelectButtonDialogListener() {
            @Override
            public void onCancelButtonClick(int selectIndex) {
                // 按照这种方式做删除操作，这个if内的代码有bug，实际代码中按需操作
                final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
                if (contentAry[selectIndex].equals(context.getResources().getString(R.string.resend))) {
                    switch (msgType) {
                        case AfMessageInfo.MESSAGE_TEXT:
                            listMsgs.remove(position);
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((GroupChatActivity) context).resendTextOrEmotion(afMessageInfo);
                            break;
                        case AfMessageInfo.MESSAGE_STORE_EMOTIONS:
                            listMsgs.remove(position);
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((GroupChatActivity) context).resendGifImage(afMessageInfo);
                            break;
                        case AfMessageInfo.MESSAGE_VOICE: {
                            listMsgs.remove(position);
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((GroupChatActivity) context).resendVoice(afMessageInfo);
                            break;
                        }
                        case AfMessageInfo.MESSAGE_IMAGE: {
                            listMsgs.remove(position);
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((GroupChatActivity) context).resendImage(afMessageInfo);
                            break;
                        }
                        case AfMessageInfo.MESSAGE_EMOTIONS:
                            //趣味表情
                            listMsgs.remove(position);
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((GroupChatActivity) context).resendDefaultImage(afMessageInfo);
                            break;
                        case AfMessageInfo.MESSAGE_CARD:
                            listMsgs.remove(position);
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((GroupChatActivity) context).resendCard(afMessageInfo);
                            break;
                        case AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG:
                        case AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG:
                            listMsgs.remove(position);
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((GroupChatActivity) context).resendShareBroadcast(afMessageInfo, msgType == AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG);
                            break;
                        default:

                            break;
                    }

                } else if (contentAry[selectIndex].equals(context.getResources().getString(R.string.copy))) {
                    if (v instanceof TextView) {
                        CommonUtils.copy((TextView) v, context);
                    }
                } else if (contentAry[selectIndex].equals(context.getResources().getString(R.string.delete))) {
                    listMsgs.remove(afMessageInfo);
                    notifyDataSetChanged();
                    ((GroupChatActivity) context).delete(afMessageInfo);

                    if (msgType == AfMessageInfo.MESSAGE_VOICE && VoiceManager.getInstance().isPlaying()) {
                        VoiceManager.getInstance().pause();
                    }
                    //forward msg
                } else if (contentAry[selectIndex].equals(context.getResources().getString(R.string.forward))) {
                    PalmchatLogUtils.println("forward received text fid:" + afMessageInfo.fid);
                    final int msgType2 = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
                    switch (msgType2) {
                        case AfMessageInfo.MESSAGE_TEXT:
                        case AfMessageInfo.MESSAGE_CARD:

                            CacheManager.getInstance().setForwardMsg(afMessageInfo);
                            Intent intent = new Intent(mContext, ForwardSelectActivity.class);
                            mContext.startActivity(intent);
                            break;

                        case AfMessageInfo.MESSAGE_IMAGE:
                            CacheManager.getInstance().setForwardMsg(afMessageInfo);
                            AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
                            if (null != afAttachImageInfo) {
                                String largeFilename = afAttachImageInfo.large_file_name;
//						large_file has been downloaded
                                if (!CommonUtils.isEmpty(largeFilename)) {
                                    Intent intent2 = new Intent(mContext, ForwardSelectActivity.class);
                                    mContext.startActivity(intent2);
                                } else {
                                    toImageActivity(afMessageInfo._id, afAttachImageInfo, false, true);
                                }
                            }

                            break;
                    }

                }
            }
        });
        appDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isShowMenu = false;
            }
        });
        appDialog.show();
    }

    /**
     * 长按事件项目
     *
     * @param status
     * @param msgType
     * @param entity
     * @param v
     * @param position
     */
    void showLongClickItemForResend(int status, int msgType, AfMessageInfo entity, View v, int position) {
        longClick(mContext, R.array.chat_resend, entity, v, position);
    }

    /** 长按聊天内容，然后根据聊天内容类型不同，则显示不同的上下文内容
     * @param status 消息状态
     * @param msgType 消息类型
     * @param entity 消息对象
     * @param v 视图控件
     * @param position 消息集合中的位置
     */
    void showLongClickItem(int status, int msgType, AfMessageInfo entity, View v, int position) {
        if (status == AfMessageInfo.MESSAGE_UNSENT) { //消息发送失败
            if (msgType == AfMessageInfo.MESSAGE_TEXT) {
                longClick(mContext, R.array.chat_msg_op_f, entity, v, position);
            } else if (msgType == AfMessageInfo.MESSAGE_EMOTIONS || msgType == AfMessageInfo.MESSAGE_VOICE || msgType == AfMessageInfo.MESSAGE_STORE_EMOTIONS) {
                longClick(mContext, R.array.chat_msg_op2_f, entity, v, position);
            } else if (msgType == AfMessageInfo.MESSAGE_CARD || msgType == AfMessageInfo.MESSAGE_IMAGE) {
                longClick(mContext, R.array.chat_forward_msg_op2_f, entity, v, position);
            } else {
                longClick(mContext, R.array.chat_msg_op2_f, entity, v, position);
            }
        } else {
            if (status == AfMessageInfo.MESSAGE_SENTING) {//sendding
                return;
            }
            if (msgType == AfMessageInfo.MESSAGE_TEXT) {
                longClick(mContext, R.array.chat_msg_op3_f, entity, v, position);
            } else if (msgType == AfMessageInfo.MESSAGE_VOICE
                    || msgType == AfMessageInfo.MESSAGE_EMOTIONS
                    || msgType == AfMessageInfo.MESSAGE_STORE_EMOTIONS
                    || msgType == AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG
                    || msgType == AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG) {
                longClick(mContext, R.array.chat_msg_op32_f, entity, v, position);
            } else if (msgType == AfMessageInfo.MESSAGE_CARD || msgType == AfMessageInfo.MESSAGE_IMAGE) {
                longClick(mContext, R.array.chat_msg_op31_f, entity, v, position);
            }
        }

    }

    /** 跳转到图片页面
     * @param msg_id Image标识
     * @param afAttachImageInfo Imagec对象
     * @param isSent 是否发送
     * @param isForward 是否转发
     */
    private void toImageActivity(int msg_id, AfAttachImageInfo afAttachImageInfo, boolean isSent, boolean isForward) {
        if (null != listMsgs) {
            int size = listMsgs.size();
            int index = 0;
            ArrayList<AfMessageInfo> imgMsgList = new ArrayList<AfMessageInfo>();
            for (int i = 0; i < size; i++) {
                AfMessageInfo msgInfo = listMsgs.get(i);
                final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & msgInfo.type;
                if (msgType == AfMessageInfo.MESSAGE_IMAGE) {
                    imgMsgList.add(msgInfo);
                    if (msgInfo._id == msg_id) {
                        index = imgMsgList.size() - 1;
                    }
                }
            }
            if (!imgMsgList.isEmpty()) {
                new LargeImageDialog(mContext, imgMsgList, index,
                        Consts.FROM_GROUP_CHAT, LargeImageDialog.TYPE_CHAT, isForward)
                        .show();
            }

            if (VoiceManager.getInstance().isPlaying()) {//若语音在播放，则停止
                VoiceManager.getInstance().pause();
            }
        }
    }

    /** 跳转到profile页面
     * @param afFriendInfo  用户对象
     */
    private void toProfile(final AfFriendInfo afFriendInfo) {

        if (afFriendInfo.afId.equals(myProfileInfo.afId)) { //跳转到当前用户的profile
            Bundle bundle = new Bundle();
            bundle.putString(JsonConstant.KEY_AFID, myProfileInfo.afId);
            Intent intent = new Intent(mContext, MyProfileActivity.class);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        } else { //跳转到好友的profile

            // heguiming 2013-12-04
            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.GROUP_T_PF);
//            MobclickAgent.onEvent(mContext, ReadyConfigXML.GROUP_T_PF);

            Intent intent = new Intent(mContext, ProfileActivity.class);
            AfProfileInfo info = AfFriendInfo.friendToProfile(afFriendInfo);
            intent.putExtra(JsonConstant.KEY_PROFILE, info);
            //请求新的profile资料
            intent.putExtra(JsonConstant.KEY_FLAG, true);
            intent.putExtra(JsonConstant.KEY_AFID, info.afId);

            intent.putExtra(JsonConstant.KEY_USER_MSISDN, info.user_msisdn);
            //此处为了统计从哪个页面到好友profile
            intent.putExtra(JsonConstant.KEY_FROM_XX_PROFILE, ProfileActivity.GPCHAT_TO_PROFILE);
            mContext.startActivity(intent);
        }
    }

    /** 跳转到profile页面
     * @param afid
     */
    private void toProfileForAfid(final String afid) {
        if (afid.equals(myProfileInfo.afId)) {
            Bundle bundle = new Bundle();
            bundle.putString(JsonConstant.KEY_AFID, myProfileInfo.afId);
            Intent intent = new Intent(mContext, MyProfileActivity.class);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        } else {
            // heguiming 2013-12-04
            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.GROUP_T_PF);
//            MobclickAgent.onEvent(mContext, ReadyConfigXML.GROUP_T_PF);

            Intent intent = new Intent(mContext, ProfileActivity.class);
            //请求新的profile资料
            intent.putExtra(JsonConstant.KEY_FLAG, true);
            intent.putExtra(JsonConstant.KEY_AFID, afid);
            intent.putExtra(JsonConstant.KEY_FROM_XX_PROFILE, ProfileActivity.GPCHAT_TO_PROFILE);
            mContext.startActivity(intent);
        }
    }

    /** 获取最后一条数据
     * @return 返回对象
     */
    public AfMessageInfo getLastMsg() {
        // TODO Auto-generated method stub
        if (getCount() <= 0) {
            return null;
        }
        return listMsgs.get(getCount() - 1);
    }

    /**
     * 显示时间分隔线
     *
     * @param l_chatting_date   控件封装
     * @param timeTxt getView下标
     */
    private void showTimeLine(LinearLayout l_chatting_date, TextView timeTxt, int position) {
        l_chatting_date.setVisibility(View.GONE);
        timeTxt.setVisibility(View.GONE);
        // 添加时间分隔线
        if (null != listMsgs && !listMsgs.isEmpty()) {
            long clientTime = getItem(position).client_time;
            if (clientTime != 0L) {
                Date date = new Date(clientTime);
                if (position == 0) {
                    showTimeText(l_chatting_date, timeTxt, date);
                } else {
                    long serClientTime = getItem(position - 1).client_time;
                    if (serClientTime != 0L) {
                        // 大于3分钟
                        if (clientTime - serClientTime > THREE_MIN) {
                            showTimeText(l_chatting_date, timeTxt, date);
                        }
                    }
                }
            }
        }
    }

    /** 显示时间
     * @param l_chatting_date 布局
     * @param timeTxt 显示时间TextView
     * @param date 时间
     */
    private void showTimeText(LinearLayout l_chatting_date, TextView timeTxt, Date date) {
        String text = DateUtil.dateChangeStr(date, PalmchatApp.getApplication());
        timeTxt.setText(text);
        l_chatting_date.setVisibility(View.VISIBLE);
        timeTxt.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(text)) {
            l_chatting_date.setVisibility(View.GONE);
            timeTxt.setVisibility(View.GONE);
        }
    }

    /**
     * 退出chatting页面时，需执行的方法
     */
    public void exit() {
//        CacheManager.getInstance().recycleGifDrawble();
    }

    // 统一了一下语音接收和发送
    private void processVoice(AfMessageInfo entity, ImageView voiceIconFrom, ImageView voiceIconFromAnim, ImageView voiceStatusFrom, String path) {
        if (mIsRecording) {
            return;
        }

        if (!CommonUtils.isHasSDCard()) {
            ToastManager.getInstance().show(mContext, R.string.without_sdcard_cannot_play_voice_and_so_on);
            return;
        }

        // 只有接收才有
        if (null != voiceStatusFrom) {
            entity.status = AfMessageInfo.MESSAGE_READ;
            int statusAction = mAfCorePalmchat.AfDbGrpMsgSetStatus(entity._id, entity.status);
            voiceStatusFrom.setVisibility(View.GONE);
        }

        boolean isPlay = VoiceManager.getInstance().isPlaying();
        if (isPlay) {
            VoiceManager.getInstance().pause();

            if (VoiceManager.getInstance().getView().equals(voiceIconFromAnim)) {
                return;
            }
        }

        if (CommonUtils.isEmpty(VoiceManager.getInstance().getPath())) {
            VoiceManager.getInstance().setView(voiceIconFromAnim);
            VoiceManager.getInstance().setPlayIcon(voiceIconFrom);
            startPlayAnim(voiceIconFromAnim, voiceIconFrom);
            VoiceManager.getInstance().setmActivity(mContext);
            VoiceManager.getInstance().play(mContext, entity, path, new VoiceManager.OnCompletionListener() {
                @Override
                public void onCompletion() {
                }

                @Override
                public Object onGetContext() {
                    return GroupChattingAdapter.this;
                }

                @Override
                public void onError() {
                }
            });
        }
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        // TODO Auto-generated method stub
        if (code == Consts.REQ_CODE_SUCCESS) {
                if (null != result && result instanceof String) {
                    String large_file_name = (String) result;

                    if (null != user_data && user_data instanceof String) {
                        FileUtils.copyToImg(large_file_name, (String)user_data);
                        notifyDataSetChanged();
                    }
                }
        } else {
            Consts.getInstance().showToast(mContext, code, flag, http_code);
        }
    }
}