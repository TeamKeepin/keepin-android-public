package com.keepin.android.presentation.archive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keepin.android.databinding.ItemArchiveDetailPresentImageBinding
import com.keepin.android.util.navigateWithData

class PresentImageAdapter : RecyclerView.Adapter<PresentImageAdapter.PresentImageViewHolder>() {

    private var item = emptyList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresentImageViewHolder {
        val binding =
            ItemArchiveDetailPresentImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return PresentImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PresentImageViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount() = item.size

    fun setItem(imgUrl: List<String>) {
        item = imgUrl
        notifyDataSetChanged()
    }

    inner class PresentImageViewHolder(private val binding: ItemArchiveDetailPresentImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: String) {
            binding.imageUrl = imgUrl
            binding.imgArchiveDetailImage.setOnClickListener {
                it.navigateWithData(
                    ArchiveDetailFragmentDirections.actionArchiveDetailFragmentToDetailImageFragment(
                        item.toTypedArray()
                    )
                )
            }
        }
    }
}
