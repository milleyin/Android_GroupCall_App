package com.afmobi.palmchat.ui.activity.social;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.FacebookConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.gif.GifImageView;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.SoftkeyboardActivity;
import com.afmobi.palmchat.ui.activity.main.ChatsContactsActivity;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.model.DialogItem;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog.ILargeImageDialog;
import com.afmobi.palmchat.ui.activity.publicaccounts.InnerNoAbBrowserActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountDetailsActivity;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.FlowLayout;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayoutEditText;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayoutEditText.IOnKeyboardStateChangedListener;
import com.afmobi.palmchat.ui.customview.ListDialog;
import com.afmobi.palmchat.ui.customview.ListDialog.OnItemClick;
import com.afmobi.palmchat.ui.customview.MyListView;
import com.afmobi.palmchat.ui.customview.MyScrollView;
import com.afmobi.palmchat.ui.customview.ScrollViewListener;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager.SystemBarConfig;
import com.afmobi.palmchat.ui.customview.TextViewFixTouchConsume;
import com.afmobi.palmchat.ui.customview.videoview.CustomVideoController;
import com.afmobi.palmchat.ui.customview.videoview.VedioManager;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.BroadcastUtil;
import com.afmobi.palmchat.util.ByteUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.DateUtil;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobi.palmchat.util.VoiceManager.OnCompletionListener;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobi.palmchat.util.universalimageloader.core.assist.FailReason;
import com.afmobi.palmchat.util.universalimageloader.core.listener.ImageLoadingListener;
import com.afmobigroup.gphone.R;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfCommentInfo;
import com.core.AfResponseComm.AfLikeInfo;
import com.core.AfResponseComm.AfMFileInfo;
import com.core.AfResponseComm.AfPeoplesChaptersList;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import de.greenrobot.event.EventBus;

public class BroadcastDetailActivity extends BaseActivity implements AfHttpResultListener, OnItemClick, OnClickListener, ScrollViewListener {

    private ImageView mHeadImg; // 博主的头像
    // private TextView mSexAgeTxt; // 性别
    private TextView mNameTxt;// 名字
    private ImageView pa_icon;// 官方账号标志
    private TextView mTimeTxt;
    private TextView mRangeTxt;// 距离
    private LinearLayout lin_range;
    private TextViewFixTouchConsume mMessageTxt;// 广播文字内容
    /* 转发者的Profile */
    RelativeLayout relative_ProfileForward;
    private ImageView mHeadImg_forward; // 博主的头像
    // private TextView mSexAgeTxt_forward; // 性别
    // private ImageView pa_icon_forward;////官方账号标志
    private TextView mNameTxt_forward;// 名字
    // private TextView mShareaPostText;// 显示Share A Post
    private TextView mTimeTxt_forward;
    private TextView mRangeTxt_forward;// 距离
    private LinearLayout lin_range_forward;
    private TextViewFixTouchConsume mMessageTxt_forward;// 转发者广播文字内容
    private GifImageView gifImageView;// 播放语音动画
    private Context mContext;
    private TextView mUnknowTxt;
    public static final int TYPE_BROADCASTLIST = 8000;// 来自广播列表
    public static final int TYPE_RETRY_BROADCASTLIST = 8001;// 自己发广播retry
    public static final int COMMENT_OPERATION = 8002;// 操作评论
    public static final int OTHER_OPERATION = 8003;// 举报等
    public static final int SHARE_BROADCAST_DETAIL = 8005;// 分享广播
    public static final int FORWARD_BROADCAST_DETAIL = 8006;// 转发广播
    private int act_type, act_comm_flag;
    /**
     * 从哪里进入的 来源不同 处理也不同 主要用于数据统计
     */
    public static final int FROM_DEFAULT = 0;
    public static final int FROM_BROADCAST = 1;
    public static final int FROM_FAR_FAR_AWAY = 2;
    public static final int FROM_PROFILE = 3;
    public static final int FROM_HOMEBROADCAST = 4;
    public static final int FROM_BC_COMMON = 5;
    public static final int FROM_NEARBY = 6;
    public static final int FROM_FRIENDCIRCLE = 7;
    public static final int FROM_HOTTODAY = 8;
    public static final int FROM_TAGPAGE = 9;
    public static final int FROM_OFFICIAL_ACCOUNT = 10;
    private int mFromTag;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private FlowLayout lin_pic;
    private LinearLayout lin_play_icon_to_voice;
    private ImageView play_icon_to_voice;
    private ImageView play_icon_to_voice_anim;
    private TextView play_time_to_voice;
    String to_afid = "", to_sname = "";
    private TextView txt_like, txt_comment_total;
    private View view_like, txt_comment_more, view_comment_more, lin_comment, view_bottom_cmd;
    private TextView txt_like_click, txt_comment, txt_forward;
    private MyListView listView_comment;
    private boolean isSingle = false;
    /**
     * Successful download suffix
     */
    private final String DOWNLOAD_SUCCESS_SUFFIX = "_COMPLETE";
    private CacheManager cacheManager;
    private AfProfileInfo profileInfo;
    private AfPalmchat mAfCorePalmchat;
    public AfChapterInfo afChapterInfo;
    private View back;
    private ImageView rightImageview;
    private TextView title;
    private ImageView[] imageViews = new ImageView[3];
    private ImageView[] imgeViews_paicon = new ImageView[3];
    private CommentAdapter adapter;
    private View send_button, vImageEmotion;
    private TextView vEditTextContent;
    public View input_box_area, chatting_options_layout;
    private KeyboardListenRelativeLayoutEditText bc_detail;
    private RelativeLayout relative_userProfile;
    private LinearLayout lin_msg_part;
    private MyScrollView my_sv;
    int softkeyboard_H = -1;
    private SystemBarTintManager tintManager;
    SystemBarConfig systemBarConfig;
    private ProgressBar more_comment_pb;
    private ProgressBar progressBar;
    private boolean isLoadMore;
    private boolean is_only_com = false;
    private int pageid = (int) System.currentTimeMillis() + new Random(10000).nextInt();
    private int START_INDEX = 0;
    private static final int LIMIT = 30;
    private static final int SET_AFCHAPTERINFO = 110;
    private static final int REFRESH_COMMENTADAAPTER = 111;
    private static final int SET_COMMENTADAAPTER = 112;
    private static final int ADD_COMMENTADAAPTER = 113;
    private String currentMsg = ""; // 当前的评论内容
    private LargeImageDialog largeImageDialog;// 图片广播 查看大图
    private int c_position = -1;
    int comment_count = 0;// 评论总数
    /**
     * 点击评论进入
     */
    private boolean mBl_FromComment;
    private int dimen_text_notexist;// 原文不存在时的间距
    private int color_text_content, color_text_content_share;// 原文广播不存在的文字颜色
    private int dimen_headsize, // 正常头像尺寸
            dimen_headsize_share;// 原文头像尺寸
    private float dimen_text_size, // 正常文字尺寸
            dimen_text_size_share;// 原文文字尺寸
    private float dimen_region_text_size, // 正常时间地区文字尺寸
            dimen_region_text_size_share;// 原文时间地区文字尺寸
    private AfFriendInfo friendInfo;//保存广播用户的信息
    //
//    /*视频相关*/
//    /*视频相关控件*/
    private ViewGroup fl_video_layout;
    private CustomVideoController custom_video_controller;
    private boolean isPause = false;
    private Rect rect;
    int iPaddingBroadcastMessage;//广播被转发时原文的边距等
    private View r_paofile, relativelayout_title;

    @Override
    public void findViews() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            afChapterInfo = (AfChapterInfo) bundle.getSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO);
            act_type = (int) bundle.getInt(JsonConstant.KEY_BC_SKIP_TYPE, TYPE_BROADCASTLIST);
            act_comm_flag = (int) bundle.getInt(JsonConstant.KEY_FLAG, -1);
        }
        setContentView(R.layout.broadcast_detail);
        r_paofile = findViewById(R.id.r_paofile);
        relativelayout_title = findViewById(R.id.relativelayout_title);
        mContext = this;
        cacheManager = CacheManager.getInstance();
        profileInfo = cacheManager.getMyProfile();
        mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
        mFromTag = intent.getIntExtra("FromPage", FROM_DEFAULT);
        mBl_FromComment = intent.getBooleanExtra(IntentConstant.FROMCOMMENT, false);
        send_button = findViewById(R.id.chatting_operate_one);
        input_box_area = findViewById(R.id.input_box_area);
        chatting_options_layout = findViewById(R.id.chatting_options_layout);
        vImageEmotion = findViewById(R.id.image_emotion);
        vEditTextContent = (TextView) findViewById(R.id.chatting_message_edit);
        vEditTextContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(CommonUtils.TEXT_MAX)});
        vEditTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == CommonUtils.TEXT_MAX) {
                    ToastManager.getInstance().show(mContext, R.string.comment_long);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tintManager = new SystemBarTintManager(this);
        systemBarConfig = tintManager.getConfig();

        vImageEmotion.setOnClickListener(this);
        vEditTextContent.setOnClickListener(this);
        bc_detail = (KeyboardListenRelativeLayoutEditText) findViewById(R.id.bc_detail);
        bc_detail.setOnKeyboardStateChangedListener(new IOnKeyboardStateChangedListener() {

            @Override
            public void onKeyboardStateChanged(int state, int mHeight) {
                switch (state) {
                    case KeyboardListenRelativeLayoutEditText.KEYBOARD_STATE_HIDE:// keyboard
                        // hide
                        PalmchatLogUtils.println("KEYBOARD_STATE_HIDE mHeight," + mHeight);
                        break;
                    case KeyboardListenRelativeLayoutEditText.KEYBOARD_STATE_SHOW:// keyboard
                        // show
                        PalmchatLogUtils.println("KEYBOARD_STATE_SHOW softkeyboard_H," + mHeight);
                        softkeyboard_H = mHeight;
                    default:
                        break;
                }
            }
        });

        my_sv = (MyScrollView) findViewById(R.id.my_sv);
        my_sv.setScrollViewListener(this);

        relative_userProfile = (RelativeLayout) findViewById(R.id.userprofile);
        lin_msg_part = (LinearLayout) findViewById(R.id.lin_msg_part);
        back = findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.title_text);
        title.setText(R.string.details);
        rightImageview = (ImageView) findViewById(R.id.op2);
        rightImageview.setVisibility(View.VISIBLE);
        rightImageview.setBackgroundResource(R.drawable.navigation);
        rightImageview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showItemDialog(OTHER_OPERATION);
            }
        });
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (bc_detail.hasKeyboard()) {
                    CommonUtils.closeSoftKeyBoard(vEditTextContent);
                }
                finish();
            }
        });

        // 如果是分享的 以下是分享的人的处理
        relative_ProfileForward = (RelativeLayout) findViewById(R.id.userprofile_forward);
        mHeadImg_forward = (ImageView) findViewById(R.id.head_img_forward);
        // pa_icon_forward = (ImageView) findViewById(R.id.pa_icon_forward);
        // mSexAgeTxt_forward = (TextView)
        // findViewById(R.id.sex_age_txt_forward);
        mNameTxt_forward = (TextView) findViewById(R.id.name_txt_forward);
        mTimeTxt_forward = (TextView) findViewById(R.id.time_txt_forward);
        mRangeTxt_forward = (TextView) findViewById(R.id.range_txt_forward);
        lin_range_forward = (LinearLayout) findViewById(R.id.lin_range_forward);
        mMessageTxt_forward = (TextViewFixTouchConsume) findViewById(R.id.message_txt_forward);
        // mShareaPostText = (TextView) findViewById(R.id.share_a_post_text);
        mMessageTxt_forward.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());
        mUnknowTxt = (TextView) findViewById(R.id.type_unknow_textView);
        CommonUtils.setUnKnownSpanLink(this, mUnknowTxt, InnerNoAbBrowserActivity.class);
        mHeadImg = (ImageView) findViewById(R.id.head_img);
        lin_pic = (FlowLayout) findViewById(R.id.lin_pic);
        // mSexAgeTxt = (TextView) findViewById(R.id.sex_age_txt);
        pa_icon = (ImageView) findViewById(R.id.pa_icon);
        mNameTxt = (TextView) findViewById(R.id.name_txt);
        mTimeTxt = (TextView) findViewById(R.id.time_txt);
        mRangeTxt = (TextView) findViewById(R.id.range_txt);
        lin_range = (LinearLayout) findViewById(R.id.lin_range);
        mMessageTxt = (TextViewFixTouchConsume) findViewById(R.id.message_txt);
        mMessageTxt.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());

        gifImageView = (GifImageView) findViewById(R.id.gif);
        lin_play_icon_to_voice = (LinearLayout) findViewById(R.id.lin_play_icon_to_voice);
        play_icon_to_voice = (ImageView) findViewById(R.id.play_icon_to_voice);
        play_icon_to_voice_anim = (ImageView) findViewById(R.id.play_icon_to_voice_anim);
        play_time_to_voice = (TextView) findViewById(R.id.play_time_to_voice);

        txt_like = (TextView) findViewById(R.id.txt_like);
        txt_comment_total = (TextView) findViewById(R.id.comment_total);
        view_like = findViewById(R.id.view_like);
        view_bottom_cmd = findViewById(R.id.layout_bottom_cmd);

        txt_comment_more = findViewById(R.id.txt_comment_more);
        view_comment_more = findViewById(R.id.view_comment_more);
        listView_comment = (MyListView) findViewById(R.id.lv_comment);
        more_comment_pb = (ProgressBar) findViewById(R.id.bar);
        progressBar = (ProgressBar) findViewById(R.id.img_loading);
        lin_comment = findViewById(R.id.lin_comment);

        txt_like_click = (TextView) findViewById(R.id.txt_like_click);
        txt_comment = (TextView) findViewById(R.id.txt_comment);
        txt_forward = (TextView) findViewById(R.id.txt_forward);
        /*初始化视频相关控件*/
        fl_video_layout = (ViewGroup) findViewById(R.id.fl_video_layout);
        custom_video_controller = (CustomVideoController) findViewById(R.id.custom_video_controller);

        mNameTxt.setFocusable(true);
        mNameTxt.setFocusableInTouchMode(true);
        mNameTxt.requestFocus();

        imageViews[0] = (ImageView) findViewById(R.id.like_head1);
        imageViews[1] = (ImageView) findViewById(R.id.like_head2);
        imageViews[2] = (ImageView) findViewById(R.id.like_head3);

        imgeViews_paicon[0] = (ImageView) findViewById(R.id.like_head1_paicon);
        imgeViews_paicon[1] = (ImageView) findViewById(R.id.like_head2_paicon);
        imgeViews_paicon[2] = (ImageView) findViewById(R.id.like_head3_paicon);

        txt_comment_more.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (progressBar.getVisibility() == View.GONE) {
                    loadmore();
                }
            }
        });

        color_text_content = mContext.getResources().getColor(R.color.text_level_1);
        color_text_content_share = mContext.getResources().getColor(R.color.text_level_1);
        dimen_text_notexist = (int) mContext.getResources().getDimension(R.dimen.broadcast_share_text_notexist);
        dimen_headsize = (int) mContext.getResources().getDimension(R.dimen.d_broadcast_headszie);
        dimen_headsize_share = (int) mContext.getResources().getDimension(R.dimen.d_broadcast_headszie_share);
        dimen_text_size = mContext.getResources().getDimension(R.dimen.d_broadcast_nameszie);
        dimen_text_size_share = mContext.getResources().getDimension(R.dimen.d_broadcast_nameszie_share);
        dimen_region_text_size = mContext.getResources().getDimension(R.dimen.d_broadcast_desc_sp);
        dimen_region_text_size_share = mContext.getResources().getDimension(R.dimen.d_broadcast_desc_sp_share);
        iPaddingBroadcastMessage = getResources().getDimensionPixelSize(R.dimen.d_broadcast_item_margin);
    }

    @Override
    public void init() {
        if (act_type != TYPE_RETRY_BROADCASTLIST) {
            progressBar.setVisibility(View.VISIBLE);
            rightImageview.setVisibility(View.GONE);
            lin_comment.setVisibility(View.VISIBLE);
            loadData();
            if (adapter == null) {
                adapter = new CommentAdapter(mContext, new ArrayList<AfCommentInfo>(), CommentAdapter.BROADCAST_DETAIL);
                listView_comment.setAdapter(adapter);
            }

            txt_like_click.setOnClickListener(this);
            txt_comment.setOnClickListener(this);
            txt_forward.setOnClickListener(this);

        } else {
            input_box_area.setVisibility(View.GONE);
            lin_comment.setVisibility(View.GONE);
        }
        setComment_clickable();// 设置当在取数据时 无法点赞等操作
        setAfChapterInfoAndShareInfo(afChapterInfo);
        to_afid = "";
        to_sname = "";

        if (mBl_FromComment) {
            txt_comment.performClick();
        }

    }

    private void setAfChapterInfoAndShareInfo(AfChapterInfo afChapterInfo) {

        if (BroadcastUtil.isShareBroadcast(afChapterInfo)) {// 存在分享

            if (afChapterInfo.share_del != DefaultValueConstant.BROADCAST_SHARE_DELETE_FLAG && afChapterInfo.share_info != null) {// 原文存在不
                bindView(afChapterInfo.share_info, false, true); // 有分享的时候
                // 绑定内容为被分享的Chapter
                // ----设置原文作者的Profile
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(iPaddingBroadcastMessage, 0, iPaddingBroadcastMessage, 0);
                relative_userProfile.setLayoutParams(lp);
                relative_userProfile.setPadding(iPaddingBroadcastMessage, iPaddingBroadcastMessage, iPaddingBroadcastMessage, iPaddingBroadcastMessage);
                relative_userProfile.setBackgroundResource(R.color.base_back);
                relative_userProfile.setOnClickListener(BroadcastDetailActivity.this);
                lin_msg_part.setOnClickListener(BroadcastDetailActivity.this);
                if (afChapterInfo.share_info.getType() == Consts.BR_TYPE_UNKNOW) {//如果原文类型不可识别
                    mUnknowTxt.setVisibility(View.VISIBLE);//广播类型不可识别的提示
                } else {
                    mUnknowTxt.setVisibility(View.GONE);
                }
            } else {// 当原文已经被删除的时候
                bindView(afChapterInfo.share_info, true, true); // 有分享的时候
                mUnknowTxt.setVisibility(View.GONE);                                            // 绑定内容为被分享的Chapter
                relative_userProfile.setVisibility(View.GONE);
                mMessageTxt.setVisibility(View.VISIBLE);
                mMessageTxt.setText(R.string.the_broadcast_doesnt_exist);
                mMessageTxt.setPadding(0, dimen_text_notexist, 0, dimen_text_notexist);
            }
            mMessageTxt.setTextColor(color_text_content_share);
            mMessageTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_text_size_share);
            // ----设置原文作者的消息内容
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp2.setMargins(iPaddingBroadcastMessage, 0, iPaddingBroadcastMessage, 0);
            lin_msg_part.setLayoutParams(lp2);

            if (afChapterInfo.share_del != DefaultValueConstant.BROADCAST_SHARE_DELETE_FLAG && afChapterInfo.share_info != null
                    && afChapterInfo.share_info.getType() != Consts.BR_TYPE_UNKNOW
                    && afChapterInfo.share_info.getType() != Consts.BR_TYPE_IMAGE_TEXT
                    && afChapterInfo.share_info.getType() != Consts.BR_TYPE_IMAGE
                    && afChapterInfo.share_info.getType() != Consts.BR_TYPE_VIDEO_TEXT
                    && afChapterInfo.share_info.getType() != Consts.BR_TYPE_VIDEO) {
                lin_msg_part.setPadding(0, 0, 0, iPaddingBroadcastMessage);
            }
            lin_msg_part.setBackgroundResource(R.color.base_back);

            // -----转发者的Profile设置
            relative_ProfileForward.setVisibility(View.VISIBLE);
            bindForwarderUser(afChapterInfo);// 绑定转发者的Profile

            mHeadImg.getLayoutParams().height = dimen_headsize_share;
            mHeadImg.getLayoutParams().width = dimen_headsize_share;
            mNameTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_text_size_share);
            mTimeTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_region_text_size_share);
        } else {// 无分享
            relative_userProfile.setPadding(iPaddingBroadcastMessage, iPaddingBroadcastMessage, iPaddingBroadcastMessage, iPaddingBroadcastMessage / 2);
            relative_userProfile.setBackgroundResource(R.color.white);
            bindView(afChapterInfo, false, false);
            if (afChapterInfo.getType() == Consts.BR_TYPE_UNKNOW) {//如果 类型不可识别
                mUnknowTxt.setVisibility(View.VISIBLE);//广播类型不可识别的提示
            } else {
                mUnknowTxt.setVisibility(View.GONE);
            }
            mHeadImg.getLayoutParams().height = dimen_headsize;
            mHeadImg.getLayoutParams().width = dimen_headsize;
            mNameTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_text_size);
            mTimeTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_region_text_size);
            mRangeTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_region_text_size);
            mMessageTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_text_size);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SET_AFCHAPTERINFO:// 从服务器获得完整信息后 刷新界面
                    AfChapterInfo afChapterInfo = (AfChapterInfo) msg.obj;
                    setAfChapterInfoAndShareInfo(afChapterInfo);
                    bindComment(afChapterInfo);
                    bindLike(afChapterInfo);
                    setComment_clickable();
                    sendUpdateBroadcastclikeForResult(afChapterInfo);
                    break;
                case REFRESH_COMMENTADAAPTER:// 获取更多评论后 刷新评论
                    ArrayList<AfCommentInfo> afCommentInfos = (ArrayList<AfCommentInfo>) msg.obj;
                    refresh_CommentAdapter(afCommentInfos);
                    break;
                case SET_COMMENTADAAPTER:// 设置评论适配器
                    ArrayList<AfCommentInfo> arrayList = (ArrayList<AfCommentInfo>) msg.obj;
                    if (adapter == null) {
                        adapter = new CommentAdapter(mContext, arrayList, CommentAdapter.BROADCAST_DETAIL);
                        listView_comment.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged(arrayList, true);
                    }
                    break;
                case ADD_COMMENTADAAPTER:// 发一个评论后刷新
                    AfCommentInfo afCommentInfo = (AfCommentInfo) msg.obj;
                    adapter.addCommentToFirst(afCommentInfo);
                    break;
            }
        }

        ;
    };

    /**
     * 发广播告诉所有的接收者，这个广播详细真实数据，所有接收者 更新
     *
     * @param afChapterInfo
     */
    private void sendUpdateBroadcastclikeForResult(AfChapterInfo afChapterInfo) {
        afChapterInfo.eventBus_action = Constants.UPDATE_LIKE;
        EventBus.getDefault().post(afChapterInfo);
    }

    private void loadmore() {
        isLoadMore = true;
        is_only_com = true;
        txt_comment_more.setVisibility(View.GONE);
        more_comment_pb.setVisibility(View.VISIBLE);
        START_INDEX++;
        loadDataFromServer(pageid);
    }

    /**
     * 取广播详情 和评论
     */
    private void loadData() {
        isLoadMore = false;
        START_INDEX = 0;
        pageid++;
        loadDataFromServer(pageid);
    }

    /**
     * 评论列表要有pageID进行分页
     *
     * @param pageId
     */
    private void loadDataFromServer(int pageId) {
        int page_start = (START_INDEX * LIMIT);
        mAfCorePalmchat.AfHttpBcgetChaptersByMid(pageId, page_start, LIMIT, afChapterInfo.mid, is_only_com, null, this);
    }

    /**
     * 自己是否点过赞
     *
     * @param isLike
     */
    public void bindisLike(boolean isLike) {
        if (isLike) {
            txt_like_click.setSelected(true);
            txt_like_click.setClickable(false);
        } else {
            txt_like_click.setSelected(false);
            txt_like_click.setClickable(true);
        }
    }

    /**
     * 当是转发的内容时 这里处理转发者的信息
     *
     * @param info
     */
    private void bindForwarderUser(AfChapterInfo info) {

        if (info.profile_Info != null) {
            mNameTxt_forward.setText(info.profile_Info.name);
            // int age = info.profile_Info.age;
            // byte sex = info.profile_Info.sex;
            // mShareaPostText.setVisibility(View.VISIBLE);
            // mSexAgeTxt_forward.setText(String.valueOf(age));
            // mSexAgeTxt_forward.setBackgroundResource(Consts.AFMOBI_SEX_MALE
            // == sex ? R.drawable.bg_sexage_male :
            // R.drawable.bg_sexage_female);
            // mSexAgeTxt_forward.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE
            // == sex ? R.drawable.icon_sexage_boy :
            // R.drawable.icon_sexage_girl, 0, 0, 0);
            // if(info.profile_Info.user_class==DefaultValueConstant.BROADCAST_PROFILE_PA){
            // pa_icon_forward.setVisibility(View.VISIBLE);
            // }
            if (mHeadImg_forward != null) {
                ImageManager.getInstance().DisplayAvatarImage(mHeadImg_forward, info.getServerUrl(), info.afid, Consts.AF_HEAD_MIDDLE, info.profile_Info.sex, info.getSerialFromHead(), null);
                mHeadImg_forward.setOnClickListener(new MyClick(info));
                mNameTxt_forward.setOnClickListener(new MyClick(info));
            }
        }

        mTimeTxt_forward.setText(DateUtil.getTimeAgo(mContext, info.time));

        bind_disply_lat_lng(info, mRangeTxt_forward);
        if (TextUtils.isEmpty(mRangeTxt_forward.getText())) {
            lin_range_forward.setVisibility(View.GONE);
        } else {
            lin_range_forward.setVisibility(View.VISIBLE);
        }

        if (info.share_flag == DefaultValueConstant.BROADCAST_SHARE_NOCOMMENT || TextUtils.isEmpty(info.content)) {
            mMessageTxt_forward.setVisibility(View.GONE);
        } else {
            mMessageTxt_forward.setVisibility(View.VISIBLE);
            CharSequence text = EmojiParser.getInstance(mContext).parseEmojiTags(mContext, info.content, info.list_tags, CommonUtils.dip2px(mContext, 24));
            mMessageTxt_forward.setText(text);
        }

    }

    /**
     * @param info
     * @param isShareAndNotExist 当是分享的内容 且广播原文不存在的时候为true
     */
    private void bindView(AfChapterInfo info, boolean isShareAndNotExist, boolean isShared) {
        if (isShareAndNotExist) {// 当原文本删除的时候 点赞部分还是可以动用的
            //String likes = (info.list_likes.size()>99?"99+":String.valueOf(info.list_likes.size()));
            String likes = String.valueOf(info.list_likes.size());
            txt_like.setText(likes);
            view_like.setOnClickListener(this);
        } else {
            mNameTxt.setOnClickListener(new MyClick(info));
            mHeadImg.setOnClickListener(new MyClick(info));
            if (info.profile_Info != null) {
                mNameTxt.setText(info.profile_Info.name);
                if (info.profile_Info.user_class == DefaultValueConstant.BROADCAST_PROFILE_PA) {
                    pa_icon.setVisibility(View.VISIBLE);
                }
                // int age = info.profile_Info.age;
                // byte sex = info.profile_Info.sex;
                // mSexAgeTxt.setText(String.valueOf(age));
                // mSexAgeTxt.setBackgroundResource(Consts.AFMOBI_SEX_MALE ==
                // sex ? R.drawable.bg_sexage_male :
                // R.drawable.bg_sexage_female);
                // mSexAgeTxt.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE
                // == sex ? R.drawable.icon_sexage_boy :
                // R.drawable.icon_sexage_girl, 0, 0, 0);
                // WXl 20151013 调UIL的显示头像方法, 替换原来的AvatarImageInfo.统一图片管理
                ImageManager.getInstance().DisplayAvatarImage(mHeadImg, info.getServerUrl(), info.afid, Consts.AF_HEAD_MIDDLE, info.profile_Info.sex, info.getSerialFromHead(), null);
            }

            mTimeTxt.setText(DateUtil.getTimeAgo(mContext, info.time));

            lin_comment.setTag(info);
            bind_disply_lat_lng(info, mRangeTxt);
            if (TextUtils.isEmpty(mRangeTxt.getText())) {
                lin_range.setVisibility(View.GONE);
            } else {
                lin_range.setVisibility(View.VISIBLE);
            }
            // CharSequence text =
            // EmojiParser.getInstance(mContext).parse(info.content);
            // mMessageTxt.setText(text);

            if (act_type == TYPE_RETRY_BROADCASTLIST) {// Retry
                view_like.setVisibility(View.GONE);
                view_bottom_cmd.setVisibility(View.GONE);
            } else {
                view_like.setVisibility(View.VISIBLE);
                view_bottom_cmd.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(info.content)) {
                mMessageTxt.setVisibility(View.GONE);
            } else {
                mMessageTxt.setVisibility(View.VISIBLE);
                mMessageTxt.setTextColor(color_text_content);
                mMessageTxt.setPadding(0, 0, 0, 0);
                // 解析表情和Tags
                CharSequence text = EmojiParser.getInstance(mContext).parseEmojiTags(mContext, info.content, info.list_tags, CommonUtils.dip2px(mContext, 24));
                mMessageTxt.setText(text);
                CommonUtils.setUrlSpanLink(this, mMessageTxt);
            }
            //String likes = (info.list_likes.size()>99?"99+":String.valueOf(info.list_likes.size()));
            String likes = String.valueOf(info.list_likes.size());
            txt_like.setText(likes);
            view_like.setOnClickListener(this);
            send_button.setOnClickListener(this);
            // txt_other_btn.setOnClickListener(new MyClick(info));
            bindPictureView(info);
            bindVoiceView(info);
            bindVideoView(info, isShared);
        }

    }

    public void bindLike(final AfChapterInfo info) {

        if (info != null) {
            int like_count = info.list_likes.size();
            AfLikeInfo afLikeInfo = null;
            BroadcastUtil.ServerData_AfDBListAfLikeInfoInsert(Consts.DATA_BROADCAST_PAGE, info._id, info.list_likes);
            for (int i = imageViews.length - 1; i >= 0; i--) {
                if (i < like_count) {
                    afLikeInfo = info.list_likes.get(i);
                    if (afLikeInfo.afid.equals(profileInfo.afId)) {
                        info.isLike = true;
                    }
                    imageViews[i].setVisibility(View.VISIBLE);
                    if (afLikeInfo != null) {
                        // WXl 20151013 调UIL的显示头像方法, 替换原来的AvatarImageInfo.统一图片管理
                        ImageManager.getInstance().DisplayAvatarImage(imageViews[i], afLikeInfo.profile_Info.getServerUrl(), afLikeInfo.afid, Consts.AF_HEAD_MIDDLE, afLikeInfo.profile_Info.sex, afLikeInfo.profile_Info.getSerialFromHead(), null);
                        if (DefaultValueConstant.BROADCAST_PROFILE_PA == afLikeInfo.profile_Info.user_class) {
                            imgeViews_paicon[i].setVisibility(View.VISIBLE);
                        } else {
                            imgeViews_paicon[i].setVisibility(View.GONE);
                        }
                    }
                } else {
                    imageViews[i].setVisibility(View.GONE);
                }
            }
            //String likes = (info.total_like>99?"99+":String.valueOf(info.total_like));
            String likes = String.valueOf(info.total_like);
            txt_like.setText(likes);
            bindisLike(info.isLike);
        }
    }

    /**
     * 设置评论总数
     *
     * @param comment_count
     */
    public void setComment_count(int comment_count) {
        //String commentCount = (comment_count>99?"99+":String.valueOf(comment_count));
        String commentCount = String.valueOf(comment_count);
        String total = (mContext.getString(R.string.bc_comment_total)).replace(ReplaceConstant.TARGET_FOR_REPLACE, commentCount);
        txt_comment_total.setText(total);
    }

    public void bindComment(final AfChapterInfo info) {
        if (info != null) {
            comment_count = info.total_comment;
            setComment_count(comment_count);
            txt_comment_total.setVisibility(View.VISIBLE);
            if (comment_count > 3) {
                if (LIMIT < comment_count) {
                    view_comment_more.setVisibility(View.VISIBLE);
                } else {
                    view_comment_more.setVisibility(View.GONE);
                }
            } else {
                view_comment_more.setVisibility(View.GONE);
            }
            Message msg = new Message();
            msg.what = SET_COMMENTADAAPTER;
            msg.obj = info.list_comments;
            handler.sendMessage(msg);
            listView_comment.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, final View view, final int position, long arg3) {
                    if (progressBar.getVisibility() == View.GONE) {
                        AfCommentInfo afCommentInfo = adapter.getItem(position);
                        String hint_name = mContext.getString(R.string.reply_xxxx).replace(Constants.REPLY_STRING, afCommentInfo.profile_Info.name + Constants.REPLY_STRING_DIVIED);
                        if (afCommentInfo.afid.equals(profileInfo.afId)) { // 点击自己的评论内容
                            to_afid = "";
                            to_sname = "";
                            commentOrReply("");
                        } else { // 点击别人的评论内容
                            to_afid = afCommentInfo.afid;
                            if (afCommentInfo.profile_Info != null) {
                                to_sname = afCommentInfo.profile_Info.name;
                            }
                            commentOrReply(hint_name);
                        }
                        setting_scrollBy(view);
                    }

                }
            });
            listView_comment.setOnItemLongClickListener(new OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (progressBar.getVisibility() == View.GONE) {
                        c_position = position;
                        showItemDialog(COMMENT_OPERATION);
                    }
                    return true;
                }
            });
            listView_comment.requestDisallowInterceptTouchEvent(true);
            lin_comment.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 删除评论
     *
     * @param info
     * @param position
     */
    public void del_comment(AfChapterInfo info, int position) {
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_BCMCOM);
        // MobclickAgent.onEvent(mContext, ReadyConfigXML.DEL_BCMCOM);
        AfCommentInfo afCommentInfo = adapter.getItem(position);
        String afId = afCommentInfo.afid;
        String c_id = afCommentInfo.comment_id;
        if (TextUtils.isEmpty(c_id)) {
            ToastManager.getInstance().show(mContext, R.string.DeleteFailed);
        } else {
            showProgressDialog(mContext.getString(R.string.deleting));
            mAfCorePalmchat.AfHttpBCMsgOperate(Consts.REQ_BCCOMMENT_DELETE, afId, "", info.mid, null, c_id, position, BroadcastDetailActivity.this);
        }
    }

    private void refresh_CommentAdapter(ArrayList<AfCommentInfo> afCommentInfos) {
        if (afCommentInfos != null) {
            adapter.notifyDataSetChanged(afCommentInfos, isLoadMore);
            lin_comment.setVisibility(View.VISIBLE);
            afChapterInfo.list_comments.addAll(afCommentInfos);
        } else {
            if (isLoadMore && is_only_com) {// wxl 服务器上的数据会出现评论数11条，实际评论数为9条的情况，
                // 这里的判断用于这个情况下 show more的错误修复
                view_comment_more.setVisibility(View.GONE);
                txt_comment_more.setVisibility(View.GONE);
                more_comment_pb.setVisibility(View.GONE);
            }
            return;
        }
        if (isLoadMore && is_only_com) {
            if (adapter.getCount() >= afChapterInfo.total_comment) {
                view_comment_more.setVisibility(View.GONE);
            } else {
                view_comment_more.setVisibility(View.VISIBLE);
            }
            txt_comment_more.setVisibility(View.VISIBLE);
            more_comment_pb.setVisibility(View.GONE);
        }
    }

    private void bindPictureView(final AfChapterInfo info) {
        int Padding = getResources().getDimensionPixelSize(R.dimen.d_broadcast_item_margin);
        if (info.getType() == Consts.BR_TYPE_IMAGE_TEXT || info.getType() == Consts.BR_TYPE_IMAGE) {
            lin_pic.removeAllViews();
            final ArrayList<AfMFileInfo> al_afFileInfos = info.list_mfile;
            int list_mfile_size = al_afFileInfos.size();
            if (list_mfile_size > 1) {
                isSingle = false;
            } else {
                isSingle = true;
            }


            int[] picRule = AdapterBroadcastUitls.getImageRule_Size_cut(info, ImageUtil.DISPLAYW);// 计算各个图片的宽高和裁切
            lin_pic.setPicRule(picRule);

            for (int i = 0; i < list_mfile_size; i++) {
                if (null != lin_pic) {
                    final ImageView imageView = new ImageView(mContext);
                    imageView.setTag(i);
                    final AfMFileInfo mfileInfo = al_afFileInfos.get(i);
                    String thumb_url = mfileInfo.thumb_url;
                    if (!TextUtils.isEmpty(thumb_url)) {
                        thumb_url = CacheManager.getInstance().getThumb_url(thumb_url, isSingle, info == null ? null : info.pic_rule);

                        final int inx = i;
                        ImageManager.getInstance().DisplayImage(imageView, thumb_url, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                AdapterBroadcastUitls.setDefaultImageLayoutParams(imageView, isSingle, al_afFileInfos.get(inx).url, mfileInfo, info.pic_rule);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }

                        });

                        imageView.setScaleType(ScaleType.CENTER_CROP);
                        imageView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (VoiceManager.getInstance().isPlaying()) {
                                    VoiceManager.getInstance().pause();
                                }

                                if (VedioManager.getInstance().getVideoController() != null) {
                                    VedioManager.getInstance().getVideoController().stop();
                                }

                                int img_index = (Integer) v.getTag();
                                PalmchatLogUtils.e("--BroadCast--", al_afFileInfos.get(img_index).url);
                                if (BroadcastUtil.isShareBroadcast(afChapterInfo) && afChapterInfo.share_info != null) {
                                    largeImageDialog = new LargeImageDialog(mContext, al_afFileInfos, img_index, LargeImageDialog.TYPE_BROADCASTLIST, afChapterInfo.share_info, act_type);
                                } else {
                                    largeImageDialog = new LargeImageDialog(mContext, al_afFileInfos, img_index, LargeImageDialog.TYPE_BROADCASTLIST, afChapterInfo, act_type);
                                }
                                largeImageDialog.show();
                                largeImageDialog.setILargeImageDialog(new ILargeImageDialog() {

                                    @Override
                                    public void onItemClickeReportAbuse() {
                                        String msg = mContext.getString(R.string.sure_report_abuse);
                                        showAppDialog(msg, Consts.REQ_BCMSG_ACCUSATION, afChapterInfo, false);
                                    }

                                    @Override
                                    public void onItemClickeDelete() {
                                        String msg = mContext.getString(R.string.bc_del);
                                        showAppDialog(msg, Consts.REQ_BCMSG_DELETE, afChapterInfo, true);

                                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_BCM);
                                        // MobclickAgent.onEvent(mContext,
                                        // ReadyConfigXML.DEL_BCM);
                                    }
                                });
                            }
                        });
                    } else {// 正在发送的广播详情
                        Bitmap bm = ImageManager.getInstance().loadLocalImageSync(al_afFileInfos.get(i).local_thumb_path, true);//ImageUtil.readBitMap(al_afFileInfos.get(i).local_thumb_path);
                        imageView.setScaleType(ScaleType.CENTER_CROP);
                        AdapterBroadcastUitls.setImageLayoutParams(imageView, bm, isSingle, al_afFileInfos.get(i));
                        imageView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int img_index = (Integer) v.getTag();
                                PalmchatLogUtils.e("--BroadCast--", al_afFileInfos.get(img_index).url);
                                largeImageDialog = new LargeImageDialog(mContext, al_afFileInfos, img_index, LargeImageDialog.TYPE_BROADCASTLIST, afChapterInfo, act_type);
                                largeImageDialog.show();
                            }
                        });
                    }
                    lin_pic.addView(imageView);
                }
            }
            lin_pic.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(Padding, Padding, Padding, Padding);
            view_like.setLayoutParams(lp);
        } else {
            lin_pic.setVisibility(View.GONE);
            /** 更具设计效果在没有图片的情况下距离上面的是24dp,有图片的话就是正常的12dp */
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(Padding, Padding * 2, Padding, Padding);
            view_like.setLayoutParams(lp);
        }
    }

    private void bindVoiceView(final AfChapterInfo info) {
        String voicePaht = "";
        if (info.getType() == Consts.BR_TYPE_VOICE_TEXT || info.getType() == Consts.BR_TYPE_VOICE) {
            final AfMFileInfo afMFileInfo = info.list_mfile.get(0);
            if (afMFileInfo == null) {
                return;
            }
            String url = afMFileInfo.url;
            if (!TextUtils.isEmpty(url)) {
                final String voiceName = PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(afMFileInfo.url);
                voicePaht = RequestConstant.VOICE_CACHE + voiceName;
                boolean exists = IsVoiceFileExistsSDcard(voicePaht);
                if (exists) {
                    info.download_success = true;
                    voicePaht = voicePaht + DOWNLOAD_SUCCESS_SUFFIX;
                } else {
                    info.download_success = false;
                }
            } else {
                voicePaht = afMFileInfo.local_img_path;
            }
            if (TextUtils.isEmpty(voicePaht)) {
                return;
            }
            File file = new File(voicePaht);
            showStopAnim(file.getPath());
            if (lin_play_icon_to_voice != null) {
                lin_play_icon_to_voice.setOnClickListener(new MyClick(info, file));
            }

            if (play_icon_to_voice != null) {
                play_icon_to_voice.setOnClickListener(new MyClick(info, file));
            }

            if (play_icon_to_voice_anim != null) {
                play_icon_to_voice_anim.setOnClickListener(new MyClick(info, file));
            }
            if (!TextUtils.isEmpty(url)) { // Server data
                if (info.download_success) {
                    lin_play_icon_to_voice.setBackgroundResource(R.drawable.broadcast_voiceloadp);
                    lin_play_icon_to_voice.setGravity(Gravity.LEFT);
                    gifImageView.setVisibility(View.GONE);
                    play_icon_to_voice.setVisibility(View.VISIBLE);
                    play_icon_to_voice_anim.setVisibility(View.VISIBLE);
                    play_time_to_voice.setVisibility(View.VISIBLE);
                } else {
                    downloadVoice(info, file);
                }

            } else { // Our data
                lin_play_icon_to_voice.setBackgroundResource(R.drawable.broadcast_voiceloadp);
                lin_play_icon_to_voice.setGravity(Gravity.LEFT);
                gifImageView.setVisibility(View.GONE);
                play_icon_to_voice.setVisibility(View.VISIBLE);
                play_icon_to_voice_anim.setVisibility(View.VISIBLE);
                play_time_to_voice.setVisibility(View.VISIBLE);
            }
            play_time_to_voice.setTag(voicePaht);
            if (gifImageView.getVisibility() == View.GONE) {
                lin_play_icon_to_voice.setClickable(true);
                play_icon_to_voice.setClickable(true);
                play_icon_to_voice_anim.setClickable(true);
                if (file.exists() && file.length() > 0) {
                    int nVoiceTime = 0;
                    if (!TextUtils.isEmpty(info.desc)) {
                        try {
                            nVoiceTime = Integer.valueOf(info.desc);
                        } catch (Exception e) {
                            nVoiceTime = 0;
                        }
                    } else {
                        BigDecimal voicetime = CommonUtils.getBigDecimalCount(VoiceManager.getInstance().getVoiceTime(voicePaht));
                        nVoiceTime = voicetime.intValue();
                    }

                    if (play_time_to_voice != null) {
                        if (nVoiceTime == 0) {
                            play_icon_to_voice.setClickable(false);
                            lin_play_icon_to_voice.setClickable(false);
                            play_icon_to_voice_anim.setClickable(false);
                        } else {
                            play_icon_to_voice.setClickable(true);
                            lin_play_icon_to_voice.setClickable(true);
                            play_icon_to_voice_anim.setClickable(true);
                        }
                        play_time_to_voice.setText(nVoiceTime + "s");
                    }
                } else {
                    if (play_time_to_voice != null) {
                        play_time_to_voice.setText("");
                    }
                }
            } else {
                lin_play_icon_to_voice.setClickable(false);
                play_icon_to_voice.setClickable(false);
                play_icon_to_voice_anim.setClickable(false);
            }
            lin_play_icon_to_voice.setVisibility(View.VISIBLE);
        } else {
            lin_play_icon_to_voice.setVisibility(View.GONE);
        }

    }

    /**
     * 视频显示
     *
     * @param info
     */
    private void bindVideoView(final AfChapterInfo info, boolean isShared) {
        if (info.getType() == Consts.BR_TYPE_VIDEO_TEXT || info.getType() == Consts.BR_TYPE_VIDEO) {
            AfResponseComm.AfMFileInfo afMFileInfo = info.list_mfile.get(0);
            if (afMFileInfo == null) {
                return;
            }


            /*根据url 里的图片尺寸对布局的高度进行设置*/
            if (!TextUtils.isEmpty(afMFileInfo.url)) {
                try {
                    int video_w = CommonUtils.split_source_w(afMFileInfo.url);
                    int video_h = CommonUtils.split_source_h(afMFileInfo.url);
                    int msgPartWidth = ImageUtil.DISPLAYW - (isShared ? iPaddingBroadcastMessage * 2 : 0);
                    int real_h = msgPartWidth * video_h / video_w;
                    real_h = CommonUtils.checkDisplayRatio_h(msgPartWidth, real_h);
                    ViewGroup.LayoutParams layout = fl_video_layout.getLayoutParams();
                    if (layout.height != real_h) {
                        layout.height = real_h;
                        fl_video_layout.setLayoutParams(layout);
                    }
                } catch (Exception e) {//防止URL中没带宽高参数时做的保护
                    e.printStackTrace();
                }
            }

            custom_video_controller.setFlag(info.mid + this.getClass().getName());
            custom_video_controller.setTime(afMFileInfo.duration * 1000);
            if (afMFileInfo.url != null)
                custom_video_controller.setPath(afMFileInfo.url);
            /*缩略图地址*/
            String thumb_url = afMFileInfo.thumb_url;
            if (!TextUtils.isEmpty(thumb_url))
                custom_video_controller.setImgFirstFrame(thumb_url);

            fl_video_layout.setVisibility(View.VISIBLE);

        } else {
            fl_video_layout.setVisibility(View.GONE);
        }


    }

    private void intoLikeListActivity() {
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo);
        Intent intent2 = new Intent(mContext, BroadcastLikeActivity.class);
        intent2.putExtras(bundle2);
        startActivityForResult(intent2, IntentConstant.DELETE_BROADCAST);
    }

    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (VedioManager.getInstance().getVideoController() != null) {
            if (!VedioManager.getInstance().getVideoController().getVisibleRect(rect)) {
                VedioManager.getInstance().getVideoController().stop();
            }
        }
    }

    class MyClick implements OnClickListener {
        private File file;
        private AfChapterInfo afChapterInfo;

        public MyClick() {
        }

        public MyClick(AfChapterInfo info) {
            this.afChapterInfo = info;
        }

        public MyClick(AfChapterInfo info, File file) {
            this.afChapterInfo = info;
            this.file = file;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lin_play_icon_to_voice:
                case R.id.play_icon_to_voice:
                case R.id.play_icon_to_voice_anim:
                    Play(afChapterInfo, file);
                    break;
                case R.id.head_img:
                case R.id.head_img_forward:
                case R.id.name_txt:
                case R.id.name_txt_forward:
                    if (afChapterInfo == null || afChapterInfo.profile_Info == null) {
                        break;
                    }
                    afChapterInfo.profile_Info.afId = afChapterInfo.afid;
                    AfProfileInfo profile = AfFriendInfo.friendToProfile(afChapterInfo.profile_Info);
                    if (DefaultValueConstant.BROADCAST_PROFILE_PA == profile.user_class) {
                        Intent intent = new Intent(mContext, PublicAccountDetailsActivity.class);
                        intent.putExtra("Info", profile);
                        mContext.startActivity(intent);
                    } else if (profile != null && profileInfo != null && !TextUtils.isEmpty(profile.afId) && profile.afId.equals(profileInfo.afId)) {

                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_MMPF);
                        // MobclickAgent.onEvent(mContext,
                        // ReadyConfigXML.ENTRY_MMPF);

                        Bundle bundle = new Bundle();
                        bundle.putString(JsonConstant.KEY_AFID, profile.afId);
                        Intent intent = new Intent(mContext, MyProfileActivity.class);
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);

                    } else {

                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BCM_T_PF);
                        // MobclickAgent.onEvent(mContext, ReadyConfigXML.BCM_T_PF);

                        Intent intent = new Intent(mContext, ProfileActivity.class);
                        intent.putExtra(JsonConstant.KEY_PROFILE, (Serializable) profile);
                        intent.putExtra(JsonConstant.KEY_FLAG, true);
                        intent.putExtra(JsonConstant.KEY_AFID, profile.afId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }

                    break;
                default:
                    break;
            }

        }

    }

    private AnimationDrawable animDrawable;

    private void startPlayAnim(final View view, final View playIcon) {
        playIcon.setBackgroundResource(R.drawable.voice_pause_icon);
        view.post(new Runnable() {
            @Override
            public void run() {
                animDrawable = ((AnimationDrawable) mContext.getResources().getDrawable(R.anim.playing_from_voice_frame));
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    view.setBackgroundDrawable(animDrawable);
                } else {
                    view.setBackground(animDrawable);
                }
                if (animDrawable != null && !animDrawable.isRunning()) {
                    animDrawable.setOneShot(false);
                    animDrawable.start();
                }
            }
        });
    }

    private String showStopAnim(final String path) {
        if (!path.equals(VoiceManager.getInstance().getPath()) && VoiceManager.getInstance().getPlayer() != null && VoiceManager.getInstance().getPlayer().isPlaying()) {
            stopPlayAnim(play_icon_to_voice_anim, play_icon_to_voice);
            Log.e("showStopAnim", "stopPlayAnim");

        } else if (path.equals(VoiceManager.getInstance().getPath()) && VoiceManager.getInstance().getPlayer() != null && VoiceManager.getInstance().getPlayer().isPlaying()) {
            startPlayAnim(play_icon_to_voice_anim, play_icon_to_voice);
            Log.e("showStopAnim", "startPlayAnim");
        } else {
            stopPlayAnim(play_icon_to_voice_anim, play_icon_to_voice);
            Log.e("showStopAnim", "stopPlayAnim");

        }
        return path;
    }

    public void stopPlayAnim(final View view, final View playIcon) {
        view.setBackgroundResource(R.drawable.voice_anim01);
        playIcon.setBackgroundResource(R.drawable.chatting_voice_player_icon);
    }

    /**
     * 播放语音
     *
     * @param afChapterInfo
     * @param file22
     */
    public void Play(final AfChapterInfo afChapterInfo, final File file22) {
        if (afChapterInfo != null) {
            String voicePaht = (String) play_time_to_voice.getTag();
            final File file = new File(voicePaht);
            if (file.exists() && file.length() > 0) {
                boolean isPlay = VoiceManager.getInstance().isPlaying();
                if (isPlay) {
                    VoiceManager.getInstance().pause();
                    return;
                }

                VoiceManager.getInstance().setView(play_icon_to_voice_anim);
                VoiceManager.getInstance().setPlayIcon(play_icon_to_voice);
                VoiceManager.getInstance().setmActivity(BroadcastDetailActivity.this);
                VoiceManager.getInstance().play(file.getPath(), new OnCompletionListener() {
                    @Override
                    public Object onGetContext() {
                        return mContext;
                    }

                    @Override
                    public void onCompletion() {
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(mContext, mContext.getString(R.string.audio_error), Toast.LENGTH_SHORT).show();
                        downloadVoice(afChapterInfo, file);
                    }
                });

                if (VoiceManager.getInstance().isPlaying()) {
                    startPlayAnim(play_icon_to_voice_anim, play_icon_to_voice);
                }
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.no_audio_file), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 发广播通知 各个页面这个广播被删除了
     *
     * @param afChapterInfo
     */
    private void sendUpdate_delect_BroadcastList(AfChapterInfo afChapterInfo) {
        afChapterInfo.eventBus_action = Constants.UPDATE_DELECT_BROADCAST;
        EventBus.getDefault().post(afChapterInfo);
    }

    /**
     * download Voice file
     */
    public void downloadVoice(final AfChapterInfo afChapterInfo, final File file) {
        final String voice_path = file.getPath();
        lin_play_icon_to_voice.setBackgroundResource(R.drawable.broadcast_voiceload);
        lin_play_icon_to_voice.setGravity(Gravity.CENTER);
        gifImageView.setVisibility(View.VISIBLE);
        play_icon_to_voice.setVisibility(View.GONE);
        play_icon_to_voice_anim.setVisibility(View.GONE);
        play_time_to_voice.setVisibility(View.GONE);
        final AfMFileInfo info = afChapterInfo.list_mfile.get(0);
        PalmchatLogUtils.e("downloadVoice", info.url);
        if (!CacheManager.getInstance().getGifDownLoadMap().containsKey(info.url)) {
            CacheManager.getInstance().getGifDownLoadMap().put(info.url, voice_path);
            String voiceUrl = info.url;
            if (!voiceUrl.startsWith(JsonConstant.HTTP_HEAD)) {
                voiceUrl = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerInfo()[Constants.CORE_SERVER_BROADCAST_MEDIA_HOST] + info.url;
            }
            PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBroadcastDownload(voiceUrl, voice_path, null, new AfHttpResultListener() {
                @Override
                public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
                    PalmchatLogUtils.d("AfOnResult========", "flag," + flag + "code=" + code + ",http_code=" + http_code);
                    if (code == Consts.REQ_CODE_SUCCESS) {
                        File newFile = new File(voice_path + DOWNLOAD_SUCCESS_SUFFIX);
                        boolean falg = file.renameTo(newFile);
                        if (falg) {
                            String download_success_voice_path = newFile.getPath();
                            lin_play_icon_to_voice.setBackgroundResource(R.drawable.broadcast_voiceloadp);
                            lin_play_icon_to_voice.setGravity(Gravity.LEFT);
                            gifImageView.setVisibility(View.GONE);
                            play_icon_to_voice.setVisibility(View.VISIBLE);
                            play_icon_to_voice_anim.setVisibility(View.VISIBLE);
                            play_time_to_voice.setVisibility(View.VISIBLE);
                            if (play_time_to_voice != null) {
                                if (!TextUtils.isEmpty(afChapterInfo.desc)) {
                                    play_time_to_voice.setText(afChapterInfo.desc + "s");
                                } else {
                                    BigDecimal voicetime = CommonUtils.getBigDecimalCount(VoiceManager.getInstance().getVoiceTime(download_success_voice_path));
                                    play_time_to_voice.setText(voicetime.intValue() + "s");
                                }

                                play_time_to_voice.setTag(download_success_voice_path);
                            }
                            afChapterInfo.download_success = true;
                            lin_play_icon_to_voice.setClickable(true);
                            play_icon_to_voice.setClickable(true);
                        } else {
                            afChapterInfo.download_success = false;
                        }
                    } else {
                        lin_play_icon_to_voice.setBackgroundResource(R.drawable.broadcast_voiceload);
                        lin_play_icon_to_voice.setGravity(Gravity.CENTER);
                        gifImageView.setVisibility(View.VISIBLE);
                        play_icon_to_voice.setVisibility(View.GONE);
                        play_icon_to_voice_anim.setVisibility(View.GONE);
                        play_time_to_voice.setVisibility(View.GONE);

                        lin_play_icon_to_voice.setClickable(false);
                        play_icon_to_voice.setClickable(false);
                        afChapterInfo.download_success = false;
                    }
                    if (CacheManager.getInstance().getGifDownLoadMap().containsKey(info.url)) {
                        CacheManager.getInstance().getGifDownLoadMap().remove(info.url);
                    }
                }
            });
        }
    }

    public boolean IsVoiceFileExistsSDcard(String voicePath) {
        boolean falg = false;
        if (!TextUtils.isEmpty(voicePath)) {
            String path = voicePath + DOWNLOAD_SUCCESS_SUFFIX;
            File file = new File(path);
            if (file.exists()) {
                falg = true;
            } else {
                falg = false;
            }
        }
        return falg;
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        PalmchatLogUtils.e(BroadcastDetailActivity.class.getCanonicalName(), "----flag:" + flag + "----code:" + code + "----result:" + result);
        cacheManager.getsendTime(System.currentTimeMillis()).toString();
        if (code == Consts.REQ_CODE_SUCCESS) {
            int status = AfMessageInfo.MESSAGE_SENT;
            switch (flag) {
                case Consts.REQ_BCCOMMENT_DELETE:
                    if (user_data != null) {
                        dismissProgressDialog();
                        ToastManager.getInstance().show(mContext, R.string.success);
                        int position = (Integer) user_data;
                        mAfCorePalmchat.AfDBBCCommentDeleteByID(adapter.afInfos.get(position)._id);
                        adapter.afInfos.remove(position);
                        adapter.notifyDataSetChanged();
                    /* modify by zhh 2015-04-09 */

                        afChapterInfo.list_comments.remove(position);
                        afChapterInfo.total_comment = afChapterInfo.total_comment - 1;
                        setComment_count(afChapterInfo.total_comment);
                        sendUpdateBroadcastclikeForResult(afChapterInfo);
                    }
                    break;
                case Consts.REQ_BCGET_COMMENTS_BY_MID:
                    if (result != null) {
                        if (isLoadMore && is_only_com) {// 查看更多评论的时候
                            AfResponseComm afResponseComm = (AfResponseComm) result;
                            AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
                            if (afPeoplesChaptersList.list_chapters.size() > 0) {
                                AfChapterInfo temp_afChapterInfo = afPeoplesChaptersList.list_chapters.get(0);
                                // refresh_CommentAdapter(temp_afChapterInfo.list_comments);
                                Message msg = new Message();
                                msg.what = REFRESH_COMMENTADAAPTER;
                                msg.obj = temp_afChapterInfo.list_comments;
                                handler.sendMessage(msg);
                            } else {
                                txt_comment_more.setVisibility(View.VISIBLE);
                                more_comment_pb.setVisibility(View.GONE);
                            }
                        } else {// 刷详情的时候
                            AfResponseComm afResponseComm = (AfResponseComm) result;
                            AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
                            AfChapterInfo temp_afChapterInfo = afPeoplesChaptersList.list_chapters.get(0);
                            progressBar.setVisibility(View.GONE);
                            rightImageview.setVisibility(View.VISIBLE);
                        /*
                         * txt_like_click.setClickable(true);
						 * txt_comment.setClickable(true);
						 * txt_forward.setClickable(true);
						 */

                            if (temp_afChapterInfo.mid != null) {
                                temp_afChapterInfo._id = afChapterInfo._id;
                                boolean isChanged = false;
                                if (temp_afChapterInfo.list_likes.size() > 0) {
                                    if (temp_afChapterInfo.list_likes.size() != afChapterInfo.list_likes.size()) {
                                        isChanged = true;// like有改变
                                    }
                                    afChapterInfo.total_like = temp_afChapterInfo.total_like;
                                    afChapterInfo.list_likes.clear();
                                    afChapterInfo.list_likes.addAll(temp_afChapterInfo.list_likes);

                                } else {
                                    temp_afChapterInfo.total_like = afChapterInfo.total_like;
                                    temp_afChapterInfo.list_likes.addAll(afChapterInfo.list_likes);
                                }
                                if (temp_afChapterInfo.list_comments.size() > 0) {
                                    if (temp_afChapterInfo.list_comments.size() != afChapterInfo.list_comments.size()) {
                                        isChanged = true;// 评论有改变
                                    }
                                    afChapterInfo.list_comments.clear();
                                    afChapterInfo.list_comments.addAll(temp_afChapterInfo.list_comments);
                                } else {
                                    temp_afChapterInfo.total_comment = afChapterInfo.total_comment;
                                    temp_afChapterInfo.list_comments.addAll(afChapterInfo.list_comments);
                                }
                                if (temp_afChapterInfo.isLike != afChapterInfo.isLike) {
                                    isChanged = true;
                                }
                                temp_afChapterInfo.isLike = afChapterInfo.isLike;

                                // if(isChanged){//当有改变的时候才去重新刷
                                byte content_flag = afChapterInfo.content_flag;// 这里这样处理
                                // 是因为服务器刷详情的时候不会带content_flag
                                afChapterInfo = temp_afChapterInfo;
                                afChapterInfo.content_flag = content_flag;
                                Message msg = new Message();
                                msg.what = SET_AFCHAPTERINFO;
                                msg.obj = temp_afChapterInfo;
                                handler.sendMessage(msg);
                                // }
                            }
                        }
                    }
                    break;
                case Consts.REQ_BCMSG_AGREE:
                    if (user_data != null && user_data instanceof AfChapterInfo) {
                        AfChapterInfo afChapterInfo = (AfChapterInfo) user_data;
                        if (afChapterInfo != null && afChapterInfo.list_likes.size() > 0) {
                            int _id = afChapterInfo.list_likes.get(0)._id;
                            mAfCorePalmchat.AfDBBCLikeUpdateStatusByID(status, _id);
                            mAfCorePalmchat.AfBcLikeFlagSave(afChapterInfo.mid);
                            sendUpdateBroadcastclikeForResult(afChapterInfo);

                            switch (mFromTag) {
                                case FROM_BROADCAST:
                                case FROM_HOMEBROADCAST:
                                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LIKE_BCM);
                                    // MobclickAgent.onEvent(mContext,
                                    // ReadyConfigXML.LIKE_BCM);
                                    break;

                                case FROM_FAR_FAR_AWAY:
                                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LIKE_BCM_FAR);
                                    // MobclickAgent.onEvent(mContext,
                                    // ReadyConfigXML.LIKE_BCM_FAR);
                                    break;

                                case FROM_PROFILE:
                                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LIKE_BCM_PF);
                                    // MobclickAgent.onEvent(mContext,
                                    // ReadyConfigXML.LIKE_BCM_PF);
                                    break;

                                default:
                                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LIKE_BCM);
                                    // MobclickAgent.onEvent(mContext,
                                    // ReadyConfigXML.LIKE_BCM);
                                    break;
                            }
                        }

                    }
                    break;
                case Consts.REQ_BCMSG_COMMENT:
                    if (user_data != null) {
                        int c_db_id = (Integer) user_data;
                        if (afChapterInfo != null && afChapterInfo.list_comments.size() > 0) {
                            // int c_db_id = afChapterInfo.list_comments.get(0)._id;
                            mAfCorePalmchat.AfDBBCCommentUpdateStatusByID(status, c_db_id);
                            if (result != null) {
                                mAfCorePalmchat.AfDBBCCommentUpdateCidByID(String.valueOf(result), c_db_id);
                                afChapterInfo.list_comments.get(0).comment_id = String.valueOf(result);
                                sendUpdateBroadcastclikeForResult(afChapterInfo);

                                switch (mFromTag) {
                                    case FROM_BROADCAST:
                                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.COM_BCM);
                                        // MobclickAgent.onEvent(mContext,
                                        // ReadyConfigXML.COM_BCM);
                                        break;

                                    case FROM_FAR_FAR_AWAY:
                                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.COM_BCM_FAR);
                                        // MobclickAgent.onEvent(mContext,
                                        // ReadyConfigXML.COM_BCM_FAR);
                                        break;

                                    case FROM_PROFILE:
                                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.COM_BCM_PF);
                                        // MobclickAgent.onEvent(mContext,
                                        // ReadyConfigXML.COM_BCM_PF);
                                        break;

                                    default:
                                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.COM_BCM);
                                        // MobclickAgent.onEvent(mContext,
                                        // ReadyConfigXML.COM_BCM);
                                        break;
                                }

                            }
                        }
                    }
                    break;
                case Consts.REQ_BCMSG_ACCUSATION:// 举报

                    switch (mFromTag) {
                        case FROM_BROADCAST:
                            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.REPORT_BCM);
                            // MobclickAgent.onEvent(mContext,
                            // ReadyConfigXML.REPORT_BCM);
                            break;

                        case FROM_FAR_FAR_AWAY:
                            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.REPORT_BCM_FAR);
                            // MobclickAgent.onEvent(mContext,
                            // ReadyConfigXML.REPORT_BCM_FAR);
                            break;

                        case FROM_PROFILE:
                            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.REPORT_BCM_PF);
                            // MobclickAgent.onEvent(mContext,
                            // ReadyConfigXML.REPORT_BCM_PF);
                            break;

                        default:
                            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.REPORT_BCM);
                            // MobclickAgent.onEvent(mContext,
                            // ReadyConfigXML.REPORT_BCM);
                            break;
                    }

                    dismissProgressDialog();
                    ToastManager.getInstance().show(mContext, R.string.report_success);
                    break;
                case Consts.REQ_BCMSG_DELETE:
                    dismissProgressDialog();

                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_BCM_SUCC);
                    // MobclickAgent.onEvent(mContext, ReadyConfigXML.DEL_BCM_SUCC);

                    ToastManager.getInstance().show(mContext, R.string.bc_del_success);
                    if (largeImageDialog != null) {
                        if (largeImageDialog.isShowing()) {
                            largeImageDialog.dismiss();
                        }
                    }
                    sendUpdate_delect_BroadcastList(afChapterInfo);
                    mAfCorePalmchat.AfDBBCChapterDeleteByID(afChapterInfo._id);
                    this.finish();
                    break;
                default:
                    break;
            }
        } else {
            dismissProgressDialog();
            switch (flag) {
                case Consts.REQ_BCMSG_ACCUSATION:
                case Consts.REQ_BCMSG_DELETE:
                    dismissProgressDialog();
                    if (code != Consts.REQ_CODE_UNNETWORK && (code == Consts.REQ_CODE_202 || code == Consts.REQ_CODE_201 || code == Consts.REQ_CODE_142)) {
                        finish();
                        sendUpdate_delect_BroadcastList(afChapterInfo);
                    }
                    break;
                case Consts.REQ_BCGET_COMMENTS_BY_MID:
                    txt_comment_more.setVisibility(View.VISIBLE);
                    more_comment_pb.setVisibility(View.GONE);

                    progressBar.setVisibility(View.GONE);
                    rightImageview.setVisibility(View.VISIBLE);
                    setComment_clickable();//根据需求 获取失败的情况 也可以点赞评论
                    if (code == Consts.REQ_CODE_142 || code == Consts.REQ_CODE_201 || code == Consts.REQ_CODE_202 // 广播不存在
                            ) {
                        if (afChapterInfo.ds_type != Consts.DATA_FROM_LOCAL) {
                            finish();
                            sendUpdate_delect_BroadcastList(afChapterInfo);
                        }
                    }
                    break;
                case Consts.REQ_BCMSG_AGREE:// 点赞失败处理
                    if (code == Consts.REQ_CODE_142 || code == Consts.REQ_CODE_201 || code == Consts.REQ_CODE_202) {// 文字不存在或已删除
                        finish();
                        sendUpdate_delect_BroadcastList(afChapterInfo);
                    } else {// 重复点赞-169， 广播过期-203，网络失败4096 等其他原因失败的处理
                        AfChapterInfo afChapterInfo2 = (AfChapterInfo) user_data;
                        if (code == Consts.REQ_CODE_169) {// 重读点赞
                            mAfCorePalmchat.AfBcLikeFlagSave(afChapterInfo.mid);
                        } else {
                            afChapterInfo.isLike = false;
                        }
                        if (afChapterInfo2 != null && afChapterInfo2.list_likes.size() > 0) {
                            afChapterInfo2.total_like--;
                            int l_db_id = afChapterInfo2.list_likes.get(0)._id;
                            mAfCorePalmchat.AfDBBCLikeDeleteByID(l_db_id);
                            afChapterInfo2.list_likes.remove(0);
                            bindLike(afChapterInfo);
                        }
                    }
                    break;
                case Consts.REQ_BCMSG_COMMENT:// 评论失败处理
                    if (code == Consts.REQ_CODE_UNNETWORK || code == Consts.REQ_CODE_203) {// 广播超过30天过期
                        if (user_data != null) {
                            int c_db_id = (Integer) user_data;
                            mAfCorePalmchat.AfDBBCCommentDeleteByID(c_db_id);
                            afChapterInfo.total_comment--;
                            setComment_count(afChapterInfo.total_comment);
                            int index = ByteUtils.indexOfAfChapterInfo_listCommentBy_id(afChapterInfo.list_comments, c_db_id);
                            if (index > -1) {
                                afChapterInfo.list_comments.remove(index);
                            }
                            adapter.notifyDataSetChanged(afChapterInfo.list_comments);
//						Message msg = new Message();
//						msg.what = REFRESH_COMMENTADAAPTER;
//						msg.obj = null;//;afChapterInfo.list_comments;
//						handler.sendMessage(msg);
                        }
                    } else if (code == Consts.REQ_CODE_142 || code == Consts.REQ_CODE_201 || code == Consts.REQ_CODE_202) {// 广播已被删除
                        finish();
                        sendUpdate_delect_BroadcastList(afChapterInfo); // 刷新广播列表
                    }
                    break;
                case Consts.REQ_BCCOMMENT_DELETE:// 删除广播失败
                    if (user_data != null) {
                        if (code == Consts.REQ_CODE_142 || code == Consts.REQ_CODE_202) {// 广播不存在或已被删除
                            dismissProgressDialog();
                            ToastManager.getInstance().show(mContext, R.string.success);
                            int position = (Integer) user_data;
                            adapter.afInfos.remove(position);
                            adapter.notifyDataSetChanged();
                            afChapterInfo.list_comments.remove(position);
                            afChapterInfo.total_comment = afChapterInfo.total_comment - 1;
                            setComment_count(afChapterInfo.total_comment);
                            sendUpdateBroadcastclikeForResult(afChapterInfo);
                            return;
                        }
                    }
                    break;
            }
            if (code == Consts.REQ_CODE_104) {// PageID过期
                PalmchatLogUtils.e(BroadcastDetailActivity.class.getCanonicalName(), "----code:" + code);
                loadData();
            } else {
                if (afChapterInfo.ds_type != Consts.DATA_FROM_LOCAL) { // 只要不是自己本地发的广播
                    Consts.getInstance().showToast(context, code, flag, http_code);
                }
            }

        }
    }

    /**
     * zhh 进行评论或者回复
     *
     * @param hint_content 为空或者@name,评论时hint内容
     */
    private void commentOrReply(String hint_content) {
        input_box_area.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(hint_content)) { // @他人时
            vEditTextContent.setHint(hint_content);
        }
        toSoftkeyboardActivity(false, hint_content);

    }

    public void setFace(int id, String data) {
    }

    public void setting_scrollBy(final View view) {
        softkeyboard_H = SharePreferenceUtils.getInstance(mContext).getSoftkey_h() + input_box_area.getHeight();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                int[] location = new int[2];// 保存当前坐标的数组
                view.getLocationOnScreen(location);// 获取选中的 Item 在屏幕中的位置，以左上角为原点
                // (0, 0)

                int OldListY = location[1];// Y 坐标

                int y = (OldListY - softkeyboard_H) + input_box_area.getHeight() + view.getHeight() - systemBarConfig.getStatusBarHeight();
                PalmchatLogUtils.println("softkeyboard_H=" + softkeyboard_H + ", OldListY=" + OldListY + ",getStatusBarHeight=" + ",Y=" + y);

                my_sv.smoothScrollBy(0, y);


            }
        }, 300);
    }

    private void showItemDialog(int Operation_type) {
        List<DialogItem> items = new ArrayList<DialogItem>();
        if (Operation_type != FORWARD_BROADCAST_DETAIL) {

            if (profileInfo.afId.equals(afChapterInfo.afid)) {
                if (Operation_type == COMMENT_OPERATION) {
                    items.add(new DialogItem(R.string.del_comment, R.layout.custom_dialog_image, 0, R.drawable.icon_list_delete));
                } else {
                    // if(act_type != TYPE_RETRY_BROADCASTLIST){
                    items.add(new DialogItem(R.string.delete, R.layout.custom_dialog_image, 0, R.drawable.icon_list_delete));
                    // }else {
                    // items.add(new DialogItem(R.string.cancel,
                    // R.layout.custom_dialog_image, 0,
                    // R.drawable.icon_list_delete));
                    // }
                }
            } else {
                if (Operation_type == COMMENT_OPERATION) {
                    AfCommentInfo afCommentInfo = adapter.getItem(c_position);
                    if (afCommentInfo != null) {
                        if (afCommentInfo.afid.equals(profileInfo.afId)) {
                            items.add(new DialogItem(R.string.del_comment, R.layout.custom_dialog_image, 0, R.drawable.icon_list_delete));
                        }
                    }
                }
            }
            if (act_type != TYPE_RETRY_BROADCASTLIST) {
                if (!profileInfo.afId.equals(afChapterInfo.afid) && Operation_type == OTHER_OPERATION) {
                    items.add(new DialogItem(R.string.report_abuse, R.layout.custom_dialog_image, 0, R.drawable.broadcast_dialog_abuse));
                }
            } else {
                items.add(new DialogItem(R.string.retry, R.layout.custom_dialog_image, 0, R.drawable.icon_list_reply));
            }
            if (Operation_type == COMMENT_OPERATION) {
                items.add(new DialogItem(R.string.reply_comment, R.layout.custom_dialog_image, 0, R.drawable.icon_list_reply));
            }
        } else {
            if (Operation_type != COMMENT_OPERATION && act_type != TYPE_RETRY_BROADCASTLIST// 如果是还没发出去的广播
                    && !(afChapterInfo.getType() == Consts.BR_TYPE_VIDEO_TEXT || afChapterInfo.getType() == Consts.BR_TYPE_VIDEO)) { // send to friend
                items.add(new DialogItem(R.string.broadcast_send_to_friend, R.layout.custom_dialog_image, 0, R.drawable.broadcast_dialog_send_to_friends));
            }
            if (CommonUtils.isAppExist(FacebookConstant.FACEBOOKLITE_PACKAGE)) {
                items.add(new DialogItem(R.string.broadcast_sharetofblite, R.layout.custom_dialog_image, 0, R.drawable.icon_sheet_share_to_facebook));
            }
            items.add(new DialogItem(R.string.broadcast_tagpage_sharebroadcast, R.layout.custom_dialog_image, 0, R.drawable.icon_sheet_share_to_broadcast));
            boolean mBl_IsFbExist = CommonUtils.isAppExist(FacebookConstant.FACEBOOKPACKAGE);
            if (mBl_IsFbExist) {
                items.add(new DialogItem(R.string.share_to_facebook, R.layout.custom_dialog_image, 0, R.drawable.icon_sheet_share_to_facebook));
            }
        }
        // items.add(new DialogItem(R.string.cancel,
        // R.layout.custom_dialog_image));

        ListDialog dialog = new ListDialog(this, items);
        dialog.setItemClick(this);
        dialog.show();
    }

    @Override
    public void onItemClick(DialogItem item) {
        // TODO Auto-generated method stub
        if (item.getTextId() == R.string.delete// 删除广播
            // ||item.getTextId() == R.string.cancel
                ) {
            /*点击删除时 停止正在播放的视频*/
            if (VedioManager.getInstance().getVideoController() != null) {
                VedioManager.getInstance().getVideoController().stop();
            }
            if (act_type != TYPE_RETRY_BROADCASTLIST) {
                String msg = mContext.getString(R.string.bc_del);
                showAppDialog(msg, Consts.REQ_BCMSG_DELETE, afChapterInfo, true);

                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_BCM);
                // MobclickAgent.onEvent(mContext, ReadyConfigXML.DEL_BCM);

            } else {
                if (VoiceManager.getInstance().isPlaying()) {
                    VoiceManager.getInstance().pause();
                }


                int code = mAfCorePalmchat.AfDBBCChapterDeleteByID(afChapterInfo._id);
                CacheManager.getInstance().getBC_resend_HashMap().remove(afChapterInfo._id);
                if (code >= 0) {
                    EventBus.getDefault().post(afChapterInfo);
                    this.finish();
                }
            }
        } else if (item.getTextId() == R.string.report_abuse) {

            String msg = mContext.getString(R.string.sure_report_abuse);
            showAppDialog(msg, Consts.REQ_BCMSG_ACCUSATION, afChapterInfo, false);

        } else if (item.getTextId() == R.string.retry) {
            CacheManager.getInstance().getBC_resend_HashMap().remove(afChapterInfo._id);
            this.finish();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    BroadcastUtil.getInstance().resendBroadcast(mContext, afChapterInfo);
                }
            }, 300);
            // CacheManager.getInstance().getBC_sendFailed().remove(0);
        } else if (item.getTextId() == R.string.del_comment) {
            if (c_position > -1) {
                del_comment(afChapterInfo, c_position);
            }
        } else if (item.getTextId() == R.string.reply_comment) {
            // listView_comment.performItemClick(listView_comment, position,
            // id);
            if (progressBar.getVisibility() == View.GONE) {
                AfCommentInfo afCommentInfo = adapter.getItem(c_position);

                to_afid = afCommentInfo.afid;
                to_sname = afCommentInfo.profile_Info.name;
                // opne_inputbox(hint_name3);
                if (afCommentInfo.afid.equals(profileInfo.afId)) { // 点击自己评论内容
                    to_afid = "";
                    to_sname = "";
                    commentOrReply("");
                } else { // 点击他人评论内容
                    String hint_name = mContext.getString(R.string.reply_xxxx).replace(Constants.REPLY_STRING, afCommentInfo.profile_Info.name + Constants.REPLY_STRING_DIVIED);
                    to_afid = afCommentInfo.afid;
                    to_sname = afCommentInfo.profile_Info.name;
                    commentOrReply(hint_name);
                }
                View view = listView_comment.getChildAt(c_position);
                if (isLoadMore) {
                    setting_scrollBy(view);
                }
            }
        } else if (item.getTextId() == R.string.broadcast_send_to_friend) {
            // if (afChapterInfo.share_del ==
            // DefaultValueConstant.BROADCAST_SHARE_DELETE_FLAG) {// 转发的
            // ToastManager.getInstance().show(this,
            // R.string.the_broadcast_doesnt_exist);
            // } else {

            List<MainAfFriendInfo> tempList = CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).getList();//获取最近联系人的数据
            List<AfFriendInfo> mAfFriendInfosList = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).getList();//获取联系人(好友)的数据
            List<AfGrpProfileInfo> grp_cacheList = CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).getList();//获取群组列表数据
            if (tempList.size() <= 0 && mAfFriendInfosList.size() <= 0) {
                ToastManager.getInstance().show(mContext, R.string.no_friends_or_group_chat);
            } else {
                int tplist = 0;//非官方账号的时候就++
                for (int i = 0; i < tempList.size(); i++) {
                    MainAfFriendInfo info = tempList.get(i);
                    AfFriendInfo afFriendInfo = info.afFriendInfo;
                    if (null != info && null != afFriendInfo) {
                        if (afFriendInfo.afId.toLowerCase().startsWith("g") || afFriendInfo.afId.toLowerCase().startsWith("r")) {
                            continue;
                        }
                    }
                    tplist++;
                }
                int infolist = 0;
                for (int s = 0; s < mAfFriendInfosList.size(); s++) {
                    AfFriendInfo afFriendInfo = mAfFriendInfosList.get(s);
                    if (null != afFriendInfo) {
                        if (afFriendInfo.afId.toLowerCase().startsWith("g") || afFriendInfo.afId.toLowerCase().startsWith("r")) {
                            continue;
                        }
                    }
                    infolist++;
                }

                if (tplist > 0 || infolist > 0 || grp_cacheList.size() > 0) {
                    if (afChapterInfo != null) {
                        Intent intent = new Intent(mContext, ChatsContactsActivity.class);
                        intent.putExtra(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo);
                        intent.putExtra(JsonConstant.KEY_SHARE_ID, afChapterInfo.mid);
                        intent.putExtra(JsonConstant.KEY_IS_SHARE_TAG, false);
                        intent.putExtra(JsonConstant.KEY_BROADCAST_CONTENT, afChapterInfo.content);
                        intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_AFID, afChapterInfo.afid);
                        if (afChapterInfo.profile_Info != null) {
                            intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_NAME, afChapterInfo.profile_Info.name);
                            intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_HEADER_URL, afChapterInfo.profile_Info.head_img_path);
                        }
                        if (afChapterInfo.list_mfile != null && afChapterInfo.list_mfile.size() > 0) {
                            intent.putExtra(JsonConstant.KEY_TAG_URL, afChapterInfo.list_mfile.get(0).thumb_url);
                        }
                /*
                 * else if(afChapterInfo.share_info != null &&
				 * afChapterInfo.share_info.list_mfile != null &&
				 * afChapterInfo.share_info.list_mfile.size() >0) {
				 * //判断转发广播内是否有图片 intent.putExtra(JsonConstant.KEY_TAG_URL,
				 * afChapterInfo.share_info.list_mfile.get(0).thumb_url); }
				 */
                        startActivityForResult(intent, IntentConstant.SHARE_BROADCAST);
                    }
                } else {
                    ToastManager.getInstance().show(mContext, R.string.no_friends_or_group_chat);
                }
            }
        } else if (item.getTextId() == R.string.broadcast_tagpage_sharebroadcast) {// 分享到广播
            // if (afChapterInfo.share_del ==
            // DefaultValueConstant.BROADCAST_SHARE_DELETE_FLAG) {// 转发的
            // ToastManager.getInstance().show(this,
            // R.string.the_broadcast_doesnt_exist);
            // } else {
            Bundle bundle_forword = new Bundle();
            if (BroadcastUtil.isShareBroadcast(afChapterInfo) && afChapterInfo.share_info != null) {
                bundle_forword.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo.share_info);
            } else {
                bundle_forword.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo);
            }
            Intent intent_forword = new Intent(mContext, ShareToBroadcastActivity.class);
            intent_forword.putExtras(bundle_forword);
            intent_forword.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent_forword, IntentConstant.SHARE_BROADCAST);
            // }
        } else if (item.getTextId() == R.string.share_to_facebook) {// 分享到FB
            // if (afChapterInfo.share_del ==
            // DefaultValueConstant.BROADCAST_SHARE_DELETE_FLAG) {// 转发的
            // ToastManager.getInstance().show(this,
            // R.string.the_broadcast_doesnt_exist);
            // } else {
            Intent intentfb = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo);
            intentfb.setClass(mContext, BroadcastShareToFbActivity.class);
            intentfb.putExtras(bundle);
            ((Activity) mContext).startActivityForResult(intentfb, IntentConstant.REQUEST_CODE_SHARETOFACEBOOK);
            // }
        } else if (item.getTextId() == R.string.cancel) {
            c_position = -1;
        } else if (item.getTextId() == R.string.broadcast_sharetofblite) {
            Intent intentfb = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo);
            bundle.putBoolean(IntentConstant.ISWEBMODESHOW, true);
            intentfb.setClass(mContext, BroadcastShareToFbActivity.class);
            intentfb.putExtras(bundle);
            ((Activity) mContext).startActivityForResult(intentfb, IntentConstant.REQUEST_CODE_SHARETOFACEBOOK);
        }
    }

    public void showAppDialog(String msg, final int code, final AfChapterInfo afChapterInfo, final boolean isDelete) {
        AppDialog appDialog = new AppDialog(mContext);
        appDialog.createConfirmDialog(mContext, msg, new OnConfirmButtonDialogListener() {
            @Override
            public void onRightButtonClick() {
                // if (code == Consts.REQ_BCMSG_DELETE) {
                if (afChapterInfo != null) {
                    if (isDelete) {
                        showProgressDialog(R.string.deleting);
                    } else {
                        showProgressDialog(R.string.please_wait);
                    }
                    mAfCorePalmchat.AfHttpBCMsgOperate(code, profileInfo.afId, "", afChapterInfo.mid, null, null, null, BroadcastDetailActivity.this);
                }
                // }
            }

            @Override
            public void onLeftButtonClick() {

            }
        });
        appDialog.show();
    }

    public void emojj_del() {
        // CommonUtils.isDeleteIcon(R.drawable.emojj_del, vEditTextContent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth = p.x;
        int screenHeight = p.y;
        rect = new Rect(0, 0, screenWidth, screenHeight);

    }

    @Override
    protected void onPause() {
        super.onPause();
               /*停止当前正在播放的视频*/
        if (VedioManager.getInstance().getVideoController() != null) {
            VedioManager.getInstance().getVideoController().pause();
        }

        VoiceManager.getInstance().completion();

    }

    @Override
    protected void onStop() {
        super.onStop();
                /*停止当前正在播放的视频*/
        if (VedioManager.getInstance().getVideoController() != null) {
            VedioManager.getInstance().getVideoController().stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // if (bc_detail.hasKeyboard()) {
        // CommonUtils.closeSoftKeyBoard(vEditTextContent);
        // }
        // 如果评论的输入键盘存在 那就关闭键盘
        MyActivityManager.getScreenManager().popActivity(SoftkeyboardActivity.class);
    }

    public void setComment_clickable() {
        if (progressBar.getVisibility() == View.GONE) {
            vEditTextContent.setEnabled(true);
            vEditTextContent.setClickable(true);
            send_button.setClickable(true);
            vImageEmotion.setClickable(true);
            txt_like_click.setClickable(true);
            txt_comment.setClickable(true);
            txt_forward.setClickable(true);
        } else {
            vEditTextContent.setEnabled(false);
            vEditTextContent.setClickable(false);
            send_button.setClickable(false);
            vImageEmotion.setClickable(false);
            txt_like_click.setClickable(false);
            txt_comment.setClickable(false);
            txt_forward.setClickable(false);
        }
    }

    public boolean chekeMeIsLike(ArrayList<AfLikeInfo> afLikeInfos) {
        for (AfLikeInfo afLikeInfo : afLikeInfos) {
            if (afLikeInfo.afid.equals(profileInfo.afId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        PalmchatLogUtils.e(BroadcastDetailActivity.class.getName(), "----onActivityResult----");
        switch (requestCode) {

            case Constants.COMMENT_FLAG:
                if (data != null) {
                    view_bottom_cmd.setVisibility(View.VISIBLE);
                    chatting_options_layout.setVisibility(View.GONE);
                /* 评论内容 */
                    String msg = data.getExtras().getString(JsonConstant.KEY_COMMENT_COUNTENT);
                /* 是否需要发送到服务器 */
                    String isSend = data.getExtras().getString("isSend");
                    if (null != isSend) {
                        if (isSend.equals("1")) { // 需要发送到服务器
                            sendComment(msg); // 发送评论
                        } else if (isSend.equals("2")) { // 不需要发送到服务器
                            if (!TextUtils.isEmpty(msg)) {
                                // edittedtMsg = msg;
                            /* 解析表情 */
                                CharSequence text = EmojiParser.getInstance(mContext).parse(msg);
                                vEditTextContent.setText(text);
                            } else {
                                clearCommentEdt();
                            }
                        }
                    }

                } else {
                    clearCommentEdt();
                }
                break;
            case IntentConstant.SHARE_BROADCAST:
                if (data != null) {
                    boolean isSuccess = data.getBooleanExtra(JsonConstant.KEY_SEND_IS_SEND_SUCCESS, false);
                    String tempTipContent = data.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL);
                    int code = data.getIntExtra(JsonConstant.KEY_SHARE_OR_SEND_FRIENDS_ERROR_CODE, 0);

                    String tipContent;
                    if (isSuccess) {
                        tipContent = getResources().getString(R.string.share_friend_success);
                    } else {
                        if (!TextUtils.isEmpty(tempTipContent)) {
                            tipContent = tempTipContent;
                        } else {
                            tipContent = getResources().getString(R.string.share_friend_failed);
                        }
                    }
                /* sendUpdate_delect_BroadcastList(afChapterInfo); *//** 在BroadcastUtil调用了这里就不调用 */
                    ToastManager.getInstance().showShareBroadcast(this, DefaultValueConstant.SHARETOASTTIME, isSuccess, tipContent);
                    if (code == Consts.REQ_CODE_201 || code == Consts.REQ_CODE_202 || code == Consts.REQ_CODE_BROADCAST_SHARE_ERROR) {
                        finish();
                    }

                }
                break;
            case IntentConstant.DELETE_BROADCAST:
                if (data != null)
                    finish();
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 发送评论
     *
     * @param msg_conent
     */
    private void sendComment(String msg_conent) {
        // if (msg != null) {
        if (!TextUtils.isEmpty(msg_conent)) {
            String hint_text = vEditTextContent.getHint().toString();
            if (!hint_text.equals(getString(R.string.hint_commet))) { // @某人时，格式为
                // @xxxx
                // content
                msg_conent = hint_text + msg_conent;
            }

            clearCommentEdt();
            String time = cacheManager.getsendTime(System.currentTimeMillis()).toString();
            /* 将评论插入本地数据库 */
            int _id = mAfCorePalmchat.AfDBBCCommentInsert(Consts.DATA_BROADCAST_PAGE, afChapterInfo._id, "", time, profileInfo.afId, to_afid, msg_conent, AfMessageInfo.MESSAGE_SENTING, AfProfileInfo.profileToFriend(profileInfo));
            AfCommentInfo comment = BroadcastUtil.toAfCommentInfo(_id, afChapterInfo._id, to_afid, "", time, profileInfo.afId, msg_conent, AfMessageInfo.MESSAGE_SENTING, AfProfileInfo.profileToFriend(profileInfo), to_sname);
            afChapterInfo.list_comments.add(0, comment);
            afChapterInfo.total_comment++;
            /* 设置评论总数 */
            setComment_count(afChapterInfo.total_comment);
            /* 刷新页面评论列表 */
            Message msg = new Message();
            msg.what = ADD_COMMENTADAAPTER;
            msg.obj = comment;
            handler.sendMessage(msg);
            PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_COMMENT);
            /* 发送评论到服务器 */
            mAfCorePalmchat.AfHttpBCMsgOperate(Consts.REQ_BCMSG_COMMENT, profileInfo.afId, to_afid, afChapterInfo.mid, msg_conent, null, _id, BroadcastDetailActivity.this);

        } else {
            clearCommentEdt();
            ToastManager.getInstance().show(mContext, R.string.hint_commet);
        }
        // }
    }

    /**
     * zhh 进入输入法页面
     *
     * @param isOpenEmotion 是否弹出表情框
     * @param hint_content  //@xxxx 回复时hint内容
     */
    private void toSoftkeyboardActivity(boolean isOpenEmotion, String hint_content) {
        if (VedioManager.getInstance().getVideoController() != null)
            VedioManager.getInstance().getVideoController().stop();

        currentMsg = vEditTextContent.getText().toString().trim();
        Intent sIntent = new Intent(context, SoftkeyboardActivity.class);
        if (!TextUtils.isEmpty(currentMsg)) {
            sIntent.putExtra("txtMessage", currentMsg);
        }
        sIntent.putExtra(JsonConstant.KEY_HINT_NAME, hint_content);
        sIntent.putExtra(JsonConstant.KEY_OPNE_EMOTION, isOpenEmotion);
        startActivityForResult(sIntent, Constants.COMMENT_FLAG);


    }

    private void bind_disply_lat_lng(final AfChapterInfo info, TextView textRange) {
        if (info.lat != null && info.lng != null) {
            double lat = Double.valueOf(SharePreferenceUtils.getInstance(mContext).getLat());
            double lon = Double.valueOf(SharePreferenceUtils.getInstance(mContext).getLng());
            String msg = "lat1=" + lat + ",lon1=" + lon + ",lat2=" + info.lat + ",lon2=" + info.lng;
            PalmchatLogUtils.e("getDistance", msg);
            String range = CommonUtils.getDistance(lon, lat, info.lng, info.lat);
            textRange.setText(range);
        } else {
            textRange.setText(info.country);
        }
    }

    /**
     * zhh 清空评论框
     */
    private void clearCommentEdt() {
        vEditTextContent.setText("");
        vEditTextContent.setHint(R.string.hint_commet);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.image_emotion: // 点击表情图标
                toSoftkeyboardActivity(true, "");
                break;
            case R.id.chatting_message_edit: // 点击评论框
                toSoftkeyboardActivity(false, "");
                break;
            case R.id.view_like:
                if (afChapterInfo.total_like > 0) {
                    intoLikeListActivity();
                }
                break;
            case R.id.lin_msg_part:// 如果是转发的原文 点原文信息区域 就跳原文广播
            case R.id.userprofile:// 如果是转发的原文 点Profile区域 就跳原文广播
                if (BroadcastUtil.isShareBroadcast(afChapterInfo) && afChapterInfo.share_info != null &&
                        afChapterInfo.share_info.profile_Info != null && afChapterInfo.share_info.mid != null) {
                    if (!afChapterInfo.share_info.mid.contains(afChapterInfo.share_info.profile_Info.afId + "_")) {
                        if (VoiceManager.getInstance().isPlaying()) {
                            VoiceManager.getInstance().pause();
                        }


                        Bundle bundle = new Bundle();
                        bundle.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo.share_info);
                        bundle.putInt(JsonConstant.KEY_BC_SKIP_TYPE, SHARE_BROADCAST_DETAIL);
                        // bundle.putInt(JsonConstant.KEY_FLAG, flag);
                        Intent intent = new Intent(mContext, BroadcastDetailActivity.class);
                        intent.putExtra("FromPage", BroadcastDetailActivity.FROM_BROADCAST);
                        intent.putExtras(bundle);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                }
                break;
        /*
         * case R.id.head_img:
		 *
		 * break;
		 */

            case R.id.txt_like_click:
                if (!txt_like_click.isSelected()) {
                    String time = cacheManager.getsendTime(System.currentTimeMillis()).toString();
                    int l_db_id = mAfCorePalmchat.AfDBBCLikeInsert(Consts.DATA_BROADCAST_PAGE, afChapterInfo._id, "", time, profileInfo.afId, AfMessageInfo.MESSAGE_SENTING, AfProfileInfo.profileToFriend(profileInfo));
                    AfLikeInfo afLikeInfo = BroadcastUtil.toAfLikeinfo(l_db_id, afChapterInfo._id, "", time, profileInfo.afId, AfMessageInfo.MESSAGE_SENTING, AfProfileInfo.profileToFriend(profileInfo));
                    afChapterInfo.list_likes.add(0, afLikeInfo);
                    afChapterInfo.total_like++;
                    //String likes = (afChapterInfo.total_like>99?"99+":String.valueOf(afChapterInfo.total_like));
                    String likes = String.valueOf(afChapterInfo.total_like);
                    txt_like.setText(likes);
                    afChapterInfo.isLike = true;
                    PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_LIKE);
                    mAfCorePalmchat.AfHttpBCMsgOperate(Consts.REQ_BCMSG_AGREE, profileInfo.afId, "", afChapterInfo.mid, null, null, afChapterInfo, BroadcastDetailActivity.this);
                    bindLike(afChapterInfo);
                }
			/*
			 * else { intoLikeListActivity(); }
			 */
                break;
            case R.id.txt_comment:
                view_bottom_cmd.setVisibility(View.GONE);
                chatting_options_layout.setVisibility(View.VISIBLE);
                // chatting_options_layout.performClick();
                toSoftkeyboardActivity(false, "");
                break;
            case R.id.txt_forward:
                showItemDialog(FORWARD_BROADCAST_DETAIL);
                break;
            case R.id.view_more:
            case R.id.bc_item:

            case R.id.chatting_operate_one: // 点击发送按钮
                if (vEditTextContent != null) {
                    String msg_conent = vEditTextContent.getText().toString();
                    sendComment(msg_conent);
                }
                break;
        }
    }


}