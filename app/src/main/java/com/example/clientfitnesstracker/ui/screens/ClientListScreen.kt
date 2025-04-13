package com.example.clientfitnesstracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clientfitnesstracker.data.db.entities.Client
import com.example.clientfitnesstracker.ui.components.ClientCard
import com.example.clientfitnesstracker.viewmodel.ClientViewModel
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.navigation.NavHostController

@Composable
fun ClientListScreen(viewModel: ClientViewModel , navController: NavHostController) {
    val clientList by viewModel.clients.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Client List",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Text("Add Client +")
        }

        if (clientList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No clients found.")
            }
        } else {
            LazyColumn {
                items(clientList) { client ->
                    ClientCard(
                        client = client,
                        onDelete = { viewModel.deleteClient(client) },
                        onClick = {
                            navController.navigate("schedule/${client.id}")
                        }
                        )
                }
            }
        }

        if (showDialog) {
            AddClientDialog(
                onDismiss = { showDialog = false },
                onSave = { client ->
                    viewModel.addClient(client)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AddClientDialog(onDismiss: () -> Unit, onSave: (Client) -> Unit) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var dateJoined by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var fitnessLevel by remember { mutableStateOf("Beginner") }
    var goal by remember { mutableStateOf("Weight Loss") }

    val fitnessOptions = listOf("Beginner", "Intermediate", "Expert")
    val goalOptions = listOf("Weight Loss", "Muscle Gain", "Endurance", "Flexibility", "General Fitness")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Client") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") })
                OutlinedTextField(value = dateJoined, onValueChange = { dateJoined = it }, label = { Text("Date Joined") })
                OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") })
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })

                DropdownSelector(label = "Fitness Level", options = fitnessOptions, selected = fitnessLevel) {
                    fitnessLevel = it
                }

                DropdownSelector(label = "Goal", options = goalOptions, selected = goal) {
                    goal = it
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isNotBlank() && age.isNotBlank() && phone.isNotBlank()) {
                    onSave(
                        Client(
                            name = name,
                            age = age.toIntOrNull() ?: 0,
                            dateJoined = dateJoined,
                            address = address,
                            phone = phone,
                            fitnessLevel = fitnessLevel,
                            goal = goal
                        )
                    )
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun DropdownSelector(label: String, options: List<String>, selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label)
        Box {
            OutlinedTextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}



