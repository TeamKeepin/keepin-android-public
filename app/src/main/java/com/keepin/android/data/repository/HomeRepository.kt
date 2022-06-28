package com.keepin.android.data.repository

import com.keepin.android.R
import com.keepin.android.data.datasource.HomeDataSource
import com.keepin.android.data.datasource.SharedPreferencesDataSource
import com.keepin.android.data.entity.response.RandomKeepIn
import com.keepin.android.data.entity.response.Reminder
import com.keepin.android.util.getErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import java.lang.IndexOutOfBoundsException
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val homeDataSource: HomeDataSource,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) {
    fun getUserName() = flow {
        emit(sharedPreferencesDataSource.getUserName())
    }

    fun getNoticeDialog() = sharedPreferencesDataSource.getNoticeDialog()

    fun setNoticeDialog() = sharedPreferencesDataSource.saveNoticeDialog()

    fun isContainNoticeDialog() = sharedPreferencesDataSource.isContainNoticeDialog()

    fun getWelcomeText(): Flow<Int> {
        return flow {
            val stringId = when ((System.currentTimeMillis() % 8).toInt()) {
                0 -> R.string.home_welcome0
                1 -> R.string.home_welcome1
                2 -> R.string.home_welcome2
                3 -> R.string.home_welcome3
                4 -> R.string.home_welcome4
                5 -> R.string.home_welcome5
                6 -> R.string.home_welcome6
                7 -> R.string.home_welcome7
                else -> throw IndexOutOfBoundsException()
            }
            emit(stringId)
        }
    }

    fun getRandomKeepIn(): Flow<RandomKeepIn> {
        return flow {
            kotlin.runCatching {
                homeDataSource.getRandomKeepIn()
            }.onSuccess { randomKeepIn ->
                emit(randomKeepIn.data)
            }.onFailure { e ->
                Timber.d(getErrorMessage(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getComingReminder(): Flow<List<Reminder>> {
        return flow {
            kotlin.runCatching {
                homeDataSource.getComingReminder()
            }.onSuccess { reminderList ->
                emit(reminderList.data.reminders)
            }.onFailure { e ->
                Timber.d(getErrorMessage(e))
            }
        }
    }
}
