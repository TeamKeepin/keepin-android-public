package com.keepin.android.presentation.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentSignUpFirstBinding
import com.keepin.android.util.UtilDialog
import com.keepin.android.util.navigate
import com.keepin.android.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFirstFragment :
    BindingFragment<FragmentSignUpFirstBinding>(R.layout.fragment_sign_up_first) {
    private val signUpViewModel by activityViewModels<SignUpViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.signUpViewModel = signUpViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        signUpViewModel.resetEmailChecked()
        setBtnBackClickListener()
        setIsEmailCheckedObserve()
        return binding.root
    }

    private fun setBtnBackClickListener() {
        binding.btnSignUpFirstBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun setIsEmailCheckedObserve() {
        signUpViewModel.isEmailChecked.observe(viewLifecycleOwner) { isEmailChecked ->
            isEmailChecked?.let {
                when (isEmailChecked) {
                    true -> navigate(R.id.action_signUpFirstFragment_to_signUpSecondFragment)
                    false ->
                        UtilDialog(7) { signUpViewModel.isEmailDuplicate() }.show(
                            childFragmentManager,
                            "EMAIL_CHECK"
                        )
                }
            }
        }
    }
}
