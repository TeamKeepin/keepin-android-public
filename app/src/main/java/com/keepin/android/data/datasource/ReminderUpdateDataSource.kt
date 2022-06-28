package com.keepin.android.data.datasource

import com.keepin.android.data.entity.request.RequestPostReminder
import com.keepin.android.data.entity.response.BaseResponse
import com.keepin.android.data.entity.response.ResponseReminderDetail

interface ReminderUpdateDataSource {
    suspend fun getReminderDetail(reminderId: String): ResponseReminderDetail

    suspend fun postReminder(body: RequestPostReminder): BaseResponse

    suspend fun putReminder(body: RequestPostReminder, reminderId: String): BaseResponse
}
