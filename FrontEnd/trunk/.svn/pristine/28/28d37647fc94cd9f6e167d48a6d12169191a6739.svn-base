package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.afmobigroup.gphone.R;

public class CollapsibleTextView extends LinearLayout {

    /**
     * default text show max lines
     */
    private static int DEFAULT_MAX_LINE_COUNT = 50;

    private static final int COLLAPSIBLE_STATE_NONE = 0;
    private static final int COLLAPSIBLE_STATE_SHRINKUP = 1;
    private static final int COLLAPSIBLE_STATE_SPREAD = 2;
    private int left_img = 0;
    private int top_img = 0;
    private int right_img = 0;
    private int bottom_img = 0;
    private int drawPadding;
    private TextView desc;
    private TextView tv_top;
    private TextView descOp;

    private int mState;
    private boolean flag;

    public CollapsibleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.collapsible_textview, this);
        view.setPadding(0, -1, 0, 0);
        desc = (TextView) view.findViewById(R.id.desc_tv);
        tv_top = (TextView) view.findViewById(R.id.tv_top);
        descOp = (TextView) view.findViewById(R.id.desc_op_tv);
    }

    public CollapsibleTextView(Context context) {
        this(context, null);
    }

    public CollapsibleTextView(Context context, int maxlines) {
        this(context, null);

    }


    public final void setDesc(CharSequence charSequence, BufferType bufferType) {
        desc.setCompoundDrawablesWithIntrinsicBounds(left_img, top_img, right_img, bottom_img);
        desc.setCompoundDrawablePadding(drawPadding);
        tv_top.setVisibility(View.GONE);
        desc.setText(charSequence, bufferType);
        desc.setBackgroundResource(R.drawable.comment_uilist);
        desc.setMovementMethod(LinkMovementMethod.getInstance()); //实现文本点击事件
        mState = COLLAPSIBLE_STATE_SPREAD;
    }
    public void setSize(int size) {
        desc.setTextSize(size);
    }

    public final void setDesc(CharSequence name, CharSequence charSequence, BufferType bufferType) {
        tv_top.setVisibility(View.VISIBLE);
        tv_top.setText(name, bufferType);
        desc.setCompoundDrawablesWithIntrinsicBounds(left_img, top_img, right_img, bottom_img);
        desc.setText(charSequence, bufferType);
        mState = COLLAPSIBLE_STATE_SPREAD;
    }

    /**
     * zhh 添加点击事件
     *
     * @param ls
     */
    public void addOnClickListener(OnClickListener ls) {
        tv_top.setOnClickListener(ls);
        desc.setOnClickListener(ls);
        tv_top.setBackgroundResource(R.drawable.comment_uilist);
        desc.setBackgroundResource(R.drawable.comment_uilist);
    }

    /**
     * 设置超链接可以点击
     *
     * @param tv
     */
    public final void setLink(final TextView tv) {
        if (tv != null && tv.getText().length() > 0) {
            tv.setAutoLinkMask(Linkify.WEB_URLS);// 超链接起作用
            tv.setMovementMethod(LinkMovementMethod.getInstance());// 超链接起作用
        }
    }


    /**
     * 获取字符串
     *
     * @return
     */
    public String getDesc() {
        String str = "";
        if (!tv_top.getText().toString().trim().equals("")) {
            str = tv_top.getText().toString() + desc.getText().toString();
        } else {
            str = desc.getText().toString();
        }
        return str;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!flag) {
            flag = true;
            if (desc.getLineCount() <= DEFAULT_MAX_LINE_COUNT) {
                mState = COLLAPSIBLE_STATE_NONE;
                descOp.setVisibility(View.GONE);
                desc.setMaxLines(DEFAULT_MAX_LINE_COUNT + 1);
            } else {
//				post(new InnerRunnable());
            }
        }
    }


    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom,int padding) {
        this.left_img = left;
        this.top_img = top;
        this.right_img = right;
        this.bottom_img = bottom;
        this.drawPadding=padding;
    }

    public void setMaxLines(int maxlines) {
        DEFAULT_MAX_LINE_COUNT = maxlines;
    }

    public int getLength() {
        return desc.getText().toString().trim().length();
    }

    public TextView getDescTextView() {
        // TODO Auto-generated method stub
        return desc;
    }

}
