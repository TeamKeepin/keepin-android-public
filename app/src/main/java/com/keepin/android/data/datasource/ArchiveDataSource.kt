package com.keepin.android.data.datasource

import com.keepin.android.data.entity.request.RequestDeletePresent
import com.keepin.android.data.entity.response.BaseResponse
import com.keepin.android.data.entity.response.ResponseDetailPresent
import com.keepin.android.data.entity.response.ResponsePresentList

interface ArchiveDataSource {

    suspend fun getArchive(taken: Boolean, recent: Boolean): ResponsePresentList

    suspend fun getArchiveDetail(keepinIdx: String): ResponseDetailPresent

    suspend fun deleteArchive(body: RequestDeletePresent): BaseResponse

    suspend fun getSearchCategory(keyword: String): ResponsePresentList

    suspend fun getSearchKeyword(keyword: String): ResponsePresentList
}
