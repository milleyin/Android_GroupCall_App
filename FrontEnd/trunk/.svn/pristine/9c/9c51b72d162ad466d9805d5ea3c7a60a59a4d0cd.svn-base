package com.afmobi.palmchat.util.universalimageloader.core.display;

 
import android.graphics.*;
import android.graphics.drawable.Drawable;

import com.afmobi.palmchat.util.universalimageloader.core.assist.LoadedFrom;
import com.afmobi.palmchat.util.universalimageloader.core.imageaware.ImageAware;
import com.afmobi.palmchat.util.universalimageloader.core.imageaware.ImageViewAware;
 /**
  * 按窄边缩放的Displayer， 确保图片不会被变形，而是被裁切
  * @author xiaolong
  *
  */
public class SquareCutBitmapDisplayer implements BitmapDisplayer {

 
	protected final int margin;

	public SquareCutBitmapDisplayer( ) {
		this(  0);
	}

	public SquareCutBitmapDisplayer(  int marginPixels) { 
		this.margin = marginPixels;
	}

	@Override
	public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
		if (!(imageAware instanceof ImageViewAware)) {
			throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
		}

		imageAware.setImageDrawable(new SquareCutDrawable(bitmap,   margin));
	}

	public static class SquareCutDrawable extends Drawable {

		 
		protected final int margin;

		protected final RectF mRect = new RectF(),
				mBitmapRect;
		protected final BitmapShader bitmapShader;
		protected final Paint paint;
		protected final Bitmap oBitmap;
		public SquareCutDrawable(Bitmap bitmap,  int margin) {
			 
			this.margin = margin;
			oBitmap=bitmap;
			bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			mBitmapRect = new RectF (margin, margin, bitmap.getWidth() - margin, bitmap.getHeight() - margin);
			
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setShader(bitmapShader);
		}

		@Override
		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);
			/*mRect.set(margin, margin, bounds.width() - margin, bounds.height() - margin);
			
			// Resize the original bitmap to fit the new bound
			Matrix shaderMatrix = new Matrix();
			shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.FILL);
			bitmapShader.setLocalMatrix(shaderMatrix);*/
			computeBitmapShaderSize();
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
	        matrix.postScale(scale,scale);
	        matrix.postTranslate((bounds.width()-oBitmap.getWidth()*scale )/2
	        		, (bounds.height()-oBitmap.getHeight()*scale )/2);
	        bitmapShader.setLocalMatrix(matrix);
	    }
		@Override
		public void draw(Canvas canvas) {
			  Rect bounds = getBounds();
			canvas.drawRect( 0,0,bounds.width(),bounds.height(),  paint);
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
