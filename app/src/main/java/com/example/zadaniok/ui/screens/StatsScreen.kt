package com.example.zadaniok.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zadaniok.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    onBack: () -> Unit,
    vm: TaskViewModel = viewModel()
) {
    val tasks by vm.tasks.collectAsState()

    val done = tasks.count { it.isDone }
    val todo = tasks.size - done

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statystyki") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Wróć")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Wszystkie zadania: ${tasks.size}", style = MaterialTheme.typography.titleLarge)
            Text("Zrobione: $done", style = MaterialTheme.typography.titleMedium)
            Text("Do zrobienia: $todo", style = MaterialTheme.typography.titleMedium)
        }
    }
}
