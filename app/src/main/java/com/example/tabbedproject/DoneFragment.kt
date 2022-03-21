package com.example.tabbedproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabbedproject.data.Task
import com.example.tabbedproject.databinding.FragmentDoneBinding

class DoneFragment : Fragment() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close) }
    private val toButtom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom) }
    private val fromButtom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom) }
    private val sharedViewModel: SharedViewModel by activityViewModels { TaskViewModelFactory((requireActivity().application as TaskApplication).repository) }
    private var doneTasks: List<Task> = emptyList()
    private var clicked = false
    private lateinit var myAdapter: TaskAdapter
    private var _binding: FragmentDoneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoneBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.getUserTasks(sharedViewModel.username)
        initClickListeners()

        myAdapter = TaskAdapter(doneTasks)

        sharedViewModel.taskList.observe(viewLifecycleOwner) {
            binding.group.isGone = true
            doneTasks = it.filter {
                it.state == "done"
            }
            Log.d("ali", "onSearh: " + sharedViewModel.searchCouple.toString())

            if (sharedViewModel.searchCouple.second.isNotBlank()) {
                applySearch()
                Log.d("ali", "onSearh: ")
            }

            myAdapter = TaskAdapter(doneTasks)
            myAdapter.onViewClicked(object : TaskAdapter.ItemClick {
                override fun onClick(task: Task) {
                    val dialog = EditTaskDialog(task)
                    dialog.show(childFragmentManager, "Add task dialog")
                }
            })
            setRecyclerAdapter(myAdapter)


        }
    }

    private fun initClickListeners() {
        binding.openFloatingButton.setOnClickListener {

            if (!clicked) {
                openFabs()

            } else {
                closeFabs()

            }
        }
        binding.addFloatingButton.setOnClickListener {
            val dialog = AddTaskDialog("Done")
            dialog.show(childFragmentManager, "Add task dialog")
        }
        binding.searchFloatingButton.setOnClickListener {
            val dialog = SearchTaskDialog()
            dialog.show(childFragmentManager, "Search task dialog")
        }
    }

    private fun openFabs() {
        binding.addFloatingButton.apply {
            isVisible = true
            isClickable = true
            startAnimation(fromButtom)
        }

        binding.searchFloatingButton.apply {
            isVisible = true
            isClickable = true
            startAnimation(fromButtom)
        }

        binding.openFloatingButton.startAnimation(rotateOpen)
        clicked = !clicked
    }

    private fun closeFabs() {
        binding.addFloatingButton.apply {
            isGone = true
            isClickable = false
            startAnimation(toButtom)

        }
        binding.searchFloatingButton.apply {
            isGone = true
            isClickable = false
            startAnimation(toButtom)
        }
        binding.openFloatingButton.startAnimation(rotateClose)

        clicked = !clicked
    }

    private fun setRecyclerAdapter(myAdapter: TaskAdapter) {
        binding.recyclerView.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(context)
            myAdapter.notifyDataSetChanged()
        }
    }
private fun applySearch(){

    val searchCouple = sharedViewModel.searchCouple
    val searchedAttribute = searchCouple.first
    val query = searchCouple.second
    when (searchedAttribute) {
        "title" -> doneTasks.filter { it.title.contains(query) }
        "description" -> doneTasks.filter { it.description.contains(query) }
        "date" -> doneTasks.filter { it.date.contains(query) }
        "time" -> doneTasks.filter { it.time.contains(query) }
    }
    myAdapter.notifyDataSetChanged()
    sharedViewModel.searchCouple = Pair("","")
}

    override fun onResume() {
        super.onResume()
        Log.d("ali", "onSearh resume: " + sharedViewModel.searchCouple.toString())

        applySearch()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}