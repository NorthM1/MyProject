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
    fun insert(session: TrainingSessionEntity)

    @Query("SELECT * FROM training_session")
    fun getAllTrainingSessions(): Flow<List<TrainingSessionEntity>>
}
