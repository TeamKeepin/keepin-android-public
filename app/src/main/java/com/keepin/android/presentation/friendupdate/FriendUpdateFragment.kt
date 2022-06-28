package com.keepin.android.presentation.friendupdate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentFriendUpdateBinding
import com.keepin.android.util.popBackStack
import com.keepin.android.util.repeatOnLifecycle
import com.keepin.android.util.toastMessageUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class FriendUpdateFragment :
    BindingFragment<FragmentFriendUpdateBinding>(R.layout.fragment_friend_update) {
    private val friendUpdateViewModel by viewModels<FriendUpdateViewModel>()
    private val args by navArgs<FriendUpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = friendUpdateViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBtnBackClickListener()
        initFriendUpdateMode()
        initEtFriendNameText()
        initTvFriendUpdateClickListener()
        initFriendUpdateCollect()
    }

    private fun initFriendUpdateMode() {
        binding.mode = getFriendUpdateMode()
        binding.executePendingBindings()
    }

    private fun getFriendUpdateMode(): FriendUpdateMode {
        return when {
            args.friendId.isEmpty() -> FriendUpdateMode.CREATE
            else -> FriendUpdateMode.UPDATE
        }
    }

    private fun initBtnBackClickListener() {
        binding.btnFriendUpdateBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun initEtFriendNameText() {
        binding.etFriendUpdate.setText(args.friendName)
    }

    private fun initTvFriendUpdateClickListener() {
        binding.tvFriendUpdate.setOnClickListener {
            when (getFriendUpdateMode()) {
                FriendUpdateMode.CREATE -> friendUpdateViewModel.addFriend()
                FriendUpdateMode.UPDATE -> friendUpdateViewModel.updateFriendName(args.friendId)
            }
        }
    }

    private fun initFriendUpdateCollect() {
        repeatOnLifecycle {
            friendUpdateViewModel.friendUpdate.filter { it }.collect {
                showFriendUpdateToast()
                popBackStack()
            }
        }
    }

    private fun showFriendUpdateToast() {
        if (getFriendUpdateMode() == FriendUpdateMode.CREATE) {
            val name = binding.etFriendUpdate.text.toString()
            requireContext().toastMessageUtil(getString(R.string.friend_update_toast, name))
        }
    }
}
