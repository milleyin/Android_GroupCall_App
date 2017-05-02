package com.afmobi.palmchat.util.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.universalimageloader.core.assist.LoadedFrom;
import com.afmobi.palmchat.util.universalimageloader.core.imageaware.ImageAware;
import com.afmobi.palmchat.util.universalimageloader.core.imageaware.ImageViewAware;

/**
 * Created by hj on 2016/1/11.
 */
public class DownRoundBitmapDisplayer  implements BitmapDisplayer {

    protected  final int margin ;
    protected  final boolean  isHaveBorder ;
    protected Bitmap oBitmap;//原图
    public DownRoundBitmapDisplayer(boolean isHaveBorder) {
        this(0,isHaveBorder);
    }

    public DownRoundBitmapDisplayer(int margin,boolean isHaveBorder) {
        this.margin = margin;
        this.isHaveBorder=isHaveBorder;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }
        int radius=18+8* ImageUtil.DISPLAYW/480;
        imageAware.setImageDrawable(new RoundedCornerDrawable(bitmap,radius , margin));
    }

    /**
     * modify wxl  改为直接渲染图片的时候 不绘制下边的圆角外的部分 实现只有下部圆角效果
     */
    public static class RoundedCornerDrawable extends Drawable {
        protected final float cornerRadius;
        protected final int margin;

        protected final RectF mRect = new RectF(),
                mBitmapRect;
        protected final BitmapShader bitmapShader;
        protected final Paint paint;

        public RoundedCornerDrawable(Bitmap bitmap, int cornerRadius, int margin) {
            this.cornerRadius = cornerRadius;
            this.margin = margin;

            bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapRect = new RectF (margin, margin, bitmap.getWidth() - margin, bitmap.getHeight() - margin);

            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(bitmapShader);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mRect.set(margin, margin, bounds.width() - margin, bounds.height() - margin);

            // Resize the original bitmap to fit the new bound
            Matrix shaderMatrix = new Matrix();
            shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.FILL);
            bitmapShader.setLocalMatrix(shaderMatrix);

        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawRect(mRect.left,mRect.top,mRect.right,mRect.bottom-cornerRadius,  paint);
            canvas.drawRoundRect(mRect, cornerRadius, cornerRadius, paint);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            paint.setColorFilter(cf);
        }
    }
}
