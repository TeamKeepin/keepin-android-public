package com.keepin.android.data.repository

import com.keepin.android.data.datasource.ReminderUpdateDataSource
import com.keepin.android.data.entity.request.RequestPostReminder
import com.keepin.android.data.entity.response.ReminderDetailData
import javax.inject.Inject

class ReminderUpdateRepository @Inject constructor(
    private val reminderUpdateDataSource: ReminderUpdateDataSource
) {
    suspend fun getReminderDetail(reminderId: String): ReminderDetailData? {
        runCatching {
            reminderUpdateDataSource.getReminderDetail(reminderId)
        }.getOrNull()?.let { responseReminderDetail ->
            return responseReminderDetail.data
        } ?: return null
    }

    suspend fun postReminder(body: RequestPostReminder) =
        runCatching {
            reminderUpdateDataSource.postReminder(body)
        }.getOrNull()

    suspend fun putReminder(body: RequestPostReminder, reminderId: String) =
        runCatching {
            reminderUpdateDataSource.putReminder(body, reminderId)
        }.getOrNull()
}
