package com.example.myproject.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "training_session_action_progress",
    foreignKeys = [
        ForeignKey(
            entity = TrainingSessionEntity::class,
            parentColumns = ["_id"],
            childColumns = ["training_session_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = ActionEntity::class,
            parentColumns = ["_id"],
            childColumns = ["action_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        // 核心唯一索引：每个动作在同一会话中只有一条进度
        Index(value = ["training_session_id", "action_id"], unique = true),
        Index("training_session_id"),
        Index("action_id")
    ]
)
data class TrainingSessionActionProgressEntity(
    /** 主键 ID，自增 */
    @PrimaryKey(autoGenerate = true)
    val _id: Long = 0,

    /** 关联的训练会话ID */
    @ColumnInfo(name = "training_session_id")
    val trainingSessionId: Long,

    /** 关联的动作ID */
    @ColumnInfo(name = "action_id")
    val actionId: String,

    /** 该动作已完成的组数 */
    @ColumnInfo(name = "completed_groups")
    val completedGroups: Int = 0,

//    /** 该动作已跳过的组数 */
//    @ColumnInfo(name = "skipped_groups")
//    val skippedGroups: Int = 0,

    /** 数据创建时间戳 */
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    /** 最后更新时间戳 */
    @ColumnInfo(name = "last_updated_at")
    val lastUpdatedAt: Long = System.currentTimeMillis()
)