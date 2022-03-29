package com.example.tabbedproject.ui.dialogs

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.tabbedproject.ui.SharedViewModel


class DeleteAllDialog : DialogFragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?) =

        AlertDialog.Builder(requireContext()).setTitle("Delete all tasks?").setMessage("Are you sure you want to delete all of your tasks?")
            .setPositiveButton("Yes") { _, _ ->
                sharedViewModel.deleteAll()
            }.setNegativeButton("No") { _, _ ->
                dismiss()
            }.create()

}