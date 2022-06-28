package com.keepin.android.data.api

import com.keepin.android.data.entity.response.BaseResponse
import com.keepin.android.data.entity.response.ResponseProfile
import retrofit2.http.DELETE
import retrofit2.http.GET

interface ProfileService {
    @GET("/my/profile")
    suspend fun getMyProfile(): ResponseProfile

    @DELETE("setting/withdrawal")
    suspend fun withdrawal(): BaseResponse
}
