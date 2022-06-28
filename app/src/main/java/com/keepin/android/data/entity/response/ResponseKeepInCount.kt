package com.keepin.android.data.entity.response

import com.google.gson.annotations.SerializedName

data class ResponseKeepInCount(
    val status: Int,
    val message: String,
    val data: KeepInCount
)

data class KeepInCount(
    @SerializedName("total")
    val total: Int,
    @SerializedName("taken")
    val taken: Int,
    @SerializedName("given")
    val given: Int
)
