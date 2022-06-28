package com.keepin.android.data.entity.response

import com.google.gson.annotations.SerializedName

data class ResponsePresentList(
    val status: Int,
    val message: String,
    val data: KeepInList
)

data class KeepInList(
    @SerializedName("keepins")
    val keepIns: List<KeepInData>,
)

data class KeepInData(
    @SerializedName("_id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("date")
    val date: String
)
