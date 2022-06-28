package com.keepin.android.data.entity.response

import com.google.gson.annotations.SerializedName

data class ResponseFriendDetail(
    val status: Int,
    val message: String,
    val data: FriendDetail
)

data class FriendDetail(
    @SerializedName("name")
    val name: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("taken")
    val taken: Int,
    @SerializedName("given")
    val given: Int,
    @SerializedName("memo")
    val memo: String
)
