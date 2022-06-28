package com.keepin.android.data.api

import com.keepin.android.data.entity.response.ResponseFriends
import com.keepin.android.data.entity.response.ResponseKeepInCount
import retrofit2.http.GET

interface MyPageService {
    @GET("/my")
    suspend fun getMyKeepInCount(): ResponseKeepInCount

    @GET("/friend")
    suspend fun getFriendsList(): ResponseFriends
}
