package com.afmobi.palmchat.ui.customview;

import java.util.ArrayList;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
/**
 * 送花和转盘动画
 * @author xiaolong
 *
 */
public class FlowerAniView extends View implements Runnable{
	private int aniType=0;//0花的动画，1转盘动画，2编辑广播图片引导动画
	private final int TYPE_FLOWER=0,TYPE_PREDICT=1,TYPE_EDIT_BROADCASTPHOTO=2;
	public FlowerAniView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
	}
	public FlowerAniView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle); 
	}
	private Bitmap []imgFlower;
	private Rect rectRoseSrc;
	private int flowersTextRectRadio;
	private Bitmap imgRose;
	private final int flowerSize=13;
	private final int FLOWER_MAX_COUNT=30; 
	private final int STIME1920=1920;
	private final int STIME300=300;
	private final int STIME360=360;
	private final int STIME420=420;
	private final int STIME480=480;
	private final int STIME540=540;
	private Thread mThread;
	private long timeStart,timeEnd;
	private PaintFlagsDrawFilter  mPaintFlagsDrawFilter=new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
	@Override
	protected void onDraw(Canvas canvas) { 
		super.onDraw(canvas);
		canvas.setDrawFilter(mPaintFlagsDrawFilter);  
		if(aniType==TYPE_FLOWER){
				if(isRunning&&mList==null){
					mList=new ArrayList<FlowerAniView.Particle>();
					 for(int i=0;i<FLOWER_MAX_COUNT;i++){
						 Particle _p=new Particle();
						 mList.add(_p);
					 } 
					 timeStart=System.currentTimeMillis();
				}
				if(imgRose==null){
					return ;
				}
				int _time=(int) (System.currentTimeMillis()-timeStart);
				if(_time<STIME540+STIME1920+STIME300){
					int _alpha=0xFF*_time/STIME300;
					 if(_time>STIME540+STIME1920){
						 _alpha=0xFF-0xFF*(_time-(STIME540+STIME1920))/STIME300;
					}
					if(_alpha>0xFF){
						_alpha=0xFF;
					}
					if(_alpha<0){
						_alpha=0;
					}
					mPaint.setAlpha(_alpha);
					int _w=imgRose.getWidth();
					int _h=imgRose.getHeight();
					if(_time<STIME300){
					    _w=imgRose.getWidth()*3/10+imgRose.getWidth()*7/10*_time/STIME300;
					    _h=imgRose.getHeight()*3/10+imgRose.getHeight()*7/10*_time/STIME300;
						if(_w>imgRose.getWidth() ){
							_w=imgRose.getWidth() ;
						}
						if(_h>imgRose.getHeight() ){
							_h=imgRose.getHeight() ;
						}
					}else if(_time<STIME360){
					    _w=imgRose.getWidth()  +imgRose.getWidth()*2/10*(_time-STIME300)/60;
					    _h=imgRose.getHeight() +imgRose.getHeight()*2/10*(_time-STIME300)/60;
						if(_w>imgRose.getWidth()*12/10){
							_w=imgRose.getWidth()*12/10;
						}
						if(_h>imgRose.getHeight()*12/10){
							_h=imgRose.getHeight()*12/10;
						}
					}
					else if(_time<STIME420){
						_w=imgRose.getWidth()*12/10  -imgRose.getWidth()*2/10*(_time-STIME360)/60;
					    _h=imgRose.getHeight()*12/10 -imgRose.getHeight()*2/10*(_time-STIME360)/60;
						if(_w<imgRose.getWidth() ){
							_w=imgRose.getWidth() ;
						}
						if(_h<imgRose.getHeight() ){
							_h=imgRose.getHeight() ;
						}
					}else if(_time<STIME480){
						 _w=imgRose.getWidth()  +imgRose.getWidth()*1/10*(_time-STIME420)/60;
						    _h=imgRose.getHeight() +imgRose.getHeight()*1/10*(_time-STIME420)/60;
							if(_w>imgRose.getWidth()*11/10){
								_w=imgRose.getWidth()*11/10;
							}
							if(_h>imgRose.getHeight()*11/10){
								_h=imgRose.getHeight()*11/10;
							}
					}else if(_time<STIME540){
						_w=imgRose.getWidth()*11/10  -imgRose.getWidth()*1/10*(_time-STIME480)/60;
					    _h=imgRose.getHeight()*11/10 -imgRose.getHeight()*1/10*(_time-STIME480)/60;
						if(_w<imgRose.getWidth() ){
							_w=imgRose.getWidth() ;
						}
						if(_h<imgRose.getHeight() ){
							_h=imgRose.getHeight() ;
						}
					}
					Rect _des=new Rect( getWidth()-_w>>1,getHeight()-_h>>1,(getWidth()-_w>>1)+_w,(getHeight()-_h>>1)+_h);
					canvas.drawBitmap(imgRose, rectRoseSrc,_des , mPaint);
					if(!TextUtils.isEmpty(flowerCount)){
						int _offyText=getHeight()/2+_h/2+_h/4;
						mPaint.setColor(0xccffffff);
						mPaint.setTextSize(_h/5);  
						RectF rectFlowerText=new RectF(
								(getWidth()-mPaint.measureText(flowerCount))/2-flowersTextRectRadio
								,  _offyText- mPaint.getTextSize() -flowersTextRectRadio/2 ,
								getWidth()/2+mPaint.measureText(flowerCount)/2+flowersTextRectRadio 
								,_offyText +flowersTextRectRadio );
						
						canvas.drawRoundRect(rectFlowerText,flowersTextRectRadio,flowersTextRectRadio,mPaint); 
						mPaint.setColor(0xffe93770);
						
						canvas.drawText(flowerCount,(getWidth()-mPaint.measureText(flowerCount))/2
								,  _offyText , mPaint);
					}
				}else{
					 stopPlayFlowers();
					 return ;
				}
					
				if(_time>=STIME540){
					mPaint.setAlpha(0xFF);
				 if(mList!=null){
					 for(int i=0;i<mList.size();i++){
						 if(mList.get(i).draw(canvas)){
							 mList.remove(i);
							 i--;
							 if(mList.isEmpty()){ 
		//						 stopPlayFlowers();
								 return ;
							 }
						 }
					 }
				 }
			 }
		}else if(aniType==TYPE_PREDICT){
			drawPalmGuess(canvas);
		}else if(aniType==TYPE_EDIT_BROADCASTPHOTO){
			drawTips(canvas);
		}
	}
	/**
	 * 绘制转盘
	 * @param canvas
	 */
	private void drawPalmGuess(Canvas canvas){ 
		if(imgPredict_Circle!=null){
			mPaint.setColor(colorPredict);
			mPaint.setStyle(Style.FILL);
			canvas.drawRect(0,0,getWidth(),getHeight(), mPaint);//
			if(rectDest_PredictCircle==null){ 
				float _w=imgPredict_Circle.getWidth()*zoom;//getWidth()*4/5;
				float _h=_w;
				rectDest_PredictCircle= new RectF( (getWidth()-_w)/2,(getHeight()-_h)/2,
				(getWidth()-_w )/2+_w,(getHeight()-_h )/2+_h);
				
				_w=imgPredictDouble_Points.getWidth()*zoom; 
				_h=imgPredictDouble_Points.getHeight()*zoom; 
				float dh =  getHeight() / 2 - imgPredict_Circle.getHeight()/2*zoom -  zoom*imgPredictDouble_Points.getHeight()*154/290 ;
				rectDest_PredictDouble= new RectF( (getWidth() - imgPredictDouble_Points.getWidth()*zoom)/2 ,dh ,
						(getWidth()-_w )/2+_w,dh+_h);
				
				rectDest_PredictStart= new RectF( 
						 rectDest_PredictCircle.centerX()-imgPredict_start[frameIndex_predictStart].getWidth()/2*zoom
						,rectDest_PredictCircle.centerY()-imgPredict_start[frameIndex_predictStart].getHeight()/2*zoom ,
						 rectDest_PredictCircle.centerX()+imgPredict_start[frameIndex_predictStart].getWidth()/2*zoom
						,rectDest_PredictCircle.centerY()+imgPredict_start[frameIndex_predictStart].getHeight()/2*zoom);
//				int imgIndex=0;
				for(int i=0;i<arrRectPredictBag.length;i++){
//					if(i==phonePointsIndex){
//						imgIndex=5;
//					}else{ 
//						imgIndex=i;
//						if(imgIndex==5){//5为手机 
//							imgIndex=4;
//						}
//					}
					arrRectPredictBag[i].left=rectDest_PredictCircle.left+arrRectPredictBag[i].left*zoom-imgPredict_moneyBag[predictMoneyBag_imgIndex[i] ].getWidth()/2*zoom;
					arrRectPredictBag[i].top=rectDest_PredictCircle.top+arrRectPredictBag[i].top*zoom-imgPredict_moneyBag[predictMoneyBag_imgIndex[i] ].getHeight()/2*zoom;
					arrRectPredictBag[i].right=arrRectPredictBag[i].left+imgPredict_moneyBag[predictMoneyBag_imgIndex[i]].getWidth()*zoom;
					arrRectPredictBag[i].bottom=arrRectPredictBag[i].top+imgPredict_moneyBag[predictMoneyBag_imgIndex[i]].getHeight()*zoom;
				}
			}
			mPaint.setAlpha(0xFF);
			// 			canvas.drawBitmap(imgPredictDouble_Points,  getWidth() - imgPredictDouble_Points.getWidth()>>1 ,dh , mPaint);  //add by zhh 画转盘头部的图片
 			canvas.drawBitmap(imgPredictDouble_Points, rectPredictDouble_src,rectDest_PredictDouble , mPaint);
			canvas.drawBitmap(imgPredict_Circle, rectPredictCircle_src,rectDest_PredictCircle , mPaint);
		 
			for(int i=0;i<arrRectPredictBag.length;i++){
				if(i==phonePointsIndex){ 
					canvas.drawBitmap(imgPredict_moneyBag[predictMoneyBag_imgIndex[i] ], 
							 rectPredictBag_Phone_src ,arrRectPredictBag[i]//rectDest_PredictCircle.top+arrRectPredictBag[i].top-imgPredict_moneyBag[i%3].getHeight()/2
							, mPaint);
				}else{  
				  canvas.drawBitmap(imgPredict_moneyBag[predictMoneyBag_imgIndex[i] ], 
						  rectPredictBag_src//rectDest_PredictCircle.left+arrRectPredictBag[i].left-imgPredict_moneyBag[i%3].getWidth()/2
						,arrRectPredictBag[i]//rectDest_PredictCircle.top+arrRectPredictBag[i].top-imgPredict_moneyBag[i%3].getHeight()/2
						, mPaint);
				  drawNum(canvas,predictArrPoints[i],arrRectPredictBag[i].centerX(),arrRectPredictBag[i].bottom-arrRectPredictBag[i].height()/5);
				}
//				canvas.drawText(predictArrPoints[i]+strPoints, rectDest_PredictCircle.left, rectDest_PredictCircle.top, mPaint);
				
			}
			 
		    Matrix matrix = new Matrix(); 
		    matrix.postScale( zoom,  zoom);
		    matrix.postTranslate(rectDest_PredictCircle.centerX(), rectDest_PredictCircle.centerY());  
		    
            matrix.postRotate(predictRotation,  rectDest_PredictCircle.centerX() ,rectDest_PredictCircle.centerY());  
			canvas.drawBitmap(imgPredict_Fan, matrix , mPaint);
//			canvas.drawBitmap(imgPredict_start[frameIndex_predictStart],
//					rectDest_PredictCircle.centerX()-imgPredict_start[frameIndex_predictStart].getWidth()/2
//					,rectDest_PredictCircle.centerY()-imgPredict_start[frameIndex_predictStart].getHeight()/2
//					,mPaint);
			canvas.drawBitmap(imgPredict_start[frameIndex_predictStart], 
					rectPredictStart_src,rectDest_PredictStart , mPaint);
			
			
			if(isStartPredictAni){
				   predictRotation+=predictSpeed;
				    if(predictRotation>DEGREE_360){
				    	predictRotation=0;
				    }
				int _time=(int) (System.currentTimeMillis()-timeStart);
				if(_time<STIME1920*3/2){
					predictSpeed=1+_time/(STIME540/6);
					if(predictSpeed>DEGREE_60){
						predictSpeed=DEGREE_60;
						if(predictRotation%DEGREE_60!=0){
							predictRotation=predictRotation/DEGREE_60*DEGREE_60;
						}
					}
				} else if(_time>STIME1920*3){ 
					if(predictSpeed==0){
						if(System.currentTimeMillis()-timeEnd>STIME1920 ){//over 
							if(isRunning&&mAnimationChangeListener!=null){
								mAnimationChangeListener.animationStoped();//动画完成播放 回调
							}
							stopPlayPredict();
							return;
						}
					}else if(predictSpeed<6){
						int offD=(DEGREE_60*winDegree+DEGREE_60+DEGREE_60/2);
						if( Math.abs( predictRotation-offD)<6){
							predictRotation=offD;
							predictSpeed=0;
							timeEnd=System.currentTimeMillis();
						}
					}else{
						predictSpeedAdd++;
						if(predictSpeedAdd >5){
							predictSpeedAdd=0;
							predictSpeed--;
						}
						if(predictSpeed<0){
							predictSpeed=0;
						}
					}
				} 
			}else{
				 
			}
		}
	
	} 
	private boolean isStartPredictAni;
	private int winDegree=0;
	private int frameIndex_predictStart;
	private int predictSpeedAdd;
	private int predictSpeed=1;
	private final int DEGREE_60=60,DEGREE_360=360;
	private final int MONEY_BAG_COUNT=6;
   private int predictRotation=30+180;
	private Rect rectPredictCircle_src,rectPredictDouble_src,rectPredictStart_src,rectPredictBag_src,rectPredictBag_Phone_src;
	private RectF rectDest_PredictCircle,rectDest_PredictDouble,rectDest_PredictStart;
	private RectF rectPredictstart ;
	private RectF[]arrRectPredictBag;
   private Bitmap imgPredict_Circle,imgPredict_Fan,imgPredictDouble_Points;
   private Bitmap []imgPredict_start;
   private Bitmap []imgPredict_moneyBag;
   private Bitmap imgPredict_num;
   private AnimationChangeListener mAnimationChangeListener;
//   private int[] predictArrPoints;
//   private String strPoints;
   private int colorPredict=0x33000000;
   private float zoom=1.0f;// 当屏幕太小时 需要缩小
   private int phonePointsIndex=0;//手机点索引
   private int[] predictMoneyBag_imgIndex;//对应每个奖项的奖品图片索引
   private int[] predictArrPoints;
   /**
    * 开始PalmGuess转盘抽奖动画
    * @param _aniListener  实现该接口 监听动画播放完事件
    */
   /*public void startPlayPredict( AnimationChangeListener _aniListener,int[]arrPoints,int winPoints){
		aniType=TYPE_PREDICT ; 
		mAnimationChangeListener=_aniListener;
		if(mPaint==null){
			mPaint = new Paint();
		} 
		
		//先用冒泡排序安升序排列中奖值
		int n = arrPoints.length;    
	    for (int i = 0; i < n - 1; i++) {    
	      for (int j = 0; j < n - 1; j++) {   
	  
	        if (arrPoints[j] > arrPoints[j + 1]) {    
	          int temp = arrPoints[j];    
	          arrPoints[j] = arrPoints[j + 1];    
	          arrPoints[j + 1] = temp;    
	        }   
	      }    
	    }   
	  
	    predictArrPoints=arrPoints;
		
		if(arrPoints!=null){//找出中奖的索引
//			int []losePoints=new int[arrPoints.length-1];
			for(int i=0,j=0;i<arrPoints.length;i++){
				if(winPoints==arrPoints[i]){
					winDegree=i; 
				}else{//未中之数
//					losePoints[j]=i;	
					j++;
				} 
			} 
			if(winDegree==5){//如果中将值刚好是最大值 那么要往前移
				winPoints=arrPoints[4]=arrPoints[5];
				winDegree=4;
			}
			int []arrPointsToDegreeIndex=new int []{2,0,4,1,3};//中奖索引和角度索引匹配
			winDegree=arrPointsToDegreeIndex[winDegree];
			predictMoneyBag_imgIndex=new int[arrPoints.length];
			phonePointsIndex=5;//设定最下面的为Phone写死了 不再随机//losePoints[(int) (Math.abs( Math.random()*10)%(arrPoints.length-1))];//
			for(int i=0,j=0;i<arrPoints.length;i++){//设置对应的奖品图片索引，包括手机图片
				*//*if(phonePointsIndex==i){
					predictMoneyBag_imgIndex[i]=5;//5为手机图片
				}else{ 
					predictMoneyBag_imgIndex[i]=j;
					j++; 
				}*//*
				predictMoneyBag_imgIndex[i]=i;
			} 
			
		}
		if(imgPredict_Circle==null){  
			imgPredictDouble_Points = BitmapFactory.decodeResource(getResources(), R.drawable.icon_double_points);
			imgPredict_Circle=BitmapFactory.decodeResource(getResources(), R.drawable.bg_turnplate_predict);
			imgPredict_Fan=BitmapFactory.decodeResource(getResources(), R.drawable.img_start_cursor_n);
			imgPredict_start=new Bitmap[2];
			imgPredict_start[0]=BitmapFactory.decodeResource(getResources(), R.drawable.btn_start_turnplate_n);
			imgPredict_start[1]=BitmapFactory.decodeResource(getResources(), R.drawable.btn_start_turnplate_p);
			imgPredict_num=BitmapFactory.decodeResource(getResources(), R.drawable.img_palmguess_0_x);
			
			imgPredict_moneyBag=new Bitmap[6];
			imgPredict_moneyBag[0]=BitmapFactory.decodeResource(getResources(), R.drawable.img_palmguess_gift_01);
			imgPredict_moneyBag[1]=BitmapFactory.decodeResource(getResources(), R.drawable.img_palmguess_gift_02);
			imgPredict_moneyBag[2]=BitmapFactory.decodeResource(getResources(), R.drawable.img_palmguess_gift_03);
			imgPredict_moneyBag[3]=BitmapFactory.decodeResource(getResources(), R.drawable.img_palmguess_gift_04);
			imgPredict_moneyBag[4]=BitmapFactory.decodeResource(getResources(), R.drawable.img_palmguess_gift_05);
			imgPredict_moneyBag[5]=BitmapFactory.decodeResource(getResources(), R.drawable.img_palmguess_gift_smartphone);
			//double图片Y坐标
			int _height= AppUtils.HEIGHT;
			if(AppUtils.HEIGHT<=800){
				_height=AppUtils.HEIGHT*3/4;
			}
			float dy =_height/ 2 - imgPredict_Circle.getHeight()/2 - imgPredictDouble_Points.getHeight()*154/290 ;//154/290 是图片需要和转盘不重叠部分的比例
			if(dy<0){//如果手机屏幕太小了，那要缩放
				zoom=(_height/2)/((float)(imgPredict_Circle.getHeight()/2+ imgPredictDouble_Points.getHeight()*154/290));
			}
			rectPredictstart=new RectF(0,0,imgPredict_start[0].getWidth()*zoom,imgPredict_start[1].getHeight()*zoom);
			rectPredictCircle_src=new Rect(0,0,  imgPredict_Circle.getWidth()  , imgPredict_Circle.getHeight() );
			rectPredictDouble_src=new Rect(0,0,  imgPredictDouble_Points.getWidth()  , imgPredictDouble_Points.getHeight() );
			rectPredictStart_src=new Rect(0,0,  imgPredict_start[0].getWidth()  , imgPredict_start[0].getHeight() );
		 	rectPredictBag_src=new Rect(0,0,  imgPredict_moneyBag[0].getWidth()  , imgPredict_moneyBag[0].getHeight() );
		 	rectPredictBag_Phone_src=new Rect(0,0,  imgPredict_moneyBag[5].getWidth()  , imgPredict_moneyBag[5].getHeight() );
		 	arrRectPredictBag=new RectF[MONEY_BAG_COUNT];
		 	
		 	float zoomNum=((float)imgPredict_num.getWidth())/((float)predict_num_cell[predict_num_cell.length-1]);
		 	for(int i=0;i<predict_num_cell.length;i++){
		 		predict_num_cell[i]=(int) (predict_num_cell[i]*zoomNum);
		 	}
			float WH=imgPredict_moneyBag[0].getWidth() ;
			int[][]pos=new int[][]{
					{rectPredictCircle_src.width()/2, rectPredictCircle_src.width()/5},
					{ rectPredictCircle_src.width() /4, rectPredictCircle_src.width()*2/3},
					{rectPredictCircle_src.width()*3/4, rectPredictCircle_src.width()*2/3},
					
					{rectPredictCircle_src.width()/4, rectPredictCircle_src.width()/3},
					{ rectPredictCircle_src.width()*3/4, rectPredictCircle_src.width()/3},
					{rectPredictCircle_src.width()/2, rectPredictCircle_src.width()*4/5} 
				
			};
			for(int i=0;i<arrRectPredictBag.length;i++){
				arrRectPredictBag[i]=new RectF(pos[i][0], pos[i][1],WH,WH);
			}
//			arrRectPredictBag[0]=new RectF( rectPredictCircle_src.width()/2, rectPredictCircle_src.width()/5,WH,WH);
//			arrRectPredictBag[1]=new RectF( rectPredictCircle_src.width()/2, rectPredictCircle_src.width()*4/5,WH,WH);
//			arrRectPredictBag[5]=new RectF( rectPredictCircle_src.width()/4, rectPredictCircle_src.width()/3,WH,WH);
//			arrRectPredictBag[2]=new RectF( rectPredictCircle_src.width()*3/4, rectPredictCircle_src.width()/3,WH,WH);
//			arrRectPredictBag[4]=new RectF( rectPredictCircle_src.width() /4, rectPredictCircle_src.width()*2/3,WH,WH);
//			arrRectPredictBag[3]=new RectF( rectPredictCircle_src.width()*3/4, rectPredictCircle_src.width()*2/3,WH,WH);
			
			arrRectPredictBag[phonePointsIndex].set(pos[phonePointsIndex][0], pos[phonePointsIndex][1]
					,rectPredictBag_Phone_src.width(),rectPredictBag_Phone_src.height());
		}
		 if(mThread==null){
			 mThread=new Thread(this); 
			 mThread.start();
		 } 
		 isRunning=true;
		 setVisibility(View.VISIBLE);
		
	}*/
   											//0,22,22,16,23,22,23,22,22,23,22,21
   private int[] predict_num_cell=new int[]{0,22,44,60,83,105,128,150,172,195,217,238};
   private void drawNum(Canvas canvas,int num,float centerX,float y){
	   String strNum=String.valueOf(num);
	   char[] arrChar=new char[strNum.length()];
	  int offx=predict_num_cell[1];//加x的宽度
	   for(int i=0;i<strNum.length();i++){
		   arrChar[i]=(char) (strNum.charAt(i)-48);
		   arrChar[i]++;
		   if( arrChar[i]<11){
			   offx+=(predict_num_cell[arrChar[i]+1]-predict_num_cell[arrChar[i]]);
		   }
	   }
	   centerX-=offx/2;//居中
	   //绘制x
	   canvas.drawBitmap(imgPredict_num, new Rect(predict_num_cell[0],0,predict_num_cell[1],imgPredict_num.getHeight())
	   , new RectF(centerX,y,centerX+(predict_num_cell[1]-predict_num_cell[0]),y+imgPredict_num.getHeight()), null);
	   centerX+=(predict_num_cell[ 1]-predict_num_cell[0]);
	   //绘制数值
	   for(int i=0;i<strNum.length();i++){
		   arrChar[i]=(char) (strNum.charAt(i)-48);
		   arrChar[i]++;
		   if( arrChar[i]<11){
			   canvas.drawBitmap(imgPredict_num, new Rect(predict_num_cell[arrChar[i]],0,predict_num_cell[arrChar[i]+1],imgPredict_num.getHeight())
			   , new RectF(centerX,y,centerX+(predict_num_cell[arrChar[i]+1]-predict_num_cell[arrChar[i]]),y+imgPredict_num.getHeight()), null);
			   centerX+=(predict_num_cell[arrChar[i]+1]-predict_num_cell[arrChar[i]]);
		   }
	   }
	   
   }
   public void stopPlayPredict(){ 
		
		isRunning=false; 
		if(imgPredict_start!=null){
			for(int i=0;i<imgPredict_start.length;i++){
				if(imgPredict_start[i]!=null){
					if(!imgPredict_start[i].isRecycled()){
						imgPredict_start[i].recycle();
					}
					imgPredict_start[i]=null;
				}
			}
			imgPredict_start=null;
		}
		if(imgPredictDouble_Points!=null){ 
			if(!imgPredictDouble_Points.isRecycled()){
				imgPredictDouble_Points.recycle();
			}
			imgPredictDouble_Points=null;
		}
		if(imgPredict_moneyBag!=null){
			for(int i=0;i<imgPredict_moneyBag.length;i++){
				if(imgPredict_moneyBag[i]!=null){
					if(!imgPredict_moneyBag[i].isRecycled()){
						imgPredict_moneyBag[i].recycle();
					}
					imgPredict_moneyBag[i]=null;
				}
			}
			 arrRectPredictBag=null;
				
			 
			imgPredict_moneyBag=null;
		}
		
		 
		if(imgPredict_Circle!=null){
			if(!imgPredict_Circle.isRecycled()){
				imgPredict_Circle.recycle();
			}
			imgPredict_Circle=null;
		}
		if(imgPredict_Fan!=null){
			if(!imgPredict_Fan.isRecycled()){
				imgPredict_Fan.recycle();
			}
			imgPredict_Fan=null;
		}
		if(imgPredict_num!=null){
			if(!imgPredict_num.isRecycled()){
				imgPredict_num.recycle();
			}
			imgPredict_num=null;
		}
		
		 setVisibility(View.GONE);
		if(mThread!=null){
			mThread=null; 
		} 
	}
	private String flowerCount;
	public void startPlayFlowers(String _flowerCount){
		aniType=TYPE_FLOWER;
		if(_flowerCount==null){
			flowerCount="";
		}else{
			flowerCount = "x " + getResources().getString(R.string.Flowers).replace("XXXX", _flowerCount);
		}
		if(mPaint==null){
			mPaint = new Paint();
		}
		flowersTextRectRadio=getResources().getDimensionPixelSize(R.dimen.d_10dp);
		if(imgFlower==null){
			 imgFlower=new Bitmap[flowerSize];
			 imgFlower[0]=BitmapFactory.decodeResource(getResources(), R.drawable.petal_01);
			 imgFlower[1]=BitmapFactory.decodeResource(getResources(), R.drawable.petal_02);
			 imgFlower[2]=BitmapFactory.decodeResource(getResources(), R.drawable.petal_03);
			 imgFlower[3]=BitmapFactory.decodeResource(getResources(), R.drawable.petal_04);
			 imgFlower[4]=BitmapFactory.decodeResource(getResources(), R.drawable.petal_05);
			 imgFlower[5]=BitmapFactory.decodeResource(getResources(), R.drawable.petal_06);
			 imgFlower[6]=BitmapFactory.decodeResource(getResources(), R.drawable.petal_07);  
			 imgFlower[7]=BitmapFactory.decodeResource(getResources(), R.drawable.petal_08);  
			 imgFlower[8]=BitmapFactory.decodeResource(getResources(), R.drawable.petal_09);  
			 imgFlower[9]=BitmapFactory.decodeResource(getResources(), R.drawable.petal_010);  
			 imgFlower[10]=BitmapFactory.decodeResource(getResources(), R.drawable.petal_011);  
			 imgFlower[11]=BitmapFactory.decodeResource(getResources(), R.drawable.petal_012);  
			 imgFlower[12]=BitmapFactory.decodeResource(getResources(), R.drawable.petal_013);  
			  
		 }
		if(imgRose==null){
			imgRose=BitmapFactory.decodeResource(getResources(), R.drawable.rose_01);
			rectRoseSrc=new Rect(0,0,imgRose.getWidth(),imgRose.getHeight());
		}
		 if(mThread==null){
			 mThread=new Thread(this); 
			 mThread.start();
		 } 
		 isRunning=true;
		 setVisibility(View.VISIBLE);
		
	}
	private boolean isRunning;
	public void stopPlayFlowers(){
		isRunning=false;
		if(mList!=null){ 
			mList.clear();
			mList=null;  
		}
		if(imgFlower!=null){
			for(int i=0;i<imgFlower.length;i++){
				if(imgFlower[i]!=null){
					if(!imgFlower[i].isRecycled()){
						imgFlower[i].recycle();
					}
					imgFlower[i]=null;
				}
			}
			imgFlower=null;
		}
		if(imgRose!=null){
			if(!imgRose.isRecycled()){
				imgRose.recycle();
			}
			imgRose=null;
		}
		 setVisibility(View.GONE);
		if(mThread!=null){
			mThread=null; 
		} 
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if( aniType==TYPE_EDIT_BROADCASTPHOTO){
//			if(isTipsCanCancel){
				stopPlayEditBroadcastPicTips();
			/*}else{20160822 改为点击任意都可以取消动画
				if(ev.getAction()==MotionEvent.ACTION_UP&&Math.abs(skip_x+skip_Width/2-ev.getX())<skip_Width/2&&
						Math.abs( skip_y-tips_text_size/2-ev.getY())<tips_text_size  ){
					stopPlayEditBroadcastPicTips();
				}
			}*/
			return true;
		}
		if(isStartPredictAni){
			return super.onTouchEvent(ev);
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			 if(rectDest_PredictCircle!=null){
				 if(Math.abs(rectDest_PredictCircle.centerX()-ev.getX())<rectDest_PredictCircle.width()/2&&
						 Math.abs( rectDest_PredictCircle.centerY()-ev.getY())<rectDest_PredictCircle.height()/2 ){
					 frameIndex_predictStart=1;
				 }
			 }
			break;
		case MotionEvent.ACTION_UP:
			frameIndex_predictStart=0;
			if(rectDest_PredictCircle!=null){ 
				 if(Math.abs(rectDest_PredictCircle.centerX()-ev.getX())<rectDest_PredictCircle.width()/2&&
						 Math.abs( rectDest_PredictCircle.centerY()-ev.getY())<rectDest_PredictCircle.height()/2 ){
					 isStartPredictAni=true;
					 timeStart=System.currentTimeMillis();
				 }
			 }
			break;
		}
		return true;//super.onTouchEvent(ev);
	}
	private Paint mPaint;
	private ArrayList <Particle>mList;
	class Particle{
		int imgType;
		int x,y;
		int speedX,speedY;
		Particle(){
			imgType=(int)(Math.random() * (flowerSize ));
			y=-10-(int)(Math.random() *getHeight()*3/4 );
			x=   (int)(Math.random() * getWidth()*13/14);  
			speedX=1+(int)(Math.random() *5);
			speedY=6+(1+(int)( getHeight()/130));
			if(Math.random()*4<2){
				speedX+=(Math.random()*8);
				speedY+=(Math.random()*7);
			}
			if(Math.random()*2<1){
				speedX=-speedX;
			}
			
			
		}
		boolean draw(Canvas g){ 
			if(y>-10&&imgFlower[imgType]!=null){
				g.drawBitmap(imgFlower[imgType],x,y,mPaint);
				x+=speedX;
			}
			if(y>getHeight()){
				return true;
			}
			if(x<0){
				speedX=Math.abs(speedX) *(1+(int)(Math.random() *4))/4;
				if(speedX<1){
					speedX=1;
				}
			}else if(x>getWidth()*12/13 ){
				speedX=-Math.abs(speedX) *(1+(int)(Math.random() *4))/4;
				if(speedX==0){
					speedX=-1;
				}
			}
			
			y+=speedY;
			return false;
		}
	}
	/**
	 * ----------------------------------------------------------------------------------------------
	 * 编辑广播图片动画
	 */
	
	private Bitmap imgTipsHand;
	private Rect rectTipsHand,rectTipsHand_scale;
	/**
	 * 开始播放动画
	 * @param _aniListener
	 */
	public void startPlayEditBroadcastPicTips( AnimationChangeListener _aniListener ){
		aniType=TYPE_EDIT_BROADCASTPHOTO ; 
		mAnimationChangeListener=_aniListener;
		if(mPaint==null){
			mPaint = new Paint();
		} 
		tips_margin=(int) getResources().getDimension(R.dimen.accounts_imagetxt_description_LineSpacing_size);
		 tips_text_size=  getResources().getDimension(R.dimen.broadcast_tagpage_dialog_text_size);
		 tips_marginTop=(int) getResources().getDimension(R.dimen.group_profile_photo_margin_top);
		 
		 tips_text=getResources().getString(R.string.drag_img_change_layout);
		skip_text=getResources().getString(R.string.skip)+">>";
		imgTipsHand = BitmapFactory.decodeResource(getResources(), R.drawable.img_hand_demo_editphoto);
		rectTipsHand=new Rect(0,0,imgTipsHand.getWidth(),imgTipsHand.getHeight());
		rectTipsHand_scale=new Rect(0,0,imgTipsHand.getWidth(),imgTipsHand.getHeight());
		isTipsCanCancel=false;
		 if(mThread==null){
			 mThread=new Thread(this); 
			 mThread.start();
		 } 
		 isRunning=true;
		 setVisibility(View.VISIBLE); 
	}
	 public void stopPlayEditBroadcastPicTips(){  
			isRunning=false;  
			 setVisibility(View.GONE);
			 if(imgTipsHand!=null){
				 if(!imgTipsHand.isRecycled()){
					 imgTipsHand.recycle();
				 }
				 imgTipsHand=null;
			 }
			 tipsCellPic=null;
			 circle_radius=0;
			 circle_apha=0;
			  select_apha=0xff;
			  tips_hand_scale=100;
			if(mThread!=null){
				mThread=null; 
			} 
	 }
	class CellPic{ 
		RectF srcRect ;//图片真实尺寸 
		RectF destRect ;//要画到的位置的
		RectF destRect_aniStart;//产生新布局后 显示移动过去的动画时的起始位置
//		RectF destRect_aniMove;//产生新布局后 显示移动过去的动画时 
	}
	private ArrayList<CellPic>tipsCellPic ; 
	private long timeTipsStart;
	private boolean isTipsCanCancel;
	private int tips_margin;//引导框里的间距
	private float tips_text_size;//
	private int tips_marginTop;//
	private String tips_text;
	private String skip_text;
//	private Rect rectTipsBg;
	private float skip_Width,skip_x,skip_y;

	/**
	 * 绘制编辑广播图片提示动画
	 * @param canvas
	 */
	private void drawTips(Canvas canvas){  
		mPaint.setColor(0xBF000000);
		int bgW=ImageUtil.DISPLAYW*79/100;
		int bgH=bgW	*100/88;
		final float sx=ImageUtil.DISPLAYW/2-bgW/2;
		final float sy=ImageUtil.DISPLAYH/2-bgH/2;   
		mPaint.setStyle(Style.FILL); 
		canvas.drawRect(0,0,ImageUtil.DISPLAYW,ImageUtil.DISPLAYH, mPaint);
		float scaleTo_dy=sy+bgH/3;
		
		mPaint.setColor(0xffcccccc);
		mPaint.setTextSize(tips_text_size);
		canvas.drawText(tips_text, (ImageUtil.DISPLAYW-mPaint.measureText(tips_text))/2, sy -tips_marginTop, mPaint);
		skip_Width=mPaint.measureText(skip_text);
		skip_x=sx+bgW-skip_Width;
		skip_y=sy +bgH+tips_marginTop+tips_text_size;
		canvas.drawText(skip_text, skip_x  ,skip_y , mPaint);

		mPaint.setColor(0xff0091ea);
	
		final int cellW=bgW/3-tips_margin;
		final int cellH=cellW*3/4;
		if(tipsCellPic==null){  
			
			timeTipsStart=System.currentTimeMillis();
			tipsCellPic=new  ArrayList<CellPic>();
			float  dx=sx;
			float  dy=sy;
			for(int i=0;i<3;i++){ 
					CellPic _c=new CellPic(); 
					tipsCellPic.add(_c);  
					
					 
					_c.srcRect=new RectF(dx,dy,dx+cellW,dy+cellH) ;
					_c.destRect=new RectF(dx,dy,dx+cellW,dy+cellH) ; 
					_c.destRect_aniStart=new RectF(0,0,cellW,cellH) ; 
					 
					dx+=cellW; 
					dx+=tips_margin;
			}
		}
		final float maxRadius=cellW/5;
		final float minRadius=cellW/7;
		
		float timePass=(float) (System.currentTimeMillis()-timeTipsStart);
		if(timePass>TIPS_TIME_SCALE+TIPS_SCALE_TIME_CAST+1000){
			isTipsCanCancel=true;
			timeTipsStart=System.currentTimeMillis(); 
			float  dx=sx;
			float  dy=sy;
			for(int i=0;i<tipsCellPic.size();i++){   
					tipsCellPic.get(i).srcRect.set( dx,dy,dx+cellW,dy+cellH) ;
					tipsCellPic.get(i).destRect.set( dx,dy,dx+cellW,dy+cellH) ; 
					tipsCellPic.get(i).destRect_aniStart.set(0,0,cellW,cellH) ;  
					dx+=cellW; 
					dx+=tips_margin; 
			}
			circle_radius=0;
			tips_hand_scale=100;
		     circle_apha=0;
			  select_apha=0xff;
		}else if(timePass>=TIPS_TIME_SCALE+TIPS_SCALE_TIME_CAST ){//  
			tipsCellPic.get(0).destRect.right=tipsCellPic.get(0).destRect.left+(bgW/2-tips_margin );
			tipsCellPic.get(0).destRect.bottom=tipsCellPic.get(0).destRect.top+(tipsCellPic.get(0).destRect.right-tipsCellPic.get(0).destRect.left)*3/4;
			
			tipsCellPic.get(2).destRect.left=tipsCellPic.get(2).srcRect.right-(bgW/2-tips_margin );
			tipsCellPic.get(2).destRect.bottom=tipsCellPic.get(2).srcRect.top+(tipsCellPic.get(2).destRect.right-tipsCellPic.get(2).destRect.left)*3/4;;
		
			
			tipsCellPic.get(1).destRect.left=tipsCellPic.get(0).srcRect.left ;
			tipsCellPic.get(1).destRect.right=tipsCellPic.get(2).srcRect.right ;
			tipsCellPic.get(1).destRect.top= tipsCellPic.get(0).destRect.bottom+tips_margin;
		    tipsCellPic.get(1).destRect.bottom= sy+bgH;   
			select_apha=0xff;
		}
		 else if(timePass>TIPS_TIME_SCALE){//2160){开始放大
			tipsCellPic.get(1).destRect.left=tipsCellPic.get(1).srcRect.left-(tipsCellPic.get(1).srcRect.left-sx ) *(timePass-TIPS_TIME_SCALE)/TIPS_SCALE_TIME_CAST;
			tipsCellPic.get(1).destRect.right=tipsCellPic.get(1).srcRect.right+(tipsCellPic.get(1).srcRect.left-sx ) *(timePass-TIPS_TIME_SCALE)/TIPS_SCALE_TIME_CAST;
			tipsCellPic.get(1).destRect.top=tipsCellPic.get(1).destRect_aniStart.top-
					(tipsCellPic.get(1).destRect_aniStart.top-scaleTo_dy ) *(timePass-TIPS_TIME_SCALE)/TIPS_SCALE_TIME_CAST;
			tipsCellPic.get(1).destRect.bottom=tipsCellPic.get(1).destRect_aniStart.bottom+
					(sy+bgH-tipsCellPic.get(1).destRect_aniStart.bottom ) *(timePass-TIPS_TIME_SCALE)/TIPS_SCALE_TIME_CAST;  
			tipsCellPic.get(0).destRect.right=tipsCellPic.get(0).srcRect.right+(  bgW/2-tips_margin-tipsCellPic.get(0).srcRect.width()) *(timePass-TIPS_TIME_SCALE)/TIPS_SCALE_TIME_CAST;
			tipsCellPic.get(0).destRect.bottom=tipsCellPic.get(0).srcRect.bottom+( bgH/3-tips_margin -tipsCellPic.get(0).srcRect.height()) *(timePass-TIPS_TIME_SCALE)/TIPS_SCALE_TIME_CAST;
			
			tipsCellPic.get(2).destRect.left=tipsCellPic.get(2).srcRect.left-(  bgW/2-tips_margin-tipsCellPic.get(2).srcRect.width() ) *(timePass-TIPS_TIME_SCALE)/TIPS_SCALE_TIME_CAST;
			tipsCellPic.get(2).destRect.bottom=tipsCellPic.get(2).srcRect.bottom+( bgH/3 -tips_margin-tipsCellPic.get(2).srcRect.height() ) *(timePass-TIPS_TIME_SCALE)/TIPS_SCALE_TIME_CAST;
			circle_apha=0;
			circle_radius=0;
			select_apha=select_apha+(0xff-select_apha)*(timePass-TIPS_TIME_SCALE)/TIPS_SCALE_TIME_CAST;
			if(select_apha>0xff){
				select_apha=0xff;
			}
			
		} else if(timePass>TIPS_TIME_MOVE_RELEASE){//移到目的地 开始释放长按
				tips_hand_scale=tips_hand_scale_pre+(TIPS_HAND_SCALE)*(timePass-TIPS_TIME_MOVE_RELEASE)/TIPS_TIME_RELEASE_CAST; 
				/*if(tips_hand_scale>100){
					tips_hand_scale=100;
				}*/
				circle_radius=minRadius+(maxRadius-minRadius)*(timePass-TIPS_TIME_MOVE_RELEASE)/TIPS_TIME_RELEASE_CAST; 
				circle_apha=circle_apha-(TIPS_ALPHA_CIRCLE_MAX-TIPS_ALPHA_CIRCLE_MIN)*(timePass-TIPS_TIME_MOVE_RELEASE)/TIPS_TIME_RELEASE_CAST; 
				if(circle_apha<0){
					circle_apha=0;
				}
			 
		}
		else if(timePass>TIPS_TIME_MOVE){//1000+160+120){//移动中
			tips_hand_scale_pre=tips_hand_scale;
			tipsCellPic.get(1).destRect_aniStart.top=tipsCellPic.get(1).destRect.top=tipsCellPic.get(1).srcRect.top+cellW/8+bgH/2*(timePass-TIPS_TIME_MOVE)/TIPS_TIME_MOVE_CAST;
		  tipsCellPic.get(1).destRect_aniStart.bottom=tipsCellPic.get(1).destRect.bottom=tipsCellPic.get(1).destRect.top-2*cellH/8+tipsCellPic.get(1).srcRect.height();
		}
		else if(timePass>TIPS_TIME_HOLD){
			
		}
		else if(timePass>TIPS_TIME_PRESS){//长安完成，选择的图片开始变透明变小
			
			select_apha  =0xff-(0xff-TIPS_ALPHA_CIRCLE_MAX)*(timePass-TIPS_TIME_PRESS)/TIPS_TIME_ALPHACHANGE_CAST;
			if(select_apha<TIPS_ALPHA_CIRCLE_MAX){
				select_apha=TIPS_ALPHA_CIRCLE_MAX;
			}
			tipsCellPic.get(1).destRect.left=tipsCellPic.get(1).srcRect.left+(cellW/9) *(timePass-TIPS_TIME_PRESS)/TIPS_TIME_ALPHACHANGE_CAST;
			tipsCellPic.get(1).destRect.right=tipsCellPic.get(1).srcRect.right-(cellW/9) *(timePass-TIPS_TIME_PRESS)/TIPS_TIME_ALPHACHANGE_CAST;
			tipsCellPic.get(1).destRect.top=tipsCellPic.get(1).srcRect.top+ (cellH/9) *(timePass-TIPS_TIME_PRESS)/TIPS_TIME_ALPHACHANGE_CAST;
			tipsCellPic.get(1).destRect.bottom=tipsCellPic.get(1).srcRect.bottom- (cellH/9 ) *(timePass-TIPS_TIME_PRESS)/TIPS_TIME_ALPHACHANGE_CAST; 
			
		}
		else if(timePass>1000){//开始出现长安的收和圈圈 
			circle_radius=maxRadius-(maxRadius-minRadius)*(timePass-1000)/TIPS_TIME_PRESS_CAST; 
			tips_hand_scale=100-(TIPS_HAND_SCALE)*(timePass-1000)/TIPS_TIME_PRESS_CAST; 
			circle_apha=TIPS_ALPHA_CIRCLE_MIN+(TIPS_ALPHA_CIRCLE_MAX-TIPS_ALPHA_CIRCLE_MIN)*(timePass-1000)/TIPS_TIME_PRESS_CAST; 
			if(circle_apha>255){
				circle_apha=255;
			}
		}
		
		
		for (int i = 0; i < tipsCellPic.size(); i++) { 
			if(i==1){
				if(select_apha<0xff){
					mPaint.setARGB(0xff ,0xff,0xff,0xff);
					canvas.drawRect(tipsCellPic.get(i).destRect, mPaint);
				}
				
				mPaint.setARGB((int)select_apha,0,0x91,0xea);
				canvas.drawRect(tipsCellPic.get(i).destRect, mPaint);
				
				mPaint.setARGB((int)circle_apha, 0, 0, 0 );
				canvas.drawCircle(tipsCellPic.get(i).destRect.centerX(), tipsCellPic.get(i).destRect.centerY(),
						circle_radius, mPaint);
			}else{
				mPaint.setColor(0xff0091ea);
				canvas.drawRect(tipsCellPic.get(i).destRect, mPaint);
			}
		}
		if(circle_radius>0){
			rectTipsHand_scale.set(
					(int)(tipsCellPic.get(1).destRect.centerX()-rectTipsHand.width()/2*tips_hand_scale/100),
					(int)tipsCellPic.get(1).destRect.centerY()
					, 	(int)(tipsCellPic.get(1).destRect.centerX()+rectTipsHand.width()/2*tips_hand_scale/100),
							(int)(tipsCellPic.get(1).destRect.centerY()+rectTipsHand.height()*tips_hand_scale/100));
			canvas.drawBitmap(imgTipsHand, rectTipsHand, rectTipsHand_scale, null);
		}
	}
	
	private final int TIPS_HAND_SCALE=30;
	
	private final int TIPS_TIME_PRESS_CAST=260;//按下去渐变毫秒数 出现手和圈圈 
	private final int TIPS_TIME_ALPHACHANGE_CAST=160;//长安完成，选择的图片开始变透明变小的时间
	private final int TIPS_TIME_HOLD_CAST=800;//停800毫秒 表示我正在长按中
	private final int TIPS_TIME_MOVE_CAST=200;//长按后 往下拖动的时间
	private final int TIPS_TIME_RELEASE_CAST=260;//拖到目的地后 释放长按 手消失和圈圈消失的渐变时间
	private final int TIPS_SCALE_TIME_CAST=400;//被拖动到目的的图片 释放长按后放大到宽度全屏的时间 
	private final int TIPS_TIME_PRESS=1000+TIPS_TIME_PRESS_CAST,//1秒后 
			TIPS_TIME_HOLD=TIPS_TIME_PRESS+TIPS_TIME_ALPHACHANGE_CAST,
			TIPS_TIME_MOVE=TIPS_TIME_HOLD+TIPS_TIME_HOLD_CAST, 
			TIPS_TIME_MOVE_RELEASE=TIPS_TIME_MOVE+TIPS_TIME_MOVE_CAST,
			TIPS_TIME_SCALE=TIPS_TIME_MOVE_RELEASE+TIPS_TIME_RELEASE_CAST;
	

	private final int TIPS_ALPHA_CIRCLE_MIN=35,
			TIPS_ALPHA_CIRCLE_MAX=161;
	private float circle_radius=0;
	private float circle_apha=0;
	private float select_apha=0xff;
	private float tips_hand_scale=100;
	private float tips_hand_scale_pre=100;
	/*private int  setTipsCellPos(int base,int len ,int time,int maxtime){
		int d= base+len*time/maxtime;
		if(d>base+len){
			
		}
	}*/
	/**
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return isRunning;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRunning){
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			postInvalidate();
		}
	}
	public interface AnimationChangeListener{
		public void animationStoped();
	}
}
