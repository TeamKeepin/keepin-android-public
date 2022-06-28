package com.keepin.android.data.api

import com.keepin.android.data.entity.request.RequestDeletePresent
import com.keepin.android.data.entity.response.BaseResponse
import com.keepin.android.data.entity.response.ResponseDetailPresent
import com.keepin.android.data.entity.response.ResponsePresentList
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ArchiveService {
    @GET("/keepin")
    suspend fun getPresent(
        @Query("taken") taken: Boolean,
        @Query("recent") recent: Boolean
    ): ResponsePresentList

    @GET("/keepin/all")
    suspend fun getSearchResult(
        @Query("title") title: String,
    ): ResponsePresentList

    @GET("/keepin/category")
    suspend fun getSearchCategoryResult(
        @Query("category") category: String,
    ): ResponsePresentList

    @GET("/keepin/detail/{keepinIdx}")
    suspend fun getPresentDetail(
        @Path("keepinIdx") keepinInx: String
    ): ResponseDetailPresent

    @POST("/keepin/delete")
    suspend fun deletePresent(
        @Body body: RequestDeletePresent
    ): BaseResponse
}
