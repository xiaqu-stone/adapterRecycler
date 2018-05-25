package com.stone.recyclerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stone.recyclerdemo.pager_grid.PageRecyclerHelper;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mRecyclerView = findViewById(R.id.recyclerView);

        initData();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        // 设置数据
        mRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item, parent, false)) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView.findViewById(R.id.text)).setText(dataList.get(position));
            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }
        });


        PageRecyclerHelper helper = new PageRecyclerHelper();
        helper.setupRecyclerView(mRecyclerView);
    }

    private List<String> dataList = null;

    private void initData() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 47; i++) {
            dataList.add(String.valueOf(i));
        }
    }

    private static final String TAG = "Main2Activity";

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(20);
            }
        }, 2000);
//        mRecyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                testSize();
//                mRecyclerView.postDelayed(this, 2000);
//            }
//        }, 3000);
    }

    private void testSize() {
//        Log.w(TAG, "run: getWidth() = " + mRecyclerView.getHeight());
//        Log.w(TAG, "run: getMeasuredHeight() = " + mRecyclerView.getMeasuredHeight());
//
//        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(outMetrics);
//        Log.w(TAG, "run: " + outMetrics.widthPixels);
//        Log.w(TAG, "run: " + outMetrics.heightPixels);
    }
}
