package com.afmobi.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gpuimage.GPUImage;
import gpuimage.GPUImageFilter;

/**
 * Created by daniel@afmobi on 2016/5/19.
 */


public class VisionUtils {

    public final static int IMAGE_MAX_WIDTH = 720;
    public final static int IMAGE_MAX_HEIGHT = 960;

    private static  List<Bitmap> mEditInputBitmapCacheList = new ArrayList<Bitmap>();


    public static Bitmap bitmapFilterFactory(Context context, Bitmap bitmap, GPUImageFilter filter) {

        if (bitmap != null && filter != null) {

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newWidth = width, newHeight = height;

            if (width > IMAGE_MAX_WIDTH || height > IMAGE_MAX_HEIGHT) {

                if (width * IMAGE_MAX_HEIGHT > height * IMAGE_MAX_WIDTH) {
                    newHeight = height > IMAGE_MAX_HEIGHT ? IMAGE_MAX_HEIGHT : height;

                    newWidth = newHeight * width / height;
                } else {
                    newWidth = width > IMAGE_MAX_WIDTH ? IMAGE_MAX_WIDTH : width;

                    newHeight = newWidth * height / width;
                }
            }

            return bitmapFilterFactory(context, bitmap, newWidth, newHeight, filter);
        } else {
            return null;
        }
    }

    public static Bitmap bitmapFilterFactory(Context context, Bitmap bitmap, int width, int height, GPUImageFilter filter) {

        if (context != null && bitmap != null && filter != null && width > 0 && height > 0) {

            BitmapDrawable drawable = null;
            GPUImage gpuImage = new GPUImage(context);
            bitmap = scaleBitmap(bitmap, width, height);

            gpuImage.setFilter(filter);

            gpuImage.setImage(bitmap);

            return gpuImage.getBitmapWithFilterApplied();

        } else {
            return null;
        }
    }

    public static Bitmap bitmapFilterFactory(Context context, Bitmap bitmap, float scaleRatio, GPUImageFilter filter) {

        if (context != null && bitmap != null && filter != null) {

            BitmapDrawable drawable = null;
            GPUImage gpuImage = new GPUImage(context);
            int width = (int) ((float) bitmap.getWidth() * scaleRatio);
            int height = (int) ((float) bitmap.getHeight() * scaleRatio);
            bitmap = scaleBitmap(bitmap, width, height);

            gpuImage.setFilter(filter);

            gpuImage.setImage(bitmap);

            return gpuImage.getBitmapWithFilterApplied();

        } else {
            return null;
        }
    }

    public static Bitmap scaleBitmap(Bitmap bm, int newWidth, int newHeight) {

        int width = bm.getWidth();
        int height = bm.getHeight();
        if (newHeight == height && newWidth == newWidth) {
            return bm;
        } else {
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                    true);
            return bitmap;
        }
    }


    public int setFilterValue(GPUImageFilter filter, int value) {

        int result = 0;


        return result;

    }

    public static float floatRange(int indexer, int base, float start, float end) {

        float delta = (end - start) * indexer / base;

        float result = start + delta;

        return result;


    }


    public static float getImageRatio(String imagePath) {

        Float imageRatio = 1.0f;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);//没有加载图片，仅获取到图片分辨率

        if (options.outHeight > 0 && options.outHeight > 0) {

            imageRatio = (float) options.outWidth / options.outHeight;
        }

        return imageRatio;

    }

    public static float getImageRatio(Bitmap bitmap) {

        Float imageRatio = 1.0f;

        if (bitmap != null) {

            imageRatio = (float) bitmap.getWidth() / bitmap.getHeight();

        }

        return imageRatio;

    }


    public static List<Bitmap> getEditInputBitmapCacheList(){
        return mEditInputBitmapCacheList;
    }




}
