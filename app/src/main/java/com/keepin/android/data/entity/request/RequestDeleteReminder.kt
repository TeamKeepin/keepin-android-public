package com.keepin.android.data.entity.request

import com.google.gson.annotations.SerializedName

data class RequestDeleteReminder(
    @SerializedName("reminderArray")
    val reminderArray: List<String>
)
