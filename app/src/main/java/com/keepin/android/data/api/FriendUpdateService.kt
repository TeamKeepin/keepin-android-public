package com.keepin.android.data.api

import com.keepin.android.data.entity.request.RequestUpdateName
import com.keepin.android.data.entity.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FriendUpdateService {
    @POST("/friend")
    suspend fun addFriend(
        @Body body: RequestUpdateName,
    ): BaseResponse

    @PUT("/friend/{friendId}")
    suspend fun putFriendName(
        @Body body: RequestUpdateName,
        @Path("friendId") friendId: String
    ): BaseResponse
}
