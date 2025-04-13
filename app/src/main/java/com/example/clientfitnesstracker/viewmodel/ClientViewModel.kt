package com.example.clientfitnesstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientfitnesstracker.data.db.entities.Client
import com.example.clientfitnesstracker.repository.ClientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClientViewModel(private val repository: ClientRepository) : ViewModel() {

    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    val clients: StateFlow<List<Client>> = _clients

    init {
        viewModelScope.launch {
            repository.getClients().collect { list ->
                _clients.value = list
                if (list.isEmpty()) {
                    insertDefaultClient()
                }
            }
        }
    }

    fun addClient(client: Client) {
        viewModelScope.launch {
            repository.addClient(client)
        }
    }

    fun deleteClient(client: Client) {
        viewModelScope.launch {
            repository.deleteClient(client)
        }
    }

    private fun insertDefaultClient() {
        viewModelScope.launch {
            repository.addClient(
                Client(
                    name = "Sarah Thompson",
                    age = 30,
                    dateJoined = "2024-01-01",
                    address = "123 Fitness Ave",
                    phone = "9876543210",
                    fitnessLevel = "Intermediate",
                    goal = "General Fitness"
                )
            )
        }
    }
}
