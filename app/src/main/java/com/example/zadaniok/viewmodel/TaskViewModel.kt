package com.example.zadaniok.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zadaniok.data.datastore.TaskDataStore
import com.example.zadaniok.data.repo.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = TaskRepository(
        TaskDataStore(app.applicationContext)
    )

    val tasks: StateFlow<List<com.example.zadaniok.data.model.Task>> =
        repo.tasksFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            repo.addTask(title, description)
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            repo.deleteTask(id)
        }
    }

    fun toggleDone(id: Long) {
        viewModelScope.launch {
            repo.toggleDone(id)
        }
    }
}
