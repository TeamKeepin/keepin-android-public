package com.keepin.android.data.api

import com.keepin.android.data.entity.request.RequestUpdateName
import com.keepin.android.data.entity.request.RequestUpdatePassword
import com.keepin.android.data.entity.request.RequestUpdatePhone
import com.keepin.android.data.entity.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.PUT

interface ProfileUpdateService {
    @PUT("/my/profile")
    suspend fun putMyProfileName(
        @Body body: RequestUpdateName,
    ): BaseResponse

    @PUT("/my/edit/password")
    suspend fun putMyPassword(
        @Body body: RequestUpdatePassword
    ): BaseResponse

    @PUT("/my/phone")
    suspend fun putMyPhone(
        @Body body: RequestUpdatePhone
    ): BaseResponse
}
