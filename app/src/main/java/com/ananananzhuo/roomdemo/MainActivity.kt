package com.ananananzhuo.roomdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.ananananzhuo.mvvm.activity.CustomAdapterActivity
import com.ananananzhuo.mvvm.bean.bean.ItemData
import com.ananananzhuo.mvvm.callback.CallData
import com.ananananzhuo.mvvm.callback.Callback
import com.ananananzhuo.roomdemo.memorydatabasebuilder.MemoryDatabaseBuilderActivity
import com.ananananzhuo.roomdemo.operatewithlivedata.OperateWithLivedataActivity
import com.ananananzhuo.roomdemo.relative.ManyToManyActivity
import com.ananananzhuo.roomdemo.relative.OneToManyActivity
import com.ananananzhuo.roomdemo.relative.OneToOneActivity
import com.ananananzhuo.roomdemo.savelist.SaveListDataActivity
import com.ananananzhuo.roomdemo.simpleuse.SimpleUseActivity
import kotlinx.android.synthetic.main.activity_main.*

fun logEE(msg: String) {
    Log.e("安安安安卓", msg)
}

class MainActivity : CustomAdapterActivity() {
    private lateinit var db: OurRoomDatabase
    private var count = 0
    override fun getAdapterDatas(): MutableList<ItemData> = mutableListOf(
        ItemData(title = "简单使用Room", callback = object : Callback {
            override fun callback(callData: CallData) {
                startActivity(Intent(this@MainActivity, SimpleUseActivity::class.java))
            }
        }),
        ItemData(title = "使用Livedata观察数据查询", callback = object : Callback {
            override fun callback(callData: CallData) {
                startActivity(Intent(this@MainActivity, OperateWithLivedataActivity::class.java))
            }
        }),
        ItemData(title = "inMemoryDatabaseBuilder方式获取数据库对象", callback = object : Callback {
            override fun callback(callData: CallData) {
                startActivity(Intent(this@MainActivity, MemoryDatabaseBuilderActivity::class.java))
            }
        }),
        ItemData(title = "查询一条数据", callback = object : Callback {
            override fun callback(callData: CallData) {
                db.goodsDao().getOne(1L).observe(this@MainActivity) {
                    count++
                    callData.itemData.apply {
                        content = "查询第$count 次"
                        notifyDataSetChange()
                    }
                }
            }
        }),
        ItemData(title = "Room存储List列表", callback = object : Callback {
            override fun callback(callData: CallData) {
                startActivity(Intent(this@MainActivity, SaveListDataActivity::class.java))
            }
        }),
        ItemData(title = "Room数据关系一对一") {
            startActivity(Intent(this, OneToOneActivity::class.java))
        },
        ItemData(title = "Room数据关系一对多") {
            startActivity(Intent(this, OneToManyActivity::class.java))
        },
        ItemData(title = "Room数据关系do对多") {
            startActivity(Intent(this, ManyToManyActivity::class.java))
        },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db =
            Room.databaseBuilder(applicationContext, OurRoomDatabase::class.java, "goods").build()
    }


    override fun showFirstItem(): Boolean = true
}