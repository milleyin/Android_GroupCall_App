package com.afmobi.palmchat.util.universalimageloader.core.display;
 
import android.graphics.*;
import android.graphics.drawable.Drawable;

import com.afmobi.palmchat.util.universalimageloader.core.assist.LoadedFrom;
import com.afmobi.palmchat.util.universalimageloader.core.imageaware.ImageAware;
import com.afmobi.palmchat.util.universalimageloader.core.imageaware.ImageViewAware;
 /**
  * 把图片按宽度占满 高度按比例缩放
  * @author xiaolong
  *
  */
public class FitXDisplayer implements BitmapDisplayer {

 
	protected final int margin;
	private final int m_maxHeight;
	public FitXDisplayer(int _maxheight ) {
		m_maxHeight=_maxheight ;
		margin=0; 
	} 
	  
	@Override
	public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
		if (!(imageAware instanceof ImageViewAware)) {
			throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
		} 
		imageAware.setImageDrawable(new FitXDrawable(bitmap, m_maxHeight,  margin));
	}

	public static class FitXDrawable extends Drawable { 
		 
		protected final int margin;
		protected final int m_maxHeight;
//		protected final RectF mRect = new RectF();
//				mBitmapRect;
		protected final BitmapShader bitmapShader;
		protected final Paint paint;
		protected final Bitmap oBitmap;
		public FitXDrawable(Bitmap bitmap,  int _height,int margin) {
			m_maxHeight=_height;
			this.margin = margin;
			oBitmap=bitmap;
			bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//			mBitmapRect = new RectF (margin, margin, bitmap.getWidth() - margin, bitmap.getHeight() - margin);
			
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setShader(bitmapShader);
		}

		@Override
		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);
			 
			computeBitmapShaderSize();
		}
		  /**
	     * 计算Bitmap shader 大小
	     */
	    public void computeBitmapShaderSize(){
	        Rect bounds = getBounds();
	        if(bounds == null) return;
	        
	        Matrix matrix = new Matrix();
	        float scaleX = bounds.width() / (float)oBitmap.getWidth(); 
	        float scaleY=scaleX;
	        mHeight=oBitmap.getHeight() *scaleY;
	        if(m_maxHeight>0&&mHeight>m_maxHeight){
	        	scaleY=m_maxHeight / (float)oBitmap.getHeight(); 
	        }
	        matrix.postScale(scaleX,scaleY);
	        bitmapShader.setLocalMatrix(matrix);
	    }
	    float mHeight=0;
		@Override
		public void draw(Canvas canvas) {
			  Rect bounds = getBounds();
			canvas.drawRect( 0,0,bounds.width(),mHeight,  paint);
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
