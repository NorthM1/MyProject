package com.example.myproject.ui.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.base.BaseFragment
import com.example.myproject.databinding.FragmentPlanBinding
import com.example.myproject.ui.activity.PlanDetailActivity
import com.example.myproject.ui.adapter.PlanAdapter
import com.example.myproject.ui.viewmodel.PlanViewModel
import com.example.myproject.util.ToastUtil
import kotlinx.coroutines.launch

class PlanFragment : BaseFragment<FragmentPlanBinding>() {

    private val viewModel: PlanViewModel by viewModels()

    private val recyclerView by lazy {
        binding.recyclerview
    }

    private val adapter by lazy {
        PlanAdapter(
            onItemClick = { plan ->
//                ToastUtil.show("点击了方案: ${plan.name}")
                var intent = Intent(activity, PlanDetailActivity::class.java)
                intent.putExtra("planId", plan.planId)
                startActivity(intent)
            },
            onAddClick = {
                ToastUtil.show("点击了添加新训练方案")
            }
        )
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPlanBinding.inflate(inflater, container, false)

    override fun initView() {
        recyclerView.adapter=adapter
        recyclerView.layoutManager= LinearLayoutManager(requireContext())
    }

    override fun initData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.plans.collect { planList ->
                    // Ensure list is applied then scroll to top to avoid initial auto-scrolling to bottom
                    adapter.submitList(planList) {
                        // use post to ensure RecyclerView has measured if needed
                        recyclerView.post {
                            recyclerView.scrollToPosition(0)
                        }
                    }
                }
            }
        }
    }
}