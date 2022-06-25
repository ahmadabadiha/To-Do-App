package com.example.tabbedproject.ui.sharedviewmodel

import androidx.lifecycle.*
import com.example.tabbedproject.data.Task
import com.example.tabbedproject.data.User
import com.example.tabbedproject.ui.repository.Repository
import kotlinx.coroutines.launch

class SharedViewModel(private val repository: Repository) : ViewModel() {
    private var _isUserSet = MutableLiveData<Boolean>()
    val isUserSet: LiveData<Boolean> get() = _isUserSet
    var searchCouple = MutableLiveData<Pair<String,String>>()
    var username = ""

    lateinit var taskList: LiveData<List<Task>>
    lateinit var userList: List<User>

    fun checkUserSet(user: User) {
        if (userList.contains(user)) {
            username = user.username
            _isUserSet.value = true
        } else {
            _isUserSet.value = false
        }
    }

    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }

    fun getUsers() = viewModelScope.launch {
        userList = repository.getUsers()
    }

    fun insertTask(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
    }

    fun getUserTasks(username: String) = viewModelScope.launch {
        taskList = repository.getUserTasks(username)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

