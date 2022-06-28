package com.keepin.android.presentation.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentOnBoardingBinding
import com.keepin.android.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment :
    BindingFragment<FragmentOnBoardingBinding>(R.layout.fragment_on_boarding) {
    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVpOnBoardingAdapter()
        initVpOverScrollModeNever()
        initVpOnBoardingPageCallBack()
        initTabOnBoardingMediator()
        initBtnOnBoardingStartClickListener()
    }

    private fun initVpOnBoardingAdapter() {
        binding.vpOnBoarding.adapter = OnBoardingAdapter(viewModel.onBoarding)
    }

    private fun initVpOnBoardingPageCallBack() {
        binding.vpOnBoarding.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.position = position
                    binding.executePendingBindings()
                }
            }
        )
    }

    private fun initVpOverScrollModeNever() {
        binding.vpOnBoarding.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    private fun initTabOnBoardingMediator() {
        TabLayoutMediator(binding.tabOnBoarding, binding.vpOnBoarding) { _, _ -> }.attach()
    }

    private fun initBtnOnBoardingStartClickListener() {
        binding.btnOnBoardingStart.setOnClickListener {
            viewModel.setOnBoarding()
            popBackStack()
        }
    }
}
