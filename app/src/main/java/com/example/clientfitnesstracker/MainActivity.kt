package com.example.clientfitnesstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.clientfitnesstracker.ui.theme.ClientFitnessTrackerTheme
import com.example.clientfitnesstracker.viewmodel.ClientViewModel
import android.app.Application
import com.example.clientfitnesstracker.data.db.AppDatabase
import com.example.clientfitnesstracker.repository.ClientRepository
import com.example.clientfitnesstracker.viewmodel.ClientViewModelFactory
import com.example.clientfitnesstracker.viewmodel.WorkoutViewModel
import com.example.clientfitnesstracker.viewmodel.WorkoutViewModelFactory
import com.example.clientfitnesstracker.repository.WorkoutRepository



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClientFitnessApp(application)
        }
    }
}

@Composable
fun ClientFitnessApp(application: Application) {
    ClientFitnessTrackerTheme {
        val navController = rememberNavController()

        // Build Room database and repository manually
        val context = application.applicationContext
        val database = AppDatabase.getDatabase(context)

        val clientRepository = ClientRepository(database.clientDao())
        val workoutRepository = WorkoutRepository(database.workoutDao())

        // ViewModel factories
        val clientVMFactory = ClientViewModelFactory(clientRepository)
        val workoutVMFactory = WorkoutViewModelFactory(workoutRepository)

        // ViewModels
        val clientViewModel: ClientViewModel = viewModel(factory = clientVMFactory)
        val workoutViewModel: WorkoutViewModel = viewModel(factory = workoutVMFactory)


        val bottomNavItems = listOf(
            BottomNavItem("clients", "Clients", Icons.Default.AccountBox),
            BottomNavItem("calendar", "Calendar", Icons.Default.DateRange),
            BottomNavItem("schedule/1", "Schedule", Icons.Default.Home),
            BottomNavItem("settings", "Settings", Icons.Default.Settings)
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { BottomNavBar(navController, bottomNavItems) }
        ) { innerPadding ->
            NavigationGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                clientViewModel = clientViewModel,
                workoutViewModel = workoutViewModel
            )
        }
    }
}
