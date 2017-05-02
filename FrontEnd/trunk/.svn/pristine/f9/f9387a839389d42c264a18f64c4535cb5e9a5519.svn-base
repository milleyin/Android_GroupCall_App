package com.afmobi.palmchat.ui.activity.social;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.FacebookConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.eventbusmodel.EventFollowNotice;
import com.afmobi.palmchat.gif.GifImageView;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.main.ChatsContactsActivity;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.model.DialogItem;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.InnerNoAbBrowserActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountDetailsActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.FlowLayout;
import com.afmobi.palmchat.ui.customview.ListDialog;
import com.afmobi.palmchat.ui.customview.TextViewFixTouchConsume;
import com.afmobi.palmchat.ui.customview.ViewEditBroadcastPicture;
import com.afmobi.palmchat.ui.customview.videoview.CustomVideoController;
import com.afmobi.palmchat.ui.customview.videoview.VedioManager;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.BroadcastUtil;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.DateUtil;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
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
import com.core.AfResponseComm.AfMFileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

/**
 * AdapterBroadcastMessages
 *
 * @author gtf
 */
public class AdapterBroadcastUitls implements ListDialog.OnItemClick {
    String TAG = AdapterBroadcastUitls.class.getCanonicalName();
    private Activity mContext;
    private boolean isSingle = false;
    LargeImageDialog largeImageDialog;
    private final int BC_CITY = 12360;
    private String mCurPAccountId;
    public static final int HOME_TO_PROFILE = 0x02;
    public static final int BC_TO_PROFILE = 0x03;
    public static final int CHAT_TO_PROFILE = 0x04;
    public static final int FRIENDS_TO_PROFILE = 0x05;
    public static final int GPCHAT_TO_PROFILE = 0x06;
//	private boolean isWholeFollow;//判断是否关注过好友
    /**
     * 客户端是否安装fb
     */
    boolean mBl_IsFbExist;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static final String DOWNLOAD_SUCCESS_SUFFIX = "_COMPLETE"; // Successful
    // download
    // suffix
    private int mFromPage, flag;
    private AfProfileInfo mProfileInfo;//profile或Myprofile传过来的
    private static int perPicMargin;// 广播每张图的间距
    private static int picLayoutMargin;// 广播图片整个区域离屏幕边界的间距
    int iPaddingBroadcastMessage,//广播被转发时原文的边距等
            lp2Top;
    private static final int FOLLOWTYPE = 1;//表示关注过的

    /*
     * public AdapterBroadcastUitls(Context c, int from, int flag){ mContext =
	 * c; mFromPage = from; this.flag = flag; }
	 */

    public AdapterBroadcastUitls(Activity c, int from) {
        mContext = c;
        mFromPage = from;
        if (perPicMargin == 0) {
            perPicMargin = (int) PalmchatApp.getApplication().getResources().getDimension(R.dimen.d_broadcast_pic_margin);
            picLayoutMargin = (int) PalmchatApp.getApplication().getResources().getDimension(R.dimen.d_broadcast_item_margin);
        }

    }

    public AdapterBroadcastUitls(Activity c, int from, AfProfileInfo info) {
        mContext = c;
        mFromPage = from;
        mProfileInfo = info;
    }

    /**
     * 设置listitem里的控件内容
     * BaseFragment mBaseFragment,Activity mActivity VideoView新增参数，确定来自哪个页面对象
     *
     * @param viewHolder
     * @param info
     * @param position
     */
    public void bindView(final BaroadCast_ViewHolder viewHolder, final AfChapterInfo info, final int position, String className) {
        if (viewHolder != null && info != null) {
            /*if(iPaddingBroadcastMessage<=0){
              iPaddingBroadcastMessage=mContext.getResources().getDimensionPixelSize(R.dimen.d_broadcast_item_margin);
			  lp2Top = mContext.getResources().getDimensionPixelSize(R.dimen.accounts_imagetxt_img_margin_size);
			}*/
            AfProfileInfo profile1 = AfFriendInfo.friendToProfile(info.profile_Info);
            boolean isWholeFollow = CommonUtils.isFollow(profile1.afId);
            if (BroadcastUtil.isShareBroadcast(info)) {//是分享的
                bindForwarderUser(viewHolder, info, position);//设置转发者的内容
                //如果原文已经被删除了
                if (info.share_del == DefaultValueConstant.BROADCAST_SHARE_DELETE_FLAG) {//如果原文已经被删除了
                    viewHolder.relative_userprofile.setVisibility(View.GONE);
                    viewHolder.mMessageTxt.setText(R.string.the_broadcast_doesnt_exist);
                    viewHolder.mMessageTxt.setVisibility(View.VISIBLE);
                    viewHolder.continueReadTextView.setVisibility(View.GONE);
                    viewHolder.mMessageTxt.setPadding(0, dimen_text_notexist, 0, dimen_text_notexist);
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp2.setMargins(iPaddingBroadcastMessage, lp2Top, iPaddingBroadcastMessage, 0);
                    viewHolder.lin_msg_part.setLayoutParams(lp2);
                    viewHolder.mUnknowTxt.setVisibility(View.GONE);//广播类型不可识别的提示
                } else {
                    if (info.share_info != null) {// 如果分享不为空
                        bindUser(viewHolder, info.share_info, position, true);//设置被转发的人的内容
                        if (info.share_info.getType() == Consts.BR_TYPE_UNKNOW) {//如果原文类型不可识别
                            viewHolder.mUnknowTxt.setVisibility(View.VISIBLE);//广播类型不可识别的提示
                        } else {
                            viewHolder.mUnknowTxt.setVisibility(View.GONE);
                        }
                    }
                    viewHolder.relative_userprofile.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(iPaddingBroadcastMessage, iPaddingBroadcastMessage / 2, iPaddingBroadcastMessage, 0);
                    viewHolder.relative_userprofile.setLayoutParams(lp);
                    viewHolder.relative_userprofile.setPadding(iPaddingBroadcastMessage, iPaddingBroadcastMessage, iPaddingBroadcastMessage, iPaddingBroadcastMessage / 2);
                    viewHolder.relative_userprofile.setBackgroundResource(R.color.base_back);

                    viewHolder.bc_mHeadImg.getLayoutParams().height = dimen_headsize_share;
                    viewHolder.bc_mHeadImg.getLayoutParams().width = dimen_headsize_share;
                    viewHolder.bc_mNameTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_text_size_share);
                    viewHolder.mTimeTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_region_text_size_share);
                    viewHolder.mRangeTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_region_text_size_share);
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp2.setMargins(iPaddingBroadcastMessage, 0, iPaddingBroadcastMessage, 0);
                    viewHolder.lin_msg_part.setLayoutParams(lp2);
                }

                viewHolder.mMessageTxt.setTextColor(color_text_content_share);
                viewHolder.mMessageTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_text_size_share);

                if (info.share_del == 0 && info.share_info != null
                        && info.share_info.getType() != Consts.BR_TYPE_UNKNOW
                        && info.share_info.getType() != Consts.BR_TYPE_IMAGE_TEXT
                        && info.share_info.getType() != Consts.BR_TYPE_IMAGE
                        && info.share_info.getType() != Consts.BR_TYPE_VIDEO
                        && info.share_info.getType() != Consts.BR_TYPE_VIDEO_TEXT) {
                    viewHolder.lin_msg_part.setPadding(0, 0, 0, iPaddingBroadcastMessage);
                } else {
                    viewHolder.lin_msg_part.setPadding(0, 0, 0, 0);
                }
                viewHolder.lin_msg_part.setBackgroundResource(R.color.base_back);//设置原文背景色
                //首先判断是否是friendcircle和profile，是的话直接隐藏不显示控件
                if (mFromPage == BroadcastDetailActivity.FROM_FRIENDCIRCLE || mFromPage == BroadcastDetailActivity.FROM_PROFILE ||
                        mFromPage == BroadcastDetailActivity.FROM_OFFICIAL_ACCOUNT) {
                    viewHolder.mTvBroadcastForWardFollow.setVisibility(View.GONE);
                    viewHolder.mTvBroadcastFollow.setVisibility(View.GONE);
                } else {
                    if (isWholeFollow) {//判断是否关注过
                        viewHolder.mTvBroadcastForWardFollow.setVisibility(View.GONE);
                    } else {
                        viewHolder.mTvBroadcastForWardFollow.setVisibility(View.VISIBLE);
                        controlVoluation(viewHolder.mTvBroadcastForWardFollow);
                    }
                    viewHolder.mTvBroadcastFollow.setVisibility(View.GONE);//是转发但是是原文的话不显示follow按钮
                    if (info.afid.equals(CacheManager.getInstance().getMyProfile().afId)) {//自己转发的话不就显示follow按钮
                        viewHolder.mTvBroadcastForWardFollow.setVisibility(View.GONE);
                    }
                    viewHolder.mTvBroadcastForWardFollow.setOnClickListener(new MyClick(info, viewHolder, position));
                }
            } else {//------------------以下为非分享的
                viewHolder.relative_userprofile_forward.setVisibility(View.GONE);
                viewHolder.relative_userprofile.setVisibility(View.VISIBLE);
                bindUser(viewHolder, info, position, false);

                if (info.getType() == Consts.BR_TYPE_UNKNOW) {//如果 类型不可识别
                    viewHolder.mUnknowTxt.setVisibility(View.VISIBLE);//广播类型不可识别的提示
                } else {
                    viewHolder.mUnknowTxt.setVisibility(View.GONE);
                }
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, 0);
                viewHolder.relative_userprofile.setLayoutParams(lp);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp2.setMargins(0, 0, 0, 0);
                viewHolder.lin_msg_part.setLayoutParams(lp2);
                viewHolder.relative_userprofile.setPadding(iPaddingBroadcastMessage, iPaddingBroadcastMessage, iPaddingBroadcastMessage, iPaddingBroadcastMessage / 2);
                viewHolder.lin_msg_part.setPadding(0, 0, 0, 0);
                viewHolder.relative_userprofile.setBackgroundResource(R.color.white);
                viewHolder.lin_msg_part.setBackgroundResource(R.color.white);

                viewHolder.bc_mHeadImg.getLayoutParams().height = dimen_headsize;
                viewHolder.bc_mHeadImg.getLayoutParams().width = dimen_headsize;
                viewHolder.bc_mNameTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_text_size);
                viewHolder.mTimeTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_region_text_size);
                viewHolder.mRangeTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_region_text_size);
                viewHolder.mMessageTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen_text_size);
                //首先判断是否是friendcircle和profile，官方账号广播历史记录，是的话直接隐藏不显示控件
                if (mFromPage == BroadcastDetailActivity.FROM_FRIENDCIRCLE || mFromPage == BroadcastDetailActivity.FROM_PROFILE ||
                        mFromPage == BroadcastDetailActivity.FROM_OFFICIAL_ACCOUNT) {
                    viewHolder.mTvBroadcastFollow.setVisibility(View.GONE);
                } else {
                    //后面那个判断是去获取是否follow,有的时候前面的并不是一定是正确的。
                    if (isWholeFollow) {//判断是否关注过
                        viewHolder.mTvBroadcastFollow.setVisibility(View.GONE);
                    } else {
                        viewHolder.mTvBroadcastFollow.setVisibility(View.VISIBLE);
                        controlVoluation(viewHolder.mTvBroadcastFollow);
                    }
                    //判断官方账号是否follow过
                    if (null != CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).searchNoCheckName(profile1.afId, false, true)) {
                        viewHolder.mTvBroadcastFollow.setVisibility(View.GONE);//follow按钮
                    } else if (info.afid.equals(CacheManager.getInstance().getMyProfile().afId)) {//判断是否是自己的账号
                        viewHolder.mTvBroadcastFollow.setVisibility(View.GONE);
                    }
                    viewHolder.mTvBroadcastFollow.setOnClickListener(new MyClick(info, viewHolder, position));
                }
            }

			/*if(BroadcastDetailActivity.FROM_TAGPAGE==mFromPage){
                viewHolder.view_comment_more.setVisibility(View.GONE);
				viewHolder.chatting_options_layout.setVisibility(View.GONE);
			}*/


            bindLikeComment(viewHolder, info, position);
            if (info.share_info != null) {//如果分享不为空
                if (info.share_info.getType() != Consts.BR_TYPE_UNKNOW) {//可以识别类型 那么 就要绑定分享的图片等
                    bindPictureView(viewHolder, info.share_info, position);
                    bindVoiceView(viewHolder, info.share_info, position, info.mid);
                    bindVideoView(viewHolder, info.share_info, position, className, info.mid, true);

                } else {
                    viewHolder.lin_pic.setVisibility(View.GONE);
                }

            } else if (info.getType() != Consts.BR_TYPE_UNKNOW) {// 可以识别类型 那么 就要绑定 图片等
                bindPictureView(viewHolder, info, position);
                bindVoiceView(viewHolder, info, position, info.mid);
                bindVideoView(viewHolder, info, position, className, info.mid, false);

            } else {
                viewHolder.lin_pic.setVisibility(View.GONE);
            }
        }
    }

    private void bindLikeComment(final BaroadCast_ViewHolder viewHolder, final AfChapterInfo info, int position) {
        if (info.total_like > 999) {
            viewHolder.txt_like.setText("999+");
        } else {
            viewHolder.txt_like.setText(String.valueOf(info.total_like));
        }

        if (info.total_comment > 999) {
            viewHolder.txt_comment.setText("999+");
        } else {
            viewHolder.txt_comment.setText(String.valueOf(info.total_comment));
        }
        viewHolder.txt_like.setOnClickListener(new MyClick(info, viewHolder, position));
        viewHolder.txt_comment.setOnClickListener(new MyClick(info, viewHolder, position));
        viewHolder.txt_forward.setOnClickListener(new MyClick(info, viewHolder, position));
        viewHolder.view_more.setOnClickListener(new MyClick(info, viewHolder, position));
//		viewHolder.chatting_operate_one.setOnClickListener(new MyClick(info, viewHolder, position));
//		viewHolder.bc_tag.setOnClickListener(new MyClick(info, viewHolder, position));

        if (info.isLike) {
            viewHolder.txt_like.setSelected(true);
            viewHolder.txt_like.setClickable(false);
        } else {
            viewHolder.txt_like.setSelected(false);
            viewHolder.txt_like.setClickable(true);
        }
        if (info.mid == null) {
            return;
        }
        if (viewHolder.sending.getVisibility() == View.VISIBLE && viewHolder.mTimeTxt.getVisibility() == View.GONE || info.mid.contains(CacheManager.getInstance().getMyProfile().afId + "_")) {
            viewHolder.txt_like.setClickable(false);
            viewHolder.txt_comment.setClickable(false);
            viewHolder.txt_forward.setClickable(false);
            viewHolder.view_more.setClickable(false);
//			viewHolder.chatting_operate_one.setClickable(false);
        } else {
            viewHolder.txt_like.setClickable(true);
            viewHolder.txt_comment.setClickable(true);
            viewHolder.txt_forward.setClickable(true);
            viewHolder.view_more.setClickable(true);
//			viewHolder.chatting_operate_one.setClickable(true);
        }
        if (BroadcastUtil.isShareBroadcast(info) && info.share_info != null) {//如果是转发的 ，点击这个区域显示原文
            if (info.share_del != DefaultValueConstant.BROADCAST_SHARE_DELETE_FLAG) {//如果没被删除掉
                viewHolder.bc_item.setOnClickListener(new MyClick(info.share_info, viewHolder, position));
            } else {
                viewHolder.bc_item.setOnClickListener(null);
            }
        } else {
            viewHolder.bc_item.setOnClickListener(new MyClick(info, viewHolder, position));
            if (viewHolder.sending.getVisibility() == View.VISIBLE && viewHolder.mTimeTxt.getVisibility() == View.GONE || info.mid.contains(CacheManager.getInstance().getMyProfile().afId + "_")) {
                viewHolder.bc_item.setClickable(false);
            } else {
                viewHolder.bc_item.setClickable(true);
            }
        }
        viewHolder.txt_comment_more.setOnClickListener(new MyClick(info, viewHolder, position));
//		viewHolder.txt_other_btn.setOnClickListener(new MyClick(info, viewHolder, position));
    }

    private void bindUser(final BaroadCast_ViewHolder viewHolder, final AfChapterInfo info, final int position, boolean isSharedInfo) {
        viewHolder.bc_mHeadImg.setOnClickListener(new MyClick(info, viewHolder, position));
        viewHolder.bc_mNameTxt.setOnClickListener(new MyClick(info, viewHolder, position));
        if (info.profile_Info != null) {
            viewHolder.bc_mNameTxt.setText(info.profile_Info.name);
            if (info.profile_Info.user_class == DefaultValueConstant.BROADCAST_PROFILE_PA) {
                viewHolder.pa_icon.setVisibility(View.VISIBLE);
            } else {
                viewHolder.pa_icon.setVisibility(View.GONE);
            }
        }
        if (isSharedInfo) {/**如果是true的话那就是分享内容切内容没有删除掉*/
            viewHolder.mRangeTxt.setVisibility(View.GONE);
            viewHolder.view_line.setVisibility(View.GONE);
        } else {
            viewHolder.mRangeTxt.setVisibility(View.VISIBLE);
            viewHolder.view_line.setVisibility(View.VISIBLE);
        }
        viewHolder.mTimeTxt.setText(DateUtil.getTimeAgo(mContext, info.time));

        if (info.ds_type == Consts.DATA_FROM_LOCAL) {// 自己刚发的广播
            if (CacheManager.getInstance().getBC_resend_HashMap().containsKey(info._id)) {
                viewHolder.sending.setVisibility(View.VISIBLE);
                viewHolder.mTimeTxt.setVisibility(View.GONE);
            } else {
                viewHolder.sending.setVisibility(View.GONE);
                viewHolder.mTimeTxt.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.sending.setVisibility(View.GONE);
            viewHolder.mTimeTxt.setVisibility(View.VISIBLE);
        }
//		viewHolder.view_line.setVisibility(View.VISIBLE);
        if (mFromPage != BroadcastDetailActivity.FROM_BC_COMMON) {//普通列表形式的广播
            String city = CacheManager.getInstance().getMyProfile().city;
            if (this.flag == BC_CITY) {//同城广播
                if (TextUtils.isEmpty(city) || DefaultValueConstant.OTHER.equals(city)) {
                    bind_disply_lat_lng(viewHolder, info);
                } else {
                    viewHolder.mRangeTxt.setText(city);
                }
            } else if (!isSharedInfo) {
                String region = CacheManager.getInstance().getMyProfile().region;
                if (!TextUtils.isEmpty(region) && !DefaultValueConstant.OTHERS.equals(region)
                        && mFromPage == BroadcastDetailActivity.FROM_NEARBY) { //省不为空 显示省
                    viewHolder.mRangeTxt.setText(region);
                } else {//省空 显示国家 不判断经纬度了。//如果是原文  那原文的广播显示国家
                    viewHolder.mRangeTxt.setText(info.country);
                }
            }
        } else {
            if (this.flag == BC_CITY) {
                String city = CacheManager.getInstance().getMyProfile().city;
                if (TextUtils.isEmpty(city) || DefaultValueConstant.OTHER.equals(city)) {
                    viewHolder.mRangeTxt.setText("");
                } else {
                    viewHolder.mRangeTxt.setText(city);
                }
            } else {
                viewHolder.mRangeTxt.setText(info.country);
            }
        }
        if (TextUtils.isEmpty(viewHolder.mRangeTxt.getText())) {
            viewHolder.lin_mRange.setVisibility(View.GONE);
        } else {
            viewHolder.lin_mRange.setVisibility(View.VISIBLE);
        }
        String str = ToDBC(info.content);
        viewHolder.mMessageTxt.setTextColor(color_text_content);
        viewHolder.mMessageTxt.setPadding(0, 0, 0, 0);

        if (TextUtils.isEmpty(str)) {
            viewHolder.mMessageTxt.setVisibility(View.GONE);
            viewHolder.mMessageTxt.setText("");
            viewHolder.continueReadTextView.setVisibility(View.GONE);
        } else {
            viewHolder.mMessageTxt.setVisibility(View.VISIBLE);
            //解析表情和Tags
            CharSequence text = EmojiParser.getInstance(mContext).parseEmojiTags(mContext, str, info.list_tags, CommonUtils.dip2px(mContext, 24));
            viewHolder.mMessageTxt.setText(text); //如果是被转发的  那这里代表原文文字内容
            //SetLinkClickIntercept(viewHolder.mMessageTxt);
            CommonUtils.setUrlSpanLink(mContext, viewHolder.mMessageTxt);
            if (info.content_flag == 1) {
                viewHolder.continueReadTextView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.continueReadTextView.setVisibility(View.GONE);
            }
        }
        /*	viewHolder.messgeText_LinesCount=-1;  暂时不处理可能改为服务器截取
        viewHolder.continueReadTextView.setVisibility(View.GONE);
			ViewTreeObserver vto = viewHolder.mMessageTxt.getViewTreeObserver();
			vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
				@Override
				public boolean onPreDraw() {//判断行数 如果超出 就隐藏多余的行数
					if( viewHolder.messgeText_LinesCount<0){
						viewHolder.messgeText_LinesCount= viewHolder.mMessageTxt.getLineCount();
						PalmchatLogUtils.i("WXL", "L="+viewHolder.messgeText_LinesCount);
						if(viewHolder.messgeText_LinesCount>Constants.BROADCAST_TEXTMAXLINES){
							viewHolder.mMessageTxt.setMaxLines(Constants.BROADCAST_TEXTMAXLINES);
							viewHolder.continueReadTextView.setVisibility(View.VISIBLE);
						}
					}
					return true;
				}
			});*/


        if (info.profile_Info != null) {
            int age = info.profile_Info.age;
            byte sex = info.profile_Info.sex;
//			viewHolder.bc_mSexAgeTxt.setText(String.valueOf(age));
//			viewHolder.bc_mSexAgeTxt.setBackgroundResource(Consts.AFMOBI_SEX_MALE == sex ? R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
//			viewHolder.bc_mSexAgeTxt.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == sex ? R.drawable.icon_sexage_boy : R.drawable.icon_sexage_girl, 0, 0, 0);
            // WXl 20151013 调UIL的显示头像方法, 替换原来的AvatarImageInfo.统一图片管理
            ImageManager.getInstance().DisplayAvatarImage(viewHolder.bc_mHeadImg, info.getServerUrl(), info.afid, Consts.AF_HEAD_MIDDLE, sex, info.getSerialFromHead(), null);
        }
    }

    public void bindForwarderUser(final BaroadCast_ViewHolder viewHolder, final AfChapterInfo info, final int position) {
        if (viewHolder != null && info != null) {
            viewHolder.relative_userprofile_forward.setVisibility(View.VISIBLE);
            viewHolder.relative_userprofile_forward.setOnClickListener(new MyClick(info, viewHolder, position));
            viewHolder.bc_mHeadImg_forward.setOnClickListener(new MyClick(info, viewHolder, position));
            viewHolder.bc_mNameTxt_forward.setOnClickListener(new MyClick(info, viewHolder, position));
            if (info.profile_Info != null) {
                viewHolder.bc_mNameTxt_forward.setText(info.profile_Info.name);
                if (info.profile_Info.user_class == DefaultValueConstant.BROADCAST_PROFILE_PA) {
                    viewHolder.pa_icon.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.pa_icon.setVisibility(View.GONE);
                }
            }

            viewHolder.mTimeTxt_forward.setText(DateUtil.getTimeAgo(mContext, info.time));

            if (info.ds_type == Consts.DATA_FROM_LOCAL) {// 自己刚发的广播
                if (CacheManager.getInstance().getBC_resend_HashMap().containsKey(info._id)) {
                    viewHolder.sending.setVisibility(View.VISIBLE);
                    viewHolder.mTimeTxt_forward.setVisibility(View.GONE);
                } else {
                    viewHolder.sending_forward.setVisibility(View.GONE);
                    viewHolder.mTimeTxt_forward.setVisibility(View.VISIBLE);
                }
            } else {
                viewHolder.sending_forward.setVisibility(View.GONE);
                viewHolder.mTimeTxt_forward.setVisibility(View.VISIBLE);
            }

            if (mFromPage != BroadcastDetailActivity.FROM_BC_COMMON) {//普通列表形式的广播
                String city = CacheManager.getInstance().getMyProfile().city;
                if (this.flag == BC_CITY) {//同城广播
                    if (TextUtils.isEmpty(city) || DefaultValueConstant.OTHER.equals(city)) {
                        bind_disply_lat_lng(viewHolder, info);
                    } else {
                        viewHolder.mRangeTxt_forward.setText(city);
                    }
                } else {
                    if (mFromPage == BroadcastDetailActivity.FROM_FRIENDCIRCLE
                            || mFromPage == BroadcastDetailActivity.FROM_HOTTODAY) {// 朋友圈或Hottoday 							// 直接显示国家
                        viewHolder.mRangeTxt_forward.setText(info.country);//显示国家s
                    } else {// Nearby or ...
                        String region = CacheManager.getInstance().getMyProfile().region;
                        if (!TextUtils.isEmpty(region) && !DefaultValueConstant.OTHERS.equals(region)
                                && mFromPage == BroadcastDetailActivity.FROM_NEARBY) { //省不为空 显示省
                            viewHolder.mRangeTxt_forward.setText(region);
                        } else {//省空 显示国家 不判断经纬度了。
                            viewHolder.mRangeTxt_forward.setText(info.country);
                        }
                    }
                }
            } else {
                if (this.flag == BC_CITY) {
                    String city = CacheManager.getInstance().getMyProfile().city;
                    if (TextUtils.isEmpty(city) || DefaultValueConstant.OTHER.equals(city)) {
                        viewHolder.mRangeTxt_forward.setText("");
                    } else {
                        viewHolder.mRangeTxt_forward.setText(city);
                    }
                } else {
                    viewHolder.mRangeTxt_forward.setText(info.country);
                }
            }
            if (TextUtils.isEmpty(viewHolder.mRangeTxt_forward.getText())) {
                viewHolder.lin_mRange_forward.setVisibility(View.GONE);
            } else {
                viewHolder.lin_mRange_forward.setVisibility(View.VISIBLE);
            }

            String str = ToDBC(info.content);
            //解析表情和Tags
            if (info.share_flag == DefaultValueConstant.BROADCAST_SHARE_NOCOMMENT
                    || TextUtils.isEmpty(str)) {
                viewHolder.mMessageTxt_forward.setVisibility(View.GONE);
                viewHolder.continueReadTextView_forward.setVisibility(View.GONE);
            } else {
                viewHolder.mMessageTxt_forward.setVisibility(View.VISIBLE);
                CharSequence text = EmojiParser.getInstance(mContext).parseEmojiTags(mContext, str, info.list_tags, CommonUtils.dip2px(mContext, 24));
                viewHolder.mMessageTxt_forward.setText(text);
                if (info.content_flag == 1) {
                    viewHolder.continueReadTextView_forward.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.continueReadTextView_forward.setVisibility(View.GONE);
                }
            }


            if (info.profile_Info != null) {
                int age = info.profile_Info.age;
                byte sex = info.profile_Info.sex;
//				viewHolder.bc_mSexAgeTxt_forward.setText(String.valueOf(age));
//				viewHolder.bc_mSexAgeTxt_forward.setBackgroundResource(Consts.AFMOBI_SEX_MALE == sex ? R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
//				viewHolder.bc_mSexAgeTxt_forward.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == sex ? R.drawable.icon_sexage_boy : R.drawable.icon_sexage_girl, 0, 0, 0);
                // WXl 20151013 调UIL的显示头像方法, 替换原来的AvatarImageInfo.统一图片管理
                ImageManager.getInstance().DisplayAvatarImage(viewHolder.bc_mHeadImg_forward, info.getServerUrl(), info.afid, Consts.AF_HEAD_MIDDLE, sex, info.getSerialFromHead(), null);
            }


        }
    }

    /**
     * 显示经纬度或国家
     *
     * @param viewHolder
     * @param info
     */
    private void bind_disply_lat_lng(final BaroadCast_ViewHolder viewHolder, final AfChapterInfo info) {
        if (info.lat != null && info.lng != null) {
            double lat = Double.valueOf(SharePreferenceUtils.getInstance(mContext).getLat());
            double lon = Double.valueOf(SharePreferenceUtils.getInstance(mContext).getLng());
            String msg = "lat1=" + lat + ",lon1=" + lon + ",lat2=" + info.lat + ",lon2=" + info.lng;
            PalmchatLogUtils.e("getDistance", msg);
            String range = CommonUtils.getDistance(lon, lat, info.lng, info.lat);
            viewHolder.mRangeTxt.setText(range);
        } else {
            viewHolder.mRangeTxt.setText(info.country);
        }
    }

    /**
     * FarFarAway模块专用
     *
     * @param viewHolder
     * @param info
     * @param position
     * @param lat
     * @param lon
     */
    public void bindViewFarFarAway(final BaroadCast_ViewHolder viewHolder, final AfChapterInfo info, final int position, double lat, double lon) {
        viewHolder.bc_mHeadImg.setOnClickListener(new MyClick(info, viewHolder, position));
        viewHolder.bc_mNameTxt.setOnClickListener(new MyClick(info, viewHolder, position));
        viewHolder.bc_mNameTxt.setText(info.profile_Info.name);
        viewHolder.mTimeTxt.setText(info.time);
        if (info.ds_type == Consts.DATA_FROM_LOCAL) {
            if (CacheManager.getInstance().getBC_resend_HashMap().containsKey(info._id)) {
                viewHolder.sending.setVisibility(View.VISIBLE);
                viewHolder.mTimeTxt.setVisibility(View.GONE);
            } else {
                viewHolder.sending.setVisibility(View.GONE);
                viewHolder.mTimeTxt.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.sending.setVisibility(View.GONE);
            viewHolder.mTimeTxt.setVisibility(View.VISIBLE);
        }
        viewHolder.view_line.setVisibility(View.VISIBLE);
        if (info.lat != null && info.lng != null) {
            String msg = "lat1=" + lat + ",lon1=" + lon + ",lat2=" + info.lat + ",lon2=" + info.lng;
            PalmchatLogUtils.e("getDistance", msg);
            String range = CommonUtils.getDistance(lon, lat, info.lng, info.lat);
            viewHolder.mRangeTxt.setText(range);
        } else {
            viewHolder.mRangeTxt.setText(info.country);
        }
        CharSequence text = EmojiParser.getInstance(mContext).parse(info.content, CommonUtils.dip2px(mContext, 24));
        viewHolder.mMessageTxt.setText(text);
        int age = info.profile_Info.age;
        byte sex = info.profile_Info.sex;
//		viewHolder.bc_mSexAgeTxt.setText(String.valueOf(age));
//		viewHolder.bc_mSexAgeTxt.setBackgroundResource(Consts.AFMOBI_SEX_MALE == sex ? R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
//		viewHolder.bc_mSexAgeTxt.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == sex ? R.drawable.icon_sexage_boy : R.drawable.icon_sexage_girl, 0, 0, 0);

        // WXl 20151013 调UIL的显示头像方法, 替换原来的AvatarImageInfo.统一图片管理
        ImageManager.getInstance().DisplayAvatarImage(viewHolder.bc_mHeadImg, info.getServerUrl(), info.afid, Consts.AF_HEAD_MIDDLE, sex, info.getSerialFromHead(), null);
        /*if (info.status == AfMessageInfo.MESSAGE_UNSENT) {
            viewHolder.txt_other_btn.setText(mContext.getString(R.string.retry));
		} else {
			viewHolder.txt_other_btn.setText("");
		}*/

        if (TextUtils.isEmpty(info.content)) {
            viewHolder.mMessageTxt.setVisibility(View.GONE);
        } else {
            viewHolder.mMessageTxt.setVisibility(View.VISIBLE);
        }
        viewHolder.txt_like.setText(String.valueOf(info.total_like));
        viewHolder.txt_comment.setText(String.valueOf(info.total_comment));
        // if (adapter != null ) {
        viewHolder.txt_like.setOnClickListener(new MyClick(info, viewHolder, position));
        viewHolder.txt_comment.setOnClickListener(new MyClick(info, viewHolder, position));
        viewHolder.txt_forward.setOnClickListener(new MyClick(info, viewHolder, position));
        viewHolder.view_more.setOnClickListener(new MyClick(info, viewHolder, position));
//		viewHolder.chatting_operate_one.setOnClickListener(new MyClick(info, viewHolder, position));

        if (info.isLike) {
            viewHolder.txt_like.setSelected(true);
            viewHolder.txt_like.setClickable(false);
        } else {
            viewHolder.txt_like.setSelected(false);
            viewHolder.txt_like.setClickable(true);
        }
        // }
        if (viewHolder.sending.getVisibility() == View.VISIBLE && viewHolder.mTimeTxt.getVisibility() == View.GONE) {
            viewHolder.txt_like.setClickable(false);
            viewHolder.txt_comment.setClickable(false);
            viewHolder.txt_forward.setClickable(false);
            viewHolder.view_more.setClickable(false);
//			viewHolder.chatting_operate_one.setClickable(false);
        } else {
            viewHolder.txt_like.setClickable(true);
            viewHolder.txt_comment.setClickable(true);
            viewHolder.txt_forward.setClickable(true);
            viewHolder.view_more.setClickable(true);
//			viewHolder.chatting_operate_one.setClickable(true);
        }
        viewHolder.bc_item.setOnClickListener(new MyClick(info, viewHolder, position));
//		viewHolder.txt_other_btn.setOnClickListener(new MyClick(info, viewHolder, position));

        bindPictureView(viewHolder, info, position);
        bindVoiceView(viewHolder, info, position, info.mid);
    }

    private static int setOneRowPos(int availableWidth, int start_i, int end_i, int y, int colCount, int rowIndex, ArrayList<AfMFileInfo> afFileInfos, boolean isOldBroadcast) {
        int cw = availableWidth / colCount;
        int minH = 0;//最小高
        for (int i = start_i; i <= end_i; i++) {
            int pictureW = 0;
            if (afFileInfos.get(i).resize != null) {
                pictureW = afFileInfos.get(i).resize[0];
            } else if (!TextUtils.isEmpty(afFileInfos.get(i).local_thumb_path)) {
                pictureW = CommonUtils.split_source_w(afFileInfos.get(i).local_thumb_path);
            } else if (!TextUtils.isEmpty(afFileInfos.get(i).url)) {
                pictureW = CommonUtils.split_source_w(afFileInfos.get(i).url);
            }
            int pictureH = 0;
            if (afFileInfos.get(i).resize != null) {
                pictureH = afFileInfos.get(i).resize[1];
            } else if (!TextUtils.isEmpty(afFileInfos.get(i).local_thumb_path)) {
                pictureH = CommonUtils.split_source_h(afFileInfos.get(i).local_thumb_path);
            } else if (!TextUtils.isEmpty(afFileInfos.get(i).url)) {
                pictureH = CommonUtils.split_source_h(afFileInfos.get(i).url);
            }
            int _h = pictureH * cw / pictureW;
            if (minH == 0 || minH > _h) {
                minH = _h;
            }
        }
        if (isOldBroadcast) {
            minH = cw;
        } else {
            if (minH > cw * 4 / 3) {//超长图 限制高度
                minH = cw * 4 / 3;
            }
            if (minH < cw / 3) {//超宽图 加高高度
                minH = cw / 3;
            }
        }
        AfMFileInfo mfile = null;
        for (int i = start_i, j = 0; i <= end_i; i++, j++) {
            mfile = afFileInfos.get(i);
            mfile.resize = new int[2];
            mfile.resize[0] = cw;
            mfile.resize[1] = minH;
            mfile.cut = new int[4];
            int pictureW = 0;
            if (afFileInfos.get(i).resize != null) {
                pictureW = afFileInfos.get(i).resize[0];
            } else {
                pictureW = CommonUtils.split_source_w(afFileInfos.get(i).url);
            }
            int pictureH = 0;
            if (afFileInfos.get(i).resize != null) {
                pictureH = afFileInfos.get(i).resize[1];
            } else {
                pictureH = CommonUtils.split_source_h(afFileInfos.get(i).url);
            }

            if (isOldBroadcast) {
                mfile.cut[0] = 0;
                mfile.cut[2] = pictureW;
                mfile.cut[1] = 0;
                mfile.cut[3] = pictureH;
            } else {
                int _clipH = (pictureW) * minH / cw;
                mfile.cut[0] = 0;
                mfile.cut[2] = pictureW;
                mfile.cut[1] = pictureH - _clipH >> 1;
                mfile.cut[3] = mfile.cut[1] + _clipH;

                if (mfile.cut[1] < 0) {
                    mfile.cut[1] = 0;
                }
                if (mfile.cut[1] + mfile.cut[3] > pictureH) {
                    mfile.cut[3] = pictureH - mfile.cut[1];
                }
            }
        }
        return y + minH;
    }

    public static int[] getImageRule_Size_cut(AfChapterInfo info, int availableWidth) {
        boolean isOldBroadcast = false;
        final ArrayList<AfMFileInfo> afFileInfos = info.list_mfile;
        int[] picRule = ViewEditBroadcastPicture.MAPARR_DEFAULT_PICTURE_RULE[afFileInfos.size() - 1];
        if (!TextUtils.isEmpty(info.pic_rule)) {
            String rule = info.pic_rule;
            if (rule.length() > 2) {
                rule = rule.substring(1, rule.length() - 1);
            }
            String[] ruleArr = rule.split(",");
            if (ruleArr.length > 0) {
                picRule = new int[ruleArr.length];
                for (int i = 0; i < ruleArr.length; i++) {
                    picRule[i] = Integer.parseInt(ruleArr[i]);
                }
            }
        } else {
            isOldBroadcast = true;
            picRule = ViewEditBroadcastPicture.MAPARR_DEFAULT_PICTURE_RULE_OLD[afFileInfos.size() - 1];
        }

        int count = afFileInfos.size();
        int[] rowArr = picRule;//ViewEditBroadcastPicture.map[count-1];
        int startI = 0;
        int y = 0;
        int countR = 0;
        for (int i = 0, j = 0; i < count; i++) {
            if (countR >= rowArr[j] - 1) {
                y = setOneRowPos(availableWidth, startI, i, y, rowArr[j], j, afFileInfos, isOldBroadcast);//设置单行图片排列
                startI = i + 1;
                countR = 0;
                j++;
            } else {
                countR++;
            }
        }
        return picRule;
    }

    /**
     * 设置显示广播列表中的图片
     *
     * @param viewHolder
     * @param info
     * @param position
     */
    public void bindPictureView(final BaroadCast_ViewHolder viewHolder, final AfChapterInfo info, final int position) {
        if (info.getType() == Consts.BR_TYPE_IMAGE_TEXT || info.getType() == Consts.BR_TYPE_IMAGE) {
            viewHolder.lin_pic.removeAllViews();
            final ArrayList<AfMFileInfo> al_afFileInfos = info.list_mfile;
            int list_mfile_size = al_afFileInfos.size();
            if (list_mfile_size > 1) {
                isSingle = false;
            } else {
                isSingle = true;
            }
            int[] picRule = getImageRule_Size_cut(info, ImageUtil.DISPLAYW);//计算各个图片的宽高和裁切
            viewHolder.lin_pic.setPicRule(picRule);
            for (int i = 0; i < list_mfile_size; i++) {// 添加所有广播图片
                String thumb_url = "";
                if (null != viewHolder.lin_pic) {
                    final ImageView imageView = new ImageView(mContext);
                    imageView.setTag(i);
                    imageView.setScaleType(ScaleType.CENTER_CROP);
                    final AfMFileInfo mfileInfo = al_afFileInfos.get(i);
                    thumb_url = mfileInfo.thumb_url;
                    if (!TextUtils.isEmpty(thumb_url)) {
                        thumb_url = CacheManager.getInstance().getThumb_url(thumb_url, isSingle, info.pic_rule);
                        final String img_url = mfileInfo.url;
                        // imageView异步显示图片，如果图片本地不存在就下载
                        ImageManager.getInstance().DisplayImage(imageView, thumb_url, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                // 下载开始的时候 要设置这个图片的宽高和排布 显示默认的灰色框框
                                setDefaultImageLayoutParams(imageView, isSingle, img_url, mfileInfo, info == null ? null : info.pic_rule);
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

                        imageView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {// 点击图片显示大图
                                if (VoiceManager.getInstance().isPlaying()) {
                                    VoiceManager.getInstance().pause();
                                }


                                int img_index = (Integer) v.getTag();
                                if (iAdapterBroadcastUitls != null) {
                                    iAdapterBroadcastUitls.onBindLookePicture(viewHolder, info, position, imageView, img_index);
                                }
                            }
                        });
                    } else {// 正在发送的广播详情

                        Bitmap bm = ImageManager.getInstance().loadLocalImageSync(al_afFileInfos.get(i).local_thumb_path, true);//ImageUtil.readBitMap(al_afFileInfos.get(i).local_thumb_path);
                        setImageLayoutParams(imageView, bm, isSingle, al_afFileInfos.get(i));
                        imageView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int img_index = (Integer) v.getTag();
                                if (iAdapterBroadcastUitls != null) {
                                    iAdapterBroadcastUitls.onBindLookePicture(viewHolder, info, position, imageView, img_index);
                                }
                            }
                        });
                    }
                    viewHolder.lin_pic.addView(imageView);

                }
            }
            viewHolder.lin_pic.setVisibility(View.VISIBLE);
        } else {
            viewHolder.lin_pic.setVisibility(View.GONE);
        }
    }

    /**
     * 设置语音广播
     *
     * @param viewHolder
     * @param info
     */
    private void bindVoiceView(final BaroadCast_ViewHolder viewHolder, final AfChapterInfo info, int position, final String bodyMid) {
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
            File file = new File(voicePaht);
            showStopAnim(file.getPath(), viewHolder, position, info, bodyMid);
            if (viewHolder.lin_play_icon_to_voice != null) {
                viewHolder.lin_play_icon_to_voice.setOnClickListener(new MyClick(info, viewHolder, bodyMid));
            }

            if (viewHolder.play_icon_to_voice != null) {
                viewHolder.play_icon_to_voice.setOnClickListener(new MyClick(info, viewHolder, bodyMid));
            }

            if (viewHolder.play_icon_to_voice_anim != null) {
                viewHolder.play_icon_to_voice_anim.setOnClickListener(new MyClick(info, viewHolder, bodyMid));
            }
            if (!TextUtils.isEmpty(url)) { // Server data
                if (info.download_success) {
                    viewHolder.lin_play_icon_to_voice.setBackgroundResource(R.drawable.broadcast_voiceloadp);
                    viewHolder.lin_play_icon_to_voice.setGravity(Gravity.LEFT);
                    viewHolder.gifImageView.setVisibility(View.GONE);
                    viewHolder.play_icon_to_voice.setVisibility(View.VISIBLE);
                    viewHolder.play_icon_to_voice_anim.setVisibility(View.VISIBLE);
                    viewHolder.play_time_to_voice.setVisibility(View.VISIBLE);
                } else {
                    downloadVoice(viewHolder, info, file);
                }

            } else { // Our data
                viewHolder.lin_play_icon_to_voice.setBackgroundResource(R.drawable.broadcast_voiceloadp);
                viewHolder.lin_play_icon_to_voice.setGravity(Gravity.LEFT);
                viewHolder.gifImageView.setVisibility(View.GONE);
                viewHolder.play_icon_to_voice.setVisibility(View.VISIBLE);
                viewHolder.play_icon_to_voice_anim.setVisibility(View.VISIBLE);
                viewHolder.play_time_to_voice.setVisibility(View.VISIBLE);
            }
            viewHolder.play_time_to_voice.setTag(voicePaht);
            if (viewHolder.gifImageView.getVisibility() == View.GONE) {
                viewHolder.lin_play_icon_to_voice.setClickable(true);
                viewHolder.play_icon_to_voice.setClickable(true);
                viewHolder.play_icon_to_voice_anim.setClickable(true);
                if (file.exists() && file.length() > 0) {
                    int nVoiceTime = 0;
                    if (!TextUtils.isEmpty(info.desc)) {
                        try {
                            nVoiceTime = Integer.valueOf(info.desc);
                        } catch (Exception e) {
                            nVoiceTime = 0;
                        }
                    } else if (0 < afMFileInfo.recordTime) {
                        nVoiceTime = afMFileInfo.recordTime;
                    } else {
                        BigDecimal voicetime = CommonUtils.getBigDecimalCount(VoiceManager.getInstance().getVoiceTime(voicePaht));
                        nVoiceTime = voicetime.intValue();
                    }

                    if (viewHolder.play_time_to_voice != null) {
                        if (nVoiceTime == 0) {
                            viewHolder.play_icon_to_voice.setClickable(false);
                            viewHolder.lin_play_icon_to_voice.setClickable(false);
                            viewHolder.play_icon_to_voice_anim.setClickable(false);
                        } else {
                            viewHolder.play_icon_to_voice.setClickable(true);
                            viewHolder.lin_play_icon_to_voice.setClickable(true);
                            viewHolder.play_icon_to_voice_anim.setClickable(true);
                        }
                        viewHolder.play_time_to_voice.setText(nVoiceTime + "s");
                    }
                } else {
                    if (viewHolder.play_time_to_voice != null) {
                        viewHolder.play_time_to_voice.setText("");
                    }
                }
            } else {
                viewHolder.lin_play_icon_to_voice.setClickable(false);
                viewHolder.play_icon_to_voice.setClickable(false);
                viewHolder.play_icon_to_voice_anim.setClickable(false);
            }
            viewHolder.lin_play_icon_to_voice.setVisibility(View.VISIBLE);
        } else {
            viewHolder.lin_play_icon_to_voice.setVisibility(View.GONE);
        }

    }

    private void bindVideoView(final BaroadCast_ViewHolder viewHolder, final AfChapterInfo info, final int position, String clasName, String mid, boolean isShared) {
        String videoPath = "";

        if (info.getType() == Consts.BR_TYPE_VIDEO_TEXT || info.getType() == Consts.BR_TYPE_VIDEO) {
            AfResponseComm.AfMFileInfo afMFileInfo = info.list_mfile.get(0);
            if (afMFileInfo == null) {
                return;
            }

            /*if(position % 2 == 0 ) {
                afMFileInfo.url="http://192.168.10.53/a-416-800_416_800.mp4" ;
                afMFileInfo.thumb_url="http://192.168.10.53/a_416_800.png";
            }else {
                afMFileInfo.url="http://192.168.10.53/b-800-200_800_200.mp4" ;
                afMFileInfo.thumb_url="http://192.168.10.53/b_800_200.png";
            }*/
            String url = afMFileInfo.url;
            if (!TextUtils.isEmpty(url)) {

            /*根据url 里的图片尺寸对布局的高度进行设置*/
                try {
                    int video_w = CommonUtils.split_source_w(afMFileInfo.url);
                    int video_h = CommonUtils.split_source_h(afMFileInfo.url);
                    int msgPartWidth = ImageUtil.DISPLAYW - (isShared ? iPaddingBroadcastMessage * 2 : 0);
                    int real_h = msgPartWidth * video_h / video_w;
                    real_h = CommonUtils.checkDisplayRatio_h(msgPartWidth, real_h);
                    ViewGroup.LayoutParams layout = viewHolder.fl_video_layout.getLayoutParams();
                    if (layout.height != real_h) {
                        layout.height = real_h;
                        viewHolder.fl_video_layout.setLayoutParams(layout);
                    }
                } catch (Exception e) {//防止URL中没带宽高参数时做的保护
                    e.printStackTrace();
                }

                viewHolder.custom_video_controller.setFlag(mid + "_" + clasName);
                viewHolder.custom_video_controller.setTime(afMFileInfo.duration * 1000);

                if (afMFileInfo.url != null)
                    viewHolder.custom_video_controller.setPath(afMFileInfo.url);
                /*缩略图地址*/
                String thumb_url = afMFileInfo.thumb_url;
                if (!TextUtils.isEmpty(thumb_url))
                    viewHolder.custom_video_controller.setImgFirstFrame(thumb_url);


                final String videoName = PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(afMFileInfo.url);
                videoPath = RequestConstant.VEDIO_CACHE + videoName;
                boolean exists = IsVoiceFileExistsSDcard(videoPath);
                if (exists) {
                    info.download_success = true;
                    videoPath = videoPath + DOWNLOAD_SUCCESS_SUFFIX;
                } else {
                    info.download_success = false;
                    File file = new File(videoPath);
                    downloadVoice(viewHolder, info, file);
                }

            }

            viewHolder.fl_video_layout.setVisibility(View.VISIBLE);

        } else {
            viewHolder.fl_video_layout.setVisibility(View.GONE);
        }
    }


    public void initTextView(BaroadCast_ViewHolder viewHolder, View convertView) {
        CacheManager.getInstance().getMyProfile();
        //转发者
        viewHolder.relative_userprofile_forward = (RelativeLayout) convertView.findViewById(R.id.userprofile_forward);
        viewHolder.bc_mHeadImg_forward = (ImageView) convertView.findViewById(R.id.head_img_forward);
//		viewHolder.bc_mSexAgeTxt_forward = (TextView) convertView.findViewById(R.id.sex_age_txt_forward);
//		viewHolder.pa_icon_forward= (ImageView) convertView.findViewById(R.id.pa_icon_forward);
        viewHolder.bc_mNameTxt_forward = (TextView) convertView.findViewById(R.id.name_txt_forward);
        viewHolder.mTimeTxt_forward = (TextView) convertView.findViewById(R.id.txt_datatime_forward);
        viewHolder.mRangeTxt_forward = (TextView) convertView.findViewById(R.id.txt_range_forward);
        viewHolder.mMessageTxt_forward = (TextViewFixTouchConsume) convertView.findViewById(R.id.message_txt_forward);
        viewHolder.mMessageTxt_forward.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());
        viewHolder.continueReadTextView_forward = (TextView) convertView.findViewById(R.id.continue_read_textView_forward);
        viewHolder.sending_forward = (TextView) convertView.findViewById(R.id.sending_forward);
        viewHolder.mTvBroadcastForWardFollow = (TextView) convertView.findViewById(R.id.tv_broadcast_forward_follow);
        viewHolder.mTvBroadcastForWardFollow.setVisibility(View.GONE);

        //作者 或原文作者
        viewHolder.relative_userprofile = (RelativeLayout) convertView.findViewById(R.id.userprofile);
        viewHolder.bc_mHeadImg = (ImageView) convertView.findViewById(R.id.head_img);
        viewHolder.lin_pic = (FlowLayout) convertView.findViewById(R.id.lin_pic);
//		viewHolder.bc_mSexAgeTxt = (TextView) convertView.findViewById(R.id.sex_age_txt);
        viewHolder.pa_icon = (ImageView) convertView.findViewById(R.id.pa_icon);
        viewHolder.bc_mNameTxt = (TextView) convertView.findViewById(R.id.name_txt);
        viewHolder.mTimeTxt = (TextView) convertView.findViewById(R.id.txt_datatime);
        viewHolder.sending = (TextView) convertView.findViewById(R.id.sending);
        viewHolder.view_line = convertView.findViewById(R.id.view_line);
        viewHolder.mRangeTxt = (TextView) convertView.findViewById(R.id.txt_range);
        viewHolder.mMessageTxt = (TextViewFixTouchConsume) convertView.findViewById(R.id.message_txt);
        viewHolder.mMessageTxt.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());
        viewHolder.continueReadTextView = (TextView) convertView.findViewById(R.id.continue_read_textView);
//		viewHolder.bc_tag = (TextView) convertView.findViewById(R.id.bc_tag);
        viewHolder.gifImageView = (GifImageView) convertView.findViewById(R.id.gif);
//		viewHolder.txt_other_btn = (TextView) convertView.findViewById(R.id.txt_other_btn);
        viewHolder.lin_play_icon_to_voice = (LinearLayout) convertView.findViewById(R.id.lin_play_icon_to_voice);
        viewHolder.lin_mRange = (LinearLayout) convertView.findViewById(R.id.lin_show_other);
        viewHolder.lin_mRange_forward = (LinearLayout) convertView.findViewById(R.id.lin_show_other_forward);
        viewHolder.play_icon_to_voice = (ImageView) convertView.findViewById(R.id.play_icon_to_voice);
        viewHolder.play_icon_to_voice_anim = (ImageView) convertView.findViewById(R.id.play_icon_to_voice_anim);
        viewHolder.play_time_to_voice = (TextView) convertView.findViewById(R.id.play_time_to_voice);

        viewHolder.textview = (TextView) convertView.findViewById(R.id.chatting_message_edit);
        viewHolder.txt_like = (TextView) convertView.findViewById(R.id.txt_like);
        viewHolder.txt_comment = (TextView) convertView.findViewById(R.id.txt_comment);
        viewHolder.txt_forward = (TextView) convertView.findViewById(R.id.txt_forward);
        viewHolder.txt_comment_total = (TextView) convertView.findViewById(R.id.comment_total);
        viewHolder.view_more = convertView.findViewById(R.id.view_more);
        viewHolder.txt_comment_more = convertView.findViewById(R.id.txt_comment_more);
        viewHolder.view_comment_more = convertView.findViewById(R.id.view_comment_more);
        viewHolder.listView_comment = (LinearLayout) convertView.findViewById(R.id.lv_comment);

        viewHolder.bc_item = convertView.findViewById(R.id.bc_item);
        viewHolder.lin_comment = convertView.findViewById(R.id.lin_comment);
//		viewHolder.comment_part = convertView.findViewById(R.id.comment_part);
//		viewHolder.chatting_operate_one = convertView.findViewById(R.id.chatting_operate_one);
        viewHolder.chatting_options_layout = convertView.findViewById(R.id.chatting_options_layout);
        viewHolder.lin_msg_part = convertView.findViewById(R.id.lin_msg_part);

        viewHolder.line = convertView.findViewById(R.id.brd_divider_line);
        viewHolder.divide_two = convertView.findViewById(R.id.divide_two);
        viewHolder.mUnknowTxt = (TextView) convertView.findViewById(R.id.type_unknow_textView);
        viewHolder.mTvBroadcastFollow = (TextView) convertView.findViewById(R.id.tv_broadcast_follow);
        CommonUtils.setUnKnownSpanLink(mContext, viewHolder.mUnknowTxt, InnerNoAbBrowserActivity.class);
        if (BroadcastDetailActivity.FROM_HOTTODAY == mFromPage ||
                BroadcastDetailActivity.FROM_TAGPAGE == mFromPage || BroadcastDetailActivity.FROM_OFFICIAL_ACCOUNT == mFromPage) {
            viewHolder.chatting_options_layout.setVisibility(View.GONE);
            viewHolder.lin_comment.setVisibility(View.GONE);
        }

		/*初始化视频相关控件*/
        viewHolder.fl_video_layout = (ViewGroup) convertView.findViewById(R.id.fl_video_layout);
        viewHolder.custom_video_controller = (CustomVideoController) convertView.findViewById(R.id.custom_video_controller);

        color_text_content = mContext.getResources().getColor(R.color.text_level_1);
        color_text_content_share = mContext.getResources().getColor(R.color.text_level_1);
        dimen_text_notexist = (int) mContext.getResources().getDimension(R.dimen.broadcast_share_text_notexist);

        dimen_headsize = (int) mContext.getResources().getDimension(R.dimen.d_broadcast_headszie);
        dimen_headsize_share = (int) mContext.getResources().getDimension(R.dimen.d_broadcast_headszie_share);
        dimen_text_size = mContext.getResources().getDimension(R.dimen.d_broadcast_nameszie);
        dimen_text_size_share = mContext.getResources().getDimension(R.dimen.d_broadcast_nameszie_share);
        dimen_region_text_size = mContext.getResources().getDimension(R.dimen.d_broadcast_desc_sp);
        dimen_region_text_size_share = mContext.getResources().getDimension(R.dimen.d_broadcast_desc_sp_share);
        iPaddingBroadcastMessage = mContext.getResources().getDimensionPixelSize(R.dimen.d_broadcast_item_margin);
        lp2Top = mContext.getResources().getDimensionPixelSize(R.dimen.accounts_imagetxt_img_margin_size);
    }

    private int dimen_text_notexist;//原文不存在时的间距
    private int color_text_content,
            color_text_content_share;//原文广播不存在的文字颜色
    private int dimen_headsize,//正常头像尺寸
            dimen_headsize_share;//原文头像尺寸
    private float dimen_text_size,//正常文字尺寸
            dimen_text_size_share;//原文文字尺寸
    private float dimen_region_text_size,//正常时间地区文字尺寸
            dimen_region_text_size_share;//原文时间地区文字尺寸

    private void showItemDialog(final Context c, int type, final AfChapterInfo afChapterInfo, int position) {
        List<DialogItem> items = new ArrayList<DialogItem>();
        if (type == JsonConstant.SHARE_BROADCAST) {
            if (afChapterInfo.afid.equals(CacheManager.getInstance().getMyProfile().afId)) {
                items.add(new DialogItem(R.string.delete, R.layout.custom_dialog_image, position, type, R.drawable.icon_list_delete, afChapterInfo));
            } else {
                items.add(new DialogItem(R.string.report_abuse, R.layout.custom_dialog_image, position, type, R.drawable.broadcast_dialog_abuse, afChapterInfo));
            }
        } else {
            if (!(afChapterInfo.getType() == Consts.BR_TYPE_VIDEO_TEXT || afChapterInfo.getType() == Consts.BR_TYPE_VIDEO)) {
                items.add(new DialogItem(R.string.broadcast_send_to_friend, R.layout.custom_dialog_image, position, type, R.drawable.broadcast_dialog_send_to_friends, afChapterInfo));
            }


            if (CommonUtils.isAppExist(FacebookConstant.FACEBOOKLITE_PACKAGE)) {

                items.add(new DialogItem(R.string.broadcast_sharetofblite, R.layout.custom_dialog_image, position, type, R.drawable.icon_sheet_share_to_facebook, afChapterInfo));
            }
            items.add(new DialogItem(R.string.broadcast_tagpage_sharebroadcast, R.layout.custom_dialog_image, position, type, R.drawable.icon_sheet_share_to_broadcast, afChapterInfo));
            boolean mBl_IsFbExist = CommonUtils.isAppExist(FacebookConstant.FACEBOOKPACKAGE);
            if (mBl_IsFbExist) {
                items.add(new DialogItem(R.string.share_to_facebook, R.layout.custom_dialog_image, position, type, R.drawable.icon_sheet_share_to_facebook, afChapterInfo));
            }
        }
        ListDialog dialog = new ListDialog(c, items);
        dialog.setItemClick(this);
        dialog.show();
    }

    @Override
    public void onItemClick(DialogItem item) {
        AfChapterInfo afChapterInfo = (AfChapterInfo) item.getObject();
        switch (item.getTextId()) {
            case R.string.delete:
                /*点击删除时，停止掉正在播放的广播*/
                if (VedioManager.getInstance().getVideoController() != null)
                    VedioManager.getInstance().getVideoController().stop();

                if (iAdapterBroadcastUitls != null) {
                    iAdapterBroadcastUitls.onClikeMore(item);
                }
                break;
            case R.string.report_abuse:
                if (iAdapterBroadcastUitls != null) {
                    iAdapterBroadcastUitls.onClikeMore(item);
                }
                break;
            case R.string.broadcast_send_to_friend:


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
                        //if (afChapterInfo.share_del!= DefaultValueConstant.BROADCAST_SHARE_DELETE_FLAG) {
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
                /*	else if(afChapterInfo.share_info != null && afChapterInfo.share_info.list_mfile != null && afChapterInfo.share_info.list_mfile.size() >0) { //判断转发广播内是否有图片
                        intent.putExtra(JsonConstant.KEY_TAG_URL, afChapterInfo.share_info.list_mfile.get(0).thumb_url);
					}*/
                        if (mContext instanceof Activity) {
                            Activity mainTab = (Activity) mContext;
                            mainTab.startActivityForResult(intent, IntentConstant.SHARE_BROADCAST);
                        }
                /*}else{//如果原文已经被删除了
                    ToastManager.getInstance().show(mContext, R.string.the_broadcast_doesnt_exist);
				}*/
                    } else {
                        ToastManager.getInstance().show(mContext, R.string.no_friends_or_group_chat);
                    }
                }
                break;
            case R.string.broadcast_tagpage_sharebroadcast:


                if (afChapterInfo.share_del != DefaultValueConstant.BROADCAST_SHARE_DELETE_FLAG) {


                    Bundle bundle_forword = new Bundle();
                    if (BroadcastUtil.isShareBroadcast(afChapterInfo) && afChapterInfo.share_info != null) {
                        bundle_forword.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo.share_info);
                    } else {
                        bundle_forword.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo);
                    }
                    Intent intent_forword = new Intent(mContext, ShareToBroadcastActivity.class);
                    intent_forword.putExtras(bundle_forword);
                    intent_forword.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (mContext instanceof Activity) {
//					Activity mainTab = (Activity) mContext;
                        mContext.startActivityForResult(intent_forword, IntentConstant.SHARE_BROADCAST);
                    }
                } else {// 如果原文已经被删除了
                    ToastManager.getInstance().show(mContext, R.string.the_broadcast_doesnt_exist);
                }
                break;
            case R.string.broadcast_sharetofblite:

                jumpToShareToFbActivity(afChapterInfo, true);
                break;
            case R.string.share_to_facebook:

                jumpToShareToFbActivity(afChapterInfo, false);
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到facebook 分享
     *
     * @param afChapterInfo
     * @param isWebShow
     */
    private void jumpToShareToFbActivity(AfChapterInfo afChapterInfo, boolean isWebShow) {
        if (afChapterInfo.share_del != DefaultValueConstant.BROADCAST_SHARE_DELETE_FLAG) {

            Intent intentfb = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo);
            bundle.putBoolean(IntentConstant.ISWEBMODESHOW, isWebShow);
            intentfb.setClass(mContext, BroadcastShareToFbActivity.class);
            intentfb.putExtras(bundle);
            ((Activity) mContext).startActivityForResult(intentfb, IntentConstant.REQUEST_CODE_SHARETOFACEBOOK);
        } else {// 如果原文已经被删除了
            ToastManager.getInstance().show(mContext, R.string.the_broadcast_doesnt_exist);
        }
    }


    class MyClick implements OnClickListener {
        private AfChapterInfo afChapterInfo;
        private BaroadCast_ViewHolder viewHolder;
        private File file;
        private int position;
        /**
         * 主题广播id
         */
        private String bodyMid;
        String time;
        private AfProfileInfo profileInfo;

        public MyClick() {

        }

        public MyClick(AfChapterInfo info, BaroadCast_ViewHolder viewHolder, String bodyMid) {
            this.afChapterInfo = info;
            this.viewHolder = viewHolder;
            this.time = System.currentTimeMillis() + "";
            this.profileInfo = CacheManager.getInstance().getMyProfile();
            this.bodyMid = bodyMid;

        }

        public MyClick(AfChapterInfo info, BaroadCast_ViewHolder viewHolder, int position) {
            this.afChapterInfo = info;
            this.viewHolder = viewHolder;
            this.time = System.currentTimeMillis() + "";
            this.profileInfo = CacheManager.getInstance().getMyProfile();
            this.position = position;
        }

        public MyClick(AfChapterInfo info, BaroadCast_ViewHolder holder, File file) {
            this.afChapterInfo = info;
            this.viewHolder = holder;
            this.file = file;
            this.profileInfo = CacheManager.getInstance().getMyProfile();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.lin_play_icon_to_voice:
                case R.id.play_icon_to_voice:
                case R.id.play_icon_to_voice_anim:
                    Play(afChapterInfo, viewHolder, this.bodyMid);
                    break;
			/*case R.id.txt_other_btn:
				if (afChapterInfo != null) {
					switch (afChapterInfo.getType()) {
					case Consts.BR_TYPE_TEXT:
						// BroadcastUtil.getInstance().resendBroadcastMsg(mContext,
						// afNearByGpsInfo,Consts.BC_PURVIEW_ALL,"#tag");
						break;
					case Consts.BR_TYPE_IMAGE_TEXT:
					case Consts.BR_TYPE_IMAGE:
						// BroadcastUtil.getInstance().resendBroadcastPictureAndText(mContext,
						// afNearByGpsInfo);
						break;
					case Consts.BR_TYPE_VOICE_TEXT:
					case Consts.BR_TYPE_VOICE:
						// BroadcastUtil.getInstance().resendBroadcastVoiceAndText(mContext,
						// afNearByGpsInfo);
						break;
					default:
						break;
					}
				}
				break;*/
                case R.id.head_img:
                case R.id.head_img_forward:
                case R.id.name_txt:
                case R.id.name_txt_forward:
				/*if (mFromPage != BroadcastDetailActivity.FROM_PROFILE) {*/
                    if (afChapterInfo == null) {
                        break;
                    }
                    if (null != mProfileInfo) {
                        if (mProfileInfo.afId.equals(afChapterInfo.afid)) {
                            return;
                        }
                    }
                    if (mFromPage == BroadcastDetailActivity.FROM_FAR_FAR_AWAY) {
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.FAR_T_PF);
//						MobclickAgent.onEvent(mContext, ReadyConfigXML.FAR_T_PF);
                    }

                    afChapterInfo.profile_Info.afId = afChapterInfo.afid;
                    AfProfileInfo profile = AfFriendInfo.friendToProfile(afChapterInfo.profile_Info);
                    if (DefaultValueConstant.BROADCAST_PROFILE_PA == afChapterInfo.profile_Info.user_class) {
                        if ((!TextUtils.isEmpty(mCurPAccountId)) && (afChapterInfo.profile_Info.afId.equals(mCurPAccountId))) {
                            if (mContext instanceof Activity)
                                ((Activity) mContext).finish();
                        } else {
                            Intent intent = new Intent(mContext, PublicAccountDetailsActivity.class);
                            intent.putExtra("Info", afChapterInfo.profile_Info);
                            mContext.startActivity(intent);
                        }

                    } else if (profile != null && profileInfo != null && !TextUtils.isEmpty(profile.afId)//当服务器出错的时候 可能为null
                            && profile.afId.equals(profileInfo.afId)) {

                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_MMPF);
//						MobclickAgent.onEvent(mContext, ReadyConfigXML.ENTRY_MMPF);

                        Bundle bundle = new Bundle();
                        bundle.putString(JsonConstant.KEY_AFID, profile.afId);
                        Intent intent = new Intent(mContext, MyProfileActivity.class);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);

                    } else {

                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BCM_T_PF);
//						MobclickAgent.onEvent(mContext, ReadyConfigXML.BCM_T_PF);
                        int from = 1;
                        if (mFromPage == BroadcastDetailActivity.FROM_BROADCAST) {
                            from = ProfileActivity.BC_TO_PROFILE;
                        } else {
                            from = ProfileActivity.HOME_TO_PROFILE;
                        }
                        Intent intent = new Intent(mContext, ProfileActivity.class);
                        intent.putExtra(JsonConstant.KEY_PROFILE, (Serializable) profile);
                        intent.putExtra(JsonConstant.KEY_FLAG, true);
                        intent.putExtra(JsonConstant.KEY_AFID, profile.afId);
                        intent.putExtra(JsonConstant.KEY_FROM_XX_PROFILE, from);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
				/*}*/
                    break;

                case R.id.txt_like: // 点赞
                    if (iAdapterBroadcastUitls != null) {
                        iAdapterBroadcastUitls.onClikeLike(viewHolder, afChapterInfo, position);
                    }
                    break;
                case R.id.txt_comment:// 点击评论按钮
                    if (VedioManager.getInstance().getVideoController() != null)
                        VedioManager.getInstance().getVideoController().stop();

                    if (mFromPage == BroadcastDetailActivity.FROM_NEARBY || mFromPage == BroadcastDetailActivity.FROM_FRIENDCIRCLE ||
                            mFromPage == BroadcastDetailActivity.FROM_FAR_FAR_AWAY) { // 直接回复
                        viewHolder.chatting_options_layout.performClick();
                    } else //其他的都进详情
					/*if (mFromPage == BroadcastDetailActivity.FROM_PROFILE||
						BroadcastDetailActivity.FROM_TAGPAGE==mFromPage
						|| BroadcastDetailActivity.FROM_HOTTODAY == mFromPage) */ { // 进入广播详情
                        jumpToBroadcastDetail(afChapterInfo, profileInfo);
                    }


                    break;
                case R.id.txt_forward:
                    showItemDialog(mContext, JsonConstant.FORWARD_BROADCAST, afChapterInfo, position);
                    break;
                case R.id.view_more: // 查看更多评论
                    showItemDialog(mContext, JsonConstant.SHARE_BROADCAST, afChapterInfo, position);
                    break;
                case R.id.bc_item://如果是转发的文章 那么点击后进 原文的详情
                case R.id.txt_comment_more:
                case R.id.userprofile_forward://如果是转发的广播，那么点转发人 Profile区域的时候 进这则广播的详情
                    // Toast.makeText(mContext, "bc_item", 1).show();
                    if (!afChapterInfo.mid.contains(profileInfo.afId + "_")) {
                        if (VoiceManager.getInstance().isPlaying()) {
                            VoiceManager.getInstance().pause();
                        }


                        Bundle bundle = new Bundle();
                        bundle.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo);
                        bundle.putInt(JsonConstant.KEY_BC_SKIP_TYPE, mFromPage);
                        bundle.putInt(JsonConstant.KEY_FLAG, flag);
                        Intent intent = new Intent(mContext, BroadcastDetailActivity.class);

                        switch (mFromPage) {
                            case BroadcastDetailActivity.FROM_BROADCAST:
                            case BroadcastDetailActivity.FROM_HOMEBROADCAST:
                                intent.putExtra("FromPage", BroadcastDetailActivity.FROM_BROADCAST);
                                break;

                            case BroadcastDetailActivity.FROM_FAR_FAR_AWAY:
                                intent.putExtra("FromPage", BroadcastDetailActivity.FROM_FAR_FAR_AWAY);
                                break;

                            case BroadcastDetailActivity.FROM_PROFILE:
                                intent.putExtra("FromPage", BroadcastDetailActivity.FROM_PROFILE);
                                break;

                            case BroadcastDetailActivity.FROM_TAGPAGE:
                                intent.putExtra("FromPage", BroadcastDetailActivity.FROM_TAGPAGE);
                                break;
                            case BroadcastDetailActivity.FROM_OFFICIAL_ACCOUNT:
                                intent.putExtra("FromPage", BroadcastDetailActivity.FROM_OFFICIAL_ACCOUNT);
                                break;

                            default:
                                intent.putExtra("FromPage", BroadcastDetailActivity.FROM_BROADCAST);
                                break;
                        }

                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                    break;
                case R.id.chatting_options_layout:

                    break;
                case R.id.tv_broadcast_follow:
                case R.id.tv_broadcast_forward_follow:
                    AfProfileInfo profile1 = AfFriendInfo.friendToProfile(afChapterInfo.profile_Info);
                    String mAfid = profile1.afId;
                    if (AppUtils.isFastDoubleClick()) {
                        return;
                    }
                    //判断是否在自己的黑名单里面如果是的话就提示follow失败
                    if (null != CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_BF).searchNoCheckName(profile1.afId, false, true)) {
                        ToastManager.getInstance().show(mContext, R.string.broadcast_follow_black_prompt);
                        return;
                    }
                    if (mAfid.startsWith("r")) {
                        if (null == CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).searchNoCheckName(profile1.afId, false, true)) {
                            follow(afChapterInfo, profile1, viewHolder);
                        }
                    } else {
                        if (!TextUtils.isEmpty(mAfid) && !CacheManager.getInstance().getClickableMap().get(mAfid + CacheManager.follow_suffix)) {
                            AddFollw(profile1, viewHolder);
                        }
                    }
                    break;
                default:
                    break;
            }

        }

    }


    /**
     * 点击评论跳转到广播详情
     */
    private void jumpToBroadcastDetail(AfChapterInfo afChapterInfo, AfProfileInfo profileInfo) {
        if (!afChapterInfo.mid.contains(profileInfo.afId + "_")) {
            if (VoiceManager.getInstance().isPlaying()) {
                VoiceManager.getInstance().pause();
            }


            Bundle bundle = new Bundle();
            bundle.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo);
            bundle.putInt(JsonConstant.KEY_BC_SKIP_TYPE, mFromPage);
            bundle.putInt(JsonConstant.KEY_FLAG, flag);
            Intent intent = new Intent(mContext, BroadcastDetailActivity.class);
            intent.putExtra("FromPage", mFromPage);
            intent.putExtra(IntentConstant.FROMCOMMENT, true);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        }
    }

    private AnimationDrawable animDrawable;// 播放语音播放效果的动画

    private void startPlayAnim(final View view, final View playIcon) {
        if (!VoiceManager.getInstance().getView().equals(view)) {
            return;
        }
        playIcon.setBackgroundResource(R.drawable.voice_pause_icon);
     /*   view.post(new Runnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {*/
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
      /*      }
        });*/

        // 解决广播列表播放播放语音下拉列表，语音显示停止实际继续播放
        view.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (VoiceManager.getInstance().isPlaying()) {
                    if (animDrawable != null && !animDrawable.isRunning()) {
                        animDrawable.setOneShot(false);
                        animDrawable.start();
                    }
                }
                return true;
            }
        });
    }

    private String showStopAnim(final String path, BaroadCast_ViewHolder viewHolder, int position, AfChapterInfo info, String bodyMid) {
        if (!path.equals(VoiceManager.getInstance().getPath()) && VoiceManager.getInstance().isPlaying()) {
            stopPlayAnim(viewHolder.play_icon_to_voice_anim, viewHolder.play_icon_to_voice);
            Log.e("showStopAnim", "stopPlayAnim");

        } else if (path.equals(VoiceManager.getInstance().getPath()) && VoiceManager.getInstance().isPlaying() &&
                (bodyMid.equals(VoiceManager.getInstance().getMainMid()))) {
            startPlayAnim(viewHolder.play_icon_to_voice_anim, viewHolder.play_icon_to_voice);
            Log.e("showStopAnim", "startPlayAnim");
        } else {
            stopPlayAnim(viewHolder.play_icon_to_voice_anim, viewHolder.play_icon_to_voice);
            Log.e("showStopAnim", "stopPlayAnim");

        }
        return path;
    }

    private void stopPlayAnim(final View view, final View playIcon) {
        Drawable de = view.getBackground();
        // 刚进去聊天界面是没有赋动画的
        if (de instanceof AnimationDrawable) {
            AnimationDrawable playAnim = (AnimationDrawable) de;
            playAnim.stop();
        }

        view.setBackgroundResource(R.drawable.voice_anim01);
        playIcon.setBackgroundResource(R.drawable.chatting_voice_player_icon);
    }

    private void Play(final AfChapterInfo afNearByGpsInfo, final BaroadCast_ViewHolder holder, String bodyMid) {
        if (afNearByGpsInfo != null && holder != null) {
            String voicePaht = (String) holder.play_time_to_voice.getTag();
            final File file = new File(voicePaht);
            if (file.exists() && file.length() > 0) {
                boolean isPlay = VoiceManager.getInstance().isPlaying();
                if (isPlay) {
                    VoiceManager.getInstance().pause();

                    if (holder.play_icon_to_voice_anim.equals(VoiceManager.getInstance().getView())) {
                        return;
                    }
                }

                if (CommonUtils.isEmpty(VoiceManager.getInstance().getPath())) {
                    VoiceManager.getInstance().setView(holder.play_icon_to_voice_anim);
                    VoiceManager.getInstance().setPlayIcon(holder.play_icon_to_voice);
                    startPlayAnim(holder.play_icon_to_voice_anim, holder.play_icon_to_voice);
                    VoiceManager.getInstance().setmActivity(mContext);
                    VoiceManager.getInstance().setMainMid(bodyMid);
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
                            downloadVoice(holder, afNearByGpsInfo, file);
                        }
                    });
                }
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.no_audio_file), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * @param holder
     * @param afChapterInfo
     * @param file
     */
    private void downloadVoice(final BaroadCast_ViewHolder holder, final AfChapterInfo afChapterInfo, final File file) {
        final String voice_path = file.getPath();
        holder.lin_play_icon_to_voice.setBackgroundResource(R.drawable.broadcast_voiceload);
        holder.lin_play_icon_to_voice.setGravity(Gravity.CENTER);
        holder.gifImageView.setVisibility(View.VISIBLE);
        holder.play_icon_to_voice.setVisibility(View.GONE);
        holder.play_icon_to_voice_anim.setVisibility(View.GONE);

        if (holder.play_time_to_voice != null && !TextUtils.isEmpty(afChapterInfo.desc)) {
            holder.play_time_to_voice.setVisibility(View.VISIBLE);
            holder.play_time_to_voice.setText(afChapterInfo.desc + "s");
        } else {
            holder.play_time_to_voice.setVisibility(View.GONE);
        }
        final AfMFileInfo info = afChapterInfo.list_mfile.get(0);
        // PalmchatLogUtils.e("downloadVoice", info.url);
        if (!CacheManager.getInstance().getGifDownLoadMap().containsKey(info.url)) {
            CacheManager.getInstance().getGifDownLoadMap().put(info.url, voice_path);
            String voiceUrl = info.url;
            if (!voiceUrl.startsWith(JsonConstant.HTTP_HEAD)) {
                voiceUrl = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerInfo()[Constants.CORE_SERVER_BROADCAST_MEDIA_HOST] + info.url;
            }
            PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBroadcastDownload(voiceUrl, voice_path, null, new AfHttpResultListener() {
                @Override
                public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
                    // PalmchatLogUtils.d("AfOnResult========","flag," + flag +
                    // "code=" + code +",http_code=" + http_code);
                    if (code == Consts.REQ_CODE_SUCCESS) {
                        File newFile = new File(voice_path + DOWNLOAD_SUCCESS_SUFFIX);
                        boolean falg = file.renameTo(newFile);
                        if (falg) {
                            String download_success_voice_path = newFile.getPath();
                            holder.lin_play_icon_to_voice.setBackgroundResource(R.drawable.broadcast_voiceloadp);
                            holder.lin_play_icon_to_voice.setGravity(Gravity.LEFT);
                            holder.gifImageView.setVisibility(View.GONE);
                            holder.play_icon_to_voice.setVisibility(View.VISIBLE);
                            holder.play_icon_to_voice_anim.setVisibility(View.VISIBLE);
                            holder.play_time_to_voice.setVisibility(View.VISIBLE);
                            if (holder.play_time_to_voice != null) {
                                if (!TextUtils.isEmpty(afChapterInfo.desc)) {
                                    holder.play_time_to_voice.setText(afChapterInfo.desc + "s");
                                } else {
                                    BigDecimal voicetime = CommonUtils.getBigDecimalCount(VoiceManager.getInstance().getVoiceTime(download_success_voice_path));
                                    holder.play_time_to_voice.setText(voicetime.intValue() + "s");
                                }

                                holder.play_time_to_voice.setTag(download_success_voice_path);
                            }
                            afChapterInfo.download_success = true;
                            holder.lin_play_icon_to_voice.setClickable(true);
                            holder.play_icon_to_voice.setClickable(true);
                        } else {
                            afChapterInfo.download_success = false;
                        }
                    } else {
                        holder.lin_play_icon_to_voice.setBackgroundResource(R.drawable.broadcast_voiceload);
                        holder.lin_play_icon_to_voice.setGravity(Gravity.CENTER);
                        holder.gifImageView.setVisibility(View.VISIBLE);
                        holder.play_icon_to_voice.setVisibility(View.GONE);
                        holder.play_icon_to_voice_anim.setVisibility(View.GONE);
                        holder.play_time_to_voice.setVisibility(View.GONE);

                        holder.lin_play_icon_to_voice.setClickable(false);
                        holder.play_icon_to_voice.setClickable(false);
                        afChapterInfo.download_success = false;
                    }
                    if (CacheManager.getInstance().getGifDownLoadMap().containsKey(info.url)) {
                        CacheManager.getInstance().getGifDownLoadMap().remove(info.url);
                    }
                }
            });
        }
    }

    private boolean IsVoiceFileExistsSDcard(String voicePath) {
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

    /**
     * 设置广播图片的排布与宽高
     *
     * @param imageView
     * @param isSingle
     * @param source_pic_url
     */
    public static void setDefaultImageLayoutParams(View imageView, boolean isSingle, String source_pic_url, AfMFileInfo mfile, String picRule) {
        int limit = (int) (ImageUtil.DISPLAYW - picLayoutMargin * 2);
		/*int zoom_px = 0;
		if (isSingle) {
			if (TextUtils.isEmpty(source_pic_url) || !source_pic_url.contains("_")) {
				// if (info.type == BroadCastImageInfo.OTHER_TYPE) {
				FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(limit, limit);
				imageView.setLayoutParams(params);
				// }else {
				// LinearLayout.LayoutParams params = new
				// LinearLayout.LayoutParams(imageView.getWidth(),
				// imageView.getHeight());
				// imageView.setLayoutParams(params);
				// }
			} else {
				int source_w = CommonUtils.split_source_w(source_pic_url);
				int source_h = CommonUtils.split_source_h(source_pic_url);
				if (source_w > 0 && source_h > 0) {
					// imageView.setLayoutParams(new
					// FlowLayout.LayoutParams(source_w, source_h));
					zoom_px = source_h * limit / source_w;// (int)Math.round((
															// source_h * limit
															// * 1.0 / source_w
															// ));
					imageView.setLayoutParams(new FlowLayout.LayoutParams(limit, zoom_px));
				} else {
					imageView.setLayoutParams(new FlowLayout.LayoutParams(limit, limit));
				}
			}
		} else {*/
        int _pw = limit / 3;
        limit = _pw - perPicMargin;
        int _h = limit, _w = limit;
        if (TextUtils.isEmpty(picRule) && isSingle) {//如果是老的广播 并且是单图
            if (!TextUtils.isEmpty(source_pic_url) && source_pic_url.contains("_")) {
                int source_w = CommonUtils.split_source_w(source_pic_url);
                int source_h = CommonUtils.split_source_h(source_pic_url);
                if (source_w > 0 && source_h > 0) {
                    int zoom_px = source_h * limit / source_w;
                    _w = limit;
                    _h = zoom_px;
                }
            }
        } else if (mfile.resize != null) {
            _w = mfile.resize[0];
            _h = mfile.resize[1];
        }
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(_w, _h);
        params.setMargins(0, 0, perPicMargin, perPicMargin);
        imageView.setLayoutParams(params);
//		}
    }

    /**
     * The Broadcast module preview picture size is defined as follows:
     * Note: W only H wide limit is high, in order to fit widescreen and vertical screen pictures and by limiting the size of geometric fit, for example: widescreen picture to limit the wide scale adaptation, vertical screen pictures according to the limit higher than China "adaptation
     * MDPI (320x480) hdpi (480x800) xhdpi (720x1280) xxhdpi (1080x1920)
     * A single photo thumbnail W:160 H:160 W:320 H:320 W:512 H:512 W:768 H:768
     * 64x64 120x120 180x180 270x270 square thumbnails
     */
    public static void setImageLayoutParams(ImageView imageView, Bitmap bm, boolean isSingle, AfMFileInfo mfile) {
        int limit = (int) (ImageUtil.DISPLAYW - picLayoutMargin * 2);
        int zoom_px = 0;
//			if (isSingle) {
//				if(bm!=null){
//				         zoom_px = (int)Math.round(( bm.getHeight() * limit * 1.0 /  bm.getWidth()));
//				         imageView.setLayoutParams(new FlowLayout.LayoutParams( limit , zoom_px));
//				}
//
//			}else {
        int _pw = limit / 3;
        limit = _pw - perPicMargin;
        int _h = limit, _w = limit;
        if (mfile.resize != null) {
            _w = mfile.resize[0];
            _h = mfile.resize[1];
        }
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(_w, _h);
        params.setMargins(0, 0, perPicMargin, perPicMargin);
        imageView.setLayoutParams(params);

//			}
        imageView.setImageBitmap(bm);
    }

    private IAdapterBroadcastUitls iAdapterBroadcastUitls;

    public void setIAdapterBroadcastUitls(IAdapterBroadcastUitls l) {
        iAdapterBroadcastUitls = l;
    }

    /**
     * 广播通用事件接口
     *
     * @author xiaolong
     */
    public interface IAdapterBroadcastUitls {
        void onClikeLike(BaroadCast_ViewHolder viewHolder, AfChapterInfo afChapterInfo, int position);

        void onClikeMore(DialogItem dialogItem);

        void onBindLookePicture(BaroadCast_ViewHolder viewHolder, AfChapterInfo afChapterInfo, int position, ImageView imageView, int img_index);

        void notifyData_VoiceManagerCompletion();

    }

    private final int TEXT_MAXLENGTH_CUT = 140, TEXT_MAXLENGTH = 190;

    /**//// <summary>
    /// 转半角的函数(DBC case)
    /// </summary>
    /// <param name="input">任意字符串</param>
    /// <returns>半角字符串</returns>
    ///<remarks>
    ///全角空格为12288，半角空格为32
    ///其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
    ///</remarks>
    private String ToDBC(String input) {
        if (!TextUtils.isEmpty(input)) {
            if (input.length() > TEXT_MAXLENGTH_CUT) {//
                String strMore = input.substring(TEXT_MAXLENGTH_CUT, input.length() > TEXT_MAXLENGTH ? TEXT_MAXLENGTH : input.length());
                int tagIndex = strMore.indexOf("#");//找到第一个# 找到就结束了
                int blackIndex = strMore.indexOf(" ");//找到第一个空格 找到就结束了
                int emojiIndex = strMore.indexOf("[");//找到第一个[ 找到就结束了
                if (tagIndex >= 0) {
                    input = input.substring(0, TEXT_MAXLENGTH_CUT + tagIndex);
                } else if (blackIndex >= 0) {
                    input = input.substring(0, TEXT_MAXLENGTH_CUT + blackIndex);
                } else if (emojiIndex >= 0) {
                    input = input.substring(0, TEXT_MAXLENGTH_CUT + emojiIndex);
                } else {
                    if (input.length() > TEXT_MAXLENGTH) {
                        input = input.substring(0, TEXT_MAXLENGTH);
                    }
                }

            }
            char[] c = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == 12288) {
                    c[i] = (char) 32;
                    continue;
                }
                if (c[i] > 65280 && c[i] < 65375)
                    c[i] = (char) (c[i] - 65248);
            }
            return new String(c);
        }
        return input;
    }

    public void setCurPAccountId(String curPAccountId) {
        this.mCurPAccountId = curPAccountId;
    }

    // 判断是否点击关注按钮
    public void AddFollw(final AfProfileInfo profileInfo, final BaroadCast_ViewHolder viewHolder) {
        final String afid = profileInfo.afId;
        final boolean follow = CacheManager.getInstance().isFollow(afid);//防止一个屏幕显示两个账户的数据时出现错误，所以在这样在取一次是否关注的数据
        if (!follow) {
            Update_IsFollow(afid);
            PalmchatApp.getApplication().mAfCorePalmchat.AfHttpFollowCmd(Consts.AFMOBI_FOLLOW_MASTRE, afid, Consts.HTTP_ACTION_A, follow, new AfHttpResultListener() {
                @Override
                public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
                    if (code == Consts.REQ_CODE_SUCCESS) {
                        if (flag == Consts.REQ_FLAG_FOLLOW_LIST) {
                            boolean isFollow = (Boolean) user_data;
                            isFollow(isFollow, profileInfo, viewHolder);
                            EventBus.getDefault().post(new EventFollowNotice(afid));
                        }
                    } else {
                        if (flag == Consts.REQ_FLAG_FOLLOW_LIST) {
                            if(code == Consts.REQ_CODE_4096){
                                Consts.getInstance().showToast(mContext, code, flag, http_code);
                            }else{
                                boolean isFollow = (Boolean) user_data;
                                String msg = "";
                                if (isFollow) {
                                    msg = mContext.getString(R.string.unfollow_failure).replace("XXXX", profileInfo.name);
                                } else {
                                    msg = mContext.getString(R.string.follow_failure).replace("XXXX", profileInfo.name);
                                }
                                ToastManager.getInstance().show(mContext, msg);
                            }
                        }
                    }
                    Update_IsFollow(profileInfo.afId);
                }
            });
            MessagesUtils.addStranger2Db(profileInfo);
        } else if (follow) {//已关注过的
            changeFontPicture(true, viewHolder, profileInfo);
        }
    }

    private void isFollow(boolean bool, final AfProfileInfo profileInfo, final BaroadCast_ViewHolder viewHolder) {
        if (!bool) {// 关注一个人
            MessagesUtils.onAddFollowSuc(Consts.AFMOBI_FOLLOW_TYPE_MASTER, profileInfo);
			/*MessagesUtils.addMsg2Chats(PalmchatApp.getApplication().mAfCorePalmchat,profileInfo.afId, MessagesUtils.ADD_CHATS_FOLLOW);*///关注成功后会chat那边会增加一条消息，
            changeFontPicture(true, viewHolder, profileInfo);
        }

    }

    /**
     * 标识是否点击过 设置为true表示已经点击过了，follow成功或失败后都必须在次执行此方法设置为false
     * @param afid
     */
    public void Update_IsFollow(String afid) {
        if (CacheManager.getInstance().getClickableMap().containsKey(afid + CacheManager.follow_suffix)) {
            if (CacheManager.getInstance().getClickableMap().get(afid + CacheManager.follow_suffix)) {
                CacheManager.getInstance().getClickableMap().put(afid + CacheManager.follow_suffix, false);
            } else {
                CacheManager.getInstance().getClickableMap().put(afid + CacheManager.follow_suffix, true);
            }
        }
    }

    /**
     * 加载列表的时候，判断是否关注过的
     *
     * @param bool
     * @param viewHolder
     */
    private void changeFontPicture(boolean bool, final BaroadCast_ViewHolder viewHolder, AfProfileInfo profileInfo) {
        if (bool) {
            if (viewHolder.mTvBroadcastFollow.getVisibility() == View.VISIBLE) {
                viewHolder.mTvBroadcastFollow.setText(R.string.followed);//已关注过的
                viewHolder.mTvBroadcastFollow.setBackgroundResource(R.drawable.btn_broadcast_follow_d);
                viewHolder.mTvBroadcastFollow.setTextColor(mContext.getResources().getColor(R.color.color_c2c3c8));
                AlphaAnimation animation = new AlphaAnimation(1.0f, 0f);
                animation.setDuration(2500);
                viewHolder.mTvBroadcastFollow.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        viewHolder.mTvBroadcastFollow.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            if (viewHolder.mTvBroadcastForWardFollow.getVisibility() == View.VISIBLE) {
                viewHolder.mTvBroadcastForWardFollow.setText(R.string.followed);
                viewHolder.mTvBroadcastForWardFollow.setBackgroundResource(R.drawable.btn_broadcast_follow_d);
                viewHolder.mTvBroadcastForWardFollow.setTextColor(mContext.getResources().getColor(R.color.color_c2c3c8));
                AlphaAnimation animation = new AlphaAnimation(1.0f, 0f);
                animation.setDuration(2500);
                viewHolder.mTvBroadcastForWardFollow.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        viewHolder.mTvBroadcastForWardFollow.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        } else {
            controlVoluation(viewHolder.mTvBroadcastFollow);
            controlVoluation(viewHolder.mTvBroadcastForWardFollow);
        }
    }

    /**
     * 给控件赋值
     *
     * @param view
     */
    private void controlVoluation(TextView view) {
        Animation ani=view.getAnimation();
        if(ani!=null){//如果还有动画没播完的 就取消动画 清空
            ani.setAnimationListener(null);
            view.clearAnimation();
        }
        view.setText(R.string.follow);
        view.setVisibility(View.VISIBLE);
        view.setBackgroundResource(R.drawable.btn_broadcast_follow_selector);
        view.setTextColor(mContext.getResources().getColorStateList(R.drawable.btn_tv_broadcast_follow_selector));
    }

    /**
     * follow操作
     */
    private void follow(final AfChapterInfo chapterInfo, final AfProfileInfo profileInfo, final BaroadCast_ViewHolder viewHolder) {
        if (null != chapterInfo) {
            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.FOL_PBL);
            PalmchatApp.getApplication().mAfCorePalmchat.AfHttpFriendOpr(null, chapterInfo.afid, Consts.HTTP_ACTION_A, Consts.FRIENDS_PUBLIC_ACCOUNT,
                    (byte) Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT, null, Consts.HTTP_ACTION_A, new AfHttpResultListener() {
                        @Override
                        public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
                            if (code == Consts.REQ_CODE_SUCCESS) {
                                switch (flag) {
                                    case Consts.REQ_FRIEND_LIST:
                                        byte action = (Byte) user_data;
                                        if (action == Consts.HTTP_ACTION_A) {
                                            profileInfo.type = Consts.AFMOBI_FRIEND_TYPE_PFF;
                                            CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).saveOrUpdate(profileInfo, true, true);
                                        }
                                        changeFontPicture(true, viewHolder, profileInfo);
                                        break;
                                }
                            } else {
                                switch (flag) {
                                    case Consts.REQ_FRIEND_LIST:
                                        ToastManager.getInstance().show(mContext, R.string.req_failed);
                                        break;
                                }
                            }
                        }
                    });
        }
    }
}
