package com.keepin.android.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {
    val profile = repository.getMyProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val tryWithdrawal = MutableSharedFlow<Boolean>()

    val isWithdrawal = tryWithdrawal.filter { it }.flatMapLatest {
        repository.withdrawal()
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    fun withdrawal() {
        viewModelScope.launch {
            tryWithdrawal.emit(true)
        }
    }

    fun clearUserData() {
        repository.clearUserData()
    }
}
