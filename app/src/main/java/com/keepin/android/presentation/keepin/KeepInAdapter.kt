package com.keepin.android.presentation.keepin

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keepin.android.R
import com.keepin.android.databinding.ItemKeepInPhotoBinding

class KeepInAdapter(var viewModel: KeepInViewModel) :
    ListAdapter<Uri, KeepInAdapter.KeepInViewHolder>(KeepInDiffUtil()) {
    inner class KeepInViewHolder(
        private val binding: ItemKeepInPhotoBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(keepIn: Uri) {
            binding.data = keepIn.toString()
            binding.icKeepInDeletePhoto.setOnClickListener {
                viewModel.removeImageList(requireNotNull(binding.data).toUri())
                viewModel.removeImageMultipartList(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KeepInViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemKeepInPhotoBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_keep_in_photo, parent, false)
        return KeepInViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: KeepInViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    private class KeepInDiffUtil : DiffUtil.ItemCallback<Uri>() {
        override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }
    }
}
