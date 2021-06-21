package com.ananananzhuo.roomdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ananananzhuo.roomdemo.memorydatabasebuilder.MemoryDatabaseBuilderActivity
import com.ananananzhuo.roomdemo.operatewithlivedata.OperateWithLivedataActivity
import com.ananananzhuo.roomdemo.simpleuse.SimpleUseActivity
import kotlinx.android.synthetic.main.activity_main.*
fun logEE(msg:String){
    Log.e("安安安安卓",msg)
}
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_simpleuse_room.setOnClickListener {
            startActivity(Intent(this,SimpleUseActivity::class.java))
        }

        btn_operate_with_livedata.setOnClickListener {
            startActivity(Intent(this,OperateWithLivedataActivity::class.java))
        }

        btn_memory_database.setOnClickListener {
            startActivity(Intent(this,MemoryDatabaseBuilderActivity::class.java))
        }
    }
}