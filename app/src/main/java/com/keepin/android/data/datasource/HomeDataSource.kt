package com.keepin.android.data.datasource

import com.keepin.android.data.entity.response.ResponseRandomKeepIn
import com.keepin.android.data.entity.response.ResponseReminder

interface HomeDataSource {
    suspend fun getRandomKeepIn(): ResponseRandomKeepIn

    suspend fun getComingReminder(): ResponseReminder
}
