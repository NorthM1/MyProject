package com.example.myproject.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.MyApplication
import com.example.myproject.repository.PlanRepository
import com.example.myproject.ui.home.HomeData
import com.example.myproject.ui.home.TodayTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PlanRepository(application)

    private val _homeData = MutableStateFlow(HomeData("", "", 0, 0, 0, "", "", false))
    val homeData: StateFlow<HomeData> = _homeData.asStateFlow()

    private val _todayTasks = MutableStateFlow<List<TodayTask>>(emptyList())
    val todayTasks: StateFlow<List<TodayTask>> = _todayTasks.asStateFlow()

    init {
        val userId = (application as MyApplication).userId
        if (userId != null) {
            viewModelScope.launch {
                repository.getHomeData(userId).collect { data ->
                    _homeData.value = data
                }
            }
            viewModelScope.launch {
                repository.getTodayTasks(userId).collect { tasks ->
                    _todayTasks.value = tasks
                }
            }
        }
    }
}
