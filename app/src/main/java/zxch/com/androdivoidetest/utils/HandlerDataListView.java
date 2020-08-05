package zxch.com.androdivoidetest.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class HandlerDataListView extends ListView {
    public HandlerDataListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HandlerDataListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HandlerDataListView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}