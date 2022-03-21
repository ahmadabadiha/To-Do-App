package com.example.tabbedproject

import androidx.lifecycle.LiveData
import com.example.tabbedproject.data.Task
import com.example.tabbedproject.data.TaskDao
import com.example.tabbedproject.data.User
import com.example.tabbedproject.data.UserDao
import kotlin.concurrent.thread

class Repository(private val taskDao: TaskDao, private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)

    }

    suspend fun getUsers(): List<User> {
        return userDao.getUsers()

    }

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    fun getUserTasks(username: String): LiveData<List<Task>> {
        return taskDao.getUserTasks(username)
    }

    /*fun checkUserLogin(user: User): Boolean {
        if (getUsers().contains(user)) return true
        return false
    }*/
}