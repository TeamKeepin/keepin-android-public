package com.keepin.android.data.datasource

import com.keepin.android.data.api.FriendDetailService
import com.keepin.android.data.entity.request.RequestFriendMemo
import javax.inject.Inject

class FriendDetailDataSourceImpl @Inject constructor(
    private val friendDetailService: FriendDetailService
) : FriendDetailDataSource {
    override suspend fun getFriendDetail(friendId: String) =
        friendDetailService.getFriendDetail(friendId)

    override suspend fun getFriendKeepIn(friendId: String, taken: Boolean) =
        friendDetailService.getFriendKeepIn(friendId, taken)

    override suspend fun putFriendMemo(body: RequestFriendMemo, friendId: String) =
        friendDetailService.putFriendMemo(body, friendId)

    override suspend fun deleteFriend(friendId: String) = friendDetailService.deleteFriend(friendId)
}
