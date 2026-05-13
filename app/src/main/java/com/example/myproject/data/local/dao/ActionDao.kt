package com.example.myproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myproject.data.local.entity.ActionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(action: ActionEntity)

    @Query("SELECT * FROM `action`")
    fun getAllActions(): Flow<List<ActionEntity>>
}
