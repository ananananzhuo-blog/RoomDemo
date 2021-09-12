package com.ananananzhuo.roomdemo.relative

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.room.*
import com.ananananzhuo.mvvm.activity.CustomAdapterActivity
import com.ananananzhuo.mvvm.bean.bean.ItemData
import com.ananananzhuo.roomdemo.OurRoomDatabase
import com.ananananzhuo.roomdemo.logEE
import kotlinx.coroutines.launch
import java.lang.StringBuilder

/**
 * author  :mayong
 * function:
 * date    :2021/9/12
 **/
class ManyToManyActivity : CustomAdapterActivity() {
    private lateinit var database: OurRoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(this, OurRoomDatabase::class.java, "parent2AndSun2").build()
    }

    override fun getAdapterDatas(): MutableList<ItemData> = mutableListOf(
        ItemData(title = "插入小头爸爸") {
            lifecycleScope.launch {
                val parent = Parent2("小头爸爸", 1)
                database.parent2AndSun2Dao().insertParent2(parent)
                it.itemData.run {
                    content = "插入成功"
                    notifyDataSetChange()
                }
            }
        },
        ItemData(title = "插入隔壁老王") {
            lifecycleScope.launch {
                val parent = Parent2("隔壁老王", 1)
                database.parent2AndSun2Dao().insertParent2(parent)
                it.itemData.run {
                    content = "插入成功"
                    notifyDataSetChange()
                }
            }
        },
        ItemData(title = "插入大头儿子") {
            lifecycleScope.launch {
                val sun = Sun2("大头儿子", 1)
                database.parent2AndSun2Dao().insertSun2(sun)
                it.itemData.run {
                    content = "插入成功"
                    notifyDataSetChange()
                }
            }
        },
        ItemData(title = "插入大头儿子的弟弟") {
            lifecycleScope.launch {
                val sun = Sun2("大头儿子的弟弟", 1)
                database.parent2AndSun2Dao().insertSun2(sun)
                it.itemData.run {
                    content = "插入成功"
                    notifyDataSetChange()
                }
            }
        },
        ItemData(title = "查询小头爸爸的儿子们") {
            lifecycleScope.launch {
                val parentSun = database.parent2AndSun2Dao().queryParent(2)
                val sb = StringBuilder()
                sb.append("父亲是:${parentSun.parent.name}\n")
                parentSun.sun.forEachIndexed { index, sun ->
                    sb.append("儿子$index  ${sun.name} \n")
                }
                it.itemData.content = sb.toString()
                it.itemData.notifyDataSetChange()
            }
        },
        ItemData(title = "查询大头儿子弟弟的爸爸") {
            lifecycleScope.launch {
                val sunParent = database.parent2AndSun2Dao().querySun(2)
                val sb = StringBuilder()
                sb.append("儿子是:${sunParent.sun.name}\n")
                sunParent.parent.forEachIndexed { index, sun ->
                    sb.append("父亲$index : ${sun.name} \n")
                }
                it.itemData.content = sb.toString()
                it.itemData.notifyDataSetChange()
            }
        }
    )

    override fun showFirstItem(): Boolean = false
}

@Dao
interface Parent2AndSun2Dao {
    @Insert
    suspend fun insertParent2(parent: Parent2)

    @Insert
    suspend fun insertSun2(sun: Sun2)

    @Transaction
    @Query("select * from Parent2 where parentId=:parentId")
    suspend fun queryParent(parentId: Long): Parent2Sun2Ref

    @Transaction
    @Query("select * from Sun2 where sunId=:sunId")
    suspend fun querySun(sunId: Long): Sun2Parent2Ref
}

@Entity
data class Parent2(var name: String, var combineId: Long) {
    @PrimaryKey(autoGenerate = true)
    var parentId: Long? = null
}

@Entity
data class Sun2(var name: String, var combineId: Long) {
    @PrimaryKey(autoGenerate = true)
    var sunId: Long? = null
}

data class Parent2Sun2Ref(
    @Embedded
    var parent: Parent2,
    @Relation(
        parentColumn = "combineId",
        entityColumn = "combineId"
    )
    var sun: List<Sun2>
)

data class Sun2Parent2Ref(
    @Embedded
    var sun: Sun2,
    @Relation(
        parentColumn = "combineId",
        entityColumn = "combineId"
    )
    var parent: List<Parent2>
)