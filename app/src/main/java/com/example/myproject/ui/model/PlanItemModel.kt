package com.example.myproject.ui.model

import android.os.Parcelable

data class PlanItemModel(
    val userId: String,

    val planId: String,

    /**
     * 方案ID
     * 数据来源：[UserPlanEntity._id]
     */
    val _id: Int,

    /**
     * 方案标题
     * 数据来源：[PlanEntity.name]
     * 关联方式：通过 [UserPlanEntity.planId] 关联 [PlanEntity.id] 查询
     */
    val title: String,

    /**
     * 状态标签文字（如“进行中”“未开始”“已完成”）
     * 数据来源：[UserPlanEntity.status] 转换
     * 转换规则：
     *   [UserPlanEntity.Status.IN_PROGRESS] (0) → "进行中"
     *   [UserPlanEntity.Status.NOT_STARTED] (1) → "未开始"
     *   [UserPlanEntity.Status.COMPLETED] (2) → "已完成"
     */
    val status: Int,

    /**
     * 是否是预置方案
     * 数据来源：[PlanEntity.isPreset]拼接
     * 拼接规则：
     *   isPreset=true → "预置方案"
     *   isPreset=false → "自定义方案"
     */
    val isPreset: Boolean,

    /**
     * 方案总周数
     * 数据来源：[PlanEntity.totalWeeks]
     * 关联方式：通过 [UserPlanEntity.planId] 关联 [PlanEntity.id] 查询
     */
    val totalWeeks: Int,

    /**
     * 训练频率（0=每天，1=隔天）
     * 数据来源：[PlanEntity.frequency]
     * 关联方式：通过 [UserPlanEntity.planId] 关联 [PlanEntity.id] 查询
     * （可选：如果 UI 需要显示文字，可在 Repository 层转换为“每天训练”/“隔天训练”）
     */
    val frequency: Int,

    /**
     * 是否收藏
     * 数据来源：[UserPlanEntity.isFavorite]
     */
    val isFavorite: Boolean = false,

    /**
     * 【整个方案的训练进度】（0-100）
     * 数据来源：计算得出（基于已完成的训练天数）
     * 计算规则：
     *   1. 计算方案总天数：[PlanEntity.totalWeeks] * 7
     *   2. 统计已完成天数：
     *      - 查询 [TrainingSessionEntity] 表
     *      - 条件：user_id = 当前用户ID AND plan_id = 方案ID AND is_completed = 1
     *      - COUNT 统计符合条件的记录数，即为「已完成天数」
     *   3. 进度 = min(100, 已完成天数 * 100 / 方案总天数)
     *   4. 如果方案未开始（[UserPlanEntity.startDate] 为 null），进度为 0
     *
     * 【注意】与单日进度的区别：
     *   - 单日进度：基于当日的组次完成情况（TrainingSessionEntity.cachedProgress）
     *   - 整体进度：基于已完成的训练天数（统计历史会话记录）
     */
    val progress: Int,

    /**
     * 每天训练时长（分钟）
     * 数据来源：[PlanEntity.dailyMinutes]
     * 关联方式：通过 [UserPlanEntity.planId] 关联 [PlanEntity.id] 查询
     */
    val dailyMinutes: Int,

    /**
     * 该方案包含的总动作个数
     * 数据来源：统计 [PlanActionEntity] 中匹配该方案的记录数
     */
    val actionCount: Int,

    /**
     * 【方案剩余天数】
     * 数据来源：计算得出（基于已完成的训练天数）
     * 计算规则：
     *   1. 计算方案总天数：[PlanEntity.totalWeeks] * 7
     *   2. 统计已完成天数：
     *      - 查询 [TrainingSessionEntity] 表
     *      - 条件：user_id = 当前用户ID AND plan_id = 方案ID AND is_completed = 1
     *      - COUNT 统计符合条件的记录数，即为「已完成天数」
     *   3. 剩余天数 = max(0, 方案总天数 - 已完成天数)
     *   4. 如果方案未开始（[UserPlanEntity.startDate] 为 null），剩余天数为方案总天数
     *
     * 【注意】与“自然剩余天数”的区别：
     *   - 自然剩余：基于时间差计算，不管用户有没有训练
     *   - 当前逻辑：基于实际完成的训练天数计算，用户不训练则剩余天数不变
     */
    val remainTimes: Int
)