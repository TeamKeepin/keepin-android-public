package com.keepin.android.data.datasource

import com.keepin.android.data.api.ProfileUpdateService
import com.keepin.android.data.entity.request.RequestUpdateName
import com.keepin.android.data.entity.request.RequestUpdatePassword
import com.keepin.android.data.entity.request.RequestUpdatePhone
import javax.inject.Inject

class ProfileUpdateDataSourceImpl @Inject constructor(
    private val profileUpdateService: ProfileUpdateService
) : ProfileUpdateDataSource {
    override suspend fun putMyProfileName(body: RequestUpdateName) =
        profileUpdateService.putMyProfileName(body)

    override suspend fun putMyPassword(body: RequestUpdatePassword) =
        profileUpdateService.putMyPassword(body)

    override suspend fun putMyPhone(body: RequestUpdatePhone) =
        profileUpdateService.putMyPhone(body)
}
