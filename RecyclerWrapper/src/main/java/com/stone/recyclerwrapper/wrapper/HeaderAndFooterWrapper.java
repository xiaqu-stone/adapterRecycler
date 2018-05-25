package com.stone.recyclerwrapper.wrapper;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.stone.recyclerwrapper.MultiItemTypeAdapter;
import com.stone.recyclerwrapper.base.ViewHolder;
import com.stone.recyclerwrapper.utils.WrapperUtils;

/**
 * 添加header与footer 的适配器
 * <p/>
 * 备注：
 * 添加的headView与FootView其布局文件中根布局禁止LinearLayout,推荐使用RelativeLayout;
 * FrameLayout效果未知。
 * <p/>
 * Created by zhy on 16/6/23.
 */
@SuppressWarnings("unused")
public class HeaderAndFooterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;

    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        setWrapAdapter(adapter);
    }

    public void setWrapAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null)
            return;
        if (this.mInnerAdapter != null)
            this.mInnerAdapter.unregisterAdapterDataObserver(observer);
        this.mInnerAdapter = adapter;
        this.mInnerAdapter.registerAdapterDataObserver(observer);
    }

    public RecyclerView.Adapter getmInnerAdapter() {
        return mInnerAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return ViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));

        } else if (mFootViews.get(viewType) != null) {
            return ViewHolder.createViewHolder(parent.getContext(), mFootViews.get(viewType));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            return;
        }
        if (isFooterViewPos(position)) {
            return;
        }
        //noinspection unchecked
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                } else if (mFootViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position);
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        //noinspection unchecked
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holder);
        }
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    public void addHeaderView(View view) {
        addHeaderView(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addHeaderView(int viewType, View view) {
        if (getHeadersCount() == 0) {
            if (mInnerAdapter instanceof MultiItemTypeAdapter) {
                ((MultiItemTypeAdapter) mInnerAdapter).setHeaderWrapper(HeaderAndFooterWrapper.this);
            } else if (mInnerAdapter instanceof EmptyWrapper) {
                RecyclerView.Adapter adapter = ((EmptyWrapper) mInnerAdapter).getmInnerAdapter();
                if (adapter instanceof MultiItemTypeAdapter) {
                    ((MultiItemTypeAdapter) adapter).setHeaderWrapper(HeaderAndFooterWrapper.this);
                } else if (adapter instanceof LoadMoreWrapper) {
                    ((MultiItemTypeAdapter) ((LoadMoreWrapper) adapter).getmInnerAdapter()).setHeaderWrapper(HeaderAndFooterWrapper.this);
                }
            } else if (mInnerAdapter instanceof LoadMoreWrapper) {
                RecyclerView.Adapter adapter = ((LoadMoreWrapper) mInnerAdapter).getmInnerAdapter();
                if (adapter instanceof MultiItemTypeAdapter) {
                    ((MultiItemTypeAdapter) adapter).setHeaderWrapper(HeaderAndFooterWrapper.this);
                } else if (adapter instanceof EmptyWrapper) {
                    ((MultiItemTypeAdapter) ((EmptyWrapper) adapter).getmInnerAdapter()).setHeaderWrapper(HeaderAndFooterWrapper.this);
                }
            }
        }
        if (mHeaderViews.indexOfValue(view) == -1) {
            mHeaderViews.put(viewType, view);
            notifyDataSetChanged();
        } /*else {
            try {
                throw new Exception(view.toString() + ",this view is already added to headers");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public boolean containsHeader(View view) {
        return containsView(mHeaderViews, view);
    }

    public boolean containsFooter(View view) {
        return containsView(mFootViews, view);
    }

    private boolean containsView(SparseArrayCompat<View> views, View view) {
        return views.indexOfValue(view) != -1;
    }

    public void addFootView(View view) {
        addFootView(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public void addFootView(int viewType, View view) {
        if (mFootViews.indexOfValue(view) == -1) {
            mFootViews.put(viewType, view);
            notifyDataSetChanged();
        } /*else {
            try {
                throw new Exception(view.toString() + ",this view is already added to footers");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public boolean removeFootView(View view) {
        return removeView(mFootViews, view);
    }

    public boolean removeHeadView(View view) {
        return removeView(mHeaderViews, view);
    }

    private boolean removeView(SparseArrayCompat<View> views, View view) {
        int index = views.indexOfValue(view);
        if (index == -1) {
            return false;
        }
        views.removeAt(index);
//        notifyDataSetChanged();
        return true;
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        HeaderAndFooterWrapper headerAndFooterAdapter = HeaderAndFooterWrapper.this;

        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            headerAndFooterAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            headerAndFooterAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            headerAndFooterAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition,
                                     int itemCount) {
            headerAndFooterAdapter.notifyItemMoved(fromPosition, itemCount);
        }
    };

}
