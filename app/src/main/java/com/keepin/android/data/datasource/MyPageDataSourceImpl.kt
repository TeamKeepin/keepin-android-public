package com.keepin.android.data.datasource

import com.keepin.android.data.api.MyPageService
import javax.inject.Inject

class MyPageDataSourceImpl @Inject constructor(
    private val myPageService: MyPageService
) : MyPageDataSource {
    override suspend fun getMyKeepInCount() = myPageService.getMyKeepInCount()

    override suspend fun getFriendsList() = myPageService.getFriendsList()
}
