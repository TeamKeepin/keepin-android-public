package com.keepin.android.data.repository

import com.keepin.android.data.datasource.FriendDetailDataSource
import com.keepin.android.data.entity.request.RequestFriendMemo
import com.keepin.android.data.entity.response.FriendDetail
import com.keepin.android.data.entity.response.KeepInData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class FriendDetailRepository @Inject constructor(
    private val dataSource: FriendDetailDataSource
) {
    fun getFriendDetail(friendId: String): Flow<FriendDetail?> {
        return flow {
            kotlin.runCatching {
                dataSource.getFriendDetail(friendId)
            }.onSuccess { friendDetail ->
                emit(friendDetail.data)
            }.onFailure { e ->
                Timber.d(e.toString())
                emit(null)
            }
        }
    }

    fun getFriendKeepIn(friendId: String, taken: Boolean): Flow<List<KeepInData>?> {
        return flow {
            kotlin.runCatching {
                dataSource.getFriendKeepIn(friendId, taken)
            }.onSuccess { friendKeepIn ->
                emit(friendKeepIn.data.keepIns)
            }.onFailure { e ->
                Timber.d(e.toString())
                emit(null)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun putFriendMemo(memo: String, friendId: String): Flow<Boolean> {
        return flow {
            runCatching {
                dataSource.putFriendMemo(RequestFriendMemo(memo), friendId)
            }.onSuccess {
                emit(true)
            }.onFailure { e ->
                Timber.d(e.toString())
                emit(false)
            }
        }
    }

    fun deleteFriend(friendId: String): Flow<Boolean> {
        return flow {
            runCatching {
                dataSource.deleteFriend(friendId)
            }.onSuccess {
                emit(true)
            }.onFailure { e ->
                Timber.d(e.toString())
                emit(false)
            }
        }
    }
}
