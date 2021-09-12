package com.ananananzhuo.roomdemo

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ananananzhuo.roomdemo.relative.*
import com.ananananzhuo.roomdemo.savelist.Book
import com.ananananzhuo.roomdemo.savelist.Student
import com.ananananzhuo.roomdemo.savelist.StudentAndBookDao
import com.ananananzhuo.roomdemo.simpleuse.Goods
import com.ananananzhuo.roomdemo.simpleuse.GoodsDao

/**
 * author  :mayong
 * function:
 * date    :2021/6/20
 **/
@Database(
    entities = [Goods::class,
        Student::class, Book::class,
        Parent::class, Sun::class,
        Parent1::class, Sun1::class,
        Parent2::class, Sun2::class,
       ],
    version = 1,
    exportSchema = false
)
abstract class OurRoomDatabase : RoomDatabase() {
    abstract fun goodsDao(): GoodsDao

    abstract fun studentAndBookDao(): StudentAndBookDao

    abstract fun parentSunDao(): ParentSonDao

    abstract fun parent1AndSun1Dao(): Parent1AndSun1Dao

    abstract fun parent2AndSun2Dao(): Parent2AndSun2Dao

}