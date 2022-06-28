package com.keepin.android.data.datasource

import com.keepin.android.data.entity.request.RequestFindEmail
import com.keepin.android.data.entity.response.ResponseFindEmail

interface FindEmailDataSource {
    suspend fun findEmail(body: RequestFindEmail): ResponseFindEmail
}
