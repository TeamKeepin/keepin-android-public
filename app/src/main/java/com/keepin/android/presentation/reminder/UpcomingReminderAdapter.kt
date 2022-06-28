package com.keepin.android.presentation.reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keepin.android.R
import com.keepin.android.data.entity.response.Reminder
import com.keepin.android.databinding.ItemReminderUpcomingBinding
import com.keepin.android.util.navigateWithData

class UpcomingReminderAdapter(val viewModel: ReminderViewModel, val month: Int) :
    ListAdapter<Reminder, UpcomingReminderAdapter.UpcomingEventViewHolder>(ReminderDiffUtil) {
    class UpcomingEventViewHolder(
        private val binding: ItemReminderUpcomingBinding,
        private val viewModel: ReminderViewModel,
        private val month: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Reminder) {
            binding.event = event
            binding.viewModel = viewModel
            setSwitchClickListener()
            setCheckBoxClickListener()
            setItemLongClickListener(month)
            setItemClickListener(event.id)
        }

        private fun setSwitchClickListener() {
            binding.switchUpcoming.setOnClickListener {
                viewModel.putReminderAlarm(
                    binding.switchUpcoming.isChecked,
                    requireNotNull(binding.event).id
                )
            }
        }

        private fun setCheckBoxClickListener() {
            binding.checkBoxUpcoming.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!viewModel.isContainItem(requireNotNull(binding.event).id)) {
                        viewModel.addDeleteList(requireNotNull(binding.event).id)
                    }
                    requireNotNull(viewModel.reminderList.value)[month].upcomingReminders[adapterPosition].isChecked =
                        true
                } else {
                    viewModel.removeDeleteList(requireNotNull(binding.event).id)
                    requireNotNull(viewModel.reminderList.value)[month].upcomingReminders[adapterPosition].isChecked =
                        false
                }
            }
        }

        private fun setItemLongClickListener(month: Int) {
            binding.layoutUpcoming.setOnLongClickListener {
                if (!requireNotNull(viewModel.isEdit.value)) {
                    viewModel.setLongClickPosition(false, month, adapterPosition)
                    viewModel.setIsEdit(true)
                }
                true
            }
        }

        private fun setItemClickListener(eventId: String) {
            binding.layoutUpcoming.setOnClickListener {
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
    }

    override fun onBindViewHolder(
        holder: UpcomingEventViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpcomingEventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemReminderUpcomingBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_reminder_upcoming, parent, false)
        return UpcomingEventViewHolder(binding, viewModel, month)
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
