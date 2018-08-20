package com.stone.recyclerwrapper

import android.content.Context
import android.util.Log
import com.stone.recyclerwrapper.base.ItemViewDelegate
import com.stone.recyclerwrapper.base.ViewHolder

/**
 * Created By: sqq
 * Created Time: 8/20/18 3:43 PM.
 */
class QAdapter<T>(private val ctx: Context, private val layoutId: Int, val data: List<T>? = null, bindData: (holder: ViewHolder, itemData: T, pos: Int) -> Unit) : MultiItemTypeAdapter<T>(ctx, data) {

    init {
        addItemViewDelegate(object : ItemViewDelegate<T> {
            override fun getItemViewLayoutId(): Int {
                return layoutId
            }

            override fun isForViewType(itemData: T, position: Int): Boolean {
                return true
            }

            override fun bindData(holder: ViewHolder?, t: T, position: Int) {
                if (holder == null || t == null) {
                    Log.e("CommonAdapter", "the holder or itemData is null when called method bindData in CommonAdapter");
                } else {
                    bindData.invoke(holder, t, position)
                }
            }

        })
    }
}