
package com.afmobi.palmchat.ui.activity.chats.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.profile.GiftDetailActivity;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountDetailsActivity;
import com.afmobi.palmchat.ui.activity.social.BroadcastDetailActivity;
import com.afmobi.palmchat.ui.activity.tagpage.TagPageActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnInputDialogListener;
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
import com.core.AfDatingInfo;
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

public class ChattingAdapter extends BaseAdapter implements OnClickListener, AfHttpResultListener {

    /**
     * 相隔3分钟的毫秒级
     */
    public static final long THREE_MIN = 3 * 60 * 1000;

    /**
     * 消息集合
     */
    private ArrayList<AfMessageInfo> listMsgs;

    private Activity mContext;

    private LayoutInflater mInflater;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    //中间件类
    private AfPalmchat mAfCorePalmchat;
    private AfProfileInfo myProfileInfo;
    /**
     * 相隔3分钟的毫秒级
     */
    private String mFriendAfid;

    private StringBuilder path;

    /**
     * 是否已经显示Menu
     */
    boolean isShowMenu;
    /**
     * 正在录音
     */
    private boolean mIsRecording;


    /**
     * 构造方法
     *
     * @param context
     * @param list
     * @param listview
     * @param currentAfid
     */
    public ChattingAdapter(Activity context, ArrayList<AfMessageInfo> list, ListView listview,  String currentAfid) {
        mContext = context;
        this.listMsgs = list;
        mInflater = LayoutInflater.from(context);
        //初始化myProfile
        myProfileInfo = CacheManager.getInstance().getMyProfile();
        mAfCorePalmchat = ((PalmchatApp) mContext.getApplicationContext()).mAfCorePalmchat;
        mFriendAfid = currentAfid;
    }

    public void setIsRecording(boolean isRecording) {
        mIsRecording = isRecording;
    }

    @Override
    public void notifyDataSetChanged() {
        myProfileInfo = CacheManager.getInstance().getMyProfile();
        super.notifyDataSetChanged();
    }

    /**
     * 设置消息集合
     *
     * @param list
     */
    public void setList(ArrayList<AfMessageInfo> list) {
        this.listMsgs = list;
    }

    /**
     * 获取消息集合
     *
     * @return
     */
    public ArrayList<AfMessageInfo> getLists() {
        return listMsgs;
    }


    public int getCount() {
        return listMsgs.size();
    }

    OnItemLongClick onItemLongClick;

    /**
     * 设置点击item事件
     *
     * @param onItemLongClick
     */
    public void setOnItemClick(OnItemLongClick onItemLongClick) {
        this.onItemLongClick = onItemLongClick;
    }

    /**
     * 获取事件
     *
     * @return
     */
    public OnItemLongClick getOnItemClick() {
        return onItemLongClick;
    }


    public AfMessageInfo getItem(int position) {
        return listMsgs.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        final AfMessageInfo entity = listMsgs.get(position);
        //信息类型   AfMessageInfo.MESSAGE_TYPE_MASK & entity.type  (//信息来源  AfMessageInfo.MESSAGE_TYPE_MASK_GRP & entity.type != 0)
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
            if (MessagesUtils.isReceivedMessage(status)) {
                //初始化收到消息布局
                initLeftView(viewHolder, convertView);
            } else {
                //初始化发送消息布局
                initRightView(viewHolder, convertView);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (MessagesUtils.isReceivedMessage(status)) {//收到消息
                if (viewHolder.initLeftViewStub == null) {
                    //初始化收到消息布局
                    initLeftView(viewHolder, convertView);
                }
            } else {
                if (viewHolder.initRightViewStub == null) {
                    //初始化发送消息布局
                    initRightView(viewHolder, convertView);
                }
            }
        }
        if (position < 0) return convertView;

        viewHolder.l_group_system_msg.setVisibility(View.GONE);
        viewHolder.vTextViewGroupSystemText.setVisibility(View.GONE);
        if (MessagesUtils.isReceivedMessage(status)) {//收到消息
            viewHolder.leftViewStub.setVisibility(View.VISIBLE);
            PalmchatLogUtils.println("ChattingAdapter  position  " + position + " getView  " + entity.client_time);
            //点击用户头像事件
            viewHolder.vImageViewChattingPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AfFriendInfo afFriendInfo2 = CacheManager.getInstance().searchAllFriendInfo(entity.fromAfId);
                    if (afFriendInfo2 != null && afFriendInfo2.afId != null && afFriendInfo2.afId.startsWith("r")) {
                        if (!CommonUtils.isSystemAccount(afFriendInfo2.afId)) {
                            toPublicAccountDetail(afFriendInfo2);//进入公共帐号页面
                        }
                    } else if (afFriendInfo2 != null && afFriendInfo2.afId != null) {
                        toProfile(afFriendInfo2);//进入朋友页面
                    }
                }
            });

            viewHolder.vImageViewChattingPhoto.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                 /*   Object obj = entity.attach;
                    if (obj != null && obj instanceof AfFriendInfo) {
                        final AfFriendInfo afFriendInfo = (AfFriendInfo) entity.attach;
                        if (onItemLongClick != null) {
                            onItemLongClick.onItemClick(Chatting.ACTION_LONG_CLICK, afFriendInfo.name, afFriendInfo.afId, afFriendInfo.sex);
                        }
                    } else {*/
                        AfFriendInfo afFriendInfo2 = CacheManager.getInstance().searchAllFriendInfo(entity.fromAfId);
                        if (afFriendInfo2 != null && !afFriendInfo2.afId.startsWith(DefaultValueConstant._R)) {
                            if (onItemLongClick != null) {
                                onItemLongClick.onItemClick(Chatting.ACTION_LONG_CLICK, afFriendInfo2.name, afFriendInfo2.afId, afFriendInfo2.sex);
                            }
                        } else {
                            PalmchatLogUtils.println("vImageViewChattingPhoto  get card failure no name.");
                        }
                    //}
                    return true;
                }
            });

            final ImageView imgIconHead = viewHolder.vImageViewChattingPhoto;
            final AfFriendInfo afF = CacheManager.getInstance().searchAllFriendInfo(entity.fromAfId);
            if (afF != null) {
           /*     if (!CommonUtils.showHeadImage(afF.afId, imgIconHead, afF.sex)) {
                    final String imgUrl = afF.head_img_path;
                    imgIconHead.setTag(imgUrl);*/
                    //调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
                    ImageManager.getInstance().DisplayAvatarImage(imgIconHead, afF.getServerUrl(), afF.getAfidFromHead()
                            , Consts.AF_HEAD_MIDDLE, afF.sex, afF.getSerialFromHead(), null);
//                }
            }
            //显示时间
            viewHolder.vTextViewSendTime.setText(dateFormat.format(new Date(entity.client_time)));//(CommonUtils.getRealChatDate(mContext, System.currentTimeMillis(), entity.client_time));
            switch (msgType) {
                case AfMessageInfo.MESSAGE_TEXT:
              /*  case AfMessageInfo.MESSAGE_FOLLOW: */{
                    viewHolder.myTextViewContentFromText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    viewHolder.myTextViewContentFromText.setCompoundDrawablePadding(0);
                    viewHolder.myTextViewContentFromText.setText(entity.msg);//chatting_msg_content_from_text
                    CharSequence text = EmojiParser.getInstance(mContext).parse(entity.msg);
                    viewHolder.myTextViewContentFromText.setText(text);
                    CommonUtils.setUrlSpanLink(mContext,viewHolder.myTextViewContentFromText);
                    showOrDisappear(viewHolder, status, msgType);
                    viewHolder.myTextViewContentFromText.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showLongClickItem(status, msgType, entity, v, position);
                            return true;
                        }
                    });
                    break;
                }

//			receive flower msg
                case AfMessageInfo.MESSAGE_FLOWER: {
                    String showMsg = mContext.getString(R.string.sent_flower_msg).replace("XXXX", entity.msg);
                    viewHolder.fromFlowerText.setText(showMsg);
                    showOrDisappear(viewHolder, status, msgType);
                    if (CacheManager.getInstance().getFlowerSendingMap().containsKey(entity.fromAfId + entity._id + CacheManager.FLOWER_MSG_SUFFIX)) {
                        viewHolder.fromFlowerSending.setVisibility(View.VISIBLE);
                        viewHolder.fromFlowerSend.setVisibility(View.GONE);
                    } else {
                        viewHolder.fromFlowerSending.setVisibility(View.GONE);
                        viewHolder.fromFlowerSend.setVisibility(View.VISIBLE);
                    }
                    viewHolder.fromFlowerView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(mContext, GiftDetailActivity.class);
                            mContext.startActivity(intent);
                        }
                    });

                    viewHolder.fromFlowerText.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(mContext, GiftDetailActivity.class);
                            mContext.startActivity(intent);
                        }
                    });

                    viewHolder.fromFlowerView.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showLongClickItem(status, msgType, entity, v, position);
                            return true;
                        }
                    });

                    viewHolder.fromFlowerText.setOnLongClickListener(new OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            showLongClickItem(status, msgType, entity, v, position);
                            return true;
                        }
                    });

//					send flower btn
                    viewHolder.fromFlowerSend.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            sendGift(entity, 0);
                        }
                    });
                    break;
                }

                case AfMessageInfo.MESSAGE_STORE_EMOTIONS: {//store动态表情
                    //显示gif动画
                    viewHolder.myTextViewContentFromTextGif.displayGif(mContext, viewHolder.myTextViewContentFromTextGif, entity.msg, null);
                    showOrDisappear(viewHolder, status, msgType);
                    viewHolder.myTextViewContentFromTextGif.setOnLongClickListener(new OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            showLongClickItem(status, msgType, entity, v, position);
                            return true;
                        }
                    });
                    break;
                }
                case AfMessageInfo.MESSAGE_VOICE: {   //语音
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
                                CharSequence text = EmojiParser.getInstance(mContext).parse(afAttachPAMsgInfo.content,  CommonUtils.dip2px(mContext, 13));
                                viewHolder.from_share_bellow_title.setText(text);
                                viewHolder.from_share_bellow_title.setVisibility(View.VISIBLE);
                            }
                        }
                        viewHolder.chatting_from_layout_from_share.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toBroadcastDetailOrTagPage(afAttachPAMsgInfo);
                            }
                        });
                        viewHolder.chatting_msg_content_from_share.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toBroadcastDetailOrTagPage(afAttachPAMsgInfo);
                            }
                        });
               /*         viewHolder.from_share_header.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                             *//*   if(afFChat != null){
                                    toProfile(afFChat);
                                }
                                else{
                                    toProfileForAfid(afAttachPAMsgInfo.afid);
                                }*//*
                                toBroadcastDetailOrTagPage(afAttachPAMsgInfo);
                            }
                        });*/
                      /*  viewHolder.from_share_title.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (afAttachPAMsgInfo.msgtype == AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG) {
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
//                case AfMessageInfo.MESSAGE_EMOTIONS:趣味表情没有了
                case AfMessageInfo.MESSAGE_IMAGE: {   //图片或表情
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
                                    PalmchatLogUtils.println("imageContent  onclick private chat");
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
                    } else */
                    {//图片
                        imageContent.setVisibility(View.VISIBLE);
                        imageContentEmotion.setVisibility(View.GONE);
                        final AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) entity.attach;
                        if (afAttachImageInfo != null) {
                            String path = RequestConstant.IMAGE_CACHE + afAttachImageInfo.small_file_name;
                          /*  ChatImageInfo chatImageInfo = new ChatImageInfo(path, false);
                            chatImageInfo.setWhich_screen(Consts.CHATTING);
                            chatImageInfo.setChattingType(Consts.SHARE_CHATTING);
                            if(isExistsImage(imageContent,path,afAttachImageInfo,chatImageInfo)) { //判断是否存在图片
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

                        if (status == AfMessageInfo.MESSAGE_READ) {
                            viewHolder.textViewChattingFileSizeFromImage.setVisibility(View.GONE);
                        } else {
                            viewHolder.textViewChattingFileSizeFromImage.setVisibility(View.GONE);
                        }


                        viewHolder.imageViewChattingMsgContentFromImage.setOnLongClickListener(new OnLongClickListener() {

                            @Override
                            public boolean onLongClick(View v) {
                                // TODO Auto-generated method stub
                                showLongClickItem(status, msgType, entity, v, position);
                                return true;
                            }
                        });
                    }

                    showOrDisappear(viewHolder, status, msgType);
                    break;
                }
                case AfMessageInfo.MESSAGE_CARD: {//朋友名片信息
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

                        //调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
                        ImageManager.getInstance().DisplayAvatarImage(imgIcon, afFriendInfo.getServerUrl(), afFriendInfo.getAfidFromHead()
                                , Consts.AF_HEAD_MIDDLE, afFriendInfo.sex, afFriendInfo.getSerialFromHead(), null);
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
                            final ImageView imgIcon = viewHolder.imageViewIconFromCard;
                            //调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
                            ImageManager.getInstance().DisplayAvatarImage(imgIcon, afFriendInfo2.getServerUrl(), afFriendInfo2.getAfidFromHead()
                                    , Consts.AF_HEAD_MIDDLE, afFriendInfo2.sex, afFriendInfo2.getSerialFromHead(), null);
                        } else {
                            PalmchatLogUtils.println("from get card failure.");
                            viewHolder.textViewchattingMsgContentFromCard.setText(mContext.getString(R.string.af_id) + ":" + CommonUtils.getCorrectAfid(entity.msg));
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
                            if (entity.msg.equals(CacheManager.getInstance().getMyProfile().afId)) {
                                toMyProfileActivity(entity);
                            } else if (afFriendInfo != null) {
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
                case AfMessageInfo.MESSAGE_FRIEND_REQ:
                case AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS:
                case AfMessageInfo.MESSAGE_FOLLOW:
                {
                    if(msgType == AfMessageInfo.MESSAGE_FOLLOW)
                    {
                        disableFromAndTo(viewHolder);
                    }
                    else {
                        showSystemText(entity, viewHolder);
                    }
                    break;
                }

                default:
                    break;
            }
        } else {//发送消息
            viewHolder.vTextViewSendTime.setText(dateFormat.format(new Date(entity.client_time)));//(CommonUtils.getRealChatDate(mContext, System.currentTimeMillis(), entity.client_time));
            if (null != myProfileInfo) {
                //调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
                ImageManager.getInstance().DisplayAvatarImage(viewHolder.mMyHeadImg, myProfileInfo.getServerUrl(), myProfileInfo.getAfidFromHead()
                        , Consts.AF_HEAD_MIDDLE, myProfileInfo.sex, myProfileInfo.getSerialFromHead(), null);
            }
            viewHolder.mMyHeadImg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mContext != null) {
                        mContext.startActivityForResult(new Intent(mContext, MyProfileActivity.class),IntentConstant.REQUEST_CODE_CHATTING);
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
                    break;
                }
                case AfMessageInfo.MESSAGE_STORE_EMOTIONS: {
                    viewHolder.myTextViewContentTextGif.displayGif(mContext, viewHolder.myTextViewContentTextGif, entity.msg, null);
                    showOrDisappear(viewHolder, status, msgType);

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
                                if(afAttachPAMsgInfo.msgtype == AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG) {
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
//                case AfMessageInfo.MESSAGE_EMOTIONS:趣味表情已经没有了
                case AfMessageInfo.MESSAGE_IMAGE: {
                    final ImageView imageContent = viewHolder.imageViewContentImage;
                    final ImageView imageContentEmotion = viewHolder.imageViewContentEmotion;
                    //趣味表情
                   /* if (msgType == AfMessageInfo.MESSAGE_EMOTIONS) {
                        imageContent.setVisibility(View.GONE);
                        imageContentEmotion.setVisibility(View.VISIBLE);
                        String text = entity.msg;
                        String[] s = text.split(":");
                        String emotionPath = "default/" + s[1] + "L.png";
                        if (!CommonUtils.isEmpty(emotionPath)) {
                            ChatImageInfo chatImageInfo = new ChatImageInfo(emotionPath, true);
                            ImageLoader.getInstance().displayImage(imageContentEmotion, chatImageInfo);
                        }
                        viewHolder.textViewSendingProgressImage.setVisibility(View.GONE);
                        imageContentEmotion.setOnLongClickListener(new OnLongClickListener() {

                            @Override
                            public boolean onLongClick(View v) {
                                showLongClickItem(status, msgType, entity, v, position);
                                return true;
                            }
                        });
                    } else*/
                    {//图片
                        imageContent.setVisibility(View.VISIBLE);
                        imageContentEmotion.setVisibility(View.GONE);
                        final AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) entity.attach;
                        if (afAttachImageInfo != null) {
                            String path = RequestConstant.IMAGE_CACHE + afAttachImageInfo.small_file_name;

                            ImageManager.getInstance().DisplayChatImage(imageContent,path,null,R.color.white,R.drawable.ic_loadfailed, Constants.CHATIMAGE_SIZE);

                            viewHolder.textViewSendingProgressImage.setText(afAttachImageInfo.progress + "%");
                            if (AfMessageInfo.MESSAGE_SENT == status || AfMessageInfo.MESSAGE_SENT_AND_READ == status) {
                                viewHolder.textViewSendingProgressImage.setVisibility(View.GONE);
                            } else {
                                viewHolder.textViewSendingProgressImage.setVisibility(View.VISIBLE);
                            }
                            imageContent.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    toImageActivity(entity._id, afAttachImageInfo, true, false);
                                }

                            });
                        }

                    }
                    /*if (msgType == AfMessageInfo.MESSAGE_EMOTIONS) {
                        if (AfMessageInfo.MESSAGE_SENTING == status) {
                            showProgressBar(viewHolder.imageViewSendingStautsImage, viewHolder.progressbarStatusImage);
                        } else if (AfMessageInfo.MESSAGE_UNSENT == status) {
                            disappearProgressBar(viewHolder.imageViewSendingStautsImage, viewHolder.progressbarStatusImage);

                            showSendFail(viewHolder.imageViewSendingStautsImage, status, msgType, entity, position);
                        } else if (AfMessageInfo.MESSAGE_SENT == status) {
                            disappearProgressBar(viewHolder.imageViewSendingStautsImage, viewHolder.progressbarStatusImage);
                            showSendSuccessed(viewHolder.imageViewSendingStautsImage);
                        } else {
                            disappearProgressBar(viewHolder.imageViewSendingStautsImage, viewHolder.progressbarStatusImage);
                            showSendAndReadSuccessed(viewHolder.imageViewSendingStautsImage, msgType);
                        }
                        //sending(entity.status ,viewHolder.imageViewSendingStautsImage);
                    } else */
                    {
                        AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) entity.attach;
                        if (afAttachImageInfo != null) {
                            showProgressBarOrNot(position, entity, msgType, status, viewHolder.imageViewSendingStautsImage, viewHolder.progressbarStatusImage);
                        }
                    }
                    showOrDisappear(viewHolder, status, msgType);

                    viewHolder.imageViewContentImage.setOnLongClickListener(new OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
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
                        viewHolder.textViewContentCard.setText(mContext.getString(R.string.af_id) + ":" + CommonUtils.getCorrectAfid(afFriendInfo.afId));
                        showProgressBarOrNot(position, entity, msgType, status, viewHolder.imageViewSendingStautsCardImg, viewHolder.imageViewSendingStautsCard);

                        final ImageView imgIcon = viewHolder.imageViewIconCard;
                        //调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
                        ImageManager.getInstance().DisplayAvatarImage(imgIcon, afFriendInfo.getServerUrl(), afFriendInfo.getAfidFromHead()
                                , Consts.AF_HEAD_MIDDLE, afFriendInfo.sex, afFriendInfo.getSerialFromHead(), null);
                    } else {
                        final ImageView imgIcon = viewHolder.imageViewIconCard;
                        final AfFriendInfo afFriendInfo2 = CacheManager.getInstance().searchAllFriendInfo(entity.msg);
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
                            viewHolder.textViewContentCard.setText(mContext.getString(R.string.af_id) + ":" + CommonUtils.getCorrectAfid(afFriendInfo2.afId));
                            showProgressBarOrNot(position, entity, msgType, status, viewHolder.imageViewSendingStautsCardImg, viewHolder.imageViewSendingStautsCard);

                            //调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
                            ImageManager.getInstance().DisplayAvatarImage(imgIcon, afFriendInfo2.getServerUrl(), afFriendInfo2.getAfidFromHead()
                                    , Consts.AF_HEAD_MIDDLE, afFriendInfo2.sex, afFriendInfo2.getSerialFromHead(), null);
                        } else {
                            PalmchatLogUtils.println("get card failure.");
                            viewHolder.textViewNameCard.setText(mContext.getString(R.string.af_id) + ":" + CommonUtils.getCorrectAfid(entity.msg));

                        }
                    }
                    showOrDisappear(viewHolder, status, msgType);

                    viewHolder.relativeLayoutToCard.setOnLongClickListener(new View.OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            showLongClickItem(status, msgType, entity, v, position);
                            return true;
                        }
                    });
                    viewHolder.relativeLayoutToCard.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (entity.msg.equals(CacheManager.getInstance().getMyProfile().afId)) {
                                toMyProfileActivity(entity);
                            } else if (afFriendInfo != null) {
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
            if(position == 0 && getCount() == 0) {
                viewHolder.vTextViewSendTime.setVisibility(View.GONE);
            }
            else{
                if(position == 0 && getCount() > 1) {
                    showTimeLine(viewHolder.l_chatting_date, viewHolder.vTextViewSendTime, position);
                }
            }
            viewHolder.l_group_system_msg.setVisibility(View.GONE);
            viewHolder.vTextViewGroupSystemText.setVisibility(View.GONE);
            return convertView;
        }
        showTimeLine(viewHolder.l_chatting_date, viewHolder.vTextViewSendTime, position);
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

    /**
     * 初始化发送消息布局
     *
     * @param viewHolder
     * @param convertView
     */
    private void initLeftView(ViewHolder viewHolder, View convertView) {
        //加载布局
        viewHolder.initLeftViewStub = viewHolder.leftViewStub.inflate();
        viewHolder.leftViewStub.setVisibility(View.VISIBLE);
        //photo(head) image
        viewHolder.vImageViewChattingPhoto = (ImageView) convertView.findViewById(R.id.chatting_photo);
        viewHolder.relativeLayoutFrom = (RelativeLayout) convertView.findViewById(R.id.chatting_from_layout_from);
        //receive text
        viewHolder.relativeLayoutFromText = (RelativeLayout) convertView.findViewById(R.id.chatting_from_layout_from_text);//chatting_from_layout_from_text
        viewHolder.myTextViewContentFromText = (MyTextView) convertView.findViewById(R.id.chatting_msg_content_from_text);//chatting_msg_content_from_text
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

    	    /*收到送花消息*/
        viewHolder.fromFlowerView = convertView.findViewById(R.id.chatting_from_layout_from_flower);
        viewHolder.fromFlowerText = (MyTextView) convertView.findViewById(R.id.chatting_msg_content_from_text_flower);
        viewHolder.fromFlowerSend = (TextView) convertView.findViewById(R.id.chatting_from_layout_send_flower);
        viewHolder.fromFlowerSending = convertView.findViewById(R.id.chatting_from_layout_sending_flower);

    	    /*收到送花消息 end*/
        viewHolder.chatting_from_layout_from_share = convertView.findViewById(R.id.chatting_from_layout_from_share);
        viewHolder.from_share_header = (ImageView) convertView.findViewById(R.id.from_share_header);
        viewHolder.from_share_title = (TextView) convertView.findViewById(R.id.from_share_title);
        viewHolder.from_share_bellow_title = (TextView) convertView.findViewById(R.id.from_share_bellow_title);
        viewHolder.chatting_msg_content_from_share = (ImageView) convertView.findViewById(R.id.chatting_msg_content_from_share);
    		/*收到消息end*/
    }

    /**
     * 初始化发送消息布局
     *
     * @param viewHolder
     * @param convertView
     */
    private void initRightView(ViewHolder viewHolder, View convertView) {
  /*自己发送消息给别人 */
        //加载布局
        viewHolder.initRightViewStub = viewHolder.rightViewStub.inflate();
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
        viewHolder.linearLayoutMsgVoice = (LinearLayout) convertView.findViewById(R.id.chatting_msg_voice);
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
        viewHolder.send_share_bellow_title = (MyTextView) convertView.findViewById(R.id.send_share_bellow_title);
        viewHolder.chatting_msg_content_send_share = (ImageView) convertView.findViewById(R.id.chatting_msg_content_send_share);
        viewHolder.sending_status_layout_send_share= convertView.findViewById(R.id.sending_status_layout_send_share);
        viewHolder.sending_status_text_img_share= (ImageView) convertView.findViewById(R.id.sending_status_text_img_share);
        viewHolder.sending_status_text_share = (ProgressBar) convertView.findViewById(R.id.sending_status_text_share);
        viewHolder.send_share_top_row = (RelativeLayout) convertView.findViewById(R.id.send_share_top_row);
    }

    /**
     * 设置播放语音动画
     *
     * @param entity
     * @param voiceIconFrom
     * @param voiceIconFromAnim
     * @param afAttachVoiceInfo
     * @return
     */
    private String showStopAnim(final AfMessageInfo entity, final ImageView voiceIconFrom, final ImageView voiceIconFromAnim, final AfAttachVoiceInfo afAttachVoiceInfo) {
        final String path = RequestConstant.VOICE_CACHE + afAttachVoiceInfo.file_name;
        if (!path.equals(VoiceManager.getInstance().getPath()) && VoiceManager.getInstance().getPlayer() != null && VoiceManager.getInstance().getPlayer().isPlaying()) {//如果正在播放并且不是当前语音，则停止动画
            stopPlayAnim(voiceIconFromAnim, voiceIconFrom);
        } else if (path.equals(VoiceManager.getInstance().getPath()) && VoiceManager.getInstance().getPlayer() != null && VoiceManager.getInstance().getPlayer().isPlaying()) {//如果播放的是当前录音，则开始动画
            //需要重新赋值，否则在播放一个语音的同时又接收到一个语音，此时再点击播放新语音时，会出现第一个的播放动画未停止
            VoiceManager.getInstance().setView(voiceIconFromAnim);
            VoiceManager.getInstance().setPlayIcon(voiceIconFrom);
            startPlayAnim(voiceIconFromAnim, voiceIconFrom);
        } else {//停止动画
            stopPlayAnim(voiceIconFromAnim, voiceIconFrom);
        }
        return path;
    }


    /**
     * 跳转到好友页面
     *
     * @param entity
     */
    private void toMyProfileActivity(
            final AfMessageInfo entity) {
        Bundle bundle = new Bundle();
        bundle.putString(JsonConstant.KEY_AFID, entity.msg);
        Intent intent = new Intent(mContext, MyProfileActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 显示发送消息的进度条
     *
     * @param position
     * @param entity
     * @param msgType
     * @param status
     * @param img
     * @param progressBar
     */
    private void showProgressBarOrNot(final int position, final AfMessageInfo entity, final int msgType, final int status, ImageView img, ProgressBar progressBar) {
        if (AfMessageInfo.MESSAGE_SENTING == status) { //显示正在发送的进度条
            PalmchatLogUtils.println("position  " + position + "TIME  sending  " + dateFormat.format(new Date()) + "  ");
            showProgressBar(img, progressBar);
        } else if (AfMessageInfo.MESSAGE_UNSENT == status) {//发送失败
            PalmchatLogUtils.println("position  " + position + "TIME  fail  " + dateFormat.format(new Date()) + "  ");
            disappearProgressBar(img, progressBar);//隐藏进度条
            showSendFail(img, status, msgType, entity, position);//显示发送错误
        } else if (AfMessageInfo.MESSAGE_SENT == status) {//发送成功
            PalmchatLogUtils.println("position  " + position + "TIME  success  " + dateFormat.format(new Date()) + "  ");
            disappearProgressBar(img, progressBar);
            showSendSuccessed(img);
        } else {//send and already read
            disappearProgressBar(img, progressBar);
            showSendAndReadSuccessed(img, msgType);
        }
    }


    /**
     * 显示进度条
     *
     * @param imageViewSendingStautsImage
     * @param progressbarStatusImage
     */
    private void showProgressBar(ImageView imageViewSendingStautsImage,
                                 ProgressBar progressbarStatusImage) {
        imageViewSendingStautsImage.setVisibility(View.GONE);
        progressbarStatusImage.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏进度条
     *
     * @param imageViewSendingStautsImage
     * @param progressbarStatusImage
     */
    private void disappearProgressBar(ImageView imageViewSendingStautsImage,
                                      ProgressBar progressbarStatusImage) {
        imageViewSendingStautsImage.setVisibility(View.VISIBLE);
        progressbarStatusImage.setVisibility(View.GONE);
    }

    /**
     * 根据消息类型显示或隐藏布局
     *
     * @param viewHolder
     * @param status
     * @param msgType
     */
    private void showOrDisappear(ViewHolder viewHolder, int status, int msgType) {
        if (MessagesUtils.isReceivedMessage(status)) {//收到
            if (viewHolder.relativeLayoutFrom != null) {
                viewHolder.relativeLayoutFrom.setVisibility(View.VISIBLE);
            }
            if (viewHolder.relativeLayoutTo != null) {
                viewHolder.relativeLayoutTo.setVisibility(View.GONE);
            }
            viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
            switch (msgType) {
                case AfMessageInfo.MESSAGE_TEXT:
                case AfMessageInfo.MESSAGE_FOLLOW: {
                    viewHolder.relativeLayoutFromText.setVisibility(View.VISIBLE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.GONE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
                    viewHolder.fromFlowerView.setVisibility(View.GONE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);
                    break;
                }


                case AfMessageInfo.MESSAGE_FLOWER: {
                    viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.GONE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
                    viewHolder.fromFlowerView.setVisibility(View.VISIBLE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);

                    break;
                }


                case AfMessageInfo.MESSAGE_STORE_EMOTIONS: {
                    viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.VISIBLE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.GONE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
                    viewHolder.fromFlowerView.setVisibility(View.GONE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);
                    break;
                }
                case AfMessageInfo.MESSAGE_VOICE: {
                    viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.VISIBLE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.GONE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
                    viewHolder.fromFlowerView.setVisibility(View.GONE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);
                    break;
                }
                case AfMessageInfo.MESSAGE_EMOTIONS:
                case AfMessageInfo.MESSAGE_IMAGE: {
                    viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.VISIBLE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.GONE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
                    viewHolder.fromFlowerView.setVisibility(View.GONE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);
                    break;
                }
                case AfMessageInfo.MESSAGE_CARD: {
                    viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.VISIBLE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
                    viewHolder.fromFlowerView.setVisibility(View.GONE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);
                    break;
                }
                case AfMessageInfo.MESSAGE_FRIEND_REQ: {
                    viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.GONE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.VISIBLE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);
                    if (viewHolder.relativeLayoutFrom != null) {
                        viewHolder.relativeLayoutFrom.setVisibility(View.GONE);//接收消息大布局
                    }
                    if (viewHolder.relativeLayoutTo != null) {
                        viewHolder.relativeLayoutTo.setVisibility(View.GONE);  //发送消息大布局
                    }
                    viewHolder.fromFlowerView.setVisibility(View.GONE);
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
        } else {
            if (viewHolder.relativeLayoutTo != null) {
                viewHolder.relativeLayoutTo.setVisibility(View.VISIBLE);
            }
            if (viewHolder.relativeLayoutFrom != null) {
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


    /**
     * 显示系统消息
     *
     * @param entity
     * @param viewHolder
     */
    private void showSystemText(final AfMessageInfo entity,
                                ViewHolder viewHolder) {
        viewHolder.vTextViewSendTime.setText(dateFormat.format(new Date(entity.client_time)));
        viewHolder.l_group_system_msg.setVisibility(View.VISIBLE);
        viewHolder.vTextViewGroupSystemText.setVisibility(View.VISIBLE);
        viewHolder.vTextViewGroupSystemText.setText(entity.msg);
        disableFromAndTo(viewHolder);
    }


    /**
     * 隐藏接收和发送布局
     *
     * @param viewHolder
     */
    private void disableFromAndTo(ViewHolder viewHolder) {
        if (viewHolder.relativeLayoutFrom != null) {
            viewHolder.relativeLayoutFrom.setVisibility(View.GONE);
        }
        if (viewHolder.relativeLayoutTo != null) {
            viewHolder.relativeLayoutTo.setVisibility(View.GONE);
        }
    }

    private AnimationDrawable animDrawable;

    /**
     * 显示发送成功标识
     *
     * @param view
     */
    private void showSendSuccessed(final View view) {
        view.setVisibility(View.VISIBLE);
        view.setBackgroundResource(R.drawable.msg_send_delivered);
    }

    /**
     * 显示发送成功并被阅读标识
     *
     * @param view
     * @param msgType
     */
    private void showSendAndReadSuccessed(final View view, final int msgType) {
        if (!CommonUtils.isEmpty(mFriendAfid) && mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS)) {//palmchat team
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        view.setBackgroundResource(R.drawable.chatings_sent_icon);
    }

    /**
     * 显示发送失败标识
     *
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

    /**
     * 缓存类
     */
    class ViewHolder {

        public ViewStub leftViewStub;
        public ViewStub rightViewStub;
        public View initLeftViewStub;
        public View initRightViewStub;
        //    	receive flower msg
        public View fromFlowerView;
        public MyTextView fromFlowerText;
        public TextView fromFlowerSend;
        public View fromFlowerSending;

        //消息时间
        public LinearLayout l_chatting_date;
        public RelativeLayout l_group_system_msg;
        public TextView vTextViewSendTime;
        public TextView vTextViewGroupSystemText;//textview_group_system_text
        //公用头像(除收到的好友请求消息imageViewFromRecommendIcon/imageViewFromRecommendPhoto外)
        public ImageView vImageViewChattingPhoto;//chatting_photo
        public int position;

        /*收到消息begin*/
        public RelativeLayout relativeLayoutFrom;//chatting_from_layout_from
        //receive text
        public RelativeLayout relativeLayoutFromText;//chatting_from_layout_from_text
        public MyTextView myTextViewContentFromText;//chatting_msg_content_from_text
        //receive gif
        public RelativeLayout relativeLayoutFromTextGif;//chatting_from_layout_from_text
        public GifImageView myTextViewContentFromTextGif;//chatting_msg_content_from_text
        //receive voice
        public RelativeLayout relativeLayoutFromVoice;//chatting_from_layout_from_voice
        public TextView textViewChattingPlayTimeFromVoice;//chatting_play_time_from_voice
        public ImageView imageViewChattingPlayIconFromVoice;//chatting_play_icon_from_voice
        public ImageView imageViewChattingPlayIconFromVoiceAnim;//chatting_play_icon_from_voice_anim
        public ImageView imageViewSendingStatusFromVoice;//sending_status_from_voice
        public LinearLayout linearLayoutContentfromVoice;
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

        public View chatting_from_layout_from_share;
        public ImageView from_share_header;
        public TextView from_share_title;
        public TextView from_share_bellow_title;
        public ImageView chatting_msg_content_from_share;
        /*收到好友消息 end*/

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
        public LinearLayout linearLayoutMsgVoice;
        public ImageView imageViewPlayIconVoice;
        public ImageView imageViewPlayIconVoiceAnim;
        public TextView textViewPlayTimeToVoice;
        //send image
        public RelativeLayout relativeLayoutToImage;
        public ProgressBar progressbarStatusImage;//progressbar_status
        public RoundImageView imageViewContentImage;
        public ImageView imageViewContentEmotion;//chatting_msg_content_image_emotion
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
        public MyTextView send_share_bellow_title;
        public ImageView chatting_msg_content_send_share;
        public RelativeLayout chatting_send_share_title_row;
        public View sending_status_layout_send_share;
        public ImageView sending_status_text_img_share;
        public ProgressBar sending_status_text_share;
        public RelativeLayout send_share_top_row;
		/*自己发送消息 end*/

    }

    private Handler handler = new Handler();

    /**
     * 停止动画
     *
     * @param view
     * @param playIcon
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

    /**
     * 播放动画
     *
     * @param view
     * @param playIcon
     */
    void startPlayAnim(final View view, final View playIcon) {
        if (!VoiceManager.getInstance().getView().equals(view)) {
            return;
        }
        playIcon.setBackgroundResource(R.drawable.voice_pause_icon);
      /*  handler.post(new Runnable() {
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
                    PalmchatLogUtils.i("startPlayAnim",String.valueOf(view.hashCode()));
                }
           // }
       // });
    }

    /**
     * 长按事件
     *
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
        AppDialog appDialog = new AppDialog(context);
        String title = context.getResources().getString(R.string.operate);
        appDialog.createSelectBtnDialog(context, title, contentAry, new OnSelectButtonDialogListener() {
            @Override
            public void onCancelButtonClick(int selectIndex) {
                final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
                if (contentAry[selectIndex].equals(context.getResources().getString(R.string.resend))) {
                    // 按照这种方式做删除操作，这个if内的代码有bug，实际代码中按需操作
                    switch (msgType) {
                        case AfMessageInfo.MESSAGE_TEXT:
                            if(listMsgs.size() > position) {
                                listMsgs.remove(position);
                            }
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((Chatting) context).resendTextOrEmotion(afMessageInfo);
                            break;
                        case AfMessageInfo.MESSAGE_STORE_EMOTIONS:
                            if(listMsgs.size() > position) {
                                listMsgs.remove(position);
                            }
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((Chatting) context).resendGifImage(afMessageInfo);
                            break;
                        case AfMessageInfo.MESSAGE_VOICE: {
                            if(listMsgs.size() > position) {
                                listMsgs.remove(position);
                            }
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((Chatting) context).resendVoice(afMessageInfo);
                            break;
                        }
                        case AfMessageInfo.MESSAGE_IMAGE: {
                            if(listMsgs.size() > position) {
                                listMsgs.remove(position);
                            }
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((Chatting) context).resendImage(afMessageInfo);
                            break;
                        }
                        case AfMessageInfo.MESSAGE_EMOTIONS:
                            //趣味表情
                            if(listMsgs.size() > position) {
                                listMsgs.remove(position);
                            }
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((Chatting) context).resendDefaultImage(afMessageInfo);
                            break;
                        case AfMessageInfo.MESSAGE_CARD:
                            if(listMsgs.size() > position) {
                                listMsgs.remove(position);
                            }
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((Chatting) context).resendCard(afMessageInfo);
                            break;
                        case AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG:
                        case AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG:
                            if(listMsgs.size() > position) {
                                listMsgs.remove(position);
                            }
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((Chatting) context).resendShareBroadcast(afMessageInfo,msgType == AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG);
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
                    ((Chatting) context).delete(afMessageInfo);

                    if (msgType == AfMessageInfo.MESSAGE_VOICE && VoiceManager.getInstance().isPlaying()) {
                        VoiceManager.getInstance().pause();
                    }

//					forward msg
                } else if (contentAry[selectIndex].equals(context.getResources().getString(R.string.forward))) {

                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.F_NUM);
//                    MobclickAgent.onEvent(context, ReadyConfigXML.F_NUM);

                    PalmchatLogUtils.println("forward received text fid:" + afMessageInfo.fid);
                    final int msgType2 = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
                    switch (msgType2) {
                        case AfMessageInfo.MESSAGE_TEXT:
                        case AfMessageInfo.MESSAGE_CARD:

                            CacheManager.getInstance().setForwardMsg(afMessageInfo);
                            Intent intent = new Intent(context, ForwardSelectActivity.class);
                            context.startActivity(intent);
                            break;

                        case AfMessageInfo.MESSAGE_IMAGE:
                            CacheManager.getInstance().setForwardMsg(afMessageInfo);
                            AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
                            if (null != afAttachImageInfo) {
                                String largeFilename = afAttachImageInfo.large_file_name;
//							large_file has been downloaded
                                if (!CommonUtils.isEmpty(largeFilename)) {
                                    Intent intent2 = new Intent(context, ForwardSelectActivity.class);
                                    context.startActivity(intent2);
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
    /**
     * 长按事件项目
     *
     * @param status
     * @param msgType
     * @param entity
     * @param v
     * @param position
     */
    void showLongClickItem(int status, int msgType, AfMessageInfo entity, View v, int position) {

//		palmchat team
        if (entity.getKey().startsWith("r")) {
            if (status == AfMessageInfo.MESSAGE_UNSENT) {
                if (msgType == AfMessageInfo.MESSAGE_TEXT) {
                    longClick(mContext, R.array.chat_msg_op, entity, v, position);
                } else if (msgType == AfMessageInfo.MESSAGE_VOICE || msgType == AfMessageInfo.MESSAGE_EMOTIONS) {
                    longClick(mContext, R.array.chat_msg_op2, entity, v, position);
                } else if (msgType == AfMessageInfo.MESSAGE_IMAGE || msgType == AfMessageInfo.MESSAGE_CARD) {
                    longClick(mContext, R.array.chat_forward_msg_op2, entity, v, position);
                } else {
                    longClick(mContext, R.array.chat_msg_op2, entity, v, position);
                }
            } else {
                if (status == AfMessageInfo.MESSAGE_SENTING) { //sendding
                    return;
                }
                if (msgType == AfMessageInfo.MESSAGE_TEXT) {
                    longClick(mContext, R.array.chat_msg_op3, entity, v, position);

                } else if (msgType == AfMessageInfo.MESSAGE_VOICE) {
                    longClick(mContext, R.array.chat_msg_op31, entity, v, position);
                } else if (msgType == AfMessageInfo.MESSAGE_IMAGE || msgType == AfMessageInfo.MESSAGE_EMOTIONS) {
                    longClick(mContext, R.array.chat_msg_op31, entity, v, position);
                } else if (msgType == AfMessageInfo.MESSAGE_CARD) {
                    longClick(mContext, R.array.chat_msg_op31, entity, v, position);
                }
            }
        } else {
            if (status == AfMessageInfo.MESSAGE_UNSENT) {
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

                } else if (msgType == AfMessageInfo.MESSAGE_VOICE ||
                        msgType == AfMessageInfo.MESSAGE_EMOTIONS ||
                        msgType == AfMessageInfo.MESSAGE_STORE_EMOTIONS ||
                        msgType == AfMessageInfo.MESSAGE_FOLLOW ||
                        msgType == AfMessageInfo.MESSAGE_FLOWER ||
                        msgType == AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG ||
                        msgType == AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG) {
                    longClick(mContext, R.array.chat_msg_op32_f, entity, v, position);
                } else if (msgType == AfMessageInfo.MESSAGE_CARD || msgType == AfMessageInfo.MESSAGE_IMAGE) {
                    longClick(mContext, R.array.chat_msg_op31_f, entity, v, position);
                }
            }
        }


    }

    /**
     * 跳转到浏览图片页面
     *
     * @param msg_id
     * @param afAttachImageInfo
     * @param isSent
     * @param isForward
     */
    private void toImageActivity(int msg_id, AfAttachImageInfo afAttachImageInfo, boolean isSent, boolean isForward) {
        if (null != listMsgs) {
            int size = listMsgs.size();
            int index = 0;//当前点击图片的索引
            ArrayList<AfMessageInfo> imgMsgList = new ArrayList<AfMessageInfo>();
            for (int i = 0; i < size; i++) { //取得图片的总数
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
                        Consts.FROM_CHATTING, LargeImageDialog.TYPE_CHAT, isForward)
                        .show();
            }

            if (VoiceManager.getInstance().isPlaying()) {//停止播放语音
                VoiceManager.getInstance().pause();
            }
        }

    }

    /**
     * 跳转到profile页面
     *
     * @param afFriendInfo
     */
    private void toProfile(final AfFriendInfo afFriendInfo) {
        if(afFriendInfo.afId.equals(myProfileInfo.afId)){
            toMyProfile();
            return;
        }
        Intent intent = new Intent(mContext, ProfileActivity.class);
        AfProfileInfo info = AfFriendInfo.friendToProfile(afFriendInfo);
        intent.putExtra(JsonConstant.KEY_PROFILE, info);
        //请求新的profile资料
        intent.putExtra(JsonConstant.KEY_FLAG, true);
        intent.putExtra(JsonConstant.KEY_AFID, info.afId);
        intent.putExtra(JsonConstant.KEY_USER_MSISDN, info.user_msisdn);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(JsonConstant.KEY_FROM_XX_PROFILE, ProfileActivity.CHAT_TO_PROFILE);
        mContext.startActivityForResult(intent,1001);
    }

    /**
     * 通过afid跳转到profile页面
     *
     * @param afid
     */
    private void toProfileForAfid(final String afid) {
        if(afid.equals(myProfileInfo.afId)){
            toMyProfile();
            return;
        }
        Intent intent = new Intent(mContext, ProfileActivity.class);
        //请求新的profile资料
        intent.putExtra(JsonConstant.KEY_FLAG, true);
        intent.putExtra(JsonConstant.KEY_AFID, afid);
        intent.putExtra(JsonConstant.KEY_FROM_XX_PROFILE, ProfileActivity.CHAT_TO_PROFILE);
        mContext.startActivityForResult(intent,1001);
    }

    private void toMyProfile() {
        // TODO Auto-generated method stub
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_MMPF);
        mContext.startActivity(new Intent(mContext, MyProfileActivity.class));

    }

    /**
     * 获取最新一条数据
     *
     * @return
     */
    public AfMessageInfo getLastMsg() {
        if (getCount() <= 0) {
            return null;
        }
        return listMsgs.get(getCount() - 1);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 显示时间分隔线
     *
     * @param l_chatting_date
     * @param timeTxt         控件封装
     * @param position        getView下标
     */
    private void showTimeLine(LinearLayout l_chatting_date, TextView timeTxt, int position) {
        l_chatting_date.setVisibility(View.GONE);
        timeTxt.setVisibility(View.GONE);
        // 添加时间分隔线
        if (null != listMsgs && !listMsgs.isEmpty()) {
            long clientTime = getItem(position).client_time;
            if (clientTime != 0L) {
                Date date = new Date(clientTime);
                if (position == 0) {//第一个位置显示时间
                    showTimeText(l_chatting_date, timeTxt, date);
                } else {
                    long serClientTime = getItem(position - 1).client_time;
                    if (serClientTime != 0L) {
                        // 判断当前消息与上条消息的时间是否相隔大于3分钟，若大于则显示时间分隔线
                        if (clientTime - serClientTime > THREE_MIN) {
                            showTimeText(l_chatting_date, timeTxt, date);
                        }
                    }
                }
            }
        }
    }

    

    /**
     * 跳转到公共帐号详情页面
     *
     * @param afFriendInfo
     */
    private void toPublicAccountDetail(
            final AfFriendInfo afFriendInfo) {
        Intent intent = new Intent(mContext, PublicAccountDetailsActivity.class);
        intent.putExtra("Info", afFriendInfo);
        mContext.startActivity(intent);
    }

    /**
     * 设置时间
     *
     * @param l_chatting_date
     * @param timeTxt
     * @param date
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
     * 向好友送花
     *
     * @param afMessageInfo
     * @param number
     */
    private void sendGift(final AfMessageInfo afMessageInfo, final int number) {
        // TODO Auto-generated method stub
        if (mContext instanceof Chatting) {
            Chatting chatting = (Chatting) mContext;
            chatting.closeEmotions();
        }
        final AppDialog appDialog = new AppDialog(mContext);

        appDialog.createSendGiftDialog(mContext, mContext.getString(R.string.send_gift), null, number, new OnInputDialogListener() {
            @Override
            public void onRightButtonClick(String inputStr) {
                if (!TextUtils.isEmpty(inputStr)) {
                    int num = Integer.parseInt(inputStr);
//					int allFlower = CacheManager.getInstance().getMyProfile().dating.wealth_flower;
//					int left = allFlower - num;
                    if (num > 0) {
                        appDialog.dismiss();
//						showProgressDialog(R.string.please_wait);
//						update_sendGift_clickable();
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PNSD_FL);
//                        MobclickAgent.onEvent(mContext, ReadyConfigXML.PNSD_FL);
                        String key = afMessageInfo.fromAfId + afMessageInfo._id + CacheManager.FLOWER_MSG_SUFFIX;
                        CacheManager.getInstance().getFlowerSendingMap().put(key, key);
                        notifyDataSetChanged();
                          PalmchatApp.getApplication().mAfCorePalmchat.AfHttpSendGift(afMessageInfo.fromAfId, num, System.currentTimeMillis(), key, ChattingAdapter.this);

                    } else {
                        ToastManager.getInstance().show(mContext, R.string.input_numer_flowers);
                    }
                } else {
                    ToastManager.getInstance().show(mContext, R.string.input_numer_flowers);
                }
            }

            @Override
            public void onLeftButtonClick() {

            }
        });

        appDialog.show();

    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        // TODO Auto-generated method stub
        if (code == Consts.REQ_CODE_SUCCESS) {
            if (flag == Consts.REQ_SEND_GIFT) {  
//				dismissProgressDialog();
                AfDatingInfo[] afDatingInfos = (AfDatingInfo[]) result;
                if (afDatingInfos != null && afDatingInfos.length > 1) {
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PFSD_FL_SUCC);
//                    MobclickAgent.onEvent(mContext, ReadyConfigXML.PFSD_FL_SUCC);

                    AfDatingInfo myDatingInfo = afDatingInfos[0];
//					AfDatingInfo otherDatingInfo = afDatingInfos[1];

                    ToastManager.getInstance().show(mContext, R.string.success);
                    AfProfileInfo myProfileInfo = CacheManager.getInstance().getMyProfile();
                    myProfileInfo.dating = myDatingInfo;

                    if (user_data != null && user_data instanceof String) {
                        String key = (String) user_data;
                        CacheManager.getInstance().getFlowerSendingMap().remove(key);
                        notifyDataSetChanged();
                    }
                    if (mContext!=null&&mContext instanceof Chatting) {
                        Chatting chatting = (Chatting) mContext;
                        chatting.ShowSendFlowerAnim(null);//播放送花动画
                    }
                } else {
                    ToastManager.getInstance().show(mContext, R.string.failure);
                }

            }
            else{
                if (null != result && result instanceof String) {
                    String large_file_name = (String) result;

                    if (null != user_data && user_data instanceof String) {
                        FileUtils.copyToImg(large_file_name, (String)user_data);
                        notifyDataSetChanged();
                    }
                }
            }

        } else {
            if (flag == Consts.REQ_SEND_GIFT) {
                 
                if (user_data != null && user_data instanceof String) {
                    String key = (String) user_data;
                    CacheManager.getInstance().getFlowerSendingMap().remove(key);
                    notifyDataSetChanged();
                }
            }

            Consts.getInstance().showToast(mContext, code, flag, http_code);
        }
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
            int statusAction = mAfCorePalmchat.AfDbMsgSetStatus(entity._id, entity.status);
            voiceStatusFrom.setVisibility(View.GONE);
        }
  /*      View view = VoiceManager.getInstance().getView();
        if (view != null) {
            AnimationDrawable playAnim = (AnimationDrawable) view.getBackground();
            playAnim.setOneShot(true);
            playAnim.stop();
            view.setBackgroundResource(R.drawable.voice_anim01);
        }*/
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
                    return ChattingAdapter.this;
                }

                @Override
                public void onError() {
                }
            });
        }
    }

}