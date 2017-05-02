package com.afmobi.palmchat.ui.customview;

/**
 * Created by hp on 2016/8/17.
 */

    import android.graphics.RectF;
    import android.view.View;
    import android.content.Context;
    import android.graphics.Canvas;
    import android.graphics.Color;
    import android.graphics.Paint;
    import android.graphics.PorterDuff;
    import android.util.AttributeSet;
    import android.util.Log;
    import android.view.MotionEvent;

    import com.afmobigroup.gphone.R;

    import java.util.LinkedList;

public  class SimpleWaveView extends View {
    Context context;

    public final static int MODE_AMP_ORIGIN = 1;
    public final static int MODE_AMP_ABSOLUTE = 2;
    public int modeAmp;
    public final static int MODE_HEIGHT_PX = 1;
    public final static int MODE_HEIGHT_PERCENT = 2;
    public int modeHeight;
    public final static int MODE_ZERO_TOP = 1;
    public final static int MODE_ZERO_CENTER = 2;
    public final static int MODE_ZERO_BOTTOM = 3;
    public int modeZero;
    public final static int MODE_PEAK_ORIGIN = 1;
    public final static int MODE_PEAK_PARALLEL = 2;
    public final static int MODE_PEAK_CROSS_TOP_BOTTOM = 3;
    public final static int MODE_PEAK_CROSS_BOTTOM_TOP = 4;
    public final static int MODE_PEAK_CROSS_TURN_TOP_BOTTOM = 5;
    public int modePeak;
    public final static int MODE_DIRECTION_LEFT_RIGHT = 1;
    public final static int MODE_DIRECTION_RIGHT_LEFT = 2;
    public int modeDirection;

    private int barUnitSize;
    private int peakUnitSize;

//    public boolean showPeak;
    public boolean showBar;
    public boolean showXAxis;

    public int height;
    public int width;
    public boolean haveGotWidthHeight = false;
    //    public int barWidth = 10;
    public int barGap;

    public LinkedList<Integer> dataList;
    private LinkedList<BarPoints> innerDataList = new LinkedList<BarPoints>();

    class BarPoints {
        int amplitude;//input data
        int amplitudePx;//in px
        int amplitudePxTop;//top point in px
        int amplitudePxBottom;//bottom point in px
        int amplitudePxTopCanvas;//top point in canvas, lookout that y-axis is down orientation
        int amplitudePxBottomCanvas;//bottom point in canvas
        int xOffset;//position in x axis

        public BarPoints(int amplitude) {
            this.amplitude = amplitude;
        }
    }


    public LinkedList<BarPoints> BarPointsdataList;

    public Paint barPencilFirst = new Paint();
    public Paint barPencilSecond = new Paint();
    public int firstPartNum;//position to divide the first part and the second part

    private float[] barPoints;
    private int barNum;

    private float[] xAxisPoints;
    public Paint xAxisPencil = new Paint();

    public boolean clearScreen = false;

    public interface ClearScreenListener {
        void clearScreen(Canvas canvas);
    }

    public ClearScreenListener clearScreenListener;

    public interface ProgressTouch {
        void progressTouch(int progress, MotionEvent event);
    }

    public ProgressTouch progressTouch;


    public SimpleWaveView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SimpleWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void getWidthLength() {

        post(new Runnable() {
            @Override
            public void run() {
                width = SimpleWaveView.this.getWidth();
                height = SimpleWaveView.this.getHeight();
                haveGotWidthHeight = true;
                SimpleWaveView.this.invalidate();
            }
        });


    }

    public void init() {
        width = this.getWidth();
        height = this.getHeight();
        Log.d("","SimpleWaveform: w,h: " + width +" "+height);
        if (width > 0 && height > 0) {
            haveGotWidthHeight = true;
        }else{
            haveGotWidthHeight = false;
        }
        if (!haveGotWidthHeight) {
            getWidthLength();
        }
        barGap =20;
        firstPartNum = 0;
        modeAmp = MODE_AMP_ABSOLUTE;
        modeHeight = MODE_HEIGHT_PERCENT;
        modeZero = MODE_ZERO_CENTER;
        modePeak = MODE_PEAK_CROSS_TOP_BOTTOM;
        showBar = true;
        showXAxis = false;
        xAxisPencil.setStrokeWidth(1);
        xAxisPencil.setColor(0x88ffffff);
        dataList = null;
        clearScreenListener = null;

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (progressTouch != null) {
                    if (barGap != 0) {
                        if (modeDirection == MODE_DIRECTION_LEFT_RIGHT) {
                            progressTouch.progressTouch((int) (event.getX() / barGap) + 1, event);
                        } else {
                            progressTouch.progressTouch((int) ((width - event.getX()) / barGap) + 1, event);
                        }
                    }
                }
                return true;
            }

        });
    }

    public void setDataList(LinkedList<Integer> ampList) {
        this.dataList = ampList;
    }

    public void refresh() {
        this.invalidate();
    }


    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("", "normal view: onDraw()");
        if (clearScreenListener != null) {
            clearScreenListener.clearScreen(canvas);
        } else {
            this.setBackgroundColor(getResources().getColor(R.color.brdcast_voice_blue));
        }
        if (clearScreen) {
            clearScreen = false;
            return;
        }
        drawWaveList(canvas);

    }

    private void drawWaveList(Canvas canvas) {
        if (!haveGotWidthHeight) {
            Log.d("","SimpleWaveform: drawWaveList() return for no width and height");
            return;
        }
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        innerDataList.clear();
        barNum = (width / barGap) + 2;
        if (barNum > dataList.size()) {
            barNum = dataList.size();
        }
        if (showBar) {
            barUnitSize = 4;
            this.barPoints = new float[barNum * barUnitSize];
        }
        for (int i = 0; i < barNum; i++) {
            BarPoints barPoints = new BarPoints(dataList.get(i));
            if (modeDirection == MODE_DIRECTION_LEFT_RIGHT) {
                barPoints.xOffset = i * barGap;
            }
            if (modeHeight == MODE_HEIGHT_PERCENT) {
                barPoints.amplitudePx = (barPoints.amplitude * height) / 50;
            }
            if (modeAmp == MODE_AMP_ABSOLUTE) {
                barPoints.amplitudePxTop = Math.abs(barPoints.amplitudePx);
                barPoints.amplitudePxBottom = -Math.abs(barPoints.amplitudePx);
            }
            switch (modeZero) {
                case MODE_ZERO_CENTER:
                    barPoints.amplitudePxTopCanvas = -barPoints.amplitudePxTop + height / 2;
                    barPoints.amplitudePxBottomCanvas = -barPoints.amplitudePxBottom + height / 2;
                    break;
            }
            //now we get the data to show
            innerDataList.addLast(barPoints);
            if (showBar) {
                this.barPoints[i * barUnitSize] = barPoints.xOffset;
                this.barPoints[i * barUnitSize + 1] = barPoints.amplitudePxTopCanvas;
                this.barPoints[i * barUnitSize + 2] = barPoints.xOffset;
                this.barPoints[i * barUnitSize + 3] = barPoints.amplitudePxBottomCanvas;
            }
        }
        /**
         * draw
         */
        if (firstPartNum > barNum) {
            firstPartNum = barNum;
        }
        if (showBar) {
            for (int i = 0; i < innerDataList.size(); i++)
            {
                BarPoints myBarPoints=(BarPoints)innerDataList.get(i);
                RectF _RectF=new RectF();
                _RectF.left=myBarPoints.xOffset;
                _RectF.top=myBarPoints.amplitudePxTopCanvas;
                _RectF.right=myBarPoints.xOffset+10;
                _RectF.bottom=myBarPoints.amplitudePxBottomCanvas;
                canvas.drawRoundRect(_RectF, 6, 6,barPencilFirst);
            }
        }
    }

}