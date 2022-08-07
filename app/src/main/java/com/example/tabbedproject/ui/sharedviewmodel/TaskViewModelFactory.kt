package com.example.tabbedproject.ui.sharedviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tabbedproject.repository.Repository
import java.lang.IllegalArgumentException


class TaskViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel(repository) as T
        } else throw IllegalArgumentException("Invalid ViewModel!!!")
    }
}