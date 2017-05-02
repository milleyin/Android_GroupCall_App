package com.afmobi.utils;

import android.content.Context;
import android.graphics.PointF;

import com.afmobi.custom.filter.IFImageFilter;
import com.afmobi.custom.filter.TFImageFilter;
import com.afmobi.custom.filter.IFLookupFilter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;

import gpuimage.GPUImageFilter;
import gpuimage.GPUImageFilterGroup;
import gpuimage.GPUImageToneCurveFilter;
import gpuimage.GPUImageView;

/**
 * Created by daniel@afmobi on 2016/5/23.
 */
public class VisionFilterManager {




    private GPUImageView mGpuImageView;

    private VisionFilterGroup mGpuImageGroupFilter;

    private GPUImageToneCurveFilter mLuxImageLuxFilter;

    private Context mContext;

    private VisionFilterType mCurrToolFilterType;

    private TFImageFilter mCurrToolFilter;

    private static LinkedHashSet<VisionFilterType> mToolFilterTypeSet = new LinkedHashSet<>();;


    public VisionFilterManager(Context context, GPUImageView gpuImageView){

        mContext = context;

        this.mGpuImageView = gpuImageView;
        mLuxImageLuxFilter = new GPUImageToneCurveFilter();
        mGpuImageGroupFilter = new VisionFilterGroup(mLuxImageLuxFilter);

        mGpuImageView.setFilter(mGpuImageGroupFilter);

    }

    public void  setTopFilter(VisionFilterType type){

        if (type == VisionFilterType.TYPE_NONE){
            mGpuImageGroupFilter.clearTopFilter();
        }
        else{
            mGpuImageGroupFilter.setTopFilter(creatImageFilter(type));
        }
        mGpuImageView.setFilter(mGpuImageGroupFilter);
    }

    public void addToolFilter(VisionFilterType type){

        if (!mToolFilterTypeSet.contains(type)){
            mToolFilterTypeSet.add(type);
            mGpuImageGroupFilter.addToolFilter(creatImageFilter(type));
        }
    }

    public void clearToolFilter(VisionFilterType type){
        if (mToolFilterTypeSet.contains(type)){
            mToolFilterTypeSet.remove(type);
            mGpuImageGroupFilter.clearToolFilter(VisionConst.getFilterClassByType(type));
        }

    }
    public GPUImageFilter creatImageFilter(Class<?> clazz, VisionFilterType type){
        GPUImageFilter filter = null;

        try {

            if (clazz.getSuperclass()==IFImageFilter.class){

                Constructor<?> ctor = (Constructor<Context>) clazz.getConstructor(Context.class);
                filter = (GPUImageFilter) ctor.newInstance(mContext);
            }
            else{
                filter = (GPUImageFilter) clazz.newInstance();

                if (filter.getClass() == IFLookupFilter.class){
                    IFLookupFilter tfLookupFilter = (IFLookupFilter) filter;
                    tfLookupFilter.setFilterType(mContext, type);
                }
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return filter;

    }

    public GPUImageFilter creatImageFilter(VisionFilterType type){

        Class<?> filterClass = VisionConst.getFilterClassByType(type);
        return creatImageFilter(filterClass, type);

    }

    public void setLuxFilterValue(int value){

        int base = 192;
        value -= 50;
        float output = base + value/2;
        float input = base - value/2;
        float pointRate = 1.0f / 255;

        PointF[] pointFs = new PointF[4];

        pointFs[0] = new PointF(0f*pointRate, 0f*pointRate);

        pointFs[1] = new PointF(128f*pointRate, 128f*pointRate);


        pointFs[2] = new PointF(input*pointRate, output*pointRate);

        pointFs[3] = new PointF(255f*pointRate, 255f*pointRate);
        GPUImageToneCurveFilter filter = (GPUImageToneCurveFilter) mGpuImageGroupFilter.getLuxFilter();

        filter.setRgbCompositeControlPoints(pointFs);


/*        GPUImageLuminanceRangeFilter filter = (GPUImageLuminanceRangeFilter) mGpuImageGroupFilter.getLuxFilter();

        filter.setBrightness(VisionUtils.floatRange(value - 50, 100, 0, 1));*/
    }


    public void entryToolOperation(VisionFilterType type){
        mCurrToolFilterType = type;

        Class<?> clazz = VisionConst.getFilterClassByType(type);
        GPUImageFilter filter = mGpuImageGroupFilter.findExistFilterByClass(clazz);

        if (filter==null){
            filter = creatImageFilter(clazz, type);
        }

        mCurrToolFilter = (TFImageFilter) filter;
        mGpuImageGroupFilter.addToolFilter(filter);
        mGpuImageView.setFilter(mGpuImageGroupFilter);
    }

    public void exitToolOperation(boolean done){

        Class<?> clazz = VisionConst.getFilterClassByType(mCurrToolFilterType);

        if (!done){
            mGpuImageGroupFilter.clearToolFilter(clazz);
            mGpuImageView.setFilter(mGpuImageGroupFilter);
        }
        mCurrToolFilter = null;
        mCurrToolFilterType = VisionFilterType.TYPE_NONE;

    }

    public void setCurrToolValue(int value){
        setCurrToolValue(0, value);
    }
    public void setCurrToolValue(int type, int value){

        mCurrToolFilter.setValue(type, value);
        mGpuImageView.requestRender();
    }

    public void setCurrToolExtraValue(int value){
        mCurrToolFilter.setValueExtra(0, value);
        mGpuImageView.requestRender();
    }

    public void setCurrToolExtraValue(int type, int value){
        mCurrToolFilter.setValueExtra(type, value);
        mGpuImageView.requestRender();
    }

    public int getCurrToolValue(int type){
        return mCurrToolFilter.getValue(type);
    }

    public int getCurrToolValue(){
        return mCurrToolFilter.getValue(0);
    }


    public int getCurrToolExtraValue(int type){
        return mCurrToolFilter.getValueExtra(type);
    }
    public int getCurrToolExtraValue(){
        return mCurrToolFilter.getValueExtra(0);
    }


    //add for test;
    public GPUImageFilter getToolFilter(VisionFilterType type){

        return mGpuImageGroupFilter.findExistFilterByClass(VisionConst.getFilterClassByType(type));
    }



    public static  class VisionFilterGroup extends GPUImageFilterGroup{

        private boolean mTopFilterExsited = false;

        public VisionFilterGroup(GPUImageFilter luxFilter){
            mFilters.add(luxFilter);

        }

        public void setTopFilter(GPUImageFilter filter){
            if (filter == null) {
                return;
            }

            if (mTopFilterExsited){
                GPUImageFilter tmpFillter = mFilters.get(1);
                mFilters.remove(1);
                tmpFillter.destroy();
            }
            mTopFilterExsited = true;
            mFilters.add(filter);
            updateMergedFilters();

        }

        public void clearTopFilter(){
            if (!mTopFilterExsited){

                GPUImageFilter tmpFillter = mFilters.get(1);
                mFilters.remove(1);
                updateMergedFilters();
                tmpFillter.destroy();
            }
        }

        public void addToolFilter(GPUImageFilter filter){

            if (filter!=null){
                addFilter(filter);
            }
        }

        public void clearToolFilter(Class<?> clazz){

            for (GPUImageFilter filter: mFilters){

                Class<?> filterClass = filter.getClass();

                if (filterClass == clazz){
                    mFilters.remove(filter);
                    filter.destroy();
                    updateMergedFilters();
                    break;
                }
            }

        }

        public GPUImageFilter getLuxFilter(){

            return mFilters.get(0);
        }

        public GPUImageFilter findExistFilterByClass(Class<?> clazz){

            for (GPUImageFilter filter : mFilters){

                Class<?> filterClass = filter.getClass();

                if (clazz == filterClass){

                    return filter;
                }
            }
            return null;
        }
    }



}







