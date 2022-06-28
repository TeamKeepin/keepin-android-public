package com.keepin.android.data.datasource

import com.keepin.android.data.api.AuthService
import com.keepin.android.data.entity.request.RequestEmailCheck
import com.keepin.android.data.entity.request.RequestSignIn
import com.keepin.android.data.entity.request.RequestSignUp
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthDataSource {
    override suspend fun postSignIn(body: RequestSignIn) = authService.postSignIn(body)

    override suspend fun postSignUp(body: RequestSignUp) = authService.postSignUp(body)

    override suspend fun postEmailCheck(body: RequestEmailCheck) = authService.postEmailCheck(body)
}
