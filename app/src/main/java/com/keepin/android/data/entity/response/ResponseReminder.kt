package com.keepin.android.data.entity.response

import com.google.gson.annotations.SerializedName

data class ResponseReminder(
    @SerializedName("data")
    val `data`: ReminderData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)

data class ReminderData(
    @SerializedName("reminders")
    val reminders: List<Reminder>
)

data class Reminder(
    @SerializedName("date")
    val date: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("isAlarm")
    val isAlarm: Boolean,
    @SerializedName("isImportant")
    val isImportant: Boolean,
    @SerializedName("isPassed")
    val isPassed: Boolean,
    @SerializedName("title")
    val title: String,
    @SerializedName("isChecked")
    var isChecked: Boolean = false
)

data class MonthReminder(
    val pastReminders: MutableList<Reminder>,
    val upcomingReminders: MutableList<Reminder>
)
