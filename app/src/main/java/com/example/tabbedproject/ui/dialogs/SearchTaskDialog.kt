package com.example.tabbedproject.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.tabbedproject.R
import com.example.tabbedproject.databinding.SearchTaskDialogBinding
import com.example.tabbedproject.ui.SharedViewModel

class SearchTaskDialog : DialogFragment() {
    private var _binding: SearchTaskDialogBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = SearchTaskDialogBinding.inflate(layoutInflater)

        setDropDownItems()

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(
                binding.root
            ).setTitle("Search between done tasks:")
                .setPositiveButton("Search", DialogInterface.OnClickListener { dialog, id ->
                   // if (binding.searchET.text.toString().isNotBlank() && binding.autoCompleteTextView.text.toString().isNotBlank()){
                        sharedViewModel.searchCouple.value = Pair( binding.autoCompleteTextView.text.toString(),binding.searchET.text.toString())
                  //  } else Toast.makeText(requireContext(), "Please enter the required fields.", Toast.LENGTH_SHORT).show()
                    dismiss()
                }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                    getDialog()?.cancel()
                })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")


    }

    private fun setDropDownItems() {
        val items = listOf("title", "description", "time", "date")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.autoCompleteTextView.setAdapter(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}