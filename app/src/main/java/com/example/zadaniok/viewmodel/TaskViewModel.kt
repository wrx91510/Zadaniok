package com.example.zadaniok.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.zadaniok.data.datastore.TaskDataStore
import com.example.zadaniok.data.repo.TaskRepository
import com.example.zadaniok.network.RetrofitClient
import com.example.zadaniok.notifications.ReminderWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


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

    // --- CYTAT (API) ---
    private val _quote = MutableStateFlow<String?>(null)
    val quote: StateFlow<String?> = _quote

    fun loadQuote() {
        viewModelScope.launch {
            try {
                val list = RetrofitClient.api.getRandomQuote()
                val q = list.firstOrNull()
                _quote.value = if (q != null) "„${q.q}” — ${q.a}" else "Brak cytatu"
            } catch (e: Exception) {
                android.util.Log.e("QUOTE", "loadQuote failed", e)
                _quote.value =
                    "Błąd: ${e.javaClass.simpleName}${if (e.message != null) " - ${e.message}" else ""}"
            }
        }
    }


    // --- TASKI ---
    fun addTask(title: String, description: String, dueAt: Long?) {
        viewModelScope.launch {
            repo.addTask(title, description, dueAt)
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

    // --- PRZYPOMNIENIA NA TERMIN ---
    fun scheduleReminderAt(dueAt: Long, title: String, description: String) {
        val delayMs = dueAt - System.currentTimeMillis()
        if (delayMs <= 0) return

        val data = workDataOf(
            "title" to title,
            "desc" to description.ifBlank { "Masz zadanie do wykonania" }
        )

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(data)
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(getApplication()).enqueue(request)
    }
}
