package com.example.giftplanner.ui.presentslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.giftplanner.data.Entity.Plan
import com.example.giftplanner.data.Entity.Present
import com.example.giftplanner.data.dao.PresentDao
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

    sealed class Event {
        object NavigateToAddPresent : Event()
        data class NavigateToEditPresent(val present: Present) : Event()
    }
}