package com.keepin.android.util

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.keepin.android.R
import com.keepin.android.databinding.DialogNoticeBinding
import timber.log.Timber
import java.lang.IllegalStateException

class NoticeDialog(
    private val doAfterConfirm: (isChecked: Boolean?) -> Unit
) : DialogFragment() {
    private var _binding: DialogNoticeBinding? = null
    val binding get() = _binding ?: error(getString(R.string.binding_error))

    private lateinit var database: DatabaseReference

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogNoticeBinding.inflate(requireActivity().layoutInflater)
        database = Firebase.database.reference
        setConfirmTextClickListener()
        readNoticeFromFirebase()

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
                    (resources.displayMetrics.widthPixels * 0.88).toInt(),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setBackgroundDrawableResource(R.drawable.border_gray_f3f5f8_fill_10)
            }
        }
    }

    private fun readNoticeFromFirebase() {
        database.child(FIREBASE_NOTICE).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val showCheckBox = snapshot.child(FIREBASE_NOTICE_CHECK).value

                    if (showCheckBox is Boolean) {
                        initCheckBox(showCheckBox)
                    }

                    initNoticeData(
                        snapshot.getSnapshotStringValue(FIREBASE_NOTICE_TITLE),
                        snapshot.getSnapshotStringValue(FIREBASE_NOTICE_CONTENT1),
                        snapshot.getSnapshotStringValue(FIREBASE_NOTICE_CONTENT2),
                        snapshot.getSnapshotStringValue(FIREBASE_NOTICE_CONTENT3),
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.d(error.toString())
                }
            }
        )
    }

    private fun initNoticeData(
        title: String,
        content1: String,
        content2: String,
        content3: String
    ) {
        with(binding) {
            tvNoticeDialogTitle.text = title.setLinedString()
            tvNoticeDialogContent1.initSetText(content1)
            tvNoticeDialogContent2.initSetText(content2)
            tvNoticeDialogContent3.initSetText(content3)
        }
    }

    private fun initCheckBox(visibility: Boolean) {
        if (visibility) {
            binding.cbNoticeDialogShow.visibility = View.VISIBLE
        } else {
            binding.cbNoticeDialogShow.visibility = View.GONE
            binding.tvNoticeDialogConfirm.textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
    }

    private fun TextView.initSetText(string: String) {
        if (string == "") {
            this.visibility = View.GONE
        } else {
            this.text = string.setLinedString()
        }
    }

    private fun DataSnapshot.getSnapshotStringValue(path: String): String {
        return this.child(path).value.toString()
    }

    private fun String.setLinedString(): String {
        return this.replace("\\n", System.getProperty("line.separator")!!)
    }

    private fun setConfirmTextClickListener() {
        binding.tvNoticeDialogConfirm.setOnClickListener {
            doAfterConfirm(binding.cbNoticeDialogShow.isChecked)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val FIREBASE_NOTICE = "notice"
        const val FIREBASE_NOTICE_TITLE = "title"
        const val FIREBASE_NOTICE_CONTENT1 = "content1"
        const val FIREBASE_NOTICE_CONTENT2 = "content2"
        const val FIREBASE_NOTICE_CONTENT3 = "content3"
        const val FIREBASE_NOTICE_CHECK = "showCheck"
        const val FIREBASE_NOTICE_SHOW = "showNotice"
    }
}
