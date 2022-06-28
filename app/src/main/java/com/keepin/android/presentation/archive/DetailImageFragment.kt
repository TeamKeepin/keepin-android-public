package com.keepin.android.presentation.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentDetailImageBinding
import com.keepin.android.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailImageFragment :
    BindingFragment<FragmentDetailImageBinding>(R.layout.fragment_detail_image) {

    private val presentImageFullAdapter by lazy { PresentImageFullAdapter() }
    private val archiveDetailViewModel: ArchiveDetailViewModel by viewModels()
    private val args by navArgs<DetailImageFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.vm = archiveDetailViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setImageAdapter()
        setListener()
        return binding.root
    }

    private fun setImageAdapter() {
        binding.vpDetailImage.offscreenPageLimit = 1
        binding.vpDetailImage.adapter = presentImageFullAdapter
        args.detailImage?.let {
            presentImageFullAdapter.setItem(it.toList())
            if (it.size > 1) {
                setIndicator(binding)
            }
        }
    }

    private fun setIndicator(binding: FragmentDetailImageBinding) {
        TabLayoutMediator(binding.tabDetailImage, binding.vpDetailImage) { _, _ ->
        }.attach()
    }

    private fun setListener() {
        binding.btnDetailImageClose.setOnClickListener {
            popBackStack()
        }
    }
}
