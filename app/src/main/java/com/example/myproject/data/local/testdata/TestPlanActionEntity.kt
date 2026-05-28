package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.PlanActionEntity

object TestPlanActionEntity {
    fun getTestPlanActionList(): List<PlanActionEntity> = listOf(
        // plan_001: 初期康复 — 3个动作，共8组
        PlanActionEntity(_id = 1, planId = "plan_001", actionId = "act_001", groups = 3, times = 15),
        PlanActionEntity(_id = 2, planId = "plan_001", actionId = "act_002", groups = 3, times = 10),
        PlanActionEntity(_id = 3, planId = "plan_001", actionId = "act_004", groups = 2, times = 10),

        // plan_002: 进阶康复 — 3个动作，共12组
        PlanActionEntity(_id = 4, planId = "plan_002", actionId = "act_002", groups = 4, times = 15),
        PlanActionEntity(_id = 5, planId = "plan_002", actionId = "act_003", groups = 5, times = 10),
        PlanActionEntity(_id = 6, planId = "plan_002", actionId = "act_004", groups = 3, times = 12),

        // plan_003: 力量强化 — 3个动作，共12组
        PlanActionEntity(_id = 7, planId = "plan_003", actionId = "act_001", groups = 3, times = 15),
        PlanActionEntity(_id = 8, planId = "plan_003", actionId = "act_003", groups = 5, times = 12),
        PlanActionEntity(_id = 9, planId = "plan_003", actionId = "act_004", groups = 4, times = 12)
    )
}
