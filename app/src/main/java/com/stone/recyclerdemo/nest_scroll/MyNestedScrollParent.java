package com.stone.recyclerdemo.nest_scroll;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.stone.recyclerdemo.Logs;


//http://blog.csdn.net/lmj121212/article/details/52974427

/**
 * Created By: sqq
 * Created Time: 17/11/6 下午5:43.
 */

public class MyNestedScrollParent extends FrameLayout implements NestedScrollingParent {
    public MyNestedScrollParent(Context context) {
        super(context);
    }

    public MyNestedScrollParent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyNestedScrollParent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private static final String TAG = "MyNestedScrollParent";

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        Logs.i(TAG, "onStartNestedScroll() called with: " + "child = [" + child + "], target = [" + target + "], nestedScrollAxes = [" + nestedScrollAxes + "]");
        return true;
//        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        Logs.i(TAG, "onNestedScrollAccepted() called with: " + "child = [" + child + "], target = [" + target + "], axes = [" + axes + "]");
        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(@NonNull View child) {
        super.onStopNestedScroll(child);
        Logs.i(TAG, "onStopNestedScroll() called with: " + "child = [" + child + "]");
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Logs.i(TAG, "onNestedScroll() called with: " + "target = [" + target + "], dxConsumed = [" + dxConsumed + "], dyConsumed = [" + dyConsumed + "], dxUnconsumed = [" + dxUnconsumed + "], dyUnconsumed = [" + dyUnconsumed + "]");
    }


}
