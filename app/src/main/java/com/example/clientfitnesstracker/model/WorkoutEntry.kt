package com.example.clientfitnesstracker.model

data class WorkoutEntry(
    val date: String,              // Format: "2024-04-12"
    val type: String,              // e.g., "Cardio", "Strength"
    val durationMinutes: Int?,     // Optional
    val notes: String              // Optional
)
