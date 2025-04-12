package com.example.clientfitnesstracker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.clientfitnesstracker.model.Client
import com.example.clientfitnesstracker.model.WorkoutEntry
import com.example.clientfitnesstracker.ui.CalendarScreen
import com.example.clientfitnesstracker.ui.ScheduleHomeScreen
import com.example.clientfitnesstracker.ui.SettingsScreen
import com.example.clientfitnesstracker.ui.screens.AddEditClientScreen
import com.example.clientfitnesstracker.ui.screens.ClientListScreen
import com.example.clientfitnesstracker.ui.screens.client.WorkoutHistoryScreen
import com.example.clientfitnesstracker.viewmodel.ClientViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val clientViewModel: ClientViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "clients",
        modifier = modifier
    ) {
        // Client list screen
        composable("clients") {
            ClientListScreen(
                viewModel = clientViewModel,
                onClientClick = { client ->
                    navController.navigate("workoutHistory/${client.id}")
                },
                onAddClient = {
                    navController.navigate("addClient")
                }
            )
        }

        // Add new client
        composable("addClient") {
            AddEditClientScreen(
                onSave = {
                    clientViewModel.addClient(it)
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        // Edit existing client
        composable("editClient/{clientId}") { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId")?.toIntOrNull()
            val client = clientViewModel.getClientById(clientId ?: -1)

            client?.let {
                AddEditClientScreen(
                    existingClient = it,
                    onSave = { updatedClient ->
                        clientViewModel.updateClient(updatedClient)
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    },
                    onDelete = { clientToDelete ->
                        clientViewModel.deleteClient(clientToDelete)
                        navController.popBackStack()
                    }
                )
            }
        }

        // Workout history for a specific client
        composable("workoutHistory/{clientId}") { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId")?.toIntOrNull()
            val client = clientViewModel.getClientById(clientId ?: -1)

            client?.let {
                WorkoutHistoryScreen(
                    client = it,
                    onBack = { navController.popBackStack() },
                    onAddWorkout = { entry: WorkoutEntry ->
                        clientViewModel.addWorkoutToClient(client.id, entry)
                    }
                )
            }
        }

        // Other screens
        composable("calendar") { CalendarScreen() }
        composable("schedule") { ScheduleHomeScreen() }
        composable("settings") { SettingsScreen() }
    }
}
