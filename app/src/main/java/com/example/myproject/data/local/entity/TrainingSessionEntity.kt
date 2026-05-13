package com.example.myproject.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.myproject.data.local.entity.PlanEntity
import com.example.myproject.data.local.entity.UserEntity

@Entity(
    tableName = "training_session",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = PlanEntity::class,
            parentColumns = ["_id"],
            childColumns = ["plan_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        // 核心唯一索引：每个用户、每个方案、每天只有一条会话
        Index(value = ["user_id", "plan_id", "train_date"], unique = true),
        // 高频查询索引：查询用户某天的所有方案会话
        Index(value = ["user_id", "train_date"]),
        Index("plan_id")
    ]
)
data class TrainingSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val _id: Long = 0,

    /** 归属用户ID */
    @ColumnInfo(name = "user_id")
    val userId: String,

    /** 关联的方案ID */
    @ColumnInfo(name = "plan_id")
    val planId: String,

    /** 训练日期（格式：yyyy-MM-dd，按自然天归属） */
    @ColumnInfo(name = "train_date")
    val trainDate: String,

    /** 当日方案总组数（创建时从PlanActionEntity计算并缓存） */
    @ColumnInfo(name = "today_total_groups")
    val todayTotalGroups: Int,

    /** 当日已完成的总组数 */
    @ColumnInfo(name = "total_completed_groups")
    val totalCompletedGroups: Int = 0,


//    /** 【缓存】当日训练进度（0-100），避免每次实时计算 */
//    @ColumnInfo(name = "cached_progress")
//    val cachedProgress: Float = 0f,

    /** 今日方案是否已完成（用户确认后设为true） */
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    /** 数据创建时间戳 */
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    /** 最后更新时间戳 */
    @ColumnInfo(name = "last_updated_at")
    val lastUpdatedAt: Long = System.currentTimeMillis(),

//    /** 同步状态 */
//    @ColumnInfo(name = "sync_status")
//    val syncStatus: Int = SyncStatus.PENDING,

//    /** 后端ID映射 */
//    @ColumnInfo(name = "server_id")
//    val serverId: String? = null,

    //    /** 【进度恢复】当前正在进行的动作ID（null表示未开始/已完成） */
//    @ColumnInfo(name = "current_action_id")
//    val currentActionId: String? = null,
//
//    /** 【进度恢复】当前正在进行的组号（从1开始，null表示未开始/已完成） */
//    @ColumnInfo(name = "current_group_number")
//    val currentGroupNumber: Int? = null,
//
//    /** 【配置沿用】当日方案配置快照（JSON格式，存储创建时的PlanAction配置） */
//    @ColumnInfo(name = "plan_config_snapshot")
//    val planConfigSnapshot: String,


//    /** 软删除标记 */
//    @ColumnInfo(name = "is_deleted")
//    val isDeleted: Boolean = false
) {
    companion object {
        object SyncStatus {
            const val SYNCED = 0
            const val PENDING = 1
        }
    }
}