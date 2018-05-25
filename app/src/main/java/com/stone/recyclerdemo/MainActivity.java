package com.stone.recyclerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyler;


    String[] strTitle = {
            "摇一摇",
            "积分商城",
            "VIP",
            "V在线客服",
            "sss",
            "更改",
            "ssd",
            "多岁的",
            "的地方",
            "换行",
            "摇一摇",
            "积分商城",
            "VIP",
            "V在线客服",
            "sss",
            "更改",
            "ssd",
            "多岁的",
            "的地方",
            "换行",
    };
    private static final String TAG = "MainActivity";
    private TextView tvMessage;
    private TextSwitcher txtSwitcher;
    private Handler mHandler;
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMessage = (TextView) findViewById(R.id.tvMessage);
        txtSwitcher = ((TextSwitcher) findViewById(R.id.txtSwitcher));
        findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, com.stone.recyclerdemo.pager_grid.MainActivity.class));
                startLoop();
            }
        });

        txtSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = (TextView) LayoutInflater.from(MainActivity.this).inflate(R.layout.text_view, new FrameLayout(getApplicationContext()), false);
                return textView;
            }
        });
        txtSwitcher.setInAnimation(this, R.anim.slide_in_bottom);
        txtSwitcher.setOutAnimation(this, R.anim.slide_out_top);

        mHandler = new Handler();
        txtSwitcher.setCurrentText(strTitle[0]);
        startLoop();

        tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
                mHandler.removeCallbacksAndMessages(null);
            }
        });


//        recyler = (RecyclerView) findViewById(R.id.recyler);
//
//        recyler.setLayoutManager(new TableLayoutManager());
////        recyler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//
//        recyler.setAdapter(new RecyclerView.Adapter() {
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = getLayoutInflater().inflate(R.layout.layout_item, parent, false);
//                if (view == null) {
//                    Log.i(TAG, "onCreateViewHolder: ");
//                }
//                return new RecyclerView.ViewHolder(view) {
//                };
//            }
//
//            @Override
//            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//                ((TextView) holder.itemView.findViewById(R.id.tv_title)).setText(strTitle[position]);
//                View lineV = holder.itemView.findViewById(R.id.lineV);
//                View lineH = holder.itemView.findViewById(R.id.lineH);
//                if (position % 2 == 0) {
//                    lineV.setVisibility(View.INVISIBLE);
//                }
//
//                if (position == 0 || position == 1) {
//                    lineH.setVisibility(View.INVISIBLE);
//                }
//            }
//
//            @Override
//            public int getItemCount() {
//                return strTitle.length;
//            }
//        });


//        getClassLoader().getResourceAsStream()

//        recyler.addItemDecoration(new TableDividerItemDecoration(10, Color.BLACK,getApplicationContext()));


//        URL url1 = getClassLoader().getResource("");
//        URL url1 = ClassLoader.getSystemResource("");//返回手机的根目录
//        Log.i(TAG, "onCreate: " + url1);

//        try {
//            File file = new File(url1.toURI());
//            String absolutePath = file.getAbsolutePath();
//            Log.i(TAG, "onCreate: " + absolutePath);
//
//            String[] list = file.list();
//            for (String str :
//                    list) {
//                Log.e(TAG, "onCreate: list->" + str);
//            }
//
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

//        URL url2 = getClassLoader().getResource("/app/src/main/assets/book_test.xml");
//        URL url2 = ClassLoader.getSystemResource("/app/src/main/assets/book_test.xml");
//        Log.i(TAG, "onCreate: " + url2);

//        SAXParseTest.start(getApplicationContext());
    }

    private void startLoop() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ++index;
                txtSwitcher.setText(strTitle[index % strTitle.length]);
                mHandler.postDelayed(this, 3000);
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.layout_item, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.setText(strTitle[position]);
        }

        @Override
        public int getItemCount() {
            return strTitle.length;
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = ((TextView) itemView.findViewById(R.id.tv_title));
        }

        void setText(String text) {
            tvTitle.setText(text);
        }
    }


}
