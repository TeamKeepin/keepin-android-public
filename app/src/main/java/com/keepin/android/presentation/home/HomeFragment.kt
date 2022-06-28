package com.keepin.android.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentHomeBinding
import com.keepin.android.util.NoticeDialog
import com.keepin.android.util.NoticeDialog.Companion.FIREBASE_NOTICE
import com.keepin.android.util.NoticeDialog.Companion.FIREBASE_NOTICE_SHOW
import com.keepin.android.util.navigateWithData
import com.keepin.android.util.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwipeRefreshLayout()
        initRandomKeepInCollect()
        homeViewModel.initRandomKeepIn()
        showNoticeDialog()
    }

    private fun initSwipeRefreshLayout() {
        with(binding.swipeHomeKeepinRefresh) {
            setColorSchemeColors(requireContext().getColor(R.color.green_15bd6f))
            setOnRefreshListener {
                homeViewModel.getRandomKeepIn()
            }
        }
    }

    private fun initRandomKeepInCollect() {
        repeatOnLifecycle {
            homeViewModel.keepIn.filterNotNull().collect { randomKeepIn ->
                setLayoutHomeKeepInClickListener(randomKeepIn.id)
                binding.swipeHomeKeepinRefresh.isRefreshing = false
            }
        }
    }

    private fun setLayoutHomeKeepInClickListener(id: String) {
        binding.imgRandomKeepin.setOnClickListener {
            navigateWithData(HomeFragmentDirections.actionHomeFragmentToArchiveDetailFragment(id))
        }
    }

    private fun showNoticeDialog() {
        Firebase.database.reference
            .child(FIREBASE_NOTICE).child(FIREBASE_NOTICE_SHOW).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val showNotice = snapshot.value
                        if (showNotice is Boolean && showNotice
                            && !homeViewModel.isContainNoticeDialog()
                            && !homeViewModel.getNoticeDialog()
                        ) {
                            NoticeDialog { notShowCheck ->
                                if (notShowCheck != null && notShowCheck) {
                                    homeViewModel.setNoticeDialog()
                                }
                            }.show(childFragmentManager, "notice_dialog")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.d(error.toString())
                    }
                }
            )
    }
}
