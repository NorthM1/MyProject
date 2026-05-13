package com.example.myproject.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * 全局的 Toast 工具类
 */
object ToastUtil {
    private var appContext: Context? = null

    /**
     * 在 Application 中进行初始化
     */
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun show(message: String?) {
        if (message.isNullOrEmpty()) return
        appContext?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun show(@StringRes resId: Int) {
        appContext?.let {
            Toast.makeText(it, resId, Toast.LENGTH_SHORT).show()
        }
    }

    fun showLong(message: String?) {
        if (message.isNullOrEmpty()) return
        appContext?.let {
            Toast.makeText(it, message, Toast.LENGTH_LONG).show()
        }
    }
}
