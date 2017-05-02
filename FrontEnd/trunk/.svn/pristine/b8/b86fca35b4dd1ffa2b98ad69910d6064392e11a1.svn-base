package com.afmobi.palmchat.ui.activity.social;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.model.DialogItem;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog.ILargeImageDialog;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountDetailsActivity;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.social.AdapterBroadcastUitls.IAdapterBroadcastUitls;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.CollapsibleTextView;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager.SystemBarConfig;
import com.afmobi.palmchat.ui.customview.videoview.VedioManager;
import com.afmobi.palmchat.util.BroadcastUtil;
import com.afmobi.palmchat.util.ByteUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobi.palmchat.util.universalimageloader.core.assist.FailReason;
import com.afmobi.palmchat.util.universalimageloader.core.listener.ImageLoadingListener;
import com.afmobigroup.gphone.R;
import com.core.AfFriendInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfCommentInfo;
import com.core.AfResponseComm.AfLikeInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import de.greenrobot.event.EventBus;

/**
 * AdapterBroadcastMessages 所有广播列表的Adapter
 *
 * @author gtf
 */
public class AdapterBroadcastMessages extends BaseAdapter implements IAdapterBroadcastUitls {
    String TAG = AdapterBroadcastMessages.class.getCanonicalName();
    private Activity mContext;
    // 广播数据列表
    private List<AfChapterInfo> broadcastMessageList = new ArrayList<AfChapterInfo>();
    // 记录comment
    private ArrayList<CommentModel> aModels = new ArrayList<CommentModel>();

    private CacheManager cacheManager;
    private AfProfileInfo myprofileInfo;
    private Handler handler = new Handler();

    private AfPalmchat mAfCorePalmchat;
    private IFragmentBroadcastListener broadcastFragment;
    private AdapterBroadcastUitls adapterBroadcastUitls;
    private LargeImageDialog largeImageDialog;
    private int SHARE_BROADCAST = 1;
    private int FORWARD_BROADCAST = 2;
    private int c_index = -1;

    SystemBarConfig systemBarConfig;
    private int fromType;


    // private int fromType; // add by zhh 来自哪个页面

    /**
     * @param mContext
     * @param broadcastMessageList
     * @param dataType
     * @param broadcastFragment
     * @param fromType             ，new around,Friend Cricle，hottoday
     */
    public AdapterBroadcastMessages(Activity mContext, List<AfChapterInfo> broadcastMessageList, byte dataType, IFragmentBroadcastListener broadcastFragment, int fromType) {
        this.mContext = mContext;
        cacheManager = CacheManager.getInstance();
        myprofileInfo = cacheManager.getMyProfile();
        mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
        this.broadcastFragment = broadcastFragment;
        // this.fromType = fromType;
        setBroadcastMessageList(broadcastMessageList, false);
        adapterBroadcastUitls = new AdapterBroadcastUitls(mContext, fromType);
        this.fromType = fromType;
        adapterBroadcastUitls.setIAdapterBroadcastUitls(this);
    }

/*	public AdapterBroadcastMessages(Context mContext, List<AfChapterInfo> broadcastMessageList, byte dataType, ListViewAddOn listViewAddOn) {
        this.mContext = mContext;
		cacheManager = CacheManager.getInstance();
		myprofileInfo = cacheManager.getMyProfile();
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		setBroadcastMessageList(broadcastMessageList, false);
		adapterBroadcastUitls = new AdapterBroadcastUitls(mContext, BroadcastDetailActivity.FROM_BROADCAST);
		adapterBroadcastUitls.setIAdapterBroadcastUitls(this); 
	}*/

    @Override
    public int getCount() {
        if (broadcastMessageList == null) {
            return 0;
        }
        return broadcastMessageList.size();
    }

    @Override
    public AfChapterInfo getItem(int position) {
        if (broadcastMessageList == null || position >= getCount()) {
            return null;
        }
        return broadcastMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getPosition(String mid) {
        for (int i = 0; i < getCount(); i++) {
            if (mid.equals(getItem(i).mid)) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        if (null != this.broadcastMessageList) {
            this.broadcastMessageList.clear();
        }
    }

    private void setBroadcastMessageList(List<AfChapterInfo> broadcastMessageList, boolean isLoadMore) {
        ArrayList<AfChapterInfo> sendingData = new ArrayList<AfChapterInfo>();
        if (!isLoadMore) {
            int size = this.broadcastMessageList.size();
            for (int i = 0; i < size; i++) {
                AfChapterInfo afChapterInfo = this.broadcastMessageList.get(i);
                if (afChapterInfo.status == AfMessageInfo.MESSAGE_SENTING && afChapterInfo.ds_type == Consts.DATA_FROM_LOCAL) {
                    sendingData.add(0, afChapterInfo);
                }
            }
            this.broadcastMessageList.clear();
        }
        if (null != broadcastMessageList) {
            for (AfChapterInfo afChapterInfo : sendingData) {
                broadcastMessageList.add(0, afChapterInfo);
            }
            this.broadcastMessageList.addAll(broadcastMessageList);
        }

    }

    public void notifyDataSetChanged(List<AfChapterInfo> broadcastMessageList, boolean isLoadMore) {
        //如果有语音正在播放则停止
        if (VoiceManager.getInstance().isPlaying() && (!isLoadMore)) {
            VoiceManager.getInstance().pause();
        }
        /*下拉刷新时停止掉正在播放的视频*/
        if (VedioManager.getInstance().getVideoController() != null)
            VedioManager.getInstance().getVideoController().stop();

        setBroadcastMessageList(broadcastMessageList, isLoadMore);
        notifyDataSetChanged();
    }

    public void RefreshStatus(AfChapterInfo info) {
        for (AfChapterInfo item_info : broadcastMessageList) {
            if (item_info.time.equals(info.time)) {

            }
        }
    }

    public void notifyDataSetChanged(AfChapterInfo afChapterInfo, int _id) {
        int index = ByteUtils.indexOfBroadCastListByid(broadcastMessageList, _id);
        if (index > -1) {
            broadcastMessageList.set(index, afChapterInfo);
        }
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged(AfChapterInfo afChapterInfo) {
        if (broadcastMessageList.size() > 0) {
            broadcastMessageList.add(0, afChapterInfo);
        } else {
            broadcastMessageList.add(afChapterInfo);
        }
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged_removeBy_id(int _id) {
        int index = ByteUtils.indexOfBroadCastListByid(broadcastMessageList, _id);
        PalmchatLogUtils.e(TAG, "----f,index=" + index);
        if (index > -1) {
            broadcastMessageList.remove(index);
        }
        notifyDataSetChanged();
    }

    /**
     * 更新某个id的广播 用于发广播的时候新增一个广播到列表
     *
     * @param mid
     */

    public void notifyDataSetChanged_removeBymid(String mid) {
        int index = ByteUtils.indexOfBroadCastListBymid(broadcastMessageList, mid);
        PalmchatLogUtils.e(TAG, "notifyDataSetChanged_removeBymid,index=" + index);
        if (index > -1) {
            broadcastMessageList.remove(index);
            notifyDataSetChanged();
        }
    }

    public void notifyDataSetChanged_updateLikeBymid(AfChapterInfo afChapterInfo) {
        int index = ByteUtils.indexOfBroadCastListBymid(broadcastMessageList, afChapterInfo.mid);
        if (index > -1) {
            PalmchatLogUtils.e("like__", afChapterInfo.total_like + "");
            broadcastMessageList.set(index, afChapterInfo);
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        BaroadCast_ViewHolder viewHolder = null;

        final AfChapterInfo info = broadcastMessageList.get(position);
        if (null == convertView) {
            viewHolder = new BaroadCast_ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.broadcast_list_messages_item_text, null);
            adapterBroadcastUitls.initTextView(viewHolder, convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BaroadCast_ViewHolder) convertView.getTag();
        }
        if (fromType == BroadcastDetailActivity.FROM_HOTTODAY || fromType == BroadcastDetailActivity.FROM_NEARBY) {
            viewHolder.line.setVisibility(View.VISIBLE);
        }
        adapterBroadcastUitls.bindView(viewHolder, info, position, broadcastFragment.getClass().getName());
//        initVideoView(convertView, info, viewHolder, position);
        bindComment(viewHolder, info, position);
        bindCommentClick(info, viewHolder, position);
        setSending_notComment(viewHolder);
        return convertView;
    }

    public void bindCommentClick(final AfChapterInfo info, final BaroadCast_ViewHolder viewHolder, final int position) {
        viewHolder.chatting_options_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (VedioManager.getInstance().getVideoController() != null)
                    VedioManager.getInstance().getVideoController().stop();

                String hint_msg = mContext.getString(R.string.hint_commet);
                CommentModel commentModel = new CommentModel();
                commentModel.setAfChapterInfo(info);
                commentModel.setHolder(viewHolder);
                commentModel.setHint_msg(hint_msg);
                aModels.clear();
                aModels.add(commentModel);
                if (broadcastFragment != null && broadcastFragment.interface_isAdded()) {
                    broadcastFragment.interface_open_inputbox();
                    setting_scrollBy(view, viewHolder, false);
                }


            }
        });

    }


//    private void initVideoView(View convertView, AfChapterInfo info, BaroadCast_ViewHolder viewHolder, int position) {
//        //如果原文已经被删除了
//        if (info.share_del == DefaultValueConstant.BROADCAST_SHARE_DELETE_FLAG) {
//        } else {
//            if (info.share_info != null) {//如果分享不为空
//                if (info.share_info.getType() != Consts.BR_TYPE_UNKNOW) {//可以识别类型 那么 就要绑定分享的图片等
//
//                    bindVideoView(convertView, viewHolder, info.share_info, position);
//                } else {
//                    viewHolder.lin_pic.setVisibility(View.GONE);
//                }
//
//            } else if (info.getType() != Consts.BR_TYPE_UNKNOW) {// 可以识别类型 那么 就要绑定 图片等
//
//                bindVideoView(convertView, viewHolder, info, position);
//            } else {
//                viewHolder.lin_pic.setVisibility(View.GONE);
//            }
//        }
//
//
//    }
//
//    private void bindVideoView(View convertView, final BaroadCast_ViewHolder viewHolder, AfChapterInfo info, final int position) {
//        if (info.getType() == Consts.BR_TYPE_VIDEO_TEXT || info.getType() == Consts.BR_TYPE_VIDEO) {
//            AfResponseComm.AfMFileInfo afMFileInfo = info.list_mfile.get(0);
//            if (afMFileInfo == null) {
//                return;
//            }
//            Log.i("zhh_20160715", "AdapterBroadcastMessages bindVideoView() ");
//
//            /*根据url 里的图片尺寸对布局的高度进行设置*/
//            if (!TextUtils.isEmpty(afMFileInfo.url)) {
//                try {
//                    int video_w = CommonUtils.split_source_w(afMFileInfo.url);
//                    int video_h = CommonUtils.split_source_h(afMFileInfo.url);
//                    int real_h = ImageUtil.DISPLAYW * video_h / video_w;
//                    real_h = CommonUtils.checkDisplayRatio_h(ImageUtil.DISPLAYW, real_h);
//                    ViewGroup.LayoutParams layout = viewHolder.fl_video_layout.getLayoutParams();
//                    if (layout.height != real_h) {
//                        layout.height = real_h;
//                        viewHolder.fl_video_layout.setLayoutParams(layout);
//                    }
//                } catch (Exception e) {//防止URL中没带宽高参数时做的保护
//                    e.printStackTrace();
//                }
//            }
//
//
////          viewHolder.custom_video_controller.setPosition(position);
//
//            viewHolder.custom_video_controller.setTime(afMFileInfo.duration * 1000);
//            if (afMFileInfo.url != null)
//                viewHolder.custom_video_controller.setPath(afMFileInfo.url);
//            /*缩略图地址*/
//            String thumb_url = afMFileInfo.thumb_url;
//            if (!TextUtils.isEmpty(thumb_url))
//                viewHolder.custom_video_controller.setImgFirstFrame(thumb_url);
//
//            viewHolder.fl_video_layout.setVisibility(View.VISIBLE);
//
//        } else {
//            viewHolder.fl_video_layout.setVisibility(View.GONE);
//        }
//    }


    public void setSending_notComment(final BaroadCast_ViewHolder viewHolder) {
        if (viewHolder.sending.getVisibility() == View.VISIBLE && viewHolder.mTimeTxt.getVisibility() == View.GONE) {

            viewHolder.chatting_options_layout.setClickable(false);
        } else {
            viewHolder.chatting_options_layout.setClickable(true);
        }
    }

    public List<AfChapterInfo> getLists() {
        return broadcastMessageList;
    }

    public void bindComment(final BaroadCast_ViewHolder viewHolder, final AfChapterInfo info, final int position) {
        if (info != null) {
            int comment_count = info.list_comments.size();

            if (comment_count > 0) {
                final AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
                String total = (mContext.getString(R.string.bc_comment_total)).replace(ReplaceConstant.TARGET_FOR_REPLACE, String.valueOf( info.total_comment));
                viewHolder.txt_comment_total.setVisibility(View.GONE);
                // 当评论数大于三条时，底部显示查看更多评论控件，小于等于三条时全部显示
                if (info.total_comment > 3) {
                    viewHolder.view_comment_more.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.view_comment_more.setVisibility(View.GONE);
                }
                viewHolder.txt_comment_total.setText(total);
                if (fromType == BroadcastDetailActivity.FROM_HOTTODAY) {
                    viewHolder.lin_comment.setVisibility(View.GONE);
                } else {
                    viewHolder.lin_comment.setVisibility(View.VISIBLE);
                }
                CollapsibleTextView c_tView;
                viewHolder.listView_comment.removeAllViews();
                for (int i = 0; i < comment_count; i++) {
                    if (i < 3 && comment_count > 0) {
                        final AfCommentInfo afCommentInfo = info.list_comments.get(i);
                        c_tView = new CollapsibleTextView(mContext);
                        String name = afCommentInfo.profile_Info.name;
                        String conent = afCommentInfo.comment;
                        String reply = mContext.getString(R.string.reply_xxxx).replace(Constants.REPLY_STRING, "");
                        String str = "";
                        if (DefaultValueConstant.BROADCAST_PROFILE_PA == afCommentInfo.profile_Info.user_class) {
                            c_tView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_broadcast_list_comment_public_account, 0, 0, 0, 10);
                        } else {
                            c_tView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0, 0);
                        }
                        if (conent == null) {// conent可能为空(当服务器出错的时候)
                            conent = "";
                        }
                        if (conent.contains(reply)) {
                            str = CommonUtils.ToDBC(name + ": " + conent);
                        } else {
                            str = CommonUtils.ToDBC(name + ": " + conent);
                        }

                        OnClickListener nameOnClickListener = new OnClickListener() { // 点击评论者的名字跳转到profle页面
                            @Override
                            public void onClick(View v) {
                                // if (fromType !=
                                // BroadcastDetailActivity.FROM_PROFILE) {
/*								if (info == null) {
                                    return;
								}*/
                                //
                                // if (fromType ==
                                // BroadcastDetailActivity.FROM_FAR_FAR_AWAY) {
                                // new
                                // ReadyConfigXML().saveReadyInt(ReadyConfigXML.FAR_T_PF);
                                // MobclickAgent.onEvent(mContext,
                                // ReadyConfigXML.FAR_T_PF);
                                // }

                                afCommentInfo.profile_Info.afId = afCommentInfo.afid;
                                AfProfileInfo profile = AfFriendInfo.friendToProfile(afCommentInfo.profile_Info);
                                if (DefaultValueConstant.BROADCAST_PROFILE_PA == profile.user_class) {
                                    Intent intent = new Intent(mContext, PublicAccountDetailsActivity.class);
                                    intent.putExtra("Info", profile);
                                    mContext.startActivity(intent);

                                } else if (profile.afId.equals(profileInfo.afId)) { // 点击自己

                                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_MMPF);
//									MobclickAgent.onEvent(mContext, ReadyConfigXML.ENTRY_MMPF);

                                    Bundle bundle = new Bundle();
                                    bundle.putString(JsonConstant.KEY_AFID, profile.afId);
                                    Intent intent = new Intent(mContext, MyProfileActivity.class);
                                    intent.putExtras(bundle);
                                    mContext.startActivity(intent);

                                } else {

                                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BCM_T_PF);
//									MobclickAgent.onEvent(mContext, ReadyConfigXML.BCM_T_PF);
                                    // int from = 1;
                                    // if (fromType ==
                                    // BroadcastDetailActivity.FROM_BROADCAST) {
                                    // from = ProfileActivity.BC_TO_PROFILE;
                                    // } else {
                                    // from = ProfileActivity.HOME_TO_PROFILE;
                                    // }

                                    Intent intent = new Intent(mContext, ProfileActivity.class);
                                    intent.putExtra(JsonConstant.KEY_PROFILE, profile);
                                    intent.putExtra(JsonConstant.KEY_FLAG, true);
                                    intent.putExtra(JsonConstant.KEY_AFID, profile.afId);
                                    intent.putExtra(JsonConstant.KEY_FROM_XX_PROFILE, BroadcastDetailActivity.FROM_BROADCAST);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    mContext.startActivity(intent);
                                }
                                // }
                            }
                        };
                        OnClickListener contentOnClickListener = new OnClickListener() { // 点击评论内容进行回复
                            @Override
                            public void onClick(View v) {
                                PalmchatLogUtils.e(TAG, "position=" + position);
                                String hint_name = mContext.getString(R.string.reply_xxxx).replace(Constants.REPLY_STRING, afCommentInfo.profile_Info.name + Constants.REPLY_STRING_DIVIED);
                                CommentModel commentModel = new CommentModel();
                                commentModel.setAfChapterInfo(info);
                                commentModel.setHolder(viewHolder);
                                String to_afid = afCommentInfo.afid;
                                commentModel.setTo_afid(to_afid, afCommentInfo.profile_Info.name);
                                commentModel.setHint_msg(hint_name);
                                aModels.clear();
                                aModels.add(commentModel);
                                if (broadcastFragment != null && broadcastFragment.interface_isAdded()) {
                                    broadcastFragment.interface_open_inputbox();
                                    setting_scrollBy(v, viewHolder, true);
                                }
                            }
                        };
                        // 将评论中表情转换为图片
                        CharSequence text = EmojiParser.getInstance(mContext).parse(str, name, CommonUtils.dip2px(mContext, 16), nameOnClickListener, contentOnClickListener);
                        c_tView.setDesc(text, TextView.BufferType.NORMAL);
                        c_tView.setMinimumHeight(24);
                        c_tView.setPadding(0, 5, 0, 5);
                        viewHolder.listView_comment.addView(c_tView);
                    }
                }
            } else {
                viewHolder.lin_comment.setVisibility(View.GONE);
            }
        }
    }

    AfHttpResultListener afHttpResultListener = new AfHttpResultListener() {

        @Override
        public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
            PalmchatLogUtils.e(AdapterBroadcastMessages.class.getCanonicalName(), "----flag:" + flag + "----code:" + code + "----result:" + result);
            cacheManager.getsendTime(System.currentTimeMillis()).toString();
            if (code == Consts.REQ_CODE_SUCCESS) {
                int status = AfMessageInfo.MESSAGE_SENT;
                switch (flag) {
                    case Consts.REQ_BCMSG_AGREE:
                        if (user_data != null && user_data instanceof BaroadCast_ViewHolder) {
                            BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
                            if (viewHolder.lin_comment.getTag() instanceof AfChapterInfo) {
                                AfChapterInfo afChapterInfo = (AfChapterInfo) viewHolder.lin_comment.getTag();
                                int like_db_id = (Integer) viewHolder.txt_like.getTag();
                                mAfCorePalmchat.AfDBBCLikeUpdateStatusByID(status, like_db_id);
                                mAfCorePalmchat.AfBcLikeFlagSave(afChapterInfo.mid);
                                viewHolder.txt_like.setTag(0);
                                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LIKE_BCM);
//							MobclickAgent.onEvent(mContext, ReadyConfigXML.LIKE_BCM);
                            }

                        }
                        break;
                    case Consts.REQ_BCMSG_COMMENT:
                        if (user_data != null && user_data instanceof BaroadCast_ViewHolder) {
                            BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
                            int c_db_id = (Integer) viewHolder.txt_comment.getTag();

                            mAfCorePalmchat.AfDBBCCommentUpdateStatusByID(status, c_db_id);
                            if (result != null) {
                                mAfCorePalmchat.AfDBBCCommentUpdateCidByID(String.valueOf(result), c_db_id);
                                if (viewHolder.txt_comment_more.getTag() != null && viewHolder.txt_comment_more.getTag() instanceof AfCommentInfo) {
                                    AfCommentInfo afCommentInfo = (AfCommentInfo) viewHolder.txt_comment_more.getTag();
                                    if (afCommentInfo != null) {//设置新发广播的cid
                                        afCommentInfo.comment_id = String.valueOf(result);
                                    }
                                }
                                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.COM_BCM);
//							MobclickAgent.onEvent(mContext, ReadyConfigXML.COM_BCM);

                            }
                            viewHolder.txt_comment.setTag(0);
                        }
                        break;
                    case Consts.REQ_BCMSG_ACCUSATION:// 举报
                        if (broadcastFragment != null && broadcastFragment.interface_isAdded()) {
                            broadcastFragment.interface_dismissProgressDialog();
                        }
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.REPORT_BCM);
//					MobclickAgent.onEvent(mContext, ReadyConfigXML.REPORT_BCM);

                        ToastManager.getInstance().show(mContext, R.string.report_success);
                        break;
                    case Consts.REQ_BCMSG_DELETE:// 删除广播成功
                        if (broadcastFragment != null && broadcastFragment.interface_isAdded()) {
                            broadcastFragment.interface_dismissProgressDialog();
                        }
                        ToastManager.getInstance().show(mContext, R.string.bc_del_success);
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_BCM_SUCC);
//					MobclickAgent.onEvent(mContext, ReadyConfigXML.DEL_BCM_SUCC);

                        if (user_data != null) {
                            AfChapterInfo afInfo = (AfChapterInfo) user_data;
                            sendUpdateDeleteBroadcastList(afInfo);
                            mAfCorePalmchat.AfDBBCChapterDeleteByID(afInfo._id);
                            if (largeImageDialog != null) {
                                if (largeImageDialog.isShowing()) {
                                    largeImageDialog.dismiss();
                                }
                            }
                        }
                        break;
                    case Consts.REQ_BCCOMMENT_DELETE:// 删除评论成功
                        if (broadcastFragment != null && broadcastFragment.interface_isAdded()) {
                            broadcastFragment.interface_dismissProgressDialog();
                        }
                        ToastManager.getInstance().show(mContext, R.string.success);
                        if (user_data != null) {
                            BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
                            if (viewHolder.lin_comment.getTag() instanceof AfChapterInfo) {
                                AfChapterInfo afChapterInfo = (AfChapterInfo) viewHolder.lin_comment.getTag();
                                if (c_index != -1) {
                                    afChapterInfo.list_comments.remove(c_index);
                                    afChapterInfo.total_comment--;
                                    int position = (Integer) viewHolder.bc_item.getTag();
                                    viewHolder.txt_comment.setText(afChapterInfo.total_comment + "");
                                    int c_db_id = (Integer) viewHolder.txt_comment.getTag();
                                    mAfCorePalmchat.AfDBBCCommentDeleteByID(c_db_id);
                                    bindComment(viewHolder, afChapterInfo, position);
                                }
                                c_index = -1;
                            }
                        }
                        break;
                }
            } else {

                switch (flag) {
                    case Consts.REQ_BCMSG_AGREE:// 点赞失败
                        if (user_data != null && user_data instanceof BaroadCast_ViewHolder) {
                            BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
                            if (viewHolder.lin_comment.getTag() instanceof AfChapterInfo) {
                                AfChapterInfo afChapterInfo = (AfChapterInfo) viewHolder.lin_comment.getTag();
                                if (code == Consts.REQ_CODE_142) {// 广播不存在或已经被删除
                                    Consts.getInstance().showToast(mContext, code, flag, http_code);
                                    mAfCorePalmchat.AfDBBCChapterDeleteByID(afChapterInfo._id);
                                    sendUpdateDeleteBroadcastList(afChapterInfo);// 发广播通知删除广播
                                } else {
                                    if (code == Consts.REQ_CODE_169) {// 重复点赞
                                        mAfCorePalmchat.AfBcLikeFlagSave(afChapterInfo.mid);
                                    } else {// 其他网络等原因引起的赞失败
                                        afChapterInfo.isLike = false;
                                        viewHolder.txt_like.setSelected(false);
                                        viewHolder.txt_like.setClickable(true);
                                        Consts.getInstance().showToast(mContext, code, flag, http_code);
                                    }
                                    if (afChapterInfo != null && afChapterInfo.list_likes.size() > 0) {
                                        afChapterInfo.total_like--;
                                        viewHolder.txt_like.setText(String.valueOf(afChapterInfo.total_like));
                                        int like_db_id = (Integer) viewHolder.txt_like.getTag();
                                        mAfCorePalmchat.AfDBBCLikeDeleteByID(like_db_id);
                                        afChapterInfo.list_likes.remove(0);
                                    }
                                    sendUpdateLikeBroadcastList(afChapterInfo);
                                }
                            }
                        }
                        break;
                    case Consts.REQ_BCMSG_COMMENT:// 评论失败
                        if (user_data != null && user_data instanceof BaroadCast_ViewHolder) {
                            BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
                            if (viewHolder.lin_comment.getTag() instanceof AfChapterInfo) {
                                AfChapterInfo afChapterInfo = (AfChapterInfo) viewHolder.lin_comment.getTag();
                                int c_db_id = (Integer) viewHolder.txt_comment.getTag();
                                int index = ByteUtils.indexOfAfChapterInfo_listCommentBy_id(afChapterInfo.list_comments, c_db_id);
                                if (index != -1) {
                                    afChapterInfo.list_comments.remove(index);
                                }
                                afChapterInfo.total_comment--;
                                int position = (Integer) viewHolder.bc_item.getTag();
                                viewHolder.txt_comment.setText(afChapterInfo.total_comment + "");
                                bindComment(viewHolder, afChapterInfo, position);
                                mAfCorePalmchat.AfDBBCCommentDeleteByID(c_db_id);
                                if (code == Consts.REQ_CODE_142) {// 广播已经被删除了 或不存在
                                    sendUpdateDeleteBroadcastList(afChapterInfo);
                                    mAfCorePalmchat.AfDBBCChapterDeleteByID(afChapterInfo._id);
                                    Consts.getInstance().showToast(mContext, code, flag, http_code);
                                } else if (code == Consts.REQ_CODE_201 || code == Consts.REQ_CODE_203) {// 广播过期
                                    // 无法评论了
                                    Consts.getInstance().showToast(mContext, code, flag, http_code);
                                } else {//4096等
                                    Consts.getInstance().showToast(mContext, code, flag, http_code);// ToastManager.getInstance().show(mContext, R.string.comment_failure);
                                }
                            }
                        }
                        break;
                    case Consts.REQ_BCMSG_ACCUSATION:
                    case Consts.REQ_BCMSG_DELETE:
                    case Consts.REQ_BCCOMMENT_DELETE:
                        if (broadcastFragment != null && broadcastFragment.interface_isAdded()) {
                            broadcastFragment.interface_dismissProgressDialog();
                        }
                        if (code == Consts.REQ_CODE_142 || code == Consts.REQ_CODE_201 || code == Consts.REQ_CODE_202) {
                            if (user_data != null) {
                                AfChapterInfo afInfo = (AfChapterInfo) user_data;
                                sendUpdateDeleteBroadcastList(afInfo);
                                mAfCorePalmchat.AfDBBCChapterDeleteByID(afInfo._id);
                            }
                        }
                        Consts.getInstance().showToast(mContext, code, flag, http_code);
                        break;
                    default:
                        Consts.getInstance().showToast(mContext, code, flag, http_code);
                        break;
                }
            }

        }
    };

    public void sendComment() {
        final CommentModel commentModel = getCommentModel();
        if (commentModel != null) {
            myprofileInfo = cacheManager.getMyProfile();
            final String to_afid = commentModel.getTo_afid();
            final String to_sname = commentModel.getTo_sname();
            final String msg = commentModel.getMsg();
            AfChapterInfo afChapterInfo = commentModel.getAfChapterInfo();
            final int position = getPosition(afChapterInfo.mid);
            final BaroadCast_ViewHolder holder = commentModel.getHolder();

            int status = AfMessageInfo.MESSAGE_SENTING;
            String time = CacheManager.getInstance().getsendTime(System.currentTimeMillis());
            if (holder != null && position != -1) {
                holder.bc_item.setTag(position);
                if (TextUtils.isEmpty(msg)) {
                    ToastManager.getInstance().show(mContext, R.string.hint_commet);
                } else {
                    PalmchatLogUtils.e(AdapterBroadcastMessages.class.getCanonicalName() + "_comment", msg + "mid=" + afChapterInfo.mid);
                    if (!TextUtils.isEmpty(msg)) {
                        int c_db_id = mAfCorePalmchat.AfDBBCCommentInsert(Consts.DATA_BROADCAST_PAGE, afChapterInfo._id, "", time, myprofileInfo.afId, to_afid, msg, AfMessageInfo.MESSAGE_SENTING, AfProfileInfo.profileToFriend(myprofileInfo));
                        AfCommentInfo afCommentInfo = BroadcastUtil.toAfCommentInfo(c_db_id, afChapterInfo._id, to_afid, "", time, myprofileInfo.afId, msg, status, myprofileInfo, to_sname);
                        afChapterInfo.list_comments.add(0, afCommentInfo);
                        afChapterInfo.total_comment++;
                        holder.txt_comment.setText(afChapterInfo.total_comment + "");
                        bindComment(holder, afChapterInfo, position);
                        holder.txt_comment.setTag(c_db_id);
                        holder.lin_comment.setTag(afChapterInfo);
                        holder.txt_comment_more.setTag(afCommentInfo);//传入新发的评论 用户服务器返回评论结果的时候 设置cid
                        PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_COMMENT);
                        mAfCorePalmchat.AfHttpBCMsgOperate(Consts.REQ_BCMSG_COMMENT, myprofileInfo.afId, to_afid, afChapterInfo.mid, msg, null, holder, afHttpResultListener);
                    }
                }

            } else {
                PalmchatLogUtils.e(AdapterBroadcastMessages.class.getCanonicalName(), "评论失败");
            }
            PalmchatLogUtils.e(AdapterBroadcastMessages.class.getCanonicalName(), "评论失败，commentModel = null");
        }
    }

    public void setting_scrollBy(final View view, final BaroadCast_ViewHolder viewHolder, final boolean isReply) {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                int[] location = new int[2];// 保存当前坐标的数组
                int y = -1;
                int mianTab_title_and_meun_Height = 0;
                if (broadcastFragment != null) {
                    if (broadcastFragment.interface_getActivity() instanceof MainTab) {
                        MainTab mainTab = (MainTab) broadcastFragment.interface_getActivity();
                        mianTab_title_and_meun_Height = mainTab.mTitleLinearLayout.getHeight() + mainTab.mTabsView.getHeight();
                        systemBarConfig = ((BaseFragmentActivity) broadcastFragment.interface_getActivity()).systemBarConfig;
                    }
                }
                int StatusBarHeight = 0;
                if (systemBarConfig != null) {
                    StatusBarHeight = systemBarConfig.getStatusBarHeight();//60
                }
                int softkeyboard_H = SharePreferenceUtils.getInstance(mContext).getSoftkey_h() - mianTab_title_and_meun_Height;
                int OldListY = 0;
                if (isReply) {
                    viewHolder.listView_comment.getLocationOnScreen(location);// 获取选中的
                    // Item
                    // 在屏幕中的位置，以左上角为原点
                    // (0,
                    // 0)
                    OldListY = location[1];// Y 坐标
                } else {
                    view.getLocationOnScreen(location);
//    				viewHolder.lin_comment.getLocationOnScreen(location);// 获取选中的
                    // Item
                    // 在屏幕中的位置，以左上角为原点
                    // (0,
                    // 0)
                    OldListY = location[1];//- view.getHeight();// Y 坐标
                }

//				y = (OldListY - softkeyboard_H - mianTab_title_and_meun_Height - StatusBarHeight) + view.getBottom();
                y = ((OldListY) - (PalmchatApp.getApplication().getWindowHeight() - softkeyboard_H - mianTab_title_and_meun_Height - StatusBarHeight));
                if (broadcastFragment != null && broadcastFragment.interface_isAdded()) {
                    broadcastFragment.interface_getmListView().smoothScrollBy(y, 500);
                }
            }
        }, 300);
    }

    class CommentModel {

        public CommentModel() {

        }

        private AfChapterInfo afChapterInfo;
        private BaroadCast_ViewHolder holder;

        private String to_afid;
        private String msg;
        private String hint_name;
        private String to_sname;

        public String getTo_afid() {
            if (to_afid == null) {
                to_afid = "";
            }
            return to_afid;
        }

        public String getTo_sname() {
            return to_sname;
        }

        public void setTo_afid(String to_afid, String to_sname) {
            if (to_afid.equals(myprofileInfo.afId)) {
                to_afid = "";
            }
            this.to_afid = to_afid;
            this.to_sname = to_sname;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getHint_name() {
            return hint_name;
        }

        public void setHint_msg(String hint_msg) {
            if (TextUtils.isEmpty(to_afid)) {
                hint_msg = mContext.getString(R.string.hint_commet);
            }
            this.hint_name = hint_msg;
        }

        public AfChapterInfo getAfChapterInfo() {
            return afChapterInfo;
        }

        public AfChapterInfo setAfChapterInfo(AfChapterInfo afChapterInfo) {
            return this.afChapterInfo = afChapterInfo;
        }

        public BaroadCast_ViewHolder getHolder() {
            return holder;
        }

        public void setHolder(BaroadCast_ViewHolder holder) {
            this.holder = holder;
        }
    }

    public CommentModel getCommentModel() {
        if (aModels.size() > 0) {
            return aModels.get(0);
        }
        return null;
    }

    public void showAppDialog(String msg, final int code, final AfChapterInfo afChapterInfo) {
        AppDialog appDialog = new AppDialog(mContext);
        appDialog.createConfirmDialog(mContext, msg, new OnConfirmButtonDialogListener() {
            @Override
            public void onRightButtonClick() {
                if (afChapterInfo != null) {
                    if (broadcastFragment != null && broadcastFragment.interface_isAdded()) {
                        broadcastFragment.interface_showProgressDialog();
                    }
                    mAfCorePalmchat.AfHttpBCMsgOperate(code, myprofileInfo.afId, "", afChapterInfo.mid, null, null, afChapterInfo, afHttpResultListener);
                }
            }

            @Override
            public void onLeftButtonClick() {

            }
        });
        appDialog.show();
    }

    private void sendUpdateDeleteBroadcastList(AfChapterInfo afChapterInfo) {
        afChapterInfo.eventBus_action = Constants.UPDATE_DELECT_BROADCAST;
        EventBus.getDefault().post(afChapterInfo);
    }

    private void sendUpdateLikeBroadcastList(AfChapterInfo afChapterInfo) {
        afChapterInfo.eventBus_action = Constants.UPDATE_LIKE;
        EventBus.getDefault().post(afChapterInfo);

    }

    @Override
    public void onClikeLike(BaroadCast_ViewHolder viewHolder, AfChapterInfo afChapterInfo, int position) {
        if (!viewHolder.txt_like.isSelected()) {
            String time = String.valueOf(System.currentTimeMillis());
            int like_db_id = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCLikeInsert(Consts.DATA_BROADCAST_PAGE, afChapterInfo._id, "", time, myprofileInfo.afId, AfMessageInfo.MESSAGE_SENTING, AfProfileInfo.profileToFriend(myprofileInfo));

            AfLikeInfo afLikeInfo = BroadcastUtil.toAfLikeinfo(like_db_id, afChapterInfo._id, "", time, myprofileInfo.afId, AfMessageInfo.MESSAGE_SENTING, AfProfileInfo.profileToFriend(myprofileInfo));
            afChapterInfo.list_likes.add(afLikeInfo);
            viewHolder.txt_like.setTag(like_db_id);
            afChapterInfo.total_like++;
            afChapterInfo.isLike = true;
            viewHolder.txt_like.setText(afChapterInfo.total_like + "");
            viewHolder.lin_comment.setTag(afChapterInfo);
            viewHolder.txt_like.setSelected(true);
            viewHolder.txt_like.setClickable(false);
            sendUpdateLikeBroadcastList(afChapterInfo);
            PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_LIKE);
            PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMsgOperate(Consts.REQ_BCMSG_AGREE, myprofileInfo.afId, "", afChapterInfo.mid, null, null, viewHolder, afHttpResultListener);

        }
    }

    @Override
    public void onClikeMore(DialogItem dialogItem) {
        if (VoiceManager.getInstance().isPlaying()) {
            VoiceManager.getInstance().pause();
        }


        AfChapterInfo afChapterInfo = (AfChapterInfo) dialogItem.getObject();
        switch (dialogItem.getTextId()) {
            case R.string.delete:
                String msg = mContext.getString(R.string.bc_del);
                showAppDialog(msg, Consts.REQ_BCMSG_DELETE, afChapterInfo);
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_BCM);
//				MobclickAgent.onEvent(mContext, ReadyConfigXML.DEL_BCM);
                break;
            case R.string.report_abuse:
                String msgAbuse = mContext.getString(R.string.sure_report_abuse);
                showAppDialog(msgAbuse, Consts.REQ_BCMSG_ACCUSATION, afChapterInfo);
                break;
        }
    }

    /**
     * 点击广播图片 查看大图 处理举报和删除图片
     */
    @Override
    public void onBindLookePicture(BaroadCast_ViewHolder viewHolder, final AfChapterInfo afChapterInfo, final int position, final ImageView imageView, final int img_index) {

        largeImageDialog = new LargeImageDialog(mContext, afChapterInfo.list_mfile, img_index, LargeImageDialog.TYPE_BROADCASTLIST, afChapterInfo);
        largeImageDialog.show();
        largeImageDialog.setILargeImageDialog(new ILargeImageDialog() {

            @Override
            public void onItemClickeReportAbuse() {
                String msg = mContext.getString(R.string.sure_report_abuse);
                showAppDialog(msg, Consts.REQ_BCMSG_ACCUSATION, afChapterInfo);
            }

            @Override
            public void onItemClickeDelete() {
                String msg = mContext.getString(R.string.bc_del);
                showAppDialog(msg, Consts.REQ_BCMSG_DELETE, afChapterInfo);

                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_BCM);
//				MobclickAgent.onEvent(mContext, ReadyConfigXML.DEL_BCM);
            }
        });

    }

    @Override
    public void notifyData_VoiceManagerCompletion() {
        notifyDataSetChanged();
    }

    public void pageImgLoad(int start_index, int end_index) {
        // for (; start_index < end_index; start_index++) {
        // PalmchatLogUtils.e(TAG,
        // "start_index = "+start_index+",end_index = "+end_index);
        // AfChapterInfo afChapterInfo = getItem(start_index);
        // adapterBroadcastUitls.bindPictureView(viewHolder, afChapterInfo,
        // start_index, false);
        // }
    }

    public interface IFragmentBroadcastListener {
        void interface_open_inputbox();

        void interface_dismissProgressDialog();

        boolean interface_isAdded();

        Activity interface_getActivity();

        void interface_showProgressDialog();

        XListView interface_getmListView();
    }
}
