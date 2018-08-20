## 关于RecyclerView的Adapter与ItemDivider封装

针对RecyclerView封装的各种简单易用的Adapter，基于鸿洋的开源库进而封装而成的。包括加载更多，空数据，添加Header、Footer，多种ItemType以及极简模式的Adapter，并附带万能ViewHolder的封装。

### Gradle Dependencies

```
implementation "com.sqq.xiaqu:adapter-recycler:1.0.1"
```

### 使用介绍

#### Kotlin `1.0.1新增`

```
val qAdapter = QAdapter<String>(act, R.layout.activity_kotlin) { holder, itemData, pos -> Logs.d("KotlinActivity.onCreate() called with: holder = [$holder], itemData = [$itemData], pos = [$pos]")}
```

配合Kotlin中的lambda，简化Adapter的初始化代码，替换内部类的实现方式

#### Java调用
1. 最常用的单种Item的书写方式：

    ```
    mRecyclerView.setAdapter(new CommonAdapter<String>(this, R.layout.item_list, mDatas){
         @Override
         public void convert(ViewHolder holder, String s){//直接绑定数据
               holder.setText(R.id.id_item_list_title, s);
         }
    });
    ```
    在构造Adapter时数据也可直接为null，如下：
    ```
    new CommonAdapter<String>(context,layoutId,null)
    ```
    最后数据的设置通过adapter.setData()或者adapter.addDataAll()进行更新
    
2. 多种ItemViewType
    ```
    MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(this,mDatas);
    adapter.addItemViewDelegate(new MsgSendItemDelagate());
    adapter.addItemViewDelegate(new MsgComingItemDelagate());
    ```

    **每种Item类型对应一个ItemViewDelegete，例如：**

    ```
    public class MsgComingItemDelagate implements ItemViewDelegate<ChatMessage>{
          @Override
          public int getItemViewLayoutId(){
                    return R.layout.main_chat_from_msg;
          }
          @Override
          public boolean isForViewType(ChatMessage item, int position){//根据数据判断是否使用当前布局
              return item.isComMeg();
          }
          @Override
          public void convert(ViewHolder holder, ChatMessage chatMessage, int position){
              holder.setText(R.id.chat_from_content, chatMessage.getContent());
              holder.setText(R.id.chat_from_name, chatMessage.getName());
              holder.setImageResource(R.id.chat_from_icon, chatMessage.getIcon());
          }
    }
    ```

3. 添加HeaderView、FooterView

    ```
    mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
    TextView t1 = new TextView(this);
    t1.setText("Header 1");
    TextView t2 = new TextView(this);
    t2.setText("Header 2");
    mHeaderAndFooterWrapper.addHeaderView(t1);
    mHeaderAndFooterWrapper.addHeaderView(t2);
    mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
    ```
    也可以通过remove方法将已添加的View移除
4. 添加LoadMore
    ```
    mLoadMoreWrapper = new LoadMoreWrapper(mOriginAdapter);
    mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
    mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener(){
        @Override
        public void onLoadMoreRequested(){
        }
    });
    mRecyclerView.setAdapter(mLoadMoreWrapper);
    ```

    项目中LoadMoreWrapper的使用都是默认的布局文件，所以在配合网络请求的回调中需要添加如下代码：

    ```
    @Override
    public void onRequestSuccess(String content, Object viewModel, String url, String message, boolean isCache) {
        super.onRequestSuccess(content, viewModel, url, message, isCache);
        //根据数据返回，设置是否还可加载更多，即是否还需要自动触发LoadMore的加载
        loadmoreWrapper.setCanAutoLoadMore(isCanRefresh);
    }
    @Override
    public void onRequestFailure(String message) {
        super.onRequestFailure(message);
        //当加载失败时，展示失败的View，并添加点击重试的监听
        loadmoreWrapper.setLoadMoreFailture();
    }
    ```

5. 添加EmptyView
    ```
    mEmptyWrapper = new EmptyWrapper(mAdapter);
    mEmptyWrapper.setEmptyView(R.layout.empty_view);
    mRecyclerView.setAdapter(mEmptyWrapper);
    ```
6. 支持链式添加多种功能
    ```
    mAdapter = new HeaderAndFooterWrapper(
        new EmptyWrapper(
             new LoadmoreWrapper(mOriginAdapter）
        )
    );
    ```
    
    **不同的包装顺序会产生不一样的UI展示**
    
    - 当存在header或footer时，空数据View不会展示

        ```
        new EmptyWrapper(new HeaderAndFooterWrapper(originAdapter));
        ```
    - 空数据View会在header下面，即占据原有列表的位置

        ```
        new HeaderAndFooterWrapper(new EmptyWrapper(originAdapter));
        ```
7. ColorDividerItemDecoration

    可以自定义Divider的位置(l, t, r, b, 通过DecorationOrientation来定义)、颜色、大小

    ```
    mRecyclerView.addItemDecoration(new ColorDividerItemDecoration())
    ```


8. 每个MultiItemAdapter实例单独持有 防多次点击的计时，并能够手动设置其点击阈值间隔

    ```
    adapter.setFastClickThreshold(300L)
    ```




