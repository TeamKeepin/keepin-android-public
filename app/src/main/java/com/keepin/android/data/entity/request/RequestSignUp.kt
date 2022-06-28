package com.keepin.android.data.entity.request

data class RequestSignUp(
    val email: String,
    val password: String,
    val name: String,
    val birth: String,
    val phoneToken: String,
    val phone: String
)
