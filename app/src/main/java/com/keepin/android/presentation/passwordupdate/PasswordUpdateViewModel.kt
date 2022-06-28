package com.keepin.android.presentation.passwordupdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.repository.ProfileUpdateRepository
import com.keepin.android.util.StringValidChecker
import com.keepin.android.util.stringValidCheck
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class PasswordUpdateViewModel @Inject constructor(
    private val repository: ProfileUpdateRepository
) : ViewModel() {
    val currentPassword = MutableStateFlow("")

    private val _currentPasswordValid = MutableStateFlow(StringValidChecker.EMPTY)
    val currentPasswordValid = _currentPasswordValid.asStateFlow()

    val newPassword = MutableStateFlow("")

    val newPasswordValid = newPassword.transform { newPassword ->
        val regex = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,}$"
        emit(newPassword.stringValidCheck { Pattern.matches(regex, newPassword) })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), StringValidChecker.EMPTY)

    val checkPassword = MutableStateFlow("")

    val checkPasswordValid = combine(checkPassword, newPassword) { checkPassword, newPassword ->
        checkPassword.stringValidCheck { checkPassword == newPassword }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), StringValidChecker.EMPTY)

    val canPasswordUpdate: StateFlow<Boolean> = combine(
        currentPasswordValid,
        newPasswordValid,
        checkPasswordValid
    ) { password, newValid, checkValid ->
        password == StringValidChecker.VALID && newValid == StringValidChecker.VALID && checkValid == StringValidChecker.VALID
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val tryPasswordUpdate = MutableSharedFlow<Boolean>()

    val passwordUpdate = tryPasswordUpdate.filter { it }.flatMapLatest {
        val catch = { _currentPasswordValid.value = StringValidChecker.NOTVALID }
        repository.putMyPassword(currentPassword.value, newPassword.value, catch)
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    init {
        viewModelScope.launch {
            currentPassword.collect { currentPassword ->
                _currentPasswordValid.value = when {
                    currentPassword.isEmpty() -> StringValidChecker.EMPTY
                    else -> StringValidChecker.VALID
                }
            }
        }
    }

    fun clearText(text: MutableStateFlow<String>) {
        text.value = ""
    }

    fun passwordUpdate() {
        viewModelScope.launch {
            tryPasswordUpdate.emit(true)
        }
    }
}
