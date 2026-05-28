package com.example.myproject.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.databinding.ItemTodayTaskBinding
import com.example.myproject.ui.home.TodayTask

class TodayTaskAdapter : ListAdapter<TodayTask, TodayTaskAdapter.TaskViewHolder>(object: DiffUtil.ItemCallback<TodayTask>() {
    override fun areItemsTheSame(
        oldItem: TodayTask,
        newItem: TodayTask
    ): Boolean {
        return oldItem.actionId == newItem.actionId
    }

    override fun areContentsTheSame(
        oldItem: TodayTask,
        newItem: TodayTask
    ): Boolean {
        return oldItem == newItem
    }
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTodayTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position), position + 1)
    }

    class TaskViewHolder(private val itemBinding: ItemTodayTaskBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(task: TodayTask, order: Int) {
            itemBinding.tvOrder.text = order.toString()
            itemBinding.tvActionName.text = task.name
            itemBinding.tvActionMeta.text = task.meta

            if (task.isCompleted) {
                itemBinding.tvStatus.text = "已完成"
            } else {
                itemBinding.tvStatus.text = "待完成"
            }
        }
    }
}