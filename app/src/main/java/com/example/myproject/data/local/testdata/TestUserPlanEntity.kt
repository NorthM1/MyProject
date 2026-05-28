package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.UserPlanEntity

object TestUserPlanEntity {
    /** 训练开始时间戳：当前时间 - 13天 */
    private val startTimestamp: Long
        get() = System.currentTimeMillis() - 13 * 86_400_000L

    fun getTestUserPlanList(): List<UserPlanEntity> {
        val start = startTimestamp
        return listOf(
            UserPlanEntity(
                _id = 1,
                planId = "plan_001",
                userId = "user_1001",
                status = UserPlanEntity.Companion.Status.IN_PROGRESS,
                isFavorite = true,
                startDate = start,
                lastCompletedTime = start + 10 * 86_400_000L,
                createdAt = start,
                updatedAt = start,
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
                createdAt = start,
                updatedAt = start,
                syncStatus = UserPlanEntity.Companion.SyncStatus.SYNCED
            ),
            UserPlanEntity(
                _id = 3,
                planId = "plan_003",
                userId = "user_1001",
                status = UserPlanEntity.Companion.Status.NOT_STARTED,
                isFavorite = false,
                startDate = null,
                lastCompletedTime = null,
                createdAt = start,
                updatedAt = start,
                syncStatus = UserPlanEntity.Companion.SyncStatus.SYNCED
            )
        )
    }
}
