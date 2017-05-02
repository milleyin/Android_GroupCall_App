package com.afmobi.palmchat.ui.activity.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobi.customview.VisionFilterSwithView;
import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.utils.VisionConst;
import com.afmobi.utils.VisionFilterType;
import com.afmobigroup.gphone.R;
import com.core.AfResponseComm;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class VisionFilterActivity extends BaseActivity implements View.OnClickListener, VisionFilterSwithView.PhotoSwitchLitener {

    private VisionFilterSwithView mVisionFilterSwitchView;
    private RecyclerView mFilterRecyclerView;
    private LocalRecycelerViewAdapter mLocalRecycelerViewAdapter;
    private List<VisionFilterType> mFilterTypeList;
    private TextView mFilterTitle;

    private int mPictureCount;
    private int mPhotoIndex;
    private int[] mFilterIndexArr;
    private int[] mFilterScrollArr;//记录滚动位置的 便于切换图片后的恢复

    private ArrayList<AfResponseComm.AfMFileInfo> picturePathList;


    @Override
    public void onBackPressed() {
        findViewById(R.id.btn_back).performClick();
    }

    @Override
    public void findViews() {

        Intent mIntent = getIntent();
        if (null != mIntent) {
            mPhotoIndex = mIntent.getIntExtra("photoIndex", 0);
            picturePathList= (ArrayList<AfResponseComm.AfMFileInfo>) mIntent.getSerializableExtra(JsonConstant.KEY_SENDBROADCAST_PICLIST);
        }
        setContentView(R.layout.activity_vsion_filter);
        mFilterTitle = (TextView) findViewById(R.id.tv_filter_title);

        mVisionFilterSwitchView = (VisionFilterSwithView) findViewById(R.id.vision_filter_switch_view);
        mVisionFilterSwitchView.setPhotoIndex(mPhotoIndex);
        mVisionFilterSwitchView.setPhotoSwitchLitener(this);
        mPictureCount = mVisionFilterSwitchView.getPictureCount();

        mFilterRecyclerView = (RecyclerView) findViewById(R.id.rv_filter);
        ScrollSpeedControlLayoutManager linearLayoutMgr = new ScrollSpeedControlLayoutManager(this);
        linearLayoutMgr.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFilterRecyclerView.setLayoutManager(linearLayoutMgr);
        mLocalRecycelerViewAdapter = new LocalRecycelerViewAdapter();
        mFilterRecyclerView.setAdapter(mLocalRecycelerViewAdapter);


        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.ok_button).setOnClickListener(this);

        mFilterTypeList = VisionConst.getTopFilterTypeList();
        setTitle(mPhotoIndex);


        mFilterIndexArr = new int[mPictureCount];
        mFilterScrollArr= new int[mPictureCount];
    }

    @Override
    public void init() {

    }


    public boolean checkFilterApply(){

        boolean filterApplay = false;

        for (int filterIndex: mFilterIndexArr){
            if (filterIndex>0){
                filterApplay = true;
            }

        }
        return filterApplay;
    }



    public void setTitle(int index) {
        mPhotoIndex = index;
        int photoIndex = mPhotoIndex + 1;
        String title=getResources().getString(R.string.photo);
        mFilterTitle.setText(title+"(" + photoIndex + "/" + mPictureCount + ")");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_back) {
            if (checkFilterApply()){
                AppDialog appDialog = new AppDialog(this);
                appDialog.createConfirmDialog(this, null, getString(R.string.exit_dialog_content), null, getString(R.string.exit), new AppDialog.OnConfirmButtonDialogListener() {
                    @Override
                    public void onRightButtonClick() {
                        setResult(1000);
                        finish();
                    }

                    @Override
                    public void onLeftButtonClick() {

                    }
                });
                appDialog.show();
            }
            else{
                setResult(1000);
                finish();
            }
        } else if (id == R.id.ok_button) {
            mVisionFilterSwitchView.saveFilterBitmapToCache();
            if(picturePathList!=null){//所有滤镜过的都要保存到文件
                for(int i=0;i<picturePathList.size();i++){
                    AfResponseComm.AfMFileInfo afMFileInfo=picturePathList.get(i);
                    if(afMFileInfo.isVisionEdit) {
                        mVisionFilterSwitchView.saveFilterBitmapToFile(i, afMFileInfo.local_img_path);
                        afMFileInfo.isVisionEdit = false;
                    }
                    //统计需要
                    afMFileInfo.picFilter=  VisionConst.getStringFilterNameForStatistics(  afMFileInfo.picFilter, mFilterTypeList.get( mFilterIndexArr[i]));


                }
            }
            Intent intent=new Intent();//要返回滤镜的结果  主要为了统计功能
            intent.putExtra(JsonConstant.KEY_SENDBROADCAST_PICLIST, picturePathList);
            setResult(0,intent);
            finish();
        }
    }

    /**
     * 切换一张图片 重新设置滤镜位置
     * @param index
     */
    @Override
    public void onPhotoSwitch(int index) {
        setTitle(index);
        int filterIndex = mFilterIndexArr[index];
//        mFilterRecyclerView.smoothScrollToPosition(filterIndex);
// 根据要求每次切换图片都要让当前选中的图片设置为第2个
        LinearLayoutManager layoutManager = (LinearLayoutManager) mFilterRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemWidth= firstVisiableChildView.getWidth();//每个的宽度
        int scrollX=(position) * itemWidth - firstVisiableChildView.getLeft();//当前scroll位置

        int dScrollX=mFilterScrollArr[index];//(filterIndex-1)*itemWidth;
        int distance=dScrollX-scrollX;
        /* if(scrollX-distance<0){
            distance=scrollX;
        }*/
        mFilterRecyclerView.smoothScrollBy(distance,0);
        mLocalRecycelerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mVisionFilterSwitchView!=null){
            mVisionFilterSwitchView.resetToFilterImage();
        }
    }

    private Handler mHandler=new Handler(   );
    /**
     * 各个滤镜效果适配器
     */
    class LocalRecycelerViewAdapter extends RecyclerView.Adapter<LocalRecycelerViewAdapter.LocalViewHolder> {
       public class LocalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView mImageView;
            TextView mTextView;

            public LocalViewHolder(View itemView) {
                super(itemView);
                mImageView = (ImageView) itemView.findViewById(R.id.iv_filter_index);
                mTextView = (TextView) itemView.findViewById(R.id.tv_filter_index);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(final View v) {
                int position = getLayoutPosition();

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int w = v.getWidth();

                        float x = v.getX();
                        float remainDistance = ImageUtil.DISPLAYW - (x + w);
                        int move = 0;
                        if (remainDistance < w) {
                            move = (int) (w - remainDistance);
                            mFilterRecyclerView.smoothScrollBy(move, 0);
                        } else if (x < w) {
                            move = (int) (-(w - x));
                            mFilterRecyclerView.smoothScrollBy(move, 0);
                        }
                        PalmchatLogUtils.i("WXL", "smoothScrollBy " + String.valueOf(move));
                        notifyDataSetChanged();


                        LinearLayoutManager layoutManager = (LinearLayoutManager) mFilterRecyclerView.getLayoutManager();
                        int firstposition = layoutManager.findFirstVisibleItemPosition();
                        View firstVisiableChildView = layoutManager.findViewByPosition(firstposition);
                        int itemWidth = firstVisiableChildView.getWidth();//每个的宽度
                        int scrollX = (firstposition) * itemWidth - firstVisiableChildView.getLeft();//当前scroll位置
                        mFilterScrollArr[mPhotoIndex] = scrollX + move;//记录当前scroll位置便于恢复
                    }
                },10);
                    if( mFilterIndexArr[mPhotoIndex]!=position) {//如果没有滤镜过 才去滤镜
                        mVisionFilterSwitchView.setFilterType(mFilterTypeList.get(position));
                        mVisionFilterSwitchView.doProcessFilter(VisionFilterActivity.this);
                        mFilterIndexArr[mPhotoIndex] = position;
                        if (picturePathList != null && mPhotoIndex < picturePathList.size()) {
                            picturePathList.get(mPhotoIndex).isVisionEdit = true;
                        }
                    }



            }
        }


        @Override
        public LocalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = getLayoutInflater().inflate(R.layout.item_vision_filter_layout, parent, false);

            view.setBackgroundColor(0xff000000);

            return new LocalViewHolder(view);
        }

        @Override
        public void onBindViewHolder(LocalViewHolder holder, int position) {
            VisionFilterType type = mFilterTypeList.get(position);
            int drawableID = VisionConst.getFilterDrawableByType(type);
            int stringID = VisionConst.getStringIDbyFilterType(type);


            holder.mTextView.setText(stringID);
            holder.mImageView.setImageResource(drawableID);


            if (position == mFilterIndexArr[mPhotoIndex]) {

                holder.mTextView.setTextColor(0xffffffff);

                holder.mImageView.setBackgroundColor(0xff128dcd);

            } else {
                holder.mTextView.setTextColor(0xff666666);
                holder.mImageView.setBackgroundColor(0xff000000);
            }


        }

        @Override
        public int getItemCount() {
            return mFilterTypeList.size();
        }

    }

    class ScrollSpeedControlLayoutManager extends LinearLayoutManager{
        private float MILLISECONDS_PER_INCH = 1000.03f;
        Context mContext;
        public ScrollSpeedControlLayoutManager(Context context) {
            super(context);
            mContext=context;
            MILLISECONDS_PER_INCH =1000;//  mContext.getResources().getDisplayMetrics().density * 100.0f;
        }

        /**
         * 这段重写是因为RecyclerView有bug 无法用wrap_content
         * @param recycler
         * @param state
         * @param widthSpec
         * @param heightSpec
         */
        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
//                super.onMeasure(recycler, state, widthSpec, heightSpec);
            int height = 0;
//                Log.i("msg", "onMeasure---MeasureSpec-" + View.MeasureSpec.getSize(heightSpec));
            int childCount = getItemCount();
            for (int i = 0; i < childCount; i++) {
                View child = recycler.getViewForPosition(i);
                measureChild(child, widthSpec, heightSpec);
//                    if (i % getSpanCount() == 0) {
                int measuredHeight = child.getMeasuredHeight() + getDecoratedBottom(child);
                if(height<measuredHeight) {
                    height = measuredHeight;
                }
//                    }
            }
//                Log.i("msg", "onMeasure---height-" + height);
            setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), height);
        }


        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            LinearSmoothScroller linearSmoothScroller =
                    new LinearSmoothScroller(recyclerView.getContext()) {
                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition) {
                            return ScrollSpeedControlLayoutManager.this
                                    .computeScrollVectorForPosition(targetPosition);
                        }

                        //This returns the milliseconds it takes to
                        //scroll one pixel.
                        @Override
                        protected float calculateSpeedPerPixel
                        (DisplayMetrics displayMetrics) {
                            return 1000;//MILLISECONDS_PER_INCH / displayMetrics.density;
                            //返回滑动一个pixel需要多少毫秒
                        }

                    };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }


        public void setSpeedSlow() {
            //自己在这里用density去乘，希望不同分辨率设备上滑动速度相同
            //0.3f是自己估摸的一个值，可以根据不同需求自己修改
            MILLISECONDS_PER_INCH = mContext.getResources().getDisplayMetrics().density * 5.3f;
        }

        public void setSpeedFast() {
            MILLISECONDS_PER_INCH = mContext.getResources().getDisplayMetrics().density * 5.03f;
        }
    }
}
