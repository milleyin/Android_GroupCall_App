package com.afmobi.palmchat.ui.customview;

import android.content.Context;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
/**
 * 之所以加这个类是以因为要处理广播的文字内容中有部分是tag需要点击进TagPage，另外一部分文字点击又是进广播详情的，所以加入这个类来处理这个问题
 * @author xiaolong
 *
 */
public class TextViewFixTouchConsume extends TextView {

    boolean dontConsumeNonUrlClicks = true;
    boolean linkHit;

    public TextViewFixTouchConsume(Context context) {
        super(context);
    }

    public TextViewFixTouchConsume(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewFixTouchConsume(
        Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        linkHit = false;
//        boolean res = super.onTouchEvent(event);
//
//        if (dontConsumeNonUrlClicks)
//            return linkHit;
//        return res;
//
//    }

//    public void setTextViewHTML(String html)
//    {
//        CharSequence sequence = Html.fromHtml(html);
//        SpannableStringBuilder strBuilder = 
//            new SpannableStringBuilder(sequence);
//        setText(strBuilder);
//    }

    public static class LocalLinkMovementMethod extends LinkMovementMethod{
        static LocalLinkMovementMethod sInstance;


        public static LocalLinkMovementMethod getInstance() {
            if (sInstance == null)
                sInstance = new LocalLinkMovementMethod();

            return sInstance;
        }
        
        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        	return super.onTouchEvent(widget, buffer, event);
        }
//
//        @Override
//        public boolean onTouchEvent(TextView widget, 
//            Spannable buffer, MotionEvent event) {
//            int action = event.getAction();
//
//            if (action == MotionEvent.ACTION_UP ||
//                    action == MotionEvent.ACTION_DOWN) {
//                int x = (int) event.getX();
//                int y = (int) event.getY();
//
//                x -= widget.getTotalPaddingLeft();
//                y -= widget.getTotalPaddingTop();
//
//                x += widget.getScrollX();
//                y += widget.getScrollY();
//
//                Layout layout = widget.getLayout();
//                int line = layout.getLineForVertical(y);
//                int off = layout.getOffsetForHorizontal(line, x);
//
//                ClickableSpan[] link = buffer.getSpans(
//                    off, off, ClickableSpan.class);
//
//                if (link.length != 0) {
//                    if (action == MotionEvent.ACTION_UP) {
//                        link[0].onClick(widget);
//                    } else if (action == MotionEvent.ACTION_DOWN) {
//                        Selection.setSelection(buffer,
//                                buffer.getSpanStart(link[0]),
//                                buffer.getSpanEnd(link[0]));
//                    }
//
//                    if (widget instanceof TextViewFixTouchConsume){
//                        ((TextViewFixTouchConsume) widget).linkHit = true;
//                    }
//                    return true;
//                } else {
//                    Selection.removeSelection(buffer);
//                    Touch.onTouchEvent(widget, buffer, event);
//                    return false;
//                }
//            }
//            return Touch.onTouchEvent(widget, buffer, event);
//        }
     }
}