package com.stone.recyclerwrapper.wrapper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.stone.recyclerwrapper.base.ViewHolder;
import com.stone.recyclerwrapper.utils.WrapperUtils;

/**
 * 添加EmptyView的适配器
 * Created by zhy on 16/6/23.
 */
public class EmptyWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @SuppressWarnings("WeakerAccess")
    public static final int ITEM_TYPE_EMPTY = Integer.MAX_VALUE - 1;

    private RecyclerView.Adapter mInnerAdapter;
    private View mEmptyView;
    private int mEmptyLayoutId;
    private int mViewType;

    public EmptyWrapper(RecyclerView.Adapter adapter) {
        setWrapAdapter(adapter);
    }

    @SuppressWarnings("WeakerAccess")
    public void setWrapAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null)
            return;
        if (this.mInnerAdapter != null)
            this.mInnerAdapter.unregisterAdapterDataObserver(observer);
        this.mInnerAdapter = adapter;
        this.mInnerAdapter.registerAdapterDataObserver(observer);
    }

    @SuppressWarnings("WeakerAccess")
    public RecyclerView.Adapter getmInnerAdapter() {
        return mInnerAdapter;
    }

    /**
     * 判断数据源是否为空
     */
    private boolean isEmpty() {
        return (mEmptyView != null || mEmptyLayoutId != 0) && mInnerAdapter.getItemCount() == 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isEmpty()) {
            ViewHolder holder;
            if (mEmptyView != null) {
                holder = ViewHolder.createViewHolder(parent.getContext(), mEmptyView);
            } else {
                holder = ViewHolder.createViewHolder(parent.getContext(), parent, mEmptyLayoutId);
            }
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isEmpty()) {
                    return gridLayoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });


    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        //noinspection unchecked
        mInnerAdapter.onViewAttachedToWindow(holder);
        if (isEmpty()) {
            WrapperUtils.setFullSpan(holder);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (isEmpty()) {
            return mViewType == 0 ? ITEM_TYPE_EMPTY : mViewType;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isEmpty()) {
            return;
        }
        //noinspection unchecked
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) return 1;
        return mInnerAdapter.getItemCount();
    }

    public EmptyWrapper setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        return this;
    }

    public EmptyWrapper setEmptyView(int viewType, View emptyView) {
        mEmptyView = emptyView;
        this.mViewType = viewType;
        return this;
    }

    public EmptyWrapper setEmptyView(int layoutId) {
        mEmptyLayoutId = layoutId;
        mViewType = layoutId;
        return this;
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        EmptyWrapper emptyWrapper = EmptyWrapper.this;

        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            emptyWrapper.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            emptyWrapper.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            emptyWrapper.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition,
                                     int itemCount) {
            emptyWrapper.notifyItemMoved(fromPosition, itemCount);
        }
    };
}
