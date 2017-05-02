package com.afmobi.palmchat.ui.activity.tagpage.adapter;

import java.util.ArrayList;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.model.DialogItem;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog.ILargeImageDialog;
import com.afmobi.palmchat.ui.activity.social.AdapterBroadcastMessages;
import com.afmobi.palmchat.ui.activity.social.AdapterBroadcastUitls;
import com.afmobi.palmchat.ui.activity.social.AdapterBroadcastUitls.IAdapterBroadcastUitls;
import com.afmobi.palmchat.ui.activity.social.BaroadCast_ViewHolder;
import com.afmobi.palmchat.ui.activity.social.BroadcastDetailActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.videoview.VedioManager;
import com.afmobi.palmchat.util.BroadcastUtil;
import com.afmobi.palmchat.util.ByteUtils;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobigroup.gphone.R;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfLikeInfo;
import com.core.AfResponseComm.AfMFileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import de.greenrobot.event.EventBus;

/**
 * TopPosts 适配器
 *
 * @author Transsion
 */
public class TopPostsAdapter extends BaseAdapter implements IAdapterBroadcastUitls {

    private final String TAG = TopPostsAdapter.class.getSimpleName();
    /**
     * Context
     */
    private Activity mActivity;
    /**
     * 数据列表
     */
    private ArrayList<AfChapterInfo> mAfChapterList;
    /**
     * item填充类
     */
    private AdapterBroadcastUitls adapterBroadcastUitls;
    /**
     * 找个有图片的url
     */
    private String mFirstUrl;
    /**
     * 个人资料
     */
    private AfProfileInfo myprofileInfo;
    /***/
    private CacheManager cacheManager;

    private AfPalmchat mAfCorePalmchat;
    /***/
    private LargeImageDialog largeImageDialog;
    /**
     * 结果回调
     */
    AfHttpResultListener afHttpResultListener = new AfHttpResultListener() {

        @Override
        public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
            PalmchatLogUtils.e(AdapterBroadcastMessages.class.getCanonicalName(), "----flag:" + flag + "----code:" + code + "----result:" + result);
            cacheManager.getsendTime(System.currentTimeMillis()).toString();
            if (code == Consts.REQ_CODE_SUCCESS) {
                int status = AfMessageInfo.MESSAGE_SENT;
                switch (flag) {
                    //点赞成功
                    case Consts.REQ_BCMSG_AGREE: {
                        if (user_data != null && user_data instanceof BaroadCast_ViewHolder) {
                            BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
                            if (viewHolder.lin_comment.getTag() instanceof AfChapterInfo) {
                                AfChapterInfo afChapterInfo = (AfChapterInfo) viewHolder.lin_comment.getTag();
                                int like_db_id = (Integer) viewHolder.txt_like.getTag();
                                mAfCorePalmchat.AfDBBCLikeUpdateStatusByID(status, like_db_id);
                                mAfCorePalmchat.AfBcLikeFlagSave(afChapterInfo.mid);
                                viewHolder.txt_like.setTag(0);
                            }
                        }
                        break;
                    }
                    //举报成功
                    case Consts.REQ_BCMSG_ACCUSATION: {
                        ToastManager.getInstance().show(mActivity, R.string.report_success);
                        break;
                    }
                    // 删除广播成功
                    case Consts.REQ_BCMSG_DELETE: {
                        ToastManager.getInstance().show(mActivity, R.string.bc_del_success);
                        if (user_data != null) {
                            AfChapterInfo afInfo = (AfChapterInfo) user_data;
                            sendUpdateDeleteBroadcastList(afInfo);
                            mAfCorePalmchat.AfDBBCChapterDeleteByID(afInfo._id);
                        }
                        break;
                    }
                }
            } else {
                switch (flag) {
                    // 点赞失败
                    case Consts.REQ_BCMSG_AGREE: {
                        if (user_data != null && user_data instanceof BaroadCast_ViewHolder) {
                            BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
                            if (viewHolder.lin_comment.getTag() instanceof AfChapterInfo) {
                                AfChapterInfo afChapterInfo = (AfChapterInfo) viewHolder.lin_comment.getTag();
                                if (code == Consts.REQ_CODE_142) {// 广播不存在或已经被删除
                                    Consts.getInstance().showToast(mActivity, code, flag, http_code);
                                    mAfCorePalmchat.AfDBBCChapterDeleteByID(afChapterInfo._id);
                                    sendUpdateDeleteBroadcastList(afChapterInfo);// 发广播通知删除广播
                                } else {
                                    if (code == Consts.REQ_CODE_169) {// 重复点赞
                                        mAfCorePalmchat.AfBcLikeFlagSave(afChapterInfo.mid);
                                    } else {// 其他网络等原因引起的赞失败
                                        afChapterInfo.isLike = false;
                                        viewHolder.txt_like.setSelected(false);
                                        viewHolder.txt_like.setClickable(true);
                                        Consts.getInstance().showToast(mActivity, code, flag, http_code);
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
                    }

                    case Consts.REQ_BCMSG_ACCUSATION:
                    case Consts.REQ_BCMSG_DELETE: {
                        if (code == Consts.REQ_CODE_142 || code == Consts.REQ_CODE_201 || code == Consts.REQ_CODE_202) {
                            if (user_data != null) {
                                AfChapterInfo afInfo = (AfChapterInfo) user_data;
                                sendUpdateDeleteBroadcastList(afInfo);
                                mAfCorePalmchat.AfDBBCChapterDeleteByID(afInfo._id);
                            }
                        }
                        Consts.getInstance().showToast(mActivity, code, flag, http_code);
                        break;
                    }
                    default:
                        Consts.getInstance().showToast(mActivity, code, flag, http_code);
                        break;
                }
            }

        }
    };

    /**
     * 构造方法
     *
     * @param
     */
    public TopPostsAdapter(Activity activity) {
        // TODO Auto-generated constructor stub
        this.mActivity = activity;
        this.mAfChapterList = new ArrayList<AfChapterInfo>();
        this.cacheManager = CacheManager.getInstance();
        this.myprofileInfo = cacheManager.getMyProfile();
        this.mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
        adapterBroadcastUitls = new AdapterBroadcastUitls(mActivity, BroadcastDetailActivity.FROM_TAGPAGE);
        adapterBroadcastUitls.setIAdapterBroadcastUitls(this);
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
    public int getCount() {
        // TODO Auto-generated method stub
        return null == mAfChapterList ? 0 : mAfChapterList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null == mAfChapterList ? null : mAfChapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        // TODO Auto-generated method stub
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        BaroadCast_ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new BaroadCast_ViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.broadcast_list_messages_item_text, null);
            adapterBroadcastUitls.initTextView(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BaroadCast_ViewHolder) convertView.getTag();
        }

        AfChapterInfo info = mAfChapterList.get(position);
        if (null != info) {
            adapterBroadcastUitls.bindView(viewHolder, info, position, mActivity.getClass().getName());
            if (TextUtils.isEmpty(mFirstUrl)) {
                ArrayList<AfMFileInfo> al_afFileInfos = info.list_mfile;
                if ((null != al_afFileInfos) && (al_afFileInfos.size() > 0)) {
                    mFirstUrl = al_afFileInfos.get(0).thumb_url;
                }
            }
        }

        return convertView;
    }

    /**
     * 获得一张tag默认图片
     *
     * @return
     */
    public String getImageUrl() {
        return this.mFirstUrl;
    }

    /**
     * 更新数据
     */
    public void updateData(ArrayList<AfChapterInfo> afChapterList, boolean isLoadMore) {
        if (null != afChapterList) {
            if (!isLoadMore) {
                mAfChapterList.clear();
                if (VoiceManager.getInstance().isPlaying()) {
                    VoiceManager.getInstance().pause();
                }

                if (VedioManager.getInstance().getVideoController() != null)
                    VedioManager.getInstance().getVideoController().stop();
            }
            if (afChapterList.size() > 0) {
                mAfChapterList.addAll(afChapterList);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 更新某个id的广播 用于发广播的时候新增一个广播到列表
     *
     * @param
     */
    public void notifyDataSetChanged_removeBymid(String mid) {
        int index = ByteUtils.indexOfBroadCastListBymid(mAfChapterList, mid);
        PalmchatLogUtils.e(TAG, "notifyDataSetChanged_removeBymid,index=" + index);
        if (index > -1) {
            mAfChapterList.remove(index);
            notifyDataSetChanged();
        }
    }

    /**
     * 发送失败删除临时项
     *
     * @param _id
     */
    public void updateDataStatus(int _id) {
        int index = ByteUtils.indexOfBroadCastListByid(mAfChapterList, _id);
        PalmchatLogUtils.e(TAG, "----f,index=" + index);
        if (index > -1) {
            mAfChapterList.remove(index);
        }
        notifyDataSetChanged();

    }

    /**
     * 更新发送广播临时项状态
     *
     * @param afChapterInfo
     */
    public void updateDataStatus(AfChapterInfo afChapterInfo) {
        int index = ByteUtils.indexOfBroadCastListByid(mAfChapterList, afChapterInfo._id);
        if (index > -1) {
            mAfChapterList.set(index, afChapterInfo);
        }
        notifyDataSetChanged();
    }


    /**
     * 发了广播后更新本地list
     *
     * @param afChapterInfo
     */
    public void updateData(AfChapterInfo afChapterInfo) {
        if (null != afChapterInfo) {
            if (mAfChapterList.size() > 0) {
                mAfChapterList.add(0, afChapterInfo);
            } else {
                mAfChapterList.add(afChapterInfo);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 清理数据
     */
    public void cleanData() {
        if (null != mAfChapterList) {
            mAfChapterList.clear();
        }
    }

    /**
     * 更新点赞和评论
     *
     * @param afChapterInfo
     */
    public void updateLikeOrComment(AfChapterInfo afChapterInfo) {
        int index = ByteUtils.indexOfBroadCastListBymid(mAfChapterList, afChapterInfo.mid);
        if (index > -1) {
            mAfChapterList.set(index, afChapterInfo);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onClikeLike(BaroadCast_ViewHolder viewHolder, AfChapterInfo afChapterInfo, int position) {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        if (VoiceManager.getInstance().isPlaying()) {
            VoiceManager.getInstance().pause();
        }


        AfChapterInfo afChapterInfo = (AfChapterInfo) dialogItem.getObject();
        switch (dialogItem.getTextId()) {
            case R.string.delete:
                String msg = mActivity.getString(R.string.bc_del);
                showAppDialog(msg, Consts.REQ_BCMSG_DELETE, afChapterInfo, dialogItem.getPositionId());
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_BCM);
//				MobclickAgent.onEvent(mActivity, ReadyConfigXML.DEL_BCM);
                break;
            case R.string.report_abuse:
                String msgAbuse = mActivity.getString(R.string.sure_report_abuse);
                showAppDialog(msgAbuse, Consts.REQ_BCMSG_ACCUSATION, afChapterInfo, dialogItem.getPositionId());
                break;
        }
    }


    @Override
    public void onBindLookePicture(BaroadCast_ViewHolder viewHolder, final AfChapterInfo afChapterInfo, final int position, ImageView imageView, int img_index) {
        // TODO Auto-generated method stub
        largeImageDialog = new LargeImageDialog(mActivity, afChapterInfo.list_mfile, img_index, LargeImageDialog.TYPE_BROADCASTLIST, afChapterInfo);
        largeImageDialog.show();
        largeImageDialog.setILargeImageDialog(new ILargeImageDialog() {

            @Override
            public void onItemClickeReportAbuse() {
                String msg = mActivity.getString(R.string.sure_report_abuse);
                showAppDialog(msg, Consts.REQ_BCMSG_ACCUSATION, afChapterInfo, position);
            }

            @Override
            public void onItemClickeDelete() {
                String msg = mActivity.getString(R.string.bc_del);
                showAppDialog(msg, Consts.REQ_BCMSG_DELETE, afChapterInfo, position);

                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_BCM);
//				MobclickAgent.onEvent(mActivity, ReadyConfigXML.DEL_BCM);
            }
        });
    }

    /**
     * dialog
     *
     * @param msg
     * @param code
     * @param afChapterInfo
     * @param position
     */
    public void showAppDialog(String msg, final int code, final AfChapterInfo afChapterInfo, final int position) {
        AppDialog appDialog = new AppDialog(mActivity);
        appDialog.createConfirmDialog(mActivity, msg, new OnConfirmButtonDialogListener() {
            @Override
            public void onRightButtonClick() {
                if (afChapterInfo != null) {

                    mAfCorePalmchat.AfHttpBCMsgOperate(code, myprofileInfo.afId, "", afChapterInfo.mid, null, null, afChapterInfo, afHttpResultListener);
                }
            }

            @Override
            public void onLeftButtonClick() {

            }
        });
        appDialog.show();
    }


    @Override
    public void notifyData_VoiceManagerCompletion() {
        // TODO Auto-generated method stub
        notifyDataSetChanged();
    }
}
