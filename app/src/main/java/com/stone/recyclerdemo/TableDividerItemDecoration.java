package com.stone.recyclerdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created By: sqq
 * Created Time: 16/12/26 下午3:54.
 */

public class TableDividerItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * divider大小
     */
    private final int mSize;
    private final Drawable mDivider;

    public TableDividerItemDecoration(int mSize, @ColorInt int color, Context ctx) {
        this.mSize = mSize;
        this.mDivider = new ColorDrawable(color);
    }

    /**
     * 该方法会在绘制 item 之前调用，绘制范围是 RecyclerView 范围内的任意位置，不局限在 item 中。
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        c.save();
        int left, right, top, bottom;


        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int position = params.getViewLayoutPosition();

            if (position % 2 == 1) {
                right = child.getLeft() + params.leftMargin;
                left = child.getLeft() - mSize;
                top = child.getTop() + params.topMargin;
                bottom = child.getHeight() + top;
            } else {
                if (position == 0) {
                    left = top = right = bottom = 0;
                } else {
                    left = parent.getPaddingLeft();
                    right = parent.getWidth() - parent.getPaddingRight();
                    bottom = child.getTop() + params.topMargin;
                    top = bottom + mSize;
                }
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }

        c.restore();
    }

    /**
     * 该方法会在 item 绘制完之后调用，绘制在最上层。使用方法和 onDraw() 相同。
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
       /* c.save();
        int left, right, top, bottom;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int position = params.getViewLayoutPosition();

            if (position % 2 == 1) {
                right = child.getLeft() + params.leftMargin;
                left = child.getLeft() - mSize;
                top = child.getTop() + params.topMargin;
                bottom = child.getHeight() + top;
            } else {
                if (position == 0) {
                    left = top = right = bottom = 0;
                } else {
                    left = parent.getPaddingLeft();
                    right = parent.getWidth() - parent.getPaddingRight();
                    bottom = child.getTop() + params.topMargin;
                    top = bottom + mSize;
                }
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }

        c.restore();*/
    }

    /**
     * 简单说就是为item添加四个边的divider的宽度，最终item会以pading的形式加到item的上面
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
//        outRect.set(mSize, 0, 0, mSize);
        Log.i(TAG, "getItemOffsets: position = " + position);
        if (position == 0) {
            outRect.set(0, 0, 0, 0);
        } else if (position == 1) {
            outRect.set(mSize, 0, 0, 0);
        } else if (position % 2 == 0) {
            outRect.set(0, mSize, 0, 0);
        } else {
            outRect.set(mSize, mSize, 0, 0);
        }
    }

    private static final String TAG = "TableDividerItemDecorat";
}
