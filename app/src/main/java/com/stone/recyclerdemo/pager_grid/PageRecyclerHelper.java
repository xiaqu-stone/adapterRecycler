package com.stone.recyclerdemo.pager_grid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.stone.recyclerdemo.Logs;

/**
 * Created By: sqq
 * Created Time: 17/10/27 下午3:21.
 */

public class PageRecyclerHelper {
    /*
    *
    * 关于RecyclerView几种监听的回调顺序：
    * 1. 首先在初始化UI时，会回调一次onScroll()
    * 2. ACTION_DOWN时，回调 OnTouchListener()
    * 3. ACTION_MOVE时，首先回调OnTouchListener()，再次回调onScrollStateChanged(),表示滚动状态的改变state==1 处于dragging，最后回调onScroll(),将本次滑动体现到具体的滑动距离上。
    * 4. 然后就是处于 ACTION_MOVE dragging的状态中，每次先后的回调 onTouchListener 与 onScroll()
    * 5. ACTION_UP时，先是onTouchListener，其实，此时也就是所谓的惯性滑动时间，会先回调 onFling()，接着onScrollStateChanged() state==2 处于Setting状态，完后调用onScroll(),
    * 6. 在最后的惯性时间里，会一直根据最后onFling()回调的速度，在逐级衰减的过程中 一直反复回调 onScroll 将当前惯性的滑动距离 提现出来。
    * 7. 静止后，回调onScrollStateChanged（） state==0，将OnScrollListener的滑动监听置为 idle 空闲状态
    *
    * 关于滑动翻页实现的思路：
    *
    * 1. 确定当前的滑动的方向（left or right），onTouchListener 或者 根据onScroll
    * 2. 监听当前的滑动距离，确定达到翻页滑动距离阈值 onScroll
    * 3. 计算滑动位置，即确定当前位置与目标滚动位置
    * 4. 滚动到指定目标位置 smoothScrollTo
    * 优化点：
    * 5. 处理滚动到指定位置时的 滚动动画
    * 6. 处理滚动时Item的动画
    * 注意点：
    * 7. 处理OnScrollListener时需要注意，当state==2时，不一定是经过state=1的状态转化过来的。当通过手动调用mRecyclerView.smoothScroll等滚动API时，state会直接由state=0变化为state=2。但是这中间可以通过 onTouchListener（由于未经过Touch事件，故不会不触发此监听）或者是否经过了state=1的状态来区分当前触发OnScrollListener的原因。
    * 8.
    * */
    private static final int NULL_ORIENTATION = -1;

    private RecyclerView mRecyclerView;

//    private PageScrollListener mOnScrollListener = new PageScrollListener();

    private PageFlingListener mOnFlingListener = new PageFlingListener();
    /**
     * 标记state ==0 时，是手动还是自动 的
     */
    private boolean isManualResetScrollState;
    /**
     * 记录每次触摸开始（即 ACTION_MOVE ）滑动时的位置。
     */
    private int startX, startY;
    /**
     * 累加记录当前滑动的总距离
     */
    private int offsetX, offsetY;

    private int mOrientation = -1;
    private ValueAnimator mAnimator;

    public void setupRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        recyclerView.addOnScrollListener(new PageScrollListener());
        recyclerView.setOnFlingListener(mOnFlingListener);
        recyclerView.setOnTouchListener(new PageTouchListener());

        updateLayoutManger();
    }

    public void updateLayoutManger() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager.canScrollVertically()) {
                mOrientation = LinearLayoutManager.VERTICAL;
            } else if (layoutManager.canScrollHorizontally()) {
                mOrientation = LinearLayoutManager.HORIZONTAL;
            } else {
                mOrientation = NULL_ORIENTATION;
            }
            if (mAnimator != null) {
                mAnimator.cancel();
            }
            startX = 0;
            startY = 0;
            offsetX = 0;
            offsetY = 0;
        }
    }

    private static final String TAG = "PageRecyclerHelper";


    private class PageTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Logs.v(TAG, "onTouch() called with: v = [" + v + "], event = [" + event + "]");
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startY = offsetY;
                startX = offsetX;
                Logs.i(TAG, "onTouch:  startX = " + startX + ", startY = " + startY + ", offsetX = " + offsetX + ", offsetY = " + offsetY);
            }
            return false;
        }
    }

    private class PageScrollListener extends RecyclerView.OnScrollListener {
        /**
         * @param state 0: idle 1: dragging 3: setting
         */
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int state) {
            Logs.i(TAG, "onScrollStateChanged() called with: " + "recyclerView = [" + recyclerView + "], state = [" + state + "]");
            //newState==0表示滚动停止，此时需要处理回滚
            if (state == 0 && mOrientation != NULL_ORIENTATION) {
//                isScrolled = false;
                if (isManualResetScrollState) {
                    isManualResetScrollState = false;
                    return;
                }
                boolean move;
                int vX = 0, vY = 0;
                if (mOrientation == LinearLayoutManager.VERTICAL) {
                    int absY = Math.abs(offsetY - startY);
                    //如果滑动的距离超过屏幕的一半表示需要滑动到下一页
                    move = absY > recyclerView.getHeight() / 2;
                    vY = 0;

                    if (move) {
                        vY = offsetY - startY < 0 ? -1000 : 1000;
                    }

                } else {
                    int absX = Math.abs(offsetX - startX);
                    move = absX > recyclerView.getWidth() / 2;
                    if (move) {
                        vX = offsetX - startX < 0 ? -1000 : 1000;
                    }

                }

                mOnFlingListener.onFling(vX, vY);

            } else if (state == 1) {
                startY = offsetY;
                startX = offsetX;
                Logs.i(TAG, "onTouch:  startX = " + startX + ", startY = " + startY + ", offsetX = " + offsetX + ", offsetY = " + offsetY);
            }
        }

        /**
         * 每次{@link android.support.v7.widget.RecyclerView.OnScrollListener#onScrolled(int, int)}回调的参数值，都是针对当前 Touch事件 ACTION_MOVE的距离滑动反馈。
         * 或者 ACTION_UP 后 的惯性滑动Fling 的距离回调
         * dx dy 为正值，则属于正向滑动；为负值，则为反向滑动
         *
         * @param dx 本次滑动的 x轴滑动距离
         * @param dy y轴的滑动距离
         */
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Logs.i(TAG, "onScrolled() called with: " + "recyclerView = [" + recyclerView + "], dx = [" + dx + "], dy = [" + dy + "]");
            offsetY += dy;
            offsetX += dx;
        }
    }

    private class PageFlingListener extends RecyclerView.OnFlingListener {
        /**
         * ACTION_UP 后用以处理的惯性的回调，当RecyclerView不存在可滑动距离时（即滑动到顶部或者底部时），仍然会回调此方法
         * 速度值的正负值，代表了当前滑动方向的正负，滑动距离的正负取决于是否朝向 x y轴的正向滑动，也就是与x y 轴的正负一致
         *
         * @param velocityX x方向的速度
         * @param velocityY y方向的速度
         * @return true 劫持{@link RecyclerView#fling(int, int)}的处理逻辑，false 保有原有的RecyclerView的处理逻辑
         */
        @Override
        public boolean onFling(int velocityX, int velocityY) {

            Logs.i(TAG, "onFling() called with: " + "velocityX = [" + velocityX + "], velocityY = [" + velocityY + "]");
            if (mOrientation == NULL_ORIENTATION) {
                return false;
            }
            //获取开始滚动时所在页面的index
            int p = getStartPageIndex();
            Log.i(TAG, "onFling: the start page index = " + p + ", startX = " + startX + ", startY = " + startY + ", offsetX = " + offsetX + ", offsetY = " + offsetY);
            //记录滚动开始和结束的位置
            int endPoint;
            int startPoint;

            //如果是垂直方向
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                startPoint = offsetY;

                if (velocityY < 0) {//不支持REVERSE = true
                    p--;
                } else if (velocityY > 0) {
                    p++;
                }
                //更具不同的速度判断需要滚动的方向
                //注意，此处有一个技巧，就是当速度为0的时候就滚动会开始的页面，即实现页面复位
                endPoint = p * mRecyclerView.getHeight();

            } else {
                startPoint = offsetX;
                if (velocityX < 0) {
                    p--;
                } else if (velocityX > 0) {
                    p++;
                }
                endPoint = p * mRecyclerView.getWidth();
                Log.i(TAG, "onFling: the end page index = " + p + ", startX = " + startX + ", startY = " + startY + ", offsetX = " + offsetX + ", offsetY = " + offsetY);
            }
            if (endPoint < 0) {
                endPoint = 0;
            }
            if (startPoint == endPoint) return true;
            //使用动画处理滚动
            if (mAnimator == null) {
                mAnimator = ValueAnimator.ofInt(startPoint, endPoint);

                mAnimator.setDuration(300);
                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int nowPoint = (int) animation.getAnimatedValue();

                        if (mOrientation == LinearLayoutManager.VERTICAL) {
                            int dy = nowPoint - offsetY;
                            //这里通过RecyclerView的scrollBy方法实现滚动。
                            mRecyclerView.scrollBy(0, dy);
                        } else {
                            int dx = nowPoint - offsetX;
                            mRecyclerView.scrollBy(dx, 0);
                        }
                    }
                });
                mAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //回调监听
                        int scrollState = mRecyclerView.getScrollState();
                        Log.i(TAG, "onAnimationEnd: the end state " + scrollState);
                        if (scrollState != 0) {
                            isManualResetScrollState = true;
                            mRecyclerView.stopScroll();
                        }
                        Log.i(TAG, "onAnimationEnd: the end111 state " + mRecyclerView.getScrollState());
                        if (null != mOnPageChangeListener) {
                            mOnPageChangeListener.onPageChange(getPageIndex());
                        }
                    }
                });
            } else {
                mAnimator.cancel();
                mAnimator.setIntValues(startPoint, endPoint);
            }

            mAnimator.start();

            return true;
        }
    }

    private int getPageIndex() {
        int p;
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            p = offsetY / mRecyclerView.getHeight();
        } else {
            p = offsetX / mRecyclerView.getWidth();
        }
        return p;
    }

    private int getStartPageIndex() {
        int p;
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            p = startY / mRecyclerView.getHeight();
        } else {
            p = startX / mRecyclerView.getWidth();
        }
        return p;
    }

    private onPageChangeListener mOnPageChangeListener;

    public void setOnPageChangeListener(onPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    public interface onPageChangeListener {
        void onPageChange(int index);
    }
}
