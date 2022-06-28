package com.keepin.android.presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentMyPageBinding
import com.keepin.android.util.navigate
import com.keepin.android.util.navigateWithData
import com.keepin.android.util.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val myPageViewModel by viewModels<MyPageViewModel>()
    private val adapter by lazy { MyPageFriendsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = myPageViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBtnMyPageProfileClickListener()
        initBtnMyPageFriendAddClickListener()
        initRvMyPageFriend()
        initFriendsListCollect()
    }

    private fun initBtnMyPageProfileClickListener() {
        binding.btnMyPageProfile.setOnClickListener {
            navigate(R.id.action_myPageFragment_to_profileFragment)
        }
    }

    private fun initBtnMyPageFriendAddClickListener() {
        binding.btnMyPageFriendAdd.setOnClickListener {
            navigateWithData(MyPageFragmentDirections.actionMyPageFragmentToFriendUpdateFragment())
        }
    }

    private fun initRvMyPageFriend() {
        binding.rvMyPageFriend.adapter = adapter
        binding.rvMyPageFriend.setHasFixedSize(true)
    }

    private fun initFriendsListCollect() {
        repeatOnLifecycle {
            myPageViewModel.friendsList.collect { friendsList ->
                adapter.submitList(friendsList)
            }
        }
    }
}
