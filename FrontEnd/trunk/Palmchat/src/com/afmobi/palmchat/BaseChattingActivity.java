package com.afmobi.palmchat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.afmobi.palmchat.eventbusmodel.EmoticonDownloadEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.customview.EmojjView;
import com.afmobi.palmchat.ui.customview.FaceFooterView;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobigroup.gphone.R;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.cache.CacheManager;

/**
 * Created by hj on 2016/6/13.
 */
public class BaseChattingActivity extends BaseActivity {
    /**
     * 表情类
     */
    public EmojjView emojjView;
    public AfPalmchat mAfCorePalmchat;
    public FaceFooterView faceFooterView;
    protected String mFriendAfid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //初始化中件间
        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        super.onCreate(savedInstanceState);
    }
    /**
     * 是否已转发,主要用户当设备的设置发生变化时（如字体大小发生变化）
     */
    protected boolean mIsForworded;
    @Override
    public void findViews() {

    }

    @Override
    public void init() {

    }

    public class Params {
        public String itemId;
        public String gifFolderPath;
        public boolean is_face_change;
    }

    /** 是否显示表情下载信息
     * @param item_id
     */
    public void setShowOrNot(String item_id) {
        // TODO Auto-generated method stub
        emojjView.getViewPager().setVisibility(View.GONE);
        emojjView.getPageLayout().setVisibility(View.GONE);

        emojjView.getR_download().setVisibility(View.VISIBLE);
        emojjView.getLinearBtn().setVisibility(View.VISIBLE);
        emojjView.getR_progress().setVisibility(View.GONE);

        if (CacheManager.getInstance().getStoreDownloadingMap().containsKey(item_id)) {
            emojjView.getLinearBtn().setVisibility(View.GONE);
            emojjView.getR_progress().setVisibility(View.VISIBLE);
            int progress = CacheManager.getInstance().getStoreDownloadingMap().get(item_id);
            emojjView.getProgress().setProgress(progress);
            emojjView.getText_progress().setText(progress + "%");
        }
    }

    /**
     * EventBus回调 下载表情时
     *
     * @param event 进度条消息
     */
    public void onEventMainThread(EmoticonDownloadEvent event) {
        String itemId = event.getItem_id();
        boolean isDownloaded = event.isDownloaded();
        int progress = event.getProgress();
        if (progress == 100 || isDownloaded) {
            if (CacheManager.getInstance().getItemid_update().containsKey(itemId)) {
                CacheManager.getInstance().getItemid_update().remove(itemId);
            }
            if (faceFooterView != null) {
                faceFooterView.refreshFaceFootView();
                faceFooterView.showDownlaodStoreFace();
                faceFooterView.showNewSymbolOrNot();
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isforword",mIsForworded);
        super.onSaveInstanceState(outState);
    }

    /**
     * 设置聊天窗口背景
     */
    protected void setChatBg() {
        String background_name = SharePreferenceUtils.getInstance(this).getBackgroundForAfid(CacheManager.getInstance().getMyProfile().afId, mFriendAfid);
        // default bg
        if ("background0".equals(background_name)) {
            getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_chatting)));

        } else {
            Bitmap bitmap;
            int background_resid = CommonUtils.getBcakgroundResIdByName(background_name);
            try {
                bitmap = BitmapFactory.decodeResource(getResources(), background_resid);
                if (bitmap != null) {
                    int width = ImageUtil.DISPLAYW;
                    int height = ImageUtil.DISPLAYH;
                    bitmap = ImageUtil.getImageThumbnail(bitmap, width, height);
                    PalmchatLogUtils.println(" chatting background width  " + width + " height " + height);
                    getWindow().setBackgroundDrawable(new BitmapDrawable(bitmap));

                }

            } catch (OutOfMemoryError e) {
                // TODO: handle exception
                e.printStackTrace();
                getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_chatting)));
            }

        }

    }
}
