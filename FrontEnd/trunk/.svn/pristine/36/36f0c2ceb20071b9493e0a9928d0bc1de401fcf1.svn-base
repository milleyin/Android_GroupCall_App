package com.afmobi.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.afmobi.utils.VisionConst;
import com.afmobi.utils.VisionFilterType;
import com.afmobi.utils.VisionUtils;
import com.afmobi.vision.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gpuimage.GPUImage;
import gpuimage.GPUImageSketchFilter;
import gpuimage.GPUImageView;

/**
 * Created by daniel on 16-8-3.
 */
public class VisionFilterSwithView extends FrameLayout {

    private final static int MAX_IMAGE_COUNT  = 9;

    private ViewPager mViewPager;

    private List<Bitmap> mBitmapList;
    private List<Bitmap> mFilterBitmapList;

    private List<ImageView> mImageViewList;

    private List<VisionFilterType> mImageFilterTypes;


    private int mCurrentImageIndex = 0;


    private PhotoSwitchLitener mPhotoSwitchLitener;
    private boolean isShowTouchDownEffeft;//当按下去的时候显示 原图


    public VisionFilterSwithView(Context context) {
        super(context);
        initView(context, null);
    }

    public VisionFilterSwithView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public VisionFilterSwithView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public  void initView(Context context,  AttributeSet attrs){

        View view = View.inflate(getContext(), R.layout.vision_filter_switch_view_layout, this);

        initDatas();

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new VisionFilterViewPagerAdapter());

        mViewPager.addOnPageChangeListener(new LocalOnPageChangeListener());
        /*mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    VisionFilterType filterType = mImageFilterTypes.get(mCurrentImageIndex);
                    if(filterType!=VisionFilterType.ORIGIN) {
                        mImageViewList.get(mCurrentImageIndex).setImageBitmap(mBitmapList.get(mCurrentImageIndex));
                        isShowTouchDownEffeft=true;
                    }
                }else if(event.getAction()==MotionEvent.ACTION_UP){
                    VisionFilterType filterType = mImageFilterTypes.get(mCurrentImageIndex);
                    if(filterType!=VisionFilterType.ORIGIN) {
                        mImageViewList.get(mCurrentImageIndex).setImageBitmap(mFilterBitmapList.get(mCurrentImageIndex));
                        isShowTouchDownEffeft=false;
                    }
                }
                return false;
            }
        });*/
    }

    public void initDatas(){
        mImageViewList = new ArrayList<ImageView>();
        mFilterBitmapList = new ArrayList<>(MAX_IMAGE_COUNT);
        mImageFilterTypes = new ArrayList<>();
        mBitmapList = new ArrayList<Bitmap>();
        List<Bitmap> bitmapList = VisionUtils.getEditInputBitmapCacheList();
        int size = bitmapList.size();
        for (int i=0; i<size; i++){
            addBitmap(bitmapList.get(i));
        }

        for (int i=0; i<MAX_IMAGE_COUNT; i++){
            mImageFilterTypes.add(VisionFilterType.ORIGIN); //default :no filter
        }
    }


    public void saveFilterBitmapToCache(){

        List<Bitmap> bitmapList = VisionUtils.getEditInputBitmapCacheList();
        bitmapList.clear();
        bitmapList.addAll(mFilterBitmapList);

    }


    public int getPictureCount(){
        return mBitmapList.size();
    }

    public void setPhotoIndex(int index){

        mViewPager.setCurrentItem(index);

    }

    public void setPhotoSwitchLitener(PhotoSwitchLitener listener){
        mPhotoSwitchLitener = listener;
    }


    public void addBitmap(Bitmap bitmap){
        mBitmapList.add(bitmap);
        mFilterBitmapList.add(bitmap);

        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundColor(0xff141619);
//        imageView.setPadding(40, 0, 40, 0);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mImageViewList.add(imageView);

    }

    public void setFilterType(int index, VisionFilterType filterType){
        mImageFilterTypes.set(index, filterType);

    }

    public void setFilterType(VisionFilterType filterType){
        setFilterType(mCurrentImageIndex, filterType);
    }


    public void setFilterBitmap(Activity activity,final Bitmap bitmap, final int index){

        Bitmap old = mFilterBitmapList.get(index);

        mFilterBitmapList.set(index, bitmap);
        mImageViewList.get(index).setImageBitmap(bitmap);
        if (old!=null && mBitmapList.get(index) != old){
            old.recycle();
        }
    }

    public void doProcessFilter(final Activity activity){
     /*   new Thread(new Runnable() {
            @Override
            public void run() {*/
                Bitmap bitmap = mBitmapList.get(mCurrentImageIndex);
                VisionFilterType filterType = mImageFilterTypes.get(mCurrentImageIndex);

                final  Bitmap newBitmap = VisionUtils.bitmapFilterFactory(getContext(), bitmap, VisionConst.creatFilterByType(getContext(), filterType));
                setFilterBitmap(activity,newBitmap, mCurrentImageIndex);
                /*if(activity!=null){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setFilterBitmap(newBitmap, mCurrentImageIndex);
                        }
                    });
                }*/

           /* }
        }).start();*/

    }

    /**
     * 保存已滤镜后的图片到文件缓存
     * @param index
     * @param path
     */
    public void saveFilterBitmapToFile(int index,String path){
      Bitmap sourceBitmap=  mFilterBitmapList.get(index);
        FileOutputStream fOut = null;
        if (null != sourceBitmap) {
            try {
                fOut = new FileOutputStream(path);
                sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (null != fOut) {
                    try {
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void resetToFilterImage(){
        VisionFilterType filterType = mImageFilterTypes.get(mCurrentImageIndex);
        if(filterType!=VisionFilterType.ORIGIN) {
            mImageViewList.get(mCurrentImageIndex).setImageBitmap(mFilterBitmapList.get(mCurrentImageIndex));
            isShowTouchDownEffeft=false;
        }
    }
    class VisionFilterViewPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return mBitmapList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = mImageViewList.get(position);

            Bitmap bitmap = mFilterBitmapList.get(position);

            imageView.setImageBitmap(bitmap);

            container.addView(imageView);
            imageView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    VisionFilterType filterType = mImageFilterTypes.get(mCurrentImageIndex);
                    if(filterType!=VisionFilterType.ORIGIN) {
                        mImageViewList.get(mCurrentImageIndex).setImageBitmap(mBitmapList.get(mCurrentImageIndex));
                        isShowTouchDownEffeft=true;
                    }
                    return false;
                }
            });
            imageView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_UP){
                        resetToFilterImage();
                    }
                    return false;
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    class LocalOnPageChangeListener implements ViewPager.OnPageChangeListener{


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(isShowTouchDownEffeft ) {
                VisionFilterType filterType = mImageFilterTypes.get(mCurrentImageIndex);
                if (filterType != VisionFilterType.ORIGIN) {//如果有滤镜 那就恢复
                    mImageViewList.get(mCurrentImageIndex).setImageBitmap(mFilterBitmapList.get(mCurrentImageIndex));
                }
                isShowTouchDownEffeft=false;
            }
        }

        @Override
        public void onPageSelected(int position) {

            mCurrentImageIndex = position;

            if (mPhotoSwitchLitener!=null){
                mPhotoSwitchLitener.onPhotoSwitch(mCurrentImageIndex);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public interface PhotoSwitchLitener{


        void onPhotoSwitch(int index);


    }





}
