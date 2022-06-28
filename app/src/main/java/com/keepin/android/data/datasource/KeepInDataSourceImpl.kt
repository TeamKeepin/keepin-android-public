package com.keepin.android.data.datasource

import com.keepin.android.data.api.KeepInService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class KeepInDataSourceImpl @Inject constructor(
    private val keepInService: KeepInService
) : KeepInDataSource {
    override suspend fun postKeepIn(
        map: Map<String, RequestBody>,
        photo: ArrayList<MultipartBody.Part>
    ) = keepInService.postKeepIn(map, photo)

    override suspend fun modifyKeepIn(
        map: Map<String, RequestBody>,
        keepInIdx: String
    ) = keepInService.modifyKeepIn(map, keepInIdx)
}
