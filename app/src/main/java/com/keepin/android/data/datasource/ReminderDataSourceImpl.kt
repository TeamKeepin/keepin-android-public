package com.keepin.android.data.datasource

import com.keepin.android.data.api.ReminderService
import com.keepin.android.data.entity.request.RequestDeleteReminder
import com.keepin.android.data.entity.request.RequestModifyReminderAlarm
import javax.inject.Inject

class ReminderDataSourceImpl @Inject constructor(
    private val reminderService: ReminderService
) : ReminderDataSource {
    override suspend fun getReminderOfMonth(year: String, month: String) =
        reminderService.getReminderOfMonth(year, month)

    override suspend fun getReminderOfYear(year: String) = reminderService.getReminderOfYear(year)

    override suspend fun deleteReminder(body: RequestDeleteReminder) =
        reminderService.deleteReminder(body)

    override suspend fun putReminderAlarm(body: RequestModifyReminderAlarm, reminderId: String) =
        reminderService.putReminderAlarm(body, reminderId)
}
