package com.example.clientfitnesstracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Appointment(
    val clientName: String,
    val hour: String,
    val exercises: List<String>
)

@Composable
fun AppointmentList(appointments: List<Appointment>) {
    var selectedAppointment by remember { mutableStateOf<Appointment?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(appointments) { appointment ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { selectedAppointment = appointment },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Client: ${appointment.clientName}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Time: ${appointment.hour}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    selectedAppointment?.let { appointment ->
        AlertDialog(
            onDismissRequest = { selectedAppointment = null },
            title = { Text("Exercises for ${appointment.clientName}") },
            text = {
                Column {
                    appointment.exercises.forEach { exercise ->
                        Text("- $exercise", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedAppointment = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleHomeScreen() {
    val sampleAppointments = listOf(
        Appointment("John Doe", "10:00 AM", listOf("Push-ups", "Squats", "Plank")),
        Appointment("Jane Smith", "11:30 AM", listOf("Yoga", "Stretching")),
        Appointment("Alice Brown", "2:00 PM", listOf("Deadlifts", "Bench Press"))
    )

    Column {
        AppointmentList(appointments = sampleAppointments)
    }
}
