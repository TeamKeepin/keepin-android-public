package com.keepin.android.presentation.findpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentFindPasswordBinding
import com.keepin.android.util.KeyBoardUtil
import com.keepin.android.util.UtilDialog
import com.keepin.android.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindPasswordFragment :
    BindingFragment<FragmentFindPasswordBinding>(R.layout.fragment_find_password) {

    private val findPasswordViewModel by viewModels<FindPasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.findPasswordViewModel = findPasswordViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setListener()
        observe()
        return binding.root
    }

    private fun setListener() {
        binding.tvFindPasswordConfirm.setOnClickListener {
            findPasswordViewModel.requestFindPassword()
            KeyBoardUtil.hide(requireActivity())
        }
        binding.imgbtnFindpasswordBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun observe() {
        findPasswordViewModel.isFindPwSuccess.observe(viewLifecycleOwner) {
            if (it) {
                UtilDialog(10) { popBackStack() }.show(
                    childFragmentManager,
                    "PASSWORD_CHANGE"
                )
            } else {
                UtilDialog(9) {}.show(childFragmentManager, "EMAIL_NOT_SIGN")
            }
        }
    }
}
