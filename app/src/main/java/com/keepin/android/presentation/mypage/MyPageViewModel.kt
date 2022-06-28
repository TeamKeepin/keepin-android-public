package com.keepin.android.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    myPageRepository: MyPageRepository
) : ViewModel() {
    val userName = myPageRepository.getUserName()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    val keepInCount = myPageRepository.getMyKeepInCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val friendsList = myPageRepository.getFriendsList()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), listOf())
}
