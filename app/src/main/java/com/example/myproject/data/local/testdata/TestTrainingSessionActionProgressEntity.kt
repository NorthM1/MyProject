package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.TrainingSessionActionProgressEntity

object TestTrainingSessionActionProgressEntity {
    fun getTestProgressList(): List<TrainingSessionActionProgressEntity> {
        return listOf(
            TrainingSessionActionProgressEntity(
                _id = 1L,
                trainingSessionId = 1L,
                actionId = "act_001",
                completedGroups = 3
            ),
            TrainingSessionActionProgressEntity(
                _id = 2L,
                trainingSessionId = 1L,
                actionId = "act_002",
                completedGroups = 3
            ),
            TrainingSessionActionProgressEntity(
                _id = 3L,
                trainingSessionId = 1L,
                actionId = "act_004",
                completedGroups = 2
            ),
            TrainingSessionActionProgressEntity(
                _id = 4L,
                trainingSessionId = 2L,
                actionId = "act_001",
                completedGroups = 3
            ),
            TrainingSessionActionProgressEntity(
                _id = 5L,
                trainingSessionId = 2L,
                actionId = "act_002",
                completedGroups = 1
            ),
            TrainingSessionActionProgressEntity(
                _id = 6L,
                trainingSessionId = 2L,
                actionId = "act_004",
                completedGroups = 0
            )
        ,
            // === Progress entries for today's session (trainingSessionId = 3) ===
            TrainingSessionActionProgressEntity(
                _id = 7L,
                trainingSessionId = 3L,
                actionId = "act_001",
                completedGroups = 2
            ),
            TrainingSessionActionProgressEntity(
                _id = 8L,
                trainingSessionId = 3L,
                actionId = "act_002",
                completedGroups = 1
            ),
            TrainingSessionActionProgressEntity(
                _id = 9L,
                trainingSessionId = 3L,
                actionId = "act_004",
                completedGroups = 0
            )
        )
    }
}

