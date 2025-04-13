package com.example.clientfitnesstracker.model
import com.example.clientfitnesstracker.model.WorkoutEntry
data class Client(
    val id: Int,
    val name: String,
    val age: Int,
    val fitnessLevel: String,
    val goals: String,
    val history: List<WorkoutEntry> = emptyList()
)

