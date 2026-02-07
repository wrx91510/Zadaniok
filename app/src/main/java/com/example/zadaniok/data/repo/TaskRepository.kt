package com.example.zadaniok.data.repo

import com.example.zadaniok.data.datastore.TaskDataStore
import com.example.zadaniok.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class TaskRepository(
    private val store: TaskDataStore
) {
    private val json = Json { ignoreUnknownKeys = true }

    val tasksFlow: Flow<List<Task>> = store.tasksJsonFlow.map { raw ->
        runCatching { json.decodeFromString<List<Task>>(raw) }.getOrElse { emptyList() }
    }

    suspend fun addTask(title: String, description: String, dueAt: Long?) {
        val current = tasksFlow.first()
        val newTask = Task(
            id = System.currentTimeMillis(),
            title = title.trim(),
            description = description.trim(),
            dueAt = dueAt
        )

        save(current + newTask)
    }

    suspend fun deleteTask(id: Long) {
        val current = tasksFlow.first()
        save(current.filterNot { it.id == id })
    }

    suspend fun toggleDone(id: Long) {
        val current = tasksFlow.first()
        save(current.map { if (it.id == id) it.copy(isDone = !it.isDone) else it })
    }

    private suspend fun save(list: List<Task>) {
        store.saveTasksJson(json.encodeToString(list))
    }
}
