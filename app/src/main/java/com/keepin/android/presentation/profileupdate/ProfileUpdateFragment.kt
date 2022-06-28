package com.keepin.android.presentation.profileupdate

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentProfileUpdateBinding
import com.keepin.android.util.popBackStack
import com.keepin.android.util.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class ProfileUpdateFragment :
    BindingFragment<FragmentProfileUpdateBinding>(R.layout.fragment_profile_update) {
    private val profileUpdateViewModel by viewModels<ProfileUpdateViewModel>()
    private val args by navArgs<ProfileUpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = profileUpdateViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileUpdateViewModel.initProfileCategory(getProfileCategory())
        initBtnBackClickListener()
        initProfileCategoryCollect()
        initProfileUpdateCollect()
    }

    private fun getProfileCategory(): ProfileUpdateCategory {
        return when {
            args.name.isEmpty() -> ProfileUpdateCategory.PHONE
            else -> ProfileUpdateCategory.NAME
        }
    }

    private fun initBtnBackClickListener() {
        binding.btnProfileUpdateBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun initProfileCategoryCollect() {
        repeatOnLifecycle {
            profileUpdateViewModel.category.filterNotNull().collect { category ->
                when (category) {
                    ProfileUpdateCategory.NAME -> binding.etProfileUpdate.setText(args.name)
                    ProfileUpdateCategory.PHONE -> initEtProfileUpdate()
                }
            }
        }
    }

    private fun initEtProfileUpdate() {
        with(binding.etProfileUpdate) {
            setText(args.phone)
            addTextChangedListener(PhoneNumberFormattingTextWatcher("KR"))
        }
    }

    private fun initProfileUpdateCollect() {
        repeatOnLifecycle {
            profileUpdateViewModel.profileUpdate.filter { it }.collect {
                popBackStack()
            }
        }
    }
}
