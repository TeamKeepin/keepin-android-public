package com.keepin.android.data.entity.request

import com.google.gson.annotations.SerializedName

data class RequestSignIn(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("fcm")
    val fcm: String
)
