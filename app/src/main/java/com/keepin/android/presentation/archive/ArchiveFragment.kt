package com.keepin.android.presentation.archive

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentArchiveBinding
import com.keepin.android.util.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArchiveFragment : BindingFragment<FragmentArchiveBinding>(R.layout.fragment_archive) {

    private val archiveViewModel: ArchiveViewModel by viewModels()
    private val presentAdapter by lazy { PresentAdapter(0) }
    private val args by navArgs<ArchiveFragmentArgs>()

    var checkTaken: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.vm = archiveViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        if (args.keepinTaken) {
            requestPresentItem(true, true)
            binding.tvArchiveGive.setTextColor(Color.parseColor("#a5a5a5"))
            binding.viewArchiveDivGiven.setBackgroundColor(Color.parseColor("#a5a5a5"))
            checkTaken = true
            binding.tvArchiveTaken.setTextColor(Color.parseColor("#15bd6f"))
            binding.viewArchiveDivTaken.visibility = View.VISIBLE
            binding.viewArchiveDivGiven.visibility = View.INVISIBLE
        } else {
            requestPresentItem(false, true)
            binding.tvArchiveGive.setTextColor(Color.parseColor("#15bd6f"))
            binding.viewArchiveDivGiven.setBackgroundColor(Color.parseColor("#15bd6f"))
            binding.tvArchiveTaken.setTextColor(Color.parseColor("#a5a5a5"))
            binding.viewArchiveDivTaken.visibility = View.INVISIBLE
            binding.viewArchiveDivGiven.visibility = View.VISIBLE
            checkTaken = false
        }
        observeData()
        setPresentAdapter()
        setListeners()
        return binding.root
    }

    private fun setPresentAdapter() {
        binding.recyclerviewArchiveItem.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerviewArchiveItem.adapter = presentAdapter
    }

    private fun requestPresentItem(taken: Boolean, recent: Boolean) {
        archiveViewModel.requestPresentTaken(taken, recent)
    }

    private fun observeData() {
        archiveViewModel.presentList.observe(viewLifecycleOwner) {
            presentAdapter.setItem(it)
            binding.recyclerviewArchiveItem.smoothScrollToPosition(0)
            if (it.isNullOrEmpty()) {
                binding.imgArchiveEmpty.isVisible = true
                binding.tvArchiveEmpty.isVisible = true
            } else {
                binding.imgArchiveEmpty.isVisible = false
                binding.tvArchiveEmpty.isVisible = false
            }
        }
    }

    private fun setListeners() {
        binding.layoutArchiveAppbarTitle.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

                if (kotlin.math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                    binding.imgArchiveHideSearch.isVisible = true
                    binding.tvArchiveNew.isVisible = false
                    binding.imgArchiveOp.isVisible = false
                } else {
                    binding.imgArchiveHideSearch.isVisible = false
                    binding.tvArchiveNew.isVisible = true
                    binding.imgArchiveOp.isVisible = true
                }
            }
        )
        binding.imgbtnArchiveSearch.setOnClickListener {
            navigate(R.id.action_archiveFragment_to_searchPresentFragment)
        }
        binding.imgArchiveHideSearch.setOnClickListener {
            navigate(R.id.action_archiveFragment_to_searchPresentFragment)
        }
        binding.tvArchiveGive.setOnClickListener { // 준
            if (binding.tvArchiveNew.text.equals("최신순")) {
                archiveViewModel.requestPresentTaken(false, true)
            } else {
                archiveViewModel.requestPresentTaken(false, false)
            }
            binding.tvArchiveGive.setTextColor(Color.parseColor("#15bd6f"))
            binding.viewArchiveDivGiven.setBackgroundColor(Color.parseColor("#15bd6f"))
            binding.tvArchiveTaken.setTextColor(Color.parseColor("#a5a5a5"))
            binding.viewArchiveDivTaken.visibility = View.INVISIBLE
            binding.viewArchiveDivGiven.visibility = View.VISIBLE
            checkTaken = false
        }
        binding.tvArchiveTaken.setOnClickListener { // 받은
            if (binding.tvArchiveNew.text.equals("오래된순")) {
                archiveViewModel.requestPresentTaken(true, false)
            } else {
                archiveViewModel.requestPresentTaken(true, true)
            }
            binding.tvArchiveGive.setTextColor(Color.parseColor("#a5a5a5"))
            binding.viewArchiveDivGiven.setBackgroundColor(Color.parseColor("#a5a5a5"))
            binding.tvArchiveTaken.setTextColor(Color.parseColor("#15bd6f"))
            binding.viewArchiveDivTaken.visibility = View.VISIBLE
            binding.viewArchiveDivGiven.visibility = View.INVISIBLE
            checkTaken = true
        }
        binding.tvArchiveNew.setOnClickListener {

            if (binding.tvArchiveNew.text.equals("최신순")) {
                binding.tvArchiveNew.text = "오래된순"
                if (checkTaken) {
                    archiveViewModel.requestPresentTaken(true, false)
                } else {
                    archiveViewModel.requestPresentTaken(false, false)
                }
                // 오래된거 나
            } else if (binding.tvArchiveNew.text.equals("오래된순")) {
                binding.tvArchiveNew.text = "최신순"
                // 최신거
                if (checkTaken) {
                    archiveViewModel.requestPresentTaken(taken = true, true)
                } else {
                    archiveViewModel.requestPresentTaken(false, true)
                }
            }
        }
    }
}
