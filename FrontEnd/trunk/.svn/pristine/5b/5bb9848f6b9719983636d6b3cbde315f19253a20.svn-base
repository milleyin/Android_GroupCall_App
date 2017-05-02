package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.afmobigroup.gphone.R;

/**
 * Created by Thinkpad on 2016/6/30.
 */
public class RoundImageView extends ImageView
{

    /** 图片的类型，圆形or圆角  */
    private int type;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;
    private static final int TYPE_CIRCLE_WITHBORDER= 2;//圆带白边
    private static final int TYPE_ROUND_TOP_ARC = 3;//上边圆角 下边直角
    /** 圆角大小的默认值  */
    private static final int BODER_RADIUS_DEFAULT = 10;
    /**  * 圆角的大小 */
    private int mBorderRadius;

    /**  * 绘图的Paint */
    private Paint mBitmapPaint;
    /** * 圆角的半径 */
    private int mRadius;
    /** * 3x3 矩阵，主要用于缩小放大 */
    private Matrix mMatrix;
    /**  * 渲染图像，使用图像为绘制图形着色 */
    private BitmapShader mBitmapShader;
    /** * view的宽度  */
    private int mWidth;
    private RectF mRoundRect;
    private Paint mBorderPaint;
    public RoundImageView(Context context){
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RoundImageView);
        mBorderRadius = a.getDimensionPixelSize(
                R.styleable.RoundImageView_borderRadius, (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                BODER_RADIUS_DEFAULT, getResources()
                                        .getDisplayMetrics()));// 默认为10dp
        type = a.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);// 默认为Circle
        a.recycle();
        if(type==TYPE_CIRCLE_WITHBORDER){
            mBorderPaint=new Paint();
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(Color.WHITE);
            mBorderPaint.setStrokeWidth( 2.0f);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (type == TYPE_CIRCLE||type == TYPE_CIRCLE_WITHBORDER)
        {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }

    }

    private void setUpShader()
    {

        Drawable drawable = getDrawable();
        if (drawable == null)    return;
        Bitmap bmp = drawableToBitamp(drawable);
        if(bmp==null)   return ;
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (type == TYPE_CIRCLE||type==TYPE_CIRCLE_WITHBORDER)
        {
            int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
            scale = mWidth * 1.0f / bSize;
            // shader的变换矩阵，我们这里主要用于放大或者缩小
            mMatrix.setScale(scale, scale);
        } else if (type == TYPE_ROUND )
        {
            scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight()
                    * 1.0f / bmp.getHeight());
            // shader的变换矩阵，我们这里主要用于放大或者缩小
            mMatrix.setScale(scale, scale);
        }else if(type==TYPE_ROUND_TOP_ARC){
             float scaleX = getWidth()  / (float)bmp.getWidth();
            float scaleY = getHeight() / (float)bmp.getHeight();
              scale = scaleX > scaleY ? scaleX : scaleY;
            int sx=getWidth();
            float ssx=(getWidth()-bmp.getWidth()*scale );
            // shader的变换矩阵，我们这里主要用于放大或者缩小
            mMatrix.setScale(scale, scale);
            mMatrix.postTranslate(ssx/2
                    , ( getHeight()-bmp.getHeight()*scale )/2);
        }

        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mBitmapPaint.setShader(mBitmapShader);
    }



    private Bitmap drawableToBitamp(Drawable drawable)
    {
        if (drawable instanceof BitmapDrawable)
        {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        if(w<=0||h<=0){
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (getDrawable() == null)  return;
        setUpShader();
        if (type == TYPE_ROUND)
        {
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
                    mBitmapPaint);
        }if (type == TYPE_ROUND_TOP_ARC)
        {
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
                    mBitmapPaint);
            canvas.drawRect(mRoundRect.left,mRoundRect.top+mBorderRadius,mRoundRect.right,mRoundRect.bottom,mBitmapPaint);

        } else if(type==TYPE_CIRCLE_WITHBORDER)
        {
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
            canvas.drawCircle(mRadius, mRadius, mRadius,mBorderPaint);
        }else{
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        // 圆角图片的范围
        if (type == TYPE_ROUND||type==TYPE_ROUND_TOP_ARC)
            mRoundRect = new RectF(0, 0, getWidth(), getHeight());
    }
}