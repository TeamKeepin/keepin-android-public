package com.keepin.android.presentation.signup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentSignUpSecondBinding
import com.keepin.android.presentation.main.MainActivity
import com.keepin.android.util.DatePickerDialog
import com.keepin.android.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpSecondFragment :
    BindingFragment<FragmentSignUpSecondBinding>(R.layout.fragment_sign_up_second) {
    private val signUpViewModel by activityViewModels<SignUpViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.signUpViewModel = signUpViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        signUpViewModel.resetIsSignUp()
        signUpViewModel.resetSecondData()
        signUpViewModel.getFirebaseToken()
        setBtnBackClickListener()
        setEtBirthDayClickListener()
        setEtPhoneTextChangedListener()
        setTvServiceClickListener()
        setTvInformationClickListener()
        setIsSignUpObserve()
        return binding.root
    }

    private fun setBtnBackClickListener() {
        binding.btnSignUpSecondBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun setEtPhoneTextChangedListener() {
        binding.etSignUpPhoneNumber.addTextChangedListener(
            PhoneNumberFormattingTextWatcher(
                getString(R.string.sign_up_phone_country_code)
            )
        )
    }

    private fun setEtBirthDayClickListener() {
        binding.etSignUpBirthDay.setOnClickListener {
            DatePickerDialog() { date, networkDate ->
                signUpViewModel.setSelectedBirthDate(date, networkDate)
            }.show(childFragmentManager, "SELECT_BIRTH")
        }
    }

    private fun setTvServiceClickListener() {
        binding.tvSignUpSecondServiceTerms.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.link_service_terms))
                )
            )
        }
    }

    private fun setTvInformationClickListener() {
        binding.tvSignUpSecondPersonalInfo.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.link_service_personal_information))
                )
            )
        }
    }

    private fun setIsSignUpObserve() {
        signUpViewModel.isSignUp.observe(viewLifecycleOwner) { isSignUp ->
            when (isSignUp) {
                true -> {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }
}
