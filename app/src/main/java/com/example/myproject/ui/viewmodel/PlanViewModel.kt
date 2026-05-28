package com.example.myproject.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.MyApplication
import com.example.myproject.data.local.entity.UserPlanEntity
import com.example.myproject.repository.PlanRepository
import com.example.myproject.ui.model.PlanItemModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlanViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlanRepository
    private val _plans = MutableStateFlow<List<PlanItemModel>>(emptyList())
    val plans: StateFlow<List<PlanItemModel>> = _plans.asStateFlow()

    init {
        repository = PlanRepository(application)
        val userId = (application as MyApplication).userId
        if (userId != null) {
            fetchPlans(userId)
        }
    }

    private fun fetchPlans(userId: String) {
        viewModelScope.launch {
            repository.getPlanItemModel(userId).collect { planList -> _plans.value = planList }
        }
    }
}