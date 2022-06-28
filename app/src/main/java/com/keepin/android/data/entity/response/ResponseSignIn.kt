package com.keepin.android.data.entity.response

import com.google.gson.annotations.SerializedName

data class ResponseSignIn(
    @SerializedName("data")
    val `data`: LoginData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)

data class LoginData(
    @SerializedName("jwt")
    val jwt: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)
