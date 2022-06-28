package com.keepin.android.data.entity.response

import com.google.gson.annotations.SerializedName

data class ResponseReminderDetail(
    @SerializedName("data")
    val `data`: ReminderDetailData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)

data class ReminderDetailData(
    @SerializedName("date")
    val date: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("isAlarm")
    val isAlarm: Boolean,
    @SerializedName("isImportant")
    val isImportant: Boolean,
    @SerializedName("title")
    val title: String,
    @SerializedName("daysAgo")
    val daysAgo: Int?
)
