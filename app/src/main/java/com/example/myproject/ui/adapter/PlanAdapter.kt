package com.example.myproject.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.myproject.databinding.ItemPlanAddBinding
import com.example.myproject.databinding.ItemPlanBinding
import com.example.myproject.ui.model.PlanItemModel

class PlanAdapter(
    private val onItemClick: (PlanItemModel) -> Unit,
    private val onAddClick: () -> Unit
) :
    ListAdapter<PlanItemModel, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<PlanItemModel>() {
        override fun areItemsTheSame(oldItem: PlanItemModel, newItem: PlanItemModel): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: PlanItemModel, newItem: PlanItemModel): Boolean {
            return oldItem == newItem
        }
    }) {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_ADD = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size) TYPE_ADD else TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == TYPE_ADD) {
            val binding = ItemPlanAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PlanAddViewHolder(binding).apply {
                itemView.setOnClickListener { onAddClick() }
            }
        }
        val binding = ItemPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding).apply {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is PlanViewHolder) {
             holder.bind(getItem(position), position)
        }
    }
}

class PlanViewHolder(var itemBinding: ItemPlanBinding) : RecyclerView.ViewHolder(itemBinding.root){

    fun bind(plan:PlanItemModel, positon:Int){
        itemBinding.tvTitle.text = plan.title
        itemBinding.tvStatusTag.apply {
            text = when (plan.status) {
                0 -> "进行中"
                1 -> "未开始"
                2 -> "已完成"
                else -> "未知"
            }
            setTextColor(when(plan.status){
                0 -> Color.parseColor("#FFFFA500")
                1 -> Color.parseColor("#FF808080")
                2 -> Color.parseColor("#FF008000")
                else -> Color.parseColor("#FF000000")
            })
        }
        itemBinding.tvSubtitle.apply{
            text=when(plan.isPreset){
                true -> "预置方案 · ${plan.totalWeeks}周计划"
                false -> "自定义方案 · ${plan.totalWeeks}周计划"
            }
        }

        itemBinding.tvProgressText.text="完成进度${plan.progress}%"
        itemBinding.progressBar.progress=plan.progress
        val days:String=when(plan.frequency){
            0->"每日"
            1->"隔日"
            else -> "未知"
        }
        itemBinding.tvBottomDetails.text="${plan.actionCount} 个动作    ${days} ${plan.dailyMinutes} 分钟    ${if (plan.remainTimes > 0) "剩余 ${plan.remainTimes} 次训练" else "已完成"}"
    }
}
class PlanAddViewHolder(var itemBinding: ItemPlanAddBinding) : RecyclerView.ViewHolder(itemBinding.root)
