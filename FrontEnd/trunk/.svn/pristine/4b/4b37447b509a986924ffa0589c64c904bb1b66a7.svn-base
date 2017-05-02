package com.afmobi.palmchat.util.universalimageloader.core.display;
import android.graphics.Bitmap;

import com.afmobi.palmchat.util.universalimageloader.core.assist.LoadedFrom;
import com.afmobi.palmchat.util.universalimageloader.core.imageaware.ImageAware;
import com.afmobi.palmchat.util.universalimageloader.core.imageaware.ImageViewAware;
/**
 * 圆形图片显示者，用于Universal Image Loader显示圆形图片
 * @author xiaolong
 *
 */
public class CircleBitmapDisplayer implements BitmapDisplayer {

    protected  final int margin ;
    protected  final boolean  isHaveBorder ;
    public CircleBitmapDisplayer(boolean isHaveBorder) {
        this(0,isHaveBorder);
    }

    public CircleBitmapDisplayer(int margin,boolean isHaveBorder) {
        this.margin = margin;
        this.isHaveBorder=isHaveBorder;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }

        imageAware.setImageDrawable(new CircleDrawable(bitmap, margin,isHaveBorder));
    }


}
