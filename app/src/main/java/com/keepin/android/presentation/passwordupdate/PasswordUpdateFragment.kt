package com.keepin.android.presentation.passwordupdate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentPasswordUpdateBinding
import com.keepin.android.util.KeyBoardUtil
import com.keepin.android.util.navigate
import com.keepin.android.util.popBackStack
import com.keepin.android.util.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PasswordUpdateFragment :
    BindingFragment<FragmentPasswordUpdateBinding>(R.layout.fragment_password_update) {
    private val passwordUpdateViewModel by viewModels<PasswordUpdateViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = passwordUpdateViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBtnBackClickListener()
        initLayoutClickListener()
        initFindPasswordClickListener()
        initPasswordUpdateCollect()
    }

    private fun initBtnBackClickListener() {
        binding.btnPasswordUpdateBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun initLayoutClickListener() {
        binding.layoutPasswordUpdate.setOnClickListener {
            KeyBoardUtil.hide(requireActivity())
        }
    }

    private fun initFindPasswordClickListener() {
        binding.tvPasswordUpdateFindPassword.setOnClickListener {
            navigate(R.id.action_passwordUpdateFragment_to_findPasswordFragment)
        }
    }

    private fun initPasswordUpdateCollect() {
        repeatOnLifecycle {
            passwordUpdateViewModel.passwordUpdate.filter { it }.collect {
                popBackStack()
            }
        }
    }
}
