package com.example.giftplanner.ui.presentslist.addedit

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftplanner.data.Entity.Plan
import com.example.giftplanner.data.Entity.Present
import com.example.giftplanner.data.dao.PlanDao
import com.example.giftplanner.data.dao.PresentDao
import com.example.giftplanner.ui.*
import com.example.giftplanner.ui.planslist.PlansListViewModel
import com.example.giftplanner.ui.planslist.addedit.AddEditPlanViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*

class AddEditPresentViewModel @ViewModelInject constructor(
    private val presentDao: PresentDao,
    @Assisted private val state: SavedStateHandle,
) : ViewModel()  {

    private val present = state.get<Present>("present")

    val isDeleteAllow: Boolean = present != null

    var name: String = state.get<String>("namePresent") ?: present?.name ?: ""
        set(value) {
            field = value
            state.set("namePresent", value)
        }

    var cost: Double = state.get<Double>("cost") ?: present?.cost ?: 0.0
        set(value) {
            field = value
            state.set("cost", value)
        }

    private val eventChannel = Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    fun onConfirmButtonClick() {
        if (name.isBlank()) {
            showInvalidInputMessage("Название не может быть пустым")
            return
        }
        if (cost.isNaN()) {
            showInvalidInputMessage("Стоимость не может быть пустой")
            return
        }

        if(present != null) {

            val updatedPresent = present.copy(
                    name = name,
                    cost = cost
            )

            updatePresent(updatedPresent)

        } else {

            val newPresent = Present(
                    name = name,
                    cost = cost
            )

            createPresent(newPresent)
        }
    }

    fun onDeleteMenuClick() {
        deletePresent(present!!)
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        eventChannel.send(Event.ShowInvalidInputMessage(text))
    }

    private fun updatePresent(present: Present) = viewModelScope.launch {
        presentDao.update(present)
        eventChannel.send(Event.NavigateBackWithResult(EDIT_PRESENT_RESULT_OK))
    }

    private fun createPresent(present: Present) = viewModelScope.launch {
        presentDao.insert(present)
        eventChannel.send(Event.NavigateBackWithResult(ADD_PRESENT_RESULT_OK))
    }

    private fun deletePresent(present: Present) = viewModelScope.launch {
        presentDao.delete(present)
        eventChannel.send(Event.NavigateBackWithResult(DELETE_PRESENT_RESULT_OK))
    }

    sealed class Event {
        data class NavigateBackWithResult(val result: Int) : Event()
        data class ShowInvalidInputMessage(val msg: String) : Event()
    }
}