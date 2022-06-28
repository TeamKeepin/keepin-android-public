package com.keepin.android.data.api

import com.keepin.android.data.entity.request.RequestDeleteReminder
import com.keepin.android.data.entity.request.RequestModifyReminderAlarm
import com.keepin.android.data.entity.response.BaseResponse
import com.keepin.android.data.entity.response.ResponseReminder
import com.keepin.android.data.entity.response.ResponseReminderOfYear
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ReminderService {
    @GET("/reminder/date")
    suspend fun getReminderOfMonth(
        @Query("year") year: String,
        @Query("month") month: String
    ): ResponseReminder

    @GET("/reminder/year")
    suspend fun getReminderOfYear(
        @Query("year") year: String
    ): ResponseReminderOfYear

    @POST("/reminder/delete")
    suspend fun deleteReminder(
        @Body body: RequestDeleteReminder
    ): BaseResponse

    @PUT("/reminder/modify/alarm/{reminderId}")
    suspend fun putReminderAlarm(
        @Body body: RequestModifyReminderAlarm,
        @Path("reminderId") reminderId: String
    ): BaseResponse
}
