package com.keepin.android.data.entity.response

import com.google.gson.annotations.SerializedName

data class ResponseRandomKeepIn(
    val status: Int,
    val message: String,
    val data: RandomKeepIn
)

data class RandomKeepIn(
    @SerializedName("_id")
    val id: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("title")
    val title: String
)
