package com.keepin.android.data.entity.request

import com.google.gson.annotations.SerializedName

data class RequestUpdatePhone(
    @SerializedName("phone")
    val phone: String
)
