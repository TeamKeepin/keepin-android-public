package com.keepin.android.presentation.friendsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keepin.android.BR
import com.keepin.android.data.entity.response.Friend
import com.keepin.android.databinding.ItemFriendSelectBinding

class FriendSelectedAdapter(
    private val friendSearchViewModel: FriendSearchViewModel
) :
    ListAdapter<Friend, FriendSelectedAdapter.FriendSelectedViewHolder>(FRIEND_DIFF_UTIL) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = FriendSelectedViewHolder(
        ItemFriendSelectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        friendSearchViewModel
    )

    override fun onBindViewHolder(
        holder: FriendSelectedViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class FriendSelectedViewHolder(
        private val binding: ItemFriendSelectBinding,
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
            binding.layoutItemFriendSelect.setOnClickListener {
                friendSearchViewModel.unSelectFriend(adapterPosition)
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
