package com.keepin.android.data.datasource

import com.keepin.android.data.api.FindEmailService
import com.keepin.android.data.entity.request.RequestFindEmail
import javax.inject.Inject

class FindEmailDataSourceImpl @Inject constructor(
    private val findEmailService: FindEmailService
) : FindEmailDataSource {
    override suspend fun findEmail(body: RequestFindEmail) =
        findEmailService.findEmail(body)
}
