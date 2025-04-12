package com.example.clientfitnesstracker.ui.screens.client

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.clientfitnesstracker.ui.components.WorkoutSummaryCard
import com.example.clientfitnesstracker.model.Client
import com.example.clientfitnesstracker.model.WorkoutEntry
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHistoryScreen(
    client: Client,
    onBack: () -> Unit,
    onAddWorkout: (WorkoutEntry) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${client.name}'s Workouts") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Workout")
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .fillMaxSize()
        ) {
            Text("Age: ${client.age}", style = MaterialTheme.typography.bodyMedium)
            Text("Fitness Level: ${client.fitnessLevel}", style = MaterialTheme.typography.bodyMedium)
            Text("Goal: ${client.goals}", style = MaterialTheme.typography.bodyMedium)
            WorkoutSummaryCard(client = client)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Workout History", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Workout History", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (client.history.isEmpty()) {
                Text("No workouts logged yet.")
            } else {
                LazyColumn {
                    items(client.history) { workout ->
                        WorkoutCard(workout)
                    }
                }
            }
        }

        if (showDialog) {
            AddWorkoutDialog(
                onDismiss = { showDialog = false },
                onSave = {
                    onAddWorkout(it)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun WorkoutCard(entry: WorkoutEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Date: ${entry.date}", style = MaterialTheme.typography.bodyMedium)
            Text("Type: ${entry.type}")
            entry.durationMinutes?.let {
                Text("Duration: $it min")
            }
            if (entry.notes.isNotBlank()) {
                Text("Notes: ${entry.notes}")
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutDialog(
    onDismiss: () -> Unit,
    onSave: (WorkoutEntry) -> Unit
) {
    var type by remember { mutableStateOf("Cardio") }
    var duration by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val types = listOf("Cardio", "Strength", "Mobility", "Stretching")

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                onSave(
                    WorkoutEntry(
                        date = today,
                        type = type,
                        durationMinutes = duration.toIntOrNull(),
                        notes = notes
                    )
                )
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Log Workout") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Type dropdown
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = type,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Workout Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        types.forEach {
                            DropdownMenuItem(text = { Text(it) }, onClick = {
                                type = it
                                expanded = false
                            })
                        }
                    }
                }

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duration (min)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") }
                )
            }
        }
    )
}
