package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.util.AnimationController;
import com.afmobigroup.gphone.R;

/**
 * Created by Transsion on 2016/5/16.
 */
public class SendBroadcastMaskingView extends FrameLayout{

    /**发送广播蒙层*/
    private View mSendbroad_Masking;
    /**发送布局*/
    private View mV_Maintab_Send_layout;
    /**发图片按钮*/
    private ImageButton mIBtn_SendPhoto;
    /**发图片文字*/
    private TextView mTv_SendPhoto;
    /**发声音按钮*/
    private ImageButton mIBtn_SendVoice;
    /**发声音文字*/
    private TextView mTv_SendVoice;
    /**默认值*/
    private final int DEAFULTVALUE=-1;
    /**上下文*/
    private Context mContext;
    /**高的*/
    private final static int TYPE1 = 1;
    /***/
    private final static int TYPE2 = 2;

    /**按钮点击回调*/
    private SendBroadBtnListener mSendBroadBtnListener;

    public SendBroadcastMaskingView(Context context) {
        super(context);
        this.mContext = context;
        init(context, null);
    }

    public SendBroadcastMaskingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context, attrs);
    }

    public SendBroadcastMaskingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init(context, attrs);
    }

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs){
        int type = TYPE1;
        if(attrs!=null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MaskingView);
            type = typedArray.getInt(R.styleable.MaskingView_layouttype,TYPE1);
        }

        View view = null;
        if(type==TYPE1){
            view  = LayoutInflater.from(context).inflate(R.layout.fab_layout,null);
        } else {
            view  = LayoutInflater.from(context).inflate(R.layout.tagpage_fab_layout,null);
        }

        mSendbroad_Masking = view.findViewById(R.id.fabviewlayout_id);
        mV_Maintab_Send_layout = view.findViewById(R.id.maintab_send_layout);

        mIBtn_SendPhoto = (ImageButton)view.findViewById(R.id.maintab_send_photos_id);

        mIBtn_SendPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=mSendBroadBtnListener){
                    mSendBroadBtnListener.sendPhoto();
                }
            }
        });

        mTv_SendPhoto= (TextView)view.findViewById(R.id.maintab_send_photos_text_id);
        mTv_SendPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=mSendBroadBtnListener){
                    mSendBroadBtnListener.sendPhoto();
                }
            }
        });

        mIBtn_SendVoice = (ImageButton)view.findViewById(R.id.maintab_send_voice_id);
        mIBtn_SendVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=mSendBroadBtnListener){
                    mSendBroadBtnListener.sendVoice();
                }
            }
        });
        //文字部分
        mTv_SendVoice = (TextView)view.findViewById(R.id.maintab_send_voice_text_id);
        mTv_SendVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=mSendBroadBtnListener){
                    mSendBroadBtnListener.sendVoice();
                }
            }
        });
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setVisibility(GONE);
                mV_Maintab_Send_layout.setVisibility(INVISIBLE);
                if(null!=mSendBroadBtnListener){
                    mSendBroadBtnListener.dismiss();
                }
                return true;
            }
        });
        addView(view);

    }

    /**
     * 设置回调监听
     * @param sendBroadBtnListener
     */
    public void setSendBroadBtnListener(SendBroadBtnListener sendBroadBtnListener) {
        this.mSendBroadBtnListener = sendBroadBtnListener;
    }

    /**
     * 显示布局  由于第一次布局需要时间，所以多了 postDelayed上面的代码
     */
    public void show(Handler handler){
        mTv_SendVoice.setVisibility(View.INVISIBLE);
        mIBtn_SendVoice.setVisibility(View.INVISIBLE);
        mTv_SendPhoto.setVisibility(View.INVISIBLE);
        mIBtn_SendPhoto.setVisibility(View.INVISIBLE);
        mV_Maintab_Send_layout.setVisibility(View.VISIBLE);
        setVisibility(VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mV_Maintab_Send_layout.setVisibility(View.VISIBLE);
                mTv_SendVoice.setVisibility(View.VISIBLE);
                mIBtn_SendVoice.setVisibility(View.VISIBLE);
                mTv_SendPhoto.setVisibility(View.VISIBLE);
                mIBtn_SendPhoto.setVisibility(View.VISIBLE);
                AnimationController.createRotateAnimation(DefaultValueConstant.DURATION_ANIMATION_200,mIBtn_SendPhoto).start();
                AnimationController.createAlphaAnimation(DefaultValueConstant.DURATION_ANIMATION_500,mIBtn_SendPhoto,0.0F,1.0F).start();
                //mTv_SendPhoto
                AnimationController.createAlphaAnimation(DefaultValueConstant.DURATION_ANIMATION_500,mTv_SendPhoto,0.0F,1.0F).start();
                //mTv_SendVoice
                AnimationController.createTranslationYAnimation(DefaultValueConstant.DURATION_ANIMATION_200,mTv_SendVoice,mTv_SendPhoto.getY(),mTv_SendVoice.getY()).start();
                AnimationController.createAlphaAnimation(DefaultValueConstant.DURATION_ANIMATION_500,mTv_SendVoice,0.0F,1.0F).start();
                AnimationController.createScaleAnimation(DefaultValueConstant.DURATION_ANIMATION_200,mTv_SendVoice,0.0F,1.0F,View.SCALE_X).start();
                AnimationController.createScaleAnimation(DefaultValueConstant.DURATION_ANIMATION_200,mTv_SendVoice,0.0F,1.0F,View.SCALE_Y).start();
                //mIBtn_SendVoice
                AnimationController.createTranslationYAnimation(DefaultValueConstant.DURATION_ANIMATION_200,mIBtn_SendVoice,mIBtn_SendPhoto.getY(),mIBtn_SendVoice.getY()).start();
                AnimationController.createAlphaAnimation(DefaultValueConstant.DURATION_ANIMATION_500,mIBtn_SendVoice,0.0F,1.0F).start();
                AnimationController.createScaleAnimation(DefaultValueConstant.DURATION_ANIMATION_200,mIBtn_SendVoice,0.0F,1.0F,View.SCALE_Y).start();
                AnimationController.createScaleAnimation(DefaultValueConstant.DURATION_ANIMATION_200,mIBtn_SendVoice,0.0F,1.0F,View.SCALE_X).start();
            }
        },30);
    }

    /**
     * 隐藏
     */
    public void dismiss(){
        setVisibility(View.GONE);
    }
}
