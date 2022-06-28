package com.keepin.android.data.datasource

import com.keepin.android.data.entity.request.RequestDeleteReminder
import com.keepin.android.data.entity.request.RequestModifyReminderAlarm
import com.keepin.android.data.entity.response.BaseResponse
import com.keepin.android.data.entity.response.ResponseReminder
import com.keepin.android.data.entity.response.ResponseReminderOfYear

interface ReminderDataSource {
    suspend fun getReminderOfMonth(year: String, month: String): ResponseReminder

    suspend fun getReminderOfYear(year: String): ResponseReminderOfYear

    suspend fun deleteReminder(body: RequestDeleteReminder): BaseResponse

    suspend fun putReminderAlarm(body: RequestModifyReminderAlarm, reminderId: String): BaseResponse
}
