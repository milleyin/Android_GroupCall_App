package com.afmobi.palmchat.ui.customview;

import java.util.Iterator;
import java.util.LinkedList;

import com.afmobi.palmchat.util.ImageUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 线性自动换行布局
 * @author gtf
 */
public class FlowLayout extends ViewGroup{
    private boolean adjustChildWidthWithParent;
    
    public int width = 0;
    public int height = 0;

    public FlowLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    
    public FlowLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        width = widthMeasureSpec;
        height = heightMeasureSpec;
       
        measure(widthMeasureSpec, heightMeasureSpec, widthMode, widthSize, heightMode, heightSize);
    }

    private void measure(int widthMeasureSpec, int heightMeasureSpec, int widthMode, int widthSize, int heightMode, int heightSize){
        int availableWidth = widthSize - getPaddingLeft() - getPaddingRight();   // 可用宽度
        int parentViewHeight = 0; // 实际需要的高度
        int rowWidth = 0;  // 记录行宽
        int rowHeight = 0;  // 记录行高
        int childViewWidth; // 记录子View宽度
        int childViewHeight;    // 记录子View高度
        View childView;
        LinkedList<View> rowViews = new LinkedList<View>();
        boolean widthSizeUnspecified = widthMode == MeasureSpec.UNSPECIFIED;
        int  count= getChildCount();
        for (int position = 0; position <count; position++) {
            childView = getChildAt(position);
            if(childView.getVisibility() == View.GONE) continue;

            // 测量并计算当前View需要的宽高
            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            LayoutParams lp = (LayoutParams) childView.getLayoutParams();
            childViewWidth = lp.leftMargin+childView.getMeasuredWidth()+lp.rightMargin;
            childViewHeight = lp.topMargin+childView.getMeasuredHeight()+lp.bottomMargin;

            if(count==1){ 
             	if(childViewWidth*4/3 <childViewHeight){
             		childViewHeight=childViewWidth*4/3;
             	} 
             }
            // 如果宽度方面是包括，那么就记录总宽度，否则就换行并调整子Vie的宽度
            if(widthSizeUnspecified){
                rowWidth += childViewWidth;
                // 更新行高
                if(childViewHeight > parentViewHeight){
                    parentViewHeight = childViewHeight;
                }
            }else{
                // 如果宽度方面加上当前View就超过了可用宽度
                if(rowWidth + childViewWidth > availableWidth){
                    // 就调整之前的View的宽度以充满
                    if(adjustChildWidthWithParent){
                        adjustChildWidthWithParent(rowViews, availableWidth, widthMeasureSpec, heightMeasureSpec);
                    }
                    rowViews.clear();
                    rowWidth = 0;   //  清空行宽
                    parentViewHeight += rowHeight;    // 增加View高度
                    rowHeight = 0;  // 清空行高
                }

                rowViews.add(childView);
                rowWidth += childViewWidth;  // 增加行宽

                // 更新行高
                if(childViewHeight > rowHeight){
                    rowHeight = childViewHeight;
                }
            }
        }

        if(!widthSizeUnspecified && !rowViews.isEmpty()){
            // 就调整剩余的View的宽度以充满
            if(adjustChildWidthWithParent){
                adjustChildWidthWithParent(rowViews, availableWidth, widthMeasureSpec, heightMeasureSpec);
            }
            rowViews.clear();
            parentViewHeight += rowHeight;    // 增加View高度
        }

        int finalWidth = 0;
        int finalHeight = 0;
        switch (widthMode){
            case MeasureSpec.EXACTLY :
            case MeasureSpec.AT_MOST:
                finalWidth = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                finalWidth = rowWidth + getPaddingLeft() + getPaddingRight();
                break;
        }
        switch (heightMode){
            case MeasureSpec.EXACTLY :
                finalHeight = heightSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                finalHeight = parentViewHeight + getPaddingTop() + getPaddingBottom();
                break;
        }
//        setMeasuredDimension(finalWidth, finalHeight);
       
        int[]rowArr=picRule;//ViewEditBroadcastPicture.map[count-1];
        int startI=0;
		int y=0;
		int countR=0; 
		boolean isLastRow=false;
		for(int i=0,j=0;i<count;i++){
			if(countR>=rowArr[ j]-1){ 
				if(j==rowArr.length-1){//如果是最后一行了
					isLastRow=true;
				}
				y=setOneRowPos(availableWidth,startI,i,y,rowArr[ j],j,true,isLastRow);//设置单行图片排列
				startI=i+1;
				countR=0;
				j++;
			}else{
				countR++;
			}
		}
        setMeasuredDimension(widthSize//ImageUtil.DISPLAYW
        		, y);
    }

    /**
     * 调整views集合中的View，让所有View的宽度加起来正好等于parentViewWidth
     * @param views 子View集合
     * @param parentViewWidth 父Vie的宽度
     * @param parentWidthMeasureSpec 父View的宽度规则
     * @param parentHeightMeasureSpec 父View的高度规则
     */
    private void adjustChildWidthWithParent(LinkedList<View> views, int parentViewWidth, int parentWidthMeasureSpec, int parentHeightMeasureSpec){
        // 先去掉所有子View的外边距
        for(View view : views){
            if(view.getLayoutParams() instanceof MarginLayoutParams){
                MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
                parentViewWidth -= lp.leftMargin + lp.rightMargin;
            }
        }

        // 去掉宽度大于平均宽度的View后再次计算平均宽度
        int averageWidth = parentViewWidth /views.size();
        int bigTabCount = views.size();
        while(true){
            Iterator<View> iterator = views.iterator();
            while(iterator.hasNext()){
                View view = iterator.next();
                if(view.getMeasuredWidth() > averageWidth){
                    parentViewWidth -= view.getMeasuredWidth();
                    bigTabCount--;
                    iterator.remove();
                }
            }
            averageWidth = parentViewWidth /bigTabCount;
            boolean end = true;
            for(View view : views){
                if(view.getMeasuredWidth() > averageWidth){
                    end = false;
                }
            }
            if(end){
                break;
            }
        }

        // 修改宽度小于新的平均宽度的View的宽度
        for(View view : views){
            if(view.getMeasuredWidth() < averageWidth){
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = averageWidth;
                view.setLayoutParams(layoutParams);
                // 再次测量让新宽度生效
                if(layoutParams instanceof MarginLayoutParams){
                    measureChildWithMargins(view, parentWidthMeasureSpec, 0, parentHeightMeasureSpec, 0);
                }else{
                    measureChild(view, parentWidthMeasureSpec, parentHeightMeasureSpec);
                }
            }
        }
    }
    private int[] picRule;
    public void setPicRule(int []rule){
    	picRule=rule;
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int viewWidth = r - l;
       /* int leftOffset = getPaddingLeft();
        int topOffset = getPaddingTop();
        int rowMaxHeight = 0;
        View childView;*/
        int count = getChildCount();
        int[]rowArr=picRule;//ViewEditBroadcastPicture.map[count-1];
        
        
        
        int startI=0;
		int y=0;
		int countR=0; 
		boolean isLastRow=false;
		for(int i=0,j=0;i<count;i++){
			if(countR>=rowArr[ j]-1){
				if(j==rowArr.length-1){//如果是最后一行了
					isLastRow=true;
				}
				y=setOneRowPos(viewWidth,startI,i,y,rowArr[ j],j,true,isLastRow);//设置单行图片排列
				startI=i+1;
				countR=0;
				j++;
			}else{
				countR++;
			}
		}
		 
       /* for( int w = 0; w < count; w++ ){
            childView = getChildAt(w);
            LayoutParams lp = (LayoutParams) childView.getLayoutParams();

            // 如果加上当前子View的宽度后超过了ViewGroup的宽度，就换行
            int occupyWidth = lp.leftMargin + childView.getMeasuredWidth() + lp.rightMargin;
            if(leftOffset + occupyWidth + getPaddingRight() > viewWidth){
                leftOffset = getPaddingLeft();  // 回到最左边
                topOffset += rowMaxHeight;  // 换行
                rowMaxHeight = 0;
            }

            int left = leftOffset + lp.leftMargin;
            int top = topOffset + lp.topMargin;
            int right = leftOffset+ lp.leftMargin + childView.getMeasuredWidth();
            int bottom =  topOffset + lp.topMargin + childView.getMeasuredHeight();
             
            childView.layout(left, top, right, bottom);

            // 横向偏移
            leftOffset += occupyWidth;

            // 试图更新本行最高View的高度
            int occupyHeight = lp.topMargin + childView.getMeasuredHeight() + lp.bottomMargin;
            if(occupyHeight > rowMaxHeight){
                rowMaxHeight = occupyHeight;
            }
        }*/
    }

    
    /**
	 * 设置一行中所有图片的坐标
	 * @param start_i
	 * @param end_i
	 * @param y
	 * @param colCount
	 * @return 返回排了这列后 下一列的起始Y卓标
	 */
	private int setOneRowPos(int width,int start_i,int end_i,int y,int colCount,int rowIndex,boolean isLayout,boolean isLastRow){
		int cw=width/colCount;
		  
		int minH=0;//最小高
		for(int i=start_i;i<=end_i;i++){
//			 LayoutParams lp = (LayoutParams) getChildAt(i).getLayoutParams();
			int _h= getChildAt(i).getMeasuredHeight()*cw/ getChildAt(i).getMeasuredWidth();
			  if(minH==0||minH>_h){
				minH=_h;
			}
		} 
		if(minH>cw*4/3){//超长图 限制高度
			minH=cw*4/3;
		}
		if(minH<cw/3){//超宽图 加高高度
			minH=cw/3;
		}
//		CellPic cellPic=null;
		if(isLayout){
			for(int i=start_i,j=0;i<=end_i;i++,j++){
				/*cellPic= picArr.get(i);
				cellPic.destRect.left =j*cw;
				cellPic.destRect.top =y;
				cellPic.destRect.right =j*cw+cw;
				cellPic.destRect.bottom =y+minH;
				cellPic.rowIndex=  rowIndex ;
				cellPic.colIndex=j;
				cellPic.zoomType=0;
				int _clipH=(cellPic.srcRect.right )*minH/cw;
				picArr.get(i).srcRect_clip.left=cellPic.srcRect.left;
				picArr.get(i).srcRect_clip.right=cellPic.srcRect.right;
				picArr.get(i).srcRect_clip.top= cellPic.srcRect.bottom-_clipH>>1;
				picArr.get(i).srcRect_clip.bottom= picArr.get(i).srcRect_clip.top+_clipH ;
				*/
				 LayoutParams lp = (LayoutParams) getChildAt(i).getLayoutParams();
				  int left = j*cw + lp.leftMargin;
		            int top = y + lp.topMargin;
		            int right = j*cw+cw- (i>=end_i?0:lp.rightMargin)  ;//如果是最后一列就不用加间距了
		            int bottom =  y+minH - lp.bottomMargin  ;
		            if(isLastRow){//最后一行不用加间距的
		            	bottom =  y+minH;
		            }
		            getChildAt(i).layout(left, top, right, bottom);
			} 
		}
		return y+minH;
	}
	 
    /**
     * Returns a set of layout parameters with a width of
     * {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT},
     * and a height of {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT}.
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /**
     * 设置当一行的子View宽度没有充满LineWrapLayout时是否调整这一行所有子View的宽度以充满LineWrapLayout
     * @param adjustChildWidthWithParent 当一行的子View宽度没有充满LineWrapLayout时是否调整这一行所有子View的宽度以充满LineWrapLayout
     */
    public void setAdjustChildWidthWithParent(boolean adjustChildWidthWithParent) {
        this.adjustChildWidthWithParent = adjustChildWidthWithParent;
        requestLayout();
    }

    public static class LayoutParams extends MarginLayoutParams {

        /**
         * {@inheritDoc}
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int width, int height) {
            super(width, height);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        /**
         * Copy constructor. Clones the width, height, margin values, and
         * gravity of the source.
         *
         * @param source The layout params to copy from.
         */
        public LayoutParams(LayoutParams source) {
            super(source);
        }
    }
    
    public int get_Width(){
		return width;
    }
    public int get_Height(){
    	return height;
    }
}