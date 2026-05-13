package com.example.myproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myproject.data.local.entity.TrainingActionRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingActionRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: TrainingActionRecordEntity)

    @Query("SELECT * FROM training_action_record")
    fun getAllRecords(): Flow<List<TrainingActionRecordEntity>>
}
