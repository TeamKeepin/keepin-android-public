package com.keepin.android.presentation.frienddetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keepin.android.data.entity.response.KeepInData
import com.keepin.android.databinding.ItemArchivePresentBinding
import com.keepin.android.util.navigateWithData

class FriendDetailAdapter :
    ListAdapter<KeepInData, FriendDetailAdapter.FriendKeepInViewHolder>(FRIEND_DETAIL_DIFF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FriendKeepInViewHolder(
            ItemArchivePresentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: FriendKeepInViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class FriendKeepInViewHolder(
        private val binding: ItemArchivePresentBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(keepIn: KeepInData) {
            with(binding) {
                present = keepIn
                executePendingBindings()
            }
            setLayoutClickListener(keepIn)
        }

        private fun setLayoutClickListener(keepIn: KeepInData) {
            binding.layoutArchivePresent.setOnClickListener { view ->
                view.navigateWithData(
                    FriendDetailFragmentDirections.actionFriendDetailFragmentToArchiveDetailFragment(
                        keepIn.id
                    )
                )
            }
        }
    }

    companion object {
        private val FRIEND_DETAIL_DIFF_UTIL = object : DiffUtil.ItemCallback<KeepInData>() {
            override fun areItemsTheSame(oldItem: KeepInData, newItem: KeepInData) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: KeepInData, newItem: KeepInData) =
                oldItem == newItem
        }
    }
}
