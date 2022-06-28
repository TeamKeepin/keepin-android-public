package com.keepin.android.presentation.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentSignInBinding
import com.keepin.android.presentation.main.MainActivity
import com.keepin.android.util.UtilDialog
import com.keepin.android.util.UtilDialog.Companion.LOGIN_ERROR
import com.keepin.android.util.navigate
import com.keepin.android.util.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignInFragment :
    BindingFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {
    private val signInViewModel by viewModels<SignInViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.signInViewModel = signInViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        signInViewModel.resetText()
        signInViewModel.getFcmToken()
        setFindEmailClickListener()
        setFindPasswordClickListener()
        setIsSuccessLoginCollect()
        return binding.root
    }

    private fun setFindEmailClickListener() {
        binding.tvSignInInputFindEmail.setOnClickListener {
            navigate(R.id.action_signInFragment_to_findEmailFragment)
        }
    }

    private fun setFindPasswordClickListener() {
        binding.tvSignInInputFindPw.setOnClickListener {
            navigate(R.id.action_signInFragment_to_findPasswordFragment)
        }
    }

    private fun setIsSuccessLoginCollect() {
        repeatOnLifecycle {
            signInViewModel.isSuccessLogin.collect { isSuccessLogin ->
                when (isSuccessLogin) {
                    true -> {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finish()
                    }
                    false -> {
                        UtilDialog(LOGIN_ERROR) {}.show(
                            childFragmentManager,
                            "login_error"
                        )
                    }
                }
            }
        }
    }
}
