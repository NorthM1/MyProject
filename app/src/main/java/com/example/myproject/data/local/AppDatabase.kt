package com.example.myproject.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myproject.data.local.dao.*
import com.example.myproject.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        ActionEntity::class,
        ActionStepEntity::class,
        PlanEntity::class,
        PlanActionEntity::class,
        UserPlanEntity::class,
        TrainingSessionEntity::class,
        TrainingSessionActionProgressEntity::class,
        TrainingActionRecordEntity::class
    ],
    version = 1,
    exportSchema = false   // 导出 schema 到 JSON，便于后续 Migration 追踪
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun actionDao(): ActionDao
    abstract fun actionStepDao(): ActionStepDao
    abstract fun planDao(): PlanDao
    abstract fun planActionDao(): PlanActionDao
    abstract fun userPlanDao(): UserPlanDao
    abstract fun trainingSessionDao(): TrainingSessionDao
    abstract fun trainingSessionActionProgressDao(): TrainingSessionActionProgressDao
    abstract fun trainingActionRecordDao(): TrainingActionRecordDao

    companion object {
        private const val DB_NAME = "MyDatabase.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val dbBuilder = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                // 首次创建数据库时注入初始数据
                dbBuilder.addCallback(DatabaseSeeder { INSTANCE!! })
                INSTANCE ?: dbBuilder
                    .build()
                    .also { INSTANCE = it }
            }
    }
}