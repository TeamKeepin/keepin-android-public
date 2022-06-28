package com.keepin.android.data.datasource

import com.keepin.android.data.entity.response.BaseResponse
import com.keepin.android.data.entity.response.ResponseProfile

interface ProfileDataSource {
    suspend fun getMyProfile(): ResponseProfile

    suspend fun withdrawal(): BaseResponse
}
