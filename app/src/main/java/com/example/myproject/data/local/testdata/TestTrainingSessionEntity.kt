package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.TrainingSessionEntity

object TestTrainingSessionEntity {
    fun getTestTrainingSessionList(): List<TrainingSessionEntity> {
        return listOf(
            TrainingSessionEntity(
                _id = 1L,
                userId = "user_1001",
                planId = "plan_001",
                trainDate = "2024-03-20",
                todayTotalGroups = 8,
                totalCompletedGroups = 8, 
                isCompleted = true,
                createdAt = 1710892800000L,
                lastUpdatedAt = 1710896400000L
            ),
            TrainingSessionEntity(
                _id = 2L,
                userId = "user_1001",
                planId = "plan_001",
                trainDate = "2024-03-21",
                todayTotalGroups = 8,
                totalCompletedGroups = 4,
                isCompleted = false,
                createdAt = 1710979200000L,
                lastUpdatedAt = 1710981000000L
            ),
            // === Today's session (added for tests) ===
            // trainDate 固定为今天（构建时为 2026-04-14）以便在测试中识别为当前日的进度数据
            TrainingSessionEntity(
                _id = 3L,
                userId = "user_1001",
                planId = "plan_001",
                trainDate = "2026-04-14",
                todayTotalGroups = 8,
                totalCompletedGroups = 3,
                isCompleted = false,
                createdAt = System.currentTimeMillis(),
                lastUpdatedAt = System.currentTimeMillis() + 600000L
            )
        )
    }
}

