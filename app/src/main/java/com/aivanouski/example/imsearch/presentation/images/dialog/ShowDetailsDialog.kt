package com.aivanouski.example.imsearch.presentation.images.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.aivanouski.example.imsearch.R

class ShowDetailsDialog : DialogFragment(), DialogInterface.OnClickListener {

    private lateinit var onConfirmed: () -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cancel dialog after state restoration,
        // as reference to onConfirm will be changed
        if (savedInstanceState != null) {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.details_dialog_message)
            .setPositiveButton(R.string.yes, this)
            .setNegativeButton(R.string.no, null)
            .create()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            onConfirmed.invoke()
        }
    }

    companion object {

        val TAG: String = ShowDetailsDialog::class.java.simpleName

        fun newInstance(
            onConfirm: () -> Unit
        ): ShowDetailsDialog {
            return ShowDetailsDialog()
                .apply {
                    this.onConfirmed = onConfirm
                }
        }
    }
}