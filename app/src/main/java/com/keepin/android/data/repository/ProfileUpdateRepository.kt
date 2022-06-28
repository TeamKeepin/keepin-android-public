package com.keepin.android.data.repository

import com.keepin.android.data.datasource.ProfileUpdateDataSource
import com.keepin.android.data.datasource.SharedPreferencesDataSource
import com.keepin.android.data.entity.request.RequestUpdateName
import com.keepin.android.data.entity.request.RequestUpdatePassword
import com.keepin.android.data.entity.request.RequestUpdatePhone
import com.keepin.android.util.getErrorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ProfileUpdateRepository @Inject constructor(
    private val profileUpdateDataSource: ProfileUpdateDataSource,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) {
    fun putMyProfileName(name: String): Flow<Boolean> {
        return flow {
            profileUpdateDataSource.putMyProfileName(RequestUpdateName(name))
            sharedPreferencesDataSource.saveUserName(name)
            emit(true)
        }.catch { e ->
            Timber.d(getErrorMessage(e))
            emit(false)
        }
    }

    fun putMyPassword(password: String, newPassword: String, catch: () -> Unit): Flow<Boolean> {
        return flow {
            val body = RequestUpdatePassword(password, newPassword)
            profileUpdateDataSource.putMyPassword(body)
            sharedPreferencesDataSource.saveUserPassword(newPassword)
            emit(true)
        }.catch { e ->
            Timber.d(getErrorMessage(e))
            catch()
            emit(false)
        }
    }

    fun putMyPhone(phone: String): Flow<Boolean> {
        return flow {
            profileUpdateDataSource.putMyPhone(RequestUpdatePhone(phone))
            emit(true)
        }.catch { e ->
            Timber.d(getErrorMessage(e))
            emit(false)
        }
    }
}
