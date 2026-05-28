package com.example.myproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myproject.data.local.entity.PlanActionEntity
import com.example.myproject.data.local.dao.model.ActionItemQueryModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(planAction: PlanActionEntity): Long

    @Query("SELECT * FROM plan_action")
    fun getAllPlanActions(): Flow<List<PlanActionEntity>>

    @Query("SELECT * FROM plan_action WHERE plan_id = :planId AND action_id = :actionId LIMIT 1")
    suspend fun findPlanAction(planId: String, actionId: String): PlanActionEntity?

    @Query("SELECT * FROM plan_action WHERE plan_id = :planId")
    suspend fun getPlanActionsForPlan(planId: String): List<PlanActionEntity>

    @Query("""
        SELECT 
            pa.action_id AS action_id,
            a.name AS title,
            a.duration_seconds AS duration_seconds,
            pa.`groups` AS `groups`,
            pa.times AS times,
            tsap.completed_groups AS completed_groups
            
        FROM plan_action pa
        INNER JOIN `action` a ON a._id = pa.action_id
        LEFT JOIN training_session ts 
            ON ts.user_id = :userId 
           AND ts.plan_id = pa.plan_id 
           AND ts.train_date = :todayDateStr
        LEFT JOIN training_session_action_progress tsap 
            ON tsap.training_session_id = ts._id 
           AND tsap.action_id = pa.action_id
        WHERE pa.plan_id = :planId
        ORDER BY pa._id ASC
    """)
    fun getActionItemsProgress(userId: String, planId: String, todayDateStr: String): Flow<List<ActionItemQueryModel>>
}
