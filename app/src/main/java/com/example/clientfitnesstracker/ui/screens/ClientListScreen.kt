package com.example.clientfitnesstracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clientfitnesstracker.model.Client
import com.example.clientfitnesstracker.viewmodel.ClientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListScreen(
    viewModel: ClientViewModel,
    onClientClick: (Client) -> Unit,
    onAddClient: () -> Unit
) {
    val clients = viewModel.clients

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Client Management") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClient) {
                Icon(Icons.Default.Add, contentDescription = "Add Client")
            }
        }
    ) { paddingValues ->
        if (clients.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No clients added yet.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(clients) { client ->
                    ClientCard(client = client, onClick = { onClientClick(client) })
                }
            }
        }
    }
}

@Composable
fun ClientCard(client: Client, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = client.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Age: ${client.age}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Fitness Level: ${client.fitnessLevel}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Goals: ${client.goals}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
