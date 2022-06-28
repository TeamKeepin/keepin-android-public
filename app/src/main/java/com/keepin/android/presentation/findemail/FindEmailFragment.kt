package com.keepin.android.presentation.findemail

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentFindEmailBinding
import com.keepin.android.util.DatePickerDialog
import com.keepin.android.util.UtilDialog
import com.keepin.android.util.navigateWithData
import com.keepin.android.util.popBackStack
import com.keepin.android.util.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FindEmailFragment :
    BindingFragment<FragmentFindEmailBinding>(R.layout.fragment_find_email) {
    private val findEmailViewModel by viewModels<FindEmailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = findEmailViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBtnBackClickListener()
        initEtPhoneTextChangedListener()
        initEtBirthDayClickListener()
        initFindEmailCollect()
    }

    private fun initBtnBackClickListener() {
        binding.btnFindEmailBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun initEtPhoneTextChangedListener() {
        binding.etFindEmailPhoneNumber.addTextChangedListener(
            PhoneNumberFormattingTextWatcher("KR")
        )
    }

    private fun initEtBirthDayClickListener() {
        binding.etFindEmailBirthDay.setOnClickListener {
            DatePickerDialog() { date, networkDate ->
                findEmailViewModel.setSelectedBirthDate(date, networkDate)
            }.show(childFragmentManager, "SELECT_BIRTH")
        }
    }

    private fun initFindEmailCollect() {
        repeatOnLifecycle {
            findEmailViewModel.findEmail.collect { response ->
                when (response) {
                    is FindEmailApiResponse.Success -> {
                        val direction = FindEmailFragmentDirections
                        val action =
                            direction.actionFindEmailFragmentToFindEmailSuccessFragment(response.email)
                        navigateWithData(action)
                    }
                    is FindEmailApiResponse.Error -> {
                        UtilDialog(8) {}.show(childFragmentManager, "EMAIL_NOT_FOUND")
                    }
                }
            }
        }
    }
}
