package com.keepin.android.util

import timber.log.Timber

object TimberUtil {
    fun timber(tag: String, msg: String) {
        Timber.tag(tag).d(msg)
    }
}
