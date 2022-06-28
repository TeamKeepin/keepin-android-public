package com.keepin.android.data.repository

import com.keepin.android.data.datasource.FriendUpdateDataSource
import com.keepin.android.data.entity.request.RequestUpdateName
import com.keepin.android.util.getErrorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class FriendUpdateRepository @Inject constructor(
    private val friendUpdateDataSource: FriendUpdateDataSource
) {
    fun addFriend(name: String): Flow<Boolean> {
        return flow {
            friendUpdateDataSource.addFriend(RequestUpdateName(name))
            emit(true)
        }.catch { e ->
            Timber.d(getErrorMessage(e))
            emit(false)
        }
    }

    fun putFriendName(name: String, friendId: String): Flow<Boolean> {
        return flow {
            friendUpdateDataSource.putFriendName(RequestUpdateName(name), friendId)
            emit(true)
        }.catch { e ->
            Timber.d(getErrorMessage(e))
            emit(false)
        }
    }
}
