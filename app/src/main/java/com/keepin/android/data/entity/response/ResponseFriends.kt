package com.keepin.android.data.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponseFriends(
    val status: Int,
    val message: String,
    val data: FriendsList
)

data class FriendsList(
    @SerializedName("friends")
    val friends: List<Friend>
)

@Parcelize
data class Friend(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String
) : Parcelable
