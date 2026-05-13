package com.example.myproject.data.local.dao.model

data class UserPlanWithDetails(
    @androidx.room.ColumnInfo(name = "_id")
    val id: Int,
    val userId:String,
    val planId:String,
    val title: String,
    val status: Int,
    val isPreset: Boolean,
    val totalWeeks: Int,
    val frequency: Int,
    val isFavorite: Boolean,
    @androidx.room.ColumnInfo(name = "daily_minutes")
    val dailyMinutes: Int,
    @androidx.room.ColumnInfo(name = "start_date")
    val startDate: Long?,
    @androidx.room.ColumnInfo(name = "completed_days")
    val completedDays: Int,
    @androidx.room.ColumnInfo(name = "action_count")
    val actionCount: Int
)