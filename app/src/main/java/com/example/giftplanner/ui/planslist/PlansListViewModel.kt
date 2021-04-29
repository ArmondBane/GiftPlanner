package com.example.giftplanner.ui.planslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.giftplanner.data.Entity.Plan
import com.example.giftplanner.data.dao.PlanDao
import com.example.giftplanner.ui.ADD_PLAN_RESULT_OK
import com.example.giftplanner.ui.DELETE_PLAN_RESULT_OK
import com.example.giftplanner.ui.EDIT_PLAN_RESULT_OK
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

    private fun showEditConfirmationMessage(text: String) = viewModelScope.launch {
        eventChannel.send(Event.ShowEditConfirmationMessage(text))
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_PLAN_RESULT_OK -> showEditConfirmationMessage("План успешно добавлен")
            EDIT_PLAN_RESULT_OK -> showEditConfirmationMessage("План успешно обновлен")
            DELETE_PLAN_RESULT_OK -> showEditConfirmationMessage("План успешно удален")
        }
    }

    sealed class Event {
        object NavigateToAddPlan : Event()
        data class NavigateToEditPlan(val plan: Plan) : Event()
        data class ShowEditConfirmationMessage(val msg: String) : Event()
    }
}