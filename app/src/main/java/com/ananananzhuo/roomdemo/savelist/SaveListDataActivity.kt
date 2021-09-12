package com.ananananzhuo.roomdemo.savelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.room.*
import com.ananananzhuo.mvvm.activity.CustomAdapterActivity
import com.ananananzhuo.mvvm.bean.bean.ItemData
import com.ananananzhuo.mvvm.callback.CallData
import com.ananananzhuo.mvvm.callback.Callback
import com.ananananzhuo.roomdemo.OurRoomDatabase
import com.ananananzhuo.roomdemo.R
import com.ananananzhuo.roomdemo.logEE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.lang.reflect.Type
import java.util.*

class SaveListDataActivity : CustomAdapterActivity() {

    private lateinit var database: OurRoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(this, OurRoomDatabase::class.java, "student").build()
    }

    override fun getAdapterDatas(): MutableList<ItemData> = mutableListOf(
        ItemData(title = "插入学生信息") { callData ->
            val student =
                Student("保尔柯察金", listOf(Book("坏蛋是怎么养成的")), Date(System.currentTimeMillis()))
            lifecycleScope.launch {
                database.studentAndBookDao().insert(student)
            }
            callData.itemData.content = "插入成功"
            callData.itemData.notifyDataSetChange()
        },
        ItemData(title = "查询学生信息") {
            lifecycleScope.launch {
                val student = database.studentAndBookDao().getStudents()
                val content = StringBuilder()
                student.forEach {
                    content.append("${it.books[0].name} ,")
                }
                it.itemData.content = content.toString()
                it.itemData.notifyDataSetChange()
            }
        },
        ItemData(title = "查询日期信息") {
            lifecycleScope.launch {
                val students = database.studentAndBookDao().getStudents()
                logEE(students.toString())
            }
        }
    )

    override fun showFirstItem(): Boolean = false
}

@Dao
interface StudentAndBookDao {
    @Insert
    suspend fun insert(student: Student)

    @Query("select * from student")
    suspend fun getStudents(): List<Student>


}

@Entity
data class Book(var name: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}

@TypeConverters(BookConvert::class, DateConverter::class)
@Entity
data class Student(var name: String, var books: List<Book>, var date: Date) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}

class BookConvert {
    private val gson = Gson()

    @TypeConverter
    fun objectToString(list: List<Book>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToObject(json: String?): List<Book> {
        val listType: Type = object : TypeToken<List<Book>>() {}.type
        return gson.fromJson(json, listType)
    }
}

class DateConverter {
    @TypeConverter
    fun revertDate(value: Long): Date {
        return Date(value);
    }

    @TypeConverter
    fun converterDate(value: Date): Long {
        return value.time;
    }
}