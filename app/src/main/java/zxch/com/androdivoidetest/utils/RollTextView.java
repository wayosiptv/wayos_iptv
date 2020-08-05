package zxch.com.androdivoidetest.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/5/12 0012.
 */

@SuppressLint("AppCompatCustomView")
public class RollTextView extends TextView {
    public RollTextView(Context context) {

//一个参数的构造函数是要调用两个参数的构造函数的,看源码就知道,所以这里要改成this
//		super(context);
        this(context, null);
    }

    public RollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);

    }

    /*
      是否有焦点
     */
    @Override
    public boolean isFocused() {
        return true;
    }

    /*
    当我的焦点发生改变时,调用此方法
    boolean focused 代表当前的焦点状态
    如果有焦点(focused),走父类的回调,如果失去焦点,就不走父类,自己处理
     */
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused)
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    /*
    窗体焦点发生改变,回调此方法
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus)
            super.onWindowFocusChanged(hasFocus);
    }
}
