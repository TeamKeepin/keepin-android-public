package com.keepin.android.util

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber

fun getErrorMessage(e: Throwable): String {
    return when (e) {
        is HttpException -> parseErrorBody(e.response()?.errorBody()) ?: e.toString()
        else -> e.toString()
    }
}

private fun parseErrorBody(responseBody: ResponseBody?): String? {
    responseBody?.let { body ->
        val error = JSONObject(body.string())
        Timber.d(error.toString())
        return error.getString("message")
    }
    return null
}
