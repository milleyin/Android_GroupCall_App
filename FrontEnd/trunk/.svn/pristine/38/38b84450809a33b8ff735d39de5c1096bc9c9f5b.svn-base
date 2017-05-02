package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 裁剪边框
 * 
 * @author gtf
 * @time 2014-9-18 下午3:53:00
 */
public class ClipView extends View {
	
	/**
	 * 边框距左右边界距离，用于调整边框长度
	 */
	public static final int BORDERDISTANCE =0;//40;
	
	public static final int CIRCULAR = 0; //圆形
	public static final int CIRCLE_ZOOM = 3;
	public int innerCircle = this.getWidth()/CIRCLE_ZOOM ; //内圆半径
	public static final int RECTANGULAR = 1;//矩形
	/**
	 * 水平方向与View的边距
	 */
	public static int mHorizontalPadding =  16;
	public static int TYPE_START = 0;
	
	private Paint mPaint;
	private Context mContext;
	
	public ClipView(Context context) {
		this(context, null);
		mContext = context;
	}

	public ClipView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mContext = context;
	}

	public ClipView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mPaint = new Paint();
		mContext = context;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = this.getWidth();
		int height = this.getHeight();
		if (TYPE_START == CIRCULAR) {
			innerCircle =  width / CIRCLE_ZOOM; // 内圆半径
			int ringWidth = height; // 圆环宽度
			mPaint.setColor(Color.TRANSPARENT);
			mPaint.setDither(true); //防抖动
			mPaint.setAntiAlias(true);//防锯齿
			mPaint.setStyle(Paint.Style.STROKE);
			
			// 绘制透明内圆
			canvas.drawCircle(width / 2, height / 2, width/2-mHorizontalPadding, mPaint);
			
			// 绘制全局圆环
			mPaint.setColor(0xaa000000);
			mPaint.setStrokeWidth(ringWidth);
			canvas.drawCircle(width / 2, height / 2,   width/2-mHorizontalPadding + ringWidth/2, mPaint);

			
		}else {
			
			// 边框长度，据屏幕左右边缘50px
			int borderlength = width - BORDERDISTANCE *2;
			
			mPaint.setColor(0xaa000000);
			
			// 以下绘制透明暗色区域
			// top
			canvas.drawRect(0, 0, width, (height - borderlength) / 2, mPaint);
			// bottom
			canvas.drawRect(0, (height + borderlength) / 2, width, height, mPaint);
			// left
			canvas.drawRect(0, (height - borderlength) / 2, BORDERDISTANCE,(height + borderlength) / 2, mPaint);
			// right
			canvas.drawRect(borderlength + BORDERDISTANCE, (height - borderlength) / 2, width, (height + borderlength) / 2, mPaint);
			
			// 以下绘制边框线
			mPaint.setColor(Color.WHITE);
			mPaint.setStrokeWidth(2.0f);
			// top
			canvas.drawLine(BORDERDISTANCE, (height - borderlength) / 2, width - BORDERDISTANCE, (height - borderlength) / 2, mPaint);
			// bottom
			canvas.drawLine(BORDERDISTANCE, (height + borderlength) / 2, width - BORDERDISTANCE, (height + borderlength) / 2, mPaint);
			// left
			canvas.drawLine(BORDERDISTANCE, (height - borderlength) / 2, BORDERDISTANCE, (height + borderlength) / 2, mPaint);
			// right
			canvas.drawLine(width - BORDERDISTANCE, (height - borderlength) / 2, width - BORDERDISTANCE, (height + borderlength) / 2, mPaint);
		}

	}
	
	/* 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	public void setStart(int start){
		TYPE_START = start;
	}

	
	public int getInnerCircle(){
		return innerCircle;
	}
}
