package com.keepin.android.data.repository

import com.keepin.android.data.datasource.ReminderDataSource
import com.keepin.android.data.entity.request.RequestDeleteReminder
import com.keepin.android.data.entity.request.RequestModifyReminderAlarm
import com.keepin.android.data.entity.response.MonthReminder
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val reminderDataSource: ReminderDataSource
) {
    suspend fun getReminderOfMonth(
        year: String,
        month: String,
        currentYear: Int,
        currentMonth: Int
    ): MonthReminder? {
        runCatching { reminderDataSource.getReminderOfMonth(year, month) }.getOrNull()
            ?.let { reminderOfMonth ->
                return MonthReminder(
                    mutableListOf(),
                    mutableListOf()
                ).apply {
                    when {
                        year.toInt() < currentYear -> this.pastReminders.addAll(reminderOfMonth.data.reminders)
                        year.toInt() > currentYear -> this.upcomingReminders.addAll(reminderOfMonth.data.reminders)
                        else -> {
                            when {
                                month.toInt() < currentMonth -> this.pastReminders.addAll(
                                    reminderOfMonth.data.reminders
                                )
                                month.toInt() > currentMonth -> this.upcomingReminders.addAll(
                                    reminderOfMonth.data.reminders
                                )
                                else -> {
                                    reminderOfMonth.data.reminders.forEach { reminder ->
                                        when (reminder.isPassed) {
                                            true -> this.pastReminders.add(reminder)
                                            false -> this.upcomingReminders.add(reminder)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } ?: return null
    }

    suspend fun getReminder(
        year: String,
        currentYear: Int,
        currentMonth: Int
    ): MutableList<MonthReminder>? {
        runCatching {
            reminderDataSource.getReminderOfYear(year)
        }.getOrNull()?.let { reminderOfYear ->
            return MutableList(12) {
                MonthReminder(
                    mutableListOf(),
                    mutableListOf()
                )
            }.apply {
                for ((month, reminderList) in reminderOfYear.data.reminders.withIndex()) {
                    when {
                        year.toInt() < currentYear -> {
                            this[month].pastReminders.addAll(reminderList)
                        }
                        year.toInt() > currentYear -> {
                            this[month].upcomingReminders.addAll(reminderList)
                        }
                        else -> {
                            when {
                                month < currentMonth - 1 -> this[month].pastReminders.addAll(
                                    reminderList
                                )
                                month > currentMonth - 1 -> this[month].upcomingReminders.addAll(
                                    reminderList
                                )
                                else -> {
                                    reminderList.forEach { reminder ->
                                        when (reminder.isPassed) {
                                            true -> this[month].pastReminders.add(reminder)
                                            false -> this[month].upcomingReminders.add(
                                                reminder
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } ?: return null
    }

    suspend fun deleteReminder(body: RequestDeleteReminder) =
        runCatching {
            reminderDataSource.deleteReminder(body)
        }.getOrNull()

    suspend fun putReminderAlarm(body: RequestModifyReminderAlarm, reminderId: String) =
        runCatching {
            reminderDataSource.putReminderAlarm(body, reminderId)
        }.getOrNull()
}
