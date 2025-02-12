package com.afmobi.palmchat.ui.activity.groupchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.customview.RoundImageView;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.gif.GifImageView;
import com.afmobi.palmchat.listener.OnItemLongClick;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chats.adapter.ChattingAdapter;
import com.afmobi.palmchat.ui.customview.MyTextView;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.DateUtil;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.VoiceManager;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfAttachImageInfo;
import com.core.AfAttachPAMsgInfo;
import com.core.AfAttachVoiceInfo;
import com.core.AfFriendInfo;
import com.core.AfMessageInfo;
import com.core.AfResponseComm;
import com.core.Consts;
import com.core.cache.CacheManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PopGroupMessageAdapter extends BaseAdapter {

    private ArrayList<AfMessageInfo> listMsgs;

    private Context mContext;

    private LayoutInflater mInflater;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


    public PopGroupMessageAdapter(Context context, ArrayList<AfMessageInfo> list, ListView listview ) {
        mContext = context;
        this.listMsgs = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /** 设置消息集合
     * @param list 群消息集合
     */
    public void setList(ArrayList<AfMessageInfo> list) {
        this.listMsgs = list;
    }


    /** 获取群消息集合
     * @return
     */
    public ArrayList<AfMessageInfo> getLists() {
        return listMsgs;
    }


    public int getCount() {
        return listMsgs.size();
    }

    public AfMessageInfo getItem(int position) {
        return listMsgs.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    OnItemLongClick onItemLongClick;

    /** 设置点击事件
     * @param onItemLongClick
     */
    public void setOnItemClick(OnItemLongClick onItemLongClick) {
        this.onItemLongClick = onItemLongClick;
    }

    public OnItemLongClick getOnItemClick() {
        return onItemLongClick;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        final AfMessageInfo entity = listMsgs.get(position);
        //信息类型  --文本消息、图片、表情、自定义表情
        final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & entity.type;
//    	final int msgTypeNew = entity.type;
        final int status = entity.status;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            viewHolder.position = position;

            convertView = mInflater.inflate(R.layout.item_pop_msg, null);
            //photo(head) image
            viewHolder.vImageViewChattingPhoto = (ImageView) convertView.findViewById(R.id.chatting_photo);
            /*收到消息begin*/
            viewHolder.relativeLayoutFrom = (RelativeLayout) convertView.findViewById(R.id.chatting_from_layout_from);
            //receive text
            viewHolder.relativeLayoutFromText = (RelativeLayout) convertView.findViewById(R.id.chatting_from_layout_from_text);//chatting_from_layout_from_text
            viewHolder.myTextViewContentFromText = (MyTextView) convertView.findViewById(R.id.chatting_msg_content_from_text);//chatting_msg_content_from_text
            //receive gif
            viewHolder.relativeLayoutFromTextGif = (RelativeLayout) convertView.findViewById(R.id.chatting_from_layout_text_gif);//chatting_from_layout_text_gif
            viewHolder.myTextViewContentFromTextGif = (GifImageView) convertView.findViewById(R.id.chatting_msg_content_from_text_gift);//chatting_msg_content_from_text_gift
            //receive voice
            viewHolder.relativeLayoutFromVoice = (RelativeLayout) convertView.findViewById(R.id.chatting_from_layout_from_voice);//chatting_from_layout_from_voice
            viewHolder.textViewChattingPlayTimeFromVoice = (TextView) convertView.findViewById(R.id.chatting_play_time_from_voice);//chatting_play_time_from_voice
            viewHolder.imageViewChattingPlayIconFromVoice = (ImageView) convertView.findViewById(R.id.chatting_play_icon_from_voice);//chatting_play_icon_from_voice
            viewHolder.imageViewChattingPlayIconFromVoiceAnim = (ImageView) convertView.findViewById(R.id.chatting_play_icon_from_voice_anim);//chatting_play_icon_from_voice_anim
            viewHolder.imageViewSendingStatusFromVoice = (ImageView) convertView.findViewById(R.id.sending_status_from_voice);//sending_status_from_voice
            //receive image
            viewHolder.relativeLayoutFromImage = (RelativeLayout) convertView.findViewById(R.id.chatting_from_layout_from_image);//chatting_from_layout_from_image
            viewHolder.imageViewChattingMsgContentFromImage = (RoundImageView) convertView.findViewById(R.id.chatting_msg_content_from_image);//chatting_msg_content_from_image
            viewHolder.imageViewChattingMsgContentFromEmotion = (TextView) convertView.findViewById(R.id.chatting_msg_content_from_emotion);//chatting_msg_content_from_emotion
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
    		
    		/*收到好友请求消息 begin*/
            viewHolder.linearLayoutFromRecommend = (LinearLayout) convertView.findViewById(R.id.chatting_linearlayout_layout_from_recommend); //chatting_linearlayout_layout_from_recommend
            viewHolder.imageViewFromRecommendIcon = (ImageView) convertView.findViewById(R.id.icon_from_recommend); //icon_from_recommend
            viewHolder.textViewFriendSignFromRecommend = (TextView) convertView.findViewById(R.id.friend_sign_from_recommend);//friend_sign_from_recommend
            viewHolder.textViewFriendSignFromAccept = (TextView) convertView.findViewById(R.id.accept);//accept
            viewHolder.textViewFriendSignFromIgnore = (TextView) convertView.findViewById(R.id.ignore);//ignore
            viewHolder.imageViewFromRecommendPhoto = (ImageView) convertView.findViewById(R.id.chatting_photo_from_recommend); //chatting_photo_from_recommend
            viewHolder.textViewFriendSignFromRecommendMsg = (TextView) convertView.findViewById(R.id.chatting_msg_content_from_recommend); //chatting_msg_content_from_recommend

            viewHolder.chatting_from_layout_from_share = convertView.findViewById(R.id.chatting_from_layout_from_share);
            viewHolder.from_share_header = (ImageView) convertView.findViewById(R.id.from_share_header);
            viewHolder.from_share_title = (TextView) convertView.findViewById(R.id.from_share_title);
            viewHolder.from_share_bellow_title = (TextView) convertView.findViewById(R.id.from_share_bellow_title);
            viewHolder.chatting_msg_content_from_share = (ImageView) convertView.findViewById(R.id.chatting_msg_content_from_share);
    		/*收到好友消息 end*/
    		
    		/*收到消息end*/
            viewHolder.l_chatting_date = (LinearLayout) convertView.findViewById(R.id.l_chatting_date);
            viewHolder.vTextViewSendTime = (TextView) convertView.findViewById(R.id.chatting_date);
            viewHolder.vTextViewGroupSystemText = (TextView) convertView.findViewById(R.id.textview_group_system_text);
            viewHolder.vTextViewFriendName = (TextView) convertView.findViewById(R.id.tv_name);
				/*自己发送消息给别人 */
            viewHolder.mMyHeadImg = (ImageView) convertView.findViewById(R.id.chatting_right_photo);
            viewHolder.relativeLayoutTo = (RelativeLayout) convertView.findViewById(R.id.chatting_to_layout_to);
				/*发送文本*/
            viewHolder.relativeLayoutToText = (RelativeLayout) convertView.findViewById(R.id.chatting_to_layout_text);
            viewHolder.imageViewSendingStautsTextImg = (ImageView) convertView.findViewById(R.id.sending_status_text_img);
            viewHolder.imageViewSendingStautsText = (ProgressBar) convertView.findViewById(R.id.sending_status_text);
            viewHolder.myTextViewContentText = (MyTextView) convertView.findViewById(R.id.chatting_msg_content_text);
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
            viewHolder.imageViewContentImage = (ImageView) convertView.findViewById(R.id.chatting_msg_content_image);
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
//	    	  convertView = buildView(viewHolder,entity,position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        AvatarImageInfo imageInfo = null;
        if (position < 0) return convertView; 
        viewHolder.mMyHeadImg.setVisibility(View.GONE);

        viewHolder.vTextViewGroupSystemText.setVisibility(View.GONE);
        if (MessagesUtils.isReceivedMessage(status)) {//收到消息
            PalmchatLogUtils.println("GroupChattingAdapter  position  " + position + " getView  " + entity.client_time);

            viewHolder.vTextViewSendTime.setText(dateFormat.format(new Date(entity.client_time)));//(CommonUtils.getRealChatDate(mContext, System.currentTimeMillis(), entity.client_time));
            final AfFriendInfo afFriend = CacheManager.getInstance().searchAllFriendInfo(entity.fromAfId);
            if (afFriend != null) {
                viewHolder.vTextViewFriendName.setVisibility(View.VISIBLE);
                viewHolder.vTextViewFriendName.setText(afFriend.name);
                if (!TextUtils.isEmpty(afFriend.alias)) {
                    viewHolder.vTextViewFriendName.setText(afFriend.alias);
                }
            } else { //如果此用户不存在当前friend，则直接friend名直接显示afid
                viewHolder.vTextViewFriendName.setVisibility(View.VISIBLE);
                //getCorrectAfid方法去除a,只显示数字
                viewHolder.vTextViewFriendName.setText(CommonUtils.getCorrectAfid(entity.fromAfId));
//				viewHolder.vTextViewFriendName.setVisibility(View.INVISIBLE);
            }

            final ImageView imgIconHead = viewHolder.vImageViewChattingPhoto;
            //AfFriendInfo afF = CacheManager.getInstance().searchAllFriendInfo(entity.fromAfId);
            if (afFriend != null) {
                entity.mSex = afFriend.sex;
                entity.mName = afFriend.name;
                entity.mAlias = afFriend.alias;
                entity.headImagePath = afFriend.head_img_path;

//                if (!CommonUtils.showHeadImage(afFriend.afId, imgIconHead, afFriend.sex)) {//判断是否显示默认指定的头像(如系统帐号头像)
                	//调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
					ImageManager.getInstance().DisplayAvatarImage(imgIconHead, afFriend.getServerUrl(),
							entity.fromAfId,Consts.AF_HEAD_MIDDLE, afFriend.sex,afFriend.getSerialFromHead(),null);
//                }
            } else {
            	//调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
				ImageManager.getInstance().DisplayAvatarImage(imgIconHead, entity.getServerUrl(),
						entity.fromAfId,Consts.AF_HEAD_MIDDLE, entity.mSex,entity.getSerialFromHead(),null);
            }

            imgIconHead.setVisibility(View.GONE);

            switch (msgType) { //根据消息类型显示
                case AfMessageInfo.MESSAGE_TEXT: { //文本消息
                    viewHolder.myTextViewContentFromText.setText(entity.msg);//chatting_msg_content_from_text
                    CharSequence text = EmojiParser.getInstance(mContext).parse(entity.msg);
                    viewHolder.myTextViewContentFromText.setText(text);
                    showOrDisappear(viewHolder, status, msgType);
                    break;
                }
                case AfMessageInfo.MESSAGE_STORE_EMOTIONS: {//store表情

                    viewHolder.myTextViewContentFromTextGif.displayGif(mContext, viewHolder.myTextViewContentFromTextGif, entity.msg, null);
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
                        if (entity.autoPlay) {
                            PalmchatLogUtils.println("GroupChattingAdapter  entity.autoPlay  " + entity.autoPlay);
                            entity.autoPlay = false;
                            stopPlayAnim(voiceIconFromAnim, voiceIconFrom);
                            VoiceManager.getInstance().setView(null);
                            VoiceManager.getInstance().setPlayIcon(null);
                            viewHolder.imageViewChattingPlayIconFromVoice.performClick();
                        }
                    } else {
                        viewHolder.textViewChattingPlayTimeFromVoice.setText("0s");
                    }
                    if (status == AfMessageInfo.MESSAGE_READ) {
                        voiceStatusFrom.setVisibility(View.GONE);
                    } else {
                        voiceStatusFrom.setVisibility(View.VISIBLE);
                    }
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
                                viewHolder.from_share_title.setText(afAttachPAMsgInfo.name);
                                if (TextUtils.isEmpty(afAttachPAMsgInfo.content)) {
                                    viewHolder.from_share_bellow_title.setVisibility(View.GONE);
                                } else {
                                    CharSequence text = EmojiParser.getInstance(mContext).parse(afAttachPAMsgInfo.content);
                                    viewHolder.from_share_bellow_title.setText(text);
                                    viewHolder.from_share_bellow_title.setVisibility(View.VISIBLE);
                                }
                            }
                        }
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
                    }
                    break;
                }
//                case AfMessageInfo.MESSAGE_EMOTIONS:
                case AfMessageInfo.MESSAGE_IMAGE: {
                    final ImageView imageContent = viewHolder.imageViewChattingMsgContentFromImage;
                    final TextView imageContentEmotion = viewHolder.imageViewChattingMsgContentFromEmotion;
                    //趣味表情
                   /* if (msgType == AfMessageInfo.MESSAGE_EMOTIONS) {
                        viewHolder.textViewChattingFileSizeFromImage.setVisibility(View.GONE);
                        imageContent.setVisibility(View.GONE);
                        imageContentEmotion.setVisibility(View.VISIBLE);
                        String text = entity.msg;
                        imageContentEmotion.setText(text);
                    } else*/ {//图片
                        imageContent.setVisibility(View.VISIBLE);
                        imageContentEmotion.setVisibility(View.GONE);
                        final AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) entity.attach;
                        if (afAttachImageInfo != null) {

                            String path = RequestConstant.IMAGE_CACHE + afAttachImageInfo.small_file_name;
                         /*   ChatImageInfo chatImageInfo = new ChatImageInfo(path, false);
                            ImageLoader.getInstance().displayImage(imageContent, chatImageInfo);*/
                            ImageManager.getInstance().DisplayChatImage(imageContent,path,null,R.color.white,R.drawable.ic_loadfailed, Constants.CHATIMAGE_SIZE);

                            viewHolder.textViewChattingProgressFromImageFromImage.setVisibility(View.GONE);
                            viewHolder.textViewChattingFileSizeFromImage.setText(CommonUtils.showImageSize(afAttachImageInfo.large_file_size));
                        }

                        if (status == AfMessageInfo.MESSAGE_READ) {
                            viewHolder.textViewChattingFileSizeFromImage.setVisibility(View.GONE);
                        } else {
                            viewHolder.textViewChattingFileSizeFromImage.setVisibility(View.GONE);
                        }


                    }

                    showOrDisappear(viewHolder, status, msgType);
                    break;
                }
                case AfMessageInfo.MESSAGE_CARD: {
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
                        viewHolder.textViewchattingMsgContentFromCard.setText(mContext.getString(R.string.af_id) + ":" + CommonUtils.getCorrectAfid(afFriendInfo.afId));
                        final ImageView imgIcon = viewHolder.imageViewIconFromCard;

                      //调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
        				ImageManager.getInstance().DisplayAvatarImage(imgIcon, afFriendInfo.getServerUrl(),
        						afFriendInfo.getAfidFromHead(),Consts.AF_HEAD_MIDDLE, afFriendInfo.sex,afFriendInfo.getSerialFromHead(),null);
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
                            viewHolder.textViewchattingMsgContentFromCard.setText(mContext.getString(R.string.af_id) + ":" + CommonUtils.getCorrectAfid(afFriendInfo2.afId));
//						sending(entity.status ,viewHolder.imageViewSendingStautsCard);
                            final ImageView imgIcon = viewHolder.imageViewIconFromCard;

                          //调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
            				ImageManager.getInstance().DisplayAvatarImage(imgIcon, afFriendInfo2.getServerUrl(),
            						afFriendInfo2.getAfidFromHead(),Consts.AF_HEAD_MIDDLE, afFriendInfo2.sex,afFriendInfo2.getSerialFromHead(),null);
                        } else {
                            PalmchatLogUtils.println("from get card failure.");
                            viewHolder.textViewchattingMsgContentFromCard.setText(mContext.getString(R.string.af_id) + ":" + CommonUtils.getCorrectAfid(entity.msg));
                        }
                    }
                    showOrDisappear(viewHolder, status, msgType);
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
        }

        showTimeLine(viewHolder.l_chatting_date, viewHolder.vTextViewSendTime, position);
        return convertView;
    }

    /** 显示系统信息
     * @param entity
     * @param viewHolder
     */
    private void showSystemText(final AfMessageInfo entity,
                                ViewHolder viewHolder) {
        viewHolder.vTextViewGroupSystemText.setVisibility(View.VISIBLE);
        viewHolder.vTextViewGroupSystemText.setText(entity.msg);
        disableFromAndTo(viewHolder);
    }


    /** 隐藏接收和发送消息布局
     * @param viewHolder
     */
    private void disableFromAndTo(ViewHolder viewHolder) {
        viewHolder.relativeLayoutFrom.setVisibility(View.GONE);
        viewHolder.relativeLayoutTo.setVisibility(View.GONE);
    }

    /** 根据消息的类型不同，显示或隐藏view控件
     * @param viewHolder
     * @param status 消息状态
     * @param msgType 消息类型
     */
    private void showOrDisappear(ViewHolder viewHolder, int status, int msgType) {
        if (MessagesUtils.isReceivedMessage(status)) {//收到
            viewHolder.relativeLayoutFrom.setVisibility(View.VISIBLE);
            viewHolder.relativeLayoutTo.setVisibility(View.GONE);
            viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
            switch (msgType) {
                case AfMessageInfo.MESSAGE_TEXT: { //收到的文本消息，则只显示文本布局
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
                case AfMessageInfo.MESSAGE_VOICE: { //收到的语音消息，则只显示语音布局
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
                case AfMessageInfo.MESSAGE_CARD: { //收到名片消息，则只显示名片布局
                    viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.VISIBLE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.GONE);
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);
                    break;
                }
                case AfMessageInfo.MESSAGE_FRIEND_REQ: {//朋友请求消息
                    viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromTextGif.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromVoice.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromImage.setVisibility(View.GONE);
                    viewHolder.relativeLayoutFromCard.setVisibility(View.GONE);
                    viewHolder.linearLayoutFromRecommend.setVisibility(View.VISIBLE);
                    viewHolder.relativeLayoutFrom.setVisibility(View.GONE);//接收消息大布局
                    viewHolder.relativeLayoutTo.setVisibility(View.GONE);  //发送消息大布局
                    viewHolder.chatting_from_layout_from_share.setVisibility(View.GONE);
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
        }
    }

    static class ViewHolder {

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
        //receive gif
        public RelativeLayout relativeLayoutFromTextGif;//chatting_from_layout_from_text
        public GifImageView myTextViewContentFromTextGif;//chatting_msg_content_from_text
        //receive voice
        public RelativeLayout relativeLayoutFromVoice;//chatting_from_layout_from_voice
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
        public ImageView imageViewContentImage;
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
		/*自己发送消息 end*/

    }

    private Handler handler = new Handler();

    /** 停止语音播放动画
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
     * @param l_chatting_date  控件封装
     * @param timeTxt getView下标
      * @param position getView下标
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
                        if (clientTime - serClientTime > ChattingAdapter.THREE_MIN) {
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
        //获取格式化后的时间
        String text = DateUtil.dateChangeStr(date, PalmchatApp.getApplication());
        timeTxt.setText(text);
        l_chatting_date.setVisibility(View.VISIBLE);
        timeTxt.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(text)) {
            l_chatting_date.setVisibility(View.GONE);
            timeTxt.setVisibility(View.GONE);
        }
    }

}