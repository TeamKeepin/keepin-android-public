package com.keepin.android.presentation.reminderupdate

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.keepin.android.R
import com.keepin.android.databinding.DialogReminderAlarmPickerBinding
import java.lang.IllegalStateException

class ReminderAlarmPickerDialog(
    private val setSelectedAlarmTime: (alarmTime: String) -> Unit
) : DialogFragment() {
    private var _binding: DialogReminderAlarmPickerBinding? = null
    val binding get() = _binding ?: error(getString(R.string.binding_error))

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogReminderAlarmPickerBinding.inflate(requireActivity().layoutInflater)
        setConfirmTextClickListener()
        setCancelTextClickListener()
        return activity?.let {
            val dialog = AlertDialog.Builder(it).create()
            dialog.setView(binding.root)
            dialog
        } ?: throw IllegalStateException()
    }

    override fun onStart() {
        super.onStart()
        setLayout()
        setValueAtAlarmPicker()
    }

    private fun setLayout() {
        requireNotNull(dialog).apply {
            requireNotNull(window).apply {
                setLayout(
                    (resources.displayMetrics.widthPixels * 0.8).toInt(),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setBackgroundDrawableResource(R.drawable.border_gray_f3f5f8_fill_10)
            }
        }
    }

    private fun setValueAtAlarmPicker() {
        binding.numberPickerDialogAlarmPicker.apply {
            wrapSelectorWheel = false
            minValue = 0
            maxValue = 4
            displayedValues =
                arrayOf(
                    context.getString(R.string.alarm_picker_today),
                    context.getString(R.string.alarm_picker_1_day_before),
                    context.getString(R.string.alarm_picker_2_day_before),
                    context.getString(R.string.alarm_picker_3_day_before),
                    context.getString(R.string.alarm_picker_1_week_before),
                )
        }
    }

    private fun setConfirmTextClickListener() {
        binding.tvDialogAlarmPickerConfirm.setOnClickListener {
            setSelectedAlarmTime(
                when (binding.numberPickerDialogAlarmPicker.value) {
                    0 -> getString(R.string.alarm_picker_selected_today)
                    1 -> getString(R.string.alarm_picker_selected_1_day_before)
                    2 -> getString(R.string.alarm_picker_selected_2_day_before)
                    3 -> getString(R.string.alarm_picker_selected_3_day_before)
                    4 -> getString(R.string.alarm_picker_selected_1_week_before)
                    else -> throw IllegalAccessException()
                }
            )
            dismiss()
        }
    }

    private fun setCancelTextClickListener() {
        binding.tvDialogAlarmPickerCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
