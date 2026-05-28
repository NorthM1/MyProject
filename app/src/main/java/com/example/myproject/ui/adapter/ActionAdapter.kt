package com.example.myproject.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.databinding.ItemActionBinding
import com.example.myproject.ui.model.ActionItemModel

class ActionAdapter(private val onItemClick: ((ActionItemModel) -> Unit)? = null) : ListAdapter<ActionItemModel, ActionViewHolder>(object: DiffUtil.ItemCallback<ActionItemModel>(){
    override fun areItemsTheSame(
        p0: ActionItemModel,
        p1: ActionItemModel
    ): Boolean {
        return p0.actionId == p1.actionId
    }

    override fun areContentsTheSame(
        p0: ActionItemModel,
        p1: ActionItemModel
    ): Boolean {
        return p0 == p1
    }
}){
    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): ActionViewHolder {
        val binding= ItemActionBinding.inflate(LayoutInflater.from(p0.context),p0,false)
        return ActionViewHolder(binding)
    }

    override fun onBindViewHolder(
        p0: ActionViewHolder,
        p1: Int
    ) {
        val item = getItem(p1)
        p0.bind(item)
        p0.itemView.setOnClickListener { onItemClick?.invoke(item) }
    }

    // ...existing code...
}

class ActionViewHolder(private val binding: ItemActionBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ActionItemModel) {
        // 序号
        binding.tvStepNumber.text = item.stepNumber.toString()

        // 标题
        binding.tvActionTitle.text = item.title

        // 计算时长分钟数（向下取整），UI 可自行进一步格式化
        val durationMinutes = item.durationSeconds / 60

        // 还剩组数（不得为负）
        val remainingGroups = (item.groups - item.completedGroups).coerceAtLeast(0)

        // 描述文本：以原生数字为主，尽量不做复杂拼接，便于上层按需求拼装
        binding.tvActionDesc.text = "${item.groups}组 × ${item.times}次 · 每组约 $durationMinutes 分钟 · 还剩 $remainingGroups 组"

        // 进度条：基于已完成组数 / 总组数，避免除以 0
        val progress = if (item.groups > 0) {
            ((item.completedGroups * 100) / item.groups).coerceIn(0, 100)
        } else 0
        binding.pbAction.progress = progress
    }
}
