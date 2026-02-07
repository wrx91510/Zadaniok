package com.example.zadaniok.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zadaniok.viewmodel.TaskViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    onAddClick: () -> Unit,
    onStatsClick: () -> Unit,
    vm: TaskViewModel = viewModel()
) {
    val tasks by vm.tasks.collectAsState()
    val sortedTasks = tasks.sortedBy { it.isDone }

    val quote by vm.quote.collectAsState()
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    // BONUS: odświeżanie "now" co minutę (tylko do statusu "PO TERMINIE")
    var now by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000)
            now = System.currentTimeMillis()
        }
    }

    // Cytat ładujemy 1 raz po wejściu + można odświeżyć przyciskiem
    LaunchedEffect(Unit) {
        vm.loadQuote()
    }

    // Dialog usuwania
    var showDeleteDialog by remember { mutableStateOf(false) }
    var taskToDeleteId by remember { mutableStateOf<Long?>(null) }
    var taskToDeleteTitle by remember { mutableStateOf("") }

    // SNACKBAR
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
            FloatingActionButton(
                onClick = onAddClick,
                modifier = Modifier.padding(bottom = 130.dp)
            ) {
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

            // WAŻNE: niezależnie czy lista pusta czy nie, dajemy sekcję z weight(1f)
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Text("Brak zadań. Kliknij + aby dodać.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sortedTasks, key = { it.id }) { task ->
                        val isOverdue = task.dueAt != null && task.dueAt!! < now && !task.isDone

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

                                    IconButton(onClick = {
                                        taskToDeleteId = task.id
                                        taskToDeleteTitle = task.title
                                        showDeleteDialog = true
                                    }) {
                                        Icon(Icons.Filled.Delete, contentDescription = "Usuń")
                                    }
                                }

                                if (task.description.isNotBlank()) {
                                    Text(task.description)
                                }

                                if (task.dueAt != null) {
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Termin: ${dateFormat.format(task.dueAt!!)}",
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        if (isOverdue) {
                                            AssistChip(
                                                onClick = { },
                                                leadingIcon = {
                                                    Icon(
                                                        imageVector = Icons.Filled.Warning,
                                                        contentDescription = "Po terminie",
                                                        tint = Color.White
                                                    )
                                                },
                                                colors = AssistChipDefaults.assistChipColors(
                                                    containerColor = Color(0xFFD32F2F),
                                                    labelColor = Color.White
                                                ),
                                                label = { Text("PO TERMINIE") }
                                            )
                                        }
                                    }
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

            // === CYTAT NA SAMYM DOLE ===
            Spacer(Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Cytat dnia", style = MaterialTheme.typography.titleMedium)

                        TextButton(onClick = { vm.loadQuote() }) {
                            Text("Odśwież")
                        }
                    }

                    Text(
                        text = quote ?: "Ładowanie...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Dialog potwierdzenia usunięcia
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Usuwanie zadania") },
                    text = { Text("Czy na pewno chcesz usunąć to zadanie?\n\n„$taskToDeleteTitle”") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val id = taskToDeleteId
                                if (id != null) vm.deleteTask(id)

                                showDeleteDialog = false
                                taskToDeleteId = null
                                taskToDeleteTitle = ""

                                scope.launch {
                                    snackbarHostState.showSnackbar("Usunięto zadanie")
                                }
                            }
                        ) {
                            Text("Usuń", color = Color(0xFFD32F2F))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDeleteDialog = false
                                taskToDeleteId = null
                                taskToDeleteTitle = ""
                            }
                        ) {
                            Text("Anuluj")
                        }
                    }
                )
            }
        }
    }
}
