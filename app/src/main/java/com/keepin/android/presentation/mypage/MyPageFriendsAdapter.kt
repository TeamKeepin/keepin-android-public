package com.keepin.android.presentation.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keepin.android.base.BaseDiffUtilItemCallback
import com.keepin.android.data.entity.response.Friend
import com.keepin.android.databinding.ItemMyPageFriendBinding
import com.keepin.android.util.navigateWithData

class MyPageFriendsAdapter :
    ListAdapter<Friend, MyPageFriendsAdapter.MyPageFriendsViewHolder>(
        BaseDiffUtilItemCallback(
            itemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
            contentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageFriendsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMyPageFriendBinding.inflate(inflater, parent, false)
        return MyPageFriendsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPageFriendsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyPageFriendsViewHolder(private val binding: ItemMyPageFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend) {
            binding.friend = friend
            binding.root.setOnClickListener { view ->
                val directions = MyPageFragmentDirections
                val action = directions.actionMyPageFragmentToFriendDetailFragment(friend.id)
                view.navigateWithData(action)
            }
            binding.executePendingBindings()
        }
    }
}
