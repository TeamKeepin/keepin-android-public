package com.keepin.android.data.repository

import com.keepin.android.data.datasource.MyPageDataSource
import com.keepin.android.data.datasource.SharedPreferencesDataSource
import com.keepin.android.data.entity.response.Friend
import com.keepin.android.data.entity.response.KeepInCount
import com.keepin.android.util.getErrorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class MyPageRepository @Inject constructor(
    private val myPageDataSource: MyPageDataSource,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) {
    fun getUserName() = flow {
        emit(sharedPreferencesDataSource.getUserName())
    }

    fun getMyKeepInCount(): Flow<KeepInCount> {
        return flow {
            runCatching {
                myPageDataSource.getMyKeepInCount()
            }.onSuccess { keepInCount ->
                emit(keepInCount.data)
            }.onFailure { e ->
                Timber.d(getErrorMessage(e))
            }
        }
    }

    fun getFriendsList(): Flow<List<Friend>> {
        return flow {
            runCatching {
                myPageDataSource.getFriendsList()
            }.onSuccess { friendsList ->
                emit(friendsList.data.friends)
            }.onFailure { e ->
                Timber.d(getErrorMessage(e))
            }
        }
    }
}
