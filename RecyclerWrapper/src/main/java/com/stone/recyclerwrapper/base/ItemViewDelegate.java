package com.stone.recyclerwrapper.base;


public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    /**
     * 根据position or itemData,来判断当前item是否是此ViewType
     *
     * @return true: 当前position的item选用此 item layout；false: 当前position的item不选用此ViewType
     */
    boolean isForViewType(T itemData, int position);

    void bindData(ViewHolder holder, T t, int position);

}
