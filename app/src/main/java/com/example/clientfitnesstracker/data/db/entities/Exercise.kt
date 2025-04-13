package com.example.clientfitnesstracker.data.db.entities


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: Int,
    val name: String,
    val duration: Int, // in minutes
    val sets: Int,
    val reps: Int,
    val weight: Float = 0f,
    val distance: Float = 0f
)
