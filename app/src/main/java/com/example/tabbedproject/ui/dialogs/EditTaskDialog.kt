package com.example.tabbedproject.ui.dialogs

import android.app.*
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ShareCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.tabbedproject.R
import com.example.tabbedproject.data.Task
import com.example.tabbedproject.databinding.EditTaskDialogBinding
import com.example.tabbedproject.ui.sharedviewmodel.SharedViewModel
import java.util.*

class EditTaskDialog(private val task: Task) : DialogFragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var _binding: EditTaskDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>
    private lateinit var uri: Uri
    private lateinit var taskList: List<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            uri = it ?: Uri.parse(task.imageAddress)
            binding.image.setImageURI(uri)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = EditTaskDialogBinding.inflate(layoutInflater)

        setViews()
        initSetListeners()
        sharedViewModel.taskList.observe(this) {
            taskList = it
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(
                binding.root
            ).setTitle("Edit your task:")
                .setPositiveButton("Save") { _, _ ->
                    val imageAddress = if (!::uri.isInitialized) task.imageAddress else uri.toString()
                    val date = if (binding.datePicker.text.toString() != "Select a date") binding.datePicker.text.toString() else ""
                    val time = if (binding.timePicker.text.toString() != "Select a time") binding.timePicker.text.toString() else ""
                    val taskId = task.id
                    val task = Task(
                        id = taskId,
                        title = binding.titleEt.text.toString(),
                        description = binding.descriptionEt.text.toString(),
                        date = date,
                        time = time,
                        imageAddress = imageAddress,
                        state = binding.autoCompleteTextView.text.toString(),
                        user_username = sharedViewModel.username
                    )
                    sharedViewModel.updateTask(task)
                    dismiss()
                }.setNegativeButton("Cancel") { _, _ ->
                    dialog?.cancel()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setViews() {
        val parsedUri = Uri.parse(task.imageAddress)
        if (parsedUri == Uri.EMPTY) binding.image.setImageResource(R.drawable.ic_menu_gallery) else binding.image.setImageURI(parsedUri)
        binding.titleEt.setText(task.title)
        binding.descriptionEt.setText(task.description)
        binding.datePicker.text = task.date.ifBlank { "Select a date" }
        binding.timePicker.text = task.time.ifBlank { "Select a time" }
        val items = listOf("To Do", "Doing", "Done")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.autoCompleteTextView.setAdapter(adapter)
        binding.autoCompleteTextView.setText(task.state, false)
    }

    private fun initSetListeners() {
        binding.image.setOnClickListener {
            activityResultLauncher.launch("image/*")
        }
        binding.datePicker.setOnClickListener {
            datePick()
        }

        binding.timePicker.setOnClickListener {
            timePick()
        }

        binding.shareButton.setOnClickListener {
            val shareText = "title: ${task.title}, description: ${task.description}, time: ${task.date} ${task.time}, status: ${task.state}"
            val x = ShareCompat.IntentBuilder(requireContext()).setText(shareText).setType("text/plain").intent
            val title = resources.getString(R.string.share_title)
            val chooser = Intent.createChooser(x, title)
            startActivity(chooser)
        }

        binding.deleteButton.setOnClickListener {
            sharedViewModel.deleteTask(task)
            dismiss()
        }

    }


    private fun datePick() {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            binding.datePicker.text = "${year}/${(month + 1)}/${day}"
        }
        val calender = Calendar.getInstance()
        val day = calender.get(Calendar.DAY_OF_MONTH)
        val month = calender.get(Calendar.MONTH)
        val year = calender.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(requireContext(), listener, year, month, day)
        datePickerDialog.setTitle("Select date of your awesome task:")
        datePickerDialog.show()
    }

    private fun timePick() {
        var h = 0
        var m = 0
        val listener = TimePickerDialog.OnTimeSetListener { _, hour, min ->
            h = hour
            m = min
            binding.timePicker.text = "$h:$m"

        }
        val timePickerDialog = TimePickerDialog(requireContext(), listener, h, m, true)
        timePickerDialog.setTitle("Select time of your awesome task:")
        timePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}