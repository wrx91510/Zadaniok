package com.example.zadaniok.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zadaniok.viewmodel.TaskViewModel
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight








@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    onAddClick: () -> Unit,
    onStatsClick: () -> Unit,
    vm: TaskViewModel = viewModel()
) {
    val tasks by vm.tasks.collectAsState()
    val sortedTasks = tasks.sortedBy { it.isDone }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Zadaniok • Lista zadań",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },

                actions = { TextButton(onClick = onStatsClick) { Text("Statystyki") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Filled.Add, contentDescription = "Dodaj zadanie")
            }

        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {
            Text("Liczba zadań: ${tasks.size}", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            if (tasks.isEmpty()) {
                Text("Brak zadań. Kliknij + aby dodać.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sortedTasks, key = { it.id }) { task ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        textDecoration = if (task.isDone)
                                            TextDecoration.LineThrough
                                        else
                                            TextDecoration.None
                                    )

                                    IconButton(onClick = { vm.deleteTask(task.id) }) {
                                        Icon(Icons.Filled.Delete, contentDescription = "Usuń")
                                    }
                                }

                                if (task.description.isNotBlank()) {
                                    Text(task.description)
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    val chipColor by animateColorAsState(
                                        targetValue = if (task.isDone) Color(0xFFC8E6C9) else Color(0xFFFFD8A8),
                                        label = "chipColor"
                                    )

                                    AssistChip(
                                        onClick = { vm.toggleDone(task.id) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = chipColor,
                                            labelColor = Color(0xFF1B1B1B)
                                        ),
                                        label = { Text(if (task.isDone) "Zrobione" else "Do zrobienia") }
                                    )



                                    Checkbox(
                                        checked = task.isDone,
                                        onCheckedChange = { vm.toggleDone(task.id) }
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}
