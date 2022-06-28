package com.keepin.android.data.repository

import com.keepin.android.data.datasource.KeepInDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class KeepInRepository @Inject constructor(
    private val keepInDataSource: KeepInDataSource
) {
    suspend fun postKeepIn(
        map: Map<String, RequestBody>,
        photo: ArrayList<MultipartBody.Part>
    ) = runCatching {
        keepInDataSource.postKeepIn(map, photo)
    }.getOrNull()

    suspend fun modifyKeepIn(
        map: Map<String, RequestBody>,
        keepInIdx: String
    ) = runCatching {
        keepInDataSource.modifyKeepIn(map, keepInIdx)
    }.getOrNull()
}
