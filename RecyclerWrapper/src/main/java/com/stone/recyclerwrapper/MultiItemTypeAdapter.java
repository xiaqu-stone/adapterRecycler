package com.stone.recyclerwrapper;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stone.recyclerwrapper.base.ItemViewDelegate;
import com.stone.recyclerwrapper.base.ItemViewDelegateManager;
import com.stone.recyclerwrapper.base.ViewHolder;
import com.stone.recyclerwrapper.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 多ItemType适配器
 * Created by zhy on 16/4/9.
 */
public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected List<T> mDatas = new ArrayList<>();

    protected ItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClickListener<T> mOnItemClickListener;
    protected OnItemLongClickListener<T> mOnItemLongClickListener;
    protected HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    public MultiItemTypeAdapter(Context context, List<T> datas) {
        mContext = context.getApplicationContext();
        if (datas != null) {
            mDatas.addAll(datas);
        }
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mItemViewDelegateManager.getItemViewLayoutId(viewType);
        ViewHolder holder;
        if (customViewLayoutParams() != null) {
            View itemView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            itemView.setLayoutParams(customViewLayoutParams());
            holder = ViewHolder.createViewHolder(mContext, itemView);
        } else {
            holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        }
        setListener(parent, holder, viewType);
        return holder;
    }


    /**
     * 按钮点击时间阈值
     */
    private long fastClickThreshold = 500;
    /**
     * 上次按钮点击时间
     */
    private long lastClickTime;

    public MultiItemTypeAdapter setFastClickThreshold(long fastClickThreshold) {
        this.fastClickThreshold = fastClickThreshold;
        return this;
    }

    /**
     * @function: 防止按钮快速多次点击
     * @usage: if (MyUtil.isFastDoubleClick()) {return;}
     */
    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < fastClickThreshold) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public void bindData(ViewHolder holder, T t) {
        mItemViewDelegateManager.bindData(holder, t, getTruePosition(holder));
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFastDoubleClick()) return;
                if (mOnItemClickListener != null && mDatas.size() > 0) {
                    int position = getTruePosition(viewHolder);
                    mOnItemClickListener.onItemClick(v, viewHolder, mDatas.get(position), position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null && mDatas.size() > 0) {
                    int position = getTruePosition(viewHolder);
                    return mOnItemLongClickListener.onItemLongClick(v, viewHolder, mDatas.get(position), position);
                }
                return false;
            }
        });
    }

    private int getHeaderCount() {
        int headersCount = 0;
        if (mHeaderAndFooterWrapper != null) {
            headersCount = mHeaderAndFooterWrapper.getHeadersCount();
        }
        return headersCount;
    }

    private int getTruePosition(ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        position = position - getHeaderCount();
        position = position < 0 ? 0 : position;
        return position >= mDatas.size() ? mDatas.size() - 1 : position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindData(holder, mDatas.get(position));
    }

    /**
     * 用于代码中自定义item 宽高
     */
    protected ViewGroup.LayoutParams customViewLayoutParams() {
        return null;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public List<T> getDatas() {
        return mDatas;
    }

    /**
     * @param pos  mDatas中的位置
     * @param item
     */
    public void add(int pos, T item) {

        mDatas.add(pos, item);

        notifyItemInserted(pos + getHeaderCount());

    }

    /**
     * 增加数据
     *
     * @param mList
     */
    public void addDataAll(List<T> mList) {
        this.mDatas.addAll(mList);
        notifyDataSetChanged();
    }

    /**
     * 清除之前数据,增加新的数据.即,刷新数据.
     *
     * @param mList
     */
    public void setData(final List<T> mList) {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mList != null) {
                    mDatas.clear();
                    mDatas.addAll(mList);
                }
                notifyDataSetChanged();
            }
        }, 200);
    }

    private Handler mHandler;

    public void delete(int pos) {
        mDatas.remove(pos);
        notifyItemRemoved(pos + getHeaderCount());
    }

    /**
     * 清除list里的所有数据、并刷新数据列表
     */
    public void clearAllList() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void clearAllList(boolean isNotify) {
        mDatas.clear();
        if (isNotify) notifyDataSetChanged();
    }

    private static final String TAG = "MultiItemTypeAdapter";


    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, RecyclerView.ViewHolder holder, T itemData, int position);
    }

    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, T itemData, int position);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setHeaderWrapper(HeaderAndFooterWrapper mHeaderWrapper) {
        this.mHeaderAndFooterWrapper = mHeaderWrapper;
    }


}
