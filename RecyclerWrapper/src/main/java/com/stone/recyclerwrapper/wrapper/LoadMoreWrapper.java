package com.stone.recyclerwrapper.wrapper;

import android.support.annotation.IdRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stone.recyclerwrapper.R;
import com.stone.recyclerwrapper.base.ViewHolder;
import com.stone.recyclerwrapper.utils.WrapperUtils;

/**
 * 加载更多的适配器
 * Created by zhy on 16/6/23.
 */
public class LoadMoreWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;

    private RecyclerView.Adapter mInnerAdapter;
    private View mLoadMoreView;
    private int mLoadMoreLayoutId;
    private boolean canAutoLoadMore;
    private boolean showLoadMore;

    private int mViewType;

    public LoadMoreWrapper(RecyclerView.Adapter adapter) {
        mLoadMoreLayoutId = R.layout.default_loading;
        setWrapAdapter(adapter);
    }

    public RecyclerView.Adapter getmInnerAdapter() {
        return mInnerAdapter;
    }

    public void setWrapAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null)
            return;
        if (this.mInnerAdapter != null)
            this.mInnerAdapter.unregisterAdapterDataObserver(observer);
        this.mInnerAdapter = adapter;
        this.mInnerAdapter.registerAdapterDataObserver(observer);

    }

    private boolean hasLoadMore() {
        return (mLoadMoreView != null || mLoadMoreLayoutId != 0) && showLoadMore;
    }


    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && (position >= mInnerAdapter.getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoadMore(position)) {
            return mViewType == 0 ? ITEM_TYPE_LOAD_MORE : mViewType;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            ViewHolder holder;
            if (mLoadMoreView != null) {
                holder = ViewHolder.createViewHolder(parent.getContext(), mLoadMoreView);
            } else {
                holder = ViewHolder.createViewHolder(parent.getContext(), parent, mLoadMoreLayoutId);
                mLoadMoreView = holder.getConvertView();
            }
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            if (mOnLoadMoreListener != null && canAutoLoadMore) {
                startLoadMore();
                mOnLoadMoreListener.onLoadMoreRequested();
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isShowLoadMore(position)) {
                    return layoutManager.getSpanCount();
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
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (isShowLoadMore(holder.getLayoutPosition())) {
            setFullSpan(holder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

            p.setFullSpan(true);
        }
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0);
    }


    public interface OnLoadMoreListener {
        void onLoadMoreRequested();
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public LoadMoreWrapper setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        if (loadMoreListener != null) {
            mOnLoadMoreListener = loadMoreListener;
        }
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(int viewType, View loadMoreView) {
        mLoadMoreView = loadMoreView;
        mViewType = viewType;
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(int layoutId) {
        mLoadMoreLayoutId = layoutId;
        mViewType = layoutId;
        return this;
    }

    /**
     * 当使用的是默认LoadingLayout时，采用此方法显示失败情况
     */
    public LoadMoreWrapper setLoadMoreFailure() {
        if (showLoadMore) {
            return setLoadMoreFailure(R.id.loading_text, "加载失败，请点击重试...");
        } else {
            return this;
        }

    }

    public LoadMoreWrapper startLoadMore() {
        mLoadMoreView.setOnClickListener(null);
        mLoadMoreView.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        return setLoadMoreText(R.id.loading_text, "正在加载...");
    }

    /**
     * 自定义失败时的显示情况，有一定局限性，注意使用方式，参考参数的说明
     *
     * @param viewId      TextView ID
     * @param loadingText 显示文案
     */
    public LoadMoreWrapper setLoadMoreFailure(@IdRes int viewId, String loadingText) {
        canAutoLoadMore = false;
        mLoadMoreView.setOnClickListener(mClickListener);
        mLoadMoreView.findViewById(R.id.progress_bar).setVisibility(View.GONE);
        return setLoadMoreText(viewId, loadingText);
    }

    private LoadMoreWrapper setLoadMoreText(int viewId, String loadingText) {
        TextView tvLoadingText = (TextView) mLoadMoreView.findViewById(viewId);
        tvLoadingText.setText(loadingText);
        return this;
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mOnLoadMoreListener != null) {
                startLoadMore();
                mOnLoadMoreListener.onLoadMoreRequested();
            }
        }
    };

    /**
     * 是否可以加载更多
     */
    public LoadMoreWrapper setCanAutoLoadMore(boolean canAutoLoadMore) {
        this.canAutoLoadMore = canAutoLoadMore;
        if (canAutoLoadMore && !showLoadMore) {
            showLoadMore = true;
            notifyDataSetChanged();
        }
        if (!canAutoLoadMore && showLoadMore) {
            showLoadMore = false;
            notifyDataSetChanged();
        }
        return this;
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        LoadMoreWrapper loadmoreWrapper = LoadMoreWrapper.this;

        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            loadmoreWrapper.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            loadmoreWrapper.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            loadmoreWrapper.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition,
                                     int itemCount) {
            loadmoreWrapper.notifyItemMoved(fromPosition, itemCount);
        }
    };
}
