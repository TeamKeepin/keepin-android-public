package com.keepin.android.data.api

import com.keepin.android.data.entity.request.RequestFindEmail
import com.keepin.android.data.entity.response.ResponseFindEmail
import retrofit2.http.Body
import retrofit2.http.POST

interface FindEmailService {
    @POST("/my/find/email")
    suspend fun findEmail(
        @Body body: RequestFindEmail
    ): ResponseFindEmail
}
