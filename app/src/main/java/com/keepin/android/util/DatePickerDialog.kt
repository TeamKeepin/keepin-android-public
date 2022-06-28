package com.keepin.android.util

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.keepin.android.R
import com.keepin.android.databinding.DialogDatePickerBinding
import java.lang.IllegalStateException

class DatePickerDialog(private val doAfterConfirm: (date: String, networkDate: String) -> Unit) :
    DialogFragment() {
    private var _binding: DialogDatePickerBinding? = null
    val binding get() = _binding ?: error(getString(R.string.binding_error))

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogDatePickerBinding.inflate(requireActivity().layoutInflater)
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

    private fun sendSelectedDate() {
        with(binding.datePickerDialogDatePicker) {
            val yearFormat = getYearFormat(year)
            val monthFormat = getMonthFormat(month)
            val dayFormat = getDayFormat(dayOfMonth)
            val date = "$yearFormat.$monthFormat.$dayFormat"
            val networkDate = "$yearFormat-$monthFormat-$dayFormat"
            doAfterConfirm(date, networkDate)
        }
        dismiss()
    }

    private fun getYearFormat(year: Int): String = year.toString()

    private fun getMonthFormat(month: Int): String {
        return if (month < 9) {
            "0" + (month + 1).toString()
        } else {
            (month + 1).toString()
        }
    }

    private fun getDayFormat(day: Int): String {
        return if (day < 10) {
            "0$day"
        } else {
            day.toString()
        }
    }

    private fun setConfirmTextClickListener() {
        binding.tvDialogDatePickerConfirm.setOnClickListener {
            sendSelectedDate()
        }
    }

    private fun setCancelTextClickListener() {
        binding.tvDialogDatePickerCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
