package com.keepin.android.data.entity.response

import com.google.gson.annotations.SerializedName

data class ResponseFindEmail(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("email")
    val email: String
)
