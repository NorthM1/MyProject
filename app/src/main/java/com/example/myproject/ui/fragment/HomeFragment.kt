package com.example.myproject.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.base.BaseFragment
import com.example.myproject.databinding.FragmentHomeBinding
import com.example.myproject.ui.home.TodayTask
import com.example.myproject.ui.adapter.TodayTaskAdapter

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

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
        val dummyTasks = listOf(
            TodayTask(1, "踝泵运动", "3 组 × 15 次", true),
            TodayTask(2, "膝关节屈伸训练", "3 组 × 10 次", false),
            TodayTask(3, "直腿抬高训练", "3 组 × 10 次", false)
        )
        taskAdapter.submitList(dummyTasks)

        if (dummyTasks.isEmpty()) {
            binding.tvEmptyTask.visibility = View.VISIBLE
            binding.rvTodayTasks.visibility = View.GONE
        } else {
            binding.tvEmptyTask.visibility = View.GONE
            binding.rvTodayTasks.visibility = View.VISIBLE
        }
    }
}