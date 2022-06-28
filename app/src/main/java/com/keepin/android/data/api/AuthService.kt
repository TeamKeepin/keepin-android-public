package com.keepin.android.data.api

import com.keepin.android.data.entity.request.RequestEmailCheck
import com.keepin.android.data.entity.request.RequestSignIn
import com.keepin.android.data.entity.request.RequestSignUp
import com.keepin.android.data.entity.response.BaseResponse
import com.keepin.android.data.entity.response.ResponseSignIn
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/user/signin")
    suspend fun postSignIn(
        @Body body: RequestSignIn
    ): ResponseSignIn

    @POST("/user/signup")
    suspend fun postSignUp(
        @Body body: RequestSignUp
    ): BaseResponse

    @POST("/user/email/check")
    suspend fun postEmailCheck(
        @Body body: RequestEmailCheck
    ): BaseResponse
}
