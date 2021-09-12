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
 * function:
 * date    :2021/9/12
 **/
class OneToManyActivity : CustomAdapterActivity() {

    private lateinit var database: OurRoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(this,OurRoomDatabase::class.java,"parent1andsun1").build()
    }
    override fun getAdapterDatas(): MutableList<ItemData> = mutableListOf(
        ItemData("插入一个爹"){
            lifecycleScope.launch {
                val parent=Parent1("小头爸爸")
                database.parent1AndSun1Dao().insertParent1(parent)
                it.itemData.run {
                    content="插入成功"
                    notifyDataSetChange()
                }
            }
        },
        ItemData("插入一个儿子"){
            lifecycleScope.launch {
                val sun = Sun1("大头儿子",1)
                database.parent1AndSun1Dao().insertSun1(sun)
            }
            it.itemData.run {
                content="插入成功"
                notifyDataSetChange()
            }
        },
        ItemData("插入第二个儿子"){
            lifecycleScope.launch {
                val sun = Sun1("大头儿子的弟弟",1)
                database.parent1AndSun1Dao().insertSun1(sun)
            }
            it.itemData.run {
                content="插入成功"
                notifyDataSetChange()
            }
        },
        ItemData(title = "查询一个爹和两个儿子"){
            lifecycleScope.launch {
                val parentAndSunRef = database.parent1AndSun1Dao().queryParent1AndSun1(1)
                val con = "父亲是：${parentAndSunRef.parent.name} " +
                        "\n 第一个儿子是：${parentAndSunRef.sun1s[0].name} " +
                        "\n 第二个儿子是：${parentAndSunRef.sun1s[1].name}"
                it.itemData.run {
                    content=con
                    notifyDataSetChange()
                }
            }
        }
    )

    override fun showFirstItem(): Boolean =false
}
@Dao
interface Parent1AndSun1Dao{
    @Insert
    suspend fun insertParent1(parent: Parent1)
    @Insert
    suspend fun insertSun1(parent: Sun1)
    /**
     * 查询父亲和儿子的集合，这里必须要有@Transaction注解，目的是保准原子操作
     */
    @Transaction
    @Query("select * from Parent1 where parentId=:parentId")
    suspend fun queryParent1AndSun1(parentId:Long):Parent1AndSun1Ref
}
@Entity
data class Parent1(var name:String){
    @PrimaryKey(autoGenerate = true)
    var parentId:Long?=null
}
@Entity
data class Sun1(var name:String,var refparentId:Long){
    @PrimaryKey(autoGenerate = true)
    var sunId:Long?=null

}
data class Parent1AndSun1Ref(
    @Embedded
    val parent:Parent1,
    @Relation(
        parentColumn = "parentId",//这里使用parentColumn
        entityColumn = "refparentId"
    )
    val sun1s:List<Sun1>//因为一个父亲对应多个孩子，所以这里必须使用列表
)