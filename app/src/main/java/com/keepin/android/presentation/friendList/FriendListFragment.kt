package com.keepin.android.presentation.friendList

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentFriendListBinding
import com.keepin.android.util.popBackStack

class FriendListFragment :
    BindingFragment<FragmentFriendListBinding>(R.layout.fragment_friend_list) {
    private val args by navArgs<FriendListFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBtnBackClickListener()
        initTvFriendListTitleText()
        initRvFriendListAdapter()
    }

    private fun initBtnBackClickListener() {
        binding.btnFriendListBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun initRvFriendListAdapter() {
        binding.rvFriendList.adapter = FriendListAdapter(args.friendsList)
        binding.rvFriendList.setHasFixedSize(true)
    }

    private fun initTvFriendListTitleText() {
        binding.tvFriendListTitle.text =
            getFormattedTitleText(args.friendsList[0].name, args.friendsList.size - 1)
    }

    private fun getFormattedTitleText(name: String, size: Int): String {
        return when (size == 0) {
            true -> getString(R.string.friend_list_title, name, "")
            false -> getString(R.string.friend_list_title, name, " 외 " + size.toString() + "명")
        }
    }
}
