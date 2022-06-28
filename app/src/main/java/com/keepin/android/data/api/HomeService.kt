package com.keepin.android.data.api

import com.keepin.android.data.entity.response.ResponseRandomKeepIn
import com.keepin.android.data.entity.response.ResponseReminder
import retrofit2.http.GET

interface HomeService {
    @GET("/random")
    suspend fun getRandomKeepIn(): ResponseRandomKeepIn

    @GET("/reminder/oncoming")
    suspend fun getComingReminder(): ResponseReminder
}
