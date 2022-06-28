package com.keepin.android.presentation.profileupdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.repository.ProfileUpdateRepository
import com.keepin.android.util.stringValidCheck
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class ProfileUpdateViewModel @Inject constructor(
    private val repository: ProfileUpdateRepository
) : ViewModel() {
    val profile = MutableStateFlow("")

    private val _category = MutableStateFlow<ProfileUpdateCategory?>(null)
    val category = _category.asStateFlow()

    val profileValid = combine(profile, category.filterNotNull()) { profile, category ->
        when (category) {
            ProfileUpdateCategory.NAME -> {
                profile.stringValidCheck { profile.isNotBlank() }
            }
            ProfileUpdateCategory.PHONE -> {
                val regex = "^\\d{3}-\\d{3,4}-\\d{4}$"
                profile.stringValidCheck { Pattern.matches(regex, profile) }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val _profileUpdate = MutableSharedFlow<Boolean>()
    val profileUpdate = _profileUpdate.asSharedFlow()

    fun initProfileCategory(category: ProfileUpdateCategory) {
        _category.value = category
    }

    fun profileUpdate() {
        when (category.value) {
            ProfileUpdateCategory.NAME -> putMyProfileName()
            ProfileUpdateCategory.PHONE -> putMyPhone()
            else -> return
        }
    }

    private fun putMyProfileName() {
        viewModelScope.launch() {
            val response = repository.putMyProfileName(profile.value).first()
            _profileUpdate.emit(response)
        }
    }

    private fun putMyPhone() {
        viewModelScope.launch() {
            val response = repository.putMyPhone(profile.value).first()
            _profileUpdate.emit(response)
        }
    }
}

enum class ProfileUpdateCategory { NAME, PHONE }
