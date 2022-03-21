package com.example.tabbedproject

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.example.tabbedproject.data.Task
import com.example.tabbedproject.databinding.AddTaskDialogBinding
import com.example.tabbedproject.databinding.FragmentDoneBinding
import java.lang.Exception
import java.util.*

class AddTaskDialog(private val state: String) : DialogFragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var _binding: AddTaskDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var uri: Uri
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>
    private lateinit var taskList: List<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            uri = it
            Log.d("ali", "onCreateDialog: " + uri.toString())
            binding.image.setImageURI(uri)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = AddTaskDialogBinding.inflate(layoutInflater)
        sharedViewModel.getUserTasks(sharedViewModel.username)
        initSetListeners()
        initSetObservers()
        sharedViewModel.taskList.observe(this) {
            Log.d("ali", "initSetObservers: ${it.toString()}")
            taskList = it}
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(
                binding.root
            ).setTitle("Add a task:")
                .setPositiveButton("Save", DialogInterface.OnClickListener { dialog, id ->
                    if (!::uri.isInitialized) uri = Uri.EMPTY
                    val taskId = taskList.size.toString()
                    val task = Task(
                        id = taskId,
                        title = binding.titleEt.text.toString(),
                        description = binding.descriptionEt.text.toString(),
                        date = binding.datePicker.text.toString(),
                        time = binding.timePicker.text.toString(),
                        imageAddress = uri.toString(),
                        state = state,
                        user_username = sharedViewModel.username
                    )
                    sharedViewModel.insertTask(task)

                    dismiss()
                }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                    getDialog()?.cancel()
                })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    /* override fun onAttach(context: Context) {
         super.onAttach(context)
         try {
             listener = requireContext() as AddTaskListener
         } catch (e: Exception) {
             e.printStackTrace()
         }
     }

     interface AddTaskListener {
         fun getTaskDetails(title: String, description: String)
     }
 */
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
    }

    private fun initSetObservers() {
        /*   sharedViewModel.username.observe(viewLifecycleOwner) {
               username = it
           }
   */

    }

    private fun datePick() {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            binding.datePicker.text = "${year.toString()}/${(month + 1).toString()}/${day.toString()}"
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