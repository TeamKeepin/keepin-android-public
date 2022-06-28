package com.keepin.android.data.api

import com.keepin.android.data.entity.request.RequestFriendMemo
import com.keepin.android.data.entity.response.BaseResponse
import com.keepin.android.data.entity.response.ResponseFriendDetail
import com.keepin.android.data.entity.response.ResponsePresentList
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FriendDetailService {
    @GET("/friend/{friendId}")
    suspend fun getFriendDetail(
        @Path("friendId") friendId: String
    ): ResponseFriendDetail

    @GET("/friend/keepin/{friendId}")
    suspend fun getFriendKeepIn(
        @Path("friendId") friendId: String,
        @Query("taken") taken: Boolean
    ): ResponsePresentList

    @PUT("/friend/memo/{friendId}")
    suspend fun putFriendMemo(
        @Body body: RequestFriendMemo,
        @Path("friendId") friendId: String
    ): BaseResponse

    @DELETE("/friend/{friendId}")
    suspend fun deleteFriend(
        @Path("friendId") friendId: String
    ): BaseResponse
}
