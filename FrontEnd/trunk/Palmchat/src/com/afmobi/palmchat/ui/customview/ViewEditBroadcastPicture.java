package com.afmobi.palmchat.ui.customview;

import java.util.ArrayList;
import java.util.List;

import com.afmobi.palmchat.eventbusmodel.ModifyPictureListEvent;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobi.utils.VisionUtils;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.social.EditBroadcastPictureActivity;
import com.afmobi.palmchat.util.ImageUtil;
import com.core.AfResponseComm.AfMFileInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import de.greenrobot.event.EventBus;

public class ViewEditBroadcastPicture extends View implements Runnable{
	class CellPic{
		String path;
		Bitmap img;
		Rect srcRect ;//图片真实尺寸
		Rect srcRect_clip ;//排布裁切后的尺寸
		Rect destRect ;//要画到的位置的
		Rect destRect_aniStart;//产生新布局后 显示移动过去的动画时的起始位置
		Rect destRect_aniMove;//产生新布局后 显示移动过去的动画时
		Rect destRect_draw_widthMagrin;//最后真正要画的  带间距的
		int rowIndex;//该图片在排列的第几行
		int colIndex;//第几列
		int zoomType;//缩放类型 
		/**
		 * 0为不缩放
		 * 1为四边都往里缩
		 * 2 <<往左缩
		 * 3 >>往右缩
		 * 4 ↑靠上缩
		 * 5 ↓靠下边缩 上边留空
		 */
		
		boolean isSetZoom;
	}
	 
	private ArrayList<CellPic>picArr=new  ArrayList<CellPic>(); 
	public static final  int[][]MAPARR_DEFAULT_PICTURE_RULE=new int[ ][]{
		{1},
		{2},//两张图  1行两列
		{2,1},
		{2,2},//4 张图两行2列
		{2,3},//5张图 第一行2列  第2行3列 
		{2,2,2 },//6张图 3行2列
		{2,3,2},//7张图 第一行2列 第2行三列  第3行 2列
		{3,2,3},
		{2,2,3,2},
};

public static final  int[][]MAPARR_DEFAULT_PICTURE_RULE_OLD=new int[ ][]{//老版本9宫格
	{1},
	{2},//两张图  1行两列
	{3},
	{2,2},//4 张图两行2列
	{3,2},//5张图 第一行2列  第2行3列 
	{3,3 },//6张图 3行2列
	{3,3,1},//7张图 第一行2列 第2行三列  第3行 2列
	{3,3,2},
	{3,3,3},
};


    private EditAndDeleteHelper mEditAndDeleteHelper;


    public ViewEditBroadcastPicture(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ViewEditBroadcastPicture(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        checkLongTouchDistance = dip2px(context, 2);
        marginPx = (int) (getResources().getDimension(R.dimen.broadcast_edit_pic_margin) / 2);
        scollSpeed = (int) getResources().getDimension(R.dimen.broadcast_edit_pic_scroll_speed);
        mEditAndDeleteHelper = new EditAndDeleteHelper();

    }

    private int scollSpeed;//编辑图片 选中图片后 长安拖动 时 拖动滚动ScrollView的速度

    /**
     * 设置一行中所有图片的坐标
     *
     * @param start_i
     * @param end_i
     * @param y
     * @param colCount
     * @return 返回排了这列后 下一列的起始Y卓标
     */
    private int setOneRowPos(int start_i, int end_i, int y, int colCount, int rowIndex, CellPic selecPic) {
        int cw = ImageUtil.DISPLAYW / colCount;

        int minH = 0;//最小高
        for (int i = start_i; i <= end_i; i++) {
            int _h = picArr.get(i).srcRect.bottom * cw / picArr.get(i).srcRect.right;
            if (minH == 0 || minH > _h) {
                minH = _h;
            }
        }
        if (minH > cw * 4 / 3) {//超长图 限制高度
            minH = cw * 4 / 3;
        }
        if (minH < cw / 3) {//超宽图 加高高度
            minH = cw / 3;
        }
        CellPic cellPic = null;
        for (int i = start_i, j = 0; i <= end_i; i++, j++) {
            cellPic = picArr.get(i);
            if (cellPic.equals(selecPic)) {
                cellPic.destRect_aniMove.left = cellPic.destRect_aniStart.left = rectSelect.left;
                cellPic.destRect_aniMove.top = cellPic.destRect_aniStart.top = rectSelect.top;
                cellPic.destRect_aniMove.right = cellPic.destRect_aniStart.right = rectSelect.right;
                cellPic.destRect_aniMove.bottom = cellPic.destRect_aniStart.bottom = rectSelect.bottom;
            } else {
                cellPic.destRect_aniMove.left = cellPic.destRect_aniStart.left = cellPic.destRect.left;
                cellPic.destRect_aniMove.top = cellPic.destRect_aniStart.top = cellPic.destRect.top;
                cellPic.destRect_aniMove.right = cellPic.destRect_aniStart.right = cellPic.destRect.right;
                cellPic.destRect_aniMove.bottom = cellPic.destRect_aniStart.bottom = cellPic.destRect.bottom;
            }
            cellPic.destRect.left = j * cw;
            cellPic.destRect.top = y;
            cellPic.destRect.right = j * cw + cw;
            cellPic.destRect.bottom = y + minH;
            cellPic.rowIndex = rowIndex;
            cellPic.colIndex = j;
            cellPic.zoomType = 0;
//			if(cellPic.srcRect.right<cellPic.srcRect.bottom){//竖直的
				int _clipH=(cellPic.srcRect.right )*minH/cw;
				picArr.get(i).srcRect_clip.left=cellPic.srcRect.left;
				picArr.get(i).srcRect_clip.right=cellPic.srcRect.right;
				picArr.get(i).srcRect_clip.top= cellPic.srcRect.bottom-_clipH>>1;
				picArr.get(i).srcRect_clip.bottom= picArr.get(i).srcRect_clip.top+_clipH ;
				if(picArr.get(i).srcRect_clip.top<0){
					picArr.get(i).srcRect_clip.top=0;
				}
				if(picArr.get(i).srcRect_clip.top+picArr.get(i).srcRect_clip.bottom>cellPic.srcRect.height()){
					picArr.get(i).srcRect_clip.bottom=cellPic.srcRect.height()-picArr.get(i).srcRect_clip.top;
				}
//			}else  {//横的
//				int _clipW=(cellPic.srcRect.bottom )*minH/cw;
//				picArr.get(i).srcRect_clip.left=cellPic.srcRect.right-_clipW>>1;
//				picArr.get(i).srcRect_clip.right=picArr.get(i).srcRect_clip.left+_clipW;
//				picArr.get(i).srcRect_clip.top= cellPic.srcRect.top;
//				picArr.get(i).srcRect_clip.bottom= picArr.get(i).srcRect_clip.bottom ;
//			}
		} 
		return y+minH;
	}
	private boolean isShowAnimationing;
	private long timeShowAnimationStart;
	private CellPic selectAnimationCellPic;
	/**
	 * 设置图片排列
	 * @param isShowAnimation 是否显示移动效果
	 */
	private void reLayout(boolean isShowAnimation,CellPic selectPic){
		if(isShowAnimation){
			isShowAnimationing=isShowAnimation;
			timeShowAnimationStart=System.currentTimeMillis();
		}
		 for(int m=0;m<rowArr.size();m++){//去掉不包含内容的行
			 if(rowArr.get(m)==0){
				 rowArr.remove(m);
			 }
		 }
		 selectAnimationCellPic=selectPic;
		int startI=0;
		int y=0;
		int count=0;
		for(int i=0,j=0;i<picArr.size();i++){
			if(count>=rowArr.get( j)-1){
				y=setOneRowPos(startI,i,y,rowArr.get( j),j,selectPic);//设置单行图片排列
				startI=i+1;
				count=0;
				j++;
			}else{
				count++;
			}
		}
		
//		y+=40; 
		mHeight=y;
		RelativeLayout.LayoutParams layout=	new RelativeLayout.LayoutParams(ImageUtil.DISPLAYW , mHeight); 
	    setLayoutParams(layout);  
	}
	private int mHeight;
	private ArrayList< Integer>rowArr=new ArrayList< Integer>();
	private EditBroadcastPictureActivity mEditBroadcastPictureActivity;
	private boolean isExist(String imgPath){
		for(int i=0;i<picArr.size();i++){
			if(imgPath!=null&&imgPath.equals(picArr.get(i).path)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 发广播时获取图片排布规则
	 * @return
	 */
	public String getPictureRule(){
		String r="{";
		for(int i=0;i<rowArr.size();i++){
			r+=rowArr.get(i);
			if(i<rowArr.size()-1){
				r+=",";
			}
		}
		r+="}";
		return   r ;
	}
	/**
	 * 发广播前设置每张图片的尺寸和裁切区域
	 */
	public void getPictureSizeCut(){
		for(int i=0;i<picturePathList.size();i++){
			 
			picturePathList.get(i).resize=new int[]{picArr.get(i).destRect.width(),picArr.get(i).destRect.height()};
			
			/*int _clipH=(picArr.get(i).srcRect.right )*picArr.get(i).destRect.height()/picArr.get(i).destRect.width();
			int cut_x=picArr.get(i).srcRect.left*picArr.get(i).destRect.width()/picArr.get(i).srcRect.right;
			int cut_w= picArr.get(i).srcRect_clip.right*picArr.get(i).destRect.width()/picArr.get(i).srcRect.right;
			int cut_y=picArr.get(i).srcRect_clip.top*picArr.get(i).destRect.width()/picArr.get(i).srcRect.right;
			int cut_h= picArr.get(i).srcRect_clip.bottom*picArr.get(i).destRect.width()/picArr.get(i).srcRect.right;*/
			
			picturePathList.get(i).cut=new int[]{picArr.get(i).srcRect_clip.left,
									  picArr.get(i).srcRect_clip.top,
									   picArr.get(i).srcRect_clip.width(),
									   picArr.get(i).srcRect_clip.height()};
			if(picturePathList.get(i).cut[0]<0){
				picturePathList.get(i).cut[0]=0;
			}
			if(picturePathList.get(i).cut[1]<0){
				picturePathList.get(i).cut[1]=0;
			}
			if(picturePathList.get(i).cut[0]+picturePathList.get(i).cut[2]> picArr.get(i).srcRect.width()){
				 picturePathList.get(i).cut[2]= picArr.get(i).srcRect.width()-picturePathList.get(i).cut[0];
			}
			if(picturePathList.get(i).cut[1]+picturePathList.get(i).cut[3]> picArr.get(i).srcRect.height()){
				 picturePathList.get(i).cut[3]= picArr.get(i).srcRect.height()-picturePathList.get(i).cut[1];
			}
		}
	}
	/**
	 * 替换一张图片
	 * @param index
	 * @param imgpaths
	 */
	public void changePath(int index,ArrayList<String> imgpaths){
		if(imgpaths.size()>0){
			
			if(index<picArr.size()){
				Bitmap img=	picArr.get(index).img;
				if(	img!=null&&!img.isRecycled()	){
					img.recycle();
				}
				img=null;
				
				
				CellPic _c=new CellPic();
				_c.path=imgpaths.get(0);
				picArr.set( index,_c);  
				img=_c.img= ImageManager.getInstance().loadLocalImageSync(_c.path,true);//ImageUtil.getBitmapFromFile(_c.path,true);
				_c.srcRect=new Rect(0,0,img.getWidth(),img.getHeight()) ;
				 _c.destRect=new Rect(0,0,img.getWidth(),img.getHeight()) ; 
				_c.destRect_aniStart=new Rect(0,0,img.getWidth(),img.getHeight()) ; 
				_c.srcRect_clip=new Rect(0,0,img.getWidth(),img.getHeight()) ;   
				_c.destRect_aniMove=new Rect(0,0,img.getWidth(),img.getHeight()) ;   
				_c.destRect_draw_widthMagrin=new Rect(0,0,img.getWidth(),img.getHeight()) ;
				reLayout(false,null);
			}
		}
	}
	private ArrayList<AfMFileInfo> picturePathList;
	/**
	 *  

	 * @param activity
	 */
	public void setImagePathArr(ArrayList<AfMFileInfo> pList ,EditBroadcastPictureActivity activity){
		 
		mEditBroadcastPictureActivity=activity;
		picturePathList=pList;
		
		Bitmap img=null;
		 
		for(int i=0;i<pList.size();i++){
			if(!isExist(pList.get(i).local_img_path )){//判断是否已经存在了的
				CellPic _c=new CellPic();
				_c.path=pList.get(i).local_img_path;
				picArr.add(_c);
			}
		}  
		 
		for(int i=0;i<picArr.size();i++){
			if(picArr.get(i).img==null){//排除之前已经加过的
				img=ImageManager.getInstance().loadLocalImageSync(picArr.get(i).path,false);//不缓存 因为缓存了 在使用滤镜方面会有问题//ImageUtil.getBitmapFromFile(picArr.get(i).path,true);
				if(img!=null){
					picArr.get(i).img=img;
					picArr.get(i).srcRect=new Rect(0,0,img.getWidth(),img.getHeight()) ;
					picArr.get(i).destRect=new Rect(0,0,img.getWidth(),img.getHeight()) ; 
					picArr.get(i).destRect_aniStart=new Rect(0,0,img.getWidth(),img.getHeight()) ; 
					picArr.get(i).destRect_aniMove=new Rect(0,0,img.getWidth(),img.getHeight()) ;  
				 	picArr.get(i).srcRect_clip=new Rect(0,0,img.getWidth(),img.getHeight()) ; 
				 	picArr.get(i).destRect_draw_widthMagrin=new Rect(0,0,img.getWidth(),img.getHeight()) ;
				}
			}
		}
		rowArr.clear();//重新设置行
		for(int i=0;i<MAPARR_DEFAULT_PICTURE_RULE[picArr.size()-1].length;i++){
			rowArr.add(MAPARR_DEFAULT_PICTURE_RULE[picArr.size()-1][i]);
		}
		  
		reLayout(false,null);
		
		 if(mThread==null){
			 mThread=new Thread(this); 
			 mThread.start();
		 } 
		 isRunning=true;
	} 
	
	/**
	 * 从发广播界面 返回到编辑图片界面时的图片布局恢复
	 * @param pList
	 * @param picRule
	 * @param activity
	 */
	public void recoveryPicLayout(ArrayList<AfMFileInfo> pList ,String picRule,EditBroadcastPictureActivity activity){
		mEditBroadcastPictureActivity=activity;
		picturePathList=pList;
		 
		Bitmap img=null;
		 
		for(int i=0;i<picturePathList.size();i++){
			if(!isExist(picturePathList.get(i).local_img_path)){//判断是否已经存在了的
				CellPic _c=new CellPic();
				_c.path=picturePathList.get(i).local_img_path;
				picArr.add(_c);
			}
		}  
		 
		for(int i=0;i<picArr.size();i++){
			if(picArr.get(i).img==null) {//排除之前已经加过的
				img = ImageManager.getInstance().loadLocalImageSync(picArr.get(i).path, true);//ImageUtil.getBitmapFromFile(picArr.get(i).path,true);
				if (img != null) {
					picArr.get(i).img = img;
					picArr.get(i).srcRect = new Rect(0, 0, img.getWidth(), img.getHeight());
					picArr.get(i).destRect = new Rect(0, 0, img.getWidth(), img.getHeight());
					picArr.get(i).destRect_aniStart = new Rect(0, 0, img.getWidth(), img.getHeight());
					picArr.get(i).destRect_aniMove = new Rect(0, 0, img.getWidth(), img.getHeight());
					picArr.get(i).srcRect_clip = new Rect(0, 0, img.getWidth(), img.getHeight());
					picArr.get(i).destRect_draw_widthMagrin = new Rect(0, 0, img.getWidth(), img.getHeight());
				}
			}
		}
		rowArr.clear();//重新设置行
		if(picRule!=null&&picRule.length()>2){
			picRule=picRule.substring(1,picRule.length()-1);
			if(picRule.length()==1){
				rowArr.add(Integer.parseInt(picRule));
			}else{
				String[]ruleArr=picRule.split(",");
				for(int i=0;i<ruleArr.length;i++){
					rowArr.add(Integer.parseInt(ruleArr[i]));
				}
			} 
			
		}else{
			for(int i=0;i<MAPARR_DEFAULT_PICTURE_RULE[picArr.size()-1].length;i++){
				rowArr.add(MAPARR_DEFAULT_PICTURE_RULE[picArr.size()-1][i]);
			}
		}
		  
		reLayout(false,null);
		
		 if(mThread==null){
			 mThread=new Thread(this); 
			 mThread.start();
		 } 
		 isRunning=true;
	} 
	/**
	 * 删除一张图片
	 * @param index
	 */
	public void removePic(int index){
		if(index<picArr.size()){
			
			CellPic _c=picArr.remove( index);
			rowArr.set(_c.rowIndex, rowArr.get(_c.rowIndex)-1); 
			Bitmap img= _c.img;
			 if(img!=null&&!img.isRecycled()){
				 img.recycle();
			 }
			 img=null;
			 reLayout(false,null);
		} 
	}
	 

	private Thread mThread;
	private Paint mPaint=new Paint();
	//ImageUtil.DISPLAYH
	private int ANIMATION_TIME=300;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	  
			if (isShowAnimationing) {// 显示移动过去的动画效果
				int _selectIndex = 0;
				for (int i = 0; i < picArr.size(); i++) {
					if (selectAnimationCellPic == null || !picArr.get(i).equals(selectAnimationCellPic)) {
						drawZoomPic(canvas, i, picArr.get(i).srcRect_clip, picArr.get(i).destRect_aniMove, null, picArr.get(i).zoomType, true, picArr.get(i));
					} else {
						_selectIndex = i;
					}
				}
				if (selectAnimationCellPic != null&&_selectIndex<picArr.size()) {
					int alpha = 0x88 + (255 - 0x88) * ((int) (System.currentTimeMillis() - timeShowAnimationStart)) / ANIMATION_TIME;
					if (alpha > 255) {
						alpha = 255;
					}
					mPaint.setAlpha(alpha);
					drawZoomPic(canvas, _selectIndex, picArr.get(_selectIndex).srcRect_clip, picArr.get(_selectIndex).destRect_aniMove, mPaint, picArr.get(_selectIndex).zoomType, true, picArr.get(_selectIndex));
				}
				if (System.currentTimeMillis() - timeShowAnimationStart > ANIMATION_TIME) {
					isShowAnimationing = false;
				}
			} else {
				for (int i = 0; i < picArr.size(); i++) {
					if (!isLongTouchDown || selectIndex != i) {
						drawZoomPic(canvas, i, picArr.get(i).srcRect_clip, picArr.get(i).destRect, null, picArr.get(i).zoomType, false, null);
					}
				}
				if (isLongTouchDown && selectIndex < picArr.size()) {
					mPaint.setAlpha(0x88);
					drawZoomPic(canvas, selectIndex, picArr.get(selectIndex).srcRect_clip, rectSelect, mPaint, picArr.get(selectIndex).zoomType, false, null);
				}
			}
		 
	}
	private int marginPx,//图片之间的间距
					marginPx_left,marginPx_right,marginPx_top;//左间距 右间距
	/**
	 * 0为不缩放
	 * 1为四边都往里缩
	 * 2 <<往左缩
	 * 3 >>往右缩
	 * 4 ↑靠上缩
	 * 5 ↓靠下边缩 上边留空
	 */
	private void drawZoomPic(Canvas canvas,int index,Rect srcRect,Rect destRect,Paint paint,int zoomType,boolean isShowAnimation,CellPic cellpic){
	 	int margin_X=(int)((marginPx*14* (System.currentTimeMillis()-time_move_status_changed))/500);//当被选中时 图片X轴的间距
	 	if(margin_X>marginPx*14){
	 		margin_X=marginPx*14;
	 	}
	 	int margin_Y=margin_X;//margin_X*srcRect.height()/srcRect.width();//当被选中时 Y间距
	 	marginPx_top=marginPx_left=marginPx_right=marginPx;
		int colNum=rowArr.get( picArr.get(index).rowIndex);
		if( picArr.get(index).colIndex==0){
			marginPx_left=0;
		}
		if( picArr.get(index).colIndex==colNum-1){
			marginPx_right=0;
		}
		if( picArr.get(index).rowIndex==0){
			marginPx_top=0;
		}
		if(zoomType==0){//不缩放
			picArr.get(index).destRect_draw_widthMagrin.set(destRect.left+marginPx_left,destRect.top+marginPx_top,destRect.right-marginPx_right ,destRect.bottom-marginPx );
			canvas.drawBitmap(picArr.get(index).img, srcRect,picArr.get(index).destRect_draw_widthMagrin , paint);
		}else if(zoomType==1){//选中靠紧 
			picArr.get(index).destRect_draw_widthMagrin.set(destRect.left+margin_X,destRect.top+margin_Y,destRect.right-margin_X ,destRect.bottom-margin_Y );
			canvas.drawBitmap(picArr.get(index).img, srcRect,picArr.get(index).destRect_draw_widthMagrin , paint); 
		}else if(zoomType==2){//  <<往左缩
			picArr.get(index).destRect_draw_widthMagrin.set(destRect.left+marginPx_left-margin_X ,destRect.top+marginPx_top,destRect.right-margin_X ,destRect.bottom-marginPx );
			canvas.drawBitmap(picArr.get(index).img, srcRect,picArr.get(index).destRect_draw_widthMagrin , paint); 
		}else if(zoomType==3){// >>往右缩 
			picArr.get(index).destRect_draw_widthMagrin.set(destRect.left+margin_X ,destRect.top+marginPx_top,destRect.right-marginPx_right+margin_X  ,destRect.bottom-marginPx );
			canvas.drawBitmap(picArr.get(index).img, srcRect,picArr.get(index).destRect_draw_widthMagrin , paint); 
		}else if(zoomType==4){// ↑靠上缩
			picArr.get(index).destRect_draw_widthMagrin.set(destRect.left+marginPx_left,destRect.top+marginPx_top-margin_Y,destRect.right-marginPx_right ,destRect.bottom-margin_Y );
			canvas.drawBitmap(picArr.get(index).img, srcRect,picArr.get(index).destRect_draw_widthMagrin , paint); 
		}else if(zoomType==5){// ↓靠下边缩 上边留空
			picArr.get(index).destRect_draw_widthMagrin.set(destRect.left+marginPx_left,destRect.top+margin_Y,destRect.right-marginPx_right ,destRect.bottom-marginPx+margin_Y );
			canvas.drawBitmap(picArr.get(index).img, srcRect,picArr.get(index).destRect_draw_widthMagrin , paint); 
		}
		
		if(isShowAnimation&&cellpic!=null){
			cellpic.destRect_aniMove.left=cellpic.destRect_aniStart.left+
					(cellpic.destRect.left-cellpic.destRect_aniStart.left)*((int)(System.currentTimeMillis()-timeShowAnimationStart))/ANIMATION_TIME;
			
			cellpic.destRect_aniMove.right=cellpic.destRect_aniStart.right+
					(cellpic.destRect.right-cellpic.destRect_aniStart.right)*((int)(System.currentTimeMillis()-timeShowAnimationStart))/ANIMATION_TIME;
			 
			cellpic.destRect_aniMove.top=cellpic.destRect_aniStart.top+
					(cellpic.destRect.top-cellpic.destRect_aniStart.top)*((int)(System.currentTimeMillis()-timeShowAnimationStart))/ANIMATION_TIME;
			
			cellpic.destRect_aniMove.bottom=cellpic.destRect_aniStart.bottom+
					(cellpic.destRect.bottom-cellpic.destRect_aniStart.bottom)*((int)(System.currentTimeMillis()-timeShowAnimationStart))/ANIMATION_TIME;
		}
        if (!isShowAnimation || (index != selectIndex)) {
            mEditAndDeleteHelper.onDraw(canvas, index);

        }


    }
	public void release(boolean isStopThread){
		if(isStopThread){
			isRunning=false;
		}
//		 Bitmap img=null;
		for(int i=0;i<picArr.size();i++){
//			 img=picArr.get(i).img;
			 /*if(img!=null&&!img.isRecycled()){
				 img.recycle();之所以不回收了 是因为交给UIL自己去管理回收与否 我们自己不做回收处理 否则可能
			 }*/
//			 img=null;
			picArr.get(i).img=null;
		} 
		picArr.clear();
		ImageManager.getInstance().clearCache();
	}
	private boolean isInRect(float x,float y,float dx,float dy,float dex,float dey){
		if(dx<x&&x<dex&&dy<y&&y<dey){
			return true;
		}
		return false;
	}
	private int selectIndex;
	private boolean isLongTouchDown;//长按后 执行拖动图片操作
	private void movePic(int index,float  x, float  y){
		rectSelect.left =(int) (x-offsetTouchedX);
		rectSelect.right =rectSelect.left+selectRW;
		rectSelect.top =(int) (y-offsetTouchedY);
		rectSelect.bottom =rectSelect.top+selectRH;
	}
	private boolean isDownOfThePic(float offx,float offy,float w,float h){
		if(offx<(h/5) || offx >(w*4/5) ){
			return false;//???有bug
		}
		return offy >h*4/5;
	}
	private boolean isUpOfThePic(float offx,float offy,float w,float h){
 
		if(offx<(h/5) || offx >(w*4/5) ){
			return false;//???有bug
		}
		return offy <h/5;
	}
	private int move_status;//当前拖动到的状态
	private long time_move_status_changed;//状态变化起始时间
	private int preMoveIndex;//上一个拖动到的位置
	/**
	 * 0为不缩放
	 * 1为四边都往里缩
	 * 2 <<往左缩
	 * 3 >>往右缩
	 * 4 ↑靠上缩
	 * 5 ↓靠下边缩 上边留空
	 */
	private boolean setZoomType(int index,float x,float y,boolean isColIsMax,boolean isSameRow){
		float offy=y-picArr.get(index).destRect.top;
		float  h= picArr.get(index).destRect.height();
		float offx=x-picArr.get(index).destRect.left;
		float  w= picArr.get(index).destRect.width();
		boolean isChanged=false;
		int status=0;
		if( isDownOfThePic(offx,offy,w,h)){ 
			int  rowIndex= picArr.get(index).rowIndex;
			for(int i=0;i<picArr.size();i++){//目标图片以上的 都要挪 往上
				if((rowIndex== picArr.get(i).rowIndex||i<index)&&selectIndex!=i){
					picArr.get(i).zoomType=4;
					picArr.get(i).isSetZoom=true;
				}
			} 
			status=1;
			isChanged= true;
		}else if(isUpOfThePic(offx,offy,w,h)){
			int  rowIndex= picArr.get(index).rowIndex;
			for(int i=0;i<picArr.size();i++){//都要挪 往下
				if( rowIndex== picArr.get(i).rowIndex||i>index){
					picArr.get(i).zoomType=5;
					picArr.get(i).isSetZoom=true;
				}
			} 
			status=2;
			isChanged= true;
		}else if(!isColIsMax||isSameRow){//一行最多3列， 如果列数没满 或则 列数已满 但是 插入的和被插入的位置是统一行的  那也可以插入
			
			if( offx >w/2){//<<
				picArr.get(index).zoomType=2;
				picArr.get(index).isSetZoom=true;
				int colNum=rowArr.get( picArr.get(index).rowIndex);//这一行有几列？
				if(picArr.get(index).colIndex+1<colNum){// 如果右边还有一列 那也要缩放 往>>移
					picArr.get(index+1).zoomType=3;
					picArr.get(index+1).isSetZoom=true;
				}
				int _colIndex=picArr.get(index).colIndex;
				for(int i=0;i<_colIndex;i++){//当前的左边都要<<移动
					if(index-_colIndex+i!=selectIndex){
						picArr.get(index-_colIndex+i).zoomType=2;
						picArr.get(index-_colIndex+i).isSetZoom=true;
					}
				}
				status=3;
			}else{//>>
				picArr.get(index).zoomType=3;
				picArr.get(index).isSetZoom=true;
				int colNum=rowArr.get( picArr.get(index).rowIndex);//这一行有几列？
				if(picArr.get(index).colIndex>0){// 如果左边还有一列 那也要<<缩放
					picArr.get(index-1).zoomType=2;
					picArr.get(index-1).isSetZoom=true;
				}
				
				int _colIndex=picArr.get(index).colIndex;
				for(int i=0;i<colNum-_colIndex;i++){//当前的左边都要<<移动
					if(index +i!=selectIndex){
						picArr.get(index +i).zoomType=3;
						picArr.get(index +i).isSetZoom=true;
					}
				}
				 status=4;
			}
		
			isChanged= true;
		}
		if(isChanged&&status!=move_status||preMoveIndex!=index){//当前的拖动引起的图片间隔变化产生了
			move_status=status;
			preMoveIndex =index;
			time_move_status_changed=System.currentTimeMillis();
		}
		return isChanged;
	}
	private float checkLongTouchDistance;
	private float dip2px(Context context, float dipValue){ 
         float scale = context.getResources().getDisplayMetrics().density;  
         if(scale>=4.0){//屏幕像素密度超高的手机 必须把这个间距放大 以使 长按操作不被误判
        	 scale*=4;
         }else if(scale>=3.0){//屏幕像素密度超高的手机 必须把这个间距放大 以使 长按操作不被误判
        	 scale*=2;
         }
         return   dipValue * scale + 0.5f ; 
	} 
	private int selectRW,selectRH;
	private Rect rectSelect=new Rect();
	private float preX,preY;
	private float offsetTouchedX,offsetTouchedY;
	private float toutchDownX,toutchDownY;
	private long timeTouchDown;
	private boolean isCheckLongToutch;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub  
		float x=event.getX();
		float y=event.getY(); 
		if(y<=0){
			y=1;//如果滑动到View界面之外  那就是负数 这个时候判断为1  修正拖动到最上面会判断不准的问题
		}
		switch (event.getAction()) { 
		case MotionEvent.ACTION_DOWN:
			toutchDownX=x;
			toutchDownY=y;
			timeTouchDown=System.currentTimeMillis();
			isCheckLongToutch=true;
			 
                mEditAndDeleteHelper.onToutchEvent(event);
			break;
		case MotionEvent.ACTION_MOVE:
			if(isLongTouchDown){
				if(mEditBroadcastPictureActivity!=null){
					 
					mEditBroadcastPictureActivity.setScrollViewEnable(false,(int)(y-preY),scollSpeed,(int)y);
					mEditBroadcastPictureActivity.setTitleVisable(false);
					setTouchedHeight(true);
				}
				movePic(selectIndex,x ,y );
				preX=x;
				preY=y;
				for(int i=0;i<picArr.size();i++){
					picArr.get(i).isSetZoom=false;
				}
				
				for(int i=0;i<picArr.size();i++){
					if(selectIndex!=i){//不能是自己
						if(isInRect(x,y,
								picArr.get(i).destRect.left,picArr.get(i).destRect.top,picArr.get(i).destRect.right
								,picArr.get(i).destRect.bottom)){ 
							
//								if (rowArr.get(picArr.get(i).rowIndex) < 3||// 一行不能大于3ge
//										picArr.get(selectIndex).rowIndex==picArr.get(i).rowIndex) {//如果一行大于3个 那必须是同一行
									if(!setZoomType(i,x,y,rowArr.get(   picArr.get(i).rowIndex)>=3,picArr.get(selectIndex).rowIndex==picArr.get(i).rowIndex)){
										if(!picArr.get(i).isSetZoom){
											picArr.get(i).zoomType=0;
										}
									}
//								}
								
							 
						}else{
							if(!picArr.get(i).isSetZoom){
								picArr.get(i).zoomType=0;
							}
						}
					}
				}
			}else {
				float dx=Math.abs( toutchDownX-x) ;
				float dy=Math.abs( toutchDownY-y) ;
				boolean isDx=dx>checkLongTouchDistance;
				boolean isDy=dy>checkLongTouchDistance;
				PalmchatLogUtils.e("WXL","dy="+dy   +"   isDY="+isDy + "   checkLongTouchDistance="+checkLongTouchDistance );
				if( isDx|| 
						isDy  ){//如果滑动出了一定的距离 那就不能算是在长按中了
					isCheckLongToutch=false;
					timeTouchDown=System.currentTimeMillis(); 
				} 
			}
			break;
		case MotionEvent.ACTION_UP:
			isCheckLongToutch=false;

			long timeDownToUp=System.currentTimeMillis()-timeTouchDown;
			timeTouchDown=System.currentTimeMillis();
			if(isLongTouchDown){
				isLongTouchDown=false;
				mEditBroadcastPictureActivity.setTitleVisable(true);
				setTouchedHeight(false);
				if(mEditBroadcastPictureActivity!=null){
					mEditBroadcastPictureActivity.setScrollViewEnable(true,0,0,0);
				}
				//			arrZoomType.set(  selectIndex,0) ;
				picArr.get(selectIndex).zoomType=0;

				for(int i=0;i<picArr.size();i++){
					if(selectIndex!=i&& isInRect(x,y,
							picArr.get(i).destRect.left,picArr.get(i).destRect.top,picArr.get(i).destRect.right
							,picArr.get(i).destRect.bottom)){
						float offy=y-picArr.get(i).destRect.top;
						float offx=x-picArr.get(i).destRect.left;
						float  w= picArr.get(i).destRect.right-picArr.get(i).destRect.left;
						float  h= picArr.get(i).destRect.bottom-picArr.get(i).destRect.top;
						if(isDownOfThePic(offx,offy,w,h)){//往下插入一行

							int selectRowIndex=picArr.get(selectIndex).rowIndex;
							int aimRowIndex=picArr.get(i).rowIndex;
							int newRorCount=rowArr.get(selectRowIndex)-1;
							rowArr.set(selectRowIndex,newRorCount);//先从原来的位置上去掉
							CellPic _sp=picArr.get(selectIndex);

							picArr.remove(selectIndex);
							AfMFileInfo _afmi=picturePathList.remove(selectIndex);
							//计算下一行的图片索引
							int newPicArrIndex=picArr.size()-1;
							if(selectRowIndex==aimRowIndex+1&&newRorCount==0){//如果是同一行的 而且 这行只有一张图
								newPicArrIndex=selectIndex;
							}else{
								for(int k=0;k<picArr.size();k++){
									if(picArr.get(k).rowIndex==aimRowIndex+1){
										newPicArrIndex=k;
										break;
									}
								}
							}
							if(newPicArrIndex<0){
								newPicArrIndex=0;
							}
							//插入对应位置
							if(newPicArrIndex<picArr.size() ){
								picArr.add( newPicArrIndex, _sp);
								picturePathList.add(newPicArrIndex,_afmi);
							}else{
								picArr.add(  _sp);//?
								picturePathList.add( _afmi);
							}
										 /*if(selectRowIndex+1<rowArr.size()-1){
											 rowArr.add(selectRowIndex+1,1   );
										 }else{
											 rowArr.add(1);
										 }		*/
							if(aimRowIndex+1>=rowArr.size()){
								rowArr.add( 1   );
							}else{
								rowArr.add(aimRowIndex+1,1   );
							}

							reLayout(true,_sp);//重新布局

						}else if(isUpOfThePic(offx,offy,w,h)){//往上插入一行
							int selectRowIndex=picArr.get(selectIndex).rowIndex;
							int aimRowIndex=picArr.get(i).rowIndex;
							CellPic _sp=picArr.get(selectIndex);
							rowArr.set(selectRowIndex,rowArr.get(selectRowIndex)-1);//先从原来的位置上去掉
							picArr.remove(selectIndex); //去掉之前的
							AfMFileInfo _afmi=picturePathList.remove(selectIndex);
							//计算上一行的图片索引
							int newPicArrIndex=0;
							for(int k=0;k<picArr.size();k++){
								if(picArr.get(k).rowIndex==aimRowIndex ){
									newPicArrIndex=k;
									break;
								}
							}
							//插入对应位置
							if(newPicArrIndex-1>=0){
//										if(selectIndex<i){
								picArr.add( newPicArrIndex , _sp);
								picturePathList.add(newPicArrIndex,_afmi);
//										}else{
//											picArr.add( newPicArrIndex , _sp);
//										}

							}else{
								picArr.add( 0, _sp);//?
								picturePathList.add(0,_afmi);
							}
							rowArr.add(aimRowIndex,1   );


							reLayout(true,_sp);//重新布局
						}else if(selectIndex!=i){
							int selectRowIndex = picArr.get(selectIndex).rowIndex;
							int destRowIndex = picArr.get(i).rowIndex;
							if (rowArr.get(picArr.get(i).rowIndex) < 3||// 一行不能大于3ge
									selectRowIndex==destRowIndex) {//如果一行大于3个 那必须是同一行

//								float offx=x-picArr.get(i).destRect.left;
//								float  w= picArr.get(i).destRect.right-picArr.get(i).destRect.left;

								rowArr.set(destRowIndex, rowArr.get(destRowIndex) + 1);
								rowArr.set(selectRowIndex, rowArr.get(selectRowIndex) - 1);

								CellPic _sp = picArr.get(selectIndex);
								picArr.remove(selectIndex);
								AfMFileInfo _afmi=picturePathList.remove(selectIndex);
								if(offx>w/2){//插右边
									if (i   >= picArr.size()) {
										picArr.add(_sp);
										picturePathList.add( _afmi);
									} else {
										if(selectIndex<i){
											picArr.add(i  , _sp);
											picturePathList.add(i,_afmi);
										}else{
											picArr.add(i + 1, _sp);
											picturePathList.add(i+1,_afmi);
										}
									}
								}else{//插左边
									if(selectIndex<i){
										if(i-1<0){
											picArr.add(0,_sp );
											picturePathList.add(0,_afmi);
										}else{
											picArr.add(i-1,_sp );
											picturePathList.add(i-1,_afmi);
										}
									} else {
										picArr.add(i , _sp);
										picturePathList.add(i,_afmi);
									}
								}

								reLayout(true,_sp);
							}

						}
						return true;
//						else {
//							return true;//选中的图片是否就是放下图片的位置
//						}
					}
				}

				if( !isInRect(x,y,
						picArr.get(selectIndex).destRect.left,picArr.get(selectIndex).destRect.top,picArr.get(selectIndex).destRect.right
						,picArr.get(selectIndex).destRect.bottom)){
					//---------------增加一行
					int selectRowIndex=picArr.get(selectIndex).rowIndex;
					rowArr.set(selectRowIndex,rowArr.get(selectRowIndex)-1);


					CellPic _sp=picArr.get(selectIndex);
					picArr.remove(selectIndex);
					AfMFileInfo _afmi=picturePathList.remove(selectIndex);
					picArr.add(  _sp);
					picturePathList.add(_afmi);

					rowArr.add(1);

					reLayout(true,_sp);
				}

			}else if (mEditAndDeleteHelper.onToutchEvent(event) && timeDownToUp < 150){
                    mEditAndDeleteHelper.doProcessEvent();
			}
          /*  不需要进了else if (timeDownToUp < 150) {//单击事件 进入大图模式

				float dx=Math.abs( toutchDownX-x) ;
				float dy=Math.abs( toutchDownY-y) ;
				boolean isDx=dx<checkLongTouchDistance;
				boolean isDy=dy<checkLongTouchDistance;
				if (isDx && isDy) {

					if (mEditBroadcastPictureActivity != null && !mEditBroadcastPictureActivity.setTitleVisable(true)) {
						for (int i = 0; i < picArr.size(); i++) {
							if (isInRect(x, y, picArr.get(i).destRect.left, picArr.get(i).destRect.top, picArr.get(i).destRect.right, picArr.get(i).destRect.bottom)) {
								mEditBroadcastPictureActivity.preViewImage(i);
							}
						}
					}
				}

			}*/
			break;
		}
		
		return true;// super.onTouchEvent(event);
	}

	/**
	 * 用于灭屏时候的恢复到非长按编辑状态
	 */
	public void setOnPause(){
		if(isLongTouchDown) {
			isLongTouchDown = false;
			isShowAnimationing=false;
			mEditBroadcastPictureActivity.setTitleVisable(true);
			setTouchedHeight(false);
			if (mEditBroadcastPictureActivity != null) {
				mEditBroadcastPictureActivity.setScrollViewEnable(true, 0, 0, 0);
			}
			for(int i=0;i<picArr.size();i++){
				if( picArr.get(i)!=null) {
					picArr.get(i).zoomType = 0;
				}
			}

		}
	}
	/**
	 * ScrollView在滚动
	 */
	public void touchAction( ){
//		int type=ev.getAction();
//		PalmchatLogUtils.i("WXL", "getAction==="+type);
		isCheckLongToutch=false;
		timeTouchDown=System.currentTimeMillis();
		/*if(mEditBroadcastPictureActivity!=null){
		  mEditBroadcastPictureActivity.setTitleVisable(false);
		}*/
		
		if(!isLongTouchDown){
			if(mEditBroadcastPictureActivity!=null){ 
				mEditBroadcastPictureActivity.setTitleVisable(true);
			}
			setTouchedHeight(false);
		}
	}
	private void checkLongToutch(float x,float y){
		for(int i=0;i<picArr.size();i++){
			if(isInRect(x,y,picArr.get(i).destRect.left,picArr.get(i).destRect.top,
					picArr.get(i).destRect.right,picArr.get(i).destRect.bottom)){
				selectIndex=i;
				preX=x;
				preY=y;
				offsetTouchedX=x-picArr.get(i).destRect.left;
				offsetTouchedY=y-picArr.get(i).destRect.top;
//				arrZoomType.set(  selectIndex,1) ;
				picArr.get(selectIndex).zoomType=1;
				selectRW=picArr.get(i).destRect.width();
				selectRH=picArr.get(i).destRect.height(); 
//				rectSelect.left+=selectRW/10;
//				rectSelect.top+=selectRH/10;
//				
//				selectRW=selectRW*9/10;
//				selectRH=selectRH*9/10;
				rectSelect.left=picArr.get(i).destRect.left;
				rectSelect.top=picArr.get(i).destRect.top;
				rectSelect.right=rectSelect.left+selectRW;
				rectSelect.bottom=rectSelect.top+selectRH;
				isLongTouchDown=true;
				isCheckLongToutch=false;
				
				time_move_status_changed=System.currentTimeMillis();
				break;
			 }
		}
	}
 
	private void setTouchedHeight(boolean isEditMode){
	 
			
			  
		int h=isEditMode?(mHeight+rectSelect.height()):mHeight;
		RelativeLayout.LayoutParams layout=	new RelativeLayout.LayoutParams(ImageUtil.DISPLAYW , h); 
	    setLayoutParams(layout);  
	    
	}
	private boolean isRunning;
	@Override
	public void run() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		while(isRunning){
			try {
				if(isCheckLongToutch&&System.currentTimeMillis()-timeTouchDown>500){
//					PalmchatLogUtils.e("WXL","time="+(System.currentTimeMillis()-timeTouchDown));
					checkLongToutch(toutchDownX,toutchDownY);
				}
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			postInvalidate();
		}
	
	}
	
	public boolean isLongTouchDown(){
		return isLongTouchDown;
	}

    public void setEditOnClickListener(OnEditClickListener listener){
        mEditAndDeleteHelper.setEditOnClickListener(listener);
    }

	/**
	 * 经过滤镜后返回的图片刷新界面
	 */
    public void applyVisionFilter(){
        List<Bitmap> bitmapList = VisionUtils.getEditInputBitmapCacheList();
        int picSize = picArr.size();
        for (int i=0; i<picSize; i++){
            picArr.get(i).img = bitmapList.get(i);
        }
        postInvalidate();
    }


    class EditAndDeleteHelper {
        final static int TOUCH_TYPE_NONE = 0;
        final static int TOUCH_TYPE_EDIT = 1;
        final static int TOUCH_TYPE_DELETE = 2;

       /* final static float EDIT_WIDTH_DP = 28;
        final static float EDIT_HEIGHT_DP = 12;
        final static float DELETE_WIDTH_DP = 16;
        final static float DELETE_HEIGHT_DP = 16;
        final static float MARGIN_DP = 4;
        final static float TEXT_SIZE_SP = 12;*/


        private int mCurrentTouchndex = -1;

        private int mEdit_padingLeftRight;
        private float mEditHeight;
        private int mDeleteWidth;
        private int mDeletHeight;
        private int mMargin;
        Paint.FontMetricsInt mFontMetrics;


        private Paint mEditPaint;
        private Paint mTextPaint;

        private Drawable mDeleteNDrawable;
        private Drawable mDeletePDrawable;

        private int mCurrTouchType = TOUCH_TYPE_NONE;

        private OnEditClickListener mOnEditClickListener;

		private String strEdit;

        public EditAndDeleteHelper() {
            Context context = getContext();
			mEdit_padingLeftRight =(int)getResources().getDimension(R.dimen.edit_broadcast_pic_edit_margin);
//            mEditHeight = (int)getResources().getDimension(R.dimen.edit_broadcast_pic_edit_h);
			mDeletHeight=mDeleteWidth =(int)getResources().getDimension(R.dimen.edit_broadcast_pic_delete_wh);
            mMargin = (int)getResources().getDimension(R.dimen.edit_broadcast_pic_edit_margin);
			float textSize= getResources().getDimension(R.dimen.edit_broadcast_pic_edit_textsize);
			strEdit= getResources().getString(R.string.edit_my_profile);//只有这个的Edit翻译了
			mEditPaint = new Paint();

            mEditPaint.setAntiAlias(true);
            mEditPaint.setStyle(Style.FILL);
            mEditPaint.setColor(Color.BLACK);
            mEditPaint.setAlpha(70);


            mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setColor(Color.WHITE);
            mTextPaint.setStrokeWidth(3);
            mTextPaint.setTextSize( textSize);
            mFontMetrics = mTextPaint.getFontMetricsInt();
            mTextPaint.setTextAlign(Paint.Align.CENTER);

            mDeleteNDrawable = getResources().getDrawable(R.drawable.btn_broadcast_edit_photo_delete_n );
            mDeletePDrawable = getResources().getDrawable(R.drawable.btn_broadcast_edit_photo_delete_p );


        }

        public int dip2px(Context context, float dipValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        }
        public void onDraw(Canvas canvas, int index) {
            Drawable deleteDrawble;
            Rect cellRect = picArr.get(index).destRect_draw_widthMagrin;
			float editRectWidth=mTextPaint.measureText(strEdit)+mEdit_padingLeftRight*2;
			mEditHeight=mTextPaint.getTextSize()+mEdit_padingLeftRight;
            RectF editRect = new RectF(cellRect.left + mMargin, cellRect.bottom - mMargin - mEditHeight, cellRect.left + mMargin + editRectWidth, cellRect.bottom - mMargin );

            Rect deleteRect = new Rect(cellRect.right - mMargin - mDeleteWidth, cellRect.top + mMargin, cellRect.right - mMargin, cellRect.top + mMargin + mDeletHeight);

            //edit bg
            canvas.drawRoundRect(editRect, 8, 8, mEditPaint);

            //edit text
            int baseline = (int) ((editRect.bottom + editRect.top - mFontMetrics.bottom - mFontMetrics.top) / 2);
            if(strEdit!=null){
				canvas.drawText(strEdit, editRect.centerX(), baseline, mTextPaint);
			}
            //delete btn
            deleteDrawble = mCurrTouchType== TOUCH_TYPE_DELETE && index==mCurrentTouchndex ? mDeletePDrawable : mDeleteNDrawable;

            deleteDrawble.setBounds(deleteRect);
            deleteDrawble.draw(canvas);
        }

        public void setEditOnClickListener(OnEditClickListener listener){

            mOnEditClickListener = listener;
        }



        public boolean onToutchEvent(MotionEvent event) {


            float x = event.getX();
            float y = event.getY();

            int preTouchIndex = mCurrentTouchndex;
            int preTouchType = mCurrTouchType;

            if (event.getAction() == MotionEvent.ACTION_DOWN){
                //reset touch info
                mCurrentTouchndex = -1;
                mCurrTouchType = TOUCH_TYPE_NONE;
            }


            for (int i = 0; i < picArr.size(); i++) {


                Rect cellRect = picArr.get(i).destRect_draw_widthMagrin;

                RectF editRect = new RectF(cellRect);

                RectF deleteRect = new RectF(cellRect.right - mMargin*2 - mDeleteWidth, cellRect.top , cellRect.right, cellRect.top + mMargin*2 + mDeletHeight);

                if (isInRect(x, y, deleteRect.left, deleteRect.top,
                        deleteRect.right, deleteRect.bottom)) {

                    mCurrentTouchndex = i;
                    mCurrTouchType = TOUCH_TYPE_DELETE;
                    break;
                } else if (isInRect(x, y, editRect.left, editRect.top,
                        editRect.right, editRect.bottom)) {

                    mCurrentTouchndex = i;
                    mCurrTouchType = TOUCH_TYPE_EDIT;
                    break;
                } else {
                    mCurrentTouchndex = -1;
                    mCurrTouchType = TOUCH_TYPE_NONE;
                }

            }

            if (event.getAction() == MotionEvent.ACTION_UP){

                if (mCurrentTouchndex != preTouchIndex){
                    mCurrentTouchndex = -1;
                    mCurrTouchType = TOUCH_TYPE_NONE;
                }

            }

            if (mCurrentTouchndex!= -1){

                return true;

            }
            else{
                return false;
            }
        }


        public void doProcessEvent(){

            if (mCurrTouchType == TOUCH_TYPE_DELETE){
                int index = mCurrentTouchndex;
//                removePic(mCurrentTouchndex);
//
//                mCurrentTouchndex = -1;
//                mCurrTouchType = TOUCH_TYPE_NONE;
//                postInvalidate();
                EventBus.getDefault().post(new ModifyPictureListEvent(ModifyPictureListEvent.DELETE,index));

            }
            else if (mCurrTouchType == TOUCH_TYPE_EDIT){

                int size = picArr.size();

                List<Bitmap> bitmapList = VisionUtils.getEditInputBitmapCacheList();
                bitmapList.clear();

                for (int i=0; i<size; i++){
                    bitmapList.add(picArr.get(i).img);

                }

                if (mOnEditClickListener!=null){
                    mOnEditClickListener.onEditClickListner(mCurrentTouchndex);
                }
                mCurrentTouchndex = -1;
                mCurrTouchType = TOUCH_TYPE_NONE;
            }
        }

    }




    public interface OnEditClickListener{

        void onEditClickListner(int photoIndex);

    }

}
