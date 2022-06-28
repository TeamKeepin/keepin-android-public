package com.keepin.android.data.datasource

interface SharedPreferencesDataSource {
    fun saveUserToken(accessToken: String, refreshToken: String)

    fun saveUserName(userName: String)

    fun saveUserPassword(password: String)

    fun saveUserData(userEmail: String, userPw: String)

    fun saveOnBoarding()

    fun saveNoticeDialog()

    fun getAccessToken(): String

    fun getRefreshToken(): String

    fun getUserName(): String

    fun getUserEmail(): String

    fun getUserPw(): String

    fun getOnBoarding(): Boolean

    fun getNoticeDialog(): Boolean

    fun isContainNoticeDialog(): Boolean

    fun clearUserData()
}
