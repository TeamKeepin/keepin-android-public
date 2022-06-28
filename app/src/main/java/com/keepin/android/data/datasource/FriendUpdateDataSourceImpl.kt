package com.keepin.android.data.datasource

import com.keepin.android.data.api.FriendUpdateService
import com.keepin.android.data.entity.request.RequestUpdateName
import javax.inject.Inject

class FriendUpdateDataSourceImpl @Inject constructor(
    private val friendUpdateService: FriendUpdateService
) : FriendUpdateDataSource {
    override suspend fun addFriend(body: RequestUpdateName) = friendUpdateService.addFriend(body)

    override suspend fun putFriendName(body: RequestUpdateName, friendId: String) =
        friendUpdateService.putFriendName(body, friendId)
}
