package com.ananananzhuo.roomdemo.embdded

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.room.*
import com.ananananzhuo.mvvm.activity.CustomAdapterActivity
import com.ananananzhuo.mvvm.bean.bean.ItemData
import com.ananananzhuo.roomdemo.OurRoomDatabase
import kotlinx.coroutines.launch

/**
 * author  :mayong
 * function:对象之间的嵌套
 * date    :2021/9/12
 **/
class EmbddedActivity : CustomAdapterActivity() {
    private lateinit var database: OurRoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(this,OurRoomDatabase::class.java,"parent3AndSun3").build()
    }
    override fun getAdapterDatas(): MutableList<ItemData> = mutableListOf(
        ItemData(title = "插入小头爸爸"){
            lifecycleScope.launch {
                database.parent3AndSun3Dao().insertParent(Parent3("小头爸爸", Sun3("大头儿子")))
            }
        },
        ItemData(title = "查询小头爸爸"){
            lifecycleScope.launch {
                val parent3 = database.parent3AndSun3Dao().queryParent(1)
                it.itemData.run {
                    content="父亲的名字：${parent3.parentName} \n 儿子的名字：${parent3.sun.sunName}"
                    notifyDataSetChange()
                }
            }
        }
    )

    override fun showFirstItem(): Boolean=false
}
@Dao
interface Parent3Sun3Dao {
    @Insert
    suspend fun insertParent(parent3: Parent3)

    @Insert
    suspend fun insertSun(sun: Sun3)

    @Query("select * from Parent3 where parentId=:parentId")
    suspend fun queryParent(parentId: Long): Parent3
}

@Entity
data class Parent3(
    var parentName: String,
    @Embedded
    var sun: Sun3
){
    @PrimaryKey(autoGenerate = true)
    var parentId: Long? = null
}

@Entity
data class Sun3(
    var sunName: String
){
    @PrimaryKey(autoGenerate = false)
    var sunId: Long? = null
}