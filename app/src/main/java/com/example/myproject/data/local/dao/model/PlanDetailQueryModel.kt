package com.example.myproject.data.local.dao.model

import androidx.room.ColumnInfo

data class PlanDetailQueryModel(
    val planId: String,
    val userId: String,
    val title: String,
    val status: Int,
    val isPreset: Boolean,
    val totalWeeks: Int,
    val frequency: Int,
    @ColumnInfo(name = "daily_minutes")
    val dailyMinutes: Int,
    @ColumnInfo(name = "completed_days")
    val completedDays: Int,
    @ColumnInfo(name = "action_count")
    val actionCount: Int,
    @ColumnInfo(name = "today_completed_groups")
    val todayCompletedGroups: Int?,
    @ColumnInfo(name = "today_total_groups")
    val todayTotalGroups: Int?
)
