package com.afmobi.palmchat.ui.activity.palmcall;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfPalmCallResp;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by heguiming on 2016/6/27.
 */
public class PalmCallAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList<AfPalmCallResp.AfPalmCallHotListItem> mHotList = new ArrayList<>();
    //private ArrayList<AfFriendInfo> listInfo;
    private ArrayList<CallGridAdapter> mListItem = new ArrayList<>();
    public static final String DOWNLOAD_SUCCESS_SUFFIX = "_COMPLETE"; // Successful download suffix
    public static final int STARTVOICE = 1;
    public static final int STOPVOICE = 2;
    /**好友详情信息*/
    /*private HashMap<String,AfFriendInfo> mHashMapFriendInfos;*/
    /**
     * 本机号的语音简介
     */
    //显示自己还剩多长通话时间
    private int mTime;
    private String mCurAudioIntro = "";
    /***/
    private String mCurAfId = "";
    //保存列表刷新时间
    private long RefreshTiem;
    private LinearLayout.LayoutParams mColLayoutParams;
    private AnimationDrawable animDrawable;// 播放语音播放效果的动画
    public PalmCallAdapter(Activity activity) {
        this.mActivity = activity;
        this.mHotList = new ArrayList<AfPalmCallResp.AfPalmCallHotListItem>();
        /*this.mHashMapFriendInfos = new HashMap<String,AfFriendInfo>();*/
        this.mCurAfId = CacheManager.getInstance().getMyProfile().afId;

        int dip = mActivity.getResources().getDimensionPixelSize(R.dimen.palmcall_Hostlist_intro_Heigth);//增加头像下面控件的高度
        int displayWidth = ImageUtil.DISPLAYW / 2;//iamge头像显示的宽高
        mColLayoutParams = new LinearLayout.LayoutParams(displayWidth, displayWidth + dip);
        mColLayoutParams.weight = 1.0f;
        PalmCallVoiceManager.getInstance().setActivity(activity);
    }

    @Override
    public int getCount() {
        return mListItem.size();
    }

    @Override
    public CallGridAdapter getItem(int position) {
        /*return mListItem.get(position);*/
        if (position > 0 && position <= getCount()) {
            return mListItem.get(position);
        } else {
            return mListItem.get(0);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mViewHolder viewHolder;
        final ViewHolder viewHolder1;
        final ViewHolder viewHolder2;
        if (convertView == null) {
            viewHolder = new mViewHolder();
            viewHolder1 = new ViewHolder();
            viewHolder2 = new ViewHolder();

            convertView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_palmcall_item, null);

            View rl1 = convertView.findViewById(R.id.rl1);
            View rl2 = convertView.findViewById(R.id.rl2);

            viewHolder1.mIvCallPhoto = (ImageView) rl1.findViewById(R.id.iv_call_photo);
            viewHolder1.mTvCallAge = (TextView) rl1.findViewById(R.id.tv_call_age);
            viewHolder1.mTvCallName = (TextView) rl1.findViewById(R.id.tv_call_name);
            viewHolder1.mTvCallAnswerinlg = (TextView) rl1.findViewById(R.id.tv_call_answerinlg);
            viewHolder1.mIvCallPlay = (ImageView) rl1.findViewById(R.id.iv_call_play);
            /*viewHolder1.mIvPalmcallCall = (ImageView)rl1.findViewById(R.id.iv_palmcall_call);*/

            viewHolder2.mIvCallPhoto = (ImageView) rl2.findViewById(R.id.iv_call_photo2);
            viewHolder2.mTvCallAge = (TextView) rl2.findViewById(R.id.tv_call_age2);
            viewHolder2.mTvCallName = (TextView) rl2.findViewById(R.id.tv_call_name2);
            viewHolder2.mTvCallAnswerinlg = (TextView) rl2.findViewById(R.id.tv_call_answerinlg2);
            viewHolder2.mIvCallPlay = (ImageView) rl2.findViewById(R.id.iv_call_play2);
            /*viewHolder2.mIvPalmcallCall = (ImageView)rl2.findViewById(R.id.iv_palmcall_call);*/

            viewHolder1.rl = rl1;
            viewHolder2.rl = rl2;

            viewHolder.viewHolder1 = viewHolder1;
            viewHolder.viewHolder2 = viewHolder2;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (mViewHolder) convertView.getTag();
        }
        bindView(viewHolder, getItem(position));
        return convertView;
    }


    private void bindView(mViewHolder viewHolder, CallGridAdapter item) {

        viewHolder.viewHolder1.rl.setLayoutParams(mColLayoutParams);
        viewHolder.viewHolder2.rl.setLayoutParams(mColLayoutParams);


        if (item.listItem1 != null) {
            viewHolder.viewHolder1.rl.setVisibility(View.VISIBLE);
            getRlView(viewHolder.viewHolder1, item.listItem1);
        } else {
            viewHolder.viewHolder1.rl.setVisibility(View.INVISIBLE);
        }

        if (item.listItem2 != null) {
            viewHolder.viewHolder2.rl.setVisibility(View.VISIBLE);
            getRlView(viewHolder.viewHolder2, item.listItem2);
        } else {
            viewHolder.viewHolder2.rl.setVisibility(View.INVISIBLE);
        }
    }

    private void getRlView(final ViewHolder viewHolder, final AfPalmCallResp.AfPalmCallHotListItem listItem1) {
        //final AfFriendInfo afFriendInfo =((mHashMapFriendInfos==null)||(mHashMapFriendInfos.get(mHotList.get(position).afid)==null))?(new AfFriendInfo()):mHashMapFriendInfos.get(mHotList.get(position).afid);
        viewHolder.mTvCallName.setText(listItem1.name);
        viewHolder.mTvCallAge.setText(listItem1.age + "");
        viewHolder.mTvCallAge.setBackgroundResource(Consts.AFMOBI_SEX_MALE == listItem1.sex ? R.drawable.bg_palmcall_age_male : R.drawable.bg_palmcall_age_female);
        viewHolder.mTvCallAge.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == listItem1.sex ? R.drawable.bg_palmcall_gender_male : R.drawable.bg_palmcall_gender_female, 0, 0, 0);
        int ss = listItem1.answeringTimes;
        final String url = CommonUtils.splicePalcallAudioUrl(listItem1.mediaDescUrl,listItem1.afid);
        String title = mActivity.getString(R.string.call_ai_times).replace(ReplaceConstant.TARGET_FOR_REPLACE, String.valueOf(ss));
        viewHolder.mTvCallAnswerinlg.setText(title);
        PalmCallVoiceManager.getInstance().setViewHashMap(PalmCallAdapter.class.hashCode(),RefreshTiem);
        if(!TextUtils.isEmpty(url)) {
            viewHolder.mIvCallPlay.setVisibility(View.VISIBLE);
            /*PalmCallVoiceManager.getInstance().showDownloadAnim(url,viewHolder.mIvCallPlay);*/
            PalmchatLogUtils.i("PalmCallVoiceManager","---------position--------:"+ listItem1.name);
            final String id = listItem1.afid.substring(1);
            PalmchatLogUtils.i("PalmCallVoiceManager","---------afid-----------:"+id);
            final String path = PalmCallVoiceManager.getInstance().showStopAnim(url, viewHolder.mIvCallPlay, Integer.parseInt(id));
            viewHolder.mIvCallPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        PalmCallVoiceManager.getInstance().bindVoiceView(viewHolder.mIvCallPlay, path, url, Integer.parseInt(id),PalmCallAdapter.class.hashCode());
                }
            });
        }
        else{
            viewHolder.mIvCallPlay.setVisibility(View.GONE);
        }
        viewHolder.mIvCallPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, PalmCallDetailDialogActivity.class);
                intent.putExtra(JsonConstant.CALLLISTITEM, (Serializable) listItem1);
                intent.putExtra(JsonConstant.CALLLISTTIME, mTime);
                intent.putExtra("listitem", listItem1);
                mActivity.startActivity(intent);
            }
        });
        AfProfileInfo afprofileinfo = new AfProfileInfo();
        afprofileinfo.afId = listItem1.afid;
        afprofileinfo.head_img_path = CommonUtils.splicePalcallCoverUrl(listItem1.coverUrl,listItem1.afid);
        afprofileinfo.name = listItem1.name;
        afprofileinfo.sex = (byte) listItem1.sex;
        ImageManager.getInstance().DisplayAvatarImageCall(viewHolder.mIvCallPhoto, afprofileinfo.head_img_path,
                  afprofileinfo.sex, null, false,false);
        if (TextUtils.isEmpty(mCurAudioIntro) && (null != listItem1.afid) && (listItem1.afid.equals(mCurAfId))) {
            //mCurAudioIntro = listItem1.mediaDescUrl;
            PalmcallManager.getInstance().setPalmcallSelfMediaUrl(url);
        }
    }

    class mViewHolder {
        ViewHolder viewHolder1;
        ViewHolder viewHolder2;
    }

    class ViewHolder {
        View rl;
        TextView mTvCallName, mTvCallAge, mTvCallAnswerinlg;
        ImageView mIvCallPhoto, mIvCallPlay, mIvPalmcallCall;
    }

    public void updatelist(ArrayList<AfPalmCallResp.AfPalmCallHotListItem> mHotListItem, int time, boolean isRefresh,long requestTime) {
        mCurAudioIntro = "";
        mTime = time;
        RefreshTiem = requestTime;
        if (!isRefresh) {
            mHotList.clear();
            mListItem.clear();
            /*mHashMapFriendInfos.clear();*/
        }
        if (mHotListItem != null) {
            mHotList.addAll(mHotListItem);
        }

        if (mHotList != null && mHotList.size() > 12) {
            int mInt = mHotList.size() % 2;//取余
            if (mInt > 0) {
                for (int i = 0; i < mInt; i++) {//循环删除多于的数据，保证每行都显示两条数据
                    mHotList.remove(mHotList.size() - 1);
                }
            }
        }

        setDate();
        notifyDataSetChanged();
    }

    /**
     * 给mListItem添加数据
     */
    private void setDate() {
        int sizemo = mHotList.size() % 2;

        CallGridAdapter listItem = null;

        for (int i = 0; i < mHotList.size(); i++) {
            AfPalmCallResp.AfPalmCallHotListItem item = mHotList.get(i);
            int posion = i % 2;

            switch (posion) {
                case 0:
                    CallGridAdapter callGridAdapter = new CallGridAdapter();
                    callGridAdapter.listItem1 = item;
                    listItem = callGridAdapter;
                    break;
                case 1:
                    listItem.listItem2 = item;
                    break;
                default:
                    break;
            }

            switch (sizemo) {
                case 0:
                    if (listItem.listItem1 != null && listItem.listItem2 != null) {
                        mListItem.add(listItem);
                    }
                    break;
                case 1:
                    if (i == mHotList.size() - 1) {
                        mListItem.add(listItem);
                    } else {
                        if (listItem.listItem1 != null && listItem.listItem2 != null) {
                            mListItem.add(listItem);
                        }
                    }
                    break;
            }

        }
    }
}
