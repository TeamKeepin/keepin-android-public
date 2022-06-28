package com.keepin.android.util

enum class StringValidChecker { EMPTY, VALID, NOTVALID }

fun String.stringValidCheck(isValidCheck: () -> Boolean): StringValidChecker {
    return when {
        isEmpty() -> StringValidChecker.EMPTY
        isValidCheck() -> StringValidChecker.VALID
        else -> StringValidChecker.NOTVALID
    }
}
