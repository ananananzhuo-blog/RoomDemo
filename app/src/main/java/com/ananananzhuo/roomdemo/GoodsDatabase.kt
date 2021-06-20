package com.ananananzhuo.roomdemo

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ananananzhuo.roomdemo.simpleuse.Goods
import com.ananananzhuo.roomdemo.simpleuse.GoodsDao

/**
 * author  :mayong
 * function:
 * date    :2021/6/20
 **/
@Database(entities = [Goods::class],version = 1)
abstract class GoodsDatabase: RoomDatabase() {
   abstract fun goodsDao(): GoodsDao

}