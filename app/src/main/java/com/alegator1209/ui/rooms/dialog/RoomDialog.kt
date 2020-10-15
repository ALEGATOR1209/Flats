package com.alegator1209.ui.rooms.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.alegator1209.R

class RoomDialog : DialogFragment() {
    private lateinit var onDone: (name: String) -> Unit
    private lateinit var roomName: EditText

    fun setOnDone(f: (name: String) -> Unit) { onDone = f }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        onViewStateRestored(savedInstanceState)
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater

        val dialog = inflater?.inflate(R.layout.dialog_room, null)
        roomName = dialog?.findViewById(R.id.roomName) ?: error("NPE")

        builder
            .setView(dialog)
            .setPositiveButton(getString(R.string.add_room)) { _, _ -> onDone(
                roomName.text.toString()
            )}
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }

        return builder.create()
    }
}