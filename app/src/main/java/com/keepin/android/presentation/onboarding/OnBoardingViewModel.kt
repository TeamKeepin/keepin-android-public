package com.keepin.android.presentation.onboarding

import androidx.lifecycle.ViewModel
import com.keepin.android.R
import com.keepin.android.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val onBoarding = listOf(
        OnBoarding(
            position = 0,
            keepin = R.string.on_boarding_first,
            title = R.string.on_boarding_first_title,
            content = R.string.on_boarding_first_content,
            image = R.drawable.ic_on_boarding_first
        ),
        OnBoarding(
            position = 1,
            keepin = R.string.on_boarding_second,
            title = R.string.on_boarding_second_title,
            content = R.string.on_boarding_second_content,
            image = R.drawable.ic_on_boarding_second
        ),
        OnBoarding(
            position = 2,
            keepin = R.string.on_boarding_third,
            title = R.string.on_boarding_third_title,
            content = R.string.on_boarding_third_content,
            image = R.drawable.ic_on_boarding_third
        )
    )

    fun setOnBoarding() {
        authRepository.setOnBoarding()
    }
}
