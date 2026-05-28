package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.PlanEntity

object TestPlanEntity {
    fun getTestPlanList(): List<PlanEntity> = listOf(
        PlanEntity(
            _id = "plan_001",
            name = "膝关节术后1-2周初期康复",
            coverImageUri = "assets/images/plan_1_2_weeks.png",
            totalWeeks = 2,
            dailyMinutes = 30,
            frequency = PlanEntity.Companion.Frequency.DAILY,
            isPreset = true
        ),
        PlanEntity(
            _id = "plan_002",
            name = "膝关节术后3-4周进阶康复",
            coverImageUri = "assets/images/plan_3_4_weeks.png",
            totalWeeks = 2,
            dailyMinutes = 45,
            frequency = PlanEntity.Companion.Frequency.DAILY,
            isPreset = true
        ),
        PlanEntity(
            _id = "plan_003",
            name = "膝关节术后5-8周力量强化",
            coverImageUri = null,
            totalWeeks = 4,
            dailyMinutes = 60,
            frequency = PlanEntity.Companion.Frequency.ALTERNATE,
            isPreset = true
        )
    )
}
