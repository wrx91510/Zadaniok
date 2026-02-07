package com.example.zadaniok.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zadaniok.ui.screens.AddEditScreen
import com.example.zadaniok.ui.screens.StatsScreen
import com.example.zadaniok.ui.screens.TaskListScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.TaskList.route
    ) {
        composable(Routes.TaskList.route) {
            TaskListScreen(
                onAddClick = { navController.navigate(Routes.AddEdit.route) },
                onStatsClick = { navController.navigate(Routes.Stats.route) }
            )
        }
        composable(Routes.AddEdit.route) {
            AddEditScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.Stats.route) {
            StatsScreen(onBack = { navController.popBackStack() })
        }
    }
}
