package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;

/**
 * 自定义ScrollView
 *
 * @author afmobi
 * @date 2014-09-25
 */
public class ReboundScrollView extends ScrollView {
    /** 阻尼系数,越小阻力就越大. */
    private static final float SCROLL_RATIO = 0.3f;

    /** 滑动至翻转的距离. */
    private static final int TURN_DISTANCE = 100;

    /** 头部view. */
    private View mHeader;

    /** 头部view高度. */
    private int mHeaderHeight;

    /** 头部view显示高度. */
    private int mHeaderVisibleHeight;

    /** ScrollView的content view. */
    private View mContentView;

    /** ScrollView的content view矩形. */
    private Rect mContentRect = new Rect();

    /** 首次点击的Y坐标. */
    private float mTouchDownY;

    /** 是否关闭ScrollView的滑动. */
    private boolean mEnableTouch = false;

    /** 是否开始移动. */
    private boolean isMoving = false;

    /** 是否移动到顶部位置. */
    private boolean isTop = false;

    /** 头部图片初始顶部和底部. */
    private int mInitTop, mInitBottom;

    /** 头部图片拖动时顶部和底部. */
    private int mCurrentTop, mCurrentBottom;

    /** 状态变化时的监听器. */
    private OnTurnListener mOnTurnListener;

    private enum State {
        /**顶部*/
        UP,
        /**底部*/
        DOWN,
        /**正常*/
        NORMAL
    }

    /** 状态. */
    private State mState = State.NORMAL;
    
    public int scrollY = 0;

    public ReboundScrollView(Context context) {
        super(context);
        init(context, null);
    }

    public ReboundScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ReboundScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // set scroll mode
        setOverScrollMode(OVER_SCROLL_NEVER);

        if (null != attrs) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ReboundScrollView);

            if (ta != null) {
                mHeaderHeight = (int) ta.getDimension(R.styleable.ReboundScrollView_headerHeight, -1);
                mHeaderVisibleHeight = (int) ta.getDimension(R.styleable.ReboundScrollView_headerVisibleHeight, -1);
                ta.recycle();
            }
        }
    }

    /**
     * 设置Header
     *
     * @param view
     */
    public void setHeader(View view) {
        mHeader = view;
    }

    /**
     * 设置状态改变时的监听器
     *
     * @param turnListener
     */
    public void setOnTurnListener(OnTurnListener turnListener) {
        mOnTurnListener = turnListener;
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            mContentView = getChildAt(0);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        PalmchatLogUtils.e("onScrollChanged", getScrollY()+"");
        if (getScrollY() <= 0) {
            isTop = true;
        }else {
        	 isTop = false;
		}
        if (mOnTurnListener != null) {
        	mOnTurnListener.onAlpha(t);
        	scrollY = t;
		}
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchDownY = ev.getY();
            mCurrentTop = mInitTop = mHeader.getTop();
            mCurrentBottom = mInitBottom = mHeader.getBottom();
        }
        return super.onInterceptTouchEvent(ev);
    }
    

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mContentView != null) {
            doTouchEvent(ev);
        }

        // 禁止控件本身的滑动.
        return mEnableTouch || super.onTouchEvent(ev);
    }

    /**
     * 触摸事件处理
     *
     * @param event
     */
    private void doTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
            	if (isTop) {
            		mEnableTouch =true;
				}else {
					mEnableTouch =false;
				}
                doActionMove(event);
                break;

            case MotionEvent.ACTION_UP:
                // 回滚动画
                if (isNeedAnimation() && getScrollY()==0) {
                    rollBackAnimation();
                }

                if (getScrollY() == 0 && isTop) {
                	mEnableTouch =true;
                    mState = State.NORMAL;
                }else {
                	mEnableTouch =false;
				}

                isMoving = false;
                doOnBorderListener();
                break;

            default:
                break;
        }
    }

    /**
     * 执行移动动画
     *
     * @param event
     */
    private void doActionMove(MotionEvent event) {
        // 当滚动到顶部时，将状态设置为正常，避免先向上拖动再向下拖动到顶端后首次触摸不响应的问题
        if (getScrollY() == 0) {
            mState = State.NORMAL;
            // 滑动经过顶部初始位置时，修正Touch down的坐标为当前Touch点的坐标
            if (isTop) {
                mTouchDownY = event.getY();
                isTop = false;
            }
        }

        float deltaY = event.getY() - mTouchDownY;
        // 对于首次Touch操作要判断方位：UP OR DOWN
        if (deltaY < 0 && mState == State.NORMAL) {
            mState = State.UP;
        } else if (deltaY > 0 && mState == State.NORMAL) {
            mState = State.DOWN;
        }

        if (mState == State.UP) {
            deltaY = deltaY < 0 ? deltaY : 0;

            isMoving = false;
            mEnableTouch = false;
        } else if (mState == State.DOWN) {
            if (getScrollY() <= deltaY) {
                mEnableTouch = true;
                isMoving = true;
            }
            deltaY = deltaY < 0 ? 0 : deltaY;
        }

        if (isMoving && getScrollY() == 0) {
            // 初始化content view矩形
            if (mContentRect.isEmpty()) {
                // 保存正常的布局位置
                mContentRect.set(mContentView.getLeft(), mContentView.getTop(), mContentView.getRight(),
                        mContentView.getBottom());
            }

            // 计算header移动距离(手势移动的距离*阻尼系数*0.5)
            float headerMoveHeight = deltaY * SCROLL_RATIO * SCROLL_RATIO;
            mCurrentTop = (int) (mInitTop + headerMoveHeight);
//            if (mCurrentBottom <= mInitBottom) {
            	mCurrentBottom = (int) (mInitBottom + headerMoveHeight);
//            }
//			}else {
//				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mInitBottom);
//				params.setMargins(0, mCurrentTop, 0, 0);
//				mHeader.setLayoutParams(params);
//			}
            Log.e("mHeader", "mCurrentTop="+mCurrentTop+"|mCurrentBottom="+mCurrentBottom+"mHeader.H="+mHeader.getHeight());
			

            // 计算content移动距离(手势移动的距离*阻尼系数)
            float contentMoveHeight = deltaY * 0.5f * SCROLL_RATIO;

            // 修正content移动的距离，避免超过header的底边缘
            int headerBottom = mCurrentBottom - mHeaderVisibleHeight;
            int top = (int) (mContentRect.top + contentMoveHeight);
            int bottom = (int) (mContentRect.bottom + contentMoveHeight);

            if (top <= headerBottom ) {
                // 移动content view
                mContentView.layout(mContentRect.left, top, mContentRect.right, bottom);

                // 移动header view
                mHeader.layout(mHeader.getLeft(), mCurrentTop, mHeader.getRight(), mCurrentBottom);
            }
        }
    }

    
    private void rollBackAnimation() {
        TranslateAnimation tranAnim = new TranslateAnimation(0, 0, Math.abs(mInitTop - mCurrentTop), 0);
        tranAnim.setDuration(1000);
        mHeader.startAnimation(tranAnim);

        mHeader.layout(mHeader.getLeft(), mInitTop, mHeader.getRight(), mInitBottom);

        // 开启移动动画
        TranslateAnimation innerAnim = new TranslateAnimation(0, 0, mContentView.getTop(), mContentRect.top);
        innerAnim.setDuration(1000);
        mContentView.startAnimation(innerAnim);
        mContentView.layout(mContentRect.left, mContentRect.top, mContentRect.right, mContentRect.bottom);

        mContentRect.setEmpty();

        // 回调监听器
        if (mCurrentTop > mInitTop + TURN_DISTANCE && mOnTurnListener != null){
//        		mOnTurnListener.onTurn();
        }
    }

    /**
     * 是否需要开启动画
     */
    private boolean isNeedAnimation() {
        return !mContentRect.isEmpty() && isMoving;
    }

    /**
     * 翻转事件监听器
     *
     * @author afmobi
     */
    public interface OnTurnListener {
        
    	/**
         * Called when scroll to bottom
         */
        public void onLoadMore();
     
        /**
         * Called when scroll to top
         */
        public void onRefresh();
    	
        public void onAlpha(float size);
    }
    
    public void setIsTop(boolean istop){
    	this.isTop = istop;
    }
    public boolean getIsTop(){
		return isTop;
    }
    public void setEnableTouch(boolean mEnableTouch){
    	this.mEnableTouch = mEnableTouch;
    }
    
    private void doOnBorderListener() {

    	View contentView = getChildAt(0);
        if (contentView != null && contentView.getMeasuredHeight() <= getScrollY() + getHeight()) {
            if (mOnTurnListener != null) {
            	mOnTurnListener.onLoadMore();
            }
        } else if (getScrollY() == 0) {
            if (mOnTurnListener != null) {
            	mOnTurnListener.onRefresh();
            }
        }
    }
}
