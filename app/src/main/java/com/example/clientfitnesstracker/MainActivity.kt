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
import androidx.navigation.compose.rememberNavController
import com.example.clientfitnesstracker.ui.theme.ClientFitnessTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClientFitnessApp()
        }
    }
}

@Preview
@Composable
fun ClientFitnessApp() {
    ClientFitnessTrackerTheme {
        val navController = rememberNavController()
        val bottomNavItems = listOf(
            BottomNavItem("clients", "Clients", Icons.Default.AccountBox),
            BottomNavItem("calendar", "Calendar", Icons.Default.DateRange),
            BottomNavItem("schedule", "Schedule", Icons.Default.Home),
            BottomNavItem("settings", "Settings", Icons.Default.Settings)
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { BottomNavBar(navController, bottomNavItems) }
        ) { innerPadding ->
            NavigationGraph(navController, modifier = Modifier.padding(innerPadding))
        }
    }
}