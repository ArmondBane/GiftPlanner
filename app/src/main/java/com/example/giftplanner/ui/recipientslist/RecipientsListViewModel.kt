package com.example.giftplanner.ui.recipientslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.giftplanner.data.Entity.Present
import com.example.giftplanner.data.Entity.Recipient
import com.example.giftplanner.data.dao.RecipientDao
import com.example.giftplanner.ui.*
import com.example.giftplanner.ui.EDIT_PLAN_RESULT_OK
import com.example.giftplanner.ui.planslist.PlansListViewModel
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

    private fun showEditConfirmationMessage(text: String) = viewModelScope.launch {
        eventChannel.send(Event.ShowEditConfirmationMessage(text))
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_RECIPIENT_RESULT_OK -> showEditConfirmationMessage("Получатель успешно добавлен")
            EDIT_RECIPIENT_RESULT_OK -> showEditConfirmationMessage("Получатель успешно обновлен")
            DELETE_RECIPIENT_RESULT_OK -> showEditConfirmationMessage("Получатель успешно удален")
        }
    }

    sealed class Event {
        object NavigateToAddRecipient : Event()
        data class NavigateToEditRecipient(val recipient: Recipient) : Event()
        data class ShowEditConfirmationMessage(val msg: String) : Event()
    }
}