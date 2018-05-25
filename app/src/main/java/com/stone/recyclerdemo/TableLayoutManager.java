package com.stone.recyclerdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created By: sqq
 * Created Time: 16/12/26 下午12:41.
 */
public class TableLayoutManager extends RecyclerView.LayoutManager {

    public TableLayoutManager() {
        //使得RecyclerView 的大小测量方式按照本LayoutManager的方式来计算，一般是为了使得RecyclerView支持WrapContent。
        setAutoMeasureEnabled(true);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {

        return new RecyclerView.LayoutParams(-2, -2);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        detachAndScrapAttachedViews(recycler);

        int left, top, right;
        top = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (i == 0) {
            }

            View child = recycler.getViewForPosition(i);

            child.getLayoutParams().width = getWidth() / 2;

            addView(child);

            measureChildWithMargins(child, 0, 0);

            int width = getDecoratedMeasuredWidth(child);
            int height = getDecoratedMeasuredHeight(child);

            left = i % 2 == 0 ? 0 : width;

            right = left + width;

            layoutDecoratedWithMargins(child, left, top, right, top + height);

            top += i % 2 == 0 ? 0 : height;

        }
    }

    @Override
    public void setAutoMeasureEnabled(boolean enabled) {
        super.setAutoMeasureEnabled(enabled);

    }
}
