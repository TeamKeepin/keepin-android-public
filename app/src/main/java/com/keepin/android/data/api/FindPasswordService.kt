package com.keepin.android.data.api

import com.keepin.android.data.entity.request.RequestFindPassword
import com.keepin.android.data.entity.response.ResponseFindPassword
import retrofit2.http.Body
import retrofit2.http.POST

interface FindPasswordService {
    @POST("/my/find/password")
    suspend fun findPassword(
        @Body body: RequestFindPassword
    ): ResponseFindPassword
}
