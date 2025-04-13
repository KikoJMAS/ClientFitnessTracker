package com.example.clientfitnesstracker.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.clientfitnesstracker.data.db.entities.WorkoutSession
import com.example.clientfitnesstracker.viewmodel.WorkoutViewModel
import com.example.clientfitnesstracker.viewmodel.ClientViewModel

@Composable
fun ScheduleHomeScreen(clientViewModel: ClientViewModel, workoutViewModel: WorkoutViewModel, clientId: Int) {
    val context = LocalContext.current
    val sessions by workoutViewModel.sessions.collectAsState()


    var showDialog by remember { mutableStateOf(false) }
    var selectedSession by remember { mutableStateOf<WorkoutSession?>(null) }
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    LaunchedEffect(clientId) {
        workoutViewModel.loadSessions(clientId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = {
            selectedSession = null
            title = ""
            date = ""
            showDialog = true
        }) {
            Text("Add New Schedule")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sessions) { session ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            selectedSession = session
                            title = session.title
                            date = session.date
                            showDialog = true
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Title: ${session.title}")
                        Text("Date: ${session.date}")
                        Text("Created: ${formatTimestamp(session.createdAt)}")
                        session.updatedAt?.let {
                            Text("Updated: ${formatTimestamp(it)}")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { workoutViewModel.deleteSession(session) }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (selectedSession == null) "Add Schedule" else "Edit Schedule") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Date") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (title.isNotBlank() && date.isNotBlank()) {
                        if (selectedSession == null) {
                            workoutViewModel.addSession(
                                WorkoutSession(
                                    clientId = clientId,
                                    title = title,
                                    date = date
                                )
                            )
                            Toast.makeText(context, "Schedule added", Toast.LENGTH_SHORT).show()
                        } else {
                            workoutViewModel.updateSession(
                                selectedSession!!.copy(title = title, date = date)
                            )
                            Toast.makeText(context, "Schedule updated", Toast.LENGTH_SHORT).show()
                        }
                        showDialog = false
                    } else {
                        Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun formatTimestamp(timestamp: Long): String {
    return java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
        .format(java.util.Date(timestamp))
}
