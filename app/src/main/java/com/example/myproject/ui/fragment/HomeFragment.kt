package com.example.myproject.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.base.BaseFragment
import com.example.myproject.databinding.FragmentHomeBinding
import com.example.myproject.ui.adapter.TodayTaskAdapter
import com.example.myproject.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()
    private val taskAdapter by lazy { TodayTaskAdapter() }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.rvTodayTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
    }

    override fun initData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.homeData.collect { data ->
                        binding.tvGreeting.text = greetingText()
                        binding.tvUserName.text = data.userName
                        binding.tvSurgeryDayHint.text = data.surgeryDayHint
                        binding.tvRecoveryDays.text = data.recoveryDays.toString()
                        binding.tvCheckInDays.text = data.checkInDays.toString()
                        binding.tvCompletionRate.text = "${data.completionRate}%"
                        binding.tvBannerTitle.text = data.bannerTitle
                        binding.tvBannerSubtitle.text = data.bannerSubtitle
                    }
                }
                launch {
                    viewModel.todayTasks.collect { tasks ->
                        taskAdapter.submitList(tasks)
                        if (tasks.isEmpty()) {
                            binding.tvEmptyTask.visibility = View.VISIBLE
                            binding.rvTodayTasks.visibility = View.GONE
                        } else {
                            binding.tvEmptyTask.visibility = View.GONE
                            binding.rvTodayTasks.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun greetingText(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour in 5..8 -> "早上好，"
            hour in 9..11 -> "上午好，"
            hour in 12..13 -> "中午好，"
            hour in 14..17 -> "下午好，"
            else -> "晚上好，"
        }
    }
}