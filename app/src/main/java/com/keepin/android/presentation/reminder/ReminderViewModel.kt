package com.keepin.android.presentation.reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.entity.request.RequestDeleteReminder
import com.keepin.android.data.entity.request.RequestModifyReminderAlarm
import com.keepin.android.data.entity.response.MonthReminder
import com.keepin.android.data.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
) : ViewModel() {
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    private val _selectedYear = MutableLiveData(currentYear.toString())
    val selectedYear: LiveData<String> = _selectedYear

    private val _selectedMonth = MutableLiveData(currentMonth.toString())
    val selectedMonth: LiveData<String> = _selectedMonth

    private val _reminderList = MutableLiveData<MutableList<MonthReminder>>()
    val reminderList: LiveData<MutableList<MonthReminder>> = _reminderList

    private val _reminderMonth = MutableLiveData<MonthReminder?>()
    val reminderMonth: LiveData<MonthReminder?> = _reminderMonth

    private val _isEdit = MutableLiveData(false)
    val isEdit: LiveData<Boolean> = _isEdit

    private val _isGoReminderUpdate = MutableLiveData(false)
    val isGoReminderUpdate: LiveData<Boolean> = _isGoReminderUpdate

    private val _deleteList = MutableLiveData(mutableListOf<String>())
    val deleteList: LiveData<MutableList<String>> = _deleteList

    fun setNextYear() {
        if (requireNotNull(selectedYear.value).toInt() > 1900)
            _selectedYear.value = (requireNotNull(selectedYear.value).toInt() + 1).toString()
    }

    fun setLastYear() {
        if (requireNotNull(selectedYear.value).toInt() < 2099)
            _selectedYear.value = (requireNotNull(selectedYear.value).toInt() - 1).toString()
    }

    fun setSelectedMonth(month: Int) {
        if (month < 10) {
            _selectedMonth.value = "0$month"
        } else {
            _selectedMonth.value = month.toString()
        }
    }

    fun setIsEdit(isEdit: Boolean) {
        clearDeleteList()
        _isEdit.value = isEdit
    }

    fun setIsGoReminderUpdate(isGoReminderUpdate: Boolean) {
        _isGoReminderUpdate.value = isGoReminderUpdate
    }

    fun isContainItem(id: String) = requireNotNull(deleteList.value).contains(id)

    fun addDeleteList(id: String) {
        _deleteList.value = requireNotNull(_deleteList.value).apply { add(id) }
    }

    fun removeDeleteList(id: String) {
        _deleteList.value = requireNotNull(_deleteList.value).apply { remove(id) }
    }

    private fun clearDeleteList() {
        _deleteList.value = requireNotNull(_deleteList.value).apply { clear() }
    }

    fun setLongClickPosition(isPast: Boolean, month: Int, position: Int) {
        if (month > -1 && position > -1) {
            with(requireNotNull(_reminderList.value)[month]) {
                when (isPast) {
                    true -> pastReminders[position].isChecked = true
                    false -> upcomingReminders[position].isChecked = true
                }
            }
        }
    }

    fun cancelEdit() {
        setLongClickPosition(false, -1, -1)
        setIsEdit(false)
    }

    fun initReminderMonth() {
        _reminderMonth.value = null
    }

    fun getReminder() {
        viewModelScope.launch {
            reminderRepository.getReminder(
                requireNotNull(selectedYear.value), currentYear, currentMonth
            )?.let { _reminderList.postValue(it) }
        }
    }

    private fun getReminderOfMonth() {
        viewModelScope.launch {
            reminderRepository.getReminderOfMonth(
                requireNotNull(selectedYear.value),
                requireNotNull(selectedMonth.value),
                currentYear,
                currentMonth
            )?.let {
                _reminderMonth.postValue(it)
                requireNotNull(_reminderList.value)[requireNotNull(selectedMonth.value).toInt() - 1] =
                    it
            }
        }
    }

    fun putReminderAlarm(isAlarm: Boolean, reminderId: String) {
        viewModelScope.launch {
            reminderRepository.putReminderAlarm(RequestModifyReminderAlarm(isAlarm), reminderId)
        }
    }

    fun deleteReminder() {
        viewModelScope.launch {
            reminderRepository.deleteReminder(
                RequestDeleteReminder(
                    requireNotNull(
                        deleteList.value
                    )
                )
            )?.let {
                clearDeleteList()
                getReminderOfMonth()
            }
        }
    }
}
