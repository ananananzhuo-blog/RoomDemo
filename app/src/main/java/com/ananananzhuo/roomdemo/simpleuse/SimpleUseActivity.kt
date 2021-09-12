package com.ananananzhuo.roomdemo.simpleuse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.ananananzhuo.roomdemo.OurRoomDatabase
import com.ananananzhuo.roomdemo.R
import kotlinx.android.synthetic.main.activity_simple_use.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder

class SimpleUseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_use)
        val db =
            Room.databaseBuilder(applicationContext, OurRoomDatabase::class.java, "goods").build()
        btn_insert.setOnClickListener {
           GlobalScope.launch {
               db.goodsDao().insertAll(Goods("阿娜卡列尼亚"))
           }
        }
        btn_getall.setOnClickListener {
            GlobalScope.launch {
                val all = db.goodsDao().getAll()
                val sb = StringBuilder()
                for (good in all){
                    sb.append("商品id：${good.id}  商品名：${good.name} \n")
                }
                withContext(Dispatchers.Main){
                    tv_simpleuse.text=sb.toString()
                }
            }
        }


    }
}