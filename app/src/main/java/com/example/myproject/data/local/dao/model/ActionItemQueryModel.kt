package com.example.myproject.data.local.dao.model

import androidx.room.ColumnInfo

data class ActionItemQueryModel(
    @ColumnInfo(name = "action_id")
    val actionId: String,
    val title: String,
    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Int,
    val groups: Int,
    val times: Int,
    @ColumnInfo(name = "completed_groups")
    val completedGroups: Int?
)

