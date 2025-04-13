package com.example.clientfitnesstracker.data.db.dao

import androidx.room.*
import com.example.clientfitnesstracker.data.db.entities.WorkoutSession
import com.example.clientfitnesstracker.data.db.entities.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout_sessions WHERE clientId = :clientId")
    fun getSessionsForClient(clientId: Int): Flow<List<WorkoutSession>>

    @Insert
    suspend fun insertSession(session: WorkoutSession): Long

    @Insert
    suspend fun insertExercises(exercises: List<Exercise>)

    @Update
    suspend fun updateSession(session: WorkoutSession)

    @Delete
    suspend fun deleteSession(session: WorkoutSession)


    @Query("SELECT * FROM exercises WHERE sessionId = :sessionId")
    fun getExercisesForSession(sessionId: Int): Flow<List<Exercise>>
}