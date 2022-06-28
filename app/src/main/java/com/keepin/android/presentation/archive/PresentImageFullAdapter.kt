package com.keepin.android.presentation.archive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keepin.android.databinding.ItemArchiveDetailPresentFullImageBinding

class PresentImageFullAdapter :
    RecyclerView.Adapter<PresentImageFullAdapter.PresentImageFullViewHolder>() {
    private var item = emptyList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresentImageFullViewHolder {
        val binding =
            ItemArchiveDetailPresentFullImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return PresentImageFullViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PresentImageFullViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount() = item.size

    fun setItem(imgUrl: List<String>) {
        item = imgUrl
        notifyDataSetChanged()
    }

    class PresentImageFullViewHolder(private val binding: ItemArchiveDetailPresentFullImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: String) {
            binding.imageUrl = imgUrl
        }
    }
}
