package com.keepin.android.data.repository

import com.keepin.android.data.datasource.ProfileDataSource
import com.keepin.android.data.datasource.SharedPreferencesDataSource
import com.keepin.android.data.entity.response.Profile
import com.keepin.android.util.getErrorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileDataSource: ProfileDataSource,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) {
    fun getMyProfile(): Flow<Profile?> {
        return flow<Profile?> {
            emit(profileDataSource.getMyProfile().data)
        }.catch { e ->
            Timber.d(getErrorMessage(e))
            emit(null)
        }
    }

    fun withdrawal(): Flow<Boolean> {
        return flow {
            profileDataSource.withdrawal()
            clearUserData()
            emit(true)
        }.catch { e ->
            Timber.d(getErrorMessage(e))
            emit(false)
        }
    }

    fun clearUserData() {
        sharedPreferencesDataSource.clearUserData()
    }
}
