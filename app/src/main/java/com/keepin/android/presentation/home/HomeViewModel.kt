package com.keepin.android.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.entity.response.RandomKeepIn
import com.keepin.android.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    val userName = homeRepository.getUserName()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    val welcomeText = homeRepository.getWelcomeText()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val reminderList = homeRepository.getComingReminder()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val _keepIn = MutableStateFlow<RandomKeepIn?>(null)
    val keepIn = _keepIn.asStateFlow()

    fun initRandomKeepIn() {
        viewModelScope.launch {
            if (keepIn.value == null) {
                getRandomKeepIn()
            }
        }
    }

    fun getRandomKeepIn() {
        viewModelScope.launch {
            homeRepository.getRandomKeepIn().collect { randomKeepIn ->
                _keepIn.value = randomKeepIn
            }
        }
    }

    fun setNoticeDialog() = homeRepository.setNoticeDialog()

    fun getNoticeDialog() = homeRepository.getNoticeDialog()

    fun isContainNoticeDialog() = homeRepository.isContainNoticeDialog()
}
