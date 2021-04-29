package com.example.giftplanner.ui.recipientslist.addedit

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftplanner.data.Entity.Present
import com.example.giftplanner.data.Entity.Recipient
import com.example.giftplanner.data.dao.PlanDao
import com.example.giftplanner.data.dao.RecipientDao
import com.example.giftplanner.ui.*
import com.example.giftplanner.ui.planslist.PlansListViewModel
import com.example.giftplanner.ui.presentslist.addedit.AddEditPresentViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditRecipientViewModel @ViewModelInject constructor(
    private val recipientDao: RecipientDao,
    @Assisted private val state: SavedStateHandle,
) : ViewModel()  {

    private val recipient = state.get<Recipient>("recipient")

    val isDeleteAllow: Boolean = recipient != null

    var name: String = state.get<String>("nameRecipient") ?: recipient?.name ?: ""
        set(value) {
            field = value
            state.set("nameRecipient", value)
        }

    private val eventChannel = Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    fun onConfirmButtonClick() {
        if (name.isBlank()) {
            showInvalidInputMessage("Название не может быть пустым")
            return
        }

        if (recipient != null) {

            val updatedRecipient = recipient.copy(
                    name = name
            )

            updatePresent(updatedRecipient)

        } else {

            val newRecipient = Recipient(
                    name = name
            )

            createPresent(newRecipient)

        }
    }

    fun onDeleteMenuClick() {
        deleteRecipient(recipient!!)
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        eventChannel.send(Event.ShowInvalidInputMessage(text))
    }

    private fun updatePresent(recipient: Recipient) = viewModelScope.launch {
        recipientDao.update(recipient)
        eventChannel.send(Event.NavigateBackWithResult(EDIT_RECIPIENT_RESULT_OK))
    }

    private fun createPresent(recipient: Recipient) = viewModelScope.launch {
        recipientDao.insert(recipient)
        eventChannel.send(Event.NavigateBackWithResult(ADD_RECIPIENT_RESULT_OK))
    }

    private fun deleteRecipient(recipient: Recipient) = viewModelScope.launch {
        recipientDao.delete(recipient)
        eventChannel.send(Event.NavigateBackWithResult(DELETE_RECIPIENT_RESULT_OK))
    }

    sealed class Event {
        data class NavigateBackWithResult(val result: Int) : Event()
        data class ShowInvalidInputMessage(val msg: String) : Event()
    }
}