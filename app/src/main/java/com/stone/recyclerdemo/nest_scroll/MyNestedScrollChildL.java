package com.stone.recyclerdemo.nest_scroll;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.NestedScrollingChild;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created By: sqq
 * Created Time: 17/11/6 下午5:40.
 */

public class MyNestedScrollChildL extends LinearLayout implements NestedScrollingChild {
    public MyNestedScrollChildL(Context context) {
        super(context);
    }

    public MyNestedScrollChildL(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollChildL(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyNestedScrollChildL(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
