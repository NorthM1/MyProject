package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.TrainingSessionActionProgressEntity

object TestTrainingSessionActionProgressEntity {
    /**
     * 每个会话中每个动作的完成进度。
     * - session 1~11 (已完成): 所有动作全部完成
     * - session 12 (今天): act_001=2/3, act_002=1/3, act_004=0/2
     */
    fun getTestProgressList(): List<TrainingSessionActionProgressEntity> {
        val list = mutableListOf<TrainingSessionActionProgressEntity>()
        var progressId = 0L

        // 已完成会话：每个动作全部完成
        for (sessionId in 1L..11L) {
            list += TrainingSessionActionProgressEntity(
                _id = ++progressId,
                trainingSessionId = sessionId,
                actionId = "act_001",
                completedGroups = 3
            )
            list += TrainingSessionActionProgressEntity(
                _id = ++progressId,
                trainingSessionId = sessionId,
                actionId = "act_002",
                completedGroups = 3
            )
            list += TrainingSessionActionProgressEntity(
                _id = ++progressId,
                trainingSessionId = sessionId,
                actionId = "act_004",
                completedGroups = 2
            )
        }

        // 今天会话(12)：部分完成
        list += TrainingSessionActionProgressEntity(
            _id = ++progressId, // 34
            trainingSessionId = 12L,
            actionId = "act_001",
            completedGroups = 2
        )
        list += TrainingSessionActionProgressEntity(
            _id = ++progressId, // 35
            trainingSessionId = 12L,
            actionId = "act_002",
            completedGroups = 1
        )
        list += TrainingSessionActionProgressEntity(
            _id = ++progressId, // 36
            trainingSessionId = 12L,
            actionId = "act_004",
            completedGroups = 0
        )

        return list
    }
}
