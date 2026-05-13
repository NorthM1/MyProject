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
    fun insert(progress: TrainingSessionActionProgressEntity)

    @Query("SELECT * FROM training_session_action_progress")
    fun getAllProgress(): Flow<List<TrainingSessionActionProgressEntity>>
}
