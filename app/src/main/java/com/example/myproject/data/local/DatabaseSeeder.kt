package com.example.myproject.data.local

import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myproject.data.local.testdata.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 用于在数据库首次创建时插入初始测试数据的回调
 * 如果不需要该功能，可以直接从 [AppDatabase.getInstance] 构建器中移除对此回调的调用
 */
class DatabaseSeeder(private val dbProvider: () -> AppDatabase) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            val database = dbProvider()

            // 注意：这部分需要您在 AppDatabase 中声明相应的获取 Dao 的抽象方法，并在各个 Dao 中提供 insert 方法。
            val userDao = database.userDao()
            val actionDao = database.actionDao()
            val actionStepDao = database.actionStepDao()
            val planDao = database.planDao()
            val planActionDao = database.planActionDao()
            val userPlanDao = database.userPlanDao()
            val trainingSessionDao = database.trainingSessionDao()
            val trainingSessionActionProgressDao = database.trainingSessionActionProgressDao()
            val trainingActionRecordDao = database.trainingActionRecordDao()

            // 1. 插入用户数据
            userDao.insert(TestUserEntity.getTestUserEntity())

            // 2. 插入动作库数据
            TestActionEntity.getTestActionList().forEach { actionDao.insert(it) }

            // 3. 插入动作步骤数据
            TestActionStepEntity.getTestActionStepList().forEach { actionStepDao.insert(it) }

            // 4. 插入方案模板数据
            TestPlanEntity.getTestPlanList().forEach { planDao.insert(it) }

            // 5. 插入方案-动作关联数据
            TestPlanActionEntity.getTestPlanActionList().forEach { planActionDao.insert(it) }

            // 6. 插入用户-方案绑定数据
            TestUserPlanEntity.getTestUserPlanList().forEach { userPlanDao.insert(it) }

            // 7. 插入训练会话数据
            TestTrainingSessionEntity.getTestTrainingSessionList().forEach { trainingSessionDao.insert(it) }

            // 8. 插入训练动作进度数据
            TestTrainingSessionActionProgressEntity.getTestProgressList().forEach { trainingSessionActionProgressDao.insert(it) }

            // 9. 插入训练动作组记录数据
            TestTrainingActionRecordEntity.getTestRecordList().forEach { trainingActionRecordDao.insert(it) }
        }
    }
}
