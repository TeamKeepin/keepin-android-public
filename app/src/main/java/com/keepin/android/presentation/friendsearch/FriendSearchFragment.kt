package com.keepin.android.presentation.friendsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentFriendSearchBinding
import com.keepin.android.util.KeyBoardUtil
import com.keepin.android.util.navigateWithData
import com.keepin.android.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendSearchFragment :
    BindingFragment<FragmentFriendSearchBinding>(R.layout.fragment_friend_search) {
    private val friendSearchViewModel by viewModels<FriendSearchViewModel>()
    private val args by navArgs<FriendSearchFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.friendSearchViewModel = friendSearchViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initSelectedList()
        setLayoutFriendSearchClickListener()
        setBtnBackClickListener()
        setTvGoNewFriendClickListener()
        setTvCompleteClickListener()
        setRvSearchedAdapter()
        setRvSelectedAdapter()
        setFriendSearchQueryObserve()
        setFriendSearchedListObserve()
        setFriendSelectedListObserve()
        friendSearchViewModel.getFriendsList()
        return binding.root
    }

    private fun initSelectedList() {
        args.friendsList?.let { friendsList ->
            friendSearchViewModel.setSelectedList(friendsList.toList())
        }
    }

    private fun setLayoutFriendSearchClickListener() {
        binding.layoutFriendSearch.setOnClickListener {
            KeyBoardUtil.hide(requireActivity())
        }
    }

    private fun setBtnBackClickListener() {
        binding.btnFriendSearchBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun setTvGoNewFriendClickListener() {
        binding.tvFriendSearchAddFriend.setOnClickListener {
            navigateWithData(FriendSearchFragmentDirections.actionFriendSearchFragmentToFriendUpdateFragment())
        }
    }

    private fun setTvCompleteClickListener() {
        binding.tvFriendSearchComplete.setOnClickListener {
            setFriendSearchFragmentResult()
            popBackStack()
        }
    }

    private fun setFriendSearchFragmentResult() {
        setFragmentResult(
            "friendSearch",
            bundleOf(
                "friendName" to friendSearchViewModel.getSelectedFriendName(),
                "friendIdList" to friendSearchViewModel.getSelectedFriendIdList()
            )
        )
    }

    private fun setRvSearchedAdapter() {
        binding.rvFriendSearchSearched.adapter =
            FriendSearchedAdapter(friendSearchViewModel)
    }

    private fun setRvSelectedAdapter() {
        binding.rvFriendSearchSelected.adapter =
            FriendSelectedAdapter(friendSearchViewModel)
    }

    private fun setFriendSearchQueryObserve() {
        with(friendSearchViewModel) {
            query.observe(viewLifecycleOwner) {
                setSearchedList()
            }
        }
    }

    private fun setFriendSearchedListObserve() {
        friendSearchViewModel.friendSearchedList.observe(viewLifecycleOwner) { friendSearchedList ->
            with(binding.rvFriendSearchSearched.adapter as FriendSearchedAdapter) {
                submitList(friendSearchedList)
            }
        }
    }

    private fun setFriendSelectedListObserve() {
        friendSearchViewModel.friendSelectedList.observe(viewLifecycleOwner) { friendSelectedList ->
            with(binding.rvFriendSearchSelected.adapter as FriendSelectedAdapter) {
                submitList(friendSelectedList)
            }
        }
    }
}
