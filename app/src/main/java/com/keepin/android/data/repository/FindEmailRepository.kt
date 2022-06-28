package com.keepin.android.data.repository

import com.keepin.android.data.datasource.FindEmailDataSource
import com.keepin.android.data.entity.request.RequestFindEmail
import com.keepin.android.presentation.findemail.FindEmailApiResponse
import com.keepin.android.util.getErrorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class FindEmailRepository @Inject constructor(
    private val findEmailDataSource: FindEmailDataSource
) {
    fun findEmail(name: String, birth: String, phone: String): Flow<FindEmailApiResponse> {
        return flow<FindEmailApiResponse> {
            val body = RequestFindEmail(name = name, birth = birth, phone = phone)
            emit(FindEmailApiResponse.Success(findEmailDataSource.findEmail(body).email))
        }.catch { e ->
            emit(FindEmailApiResponse.Error(e))
            Timber.d(getErrorMessage(e))
        }
    }
}
