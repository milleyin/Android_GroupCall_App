package com.afmobi.palmchat.ui.activity.publicaccounts.adapter;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.listener.OnItemLongClick;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.chats.ForwardSelectActivity;
import com.afmobi.palmchat.ui.activity.payment.MyWapActivity;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.AccountsChattingActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.InnerNoAbBrowserActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountDetailsActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnSelectButtonDialogListener;
import com.afmobi.palmchat.ui.customview.list.CircleAdapter;
import com.afmobi.palmchat.ui.customview.list.LinearLayoutListView;
import com.afmobi.palmchat.ui.customview.MyTextView;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.DateUtil;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfAttachImageInfo;
import com.core.AfAttachPAMsgInfo;
import com.core.AfAttachPalmCoinSysInfo;
import com.core.AfFriendInfo;
import com.core.AfLoginInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.AfAttachPalmCoinSysInfo.AfAttachPalmCoinSysInfoItem;
import com.core.AfProfileInfo.PalmCoinMenuItemInfo;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
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

public class AccountsChattingAdapter extends BaseAdapter implements OnClickListener, AfHttpResultListener {

    /**
     * 相隔3分钟的毫秒级
     */
    public static final long THREE_MIN = 3 * 60 * 1000;
    /**
     * 消息集合
     */
    private ArrayList<AfMessageInfo> mMsgsList = new ArrayList<AfMessageInfo>();
    /**
     * 上下文
     */
    private Activity mContext;
    /**
     * 布局加载器
     */
    private LayoutInflater mInflater;
    /**
     * 日期格式转换类
     */
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat mDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    /**
     * 中间件类(jni接口)
     */
    private AfPalmchat mAfCorePalmchat;
    /**
     * 个人信息
     */
    private AfProfileInfo myProfileInfo;
    /**
     * 对方ID
     */
    private String mFriendAfid;
    /**
     * 长按接口
     */
    private OnItemLongClick mOnItemLongClick;

    private HashMap<String, AfAttachPAMsgInfo> afAttachPAMsgInfoHashMap = new HashMap<>();
    /**
     * 是否已经显示Menu
     */
    boolean isShowMenu;
    private boolean mIsHistory;

    /**
     * 构造方法
     *
     * @param context
     * @param
     * @param listview
     * @param
     * @param currentAfid
     */
    public AccountsChattingAdapter(Activity context, ArrayList<AfMessageInfo> msgsList, ListView listview, String currentAfid, boolean isHistory) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMsgsList = msgsList;
        // 初始化myProfile
        this.myProfileInfo = CacheManager.getInstance().getMyProfile();
        this.mAfCorePalmchat = ((PalmchatApp) mContext.getApplicationContext()).mAfCorePalmchat;
        this.mFriendAfid = currentAfid;
        //初始化中件间
        mAfCorePalmchat = ((PalmchatApp) mContext.getApplication()).mAfCorePalmchat;
        mIsHistory = isHistory;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return null == mMsgsList ? 0 : mMsgsList.size();
    }

    @Override
    public AfMessageInfo getItem(int position) {
        // TODO Auto-generated method stub
        return mMsgsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /**
     * 确定这么调用不会有问题？记得有点问题的
     */
    @Override
    public void notifyDataSetChanged() {
        myProfileInfo = CacheManager.getInstance().getMyProfile();
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final AfMessageInfo entity = mMsgsList.get(position);
        final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & entity.type;
        final int status = entity.status;
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            viewHolder.position = position;

            convertView = mInflater.inflate(R.layout.item_chatting_accounts_to_text, null);

            viewHolder.l_chatting_date = (LinearLayout) convertView.findViewById(R.id.l_chatting_accounts_date);
            viewHolder.vTextViewSendTime = (TextView) convertView.findViewById(R.id.chatting_accounts_date);
            /* 收到好友请求消息 begin */
            viewHolder.rightViewStub = (ViewStub) convertView.findViewById(R.id.view_stub_accounts_right);
            viewHolder.leftViewStub = (ViewStub) convertView.findViewById(R.id.view_stub_accounts_left);
            /* 收到好友消息 end */
            // 由于view太多，导致性能很差，现在改为延迟加载布局
            if (MessagesUtils.isReceivedMessage(status)) {
                // 初始化收到消息布局
                initLeftView(viewHolder, convertView);
            } else {
                // 初始化发送消息布局
                initRightView(viewHolder, convertView);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (MessagesUtils.isReceivedMessage(status)) {// 收到消息
                if (viewHolder.initLeftViewStub == null) {
                    // 初始化收到消息布局
                    initLeftView(viewHolder, convertView);
                }
            } else {
                if (viewHolder.initRightViewStub == null) {
                    // 初始化发送消息布局
                    initRightView(viewHolder, convertView);
                }
            }
        }
        if (position < 0) {
            return convertView;
        }

        if (MessagesUtils.isReceivedMessage(status)) {// 收到消息
            viewHolder.leftViewStub.setVisibility(View.VISIBLE);
            PalmchatLogUtils.println("ChattingAdapter  position  " + position + " getView  " + entity.client_time);
            // 点击用户头像事件
            viewHolder.vImageViewChattingPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AfFriendInfo afFriendInfo2 = CacheManager.getInstance().searchAllFriendInfo(entity.fromAfId);
                    if (afFriendInfo2 != null && afFriendInfo2.afId != null && afFriendInfo2.afId.startsWith("r")) {
                        if (!afFriendInfo2.unfollow_opr) {//不能unfollow的账号进入聊天页面 如paymentservice
                            if (!CommonUtils.isSystemAccount(afFriendInfo2.afId)) {
                                toPublicAccountDetail(afFriendInfo2);
                            }
                        }
                    }
                }
            });

            if (!mIsHistory) {
                viewHolder.vImageViewChattingPhoto.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Object obj = entity.attach;
                        if (obj != null && obj instanceof AfFriendInfo) {
                            final AfFriendInfo afFriendInfo = (AfFriendInfo) entity.attach;
                            if (mOnItemLongClick != null) {
                                mOnItemLongClick.onItemClick(Chatting.ACTION_LONG_CLICK, afFriendInfo.name, afFriendInfo.afId, afFriendInfo.sex);
                            }
                        } else {
                            AfFriendInfo afFriendInfo2 = CacheManager.getInstance().searchAllFriendInfo(entity.fromAfId);
                            if (afFriendInfo2 != null && !afFriendInfo2.afId.startsWith(DefaultValueConstant._R)) {
                                if (mOnItemLongClick != null) {
                                    mOnItemLongClick.onItemClick(Chatting.ACTION_LONG_CLICK, afFriendInfo2.name, afFriendInfo2.afId, afFriendInfo2.sex);
                                }
                            } else {
                                PalmchatLogUtils.println("vImageViewChattingPhoto  get card failure no name.");
                            }
                        }
                        return true;
                    }
                });
            }

            final ImageView imgIconHead = viewHolder.vImageViewChattingPhoto;
            final AfFriendInfo afF = CacheManager.getInstance().searchAllFriendInfo(entity.fromAfId);
            if (afF != null) {
            /*	if (!CommonUtils.showHeadImage(afF.afId, imgIconHead, afF.sex)) {
                    final String imgUrl = afF.head_img_path;
					imgIconHead.setTag(imgUrl);*/
                // 调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
                ImageManager.getInstance().DisplayAvatarImage(imgIconHead, afF.getServerUrl(), afF.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, afF.sex, afF.getSerialFromHead(), null);
//				}
            }
            // 显示时间
            viewHolder.vTextViewSendTime.setText(mDateFormat.format(new Date(entity.client_time)));// (CommonUtils.getRealChatDate(mContext,
            // System.currentTimeMillis(),
            // entity.client_time));
            viewHolder.myTextViewContentFromText.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showLongClickItem(status, msgType, entity, v, position);
                    return true;
                }
            });
            switch (msgType) {
                case AfMessageInfo.MESSAGE_PAY_SYS_NOTIFY: {// B31消息
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewHolder.initLeftViewStub_id.getLayoutParams());
                    layoutParams.setMargins(0, 0, 0, CommonUtils.dip2px(mContext.getApplicationContext(), 22));
                    viewHolder.initLeftViewStub_id.setLayoutParams(layoutParams);

                    viewHolder.vImageViewChattingPhoto.setVisibility(View.GONE);
                    if (viewHolder.relativeLayoutFromText != null) {
                        viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                    }
                    if (viewHolder.rl_ImgTexMsg != null) {
                        viewHolder.rl_ImgTexMsg.setVisibility(View.GONE);
                    }
                    if (viewHolder.mMyHeadImg != null) {
                        viewHolder.mMyHeadImg.setVisibility(View.GONE);
                    }
                    if (viewHolder.relativeLayoutToText != null) {
                        viewHolder.relativeLayoutToText.setVisibility(View.GONE);
                    }

                    viewHolder.ll_coin_change.setVisibility(View.VISIBLE);
                    AfAttachPalmCoinSysInfo afAttachPalmCoinSysInfo = (AfAttachPalmCoinSysInfo) (entity.attach);
                    if (afAttachPalmCoinSysInfo != null) {
                        /** 获取状态 */
                        int rechargeStatus = afAttachPalmCoinSysInfo.status;
                        if (rechargeStatus == 0) { // 处理中
                            viewHolder.img_coin_status.setBackgroundResource(R.drawable.ic_payment__handing);
                        } else if (rechargeStatus == 1) { // 成功
                            viewHolder.img_coin_status.setBackgroundResource(R.drawable.ic_payment_sevice);
                        } else if (rechargeStatus == 2) { // 失败
                            viewHolder.img_coin_status.setBackgroundResource(R.drawable.ic_payment__failed);
                        }

                        /** 设置标题 */
                        viewHolder.tv_coin_title.setText(afAttachPalmCoinSysInfo.header);
                        /** 设置body */
                        ArrayList<AfAttachPalmCoinSysInfoItem> body_list = afAttachPalmCoinSysInfo.body_list;
                        if (body_list != null && body_list.size() > 0) {

                            List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
                            for (int i = 0; i < body_list.size(); i++) {
                                AfAttachPalmCoinSysInfoItem afAttachPalmCoinSysInfoItem = body_list.get(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("textName", afAttachPalmCoinSysInfoItem.key);
                                map.put("textValue", afAttachPalmCoinSysInfoItem.value);
                                mData.add(map);
                            }

                            CircleAdapter mCircleAdapter = new CircleAdapter(mContext, mData, R.layout.item_message_coin_change, new String[]{"textName", "textValue"}, new int[]{R.id.tv_coin_key, R.id.tv_coin_value});
                            viewHolder.mLlistView.setAdapter(mCircleAdapter);
                        }
                    }

                    final String url = afAttachPalmCoinSysInfo.footer;
                    if (TextUtils.isEmpty(url)) {//当充值消息不需要跳转的时候 就不显示那个detail按钮
                        viewHolder.rl_coin_more_detail.setVisibility(View.GONE);
                    } else {
                        viewHolder.rl_coin_more_detail.setVisibility(View.VISIBLE);
                    }
                    viewHolder.ll_coin_change.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(url)) {
                                toOrderDetail(url);
                            }
                        }
                    });
                    break;
                }
                case AfMessageInfo.MESSAGE_TEXT:
                case AfMessageInfo.MESSAGE_FOLLOW: {
                    /**隐藏B31通知布局*/
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewHolder.initLeftViewStub_id.getLayoutParams());
                    layoutParams.setMargins(0, 0, 0, CommonUtils.dip2px(mContext.getApplicationContext(), 22));
                    viewHolder.initLeftViewStub_id.setLayoutParams(layoutParams);
                    viewHolder.ll_coin_change.setVisibility(View.GONE);
                    viewHolder.vImageViewChattingPhoto.setVisibility(View.VISIBLE);
                    viewHolder.relativeLayoutFromText.setVisibility(View.VISIBLE);
                    viewHolder.myTextViewContentFromText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    viewHolder.myTextViewContentFromText.setCompoundDrawablePadding(0);
                    viewHolder.myTextViewContentFromText.setText(entity.msg);// chatting_msg_content_from_text
                    CharSequence text = EmojiParser.getInstance(mContext).parse(entity.msg);
                    viewHolder.myTextViewContentFromText.setText(text);
                    showOrDisappear(viewHolder, status, msgType);
                    viewHolder.rl_ImgTexMsg.setVisibility(View.GONE);
                    break;
                }

                case AfMessageInfo.MESSAGE_PA_URL:// 公众帐号图文通知消息
                case AfMessageInfo.MESSAGE_PUBLIC_ACCOUNT:// 公众帐号文字通知消息
                {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewHolder.initLeftViewStub_id.getLayoutParams());
                    /**隐藏B31通知布局*/
                    viewHolder.ll_coin_change.setVisibility(View.GONE);
                    if (msgType == AfMessageInfo.MESSAGE_PUBLIC_ACCOUNT && !mIsHistory) {
                        layoutParams.setMargins(0, 0, 0, CommonUtils.dip2px(mContext.getApplicationContext(), 22));
                        viewHolder.initLeftViewStub_id.setLayoutParams(layoutParams);
                        viewHolder.vImageViewChattingPhoto.setVisibility(View.VISIBLE);
                        viewHolder.relativeLayoutFromText.setVisibility(View.VISIBLE);
                        viewHolder.myTextViewContentFromText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        viewHolder.myTextViewContentFromText.setCompoundDrawablePadding(0);
                        StringBuilder msgtext = new StringBuilder();// entity.msg +
                        // entity.attach==null?"":
                        if (null != entity.msg) {
                            msgtext.append(entity.msg);
                        }
                        viewHolder.myTextViewContentFromText.setText(msgtext.toString());// chatting_msg_content_from_text
                        CommonUtils.setUrlSpanLink(mContext, viewHolder.myTextViewContentFromText);
                        showOrDisappear(viewHolder, status, msgType);
                        viewHolder.rl_ImgTexMsg.setVisibility(View.GONE);
                        break;
                    } else {
                        if (mIsHistory) {
                            if (position == 0) {
                                layoutParams.setMargins(0, CommonUtils.dip2px(mContext.getApplicationContext(), 10), 0, 0);
                            } else {
                                layoutParams.setMargins(0, CommonUtils.dip2px(mContext.getApplicationContext(), 4), 0, 0);
                            }
                        } else {
                            layoutParams.setMargins(0, 0, 0, CommonUtils.dip2px(mContext.getApplicationContext(), 22));
                            viewHolder.mtvPublicTime.setVisibility(View.GONE);
                        }
                        viewHolder.initLeftViewStub_id.setLayoutParams(layoutParams);
                        viewHolder.vImageViewChattingPhoto.setVisibility(View.GONE);
                        viewHolder.relativeLayoutFromText.setVisibility(View.GONE);
                        viewHolder.rl_ImgTexMsg.setTag(position);
                        if (entity.attach != null && entity.attach instanceof AfAttachPAMsgInfo) {
                            AfAttachPAMsgInfo afAttachPAMsgInfo = (AfAttachPAMsgInfo) (entity.attach);
                            try {
                                viewHolder.mtvPublicTime.setText(mDateFormat.format(afAttachPAMsgInfo.time));
                            } catch (Exception e) {
                                viewHolder.mtvPublicTime.setText(afAttachPAMsgInfo.time);
                                e.printStackTrace();
                            }
                            if (msgType == AfMessageInfo.MESSAGE_PUBLIC_ACCOUNT) {
                                viewHolder.iv_ImgTexMsgImage.setVisibility(View.GONE);
                                viewHolder.tv_ImgTexMsgTitle.setVisibility(View.GONE);
                                viewHolder.view_divideline.setVisibility(View.GONE);
                                viewHolder.view_fulltxt.setVisibility(View.GONE);
                                viewHolder.rl_ImgTexMsg.setOnClickListener(null);
                                viewHolder.tv_ImgTexMsgDescription.setVisibility(View.GONE);
                                viewHolder.tv_account_text_des.setVisibility(View.VISIBLE);
                                if (null != afAttachPAMsgInfo.content) {
                                    viewHolder.tv_account_text_des.setText(Html.fromHtml(afAttachPAMsgInfo.content));
                                }
                            } else {
                                viewHolder.rl_ImgTexMsg.setOnClickListener(this);
                                if (TextUtils.isEmpty(afAttachPAMsgInfo.imgurl)) {
                                    break;
                                }
                                if (null != afAttachPAMsgInfo.title) {
                                    viewHolder.tv_ImgTexMsgTitle.setText(afAttachPAMsgInfo.title);
                                }
                                viewHolder.iv_ImgTexMsgImage.setVisibility(View.VISIBLE);
                                viewHolder.tv_ImgTexMsgTitle.setVisibility(View.VISIBLE);
                                viewHolder.view_divideline.setVisibility(View.VISIBLE);
                                viewHolder.view_fulltxt.setVisibility(View.VISIBLE);
                                viewHolder.tv_ImgTexMsgDescription.setVisibility(View.VISIBLE);
                                viewHolder.tv_account_text_des.setVisibility(View.GONE);
                                if (null != afAttachPAMsgInfo.content) {
                                    viewHolder.tv_ImgTexMsgDescription.setText(Html.fromHtml(afAttachPAMsgInfo.content));
                                }
                            }
                            viewHolder.rl_ImgTexMsg.setVisibility(View.VISIBLE);
                            if (!mIsHistory) {
                                viewHolder.rl_ImgTexMsg.setOnLongClickListener(new OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        showLongClickItem(status, msgType, entity, v, position);
                                        return true;
                                    }
                                });
                            }
                            ImageManager.getInstance().DisplayImage(viewHolder.iv_ImgTexMsgImage, getUrlWithStatistics(afAttachPAMsgInfo.imgurl), R.color.light_white, false, null);

                        }
                    }
                }
                break;
                default:
                    break;
            }
            // 收到消息结束
        } else {// 发送消息
            viewHolder.vTextViewSendTime.setText(mDateFormat.format(new Date(entity.client_time)));// (CommonUtils.getRealChatDate(mContext,
            // System.currentTimeMillis(),
            // entity.client_time));
            switch (msgType) {
                case AfMessageInfo.MESSAGE_TEXT: {
                    PalmchatLogUtils.println("entity " + entity + "  viewHolder.vTextViewContent  " + viewHolder.myTextViewContentText);
                    CharSequence text = EmojiParser.getInstance(mContext).parse(entity.msg);
                    viewHolder.myTextViewContentText.setText(text);
                    showProgressBarOrNot(position, entity, msgType, status, viewHolder.imageViewSendingStautsTextImg, viewHolder.imageViewSendingTextStauts);
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

                default:
                    break;
            }

        }
        if (!mIsHistory) {
            showTimeLine(viewHolder.l_chatting_date, viewHolder.vTextViewSendTime, position);
        }
        return convertView;
    }

    /**
     * 设置消息集合
     *
     * @param list
     */
    public void setMsgsList(ArrayList<AfMessageInfo> list) {
        this.mMsgsList = list;
    }

    /**
     * 获取消息集合
     *
     * @return
     */
    public ArrayList<AfMessageInfo> getMsgsLists() {
        return mMsgsList;
    }

    /**
     * 设置点击item事件
     *
     * @param mOnItemLongClick
     */
    public void setOnItemClick(OnItemLongClick mOnItemLongClick) {
        this.mOnItemLongClick = mOnItemLongClick;
    }

    /**
     * 获取事件
     *
     * @return
     */
    public OnItemLongClick getOnItemClick() {
        return mOnItemLongClick;
    }

    /**
     * 初始化发送消息布局
     *
     * @param viewHolder
     * @param convertView
     */
    private void initLeftView(ViewHolder viewHolder, View convertView) {
        // 加载布局
        viewHolder.initLeftViewStub = viewHolder.leftViewStub.inflate();
        viewHolder.leftViewStub.setVisibility(View.VISIBLE);
        // photo(head) image
        viewHolder.vImageViewChattingPhoto = (ImageView) convertView.findViewById(R.id.chatting_accounts_photo);
        viewHolder.relativeLayoutFrom = (RelativeLayout) convertView.findViewById(R.id.chatting_accounts_from_layout_from);
        // receive text
        viewHolder.relativeLayoutFromText = (RelativeLayout) convertView.findViewById(R.id.chatting_accounts_from_layout_from_text);// chatting_from_layout_from_text
        viewHolder.myTextViewContentFromText = (MyTextView) convertView.findViewById(R.id.chatting_accounts_msg_content_from_text);// chatting_msg_content_from_text

        // 收到公共账号图文消息
        viewHolder.rl_ImgTexMsg = (RelativeLayout) convertView.findViewById(R.id.chatting_accounts_imagetxtmsg_layout_id);
        viewHolder.tv_ImgTexMsgTitle = (TextView) convertView.findViewById(R.id.chatting_accounts_imagetxtmsg_title_id);
        viewHolder.tv_ImgTexMsgDescription = (TextView) convertView.findViewById(R.id.chatting_accounts_imagetxtmsg_description_id);
        viewHolder.iv_ImgTexMsgImage = (ImageView) convertView.findViewById(R.id.chatting_accounts_imagetxtmsg_img_id);

        // 收到coin改变消息
        viewHolder.ll_coin_change = convertView.findViewById(R.id.ll_coin_change);
        viewHolder.img_coin_status = (ImageView) convertView.findViewById(R.id.img_coin_status);
        viewHolder.tv_coin_title = (TextView) convertView.findViewById(R.id.tv_coin_title);
        viewHolder.rl_coin_more_detail = (View) convertView.findViewById(R.id.rl_more_detail);

        viewHolder.mLlistView = (LinearLayoutListView) convertView.findViewById(R.id.ll_coin_list);
        viewHolder.mtvPublicTime = (TextView) convertView.findViewById(R.id.public_account_time);
        viewHolder.view_divideline = convertView.findViewById(R.id.chatting_accounts_divideline_id);
        viewHolder.view_fulltxt = convertView.findViewById(R.id.chatting_accounts_fulltxt_layout_id);
        viewHolder.initLeftViewStub_id = convertView.findViewById(R.id.view_stub_accounts_left_id);
        viewHolder.tv_account_text_des = (TextView) convertView.findViewById(R.id.chatting_accounts_txtmsg_description_id);
    }

    /**
     * 初始化发送消息布局
     *
     * @param viewHolder
     * @param convertView
     */
    private void initRightView(ViewHolder viewHolder, View convertView) {
		/* 自己发送消息给别人 */
        // 加载布局
        viewHolder.initRightViewStub = viewHolder.rightViewStub.inflate();
        viewHolder.rightViewStub.setVisibility(View.VISIBLE);
        viewHolder.mMyHeadImg = (ImageView) convertView.findViewById(R.id.chatting_accounts_right_photo);
        viewHolder.relativeLayoutTo = (RelativeLayout) convertView.findViewById(R.id.chatting_accounts_to_layout_to);
		/* 发送文本 */
        viewHolder.relativeLayoutToText = (RelativeLayout) convertView.findViewById(R.id.chatting_accounts_to_layout_text);
        viewHolder.imageViewSendingStautsTextImg = (ImageView) convertView.findViewById(R.id.sending_accounts_status_text_img);
        viewHolder.imageViewSendingTextStauts = (ProgressBar) convertView.findViewById(R.id.sending_accounts_status_text);
        viewHolder.myTextViewContentText = (MyTextView) convertView.findViewById(R.id.chatting_accounts_msg_content_text);

        if (null != myProfileInfo) {
            // 调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
            ImageManager.getInstance().DisplayAvatarImage(viewHolder.mMyHeadImg, myProfileInfo.getServerUrl(), myProfileInfo.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, myProfileInfo.sex, myProfileInfo.getSerialFromHead(), null);
            viewHolder.mMyHeadImg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivityForResult(new Intent(mContext, MyProfileActivity.class), IntentConstant.REQUEST_CODE_CHATTING);
                }
            });
        }
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
        if (AfMessageInfo.MESSAGE_SENTING == status) { // 显示正在发送的进度条
            PalmchatLogUtils.println("position  " + position + "TIME  sending  " + mDateFormat.format(new Date()) + "  ");
            showProgressBar(img, progressBar);
        } else if (AfMessageInfo.MESSAGE_UNSENT == status) {// 发送失败
            PalmchatLogUtils.println("position  " + position + "TIME  fail  " + mDateFormat.format(new Date()) + "  ");
            disappearProgressBar(img, progressBar);// 隐藏进度条
            showSendFail(img, status, msgType, entity, position);// 显示发送错误
        } else if (AfMessageInfo.MESSAGE_SENT == status) {// 发送成功
            PalmchatLogUtils.println("position  " + position + "TIME  success  " + mDateFormat.format(new Date()) + "  ");
            disappearProgressBar(img, progressBar);
            showSendSuccessed(img);
        } else {// send and already read
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
    private void showProgressBar(ImageView imageViewSendingStautsImage, ProgressBar progressbarStatusImage) {
        imageViewSendingStautsImage.setVisibility(View.GONE);
        progressbarStatusImage.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏进度条
     *
     * @param imageViewSendingStautsImage
     * @param progressbarStatusImage
     */
    private void disappearProgressBar(ImageView imageViewSendingStautsImage, ProgressBar progressbarStatusImage) {
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
        if (MessagesUtils.isReceivedMessage(status)) {// 收到
            if (viewHolder.initLeftViewStub_id != null) {
                viewHolder.initLeftViewStub_id.setVisibility(View.VISIBLE);
            }
            if (viewHolder.relativeLayoutTo != null) {
                viewHolder.relativeLayoutTo.setVisibility(View.GONE);
            }
            switch (msgType) {
                case AfMessageInfo.MESSAGE_TEXT:
                case AfMessageInfo.MESSAGE_FOLLOW: {
                    viewHolder.relativeLayoutFromText.setVisibility(View.VISIBLE);
                    break;
                }

                default:
                    break;
            }
        } else {
            if (viewHolder.relativeLayoutTo != null) {
                viewHolder.relativeLayoutTo.setVisibility(View.VISIBLE);
            }
            if (viewHolder.initLeftViewStub_id != null) {
                viewHolder.initLeftViewStub_id.setVisibility(View.GONE);
            }
            switch (msgType) {
                case AfMessageInfo.MESSAGE_TEXT:
                    viewHolder.relativeLayoutToText.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

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
        if (!CommonUtils.isEmpty(mFriendAfid) && mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS)) {// palmchat
            // team
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
                showLongClickItem(status, msgType, entity, view, position);
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

        // 消息时间
        public LinearLayout l_chatting_date;
        public TextView vTextViewSendTime;
        // 公用头像(除收到的好友请求消息imageViewFromRecommendIcon/imageViewFromRecommendPhoto外)
        public ImageView vImageViewChattingPhoto;// chatting_photo
        public int position;

        /* 收到消息begin */
        public RelativeLayout relativeLayoutFrom;// chatting_from_layout_from
        /* 收到消息begin */
        public View initLeftViewStub_id;// chatting_from_layout_from
        // receive text
        public RelativeLayout relativeLayoutFromText;// chatting_from_layout_from_text
        public MyTextView myTextViewContentFromText;// chatting_msg_content_from_text
        public TextView tv_account_text_des;
        /**
         * 收发图文begin
         */
        public RelativeLayout rl_ImgTexMsg;
        /**
         * title
         */
        public TextView tv_ImgTexMsgTitle;
        /**
         * 图片
         */
        public ImageView iv_ImgTexMsgImage;
        /**
         * 描述
         */
        public TextView tv_ImgTexMsgDescription;
        /**
         * 收发图文end
         */
		/* 收到消息end */

		/* 自己发送消息 begin */
        public RelativeLayout relativeLayoutTo;
        // send text
        public RelativeLayout relativeLayoutToText;
        public ImageView imageViewSendingStautsTextImg;
        public ProgressBar imageViewSendingTextStauts;
        public MyTextView myTextViewContentText;
        public ImageView mMyHeadImg;
		/* 自己发送消息 end */

		/* coin change begin */
        /**
         * 最外层布局控件
         */
        public View ll_coin_change;
        /**
         * 充值状态图标
         */
        public ImageView img_coin_status;
        /**
         * 标题文字
         */
        public TextView tv_coin_title;
        /**
         * 显示更多的view
         */
        public View rl_coin_more_detail;
        /**
         * body布局
         */
        private LinearLayoutListView mLlistView;

        /**
         * 公众帐号时间
         */
        public TextView mtvPublicTime;

        public View view_divideline;
        public View view_fulltxt;
		/* coin change end */

    }

    private Handler handler = new Handler();

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
        if (isShowMenu)
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
                            mMsgsList.remove(position);
                            afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
                            ((AccountsChattingActivity) context).resendTextOrEmotion(afMessageInfo);
                            break;
                        default:
                            break;
                    }

                } else if (contentAry[selectIndex].equals(context.getResources().getString(R.string.copy))) {
                    if (v instanceof TextView) {
                        CommonUtils.copy((TextView) v, context);
                    }
                } else if (contentAry[selectIndex].equals(context.getResources().getString(R.string.delete))) {
                    mMsgsList.remove(afMessageInfo);
                    notifyDataSetChanged();
                    ((AccountsChattingActivity) context).delete(afMessageInfo);

                    if (msgType == AfMessageInfo.MESSAGE_VOICE && VoiceManager.getInstance().isPlaying()) {
                        VoiceManager.getInstance().pause();
                    }

                    // forward msg
                } else if (contentAry[selectIndex].equals(context.getResources().getString(R.string.forward))) {

                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.F_NUM);
                    // MobclickAgent.onEvent(context, ReadyConfigXML.F_NUM);

                    PalmchatLogUtils.println("forward received text fid:" + afMessageInfo.fid);
                    final int msgType2 = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
                    switch (msgType2) {
                        case AfMessageInfo.MESSAGE_TEXT:

                            CacheManager.getInstance().setForwardMsg(afMessageInfo);
                            Intent intent = new Intent(context, ForwardSelectActivity.class);
                            context.startActivity(intent);
                            break;

                        case AfMessageInfo.MESSAGE_IMAGE:
                            CacheManager.getInstance().setForwardMsg(afMessageInfo);
                            AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
                            if (null != afAttachImageInfo) {
                                String largeFilename = afAttachImageInfo.large_file_name;
                                // large_file has been downloaded
                                if (!CommonUtils.isEmpty(largeFilename)) {
                                    Intent intent2 = new Intent(context, ForwardSelectActivity.class);
                                    context.startActivity(intent2);
                                } else {
                                    // toImageActivity(afMessageInfo._id,
                                    // afAttachImageInfo, false, true);
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
    void showLongClickItem(int status, int msgType, AfMessageInfo entity, View v, int position) {

        // palmchat team
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
                if (msgType == AfMessageInfo.MESSAGE_TEXT) {
                    longClick(mContext, R.array.chat_msg_op3, entity, v, position);
                }
                if (msgType == AfMessageInfo.MESSAGE_PUBLIC_ACCOUNT) {
                    longClick(mContext, R.array.chat_msg_op3, entity, v, position);

                } else if (msgType == AfMessageInfo.MESSAGE_PA_URL) {
                    longClick(mContext, R.array.chat_msg_op31, entity, v, position);
                }
            }
        }
    }

    /**
     * 跳转到公共帐号详情页面
     *
     * @param afFriendInfo
     */
    private void toPublicAccountDetail(final AfFriendInfo afFriendInfo) {
        Intent intent = new Intent(mContext, PublicAccountDetailsActivity.class);
        intent.putExtra("Info", afFriendInfo);
        mContext.startActivityForResult(intent, IntentConstant.REQUEST_CODE_ACCOUNTSCHAT);
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
        return mMsgsList.get(getCount() - 1);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.chatting_accounts_imagetxtmsg_layout_id: {
                    Integer position = (Integer) v.getTag();
                    String url = getUrlWithStatistics(((AfAttachPAMsgInfo) mMsgsList.get(position).attach).url);
                    PalmchatLogUtils.i(AccountsChattingAdapter.class.getSimpleName(), "---URL---" + url);
                    Intent intent = new Intent();
                    intent.setClass(mContext, InnerNoAbBrowserActivity.class);
                    intent.putExtra(IntentConstant.RESOURCEURL, url);
                    ((Activity) mContext).startActivity(intent);
                    break;
                }

                default:
                    break;
            }
        }
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
        if (null != mMsgsList && !mMsgsList.isEmpty()) {
            long clientTime = getItem(position).client_time;
            if (clientTime != 0L) {
                Date date = new Date(clientTime);
                if (position == 0) {// 第一个位置显示时间
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
     * 拼url 用于统计
     *
     * @param url
     * @return
     */
    private String getUrlWithStatistics(String url) {

        try {
            String sex = (myProfileInfo.sex == Consts.AFMOBI_SEX_MALE ? "male" : "famale");
            url = url + "?" + "params=" + myProfileInfo.afId + "|" + sex + "|" + myProfileInfo.age + "|" + myProfileInfo.country + "|" + myProfileInfo.region + "|" + myProfileInfo.city + "|" + mContext.getResources().getConfiguration().locale.getLanguage() + "|Android" + Build.VERSION.RELEASE + "|" + Build.BRAND + "_" + Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        PalmchatLogUtils.i("WXL", "statistics=" + url);
        return url;
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

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        // TODO Auto-generated method stub
        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                case Consts.REQ_FLAG_POLLING_PA_GETDETAIL:
                    if (result != null) {
                        AfAttachPAMsgInfo afAttachPAMsgInfo = (AfAttachPAMsgInfo) result;
                        if (afAttachPAMsgInfo != null) {
                            ViewHolder viewHolder = (ViewHolder) user_data;
                            String url = (String) viewHolder.tv_ImgTexMsgTitle.getTag();
                            if (!afAttachPAMsgInfoHashMap.containsKey(url)) {
                                afAttachPAMsgInfoHashMap.put(url, afAttachPAMsgInfo);
                            }
                            setPAMsgInfo(afAttachPAMsgInfo, viewHolder);
                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
            Consts.getInstance().showToast(mContext, code, flag, http_code);
        }
    }

    /**
     * 设置图文消息
     */
    private void setPAMsgInfo(AfAttachPAMsgInfo afAttachPAMsgInfo, ViewHolder viewHolder) {
        if (null != afAttachPAMsgInfo.title) {
            viewHolder.tv_ImgTexMsgTitle.setText(afAttachPAMsgInfo.title);
        }

        if (null != afAttachPAMsgInfo.content) {
            viewHolder.tv_ImgTexMsgDescription.setText(Html.fromHtml(afAttachPAMsgInfo.content));
        }
        ImageManager.getInstance().DisplayImage(viewHolder.iv_ImgTexMsgImage, afAttachPAMsgInfo.imgurl, R.color.light_white, false, null);
        viewHolder.rl_ImgTexMsg.setVisibility(View.VISIBLE);
    }

    /**
     * 获取消息集合
     *
     * @return
     */
    public ArrayList<AfMessageInfo> getLists() {
        return mMsgsList;
    }

    /**
     * 进入订单详情wap页面
     */
    private void toOrderDetail(String url) {
        try {

            String sessionid = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession();
            String countryCode = AppUtils.getCCFromProfile_forRecharge(CacheManager.getInstance().getMyProfile());
            String language = SharePreferenceUtils.getInstance(mContext).getLocalLanguage();
            String password = "";
            AfLoginInfo[] myAccounts = mAfCorePalmchat.AfDbLoginGetAccount();
            if (myAccounts != null && myAccounts.length > 0) {
                AfLoginInfo afLoginInfo = myAccounts[0];
                if (afLoginInfo != null) {
                    password = afLoginInfo.password;
                    /** DESC + Base64 */
                    password = PalmchatApp.getApplication().mAfCorePalmchat.AfLoginEncode(password, sessionid);
                }
            }
            String imei = PalmchatApp.getOsInfo().getImei();
            String afid = CacheManager.getInstance().getMyProfile().afId;


            if (sessionid != null)
                sessionid = URLEncoder.encode(sessionid, "UTF-8");
            if (countryCode != null)
                countryCode = URLEncoder.encode(countryCode, "UTF-8");
            if (language != null)
                language = URLEncoder.encode(language, "UTF-8");
            if (password != null)
                password = URLEncoder.encode(password, "UTF-8");
            if (imei != null)
                imei = URLEncoder.encode(imei, "UTF-8");
            if (afid != null)
                afid = URLEncoder.encode(afid, "UTF-8");

            String mUrl = url;
            if (mUrl.contains("?")) {
                mUrl = mUrl + "&sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
            } else {
                mUrl = mUrl + "?sessionid=" + sessionid + "&countryCode=" + countryCode + "&language=" + language + "&password=" + password + "&imei=" + imei + "&afid=" + afid;
            }

            Intent it = new Intent(mContext, MyWapActivity.class);
            it.putExtra(IntentConstant.WAP_URL, mUrl);
            mContext.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}