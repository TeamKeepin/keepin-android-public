package com.keepin.android.data.datasource

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SharedPreferencesDataSource {
    override fun saveUserToken(accessToken: String, refreshToken: String) {
        sharedPreferences.edit()
            .putString(ACCESS_TOKEN, accessToken)
            .putString(REFRESH_TOKEN, refreshToken)
            .apply()
    }

    override fun saveUserName(userName: String) {
        sharedPreferences.edit()
            .putString(USER_NAME, userName)
            .apply()
    }

    override fun saveUserPassword(password: String) {
        sharedPreferences.edit()
            .putString(USER_PW, password)
            .apply()
    }

    override fun saveUserData(userEmail: String, userPw: String) {
        sharedPreferences.edit()
            .putString(USER_EMAIL, userEmail)
            .putString(USER_PW, userPw)
            .apply()
    }

    override fun saveOnBoarding() {
        sharedPreferences.edit()
            .putBoolean(ON_BOARDING, true)
            .apply()
    }

    override fun saveNoticeDialog() {
        sharedPreferences.edit()
            .putBoolean(NOTICE_DIALOG, true)
            .apply()
    }

    override fun getAccessToken() =
        sharedPreferences.getString(ACCESS_TOKEN, "") ?: ""

    override fun getRefreshToken() = sharedPreferences.getString(REFRESH_TOKEN, "") ?: ""

    override fun getUserName() = sharedPreferences.getString(USER_NAME, "") ?: ""

    override fun getUserEmail() = sharedPreferences.getString(USER_EMAIL, "") ?: ""

    override fun getUserPw() = sharedPreferences.getString(USER_PW, "") ?: ""

    override fun getOnBoarding() = sharedPreferences.getBoolean(ON_BOARDING, false)

    override fun getNoticeDialog() = sharedPreferences.getBoolean(NOTICE_DIALOG, false)

    override fun isContainNoticeDialog() = sharedPreferences.contains(NOTICE_DIALOG)

    override fun clearUserData() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    companion object {
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val USER_NAME = "USER_NAME"
        private const val USER_EMAIL = "USER_EMAIL"
        private const val USER_PW = "USER_PW"
        private const val ON_BOARDING = "ON_BOARDING"
        private const val NOTICE_DIALOG = "NOTICE_DIALOG"
    }
}
