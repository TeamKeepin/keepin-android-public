package com.keepin.android.data.repository

import com.keepin.android.data.datasource.FindPasswordDataSource
import com.keepin.android.data.entity.request.RequestFindPassword
import javax.inject.Inject

class FindPasswordRepository @Inject constructor(private val findPasswordDataSource: FindPasswordDataSource) {

    suspend fun findPassword(body: RequestFindPassword) =
        kotlin.runCatching { findPasswordDataSource.findPassword(body) }.getOrNull()
}
