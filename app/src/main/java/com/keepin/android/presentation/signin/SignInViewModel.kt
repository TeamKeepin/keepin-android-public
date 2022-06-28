package com.keepin.android.presentation.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.keepin.android.data.repository.AuthRepository
import com.keepin.android.util.MediatorLiveDataUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val email = MutableLiveData(authRepository.getUserEmail())
    val password = MutableLiveData(authRepository.getUserPassword())
    private var fcmToken = ""

    private val _canAutoSignIn = MutableLiveData(checkCanSignIn())
    val canAutoSignIn: LiveData<Boolean> = _canAutoSignIn

    private val _canSignIn =
        MediatorLiveDataUtil.initMediatorLiveData(listOf(email, password)) { checkCanSignIn() }
    val canSignIn: LiveData<Boolean> = _canSignIn

    private val _isSuccessLogin = MutableSharedFlow<Boolean>()
    val isSuccessLogin = _isSuccessLogin.asSharedFlow()

    private fun checkCanSignIn() =
        requireNotNull(email.value).isNotBlank() && requireNotNull(password.value).isNotBlank()

    fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                fcmToken = task.result.toString()
            }
        )
    }

    fun getOnBoarding() = authRepository.getOnBoarding()

    fun clearText(text: MutableLiveData<String>) {
        text.value = ""
    }

    fun resetText() {
        email.value = ""
        password.value = ""
    }

    fun postLogin() {
        viewModelScope.launch {
            authRepository.postSignIn(
                requireNotNull(email.value),
                requireNotNull(password.value),
                fcmToken
            ).collect { result ->
                _isSuccessLogin.emit(result)
            }
        }
    }
}
