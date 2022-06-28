package com.keepin.android.presentation.archive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keepin.android.data.entity.response.KeepInData
import com.keepin.android.databinding.ItemArchivePresentBinding
import com.keepin.android.util.navigateWithData

class PresentAdapter(viewId: Int) : RecyclerView.Adapter<PresentAdapter.PresentViewHolder>() {

    private var item = mutableListOf<KeepInData>()
    var Id = viewId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresentViewHolder {
        val binding =
            ItemArchivePresentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PresentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PresentViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount() = item.size

    fun setItem(presentList: List<KeepInData>) {
        val diffResult = DiffUtil.calculateDiff(ContentDiffUtil(item, presentList), false)
        // item.clear()
        item = presentList as MutableList<KeepInData>
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ContentDiffUtil(
        private val oldList: List<KeepInData>,
        private val currentList: List<KeepInData>
    ) : DiffUtil.Callback() {
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == currentList[newItemPosition]
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == currentList[newItemPosition].id
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = currentList.size
    }

    inner class PresentViewHolder(private val binding: ItemArchivePresentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(present: KeepInData) {
            binding.present = present
            binding.layoutArchivePresent.setOnClickListener {
                when (Id) {
                    0 ->
                        it.navigateWithData(
                            ArchiveFragmentDirections.actionArchiveFragmentToArchiveDetailFragment(
                                present.id
                            )
                        )
                    1 -> it.navigateWithData(
                        SearchPresentFragmentDirections.actionSearchPresentFragmentToArchiveDetailFragment(
                            present.id
                        )
                    )
                    else -> it.navigateWithData(
                        SearchPresentCategoryFragmentDirections.actionSearchPresentCategoryFragmentToArchiveDetailFragment(
                            present.id
                        )
                    )
                }
            }
        }
    }
}
