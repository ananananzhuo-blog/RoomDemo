package com.ananananzhuo.roomdemo.simpleuse

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ananananzhuo.roomdemo.operatewithlivedata.GoodsBean

/**
 * author  :mayong
 * function:
 * date    :2021/6/20
 **/
@Dao
interface GoodsDao {
    @Query("SELECT * FROM goods")
    fun getAll(): List<Goods>

    @Query("SELECT * FROM goods WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Goods>

    @Insert
    fun insertAll(vararg users: Goods)

    @Delete
    fun delete(user: Goods)


    /**
     * 查询一条，使用Livedata返回
     */
    @Query("select goodsName from goods where id=:id")
    fun getOne(id: Long):LiveData<GoodsBean>
}