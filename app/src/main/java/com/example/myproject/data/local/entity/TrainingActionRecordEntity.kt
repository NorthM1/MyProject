package com.example.myproject.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "training_action_record",
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
        Index("training_session_id"),
        Index("action_id"),
        Index(value = ["training_session_id", "action_id", "created_at"], unique = true),
        Index("created_at")
    ]
)
data class TrainingActionRecordEntity(
    /** 主键 ID */
    @PrimaryKey(autoGenerate = true)
    val _id: Long=0,

    /** 关联的训练会话ID */
    @ColumnInfo(name = "training_session_id")
    val trainingSessionId: Long,

    /** 关联的动作ID */
    @ColumnInfo(name = "action_id")
    val actionId: String,

    /** 该组的状态：0=完成，1=跳过 */
    @ColumnInfo(name = "status")
    val status: Int = ActionRecordStatus.COMPLETED,

    /** 这一组完成的时间戳 */
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

//    /** 该动作实际完成的次数 */
//    @ColumnInfo(name = "actual_reps")
//    val actualReps: Int? = 0,
//
//    /** 该动作跳过的次数 */
//    @ColumnInfo(name = "skip_reps")
//    val skipReps: Int? = 0,
//
//    /** 该组的预定次数（记录当时的配置） */
//    @ColumnInfo(name = "planned_reps")
//    val plannedReps: Int,



    //    /** 该组的组号（从1开始） */
//    @ColumnInfo(name = "group_number")
//    val groupNumber: Int,
//

//    /** 最后更新时间戳 */
//    @ColumnInfo(name = "last_updated_at")
//    val lastUpdatedAt: Long = System.currentTimeMillis(),

//    /** 后端ID映射 */
//    @ColumnInfo(name = "server_id")
//    val serverId: String? = null,
//
//    /** 同步状态 */
//    @ColumnInfo(name = "sync_status")
//    val syncStatus: Int = SyncStatus.PENDING,
//
//    /** 软删除标记 */
//    @ColumnInfo(name = "is_deleted")
//    val isDeleted: Boolean = false
)
{
    companion object {
        object ActionRecordStatus {
            const val COMPLETED = 0
            const val SKIPPED = 1
        }

        object SyncStatus {
            const val SYNCED = 0
            const val PENDING = 1
        }
    }
}