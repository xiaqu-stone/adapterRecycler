package com.stone.recyclerwrapper;

import android.content.Context;

import com.stone.recyclerwrapper.base.ItemViewDelegate;
import com.stone.recyclerwrapper.base.ViewHolder;

import java.util.List;

/**
 * 简单通用型Adapter
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {

    public CommonAdapter(Context context, final int layoutId, List<T> datas) {
        super(context, datas);

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void bindData(ViewHolder holder, T t, int position) {
                CommonAdapter.this.bindData(holder, t, position);
            }
        });

    }

    /**
     * 在此处绑定View数据
     *
     * @param holder   ViewHodler
     * @param itemData 数据Model的对象
     * @param position pos 下标索引
     */
    protected abstract void bindData(ViewHolder holder, T itemData, int position);


}
