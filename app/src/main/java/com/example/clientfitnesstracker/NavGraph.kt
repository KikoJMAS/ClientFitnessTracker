package com.example.clientfitnesstracker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.clientfitnesstracker.ui.*

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "schedule", modifier = modifier) {
        composable("calendar") { CalendarScreen() }
        composable("clients") { ClientListScreen() }
        composable("schedule") { ScheduleHomeScreen() }
        composable("settings") { SettingsScreen() }
    }
}
