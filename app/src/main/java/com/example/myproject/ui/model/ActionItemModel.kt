package com.example.myproject.ui.model

data class ActionItemModel(
    /**
     * 动作ID
     * 数据来源：[ActionEntity._id]
     * 关联方式：通过 [PlanActionEntity.actionId] 获取
     */
    val actionId: String,

    /**
     * 动作序号
     * 数据来源：列表索引 + 1（或在插入 [PlanActionEntity] 时的顺序）
     */
    val stepNumber: Int,

    /**
     * 动作标题
     * 数据来源：[ActionEntity.name]
     */
    val title: String,

    /**
     * 动作时长（秒）
     * 数据来源：[ActionEntity.durationSeconds]
     * 说明：UI可自行转换为分钟（如 durationSeconds / 60）
     */
    val durationSeconds: Int,

    /**
     * 动作组数
     * 数据来源：[PlanActionEntity.groups]
     */
    val groups: Int,

    /**
     * 每组次数
     * 数据来源：[PlanActionEntity.times]
     */
    val times: Int,

    /**
     * 完成组数
     * 数据来源[TrainingSessionActionProgressEntity.completedGroups]
      关联方式：通过 [TrainingSessionActionProgressEntity] 获取
     */
    val completedGroups:Int
)
