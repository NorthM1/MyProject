package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.TrainingActionRecordEntity

object TestTrainingActionRecordEntity {
    private const val SESSION_1_BASE_TIME = 1710892800000L
    private const val SESSION_2_BASE_TIME = 1710979200000L
    // base time for today's session (non-const because it's computed at runtime)
    private val SESSION_3_BASE_TIME = System.currentTimeMillis()

    fun getTestRecordList(): List<TrainingActionRecordEntity> {
        return listOf(
            // Session 1
            TrainingActionRecordEntity(_id = 1L, trainingSessionId = 1L, actionId = "act_001", status = 0, createdAt = SESSION_1_BASE_TIME + 120000),
            TrainingActionRecordEntity(_id = 2L, trainingSessionId = 1L, actionId = "act_001", status = 0, createdAt = SESSION_1_BASE_TIME + 240000),
            TrainingActionRecordEntity(_id = 3L, trainingSessionId = 1L, actionId = "act_001", status = 0, createdAt = SESSION_1_BASE_TIME + 360000),

            TrainingActionRecordEntity(_id = 4L, trainingSessionId = 1L, actionId = "act_002", status = 0, createdAt = SESSION_1_BASE_TIME + 480000),
            TrainingActionRecordEntity(_id = 5L, trainingSessionId = 1L, actionId = "act_002", status = 0, createdAt = SESSION_1_BASE_TIME + 600000),
            TrainingActionRecordEntity(_id = 6L, trainingSessionId = 1L, actionId = "act_002", status = 1, createdAt = SESSION_1_BASE_TIME + 650000),

            TrainingActionRecordEntity(_id = 7L, trainingSessionId = 1L, actionId = "act_004", status = 0, createdAt = SESSION_1_BASE_TIME + 800000),
            TrainingActionRecordEntity(_id = 8L, trainingSessionId = 1L, actionId = "act_004", status = 0, createdAt = SESSION_1_BASE_TIME + 900000),

            // Session 2
            TrainingActionRecordEntity(_id = 9L, trainingSessionId = 2L, actionId = "act_001", status = 0, createdAt = SESSION_2_BASE_TIME + 120000),
            TrainingActionRecordEntity(_id = 10L, trainingSessionId = 2L, actionId = "act_001", status = 0, createdAt = SESSION_2_BASE_TIME + 240000),
            TrainingActionRecordEntity(_id = 11L, trainingSessionId = 2L, actionId = "act_001", status = 0, createdAt = SESSION_2_BASE_TIME + 360000),

            TrainingActionRecordEntity(_id = 12L, trainingSessionId = 2L, actionId = "act_002", status = 0, createdAt = SESSION_2_BASE_TIME + 500000)

            // === Records for today's session (trainingSessionId = 3) ===
            // act_001: 2 completed groups
            ,TrainingActionRecordEntity(_id = 13L, trainingSessionId = 3L, actionId = "act_001", status = 0, createdAt = SESSION_3_BASE_TIME + 120000)
            ,TrainingActionRecordEntity(_id = 14L, trainingSessionId = 3L, actionId = "act_001", status = 0, createdAt = SESSION_3_BASE_TIME + 240000)
            // act_002: 1 completed group
            ,TrainingActionRecordEntity(_id = 15L, trainingSessionId = 3L, actionId = "act_002", status = 0, createdAt = SESSION_3_BASE_TIME + 360000)
        )
    }
}

