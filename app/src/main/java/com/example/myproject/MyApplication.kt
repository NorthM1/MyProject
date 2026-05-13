package com.example.myproject

import android.app.Application
import com.example.myproject.util.ToastUtil

class MyApplication : Application() {

    var userId:String?="user_1001"

    override fun onCreate() {
        super.onCreate()

        // 初始化全局 Toast
        ToastUtil.init(this)
    }
}
