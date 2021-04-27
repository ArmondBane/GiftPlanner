package com.example.giftplanner.ui.planslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.giftplanner.data.Entity.Plan
import com.example.giftplanner.data.dao.PlanDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PlansListViewModel @ViewModelInject constructor(
    private val planDao: PlanDao
) : ViewModel() {

    val data = planDao.getAllPlans().asLiveData()

    private val eventChannel = Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    fun onPlanItemClick(plan: Plan) = viewModelScope.launch {
        eventChannel.send(Event.NavigateToEditPlan(plan))
    }

    fun onAddButtonClick() = viewModelScope.launch {
        eventChannel.send(Event.NavigateToAddPlan)
    }

    sealed class Event {
        object NavigateToAddPlan : Event()
        data class NavigateToEditPlan(val plan: Plan) : Event()
    }
}