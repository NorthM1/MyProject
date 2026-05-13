package com.example.myproject.ui.model

data class PlanDetailModel(
    /**
     * 方案ID
     * 数据来源：[UserPlanEntity.planId]
     */
    val planId: String,

    /**
     * 归属用户ID
     * 数据来源：[UserPlanEntity.userId]
     */
    val userId: String,

    /**
     * 方案标题
     * 数据来源：[PlanEntity.name]
     */
    val title: String,

    /**
     * 状态（如“进行中”“未开始”“已完成”）
     * 数据来源：[UserPlanEntity.status]
     * 转换规则：0→"进行中"，1→"未开始"，2→"已完成"
     */
    val status: Int,

    /**
     * 是否为预置方案
     * 数据来源：[PlanEntity.isPreset]
     */
    val isPreset: Boolean,

    /**
     * 方案总周数
     * 数据来源：[PlanEntity.totalWeeks]
     */
    val totalWeeks: Int,

    /**
     * 每日建议时长（分钟）
     * 数据来源：[PlanEntity.dailyMinutes]
     */
    val dailyMinutes: Int,

    /**
     * 总进度百分比（0-100）
     * 数据来源：计算得出
     * 计算规则：min(100, 已完成天数 * 100 / 总天数)
     */
    val progress: Int,

    /**
     * 该方案包含的总动作个数
     * 数据来源：COUNT([PlanActionEntity]) WHERE plan_id = ?
     */
    val actionCount: Int,

    /**
     * 剩余天数
     * 数据来源：计算得出（方案总天数 - 已完成天数）
     */
    val remainDays: Int,

    /**
     * 今日已完成组数
     * 数据来源：[TrainingSessionEntity.totalCompletedGroups]
     * 说明：若当日无会话数据则为0
     */
    val todayCompletedGroups: Int,

    /**
     * 今日总任务组数
     * 数据来源：[TrainingSessionEntity.todayTotalGroups]
     * 说明：若当日无会话数据则为0
     */
    val todayTotalGroups: Int,

//    /**
//     * 今日任务进度条（0-100）
//     * 数据来源：todayCompletedGroups * 100 / todayTotalGroups
//     */
//    val todayProgress: Int,

    /**
     * 动作列表
     * 数据来源：关联查询 [PlanActionEntity] 和 [ActionEntity]
     */
    val actionList: List<ActionItemModel>
)
