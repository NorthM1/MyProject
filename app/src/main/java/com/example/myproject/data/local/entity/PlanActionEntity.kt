package com.example.myproject.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * 方案-动作关联实体类
 * 数据库表名：plan_action
 *
 * 核心业务职责：
 * 1. 多对多中间表，绑定方案模板与康复动作
 * 2. 记录「方案第几天、安排哪个动作、动作顺序」
 * 3. 核心高频查询：SELECT * FROM plan_action WHERE plan_id=? AND day_number=?
 */
@Entity(
    tableName = "plan_action",
    foreignKeys = [
        ForeignKey(
            entity = PlanEntity::class,
            parentColumns = ["_id"],
            childColumns = ["plan_id"],
            onDelete = ForeignKey.CASCADE, // 方案删除时，关联动作配置一起删除
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ActionEntity::class,
            parentColumns = ["_id"],
            childColumns = ["action_id"],
            onDelete = ForeignKey.RESTRICT, // 动作被使用时禁止删除
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("plan_id"),
        Index("action_id"),
        // 唯一约束：同一方案同一动作只能出现一次
        Index(value = ["plan_id", "action_id"], unique = true)
    ]
)
data class PlanActionEntity(
    /** 主键 ID，自增 */
    @PrimaryKey(autoGenerate = true)
    val _id: Long = 0,

    /** 关联的方案模板ID */
    @ColumnInfo(name = "plan_id")
    val planId: String,

    /** 关联的动作ID */
    @ColumnInfo(name = "action_id")
    val actionId: String,

    /** 该动作多少组**/
    @ColumnInfo(name = "groups")
    val groups: Int,

    /** 一组多少次 **/
    @ColumnInfo(name = "times")
    val times: Int,
)