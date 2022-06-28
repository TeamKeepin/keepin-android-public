package com.keepin.android.data.datasource

import com.keepin.android.data.api.ReminderUpdateService
import com.keepin.android.data.entity.request.RequestPostReminder
import javax.inject.Inject

class ReminderUpdateDataSourceImpl @Inject constructor(
    private val reminderUpdateService: ReminderUpdateService
) : ReminderUpdateDataSource {
    override suspend fun getReminderDetail(reminderId: String) =
        reminderUpdateService.getReminderDetail(reminderId)

    override suspend fun postReminder(body: RequestPostReminder) =
        reminderUpdateService.postReminder(body)

    override suspend fun putReminder(body: RequestPostReminder, reminderId: String) =
        reminderUpdateService.putReminder(body, reminderId)
}
