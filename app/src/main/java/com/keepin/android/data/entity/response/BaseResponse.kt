package com.keepin.android.data.entity.response

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String
)
