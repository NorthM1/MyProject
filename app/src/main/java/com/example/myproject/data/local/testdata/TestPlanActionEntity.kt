package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.PlanActionEntity

object TestPlanActionEntity {
    fun getTestPlanActionList(): List<PlanActionEntity> {
        return listOf(
            PlanActionEntity(
                _id = 1,
                planId = "plan_001",
                actionId = "act_001", 
                groups = 3,
                times = 15
            ),
            PlanActionEntity(
                _id = 2,
                planId = "plan_001",
                actionId = "act_002",
                groups = 3,
                times = 10
            ),
            PlanActionEntity(
                _id = 3,
                planId = "plan_001",
                actionId = "act_004", 
                groups = 2,
                times = 10
            ),
            PlanActionEntity(
                _id = 4,
                planId = "plan_002",
                actionId = "act_002", 
                groups = 4,
                times = 15
            ),
            PlanActionEntity(
                _id = 5,
                planId = "plan_002",
                actionId = "act_003",
                groups = 5,
                times = 10
            )
        )
    }
}

