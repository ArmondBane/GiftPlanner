package com.example.giftplanner.ui.recipientslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.giftplanner.data.Entity.Present
import com.example.giftplanner.data.Entity.Recipient
import com.example.giftplanner.data.dao.RecipientDao
import com.example.giftplanner.ui.presentslist.PresentsListViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RecipientsListViewModel @ViewModelInject constructor (
    private val recipientDao: RecipientDao
) : ViewModel() {

    val data = recipientDao.getAllRecipients().asLiveData()

    private val eventChannel = Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    fun onAddButtonClick() = viewModelScope.launch {
        eventChannel.send(Event.NavigateToAddRecipient)
    }

    fun onRecipientItemClick(recipient: Recipient) = viewModelScope.launch {
        eventChannel.send(Event.NavigateToEditRecipient(recipient))
    }

    sealed class Event {
        object NavigateToAddRecipient : Event()
        data class NavigateToEditRecipient(val recipient: Recipient) : Event()
    }
}