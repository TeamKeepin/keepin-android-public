package com.keepin.android.data.datasource

import com.keepin.android.data.api.FindPasswordService
import com.keepin.android.data.entity.request.RequestFindPassword
import javax.inject.Inject

class FindPasswordDataSourceImpl @Inject constructor(private val findPasswordService: FindPasswordService) :
    FindPasswordDataSource {

    override suspend fun findPassword(body: RequestFindPassword) =
        findPasswordService.findPassword(body)
}
