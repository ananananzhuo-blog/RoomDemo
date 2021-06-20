package com.ananananzhuo.roomdemo.simpleuse

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * author  :mayong
 * function:商品类
 * date    :2021/6/20
 **/

@Entity
data class Goods(
    @ColumnInfo(name = "goodsName") val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? =null


}