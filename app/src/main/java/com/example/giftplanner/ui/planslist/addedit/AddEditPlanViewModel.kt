package com.example.giftplanner.ui.planslist.addedit

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.giftplanner.data.Entity.Plan
import com.example.giftplanner.data.dao.PlanDao
import com.example.giftplanner.data.dao.PresentDao
import com.example.giftplanner.data.dao.RecipientDao
import com.example.giftplanner.ui.ADD_PLAN_RESULT_OK
import com.example.giftplanner.ui.EDIT_PLAN_RESULT_OK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddEditPlanViewModel @ViewModelInject constructor(
    private val planDao: PlanDao,
    private val presentDao: PresentDao,
    private val recipientDao: RecipientDao,
    @Assisted private val state: SavedStateHandle,
) : ViewModel()  {

    private val plan = state.get<Plan>("plan")

    @ExperimentalCoroutinesApi
    val presentList = presentDao.getAllPresents().flatMapLatest { presentList ->
        val list: MutableList<String> = arrayListOf()
        presentList.forEach {
            list.add(it.name)
        }
        return@flatMapLatest list.asFlow()
    }.asLiveData().value as MutableList<*>

    @ExperimentalCoroutinesApi
    val recipientList = recipientDao.getAllRecipients().flatMapLatest { recipientList ->
        val list: MutableList<String> = arrayListOf()
        recipientList.forEach {
            list.add(it.name)
        }
        return@flatMapLatest list.asFlow()
    }.asLiveData().value as MutableList<*>

    var holidayName: String = state.get<String>("holidayName") ?: plan?.holidayName ?: ""
        set(value) {
            field = value
            state.set("holidayName", value)
        }

    var presentId: Int = state.get<Int>("presentId") ?: plan?.present_id ?: 0
        set(value) {
            field = value
            state.set("presentId", value)
        }

    var recipientId: Int = state.get<Int>("recipientId") ?: plan?.recipient_id ?: 0
        set(value) {
            field = value
            state.set("recipientId", value)
        }

    var dateLong: Long = plan?.date ?: System.currentTimeMillis()
    var dateYear: Int = plan?.dateFormattedAsYear?.toInt() ?:
    SimpleDateFormat("yyyy", Locale.getDefault()).format(Date(dateLong)).toInt()
    var dateMonth: Int = plan?.dateFormattedAsMonth?.toInt() ?:
    SimpleDateFormat("MM", Locale.getDefault()).format(Date(dateLong)).toInt()
    var dateDay: Int = plan?.dateFormattedAsDay?.toInt() ?:
    SimpleDateFormat("dd", Locale.getDefault()).format(Date(dateLong)).toInt()
    var dateString: String = state.get<String>("momentDate") ?: plan?.dateFormatted ?:
    SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(dateLong))
        set(value) {
            field = value
            state.set("momentDate", value)
        }

    private val eventChannel = Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    fun onDateChanged() = viewModelScope.launch {
        eventChannel.send(Event.ChangeDateView)
    }

    fun onConfirmButtonClick() {
        if (holidayName.isBlank()) {
            showInvalidInputMessage("Название не может быть пустым")
            return
        }

        if (plan != null) {

            val updatePlan = plan.copy(
                holidayName = holidayName,
                date = dateLong,
                present_id = presentId,
                recipient_id = recipientId
            )

            updateMoment(updatePlan)

        } else {

            val newPlan = Plan(
                holidayName = holidayName,
                date = dateLong,
                present_id = presentId,
                recipient_id = recipientId
            )

            createMoment(newPlan)
        }
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        eventChannel.send(Event.ShowInvalidInputMessage(text))
    }

    private fun updateMoment(plan: Plan) = viewModelScope.launch {
        planDao.update(plan)
        eventChannel.send(Event.NavigateBackWithResult(EDIT_PLAN_RESULT_OK))
    }

    private fun createMoment(plan: Plan) = viewModelScope.launch {
        planDao.insert(plan)
        eventChannel.send(Event.NavigateBackWithResult(ADD_PLAN_RESULT_OK))
    }

    sealed class Event {
        object ChangeDateView : Event()
        data class ShowInvalidInputMessage(val msg: String) : Event()
        data class NavigateBackWithResult(val result: Int) : Event()
    }
}