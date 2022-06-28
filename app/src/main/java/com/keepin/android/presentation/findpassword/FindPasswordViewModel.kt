package com.keepin.android.presentation.findpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.entity.request.RequestFindPassword
import com.keepin.android.data.repository.FindPasswordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FindPasswordViewModel @Inject constructor(private val findPasswordRepository: FindPasswordRepository) :
    ViewModel() {

    val email = MutableLiveData("")

    fun clearEmailText() {
        email.value = ""
    }

    private val _isFindPwSuccess = MutableLiveData<Boolean>()
    val isFindPwSuccess: LiveData<Boolean> = _isFindPwSuccess

    fun requestFindPassword() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = findPasswordRepository.findPassword(
                    RequestFindPassword(email.value!!)
                )

                if (response?.status == 200) {
                    _isFindPwSuccess.postValue(true)
                } else {
                    _isFindPwSuccess.postValue(false)
                }
            } catch (e: HttpException) {
                _isFindPwSuccess.postValue(false)
                Timber.d(e.message())
                Timber.d(e.response().toString())
                Timber.d(e.code().toString())
            }
        }
    }
}
