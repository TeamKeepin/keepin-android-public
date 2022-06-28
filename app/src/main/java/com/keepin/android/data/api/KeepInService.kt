package com.keepin.android.data.api

import com.keepin.android.data.entity.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface KeepInService {

    @Multipart
    @POST("/keepin/all")
    suspend fun postKeepIn(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photo: ArrayList<MultipartBody.Part>
    ): BaseResponse

    @Multipart
    @PUT("/keepin/modify/{keepinIdx}")
    suspend fun modifyKeepIn(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Path("keepinIdx") keepInIdx: String
    ): BaseResponse
}
