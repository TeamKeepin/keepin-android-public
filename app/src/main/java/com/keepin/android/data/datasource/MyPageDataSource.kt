package com.keepin.android.data.datasource

import com.keepin.android.data.entity.response.ResponseFriends
import com.keepin.android.data.entity.response.ResponseKeepInCount

interface MyPageDataSource {
    suspend fun getMyKeepInCount(): ResponseKeepInCount

    suspend fun getFriendsList(): ResponseFriends
}
