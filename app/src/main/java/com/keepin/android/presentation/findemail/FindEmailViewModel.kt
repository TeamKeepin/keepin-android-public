package com.keepin.android.presentation.findemail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.repository.FindEmailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class FindEmailViewModel @Inject constructor(
    private val findEmailRepository: FindEmailRepository
) : ViewModel() {
    val name = MutableStateFlow("")

    val birthDay = MutableStateFlow("")

    val phoneNumber = MutableStateFlow("")

    private var apiBirthDay = ""

    val canFindEmail = combine(name, birthDay, phoneNumber) { name, birthDay, phoneNumber ->
        name.isNotBlank() && birthDay.isNotBlank() && phoneNumber.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val _tryFindEmail = MutableSharedFlow<Boolean>()

    val findEmail = _tryFindEmail.flatMapLatest {
        findEmailRepository.findEmail(name.value, apiBirthDay, phoneNumber.value)
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    fun setSelectedBirthDate(birthDay: String, apiBirthDay: String) {
        this.birthDay.value = birthDay
        this.apiBirthDay = apiBirthDay
    }

    fun clearText(text: MutableStateFlow<String>) {
        text.value = ""
    }

    fun findEmail() {
        viewModelScope.launch {
            _tryFindEmail.emit(true)
        }
    }
}

sealed class FindEmailApiResponse {
    data class Success(val email: String) : FindEmailApiResponse()
    data class Error(val exception: Throwable) : FindEmailApiResponse()
}
