package com.example.clientfitnesstracker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val age: Int,
    val dateJoined: String,
    val address: String,
    val phone: String,
    val fitnessLevel: String,
    val goal: String
)
