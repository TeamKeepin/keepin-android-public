package com.keepin.android.data.datasource

import com.keepin.android.data.api.HomeService
import javax.inject.Inject

class HomeDataSourceImpl @Inject constructor(
    private val homeService: HomeService
) : HomeDataSource {
    override suspend fun getRandomKeepIn() = homeService.getRandomKeepIn()

    override suspend fun getComingReminder() = homeService.getComingReminder()
}
