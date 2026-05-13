package com.example.myproject.ui.activity

import BaseActivity
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.myproject.R
import com.example.myproject.data.local.AppDatabase
import com.example.myproject.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.component1
import kotlin.collections.component2

class MainActivity : BaseActivity<ActivityMainBinding> (){
    override fun getViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun initView() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController
        val database= AppDatabase.getInstance(application)
        lifecycleScope.launch(Dispatchers.IO) {
            // 强制获取底层可写数据库，触发建库与 onCreate 回调
            database.openHelper.writableDatabase
            LogD("数据库预输入数据完成")
        }

        // 1. 将 ID 和对应的 View 放入一个 Map 集合中
        val tabs = binding.run {
            mapOf(
                R.id.home_fragment to tabHome,
//                R.id.action_fragment to tabAction,
                R.id.plan_fragment to tabPlan,
                R.id.my_fragment to tabMy
            )
        }

        // 2. 批量设置点击事件
        tabs.forEach { (destId, view) ->
            view.setOnClickListener {
                if (navController.currentDestination?.id != destId) {
                    navController.navigate(destId)
                }
            }
        }

        // 3. 统一处理选中状态
        navController.addOnDestinationChangedListener { _, destination, _ ->
            tabs.forEach { (destId, view) ->
                view.isSelected = (destId == destination.id)
            }
        }
    }

    override fun initData() {
    }

}