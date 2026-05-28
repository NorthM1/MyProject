package com.example.myproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myproject.data.local.dao.model.PlanDetailQueryModel
import com.example.myproject.data.local.dao.model.UserPlanWithDetails
import com.example.myproject.data.local.entity.UserPlanEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userPlan: UserPlanEntity)

    @Query("SELECT * FROM user_plan WHERE user_id=:userId")
    fun getAllUserPlans(userId:String): Flow<List<UserPlanEntity>>

    @Query("""
        SELECT 
            up._id AS _id,
            up.user_id as userId,
            up.plan_id as planId,
            p.name AS title,
            up.status AS status,
            p.is_preset AS isPreset,
            p.total_weeks AS totalWeeks,
            p.frequency AS frequency,
            up.is_favorite AS isFavorite,
            p.daily_minutes AS daily_minutes,
            up.start_date AS start_date,
            (SELECT COUNT(*) FROM training_session ts 
             WHERE ts.user_id = up.user_id 
               AND ts.plan_id = up.plan_id 
               AND ts.is_completed = 1) AS completed_days,
            (SELECT COUNT(*) FROM plan_action pa 
             WHERE pa.plan_id = p._id) AS action_count
        FROM user_plan up
        INNER JOIN `plan` p ON up.plan_id = p._id
        WHERE up.user_id = :userId
    """)
    fun getUserPlansWithDetails(userId: String): Flow<List<UserPlanWithDetails>>

    @Query("SELECT plan_id FROM user_plan WHERE user_id = :userId AND status = ${UserPlanEntity.Companion.Status.IN_PROGRESS} ORDER BY _id ASC LIMIT 1")
    fun getActivePlanId(userId: String): Flow<String?>

    @Query("SELECT plan_id FROM user_plan WHERE user_id = :userId AND status = ${UserPlanEntity.Companion.Status.IN_PROGRESS} ORDER BY _id ASC LIMIT 1")
    suspend fun getActivePlanIdOnce(userId: String): String?

    @Query("""
        SELECT
            up.plan_id AS planId,
            up.user_id AS userId,
            p.name AS title,
            up.status AS status,
            p.is_preset AS isPreset,
            p.total_weeks AS totalWeeks,
            p.frequency AS frequency,
            p.daily_minutes AS daily_minutes,
            
            (SELECT COUNT(*) FROM training_session ts 
             WHERE ts.user_id = up.user_id 
               AND ts.plan_id = up.plan_id 
               AND ts.is_completed = 1) AS completed_days,
               
            (SELECT COUNT(*) FROM plan_action pa 
             WHERE pa.plan_id = up.plan_id) AS action_count,
             
            today_ts.total_completed_groups AS today_completed_groups,
            today_ts.today_total_groups AS today_total_groups
            
        FROM user_plan up
        INNER JOIN `plan` p ON up.plan_id = p._id
        LEFT JOIN training_session today_ts 
            ON today_ts.user_id = up.user_id 
           AND today_ts.plan_id = up.plan_id 
           AND today_ts.train_date = :todayDateStr
        WHERE up.user_id = :userId AND up.plan_id = :planId
    """)
    fun getPlanDetailInfo(userId: String, planId: String, todayDateStr: String): Flow<PlanDetailQueryModel?>
}
