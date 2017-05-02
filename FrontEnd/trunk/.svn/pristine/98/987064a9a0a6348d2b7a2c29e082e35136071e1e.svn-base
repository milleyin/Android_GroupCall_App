package com.afmobi.palmchat.util.universalimageloader.core.display;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
/**
 * 实现圆形的Drawable  当前用于在Universal Image Load中 显示圆形图片用。
 * @author xiaolong
 *
 */
public  class CircleDrawable extends Drawable {
    public static final String TAG = "CircleDrawable";

    protected final Paint paint;
    protected final Paint mBorderPaint;
    
    protected final int margin;
    protected final BitmapShader bitmapShader; 
    protected float radius; 
    protected Bitmap oBitmap;//原图
    protected final boolean isHaveBorder;
    public CircleDrawable(Bitmap bitmap,boolean isHaveBorder){
        this(bitmap,0,isHaveBorder);
    }

    public CircleDrawable(Bitmap bitmap, int margin,boolean isHaveBorder) {
        this.margin = margin;
        this.oBitmap = bitmap;
        this.isHaveBorder=isHaveBorder;
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader); 
        
        mBorderPaint=new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(Color.WHITE);
        mBorderPaint.setStrokeWidth( 1.0f);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        computeBitmapShaderSize();
        computeRadius();

    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();//画一个圆圈

        canvas.drawCircle(bounds.width() / 2F,bounds.height() / 2F,radius,paint);
        if(isHaveBorder){
        	canvas.drawCircle(bounds.width() / 2F,bounds.height() / 2F,radius,mBorderPaint);
        }
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


    /**
     * 计算Bitmap shader 大小
     */
    public void computeBitmapShaderSize(){
        Rect bounds = getBounds();
        if(bounds == null) return;
        //选择缩放比较多的缩放，这样图片就不会有图片拉伸失衡
        Matrix matrix = new Matrix();
        float scaleX = bounds.width() / (float)oBitmap.getWidth();
        float scaleY = bounds.height() / (float)oBitmap.getHeight();
        float scale = scaleX > scaleY ? scaleX : scaleY;
        float transX=oBitmap.getWidth()*scale-bounds.width();
        float transY=oBitmap.getHeight()*scale-bounds.height();
        if(transX<0){
            transX=0;
        }
        if(transY<0){
            transY=0;
        }
        matrix.postTranslate(-transX/2,-transY/2);

        matrix.postScale(scale,scale);
        bitmapShader.setLocalMatrix(matrix);
    }

    /**
     * 计算半径的大小
     */
    public void computeRadius(){
        Rect bounds = getBounds();
        radius = bounds.width() < bounds.height() ?
                bounds.width() /2F - margin:
                bounds.height() / 2F - margin;
  
    }
}
