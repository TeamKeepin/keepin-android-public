package com.keepin.android.data.datasource

import com.keepin.android.data.entity.request.RequestEmailCheck
import com.keepin.android.data.entity.request.RequestSignIn
import com.keepin.android.data.entity.request.RequestSignUp
import com.keepin.android.data.entity.response.BaseResponse
import com.keepin.android.data.entity.response.ResponseSignIn

interface AuthDataSource {
    suspend fun postSignIn(body: RequestSignIn): ResponseSignIn

    suspend fun postSignUp(body: RequestSignUp): BaseResponse

    suspend fun postEmailCheck(body: RequestEmailCheck): BaseResponse
}
