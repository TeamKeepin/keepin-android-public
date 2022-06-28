package com.keepin.android.presentation.friendList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keepin.android.base.BaseDiffUtilItemCallback
import com.keepin.android.data.entity.response.Friend
import com.keepin.android.databinding.ItemFriendListBinding
import com.keepin.android.util.navigateWithData

class FriendListAdapter(friendsList: Array<Friend>) :
    ListAdapter<Friend, FriendListAdapter.FriendListViewHolder>(
        BaseDiffUtilItemCallback(
            itemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
            contentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )
    ) {
    init {
        submitList(friendsList.toList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFriendListBinding.inflate(inflater, parent, false)
        return FriendListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FriendListViewHolder(private val binding: ItemFriendListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend) {
            binding.friend = friend
            binding.btnFriendList.setOnClickListener { view ->
                val directions = FriendListFragmentDirections
                val action =
                    directions.actionFriendListFragmentToFriendDetailFragment(friend.id, true)
                view.navigateWithData(action)
            }
            binding.executePendingBindings()
        }
    }
}
