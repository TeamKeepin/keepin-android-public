package com.keepin.android.data.repository

import com.keepin.android.data.datasource.ArchiveDataSource
import com.keepin.android.data.entity.request.RequestDeletePresent
import javax.inject.Inject

class ArchiveRepository @Inject constructor(private val archiveDataSource: ArchiveDataSource) {

    suspend fun getArchive(taken: Boolean, recent: Boolean) =
        kotlin.runCatching { archiveDataSource.getArchive(taken, recent) }.getOrNull()

    suspend fun getArchiveDetail(keepinIdx: String) =
        kotlin.runCatching { archiveDataSource.getArchiveDetail(keepinIdx) }.getOrNull()

    suspend fun deleteArchive(body: RequestDeletePresent) =
        kotlin.runCatching { archiveDataSource.deleteArchive(body) }.getOrNull()

    suspend fun getSearchCategory(keyword: String) =
        kotlin.runCatching { archiveDataSource.getSearchCategory(keyword) }.getOrNull()

    suspend fun getSearchKeyword(keyword: String) =
        kotlin.runCatching { archiveDataSource.getSearchKeyword(keyword) }.getOrNull()
}
