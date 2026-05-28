package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.TrainingActionRecordEntity

object TestTrainingActionRecordEntity {
    /**
     * 每组动作的完成记录。
     * 仅生成今天的记录（session 12），历史记录从略。
     * - act_001: 已完成2组
     * - act_002: 已完成1组
     */
    fun getTestRecordList(): List<TrainingActionRecordEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            TrainingActionRecordEntity(_id = 1, trainingSessionId = 12, actionId = "act_001",
                status = 0, createdAt = now + 120000),
            TrainingActionRecordEntity(_id = 2, trainingSessionId = 12, actionId = "act_001",
                status = 0, createdAt = now + 240000),
            TrainingActionRecordEntity(_id = 3, trainingSessionId = 12, actionId = "act_002",
                status = 0, createdAt = now + 360000)
        )
    }
}
