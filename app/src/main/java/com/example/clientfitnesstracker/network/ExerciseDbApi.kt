package com.example.clientfitnesstracker.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ExerciseDbApi {
    @GET("exercises")
    suspend fun getExercises(@Query("category") category: String): List<String>
}
