package com.keepin.android.data.datasource

import com.keepin.android.data.entity.request.RequestUpdateName
import com.keepin.android.data.entity.request.RequestUpdatePassword
import com.keepin.android.data.entity.request.RequestUpdatePhone
import com.keepin.android.data.entity.response.BaseResponse

interface ProfileUpdateDataSource {
    suspend fun putMyProfileName(body: RequestUpdateName): BaseResponse

    suspend fun putMyPassword(body: RequestUpdatePassword): BaseResponse

    suspend fun putMyPhone(body: RequestUpdatePhone): BaseResponse
}
