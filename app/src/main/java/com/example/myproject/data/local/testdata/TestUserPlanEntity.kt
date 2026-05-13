package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.UserPlanEntity

object TestUserPlanEntity {
    private const val PLAN_START_TIME = 1710547200000L

    fun getTestUserPlanList(): List<UserPlanEntity> {
        return listOf(
            UserPlanEntity(
                _id = 1,
                planId = "plan_001",
                userId = "user_1001",
                status = UserPlanEntity.Companion.Status.IN_PROGRESS,
                isFavorite = true,
                startDate = PLAN_START_TIME,
                lastCompletedTime = PLAN_START_TIME + 86400000L,
                createdAt = PLAN_START_TIME,
                updatedAt = PLAN_START_TIME,
                syncStatus = UserPlanEntity.Companion.SyncStatus.SYNCED
            ),
            UserPlanEntity(
                _id = 2,
                planId = "plan_002",
                userId = "user_1001",
                status = UserPlanEntity.Companion.Status.NOT_STARTED,
                isFavorite = false,
                startDate = null,
                lastCompletedTime = null,
                createdAt = PLAN_START_TIME,
                updatedAt = PLAN_START_TIME,
                syncStatus = UserPlanEntity.Companion.SyncStatus.SYNCED
            )
        )
    }
}

