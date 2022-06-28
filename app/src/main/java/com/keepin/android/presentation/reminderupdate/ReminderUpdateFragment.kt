package com.keepin.android.presentation.reminderupdate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentReminderUpdateBinding
import com.keepin.android.util.DatePickerDialog
import com.keepin.android.util.KeyBoardUtil
import com.keepin.android.util.UtilDialog
import com.keepin.android.util.UtilDialog.Companion.STOP_WRITE
import com.keepin.android.util.popBackStack
import com.keepin.android.util.toastMessageUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ReminderUpdateFragment :
    BindingFragment<FragmentReminderUpdateBinding>(R.layout.fragment_reminder_update) {
    private val reminderUpdateViewModel by viewModels<ReminderUpdateViewModel>()
    private val args by navArgs<ReminderUpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = reminderUpdateViewModel
        reminderUpdateViewModel.getReminderDetail(args.reminderId)
        setMode()
        setEmptyEventTitleEditText()
        setCloseBtnClickListener()
        setDoneTextClickListener()
        setDateTextClickListener()
        setAlarmTextClickListener()
        setAlarmSwitchClickListener()
        setImportantCheckClickListener()
        setImportantTextClickListener()
        setReminderTitleObserve()
        return binding.root
    }

    private fun setMode() {
        if (args.reminderId == "") {
            reminderUpdateViewModel.clearDetailReminderData()
            setDefaultDateText()
            setDefaultAlarmText()
        }
    }

    private fun setEmptyEventTitleEditText() {
        reminderUpdateViewModel.setReminderTitleAtEditText("")
    }

    private fun setDefaultDateText() {
        val currentDate = Calendar.getInstance().time
        val currentDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        reminderUpdateViewModel.setSelectedEventDate(
            currentDateFormat.format(currentDate),
            currentDateFormat.format(currentDate).replace(".", "-")
        )
    }

    private fun setDefaultAlarmText() {
        reminderUpdateViewModel.setSelectedAlarmTime(getString(R.string.alarm_picker_selected_today))
    }

    private fun setCloseBtnClickListener() {
        binding.btnReminderUpdateClose.setOnClickListener {
            if (requireNotNull(reminderUpdateViewModel.isUpdate.value)) {
                UtilDialog(STOP_WRITE) {
                    popBackStack()
                }.show(childFragmentManager, "reminder_update_close")
            } else {
                popBackStack()
            }
        }
    }

    private fun setDoneTextClickListener() {
        binding.tvReminderUpdateDone.setOnClickListener {
            if (args.reminderId == "") {
                reminderUpdateViewModel.postReminder(
                    binding.switchReminderUpdate.isChecked,
                    binding.checkBoxReminderUpdate.isChecked
                )
            } else {
                reminderUpdateViewModel.putReminder(
                    args.reminderId,
                    binding.switchReminderUpdate.isChecked,
                    binding.checkBoxReminderUpdate.isChecked
                )
            }
            setDefaultDateText()
            setDefaultAlarmText()
            if (requireNotNull(reminderUpdateViewModel.isUpdate.value)) {
                KeyBoardUtil.hide(requireActivity())
                if (args.reminderId == "") {
                    requireContext().toastMessageUtil(getString(R.string.reminder_update_done_register))
                } else {
                    requireContext().toastMessageUtil(getString(R.string.reminder_update_done_edit))
                }
                popBackStack()
            } else {
                popBackStack()
            }
        }
    }

    private fun setDateTextClickListener() {
        binding.tvReminderUpdateDate.setOnClickListener {
            DatePickerDialog { date, networkDate ->
                reminderUpdateViewModel.setSelectedEventDate(date, networkDate)
                reminderUpdateViewModel.setIsUpdate(true)
            }.show(childFragmentManager, "reminder_date_picker")
        }
    }

    private fun setAlarmTextClickListener() {
        binding.tvReminderUpdateAlarm.setOnClickListener {
            ReminderAlarmPickerDialog { alarmTime ->
                reminderUpdateViewModel.setSelectedAlarmTime(alarmTime)
                reminderUpdateViewModel.setIsUpdate(true)
            }.show(childFragmentManager, "reminder_alarm_picker")
        }
    }

    private fun setAlarmSwitchClickListener() {
        binding.switchReminderUpdate.setOnClickListener {
            reminderUpdateViewModel.setIsUpdate(true)
        }
    }

    private fun setImportantCheckClickListener() {
        binding.checkBoxReminderUpdate.setOnClickListener {
            modifyImportantChecked()
        }
    }

    private fun setImportantTextClickListener() {
        binding.tvReminderUpdateImportant.setOnClickListener {
            with(binding.checkBoxReminderUpdate) { isChecked = !isChecked }
            modifyImportantChecked()
        }
    }

    private fun modifyImportantChecked() {
        reminderUpdateViewModel.setIsUpdate(true)
        if (binding.checkBoxReminderUpdate.isChecked) {
            binding.tvReminderUpdateImportant.setTextColor(
                getColor(requireContext(), R.color.black_343434)
            )
        } else {
            binding.tvReminderUpdateImportant.setTextColor(
                getColor(requireContext(), R.color.gray_a5a5a5)
            )
        }
    }

    private fun setReminderTitleObserve() {
        reminderUpdateViewModel.reminderTitleAtEditText.observe(viewLifecycleOwner) { reminderTitle ->
            if (reminderTitle.isEmpty()) {
                binding.tvReminderUpdateDone.setTextColor(
                    getColor(requireContext(), R.color.gray_cccccc)
                )
                binding.tvReminderUpdateDone.isClickable = false
            } else {
                binding.tvReminderUpdateDone.setTextColor(
                    getColor(requireContext(), R.color.green_15bd6f)
                )
                binding.tvReminderUpdateDone.isClickable = true
                if (args.reminderId == "") {
                    reminderUpdateViewModel.setIsUpdate(true)
                } else {
                    if (requireNotNull(reminderUpdateViewModel.detailReminderData.value).title != reminderTitle) {
                        reminderUpdateViewModel.setIsUpdate(true)
                    }
                }
            }
        }
    }
}
