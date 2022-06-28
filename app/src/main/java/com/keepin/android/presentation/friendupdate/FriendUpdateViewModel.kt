package com.keepin.android.presentation.friendupdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.repository.FriendUpdateRepository
import com.keepin.android.util.StringValidChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendUpdateViewModel @Inject constructor(
    private val repository: FriendUpdateRepository
) : ViewModel() {
    val friendName = MutableStateFlow("")

    private val _friendNameValid = MutableStateFlow(StringValidChecker.EMPTY)
    val friendNameValid = _friendNameValid.asStateFlow()

    private val _friendUpdate = MutableSharedFlow<Boolean>()
    val friendUpdate = _friendUpdate.asSharedFlow()

    init {
        viewModelScope.launch {
            friendName.collect { friendName ->
                _friendNameValid.value = when {
                    friendName.isBlank() -> StringValidChecker.EMPTY
                    else -> StringValidChecker.VALID
                }
            }
        }
    }

    fun addFriend() {
        viewModelScope.launch() {
            isSuccess(repository.addFriend(friendName.value).first())
        }
    }

    fun updateFriendName(friendId: String) {
        viewModelScope.launch() {
            isSuccess(repository.putFriendName(friendName.value, friendId).first())
        }
    }

    private suspend fun isSuccess(isSuccess: Boolean) {
        when (isSuccess) {
            true -> _friendUpdate.emit(true)
            false -> _friendNameValid.value = StringValidChecker.NOTVALID
        }
    }
}

enum class FriendUpdateMode { CREATE, UPDATE }
