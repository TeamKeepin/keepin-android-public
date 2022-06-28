package com.keepin.android.data.datasource

import com.keepin.android.data.api.ProfileService
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
    private val profileService: ProfileService
) : ProfileDataSource {
    override suspend fun getMyProfile() = profileService.getMyProfile()

    override suspend fun withdrawal() = profileService.withdrawal()
}
