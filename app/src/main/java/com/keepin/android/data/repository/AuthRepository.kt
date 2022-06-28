package com.keepin.android.data.repository

import com.keepin.android.data.datasource.AuthDataSource
import com.keepin.android.data.datasource.SharedPreferencesDataSource
import com.keepin.android.data.entity.request.RequestEmailCheck
import com.keepin.android.data.entity.request.RequestSignIn
import com.keepin.android.data.entity.request.RequestSignUp
import com.keepin.android.data.entity.response.ResponseSignIn
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) {
    suspend fun postSignIn(email: String, password: String, fcm: String) = flow {
        kotlin.runCatching {
            authDataSource.postSignIn(RequestSignIn(email = email, password = password, fcm = fcm))
        }.onSuccess { responseSignIn ->
            afterSignIn(responseSignIn, email, password)
            emit(true)
        }.onFailure { e ->
            when (e) {
                is HttpException -> {
                    sharedPreferencesDataSource.clearUserData()
                    emit(false)
                }
            }
        }
    }

    suspend fun postEmailCheck(email: String) = flow {
        kotlin.runCatching {
            authDataSource.postEmailCheck(RequestEmailCheck(email = email))
        }.getOrNull()?.let {
            emit(true)
        } ?: emit(false)
    }

    suspend fun postSignUp(
        email: String,
        password: String,
        name: String,
        birth: String,
        phoneToken: String,
        phone: String
    ) = flow {
        kotlin.runCatching {
            authDataSource.postSignUp(
                RequestSignUp(
                    email = email,
                    password = password,
                    name = name,
                    birth = birth,
                    phoneToken = phoneToken,
                    phone = phone
                )
            )
        }.getOrNull()?.let {
            emit(true)
        } ?: emit(false)
    }

    fun getUserEmail() = sharedPreferencesDataSource.getUserEmail()

    fun getUserPassword() = sharedPreferencesDataSource.getUserPw()

    fun getOnBoarding() = sharedPreferencesDataSource.getOnBoarding()

    fun setOnBoarding() {
        sharedPreferencesDataSource.saveOnBoarding()
    }

    private fun afterSignIn(responseSignIn: ResponseSignIn, email: String, password: String) {
        with(sharedPreferencesDataSource) {
            saveUserName(userName = responseSignIn.data.name)
            saveUserData(userEmail = email, userPw = password)
            saveUserToken(
                accessToken = responseSignIn.data.jwt,
                refreshToken = responseSignIn.data.refreshToken
            )
        }
    }
}
