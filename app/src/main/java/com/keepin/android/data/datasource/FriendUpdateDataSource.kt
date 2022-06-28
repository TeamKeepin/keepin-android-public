package com.keepin.android.data.datasource

import com.keepin.android.data.entity.request.RequestUpdateName
import com.keepin.android.data.entity.response.BaseResponse

interface FriendUpdateDataSource {
    suspend fun addFriend(body: RequestUpdateName): BaseResponse

    suspend fun putFriendName(body: RequestUpdateName, friendId: String): BaseResponse
}
