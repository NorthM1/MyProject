package com.example.myproject.repository

import android.content.Context
import com.example.myproject.data.local.AppDatabase
import com.example.myproject.data.local.entity.PlanEntity
import com.example.myproject.data.local.entity.UserPlanEntity
import com.example.myproject.ui.model.ActionItemModel
import com.example.myproject.ui.model.PlanDetailModel
import com.example.myproject.ui.model.PlanItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

class PlanRepository(context: Context) {

    private val database= AppDatabase.getInstance(context)

    private val userPlanDao=database.userPlanDao()
    private val planActionDao=database.planActionDao()
    fun getAllPlans(userId:String): Flow<List<UserPlanEntity>> = userPlanDao.getAllUserPlans(userId)

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