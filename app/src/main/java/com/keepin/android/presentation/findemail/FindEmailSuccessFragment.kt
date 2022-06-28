package com.keepin.android.presentation.findemail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentFindEmailSuccessBinding
import com.keepin.android.util.navigate

class FindEmailSuccessFragment :
    BindingFragment<FragmentFindEmailSuccessBinding>(R.layout.fragment_find_email_success) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBtnBackClickListener()
        initTvFindEmailSuccessText()
    }

    private fun initBtnBackClickListener() {
        binding.btnFindEmailSuccessBack.setOnClickListener {
            navigate(R.id.action_findEmailSuccessFragment_to_signInFragment)
        }
    }

    private fun initTvFindEmailSuccessText() {
        val args by navArgs<FindEmailSuccessFragmentArgs>()
        binding.tvFindEmailSuccess.text = args.email
    }
}
