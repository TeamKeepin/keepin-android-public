package com.keepin.android.presentation.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentProfileBinding
import com.keepin.android.presentation.auth.AuthActivity
import com.keepin.android.util.UtilDialog
import com.keepin.android.util.navigate
import com.keepin.android.util.navigateWithData
import com.keepin.android.util.popBackStack
import com.keepin.android.util.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfileFragment : BindingFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = profileViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBtnBackClickListener()
        initLayoutPasswordClickListener()
        initFeedbackClickListener()
        initInstaLinkClickListener()
        initLogoutClickListener()
        initWithdrawalClickListener()
        initProfileCollect()
        initLayoutServiceClickListener()
        initLayoutInformationClickListener()
        initWithdrawalCollect()
    }

    private fun initBtnBackClickListener() {
        binding.btnProfileFinish.setOnClickListener {
            popBackStack()
        }
    }

    private fun initLayoutPasswordClickListener() {
        binding.tvProfilePassword.setOnClickListener {
            navigate(R.id.action_profileFragment_to_passwordUpdateFragment)
        }
    }

    private fun initFeedbackClickListener() {
        binding.tvProfileFeedback.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.profile_email_receiver)))
            intent.type = "message/rfc822"
            startActivity(intent)
        }
    }

    private fun initInstaLinkClickListener() {
        binding.tvProfileInsta.setOnClickListener {
            navigate(R.id.action_profileFragment_to_teamKeepInFragment)
        }
    }

    private fun initLogoutClickListener() {
        binding.tvProfileLogout.setOnClickListener {
            UtilDialog(UtilDialog.BEFORE_LOGOUT) {
                profileViewModel.clearUserData()
                logoutAndWithdrawal()
            }.show(childFragmentManager, "LOGOUT")
        }
    }

    private fun initWithdrawalClickListener() {
        binding.tvProfileWithdrawal.setOnClickListener {
            UtilDialog(UtilDialog.BEFORE_DELETE_AUTH) {
                profileViewModel.withdrawal()
            }.show(childFragmentManager, "WITHDRAWAL")
        }
    }

    private fun initLayoutServiceClickListener() {
        binding.tvProfileService.setOnClickListener {
            val uri = getString(R.string.link_service_terms)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        }
    }

    private fun initLayoutInformationClickListener() {
        binding.tvProfileInformation.setOnClickListener {
            val uri = getString(R.string.link_service_personal_information)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        }
    }

    private fun initProfileCollect() {
        repeatOnLifecycle {
            profileViewModel.profile.filterNotNull().collect { profile ->
                initLayoutNameClickListener(profile.name)
                initLayoutPhoneClickListener(profile.phone)
            }
        }
    }

    private fun initLayoutNameClickListener(name: String) {
        binding.tvProfileName.setOnClickListener {
            val directions = ProfileFragmentDirections
            val action = directions.actionProfileFragmentToProfileUpdateFragment(name = name)
            navigateWithData(action)
        }
    }

    private fun initLayoutPhoneClickListener(phone: String) {
        binding.tvProfilePhone.setOnClickListener {
            val directions = ProfileFragmentDirections
            val action = directions.actionProfileFragmentToProfileUpdateFragment(phone = phone)
            navigateWithData(action)
        }
    }

    private fun initWithdrawalCollect() {
        repeatOnLifecycle {
            profileViewModel.isWithdrawal.filter { it }.collect {
                logoutAndWithdrawal()
            }
        }
    }

    private fun logoutAndWithdrawal() {
        startActivity(Intent(requireActivity(), AuthActivity::class.java))
        requireActivity().finish()
    }
}
