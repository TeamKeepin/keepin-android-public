package com.keepin.android.data.datasource

import com.keepin.android.data.entity.request.RequestFriendMemo
import com.keepin.android.data.entity.response.BaseResponse
import com.keepin.android.data.entity.response.ResponseFriendDetail
import com.keepin.android.data.entity.response.ResponsePresentList

interface FriendDetailDataSource {
    suspend fun getFriendDetail(friendId: String): ResponseFriendDetail

    suspend fun getFriendKeepIn(friendId: String, taken: Boolean): ResponsePresentList

    suspend fun putFriendMemo(body: RequestFriendMemo, friendId: String): BaseResponse

    suspend fun deleteFriend(friendId: String): BaseResponse
}
