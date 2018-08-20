package com.stone.recyclerdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.stone.recyclerwrapper.CommonAdapter
import com.stone.recyclerwrapper.QAdapter
import com.stone.recyclerwrapper.base.ViewHolder
import org.jetbrains.anko.act



class KotlinActivity : AppCompatActivity() {

    lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)


        val adapter = object : CommonAdapter<String>(act, R.layout.activity_kotlin, null) {
            override fun bindData(holder: ViewHolder, itemData: String, position: Int) {
                Logs.d("KotlinActivity.bindData() called with: holder = [$holder], itemData = [$itemData], position = [$position]")
            }

        }

        val qAdapter = QAdapter<String>(act, R.layout.activity_kotlin) { holder, itemData, pos -> Logs.d("KotlinActivity.onCreate() called with: holder = [$holder], itemData = [$itemData], pos = [$pos]")}

    }
}
