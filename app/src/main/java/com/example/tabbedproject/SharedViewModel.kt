package com.example.tabbedproject

import android.util.Log
import androidx.lifecycle.*
import com.example.tabbedproject.data.Task
import com.example.tabbedproject.data.User
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class SharedViewModel(private val repository: Repository) : ViewModel() {
    private var _isUserSet = MutableLiveData<Boolean>()
    val isUserSet: LiveData<Boolean> get() = _isUserSet
    var searchCouple = Pair("", "")
    /*private var _username =  MutableLiveData<String>()
    val username :LiveData<String> = _username*/
    var username = ""

    //var taskList = MutableLiveData<List<Task>>()
    lateinit var taskList: LiveData<List<Task>>
    private lateinit var userList: List<User>

    fun checkUserSet(user: User) {
        if (userList.contains(user)) {
            //_username.postValue(user.username)
            username = user.username
            _isUserSet.value = true
        } else {
            // _username.postValue("")
            _isUserSet.value = false
        }
    }

    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }

    fun getUsers() = viewModelScope.launch {
        userList = repository.getUsers()
        Log.d("ali", "getUsers: " + userList[0].toString())
    }

    fun insertTask(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        Log.d("ali", "updateTask: " + task.toString())
        repository.updateTask(task)
    }

    fun getUserTasks(username: String) = viewModelScope.launch {
        // taskList.postValue(repository.getUserTasks(username).value)
        taskList = repository.getUserTasks(username)
    }
}

class TaskViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel(repository) as T
        } else throw IllegalArgumentException("Invalid ViewModel!!!")
    }
}