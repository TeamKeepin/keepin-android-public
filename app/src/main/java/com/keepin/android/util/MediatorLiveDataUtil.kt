package com.keepin.android.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

object MediatorLiveDataUtil {
    fun <T> initMediatorLiveData(
        sourceList: List<LiveData<*>>,
        valueCheck: () -> T
    ) = MediatorLiveData<T>().apply {
        sourceList.forEach { source ->
            addSource(source) { value = valueCheck() }
        }
    }
}
