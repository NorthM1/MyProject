package com.example.myproject.ui.home

data class TodayTask(
    val actionId: String,
    val name: String,
    val meta: String,
    val completedGroups: Int,
    val totalGroups: Int
) {
    val isCompleted: Boolean get() = completedGroups >= totalGroups
}

