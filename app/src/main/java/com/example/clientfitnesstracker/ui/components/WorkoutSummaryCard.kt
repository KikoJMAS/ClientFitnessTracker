package com.example.clientfitnesstracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clientfitnesstracker.model.Client
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WorkoutSummaryCard(client: Client) {
    val history = client.history
    val totalWorkouts = history.size
    val totalMinutes = history.sumOf { it.durationMinutes ?: 0 }

    val mostFrequentType = history
        .groupingBy { it.type }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key ?: "N/A"

    val lastWorkoutDate = history.maxByOrNull {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.date)
    }?.date ?: "None"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Workout Summary", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Total workouts: $totalWorkouts")
            Text("Total minutes: $totalMinutes")
            Text("Most common type: $mostFrequentType")
            Text("Last workout: $lastWorkoutDate")
        }
    }
}

