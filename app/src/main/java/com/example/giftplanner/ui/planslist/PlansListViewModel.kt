package com.example.giftplanner.ui.planslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class PlansListViewModel @ViewModelInject constructor(

) : ViewModel() {

    private val eventChannel = Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    sealed class Event {

    }
}