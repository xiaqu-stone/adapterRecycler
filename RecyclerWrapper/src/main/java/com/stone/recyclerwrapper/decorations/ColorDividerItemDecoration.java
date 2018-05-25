package com.stone.recyclerwrapper.decorations;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created By: sqq
 * Created Time: 17/4/5 下午5:01.
 * <p>
 * <p>
 * 可以直接设置颜色的DividerItemDecoration(ColorInt/ColorRes/RGB。。。)
 */
public class ColorDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    @DecorationOrientation
    private int mOrientation;
    @SuppressWarnings("WeakerAccess")
    protected int dividerSize;

    private final Rect mBounds = new Rect();

    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     * {@link LinearLayoutManager}.
     *
     * @param orientation {@link DecorationOrientation}
     * @param color       divider的颜色
     * @param dividerSize divider的height or width
     */
    @SuppressWarnings("WeakerAccess")
    public ColorDividerItemDecoration(@DecorationOrientation int orientation, @ColorInt int color, int dividerSize) {
        setOrientation(orientation);
        mDivider = new ColorDrawable(color);
        this.dividerSize = dividerSize;
    }

    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     * {@link LinearLayoutManager}.
     * <p>
     * 默认是VERTICAL
     *
     * @param color       divider的颜色
     * @param dividerSize divider的height
     */
    public ColorDividerItemDecoration(@ColorInt int color, int dividerSize) {
        this(DecorationOrientation.TOP, color, dividerSize);
    }

    /**
     * Sets the orientation for this divider. This should be called if
     * {@link RecyclerView.LayoutManager} changes orientation.
     */
    public void setOrientation(@DecorationOrientation int orientation) {
        if (orientation != DecorationOrientation.LEFT && orientation != DecorationOrientation.TOP && orientation != DecorationOrientation.RIGHT && orientation != DecorationOrientation.BOTTOM) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either DecorationOrientation.LEFT or TOP");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        if (mOrientation == DecorationOrientation.TOP || mOrientation == DecorationOrientation.BOTTOM) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom, top;
            if (mOrientation == DecorationOrientation.TOP) {
                top = mBounds.top;
                bottom = top + dividerSize;
            } else {
                bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
                top = bottom - dividerSize;
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            final int right, left;
            if (mOrientation == DecorationOrientation.LEFT) {
                left = mBounds.left;
                right = left + dividerSize;
            } else {
                right = mBounds.right + Math.round(ViewCompat.getTranslationX(child));
                left = right - dividerSize;
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    @CallSuper
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        switch (mOrientation) {
            case DecorationOrientation.LEFT:
                outRect.set(dividerSize, 0, 0, 0);
                break;
            case DecorationOrientation.TOP:
                outRect.set(0, dividerSize, 0, 0);
                break;
            case DecorationOrientation.RIGHT:
                outRect.set(0, 0, dividerSize, 0);
                break;
            case DecorationOrientation.BOTTOM:
                outRect.set(0, 0, 0, dividerSize);
                break;
        }
    }
}
