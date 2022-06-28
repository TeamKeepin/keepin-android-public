package com.keepin.android.data.entity.request

data class RequestUpdatePassword(
    val currentPassword: String,
    val newPassword: String
)
