package com.example.tabbedproject

import android.os.Bundle
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
import com.example.tabbedproject.databinding.FragmentDoingBinding

class DoingFragment : Fragment() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close) }
    private val toButtom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom) }
    private val fromButtom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom) }
    private val sharedViewModel: SharedViewModel by activityViewModels { TaskViewModelFactory((requireActivity().application as TaskApplication).repository) }
    private var doingTasks: List<Task> = emptyList()
    private var clicked = false
    private lateinit var myAdapter: TaskAdapter
    private var _binding: FragmentDoingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.getUserTasks(sharedViewModel.username)
        initClickListeners()
        myAdapter = TaskAdapter(doingTasks)

        sharedViewModel.taskList.observe(viewLifecycleOwner) {
            binding.noTaskView.isVisible = it.isEmpty()

            doingTasks = it.filter {
                it.state == "Doing"
            }

            myAdapter = TaskAdapter(doingTasks)
            setRecyclerAdapter(myAdapter)


        }

        sharedViewModel.searchCouple.observe(viewLifecycleOwner) {
            applySearch()
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
            val dialog = AddTaskDialog("Doing")
            dialog.show(childFragmentManager, "Add task dialog")
        }
        binding.searchFloatingButton.setOnClickListener {
            val dialog = SearchTaskDialog()
            dialog.show(childFragmentManager, "Search task dialog")
        }

        binding.deleteAllFloatingButton.setOnClickListener {
            DeleteAllDialog().show(childFragmentManager, "Delete all dialog")

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
        binding.deleteAllFloatingButton.apply {
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

        binding.deleteAllFloatingButton.apply {
            isGone = true
            isClickable = false
            startAnimation(toButtom)
        }
        binding.openFloatingButton.startAnimation(rotateClose)

        clicked = !clicked
    }

    private fun setRecyclerAdapter(myAdapter: TaskAdapter) {
        myAdapter.onViewClicked(object : TaskAdapter.ItemClick {
            override fun onClick(task: Task) {
                val dialog = EditTaskDialog(task)
                dialog.show(childFragmentManager, "Edit task dialog")
            }
        })
        binding.recyclerView.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(context)
            myAdapter.notifyDataSetChanged()
        }
    }

    private fun applySearch() {

        val searchCouple = sharedViewModel.searchCouple.value!!
        val searchedAttribute = searchCouple.first
        val query = searchCouple.second
        if (searchedAttribute.isBlank() || query.isBlank()) setRecyclerAdapter(myAdapter)
        else {
            var searchedTask = listOf<Task>()
            when (searchedAttribute) {
                "title" -> searchedTask = doingTasks.filter { it.title.contains(query, true) }
                "description" -> searchedTask = doingTasks.filter { it.description.contains(query, true) }
                "date" -> searchedTask = doingTasks.filter { it.date.contains(query, true) }
                "time" -> searchedTask = doingTasks.filter { it.time.contains(query, true) }
            }
            setRecyclerAdapter(TaskAdapter(searchedTask))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}