package com.example.myproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myproject.data.local.entity.TrainingSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: TrainingSessionEntity): Long

    @Query("SELECT * FROM training_session")
    fun getAllTrainingSessions(): Flow<List<TrainingSessionEntity>>

    @Query("SELECT COUNT(*) FROM training_session WHERE user_id = :userId AND is_completed = 1")
    fun getCompletedSessionCount(userId: String): Flow<Int>

    @Query("SELECT * FROM training_session WHERE user_id = :userId AND plan_id = :planId AND train_date = :trainDate LIMIT 1")
    suspend fun findTodaySession(userId: String, planId: String, trainDate: String): TrainingSessionEntity?

    @Query("UPDATE training_session SET total_completed_groups = :completed, is_completed = :isDone, last_updated_at = :now WHERE _id = :id")
    suspend fun updateSessionProgress(id: Long, completed: Int, isDone: Boolean, now: Long)

    @Query("UPDATE training_session SET today_total_groups = :total, last_updated_at = :now WHERE _id = :id")
    suspend fun updateSessionTotalGroups(id: Long, total: Int, now: Long)
}
