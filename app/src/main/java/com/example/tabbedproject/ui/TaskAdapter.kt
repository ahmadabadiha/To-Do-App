package com.example.tabbedproject.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tabbedproject.R
import com.example.tabbedproject.data.Task
import com.example.tabbedproject.databinding.TaskLayoutBinding
import java.util.*

class TaskAdapter(private val doneTasks: List<Task>?) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private lateinit var itemClick: ItemClick

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val binding = TaskLayoutBinding.bind(itemView)
        private lateinit var task: Task

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            itemClick.onClick(task)
        }

        fun fill(position: Int) {
            task = doneTasks!![position]
            val title = task.title
            binding.title.text = title
            binding.time.text = "${task.time} ${task.date}"
            binding.icon.letter = title[0].toString()
            binding.icon.shapeColor = Color.rgb(Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.fill(position)
    }

    override fun getItemCount(): Int {
        return doneTasks!!.size
    }

    interface ItemClick {
        fun onClick(task: Task)
    }

    fun onViewClicked(itemClick: ItemClick) {
        this.itemClick = itemClick
    }

}
