package com.example.zadaniok.ui

sealed class Routes(val route: String) {
    data object TaskList : Routes("task_list")
    data object AddEdit : Routes("add_edit")
    data object Stats : Routes("stats")
}