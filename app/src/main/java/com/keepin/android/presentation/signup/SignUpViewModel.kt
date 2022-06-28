package com.keepin.android.presentation.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.keepin.android.data.repository.AuthRepository
import com.keepin.android.util.MediatorLiveDataUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordCheck = MutableLiveData("")
    val name = MutableLiveData("")
    val phoneNumber = MutableLiveData("")

    private val _birthDay = MutableLiveData<String>()
    val birthDay: LiveData<String> = _birthDay

    private var apiBirthDay = ""

    private val _isBirthSelected = MutableLiveData(false)
    val isBirthSelected: LiveData<Boolean> = _isBirthSelected

    private val isServiceChecked = MutableLiveData(false)

    private var fcmToken = ""

    private val _isSecondSuccess = MediatorLiveDataUtil.initMediatorLiveData(
        listOf(name, phoneNumber, isBirthSelected, isServiceChecked)
    ) { isSecondSuccessCheck() }
    val isSecondSuccess: LiveData<Boolean> = _isSecondSuccess

    private val _isSignUp = MutableLiveData<Boolean>()
    val isSignUp: LiveData<Boolean> = _isSignUp

    private val _emailValid = Transformations.map(email) { email ->
        email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    val emailValid: LiveData<Boolean> = _emailValid

    private val _passwordValid = Transformations.map(password) { password ->
        Pattern.matches("^(?=.*[a-zA-Z])(?=.*[0-9]).{8,}+$", password) || password.isBlank()
    }
    val passwordValid: LiveData<Boolean> = _passwordValid

    private val _isSamePassword = MediatorLiveDataUtil.initMediatorLiveData(
        listOf(password, passwordCheck)
    ) {
        isSamePasswordCheck()
    }
    val isSamePassword: LiveData<Boolean> = _isSamePassword

    private val _isFirstSuccess = MediatorLiveDataUtil.initMediatorLiveData(
        listOf(email, password, passwordCheck, emailValid, passwordValid, isSamePassword)
    ) { isFirstSuccessCheck() }
    val isFirstSuccess: LiveData<Boolean> = _isFirstSuccess

    private fun isSamePasswordCheck() =
        requireNotNull(password.value).isBlank() ||
            requireNotNull(passwordCheck.value).isBlank() ||
            password.value == passwordCheck.value

    private fun isFirstSuccessCheck() =
        _emailValid.value == true &&
            requireNotNull(email.value).isNotBlank() &&
            _passwordValid.value == true &&
            requireNotNull(password.value).isNotBlank() &&
            _isSamePassword.value == true &&
            requireNotNull(passwordCheck.value).isNotBlank()

    private val _isEmailChecked = MutableLiveData<Boolean?>()
    val isEmailChecked: LiveData<Boolean?> = _isEmailChecked

    fun emailCheck() {
        viewModelScope.launch {
            authRepository.postEmailCheck(requireNotNull(email.value)).collect { isEmailChecked ->
                _isEmailChecked.value = isEmailChecked
            }
        }
    }

    fun clearBirthDay() {
        _birthDay.value = ""
        _isBirthSelected.value = false
    }

    fun isEmailDuplicate() {
        _isFirstSuccess.value = false
    }

    fun setSelectedBirthDate(birthDay: String, apiBirthDay: String) {
        _birthDay.value = birthDay
        this.apiBirthDay = apiBirthDay
        _isBirthSelected.value = true
    }

    fun isCheckNotCheck() {
        isServiceChecked.value = !requireNotNull(isServiceChecked.value)
    }

    private fun isSecondSuccessCheck() =
        requireNotNull(name.value).isNotBlank() &&
            requireNotNull(phoneNumber.value).length == 13 &&
            _isBirthSelected.value == true &&
            isServiceChecked.value == true

    fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                fcmToken = task.result.toString()
            }
        )
    }

    fun signUp() {
        viewModelScope.launch {
            authRepository.postSignUp(
                email = requireNotNull(email.value),
                password = requireNotNull(password.value),
                name = requireNotNull(name.value),
                birth = apiBirthDay,
                phoneToken = fcmToken,
                phone = requireNotNull(phoneNumber.value)
            ).collect { result ->
                when (result) {
                    true -> requestSignIn()
                }
            }
        }
    }

    private fun requestSignIn() {
        viewModelScope.launch {
            authRepository.postSignIn(
                requireNotNull(email.value),
                requireNotNull(password.value),
                fcmToken
            ).collect {
                _isSignUp.postValue(true)
            }
        }
    }

    fun resetEmailChecked() {
        _isEmailChecked.value = null
    }

    fun resetSecondData() {
        name.value = ""
        _birthDay.value = ""
        phoneNumber.value = ""
    }

    fun resetIsSignUp() {
        _isSignUp.value = false
    }

    fun clearText(text: MutableLiveData<String>) {
        text.value = ""
    }
}
