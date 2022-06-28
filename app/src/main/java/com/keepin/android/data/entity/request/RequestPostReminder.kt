package com.keepin.android.data.entity.request

import com.google.gson.annotations.SerializedName

data class RequestPostReminder(
    @SerializedName("date")
    val date: String,
    @SerializedName("daysAgo")
    val daysAgo: String?,
    @SerializedName("isAlarm")
    val isAlarm: Boolean,
    @SerializedName("isImportant")
    val isImportant: Boolean,
    @SerializedName("title")
    val title: String
)
