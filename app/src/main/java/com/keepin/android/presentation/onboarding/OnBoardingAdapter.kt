package com.keepin.android.presentation.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keepin.android.base.BaseDiffUtilItemCallback
import com.keepin.android.databinding.ItemOnBoardingBinding

class OnBoardingAdapter(onBoardingList: List<OnBoarding>) :
    ListAdapter<OnBoarding, OnBoardingAdapter.OnBoardingViewHolder>(
        BaseDiffUtilItemCallback(
            itemsTheSame = { oldItem, newItem -> oldItem.position == newItem.position },
            contentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )
    ) {

    init {
        submitList(onBoardingList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOnBoardingBinding.inflate(inflater, parent, false)
        return OnBoardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OnBoardingViewHolder(private val binding: ItemOnBoardingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(onBoarding: OnBoarding) {
            binding.onBoarding = onBoarding
            binding.executePendingBindings()
        }
    }
}
