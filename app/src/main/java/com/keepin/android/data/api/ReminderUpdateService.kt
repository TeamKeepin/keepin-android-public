package com.keepin.android.data.api

import com.keepin.android.data.entity.request.RequestPostReminder
import com.keepin.android.data.entity.response.BaseResponse
import com.keepin.android.data.entity.response.ResponseReminderDetail
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReminderUpdateService {
    @GET("/reminder/detail/{reminderId}")
    suspend fun getReminderDetail(
        @Path("reminderId") reminderId: String
    ): ResponseReminderDetail

    @POST("/reminder")
    suspend fun postReminder(
        @Body body: RequestPostReminder
    ): BaseResponse

    @PUT("/reminder/modify/{reminderId}")
    suspend fun putReminder(
        @Body body: RequestPostReminder,
        @Path("reminderId") reminderId: String
    ): BaseResponse
}
