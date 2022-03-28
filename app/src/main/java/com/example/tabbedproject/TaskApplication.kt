package com.example.tabbedproject

import android.app.Application
import com.example.tabbedproject.data.TaskDatabase
import com.example.tabbedproject.ui.Repository

class TaskApplication : Application() {
    private val database by lazy { TaskDatabase.getDatabase(this) }
    val repository by lazy { Repository(database.taskDao(), database.userDao()) }
}