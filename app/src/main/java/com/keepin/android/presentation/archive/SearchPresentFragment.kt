package com.keepin.android.presentation.archive

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.RadioGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentSearchPresentBinding
import com.keepin.android.util.KeyBoardUtil
import com.keepin.android.util.navigateWithData
import com.keepin.android.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchPresentFragment :
    BindingFragment<FragmentSearchPresentBinding>(R.layout.fragment_search_present) {

    private val searchPresentViewModel: SearchPresentViewModel by viewModels()
    private val presentAdapter by lazy { PresentAdapter(1) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = searchPresentViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setKeyBoardOpen()
        setListeners()
        observeData()
    }

    private fun setKeyBoardOpen() {
        KeyBoardUtil.show(requireContext(), binding.etSearchPresent)
    }

    private fun setListeners() {

        binding.imgbtnSearchCancel.setOnClickListener {
            popBackStack()
        }

        binding.etSearchPresent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0) {
                    binding.rvSearchPresent.isVisible = false
                    binding.layoutSearchCategory.isVisible = true
                    binding.imgSearchEmpty.isVisible = false
                    binding.tvSearchEmpty.isVisible = false
                } else {
                    searchPresentViewModel.requestPresentSearchList(s.toString())
                    setPresentAdapter()
                    binding.rvSearchPresent.isVisible = true
                    binding.layoutSearchCategory.isVisible = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.rgSearchCategory1.setOnCheckedChangeListener(listener1)
        binding.rgSearchCategory2.setOnCheckedChangeListener(listener2)
    }

    private fun setPresentAdapter() {
        binding.rvSearchPresent.layoutManager = GridLayoutManager(context, 2)
        binding.rvSearchPresent.adapter = presentAdapter
    }

    private fun observeData() {
        searchPresentViewModel.presentList.observe(viewLifecycleOwner) {
            presentAdapter.setItem(it)
            if (it.isNullOrEmpty()) {
                binding.imgSearchEmpty.isVisible = true
                binding.tvSearchEmpty.isVisible = true
            } else {
                presentAdapter.setItem(it)
                KeyBoardUtil.hide(requireActivity())
                binding.imgSearchEmpty.isVisible = false
                binding.tvSearchEmpty.isVisible = false
            }
        }
    }

    private val listener1: RadioGroup.OnCheckedChangeListener =
        RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                binding.rgSearchCategory2.clearCheck()
                binding.rgSearchCategory2.setOnCheckedChangeListener(null)
                binding.rgSearchCategory2.setOnCheckedChangeListener(listener2)
                binding.rgSearchCategory2.clearCheck()
                binding.rgSearchCategory1.clearCheck()
                when (checkedId) {
                    R.id.btn_search_category_birth -> {
                        navigateWithData(
                            SearchPresentFragmentDirections.actionSearchPresentFragmentToSearchPresentCategoryFragment(
                                binding.btnSearchCategoryBirth.text.toString()
                            )
                        )
                        KeyBoardUtil.hide(requireActivity())
                    }
                    R.id.btn_search_category_anniversary -> {
                        navigateWithData(
                            SearchPresentFragmentDirections.actionSearchPresentFragmentToSearchPresentCategoryFragment(
                                binding.btnSearchCategoryAnniversary.text.toString()
                            )
                        )
                        KeyBoardUtil.hide(requireActivity())
                    }
                    R.id.btn_search_category_celeb -> {
                        navigateWithData(
                            SearchPresentFragmentDirections.actionSearchPresentFragmentToSearchPresentCategoryFragment(
                                binding.btnSearchCategoryCeleb.text.toString()
                            )
                        )
                        KeyBoardUtil.hide(requireActivity())
                    }
                    R.id.btn_search_category_compliment -> {
                        navigateWithData(
                            SearchPresentFragmentDirections.actionSearchPresentFragmentToSearchPresentCategoryFragment(
                                binding.btnSearchCategoryCompliment.text.toString()
                            )
                        )
                        KeyBoardUtil.hide(requireActivity())
                    }
                }
            }
        }
    private val listener2: RadioGroup.OnCheckedChangeListener =
        RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                binding.rgSearchCategory1.clearCheck()
                binding.rgSearchCategory1.setOnCheckedChangeListener(null)
                binding.rgSearchCategory1.setOnCheckedChangeListener(listener1)
                binding.rgSearchCategory2.clearCheck()
                binding.rgSearchCategory1.clearCheck()
                when (checkedId) {
                    R.id.btn_search_category_support -> {
                        navigateWithData(
                            SearchPresentFragmentDirections.actionSearchPresentFragmentToSearchPresentCategoryFragment(
                                binding.btnSearchCategorySupport.text.toString()
                            )
                        )
                        KeyBoardUtil.hide(requireActivity())
                    }
                    R.id.btn_search_category_surprise -> {
                        navigateWithData(
                            SearchPresentFragmentDirections.actionSearchPresentFragmentToSearchPresentCategoryFragment(
                                binding.btnSearchCategorySurprise.text.toString()
                            )
                        )
                        KeyBoardUtil.hide(requireActivity())
                    }
                    R.id.btn_search_category_thanks -> {
                        navigateWithData(
                            SearchPresentFragmentDirections.actionSearchPresentFragmentToSearchPresentCategoryFragment(
                                binding.btnSearchCategoryThanks.text.toString()
                            )
                        )
                        KeyBoardUtil.hide(requireActivity())
                    }
                    R.id.btn_search_category_etc -> {
                        navigateWithData(
                            SearchPresentFragmentDirections.actionSearchPresentFragmentToSearchPresentCategoryFragment(
                                binding.btnSearchCategoryEtc.text.toString()
                            )
                        )
                        KeyBoardUtil.hide(requireActivity())
                    }
                }
            }
        }
}
