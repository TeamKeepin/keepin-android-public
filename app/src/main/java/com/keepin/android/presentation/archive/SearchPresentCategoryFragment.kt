package com.keepin.android.presentation.archive

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentSearchPresentCategoryBinding
import com.keepin.android.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchPresentCategoryFragment :
    BindingFragment<FragmentSearchPresentCategoryBinding>(R.layout.fragment_search_present_category) {

    private val searchPresentCategoryViewModel: SearchPresentCategoryViewModel by viewModels()
    private val presentAdapter by lazy { PresentAdapter(2) }
    private val args by navArgs<SearchPresentCategoryFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = searchPresentCategoryViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setListeners()
        requestPresentItem()
        setPresentAdapter()
        observeData()
    }

    private fun setPresentAdapter() {
        binding.rvSearchCategory.layoutManager = GridLayoutManager(context, 2)
        binding.rvSearchCategory.adapter = presentAdapter
    }

    private fun requestPresentItem() {
        searchPresentCategoryViewModel.requestPresentList(args.categoryKeyword)
        searchPresentCategoryViewModel.setCategoryTitle(args.categoryKeyword)
    }

    private fun observeData() {
        searchPresentCategoryViewModel.presentList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.imgSearchCategoryEmpty.isVisible = true
                binding.tvSearchCategoryEmpty.isVisible = true
                binding.rvSearchCategory.isVisible = false
            } else {
                binding.imgSearchCategoryEmpty.isVisible = false
                binding.tvSearchCategoryEmpty.isVisible = false
                binding.rvSearchCategory.isVisible = true
                presentAdapter.setItem(it)
            }
        }
    }

    private fun setListeners() {
        binding.imgbtnSearchCategoryBack.setOnClickListener {
            popBackStack()
        }
    }
}
