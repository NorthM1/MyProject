package com.example.myproject.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 用户-方案关联实体类（用户执行的方案进度）
 * 数据库表名：user_plan
 *
 * 核心业务职责：
 * 1. 关联用户与方案模板，存储**用户专属的方案执行进度**
 * 2. 核心进度数据：状态、已完成天数、开始时间、最后训练时间
 * 3. 支持数据同步、收藏排序、去重约束
 *
 * 关联关系：
 * 外键关联 user表(_id) 和 plan表(_id)，级联删除保证数据一致性
 * 唯一约束：禁止同一用户重复添加同一个方案
 */
@Entity(
    tableName = "user_plan",
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
        Index("user_id"),
        Index("plan_id"),
        Index(value = ["user_id", "plan_id","created_at"], unique = true),
        Index(value = ["user_id", "status", "is_favorite"])
    ]
)
data class UserPlanEntity(
    /** 主键ID，自增唯一标识一条用户方案记录 */
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,

    /** 关联的方案模板ID，对应 PlanEntity._id */
    @ColumnInfo(name = "plan_id")
    val planId: String,

    /** 归属用户ID，对应 UserEntity._id */
    @ColumnInfo(name = "user_id")
    val userId: String,

    /**
     * 方案执行状态
     * 0：进行中 | 1：未开始 | 2：已完成
     * 用于方案列表排序和状态筛选
     */
    @ColumnInfo(name = "status")
    val status: Int = Status.NOT_STARTED,

    /** 是否收藏，收藏的方案在列表中优先展示 */
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,

    /**
     * 方案开始执行时间（Unix毫秒时间戳）
     * 为空 = 未开始执行
     * 用于计算：当前训练天数 = (当前时间 - startDate) / 一天毫秒数 + 1
     */
    @ColumnInfo(name = "start_date")
    val startDate: Long? = null,

//    /** 已完成训练天数，统计用户累计完成的训练天数 */
//    @ColumnInfo(name = "complete_days")
//    val completeDays: Int = 0,

    /** 最后一次完成训练的时间戳，为空表示从未完成训练 */
    @ColumnInfo(name = "last_completed_time")
    val lastCompletedTime: Long? = null,

//    /**
//     * 当日完成状态（复用主状态常量）
//     * 0：进行中 | 1：未开始 | 2：今日已完成
//     */
//    @ColumnInfo(name = "today_status")
//    val todayStatus: Int = Status.NOT_STARTED,

    /** 数据创建时间戳，用于记录创建时间 */
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    /** 数据更新时间戳，每次修改后刷新，用于云端增量同步 */
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),

    /**
     * 数据同步状态
     * 0：已同步到云端 | 1：待同步（离线修改/创建）
     */
    @ColumnInfo(name = "sync_status")
    val syncStatus: Int = SyncStatus.PENDING
) {
    companion object {
        /** 方案执行状态常量 */
        object Status {
            const val IN_PROGRESS = 0  // 进行中
            const val NOT_STARTED = 1  // 未开始
            const val COMPLETED = 2    // 已完成
        }

        /** 云端同步状态常量 */
        object SyncStatus {
            const val SYNCED = 0   // 已同步
            const val PENDING = 1  // 待同步
        }
    }
}