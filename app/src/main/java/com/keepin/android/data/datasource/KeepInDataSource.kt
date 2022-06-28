package com.keepin.android.data.datasource

import com.keepin.android.data.entity.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface KeepInDataSource {
    suspend fun postKeepIn(
        map: Map<String, @JvmSuppressWildcards RequestBody>,
        photo: ArrayList<MultipartBody.Part>
    ): BaseResponse

    suspend fun modifyKeepIn(
        map: Map<String, @JvmSuppressWildcards RequestBody>,
        keepInIdx: String
    ): BaseResponse
}
