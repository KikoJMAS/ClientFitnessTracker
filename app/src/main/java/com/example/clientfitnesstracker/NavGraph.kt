package com.example.clientfitnesstracker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.clientfitnesstracker.ui.screens.CalendarScreen
import com.example.clientfitnesstracker.ui.screens.ClientListScreen
import com.example.clientfitnesstracker.ui.screens.ScheduleHomeScreen
import com.example.clientfitnesstracker.ui.screens.SettingsScreen
import com.example.clientfitnesstracker.viewmodel.ClientViewModel
import com.example.clientfitnesstracker.viewmodel.WorkoutViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    clientViewModel: ClientViewModel,
    workoutViewModel: WorkoutViewModel
) {
    NavHost(navController = navController, startDestination = "clients", modifier = modifier) {
        composable("calendar") {
            CalendarScreen()
        }
        composable("clients") {
            ClientListScreen(viewModel = clientViewModel, navController = navController)
        }
        composable(
            route = "schedule/{clientId}",
            arguments = listOf(navArgument("clientId") { type = NavType.IntType })
        ) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getInt("clientId") ?: -1
            ScheduleHomeScreen(
                clientViewModel = clientViewModel,
                workoutViewModel = workoutViewModel,
                clientId = clientId
            )
        }

        composable("settings") {
            SettingsScreen()
        }
    }
}
