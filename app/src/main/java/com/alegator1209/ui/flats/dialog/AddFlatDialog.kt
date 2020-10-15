package com.alegator1209.ui.flats.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.alegator1209.R

class AddFlatDialog : DialogFragment() {
    private lateinit var onCreateFlat: (name: String, address: String) -> Unit
    private lateinit var flatName: EditText
    private lateinit var flatAddress: EditText

    fun setOnCreateFlat(f: (name: String, address: String) -> Unit) { onCreateFlat = f }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        onViewStateRestored(savedInstanceState)
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater

        val dialog = inflater?.inflate(R.layout.dialog_add_flat, null)
        flatName = dialog?.findViewById(R.id.flatName) ?: error("NPE")
        flatAddress = dialog.findViewById(R.id.flatAddress) ?: error("NPE")

        builder
            .setView(dialog)
            .setPositiveButton(getString(R.string.add_flat)) { _, _ -> onCreateFlat(
                flatName.text.toString(),
                flatAddress.text.toString()
            )}
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }

        return builder.create()
    }
}