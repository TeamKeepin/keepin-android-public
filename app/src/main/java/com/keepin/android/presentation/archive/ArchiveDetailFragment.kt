package com.keepin.android.presentation.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.data.entity.response.Friend
import com.keepin.android.databinding.FragmentArchiveDetailBinding
import com.keepin.android.util.UtilDialog
import com.keepin.android.util.navigateWithData
import com.keepin.android.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArchiveDetailFragment :
    BindingFragment<FragmentArchiveDetailBinding>(R.layout.fragment_archive_detail),
    PopupMenu.OnMenuItemClickListener {

    private val archiveDetailViewModel: ArchiveDetailViewModel by viewModels()
    private val presentImageAdapter by lazy { PresentImageAdapter() }
    private val args by navArgs<ArchiveDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.vm = archiveDetailViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        requestImage()
        observeData()
        setListeners()
        return binding.root
    }

    private fun requestImage() {
        archiveDetailViewModel.requestPresentDetail(args.keepinIdx)
        setShopImageAdapter()
    }

    private fun setListeners() {
        binding.btnArchiveDetailBack.setOnClickListener {
            popBackStack()
        }
        binding.btnArchiveDetailMore.setOnClickListener {
            PopupMenu(
                ContextThemeWrapper(
                    requireContext(),
                    R.style.friend_option_popup_menu
                ),
                it
            ).apply {
                setOnMenuItemClickListener(this@ArchiveDetailFragment)
                inflate(R.menu.menu_present_detail_option)
                show()
            }
        }
    }

    private fun setShopImageAdapter() {
        binding.vpArchiveDetail.offscreenPageLimit = 1
        binding.vpArchiveDetail.adapter = presentImageAdapter
        setIndicator()
    }

    private fun setIndicator() {
        TabLayoutMediator(binding.tabArchiveDetail, binding.vpArchiveDetail) { _, _ ->
        }.attach()
    }

    private fun observeData() {
        archiveDetailViewModel.presentDetail.observe(viewLifecycleOwner) {
            presentImageAdapter.setItem(it.data.photo)
            setTvArchiveDetailNameClickListener(it.data.friends.toTypedArray())
        }
        archiveDetailViewModel.isUpdate.observe(viewLifecycleOwner) { isUpdate ->
            if (isUpdate) {
                archiveDetailViewModel.resetIsUpdate()
                archiveDetailViewModel.taken.observe(viewLifecycleOwner) { taken ->
                    if (taken == true) {
                        requireView().navigateWithData(
                            ArchiveDetailFragmentDirections.actionArchiveDetailFragmentToArchiveFragment(
                                true
                            )
                        )
                    } else {
                        requireView().navigateWithData(
                            ArchiveDetailFragmentDirections.actionArchiveDetailFragmentToArchiveFragment(
                                false
                            )
                        )
                    }
                }
            }
        }
    }

    private fun setTvArchiveDetailNameClickListener(friendsList: Array<Friend>) {
        binding.tvArchiveDetailName.setOnClickListener {
            val directions = ArchiveDetailFragmentDirections
            val action = directions.actionArchiveDetailFragmentToFriendListFragment(friendsList)
            navigateWithData(action)
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_present_detail_update -> {
                requireView().navigateWithData(
                    ArchiveDetailFragmentDirections.actionArchiveDetailFragmentToKeepInFragment(
                        requireNotNull(archiveDetailViewModel.presentDetail.value)
                    )
                )
                true
            }
            R.id.menu_present_detail_delete -> {
                UtilDialog(3) {
                    archiveDetailViewModel.deletePresent(args.keepinIdx)
                }.show(childFragmentManager, "DELETE_POST")
                true
            }
            else -> false
        }
    }
}
