package com.ananananzhuo.roomdemo.relative

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.room.*
import com.ananananzhuo.mvvm.activity.CustomAdapterActivity
import com.ananananzhuo.mvvm.bean.bean.ItemData
import com.ananananzhuo.roomdemo.OurRoomDatabase
import kotlinx.coroutines.launch

/**
 * author  :mayong
 * function:一对一关系
 * date    :2021/9/12
 **/
class OneToOneActivity : CustomAdapterActivity() {
    private lateinit var database: OurRoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database=Room.databaseBuilder(this,OurRoomDatabase::class.java,"parentsun").build()
    }
    override fun getAdapterDatas(): MutableList<ItemData> = mutableListOf(
        ItemData(title = "插入一爹") {
            lifecycleScope.launch {
                val parent = Parent("小头爸爸")
                database.parentSunDao().insertParent(parent)
                it.itemData.content="插入成功"
                it.itemData.notifyDataSetChange()
            }
        },
        ItemData(title = "插入一个儿子"){
               lifecycleScope.launch {
                   val sun=Sun("大头儿子",1)
                   database.parentSunDao().insertSun(sun)
                   it.itemData.run {
                       content="插入成功"
                       notifyDataSetChange()
                   }
               }
        },
        ItemData(title = "查询数据"){
            lifecycleScope.launch {
                var parentAndSun = database.parentSunDao().queryParent(1)
                val content = "父亲是：${parentAndSun.parent}  \n儿子是: ${parentAndSun.sun}"
                it.itemData.content=content
                it.itemData.notifyDataSetChange()
            }
        }
    )

    override fun showFirstItem(): Boolean = false
}
@Dao
interface ParentSonDao {
    @Transaction
    @Insert
    suspend fun insertParent(parent: Parent)//插入父亲

    @Insert
    suspend fun insertSun(sun: Sun)//插入儿子

    /**
     * 查询父亲和儿子的组合
     */
    @Transaction
    @Query("select * from Parent where parentId=:parentId")//查询父亲和和儿子的集合，注意我们无法直接插入ParentAndSunRef
    suspend fun queryParent(parentId: Long): ParentAndSunRef
}

/**
 * 用来表示Parent和Sun的集合，如果我们想同事查询父亲和儿子可以使用父亲id查询
 */
data class ParentAndSunRef(//我们不需要给ParentAndSunRef添加@Entity注解
    @Embedded
    val parent: Parent,//
    @Relation(
        parentColumn = "parentId",//儿子关联父亲的主键parentId
        entityColumn = "hisparentId"//父亲中必须存入儿子的某个唯一id字段hisparentId
    )
    val sun: Sun
)

/**
 * 父亲
 */
@Entity
data class Parent(var name: String) {
    @PrimaryKey(autoGenerate = true)
    var parentId: Long? = null
}

/**
 * 儿子
 */
@Entity
data class Sun(var name: String,var hisparentId:Long) {
    @PrimaryKey(autoGenerate = true)
    var sunId: Long? = null
}