package com.example.myproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myproject.data.local.entity.ActionStepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionStepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(step: ActionStepEntity)

    @Query("SELECT * FROM action_step")
    fun getAllActionSteps(): Flow<List<ActionStepEntity>>
}
