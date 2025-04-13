package com.example.clientfitnesstracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clientfitnesstracker.data.db.entities.Client

@Composable
fun ClientCard(client: Client, onDelete: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Name: ${client.name}", style = MaterialTheme.typography.titleMedium)
            Text("Age: ${client.age}")
            Text("Joined: ${client.dateJoined}")
            Text("Address: ${client.address}")
            Text("Phone: ${client.phone}")
            Text("Fitness Level: ${client.fitnessLevel}")
            Text("Goal: ${client.goal}")
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onDelete) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}


