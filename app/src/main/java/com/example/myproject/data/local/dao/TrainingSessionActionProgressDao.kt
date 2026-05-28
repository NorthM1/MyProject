package com.example.myproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myproject.data.local.entity.TrainingSessionActionProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingSessionActionProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(progress: TrainingSessionActionProgressEntity): Long

    @Query("SELECT * FROM training_session_action_progress")
    fun getAllProgress(): Flow<List<TrainingSessionActionProgressEntity>>

    @Query("SELECT * FROM training_session_action_progress WHERE training_session_id = :sessionId AND action_id = :actionId LIMIT 1")
    suspend fun findProgress(sessionId: Long, actionId: String): TrainingSessionActionProgressEntity?

    @Query("UPDATE training_session_action_progress SET completed_groups = :completed, last_updated_at = :now WHERE _id = :id")
    suspend fun updateCompletedGroups(id: Long, completed: Int, now: Long)
}
