package com.ananananzhuo.roomdemo.operatewithlivedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.ananananzhuo.roomdemo.OurRoomDatabase
import com.ananananzhuo.roomdemo.R
import kotlinx.android.synthetic.main.activity_operate_with_livedata.*

class OperateWithLivedataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operate_with_livedata)
        val db =
            Room.databaseBuilder(applicationContext, OurRoomDatabase::class.java, "goods").build()
        btn_select_withid.setOnClickListener {
            db.goodsDao().getOne(2L).observe(this){
                tv_select_withid.text="查询到的商品名：${it.goodsName}"
            }
        }
    }
}