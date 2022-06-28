package com.keepin.android.presentation.friendsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keepin.android.BR
import com.keepin.android.data.entity.response.Friend
import com.keepin.android.databinding.ItemFriendSearchBinding

class FriendSearchedAdapter(
    private val friendSearchViewModel: FriendSearchViewModel
) :
    ListAdapter<Friend, FriendSearchedAdapter.FriendSearchedViewHolder>(FRIEND_DIFF_UTIL) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = FriendSearchedViewHolder(
        ItemFriendSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        friendSearchViewModel
    )

    override fun onBindViewHolder(
        holder: FriendSearchedViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class FriendSearchedViewHolder(
        private val binding: ItemFriendSearchBinding,
        private val friendSearchViewModel: FriendSearchViewModel,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend) {
            with(binding) {
                setVariable(BR.friend, friend)
                executePendingBindings()
            }
            setLayoutClickListener()
        }

        private fun setLayoutClickListener() {
            binding.layoutItemFriendSearch.setOnClickListener {
                friendSearchViewModel.selectFriend(adapterPosition)
            }
        }
    }

    companion object {
        private val FRIEND_DIFF_UTIL = object : DiffUtil.ItemCallback<Friend>() {
            override fun areItemsTheSame(oldItem: Friend, newItem: Friend) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Friend, newItem: Friend) =
                oldItem == newItem
        }
    }
}
