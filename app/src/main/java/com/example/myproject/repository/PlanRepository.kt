package com.example.myproject.repository

import android.content.Context
import com.example.myproject.data.local.AppDatabase
import com.example.myproject.data.local.entity.PlanEntity
import com.example.myproject.data.local.entity.TrainingActionRecordEntity
import com.example.myproject.data.local.entity.TrainingSessionActionProgressEntity
import com.example.myproject.data.local.entity.TrainingSessionEntity
import com.example.myproject.data.local.entity.UserPlanEntity
import com.example.myproject.ui.model.ActionItemModel
import com.example.myproject.ui.model.PlanDetailModel
import com.example.myproject.ui.model.PlanItemModel
import com.example.myproject.ui.home.HomeData
import com.example.myproject.ui.home.TodayTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

class PlanRepository(context: Context) {

    private val database= AppDatabase.getInstance(context)

    private val userDao = database.userDao()
    private val userPlanDao=database.userPlanDao()
    private val planActionDao=database.planActionDao()
    private val trainingSessionDao = database.trainingSessionDao()
    private val progressDao = database.trainingSessionActionProgressDao()
    private val recordDao = database.trainingActionRecordDao()
    fun getAllPlans(userId:String): Flow<List<UserPlanEntity>> = userPlanDao.getAllUserPlans(userId)

    /**
     * 获取首页"今日任务"列表
     * 查找用户进行中的方案，返回该方案今日的动作列表及完成进度
     */
    fun getTodayTasks(userId: String): Flow<List<TodayTask>> {
        val todayDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        return userPlanDao.getActivePlanId(userId).flatMapLatest { activePlanId ->
            if (activePlanId != null) {
                planActionDao.getActionItemsProgress(userId, activePlanId, todayDateStr)
                    .map { actions -> actions.map { it.toTodayTask() } }
            } else {
                flowOf(emptyList())
            }
        }
    }

    /**
     * 完成当前动作的一组训练。
     * @return 更新后该动作的已完成组数
     */
    suspend fun completeActionGroup(userId: String, actionId: String): Int {
        val now = System.currentTimeMillis()
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val activePlanId = userPlanDao.getActivePlanIdOnce(userId)
            ?: throw IllegalStateException("没有进行中的方案")

        // 1. 查找或创建今天的训练会话
        var session = trainingSessionDao.findTodaySession(userId, activePlanId, todayStr)
        if (session == null) {
            val planActions = planActionDao.getPlanActionsForPlan(activePlanId)
            val totalGroups = planActions.sumOf { it.groups }
            session = TrainingSessionEntity(
                userId = userId,
                planId = activePlanId,
                trainDate = todayStr,
                todayTotalGroups = totalGroups,
                totalCompletedGroups = 0,
                isCompleted = false,
                createdAt = now,
                lastUpdatedAt = now
            )
            val newId = trainingSessionDao.insert(session)
            session = session.copy(_id = newId)
        }

        // 2. 查找动作的总组数配置
        val planAction = planActionDao.findPlanAction(activePlanId, actionId)
            ?: throw IllegalStateException("方案中未找到该动作: $actionId")
        val totalGroups = planAction.groups

        // 3. 查找或创建进度记录
        var progress = progressDao.findProgress(session._id, actionId)
        if (progress == null) {
            progress = TrainingSessionActionProgressEntity(
                trainingSessionId = session._id,
                actionId = actionId,
                completedGroups = 0,
                createdAt = now,
                lastUpdatedAt = now
            )
            val newId = progressDao.insert(progress)
            progress = progress.copy(_id = newId)
        }

        val currentCompleted = progress.completedGroups
        if (currentCompleted >= totalGroups) {
            return currentCompleted
        }

        // 4. 已完成的组数 +1
        val newCompleted = currentCompleted + 1
        progressDao.updateCompletedGroups(progress._id, newCompleted, now)

        // 5. 新增完成记录
        recordDao.insert(
            TrainingActionRecordEntity(
                trainingSessionId = session._id,
                actionId = actionId,
                status = TrainingActionRecordEntity.Companion.ActionRecordStatus.COMPLETED,
                createdAt = now
            )
        )

        // 6. 更新会话的总完成组数
        val newTotalCompleted = session.totalCompletedGroups + 1
        val isDone = newTotalCompleted >= session.todayTotalGroups
        trainingSessionDao.updateSessionProgress(session._id, newTotalCompleted, isDone, now)

        return newCompleted
    }

    /**
     * 获取首页头部数据：用户信息、统计数字、banner 文案
     * 合并 user、activePlan、completedSessionCount 三个数据源
     */
    fun getHomeData(userId: String): Flow<HomeData> {
        val userFlow = userDao.getUserById(userId)
        val plansFlow = userPlanDao.getUserPlansWithDetails(userId)
        val checkInDaysFlow = trainingSessionDao.getCompletedSessionCount(userId)

        return combine(userFlow, plansFlow, checkInDaysFlow) { user, plans, checkInDays ->
            val activePlan = plans.firstOrNull { it.status == UserPlanEntity.Companion.Status.IN_PROGRESS }
            val now = System.currentTimeMillis()

            val userName = user?.name ?: ""
            val recoveryDays = if (user != null) {
                ((now - user.surgeryDate) / 86_400_000L).toInt().coerceAtLeast(0)
            } else 0
            val surgeryDayHint = if (recoveryDays > 0) "术后第 $recoveryDays 天 · 继续加油！" else ""

            val (bannerTitle, bannerSubtitle, completionRate, hasActivePlan) = if (activePlan != null) {
                val totalDays = if (activePlan.frequency == PlanEntity.Companion.Frequency.DAILY) {
                    activePlan.totalWeeks * 7
                } else {
                    (activePlan.totalWeeks * 7) / 2
                }
                val rate = if (totalDays > 0) min(100, checkInDays * 100 / totalDays) else 0
                val title = "今日方案待完成"
                val subtitle = "${activePlan.actionCount} 个动作 · 约 ${activePlan.dailyMinutes} 分钟"
                Tuple4(title, subtitle, rate, true)
            } else {
                Tuple4("暂无进行中的方案", "请先选择训练方案", 0, false)
            }

            HomeData(
                userName = userName,
                surgeryDayHint = surgeryDayHint,
                recoveryDays = recoveryDays,
                checkInDays = checkInDays,
                completionRate = completionRate,
                bannerTitle = bannerTitle,
                bannerSubtitle = bannerSubtitle,
                hasActivePlan = hasActivePlan
            )
        }
    }

    private data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

    private fun com.example.myproject.data.local.dao.model.ActionItemQueryModel.toTodayTask() = TodayTask(
        actionId = actionId,
        name = title,
        meta = "${groups} 组 × ${times} 次",
        completedGroups = completedGroups ?: 0,
        totalGroups = groups
    )

    /**
     * 获取用户所有方案并转化为 UI 所需的 PlanItemModel
     * 使用 Flow 实时更新数据
     */
    fun getPlanItemModel(userId: String): Flow<List<PlanItemModel>> {
        return userPlanDao.getUserPlansWithDetails(userId).map { list ->
            list.map { detail ->
                val totalDays = if (detail.frequency == PlanEntity.Companion.Frequency.DAILY) {
                    detail.totalWeeks * 7
                } else {
                    (detail.totalWeeks * 7) / 2
                }
                val remainTimes = if (detail.startDate == null) {
                    totalDays
                } else {
                    max(0, totalDays - detail.completedDays)
                }
                val progress = if (detail.startDate == null) {
                    0
                } else {
                    min(100, if (totalDays > 0) detail.completedDays * 100 / totalDays else 0)
                }

                PlanItemModel(
                    userId = detail.userId,
                    planId = detail.planId,
                    _id = detail.id,
                    title = detail.title,
                    status = detail.status,
                    isPreset = detail.isPreset,
                    totalWeeks = detail.totalWeeks,
                    frequency = detail.frequency,
                    isFavorite = detail.isFavorite,
                    progress = progress,
                    dailyMinutes = detail.dailyMinutes,
                    actionCount = detail.actionCount,
                    remainTimes = remainTimes
                )
            }
        }
    }

    /**
     * 获取方案详情页的所有数据并将流合并为最新状态
     */
    fun getPlanDetailFlow(userId: String, planId: String): Flow<PlanDetailModel> {
        val todayDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val planInfoFlow = userPlanDao.getPlanDetailInfo(userId, planId, todayDateStr).filterNotNull()
        val actionsFlow = planActionDao.getActionItemsProgress(userId, planId, todayDateStr)

        return planInfoFlow.combine(actionsFlow) { detailInfo, actions ->

            val totalDays = if (detailInfo.frequency == PlanEntity.Companion.Frequency.DAILY) {
                detailInfo.totalWeeks * 7
            } else {
                (detailInfo.totalWeeks * 7) / 2
            }
            val remainDays = max(0, totalDays - detailInfo.completedDays)
            val progress = min(100, if (totalDays > 0) detailInfo.completedDays * 100 / totalDays else 0)

            val actionItemModels = actions.mapIndexed { index, actionInfo ->
                ActionItemModel(
                    actionId = actionInfo.actionId,
                    stepNumber = index + 1, // 动态给上序号
                    title = actionInfo.title,
                    durationSeconds = actionInfo.durationSeconds,
                    groups = actionInfo.groups,
                    times = actionInfo.times,
                    completedGroups = actionInfo.completedGroups ?: 0
                )
            }

            PlanDetailModel(
                planId = detailInfo.planId,
                userId = detailInfo.userId,
                title = detailInfo.title,
                status = detailInfo.status,
                isPreset = detailInfo.isPreset,
                totalWeeks = detailInfo.totalWeeks,
                dailyMinutes = detailInfo.dailyMinutes,
                progress = progress,
                actionCount = detailInfo.actionCount,
                remainDays = remainDays,
                todayCompletedGroups = detailInfo.todayCompletedGroups ?: 0,
                todayTotalGroups = detailInfo.todayTotalGroups ?: 0,
                actionList = actionItemModels
            )
        }
    }
}