package com.keepin.android.data.entity.response

import com.google.gson.annotations.SerializedName

data class ResponseReminderOfYear(
    @SerializedName("data")
    val `data`: ReminderOfYear,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)

data class ReminderOfYear(
    @SerializedName("reminders")
    val reminders: List<List<Reminder>>
)
