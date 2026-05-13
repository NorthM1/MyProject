package com.example.myproject.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 康复动作分步骤实体类
 * 数据库表名：action_step
 *
 * 核心业务职责：
 * 1. 存储康复动作的分步骤说明（虚拟人演示时同步显示）
 * 2. 与 ActionEntity 是一对多关系，通过 action_id 关联
 * 3. 毕设核心亮点：虚拟人分步骤引导康复训练
 */

@Entity(
    tableName = "action_step",
    foreignKeys = [
        ForeignKey(
            entity = ActionEntity::class,
            parentColumns = ["_id"],
            childColumns = ["action_id"],
            onDelete = ForeignKey.CASCADE, // 动作删除时，关联步骤一起删除
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("action_id"),
        // 唯一约束：同一动作同一序号只能出现一次
        Index(value = ["action_id", "step_number"], unique = true)
    ]
)
data class ActionStepEntity(
    /** 主键 ID，自增 */
    @PrimaryKey(autoGenerate = true)
    val _id: Long = 0,

    /** 关联的动作ID（对应 ActionEntity._id） */
    @ColumnInfo(name = "action_id")
    val actionId: String,

    /** 步骤序号（从1开始） */
    @ColumnInfo(name = "step_number")
    val stepNumber: Int,

    /** 步骤详细描述（如：站立位，双脚与肩同宽） */
    @ColumnInfo(name = "step_description")
    val stepDescription: String,


    /** 步骤建议停留时长（单位：秒，可选） */
    @ColumnInfo(name = "step_duration_seconds")
    val stepDurationSeconds: Int? = null
)