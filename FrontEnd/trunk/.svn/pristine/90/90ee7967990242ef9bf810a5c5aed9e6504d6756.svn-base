package com.afmobi.palmchat.ui.customview;

        import android.content.Context;
        import android.support.v4.view.ViewPager;
        import android.util.AttributeSet;
        import android.view.MotionEvent;

public class BannerViewPager extends ViewPager {

    // private float mLastMotionX = 0;

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerViewPager(Context context) {
        super(context);
    }

    // @Override
    // public boolean onInterceptTouchEvent(MotionEvent arg0) {
    //
    // // TODO Auto-generated method stub
    //
    // // 当拦截触摸事件到达此位置的时候，返回true，
    //
    // // 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
    //
    // return true;
    // }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        boolean actionPost = true;
        switch (arg0.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // mLastMotionX = arg0.getX();
                // int id = getCurrentItem();
                // if (id != 0) {
                // super.requestDisallowInterceptTouchEvent(true);
                // } else {
                // super.requestDisallowInterceptTouchEvent(false);
                // }
                super.requestDisallowInterceptTouchEvent(true);

                break;

            case MotionEvent.ACTION_MOVE:
                // int id1 = getCurrentItem();
                // if ((arg0.getX() - mLastMotionX) > 0 && id1 > 0) {
                // super.requestDisallowInterceptTouchEvent(false);
                // actionPost = true;
                // }
                super.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                super.requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }

        return super.onTouchEvent(arg0) || actionPost;
    }
}
