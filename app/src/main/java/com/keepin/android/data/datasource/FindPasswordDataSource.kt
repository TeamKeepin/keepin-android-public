package com.keepin.android.data.datasource

import com.keepin.android.data.entity.request.RequestFindPassword
import com.keepin.android.data.entity.response.ResponseFindPassword

interface FindPasswordDataSource {

    suspend fun findPassword(body: RequestFindPassword): ResponseFindPassword
}
