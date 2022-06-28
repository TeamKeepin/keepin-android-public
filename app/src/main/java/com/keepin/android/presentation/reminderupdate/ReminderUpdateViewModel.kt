package com.keepin.android.presentation.reminderupdate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.entity.request.RequestPostReminder
import com.keepin.android.data.entity.response.ReminderDetailData
import com.keepin.android.data.repository.ReminderUpdateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class ReminderUpdateViewModel @Inject constructor(
    private val reminderUpdateRepository: ReminderUpdateRepository
) : ViewModel() {
    private val _isUpdate = MutableLiveData(false)
    val isUpdate: LiveData<Boolean> = _isUpdate

    private val _detailReminderData = MutableLiveData<ReminderDetailData>()
    val detailReminderData: LiveData<ReminderDetailData> = _detailReminderData

    val reminderTitleAtEditText = MutableLiveData("")

    val isAlarm = MutableLiveData<Boolean>()

    private val _selectedEventDate = MutableLiveData("")

    val selectedEventDate: LiveData<String> = _selectedEventDate

    private var networkDate = ""

    private val _selectedAlarmTime = MutableLiveData("")
    val selectedAlarmTime: LiveData<String> = _selectedAlarmTime

    private var networkAlarmTime: Int = 0

    fun setIsUpdate(isUpdate: Boolean) {
        _isUpdate.value = isUpdate
    }

    fun setReminderTitleAtEditText(title: String) {
        reminderTitleAtEditText.value = title
    }

    fun setSelectedEventDate(eventDate: String, networkDate: String) {
        _selectedEventDate.value = eventDate
        this.networkDate = networkDate
    }

    fun setSelectedAlarmTime(alarmTime: String) {
        _selectedAlarmTime.value = alarmTime
        networkAlarmTime = when (selectedAlarmTime.value.toString()) {
            "당일" -> 0
            "1일 전" -> 1
            "2일 전" -> 2
            "3일 전" -> 3
            "1주일 전" -> 7
            else -> throw IllegalArgumentException()
        }
    }

    fun clearDetailReminderData() {
        _detailReminderData.value = ReminderDetailData(
            date = selectedEventDate.value.toString(),
            id = "",
            isAlarm = false,
            isImportant = false,
            title = "",
            daysAgo = null
        )
    }

    fun getReminderDetail(reminderId: String) {
        if (reminderId != "") {
            viewModelScope.launch {
                reminderUpdateRepository.getReminderDetail(reminderId)?.let { reminderDetail ->
                    _detailReminderData.postValue(reminderDetail)
                    isAlarm.postValue(reminderDetail.isAlarm)
                    reminderTitleAtEditText.postValue(reminderDetail.title)
                    _selectedEventDate.postValue(reminderDetail.date)
                    networkDate = reminderDetail.date.replace(".", "-")
                    _selectedAlarmTime.postValue(
                        when (reminderDetail.daysAgo) {
                            0 -> "당일"
                            1 -> "1일 전"
                            2 -> "2일 전"
                            3 -> "3일 전"
                            7 -> "7일 전"
                            else -> "당일"
                        }
                    )
                }
            }
        }
    }

    fun postReminder(isAlarm: Boolean, isImportant: Boolean) {
        viewModelScope.launch {
            reminderUpdateRepository.postReminder(
                RequestPostReminder(
                    title = reminderTitleAtEditText.value.toString(),
                    date = networkDate,
                    isAlarm = isAlarm,
                    isImportant = isImportant,
                    daysAgo = when (isAlarm) {
                        true -> networkAlarmTime.toString()
                        else -> null
                    }
                )
            )
        }
    }

    fun putReminder(reminderId: String, isAlarm: Boolean, isImportant: Boolean) {
        viewModelScope.launch {
            reminderUpdateRepository.putReminder(
                body = RequestPostReminder(
                    title = reminderTitleAtEditText.value.toString(),
                    date = networkDate,
                    isAlarm = isAlarm,
                    isImportant = isImportant,
                    daysAgo = when (isAlarm) {
                        true -> networkAlarmTime.toString()
                        else -> null
                    }
                ),
                reminderId = reminderId
            )
        }
    }
}
