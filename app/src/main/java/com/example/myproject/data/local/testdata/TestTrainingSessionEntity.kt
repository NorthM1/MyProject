package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.TrainingSessionEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TestTrainingSessionEntity {
    private val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /** 生成从 dayOffset 天前的日期字符串 */
    private fun dateStr(daysAgo: Int): String = fmt.format(Date(System.currentTimeMillis() - daysAgo * 86_400_000L))

    /**
     * 训练会话数据：
     * - session 1~11: 过去11天已完成的训练（每天8组全部完成）
     * - session 12: 今天的训练（8组中完成了3组，未完成）
     */
    fun getTestTrainingSessionList(): List<TrainingSessionEntity> {
        val now = System.currentTimeMillis()
        // 已完成的11天：从13天前到3天前
        val completedSessions = (1..11).map { day ->
            TrainingSessionEntity(
                _id = day.toLong(),
                userId = "user_1001",
                planId = "plan_001",
                trainDate = dateStr(14 - day), // day1→13天前, day11→3天前
                todayTotalGroups = 8,
                totalCompletedGroups = 8,
                isCompleted = true,
                createdAt = now - (14 - day) * 86_400_000L,
                lastUpdatedAt = now - (14 - day) * 86_400_000L + 1800000L
            )
        }
        // 今天的会话：部分完成
        val todaySession = TrainingSessionEntity(
            _id = 12L,
            userId = "user_1001",
            planId = "plan_001",
            trainDate = dateStr(0),
            todayTotalGroups = 8,
            totalCompletedGroups = 3,
            isCompleted = false,
            createdAt = now,
            lastUpdatedAt = now + 600000L
        )
        return completedSessions + todaySession
    }
}
