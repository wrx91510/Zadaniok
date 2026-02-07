package com.example.zadaniok.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "zadaniok_prefs")

object TaskDataStoreKeys {
    val TASKS_JSON = stringPreferencesKey("tasks_json")
}

class TaskDataStore(private val context: Context) {

    val tasksJsonFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[TaskDataStoreKeys.TASKS_JSON] ?: "[]"
    }

    suspend fun saveTasksJson(json: String) {
        context.dataStore.edit { prefs ->
            prefs[TaskDataStoreKeys.TASKS_JSON] = json
        }
    }
}
