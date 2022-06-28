package com.keepin.android.data.entity.response

import com.google.gson.annotations.SerializedName

data class ResponseProfile(
    val status: Int,
    val message: String,
    val data: Profile
)

data class Profile(
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("birth")
    val birth: String,
)
