package com.example.giftplanner.ui.recipientslist.addedit

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.giftplanner.data.dao.PlanDao
import com.example.giftplanner.data.dao.RecipientDao
import com.example.giftplanner.ui.planslist.PlansListViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class AddEditRecipientViewModel @ViewModelInject constructor(
    private val recipientDao: RecipientDao,
    @Assisted private val state: SavedStateHandle,
) : ViewModel()  {

    private val eventChannel = Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    sealed class Event {
        data class NavigateBackWithResult(val result: Int) : Event()
    }
}