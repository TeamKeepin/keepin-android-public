package com.keepin.android.data.datasource

import com.keepin.android.data.api.ArchiveService
import com.keepin.android.data.entity.request.RequestDeletePresent
import javax.inject.Inject

class ArchiveDataSourceImpl @Inject constructor(private val archiveService: ArchiveService) :
    ArchiveDataSource {

    override suspend fun getArchive(taken: Boolean, recent: Boolean) =
        archiveService.getPresent(taken, recent)

    override suspend fun getArchiveDetail(keepinIdx: String) =
        archiveService.getPresentDetail(keepinIdx)

    override suspend fun deleteArchive(body: RequestDeletePresent) =
        archiveService.deletePresent(body)

    override suspend fun getSearchCategory(keyword: String) =
        archiveService.getSearchCategoryResult(keyword)

    override suspend fun getSearchKeyword(keyword: String) =
        archiveService.getSearchResult(keyword)
}
