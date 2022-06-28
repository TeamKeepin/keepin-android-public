package com.keepin.android.presentation.reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keepin.android.R
import com.keepin.android.data.entity.response.Reminder
import com.keepin.android.databinding.ItemReminderPastBinding
import com.keepin.android.util.navigate
import com.keepin.android.util.navigateWithData

class PastReminderAdapter(val viewModel: ReminderViewModel, val month: Int) :
    ListAdapter<Reminder, PastReminderAdapter.PastEventViewHolder>(ReminderDiffUtil) {
    class PastEventViewHolder(
        private val binding: ItemReminderPastBinding,
        private val viewModel: ReminderViewModel,
        private val month: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Reminder) {
            binding.event = event
            binding.viewModel = viewModel
            setCheckBoxClickListener()
            setItemLongClickListener(month)
            setItemClickListener(event.id)
            setBtnKeepInClickListener()
        }

        private fun setCheckBoxClickListener() {
            binding.checkBoxPast.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!viewModel.isContainItem(requireNotNull(binding.event).id)) {
                        viewModel.addDeleteList(requireNotNull(binding.event).id)
                    }
                    requireNotNull(viewModel.reminderList.value)[month].pastReminders[adapterPosition].isChecked =
                        true
                } else {
                    viewModel.removeDeleteList(requireNotNull(binding.event).id)
                    requireNotNull(viewModel.reminderList.value)[month].pastReminders[adapterPosition].isChecked =
                        false
                }
            }
        }

        private fun setItemLongClickListener(month: Int) {
            binding.layoutPast.setOnLongClickListener {
                if (!requireNotNull(viewModel.isEdit.value)) {
                    viewModel.setLongClickPosition(true, month, adapterPosition)
                    viewModel.setIsEdit(true)
                }
                true
            }
        }

        private fun setItemClickListener(eventId: String) {
            binding.layoutPast.setOnClickListener {
                if (!requireNotNull(viewModel.isEdit.value)) {
                    viewModel.setIsGoReminderUpdate(true)
                    it.navigateWithData(
                        ReminderFragmentDirections.actionReminderFragmentToReminderUpdateFragment(
                            reminderId = eventId
                        )
                    )
                }
            }
        }

        private fun setBtnKeepInClickListener() {
            binding.btnPastKeepIn.setOnClickListener {
                it.navigate(R.id.action_reminderFragment_to_keepInFragment)
            }
        }
    }

    override fun onBindViewHolder(
        holder: PastEventViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PastEventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemReminderPastBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_reminder_past, parent, false)
        return PastEventViewHolder(binding, viewModel, month)
    }

    companion object {
        private val ReminderDiffUtil = object : DiffUtil.ItemCallback<Reminder>() {
            override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean =
                oldItem.id == newItem.id
        }
    }
}
