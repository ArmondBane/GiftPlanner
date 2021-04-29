package com.example.giftplanner.ui.presentslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.giftplanner.data.Entity.Plan
import com.example.giftplanner.data.Entity.Present
import com.example.giftplanner.data.dao.PresentDao
import com.example.giftplanner.ui.*
import com.example.giftplanner.ui.DELETE_PLAN_RESULT_OK
import com.example.giftplanner.ui.EDIT_PLAN_RESULT_OK
import com.example.giftplanner.ui.planslist.PlansListViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PresentsListViewModel @ViewModelInject constructor(
    private val presentDao: PresentDao
) : ViewModel() {

    val data = presentDao.getAllPresents().asLiveData()

    private val eventChannel = Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    fun onPresentItemClick(present: Present) = viewModelScope.launch {
        eventChannel.send(Event.NavigateToEditPresent(present))
    }

    fun onAddButtonClick() = viewModelScope.launch {
        eventChannel.send(Event.NavigateToAddPresent)
    }

    private fun showEditConfirmationMessage(text: String) = viewModelScope.launch {
        eventChannel.send(Event.ShowEditConfirmationMessage(text))
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_PRESENT_RESULT_OK -> showEditConfirmationMessage("Подарок успешно добавлен")
            EDIT_PRESENT_RESULT_OK -> showEditConfirmationMessage("Подарок успешно обновлен")
            DELETE_PRESENT_RESULT_OK -> showEditConfirmationMessage("Подарок успешно удален")
        }
    }

    sealed class Event {
        object NavigateToAddPresent : Event()
        data class NavigateToEditPresent(val present: Present) : Event()
        data class ShowEditConfirmationMessage(val msg: String) : Event()
    }
}