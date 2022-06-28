package com.keepin.android.util

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.fragment.app.DialogFragment
import com.keepin.android.R
import com.keepin.android.databinding.DialogUtilBinding
import kotlin.system.exitProcess

class UtilDialog(private val dialogMode: Int, private val doAfterConfirm: () -> Unit) :
    DialogFragment() {
    private var _binding: DialogUtilBinding? = null
    val binding get() = _binding ?: error(getString(R.string.binding_error))

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogUtilBinding.inflate(requireActivity().layoutInflater)
        setCancelBtnVisibility()
        setMessageMargins()
        setMessage()
        setUpdateRequiredView()
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
        val dialogWidth = if (dialogMode == UPDATE_REQUIRED) {
            (resources.displayMetrics.widthPixels * 0.88).toInt()
        } else {
            (resources.displayMetrics.widthPixels * 0.8).toInt()
        }

        requireNotNull(dialog).apply {
            requireNotNull(window).apply {
                setLayout(
                    dialogWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setBackgroundDrawableResource(R.drawable.border_white_fill_10)
            }
        }
    }

    private fun setCancelBtnVisibility() {
        binding.tvDialogBaseCancel.visibility = when (dialogMode) {
            AFTER_DELETE_AUTH, LOGIN_ERROR, EMAIL_ERROR, EMAIL_NOT_FOUND, EMAIL_NOT_SIGN, PASSWORD_CHANGE -> View.INVISIBLE
            else -> View.VISIBLE
        }
    }

    private fun setMessageMargins() {
        val dm = resources.displayMetrics
        val param = binding.tvDialogBaseMessage.layoutParams as ViewGroup.MarginLayoutParams
        when (dialogMode) {
            BEFORE_DELETE_AUTH ->
                param.setMargins(
                    14 * dm.density.toInt(),
                    0,
                    12 * dm.density.toInt(),
                    0
                )
            else ->
                param.setMargins(
                    28 * dm.density.toInt(),
                    0,
                    28 * dm.density.toInt(),
                    0
                )
        }
        binding.tvDialogBaseMessage.layoutParams = param
    }

    private fun setMessage() {
        binding.tvDialogBaseMessage.text = when (dialogMode) {
            STOP_WRITE -> getString(R.string.base_dialog_message_stop_write)
            AFTER_DELETE_AUTH -> getString(R.string.base_dialog_message_after_delete_auth)
            BEFORE_DELETE_AUTH -> getString(R.string.base_dialog_message_before_delete_auth)
            BEFORE_DELETE_POST -> getString(R.string.base_dialog_message_before_delete_post)
            BEFORE_DELETE_FRIEND -> getString(R.string.base_dialog_message_before_delete_friend)
            BEFORE_LOGOUT -> getString(R.string.base_dialog_message_before_logout)
            LOGIN_ERROR -> getString(R.string.base_dialog_message_login_error)
            EMAIL_ERROR -> getString(R.string.base_dialog_email_error)
            EMAIL_NOT_FOUND -> getString(R.string.base_dialog_email_not_found)
            EMAIL_NOT_SIGN -> getString(R.string.base_dialog_email_not_sign)
            PASSWORD_CHANGE -> getString(R.string.base_dialog_password_change)
            STOP_MODIFY -> getString(R.string.base_dialog_message_stop_modify)
            UPDATE_REQUIRED -> getString(R.string.base_dialog_update_required)
            else -> throw IllegalStateException()
        }
    }

    private fun setUpdateRequiredView() {
        if (dialogMode == UPDATE_REQUIRED) {
            with(binding) {
                tvDialogBaseConfirm.apply {
                    text = getString(R.string.base_dialog_update_confirm)
                    setPadding(5)
                }
                tvDialogBaseCancel.apply {
                    text = getString(R.string.base_dialog_update_cancel)
                    setPadding(23, 5, 23, 5)
                }
                viewDialogBaseDivider.visibility = View.VISIBLE
            }

            this.isCancelable = false
        }
    }

    private fun setConfirmTextClickListener() {
        binding.tvDialogBaseConfirm.setOnClickListener {
            doAfterConfirm()
            dismiss()
        }
    }

    private fun setCancelTextClickListener() {
        binding.tvDialogBaseCancel.setOnClickListener {
            when (dialogMode) {
                UPDATE_REQUIRED -> {
                    requireActivity().finishAffinity()
                    exitProcess(0)
                }
                else -> dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val STOP_WRITE = 0
        const val AFTER_DELETE_AUTH = 1
        const val BEFORE_DELETE_AUTH = 2
        const val BEFORE_DELETE_POST = 3
        const val BEFORE_DELETE_FRIEND = 4
        const val BEFORE_LOGOUT = 5
        const val LOGIN_ERROR = 6
        const val EMAIL_ERROR = 7
        const val EMAIL_NOT_FOUND = 8
        const val EMAIL_NOT_SIGN = 9
        const val PASSWORD_CHANGE = 10
        const val STOP_MODIFY = 11
        const val UPDATE_REQUIRED = 12
    }
}
