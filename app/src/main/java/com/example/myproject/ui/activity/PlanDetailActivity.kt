package com.example.myproject.ui.activity

import BaseActivity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.example.myproject.MyApplication
import com.example.myproject.data.local.entity.UserPlanEntity
import com.example.myproject.databinding.ActivityPlanDetailBinding
import com.example.myproject.repository.PlanRepository
import com.example.myproject.ui.adapter.ActionAdapter
import kotlinx.coroutines.launch

class PlanDetailActivity:BaseActivity<ActivityPlanDetailBinding>() {

    private lateinit var planRepository: PlanRepository
    private var planId: String? = null
    private val actionAdapter by lazy {
        ActionAdapter { item ->
            // 点击动作后跳转到 SceneViewActivity，可传入 actionId 或其它信息
            val intent = Intent(this, com.example.myproject.SceneViewActivity::class.java)
            intent.putExtra("actionId", item.actionId)
            intent.putExtra("actionTitle", item.title)
            startActivity(intent)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityPlanDetailBinding {
        return ActivityPlanDetailBinding.inflate(inflater)
    }

    override fun initView() {
        binding.ibBack.setOnClickListener { v-> finish() }
        val bundle=intent.extras
        planId = bundle?.getString("planId", null)
        planRepository = PlanRepository(this)
        // 设置动作列表 adapter
        binding.rvExerciseList.adapter = actionAdapter
    }

    override fun initData() {
        val currentPlanId = planId ?: return
        val currentUserId = (application as MyApplication).userId ?: return

        lifecycleScope.launch {
            planRepository.getPlanDetailFlow(currentUserId, currentPlanId).collect { detailModel ->
                Log.d("log", "Plan Detail Model loaded: $detailModel")
                // 额外打印传入的 userId 与 planId，用于调试
                Log.d("log", "PlanId passed: $currentPlanId, UserId: $currentUserId")

                binding.apply {
                    tvPlanTitle.text = detailModel.title
                    tvStatusBadge.text=when(detailModel.status){
                        UserPlanEntity.Companion.Status.IN_PROGRESS->"进行中"
                        UserPlanEntity.Companion.Status.NOT_STARTED->"未开始"
                        UserPlanEntity.Companion.Status.COMPLETED->"已完成"
                        else -> "未知"
                    }
                    tvPlanInfo.text = "${detailModel.totalWeeks}周 · 每天${detailModel.dailyMinutes}分钟"
                    tvProgressPercent.text = "${detailModel.progress}%"
                    tvActionNums.text = detailModel.actionCount.toString()
                    tvRemainDays.text = detailModel.remainDays.toString()

                    val todayProgress = if (detailModel.todayTotalGroups > 0) {
                        (detailModel.todayCompletedGroups * 100) / detailModel.todayTotalGroups
                    } else {
                        0
                    }
                    tvTodayCompleted.text = "$todayProgress%"
                    pbTodayProgress.progress = todayProgress
                }
                // 更新动作列表
                actionAdapter.submitList(detailModel.actionList)


            }
        }
    }
}