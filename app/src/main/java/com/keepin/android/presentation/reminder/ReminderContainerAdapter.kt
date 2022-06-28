package com.keepin.android.presentation.reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.keepin.android.R
import com.keepin.android.data.entity.response.MonthReminder
import com.keepin.android.databinding.ItemReminderEventContainerBinding

class ReminderContainerAdapter(val viewModel: ReminderViewModel) :
    RecyclerView.Adapter<ReminderContainerAdapter.EventContainerViewHolder>() {
    private var reminderOfYear = emptyList<MonthReminder>()

    class EventContainerViewHolder(
        private val binding: ItemReminderEventContainerBinding,
        private val reminderViewModel: ReminderViewModel
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(monthReminder: MonthReminder, position: Int) {
            binding.upcomingEventSize = monthReminder.upcomingReminders.size
            binding.pastEventSize = monthReminder.pastReminders.size
            setMonthState(monthReminder, position)
            setUpcomingEventAdapter()
            setPastEventAdapter()
            submitUpcomingEventList(monthReminder)
            submitPastEventList(monthReminder)
        }

        private fun setMonthState(monthReminder: MonthReminder, position: Int) {
            binding.monthState = when {
                requireNotNull(reminderViewModel.selectedYear.value).toInt() > reminderViewModel.currentYear -> "upcoming"
                requireNotNull(reminderViewModel.selectedYear.value).toInt() < reminderViewModel.currentYear -> "past"
                position > reminderViewModel.currentMonth - 1 -> "upcoming"
                position < reminderViewModel.currentMonth - 1 -> "past"
                else -> "current"
            }
        }

        private fun setUpcomingEventAdapter() {
            binding.rvReminderEventUpcoming.adapter =
                UpcomingReminderAdapter(reminderViewModel, adapterPosition)
        }

        private fun setPastEventAdapter() {
            binding.rvReminderEventPast.adapter =
                PastReminderAdapter(reminderViewModel, adapterPosition)
        }

        private fun submitUpcomingEventList(monthReminder: MonthReminder) {
            with(binding.rvReminderEventUpcoming.adapter as UpcomingReminderAdapter) {
                submitList(monthReminder.upcomingReminders)
            }
        }

        private fun submitPastEventList(monthReminder: MonthReminder) {
            with(binding.rvReminderEventPast.adapter as PastReminderAdapter) {
                submitList(monthReminder.pastReminders)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventContainerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemReminderEventContainerBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.item_reminder_event_container,
                parent,
                false
            )
        return EventContainerViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: EventContainerViewHolder, position: Int) {
        holder.bind(reminderOfYear[position], position)
    }

    override fun getItemCount() = reminderOfYear.size

    fun setReminderOfYear(reminderOfYear: MutableList<MonthReminder>) {
        this.reminderOfYear = reminderOfYear
        notifyDataSetChanged()
    }
}
