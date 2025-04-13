package com.example.clientfitnesstracker.repository

import com.example.clientfitnesstracker.data.db.dao.WorkoutDao
import com.example.clientfitnesstracker.data.db.entities.WorkoutSession
import com.example.clientfitnesstracker.data.db.entities.Exercise
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val workoutDao: WorkoutDao) {
    fun getSessionsForClient(clientId: Int): Flow<List<WorkoutSession>> =
        workoutDao.getSessionsForClient(clientId)

    fun getExercisesForSession(sessionId: Int): Flow<List<Exercise>> =
        workoutDao.getExercisesForSession(sessionId)

    suspend fun insertSession(session: WorkoutSession): Long =
        workoutDao.insertSession(session)

    suspend fun updateSession(session: WorkoutSession) =
        workoutDao.updateSession(session)

    suspend fun deleteSession(session: WorkoutSession) =
        workoutDao.deleteSession(session)

    suspend fun insertExercises(exercises: List<Exercise>) =
        workoutDao.insertExercises(exercises)
}
