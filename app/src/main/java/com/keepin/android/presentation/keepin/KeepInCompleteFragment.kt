package com.keepin.android.presentation.keepin

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentKeepInCompleteBinding

class KeepInCompleteFragment :
    BindingFragment<FragmentKeepInCompleteBinding>(R.layout.fragment_keep_in_complete) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setLottie()
        return binding.root
    }

    private fun setLottie() {
        binding.lottieKeepInComplete.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                requireView().findNavController()
                    .navigate(KeepInCompleteFragmentDirections.actionKeepInCompleteFragmentToArchiveFragment(true))
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }
}
