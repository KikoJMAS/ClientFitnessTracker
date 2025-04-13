package com.example.clientfitnesstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientfitnesstracker.data.db.entities.WorkoutSession
import com.example.clientfitnesstracker.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WorkoutViewModel(private val repository: WorkoutRepository) : ViewModel() {

    private val _sessions = MutableStateFlow<List<WorkoutSession>>(emptyList())
    val sessions: StateFlow<List<WorkoutSession>> = _sessions.asStateFlow()

    fun loadSessions(clientId: Int) {
        viewModelScope.launch {
            repository.getSessionsForClient(clientId).collect {
                _sessions.value = it
            }
        }
    }

    fun addSession(session: WorkoutSession) = viewModelScope.launch {
        repository.insertSession(session)
    }

    fun updateSession(session: WorkoutSession) = viewModelScope.launch {
        repository.updateSession(session)
    }

    fun deleteSession(session: WorkoutSession) = viewModelScope.launch {
        repository.deleteSession(session)
    }
}
