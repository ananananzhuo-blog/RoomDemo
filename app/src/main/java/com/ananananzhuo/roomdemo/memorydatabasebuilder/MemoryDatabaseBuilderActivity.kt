package com.ananananzhuo.roomdemo.memorydatabasebuilder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ananananzhuo.roomdemo.GoodsDatabase
import com.ananananzhuo.roomdemo.R
import com.ananananzhuo.roomdemo.simpleuse.Goods
import kotlinx.android.synthetic.main.activity_memory_database_builder.*
import kotlinx.android.synthetic.main.activity_simple_use.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder

class MemoryDatabaseBuilderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_database_builder)
        val db = Room.inMemoryDatabaseBuilder(this, GoodsDatabase::class.java).build()
        btn_insert_memory.setOnClickListener {
            GlobalScope.launch {
                db.goodsDao().insertAll(Goods("hello World"))
            }
        }
        btn_query_memory.setOnClickListener {
            GlobalScope.launch {
                val all = db.goodsDao().getAll()
                val sb = StringBuilder()
                for (good in all){
                    sb.append("商品id：${good.id}  商品名：${good.name} \n")
                }
                withContext(Dispatchers.Main){
                    tv_query_result_memory.text=sb.toString()
                }
            }
        }
    }
}